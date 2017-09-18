/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.sys.dao;

import gov.kl.chengguan.common.persistence.TreeDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.sys.entity.Office;

/**
 * 机构DAO接口
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
