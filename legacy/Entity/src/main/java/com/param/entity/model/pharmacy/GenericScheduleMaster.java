package com.param.entity.model.pharmacy;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.param.entity.lis.common.LocalTimeConverter;

@NamedQueries({

		
})
@NamedNativeQueries({
	  @NamedNativeQuery(name = "GET_GENERIC_SCHEDULE_BY_SCHEDULE_ID", 
			  query = "select gsm.generic_schedule_id as \"genericScheduleId\",gsm.status as\"status\", gen.name as \"name\",sm.schedule_description as\"scheduleDescription\",gsm.generic_id as \"genericId\", "
			  		+ "	gsm.schedule_id as \"scheduleId\",gsm.is_emergency_medicine as \"isEmergencyMedicine\" ,gsm.is_investgation as \"isInvestgation\" ,"
			  		+ " gsm.org_id as \"orgId\",gsm.unit_id as \"unitId\" "
			  		+ " from pharmacy.m_generic_schedule_master gsm "
			  		+ " join pharmacy.m_schedule_master sm  on gsm.schedule_id=sm.schedule_id "
			  		+ " join public.m_generic gen on gsm.generic_id=gen.id "
			        + " where gsm.org_id = :orgId AND gsm.unit_id = :unitId "
			        + " AND gsm.generic_schedule_id=:genericScheduleId "),
	 
	  @NamedNativeQuery(name = "GET_GENERIC_SCHEDULE_BY_CODE", 
	  query = "select gsm.generic_schedule_id as \"genericScheduleId\",gsm.status as\"status\", gen.name as \"name\",sm.schedule_description as\"scheduleDescription\",gsm.generic_id as \"genericId\", "
	  		+ "	gsm.schedule_id as \"scheduleId\",gsm.is_emergency_medicine as \"isEmergencyMedicine\" ,gsm.is_investgation as \"isInvestgation\" "
	  		+ " ,gsm.org_id as \"orgId\",gsm.unit_id as \"unitId\" "
	  		+ " from pharmacy.m_generic_schedule_master gsm "
	  		+ " join pharmacy.m_schedule_master sm  on gsm.schedule_id=sm.schedule_id "
	  		+ " join public.m_generic gen on gsm.generic_id=gen.id "
	        + " where gsm.org_id = :orgId and gsm.unit_id = :unitId "
	        + " and gsm.generic_schedule_id=:genericScheduleId "),
	  
	  @NamedNativeQuery(name = "UPDATE_GET_GENERIC_SCHEDULE_BY_CODE", 
	  query = "select  gsm.generic_schedule_id as \"genericScheduleId\" ,gsm.status as\"status\",gen.name as \"name\",sm.schedule_description as\"scheduleDescription\",gsm.generic_id as \"genericId\", "
	  		+ "	gsm.schedule_id as \"scheduleId\",gsm.is_emergency_medicine as \"isEmergencyMedicine\" ,gsm.is_investgation as \"isInvestgation\", "
	  		+ " gsm.org_id as \"orgId\",gsm.unit_id as \"unitId\" "
	  		+ " from pharmacy.m_generic_schedule_master gsm "
	  		+ " join pharmacy.m_schedule_master sm  on gsm.schedule_id=sm.schedule_id "
	  		+ " join public.m_generic gen on gsm.generic_id=gen.id "
	        + " where gsm.org_id = :orgId and gsm.unit_id = :unitId "
	        + " and gsm.generic_schedule_id<> :genericScheduleId "),
	  
	  
	  @NamedNativeQuery(name = "GET_PAGINATED_GENERIC_SCHEDULE_MASTER_LIST", 
	  query = "select gsm.generic_schedule_id as \"genericScheduleId\" ,gsm.status as\"status\",gen.name as \"name\",sm.schedule_description as\"scheduleDescription\",gsm.generic_id as \"genericId\", "
	  		+ "	gsm.schedule_id as \"scheduleId\",gsm.is_emergency_medicine as \"isEmergencyMedicine\" ,gsm.is_investgation as \"isInvestgation\" "
	  		+ " ,gsm.org_id as \"orgId\",gsm.unit_id as \"unitId\" "
	  		+ " from pharmacy.m_generic_schedule_master gsm "
	  		+ " join pharmacy.m_schedule_master sm  on gsm.schedule_id=sm.schedule_id "
	  		+ " join public.m_generic gen on gsm.generic_id=gen.id "
	        + " where gsm.org_id = :orgId and gsm.unit_id = :unitId "
	  		+ " ORDER BY sm.schedule_description ASC "
	       /* + " gsm.generic_schedule_id=:genericScheduleId "*/
	       ),
	/*  + " ORDER BY generic_schedule_id DESC "*/
	 	@NamedNativeQuery(name = "GET_TOTAL_GENERIC_SCHEDULE_RECORD", query = "select count(*) from pharmacy.m_generic_schedule_master where "
				+ "org_id = :orgId and unit_id = :unitId ")
	  })

@Entity
@Table(name = "m_generic_schedule_master", schema = "pharmacy")
@SequenceGenerator(name = "m_generic_schedule_master_generic_schedule_id_seq", sequenceName = "pharmacy.m_generic_schedule_master_generic_schedule_id_seq", allocationSize = 1)
public class GenericScheduleMaster {
	@Id
	@Column(name = "generic_schedule_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_generic_schedule_master_generic_schedule_id_seq")
	private Integer genericScheduleId;
	
	@Column(name = "generic_id")
	private Integer genericId;

	@Column(name = "schedule_id")
	private Integer scheduleId;
	
	@Column(name = "is_emergency_medicine")
	private String isEmergencyMedicine;
	
	@Column(name = "is_investgation")
	private String isInvestgation;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "crated_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getGenericScheduleId() {
		return genericScheduleId;
	}

	public void setGenericScheduleId(Integer genericScheduleId) {
		this.genericScheduleId = genericScheduleId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}


	public String getIsEmergencyMedicine() {
		return isEmergencyMedicine;
	}

	public void setIsEmergencyMedicine(String isEmergencyMedicine) {
		this.isEmergencyMedicine = isEmergencyMedicine;
	}

	public String getIsInvestgation() {
		return isInvestgation;
	}

	public void setIsInvestgation(String isInvestgation) {
		this.isInvestgation = isInvestgation;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	
}
