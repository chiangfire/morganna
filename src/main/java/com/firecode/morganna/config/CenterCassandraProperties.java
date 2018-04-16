package com.firecode.morganna.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Primary
@Configuration()
@PropertySource("classpath:cassandra.properties")
@ConfigurationProperties(prefix = "center.data.cassandra")
public class CenterCassandraProperties extends CassandraProperties{
	
	/**
	 * 应用启动时执行脚本
	 */
	private List<String> startupScripts = new ArrayList<>(3);

	public List<String> getStartupScripts() {
		
		return startupScripts;
	}

	public void setStartupScripts(List<String> startupScripts) {
		
		this.startupScripts = startupScripts;
	}
	
}
