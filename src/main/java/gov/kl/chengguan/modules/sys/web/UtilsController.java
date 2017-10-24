package gov.kl.chengguan.modules.sys.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value = "${adminPath}/sys/utils")
public class UtilsController {
    @RequestMapping(value = "/uploadifive")
    @ResponseBody
    public String uploadiFive(HttpServletRequest request, HttpServletResponse response){
    	// 返回文件列表
    	// 记录源文件和目标文件全名
        Map<String, Object> results = new HashMap<String, Object>();  

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");    
        
        String sourceFileName = null;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {

            MultipartFile mf = entity.getValue();
            sourceFileName = mf.getOriginalFilename();
            
            // 得到目标文件名
            /*
             * 需要修改上传的目标文件夹
             */
            String targetFileName = request.getSession().getServletContext().getRealPath("")
            		+"uploads/"
            		+sourceFileName.substring(0,sourceFileName.lastIndexOf("."))
            		+"-"
            		+sdf.format(new Date())+ sourceFileName.substring(sourceFileName.lastIndexOf('.'));            
            
            File targetFile = new File(targetFileName);  
            if (!targetFile.exists()) {  
                targetFile.mkdirs();  
            }  
            try {  
            	mf.transferTo(targetFile);

                System.out.println("状态-" + "文件:" + sourceFileName +"上传成功,目标:" + targetFile);           
                results.put("sourceFile", sourceFileName);
                results.put("targetFile", targetFileName);                
            } catch (Exception e) {
            	e.printStackTrace(); 
            }          
        }        
        String result= JSON.toJSONString(results);
        return result;
    }
}
