package gov.kl.chengguan.modules.oa.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.oa.utils.WorkDayUtil;

/*
 * 用户首次提报案件材料完成时调用，
 * 用于设置案件提交时间和计算该子流程办结时间（使用了工作日计算—）
 */
public class CaseRegCheckCreateListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseRegCheckCreateListener";
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		logger.debug(TAG, "case registration check: Case Submitted");
		/*
		 * 测试：案件初审人员设置为 admin
		 */

		delegateTask.setAssignee("1");
	}
}
