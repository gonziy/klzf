package gov.kl.chengguan.modules.apiv1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.h2.util.New;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;



@RestController
@RequestMapping(value = "/apiv1")
public class ApiUserController  extends BaseController {
	
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	
	
	@RequestMapping(value = {"user/info/get"})
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String username = request.getParameter("username");
		String userid = request.getParameter("userid");
		if((username==null || username.isEmpty())&&(userid==null || userid.isEmpty()))
		{
			jsonObject.put("msg", "missing url, username and userid is null, please put a parameter");
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
			BaseUser baseUser = new BaseUser();
			if((username == null || username.isEmpty()) && !userid.isEmpty())
			{
				baseUser = UserUtils.getBaseById(userid);
			}else if(!username.isEmpty() && (userid==null || userid.isEmpty()))
			{
				baseUser = UserUtils.getBaseByLoginName(username);
			}
			
			if(baseUser == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {

				jsonObject.put("msg", "success");
				jsonObject.put("code", 200);

				ApiUser user = new ApiUser();
				user.setId(baseUser.getId());
				user.setUsername(baseUser.getLoginName());
				user.setNo(baseUser.getNo());
				user.setName(baseUser.getName());
				user.setOfficeId(baseUser.getOffice().getId());
				user.setOfficeName(baseUser.getOffice().getName());
				
				jsonObject.put("data",JSONObject.toJSON(user));
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
	
	@RequestMapping(value = {"user/info/list"})
	public void getUserList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		
		String officeId = request.getParameter("office_id");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		try {
			
			BaseUser whereBaseUser = new BaseUser();
			Office whereOffice = new Office();
			whereOffice.setId(officeId);
			whereBaseUser.setOffice(whereOffice);
			java.util.List<BaseUser> list =  UserUtils.getBaseAllList(whereBaseUser);
			if(list == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {

				jsonObject.put("msg", "success");
				jsonObject.put("code", 200);
				
				ArrayList<ApiUser> apiUsers = new ArrayList<ApiUser>();
				for (BaseUser user : list) {
					ApiUser apiUser = new ApiUser();
					apiUser.setId(user.getId());
					apiUser.setUsername(user.getLoginName());
					apiUser.setNo(user.getNo());
					apiUser.setName(user.getName());
					apiUser.setOfficeId(user.getOffice().getId());
					String office_name = user.getOffice().getName();
					apiUser.setOfficeName(user.getOffice().getName());
					apiUsers.add(apiUser);
				}
				
				
				
				jsonObject.put("data",JSONObject.toJSON(apiUsers));
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
	
//	@RequestMapping(value = {"user/info/updatepassword"})
//	public void setUserPassword(HttpServletRequest request, HttpServletResponse response) {
//		response.setContentType("application/json");
//		response.setHeader("Pragma", "No-cache");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setCharacterEncoding("UTF-8");
//		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//		
//		String password = request.getParameter("password");
//		String userid = request.getParameter("userid");
//		
//		if((password==null || password.isEmpty())||(userid==null || userid.isEmpty()))
//		{
//			jsonObject.put("msg", "missing url, password and userid is null");
//			jsonObject.put("code", 41010);
//			PrintWriter out;
//			try {
//				out = response.getWriter();
//				out.print(jsonObject.toJSONString());
//				out.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return;
//		}
//		gov.kl.chengguan.modules.sys.entity.User user = new User();
//		user.setId(userid);
//		user.setPassword(SystemService.entryptPassword(password));
//		
//		try {
//			Integer result = userDao.updatePasswordById(user);
//			if(result>0)
//			{
//				jsonObject.put("msg", "success");
//				jsonObject.put("code", 200);
//				jsonObject.put("result", "success");
//			}
//			else {
//				jsonObject.put("msg", "success");
//				jsonObject.put("code", 200);
//				jsonObject.put("result", "failed");
//			}
//			PrintWriter out = response.getWriter();
//			out.print(jsonObject.toJSONString());
//			out.flush();
//			
//		} catch (Exception e) {
//			jsonObject.put("msg", "system error");
//			jsonObject.put("code", -1);
//			PrintWriter out;
//			try {
//				out = response.getWriter();
//				out.print(jsonObject.toJSONString());
//				out.flush();
//			} catch (IOException e1) {
//			
//			}
//		}
//	}
	
	public class ApiUser
	{
		

		private String id;
		private String username;
		private String no;		// 工号
		private String name;	// 姓名
		private String officeId;
		private String officeName;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getNo() {
			return no;
		}
		public void setNo(String no) {
			this.no = no;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getOfficeId() {
			return officeId;
		}
		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}
		public String getOfficeName() {
			return officeName;
		}
		public void setOfficeName(String officeName) {
			this.officeName = officeName;
		}
		
		


	}

}