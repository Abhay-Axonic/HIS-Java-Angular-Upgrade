package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DrugAdministrationItem
{
	 private Long id;
	 
	 private Date prescriptionDate;
	 
	 private Integer itemId;
	 
	 private String itemCode;
	 
	 private String itemName;
	 
	 private String consumptionMethod;
	 
	 private String frequency;
	 private Integer infusionSolutionId;
	 private Integer infusionTolalVolume;
	 private Long prescriptionId;

	 public List<DoseTime1> getDoseTime() {
		return doseTime;
	}

	public void setDoseTime(List<DoseTime1> doseTime) {
		this.doseTime = doseTime;
	}

	private Integer frequencyId;
	 
	 private Integer noOfAdministration;
	 
	 private Double dose;
	 
	 private Date startDate;
	 
	 private Date stopDate;
	 
	 private String startTime;
	 
	 private String stopTime;
	 
	 private Boolean isPreStop;
	 
	 private Date preStopStartDate;
	 
	 private Date preStopEndDate;
	 
	 private Boolean prnFlag;
	 
	 private Boolean statFlag;
	 
	 private Boolean infusionFlag;
	 
	private Date prnStartDate;
	
	private String prnInstruction;
	
	private List<DoseTime1> doseTime;
	
	private Integer drugAdministrationDetailId;
	
	private Integer parentSubstituteId;
	
	private Boolean isSubstitute;
	 
	private Integer orderQty;
	
	private BigInteger acceptQty;
	
	private BigInteger rejectedQuantity;
	private BigInteger returnQuantity;

	private Character isMedicationStop;

	 private Boolean antibioticTest;
	 
	 private Long saleDetailsId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public DrugAdministrationItem() {
		super();
	}

	public String getConsumptionMethod() {
		return consumptionMethod;
	}

	public void setConsumptionMethod(String consumptionMethod) {
		this.consumptionMethod = consumptionMethod;
	}

	public Integer getNoOfAdministration() {
		return noOfAdministration;
	}

	public void setNoOfAdministration(Integer noOfAdministration) {
		this.noOfAdministration = noOfAdministration;
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

	

	public Boolean getIsPreStop() {
		return isPreStop;
	}

	public void setIsPreStop(Boolean isPreStop) {
		this.isPreStop = isPreStop;
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

	public Integer getDrugAdministrationDetailId() {
		return drugAdministrationDetailId;
	}

	public void setDrugAdministrationDetailId(Integer drugAdministrationDetailId) {
		this.drugAdministrationDetailId = drugAdministrationDetailId;
	}

	public DrugAdministrationItem(Long id, Date prescriptionDate, Integer itemId, String itemCode, String itemName,
			String consumptionMethod, String frequency, Integer frequencyId, Integer noOfAdministration, Double dose, Date startDate,
			Date stopDate, Boolean isPreStop, Date preStopStartDate, Date preStopEndDate,
			Boolean prnFlag, Boolean statFlag, Boolean infusionFlag, Date prnStartDate,
			String prnInstruction) {
		super();
		this.id = id;
		this.prescriptionDate = prescriptionDate;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.consumptionMethod = consumptionMethod;
		this.frequency = frequency;
		this.frequencyId = frequencyId;
		this.noOfAdministration = noOfAdministration;
		this.dose = dose;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.isPreStop = isPreStop;
		this.preStopStartDate = preStopStartDate;
		this.preStopEndDate = preStopEndDate;
		this.prnFlag = prnFlag;
		this.statFlag = statFlag;
		this.infusionFlag = infusionFlag;
		this.prnStartDate = prnStartDate;
		this.prnInstruction = prnInstruction;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public Boolean getIsSubstitute() {
		return isSubstitute;
	}

	public void setIsSubstitute(Boolean isSubstitute) {
		this.isSubstitute = isSubstitute;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public BigInteger getAcceptQty() {
		return acceptQty;
	}

	public void setAcceptQty(BigInteger acceptQty) {
		this.acceptQty = acceptQty;
	}

	public BigInteger getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(BigInteger rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public BigInteger getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(BigInteger returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

	public Integer getInfusionTolalVolume() {
		return infusionTolalVolume;
	}

	public void setInfusionTolalVolume(Integer infusionTolalVolume) {
		this.infusionTolalVolume = infusionTolalVolume;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Character getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(Character isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}

	public Boolean getAntibioticTest() {
		return antibioticTest;
	}

	public void setAntibioticTest(Boolean antibioticTest) {
		this.antibioticTest = antibioticTest;
	}

	public Long getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Long saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}
}