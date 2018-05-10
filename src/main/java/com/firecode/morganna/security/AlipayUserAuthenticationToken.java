package com.firecode.morganna.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AlipayUserAuthenticationToken extends AbstractAuthenticationToken{
	
	private static final long serialVersionUID = 5947624914740362730L;
	
	private AlipayAuthInfo alipayAuthInfo;
	
	private String accessToken;
	
	

	public AlipayUserAuthenticationToken(AlipayAuthInfo alipayAuthInfo, String accessToken) {
		super(null);
		this.alipayAuthInfo = alipayAuthInfo;
		this.accessToken = accessToken;
		setAuthenticated(false);
	}
	

	public AlipayUserAuthenticationToken(AlipayAuthInfo alipayAuthInfo, String accessToken,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.alipayAuthInfo = alipayAuthInfo;
		this.accessToken = accessToken;
		// must use super, as we override
		super.setAuthenticated(true); 
	}


	public Object getCredentials() {
		return this.accessToken;
	}

	public Object getPrincipal() {
		return this.alipayAuthInfo;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.accessToken = null;
	}

}
