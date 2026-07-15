package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class ClinicalHeaderTabDto {
	
			private Integer headerId;
			
			private String headerName;
			
			private char opdCheck;
			
			private char ipdCheck;
			
			private char nurseCheck;
			
			private char status;
			
			private Integer createdBy;
			
			private Integer updatedBy;
			
			private Date createdDate;
			
			private Date updatedDate;
			
			private Integer unitId;
			
			private Integer organizationId;
			
			private List<ClinicalSubtabMapperDto> listSubTabDetails;
			
			private String tabAccessIds;
			
			private String tabAccessIdsIPD;
			
			private String misReportIds;
			
			private Integer userId;

			public Integer getHeaderId() {
				return headerId;
			}

			public void setHeaderId(Integer headerId) {
				this.headerId = headerId;
			}

			public String getHeaderName() {
				return headerName;
			}

			public void setHeaderName(String headerName) {
				this.headerName = headerName;
			}

			public char getOpdCheck() {
				return opdCheck;
			}

			public void setOpdCheck(char opdCheck) {
				this.opdCheck = opdCheck;
			}

			

			public char getIpdCheck() {
				return ipdCheck;
			}

			public void setIpdCheck(char ipdCheck) {
				this.ipdCheck = ipdCheck;
			}

			public char getNurseCheck() {
				return nurseCheck;
			}

			public void setNurseCheck(char nurseCheck) {
				this.nurseCheck = nurseCheck;
			}

			public char getStatus() {
				return status;
			}

			public void setStatus(char status) {
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

			public Date getCreatedDate() {
				return createdDate;
			}

			public void setCreatedDate(Date createdDate) {
				this.createdDate = createdDate;
			}

			public Date getUpdatedDate() {
				return updatedDate;
			}

			public void setUpdatedDate(Date updatedDate) {
				this.updatedDate = updatedDate;
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

			public List<ClinicalSubtabMapperDto> getListSubTabDetails() {
				return listSubTabDetails;
			}

			public void setListSubTabDetails(List<ClinicalSubtabMapperDto> listSubTabDetails) {
				this.listSubTabDetails = listSubTabDetails;
			}

			public String getTabAccessIds() {
				return tabAccessIds;
			}

			public void setTabAccessIds(String tabAccessIds) {
				this.tabAccessIds = tabAccessIds;
			}

			public String getTabAccessIdsIPD() {
				return tabAccessIdsIPD;
			}

			public void setTabAccessIdsIPD(String tabAccessIdsIPD) {
				this.tabAccessIdsIPD = tabAccessIdsIPD;
			}

			public Integer getUserId() {
				return userId;
			}

			public void setUserId(Integer userId) {
				this.userId = userId;
			}

			public String getMisReportIds() {
				return misReportIds;
			}

			public void setMisReportIds(String misReportIds) {
				this.misReportIds = misReportIds;
			}

			
			
			
			}
