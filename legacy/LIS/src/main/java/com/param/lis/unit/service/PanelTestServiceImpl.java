package com.param.lis.unit.service;

import java.math.BigInteger;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.dao.IUnitServiceMasterDetailsDao;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.IPanelTestDao;
import com.param.lis.unit.dto.TPanelMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" ,"unused"})
public class PanelTestServiceImpl  implements IPanelTestService, ICommonConstants, IError{
  
  @Autowired
  IPanelTestDao iPanelTestDao;

  @Autowired
  IUnitServiceMasterDetailsDao iUnitServiceMasterDetailsDao;
  
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  @Transactional
  public Response addPanel(TPanelMasterDto tpanelTestMpprMasterDto)
      throws ApplicationException {
   /* try
    {
        return iPanelTestDao.addPanel(tpanelTestMpprMasterDto);
    } catch (Exception e)
    {
        
        throw e;
    }*/
	  
	  try
		{
		  BigInteger tpanelTestExists = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_PANEL_TEST_EXISTS")
					.setParameter("serviceId", tpanelTestMpprMasterDto.getServiceId())
					.setParameter("orgId", tpanelTestMpprMasterDto.getOrgId())
					.setParameter("orgUnitId", tpanelTestMpprMasterDto.getOrgUnitId())
					.uniqueResult();
		  
		  if (tpanelTestExists.compareTo(BigInteger.ZERO) == 1)
			{
			  return new Response(ERROR, ERR_500, PANEL_TEST_EXISIS, null, null);
			} else{
				BigInteger tpanelTestMpprAliasCodeExists = (BigInteger) sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_PANEL_ALIAS_CODE_BY_PANEL_ID")
						.setParameter("panelAlies", tpanelTestMpprMasterDto.getPanelAlies().trim().toLowerCase())
						.setParameter("orgId", tpanelTestMpprMasterDto.getOrgId())// labTemplateMasterDto.getListSpecialtyTemplateMapperDto().get(0).getSpecialityId()
						.setParameter("orgUnitId", tpanelTestMpprMasterDto.getOrgUnitId())
			  			.uniqueResult();
				
				 BigInteger tpanelTestMpprAliasNameExists = (BigInteger) sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_PANEL_ALIAS_NAME_BY_PANEL_ID")
							.setParameter("panelCode", tpanelTestMpprMasterDto.getPanelCode().trim().toLowerCase())
							.setParameter("orgId", tpanelTestMpprMasterDto.getOrgId())// labTemplateMasterDto.getListSpecialtyTemplateMapperDto().get(0).getSpecialityId()
							.setParameter("orgUnitId", tpanelTestMpprMasterDto.getOrgUnitId())
				  			.uniqueResult();
				
				 if(tpanelTestMpprAliasCodeExists.compareTo(BigInteger.ZERO) == 1){
					  return new Response(ERROR, ERR_500, PANEL_ALIAS_CODE_EXISIS, null, null); 
				}else if(tpanelTestMpprAliasNameExists.compareTo(BigInteger.ZERO) == 1){
					return new Response(ERROR, ERR_500, PANEL_ALIAS_NAME_EXISIS, null, null);
				}else {
					tpanelTestMpprMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(tpanelTestMpprMasterDto.getOrgUnitId()).getTime());
					Response response = iPanelTestDao.addPanel(tpanelTestMpprMasterDto);
					return response;
				}
				
			} 
		 /* if (tpanelTestMpprAliasCodeExists.compareTo(BigInteger.ZERO) == 1){
			  return new Response(ERROR, ERR_500, PANEL_ALIAS_CODE_EXISIS, null, null); 
		  }else if (tpanelTestMpprAliasNameExists.compareTo(BigInteger.ZERO) == 0) {
			 
			  if (tpanelTestExists.compareTo(BigInteger.ZERO) == 0)
				{
					tpanelTestMpprMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
					Response response = iPanelTestDao.addPanel(tpanelTestMpprMasterDto);
					return response;
				} else
				{
					return new Response(ERROR, ERR_500, PANEL_TEST_EXISIS, null, null);
				}
		  } else
		  {
				return new Response(ERROR, ERR_500, PANEL_ALIAS_NAME_EXISIS, null, null);
		  }
		  
		  */
		  
			/*if (tpanelTestMpprMstDto == null)
			{
				tpanelTestMpprMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
				Response response = iPanelTestDao.addPanel(tpanelTestMpprMasterDto);
				return response;
			} else
			{
				return new Response(ERROR, ERR_500, PANEL_CODE_EXISIS, null, null);
			}*/

		} catch (Exception e)
		{
			
			throw e;
		}
  }

  @Override
  @Transactional
  public Response getPanelById(Integer panelId, Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try
    {
        return iPanelTestDao.getPanelById(panelId,orgId,orgUnitId);
    } catch (Exception e)
    {
        
        throw e;
    }
  }

  @Override
  @Transactional
  public Response updatePanel(TPanelMasterDto tpanelTestMpprMasterDto) throws ApplicationException {
    try
    {
        return iPanelTestDao.updatePanel(tpanelTestMpprMasterDto);
    } catch (Exception e)
    {
        
        throw e;
    }
  }

  @Override
  @Transactional
  public Response activeInactivePanelStatus(Integer panelId, Integer serviceId ,Integer orgId, Integer orgUnitid,
      Character status) throws ApplicationException {
    try
    {
		com.param.global.common.Response response = null;
        Response res = iPanelTestDao.activeInactivePanelStatus(panelId,orgId,orgUnitid,status);
        if(res.getStatus().equals(SUCCESS)) {
        	  response = iUnitServiceMasterDetailsDao.updateUnitServiceMasterDetailsStatus(serviceId,orgId,orgUnitid,status);	
        	  return res;
        }else {
        	return res;
        }
    } catch (Exception e)
    {
        
        throw e;
    }
  }

  @Override
  @Transactional
  public Response getPanelList(Integer orgId, Integer orgUnitId, Integer offset,
      Integer recordPerPage) throws ApplicationException {
    try
    {
        return iPanelTestDao.getPanelList(orgId,orgUnitId,offset,recordPerPage);
    } catch (Exception e)
    {
        
        throw e;
    }
  }

  @Override
  @Transactional
  public Response getPanelListCount(Integer orgId, Integer orgUnitId) throws ApplicationException {
    try
    {
        return iPanelTestDao.getPanelListCount(orgId,orgUnitId);
    } catch (Exception e)
    {
        
        throw e;
    }
  }

@Override
@Transactional
public Response getUnitServiceMapperList(Integer orgId, Integer orgUnitId) throws ApplicationException {
	 try
	    {
	        return iPanelTestDao.getUnitServiceMapperList(orgId,orgUnitId);
	    } catch (Exception e)
	    {
	        
	        throw e;
	    }
}

@Override
@Transactional
public Response getPanelDescByPanelId(Integer panelId, Integer orgId, Integer orgUnitId)
    throws ApplicationException {
  try
  {
      return iPanelTestDao.getPanelDescByPanelId(panelId,orgId,orgUnitId);
  } catch (Exception e)
  {
      
      throw e;
  }
}

}
