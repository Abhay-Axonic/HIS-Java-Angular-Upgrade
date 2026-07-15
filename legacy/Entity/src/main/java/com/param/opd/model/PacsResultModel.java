package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_pacs_result", schema = "opd")
@SequenceGenerator(name = "t_seq_pacs_result", sequenceName = "opd.t_seq_pacs_result", allocationSize = 1)
public class PacsResultModel {
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_pacs_result")	
	@Column(name="pacs_result_id")
	private Integer pacsResultId;
	
	@Column(name="set_id")
	private String setId ;
	
	@Column(name="value_type")
	private String valueType ;
	
	@Column(name="observation_identifier")
	private String observationIdentifier;
	
	@Column(name="observation_sub_id")
	private String observationSubId;
	
	@Column(name="observation_value")
	private String observationValue;
	
	@Column(name="units")
	private String units;
	
	@Column(name="referencerange")
	private String referencerange;
	
	@Column(name="abnormal_flags")
	private String abnormalFlags;
	
	@Column(name="probability")
	private String probability;
	
	@Column(name="nature_of_abnormality_test")
	private String natureOfAbnormalityTest;
	
	@Column(name="observation_result_status")
	private String observationResultStatus;
	
	@Column(name="effectivedate_last_observation_normal_values")
	private String effectivedateLastObservationNormalValues;
	
	@Column(name="userdefined_access_checks")
	private String userdefinedAccessChecks;
	
	@Column(name="datetimeof_observation")
	private String datetimeofObservation;
	
	@Column(name="producer_id")
	private String producerid;
	
	@Column(name="responsible_observer")
	private String responsibleObserver;
	
	@Column(name="access_number")
	private String accessNumber;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="order_no")
	private String orderNo;
	
	@Column(name="organization_id")
	private Integer organizationId;

	public Integer getPacsResultId() {
		return pacsResultId;
	}

	public String getSetId() {
		return setId;
	}

	public String getValueType() {
		return valueType;
	}

	public String getObservationIdentifier() {
		return observationIdentifier;
	}

	public String getObservationSubId() {
		return observationSubId;
	}

	public String getObservationValue() {
		return observationValue;
	}

	public String getUnits() {
		return units;
	}

	public String getReferencerange() {
		return referencerange;
	}

	public String getAbnormalFlags() {
		return abnormalFlags;
	}

	public String getProbability() {
		return probability;
	}

	public String getNatureOfAbnormalityTest() {
		return natureOfAbnormalityTest;
	}

	public String getObservationResultStatus() {
		return observationResultStatus;
	}

	public String getEffectivedateLastObservationNormalValues() {
		return effectivedateLastObservationNormalValues;
	}

	public String getUserdefinedAccessChecks() {
		return userdefinedAccessChecks;
	}

	public String getDatetimeofObservation() {
		return datetimeofObservation;
	}

	public String getProducerid() {
		return producerid;
	}

	public String getResponsibleObserver() {
		return responsibleObserver;
	}

	public String getAccessNumber() {
		return accessNumber;
	}

	public void setPacsResultId(Integer pacsResultId) {
		this.pacsResultId = pacsResultId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public void setObservationIdentifier(String observationIdentifier) {
		this.observationIdentifier = observationIdentifier;
	}

	public void setObservationSubId(String observationSubId) {
		this.observationSubId = observationSubId;
	}

	public void setObservationValue(String observationValue) {
		this.observationValue = observationValue;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public void setReferencerange(String referencerange) {
		this.referencerange = referencerange;
	}

	public void setAbnormalFlags(String abnormalFlags) {
		this.abnormalFlags = abnormalFlags;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

	public void setNatureOfAbnormalityTest(String natureOfAbnormalityTest) {
		this.natureOfAbnormalityTest = natureOfAbnormalityTest;
	}

	public void setObservationResultStatus(String observationResultStatus) {
		this.observationResultStatus = observationResultStatus;
	}

	public void setEffectivedateLastObservationNormalValues(String effectivedateLastObservationNormalValues) {
		this.effectivedateLastObservationNormalValues = effectivedateLastObservationNormalValues;
	}

	public void setUserdefinedAccessChecks(String userdefinedAccessChecks) {
		this.userdefinedAccessChecks = userdefinedAccessChecks;
	}

	public void setDatetimeofObservation(String datetimeofObservation) {
		this.datetimeofObservation = datetimeofObservation;
	}

	public void setProducerid(String producerid) {
		this.producerid = producerid;
	}

	public void setResponsibleObserver(String responsibleObserver) {
		this.responsibleObserver = responsibleObserver;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
