package gov.kl.chengguan.modules.oa.listener;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.service.PushService;
import gov.kl.chengguan.modules.sys.service.UserService;

public class Doc3OfficeHeaderApproveCreateListener implements TaskListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String TAG="Doc3OfficeHeaderApproveCreateListener";
	private static String Message ="您有一项发文工作待审批";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		logger.info(TAG, "set Office Header");
		
		UserService userService = new UserService();
		List<BaseUser> officeHeaders = userService.getOfficeLeader();
		PushService pusher = new PushService();
		
		// 只处理第一个
		if(officeHeaders.size() >0 ) {
			String officeHeaderId = officeHeaders.get(0).getId();
			// ${officeHeader}
			delegateTask.setVariable("officeHeader", officeHeaderId);
			delegateTask.setAssignee(officeHeaderId);
			pusher.PushToUser(officeHeaderId, Message);
		}
		else {
			logger.error(TAG, "no office Headers Error! doc can not be approved!");
		}
	}

}
