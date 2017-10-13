/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.oa.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.utils.UserUtils;
import gov.kl.chengguan.modules.oa.dao.OaCaseDao;
import gov.kl.chengguan.modules.oa.entity.OaCase;
import gov.kl.chengguan.modules.oa.service.OaCaseService;

/**
 * 审批Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaCase")
public class OaCaseController extends BaseController {
	@Autowired
	private OaCaseService oaCaseService;
	
	@ModelAttribute
	public OaCase get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		OaCase oaCase = null;
		if (StringUtils.isNotBlank(id)){
			oaCase = oaCaseService.get(id);
//		}else if (StringUtils.isNotBlank(procInsId)){
//			oaCase = oaCaseService.getByProcInsId(procInsId);
		}
		if (oaCase == null){
			oaCase = new OaCase();
		}
		return oaCase;
	}
	
	@RequiresPermissions("oa:oaCase:view")
	@RequestMapping(value = {"list"})
	public String list(OaCase oaCase, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OaCase> page = oaCaseService.findPage(new Page<OaCase>(request, response), oaCase); 
        model.addAttribute("page", page);        
		return "modules/oa/oaCaseList";
	}
	
	/**
	 * 申请单填写
	 * 展示的页面要根据当前办结阶段变化
	 * @param oaCase
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oa:oaCase:view")
	@RequestMapping(value = "form")
	public String form(OaCase oaCase, Model model) {
		model.addAttribute("oaCase", oaCase);
		String view = "oaCase_Reg1_apply";

		// 查看审批申请单
		if (StringUtils.isNotBlank(oaCase.getId())){//.getAct().getProcInsId())){
			// 环节编号
			String taskDefKey = oaCase.getAct().getTaskDefKey();
			
			// 查看案件情况表
			if(oaCase.getAct().isFinishTask()){
				view = "oaCaseView";
			}
			// 修改案件申报
			else if ("modify".equals(taskDefKey)){
				view = "oaCaseModify";
			}
			// 案件申报
			else if ("utAnjianShenbao".equals(taskDefKey)){
				view = "oaCase_Reg1_apply";
			}
			// 案件初审
			else if ("utAnjianChushen".equals(taskDefKey)){
				view = "oaCase_Reg1_approve";
			}
			// 案件信息录入
			else if ("utAnjianLuru".equals(taskDefKey)){
				view = "oaCase_Reg2_record";
			}
			// 立案审批——承办机构
			else if ("utLaShp_Cbjg".equals(taskDefKey)){
				view = "oaCase_Reg3_approve";
			}
			// 立案审批——分管领导
			else if ("utLaShp_Fgld".equals(taskDefKey)){
				view = "oaCase_Reg4_approve";
			}			
			// 立案审批——主管领导
			else if ("utLaShp_Zgld".equals(taskDefKey)){
				view = "oaCase_Reg5_approve";
			}
			// 开始案件调查
			else if ("utAnjianDiaocha".equals(taskDefKey)){
				view = "oaCase_Survey";
			}
			// 行政处罚——承办人意见
			else if ("utXzhChf_CbrYj".equals(taskDefKey)){
				view = "oaCase_Penal";
			}
			// 行政处罚——承办机构审批
			else if ("utXzhChf_CbJg".equals(taskDefKey)){
				view = "oaCase_Penal";
			}
			// 行政处罚——案件管理中心审批
			else if ("utXzhChf_AjGlZhx".equals(taskDefKey)){
				view = "oaCase_Penal";
			}
			// 行政处罚——分管领导审批
			else if ("utXzhChf_Fgld".equals(taskDefKey)){
				view = "oaCase_Penal";
			}
			// 行政处罚——主管领导审批
			else if ("utXzhChf_Zgld".equals(taskDefKey)){
				view = "oaCase_Penal";
			}
			
			//
			// 结案审批——承办人
			else if ("utJaShp_Cbr".equals(taskDefKey)){
				view = "oaCase_Close";
			}
			// 结案审批——承办机构
			else if ("utJaShp_Cbjg".equals(taskDefKey)){
				view = "oaCase_Close";
			}
			// 结案审批——案件管理中心
			else if ("utJaShp_AjGlZhx".equals(taskDefKey)){
				view = "oaCase_Close";
			}
			// 结案审批——主管领导
			else if ("utJaShp_Zgld".equals(taskDefKey)){
				view = "oaCase_Close";
			}
			// 都不是
			else {
				view = "oaCaseList";
			}
		}

		model.addAttribute("oaCase", oaCase);
		return "modules/oa/" + view;
	}
	
	//@RequiresPermissions("oa:oaCase:view")
	@RequestMapping(value = {"list/task",""})
	public String taskList(HttpSession session, Model model) {
		/*
		String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		List<OaCase> results = oaCaseService.findTodoTasks(userId);
		model.addAttribute("oaCases", results);
		
		return "modules/oa/oaCaseTask";
		*/
		// 已经可以返回结果，但是结果页面没有注册新的流程
		return "redirect:" + adminPath + "/act/task/todo/"; 
	}
	
	/**
	 * 申请单保存/修改
	 * @param oaCase
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:oaCase:edit")
	@RequestMapping(value = "save")
	public String save(OaCase oaCase, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaCase)){
			return form(oaCase, model);
		}
		oaCaseService.save(oaCase);
		addMessage(redirectAttributes, "提交成功");
		System.out.println("----call complete current task!");
		oaCaseService.CompleteCurrentTask(oaCase);
		return "redirect:" + adminPath + "/act/task/todo/";
	}

	/**
	 * 工单执行（完成任务）
	 * @param oaCase
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oa:oaCase:edit")
	@RequestMapping(value = "saveCase")
	public String saveCase(OaCase oaCase, Model model) {
		/*
		 * xuyao jinyibuchul
		 */
		if (StringUtils.isBlank(oaCase.getAct().getFlag())) {
			if("yes".equals(oaCase.getAct().getFlag()))
				oaCase.getAct().setComment("passed");
			else
				oaCase.getAct().setComment("failed");
				//|| StringUtils.isBlank(oaCase.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(oaCase, model);
		}
		oaCaseService.SaveCaseStep(oaCase);
		// 返回了待办任务列表
		
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 删除工单
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:oaCase:edit")
	@RequestMapping(value = "deleteCase")
	public String deleteCase(OaCase oaCase, RedirectAttributes redirectAttributes) {
		oaCaseService.delete(oaCase);
		addMessage(redirectAttributes, "删除审批成功");
		return "redirect:" + adminPath + "/oa/oaCase/?repage";
	}

	/*
	 * 需要删除子流程的函数
	 */
}