package com.param.pharmacy.ip.dto.pharmacology;

public class PharmacologyUpdateItem
{
	 private Long id;
	 
     private boolean isIntervention;
     
     private Integer itemStatusId;
     
     private String remark;
     
     private Long prescriptionDetailId;
     
     private Boolean isNarcotic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isIntervention() {
		return isIntervention;
	}

	public Integer getItemStatusId() {
		return itemStatusId;
	}

	public void setItemStatusId(Integer itemStatusId) {
		this.itemStatusId = itemStatusId;
	}

	public void setIntervention(boolean isIntervention) {
		this.isIntervention = isIntervention;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Long prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}
     
	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}
}