/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.sys.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.session.Session;
import org.junit.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.kl.chengguan.common.config.Global;
import gov.kl.chengguan.common.persistence.Page;
import gov.kl.chengguan.common.security.Digests;
import gov.kl.chengguan.common.security.shiro.session.SessionDAO;
import gov.kl.chengguan.common.service.BaseService;
import gov.kl.chengguan.common.service.ServiceException;
import gov.kl.chengguan.common.utils.CacheUtils;
import gov.kl.chengguan.common.utils.Encodes;
import gov.kl.chengguan.common.utils.SpringContextHolder;
import gov.kl.chengguan.common.utils.StringUtils;
import gov.kl.chengguan.common.web.Servlets;
import gov.kl.chengguan.modules.sys.dao.AreaDao;
import gov.kl.chengguan.modules.sys.dao.BaseUserDao;
import gov.kl.chengguan.modules.sys.dao.MenuDao;
import gov.kl.chengguan.modules.sys.dao.OfficeDao;
import gov.kl.chengguan.modules.sys.dao.RoleDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.Menu;
import gov.kl.chengguan.modules.sys.entity.Office;
import gov.kl.chengguan.modules.sys.entity.Role;
import gov.kl.chengguan.modules.sys.entity.User;
import gov.kl.chengguan.modules.sys.security.SystemAuthorizingRealm;
import gov.kl.chengguan.modules.sys.utils.LogUtils;
import gov.kl.chengguan.modules.sys.utils.UserUtils;


@Service
@Transactional(readOnly = true)
public class UserService extends BaseService{
	

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static BaseUserDao baseUserDao = SpringContextHolder.getBean(BaseUserDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	/**
	 * 副局长
	 * @return
	 */
	public List<BaseUser> getDeptLeaderUser() {
		List<BaseUser> deptUsers = new ArrayList<BaseUser>();
		BaseUser userWhere = new BaseUser();
		Office officeWhere = new Office();
		officeWhere.setId("882b7895d2214c0f96fc4622f6f32147");
		userWhere.setOffice(officeWhere);
		deptUsers =  UserUtils.getBaseAllList(userWhere);
	    return deptUsers;		
	}
	/**
	 * 局长
	 * @return
	 */
	public List<BaseUser> getMainLeaderUser() {
		List<BaseUser> mainUsers = new ArrayList<BaseUser>();
		BaseUser userWhere = new BaseUser();
		Office officeWhere = new Office();
		officeWhere.setId("102");
		userWhere.setOffice(officeWhere);
		mainUsers =  UserUtils.getBaseAllList(userWhere);
	    return mainUsers;
	}
	/**
	 * 根据本人id获取本队 中队长/队长 列表
	 * @param id  本人id
	 * @param officeName  填写：中队长/队长
	 * @return
	 */
	public List<BaseUser> getInstitutionUser(String id, String officeName) {
		List<BaseUser> instUsers = new ArrayList<BaseUser>();
		BaseUser mine = UserUtils.getBaseById(id);
		if(mine != null){
			
			Office officeWhere = new Office();
			officeWhere.setId(mine.getOffice().getId());
			Office myOffice = officeDao.get(officeWhere);
			Office parentOffice = myOffice.getParent();
			if(parentOffice!=null){
				BaseUser userWhere2 = new BaseUser();
				userWhere2.setOffice(parentOffice);
				List<BaseUser> tmpUsers  = baseUserDao.findAllList(userWhere2);
				for (BaseUser baseUser : tmpUsers) {
					if(baseUser.getOffice().getName().equals(officeName)){
						instUsers.add(baseUser);
					}
				}
			}
		}
		return instUsers;
		
	}
	/**
	 * 案管中心
	 * @return
	 */
	public List<BaseUser> getMgtCenterUser() {
		List<BaseUser> mgtUsers = new ArrayList<BaseUser>();
		BaseUser userWhere = new BaseUser();
		Office officeWhere = new Office();
		officeWhere.setId("1432d48eadd246258160d6fa6e36eb2a");
		userWhere.setOffice(officeWhere);
		mgtUsers =  UserUtils.getBaseAllList(userWhere);
	    return mgtUsers;
	}
	/**
	 * 获取本队其他队员
	 * @param id 本人的id
	 * @return
	 */
	public List<BaseUser> getAssigneeUsers(String id) {
		BaseUser mine = UserUtils.getBaseById(id);
		List<BaseUser> tmpAssigneeUsers = new ArrayList<BaseUser>();
		List<BaseUser> assigneeUsers = new ArrayList<BaseUser>();
		if(mine!=null){
			BaseUser userWhere = new BaseUser();
			userWhere.setOffice(mine.getOffice());
			tmpAssigneeUsers = UserUtils.getBaseAllList(userWhere);
			for (BaseUser baseUser : tmpAssigneeUsers) {
				if(!baseUser.getId().equals(id))
					assigneeUsers.add(baseUser);
			}
		}
		
		return assigneeUsers;
	}
	



}
