package com.param.adt.master.unit.model;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({

        @NamedQuery(name="IS_DOCTOR_FAVORITES_MEDICATION_EXIST",query="SELECT docFav.favouriteMedicationId as favouriteMedicationId, "
                + "docFav.doctorId as doctorId, "
                + "docFav.itemId as itemId, "
                + "docFav.roaId as roaId, "
                + "docFav.dose as dose, "
                + "docFav.doseUnitId as doseUnitId, "
                + "docFav.frequencyId as frequencyId "
                + "FROM DoctorFavouriteMedicationsMaster docFav "
                + "WHERE docFav.status='A' "
                + "AND docFav.organizationId=:orgId "
                + "AND docFav.unitId=:unitId "),

})

@NamedNativeQueries({

        @NamedNativeQuery(name = "GET_DOCTOR_FAVORITES_MEDICATION_LIST_BY_DOC_ID", query = "SELECT docFav.favourite_medication_id as \"favouriteMedicationId\",  "
                + " docFav.doctor_id as \"doctorId\",  "
                + " docFav.speciality_id as \"specialityId\",  "
//                + " coalesce(temp.rate, 0) as \"rate\",  "
                + " case when (docFav.item_id is null) then 0 else docFav.item_id end AS \"itemId\", "
                + " case when (gen.id is null) then 0 else gen.id end AS \"genericId\", "
                + " case when (itm.name is null) then docFav.item_name else itm.name end AS \"itemName\", "
                + " case when (gen.name is null) then docFav.generic_name else gen.name end AS \"genericName\", "
                + " itm.is_dose_calculation_applicable as \"isDoseCalculationApplicable\", "
                + " itm.dose_calculation_applicable_data as \"doseCalculationApplicableData\", "
                + " docFav.roa_id as \"roaId\",  "
                + " docFav.order_qty as \"quantity\",  "
                + " docFav.duration as \"durationCount\",  "
                + " roa.description as \"roaDesc\",  "
                + " docFav.dose_unit_id as \"doseUnitId\",  "
                + " docFav.dose as \"dose\",  "
                + " su.unit as \"doseDesc\",  "
                + " docFav.frequency_id as \"frequencyId\",  "
                + " freq.description as \"frequencyDesc\",  "
                + " docFav.instruction_id as \"instructionId\" , "
                + " instr.instruction_desc as \"instructionDesc\",  "
                + " docFav.statflag as \"isStat\","
                + "  docFav.order_qty as \"orderQty\",   " +
                "  docFav.is_medication_stop as \"isMedicationStop\",   " +
                " docFav.is_non_formulary as \"isNonFormulary\",   " +
                " docFav.strength as \"strength\",   " +
                " docFav.duration_unit as \"durationUnit\","
                + " docFav.instruction_desc \"instructionText\",  "
                + " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\", "
                + " docFav.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",docFav.factor_id as \"factorId\", "
                + " docFav.formulation_dose_mapp_id as \"formulationDosageMappId\", "
                + " fr.no_of_administration_time as \"noOfAdministrationTime\",  "
                + " itm.formulation_type_id as \"formulationTypeId\"  " + " FROM doctor.m_doctor_favorite_medications_master docFav  "
                + " LEFT JOIN public.m_item itm on itm.id=docFav.item_id  "
                + " LEFT JOIN public.m_route_of_administration roa on roa.id=docFav.roa_id  "
                + " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id  "
                + " INNER JOIN public.m_frequency freq on freq.id=docFav.frequency_id  "
                + " LEFT JOIN public.m_strength_unit su ON docFav.dose_unit_id = su.id  "
                + " INNER JOIN public.m_frequency fr ON docFav.frequency_id = fr.id  "
                + " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=docFav.formulation_dose_mapp_id  "
                + " LEFT JOIN public.m_instruction instr on instr.instruction_id=docFav.instruction_id  "
                + " LEFT JOIN public.m_strength_unit presDose ON docFav.prescribe_dose_unit_id = presDose.id "
//                + "  LEFT JOIN ( select  max( cast((b.rate) as decimal(10, 2))) rate , mi.id item_id "
//                + "	from inventory.t_batch b  "
//                + "	join inventory.t_store_stock S on B.id = S.batch_id  "
//                + "	join m_item mi on b.item_id = mi.id  "
//                + "	join m_store ms on s.store_id = ms.store_id  "
//                + "	join m_store_item_mapper msim on msim.item_id = item.id  "
//                + "	where  "
//                + "	msim.unit_id = :unitId  "
//                + "	and ms.unit_id = :unitId  " +
//                "	and ms.is_pharmacy = 'Y' " +
//                "	and b.unit_id = :unitId  " +
//                "	and s.unit_id = :unitId  " +
//                "	and to_char(b.expiry_date, 'yyyy-mm-dd') >= :expDate  " +
//                "	and b.is_quarantine = 'N'  " +
//                "	group by itm.id  " +
//                "	) as temp on temp.item_id = docFav.item_id "
                + " WHERE docFav.organization_id=:orgId "
                + " AND docFav.unit_id=:unitId "
                + " AND docFav.status='A' "),

})

