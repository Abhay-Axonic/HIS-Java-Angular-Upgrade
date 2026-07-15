package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	
	@NamedQuery(name="GET_STAFF_ROLE_LIST",query="SELECT staffRoleMaster.id as id, "
			+ "staffRoleMaster.role as role, "
			+ "staffRoleMaster.description as description, "
			+ "staffRoleMaster.is_active as is_active, "
			+ "staffRoleMaster.is_deleted as is_deleted "
			+ "FROM StaffRoleMaster as staffRoleMaster ")

})

@Entity
@Table(name = "m_staff_role_master", schema = "public")
@SequenceGenerator(name="m_staff_role_master_id_seq", sequenceName="public.m_staff_role_master_id_seq", allocationSize=1)
public class StaffRoleMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_staff_role_master_id_seq")
	@Column(name = "id")
	private int id;
	
	@Column(name = "role")
	private String role;

	@Column(name = "description")
	private String description;	
	
	@Column(name = "is_active")
	private Boolean is_active;
	
	@Column(name = "is_deleted")
	private Boolean is_deleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
