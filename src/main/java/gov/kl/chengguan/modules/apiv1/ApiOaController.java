package gov.kl.chengguan.modules.apiv1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.jtds.jdbc.DateTime;

import org.h2.util.New;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.Result;
import com.sun.tools.javac.util.List;

import gov.kl.chengguan.common.config.Global;
import gov.kl.chengguan.common.supcan.common.Common;
import gov.kl.chengguan.common.utils.FileUtils;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.oa.dao.OaCaseFieldsDao;
import gov.kl.chengguan.modules.oa.dao.OaFilesDao;
import gov.kl.chengguan.modules.oa.entity.OaCaseFields;
import gov.kl.chengguan.modules.oa.entity.OaFiles;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;



@RestController
@RequestMapping(value = "/apiv1")
public class ApiOaController  extends BaseController {

	private static OaCaseFieldsDao caseFieldsDao = SpringContextHolder.getBean(OaCaseFieldsDao.class);
	private static OaFilesDao filesDao = SpringContextHolder.getBean(OaFilesDao.class);
	
	
	@RequestMapping(value = {"oa/casefields/get"})
	public void getCaseFieldsInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String no = request.getParameter("no");
		if(no==null || no.isEmpty())
		{
			jsonObject.put("msg", "missing url, no is null, please put a parameter");
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
			OaCaseFields fields = caseFieldsDao.get(no);
			
			
			if(fields == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				ApiOaCaseFields apiFields = new ApiOaCaseFields();
				apiFields.setNo(fields.getNo());
				apiFields.setValueFirst(fields.getValueFirst());
				apiFields.setValueSecond(fields.getValueSecond());
				apiFields.setValueThird(fields.getValueThird());
				apiFields.setIntro(fields.getIntro());
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(apiFields));
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
	
	@RequestMapping(value = {"oa/casefields/insert"})
	public void insertCaseFieldsInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String no = request.getParameter("no");
		String v1 = request.getParameter("v1");
		String v2 = request.getParameter("v2");
		String v3 = request.getParameter("v3");
		String intro = request.getParameter("intro");
		
		if(no==null || no.isEmpty())
		{
			jsonObject.put("msg", "missing url, no is null, please put a parameter");
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
			OaCaseFields fields = new OaCaseFields();
			fields.setNo(no);
			fields.setValueFirst(v1);
			fields.setValueSecond(v2);
			fields.setValueThird(v3);
			fields.setIntro(intro);
			fields.setCreateBy(new User());
			fields.setUpdateBy(new User());
			fields.setCreateDate(new Date());
			fields.setUpdateDate(new Date());
			
			int result = caseFieldsDao.insert(fields);
			
			
			
			if(result == 0)
			{
				jsonObject.put("msg", "insert data failed");
				jsonObject.put("code", -1);
				
			}else {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "success");
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
	
	@RequestMapping(value = {"oa/casefields/update"})
	public void updateCaseFieldsInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String no = request.getParameter("no");
		String v1 = request.getParameter("v1");
		String v2 = request.getParameter("v2");
		String v3 = request.getParameter("v3");
		String intro = request.getParameter("intro");
		
		if(no==null || no.isEmpty())
		{
			jsonObject.put("msg", "missing url, no is null, please put a parameter");
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
			OaCaseFields fields = caseFieldsDao.get(no);
			fields.setNo(no);
			if(v1!=null && !v1.isEmpty())
			{
				fields.setValueFirst(v1);
			}
			if(v2!=null && !v2.isEmpty())
			{
				fields.setValueSecond(v2);
			}
			if(v3!=null && !v3.isEmpty())
			{
				fields.setValueThird(v3);
			}
			if(intro!=null && !intro.isEmpty())
			{
				fields.setIntro(intro);
			}			
			fields.setUpdateBy(new User());
			fields.setUpdateDate(new Date());
			
			int result = caseFieldsDao.update(fields);
			
			
			
			if(result == 0)
			{
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "failed");
				
			}else {
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "success");
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
	
	@RequestMapping(value = {"oa/files/get"})
	public void getFileInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String id = request.getParameter("id");
		if(id==null || id.isEmpty())
		{
			jsonObject.put("msg", "missing url, id is null, please put a parameter");
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
			OaFiles file = filesDao.get(id);
			
			
			if(file == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				ApiOaFiles apiFile = new ApiOaFiles();
				apiFile.setFileName(file.getFileName());
				apiFile.setFilePath(file.getFilePath());
				apiFile.setGroupId(file.getGroupId());
				apiFile.setId(Long.parseLong(file.getId()));
				apiFile.setType(file.getType());
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(apiFile));
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
	
	@RequestMapping(value = {"oa/files/list"})
	public void getFileList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String groupId = request.getParameter("group_id");
		try {
			OaFiles whereFiles = new OaFiles();
			if(groupId!=null && !groupId.isEmpty())
			{
				whereFiles.setGroupId(groupId);
			}
			java.util.List<OaFiles> files = filesDao.findAllList(whereFiles);
			
			if(files == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				java.util.List<ApiOaFiles> apiFiles = new ArrayList<ApiOaFiles>();
				for (OaFiles f : files) {
					ApiOaFiles apifile = new ApiOaFiles();
					apifile.setDel_flag(f.getDelFlag());
					apifile.setFileName(f.getFileName());
					apifile.setFilePath(f.getFilePath());
					apifile.setGroupId(f.getGroupId());
					apifile.setId(Long.parseLong(f.getId()));
					apifile.setType(f.getType());
					apiFiles.add(apifile);
				}
				
				
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				
				jsonObject.put("data",JSONObject.toJSON(apiFiles));
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
	
	@RequestMapping(value = {"oa/files/delete"})
	public void deleteFileInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String id = request.getParameter("id");
		if(id==null || id.isEmpty())
		{
			jsonObject.put("msg", "missing url, id is null, please put a parameter");
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
			OaFiles file = filesDao.get(id);
			
			if(file == null)
			{
				jsonObject.put("msg", "data is null");
				jsonObject.put("code", 44004);
				
			}else {
				file.setDelFlag("1");
				int result = filesDao.delete(file);
						
				if(result>0)
				{
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "success");					
				}else 
				{
					jsonObject.put("msg", "success");
					jsonObject.put("code", 0);
					jsonObject.put("result", "failed");	
				}
				
				
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
	
	@RequestMapping(value = {"oa/files/upload"})
	public void uploadFileInfo(HttpServletRequest request, HttpServletResponse response,MultipartFile file) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		String group_id = request.getParameter("group_id");
		String file_name = request.getParameter("file_name");
		
		String filename = file.getOriginalFilename().toLowerCase();
		String fileExt = filename.substring(filename.lastIndexOf("."));
		String fileType = "";

		// 判断文件格式,非法格式禁止上传
		if (fileExt.equals(".jpg") || fileExt.equals(".jpeg")
				|| fileExt.equals(".png")) {
			fileType = "image";
		} else if (fileExt.equals(".mpeg") || fileExt.equals(".mpg")
				|| fileExt.equals(".dat") || fileExt.equals(".avi")
				|| fileExt.equals(".mov") || fileExt.equals(".asf")
				|| fileExt.equals(".wmv") || fileExt.equals(".3gp")
				|| fileExt.equals(".mkv") || fileExt.equals(".flv")
				|| fileExt.equals(".f4v") || fileExt.equals(".rmvb")) {
			fileType = "video";
		} else if (fileExt.equals(".mp3") || fileExt.equals(".wma")
				|| fileExt.equals(".arm") || fileExt.equals(".wave")
				|| fileExt.equals(".aiff") || fileExt.equals(".flac")
				|| fileExt.equals(".aac")) {
			fileType = "audio";
		} else if (fileExt.equals(".txt") || fileExt.equals(".doc")
				|| fileExt.equals(".docx") || fileExt.equals(".ppt")
				|| fileExt.equals(".pptx") || fileExt.equals(".xls")
				|| fileExt.equals(".xlsx") || fileExt.equals(".pdf")
				|| fileExt.equals(".rtf") || fileExt.equals(".odt")) {
			fileType = "document";
		} else {
			jsonObject.put("msg", "invalid file type");
			jsonObject.put("code", 40004);
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
		String folderName = new SimpleDateFormat ("yyyyMMdd").format(new Date());
		String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
				+ fileType + "/" + folderName + "/";
		FileUtils.createDirectory(FileUtils.path(realPath));
		//设置服务器上存储的文件名，并将文件转存
		String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ((int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000) + fileExt;
		String file_path = realPath + newFileName;
		try {
			file.transferTo(new File(file_path));
		} catch (Exception e) {
			jsonObject.put("msg", "success");
			jsonObject.put("code", 0);
			jsonObject.put("result", "failed");
			jsonObject.put("remark", "save file failed");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonObject.toJSONString());
				out.flush();
			} catch (IOException e1) {
			
			}
			return;
		}
		
		//如果不自定义文件名，则为上传的文件名
		if(file_name==null || file_name.isEmpty()){
			file_name = filename.substring(0,filename.lastIndexOf("."));
		}
		if(group_id==null || group_id.isEmpty())
		{
			jsonObject.put("msg", "missing url, group_id is null");
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
		String httpPath = "http://localhost:9090/day5"
				+ Global.USERFILES_BASE_URL  + fileType + "/"
				+ folderName + "/" + newFileName;
		try {
			OaFiles oaFiles = new OaFiles();
			oaFiles.setCreateBy(new User());
			oaFiles.setCreateDate(new Date());
			oaFiles.setFileName(file_name);
			oaFiles.setFilePath(httpPath);
			oaFiles.setGroupId(group_id);
			oaFiles.setType(fileType);
			
			int result = filesDao.insert(oaFiles);
			if(result>0){
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "success");
				jsonObject.put("path", httpPath);
			}else{
				jsonObject.put("msg", "success");
				jsonObject.put("code", 0);
				jsonObject.put("result", "failed");
				jsonObject.put("remark", "insert to database failed");
			}
			PrintWriter out;
			out = response.getWriter();
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
	
	
	
	
	public class ApiOaCaseFields{

		private String no;
		private String valueFirst; //第1个关键字
		private String valueSecond; //第2个关键字
		private String valueThird; //第3个关键字
		private String intro;  //案情简介
		
		
		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getValueFirst() {
			return valueFirst;
		}

		public void setValueFirst(String valueFirst) {
			this.valueFirst = valueFirst;
		}

		public String getValueSecond() {
			return valueSecond;
		}

		public void setValueSecond(String valueSecond) {
			this.valueSecond = valueSecond;
		}

		public String getValueThird() {
			return valueThird;
		}

		public void setValueThird(String valueThird) {
			this.valueThird = valueThird;
		}

		public String getIntro() {
			return intro;
		}

		public void setIntro(String intro) {
			this.intro = intro;
		}
	}
	
	public class ApiOaFiles
	{
		private Long id;

		private String groupId; 
		private String fileName;
		private String filePath; 
		private String type;
		private String del_flag;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDel_flag() {
			return del_flag;
		}
		public void setDel_flag(String del_flag) {
			this.del_flag = del_flag;
		}
	}
	

}