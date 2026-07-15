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
	@NamedNativeQuery(name = "GET_RED_CELL_DETAILS",query="select  "
			+ " redCell.red_cell_id as \"redCellId\", transfusion_transmitted_diseases_id as \"transfusionTransmittedDiseasesId\" , "
			+ " grouping_blood_group_id as \"groupingBloodGroupId\", grouping_technician_id as \"groupingTechnicianId\" , "
			+ " grouping_irregular_antibodies as \"groupingIrregularAntibodies\", " 
			+ " result_a as \"resultA\" , result_b as \"resultB\",result_ab as \"resultAB\",result_d as \"resultD\" , "
			+ " result_a1 as \"resultA1\", result_h as \"resultH\",serum_blood_group_id as \"serumBloodGroupId\" , "
			+ " result_a_cell as \"resultACell\",result_b_cell as \"resultBCell\",result_o_cell as \"resultOCell\" , "
			+ " result_ob_cell as \"resultOBCell\", groupingBlood.blood_group_desc as \"groupingBloodGroup\" , serumBlood.blood_group_desc as \"serumBloodGroup\", " 
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  redCell.grouping_technician_id ) is not null " 
			+ "   then " 
			+ "   	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "   	from public.m_employee_master emp " 
			+ "   	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id "  
			+ "   	where user_id =  redCell.grouping_technician_id ) " 
			+ " else "  
			+ "    (select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "    from doctor.m_doctor_master doctor " 
			+ "    inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ "    where doctor.user_id =  redCell.grouping_technician_id ) "  
			+ " end) as \"groupingUserName\"," 
			+ " (case when  (select emp.user_id from public.m_employee_master emp where user_id =  redCell.serum_technician_id  ) is not null "  
			+ "   then "  
			+ "  	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(emp.first_name), ' ', coalesce(emp.middle_name), ' ', coalesce(emp.last_name)) "  
			+ "  	from public.m_employee_master emp "  
			+ "  	inner join public.m_prefix_master prefix on emp.prefix_id = prefix.prefix_id " 
			+ "  	where user_id =  redCell.serum_technician_id  ) " 
			+ " else "  
			+ "  	(select concat(coalesce(prefix.prefix_code), ' ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) "  
			+ "  	from doctor.m_doctor_master doctor "  
			+ "  	inner join public.m_prefix_master prefix on doctor.prefix_id = prefix.prefix_id "  
			+ " 	where doctor.user_id =  redCell.serum_technician_id  ) " 
			+ " end) as \"serumUserName\" "
			+ " from blood_bank.t_blood_red_cell redCell " 
			+ " inner join blood_bank.m_blood_group_master groupingBlood on groupingBlood.blood_group_id = redCell.grouping_blood_group_id "  
			+ " inner join blood_bank.m_blood_group_master serumBlood on serumBlood.blood_group_id = redCell.serum_blood_group_id " 
			+ " inner join public.m_user_master groupingUser on groupingUser.user_id = redCell.grouping_technician_id " 
			+ " inner join public.m_user_master serumUser on serumUser.user_id = redCell.serum_technician_id "
			+ " where redCell.red_cell_id = :redCellId "),
	
	@NamedNativeQuery(name = "GET_RED_CELL_DETAILS_ON_COMPONENT_SEPARATION",query=" select " 
			+ " tbrc.red_cell_id as \"redCellId\", tbrc.transfusion_transmitted_diseases_id as \"transfusionTransmittedDiseasesId\" , "
			+ " tbrc.blood_confirmation_status as \"bloodConfirmationStatus\", " 
			+ " bloodBankStatusMaster.name as \"statusName\"," 
			+ " (case when mstr.created_date is not null then to_char(mstr.created_date, 'DD-MM-YYYY') " 
			+ " else to_char( bloodBagCollection.created_date, 'DD-MM-YYYY') " 
			+ " end) as \"collectionDate\", " 
			+ " (case when bloodBagCollection.blood_bag_type_id is not null then (select mbbtm.blood_bag_type_desc from blood_bank.m_blood_bag_type_master mbbtm where mbbtm.blood_bag_type_id = bloodBagCollection.blood_bag_type_id ) " 
			+ " else (select mbbtm.blood_bag_type_desc from blood_bank.m_blood_bag_type_master mbbtm where mbbtm.blood_bag_type_id = bloodBankStock.blood_bag_type_id ) " 
			+ " end) as \"bloodBagTypeDesc\" , " 
			+ " bloodBagCollection.barcode as \"barcode\", " 
			+ " tbrc.confirmation_blood_group_id as \"confirmationBloodGroupId\" , " 
			+ " mstr.blood_bag_no as \"bloodBagNo\" , " 
			+ " (case when donor.donor_number is not null then donor.donor_number " 
			+ " else bloodBankStock.loan_number " 
			+ " end) as \"donorNumber\", " 
			+ " bloodBagCapacityMaster.blood_bag_capacity_desc as \"bloodBagCapacity\", " 
			+ " (case when bloodBagCollection.blood_unit_number is not null then bloodBagCollection.blood_unit_number " 
			+ " else bloodBankStock.blood_unit " 
			+ " end) as \"bloodUnitNumber\", bloodGroupMaster.blood_group_desc as \"bloodGroup\" " 
			+ " from blood_bank.t_blood_red_cell tbrc " 
			+ " inner join blood_bank.t_transfusion_transmitted_diseases mstr on " 
			+ " tbrc.transfusion_transmitted_diseases_id = mstr.transfusion_transmitted_diseases_id " 
			+ " left join blood_bank.t_blood_bag_collection bloodBagCollection on " 
			+ " mstr.blood_bag_collection_id = bloodBagCollection.blood_bag_collection_id " 
			+ " left join blood_bank.t_donor_registration donor on bloodBagCollection.donor_id = donor.donor_id " 
			+ " left join blood_bank.m_blood_bank_stock bloodBankStock on mstr.blood_bank_stock_id = bloodBankStock.blood_bank_stock_id " 
			+ " left join blood_bank.m_blood_bag_capacity_master bloodBagCapacityMaster on " 
			+ " bloodBagCapacityMaster.blood_bag_capacity_id = (case when bloodBagCollection.bag_capacity_id is null then bloodBankStock.blood_bag_capacity_id else bloodBagCollection.bag_capacity_id end) "
			+ " left join blood_bank.m_blood_bank_status_master bloodBankStatusMaster on tbrc.blood_confirmation_status = bloodBankStatusMaster.id " 
			+ " left join blood_bank.m_blood_group_master bloodGroupMaster on tbrc.confirmation_blood_group_id = bloodGroupMaster.blood_group_id "
			+ "  left join blood_bank.t_blood_component_separation comp on comp.red_cell_id  = tbrc.red_cell_id "
			+ " where tbrc.organization_id =:organizationId "
			+ " AND tbrc.unit_id =:unitId "
			+ " AND  comp.blood_component_separation_id is null "),
	
	})



