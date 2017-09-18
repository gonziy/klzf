/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.test.dao;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.test.entity.Test;

/**
 * 测试DAO接口
 */
@MyBatisDao
public interface TestDao extends CrudDao<Test> {
	
}
