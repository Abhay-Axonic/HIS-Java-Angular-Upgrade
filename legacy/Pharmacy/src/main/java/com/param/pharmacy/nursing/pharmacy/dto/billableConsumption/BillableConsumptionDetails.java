package com.param.pharmacy.nursing.pharmacy.dto.billableConsumption;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BillableConsumptionDetails
{
	private Integer id;
	
	private String consumptionNo;
	
	private Date consumptionDate;
	
	private String returnNo;
	
	private Date returnDate;
	
	private String store;
	
	private String visitType;
	
	private String uhid;
	
	private String patientDetail;
	
	private String visitNo;
	
	private String wardName;
	
	private String bedNo;
	
	private Date visitDate;
	
	private List<BillableConsumptionDetailItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConsumptionNo() {
		return consumptionNo;
	}

	public void setConsumptionNo(String consumptionNo) {
		this.consumptionNo = consumptionNo;
	}

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public List<BillableConsumptionDetailItem> getItems() {
		return items;
	}

	public void setItems(List<BillableConsumptionDetailItem> items) {
		this.items = items;
	}

	public BillableConsumptionDetails() {
		super();
	}

	public BillableConsumptionDetails(Integer id, String consumptionNo, Date consumptionDate, String store,
			String visitType, String uhid, String patientDetail, String visitNo) {
		super();
		this.id = id;
		this.consumptionNo = consumptionNo;
		this.consumptionDate = consumptionDate;
		this.store = store;
		this.visitType = visitType;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.visitNo = visitNo;
	}

	public BillableConsumptionDetails(Integer id, String store, String visitType,
			String uhid, String patientDetail, String visitNo) {
		super();
		this.id = id;
		this.store = store;
		this.visitType = visitType;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.visitNo = visitNo;
	}

	public BillableConsumptionDetails(Integer id, String consumptionNo, Date consumptionDate) {
		super();
		this.id = id;
		this.consumptionNo = consumptionNo;
		this.consumptionDate = consumptionDate;
	}
	
}