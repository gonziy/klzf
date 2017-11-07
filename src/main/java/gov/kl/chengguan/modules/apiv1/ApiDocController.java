package gov.kl.chengguan.modules.apiv1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.DATE;

import org.activiti.engine.TaskService;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.util.ByteSource;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.security.Digests;
import gov.kl.chengguan.common.utils.Encodes;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.act.entity.Act;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.act.utils.ActUtils;
import gov.kl.chengguan.modules.apiv1.ApiOaController.ApiOaCase;
import gov.kl.chengguan.modules.cms.dao.BaseArticleDao;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.oa.dao.OaDoc3Dao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.entity.OaDoc3;
import gov.kl.chengguan.modules.oa.service.OaDoc3RoutingService;
import gov.kl.chengguan.modules.sys.dao.BaseUserDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.security.SystemAuthorizingRealm.Principal;
import gov.kl.chengguan.modules.sys.service.PushService;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.service.UserService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;



@RestController
@RequestMapping(value = "/apiv1")
public class ApiDocController  extends BaseController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private OaDoc3Dao oaDoc3Dao;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private OaDoc3RoutingService doc3Service;
	
	@RequestMapping(value = {"oa/doc/test"})
	public void test(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		PrintWriter out = null;
		jsonObject.put("msg", "missing url, attachLinks is null, please put a parameter");
		jsonObject.put("code", 41010);
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(jsonObject.toJSONString());
		out.flush();
	}
	/**
	 * 流程审批
	 * @param request
	 * @param response
	 * @param reqData
	 */
	@RequestMapping(value = {"oa/doc/approve"})
	public void docToNextStep(HttpServletRequest request, HttpServletResponse response, @RequestBody String reqData) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String dataString = reqData;
		PrintWriter out;
		if(dataString != null)
		{
			JSONObject json = JSONObject.parseObject(dataString);
			JSONObject userJson = json.getJSONObject("user");
			JSONObject docJson = json.getJSONObject("document");
//			java.util.List<Act> todoList = actTaskService.findTodoTasks(userJson.getString("userId"), ActUtils.PD_DOC3_ROUTING[0]);
			java.util.List<OaDoc3> todoList =doc3Service.findTodoTasks(userJson.getString("userId"));
			OaDoc3 model = null;
			if(todoList.size()>0)
			{
//				for (Act act : todoList) {
//					String businessId= act.getBusinessId();
//					businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
//					OaDoc3 oaDoc3 = oaDoc3Dao.get(businessId);
//					if(oaDoc3.getId().equals(docJson.getString("id"))){
//						model = oaDoc3;
//						model.setAct(act);
//						model.setTask(act.getTask());
//						break;
//					}
//				}
				for (OaDoc3 doc : todoList) {
					if(doc.getId().equals(docJson.getString("id"))){
						model = doc;
						break;
					}
				}
			}
			if (model==null) {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "failed");
				jsonObject.put("remark", "you don't have permission to approve");
			} else{
				
			}
			Integer stage = docJson.getInteger("progressCode");
			if(stage==1){
				//办公室主任审批
				
				try {
					model.setLeaderId(docJson.getString("approvalId"));
					model.setDueDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59"));
					User updater = userDao.get(userJson.getString("userId"));
					model.setUpdateBy(updater);
					model.setUpdateDate(new Date());
					doc3Service.mobileSaveStep(model, 1);
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "success");
					jsonObject.put("remark", "");
				} catch (Exception e) {
				}

			}else if(stage == 2){
				
			}else if(stage == 3){
				
			}else if(stage == 4){
				
			}
			
			
			
			
		}else {

			jsonObject.put("msg", "data is null");
			jsonObject.put("code", 44004);
		}
		try {
			out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {

			}
		}
	}
	
	/**
	 * 创建公文流转
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/doc/create"})
	public void docCreate(HttpServletRequest request, HttpServletResponse response, @RequestBody String reqData) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String dataString = reqData;
		PrintWriter out;
		if (dataString != null) {
			JSONObject json = JSONObject.parseObject(dataString);
			JSONObject userJson = json.getJSONObject("user");
			JSONObject docJson = json.getJSONObject("document");
			
			if(docJson.getString("title")==null || docJson.getString("title").isEmpty()){
				jsonObject.put("msg", "missing url, title is null, please put a parameter");
				jsonObject.put("code", 41010);
				try {
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			if(docJson.getString("files")==null || docJson.getString("files").isEmpty()){
				jsonObject.put("msg", "missing url, files is null, please put a parameter");
				jsonObject.put("code", 41010);
				try {
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			if(userJson.getString("userId")==null || userJson.getString("userId").isEmpty()){
				jsonObject.put("msg", "missing url, userId is null, please put a parameter");
				jsonObject.put("code", 41010);
				try {
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			
			OaDoc3 doc3 = new OaDoc3();
			doc3.setAttachLinks(docJson.getString("files"));
			User currorUser = userDao.get(userJson.getString("userId"));
			doc3.setCreateBy(currorUser);
			doc3.setCreateDate(new Date());
			doc3.setDocTitle(docJson.getString("title"));
			doc3.setUpdateBy(currorUser);
			doc3.setUpdateDate(new Date());
			doc3Service.mobileSave(userJson.getString("userId"), doc3);

			jsonObject.put("msg", "success");
			jsonObject.put("code", 0);
			jsonObject.put("result",  "success");
			
			
		}
		else {
			jsonObject.put("msg", "data is null");
			jsonObject.put("code", 44004);
		}
		try {
			out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {

			}
		}
			
	}
	
	@RequestMapping(value = {"oa/doc/todolist"})
	public void getDocToDoList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String userId = request.getParameter("user_id");
		PrintWriter out = null;
		if(userId==null || userId.isEmpty()){
			jsonObject.put("msg", "missing url, page is null, please put a parameter");
			jsonObject.put("code", 41010);
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		java.util.List<OaDoc3> list = doc3Service.findTodoTasks(userId);
		if(list == null){
			jsonObject.put("msg", "data is null");
			jsonObject.put("code", 44004);
		}else {
			java.util.List<ApiDoc3> results = new ArrayList<ApiDoc3>();
			com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
			for (OaDoc3 doc : list) {
				results.add(ToApiDoc3(doc));
			}
			jsonObject.put("msg", "success");
			jsonObject.put("code", 0);
			jsonData.put("data", results);
			jsonObject.put("data", JSONObject.toJSON(jsonData));
			try {
				out = response.getWriter();
			} catch (IOException e) {
				jsonObject.put("msg", "system error");
				jsonObject.put("code", -1);
				try {
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();
				} catch (IOException e1) {
				
				}
			}
		}
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(jsonObject.toJSONString());
		out.flush();
	}
	/**
	 * 公文列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/doc/list"})
	public void getDocList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String pageIndex = request.getParameter("page");
		PrintWriter out = null;
		if(pageIndex==null || pageIndex.isEmpty()){
			jsonObject.put("msg", "missing url, page is null, please put a parameter");
			jsonObject.put("code", 41010);
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		OaDoc3 oaCaseWhere = new OaDoc3();
		Page<OaDoc3> pageWhere = new Page<OaDoc3>(request, response);
		pageWhere.setPageSize(10);
		pageWhere.setPageNo(Integer.parseInt(pageIndex));
		Page<OaDoc3> page = doc3Service.findPage(pageWhere, oaCaseWhere); 
		java.util.List<OaDoc3> list = page.getList();
		if(list == null){
			jsonObject.put("msg", "data is null");
			jsonObject.put("code", 44004);
		}else {
			com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
			jsonData.put("pagesize", 10);
			jsonData.put("pagecount", page.getLast());
			java.util.List<ApiDoc3> results = new ArrayList<ApiDoc3>();
			for (OaDoc3 doc : list) {
				results.add(ToApiDoc3(doc));
			}
			jsonObject.put("msg", "success");
			jsonObject.put("code", 0);
			jsonData.put("data", results);
			jsonObject.put("data", JSONObject.toJSON(jsonData));
			try {
				out = response.getWriter();
			} catch (IOException e) {
				jsonObject.put("msg", "system error");
				jsonObject.put("code", -1);
				try {
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();
				} catch (IOException e1) {
				
				}
			}
		}
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(jsonObject.toJSONString());
		out.flush();
		
	}
	/**
	 * 公文获取
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/doc/get"})
	public void getDoc(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		com.alibaba.fastjson.JSONObject dataObject = new com.alibaba.fastjson.JSONObject();
		String id = request.getParameter("id");
		PrintWriter out;

			
			if(id==null || id.isEmpty()){
				jsonObject.put("msg", "missing url, id is null, please put a parameter");
				jsonObject.put("code", 41010);
				try {
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			OaDoc3 oaDoc3 = doc3Service.get(id);
			if(oaDoc3!=null){
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("data", JSONObject.toJSON(ToApiDoc3(oaDoc3)));
			}
			else {

				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
			}
			
		try {
			out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {

			}
		}
			
	}
	
	/**
	 * 转换
	 * @param doc
	 * @return
	 */
	public ApiDoc3 ToApiDoc3(OaDoc3 doc)
	{
		ApiDoc3 apiDoc3 = new ApiDoc3();
		apiDoc3.setFiles(doc.getAttachLinks());
		apiDoc3.setDocTitle(doc.getDocTitle());
		apiDoc3.setDRStage(doc.getDRStage());
		apiDoc3.setDueDate(doc.getDueDate());
		apiDoc3.setId(doc.getId());
		apiDoc3.setLeaderApproveDate(doc.getLeaderApproveDate());
		apiDoc3.setLeaderId(doc.getLeaderId());
		apiDoc3.setLeaderOption(doc.getLeaderOption());
		apiDoc3.setOfficeHeaderApproval(doc.isOfficeHeaderApproval());
		apiDoc3.setOfficeHeaderApproveDate(doc.getOfficeHeaderApproveDate());
		apiDoc3.setOfficeHeaderOption(doc.getOfficeHeaderOption());
		apiDoc3.setReviewersIDs(doc.getReviewersIDs());
		apiDoc3.setReviewersIDs1(doc.getReviewersIDs1());
		return apiDoc3;
	}

	public class ApiDoc3
	{
		private String id;
		// 发文的标题
		private String docTitle;
		// 到公文的链接，可能一次发布多个文件，用;分割
		private String files;
		// 办公室领导意见
		private String officeHeaderOption;
		private boolean officeHeaderApproval;
		private Date officeHeaderApproveDate;
		// 到期日期
		private Date dueDate;

		// 审阅领导
		private String leaderId;
		private String leaderOption;
		private Date leaderApproveDate;
		// 公文审阅人ID列表，用;分割
		private String reviewersIDs;
		private String reviewersIDs1;
		private String DRStage;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getDocTitle() {
			return docTitle;
		}
		public void setDocTitle(String docTitle) {
			this.docTitle = docTitle;
		}
		public String getFiles() {
			return files;
		}
		public void setFiles(String attachLinks) {
			this.files = attachLinks;
		}
		public String getOfficeHeaderOption() {
			return officeHeaderOption;
		}
		public void setOfficeHeaderOption(String officeHeaderOption) {
			this.officeHeaderOption = officeHeaderOption;
		}
		public boolean isOfficeHeaderApproval() {
			return officeHeaderApproval;
		}
		public void setOfficeHeaderApproval(boolean officeHeaderApproval) {
			this.officeHeaderApproval = officeHeaderApproval;
		}
		public Date getOfficeHeaderApproveDate() {
			return officeHeaderApproveDate;
		}
		public void setOfficeHeaderApproveDate(Date officeHeaderApproveDate) {
			this.officeHeaderApproveDate = officeHeaderApproveDate;
		}
		public Date getDueDate() {
			return dueDate;
		}
		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		public String getLeaderId() {
			return leaderId;
		}
		public void setLeaderId(String leaderId) {
			this.leaderId = leaderId;
		}
		public String getLeaderOption() {
			return leaderOption;
		}
		public void setLeaderOption(String leaderOption) {
			this.leaderOption = leaderOption;
		}
		public Date getLeaderApproveDate() {
			return leaderApproveDate;
		}
		public void setLeaderApproveDate(Date leaderApproveDate) {
			this.leaderApproveDate = leaderApproveDate;
		}
		public String getReviewersIDs() {
			return reviewersIDs;
		}
		public void setReviewersIDs(String reviewersIDs) {
			this.reviewersIDs = reviewersIDs;
		}
		public String getReviewersIDs1() {
			return reviewersIDs1;
		}
		public void setReviewersIDs1(String reviewersIDs1) {
			this.reviewersIDs1 = reviewersIDs1;
		}
		public String getDRStage() {
			return DRStage;
		}
		public void setDRStage(String dRStage) {
			DRStage = dRStage;
		}
		
	}
}