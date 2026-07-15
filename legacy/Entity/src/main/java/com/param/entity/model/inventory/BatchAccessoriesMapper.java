package com.param.entity.model.inventory;

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

@Entity(name = "BatchAccessoriesMapper")
@Table(name = "t_batch_accessories_mapper", schema = "inventory")
public class BatchAccessoriesMapper extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	protected Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_accessories_id", nullable = false)
	private Batch batchAccessory;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Batch getBatchAccessory() {
		return batchAccessory;
	}

	public void setBatchAccessory(Batch batchAccessory) {
		this.batchAccessory = batchAccessory;
	}

	public BatchAccessoriesMapper(Batch batch, Batch batchAccessory) {
		super();
		this.batch = batch;
		this.batchAccessory = batchAccessory;
	}

	public BatchAccessoriesMapper() {
		super();
	}

	

	public BatchAccessoriesMapper(Batch batch) {
		this.batchAccessory=batch;
	}
	
}