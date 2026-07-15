package com.param.otims.model;

import java.math.BigDecimal;
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


@NamedQueries({
	
	@NamedQuery(name="ADDITIONAL_OT_TIME_LIST",query="SELECT aot.additionalOtTimeId as additionalOtTimeId, "
			+ "aot.procedureTypeId as procedureTypeId, "
			+ "proTyp.procedureTypeName as procedureTypeName, "
			+ "aot.timeMin as timeMin, "
			+ "aot.rate as rate, "
			+ "aot.status as status "
			+ "FROM AdditionalOtTime aot "
			+ "INNER JOIN aot.procedureTypeMaster proTyp "
			+ "WHERE aot.orgId=:orgId "
			+ "AND aot.unitId=:unitId "),
	
	@NamedQuery(name="IS_ADDITIONAL_OT_TIME_EXIST_NOT_BY_ID",query="SELECT aot.additionalOtTimeId as additionalOtTimeId, "
			+ "aot.procedureTypeId as procedureTypeId, "
			+ "aot.timeMin as timeMin, "
			+ "aot.rate as rate, "
			+ "aot.status as status "
			+ "FROM AdditionalOtTime aot "
			+ "WHERE aot.orgId=:orgId "
			+ "AND aot.unitId=:unitId "
			+ "AND aot.procedureTypeId=:procedureTypeId "
			+ "AND aot.timeMin=:timeMin "
			+ "AND aot.rate=:rate "
			+ "AND aot.additionalOtTimeId!=:additionalOtTimeId"),
	
	@NamedQuery(name="GET_ADDTIONAL_RATE",query="SELECT coalesce(rate/timeMin,1 ) as rate FROM AdditionalOtTime "
			+ "WHERE orgId=:orgId "
			+ "AND unitId=:unitId "
			+ "AND procedureTypeId=:procedureTypeId")
})


@Entity
@Table(name="t_additional_ot_time",schema="otims")
@SequenceGenerator(name="additional_ot_time_seq",sequenceName="otims.additional_ot_time_seq",allocationSize=1)
public class AdditionalOtTime {
	
	@Id
	@Column(name="additional_ot_time_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="additional_ot_time_seq")
	private Integer additionalOtTimeId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="procedure_type_id")
	private Integer procedureTypeId;
	
	@Column(name="time_min")
	private BigDecimal timeMin;
	
	@Column(name="rate")
	private BigDecimal rate;
	
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "procedure_type_id", insertable = false, updatable = false)
	private ProcedureTypeMaster procedureTypeMaster;

	public Integer getAdditionalOtTimeId() {
		return additionalOtTimeId;
	}

	public void setAdditionalOtTimeId(Integer additionalOtTimeId) {
		this.additionalOtTimeId = additionalOtTimeId;
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

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public BigDecimal getTimeMin() {
		return timeMin;
	}

	public void setTimeMin(BigDecimal timeMin) {
		this.timeMin = timeMin;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
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

}

