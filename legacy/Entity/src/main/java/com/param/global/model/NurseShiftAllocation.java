package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = "GET_NURSE_ALLOACTION_BY_DETAILS_ID", query = "SELECT nurseShiftAllocation.nurseShiftAllocationId as nurseShiftAllocationId,"
			+ "to_char(nurseShiftAllocation.fromDate,'dd/mm/yyyy') as fromDate," 
			+ "to_char(nurseShiftAllocation.toDate,'dd/mm/yyyy') as toDate,"
			+ " nurseShiftAllocation.wardId as wardId,"
			+ " nurseShiftAllocation.nurseId as nurseId,"
			+ " nurseShiftAllocation.status as status,"
			+ " nurseShiftAllocation.createdBy as createdBy,"
			+ " nurseShiftAllocation.createdDate as createdDate,"
			+ " nurseShiftAllocation.updatedBy as updatedBy ,"
			+ " nurseShiftAllocation.updatedDate as updatedDate,"
			+ " nurseShiftAllocation.orgId as orgId,"
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId as nurseShiftAllocationDetailsId,"
			+ " nurseShiftAllocationDetails.shiftDate as shiftDate,"
			+ " nurseShiftAllocationDetails.shiftId as shiftId,"
			+ " nurseShiftAllocationDetails.nurseId as nurseId,"
			+ " nurseShiftAllocationDetails.createdDate as createdDate,"
			+ " nurseShiftAllocationDetails.updatedDate as updatedDate,"
			+ " nurseShiftAllocationDetails.createdBy as createdBy,"
			+ " nurseShiftAllocationDetails.updatedBy as updatedBy,"
			+ " nurseShiftAllocationDetails.status as status ,"
			+ " nurseShiftAllocationDetails.unitId as unitId ,"
			+ " nurseShiftAllocationDetails.orgId as orgId "
		    + " FROM NurseShiftAllocation nurseShiftAllocation"
			+ " JOIN nurseShiftAllocation.nurseShiftAllocationDetails nurseShiftAllocationDetails"
			+ " WHERE "
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId=:nurseShiftAllocationDetailsId"
			+ " AND nurseShiftAllocation.orgId=:orgId "
			+ " AND nurseShiftAllocation.unitId=:unitId "),

	@NamedQuery(name = "GET_NURSE_ALLOACTION_BY_CODE", query = "SELECT nurseShiftAllocation.nurseShiftAllocationId as nurseShiftAllocationId,"
			+ "to_char(nurseShiftAllocation.fromDate,'dd/mm/yyyy') as fromDate," 
			+ "to_char(nurseShiftAllocation.toDate,'dd/mm/yyyy') as toDate,"
			+ " nurseShiftAllocation.wardId as wardId,"
			+ " nurseShiftAllocation.nurseId as nurseId,"
			+ " nurseShiftAllocation.status as status,"
			+ " nurseShiftAllocation.createdBy as createdBy,"
			+ " nurseShiftAllocation.createdDate as createdDate,"
			+ " nurseShiftAllocation.updatedBy as updatedBy ,"
			+ " nurseShiftAllocation.updatedDate as updatedDate,"
			+ " nurseShiftAllocation.orgId as orgId,"
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId as nurseShiftAllocationDetailsId,"
			+ " nurseShiftAllocationDetails.shiftDate as shiftDate,"
			+ " nurseShiftAllocationDetails.shiftId as shiftId,"
			+ " nurseShiftAllocationDetails.nurseId as nurseId,"
			+ " nurseShiftAllocationDetails.createdDate as createdDate,"
			+ " nurseShiftAllocationDetails.updatedDate as updatedDate,"
			+ " nurseShiftAllocationDetails.createdBy as createdBy,"
			+ " nurseShiftAllocationDetails.updatedBy as updatedBy,"
			+ " nurseShiftAllocationDetails.status as status,"
			+ " nurseShiftAllocationDetails.unitId as unitId,"
			+ " nurseShiftAllocationDetails.orgId as orgId "
		    + " FROM NurseShiftAllocation nurseShiftAllocation "
			+ " JOIN nurseShiftAllocation.nurseShiftAllocationDetails nurseShiftAllocationDetails"
			+ " WHERE nurseShiftAllocation.orgId=:orgId "
			+ " AND nurseShiftAllocation.unitId=:unitId "),
		

	

	@NamedQuery(name = "GET_PAGINATED_NURSE_ALLOACTION_LIST", query = "SELECT nurseShiftAllocation.nurseShiftAllocationId as nurseShiftAllocationId,"
			+ "to_char(nurseShiftAllocation.fromDate,'dd/mm/yyyy') as fromDate," 
			+ "to_char(nurseShiftAllocation.toDate,'dd/mm/yyyy') as toDate,"
			+ " nurseShiftAllocation.wardId as wardId,"
			+ " nurseShiftAllocation.nurseId as nurseId,"
			+ " nurseShiftAllocation.status as status,"
			+ " nurseShiftAllocation.createdBy as createdBy,"
			+ " nurseShiftAllocation.createdDate as createdDate,"
			+ " nurseShiftAllocation.updatedBy as updatedBy ,"
			+ " nurseShiftAllocation.updatedDate as updatedDate,"
			+ " nurseShiftAllocation.orgId as orgId,"
			+ " nurseShiftAllocationDetails.shiftId as shiftId,"
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId as nurseShiftAllocationDetailsId,"
			+ " nurseShiftAllocationDetails.bedId as bedId,"
			+ " nurseShiftAllocationDetails.nurseId as nurseId,"
			+ " nurseShiftAllocationDetails.createdBy as createdBy,"
			+ " nurseShiftAllocationDetails.updatedBy as updatedBy,"
			+ " nurseShiftAllocationDetails.status as status,"
			+ " nurseShiftAllocationDetails.unitId as unitId, "
			+ " nurseShiftAllocationDetails.orgId as orgId "
		    + " FROM NurseShiftAllocation nurseShiftAllocation"
			+ " JOIN nurseShiftAllocation.nurseShiftAllocationDetails nurseShiftAllocationDetails"
			+ " WHERE nurseShiftAllocation.orgId=:orgId "
			+ " AND nurseShiftAllocation.unitId=:unitId"
			+ " AND (to_char(nurseShiftAllocationDetails.shiftDate,'DD-MM-YYYY') between :fromDate and :toDate)"
			+ " ORDER BY nurseShiftAllocationDetailsId DESC"),
	
	/*@NamedQuery(name = "GET_ACTIVE_NURSE_ALLOACTION_LIST", query = "SELECT nurseShiftAllocation.nurseShiftAllocationId as nurseShiftAllocationId,"
			+ "to_char(nurseShiftAllocation.fromDate,'dd/mm/yyyy') as fromDate," 
			+ "to_char(nurseShiftAllocation.toDate,'dd/mm/yyyy') as toDate,"
			+ " nurseShiftAllocation.wardId as wardId,"
			+ " nurseShiftAllocation.nurseId as nurseId,"
			+ " nurseShiftAllocation.status as status,"
			+ " nurseShiftAllocation.createdBy as createdBy,"
			+ " nurseShiftAllocation.createdDate as createdDate,"
			+ " nurseShiftAllocation.updatedBy as updatedBy ,"
			+ " nurseShiftAllocation.updatedDate as updatedDate,"
			+ " nurseShiftAllocation.orgId as orgId,"
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId as nurseShiftAllocationDetailsId,"
			+ " to_char(nurseShiftAllocationDetails.shiftDate,'dd/mm/yyyy') as shiftDate,"
			+ " nurseShiftAllocationDetails.shiftId as shiftId,"
			+ " nurseShiftAllocationDetails.nurseId as nurseId,"
		    + " FROM NurseShiftAllocation nurseShiftAllocation"
			+ " JOIN nurseShiftAllocation.nurseShiftAllocationDetails nurseShiftAllocationDetails"
			+ " WHERE nurseShiftAllocation.orgId=:orgId "
			+ " AND nurseShiftAllocation.unitId=:unitId "
			+ " AND nurseShiftAllocation.status='A' "),*/
	
	@NamedQuery(name = "GET_ACTIVE_NURSE_ALLOACTION_LIST_BY_ID", query = "SELECT nurseShiftAllocation.nurseShiftAllocationId as nurseShiftAllocationId,"
			+ "to_char(nurseShiftAllocation.fromDate,'dd/mm/yyyy') as fromDate," 
			+ "to_char(nurseShiftAllocation.toDate,'dd/mm/yyyy') as toDate,"
			+ " nurseShiftAllocation.wardId as wardId,"
			+ " nurseShiftAllocation.nurseId as nurseId,"
			+ " nurseShiftAllocation.status as status,"
			+ " nurseShiftAllocation.createdBy as createdBy,"
			+ " nurseShiftAllocation.createdDate as createdDate,"
			+ " nurseShiftAllocation.updatedBy as updatedBy ,"
			+ " nurseShiftAllocation.updatedDate as updatedDate,"
			+ " nurseShiftAllocation.orgId as orgId,"
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId as nurseShiftAllocationDetailsId,"
			+ " nurseShiftAllocationDetails.shiftDate as shiftDate,"
			+ " nurseShiftAllocationDetails.shiftId as shiftId,"
			+ " nurseShiftAllocationDetails.nurseId as nurseId,"
			+ " nurseShiftAllocationDetails.createdDate as createdDate,"
			+ " nurseShiftAllocationDetails.updatedDate as updatedDate,"
			+ " nurseShiftAllocationDetails.createdBy as createdBy,"
			+ " nurseShiftAllocationDetails.updatedBy as updatedBy,"
			+ " nurseShiftAllocationDetails.status as status ,"
			+ " nurseShiftAllocationDetails.unitId as unitId, "
			+ " nurseShiftAllocationDetails.orgId as orgId "
		    + " FROM NurseShiftAllocation nurseShiftAllocation"
			+ " JOIN nurseShiftAllocation.nurseShiftAllocationDetails nurseShiftAllocationDetails"
			+ " WHERE nurseShiftAllocation.orgId=:orgId "
			+ " AND nurseShiftAllocation.unitId=:unitId "),
		
	@NamedQuery(name = "GET_CHECK_NURSE_ALLOACTION_BED_BY_SHIFT_ID", query = "SELECT nurseShiftAllocation.nurseShiftAllocationId as nurseShiftAllocationId,"
			+ "to_char(nurseShiftAllocation.fromDate,'dd/mm/yyyy') as fromDate," 
			+ "to_char(nurseShiftAllocation.toDate,'dd/mm/yyyy') as toDate,"
			+ " nurseShiftAllocation.toDate as toDate,"
			+ " nurseShiftAllocation.wardId as wardId,"
			+ " nurseShiftAllocation.nurseId as nurseId,"
			+ " nurseShiftAllocation.status as status,"
			+ " nurseShiftAllocation.createdBy as createdBy,"
			+ " nurseShiftAllocation.createdDate as createdDate,"
			+ " nurseShiftAllocation.updatedBy as updatedBy ,"
			+ " nurseShiftAllocation.updatedDate as updatedDate,"
			+ " nurseShiftAllocation.orgId as orgId,"
			+ " nurseShiftAllocationDetails.nurseShiftAllocationDetailsId as nurseShiftAllocationDetailsId,"
			+ " nurseShiftAllocationDetails.shiftDate as shiftDate,"
			+ " nurseShiftAllocationDetails.shiftId as shiftId,"
			+ " nurseShiftAllocationDetails.nurseId as nurseId,"
			+ " nurseShiftAllocationDetails.createdDate as createdDate,"
			+ " nurseShiftAllocationDetails.updatedDate as updatedDate,"
			+ " nurseShiftAllocationDetails.createdBy as createdBy,"
			+ " nurseShiftAllocationDetails.updatedBy as updatedBy,"
			+ " nurseShiftAllocationDetails.status as status ,"
			+ " nurseShiftAllocationDetails.unitId as unitId, "
			+ " nurseShiftAllocationDetails.orgId as orgId "
		    + " FROM NurseShiftAllocation nurseShiftAllocation"
			+ " JOIN nurseShiftAllocation.nurseShiftAllocationDetails nurseShiftAllocationDetails"
			+ " WHERE "
			+ " nurseShiftAllocationDetails.shiftId=:shiftId"
			+ " AND nurseShiftAllocation.orgId=:orgId "
			+ " AND nurseShiftAllocation.unitId=:unitId "),
})

