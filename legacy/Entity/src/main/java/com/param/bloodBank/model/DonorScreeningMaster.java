package com.param.bloodBank.model;

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
@NamedQueries({
	
	@NamedQuery(name="CHECK_FOR_UNIQUE",
			query="SELECT mstr.id as id "
					+ " FROM DonorScreeningMaster mstr "
					+ " WHERE mstr.organizationId=:organizationId "
					+ " AND mstr.unitId=:unitId "
					+ " AND mstr.parameter=:parameter "
					+"  AND mstr.genderId=:genderId")
	
})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_DONOR_SCREENING_LIST",
			query="SELECT  mstr.id as \"id\" , "
					+ " mstr.parameter as \"parameter\" , "
					+ " mstr.gender_id as \"genderId\" , "
					+ " mstr.deferred_type_id as \"deferredTypeId\" ,  "
					+ " def.deffered_type_name as \"deferredType\" ,  "
					+ " mstr.status as \"status\" ,  "
					+ " mstr.unit_id as \"unitId\" , "
					+ " gendermst.gender_name as \"gender\" , "
					+ " mstr.organization_id as \"organizationId\"  "
					+ " FROM blood_bank.m_donor_screening_master mstr "
					+ " inner join public.m_gender_master gendermst on mstr.gender_id = gendermst.gender_id "
					+ " inner join blood_bank.m_deffered_type_master def on mstr.deferred_type_id = def.deffered_type_id"
					+ " WHERE mstr.organization_id=:organizationId "
					+ " AND mstr.unit_id=:unitId "),
	
	@NamedNativeQuery(name="GET_DEFFERD_TYPE_LIST",
	query="SELECT  deffered_type_id as \"defferedTypeId\" , "
			+ " deffered_type_name as \"defferedTypeName\" "
			+ " FROM blood_bank.m_deffered_type_master "),
	

@NamedNativeQuery(name="GET_DONOR_SCREENING_LIST_GENDER_ID",
query="SELECT  mstr.id as \"id\" , "
		+ " mstr.parameter as \"parameter\" , "
		+ " mstr.gender_id as \"genderId\" , "
		+ " mstr.deferred_type_id as \"deferredTypeId\" ,  "
		+ " def.deffered_type_name as \"deferredType\" ,  "
		+ " mstr.status as \"status\" ,  "
		+ " mstr.unit_id as \"unitId\" , "
		+ " gendermst.gender_name as \"gender\" , "
		+ " mstr.organization_id as \"organizationId\"  "
		+ " FROM blood_bank.m_donor_screening_master mstr "
		+ " inner join public.m_gender_master gendermst on mstr.gender_id = gendermst.gender_id "
		+ " inner join blood_bank.m_deffered_type_master def on mstr.deferred_type_id = def.deffered_type_id"
		+ " WHERE mstr.organization_id=:organizationId "
		+ " AND mstr.unit_id=:unitId "
		+ " AND mstr.gender_id=:genderId")
	
	
})





@Entity
@Table(name="m_donor_screening_master", schema="blood_bank")
@SequenceGenerator(name="seq_m_donor_screening_master", sequenceName="blood_bank.seq_m_donor_screening_master", allocationSize= 1)
public class DonorScreeningMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_donor_screening_master")
	
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "parameter")
	private String parameter;
	
	@Column(name = "deferred_type_id")
	private Integer deferredTypeId;
	
	@Column(name = "gender_id")
	private Integer genderId;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Integer getDeferredTypeId() {
		return deferredTypeId;
	}

	public void setDeferredTypeId(Integer deferredTypeId) {
		this.deferredTypeId = deferredTypeId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
}
