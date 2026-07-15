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


@NamedQueries({
	
	@NamedQuery(name="GET_OP_CONSULTATION_LIST",query="SELECT opccm.opVisitRuleId as opVisitRuleId,"
			+ "	opccm.specialityId as specialityId,"
			+ "	spl.specialityName as specialityName, "
			+ " (case when doc.doctorId is not null then "
			+ "		concat('Dr. ',coalesce(doc.firstName, ''),' ',coalesce(doc.lastName, '')) "
			+ "	else '-' end )as doctorName, " 
			+ "	opccm.followupVisitDays as followupVisitDays, "
			+ "	opccm.followupVisitCount as followupVisitCount, "
			+ "	opccm.secondaryVisitDays as secondaryVisitDays, "
			+ " opccm.status as status ,"
			+ " opccm.doctorId as doctorId ,"
			+ "	opccm.secondaryVisitCount as secondaryVisitCount "
			+ "FROM OPConsultationConfigurationMaster opccm "
			+ "	INNER JOIN opccm.specialityMaster spl "
			+ "	LEFT JOIN opccm.doctorMaster doc "
			+ "WHERE opccm.organizationId=:orgId "
			+ "	AND opccm.unitId=:unitId "
			+ " "),
	
	@NamedQuery(name="GET_OP_CONSULTATION_CONFIGURATION_LIST_BY_ID", query="SELECT opccm.opVisitRuleId as opVisitRuleId,"
			+ "opccm.specialityId as specialityId,"
			+ "opccm.doctorId as doctorId,"
			+ "opccm.followupVisitDays as followupVisitDays, "
			+ "opccm.followupVisitCount as followupVisitCount, "
			+ "opccm.secondaryVisitDays as secondaryVisitDays, "
			+ "opccm.secondaryVisitCount as secondaryVisitCount "
			+ "FROM OPConsultationConfigurationMaster opccm "
			+ "WHERE opccm.opVisitRuleId=:opVisitRuleId "),
	
	@NamedQuery(name="GET_OP_CONSULTATION_LIST_BY_SPECILAITY_ID",query="SELECT opccm.opVisitRuleId as opVisitRuleId,"
			+ "opccm.specialityId as specialityId,"
			+ "opccm.followupVisitDays as followupVisitDays, "
			+ "opccm.followupVisitCount as followupVisitCount, "
			+ "opccm.secondaryVisitDays as secondaryVisitDays, "
			+ "opccm.secondaryVisitCount as secondaryVisitCount "
			+ "FROM OPConsultationConfigurationMaster opccm "
			+ "WHERE opccm.organizationId=:orgId "
			+ "AND opccm.unitId=:unitId "
			+ "AND opccm.status='A' "
			+ "AND opccm.specialityId=:specialityId "),
	
})



@Entity
@Table(name="m_op_consultation_configuration_master",schema="opd")
@SequenceGenerator(name="op_visit_rule_master_seq",sequenceName="opd.op_visit_rule_master_seq",allocationSize=1)
public class OPConsultationConfigurationMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="op_visit_rule_master_seq")
	@Column(name="op_visit_rule_id")
	private Integer opVisitRuleId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="followup_visit_days")
	private Integer followupVisitDays;

	@Column(name="followup_visit_count")
	private Integer followupVisitCount;
	
	@Column(name="secondary_visit_days")
	private Integer secondaryVisitDays;
	
	@Column(name="secondary_visit_count")
	private Integer secondaryVisitCount;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOpVisitRuleId() {
		return opVisitRuleId;
	}

	public void setOpVisitRuleId(Integer opVisitRuleId) {
		this.opVisitRuleId = opVisitRuleId;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getFollowupVisitDays() {
		return followupVisitDays;
	}

	public void setFollowupVisitDays(Integer followupVisitDays) {
		this.followupVisitDays = followupVisitDays;
	}

	public Integer getFollowupVisitCount() {
		return followupVisitCount;
	}

	public void setFollowupVisitCount(Integer followupVisitCount) {
		this.followupVisitCount = followupVisitCount;
	}

	public Integer getSecondaryVisitDays() {
		return secondaryVisitDays;
	}

	public void setSecondaryVisitDays(Integer secondaryVisitDays) {
		this.secondaryVisitDays = secondaryVisitDays;
	}

	public Integer getSecondaryVisitCount() {
		return secondaryVisitCount;
	}

	public void setSecondaryVisitCount(Integer secondaryVisitCount) {
		this.secondaryVisitCount = secondaryVisitCount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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


/*op_visit_rule_id integer NOT NULL DEFAULT nextval('opd.op_visit_rule_master_seq'::regclass),
organization_id integer,
unit_id integer,
speciality_id integer,
followup_visit_days integer,
followup_visit_count integer,
secondary_visit_days integer,
secondary_visit_count integer,
status character(1) DEFAULT 'A'::"char",
created_date timestamp without time zone,
created_by integer,
updated_date timestamp without time zone DEFAULT now(),
updated_by integer,*/