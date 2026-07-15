package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;


@Entity
@Table(name="t_panel_details", schema="lab")
@SequenceGenerator(name="t_seq_panel_details", sequenceName="lab.t_seq_panel_details", allocationSize=1)
public class TPanelDetailsMasterOPD {
  
  @Id
  @Column(name = "panel_details_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_panel_details")
  private int panelDetailsId;

  @Column(name = "service_id")
  private Integer serviceId;
  
  @Column(name = "panel_id")
  private Integer panelId;

  @Column(name = "test_id")
  private Integer testId;

  @Column(name = "header_id")
  private Integer headerId;

  @Column(name = "org_id")
  private Integer orgId;

  @Column(name = "org_unit_id")
  private Integer orgUnitId;
  
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
  

  @Column(name = "print_order")
  private Integer printOrder;


  @Column(name = "updated_date")
  private Long updatedDate;
 
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "test_id", insertable = false, nullable = false, updatable = false)
  private TestMasterOPD testMaster;
  
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "panel_id", insertable = false, nullable = false, updatable = false)
  private TPanelMasterOPD tPanelMaster;
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "header_id", insertable = false, nullable = false, updatable = false)
	private HeaderMasterOPD headerMasterer;
 

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public Integer getTestId() {
    return testId;
  }

  public void setTestId(Integer testId) {
    this.testId = testId;
  }

  public Integer getHeaderId() {
    return headerId;
  }

  public void setHeaderId(Integer headerId) {
    this.headerId = headerId;
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

  public char getStatus() {
    return status;
  }

  public void setStatus(char status) {
    this.status = (status == '\u0000') ? 'I' : status;
  }

  public char getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(char isDeleted) {
    this.isDeleted =  (isDeleted == '\u0000') ? 'N' : isDeleted;
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

  

  public TestMasterOPD getTestMaster() {
    return testMaster;
  }

  public void setTestMaster(TestMasterOPD testMaster) {
    this.testMaster = testMaster;
  }

public int getPanelDetailsId() {
	return panelDetailsId;
}

public void setPanelDetailsId(int panelDetailsId) {
	this.panelDetailsId = panelDetailsId;
}

public TPanelMasterOPD gettPanelMaster() {
	return tPanelMaster;
}

public void settPanelMaster(TPanelMasterOPD tPanelMaster) {
	this.tPanelMaster = tPanelMaster;
}

public Integer getPanelId() {
	return panelId;
}

public void setPanelId(Integer panelId) {
	this.panelId = panelId;
}

public Integer getPrintOrder() {
	return printOrder;
}

public void setPrintOrder(Integer printOrder) {
	this.printOrder = printOrder;
}


  

}
