package gov.kl.chengguan.modules.oa.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.search.DateTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;

import gov.kl.chengguan.common.mapper.JsonMapper;
import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.oa.entity.OaDoc;
import gov.kl.chengguan.modules.oa.service.OaDocRoutingService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/oa/docRouting")
public class DocRoutingController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected OaDocRoutingService docRoutingService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected TaskService taskService;

	/*
	 * 
	 * 先关闭shiro权限
	 */
	
	//@RequiresPermissions("oa:docRouting:view")
	@RequestMapping(value = {"form"})
	public String form(OaDoc doc, Model model) {
		model.addAttribute("doc", doc);
		return "modules/oa/docRoutingForm";
	}

	/**
	 * 启动公文流转
	 */
	//@RequiresPermissions("oa:docRouting:edit")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@ModelAttribute("doc") OaDoc doc, RedirectAttributes redirectAttributes) {
		try {				
			//将js分割的逗号改为分号
			doc.setDocApproverIDs(doc.getDocApproverIDs().replace(",",";"));
			// 部分字段需要补充
			doc.setCreateBy(UserUtils.getUser());	
			doc.setCreateDate(Calendar.getInstance().getTime());
			
			//System.out.println("docRouting save:" + doc.toString());				
			Map<String, Object> variables = Maps.newHashMap();
			docRoutingService.save(doc, variables);
			addMessage(redirectAttributes, "流程已启动，流程ID：" + doc.getProcessInstanceId());
		} catch (Exception e) {
			logger.error("启动公文流转失败：", e);
			addMessage(redirectAttributes, "系统内部错误！");
		}
		return "modules/oa/docRoutingList";
		//return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 任务列表
	 * @param leave	
	 */
	//@RequiresPermissions("oa:docRouting:view")
	@RequestMapping(value = {"list/task",""})
	public String taskList(HttpSession session, Model model) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		
		System.out.println("the User Task list:" + userId);
		List<OaDoc> results = docRoutingService.findTodoTasks(userId);
		model.addAttribute("docs", results);
		return "modules/oa/docRoutingTask";
	}

	/**
	 * 读取所有流程
	 * @return
	 */
	//@RequiresPermissions("oa:docRouting:view")
	@RequestMapping(value = {"list"})
	public String list(OaDoc doc, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OaDoc> page = docRoutingService.find(new Page<OaDoc>(request, response), doc); 
        model.addAttribute("page", page);
		return "modules/oa/docRoutingList";
	}
	
	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail/{id}")
	@ResponseBody
	public String getOaDoc(@PathVariable("id") String id) {
		OaDoc doc = docRoutingService.get(id);
		String jsonDoc = JsonMapper.getInstance().toJson(doc);
		return jsonDoc;
	}

	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail-with-vars/{id}/{taskId}")
	@ResponseBody
	public String getLeaveWithVars(@PathVariable("id") String id, @PathVariable("taskId") String taskId) {
		OaDoc doc = docRoutingService.get(id);
		Map<String, Object> variables = taskService.getVariables(taskId);
		doc.setVariables(variables);
		return JsonMapper.getInstance().toJson(doc);
	}
	
    @InitBinder  
    public void initBinder(WebDataBinder binder, WebRequest request) {  
          
        //转换日期  
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }  

}
