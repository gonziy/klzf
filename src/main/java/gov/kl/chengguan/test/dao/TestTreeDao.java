/**
 * author Gonziy
 */
package gov.kl.chengguan.test.dao;

import gov.kl.chengguan.common.persistence.TreeDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.test.entity.TestTree;

/**
 * 树结构生成DAO接口
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}