package com.param.entity.model.procurement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "LoanInDetail")
@Table(name = "t_loan_in_detail", schema = "procurement")
public class LoanInDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_in_id")
	private LoanIn loanIn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;
	
	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "cop")
	private Double cop;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "specification")
	private String specification;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@Column(name = "approved_quantity")
	private Integer approvedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loanInDetail", cascade = CascadeType.ALL)
	private List<LoanInUomMapper> loanInUomMapper;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoanIn getLoanIn() {
		return loanIn;
	}

	public void setLoanIn(LoanIn loanIn) {
		this.loanIn = loanIn;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}
	
	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public List<LoanInUomMapper> getLoanInUomMapper() {
		return loanInUomMapper;
	}

	public void setLoanInUomMapper(List<LoanInUomMapper> loanInUomMapper) {
		this.loanInUomMapper = loanInUomMapper;
	}

	public LoanInUomMapper addLoanInUomMapper(LoanInUomMapper loanInUomMapper) {
		if(getLoanInUomMapper() == null)
			this.loanInUomMapper = new ArrayList<LoanInUomMapper>();
		
		getLoanInUomMapper().add(loanInUomMapper);
		loanInUomMapper.setLoanInDetail(this);

		return loanInUomMapper;
	}
	
	public LoanInDetail() {
		super();
	}

	public LoanInDetail(Integer id, LoanIn loanIn, Item item, UomType uomType, UomUnit uomUnit,
			String batchNo, Date expiryDate, Integer quantity, Double cop, Double totalAmount, String remark,
			String specification, Status status, Integer approvedQuantity) {
		super();
		this.id = id;
		this.loanIn = loanIn;
		this.item = item;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
		this.approvedQuantity = approvedQuantity;
	}

	public LoanInDetail(Item item, UomType uomType, UomUnit uomUnit, String batchNo, Date expiryDate,
			Integer quantity, Double cop, Double totalAmount, String remark, String specification, Status status) {
		super();
		this.item = item;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
	}
	
	public LoanInDetail(Item item, String batchNo, Date expiryDate,
			Integer quantity, Double cop, Double totalAmount, String remark, String specification, Status status) {
		super();
		this.item = item;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
	}
	
	public LoanInDetail(Integer id) {
		super();
		this.id = id;
	}

	public void updateLoanInDetail(Item item, UomType uomType, UomUnit uomUnit, String batchNo, Date expiryDate,
			Integer quantity, Double cop, Double totalAmount, String remark, String specification, Status status)
	{
		this.item = item;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
	}
	
	public void updateLoanInDetail(Item item, String batchNo, Date expiryDate,
			Integer quantity, Double cop, Double totalAmount, String remark, String specification, Status status)
	{
		this.item = item;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
	}
	
}