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

import com.alipay.api.domain.AlipayUserDeliverAddress;
import com.alipay.api.domain.AlipayUserPicture;
import com.datastax.driver.core.DataType.Name;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 支付宝用户信息
 * @author JIANG
 */
@Getter
@Setter
@NoArgsConstructor
@Table("f_alipay_user")
public class AlipayUser {
	//支付宝id
	@PrimaryKey("user_id")
	@CassandraType(type=Name.VARCHAR)
	private String userId;
	//用户头像地址
	@CassandraType(type=Name.VARCHAR)
	private String avatar;
	//用户昵称
	@Column("nick_name")
	@CassandraType(type=Name.VARCHAR)
	private String nickName;
	//用户省份
	@CassandraType(type=Name.VARCHAR)
	private String province;
	//用户城市
	@CassandraType(type=Name.VARCHAR)
	private String city;
	//用户性别，M为男性，F为女性
	@CassandraType(type=Name.VARCHAR)
	private String gender;
	//1代表公司账户2代表个人账户
	@Column("user_type")
	@CassandraType(type=Name.VARCHAR)
	private String userType;
	//Q代表快速注册用户 T代表已认证用户 B代表被冻结账户 W代表已注册，未激活的账户 
	@Column("user_status")
	@CassandraType(type=Name.VARCHAR)
	private String  userStatus;
	//T是通过 F是没有实名认证 
	@Column("is_certified")
	@CassandraType(type=Name.VARCHAR)
	private String isCertified;
	//T是学生 F不是学生
	@Column("is_student_certified")
	@CassandraType(type=Name.VARCHAR)
	private String  isStudentCertified;
	//详细地址
	@CassandraType(type=Name.VARCHAR)
	private String address;
	//区县名称。
	@CassandraType(type=Name.VARCHAR)
	private String area;
    //经营/业务范围（用户类型是公司类型时才有此字段）。
	@Column("business_scope")
	@CassandraType(type=Name.VARCHAR)
	private String businessScope;
	//【注意】只有is_certified为T的时候才有意义，否则不保证准确性.证件号码，结合证件类型使用.
	@Column("cert_no")
	@CassandraType(type=Name.VARCHAR)
	private String certNo;
	/** 
	 * 【注意】只有is_certified为T的时候才有意义，否则不保证准确性.
        0:身份证
		1:护照
		2:军官证
		3:士兵证
		4:回乡证
		5:临时身份证
		6:户口簿
		7:警官证
		8:台胞证
		9:营业执照
		10:其它证件
		11:港澳居民来往内地通行证
		12:台湾居民来往大陆通行证
	 */
	@Column("cert_type")
	@CassandraType(type=Name.VARCHAR)
	private String certType;
	
	//学信网返回的学校名称，有可能为空。
	@Column("college_name")
	@CassandraType(type=Name.VARCHAR)
	private String collegeName;
	//国家码
	@Column("country_code")
	@CassandraType(type=Name.VARCHAR)
	private String countryCode;
	
	//学信网返回的学历/学位信息，数据质量一般，请谨慎使用，取值包括：博士研究生、硕士研究生、高升本、专科、博士、硕士、本科、夜大电大函大普通班、专科(高职)、第二学士学位。
	@CassandraType(type=Name.VARCHAR)
	private String degree;
	
	//收货地址列表
	@Transient
	private List<AlipayUserDeliverAddress> deliverAddresses;
	// 企业法人证件图片（用户类型是公司类型时才有此字段）。
	@Transient
	private List<AlipayUserPicture> firmLegalPersonPictures;
	// 企业相关证件图片，包含图片地址（目前需要调用alipay.user.certify.image.fetch转换一下）及类型（用户类型是公司类型时才有此字段）。
	@Transient
	private List<AlipayUserPicture> firmPictures;
	//个人证件图片（用户类型是个人的时候才有此字段）。
	@Transient
	private List<AlipayUserPicture> personPictures;
	
	//用户支付宝邮箱登录名
	@CassandraType(type=Name.VARCHAR)
	private String email;
	
	//入学时间，yyyy-mm-dd格式
	@CassandraType(type=Name.VARCHAR)
	private String enrollmentTime;
	
	//企业代理人证件有效期（用户类型是公司类型时才有此字段）。
	@Column("firm_agent_person_cert_expiry_date")
	@CassandraType(type=Name.VARCHAR)
	private String firmAgentPersonCertExpiryDate;
	
	//企业代理人证件号码（用户类型是公司类型时才有此字段）。
	@Column("firm_agent_person_cert_no")
	@CassandraType(type=Name.VARCHAR)
	private String firmAgentPersonCertNo;
	//企业代理人证件类型, 返回值参考cert_type字段（用户类型是公司类型时才有此字段）。
	@Column("firm_agent_person_cert_type")
	@CassandraType(type=Name.VARCHAR)
	private String firmAgentPersonCertType;
	
