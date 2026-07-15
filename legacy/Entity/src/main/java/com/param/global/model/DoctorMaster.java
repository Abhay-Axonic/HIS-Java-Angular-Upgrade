package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.OccupationMaster;
import com.param.adt.master.global.model.UserMaster;


@NamedQueries({
	
	
	@NamedQuery(name="GET_DOCTOR_DETAILS",query="SELECT doctor.doctorId as doctorId, "
			+ "doctor.accessCardNumber as accessCardNumber, "
			+ "doctor.prefixId as prefixId, "
			+ "'Dr' as prefixCode, "
			+ "coalesce(doctor.firstName,'') as firstName, "
			+ "coalesce(doctor.middleName,'') as middleName, "
			+ "coalesce(doctor.lastName,'') as lastName, "
			+ "doctor.genderId as genderId, "
			+ "to_char(doctor.dob,'MM/DD/YYYY') as dob, "
			+ "to_char(doctor.dob,'DD/MM/YYYY') as birthDate, "
			+ "doctor.dob as birthDateNew, "
			+ "doctor.pwdExpiry as pwdExpiry, "
			+ "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, doctor.dob)),'999')) as ageString, "
			+ "doctor.mobileNumber as mobileNumber, "
			+ "doctor.countryCallingCode as countryCallingCode, "
			+ "doctor.barCode as barCode, "
			+ "doctor.registrationNumber as registrationNumber, "
			+ "doctor.isVip as isVip, "
			+ "doctor.vipRemark as vipRemark, "
			+ "doctor.identificationTypeId as identificationTypeId, "
			+ "identificationMaster.identificationName as identificationName, "
			+ "doctor.identificationNumber as identificationNumber, "
			+ "doctor.email as email,"
			+ "doctor.docCategoryId as docCategoryId, "
			+ "doctorCategoryMaster.doctorCategoryDescription as docCategoryDescription, "
			+ "doctor.aliseName as aliseName, "
			+ "to_char(doctor.identificationExpiryDate,'MM/DD/YYYY') as identificationExpiryDate, "
			+ "doctor.occupationId as occupationId, "
			+ "doctor.specialityId as specialityId, "
			+ "specialityMaster.specialityName as specialityName, "
			+ "doctor.subSpecialityId as subSpecialityId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityName, "
			+ "doctor.docDesignationId as docDesignationId, "
			+ "employeeDesignationMaster.employeeDesignationDescription as docDesignationDescription, "
			+ "doctor.docCode as docCode, "
			+ "to_char(doctor.dateOfJoining,'MM/DD/YYYY') as dateOfJoining, "
			+ "to_char(doctor.createdDate,'DD-MM-YYYY HH:MM:SS') as createdDate, "
			+ "doctor.createdBy as createdBy, "
			+ "doctor.status as status, "
			+ "doctor.allowedOverBookingSlots as allowedOverBookingSlots, "
			+ "doctor.userId as userId, "
			+ "doctor.unitId as unitId, "
			+ "doctor.organizationId as organizationId, "
			+ "doctor.doctorImage as doctorImage, "
			+ "doctor.registrationNumber as registrationNumber, "
			+ "doctor.slotTimeSpan as slotTimeSpan,doctor.isRMO as isRMO,doctor.doctorPin as doctorPin, "
			+ "doctorDetails.doctorDetailsId as doctorDetailsId, "
			+ "doctorDetails.nationalityId as nationalityId, "
			+ "doctorDetails.raceId as raceId, "
			+ "doctorDetails.maritalStatusId as maritalStatusId,"
			+ " doctor.processShareAgainstUnsettledBill as processShareAgainstUnsettledBill, "
			+ "doctorDetails.address as address,"
			+ "doctorDetails.countryId as countryId, "
			+ "doctorDetails.stateId as stateId, "
			+ "doctorDetails.districtId as districtId, "
			+ "doctorDetails.cityId as cityId, "
			+ "doctorDetails.areaId as areaId, "
			+ "area.areaName as areaName, "
			+ "doctorDetails.zipCode as zipCode, "
			+ "doctorDetails.phoneCode as phoneCode, "
			+ "doctorDetails.phoneNumber as phoneNumber, "
			+ "doctorDetails.permanentAddress as permanentAddress, "
			+ "doctorDetails.permanentCountryId as permanentCountryId, "
			+ "doctorDetails.permanentStateId as permanentStateId, "
			+ "doctorDetails.permanentDistrictId as permanentDistrictId, "
			+ "doctorDetails.permanentCityId as permanentCityId, "
			+ "doctorDetails.permanentAreaId as permanentAreaId, "
			+ "doctorDetails.permanentZipCode as permanentZipCode, "
			+ "doctorDetails.permanentPhoneCode as permanentPhoneCode, "
			+ "doctorDetails.permanentPhoneNumber as permanentPhoneNumber, "
			+ "doctorDetails.bankName as bankName, "
			+ "doctorDetails.accountNumber as accountNumber, "
			+ "doctorDetails.bankBranchName as bankBranchName, "
			+ "identificationMaster.isExpiryRequired as isExpiryRequired,"
			+ "doctorDetails.ifscCode as ifscCode ,"
			+ "doctor.doctorImage as doctorImage,  "
			+ "doctor.creditDays as creditDays,  "
            + "doctor.generalLedgerId as generalLedgerId "
			+ "FROM DoctorMaster doctor "
			+ "INNER JOIN doctor.doctorDetailslist doctorDetails "
			+ "LEFT JOIN doctorDetails.areaMaster area "
			+ "LEFT JOIN doctor.specialityMaster specialityMaster "
			+ "LEFT JOIN doctor.subSpecialityMaster subSpecialityMaster "
			+ "LEFT JOIN doctor.doctorCategoryMaster doctorCategoryMaster "
			+ "LEFT JOIN doctor.employeeDesignationMaster employeeDesignationMaster "
			+ "LEFT JOIN doctor.identificationMaster identificationMaster "
			+ "WHERE doctor.organizationId=:organizationId "),
	
	
	
	   @NamedQuery(name = "UPDATE_DOCTOR_IMAGE_PATH",
	    query = "UPDATE DoctorMaster SET doctorImage=:doctorImage"
	        + " WHERE doctorId=:doctorId " 
	    	+ " AND organizationId =:orgId "
	        //+ " AND unitId =:unitId "
	    	),
	   
	   @NamedQuery(name="UNIVERSAL_SEARCH_FOR_DOCTOR_DETAILS",query="SELECT doctor.doctorId as doctorId, "
				+ "doctor.accessCardNumber as accessCardNumber, "
				+ "doctor.prefixId as prefixId, "
				+ "coalesce(doctor.firstName,'') as firstName, "
				+ "coalesce(doctor.middleName,'') as middleName, "
				+ "coalesce(doctor.lastName,'') as lastName, "
				+ "doctor.genderId as genderId, "
				+ "to_char(doctor.dob,'MM/DD/YYYY') as dob, "
				+ "to_char(doctor.dob,'DD/MM/YYYY') as birthDate, "
				+ "doctor.pwdExpiry as pwdExpiry, "
				+ "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, doctor.dob)),'999')) as ageString, "
				+ "doctor.mobileNumber as mobileNumber, "
				+ "doctor.countryCallingCode as countryCallingCode, "
				+ "doctor.barCode as barCode, "
				+ "doctor.registrationNumber as registrationNumber, "
				+ "doctor.isVip as isVip, "
				+ "doctor.vipRemark as vipRemark, "
				+ "doctor.identificationTypeId as identificationTypeId, "
				+ "identificationMaster.identificationName as identificationName, "
				+ "doctor.identificationNumber as identificationNumber, "
				+ "doctor.email as email,"
				+ "doctor.docCategoryId as docCategoryId, "
				+ "doctorCategoryMaster.doctorCategoryDescription as docCategoryDescription, "
				+ "doctor.aliseName as aliseName, "
				+ "to_char(doctor.identificationExpiryDate,'MM/DD/YYYY') as identificationExpiryDate, "
				+ "doctor.occupationId as occupationId, "
				+ "doctor.specialityId as specialityId, "
				+ "specialityMaster.specialityName as specialityName, "
				+ "doctor.subSpecialityId as subSpecialityId, "
				+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityName, "
				+ "doctor.docDesignationId as docDesignationId, "
				+ "employeeDesignationMaster.employeeDesignationDescription as docDesignationDescription, "
				+ "doctor.docCode as docCode, "
				+ "to_char(doctor.dateOfJoining,'MM/DD/YYYY') as dateOfJoining, "
				+ "to_char(doctor.createdDate,'DD-MM-YYYY HH:MM:SS') as createdDate, "
				+ "doctor.createdBy as createdBy, "
				+ "doctor.status as status, "
				+ "doctor.allowedOverBookingSlots as allowedOverBookingSlots, "
				+ "doctor.userId as userId, "
				+ "doctor.unitId as unitId, "
				+ "doctor.organizationId as organizationId, "
				+ "doctor.doctorImage as doctorImage, "
				+ " doctor.processShareAgainstUnsettledBill as processShareAgainstUnsettledBill, "
				+ "doctor.registrationNumber as registrationNumber, "
				+ "doctor.slotTimeSpan as slotTimeSpan,doctor.isRMO as isRMO,doctor.doctorPin as doctorPin, "
				+ "doctorDetails.doctorDetailsId as doctorDetailsId, "
				+ "doctorDetails.nationalityId as nationalityId, "
				+ "doctorDetails.raceId as raceId, "
				+ "doctorDetails.maritalStatusId as maritalStatusId, "
				+ "doctorDetails.address as address,"
				+ "doctorDetails.countryId as countryId, "
				+ "doctorDetails.stateId as stateId, "
				+ "doctorDetails.districtId as districtId, "
				+ "doctorDetails.cityId as cityId, "
				+ "doctorDetails.areaId as areaId, "
				+ "doctorDetails.zipCode as zipCode, "
				+ "doctorDetails.phoneCode as phoneCode, "
				+ "doctorDetails.phoneNumber as phoneNumber, "
				+ "doctorDetails.permanentAddress as permanentAddress, "
				+ "doctorDetails.permanentCountryId as permanentCountryId, "
				+ "doctorDetails.permanentStateId as permanentStateId, "
				+ "doctorDetails.permanentDistrictId as permanentDistrictId, "
				+ "doctorDetails.permanentCityId as permanentCityId, "
				+ "doctorDetails.permanentAreaId as permanentAreaId, "
				+ "doctorDetails.permanentZipCode as permanentZipCode, "
				+ "doctorDetails.permanentPhoneCode as permanentPhoneCode, "
				+ "doctorDetails.permanentPhoneNumber as permanentPhoneNumber, "
				+ "doctorDetails.bankName as bankName, "
				+ "doctorDetails.accountNumber as accountNumber, "
				+ "doctorDetails.bankBranchName as bankBranchName, "
				+ "identificationMaster.isExpiryRequired as isExpiryRequired,"
				+ "doctorDetails.ifscCode as ifscCode, "
				+ "doctor.creditDays as creditDays "
				+ "FROM DoctorMaster doctor "
				+ "INNER JOIN doctor.doctorDetailslist doctorDetails "
				+ "LEFT JOIN doctor.specialityMaster specialityMaster "
				+ "LEFT JOIN doctor.subSpecialityMaster subSpecialityMaster "
				+ "LEFT JOIN doctor.doctorCategoryMaster doctorCategoryMaster "
				+ "LEFT JOIN doctor.employeeDesignationMaster employeeDesignationMaster "
				+ "LEFT JOIN doctor.identificationMaster identificationMaster "
				+ "WHERE doctor.organizationId=:organizationId "
				//+ "AND doctor.unitId=:unitId "
				+ "AND (lower(doctor.docCode) like :searchKeyword OR lower(doctor.firstName) like :searchKeyword "
				+ " OR lower(doctor.middleName) like :searchKeyword OR lower(doctor.lastName) like :searchKeyword "
				+ " OR lower(specialityMaster.specialityName) like :searchKeyword OR lower(subSpecialityMaster.subSpecialityMasterName) like :searchKeyword "
				+ " OR lower(doctorCategoryMaster.doctorCategoryDescription) like :searchKeyword OR lower(employeeDesignationMaster.employeeDesignationDescription ) like :searchKeyword )"),  
		
		
})
@NamedNativeQueries({
		@NamedNativeQuery(
			name = "GET_DOCTORDETAILS_FOR_SYNC_BY_ID",
					query = " select  docdt.country_id as \"countryId\","
							 + " 	doc.doctor_id as \"doctorLocalId\", " 
							 + " 	doc.registration_number as \"registrationNumber\", " 
							 + " 	doc.organization_id as \"hospitalId\", " 
							 + " 	userUnitMapp.unit_id as \"unit\", " 
							 + " 	userUnitMapp.unit_id as \"unitId\", " 
							 + " 	doc.doctor_number as \"doctorNumber\", " 
							 + " 	concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", " 
							 + " 	to_char(doc.dob, 'DD-MM-YYYY HH24:MI:SS.SS') as \"dateOfBirth\", " 
							 + " 	gen.gender_name as \"gender\", " 
							 + " 	to_char(doc.date_of_joining, 'DD-MM-YYYY HH24:MI:SS.SS') as \"dateOfJoining\", " 
							 + " 	doc.email as \"emailId\", " 
							 + " 	doc.mobile_number as \"mobileNumber\", " 
							 + " 	docdt.zip_code as \"pincode\", " 
							 + " 	ct.city_name as \"city\", " 
							 + " 	ar.area_name as \"area\", " 
							 + " 	doc.status as \"status\", " 
							 + " 	cast(doc.slot_time_span as double precision) as \"doctorEpisodeTime\", " 
							 + " 	doc.doctor_image as \"profilePicture\", " 
							 + " 	spc.speciality_name as \"classification\", " 
							 + " 	spc.speciality_name as \"department\", " 
							 + " 	st.state_name as \"state\", " 
							 + " 	spc.speciality_id as \"departmentId\", " 
							 + " 	2 as \"applicationId\", " 
							 + " 	un.unit_name as \"unitName\", " 
							 + " 	opc.followup_visit_days as \"followUpDuration\", " 
							 + " 	un.global_unit_id as \"globalUnitId\", " 
							 + " 	org.global_org_id as \"globalOrgId\", "
							 + "	doc.global_doc_id as \"globalDocId\", "
							 + "    (select array_to_string(array(select qualification_desc from doctor.t_doctor_doc_mapper where doctor_id =:id), ', ')) as \"education\" " 
							 + " from " 
							 + " 	doctor.m_doctor_master doc " 
							 + " left join public.m_gender_master gen on " 
							 + " 	gen.gender_id = doc.gender_id " 
							 + " inner join doctor.m_doctor_details docdt on " 
							 + " 	docdt.doctor_id = doc.doctor_id " 
							 + " left join public.m_city_master ct on " 
							 + " 	ct.city_id = docdt.city_id " 
							 + " left join public.m_area_master ar on " 
							 + " 	ar.area_id = docdt.area_id " 
							 + " left join public.m_state_master st on " 
							 + " 	st.state_id = docdt.state_id " 
							 + " inner join doctor.t_doctor_speciality_mapper docSpecMapp on " 
							 + " 	docSpecMapp.doctor_id = doc.doctor_id " 
							 + " 	and docSpecMapp.status = 'A' " 
							 /*+ " left join doctor.t_doctor_sub_speciality_mapper docSubSpeMapp on " 
							 + " 	docSubSpeMapp.doctor_id = doc.doctor_id " 
							 + " 	and docSubSpeMapp.status = 'A' " */
							 + " inner join doctor.m_speciality_master spc on " 
							 + " 	spc.speciality_id = docSpecMapp.speciality_id " 
							 + " inner join public.t_user_unit_mapper userUnitMapp on " 
							 + " 	userUnitMapp.user_id = doc.user_id " 
							 + " 	and userUnitMapp.status = 'A' " 
							 + " inner join public.m_unit_master un on " 
							 + " 	un.unit_id = userUnitMapp.unit_id " 
							 + " inner join public.m_organization_master org on " 
							 + " 	org.organization_id = doc.organization_id 	 " 
							 + " left join opd.m_op_consultation_configuration_master opc on " 
							 + " 	opc.speciality_id = docSpecMapp.speciality_id "
							 + " 	and opc.doctor_id = doc.doctor_id "
							 + " 	and opc.status = 'A' " 
							 + " 	and opc.unit_id = un.unit_id " 
							 + " where " 
							 + " 	doc.doctor_id =:id "
							 + "	and org.global_org_id is not null and un.global_unit_id is not null " ),
		
		@NamedNativeQuery(name="GET_DOCTOR_LIST_BY_SPECIALITY",query="select " 
			+ " 	doctorspec0_.doctor_speciality_id as \"doctorSpecialityId\", " 
			+ " 	coalesce(doctormast1_.first_name, '') as \"firstName\", " 
			+ " 	('Dr. ' || coalesce(doctormast1_.first_name, '')|| ' ' || coalesce(doctormast1_.middle_name, '')|| ' ' || coalesce(doctormast1_.last_name, '')) as \"doctorName\", " 
			+ " 	doctormast1_.last_name as \"lastName\", " 
			+ " 	doctorspec0_.speciality_id as \"specialityId\", " 
			+ " 	speciality2_.speciality_name as \"specialityName\", " 
			+ " 	doctormast1_.doctor_id as \"doctorId\", " 
			+ " 	doctormast1_.user_id as \"userId\" " 
			+ " from " 
			+ " 	doctor.t_doctor_speciality_mapper doctorspec0_ " 
			+ " inner join doctor.m_doctor_master doctormast1_ on " 
			+ " 	doctorspec0_.doctor_id = doctormast1_.doctor_id " 
			+ " inner join t_user_unit_mapper uum on " 
			+ " 	uum.user_id = doctormast1_.user_id	 " 
			+ " inner join doctor.m_speciality_master speciality2_ on " 
			+ " 	doctorspec0_.speciality_id = speciality2_.speciality_id " 
			+ " where " 
			+ " 	doctorspec0_.speciality_id =:specilaityId " 
			+ " 	and doctormast1_.status = 'A' " 
			+ " 	and doctorspec0_.status = 'A' " 
			+ " 	and doctormast1_.organization_id =:organizationId " 
			+ " 	and uum.unit_id =:unitId "
			+ "		AND uum.status='A' " 
			+ " order by " 
			+ " 	doctormast1_.first_name " ),
		
		@NamedNativeQuery(name="GET_DOCTORS_LIST_BY_SPECIALITIES",query="select " 
				+ " 	doctorspec0_.doctor_speciality_id as \"doctorSpecialityId\", " 
				+ " 	coalesce(doctormast1_.first_name, '') as \"firstName\", " 
				+ " 	concat('Dr. ',coalesce(doctormast1_.first_name, ''),' ',coalesce(doctormast1_.last_name, '')) as \"doctorName\", " 
				+ " 	doctormast1_.last_name as \"lastName\", " 
				+ " 	doctorspec0_.speciality_id as \"specialityId\", " 
				+ " 	speciality2_.speciality_name as \"specialityName\", " 
				+ " 	doctormast1_.doctor_id as \"doctorId\", " 
				+ " 	doctormast1_.user_id as \"userId\", "
				+ "		doctormast1_.doctor_image as \"doctorImage\", "
				+ "		doctormast1_.allowed_over_booking_slots as \"allowedOverBookingSlots\" " 
				+ " from " 
				+ " 	doctor.t_doctor_speciality_mapper doctorspec0_ " 
				+ " inner join doctor.m_doctor_master doctormast1_ on " 
				+ " 	doctorspec0_.doctor_id = doctormast1_.doctor_id " 
				+ " inner join t_user_unit_mapper uum on " 
				+ " 	uum.user_id = doctormast1_.user_id	 " 
				+ " inner join doctor.m_speciality_master speciality2_ on " 
				+ " 	doctorspec0_.speciality_id = speciality2_.speciality_id " 
				+ " where " 
				+ " 	doctorspec0_.speciality_id IN (:specilaityArray) " 
				+ " 	and doctormast1_.status = 'A' " 
				+ " 	and doctorspec0_.status = 'A' " 
				+ " 	and doctormast1_.organization_id =:organizationId " 
				+ " 	and uum.unit_id =:unitId "
				+ "		AND uum.status='A' " 
				+ " order by " 
				+ " 	doctormast1_.first_name " ),
		
		@NamedNativeQuery(name="GET_ACTIVE_DOCTOR_LIST",query= " select " 
				 + " 	doc.doctor_id as \"doctorId\", " 
				 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
				 + " 	coalesce(doc.first_name) as \"firstName\", " 
				 + " 	coalesce(doc.middle_name) as \"middleName\", " 
				 + " 	coalesce(doc.last_name) as \"lastName\" " 
				 + " from " 
				 + " 	doctor.m_doctor_master doc " 
				 + " inner join t_user_unit_mapper uum on " 
				 + " 	uum.user_id = doc.user_id " 
				 + " where " 
				 + " 	doc.organization_id =:orgId " 
				 + " 	and uum.unit_id =:unitId " 
				 + " 	and doc.status = 'A' " 
				 + " 	and uum.status = 'A' " 
				 + " order by " 
				 + " 	doc.first_name "   ),
		
		@NamedNativeQuery(name="GET_ACTIVE_DOCTOR_LIST_WITH_SPECIALTY",query= " select " 
				 + " 	doc.doctor_id as \"doctorId\", " 
				 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
				 + " 	coalesce(doc.first_name) as \"firstName\", " 
				 + " 	coalesce(doc.middle_name) as \"middleName\", " 
				 + " 	mpp.speciality_id as \"specialityId\", " 
				 + " 	coalesce(doc.last_name) as \"lastName\" " 
				 + " from " 
				 + " 	doctor.m_doctor_master doc " 
				 + " inner join t_user_unit_mapper uum on " 
				 + " 	uum.user_id = doc.user_id " 
				 + " inner join doctor.t_doctor_speciality_mapper mpp " 
				 + " 	on mpp.doctor_id = doc.doctor_id  "
 
				 + " where " 
				 + " 	doc.organization_id =:orgId " 
				 + " 	and uum.unit_id =:unitId " 
				 + " 	and doc.status = 'A' " 
				 + " 	and uum.status = 'A' " 
				 + " 	and  mpp.status = 'A' " 
				 + " order by " 
				 + " 	doc.first_name "   ),
		
		
		
		@NamedNativeQuery(name="GET_PLAIN_ACTIVE_DOCTOR_LIST",query="select " 
				 + " 	doc.doctor_id as \"doctorId\", " 
				 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
				 + " 	coalesce(doc.first_name) as \"firstName\", " 
				 + " 	coalesce(doc.middle_name) as \"middleName\","
				 + " doc.process_share_against_unsettled_bill as \"processShareAgainstUnsettledBill\", " 
				 + " 	coalesce(doc.last_name) as \"lastName\", "
				 + " 	doc.credit_days as \"creditDays\" "
				 /* + " 	doctSpemppr.speciality_id as \"specialityId\", "
				 + "	spl.speciality_name as \"specialityName\" " */
				 + " from " 
				 + " 	doctor.m_doctor_master doc " 
				 + " inner join t_user_unit_mapper uum on " 
				 + " 	uum.user_id = doc.user_id " 
				 + " where " 
				 + " 	doc.organization_id =:orgId " 
				 + " 	and uum.unit_id =:unitId " 
				 + " 	and doc.status = 'A' " 
				 + "	AND uum.status='A' " 
				 + " order by " 
				 + " 	doc.first_name " ),
		
		@NamedNativeQuery(name="AUTO_FILL_SEARCH_FOR_DOCTOR_NAME",query="select " 
				 + " 	distinct(concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) as \"doctorName\", " 
				 + " 	doc.doctor_id as \"doctorId\",   "
				 + " 0 as \"doctorType\"  "
//				 + " 	coalesce(doc.first_name) as \"firstName\", " 
//				 + " 	coalesce(doc.middle_name) as \"middleName\", " 
//				 + " 	coalesce(doc.last_name) as \"lastName\" " 
//HIS-14348		 + " 	doctSpemppr.speciality_id as \"specialityId\", "
//				 + "	spl.speciality_name as \"specialityName\" " 
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
//				 + " 	and uum.unit_id =:unitId " 
				 + " 	and doc.status = 'A' " 
				 + "	and (LOWER(doc.first_name) LIKE :doctorName "
				 + "	or LOWER(doc.middle_name) LIKE :doctorName "
				 + "	or LOWER(doc.last_name) LIKE :doctorName) "
				 + " 	and doctSpemppr.status = 'A' "
				 + "	AND uum.status='A' " 
				 + " union 	"  
				 + " select distinct(concat(coalesce(doc.doctor_name))) as \"doctorName\", "  
				 + "	doc.id as \"doctorId\", "  
				 + " 1 as \"doctorType\" "
				 + " from "  
				 + "	pharmacy.t_temp_doctor doc "  
				 + " where " 
				 + "	doc.organization_id =:orgId "  
				 + "	and (LOWER(doc.doctor_name) like :doctorName "  
				 + "		) "
				  ),
		
		@NamedNativeQuery(name="GET_ACTIVE_RMO_DOCTOR_LIST",query="SELECT doc.doctor_id as \"doctorId\", "
					+ " concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\" "
					+ " FROM doctor.m_doctor_master doc "
					 + " inner join t_user_unit_mapper uum on " 
					 + " 	uum.user_id = doc.user_id "
					+ "WHERE doc.organization_id =:orgId "
					+ "AND uum.unit_id=:unitId "
					+ "AND doc.status='A' "
					 +"and uum.status = 'A' "
					+ "AND doc.is_rmo='Y'  "
					+ "AND uum.status='A' "
					+ " ORDER BY \"doctorName\" ASC "),
		
		@NamedNativeQuery(name="GET_ACTIVE_DOCTOR_LIST_FOR_SWAP_DOCTOR",
				query="select distinct(mdm.doctor_id)  as \"doctorId\", "
						+ " concat('Dr. ',mdm.first_name,' ',mdm.last_name ,'|',spl.speciality_name) as \"doctorName\" , "
						+ " spl.speciality_id as \"specialityId\", "
						+ " spl.speciality_name as \"specialityName\" " 
						+ " from doctor.m_doctor_master mdm " 
						+ " inner join doctor.t_doctor_speciality_mapper dosspl on dosspl.doctor_id=mdm.doctor_id "
						+ " inner join doctor.m_speciality_master spl on spl.speciality_id=dosspl.speciality_id and spl.status='A' "
						+ " where " 
						+ " mdm.doctor_id in "
						+ " (select doctor_id from opd.t_encounter_master where doctor_id is not null " 
						+ " and status_id in (5,6) "
						+ " and unit_id = :unitId) "
						+ " and dosspl.speciality_id in "
						+ " (select dept_id from opd.t_encounter_master where doctor_id is not null " +
						"  	and status_id in (5,6) "
						+ " and unit_id = :unitId) "
						  //+ "and mdm.organization_id =:orgId"),
						),
		
		@NamedNativeQuery(name="GET_DOCTOR_LIST_FOR_OPD_BILLING",query= " select " 
				 + " 	doc.doctor_id as \"doctorId\", " 
				 + " 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
				 + " 	coalesce(doc.first_name) as \"firstName\", " 
				 + " 	coalesce(doc.middle_name) as \"middleName\", " 
				 + " 	coalesce(doc.last_name) as \"lastName\" " 
				 + " from " 
				 + " 	doctor.m_doctor_master doc " 
				 + " inner join t_user_unit_mapper uum on " 
				 + " 	uum.user_id = doc.user_id " 
				 + " where " 
				 + " 	doc.organization_id =:orgId " 
				 + " 	and uum.unit_id =:unitId " 
				 + " 	and doc.status = 'A' "
				 + "    and doc.is_rmo  = 'N'" 
				 + " 	and uum.status = 'A' " 
				 + " order by " 
				 + " 	doc.first_name "   ),
					  
})
		

