package gov.kl.chengguan.modules.oa.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.batik.parser.PathArrayProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.PUSH;

import gov.kl.chengguan.modules.sys.service.PushService;

public class Doc3LeaderApproveCompleteListener implements TaskListener {
	private static final long serialVersionUID = 1L;

	private static String TAG="Doc3LeaderApproveCompleteListener";
	private static String Message ="您有一项发文工作待审批";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		String officeHeaderId = (String)delegateTask.getVariable("officeHeader").toString();
		System.out.println(TAG + " office Header id is: " + officeHeaderId);
		PushService pusher = new PushService();
		if(officeHeaderId != null)
		{
			pusher.PushToUser(officeHeaderId, Message);
		}

	}

}
