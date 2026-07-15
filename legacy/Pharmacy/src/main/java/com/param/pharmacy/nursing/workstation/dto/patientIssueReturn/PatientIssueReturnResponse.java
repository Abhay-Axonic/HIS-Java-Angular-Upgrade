package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class PatientIssueReturnResponse
{
	private Integer id;
	
	private String returnNo;
	
	private Date returnDate;
	
	private String uhid;
	
	private String patientDetail;
	
	private String visitType;
	
	private String visitNo;
	
	private String store;
	
	private String wardName;
	
	private String bedNo;
	
	private String status;

	private String indentNo;
	
    private boolean isImplant;
    
    private boolean isConsignment;

	private Boolean fromMedicineIssues;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String string) {
		this.indentNo = string;
	}
	
    public boolean isImplant() {
        return isImplant;
    }
    public void setImplant(boolean isImplant) {
        this.isImplant = isImplant;
    }
    public boolean isConsignment() {
        return isConsignment;
    }
    public void setConsignment(boolean isConsignment) {
        this.isConsignment = isConsignment;
    }

	public Boolean getFromMedicineIssues() {
		return fromMedicineIssues;
	}

	public void setFromMedicineIssues(Boolean fromMedicineIssues) {
		this.fromMedicineIssues = fromMedicineIssues;
	}

	public PatientIssueReturnResponse() {
		super();
	}
	
	public PatientIssueReturnResponse(Integer id, String returnNo, Date returnDate, String uhid, String patientDetail,
			String visitType, String visitNo, String store, String status, Boolean fromMedicineIssues) {
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.store = store;
		this.status = status;
		this.fromMedicineIssues = fromMedicineIssues;
	}

}