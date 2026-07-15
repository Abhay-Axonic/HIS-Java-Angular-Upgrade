package com.param.pharmacy.master.unit.dto.kit;

import java.util.List;

import com.param.pharmacy.master.global.dto.item.ItemRequest;
import com.param.service.global.model.CookieObjectDto;

public class KitTypeMasterRequest extends CookieObjectDto{
	
	protected Integer id; 
	protected String description;
	protected String code;
	
	protected String quantity;
	



	protected Integer kitTypeId;
	
	protected List<ItemKitMasterRequest> items;
	protected Boolean status;
	
		public KitTypeMasterRequest() {
		super();
		
		}
	
	
		



		public List<ItemKitMasterRequest> getItems() {
			return items;
		}






		public void setItems(List<ItemKitMasterRequest> items) {
			this.items = items;
		}






		public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}

		public Integer getKitTypeId() {
			return kitTypeId;
		}



		public void setKitTypeId(Integer kitTypeId) {
			this.kitTypeId = kitTypeId;
		}
		
		public String getQuantity() {
			return quantity;
		}


		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
}
