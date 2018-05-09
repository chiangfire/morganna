package com.firecode.morganna.security.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

import com.firecode.kabouros.common.domain.ApplicationProperties;
import com.firecode.kabouros.common.keygen.IPIdGenerator;
import com.firecode.kabouros.security.SecurityAuthenticationHandler;
import com.firecode.morganna.config.alipay.AntplatformProperties;

import io.netty.util.CharsetUtil;
import reactor.core.publisher.Mono;

/**
 * 支付宝认证过程业务流转处理器
 * @author JIANG
 *
 */
public class AlipaySecurityAuthenticationHandler implements SecurityAuthenticationHandler {
	
	private Log logger = LogFactory.getLog(AlipaySecurityAuthenticationHandler.class);
	
	public static final String ALIPAY_AUTH_PARAM_STATE = "alipay_auth_param_state";
	public static final String ALIPAY_PARAM_APP_ID = "app_id";
	public static final String ALIPAY_PARAM_SCOPE = "scope";
	public static final String ALIPAY_PARAM_REDIRECT_URI = "redirect_uri";
	public static final String ALIPAY_PARAM_STATE = "state";
	public static final String ALIPAY_PARAM_AUTH_CODE = "auth_code";
	
    //重定向策略
	private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
    //请求缓存
	private ServerRequestCache requestCache = new WebSessionServerRequestCache();
	//认证成功后跳转地址
	private final URI indexLocation;
	//认证失败跳转地址
	private final URI authErrorLocation;
	//支付宝用户登陆认证地址
	private final String authLocation;
	//本系统支付宝用户登陆地址
	private final String loginAddress;
	
	public AlipaySecurityAuthenticationHandler(AntplatformProperties antplatformProperties,ApplicationProperties applicationProperties) throws UnsupportedEncodingException {
		Assert.notNull(antplatformProperties, "antplatformProperties cannot be null");
		Assert.notNull(applicationProperties, "applicationProperties cannot be null");
		this.loginAddress = URLEncoder.encode(String.join("", applicationProperties.getHost(),applicationProperties.getLoginLocation()), CharsetUtil.UTF_8.name());
		logger.info(String.join("：","支付宝用户登陆地址",loginAddress));
		Map<String,String> params = new HashMap<String,String>(3);
		params.put(ALIPAY_PARAM_APP_ID, antplatformProperties.getAppId());
		params.put(ALIPAY_PARAM_SCOPE, antplatformProperties.getScope());
		params.put(ALIPAY_PARAM_REDIRECT_URI, loginAddress);
		this.authLocation = String.join("",antplatformProperties.getAppAuthorize() ,mosaicParams(params));
		logger.info(String.join("：", "支付宝用户登陆认证地址",authLocation));
		this.indexLocation = URI.create(applicationProperties.getIndexLocation());
		logger.info(String.join("：", "支付宝用户登陆认证完成后跳转地址",indexLocation.toString()));
		this.authErrorLocation = URI.create(applicationProperties.getAuthErrorLocation());
		logger.info(String.join("：", "支付宝认证失败跳转地址",this.authErrorLocation .toString()));
	}

	
	/**
	 * 认证
	 */
	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {
		String state = String.join("", IPIdGenerator.getInstance().generate().toString());
		URI location = URI.create(String.join("", authLocation,"&",ALIPAY_PARAM_STATE,"=",state));
		return exchange.getSession()
				       .map(WebSession::getAttributes)
				       .doOnNext(attrs -> attrs.put(ALIPAY_AUTH_PARAM_STATE, state))
				       .and(this.requestCache.saveRequest(exchange)
				    		                 .then(this.redirectStrategy.sendRedirect(exchange, location)));
	}
	
	/**
	 * 认证失败
	 */
	@Override
	public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
		ServerWebExchange exchange = webFilterExchange.getExchange();
		logger.error(exception);
		return exchange.getSession()
		           .map(WebSession::getAttributes)
		           .doOnNext(attrs -> attrs.remove(ALIPAY_AUTH_PARAM_STATE))
		           .and(this.redirectStrategy.sendRedirect(exchange, this.authErrorLocation));
	}

	/**
	 * 认证成功
	 */
	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		ServerWebExchange exchange = webFilterExchange.getExchange();
		return exchange.getSession()
			           .map(WebSession::getAttributes)
			           .doOnNext(attrs -> attrs.remove(ALIPAY_AUTH_PARAM_STATE))
			           .and(this.requestCache.getRedirectUri(exchange)
		                                     .defaultIfEmpty(this.indexLocation)
		                                     .flatMap(location -> this.redirectStrategy.sendRedirect(exchange, location)));
	}
	
	public void setRequestCache(ServerRequestCache requestCache) {
		Assert.notNull(requestCache, "requestCache cannot be null");
		this.requestCache = requestCache;
	}
	
	public void setRedirectStrategy(ServerRedirectStrategy redirectStrategy) {
		Assert.notNull(redirectStrategy, "redirectStrategy cannot be null");
		this.redirectStrategy = redirectStrategy;
	}
	/**
	 * 拼接GET请求参数
	 * @param params
	 * @return
	 */
	private String mosaicParams(Map<String,String> params){
		StringBuilder sb = new StringBuilder("?");
		for(String key:params.keySet()){
			sb.append("&").append(key).append("=").append(params.get(key));
		}
		sb.deleteCharAt(1);
		return sb.toString();
	}
}
