package gov.kl.chengguan.modules.oa.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.oa.entity.Leave;
import gov.kl.chengguan.modules.oa.entity.OaCaseFields;

/**
 * 获取案件选中的关键字DAO接口
 */
@MyBatisDao
public interface OaCaseFieldsDao extends CrudDao<OaCaseFields> {
	
}
