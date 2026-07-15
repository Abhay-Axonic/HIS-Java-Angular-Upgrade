package com.param.procurement.consignment.dto.patientConsumption;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientConsumtionIssueListDetails {
	
	private Integer id;
	
	private String toStore;
	
	private Integer storeId;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String uhid;
	
	private String remark;
	
	private List<PatientConsumtionDetailtem> items;
	
	//private List<PatientConsumtionIssueDetailListItem> itemss;
	

	public PatientConsumtionIssueListDetails()
	{
		super();
	}
	
	/*public 	PatientConsumtionIssueListDetails(Integer id, String toStore, Integer storeId, String visitType,
			String patientName, String uhid, String visitNo, String indentNo, Integer carrierId, String carrierName) {
		super();
		this.id = id;
		this.toStore = toStore;
		this.storeId = storeId;
		this.visitType = visitType;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitNo = visitNo;
		this.indentNo = indentNo;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
	}*/
	public PatientConsumtionIssueListDetails(Integer id,String issueNo, Date issueDate, String toStore, Integer storeId,String uhid) 
	{

		super();
		this.id = id;
		this.issueNo=issueNo;
		this.issueDate=issueDate;
		this.toStore = toStore;
		this.storeId = storeId;
		this.uhid=uhid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PatientConsumtionDetailtem> getItems() {
		return items;
	}

	public void setItems(List<PatientConsumtionDetailtem> items) {
		this.items = items;
	}
	
	public void addItems(PatientConsumtionDetailtem items) {
		if (this.items == null)
			this.items = new ArrayList<PatientConsumtionDetailtem>();

		this.items.add(items);
	}


}
