package com.param.billing.global.model;

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
	 @NamedQuery(name="GET_SERVICE_BASE_PRICE_LIST",
			 query="SELECT sbpm.serviceBasePriceId AS serviceBasePriceId,"
			 		+ "sbpm.serviceMasterId AS serviceMasterId ,"
			 		+ "sbpm.basePrice AS basePrice,"
			 		+ "sbpm.status AS status,"
			 		+ "to_char(sbpm.toDate,'dd/MM/yyyy') AS toDate,"
			 		+ "to_char(sbpm.fromDate,'dd/MM/yyyy') AS fromDate,"
			 		+ "sm.serviceStandardName AS serviceName,"
			 		+ "sm.serviceStandardCode AS serviceStandardCode,"
			 		+ "spemstr.specialityName AS specialityName,"
			 		+ "spemstr.specialityId AS specialityId,"
			 		+ "subSpMstr.subSpecialityMasterId AS subSpecialityMasterId,"
			 		+ "subSpMstr.subSpecialityMasterName AS subSpecialityMasterName "
			 		+ "FROM ServiceBasePriceMaster sbpm "
			 		+ "INNER JOIN sbpm.serviceMaster sm "
			 		+ "INNER JOIN sm.specialityMaster spemstr "
			 		+ "INNER JOIN sm.subSpecialityMaster subSpMstr "
			 		+ " WHERE sbpm.orgnisationId=:orgId "
			 		+ " AND sbpm.unitId=:unitId "
			 		+ " AND sbpm.status='A' "
			 		+ " ORDER BY sbpm.serviceBasePriceId DESC"),
	 
	 @NamedQuery(name="GET_SERVICE_BASE_PRICE_LIST_BY_ID",
			 query="SELECT sbpm.serviceBasePriceId AS serviceBasePriceId,"
			 		+ "sbpm.serviceMasterId AS serviceMasterId ,"
			 		+ "sbpm.basePrice AS basePrice,"
			 		+ "sbpm.status AS status,"
			 		+ "to_char(sbpm.toDate,'MM/dd/yyyy') AS toDate,"
			 		+ "to_char(sbpm.fromDate,'MM/dd/yyyy') AS fromDate,"
			 		+ "sm.serviceStandardCode AS serviceStandardCode,"
			 		+ "sm.serviceStandardName AS serviceName,"
			 		+ "spemstr.specialityName AS specialityName,"
			 		+ "spemstr.specialityId AS specialityId,"
			 		+ "subSpMstr.subSpecialityMasterId AS subSpecialityMasterId,"
			 		+ "subSpMstr.subSpecialityMasterName AS subSpecialityMasterName "
			 		+ "FROM ServiceBasePriceMaster sbpm "
			 		+ "INNER JOIN sbpm.serviceMaster sm "
			 		+ "INNER JOIN sm.specialityMaster spemstr "
			 		+ "INNER JOIN sm.subSpecialityMaster subSpMstr "
			 		+ " WHERE sbpm.serviceBasePriceId=:serviceBasePriceId "),
	 
	 @NamedQuery(name="GET_SERVICE_BASE_PRICE_LIST_SEARCH",
	 query="SELECT sbpm.serviceBasePriceId AS serviceBasePriceId,"
	 		+ "sbpm.serviceMasterId AS serviceMasterId ,"
	 		+ "sbpm.basePrice AS basePrice,"
	 		+ "sbpm.status AS status,"
	 		+ "to_char(sbpm.toDate,'dd/MM/yyyy') AS toDate,"
	 		+ "to_char(sbpm.fromDate,'dd/MM/yyyy') AS fromDate,"
	 		+ "sm.serviceStandardName AS serviceName,"
	 		+ "sm.serviceStandardCode AS serviceStandardCode,"
	 		+ "spemstr.specialityName AS specialityName,"
	 		+ "spemstr.specialityId AS specialityId,"
	 		+ "subSpMstr.subSpecialityMasterId AS subSpecialityMasterId,"
	 		+ "subSpMstr.subSpecialityMasterName AS subSpecialityMasterName "
	 		+ "FROM ServiceBasePriceMaster sbpm "
	 		+ "INNER JOIN sbpm.serviceMaster sm "
	 		+ "INNER JOIN sm.specialityMaster spemstr "
	 		+ "INNER JOIN sm.subSpecialityMaster subSpMstr "
	 		+ " WHERE sbpm.orgnisationId=:orgId "
	 		+ " AND sbpm.unitId=:unitId "
	 		+ " AND sbpm.status='A' "
	 		+ " AND (trim(lower(sm.serviceStandardName)) like :serviceSearch OR trim(lower(sm.serviceStandardCode))  like :serviceSearch ) "
	 		+ " ORDER BY sbpm.serviceBasePriceId DESC"),

 })

@Entity
@Table(name="m_service_base_price_master",schema="service")
@SequenceGenerator(name = "service_base_price_master_seq", sequenceName = "service.service_base_price_master_seq", allocationSize = 1)
public class ServiceBasePriceMaster {

	@Id
	@Column(name = "service_base_price_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_base_price_master_seq")
	private int serviceBasePriceId;
	  
	@Column(name = "service_master_id")
	private Integer serviceMasterId;
	  
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	  
	@Column(name = "base_price")
	private double basePrice;
	  
	@Column(name = "from_date")
	private Date fromDate;
	  
	@Column(name = "to_date")
	private Date toDate;
	  
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organisation_id")
	private Integer orgnisationId;
	
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
	
	@Column(name = "cost_of_service")
	private double costOfService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service_master_id", insertable=false, updatable=false, nullable=false)
	private ServiceMaster serviceMaster;

	public double getCostOfService() {
		return costOfService;
	}

	public void setCostOfService(double costOfService) {
		this.costOfService = costOfService;
	}

	public int getServiceBasePriceId() {
		return serviceBasePriceId;
	}

	public void setServiceBasePriceId(int serviceBasePriceId) {
		this.serviceBasePriceId = serviceBasePriceId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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
