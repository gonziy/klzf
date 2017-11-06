package gov.kl.chengguan.modules.apiv1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.cms.dao.ArticleDao;
import gov.kl.chengguan.modules.cms.dao.BaseArticleDao;
import gov.kl.chengguan.modules.cms.entity.Article;
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
	@Autowired
	private ArticleDao articleDao;
	
	
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
	
	@RequestMapping(value = {"app/check/update"})
	public void getVersionList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String version = request.getParameter("versionCode");
		if(version==null || version.isEmpty())
		{
			jsonObject.put("msg", "missing url, versionCode is null");
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
			/* ##############################↓↓↓↓↓↓↓↓↓↓↓↓↓↓读取字段看这一段↓↓↓↓↓↓↓↓↓↓########################### */
			/*
			 * categoryid 的值
			 * ab46c1295da648a18a0291b83ed0e30b
			 */
			BaseArticle whereArticle = new BaseArticle();
			Category whereCategory = new Category();
			whereCategory.setId("ab46c1295da648a18a0291b83ed0e30b");
			whereArticle.setCategory(whereCategory);
			BaseArticle lastversion = baseArticleDao.findLast(whereArticle) ;
			/* ##############################↑↑↑↑↑↑↑↑↑↑↑↑↑↑读取字段看这一段↑↑↑↑↑↑↑↑↑########################### */
			if(lastversion!=null)
			{
				com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				if(Integer.parseInt(version)<lastversion.getWeight()){
					jsonData.put("versionCode", lastversion.getWeight());
					jsonData.put("versionName", lastversion.getTitle());
					jsonData.put("url", "http://47.93.52.62:8080/klzf2/app"+lastversion.getWeight()+".apk");
					jsonData.put("description", lastversion.getDescription());
				}else{
					jsonData.put("versionCode", 0);
				}
				
				jsonObject.put("data", jsonData);
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
	
	
	/*
	 * 查找对应关系文章 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"cms/article/similar"})
	public void getLikeArticleList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String title = request.getParameter("title");
		if(title==null || title.isEmpty())
		{
			jsonObject.put("msg", "missing url, title is null");
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
			Article articleWhere = new Article();
			articleWhere.setTitle(title);
			java.util.List<Article> list = articleDao.getLikeList(articleWhere);
			if(list!=null)
			{
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				ArrayList<ApiArticle> articles = new ArrayList<ApiArticle>();
				for (Article article : list) {
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
			/* ##############################↓↓↓↓↓↓↓↓↓↓↓↓↓↓读取字段看这一段↓↓↓↓↓↓↓↓↓↓########################### */
			/*
			 * categoryid 的值
			 * 案件来源：0cbf63e927174b94bcbe19b6d541a19e
			 * 行为：3
			 * 处罚办法：4
			 * 法律条文：5
			 */
			BaseArticle whereArticle = new BaseArticle();
			Category whereCategory = new Category();
			whereCategory.setId(categoryid);
			whereArticle.setCategory(whereCategory);
			java.util.List<BaseArticle> list = baseArticleDao.findList(whereArticle) ;
			/* ##############################↑↑↑↑↑↑↑↑↑↑↑↑↑↑读取字段看这一段↑↑↑↑↑↑↑↑↑########################### */
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