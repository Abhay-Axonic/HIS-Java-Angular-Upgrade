package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;

import java.sql.Timestamp;


/**
 * The persistent class for the t_po_accessories_mapper database table.
 * 
 */
@Entity
@Table(name="t_po_accessories_mapper", schema = "procurement")
public class PoAccessoriesMapper extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_detail_id", nullable = false)
    private PurchaseOrderDetail purchaseOrderDetail;

	public PoAccessoriesMapper() {
		super();
	}

	public PoAccessoriesMapper(Item item) {
		super();
		this.item = item;
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

	public PurchaseOrderDetail getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

}