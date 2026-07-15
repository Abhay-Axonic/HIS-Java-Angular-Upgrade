package com.param.global.model;

import java.time.Instant;
import java.time.LocalTime;

import java.util.Date;
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

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
/*@NamedQuery(name="GET_INTAKE_OUTPUT_CHART_LIST_BY_PATIENT_ID",
query="SELECT intakeOutputChartMaster.intakeOutputChartId AS intakeOutputChartId,"
	//	+ " intakeOutputChartMaster.totalIntake AS totalIntake,"
	//	+ " intakeOutputChartMaster.totalOutput AS totalOutput,"
	    + " intakeOutputChartMaster.isIoStart AS isIoStart,"
	    + " to_char(intakeOutputChartMaster.intakeDate, 'DD/MM/YYYY') AS intakeDate, "
	    + " to_char(intakeOutputChartMaster.intakeTime, 'HH24:MI') AS intakeTime,"
	    + " to_char(intakeOutputChartMaster.outputDate, 'DD/MM/YYYY') AS outputDate, "
	    + " to_char(intakeOutputChartMaster.outputTime, 'HH24:MI') AS outputTime,"
		+ " intakeOutputChartMaster.balance AS balance, "
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeDate, 'DD-MM-YYYY') AS startIntakeDate,"
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeTime, 'HH24:MI') AS startIntakeTime,"
		+ " to_char(outputChartDetailsMaster.startOutputTime, 'HH24:MI') AS startOutputTime,"
		+ " to_char(outputChartDetailsMaster.startOutputDate, 'DD-MM-YYYY') AS startOutputDate, listShiftMaster.shiftName as intakeShiftName,"
		+ " shiftMasterList.shiftName as outputShiftName ,intakeFluidTypeMaster.intakeTypeFluidDesc as intakeTypeFluidDesc, "
		+ " outputTypeMaster.outputTypeDesc as outputTypeDesc, "
		+ " intakeOutputChartDetailsMaster.startIntakeQuantity AS startIntakeQuantity,"
		+ " intakeOutputChartDetailsMaster.totalIntake AS totalIntake,"
		+ " outputChartDetailsMaster.totalOutput as totalOutput, "
		+ " outputChartDetailsMaster.startOutputQuantity as startOutputQuantity "
		+ " FROM IntakeOutputChartMaster intakeOutputChartMaster "
		+ " LEFT JOIN intakeOutputChartMaster.listIntakeOutputChartDetailsMaster intakeOutputChartDetailsMaster"
		+ " LEFT JOIN intakeOutputChartMaster.listOutputChartDetailsMaster outputChartDetailsMaster"
		+ " LEFT JOIN intakeOutputChartDetailsMaster.shiftMasterIntake listShiftMaster"
		+ " LEFT JOIN outputChartDetailsMaster.shiftMasterOutput shiftMasterList"
		+ " LEFT JOIN intakeOutputChartDetailsMaster.intakeFluidTypeMaster intakeFluidTypeMaster"
		+ " LEFT JOIN outputChartDetailsMaster.outputTypeMaster outputTypeMaster"
		+ " WHERE intakeOutputChartMaster.organizationId = :organizationId "
		+ " AND intakeOutputChartMaster.unitId = :unitId "
		+ " AND intakeOutputChartMaster.patientId = :patientId "
		+ " AND to_char(intakeOutputChartMaster.createdDate, 'DD-MM-YYYY') = :createdDate "
//		+ " ORDER BY intakeOutputChartMaster.createdDate Desc"
		+ " ORDER BY intakeOutputChartMaster.intakeOutputChartId asc"

),*/

@NamedQuery(name="GET_INTAKE_OUTPUT_CHART_HISTORY_LIST_BY_DATE",
query="SELECT distinct(intakeOutputChartMaster.intakeOutputChartId) AS intakeOutputChartId,"
		+ " intakeOutputChartDetailsMaster.totalIntake AS totalIntake,"
		+ " outputChartDetailsMaster.totalOutput AS totalOutput, "
		+ "intakeOutputChartMaster.balance AS balance, "
		+ " to_char(intakeOutputChartMaster.intakeDate, 'DD-MM-YYYY') AS intakeDate,"
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeDate, 'DD-MM-YYYY') AS startIntakeDate,"
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeTime, 'HH24:MI') AS startIntakeTime,"
		+ " to_char(outputChartDetailsMaster.startOutputTime, 'HH24:MI') AS startOutputTime,"
		+ " to_char(outputChartDetailsMaster.startOutputDate, 'DD-MM-YYYY') AS startOutputDate, "
		+ " listShiftMaster.shiftName as intakeShiftName,"
		+ " shiftMasterList.shiftName as outputShiftName ,"
