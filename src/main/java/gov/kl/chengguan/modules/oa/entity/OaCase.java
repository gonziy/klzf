package gov.kl.chengguan.modules.oa.entity;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;

import gov.kl.chengguan.common.persistence.ActEntity;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.modules.sys.dao.OfficeDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;

/*
 * 公文流转实体类
 */
public class OaCase extends ActEntity<OaCase> {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	
	/**
	 * 版本id
	 */
	private static final long serialVersionUID = 1L;		

	/**
	 *  案件手动增长编号
	 */
	private Integer autoId; //17-1105 案件编号（规则 :年份+05(垦利区号)+中队id+案件总体编号（三位数字）     例如：20170501001）
	public Integer getAutoId() {
		return autoId;
	}

	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}
	/*
	 * 需要对案件经办各个环节，时间进行记录
	 * 每个流程的办理人是否需要临时修改，人员变动会引起问题
	 */
	private String title; //17-1015案件名
	// 案件当事人
	private String caseParties; 
	// 法定代理人
	private String caseLegalAgent;
	// 地址
	private String address;


	// 电话
	private String phoneNumber;
	// 描述
	private String caseDescription;
	// 案件的链接，可能一次发布多个文件，用;分割
	private String caseDocuments;	
	private String caseImages;	
	private String caseVideos;	
	
	private String caseThumbnails;
	// 初步审核意见
	private String caseCheckResult;
	private Boolean caseCheckFlag;
	// 案件来源，如果能列出应该时id值，
	private String caseSource;
	// 案件承办人,可以多个，用;隔开
	private String assigneeIds;
	private String assigneeNames;
	
	private String assigneeNameAndId1;
	private String assigneeNameAndId2;
	private String assigneeNamesAndId;

	private String officeAddress;	
	private String officeTel;
	private String officeName;
	public String getOfficeName() {
		User user = userDao.get(createBy.getId());
		Office office = officeDao.get(user.getOffice().getId());
		Office parentOffice = officeDao.get(office.getParentId());
		return parentOffice.getName();
	}

	public String getOfficeAddress() {
		User user = userDao.get(createBy.getId());
		Office office = officeDao.get(user.getOffice().getId());
		return office.getAddress();
	}

	public String getOfficeTel() {
		User user = userDao.get(createBy.getId());
		Office office = officeDao.get(user.getOffice().getId());
		return office.getPhone();
	}
	

	public String getAssigneeNames() {
		String strAssigneeNames = "";
		if(getAssigneeIds()!=null && !getAssigneeIds().isEmpty()){
			String[] assigneeIdsList = getAssigneeIds().split(";");
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
		return strAssigneeNames;
	}
	public String getAssigneeNameAndId1() {
		String[] us = getAssigneeNamesAndId().split("<br/>");
		return us[0];
	}

	public String getAssigneeNameAndId2() {
		String[] us = getAssigneeNamesAndId().split("<br/>");
		return us[1];
	}
	public String getAssigneeNamesAndId() {
		String strAssigneeNamesAndIds = "";
		if(getAssigneeIds()!=null && !getAssigneeIds().isEmpty()){
			String[] assigneeIdsList = getAssigneeIds().split(";");
			if(assigneeIdsList.length>0){
				for (String id : assigneeIdsList) {
					User user = userDao.get(id);
					if(user!=null){
						strAssigneeNamesAndIds += user.getName() +"(执法证号:"+user.getNo() +")" + "<br/>";
					}
				}
				if(strAssigneeNamesAndIds.endsWith("<br/>")){
					strAssigneeNamesAndIds = strAssigneeNamesAndIds.substring(0,strAssigneeNamesAndIds.length()-5);
				}
			}
		}
		return strAssigneeNamesAndIds;
	}
	// 案件的规范描述：[某行为] 违反了 [某条例]
	private String normCaseDescPart1;
	private String normCaseDescPart2;	

	private Boolean illegalConstructionFlag; //17-1015违建标识
	

	// 承办机构意见
	private String institutionRegOption;
	// 承办机构确认
	private Boolean institutionRegApproval;	
	// ----
	// 分管领导意见
	private String deptLeaderRegOption;
	// 分管领导确认
	private Boolean deptLeaderRegApproval;	
	// -----
	// 主管领导意见
	private String mainLeaderRegOption;
	// 主管领导确认
	private Boolean mainLeaderRegApproval;
	// 立案开始和完成时间
	private Date caseRegStartDate;
	private Date caseRegEndDate;
	// ------
	// 案件勘察完成日期
	private Date caseSurveyEndDate;
	
	//==
	// 承办人意见：[依据法条]，给出处罚，[处罚类型]
	private String normAssigneePenalOptPart1;
	private String normAssigneePenalOptPart2;

	private String assigneePenalOption;
	private String caseDocNo; //17-1015案件文号
	//===
	// 承办单位处罚意见：
	private String institutionPenalOption;
	private Boolean institutionPenalApproval;
	//====
	// 案件管理中心处罚意见：
	private String caseMgtCenterPenalOption;
	private Boolean caseMgtCenterPenalApproval;
	//=====
	// 分管领导处罚意见：
	private String deptLeaderPenalOption;
	private Boolean deptLeaderPenalApproval;
	//======
	// 主管领导处罚意见：
	private String mainLeaderPenalOption;
	private Boolean mainLeaderPenalApproval;
	// 处罚处理开始和完成时间
	private Date casePenalStartDate;
	private Date casePenalEndDate;

	//++
	// 承办人结案意见
	private String assigneeCloseCaseOption;
	//+++
	// 承办单位结案意见：
	private String institutionCloseCaseOption;
	private Boolean institutionCloseCaseApproval;
	//++++
	// 案件管理中心结案意见：
	private String caseMgtCenterCloseCaseOption;
	private Boolean caseMgtCenterCloseCaseApproval;
	//+++++
	// 主管领导结案意见：
	private String mainLeaderCloseCaseOption;
	private Boolean mainLeaderCloseCaseApproval;	
	// 结案开始和完成时间
	private Date caseCloseUpStartDate;
	private Date caseCloseUpEndDate;
	private String normCaseDesc;
	private String normAssigneePenalOpt;	
	
	
	/*
	 * 案件办理阶段
	 * 0：申报
	 * 1：立案
	 * 2：调查
	 * 3：处罚
	 * 4：结案
	 * 5: 案件完结：因为案件结束可能在stage1和stage4之后，为了区分这两种情况，增加此状态
	 */
	private Integer caseStage;
	
	/*
	 * 驳回标志
	 * 注意：使用数据库deptLeaderCloseCaseApproval作为存放字段
	 */
	private Boolean rejectFlag;
	
	/* 
	 * 1030添加字段
	 */
	
	//立案承办人
	private String LaRecAssignee;
	//立案承办机构
	private String institutionRegAssignee;
	//立案分管领导
	private String deptLeaderRegAssignee;
	//立案主要领导
	private String mainLeaderRegAssignee;
	//处罚 承办人
	private String penalAssignee;
	//处罚承办机构
	private String institutionPenalAssignee;
	//处罚案管中心
	private String caseMgtCenterPenalAssignee;
	//处罚分管领导
	private String deptLeaderPenalAssignee;
	//处罚主要领导
	private String mainLeaderPenalAssignee;
	//结案承办人
	private String closeUpAssignee;
	//结案承办机构
	private String institutionCloseAssignee;
	//结案案管中心
	private String caseMgtCenterCloseAssignee;
	//结案主要领导
	private String mainLeaderCloseAssignee;

	//立案 承办人 时间
	private Date LaRecDate;
	//立案承办机构 时间
	private Date institutionRegDate;
	//立案 分管领导 时间
	private Date deptLeaderRegDate;
	//处罚承办机构时间
	private Date institutionPenalDate;
	//处罚案管中心时间
	private Date caseMgtCenterPenalDate;
	//处罚分管领导时间
	private Date deptLeaderPenalDate;
	//结案 承办机构时间
	private Date institutionCloseDate;
	//结案 案管中心 时间
	private Date caseMgtCenterCloseDate;
	/*
	 * 
	 */
	//催告时间
	private Date gaozhiDate;
	//处罚决定时间
	private Date chufaDate;
	//催告时间
	private Date cuigaoDate;
	//到期时间(实时计算)
	private Integer queryExpire;
	
	public Date getGaozhiDate() {
		return gaozhiDate;
	}

	public void setGaozhiDate(Date gaozhiDate) {
		this.gaozhiDate = gaozhiDate;
	}

	public Date getChufaDate() {
		return chufaDate;
	}

	public void setChufaDate(Date chufaDate) {
		this.chufaDate = chufaDate;
	}

	public Date getCuigaoDate() {
		return cuigaoDate;
	}

	public void setCuigaoDate(Date cuigaoDate) {
		this.cuigaoDate = cuigaoDate;
	}

	public Integer getQueryExpire() {
		return queryExpire;
	}

	public void setQueryExpire(Integer queryExpire) {
		this.queryExpire = queryExpire;
	}
	// 与Case实例相关的流程定义，
	// 所在的流程实例、所在任务，其中的流程变量，
	// 流程历史实例
	private ProcessDefinition processDefinition;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	//使用该流程实例ID作为外键记录流程中上传的照片、文档、视频等资源
	private String processInstanceId; // 流程实例编号
	//
	private Task task;
	private Map<String, Object> variables;
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;

	/*
	 * 查询时使用的变量列表
	 */

	private String caseQueryUserId;	 
	public String getCaseQueryUserId() {
		return caseQueryUserId;
	}

	public void setCaseQueryUserId(String caseQueryUserId) {
		this.caseQueryUserId = caseQueryUserId;
	}
	// 涉案人
	private String caseQueryParty;	 
	// 法人
	private String caseQueryLegalAgent;	
	//地址
	private String caseQueryAddress;	 
	//电话
	private String caseQueryPhoneNumber;	 
	// 经办人
	private User caseQueryAssignee;	
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
	// 案件办理进度
	private Integer caseQueryStage;
	private String caseQueryTitle;	 
	private String caseQueryDocNo;	 
	private Boolean caseQueryIllegalConstruct;
	private Boolean caseQueryCheckFlag;
	
	/*
	 * 用于标识案件的名称
	 */
	public String  getCaseTitle() {
		return caseParties;			
	}	
	
	public String getNormCaseDesc() {
		if(normCaseDescPart1 != null && normCaseDescPart2 != null)
		{
			StringBuilder sb = new StringBuilder(normCaseDescPart1);
			sb.append(" 违反了： ").append(normCaseDescPart2).append(" 之规定");
			normCaseDesc = sb.toString();
			return normCaseDesc;
		}
		return  "";
	}
	
	public String getNormAssigneePenalOpt() {
		if(normCaseDescPart2 !=null && normAssigneePenalOptPart2!=null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" 建议依据： ").append(normCaseDescPart2).append(" 给予处罚: ")
				.append(normAssigneePenalOptPart2);
			normAssigneePenalOpt = sb.toString();
			return normAssigneePenalOpt;
		}
		return "";
	}
	
	
	public OaCase() {
		super();
		caseStage = 0;
		rejectFlag = false;
		// TODO Auto-generated constructor stub
	}
	public OaCase(String id) {
		super(id);
		
		if(caseParties!= null && caseParties.length() >0 )
		{
			String[] ss = caseParties.split(";");
			caseLegalAgent = ss[0];
		}
		caseStage = 0;
		rejectFlag = false;
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

	public Boolean getCaseCheckFlag() {
		return caseCheckFlag;
	}

	public void setCaseCheckFlag(Boolean caseCheckFlag) {
		this.caseCheckFlag = caseCheckFlag;
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
	public void setCaseMgtCenterCloseCaseApproval(Boolean caseMgtCenterCloseCaseApproval) {
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
	public User getCaseQueryAssignee() {
		return caseQueryAssignee;
	}
	public void setCaseQueryAssignee(User caseQueryAssignee) {
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
	
	//
	public Integer getCaseStage() {
		return caseStage;
	}
	public void setCaseStage(Integer caseStage) {
		this.caseStage = caseStage;
	}
	
	public Integer getCaseQueryStage() {
		return caseQueryStage;
	}
	public void setCaseQueryStage(Integer caseQueryStage) {
		this.caseQueryStage = caseQueryStage;
	}
	
	public void setNormCaseDesc(String normCaseDesc) {
		this.normCaseDesc = normCaseDesc;
	}
	public void setNormAssigneePenalOpt(String normAssigneePenalOpt) {
		this.normAssigneePenalOpt = normAssigneePenalOpt;
	}
	
	//	20171015
	public Boolean getIllegalConstructionFlag() {
		return illegalConstructionFlag;
	}
	public void setIllegalConstructionFlag(Boolean illegalConstructionFlag) {
		this.illegalConstructionFlag = illegalConstructionFlag;
	}
	public String getCaseDocNo() {
		return caseDocNo;
	}
	public void setCaseDocNo(String caseDocNo) {
		this.caseDocNo = caseDocNo;
	}
	
	/*
	 * 用于标识案件的名称
	 */

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	public String getCaseQueryTitle() {
		return caseQueryTitle;
	}
	public void setCaseQueryTitle(String caseQueryTitle) {
		this.caseQueryTitle = caseQueryTitle;
	}
	public String getCaseQueryDocNo() {
		return caseQueryDocNo;
	}
	public void setCaseQueryDocNo(String caseQueryDocNo) {
		this.caseQueryDocNo = caseQueryDocNo;
	}

	public Boolean getCaseQueryIllegalConstruct() {
		return caseQueryIllegalConstruct;
	}

	public void setCaseQueryIllegalConstruct(Boolean caseQueryIllegalConstruct) {
		this.caseQueryIllegalConstruct = caseQueryIllegalConstruct;
	}

	public Boolean getCaseQueryCheckFlag() {
		return caseQueryCheckFlag;
	}
	
	public void setCaseQueryCheckFlag(Boolean caseQueryCheckFlag) {
		this.caseQueryCheckFlag = caseQueryCheckFlag;
	}

	// 增加驳回标志
	public Boolean getRejectFlag() {
		return rejectFlag;
	}

	public void setRejectFlag(Boolean rejectFlag) {
		this.rejectFlag = rejectFlag;
	}	
	

	/*
	 * 1030添加字段
	 */
	
	public String getLaRecAssignee() {
		return LaRecAssignee;
	}

	public void setLaRecAssignee(String laRecAssignee) {
		LaRecAssignee = laRecAssignee;
	}

	public String getInstitutionRegAssignee() {
		return institutionRegAssignee;
	}

	public void setInstitutionRegAssignee(String institutionRegAssignee) {
		this.institutionRegAssignee = institutionRegAssignee;
	}

	public String getDeptLeaderRegAssignee() {
		return deptLeaderRegAssignee;
	}

	public void setDeptLeaderRegAssignee(String deptLeaderRegAssignee) {
		this.deptLeaderRegAssignee = deptLeaderRegAssignee;
	}

	public String getMainLeaderRegAssignee() {
		return mainLeaderRegAssignee;
	}

	public void setMainLeaderRegAssignee(String mainLeaderRegAssignee) {
		this.mainLeaderRegAssignee = mainLeaderRegAssignee;
	}

	public String getPenalAssignee() {
		return penalAssignee;
	}

	public void setPenalAssignee(String penalAssignee) {
		this.penalAssignee = penalAssignee;
	}

	public String getInstitutionPenalAssignee() {
		return institutionPenalAssignee;
	}

	public void setInstitutionPenalAssignee(String institutionPenalAssignee) {
		this.institutionPenalAssignee = institutionPenalAssignee;
	}

	public String getCaseMgtCenterPenalAssignee() {
		return caseMgtCenterPenalAssignee;
	}

	public void setCaseMgtCenterPenalAssignee(String caseMgtCenterPenalAssignee) {
		this.caseMgtCenterPenalAssignee = caseMgtCenterPenalAssignee;
	}

	public String getDeptLeaderPenalAssignee() {
		return deptLeaderPenalAssignee;
	}

	public void setDeptLeaderPenalAssignee(String deptLeaderPenalAssignee) {
		this.deptLeaderPenalAssignee = deptLeaderPenalAssignee;
	}

	public String getMainLeaderPenalAssignee() {
		return mainLeaderPenalAssignee;
	}

	public void setMainLeaderPenalAssignee(String mainLeaderPenalAssignee) {
		this.mainLeaderPenalAssignee = mainLeaderPenalAssignee;
	}

	public String getCloseUpAssignee() {
		return closeUpAssignee;
	}

	public void setCloseUpAssignee(String closeUpAssignee) {
		this.closeUpAssignee = closeUpAssignee;
	}

	public String getInstitutionCloseAssignee() {
		return institutionCloseAssignee;
	}

	public void setInstitutionCloseAssignee(String institutionCloseAssignee) {
		this.institutionCloseAssignee = institutionCloseAssignee;
	}

	public String getCaseMgtCenterCloseAssignee() {
		return caseMgtCenterCloseAssignee;
	}

	public void setCaseMgtCenterCloseAssignee(String caseMgtCenterCloseAssignee) {
		this.caseMgtCenterCloseAssignee = caseMgtCenterCloseAssignee;
	}

	public String getMainLeaderCloseAssignee() {
		return mainLeaderCloseAssignee;
	}

	public void setMainLeaderCloseAssignee(String mainLeaderCloseAssignee) {
		this.mainLeaderCloseAssignee = mainLeaderCloseAssignee;
	}

	public Date getLaRecDate() {
		return LaRecDate;
	}

	public void setLaRecDate(Date laRecDate) {
		LaRecDate = laRecDate;
	}

	public Date getInstitutionRegDate() {
		return institutionRegDate;
	}

	public void setInstitutionRegDate(Date institutionRegDate) {
		this.institutionRegDate = institutionRegDate;
	}

	public Date getDeptLeaderRegDate() {
		return deptLeaderRegDate;
	}

	public void setDeptLeaderRegDate(Date deptLeaderRegDate) {
		this.deptLeaderRegDate = deptLeaderRegDate;
	}

	public Date getInstitutionPenalDate() {
		return institutionPenalDate;
	}

	public void setInstitutionPenalDate(Date institutionPenalDate) {
		this.institutionPenalDate = institutionPenalDate;
	}

	public Date getCaseMgtCenterPenalDate() {
		return caseMgtCenterPenalDate;
	}

	public void setCaseMgtCenterPenalDate(Date caseMgtCenterPenalDate) {
		this.caseMgtCenterPenalDate = caseMgtCenterPenalDate;
	}

	public Date getDeptLeaderPenalDate() {
		return deptLeaderPenalDate;
	}

	public void setDeptLeaderPenalDate(Date deptLeaderPenalDate) {
		this.deptLeaderPenalDate = deptLeaderPenalDate;
	}

	public Date getInstitutionCloseDate() {
		return institutionCloseDate;
	}

	public void setInstitutionCloseDate(Date institutionCloseDate) {
		this.institutionCloseDate = institutionCloseDate;
	}

	public Date getCaseMgtCenterCloseDate() {
		return caseMgtCenterCloseDate;
	}

	public void setCaseMgtCenterCloseDate(Date caseMgtCenterCloseDate) {
		this.caseMgtCenterCloseDate = caseMgtCenterCloseDate;
	}
	
	@Override
	public String toString() {
		return "OaCase [caseParties=" + caseParties + ", caseLegalAgent=" + caseLegalAgent + ", address=" + address
				+ ", phoneNumber=" + phoneNumber + ", caseDescription=" + caseDescription 
				+ ", caseCheckResult=" + caseCheckResult + ", caseSource=" + caseSource
				+ ", assigneeIds=" + assigneeIds + "]";
	}

}
