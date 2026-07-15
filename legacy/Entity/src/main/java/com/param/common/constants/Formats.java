package com.param.common.constants;

public enum Formats {

	DD(6, "DD", "DD"),
	
	MM(5, "MM", "MM"),
	
	YY(2, "YY", "YY"),
	
	YYYY(1, "YYYY", "YYYY"),
	
	MMM(4, "MMM", "MMM"),
	
	MMMM(3, "MMMM", "MMMM"),

	UnitCode(7, "Unit Code", "UC"),

	StoreCode(8, "Store Code", "SC"),

	UserDefine(9, "User Define", "UD"),

	slash(10, "/", "/"),

	hyphen(11, "-", "-"),

	StartFrom(12, "Start From", "SF");
	
	private Integer id;

	private String formats;

	private String code;

	private Formats(Integer id, String formats, String code) {
		this.id = id;
		this.formats = formats;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public String getFormats() {
		return formats;
	}

	public String getCode() {
		return code;
	}

	public static String getById(Integer id) {
		for (Formats es : Formats.values()) {
			if (es.id.equals(id)) {
				return es.getFormats();
			}
		}
		throw new IllegalArgumentException();
	}
}
