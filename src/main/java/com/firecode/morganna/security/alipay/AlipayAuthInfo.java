package com.firecode.morganna.security.alipay;

import com.firecode.morganna.domain.alipay.AlipayEmpowerRecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付宝认证信息
 * @author JIANG
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlipayAuthInfo {
	
	/** 
	 * 支付宝用户 user_id
	 */
	private String userId;
	
	/** 
	 * 访问令牌。通过该令牌调用需要授权类接口
	 */
	private String accessToken;

	/** 
	 * 令牌类型，permanent表示返回的access_token和refresh_token永久有效，非永久令牌不返回该字段
	 */
	private String authTokenType;
	/** 
	 * 访问令牌的有效时间，单位是秒。
	 */
	private String expiresIn;

	/** 
	 * 刷新令牌的有效时间，单位是秒。
	 */
	private String reExpiresIn;

	/** 
	 * 刷新令牌。通过该令牌可以刷新access_token
	 */
	private String refreshToken;
	/**
	 * 第三方应用授权记录
	 */
	private AlipayEmpowerRecord alipayEmpowerRecord;
}
