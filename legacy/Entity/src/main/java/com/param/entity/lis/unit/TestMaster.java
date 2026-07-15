package com.param.entity.lis.unit;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.global.ContainerMaster;
import com.param.entity.lis.global.PrerequisitesMaster;
import com.param.entity.lis.global.ReportTypeMaster;
import com.param.entity.lis.global.SampleMaster;
import com.param.entity.lis.global.SpecimanMaster;
import com.param.entity.lis.global.TechniqueMaster;
import com.param.entity.lis.global.TurnAroundTimeMaster;
import com.param.global.model.ServiceMaster;
import com.param.global.model.SubSpecialityMaster;

/*Test Master*/

@NamedQueries({

		@NamedQuery(name = "GET_TEST_BY_TEST_ID", query = "SELECT tm" + " FROM	TestMaster tm "
				+ " INNER JOIN tm.listTestParamMppr lstTstParamMppr " + " WHERE	tm.testId =:testId "
				+ " AND lstTstParamMppr.testParaStatus = 'A' AND tm.orgId=:orgId " //AND tm.orgUnitId = :orgUnitId
				+ " AND tm.testStatus = 'A'" 
				+ " AND tm.testType=:testType" 
				+ " AND lstTstParamMppr.isDeleted='N' order by lstTstParamMppr.headerSequence asc"),

		@NamedQuery(name = "GET_TEST_BY_TEST_ID_ON_SINGLE_PARAMETER", query = "SELECT tm" + " FROM	TestMaster tm "
				+ " INNER JOIN tm.listTestParamMppr lstTstParamMppr " + " WHERE	tm.testId =:testId "
				+ " AND tm.orgId=:orgId AND tm.orgUnitId = :orgUnitId "
			//	+ " AND lstTstParamMppr.testParaStatus = 'A'"
			//	+ " AND tm.testStatus = 'A'" 
			//	+ " AND tm.testType=:testType" 
				+ " AND lstTstParamMppr.isDeleted='N' "),
		
		@NamedQuery(name = "GET_TEST_MASTER_LIST", query = "SELECT tm " + " FROM TestMaster tm "
				+ " WHERE tm.orgId=:orgId AND tm.isHistoCyto=:isHistoCyto" + " AND tm.orgUnitId = :orgUnitId" 
				+ " AND tm.testType=:testType "),

		@NamedQuery(name = "GET_TEST_MASTER_LIST_ON_SINGLE_PARAMETER", query = "SELECT tm " + " FROM TestMaster tm "
				+ "  inner join tm.subSpecialityMaster subSpecialityMst"
				+ " WHERE tm.orgId=:orgId AND tm.isHistoCyto=:isHistoCyto" + " AND tm.orgUnitId = :orgUnitId" ),
				//+ " AND tm.testType=:testType "),
		
		@NamedQuery(name = "GET_TEST_BY_TEST_CODE", query = "SELECT tm.testId as testId," + " tm.testCode as testCode "
				+ " FROM	TestMaster tm " + " INNER JOIN tm.listTestParamMppr lstTstParamMppr "
				+ " WHERE	 lower(tm.testCode) = lower(:testCode)"
				+ " AND tm.orgId=:orgId AND tm.orgUnitId = :orgUnitId" + " AND tm.testType=:testType "
				+ " AND lstTstParamMppr.isDeleted='N' "),

		@NamedQuery(name = "GET_TEST_BY_TEST_CODE_UPDATE", query = "SELECT tm.testId as testId,"
				+ " tm.testCode as testCode " + " FROM	TestMaster tm "
				+ " INNER JOIN tm.listTestParamMppr lstTstParamMppr " + " WHERE	 lower(tm.testCode) = lower(:testCode)"
				+ " AND tm.orgId=:orgId AND tm.orgUnitId = :orgUnitId" + " AND tm.testType=:testType  AND tm.testStatus = 'A' "
				+ " AND lstTstParamMppr.isDeleted='N' " + " AND tm.testId <>:testId "),

		@NamedQuery(name = "SEARCH_SPECIMEN_ACCEPTED_TEST", query = " SELECT  " + "	testMaster.testId AS id, "
				+ "	testMaster.testDesc AS label " + "FROM " + "	TestMaster testMaster " + "WHERE "
				+ "	testMaster.orgId = :orgId " + "	AND testMaster.orgUnitId = :orgUnitId "
				+ "	AND testMaster.testStatus='A' "
				+ " order by testMaster.testDesc asc "),

		@NamedQuery(name = "GET_ACTIVE_TEST_LIST", query = " SELECT  " + "	testMaster.testId AS testId, "
				+ "	testMaster.testCode AS testCode, " + "	testMaster.testDesc AS testDesc " + "FROM "
				+ "	TestMaster testMaster " + "WHERE " + "	testMaster.orgId = :orgId "
				+ "	AND testMaster.orgUnitId = :orgUnitId " + "	AND testMaster.testStatus='A' "
						+ " ORDER BY testMaster.testDesc ASC "),

		@NamedQuery(name = "GET_PAGINATED_MULTI_PARAM_TEST_MASTER_DETAILS_LIST_SEARCH",
		query = "SELECT tm  FROM TestMaster tm "
				+ " WHERE tm.orgId=:orgId AND tm.isHistoCyto=:isHistoCyto" 
				+ " AND tm.orgUnitId = :orgUnitId " 
				+ " AND tm.testType=:testType "
				+ " AND (lower(tm.testCode) like :testDesc"
				+ " OR lower(tm.testDesc) like :testDesc "
				+ " OR cast(tm.sampleVolume as text) like :testDesc "
				+ " OR cast(tm.noOfSampleReq as text) like :testDesc "
				+ " OR lower(tm.identificationNo) like :testDesc "
				+ "OR lower(tm.testAlies) like :testDesc )"),
		/*
		 * @NamedQuery(name = "ACTIVATE_INACTIVATE_TEST", query =
		 * "UPDATE TestMaster testMaster" + " SET	testMaster.testStatus =:testStatus "
		 * + " WHERE testMaster.testId = :testId")
		 */

})

