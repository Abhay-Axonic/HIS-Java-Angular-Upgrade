package com.param.pharmacy.master.unit.dto.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class PrefixConfigurationDto extends CookieObjectDto{
	
	private Integer id;

	private Integer screenId;
	
	private String screenName;
	
	private Integer resetId;
	
	private String resetName;
	
	private Integer runningNumber;
	
	private Integer length;
	
	private Integer formatId;
	
	private String formatName;
	
	private String code;
	
	private Integer startingMonth;
	
	private Integer duration;
	
	private String generatedCode;
	
	private Boolean flag;
	
	public PrefixConfigurationDto() {
		super();
	}

	public PrefixConfigurationDto(Integer id, Integer screenId, Integer resetId,String resetName, 
			Integer runningNumber, Integer length, String generatedCode) {
		super();
		this.id = id;
		this.screenId = screenId;
		this.resetId = resetId;
		this.resetName = resetName;
		this.runningNumber = runningNumber;
		this.length = length;
		this.generatedCode = generatedCode;
	}
	
	public PrefixConfigurationDto(Integer id, Integer screenId,String screenName, Integer resetId,String resetName, Integer runningNumber, Integer length,
			String generatedCode) {
		super();
		this.id = id;
		this.screenId = screenId;
		this.screenName = screenName;
		this.resetId = resetId;
		this.resetName = resetName;
		this.runningNumber = runningNumber;
		this.length = length;
		this.generatedCode = generatedCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Integer getResetId() {
		return resetId;
	}

	public void setResetId(Integer resetId) {
		this.resetId = resetId;
	}

	public Integer getRunningNumber() {
		return runningNumber;
	}

	public void setRunningNumber(Integer runningNumber) {
		this.runningNumber = runningNumber;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getFormatId() {
		return formatId;
	}

	public void setFormatId(Integer formatId) {
		this.formatId = formatId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStartingMonth() {
		return startingMonth;
	}

	public void setStartingMonth(Integer startingMonth) {
		this.startingMonth = startingMonth;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getGeneratedCode() {
		return generatedCode;
	}

	public void setGeneratedCode(String generatedCode) {
		this.generatedCode = generatedCode;
	}

	public String getResetName() {
		return resetName;
	}

	public void setResetName(String resetName) {
		this.resetName = resetName;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	
	
}
