package com.param.global.model;

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
	@NamedNativeQuery(name = "GET_ROOM_RENT_CONFIG_BY_ORG_UNIT_BEDCATEGORY",
				query= " select	rrc.room_rent_charges_config_id as \"roomRentChargesConfigId\", rrc.bed_category_id as \"bedCategoryId\",  "
						+ " rrc.bed_charges_service_id as \"bedChargesServiceId\", rrc.nursing_charges_service_id as \"nursingChargesServiceId\",  "
						+ " rrc.meal_charges_service_id as \"mealChargesServiceId\", rrc.organisation_id as \"organisationId\", rrc.unit_id as \"unitId\", "
						+ " (select srv.speciality_id  from service.m_service_master srv where srv.service_master_id = rrc.bed_charges_service_id) as \"bedServSpecialityId\" , "
						+ " (select srv.sub_speciality_id  from service.m_service_master srv where srv.service_master_id = rrc.bed_charges_service_id) as \"bedServSubSpecialityId\", "
						+ "  "
						+ " (select srv.speciality_id  from service.m_service_master srv where srv.service_master_id = rrc.nursing_charges_service_id) as \"nurServSpecialityId\" , "
						+ " (select srv.sub_speciality_id  from service.m_service_master srv where srv.service_master_id = rrc.nursing_charges_service_id) as \"nurServSubSpecialityId\", "
						+ "  "
						+ " (select srv.speciality_id  from service.m_service_master srv where srv.service_master_id = rrc.meal_charges_service_id) as \"mealServSpecialityId\" , "
						+ " (select srv.sub_speciality_id  from service.m_service_master srv where srv.service_master_id = rrc.meal_charges_service_id) as \"mealServSubSpecialityId\" "
						+ "  "
						+ " FROM public.m_room_rent_charges_config rrc "
						+ " where rrc.organisation_id = :orgId and rrc.unit_id = :unitId and rrc.status = 'A' ")
})
@Entity
@Table(name = "m_room_rent_charges_config" , schema = "public")
@SequenceGenerator(name = "m_room_rent_charges_config" , sequenceName = "public.m_room_rent_charges_config" , allocationSize = 1)
public class MRoomRentChargesConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_room_rent_charges_config")
	@Column(name = "room_rent_charges_config_id")
	private int roomRentChargesConfigId;
	
	@Column(name = "bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name = "bed_charges_service_id")
	private Integer bedChargesServiceId;
	
	@Column(name = "nursing_charges_service_id")
	private Integer nursingChargesServiceId;
	
	@Column(name = "meal_charges_service_id")
	private Integer mealChargesServiceId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public int getRoomRentChargesConfigId() {
		return roomRentChargesConfigId;
	}

	public void setRoomRentChargesConfigId(int roomRentChargesConfigId) {
		this.roomRentChargesConfigId = roomRentChargesConfigId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getBedChargesServiceId() {
		return bedChargesServiceId;
	}

	public void setBedChargesServiceId(Integer bedChargesServiceId) {
		this.bedChargesServiceId = bedChargesServiceId;
	}

	public Integer getNursingChargesServiceId() {
		return nursingChargesServiceId;
	}

	public void setNursingChargesServiceId(Integer nursingChargesServiceId) {
		this.nursingChargesServiceId = nursingChargesServiceId;
	}

	public Integer getMealChargesServiceId() {
		return mealChargesServiceId;
	}

	public void setMealChargesServiceId(Integer mealChargesServiceId) {
		this.mealChargesServiceId = mealChargesServiceId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
