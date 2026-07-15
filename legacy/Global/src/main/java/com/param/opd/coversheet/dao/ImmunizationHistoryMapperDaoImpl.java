package com.param.opd.coversheet.dao;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ImmunizationHistoryMapperDto;
import com.param.opd.coversheet.model.ImmunizationHistoryMapper;
import com.param.opd.coversheet.service.IImmunizationHistoryMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImmunizationHistoryMapperDaoImpl extends GenericDao<ImmunizationHistoryMapper, Integer> implements
		IImmunizationHistoryMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(ImmunizationHistoryMapperDaoImpl.class);


	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;
	
	public ImmunizationHistoryMapperDaoImpl() {
		super(ImmunizationHistoryMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IImmunizationHistoryMapperService iImmunizationHistoryMapperService;
	
	@Override
	public Response saveImmunizationHistoryMapper(ImmunizationHistoryMapperDto immunizationHistoryMapperDto)throws ApplicationException {
		try
		{
			for(int i=0;i<immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().size();i++)
			{
				if(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationHistoryMapperId() != null && 
						immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationHistoryMapperId()>0
						)
				{
					ImmunizationHistoryMapper immunizationHistoryMapper = findById(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationHistoryMapperId());
					immunizationHistoryMapper.setIsReviewedFlag(YES);
					iImmunizationHistoryMapperService.updateSaveNurseReviewedFlag(immunizationHistoryMapper);
				}
				
				ImmunizationHistoryMapper immunizationHistoryMapper = new ImmunizationHistoryMapper();
				immunizationHistoryMapper.setDrugId(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getDrugId());
				
				immunizationHistoryMapper.setIsAdministeredStatus(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getIsAdministeredStatus());
				if(immunizationHistoryMapper.getIsAdministeredStatus()=='N')
				{
					immunizationHistoryMapper.setImmunizationDate(null);
	
				}else {
						immunizationHistoryMapper.setImmunizationDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationDate(),"dd-MM-yyyy"));
				}
				immunizationHistoryMapper.setRoleId(immunizationHistoryMapperDto.getRoleId());
				immunizationHistoryMapper.setIsReviewedFlag(
						immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
							immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getIsReviewedFlag()
						);
				
				immunizationHistoryMapper.setCreatedDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getCreatedDate(), "dd-MM-yyyy"));
				if(immunizationHistoryMapperDto.getEncounterId()!=null){
					immunizationHistoryMapper.setEncounterId(immunizationHistoryMapperDto.getEncounterId());
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					immunizationHistoryMapper.setAdmissionId(immunizationHistoryMapperDto.getAdmissionId());
				}
				immunizationHistoryMapper.setEncounterId(immunizationHistoryMapperDto.getEncounterId());
				immunizationHistoryMapper.setCreatedBy(immunizationHistoryMapperDto.getCreatedBy());
				immunizationHistoryMapper.setUpdatedBy(immunizationHistoryMapperDto.getUpdatedBy());
				immunizationHistoryMapper.setUpdatedDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getUpdatedDate(), "dd-MM-yyyy"));
				immunizationHistoryMapper.setStatus(immunizationHistoryMapperDto.getStatus());
				immunizationHistoryMapper.setOrganizationId(immunizationHistoryMapperDto.getOrganizationId());
				immunizationHistoryMapper.setUnitId(immunizationHistoryMapperDto.getUnitId());
				immunizationHistoryMapper.setPatientId(immunizationHistoryMapperDto.getPatientId());
				
				ImmunizationHistoryMapper historyId=save(immunizationHistoryMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("saveImmunizationHistoryMapper failed", he);
		} catch (Exception e) {
		logger.error("saveImmunizationHistoryMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, "Please add Date", null, null);
	}

	@Override
	public Response getListOfImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<ImmunizationHistoryMapperDto> listImmunizationHistoryMapperDto = statlessSession.getNamedQuery("GET_LIST_IMMUNIZATION_MASTER_HISTORY")
					.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
					.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
					.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listImmunizationHistoryMapperDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfImmunizationHistoryMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOldImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		try
		{
			List<ImmunizationHistoryMapperDto> listImmunizationHistoryMapperDto;
			/*if(immunizationHistoryMapperDto.getRoleId() == 3)
			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				 listImmunizationHistoryMapperDto = statlessSession.getNamedQuery("GET_LIST_IMMUNIZATION_MASTER_HISTORY_BY_ROLE_ID")
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
						.setParameter("encounterId", immunizationHistoryMapperDto.getEncounterId())
						.setParameter("roleId", immunizationHistoryMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
				statlessSession.close();
			}
			else
			{*/
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				 listImmunizationHistoryMapperDto = statlessSession.getNamedQuery("GET_LIST_IMMUNIZATION_MASTER_HISTORY")
//						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
//						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
//						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
//						.setParameter("encounterId", immunizationHistoryMapperDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
//				statlessSession.close();
			/*}
			*/
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_IMMUNIZATION_MASTER_HISTORY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				/*if(immunizationHistoryMapperDto.getEncounterId()!=null){
					subQuery.append(" AND (imHisMapper.encounterId IS NULL OR imHisMapper.encounterId !="+immunizationHistoryMapperDto.getEncounterId()+")");
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId asc");
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					subQuery.append(" AND (imHisMapper.admissionId IS NULL OR imHisMapper.admissionId !="+immunizationHistoryMapperDto.getAdmissionId()+")");
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId asc");
				}*/
				query=query.concat(subQuery.toString());
				listImmunizationHistoryMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
						.setParameter("created_date", immunizationHistoryMapperDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listImmunizationHistoryMapperDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldImmunizationHistoryMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		try
		{
			List<ImmunizationHistoryMapperDto> listImmunizationHistoryMapperDto;
			/*if(immunizationHistoryMapperDto.getRoleId() == 3)
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_IMMUNIZATION_MASTER_HISTORY_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(immunizationHistoryMapperDto.getEncounterId()!=null){
					subQuery.append(" AND imHisMapper.encounterId ="+immunizationHistoryMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId desc");
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					subQuery.append(" AND imHisMapper.admissionId ="+immunizationHistoryMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId desc");
				}
				query=query.concat(subQuery.toString());
				listImmunizationHistoryMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
						.setParameter("roleId", immunizationHistoryMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
			}
			else
			{*/

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_IMMUNIZATION_MASTER_HISTORY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(immunizationHistoryMapperDto.getEncounterId()!=null){
					subQuery.append(" AND imHisMapper.encounterId = :encounterId");
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId asc");
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					subQuery.append(" AND imHisMapper.admissionId = :admissionId");
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId asc");
				}
				query=query.concat(subQuery.toString());
				Query createQuery=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId());

			if (immunizationHistoryMapperDto.getEncounterId() != null) {
				createQuery.setParameter("encounterId", immunizationHistoryMapperDto.getEncounterId()); // Set encounterId parameter
			}
			if (immunizationHistoryMapperDto.getAdmissionId() != null) {
				createQuery.setParameter("admissionId", immunizationHistoryMapperDto.getAdmissionId()); // Set admissionId parameter
			}

			listImmunizationHistoryMapperDto=createQuery.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
			/*}*/
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listImmunizationHistoryMapperDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentImmunizationHistoryMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().size();i++)
			{
				
				ImmunizationHistoryMapper immunizationHistoryMapper = findById(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationHistoryMapperId());
					
				/*if(immunizationHistoryMapperDto.getRoleId() != null 
						&& immunizationHistoryMapperDto.getRoleId() == 2 
						&& immunizationHistoryMapper.getRoleId() != 2 
						&& immunizationHistoryMapper.getIsReviewedFlag() == 'N'
						)
				{
					iImmunizationHistoryMapperService.saveImmunizationHistoryMapper(immunizationHistoryMapperDto);
				}*/
				
				immunizationHistoryMapper.setDrugId(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getDrugId());
				immunizationHistoryMapper.setIsAdministeredStatus(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getIsAdministeredStatus());
//				immunizationHistoryMapper.setImmunizationDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationDate(),"dd-MM-yyyy"));
				if(immunizationHistoryMapper.getIsAdministeredStatus()=='N')
				{
					immunizationHistoryMapper.setImmunizationDate(null);
	
				}else {
						immunizationHistoryMapper.setImmunizationDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getImmunizationDate(),"dd-MM-yyyy"));
				}
				immunizationHistoryMapper.setRoleId(immunizationHistoryMapperDto.getRoleId());
				immunizationHistoryMapper.setIsReviewedFlag(immunizationHistoryMapperDto.getRoleId() == 2 
						&& immunizationHistoryMapperDto.getRoleId() == 3
						? YES : immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getIsReviewedFlag());
				immunizationHistoryMapper.setCreatedDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getCreatedDate(), "dd-MM-yyyy"));
				if(immunizationHistoryMapperDto.getEncounterId()!=null){
					immunizationHistoryMapper.setEncounterId(immunizationHistoryMapperDto.getEncounterId());
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					immunizationHistoryMapper.setAdmissionId(immunizationHistoryMapperDto.getAdmissionId());
				}
				immunizationHistoryMapper.setCreatedBy(immunizationHistoryMapperDto.getCreatedBy());
				immunizationHistoryMapper.setUpdatedBy(immunizationHistoryMapperDto.getUpdatedBy());
				immunizationHistoryMapper.setUpdatedDate(CommonDateUtils.getDate(immunizationHistoryMapperDto.getUpdatedDate(), "dd-MM-yyyy"));
				immunizationHistoryMapper.setStatus(immunizationHistoryMapperDto.getStatus());
				immunizationHistoryMapper.setOrganizationId(immunizationHistoryMapperDto.getOrganizationId());
				immunizationHistoryMapper.setUnitId(immunizationHistoryMapperDto.getUnitId());
				immunizationHistoryMapper.setPatientId(immunizationHistoryMapperDto.getPatientId());
				
				ImmunizationHistoryMapper historyId=save(immunizationHistoryMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
		logger.error("updateImmunizationHistoryMapper failed", he);
		} catch (Exception e) {
		logger.error("updateImmunizationHistoryMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, "Please add Date", null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			ImmunizationHistoryMapper immunizationHistoryMapper)
			throws ApplicationException {
		try
		{
			save(immunizationHistoryMapper);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateSaveNurseReviewedFlag failed", he);
		} catch (Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateImmunizationHistoryForOldData(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().size();i++)
			{
				
//					Session statlessSession = sessionFactory.getCurrentSession();
//					int updateCount= statlessSession.getNamedQuery("UPDATE_OLD_IMMUNIZATION")
//							.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
//							.setParameter("drugId", immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getDrugId())
//							.setParameter("roleId", immunizationHistoryMapperDto.getRoleId())
//							.setParameter("encounterId", immunizationHistoryMapperDto.getEncounterId())
//							.executeUpdate();
				String query=sessionFactory.getCurrentSession().getNamedQuery("UPDATE_OLD_IMMUNIZATION").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(immunizationHistoryMapperDto.getEncounterId()!=null){
					subQuery.append(" AND imHisMapper.encounterId = :encounterId");
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					subQuery.append(" AND imHisMapper.admissionId = :admissionId");
				}
				query=query.concat(subQuery.toString());
				Query createQuery= sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("habitId", immunizationHistoryMapperDto.getListListImmunizationHistoryMapperDto().get(i).getDrugId())
						.setParameter("roleId", immunizationHistoryMapperDto.getRoleId());

				if (immunizationHistoryMapperDto.getEncounterId() != null) {
					createQuery.setParameter("encounterId", immunizationHistoryMapperDto.getEncounterId()); // Set encounterId parameter
				}
				if (immunizationHistoryMapperDto.getAdmissionId() != null) {
					createQuery.setParameter("admissionId", immunizationHistoryMapperDto.getAdmissionId()); // Set admissionId parameter
				}

				int updateCount=createQuery.executeUpdate();
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateImmunizationHistoryForOldData failed", he);
		} catch (Exception e) {
		logger.error("updateImmunizationHistoryForOldData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentImmunizationHistoryMapperForCoversheet(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto) {
		try
		{
			List<ImmunizationHistoryMapperDto> listImmunizationHistoryMapperDto;
			/*if(immunizationHistoryMapperDto.getRoleId() == 3)
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_IMMUNIZATION_MASTER_HISTORY_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(immunizationHistoryMapperDto.getEncounterId()!=null){
					subQuery.append(" AND imHisMapper.encounterId ="+immunizationHistoryMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId desc");
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					subQuery.append(" AND imHisMapper.admissionId ="+immunizationHistoryMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId desc");
				}
				query=query.concat(subQuery.toString());
				listImmunizationHistoryMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
						.setParameter("roleId", immunizationHistoryMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
			}
			else
			{*/

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_IMMUNIZATION_MASTER_HISTORY_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				/*if(immunizationHistoryMapperDto.getEncounterId()!=null){
					subQuery.append(" AND imHisMapper.encounterId ="+immunizationHistoryMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId asc");
				}
				if(immunizationHistoryMapperDto.getAdmissionId()!=null){
					subQuery.append(" AND imHisMapper.admissionId ="+immunizationHistoryMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	imHisMapper.immunizationHistoryMapperId asc");
				}*/
				query=query.concat(subQuery.toString());
				listImmunizationHistoryMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", immunizationHistoryMapperDto.getPatientId())
						.setParameter("unitId", immunizationHistoryMapperDto.getUnitId())
						.setParameter("organizationId", immunizationHistoryMapperDto.getOrganizationId())
						.setParameter("created_date", immunizationHistoryMapperDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(ImmunizationHistoryMapperDto.class)).list();
			/*}*/
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listImmunizationHistoryMapperDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentImmunizationHistoryMapperForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

}
