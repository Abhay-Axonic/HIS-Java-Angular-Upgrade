package com.param.billing.patientType.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.PatientTypeGroupWiseDetailsMaster;
import com.param.billing.patientType.dto.PatientTypeGroupWiseDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import com.param.global.service.VerifiedIdentityService;

@SuppressWarnings({"rawtypes","unchecked","unused"})
@Repository
public class PatientTypeGroupWiseDetailsDaoImpl extends GenericDao<PatientTypeGroupWiseDetailsMaster,Integer> implements IPatientTypeGroupWiseDetailsDao,ICommonConstants{

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public PatientTypeGroupWiseDetailsDaoImpl() {
		super(PatientTypeGroupWiseDetailsMaster.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;
	
	
	@Override
	public Response savePatientTypeGroupWiseDetails(List<PatientTypeGroupWiseDetailsDto> patientTypeGroupWiseDetailsDto) {
		try {
			LinkedList<Integer>  ids = new LinkedList<>();
			
			for (PatientTypeGroupWiseDetailsDto patientGroupMasterList : patientTypeGroupWiseDetailsDto) {
				
				
				StringBuilder query = new StringBuilder("select patient_type_group_id as \"patientTypeGroupId\" " + 
						"from billing.m_patient_type_group_wise_details where" );
				        query.append(patientGroupMasterList.getOrganizationId()!=null ? " organization_id =:organizationId ": " ");
				        query.append(patientGroupMasterList.getUnitId()!=null ? " AND unit_id =:unitId " : " ");
						query.append(patientGroupMasterList.getSpecialityId()!=null ? " AND speciality_id =:specialityId ": " ");
						query.append(patientGroupMasterList.getSubSpecialityId()!=null ? " AND  sub_speciality_id =:subSpecialityId " : " ");
						query.append(patientGroupMasterList.getMarkupStatus()!=null ? " AND markup_status =:markupStatus ": " ");
						query.append(patientGroupMasterList.getPatientTypeId()!=null ? " AND patient_type_id =:patientTypeId " : " ");
						query.append(patientGroupMasterList.getServiceId()!=null ? " AND service_id =:serviceId " : " ");
						query.append(patientGroupMasterList.getTariffId()!=null ? " AND tariff_id =:tariffId " : " ");
						query.append(patientGroupMasterList.getVisitTypeId()!=null ? " AND visit_type_id =:visitTypeId " : " ");
						query.append(patientGroupMasterList.getPatientCategoryId()!=null ? " AND payment_entitlement_id =:patientCategoryId " : " ");
						query.append(patientGroupMasterList.getItemId()!=null ? " AND item_id =:itemId " : " ");
						query.append(patientGroupMasterList.getAssetCategoryId()!=null ? " AND asset_category_id =:assetCategoryId " : " ");
						query.append(patientGroupMasterList.getProductCategoryId()!=null ? " AND product_category_id =:productCategoryId " : " ");
				
						//" where speciality_id="+ patientGroupMasterList.getSpecialityId() +
						//" AND sub_speciality_id="+patientGroupMasterList.getSubSpecialityId() +
						//" AND markup_status="+patientGroupMasterList.getMarkupStatus() + 
						//" AND patient_type_id="+patientGroupMasterList.getPatientTypeId() + 
						//" AND service_id="+patientGroupMasterList.getServiceId() +
						//" AND tariff_id="+patientGroupMasterList.getTariffId() +
						//" AND visit_type_id="+patientGroupMasterList.getVisitTypeId() +
						//" AND payment_entitlement_id="+patientGroupMasterList.getPaymentEntitlementId() +
						//" AND patient_category_id="+patientGroupMasterList.getPatientCategoryId() +
						//" AND item_id="+patientGroupMasterList.getItemId() +
						//" AND asset_category_id="+patientGroupMasterList.getAssetCategoryId() +
						//" AND product_category_id="+patientGroupMasterList.getProductCategoryId() +
						//" AND organization_id="+patientGroupMasterList.getOrganizationId() +
						//" AND unit_id="+patientGroupMasterList.getUnitId()
				//For Checking Start

				Query sqlQuery =sessionFactory.getCurrentSession().createSQLQuery(query.toString());

				if(patientGroupMasterList.getOrganizationId()!=null)
					sqlQuery.setParameter("organizationId", patientGroupMasterList.getOrganizationId());

				if(patientGroupMasterList.getUnitId()!=null)
					sqlQuery.setParameter("unitId", patientGroupMasterList.getUnitId());

				if(patientGroupMasterList.getSpecialityId()!=null)
					sqlQuery.setParameter("specialityId", patientGroupMasterList.getSpecialityId());

				if(patientGroupMasterList.getSubSpecialityId()!=null)
					sqlQuery.setParameter("subSpecialityId", patientGroupMasterList.getSubSpecialityId());

				if(patientGroupMasterList.getMarkupStatus()!=null)
					sqlQuery.setParameter("markupStatus", patientGroupMasterList.getMarkupStatus());

				if(patientGroupMasterList.getPatientTypeId()!=null)
					sqlQuery.setParameter("patientTypeId", patientGroupMasterList.getPatientTypeId());

				if(patientGroupMasterList.getServiceId()!=null)
					sqlQuery.setParameter("serviceId", patientGroupMasterList.getServiceId());

				if(patientGroupMasterList.getTariffId()!=null)
					sqlQuery.setParameter("tariffId", patientGroupMasterList.getTariffId());

				if(patientGroupMasterList.getVisitTypeId()!=null)
					sqlQuery.setParameter("visitTypeId", patientGroupMasterList.getVisitTypeId());

				if(patientGroupMasterList.getPatientCategoryId()!=null)
					sqlQuery.setParameter("patientCategoryId", patientGroupMasterList.getPatientCategoryId());

				if(patientGroupMasterList.getItemId()!=null)
					sqlQuery.setParameter("itemId", patientGroupMasterList.getItemId());

				if(patientGroupMasterList.getAssetCategoryId()!=null)
					sqlQuery.setParameter("assetCategoryId", patientGroupMasterList.getAssetCategoryId());

				if(patientGroupMasterList.getProductCategoryId()!=null)
					sqlQuery.setParameter("productCategoryId", patientGroupMasterList.getProductCategoryId());

				List<PatientTypeGroupWiseDetailsDto> patientTypeGroupList = sqlQuery
						.setResultTransformer(Transformers.aliasToBean(PatientTypeGroupWiseDetailsDto.class)).list();
				//For Checking End
				
				if(patientTypeGroupList != null && patientTypeGroupList.size() > 0) {
					ids.add(patientTypeGroupList.getFirst().getPatientTypeGroupId());
				}

					PatientTypeGroupWiseDetailsMaster patientGroupMaster = new PatientTypeGroupWiseDetailsMaster();

					patientGroupMaster.setSpecialityId(patientGroupMasterList.getSpecialityId());
					patientGroupMaster.setSubSpecialityId(patientGroupMasterList.getSubSpecialityId());
					patientGroupMaster.setCreatedBy(patientGroupMasterList.getCreatedBy());
					patientGroupMaster.setMarkupStatus(patientGroupMasterList.getMarkupStatus());
					patientGroupMaster.setOrganizationId(patientGroupMasterList.getOrganizationId());
					patientGroupMaster.setPercentage(patientGroupMasterList.getPercentage());
					patientGroupMaster.setUnitId(patientGroupMasterList.getUnitId());
					patientGroupMaster.setUpdatedBy(patientGroupMasterList.getUpdatedBy());
					patientGroupMaster.setPatientTypeId(patientGroupMasterList.getPatientTypeId());
					patientGroupMaster.setStatus("A");
					patientGroupMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientGroupMasterList.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					patientGroupMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientGroupMasterList.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					patientGroupMaster.setServiceId(patientGroupMasterList.getServiceId());
					patientGroupMaster.setTariffId(patientGroupMasterList.getTariffId());
					patientGroupMaster.setVisitTypeId(patientGroupMasterList.getVisitTypeId());
					patientGroupMaster.setPaymentEntitlementId(patientGroupMasterList.getPaymentEntitlementId());
					patientGroupMaster.setPatientCategoryId(patientGroupMasterList.getPatientCategoryId());
					patientGroupMaster.setProductCategoryId(patientGroupMasterList.getProductCategoryId());
					patientGroupMaster.setAssetCategoryId(patientGroupMasterList.getAssetCategoryId());
					patientGroupMaster.setItemId(patientGroupMasterList.getItemId());
					save(patientGroupMaster);
				
			

				
			}
			
			if(ids.size() > 0) {
				sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM billing.m_patient_type_group_wise_details where patient_type_group_id IN (:patientTypeGroupId)")
			.setParameterList("patientTypeGroupId", ids)
			.executeUpdate();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getPatientTypeGroupWiseDetails(int patientTypeId,int organizationId,int unitId) {
		try {
			List<PatientTypeGroupWiseDetailsDto> patientTypeGroupList =sessionFactory.getCurrentSession()
					.createSQLQuery(" select " 
							 + " 	pts.patient_type_group_id as \"patientTypeGroupId\", " 
							 + " 	pts.speciality_id as \"specialityId\", " 
							 + " 	pts.sub_speciality_id as \"subSpecialityId\", " 
							 + " 	pts.percentage as \"percentage\", " 
							 + " 	pts.code as \"code\", " 
							 + " 	pts.description as \"description\", " 
							 + " 	pts.markup_status as \"markupStatus\", " 
							 + " 	pts.status as \"status\", " 
							 + " 	pts.unit_id as \"unitId\", " 
							 + " 	serviceM.service_standard_name as \"serviceName\", " 
							 + " 	serviceM.service_standard_code as \"serviceCode\", " 
							 + " 	serviceM.service_master_id as \"serviceId\", " 
							 + " 	item.name as \"itemName\", " 
							 + " 	item.code as \"itemCode\", " 
							 + " 	product.category as \"productCategoryName\", " 
							 + " 	asset.category as \"assetCategoryName\", " 
							 + " 	asset.id as \"assetCategoryId\", " 
							 + " 	pts.organization_id as \"organizationId\", " 
							 + " 	to_char(pts.created_date, 'dd/mm/yyyy') as \"createdDate\", " 
							 + " 	pts.created_by as \"createdBy\", " 
							 + " 	to_char(pts.updated_date, 'dd/mm/yyyy') as \"updatedDate\", " 
							 + " 	pts.updated_by as \"updatedBy\", " 
							 + " 	sp.speciality_name as \"specialityName\", " 
							 + " 	ssm.sub_speciality_name as \"subSpecialityName\", " 
							 + " 	pts.tariff_id as \"tariffId\", " 
							 + " 	pts.patient_category_id as \"patientCategoryId\", " 
							 + " 	pts.payment_entitlement_id as \"paymentEntitlementId\", " 
							 + " 	pts.visit_Type_id as \"visitTypeId\", " 
							 + " 	visit.visit_type_code as \"visitType\", " 
							 + " 	tariff.tariff_desc as \"tariffDesc\", " 
							 + " 	pCat.patient_category as \"patientCategory\", " 
							 + " 	paymentEntlment.payment_entitlement_desc as \"paymentEntitlementDesc\" " 
							 + " from " 
							 + " 	billing.m_patient_type_group_wise_details pts " 
							 + " left join doctor.m_speciality_master sp on " 
							 + " 	pts.speciality_id = sp.speciality_id " 
							 + " left join doctor.m_sub_speciality_master ssm on " 
							 + " 	pts.sub_speciality_id = ssm.sub_speciality_id " 
							 + " left join service.m_service_master serviceM on " 
							 + " 	serviceM.service_master_id = pts.service_id " 
							 + " left join public.m_item item on " 
							 + " 	item.id = pts.item_id " 
							 + " left join public.m_product_category product on " 
							 + " 	product.id = pts.product_category_id " 
							 + " left join public.m_asset_category asset on " 
							 + " 	asset.id = pts.asset_category_id " 
							 + " left join public.m_visit_type_master visit on " 
							 + " 	visit.visit_type_id = pts.visit_type_id " 
							 + " left join public.m_payment_entitlement_master paymentEntlment on " 
							 + " 	paymentEntlment.payment_entitlement_id = pts.payment_entitlement_id	 " 
							 + " left join public.m_patient_category_master pCat on " 
							 + " 	pCat.patient_category_id = pts.patient_category_id " 
							 + " left join service.m_tariff_master tariff on " 
							 + " 	tariff.tariff_id = pts.tariff_id " 
							 + " where pts.patient_type_id =:patientTypeId "
							 + " 	AND pts.organization_id =:organizationId "
							 + " 	AND pts.unit_id =:unitId ")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("patientTypeId", patientTypeId)
					.setResultTransformer(Transformers.aliasToBean(PatientTypeGroupWiseDetailsDto.class)).list();
			

				return new Response(SUCCESS, SUCCESS_CODE, "success", patientTypeGroupList, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updatePatientTypeGroupStatus(PatientTypeGroupWiseDetailsDto patientTypeGroupWiseDetailsDto) {
		try {
			// BATCH-4 org-trust fix (CRUD-sibling of the batch-2 patientType toggle guard): this
			// toggle was a bare write-by-ID (findById + update, zero org check). Reject rows not
			// owned by the verified tenant.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			PatientTypeGroupWiseDetailsMaster master = findById(patientTypeGroupWiseDetailsDto.getPatientTypeGroupId());
			if (master == null || master.getOrganizationId() == null
					|| !master.getOrganizationId().equals(verifiedOrgId)
					|| master.getUnitId() == null
					|| !master.getUnitId().equals(verifiedUnitId)) {
				return new Response<>(ERROR, ERROR_CODE, "Cross-tenant update rejected", null, null);
			}
			master.setUpdatedBy(patientTypeGroupWiseDetailsDto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(patientTypeGroupWiseDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			master.setStatus(patientTypeGroupWiseDetailsDto.getStatus().toString());
			update(master);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
