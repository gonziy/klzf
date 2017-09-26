/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.cms.dao;

import java.util.List;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.cms.entity.Article;
import gov.kl.chengguan.modules.cms.entity.BaseArticle;
import gov.kl.chengguan.modules.cms.entity.Category;

/**
 * 文章DAO接口
 */
@MyBatisDao
public interface BaseArticleDao extends CrudDao<BaseArticle> {
	
	public List<BaseArticle> findByIdIn(String[] ids);
	
	
}
