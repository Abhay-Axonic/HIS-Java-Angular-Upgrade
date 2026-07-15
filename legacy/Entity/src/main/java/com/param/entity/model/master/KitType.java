package com.param.entity.model.master;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "KitType")
@Table(name = "m_kit_type", schema = "public")
public class KitType extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(nullable=false, length=10)
	private String code;
	

	@Column(nullable=false, length=10)
	private String description;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public KitType() {
		super();
	
	}
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public KitType(Integer id) {
		super();
		this.id = id;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public KitType(String code,String description,Boolean status) {
		super();
		this.code = code;
		this.description = description;
		this.isActive=status;
	}
	
	public void updateKitType(String code,String description) {
		
		this.code = code;
		this.description = description;
		
	}

	public KitType(Integer id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	

	public void addKitType(KitType kitType) {
		// TODO Auto-generated method stub
		
	}
}
