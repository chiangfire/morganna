package com.firecode.morganna.domain.common;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.datastax.driver.core.DataType.Name;
import com.firecode.morganna.framework.annotation.GeneratedValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 错误日志
 * @author JIANG
 */
@Table("f_error_log")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLog {
	
	@PrimaryKey
	@GeneratedValue
	private Long id;
	
	@CassandraType(type=Name.VARCHAR)
	private String path;
	
	@CassandraType(type=Name.INT)
	private int status;
	
	@CassandraType(type=Name.TEXT)
	private String error;
	
	@CassandraType(type=Name.VARCHAR)
	private String message;
	
	@CassandraType(type=Name.BIGINT)
	@Column("user_id")
	private Long userId;
	
	@Column("application_name")
	private String applicationName;
	
	@CassandraType(type=Name.VARCHAR)
	private String exception;
	
	@CassandraType(type=Name.TIMESTAMP)
	@CreatedDate
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	private Date timestamp;
	
	@CassandraType(type=Name.VARCHAR)
	@Column("method_name")
	private String methodName;
	
	@CassandraType(type=Name.TEXT)
	private String trace;
	
}
