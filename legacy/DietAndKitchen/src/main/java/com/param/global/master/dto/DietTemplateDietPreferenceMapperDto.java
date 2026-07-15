package com.param.global.master.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DietTemplateDietPreferenceMapperDto {

	private Integer dietTemplateDietPrefId;

	private Integer dietTemplateId;

	private Integer dietPrefId;

	private String dietPreferenceDesc;

	public Integer getDietTemplateDietPrefId() {
		return dietTemplateDietPrefId;
	}

	public void setDietTemplateDietPrefId(Integer dietTemplateDietPrefId) {
		this.dietTemplateDietPrefId = dietTemplateDietPrefId;
	}

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public Integer getDietPrefId() {
		return dietPrefId;
	}

	public void setDietPrefId(Integer dietPrefId) {
		this.dietPrefId = dietPrefId;
	}

	public String getDietPreferenceDesc() {
		return dietPreferenceDesc;
	}

	public void setDietPreferenceDesc(String dietPreferenceDesc) {
		this.dietPreferenceDesc = dietPreferenceDesc;
	}

}