@NamedNativeQueries({	
@NamedNativeQuery(
		name = "GET_CHECK_NURSE_ALLOACTION_BY_NURSE_ID",
        query ="select  nsa.nurse_shift_allocation_id as \"nurseShiftAllocationId\", to_char(nsa.from_date, 'YYYY-MM-DD') as \"fromDate\", to_char(nsa.to_date, 'YYYY-MM-DD') as \"toDate\", "
				+ " nsa.ward_id as \"wardId\", nsa.nurse_id as \"nurseId\", to_char(ad.shift_date, 'YYYY-MM-DD') as \"shiftDate\", ad.shift_id as \"shiftId\", "
                + " ad.bed_id as \"bedId\" "
				+ " FROM public.m_nurse_shift_allocation nsa  "
				+ " join public.m_nurse_shift_allocation_details ad on nsa.nurse_shift_allocation_id =ad.nurse_shift_allocation_id "
				+ " where nsa.nurse_id=:nurseId AND nsa.ward_id=:wardId AND nsa.org_id=:orgId AND nsa.unit_id=:unitId and nsa.status ='A' " 
				+ " AND (to_char(ad.shift_date, 'YYYY-MM-DD') between :fromdate AND :todate)"),

@NamedNativeQuery(name = "GET_TOTAL_NURSE_ALLOACTION", query = "select count(*) FROM public.m_nurse_shift_allocation   where "
		+ "org_id = :orgId and unit_id = :unitId ") 


})
			