@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_TOTAL_TEST_RECORD", query = "SELECT " + "	COUNT(*) " + " FROM "
				+ "	lab.m_test_master testmst " + " WHERE " + "	testmst.org_unit_id = :orgUnitId "
				+ "	AND testmst.org_id = :orgId " + " AND testmst.is_histo_cyto = :isHistoCyto "
				+ " AND testmst.test_type = :testType "),
		
		@NamedNativeQuery(name = "GET_CONT_OF_SAME_TEST_MASTER_WITH_DIFF_STATUS", query = "SELECT " + "COUNT(test_id)" + " FROM " 
		        + " lab.m_test_master testmst" + " WHERE " + "testmst.org_unit_id = :orgUnitId"
				+ " AND testmst.service_id = :serviceId AND testmst.test_status = 'A' "
		        + "AND testmst.test_id <> :testId" ),
		
		
		@NamedNativeQuery(name = "GET_TOTAL_SEROLOGY_TEST_RECORD", query = "SELECT " + "	COUNT(*) " + " FROM "
				+ "	lab.m_test_master testmst "
				+ " inner join doctor.m_sub_speciality_master sub_speciality_mst on testmst.dept_id = sub_speciality_mst.sub_speciality_id "
				+ " WHERE " + "	testmst.org_unit_id = :orgUnitId "
				+ "	AND testmst.org_id = :orgId " + " AND testmst.is_histo_cyto = :isHistoCyto "
				),

		@NamedNativeQuery(name = "GET_TOTAL_TEST_IN_SERVICE_RECORD", query = "SELECT  "
				+ " COUNT(*) FROM lab.m_test_master " + " WHERE org_id=:orgId " + " AND org_unit_id=:orgUnitId "
				+ " AND service_id=:serviceId " + " AND test_status= 'A' "),

		@NamedNativeQuery(name = "GET_SINGLE_PARAM_TEST_LIST", query = "select " + "  test_mst.test_id as \"testId\", "
				+ "  parameter_mst.parameter_id as \"parameterId\", "
				+ "  parameter_mst.refrange_type_id as \"refrangeTypeId\", "
				+ "  sub_speciality_mst.sub_speciality_name as \"deptName\", "
				+ "  test_mst.test_desc as \"testDesc\", " + "  test_mst.test_code as \"testCode\", "
				+ "  test_mst.test_alies as \"testAlies\", " + "  sample_mst.sample_desc as \"sampleDesc\", "
				+ "  test_mst.sample_volume as \"sampleVolume\", "
				+ "  test_mst.no_of_sample_req as \"noOfSampleReq\", "
				+ "  test_mst.is_outsourced as \"isOutSourced\", "
				+ "  test_mst.service_id as \"serviceId\", "
				+ "  test_mst.identification_no as \"identificationNo\", " + "  test_mst.test_status as \"status\" "
				+ "from " + "  lab.m_test_master test_mst "
				+ "  inner join lab.t_test_parameter_mppr test_param_mppr on test_mst.test_id = test_param_mppr.test_id "
				+ "  inner join lab.m_parameter_master parameter_mst on test_param_mppr.parameter_id = parameter_mst.parameter_id "
				+ "  inner join doctor.m_sub_speciality_master sub_speciality_mst on test_mst.dept_id = sub_speciality_mst.sub_speciality_id "
				+ "  inner join lab.m_sample_master sample_mst on test_mst.sample_id = sample_mst.sample_id " + "where "
				+ "  test_mst.org_id = :orgId " + "  and test_mst.org_unit_id = :unitId "
				+ "  and test_mst.test_type = 0 " + "  and test_mst.is_histo_cyto = :isHistoCyto "
				+ " and test_param_mppr.is_deleted='N' "),

		@NamedNativeQuery(name = "GET_TEST_MASTER_COUNT", query = "select " + "  count(*) " + "from "
				+ "  lab.m_test_master test_mst "
				+ "  inner join lab.t_test_parameter_mppr test_param_mppr on test_mst.test_id = test_param_mppr.test_id "
				+ "where " + "  test_mst.org_id = :orgId " + "  and test_mst.org_unit_id = :unitId and test_param_mppr.is_deleted = 'N' "
				+ "  and test_mst.test_type = 0"),

		@NamedNativeQuery(name = "GET_SINGLE_PARAM_TEST_DETAILS", query = "select "
				+ "  container_mst.container_name as \"containerName\", "
				+ "  specimen_mst.speciman_name as \"specimenType\", "
				+ "  report_type_mst.report_type_name as \"reportTypeName\", "
				+ "  technique_master.technique_name as \"techniqueName\", "
				+ "  prerequisites_master.prerequisites_name as \"prerequisitesName\", "
				+ "  parameter_mst.parameter_name as \"parameterName\", "
				+ "  parameter_mst.parameter_code as \"parameterCode\", "
				+ "  parameter_mst.alies_name as \"aliesName\", " + "  test_mst.foot_note as \"footNote\", "
				+ "  test_mst.urgent_tat_time || ' ' || urgenttat.tat_time_name as \"urgentTat\", "
				+ "  test_mst.norml_tat_time || ' ' || normaltat.tat_time_name as \"normalTat\", "
				+ "  test_mst.is_centrifugation_requried as \"isCentrifugation\", "
				+ "  test_mst.pro_release as \"isProvisionalRelease\", "
				+ "  refrangetype.refrange_type_name as \"refrangeType\", "
				+ "  parameter_mst.param_precision as \"paramPrecison\" " + "from " + "  lab.m_test_master test_mst "
				+ "  inner join lab.t_test_parameter_mppr test_param_mppr on test_mst.test_id = test_param_mppr.test_id "
				+ "  inner join lab.m_parameter_master parameter_mst on test_param_mppr.parameter_id = parameter_mst.parameter_id "
				+ "  left join lab.m_speciman_master specimen_mst on test_mst.speciman_id = specimen_mst.speciman_id "
				+ "  left join lab.m_container_master container_mst on test_mst.container_id = container_mst.container_id "
				+ "  left join lab.m_report_type_master report_type_mst on test_mst.report_type_id = report_type_mst.report_type_id "
				+ "  left join lab.m_technique_master technique_master on test_mst.technique_id = technique_master.technique_id "
				+ "  left join lab.m_prerequisites_master prerequisites_master on test_mst.prerequisits_id = prerequisites_master.prerequisites_id "
				+ "  left join lab.m_tat_time_master urgenttat on test_mst.urgent_tat_id = urgenttat.tat_time_id "
				+ "  left join lab.m_tat_time_master normaltat on test_mst.norml_tat_id = normaltat.tat_time_id "
				+ "  left join lab.m_refrange_type_master refrangetype on parameter_mst.refrange_type_id = refrangetype.refrange_type_id "
				+ "where " + "  test_mst.org_id = :orgId " + "  and test_mst.org_unit_id = :unitId and test_param_mppr.is_deleted = 'N' "
				+ "  and test_mst.test_id = :testId"),

		@NamedNativeQuery(name = "GET_SINGLE_PARAM_DELTA_DETAILS", query = "select "
				+ "  parameter_mst.delta_per as \"deltaPer\", "
				+ "  parameter_mst.delta_days_id || ' Days ' as \"deltaDays\" " + "from "
				+ "  lab.m_test_master test_mst "
				+ "  inner join lab.t_test_parameter_mppr test_param_mppr on test_mst.test_id = test_param_mppr.test_id "
				+ "  inner join lab.m_parameter_master parameter_mst on test_param_mppr.parameter_id = parameter_mst.parameter_id "
				+ "  and test_mst.test_id = :testId and test_param_mppr.is_deleted = 'N'  "),
		
		@NamedNativeQuery(name = "GET_PAGINATED_CYOTOLOGY_TEST_MASTER_DETAILS_LIST_SEARCH",
		query = "select  test_mst.test_id as \"testId\", "
				+ "  parameter_mst.parameter_id as \"parameterId\", "
				+ "  test_mst.service_id as \"serviceId\", "
				+ "  parameter_mst.refrange_type_id as \"refrangeTypeId\", "
				+ "  sub_speciality_mst.sub_speciality_name as \"deptName\", "
				+ "  test_mst.test_desc as \"testDesc\", " + "  test_mst.test_code as \"testCode\", "
				+ "  test_mst.test_alies as \"testAlies\", " + "  sample_mst.sample_desc as \"sampleDesc\", "
				+ "  test_mst.sample_volume as \"sampleVolume\", "
				+ "  test_mst.no_of_sample_req as \"noOfSampleReq\", "
				+ "  test_mst.is_outsourced as \"isOutSourced\", "
				+ "  test_mst.identification_no as \"identificationNo\", " + "  test_mst.test_status as \"status\" "
				+ "from " + "  lab.m_test_master test_mst "
				+ "  inner join lab.t_test_parameter_mppr test_param_mppr on test_mst.test_id = test_param_mppr.test_id "
				+ "  inner join lab.m_parameter_master parameter_mst on test_param_mppr.parameter_id = parameter_mst.parameter_id "
				+ "  inner join doctor.m_sub_speciality_master sub_speciality_mst on test_mst.dept_id = sub_speciality_mst.sub_speciality_id "
				+ "  inner join lab.m_sample_master sample_mst on test_mst.sample_id = sample_mst.sample_id " + "where "
				+ "  test_mst.org_id = :orgId " + "  and test_mst.org_unit_id = :unitId "
				+ "  and test_mst.test_type = 0 " + "  and test_mst.is_histo_cyto = :isHistoCyto "
				+ "  and (lower(sub_speciality_mst.sub_speciality_name ) like :testDesc "
				+ " OR lower(test_mst.test_desc ) like :testDesc "
				+ " OR lower(test_mst.test_code ) like :testDesc "
				+ " OR lower(test_mst.test_alies ) like :testDesc "
				+ " OR lower(sample_mst.sample_desc ) like :testDesc "
				+ " OR lower(test_mst.is_outsourced ) like :testDesc "
				+ " OR lower(test_mst.identification_no  ) like :testDesc )"
				+ " and test_param_mppr.is_deleted='N' "),
		
		@NamedNativeQuery(name = "GET_PAGINATED_TEST_MASTER_DETAILS_LIST_SEARCH", query = "select " + "  test_mst.test_id as \"testId\", "
				+ "  parameter_mst.parameter_id as \"parameterId\", "
				+ "  parameter_mst.refrange_type_id as \"refrangeTypeId\", "
				+ "  sub_speciality_mst.sub_speciality_name as \"deptName\", "
				+ "  test_mst.test_desc as \"testDesc\", " + "  test_mst.test_code as \"testCode\", "
				+ "  test_mst.test_alies as \"testAlies\", " + "  sample_mst.sample_desc as \"sampleDesc\", "
				+ "  test_mst.sample_volume as \"sampleVolume\", "
				+ "  test_mst.no_of_sample_req as \"noOfSampleReq\", "
				+ "  test_mst.is_outsourced as \"isOutSourced\", "
				+ "  test_mst.service_id as \"serviceId\", "
				+ "  test_mst.identification_no as \"identificationNo\", " + "  test_mst.test_status as \"status\" "
				+ "from " + "  lab.m_test_master test_mst "
				+ "  inner join lab.t_test_parameter_mppr test_param_mppr on test_mst.test_id = test_param_mppr.test_id "
				+ "  inner join lab.m_parameter_master parameter_mst on test_param_mppr.parameter_id = parameter_mst.parameter_id "
				+ "  inner join doctor.m_sub_speciality_master sub_speciality_mst on test_mst.dept_id = sub_speciality_mst.sub_speciality_id "
				+ "  inner join lab.m_sample_master sample_mst on test_mst.sample_id = sample_mst.sample_id " + "where "
				+ "  test_mst.org_id = :orgId " + "  and test_mst.org_unit_id = :unitId "
				+ "  and test_mst.test_type = 0 " + "  and test_mst.is_histo_cyto = :isHistoCyto "
				+ "  and (lower(sub_speciality_mst.sub_speciality_name ) like :testDesc "
				+ " OR lower(test_mst.test_desc ) like :testDesc "
				+ " OR lower(test_mst.test_code ) like :testDesc "
				+ " OR lower(test_mst.test_alies ) like :testDesc "
				+ " OR lower(sample_mst.sample_desc ) like :testDesc "
				+ " OR lower(test_mst.is_outsourced ) like :testDesc "
				+ " OR lower(test_mst.identification_no  ) like :testDesc )"
				+ " and test_param_mppr.is_deleted='N' "),
})

