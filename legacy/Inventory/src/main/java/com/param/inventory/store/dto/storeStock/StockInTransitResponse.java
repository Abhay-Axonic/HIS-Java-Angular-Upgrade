package com.param.inventory.store.dto.storeStock;

import java.math.BigInteger;
import java.util.Date;

public class StockInTransitResponse {
	protected BigInteger id;

	protected Integer currentbal;
	protected Integer transitstock;
	protected String transactionnumber;
	protected Date date;
	protected String screen;

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	 

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Integer getCurrentbal() {
		return currentbal;
	}

	public void setCurrentbal(Integer currentbal) {
		this.currentbal = currentbal;
	}

	public Integer getTransitstock() {
		return transitstock;
	}

	public void setTransitstock(Integer transitstock) {
		this.transitstock = transitstock;
	}

	public String getTransactionnumber() {
		return transactionnumber;
	}

	public void setTransactionnumber(String transactionnumber) {
		this.transactionnumber = transactionnumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
