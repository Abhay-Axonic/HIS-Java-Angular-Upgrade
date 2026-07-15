package com.param.CSSDMaster.model;

import java.util.Date;

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

import com.param.entity.model.master.Item;

@NamedQueries({ @NamedQuery(name = "DELETE_KIT_INSTRUMENT_MAPPER_LIST", query = "DELETE FROM KitInstrumentMapper mapper"
		+ " WHERE mapper.kitId =:kitId"),

		@NamedQuery(name = "GET_KIT_INSTRUMENT_MAPPER_LIST", query = "SELECT mapper.kitInstrumentId as kitInstrumentId, mapper.kitId as kitId,"
				+ " mapper.instrumentId as instrumentId,item.name as instrumentName, mapper.quantity as quantity,"
				+ " mapper.created_by as created_by,mapper.updated_by as updated_by,"
				+ " to_char(mapper.created_date,'dd-MM-yyyy') as created_date,"
				+ " to_char(mapper.updated_date,'dd-MM-yyyy') as updated_date " 
				+ " FROM KitInstrumentMapper mapper "
				+ " INNER JOIN mapper.item as item "),
		
		@NamedQuery(name = "GET_INSTRUMENT_LIST_BY_KIT_ID", query = "SELECT mapper.kitInstrumentId as kitInstrumentId, "
				+ "	mapper.instrumentId as instrumentId,item.name as instrumentName,item.code as instrumentCode,"
				+ " mapper.quantity as quantity FROM KitInstrumentMapper mapper "
				+ "	INNER JOIN mapper.item as item WHERE mapper.kitId=:kitId")

})

@Entity
@Table(name = "t_kit_instrument_mapper", schema = "cssd")
@SequenceGenerator(name = "t_kit_instrument_mapper_kit_instrument_id_seq", sequenceName = "cssd.t_kit_instrument_mapper_kit_instrument_id_seq", allocationSize = 1)
public class KitInstrumentMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_instrument_mapper_kit_instrument_id_seq")
	@Column(name = "kit_instrument_id")
	private Integer kitInstrumentId;

	@Column(name = "kit_id")
	private Integer kitId;

	@Column(name = "instrument_id")
	private Integer instrumentId;

	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;

	
	  @OneToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "instrument_id", insertable = false, updatable = false)
	  private Item item;
	 
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster kitMaster;

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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	
	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}


	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}

	public Integer getKitInstrumentId() {
		return kitInstrumentId;
	}

	public void setKitInstrumentId(Integer kitInstrumentId) {
		this.kitInstrumentId = kitInstrumentId;
	}

	

}
