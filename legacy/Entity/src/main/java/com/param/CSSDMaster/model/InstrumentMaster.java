package com.param.CSSDMaster.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Store;


@NamedQueries({ 

		@NamedQuery(name = "SEARCH_INSTRUMENT_BY_NAME", query = "SELECT inst.instrumentId as instrumentId,"
				+ " inst.instrumentName as instrumentName,"
				+ " inst.instrumentCode as instrumentCode " 
				+ " FROM InstrumentMaster inst where "
				+ " ( lower(inst.instrumentName) LIKE :name "
				+ " or lower(inst.instrumentCode) LIKE :name ) "
				+ " and ( inst.organizationId=:organizationId and inst.unitId=:unitId and inst.storeId=:storeId ) "
				),

		

})




@Entity
@Table(name = "m_instrument_master", schema = "cssd")
@SequenceGenerator(name = "m_instrument_master_instrument_id_seq", sequenceName = "cssd.m_instrument_master_instrument_id_seq", allocationSize = 1)
public class InstrumentMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_instrument_master_instrument_id_seq")
	@Column(name = "instrument_id")
	private Integer instrumentId;

	@Column(name = "instrument_code")
	private String instrumentCode;

	@Column(name = "instrument_name")
	private String instrumentName;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "store_id")
	private Integer storeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;


	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	
	public String getInstrumentCode() {
		return instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode) {
		this.instrumentCode = instrumentCode;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	
}
