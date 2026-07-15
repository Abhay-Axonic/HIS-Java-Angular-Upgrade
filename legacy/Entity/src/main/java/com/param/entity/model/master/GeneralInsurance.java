package com.param.entity.model.master;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "GenralInsurance")
@Table(name = "m_general_insurance_company", schema = "public")
public class GeneralInsurance extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "insurance_company_name", nullable = false, length = 100)
	private String insuranceCompanyName;
	
	@Column(name = "contact_no", length = 20)
	private String contactNo;
	
	@Column(name = "email", length = 100)
	private String email;
	
	@Column(name = "contact_person_name", nullable = false, length = 100)
	private String contactPersonName;
	
	@Column(name = "contact_person_number", length = 20)
	private String contactPersonNumber;
	
	@Column(name = "zip_code")
	private String zipcode;
	
	@Column(nullable = false, length = 200)
	private String address;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id", nullable = false)
	private Area area;
	
	public GeneralInsurance() {
		super();
	}
	
	public GeneralInsurance(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonNumber() {
		return contactPersonNumber;
	}

	public void setContactPersonNumber(String contactPersonNumber) {
		this.contactPersonNumber = contactPersonNumber;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
