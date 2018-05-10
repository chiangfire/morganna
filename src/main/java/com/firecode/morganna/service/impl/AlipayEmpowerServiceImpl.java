package com.firecode.morganna.service.impl;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayOpenAuthTokenAppQueryRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.firecode.morganna.common.AlipayApiExecuteException;
import com.firecode.morganna.domain.alipay.AlipayEmpowerRecord;
import com.firecode.morganna.domain.alipay.AlipayUser;
import com.firecode.morganna.repository.AlipayEmpowerRepository;
import com.firecode.morganna.repository.AlipayUserRepository;
import com.firecode.morganna.security.AlipayAuthInfo;
import com.firecode.morganna.service.AbstractAlipayService;
import com.firecode.morganna.service.AlipayEmpowerService;

import reactor.core.publisher.Mono;

/**
 * 支付宝用户授权服务
 * @author JIANG
 */
@Service("alipayEmpowerServiceImpl")
public class AlipayEmpowerServiceImpl extends AbstractAlipayService implements AlipayEmpowerService{
	
	private Log logger = LogFactory.getLog(AlipayEmpowerServiceImpl.class);
	
	@Autowired
	private AlipayUserRepository alipayUserRepository;
	@Autowired
	private AlipayEmpowerRepository alipayEmpowerRepository;

