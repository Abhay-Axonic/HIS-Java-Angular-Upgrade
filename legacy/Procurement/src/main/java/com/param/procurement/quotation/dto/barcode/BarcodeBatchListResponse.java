package com.param.procurement.quotation.dto.barcode;

import java.util.Date;

import com.param.global.dto.items.BatchListResponse;

public class BarcodeBatchListResponse extends BatchListResponse {

	protected Integer barcodePrintCount;

	protected Double mrp;

	protected String hospitalName;
	protected Integer manufactureYear;
	protected String prePrintedBarcode;
    protected Integer itemId;
    protected Long batchId;
    protected boolean isManualBarcodeRequired;
    protected boolean isPRNRequired;
    protected String pharmacyPRNFormat;
    protected String priPrintedBarcodeNo;

	protected String grnNo;
	protected String grnDate;
	protected String vendorName;
	protected String batchNo;
	protected String lotNumber;
	protected Date expiry;
	protected Integer printedCount;

	public String getPriPrintedBarcodeNo() {
		return priPrintedBarcodeNo;
	}

	public void setPriPrintedBarcodeNo(String priPrintedBarcodeNo) {
		this.priPrintedBarcodeNo = priPrintedBarcodeNo;
	}

	public BarcodeBatchListResponse() {
		super();
	}

	public Integer getBarcodePrintCount() {
		return barcodePrintCount;
	}

	public void setBarcodePrintCount(Integer barcodePrintCount) {
		this.barcodePrintCount = barcodePrintCount;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public String getPrePrintedBarcode() {
		return prePrintedBarcode;
	}

	public void setPrePrintedBarcode(String prePrintedBarcode) {
		this.prePrintedBarcode = prePrintedBarcode;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public boolean getIsManualBarcodeRequired() {
		return isManualBarcodeRequired;
	}

	public void setIsManualBarcodeRequired(boolean isManualBarcodeRequired) {
		this.isManualBarcodeRequired = isManualBarcodeRequired;
	}

	public boolean getIsPRNRequired() {
		return isPRNRequired;
	}

	public void setIsPRNRequired(boolean isPRNRequired) {
		this.isPRNRequired = isPRNRequired;
	}

	public String getPharmacyPRNFormat() {
		return pharmacyPRNFormat;
	}

	public void setPharmacyPRNFormat(String pharmacyPRNFormat) {
		this.pharmacyPRNFormat = pharmacyPRNFormat;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public String getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(String grnDate) {
		this.grnDate = grnDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getPrintedCount() {
		return printedCount;
	}

	public void setPrintedCount(Integer printedCount) {
		this.printedCount = printedCount;
	}


	public String getBatchNo() {
		return batchNo;
	}


	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

}