//		+ " intakeFluidTypeMaster.intakeTypeFluidDesc as intakeTypeFluidDesc, "
//		+ " outputTypeMaster.outputTypeDesc as outputTypeDesc, "
		+ " intakeOutputChartDetailsMaster.startIntakeQuantity AS startIntakeQuantity,"
		+ " intakeOutputChartDetailsMaster.shiftId AS intakeShiftId,"
		+ " outputChartDetailsMaster.shiftId AS outputShiftId ,"
		+ " outputChartDetailsMaster.startOutputQuantity as startOutputQuantity "
		+ " FROM IntakeOutputChartMaster intakeOutputChartMaster "
		+ " LEFT JOIN intakeOutputChartMaster.listIntakeOutputChartDetailsMaster intakeOutputChartDetailsMaster"
		+ " LEFT JOIN intakeOutputChartMaster.listOutputChartDetailsMaster outputChartDetailsMaster"
		+ " LEFT JOIN intakeOutputChartDetailsMaster.shiftMasterIntake listShiftMaster"
		+ " LEFT JOIN outputChartDetailsMaster.shiftMasterOutput shiftMasterList"
		+ " LEFT JOIN intakeOutputChartDetailsMaster.intakeFluidTypeMaster intakeFluidTypeMaster"
		+ " LEFT JOIN outputChartDetailsMaster.outputTypeMaster outputTypeMaster"
		+ " WHERE intakeOutputChartMaster.organizationId = :organizationId "
		+ " AND intakeOutputChartMaster.unitId = :unitId "
		+ " AND intakeOutputChartMaster.patientId = :patientId "
		+ " AND to_char(intakeOutputChartMaster.intakeDate, 'DD-MM-YYYY') BETWEEN :fromDate "
		+ " AND :todate "
		+ " ORDER BY to_char(intakeOutputChartMaster.intakeDate, 'DD-MM-YYYY') desc "

),

@NamedQuery(name="GET_INPUT_OUTPUT_CHART_HISTORY_BY_DATE_FOR_INPUT",
query="SELECT distinct(to_char(intakeOutputChartMaster.intakeDate, 'DD-MM-YYYY')) AS intakeDate "
//		+ " to_char(intakeOutputChartMaster.outputDate, 'DD-MM-YYYY') AS outputDate"
		+ " FROM IntakeOutputChartMaster intakeOutputChartMaster "
		+ " WHERE intakeOutputChartMaster.organizationId = :organizationId "
		+ " AND intakeOutputChartMaster.unitId = :unitId "
		+ " AND intakeOutputChartMaster.patientId = :patientId "
		+ " AND to_char(intakeOutputChartMaster.intakeDate, 'DD-MM-YYYY') BETWEEN :fromDate "
		+ " AND :todate "
		+ " ORDER BY to_char(intakeOutputChartMaster.intakeDate, 'DD-MM-YYYY') desc "

),

@NamedQuery(name="GET_INTAKE_AND_OUTPUT_DATE_BY_PATIENT_ID",
query="SELECT distinct(intakeOutputChartMaster.patientId) AS patientId, "
		+ " to_char(intakeOutputChartMaster.intakeDate, 'DD/MM/YYYY') AS intakeDate, "
	    + " to_char(intakeOutputChartMaster.intakeTime, 'HH24:MI') AS intakeTime,"
		+ " to_char(intakeOutputChartMaster.outputTime, 'HH24:MI') AS outputTime,"
		+ " to_char(intakeOutputChartMaster.outputDate, 'DD/MM/YYYY') AS outputDate"
		+ " FROM IntakeOutputChartMaster intakeOutputChartMaster "
		+ " WHERE intakeOutputChartMaster.organizationId = :organizationId "
		+ " AND intakeOutputChartMaster.patientId = :patientId "

),

})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_INPUT_OUTPUT_CHART_HISTORY_FOR_SHIFT",
			query="select " 
	+ 
					" distinct(intopdt.shift_id) as \"shiftId\"," + 
					" shftmst.shift_name as \"intakeShiftName\" "+ 
					" from adt.t_intake_output_chart_details intopdt" + 
					" inner join adt.t_intake_output_chart intakech on intakech.intake_output_chart_id=intopdt.intake_output_chart_id" + 
					" inner join public.m_shift_master shftmst on shftmst.shift_id=intopdt.shift_id" + 
					" where" + 
					"	intakech.organization_id =:organizationId" + 
					"	and intakech.unit_id =:unitId" + 
					"	and intakech.patient_id =:patientId "+
