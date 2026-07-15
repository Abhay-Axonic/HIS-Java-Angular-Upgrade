package com.param.global.dao;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.City;
import com.param.entity.model.master.Country;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.State;
import com.param.entity.model.master.Vendor;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.sync.DoctorSyncDto;
import com.param.global.model.DoctorDetails;
import com.param.global.model.DoctorMaster;
import com.param.global.model.DoctorSpecialityMapper;
import com.param.global.model.DoctorSubSplecialityMapper;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.utility.UserComponent;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DoctorMasterDaoImpl extends GenericDao<DoctorMaster, Integer> implements ICommonConstants, IDoctorMasterDao{

	private static final Logger logger = Logger.getLogger(DoctorMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private IUserMasterDao iUserMasterDao;
	 
	public DoctorMasterDaoImpl() {
		super(DoctorMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getDoctorMasterList(DoctorMasterDto doctorMasterDto)throws ApplicationException {
		try
		{
		
			List<DoctorMasterDto> listDoctorMasterDto = sessionFactory.getCurrentSession().getNamedQuery("AUTO_FILL_SEARCH_FOR_DOCTOR_NAME")
//					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("doctorName", "%" + doctorMasterDto.getDoctorName().toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDoctorMasterDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
			logger.error("Error while fetching doctor master list", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response checkUniqueDoctor(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	@Override
	public Response saveDoctorRegistration(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			DoctorMaster doctorMaster = mapper.map(doctorMasterDto,DoctorMaster.class,
					"DoctorMasterDto_to_DoctorMaster");
			doctorMaster=save(doctorMaster);
			Integer dId=doctorMaster.getDoctorId();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dId);
		}catch (Exception e) {
			logger.error("Error while saving doctor registration", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorDetails(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			DoctorDetails doctorDetails = mapper.map(doctorMasterDto,DoctorDetails.class,
					"DoctorMasterDto_to_DoctorDetails");
			doctorDetails.setStatus('A');
			Integer doctorDetailsId=(Integer) sessionFactory.getCurrentSession().save(doctorDetails);
			doctorMasterDto.setDoctorDetailsId(doctorDetailsId);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, doctorMasterDto);
		}catch (Exception e) {
			logger.error("Error while saving doctor details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorDetails(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			List<DoctorMasterDto> doctorMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_DETAILS")
					.setParameter("organizationId", doctorMasterDto.getOrganizationId())
					//.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		} catch (Exception e) {
			logger.error("Error while fetching doctor details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorDetails(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
				DoctorMaster doctorMaster = mapper.map(doctorMasterDto,DoctorMaster.class,
						"DoctorMasterDto_to_DoctorMaster");
				session.update(doctorMaster);
				
				DoctorDetails docDetails = mapper.map(doctorMasterDto,DoctorDetails.class,
						"DoctorMasterDto_to_DoctorDetails");
			docDetails.setStatus('A');
				session.update(docDetails);
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
				logger.error("Error while updating doctor details", he);
			} catch (Exception e) {
				logger.error("Error while updating doctor details", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorStatus(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorMaster emp set emp.status=:status, "
						+ "emp.updatedBy=:updatedBy, "
						+ "emp.updatedDate=:updatedDate "
						+ "WHERE emp.doctorId=:doctorId ")
				.setParameter("updatedBy", doctorMasterDto.getUpdatedBy())
				.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorMasterDto.getUpdatedDate(), "yyyy-M-dd"))
				.setParameter("doctorId", doctorMasterDto.getDoctorId())
				.setParameter("status", doctorMasterDto.getStatus())
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorDetails emp set emp.status=:status, "
						+ "emp.updatedBy=:updatedBy, "
						+ "emp.updatedDate=:updatedDate "
						+ "WHERE emp.doctorId=:doctorId ")
				.setParameter("updatedBy", doctorMasterDto.getUpdatedBy())
				.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorMasterDto.getUpdatedDate(), "yyyy-M-dd"))
				.setParameter("doctorId", doctorMasterDto.getDoctorId())
				.setParameter("status", doctorMasterDto.getStatus())
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery("UPDATE DependentDetails depDet set depDet.status=:status, "
					+ "depDet.updatedBy=:updatedBy, "
					+ "depDet.updatedDate=:updatedDate "
					+ "WHERE depDet.empDocId=:doctorId "
					+ "AND depDet.typeId=:typeId ")
				.setParameter("updatedBy", doctorMasterDto.getUpdatedBy())
				.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorMasterDto.getUpdatedDate(), "yyyy-M-dd"))
				.setParameter("doctorId", doctorMasterDto.getDoctorId())
				.setParameter("typeId", doctorMasterDto.getTypeId())
				.setCharacter("status", doctorMasterDto.getStatus())
			.executeUpdate();
			
			UserMasterDto userMasterDtos = new UserMasterDto();
            userMasterDtos.setUserId(doctorMasterDto.getUserId());		
	    	userMasterDtos.setOrganizationId(doctorMasterDto.getOrganizationId());		
	    	userMasterDtos.setUnitId(doctorMasterDto.getUnitId());		
            		
			Response loginResponse = iUserMasterDao.getUserDataByIdNew(userMasterDtos);
			
			 if(doctorMasterDto.getStatus() == 'I') {
				if (loginResponse.getStatus().equals(SUCCESS)) {
					 UserMasterDto userMasterDto = new UserMasterDto();
					 userMasterDto = (UserMasterDto) loginResponse.getObject();
					 userMasterDto.setStatus("I");
					 UserComponent.updateUserDetailsForAuth(userMasterDto);
				}
			 }
			 
			if(doctorMasterDto.getUserId() != null && doctorMasterDto.getUserId() > 0){
				sessionFactory.getCurrentSession().createQuery("UPDATE UserMaster userMst set userMst.status=:status "
						+ "WHERE userMst.userId=:userId ")
				.setParameter("userId", doctorMasterDto.getUserId())
				.setParameter("status", doctorMasterDto.getStatus().toString())
				.executeUpdate();
				}
			
			 if(doctorMasterDto.getStatus() == 'A') {
					if (loginResponse.getStatus().equals(SUCCESS)) {
						 UserMasterDto userMasterDto = new UserMasterDto();
						 userMasterDto = (UserMasterDto) loginResponse.getObject();
						 userMasterDto.setStatus("A");
						 UserComponent.updateUserDetailsForAuth(userMasterDto);
					}
			 }
			    
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
				logger.error("Error while updating doctor status", he);
			} catch (Exception e) {
				logger.error("Error while updating doctor status", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorSpecialityMapper(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			for(DoctorSpecialityMapperDto dto : doctorMasterDto.getDoctorSpecialityMapperDtosList())
			{
				DoctorSpecialityMapper doctorSpecialityMapper = new DoctorSpecialityMapper();
					doctorSpecialityMapper.setSpecialityId(dto.getSpecialityId());
					doctorSpecialityMapper.setDoctorId(doctorMasterDto.getDoctorId());
					doctorSpecialityMapper.setCreatedBy(dto.getCreatedBy());
					doctorSpecialityMapper.setUpdatedBy(dto.getUpdatedBy());
					doctorSpecialityMapper.setStatus('A');
					doctorSpecialityMapper.setCreatedDate(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()));
					doctorSpecialityMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()));
				sessionFactory.getCurrentSession().save(doctorSpecialityMapper);
				
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);

			} catch (HibernateException he) {
				logger.error("Error while saving doctor speciality mapper", he);
			} catch (Exception e) {
				logger.error("Error while saving doctor speciality mapper", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchDoctorDetailsByCriteria(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			StringBuilder rawQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_DETAILS").getQueryString());
			
			if(doctorMasterDto.getDoctorName()!=null && !doctorMasterDto.getDoctorName().isEmpty())
				rawQuery.append(" AND lower(concat(coalesce(doctor.firstName),coalesce(doctor.lastName))) LIKE concat('%',LOWER(:doctorName),'%') ");
			if(doctorMasterDto.getDocCategoryId()>0)
				rawQuery.append(" AND doctor.docCategoryId=:docCategoryId ");
			if(doctorMasterDto.getDocDesignationId()>0)
				rawQuery.append(" AND doctor.docDesignationId=:docDesignationId");
			if(doctorMasterDto.getSpecialityId()>0){
				rawQuery.append(" AND doctor.doctorId IN (select doctorId from DoctorSpecialityMapper where status='A' AND specialityId =:specialityId ) ");
			}
			if(doctorMasterDto.getDocCode()!=null && !doctorMasterDto.getDocCode().isEmpty())
				rawQuery.append(" AND doctor.docCode=:docCode ");
			if(doctorMasterDto.getIdentificationTypeId()>0)
				rawQuery.append(" AND doctor.identificationTypeId=:identificationTypeId ");
			if(doctorMasterDto.getIdentificationNumber()!=null && !doctorMasterDto.getIdentificationNumber().isEmpty())
				rawQuery.append(" AND lower(doctor.identificationNumber) LIKE concat('%',LOWER(:identificationNumber),'%')");
			
			List<DoctorMasterDto> doctorRegistrationDtosList;

			  Query createQQ = sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", doctorMasterDto.getOrganizationId())
						.setFirstResult(doctorMasterDto.getOffset() != null ? doctorMasterDto.getOffset() : 0)
						.setMaxResults(doctorMasterDto.getNoOfRecordsPerPage() != null ? doctorMasterDto.getNoOfRecordsPerPage() : 10);
				if(doctorMasterDto.getDoctorName()!=null && !doctorMasterDto.getDoctorName().isEmpty())
					createQQ.setParameter("doctorName",doctorMasterDto.getDoctorName());
				if(doctorMasterDto.getDocCategoryId()>0)
					createQQ.setParameter("docCategoryId",doctorMasterDto.getDocCategoryId());
				if(doctorMasterDto.getDocDesignationId()>0)
					createQQ.setParameter("docDesignationId",doctorMasterDto.getDocDesignationId());
				if(doctorMasterDto.getSpecialityId()>0){
					createQQ.setParameter("specialityId",doctorMasterDto.getSpecialityId());
				}
				if(doctorMasterDto.getDocCode()!=null && !doctorMasterDto.getDocCode().isEmpty())
					createQQ.setParameter("docCode",doctorMasterDto.getDocCode());
				if(doctorMasterDto.getIdentificationTypeId()>0)
					createQQ.setParameter("identificationTypeId",doctorMasterDto.getIdentificationTypeId());
				if(doctorMasterDto.getIdentificationNumber()!=null && !doctorMasterDto.getIdentificationNumber().isEmpty())
					createQQ.setParameter("identificationNumber",doctorMasterDto.getIdentificationNumber());

				doctorRegistrationDtosList =createQQ.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, doctorRegistrationDtosList, null);
		} catch (Exception e) {
			logger.error("Error while searching doctor details by criteria", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorForSyncById(Integer docId) throws ApplicationException {
		try{
			List<DoctorSyncDto> doctorUnitSpecialityList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTORDETAILS_FOR_SYNC_BY_ID").setParameter("id", docId)
					.setResultTransformer(Transformers.aliasToBean(DoctorSyncDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorUnitSpecialityList, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDoctorImage(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		 try{
		      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_DOCTOR_IMAGE_PATH")
		          .setParameter("orgId", doctorMasterDto.getOrganizationId())
		          .setParameter("doctorId", doctorMasterDto.getDoctorId())
		          .setParameter("doctorImage", doctorMasterDto.getDoctorImage()).executeUpdate();
		      if(result != null){
		          return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		      }
		              
		  }catch(HibernateException he){
			logger.error("Error while updating doctor image", he);
		  }catch(Exception e){
			logger.error("Error while updating doctor image", e);
		  }
		  return new Response<>(ERROR, ERROR_CODE, null, null, null);
		  }

	@Override
	public Response updateDoctorSpecialityMapper(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorSpecialityMapper set status='I', "
					+ "updatedBy="+doctorMasterDto.getUpdatedBy()+" "
					+ "where doctorId="+doctorMasterDto.getDoctorId()).executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorSubSplecialityMapper set status='I', "
					+ "updatedBy="+doctorMasterDto.getUpdatedBy()+" "
					+ "where doctorId="+doctorMasterDto.getDoctorId()).executeUpdate();
			
			saveDoctorSpecialityMapper(doctorMasterDto);
			saveDoctorSubSpecialityMapper(doctorMasterDto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			logger.error("Error while updating doctor speciality mapper", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorNameById(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			
			Integer id=doctorMasterDto.getDoctorId();
			String fname= (String) sessionFactory.getCurrentSession().createSQLQuery(
					"select first_name as \"firstName\" "
							+ "from doctor.m_doctor_master "
							+ "where doctor_id=:id "
						)
					.setParameter("id",id)
					.uniqueResult();
			
			String doctorName= (String) sessionFactory.getCurrentSession().createSQLQuery(
					"select  concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\" " 
							+ " from doctor.m_doctor_master doc "
							+ " where doc.doctor_id=:id "
						)
					.setParameter("id",id)
					.uniqueResult();
			
			DoctorMasterDto doctorMasterDto1= new DoctorMasterDto();
			doctorMasterDto1.setFirstName(fname);
			doctorMasterDto1.setDoctorName(doctorName);
			 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, doctorMasterDto1);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getActiveDocList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			List<DoctorMasterDto> doctorMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_LIST")
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		} catch (Exception e) {
			logger.error("Error while fetching active doctor list", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRMODoctorList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			List<DoctorMasterDto> rmoDoctorMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_RMO_DOCTOR_LIST")
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, rmoDoctorMasterList, null);
		} catch (Exception e) {
			logger.error("Error while fetching RMO doctor list", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response validateDoctorPin(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{

			BigInteger doctorPinCount;
			Integer doctorPinCountInt = 0;
			DoctorMasterDto doctorMasterDto1=new DoctorMasterDto();
			if(doctorMasterDto.getDoctorId()==null){
				doctorPinCount =(BigInteger)sessionFactory.getCurrentSession()
						 .createSQLQuery(" select COUNT(*) from doctor.m_doctor_master where doctor_pin=:pin "
										 + " and  organization_id=:orgId "
						 )
						 .setParameter("pin", doctorMasterDto.getDoctorPin())
						 .setParameter("orgId", doctorMasterDto.getOrganizationId())
						 .uniqueResult();
			}else{
				doctorPinCount =(BigInteger)sessionFactory.getCurrentSession()
						 .createSQLQuery(" select COUNT(*) from doctor.m_doctor_master where doctor_pin=:pin "
										 + " and doctor_id!=:doctorId and organization_id=:orgId "
						 )
						 .setParameter("doctorId", doctorMasterDto.getDoctorId())
						 .setParameter("pin", doctorMasterDto.getDoctorPin())
						 .setParameter("orgId", doctorMasterDto.getOrganizationId())
						 .uniqueResult();
			}
			doctorPinCountInt = doctorPinCount.intValue();
			if(doctorPinCountInt!=0)
				return new Response(ERROR, ERROR_CODE, "Pin Already in use", null, doctorMasterDto1);
			else
				return new Response(SUCCESS, SUCCESS_CODE, "available", null, null);


		} catch (Exception e) {
			logger.error("Error while validating doctor pin", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response validateDoctorPinByDoctorId(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			DoctorMasterDto doctorMasterDto1 =(DoctorMasterDto)sessionFactory.getCurrentSession()
					.createQuery(" select doctorPin as doctorPin from DoctorMaster where doctorPin=:pin "
							+ "and organizationId=:orgId "
							+ "and doctorId=:docId ")
					.setParameter("pin", doctorMasterDto.getDoctorPin())
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					//.setParameter("unitId", doctorMasterDto.getUnitId())
					.setParameter("docId", doctorMasterDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();
			if(doctorMasterDto1!=null)
				return new Response(SUCCESS, SUCCESS_CODE, "Pin verified", null, doctorMasterDto1);
			else
				return new Response(ERROR, ERROR_CODE, "Incorrect Pin", null, null);

		} catch (Exception e) {
			logger.error("Error while validating doctor pin by doctor id", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDocotorListForAppoitments(Integer unitId, Integer specialityId) throws ApplicationException {
		try{
			
			String query =  " select " 
					+ " 	doc.doctor_id as \"doctorId\", " 
					+ " 	docSplMpr.doctor_speciality_id as \"doctorSpecialityId\", " 
					+ " 	coalesce(doc.first_name, '') as \"firstName\", " 
					+ " 	coalesce(doc.last_name, '') as \"lastName\", " 
					+ " 	docSplMpr.speciality_id as \"specialityId\", " 
					+ " 	spc.speciality_name as \"specialityName\" "
					+ "      ,doc.ai_doctor as \"aiDoctor\" "
					+ " from " 
					+ " 	doctor.t_doctor_speciality_mapper docSplMpr " 
					+ " inner join doctor.m_doctor_master doc on " 
					+ " 	doc.doctor_id = docSplMpr.doctor_id " 
					+ " inner join doctor.m_speciality_master spc on " 
					+ " 	spc.speciality_id = docSplMpr.speciality_id " 
					+ " inner join t_user_unit_mapper uum on " 
					+ " 	uum.user_id = doc.user_id " 
					+ " where " 
					+ " 	doc.status = 'A' " 
					+ " 	and docSplMpr.status = 'A' " 
					+ " 	and uum.unit_id =:unitId " 
					+ " 	and uum.status = 'A' " 
					+ " 	and docSplMpr.speciality_id =:specilaityId " 
					+ " order by "
					+ " 	doc.first_name ASC" ;
			
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameter("specilaityId", specialityId)
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (Exception e) {
			logger.error("Error while fetching doctor list for appointments", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorCount(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(DoctorMaster.class); 
			c.add(Restrictions.eq("organizationId", doctorMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("Error while fetching doctor count", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getStaffListByRoleId(UserRoleMasterDto dto) throws ApplicationException {
		try{
			List<UserMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STAFF_LIST_BY_ROLE_ID")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("organizationId", dto.getOrganisationId())
					.setParameter("roleId", dto.getRoleId())
					.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (Exception e) {
			logger.error("Error while fetching staff list by role id", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response doctorDetailsUniversalSearch(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			StringBuilder rawQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("UNIVERSAL_SEARCH_FOR_DOCTOR_DETAILS").getQueryString());
					List<DoctorMasterDto> doctorRegistrationDtosList;
			
			if(doctorMasterDto.getIsPatientSearch()!=null && doctorMasterDto.getIsPatientSearch()=='Y') {
			 doctorRegistrationDtosList = sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", doctorMasterDto.getOrganizationId())
						.setParameter("searchKeyword", "%"+ doctorMasterDto.getDoctorName().trim().toLowerCase()+"%")
						.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			}else {
				 doctorRegistrationDtosList = sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", doctorMasterDto.getOrganizationId())
						.setParameter("searchKeyword", "%"+ doctorMasterDto.getDoctorName().trim().toLowerCase()+"%")
						.setFirstResult(doctorMasterDto.getOffset() != null ? doctorMasterDto.getOffset() : 0)
						.setMaxResults(doctorMasterDto.getNoOfRecordsPerPage() != null ? doctorMasterDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			}
		
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorRegistrationDtosList, null);
		} catch (Exception e) {
			logger.error("Error while performing doctor details universal search", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveDoctorSpecialityMapper(Integer doctorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getPlainDocList(DoctorMasterDto doctorMasterDto) {
		try{
			List<DoctorMasterDto> doctorMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PLAIN_ACTIVE_DOCTOR_LIST")
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		} catch (Exception e) {
			logger.error("Error while fetching plain doctor list", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorSubSpecialityMapper(DoctorMasterDto doctorMasterDto) {
		try{
			for(DoctorSpecialityMapperDto dto : doctorMasterDto.getDoctorSubSpecialityMapperDtosList())
			{
				DoctorSubSplecialityMapper doctorSpecialityMapper = new DoctorSubSplecialityMapper();
					doctorSpecialityMapper.setSubSpecialityId(dto.getSubSpecialityId());
					doctorSpecialityMapper.setDoctorId(doctorMasterDto.getDoctorId());
					doctorSpecialityMapper.setCreatedBy(dto.getCreatedBy());
					doctorSpecialityMapper.setUpdatedBy(dto.getUpdatedBy());
					doctorSpecialityMapper.setStatus('A');
					doctorSpecialityMapper.setCreatedDate(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()));
					doctorSpecialityMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(doctorMasterDto.getUnitId()));
				sessionFactory.getCurrentSession().save(doctorSpecialityMapper);
				
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			
			} catch (HibernateException he) {
			logger.error("saveDoctorSubSpecialityMapper failed", he);
			} catch (Exception e) {
			logger.error("saveDoctorSubSpecialityMapper failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListBySpecialityId(DoctorSpecialityMapperDto doctorSpecialityMapper) {
		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery("select distinct doctorspec0_.doctor_speciality_id as \"doctorSpecialityId\", " 
							+" 	coalesce(doctormast1_.first_name, '') as \"firstName\", " 
							+" 	('Dr. ' || coalesce(doctormast1_.first_name, '')|| ' ' || coalesce(doctormast1_.middle_name, '')|| ' ' || coalesce(doctormast1_.last_name, '')) as \"doctorName\", " 
							+" 	doctormast1_.last_name as \"lastName\", " 
							+" 	doctorspec0_.speciality_id as \"specialityId\", " 
							+" 	speciality2_.speciality_name as \"specialityName\", " 
							+" 	 doctormast1_.doctor_id as \"doctorId\", " 
							+" 	doctormast1_.user_id as \"userId\"" 
							+" from \r\n"  
							+" 	doctor.t_doctor_speciality_mapper doctorspec0_ " 
							+" inner join doctor.m_doctor_master doctormast1_ on "  
							+" 	doctorspec0_.doctor_id = doctormast1_.doctor_id "  
							+" inner join t_user_unit_mapper uum on " 
							+" 	uum.user_id = doctormast1_.user_id	 " 
							+" inner join doctor.m_speciality_master speciality2_ on " 
							+" 	doctorspec0_.speciality_id = speciality2_.speciality_id " 
							+" inner join opd.m_slot_master sm on sm.doctor_id=doctormast1_.doctor_id " 
							+" and to_char(sm.appointment_date,'YYYY-MM-DD')> '"+doctorSpecialityMapper.getAppointmentDate()+"' "
							+" where "  
							+" 	doctorspec0_.speciality_id =:specilaityId " 
							+" 	and doctormast1_.status = 'A' "  
							+" 	and doctorspec0_.status = 'A' " 
							+" 	and doctormast1_.organization_id =:organizationId " 
							+" 	and uum.unit_id =:unitId " 
							+"	AND uum.status='A' " 
							+"	and speciality2_.dept_wise_appointment='N' " 
							+" union " 
							+" select distinct doctorspec0_.doctor_speciality_id as \"doctorSpecialityId\", " 
							+" 	coalesce(doctormast1_.first_name, '') as \"firstName\", " 
							+" 	('Dr. ' || coalesce(doctormast1_.first_name, '')|| ' ' || coalesce(doctormast1_.middle_name, '')|| ' ' || coalesce(doctormast1_.last_name, '')) as \"doctorName\", " 
							+" 	doctormast1_.last_name as \"lastName\", " 
							+" 	doctorspec0_.speciality_id as \"specialityId\", " 
							+" 	speciality2_.speciality_name as \"specialityName\", " 
							+" 	 doctormast1_.doctor_id as \"doctorId\", " 
							+" 	doctormast1_.user_id as \"userId\"" 
							+" from \r\n"  
							+" 	doctor.t_doctor_speciality_mapper doctorspec0_ " 
							+" inner join doctor.m_doctor_master doctormast1_ on "  
							+" 	doctorspec0_.doctor_id = doctormast1_.doctor_id "  
							+" inner join t_user_unit_mapper uum on " 
							+" 	uum.user_id = doctormast1_.user_id	 " 
							+" inner join doctor.m_speciality_master speciality2_ on " 
							+" 	doctorspec0_.speciality_id = speciality2_.speciality_id " 
							+" where " 
							+" 	doctorspec0_.speciality_id =:specilaityId " 
							+" 	and doctormast1_.status = 'A' "  
							+" 	and doctorspec0_.status = 'A' " 
							+" 	and doctormast1_.organization_id =:organizationId " 
							+" 	and uum.unit_id =:unitId " 
							+"	AND uum.status='A' " 
							+"	and speciality2_.dept_wise_appointment='Y' " 
							+"  ORDER BY \"doctorName\" ASC"
							)
					.setParameter("organizationId", doctorSpecialityMapper.getOrganizationId())
					.setParameter("unitId", doctorSpecialityMapper.getUnitId())
					.setParameter("specilaityId", doctorSpecialityMapper.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {
		logger.error("getDoctorListBySpecialityId failed", he);
		} catch (Exception e) {
		logger.error("getDoctorListBySpecialityId failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDocListWithSpeciality(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			List<DoctorMasterDto> doctorMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_LIST_WITH_SPECIALTY")
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
		} catch (Exception e) {
		logger.error("getActiveDocListWithSpeciality failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateGlobalDocId(Integer doctorId, int globalDocId) {
		try{
				sessionFactory.getCurrentSession()
					.createSQLQuery("update doctor.m_doctor_master set global_doc_id=:globalDocId where doctor_id=:doctorId ")
					.setParameter("doctorId", doctorId)
					.setParameter("globalDocId", globalDocId)
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("updateGlobalDocId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Integer getGlobalDocId(Integer doctorId) {
		try{
				Integer globalDocId = (Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select global_doc_id from doctor.m_doctor_master where doctor_id=:doctorId ")
					.setParameter("doctorId", doctorId)
					.uniqueResult();
			
			return globalDocId;
		} catch (Exception e) {
		logger.error("getGlobalDocId failed", e);
		}
		return null;
	}

	@Override
	public Response checkDoctorRegNoAvailability(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			
			String message = "";
			DoctorMasterDto doctorMasterDto1=new DoctorMasterDto();
			if(doctorMasterDto.getDoctorId()==null){
		         doctorMasterDto1 =(DoctorMasterDto)sessionFactory.getCurrentSession()
		        		 .createQuery(" select docMas.registrationNumber as registrationNumber from DoctorMaster docMas "
									+ " where docMas.registrationNumber=:regNo "
									+ "and docMas.organizationId=:orgId ")
					.setParameter("regNo", doctorMasterDto.getRegistrationNumber())
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();
			}else{
				 doctorMasterDto1 =(DoctorMasterDto)sessionFactory.getCurrentSession()
							.createQuery(" select docMas.registrationNumber as registrationNumber from DoctorMaster docMas "
										+ " where docMas.registrationNumber=:regNo "
										+ " and docMas.doctorId!=:doctorId "
										+ " and docMas.organizationId=:orgId ")
							.setParameter("doctorId", doctorMasterDto.getDoctorId())
							.setParameter("regNo", doctorMasterDto.getRegistrationNumber())
							.setParameter("orgId", doctorMasterDto.getOrganizationId())
							.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();
			}
			String doctorName = null;
			String Global_Scheduling_URI = null;
			Response globalRes=isheduleDao.getConnectionStringForMyLife();
			MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)globalRes.getObject();
			if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
			{
				Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink(); 
				URI url = URI.create(Global_Scheduling_URI + "/checkDoctorRegNoAvailability/"+doctorMasterDto.getRegistrationNumber().toLowerCase());
				String stringResponse = restTemplate.getForObject(url, String.class);
				Response commonResponse = gson.fromJson(stringResponse, Response.class);
				doctorName = (String) commonResponse.getObject();
			}
			
			
			if(doctorMasterDto1!=null || doctorName!=null)
			{
				if(doctorName!=null)
					message = "Registration number already in use for Dr. "+doctorName+", Do you want to continue ?";
				else
					message = "Registration number already in use, Do you want to continue ?";
				
				return new Response(ERROR, ERROR_CODE, message, null, doctorName);
			}
				
			else
				return new Response(SUCCESS, SUCCESS_CODE, "available", null, null);
			
			
		} catch (Exception e) {
		logger.error("checkDoctorRegNoAvailability failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListForSwapDoctor(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			List<DoctorMasterDto> doctorMasterDtosListSwap = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_LIST_FOR_SWAP_DOCTOR")
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosListSwap, null);
		} catch (Exception e) {
		logger.error("getDoctorListForSwapDoctor failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorsForWebAppointment(Integer unitId, Integer specialityId, Boolean flag)
			throws ApplicationException {
		try{
            //flag true means get all doctor and false means get doctors mapped with speciality			
			String query =  " select " 
					+ " 	doc.doctor_id as \"doctorId\", " 
					+ " 	coalesce(doc.first_name, '') as \"firstName\", "
					+ " 	coalesce(doc.last_name, '') as \"lastName\", " 
					+ " 	docSplMpr.speciality_id as \"specialityId\", " 
					+ " 	spc.speciality_name as \"specialityName\","
					+ "     doc.doctor_image as \"doctorImage\","
					+ "     doc.user_id as \"userId\" " 
					+ " from " 
					+ " 	doctor.t_doctor_speciality_mapper docSplMpr " 
					+ " inner join doctor.m_doctor_master doc on " 
					+ " 	doc.doctor_id = docSplMpr.doctor_id " 
					+ " inner join doctor.m_speciality_master spc on " 
					+ " 	spc.speciality_id = docSplMpr.speciality_id " 
					+ " inner join t_user_unit_mapper uum on " 
					+ " 	uum.user_id = doc.user_id " 
					+ " where " 
					+ " 	doc.status = 'A' " 
					+ " 	and docSplMpr.status = 'A' " 
					+ " 	and uum.unit_id =:unitId " 
					+ " 	and uum.status = 'A' " 
					+ " 	and (docSplMpr.speciality_id =:specilaityId or "+ flag+")"
					+ " order by "
					+ " 	doc.first_name " ;
			
			List<DoctorMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameter("specilaityId", specialityId)
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);
			
		} catch (Exception e) {
		logger.error("getDoctorsForWebAppointment failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorRegistrationNumber(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			String query =  " select  mdm.registration_number  as \"registrationNumber\" " 
					+ " from doctor.t_doctor_doc_mapper mdm " 
					+ " where mdm.doctor_id = :doctorId and "
					+ " mdm.unit_id =:unitId and mdm.organization_id =:orgId and mdm.status ='A' " 
					+ " order by mdm.doctor_doc_id  desc limit 1 " ;
			
			DoctorMasterDto dto = (DoctorMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setParameter("doctorId", doctorMasterDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, dto);
			
		} catch (Exception e) {
		logger.error("getDoctorRegistrationNumber failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorMasterList1(DoctorMasterDto doctorMasterDto)throws ApplicationException {
		try
		{
			String query="select " 
					 + " 	distinct(concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) as \"doctorName\", " 
					 + " 	doc.doctor_id as \"doctorId\",   "
					 + " 0 as \"doctorType\"  "
					 + " from " 
					 + " 	doctor.t_doctor_speciality_mapper doctSpemppr " 
					 + " inner join doctor.m_doctor_master doc on " 
					 + " 	doctSpemppr.doctor_id = doc.doctor_id " 
					 + " inner join t_user_unit_mapper uum on " 
					 + " 	uum.user_id = doc.user_id " 
					 + " inner join doctor.m_speciality_master spl on "
					 + " 	spl.speciality_id = doctSpemppr.speciality_id "	
					 + " where " 
					 + " 	doc.organization_id =:orgId " 
					 + " 	and doc.status = 'A' "
					 + " 	and doctSpemppr.status = 'A' "
					 + "	AND uum.status='A' " 
					 + " union 	"  
					 + " select distinct(concat(coalesce(doc.doctor_name))) as \"doctorName\", "  
					 + "	doc.id as \"doctorId\", "  
					 + " 1 as \"doctorType\" "
					 + " from "  
					 + "	pharmacy.t_temp_doctor doc "  
					 + " where " 
					 + "	doc.organization_id =:orgId " ; 
			List<DoctorMasterDto> listDoctorMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", doctorMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDoctorMasterDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getDoctorMasterList1 failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getDoctorListByOrgId(DoctorSpecialityMapperDto doctorSpecialityMapper) throws ApplicationException {

		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(" select  " + 
							"	distinct docspec.doctor_speciality_id as \"doctorSpecialityId\", " + 
							"	coalesce(doc.first_name, '') as \"firstName\", " + 
							"	('Dr. ' || coalesce(doc.first_name, '')|| ' ' || coalesce(doc.middle_name, '')|| ' ' || coalesce(doc.last_name, '')) as \"doctorName\", " + 
							"	doc.last_name as \"lastName\", " + 
							"	docspec.speciality_id as \"specialityId\", " + 
							"	scpe.speciality_name as \"specialityName\", " + 
							"	doc.doctor_id as \"doctorId\", " + 
							"	doc.user_id as \"userId\", " + 
							"	tusm.dept_wise_appointment as \"deptWiseAppointment\" " + 
							"	from public.t_unit_speciality_mapper tusm  " + 
							"	inner join doctor.t_doctor_speciality_mapper docspec on docspec.speciality_id = tusm .speciality_id  " + 
							"	inner join doctor.m_doctor_master doc on docspec.doctor_id = doc.doctor_id " + 
							"	inner join doctor.m_speciality_master scpe on docspec.speciality_id = scpe.speciality_id "
							+ " inner join  t_user_unit_mapper uum on uum.user_id = doc.user_id " + 
							"	where tusm.unit_id  = :unitId and tusm.organization_id  = :organizationId and 	doc.status = 'A' and uum.unit_id = :unitId  " + 
							"	and docspec.status = 'A' " 
							)
					.setParameter("organizationId", doctorSpecialityMapper.getOrganizationId())
					.setParameter("unitId", doctorSpecialityMapper.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {
		logger.error("getDoctorListByOrgId failed", he);
		} catch (Exception e) {
		logger.error("getDoctorListByOrgId failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorMasterLedgerId(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("UPDATE doctor.m_doctor_master  set general_ledger_id=:generalLedgerId "
						+ "WHERE doctor_id=:doctorId ")
				.setParameter("doctorId", doctorMasterDto.getDoctorId())
				.setParameter("generalLedgerId", doctorMasterDto.getGeneralLedgerId())
			.executeUpdate();
			
			} catch (HibernateException he) {
			logger.error("updateDoctorMasterLedgerId failed", he);
			} catch (Exception e) {
			logger.error("updateDoctorMasterLedgerId failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorDetailsEncrypted(DoctorMasterDto doctorMasterDto) {
		try{
			String query =  " select mobile_number as \"mobileNumber\" ,  " +
					"first_name  as \"firstName\"  " +
					"from doctor.m_doctor_master mdm   " +
					"where   user_id  = :userID " ;

			DoctorMasterDto dto = (DoctorMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("userID", doctorMasterDto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();

			String query1 =  " select pms_url as \"pmsUrl\" " +
					" from public.hl7_config where is_pms ='PMS' and unit_id  = :unitId " ;

			DoctorMasterDto dto1 = (DoctorMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query1)
					.setParameter("unitId", doctorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();
			dto.setPmsUrl(dto1 != null ? dto1.getPmsUrl() : null);

			return new Response(SUCCESS, null, null, null, dto);

		} catch (Exception e) {
		logger.error("getDoctorDetailsEncrypted failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


    @Override
    public Response getDoctorListForAppointmentsWebUser(Integer unitId, Integer specialityId) throws ApplicationException {
        try{

            String query =  " select "
                    + " 	docMst.doctor_id as \"doctorId\", "
                    + " 	docSubSplMppr.doctor_sub_speciality_id as \"doctorSubSpecialityId\", "
                    + " 	coalesce(docMst.first_name, '') as \"firstName\", "
                    + " 	coalesce(docMst.last_name, '') as \"lastName\", "
                    + " 	subSplMst.sub_speciality_name as \"subSpecialityName\", "
                    + " 	subSplMst.sub_speciality_id as \"subSpecialityId\", "
                    + " 	splMst.speciality_id as \"specialityId\", "
                    + " 	splMst.speciality_name as \"specialityName\", "
                    + " 	consultMppr.new_visit_rate as \"newVisitRate\", "
                    + " 	consultMppr.follow_up_visit_rate as \"followUpVisitRate\", "
                    + " 	consultMppr.secondary_visit_rate as \"secondaryVisitRate\" "
                    + " from "
                    + " 	doctor.m_speciality_master splMst "
                    + " inner join doctor.m_sub_speciality_master subSplMst on "
                    + " 	subSplMst.speciality_id = splMst.speciality_id "
                    + " inner join doctor.t_doctor_sub_speciality_mapper docSubSplMppr on "
                    + " 	docSubSplMppr.sub_speciality_id = subSplMst.sub_speciality_id "
                    + " inner join doctor.m_doctor_master docMst on "
                    + " 	docMst.doctor_id = docSubSplMppr.doctor_id "
                    + " inner join t_unit_speciality_mapper unitSplMppr on "
                    + " 	unitSplMppr.speciality_id  = splMst.speciality_id "
                    + " 	left join doctor.m_doctor_consultation_service_mapper consultMppr on "
                    + " 	consultMppr.doctor_id = docMst.doctor_id  "
                    + " 	and consultMppr.unit_id  = unitSplMppr.unit_id   "
                    + " 	and consultMppr.status = 'A'  "
                    + " where "
                    + " 	splMst.speciality_id = :specilaityId "
                    + " 	and docSubSplMppr.status = 'A' "
                    + " 	and subSplMst.status = 'A' "
                    + " 	and unitSplMppr.status = 'A' "
                    + " 	and unitSplMppr.unit_id =:unitId "
                    + " 	and docMst.status = 'A' "
                    + " order by "
                    + " 	docMst.first_name ASC" ;

            List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("unitId", unitId)
                    .setParameter("specilaityId", specialityId)
                    .setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
            return new Response(SUCCESS, null, null, specialityMasterDtosList, specialityMasterDtosList.size());

        } catch (Exception e) {
        logger.error("getDoctorListForAppointmentsWebUser failed", e);
        }
        return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
    }

	@Override
	public Response getAvailableDoctorBySpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperDto) {
		try {
			StringBuilder stringQuery = new StringBuilder(
					"SELECT d.doctor_id AS \"doctorId\", " +
							"CONCAT(COALESCE(d.first_name, ''), ' ', COALESCE(d.last_name, '')) AS \"doctorName\", " +
							"sp.speciality_id AS \"specialityId\", " +
							"s.slot_type_id AS \"slotTypeId\" " +
							"FROM opd.m_slot_master s " +
							"JOIN doctor.m_doctor_master d ON d.doctor_id = s.doctor_id " +
							"JOIN doctor.m_speciality_master sp ON sp.speciality_id = s.speciality_id " +
							"WHERE s.speciality_id = :specialityId "
			);

			if (doctorSpecialityMapperDto.getDateFromAI() == null
					|| doctorSpecialityMapperDto.getDateFromAI().trim().isEmpty()) {

				stringQuery.append(
						"AND s.appointment_date = CAST(CURRENT_TIMESTAMP AT TIME ZONE " +
								"(SELECT mum.time_zone FROM m_unit_master mum WHERE mum.unit_id = :unitId) AS DATE) ");
			} else {

				stringQuery.append(
						"AND TO_CHAR(s.appointment_date, 'YYYY-MM-DD') = :dateFromAI "
				);
			}

			stringQuery.append(
					"AND s.status = 'A'  AND s.from_time >= CAST(CURRENT_TIMESTAMP AT TIME ZONE (SELECT mum.time_zone FROM m_unit_master mum WHERE mum.unit_id = :unitId) AS TIME) " +
							"GROUP BY d.doctor_id, d.first_name, d.last_name, sp.speciality_id, s.slot_type_id " +
							"ORDER BY d.first_name"
			);


			Query query = sessionFactory.getCurrentSession()
					.createSQLQuery(stringQuery.toString())
					.setParameter("specialityId", doctorSpecialityMapperDto.getSpecialityId())
					.setParameter("unitId", doctorSpecialityMapperDto.getUnitId());

			if (doctorSpecialityMapperDto.getDateFromAI() != null
					&& !doctorSpecialityMapperDto.getDateFromAI().trim().isEmpty()) {
				query.setParameter("dateFromAI", doctorSpecialityMapperDto.getDateFromAI());
			}

			List<DoctorSpecialityMapperDto> specialityMasterDtosList =
					query.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class))
							.list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException e) {
		logger.error("getAvailableDoctorBySpeciality failed", e);
		} catch (Exception e) {
		logger.error("getAvailableDoctorBySpeciality failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

}
