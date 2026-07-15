package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({	
	
	@NamedQuery(name="GET_OTIMS_USER_LIST",query="SELECT otimsUserMaster.id as id, "
			+ "otimsUserMaster.username as username, "
			+ "otimsUserMaster.password as password, "
			+ "otimsUserMaster.firstname as firstname, "
			+ "otimsUserMaster.lastname as lastname, "
			+ "otimsUserMaster.email as email, "
			+ "otimsUserMaster.mobile as mobile, "
			+ "otimsUserMaster.role as role, "
			+ "otimsUserMaster.is_active as is_active, "
			+ "otimsUserMaster.is_deleted as is_deleted "
			+ "FROM OtimsUserMaster as otimsUserMaster ")
})

@Entity
@Table(name = "m_otims_user_master", schema = "public")
@SequenceGenerator(name="m_otims_user_master_id_seq", sequenceName="public.m_otims_user_master_id_seq", allocationSize=1)
public class OtimsUserMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_otims_user_master_id_seq")
	@Column(name = "id")
	private int id;
	
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "firstname")
	private String firstname;	
	
	@Column(name = "lastname")
	private String lastname;	
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "role")
	private int role;
	
	@Column(name = "is_active")
	private Boolean is_active;
	
	@Column(name = "is_deleted")
	private Boolean is_deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role", insertable = false, updatable = false)
	private StaffRoleMaster staffRoleTypeMaster;	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public StaffRoleMaster getStaffRoleTypeMaster() {
		return staffRoleTypeMaster;
	}

	public void setStaffRoleTypeMaster(StaffRoleMaster staffRoleTypeMaster) {
		this.staffRoleTypeMaster = staffRoleTypeMaster;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

}
