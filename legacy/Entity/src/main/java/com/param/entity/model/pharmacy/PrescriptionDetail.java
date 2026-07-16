package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.ConsumableMethod;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.User;

@Entity(name="PrescriptionDetail")
@Table(name="t_prescription_detail",schema="pharmacy")
public class PrescriptionDetail extends BaseEntity implements Serializable {
	public List<SaleDetail> getSaleDetails() {
		return saleDetails;
	}

	public void setSaleDetails(List<SaleDetail> saleDetails) {
		this.saleDetails = saleDetails;
	}


	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false )
	private Long id;

	@Column(name="authorised_by")
	private Integer authorisedBy;

	@Column(name="authorised_date")
	private Timestamp authorisedDate;

	@Column(name="discontinue_by")
	private Integer discontinueBy;

	@Column(name="discontinue_on")
	private Timestamp discontinueOn;

	@Column(name="is_discontinued" )
	private Boolean isDiscontinued;

	@Column(name="is_intervention")
	private Boolean isIntervention;

	@Column(name="is_pre_stop")
	private Boolean isPreStop;
	
	@Column(name="prnflag")
	private Boolean prnFlag;
	
	@Column(name="statflag")
	private Boolean statFlag;
	
	@Column(name="infusionflag")
	private Boolean infusionFlag;
	
	@Column(name="pre_stop_start_date")
	private Date preStopStartDate;
	
	@Column(name="pre_stop_end_date")
	private Date preStopEndDate;
	
	@Column(name="prn_start_date")
	private Date prnStartDate;
	
	@Column(name="prn_instruction")
	private String prnInstruction;

	@Column(name="infusion_solution_id")
	private Integer infusionSolutionId;
	
	@Column(name="is_solution")
	private boolean isSolution;
	
	@Column(name="is_additive")
	private boolean isAdditive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@Column(name="order_qty" )
	private Integer orderQty;

	@Column(name="pending_qty" )
	private Integer pendingQty;

	@Column(name="prescription_date")
	private Timestamp prescriptionDate;

	@Column(name="refill_qty")
	private Integer refillQty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="store_id" )
	private Store store;
	
	@Column(name="strength" )
	private Double strength;

	@Column(name="uom_unit_id" )
	private Integer uomUnitId;

	@Column(name="is_non_formulary")
	private Character isNonFormulary;
	
	@Column(name="instruction_desc")
	private String instructionDesc;

	@Column(name="is_medication_stop")
	private char isMedicationStop;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="prescription_id" )
	private Prescription prescription;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="consumable_method_id" )
	private ConsumableMethod consumableMethod;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="parent_substitute_id")
	private Integer parentSubstituteId;
	/*@
	 Column(name="revised_order_quantity")
	private Integer revisedOrderQuantity;
	*/
	@Column(name="prescribe_dose")
    private Double prescribedDose;
	
