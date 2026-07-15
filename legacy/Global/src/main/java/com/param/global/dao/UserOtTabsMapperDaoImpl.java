package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.UserOtTabsMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.UserOtTabsMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class UserOtTabsMapperDaoImpl extends GenericDao<UserOtTabsMapper, Integer>
implements IUserOtTabsMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(UserOtTabsMapperDaoImpl.class);


public UserOtTabsMapperDaoImpl() {
	super(UserOtTabsMapper.class);
}

@Autowired
SessionFactory sessionFactory;


@Override
public Response getUserByRoleType(UserOtTabsMapperDto userOtTabsMapperDto) {
		try {
			
			UserOtTabsMapper userotMstr = new UserOtTabsMapper();
			userotMstr.setCssdKit(userOtTabsMapperDto.getCssdKit());
			userotMstr.setEquipment(userOtTabsMapperDto.getEquipment());
			userotMstr.setInventry(userOtTabsMapperDto.getInventry());
			userotMstr.setNotes(userOtTabsMapperDto.getNotes());
			userotMstr.setOtDetails(userOtTabsMapperDto.getOtDetails());
			userotMstr.setProcedureDetails(userOtTabsMapperDto.getProcedureDetails());
			userotMstr.setService(userOtTabsMapperDto.getService());
			userotMstr.setTimeSheet(userOtTabsMapperDto.getTimeSheet());
			userotMstr.setOrganizationId(userOtTabsMapperDto.getOrganizationId());
			userotMstr.setUserId(userOtTabsMapperDto.getUserId());
			userotMstr.setUnitId(userOtTabsMapperDto.getUnitId());	
			userotMstr.setUpdatedBy(userOtTabsMapperDto.getUpdatedBy());
			userotMstr.setUpdatedDate(TimeZoneComponent.getDateByzone(userOtTabsMapperDto.getUnitId()));
			userotMstr.setStatus('A');
			userotMstr.setCreatedBy(userOtTabsMapperDto.getCreatedBy());
			userotMstr.setCreatedDate(TimeZoneComponent.getDateByzone(userOtTabsMapperDto.getUnitId()));
			userotMstr.setAnesthesiaNotes(userOtTabsMapperDto.getAnesthesiaNotes());
			userotMstr.setDischargeNotes(userOtTabsMapperDto.getDischargeNotes());
			userotMstr.setFollowUpNotes(userOtTabsMapperDto.getFollowUpNotes());
			userotMstr.setImplant(userOtTabsMapperDto.getImplant());
			userotMstr.setMedicalGas(userOtTabsMapperDto.getMedicalGas());
			userotMstr.setMedication(userOtTabsMapperDto.getMedication());
			userotMstr.setSurgeryNotes(userOtTabsMapperDto.getSurgeryNotes());
			if(userOtTabsMapperDto.getUserOtTabMapperId()!=null){
				
				userotMstr.setUserOtTabMapperId(userOtTabsMapperDto.getUserOtTabMapperId());
				update(userotMstr);
				
			}else {
				
				save(userotMstr);
			}
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
	logger.error("getUserByRoleType failed", he);
	} catch (Exception e) {
	logger.error("getUserByRoleType failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


@Override
public Response getUserOtTabsByUserId(Integer userId) throws ApplicationException {
	try {
		
		List<UserOtTabsMapperDto> userRoleNameList = sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT  userOtTabsMapper.user_ot_tab_mapper_id as \"userOtTabMapperId\","
						+ " userOtTabsMapper.user_id as \"userId\","
						+ " userOtTabsMapper.ot_details as \"otDetails\","
						+ " userOtTabsMapper.procedure_details as \"procedureDetails\","
						+ " userOtTabsMapper.service as \"service\","
						+ " userOtTabsMapper.inventry as \"inventry\","
						+ " userOtTabsMapper.equipment as \"equipment\","
						+ " userOtTabsMapper.cssd_kit as \"cssdKit\","
						+ " userOtTabsMapper.notes as \"notes\","
						+ " userOtTabsMapper.medication as \"medication\","
						+ " userOtTabsMapper.implant as \"implant\","
						+ " userOtTabsMapper.medical_gas as \"medicalGas\","
						+ " userOtTabsMapper.anesthesia_notes as \"anesthesiaNotes\","
						+ " userOtTabsMapper.surgery_notes as \"surgeryNotes\","
						+ " userOtTabsMapper.discharge_notes as \"dischargeNotes\","
						+ " userOtTabsMapper.follow_up_notes as \"followUpNotes\","
						+ " userOtTabsMapper.time_sheet as \"timeSheet\" "
						+ " FROM public.t_user_ot_tab_mapper userOtTabsMapper "
						+ " WHERE  UserOtTabsMapper.user_id =:userId ")
				.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(UserOtTabsMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, userRoleNameList, null);
		
	} catch (HibernateException he) {
	logger.error("getUserOtTabsByUserId failed", he);
	} catch (Exception e) {
	logger.error("getUserOtTabsByUserId failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);}
}
