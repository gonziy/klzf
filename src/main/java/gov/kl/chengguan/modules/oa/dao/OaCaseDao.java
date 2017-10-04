package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;

@MyBatisDao
public interface OaCaseDao extends CrudDao<OaCase>{
	/**
	 * 更新流程实例ID
	 * @param OaDoc
	 * @return
	 */
	public int updateProcessInstanceId(OaCase oaCase);
	
}