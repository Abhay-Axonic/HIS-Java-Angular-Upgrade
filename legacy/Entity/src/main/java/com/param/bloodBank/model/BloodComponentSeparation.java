package com.param.bloodBank.model;

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
	@NamedNativeQuery(name = "GET_COMPONENT_SEPARATION_LIST_FOR_STOCK_UPDATION",query="select "
			+ " comSepDetails.blood_bag_no AS \"bloodBagNo\", "
			+ " (case when tttd.created_date is not null then to_char(tttd.created_date, 'DD-MM-YYYY') " 
			+ " else to_char( bloodBagCollection.created_date, 'DD-MM-YYYY') " 
			+ " end) as \"collectionDate\", "
			+ " compMstr.blood_component_desc AS \"componentName\" , " 
			+ " compMstr.expiry AS \"expiryInDays\" , "
			+ " to_char( compMstr.created_date, 'YYYY-MM-DD') as \"expiryDate\","
			+ " bloodGroupMaster.blood_group_desc as \"bloodGroup\","
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  tttd.ttd_done_by_id ) is not null " 
			+ "   then " 
			+ "   	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "   	from public.m_employee_master emp " 
			+ "   	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id "  
			+ "   	where user_id =  tttd.ttd_done_by_id ) " 
			+ " else "  
			+ "    (select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "    from doctor.m_doctor_master doctor " 
			+ "    inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ "    where doctor.user_id =  tttd.ttd_done_by_id ) "  
			+ " end) as \"ttdDoneBy\"," 
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  compSep.technician_id ) is not null " 
			+ "   then " 
			+ "   	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "   	from public.m_employee_master emp " 
			+ "   	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id "  
			+ "   	where user_id =  compSep.technician_id ) " 
			+ " else "  
			+ "    (select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "    from doctor.m_doctor_master doctor " 
			+ "    inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ "    where doctor.user_id =  compSep.technician_id ) "  
			+ " end) as \"extractionDoneBy\"," 
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  tbrc.grouping_technician_id ) is not null " 
			+ "   then " 
			+ "   	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "   	from public.m_employee_master emp " 
			+ "   	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id "  
			+ "   	where user_id =  tbrc.grouping_technician_id ) " 
			+ " else "  
			+ "    (select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "    from doctor.m_doctor_master doctor " 
			+ "    inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ "    where doctor.user_id =  tbrc.grouping_technician_id ) "  
			+ " end) as \"redCellGroupingDoneBy\"," 
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  tbrc.serum_technician_id ) is not null " 
			+ "   then " 
			+ "   	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "   	from public.m_employee_master emp " 
			+ "   	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id "  
			+ "   	where user_id =  tbrc.serum_technician_id ) " 
			+ " else "  
			+ "    (select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "    from doctor.m_doctor_master doctor " 
			+ "    inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ "    where doctor.user_id =  tbrc.serum_technician_id ) "  
			+ " end) as \"redSerumDoneBy\"," 
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  tbrc.confirmation_technician_id ) is not null " 
			+ "   then " 
			+ "   	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "   	from public.m_employee_master emp " 
			+ "   	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id "  
			+ "   	where user_id =  tbrc.confirmation_technician_id ) " 
			+ " else "  
			+ "    (select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "    from doctor.m_doctor_master doctor " 
			+ "    inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ "    where doctor.user_id =  tbrc.confirmation_technician_id ) "  
			+ " end) as \"bloodGroupConfirmationDoneBy\","
			+ " compSep.blood_component_separation_id as \"bloodComponentSeparationId\","
			+ " comSepDetails.component_id as \"componentId\", "
			+ " bloodBagCollection.blood_bag_type_id as\"bloodBagTypeId\","
			+ " bloodBagCollection.blood_bank_id as \"bloodBankId\","
			+ " bloodBagCollection.bag_capacity_id as \"bagCapacityId\","
			+ " bloodBagCollection.barcode as \"barcode\", "  
			+ " bloodBagCollection.batch_no as \"batchNo\", " 
			+ " tbrc.confirmation_blood_group_id as \"bloodGroupId\","
			+ " bloodBagCollection.blood_unit_number as \"bloodUnitNumber\","
			+ " bloodBagCollection.segment_number as \"segmentNumber\","
			+ " donor.donor_id as \"donorId\","
			+ " donor.donor_number as \"donorNumber\","
			+ " compSep.red_cell_id as \"redCellId\", "
			+ " bloodBankStock.short_name as \"shortName\"  " 
			+ " FROM blood_bank.t_blood_component_separation compSep "
			+ " INNER join blood_bank.t_blood_component_separation_deatils comSepDetails on comSepDetails.blood_component_separation_id = compSep.blood_component_separation_id "
			+ " INNER join blood_bank.m_blood_component_master compMstr on compMstr.blood_component_id = comSepDetails.component_id "
			+ " INNER join blood_bank.t_blood_red_cell tbrc on tbrc.red_cell_id = compSep.red_cell_id "  
			+ " INNER join blood_bank.t_transfusion_transmitted_diseases tttd on tttd.transfusion_transmitted_diseases_id = compSep.transfusion_transmitted_diseases_id "  
			+ " LEFT join blood_bank.t_blood_bag_collection bloodBagCollection on tttd.blood_bag_collection_id = bloodBagCollection.blood_bag_collection_id "  
			+ " LEFT join blood_bank.t_donor_registration donor on bloodBagCollection.donor_id = donor.donor_id " 
			+ " LEFT join blood_bank.m_blood_bank_stock bloodBankStock on tttd.blood_bank_stock_id = bloodBankStock.blood_bank_stock_id "  
			+ " LEFT join blood_bank.m_blood_bag_capacity_master bloodBagCapacityMaster on bloodBagCapacityMaster.blood_bag_capacity_id "
			+ " = (case when bloodBagCollection.bag_capacity_id is null then bloodBankStock.blood_bag_capacity_id else bloodBagCollection.bag_capacity_id end) "  
			+ " LEFT join blood_bank.m_blood_bank_status_master bloodBankStatusMaster on tbrc.blood_confirmation_status = bloodBankStatusMaster.id " 
			+ " LEFT join blood_bank.m_blood_group_master bloodGroupMaster on tbrc.confirmation_blood_group_id = bloodGroupMaster.blood_group_id "
			+ " where compSep.organization_id = :organizationId "
			+ " AND compSep.unit_id = :unitId "
			+ " AND tbrc.blood_confirmation_status = 1  ") // pending 

})
@Entity
@Table(name = "t_blood_component_separation", schema = "blood_bank")
@SequenceGenerator(name = "blood_component_separation_seq", sequenceName = "blood_bank.blood_component_separation_seq", allocationSize = 1)
public class BloodComponentSeparation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_component_separation_seq")
	@Column(name = "blood_component_separation_id")
	private Integer bloodComponentSeparationId;
	
	@Column(name = "red_cell_id")
	private Integer redCellId;
	
	@Column(name = "transfusion_transmitted_diseases_id")
	private Integer transfusionTransmittedDiseasesId;
	
	@Column(name = "technician_id")
	private Integer technicianId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "bag_capacity")
	private Integer bagCapacity;
	
	@Column(name = "collected_volume")
	private Integer collectedVolume;
	
	@Column(name = "seperation_date")
	private Date seperationDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getBloodComponentSeparationId() {
		return bloodComponentSeparationId;
	}

	public void setBloodComponentSeparationId(Integer bloodComponentSeparationId) {
		this.bloodComponentSeparationId = bloodComponentSeparationId;
	}

	public Integer getRedCellId() {
		return redCellId;
	}

	public void setRedCellId(Integer redCellId) {
		this.redCellId = redCellId;
	}

	public Integer getTransfusionTransmittedDiseasesId() {
		return transfusionTransmittedDiseasesId;
	}

	public void setTransfusionTransmittedDiseasesId(Integer transfusionTransmittedDiseasesId) {
		this.transfusionTransmittedDiseasesId = transfusionTransmittedDiseasesId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getBagCapacity() {
		return bagCapacity;
	}

	public void setBagCapacity(Integer bagCapacity) {
		this.bagCapacity = bagCapacity;
	}

	public Integer getCollectedVolume() {
		return collectedVolume;
	}

	public void setCollectedVolume(Integer collectedVolume) {
		this.collectedVolume = collectedVolume;
	}

	public Date getSeperationDate() {
		return seperationDate;
	}

	public void setSeperationDate(Date seperationDate) {
		this.seperationDate = seperationDate;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
}
