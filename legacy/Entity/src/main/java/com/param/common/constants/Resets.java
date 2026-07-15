package com.param.common.constants;

public enum Resets {
	Daily(3,"Daily"),
	FinancialYearly(4,"Financial yearly"),
	Monthly(2,"Monthly"),
	Never(5,"Never"),
	Yearly(1,"Yearly");
	
	private Integer id;
	
	private String resets;

	public String getResets() {
		return resets;
	}
	
	public Integer getId() {
		return id;
	}
	
	private Resets(Integer id ,String resets) {
		this.id = id;
		this.resets = resets;
	}
	
	public static String getById(Integer id) {
        for (Resets es : Resets.values()) {
            if (es.id.equals(id)) {
                return es.getResets();
            }
        }
        throw new IllegalArgumentException();
	}
}
