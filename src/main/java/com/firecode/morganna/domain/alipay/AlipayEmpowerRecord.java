package com.firecode.morganna.domain.alipay;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.datastax.driver.core.DataType.Name;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付宝第三方应用授权记录
 * @author JIANG
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table("f_alipay_empower_record")
public class AlipayEmpowerRecord {
	
	/** 
	 * 支付宝用户 user_id
	 */
	@PrimaryKey("user_id")
	@CassandraType(type=Name.VARCHAR)
	private String userId;
	
	/** 
	 * 授权商户的appid
	 */
	@Column("auth_app_id")
	@CassandraType(type=Name.VARCHAR)
	private String authAppId;
	/** 
	 * 应用授权令牌
	 * 有效期为365天<现在是永久有效>，刷新后重新计时
	 */
	@Column("app_auth_token")
	@CassandraType(type=Name.VARCHAR)
	private String appAuthToken;
	/** 
	 * 刷新令牌
	 * 刷新令牌后，我们会保证老的app_auth_token从刷新开始10分钟内可继续使用，请及时替换为最新token
	 */
	@Column("app_refresh_token")
	@CassandraType(type=Name.VARCHAR)
	private String appRefreshToken;
	/** 
	 * 授权生效时间
	 */
	@Column("auth_start")
	@CassandraType(type=Name.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	private Date authStart;
	
	/** 
	 * 授权失效时间
	 */
	@Column("auth_end")
	@CassandraType(type=Name.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	@Deprecated
	private Date authEnd;
	/** 
	 * valid：有效状态；invalid：无效状态
	 */
	@CassandraType(type=Name.VARCHAR)
	private String status;
	
	/** 
	 * 当前app_auth_token的授权接口列表
	 */
	@Transient
	private List<String> authMethods;
	
	@CreatedDate
	@Column("create_time")
	@CassandraType(type=Name.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	private Date createTime;
	
	@LastModifiedDate
	@Column("last_modify_time")
	@CassandraType(type=Name.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	private Date lastModifyTime;
}
