package gov.kl.chengguan.modules.oa.listener;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 设置继续向下执行的变量
 */
public class CaseSurveyCompleteListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseSurveyCreateListener";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		Calendar ca = Calendar.getInstance();
		Date dtSurvey = ca.getTime();		
		logger.debug(TAG, "case survey create date: " + dtSurvey);	
		delegateTask.setVariable("regPass", 1);
	}

}
