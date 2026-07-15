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
		@NamedNativeQuery(name = "GET_OT_SERVICES_BY_PRO_REQ_ID", query = /*
																			 * "select otsrv.ot_service_id as \"otServiceId\", otsrv.procedure_order_id as \"procedureOrderId\", otsrv.service_id as \"serviceId\",  "
																			 * +
																			 * " otsrv.amount as \"amount\", otsrv.amount as \"rate\", otsrv.org_id as \"orgId\", otsrv.unit_id as \"unitId\", otsrv.created_by as \"createdBy\",  "
																			 * +
																			 * " to_char(otsrv.created_date,'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", srv.service_standard_name as \"serviceName\", "
																			 * +
																			 * " srv.speciality_id as \"specialityId\", srv.sub_speciality_id as \"subSpecialityId\", spc.speciality_name as \"speciality\", "
																			 * + " prord.service_id as \"procedureId\" "
																			 * + " from otims.t_ot_service otsrv " +
																			 * " inner join service.m_service_master srv "
																			 * +
																			 * " on srv.service_master_id = otsrv.ot_service_id "
																			 * +
																			 * " on srv.service_master_id = otsrv.service_id "
																			 * +
																			 * " inner join doctor.m_speciality_master spc "
																			 * +
																			 * " on spc.speciality_id = srv.speciality_id  "
																			 * +
																			 * " inner join otims.t_procedure_order_master prord "
																			 * +
																			 * " on prord.procedure_order_id = otsrv.procedure_order_id "
																			 * + " where prord.pro_req_id = :id "
																			 */

		"select " 
		        + "	otsrv.ot_service_id as \"otServiceId\", "
		        + "	otsrv.procedure_id as \"procedureId\", "
				+ "	otsrv.service_id as \"serviceId\", " 
		        + "	otsrv.amount as \"amount\", "
				+ "	otsrv.amount as \"rate\", " 
		        + "	otsrv.org_id as \"orgId\", " 
		        + "	otsrv.unit_id as \"unitId\", "
				+ "	otsrv.created_by as \"createdBy\", "
				+ "	to_char(otsrv.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", "
				+ "	srv.service_standard_name as \"serviceName\", " 
				+ "	srv.speciality_id as \"specialityId\", "
				+ "	srv.sub_speciality_id as \"subSpecialityId\", " 
				+ "	spc.speciality_name as \"speciality\" "
				+ " from " 
				+ "	otims.t_ot_service otsrv "
				+ " inner join service.m_service_master srv on " 
				+ "	srv.service_master_id = otsrv.service_id "
				+ " inner join doctor.m_speciality_master spc on " 
				+ "	spc.speciality_id = srv.speciality_id "
				+ " where " 
				+ "	otsrv.proc_req_id = :id") })
@Entity
@Table(name = "t_ot_service", schema = "otims")
@SequenceGenerator(name = "t_ot_service_seq", sequenceName = "otims.t_ot_service_seq", allocationSize = 1)
public class OperationTheaterService {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_ot_service_seq")
	@Column(name = "ot_service_id")
	private Integer otServiceId;

	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;

	@Column(name = "service_id")
	private Integer serviceId;

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

	@Column(name = "proc_req_id ")
	private Integer proReqId;

	@Column(name = "procedure_id")
	private Integer procedureId;

	public Integer getOtServiceId() {
		return otServiceId;
	}

	public void setOtServiceId(Integer otServiceId) {
		this.otServiceId = otServiceId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
		this.status = status;
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

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

}
