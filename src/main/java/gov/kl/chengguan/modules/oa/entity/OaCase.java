package gov.kl.chengguan.modules.oa.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Case;
import org.apache.xmlbeans.impl.jam.internal.elements.VoidClassImpl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.mail.handlers.text_html;

import gov.kl.chengguan.common.persistence.ActEntity;

/*
 * 公文流转实体类
 */
public class OaCase extends ActEntity<OaCase> {
	/**
	 * 版本id
	 */
	private static final long serialVersionUID = 1L;	
	
	
	/*
	 * 需要对案件经办各个环节，时间进行记录
	 * 每个流程的办理人是否需要临时修改，人员变动会引起问题
	 */
	// 案件当事人
	private String caseParties; 
	// 法定代理人
	private String caseLegalAgent;
	// 地址
	private String address;
	// 电话
	private String  phoneNumber;
	// 描述
	private String caseDescription;
	// 案件的链接，可能一次发布多个文件，用;分割
	private String caseAttachmentLinks;	
	// --
	// 案件来源，如果能列出应该时id值，
	private String caseSource;
	// 案件承办人,可以多个，用;隔开
	private String assigneeIds;
	// 案件的规范描述：[某行为] 违反了 [某条例]
	private String normCaseDescPart1;
	private String normCaseDescPart2;	
	public String getNormCaseDesc() {
		StringBuilder sb = new StringBuilder(normCaseDescPart1);
		sb.append(" 违反了： ").append(normCaseDescPart2).append(" 之规定");
		return sb.toString();
	}
	// ---
	// 承办机构意见
	private String institutionRegOption;
	// 承办机构确认
	private boolean institutionRegApproval;	
	// ----
	// 分管领导意见
	private String deptLeaderRegOption;
	// 分管领导确认
	private boolean deptLeaderRegApproval;	
	// -----
	// 主管领导意见
	private String mainLeaderRegOption;
	// 主管领导确认
	private boolean mainLeaderRegApproval;
	// 立案开始和完成时间
	private Date caseRegStartDate;
	private Date caseRegEndDate;
	// ------
	// 案件侦查，是否完成
	private boolean bCaseSurveyFinish;
	private Date caseSurveyEndDate;
	
	//==
	// 承办人意见：[依据法条]，给出处罚，[处罚类型]
	private String normAssigneePenalOptPart1;
	private String normAssigneePenalOptPart2;
	public String getNormAssigneePenalOpt() {
		StringBuilder sb = new StringBuilder();
		sb.append(" 建议依据： ").append(normAssigneePenalOptPart1).append(" 给于处罚: ")
			.append(normAssigneePenalOptPart2);
		return sb.toString();
	}
	private String assigneePenalOption;
	//===
	// 承办单位处罚意见：
	private String institutionPenalOption;
	private boolean institutionPenalApproval;
	//====
	// 案件管理中心处罚意见：
	private String caseMgtCenterPenalOption;
	private boolean caseMgtCenterPenalApproval;
	//=====
	// 分管领导处罚意见：
	private String deptLeaderPenalOption;
	private boolean deptLeaderPenalApproval;
	//======
	// 主管领导处罚意见：
	private String mainLeaderPenalOption;
	private boolean mainLeaderPenalApproval;
	// 处罚处理开始和完成时间
	private Date casePenalStartDate;
	private Date casePenalEndDate;
	
	// 
	//++
	// 承办人结案意见
	private String assigneeCloseCaseOption;
	//+++
	// 承办单位结案意见：
	private String institutionCloseCaseOption;
	private boolean institutionCloseCaseApproval;
	//++++
	// 案件管理中心结案意见：
	private String caseMgtCenterCloseCaseOption;
	private boolean caseMgtCenterCloseCaseApproval;
	//+++++
	// 主管领导结案意见：
	private String mainLeaderCloseCaseOption;
	private boolean mainLeaderCloseCaseApproval;	
	// 结案开始和完成时间
	private Date caseCloseUpStartDate;
	private Date caseCloseUpEndDate;
	
	//
	// 与Case实例相关的流程定义，
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
	
	/*
	 * 查询时使用的变量列表
	 */
	// 涉案人
	private String caseQueryParty;	 
	// 法人
	private String caseQueryLegalAgent;	
	//地址
	private String caseQueryAddress;	 
	//电话
	private String caseQueryPhoneNumber;	 
	// 经办人
	private String caseQueryAssignee;	
	// 违反的法条 normCaseDescPart2
	private String caseQueryBrokeLaw; 
	// 处罚类型 normAssigneePenalOptPart2
	private String caseQueryPenal; 	
	
