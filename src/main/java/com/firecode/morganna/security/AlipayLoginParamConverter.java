package com.firecode.morganna.security;

import static com.firecode.morganna.security.AlipayAuthenticationDispatcherHandler.ALIPAY_AUTH_PARAM_STATE;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
/**
 * 支付宝认证参数转换器
 * @author JIANG
 *
 */
public class AlipayLoginParamConverter implements Function<ServerWebExchange, Mono<Authentication>>{
	

	@Override
	public Mono<Authentication> apply(ServerWebExchange exchange) {
		MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
		String authCode = queryParams.getFirst(AlipayAuthenticationDispatcherHandler.ALIPAY_PARAM_AUTH_CODE);
		String state = queryParams.getFirst(AlipayAuthenticationDispatcherHandler.ALIPAY_PARAM_STATE);
		return exchange.getSession()
				       .map(WebSession::getAttributes)
				       .flatMap(attrs -> authenticationParam(attrs,authCode,state));
	}
	/**
	 * @param attrs
	 * @param authCode
	 * @param state
	 * @return
	 */
	private Mono<Authentication> authenticationParam(Map<String,Object> attrs,String authCode,String state){
		//如果当前请求不是从支付宝重定向过来，直接抛出异常
 	    if(StringUtils.isEmpty(authCode)||StringUtils.isEmpty(state)||!state.equals(attrs.get(ALIPAY_AUTH_PARAM_STATE))){
		    return Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials")));
	    }
	    return Mono.just(new UsernamePasswordAuthenticationToken(authCode, null));
	}

}
