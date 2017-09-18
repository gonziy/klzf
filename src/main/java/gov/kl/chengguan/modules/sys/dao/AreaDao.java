/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.sys.dao;

import gov.kl.chengguan.common.persistence.TreeDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.sys.entity.Area;

/**
 * 区域DAO接口
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
}