	/**
	 * 调用支付宝接口加载支付宝用户信息并更新到数据库
	 * @param accessToken   
	 * @return
	 * @throws AlipayApiException
	 */
	@Override
	public Mono<AlipayUser> loadAlipayUser(String accessToken) throws AlipayApiException{
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		AlipayUserInfoShareResponse response = alipayClient.execute(request, accessToken);
		if(response.isSuccess()){
			this.queryAlipayUser(response.getUserId()).flatMap(alipayUser -> {
				if(null == alipayUser){
					alipayUser = new AlipayUser();
				}
				BeanUtils.copyProperties(response, alipayUser);
				return alipayUserRepository.save(alipayUser);
			});
		}
		throw new AlipayApiException(response.getCode(),response.getMsg());
	}
	/**
	 * 使用auth_code换取用户access_token，user_id
	 * 注意每个authCode只能使用一次
	 * @param authCode
	 * @param isRefresh  是不是刷新access_token
	 * @throws AlipayApiException
	 * 注意：authCode只能使用一次
	 */
	@Override
	public AlipaySystemOauthTokenResponse exchangeAccessToken(String authCode,boolean isRefresh) throws AlipayApiException{
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		//值为authorization_code时，代表用authCode换取；值为refresh_token时，代表用refresh_token换取
		request.setGrantType(this.getGrantType(isRefresh));
		if(isRefresh){
			request.setRefreshToken(authCode);
		}else{
			request.setCode(authCode);
		}
		logger.info(String.join("", "正在",isRefresh ? "刷新" : "换取"," access_token，参数 authCode：",authCode));
		AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			logger.info(String.join("：", "成功换取 access_token",response.getAccessToken()));
			return response;
		}
		throw new AlipayApiException(response.getCode(),response.getMsg());
	}
	
	/**
	 * 查询支付宝用户认证信息
	 * 注意每个authCode只能使用一次
	 * @param authCode
	 * @throws AlipayApiException
	 * 注意：authCode只能使用一次
	 */
	@Override
	public Mono<AlipayAuthInfo> queryAlipayAuthInfo(String authCode)throws AlipayApiException{
		AlipaySystemOauthTokenResponse exchangeAccessToken = this.exchangeAccessToken(authCode, false);
		AlipayAuthInfo alipayAuthInfo = new AlipayAuthInfo();
		BeanUtils.copyProperties(exchangeAccessToken, alipayAuthInfo);
		//查询用户第三方应用授权记录
		return this.queryUserEmpowerRecord(alipayAuthInfo.getUserId()).flatMap(alipayEmpowerRecord -> {
			Date currentDate = new Date();
			//有授权记录并且没有过期
			if(null != alipayEmpowerRecord && alipayEmpowerRecord.getAuthEnd().after(currentDate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentDate);
				//预计提前15天刷新授权记录
				calendar.add(Calendar.DATE, +15);
				if(calendar.getTime().after(alipayEmpowerRecord.getAuthEnd())){
					//刷新授权记录
					return this.exchangeAlipayEmpowerRecord(alipayEmpowerRecord.getAppRefreshToken(), true).map(newEmpowerRecord ->{
						alipayAuthInfo.setAlipayEmpowerRecord(newEmpowerRecord);
						return alipayAuthInfo;
					});
				}
				alipayAuthInfo.setAlipayEmpowerRecord(alipayEmpowerRecord);
			}
			return Mono.just(alipayAuthInfo);
		}).defaultIfEmpty(alipayAuthInfo);
	}
	
	/**
	 * 使用app_auth_code换取用户app_auth_token
	 * @param appAuthCode 只能使用一次
	 * @param isRefresh   是不是刷新app_auth_token
	 * @return
	 * @throws AlipayApiException
	 * 注意：appAuthCode 只能使用一次
	 * app_auth_token有效期为365天，刷新后重新计时
	 */
	@Override
	public Mono<AlipayEmpowerRecord> exchangeAlipayEmpowerRecord(String appAuthCode,boolean isRefresh){
		return this.queryAppAuthToken(appAuthCode, isRefresh).flatMap(authTokenAppResponse -> {
			AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
			String param = String.join("", "{\"app_auth_token\":\"",authTokenAppResponse.getAppAuthToken(),"\"}");
			request.setBizContent(param);
			AlipayOpenAuthTokenAppQueryResponse response;
			logger.info(String.join("：", "正在调用支付宝接口查询第三方应用授权详情。。。 参数",param));
			try {
				response = alipayClient.execute(request);
				if(response.isSuccess()){
					return this.queryUserEmpowerRecord(response.getUserId()).flatMap(empowerRecord -> {
						if(null == empowerRecord){
							empowerRecord = new AlipayEmpowerRecord();
							empowerRecord.setCreateTime(new Date());
						}
						BeanUtils.copyProperties(response, empowerRecord);
						empowerRecord.setAppAuthToken(authTokenAppResponse.getAppAuthToken());
						empowerRecord.setAppRefreshToken(authTokenAppResponse.getAppRefreshToken());
						empowerRecord.setLastModifyTime(new Date());
						return alipayEmpowerRepository.save(empowerRecord);
					    /*logger.info(String.join("：", "第三方应用授权详情查询成功",empowerRecord.toString()));
						return empowerRecord;*/
					});
				}
				throw new AlipayApiExecuteException(response.getCode(),response.getMsg());
			} catch (AlipayApiException e) {
				throw new AlipayApiExecuteException(e.getErrCode(),e.getErrMsg());
			}
		});
	}
	/**
	 * 查询支付宝用户授权记录
	 * @param userId          支付宝用户ID
	 * @return
	 */
	@Override
	public Mono<AlipayEmpowerRecord> queryUserEmpowerRecord(String userId) {
		
		return alipayEmpowerRepository.findById(userId);
	}
	
	/**
	 * 查询支付宝用户信息
	 * @param userId          支付宝用户ID
	 * @return
	 */
	@Override
	public Mono<AlipayUser> queryAlipayUser(String userId) {
		
		return alipayUserRepository.findById(userId);
	}
	
	/**
	 * 查询支付宝用户 AppAuthToken
	 * @param appAuthCode
	 * @param isRefresh
	 * @return
	 * @throws AlipayApiException
	 */
	private Mono<AlipayOpenAuthTokenAppResponse> queryAppAuthToken(String appAuthCode,boolean isRefresh) {
		return Mono.create(monoSink -> {
			AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
			String param = String.join("", "{","\"grant_type\":\"",this.getGrantType(isRefresh),"\",",isRefresh ? "\"refresh_token\":\"" : "\"code\":\"",appAuthCode,"\"}");
			request.setBizContent(param);
			logger.info(String.join("：", "正在调用支付宝接口查询 app_auth_token。。。 参数",param));
			AlipayOpenAuthTokenAppResponse response;
			try {
				response = this.alipayClient.execute(request);
				if(response.isSuccess()){
					logger.info(String.join("：", "成功获取 app_auth_token",response.getAppAuthToken()));
					monoSink.success(response);
				}
				monoSink.error(new AlipayApiException(response.getCode(),response.getMsg()));
			} catch (AlipayApiException e) {
				monoSink.error(e);
			}
		});
	}
}
