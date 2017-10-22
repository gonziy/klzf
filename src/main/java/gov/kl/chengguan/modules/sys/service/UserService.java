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
import gov.kl.chengguan.common.utils.StringUtils;
import gov.kl.chengguan.common.web.Servlets;
import gov.kl.chengguan.modules.sys.dao.MenuDao;
import gov.kl.chengguan.modules.sys.dao.OfficeDao;
import gov.kl.chengguan.modules.sys.dao.RoleDao;
import gov.kl.chengguan.modules.sys.dao.UserDao;
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
	

	@Autowired
	private UserDao userDao;
	@Autowired
	private OfficeDao officeDao;

	
	public List<User> getdeptLeaderUser(String user_id) {
		List<User> deptUsers = new ArrayList<User>();
		User userWhere = new User();
		Office officeWhere = new Office();
		officeWhere.setId("882b7895d2214c0f96fc4622f6f32147");
		userWhere.setOffice(officeWhere);
	    List<User> fujuList = userDao.findUserByOfficeId(userWhere);
	    for (User u : fujuList) {
			deptUsers.add(u);
		}
	    
	    User user = userDao.get(user_id);
	    User userWhere2 = new User();
		Office officeWhere2 = new Office();
		String officeidString = officeDao.get(user.getOffice().getId()).getParentId();
		officeWhere2.setId("officeidString");
		userWhere2.setOffice(officeWhere2);
		List<User> tmpList = userDao.findUserByOfficeId(userWhere2);
		for (User u : tmpList) {
			if(u.getOffice().getName().equals("队长"))
			{
				deptUsers.add(u);
			}
		}
	    return deptUsers;
		
	}
	public User getMainLeaderUser(String id) {
		return UserUtils.get(id);
	}
	public User getInstitutionUser(String id) {
		return UserUtils.get(id);
	}
	public User getMgtCenterUser(String id) {
		return UserUtils.get(id);
	}
	public List<User> getAssigneeUsers(String id) {
		return null;
	}
	



}
