package gov.kl.chengguan.modules.oa.listener;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.poi.ss.util.SSCellRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.oa.utils.WorkDayUtil;

/*
 * 用户首次提报案件材料完成时调用，
 * 用于设置案件提交时间和计算该子流程办结时间（使用了工作日计算—）
 */
public class CaseRegDeptLdrApproveCreateListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseDeptLdrApproveCreateListener";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		logger.debug(TAG, "case record create: Set Candidate according to bp variables");
		/*
		 * 计算工作日，设置超时（5个工作日）
		 */
		Date dtStart = (Date)delegateTask.getVariable("caseRegStartDate");
		Date dtDue = (Date)delegateTask.getVariable("caseRegDueDate");
		// 检查参数获取的情况
		logger.debug(TAG, "reg start date: " + dtStart.toString() + "reg due date:" + dtDue.toString());	
		Calendar ca = Calendar.getInstance();
		// 超时检查
		
		// 在任务complete之前设置变量，在此处
		//delegateTask.addCandidateUser("1");
		delegateTask.setAssignee("1");
	}

}
