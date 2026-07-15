package com.param.global.unit.controller;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.DoctorInCabin;
import com.param.global.unit.dao.IDoctorInCabinDao;
import com.param.global.unit.dto.DoctorInCabinDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Repository
public class DoctorInCabinDaoImpl extends GenericDao<DoctorInCabin, Integer> implements IDoctorInCabinDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(DoctorInCabinDaoImpl.class);


	public DoctorInCabinDaoImpl() {
		super(DoctorInCabin.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException{
		try {
			DoctorInCabin cabinMaster=new DoctorInCabin();
			cabinMaster.setDoctorId(dto.getDoctorId());
			cabinMaster.setCabinId(dto.getCabinId());
			cabinMaster.setOrganizationId(dto.getOrganizationId());
			cabinMaster.setUnitId(dto.getUnitId());
			cabinMaster.setStatus('A');
			cabinMaster.setCreatedBy(dto.getCreatedBy());
			cabinMaster.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			cabinMaster.setUpdatedBy(dto.getUpdatedBy());
			cabinMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			save(cabinMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveDoctorInCabin failed", he);
		} catch (Exception e) {
		logger.error("saveDoctorInCabin failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactivePreviousDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException {
		try
        {
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update public.t_doctor_in_cabin set status = 'I' where doctor_id = "+dto.getDoctorId())
					.executeUpdate();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
        } catch (Exception e)
        {
        logger.error("inactivePreviousDoctorInCabin failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException{

		try {
			DoctorInCabinDto doctorInCabinDto=(DoctorInCabinDto) sessionFactory.getCurrentSession().
					createSQLQuery("select mcm.cabin_desc as \"cabinName\",concat( mdm.first_name, ' ', mdm.last_name) \"doctorName\" ,tdic.doctor_id \"doctorId\", "
							+ "  array_to_string(array( select qualification_desc from doctor.t_doctor_doc_mapper tddm where doctor_id  = mdm.doctor_id and status = 'A' ),',')   as \"qualificationDesc\"  "
							+ " from m_cabin_master mcm "
							+ " left join 	public.t_doctor_in_cabin tdic  on tdic.cabin_id = mcm.cabin_id and tdic.status  = 'A' "
							+ " left join doctor.m_doctor_master mdm on tdic.doctor_id  = mdm.doctor_id "
							+ " where mcm.cabin_id ="+dto.getCabinId())
					.setResultTransformer(Transformers.aliasToBean(DoctorInCabinDto.class)).uniqueResult();
		 return new Response(SUCCESS, SUCCESS_CODE, null, null, doctorInCabinDto);	
	} catch (HibernateException he) {
	logger.error("getDoctorInCabin failed", he);
	} catch (Exception e) {
	logger.error("getDoctorInCabin failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
}
