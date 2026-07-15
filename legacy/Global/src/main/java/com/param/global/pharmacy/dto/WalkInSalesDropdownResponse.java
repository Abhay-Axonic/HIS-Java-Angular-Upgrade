package com.param.global.pharmacy.dto;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.DiscountCategory;
import com.param.global.dto.global.Gender;
import com.param.global.dto.global.Prefix;

public class WalkInSalesDropdownResponse {
	protected List<Prefix> prefix;

	protected List<Gender> gender;

	protected List<DiscountCategory> discountCategory;

	public WalkInSalesDropdownResponse() {
		super();
	}

	public List<Prefix> getPrefix() {
		return prefix;
	}

	public void setPrefix(List<Prefix> prefix) {
		this.prefix = prefix;
	}

	public List<Gender> getGender() {
		return gender;
	}

	public void setGender(List<Gender> gender) {
		this.gender = gender;
	}

	public List<DiscountCategory> getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(List<DiscountCategory> discountCategory) {
		this.discountCategory = discountCategory;
	}
	
	public void addPrefix(Prefix prefix) {
		if (this.prefix == null)
			this.prefix = new ArrayList<Prefix>();

		this.prefix.add(prefix);
	}
	
	public void addGender(Gender gender) {
		if (this.gender == null)
			this.gender = new ArrayList<Gender>();

		this.gender.add(gender);
	}
	
	public void addDiscountCategory(DiscountCategory discountCategory) {
		if (this.discountCategory == null)
			this.discountCategory = new ArrayList<DiscountCategory>();

		this.discountCategory.add(discountCategory);
	}
}
