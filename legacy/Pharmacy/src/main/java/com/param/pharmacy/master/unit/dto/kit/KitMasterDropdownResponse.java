package com.param.pharmacy.master.unit.dto.kit;



import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.KitType;

public class KitMasterDropdownResponse {
	
	
	protected List<KitType> kitType;
	
	





	public KitMasterDropdownResponse() {
		super();
		// TODO Auto-generated constructor stub
	}



	
	

	public List<KitType> getKitType() {
		return kitType;
	}






	public void setKitType(List<KitType> kitType) {
		this.kitType = kitType;
	}






	public void addKitType(KitType kitType) {
		if (this.kitType == null)
			this.kitType = new ArrayList<KitType>();

		this.kitType.add(kitType);
		
	}
	

}
