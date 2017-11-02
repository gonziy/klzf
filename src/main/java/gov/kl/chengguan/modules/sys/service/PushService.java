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
public class PushService extends BaseService{
	private static String apiKey = "GRXsGtkIyZ0KPn9coWEmDu1j";
	private static String secretKey = "LtSSmxavNxbXfqj4XeY8hmYtTXUhH36I";
	
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static BaseUserDao baseUserDao = SpringContextHolder.getBean(BaseUserDao.class);
	
	private boolean enabled = true;	
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void PushToUser(String userid,String msg) {
	}
}

