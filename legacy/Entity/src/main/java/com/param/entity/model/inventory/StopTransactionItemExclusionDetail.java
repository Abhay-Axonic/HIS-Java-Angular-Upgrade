package com.param.entity.model.inventory;

import java.io.Serial;
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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;

/**
 * The persistent class for the t_stop_transaction database table.
 * 
 */
@Entity
@Table(name = "t_stop_transaction_item_exclusion_detail", schema = "inventory")
public class StopTransactionItemExclusionDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stop_transaction_id")
	private StopTransaction StopTransaction;
	
	

	public StopTransactionItemExclusionDetail() {
		super();
	}
	

	public StopTransactionItemExclusionDetail(Item item,Status status) {
		this.item = item;
		this.status = status;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public StopTransaction getStopTransaction() {
		return StopTransaction;
	}

	public void setStopTransaction(StopTransaction stopTransaction) {
		StopTransaction = stopTransaction;
	}

}
