package com.param.entity.model.inventory;

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
import com.param.entity.model.master.Item;

@Entity(name = "DrugRecallDetail")
@Table(name = "t_drug_recall_detail", schema = "inventory")
public class DrugRecallDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "reason")
	private String reason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drug_recall_id")
	private DrugRecall DrugRecall;

	public DrugRecallDetail() {
		super();
	}

	public DrugRecallDetail(Item item, String reason) {
		this.item = item;
		this.reason = reason;
	}

	public DrugRecallDetail(Batch batch, String reason) {
		this.batch = batch;
		this.reason = reason;
	}

	public DrugRecallDetail(Item item, Batch batch, String reason) {
		this.item = item;
		this.batch = batch;
		this.reason = reason;
	}

	public DrugRecallDetail(Batch batch) {
		this.batch = batch;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public DrugRecall getDrugRecall() {
		return DrugRecall;
	}

	public void setDrugRecall(DrugRecall drugRecall) {
		DrugRecall = drugRecall;
	}

	public void updateDrugRecallDetail(Item item, Batch batch, String reason) {
		this.item = item;
		this.batch = batch;
		this.reason = reason;

	}

	public void addBatchDetail(Batch batch) {
		this.batch = batch;
	}

}
