package gov.kl.chengguan.modules.oa.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.service.BaseService;
import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.common.utils.Collections3;
import gov.kl.chengguan.modules.act.entity.Act;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.act.utils.ActUtils;
import gov.kl.chengguan.modules.oa.dao.OaDoc3Dao;
import gov.kl.chengguan.modules.oa.entity.OaDoc3;
import gov.kl.chengguan.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OaDoc3RoutingService extends CrudService<OaDoc3Dao, OaDoc3> {
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OaDoc3Dao oaDoc3Dao;
	
	public OaDoc3 getByProcInsId(String procInsId) {
		return dao.getByProcInsId(procInsId);
	}
	
	public Page<OaDoc3> findPage(Page<OaDoc3> page, OaDoc3 oaDoc3) {
		oaDoc3.setPage(page);
		page.setList(dao.findList(oaDoc3));
		return page;
	}

	/*
	 * 根据用户返回代办的案件
	 */
	public List<OaDoc3> findTodoTasks(String userId) {
		List<Act> acts = actTaskService.findTodoTasks(userId);
		List<OaDoc3> results = new ArrayList<OaDoc3>();
	
		for(Act act : acts)
		{
			if(act.getProcDefKey() == ActUtils.PD_DOC3_ROUTING[0]) 
			{
				//根据business id 获取案件参数
				String businessId = act.getBusinessId();
				businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
				OaDoc3 oaDoc3 = oaDoc3Dao.get(businessId);
				if(oaDoc3 != null) {
					oaDoc3.setTask(act.getTask());
					oaDoc3.setProcessInstance(act.getProcIns());
					oaDoc3.setProcessDefinition(act.getProcDef());
					oaDoc3.setVariables(act.getVars().getVariableMap());
					results.add(oaDoc3);
				}
			}
		}
		return results;
	}
	
	/*
	 * 根据用户返回已完成的案件
	 */
	public List<OaDoc3> findFinishedTasks(String userId) {
		List<Act> acts = actTaskService.findFinishedTasks(userId);
		List<OaDoc3> results = new ArrayList<OaDoc3>();
	
		for(Act act : acts)
		{
			if(act.getProcDefKey() == ActUtils.PD_DOC3_ROUTING[0])
			{
				//根据business id 获取案件参数
				String businessId = act.getBusinessId();
				businessId = businessId.substring(businessId.indexOf(":") + 1,businessId.length());
				OaDoc3 oaDoc3 = oaDoc3Dao.get(businessId);
				if(oaDoc3 != null) {
					oaDoc3.setTask(act.getTask());
					oaDoc3.setProcessInstance(act.getProcIns());
					oaDoc3.setProcessDefinition(act.getProcDef());
					results.add(oaDoc3);
				}
			}
		}
		return results;
	}
	
	/*
	 * 手机启动公文传阅流程
	 */
	@Transactional(readOnly = false)
	public void mobileSave(String userId,OaDoc3 oaDoc3) {
		// 申请发起
		Map<String, Object> vars = Maps.newHashMap();
		if (StringUtils.isBlank(oaDoc3.getId())){
			oaDoc3.preInsert();
			dao.insert(oaDoc3);
			
			// 启动流程
			vars.put("applyer", userId);
			actTaskService.startProcess(ActUtils.PD_DOC3_ROUTING[0], ActUtils.PD_DOC3_ROUTING[1], 
					oaDoc3.getId(), oaDoc3.getDocTitle(), vars);
		} 
		else{
			oaDoc3.preUpdate();
			dao.update(oaDoc3);
			oaDoc3.getAct().setComment(("yes".equals(oaDoc3.getAct().getFlag())?"[重申] ":"[销毁] ")+oaDoc3.getAct().getComment());
			vars.put("pass", "yes".equals(oaDoc3.getAct().getFlag())? "1" : "0");
			actTaskService.complete(oaDoc3.getAct().getTaskId(), oaDoc3.getAct().getProcInsId(), 
					oaDoc3.getAct().getComment(), oaDoc3.getDocTitle(), vars);
		}
	}
	
	/**
	 * 审核新增或编辑
	 * @param oaDoc3
	 */
	@Transactional(readOnly = false)
	public void save(OaDoc3 oaDoc3, Map<String, Object> vars) {
		// 申请发起
		if (StringUtils.isBlank(oaDoc3.getId())){
			oaDoc3.preInsert();
			dao.insert(oaDoc3);
			
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_DOC3_ROUTING[0], ActUtils.PD_DOC3_ROUTING[1], 
					oaDoc3.getId(), oaDoc3.getDocTitle(), vars);
		} 
		else{
			oaDoc3.preUpdate();
			dao.update(oaDoc3);
			oaDoc3.getAct().setComment(("yes".equals(oaDoc3.getAct().getFlag())?"[重申] ":"[销毁] ")+oaDoc3.getAct().getComment());
			vars.put("pass", "yes".equals(oaDoc3.getAct().getFlag())? "1" : "0");
			actTaskService.complete(oaDoc3.getAct().getTaskId(), oaDoc3.getAct().getProcInsId(), 
					oaDoc3.getAct().getComment(), oaDoc3.getDocTitle(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * @param oaDoc3
	 */
	@Transactional(readOnly = false)
	public void mobileSaveStep(OaDoc3 oaDoc3, int iReturnState) {		
		// 设置意见
		oaDoc3.getAct().setComment(iReturnState ==1?"[同意] ":"[驳回] ");
		oaDoc3.preUpdate();

		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaDoc3.getAct().getTaskDefKey();

		Map<String, Object> vars = Maps.newHashMap();
		// 审核环节
		if ("utOfficeHeaderApprove".equals(taskDefKey)){
			oaDoc3.setOfficeHeaderApproveDate(Calendar.getInstance().getTime());
			oaDoc3.setDRStage("1");
			oaDoc3.setOfficeHeaderApproval((iReturnState ==1)?true : false);
			oaDoc3Dao.updateOfficeHeaderApproval(oaDoc3);
			
			vars.put("pass", "yes".equals(oaDoc3.getAct().getFlag())? "1" : "0");			
		}
		else if ("utLeaderApprove".equals(taskDefKey)){
			oaDoc3.setLeaderApproveDate(Calendar.getInstance().getTime());
			oaDoc3.setDRStage("2");
			oaDoc3Dao.updateLeaderApproval(oaDoc3);
		}
		else if ("utInformApplyer".equals(taskDefKey)){
			// 
		}
		else if ("utOfficeHeaderDispatch".equals(taskDefKey)){
			oaDoc3.setDRStage("3");
			String[] ss = oaDoc3.getReviewersIDs1().trim().split(";");
			List<String> reviewers = new ArrayList<String>();
			for(String s : ss)
			{
				reviewers.add(s.trim());				
			}
			vars.put("approvers", reviewers);
			oaDoc3Dao.updateOfficeHeaderDispatch(oaDoc3);
		}
		else if ("utBrowseDoc".equals(taskDefKey)){ //11
			// stage 4
		}
		else if ("end_event".equals(taskDefKey)){
			// 这个语句没有执行，如果不行就只能使用空任务
//			oaDoc3.setDRStage("5");
//			oaDoc3Dao.updateDrStage(oaDoc3);
		}
		else{
			return;
		}
		
		// 提交流程任务
		actTaskService.complete(oaDoc3.getAct().getTaskId(), oaDoc3.getAct().getProcInsId(), oaDoc3.getAct().getComment(), vars);
	}
	
	/**
	 * 审核审批保存
	 * @param oaDoc3
	 */
	@Transactional(readOnly = false)
	public void saveStep(OaDoc3 oaDoc3) {		
		// 设置意见
		oaDoc3.getAct().setComment(("yes".equals(oaDoc3.getAct().getFlag())?"[同意] ":"[驳回] ")+oaDoc3.getAct().getComment());
		oaDoc3.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaDoc3.getAct().getTaskDefKey();

		Map<String, Object> vars = Maps.newHashMap();
		// 审核环节
		if ("utOfficeHeaderApprove".equals(taskDefKey)){
			oaDoc3.setOfficeHeaderApproveDate(Calendar.getInstance().getTime());
			oaDoc3.setDRStage("1");
			oaDoc3.setOfficeHeaderApproval("yes".equals(oaDoc3.getAct().getFlag())? true : false);		
			oaDoc3Dao.updateOfficeHeaderApproval(oaDoc3);
			
			vars.put("pass", "yes".equals(oaDoc3.getAct().getFlag())? "1" : "0");			
		}
		else if ("utLeaderApprove".equals(taskDefKey)){
			oaDoc3.setLeaderApproveDate(Calendar.getInstance().getTime());
			oaDoc3.setDRStage("2");
			oaDoc3Dao.updateLeaderApproval(oaDoc3);
		}
		else if ("utInformApplyer".equals(taskDefKey)){
			// 
		}
		else if ("utOfficeHeaderDispatch".equals(taskDefKey)){
			oaDoc3.setDRStage("3");
			String[] ss = oaDoc3.getReviewersIDs1().trim().split(";");
			List<String> reviewers = new ArrayList<String>();
			for(String s : ss)
			{
				reviewers.add(s.trim());				
			}
			vars.put("approvers", reviewers);
			oaDoc3Dao.updateOfficeHeaderDispatch(oaDoc3);
		}
		else if ("utBrowseDoc".equals(taskDefKey)){ //11
			// stage 4
		}
		else if ("end_event".equals(taskDefKey)){
			// 这个语句没有执行，如果不行就只能使用空任务
//			oaDoc3.setDRStage("5");
//			oaDoc3Dao.updateDrStage(oaDoc3);
		}
		else{
			return;
		}
		
		// 提交流程任务
		actTaskService.complete(oaDoc3.getAct().getTaskId(), oaDoc3.getAct().getProcInsId(), oaDoc3.getAct().getComment(), vars);
	}

}

