package gov.kl.chengguan.modules.oa.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.poi.ss.util.SSCellRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.oa.utils.WorkDayUtil;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.service.PushService;

/*
 * 用户首次提报案件材料完成时调用，
 * 用于设置案件提交时间和计算该子流程办结时间（使用了工作日计算—）
 */
public class CaseInstitutionApproveCreateListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseInstitutionApproveCreateListener";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static String Message ="您有一项工作等待办理";
	
	@SuppressWarnings("unchecked")
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		logger.debug(TAG, "case record create: Set Candidate according to bp variables");
		
		List<BaseUser> institutionHeaders = (List<BaseUser>)delegateTask.getVariable("InstitutionLeaders");
		PushService pusher = new PushService();
		if(institutionHeaders.size() < 1) {
			logger.debug(TAG, "ERROR,no candidate user for Task!");
		}
		else if(institutionHeaders.size() == 1) {
			delegateTask.setAssignee(institutionHeaders.get(0).getId());
			pusher.PushToUser(institutionHeaders.get(0).getId(), Message);
		}
		else {
			for(BaseUser bu : institutionHeaders)
			{
				delegateTask.addCandidateUser(bu.getId());
				pusher.PushToUser(bu.getId(), Message);
			}			
		}
	}

}