	//企业代理人姓名（用户类型是公司类型时才有此字段）。
	@Column("firm_agent_person_name")
	@CassandraType(type=Name.VARCHAR)
	private String firmAgentPersonName;
	
	//企业法人证件有效期（用户类型是公司类型时才有此字段）。
	@Column("firm_legal_person_cert_expiry_date")
	@CassandraType(type=Name.VARCHAR)
	private String firmLegalPersonCertExpiryDate;
    //法人证件号码（用户类型是公司类型时才有此字段）。
	@Column("firm_legal_person_cert_no")
	@CassandraType(type=Name.VARCHAR)
	private String firmLegalPersonCertNo;
	//企业法人证件类型, 返回值参考cert_type字段（用户类型是公司类型时才有此字段）。
	@Column("firm_legal_person_cert_type")
	@CassandraType(type=Name.VARCHAR)
	private String firmLegalPersonCertType;
	//企业法人名称（用户类型是公司类型时才有此字段）。
	@Column("firm_legal_person_name")
	@CassandraType(type=Name.VARCHAR)
	private String firmLegalPersonName;
	/** 
	 * 公司类型，包括（用户类型是公司类型时才有此字段）：
	 * CO(公司)
     * INST(事业单位),
     * COMM(社会团体),
	 * NGO(民办非企业组织),
	 * STATEORGAN(党政国家机关)
	 */
	@Column("firm_type")
	@CassandraType(type=Name.VARCHAR)
	private String firmType;
	
    //预期毕业时间，不保证准确性，yyyy-mm-dd格式。
	@Column("graduation_time")
	@CassandraType(type=Name.VARCHAR)
	private String graduationTime;
	
	//余额账户是否被冻结。T--被冻结；F--未冻结
	@Column("is_balance_frozen")
	@CassandraType(type=Name.VARCHAR)
	private String isBalanceFrozen;
	
	//营业执照有效期，yyyyMMdd或长期，（用户类型是公司类型时才有此字段）。
	@Column("license_expiry_date")
	@CassandraType(type=Name.VARCHAR)
	private String licenseExpiryDate;
	
	//企业执照号码（用户类型是公司类型时才有此字段）。
	@Column("license_no")
	@CassandraType(type=Name.VARCHAR)
	private String licenseNo;
	
	//支付宝会员等级
	@Column("member_grade")
	@CassandraType(type=Name.VARCHAR)
	private String memberGrade;
	//手机号码。
	@CassandraType(type=Name.VARCHAR)
	private String mobile;
	
	//组织机构代码（用户类型是公司类型时才有此字段）。
	@Column("organization_code")
	@CassandraType(type=Name.VARCHAR)
	private String organizationCode;
	//个人用户生日。
	@Column("person_birthday")
	@CassandraType(type=Name.VARCHAR)
	private String personBirthday;
	//证件有效期（用户类型是个人的时候才有此字段）。
	@Column("person_cert_expiry_date")
	@CassandraType(type=Name.VARCHAR)
	private String personCertExpiryDate;
	//电话号码。
	@CassandraType(type=Name.VARCHAR)
	private String phone;
	//职业
	@CassandraType(type=Name.VARCHAR)
	private String profession;
	//淘宝id
	@Column("taobao_id")
	@CassandraType(type=Name.VARCHAR)
	private String taobaoId;
	//【注意】只有is_certified为T的时候才有意义，否则不保证准确性.若用户是个人用户，则是用户的真实姓名；若是企业用户，则是企业名称。
	@Column("user_name")
	@CassandraType(type=Name.VARCHAR)
	private String userName;
	//邮政编码。
	@CassandraType(type=Name.VARCHAR)
	private String zip;
	@Column("create_time")
	@CassandraType(type=Name.TIMESTAMP)
	@CreatedDate
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	private Date createTime;
	
	@Column("last_modify_time")
	@CassandraType(type=Name.TIMESTAMP)
	@LastModifiedDate
	@DateTimeFormat(pattern="yyyy-MM-dd hh:MM:ss")
	private Date lastModifyTime;
	
	@Override
	public String toString() {
		return "AlipayUser [userId=" + userId + ", avatar=" + avatar + ", nickName=" + nickName + ", province="
				+ province + ", city=" + city + ", gender=" + gender + ", userType=" + userType + ", userStatus="
				+ userStatus + ", isCertified=" + isCertified + ", isStudentCertified=" + isStudentCertified + "]";
	}
}
