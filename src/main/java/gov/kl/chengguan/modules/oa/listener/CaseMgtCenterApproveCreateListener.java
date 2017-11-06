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

/*
 * 用户首次提报案件材料完成时调用，
 * 用于设置案件提交时间和计算该子流程办结时间（使用了工作日计算—）
 */
public class CaseMgtCenterApproveCreateListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseCaseMgtCenterApproveCreateListener";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		logger.debug(TAG, "case record create: Set Candidate according to bp variables");
		
		List<BaseUser> cmas = (List<BaseUser>)delegateTask.getVariable("CaseMgtApprovers");
		if(cmas.size() < 1) {
			logger.debug(TAG, "ERROR,no candidate user for Task!");
		}
		else if(cmas.size() == 1) {
			delegateTask.setAssignee(cmas.get(0).getId());
		}
		else {
			for(BaseUser bu : cmas)
			{
				delegateTask.addCandidateUser(bu.getId());
			}			
		}
	}

}
