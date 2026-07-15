package com.param.otims.dao;

import java.util.List;

import com.param.global.dto.RadiologyOrdersListDto;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.otims.model.PreOperativeDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import static com.param.global.common.IError.SUCCESS_200;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PreOperativeDetailsDaoImpl extends GenericDao<PreOperativeDetails, Integer> implements IPreOperativeDetailsDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(PreOperativeDetailsDaoImpl.class);

	public PreOperativeDetailsDaoImpl() {
		super(PreOperativeDetails.class);
	}

	@Override
	public Response savePreOperativeDetails(PreOperativeDetailsDto dto) throws ApplicationException {
		try{
			PreOperativeDetails master = new PreOperativeDetails();
			master.setAnaesthesiaStatus(dto.getAnaesthesiaStatus());
			master.setBloodStatus(dto.getBloodStatus());
			master.setIcuStatus(dto.getIcuStatus());
			master.setIndicationReason(dto.getIndicationReason());
			master.setPatientConsentStatus(dto.getPatientConsentStatus());
			master.setPatientIdentificationStatus(dto.getPatientIdentificationStatus());
			master.setProReqId(dto.getProReqId());
			master.setWhoChecklistStatus(dto.getWhoChecklistStatus());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setProReqId(dto.getProReqId());
			master.setStatus('A');
			if(dto.getPreOperativeId() != null) {
				master.setPreOperativeId(dto.getPreOperativeId());
				master = update(master);
			}else {
				master = save(master);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getPreOperativeDetailsByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try{
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PREOPERATIVE_DETAILS_BY_PRO_REQ_ID").getQueryString();
			PreOperativeDetailsDto obj = (PreOperativeDetailsDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("proReqId", preOperativeDetailsDto.getProReqId())
					.setResultTransformer(Transformers.aliasToBean(PreOperativeDetailsDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, obj);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getActiveDoctorList(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException {
		try{
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_DOCTOR_LIST_WITH_EMAILID").getQueryString();
			List<DoctorMasterDto> obj = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", preOperativeDetailsDto.getOrganizationId())
					.setParameter("unitId", preOperativeDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, obj, null);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getAnaesthesiaDoctorList(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try{
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_ANAESTHESIA_DOCTORS").getQueryString();
			List<DoctorMasterDto> obj = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", preOperativeDetailsDto.getOrganizationId())
					.setParameter("unitId", preOperativeDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, obj, null);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getProcedureOrderIdByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try{
			
			ChecklistMasterDto obj = (ChecklistMasterDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROC_ORDER_REQ_ID").setParameter("proReqId", preOperativeDetailsDto.getProReqId()).setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, obj);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getAiTranscriptionCreds(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException {
		{
			try {
				String query = "select transcription_url as \"aiTranscriptionUrl\", " +
						" api_key as \"aiTranscriptionApiKey\", " +
						" user_prompt as \"userPrompt\" " +
						" from public.t_ai_recording_transcription_credentials  " +
						" where unit_id  =:unitId " +
						" and organization_id =:orgId " +
						" and status = 'A' " +
						" order by ai_voice_transcription_id desc limit 1";

				PreOperativeDetailsDto credResult = (PreOperativeDetailsDto) sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("unitId", preOperativeDetailsDto.getUnitId())
						.setParameter("orgId", preOperativeDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PreOperativeDetailsDto.class))
						.uniqueResult();

				return new Response(SUCCESS, SUCCESS_200, null, null, credResult);

			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}
	}

}
