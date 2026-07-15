package com.param.pharmacy.master.unit.dto.configuration;

public class ScreenDto {

	protected Integer id;

	protected String screen;
	
	protected Boolean isGlobal;
	
	public ScreenDto(Integer integer, String string) {
		super();
	}

	public ScreenDto(Integer id, String screen ,Boolean isGlobal) {
		super();
		this.id = id;
		this.screen = screen;
		this.isGlobal = isGlobal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public Boolean getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(Boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	
}
