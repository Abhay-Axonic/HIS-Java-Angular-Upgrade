package com.param.CSSD.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.CSSDMaster.model.KitMaster;
import com.param.adt.master.global.model.ReasonMaster;
import com.param.entity.model.master.RejectReason;

@NamedQueries({

	@NamedQuery(name = "GET_KIT_LIST_BY_INDENT_NO", query = "SELECT mapper.id as id, mapper.kitId as kitId,kitMaster.code as code, "
			+ "kitMaster.name as name,mapper.quantity as quantity,mapper.status as status,mapper.rejectReasonId as rejectReasonId, "
			+ "mapper.rejectReasonNote as rejectReasonNote "
			+ "FROM KitIndentKitMapper mapper "
			+ "INNER JOIN mapper.kitMaster as kitMaster "
			+ "INNER JOIN mapper.kitIndent as kitIndent "
			+ " WHERE mapper.indentNo=:indentNo "
			+ "AND kitIndent.organizationId=:organizationId "
			+ "AND kitIndent.unitId=:unitId"),
	@NamedQuery(name = "GET_KIT_LIST_BY_INDENT_NO_FOR_KIT_ISSUE", query = "SELECT mapper.id as id, mapper.kitId as kitId,kitMaster.code as code, "
			+ "kitMaster.name as name,mapper.quantity as quantity,mapper.status as status,mapper.rejectReasonId as rejectReasonId, "
			+ "mapper.rejectReasonNote as rejectReasonNote "
			+ "FROM KitIndentKitMapper mapper "
			+ "INNER JOIN mapper.kitMaster as kitMaster "
			+ "INNER JOIN mapper.kitIndent as kitIndent "
			+ " WHERE mapper.indentNo=:indentNo AND mapper.status='Accepted' "
			+ "AND kitIndent.organizationId=:organizationId "
			+ "AND kitIndent.unitId=:unitId")
	})




@Entity
@Table(name = "t_kit_indent_details", schema = "cssd")
@SequenceGenerator(name = "t_kit_indent_details_id_seq", sequenceName = "cssd.t_kit_indent_details_id_seq", allocationSize = 1)
public class KitIndentKitMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_indent_details_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "indent_no")
	private Integer indentNo;
	
	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "issued_quantity")
	private Integer issuedQuantity;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "issue_status")
	private String issueStatus;

	@Column(name = "reject_reason_note")
	private String rejectReasonNote;
	
	@Column(name = "reject_reason_id")
	private Integer rejectReasonId;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_no", insertable = false, updatable = false)
	private KitIndent  kitIndent;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id", insertable = false, updatable = false)
	private RejectReason  rejectReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster  kitMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public KitIndent getKitIndent() {
		return kitIndent;
	}

	public void setKitIndent(KitIndent kitIndent) {
		this.kitIndent = kitIndent;
	}

	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectReasonNote() {
		return rejectReasonNote;
	}

	public void setRejectReasonNote(String rejectReasonNote) {
		this.rejectReasonNote = rejectReasonNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(Integer issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public String getIssueStatus() {
		return issueStatus;
	}

	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	
	
}
