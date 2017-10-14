package gov.kl.chengguan.modules.apiv1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.cms.dao.BaseArticleDao;
import gov.kl.chengguan.modules.cms.entity.BaseArticle;
import gov.kl.chengguan.modules.cms.entity.Category;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;


@RestController
@RequestMapping(value = "/apiv1")
public class ApiCmsController  extends BaseController {
	
	@Autowired
	private BaseArticleDao baseArticleDao;
	
	
	@RequestMapping(value = {"test"})
	public void test(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		
		java.util.List<String> users = new ArrayList<String>();
		users.add("赵");
		users.add("钱");
		users.add("孙");
		users.add("李");
		
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();

		String method = request.getMethod();
		String msg = request.getParameter("msg");
		
		try {
			jsonObject.put("msg", "method is " + method + ", msg is " +msg);
			jsonObject.put("code", 0);
			jsonObject.put("data", users);
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
		} catch (Exception e) {

		}

	}
	/**
	 * 获取文章列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"cms/article/list"})
	public void getArticleList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String categoryid = request.getParameter("categoryid");
		if(categoryid==null || categoryid.isEmpty())
		{
			jsonObject.put("msg", "missing url, categoryid is null");
			jsonObject.put("code", 41010);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			BaseArticle whereArticle = new BaseArticle();
			Category whereCategory = new Category();
			whereCategory.setId(categoryid);
			whereArticle.setCategory(whereCategory);
						
			
			java.util.List<BaseArticle> list = baseArticleDao.findList(whereArticle) ;
			if(list!=null)
			{
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				ArrayList<ApiArticle> articles = new ArrayList<ApiArticle>();
				for (BaseArticle article : list) {
					ApiArticle apiArticle = new ApiArticle();
					apiArticle.setId(article.getId());
					apiArticle.setTitle(article.getTitle());
					apiArticle.setCategoryId(article.getCategory().getId());
					apiArticle.setDescription(article.getDescription());
					
					articles.add(apiArticle);
				}

				jsonObject.put("data", articles);
			}
			else
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {
			jsonObject.put("msg", "system error");
			jsonObject.put("code", -1);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
		}

	}
		
	
	public class ApiArticle {
		private String id;
		private String categoryId;
		private String title;
		private String description;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	}

}