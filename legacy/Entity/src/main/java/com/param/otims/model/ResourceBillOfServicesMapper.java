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
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import com.param.global.model.BedCategoryMaster;

	
	/*@NamedQuery(name = "GET_RESOURCE_BILL_OF_SERVICE_MAPPER",
			   query = "SELECT rbosmapp.componenetServicesId as componenetServicesId, "
//			   		//+ "  rbm.componenetServicesDesc as componenetServicesDesc, "
			   		+ "	 rbosmapp.dependentOnPercentage as dependentOnPercentage, "
			   		+ "	 rbosmapp.dependentOn as dependentOn "
			   		+ " FROM	ResourceBillOfServicesMapper rbosmapp "
			   //		+ " Inner join rbosmapp.resourceBillOfServicesMaster rbm "
			   		+ " WHERE rbosmapp.surgeryResourceConfigId =:surgeryResourceConfigId "
			   		+ " AND rbosmapp.status ='A'")*/
	
	@NamedNativeQuery(name = "GET_RESOURCE_BILL_OF_SERVICE_MAPPER",
			   query = " select distinct(sbosm.componenet_services_id) as \"componenetServicesId\", " + 
			   		"       sbosm.dependent_on_percentage as \"dependentOnPercentage\", "+
			   		"       sbosm.resource_bill_mapper_id as \"resourceBillMapperId\"," + 
			   		"       sm.service_standard_name as \"componenetServicesDesc\", " + 
			   		"       sbosm.dependent_on as \"dependentOn\" " + 
			   		" from otims.m_resource_bill_of_services_mapper sbosm "+
			   		" inner join service.m_service_master sm on " + 
			   	    " sm.service_master_id = sbosm.componenet_services_id " + 
			   		"  where sbosm.surgery_resource_config_id =:surgeryResourceConfigId "+
			   		"  AND sbosm.status ='A'"+
			   		" order by sbosm.resource_bill_mapper_id  asc ")

@Entity
@Table(name = "m_resource_bill_of_services_mapper" , schema = "otims")
@SequenceGenerator(name = "m_resource_bill_of_services_mapper_seq" , sequenceName = "otims.m_resource_bill_of_services_mapper_seq" , allocationSize = 1)
public class ResourceBillOfServicesMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_resource_bill_of_services_mapper_seq")

	@Column(name = "resource_bill_mapper_id")
	private Integer resourceBillMapperId;
	
	@Column(name = "surgery_resource_config_id")
	private Integer surgeryResourceConfigId;
	
	@Column(name = "componenet_services_id")
	private Integer componenetServicesId;
	
	@Column(name = "dependent_on")
	private Integer dependentOn;
	
	@Column(name = "dependent_on_percentage")
	private double dependentOnPercentage;
	
	@Column(name = "status")
	private Character status;
	
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

	public Integer getResourceBillMapperId() {
		return resourceBillMapperId;
	}
	
	public void setResourceBillMapperId(Integer resourceBillMapperId) {
		this.resourceBillMapperId = resourceBillMapperId;
	}

	

	public Integer getSurgeryResourceConfigId() {
		return surgeryResourceConfigId;
	}

	public void setSurgeryResourceConfigId(Integer surgeryResourceConfigId) {
		this.surgeryResourceConfigId = surgeryResourceConfigId;
	}

	

	public Integer getComponenetServicesId() {
		return componenetServicesId;
	}

	public void setComponenetServicesId(Integer componenetServicesId) {
		this.componenetServicesId = componenetServicesId;
	}

	public Integer getDependentOn() {
		return dependentOn;
	}

	public void setDependentOn(Integer dependentOn) {
		this.dependentOn = dependentOn;
	}

	public double getDependentOnPercentage() {
		return dependentOnPercentage;
	}

	public void setDependentOnPercentage(double dependentOnPercentage) {
		this.dependentOnPercentage = dependentOnPercentage;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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
