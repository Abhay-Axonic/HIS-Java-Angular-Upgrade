package com.param.entity.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.param.entity.model.master.User;
import com.param.service.global.SessionService;
import com.param.service.global.SpringContext;
import com.param.utility.TimeZoneComponent;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public abstract class CommonEntity {

	
	@Column(name = "added_by")
	protected Integer addedByUser;
	
	@Column(name = "updated_by")
	protected Integer updatedByUser;
	
	@Column(name = "is_active")
	@ApiModelProperty(value = "isActive")
	protected Boolean isActive = true;

	@Column(name = "is_deleted")
	@ApiModelProperty(value = "IsDeleted")
	protected Boolean isDeleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "added_by",insertable = false, updatable = false )
	@ApiModelProperty(value = "Added By")
	protected User addedBy;

	@Column(name = "added_date")
	@ApiModelProperty(value = "Added Date")
	protected Date addedDate = TimeZoneComponent.getDateByzone(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by",insertable = false, updatable = false)
	@ApiModelProperty(value = "Updated By")
	protected User updatedBy;

	@Column(name = "updated_date")
	@ApiModelProperty(value = "Update Date")
	protected Date updatedDate = TimeZoneComponent.getDateByzone(0);

	public CommonEntity() {
	}

	private SessionService getSessionService() {
		return SpringContext.getBean(SessionService.class);
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getAddedBy() {
		return addedBy;
	}

	/*public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}*/

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	/*public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}*/

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/*public User getUser() {
		SessionService sessionService = getSessionService();

		if(sessionService != null){
			Integer userId = sessionService.getCookieObjectDto().getUserId();
			User user = new User(userId);
			return user;
		}

		return null;
	}*/

	public Integer getAddedByUser() {
		return addedByUser;
	}

	public void setAddedByUser(Integer addedByUser) {
		this.addedByUser = addedByUser;
		this.updatedByUser = addedByUser;
	}

	public Integer getUpdatedByUser() {
		return updatedByUser;
	}

	public void setUpdatedByUser(Integer updatedByUser) {
		this.updatedByUser = updatedByUser;
	}
	
}
