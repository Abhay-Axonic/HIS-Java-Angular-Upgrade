package com.param.inventory.store.dto.materialReturn;

import java.util.Date;
import java.util.List;

import com.param.pharmacy.op.dto.salesReturn.ItemDetails;

public class AgainstIssueResponse {

	private Integer id;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String patientDetail;
	
	private String uhid;
	
	private Date visitDate;
	
	private String visitNo;
	
	private String indentNo;
	private String storeName;
	public List<ItemDetails> getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(List<ItemDetails> itemDetails) {
		this.itemDetails = itemDetails;
	}

	private Integer storeId;
	private List<ItemDetails> itemDetails;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	
	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public AgainstIssueResponse(Integer id, String issueNo, Date issueDate, String patientDetail, String uhid,
			Date visitDate, String visitNo) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.patientDetail = patientDetail;
		this.uhid = uhid;
		this.visitDate = visitDate;
		this.visitNo = visitNo;
	}


}
