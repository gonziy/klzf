/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.oa.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.calendar.BusinessCalendar;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.realm.ldap.DefaultLdapContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.common.utils.StringUtils;
import gov.kl.chengguan.modules.act.entity.Act;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.act.utils.ActUtils;
import gov.kl.chengguan.modules.gen.dao.GenTableDao;
import gov.kl.chengguan.modules.oa.dao.OaCaseDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.entity.OaDoc;
import gov.kl.chengguan.modules.sys.dao.OfficeDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.interceptor.MobileInterceptor;
import gov.kl.chengguan.modules.sys.utils.UserUtils;
/**
 * OaCaseService
 * @author
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OaCaseService extends CrudService<OaCaseDao, OaCase> {
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OaCaseDao caseDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private OfficeDao officeDao;
	
	public OaCase getByProcInsId(String procInsId) {
		return dao.getByProcInsId(procInsId);
	}
	
	public Page<OaCase> findPage(Page<OaCase> page, OaCase oaCase) {

		oaCase.setPage(page);
		page.setList(dao.findList(oaCase));
		return page;
	}

	/*
	 * 根据用户返回代办的案件
	 */
	public List<OaCase> findTodoTasks(String userId) {
		List<Act> acts = actTaskService.findTodoTasks(userId, ActUtils.PD_CASE[0]);
		List<OaCase> results = new ArrayList<OaCase>();
	
		for(Act act : acts)
		{
			ProcessDefinitionEntity prodef = (ProcessDefinitionEntity) act.getProcDef();
			String prodefkey = prodef.getKey();
			if(prodefkey.equals(ActUtils.PD_CASE[0]))
			{
				//根据business id 获取案件参数
				String businessId = act.getBusinessId();
				businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
				OaCase oaCase = caseDao.get(businessId);
				if(oaCase != null) {
					oaCase.setTask(act.getTask());
					oaCase.setProcessInstance(act.getProcIns());
					oaCase.setProcessDefinition(act.getProcDef());
					oaCase.setVariables(act.getVars().getVariableMap());
					results.add(oaCase);
				}
			}
		}
		return results;
	}
	
	/*
	 * 根据用户返回已完成的案件
	 */
	public List<OaCase> findFinishedTasks(String userId) {
		List<Act> acts = actTaskService.findFinishedTasks(userId, ActUtils.PD_CASE[0]);
		List<OaCase> results = new ArrayList<OaCase>();
	
		for(Act act : acts)
		{
			if(act.getProcDefKey() == ActUtils.PD_CASE[0]) {
				//根据business id 获取案件参数
				String businessId = act.getBusinessId();
				businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
				OaCase oaCase = caseDao.get(businessId);
				if(oaCase != null) {
					oaCase.setTask(act.getTask());
					oaCase.setProcessInstance(act.getProcIns());
					oaCase.setProcessDefinition(act.getProcDef());
					results.add(oaCase);
				}
			}
		}
		return results;
	}
	
	/**
	 * 审核新增或编辑
	 * @param oaCase
	 */
	@Transactional(readOnly = false)
	public void save(OaCase oaCase, Map<String, Object>vars) {
		
		// 申请发起
		if (StringUtils.isBlank(oaCase.getId())){
			oaCase.preInsert();
			// 申请开始时间
			oaCase.setCaseRegStartDate(Calendar.getInstance().getTime());
			oaCase.setCaseStage(1);
			dao.insert(oaCase);
			
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_CASE[0], ActUtils.PD_CASE[1], 
					oaCase.getId(), oaCase.getTitle(),
					vars);			
		}
		else{
			// 更新数据库
			oaCase.preUpdate();
			dao.update(oaCase);
		}
	}
	
	/*
	 * 为移动端提交第一步，完成直接到承办机构审批的步骤
	 */
	@Transactional(readOnly = false)
	public void mobileSave(String userId, OaCase oaCase)
	{
		// 需要将oaCase的setCaseStage设置为1，setCaseCheckFlag(1)
		Map<String, Object> vars = Maps.newHashMap();
		if (StringUtils.isBlank(oaCase.getId())){
			oaCase.preInsert();
			// 申请开始时间
			oaCase.setCaseRegStartDate(Calendar.getInstance().getTime());
			oaCase.setCaseStage(1);
			// 补充需要完成的字段一次性全部更新到库里
			oaCase.setCaseCheckFlag(true);
			
			oaCase.setAutoId(Integer.parseInt(caseDao.getMax().toString()));
			Calendar calendar = Calendar.getInstance();
			String year = String.valueOf(calendar.YEAR);
			String officeCode = userDao.get(userId).getOffice().getCode();
			String caseId = String.format("%03d", oaCase.getAutoId()+1);  
			oaCase.setCaseDocNo(year + "05" + officeCode + caseId);
			
			oaCase.setCaseCheckResult("手机端，自动通过初审认证");
			dao.insert(oaCase);
		
			vars.put("applyer", userId);
			// 启动流程
			String procInsId =actTaskService.startProcess(ActUtils.PD_CASE[0], ActUtils.PD_CASE[1], 
					oaCase.getId(), oaCase.getTitle(),
					vars);	
			
			// 等待流程启动完成
			//try {Thread.sleep(2000);	}catch(Exception ex)	{ex.printStackTrace();}
			
			// 设置vars
			vars.put("regCheckPass", 1);
			vars.put("caseAssigneeIds", oaCase.getAssigneeIds());	
			String task13DefKey = "utLaShp_Cbjg";
			actTaskService.jumpTask(procInsId, task13DefKey, vars);
		}
	}

	/*
	 * iReturnState页面返回的状态
	 * 1： 同意
	 * 0: 驳回(更新数据)
	 */
	@Transactional(readOnly = false)
	public void mobileSaveStep(OaCase oaCase, int iReturnState) 
	{
		/*
		 * 1030更新数据库后，需要设置oaCase相应字段，在此提交时完成更新
		 */
		oaCase.getAct().setComment(iReturnState ==1?"[同意] ":"[驳回] ");
		oaCase.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaCase.getAct().getTaskDefKey();
		Map<String, Object> vars = Maps.newHashMap();
		if ("utLaShp_Cbjg".equals(taskDefKey)){
			oaCase.setInstitutionRegApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			oaCase.setCaseStage((iReturnState ==1) ? 1: 5);
			dao.updateInstitutionRegOption(oaCase);
			// 提交流程任务
			vars.put("regInstitutionPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utLaShp_Fgld".equals(taskDefKey)){
			oaCase.setDeptLeaderRegApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateDeptLeaderRegOption(oaCase);
			// 提交流程任务
			vars.put("regDeptLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utLaShp_Zgld".equals(taskDefKey)){
			oaCase.setMainLeaderRegApproval((iReturnState ==1)?true: false); 
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			if(iReturnState == 1) {
				oaCase.setCaseRegEndDate(Calendar.getInstance().getTime());
				oaCase.setCaseStage(2);
				dao.updateMainLeaderRegOption1(oaCase);		
			}
			else
				dao.updateMainLeaderRegOption(oaCase);		
			// 提交流程任务
			vars.put("regMainLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utAnjianDiaocha".equals(taskDefKey)){
			oaCase.setCaseStage(2);
			if(iReturnState >= 0)
			{
				if(iReturnState == 1) {
					// 设置案件调查结束时间
					oaCase.setCaseSurveyEndDate(Calendar.getInstance().getTime());
					oaCase.setCaseStage(3);
					vars.put("regPass", 1);
					actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
				}
				dao.updateCaseSurveyEndData(oaCase);
			}
		}
		// 立案审批结束
		//
		else if ("utXzhChf_CbrYj".equals(taskDefKey)){
			if(iReturnState == 1) {
				oaCase.setCaseStage(3);
				// 只有不是拒绝的任务才能更新开始时间
				oaCase.setCasePenalStartDate(Calendar.getInstance().getTime());
				if(oaCase.getRejectFlag()) {
					// 是被驳回的流程，但作为基本的流程，除了不干，只能让iReturnState=1...
					oaCase.setRejectFlag((iReturnState ==1)?false: true);
					dao.updateAssigneePenalOption(oaCase);
				}
				else dao.updateAssigneePenalOption1(oaCase);
				
				// 提交流程任务
				actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
			}
		}
		else if ("utXzhChf_Cbjg".equals(taskDefKey)){
			oaCase.setInstitutionPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateInstitutionPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalInstitutionPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utXzhChf_AjGlZhx".equals(taskDefKey)){
			oaCase.setCaseMgtCenterPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateMgtCenterPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalMgtCenterPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utXzhChf_Fgld".equals(taskDefKey)){
			oaCase.setDeptLeaderPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateDeptLeaderPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalDeptLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);		
		}	
		else if ("utXzhChf_Zgld".equals(taskDefKey)){
			oaCase.setMainLeaderPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			if(iReturnState == 1) {
				oaCase.setCasePenalEndDate(Calendar.getInstance().getTime());
				oaCase.setCaseStage(4);
				dao.updateMainLeaderPenalOption1(oaCase);
			}
			else 
				dao.updateMainLeaderPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalMainLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);	
		}
		// 行政处罚审批结束
		//
		else if ("utJaShp_Chbr".equals(taskDefKey)){
			if(iReturnState == 1) {
				oaCase.setCaseStage(4);
				oaCase.setCaseCloseUpStartDate(Calendar.getInstance().getTime());
				if(oaCase.getRejectFlag()){
					oaCase.setRejectFlag((iReturnState ==1)?false: true);
					dao.updateAssigneeCloseOption(oaCase);
				}
				else dao.updateAssigneeCloseOption1(oaCase);

				// 提交流程任务
				actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
			}
		}
		else if ("utJaShp_Cbjg".equals(taskDefKey)){
			oaCase.setInstitutionCloseCaseApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateInstitutionCloseOption(oaCase);
			// 提交流程任务
			vars.put("closeInstitutionPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utJaShp_AjGlZhx".equals(taskDefKey)){
			oaCase.setCaseMgtCenterCloseCaseApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateMgtCenterCloseOption(oaCase);
			// 提交流程任务
			vars.put("closeMgtCenterPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utJaShp_Zgld".equals(taskDefKey)){
			oaCase.setMainLeaderCloseCaseApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			if(iReturnState ==1) {
				oaCase.setCaseCloseUpEndDate(Calendar.getInstance().getTime());
				// 设置为完结状态
				oaCase.setCaseStage(5);
				dao.updateMainLeaderCloseOption1(oaCase);
			}
			else 
				dao.updateMainLeaderCloseOption(oaCase);
				
			// 提交流程任务
			vars.put("closeMainLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}	
		else if ("endevent_case".equals(taskDefKey)){
			// 结案审批
			
		}		
		// 未知环节，直接返回
		else{
			return;
		}		
		
	}
	
	/**
	 * 审核审批保存
	 * @param oaCase
	 */
	@Transactional(readOnly = false)
	public void saveStep(OaCase oaCase) {		
		// 设置意见
		// 如果是yes则设置为同意，否则，+ comment
		oaCase.getAct().setComment(("yes".equals(oaCase.getAct().getFlag())?"[同意] ":"[驳回] ")+oaCase.getAct().getComment());
		oaCase.preUpdate();
		
		/*
		 * 1030 更新数据，增加对操作人和操作时间的处理
		 * 可以修改此处的getId，记录人员的id或者name
		 */
		String currentUser = UserUtils.getUser().getId();
		
		/*
		 * 获取审批返回值
		 * 0 ：驳回
		 * 1：通过审批
		 * -1：未定状态(Cancel)
		 */
		int iReturnState = -1;
		String flag = oaCase.getAct().getFlag();

		if(flag != null) {
			if("yes".equals(flag))
				iReturnState = 1;
			else if("no".equals(flag))
				iReturnState = 0;
		}
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaCase.getAct().getTaskDefKey();
		Map<String, Object> vars = Maps.newHashMap();
		if ("utAnjianChushen".equals(taskDefKey)){
			//

			if(iReturnState == 1) {
				oaCase.setCaseStage(1);
				oaCase.setCaseCheckFlag(true);
			}
			else {
				// 设置为案件完结状态
				oaCase.setCaseStage(5);
				oaCase.setCaseCheckFlag(false);
			}
			dao.updateCaseCheckResult(oaCase);
			//${pass==1}
			vars.put("regCheckPass", iReturnState);
		    
			 //Map<String, Object> progress = actTaskService.getProcessProgress(oaCase.getAct().getProcInsId());
//			Map<String, Object> progress = actTaskService.getProcessProgress(oaCase.getProcessInstanceId(), taskDefKey);
//			System.out.println("progress:" + progress.get("节点名称"));
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);		
		}
		else if ("utAnjianLuru".equals(taskDefKey)){
			if(iReturnState == 1) {
				oaCase.setLaRecAssignee(currentUser);
				oaCase.setLaRecDate(Calendar.getInstance().getTime());

				Long maxId = caseDao.getMax();
				oaCase.setAutoId(Integer.parseInt(maxId.toString()) + 1);
				Calendar calendar = Calendar.getInstance();
				String year = String.valueOf(calendar.get(Calendar.YEAR));
				User currentUserModel = userDao.get(currentUser);
				String parentOfficeId = currentUserModel.getOffice().getParent().getId();
				String officeCode = officeDao.get(parentOfficeId).getCode();
				
				
				String caseId = String.format("%03d", oaCase.getAutoId());  
				oaCase.setCaseDocNo(year + "05" + officeCode + caseId);
				
				dao.updateCaseRecord(oaCase);
				// 提交流程任务
				vars.put("caseAssigneeIds", oaCase.getAssigneeIds());			
				actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
			}
		}
		else if ("utLaShp_Cbjg".equals(taskDefKey)){
			//立案 承办机构
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setDeptLeaderRegOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setInstitutionRegApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			oaCase.setCaseStage((iReturnState ==1) ? 1: 5);
			
			oaCase.setInstitutionRegAssignee(currentUser);
			oaCase.setInstitutionRegDate(Calendar.getInstance().getTime());
			
			dao.updateInstitutionRegOption(oaCase);
			// 提交流程任务
			vars.put("regInstitutionPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utLaShp_Fgld".equals(taskDefKey)){
			//立案 分管领导
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setMainLeaderRegOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setDeptLeaderRegApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			
			oaCase.setDeptLeaderRegAssignee(currentUser);
			oaCase.setDeptLeaderRegDate(Calendar.getInstance().getTime());
			dao.updateDeptLeaderRegOption(oaCase);
			// 提交流程任务
			vars.put("regDeptLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utLaShp_Zgld".equals(taskDefKey)){
			oaCase.setMainLeaderRegApproval((iReturnState ==1)?true: false); 
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			oaCase.setMainLeaderRegAssignee(currentUser);
			
			if(iReturnState == 1) {
				oaCase.setCaseRegEndDate(Calendar.getInstance().getTime());
				oaCase.setCaseStage(2);
				dao.updateMainLeaderRegOption1(oaCase);		
			}
			else
				dao.updateMainLeaderRegOption(oaCase);		
			// 提交流程任务
			vars.put("regMainLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utAnjianDiaocha".equals(taskDefKey)){
			oaCase.setCaseStage(2);
			if(iReturnState >= 0)
			{
				if(iReturnState == 1) {
					// 设置案件调查结束时间
					oaCase.setCaseSurveyEndDate(Calendar.getInstance().getTime());
					oaCase.setCaseStage(3);
					vars.put("regPass", 1);
					actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
				}
				dao.updateCaseSurveyEndData(oaCase);
			}
		}
		// 立案审批结束
		//
		else if ("utXzhChf_CbrYj".equals(taskDefKey)){
			//处罚  承办人
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setInstitutionPenalOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			if(iReturnState == 1) {
				oaCase.setCaseStage(3);
				// 只有不是拒绝的任务才能更新开始时间
				oaCase.setPenalAssignee(currentUser);
				oaCase.setCasePenalStartDate(Calendar.getInstance().getTime());
				if(oaCase.getRejectFlag()) {
					// 是被驳回的流程，但作为基本的流程，除了不干，只能让iReturnState=1...
					oaCase.setRejectFlag((iReturnState ==1)?false: true);
					dao.updateAssigneePenalOption(oaCase);
				}
				else dao.updateAssigneePenalOption1(oaCase);
				
				// 提交流程任务
				actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
			}
		}
		else if ("utXzhChf_Cbjg".equals(taskDefKey)){
			//处罚  承办机构
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setCaseMgtCenterPenalOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setInstitutionPenalAssignee(currentUser);
			oaCase.setInstitutionPenalDate(Calendar.getInstance().getTime());
			oaCase.setInstitutionPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateInstitutionPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalInstitutionPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utXzhChf_AjGlZhx".equals(taskDefKey)){
			//处罚  案管中心
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setDeptLeaderPenalOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setCaseMgtCenterPenalAssignee(currentUser);
			oaCase.setCaseMgtCenterPenalDate(Calendar.getInstance().getTime());
			
			oaCase.setCaseMgtCenterPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateMgtCenterPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalMgtCenterPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utXzhChf_Fgld".equals(taskDefKey)){
			//处罚  分管领导
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setMainLeaderPenalOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setDeptLeaderPenalAssignee(currentUser);
			oaCase.setDeptLeaderPenalDate(Calendar.getInstance().getTime());
			
			oaCase.setDeptLeaderPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateDeptLeaderPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalDeptLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);		
		}	
		else if ("utXzhChf_Zgld".equals(taskDefKey)){
			oaCase.setMainLeaderPenalAssignee(currentUser);
			oaCase.setMainLeaderPenalApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			if(iReturnState == 1) {
				oaCase.setCasePenalEndDate(Calendar.getInstance().getTime());
				oaCase.setCaseStage(4);
				dao.updateMainLeaderPenalOption1(oaCase);
			}
			else 
				dao.updateMainLeaderPenalOption(oaCase);
			// 提交流程任务
			vars.put("penalMainLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);	
		}
		// 行政处罚审批结束
		//
		else if ("utJaShp_Chbr".equals(taskDefKey)){
			//结案 承办人
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setInstitutionCloseCaseOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			if(iReturnState == 1) {
				oaCase.setCaseStage(4);
				oaCase.setCloseUpAssignee(currentUser);
				oaCase.setCaseCloseUpStartDate(Calendar.getInstance().getTime());
				if(oaCase.getRejectFlag()){
					oaCase.setRejectFlag((iReturnState ==1)?false: true);
					dao.updateAssigneeCloseOption(oaCase);
				}
				else dao.updateAssigneeCloseOption1(oaCase);

				// 提交流程任务
				actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
			}
		}
		else if ("utJaShp_Cbjg".equals(taskDefKey)){
			//结案 承办机构
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setCaseMgtCenterCloseCaseOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setInstitutionCloseAssignee(currentUser);
			oaCase.setInstitutionCloseDate(Calendar.getInstance().getTime());
			
			oaCase.setInstitutionCloseCaseApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateInstitutionCloseOption(oaCase);
			// 提交流程任务
			vars.put("closeInstitutionPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utJaShp_AjGlZhx".equals(taskDefKey)){
			//结案 案管中心
			
			//如果上级拒绝，则本次提交清空上次审批意见,以保证状态读取
			OaCase tmpModelCase = caseDao.get(oaCase.getId());
			tmpModelCase.setMainLeaderCloseCaseOption(null);
			caseDao.update(tmpModelCase);
			//结束
			
			oaCase.setCaseMgtCenterCloseAssignee(currentUser);
			oaCase.setCaseMgtCenterCloseDate(Calendar.getInstance().getTime());
			
			oaCase.setCaseMgtCenterCloseCaseApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			dao.updateMgtCenterCloseOption(oaCase);
			// 提交流程任务
			vars.put("closeMgtCenterPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utJaShp_Zgld".equals(taskDefKey)){
			oaCase.setMainLeaderCloseAssignee(currentUser);
			
			oaCase.setMainLeaderCloseCaseApproval((iReturnState ==1)?true: false);
			oaCase.setRejectFlag((iReturnState ==1)?false: true);
			if(iReturnState ==1) {
				oaCase.setCaseCloseUpEndDate(Calendar.getInstance().getTime());
				// 设置为完结状态
				oaCase.setCaseStage(5);
				dao.updateMainLeaderCloseOption1(oaCase);
			}
			else 
				dao.updateMainLeaderCloseOption(oaCase);
				
			// 提交流程任务
			vars.put("closeMainLeaderPass", iReturnState);
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}	
		else if ("endevent_case".equals(taskDefKey)){
			// 结案审批
			
		}		
		// 未知环节，直接返回
		else{
			return;
		}
		
	}
	
	@Transactional(readOnly = false)
	public void CompleteTask(OaCase oaCase)	
	{
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(oaCase.getAct().getFlag())? "1" : "0");
		actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
	}
	
}
