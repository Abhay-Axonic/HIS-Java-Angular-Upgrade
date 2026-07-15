package com.param.entity.lis.global;

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
	
	@NamedQuery(name = "CHECK_TEMPLATE_SERVICE_MAPPER_AVAILABLE",
	query = "SELECT templateServiceMapper.templateServiceMpperId as templateServiceMpperId,"
		+ " templateServiceMapper.templateId as templateId "
		+ " FROM TemplateServiceMapper templateServiceMapper"
		+ " WHERE templateServiceMapper.organizationId= :organizationId "
		+ " AND templateServiceMapper.unitId= :unitId "
		+"  AND templateServiceMapper.templateId= :templateId "
		+ " AND templateServiceMapper.serviceId= :serviceId"),
	
	/*@NamedQuery(name = "GET_ACTIVE_TEMPLATE_SERVICE_MAPPER_LIST",
	query = "SELECT templateServiceMapper.templateServiceMpperId as templateServiceMpperId,"
	    + " templateServiceMapper.serviceId as serviceId,"
		+ " templateServiceMapper.templateId as templateId,"
		+ " templateServiceMapper.status as status,"
		+ " templateServiceMapper.isDeleted as isDeleted,"
		+ " templateServiceMapper.organizationId as organizationId, "
		+ " templateServiceMapper.unitId as unitId "
		+ " FROM TemplateServiceMapper templateServiceMapper "
		+ " WHERE templateServiceMapper.status= 'A' "
		+ " AND templateServiceMapper.organizationId= :organizationId "
		+ " AND templateServiceMapper.unitId= :unitId "
		+ " AND templateServiceMapper.isDeleted!= 'Y' "
		),*/
	
