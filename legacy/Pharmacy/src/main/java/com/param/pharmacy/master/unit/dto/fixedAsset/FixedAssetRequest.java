package com.param.pharmacy.master.unit.dto.fixedAsset;

public class FixedAssetRequest extends FixedAssetResponse {

	private Integer itemRequestId;

	private Boolean isCssd;

	private Boolean isModality;

	private Boolean isChargeableEqupiment;
	protected Boolean isAccessories;

	protected Boolean isSpares;
	protected Boolean isDialyser ;
	protected Boolean isCritical ;

	public Boolean getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Boolean isCritical) {
		this.isCritical = isCritical;
	}

	public Boolean getIsDialyser() {
		return isDialyser;
	}

	public void setIsDialyser(Boolean isDialyser) {
		this.isDialyser = isDialyser;
	}

	public Boolean getIsAccessories() {
		return isAccessories;
	}

	public void setIsAccessories(Boolean isAccessories) {
		this.isAccessories = isAccessories;
	}

	public Boolean getIsSpares() {
		return isSpares;
	}

	public void setIsSpares(Boolean isSpares) {
		this.isSpares = isSpares;
	}

	public FixedAssetRequest() {
		super();
	}

	public Integer getItemRequestId() {
		return itemRequestId;
	}

	public void setItemRequestId(Integer itemRequestId) {
		this.itemRequestId = itemRequestId;
	}

	public Boolean getIsCssd() {
		return isCssd;
	}

	public void setIsCssd(Boolean isCssd) {
		this.isCssd = isCssd;
	}

	public Boolean getIsModality() {
		return isModality;
	}

	public void setIsModality(Boolean isModality) {
		this.isModality = isModality;
	}

	public Boolean getIsChargeableEqupiment() {
		return isChargeableEqupiment;
	}

	public void setIsChargeableEqupiment(Boolean isChargeableEqupiment) {
		this.isChargeableEqupiment = isChargeableEqupiment;
	}

}