//	@Column(name="prescribe_dose_unit_id")
//	private Integer prescribedDoseUnitId;
	
	@Column(name="factor_id")
    private Integer factorId;
	
	@Column(name="formulation_dose_mapp_id")
	private Integer formulationDosageMappId;
	
	@Column(name="duration")
	private Integer duration;
	
	@Column(name="duration_unit")
	private String durationUnit;
	
	@Column(name="priority_id")
	private Integer priorityId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="prescriptionDetail",cascade = CascadeType.ALL)
	private List<PrescriptionFrequencyDetail> PrescriptionFrequencyDetails;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="prescriptionDetail",cascade = CascadeType.ALL)
	private List<IssueDetail> issueDetail;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="prescriptionDetail",cascade = CascadeType.ALL)
	private List<DrugAdministrationDetail> drugAdministrationDetail;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="prescriptionDetail",cascade = CascadeType.ALL)
	private List<SaleDetail> saleDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="instruction_id")
	private Instruction instruction;
	
	@Column(name="revised_ordered_quantity" )
	private Integer revisedOrderedQuantity;
	@Column(name="is_discharge_medication")
	private Boolean isDischarge;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="prescribe_dose_unit_id")
	private StrengthUnit strengthUnitPresDTL;
	
	@Column(name="is_manually_added_item")
	private boolean isManuallyAddedItem;
	
	@Column(name="infusion_total_volume")
	private Integer infusionTotalVolume;
	
	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}

	public PrescriptionDetail() {
		super();
	}

	public PrescriptionDetail(Long id) {
		super();
		this.id = id;
	}
	
	public PrescriptionDetail(Long id, Integer revisedOrderedQuantity) {
		super();
		this.id = id;
		this.revisedOrderedQuantity = revisedOrderedQuantity;
	}

	public PrescriptionDetail(Long id, Integer authorisedBy, Timestamp authorisedDate, Integer discontinueBy,
			Timestamp discontinueOn, Boolean isDiscontinued, Boolean isIntervention, Boolean isPreStop, Item item,
			Integer orderQty, Integer pendingQty, Timestamp prescriptionDate, Integer refillQty, Store store,
			Double strength, Integer uomUnitId, Prescription prescription,
			List<PrescriptionFrequencyDetail> prescriptionFrequencyDetails) {
		super();
		this.id = id;
		this.authorisedBy = authorisedBy;
		this.authorisedDate = authorisedDate;
		this.discontinueBy = discontinueBy;
		this.discontinueOn = discontinueOn;
		this.isDiscontinued = isDiscontinued;
		this.isIntervention = isIntervention;
		this.isPreStop = isPreStop;
		this.item = item;
		this.orderQty = orderQty;
		this.pendingQty = pendingQty;
		this.prescriptionDate = prescriptionDate;
		this.refillQty = refillQty;
		this.store = store;
		this.strength = strength;
		this.uomUnitId = uomUnitId;
		this.prescription = prescription;
		this.PrescriptionFrequencyDetails = prescriptionFrequencyDetails;
		
	}

	
	
	public PrescriptionDetail(Integer authorisedBy, Timestamp authorisedDate, Boolean isDiscontinued, Boolean isIntervention, Boolean prnFlag,
			Boolean infusionFlag,String prnInstruction, Item item, Status status, Store store,
			 Instruction instruction,Integer orderQty,Boolean isActive, User addedBy,User updatedBy, Boolean isDeleted,
			 Date addedDate, Date updatedDate) {
		super();
		this.authorisedBy = authorisedBy;
		this.authorisedDate = authorisedDate;
		this.isDiscontinued = isDiscontinued;
		this.isIntervention = isIntervention;
		this.prnFlag = prnFlag;
		this.infusionFlag = infusionFlag;
		this.prnInstruction = prnInstruction;
		this.item = item;
		this.status = status;
		this.store = store;
		this.instruction = instruction;
		this.orderQty = orderQty;
		this.isActive = isActive;
		this.addedBy = addedBy;
		this.updatedBy = updatedBy;
		this.isDeleted = isDeleted;
		this.addedDate = addedDate;
		this.updatedDate = updatedDate;
	}
	
	public void updatePrescriptionDetail(Status status) {
		this.status = status;
	}
	

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAuthorisedBy() {
		return this.authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public Timestamp getAuthorisedDate() {
		return this.authorisedDate;
	}

	public void setAuthorisedDate(Timestamp authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public Integer getDiscontinueBy() {
		return this.discontinueBy;
	}

	public void setDiscontinueBy(Integer discontinueBy) {
		this.discontinueBy = discontinueBy;
	}

	public Timestamp getDiscontinueOn() {
		return this.discontinueOn;
	}

	public void setDiscontinueOn(Timestamp discontinueOn) {
		this.discontinueOn = discontinueOn;
	}

	public Boolean getIsDiscontinued() {
		return this.isDiscontinued;
	}

	public void setIsDiscontinued(Boolean isDiscontinued) {
		this.isDiscontinued = isDiscontinued;
	}

	public Boolean getIsIntervention() {
		return this.isIntervention;
	}

	public void setIsIntervention(Boolean isIntervention) {
		this.isIntervention = isIntervention;
	}

	public Boolean getIsPreStop() {
		return this.isPreStop;
	}

	public void setIsPreStop(Boolean isPreStop) {
		this.isPreStop = isPreStop;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ConsumableMethod getConsumableMethod() {
		return consumableMethod;
	}

	public void setConsumableMethod(ConsumableMethod consumableMethod) {
		this.consumableMethod = consumableMethod;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<IssueDetail> getIssueDetail() {
		return issueDetail;
	}

	public void setIssueDetail(List<IssueDetail> issueDetail) {
		this.issueDetail = issueDetail;
	}

	public Integer getOrderQty() {
		return this.orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getPendingQty() {
		return this.pendingQty;
	}

	public void setPendingQty(Integer pendingQty) {
		this.pendingQty = pendingQty;
	}

	public Timestamp getPrescriptionDate() {
		return this.prescriptionDate;
	}

	public void setPrescriptionDate(Timestamp prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Integer getRefillQty() {
		return this.refillQty;
	}

	public void setRefillQty(Integer refillQty) {
		this.refillQty = refillQty;
	}

	public Store getStoreId() {
		return this.store;
	}

	public void setStoreId(Store store) {
		this.store = store;
	}

	public Double getStrength() {
		return this.strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Integer getUomUnitId() {
		return this.uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Prescription getPrescription() {
		return this.prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	

	public List<PrescriptionFrequencyDetail> getPrescriptionFrequencyDetails() {
		return this.PrescriptionFrequencyDetails;
	}

	public void setPrescriptionFrequencyDetails(List<PrescriptionFrequencyDetail> PrescriptionFrequencyDetails) {
		this.PrescriptionFrequencyDetails = PrescriptionFrequencyDetails;
	}

	public PrescriptionFrequencyDetail addPrescriptionFrequencyDetail(PrescriptionFrequencyDetail PrescriptionFrequencyDetail) {
		getPrescriptionFrequencyDetails().add(PrescriptionFrequencyDetail);
		PrescriptionFrequencyDetail.setTPrescriptionDetail(this);

		return PrescriptionFrequencyDetail;
	}

	public PrescriptionFrequencyDetail removePrescriptionFrequencyDetail(PrescriptionFrequencyDetail PrescriptionFrequencyDetail) {
		getPrescriptionFrequencyDetails().remove(PrescriptionFrequencyDetail);
		PrescriptionFrequencyDetail.setTPrescriptionDetail(null);

		return PrescriptionFrequencyDetail;
	}

	
	public PrescriptionFrequencyDetail addPrescriptionFrequencyDetails(PrescriptionFrequencyDetail PrescriptionFrequencyDetail) {
		if (getPrescriptionFrequencyDetails() == null)
			this.PrescriptionFrequencyDetails = new ArrayList<PrescriptionFrequencyDetail>();
		
		getPrescriptionFrequencyDetails().add(PrescriptionFrequencyDetail);
		PrescriptionFrequencyDetail.setPrescriptionDetail(this);

		return PrescriptionFrequencyDetail;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getPrnFlag() {
		return prnFlag;
	}

	public void setPrnFlag(Boolean prnFlag) {
		this.prnFlag = prnFlag;
	}

	public Boolean getStatFlag() {
		return statFlag;
	}

	public void setStatFlag(Boolean statFlag) {
		this.statFlag = statFlag;
	}

	public Boolean getInfusionFlag() {
		return infusionFlag;
	}

	public void setInfusionFlag(Boolean infusionFlag) {
		this.infusionFlag = infusionFlag;
	}

	public Date getPreStopStartDate() {
		return preStopStartDate;
	}

	public void setPreStopStartDate(Date preStopStartDate) {
		this.preStopStartDate = preStopStartDate;
	}

	public Date getPreStopEndDate() {
		return preStopEndDate;
	}

	public void setPreStopEndDate(Date preStopEndDate) {
		this.preStopEndDate = preStopEndDate;
	}

	public Date getPrnStartDate() {
		return prnStartDate;
	}

	public void setPrnStartDate(Date prnStartDate) {
		this.prnStartDate = prnStartDate;
	}

	public String getPrnInstruction() {
		return prnInstruction;
	}

	public void setPrnInstruction(String prnInstruction) {
		this.prnInstruction = prnInstruction;
	}

	public List<DrugAdministrationDetail> getDrugAdministrationDetail() {
		return drugAdministrationDetail;
	}

	public void setDrugAdministrationDetail(List<DrugAdministrationDetail> drugAdministrationDetail) {
		this.drugAdministrationDetail = drugAdministrationDetail;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}
	
    	
    public Integer getRevisedOrderedQuantity() {
		return revisedOrderedQuantity;
	}

	public void setRevisedOrderedQuantity(Integer revisedOrderedQuantity) {
		this.revisedOrderedQuantity = revisedOrderedQuantity;
	}
	

	public void updatePrescriptionDetail(Integer quantityToOrdered) {
		this.revisedOrderedQuantity = quantityToOrdered;
		this.orderQty=quantityToOrdered;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public void updatePrescriptionDetailsStatus(Status status) {
		this.status = status;
		
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public boolean getIsManuallyAddedItem() {
		return isManuallyAddedItem;
	}

	public void setIsManuallyAddedItem(boolean isManuallyAddedItem) {
		this.isManuallyAddedItem = isManuallyAddedItem;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

	public boolean getIsSolution() {
		return isSolution;
	}

	public void setIsSolution(boolean isSolution) {
		this.isSolution = isSolution;
	}

	public boolean getIsAdditive() {
		return isAdditive;
	}

	public void setIsAdditive(boolean isAdditive) {
		this.isAdditive = isAdditive;
	}

	public Integer getInfusionTotalVolume() {
		return infusionTotalVolume;
	}

	public void setInfusionTotalVolume(Integer infusionTotalVolume) {
		this.infusionTotalVolume = infusionTotalVolume;
	}

	public Double getPrescribedDose() {
		return prescribedDose;
	}

	public void setPrescribedDose(Double prescribedDose) {
		this.prescribedDose = prescribedDose;
	}

//	public Integer getPrescribedDoseUnitId() {
//		return prescribedDoseUnitId;
//	}
//
//	public void setPrescribedDoseUnitId(Integer prescribedDoseUnitId) {
//		this.prescribedDoseUnitId = prescribedDoseUnitId;
//	}

	public Integer getFactorId() {
		return factorId;
	}

	public void setFactorId(Integer factorId) {
		this.factorId = factorId;
	}

	public Integer getFormulationDosageMappId() {
		return formulationDosageMappId;
	}

	public void setFormulationDosageMappId(Integer formulationDosageMappId) {
		this.formulationDosageMappId = formulationDosageMappId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public StrengthUnit getStrengthUnitPresDTL() {
		return strengthUnitPresDTL;
	}

	public void setStrengthUnitPresDTL(StrengthUnit strengthUnitPresDTL) {
		this.strengthUnitPresDTL = strengthUnitPresDTL;
	}

	public char getIsMedicationStop() {
		return isMedicationStop = (isMedicationStop != '\u0000' ? isMedicationStop : 'N');
	}

	public void setIsMedicationStop(char isMedicationStop) {
		this.isMedicationStop = (isMedicationStop != '\u0000' ? isMedicationStop : 'N');
	}
	
	
	/*public Integer getRevisedOrderQuantity() {
		return revisedOrderQuantity;
	}

	public void setRevisedOrderQuantity(Integer revisedOrderQuantity) {
		this.revisedOrderQuantity = revisedOrderQuantity;
	}*/
	
	
}