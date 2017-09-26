/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import gov.kl.chengguan.modules.sys.entity.User;
import com.google.common.collect.Lists;
import gov.kl.chengguan.common.persistence.DataEntity;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;

/**
 * 文章Entity
 */
public class BaseArticle extends DataEntity<BaseArticle> {

	
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;	// 标题
	private String description;// 描述、摘要
	
	public BaseArticle() {
		super();
	}

	public BaseArticle(String id){
		this();
		this.id = id;
	}
	
	public BaseArticle(Category category){
		this();
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min=0, max=255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}


