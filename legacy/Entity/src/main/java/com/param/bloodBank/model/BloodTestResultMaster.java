package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


	@NamedQueries({
		
		@NamedQuery(name="GET_BLOOD_TEST_RESULT_MASTER_LIST",
			query=" SELECT bloodTestResultMaster.bloodTestResultId AS bloodTestResultId, bloodTestResultMaster.bloodTestResultCode AS bloodTestResultCode,"
					+ " bloodTestResultMaster.bloodTestResultDesc AS bloodTestResultDesc,  bloodTestResultMaster.organizationId AS organizationId,"
					+ " bloodTestResultMaster.unitId AS unitId, bloodTestResultMaster.status AS status "
					+ " FROM BloodTestResultMaster bloodTestResultMaster"
					+ " WHERE bloodTestResultMaster.organizationId= :organizationId"
					+ " AND bloodTestResultMaster.unitId= :unitId"
					+ " ORDER BY bloodTestResultMaster.bloodTestResultId asc "
					),
		
		@NamedQuery(name="GET_BLOOD_TEST_RESULT_MASTER_BY_ID",
			query=" SELECT bloodTestResultMaster.bloodTestResultId AS bloodTestResultId, bloodTestResultMaster.bloodTestResultCode AS bloodTestResultCode,"
					+ " bloodTestResultMaster.bloodTestResultDesc AS bloodTestResultDesc,  bloodTestResultMaster.organizationId AS organizationId,"
					+ " bloodTestResultMaster.unitId AS unitId, bloodTestResultMaster.status AS status "
					+ " FROM BloodTestResultMaster bloodTestResultMaster"
					+ " WHERE bloodTestResultMaster.bloodTestResultId= :bloodTestResultId"
					+ " AND bloodTestResultMaster.organizationId= :organizationId"
					+ " AND bloodTestResultMaster.unitId= :unitId"
					),
		
		@NamedQuery(name="GET_ACTIVE_BLOOD_TEST_RESULT_MASTER",
			query=" SELECT bloodTestResultMaster.bloodTestResultId AS bloodTestResultId, bloodTestResultMaster.bloodTestResultCode AS bloodTestResultCode,"
					+ " bloodTestResultMaster.bloodTestResultDesc AS bloodTestResultDesc,  bloodTestResultMaster.organizationId AS organizationId,"
					+ " bloodTestResultMaster.unitId AS unitId, bloodTestResultMaster.status AS status "
					+ " FROM BloodTestResultMaster bloodTestResultMaster"
					+ " WHERE bloodTestResultMaster.status= 'A'"
					+ " AND bloodTestResultMaster.organizationId= :organizationId"
					+ " AND bloodTestResultMaster.unitId= :unitId"
					+ " ORDER BY bloodTestResultMaster.bloodTestResultId asc "
					),
		
		@NamedQuery(name="GET_BLOOD_TEST_RESULT_MASTER_BY_NAME",
			query=" SELECT bloodTestResultMaster.bloodTestResultId AS bloodTestResultId "
					+ " FROM BloodTestResultMaster bloodTestResultMaster"
					+ " WHERE LOWER(bloodTestResultMaster.bloodTestResultDesc)= :bloodTestResultDesc"
					),
		
		@NamedQuery(name="GET_BLOOD_TEST_RESULT_MASTER_BY_NAME_NOT_ID",
			query=" SELECT bloodTestResultMaster.bloodTestResultId AS bloodTestResultId "
					+ " FROM BloodTestResultMaster bloodTestResultMaster"
					+ " WHERE bloodTestResultMaster.bloodTestResultId!= :bloodTestResultId"
					+ " AND LOWER(bloodTestResultMaster.bloodTestResultDesc)= :bloodTestResultDesc"
					),
		
		
			
	})
	@Entity
	@Table(name = "m_blood_test_result_master", schema = "blood_bank")
	@SequenceGenerator(name = "seq_m_blood_test_result_master", sequenceName = "blood_bank.seq_m_blood_test_result_master", allocationSize = 1)
	public class BloodTestResultMaster {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_test_result_master")
		
		@Column(name = "blood_test_result_id")
		private Integer bloodTestResultId;
		
		@Column(name = "blood_test_result_code")
		private String bloodTestResultCode;
		
		@Column(name = "blood_test_result_desc")
		private String bloodTestResultDesc;
		
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

		public Integer getBloodTestResultId() {
			return bloodTestResultId;
		}

		public void setBloodTestResultId(Integer bloodTestResultId) {
			this.bloodTestResultId = bloodTestResultId;
		}

		public String getBloodTestResultCode() {
			return bloodTestResultCode;
		}

		public void setBloodTestResultCode(String bloodTestResultCode) {
			this.bloodTestResultCode = bloodTestResultCode;
		}

		public String getBloodTestResultDesc() {
			return bloodTestResultDesc;
		}

		public void setBloodTestResultDesc(String bloodTestResultDesc) {
			this.bloodTestResultDesc = bloodTestResultDesc;
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

