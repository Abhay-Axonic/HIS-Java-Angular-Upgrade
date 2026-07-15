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

@NamedQueries({

	@NamedQuery(name = "GET_KIT_LIST_BY_ISSUE_NO", query = "SELECT mapper.kitId as kitId,kitMaster.code as kitCode, "
			+ "kitMaster.name as kitName,mapper.quantity as quantity FROM KitIssueKitMapper mapper "
			+ "INNER JOIN mapper.kitMaster as kitMaster WHERE mapper.issueNo=:issueNo")
	})


@Entity
@Table(name = "t_kit_issue_kit_mapper", schema = "cssd")
@SequenceGenerator(name = "t_kit_issue_kit_mapper_id_seq", sequenceName = "cssd.t_kit_issue_kit_mapper_id_seq", allocationSize = 1)
public class KitIssueKitMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_issue_kit_mapper_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "issue_no")
	private Integer issueNo;
	
	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_no", insertable = false, updatable = false)
	private KitIssue  kitIssue;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster  kitMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
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

	public KitIssue getKitIssue() {
		return kitIssue;
	}

	public void setKitIssue(KitIssue kitIssue) {
		this.kitIssue = kitIssue;
	}

	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}
	
	
		
}
