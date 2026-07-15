package com.param.entity.lis.unit;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
				@NamedQuery(name = "GET_MICROBIOLOGY_TEST_BY_ID",
						query = " SELECT "
								+ " microbiologyTest.microTestId AS id,"
								+ " microbiologyTest.microTestCode AS code,"
								+ " microbiologyTest.microTestDesc AS desc,"
								+ " microbiologyTest.orgId AS orgId,"
								+ " microbiologyTest.orgUnitId AS orgUnitId,"
								+ " microbiologyTest.status AS status "
								+ " FROM MicrobiologyTestMaster microbiologyTest"
								+ " WHERE microbiologyTest.microTestId= :id "
								+ " AND microbiologyTest.orgId= :orgId"
								//+ " AND microbiologyTest.orgUnitId= :orgUnitId" 
							
						
						),
				
				@NamedQuery(name = "GET_MICROBIOLOGY_TEST_LIST",
						query = " SELECT "
								+ " microbiologyTest.microTestId AS id,"
								+ " microbiologyTest.orgId AS orgId,"
								+ " microbiologyTest.microTestDesc AS desc,"
								+ " microbiologyTest.microTestCode AS code,"
								+ " microbiologyTest.orgUnitId AS orgUnitId,"
								+ " microbiologyTest.status AS status "
								+ " FROM MicrobiologyTestMaster microbiologyTest"
								//+ " WHERE microbiologyTest.orgUnitId= :orgUnitId" //dilip
								+ " WHERE microbiologyTest.orgId= :orgId"
								+ " order by microbiologyTest.microTestId asc"
						
						),
				
				@NamedQuery(name = "GET_ACTIVE_MICROBIOLOGY_TEST_LIST",
						query = " SELECT "
								+ " microbiologyTest.microTestId AS id, "
								+ " microbiologyTest.microTestDesc AS desc,"
								+ " microbiologyTest.microTestCode AS code,"
								+ " microbiologyTest.orgId AS orgId, "
								+ " microbiologyTest.orgUnitId AS orgUnitId,"
								+ " microbiologyTest.status AS status "
								+ " FROM MicrobiologyTestMaster microbiologyTest "
								+ " WHERE microbiologyTest.status= 'A'"
								//+ " AND microbiologyTest.orgUnitId= :orgUnitId" //dilip
								+ " AND microbiologyTest.orgId= :orgId"
								+ " order by microbiologyTest.microTestId asc"
						
						),
				
				@NamedQuery(name = "GET_MICROBIOLOGY_TEST_BY_NAME",
						query = " SELECT "
								+ " microbiologyTest.microTestId AS id,"
								+ " microbiologyTest.microTestDesc AS desc"
								+ " FROM MicrobiologyTestMaster microbiologyTest"
								+ " WHERE LOWER(microbiologyTest.microTestDesc)= :desc"
								+ " AND microbiologyTest.orgId= :orgId"
						
						),
				
				@NamedQuery(name = "GET_MICROBIOLOGY_TEST_BY_CODE",
				query = " SELECT "
						+ " microbiologyTest.microTestId AS id,"
						+ " microbiologyTest.microTestCode AS code"
						+ " FROM MicrobiologyTestMaster microbiologyTest"
						+ " WHERE LOWER(microbiologyTest.microTestCode)= :code"
						+ " AND microbiologyTest.orgId= :orgId"
				
				),
				
				@NamedQuery(name = "GET_MICROBIOLOGY_TEST_BY_NAME_NOT_ID",
						query = " SELECT "
								+ " microbiologyTest.microTestId AS id,"
								+ " microbiologyTest.microTestDesc AS desc"
								+ " FROM MicrobiologyTestMaster microbiologyTest"
								+ " WHERE microbiologyTest.microTestId != :id"
								+ " AND LOWER(microbiologyTest.microTestDesc)= :desc"
								
						),
				
				@NamedQuery(name = "UPDATE_CHECK_UNIQUE_MICROBIOLOGY_TEST_CODE",
				query = " SELECT "
						+ " microbiologyTest.microTestId AS id,"
						+ " microbiologyTest.microTestCode AS code"
						+ " FROM MicrobiologyTestMaster microbiologyTest"
						+ " WHERE microbiologyTest.microTestId != :id"
						+ " AND LOWER(microbiologyTest.microTestCode)= :code"
						
				)
	
	})

@Entity
@Table(name = "m_microbiology_test_master", schema = "lab")
@SequenceGenerator(name = "m_seq_microbiology_test_master", sequenceName = "lab.m_seq_microbiology_test_master",
    allocationSize = 1)
public class MicrobiologyTestMaster {
  
  @Id
  @Column(name = "micro_test_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_microbiology_test_master")
  private int microTestId;

  @Column(name = "micro_test_code")
  private String microTestCode;
  
  @Column(name = "micro_test_desc")
  private String microTestDesc;
  
  @Column(name = "org_id")
  private Integer orgId;

  @Column(name = "org_unit_id")
  private Integer orgUnitId;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_date")
  private Date createdDate;

  @Column(name = "updated_by")
  private Integer updatedBy;

  @Column(name = "updated_date")
  private Date updatedDate;

  @Column(name = "status")
  private Character status;

  public int getMicroTestId() {
    return microTestId;
  }

  public void setMicroTestId(int microTestId) {
    this.microTestId = microTestId;
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

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }


  public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

 
  public Character getStatus() {
    return status;
  }

  public void setStatus(Character status) {
    this.status = status;
  }

  public String getMicroTestCode() {
    return microTestCode;
  }

  public Date getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}

public Date getUpdatedDate() {
	return updatedDate;
}

public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
}

public void setMicroTestCode(String microTestCode) {
    this.microTestCode = microTestCode;
  }

  public String getMicroTestDesc() {
    return microTestDesc;
  }

  public void setMicroTestDesc(String microTestDesc) {
    this.microTestDesc = microTestDesc;
  }
  
  

}