@Entity
@Table(name = "m_doctor_favorite_medications_master", schema = "doctor")
@SequenceGenerator(name = "doctor_favorite_medications_master_seq", sequenceName = "doctor.doctor_favorite_medications_master_seq", allocationSize = 1)
public class DoctorFavouriteMedicationsMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_favorite_medications_master_seq")
    @Column(name = "favourite_medication_id")
    private Integer favouriteMedicationId;

    @Column(name="doctor_id")
    private Integer doctorId;

    @Column(name="speciality_id")
    private Integer specialityId;


    @Column(name="item_id")
    private Integer itemId;

    @Column(name="roa_id")
    private Integer roaId;

    @Column(name="dose_unit_id")
    private Integer doseUnitId;

    @Column(name="frequency_id")
    private Integer frequencyId;

    @Column(name = "status")
    private Character status;

    @Column(name="dose")
    private Double dose;

    @Column(name="no_of_administration_time")
    private Integer noOfAdministrationTime;

    @Column(name="prescribe_dose")
    private Double prescribedDose;

    @Column(name="prescribe_dose_unit_id")
    private Integer prescribedDoseUnitId;

    @Column(name="instruction_desc")
    private String instructionDesc;

    @Column(name="formulation_dose_mapp_id")
    private Integer formulationDosageMappId;

    @Column(name="statflag")
    private Boolean statflag;

    @Column(name="instruction_id")
    private Integer instructionId;

    @Column(name="factor_id")
    private Integer factorId;

    @Column(name="is_medication_stop")
    private Character isMedicationStop;

    @Column(name="is_non_formulary")
    private Character isNonFormulary;

    @Column(name="strength")
    private Double strength;

    @Column(name="duration")
    private Integer duration;

    @Column(name="order_qty")
    private Integer orderQty;

    @Column(name="duration_unit")
    private String durationUnit;


    @Column(name="organization_id")
    private Integer organizationId;

    @Column(name="unit_id")
    private Integer unitId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "generic_name")
    private String genericName;

    public Integer getFavouriteMedicationId() {
        return favouriteMedicationId;
    }

    public void setFavouriteMedicationId(Integer favouriteMedicationId) {
        this.favouriteMedicationId = favouriteMedicationId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getRoaId() {
        return roaId;
    }

    public void setRoaId(Integer roaId) {
        this.roaId = roaId;
    }

    public Integer getDoseUnitId() {
        return doseUnitId;
    }

    public void setDoseUnitId(Integer doseUnitId) {
        this.doseUnitId = doseUnitId;
    }

    public Integer getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(Integer frequencyId) {
        this.frequencyId = frequencyId;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public Integer getNoOfAdministrationTime() {
        return noOfAdministrationTime;
    }

    public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
        this.noOfAdministrationTime = noOfAdministrationTime;
    }

    public Double getPrescribedDose() {
        return prescribedDose;
    }

    public void setPrescribedDose(Double prescribedDose) {
        this.prescribedDose = prescribedDose;
    }

    public Integer getPrescribedDoseUnitId() {
        return prescribedDoseUnitId;
    }

    public void setPrescribedDoseUnitId(Integer prescribedDoseUnitId) {
        this.prescribedDoseUnitId = prescribedDoseUnitId;
    }

    public String getInstructionDesc() {
        return instructionDesc;
    }

    public void setInstructionDesc(String instructionDesc) {
        this.instructionDesc = instructionDesc;
    }

    public Integer getFormulationDosageMappId() {
        return formulationDosageMappId;
    }

    public void setFormulationDosageMappId(Integer formulationDosageMappId) {
        this.formulationDosageMappId = formulationDosageMappId;
    }

    public Boolean getStatflag() {
        return statflag;
    }

    public void setStatflag(Boolean statflag) {
        this.statflag = statflag;
    }

    public Integer getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Integer instructionId) {
        this.instructionId = instructionId;
    }

    public Integer getFactorId() {
        return factorId;
    }

    public void setFactorId(Integer factorId) {
        this.factorId = factorId;
    }

    public Character getIsMedicationStop() {
        return isMedicationStop;
    }

    public void setIsMedicationStop(Character isMedicationStop) {
        this.isMedicationStop = isMedicationStop;
    }

    public Character getIsNonFormulary() {
        return isNonFormulary;
    }

    public void setIsNonFormulary(Character isNonFormulary) {
        this.isNonFormulary = isNonFormulary;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}
