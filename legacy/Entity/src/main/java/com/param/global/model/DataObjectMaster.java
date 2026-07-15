package com.param.global.model;

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
	@NamedQuery(name = "CHECK_DATA_OBJECT_ALREADY_EXISTS",
				query= "SELECT daob.dataObjectId as dataObjectId, daob.title as title, daob.dataObject as dataObject, daob.status as status, daob.orgId as orgId, "
					 + "		daob.unitId as unitId "
					 + "FROM DataObjectMaster daob "
					 + "WHERE daob.orgId =:orgId and daob.unitId =:unitId and lower(trim(daob.title)) = :title"),
	
	@NamedQuery(name = "GET_DATA_OBJECT_LIST",
				query= "SELECT		daob.dataObjectId as dataObjectId, daob.title as title, daob.dataObject as dataObject, daob.status as status, daob.orgId as orgId, "
					 + "			daob.unitId as unitId "
					 + "FROM DataObjectMaster daob "
					 + "WHERE daob.orgId =:orgId and daob.unitId =:unitId and daob.status ='A' "
					 + " order by daob.title "),
	
	@NamedQuery(name = "GET_DATA_OBJECT_BY_ID",
				query= "SELECT		daob.dataObjectId as dataObjectId, daob.title as title, daob.dataObject as dataObject, daob.status as status, daob.orgId as orgId, "
					 + "			daob.unitId as unitId "
					 + "FROM DataObjectMaster daob "
					 + "WHERE daob.dataObjectId =:dataObjectId "),
	
	@NamedQuery(name = "GET_ALL_DATA_OBJECT_LIST",
	query= "SELECT		daob.dataObjectId as dataObjectId, daob.title as title, daob.dataObject as dataObject, daob.status as status, daob.orgId as orgId, "
		 + "			daob.unitId as unitId "
		 + "FROM DataObjectMaster daob "
		 + "WHERE daob.orgId =:orgId and daob.unitId =:unitId  "
		 + " order by daob.title "),
	
	@NamedQuery(name = "GET_DATA_OBJECT_BY_NAME_NOT_ID",
	query= "SELECT		daob.dataObjectId as dataObjectId, daob.title as title, daob.dataObject as dataObject, daob.status as status, daob.orgId as orgId, "
		 + "			daob.unitId as unitId "
		 + "FROM DataObjectMaster daob "
		 + "WHERE daob.orgId =:orgId and daob.unitId =:unitId "
		 + " and  daob.dataObjectId <> :dataObjectId and lower(daob.title)=:title "
		)
})
@Entity
@Table(name = "m_data_object_master" , schema = "public")
@SequenceGenerator(name = "m_data_object_master_seq" , sequenceName = "public.m_data_object_master_seq" , allocationSize = 1)
public class DataObjectMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_data_object_master_seq")
	@Column(name = "data_object_id")
	private int dataObjectId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "data_object")
	private String dataObject;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "status")
	private char status;

	public int getDataObjectId() {
		return dataObjectId;
	}

	public void setDataObjectId(int dataObjectId) {
		this.dataObjectId = dataObjectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDataObject() {
		return dataObject;
	}

	public void setDataObject(String dataObject) {
		this.dataObject = dataObject;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}
}