@Entity
@Table(name="t_blood_red_cell", schema="blood_bank")
@SequenceGenerator(name="blood_red_cell_seq", sequenceName="blood_bank.blood_red_cell_seq", allocationSize= 1)
public class BloodRedCell {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_red_cell_seq")
	@Column(name = "red_cell_id")
	private Integer redCellId;
	
	@Column(name = "transfusion_transmitted_diseases_id")
	private Integer transfusionTransmittedDiseasesId;
	
	@Column(name = "grouping_blood_group_id")
	private Integer groupingBloodGroupId;
	
	@Column(name = "grouping_technician_id")
	private Integer groupingTechnicianId;
	
	@Column(name = "grouping_irregular_antibodies")
	private String groupingIrregularAntibodies;
	
	@Column(name = "checked_cell_grouping")
	private boolean checkedCellGrouping;
	
	@Column(name = "result_a")
	private Character resultA;
	
	@Column(name = "result_b")
	private Character resultB;

	@Column(name = "result_ab")
	private Character resultAB;
	
	@Column(name = "result_d")
	private Character resultD;
	
	@Column(name = "result_a1")
	private Character resultA1;
	
	@Column(name = "result_h")
	private Character resultH;
	
	@Column(name = "grouping_status")
	private Character groupingStatus;
	
	@Column(name = "serum_blood_group_id")
	private Integer serumBloodGroupId;
	
	@Column(name = "serum_technician_id")
	private Integer serumTechnicianId;
	
	@Column(name = "serum_irregular_antibodies")
	private String serumIrregularAntibodies;
	
	@Column(name = "checked_serum_grouping")
	private boolean checkedSerumGrouping;
	
	@Column(name = "result_a_cell")
	private Character resultACell;
	
	@Column(name = "result_b_cell")
	private Character resultBCell;
	
	@Column(name = "result_o_cell")
	private Character resultOCell;
	
	@Column(name = "result_ob_cell")
	private Character resultOBCell;
	
	@Column(name = "serum_status")
	private Character serumStatus;
	
	@Column(name = "confirmation_technician_id")
	private Integer confirmationTechnicianId;
	
	@Column(name = "confirmation_blood_group_id")
	private Integer confirmationBloodGroupId;
	
