package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "PMSFrequency")
@Table(name = "m_pms_frequency", schema = "inventory")
public class PMSFrequency extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

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

	public PMSFrequency() {
		super();
	}

	public PMSFrequency(Integer id) {
		super();
		this.id = id;
	}

	public PMSFrequency(Integer id, String code, String description) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
	}
	public PMSFrequency( String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}
}