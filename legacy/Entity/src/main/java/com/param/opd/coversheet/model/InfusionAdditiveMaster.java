package com.param.opd.coversheet.model;

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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.StrengthUnit;

/*@NamedQuery(name="GET_ADDITIVE_LIST_BY_ID" ,
query="SELECT "
		+ " infusionAdditiveMaster.infusionadditiveId as infusionadditiveId , "
//		+ " infusionAdditiveMaster.solutionVolume as solutionVolume , "
		+ " infusionAdditiveMaster.additiveName as additiveName, "
		+ " infusionAdditiveMaster.itemId as itemId, "
		+ " strengthUnitMaster.units as additiveUnit,"
		+ " infusionAdditiveMaster.volume as volume "
		+ " FROM InfusionAdditiveMaster infusionAdditiveMaster "
		+ " INNER JOIN infusionAdditiveMaster.strengthUnitMaster strengthUnitMaster"
		+ " WHERE  infusionAdditiveMaster.organizationId= :organizationId "
		+ " AND infusionAdditiveMaster.unitId= :unitId"
		+ " AND infusionAdditiveMaster.infusionSolutionId= :infusionSolutionId"
		)*/

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_ADDITIVE_LIST_BY_ID",
			query=" select " + 
					"	infusionad0_.infusion_additive_id as \"infusionadditiveId\", " + 
					"	infusionad0_.item_id as \"itemId\", " + 
					"	infusionad0_.additive_name as \"additiveName\", " + 
					"	strengthun1_.unit as \"additiveUnit\", " + 
					"	infusionad0_.volume as \"volume\", " + 
					"	predtl.id as \"id\" " + 
					" from " + 
					"	opd.m_infusion_additive infusionad0_  " + 
					" inner join public.m_strength_unit strengthun1_ on " + 
					"	infusionad0_.strength_unit_id = strengthun1_.id " + 
					" inner join pharmacy.t_prescription_detail predtl on " + 
					"    predtl.infusion_solution_id = infusionad0_.infusion_solution_id " + 
					"    and predtl.item_id = infusionad0_.item_id " + 
					" where " + 
					"	infusionad0_.organization_id =:organizationId " + 
					"	and infusionad0_.unit_id =:unitId " + 
					"	and infusionad0_.infusion_solution_id =:infusionSolutionId "),
	
})

@Entity
@Table(name = "m_infusion_additive", schema = "opd")
@SequenceGenerator(name = "m_infusion_additive_seq", sequenceName = "opd.m_infusion_additive_seq", allocationSize = 1)

public class InfusionAdditiveMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_infusion_additive_seq")
	
	@Column(name = "infusion_additive_id")
	private Integer infusionadditiveId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "generic_id")
	private Integer genericId;
	
	@Column(name = "strength_unit_id")
	private Integer strengthUnitId;
	
	@Column(name = "roa_id")
	private Integer roaId;
	
	@Column(name = "volume")
	private Double volume;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "infusion_solution_id")
	private Integer infusionSolutionId;
	
	@Column(name = "additive_name")
	private String additiveName;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "infusionAdditiveMaster") 
	private List<InfusionSolutionMaster> listInfusionSolutionMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="strength_unit_id", insertable=false, updatable=false)
	private StrengthUnit strengthUnitMaster ;
	
	public Integer getInfusionadditiveId() {
		return infusionadditiveId;
	}

	public void setInfusionadditiveId(Integer infusionadditiveId) {
		this.infusionadditiveId = infusionadditiveId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

	public String getAdditiveName() {
		return additiveName;
	}

	public void setAdditiveName(String additiveName) {
		this.additiveName = additiveName;
	}

	public List<InfusionSolutionMaster> getListInfusionSolutionMaster() {
		return listInfusionSolutionMaster;
	}

	public void setListInfusionSolutionMaster(List<InfusionSolutionMaster> listInfusionSolutionMaster) {
		this.listInfusionSolutionMaster = listInfusionSolutionMaster;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	

}
