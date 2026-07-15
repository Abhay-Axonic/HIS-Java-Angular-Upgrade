package com.param.global.unit.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.CabinMaster;
import com.param.global.model.OutsideHospitalDoctorMapper;
import com.param.global.model.OutsideHospitalDoctorMapper;
import com.param.global.unit.dao.IOutsideHospitalMasterDao;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.OutsideHospitalDoctorMapperDto;
import com.param.global.unit.dto.OutsideHospitalDoctorMapperDto;
import com.param.global.unit.dto.OutsideHospitalMasterDto;
import com.param.global.unit.service.IOutsideHospitalDoctorMapperDao;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository

public class OutsideHospitalDoctorMapperDaoImpl extends GenericDao<OutsideHospitalDoctorMapper, Integer> implements IOutsideHospitalDoctorMapperDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(OutsideHospitalDoctorMapperDaoImpl.class);


	public OutsideHospitalDoctorMapperDaoImpl() {
		super(OutsideHospitalDoctorMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getOutsideHospitalDoctorMapperByName(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {
			
			List<OutsideHospitalDoctorMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUTSIDE_HOSPITAL_DOCTOR_MAPPER_BY_NAME")
					.setString("hospDoctorName", outsideHospitalDoctorMapperDto.getHospDoctorName().toLowerCase())
					.setInteger("unitId", outsideHospitalDoctorMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalDoctorMapperDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getOutsideHospitalDoctorMapperByName failed", he);
		} catch (Exception e) {
		logger.error("getOutsideHospitalDoctorMapperByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response saveOutsideHospitalDoctorMapper(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {
			if(outsideHospitalDoctorMapperDto.getOutsideHosDocId() == null) {
				OutsideHospitalDoctorMapper outsideHospitalDoctorMapper=new OutsideHospitalDoctorMapper();
				outsideHospitalDoctorMapper.setOutsideHospitalId(outsideHospitalDoctorMapperDto.getOutsideHospitalId());
				outsideHospitalDoctorMapper.setHospDoctorName(outsideHospitalDoctorMapperDto.getHospDoctorName());
				outsideHospitalDoctorMapper.setOrganizationId(outsideHospitalDoctorMapperDto.getOrganizationId());
				outsideHospitalDoctorMapper.setUnitId(outsideHospitalDoctorMapperDto.getUnitId());
				outsideHospitalDoctorMapper.setStatus(outsideHospitalDoctorMapperDto.getStatus());
				outsideHospitalDoctorMapper.setCreatedBy(outsideHospitalDoctorMapperDto.getCreatedBy());
//				outsideHospitalDoctorMapper.setCreatedDate(GlobalCommonDateUtils.getDate(outsideHospitalDoctorMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				outsideHospitalDoctorMapper.setCreatedDate(TimeZoneComponent.getDateByzone(outsideHospitalDoctorMapperDto.getUnitId()));
				outsideHospitalDoctorMapper.setUpdatedBy(outsideHospitalDoctorMapperDto.getUpdatedBy());
//				outsideHospitalDoctorMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(outsideHospitalDoctorMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				outsideHospitalDoctorMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(outsideHospitalDoctorMapperDto.getUnitId()));

				save(outsideHospitalDoctorMapper);
			}else {
				OutsideHospitalDoctorMapper outsideHospitalDoctorMapper=findById(outsideHospitalDoctorMapperDto.getOutsideHosDocId());
				outsideHospitalDoctorMapper.setOutsideHospitalId(outsideHospitalDoctorMapperDto.getOutsideHospitalId());
				outsideHospitalDoctorMapper.setOrganizationId(outsideHospitalDoctorMapperDto.getOrganizationId());
				outsideHospitalDoctorMapper.setHospDoctorName(outsideHospitalDoctorMapperDto.getHospDoctorName());
				outsideHospitalDoctorMapper.setUnitId(outsideHospitalDoctorMapperDto.getUnitId());
				outsideHospitalDoctorMapper.setStatus(outsideHospitalDoctorMapperDto.getStatus());
				outsideHospitalDoctorMapper.setCreatedBy(outsideHospitalDoctorMapperDto.getCreatedBy());
//				outsideHospitalDoctorMapper.setCreatedDate(GlobalCommonDateUtils.getDate(outsideHospitalDoctorMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				outsideHospitalDoctorMapper.setCreatedDate(TimeZoneComponent.getDateByzone(outsideHospitalDoctorMapperDto.getUnitId()));
				outsideHospitalDoctorMapper.setUpdatedBy(outsideHospitalDoctorMapperDto.getUpdatedBy());
//				outsideHospitalDoctorMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(outsideHospitalDoctorMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				outsideHospitalDoctorMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(outsideHospitalDoctorMapperDto.getUnitId()));

				update(outsideHospitalDoctorMapper);
			}
			
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveOutsideHospitalDoctorMapper failed", he);
		} catch (Exception e) {
		logger.error("saveOutsideHospitalDoctorMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOutsideHospitalDoctorMapperList(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {
			String query=" select " + 
					"	mppr.outside_hos_doc_id as \"outsideHosDocId\" , " + 
					"	mppr.outside_hospital_id as \"outsideHospitalId\" , " + 
					"	mppr.hosp_doctor_name as \"outsideHospitalDoctorName\", " + 
					"	mppr.hosp_doctor_name as \"hospDoctorName\", " + 
					"	mppr.status as \"status\" " + 
					" from " + 
					"	public.t_outside_hospital_doctor_mapper mppr "
					+ " Where mppr.organization_id= :orgId"
					+ " and mppr.unit_id= :unitId"
					+ " and mppr.outside_hospital_id= :id";
			
					List<OutsideHospitalDoctorMapperDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
							.setInteger("id", outsideHospitalDoctorMapperDto.getOutsideHospitalId())
							.setInteger("orgId", outsideHospitalDoctorMapperDto.getOrganizationId())
					.setInteger("unitId", outsideHospitalDoctorMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalDoctorMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getOutsideHospitalDoctorMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateOutsideHospitalDoctorMapperStatus(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto) throws ApplicationException {
		try {
			OutsideHospitalDoctorMapper mst=findById(outsideHospitalDoctorMapperDto.getOutsideHosDocId());
			
			mst.setStatus(outsideHospitalDoctorMapperDto.getStatus());
			mst.setUpdatedBy(outsideHospitalDoctorMapperDto.getUpdatedBy());
			mst.setUpdatedDate(TimeZoneComponent.getDateByzone(outsideHospitalDoctorMapperDto.getUnitId()));
			 
			update(mst);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateOutsideHospitalDoctorMapperStatus failed", he);
	} catch (Exception e) {
	logger.error("updateOutsideHospitalDoctorMapperStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}

	@Override
	public Response getOutsideHospitalDoctorMapperById(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {
			String query=" select " + 
					"	mppr.outside_hos_doc_id as \"outsideHosDocId\" , " + 
					"	mppr.outside_hospital_id as \"outsideHospitalId\" , " + 
					"	mppr.hosp_doctor_name as \"hospDoctorName\", " + 
					"	mppr.status as \"status\" " + 
					" from " + 
					"	public.t_outside_hospital_doctor_mapper mppr "
					+ " Where mppr.organization_id= :orgId"
					+ " and mppr.unit_id= :unitId"
					+ " and mppr.outside_hos_doc_id= :id";
			
					List<OutsideHospitalDoctorMapperDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
					.setInteger("orgId", outsideHospitalDoctorMapperDto.getOrganizationId())
					.setInteger("unitId", outsideHospitalDoctorMapperDto.getUnitId())
					.setInteger("id", outsideHospitalDoctorMapperDto.getOutsideHosDocId())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalDoctorMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getOutsideHospitalDoctorMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response searchOutsideHospitalDoctorList(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {
			if(outsideHospitalDoctorMapperDto.getOutsideHospitalId() != null) {
				String query = " SELECT mppr.outside_hos_doc_id as \"outsideHosDocId\" ,"
						+ "	mppr.outside_hospital_id as \"outsideHospitalId\" ,"
						+ "	mppr.hosp_doctor_name as \"outsideHospitalDoctorName\" "
						+ " FROM public.t_outside_hospital_doctor_mapper mppr " 
						+ " WHERE mppr.status='A' "
						+ " AND mppr.outside_hospital_id=:id " 
						 + "AND lower(mppr.hosp_doctor_name) LIKE '%"+outsideHospitalDoctorMapperDto.getSearchDoctor().toString().toLowerCase().trim()+"%' ";
				
				List<OutsideHospitalDoctorMapperDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
						.setInteger("id", outsideHospitalDoctorMapperDto.getOutsideHospitalId())
						.setResultTransformer(Transformers.aliasToBean(OutsideHospitalDoctorMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}else {
				String query = " SELECT mppr.outside_hos_doc_id as \"outsideHosDocId\" ,"
						+ "	mppr.outside_hospital_id as \"outsideHospitalId\" ,mst.outside_hospital_desc as \"outsideHospitalDesc\", "
						+ "	mppr.hosp_doctor_name as \"outsideHospitalDoctorName\" "
						+ " FROM public.t_outside_hospital_doctor_mapper mppr " 
						+ " inner join public.m_outside_hospital_master mst on " + 
						"	mst.outside_hospital_id = mppr.outside_hospital_id "
						+ " WHERE mppr.status='A' "
						 + "AND lower(mppr.hosp_doctor_name) LIKE '%"+outsideHospitalDoctorMapperDto.getSearchDoctor().toString().toLowerCase().trim()+"%' ";
				
				List<OutsideHospitalDoctorMapperDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
						.setResultTransformer(Transformers.aliasToBean(OutsideHospitalDoctorMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}
			
					
			

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("searchOutsideHospitalDoctorList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getAllOutsideHospitalDoctorList(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {
			String query=" select " + 
					"	mppr.outside_hos_doc_id as \"outsideHosDocId\" , " + 
					"	mppr.outside_hospital_id as \"outsideHospitalId\" , " + 
					"	mppr.hosp_doctor_name as \"hospDoctorName\", " + 
					"	mppr.status as \"status\" " + 
					" from " + 
					"	public.t_outside_hospital_doctor_mapper mppr "
					+ " Where mppr.organization_id= :orgId"
					+ " and mppr.unit_id= :unitId";
			
					List<OutsideHospitalDoctorMapperDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
					.setInteger("orgId", outsideHospitalDoctorMapperDto.getOrganizationId())
					.setInteger("unitId", outsideHospitalDoctorMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalDoctorMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getAllOutsideHospitalDoctorList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
}
