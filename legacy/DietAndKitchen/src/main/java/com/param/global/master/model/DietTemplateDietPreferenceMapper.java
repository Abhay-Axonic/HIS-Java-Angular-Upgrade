package com.param.global.master.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.MealPreferenceMaster;

@Entity
@Table(name = "m_diet_template_diet_preference_mapper", schema = "kitchen")
@SequenceGenerator(name = "m_diet_template_diet_preferen_diet_template_diet_preference_seq", sequenceName = "kitchen.m_diet_template_diet_preferen_diet_template_diet_preference_seq", allocationSize = 1)
public class DietTemplateDietPreferenceMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_diet_template_diet_preferen_diet_template_diet_preference_seq")
	@Column(name = "diet_template_diet_preference_mapper_id")
	private Integer dietTemplateDietPrefId;

	@Column(name = "diet_template_id")
	private Integer dietTemplateId;

	@Column(name = "diet_preference_id")
	private Integer dietPrefId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_preference_id", insertable = false, updatable = false)
	private MealPreferenceMaster mealPreferenceMaster;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "diet_template_id", insertable = false, updatable = false)
	private DietTemplate dietTemplate;

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

	public MealPreferenceMaster getMealPreferenceMaster() {
		return mealPreferenceMaster;
	}

	public void setMealPreferenceMaster(MealPreferenceMaster mealPreferenceMaster) {
		this.mealPreferenceMaster = mealPreferenceMaster;
	}

	public DietTemplate getDietTemplate() {
		return dietTemplate;
	}

	public void setDietTemplate(DietTemplate dietTemplate) {
		this.dietTemplate = dietTemplate;
	}

}
