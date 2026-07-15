package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_COUNTER_MASTER_LIST",
		query="SELECT counterMaster.counterId AS counterId, counterMaster.counterCode AS counterCode, counterMaster.counterDesc AS counterDesc,"
					+ " counterMaster.organizationId AS organizationId, counterMaster.unitId AS unitId, counterMaster.status AS status"
					+ " FROM CounterMaster counterMaster where "
					+ " counterMaster.organizationId= :organizationId"
					+ " AND counterMaster.unitId= :unitId"
					+ " ORDER BY counterMaster.counterId asc"
					),
	
	@NamedQuery(name="GET_COUNTER_MASTER_BY_ID",
		query="SELECT counterMaster.counterId AS counterId, counterMaster.counterCode AS counterCode, counterMaster.counterDesc AS counterDesc,"
					+ " counterMaster.organizationId AS organizationId, counterMaster.unitId AS unitId, counterMaster.status AS status"
					+ " FROM CounterMaster counterMaster"
					+ " WHERE counterMaster.counterId= :counterId"
					+ " AND counterMaster.organizationId= :organizationId"
					+ " AND counterMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_COUNTER_MASTER",
		query="SELECT counterMaster.counterId AS counterId, counterMaster.counterCode AS counterCode, counterMaster.counterDesc AS counterDesc,"
					+ " counterMaster.organizationId AS organizationId, counterMaster.unitId AS unitId, counterMaster.status AS status"
					+ " FROM CounterMaster counterMaster"
					+ " WHERE counterMaster.status= 'A'"
					+ " AND counterMaster.organizationId= :organizationId"
					+ " AND counterMaster.unitId= :unitId"
					+ " ORDER BY counterMaster.counterId asc"
					),
	
	@NamedQuery(name="GET_COUNTER_MASTER_BY_NAME",
		query="SELECT counterMaster.counterId AS counterId "
					+ "	FROM CounterMaster counterMaster"
					+ " WHERE LOWER(counterMaster.counterDesc)= :counterDesc "
					+ " AND counterMaster.organizationId= :organizationId "
					+ " AND counterMaster.unitId= :unitId "
					),
	
	@NamedQuery(name="GET_COUNTER_MASTER_BY_NAME_NOT_ID",
		query="SELECT counterMaster.counterId AS counterId"
					+ " FROM CounterMaster counterMaster"
					+ " WHERE LOWER(counterMaster.counterDesc)= :counterDesc"
					+ " AND counterMaster.counterId != :counterId "
					+ " AND counterMaster.organizationId= :organizationId "
					+ " AND counterMaster.unitId= :unitId "
					),
	})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_COUNTERS",
		query="select "
				+ "	counter_mst.counter_id as \"id\", "
				+ "	counter_mst.counter_desc as \"name\", "
				+ "	counter_mst.counter_code as \"code\" "
				+ "from "
				+ "	public.m_counter_master counter_mst "
				+ "where "
				+ "	counter_mst.organization_id = :orgId "
				+ "	and counter_mst.unit_id =:unitId "
				+ "	and counter_mst.status = 'A'"
					)
})

@Entity
@Table(name = "m_counter_master", schema = "public")
@SequenceGenerator(name = "seq_m_counter_master", sequenceName = "public.seq_m_counter_master", allocationSize = 1)
public class CounterMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_counter_master")
	@Column(name = "counter_id")
	private Integer counterId;
	
	@Column(name = "counter_code")
	private String counterCode;
	
	@Column(name = "counter_desc")
	private String counterDesc;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterDesc() {
		return counterDesc;
	}

	public void setCounterDesc(String counterDesc) {
		this.counterDesc = counterDesc;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	
}
