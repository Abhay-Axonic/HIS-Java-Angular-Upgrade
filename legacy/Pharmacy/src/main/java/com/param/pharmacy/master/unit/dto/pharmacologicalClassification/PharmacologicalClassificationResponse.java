package com.param.pharmacy.master.unit.dto.pharmacologicalClassification;

import com.param.service.global.model.CookieObjectDto;

public abstract class PharmacologicalClassificationResponse extends CookieObjectDto{

	protected Integer id;

	protected String code;

	protected String classification;

	protected Boolean status;
	
	protected Boolean antibioticTest;

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

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getAntibioticTest() {
		return antibioticTest;
	}

	public void setAntibioticTest(Boolean antibioticTest) {
		this.antibioticTest = antibioticTest;
	}

}

/*
 * "code": "Code-1001", "classification": "Pharmacological Classification",
 * "status": true
 */
