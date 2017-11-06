package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaDoc3;

@MyBatisDao
public interface OaDoc3Dao extends CrudDao<OaDoc3>{
	/**
	 * 更新流程实例ID
	 * @param OaDoc
	 * @return
	 */

	public int updateOfficeHeaderApproval(OaDoc3 doc);
	public int updateLeaderApproval(OaDoc3 doc);
	public int updateOfficeHeaderDispatch(OaDoc3 doc);
	public int updateDrStage(OaDoc3 doc);	
	
	public int updateProcessInstanceId(OaDoc3 doc);
	//
	public OaDoc3 getByProcInsId(String procInsId);
	
	public int updateInsId(OaDoc3 doc);
	
}
