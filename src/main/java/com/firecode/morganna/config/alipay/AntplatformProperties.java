package com.firecode.morganna.config.alipay;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 蚂蚁平台基础属性
 * @author JIANG
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@ConfigurationProperties(prefix="morganna.antplatform")
public class AntplatformProperties {
	private String url;
	private String appId;
	private String privateKey;
	private String alipayPublicKey;
	private String format;
	private String charset;
	private String signType;
	/**
	 * 支付宝第三方应用授权回调地址
	 */
	private String appAuth;
	/**
	 * 支付宝用户登陆授权回调地址
	 */
	private String appAuthorize;
	private String scope;
	
}
