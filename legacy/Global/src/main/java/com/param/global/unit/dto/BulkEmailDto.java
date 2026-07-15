package com.param.global.unit.dto;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class BulkEmailDto {
	private String patientDetails;
	private String uhid;
	private List<Integer> labSampleDtlsList;
	private Integer patientId;
	private ByteArrayOutputStream byteArrayOutputStream;
	private String attachmentFileName;
	private String testDesc;
	private Long orderDate;
	private Integer statusCode; 
	private String reportString;
	private String uniqueId;
	private Integer bulkMessageQueueId;
	private Integer retryAttemps; 
	public String getPatientDetails() {
		return patientDetails;
	}
	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}
	public String getUhid() {
		return uhid;
	}
	public void setUhid(String uhid) {
		this.uhid = uhid;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}
	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}
	public String getAttachmentFileName() {
		return attachmentFileName;
	}
	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}
	
	public String getTestDesc() {
		return testDesc;
	}
	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
		public List<Integer> getLabSampleDtlsList() {
		return labSampleDtlsList;
	}
	public void setLabSampleDtlsList(List<Integer> labSampleDtlsList) {
		this.labSampleDtlsList = labSampleDtlsList;
	}
	public Long getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}
	public String getReportString() {
		return reportString;
	}
	public void setReportString(String reportString) {
		this.reportString = reportString;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Integer getBulkMessageQueueId() {
		return bulkMessageQueueId;
	}
	public void setBulkMessageQueueId(Integer bulkMessageQueueId) {
		this.bulkMessageQueueId = bulkMessageQueueId;
	}
	public Integer getRetryAttemps() {
		return retryAttemps;
	}
	public void setRetryAttemps(Integer retryAttemps) {
		this.retryAttemps = retryAttemps;
	}
	
}
