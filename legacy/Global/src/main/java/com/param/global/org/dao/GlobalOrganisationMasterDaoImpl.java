package com.param.global.org.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.sync.OrganisationMasterDtoSync;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.OrganizationUnitLicenceDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GlobalOrganisationMasterDaoImpl extends GenericDao<OrganizationMaster, Integer> implements IGlobalOrganisationMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(GlobalOrganisationMasterDaoImpl.class);

	public GlobalOrganisationMasterDaoImpl() {
		super(OrganizationMaster.class);
	}

	@Override
	public Response getGlobalOrganisationMasterForSyncById(Integer organisationId) throws ApplicationException {
		try {
			OrganisationMasterDtoSync dto = (OrganisationMasterDtoSync)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORGANISATION_MASTER_RECORD_FORSYNC_BY_ID")
					.setParameter("orgId", organisationId)
					.setResultTransformer(Transformers.aliasToBean(OrganisationMasterDtoSync.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		} catch (HibernateException he) {
			logger.error("getGlobalOrganisationMasterForSyncById failed", he);
		} catch (Exception e) {
			logger.error("getGlobalOrganisationMasterForSyncById failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Response saveOrganizationMaster(OrganizationMasterDto organizationMasterDto)throws ApplicationException {
		try{
			OrganizationMaster organizationMaster = new OrganizationMaster();
				organizationMaster.setAreaId(organizationMasterDto.getAreaId());
				organizationMaster.setCityId(organizationMasterDto.getCityId());
				organizationMaster.setCountryId(organizationMasterDto.getCountryId());
				organizationMaster.setFaxNumber(organizationMasterDto.getFaxNumber());
				organizationMaster.setCreatedBy(1);
				organizationMaster.setUpdatedBy(1);
				organizationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss"));
				organizationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss"));
				organizationMaster.setDistrictId(organizationMasterDto.getDistrictId());
				organizationMaster.setOrganizationAddress(organizationMasterDto.getOrganizationAddress());
				organizationMaster.setOrganizationCode(organizationMasterDto.getOrganizationCode());
				organizationMaster.setOrganizationContact(organizationMasterDto.getOrganizationContact());
				organizationMaster.setOrganizationDesc(organizationMasterDto.getOrganizationDesc());
				organizationMaster.setOrganizationEmailId(organizationMasterDto.getOrganizationEmailId());
				organizationMaster.setOrganizationName(organizationMasterDto.getOrganizationName());
				organizationMaster.setOrganizationLogo(organizationMasterDto.getOrganizationLogo());
				organizationMaster.setOrganizationFolder(organizationMasterDto.getOrganizationFolder());
				organizationMaster.setIsDuoAuthenticationRequired(organizationMasterDto.getIsDuoAuthenticationRequired());
				organizationMaster.setPostCode(organizationMasterDto.getPostCode());
				organizationMaster.setStateId(organizationMasterDto.getStateId());
				organizationMaster.setStatus(organizationMasterDto.getStatus());
				organizationMaster.setRegistrationNumber(organizationMasterDto.getRegistrationNumber());
				organizationMaster.setAppUrl(organizationMasterDto.getAppUrl());
				organizationMaster.setUnitTimeZone(organizationMasterDto.getUnitTimeZone()!=null ? organizationMasterDto.getUnitTimeZone():"Asia/Calcutta");
				organizationMaster.setIsSyncScheduling(organizationMasterDto.getIsSyncScheduling());
				organizationMaster.setOrgPrivileges(organizationMasterDto.getOrgPrivileges());
				organizationMaster.setChannelId(organizationMasterDto.getChannelId());
				
				organizationMaster = save(organizationMaster);
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, organizationMaster);

		}catch(HibernateException he){
		throw new ApplicationException(he.getMessage(), he);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateOrganizationMaster(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
		try {
			OrganizationMaster organizationMaster = findById(organizationMasterDto.getOrganizationId());
			// organizationMaster.setOrganizationId(organizationMasterDto.getOrganizationId());
			organizationMaster.setAreaId(organizationMasterDto.getAreaId());
			organizationMaster.setCityId(organizationMasterDto.getCityId());
			organizationMaster.setFaxNumber(organizationMasterDto.getFaxNumber());
			organizationMaster.setCountryId(organizationMasterDto.getCountryId());
			organizationMaster.setCreatedBy(1);
			organizationMaster.setUpdatedBy(1);
			organizationMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss"));
			organizationMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss"));
			organizationMaster.setDistrictId(organizationMasterDto.getDistrictId());
			organizationMaster.setOrganizationAddress(organizationMasterDto.getOrganizationAddress());
			organizationMaster.setOrganizationCode(organizationMasterDto.getOrganizationCode());
			organizationMaster.setOrganizationContact(organizationMasterDto.getOrganizationContact());
			organizationMaster.setOrganizationDesc(organizationMasterDto.getOrganizationDesc());
			organizationMaster.setOrganizationEmailId(organizationMasterDto.getOrganizationEmailId());
			organizationMaster.setOrganizationName(organizationMasterDto.getOrganizationName());
			organizationMaster.setOrganizationLogo(organizationMasterDto.getOrganizationLogo());
			organizationMaster.setOrganizationFolder(organizationMasterDto.getOrganizationFolder());
			organizationMaster.setPostCode(organizationMasterDto.getPostCode());
			organizationMaster.setStateId(organizationMasterDto.getStateId());
			organizationMaster.setStatus(organizationMasterDto.getStatus());
			organizationMaster.setRegistrationNumber(organizationMasterDto.getRegistrationNumber());
			organizationMaster.setIsDuoAuthenticationRequired(organizationMasterDto.getIsDuoAuthenticationRequired());
			organizationMaster.setAppUrl(organizationMasterDto.getAppUrl());
			organizationMaster.setUnitTimeZone(
					organizationMasterDto.getUnitTimeZone() != null ? organizationMasterDto.getUnitTimeZone()
							: "Asia/Calcutta");
			organizationMaster.setIsSyncScheduling(organizationMasterDto.getIsSyncScheduling());
			organizationMaster.setOrgPrivileges(organizationMasterDto.getOrgPrivileges());
			organizationMaster.setChannelId(organizationMasterDto.getChannelId());
			organizationMaster = update(organizationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, organizationMaster);

		} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getOrganizationByName(String organizationName)throws ApplicationException {
		try{
			List<OrganizationMasterDto> listOrganizationMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORGANIZATION_BY_NAME").setParameter("organizationName", organizationName.toLowerCase().trim()).setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, listOrganizationMasterDto, null);
		}catch(HibernateException he){
			logger.error("getOrganizationByName failed", he);
		}catch(Exception e){
			logger.error("getOrganizationByName failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public OrganizationMasterDto getOrganizationById(Integer organizationId)throws ApplicationException {
		try{
			OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().getNamedQuery("GET_ORGANIZATION_BY_ID").setParameter("organizationId", organizationId).setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();
			if(organizationId != null){
				List<OrganizationUnitLicenceDetailsDto> listLicenceDetailsDtos = null;
				String query = sessionFactory.getCurrentSession().getNamedQuery("GET_LICENCE_DETAILS").getQueryString();
				query = (query + " WHERE licence.organization_id ="+organizationId);
				listLicenceDetailsDtos = sessionFactory.getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(OrganizationUnitLicenceDetailsDto.class)).list();
				organizationMasterDto.setListOrganizationUnitLicenceDetailsDto(listLicenceDetailsDtos);
			}
			return organizationMasterDto;
		}catch(HibernateException he){
			logger.error("getOrganizationById failed", he);
		}catch(Exception e){
			logger.error("getOrganizationById failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Response getOrganizationByNameAndNotById(OrganizationMasterDto organizationMasterDto)throws ApplicationException {
		try{
			List<OrganizationMasterDto> listOrganizationMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORGANIZATION_BY_NAME_AND_NOT_BY_ID").setParameter("organizationName", organizationMasterDto.getOrganizationName().toLowerCase().trim()).setParameter("organizationId", organizationMasterDto.getOrganizationId()).setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, listOrganizationMasterDto, null);
		}catch(HibernateException he){
		throw new ApplicationException(he.getMessage(), he);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getAllOrganizationMasterList(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
		try{
			List<OrganizationMasterDto> listOrganizationMasterDto = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ALL_ORGNIZATION_LIST")
				.setFirstResult(organizationMasterDto.getOffset() != null ? organizationMasterDto.getOffset() : 0)
				.setMaxResults(organizationMasterDto.getNoOfRecordsPerPage() != null ? organizationMasterDto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class))
				.list();
			return new Response<>(SUCCESS, null, null, listOrganizationMasterDto, null);
		}catch(HibernateException he){
			logger.error("getAllOrganizationMasterList failed", he);
		}catch(Exception e){
			logger.error("getAllOrganizationMasterList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getOrganizationCount() throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(OrganizationMaster.class); 
			//c.add(Restrictions.eq("organizationId", countryMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("getOrganizationCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response updateOrganizationStatus(OrganizationMasterDto organizationMasterDto)throws ApplicationException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_ORGANIZATION_STATUS").setCharacter("status", organizationMasterDto.getStatus()).setParameter("organizationId", organizationMasterDto.getOrganizationId()).executeUpdate();
			if(result != null){
				return new Response<>(SUCCESS, null, null, null, null);
			}
		}catch(HibernateException he){
			logger.error("updateOrganizationStatus failed", he);
		}catch(Exception e){
			logger.error("updateOrganizationStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveOrganizationList() throws ApplicationException {
		try{
			List<OrganizationMasterDto> listOrganizationMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_ORGANIZATION_MASTER_LIST").setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, listOrganizationMasterDto, null);
		}catch(HibernateException he){
			logger.error("getActiveOrganizationList failed", he);
		}catch(Exception e){
			logger.error("getActiveOrganizationList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

  @Override
  public Response updateOrganizationFolderPath(OrganizationMasterDto organizationMasterDto)
      throws ApplicationException {
    try{
      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_ORGANIZATION_FOLDER")
          .setParameter("organizationFolder", organizationMasterDto.getOrganizationFolder())
          .setParameter("organizationId", organizationMasterDto.getOrganizationId()).executeUpdate();
      if(result != null){
          return new Response<>(SUCCESS, null, null, null, null);
      }
  }catch(HibernateException he){
      logger.error("updateOrganizationFolderPath failed", he);
  }catch(Exception e){
      logger.error("updateOrganizationFolderPath failed", e);
  }
  return new Response<>(ERROR, null, null, null, null);
  }

  @Override
  public Response getOrganiazationFolderPath(int organisationId) throws ApplicationException {
    try{
      OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().getNamedQuery("GET_ORGANIZATION_BY_ID").setParameter("organizationId", organisationId).setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();
      return new Response<>(SUCCESS, null, null, null, organizationMasterDto);
  }catch(HibernateException he){
	  throw he;
  }catch(Exception e){
	  throw e;
  }
 // return new Response<>(ERROR, null, null, null, null);
  }

  @Override
  public Response updateImagePath(OrganizationMasterDto organizationMasterDto)
      throws ApplicationException {
    try{
      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_ORGANIZATION_LOGO")
          .setParameter("organizationLogo", organizationMasterDto.getOrganizationLogo())
          .setParameter("image", organizationMasterDto.getImage())
          .setParameter("organizationId", organizationMasterDto.getOrganizationId()).executeUpdate();
      if(result != null){
          return new Response<>(SUCCESS, null, null, null, null);
      }
  }catch(HibernateException he){
      logger.error("updateImagePath failed", he);
  }catch(Exception e){
      logger.error("updateImagePath failed", e);
  }
  return new Response<>(ERROR, null, null, null, null);
  }

	@Override
	public Response updateGlobalOrgId(Integer localOrganisationId, Integer globalOrgId) {
		try {
			
			Integer result = sessionFactory.getCurrentSession().createSQLQuery("update m_organization_master set global_org_id=:globalOrgId where organization_id=:orgId ")
					.setParameter("orgId", localOrganisationId)
					.setParameter("globalOrgId", globalOrgId)
					.executeUpdate();
			if (result != null) {
				return new Response<>(SUCCESS, null, null, null, null);
			}
		} catch (HibernateException he) {
			logger.error("updateGlobalOrgId failed", he);
		} catch (Exception e) {
			logger.error("updateGlobalOrgId failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getOrganizationListByOrgName(String orgName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try{

			List<OrganizationMasterDto> listOrganizationMasterDto=new ArrayList<OrganizationMasterDto>();
			connection = DriverManager.getConnection(System.getenv("HIS_SUBSCRIPTIONS_DB_URL"),
					System.getenv("HIS_SUBSCRIPTIONS_DB_USERNAME"), System.getenv("HIS_SUBSCRIPTIONS_DB_PASSWORD"));
			
			 statement = connection.prepareStatement(
					" select organization_id,organization_name,organization_url from m_his_hospital_subscription_details " 
					+" where lower(organization_name) like '%"+(orgName.toLowerCase().trim())+"%' "
					+" AND doctor_app_status = 'A' group by organization_id,organization_name,organization_url ");
			 rs = statement.executeQuery();
			
			while (rs.next()) {
				OrganizationMasterDto dto=new OrganizationMasterDto();
				dto.setOrganizationId(rs.getInt("organization_id"));
				dto.setOrganizationName(rs.getString("organization_name"));
				dto.setAppUrl(rs.getString("organization_url"));
				listOrganizationMasterDto.add(dto);
			}
			return new Response<>(SUCCESS, null, null, listOrganizationMasterDto, null);
		}catch(HibernateException he){
			logger.error("getOrganizationListByOrgName failed", he);
		}catch(Exception e){
			logger.error("getOrganizationListByOrgName failed", e);
		}finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (rs != null) {
					rs.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				logger.error("getOrganizationListByOrgName failed", e);
			}
		}
		return new Response<>(ERROR, null, null, null, null);

	}

	
    @Override
    public Response updateRISPdfPathByTemplateTransactionId(String docPath, TemplateTransactionDto dto) throws ApplicationException {
         try{
			 String query = "UPDATE public.t_template_transaction SET radiology_report_link = :docPath "
					 + "WHERE template_transaction_id = :templateTransactionId "
					 + "AND org_id = :organizationId AND unit_id = :unitId";

			 Integer result = sessionFactory.getCurrentSession()
					 .createSQLQuery(query)
					 .setParameter("docPath", docPath)
					 .setParameter("templateTransactionId", dto.getTemplateTransactionId())
					 .setParameter("unitId", dto.getUnitId())
					 .setParameter("organizationId", dto.getOrgId())
					 .executeUpdate();

			 if(result != null){
                  return new Response<>(SUCCESS, null, null, null, dto);
              }
          }catch(HibernateException he){
              logger.error("updateRISPdfPathByTemplateTransactionId failed", he);
          }catch(Exception e){
              logger.error("updateRISPdfPathByTemplateTransactionId failed", e);
          }
          return new Response<>(ERROR, null, null, null, null);
          }

}
