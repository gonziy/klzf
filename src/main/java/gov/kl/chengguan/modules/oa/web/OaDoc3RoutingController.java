package gov.kl.chengguan.modules.oa.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.search.DateTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;

import gov.kl.chengguan.common.mapper.JsonMapper;
import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.oa.entity.OaDoc3;
import gov.kl.chengguan.modules.oa.service.OaDoc3RoutingService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/oa/doc3Routing")
public class OaDoc3RoutingController extends BaseController {
	@Autowired
	protected OaDoc3RoutingService oaDoc3RoutingService;

	@ModelAttribute
	public OaDoc3 get(@RequestParam(required=false) String id){//, 
	//		@RequestParam(value="act.procInsId", required=false) String procInsId) {
		OaDoc3 oaDoc3 = null;
		if (StringUtils.isNotBlank(id)){
			oaDoc3 = oaDoc3RoutingService.get(id);
		}
		if (oaDoc3 == null){
			oaDoc3 = new OaDoc3();
		}
		return oaDoc3;
	}
	
	//@RequiresPermissions("oa:oaDoc3:view")
	@RequestMapping(value = {"list"})
	public String list(OaDoc3 oaDoc3, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Page<OaDoc3> page = oaDoc3RoutingService.findPage(new Page<OaDoc3>(request, response), oaDoc3); 
	    model.addAttribute("page", page); 
	    //
		return "modules/oa/oaDoc3List";
	}
	
	/**
	 * 申请单填写
	 * 展示的页面要根据当前办结阶段变化
	 * @param oaDoc3
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("oa:oaDoc3:view")
	@RequestMapping(value = "form")
	public String form(OaDoc3 oaDoc3, Model model) {
		// 案件申报
		String view = "doc3_0Apply";
	
		// 查看审批申请单
		if (StringUtils.isNotBlank(oaDoc3.getId())){//.getAct().getProcInsId())){
			// 环节编号
			String taskDefKey = oaDoc3.getAct().getTaskDefKey();
			
			// 查看案件情况表
			if(oaDoc3.getAct().isFinishTask()){
				view = "doc3_0Apply";
			}
			// 办公室主任审核发文
			else if ("utOfficeHeaderApprove".equals(taskDefKey)){
				view = "doc3_1OfficeHeaderApprove";
			}
			// 领导审阅发文，签署意见，确定传阅人
			else if ("utLeaderApprove".equals(taskDefKey)){
				view = "doc3_2LeaderApprove";
			}
			// 通知提交人修改
			else if ("utInformApplyer".equals(taskDefKey)){
				view = "doc3_2InformApplyer";
			}
			// 办公室主任确定领导意见，转发文件给传阅人
			else if ("utOfficeHeaderDispatch".equals(taskDefKey)){
				view = "doc3_3OfficeHeaderDispatch";
			}			
			// 传阅人收文确认
			else if ("utBrowseDoc".equals(taskDefKey)){
				view = "doc3_4BrowseDoc";
			}
			// 都不是
			else {
				view = "doc3_0Apply";
			}
		}
	
		model.addAttribute("oaDoc3", oaDoc3);
		return "modules/oa/" + view;
	}
	
	//@RequiresPermissions("oa:oaDoc3:view")
	@RequestMapping(value = {"list/task",""})
	public String taskList(HttpSession session, Model model) {
//		Page<OaDoc3> page = oaDoc3RoutingService.findPage(new Page<OaDoc3>(request, response), oaDoc3); 
//	    model.addAttribute("page", page); 
//	    //
//		return "modules/oa/doc3RoutingTodoList";
		
		return "redirect:" + adminPath + "/act/task/todo/"; 
	}
	
	/**
	 * 申请单保存/修改
	 * @param oaDoc3
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	//@RequiresPermissions("oa:oaDoc3:edit")
	@RequestMapping(value = "save")
	public String save(OaDoc3 oaDoc3, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaDoc3)){
			return form(oaDoc3, model);
		}
	
		String initiator = UserUtils.getUser().getId();		
		Map<String, Object> vars = new HashMap<String, Object>();
		System.out.println("process applyer:" + initiator);
		vars.put("applyer", initiator);		
		oaDoc3RoutingService.save(oaDoc3, vars);
		//*/
		addMessage(redirectAttributes, "提交成功");
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 工单执行（完成任务）
	 * @param oaDoc3
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("oa:oaDoc3:edit")
	@RequestMapping(value = "saveDoc3")
	public String saveDoc3(OaDoc3 oaDoc3, Model model) {
	
		if (StringUtils.isBlank(oaDoc3.getAct().getFlag())) {
			if("yes".equals(oaDoc3.getAct().getFlag()))
				oaDoc3.getAct().setComment("passed");
			else
				oaDoc3.getAct().setComment("failed");

			return form(oaDoc3, model);
		}
		oaDoc3RoutingService.saveStep(oaDoc3);
		// 返回了待办任务列表		
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 删除工单
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	//@RequiresPermissions("oa:oaDoc3:edit")
	@RequestMapping(value = "deleteDoc3")
	public String deleteCase(OaDoc3 oaDoc3, RedirectAttributes redirectAttributes) {
		oaDoc3RoutingService.delete(oaDoc3);
		addMessage(redirectAttributes, "删除审批成功");
		return "redirect:" + adminPath + "/oa/oaDoc3/?repage";
	}

}
