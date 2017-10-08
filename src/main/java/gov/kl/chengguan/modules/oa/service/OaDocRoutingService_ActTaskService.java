package gov.kl.chengguan.modules.oa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.service.CrudService;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.act.utils.ActUtils;
import gov.kl.chengguan.modules.oa.dao.OaDocDao;
import gov.kl.chengguan.modules.oa.entity.OaDoc;

@Service
@Transactional(readOnly = true)
public class OaDocRoutingService_ActTaskService extends CrudService<OaDocDao, OaDoc>{
	@Autowired
	private ActTaskService actTaskService;
	
	public OaDoc getByProcInsId(String procInsId) {
		return dao.getByProcInsId(procInsId);
	}
	
	public Page<OaDoc> findPage(Page<OaDoc> page, OaDoc doc) {
		doc.setPage(page);
		page.setList(dao.findList(doc));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(OaDoc doc) {
		
		// 申请发起
		if (StringUtils.isBlank(doc.getId())){
			doc.preInsert();
			dao.insert(doc);
			
			// !!! 这里需要对相应的字符串进行分解处理，填写doc中的相应字段
			String[] ss = doc.getDocApproverIDs().split(";");
			ArrayList< String> approvers = new ArrayList<String>();
			for(String s: ss)
			{
				approvers.add(s.trim());
			}		
			
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("approvers", approvers);
			
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_DOC_ROUTIN[0], ActUtils.PD_DOC_ROUTIN[1], 
					doc.getId(), 
					doc.getDocTitle(),
					variables);
		}		
		// 重新编辑申请		
		else{
			doc.preUpdate();
			dao.update(doc);
		}
	}

	/**
	 * 审核审批保存
	 * @param doc
	 */
	@Transactional(readOnly = false)
	public void approveSave(OaDoc doc) {
		
		// 设置意见
		doc.getAct().setComment(("yes".equals(doc.getAct().getFlag())?"[同意] ":"[驳回] ")+doc.getAct().getComment());
		
		doc.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = doc.getAct().getTaskDefKey();

		// 审核环节
		if ("audit".equals(taskDefKey)){
			
		}
		else if ("audit2".equals(taskDefKey)){

		}
		else if ("apply_end".equals(taskDefKey)){
			
		}
		
		// 未知环节，直接返回
		else{
			return;
		}
		
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(doc.getAct().getFlag())? "1" : "0");
		actTaskService.complete(doc.getAct().getTaskId(), doc.getAct().getProcInsId(), doc.getAct().getComment(), vars);

//		vars.put("var_test", "yes_no_test2");
//		actTaskService.getProcessEngine().getTaskService().addComment(doc.getAct().getTaskId(), doc.getAct().getProcInsId(), doc.getAct().getComment());
//		actTaskService.jumpTask(doc.getAct().getProcInsId(), doc.getAct().getTaskId(), "audit2", vars);
	}
}
