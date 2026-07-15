package com.param.bloodBank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;


@Entity
@Table(name = "m_deffered_type_master", schema = "blood_bank")
public class DefferedTypeMaster {

	
	@Column(name = "deffered_type_id")
	private Integer defferedTypeId;
	
	@Column(name = "deffered_type_name")
	private String defferedTypeName;

	public Integer getDefferedTypeId() {
		return defferedTypeId;
	}

	public void setDefferedTypeId(Integer defferedTypeId) {
		this.defferedTypeId = defferedTypeId;
	}

	public String getDefferedTypeName() {
		return defferedTypeName;
	}

	public void setDefferedTypeName(String defferedTypeName) {
		this.defferedTypeName = defferedTypeName;
	}
	
	
}
