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
public class OaDoc extends ActEntity<OaDoc> {

	/**
	 * 版本id
	 */
	private static final long serialVersionUID = 1L;	

	// 发文的标题
	private String docTitle;
	// 到公文的链接，可能一次发布多个文件，用;分割
	private String docAttachmentLinks;	

	// 公文审阅人ID列表，用;分割
	private String docApproverIDs;
	// 到期日期
	private Date dueDate;
	
	/*
	 * 下面的这些涉及到每个审阅流程相关，再这里设置不太合适
	 * 审批意见是否要保存？默认为 已阅，
	 */

	//
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
	public OaDoc() {
		super();
	}

	public OaDoc(String id){
		super(id);
	}

	/*
	 * 
	 */
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getDocAttachmentLinks() {
		return docAttachmentLinks;
	}

	public void setDocAttachmentLinks(String docAttachmentLinks) {
		this.docAttachmentLinks = docAttachmentLinks;
	}

	public String getDocApproverIDs() {
		return docApproverIDs;
	}

	public void setDocApproverIDs(String docApproverIDs) {
		this.docApproverIDs = docApproverIDs;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}


	// 查询
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
	
	@Override
	public String toString() {
		return "OaDoc [docTitle=" + docTitle 
				+ ", docAttachmentLinks=" + docAttachmentLinks 
				+ ", docApproverIDs=" + docApproverIDs 
				+ ", dueDate=" + dueDate 
				+ ", createBy=" + createBy.getId() 
				+ ", createTime=" + createDate.toString() 
				+"]";
	}
}
