package com.param.pharmacy.master.unit.dto.rack;

import com.param.service.global.model.CookieObjectDto;

public class ShelfRequest extends CookieObjectDto{

	protected Integer shelfId;
	
	protected String shelfCode;

	protected String shelfName;
	

	public ShelfRequest() {
		super();
	}

	public ShelfRequest(Integer shelfId,String shelfCode, String shelfName) {
		super();
		this.shelfId=shelfId;
		this.shelfCode = shelfCode;
		this.shelfName = shelfName;
		
	}
	

	public Integer getShelfId() {
		return shelfId;
	}

	public void setShelfId(Integer shelfId) {
		this.shelfId = shelfId;
	}

	public String getShelfCode() {
		return shelfCode;
	}

	public void setShelfCode(String shelfCode) {
		this.shelfCode = shelfCode;
	}

	public String getShelfName() {
		return shelfName;
	}

	public void setShelfName(String shelfName) {
		this.shelfName = shelfName;
	}

}
