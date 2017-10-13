package gov.kl.chengguan.modules.oa.listener;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.oa.utils.WorkDayUtil;

/*
 * 用户首次提报案件材料完成时调用，
 * 用于设置案件提交时间和计算该子流程办结时间（使用了工作日计算—）
 */
public class CaseRegApplyCompleteListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseRegApplyCompleteListener";
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		logger.debug(TAG, "case registration: Case Attachments Submit");
		/*
		 * 计算工作日，设置超时（5个工作日）
		 */
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar ca =Calendar.getInstance();
		Date dtStart = ca.getTime();
		ca.setTime(dtStart);
		Date dtDue = WorkDayUtil.addDateByWorkDay(ca, 5).getTime();

		// 转换为流程变量
		delegateTask.setVariable("caseRegStartDate", dtStart);		
		delegateTask.setVariable("caseRegDueDate", dtDue);
		//
		logger.debug(TAG, "reg start date: " + dtStart.toString() + "reg due date:" + dtDue.toString());	
	}
}
