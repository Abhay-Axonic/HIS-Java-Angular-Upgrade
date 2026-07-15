package com.param.procurement.purchase.dto.purchaseRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.global.Priority;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseRequestDropdownResponse {

    protected List<Store> requestingStores;

    protected List<Store> deliveryStores;

    protected List<Status> status;

    protected List<Priority> priority;

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

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

    public List<Priority> getPriority() {
        return priority;
    }

    public void setPriority(List<Priority> priority) {
        this.priority = priority;
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

    public void addPriority(Priority priority) {
        if (this.priority == null)
            this.priority = new ArrayList<Priority>();

        this.priority.add(priority);
    }
}
