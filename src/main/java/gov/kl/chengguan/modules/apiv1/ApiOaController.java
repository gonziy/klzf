package gov.kl.chengguan.modules.apiv1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Task;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.Result;
import com.mysql.fabric.xmlrpc.base.Data;
import com.sun.javafx.collections.MappingChange.Map;
import com.sun.tools.javac.resources.javac;
import com.sun.tools.javac.util.List;

import gov.kl.chengguan.common.config.Global;
import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.supcan.common.Common;
import gov.kl.chengguan.common.utils.FileUtils;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.act.entity.Act;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.cms.entity.Article;
import gov.kl.chengguan.modules.cms.service.ArticleService;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.oa.dao.OaCaseDao;
import gov.kl.chengguan.modules.oa.dao.OaCaseFieldsDao;
import gov.kl.chengguan.modules.oa.dao.OaFilesDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.entity.OaCaseFields;
import gov.kl.chengguan.modules.oa.entity.OaFiles;
import gov.kl.chengguan.modules.oa.service.OaCaseService;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.service.UserService;




@RestController
@RequestMapping(value = "/apiv1")
public class ApiOaController  extends BaseController {

	private static OaCaseFieldsDao caseFieldsDao = SpringContextHolder.getBean(OaCaseFieldsDao.class);
	private static OaCaseDao caseDao = SpringContextHolder.getBean(OaCaseDao.class);
	private static OaFilesDao filesDao = SpringContextHolder.getBean(OaFilesDao.class);
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	
	@Autowired
	private OaCaseService oaCaseService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
	
