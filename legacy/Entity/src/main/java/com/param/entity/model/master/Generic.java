package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.opd.MedicationHistory;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Generic")
@Table(name = "m_generic", schema = "public")

public class Generic extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(nullable = false, length = 1000)
	private String name;
	
	@Column(name = "is_combination")
	protected Boolean isCombination = false;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id")
	private ProductCategory productCategory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "generic")
	private List<Item> itemsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "generic")
	private List<StorewiseGenericMapper> storewiseGenericList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "generic")
	private List<MedicationHistory> genericList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "generic", cascade = CascadeType.ALL)
	private List<CombinationGeneric> combinationGenericList;


	public Generic() {
		super();
	}

	public Generic(Integer id) {
		super();
		this.id = id;
	}

	public Generic(String code, String name, Boolean status, ProductCategory productCategory) {
		super();
		this.code = code;
		this.name = name;
		this.isActive = status;
		this.productCategory = productCategory;
	}
	
	public Generic(String code, String name, Boolean status, ProductCategory productCategory, Boolean isCombination) {
		super();
		this.code = code;
		this.name = name;
		this.isActive = status;
		this.productCategory = productCategory;
		this.isCombination = isCombination;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductCategory getProductCategory() {
		return this.productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public List<Item> getItemsList() {
		return this.itemsList;
	}

	public void setItemsList(List<Item> itemsList) {
		this.itemsList = itemsList;
	}

	public Item addItemsList(Item itemsList) {
		getItemsList().add(itemsList);
		itemsList.setGeneric(this);

		return itemsList;
	}

	public Item removeItemsList(Item itemsList) {
		getItemsList().remove(itemsList);
		itemsList.setGeneric(null);

		return itemsList;
	}
	
	public CombinationGeneric addCombinationGenericList(CombinationGeneric gombinationGeneric) {
		if (getCombinationGenericList() == null)
			this.combinationGenericList = new ArrayList<CombinationGeneric>();
		
		getCombinationGenericList().add(gombinationGeneric);
		gombinationGeneric.setGeneric(this);

		return gombinationGeneric;
	}

	public List<StorewiseGenericMapper> getStorewiseGenericList() {
		return storewiseGenericList;
	}

	public void setStorewiseGenericList(List<StorewiseGenericMapper> storewiseGenericList) {
		this.storewiseGenericList = storewiseGenericList;
	}

	public List<CombinationGeneric> getCombinationGenericList() {
		return combinationGenericList;
	}

	public void setCombinationGenericList(List<CombinationGeneric> combinationGenericList) {
		this.combinationGenericList = combinationGenericList;
	}

	public Boolean getIsCombination() {
		return isCombination;
	}

	public void setIsCombination(Boolean isCombination) {
		this.isCombination = isCombination;
	}
	
	

}