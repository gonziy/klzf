package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaDoc;
import gov.kl.chengguan.modules.oa.entity.TestAudit;

@MyBatisDao
public interface OaDocDao extends CrudDao<OaDoc>{
	/**
	 * 更新流程实例ID
	 * @param OaDoc
	 * @return
	 */
	public int updateProcessInstanceId(OaDoc doc);
	//
	public OaDoc getByProcInsId(String procInsId);
	
	public int updateInsId(OaDoc doc);
	
	
}
