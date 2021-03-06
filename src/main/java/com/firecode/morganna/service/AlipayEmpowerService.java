package com.firecode.morganna.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.firecode.morganna.domain.alipay.AlipayEmpowerRecord;
import com.firecode.morganna.domain.alipay.AlipayUser;
import com.firecode.morganna.security.AlipayAuthInfo;

import reactor.core.publisher.Mono;

/**
 * 支付宝用户授权服务
 * @author JIANG
 *
 */
public interface AlipayEmpowerService {
	
	/**
	 * 调用支付宝接口加载支付宝用户信息并更新到数据库
	 * @param accessToken   
	 * @return
	 * @throws AlipayApiException
	 */
	public Mono<AlipayUser> loadAlipayUser(String authCode) throws AlipayApiException;
	/**
	 * 查询支付宝用户认证信息
	 * 注意每个authCode只能使用一次
	 * @param authCode
	 * @throws AlipayApiException
	 * 注意：authCode只能使用一次
	 */
	public Mono<AlipayAuthInfo> queryAlipayAuthInfo(String authCode) throws AlipayApiException;
	
	/**
	 * 使用auth_code换取用户access_token，user_id
	 * 注意每个authCode只能使用一次
	 * @param authCode
	 * @param isRefresh  是不是刷新access_token
	 * @throws AlipayApiException
	 * 注意：authCode只能使用一次
	 */
	public AlipaySystemOauthTokenResponse exchangeAccessToken(String authCode,boolean isRefresh)throws AlipayApiException;
	/**
	 * 使用app_auth_code换取用户app_auth_token
	 * @param appAuthCode 只能使用一次
	 * @param isRefresh   是不是刷新app_auth_token
	 * @return
	 * @throws AlipayApiException
	 * 注意：appAuthCode 只能使用一次
	 * app_auth_token有效期为365天，刷新后重新计时
	 */
	public Mono<AlipayEmpowerRecord> exchangeAlipayEmpowerRecord(String appAuthCode,boolean isRefresh);
	/**
	 * 查询支付宝用户授权记录
	 * @param userId          支付宝用户ID
	 * @return
	 */
	public Mono<AlipayEmpowerRecord> queryUserEmpowerRecord(String userId);
	
	/**
	 * 查询支付宝用户信息
	 * @param userId          支付宝用户ID
	 * @return
	 */
	public Mono<AlipayUser> queryAlipayUser(String userId);
	
}
