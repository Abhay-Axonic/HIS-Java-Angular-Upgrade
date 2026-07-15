package com.param.global.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_All_TICKET_FILES_BY_TICKET_ID", 
			 query = "SELECT ticket_master_id as \"ticketMasterId\","
			 		 + "file_url as \"fileUrl\","
			 		 + "file_name as \"fileName\","
			 		 + "file_id as \"fileId\","
			 		 + "file_status as \"fileStatus\","
					 + "user_id \"userId\",unit_id as \"unitId\",organization_id as \"organizationId\" "
					 + "FROM t_ticket_system_master_uploaded_files " 
					 + "WHERE ticket_master_id =:ticketMasterId "
					 + "and file_status='A' "
					 + "and organization_id =:orgId " 
					 + "and unit_id =:unitId"),
	
	@NamedNativeQuery(name = "UPDATE_TICKET_FILES_STATUS_BY_FILE_ID",
	 query = "UPDATE "
			 + " t_ticket_system_master_uploaded_files"
			 + " SET file_status ='I',"
			 + " file_updated_date =:fileUpdatedDate"
			 + " WHERE file_id =:fileId"
			 + " and organization_id =:orgId"
			 + " and unit_id =:unitId"),

	@NamedNativeQuery(name = "UPDATE_TICKET_STATUS_BY_TICKET_ID",
	 query = "UPDATE "
			 + " t_ticket_system_master_uploaded_files"
			 + " SET file_status ='I',"
			 + " file_updated_date =:fileUpdatedDate"
			 + " WHERE ticket_master_id =:ticketMasterId"
			 + " and organization_id =:orgId"
			 + " and unit_id =:unitId"),
	
	
})
@Entity
@Table(name = "t_ticket_system_master_uploaded_files", schema = "public")
@SequenceGenerator(name = "ticket_system_master_uploaded_files_seq", sequenceName = "public.ticket_system_master_uploaded_files_seq", allocationSize = 1)
public class TicketSystemMasterFiles {
	
	@Id 
	@GeneratedValue (strategy = GenerationType.SEQUENCE , generator = "ticket_system_master_uploaded_files_seq")
	@Column (name = "file_id")
	private Integer  fileId;
	
	@Column (name = "file_url")
	private String fileUrl;
	
	@Column (name = "file_status")
    private Character fileStatus;
	
	@Column (name = "file_name")
	private String fileName;
	
	@Column(name = "ticket_master_id")
	private Integer ticketMasterId;
    
	@Column (name = "unit_id")
	private Integer unitId;
	
	@Column (name = "organization_id")
	private Integer organizationId;
	
	@Column (name = "user_id")
	private Integer userId;
	
	@Column (name = "file_created_date" )
	private Date fileCreatedDate;
	
	@Column (name = "file_updated_date")
	private Date fileUpdatedDate; 
	
	@Column (name = "file_created_by" )
	private Integer fileCreatedBy;
	
	@Column (name = "file_updated_by")
	private Integer fileUpdatedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_master_id", insertable = false, nullable = false, updatable = false)
	private TicketSystemMaster ticketSystemMaster;

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

	public Integer getTicketMasterId() {
		return ticketMasterId;
	}

	public void setTicketMasterId(Integer ticketMasterId) {
		this.ticketMasterId = ticketMasterId;
	}

	public TicketSystemMaster getTicketSystemMaster() {
		return ticketSystemMaster;
	}

	public void setTicketSystemMaster(TicketSystemMaster ticketSystemMaster) {
		this.ticketSystemMaster = ticketSystemMaster;
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

	public Date getFileCreatedDate() {
		return fileCreatedDate;
	}

	public void setFileCreatedDate(Date fileCreatedDate) {
		this.fileCreatedDate = fileCreatedDate;
	}

	public Date getFileUpdatedDate() {
		return fileUpdatedDate;
	}

	public void setFileUpdatedDate(Date fileUpdatedDate) {
		this.fileUpdatedDate = fileUpdatedDate;
	}

	public Integer getFileCreatedBy() {
		return fileCreatedBy;
	}

	public void setFileCreatedBy(Integer fileCreatedBy) {
		this.fileCreatedBy = fileCreatedBy;
	}

	public Integer getFileUpdatedBy() {
		return fileUpdatedBy;
	}

	public void setFileUpdatedBy(Integer fileUpdatedBy) {
		this.fileUpdatedBy = fileUpdatedBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
	

}
