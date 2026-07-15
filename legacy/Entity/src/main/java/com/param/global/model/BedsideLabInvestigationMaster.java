package com.param.global.model;

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

import com.param.entity.model.master.StrengthUnit;


@Entity@NamedQueries({
		
		@NamedQuery(name="GET_BEDSIDE_LAB_INVESTIGATION_BY_ID",
			query="SELECT bedsideLabInvestigationMaster.bedsideLabId AS bedsideLabId, "
					   + "bedsideLabInvestigationMaster.bedsideLabCode AS bedsideLabCode, "
					   + "bedsideLabInvestigationMaster.bedsideLabDesc AS bedsideLabDesc,"
					   + "bedsideLabInvestigationMaster.maxReferenceRange as maxReferenceRange, "
					   + " bedsideLabInvestigationMaster.minReferenceRange as minReferenceRange, "
					   + "bedsideLabInvestigationMaster.bedsideUnitId as bedsideUnitId, "
				    	+ " bedsideLabInvestigationMaster.organizationId AS organizationId, "
						+ "bedsideLabInvestigationMaster.unitId AS unitId, "
						+ "bedsideLabInvestigationMaster.status AS status "
						+ "	FROM BedsideLabInvestigationMaster bedsideLabInvestigationMaster"
						+ " WHERE bedsideLabInvestigationMaster.bedsideLabId= :bedsideLabId"
						+ " AND bedsideLabInvestigationMaster.organizationId= :organizationId"
						+ " AND bedsideLabInvestigationMaster.unitId= :unitId"
						),
		
		@NamedQuery(name="GET_ACTIVE_BEDSIDE_LAB_INVESTIGATION",
			query="SELECT bedsideLabInvestigationMaster.bedsideLabId AS bedsideLabId, "
				     	+ "bedsideLabInvestigationMaster.bedsideLabCode AS bedsideLabCode, "
					    + "bedsideLabInvestigationMaster.bedsideLabDesc AS bedsideLabDesc,"
					    + "bedsideLabInvestigationMaster.maxReferenceRange as maxReferenceRange, "
					    + "bedsideLabInvestigationMaster.minReferenceRange as minReferenceRange, "
					    + "bedsideLabInvestigationMaster.bedsideUnitId as bedsideUnitId , "
						+ " bedsideLabInvestigationMaster.organizationId AS organizationId, bedsideLabInvestigationMaster.unitId AS unitId, bedsideLabInvestigationMaster.status AS status"
						+ "	FROM BedsideLabInvestigationMaster bedsideLabInvestigationMaster"
						+ " WHERE bedsideLabInvestigationMaster.status= 'A'"
						+ " AND bedsideLabInvestigationMaster.organizationId= :organizationId"
						+ " AND bedsideLabInvestigationMaster.unitId= :unitId"
						+ " ORDER BY bedsideLabInvestigationMaster.bedsideLabId asc"
						),
		
		@NamedQuery(name="GET_BEDSIDE_LAB_INVESTIGATION_LIST",
		query="SELECT bedsideLabInvestigationMaster.bedsideLabId AS bedsideLabId, "
			     	+ "bedsideLabInvestigationMaster.bedsideLabCode AS bedsideLabCode, "
				    + "bedsideLabInvestigationMaster.bedsideLabDesc AS bedsideLabDesc,"
				    + "bedsideLabInvestigationMaster.maxReferenceRange as maxReferenceRange, "
				    + "bedsideLabInvestigationMaster.minReferenceRange as minReferenceRange, "
				    + "bedsideLabInvestigationMaster.bedsideUnitId as bedsideUnitId , "
				    + " strengthUnitMaster.units as unitName , "
					+ " bedsideLabInvestigationMaster.organizationId AS organizationId, bedsideLabInvestigationMaster.unitId AS unitId, bedsideLabInvestigationMaster.status AS status"
					+ "	FROM BedsideLabInvestigationMaster bedsideLabInvestigationMaster "
					+ " INNER JOIN bedsideLabInvestigationMaster.strengthUnitMaster strengthUnitMaster"
					+ " WHERE bedsideLabInvestigationMaster.organizationId= :organizationId"
					+ " AND bedsideLabInvestigationMaster.unitId= :unitId"
					+ " ORDER BY bedsideLabInvestigationMaster.bedsideLabId asc"
					),
		
	@NamedQuery(name="GET_BED_SIDE_LAB_INVESTIGATION_BY_NAME",
		query="SELECT bedsideLabInvestigationMaster.bedsideLabId AS bedsideLabId "
					+ "	FROM BedsideLabInvestigationMaster bedsideLabInvestigationMaster"
					+ " WHERE LOWER(bedsideLabInvestigationMaster.bedsideLabDesc)= :bedsideLabDesc"
					),
	
	
	@NamedQuery(name="GET_BEDSIDE_LAB_INVESTIGATION_BY_NAME_NOT_ID",
	query="SELECT bedsideLabInvestigationMaster.bedsideLabId AS bedsideLabId"
			+ "	FROM BedsideLabInvestigationMaster bedsideLabInvestigationMaster"
				+ " WHERE LOWER(bedsideLabInvestigationMaster.bedsideLabDesc)= :bedsideLabDesc"
				+ " AND bedsideLabInvestigationMaster.bedsideLabId != :bedsideLabId"
				),
	
	})


@Table(name = "m_bedside_lab_investigation_master", schema = "public")
@SequenceGenerator(name = "m_bedside_lab_investigation_master_seq", sequenceName = "public.m_bedside_lab_investigation_master_seq", allocationSize = 1)

public class BedsideLabInvestigationMaster {

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_bedside_lab_investigation_master_seq")
		
		@Column(name = "bedside_lab_id")
		private Integer bedsideLabId;
		
		@Column(name = "bedside_lab_code")
		private String bedsideLabCode;
		
		@Column(name = "bedside_lab_desc")
		private String bedsideLabDesc;
		
		@Column(name = "max_reference_range")
		private Integer maxReferenceRange;
		
		@Column(name = "min_reference_range")
		private Integer minReferenceRange;
		
		@Column(name = "bedside_unit_id")
		private Integer bedsideUnitId;
		
		@Column(name = "organization_id")
		private Integer organizationId;
		
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
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="bedside_unit_id", insertable=false, updatable=false)
		private StrengthUnit strengthUnitMaster ;

		public Integer getBedsideLabId() {
			return bedsideLabId;
		}

		public void setBedsideLabId(Integer bedsideLabId) {
			this.bedsideLabId = bedsideLabId;
		}

		public String getBedsideLabCode() {
			return bedsideLabCode;
		}

		public void setBedsideLabCode(String bedsideLabCode) {
			this.bedsideLabCode = bedsideLabCode;
		}

		public String getBedsideLabDesc() {
			return bedsideLabDesc;
		}

		public void setBedsideLabDesc(String bedsideLabDesc) {
			this.bedsideLabDesc = bedsideLabDesc;
		}

		public Integer getMaxReferenceRange() {
			return maxReferenceRange;
		}

		public void setMaxReferenceRange(Integer maxReferenceRange) {
			this.maxReferenceRange = maxReferenceRange;
		}

		public Integer getMinReferenceRange() {
			return minReferenceRange;
		}

		public void setMinReferenceRange(Integer minReferenceRange) {
			this.minReferenceRange = minReferenceRange;
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

		public Integer getBedsideUnitId() {
			return bedsideUnitId;
		}

		public void setBedsideUnitId(Integer bedsideUnitId) {
			this.bedsideUnitId = bedsideUnitId;
		}
		
		
}
