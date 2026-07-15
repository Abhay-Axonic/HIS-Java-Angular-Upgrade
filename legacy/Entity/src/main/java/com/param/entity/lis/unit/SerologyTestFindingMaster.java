package com.param.entity.lis.unit;


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
		@NamedQuery(name = "GET_SEROLOGY_BY_SEROLOGY_ID", query = "SELECT serologyTestFindingMaster.serologyId as id,"
				+ " serologyTestFindingMaster.serologyCode as code,"
				+ " serologyTestFindingMaster.serologyTest as desc,"
				+ " serologyTestFindingMaster.status as status,"
				+ " serologyTestFindingMaster.createdBy as createdBy,"
				+ " serologyTestFindingMaster.createdDate as createdDate,"
				+ " serologyTestFindingMaster.updatedBy as updatedBy,"
				+ " serologyTestFindingMaster.orgId as orgId," 
				+ " serologyTestFindingMaster.unitId as unitId,"
				+ " serologyTestFindingMaster.updatedDate as updatedDate" 
				+ " FROM SerologyTestFindingMaster serologyTestFindingMaster"
				+ " WHERE serologyTestFindingMaster.orgId=:orgId " + " AND serologyTestFindingMaster.unitId=:unitId "
				+ " AND serologyTestFindingMaster.serologyId=:serologyId "),

		@NamedQuery(name = "GET_SEROLOGY_BY_CODE", query = "SELECT serologyTestFindingMaster.serologyId as id,"
				+ " serologyTestFindingMaster.serologyCode as code," + " serologyTestFindingMaster.serologyTest as desc,"
				+ " serologyTestFindingMaster.status as status," + " serologyTestFindingMaster.createdBy as createdBy,"
				+ " serologyTestFindingMaster.createdDate as createdDate," + " serologyTestFindingMaster.updatedBy as updatedBy,"
				+ " serologyTestFindingMaster.unitId as unitId," + " serologyTestFindingMaster.updatedDate as updatedDate"
				+ " FROM SerologyTestFindingMaster serologyTestFindingMaster" + " WHERE serologyTestFindingMaster.orgId=:orgId "
				+ " AND serologyTestFindingMaster.unitId=:unitId "
				+ " AND lower(serologyTestFindingMaster.serologyCode) = lower(:code)"),
		
		@NamedQuery(name = "GET_SEROLOGY_BY_DESCRIPTION", query = "SELECT serologyTestFindingMaster.serologyId as id,"
				+ " serologyTestFindingMaster.serologyCode as code," + " serologyTestFindingMaster.serologyTest as desc,"
				+ " serologyTestFindingMaster.status as status," + " serologyTestFindingMaster.createdBy as createdBy,"
				+ " serologyTestFindingMaster.createdDate as createdDate," + " serologyTestFindingMaster.updatedBy as updatedBy,"
				+ " serologyTestFindingMaster.unitId as unitId," + " serologyTestFindingMaster.updatedDate as updatedDate"
				+ " FROM SerologyTestFindingMaster serologyTestFindingMaster" + " WHERE serologyTestFindingMaster.orgId=:orgId "
				+ " AND serologyTestFindingMaster.unitId=:unitId "
				+ " AND lower(serologyTestFindingMaster.serologyTest) = lower(:serologyTest)"),

		@NamedQuery(name = "UPDATE_GET_SEROLOGY_BY_CODE", query = "SELECT serologyTestFindingMaster.serologyId as id,"
				+ " serologyTestFindingMaster.serologyCode as code," + " serologyTestFindingMaster.serologyTest as desc,"
				+ " serologyTestFindingMaster.status as status," + " serologyTestFindingMaster.createdBy as createdBy,"
				+ " serologyTestFindingMaster.createdDate as createdDate," + " serologyTestFindingMaster.updatedBy as updatedBy,"
				+ " serologyTestFindingMaster.unitId as unitId," + " serologyTestFindingMaster.updatedDate as updatedDate"
				+ " FROM SerologyTestFindingMaster serologyTestFindingMaster" + " WHERE serologyTestFindingMaster.orgId=:orgId "
				+ " AND serologyTestFindingMaster.unitId=:unitId "
				+ " AND lower(serologyTestFindingMaster.serologyCode) = lower(:code)"
				+ " AND serologyTestFindingMaster.serologyId <> :serologyId "),
		
		@NamedQuery(name = "CHECK_FOR_UNIQUE_SEROLOGY_DESCRIPTION", query = "SELECT serologyTestFindingMaster.serologyId as id,"
				+ " serologyTestFindingMaster.serologyCode as code," + " serologyTestFindingMaster.serologyTest as desc,"
				+ " serologyTestFindingMaster.status as status," + " serologyTestFindingMaster.createdBy as createdBy,"
				+ " serologyTestFindingMaster.createdDate as createdDate," + " serologyTestFindingMaster.updatedBy as updatedBy,"
				+ " serologyTestFindingMaster.unitId as unitId," + " serologyTestFindingMaster.updatedDate as updatedDate"
				+ " FROM SerologyTestFindingMaster serologyTestFindingMaster" + " WHERE serologyTestFindingMaster.orgId=:orgId "
				+ " AND serologyTestFindingMaster.unitId=:unitId "
				+ " AND lower(serologyTestFindingMaster.serologyTest) = lower(:serologyTest)"
				+ " AND serologyTestFindingMaster.serologyId <> :serologyId "),

		@NamedQuery(name = "GET_PAGINATED_SEROLOGY_MASTER_LIST", query = "SELECT serologyTestFindingMaster.serologyId as id,"
				+ " serologyTestFindingMaster.serologyCode as code," + " serologyTestFindingMaster.serologyTest as desc,"
				+ " serologyTestFindingMaster.status as status," + " serologyTestFindingMaster.createdBy as createdBy,"
				+ " serologyTestFindingMaster.createdDate as createdDate," + " serologyTestFindingMaster.updatedBy as updatedBy,"
				+ " serologyTestFindingMaster.unitId as unitId," + " serologyTestFindingMaster.updatedDate as updatedDate"
				+ " FROM SerologyTestFindingMaster serologyTestFindingMaster" + " WHERE serologyTestFindingMaster.orgId=:orgId "
				+ " AND serologyTestFindingMaster.unitId=:unitId " + " ORDER BY id DESC")

})
@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_TOTAL_SEROLOGY_RECORD", query = "select count(*) from lab.m_serology_test_finding_master where "
				+ "org_id = :orgId and org_unit_id = :unitId ") })

@Entity
@Table(name = "m_serology_test_finding_master", schema = "lab")
@SequenceGenerator(name = "m_serology_test_finding_master_seq", sequenceName = "lab.m_serology_test_finding_master_seq", allocationSize = 1)
public class SerologyTestFindingMaster {
	@Id
	@Column(name = "serology_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_serology_test_finding_master_seq")
	private Integer serologyId;
	@Column(name = "serology_code")
	private String serologyCode;

	@Column(name = "serology_test")
	private String serologyTest;

	@Column(name = "serology_status")
	private char status;

	@Column(name = "crated_by")
	private int createdBy;

	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long updatedDate;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer unitId;

	public Integer getSerologyId() {
		return serologyId;
	}

	public void setSerologyId(Integer serologyId) {
		this.serologyId = serologyId;
	}

	public String getSerologyCode() {
		return serologyCode;
	}

	public void setSerologyCode(String serologyCode) {
		this.serologyCode = serologyCode;
	}

	public String getSerologyTest() {
		return serologyTest;
	}

	public void setSerologyTest(String serologyTest) {
		this.serologyTest = serologyTest;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
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
