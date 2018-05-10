package com.firecode.morganna.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import com.alipay.api.AlipayApiException;
import com.firecode.morganna.service.AlipayEmpowerService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
/**
 * 支付宝授权登陆管理器
 * @author JIANG
 *
 */
public class AlipayUserReactiveAuthenticationManager implements ReactiveAuthenticationManager {
	
	private AlipayEmpowerService alipayEmpowerService;

	public AlipayUserReactiveAuthenticationManager(AlipayEmpowerService alipayEmpowerService) {
		Assert.notNull(alipayEmpowerService, "alipayEmpowerService cannot be null");
		this.alipayEmpowerService = alipayEmpowerService;
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		final String authCode = authentication.getName();
		try {
			//查询支付宝用户认证信息
			return this.alipayEmpowerService.queryAlipayAuthInfo(authCode)
				                            .publishOn(Schedulers.parallel())
				                            .map(alipayAuthInfo -> alipayUserAuthenticationToken(alipayAuthInfo));
		} catch (AlipayApiException e) {
			return Mono.error(new AuthenticationServiceException("查询支付宝用户登陆信息 或 第三方应用授权记录失败！",e));
		}
	}
	
	private Authentication alipayUserAuthenticationToken(AlipayAuthInfo alipayAuthInfo){
		String roleName = "USER";
		List<GrantedAuthority> authorities = new ArrayList<>();
		//Assert.isTrue(!roleName.startsWith("ROLE_"), roleName + " cannot start with ROLE_ (it is automatically added)");
		authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
		return new AlipayUserAuthenticationToken(alipayAuthInfo,alipayAuthInfo.getAccessToken(),authorities);
	}

}
