package com.param.pharmacy.op.dto.salesWorkList;

public class DiscountCategoryResponse {

	private Integer id;
	
	private String discountCategory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(String discountCategory) {
		this.discountCategory = discountCategory;
	}
	public DiscountCategoryResponse(Integer id, String discountCategory) {
		super();
		this.id = id;
		this.discountCategory = discountCategory;
	}
}
