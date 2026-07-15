package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_DONOR_SCREENING_MAPPER_BY_DONOR_SCREENING_DETAIL_ID",
		query="SELECT donorScreeningDetailMapper.donorScreeningDetailMapperId as donorScreeningDetailMapperId,donorScreeningDetailMapper.donorScreeningDetailId as donorScreeningDetailId,"
				    + " donorScreeningDetailMapper.donorId as donorId,donorScreeningDetailMapper.donorScreeningParameterId as donorScreeningParameterId,"
				    + " donorScreeningDetailMapper.parameterResultStatus as parameterResultStatus,to_char(donorScreeningDetailMapper.donorDeferralTillDate,'mm/dd/yyyy') as donorDeferralTillDate,"
				    + " donorScreeningDetailMapper.status as status,donorScreeningDetailMapper.createdBy as createdBy,to_char(donorScreeningDetailMapper.createdDate,'dd/mm/yyyy') as createdDate,"
				    + " donorScreeningDetailMapper.updatedBy as updatedBy,to_char(donorScreeningDetailMapper.updatedDate, 'dd/mm/yyyy') as updatedDate,donorScreeningDetailMapper.organizationId as organizationId,"
				    + " donorScreeningDetailMapper.unitId as unitId"
					+ " FROM DonorScreeningDetailMapper donorScreeningDetailMapper"
					+ " WHERE donorScreeningDetailMapper.donorScreeningDetailId= :donorScreeningDetailId"
					+ " AND donorScreeningDetailMapper.organizationId= :organizationId"
					+ " AND donorScreeningDetailMapper.unitId= :unitId"
					+ " AND donorScreeningDetailMapper.donorScreeningParameterId= :donorScreeningParameterId "
					),
	
	@NamedQuery(name="GET_DONOR_SCREENING_MAPPER_LIST_BY_DONOR_SCREENING_DETAIL_ID",
	query="SELECT donorScreeningDetailMapper.donorScreeningDetailMapperId as donorScreeningDetailMapperId,donorScreeningDetailMapper.donorScreeningDetailId as donorScreeningDetailId,"
			    + " donorScreeningDetailMapper.donorId as donorId,donorScreeningDetailMapper.donorScreeningParameterId as donorScreeningParameterId,"
			    + " donorScreeningDetailMapper.parameterResultStatus as parameterResultStatus,to_char(donorScreeningDetailMapper.donorDeferralTillDate,'mm/dd/yyyy') as donorDeferralTillDate,"
			    + " donorScreeningDetailMapper.status as status,donorScreeningDetailMapper.createdBy as createdBy,to_char(donorScreeningDetailMapper.createdDate,'dd/mm/yyyy') as createdDate,"
			    + " donorScreeningDetailMapper.updatedBy as updatedBy,to_char(donorScreeningDetailMapper.updatedDate, 'dd/mm/yyyy') as updatedDate,donorScreeningDetailMapper.organizationId as organizationId,"
			    + " donorScreeningDetailMapper.unitId as unitId"
				+ " FROM DonorScreeningDetailMapper donorScreeningDetailMapper"
				+ " WHERE donorScreeningDetailMapper.donorScreeningDetailId= :donorScreeningDetailId"
				+ " AND donorScreeningDetailMapper.organizationId= :organizationId"
				+ " AND donorScreeningDetailMapper.unitId= :unitId"
				)
	
	})

@Entity
@Table(name = "t_donor_screening_details_mapper", schema = "blood_bank")
@SequenceGenerator(name = "seq_t_donor_screening_details_mapper", sequenceName = "blood_bank.t_donor_screening_details_mapper_seq", allocationSize = 1)
public class DonorScreeningDetailMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_donor_screening_details_mapper")
	@Column(name = "donor_screening_detail_mapper_id")
	private Integer donorScreeningDetailMapperId;
	
	@Column(name = "donor_screening_detail_id")
	private Integer donorScreeningDetailId;
	
	@Column(name = "donor_id")
	private Integer donorId;
	
	@Column(name = "donor_screening_parameter_id")
	private Integer donorScreeningParameterId;
	
	@Column(name = "parameter_result_status")
	private Character parameterResultStatus;
	
	@Column(name = "donor_deferral_till_date")
	private Date donorDeferralTillDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getDonorScreeningDetailMapperId() {
		return donorScreeningDetailMapperId;
	}

	public void setDonorScreeningDetailMapperId(Integer donorScreeningDetailMapperId) {
		this.donorScreeningDetailMapperId = donorScreeningDetailMapperId;
	}

	public Integer getDonorScreeningDetailId() {
		return donorScreeningDetailId;
	}

	public void setDonorScreeningDetailId(Integer donorScreeningDetailId) {
		this.donorScreeningDetailId = donorScreeningDetailId;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public Integer getDonorScreeningParameterId() {
		return donorScreeningParameterId;
	}

	public void setDonorScreeningParameterId(Integer donorScreeningParameterId) {
		this.donorScreeningParameterId = donorScreeningParameterId;
	}

	public Character getParameterResultStatus() {
		return parameterResultStatus;
	}

	public void setParameterResultStatus(Character parameterResultStatus) {
		this.parameterResultStatus = parameterResultStatus;
	}

	public Date getDonorDeferralTillDate() {
		return donorDeferralTillDate;
	}

	public void setDonorDeferralTillDate(Date donorDeferralTillDate) {
		this.donorDeferralTillDate = donorDeferralTillDate;
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
	
	
}
