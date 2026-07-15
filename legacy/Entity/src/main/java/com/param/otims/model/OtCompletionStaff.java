package com.param.otims.model;

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
	@NamedNativeQuery(name = "GET_OT_COMPLETION_STAFF_LIST",
					 query = "   select  " + 
					 		"	otstaff.ot_completion_staff_id as \"otCompletionStaffId\",  " + 
					 		"	otstaff.procedure_order_id as \"procedureId\",  " + 
					 		"	service.service_standard_name as \"serviceName\",  " + 
					 		"	'Dr. ' || doct_mst.first_name || ' ' || doct_mst.last_name as \"doctorName\",  " + 
					 		"	otstaff.amount as \"amount\",  "
					 		+ " otstaff.amount as \"originalAmount\"," + 
//					 		"	(select amount from otims.t_bill_of_material tbom where tbom.procedure_id = otstaff.procedure_order_id  and tbom.service_id  = otstaff.service_id and tbom.billing_bed_cat_id = :billingBedCategoryId) as \"originalAmount\",  " + 
					 		"	otstaff.is_editable as \"isEditable\",  " + 
					 		"	otstaff.doctor_id as \"doctorId\", "
					 		+ "otstaff.specility_id as \"specilityId\", " + 
					 		"	otstaff.org_id as \"organizationId\",  " + 
					 		"	otstaff.unit_id as \"unitId\",  " + 
					 		"	otstaff.created_by as \"createdBy\",  " + 
					 		"	otstaff.service_id as \"serviceId\",  " + 
					 		"	to_char(otstaff.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", "
					 		+" smd.is_doc_req as \"isRefDoctorShare\"," + 
					 		"	otstaff.proc_req_id as \"procReqId\" " + 
					 		"from  " + 
					 		"	otims.t_ot_completion_staff otstaff  " + 
					 		"left join doctor.m_doctor_master doct_mst on  " + 
					 		"	doct_mst.doctor_id = otstaff.doctor_id  " + 
					 		"inner join service.m_service_master service on  " + 
					 		"	service.service_master_id = otstaff.service_id  "
					 		+ " inner join  service.t_unit_service_master_details smd on service.service_master_id = smd.service_master_id  "
							 + "where  "
							 + "	otstaff.proc_req_id = :id  "
							 + " and otstaff.status='A' and  smd.unit_id = :unitId and smd.organization_id = :orgId ")
})




@Entity
@Table(name = "t_ot_completion_staff" , schema = "otims")
@SequenceGenerator(name = "t_ot_completion_staff_seq" , sequenceName = "otims.t_ot_completion_staff_seq" , allocationSize = 1)
public class OtCompletionStaff {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_ot_completion_staff_seq")
	@Column(name = "ot_completion_staff_id")
	private int otCompletionStaffId;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "proc_req_id")
	private Integer procReqId;
	
	@Column(name = "specility_id")
	private Integer specilityId;

	@Column(name = "is_editable")
	private Boolean isEditable;

	
	public int getOtCompletionStaffId() {
		return otCompletionStaffId;
	}

	public void setOtCompletionStaffId(int otCompletionStaffId) {
		this.otCompletionStaffId = otCompletionStaffId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Integer getProcReqId() {
		return procReqId;
	}

	public void setProcReqId(Integer procReqId) {
		this.procReqId = procReqId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecilityId() {
		return specilityId;
	}

	public void setSpecilityId(Integer specilityId) {
		this.specilityId = specilityId;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	
}
