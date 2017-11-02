package gov.kl.chengguan.modules.apiv1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.DATE;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.util.ByteSource;
import org.h2.util.New;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import gov.kl.chengguan.common.security.Digests;
import gov.kl.chengguan.common.utils.Encodes;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.sys.dao.BaseUserDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.security.SystemAuthorizingRealm.Principal;
import gov.kl.chengguan.modules.sys.service.PushService;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.service.UserService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;



@RestController
@RequestMapping(value = "/apiv1")
public class ApiUserController  extends BaseController {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static BaseUserDao baseUserDao = SpringContextHolder.getBean(BaseUserDao.class);
	
	@RequestMapping(value = {"user/device/push"})
	public void test(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");

		String user_id = request.getParameter("id");
		PushService pushService = new PushService();
		pushService.PushToUser(user_id, "你好，百度推送" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		

		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		jsonObject.put("remark", "");
		jsonObject.put("result", "success");
		jsonObject.put("msg", "success");
		jsonObject.put("code", 0);
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@RequestMapping(value = {"user/info/login"})
	public void login(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String login_name = request.getParameter("login_name");
		String password = request.getParameter("password");
		if((login_name==null || login_name.isEmpty())||(password==null || password.isEmpty()))
		{
			jsonObject.put("msg", "missing url, username or password is null");
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
			BaseUser baseUser = UserUtils.getBaseByLoginName(login_name);
			if(baseUser==null)
			{

				jsonObject.put("msg", "user data is null");
				jsonObject.put("code", 44004);
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

			String plainPassword = password;
			String sourcePassword = baseUser.getPassword();
			String plain = Encodes.unescapeHtml(plainPassword);
			byte[] salt = Encodes.decodeHex(sourcePassword.substring(0,16));
			byte[] hashPassword = Digests.sha1(plain.getBytes(),salt,1024);
			String pwd = Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
			boolean result = sourcePassword.equals(pwd);
			

			if(!result)
			{
				jsonObject.put("remark", "login failed");
				jsonObject.put("result", "failure");
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
			}else {

				jsonObject.put("remark", "login success");
				jsonObject.put("result", "success");
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);

				ApiUser user = new ApiUser();
				user.setId(baseUser.getId());
				user.setUsername(baseUser.getLoginName());
				user.setNo(baseUser.getNo());
				user.setName(baseUser.getName());
				user.setOfficeId(baseUser.getOffice().getId());
				user.setOfficeName(baseUser.getOffice().getName());
				user.setBaiduPushChannelId(baseUser.getBaiduPushChannelId()==null?"":baseUser.getBaiduPushChannelId());
				
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
	
	@RequestMapping(value = {"user/info/get"})
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String login_name = request.getParameter("login_name");
		String userid = request.getParameter("userid");
		if((login_name==null || login_name.isEmpty())&&(userid==null || userid.isEmpty()))
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
			if((login_name == null || login_name.isEmpty()) && !userid.isEmpty())
			{
				baseUser = UserUtils.getBaseById(userid);
			}else if(!login_name.isEmpty() && (userid==null || userid.isEmpty()))
			{
				baseUser = UserUtils.getBaseByLoginName(login_name);
			}
			
			if(baseUser == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {

				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);

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
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		String officeId = request.getParameter("office_id");
		String childOfficeName = request.getParameter("child_office_name");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		try {
			
			BaseUser whereBaseUser = new BaseUser();
			Office whereOffice = new Office();
			if(officeId!=null && !officeId.isEmpty()){
				whereOffice.setId(officeId);
			}
			whereBaseUser.setOffice(whereOffice);
			java.util.List<BaseUser> list =  UserUtils.getBaseAllList(whereBaseUser);
			if(list == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {

				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				ArrayList<ApiUser> apiUsers = new ArrayList<ApiUser>();
				for (BaseUser user : list) {
					if(childOfficeName!=null && !childOfficeName.isEmpty())
					{
						String tmp_office_name = "";
						tmp_office_name = user.getOffice().getName();
						if(tmp_office_name.equals(childOfficeName))
						{
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
					}else{
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
	
	@RequestMapping(value = {"user/info/updatebaidupush"})
	public void setBaiduPush(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();

		String userid = request.getParameter("userid");
		String baiduId = request.getParameter("baiduid");
		
		if(userid==null || userid.isEmpty())
		{
			jsonObject.put("msg", "missing url, userid is null");
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
			User user = new User();
			user.setId(userid);
			user.setBaiduPushChannelId(baiduId);
			
			Integer result = userDao.updateBaiduPushChannelIdById(user);
			if(result>0)
			{
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "success");
			}
			else {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "failed");
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
	
	public class ApiUser
	{
		

		private String id;
		private String username;
		private String no;		// 工号
		private String name;	// 姓名
		private String officeId;
		private String officeName;
		private String baiduPushChannelId;//百度推送账号
		public String getBaiduPushChannelId() {
			return baiduPushChannelId;
		}

		public void setBaiduPushChannelId(String baiduPushChannelId) {
			this.baiduPushChannelId = baiduPushChannelId;
		}
		
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