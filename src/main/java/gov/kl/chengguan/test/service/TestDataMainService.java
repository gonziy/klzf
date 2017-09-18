/**
 * author Gonziy
 */
package gov.kl.chengguan.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.common.utils.StringUtils;
import gov.kl.chengguan.test.entity.TestDataMain;
import gov.kl.chengguan.test.dao.TestDataMainDao;
import gov.kl.chengguan.test.entity.TestDataChild;
import gov.kl.chengguan.test.dao.TestDataChildDao;

/**
 * 主子表生成Service
 */
@Service
@Transactional(readOnly = true)
public class TestDataMainService extends CrudService<TestDataMainDao, TestDataMain> {

	@Autowired
	private TestDataChildDao testDataChildDao;
	
	public TestDataMain get(String id) {
		TestDataMain testDataMain = super.get(id);
		testDataMain.setTestDataChildList(testDataChildDao.findList(new TestDataChild(testDataMain)));
		return testDataMain;
	}
	
	public List<TestDataMain> findList(TestDataMain testDataMain) {
		return super.findList(testDataMain);
	}
	
	public Page<TestDataMain> findPage(Page<TestDataMain> page, TestDataMain testDataMain) {
		return super.findPage(page, testDataMain);
	}
	
	@Transactional(readOnly = false)
	public void save(TestDataMain testDataMain) {
		super.save(testDataMain);
		for (TestDataChild testDataChild : testDataMain.getTestDataChildList()){
			if (testDataChild.getId() == null){
				continue;
			}
			if (TestDataChild.DEL_FLAG_NORMAL.equals(testDataChild.getDelFlag())){
				if (StringUtils.isBlank(testDataChild.getId())){
					testDataChild.setTestDataMain(testDataMain);
					testDataChild.preInsert();
					testDataChildDao.insert(testDataChild);
				}else{
					testDataChild.preUpdate();
					testDataChildDao.update(testDataChild);
				}
			}else{
				testDataChildDao.delete(testDataChild);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TestDataMain testDataMain) {
		super.delete(testDataMain);
		testDataChildDao.delete(new TestDataChild(testDataMain));
	}
	
}