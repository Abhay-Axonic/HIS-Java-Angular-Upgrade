package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.EquipmentMaster;
import com.param.global.model.ServiceMaster;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_MPERIODICITY_CONFIG_LIST",
					 query = "select " 
							 + " 	distinct config.periodicity_service_config_id as \"periodicityServiceConfigId\", " 
							 + " 	srvmst.service_standard_name as \"serviceName\", " 
							 + " 	srvmst.service_standard_code as \"serviceCode\", " 
							 + " 	config.service_id as \"serviceId\" , " 
							 + " 	spc.speciality_name as \"department\", " 
							 + " 	subspc.sub_speciality_name as \"subDepartment\", " 
							 + " 	config.duration as \"duration\", " 
							 + " 	config.status as \"status\", " 
							 + " 	config.measurement_unit_id as \"measurementUnitId\", " 
							 + " 	config.visit_type_id as \"visitTypeId\", " 
							 + " 	config.asset_category_id as \"assetCategoryId\", " 
							 + " 	config.product_category_id as \"productCategoryId\", " 
							 + " 	prodCat.category as \"productCategoryName\", " 
							 + " 	assetCat.category as \"assetCategoryName\", " 
							 + " 	config.unit_id as \"unitId\", " 
							 + " 	config.organisation_id as \"organisationId\", " 
							 + " 	tat.tat_time_name as \"tatTimeName\", " 
							 + " 	config.created_by as \"createdBy\", " 
							 + " 	to_char(config.created_date, 'DD-MM-YYYY') as \"createdDate\", " 
							 + " 	baseprice.base_price as \"basePrice\", " 
							 + " 	config.equipment_id as \"equipmentId\", " 
							 + " 	item.\"name\" as \"equipmentName\" " 
							 + " from " 
							 + " 	service.m_periodicity_service_config config " 
							 + " inner join service.m_service_master srvmst on " 
							 + " 	srvmst.service_master_id = config.service_id " 
							 + " inner join service.t_unit_service_master_details srvmppr on " 
							 + " 	srvmppr.service_master_id = srvmst.service_master_id " 
							 + " inner join service.m_service_base_price_master baseprice on " 
							 + " 	srvmst.service_master_id = baseprice.service_master_id " 
							 + " inner join doctor.m_speciality_master spc on " 
							 + " 	spc.speciality_id = srvmst.speciality_id " 
							 + " left join public.m_product_category prodCat on " 
							 + " 	prodCat.id = config.product_category_id " 
							 + " left join public.m_asset_category assetCat on " 
							 + " 	assetCat.id = config.asset_category_id " 
							 + " left join public.m_item item on " 
							 + " 	item.id = config.equipment_id	 " 
							 + " inner join doctor.m_sub_speciality_master subspc on " 
							 + " 	subspc.sub_speciality_id = srvmst.sub_speciality_id " 
							 + " inner join lab.m_tat_time_master tat on " 
							 + " 	tat.tat_time_id = config.measurement_unit_id " 
							 + " where " 
							 + " 	config.unit_id = :unitId " 
							 + " 	and config.organisation_id = :orgId " 
							 + " 	and baseprice.unit_id =:unitId " 
							 + " order by " 
							 + " 	config.periodicity_service_config_id desc "),
	
	@NamedNativeQuery(name = "UPDATE_MPERIODICITY_STATUS",
					 query = " update service.m_periodicity_service_config set status =:status "
					 	   + " where periodicity_service_config_id =:id"),
	
	@NamedNativeQuery(name = "SEARCH_PATIENT_FOR_PERIODICITY_SERVICES",
	 query = "select concat(coalesce(pre.prefix_code,''),' ',pat.first_name,' ',pat.middle_name,' ',pat.last_name) as \"patientName\" , pat.patient_id as \"patientId\", adm.admission_id as \"admissionId\", "
			 + " pat.uhid_number as \"uhidNumber\", ward.ward_id as \"wardId\", ward.ward_name as \"wardName\", bed.bed_number as \"bedNumber\",  "
			 + " adm.admission_number as \"admissionNumber\", adm.visit_type_id as \"visitTypeId\", admdt.bed_id as \"bedId\" "
			 + " from adt.t_admission adm "
			 + " inner join patient.t_patient_registration pat  "
			 + " on pat.patient_id = adm.patient_id "
			 + " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + " inner join adt.t_admission_details admdt "
			 + " on admdt.admission_id = adm.admission_id "
			 + " inner join adt.m_ward_master ward "
			 + " on ward.ward_id = admdt.ward_id "
			 + " inner join adt.m_bed_master bed "
			 + " on bed.bed_id = admdt.bed_id "
			 + " where admdt.is_discharge = 'N' "
			 + " and adm.visit_type_id = :visitTypeId "
			 + " and (lower(trim(pat.first_name)) like :name or lower(trim(pat.middle_name)) like :name "
			 + " or lower(trim(pat.last_name)) like :name) "
			 + " and adm.organization_id = :orgId and adm.unit_id = :unitId"),
	
	@NamedNativeQuery(name = "VALIDATE_PERIODICITY_SERVICE_CONFIG_AVAILABILITY",
					 query = " select distinct config.periodicity_service_config_id as \"periodicityServiceConfigId\", "
					 		 + "config.service_id as \"serviceId\" , "
					 		 + "srvmst.service_standard_name as \"serviceName\", "
					 		 + "srvmst.service_standard_code as \"serviceCode\",  "
							 + "spc.speciality_name as \"department\", "
							 + "spc.speciality_id as \"departmentId\", "
							 + "subspc.sub_speciality_name as \"subDepartment\", "
							 + "config.duration as \"duration\", "
							 + "config.status as \"status\",  "
							 + "config.measurement_unit_id as \"measurementUnitId\",  "
							// + " config.equipment_id as \"equipmentId\", "
							 + " config.product_category_id as \"productCategoryId\", "
							 + " config.asset_category_id as \"assetCategoryId\", "
							 + "config.unit_id as \"unitId\", "
							 + "config.organisation_id as \"organisationId\", "
							 + "tat.tat_time_name as \"tatTimeName\","
							 + " config.created_by as \"createdBy\", "
							 + "to_char(config.created_date,'DD-MM-YYYY') as \"createdDate\" "
							 + " from service.m_periodicity_service_config config  "
							 + " inner join service.m_service_master srvmst "
							 	+ " on srvmst.service_master_id = config.service_id "
							 + " inner join service.t_unit_service_master_details srvmppr   "
							 	+ " on srvmppr.service_master_id = srvmst.service_master_id  "
							 + " inner join doctor.m_speciality_master spc "
							 	+ " on spc.speciality_id =  srvmst.speciality_id "
							 + " inner join doctor.m_sub_speciality_master subspc "
							 	+ " on subspc.sub_speciality_id = srvmst.sub_speciality_id "
							 + " inner join lab.m_tat_time_master tat "
							 	+ " on tat.tat_time_id=config.measurement_unit_id " 
					 		+" where config.service_id=:serviceId "
					 		+ "and config.unit_id=:unitId "
					 		+ "and config.organisation_id =:orgId "
					 		+ "and config.status = 'A' "
					 		+ "and config.visit_type_id=:visitTypeId "),
	

	@NamedNativeQuery(name = "VALIDATE_PERIODICITY_SERVICE_CONFIG_AVAILABILITY_UPDATE",
					 query = " select distinct config.periodicity_service_config_id as \"periodicityServiceConfigId\", config.service_id as \"serviceId\" , srvmst.service_standard_name as \"serviceName\", srvmst.service_standard_code as \"serviceCode\",  "
							 + " spc.speciality_name as \"department\", subspc.sub_speciality_name as \"subDepartment\", config.duration as \"duration\", config.status as \"status\",  "
							 + "  config.measurement_unit_id as \"measurementUnitId\",  "
							 + "  config.unit_id as \"unitId\", config.organisation_id as \"organisationId\", tat.tat_time_name as \"tatTimeName\","
							 + " config.created_by as \"createdBy\", to_char(config.created_date,'DD-MM-YYYY') as \"createdDate\" "
							 + " from service.m_periodicity_service_config config  "
							 + " inner join service.m_service_master srvmst "
							 + " on srvmst.service_master_id = config.service_id "
							 + " inner join service.t_unit_service_master_details srvmppr   "
							 + " on srvmppr.service_master_id = srvmst.service_master_id  "
							 + " inner join doctor.m_speciality_master spc "
							 + " on spc.speciality_id =  srvmst.speciality_id "
							 + " inner join doctor.m_sub_speciality_master subspc "
							 + " on subspc.sub_speciality_id = srvmst.sub_speciality_id "
							 + " inner join lab.m_tat_time_master tat "
							 + " on tat.tat_time_id=config.measurement_unit_id " 
					 		+" where config.service_id=:serviceId and config.unit_id=:unitId "
					 		+ "and config.organisation_id =:orgId and config.status = 'A' "
					 		+ "and config.visit_type_id=:visitTypeId and config.periodicity_service_config_id =:peroidicityId "),
	
	@NamedNativeQuery(name = "CHECK_IF_PER_SERV_CONFIG_EXISTS_FOR_UPDATE",
					 query = " select exists(select config.periodicity_service_config_id from service.m_periodicity_service_config config "
					 		+" where config.service_id = :serviceId and config.organisation_id = :orgId and config.unit_id = :unitId and config.periodicity_service_config_id <> :configId " 
					 		+" and config.status = 'A' and config.visit_type_id=:visitTypeId) ")
})
@Entity
@Table(name = "m_periodicity_service_config" , schema = "service")
@SequenceGenerator(name = "m_periodicity_service_config_seq" , sequenceName = "service.m_periodicity_service_config_seq" , allocationSize = 1)
public class MPeriodicityServiceConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_periodicity_service_config_seq")
	@Column(name = "periodicity_service_config_id")
	private int periodicityServiceConfigId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "measurement_unit_id")
	private Integer measurementUnitId;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "equipment_id")
	private Integer equipmentId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "product_category_id")
	private Integer productCategoryId;
	
	@Column(name = "asset_category_id")
	private Integer assetCategoryId;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipment_id" , insertable = false , updatable = false , nullable = false)
	private EquipmentMaster equipmentMaster;

	
	
	
	
	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public int getPeriodicityServiceConfigId() {
		return periodicityServiceConfigId;
	}

	public void setPeriodicityServiceConfigId(int periodicityServiceConfigId) {
		this.periodicityServiceConfigId = periodicityServiceConfigId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getMeasurementUnitId() {
		return measurementUnitId;
	}

	public void setMeasurementUnitId(Integer measurementUnitId) {
		this.measurementUnitId = measurementUnitId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public EquipmentMaster getEquipmentMaster() {
		return equipmentMaster;
	}

	public void setEquipmentMaster(EquipmentMaster equipmentMaster) {
		this.equipmentMaster = equipmentMaster;
	}
}
