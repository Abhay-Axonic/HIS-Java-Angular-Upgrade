package com.param.global.ticketofsystem.dto;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.model.TicketSystemMasterFiles;

@JsonInclude(Include.NON_NULL)
public class TicketSystemMasterDto {
	
	private Integer ticketMasterId;
	private	String ticketReportedBy;
	private String ticketContactNumber;
	private String ticketEmail;
	private String ticketNumber;
	private String ticketIssueDescription;
	private Character ticketStatus;
	private Integer unitId;
	private Integer organizationId;
	private Integer userId;
	private Date ticketCreatedDate;
	private Date ticketUpdatedDate;
	private Integer ticketCreatedBy;
	private Integer ticketUpdatedBy;
	private Integer fileId;
	private String fileUrl;
	private Character fileStatus;
	private String fileName;
	private String ticketSystemJson;
	private Integer offSet;
	private Integer noOfRecordsPerPage;
	private String ticketPriority;
	private String ticketIssueType;
	private String searchKeyWord;
	private String ticketReportedUnitName;
	private Integer [] attachmentId;
	
	private List<TicketSystemMasterFiles> ticketSystemMasterUploadedFiles;
	
	public Integer getTicketMasterId() {
		return ticketMasterId;
	}
	public void setTicketMasterId(Integer ticketMasterId) {
		this.ticketMasterId = ticketMasterId;
	}
	public String getTicketReportedBy() {
		return ticketReportedBy;
	}
	public void setTicketReportedBy(String ticketReportedBy) {
		this.ticketReportedBy = ticketReportedBy;
	}
	public String getTicketContactNumber() {
		return ticketContactNumber;
	}
	public void setTicketContactNumber(String ticketContactNumber) {
		this.ticketContactNumber = ticketContactNumber;
	}
	public String getTicketEmail() {
		return ticketEmail;
	}
	public void setTicketEmail(String ticketEmail) {
		this.ticketEmail = ticketEmail;
	}
	public String getTicketIssueDescription() {
		return ticketIssueDescription;
	}
	public void setTicketIssueDescription(String ticketIssueDescription) {
		this.ticketIssueDescription = ticketIssueDescription;
	}
	public Character getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(Character ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getTicketCreatedDate() {
		return ticketCreatedDate;
	}
	public void setTicketCreatedDate(Date ticketCreatedDate) {
		this.ticketCreatedDate = ticketCreatedDate;
	}
	public Date getTicketUpdatedDate() {
		return ticketUpdatedDate;
	}
	public void setTicketUpdatedDate(Date ticketUpdatedDate) {
		this.ticketUpdatedDate = ticketUpdatedDate;
	}
	public Integer getTicketCreatedBy() {
		return ticketCreatedBy;
	}
	public void setTicketCreatedBy(Integer ticketCreatedBy) {
		this.ticketCreatedBy = ticketCreatedBy;
	}
	public Integer getTicketUpdatedBy() {
		return ticketUpdatedBy;
	}
	public void setTicketUpdatedBy(Integer ticketUpdatedBy) {
		this.ticketUpdatedBy = ticketUpdatedBy;
	}
	public List<TicketSystemMasterFiles> getTicketSystemMasterUploadedFiles() {
		return ticketSystemMasterUploadedFiles;
	}
	public void setTicketSystemMasterUploadedFiles(List<TicketSystemMasterFiles> ticketSystemMasterUploadedFiles) {
		this.ticketSystemMasterUploadedFiles = ticketSystemMasterUploadedFiles;
	}
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Character getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Character fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTicketSystemJson() {
		return ticketSystemJson;
	}
	public void setTicketSystemJson(String ticketSystemJson) {
		this.ticketSystemJson = ticketSystemJson;
	}
	public Integer getOffSet() {
		return offSet;
	}
	public void setOffSet(Integer offSet) {
		this.offSet = offSet;
	}
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}
	public String getTicketPriority() {
		return ticketPriority;
	}
	public void setTicketPriority(String ticketPriority) {
		this.ticketPriority = ticketPriority;
	}
	public String getTicketIssueType() {
		return ticketIssueType;
	}
	public void setTicketIssueType(String ticketIssueType) {
		this.ticketIssueType = ticketIssueType;
	}
	public String getSearchKeyWord() {
		return searchKeyWord;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}
	public String getTicketReportedUnitName() {
		return ticketReportedUnitName;
	}
	public void setTicketReportedUnitName(String ticketReportedUnitName) {
		this.ticketReportedUnitName = ticketReportedUnitName;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public Integer[] getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Integer[] attachmentId) {
		this.attachmentId = attachmentId;
	}
	
	
}
