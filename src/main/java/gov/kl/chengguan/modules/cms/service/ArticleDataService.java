/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.modules.cms.dao.ArticleDataDao;
import gov.kl.chengguan.modules.cms.entity.ArticleData;

/**
 * 站点Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

}
