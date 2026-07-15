package com.param.global.dto.items;

public class ItemsSearchResponse {

	protected Integer id;

	protected String details;

	protected Integer assetCategoryId;
	
	protected Integer productCategoryId;
	
	private double patientClassPercentage;
	
	private boolean markupStatus;
	
	private Integer markUpDown;

	private double markUpDownPer;
	private Integer formulationTypeId;

    private boolean isThreeUOMApplicable;
	private Boolean isLasa;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public ItemsSearchResponse() {
		super();
	}

	public ItemsSearchResponse(Integer id, String details) {
		this.id = id;
		this.details = details;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(double patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}

	public boolean isMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(boolean markupStatus) {
		this.markupStatus = markupStatus;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
}