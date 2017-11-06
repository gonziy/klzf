package gov.kl.chengguan.modules.oa.listener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.poi.ss.util.SSCellRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.service.UserService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;
import groovyjarjarantlr.debug.ListenerBase;

/*
 * 经办机构确认
 * 用于根据承办人选择承办机构负责人，将其添加到备选用户表中
 * 由于手机端更新后直接提报到此流程节点，因此在此节点中设置相应的参数
 */
public class CaseRegInstitutionApproveCreateListener implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String TAG="CaseInstitutionApproveCreateListener";
	
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
		// 超时检查
		
		// 在任务complete之前设置变量，在此处
		String caseAssigneeIds = (String)delegateTask.getVariable("caseAssigneeIds");
		logger.debug(TAG, "get assigneeIds from Complete: " + caseAssigneeIds);			
		List<String> caseCandidateUsers = new ArrayList<String>();	
		if(caseAssigneeIds!=null && caseAssigneeIds.length() > 2)
		{
			String[] ss = caseAssigneeIds.split(";");
			if(ss.length > 0) {
				for(String s1 : ss) {
					System.out.println("---->><<<----Get Task Candidate User: " + s1);
					caseCandidateUsers.add(s1);
					// 设置其办理人为办理人
				}
				
				// 根据该任务人得到其所属机构以及机构的管理人
				UserService userService = new UserService();
				List<BaseUser> institutionLds = userService.getInstitutionUser(ss[0], "中队长");			

				List<BaseUser> deptLeaders1 = userService.getDeptLeaderUser();
				List<BaseUser> deptLeaders2 = userService.getInstitutionUser(ss[0], "队长");
				List<BaseUser> deptLds = new ArrayList<BaseUser>();
				deptLds.addAll(deptLeaders1);
				deptLds.addAll(deptLeaders2);
				
				List<BaseUser> CaseMgtUsers = userService.getMgtCenterUser();			
				List<BaseUser> MainLds = userService.getMainLeaderUser();

				// 保存流程变量，设置后续的经办人
				if(institutionLds.size() <=0 ) {
					logger.debug(TAG, "institution leaders < 0, Task maynot be completed" );
					return;
				}
				if(deptLds.size() <= 0) {
					logger.debug(TAG, "dept leaders < 0, Task maynot be completed" );
					return;
				}
				if(CaseMgtUsers.size() <=0) {
					logger.debug(TAG, "case management users < 0, Task maynot be completed" );
					return;
				}
				if(MainLds.size() <=0) {
					logger.debug(TAG, "main leader offline, Task maynot be completed" );
					return;
				}
				
				delegateTask.setVariable("cus", caseCandidateUsers);
				delegateTask.setVariable("InstitutionLeaders", institutionLds);
				delegateTask.setVariable("DeptLeaders", deptLds);
				delegateTask.setVariable("CaseMgtApprovers", CaseMgtUsers);
				delegateTask.setVariable("MainLeaders", MainLds);
				
				// 设置当前流程的经办人
				if(institutionLds.size() > 1) {
					for(BaseUser user : institutionLds)
						delegateTask.addCandidateUser(user.getId());
				}
				else {
					delegateTask.setAssignee(institutionLds.get(0).getId());
				}
			}
			else
			{
				logger.error("no user set for the process");
			}
		}
		
	}

}
