package com.param.pharmacy.master.unit.dto.configuration;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PrefixConfigurationDropdown {

	private List<ScreenDto> screen;
	
	private List<ResetDto> reset;
	
	private List<FormatsDto> format;
	
	public List<ScreenDto> getScreen() {
		return screen;
	}

	public void setScreen(List<ScreenDto> screen) {
		this.screen = screen;
	}
	
	public List<ResetDto> getReset() {
		return reset;
	}

	public void setReset(List<ResetDto> reset) {
		this.reset = reset;
	}

	public List<FormatsDto> getFormat() {
		return format;
	}

	public void setFormat(List<FormatsDto> format) {
		this.format = format;
	}

	public void addScreen(ScreenDto screen) {
		if (this.screen == null)
			this.screen = new ArrayList<ScreenDto>();
		this.screen.add(screen);
	}

}
