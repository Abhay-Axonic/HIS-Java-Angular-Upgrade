package com.param.otims.model;

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

import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.billing.global.transaction.model.TariffMaster;
import com.param.doctor.model.DoctorGrade;
import com.param.global.model.BedCategoryMaster;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PaymentEntitlementMaster;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.SurgeryGradeMaster;
import com.param.global.model.VisitMaster;
import com.param.global.model.VisitTypeMaster;
@NamedQueries({
	@NamedQuery(name="GET_SURGERY_RESOURCE_CONFIG_LIST",
			query=" SELECT "
					+ "  surgryResConfig.specialityId AS specialityId,surgryResConfig.serviceId AS serviceId,splMstr.specialityName AS specialityName,"
					+ "  surgryResConfig.procedureName as procedureName, vtpm.visitTypeName as visitTypeName,"
					+ "  surgryResConfig.tariffId as tariffId,surgryResConfig.visitTypeId as visitTypeId,  trifm.tariffDesc as tariffDesc,"
					+ "  surgryResConfig.surgeryGradeId AS surgeryGradeId,surgGradMstr.surgeryGrade AS surgeryGrade,"
					+ "  surgryResConfig.doctorGradeId AS doctorGradeId , docGrdMstr.docGradeName AS docGradeName ,"
					+ "  surgryResConfig.patientCategoryId AS patientCategoryId,patientCateMstr.patientCategory AS patientCategory,"
					+ "  surgryResConfig.paymentEntitlementId AS paymentEntitlementId ,paymentEntMstr.paymentEntitlementDesc AS paymentEntitlementDesc ,"
					+ "  surgryResConfig.bedCategoryId AS bedCategoryId ,billingBedCatMstr.billingBedDesc AS billingBedDesc,"
					+ "  surgryResConfig.surgeonPercentage AS surgeonPercentage,surgryResConfig.asstSurgeonPercentage AS asstSurgeonPercentage ,"
					+ "  surgryResConfig.anesthesiologistPercentage AS anesthesiologistPercentage ,surgryResConfig.otPercentage AS otPercentage ,"
					+ "  surgryResConfig.status AS status,surgryResConfig.surgeryResourceConfigId AS surgeryResourceConfigId,"
					+ "  surgryResConfig.surgeonDependentOn AS surgeonDependentOn,surgryResConfig.asstSurgeonDependentOn AS asstSurgeonDependentOn,"
					+ "	 surgryResConfig.anesthesiologistDependentOn AS anesthesiologistDependentOn,surgryResConfig.otDependentOn AS otDependentOn"
					+ " FROM "
					+ "  SurgeryResourceConfig surgryResConfig "
					+ " Left JOIN  surgryResConfig.specialityMaster  splMstr "
					+ " left join surgryResConfig.visitTypeMaster  vtpm "
					+ " left join surgryResConfig.tariffMaster  trifm "
					+ " left  JOIN  surgryResConfig.surgeryGradeMaster surgGradMstr "
					+ " left  JOIN  surgryResConfig.doctorGrade docGrdMstr"
					+ " left  JOIN  surgryResConfig.patientCategoryMaster patientCateMstr "
					+ " left JOIN  surgryResConfig.paymentEntitlementMaster paymentEntMstr"
					+ " left JOIN  surgryResConfig.bedCategoryMaster bedCatMstr "
					+ " left JOIN  surgryResConfig.billingBedCategoryMaster billingBedCatMstr "
					+ " WHERE "
					+ " surgryResConfig.orgId=:orgId  AND surgryResConfig.unitId=:unitId "),
	
	@NamedQuery(name="GET_SURGERY_RESOURCE_CONFIG_LIST_BY_PROCEDURE_LIST",
	query=" SELECT "
			+ "  surgryResConfig.specialityId AS specialityId,surgryResConfig.serviceId AS serviceId,splMstr.specialityName AS specialityName,"
			+ "  surgryResConfig.procedureName as procedureName, vtpm.visitTypeName as visitTypeName,"
			+ "  surgryResConfig.tariffId as tariffId,surgryResConfig.visitTypeId as visitTypeId,  trifm.tariffDesc as tariffDesc,"
			+ "  surgryResConfig.surgeryGradeId AS surgeryGradeId,surgGradMstr.surgeryGrade AS surgeryGrade,"
			+ "  surgryResConfig.doctorGradeId AS doctorGradeId , docGrdMstr.docGradeName AS docGradeName ,"
			+ "  surgryResConfig.patientCategoryId AS patientCategoryId,patientCateMstr.patientCategory AS patientCategory,"
			+ "  surgryResConfig.paymentEntitlementId AS paymentEntitlementId ,paymentEntMstr.paymentEntitlementDesc AS paymentEntitlementDesc ,"
			+ "  surgryResConfig.bedCategoryId AS bedCategoryId ,billingBedCatMstr.billingBedDesc AS billingBedDesc,"
			+ "  surgryResConfig.surgeonPercentage AS surgeonPercentage,surgryResConfig.asstSurgeonPercentage AS asstSurgeonPercentage ,"
			+ "  surgryResConfig.anesthesiologistPercentage AS anesthesiologistPercentage ,surgryResConfig.otPercentage AS otPercentage ,"
			+ "  surgryResConfig.status AS status,surgryResConfig.surgeryResourceConfigId AS surgeryResourceConfigId,"
			+ "  surgryResConfig.surgeonDependentOn AS surgeonDependentOn,surgryResConfig.asstSurgeonDependentOn AS asstSurgeonDependentOn,"
			+ "	 surgryResConfig.anesthesiologistDependentOn AS anesthesiologistDependentOn,surgryResConfig.otDependentOn AS otDependentOn"
			+ " FROM "
			+ "  SurgeryResourceConfig surgryResConfig "
			+ " Left JOIN  surgryResConfig.specialityMaster  splMstr "
			+ " left join surgryResConfig.visitTypeMaster  vtpm "
			+ " left join surgryResConfig.tariffMaster  trifm "
			+ " left  JOIN  surgryResConfig.surgeryGradeMaster surgGradMstr "
			+ " left  JOIN  surgryResConfig.doctorGrade docGrdMstr"
			+ " left  JOIN  surgryResConfig.patientCategoryMaster patientCateMstr "
			+ " left JOIN  surgryResConfig.paymentEntitlementMaster paymentEntMstr"
			+ " left JOIN  surgryResConfig.bedCategoryMaster bedCatMstr "
			+ " left JOIN  surgryResConfig.billingBedCategoryMaster billingBedCatMstr "
			+ " WHERE "
			+ " surgryResConfig.orgId=:orgId  AND surgryResConfig.unitId=:unitId AND surgryResConfig.status='A'"
			+ " and surgryResConfig.serviceId=:procedureId  "
			+ " AND surgryResConfig.bedCategoryId=:bedCatId "
			+ " AND surgryResConfig.tariffId=:tariffId "
			+ " AND surgryResConfig.paymentEntitlementId=:paymentEntitlementId "
			+ " AND surgryResConfig.visitTypeId=:visitTypeId "
			+ " AND surgryResConfig.patientCategoryId=:patientCategoryId "
			
			),
	
	@NamedQuery(name="GET_SURGERY_RESOURCE_CONFIG_LIST_BY_ID",
		query=" SELECT "
				+ "  surgryResConfig.specialityId AS specialityId,surgryResConfig.serviceId AS serviceId,splMstr.specialityName AS specialityName,"
				+ "  surgryResConfig.procedureName as procedureName, vtpm.visitTypeName as visitTypeName,"
				+ "  surgryResConfig.tariffId as tariffId,surgryResConfig.visitTypeId as visitTypeId,  trifm.tariffDesc as tariffDesc,"
				+ "  surgryResConfig.surgeryGradeId AS surgeryGradeId,surgGradMstr.surgeryGrade AS surgeryGrade,"
				+ "  surgryResConfig.doctorGradeId AS doctorGradeId , docGrdMstr.docGradeName AS docGradeName ,"
				+ "  surgryResConfig.patientCategoryId AS patientCategoryId,patientCateMstr.patientCategory AS patientCategory,"
				+ "  surgryResConfig.paymentEntitlementId AS paymentEntitlementId ,paymentEntMstr.paymentEntitlementDesc AS paymentEntitlementDesc ,"
				+ "  surgryResConfig.bedCategoryId AS bedCategoryId ,bedCatMstr.billingBedDesc AS billingBedDesc,"
				+ "  surgryResConfig.surgeonPercentage AS surgeonPercentage,surgryResConfig.asstSurgeonPercentage AS asstSurgeonPercentage ,"
				+ "  surgryResConfig.anesthesiologistPercentage AS anesthesiologistPercentage ,surgryResConfig.otPercentage AS otPercentage ,"
				+ "  surgryResConfig.status AS status,surgryResConfig.surgeryResourceConfigId AS surgeryResourceConfigId,"
				+ "  surgryResConfig.surgeonDependentOn AS surgeonDependentOn,surgryResConfig.asstSurgeonDependentOn AS asstSurgeonDependentOn,"
				+ "	 surgryResConfig.anesthesiologistDependentOn AS anesthesiologistDependentOn,surgryResConfig.otDependentOn AS otDependentOn "
				+ " FROM SurgeryResourceConfig surgryResConfig "
				+ " left JOIN  surgryResConfig.specialityMaster  splMstr "
				+ " left join surgryResConfig.visitTypeMaster  vtpm "
				+ " left join surgryResConfig.tariffMaster  trifm "
				+ " left JOIN surgryResConfig.surgeryGradeMaster surgGradMstr "
				+ " left JOIN  surgryResConfig.doctorGrade docGrdMstr"
				+ " left JOIN  surgryResConfig.patientCategoryMaster patientCateMstr "
				+ " left JOIN   surgryResConfig.paymentEntitlementMaster paymentEntMstr"
				+ " left JOIN  surgryResConfig.billingBedCategoryMaster bedCatMstr "
				+ " WHERE surgryResConfig.surgeryResourceConfigId=:surgeryResourceConfigId"),
	
	@NamedQuery(name="CHECK_ALREADY_EXIST_OR_NOT",
	query=" SELECT "
		+ "  surgryResConfig.surgeryResourceConfigId AS surgeryResourceConfigId "
		+ " FROM SurgeryResourceConfig surgryResConfig "		
		+ " WHERE surgryResConfig.serviceId=:serviceId "
		+ "   AND surgryResConfig.tariffId=:tariffId "
		+ "   AND surgryResConfig.visitTypeId=:visitTypeId "
		+ "   AND surgryResConfig.patientCategoryId=:patientCategoryId "
		+ "		AND surgryResConfig.paymentEntitlementId=:paymentEntitleId"
		+ "		AND surgryResConfig.bedCategoryId=:bedCatId"
		+ "		AND surgryResConfig.orgId =:orgId AND surgryResConfig.unitId =:unitId "),
	
	@NamedQuery(name = "GET_SURGERY_RES_CONFIG_FOR_PROCEDURE_POSTING_BY_CRITERIA",
			   query = "SELECT		src.surgeryResourceConfigId as surgeryResourceConfigId, src.specialityId as specialityId, src.surgeryGradeId as surgeryGradeId, "
			   		+ "				src.doctorGradeId as doctorGradeId, src.patientCategoryId as patientCategoryId, src.paymentEntitlementId as paymentEntitlementId, "
			   		+ "				src.bedCategoryId as bedCategoryId, src.surgeonPercentage as surgeonPercentage, src.asstSurgeonPercentage as asstSurgeonPercentage, "
			   		+ "				src.anesthesiologistPercentage as anesthesiologistPercentage, src.otPercentage as otPercentage, src.status as status, src.orgId as orgId, "
			   		+ "				src.unitId as unitId, src.surgeonDependentOn as surgeonDependentOn, src.asstSurgeonDependentOn as asstSurgeonDependentOn, src.anesthesiologistDependentOn as anesthesiologistDependentOn, "
			   		+ "				src.otDependentOn as otDependentOn "
			   		+ "FROM			SurgeryResourceConfig src "
			   		+ "WHERE		src.specialityId =:specialityId AND src.surgeryGradeId =:surgeryGradeId AND src.doctorGradeId =:doctorGradeId and status = 'A' "
			   		+ "				and src.patientCategoryId =:patientCategoryId and src.paymentEntitlementId =:paymentEntitlementId and src.bedCategoryId =:bedCategoryId "
			   		+ "	and src.orgId =:orgId and src.unitId =:unitId ")
})

