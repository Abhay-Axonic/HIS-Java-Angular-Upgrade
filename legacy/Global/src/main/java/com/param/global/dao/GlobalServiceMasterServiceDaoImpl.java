package com.param.global.dao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.model.GlobalServiceMaster;
import com.param.global.model.UnitServiceMasterDetails;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class  GlobalServiceMasterServiceDaoImpl extends GenericDao<GlobalServiceMaster, Integer> implements IGlobalServiceMasterServiceDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(GlobalServiceMasterServiceDaoImpl.class);

	public GlobalServiceMasterServiceDaoImpl() {
		super(GlobalServiceMaster.class);
	}

	

	@Override
	public Response searchGlablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			
			String  query =    " select "
					 + "  distinct (details.service_standard_name ) as \"serviceStandardName\", "
					 + "  details.service_standard_code  as \"serviceStandardCode\" "
					 + " from "
					 + " 	service.m_global_service_master  details "
					 + " where"
					 + "  (lower(details.service_standard_name) LIKE :searchKeyword "
					 + "  or  lower(details.service_standard_code) LIKE :searchKeyword )"
					 + " and details.status = 'A'";
			
			List<UnitServiceMasterDetailsDto> listServiceMasterDto = sessionFactory
					  .getCurrentSession()
					  .createSQLQuery(query)
					.setParameter("searchKeyword",'%'+unitServiceMasterDetailsDto.getSearchKeyword().toLowerCase()+'%')
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("searchGlablServiceMaster failed", he);
		} catch (Exception e) {
			logger.error("searchGlablServiceMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllGlablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			String query =
					" select " +
							"  msm.service_standard_name as \"serviceStandardName\" " +
							" from " +
							"  service.m_service_master msm " +
							" where " +
							" msm.organization_id =:orgId " +
							" and msm.status = 'A'";
			var sessionQuery = sessionFactory .getCurrentSession() .createSQLQuery(query);
			List<UnitServiceMasterDetailsDto> listServiceMasterDto = sessionQuery
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class))
					.list();

			String serviceStandardNames = listServiceMasterDto.stream()
					.map(UnitServiceMasterDetailsDto::getServiceStandardName) // Extract serviceStandardName
					.filter(Objects::nonNull) // Remove null values (if any)
					.collect(Collectors.joining(", ")); // Join with a comma

			System.out.println(serviceStandardNames);
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, serviceStandardNames);
		}catch (HibernateException he) {
			logger.error("getAllGlablServiceMaster failed", he);
		} catch (Exception e) {
			logger.error("getAllGlablServiceMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response glablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {

			String query =
					" select " +
							"  details.is_outsource as \"isOutsource\", " +
							"  details.is_outsource_job as \"isOutsourceJob\", " +
							"  details.is_cyclic as \"isCyclicFlag\", " +
							"  details.is_package as \"isPackage\", " +
							"  details.surgery_grade_id as \"surgeryGradeId\", " +
							"  details.is_rate_editable as \"isRateEditable\", " +
							"  details.rate_editble_min_value as \"rateEditbleMinValue\", " +
							"  details.rate_editable_max_value as \"rateEditableMaxValue\", " +
							"  details.is_tax_applicable as \"isTaxApplicable\", " +
							"  details.is_ref_doctor_share as \"isRefDoctorShare\", " +
							"  details.is_auto_render as \"isAutoRender\", " +
							"  details.is_allow_multiple_quantity as \"isQuantityEditable\", " +
							"  details.is_panel as \"isPanel\", " +
							"  details.is_discount as \"isDiscount\", " +
							"  details.is_procedure as \"isProcedure\", " +
							"  details.is_doc_req as \"isDocReq\", " +
							"  details.is_bed_side as \"isBedSide\", " +
							"  details.is_poc as \"isPoc\", " +
							"  details.is_periodicity as \"isPeriodicity\", " +
							"  details.discount_value as \"discountValue\", " +
							"  details.gst_type_id as \"gstTypeId\", " +
							"  details.otc_gst_type_id as \"otcGstTypeId\", " +
							"  details.organization_id as \"organizationId\", " +
							"  details.unit_id as \"unitId\", " +
							"  details.created_by as \"createdBy\", " +
							"  details.status as \"status\", " +
							"  details.updated_by as \"updatedBy\", " +
							"  details.procedure_type_id as \"procedureTypeId\", " +
							"  details.is_pacs_req as \"isPacsReq\", " +
							"  details.service_standard_name as \"serviceStandardName\", " +
							"  details.service_standard_code as \"serviceStandardCode\", " +
							"  details.speciality_name as \"specialityName\", " +
							"  details.sub_speciality_name as \"subSpecialityName\", " +
							"  details.is_appointable as \"isAppointable\", " +
							"  details.gender as \"gender\", " +
							"  details.ref_share_amt as \"refShareAmt\", " +
							"  details.ref_share_per as \"refSharePer\", " +
							"  details.is_review_applicable as \"isReviewApplicable\", " +
							"  details.review_time as \"reviewTime\", " +
							"  details.bill_of_material as \"billOfMaterial\", " +
							"  details.doc_ref_per as \"docRefPer\", " +
							"  details.sample_collection as \"sampleCollection\", " +
							"  details.is_doc_share_per_wise as \"isDocSharePerWise\", " +
							"  details.is_ref_share_per_wise as \"isRefSharePerWise\", " +
							"  details.lab_auto_mail_exclusion as \"labAutoMailExclusion\", " +
							"  details.pre_requisite as \"preRequisite\", " +
							"  details.pre_requisite_details as \"preRequisiteDetails\" " +
							" from " +
							"  service.m_global_service_master details " +
							" where " +
							"  details.speciality_name = :specialityNameList " +
							"  and details.sub_speciality_name = :subSpecialityNameList " +
							"  and details.status = 'A'";

			if (unitServiceMasterDetailsDto.getSearchKeyword() != null && !unitServiceMasterDetailsDto.getSearchKeyword().isEmpty()) {
				query +=
						" and (lower(details.service_standard_name) like :searchKeyword " +
								" or lower(details.service_standard_code) like :searchKeyword)";
			}

			var sessionQuery = sessionFactory
					.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("subSpecialityNameList", unitServiceMasterDetailsDto.getGolbalSubSpeciality())
					.setParameter("specialityNameList", unitServiceMasterDetailsDto.getGolbalSpeciality());

			if (unitServiceMasterDetailsDto.getSearchKeyword() != null && !unitServiceMasterDetailsDto.getSearchKeyword().isEmpty()) {
				sessionQuery.setParameter("searchKeyword", "%" + unitServiceMasterDetailsDto.getSearchKeyword().toLowerCase() + "%");
			}

			List<UnitServiceMasterDetailsDto> listServiceMasterDto = sessionQuery
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class))
					.list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("glablServiceMaster failed", he);
		} catch (Exception e) {
			logger.error("glablServiceMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response glablServiceMasterSpecialityName(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			
			List<UnitServiceMasterDetailsDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_NAME_FROM_GLOBAL_SERVICE_MASTER")
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("glablServiceMasterSpecialityName failed", he);
		} catch (Exception e) {
			logger.error("glablServiceMasterSpecialityName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response glablServiceMasterSubSpecialityName(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {


			String query = "select distinct (gsm.sub_speciality_name) as \"subSpecialityName\", " +
					"gsm.speciality_name as \"specialityName\" " +
					"from service.m_global_service_master gsm " +
					"where gsm.speciality_name =:specialityName ";

			List<UnitServiceMasterDetailsDto> listServiceMasterDto = sessionFactory
					.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("specialityName", unitServiceMasterDetailsDto.getGolbalSpeciality())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class))
					.list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("glablServiceMasterSubSpecialityName failed", he);
		} catch (Exception e) {
			logger.error("glablServiceMasterSubSpecialityName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


}
