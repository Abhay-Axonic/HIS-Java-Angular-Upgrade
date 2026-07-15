package com.param.pharmacy.master.global.dto.item;

import java.util.Date;

public class ItemRabbitDto {

	private Integer id;
	
	private String code;
	
	private String hsn_code;
	
	private Boolean is_batch_required;
	
	private Boolean is_combination;
	
	private Boolean is_high_risk_product;
	
	private Boolean is_infusion;
	
	private Boolean is_kit;
	
	private Boolean is_narcotic;
	
	private Boolean is_otc;
	
	private Boolean is_psychotropic;
	
	private Boolean is_reusable;
	
	private Boolean is_vaccin;
	
	private Boolean is_consignment;
	
	private Boolean is_consumable;
	
	private Boolean is_hospital_formulary;
	
	private String name;
	
	private String product_code;
	
	private Integer storage_unit_id;
	
	private Double strength;
	
	private Integer formulation_type_id;
	
	private Integer generic_id;
	
	private String generic_name;
	
	private Integer manufacturer_id;
	
	private String manufacturer_name;
	
	private Integer product_category_id;
	
	private Integer strength_unit_id;
	
	private Integer pharmacological_classification_id;
	
	private Integer p_tax_id;
	
	private Integer s_tax_id;
	
	private Integer organization_id;
	
	private Integer unit_id;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private Integer added_by;
	
	private Date added_date;
	
	private Integer updated_by;
	
	private Date updated_date;
	
	private Integer route_of_administration_id;
	
	
	public ItemRabbitDto() {
		super();
	}

