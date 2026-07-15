package com.param.global.pharmacy.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.param.global.billing.dto.PharmacyBillCommonDto;

public class WalkInSalesRequest extends PharmacyBillCommonDto{

	protected Long tempBillId;

	protected Integer screenId;

	protected Integer prefixId;

	protected String patientName;

	protected String patientAddress;

	protected Integer patientAge;

	protected Integer patientGenderId;

	protected String doctorName;

	protected String doctorAddress;

	protected Integer statusId;

	protected Integer counterId;

	protected Integer orderId;

	protected ItemRequest item;

	protected Integer tempPatientId;

	private List<ItemRequest> items;

	protected Character isContractChanged;
	
	protected Character isPayeeUpdated;
	
	private Character isSmartApplicable;

	protected String mobileNumber;

	private Boolean isFromOt;
	private Boolean cashSettlement;
	
	private Boolean searchPatient;
	
	private Integer doctorId;
	
	private Integer doctorType;
	
	private boolean atCopToSave;
	
	private Boolean isVirtualStore;
	
	private boolean isDummyService;
	
	private boolean barcodeScanned;
    private boolean isConsolidatedFlag;
	public boolean getBarcodeScanned() {
		return barcodeScanned;
	}

	public void setBarcodeScanned(boolean barcodeScanned) {
		this.barcodeScanned = barcodeScanned;
	}

	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

	public Boolean getCashSettlement() {
		return cashSettlement;
	}

	public void setCashSettlement(Boolean cashSettlement) {
		this.cashSettlement = cashSettlement;
	}

	public Integer getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Integer entitlementId) {
		this.entitlementId = entitlementId;
	}

	private String validationMessage;

	protected Integer entitlementId;
	
	private boolean addToBill;
	
	public boolean isAddToBill() {
		return addToBill;
	}

	public void setAddToBill(boolean addToBill) {
		this.addToBill = addToBill;
	}

	public WalkInSalesRequest() {
		super();
	}

	public WalkInSalesRequest(Integer screenId, Long tempBillId, ItemRequest item) {
		super();
		this.screenId = screenId;
		this.tempBillId = tempBillId;
		this.item = item;
	}

	public Long getTempBillId() {
		return tempBillId;
	}

	public void setTempBillId(Long tempBillId) {
		this.tempBillId = tempBillId;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public Integer getPatientGenderId() {
		return patientGenderId;
	}

	public void setPatientGenderId(Integer patientGenderId) {
		this.patientGenderId = patientGenderId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorAddress() {
		return doctorAddress;
	}

	public void setDoctorAddress(String doctorAddress) {
		this.doctorAddress = doctorAddress;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}


	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public ItemRequest getItem() {
		return item;
	}

	public void setItem(ItemRequest item) {
		this.item = item;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}


	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}


	public Boolean ValidateSave() {
		Boolean result = true;
		this.validationMessage = "Please fill below required fields. | ";
		if (getNetAmount() == null || getNetAmount() < 0) {
			result = false;
			this.validationMessage += "netAmount |";
		}
		if (getTaxAmount() == null || getTaxAmount() < 0) {
			result = false;
			this.validationMessage += "taxAmount |";
		}
		if (getTotalAmount()== null || getTotalAmount() < 0) {
			result = false;
			this.validationMessage += "totalAmount |";
		}
		if ((getPatientId() == null || getPatientId() < 1 && ((getAdmissionId() == null && getAdmissionId() < 1)
				|| (getEncounterId() == null && getEncounterId() < 1)))) {
			if (this.patientName == null || this.patientName.isEmpty()) {
				result = false;
				this.validationMessage += "patientName |";
			}
			/*if (this.patientAddress == null || this.patientAddress.isEmpty()) {
				result = false;
				this.validationMessage += "patientAddress |";
			}
			if (this.patientAge == null || this.patientAge < 1) {
				result = false;
				this.validationMessage += "patientAge |";
			}
			if (this.patientGenderId == null || this.patientGenderId < 1) {
				result = false;
				this.validationMessage += "patientGenderId |";
			}*/
			/*
			 * if (this.doctorName == null || this.doctorName.isEmpty()) { result = false;
			 * this.validationMessage += "doctorName |"; }
			 */
		}
		if (this.items == null || this.items.isEmpty()) {
			result = false;
			this.validationMessage += "items |";
		} else if (this.items != null && !this.items.isEmpty()
				&& (this.items.stream().filter(i -> (Objects.equals(i.ValidateSave(), false)))
						.map(ItemRequest::getItemId).collect(Collectors.toList())).size() > 0) {
			result = false;

			this.validationMessage += this.items.stream().filter(i -> (Objects.equals(i.ValidateSave(), false)))
					.map(ItemRequest::getValidationMessage).collect(Collectors.toList());

		}
		return result;
	}

	public Integer getTempPatientId() {
		return tempPatientId;
	}

	public void setTempPatientId(Integer tempPatientId) {
		this.tempPatientId = tempPatientId;
	}


	public Character getIsContractChanged() {
		return isContractChanged;
	}

	public void setIsContractChanged(Character isContractChanged) {
		this.isContractChanged = isContractChanged;
	}

	public Character getIsPayeeUpdated() {
		return isPayeeUpdated;
	}

	public void setIsPayeeUpdated(Character isPayeeUpdated) {
		this.isPayeeUpdated = isPayeeUpdated;
	}

	public Character getIsSmartApplicable() {
		return isSmartApplicable;
	}

	public void setIsSmartApplicable(Character isSmartApplicable) {
		this.isSmartApplicable = isSmartApplicable;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getIsFromOt() {
		return isFromOt;
	}

	public void setIsFromOt(Boolean isFromOt) {
		this.isFromOt = isFromOt;
	}

	public Boolean getSearchPatient() {
		return searchPatient;
	}

	public void setSearchPatient(Boolean searchPatient) {
		this.searchPatient = searchPatient;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}

	public boolean getAtCopToSave() {
		return atCopToSave;
	}

	public void setAtCopToSave(boolean atCopToSave) {
		this.atCopToSave = atCopToSave;
	}

	public Boolean getIsVirtualStore() {
		return isVirtualStore;
	}

	public void setIsVirtualStore(Boolean isVirtualStore) {
		this.isVirtualStore = isVirtualStore;
	}

	public boolean getIsConsolidatedFlag() {
		return isConsolidatedFlag;
	}

	public void setIsConsolidatedFlag(boolean isConsolidatedFlag) {
		this.isConsolidatedFlag = isConsolidatedFlag;
	}


}
