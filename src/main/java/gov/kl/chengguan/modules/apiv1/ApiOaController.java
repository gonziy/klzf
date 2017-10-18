package gov.kl.chengguan.modules.apiv1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import gov.kl.chengguan.common.config.Global;
import gov.kl.chengguan.common.utils.FileUtils;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.act.entity.Act;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.oa.dao.OaCaseDao;
import gov.kl.chengguan.modules.oa.dao.OaCaseFieldsDao;
import gov.kl.chengguan.modules.oa.dao.OaFilesDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.entity.OaCaseFields;
import gov.kl.chengguan.modules.oa.entity.OaFiles;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.User;



@RestController
@RequestMapping(value = "/apiv1")
public class ApiOaController  extends BaseController {

	private static OaCaseFieldsDao caseFieldsDao = SpringContextHolder.getBean(OaCaseFieldsDao.class);
	private static OaCaseDao caseDao = SpringContextHolder.getBean(OaCaseDao.class);
	private static OaFilesDao filesDao = SpringContextHolder.getBean(OaFilesDao.class);
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	
	@Autowired
	private ActTaskService actTaskService;
	
	
	@RequestMapping(value = {"oa/case/list"})
	public void getCaseList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String status = request.getParameter("status");
		String userId = request.getParameter("user_id");
		if(status==null || status.isEmpty())
		{
			jsonObject.put("msg", "missing url, status is null");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if(userId==null || userId.isEmpty())
		{
			jsonObject.put("msg", "missing url, user_id is null");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			
			java.util.List<Act> acts = actTaskService.findTodoTasks(userId);
			java.util.List<ApiOaCase> results = new ArrayList<ApiOaCase>();
		
			for(Act act : acts)
			{
				
				String businessId= act.getBusinessId();
				businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
				
				OaCase oaCase = caseDao.get(businessId);
				if(oaCase != null){
					ApiOaCase apiOaCase = new ApiOaCase();
					
					apiOaCase.setId(oaCase.getId());
					apiOaCase.setCaseParties (oaCase.getCaseParties ());
					apiOaCase.setCaseLegalAgent(oaCase.getCaseLegalAgent());
					apiOaCase.setAddress(oaCase.getAddress());
					apiOaCase.setPhoneNumber(oaCase.getPhoneNumber());
					apiOaCase.setCaseDescription(oaCase.getCaseDescription());
					//apiOaCase.setCaseAttachmentLinks(oaCase.getCaseAttachmentLinks());
					apiOaCase.setCaseCheckResult(oaCase.getCaseCheckResult());
					apiOaCase.setCaseSource(oaCase.getCaseSource());
					apiOaCase.setAssigneeIds(oaCase.getAssigneeIds());
					apiOaCase.setNormCaseDescPart1(oaCase.getNormCaseDescPart1());
					apiOaCase.setNormCaseDescPart2(oaCase.getNormCaseDescPart2());
					apiOaCase.setInstitutionRegOption(oaCase.getInstitutionRegOption());
					apiOaCase.setInstitutionRegApproval((Boolean)oaCase.isInstitutionRegApproval());
					apiOaCase.setDeptLeaderRegOption(oaCase.getDeptLeaderRegOption());
					apiOaCase.setDeptLeaderRegApproval(oaCase.isDeptLeaderRegApproval());
					apiOaCase.setMainLeaderRegOption(oaCase.getMainLeaderRegOption());
					apiOaCase.setMainLeaderRegApproval(oaCase.isMainLeaderRegApproval());
					apiOaCase.setCaseRegStartDate(oaCase.getCaseRegStartDate());
					apiOaCase.setCaseRegEndDate(oaCase.getCaseRegEndDate());
					apiOaCase.setCaseSurveyEndDate(oaCase.getCaseSurveyEndDate());
					apiOaCase.setNormAssigneePenalOptPart1(oaCase.getNormAssigneePenalOptPart1());
					apiOaCase.setNormAssigneePenalOptPart2(oaCase.getNormAssigneePenalOptPart2());
					apiOaCase.setAssigneePenalOption(oaCase.getAssigneePenalOption());
					apiOaCase.setInstitutionPenalOption(oaCase.getInstitutionPenalOption());
					apiOaCase.setInstitutionPenalApproval(oaCase.getInstitutionPenalApproval());
					apiOaCase.setCaseMgtCenterPenalOption(oaCase.getCaseMgtCenterPenalOption());
					apiOaCase.setCaseMgtCenterPenalApproval(oaCase.getCaseMgtCenterPenalApproval());
					apiOaCase.setDeptLeaderPenalOption(oaCase.getDeptLeaderPenalOption());
					apiOaCase.setDeptLeaderPenalApproval(oaCase.getDeptLeaderPenalApproval());
					apiOaCase.setMainLeaderPenalOption(oaCase.getMainLeaderPenalOption());
					apiOaCase.setMainLeaderPenalApproval(oaCase.getMainLeaderPenalApproval());
					apiOaCase.setCasePenalStartDate(oaCase.getCasePenalStartDate());
					apiOaCase.setCasePenalEndDate(oaCase.getCasePenalEndDate());
					apiOaCase.setCaseStage(oaCase.getCaseStage());
					apiOaCase.setAssigneeCloseCaseOption(oaCase.getAssigneeCloseCaseOption());
					apiOaCase.setInstitutionCloseCaseOption(oaCase.getInstitutionCloseCaseOption());
					apiOaCase.setInstitutionCloseCaseApproval(oaCase.getInstitutionCloseCaseApproval());
					apiOaCase.setCaseMgtCenterCloseCaseOption(oaCase.getCaseMgtCenterCloseCaseOption());
					apiOaCase.setCaseMgtCenterCloseCaseApproval(oaCase.getCaseMgtCenterCloseCaseApproval());
					apiOaCase.setMainLeaderCloseCaseOption(oaCase.getMainLeaderCloseCaseOption());
					apiOaCase.setMainLeaderCloseCaseApproval(oaCase.getMainLeaderCloseCaseApproval());
					apiOaCase.setCaseCloseUpStartDate(oaCase.getCaseCloseUpStartDate());
					apiOaCase.setCaseCloseUpEndDate(oaCase.getCaseCloseUpEndDate());
					apiOaCase.setNormCaseDesc(oaCase.getNormCaseDesc());
					apiOaCase.setNormAssigneePenalOpt(oaCase.getNormAssigneePenalOpt());
					apiOaCase.setProcessInstanceId(oaCase.getProcessInstanceId());
					apiOaCase.setCaseQueryParty(oaCase.getCaseQueryParty());
					apiOaCase.setCaseQueryLegalAgent(oaCase.getCaseQueryLegalAgent());
					apiOaCase.setCaseQueryAddress(oaCase.getCaseQueryAddress());
					apiOaCase.setCaseQueryPhoneNumber(oaCase.getCaseQueryPhoneNumber());
					apiOaCase.setCaseQueryBrokeLaw (oaCase.getCaseQueryBrokeLaw ());
					apiOaCase.setCaseQueryPenal (oaCase.getCaseQueryPenal ());
					apiOaCase.setCaseQueryRegStartDateStart(oaCase.getCaseQueryRegStartDateStart());
					apiOaCase.setCaseQueryRegStartDateEnd(oaCase.getCaseQueryRegStartDateEnd());
					apiOaCase.setCaseQueryRegEndDateStart(oaCase.getCaseQueryRegEndDateStart());
					apiOaCase.setCaseQueryRegEndDateEnd(oaCase.getCaseQueryRegEndDateEnd());
					apiOaCase.setCaseQueryCloseDateStart(oaCase.getCaseQueryCloseDateStart());
					apiOaCase.setCaseQueryCloseDateEnd(oaCase.getCaseQueryCloseDateEnd());
					apiOaCase.setCaseQueryStage(oaCase.getCaseQueryStage());

					String strAssigneeNames = "";
					if(apiOaCase.getAssigneeIds()!=null && !apiOaCase.getAssigneeIds().isEmpty()){
						String[] assigneeIdsList = apiOaCase.getAssigneeIds().split(";");
						if(assigneeIdsList.length>0){
							for (String id : assigneeIdsList) {
								User user = userDao.get(id);
								if(user!=null){
									strAssigneeNames += user.getName() + ";";
								}
							}
							if(strAssigneeNames.endsWith(";")){
								strAssigneeNames = strAssigneeNames.substring(0,strAssigneeNames.length()-1);
							}
						}
					}
					apiOaCase.setAssigneeNames(strAssigneeNames);
					
					results.add(apiOaCase);
				}
			}
			
			
			if(results == null || results.isEmpty())
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(results));
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	

	@RequestMapping(value = {"oa/casefields/get"})
	public void getCaseFieldsInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String no = request.getParameter("no");
		if(no==null || no.isEmpty())
		{
			jsonObject.put("msg", "missing url, no is null, please put a parameter");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			OaCaseFields fields = caseFieldsDao.get(no);
			
			
			if(fields == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				ApiOaCaseFields apiFields = new ApiOaCaseFields();
				apiFields.setNo(fields.getNo());
				apiFields.setValueFirst(fields.getValueFirst());
				apiFields.setValueSecond(fields.getValueSecond());
				apiFields.setValueThird(fields.getValueThird());
				apiFields.setIntro(fields.getIntro());
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(apiFields));
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	@RequestMapping(value = {"oa/casefields/insert"})
	public void insertCaseFieldsInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String no = request.getParameter("no");
		String v1 = request.getParameter("v1");
		String v2 = request.getParameter("v2");
		String v3 = request.getParameter("v3");
		String intro = request.getParameter("intro");
		
		if(no==null || no.isEmpty())
		{
			jsonObject.put("msg", "missing url, no is null, please put a parameter");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			OaCaseFields fields = new OaCaseFields();
			fields.setNo(no);
			fields.setValueFirst(v1);
			fields.setValueSecond(v2);
			fields.setValueThird(v3);
			fields.setIntro(intro);
			fields.setCreateBy(new User());
			fields.setUpdateBy(new User());
			fields.setCreateDate(new Date());
			fields.setUpdateDate(new Date());
			
			int result = caseFieldsDao.insert(fields);
			
			if(result == 0)
			{
				jsonObject.put("msg", "insert data failed");
				jsonObject.put("code", -1);
				
			}else {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "success");
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	@RequestMapping(value = {"oa/casefields/update"})
	public void updateCaseFieldsInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String no = request.getParameter("no");
		String v1 = request.getParameter("v1");
		String v2 = request.getParameter("v2");
		String v3 = request.getParameter("v3");
		String intro = request.getParameter("intro");
		
		if(no==null || no.isEmpty())
		{
			jsonObject.put("msg", "missing url, no is null, please put a parameter");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			OaCaseFields fields = caseFieldsDao.get(no);
			fields.setNo(no);
			if(v1!=null && !v1.isEmpty())
			{
				fields.setValueFirst(v1);
			}
			if(v2!=null && !v2.isEmpty())
			{
				fields.setValueSecond(v2);
			}
			if(v3!=null && !v3.isEmpty())
			{
				fields.setValueThird(v3);
			}
			if(intro!=null && !intro.isEmpty())
			{
				fields.setIntro(intro);
			}			
			fields.setUpdateBy(new User());
			fields.setUpdateDate(new Date());
			
			int result = caseFieldsDao.update(fields);
			
			
			
			if(result == 0)
			{
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "failed");
				
			}else {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "success");
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	@RequestMapping(value = {"oa/files/get"})
	public void getFileInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String id = request.getParameter("id");
		if(id==null || id.isEmpty())
		{
			jsonObject.put("msg", "missing url, id is null, please put a parameter");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			OaFiles file = filesDao.get(id);
			
			
			if(file == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				ApiOaFiles apiFile = new ApiOaFiles();
				apiFile.setFileName(file.getFileName());
				apiFile.setFilePath(file.getFilePath());
				apiFile.setGroupId(file.getGroupId());
				apiFile.setId(Long.parseLong(file.getId()));
				apiFile.setType(file.getType());
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(apiFile));
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	@RequestMapping(value = {"oa/files/list"})
	public void getFileList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String groupId = request.getParameter("group_id");
		try {
			OaFiles whereFiles = new OaFiles();
			if(groupId!=null && !groupId.isEmpty())
			{
				whereFiles.setGroupId(groupId);
			}
			java.util.List<OaFiles> files = filesDao.findAllList(whereFiles);
			
			if(files == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				java.util.List<ApiOaFiles> apiFiles = new ArrayList<ApiOaFiles>();
				for (OaFiles f : files) {
					ApiOaFiles apifile = new ApiOaFiles();
					apifile.setDel_flag(f.getDelFlag());
					apifile.setFileName(f.getFileName());
					apifile.setFilePath(f.getFilePath());
					apifile.setGroupId(f.getGroupId());
					apifile.setId(Long.parseLong(f.getId()));
					apifile.setType(f.getType());
					apiFiles.add(apifile);
				}
				
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(apiFiles));
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	@RequestMapping(value = {"oa/files/delete"})
	public void deleteFileInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String id = request.getParameter("id");
		if(id==null || id.isEmpty())
		{
			jsonObject.put("msg", "missing url, id is null, please put a parameter");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			OaFiles file = filesDao.get(id);
			
			if(file == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				file.setDelFlag("1");
				int result = filesDao.delete(file);
						
				if(result>0)
				{
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "success");					
				}else 
				{
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "failed");	
				}
				
				
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			
		}

	}
	
	@RequestMapping(value = { "oa/files/upload" })
	public void uploadFileInfo(HttpServletRequest request,
			HttpServletResponse response, MultipartFile[] files) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();

		String group_id = request.getParameter("group_id");
		String file_name = request.getParameter("file_name");
		if (files.length > 0) {
			for (MultipartFile file : files) {

				String filename = file.getOriginalFilename().toLowerCase();
				String fileExt = filename.substring(filename.lastIndexOf("."));
				String fileType = "";

				// 判断文件格式,非法格式禁止上传
				if (fileExt.equals(".jpg") || fileExt.equals(".jpeg")
						|| fileExt.equals(".png")) {
					fileType = "image";
				} else if (fileExt.equals(".mpeg") || fileExt.equals(".mpg")
						|| fileExt.equals(".dat") || fileExt.equals(".avi")
						|| fileExt.equals(".mov") || fileExt.equals(".asf")
						|| fileExt.equals(".wmv") || fileExt.equals(".3gp")
						|| fileExt.equals(".mkv") || fileExt.equals(".flv")
						|| fileExt.equals(".f4v") || fileExt.equals(".rmvb")) {
					fileType = "video";
				} else if (fileExt.equals(".mp3") || fileExt.equals(".wma")
						|| fileExt.equals(".arm") || fileExt.equals(".wave")
						|| fileExt.equals(".aiff") || fileExt.equals(".flac")
						|| fileExt.equals(".aac")) {
					fileType = "audio";
				} else if (fileExt.equals(".txt") || fileExt.equals(".doc")
						|| fileExt.equals(".docx") || fileExt.equals(".ppt")
						|| fileExt.equals(".pptx") || fileExt.equals(".xls")
						|| fileExt.equals(".xlsx") || fileExt.equals(".pdf")
						|| fileExt.equals(".rtf") || fileExt.equals(".odt")) {
					fileType = "document";
				} else {
					jsonObject.put("msg", "invalid file type");
					jsonObject.put("code", 40004);
					PrintWriter out;
					try {
						out = response.getWriter();
						out.print(jsonObject.toJSONString());
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				String folderName = new SimpleDateFormat("yyyyMMdd")
						.format(new Date());
				String realPath = Global.getUserfilesBaseDir()
						+ Global.USERFILES_BASE_URL + fileType + "/"
						+ folderName + "/";
				FileUtils.createDirectory(FileUtils.path(realPath));
				// 设置服务器上存储的文件名，并将文件转存
				String newFileName = new SimpleDateFormat("yyyyMMddHHmmss")
						.format(new Date())
						+ ((int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000)
						+ fileExt;
				String file_path = realPath + newFileName;
				try {
					file.transferTo(new File(file_path));
				} catch (Exception e) {
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "failed");
					jsonObject.put("remark", "save file failed");
					PrintWriter out;
					try {
						out = response.getWriter();
						out.print(jsonObject.toJSONString());
						out.flush();
					} catch (IOException e1) {

					}
					return;
				}

				// 如果不自定义文件名，则为上传的文件名
				if (file_name == null || file_name.isEmpty()) {
					file_name = filename
							.substring(0, filename.lastIndexOf("."));
				}
				if (group_id == null || group_id.isEmpty()) {
					jsonObject.put("msg", "missing url, group_id is null");
					jsonObject.put("code", 41010);
					PrintWriter out;
					try {
						out = response.getWriter();
						out.print(jsonObject.toJSONString());
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				String httpPath = "http://localhost:9090/day5"
						+ Global.USERFILES_BASE_URL + fileType + "/"
						+ folderName + "/" + newFileName;
				try {
					OaFiles oaFiles = new OaFiles();
					oaFiles.setCreateBy(new User());
					oaFiles.setCreateDate(new Date());
					oaFiles.setFileName(file_name);
					oaFiles.setFilePath(httpPath);
					oaFiles.setGroupId(group_id);
					oaFiles.setType(fileType);

					int result = filesDao.insert(oaFiles);
					if (result > 0) {
						jsonObject.put("msg", "success");
						jsonObject.put("code", 0);
						jsonObject.put("result", "success");
						jsonObject.put("path", httpPath);
					} else {
						jsonObject.put("msg", "success");
						jsonObject.put("code", 0);
						jsonObject.put("result", "failed");
						jsonObject.put("remark", "insert to database failed");
					}
					PrintWriter out;
					out = response.getWriter();
					out.print(jsonObject.toJSONString());
					out.flush();

				} catch (Exception e) {
					jsonObject.put("msg", "system error");
					jsonObject.put("code", -1);
					PrintWriter out;
					try {
						out = response.getWriter();
						out.print(jsonObject.toJSONString());
						out.flush();
					} catch (IOException e1) {

					}

				}

			}
		}
		
		
	}
	
	
	
	public class ApiOaCaseFields{

		private String no;
		private String valueFirst; //第1个关键字
		private String valueSecond; //第2个关键字
		private String valueThird; //第3个关键字
		private String intro;  //案情简介
		
		
		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getValueFirst() {
			return valueFirst;
		}

		public void setValueFirst(String valueFirst) {
			this.valueFirst = valueFirst;
		}

		public String getValueSecond() {
			return valueSecond;
		}

		public void setValueSecond(String valueSecond) {
			this.valueSecond = valueSecond;
		}

		public String getValueThird() {
			return valueThird;
		}

		public void setValueThird(String valueThird) {
			this.valueThird = valueThird;
		}

		public String getIntro() {
			return intro;
		}

		public void setIntro(String intro) {
			this.intro = intro;
		}
	}
	
	public class ApiOaFiles
	{
		private Long id;

		private String groupId; 
		private String fileName;
		private String filePath; 
		private String type;
		private String del_flag;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDel_flag() {
			return del_flag;
		}
		public void setDel_flag(String del_flag) {
			this.del_flag = del_flag;
		}
	}
	
	public class ApiOaCase
	{
		private String id;

		private String caseParties; 
		private String caseLegalAgent;
		private String address;
		private String  phoneNumber;
		private String caseDescription;
		private String caseAttachmentLinks;	
		private String caseCheckResult;
		private String caseSource;
		private String assigneeIds;
		private String assigneeNames;
		private String normCaseDescPart1;
		private String normCaseDescPart2;	
		private String institutionRegOption;
		private Boolean institutionRegApproval;	
		private String deptLeaderRegOption;
		private Boolean deptLeaderRegApproval;	
		private String mainLeaderRegOption;
		private Boolean mainLeaderRegApproval;
		private Date caseRegStartDate;
		private Date caseRegEndDate;
		private Date caseSurveyEndDate;
		private String normAssigneePenalOptPart1;
		private String normAssigneePenalOptPart2;
		private String assigneePenalOption;
		private String institutionPenalOption;
		private Boolean institutionPenalApproval;
		private String caseMgtCenterPenalOption;
		private Boolean caseMgtCenterPenalApproval;
		private String deptLeaderPenalOption;
		private Boolean deptLeaderPenalApproval;
		private String mainLeaderPenalOption;
		private Boolean mainLeaderPenalApproval;
		private Date casePenalStartDate;
		private Date casePenalEndDate;
		private Integer caseStage;
		private String assigneeCloseCaseOption;
		private String institutionCloseCaseOption;
		private Boolean institutionCloseCaseApproval;
		private String caseMgtCenterCloseCaseOption;
		private Boolean caseMgtCenterCloseCaseApproval;
		private String mainLeaderCloseCaseOption;
		private Boolean mainLeaderCloseCaseApproval;	
		private Date caseCloseUpStartDate;
		private Date caseCloseUpEndDate;
		private String normCaseDesc;
		private String normAssigneePenalOpt;	
		private String processInstanceId;
		private String caseQueryParty;	 
		private String caseQueryLegalAgent;	
		private String caseQueryAddress;	 
		private String caseQueryPhoneNumber;	 
		private String caseQueryBrokeLaw; 
		private String caseQueryPenal; 	
		private Date caseQueryRegStartDateStart;  
		private Date caseQueryRegStartDateEnd;  
		private Date caseQueryRegEndDateStart;  
		private Date caseQueryRegEndDateEnd;  
		private Date caseQueryCloseDateStart;  
		private Date caseQueryCloseDateEnd;
		private Integer caseQueryStage;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCaseParties() {
			return caseParties;
		}
		public void setCaseParties(String caseParties) {
			this.caseParties = caseParties;
		}
		public String getCaseLegalAgent() {
			return caseLegalAgent;
		}
		public void setCaseLegalAgent(String caseLegalAgent) {
			this.caseLegalAgent = caseLegalAgent;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getCaseDescription() {
			return caseDescription;
		}
		public void setCaseDescription(String caseDescription) {
			this.caseDescription = caseDescription;
		}
		public String getCaseAttachmentLinks() {
			return caseAttachmentLinks;
		}
		public void setCaseAttachmentLinks(String caseAttachmentLinks) {
			this.caseAttachmentLinks = caseAttachmentLinks;
		}
		public String getCaseCheckResult() {
			return caseCheckResult;
		}
		public void setCaseCheckResult(String caseCheckResult) {
			this.caseCheckResult = caseCheckResult;
		}
		public String getCaseSource() {
			return caseSource;
		}
		public void setCaseSource(String caseSource) {
			this.caseSource = caseSource;
		}
		public String getAssigneeIds() {
			return assigneeIds;
		}
		public void setAssigneeIds(String assigneeIds) {
			this.assigneeIds = assigneeIds;
		}
		public String getNormCaseDescPart1() {
			return normCaseDescPart1;
		}
		public void setNormCaseDescPart1(String normCaseDescPart1) {
			this.normCaseDescPart1 = normCaseDescPart1;
		}
		public String getNormCaseDescPart2() {
			return normCaseDescPart2;
		}
		public void setNormCaseDescPart2(String normCaseDescPart2) {
			this.normCaseDescPart2 = normCaseDescPart2;
		}
		public String getInstitutionRegOption() {
			return institutionRegOption;
		}
		public void setInstitutionRegOption(String institutionRegOption) {
			this.institutionRegOption = institutionRegOption;
		}
		public Boolean getInstitutionRegApproval() {
			return institutionRegApproval;
		}
		public void setInstitutionRegApproval(Boolean institutionRegApproval) {
			this.institutionRegApproval = institutionRegApproval;
		}
		public String getDeptLeaderRegOption() {
			return deptLeaderRegOption;
		}
		public void setDeptLeaderRegOption(String deptLeaderRegOption) {
			this.deptLeaderRegOption = deptLeaderRegOption;
		}
		public Boolean getDeptLeaderRegApproval() {
			return deptLeaderRegApproval;
		}
		public void setDeptLeaderRegApproval(Boolean deptLeaderRegApproval) {
			this.deptLeaderRegApproval = deptLeaderRegApproval;
		}
		public String getMainLeaderRegOption() {
			return mainLeaderRegOption;
		}
		public void setMainLeaderRegOption(String mainLeaderRegOption) {
			this.mainLeaderRegOption = mainLeaderRegOption;
		}
		public Boolean getMainLeaderRegApproval() {
			return mainLeaderRegApproval;
		}
		public void setMainLeaderRegApproval(Boolean mainLeaderRegApproval) {
			this.mainLeaderRegApproval = mainLeaderRegApproval;
		}
		public Date getCaseRegStartDate() {
			return caseRegStartDate;
		}
		public void setCaseRegStartDate(Date caseRegStartDate) {
			this.caseRegStartDate = caseRegStartDate;
		}
		public Date getCaseRegEndDate() {
			return caseRegEndDate;
		}
		public void setCaseRegEndDate(Date caseRegEndDate) {
			this.caseRegEndDate = caseRegEndDate;
		}
		public Date getCaseSurveyEndDate() {
			return caseSurveyEndDate;
		}
		public void setCaseSurveyEndDate(Date caseSurveyEndDate) {
			this.caseSurveyEndDate = caseSurveyEndDate;
		}
		public String getNormAssigneePenalOptPart1() {
			return normAssigneePenalOptPart1;
		}
		public void setNormAssigneePenalOptPart1(String normAssigneePenalOptPart1) {
			this.normAssigneePenalOptPart1 = normAssigneePenalOptPart1;
		}
		public String getNormAssigneePenalOptPart2() {
			return normAssigneePenalOptPart2;
		}
		public void setNormAssigneePenalOptPart2(String normAssigneePenalOptPart2) {
			this.normAssigneePenalOptPart2 = normAssigneePenalOptPart2;
		}
		public String getAssigneePenalOption() {
			return assigneePenalOption;
		}
		public void setAssigneePenalOption(String assigneePenalOption) {
			this.assigneePenalOption = assigneePenalOption;
		}
		public String getInstitutionPenalOption() {
			return institutionPenalOption;
		}
		public void setInstitutionPenalOption(String institutionPenalOption) {
			this.institutionPenalOption = institutionPenalOption;
		}
		public Boolean getInstitutionPenalApproval() {
			return institutionPenalApproval;
		}
		public void setInstitutionPenalApproval(Boolean institutionPenalApproval) {
			this.institutionPenalApproval = institutionPenalApproval;
		}
		public String getCaseMgtCenterPenalOption() {
			return caseMgtCenterPenalOption;
		}
		public void setCaseMgtCenterPenalOption(String caseMgtCenterPenalOption) {
			this.caseMgtCenterPenalOption = caseMgtCenterPenalOption;
		}
		public Boolean getCaseMgtCenterPenalApproval() {
			return caseMgtCenterPenalApproval;
		}
		public void setCaseMgtCenterPenalApproval(Boolean caseMgtCenterPenalApproval) {
			this.caseMgtCenterPenalApproval = caseMgtCenterPenalApproval;
		}
		public String getDeptLeaderPenalOption() {
			return deptLeaderPenalOption;
		}
		public void setDeptLeaderPenalOption(String deptLeaderPenalOption) {
			this.deptLeaderPenalOption = deptLeaderPenalOption;
		}
		public Boolean getDeptLeaderPenalApproval() {
			return deptLeaderPenalApproval;
		}
		public void setDeptLeaderPenalApproval(Boolean deptLeaderPenalApproval) {
			this.deptLeaderPenalApproval = deptLeaderPenalApproval;
		}
		public String getMainLeaderPenalOption() {
			return mainLeaderPenalOption;
		}
		public void setMainLeaderPenalOption(String mainLeaderPenalOption) {
			this.mainLeaderPenalOption = mainLeaderPenalOption;
		}
		public Boolean getMainLeaderPenalApproval() {
			return mainLeaderPenalApproval;
		}
		public void setMainLeaderPenalApproval(Boolean mainLeaderPenalApproval) {
			this.mainLeaderPenalApproval = mainLeaderPenalApproval;
		}
		public Date getCasePenalStartDate() {
			return casePenalStartDate;
		}
		public void setCasePenalStartDate(Date casePenalStartDate) {
			this.casePenalStartDate = casePenalStartDate;
		}
		public Date getCasePenalEndDate() {
			return casePenalEndDate;
		}
		public void setCasePenalEndDate(Date casePenalEndDate) {
			this.casePenalEndDate = casePenalEndDate;
		}
		public Integer getCaseStage() {
			return caseStage;
		}
		public void setCaseStage(Integer caseStage) {
			this.caseStage = caseStage;
		}
		public String getAssigneeCloseCaseOption() {
			return assigneeCloseCaseOption;
		}
		public void setAssigneeCloseCaseOption(String assigneeCloseCaseOption) {
			this.assigneeCloseCaseOption = assigneeCloseCaseOption;
		}
		public String getInstitutionCloseCaseOption() {
			return institutionCloseCaseOption;
		}
		public void setInstitutionCloseCaseOption(String institutionCloseCaseOption) {
			this.institutionCloseCaseOption = institutionCloseCaseOption;
		}
		public Boolean getInstitutionCloseCaseApproval() {
			return institutionCloseCaseApproval;
		}
		public void setInstitutionCloseCaseApproval(Boolean institutionCloseCaseApproval) {
			this.institutionCloseCaseApproval = institutionCloseCaseApproval;
		}
		public String getCaseMgtCenterCloseCaseOption() {
			return caseMgtCenterCloseCaseOption;
		}
		public void setCaseMgtCenterCloseCaseOption(String caseMgtCenterCloseCaseOption) {
			this.caseMgtCenterCloseCaseOption = caseMgtCenterCloseCaseOption;
		}
		public Boolean getCaseMgtCenterCloseCaseApproval() {
			return caseMgtCenterCloseCaseApproval;
		}
		public void setCaseMgtCenterCloseCaseApproval(
				Boolean caseMgtCenterCloseCaseApproval) {
			this.caseMgtCenterCloseCaseApproval = caseMgtCenterCloseCaseApproval;
		}
		public String getMainLeaderCloseCaseOption() {
			return mainLeaderCloseCaseOption;
		}
		public void setMainLeaderCloseCaseOption(String mainLeaderCloseCaseOption) {
			this.mainLeaderCloseCaseOption = mainLeaderCloseCaseOption;
		}
		public Boolean getMainLeaderCloseCaseApproval() {
			return mainLeaderCloseCaseApproval;
		}
		public void setMainLeaderCloseCaseApproval(Boolean mainLeaderCloseCaseApproval) {
			this.mainLeaderCloseCaseApproval = mainLeaderCloseCaseApproval;
		}
		public Date getCaseCloseUpStartDate() {
			return caseCloseUpStartDate;
		}
		public void setCaseCloseUpStartDate(Date caseCloseUpStartDate) {
			this.caseCloseUpStartDate = caseCloseUpStartDate;
		}
		public Date getCaseCloseUpEndDate() {
			return caseCloseUpEndDate;
		}
		public void setCaseCloseUpEndDate(Date caseCloseUpEndDate) {
			this.caseCloseUpEndDate = caseCloseUpEndDate;
		}
		public String getNormCaseDesc() {
			return normCaseDesc;
		}
		public void setNormCaseDesc(String normCaseDesc) {
			this.normCaseDesc = normCaseDesc;
		}
		public String getNormAssigneePenalOpt() {
			return normAssigneePenalOpt;
		}
		public void setNormAssigneePenalOpt(String normAssigneePenalOpt) {
			this.normAssigneePenalOpt = normAssigneePenalOpt;
		}
		public String getProcessInstanceId() {
			return processInstanceId;
		}
		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
		}
		public String getCaseQueryParty() {
			return caseQueryParty;
		}
		public void setCaseQueryParty(String caseQueryParty) {
			this.caseQueryParty = caseQueryParty;
		}
		public String getCaseQueryLegalAgent() {
			return caseQueryLegalAgent;
		}
		public void setCaseQueryLegalAgent(String caseQueryLegalAgent) {
			this.caseQueryLegalAgent = caseQueryLegalAgent;
		}
		public String getCaseQueryAddress() {
			return caseQueryAddress;
		}
		public void setCaseQueryAddress(String caseQueryAddress) {
			this.caseQueryAddress = caseQueryAddress;
		}
		public String getCaseQueryPhoneNumber() {
			return caseQueryPhoneNumber;
		}
		public void setCaseQueryPhoneNumber(String caseQueryPhoneNumber) {
			this.caseQueryPhoneNumber = caseQueryPhoneNumber;
		}
		public String getCaseQueryBrokeLaw() {
			return caseQueryBrokeLaw;
		}
		public void setCaseQueryBrokeLaw(String caseQueryBrokeLaw) {
			this.caseQueryBrokeLaw = caseQueryBrokeLaw;
		}
		public String getCaseQueryPenal() {
			return caseQueryPenal;
		}
		public void setCaseQueryPenal(String caseQueryPenal) {
			this.caseQueryPenal = caseQueryPenal;
		}
		public Date getCaseQueryRegStartDateStart() {
			return caseQueryRegStartDateStart;
		}
		public void setCaseQueryRegStartDateStart(Date caseQueryRegStartDateStart) {
			this.caseQueryRegStartDateStart = caseQueryRegStartDateStart;
		}
		public Date getCaseQueryRegStartDateEnd() {
			return caseQueryRegStartDateEnd;
		}
		public void setCaseQueryRegStartDateEnd(Date caseQueryRegStartDateEnd) {
			this.caseQueryRegStartDateEnd = caseQueryRegStartDateEnd;
		}
		public Date getCaseQueryRegEndDateStart() {
			return caseQueryRegEndDateStart;
		}
		public void setCaseQueryRegEndDateStart(Date caseQueryRegEndDateStart) {
			this.caseQueryRegEndDateStart = caseQueryRegEndDateStart;
		}
		public Date getCaseQueryRegEndDateEnd() {
			return caseQueryRegEndDateEnd;
		}
		public void setCaseQueryRegEndDateEnd(Date caseQueryRegEndDateEnd) {
			this.caseQueryRegEndDateEnd = caseQueryRegEndDateEnd;
		}
		public Date getCaseQueryCloseDateStart() {
			return caseQueryCloseDateStart;
		}
		public void setCaseQueryCloseDateStart(Date caseQueryCloseDateStart) {
			this.caseQueryCloseDateStart = caseQueryCloseDateStart;
		}
		public Date getCaseQueryCloseDateEnd() {
			return caseQueryCloseDateEnd;
		}
		public void setCaseQueryCloseDateEnd(Date caseQueryCloseDateEnd) {
			this.caseQueryCloseDateEnd = caseQueryCloseDateEnd;
		}
		public Integer getCaseQueryStage() {
			return caseQueryStage;
		}
		public void setCaseQueryStage(Integer caseQueryStage) {
			this.caseQueryStage = caseQueryStage;
		}
		public String getAssigneeNames() {
			return assigneeNames;
		}
		public void setAssigneeNames(String assigneeNames) {
			this.assigneeNames = assigneeNames;
		}
	}
	

}