	/**
	 * 案件审批
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "oa/case/approve" })
	public void updateCaseToNextStep(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String reqData) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String dataString = reqData;
		
		//dataString = request.getParameter("data");
		PrintWriter out;
		if (dataString != null) {
			JSONObject json = JSONObject.parseObject(dataString);
			JSONObject userJson = json.getJSONObject("user");
			JSONObject caseJson = json.getJSONObject("legalCase");
			String userId = userJson.getString("id");
			
			String id = caseJson.getString("id");
			String tmpStep = caseJson.getString("step");
			// 承办机构意见、分管领导意见、主管领导意见，都用这一个
			String opinion = caseJson.getString("opinion");
			// 处罚
			String punish = caseJson.getString("punish");
			// 状态，pass或reject
			String approve = caseJson.getString("approve");

			String[] tmpStepValues = tmpStep.split(",");
			// 表步骤
			String table = tmpStepValues[0];
			// 审批人步骤
			String step = tmpStepValues[1];
			OaCase model = null;
			java.util.List<Act> todoList = actTaskService.findTodoTasks(userId);
			if(todoList.size()>0)
			{
				for (Act act : todoList) {
					String businessId= act.getBusinessId();
					businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
					OaCase oaCase = caseDao.get(businessId);
					if(oaCase.getId().equals(id)){
						model = oaCase;
						model.setAct(act);
						break;
					}
				}
			}
			if (model==null) {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "failed");
				jsonObject
						.put("remark", "you don't have permission to approve");
			} else {

				if (table.equals("1")) {
					// ##################立案阶段######################
					if (step.equals("2")) {
						// 立案-承办机构
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setDeptLeaderRegOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setInstitutionRegApproval(approve.equals("pass") ? true
								: false);
						model.setInstitutionRegOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utLaShp_Cbjg")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}

					} else if (step.equals("3")) {
						// 立案 - 分管领导
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setMainLeaderRegOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setDeptLeaderRegApproval(approve.equals("pass") ? true
								: false);
						model.setDeptLeaderRegOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utLaShp_Fgld")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					} else if (step.equals("4")) {
						// 立案 - 主管领导
						model.setMainLeaderRegApproval(approve.equals("pass") ? true
								: false);
						model.setMainLeaderRegOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utLaShp_Zgld")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					}

				} else if (table.equals("2")) {
					// ##################调查阶段######################
					if (step.equals("1")) {
						// 调查
						
						if (model.getAct().getTaskDefKey()
								.equals("utAnjianDiaocha")) {
							oaCaseService.mobileSaveStep(model,1);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}

					}

				} else if (table.equals("3")) {
					// ##################处罚阶段######################
					if (step.equals("1")) {
						// 处罚-承办人
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setInstitutionPenalOption(null);
						caseDao.update(tmpModelCase);
						//结束
						model.setNormAssigneePenalOptPart2(punish);
						model.setCaseDocNo(new SimpleDateFormat(
								"yyyyMMddHHmmss").format(new Date()));

						model.setAssigneePenalOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						
						if (model.getAct().getTaskDefKey()
								.equals("utXzhChf_CbrYj")) {
							oaCaseService.mobileSaveStep(model, 1);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					} else if (step.equals("2")) {
						// 处罚-承办机构
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setCaseMgtCenterPenalOption(null);
						caseDao.update(tmpModelCase);
						//结束
						model.setInstitutionPenalApproval(approve
								.equals("pass") ? true : false);
						model.setInstitutionPenalOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						model.setCaseMgtCenterPenalOption("");
						if (model.getAct().getTaskDefKey()
								.equals("utXzhChf_Cbjg")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}

					} else if (step.equals("3")) {
						// 处罚 - 案管中心
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setDeptLeaderPenalOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setCaseMgtCenterPenalApproval(approve
								.equals("pass") ? true : false);
						model.setCaseMgtCenterPenalOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utXzhChf_AjGlZhx")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					} else if (step.equals("4")) {
						// 处罚-分管领导
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setMainLeaderPenalOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setDeptLeaderPenalApproval(approve.equals("pass") ? true
								: false);
						model.setDeptLeaderPenalOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utXzhChf_Fgld")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					} else if (step.equals("5")) {
						// 处罚-主管领导
						model.setMainLeaderPenalApproval(approve.equals("pass") ? true
								: false);
						model.setMainLeaderPenalOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utXzhChf_Zgld")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					}

				} else if (table.equals("4")) {
					// ##################结案阶段######################
					if (step.equals("1")) {
						// 结案-承办人
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setInstitutionCloseCaseOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setAssigneeCloseCaseOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utJaShp_Chbr")) {
							oaCaseService.mobileSaveStep(model, 1);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					} else if (step.equals("2")) {
						// 结案-承办机构
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setCaseMgtCenterCloseCaseOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setInstitutionCloseCaseApproval(approve
								.equals("pass") ? true : false);
						model.setInstitutionCloseCaseOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utJaShp_Cbjg")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}

					} else if (step.equals("3")) {
						// 结案 - 案管中心
						
						//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
						OaCase tmpModelCase = caseDao.get(id);
						tmpModelCase.setMainLeaderCloseCaseOption(null);
						caseDao.update(tmpModelCase);
						//结束
						
						model.setCaseMgtCenterCloseCaseApproval(approve
								.equals("pass") ? true : false);
						model.setCaseMgtCenterCloseCaseOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utJaShp_AjGlZhx")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					} else if (step.equals("4")) {
						// 结案-主管领导
						model.setMainLeaderCloseCaseApproval(approve
								.equals("pass") ? true : false);
						model.setMainLeaderCloseCaseOption(opinion.isEmpty() ? "该用户未填写意见(手机端)"
								: opinion + "(手机端)");
						if (model.getAct().getTaskDefKey()
								.equals("utJaShp_Zgld")) {
							oaCaseService.mobileSaveStep(model,
									approve.equals("pass") ? 1 : 0);
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "success");
							jsonObject.put("remark", "");
						} else {
							jsonObject.put("msg", "success");
							jsonObject.put("code", 0);
							jsonObject.put("result", "failed");
							jsonObject.put("remark", "this step has approved");
						}
					}
				} else {
					jsonObject.put("msg", "data is error");
					jsonObject.put("code", 44004);
				}
			}
		} else {
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
	 * 更新文件
	 * @param request
	 * @param response
	 * @param reqData
	 */
	@RequestMapping(value = {"oa/case/updatefiles"})
	public void updateCaseFiles(HttpServletRequest request, HttpServletResponse response, @RequestBody String reqData) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String dataString = reqData;
		PrintWriter out;
		if(dataString!=null)
		{
			JSONObject json = JSONObject.parseObject(dataString);
			JSONObject userJson = json.getJSONObject("user");
			JSONObject caseJson = json.getJSONObject("legalCase");
			String documents = "";
			String photos = "";
			String videos = "";
			JSONArray documentsJson = caseJson.getJSONArray("documents");
			JSONArray photosJson = caseJson.getJSONArray("photos");
			JSONArray videosJson = caseJson.getJSONArray("videos");
			if(documentsJson.size()>0){
				for (Object object : documentsJson) {
					documents += object.toString() + ";";
				}
				if(documents.endsWith(";")){
					documents = documents.substring(0, documents.length()-1);
				}
			}
			if(photosJson.size()>0){
				for (Object object : photosJson) {
					photos += object.toString() + ";";
				}
				if(documents.endsWith(";")){
					photos = photos.substring(0, photos.length()-1);
				}
			}
			if(videosJson.size()>0){
				for (Object object : videosJson) {
					videos += object.toString() + ";";
				}
				if(videos.endsWith(";")){
					videos = videos.substring(0, videos.length()-1);
				}
			}
			OaCase oaCase = new OaCase();
			oaCase.setId(caseJson.getString("id"));
			oaCase.setCaseDocuments(documents);
			oaCase.setCaseImages(photos);
			oaCase.setCaseVideos(videos);
			User user = userDao.get(userJson.getString("id"));
			
			caseDao.updateFiles(oaCase);

			jsonObject.put("msg", "success");
			jsonObject.put("code", 0);
			jsonObject.put("result", "success");
			jsonObject.put("remark", "");
			com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
			jsonData.put("id", "");
			jsonObject.put("data", jsonData);
			
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
	 * 立案
	 * @param request
	 * @param response
	 * @param reqData
	 */
	@RequestMapping(value = {"oa/case/create"})
	public void createCaseInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody String reqData) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String dataString = reqData;
		PrintWriter out;
		if(dataString!=null)
		{
			JSONObject json = JSONObject.parseObject(dataString);
			JSONObject userJson = json.getJSONObject("user");
			JSONObject caseJson = json.getJSONObject("legalCase");
//			String documents = "";
//			String photo = "";
//			String videos = "";
//			JSONArray documentsJson = caseJson.getJSONArray("documents");
//			JSONArray photosJson = caseJson.getJSONArray("photos");
//			JSONArray videosJson = caseJson.getJSONArray("videos");
//			if(documentsJson.size()>0){
//				for (Object object : documentsJson) {
//					documents += object.toString() + ";";
//				}
//				if(documents.endsWith(";")){
//					documents = documents.substring(0, documents.length()-1);
//				}
//			}
//			if(photosJson.size()>0){
//				for (Object object : photosJson) {
//					photo += object.toString() + ";";
//				}
//				if(documents.endsWith(";")){
//					photo = photo.substring(0, photo.length()-1);
//				}
//			}
//			if(videosJson.size()>0){
//				for (Object object : videosJson) {
//					videos += object.toString() + ";";
//				}
//				if(videos.endsWith(";")){
//					videos = videos.substring(0, videos.length()-1);
//				}
//			}
			OaCase oaCase = new OaCase();
			oaCase.setTitle(caseJson.getString("title"));
			oaCase.setCaseParties(caseJson.getString("caseParties"));
			oaCase.setAddress(caseJson.getString("address"));
			oaCase.setCaseLegalAgent(caseJson.getString("caseLegalAgent"));
			oaCase.setIllegalConstructionFlag(caseJson.getBooleanValue("illegalConstructionFlag"));
			oaCase.setPhoneNumber(caseJson.getString("phoneNumber"));
			//来源
			oaCase.setCaseSource(caseJson.getString("kind"));
			//行为
			oaCase.setNormCaseDescPart1(caseJson.getString("behavior"));
			//法条
			oaCase.setNormCaseDescPart2(caseJson.getString("provision"));
			oaCase.setCaseDescription(caseJson.getString("caseDescription"));
			oaCase.setAssigneeIds(caseJson.getString("assigneeIds"));
			User user = userDao.get(userJson.getString("id"));

			Date nowDate = new Date();
			oaCase.setCreateBy(user);
			oaCase.setCreateDate(nowDate);
			oaCase.setUpdateBy(user);
			oaCase.setUpdateDate(nowDate);
			oaCaseService.mobileSave(userJson.getString("id"), oaCase);
			jsonObject.put("msg", "success");
			jsonObject.put("code", 0);
			jsonObject.put("result", "success");
			jsonObject.put("remark", "");
			com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
			jsonData.put("id", "");
			jsonObject.put("data", jsonData);
			
		}else {
			jsonObject.put("msg", "data is null");
			jsonObject.put("code", 44004);
		}
		try {
			out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
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
	/**
	 * 案件查询
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/case/get"})
	public void getCaseInfo(HttpServletRequest request, HttpServletResponse response) {
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
			jsonObject.put("msg", "missing url, id is null");
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
			OaCase oaCase = caseDao.get(id);
			if(oaCase == null){
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
			}
			else {
				ApiOaCase apiOaCase = new ApiOaCase();
				
				String _case_sourceString = oaCase.getCaseSource();
				if(_case_sourceString=="1"){
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}else if(_case_sourceString=="2"){
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}else if(_case_sourceString=="3"){
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}else if(_case_sourceString=="4"){
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}else if(_case_sourceString=="5"){
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}else if(_case_sourceString=="6"){
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}else{
					oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
				}
				apiOaCase.setId(oaCase.getId());
			    apiOaCase.setCaseDocNo(oaCase.getCaseDocNo()==null?"":oaCase.getCaseDocNo());
				apiOaCase.setTitle(oaCase.getTitle());
				apiOaCase.setCaseParties (oaCase.getCaseParties());
				apiOaCase.setCaseLegalAgent(oaCase.getCaseLegalAgent());
				apiOaCase.setAddress(oaCase.getAddress());
				apiOaCase.setPhoneNumber(oaCase.getPhoneNumber());
				apiOaCase.setCaseDescription(oaCase.getCaseDescription());
				apiOaCase.setCaseImages(oaCase.getCaseImages());
				apiOaCase.setCaseVideos(oaCase.getCaseVideos());
				apiOaCase.setCaseThumbnails(oaCase.getCaseThumbnails());
				apiOaCase.setCaseDocuments(oaCase.getCaseDocuments());
				apiOaCase.setCaseCheckResult(oaCase.getCaseCheckResult());
				apiOaCase.setCaseSource(oaCase.getCaseSource());
				apiOaCase.setAssigneeIds(oaCase.getAssigneeIds());
				apiOaCase.setNormCaseDescPart1(oaCase.getNormCaseDescPart1());
				apiOaCase.setNormCaseDescPart2(oaCase.getNormCaseDescPart2());
				apiOaCase.setInstitutionRegOption(oaCase.getInstitutionRegOption());
//				apiOaCase.setInstitutionRegApproval((Boolean)oaCase.isInstitutionRegApproval());
				apiOaCase.setDeptLeaderRegOption(oaCase.getDeptLeaderRegOption());
//				apiOaCase.setDeptLeaderRegApproval(oaCase.isDeptLeaderRegApproval());
				apiOaCase.setMainLeaderRegOption(oaCase.getMainLeaderRegOption());
//				apiOaCase.setMainLeaderRegApproval(oaCase.isMainLeaderRegApproval());
//				apiOaCase.setCaseRegStartDate(oaCase.getCaseRegStartDate());
//				apiOaCase.setCaseRegEndDate(oaCase.getCaseRegEndDate());
//				apiOaCase.setCaseSurveyEndDate(oaCase.getCaseSurveyEndDate());
//				apiOaCase.setNormAssigneePenalOptPart1(oaCase.getNormAssigneePenalOptPart1());
				apiOaCase.setNormAssigneePenalOptPart2(oaCase.getNormAssigneePenalOptPart2());
				apiOaCase.setAssigneePenalOption(oaCase.getAssigneePenalOption());
				apiOaCase.setInstitutionPenalOption(oaCase.getInstitutionPenalOption());
//				apiOaCase.setInstitutionPenalApproval(oaCase.getInstitutionPenalApproval());
				apiOaCase.setCaseMgtCenterPenalOption(oaCase.getCaseMgtCenterPenalOption());
//				apiOaCase.setCaseMgtCenterPenalApproval(oaCase.getCaseMgtCenterPenalApproval());
				apiOaCase.setDeptLeaderPenalOption(oaCase.getDeptLeaderPenalOption());
//				apiOaCase.setDeptLeaderPenalApproval(oaCase.getDeptLeaderPenalApproval());
				apiOaCase.setMainLeaderPenalOption(oaCase.getMainLeaderPenalOption());
//				apiOaCase.setMainLeaderPenalApproval(oaCase.getMainLeaderPenalApproval());
//				apiOaCase.setCasePenalStartDate(oaCase.getCasePenalStartDate());
//				apiOaCase.setCasePenalEndDate(oaCase.getCasePenalEndDate());
				apiOaCase.setCaseStage(oaCase.getCaseStage());
//				apiOaCase.setAssigneeCloseCaseOption(oaCase.getAssigneeCloseCaseOption());
				apiOaCase.setInstitutionCloseCaseOption(oaCase.getInstitutionCloseCaseOption());
//				apiOaCase.setInstitutionCloseCaseApproval(oaCase.getInstitutionCloseCaseApproval());
				apiOaCase.setCaseMgtCenterCloseCaseOption(oaCase.getCaseMgtCenterCloseCaseOption());
//				apiOaCase.setCaseMgtCenterCloseCaseApproval(oaCase.getCaseMgtCenterCloseCaseApproval());
				apiOaCase.setMainLeaderCloseCaseOption(oaCase.getMainLeaderCloseCaseOption());
//				apiOaCase.setMainLeaderCloseCaseApproval(oaCase.getMainLeaderCloseCaseApproval());
//				apiOaCase.setCaseCloseUpStartDate(oaCase.getCaseCloseUpStartDate());
//				apiOaCase.setCaseCloseUpEndDate(oaCase.getCaseCloseUpEndDate());
				apiOaCase.setNormCaseDesc(oaCase.getNormCaseDesc());
//				apiOaCase.setNormAssigneePenalOpt(oaCase.getNormAssigneePenalOpt());
//				apiOaCase.setProcessInstanceId(oaCase.getProcessInstanceId());
//				apiOaCase.setCaseQueryParty(oaCase.getCaseQueryParty());
//				apiOaCase.setCaseQueryLegalAgent(oaCase.getCaseQueryLegalAgent());
//				apiOaCase.setCaseQueryAddress(oaCase.getCaseQueryAddress());
//				apiOaCase.setCaseQueryPhoneNumber(oaCase.getCaseQueryPhoneNumber());
//				apiOaCase.setCaseQueryBrokeLaw (oaCase.getCaseQueryBrokeLaw ());
//				apiOaCase.setCaseQueryPenal (oaCase.getCaseQueryPenal ());
//				apiOaCase.setCaseQueryRegStartDateStart(oaCase.getCaseQueryRegStartDateStart());
//				apiOaCase.setCaseQueryRegStartDateEnd(oaCase.getCaseQueryRegStartDateEnd());
//				apiOaCase.setCaseQueryRegEndDateStart(oaCase.getCaseQueryRegEndDateStart());
//				apiOaCase.setCaseQueryRegEndDateEnd(oaCase.getCaseQueryRegEndDateEnd());
//				apiOaCase.setCaseQueryCloseDateStart(oaCase.getCaseQueryCloseDateStart());
//				apiOaCase.setCaseQueryCloseDateEnd(oaCase.getCaseQueryCloseDateEnd());
//				apiOaCase.setCaseQueryStage(oaCase.getCaseQueryStage());
				String strAssigneeNames = "";
				if(apiOaCase.getAssigneeIds()!=null && !apiOaCase.getAssigneeIds().isEmpty()){
					String[] assigneeIdsList = apiOaCase.getAssigneeIds().split(";");
					if(assigneeIdsList.length>0){
						for (String assigneeid : assigneeIdsList) {
							User user = userDao.get(assigneeid);
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
					
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);

				jsonObject.put("data", JSONObject.toJSON(apiOaCase));
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
	
	/**
	 * 获取案件步骤
	 * @param oaCase
	 * @return
	 */
	private java.util.List<ApiStep> getCaseProgress(OaCase oaCase){
		java.util.List<ApiStep> list = new ArrayList<ApiStep>();
		list.add(new ApiStep("立案", "承办人", "pass", oaCase.getCaseCheckResult()==null?"":oaCase.getCaseCheckResult(),"1,1"));
		list.add(new ApiStep("立案", "承办机构",oaCase.getInstitutionRegOption()==null?"":oaCase.getInstitutionRegApproval()==true?"pass":"reject", oaCase.getInstitutionRegOption()==null?"":oaCase.getInstitutionRegOption(),"1,2"));
		list.add(new ApiStep("立案", "分管领导", oaCase.getDeptLeaderRegOption()==null?"":oaCase.getDeptLeaderRegApproval()==true?"pass":"reject", oaCase.getDeptLeaderRegOption()==null?"":oaCase.getDeptLeaderRegOption(),"1,3"));
		list.add(new ApiStep("立案", "主管领导",  oaCase.getMainLeaderRegOption()==null?"":oaCase.getMainLeaderRegApproval()==true?"pass":"reject", oaCase.getMainLeaderRegOption()==null?"":oaCase.getMainLeaderRegOption(),"1,4"));
		list.add(new ApiStep("调查", "调查", oaCase.getCaseSurveyEndDate()==null?"":true==true?"pass":"reject", "","2,1"));
		list.add(new ApiStep("处罚", "承办人", oaCase.getAssigneePenalOption()==null?"":true==true?"pass":"reject", oaCase.getAssigneePenalOption()==null?"":oaCase.getAssigneePenalOption(),"3,1"));
		list.add(new ApiStep("处罚", "承办机构", oaCase.getInstitutionPenalOption()==null?"":oaCase.getInstitutionPenalApproval()==true?"pass":"reject", oaCase.getInstitutionPenalOption()==null?"":oaCase.getInstitutionPenalOption(),"3,2"));
		list.add(new ApiStep("处罚", "案管中心", oaCase.getCaseMgtCenterPenalOption()==null?"":oaCase.getCaseMgtCenterPenalApproval()==true?"pass":"reject", oaCase.getCaseMgtCenterPenalOption()==null?"":oaCase.getCaseMgtCenterPenalOption(),"3,3"));
		list.add(new ApiStep("处罚", "分管领导", oaCase.getDeptLeaderPenalOption()==null?"":oaCase.getDeptLeaderPenalApproval()==true?"pass":"reject", oaCase.getDeptLeaderPenalOption()==null?"":oaCase.getDeptLeaderPenalOption(),"3,4"));
		list.add(new ApiStep("处罚", "主管领导", oaCase.getMainLeaderCloseCaseOption()==null?"":oaCase.getMainLeaderPenalApproval()==true?"pass":"reject", oaCase.getMainLeaderCloseCaseOption()==null?"":oaCase.getMainLeaderCloseCaseOption(),"3,5"));
		list.add(new ApiStep("结案", "承办人", oaCase.getAssigneeCloseCaseOption()==null?"":true==true?"pass":"reject", oaCase.getAssigneeCloseCaseOption()==null?"":oaCase.getAssigneeCloseCaseOption(),"4,1"));
		list.add(new ApiStep("结案", "承办机构", oaCase.getInstitutionCloseCaseOption()==null?"":oaCase.getInstitutionCloseCaseApproval()==true?"pass":"reject", oaCase.getInstitutionCloseCaseOption()==null?"":oaCase.getInstitutionCloseCaseOption(),"4,2"));
		list.add(new ApiStep("结案", "案管中心", oaCase.getCaseMgtCenterCloseCaseOption()==null?"":oaCase.getCaseMgtCenterCloseCaseApproval()==true?"pass":"reject", oaCase.getCaseMgtCenterCloseCaseOption()==null?"":oaCase.getCaseMgtCenterCloseCaseOption(),"4,3"));
		list.add(new ApiStep("结案", "主管领导", oaCase.getMainLeaderCloseCaseOption()==null?"":oaCase.getMainLeaderCloseCaseApproval()==true?"pass":"reject", oaCase.getMainLeaderCloseCaseOption()==null?"":oaCase.getMainLeaderCloseCaseOption(),"4,4"));
		list.add(new ApiStep("结束", "已完成", "", "","5,1"));
		return list;
	}
	
	/**
	 * 获取案件正在办理状态
	 * @param oacase
	 * @return
	 */
	private String getCaseProgressNow(OaCase oaCase){
		String txtString = "";
		
		java.util.List<ApiStep> steps = getCaseProgress(oaCase);
		
		for(int i = 0; i < steps.size();i++){
			if(steps.get(i).getStatus().equals("pass")){
				txtString = steps.get(i+1).getStep() + "," + steps.get(i+1).getStage() + "," + steps.get(i+1).getName();
			}else if(steps.get(i).getStatus().equals("reject")){
				txtString = steps.get(i-1).getStep() + "," + steps.get(i-1).getStage() + "," + steps.get(i-1).getName();
				break;
			}else if(steps.get(i).getStatus().isEmpty()){
				txtString = steps.get(i).getStep() + "," + steps.get(i).getStage() + "," + steps.get(i).getName();
				break;
			}
			
		}
//		String taskDefKey = oaCase.getAct().getTaskDefKey();
//		// 查看案件情况表
//		if(oaCase.getAct().isFinishTask()){
//			txtString ="4,5,结案,案件结束";
//		}
//		// 案件初审
//		else if ("utAnjianChushen".equals(taskDefKey)){
//			txtString ="1,1,立案,承办人";
//		}
//		// 案件信息录入
//		else if ("utAnjianLuru".equals(taskDefKey)){
//			txtString ="1,1,立案,承办人";
//		}
//		// 立案审批——承办机构
//		else if ("utLaShp_Cbjg".equals(taskDefKey)){
//			txtString ="1,2,立案,承办机构";
//		}
//		// 立案审批——分管领导
//		else if ("utLaShp_Fgld".equals(taskDefKey)){
//			txtString ="1,3,立案,分管领导";
//		}			
//		// 立案审批——主管领导
//		else if ("utLaShp_Zgld".equals(taskDefKey)){
//			txtString ="1,4,立案,主管领导";
//		}
//		// 开始案件调查
//		else if ("utAnjianDiaocha".equals(taskDefKey)){
//			txtString ="2,1,调查,案件调查";
//		}
//		// 行政处罚——承办人意见
//		else if ("utXzhChf_CbrYj".equals(taskDefKey)){
//			txtString ="3,1,处罚,承办人";
//		}
//		// 行政处罚——承办机构审批
//		else if ("utXzhChf_Cbjg".equals(taskDefKey)){
//			txtString ="3,2,处罚,承办机构";
//		}
//		// 行政处罚——案件管理中心审批
//		else if ("utXzhChf_AjGlZhx".equals(taskDefKey)){
//			txtString ="3,3,处罚,案管中心";
//		}
//		// 行政处罚——分管领导审批
//		else if ("utXzhChf_Fgld".equals(taskDefKey)){
//			txtString ="3,4,处罚,分管领导";
//		}
//		// 行政处罚——主管领导审批
//		else if ("utXzhChf_Zgld".equals(taskDefKey)){
//			txtString ="3,5,处罚,主管领导";
//		}
//		// 结案审批——承办人
//		else if ("utJaShp_Chbr".equals(taskDefKey)){
//			txtString ="4,1,结案,承办人";
//		}
//		// 结案审批——承办机构
//		else if ("utJaShp_Cbjg".equals(taskDefKey)){
//			txtString ="4,2,结案,承办机构";
//		}
//		// 结案审批——案件管理中心
//		else if ("utJaShp_AjGlZhx".equals(taskDefKey)){
//			txtString ="13,结案,案管中心";
//		}
//		// 结案审批——主管领导
//		else if ("utJaShp_Zgld".equals(taskDefKey)){
//			txtString ="4,4,结案,主管领导";
//		}
//		// 都不是
//		else {
//			txtString ="0,0,未知,未知";
//		}
//		
		return txtString;
	}
	/**
	 * 获取案件进度名称
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/case/getstage"})
	public void getCaseStage(HttpServletRequest request, HttpServletResponse response) {
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
			jsonObject.put("msg", "missing url, id is null");
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
			OaCase oaCase = caseDao.get(id);
			if(oaCase != null){
				String result = getCaseProgressNow(oaCase);
				com.alibaba.fastjson.JSONObject jsonResult = new com.alibaba.fastjson.JSONObject();
				if(result == null || result.isEmpty()){
					jsonObject.put("msg", "data is null");
					jsonObject.put("code", 44004);
				}
				else {
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonResult.put("result", result);
					jsonObject.put("data",jsonResult);
				}
			}else{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
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
	/**
	 * 案件进度列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/case/getstagelist"})
	public void getCaseStagelist(HttpServletRequest request, HttpServletResponse response) {
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
			jsonObject.put("msg", "missing url, id is null");
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
			OaCase oaCase = caseDao.get(id);
			
			if(oaCase!=null){
				java.util.List<ApiStep> steps = getCaseProgress(oaCase);		
				if(steps == null){
					jsonObject.put("msg", "data is null");
					jsonObject.put("code", 44004);
				}
				else {
						
	
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
					jsonData.put("data", JSONObject.toJSON(steps));
					jsonObject.put("data", jsonData);
					String progressNow = getCaseProgressNow(oaCase);
					String[] values = progressNow.split(",");
					jsonObject.put("step", values[0]);
					jsonObject.put("stage", values[1]);
					jsonObject.put("name", values[2]);
					
				}
			}else {
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
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
	
	@RequestMapping(value = {"oa/case/getfile"})
	public void getCaseFile(HttpServletRequest request, HttpServletResponse response) {
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
			jsonObject.put("msg", "missing url, id is null");
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
			OaCase oaCase = caseDao.get(id);
			if(oaCase == null){
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
			}
			else {
				ApiOaCase apiOaCase = new ApiOaCase();
				
				apiOaCase.setId(oaCase.getId());
				apiOaCase.setCaseImages(oaCase.getCaseImages());
				apiOaCase.setCaseVideos(oaCase.getCaseVideos());
				apiOaCase.setCaseThumbnails(oaCase.getCaseThumbnails());
				apiOaCase.setCaseDocuments(oaCase.getCaseDocuments());
					
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
				
				if(oaCase.getCaseDocuments()!=null && !oaCase.getCaseDocuments().isEmpty()){
					String[] docs = oaCase.getCaseDocuments().split(";");
					java.util.List<String> list = new ArrayList<String>();
					for (String doc : docs) {
						list.add(doc);
					}
					if(list != null &&!list.isEmpty()){
						com.alibaba.fastjson.JSONObject jsonDocuments = new com.alibaba.fastjson.JSONObject();
						jsonData.put("documents", list);
					}else {
						
						jsonData.put("documents",  new ArrayList<String>());
					}
				}else{
					jsonData.put("documents",  new ArrayList<String>());
				}
				if(oaCase.getCaseImages()!=null && !oaCase.getCaseImages().isEmpty()){
					String[] imgs = oaCase.getCaseImages().split(";");
					java.util.List<String> list = new ArrayList<String>();
					for (String img : imgs) {
						list.add(img);
					}
					if(list != null &&!list.isEmpty()){
						com.alibaba.fastjson.JSONObject jsonPhotos = new com.alibaba.fastjson.JSONObject();
						jsonData.put("photos", list);
					}else{
						jsonData.put("photos",  new ArrayList<String>());
					}
				}else{
					jsonData.put("photos",  new ArrayList<String>());
				}
				if(oaCase.getCaseVideos()!=null && !oaCase.getCaseVideos().isEmpty()){
					String[] vdos = oaCase.getCaseVideos().split(";");
					java.util.List<String> list = new ArrayList<String>();
					for (String vdo : vdos) {
						list.add(vdo);
					}
					if(list != null &&!list.isEmpty()){
						com.alibaba.fastjson.JSONObject jsonVideos = new com.alibaba.fastjson.JSONObject();
						jsonData.put("videos", list);
					}else{
						jsonData.put("videos", new ArrayList<String>());
					}
				}else{
					jsonData.put("videos",  new ArrayList<String>());
				}
				
				
				jsonObject.put("data", JSONObject.toJSON(jsonData));
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
	
	@RequestMapping(value = {"oa/case/list"})
	public void getCaseList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();

		String pageIndex = request.getParameter("page");
		String stage = request.getParameter("stage");
		String userId = request.getParameter("user_id");
		if(pageIndex==null || pageIndex.isEmpty())
		{
			jsonObject.put("msg", "missing url, page is null");
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
		if(stage==null || stage.isEmpty())
		{
			if(!gov.kl.chengguan.common.utils.StringUtils.isNumeric(stage)){
				jsonObject.put("msg", "missing url, stage is number");
				jsonObject.put("code", 41010);
				
			}else{
				jsonObject.put("msg", "missing url, stage is null");
				jsonObject.put("code", 41010);
			}
					
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
			OaCase oaCaseWhere = new OaCase();
			oaCaseWhere.setCaseQueryStage(Integer.parseInt(stage));
			
			Page<OaCase> pageWhere = new Page<OaCase>(request, response);
			pageWhere.setPageSize(10);
			pageWhere.setPageNo(Integer.parseInt(pageIndex));
			Page<OaCase> page = oaCaseService.findPage(pageWhere, oaCaseWhere); 
			
			
			java.util.List<OaCase> list = page.getList();
			if(list == null){
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
			}
			else {
				com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
				jsonData.put("pagesize", 10);
				jsonData.put("pagecount", page.getLast());
				java.util.List<ApiOaCase> results = new ArrayList<ApiOaCase>();
				for (OaCase oaCase : list) {
					ApiOaCase apiOaCase = new ApiOaCase();
					
					
					String _case_sourceString = oaCase.getCaseSource();
					if(_case_sourceString=="1"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="2"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="3"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="4"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="5"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="6"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else{
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}
					apiOaCase.setTitle(oaCase.getTitle());
				    apiOaCase.setCaseDocNo(oaCase.getCaseDocNo()==null?"":oaCase.getCaseDocNo());
					apiOaCase.setId(oaCase.getId());
					apiOaCase.setCaseParties (oaCase.getCaseParties());
					apiOaCase.setCaseLegalAgent(oaCase.getCaseLegalAgent());
					apiOaCase.setAddress(oaCase.getAddress());
					apiOaCase.setPhoneNumber(oaCase.getPhoneNumber());
					apiOaCase.setCaseDescription(oaCase.getCaseDescription());
					apiOaCase.setCaseImages(oaCase.getCaseImages());
					apiOaCase.setCaseVideos(oaCase.getCaseVideos());
					apiOaCase.setCaseThumbnails(oaCase.getCaseThumbnails());
					apiOaCase.setCaseDocuments(oaCase.getCaseDocuments());
					apiOaCase.setCaseCheckResult(oaCase.getCaseCheckResult());
					apiOaCase.setCaseSource(oaCase.getCaseSource());
					apiOaCase.setAssigneeIds(oaCase.getAssigneeIds());
					apiOaCase.setNormCaseDescPart1(oaCase.getNormCaseDescPart1());
					apiOaCase.setNormCaseDescPart2(oaCase.getNormCaseDescPart2());
					apiOaCase.setInstitutionRegOption(oaCase.getInstitutionRegOption());
					apiOaCase.setInstitutionRegApproval((Boolean)oaCase.getInstitutionRegApproval());
					apiOaCase.setDeptLeaderRegOption(oaCase.getDeptLeaderRegOption());
					apiOaCase.setDeptLeaderRegApproval(oaCase.getDeptLeaderRegApproval());
					apiOaCase.setMainLeaderRegOption(oaCase.getMainLeaderRegOption());
					apiOaCase.setMainLeaderRegApproval(oaCase.getMainLeaderRegApproval());
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
					apiOaCase.setStep(getCaseProgressNow(oaCase));

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
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);

				jsonData.put("data", results);
				jsonObject.put("data", JSONObject.toJSON(jsonData));
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
	
	@RequestMapping(value = {"oa/case/todolist"})
	public void getCaseToDoList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String userId = request.getParameter("user_id");

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
			
			java.util.List<OaCase> oaCaselist = oaCaseService.findTodoTasks(userId);
			java.util.List<ApiOaCase> results = new ArrayList<ApiOaCase>();
		
			for(OaCase oaCase : oaCaselist)
			{
				
//				String businessId= act.getBusinessId();
//				businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());				
				//OaCase oaCase = caseDao.get(businessId);
				if(oaCase != null){
					ApiOaCase apiOaCase = new ApiOaCase();
					
					String _case_sourceString = oaCase.getCaseSource();
					if(_case_sourceString=="1"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="2"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="3"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="4"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="5"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else if(_case_sourceString=="6"){
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}else{
						oaCase.setCaseThumbnails("http://pic.to8to.com/attch/day_150717/20150717_e4a515295cb744e19d027i0jtxc6XRla.jpg");
					}
					apiOaCase.setId(oaCase.getId());

				    apiOaCase.setCaseDocNo(oaCase.getCaseDocNo()==null?"":oaCase.getCaseDocNo());
					apiOaCase.setTitle(oaCase.getTitle());
//					apiOaCase.setCaseParties (oaCase.getCaseParties());
					apiOaCase.setCaseLegalAgent(oaCase.getCaseLegalAgent());
					apiOaCase.setAddress(oaCase.getAddress());
					apiOaCase.setPhoneNumber(oaCase.getPhoneNumber());
					apiOaCase.setCaseDescription(oaCase.getCaseDescription());
					apiOaCase.setCaseImages(oaCase.getCaseImages());
					apiOaCase.setCaseVideos(oaCase.getCaseVideos());
					apiOaCase.setCaseThumbnails(oaCase.getCaseThumbnails());
					apiOaCase.setCaseDocuments(oaCase.getCaseDocuments());
//					apiOaCase.setCaseCheckResult(oaCase.getCaseCheckResult());
					apiOaCase.setCaseSource(oaCase.getCaseSource());
					apiOaCase.setAssigneeIds(oaCase.getAssigneeIds());
//					apiOaCase.setNormCaseDescPart1(oaCase.getNormCaseDescPart1());
//					apiOaCase.setNormCaseDescPart2(oaCase.getNormCaseDescPart2());
//					apiOaCase.setInstitutionRegOption(oaCase.getInstitutionRegOption());
//					apiOaCase.setInstitutionRegApproval((Boolean)oaCase.isInstitutionRegApproval());
//					apiOaCase.setDeptLeaderRegOption(oaCase.getDeptLeaderRegOption());
//					apiOaCase.setDeptLeaderRegApproval(oaCase.isDeptLeaderRegApproval());
//					apiOaCase.setMainLeaderRegOption(oaCase.getMainLeaderRegOption());
//					apiOaCase.setMainLeaderRegApproval(oaCase.isMainLeaderRegApproval());
//					apiOaCase.setCaseRegStartDate(oaCase.getCaseRegStartDate());
//					apiOaCase.setCaseRegEndDate(oaCase.getCaseRegEndDate());
//					apiOaCase.setCaseSurveyEndDate(oaCase.getCaseSurveyEndDate());
//					apiOaCase.setNormAssigneePenalOptPart1(oaCase.getNormAssigneePenalOptPart1());
//					apiOaCase.setNormAssigneePenalOptPart2(oaCase.getNormAssigneePenalOptPart2());
//					apiOaCase.setAssigneePenalOption(oaCase.getAssigneePenalOption());
//					apiOaCase.setInstitutionPenalOption(oaCase.getInstitutionPenalOption());
//					apiOaCase.setInstitutionPenalApproval(oaCase.getInstitutionPenalApproval());
//					apiOaCase.setCaseMgtCenterPenalOption(oaCase.getCaseMgtCenterPenalOption());
//					apiOaCase.setCaseMgtCenterPenalApproval(oaCase.getCaseMgtCenterPenalApproval());
//					apiOaCase.setDeptLeaderPenalOption(oaCase.getDeptLeaderPenalOption());
//					apiOaCase.setDeptLeaderPenalApproval(oaCase.getDeptLeaderPenalApproval());
//					apiOaCase.setMainLeaderPenalOption(oaCase.getMainLeaderPenalOption());
//					apiOaCase.setMainLeaderPenalApproval(oaCase.getMainLeaderPenalApproval());
//					apiOaCase.setCasePenalStartDate(oaCase.getCasePenalStartDate());
//					apiOaCase.setCasePenalEndDate(oaCase.getCasePenalEndDate());
//					apiOaCase.setCaseStage(oaCase.getCaseStage());
//					apiOaCase.setAssigneeCloseCaseOption(oaCase.getAssigneeCloseCaseOption());
//					apiOaCase.setInstitutionCloseCaseOption(oaCase.getInstitutionCloseCaseOption());
//					apiOaCase.setInstitutionCloseCaseApproval(oaCase.getInstitutionCloseCaseApproval());
//					apiOaCase.setCaseMgtCenterCloseCaseOption(oaCase.getCaseMgtCenterCloseCaseOption());
//					apiOaCase.setCaseMgtCenterCloseCaseApproval(oaCase.getCaseMgtCenterCloseCaseApproval());
//					apiOaCase.setMainLeaderCloseCaseOption(oaCase.getMainLeaderCloseCaseOption());
//					apiOaCase.setMainLeaderCloseCaseApproval(oaCase.getMainLeaderCloseCaseApproval());
//					apiOaCase.setCaseCloseUpStartDate(oaCase.getCaseCloseUpStartDate());
//					apiOaCase.setCaseCloseUpEndDate(oaCase.getCaseCloseUpEndDate());
//					apiOaCase.setNormCaseDesc(oaCase.getNormCaseDesc());
//					apiOaCase.setNormAssigneePenalOpt(oaCase.getNormAssigneePenalOpt());
//					apiOaCase.setProcessInstanceId(oaCase.getProcessInstanceId());
//					apiOaCase.setCaseQueryParty(oaCase.getCaseQueryParty());
//					apiOaCase.setCaseQueryLegalAgent(oaCase.getCaseQueryLegalAgent());
//					apiOaCase.setCaseQueryAddress(oaCase.getCaseQueryAddress());
//					apiOaCase.setCaseQueryPhoneNumber(oaCase.getCaseQueryPhoneNumber());
//					apiOaCase.setCaseQueryBrokeLaw (oaCase.getCaseQueryBrokeLaw ());
//					apiOaCase.setCaseQueryPenal (oaCase.getCaseQueryPenal ());
//					apiOaCase.setCaseQueryRegStartDateStart(oaCase.getCaseQueryRegStartDateStart());
//					apiOaCase.setCaseQueryRegStartDateEnd(oaCase.getCaseQueryRegStartDateEnd());
//					apiOaCase.setCaseQueryRegEndDateStart(oaCase.getCaseQueryRegEndDateStart());
//					apiOaCase.setCaseQueryRegEndDateEnd(oaCase.getCaseQueryRegEndDateEnd());
//					apiOaCase.setCaseQueryCloseDateStart(oaCase.getCaseQueryCloseDateStart());
//					apiOaCase.setCaseQueryCloseDateEnd(oaCase.getCaseQueryCloseDateEnd());
//					apiOaCase.setCaseQueryStage(oaCase.getCaseQueryStage());
					apiOaCase.setStep(getCaseProgressNow(oaCase));
					apiOaCase.setExpirseDays(1);
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
				jsonObject.put("result",  "success");
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
				jsonObject.put("result","failure");
				
			}else {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result",  "success");
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
					jsonObject.put("result",  "success");					
				}else 
				{
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "failure");	
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
			HttpServletResponse response, @RequestParam("files") MultipartFile[] files) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
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
						|| fileExt.equals(".f4v") || fileExt.equals(".rmvb")
						|| fileExt.equals(".mp4")) {
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
					jsonObject.put("result",  "failure");
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

				String httpPath = "http://47.93.52.62:8080/klzf2"
						+ Global.USERFILES_BASE_URL + fileType + "/"
						+ folderName + "/" + newFileName;
				try {
						jsonObject.put("msg", "success");
						jsonObject.put("code", 0);
						jsonObject.put("result",  "success");
						com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
						jsonData.put("path", httpPath);
						jsonObject.put("data", jsonData);
					
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


		private String title; //17-1015案件名
		private String caseDocNo; //17-1015案件文号
		private String step;
		private String caseParties; 
		private String caseLegalAgent;
		private String address;
		private String phoneNumber;
		private String caseDescription;
		private String caseDocuments;	
		private String caseImages;	
		private String caseVideos;	
		private String caseThumbnails;
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
		private Integer expirseDays;
		
		public Integer getExpirseDays() {
			return expirseDays;
		}
		public void setExpirseDays(Integer expirseDays) {
			this.expirseDays = expirseDays;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCaseDocuments() {
			return caseDocuments;
		}
		public void setCaseDocuments(String caseDocuments) {
			this.caseDocuments = caseDocuments;
		}
		public String getCaseImages() {
			return caseImages;
		}
		public void setCaseImages(String caseImages) {
			this.caseImages = caseImages;
		}
		public String getCaseVideos() {
			return caseVideos;
		}
		public void setCaseVideos(String caseVideos) {
			this.caseVideos = caseVideos;
		}
		public String getCaseThumbnails() {
			return caseThumbnails;
		}
		public void setCaseThumbnails(String caseThumbnails) {
			this.caseThumbnails = caseThumbnails;
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
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getCaseDocNo() {
			return caseDocNo;
		}
		public void setCaseDocNo(String caseDocNo) {
			this.caseDocNo = caseDocNo;
		}

		public String getStep() {
			return step;
		}
		public void setStep(String step) {
			this.step = step;
		}
	}
	
	public class ApiStep
	{
		public ApiStep(String stage, String name, String status,String remark, String step){
			this.stage = stage;
			this.name = name;
			this.status = status;
			this.remark = remark;
			this.step = step;
		}
		private String stage;
		private String name;
		private String status;
		private String remark;
		private String step;
		
		public String getStep() {
			return step;
		}
		public void setStep(String step) {
			this.step = step;
		}
		public String getStage() {
			return stage;
		}
		public void setStage(String stage) {
			this.stage = stage;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
	}
	

}