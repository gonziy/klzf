/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.modules.test.entity.Test;
import gov.kl.chengguan.modules.test.dao.TestDao;

/**
 * 测试Service
 */
@Service
@Transactional(readOnly = true)
public class TestService extends CrudService<TestDao, Test> {

}
