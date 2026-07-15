package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import com.param.global.common.DateConverter;
@NamedNativeQueries({
		@NamedNativeQuery(name="SEARCH_SERVICE_MASTER_BY_UNIT_AND_ORG", 
				query=" SELECT DISTINCT service.service_master_id as \"serviceMasterId\", "
						+ "service.organization_id as \"organizationId\", "
						+ "service.service_standard_code as \"serviceStandardCode\", "
						+ " service.service_standard_name as \"serviceStandardName\", service.speciality_id as \"specialityId\","
						+ " service.sub_speciality_id as \"subSpecialityId\""
						+ " FROM service.m_service_master service "
						+ " INNER JOIN service.t_unit_service_mapper mapper ON service.service_master_id = mapper.service_id "					
						+ " WHERE lower(service.service_standard_name) like :searchKeyword "
						+ " AND service.speciality_id =:specialityId"
						+ " AND service.sub_speciality_id =:subSpecialityId"
						+ " AND mapper.unit_id =:unitId"
						+ " AND mapper.orgnisation_id=:orgId"
						+ " AND mapper.status ='A'"),
		
		@NamedNativeQuery(name="GET_SERVICES_BY_UNIT_AND_ORG", 
		query=" SELECT service.service_master_id as \"serviceMasterId\", "
				+ " service.service_standard_name as \"serviceStandardName\" "
				+ " FROM service.t_unit_service_mapper mapper "
				+ " INNER JOIN service.m_service_master service ON service.service_master_id = mapper.service_id "
				+ " WHERE service.speciality_id =:specialityId"
				+ " AND service.sub_speciality_id =:subSpecialityId"
				+ " AND mapper.unit_id =:unitId"
				+ " AND mapper.orgnisation_id=:orgId"
				+ " AND mapper.status ='A'"
				+ " ORDER BY service.service_standard_name ASC "),
		
		@NamedNativeQuery(name="SEARCH_SERVICE_MASTER_BY_WITHOUT_SPE_SUBSPE_UNIT_AND_ORG", 
		query=" SELECT DISTINCT service.service_master_id as \"serviceMasterId\", "
				+ "service.organization_id as \"organizationId\", "
				+ "service.service_standard_code as \"serviceStandardCode\", "
				+ "sm.speciality_name as \"specialityName\", "
				+ "ssm.sub_speciality_name as \"subSpecialityName\", "
				+ " service.service_standard_name as \"serviceStandardName\", service.speciality_id as \"specialityId\","
				+ " service.sub_speciality_id as \"subSpecialityId\",sm.is_rendering_speciality as \"isRenderingSpeciality\" "
				+ " FROM service.m_service_master service "
				+ " INNER JOIN service.t_unit_service_mapper mapper ON service.service_master_id = mapper.service_id "
				+ " INNER JOIN doctor.m_speciality_master sm ON service.speciality_id = sm.speciality_id "
				+ " INNER JOIN doctor.m_sub_speciality_master ssm ON service.sub_speciality_id = ssm.sub_speciality_id "
				+ " INNER JOIN service.t_unit_service_master_details dtls on dtls.service_master_id=mapper.service_id"
				+ " WHERE lower(service.service_standard_name) like :searchKeyword "
				+ " AND sm.is_rendering_speciality ='Y'"
				+ " AND mapper.unit_id =:unitId"
				+ " AND mapper.orgnisation_id=:orgId"
				+ " AND mapper.status ='A'"
				+ " AND (dtls.gender=:gender OR dtls.gender=0)"),
		
		
		@NamedNativeQuery(name="SEARCH_SERVICE_MASTER_BY_UNIT_AND_ORG_SPECIALITY_SUBSPECILAITY_LIST", 
				query=" select " 
						 + " 	service.service_master_id as \"serviceId\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	service.sub_speciality_id as \"subSpecialityId\", " 
						 + " 	service.service_standard_code as \"serviceStandardCode\", " 
						 + " 	service.service_standard_name as \"serviceStandardName\", " 
						 + " 	speciality.speciality_name as \"specialityName\", " 
						 + " 	subSpciality.sub_speciality_name as \"subSpecialityName\", " 
						 + " 	dtls.is_procedure as \"isProcedure\", " 
						 + " 	dtls.bill_of_material as \"billOfMaterial\"  " 
						 + " from " 
						 + " 	service.m_service_master service " 
						 + " inner join service.t_unit_service_mapper mapper on " 
						 + " 	service.service_master_id = mapper.service_id " 
						 + " inner join service.t_unit_service_master_details dtls on " 
						 + " 	dtls.service_master_id = service.service_master_id " 
						 + " inner join doctor.m_speciality_master speciality on " 
						 + " 	service.speciality_id = speciality.speciality_id " 
						 + " inner join doctor.m_sub_speciality_master subSpciality on " 
						 + " 	service.sub_speciality_id = subSpciality.sub_speciality_id " 
						 + " where " 
						 + " 	mapper.unit_id =:unitId " 
						 + " 	and dtls.unit_id = mapper.unit_id " 
						 + " 	and mapper.orgnisation_id =:orgId " 
						 + " 	and mapper.status = 'A' " ),
		
		@NamedNativeQuery(name="GET_MAPPED_SERVICES_BY_UNIT",
				query = "SELECT mapper.service_id as\"serviceId\", mapper.unit_id as\"unitId\", mapper.orgnisation_id as \"orgnisationId\", "
						+ " service.service_standard_name as \"serviceStandardName\", speciality.speciality_name as \"specialityName\","
						+ " subspe.sub_speciality_name as\"subSpecialityName\", mapper.status as \"status\""
						+ " FROM service.t_unit_service_mapper mapper"
						+ " INNER JOIN service.m_service_master service ON mapper.service_id = service.service_master_id"
						+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
						+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
						+ " WHERE mapper.unit_id =:unitId AND mapper.orgnisation_id =:orgId ORDER BY mapper.service_id DESC"),
		
		@NamedNativeQuery(name="UPDATE_UNIT_SERVICE_MAPPER_STATUS",
				query ="UPDATE service.t_unit_service_mapper SET status =:status"
						+ " WHERE service_id =:serviceId AND unit_id =:unitId AND orgnisation_id =:orgId"),
		
		@NamedNativeQuery(name = "GET_COUNT OF_MAPPED_SERVICES_BY_UNIT",
				query = "SELECT COUNT(*) FROM service.t_unit_service_mapper mapper"
						+ " INNER JOIN service.m_service_master service ON mapper.service_id = service.service_master_id"
						+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
						+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
						+ " WHERE mapper.unit_id =:unitId AND mapper.orgnisation_id =:orgId"),
		
		@NamedNativeQuery(name="SEARCH_SERVICES_BY_NAME_AND_CODE",
				query="select "
						 + " 	service.service_master_id as \"serviceMasterId\", "
						 + " 	service.service_standard_name as \"serviceStandardName\" , "
						 + " 	service.service_standard_code as \"serviceStandardCode\", "
						 + " 	cast ('0' as DOUBLE precision) as \"concession\", "
						 + " 	speciality.speciality_id as \"specialityId\", "
						 + " 	service.medall_service_id as \"medallServiceId\", "
						 + " 	speciality.speciality_name as \"specialityName\", "
						 + " 	sub_speciality.sub_speciality_id as \"subSpecialityId\", "
						 + " 	sub_speciality.sub_speciality_name as \"subSpecialityName\", "
						 + " 	details.is_rate_editable as \"isRateEditable\", "
						 + " 	details.is_tax_applicable as \"isTaxApplicable\", "
						 + " 	details.is_discount as \"isDiscountApplicable\", "
						 + " 	details.gst_type_id as \"taxId\", "
						 + " 	details.is_ref_doctor_share as \"isRefDoctorShare\", "
						 + " 	details.is_doc_req as \"isDoctorRequired\", "
						 + " 	details.pre_requisite as \"preRequisite\", "
						 + " 	details.pre_requisite_details as \"preRequisiteDetails\", "
						 + " 	bpm.base_price as \"basePriceForContract\", "
						 + " 	speciality.is_rendering_speciality as \"isRenderingSpeciality\" , "
						 + " 	details.is_allow_multiple_quantity as \"isQuantityEditable\", "
						 + " 	details.is_appointable as \"isAppointable\" "
						 + " from "
						 + " 	service.m_service_master service "
						 + " inner join service.t_unit_service_master_details details on "
						 + " 	details.service_master_id = service.service_master_id "
						 + " inner join service.m_service_base_price_master bpm on "
						 + " 	bpm.service_master_id = service.service_master_id "
						 + " inner join doctor.m_speciality_master speciality on "
						 + " 	service.speciality_id = speciality.speciality_id "
						 + " inner join doctor.m_sub_speciality_master sub_speciality on "
						 + " 	service.sub_speciality_id = sub_speciality.sub_speciality_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = details.unit_id "
						 + " where "
						 + " 	details.unit_id =:unitId "
						 + " 	and bpm.unit_id =:unitId "
						 + " 	and service.organization_id =:orgId "
						 + " 	and service.status = 'A' "
						 + " 	and details.is_package = 'I' "
						 + " 	and details.status = 'A' "
						 + "	and (tuabc.pharmacy_service_id is null or tuabc.pharmacy_service_id != service.service_master_id) "
						 + " 	and (details.gender =:gender "
						 + " 	or details.gender = 0) "
						 + " 	and(lower(service.service_standard_name) like :searchKeyword "
						 + " 	or lower(service.service_standard_code) like :searchKeyword) "),
		
		@NamedNativeQuery(name="SEARCH_SERVICES_BY_NAME_AND_CODE_UNIT_APPLICATION_SERVICE",
		query="select "
				 + " 	service.service_master_id as \"serviceMasterId\", "
				 + " 	service.service_standard_name as \"serviceStandardName\" , "
				 + " 	service.service_standard_code as \"serviceStandardCode\"  "
				 + " from "
				 + " 	service.m_service_master service "
				 + " inner join service.t_unit_service_master_details details on "
				 + " 	details.service_master_id = service.service_master_id "
				 + " where "
				 + " 	details.unit_id =:unitId "
				 + " 	and service.organization_id =:orgId "
				 + " 	and service.status = 'A' "
				 + " 	and details.is_package = 'I' "
				 + " 	and details.status = 'A' "
				 + " 	and(lower(service.service_standard_name) like :searchKeyword "
				 + " 	or lower(service.service_standard_code) like :searchKeyword) "),
		
		@NamedNativeQuery(name="SEARCH_GET_ACTIVE_AREA_BY_CITY_ID_DATA",
		query="select " 
				+"	areamaster0_.area_id as \"areaId\", " 
				+"	areamaster0_.area_name as \"areaName\", " 
				+"	areamaster0_.post_code as \"postCode\" " 
				+" from "  
				+"	public.m_area_master areamaster0_ " 
				+" where " 
				+"	areamaster0_.city_id =:cityId  "  
				+"	and areamaster0_.status = 'A' " 
				+" 	and(lower(areamaster0_.area_name) like :searchKeyword "
				+" or lower(areamaster0_.area_code) like :searchKeyword) ORDER BY areamaster0_.area_name asc"),
		
		
		@NamedNativeQuery(name="SURCHARGE_SERVICE_DETAILS",
		query="select service.service_master_id as \"serviceMasterId\", "
				+ "service.service_standard_name as \"serviceStandardName\" ,"
				+ "service.service_standard_code as \"serviceStandardCode\", "
				+ "CAST ('0' AS DOUBLE PRECISION) as \"concession\", "
				+ " speciality.speciality_id as \"specialityId\", "
				+ " speciality.speciality_name as \"specialityName\","
				+ " sub_speciality.sub_speciality_id as \"subSpecialityId\", "
				+ " sub_speciality.sub_speciality_name as \"subSpecialityName\", "
				+ " details.is_rate_editable as \"isRateEditable\","
				+ " details.rate_editble_min_value as \"minRateEditable\", "
				+ " details.rate_editble_min_value as \"maxRateEditable\", "
				+ " details.is_tax_applicable as \"isTaxApplicable\","
				+ " details.is_discount as \"isDiscountApplicable\","
				+ " details.discount_value as \"discount\", "
				+ " details.gst_type_id as \"taxId\", "
				+ " details.is_ref_doctor_share as \"isRefDoctorShare\", " //it is nothing but same
				+ " details.is_doc_req as \"isDoctorRequired\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercentage\", "
				+ " details.is_allow_multiple_quantity as \"isQuantityEditable\" "
				+ " FROM service.t_unit_service_mapper mapper " 
				+ " inner join service.m_service_master service on service.service_master_id  = mapper.service_id"
				+ " inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id"
				+ " inner JOIN service.m_service_base_price_master bpm ON bpm.service_master_id = mapper.service_id "
				+ " inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id"
				+ " inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id "
				+ " left join public.m_tax tax on tax.id=details.gst_type_id " 
				+ " WHERE details.unit_id = bpm.unit_id "
				+ " AND mapper.unit_id = details.unit_id "
				+ " AND mapper.unit_id =:unitId "
				+ " AND mapper.orgnisation_id=:orgId "
				+ " AND service.status='A' "
				+ " AND service.service_master_id=(select surcharge_service_id from t_unit_application_billing_configuration where organization_id=:orgId "
					+ "AND unit_id=:unitId AND status = 'A' limit 1) "),
		
		
		@NamedNativeQuery(name="CHECK_SERVICE_AVAILABLE_IN_UNIT_SERVICE_MAPPER",
		query =" select unitSrvMpper.service_id as \"serviceId\" "
				+ " from service.t_unit_service_mapper unitSrvMpper "
				+ " WHERE service_id IN (:serviceId) "
				+ " AND orgnisation_id=:orgId "
				+ " AND unit_id=:unitId "),
		
		@NamedNativeQuery(name="GET_SERVICE_BY_ID_FOR_PACKAGE_CONSUMP_VALIDATION",
		query="select service.service_master_id as \"serviceMasterId\", "
				+ "service.service_standard_name as \"serviceStandardName\" ,"
				+ "service.service_standard_code as \"serviceStandardCode\", "
				+ "CAST ('0' AS DOUBLE PRECISION) as \"concession\", "
				+ " speciality.speciality_id as \"specialityId\", "
				+ " speciality.speciality_name as \"specialityName\","
				+ " sub_speciality.sub_speciality_id as \"subSpecialityId\", "
				+ " sub_speciality.sub_speciality_name as \"subSpecialityName\", "
				+ " details.is_rate_editable as \"isRateEditable\","
				+ " details.rate_editble_min_value as \"minRateEditable\", "
				+ " details.rate_editble_min_value as \"maxRateEditable\", "
				+ " details.is_tax_applicable as \"isTaxApplicable\","
				+ " details.is_discount as \"isDiscountApplicable\","
				+ " details.discount_value as \"discount\", "
				+ " details.gst_type_id as \"taxId\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercentage\", "
				+ " details.is_allow_multiple_quantity as \"isQuantityEditable\" "
				+ " FROM service.t_unit_service_mapper mapper " 
				+ " inner join service.m_service_master service on service.service_master_id  = mapper.service_id"
				+ " inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id"
				+ " inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id"
				+ " inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id "
				+ " left join public.m_tax tax on tax.id=details.gst_type_id " 
				+ " WHERE  mapper.status = 'A' "
				+ " AND mapper.unit_id = details.unit_id and details.unit_id = :unitId and details.status = 'A' "
				+ " AND service.service_master_id =:serviceId "),
		
		@NamedNativeQuery(name="SEARCH_PROCEDURE_BY_SPECIALITY", 
		query="select service.service_master_id as \"serviceMasterId\", "
				+ "service.service_standard_name as \"serviceStandardName\" ,"
				+ "service.service_standard_code as \"serviceStandardCode\", "
                + "procedure_config.cpt_code as \"cptCode\", "
				+ "CAST ('0' AS DOUBLE PRECISION) as \"concession\", "
				+ " speciality.speciality_id as \"specialityId\", "
				+ " speciality.speciality_name as \"specialityName\","
				+ " sub_speciality.sub_speciality_id as \"subSpecialityId\", "
				+ " sub_speciality.sub_speciality_name as \"subSpecialityName\", "
				+ " details.is_rate_editable as \"isRateEditable\","
				+ " details.rate_editble_min_value as \"minRateEditable\", "
				+ " details.rate_editble_min_value as \"maxRateEditable\", "
				+ " details.is_tax_applicable as \"isTaxApplicable\","
				+ " details.is_discount as \"isDiscountApplicable\","
				+ " details.discount_value as \"discount\", "
				+ " details.gst_type_id as \"taxId\", "
				+ " details.is_ref_doctor_share as \"isRefDoctorShare\", " //it is nothing but same
				+ " details.is_doc_req as \"isDoctorRequired\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercentage\", "
				+ " details.is_allow_multiple_quantity as \"isQuantityEditable\", "
				+ " procedure_config.duration as \"procedureDuration\","
				+ " details.procedure_type_id as \"procedureTypeId\","
				+ " procedure_config.procedure_config_id as \"procedureConfigurationId\" "
				+ " FROM service.t_unit_service_mapper mapper " 
				+ " inner join service.m_service_master service on service.service_master_id  = mapper.service_id"
				+ " inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id"
				+ " inner JOIN service.m_service_base_price_master bpm ON bpm.service_master_id = mapper.service_id "
				+ " inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id"
				+ " inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id "
				+ " left join otims.t_procedure_configuration procedure_config on service.service_master_id = procedure_config.service_id "
				+ " left join public.m_tax tax on tax.id=details.gst_type_id " 
				+ " WHERE mapper.unit_id = details.unit_id "
				+ " AND bpm.unit_id = mapper.unit_id "
				+ " AND mapper.unit_id =:unitId "
				+ " AND mapper.orgnisation_id=:orgId "
				+ " AND service.status='A' " 
				+ " AND details.is_procedure = 'Y'"
				+ " AND details.procedure_type_id is not null "
				+ " AND service.speciality_id =:specialityId"
				+ " AND(lower(service.service_standard_name )like :searchKeyword OR lower(service.service_standard_code) like :searchKeyword)"),
				@NamedNativeQuery(name="SEARCH_SERVICE_MASTER_BY_ORG_UNIT_GENDER", 
				query=" SELECT DISTINCT service.service_master_id as \"serviceMasterId\", "
						+ "service.organization_id as \"organizationId\", "
						+ "service.service_standard_code as \"serviceStandardCode\", "
						+ " service.service_standard_name as \"serviceStandardName\", service.speciality_id as \"specialityId\","
						+ " service.sub_speciality_id as \"subSpecialityId\""
						+ " FROM service.m_service_master service "
						+ " INNER JOIN service.t_unit_service_mapper mapper ON service.service_master_id = mapper.service_id "
						+ " INNER JOIN service.t_unit_service_master_details dtls on dtls.service_master_id=mapper.service_id "
						+ " WHERE lower(service.service_standard_name) like :searchKeyword "
						+ " AND service.speciality_id =:specialityId"
						+ " AND service.sub_speciality_id =:subSpecialityId"
						+ " AND mapper.unit_id =:unitId"
						+ " AND mapper.orgnisation_id=:orgId "
						+ " AND (dtls.gender=:gender OR dtls.gender=0) "
						+ " AND mapper.status ='A'"),
				
				@NamedNativeQuery(name="GET_MAPPED_SERVICES_BY_UNIT_SEARCH",
				query = "SELECT mapper.service_id as\"serviceId\", mapper.unit_id as\"unitId\", mapper.orgnisation_id as \"orgnisationId\", "
						+ " service.service_standard_name as \"serviceStandardName\", speciality.speciality_name as \"specialityName\","
						+ " subspe.sub_speciality_name as\"subSpecialityName\", mapper.status as \"status\""
						+ " FROM service.t_unit_service_mapper mapper"
						+ " INNER JOIN service.m_service_master service ON mapper.service_id = service.service_master_id"
						+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
						+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
						+ " WHERE mapper.unit_id =:unitId AND mapper.orgnisation_id =:orgId "
						+ " AND(lower(service.service_standard_name )) like :searchKeyword  "
						+ " ORDER BY mapper.service_id DESC"),
				
				/*@NamedNativeQuery(name="SEARCH_ITEMS_BY_NAME_AND_CODE",
				query="select item.id as \"itemId\", "
						+ "item.name as \"itemName\" ,"
						+ "item.product_code as \"itemCode\" ,"
						+ "product.category as \"productCategoryName\", "
						+ " product.id as \"productCategoryId\", "
						+ " asset.category as \"assetCategoryName\","
						+ " asset.id as \"assetCategoryId\" "
						+ " FROM public.m_item item " 
						+ " inner join public.m_product_category product on product.id  = item.product_category_id"
						+ " inner join public.m_asset_category asset on asset.id = product.asset_category_id"
						+ " WHERE item.unit_id =:unitId "
						+ " AND item.organization_id=:orgId "
						+ " AND item.is_active=true "
						+ " AND(lower(item.name )like :searchKeyword OR lower(item.product_code) like :searchKeyword)"),*/
				
				
				@NamedNativeQuery(name="SEARCH_ITEMS_BY_NAME_AND_CODE",
				query="with items as "
						+ "(	 "
						+ " select   "
						+ " item.id as \"itemId\",   "
						+ " (case when item.product_category_id is null then generic.product_category_id else item.product_category_id end) as \"productCategoryId\",   "
						+ " item.code as \"itemCode\",   "
						+ " item.name as \"itemName\"   "
						+ " from public.m_item as item   "
						+ " left JOIN public.m_generic generic ON generic.id = item.generic_id and generic.is_deleted = false   "
						+ " and generic.is_active = true    "
						+ " inner join public.m_manufacturer mf  on mf.id = item.manufacturer_id and  mf.is_deleted = false   "
						+ " and mf.is_active = true   "
						+ " where  "
						+ " "
						+ "  item.is_deleted = false   "
						+ " and item.is_active = true   "
						+ " and item.unit_id=:unitId   "
						+ " AND (lower(item.name )like :searchKeyword OR lower(item.product_code) like :searchKeyword) AND item.organization_id=:orgId "
						+ " ) select it.*, "
						+ "   ac.category as \"assetCategoryName\", "
						+ "   pc.category as \"productCategoryName\", ac.id as \"assetCategoryId\" from items it "
						+ "  inner join public.m_product_category pc  on pc.id = it.\"productCategoryId\" and pc.is_deleted = false   "
						+ " and pc.is_active = true   "
						+ " inner join public.m_asset_category ac on ac.id = pc.asset_category_id and ac.is_deleted = false   "
						+ " and ac.is_active = true  "),
				
				@NamedNativeQuery(name="SEARCH_SERVICE_MASTER_BY_UNIT_AND_ORG_SERVICE_NAME_OR_CODE", 
				query=" select " 
						 + " 	service.service_master_id as \"serviceId\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	service.sub_speciality_id as \"subSpecialityId\", " 
						 + " 	service.service_standard_code as \"serviceStandardCode\", " 
						 + " 	service.service_standard_name as \"serviceStandardName\", " 
						 + " 	speciality.speciality_name as \"specialityName\", " 
						 + " 	subSpciality.sub_speciality_name as \"subSpecialityName\", " 
						 + " 	dtls.is_procedure as \"isProcedure\", " 
						 + " 	dtls.bill_of_material as \"billOfMaterial\"  " 
						 + " from " 
						 + " 	service.m_service_master service " 
						 + " inner join service.t_unit_service_mapper mapper on " 
						 + " 	service.service_master_id = mapper.service_id " 
						 + " inner join service.t_unit_service_master_details dtls on " 
						 + " 	dtls.service_master_id = service.service_master_id " 
						 + " inner join doctor.m_speciality_master speciality on " 
						 + " 	service.speciality_id = speciality.speciality_id " 
						 + " inner join doctor.m_sub_speciality_master subSpciality on " 
						 + " 	service.sub_speciality_id = subSpciality.sub_speciality_id " 
						 + " where " 
						 + " 	mapper.unit_id =:unitId " 
						 + " 	and dtls.unit_id = mapper.unit_id " 
						 + " 	and mapper.orgnisation_id =:orgId "
						 + "    and(lower(service.service_standard_name ) like :searchKeyword OR lower(service.service_standard_code) like :searchKeyword )" 
						 + " 	and mapper.status = 'A' " ),

				@NamedNativeQuery(name="SEARCH_SERVICES_BY_NAME_AND_CODE_FOR_PATHO_AND_RADIO",
				query="select distinct"
						+ " 	service.service_master_id as \"serviceMasterId\", "
						+ " 	service.service_standard_name as \"serviceStandardName\" , "
						+ " 	service.service_standard_code as \"serviceStandardCode\", "
						+ " 	cast ('0' as DOUBLE precision) as \"concession\", "
						+ " 	speciality.speciality_id as \"specialityId\", "
						+ " 	service.medall_service_id as \"medallServiceId\", "
						+ " 	speciality.speciality_name as \"specialityName\", "
						+ " 	sub_speciality.sub_speciality_id as \"subSpecialityId\", "
						+ " 	sub_speciality.sub_speciality_name as \"subSpecialityName\", "
						+ " 	details.is_rate_editable as \"isRateEditable\", "
						+ " 	details.is_tax_applicable as \"isTaxApplicable\", "
						+ " 	details.is_discount as \"isDiscountApplicable\", "
						+ " 	details.gst_type_id as \"taxId\", "
						+ " 	details.is_ref_doctor_share as \"isRefDoctorShare\", "
						+ " 	details.is_doc_req as \"isDoctorRequired\", "
						+ " 	details.pre_requisite as \"preRequisite\", "
						+ " 	details.pre_requisite_details as \"preRequisiteDetails\", "
						+ " 	bpm.base_price as \"basePriceForContract\", "
						+ " 	speciality.is_rendering_speciality as \"isRenderingSpeciality\" , "
						+ " 	details.is_allow_multiple_quantity as \"isQuantityEditable\", "
						+ " 	details.is_appointable as \"isAppointable\" "
						+ " from "
						+ " 	service.m_service_master service "
						+ " inner join service.t_unit_service_master_details details on "
						+ " 	details.service_master_id = service.service_master_id "
						+ " inner join service.m_service_base_price_master bpm on "
						+ " 	bpm.service_master_id = service.service_master_id "
						+ " inner join doctor.m_speciality_master speciality on "
						+ " 	service.speciality_id = speciality.speciality_id "
						+ " inner join doctor.m_sub_speciality_master sub_speciality on "
						+ " 	service.sub_speciality_id = sub_speciality.sub_speciality_id "
						+ " inner join t_unit_application_billing_configuration tuabc on "
						+ " 	tuabc.unit_id = details.unit_id "
						+ "	inner join t_lab_configuration tlc  on "
						+ "		tlc.unit_id  = tuabc .unit_id "
						+ " where "
						+ " 	details.unit_id =:unitId "
						+ " 	and bpm.unit_id =:unitId "
						+ " 	and tuabc.unit_id =:unitId "
						+ " 	and service.organization_id =:orgId "
						+ " 	and service.status = 'A' "
						+ " 	and details.is_package = 'I' "
						+ " 	and details.status = 'A' "
						+ " 	and service.speciality_id in(tlc.lab_speciality_id, tuabc.radiology_speciality_id) "
						+ "	and (tuabc.pharmacy_service_id is null or tuabc.pharmacy_service_id != service.service_master_id) "
						+ " 	and (details.gender =:gender "
						+ " 	or details.gender = 0) "
						+ " 	and(lower(service.service_standard_name) like :searchKeyword "
						+ " 	or lower(service.service_standard_code) like :searchKeyword) "),
		
})
@Entity
@Table(name="t_unit_service_mapper", schema="service")
public class UnitServiceMapper {
	
	@EmbeddedId
	private UnitServiceMapperId unitServiceMapperId;
	
	@Column(name = "orgnisation_id")
	private Integer orgnisationId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public UnitServiceMapperId getUnitServiceMapperId() {
		return unitServiceMapperId;
	}

	public void setUnitServiceMapperId(UnitServiceMapperId unitServiceMapperId) {
		this.unitServiceMapperId = unitServiceMapperId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status =  status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
