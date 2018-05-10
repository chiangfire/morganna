package com.firecode.morganna.config;

import java.io.UnsupportedEncodingException;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import com.firecode.kabouros.security.DefaultReactiveAuthorizationManager;
import com.firecode.kabouros.security.SecurityAuthenticationHandler;
import com.firecode.kabouros.security.config.ServerHttp2SecurityConfiguration;
import com.firecode.kabouros.security.support.ServerHttp2Security;
import com.firecode.morganna.config.properties.AntplatformProperties;
import com.firecode.morganna.config.properties.MorgannaPropertis;
import com.firecode.morganna.security.AlipayAuthenticationDispatcherHandler;
import com.firecode.morganna.security.AlipayUserReactiveAuthenticationManager;
import com.firecode.morganna.security.AlipayLoginParamConverter;
import com.firecode.morganna.service.AlipayEmpowerService;

import lombok.AllArgsConstructor;

@EnableWebFluxSecurity
@ImportAutoConfiguration({ServerHttp2SecurityConfiguration.class})
@EnableConfigurationProperties({ MorgannaPropertis.class, AntplatformProperties.class })
@AllArgsConstructor
public class MorgannaSecurityConfiguration {
	
	private final MorgannaPropertis morgannaPropertis;
	private final AntplatformProperties antplatformProperties;
	private final AlipayEmpowerService alipayEmpowerService;
	
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttp2Security http) throws UnsupportedEncodingException {
		SecurityAuthenticationHandler handler = new AlipayAuthenticationDispatcherHandler(antplatformProperties,morgannaPropertis);
		// 拦截路径
		ServerWebExchangeMatcher pathMatchers = ServerWebExchangeMatchers.pathMatchers(morgannaPropertis.getLoginLocation());
		//anyExchange()                      //所有权限
		//authorizeExchange()                //需要授权
		//authenticated()                    //允许认证过的用户访问
		//denyAll()                          //无条件拒绝所有访问
		//permitAll()                        //全部允许
		return http.authenticationManager(new AlipayUserReactiveAuthenticationManager(alipayEmpowerService))
			       .authorizeExchange()
			       //白名单
			       .pathMatchers(morgannaPropertis.getWhitelists().toArray(new String[morgannaPropertis.getWhitelists().size()]))
			       .permitAll()/*.matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()*/
			       .anyExchange().access(new DefaultReactiveAuthorizationManager())
			   .and()
				   .logout().disable()
			       .httpBasic().disable()
			       .csrf().disable()
			       .logout().disable()
			       .formLogin()
			           .authenticationEntryPoint(handler)
			           .requiresAuthenticationMatcher(pathMatchers)
			           .authenticationFailureHandler(handler)
			           .authenticationSuccessHandler(handler)
			           .authenticationConverter(new AlipayLoginParamConverter())
			   .and()
			   .build();
	    	
	}

}
