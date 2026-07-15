package com.param.global.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;
import com.param.global.dto.CustomHistoryConfigurationDto;
import com.param.global.model.CustomHistoryConfigurationDetails;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class CustomHistoryConfigDetailsDaoImpl extends GenericDao<CustomHistoryConfigurationDetails, Integer>
implements ICustomHistoryConfigDetailsDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(CustomHistoryConfigDetailsDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public CustomHistoryConfigDetailsDaoImpl() {
		super(CustomHistoryConfigurationDetails.class);
	}


	@Override
	public Response getCustomHistoryConfigurationList(CustomHistoryConfigurationDetailsDto customHistoryConfigurationDetailsDto)
			throws ApplicationException {
		try {
			
			List<CustomHistoryConfigurationDto> customHistoryConfigurationList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CUSTOM_HISTORY_CONFIG_LIST")
					.setParameterList("specialityIdList", customHistoryConfigurationDetailsDto.getSpecialityIdList())
					.setResultTransformer(Transformers.aliasToBean(CustomHistoryConfigurationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, customHistoryConfigurationList, null);
		}catch (HibernateException he) {
		logger.error("getCustomHistoryConfigurationList failed", he);
		} catch (Exception e) {
		logger.error("getCustomHistoryConfigurationList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveCustomHistoryConfigurationDetails(CustomHistoryConfigurationDetailsDto dtlsDto)
			throws ApplicationException {
try {
			
	CustomHistoryConfigurationDetails  mstr = new CustomHistoryConfigurationDetails();
	mstr.setSpecialityId(dtlsDto.getSpecialityId());
	mstr.setCategory(dtlsDto.getCategory());
	mstr.setRemark(dtlsDto.getRemark());
	mstr.setPatientId(dtlsDto.getPatientId());
	mstr.setVisitTypeId(dtlsDto.getVisitTypeId());
	mstr.setEncounterId(dtlsDto.getEncounterId());
	mstr.setAdmissionId(dtlsDto.getAdmissionId());
	mstr.setStatus(dtlsDto.getStatus());
	mstr.setOrganizationId(dtlsDto.getOrganizationId());
	mstr.setUnitId(dtlsDto.getUnitId());
	mstr.setCreatedBy(dtlsDto.getCreatedBy());
	mstr.setUpdatedBy(dtlsDto.getUpdatedBy());
	mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dtlsDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
	mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dtlsDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
	mstr.setCustomHistoryConfigurationMstrId(dtlsDto.getCustomHistoryConfigurationMstrId());
	mstr = save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, mstr);
		}catch (HibernateException he) {
		logger.error("saveCustomHistoryConfigurationDetails failed", he);
		} catch (Exception e) {
		logger.error("saveCustomHistoryConfigurationDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateCustomHistoryDetails(CustomHistoryConfigurationDetailsDto dto) throws ApplicationException {
		try
		{
			sessionFactory.getCurrentSession().createSQLQuery(" update public.t_custom_history_configuration_details set remark = '"+dto.getRemark()+"' where custom_history_config_details_id ="+dto.getCustomHistoryConfigDetailsId())
					.executeUpdate();
			return new Response(SUCCESS, COMMON_UPDATE, null, null, null);
		} catch (Exception e)
		{
		logger.error("updateCustomHistoryDetails failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}


	@Override
	public Response deleteCustomHistoryDetailsRecord(CustomHistoryConfigurationDetailsDto dto)
			throws ApplicationException {
		try
		{
			String query = "UPDATE public.t_custom_history_configuration_details "
					+ "SET status = :status "
					+ "WHERE custom_history_config_details_id = :customHistoryConfigDetailsId";

			sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("status", dto.getStatus())
					.setParameter("customHistoryConfigDetailsId", dto.getCustomHistoryConfigDetailsId())
					.executeUpdate();

			return new Response(SUCCESS, COMMON_UPDATE, null, null, null);
		} catch (Exception e)
		{
		logger.error("deleteCustomHistoryDetailsRecord failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}


	@Override
	public Response getPatientCustomHistoryDetails(CustomHistoryConfigurationDetailsDto dto)
			throws ApplicationException {
	try {
			
		String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_CUSTOM_HISTORY_DETAILS").getQueryString();
		StringBuffer sb= new StringBuffer(query);
		
		 if (dto.getEncounterId()!=null ) {
	            sb.append(" AND  dtls.encounter_id = " + dto.getEncounterId());
	       }
		 
		 if (dto.getAdmissionId()!=null ) {
	            sb.append(" AND dtls.admission_id= " + dto.getAdmissionId());
	       }
		 
		 sb.append(" order by dtls.custom_history_config_details_id asc ");
			List<CustomHistoryConfigurationDetailsDto> customHistoryConfigurationList = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CustomHistoryConfigurationDetailsDto.class)).list();
			Set<Integer> splIdsSet = new HashSet<Integer>();
			CustomHistoryConfigurationDto newDto = new CustomHistoryConfigurationDto();
			if(customHistoryConfigurationList!=null && customHistoryConfigurationList.size()>0) {
				for (CustomHistoryConfigurationDetailsDto customHistoryConfigurationDto : customHistoryConfigurationList) {
					splIdsSet.add(customHistoryConfigurationDto.getSpecialityId());
				}
				
				newDto.setSpecialityIdList(new ArrayList<>(splIdsSet));
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, customHistoryConfigurationList, newDto);
		}catch (HibernateException he) {
		logger.error("getPatientCustomHistoryDetails failed", he);
		} catch (Exception e) {
		logger.error("getPatientCustomHistoryDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