	@Column(name = "blood_confirmation_status")
	private Integer bloodConfirmationStatus;
	
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

	public Integer getGroupingBloodGroupId() {
		return groupingBloodGroupId;
	}

	public void setGroupingBloodGroupId(Integer groupingBloodGroupId) {
		this.groupingBloodGroupId = groupingBloodGroupId;
	}

	public Integer getGroupingTechnicianId() {
		return groupingTechnicianId;
	}

	public void setGroupingTechnicianId(Integer groupingTechnicianId) {
		this.groupingTechnicianId = groupingTechnicianId;
	}

	public String getGroupingIrregularAntibodies() {
		return groupingIrregularAntibodies;
	}

	public void setGroupingIrregularAntibodies(String groupingIrregularAntibodies) {
		this.groupingIrregularAntibodies = groupingIrregularAntibodies;
	}

	public boolean isCheckedCellGrouping() {
		return checkedCellGrouping;
	}

	public void setCheckedCellGrouping(boolean checkedCellGrouping) {
		this.checkedCellGrouping = checkedCellGrouping;
	}

	public Character getResultA() {
		return resultA;
	}

	public void setResultA(Character resultA) {
		this.resultA = resultA;
	}

	public Character getResultB() {
		return resultB;
	}

	public void setResultB(Character resultB) {
		this.resultB = resultB;
	}

	
	public Character getResultD() {
		return resultD;
	}

	public void setResultD(Character resultD) {
		this.resultD = resultD;
	}

	public Character getResultA1() {
		return resultA1;
	}

	public void setResultA1(Character resultA1) {
		this.resultA1 = resultA1;
	}

	public Character getResultH() {
		return resultH;
	}

	public void setResultH(Character resultH) {
		this.resultH = resultH;
	}

	public Character getGroupingStatus() {
		return groupingStatus;
	}

	public void setGroupingStatus(Character groupingStatus) {
		this.groupingStatus = groupingStatus;
	}

	public Integer getSerumBloodGroupId() {
		return serumBloodGroupId;
	}

	public void setSerumBloodGroupId(Integer serumBloodGroupId) {
		this.serumBloodGroupId = serumBloodGroupId;
	}

	public Integer getSerumTechnicianId() {
		return serumTechnicianId;
	}

	public void setSerumTechnicianId(Integer serumTechnicianId) {
		this.serumTechnicianId = serumTechnicianId;
	}

	public String getSerumIrregularAntibodies() {
		return serumIrregularAntibodies;
	}

	public void setSerumIrregularAntibodies(String serumIrregularAntibodies) {
		this.serumIrregularAntibodies = serumIrregularAntibodies;
	}

	public boolean isCheckedSerumGrouping() {
		return checkedSerumGrouping;
	}

	public void setCheckedSerumGrouping(boolean checkedSerumGrouping) {
		this.checkedSerumGrouping = checkedSerumGrouping;
	}

	public Character getResultACell() {
		return resultACell;
	}

	public void setResultACell(Character resultACell) {
		this.resultACell = resultACell;
	}

	public Character getResultBCell() {
		return resultBCell;
	}

	public void setResultBCell(Character resultBCell) {
		this.resultBCell = resultBCell;
	}

	public Character getResultOCell() {
		return resultOCell;
	}

	public void setResultOCell(Character resultOCell) {
		this.resultOCell = resultOCell;
	}

	public Character getSerumStatus() {
		return serumStatus;
	}

	public void setSerumStatus(Character serumStatus) {
		this.serumStatus = serumStatus;
	}

	public Integer getConfirmationTechnicianId() {
		return confirmationTechnicianId;
	}

	public void setConfirmationTechnicianId(Integer confirmationTechnicianId) {
		this.confirmationTechnicianId = confirmationTechnicianId;
	}

	public Integer getConfirmationBloodGroupId() {
		return confirmationBloodGroupId;
	}

	public void setConfirmationBloodGroupId(Integer confirmationBloodGroupId) {
		this.confirmationBloodGroupId = confirmationBloodGroupId;
	}

	public Integer getBloodConfirmationStatus() {
		return bloodConfirmationStatus;
	}

	public void setBloodConfirmationStatus(Integer bloodConfirmationStatus) {
		this.bloodConfirmationStatus = bloodConfirmationStatus;
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

	public Character getResultAB() {
		return resultAB;
	}

	public void setResultAB(Character resultAB) {
		this.resultAB = resultAB;
	}

	public Character getResultOBCell() {
		return resultOBCell;
	}

	public void setResultOBCell(Character resultOBCell) {
		this.resultOBCell = resultOBCell;
	}
	
}
