package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.entity.OaFiles;

@MyBatisDao
public interface OaFilesDao extends CrudDao<OaFiles>{
	
}