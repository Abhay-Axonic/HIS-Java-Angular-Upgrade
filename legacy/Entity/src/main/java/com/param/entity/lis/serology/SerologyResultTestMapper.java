package com.param.entity.lis.serology;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_serology_result_test_mppr", schema = "lab")
@SequenceGenerator(name = "seq_t_serology_result_test_mppr", sequenceName = "lab.seq_t_serology_result_test_mppr", allocationSize = 1)
public class SerologyResultTestMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_serology_result_test_mppr")
	@Column(name = "serology_result_test_mppr_id")
	private int serologyResultTestMpprId;

	@Column(name = "serology_test_result_id")
	private int serologyTestResultId;
	
	@Column(name = "technique_id")
	private int techniqueId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;
	
	@Column(name = "finding_result")
	private String findingResult;

	@Column(name = "finding_result_desc")
	private String findingResultDesc;
	
	@Column(name = "first_finding")
	private String firstFinding;

	@Column(name = "first_finding_desc")
	private String firstFindingDesc;

	@Column(name = "second_finding")
	private String secondFinding;

	@Column(name = "second_finding_desc")
	private String secondFindingDesc;
	
	@Column(name = "third_finding")
	private String thiredFinding;

	@Column(name = "third_finding_desc")
	private String thirdFindingDesc;

	@Column(name = "parameter_id")
	private Integer parameterId;
	
	@Column(name = "is_abnormal")
	private char isAbnormal;

	public int getSerologyResultTestMpprId() {
		return serologyResultTestMpprId;
	}

	public void setSerologyResultTestMpprId(int serologyResultTestMpprId) {
		this.serologyResultTestMpprId = serologyResultTestMpprId;
	}

	public int getSerologyTestResultId() {
		return serologyTestResultId;
	}

	public void setSerologyTestResultId(int serologyTestResultId) {
		this.serologyTestResultId = serologyTestResultId;
	}


	public int getTechniqueId() {
		return techniqueId;
	}

	public void setTechniqueId(int techniqueId) {
		this.techniqueId = techniqueId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getFindingResult() {
		return findingResult;
	}

	public void setFindingResult(String findingResult) {
		this.findingResult = findingResult;
	}

	public String getFindingResultDesc() {
		return findingResultDesc;
	}

	public void setFindingResultDesc(String findingResultDesc) {
		this.findingResultDesc = findingResultDesc;
	}

	public String getFirstFinding() {
		return firstFinding;
	}

	public void setFirstFinding(String firstFinding) {
		this.firstFinding = firstFinding;
	}

	public String getFirstFindingDesc() {
		return firstFindingDesc;
	}

	public void setFirstFindingDesc(String firstFindingDesc) {
		this.firstFindingDesc = firstFindingDesc;
	}

	public String getSecondFinding() {
		return secondFinding;
	}

	public void setSecondFinding(String secondFinding) {
		this.secondFinding = secondFinding;
	}

	public String getSecondFindingDesc() {
		return secondFindingDesc;
	}

	public void setSecondFindingDesc(String secondFindingDesc) {
		this.secondFindingDesc = secondFindingDesc;
	}

	public String getThiredFinding() {
		return thiredFinding;
	}

	public void setThiredFinding(String thiredFinding) {
		this.thiredFinding = thiredFinding;
	}

	public String getThirdFindingDesc() {
		return thirdFindingDesc;
	}

	public void setThirdFindingDesc(String thirdFindingDesc) {
		this.thirdFindingDesc = thirdFindingDesc;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public char getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(char isAbnormal) {
		this.isAbnormal = isAbnormal;
	}

	
	
	
	
}
