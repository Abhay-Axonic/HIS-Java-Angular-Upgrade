package com.param.CSSD.model;

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

import com.param.CSSDMaster.model.InstrumentMaster;
import com.param.CSSDMaster.model.KitInstrumentMapper;
import com.param.CSSDMaster.model.KitMaster;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.global.model.EquipmentMaster;



@NamedQueries({
		@NamedQuery(name = "GET_PRE_STERILIZATION_PROCESS_EQUIPMENT_LIST", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId, "
				+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
				/*
				 * + "instrumentMaster.instrumentCode as instrumentCode," +
				 * "instrumentMaster.instrumentName as instrumentName,"
				 */
				+ "instrumentMaster.code as instrumentCode,"
				+ "instrumentMaster.name as instrumentName," 
				+ "preSterilizationProcessInstrument.status as status, "
				+ "kitInstrumentMapper.quantity as quantity " 
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as instrumentMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
				+ "WHERE preSterilizationProcessInstrument.kitId=:kitId "),
		@NamedQuery(name = "CHECK_KIT_STATUS_CLEANED", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId "
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "WHERE  preSterilizationProcessInstrument.kitId=:kitId AND "
				+ "preSterilizationProcessInstrument.status='uncleaned'"),
		@NamedQuery(name = "CHECK_KIT_STATUS_INSPECTED", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId "
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "WHERE preSterilizationProcessInstrument.kitId=:kitId AND "
				+ "preSterilizationProcessInstrument.status='cleaned'"),
		@NamedQuery(name = "CHECK_KIT_STATUS_DIS_INFECTED", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId "
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "WHERE  preSterilizationProcessInstrument.kitId=:kitId AND "
				+ "preSterilizationProcessInstrument.status='inspected'"),
		@NamedQuery(name = "GET_EQUIPMENT_LIST_PENDING_FOR_INSPECTION", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId, "
				+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
				+ "instrumentMaster.code as instrumentCode,"
				+ "instrumentMaster.name as instrumentName," 
				+ "preSterilizationProcessInstrument.status as status, "
				+ "kitInstrumentMapper.quantity as quantity " 
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as instrumentMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
				+ "WHERE preSterilizationProcessInstrument.kitId=:kitId AND "
				+ "preSterilizationProcessInstrument.status='cleaned'"),
		@NamedQuery(name = "GET_EQUIPMENT_LIST_PENDING_FOR_DIS_INFECTION", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId, "
				+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
				+ "instrumentMaster.code as instrumentCode,"
				+ "instrumentMaster.name as instrumentName," 
				+ "preSterilizationProcessInstrument.status as status, "
				+ "kitInstrumentMapper.quantity as quantity " 
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as instrumentMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
				+ "WHERE  preSterilizationProcessInstrument.kitId=:kitId AND "
				+ "preSterilizationProcessInstrument.status='inspected'"),
		
		@NamedQuery(name = "GET_EQUIPMENT_LIST_PENDING_FOR_KIT_FORMATION", query = "SELECT preSterilizationProcessInstrument.id as id, "
				+ "preSterilizationProcessInstrument.kitId as kitId, "
				+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
				+ "instrumentMaster.code as instrumentCode,"
				+ "instrumentMaster.name as instrumentName," 
				+ "preSterilizationProcessInstrument.status as status, "
				+ "kitInstrumentMapper.quantity as quantity " 
				+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
				+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as instrumentMaster "
				+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
				+ "WHERE  preSterilizationProcessInstrument.kitId=:kitId AND "
				+ "preSterilizationProcessInstrument.status='disinfected'")
})


@Entity
@Table(name = "t_pre_sterilize_process_instrument", schema = "cssd")
@SequenceGenerator(name = "t_pre_sterilize_process_instrument_id_seq", sequenceName = "cssd.t_pre_sterilize_process_instrument_id_seq", allocationSize = 1)
public class PreSterilizationProcessInstrument {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pre_sterilize_process_instrument_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name="transition_number")
	private Integer transitionNumber;

	@Column(name="instrument_id")
	private Integer instrumentId;
	
	@Column(name="physical_quantity")
	private Integer physicalQuantity;
	
	@Column(name="kit_instrument_id")
	private Integer kitInstrumentId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;
	
	/*
	 * @OneToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "instrument_id", insertable = false, updatable = false)
	 * private InstrumentMaster instrumentMaster;
	 */
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instrument_id", insertable = false, updatable = false)
	private Item instrumentMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster kitMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_instrument_id", insertable = false, updatable = false)
	private KitInstrumentMapper kitInstrumentMapper;
	
	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	

	public Integer getKitInstrumentId() {
		return kitInstrumentId;
	}

	public void setKitInstrumentId(Integer kitInstrumentId) {
		this.kitInstrumentId = kitInstrumentId;
	}

	public KitInstrumentMapper getKitInstrumentMapper() {
		return kitInstrumentMapper;
	}

	public void setKitInstrumentMapper(KitInstrumentMapper kitInstrumentMapper) {
		this.kitInstrumentMapper = kitInstrumentMapper;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}

	
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

	public Integer getPhysicalQuantity() {
		return physicalQuantity;
	}

	public void setPhysicalQuantity(Integer physicalQuantity) {
		this.physicalQuantity = physicalQuantity;
	}

	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	public Item getInstrumentMaster() {
		return instrumentMaster;
	}

	public void setInstrumentMaster(Item instrumentMaster) {
		this.instrumentMaster = instrumentMaster;
	}

	public Integer getTransitionNumber() {
		return transitionNumber;
	}

	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
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
