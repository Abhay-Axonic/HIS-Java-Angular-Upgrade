package com.param.pharmacy.master.unit.dto.storeItemMapper;

import java.util.List;

import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;

public class StoreItemMapperListResponse extends StoreItemMapperResponse{

	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private List<MultipleStoreRequest> multipleStoreRequest;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public List<MultipleStoreRequest> getMultipleStoreRequest() {
		return multipleStoreRequest;
	}

	public void setMultipleStoreRequest(List<MultipleStoreRequest> multipleStoreRequest) {
		this.multipleStoreRequest = multipleStoreRequest;
	}
}

