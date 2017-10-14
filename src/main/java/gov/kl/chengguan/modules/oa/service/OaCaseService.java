/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.oa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.common.utils.StringUtils;
import gov.kl.chengguan.modules.act.entity.Act;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.act.utils.ActUtils;
import gov.kl.chengguan.modules.oa.dao.OaCaseDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.entity.OaDoc;
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
	
	public OaCase getByProcInsId(String procInsId) {
		return dao.getByProcInsId(procInsId);
	}
	
	public Page<OaCase> findPage(Page<OaCase> page, OaCase oaCase) {

		oaCase.setPage(page);
		page.setList(dao.findList(oaCase));
		return page;
	}

	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<OaCase> findTodoTasks(String userId) {
		List<Act> acts = actTaskService.findTodoTasks(userId);
		List<OaCase> results = new ArrayList<OaCase>();
	
		for(Act act : acts)
		{
			/*
			 * 
			 */
			System.out.println("==============>act info: getBusinessId" + act.getBusinessId() 
				+"Task id"+ act.getTaskId()
				+"setProcessInstance" + act.getProcIns().toString());
			
			OaCase oaCase = dao.get(act.getBusinessId());
			oaCase.setTask(act.getTask());
			oaCase.setProcessInstance(act.getProcIns());
			oaCase.setProcessDefinition(act.getProcDef());
			oaCase.setVariables((Map<String, Object>)act.getVars());
			results.add(oaCase);
		}
		return results;
	}
	
	public List<OaCase> findFinishedTasks(String userId) {
		List<Act> acts = actTaskService.findFinishedTasks(userId);
		List<OaCase> results = new ArrayList<OaCase>();
	
		for(Act act : acts)
		{
			OaCase oaCase = dao.get(act.getBusinessId());
			oaCase.setTask(act.getTask());
			oaCase.setProcessInstance(act.getProcIns());
			oaCase.setProcessDefinition(act.getProcDef());
			results.add(oaCase);
		}
		return results;
	}
	
	/**
	 * 审核新增或编辑
	 * @param oaCase
	 */
	@Transactional(readOnly = false)
	public void save(OaCase oaCase) {
		
		// 申请发起
		if (StringUtils.isBlank(oaCase.getId())){
			oaCase.preInsert();
			dao.insert(oaCase);
			
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("applyer", "1");
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_CASE[0], ActUtils.PD_CASE[1], 
					oaCase.getId(), oaCase.getCaseTitle(),
					vars);			
		}
		else{
			// 更新数据库
			oaCase.preUpdate();
			dao.update(oaCase);
		}
	}

	/**
	 * 审核审批保存
	 * @param oaCase
	 */
	@Transactional(readOnly = false)
	public void SaveCaseStep(OaCase oaCase) {		
		// 设置意见
		// 如果是yes则设置为同意，否则，+ comment
		oaCase.getAct().setComment(("yes".equals(oaCase.getAct().getFlag())?"[同意] ":"[驳回] ")+oaCase.getAct().getComment());
		oaCase.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaCase.getAct().getTaskDefKey();

		// 审核环节
		// utAnjianShenbao  案件申报
		// utAnjianChushen  案件初审
		// utAnjianLuru
		// utLaShp_Cbjg
		// utLaShp_Fgld
		// utLaShp_Zgld
		// utAnjianDiaocha
		// 
		// utXzhChf_CbrYj
		// utXzhChf_Cbjg
		// utXzhChf_AjGlZhx
		// utXzhChf_Fgld
		// utXzhChf_Zgld
		// 
		// utJaShp_Cbr
		// utJaShp_Cbjg
		// utJaShp_AjGlZhx
		// utJaShp_Zgld
		Map<String, Object> vars = Maps.newHashMap();
		
		if ("utAnjianShenbao".equals(taskDefKey)){
		}
		else if ("utAnjianChushen".equals(taskDefKey)){
			dao.updateCaseCheckResult(oaCase);
			// 提交流程任务
			vars.put("bCaseRegPassed", "yes".equals(oaCase.getAct().getFlag())? "1" : "0");
			System.out.println("---->>set bCaseRegPassed to 1");
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utAnjianLuru".equals(taskDefKey)){
			dao.updateCaseRecord(oaCase);
			// 提交流程任务
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utLaShp_Cbjg".equals(taskDefKey)){
			dao.updateInstitutionRegOption(oaCase);
			// 提交流程任务
			vars.put("institutionRegApproval", "yes".equals(oaCase.getAct().getFlag())? "1" : "0");
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utLaShp_Fgld".equals(taskDefKey)){
			dao.updateDeptLeaderRegOption(oaCase);
			// 提交流程任务
			vars.put("deptLeaderRegApproval", "yes".equals(oaCase.getAct().getFlag())? "1" : "0");
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);

		}
		else if ("utLaShp_Zgld".equals(taskDefKey)){
			dao.updateMainLeaderRegOption(oaCase);
			// 提交流程任务
			vars.put("mainLeaderRegApproval", "yes".equals(oaCase.getAct().getFlag())? "1" : "0");
			actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
		}
		else if ("utAnjianDiaocha".equals(taskDefKey)){

		}
		else if ("endevent_LaShp".equals(taskDefKey)){
			// 立案审批结束，更新数据库

		}				
		//
		else if ("utAnjianLuru".equals(taskDefKey)){

		}
		else if ("utXzhChf_CbrYj".equals(taskDefKey)){

		}
		else if ("utXzhChf_Cbjg".equals(taskDefKey)){

		}
		else if ("utXzhChf_AjGlZhx".equals(taskDefKey)){

		}
		else if ("utXzhChf_Fgld".equals(taskDefKey)){
			
		}	
		else if ("utXzhChf_Zgld".equals(taskDefKey)){
			
		}
		else if ("endevent_XzhChf".equals(taskDefKey)){
			// 行政处罚审批结束，更新数据库

		}	
		//
		else if ("utJaShp_Cbr".equals(taskDefKey)){

		}
		else if ("utJaShp_Cbjg".equals(taskDefKey)){

		}
		else if ("utJaShp_AjGlZhx".equals(taskDefKey)){

		}
		else if ("utJaShp_Zgld".equals(taskDefKey)){
			
		}	
		else if ("endevent_JaShp".equals(taskDefKey)){
			// 结案审批
			
		}		
		// 未知环节，直接返回
		else{
			return;
		}
		
	}
	
	@Transactional(readOnly = false)
	public void CompleteCurrentTask(OaCase oaCase)	
	{
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(oaCase.getAct().getFlag())? "1" : "0");
		actTaskService.complete(oaCase.getAct().getTaskId(), oaCase.getAct().getProcInsId(), oaCase.getAct().getComment(), vars);
	}
}
