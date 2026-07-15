package com.param.entity.model.inventory;

import java.util.Date;

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

@Entity(name = "OpeningBalanceAccessories")
@Table(name = "t_opening_balance_accessories", schema = "inventory")
public class OpeningBalanceAccessories extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opening_balance_detail_id")
	private OpeningBalanceDetail openingBalanceDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@Column(name = "serial_number", length = 50)
	private String serialNumber;
	
	@Column(name = "warrenty_from")
	private Date warrantyFrom;
	
	@Column(name = "warrenty_to")
	private Date warrantyTo;
	
	@Column(name = "specification")
	private String specification;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "working_condition_id")
	private WorkingCondition workingCondition;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OpeningBalanceDetail getOpeningBalanceDetail() {
		return openingBalanceDetail;
	}

	public void setOpeningBalanceDetail(OpeningBalanceDetail openingBalanceDetail) {
		this.openingBalanceDetail = openingBalanceDetail;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getWarrantyFrom() {
		return warrantyFrom;
	}

	public void setWarrantyFrom(Date warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}

	public Date getwarrantyTo() {
		return warrantyTo;
	}

	public void setwarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
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
	
	public WorkingCondition getWorkingCondition() {
		return workingCondition;
	}

	public void setWorkingCondition(WorkingCondition workingCondition) {
		this.workingCondition = workingCondition;
	}

	public OpeningBalanceAccessories() {
		super();
	}
	public void updateAccessories(Item item, String serialNumber, Date warrantyFrom, Date warrantyTo,
			String specification, Integer quantity, WorkingCondition workingCondition)
	{
		this.item = item;
		this.serialNumber = serialNumber;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.specification = specification;
		this.quantity = quantity;
		this.workingCondition = workingCondition;
	}
	public OpeningBalanceAccessories(Integer id, OpeningBalanceDetail openingBalanceDetail, Item item,
			String serialNumber, Date warrantyFrom, Date warrantyTo, String specification, WorkingCondition workingCondition) {
		super();
		this.id = id;
		this.openingBalanceDetail = openingBalanceDetail;
		this.item = item;
		this.serialNumber = serialNumber;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.specification = specification;
		this.workingCondition = workingCondition;
	}

	public OpeningBalanceAccessories(Item item, String serialNumber, Date warrantyFrom, Date warrantyTo,
			String specification, Integer quantity, WorkingCondition workingCondition) {
		super();
		this.item = item;
		this.serialNumber = serialNumber;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.specification = specification;
		this.quantity = quantity;
		this.workingCondition = workingCondition;
	}

}