	// 立案开始时间
	private Date caseQueryRegStartDateStart;  
	private Date caseQueryRegStartDateEnd;  
	// 立案完成时间
	private Date caseQueryRegEndDateStart;  
	private Date caseQueryRegEndDateEnd;  
	// 结案时间
	private Date caseQueryCloseDateStart;  
	private Date caseQueryCloseDateEnd;

	
	public OaCase() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OaCase(String id) {
		super(id);
		// TODO Auto-generated constructor stub
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
	public boolean isInstitutionRegApproval() {
		return institutionRegApproval;
	}
	public void setInstitutionRegApproval(boolean institutionRegApproval) {
		this.institutionRegApproval = institutionRegApproval;
	}
	public String getDeptLeaderRegOption() {
		return deptLeaderRegOption;
	}
	public void setDeptLeaderRegOption(String deptLeaderRegOption) {
		this.deptLeaderRegOption = deptLeaderRegOption;
	}
	public boolean isDeptLeaderRegApproval() {
		return deptLeaderRegApproval;
	}
	public void setDeptLeaderRegApproval(boolean deptLeaderRegApproval) {
		this.deptLeaderRegApproval = deptLeaderRegApproval;
	}
	public String getMainLeaderRegOption() {
		return mainLeaderRegOption;
	}
	public void setMainLeaderRegOption(String mainLeaderRegOption) {
		this.mainLeaderRegOption = mainLeaderRegOption;
	}
	public boolean isMainLeaderRegApproval() {
		return mainLeaderRegApproval;
	}
	public void setMainLeaderRegApproval(boolean mainLeaderRegApproval) {
		this.mainLeaderRegApproval = mainLeaderRegApproval;
	}
	public boolean isbCaseSurveyFinish() {
		return bCaseSurveyFinish;
	}
	public void setbCaseSurveyFinish(boolean bCaseSurveyFinish) {
		this.bCaseSurveyFinish = bCaseSurveyFinish;
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
	public boolean getInstitutionPenalApproval() {
		return institutionPenalApproval;
	}
	public void setInstitutionPenalApproval(boolean institutionPenalApproval) {
		this.institutionPenalApproval = institutionPenalApproval;
	}
	
	public String getCaseMgtCenterPenalOption() {
		return caseMgtCenterPenalOption;
	}
	public void setCaseMgtCenterPenalOption(String caseMgtCenterPenalOption) {
		this.caseMgtCenterPenalOption = caseMgtCenterPenalOption;
	}
	public boolean getCaseMgtCenterPenalApproval() {
		return caseMgtCenterPenalApproval;
	}
	public void setCaseMgtCenterPenalApproval(boolean caseMgtCenterPenalApproval) {
		this.caseMgtCenterPenalApproval = caseMgtCenterPenalApproval;
	}
	
	public String getDeptLeaderPenalOption() {
		return deptLeaderPenalOption;
	}
	public void setDeptLeaderPenalOption(String deptLeaderPenalOption) {
		this.deptLeaderPenalOption = deptLeaderPenalOption;
	}
	public boolean getDeptLeaderPenalApproval() {
		return deptLeaderPenalApproval;
	}
	public void setDeptLeaderPenalApproval(boolean deptLeaderPenalApproval) {
		this.deptLeaderPenalApproval = deptLeaderPenalApproval;
	}
	public String getMainLeaderPenalOption() {
		return mainLeaderPenalOption;
	}
	public void setMainLeaderPenalOption(String mainLeaderPenalOption) {
		this.mainLeaderPenalOption = mainLeaderPenalOption;
	}
	public boolean getMainLeaderPenalApproval() {
		return mainLeaderPenalApproval;
	}
	public void setMainLeaderPenalApproval(boolean mainLeaderPenalApproval) {
		this.mainLeaderPenalApproval = mainLeaderPenalApproval;
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
	public boolean getInstitutionCloseCaseApproval() {
		return institutionCloseCaseApproval;
	}
	public void setInstitutionCloseCaseApproval(boolean institutionCloseCaseApproval) {
		this.institutionCloseCaseApproval = institutionCloseCaseApproval;
	}
	public String getCaseMgtCenterCloseCaseOption() {
		return caseMgtCenterCloseCaseOption;
	}
	public void setCaseMgtCenterCloseCaseOption(String caseMgtCenterCloseCaseOption) {
		this.caseMgtCenterCloseCaseOption = caseMgtCenterCloseCaseOption;
	}
	public boolean getCaseMgtCenterCloseCaseApproval() {
		return caseMgtCenterCloseCaseApproval;
	}
	public void setCaseMgtCenterCloseCaseApproval(boolean caseMgtCenterCloseCaseApproval) {
		this.caseMgtCenterCloseCaseApproval = caseMgtCenterCloseCaseApproval;
	}
	public String getMainLeaderCloseCaseOption() {
		return mainLeaderCloseCaseOption;
	}
	public void setMainLeaderCloseCaseOption(String mainLeaderCloseCaseOption) {
		this.mainLeaderCloseCaseOption = mainLeaderCloseCaseOption;
	}
	public boolean getMainLeaderCloseCaseApproval() {
		return mainLeaderCloseCaseApproval;
	}
	public void setMainLeaderCloseCaseApproval(boolean mainLeaderCloseCaseApproval) {
		this.mainLeaderCloseCaseApproval = mainLeaderCloseCaseApproval;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseRegStartDate() {
		return caseRegStartDate;
	}
	public void setCaseRegStartDate(Date caseRegStartDate) {
		this.caseRegStartDate = caseRegStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseRegEndDate() {
		return caseRegEndDate;
	}
	public void setCaseRegEndDate(Date caseRegEndDate) {
		this.caseRegEndDate = caseRegEndDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseSurveyEndDate() {
		return caseSurveyEndDate;
	}
	public void setCaseSurveyEndDate(Date caseSurveyEndDate) {
		this.caseSurveyEndDate = caseSurveyEndDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCasePenalStartDate() {
		return casePenalStartDate;
	}
	public void setCasePenalStartDate(Date casePenalStartDate) {
		this.casePenalStartDate = casePenalStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCasePenalEndDate() {
		return casePenalEndDate;
	}
	public void setCasePenalEndDate(Date casePenalEndDate) {
		this.casePenalEndDate = casePenalEndDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseCloseUpStartDate() {
		return caseCloseUpStartDate;
	}
	public void setCaseCloseUpStartDate(Date caseCloseUpStartDate) {
		this.caseCloseUpStartDate = caseCloseUpStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseCloseUpEndDate() {
		return caseCloseUpEndDate;
	}
	public void setCaseCloseUpEndDate(Date caseCloseUpEndDate) {
		this.caseCloseUpEndDate = caseCloseUpEndDate;
	}
	
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
	public String getProcessInstanceId() {
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
	public String getCaseQueryAssignee() {
		return caseQueryAssignee;
	}
	public void setCaseQueryAssignee(String caseQueryAssignee) {
		this.caseQueryAssignee = caseQueryAssignee;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")	
	public Date getCaseQueryRegStartDateStart() {
		return caseQueryRegStartDateStart;
	}
	public void setCaseQueryRegStartDateStart(Date caseQueryRegStartDateStart) {
		this.caseQueryRegStartDateStart = caseQueryRegStartDateStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseQueryRegStartDateEnd() {
		return caseQueryRegStartDateEnd;
	}
	public void setCaseQueryRegStartDateEnd(Date caseQueryRegStartDateEnd) {
		this.caseQueryRegStartDateEnd = caseQueryRegStartDateEnd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseQueryRegEndDateStart() {
		return caseQueryRegEndDateStart;
	}
	public void setCaseQueryRegEndDateStart(Date caseQueryRegEndDateStart) {
		this.caseQueryRegEndDateStart = caseQueryRegEndDateStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseQueryRegEndDateEnd() {
		return caseQueryRegEndDateEnd;
	}
	public void setCaseQueryRegEndDateEnd(Date caseQueryRegEndDateEnd) {
		this.caseQueryRegEndDateEnd = caseQueryRegEndDateEnd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseQueryCloseDateStart() {
		return caseQueryCloseDateStart;
	}
	public void setCaseQueryCloseDateStart(Date caseQueryCloseDateStart) {
		this.caseQueryCloseDateStart = caseQueryCloseDateStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCaseQueryCloseDateEnd() {
		return caseQueryCloseDateEnd;
	}
	public void setCaseQueryCloseDateEnd(Date caseQueryCloseDateEnd) {
		this.caseQueryCloseDateEnd = caseQueryCloseDateEnd;
	}
}