	public ItemRabbitDto(Integer id, String code, String hsn_code, Boolean is_batch_required, Boolean is_combination,
			Boolean is_high_risk_product, Boolean is_infusion, Boolean is_kit, Boolean is_narcotic, Boolean is_otc,
			Boolean is_psychotropic, Boolean is_reusable, Boolean is_vaccin, Boolean is_consignment,
			Boolean is_consumable, Boolean is_hospital_formulary, String name, String product_code,
			Integer storage_unit_id, Double strength, Integer formulation_type_id, Integer generic_id,
			String generic_name, Integer manufacturer_id, String manufacturer_name,
			Integer strength_unit_id, Integer pharmacological_classification_id, Integer p_tax_id, Integer s_tax_id,
			Integer organization_id, Integer unit_id, Boolean is_active, Boolean is_deleted, Integer added_by,
			Date added_date, Integer updated_by, Date updated_date) {
		super();
		this.id = id;
		this.code = code;
		this.hsn_code = hsn_code;
		this.is_batch_required = is_batch_required;
		this.is_combination = is_combination;
		this.is_high_risk_product = is_high_risk_product;
		this.is_infusion = is_infusion;
		this.is_kit = is_kit;
		this.is_narcotic = is_narcotic;
		this.is_otc = is_otc;
		this.is_psychotropic = is_psychotropic;
		this.is_reusable = is_reusable;
		this.is_vaccin = is_vaccin;
		this.is_consignment = is_consignment;
		this.is_consumable = is_consumable;
		this.is_hospital_formulary = is_hospital_formulary;
		this.name = name;
		this.product_code = product_code;
		this.storage_unit_id = storage_unit_id;
		this.strength = strength;
		this.formulation_type_id = formulation_type_id;
		this.generic_id = generic_id;
		this.generic_name = generic_name;
		this.manufacturer_id = manufacturer_id;
		this.manufacturer_name = manufacturer_name;
		this.strength_unit_id = strength_unit_id;
		this.pharmacological_classification_id = pharmacological_classification_id;
		this.p_tax_id = p_tax_id;
		this.s_tax_id = s_tax_id;
		this.organization_id = organization_id;
		this.unit_id = unit_id;
		this.is_active = is_active;
		this.is_deleted = is_deleted;
		this.added_by = added_by;
		this.added_date = added_date;
		this.updated_by = updated_by;
		this.updated_date = updated_date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHsn_code() {
		return hsn_code;
	}

	public void setHsn_code(String hsn_code) {
		this.hsn_code = hsn_code;
	}

	public Boolean getIs_batch_required() {
		return is_batch_required;
	}

	public void setIs_batch_required(Boolean is_batch_required) {
		this.is_batch_required = is_batch_required;
	}

	public Boolean getIs_combination() {
		return is_combination;
	}

	public void setIs_combination(Boolean is_combination) {
		this.is_combination = is_combination;
	}

	public Boolean getIs_high_risk_product() {
		return is_high_risk_product;
	}

	public void setIs_high_risk_product(Boolean is_high_risk_product) {
		this.is_high_risk_product = is_high_risk_product;
	}

	public Boolean getIs_infusion() {
		return is_infusion;
	}

	public void setIs_infusion(Boolean is_infusion) {
		this.is_infusion = is_infusion;
	}

	public Boolean getIs_kit() {
		return is_kit;
	}

	public void setIs_kit(Boolean is_kit) {
		this.is_kit = is_kit;
	}

	public Boolean getIs_narcotic() {
		return is_narcotic;
	}

	public void setIs_narcotic(Boolean is_narcotic) {
		this.is_narcotic = is_narcotic;
	}

	public Boolean getIs_otc() {
		return is_otc;
	}

	public void setIs_otc(Boolean is_otc) {
		this.is_otc = is_otc;
	}

	public Boolean getIs_psychotropic() {
		return is_psychotropic;
	}

	public void setIs_psychotropic(Boolean is_psychotropic) {
		this.is_psychotropic = is_psychotropic;
	}

	public Boolean getIs_reusable() {
		return is_reusable;
	}

	public void setIs_reusable(Boolean is_reusable) {
		this.is_reusable = is_reusable;
	}

	public Boolean getIs_vaccin() {
		return is_vaccin;
	}

	public void setIs_vaccin(Boolean is_vaccin) {
		this.is_vaccin = is_vaccin;
	}

	public Boolean getIs_consignment() {
		return is_consignment;
	}

	public void setIs_consignment(Boolean is_consignment) {
		this.is_consignment = is_consignment;
	}

	public Boolean getIs_consumable() {
		return is_consumable;
	}

	public void setIs_consumable(Boolean is_consumable) {
		this.is_consumable = is_consumable;
	}

	public Boolean getIs_hospital_formulary() {
		return is_hospital_formulary;
	}

	public void setIs_hospital_formulary(Boolean is_hospital_formulary) {
		this.is_hospital_formulary = is_hospital_formulary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public Integer getStorage_unit_id() {
		return storage_unit_id;
	}

	public void setStorage_unit_id(Integer storage_unit_id) {
		this.storage_unit_id = storage_unit_id;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Integer getFormulation_type_id() {
		return formulation_type_id;
	}

	public void setFormulation_type_id(Integer formulation_type_id) {
		this.formulation_type_id = formulation_type_id;
	}

	public Integer getGeneric_id() {
		return generic_id;
	}

	public void setGeneric_id(Integer generic_id) {
		this.generic_id = generic_id;
	}

	public Integer getManufacturer_id() {
		return manufacturer_id;
	}

	public void setManufacturer_id(Integer manufacturer_id) {
		this.manufacturer_id = manufacturer_id;
	}
	
	public String getManufacturer_name() {
		return manufacturer_name;
	}

	public void setManufacturer_name(String manufacturer_name) {
		this.manufacturer_name = manufacturer_name;
	}

	public Integer getProduct_category_id() {
		return product_category_id;
	}

	public void setProduct_category_id(Integer product_category_id) {
		this.product_category_id = product_category_id;
	}

	public Integer getStrength_unit_id() {
		return strength_unit_id;
	}

	public void setStrength_unit_id(Integer strength_unit_id) {
		this.strength_unit_id = strength_unit_id;
	}

	public Integer getPharmacological_classification_id() {
		return pharmacological_classification_id;
	}

	public void setPharmacological_classification_id(Integer pharmacological_classification_id) {
		this.pharmacological_classification_id = pharmacological_classification_id;
	}

	public Integer getP_tax_id() {
		return p_tax_id;
	}

	public void setP_tax_id(Integer p_tax_id) {
		this.p_tax_id = p_tax_id;
	}

	public Integer getS_tax_id() {
		return s_tax_id;
	}

	public void setS_tax_id(Integer s_tax_id) {
		this.s_tax_id = s_tax_id;
	}

	public Integer getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}

	public Integer getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public Integer getAdded_by() {
		return added_by;
	}

	public void setAdded_by(Integer added_by) {
		this.added_by = added_by;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public String getGeneric_name() {
		return generic_name;
	}

	public void setGeneric_name(String generic_name) {
		this.generic_name = generic_name;
	}

	public Integer getRoute_of_administration_id() {
		return route_of_administration_id;
	}

	public void setRoute_of_administration_id(Integer route_of_administration_id) {
		this.route_of_administration_id = route_of_administration_id;
	}
}
