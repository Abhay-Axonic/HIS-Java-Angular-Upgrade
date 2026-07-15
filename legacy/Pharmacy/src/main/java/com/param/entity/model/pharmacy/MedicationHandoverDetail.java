package com.param.entity.model.pharmacy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
@Entity(name="MedicationHandoverDetail")
@Table(name="t_medication_handover_details",schema="pharmacy")
public class MedicationHandoverDetail extends BaseEntity implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false )
	private Integer id;
	@Column(name="prescription_detail_id")
	private Integer prescriptionDetailId;
	@Column(name="item_id")
	private Integer itemId;
	@Column(name="hand_over_qty")
	private Integer handOverQty;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	

	public Integer getHandOverQty() {
		return handOverQty;
	}

	public void setHandOverQty(Integer handOverQty) {
		this.handOverQty = handOverQty;
	}

	public MedicationHandover getMedicationHandover() {
		return medicationHandover;
	}

	public void setMedicationHandover(MedicationHandover medicationHandover) {
		this.medicationHandover = medicationHandover;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="medication_handover_id" )
	private MedicationHandover medicationHandover;
	
}
