package com.param.global.service.items;

import java.util.List;

public class CustomException extends Exception {
	
	public CustomException(String s) {
		super(s);
	}
	
	Integer currentStock;
	String batchNo;
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Integer getCurrentStock() {
		return currentStock;
	}
	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}
	public CustomException(String s, Integer currentStock, String batchNo) {
		super(s);
		this.currentStock=currentStock;
		this.batchNo=batchNo;
	}
	List<CustomException> errors;

	public List<CustomException> getErrors() {
		return errors;
	}

	public void setErrors(List<CustomException> errors) {
		this.errors = errors;
	}

	public CustomException(String s,List<CustomException> errors) {
		super(s);
		this.errors = errors;
	}
}
