package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({
	@NamedNativeQuery(name="GET_USER_DETAILS_LOG_BY_USER_ID",
			query="SELECT log.log_id as \"logId\", log.is_login as \"isLogin\",log.is_logout as \"isLogout\", log.status as \"status\",log.user_id as \"userId\",log.mac_address as \"macAddress\" "
					+ " FROM public.t_user_login_log log "
					+ " WHERE log.organization_id=:orgId "
					+ " AND log.unit_id=:unitId "
					+ " AND log.user_id=:userId "
					+ " AND log.is_login='Y' AND log.is_logout='N' AND log.status='A' "
//					+ "  order by  log.log_id desc limit 1"
					+ ""),
	@NamedNativeQuery(name="GET_USER_DETAILS_LOG_BY_MACADDRESS",
	query="SELECT log.log_id as \"logId\", log.is_login as \"isLogin\",log.is_logout as \"isLogout\", log.status as \"status\",log.user_id as \"userId\",log.mac_address as \"macAddress\","
			+ " log.organization_id as \"organizationId\", log.unit_id as \"unitId\" "
			+ " FROM public.t_user_login_log log "
			+ " WHERE log.organization_id=:orgId "
			+ " AND log.unit_id=:unitId "
			+ " AND log.user_id=:userId "
			+ " AND log.mac_address=:macAddress "
			+ " AND log.is_login='Y' AND log.is_logout='N' AND log.status='A' "
			+ "")
})
@NamedQueries({
	@NamedQuery(name="CHECK_OTHER_LOGIN",query="SELECT  log.logId as logId,log.macAddress as macAddress,log.isLogin as isLogin,log.unitId as unitId,"
			+ " log.organizationId as organizationId,log.userId as userId ,log.isLogout as isLogout ,log.status as status "
					+ " FROM UserLoginLog  log "
					+ " WHERE  log.macAddress=:macAddress"
					+ " AND log.isLogin='N' AND log.isLogout ='Y' AND log.status='C' "
					+ " AND log.unitId=:unitId "
					+ " AND log.userId=:userId "
					+ " AND log.organizationId=:orgId "
					+ " order by log.logId desc"),
	@NamedQuery(name="CHECK_OTHER_LOGIN_BY_USER_ID",query="SELECT  log.logId as logId,log.macAddress as macAddress,log.isLogin as isLogin,log.unitId as unitId,"
			+ " log.organizationId as organizationId,log.userId as userId "
					+ " FROM UserLoginLog  log "
					+ " WHERE log.status='A'"
					+ " AND  log.userId=:userId"
					+ " AND log.isLogin='Y' "
					+ " AND log.unitId=:unitId "
					+ " AND log.organizationId=:orgId "
					+ " order by log.logId desc"),
	@NamedQuery(name="GET_USER_LIST_OF_SCHEDULING_TO_UPDATE",
			query="SELECT  log.logId as logId "
					+ " FROM UserLoginLog  log "
					+ " WHERE log.status='C'  AND log.isLogin='N' AND log.isLogout='Y' "
					+ " and log.unitId in (:unitIds) "
					+ " order by log.logId desc")
})
@Entity
@Table(name="t_user_login_log",schema="public")
@SequenceGenerator(name="user_login_log_seq",sequenceName="public.user_login_log_seq",allocationSize=1)
public class UserLoginLog {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="user_login_log_seq")
	@Column(name="log_id")
	private Integer logId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="log_time")
	private Date logTime;
	
	@Column(name="is_login")
	private Character isLogin;
	
	@Column(name="is_logout")
	private Character isLogout;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="mac_address")
	private String macAddress;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public Character getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Character isLogin) {
		this.isLogin = isLogin;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Character getIsLogout() {
		return isLogout;
	}

	public void setIsLogout(Character isLogout) {
		this.isLogout = isLogout;
	}
	
	
}
