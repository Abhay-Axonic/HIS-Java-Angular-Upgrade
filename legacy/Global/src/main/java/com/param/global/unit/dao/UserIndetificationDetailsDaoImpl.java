package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.model.DoctorDocumentMapper;
import com.param.global.model.UserIndetificationDetails;
import com.param.global.model.UserRoleMapper;
import com.param.global.unit.dto.UserIndetificationDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})

public class UserIndetificationDetailsDaoImpl extends GenericDao<UserIndetificationDetails, Integer> implements  IUserIndetificationDetailsDaoImpl , ICommonConstants{

	final static Logger logger = Logger.getLogger(UserIndetificationDetailsDaoImpl.class);

	public UserIndetificationDetailsDaoImpl() {
		super(UserIndetificationDetails.class);

	}
	@Autowired
	SessionFactory SessionFactory;
	@Override
	public Response saveIdentificationDetailsOfUser(UserIndetificationDetailsDto userIndetificationDetailsDto)
			throws ApplicationException {
		try {
			UserIndetificationDetails userIndetificationDetails=new UserIndetificationDetails();
			userIndetificationDetails.setIdentificationTypeId(userIndetificationDetailsDto.getIdentificationTypeId());
			userIndetificationDetails.setIdentificationNumber(userIndetificationDetailsDto.getIdentificationNumber());
			userIndetificationDetails.setEmployeeId(userIndetificationDetailsDto.getEmployeeId());
			userIndetificationDetails.setUserId(userIndetificationDetailsDto.getUserId());
			userIndetificationDetails.setIdentificationExpiryDate(GlobalCommonDateUtils.getDate(userIndetificationDetailsDto.getIdentificationExpiryDate(),"dd-MM-yyyy"));
			userIndetificationDetails.setDoctorIndetificationFile(userIndetificationDetailsDto.getDoctorIndetificationFile());
			userIndetificationDetails.setDoctorId(userIndetificationDetailsDto.getDoctorId());
			userIndetificationDetails.setFileName(userIndetificationDetailsDto.getFileName()!=null ? userIndetificationDetailsDto.getFileName() : null);
			userIndetificationDetails.setCreatedBy(userIndetificationDetailsDto.getCreatedBy());
			userIndetificationDetails.setCreatedDate(TimeZoneComponent.getDateByzone(userIndetificationDetailsDto.getUnitId()));
			userIndetificationDetails.setUpdatedBy(userIndetificationDetailsDto.getUpdatedBy());
			userIndetificationDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(userIndetificationDetailsDto.getUnitId()));
			userIndetificationDetails.setStatus('A');
			userIndetificationDetails.setOrganizationId(userIndetificationDetailsDto.getOrganizationId());
			userIndetificationDetails.setUnitId(userIndetificationDetailsDto.getUnitId());
			save(userIndetificationDetails);
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, userIndetificationDetails);
	} catch (Exception e) {
		throw e;
	}
	}
	@Override
	public Response getListOfDoctorIdentificationDocuumentList(
			UserIndetificationDetailsDto userIndetificationDetailsDto) throws ApplicationException {
		try{
			
			List<UserIndetificationDetailsDto> userIndetificationDetailsDtoList = null;
			
			if(userIndetificationDetailsDto.getDoctorId()==null || userIndetificationDetailsDto.getDoctorId()<0){
				userIndetificationDetailsDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LIST_OF_STAFF_IDENTIFICATION_DOCCUEMENT")
						.setParameter("organizationId", userIndetificationDetailsDto.getOrganizationId())
						.setParameter("unitId", userIndetificationDetailsDto.getUnitId())
						.setParameter("employeeId", userIndetificationDetailsDto.getEmployeeId())
						.setResultTransformer(Transformers.aliasToBean(UserIndetificationDetailsDto.class)).list();
			}else{
				userIndetificationDetailsDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LIST_OF_DOCTOR_IDENTIFICATION_DOCCUEMENT")
						.setParameter("organizationId", userIndetificationDetailsDto.getOrganizationId())
						.setParameter("unitId", userIndetificationDetailsDto.getUnitId())
						.setParameter("doctorId", userIndetificationDetailsDto.getDoctorId())
						.setResultTransformer(Transformers.aliasToBean(UserIndetificationDetailsDto.class)).list();
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, userIndetificationDetailsDtoList, null);
		} catch (Exception e) {
			throw e;
		}
}
	@Override
	public Response updateDoctorIdentificationDetails(UserIndetificationDetailsDto userIndetificationDetailsDto)
			throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();

			String sql = "UPDATE public.t_user_indetification_details " +
					"SET status = :status " +
					"WHERE user_indetification_details_id = :userIndetificationDetailsId " +
					"AND organization_id = :organizationId " +
					"AND unit_id = :unitId";

			Query query = session.createSQLQuery(sql);
			query.setParameter("status", "I"); // Assuming the status value is always 'I'
			query.setParameter("userIndetificationDetailsId", userIndetificationDetailsDto.getUserIndetificationDetailsId());
			query.setParameter("organizationId", userIndetificationDetailsDto.getOrganizationId());
			query.setParameter("unitId", userIndetificationDetailsDto.getUnitId());

			int count = query.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, "Deleted Successfully", null, null);
	} catch (Exception e) {
		throw e;
	}
	}

	@Override
	public Response updateIdentificationDetailsOfUser(UserIndetificationDetailsDto userIndetificationDetailsDto) throws ApplicationException {
		try {
			UserIndetificationDetails userIndetificationDetails=findById(userIndetificationDetailsDto.getUserIndetificationDetailsId());
			userIndetificationDetails.setIdentificationTypeId(userIndetificationDetailsDto.getIdentificationTypeId());
			userIndetificationDetails.setIdentificationNumber(userIndetificationDetailsDto.getIdentificationNumber());
			userIndetificationDetails.setEmployeeId(userIndetificationDetailsDto.getEmployeeId());
			userIndetificationDetails.setIdentificationExpiryDate(GlobalCommonDateUtils.getDate(userIndetificationDetailsDto.getIdentificationExpiryDate(),"dd-MM-yyyy"));
			userIndetificationDetails.setDoctorIndetificationFile(userIndetificationDetailsDto.getDoctorIndetificationFile());
			userIndetificationDetails.setDoctorId(userIndetificationDetailsDto.getDoctorId());
			userIndetificationDetails.setFileName(userIndetificationDetailsDto.getFileName()!=null ? userIndetificationDetailsDto.getFileName() : null);
			update(userIndetificationDetails);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (Exception e) {
		logger.error("updateIdentificationDetailsOfUser failed", e);
	}
	return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

}
