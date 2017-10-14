package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;

@MyBatisDao
public interface OaCaseDao extends CrudDao<OaCase>{
	public OaCase getByProcInsId(String procInsId);
	public int updateInsId(OaCase testAudit);
	
	public int updateCaseCheckResult(OaCase oaCase);
	public int updateCaseRecord(OaCase oaCase);
	public int updateInstitutionRegOption(OaCase oaCase);
	public int updateDeptLeaderRegOption(OaCase oaCase);
	public int updateMainLeaderRegOption(OaCase oaCase);
}