package com.param.adt.master.global.dto;

import java.math.BigInteger;

public class BedCategoryAvailibilityDto {
	private BigInteger vacant;
	private BigInteger reserved;
	private String wardDesc;
	private BigInteger occupied;
	private BigInteger housekeeping;
	public BigInteger getVacant() {
		return vacant;
	}
	public void setVacant(BigInteger vacant) {
		this.vacant = vacant;
	}
	public BigInteger getReserved() {
		return reserved;
	}
	public void setReserved(BigInteger reserved) {
		this.reserved = reserved;
	}
	public String getWardDesc() {
		return wardDesc;
	}
	public void setWardDesc(String wardDesc) {
		this.wardDesc = wardDesc;
	}
	public BigInteger getOccupied() {
		return occupied;
	}
	public void setOccupied(BigInteger occupied) {
		this.occupied = occupied;
	}
	public BigInteger getHousekeeping() {
		return housekeeping;
	}
	public void setHousekeeping(BigInteger housekeeping) {
		this.housekeeping = housekeeping;
	}
	
}
