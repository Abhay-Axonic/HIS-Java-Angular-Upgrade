package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_PROCEDURE_ORDER_SERVICE_BY_PROCEDURE_ORDER_ID",
	query="SELECT mstr.procedureOrderServiceId as procedureOrderServiceId , " 
				+ " mstr.procedureRequestId as procedureRequestId , "
				+ " mstr.serviceId as serviceId, "
				+ " mstr.procedureOrderId as procedureOrderId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm') as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm') as updatedDate, "
				+ " mstr.status as status, "
				+ " serviceMaster.serviceStandardName as serviceStandardName, "
				+ " serviceMaster.serviceStandardCode as serviceStandardCode, "
				+ " specialityMaster.specialityName as specialityName,"
				+ " specialityMaster.specialityId as specialityId, "
				+ " subSpecialityMaster.subSpecialityMasterName as subSpecialityName, "
				+ " subSpecialityMaster.subSpecialityMasterId as subSpecialityId "
				+ " FROM ProcedureOrderService mstr "
				+ " INNER JOIN  mstr.serviceMaster serviceMaster  "
				+ " INNER JOIN  serviceMaster.specialityMaster specialityMaster  "
				+ " INNER JOIN  serviceMaster.subSpecialityMaster subSpecialityMaster  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.status = 'A' "
				+ " AND mstr.procedureOrderId= :procedureOrderId "
				
				)
})


@Entity	
@Table(name = "t_procedure_order_service", schema = "otims")
@SequenceGenerator(name = "procedure_order_service_seq", sequenceName = "otims.procedure_order_service_seq", allocationSize = 1)
public class ProcedureOrderService {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_order_service_seq")
	@Column(name = "procedure_order_service_id")
	private Integer procedureOrderServiceId;
	
	@Column(name = "procedure_request_id")
	private Integer procedureRequestId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId ;
	
	@Column(name = "status")
	private char status;
			
	@Column(name = "organization_id")
	private Integer organizationId;
	
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	public Integer getProcedureOrderServiceId() {
		return procedureOrderServiceId;
	}

	public void setProcedureOrderServiceId(Integer procedureOrderServiceId) {
		this.procedureOrderServiceId = procedureOrderServiceId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
	
	
	
}
