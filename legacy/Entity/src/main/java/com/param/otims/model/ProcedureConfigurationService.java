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
	
	@NamedQuery(name="GET_SERVICE_LIST_BY_PROCEDURE_CONFIG_ID",
	query="SELECT mstr.procedureConfigServiceId as procedureConfigServiceId , " 
				+ " mstr.procedureConfigId as procedureConfigId , "
				+ " procedureConfiguration.serviceId as procConfigServiceId,"
				+ " mstr.serviceId as serviceId, "
				+ " serviceMaster.serviceStandardName as serviceStandardName, "
				+ " serviceMaster.serviceStandardCode as serviceStandardCode, "
				+ " specialityMaster.specialityName as specialityName, "
				+ " subSpecialityMaster.subSpecialityMasterName as subSpecialityName, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " specialityMaster.specialityId as specialityId, "
				+ " subSpecialityMaster.subSpecialityMasterId as subSpecialityId,"
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " mstr.status as status "
				+ " FROM ProcedureConfigurationService mstr "
				+ " INNER JOIN  mstr.serviceMaster serviceMaster  "
				+ " INNER JOIN  serviceMaster.specialityMaster specialityMaster  "
				+ " INNER JOIN  serviceMaster.subSpecialityMaster subSpecialityMaster  "
                + " INNER JOIN  mstr.procedureConfiguration procedureConfiguration  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				//+ " AND mstr.procedureConfigId= :procedureConfigId "
				+ " AND mstr.status = 'A' "
				)
})

@Entity
@Table(name = "t_procedure_config_service" , schema = "otims")
@SequenceGenerator(name = "procedure_config_service_seq" , sequenceName = "otims.procedure_config_service_seq" , allocationSize = 1)
public class ProcedureConfigurationService {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_config_service_seq")
	@Column(name = "procedure_config_service_id")
	private Integer procedureConfigServiceId;
	
	@Column(name = "procedure_config_id")
	private Integer procedureConfigId;
	
	@Column(name = "service_id")
	private Integer serviceId ;
	
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
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_config_id", insertable = false, updatable = false)
    private ProcedureConfiguration procedureConfiguration;

	
	public Integer getProcedureConfigServiceId() {
		return procedureConfigServiceId;
	}

	public void setProcedureConfigServiceId(Integer procedureConfigServiceId) {
		this.procedureConfigServiceId = procedureConfigServiceId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
