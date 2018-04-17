package com.firecode.morganna.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Primary
@Configuration()
@PropertySource("classpath:cassandra.properties")
@ConfigurationProperties(prefix = "center.data.cassandra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CenterCassandraProperties extends CassandraProperties{
	
	/**
	 * 应用启动时执行脚本
	 */
	private List<String> startupScripts = new ArrayList<>(3);
	
}