@Entity
@Table(name = "m_nurse_shift_allocation ", schema = "public")
@SequenceGenerator(name = "m_nurse_shift_allocation_seq", sequenceName = "public.m_nurse_shift_allocation_seq", allocationSize = 1)
public class NurseShiftAllocation {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_nurse_shift_allocation_seq")
	@Column(name = "nurse_shift_allocation_id")
	private Integer nurseShiftAllocationId;

	@Column(name = "from_date")
	private Date fromDate;

	@Column(name = "to_date")
	private Date toDate;

	@Column(name = "ward_id")
	private Integer wardId;

	@Column(name = "nurse_id")
	private Integer nurseId;

	@Column(name = "unit_id")
	private Integer unitId;

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

	@Column(name = "org_id")
	private Integer orgId;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nurseShiftAllocation")
	private List<NurseShiftAllocationDetails> nurseShiftAllocationDetails;	
	
	
	public Integer getNurseShiftAllocationId() {
		return nurseShiftAllocationId;
	}

	public void setNurseShiftAllocationId(Integer nurseShiftAllocationId) {
		this.nurseShiftAllocationId = nurseShiftAllocationId;
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

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public List<NurseShiftAllocationDetails> getNurseShiftAllocationDetails() {
		return nurseShiftAllocationDetails;
	}

	public void setNurseShiftAllocationDetails(List<NurseShiftAllocationDetails> nurseShiftAllocationDetails) {
		this.nurseShiftAllocationDetails = nurseShiftAllocationDetails;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	
}
