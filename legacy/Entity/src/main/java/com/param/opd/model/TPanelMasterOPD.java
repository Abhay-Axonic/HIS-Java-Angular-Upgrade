package com.param.opd.model;

import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;


@Entity
@Table(name = "t_panel_master", schema = "lab")
@SequenceGenerator(name = "t_seq_panel_master", sequenceName = "lab.t_seq_panel_master", allocationSize = 1)
public class TPanelMasterOPD {
	@Id
	@Column(name = "panel_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_panel_master")
	private int panelId;

	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "org_id")
	private Integer orgId;
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "panel_alies")
	private String panelAlies;

	@Column(name = "panel_code")
	private String panelCode;

	@Column(name = "status")
	private char status;

	@Column(name = "is_deleted")
	private char isDeleted;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Long createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Long updatedDate;
	
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tPanelMaster")
	private List<TPanelDetailsMasterOPD> listTPanelDetailsMaster;

	public String getPanelAlies() {
		return panelAlies;
	}

	public void setPanelAlies(String panelAlies) {
		this.panelAlies = panelAlies;
	}

	public String getPanelCode() {
		return panelCode;
	}

	public void setPanelCode(String panelCode) {
		this.panelCode = panelCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;

	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getPanelId() {
		return panelId;
	}

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public List<TPanelDetailsMasterOPD> getListTPanelDetailsMaster() {
		return listTPanelDetailsMaster;
	}

	public void setListTPanelDetailsMaster(List<TPanelDetailsMasterOPD> listTPanelDetailsMaster) {
		this.listTPanelDetailsMaster = listTPanelDetailsMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	
}