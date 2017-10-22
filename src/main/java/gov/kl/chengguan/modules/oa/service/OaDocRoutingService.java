package gov.kl.chengguan.modules.oa.service;


import java.util.ArrayList;
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
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.service.BaseService;
import gov.kl.chengguan.common.utils.Collections3;
import gov.kl.chengguan.modules.act.utils.ActUtils;
import gov.kl.chengguan.modules.oa.dao.OaDocDao;
import gov.kl.chengguan.modules.oa.entity.OaDoc;

@Service
@Transactional(readOnly = true)
public class OaDocRoutingService extends BaseService{
	@Autowired
	private OaDocDao docDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;

	/*
	 * 获取一个doc实例，查询其执行的情况，恢复其执行变量
	 */
	@SuppressWarnings("unchecked")
	public OaDoc get(String id) {
		OaDoc doc = docDao.get(id);
		Map<String,Object> variables=null;
		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(doc.getProcessInstanceId()).singleResult();
		if(historicProcessInstance!=null) {
			variables = Collections3.extractToMap(historyService
					.createHistoricVariableInstanceQuery()
					.processInstanceId(historicProcessInstance.getId())
					.list(), 
					"variableName", "value");
		} else {
			variables = runtimeService.getVariables(runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(doc.getProcessInstanceId()).active().singleResult().getId());
		}
		doc.setVariables(variables);
		return doc;
	}

	/**
	 * 启动流程
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(OaDoc doc, Map<String, Object> variables) {
		
		// 保存业务数据
		if (StringUtils.isBlank(doc.getId())){
			// 以前没有doc，新发文
			doc.preInsert();
			docDao.insert(doc);
			
			logger.debug("insert doc entity: {}", doc);
		}else{
			// 更新发文
			doc.preUpdate();
			docDao.update(doc);
			
			logger.debug("update doc entity: {}", doc);
		}

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(doc.getCurrentUser().getLoginName());
		
		// 启动流程
		String businessKey = doc.getId().toString();
		
		// !!! 这里需要对相应的字符串进行分解处理，填写doc中的相应字段
		String[] ss = doc.getDocApproverIDs().split(";");
		ArrayList< String> approvers = new ArrayList<String>();
		for(String s: ss)
		{
			System.out.println(s.trim());
			approvers.add(s.trim());
		}		
		
		variables.put("approvers", approvers);
		variables.put("type", "docRouting");
		variables.put("busId", businessKey);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
				ActUtils.PD_DOC_ROUTIN[0], businessKey, variables);
		doc.setProcessInstance(processInstance);
		
		// 更新流程实例ID
		doc.setProcessInstanceId(processInstance.getId());
		docDao.updateProcessInstanceId(doc);
		
		logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { 
				ActUtils.PD_LEAVE[0], businessKey, processInstance.getId(), variables });
		
	}
	
	/**
	 * 查询指定用户的待办任务
	 * @param userId 用户ID
	 * @return
	 */
	public List<OaDoc> findTodoTasks(String userId) {		
		List<OaDoc> results = new ArrayList<OaDoc>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery()
				.processDefinitionKey(ActUtils.PD_DOC_ROUTIN[0])
				.taskAssignee(userId).active()
				.orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		
		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery()
				.processDefinitionKey(ActUtils.PD_DOC_ROUTIN[0])
				.taskCandidateUser(userId).active()
				.orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		
		// 合并，这个版本的activiti有问题啊
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).active().singleResult();
			String businessKey = processInstance.getBusinessKey();
			OaDoc doc = docDao.get(businessKey);
			doc.setTask(task);
			doc.setProcessInstance(processInstance);
			doc.setProcessDefinition(repositoryService
					.createProcessDefinitionQuery()
					.processDefinitionId((processInstance.getProcessDefinitionId())).singleResult());
			results.add(doc);
		}
		return results;
	}

	/*
	 * 查询已经完成的任务
	 */
	public List<OaDoc> findFinishedTasks(String userId) {		
		List<OaDoc> results = new ArrayList<OaDoc>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<HistoricTaskInstance> userHistoricTasks = historyService.createHistoricTaskInstanceQuery()
			.processDefinitionKey(ActUtils.PD_DOC_ROUTIN[0])
			.taskAssignee(userId)
			.orderByTaskCreateTime().desc()
			.finished()
			.list();
		
		// 根据流程业务ID查询OaDocn表
		for (HistoricTaskInstance hti : userHistoricTasks) {
			String processInstanceId = hti.getProcessInstanceId();
			OaDoc doc = docDao.getByProcInsId(processInstanceId);
			results.add(doc);
		}
		
		return results;
	}
	
	public Page<OaDoc> find(Page<OaDoc> page, OaDoc doc) {
		doc.getSqlMap().put("dsf", dataScopeFilter(doc.getCurrentUser(), "o", "u"));
		
		doc.setPage(page);
		page.setList(docDao.findList(doc));
		
		for(OaDoc item : page.getList()) {
			String processInstanceId = item.getProcessInstanceId();
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
			item.setTask(task);
			HistoricProcessInstance historicProcessInstance = historyService
					.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if(historicProcessInstance!=null) {
				item.setHistoricProcessInstance(historicProcessInstance);
				item.setProcessDefinition(repositoryService
						.createProcessDefinitionQuery()
						.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
			} 
			else {
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).active().singleResult();
				if (processInstance != null){
					item.setProcessInstance(processInstance);
					item.setProcessDefinition(repositoryService
							.createProcessDefinitionQuery()
							.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
				}
			}
		}
		return page;
	}
	
	/*
	 * 完成任务
	 */
	public void  saveStep(OaDoc doc, String submitStr) {
		if(submitStr =="yes" || submitStr == "no") {
			Map<String, Object>variables = new HashMap<String, Object>();
	
			Task task = doc.getTask();
			String taskDefKey = task.getTaskDefinitionKey();
			if(taskDefKey == "utDocApprove")
			{
				// 是否需要代办
				variables.put("bNeedCommission", submitStr);
				taskService.complete(task.getId(), variables);
			}
			else if(taskDefKey == "utDocAssigneeApprove") {
				// 代办人是否确认OK
				variables.put("bAssigneeSayOk", submitStr);	
				taskService.complete(task.getId(), variables);
			}
		}
	}
	
	/*
	 * 
	 */
	public Map<String, Object> getProcessProgress(String processInstanceId) 
	{
		Map<String, Object> results = new HashMap<String, Object>();

		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
		String activityId = execution.getActivityId();
		
		if(activityId != null)
		{
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult();
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
			List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点
	
			for (ActivityImpl activityImpl : activitiList) {
	
				String id = activityImpl.getId();
	
				// 当前节点
				if (id.equals(activityId)) {
					Map<String, Object> properties = activityImpl.getProperties();
					results.put("节点名称", properties.get("name"));	
					
					ActivityBehavior activityBehavior = activityImpl.getActivityBehavior();
					if (activityBehavior instanceof UserTaskActivityBehavior) {
	
						Task currentTask = null;
						currentTask = taskService.createTaskQuery()
								.executionId(execution.getId())
								.taskDefinitionKey(execution.getActivityId()).singleResult();
						if(currentTask == null) return results;
						String assignee = currentTask.getAssignee();
						if(assignee != null)
						{
							results.put("处理人id", assignee);
							results.put("创建时间", currentTask.getCreateTime());
						}
						else {
							results.put("任务状态", "未签收");						
						}
					}
					break;
				}
			}
		}
		return results;
	}
	
	

}

