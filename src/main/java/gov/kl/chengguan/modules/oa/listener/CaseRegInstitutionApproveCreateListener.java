package gov.kl.chengguan.modules.oa.listener;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.poi.ss.util.SSCellRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.kl.chengguan.modules.oa.utils.WorkDayUtil;
import gov.kl.chengguan.modules.sys.utils.UserUtils;

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
		if(caseAssigneeIds!=null && caseAssigneeIds.length() > 2)
		{
			String[] ss = caseAssigneeIds.split(";");
			for(String s1 : ss)
			{
				System.out.println("---->><<<----Get Task Candidate User: " + s1);
			}
			// 根据该任务人得到其所属机构以及机构的管理人
			// 设置其办理人为办理人
		}
		
		/*
		 * 查询获取用户所在的部门，
		 * 得到部门的领导，再Institute审核中设置candidateUser，用setVariable方法设置{instituteleader}
		 * 查询对应的deptLeader
		 * 设置caseManagementCenter管理人
		 * 设置相应的mainLeader
		 */
		delegateTask.addCandidateUser("1");
	}

}
