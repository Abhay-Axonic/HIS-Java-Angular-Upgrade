package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.SiteOfAdministrationMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface SiteOfAdministrationDao {
	
    public Response saveSiteOfAdministrationMaster(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException;
	
	public Response updateSiteOfAdministrationMaster(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException;
	
	public Response updateSiteOfAdministrationMasterStatus(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException;
	
	public Response getSiteOfAdministrationMasterById(Integer siteId, Integer organizationId) throws ApplicationException;
	
	public Response getSiteOfAdministrationMasterList(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto, Integer organizationId) throws ApplicationException;
	
	public Response getSiteOfAdministrationMasterCount(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException;
	
	public Response getActiveSiteOfAdministrationMaster(Integer organizationId) throws ApplicationException;
	
	public Response getSiteOfAdministrationMasterByName(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException;
	
	public Response getSiteOfAdministrationMasterByNameNotId(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException;

	public Response getSiteOfAdministrationMasterSearchList(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)throws ApplicationException;

}
