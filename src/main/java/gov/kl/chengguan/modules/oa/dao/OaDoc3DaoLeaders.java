package gov.kl.chengguan.modules.oa.dao;

import java.util.List;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaDoc3Leaders;

@MyBatisDao
public interface OaDoc3DaoLeaders extends CrudDao<OaDoc3Leaders>{

	public List<OaDoc3Leaders> getLeadersOpinions(String procInsId);
	public List<OaDoc3Leaders> getLeadersOpinionsHistory(String procInsId);
	

	
	
	
}
