package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaDoc;

@MyBatisDao
public interface OaDocDao extends CrudDao<OaDoc>{
	/**
	 * 更新流程实例ID
	 * @param OaDoc
	 * @return
	 */
	public int updateProcessInstanceId(OaDoc doc);
	
}
