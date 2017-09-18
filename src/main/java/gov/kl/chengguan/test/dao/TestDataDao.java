/**
 * author Gonziy
 */
package gov.kl.chengguan.test.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.test.entity.TestData;

/**
 * 单表生成DAO接口
 */
@MyBatisDao
public interface TestDataDao extends CrudDao<TestData> {
	
}