package com.param.adt.admission.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.BedCategoryMaster;
import com.param.global.model.VisitTypeMaster;

@NamedQueries({
	@NamedQuery(name="GET_VISITOR_PASS_MAPPER_LIST",
			query="SELECT mppr.visitorPassMapperId as visitorPassMapperId , mppr.bedCategoryId as bedCategoryId,bedCatMstr.bedCategoryDesc as bedCategoryDesc,"
					+ "mppr.visitorTypePassId as visitorTypePassId,vtm.visitTypeName as visitorPassTypeDesc ,mppr.passTypeId as passTypeId,vptm.visitorPassTypeDesc as passDesc,mppr.noOfPass as noOfPass,mppr.status as status "
					+ " FROM VisitorPassMapper mppr inner join mppr.bedCategoryMaster bedCatMstr"
					+ " inner join mppr.visitorPassTypeMaster vptm "
					+ " inner join mppr.visitTypeMaster vtm "
					+ " WHERE mppr.unitId=:unitId AND mppr.organisationId=:orgId"
					+ " ORDER BY bedCatMstr.bedCategoryDesc ASC "),
	
	@NamedQuery(name="GET_VISITOR_PASS_MAPPER_BY_ID",
	query="SELECT mppr.visitorPassMapperId as visitorPassMapperId , mppr.bedCategoryId as bedCategoryId,bedCatMstr.bedCategoryDesc as bedCategoryDesc,"
			+ "mppr.visitorTypePassId as visitorTypePassId,vtm.visitTypeName as visitorPassTypeDesc ,mppr.passTypeId as passTypeId,vptm.visitorPassTypeDesc as passDesc,mppr.noOfPass as noOfPass,mppr.status as status "
			+ " FROM VisitorPassMapper mppr inner join mppr.bedCategoryMaster bedCatMstr"
			+ " inner join mppr.visitorPassTypeMaster vptm "
			+ " inner join mppr.visitTypeMaster vtm "
			+ " WHERE mppr.visitorPassMapperId=:id"),
	@NamedQuery(name="CHECK_VISITOR_PASS_MAPPER_ALREADY_EXIST_OR_NOT",
	query="SELECT mppr.visitorPassMapperId as visitorPassMapperId "
			+ " FROM VisitorPassMapper mppr "			
			+ " WHERE mppr.unitId=:unitId AND mppr.organisationId=:orgId"
			+ " AND mppr.bedCategoryId=:bedCategoryId AND mppr.visitorTypePassId=:visitorPassTypeId AND mppr.passTypeId=:passTypeId"),
	@NamedQuery(name="CHECK_VISITOR_PASS_MAPPER_ALREADY_EXIST_OR_NOT_BY_ID",
	query="SELECT mppr.visitorPassMapperId as visitorPassMapperId "
			+ " FROM VisitorPassMapper mppr "			
			+ " WHERE mppr.unitId=:unitId AND mppr.organisationId=:orgId"
			+ " AND mppr.bedCategoryId=:bedCategoryId "
			+ " AND mppr.visitorTypePassId=:visitorPassTypeId "
			+ " AND mppr.passTypeId=:passTypeId"
			+ " AND mppr.visitorPassMapperId!=:id"),
	@NamedQuery(name="GET_VISITORPASSCOUNT_BY_BEDCATEGORY_BY_VISITORPASSID_BY_PASSID",
	query="SELECT count(mppr.noOfPass) as noOfPassCount "
			+ " FROM VisitorPassMapper mppr "			
			+ " WHERE mppr.unitId=:unitId AND mppr.organisationId=:orgId"
			+ " AND mppr.bedCategoryId=:bedCategoryId "
			+ " AND mppr.visitorTypePassId=:visitorPassTypeId "
			+ " AND mppr.passTypeId=:passTypeId AND mppr.status='A'")
})
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_VISITOR_PASS_LIST_BY_BEDCAT_VISITTYPE",
			query = " select mstr.visitor_pass_type_id as \"visitorPassTypeId\" ,mstr.visitor_pass_type_desc as \"visitorPassTypeDesc\" ,mstr.visitor_pass_type_code as \"visitorPassTypeCode\" " + 
					" from public.t_visitor_pass_mapper  mpr " + 
					" inner join adt.m_visitor_pass_type_master mstr on mpr.pass_type_id=mstr.visitor_pass_type_id " + 
					" where bed_category_id =:bedCategoryId " + 
					" and visitor_type_pass_id=:visitId " + 
					" and mstr.unit_id=:unitId and mpr.unit_id=:unitId " + 
					" and mstr.organization_id=:orgId and mpr.organisation_id=:orgId " + 
					" and  mstr.status='A'")
})
@Entity
@Table(name="t_visitor_pass_mapper ",schema="public")
@SequenceGenerator(name="visitor_mapper_seq",sequenceName="public.visitor_mapper_seq",allocationSize=1)
public class VisitorPassMapper {
	@Id
	@Column(name="visitor_pass_mapper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="visitor_mapper_seq")
	private Integer visitorPassMapperId;
	
	@Column(name="bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name="visitor_type_pass_id")
	private Integer visitorTypePassId;
	
	@Column(name="pass_type_id")
	private Integer passTypeId;
	
	@Column(name="no_of_pass")
	private Integer noOfPass;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "bed_category_id", insertable = false, updatable = false)
	private BedCategoryMaster bedCategoryMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "pass_type_id", insertable = false, updatable = false)
	private VisitorPassTypeMaster visitorPassTypeMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "visitor_type_pass_id", insertable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;

	public Integer getVisitorPassMapperId() {
		return visitorPassMapperId;
	}

	public void setVisitorPassMapperId(Integer visitorPassMapperId) {
		this.visitorPassMapperId = visitorPassMapperId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getVisitorTypePassId() {
		return visitorTypePassId;
	}

	public void setVisitorTypePassId(Integer visitorTypePassId) {
		this.visitorTypePassId = visitorTypePassId;
	}

	public Integer getPassTypeId() {
		return passTypeId;
	}

	public void setPassTypeId(Integer passTypeId) {
		this.passTypeId = passTypeId;
	}

	public Integer getNoOfPass() {
		return noOfPass;
	}

	public void setNoOfPass(Integer noOfPass) {
		this.noOfPass = noOfPass;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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
		
	
}
