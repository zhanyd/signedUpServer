package com.zhanyd.app.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhanyd.app.common.ApiResult;
import com.zhanyd.app.common.WeixinHelper;
import com.zhanyd.app.common.util.AesCbcUtil;
import com.zhanyd.app.common.util.HttpService;
import com.zhanyd.app.common.util.JwtUtils;
import com.zhanyd.app.model.ActivityInfo;
import com.zhanyd.app.model.UserInfo;
import com.zhanyd.app.service.ActivityService;
import com.zhanyd.app.service.UserService;


@RestController
@EnableAutoConfiguration
@RequestMapping("/activity")
public class ActivityController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserService userService;
	
	@Autowired
	ActivityService activityService;
	 
	 /**
     * 获取Openid和Session_key
     * @param code
     * @return
     */
    @RequestMapping("/getOpenidAndSession")
    public ApiResult getOpenidAndSession(String code,String nickName){
    	ApiResult apiResult = new ApiResult();
    	//获取Openid和Session_key
		String getUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WeixinHelper.APP_ID + "&secret=" + WeixinHelper.CORPSECRET + "&js_code=" + code + "&grant_type=authorization_code";
		String returnContent = HttpService.post(getUrl);
		logger.info("returnContent = " + returnContent);
		JSONObject jsonObject = JSONObject.parseObject(returnContent);
		String openid = jsonObject.getString("openid");
		String sessionKey = jsonObject.getString("session_key");
		
		//判断openid是否已经存在
		Map<String,String> param = new HashMap<String,String>();
		param.put("openid",openid);
        UserInfo userInfo = userService.selectByOpenid(openid);
        if(userInfo == null){
        	userInfo = new UserInfo();
        	userInfo.setOpenid(openid);
        	userInfo.setNickName(nickName);
        	//userInfo.setSessionKey(sessionKey);
        	userInfo.setCreateTime(new Date());
        	userService.insertSelective(userInfo);
        }else{
        	//userInfo.setSessionKey(sessionKey);
        	//userService.updateByPrimaryKeySelective(userInfo);
        }
        
        jsonObject.put("userId", userInfo.getId());
        return apiResult.success(jsonObject);
    }


    /**
     * 获取unionId
     * @param encryptedData
     * @param iv
     * @param code
     * @return
     */
    @RequestMapping("/getUnionId")
    public ApiResult getUnionId(String encryptedData, String iv, String code){
        ApiResult apiResult = new ApiResult();

        //获取Openid和Session_key
        String getUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WeixinHelper.APP_ID + "&secret=" + WeixinHelper.CORPSECRET + "&js_code=" + code + "&grant_type=authorization_code";
        String returnContent = HttpService.post(getUrl);
        logger.info("returnContent = " + returnContent);
        JSONObject jsonObject = JSONObject.parseObject(returnContent);
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        String uionId = jsonObject.getString("unionId");

        if(uionId == null){
            //对encryptedData加密数据进行AES解密
            try {
                String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
                System.out.println("result = " + result);
                if (null != result && result.length() > 0) {
                    jsonObject = JSONObject.parseObject(result);
                    uionId =  jsonObject.getString("unionId");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return apiResult.success(uionId);
    }
    
    
    /**
     * 新增或更新活动
     * @param request
     * @param activityInfo
     * @return
     */
    @RequestMapping("/updateActivity")
    public ApiResult<Integer> updateActivity(HttpServletRequest request, ActivityInfo activityInfo){
    	ApiResult<Integer> apiResult = new ApiResult<Integer>();
    	int count = 0;
    	String token = request.getHeader("Authorization");
    	Integer userId = JwtUtils.verifyJWT(token);
    	if(activityInfo.getId() == null) {
    		activityInfo.setCreateBy(userId);
    		activityInfo.setCreateTime(new Date());
    		count = activityService.insertSelective(activityInfo);
    	}else {
    		activityInfo.setUpdateBy(userId);
    		activityInfo.setUpdateTime(new Date());
    		count = activityService.updateByPrimaryKeySelective(activityInfo);
    	}
    	return apiResult.success(count);
    }
    
    /**
     * 获取活动详情
     * @param id
     * @return
     */
    @RequestMapping("/getActivityDetail")
    public ApiResult<ActivityInfo> getActivityDetail(Integer id){
    	ApiResult<ActivityInfo> apiResult = new ApiResult<ActivityInfo>();
    	ActivityInfo activityInfo = activityService.selectByPrimaryKey(id);
    	return apiResult.success(activityInfo);
    }
}