@Entity 
@Table(name = "m_test_master", schema = "lab")
@SequenceGenerator(name = "m_seq_test_master", sequenceName = "lab.m_seq_test_master", allocationSize = 1)
public class TestMaster {
	@Id
	@Column(name = "test_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_test_master")
	private int testId;

	@Column(name = "test_code")
	private String testCode;

	@Column(name = "test_desc")
	private String testDesc;

	@Column(name = "test_alies")
	private String testAlies;

	@Column(name = "dept_id")
	private Integer deptId;

	@Column(name = "sample_id")
	private Integer sampleId;

	@Column(name = "no_of_sample_req")
	private Integer noOfSampleReq;

	@Column(name = "sample_volume")
	private Double sampleVolume;

	@Column(name = "container_id")
	private Integer containerId;

	@Column(name = "report_type_id")
	private Integer reportTypeId;

	@Column(name = "technique_id")
	private Integer techniqueId;

	@Column(name = "identification_no")
	private String identificationNo;

	@Column(name = "prerequisits_id")
	private Integer prerequisitsId;

	@Column(name = "pro_release")
	private Character proRelease;

	@Column(name = "test_status")
	private Character testStatus;

	@Column(name = "is_outsourced")
	private Character isOutsourced;

	@Column(name = "foot_note")
	private String footNote;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long updatedDate;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "norml_tat_time")
	private Integer normlTatTime;

	@Column(name = "norml_tat_id")
	private Integer normlTatId;

	@Column(name = "urgent_tat_time")
	private Integer urgentTatTime;

	@Column(name = "urgent_tat_id")
	private Integer urgentTatId;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "test_type")
	private Integer testType;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "is_centrifugation_requried")
	private Character isCentrifugationRequried;

	@Column(name = "is_histo_cyto")
	private Character isHistoCyto;

	@Column(name = "speciman_id")
	private Integer specimanId;

	@Column(name = "is_gynac")
	private Character isGynac;

	@Column(name = "is_fnac")
	private Character isFnac;
	
	@Column(name = "is_unique_barcode")
	private Character isUniqueBarcode;
	
	@Column(name = "culture_test")
	private Boolean cultureTest;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testMaster", orphanRemoval = true)
	private List<TestParamMppr> listTestParamMppr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sample_id", insertable = false, nullable = false, updatable = false)
	private SampleMaster sampleMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "container_id", insertable = false, nullable = false, updatable = false)
	private ContainerMaster containerMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_type_id", insertable = false, nullable = false, updatable = false)
	private ReportTypeMaster reportTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prerequisits_id", insertable = false, nullable = false, updatable = false)
	private PrerequisitesMaster prerequisitesMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "technique_id", insertable = false, nullable = false, updatable = false)
	private TechniqueMaster techniqueMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "norml_tat_id", insertable = false, nullable = false, updatable = false)
	private TurnAroundTimeMaster norTurnAroundTimeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "urgent_tat_id", insertable = false, nullable = false, updatable = false)
	private TurnAroundTimeMaster urgTurnAroundTimeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id", insertable = false, nullable = false, updatable = false)
	private SubSpecialityMaster subSpecialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciman_id", insertable = false, nullable = false, updatable = false)
	private SpecimanMaster specimanMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public String getTestAlies() {
		return testAlies;
	}

	public void setTestAlies(String testAlies) {
		this.testAlies = testAlies;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	public Integer getNoOfSampleReq() {
		return noOfSampleReq;
	}

	public void setNoOfSampleReq(Integer noOfSampleReq) {
		this.noOfSampleReq = noOfSampleReq;
	}

	public Double getSampleVolume() {
		return sampleVolume;
	}

	public void setSampleVolume(Double sampleVolume) {
		this.sampleVolume = sampleVolume;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Integer getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Integer reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public Integer getTechniqueId() {
		return techniqueId;
	}

	public void setTechniqueId(Integer techniqueId) {
		this.techniqueId = techniqueId;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public Integer getPrerequisitsId() {
		return prerequisitsId;
	}

	public void setPrerequisitsId(Integer prerequisitsId) {
		this.prerequisitsId = prerequisitsId;
	}

	public Character getProRelease() {
		return proRelease;
	}

	public void setProRelease(Character proRelease) {
		this.proRelease = (proRelease == '\u0000') ? 'N' : proRelease;
	}

	public Character getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(Character testStatus) {
		this.testStatus = (testStatus == '\u0000') ? 'A' : testStatus;
	}

	public Character getIsOutsourced() {
		return isOutsourced;
	}

	public void setIsOutsourced(Character isOutsourced) {
		this.isOutsourced = (isOutsourced == '\u0000') ? 'N' : isOutsourced;
	}

	public String getFootNote() {
		return footNote;
	}

	public void setFootNote(String footNote) {
		this.footNote = footNote;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<TestParamMppr> getListTestParamMppr() {
		return listTestParamMppr;
	}

	public void setListTestParamMppr(List<TestParamMppr> listTestParamMppr) {
		this.listTestParamMppr = listTestParamMppr;
	}

	public SampleMaster getSampleMaster() {
		return sampleMaster;
	}

	public void setSampleMaster(SampleMaster sampleMaster) {
		this.sampleMaster = sampleMaster;
	}

	public ContainerMaster getContainerMaster() {
		return containerMaster;
	}

	public void setContainerMaster(ContainerMaster containerMaster) {
		this.containerMaster = containerMaster;
	}

	public ReportTypeMaster getReportTypeMaster() {
		return reportTypeMaster;
	}

	public void setReportTypeMaster(ReportTypeMaster reportTypeMaster) {
		this.reportTypeMaster = reportTypeMaster;
	}

	public PrerequisitesMaster getPrerequisitesMaster() {
		return prerequisitesMaster;
	}

	public void setPrerequisitesMaster(PrerequisitesMaster prerequisitesMaster) {
		this.prerequisitesMaster = prerequisitesMaster;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getNormlTatTime() {
		return normlTatTime;
	}

	public void setNormlTatTime(Integer normlTatTime) {
		this.normlTatTime = normlTatTime;
	}

	public Integer getNormlTatId() {
		return normlTatId;
	}

	public void setNormlTatId(Integer normlTatId) {
		this.normlTatId = normlTatId;
	}

	public Integer getUrgentTatTime() {
		return urgentTatTime;
	}

	public void setUrgentTatTime(Integer urgentTatTime) {
		this.urgentTatTime = urgentTatTime;
	}

	public Integer getUrgentTatId() {
		return urgentTatId;
	}

	public void setUrgentTatId(Integer urgentTatId) {
		this.urgentTatId = urgentTatId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public TechniqueMaster getTechniqueMaster() {
		return techniqueMaster;
	}

	public void setTechniqueMaster(TechniqueMaster techniqueMaster) {
		this.techniqueMaster = techniqueMaster;
	}

	public TurnAroundTimeMaster getNorTurnAroundTimeMaster() {
		return norTurnAroundTimeMaster;
	}

	public void setNorTurnAroundTimeMaster(TurnAroundTimeMaster norTurnAroundTimeMaster) {
		this.norTurnAroundTimeMaster = norTurnAroundTimeMaster;
	}

	public TurnAroundTimeMaster getUrgTurnAroundTimeMaster() {
		return urgTurnAroundTimeMaster;
	}

	public void setUrgTurnAroundTimeMaster(TurnAroundTimeMaster urgTurnAroundTimeMaster) {
		this.urgTurnAroundTimeMaster = urgTurnAroundTimeMaster;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public SubSpecialityMaster getSubSpecialityMaster() {
		return subSpecialityMaster;
	}

	public void setSubSpecialityMaster(SubSpecialityMaster subSpecialityMaster) {
		this.subSpecialityMaster = subSpecialityMaster;
	}

	public Character getIsCentrifugationRequried() {

		return isCentrifugationRequried;
	}

	public void setIsCentrifugationRequried(Character isCentrifugationRequried) {
		this.isCentrifugationRequried = (isCentrifugationRequried == '\u0000') ? 'N' : isCentrifugationRequried;
	}

	public Character getIsHistoCyto() {
		return isHistoCyto;
	}

	public void setIsHistoCyto(Character isHistoCyto) {
		this.isHistoCyto = (isHistoCyto == '\u0000') ? 'N' : isHistoCyto;
	}

	public Integer getSpecimanId() {
		return specimanId;
	}

	public void setSpecimanId(Integer specimanId) {
		this.specimanId = specimanId;
	}

	public SpecimanMaster getSpecimanMaster() {
		return specimanMaster;
	}

	public void setSpecimanMaster(SpecimanMaster specimanMaster) {
		this.specimanMaster = specimanMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public Character getIsGynac() {
		return isGynac;
	}

	public void setIsGynac(Character isGynac) {
		this.isGynac = (isGynac == null) ? 'N' : isGynac;
	}

	public Character getIsFnac() {
		return isFnac;
	}

	public void setIsFnac(Character isFnac) 
	{
		this.isFnac = (isFnac == null) ? 'N' : isFnac;
	}

	public Character getIsUniqueBarcode() {
		return isUniqueBarcode;
	}

	public void setIsUniqueBarcode(Character isUniqueBarcode) {
		this.isUniqueBarcode = isUniqueBarcode;
	}

	public Boolean getCultureTest() {
		return cultureTest;
	}

	public void setCultureTest(Boolean cultureTest) {
		this.cultureTest = cultureTest;
	}
    
}
