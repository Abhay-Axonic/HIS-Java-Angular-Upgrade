package com.param.procurement.common.constant;

public enum StatusEnum {
	
	APPROVED(1, "Approved"),   
	CANCELED(2, "Canceled"),
	PARTIAL_APPROVED(3, "Partial Approved"),
	PENDING(4, "Pending");
	
	private Integer code;
	private String description;
	
	private StatusEnum(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}

	 public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}
	public String getDescription(Integer code) {
		StatusEnum desc = findByValue(code); 
		String descrip=null;
		if(desc !=null) {
		 descrip= desc.getDescription();
		}
		return descrip;
	}

	public  void setDescription(String description) {
		this.description = description;
	}
	
	public static StatusEnum findByValue(Integer val) {
		for (StatusEnum value : values()) {
			if (value.getCode() == val) {
				return value;
			}
		}
		return null;
	}

}
