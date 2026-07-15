package com.param.procurement.purchase.dto.purchaseRequest;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.common.constants.Constants;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.procurement.purchase.service.purchaseRequest.PurchaseRequestService;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequest {
	private Integer id;
	private Integer itemId;
	private Integer uomTypeId;
	private Integer uomUnitId;
	private Integer quantity;
	private Integer pendingQuantity;
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	private Double cop;
	private Double amountCop;
	private String specification;
	private Integer indentQuantity;
	private List<Integer> indentId;
	private Integer statusId;
	private String validationMessage;

	@Autowired
	PurchaseRequestService prServcie;

	@Autowired
	SessionFactory sessionFactory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
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

	public List<Integer> getIndentId() {
		return indentId;
	}

	public void setIndentId(List<Integer> indentId) {
		this.indentId = indentId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getAmountCop() {
		return amountCop;
	}

	public void setAmountCop(Double amountCop) {
		this.amountCop = amountCop;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	@Override
	public String toString() {
		return "ItemRequest{" + "id=" + id + ", itemId=" + itemId + ", uomTypeId=" + uomTypeId + ", uomUnitId="
				+ uomUnitId + ", quantity=" + quantity + ", specification='" + specification + '\'' + ", indentId="
				+ indentId + ", statusId=" + statusId + '}';
	}

	public Boolean ValidateSave(Integer assetTypeId, Integer prTypeId, Integer storeId, IGlobalDao GlobalDao) {
		Boolean result = true;
		this.validationMessage = " ";
		if (this.itemId == null || this.itemId < 1) {
			result = false;
			this.validationMessage += "itemId |";
		}

		if (this.quantity == null || this.quantity < 1) {
			result = false;
			this.validationMessage += "quantity |";
		}

		if (this.statusId == null || this.statusId < 1) {
			result = false;
			this.validationMessage += "statusId |";
		}
		if (assetTypeId.equals(Constants.getConsumableId())) {
			if (this.uomTypeId == null || this.uomTypeId < 1) {
				result = false;
				this.validationMessage += "uomTypeId |";
			}
			if (this.uomUnitId == null || this.uomUnitId < 1) {
				result = false;
				this.validationMessage += "uomUnitId |";
			}
		}
		if (prTypeId.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())) {
			result = false;
			if (this.indentId == null || this.indentId.isEmpty()) {
				this.validationMessage += "indentId |";
			}
		}
		if (this.uomTypeId != null) {
			if (!isQuantityValidated(assetTypeId, storeId, this.itemId, this.quantity, this.uomTypeId, 0, GlobalDao)) {
				result = false;
				this.validationMessage += "Please enter valid PR quantity |";
			}
		} else {
			if (!isQuantityValidated(assetTypeId, storeId, this.itemId, this.quantity, null, 0, GlobalDao)) {
				result = false;
				this.validationMessage += "Please enter valid PR quantity |";
			}
		}
		return result;
	}

	public Boolean validateUpdate(Integer assetTypeId, Integer prTypeId, Integer storeId, IGlobalDao GlobalDao) {
		Boolean result = true;
		this.validationMessage = " ";

		if (this.quantity == null || this.quantity < 1) {
			result = false;
			this.validationMessage += "quantity |";
		}

		if (this.statusId == null || this.statusId < 1) {
			result = false;
			this.validationMessage += "statusId |";
		}

		// for adding new item in same PR
		if (this.id == null || this.id < 1) {
			if (this.itemId == null || this.itemId < 1) {
				ValidateSave(assetTypeId, prTypeId, storeId, GlobalDao);
			} else {
				result = false;
				this.validationMessage += "id |";
			}

		}

		return result;
	}

	private Boolean isQuantityValidated(Integer assetTypeId, Integer storeId, Integer itemId, Integer quantity,
			Integer uomTypeId, Integer currentLeaseQunatity, IGlobalDao GlobalDao) {
		Long storeStock = GlobalDao.getStoreStockByItem(storeId, itemId,0);
		Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
		Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId,null);
		Long prLeaseQunatity = GlobalDao.getPurchaseRequestLeaseQunatity(storeId, itemId,null);
		if (prLeaseQunatity < currentLeaseQunatity) {
			prLeaseQunatity = currentLeaseQunatity - prLeaseQunatity;
		} else if (prLeaseQunatity > currentLeaseQunatity) {
			prLeaseQunatity = prLeaseQunatity - currentLeaseQunatity;
		}
		// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
		// prLeaseQunatity);
		Integer leaseQuantity = quantity;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, getUom(itemId, GlobalDao), new UomType(uomTypeId));
		}
		if (maxStoreQuantity < (prLeaseQunatity + leaseQuantity + stockInTransit + storeStock)) {
			return false;
		}

		return true;
	}

	private List<PurchaseOrderUomMapper> getUom(Integer itemId, IGlobalDao GlobalDao) {
		List<UomResponse> uomResponse = GlobalDao.getUnitOfMeasurementListByItem(itemId,null);
		List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
		for (UomResponse uom : uomResponse) {
			PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
					new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
					uom.getStoreUom());
			prUomResponse.add(prUom);
		}
		return prUomResponse;
	}
}
