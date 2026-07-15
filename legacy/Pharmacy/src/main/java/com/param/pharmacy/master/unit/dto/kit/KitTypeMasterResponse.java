package com.param.pharmacy.master.unit.dto.kit;

import java.util.ArrayList;
import java.util.List;

import com.param.entity.model.master.KitType;

public class KitTypeMasterResponse {

	protected Integer id;
	protected String description;
	protected String code;
	protected Boolean status;
	
	protected List<KitType> kitType;
	
	
	public void addKitType(KitType kitType) {
		if (this.kitType == null)
			this.kitType = new ArrayList<KitType>();

		this.kitType.add(kitType);
		
	}

	public List<KitType> getKitType() {
		return kitType;
	}

	public void setKitType(List<KitType> kitType) {
		this.kitType = kitType;
	}

	public KitTypeMasterResponse() {
		super();
		
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

	public KitTypeMasterResponse(Integer id, String code, String description) {
		this.id = id;
		this.code = code;
		this.description = description;
	}

	public KitTypeMasterResponse(Integer id, String code, String description, Boolean status) {
		this.id = id;
		this.code = code;
		this.description = description;
		this.status=status;
		
	}

	public KitTypeMasterResponse(Integer id, String description) {
		super();
		this.id = id;
		this.description = description;
	}
	
	
}
