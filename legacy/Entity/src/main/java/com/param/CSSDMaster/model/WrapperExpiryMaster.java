package com.param.CSSDMaster.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;

import io.swagger.annotations.ApiModelProperty;

@NamedQueries({

		@NamedQuery(name = "GET_WRAPPER_EXPIRY_LIST", query = "SELECT wrapperExpiryMaster.id as id, "
				+ "wrapperExpiryMaster.code as code,"
				+ "wrapperExpiryMaster.status as status, " 
				+ "wrapperExpiryMaster.name as name, "
				+ "wrapperExpiryMaster.duration as duration, " 
				+ "wrapperExpiryMaster.itemId as itemId, "
				+ "wrapperExpiryMaster.created_by as created_by, "
				+ "to_char(wrapperExpiryMaster.created_date,'dd-MM-yyyy') as created_date, "
				+ "wrapperExpiryMaster.updated_by as updated_by, "
				+ "to_char(wrapperExpiryMaster.updated_date,'dd-MM-yyyy') as updated_date, "
				+ "item.name as itemName "
				+ "FROM WrapperExpiryMaster as wrapperExpiryMaster "
				+ "LEFT JOIN wrapperExpiryMaster.item as item "
				+ " WHERE wrapperExpiryMaster.organizationId=:organizationId "
				+ "and wrapperExpiryMaster.unitId=:unitId  "),
		
		@NamedQuery(name = "GET_ACTIVE_WRAPPER_EXPIRY_LIST", query = "SELECT wrapperExpiryMaster.id as id, "
				+ "wrapperExpiryMaster.code as code, "
				+ "wrapperExpiryMaster.status as status, "
				+ "wrapperExpiryMaster.name as name, "
				+ "wrapperExpiryMaster.duration as duration, " 
				+ "wrapperExpiryMaster.itemId as itemId, "
				+ "wrapperExpiryMaster.created_by as created_by, "
				+ "to_char(wrapperExpiryMaster.created_date,'dd-MM-yyyy') as created_date, "
				+ "wrapperExpiryMaster.updated_by as updated_by, "
				+ "to_char(wrapperExpiryMaster.updated_date,'dd-MM-yyyy') as updated_date, "
				+ "item.name as itemName "
				+ "FROM WrapperExpiryMaster as wrapperExpiryMaster  "
				+ "LEFT JOIN wrapperExpiryMaster.item as item "
				+ "WHERE wrapperExpiryMaster.status='A'  "
				+ " AND wrapperExpiryMaster.organizationId=:organizationId "
				+ " AND wrapperExpiryMaster.unitId=:unitId  "),
		
		@NamedQuery(name = "GET_WRAPPER_ITEM_LIST", query = "SELECT item.id as id,item.code "
				+ "FROM Item as item INNER JOIN item.organization as org "
				+ "INNER JOIN item.unit as uni  "
				+ "WHERE to_lower(item.code) LIKE wrapper OR to_lower(item.name) LIKE wrapper "
				+ " and org.organizationId=:organizationId and uni.unitId=:unitId "),
	
		@NamedQuery(name = "GET_ACTIVE_WRAPPER_EXPIRY_LIST_BY_CODE", query = "SELECT wrapperExpiryMaster.id as id, "
				+ " wrapperExpiryMaster.code as code,"
				+ " wrapperExpiryMaster.status as status, "
				+  "wrapperExpiryMaster.name as name, "
				+ " wrapperExpiryMaster.duration as duration, "
				+ " wrapperExpiryMaster.itemId as itemId, "
				+ " wrapperExpiryMaster.created_by as created_by, "
				+ " to_char(wrapperExpiryMaster.created_date,'dd-MM-yyyy') as created_date, "
				+ " wrapperExpiryMaster.updated_by as updated_by, "
				+ " to_char(wrapperExpiryMaster.updated_date,'dd-MM-yyyy') as updated_date, "
				+ " item.name as itemName "
				+ " FROM WrapperExpiryMaster as wrapperExpiryMaster "
				+ " LEFT JOIN wrapperExpiryMaster.item as item "
				+ " WHERE wrapperExpiryMaster.status='A'  "
				+ " AND wrapperExpiryMaster.organizationId=:organizationId "
				+ " AND wrapperExpiryMaster.unitId=:unitId "
				+ " AND lower(wrapperExpiryMaster.name) LIKE :code ")

		
		
})

@Entity
@Table(name = "m_wrapper_expiry_master", schema = "cssd")
@SequenceGenerator(name = "m_wrapper_expiry_master_id_seq", sequenceName = "cssd.m_wrapper_expiry_master_id_seq", allocationSize = 1)
public class WrapperExpiryMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_wrapper_expiry_master_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "duration")
	private Integer duration;

	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	
    @Column(name = "is_active") 
    private Boolean is_active;
  
    @Column(name = "is_deleted") 
    private Boolean is_deleted;
	 

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;
	
	@Column(name = "status")
	private Character status;
	
	/*
	 * @Column(name = "store_id") private Integer storeId;
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;
	

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

}
