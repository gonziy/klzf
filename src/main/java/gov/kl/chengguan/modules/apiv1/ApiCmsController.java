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
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;
import com.sun.tools.javac.resources.javac;
import com.sun.tools.javac.util.List;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;


@RestController
@RequestMapping(value = "/apiv1")
public class ApiCmsController  extends BaseController {
	
	@RequestMapping(value = {"test"})
	public void test(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
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
			jsonObject.put("code", 200);
			jsonObject.put("data", users);
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();
		} catch (Exception e) {

		}

	}
	@RequestMapping(value = {"user/info/get"})
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		try {
			jsonObject.put("msg", "success");
			jsonObject.put("code", 200);
			jsonObject.put("data", UserUtils.getBaseByLoginName("gonziy"));
			PrintWriter out = response.getWriter();
			out.print(jsonObject.toJSONString());
			out.flush();

		} catch (Exception e) {

		}

	}
		

}