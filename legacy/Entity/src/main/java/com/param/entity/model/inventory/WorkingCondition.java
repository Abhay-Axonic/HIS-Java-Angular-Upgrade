package com.param.entity.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.entity.model.base.CommonEntity;

@Entity(name = "WorkingCondition")
@Table(name = "m_working_condition", schema = "inventory")
public class WorkingCondition extends CommonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "code")
	private String code;
	
	@Column(name = "description")
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public WorkingCondition() {
		super();
	}

	public WorkingCondition(Integer id, String code, String description) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
	}

	public WorkingCondition(Integer id) {
		super();
		this.id = id;
	}
	
}