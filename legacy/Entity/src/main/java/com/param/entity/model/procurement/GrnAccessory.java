package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the t_grn_accrsssories database table.
 * 
 */
@Entity
@Table(name="t_grn_accessories", schema = "procurement")
public class GrnAccessory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name="serial_no")
	private String serialNo;

	@Temporal(TemporalType.DATE)
	@Column(name="warrenty_from")
	private Date warrentyFrom;

	@Temporal(TemporalType.DATE)
	@Column(name="warrenty_to")
	private Date warrentyTo;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="specification")
	private String specification;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_detail_id", nullable = false)
	private GoodReceiptNoteDetail grnDetail;

	public GrnAccessory() {
		super();
	}

	public GrnAccessory(Item item, String serialNo, Date warrentyFrom, Date warrentyTo) {
		super();
		this.item = item;
		this.serialNo = serialNo;
		this.warrentyFrom = warrentyFrom;
		this.warrentyTo = warrentyTo;
	}

		public GrnAccessory(Item item, String serialNo, Date warrentyFrom, Date warrentyTo,Integer quantity,String specification) {
		super();
		this.item = item;
		this.serialNo = serialNo;
		this.warrentyFrom = warrentyFrom;
		this.warrentyTo = warrentyTo;
		this.quantity = quantity;
		this.specification = specification;
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public GoodReceiptNoteDetail getGrnDetail() {
		return grnDetail;
	}

	public void setGrnDetail(GoodReceiptNoteDetail grnDetail) {
		this.grnDetail = grnDetail;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Date getWarrentyFrom() {
		return warrentyFrom;
	}

	public void setWarrentyFrom(Date warrentyFrom) {
		this.warrentyFrom = warrentyFrom;
	}

	public Date getWarrentyTo() {
		return warrentyTo;
	}

	public void setWarrentyTo(Date warrentyTo) {
		this.warrentyTo = warrentyTo;
	}
	
}