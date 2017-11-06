package gov.kl.chengguan.modules.apiv1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.DATE;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.util.ByteSource;
import org.h2.util.New;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import gov.kl.chengguan.common.security.Digests;
import gov.kl.chengguan.common.utils.Encodes;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.web.BaseController;
import gov.kl.chengguan.modules.act.service.ActTaskService;
import gov.kl.chengguan.modules.cms.service.BaseArticleService;
import gov.kl.chengguan.modules.cms.utils.CmsUtils;
import gov.kl.chengguan.modules.oa.dao.OaDoc3Dao;
import gov.kl.chengguan.modules.oa.entity.OaDoc3;
import gov.kl.chengguan.modules.oa.service.OaDoc3RoutingService;
import gov.kl.chengguan.modules.sys.dao.BaseUserDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.security.SystemAuthorizingRealm.Principal;
import gov.kl.chengguan.modules.sys.service.PushService;
import gov.kl.chengguan.modules.sys.service.SystemService;
import gov.kl.chengguan.modules.sys.service.UserService;
import gov.kl.chengguan.modules.sys.utils.UserUtils;



@RestController
@RequestMapping(value = "/apiv1")
public class ApiDocumentRouteController  extends BaseController {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static BaseUserDao baseUserDao = SpringContextHolder.getBean(BaseUserDao.class);
	private static OaDoc3Dao oaDoc3Dao = SpringContextHolder.getBean(OaDoc3Dao.class);
	private static ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);
	private static OaDoc3RoutingService doc3Service = SpringContextHolder.getBean(OaDoc3RoutingService.class);
	
	/**
	 * 创建公文流转
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"oa/docroute/create"})
	public void test(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");

		
		
		
	}
	
	
	public ApiDoc3 ToApiDoc3(OaDoc3 doc)
	{
		ApiDoc3 apiDoc3 = new ApiDoc3();
		apiDoc3.setAttachLinks(doc.getAttachLinks());
		apiDoc3.setDocTitle(doc.getDocTitle());
		apiDoc3.setDRStage(doc.getDRStage());
		apiDoc3.setDueDate(doc.getDueDate());
		apiDoc3.setId(doc.getId());
		apiDoc3.setLeaderApproveDate(doc.getLeaderApproveDate());
		apiDoc3.setLeaderId(doc.getLeaderId());
		apiDoc3.setLeaderOption(doc.getLeaderOption());
		apiDoc3.setOfficeHeaderApproval(doc.isOfficeHeaderApproval());
		apiDoc3.setOfficeHeaderApproveDate(doc.getOfficeHeaderApproveDate());
		apiDoc3.setOfficeHeaderOption(doc.getOfficeHeaderOption());
		apiDoc3.setReviewersIDs(doc.getReviewersIDs());
	}

	public class ApiDoc3
	{
		private String id;
		// 发文的标题
		private String docTitle;
		// 到公文的链接，可能一次发布多个文件，用;分割
		private String attachLinks;
		// 办公室领导意见
		private String officeHeaderOption;
		private boolean officeHeaderApproval;
		private Date officeHeaderApproveDate;
		// 到期日期
		private Date dueDate;

		// 审阅领导
		private String leaderId;
		private String leaderOption;
		private Date leaderApproveDate;
		// 公文审阅人ID列表，用;分割
		private String reviewersIDs;
		private String reviewersIDs1;
		private String DRStage;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getDocTitle() {
			return docTitle;
		}
		public void setDocTitle(String docTitle) {
			this.docTitle = docTitle;
		}
		public String getAttachLinks() {
			return attachLinks;
		}
		public void setAttachLinks(String attachLinks) {
			this.attachLinks = attachLinks;
		}
		public String getOfficeHeaderOption() {
			return officeHeaderOption;
		}
		public void setOfficeHeaderOption(String officeHeaderOption) {
			this.officeHeaderOption = officeHeaderOption;
		}
		public boolean isOfficeHeaderApproval() {
			return officeHeaderApproval;
		}
		public void setOfficeHeaderApproval(boolean officeHeaderApproval) {
			this.officeHeaderApproval = officeHeaderApproval;
		}
		public Date getOfficeHeaderApproveDate() {
			return officeHeaderApproveDate;
		}
		public void setOfficeHeaderApproveDate(Date officeHeaderApproveDate) {
			this.officeHeaderApproveDate = officeHeaderApproveDate;
		}
		public Date getDueDate() {
			return dueDate;
		}
		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		public String getLeaderId() {
			return leaderId;
		}
		public void setLeaderId(String leaderId) {
			this.leaderId = leaderId;
		}
		public String getLeaderOption() {
			return leaderOption;
		}
		public void setLeaderOption(String leaderOption) {
			this.leaderOption = leaderOption;
		}
		public Date getLeaderApproveDate() {
			return leaderApproveDate;
		}
		public void setLeaderApproveDate(Date leaderApproveDate) {
			this.leaderApproveDate = leaderApproveDate;
		}
		public String getReviewersIDs() {
			return reviewersIDs;
		}
		public void setReviewersIDs(String reviewersIDs) {
			this.reviewersIDs = reviewersIDs;
		}
		public String getReviewersIDs1() {
			return reviewersIDs1;
		}
		public void setReviewersIDs1(String reviewersIDs1) {
			this.reviewersIDs1 = reviewersIDs1;
		}
		public String getDRStage() {
			return DRStage;
		}
		public void setDRStage(String dRStage) {
			DRStage = dRStage;
		}
		
	}
}