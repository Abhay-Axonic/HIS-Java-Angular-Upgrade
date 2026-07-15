package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.ArrayList;
import java.util.List;


import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.PaymentMode;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

public class PurchaseOrderDropDownDto {
	
	List<Store> requestingStores;
	List<Store> deliveryStores;
	List<Status> status;
	List<AssetType> assetType;
	List<PaymentMode> paymentMode;
	
	
	public List<Store> getRequestingStores() {
		return requestingStores;
	}
	public void setRequestingStores(List<Store> requestingStores) {
		this.requestingStores = requestingStores;
	}

	public List<Store> getDeliveryStores() {
		return deliveryStores;
	}

	public void setDeliveryStores(List<Store> deliveryStores) {
		this.deliveryStores = deliveryStores;
	}

	public List<Status> getStatus() {
		return status;
	}
	public void setStatus(List<Status> status) {
		this.status = status;
	}
	
	public List<AssetType> getAssetType() {
		return assetType;
	}
	public void setAssetType(List<AssetType> assetType) {
		this.assetType = assetType;
	}
	
	public List<PaymentMode> getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(List<PaymentMode> paymentMode) {
		this.paymentMode = paymentMode;
	}
	public void addRequestingStore(Store store) {
		if (this.requestingStores == null)
			this.requestingStores = new ArrayList<Store>();

		this.requestingStores.add(store);
	}

	public void addDeliveryStore(Store store) {
		if (this.deliveryStores == null)
			this.deliveryStores = new ArrayList<Store>();

		this.deliveryStores.add(store);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();

		this.status.add(status);
	}
	
	public void addAssetType(AssetType assetType) {
		if (this.assetType == null)
			this.assetType = new ArrayList<AssetType>();

		this.assetType.add(assetType);
	}

	public void addPaymentMode(PaymentMode paymentMode) {
		if (this.paymentMode == null)
			this.paymentMode = new ArrayList<PaymentMode>();

		this.paymentMode.add(paymentMode);
	}
}