@Entity
@Table(name = "t_surgery_resource_config" , schema = "otims")
@SequenceGenerator(name = "t_surgery_resource_config_seq" , sequenceName = "otims.t_surgery_resource_config_seq" , allocationSize = 1)
public class SurgeryResourceConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_surgery_resource_config_seq")
	@Column(name = "surgery_resource_config_id")
	private int surgeryResourceConfigId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "surgery_grade_id")
	private Integer surgeryGradeId;
	
	@Column(name = "doctor_grade_id")
	private Integer doctorGradeId;
	
	@Column(name = "patient_category_id")
	private Integer patientCategoryId;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name = "surgeon_percentage")
	private double surgeonPercentage;
	
	@Column(name = "asst_surgeon_percentage")
	private double asstSurgeonPercentage;
	
	@Column(name = "anesthesiologist_percentage")
	private double anesthesiologistPercentage;
	
	@Column(name = "ot_percentage")
	private double otPercentage;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "surgeon_dependent_on")
	private Integer surgeonDependentOn;
	
	@Column(name = "asst_surgeon_dependent_on")
	private Integer asstSurgeonDependentOn;
	
	@Column(name = "anesthesiologist_dependent_on")
	private Integer anesthesiologistDependentOn;
	
	@Column(name = "ot_dependent_on")
	private Integer otDependentOn;
	
	@Column(name = "tariff_id")
	private Integer tariffId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "procedure_name")
	private String procedureName;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_category_id" , insertable = false , updatable = false , nullable = false)
	private BedCategoryMaster bedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_category_id" , insertable = false , updatable = false , nullable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_grade_id" , insertable = false , updatable = false , nullable = false)
	private DoctorGrade doctorGrade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_category_id" , insertable = false , updatable = false , nullable = false)
	private PatientCategoryMaster patientCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_entitlement_id" , insertable = false , updatable = false , nullable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surgery_grade_id" , insertable = false , updatable = false , nullable = false)
	private SurgeryGradeMaster surgeryGradeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id" , insertable = false , updatable = false , nullable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id" , insertable = false , updatable = false , nullable = false)
	private TariffMaster tariffMaster;

	public int getSurgeryResourceConfigId() {
		return surgeryResourceConfigId;
	}

	public void setSurgeryResourceConfigId(int surgeryResourceConfigId) {
		this.surgeryResourceConfigId = surgeryResourceConfigId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public Integer getDoctorGradeId() {
		return doctorGradeId;
	}

	public void setDoctorGradeId(Integer doctorGradeId) {
		this.doctorGradeId = doctorGradeId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public double getSurgeonPercentage() {
		return surgeonPercentage;
	}

	public void setSurgeonPercentage(double surgeonPercentage) {
		this.surgeonPercentage = surgeonPercentage;
	}

	public double getAsstSurgeonPercentage() {
		return asstSurgeonPercentage;
	}

	public void setAsstSurgeonPercentage(double asstSurgeonPercentage) {
		this.asstSurgeonPercentage = asstSurgeonPercentage;
	}

	public double getAnesthesiologistPercentage() {
		return anesthesiologistPercentage;
	}

	public void setAnesthesiologistPercentage(double anesthesiologistPercentage) {
		this.anesthesiologistPercentage = anesthesiologistPercentage;
	}

	public double getOtPercentage() {
		return otPercentage;
	}

	public void setOtPercentage(double otPercentage) {
		this.otPercentage = otPercentage;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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

	public BedCategoryMaster getBedCategoryMaster() {
		return bedCategoryMaster;
	}

	public void setBedCategoryMaster(BedCategoryMaster bedCategoryMaster) {
		this.bedCategoryMaster = bedCategoryMaster;
	}

	public DoctorGrade getDoctorGrade() {
		return doctorGrade;
	}

	public void setDoctorGrade(DoctorGrade doctorGrade) {
		this.doctorGrade = doctorGrade;
	}

	public PatientCategoryMaster getPatientCategoryMaster() {
		return patientCategoryMaster;
	}

	public void setPatientCategoryMaster(PatientCategoryMaster patientCategoryMaster) {
		this.patientCategoryMaster = patientCategoryMaster;
	}

	public PaymentEntitlementMaster getPaymentEntitlementMaster() {
		return paymentEntitlementMaster;
	}

	public void setPaymentEntitlementMaster(PaymentEntitlementMaster paymentEntitlementMaster) {
		this.paymentEntitlementMaster = paymentEntitlementMaster;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public SurgeryGradeMaster getSurgeryGradeMaster() {
		return surgeryGradeMaster;
	}

	public void setSurgeryGradeMaster(SurgeryGradeMaster surgeryGradeMaster) {
		this.surgeryGradeMaster = surgeryGradeMaster;
	}

	public Integer getSurgeonDependentOn() {
		return surgeonDependentOn;
	}

	public void setSurgeonDependentOn(Integer surgeonDependentOn) {
		this.surgeonDependentOn = surgeonDependentOn;
	}

	public Integer getAsstSurgeonDependentOn() {
		return asstSurgeonDependentOn;
	}

	public void setAsstSurgeonDependentOn(Integer asstSurgeonDependentOn) {
		this.asstSurgeonDependentOn = asstSurgeonDependentOn;
	}

	public Integer getAnesthesiologistDependentOn() {
		return anesthesiologistDependentOn;
	}

	public void setAnesthesiologistDependentOn(Integer anesthesiologistDependentOn) {
		this.anesthesiologistDependentOn = anesthesiologistDependentOn;
	}

	public Integer getOtDependentOn() {
		return otDependentOn;
	}

	public void setOtDependentOn(Integer otDependentOn) {
		this.otDependentOn = otDependentOn;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	
}
