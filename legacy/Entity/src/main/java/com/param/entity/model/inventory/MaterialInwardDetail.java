package com.param.entity.model.inventory;

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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "MaterialInwardDetail")
@Table(name = "t_material_inwards_detail", schema = "inventory")
public class MaterialInwardDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "batch_no")
	private String batchNo;

	@Column(name = "expiry_date")
	private Date expiryDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "lot_no")
	private String lotNo;

	private Double cop;

	private Double mrp;

	private Double markup;

	@Column(name = "net_amount")
	private Double netAmount;
	@Column(name = "available_amount")
	private Double availableAmount;

	public Double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}

	private String specification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "credit_note_amount")
	private Double creditNoteAmount;

	@Column(name = "received_qty")
	private Integer receivedQty;

	@Column(name = "credit_tax_amount")
	private Double creditTaxAmount;

	@Column(name = "credit_net_amount")
	private Double creditNetAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mrp_uom_type_id")
	private UomType mrpUomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReasonId;

	@Column(name = "reject_quantity")
	private Integer rejectQuantity;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "approved_quantity")
	private Integer approvedQuantity;

	@Column(name = "return_quantity")
	private Integer returnQuantity;

	@Column(name = "return_amount")
	private Double returnAmount;


	// bi-directional many-to-one association to TMaterialInward
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "material_inwards_id")
	private MaterialInward materialInward;
	@Column(name = "is_reject")
	protected Boolean isReject;
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "returnable_gatepass_id")
	private ReturnableGatepass returnableGatepassId;*/
	
	public boolean getIsReject() {
		return isReject;
	}

	public void setIsReject(boolean isReject) {
		this.isReject = isReject;
	}

	/* start code for resolved HIS-5496 Material Inward - Replacement - Not working */
			//commented above private ReturnableGatepass returnableGatepassId;	
	@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "returnable_gatepass_detail_id")
	@JoinColumn(name = "returnable_gatepass_detail_id") //in database  its foreign key returnable_gatepass_detail_id(naming issue).
	private ReturnableGatepassDetail returnableGatepassDetail;
	public ReturnableGatepassDetail getReturnableGatepassDetail() {
		return returnableGatepassDetail;
	}
	public void setReturnableGatepassDetail(ReturnableGatepassDetail returnableGatepassDetail) {
		this.returnableGatepassDetail = returnableGatepassDetail;
	}
	/* end code for resolved HIS-5496 Material Inward - Replacement - Not working */
	

	// bi-directional many-to-one association to TInwardsUomMapper
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialInwardDetail", cascade = CascadeType.ALL)
	private List<InwardsUomMapper> inwardsUomMapperList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialInwardDetail", cascade = CascadeType.ALL)
	private List<InwardTaxComponent> inwardTaxComponent;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialInwardDetail", cascade = CascadeType.ALL)
	private List<MaterialInwardAccessories> materialInwardAccessories;

	public List<MaterialInwardAccessories> getMaterialInwardAccessories() {
		return materialInwardAccessories;
	}

	public void setMaterialInwardAccessories(List<MaterialInwardAccessories> materialInwardAccessories) {
		this.materialInwardAccessories = materialInwardAccessories;
	}

	public MaterialInwardDetail() {
		super();
	}

	public MaterialInwardDetail(Integer id) {
		super();
		this.id = id;
	}

	public MaterialInwardDetail(/*ReturnableGatepass returnableGatepass*/ReturnableGatepassDetail returnableGatepassDetail, Item item, Integer quantity,
			UomType uomType, UomUnit uomUnit, String batchNo, Date expiryDate, Double cop, Double mrp, Tax tax,
			Double taxPercent, Double taxAmount, Double totalAmount, Double totalNetAmount, String specification,
			Double markup, UomType mrpUomType, Status status,Double creditNetAmount,Double availableAmount) {
		//this.returnableGatepassId = returnableGatepass;
		this.returnableGatepassDetail = returnableGatepassDetail; /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
		this.item = item;
		this.receivedQty = quantity;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.tax = tax;
		this.taxPercentage = taxPercent;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = totalNetAmount;
		this.specification = specification;
		this.markup = markup;
		this.mrpUomType = mrpUomType;
		this.status = status;
		this.creditNetAmount = creditNetAmount;
		this.availableAmount = availableAmount;
	}

	public MaterialInwardDetail(/*ReturnableGatepass returnableGatepass*/ ReturnableGatepassDetail returnableGatepassDetail, Item item, Integer quantity,
			String batchNo, Date expiryDate, Double cop, Double mrp, Tax tax, Double taxPercent, Double taxAmount,
			Double totalAmount, Double totalNetAmount, String specification, Double markup, Status status) {
		//this.returnableGatepassId = returnableGatepass;
		this.returnableGatepassDetail = returnableGatepassDetail; /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
		
		this.item = item;
		this.receivedQty = quantity;

		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.tax = tax;
		this.taxPercentage = taxPercent;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = totalNetAmount;
		this.specification = specification;
		this.markup = markup;

		this.status = status;
	}

	/*
	 * public MaterialInwardDetail(ReturnableGatepassDetail returnableGatepass, Item
	 * item, Integer quantity, UomType uomType, UomUnit uomUnit, String batchNo,
	 * Date expiryDate, Double cop, Double mrp, Tax tax, Double taxPercent, Double
	 * taxAmount, Double totalAmount, Double totalNetAmount, String specification,
	 * Double markup, UomType mrpUomType, Status status) {
	 * this.returnableGatepassDetailId = returnableGatepass; this.item = item;
	 * this.receivedQty = quantity; this.uomType = uomType; this.uomUnit = uomUnit;
	 * this.batchNo = batchNo; this.expiryDate = expiryDate; this.cop = cop;
	 * this.mrp = mrp; this.tax = tax; this.taxPercentage = taxPercent;
	 * this.taxAmount = taxAmount; this.totalAmount = totalAmount; this.netAmount =
	 * totalNetAmount; this.specification = specification; this.markup = markup;
	 * this.mrpUomType =mrpUomType; }
	 */
	public MaterialInwardDetail(Integer id, String batchNo, Double cop, Date expiryDate, Integer receivedQty, Item item,
			String lotNo, Double mrp, Double netAmount, /*ReturnableGatepass returnableGatepassDetailId*/ ReturnableGatepassDetail returnableGatepassDetail,
			String specification, Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount,
			Double creditNoteAmount, Double creditTaxAmount, Double creditNetAmount, UomType uomType, UomUnit uomUnit,
			com.param.entity.model.inventory.MaterialInward materialInward, List<InwardsUomMapper> inwardsUomMapper) {
		super();
		this.id = id;
		this.batchNo = batchNo;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.receivedQty = receivedQty;
		this.item = item;
		this.lotNo = lotNo;
		this.mrp = mrp;
		this.netAmount = netAmount;
		//this.returnableGatepassId = returnableGatepassDetailId;
		this.returnableGatepassDetail = returnableGatepassDetail; /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
		this.specification = specification;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.creditNoteAmount = creditNoteAmount;
		this.creditTaxAmount = creditTaxAmount;
		this.creditNetAmount = creditNetAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.materialInward = materialInward;
		this.inwardsUomMapperList = inwardsUomMapper;
	}

	public UomType getMrpUomType() {
		return mrpUomType;
	}

	public void setMrpUomType(UomType mrpUomType) {
		this.mrpUomType = mrpUomType;
	}

	public MaterialInwardDetail(Double creditNoteAmount, Double creditTaxAmount, Double creditNetAmount, Status status,
			Double totalAmount, Double totalTaxAmount, Double totalNetAmount,
			/*ReturnableGatepass returnableGatepass*/ ReturnableGatepassDetail returnableGatepassDetail) {
		this.creditNoteAmount = creditNoteAmount;
		this.creditTaxAmount = creditTaxAmount;
		this.creditNetAmount = creditNetAmount;
		this.status = status;
		this.totalAmount = totalAmount;
		this.taxAmount = totalTaxAmount;
		this.netAmount = totalNetAmount;
		//this.returnableGatepassId = returnableGatepass;
		this.returnableGatepassDetail = returnableGatepassDetail; /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
		this.availableAmount = creditNetAmount;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Double getCop() {
		return cop;
	}

	public Integer getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(Integer receivedQty) {
		this.receivedQty = receivedQty;
	}

	public RejectReason getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(RejectReason rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public List<InwardsUomMapper> getInwardsUomMapperList() {
		return inwardsUomMapperList;
	}

	public void setInwardsUomMapperList(List<InwardsUomMapper> inwardsUomMapperList) {
		this.inwardsUomMapperList = inwardsUomMapperList;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	

	/*public ReturnableGatepass getReturnableGatepassId() {
		return returnableGatepassId;
	}

	public void setReturnableGatepassId(ReturnableGatepass returnableGatepassId) {
		this.returnableGatepassId = returnableGatepassId;
	}*/

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getCreditNoteAmount() {
		return creditNoteAmount;
	}

	public void setCreditNoteAmount(Double creditNoteAmount) {
		this.creditNoteAmount = creditNoteAmount;
	}

	public Double getCreditTaxAmount() {
		return creditTaxAmount;
	}

	public void setCreditTaxAmount(Double creditTaxAmount) {
		this.creditTaxAmount = creditTaxAmount;
	}

	public Double getCreditNetAmount() {
		return creditNetAmount;
	}

	public void setCreditNetAmount(Double creditNetAmount) {
		this.creditNetAmount = creditNetAmount;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public MaterialInward getMaterialInward() {
		return materialInward;
	}

	public void setMaterialInward(MaterialInward materialInward) {
		this.materialInward = materialInward;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<InwardTaxComponent> getInwardTaxComponent() {
		return inwardTaxComponent;
	}

	public void setInwardTaxComponent(List<InwardTaxComponent> inwardTaxComponent) {
		this.inwardTaxComponent = inwardTaxComponent;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public InwardsUomMapper addInwardsUomMapper(InwardsUomMapper inwardsUomMapper) {

		if (getInwardsUomMapperList() == null)
			this.inwardsUomMapperList = new ArrayList<InwardsUomMapper>();
		getInwardsUomMapperList().add(inwardsUomMapper);
		inwardsUomMapper.setMaterialInwardDetail(this);

		return inwardsUomMapper;
	}

	public MaterialInwardAccessories addMaterialInwardAccessories(MaterialInwardAccessories materialInwardAccessories) {

		if (getMaterialInwardAccessories() == null)
			this.materialInwardAccessories = new ArrayList<MaterialInwardAccessories>();
		getMaterialInwardAccessories().add(materialInwardAccessories);
		materialInwardAccessories.setMaterialInwardDetail(this);

		return materialInwardAccessories;
	}

	public void updateMaterialInwardDetail(String batchNo, Double cop, Tax tax, Double taxPercent, Double taxAmount,
			Double netAmount, Integer quantity, Double creditNoteAmount, Double creditTaxAmount,
			Double creditNetAmount) {
		this.batchNo = batchNo;
		this.cop = cop;
		this.tax = tax;
		this.taxPercentage = taxPercent;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.receivedQty = quantity;
		this.creditNoteAmount = creditNoteAmount;
		this.creditTaxAmount = creditTaxAmount;
		this.creditNetAmount = creditNetAmount;

	}

	public void updateMaterialInwardDetail(String batchNo, Date expiryDate, Double cop, Double mrp, Double markup,
			Double netAmount, String specification, Double taxAmount, Double totalAmount, Integer receivedQty,
			UomType mrpUomType, UomType uomType, UomUnit uomUnit) {
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.markup = markup;
		this.netAmount = netAmount;
		this.specification = specification;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.receivedQty = receivedQty;
		this.mrpUomType = mrpUomType;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public void updateMaterialInwardDetail(String batchNo, Date expiryDate, Double cop, Double mrp, Double markup,
			Double netAmount, String specification, Double taxAmount, Double totalAmount, Integer receivedQty) {
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.markup = markup;
		this.netAmount = netAmount;
		this.specification = specification;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.receivedQty = receivedQty;
		this.mrpUomType = mrpUomType;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public void updateMaterialInwardDetail(Double creditNoteAmount, Double creditTaxAmount, Double creditNetAmount) {
		this.creditNoteAmount = creditNoteAmount;
		this.creditTaxAmount = creditTaxAmount;
		this.creditNetAmount = creditNetAmount;
	}

	public void approveMaterialInwardDetail(Integer approvedQuantity, Integer rejectQuantity, String rejectNote,
			RejectReason rejectReason, Status status) {
		this.approvedQuantity = approvedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectNote = rejectNote;
		this.rejectReasonId = rejectReason;
		this.status = status;
	}

	public void approveMaterialInwardDetail(Status status, Double creditNoteAmount, Double creditTaxAmount,
			Double creditNetAmount) {
		this.status = status;
		this.creditNoteAmount = creditNoteAmount;
		this.creditTaxAmount = creditTaxAmount;
		this.creditNetAmount = creditNetAmount;
	}

	public InwardTaxComponent addInwardTaxCom(InwardTaxComponent inwardTaxCom) {
		if (this.inwardTaxComponent == null)
			this.inwardTaxComponent = new ArrayList<InwardTaxComponent>();

		this.inwardTaxComponent.add(inwardTaxCom);
		inwardTaxCom.setMaterialInwardDetail(this);

		return inwardTaxCom;
	}

	@Override
	public String toString() {
		return "MaterialInwardDetail [id=" + id + ", batchNo=" + batchNo + ", cop=" + cop + ", expiryDate=" + expiryDate
				+ ", item=" + item + ", lotNo=" + lotNo + ", mrp=" + mrp + ", netAmount=" + netAmount
				+ ", specification=" + specification + ", status=" + status + ", taxAmount=" + taxAmount + ", tax="
				+ tax + ", taxPercentage=" + taxPercentage + ", totalAmount=" + totalAmount + ", creditNoteAmount="
				+ creditNoteAmount + ", receivedQty=" + receivedQty + ", creditTaxAmount=" + creditTaxAmount
				+ ", creditNetAmount=" + creditNetAmount + ", uomType=" + uomType + ", uomUnit=" + uomUnit
				+ ", rejectReasonId=" + rejectReasonId + ", rejectQuantity=" + rejectQuantity + ", rejectNote="
				+ rejectNote + ", approvedQuantity=" + approvedQuantity + ", materialInward=" + materialInward
				+ ", returnableGatepassDetailId=" + /*returnableGatepassId*/ returnableGatepassDetail  + ", inwardsUomMapperList="
				+ inwardsUomMapperList + ", inwardTaxComponent=" + inwardTaxComponent + "]";
	}
}
