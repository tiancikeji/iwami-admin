package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.exception.VerifyCodeMismatchException;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class UserAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private UserBiz userBiz;

	@AjaxMethod(path = "userinfo.ajax")
	public Map<Object, Object> getUserinfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			User user = null;
			
			if(params.containsKey("userid")){
				long userid = NumberUtils.toLong(params.get("userid"));
				user = userBiz.getUserById(userid);
			}
			
			if(user != null){
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				result.put("userid", user.getId());
				result.put("username", StringUtils.trimToEmpty(user.getName()));
				result.put("uuid", StringUtils.trimToEmpty(user.getUuid()));
				result.put("cellPhone", toStringL(user.getCellPhone()));
				result.put("age", user.getAge());
				result.put("job", StringUtils.trimToEmpty(user.getJob()));
				result.put("address", StringUtils.trimToEmpty(user.getAddress()));
				result.put("currentPrize", user.getCurrentPrize());
				result.put("exchangePrize", user.getExchangePrize());
				result.put("lastCellPhone1", toStringL(user.getLastCellPhone1()));
				result.put("lastAlipayAccount", StringUtils.trimToEmpty(user.getLastAlipayAccount()));
				result.put("lastBankAccount", StringUtils.trimToEmpty(user.getLastBankAccount()));
				result.put("lastBankNo", toStringL(user.getLastBankNo()));
				result.put("lastBankName", StringUtils.trimToEmpty(user.getLastBankName()));
				result.put("lastAddress", StringUtils.trimToEmpty(user.getLastAddres()));
				result.put("lastCellPhone2", toStringL(user.getLastCellPhone2()));
				result.put("lastName", StringUtils.trimToEmpty(user.getLastName()));
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_USERINFO_USERID);
				result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_USERINFO_USERID));
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in Userinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private String toStringL(long cellPhone) {
		if(cellPhone > 0)
			return "" + cellPhone;
		return StringUtils.EMPTY;
	}

	@AjaxMethod(path = "sendverifycode.ajax")
	public Map<Object, Object> sendVerifyCode(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			long cellPhone = 0;
			if(params.containsKey("cellPhone")){
				cellPhone = NumberUtils.toLong(params.get("cellPhone"));
			}
			
			if(IWamiUtils.validatePhone("" + cellPhone)){
				if(userBiz.sendVerifyCode(cellPhone)){
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_SEND_VERIFY_CODE_CELLPHONE);
				result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_SEND_VERIFY_CODE_CELLPHONE));
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in sendVerifyCode", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "register.ajax")
	public Map<Object, Object> register(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			String uuid = StringUtils.EMPTY;
			String name = StringUtils.EMPTY;
			long cellPhone = 0;
			String code = StringUtils.EMPTY;
			
			if(params.containsKey("cellPhone")){
				cellPhone = NumberUtils.toLong(params.get("cellPhone"));
			}
			
			if(IWamiUtils.validatePhone("" + cellPhone)){
				if(params.containsKey("name")){
					name = StringUtils.trimToEmpty(params.get("name"));
				}
				
				if(StringUtils.isNotBlank(name)){
					if(params.containsKey("uuid")){
						uuid = StringUtils.trimToEmpty(params.get("uuid"));
					}
					
					if(StringUtils.isNotBlank(uuid)){
						if(params.containsKey("code")){
							code = StringUtils.trimToEmpty(params.get("code"));
						}
						
						if(StringUtils.isNotBlank(code)){
							User user = userBiz.register(uuid, name, cellPhone, StringUtils.trimToEmpty(params.get("alias")), code);
							if(user != null){
								result.put("userid", user.getId());
								result.put("username", user.getName());
								result.put("uuid", user.getUuid());
								result.put("cellPhone", user.getCellPhone());
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
							} else
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
						} else{
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_REGISTER_CODE);
							result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_REGISTER_CODE));
						}
					} else{
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_REGISTER_UUID);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_REGISTER_UUID));
					}
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_REGISTER_NAME);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_REGISTER_NAME));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_REGISTER_CELLPHONE);
				result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_REGISTER_CELLPHONE));
			}
		} catch(VerifyCodeMismatchException e){
			if(logger.isErrorEnabled())
				logger.error("Exception in verify code match", e);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_REGISTER_CODE);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_REGISTER_CODE));
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in register", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "modifyuser.ajax")
	public Map<Object, Object> modifyUserInfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			long userid = 0;
			int age = 0;
			int gender = -1;
			String job = StringUtils.EMPTY;
			String address = StringUtils.EMPTY;
			User user = null;
			
			if(params.containsKey("userid")){
				userid = NumberUtils.toLong(params.get("userid"));
				user = userBiz.getUserById(userid);
			}
			
			if(user != null){
				if(params.containsKey("age")){
					age = NumberUtils.toInt(params.get("age"));
				}
				
				if(age > 0 && age < 100){
					if(params.containsKey("gender")){
						gender =  NumberUtils.toInt(params.get("gender"));
					}
					
					if(gender == 0 || gender == 1){
						if(params.containsKey("job")){
							job = StringUtils.trimToEmpty(params.get("job"));
						}
						
						if(StringUtils.isNotBlank(job)){
							if(params.containsKey("address")){
								address = StringUtils.trimToEmpty(params.get("address"));
							}
							
							if(StringUtils.isNotBlank(address)){
								user.setAge(age);
								user.setGender(gender);
								user.setJob(job);
								user.setAddress(address);
								if(userBiz.modifyUserInfo4Register(user))
									result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
								else
									result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
							} else{
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_ADDRESS);
								result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_ADDRESS));
							}
						} else{
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_JOB);
							result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_JOB));
						}
					} else{
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_GENDER);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_GENDER));
					}
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_AGE);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_AGE));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_USERID);
				result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_MODIFY_USERINFO_USERID));
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in register", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	public UserBiz getUserBiz() {
		return userBiz;
	}

	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}

}
