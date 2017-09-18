/**
 * author Gonziy
 */
package gov.kl.chengguan.test.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.test.entity.TestDataChild;

/**
 * 主子表生成DAO接口
 */
@MyBatisDao
public interface TestDataChildDao extends CrudDao<TestDataChild> {
	
}