//	@NamedQuery(name = "GET_DETAILED_SERVICE_LIST",
//	query = "SELECT templateServiceMapper.templateServiceMpperId as templateServiceMpperId,"
//	    + " templateServiceMapper.serviceId as serviceId,"
//		+ " templateServiceMapper.templateId as templateId,"
//		+ " nursingStationMaster.nursingStationDesc AS nursingStationDesc"
//		+ " FROM TemplateServiceMapper templateServiceMapper"
//		+ " INNER JOIN templateServiceMapper.nursingStationMaster nursingStationMaster"
//		+ " WHERE templateServiceMapper.templateId= :templateId "
//		+ " AND templateServiceMapper.organizationId= :organizationId "
//		+ " AND templateServiceMapper.unitId= :unitId "
//		+ " AND templateServiceMapper.isDeleted!= 'Y' "
//		),
//	
	
	@NamedQuery(name = "UPDATE_TEMPLATE_SERVICE_MAPPER_STATUS",
	query = " UPDATE TemplateServiceMapper templateServiceMapper "
			+ " SET templateServiceMapper.status = :status"
			+ " WHERE templateServiceMapper.organizationId= :organizationId "
			+ " AND templateServiceMapper.unitId= :unitId "
			+ " AND templateServiceMapper.serviceId = :serviceId"
		),
	
	@NamedQuery(name = "UPDATE_TEMPLATE_SERVICE_MAPPER",
	query = " UPDATE TemplateServiceMapper templateServiceMapper "
			+ " SET templateServiceMapper.isDeleted = :isDeleted"
			+ " WHERE templateServiceMapper.organizationId= :organizationId "
			+ " AND templateServiceMapper.unitId = :unitId"
			+ " AND templateServiceMapper.serviceId = :serviceId"
		),
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_TEMPLATE_SERVICE_MAPPER_COUNT", 
			query=" select "
					+ "	count(distinct(mppr.service_id)) as \"serviceId\" "
					+ "from "
					+ "	lab.t_template_service_mapper mppr "
					+ "	inner join service.m_service_master sermst on "
					+ "	sermst.service_master_id = mppr.service_id "
					+ "inner join public.t_template_master temp on "
					+ "	temp.template_id = mppr.template_id "
					+ "where "
					+ "	mppr.organization_id =:organizationId "
					+ "	and mppr.unit_id  = :unitId"
					+ " and mppr.is_deleted != 'Y' "
			),
	
	@NamedNativeQuery(name="GET_TEMPLATE_SERVICE_MAPPER_LIST", 
	query=" select distinct(mppr.service_id) as \"serviceId\" ,mppr.status as \"status\" ,sermst.service_standard_name as \"serviceStandardName\" " 
			+" from lab.t_template_service_mapper mppr "
			+" inner join service.m_service_master sermst on sermst.service_master_id=mppr.service_id "
			+" inner join public.t_template_master temp on temp.template_id=mppr.template_id "
			+" where mppr.organization_id= :organizationId "
			+" and mppr.unit_id= :unitId "
			+" and mppr.is_deleted!= 'Y' "
	),
	
	@NamedNativeQuery(name="GET_TEMPLATE_SERVICE_MAPPER_BY_ID", 
	query= " select mppr.template_service_mpper_id as \"templateServiceMpperId\", mppr.service_id as \"serviceId\", mppr.is_deleted as \"isDeleted\" , " 
			+" mppr.template_id as \"templateId\", mppr.organization_id as \"organizationId\", mppr.unit_id as \"unitId\", mppr.speciality_id as \"specialityId\", "
			+" mppr.sub_speciality_id as \"subSpecialityId\" ,splmst.speciality_name as \"specialityName\" , subsplmst.sub_speciality_name as \"subSpecialityName\" ,temp.title as \"title\" , "
			+" mppr.status as \"status\", sermst.service_standard_name as\"serviceStandardName\" " 
			+" from lab.t_template_service_mapper mppr "
			+" inner join public.t_template_master temp on temp.template_id=mppr.template_id "
			+" inner join doctor.m_speciality_master splmst on splmst.speciality_id=mppr.speciality_id " 
			+" inner join doctor.m_sub_speciality_master subsplmst on subsplmst.sub_speciality_id=mppr.sub_speciality_id "
			+" inner join service.m_service_master sermst on sermst.service_master_id=mppr.service_id "
			+" where mppr.organization_id= :organizationId "
			+" and mppr.unit_id= :unitId "
			+" and mppr.service_id= :serviceId "
			+" and mppr.is_deleted!= 'Y' "
	),
	
	@NamedNativeQuery(name = "GET_SERVICE_LIST_BY_SPECIALITY_AND_SUB_SPECIALITY_ID",
	query = "SELECT service.service_master_id as \"serviceId\", "
		+ "speciality.speciality_name as \"specialityName\","
		+ "speciality.speciality_id as \"specialityId\","
		+ "subspeciality.sub_speciality_name as \"subSpecialityName\", "
		+ "subspeciality.sub_speciality_id as \"subSpecialityId\", "
		+ "service.service_standard_name as\"serviceStandardName\", "
		+ "service.status as \"status\"  "
		+ "FROM service.m_service_master service"
		+ " INNER JOIN doctor.m_speciality_master as speciality ON speciality.speciality_id = service.speciality_id"
		+ " INNER JOIN doctor.m_sub_speciality_master as subspeciality ON subspeciality.sub_speciality_id = service.sub_speciality_id"
		+ " WHERE service.organization_id=:organizationId" 
		//+ " AND service.unit_id=1" 
		+ "   AND service.speciality_id =:specialityId"
		+ "   AND service.sub_speciality_id =:subSpecialityId" 
		+ "   AND service.status ='A' "
		+ " ORDER BY service.service_master_id ASC"),
	
	
	@NamedNativeQuery(name = "GET_DETAILED_SERVICE_LIST",
	query = "	select mppr.template_service_mpper_id as \"templateServiceMpperId\", mppr.service_id as \"serviceId\", " 
			+ " mppr.template_id as \"templateId\", mppr.organization_id as \"organizationId\", mppr.unit_id as \"unitId\", "
			+ " mppr.status as \"status\" , ser.service_standard_name as \"serviceStandardName\" , ser.service_standard_code as \"serviceStandardCode\" ,temp.title as \"title\""
			+ " from lab.t_template_service_mapper mppr "
			+ " inner join service.m_service_master ser on ser.service_master_id=mppr.service_id "
			+"  inner join public.t_template_master temp on temp.template_id=mppr.template_id "
			+ "  where mppr.organization_id= :organizationId "
			+ " and mppr.unit_id= :unitId "
			+ " and mppr.service_id= :serviceId "
			+ " and mppr.is_deleted!= 'Y' "),


})

@Entity
@Table(name = "t_template_service_mapper", schema = "lab")
@SequenceGenerator(name = "t_seq_template_service_mapper", sequenceName = "lab.t_seq_template_service_mapper", allocationSize = 1)
public class TemplateServiceMapper {
	@Id
	@Column(name = "template_service_mpper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_template_service_mapper")
	private Integer templateServiceMpperId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "updated_date")
	private Date updatedDate;


	public Integer getTemplateServiceMpperId() {
		return templateServiceMpperId;
	}

	public void setTemplateServiceMpperId(Integer templateServiceMpperId) {
		this.templateServiceMpperId = templateServiceMpperId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}
