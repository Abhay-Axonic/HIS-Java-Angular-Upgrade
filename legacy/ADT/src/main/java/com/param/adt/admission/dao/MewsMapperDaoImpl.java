package com.param.adt.admission.dao;

import java.util.Date;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.MewsMapperDto;
import com.param.adt.admission.model.MewsMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class MewsMapperDaoImpl extends GenericDao<MewsMapper, Integer> implements IMewsMapperDao,ICommonConstants
{

	public MewsMapperDaoImpl() {
		super(MewsMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveMewsMapper(MewsMapperDto mewsMapperDto) throws ApplicationException {
		try {
			//<-----in-activating previous active records 
			sessionFactory.getCurrentSession().createQuery("UPDATE MewsMapper set status='I' "
					+ "where patientId=:patientId "
					+ "and admissionId=:admissionId  "
					+ "and status='A' ")
			.setParameter("patientId", mewsMapperDto.getPatientId())
			.setParameter("admissionId", mewsMapperDto.getAdmissionId())
			.executeUpdate();
			//----->
			
			MewsMapper mewsMapper=new MewsMapper();
				mewsMapper.setAdmissionId(mewsMapperDto.getAdmissionId());
				mewsMapper.setPatientId(mewsMapperDto.getPatientId());
				mewsMapper.setSystolicBloodPressureId(mewsMapperDto.getSystolicBloodPressureId());
				mewsMapper.setHeartRateId(mewsMapperDto.getHeartRateId());
				mewsMapper.setAvpuScoreId(mewsMapperDto.getAvpuScoreId());
				mewsMapper.setRespiratoryRateId(mewsMapperDto.getRespiratoryRateId());
				mewsMapper.setTemperatureId(mewsMapperDto.getTemperatureId());
				mewsMapper.setTotalScore(mewsMapperDto.getTotalScore());
				mewsMapper.setOrganizationId(mewsMapperDto.getOrganizationId());
				mewsMapper.setUnitId(mewsMapperDto.getUnitId());
				mewsMapper.setStatus(mewsMapperDto.getStatus());
				mewsMapper.setCreatedBy(mewsMapperDto.getCreatedBy());
//				mewsMapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "DD-MM-YYYY HH:mm:ss"));
				mewsMapper.setCreatedDate(GlobalCommonDateUtils.getDate(mewsMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				mewsMapper.setUpdatedBy(mewsMapperDto.getUpdatedBy());
//				mewsMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "DD-MM-YYYY HH:mm:ss"));
				mewsMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(mewsMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(mewsMapper);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMewsMapperList(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<MewsMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEWS_MAPPER_LIST")
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MewsMapperDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCountOnMewsMapper(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<MewsMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNT_ON_MEWS_MAPPER")
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MewsMapperDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getListOfAllergies(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<MewsMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_ALLERGY")
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MewsMapperDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfOrderedProcedure(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<MewsMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_ORDERED_PROCEDURE")
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MewsMapperDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
