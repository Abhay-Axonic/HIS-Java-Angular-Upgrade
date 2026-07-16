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
import javax.persistence.Transient;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Status;

/**
 * The persistent class for the
 * t_stop_transaction_product_category_exclusion_detail database table.
 * 
 */

@Entity
@Table(name = "t_stop_transaction_product_category_exclusion_detail", schema = "inventory")
public class StopTransactionProdCatExclDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_category_id")
	private ProductCategory productCategory;	
		
	@ManyToOne
	@JoinColumn(name = "stop_transaction_id")
	private StopTransaction StopTransaction;
	
//	@Transient
//	@Column(name = "is_deleted")
//	private Boolean isDeleted;
	
	public StopTransactionProdCatExclDetail() {
	}

	public StopTransactionProdCatExclDetail(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public StopTransactionProdCatExclDetail(ProductCategory productCategory,Status status) {
		super();
		this.status = status;
		this.productCategory = productCategory;	
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public StopTransaction getStopTransaction() {
		return this.StopTransaction;
	}

	public void setStopTransaction(StopTransaction StopTransaction) {
		this.StopTransaction = StopTransaction;
	}
	
	//dilip

//	public Boolean getIsDeleted2() {
//		return this.isDeleted;
//	}
//
//	public void setIsDeleted2(Boolean isDeleted) {
//		this.isDeleted = isDeleted;
//	}

}