//					+"	and (to_char(intakech.intake_date, 'DD-MM-YYYY') between :fromDate and :todate)" + 
					" union distinct" + 
					" select " + 
					" distinct(opdt.shift_id) as \"shiftId\"," + 
					" shftmst.shift_name as \"intakeShiftName\" "+ 
					" from adt.t_output_chart_details opdt" + 
					" inner join adt.t_intake_output_chart intakech on intakech.intake_output_chart_id=opdt.intake_output_chart_id" + 
					" inner join public.m_shift_master shftmst on shftmst.shift_id=opdt.shift_id" + 
					" where" + 
					"	intakech.organization_id =:organizationId" + 
					"	and intakech.unit_id =:unitId" + 
					"	and intakech.patient_id =:patientId "),
	
	
	
	/*"SELECT intakeOutputChartMaster.intakeOutputChartId AS intakeOutputChartId,"
	    + " intakeOutputChartMaster.isIoStart AS isIoStart,"
	    + " to_char(intakeOutputChartMaster.intakeDate, 'DD/MM/YYYY') AS intakeDate, "
	    + " to_char(intakeOutputChartMaster.intakeTime, 'HH24:MI') AS intakeTime,"
	    + " to_char(intakeOutputChartMaster.outputDate, 'DD/MM/YYYY') AS outputDate, "
	    + " to_char(intakeOutputChartMaster.outputTime, 'HH24:MI') AS outputTime,"
		+ " intakeOutputChartMaster.balance AS balance, "
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeDate, 'DD-MM-YYYY') AS startIntakeDate,"
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeTime, 'HH24:MI') AS startIntakeTime,"
		+ " to_char(outputChartDetailsMaster.startOutputTime, 'HH24:MI') AS startOutputTime,"
		+ " to_char(outputChartDetailsMaster.startOutputDate, 'DD-MM-YYYY') AS startOutputDate, listShiftMaster.shiftName as intakeShiftName,"
		+ " shiftMasterList.shiftName as outputShiftName ,intakeFluidTypeMaster.intakeTypeFluidDesc as intakeTypeFluidDesc, "
		+ " outputTypeMaster.outputTypeDesc as outputTypeDesc, "
		+ " intakeOutputChartDetailsMaster.startIntakeQuantity AS startIntakeQuantity,"
		+ " intakeOutputChartDetailsMaster.totalIntake AS totalIntake,"
		+ " outputChartDetailsMaster.totalOutput as totalOutput, "
		+ " outputChartDetailsMaster.startOutputQuantity as startOutputQuantity "*/
		@NamedNativeQuery(name="GET_INTAKE_OUTPUT_CHART_LIST_BY_PATIENT_ID",
				query="select " + 
						"	intakeoutp0_.intake_output_chart_id as \"intakeOutputChartId\", " + 
						"   listintake1_.intake_output_chart_details_id as \"intakeOutputChartDetailsId\", "+
						"   listoutput2_.output_chart_details_id as  \"outputChartDetailsId\", "+
						"	mi.name as \"itemName\", " + 
						"	intakeoutp0_.is_io_start as \"isIoStart\", " + 
						"	to_char(intakeoutp0_.intake_date, 'DD/MM/YYYY') as \"intakeDate\", " + 
						"	to_char(intakeoutp0_.intake_time, 'HH24:MI') as \"intakeTime\", " + 
						"	to_char(intakeoutp0_.output_date, 'DD/MM/YYYY') as \"outputDate\", " + 
						"	to_char(intakeoutp0_.output_time, 'HH24:MI') as \"outputTime\", " + 
						"	intakeoutp0_.balance as \"balance\"," + 
						"	to_char(listintake1_.start_intake_date, 'DD-MM-YYYY') as \"startIntakeDate\", " + 
						"	to_char(listintake1_.start_intake_time, 'HH24:MI') as \"startIntakeTime\", " + 
						"	to_char(listoutput2_.start_output_time, 'HH24:MI') as \"startOutputTime\", " + 
						"	to_char(listoutput2_.start_output_date, 'DD-MM-YYYY') as \"startOutputDate\", " + 
						"	shiftmaste3_.shift_name as \"intakeShiftName\", " + 
						"	shiftmaste4_.shift_name as \"outputShiftName\", " + 
						"	intakeflui5_.intake_type_fluid_desc as \"intakeTypeFluidDesc\", "+
						"   intakeflui5_.intake_type_fluid_id as \"intakeTypeFluidId\"," +
						"	itakeflud.intake_Fluid_desc as \"intakeFluiddesc\","+
						" itakeflud.intake_fluid_id as \"intakeFluidId\",  " + 
						"	outputtype6_.output_type_desc as \"outputTypeDesc\", " + 
						"	listintake1_.start_intake_quantity as \"startIntakeQuantity\", " + 
						"	listintake1_.total_intake as \"totalIntake\", " + 
						"	listoutput2_.total_output as \"totalOutput\", "+
						" listoutput2_.output_type_id as \"outputTypeId\", " + 
						"	listoutput2_.start_output_quantity as \"startOutputQuantity\" " + 
						"from " + 
						"	adt.t_intake_output_chart intakeoutp0_ " + 
						"left outer join adt.t_intake_output_chart_details listintake1_ on " + 
						"	intakeoutp0_.intake_output_chart_id = listintake1_.intake_output_chart_id " +
						" AND listintake1_.status = 'A' " + 
						"left outer join public.m_shift_master shiftmaste3_ on " + 
						"	listintake1_.shift_id = shiftmaste3_.shift_id " + 
						"left outer join public.m_intake_type_fluid_master intakeflui5_ on " + 
						"	listintake1_.intake_type_fluid_id = intakeflui5_.intake_type_fluid_id " + 
						"left outer join adt.t_output_chart_details listoutput2_ on " + 
						"	intakeoutp0_.intake_output_chart_id = listoutput2_.intake_output_chart_id " +
						" AND listoutput2_.status = 'A'" + 
						"left outer join public.m_shift_master shiftmaste4_ on " + 
						"	listoutput2_.shift_id = shiftmaste4_.shift_id " + 
						"left outer join public.m_output_type_master outputtype6_ on " + 
						"	listoutput2_.output_type_id = outputtype6_.output_type_id "+
						" left outer join public.m_intake_Fluid_master itakeflud on "+
						"  listintake1_.intake_Fluid_id = itakeflud.intake_Fluid_id " + 
						"left outer join public.m_item mi on " + 
						"	listintake1_.intake_type_fluid_id = mi.id  " + 
						"where " + 
						"	intakeoutp0_.organization_id =:organizationId " + 
						"	and intakeoutp0_.unit_id =:unitId " + 
						"	and intakeoutp0_.patient_id =:patientId "+
						"   and (case " + 
						" 		when listintake1_.status is not null then listintake1_.status = 'A' " + 
						" 		when listoutput2_.status = 'A' is not null then listoutput2_.status = 'A' " + 
						" 	end)  " + 
						"	and to_char(intakeoutp0_.created_date, 'YYYY-MM-DD')=:createdDate " + 
						" order by " + 
						"	intakeoutp0_.intake_output_chart_id asc")
