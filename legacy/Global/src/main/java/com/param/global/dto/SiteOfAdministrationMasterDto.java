package com.param.global.dto;

import java.util.Date;

import javax.persistence.Column;

public class SiteOfAdministrationMasterDto {
	
	    private Integer siteId;
	
		private String siteCode;
	
	    private String siteName;
	
		private Integer organizationId;
	
		private Character status;
	
		private Integer createdBy;
		
	    private String createdDate;
	    
	    private String updatedDate;
	
		private Integer updatedBy;
		

	    private Integer offset;
		
		private Integer noOfRecordsPerPage;
	
		public Integer getSiteId() {
			return siteId;
		}

		public void setSiteId(Integer siteId) {
			this.siteId = siteId;
		}

		public String getSiteCode() {
			return siteCode;
		}

		public void setSiteCode(String siteCode) {
			this.siteCode = siteCode;
		}

		public String getSiteName() {
			return siteName;
		}

		public void setSiteName(String siteName) {
			this.siteName = siteName;
		}

		public Integer getOrganizationId() {
			return organizationId;
		}

		public void setOrganizationId(Integer organizationId) {
			this.organizationId = organizationId;
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

	

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

		

		public Integer getOffset() {
			return offset;
		}

		public void setOffset(Integer offset) {
			this.offset = offset;
		}

		public Integer getNoOfRecordsPerPage() {
			return noOfRecordsPerPage;
		}

		public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
			this.noOfRecordsPerPage = noOfRecordsPerPage;
		}

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}

		public String getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}

	
	
}
