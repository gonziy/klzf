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

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.KeyValueForMsg;
import com.baidu.yun.push.model.MsgStatInfo;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.baidu.yun.push.model.QueryStatisticMsgRequest;
import com.baidu.yun.push.model.QueryStatisticMsgResponse;



@Service
@Transactional(readOnly = true)
public class PushService extends BaseService{
	

	private static String apiKey = "GRXsGtkIyZ0KPn9coWEmDu1j";
	private static String secretKey = "LtSSmxavNxbXfqj4XeY8hmYtTXUhH36I";
	
	
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static BaseUserDao baseUserDao = SpringContextHolder.getBean(BaseUserDao.class);
	
	

	public PushKeyPair pair;
	public BaiduPushClient pushClient;
	
	public void PushToUser(String userid,String msg) {
		if(userid!=null && !userid.isEmpty()){
			BaseUser baseUser = baseUserDao.get(userid);
			if(baseUser!=null){
				String channelId= baseUser.getBaiduPushChannelId();
				if(channelId!=null && ! channelId.isEmpty()){
					if(pair==null){
						pair = new PushKeyPair(apiKey, secretKey);
					}
					if(pushClient==null){
						pushClient = new BaiduPushClient(pair,
								BaiduPushConstants.CHANNEL_REST_URL);
					}
					
					// 3. 注册YunLogHandler，获取本次请求的交互信息
			        pushClient.setChannelLogHandler (new YunLogHandler () {
			            @Override
			            public void onHandle (YunLogEvent event) {
			                System.out.println(event.getMessage());
			            }
			        });
			        
			        try {
			            // 4. 设置请求参数，创建请求实例
			                PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
			                    addChannelId(channelId).
			                    addMsgExpires(new Integer(3600 * 24)).   //设置消息的有效时间,单位秒,默认3600*5.
			                    addMessageType(1).              //设置消息类型,0表示透传消息,1表示通知,默认为0.
			                    addMessage(msg.isEmpty()?"有一项任务需要急需处理":msg).
			                    addDeviceType(3);      //设置设备类型，deviceType => 1 for web, 2 for pc, 
			                                           //3 for android, 4 for ios, 5 for wp.
			            // 5. 执行Http请求
			                PushMsgToSingleDeviceResponse response = pushClient.
			                    pushMsgToSingleDevice(request);
			            // 6. Http请求返回值解析
			                System.out.println("msgId: " + response.getMsgId()
			                        + ",sendTime: " + response.getSendTime());
			            } catch (PushClientException e) {
			                //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
			                //'true' 表示抛出, 'false' 表示捕获。
			                if (BaiduPushConstants.ERROROPTTYPE)
								try {
									{ 

									}
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							else {
			                    e.printStackTrace();
			                }
			            } catch (PushServerException e) {
			                if (BaiduPushConstants.ERROROPTTYPE)
								try {
									{

									}
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							else {
			                    System.out.println(String.format(
			                            "requestId: %d, errorCode: %d, errorMsg: %s",
			                            e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			                }
			            }
			        
					
					
				}				
			}
		}
	}
}

