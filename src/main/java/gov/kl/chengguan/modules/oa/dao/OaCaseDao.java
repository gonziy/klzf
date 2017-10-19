package gov.kl.chengguan.modules.oa.dao;

import java.util.Date;

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
	public int updateAssigneePenalOption(OaCase oaCase);
	public int updateInstitutionPenalOption(OaCase oaCase);
	public int updateMgtCenterPenalOption(OaCase oaCase);	
	public int updateDeptLeaderPenalOption(OaCase oaCase);
	public int updateMainLeaderPenalOption(OaCase oaCase);
	public int updateMainLeaderPenalOption1(OaCase oaCase);
	
	public int updateAssigneeCloseOption(OaCase oaCase);
	public int updateInstitutionCloseOption(OaCase oaCase);
	public int updateMgtCenterCloseOption(OaCase oaCase);
	public int updateMainLeaderCloseOption(OaCase oaCase);
	public int updateMainLeaderCloseOption1(OaCase oaCase);
}