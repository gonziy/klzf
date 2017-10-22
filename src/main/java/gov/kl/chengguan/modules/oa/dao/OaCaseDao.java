package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;

@MyBatisDao
public interface OaCaseDao extends CrudDao<OaCase>{
	public OaCase getByProcInsId(String procInsId);
	public int updateInsId(OaCase testAudit);
	
	//
	public int updateCaseCheckResult(OaCase oaCase);
	public int updateCaseRecord(OaCase oaCase);
	public int updateCaseAttachmentLinks(OaCase oaCase);
	public int updateInstitutionRegOption(OaCase oaCase);
	public int updateDeptLeaderRegOption(OaCase oaCase);
	public int updateMainLeaderRegOption(OaCase oaCase);
	public int updateMainLeaderRegOption1(OaCase oaCase);
	
	public int updateCaseSurveyEndData(OaCase oaCase);	
	
	
	public int updateCaseDesc(OaCase oaCase);
	public int updateCasePenal(OaCase oaCase);
	// 非拒绝的任务执行此更新语句
	public int updateAssigneePenalOption1(OaCase oaCase);
	// 拒绝返回的语句
	public int updateAssigneePenalOption(OaCase oaCase);	
	public int updateInstitutionPenalOption(OaCase oaCase);
	public int updateMgtCenterPenalOption(OaCase oaCase);	
	public int updateDeptLeaderPenalOption(OaCase oaCase);
	public int updateMainLeaderPenalOption(OaCase oaCase);
	// 主管领导审批通过的更新语句
	public int updateMainLeaderPenalOption1(OaCase oaCase);
	
	// 非拒绝的任务执行此更新语句
	public int updateAssigneeCloseOption1(OaCase oaCase);
	// 拒绝返回的语句
	public int updateAssigneeCloseOption(OaCase oaCase);
	public int updateInstitutionCloseOption(OaCase oaCase);
	public int updateMgtCenterCloseOption(OaCase oaCase);
	public int updateMainLeaderCloseOption(OaCase oaCase);
	public int updateMainLeaderCloseOption1(OaCase oaCase);
}