@Entity
@Table(name="m_doctor_master",schema="doctor")
@SequenceGenerator(name="doctor_master_seq", sequenceName="doctor.doctor_master_seq", allocationSize=1)
public class DoctorMaster 
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="doctor_master_seq")
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="doctor_number")
	private Integer doctorNumber;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="middle_name")
	private String middleName;
	
	@Column(name="last_name")
	private String  lastName;
	
	@Column(name="prefix_id")
	private Integer prefixId;
	
	@Column(name="dob")
	private Date dob;
	
	@Column(name="gender_id")
	private Integer genderId;

	@Column(name="mobile_number")
	private String mobileNumber;
	
	@Column(name="country_calling_code")
	private String countryCallingCode;
			
	@Column(name="bar_code")
	private String barCode;
	
	@Column(name="is_vip")
	private Character isVip;
	
	@Column(name="vip_remark")
	private String vipRemark;
	
	@Column(name="identification_type_id")
	private Integer identificationTypeId;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="email")
	private String email;
	
	@Column(name="doc_category_id")
	private Integer docCategoryId;
	
	@Column(name="alise_name")
	private String aliseName;
	
	@Column(name="identification_expiry_date")
	private Date identificationExpiryDate;

	@Column(name="occupation_id")
	private Integer occupationId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	 
	@Column(name="sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name="doc_designation_id")
	private Integer docDesignationId;
	
	@Column(name="doc_code")
	private String docCode;
	
	@Column(name="date_of_joining")
	private Date dateOfJoining;
	
	@Column(name="access_card_number")
	private String accessCardNumber;
	
	@Column(name="allowed_over_booking_slots")
	private Integer allowedOverBookingSlots;
	
	@Column(name="status")
	private Character status; 

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="department_id")
	private Integer departmentId;
	
	@Column(name="doctor_image")
	private String doctorImage;
	
	@Column(name = "registration_number")
	private String registrationNumber;
	
	@Column(name = "slot_time_span")
	private Integer slotTimeSpan;
	
	@Column(name ="password_expiry")
	private String pwdExpiry;
	
	@Column(name ="is_rmo")
	private Character isRMO;
	
	@Column(name ="process_share_against_unsettled_bill")
	private Character processShareAgainstUnsettledBill;
	
	@Column(name ="doctor_pin")
	private String doctorPin;
	
	@Column(name ="qualification_desc")
	private String  qualificationDesc;
	
	@Column(name ="qualification_id")
	private Integer  qualificationId;
	
	@Column(name ="degree_id")
	private Integer  degreeId;
	
	@Column(name ="global_doc_id")
	private Integer  globalDocId;
	
	@Column(name ="general_ledger_id")
	private Integer  generalLedgerId;

	@Column(name="aiDoctor",updatable = false)
	private Character ai_doctor='N';

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "doctorMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<DoctorSpecialityMapper> docotrSpecialityMapperslist;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "doctorMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<DoctorDetails> doctorDetailslist;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefix_id", insertable = false, updatable = false)
	private PrefixMaster prefixMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_speciality_id", insertable = false, updatable = false)
	private SubSpecialityMaster subSpecialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_category_id", insertable = false, updatable = false)
	private DoctorCategoryMaster doctorCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_designation_id", insertable = false, updatable = false)
	private EmployeeDesignationMaster employeeDesignationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identification_type_id", insertable = false, updatable = false)
	private IdentificationMaster identificationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "occupation_id", insertable = false, updatable = false)
	private OccupationMaster occupationMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "doctorMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<DoctorIPConsultationServiceMapper> DoctorIPConsultationServiceMapperList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "doctorMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<DoctorCabinMapper> doctorCabinMapperlist;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "doctorDocumentMapper", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorDocumentMapper> doctorMasterlist;

	@Column(name ="credit_days")
	private Integer  creditDays;
	
	public Integer getGlobalDocId() {
		return globalDocId;
	}

	public void setGlobalDocId(Integer globalDocId) {
		this.globalDocId = globalDocId;
	}

	public Integer getAllowedOverBookingSlots() {
		return allowedOverBookingSlots;
	}

	public void setAllowedOverBookingSlots(Integer allowedOverBookingSlots) {
		this.allowedOverBookingSlots = allowedOverBookingSlots;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDoctorNumber() {
		return doctorNumber;
	}

	public void setDoctorNumber(Integer doctorNumber) {
		this.doctorNumber = doctorNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public String getVipRemark() {
		return vipRemark;
	}

	public void setVipRemark(String vipRemark) {
		this.vipRemark = vipRemark;
	}

	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getDocCategoryId() {
		return docCategoryId;
	}

	public void setDocCategoryId(Integer docCategoryId) {
		this.docCategoryId = docCategoryId;
	}

	public String getAliseName() {
		return aliseName;
	}

	public void setAliseName(String aliseName) {
		this.aliseName = aliseName;
	}

	public Date getIdentificationExpiryDate() {
		return identificationExpiryDate;
	}

	public void setIdentificationExpiryDate(Date identificationExpiryDate) {
		this.identificationExpiryDate = identificationExpiryDate;
	}

	public Integer getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Integer occupationId) {
		this.occupationId = occupationId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getDocDesignationId() {
		return docDesignationId;
	}

	public void setDocDesignationId(Integer docDesignationId) {
		this.docDesignationId = docDesignationId;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getAccessCardNumber() {
		return accessCardNumber;
	}

	public void setAccessCardNumber(String accessCardNumber) {
		this.accessCardNumber = accessCardNumber;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<DoctorSpecialityMapper> getDocotrSpecialityMapperslist() {
		return docotrSpecialityMapperslist;
	}

	public void setDocotrSpecialityMapperslist(List<DoctorSpecialityMapper> docotrSpecialityMapperslist) {
		this.docotrSpecialityMapperslist = docotrSpecialityMapperslist;
	}

	public List<DoctorDetails> getDoctorDetailslist() {
		return doctorDetailslist;
	}

	public void setDoctorDetailslist(List<DoctorDetails> doctorDetailslist) {
		this.doctorDetailslist = doctorDetailslist;
	}

	public PrefixMaster getPrefixMaster() {
		return prefixMaster;
	}

	public void setPrefixMaster(PrefixMaster prefixMaster) {
		this.prefixMaster = prefixMaster;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public SubSpecialityMaster getSubSpecialityMaster() {
		return subSpecialityMaster;
	}

	public void setSubSpecialityMaster(SubSpecialityMaster subSpecialityMaster) {
		this.subSpecialityMaster = subSpecialityMaster;
	}

	public EmployeeDesignationMaster getEmployeeDesignationMaster() {
		return employeeDesignationMaster;
	}

	public void setEmployeeDesignationMaster(EmployeeDesignationMaster employeeDesignationMaster) {
		this.employeeDesignationMaster = employeeDesignationMaster;
	}

	public IdentificationMaster getIdentificationMaster() {
		return identificationMaster;
	}

	public void setIdentificationMaster(IdentificationMaster identificationMaster) {
		this.identificationMaster = identificationMaster;
	}

	public OccupationMaster getOccupationMaster() {
		return occupationMaster;
	}

	public void setOccupationMaster(OccupationMaster occupationMaster) {
		this.occupationMaster = occupationMaster;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDoctorImage() {
		return doctorImage;
	}

	public void setDoctorImage(String doctorImage) {
		this.doctorImage = doctorImage;
	}

	public DoctorCategoryMaster getDoctorCategoryMaster() {
		return doctorCategoryMaster;
	}

	public void setDoctorCategoryMaster(DoctorCategoryMaster doctorCategoryMaster) {
		this.doctorCategoryMaster = doctorCategoryMaster;
	}

	public List<DoctorIPConsultationServiceMapper> getDoctorIPConsultationServiceMapperList() {
		return DoctorIPConsultationServiceMapperList;
	}

	public void setDoctorIPConsultationServiceMapperList(
			List<DoctorIPConsultationServiceMapper> doctorIPConsultationServiceMapperList) {
		DoctorIPConsultationServiceMapperList = doctorIPConsultationServiceMapperList;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Integer getSlotTimeSpan() {
		return slotTimeSpan;
	}

	public void setSlotTimeSpan(Integer slotTimeSpan) {
		this.slotTimeSpan = slotTimeSpan;
	}

	public String getPwdExpiry() {
		return pwdExpiry;
	}

	public void setPwdExpiry(String pwdExpiry) {
		this.pwdExpiry = pwdExpiry;
	}

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public List<DoctorCabinMapper> getDoctorCabinMapperlist() {
		return doctorCabinMapperlist;
	}

	public void setDoctorCabinMapperlist(List<DoctorCabinMapper> doctorCabinMapperlist) {
		this.doctorCabinMapperlist = doctorCabinMapperlist;
	}

	public List<DoctorDocumentMapper> getDoctorMasterlist() {
		return doctorMasterlist;
	}

	public void setDoctorMasterlist(List<DoctorDocumentMapper> doctorMasterlist) {
		this.doctorMasterlist = doctorMasterlist;
	}


	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	public String getDoctorPin() {
		return doctorPin;
	}

	public void setDoctorPin(String doctorPin) {
		this.doctorPin = doctorPin;
	}

	public String getQualificationDesc() {
		return qualificationDesc;
	}

	public void setQualificationDesc(String qualificationDesc) {
		this.qualificationDesc = qualificationDesc;
	}

	public Integer getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(Integer qualificationId) {
		this.qualificationId = qualificationId;
	}

	public Integer getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Integer degreeId) {
		this.degreeId = degreeId;
	}

	public Character getProcessShareAgainstUnsettledBill() {
		return processShareAgainstUnsettledBill;
	}

	public void setProcessShareAgainstUnsettledBill(Character processShareAgainstUnsettledBill) {
		this.processShareAgainstUnsettledBill = processShareAgainstUnsettledBill;
	}

	public Integer getCreditDays() {
		return creditDays;
	}

	public void setCreditDays(Integer creditDays) {
		this.creditDays = creditDays;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public Character getAi_doctor() {
		return ai_doctor;
	}

	public void setAi_doctor(Character ai_doctor) {
		if(ai_doctor==null) ai_doctor='N';
		this.ai_doctor = ai_doctor;
	}
}
