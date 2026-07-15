package com.param.global.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.master.global.model.UserMaster;


@NamedQueries({
		@NamedQuery(name="VALIDATE_USER_TOKEN",query="SELECT sso.userId as userId, "
			+ "sso.lastLogin as lastLogin,"
			+ "sso.expiry as expiry,"
			+ "sso.token as token, "
			+ "userMaster.organizationId as organizationId, "
			+ "userMaster.unitId as unitId "
			+ "FROM UserSSODto sso INNER JOIN sso.userMaster as userMaster "
			+ "WHERE sso.token=:token "),
		@NamedQuery(name="GET_ROLE_IDS_BY_USER_ID",query="SELECT mapper.roleId "
				+ "FROM UserRoleMapper mapper "
				+ "WHERE mapper.userId=:userId ")
		
		

	
})




@Entity
@Table(name = "SSODetails", schema = "SSO")
public class UserSSODto {
	
	@Id
	@Column
	private Integer userId;
	
	@Column
	private Long lastLogin;
	
	@Column
	private Long expiry;
	
	@Column
	private String token;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Long getExpiry() {
		return expiry;
	}

	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}


}
