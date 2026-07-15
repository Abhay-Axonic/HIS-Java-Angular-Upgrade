package com.param.global.master.model;

import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({

		@NamedQuery(name = "GET_KEY_INGREDIENTS_LIST_BY_NAME", query = "SELECT keyIngredientsMaster.keyIngredientId as keyIngredientId "
				+ "FROM KeyIngredientsMaster keyIngredientsMaster "
				+ "WHERE (LOWER(keyIngredientsMaster.ingredientCode)=:ingredientsName OR keyIngredientsMaster.ingredientCode=:ingredientsName) " //dilip
				+ "AND keyIngredientsMaster.organizationId=:orgId AND keyIngredientsMaster.status='A'"),
		/*
		 * @NamedQuery(name = "GET_KEY_INGREDIENTS_MASTER_BY_ID", query =
		 * "SELECT keyIngredientsMaster.keyIngredientId as keyIngredientId ," +
		 * "keyIngredientsMaster.ingredientCode as ingredientCode ," +
		 * "keyIngredientsMaster.ingredientDescription as ingredientDescription ,"
		 * + "keyIngredientsMaster.ingredientValue as ingredientValue," +
		 * "keyIngredientsMaster.uomId as uomId, " +
		 * "keyIngMas.nutritionMasterId as nutritionId," +
		 * "keyIngMas.nutritionValue as nutritionValue, " +
		 * "keyIngMas.uomId as uomId " +
		 * "FROM KeyIngredientsNutritionMapper keyIngMas " +
		 * "INNER JOIN keyIngMas.keyIngredientsMaster keyIngredientsMaster " +
		 * "WHERE keyIngMas.keyIngredientsMasterId=:keyIngredientsId " +
		 * "AND keyIngMas.organizationId=:orgId"),
		 */
		@NamedQuery(name = "GET_KEY_INGREDIENTS_MASTER_BY_ID", query = "SELECT keyIngMas.keyIngredientId as keyIngredientId ,"
				+ "keyIngMas.ingredientCode as ingredientCode ,"
				+ "keyIngMas.ingredientDescription as ingredientDescription ,"
				+ "keyIngMas.ingredientValue as ingredientValue," + "keyIngMas.uomId as uomId "
				+ "FROM KeyIngredientsMaster keyIngMas " + "WHERE keyIngMas.keyIngredientId=:keyIngredientsId "
				+ "AND keyIngMas.organizationId=:orgId AND keyIngMas.status='A'"),

		@NamedQuery(name = "GET_NUTRITION_LIST_BY_KEYINGREDIENTSMASTER_ID", query = "SELECT keyIngMas.nutritionMasterId as nutritionId,"
				+ "nutritionalMaster.nutritionName AS nutritionName ," + "nutritionalMaster.isCalories AS isCalories ," + "keyIngMas.nutritionValue as value, "
				+ "keyIngMas.uomId as uomId ," + "keyIngMas.ingredientNutritionId as ingredientNutritionId, "
				+ "unitOfMeasurmentMaster.uomDescription as uomName " + "FROM KeyIngredientsNutritionMapper keyIngMas "
				+ "INNER JOIN keyIngMas.nutritionalMaster AS nutritionalMaster "
				+ "INNER JOIN keyIngMas.unitOfMeasurmentMaster as unitOfMeasurmentMaster "
				+ "WHERE keyIngMas.keyIngredientsMasterId=:keyIngredientId AND keyIngMas.status='A' "),

		@NamedQuery(name = "GET_ALL_KEY_INGREDIENTS_MASTER", query = "SELECT keyIngMas.keyIngredientId as keyIngredientId, "
				+ "keyIngMas.ingredientCode as ingredientCode, "
				+ "keyIngMas.ingredientDescription as ingredientDescription, "
				+ "keyIngMas.ingredientValue as ingredientValue, " + "keyIngMas.status as status, "
				+ "unitOfMeasurmentMaster.uomName as uomName, " + "unitOfMeasurmentMaster.uomId as uomId "
				+ "FROM KeyIngredientsMaster keyIngMas "
				+ "INNER JOIN keyIngMas.unitOfMeasurmentMaster as unitOfMeasurmentMaster "
				+ "WHERE keyIngMas.organizationId=:organizationId "), // and keyIngMas.unitId=:unitId"), //dilip
		
		@NamedQuery(name = "GET_ALL_KEY_INGREDIENTS_MASTER_WITHOUT_ORG_ID", query = "SELECT keyIngMas.keyIngredientId as keyIngredientId, "
				+ "keyIngMas.ingredientCode as ingredientCode, "
				+ "keyIngMas.ingredientDescription as ingredientDescription, "
				+ "keyIngMas.ingredientValue as ingredientValue, " + "keyIngMas.status as status, "
				+ "unitOfMeasurmentMaster.uomName as uomName, " + "unitOfMeasurmentMaster.uomId as uomId "
				+ "FROM KeyIngredientsMaster keyIngMas "
				+ "INNER JOIN keyIngMas.unitOfMeasurmentMaster as unitOfMeasurmentMaster "),


		@NamedQuery(name = "GET_ACTIVE_KEY_INGREDIENTS_MASTER", query = "SELECT keyIngMas.keyIngredientId as keyIngredientId, "
				+ "keyIngMas.ingredientCode as ingredientCode, "
				+ "keyIngMas.ingredientDescription as ingredientDescription, "
				+ "keyIngMas.ingredientValue as ingredientValue, " + "keyIngMas.status as status, "
				+ "unitOfMeasurmentMaster.uomName as uomName " + "FROM KeyIngredientsMaster keyIngMas "
				+ "INNER JOIN keyIngMas.unitOfMeasurmentMaster as unitOfMeasurmentMaster "
				+ "WHERE keyIngMas.status='A' and keyIngMas.organizationId=:organizationId "
				+ " ORDER BY keyIngMas.ingredientDescription ASC "), // and keyIngMas.unitId=:unitId") //dilip

})

@Entity
@Table(name = "key_ingredients_master", schema = "kitchen")
@SequenceGenerator(name = "key_ingredients_master_seq", sequenceName = "kitchen.key_ingredients_master_seq", allocationSize = 1)
public class KeyIngredientsMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "key_ingredients_master_seq")

	@Column(name = "key_ingredient_id")
	private Integer keyIngredientId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "ingredient_code")
	private String ingredientCode;

	@Column(name = "ingredient_description")
	private String ingredientDescription;

	@Column(name = "ingredient_value")
	private double ingredientValue;

	@Column(name = "uom_id")
	private Integer uomId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_id", insertable = false, updatable = false)
	UnitOfMeasurmentMaster unitOfMeasurmentMaster;

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "nutritionalMaster",
	// cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<NutritionalMaster> nutritionalMastersList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "keyIngredientsMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<KeyIngredientsMaster> keyIngredientsMaster;

	public Integer getKeyIngredientId() {
		return keyIngredientId;
	}

	public String getIngredientCode() {
		return ingredientCode;
	}

	public void setIngredientCode(String ingredientCode) {
		this.ingredientCode = ingredientCode;
	}

	public String getIngredientDescription() {
		return ingredientDescription;
	}

	public void setIngredientDescription(String ingredientDescription) {
		this.ingredientDescription = ingredientDescription;
	}

	public double getIngredientValue() {
		return ingredientValue;
	}

	public void setIngredientValue(double ingredientValue) {
		this.ingredientValue = ingredientValue;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public void setKeyIngredientId(Integer keyIngredientId) {
		this.keyIngredientId = keyIngredientId;
	}

	
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	

}
