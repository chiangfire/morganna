package com.firecode.morganna.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.firecode.morganna.config.properties.AntplatformProperties;

public abstract class AbstractAlipayService implements InitializingBean{
	
	@Autowired
	protected AntplatformProperties antplatformProperties;
	
	protected String authorization_code = "authorization_code";
	
	protected AlipayClient alipayClient;

	public void afterPropertiesSet() throws Exception{
		this.alipayClient = new DefaultAlipayClient(antplatformProperties.getUrl(), 
				                                    antplatformProperties.getAppId(), 
				                                    antplatformProperties.getPrivateKey(),
				                                    antplatformProperties.getFormat() ,
				                                    antplatformProperties.getCharset(), 
				                                    antplatformProperties.getAlipayPublicKey(),
				                                    antplatformProperties.getSignType());
	}
	
	
	protected void setAntplatformProperties(AntplatformProperties antplatformProperties) {
		this.antplatformProperties = antplatformProperties;
	}
	
	protected <T> List<T> checkLists(List<T> list,Class<T> clazz){
		if(null == list){
			return new ArrayList<T>(0);
		}
		return list;
	}
	/**
	 * 值为authorization_code时，代表用authCode换取；值为refresh_token时，代表用refresh_token换取
	 * @param isRefresh
	 * @return
	 */
	protected String getGrantType(boolean isRefresh){
		
		return isRefresh ? "refresh_token" : "authorization_code";
	}
	
}
