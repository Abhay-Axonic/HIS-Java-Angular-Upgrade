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

@Entity(name = "MaterialInwardAccessories")
@Table(name = "t_inward_accessories", schema = "inventory")
public class MaterialInwardAccessories extends BaseEntity{
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inward_detail_id")
	private MaterialInwardDetail materialInwardDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	


	@Column(name = "approved_quantity")
	private Integer approvedQty;

	@Column(name = "specification")
	private String specification;

	@Column(name = "quantity")
	private Integer quantity = 0;
	


	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MaterialInwardDetail getMaterialInwardDetail() {
		return materialInwardDetail;
	}

	public void setMaterialInwardDetail(MaterialInwardDetail materialInwardDetail) {
		this.materialInwardDetail = materialInwardDetail;
	}

	public Integer getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(Integer approvedQty) {
		this.approvedQty = approvedQty;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public MaterialInwardAccessories() {
		super();
	}
	public MaterialInwardAccessories(Item item, Integer quantity, Integer approveduantity,String specification) {
		this.quantity = quantity;
		this.item = item;
		this.approvedQty=approveduantity;
		this.specification = specification;

	}
	

}
