/**
 * author Gonziy
 */
package gov.kl.chengguan.modules.sys.dao;

import java.util.List;

import gov.kl.chengguan.common.persistence.CrudDao;
import gov.kl.chengguan.common.persistence.annotation.MyBatisDao;
import gov.kl.chengguan.modules.sys.entity.BaseUser;
import gov.kl.chengguan.modules.sys.entity.User;

/**
 * 用户DAO接口
 */
@MyBatisDao
public interface BaseUserDao extends CrudDao<BaseUser> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public BaseUser getByLoginName(BaseUser user);
	
	public BaseUser getByLoginNameAndPassword(BaseUser user);
	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<BaseUser> findUserByOfficeId(BaseUser user);
	
	/**
	 * 查询全部用户
	 * @return
	 */
	public List<BaseUser> findAllList(BaseUser user);
	/**
	 * 根据条件分页查询用户
	 * @return
	 */
	public List<BaseUser> findList(BaseUser user);
	

	

}
