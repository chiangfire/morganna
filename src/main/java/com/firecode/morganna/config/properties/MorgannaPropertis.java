package com.firecode.morganna.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.firecode.kabouros.common.domain.ApplicationProperties;

@ConfigurationProperties(prefix="morganna.application")
public class MorgannaPropertis extends ApplicationProperties{
	

}
