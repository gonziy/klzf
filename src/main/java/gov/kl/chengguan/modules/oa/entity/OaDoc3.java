package gov.kl.chengguan.modules.oa.entity;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;

import gov.kl.chengguan.common.persistence.ActEntity;

/*
 * 公文流转实体类
 */
public class OaDoc3 extends ActEntity<OaDoc3> {

	/**
	 * 版本id
	 */
	private static final long serialVersionUID = 1L;	

	// 发文的标题
	private String docTitle;
	// 到公文的链接，可能一次发布多个文件，用;分割
	private String attachLinks;
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
	//状态  0:公文提报  1:主任审批 2:领导审批 3:主任处理领导意见  (4:其他人传阅 )
	private String DRStage;
	
	/*
	 * 下面的这些涉及到每个审阅流程相关，再这里设置不太合适
	 * 审批意见是否要保存？默认为 已阅，
	 */

	// 与Doc实例相关的流程定义，
	// 所在的流程实例、所在任务，其中的流程变量，
	// 流程历史实例
	private ProcessDefinition processDefinition;
	//
	// 运行中的流程实例
	private ProcessInstance processInstance;
	private String processInstanceId; // 流程实例编号
	//
	private Task task;
	private Map<String, Object> variables;
	
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;
	
	// 查询时使用的变量列表
	private String docQueryTitle;	 
	private Date docQueryCreateDateStart;  
	private Date docQueryCreateDateEnd;
	private String docQueryStage;

	/*
	 * 
	 */
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	
	public String  getProcessInstanceId() {
		return processInstanceId;		
	}
	
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;		
	}
	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	/*
	 * 
	 */
	public OaDoc3() {
		super();
		DRStage = "0";
	}

	public OaDoc3(String id){
		super(id);
		DRStage = "0";
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getAttachLinks() {
		return attachLinks;
	}

	public void setAttachLinks(String attachLinks) {
		this.attachLinks = attachLinks;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getOfficeHeaderApproveDate() {
		return officeHeaderApproveDate;
	}

	public void setOfficeHeaderApproveDate(Date officeHeaderApproveDate) {
		this.officeHeaderApproveDate = officeHeaderApproveDate;
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

	public String getDocQueryTitle() {
		return docQueryTitle;
	}

	public void setDocQueryTitle(String docQueryTitle) {
		this.docQueryTitle = docQueryTitle;
	}

	public Date getDocQueryCreateDateStart() {
		return docQueryCreateDateStart;
	}

	public void setDocQueryCreateDateStart(Date docQueryCreateDateStart) {
		this.docQueryCreateDateStart = docQueryCreateDateStart;
	}

	public Date getDocQueryCreateDateEnd() {
		return docQueryCreateDateEnd;
	}

	public void setDocQueryCreateDateEnd(Date docQueryCreateDateEnd) {
		this.docQueryCreateDateEnd = docQueryCreateDateEnd;
	}

	public String getDocQueryStage() {
		return docQueryStage;
	}

	public void setDocQueryStage(String docQueryStage) {
		this.docQueryStage = docQueryStage;
	}
	
}
