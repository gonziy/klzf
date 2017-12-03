package gov.kl.chengguan.modules.oa.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.sys.service.PushService;

public class Doc3OfficeDispatchDocCreateListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String TAG="Doc3OfficeDispatchDocCreateListener";
	private static String Message ="您有一项发文工作待审批";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		String applyer = (String)delegateTask.getVariable("applyer");
		String officeHeader = (String)delegateTask.getVariable("officeHeader");
	
		PushService pusher = new PushService();
		
		if(applyer != null) pusher.PushToUser(applyer, Message);
		if(officeHeader != null) pusher.PushToUser(officeHeader, Message);	
		
	}

}
