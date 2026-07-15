package com.param.global.model;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_LETEST_10_TICKET_MASTER", 
				         query = "SELECT ticket_master_id as \"ticketMasterId\","
				         		 + "ticket_reported_by as \"ticketReportedBy\","
				        		 + "ticket_issue_description as \"ticketIssueDescription\","
				        		 + "ticket_contact_number as \"ticketContactNumber\","
				        		 + "ticket_email as \"ticketEmail\","
				        		 + "ticket_priority as\"ticketPriority\","
				        		 + "ticket_issue_type as\"ticketIssueType\","
				        		 + "ticket_status \"ticketStatus\","
				        		 + "ticket_number \"ticketNumber\","
				        		 + "unit_id as \"unitId\",organization_id as \"organizationId\" "
				        		 + "FROM m_ticket_system_master " 
				        		 + "WHERE organization_id =:orgId " 
				        		 + "and unit_id =:unitId "
				        		 + "ORDER BY ticket_master_id DESC "
				        		 + "limit 10" ),
		
		@NamedNativeQuery(name = "GET_TICKET_MASTER_LIST_COUNT",
						 query = "SELECT count(*) " 
								 + "FROM m_ticket_system_master "
								 + "WHERE organization_id =:orgId " 
								 + "and unit_id =:unitId"),
		
		@NamedNativeQuery(name = "GET_TICKET_MASTER_BY_ID", 
						 query = "SELECT ticket_master_id as \"ticketMasterId\","
						 		 + "ticket_reported_by as \"ticket_reported_by\","
						 		 + "ticket_issue_description as \"ticketIssueDescription\","
						 		 + "ticket_contact_number as \"ticketContactNumber\","
								 + "ticket_status \"ticketStatus\","
								 + "ticket_number \"ticketNumber\","
								 + "unit_id as \"unitId\",organization_id as \"organizationId\" "
								 + "FROM m_ticket_system_master " 
								 + "WHERE ticket_master_id =:ticketMasterId "
								 + "and organization_id =:orgId " 
								 + "and unit_id =:unitId"),

		@NamedNativeQuery(name = "UPDATE_TICKET_SYSTEM_MASTER_STATUS",
						 query = "UPDATE "
								 + " m_ticket_system_master"
								 + " SET ticket_status =:ticketStatus"
								 + " WHERE ticket_master_id =:ticketMasterId"
								 + " and organization_id =:orgId"
								 + " and unit_id =:unitId"),
		
		@NamedNativeQuery(name = "UPDATE_TICKET_SYSTEM_MASTER",
						 query = "UPDATE "
								 + " m_ticket_system_master"
								 + " SET ticket_reported_by = :ticketReportedBy,"
								 + " ticket_priority = :ticketPriority,"
								 + " ticket_issue_type = :ticketIssueType,"
								 + " ticket_issue_description = :ticketIssueDescription,"
								 + " ticket_contact_number = :ticketContactNumber,"
								 + " ticket_updated_date = :ticketUpdatedDate,"
								 + " ticket_status = :ticketStatus"
								 + " WHERE ticket_master_id =:ticketMasterId"
								 + " and organization_id =:orgId"
								 + " and unit_id =:unitId"),
		
		@NamedNativeQuery(name = "GET_TICKET_DATA_FOR_PDS_SAVE",
		 				query = "SELECT "
		 						 + " ticket_system_json as \"ticketSystemJson\" "
		 						 + " FROM public.t_unit_application_billing_configuration"
		 						 + " WHERE unit_id =:unitId"),
		
		@NamedNativeQuery(name = "SEARCH_TICKET_SYSTEM_MASTER", 
        				query = "SELECT ticket_mst.ticket_master_id as \"ticketMasterId\","
        						 + "ticket_mst.ticket_reported_by as \"ticketReportedBy\","
        						 + "ticket_mst.ticket_issue_description as \"ticketIssueDescription\","
        						 + "ticket_mst.ticket_contact_number as \"ticketContactNumber\","
        						 + "ticket_mst.ticket_email as \"ticketEmail\", "
        						 + "ticket_mst.ticket_priority as\"ticketPriority\","
				        		 + "ticket_mst.ticket_issue_type as\"ticketIssueType\","
        						 + "ticket_mst.ticket_status \"ticketStatus\","
        						 + "ticket_number \"ticketNumber\","
        						 + "ticket_mst.unit_id as \"unitId\",ticket_mst.organization_id as \"organizationId\" "
        						 + "FROM m_ticket_system_master ticket_mst " 
        						 + "WHERE ticket_mst.organization_id =:orgId " 
        						 + "and ticket_mst.unit_id =:unitId " ),
		
		@NamedNativeQuery(name = "UPDATE_TICKET_NUMBER_MASTER",
		 query = "UPDATE "
				 + " m_ticket_system_master"
				 + " SET ticket_number = :ticketNumber"
				 + " WHERE ticket_master_id =:ticketMasterId"
				 + " and organization_id =:orgId"
				 + " and unit_id =:unitId")
	
})

@Entity
@Table(name = "m_ticket_system_master", schema = "public")
@SequenceGenerator(name = "ticket_system_master_seq", sequenceName = "public.ticket_system_master_seq", allocationSize = 1)
public class TicketSystemMaster {
	
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE ,generator = "ticket_system_master_seq")
	@Column (name = "ticket_master_id")
	private Integer ticketMasterId;
	
	@Column (name = "ticket_reported_by")
    private	String ticketReportedBy;
	
	@Column (name = "ticket_contact_number")
	private String ticketContactNumber;
	
	@Column (name ="ticket_email")
	private String ticketEmail;
	
	@Column (name ="ticket_issue_description")
	private String ticketIssueDescription;
	
	@Column (name ="ticket_status")
	private Character ticketStatus;
	
	@Column (name ="ticket_number")
	private String ticketNumber;
	
	@Column (name ="unit_id")
	private Integer unitId;
	
	@Column (name ="organization_id")
	private Integer organizationId;
	
	@Column (name ="user_id")
	private Integer userId;
	
	@Column (name = "ticket_created_date" )
	private Date ticketCreatedDate;
	
	@Column (name = "ticket_updated_date")
	private Date ticketUpdatedDate; 
	
	@Column (name = "ticket_created_by" )
	private Integer ticketCreatedBy;
	
	@Column (name = "ticket_updated_by")
	private Integer ticketUpdatedBy; 
	
	@Column (name ="ticket_priority")
	private String ticketPriority;
	
	@Column (name ="ticket_issue_type")
	private String ticketIssueType;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ticketSystemMaster")
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
	

}
