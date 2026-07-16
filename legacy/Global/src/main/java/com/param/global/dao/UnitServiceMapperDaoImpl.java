package com.param.global.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.ServiceBasePriceMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.SearchItemDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.model.UnitServiceMapper;
import com.param.global.model.UnitServiceMasterDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UnitServiceMapperDaoImpl extends GenericDao<UnitServiceMapper, Integer> implements IUnitServiceMapperDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(UnitServiceMapperDaoImpl.class);

	public UnitServiceMapperDaoImpl() {
		super(UnitServiceMapper.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response saveUnitServiceMapper(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {
		try {
			UnitServiceMapper unitServiceMapper = mapper.map(unitServiceMapperDto, UnitServiceMapper.class, "global_UnitServiceMapperDto_to_UnitServiceMapper");
			save(unitServiceMapper);
			UnitServiceMasterDetails unitServiceMasterDetails=new UnitServiceMasterDetails();
			unitServiceMasterDetails.setServiceMasterId(unitServiceMapperDto.getServiceId());
			unitServiceMasterDetails.setIsDiscount('N');
			unitServiceMasterDetails.setIsOutsource('N');
			unitServiceMasterDetails.setIsPackage('I');
			unitServiceMasterDetails.setIsPanel('N');
			unitServiceMasterDetails.setIsQuantityEditable('N');
			unitServiceMasterDetails.setIsAutoRender('N');
			unitServiceMasterDetails.setIsRateEditable('N');
			unitServiceMasterDetails.setIsRefDoctorShare('N');
			unitServiceMasterDetails.setIsTaxApplicable('N');
			unitServiceMasterDetails.setIsBedSide('N');
			unitServiceMasterDetails.setIsDocReq('N');
			unitServiceMasterDetails.setIsPeriodicity('N');
			unitServiceMasterDetails.setIsPoc('N');
			unitServiceMasterDetails.setIsProcedure('N');
			unitServiceMasterDetails.setStatus('A');
			unitServiceMasterDetails.setIsAppointable('N');
			unitServiceMasterDetails.setDiscountValue(new BigDecimal(0));
			unitServiceMasterDetails.setRateEditbleMinValue(new BigDecimal(0));
			unitServiceMasterDetails.setRateEditableMaxValue(new BigDecimal(0));
			unitServiceMasterDetails.setCreatedBy(unitServiceMapperDto.getCreatedBy());
			unitServiceMasterDetails.setUpdatedBy(unitServiceMapperDto.getUpdatedBy());
			unitServiceMasterDetails.setCreatedDate(unitServiceMapperDto.getCreatedDate());
			unitServiceMasterDetails.setUpdatedDate(unitServiceMapperDto.getUpdatedDate());
			unitServiceMasterDetails.setUnitId(unitServiceMapperDto.getUnitId());
			unitServiceMasterDetails.setOrganizationId(unitServiceMapperDto.getOrgnisationId());
			unitServiceMasterDetails.setGender(0);
			unitServiceMasterDetails.setIsOutsourceJob('N');
			sessionFactory.getCurrentSession().save(unitServiceMasterDetails);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("Error while saving unit service mapper", he);
		} catch (Exception e) {
			logger.error("Error while saving unit service mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchServiceMasterBySpecialitySubSplecialityUnitAndOrg(String searchKeyword, Integer specialityId, Integer subSpecialityId, Integer unitId, Integer orgId)
			throws ApplicationException {
		try {
				List<UnitServiceMapperDto> listUnitServiceMapperDto = sessionFactory.getCurrentSession()
						.getNamedQuery("SEARCH_SERVICE_MASTER_BY_UNIT_AND_ORG")
						.setParameter("unitId", unitId)
						.setParameter("orgId", orgId)
						.setParameter("specialityId", specialityId)
						.setParameter("subSpecialityId", subSpecialityId)
						.setParameter("searchKeyword", "%"+ searchKeyword.trim().toLowerCase()+"%")
						.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class))
						.list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDto, null);
		}catch (HibernateException he) {
			logger.error("Error while searching service master by speciality sub-speciality unit and org", he);
		} catch (Exception e) {
			logger.error("Error while searching service master by speciality sub-speciality unit and org", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {
		try {
				List<UnitServiceMapperDto> listUnitServiceMapperDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_MAPPED_SERVICES_BY_UNIT")
						.setParameter("unitId", unitServiceMapperDto.getUnitId())
						.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
						.setFirstResult(unitServiceMapperDto.getOffset() != null ? unitServiceMapperDto.getOffset() : 0)
						.setMaxResults(unitServiceMapperDto.getRecordPerPage() != null ? unitServiceMapperDto.getRecordPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class))
						.list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDto, null);
		}catch (HibernateException he) {
			logger.error("Error while getting mapped services list by unit", he);
		} catch (Exception e) {
			logger.error("Error while getting mapped services list by unit", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateUnitServiceMapperStatus(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
				sessionFactory.getCurrentSession().getNamedQuery("UPDATE_UNIT_SERVICE_MAPPER_STATUS")
					.setCharacter("status", unitServiceMapperDto.getStatus())
					.setParameter("unitId", unitServiceMapperDto.getUnitId())
					.setParameter("serviceId", unitServiceMapperDto.getServiceId())
					.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
					.executeUpdate();
				//for updating unit service master details
					sessionFactory.getCurrentSession().createQuery("UPDATE UnitServiceMasterDetails usmd "
							+" SET usmd.status='"+unitServiceMapperDto.getStatus()+"'"
							+" WHERE usmd.serviceMasterId="+unitServiceMapperDto.getServiceId()
							+" AND usmd.organizationId="+unitServiceMapperDto.getOrgnisationId()
							+" AND usmd.unitId="+ unitServiceMapperDto.getUnitId())
					.executeUpdate();
					//for updating unit service master details
					sessionFactory.getCurrentSession().createQuery("UPDATE ServiceBasePriceMaster sbpm "
							+" SET sbpm.status='"+unitServiceMapperDto.getStatus()+"'"
							+" WHERE sbpm.serviceMasterId="+unitServiceMapperDto.getServiceId()
							+" AND sbpm.orgnisationId="+unitServiceMapperDto.getOrgnisationId()
							+" AND sbpm.unitId="+ unitServiceMapperDto.getUnitId())
					.executeUpdate();
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
			logger.error("Error while updating unit service mapper status", he);
		} catch (Exception e) {
			logger.error("Error while updating unit service mapper status", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getCountOfMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession().getNamedQuery("GET_COUNT OF_MAPPED_SERVICES_BY_UNIT")
						.setParameter("unitId", unitServiceMapperDto.getUnitId())
						.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
						.uniqueResult();
			if (count.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			} else
			{
				return new Response(ERROR, null, null, null, BigInteger.ZERO);
			}
		}catch (HibernateException he) {
			logger.error("Error while getting count of mapped services list by unit", he);
		} catch (Exception e) {
			logger.error("Error while getting count of mapped services list by unit", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			String[] searchString = serviceMasterDto.getSearchKeyword().split("---");
			Integer gender=0;
			String searchStr=null;
			if(searchString.length>1){
				searchStr=searchString[0];
				if(searchString[1] != null) {
					gender=Integer.parseInt(searchString[1]);
				}
				}else{
					searchStr=searchString[0];
				}
			List<ServiceSearchResDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICES_BY_NAME_AND_CODE")

					.setParameter("searchKeyword", "%"+searchStr.toLowerCase()+"%")
//					.setParameter("searchKeyword", "%"+serviceMasterDto.getSearchKeyword().toLowerCase()+"%")
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setParameter("gender", gender) //added
					//.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd")) //added
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Error while searching services by name and code", he);
		} catch (Exception e) {
			logger.error("Error while searching services by name and code", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchUnitServiceByMultipleSpecialityAndSubSpeciality(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			List<UnitServiceMapperDto> listUnitServiceMapperDtoList = null;
			String roughQuery= sessionFactory.getCurrentSession().getNamedQuery("SEARCH_SERVICE_MASTER_BY_UNIT_AND_ORG_SPECIALITY_SUBSPECILAITY_LIST").getQueryString();
			StringBuilder stringBuilderQuery = new StringBuilder(roughQuery);


			if(unitServiceMapperDto.getServiceIdList()!=null && unitServiceMapperDto.getServiceIdList().size()>0)
			{
				stringBuilderQuery.append(" AND service.service_master_id IN (:serviceIdList) ");
				stringBuilderQuery.append(" ORDER BY service.service_standard_name ASC ");
				listUnitServiceMapperDtoList = sessionFactory.getCurrentSession().createSQLQuery(stringBuilderQuery.toString())
						.setParameter("unitId", unitServiceMapperDto.getUnitId())
						.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
						.setParameterList("serviceIdList", unitServiceMapperDto.getServiceIdList())
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class))
						.list();

			}else if(unitServiceMapperDto.getSubSpecialityIdList()!=null && unitServiceMapperDto.getSubSpecialityIdList().size()>0)
			{
				stringBuilderQuery.append(" AND service.sub_speciality_id IN (:subSpecialityIdList) ");
				stringBuilderQuery.append(" ORDER BY service.service_standard_name ASC ");
				listUnitServiceMapperDtoList = sessionFactory.getCurrentSession().createSQLQuery(stringBuilderQuery.toString())
						.setParameter("unitId", unitServiceMapperDto.getUnitId())
						.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
						.setParameterList("subSpecialityIdList", unitServiceMapperDto.getSubSpecialityIdList())
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class))
						.list();
			}else if(unitServiceMapperDto.getSpecialityIdList()!=null && unitServiceMapperDto.getSpecialityIdList().size()>0 )
			{
				stringBuilderQuery.append(" AND service.speciality_id IN (:specialityIdList) ");
				stringBuilderQuery.append(unitServiceMapperDto.getSearchKeyword()!=null && unitServiceMapperDto.getSearchKeyword().length()>0 ? " AND(lower(service.service_standard_name )like '%"+unitServiceMapperDto.getSearchKeyword().toLowerCase()+"%' OR lower(service.service_standard_code) like '%"+unitServiceMapperDto.getSearchKeyword().toLowerCase()+"%') ":" ");
				stringBuilderQuery.append(" ORDER BY service.service_standard_name ASC ");
				listUnitServiceMapperDtoList = sessionFactory.getCurrentSession().createSQLQuery(stringBuilderQuery.toString())
						.setParameter("unitId", unitServiceMapperDto.getUnitId())
						.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
						.setParameterList("specialityIdList", unitServiceMapperDto.getSpecialityIdList())
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class))
						.list();
			}else{
				return new Response<>(SUCCESS, SUCCESS_CODE, "Select atleast Department OR Sub-department OR Services...!!", null, null);
			}


			return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDtoList, null);
	}catch (HibernateException he) {
		logger.error("Error while searching unit service by multiple speciality and sub-speciality", he);
	} catch (Exception e) {
		logger.error("Error while searching unit service by multiple speciality and sub-speciality", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchActiveServices(ServiceSearchReqDto serviceSearchReqDto)
			throws ApplicationException {
		try {

			List<ServiceSearchResDto> listServiceMasterDto = null;
			String str = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICES_BY_NAME_AND_CODE").getQueryString();
			StringBuffer query = new StringBuffer(str);


			if(serviceSearchReqDto.getSubGroupId() !=null && serviceSearchReqDto.getGroupId() != null){
				query.append(" AND  service.speciality_id =:specialityId ");
				query.append(" AND  service.sub_speciality_id =:subSpecialityId ");
				listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("searchKeyword", "%"+serviceSearchReqDto.getServiceName().toLowerCase()+"%")
						//.setParameter("serviceCode", serviceMasterDto.getServiceStandardCode())
						.setParameter("unitId", serviceSearchReqDto.getUnitId())
						.setParameter("orgId", serviceSearchReqDto.getOrganisationId())
						.setParameter("specialityId", serviceSearchReqDto.getSubGroupId())
						.setParameter("subSpecialityId", serviceSearchReqDto.getSubGroupId())
						.setDate("currentDate", TimeZoneComponent.getDateByzone(serviceSearchReqDto.getUnitId())) //added
						.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class))
						.list();
			}else if(serviceSearchReqDto.getGroupId() != null){
				query.append(" AND  service.speciality_id =:specialityId ");
				listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("searchKeyword", "%"+serviceSearchReqDto.getServiceName().toLowerCase()+"%")
						//.setParameter("serviceCode", serviceMasterDto.getServiceStandardCode())
						.setParameter("unitId", serviceSearchReqDto.getUnitId())
						.setParameter("orgId", serviceSearchReqDto.getOrganisationId())
						.setParameter("specialityId", serviceSearchReqDto.getGroupId())
						.setDate("currentDate", TimeZoneComponent.getDateByzone(serviceSearchReqDto.getUnitId())) //added
						.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class))
						.list();
			}else if(serviceSearchReqDto.getSubGroupId() !=null ){
				query.append(" AND  service.sub_speciality_id =:subSpecialityId ");
				listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("searchKeyword", "%"+serviceSearchReqDto.getServiceName().toLowerCase()+"%")
						//.setParameter("serviceCode", serviceMasterDto.getServiceStandardCode())
						.setParameter("unitId", serviceSearchReqDto.getUnitId())
						.setParameter("orgId", serviceSearchReqDto.getOrganisationId())
						.setParameter("subSpecialityId", serviceSearchReqDto.getSubGroupId())
						.setDate("currentDate", TimeZoneComponent.getDateByzone(serviceSearchReqDto.getUnitId())) //added
						.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class))
						.list();
			}else{
				listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("searchKeyword", "%"+serviceSearchReqDto.getServiceName().toLowerCase()+"%")
						//.setParameter("serviceCode", serviceMasterDto.getServiceStandardCode())
						.setParameter("unitId", serviceSearchReqDto.getUnitId())
						.setParameter("orgId", serviceSearchReqDto.getOrganisationId())
						.setDate("currentDate", TimeZoneComponent.getDateByzone(serviceSearchReqDto.getUnitId())) //added
						.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class))
						.list();
			}

			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Error while searching active services", he);
		} catch (Exception e) {
			logger.error("Error while searching active services", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getServiceByIdForPackageValid(int serviceId,Integer unitId) throws ApplicationException {
		try{
			List<ServiceSearchResDto> listServiceSearchResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICE_BY_ID_FOR_PACKAGE_CONSUMP_VALIDATION").
					setParameter("serviceId", serviceId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceSearchResDto, null);
		} catch (Exception e) {
			logger.error("Error while getting service by id for package valid", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public void saveUnitServiceBasePrice(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {

		try{
			ServiceBasePriceMaster  serviceBasePriceMaster=new ServiceBasePriceMaster();
			serviceBasePriceMaster.setServiceMasterId(unitServiceMapperDto.getServiceId());
			serviceBasePriceMaster.setStatus('A');
			serviceBasePriceMaster.setVisitTypeId(OP);
			serviceBasePriceMaster.setCreatedBy(unitServiceMapperDto.getCreatedBy());
			serviceBasePriceMaster.setUpdatedBy(unitServiceMapperDto.getUpdatedBy());
			serviceBasePriceMaster.setCreatedDate(new Date(unitServiceMapperDto.getCreatedDate()));
			serviceBasePriceMaster.setUpdatedDate(new Date(unitServiceMapperDto.getCreatedDate()));
			serviceBasePriceMaster.setUnitId(unitServiceMapperDto.getUnitId());
			serviceBasePriceMaster.setOrgnisationId(unitServiceMapperDto.getOrgnisationId());
			sessionFactory.getCurrentSession().save(serviceBasePriceMaster);
		}catch(HibernateException he){
			logger.error("Error while saving unit service base price", he);
		}catch(Exception e){
			logger.error("Error while saving unit service base price", e);
		}
	}
	@Override
	public Response searchProcedure(int orgId, int unitId,int specialityId, String searchKeyWord) throws ApplicationException {
		try {
			List<ServiceSearchResDto> listUnitServiceMapperDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_PROCEDURE_BY_SPECIALITY")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setParameter("specialityId", specialityId)
					.setParameter("searchKeyword", "%"+ searchKeyWord.trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDto, null);
	}catch (HibernateException he) {
		logger.error("Error while searching procedure", he);
	} catch (Exception e) {
		logger.error("Error while searching procedure", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchServiceMasterByWithoutSpecialitySubSpeciality(String searchKeyword, Integer unitId,
			Integer orgId) throws ApplicationException {
		try {
			String[] searchString = searchKeyword.split("---");
			Integer gender=0;
			if(searchString.length>1){
				searchKeyword=searchString[0];
				gender=Integer.parseInt(searchString[1]);
				}else{
					searchKeyword=searchString[0];
				}
			List<UnitServiceMapperDto> listUnitServiceMapperDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICE_MASTER_BY_WITHOUT_SPE_SUBSPE_UNIT_AND_ORG")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setParameter("gender", gender)
					.setParameter("searchKeyword", "%"+ searchKeyword.trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDto, null);
	}catch (HibernateException he) {
		logger.error("Error while searching service master without speciality sub-speciality", he);
	} catch (Exception e) {
		logger.error("Error while searching service master without speciality sub-speciality", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchServiceMasterBySpecialitySubSplecialityOrgUnitAndGender(String searchKeyword,
			Integer specialityId, Integer subSplecialityId, Integer unitId, Integer orgId, Integer gender)
			throws ApplicationException {
		try {
			List<UnitServiceMapperDto> listUnitServiceMapperDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICE_MASTER_BY_ORG_UNIT_GENDER")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setParameter("specialityId", specialityId)
					.setParameter("subSpecialityId", subSplecialityId)
					.setParameter("gender", gender)
					.setParameter("searchKeyword", "%"+ searchKeyword.trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDto, null);
	}catch (HibernateException he) {
		logger.error("Error while searching service master by speciality sub-speciality org unit and gender", he);
	} catch (Exception e) {
		logger.error("Error while searching service master by speciality sub-speciality org unit and gender", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getMappedServicesListByUnitSearch(UnitServiceMapperDto unitServiceMapperDto)
			throws ApplicationException {
		try {
			List<UnitServiceMapperDto> listUnitServiceMapperDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MAPPED_SERVICES_BY_UNIT_SEARCH")
					.setParameter("unitId", unitServiceMapperDto.getUnitId())
					.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
					.setParameter("searchKeyword", "%"+unitServiceMapperDto.getSearchKeyword().trim().toLowerCase()+"%")
					.setFirstResult(unitServiceMapperDto.getOffset() != null ? unitServiceMapperDto.getOffset() : 0)
					.setMaxResults(unitServiceMapperDto.getRecordPerPage() != null ? unitServiceMapperDto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listUnitServiceMapperDto, null);
	}catch (HibernateException he) {
		logger.error("Error while getting mapped services list by unit search", he);
	} catch (Exception e) {
		logger.error("Error while getting mapped services list by unit search", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response checkServiceMappedWithTest(ServiceMasterDto dto) throws ApplicationException {
		try {

			ContractConsumptionDto responseDto = null;
			UnitServiceMasterDetailsDto detailsDto = (UnitServiceMasterDetailsDto) sessionFactory.getCurrentSession().getNamedQuery("CHECK_SERVICE_MAPPED_WITH_TEST")
						.setParameter("unitId", dto.getUnitId())
						.setParameter("serviceId", dto.getServiceMasterId())
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class))
						.uniqueResult();

			if(dto.getEncounterId()!=null && dto.getEncounterId()>0)
			{
				String query = "select tod.order_details_id as \"orderDetailsId\" "
						+ "from opd.t_encounter_master as tem inner join public.t_order_master tom on tem.encounter_id=tom.encounter_id "
						+ "inner join public.t_order_details as tod on tom.order_id=tod.order_id "
						+ "inner join service.m_package_master as mpm on mpm.package_master_id=tod.package_id and mpm.package_type_id=1 "
						+ "where tod.service_id=:serviceId "
						+ "and tod.status='A' "
						+ "and tem.encounter_id=:encounterId "
						+ "and tod.service_rendered=0 "
						+ "and tod.org_unit_id=:unitId "
						+ "and tod.org_id=:orgId "
						+ " order by \"orderDetailsId\" asc  limit 1";

				 responseDto=(ContractConsumptionDto)sessionFactory.getCurrentSession().createSQLQuery(query)
//				.setParameter("patientId", dto.getPatientId())
				.setParameter("serviceId", dto.getServiceMasterId())
				.setParameter("encounterId", dto.getEncounterId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("orgId", dto.getOrgId())
				.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class)).uniqueResult();
			}

			return new Response(SUCCESS, responseDto!=null ? responseDto.getOrderDetailsId()+"" : null, null, null, detailsDto);

		}catch (HibernateException he) {
			logger.error("Error while checking service mapped with test", he);
		} catch (Exception e) {
			logger.error("Error while checking service mapped with test", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchItemByNameAndCode(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			String searchStr = serviceMasterDto.getSearchKeyword();

			List<SearchItemDto> listItemMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_ITEMS_BY_NAME_AND_CODE")
					.setParameter("searchKeyword", "%"+searchStr.toLowerCase()+"%")
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SearchItemDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listItemMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Error while searching item by name and code", he);
		} catch (Exception e) {
			logger.error("Error while searching item by name and code", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response searchServiceByNameOrCode(UnitServiceMapperDto unitServiceMapperDto) throws ApplicationException {
		try {
			List<UnitServiceMapperDto> listItemMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICE_MASTER_BY_UNIT_AND_ORG_SERVICE_NAME_OR_CODE")
					.setParameter("searchKeyword", "%"+unitServiceMapperDto.getSearchKeyword().toLowerCase()+"%")
					.setParameter("unitId", unitServiceMapperDto.getUnitId())
					.setParameter("orgId", unitServiceMapperDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listItemMasterDto, null);
	}catch (HibernateException he) {
		logger.error("Error while searching service by name or code", he);
	} catch (Exception e) {
		logger.error("Error while searching service by name or code", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response serviceMasterForUnitApplicationService(ServiceMasterDto serviceMasterDto)
			throws ApplicationException {
		try {

			List<ServiceSearchResDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICES_BY_NAME_AND_CODE_UNIT_APPLICATION_SERVICE")
					.setParameter("searchKeyword", "%"+serviceMasterDto.getSearchKeyword().toLowerCase()+"%")
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Error while getting service master for unit application service", he);
		} catch (Exception e) {
			logger.error("Error while getting service master for unit application service", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response serviceAreaBysearchKeyword(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_GET_ACTIVE_AREA_BY_CITY_ID_DATA")
					.setParameter("searchKeyword", "%"+areaMasterDto.getSearchKeyword().toLowerCase()+"%")
					.setParameter("cityId", areaMasterDto.getCityId())
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Error while searching service area by search keyword", he);
		} catch (Exception e) {
			logger.error("Error while searching service area by search keyword", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getServiceListBySubSpecialityIdNotByTariff(List<Integer> serviceIds, Integer unitId)
			throws ApplicationException {

		try {

			String query = "SELECT service_id AS \"serviceId\" " +
					"FROM service.t_unit_service_mapper tusm " +
					"WHERE unit_id = :unitId " +
					"AND service_id IN (:serviceIds) " +
					"AND status = 'A'";

			List<UnitServiceTariffMasterDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameterList("serviceIds", serviceIds)
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class))
					.list();


			return new Response(SUCCESS,"", null, list, null);

		}catch (HibernateException he) {
			logger.error("Error while getting service list by sub-speciality id not by tariff", he);
		} catch (Exception e) {
			logger.error("Error while getting service list by sub-speciality id not by tariff", e);
		}

		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response saveUnitServiceMapperfromUnit(UnitServiceMapperDto mapperDto) throws ApplicationException {

		try {
				UnitServiceMapper unitServiceMapper = mapper.map(mapperDto, UnitServiceMapper.class, "global_UnitServiceMapperDto_to_UnitServiceMapper");
				unitServiceMapper.setStatus('A');
				save(unitServiceMapper);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("Error while saving unit service mapper from unit", he);
		} catch (Exception e) {
			logger.error("Error while saving unit service mapper from unit", e);
		}

		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkServiceWithTestParameter(ServiceMasterDto dto) throws ApplicationException {

		BigInteger count = BigInteger.valueOf(0);
		try{

			if(dto.getServiceIds() != null && dto.getServiceIds().size()>0){

				String query = "SELECT count(*) FROM lab.t_test_parameter_mppr ttpm " +
						"INNER JOIN lab.m_test_master mtm ON ttpm.test_id = mtm.test_id " +
						"WHERE mtm.service_id = :serviceMasterId " +
						"AND mtm.org_unit_id = :unitId " +
						"AND mtm.test_status = 'A' " +
						"AND ttpm.test_para_status = 'A' " +
						"AND parameter_id IN (" +
						"    SELECT parameter_id FROM lab.t_test_parameter_mppr ttpm " +
						"    INNER JOIN lab.m_test_master mtm ON ttpm.test_id = mtm.test_id " +
						"    WHERE mtm.service_id IN (:serviceIds) " +
						"    AND mtm.org_unit_id = :unitId " +
						"    AND mtm.test_status = 'A' " +
						"    AND ttpm.test_para_status = 'A' " +
						")";

				 count = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("serviceMasterId", dto.getServiceMasterId())
						.setParameter("unitId", dto.getUnitId())
						.setParameterList("serviceIds", dto.getServiceIds())
						.uniqueResult();

				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			}else{
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			}


		}catch (HibernateException he) {
			logger.error("Error while checking service with test parameter", he);
		} catch (Exception e) {
			logger.error("Error while checking service with test parameter", e);
		}


		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, count);
	}

	@Override
	public Response searchServicesByNameAndCodeForEasyEMR(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			String[] searchString = serviceMasterDto.getSearchKeyword().split("---");
			Integer gender=0;
			String searchStr=null;
			if(searchString.length>1){
				searchStr=searchString[0];
				if(searchString[1] != null) {
					gender=Integer.parseInt(searchString[1]);
				}
			}else{
				searchStr=searchString[0];
			}
			List<ServiceSearchResDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SERVICES_BY_NAME_AND_CODE_FOR_PATHO_AND_RADIO")

					.setParameter("searchKeyword", "%"+searchStr.toLowerCase()+"%")
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setParameter("gender", gender) //added
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Error while searching services by name and code for easy EMR", he);
		} catch (Exception e) {
			logger.error("Error while searching services by name and code for easy EMR", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

}