//					+ "	and (to_char(intakech.intake_date, 'DD-MM-YYYY') between :fromDate and :todate)") 
 				})
@Entity
@Table(name = "t_intake_output_chart", schema = "adt")
@SequenceGenerator(name="t_intake_output_chart_seq", sequenceName="adt.t_intake_output_chart_seq", allocationSize=1)
public class IntakeOutputChartMaster {
	@Id
	@Column(name = "intake_output_chart_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_intake_output_chart_seq")
	private Integer intakeOutputChartId;

	@Column(name = "intake_date")
	private Date intakeDate;

	@Column(name = "intake_time")
	@Convert(converter = InstantConverter.class)
	private Instant intakeTime;
	
	@Column(name = "output_date")
	private Date outputDate;
	
	@Column(name = "output_time")
	@Convert(converter = InstantConverter.class)
	private Instant outputTime;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "user_id")
	private Integer userId;
	
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

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "balance")
	private Integer balance;
	
	@Column(name = "total_intake")
	private Integer totalIntake;

	
	@Column(name = "total_output")
	private Integer totalOutput;
	
	@Column(name = "is_io_start")
	private Boolean isIoStart;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "intakeOutputChartDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IntakeOutputChartDetailsMaster> listIntakeOutputChartDetailsMaster;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "outputChartDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OutputChartDetailsMaster> listOutputChartDetailsMaster;
	
	
	public Integer getIntakeOutputChartId() {
		return intakeOutputChartId;
	}

	public void setIntakeOutputChartId(Integer intakeOutputChartId) {
		this.intakeOutputChartId = intakeOutputChartId;
	}

	public Date getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(Date intakeDate) {
		this.intakeDate = intakeDate;
	}

	public Instant getIntakeTime() {
		return intakeTime;
	}

	public void setIntakeTime(Instant intakeTime) {
		this.intakeTime = intakeTime;
	}

	public Date getOutputDate() {
		return outputDate;
	}

	public void setOutputDate(Date outputDate) {
		this.outputDate = outputDate;
	}

	public Instant getOutputTime() {
		return outputTime;
	}

	public void setOutputTime(Instant outputTime) {
		this.outputTime = outputTime;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getTotalIntake() {
		return totalIntake;
	}

	public void setTotalIntake(Integer totalIntake) {
		this.totalIntake = totalIntake;
	}

	public Integer getTotalOutput() {
		return totalOutput;
	}

	public void setTotalOutput(Integer totalOutput) {
		this.totalOutput = totalOutput;
	}

	public Boolean getIsIoStart() {
		return isIoStart;
	}

	public void setIsIoStart(Boolean isIoStart) {
		this.isIoStart = isIoStart;
	}

	
	
	
}
