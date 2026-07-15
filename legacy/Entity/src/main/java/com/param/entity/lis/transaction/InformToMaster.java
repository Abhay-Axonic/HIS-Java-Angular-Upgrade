package com.param.entity.lis.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;




@NamedNativeQueries({
@NamedNativeQuery(name = "GET_INFORM_TO_LIST", query = "select "
		+ "inform.inform_to_id as \"informToId\", "
		+ "inform.doctor_id as \"doctorId\", "
		+ "inform.lab_sample_dtls_id as \"labSampleDtlsId\", "
		+ "inform.inform_by_id as \"informById\", "
		+ "to_char(inform.inform_date, 'DD-MM-YYYY') as \"informDate\", "
		+ "inform.note as \"note\", "
		+ "inform.unit_id as \"unitId\", "
		+ "inform.organization_id as \"organizationId\", "
		+ "inform.status as \"status\", "
		+ "(case when 1=(select count(*) from public.m_employee_master where user_id=inform.inform_by_id) then " 
		+"	coalesce(emp.first_name)|| ' ' || coalesce(emp.middle_name)|| ' ' || coalesce(emp.last_name) " 
		+"	else " 
		+"	coalesce(doct_mst.first_name)|| ' ' || coalesce(doct_mst.middle_name)|| ' ' || coalesce(doct_mst.last_name) " 
		+"	end) as \"informedBy\","
		//+ "(coalesce(emp.first_name)|| ' ' || coalesce(emp.middle_name)|| ' ' || coalesce(emp.last_name)) as \"informedBy\", "
		+ "(coalesce(doct_mst.first_name)|| ' ' || coalesce(doct_mst.middle_name)|| ' ' || coalesce(doct_mst.last_name)) as \"doctorName\" "
		+ "from "
		+ "lab.t_lab_inform_to inform "
		+ "inner join doctor.m_doctor_master doct_mst on "
		+ "inform.doctor_id = doct_mst.doctor_id "
		+ "left join public.m_employee_master emp on "
		+ "inform.inform_by_id = emp.user_id "
		+ "where "
		+ "inform.lab_sample_dtls_id = :labSampleDtlsId"),

@NamedNativeQuery(name = "GET_TREATING_DOCTOR_ID_BY_SAMPLE", 
	query = "select " 
			+"	'Dr. ' || coalesce(enc_doct_mst.first_name)|| ' ' || coalesce(enc_doct_mst.middle_name)|| ' ' || coalesce(enc_doct_mst.last_name) " + 
			"	 as \"doctorName\", " + 
			"	 enc_mst.doctor_id as \"doctorId\" " + 
			"from " + 
			"	lab.t_lab_sample_dtls smp_dtls " + 
			"inner join public.t_order_master ord_mst on " + 
			"	smp_dtls.order_id = ord_mst.order_id " + 
			"INNER join opd.t_encounter_master enc_mst on " + 
			"	ord_mst.encounter_id = enc_mst.encounter_id " + 
			"INNER join doctor.m_doctor_master enc_doct_mst on " + 
			"	enc_mst.doctor_id = enc_doct_mst.doctor_id " + 
			"where " + 
			"	smp_dtls.lab_sample_dtls_id = :labSampleDtlsId " + 
			"union " + 
			"  select " + 
			"		 'Dr. ' ||coalesce(adm_doct_mst.first_name)|| ' ' || coalesce(adm_doct_mst.middle_name)|| ' ' || coalesce(adm_doct_mst.last_name) " + 
			"	 as \"doctorName\", " + 
			"	 adm.doctor_id 	as \"doctorId\" " + 
			"from " + 
			"	lab.t_lab_sample_dtls smp_dtls " + 
			"inner join public.t_order_master ord_mst on " + 
			"	smp_dtls.order_id = ord_mst.order_id " + 
			"INNER join adt.t_admission adm on " + 
			"	ord_mst.admission_id = adm.admission_id " + 
			"INNER join doctor.m_doctor_master adm_doct_mst on " + 
			"	 adm.doctor_id =adm_doct_mst.doctor_id " + 
			"where " + 
			"	smp_dtls.lab_sample_dtls_id = :labSampleDtlsId"),

})

@Entity
@Table(name = "t_lab_inform_to", schema = "lab")
@SequenceGenerator(name = "seq_lab_inform_to", sequenceName = "lab.seq_lab_inform_to", allocationSize = 1)
public class InformToMaster {
	
	@Id
	@Column(name = "inform_to_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lab_inform_to")
	private int informToId;

	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "inform_by_id")
	private Integer informById;

	@Column(name = "inform_date")
	private Date informDate;

	@Column(name = "note")
	private String note;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public int getInformToId() {
		return informToId;
	}

	public void setInformToId(int informToId) {
		this.informToId = informToId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getInformById() {
		return informById;
	}

	public void setInformById(Integer informById) {
		this.informById = informById;
	}

	public Date getInformDate() {
		return informDate;
	}

	public void setInformDate(Date informDate) {
		this.informDate = informDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}


}
