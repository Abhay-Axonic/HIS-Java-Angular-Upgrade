package com.param.entity.model.base;


import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.param.entity.model.master.Organization;
import com.param.entity.model.master.Unit;
import com.param.utility.TimeZoneComponent;

@MappedSuperclass
public abstract class BaseEntity extends CommonEntity {

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	protected Organization organization;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id",insertable = false, updatable = false)
	protected Unit unit;

	/*public BaseEntity() {
		this.setDefaults();
	}*/

	/*public void setDefaults(){
		SessionService sessionService = getSessionService();
		if(sessionService != null && sessionService.getCookieObjectDto() != null){
			Integer userId = sessionService.getCookieObjectDto().getUserId();
			this.organization.setOrganizationId(sessionService.getCookieObjectDto().getOrganizationId());
			this.unit.setUnitId(sessionService.getCookieObjectDto().getUnitId());
			this.addedBy.setUserId(userId);
			this.addedDate = TimeZoneComponent.getDateByzone(unitId);
			this.updatedBy.setUserId(userId);
			this.updatedDate = TimeZoneComponent.getDateByzone(unitId);
		}
	}*/

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setUpdateDefaults(Integer userId , Integer unitId) {
		//SessionService sessionService = getSessionService();
		this.updatedBy.setUpdatedBy(userId);
		this.updatedDate = TimeZoneComponent.getDateByzone(this.unitId);
		/*if(sessionService != null){
			Integer userId = sessionService.getCookieObjectDto().getUserId();
			this.updatedBy.setUpdatedBy(userId);
			this.updatedDate = TimeZoneComponent.getDateByzone(unitId);
		}*/
	}

	/*private SessionService getSessionService() {
		return SpringContext.getBean(SessionService.class);
	}*/

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
	
	public void setCreateDefaults(Integer orgId, Integer unitId,Integer userId) {
		this.unitId = unitId;
		this.organizationId = orgId;
		this.addedByUser = userId;
		this.updatedByUser = userId;
		// time zone changes
		super.addedDate=TimeZoneComponent.getDateByzone(unitId);
		super.updatedDate=TimeZoneComponent.getDateByzone(unitId);
	}
	
}