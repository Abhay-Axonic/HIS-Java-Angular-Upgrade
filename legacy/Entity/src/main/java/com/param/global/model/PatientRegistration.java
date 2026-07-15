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

import com.param.adt.admission.model.Admission;
import com.param.billing.global.model.DepositMaster;
import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.coversheet.model.NursingOrderMapper;

@NamedQueries({ @NamedQuery(name = "GET_PATIENT_DETAILS_BY_ID", query = "SELECT patient.patientId as patientId,patient.email as email,patient.videoCallUrl as videoCallUrl, "
		+ "patient.uhidNumber as UHIDNumber, " + "patient.prefixId as prefixId, "
		+ "concat(coalesce(prefix.prefixCode,''),' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
		+ "concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as fFirstName, "
		+ "patient.genderId as genderId, " + "patient.mobileNumber as mobileNumber, to_char(patient.dob, 'YYYYMMDDhhmmss') as dobPacs,"
		+ "to_char(patient.dob, 'DD-MM-YYYY') as dob," + " patient.barCode as barCode," + " patient.isVip as isVip,"
		+ " patient.vipRemark as vipRemark," + "patient.isBlacklist as isBlacklist, "
		+ " patient.patientCategoryId as patientCategoryId," + " patient.registrationTypeId as registrationTypeId,"
		+ " patient.isUnknownReg as isUnknownReg,"
		+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as age, "
		+ " genderMaster.code as genderCode,genderMaster.desc as genderName ,"
		+ "coalesce(identification.identificationName,'') as identificationName, "
		+ "patient.identificationNumber as identificationNumber," 
		+ " cntyMstr.countryName as countryName,stMstr.stateName as stateName,ctyMstr.cityName as cityName ,areaMstr.areaName as areaName,disMstr.districtName as districtName,"
		+ " nationMstr.nationalityName as nationalityName ,raceMstr.raceName as raceName,mstatus.desc as maritalStatus,patientDetails.address as address,cntyMstr.countryCode as countryCode ,"
		+ " patient.unitId as unitId,patient.organizationId as organizationId, "
		+ " patient.whatsappNo as whatsappNo, "
		+ " patient.whatsappCallingCode as whatsappCallingCode, "
		+ " patient.skypeId as skypeId "
		+ " FROM PatientRegistration patient "
		+ " inner join patient.patientDetailslist patientDetails "
		+ " INNER JOIN patient.prefixMaster as prefix "
		+ " left join patientDetails.countryMaster  cntyMstr "
		+ " left join patientDetails.stateMaster stMstr "
		+ " left join patientDetails.cityMaster ctyMstr "
		+ " left join patientDetails.areaMaster areaMstr "
		+ " left join patientDetails.districtMaster disMstr "
		+ " left join patientDetails.nationalityMaster nationMstr"
		+ " left join patientDetails.raceMaster raceMstr "
		+ " left join patientDetails.maritalStatusMaster mstatus "
		+ " INNER JOIN patient.genderMaster genderMaster "
		+ " LEFT JOIN patient.identificationMaster identification " 
		+ " WHERE patient.patientId=:patientId"),

		@NamedQuery(name = "GET_PATIENT_DETAILS", query = "SELECT patient.patientId as patientId, "
				+ "patient.uhidNumber as uhidNumber, " + "patient.prefixId as prefixId, "
				+ "concat(coalesce(prefix.prefixCode,'') ,' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
				+ "patient.genderId as genderId, " + "patient.mobileNumber as mobileNumber, "
				+ "to_char(patient.dob, 'dd/MM/yyyy') as dob," + " genderMaster.code as genderCode "
				+ " FROM PatientRegistration patient " 
				+ " INNER JOIN patient.genderMaster genderMaster "
				+ " INNER JOIN patient.prefixMaster as prefix "
				+ " WHERE "
				//+ "patient.unitId=:unitId " 
				+ " patient.organizationId=:orgId"),

		@NamedQuery(name = "CHECK_UNIQUE_PATIENT", query = "SELECT pt.patientId as patientId,pt.firstName as firstName,"
				+ "pt.lastName as lastName,pt.mobileNumber as mobileNumber,to_char(pt.dob,'DD-MM-YYYY') as dob,"
				+ "pt.genderId as genderId,pt.prefixId as prefixId,pt.middleName as middleName, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pt.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pt.dob)),'M ') as age, "
                + " pt.patientCategoryId as patientCategoryId,pt.email as email,"
                + " pt.uhidNumber as  UHIDNumber "
				+ "FROM PatientRegistration pt " 
				+ "WHERE to_char(pt.dob,'DD-MM-YYYY')=:dob " 
				+ "AND pt.genderId=:genderId "
				+ "AND pt.mobileNumber=:mobileNumber " 
				+ "AND pt.organizationId=:orgId " 
				//+ "AND pt.unitId=:unitId "
				),

		@NamedQuery(name = "GET_PATIENT_DATA_FOR_SELF_REGISTRATION", query = "SELECT patient.patientId as patientId, "
				+ "patient.uhidNumber as uhidNumber, " 
				+ "patient.prefixId as prefixId, "
				+ "prefix.prefixCode as prefixCode, " 
				+ "coalesce(patient.firstName,'') as firstName, "
				+ "coalesce(patient.middleName,'') as middleName, " 
				+ "coalesce(patient.lastName,'') as lastName, patient.isAbsconded as isAbsconded,"
				+ "patient.genderId as genderId, " 
				+ "patient.imagePath as imagePath, "
				+ "patient.patientCategoryId as patientCategoryId," 
				+ "gender.code as genderCode, "
				+ "to_char(patient.dob,'MM/DD/YYYY') as dob, " 
				+ "to_char(patient.dob,'DD/MM/YYYY') as birthDate, "
				+ "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
				+ "patient.mobileNumber as mobileNumber, " 
				+ "patient.countryCallingCode as countryCallingCode, "
				+ "patient.registrationTypeId as typeId, " 
				+ "patient.email as email, "
				+ " patient.identificationTypeId as identificationTypeId, patient.identificationNumber as identificationNumber ,"
				+ " admi.admissionId  as admissionId, "
				+ " admi.visitTypeId  as visitTypeId "
				/*+ "(select admi.admissionId FROM Admission admission where admission.status='A' and admission.patientId=patient.patientId) as admissionId, "
				+ "(select admi.visitTypeId FROM Admission admission where admission.status='A' and admission.patientId=patient.patientId) as visitTypeId "*/
				+ "FROM PatientRegistration patient " 
				+ "INNER JOIN patient.prefixMaster prefix "
				+ "INNER JOIN patient.genderMaster gender "
				+ " LEFT JOIN patient.admissionlist admi with admi.status='A' "
				+ "WHERE patient.organizationId=:orgId "
				+ "AND patient.status='A' "
				+ "AND patient.isPrimary='Y' "
				+ "AND patient.registrationTypeId IN (1,3,6) "),

		@NamedQuery(name = "GET_PATIENT_DATA_FOR_SELF_PRE_BOOKED", query = "SELECT patient.patientId as patientId, "
				+ "patient.uhidNumber as uhidNumber, " 
				+ "patient.prefixId as prefixId, "
				+ "prefix.prefixCode as prefixCode, "
				+ "concat(coalesce(prefix.prefixCode,''),' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,''),' ',trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')),'Y / ',gender.code ) as patientName, "
				+ "patient.genderId as genderId, " 
				+ "gender.code as genderCode, "
				+ "patient.patientCategoryId as patientCategoryId,"
				+ "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
				+ "to_char(slot.appointmentDate,'DD/MM/YYYY') as appointmentDate, "
				+ "appointment.appointmentId as appointmentId, "
				+ "appointment.appointmentStatusId as appointmentStatusId, " 
				+ "slot.specialityId as specialityId, "
				+ "speciaityMaster.specialityName as specialityName, "
				+ "uspml.deptWiseAppointment as deptWiseAppointment, " 
				+ "slot.doctorId as doctorId, "
				+ "concat('Dr.',' ',coalesce(docMaster.firstName),' ',coalesce(docMaster.lastName)) as doctorName, "
				+ "patient.registrationTypeId as typeId, "
				+ "concat(to_char(slot.fromTime,'HH:MI AM')) as appointmentTime, "
				+ "concat(to_char(slot.fromTime,'HH24:MI')) as appointmentTime2, "
				+ "coalesce(appointment.isPaidAppointment,'N') as isPaidAppointment, "
				+ "encounter.encounterId as encounterId, "
				+ "  patient.identificationTypeId as identificationTypeId, patient.identificationNumber as identificationNumber , "
				+ "(select admission.admissionId FROM Admission admission where admission.status='A' and admission.patientId=patient.patientId) as admissionId "
				+ "FROM PatientRegistration patient "
				+ "INNER JOIN patient.patientDetailslist patientDetails " 
				+ "LEFT JOIN patient.prefixMaster prefix "
				+ "INNER JOIN patient.genderMaster gender " 
				+ "INNER JOIN patient.appointmentMastersList appointment "
				+ "LEFT JOIN appointment.encounterMasterList encounter "
				+ "INNER JOIN appointment.slotMaster slot " 
				+ "LEFT JOIN slot.specialityMaster speciaityMaster "
				+ "LEFT JOIN speciaityMaster.unitSpecialityMappersList uspml "
				+ "LEFT JOIN slot.doctorMaster docMaster " 
				+ "WHERE patient.organizationId=:orgId "
				+ "AND uspml.unitId =appointment.unitId " 
				+ "AND patient.status='A' " 
				+ "AND patientDetails.status='A' "
				+ "AND patient.isPrimary='Y' " 
				+ "AND appointment.status='A' "
				+ "AND appointment.appointmentStatusId=1 " 
				+ "AND appointment.unitId=:unitId " 
				+ "AND appointment.appointmentTypeId=1 "
				+ "AND appointment.tokenNumber is null "
				+ "AND slot.appointmentDate=:currentDate " 
				+ "AND patient.registrationTypeId NOT IN (2,4) "
				+ "AND NOT (appointment.isTeleconsultationAppointment = 1 and coalesce(appointment.isPaidAppointment,'N')='Y') "),

		@NamedQuery(name = "GET_PATIENT_DATA_FOR_SELF_PRE_BOOKED_APPOINTMENT_UNIQUE", query = "SELECT patient.patientId as patientId, "
				+ "patient.uhidNumber as uhidNumber, " 
				+ "patient.prefixId as prefixId, "
				+ "prefix.prefixCode as prefixCode, "
				+ "concat(coalesce(prefix.prefixCode,''),' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,''),' ',trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')),'Y / ',gender.code ) as patientName, "
				+ "patient.genderId as genderId, " 
				+ "gender.code as genderCode, "
				+ "patient.patientCategoryId as patientCategoryId,"
				+ "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
				+ "to_char(slot.appointmentDate,'DD/MM/YYYY') as appointmentDate, "
				+ "appointment.appointmentId as appointmentId, "
				+ "appointment.appointmentStatusId as appointmentStatusId, " 
				+ "slot.specialityId as specialityId, "
				+ "speciaityMaster.specialityName as specialityName, "
				+ "uspml.deptWiseAppointment as deptWiseAppointment, " 
				+ "slot.doctorId as doctorId, "
				+ "concat('Dr.',' ',coalesce(docMaster.firstName),' ',coalesce(docMaster.lastName)) as doctorName, "
				+ "patient.registrationTypeId as typeId, "
				+ "concat(to_char(slot.fromTime,'HH:MI AM')) as appointmentTime, "
				+ "concat(to_char(slot.fromTime,'HH24:MI')) as appointmentTime2, "
				+ "coalesce(appointment.isPaidAppointment,'N') as isPaidAppointment, "
				+ "encounter.encounterId as encounterId, "
				+ "  patient.identificationTypeId as identificationTypeId, patient.identificationNumber as identificationNumber , "
				+ "(select admission.admissionId FROM Admission admission where admission.status='A' and admission.patientId=patient.patientId) as admissionId "
				+ "FROM PatientRegistration patient "
				+ "INNER JOIN patient.patientDetailslist patientDetails " 
				+ "LEFT JOIN patient.prefixMaster prefix "
				+ "INNER JOIN patient.genderMaster gender " 
				+ "INNER JOIN patient.appointmentMastersList appointment "
				+ "LEFT JOIN appointment.encounterMasterList encounter "
				+ "INNER JOIN appointment.slotMaster slot " 
				+ "LEFT JOIN slot.specialityMaster speciaityMaster "
				+ "LEFT JOIN speciaityMaster.unitSpecialityMappersList uspml "
				+ "LEFT JOIN slot.doctorMaster docMaster " 
				+ "WHERE patient.organizationId=:orgId "
				+ "AND uspml.unitId =appointment.unitId " 
				+ "AND patient.status='A' " 
				+ "AND patientDetails.status='A' "
				+ "AND patient.isPrimary='Y' " 
				+ "AND appointment.status='A' "
				+ "AND appointment.appointmentStatusId=1 " 
				+ "AND appointment.unitId=:unitId " 
				+ "AND appointment.appointmentTypeId=1 "
				+ "AND appointment.tokenNumber is null "
				+ "AND slot.appointmentDate=:currentDate " 
				+ "AND patient.registrationTypeId NOT IN (2,4) "
				+ "AND NOT (appointment.isTeleconsultationAppointment = 1 and coalesce(appointment.isPaidAppointment,'N')='Y') "),

		
		


		@NamedQuery(name = "GET_PATIENT_DETAILS_FOR_HEALTHCARD", query = "SELECT patient.patientId as patientId, "
				+ "patient.uhidNumber as uhidNumber, " + "patient.prefixId as prefixId, "
				+ "concat(coalesce(prefix.prefixCode,'') ,' ', coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
				+ "patient.genderId as genderId, " + "patient.mobileNumber as mobileNumber, "
				+ "to_char(patient.dob, 'DD-MM-YYYY') as dob," + " patient.barCode as barCode,"
				+ " patient.isVip as isVip," + " patient.vipRemark as vipRemark,"
				+ "patient.isBlacklist as isBlacklist, " + " patient.patientCategoryId as patientCategoryId,"
				+ " patient.isUnknownReg as isUnknownReg," + " patient.imagePath as imagePath,"
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as ageString, "
				+ " genderMaster.code as genderCode, " + " patientDetails.countryId as countryId, "
				+ " patientDetails.stateId as stateId, " + " patientDetails.districtId as districtId, "
				+ " patientDetails.cityId as cityId, " + " patientDetails.areaId as areaId, "
				+ " concat(patientDetails.address,', ', area.areaName,', ',city.cityName,', ',dist.districtName,', ',state.stateName,', ',country.countryName,', ',patientDetails.zipCode,'.') as address "
				+ " FROM PatientRegistration patient " 
				+ " INNER JOIN patient.patientDetailslist patientDetails "
				+ " INNER JOIN patient.genderMaster genderMaster "
				+ " INNER JOIN patient.prefixMaster as prefix "
				+ " INNER JOIN patientDetails.countryMaster country "
				+ " INNER JOIN patientDetails.stateMaster state " 
				+ " INNER JOIN patientDetails.districtMaster dist "
				+ " INNER JOIN patientDetails.cityMaster city " 
				+ " INNER JOIN patientDetails.areaMaster area "
				+ " WHERE patient.patientId=:patientId "),

})
@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_PATIENT_DETAILS_BY_ID_FOR_BILLING_SEARCH", query = "select distinct pat.patient_id as \"patientId\", pat.uhid_number as \"uhidNumber\", pat.first_name as \"firstName\", pat.middle_name as \"middleName\", pat.last_name as \"lastName\",  "
				+ " pat.mobile_number as \"mobileNumber\", pat.email as \"email\", sm.doctor_id as \"doctorId\", pat.image_path as \"imagePath\", to_char(pat.dob,'DD-MM-YYYY') as \"dob\",pat.dob as \"dobNew\",  sm.speciality_id as \"specialityId\", speciaityMaster.speciality_name as \"specialityName\", "
				+ " trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pat.dob)),'999')) as \"ageString\",   concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\","
				+ " prfx.prefix_code as \"prefixCode\", gen.gender_name as \"genderName\",gen.gender_id as \"genderId\", patdtls.address as \"address\", cnt.country_name as \"countryName\",  "
				+ " st.state_name as \"stateName\", cty.city_name as \"cityName\", ar.area_name as \"areaName\", patdtls.zip_code as \"zipCode\",  pat.country_calling_code as \"countryCallingCode\", "
				+ " coalesce(mppr.patient_loyalty_card_mppr_id,0) as \"patientLoyaltyCardMpprId\" "
				+ " from patient.t_patient_registration pat  " + " left join public.m_prefix_master prfx "
				+ " on prfx.prefix_id = pat.prefix_id " + " left join public.m_gender_master gen  "
				+ " on gen.gender_id = pat.gender_id " + " left join patient.t_patient_details patdtls "
				+ " on patdtls.patient_id = pat.patient_id " + " left join public.m_country_master cnt  "
				+ " on cnt.country_id = patdtls.country_id " + " left join public.m_state_master st  "
				+ " on st.state_id = patdtls.state_id " + " left join public.m_city_master cty "
				+ " on cty.city_id = patdtls.city_id " + " left join public.m_area_master ar "
				+ " on ar.area_id = patdtls.area_id "
				+ " left join public.t_patient_loyalty_card_mppr mppr on mppr.patient_id = pat.patient_id and mppr.is_deleted = 'N' "
				+ " left join opd.m_appointment_master am on am.patient_id = pat.patient_id "
				+ " left join opd.m_slot_master sm on am.slot_id = sm.slot_id "
				+ " left outer join doctor.m_speciality_master speciaityMaster on sm.speciality_id = speciaityMaster.speciality_id " 
				+ " left outer join doctor.m_sub_speciality_master subSpeciaityMaster on sm.sub_speciality_id = subSpeciaityMaster.sub_speciality_id" 
				+ " left outer join doctor.m_doctor_master doc on sm.doctor_id = doc.doctor_id	"
				+ " where pat.patient_id = :patientId " 
			    + " and pat.is_primary='Y' "),

		@NamedNativeQuery(name = "UPDATE_UUID_BY_PATIENT", query = "update patient.t_patient_registration set uuid =:uuid where patient_id =:id "),

		@NamedNativeQuery(name = "GET_PATIENT_DETAILS_TO_SYNC_BY_ID", query = " select " 
				 + " 	pat.gender_id as \"genderId\",prf.prefix_desc as \"prefix\", "
				 + " 	pat.first_name as \"firstName\", " 
				 + " 	pat.middle_name as \"middleName\", " 
				 + " 	pat.last_name as \"lastName\", " 
				 + " 	gen.gender_name as \"gender\", " 
				 + " 	to_char(pat.dob, 'DD-MM-YYYY') as \"dateOfBirth\", " 
				 + " 	pat.image_path as \"patientPhoto\", " 
				 + " 	pat.mobile_number as \"mobileNumber\", " 
				 + " 	pat.email as \"emailId\", " 
				 + " 	patdt.address as \"addressDesc\", " 
                 + " 	patdt.family_id as \"familyId\", " 
				 + " 	cnt.country_name as \"country\", " 
				 + " 	st.state_name as \"state\", " 
				 + " 	ct.city_name as \"city\", " 
				 + " 	patdt.zip_code as \"pincode\", " 
				 + " 	to_char(pat.created_date, 'DD-MM-YYYY') as \"registrationDate\", " 
				 + " 	nat.nationality_name as \"nationality\", " 
				 + " 	ar.area_name as \"area\", " 
				 + " 	pat.status as \"status\", " 
				 + " 	pat.patient_id as \"patientId\", " 
				 + " 	cast(pat.patient_id as varchar) as \"localUhid\", " 
				 + " 	pat.organization_id as \"hospitalId\", " 
				 + " 	2 as \"applicationId\", " 
				 + " 	cnt.country_calling_code as \"code\", "
				 + " 	org.global_org_id as \"globalOrgId\", "
				 + " 	org.channel_id as \"channelId\", "
				 + "	1 as \"countryId\", "
				 + "	pat.uuid as \"uuid\", "
				 + "	pat.whatsapp_no as \"twitterId\", "
				 + "	pat.skype_id as \"facebookId\" "
				 + " from " 
				 + " 	patient.t_patient_registration pat " 
				 + " inner join patient.t_patient_details patdt on " 
				 + " 	patdt.patient_id = pat.patient_id " 
				 + " left join public.m_prefix_master prf on " 
				 + " 	prf.prefix_id = pat.prefix_id " 
				 + " left join public.m_gender_master gen on " 
				 + " 	gen.gender_id = pat.gender_id " 
				 + " left join public.m_country_master cnt on " 
				 + " 	cnt.country_id = patdt.country_id " 
				 + " left join public.m_state_master st on " 
				 + " 	st.state_id = patdt.state_id " 
				 + " left join public.m_city_master ct on " 
				 + " 	ct.city_id = patdt.city_id " 
				 + " left join public.m_nationality_master nat on " 
				 + " 	nat.nationality_id = patdt.nationality_id " 
				 + " left join public.m_area_master ar on " 
				 + " 	ar.area_id = patdt.area_id " 
				 + " inner join public.m_organization_master org on " 
				 + " 	org.organization_id = pat.organization_id	 " 
				 + " where " 
				 + " 	pat.patient_id = :id " 
				 + " 	and pat.registration_type_id = 1 "
				 + "	and org.global_org_id is not null " ),

		@NamedNativeQuery(name = "UPDATE_PATIENT_CATEGORY_BY_PATIENT_ID", query = "update patient.t_patient_registration set patient_category_id =:patientCategoryId where patient_id =:patientId "), 
		@NamedNativeQuery(name = "UPDATE_PATIENT_IMAGE_PATH", query = 
	       "update " + 
	       "		patient.t_patient_registration" + 
	       "		set " + 
	       "		image_path =:imagePath, " + 
	       "		image =:image " + 
	       "		where " + 
	       "		patient_id =:patientId " + 
	       "		and organization_id =:orgId " 
	       //"		and unit_id =:unitId "
	       ),
		
		
		@NamedNativeQuery(name = "GET_PATIENT_DETAILS_BY_ID_FOR_EMR",
		query = "select" + 
				"			pat.patient_id as \"patientId\"," + 
				"			pat.uhid_number as \"uhidNumber\"," + 
				"			pat.first_name as \"firstName\"," + 
				"			pat.middle_name as \"middleName\"," + 
				"			pat.last_name as \"lastName\"," + 
				"			pat.mobile_number as \"mobileNumber\"," + 
				"			pat.email as \"email\"," + 
				"			pat.image_path as \"imagePath\"," + 
				"			to_char(pat.dob, 'DD-MM-YYYY') as \"dob\"," + 
				"			trim(to_char(extract(year from age(current_timestamp, pat.dob)), '999')) as \"ageString\"," + 
				"			prfx.prefix_code as \"prefixCode\"," + 
				"			gen.gender_name as \"genderName\"," + 
				"			gen.gender_id as \"genderId\"," + 
				"			gen.gender_code as \"gendercode\"," + 
				"			patdtls.address as \"address\"," + 
				"			cnt.country_name as \"countryName\"," + 
				"			st.state_name as \"stateName\"," + 
				"			cty.city_name as \"cityName\"," + 
				"			ar.area_name as \"areaName\"," + 
				"			patdtls.zip_code as \"zipCode\"," + 
				"			pat_cate.patient_category as \"patientCategory\"," + 
				"			m_enc_mst.encounter_number as \"visitNo\"," + 
				"			to_char(m_enc_mst.encounter_date,'DD-MM-YYYY') as \"visitDate\"," + 
				"			 1 as \"visitTypeId\"," + 
				"			ent_mst.payment_entitlement_desc as \"paymentEntilementDesc\", " + 
				"			m_enc_mst.encounter_id as \"encounterId\", "+ 
				"			admn.admission_id as \"admissionId\" " + 
				"		from" + 
				"			patient.t_patient_registration pat" + 
				"		left join opd.t_encounter_master m_enc_mst on" + 
				"			pat.patient_id = m_enc_mst.patient_id " + 
				"		left join public.m_payment_entitlement_master ent_mst on" + 
				"			m_enc_mst.payment_entitlement_type_id = ent_mst.payment_entitlement_id " + 
				"		inner join public.m_patient_category_master pat_cate on" + 
				"			pat.patient_category_id = pat_cate.patient_category_id" + 
				"		left join public.m_prefix_master prfx on" + 
				"			prfx.prefix_id = pat.prefix_id" + 
				"		left join public.m_gender_master gen on" + 
				"			gen.gender_id = pat.gender_id" + 
				"		left join patient.t_patient_details patdtls on" + 
				"			patdtls.patient_id = pat.patient_id" + 
				"		left join public.m_country_master cnt on" + 
				"			cnt.country_id = patdtls.country_id" + 
				"		left join public.m_state_master st on" + 
				"			st.state_id = patdtls.state_id" + 
				"		left join public.m_city_master cty on" + 
				"			cty.city_id = patdtls.city_id" + 
				"		left join public.m_area_master ar on" + 
				"			ar.area_id = patdtls.area_id" + 
				"		 left join adt.t_admission admn on " +
				" 			admn.patient_id = pat.patient_id" + 
				"		where" + 
				"			pat.patient_id = :patientId" + 
				"			and pat.is_primary = 'Y'" + 
				"			order by m_enc_mst.encounter_id desc "),
		@NamedNativeQuery(name = "PATIENT_AUTO_FILL_SEARCH", 
		query = "	SELECT"
			+" 	pr.patient_id as \"patientId\","
			+" 	concat(coalesce(prfx.prefix_code,''),' ',coalesce( pr.first_name,''),' ',coalesce( pr.middle_name,'' ), ' ' ,coalesce( pr.last_name,'' )) as \"patientName\","
			+" 	pr.uhid_number as \"uhidNumber\","
			+" 	pr.mobile_number as \"mobileNumber\" "
			+" 	FROM patient.t_patient_registration pr	"
			+"  left join public.m_prefix_master prfx "
			+ " 	on prfx.prefix_id = pr.prefix_id " 
			+" 	LEFT JOIN opd.m_appointment_master mast on pr.patient_id=mast.patient_id and  mast.status='A'"
			+" 	LEFT JOIN adt.t_admission adm on pr.patient_id=adm.patient_id and   adm.status='A'"
			+" 	WHERE	(LOWER(pr.first_name) LIKE :patientName  "
			+" 	OR LOWER(pr.last_name) LIKE :patientName "
			+" 	OR LOWER(pr.uhid_number) LIKE :patientName "
			+" 	OR LOWER(pr.mobile_number) LIKE :patientName )"
			+" 	AND pr.status = 'A'	"
			//+" 	AND pr.unit_id =:unitId "
			+" 	AND pr.organization_id =:organizationId"),
		
		@NamedNativeQuery(name = "PATIENT_AUTO_FILL_SEARCH_PLAIN", 
		query = "	SELECT"
			+" 	pr.patient_id as \"patientId\","
			+" 	concat(coalesce(prfx.prefix_code,''),' ',coalesce( pr.first_name,''),' ',coalesce( pr.middle_name,'' ),' ',coalesce( pr.last_name,'' )) as \"patientName\","
			+" 	pr.uhid_number as \"uhidNumber\","
			+" 	pr.mobile_number as \"mobileNumber\" "
			+" 	FROM patient.t_patient_registration pr	"
			+"  left join public.m_prefix_master prfx "
			+ " 	on prfx.prefix_id = pr.prefix_id " 
			+" 	WHERE	(LOWER(pr.first_name) LIKE :patientName  "
			+" 	OR LOWER(pr.last_name) LIKE :patientName "
			+" 	OR LOWER(pr.uhid_number) LIKE :patientName "
			+" 	OR LOWER(pr.mobile_number) LIKE :patientName )"
			+" 	AND pr.status = 'A'	"
			+" 	AND pr.organization_id =:organizationId"),
		
		@NamedNativeQuery(name = "GET_PATIENT_DETAILS_BY_ID_FOR_CYCLIC_SEARCH", query = "select distinct pat.patient_id as \"patientId\", pat.uhid_number as \"uhidNumber\", pat.first_name as \"firstName\", pat.middle_name as \"middleName\", pat.last_name as \"lastName\",  "
				+ " pat.mobile_number as \"mobileNumber\", pat.image_path as \"imagePath\", to_char(pat.dob,'DD-MM-YYYY') as \"dob\",  "
				+ " trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pat.dob)),'999')) as \"ageString\","
				+ " prfx.prefix_code as \"prefixCode\", gen.gender_name as \"genderName\",gen.gender_id as \"genderId\", patdtls.address as \"address\", cnt.country_name as \"countryName\",  "
				+ " st.state_name as \"stateName\", cty.city_name as \"cityName\", ar.area_name as \"areaName\", patdtls.zip_code as \"zipCode\",  "
				//+ " kin.kin_name as \"kinName\", kin.mobile_no as \"kinMobileNumber\",  "
				+ " msm.service_standard_code as \"serviceCode\", " 
				+"  msm.service_standard_name as \"serviceStandardName\","
				+ " msm.service_master_id as \"serviceId\","
				+ " msm.speciality_id as \"specialityId\" ,"
				+ " spl.speciality_name as \"specialityName\" "
				+ " from patient.t_patient_registration pat  " 
				+ " left join public.m_prefix_master prfx "
				+ " 	on prfx.prefix_id = pat.prefix_id " 
				+ " left join public.m_gender_master gen  "
				+ " on gen.gender_id = pat.gender_id " + " left join patient.t_patient_details patdtls "
				+ " on patdtls.patient_id = pat.patient_id " + " left join public.m_country_master cnt  "
				+ " on cnt.country_id = patdtls.country_id " + " left join public.m_state_master st  "
				+ " on st.state_id = patdtls.state_id " + " left join public.m_city_master cty "
				+ " on cty.city_id = patdtls.city_id " + " left join public.m_area_master ar "
				+ " on ar.area_id = patdtls.area_id " 
				//+ " left join public.m_kin_details kin on kin.patient_id = pat.patient_id "
				+ " inner join opd.m_appointment_master am on am.patient_id = pat.patient_id"
				+ " inner join service.m_service_master as msm on msm.service_master_id = am.service_id "
				+ " inner join doctor.m_speciality_master as spl on spl.speciality_id =  msm.speciality_id "
				+ " inner join t_order_master om on om.patient_id=pat.patient_id  "
				+ " inner join t_order_details od on od.order_id=om.order_id and od.ord_cancelled='N' and od.service_id=am.service_id " 
				+ " where pat.patient_id = :patientId  and am.is_cyclic_appointment='Y' "
				//+ " and pat.is_primary='Y' "
				),
		
		@NamedNativeQuery(name = "GET_PATIENT_DATA", query = " select " 
				 + " 	patReg.patient_id as \"patientId\", " 
				 + " 	patReg.uhid_number as \"uhidNumber\", " 
				 + " 	patReg.file_sequence_number as \"fileSequenceNumber\", " 
				 + " 	patReg.prefix_id as \"prefixId\", " 
				 + " 	preFix.prefix_code as \"prefixCode\", " 
				 + " 	coalesce(patReg.first_name, '') as \"firstName\", " 
				 + " 	coalesce(patReg.middle_name, '') as \"middleName\", " 
				 + " 	coalesce(patReg.last_name, '') as \"lastName\", " 
				 + " 	patReg.gender_id as \"genderId\", " 
				 + " 	patReg.image_path as \"imagePath\", " 
				 + " 	gen.gender_code as \"genderCode\", " 
				 + " 	to_char(patReg.dob, 'MM/DD/YYYY') as \"dob\", " 
				 + " 	to_char(patReg.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	patReg.dob as \"birthDateNew\", " 
				 + " 	trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patReg.dob)), '999')) as \"ageString\", " 
				 + " 	patReg.mobile_number as \"mobileNumber\", " 
				 + " 	patReg.country_calling_code as \"countryCallingCode\", " 
				 + " 	patReg.is_vip as \"isVip\", " 
				 + " 	patReg.vip_remark as \"vipRemark\", " 
				 + " 	patReg.is_blacklist as \"isBlacklist\", " 
				 + " 	patReg.identification_type_id as \"identificationTypeId\", " 
				 + " 	coalesce(identi.identification_name, '') as \"identificationName\", " 
				 + " 	identi.is_expiry_required as \"isExpiryRequired\", " 
				 + " 	patReg.identification_number as \"identificationNumber\", " 
				 + " 	patReg.email as \"email\", " 
				 + " 	patReg.is_absconded as \"isAbsconded\", " 
				 + " 	patReg.alise_name as \"aliseName\", " 				 
				 + " 	patReg.health_number as \"healthNumber\", " 
				 + " 	patReg.health_id as \"healthId\", " 
				 + " 	patReg.aadhar_number as \"aadharNumber\", " 				 
				 + " 	patReg.uuid as \"uuid\", " 
				 + " 	patReg.status as \"status\", " 
				 + " 	patReg.patient_category_id as \"patientCategoryId\", " 
				 + " 	patReg.mpshah_patient_id as \"mpshahPatientId\", " 
				 + " 	patReg.whatsapp_no as \"whatsappNo\", " 
				 + " 	patReg.whatsapp_calling_code as \"whatsappCallingCode\", " 
				 + " 	patReg.skype_id as \"skypeId\", " 
				 + " 	to_char(patReg.identification_expiry_date, 'MM/DD/YYYY') as \"identificationExpiryDate\", " 
				 + " 	patReg.occupation_id as \"occupationId\", " 
				 + " 	patDtls.patient_details_id as \"patientDetailsId\", " 
				 + " 	patDtls.nationality_id as \"nationalityId\", " 
				 + " 	patDtls.race_id as \"raceId\", " 
				 + " 	patDtls.marital_status_id as \"maritalStatusId\", " 
				 + " 	patReg.registration_type_id as \"registrationTypeId\", " 
				 + " 	patDtls.address as \"address\", " 
				 + " 	patDtls.country_id as \"countryId\", " 
				 + " 	patDtls.state_id as \"stateId\", " 
				 + " 	patDtls.district_id as \"districtId\", " 
				 + " 	patDtls.city_id as \"cityId\", " 
                 + " 	com.country_name as \"countryName\", " 
                 + " 	sm.state_name as \"stateName\", " 
                 + " 	cm.city_name as \"cityName\", " 
				 + " 	patDtls.area_id as \"areaId\", " 
				 + " 	patDtls.zip_code as \"zipCode\", " 
				 + " 	patDtls.phone_code as \"phoneCode\", " 
				 + " 	patDtls.phone_number as \"phoneNumber\", " 
				 + " 	patDtls.company_name as \"companyName\", " 
				 + " 	patDtls.company_address as \"companyAddress\", " 
				 + " 	patDtls.company_country_id as \"companyCountryId\", " 
				 + " 	patDtls.company_state_id as \"companyStateId\", " 
				 + " 	patDtls.company_district_id as \"companyDistrictId\", " 
				 + " 	patDtls.company_city_id as \"companyCityId\", " 
				 + " 	patDtls.company_area_id as \"companyAreaId\", " 
				 + " 	patDtls.company_zip_code as \"companyZipCode\", " 
				 + " 	patDtls.company_mobile_number as \"companyMobileNumber\", " 
				 + " 	patDtls.permanent_address as \"permanentAddress\", " 
				 + " 	patDtls.permanent_country_id as \"permanentCountryId\", " 
				 + " 	patDtls.permanent_state_id as \"permanentStateId\", " 
				 + " 	patDtls.permanent_district_id as \"permanentDistrictId\", " 
				 + " 	patDtls.permanent_city_id as \"permanentCityId\", " 
				 + " 	patDtls.permanent_area_id as \"permanentAreaId\", " 
				 + " 	patDtls.permanent_zip_code as \"permanentZipCode\", " 
				 + " 	patDtls.permanent_phone_code as \"permanentPhoneCode\", " 
				 + " 	patDtls.permanent_phone_number as \"permanentPhoneNumber\", " 
				 + " 	areaS.area_name as \"areaName\", " 
	             + "  uabc.bucket_flag as \"bucketFlag\", "
               // HIS-30680 Query Optimization+ "    adm.id as \"admissionId\", " 
               //  + "    adm.visit_type_id  as \"visitTypeId\", " 
               //  + "    enc_count as \"encounterCount\", " 
               //  + "    adm.admission_number as \"admissionNumber\","
                 + " ta.admission_id as \"admissionId\", "
                 + " ta.visit_type_id as \"visitTypeId\", "
                 + " ta.admission_number as \"admissionNumber\", "
                 + " patReg.legacy_registration_id as \"legacyRegistrationId\", "
                 + " ( select count(*) as enc_count  from opd.t_encounter_master enc where enc.unit_id =:unitId and enc.status_id in (2, 7, 9, 5) and enc.patient_id = patReg.patient_id group by patient_id LIMIT 1 )   as  \"encounterCount\", "
            	 + " 	patReg.blood_group as \"bloodGroup\" " 
                 + " from " 
				 + " 	patient.t_patient_registration patReg " 
				 + " inner join patient.t_patient_details patDtls on " 
				 + " 	patReg.patient_id = patDtls.patient_id " 
	             + "  INNER JOIN public.t_unit_application_billing_configuration uabc on patReg.unit_id = uabc.unit_id "                
				 + " inner join public.m_gender_master gen on " 
				 + " 	patReg.gender_id = gen.gender_id "
                 + " left join public.m_state_master sm on " 
                 + " 	sm.state_id = patDtls.state_id " 
                 + " left join public.m_country_master com on " 
                 + " 	com.country_id = patDtls.country_id " 
                 + " left join public.m_city_master cm on " 
                 + " 	cm.city_id = patDtls.city_id " 
				 + " left join public.m_area_master areaS on "  
				 + "	areaS.area_id  = patDtls.area_id" 
				 + " left outer join public.m_prefix_master preFix on " 
				 + " 	patReg.prefix_id = preFix.prefix_id " 
				 + " left outer join adt.m_identification_master identi on " 
				 + " 	patReg.identification_type_id = identi.identification_id " 
				 + " left join adt.t_admission ta on ta.status = 'A' and ta.unit_id =:unitId and ta.patient_id = patReg.patient_id "
				 /* HIS-30680 Query Optimization + " left  join " 
				 + " ( " 
				 + " select max(admission_id) id, patient_id , visit_type_id , admission_number "
				 + " from adt.t_admission ta "
				 + " where ta.status = 'A' and ta.unit_id =:unitId "
				 + " group by patient_id , visit_type_id , admission_number) adm "
				 + " on adm.patient_id = patReg.patient_id "
    			 + " left  join "
                 + " ( "
				 + " select count(*) as enc_count , patient_id "
				 + " from opd.t_encounter_master enc "
				 + " where enc.unit_id =:unitId "
				 + " and enc.status_id in (2,7,9,5) "
				 + " group by patient_id) enc "
				 + " on enc.patient_id = patReg.patient_id "*/
				 + " where " 
				 + " 	patReg.organization_id =:orgId " 
				 + " 	and patReg.is_primary = 'Y'"
				 + "    and patDtls.status ='A' "),
		
		@NamedNativeQuery(name = "GET_PATIENT_DATA_ABDM", query = " select " 
				 + " 	patReg.patient_id as \"patientId\", " 
				 + " 	patReg.uhid_number as \"uhidNumber\", " 
				 + " 	patReg.file_sequence_number as \"fileSequenceNumber\", " 
				 + " 	patReg.prefix_id as \"prefixId\", " 
				 + " 	preFix.prefix_code as \"prefixCode\", " 
				 + " 	coalesce(patReg.first_name, '') as \"firstName\", " 
				 + " 	coalesce(patReg.middle_name, '') as \"middleName\", " 
				 + " 	coalesce(patReg.last_name, '') as \"lastName\", "
				 + "concat(coalesce(patReg.first_name,'') ,' ', coalesce(patReg.middle_name,'') ,' ', coalesce(patReg.last_name,'')) as \"patientName\", " 
				 + " 	patReg.gender_id as \"genderId\", " 
				 + " 	patReg.image_path as \"imagePath\", " 
				 + " 	gen.gender_code as \"genderCode\", " 
				 + " 	to_char(patReg.dob, 'MM/DD/YYYY') as \"dob\", " 
				 + " 	to_char(patReg.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	patReg.dob as \"birthDateNew\", " 
				 + " 	trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patReg.dob)), '999')) as \"ageString\", " 
				 + " 	patReg.mobile_number as \"mobileNumber\", " 
				 + " 	patReg.country_calling_code as \"countryCallingCode\", " 
				 + " 	patReg.is_vip as \"isVip\", " 
				 + " 	patReg.vip_remark as \"vipRemark\", " 
				 + " 	patReg.is_blacklist as \"isBlacklist\", " 
				 + " 	patReg.identification_type_id as \"identificationTypeId\", " 
				 + " 	coalesce(identi.identification_name, '') as \"identificationName\", " 
				 + " 	identi.is_expiry_required as \"isExpiryRequired\", " 
				 + " 	patReg.identification_number as \"identificationNumber\", " 
				 + " 	patReg.email as \"email\", " 
				 + " 	patReg.is_absconded as \"isAbsconded\", " 
				 + " 	patReg.alise_name as \"aliseName\", " 				 
				 + " 	patReg.health_number as \"healthNumber\", " 
				 + " 	patReg.health_id as \"healthId\", " 
				 + " 	patReg.aadhar_number as \"aadharNumber\", " 				 
				 + " 	patReg.uuid as \"uuid\", " 
				 + " 	patReg.status as \"status\", " 
				 + " 	patReg.patient_category_id as \"patientCategoryId\", " 
				 + " 	patReg.mpshah_patient_id as \"mpshahPatientId\", " 
				 + " 	patReg.whatsapp_no as \"whatsappNo\", " 
				 + " 	patReg.whatsapp_calling_code as \"whatsappCallingCode\", " 
				 + " 	patReg.skype_id as \"skypeId\", " 
				 + " 	to_char(patReg.identification_expiry_date, 'MM/DD/YYYY') as \"identificationExpiryDate\", " 
				 + " 	patReg.occupation_id as \"occupationId\", " 
				 + " 	patDtls.patient_details_id as \"patientDetailsId\", " 
				 + " 	patDtls.nationality_id as \"nationalityId\", " 
				 + " 	patDtls.race_id as \"raceId\", " 
				 + " 	patDtls.marital_status_id as \"maritalStatusId\", " 
				 + " 	patReg.registration_type_id as \"registrationTypeId\", " 
				 + " 	patDtls.address as \"address\", " 
				 + " 	patDtls.country_id as \"countryId\", " 
				 + " 	patDtls.state_id as \"stateId\", " 
				 + " 	patDtls.district_id as \"districtId\", " 
				 + " 	patDtls.city_id as \"cityId\", " 
                + " 	com.country_name as \"countryName\", " 
                + " 	sm.state_name as \"stateName\", " 
                + " 	cm.city_name as \"cityName\", " 
				 + " 	patDtls.area_id as \"areaId\", " 
				 + " 	patDtls.zip_code as \"zipCode\", " 
				 + " 	patDtls.phone_code as \"phoneCode\", " 
				 + " 	patDtls.phone_number as \"phoneNumber\", " 
				 + " 	patDtls.company_name as \"companyName\", " 
				 + " 	patDtls.company_address as \"companyAddress\", " 
				 + " 	patDtls.company_country_id as \"companyCountryId\", " 
				 + " 	patDtls.company_state_id as \"companyStateId\", " 
				 + " 	patDtls.company_district_id as \"companyDistrictId\", " 
				 + " 	patDtls.company_city_id as \"companyCityId\", " 
				 + " 	patDtls.company_area_id as \"companyAreaId\", " 
				 + " 	patDtls.company_zip_code as \"companyZipCode\", " 
				 + " 	patDtls.company_mobile_number as \"companyMobileNumber\", " 
				 + " 	patDtls.permanent_address as \"permanentAddress\", " 
				 + " 	patDtls.permanent_country_id as \"permanentCountryId\", " 
				 + " 	patDtls.permanent_state_id as \"permanentStateId\", " 
				 + " 	patDtls.permanent_district_id as \"permanentDistrictId\", " 
				 + " 	patDtls.permanent_city_id as \"permanentCityId\", " 
				 + " 	patDtls.permanent_area_id as \"permanentAreaId\", " 
				 + " 	patDtls.permanent_zip_code as \"permanentZipCode\", " 
				 + " 	patDtls.permanent_phone_code as \"permanentPhoneCode\", " 
				 + " 	patDtls.permanent_phone_number as \"permanentPhoneNumber\", " 
				 + " 	areaS.area_name as \"areaName\", " 
	             + "  uabc.bucket_flag as \"bucketFlag\", "
                + " ta.admission_id as \"admissionId\", "
                + " ta.visit_type_id as \"visitTypeId\", "
                + " ta.admission_number as \"admissionNumber\", "
                + " ( select count(*) as enc_count  from opd.t_encounter_master enc where enc.unit_id =:unitId and enc.status_id in (2, 7, 9, 5) and enc.patient_id = patReg.patient_id group by patient_id LIMIT 1 )   as  \"encounterCount\", " 
           	 + " 	patReg.blood_group as \"bloodGroup\" " 
                + " from " 
				 + " 	patient.t_patient_registration patReg " 
				 + " inner join patient.t_patient_details patDtls on " 
				 + " 	patReg.patient_id = patDtls.patient_id " 
	             + "  INNER JOIN public.t_unit_application_billing_configuration uabc on patReg.unit_id = uabc.unit_id "                
				 + " inner join public.m_gender_master gen on " 
				 + " 	patReg.gender_id = gen.gender_id "
                + " left join public.m_state_master sm on " 
                + " 	sm.state_id = patDtls.state_id " 
                + " left join public.m_country_master com on " 
                + " 	com.country_id = patDtls.country_id " 
                + " left join public.m_city_master cm on " 
                + " 	cm.city_id = patDtls.city_id " 
				 + " left join public.m_area_master areaS on "  
				 + "	areaS.area_id  = patDtls.area_id" 
				 + " left outer join public.m_prefix_master preFix on " 
				 + " 	patReg.prefix_id = preFix.prefix_id " 
				 + " left outer join adt.m_identification_master identi on " 
				 + " 	patReg.identification_type_id = identi.identification_id " 
				 + " left join adt.t_admission ta on ta.status = 'A' and ta.unit_id =:unitId and ta.patient_id = patReg.patient_id "
				 + " where " 
				 + " 	patReg.organization_id =:orgId"
				 + " and ((patReg.first_name) LIKE :searchKeyword "
				 +" 	OR (patReg.last_name) LIKE :searchKeyword "
                 +" 	OR (patReg.health_id) LIKE :searchKeyword "
				 +" 	OR (patReg.uhid_number) LIKE :searchKeyword "
				 +" 	OR (patReg.mobile_number) LIKE  :searchKeyword )" 
				 + " 	and patReg.health_number is not null"
				 + " 	and patReg.is_primary = 'Y'"
				 + "    and patDtls.status ='A' "),
		
		
		@NamedNativeQuery(name = "GET_PATIENT_DATA_ABDM_DEEPLINKING", query = " select " 
				 + " 	patReg.patient_id as \"patientId\", " 
				 + " 	patReg.uhid_number as \"uhidNumber\", " 
				 + " 	patReg.file_sequence_number as \"fileSequenceNumber\", " 
				 + " 	patReg.prefix_id as \"prefixId\", " 
				 + " 	preFix.prefix_code as \"prefixCode\", " 
				 + " 	coalesce(patReg.first_name, '') as \"firstName\", " 
				 + " 	coalesce(patReg.middle_name, '') as \"middleName\", " 
				 + " 	coalesce(patReg.last_name, '') as \"lastName\", "
				 + "concat(coalesce(patReg.first_name,'') ,' ', coalesce(patReg.middle_name,'') ,' ', coalesce(patReg.last_name,'')) as \"patientName\", " 
				 + " 	patReg.gender_id as \"genderId\", " 
				 + " 	patReg.image_path as \"imagePath\", " 
				 + " 	gen.gender_code as \"genderCode\", " 
				 + " 	to_char(patReg.dob, 'MM/DD/YYYY') as \"dob\", " 
				 + " 	to_char(patReg.dob, 'DD/MM/YYYY') as \"birthDate\", " 
				 + " 	patReg.dob as \"birthDateNew\", " 
				 + " 	trim(to_char(extract(year from age(CURRENT_TIMESTAMP, patReg.dob)), '999')) as \"ageString\", " 
				 + " 	patReg.mobile_number as \"mobileNumber\", " 
				 + " 	patReg.country_calling_code as \"countryCallingCode\", " 
				 + " 	patReg.is_vip as \"isVip\", " 
				 + " 	patReg.vip_remark as \"vipRemark\", " 
				 + " 	patReg.is_blacklist as \"isBlacklist\", " 
				 + " 	patReg.identification_type_id as \"identificationTypeId\", " 
				 + " 	coalesce(identi.identification_name, '') as \"identificationName\", " 
				 + " 	identi.is_expiry_required as \"isExpiryRequired\", " 
				 + " 	patReg.identification_number as \"identificationNumber\", " 
				 + " 	patReg.email as \"email\", " 
				 + " 	patReg.is_absconded as \"isAbsconded\", " 
				 + " 	patReg.alise_name as \"aliseName\", " 				 
				 + " 	patReg.health_number as \"healthNumber\", " 
				 + " 	patReg.health_id as \"healthId\", " 
				 + " 	patReg.aadhar_number as \"aadharNumber\", " 				 
				 + " 	patReg.uuid as \"uuid\", " 
				 + " 	patReg.status as \"status\", " 
				 + " 	patReg.patient_category_id as \"patientCategoryId\", " 
				 + " 	patReg.mpshah_patient_id as \"mpshahPatientId\", " 
				 + " 	patReg.whatsapp_no as \"whatsappNo\", " 
				 + " 	patReg.whatsapp_calling_code as \"whatsappCallingCode\", " 
				 + " 	patReg.skype_id as \"skypeId\", " 
				 + " 	to_char(patReg.identification_expiry_date, 'MM/DD/YYYY') as \"identificationExpiryDate\", " 
				 + " 	patReg.occupation_id as \"occupationId\", " 
				 + " 	patDtls.patient_details_id as \"patientDetailsId\", " 
				 + " 	patDtls.nationality_id as \"nationalityId\", " 
				 + " 	patDtls.race_id as \"raceId\", " 
				 + " 	patDtls.marital_status_id as \"maritalStatusId\", " 
				 + " 	patReg.registration_type_id as \"registrationTypeId\", " 
				 + " 	patDtls.address as \"address\", " 
				 + " 	patDtls.country_id as \"countryId\", " 
				 + " 	patDtls.state_id as \"stateId\", " 
				 + " 	patDtls.district_id as \"districtId\", " 
				 + " 	patDtls.city_id as \"cityId\", " 
               + " 	com.country_name as \"countryName\", " 
               + " 	sm.state_name as \"stateName\", " 
               + " 	cm.city_name as \"cityName\", " 
				 + " 	patDtls.area_id as \"areaId\", " 
				 + " 	patDtls.zip_code as \"zipCode\", " 
				 + " 	patDtls.phone_code as \"phoneCode\", " 
				 + " 	patDtls.phone_number as \"phoneNumber\", " 
				 + " 	patDtls.company_name as \"companyName\", " 
				 + " 	patDtls.company_address as \"companyAddress\", " 
				 + " 	patDtls.company_country_id as \"companyCountryId\", " 
				 + " 	patDtls.company_state_id as \"companyStateId\", " 
				 + " 	patDtls.company_district_id as \"companyDistrictId\", " 
				 + " 	patDtls.company_city_id as \"companyCityId\", " 
				 + " 	patDtls.company_area_id as \"companyAreaId\", " 
				 + " 	patDtls.company_zip_code as \"companyZipCode\", " 
				 + " 	patDtls.company_mobile_number as \"companyMobileNumber\", " 
				 + " 	patDtls.permanent_address as \"permanentAddress\", " 
				 + " 	patDtls.permanent_country_id as \"permanentCountryId\", " 
				 + " 	patDtls.permanent_state_id as \"permanentStateId\", " 
				 + " 	patDtls.permanent_district_id as \"permanentDistrictId\", " 
				 + " 	patDtls.permanent_city_id as \"permanentCityId\", " 
				 + " 	patDtls.permanent_area_id as \"permanentAreaId\", " 
				 + " 	patDtls.permanent_zip_code as \"permanentZipCode\", " 
				 + " 	patDtls.permanent_phone_code as \"permanentPhoneCode\", " 
				 + " 	patDtls.permanent_phone_number as \"permanentPhoneNumber\", " 
				 + " 	areaS.area_name as \"areaName\", " 
	             + "  uabc.bucket_flag as \"bucketFlag\", "
               + " ta.admission_id as \"admissionId\", "
               + " ta.visit_type_id as \"visitTypeId\", "
               + " ta.admission_number as \"admissionNumber\", "
               + " ( select count(*) as enc_count  from opd.t_encounter_master enc where enc.unit_id =:unitId and enc.status_id in (2, 7, 9, 5) and enc.patient_id = patReg.patient_id group by patient_id LIMIT 1 )   as  \"encounterCount\", " 
          	 + " 	patReg.blood_group as \"bloodGroup\" " 
               + " from " 
				 + " 	patient.t_patient_registration patReg " 
				 + " inner join patient.t_patient_details patDtls on " 
				 + " 	patReg.patient_id = patDtls.patient_id " 
	             + "  INNER JOIN public.t_unit_application_billing_configuration uabc on patReg.unit_id = uabc.unit_id "                
				 + " inner join public.m_gender_master gen on " 
				 + " 	patReg.gender_id = gen.gender_id "
               + " left join public.m_state_master sm on " 
               + " 	sm.state_id = patDtls.state_id " 
               + " left join public.m_country_master com on " 
               + " 	com.country_id = patDtls.country_id " 
               + " left join public.m_city_master cm on " 
               + " 	cm.city_id = patDtls.city_id " 
				 + " left join public.m_area_master areaS on "  
				 + "	areaS.area_id  = patDtls.area_id" 
				 + " left outer join public.m_prefix_master preFix on " 
				 + " 	patReg.prefix_id = preFix.prefix_id " 
				 + " left outer join adt.m_identification_master identi on " 
				 + " 	patReg.identification_type_id = identi.identification_id " 
				 + " left join adt.t_admission ta on ta.status = 'A' and ta.unit_id =:unitId and ta.patient_id = patReg.patient_id "
				 + " where " 
				 + " 	patReg.organization_id =:orgId"
				 + " and ((patReg.first_name) LIKE :searchKeyword "
				 +" 	OR (patReg.last_name) LIKE :searchKeyword "
                +" 	OR (patReg.health_id) LIKE :searchKeyword "
				 +" 	OR (patReg.uhid_number) LIKE :searchKeyword "
				 +" 	OR (patReg.mobile_number) LIKE  :searchKeyword )" 
				 //+ " 	and patReg.health_number is not null"
				 + " 	and patReg.is_primary = 'Y'"
				 + "    and patDtls.status ='A' ")
		
})

@Entity
@Table(name = "t_patient_registration", schema = "patient")
@SequenceGenerator(name = "patient_registration_seq", sequenceName = "patient.patient_registration_seq", allocationSize = 1)
public class PatientRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_registration_seq")
	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "uhid_number")
	private String uhidNumber;

	@Column(name = "prefix_id")
	private Integer prefixId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender_id")
	private Integer genderId;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "country_calling_code")
	private String countryCallingCode;

	@Column(name = "bar_code")
	private String barCode;

	@Column(name = "is_vip")
	private Character isVip;

	@Column(name = "vip_remark")
	private String vipRemark;

	@Column(name = "is_blacklist")
	private char isBlacklist;

	@Column(name = "identification_type_id")
	private Integer identificationTypeId;

	@Column(name = "identification_number")
	private String identificationNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "patient_category_id")
	private Integer patientCategoryId;

	@Column(name = "is_unknown_reg")
	private char isUnknownReg;

	@Column(name = "is_otc_reg")
	private char isOtcReg;

	@Column(name = "is_pre_reg")
	private char isPreReg;

	@Column(name = "alise_name")
	private String aliseName;

	@Column(name = "identification_expiry_date")
	private Date identificationExpiryDate;

	@Column(name = "occupation_id")
	private Integer occupationId;

	@Column(name = "registration_type_id")
	private Integer registrationTypeId;

	@Column(name = "previous_id")
	private Integer previousId;

	@Column(name = "emp_doc_id")
	private Integer empDocId;

	@Column(name = "emp_dep_type_id")
	private Integer empDepTypeId;

	@Column(name = "emp_doc_dep_id")
	private Integer empDocDepId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "image_path")
	private String imagePath;

	@Column(name = "is_primary")
	private Character isPrimary;

	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "mpshah_patient_id")
	private String mpshahPatientId;

	@Column(name = "image")
	private byte[] image;
	
	@Column(name = "is_absconded")
	private Character isAbsconded;
	
	@Column(name = "whatsapp_no")
	private String whatsappNo;

	@Column(name = "whatsapp_calling_code")
	private String whatsappCallingCode;
	
	@Column(name = "skype_id")
	private String skypeId;
	
	@Column(name = "file_sequence_number")
	private String fileSequenceNumber;
	
	@Column(name = "patient_remark")
	private String patientRemark;

	@Column(name = "video_call_url")
	private String videoCallUrl;
	
	@Column(name = "blood_group")
	private String bloodGroup;
	
    @Column(name = "health_number")
    private String healthNumber;
    
    @Column(name = "health_id")
    private String healthId;
    
    @Column(name = "aadhar_number")
    private String aadharNumber;

	@Column(name = "legacy_registration_id")
    private String legacyRegistrationId;

    
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefix_id", insertable = false, updatable = false)
	private PrefixMaster prefixMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_category_id", insertable = false, updatable = false)
	private PatientCategoryMaster patientCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identification_type_id", insertable = false, updatable = false)
	private IdentificationMaster identificationMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patientRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PatientDetails> patientDetailslist;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patientRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Admission> admissionlist;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patientRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DepositMaster> listDepositMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patientRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patientRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AppointmentMaster> appointmentMastersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patientRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NursingOrderMapper> nursingOrderMapperList;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Character getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Character isPrimary) {
		this.isPrimary = (isPrimary == '\u0000') ? 'Y' : isPrimary;
	}

	public List<TPeriodicityServiceOrders> getListTPeriodicityServiceOrders() {
		return listTPeriodicityServiceOrders;
	}

	public void setListTPeriodicityServiceOrders(List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders) {
		this.listTPeriodicityServiceOrders = listTPeriodicityServiceOrders;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getEmpDocId() {
		return empDocId;
	}

	public void setEmpDocId(Integer empDocId) {
		this.empDocId = empDocId;
	}

	public Integer getEmpDepTypeId() {
		return empDepTypeId;
	}

	public void setEmpDepTypeId(Integer empDepTypeId) {
		this.empDepTypeId = empDepTypeId;
	}

	public Integer getEmpDocDepId() {
		return empDocDepId;
	}

	public void setEmpDocDepId(Integer empDocDepId) {
		this.empDocDepId = empDocDepId;
	}

	public IdentificationMaster getIdentificationMaster() {
		return identificationMaster;
	}

	public void setIdentificationMaster(IdentificationMaster identificationMaster) {
		this.identificationMaster = identificationMaster;
	}

	public List<PatientDetails> getPatientDetailslist() {
		return patientDetailslist;
	}

	public void setPatientDetailslist(List<PatientDetails> patientDetailslist) {
		this.patientDetailslist = patientDetailslist;
	}

	public List<DepositMaster> getListDepositMaster() {
		return listDepositMaster;
	}

	public void setListDepositMaster(List<DepositMaster> listDepositMaster) {
		this.listDepositMaster = listDepositMaster;
	}

	public char getIsOtcReg() {
		return isOtcReg;
	}

	public void setIsOtcReg(char isOtcReg) {
		this.isOtcReg = (isOtcReg == '\u0000') ? 'N' : isOtcReg;
	}

	public char getIsPreReg() {
		return isPreReg;
	}

	public void setIsPreReg(char isPreReg) {
		this.isPreReg = (isPreReg == '\u0000') ? 'N' : isPreReg;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public Integer getPreviousId() {
		return previousId;
	}

	public void setPreviousId(Integer previousId) {
		this.previousId = previousId;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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
		this.isVip = (isVip == '\u0000') ? 'N' : isVip;
	}

	public char getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(char isBlacklist) {
		this.isBlacklist = (isBlacklist == '\u0000') ? 'N' : isBlacklist;
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

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public char getIsUnknownReg() {
		return isUnknownReg;
	}

	public void setIsUnknownReg(char isUnknownReg) {
		this.isUnknownReg = (isUnknownReg == '\u0000') ? 'N' : isUnknownReg;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public PatientCategoryMaster getPatientCategoryMaster() {
		return patientCategoryMaster;
	}

	public void setPatientCategoryMaster(PatientCategoryMaster patientCategoryMaster) {
		this.patientCategoryMaster = patientCategoryMaster;
	}

	public String getVipRemark() {
		return vipRemark;
	}

	public void setVipRemark(String vipRemark) {
		this.vipRemark = vipRemark;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getMpshahPatientId() {
		return mpshahPatientId;
	}

	public void setMpshahPatientId(String mpshahPatientId) {
		this.mpshahPatientId = mpshahPatientId;
	}

	public Character getIsAbsconded() {
		return isAbsconded;
	}

	public void setIsAbsconded(Character isAbsconded) {
		this.isAbsconded = isAbsconded;
	}

	public String getWhatsappNo() {
		return whatsappNo;
	}

	public void setWhatsappNo(String whatsappNo) {
		this.whatsappNo = whatsappNo;
	}

	public String getWhatsappCallingCode() {
		return whatsappCallingCode;
	}

	public void setWhatsappCallingCode(String whatsappCallingCode) {
		this.whatsappCallingCode = whatsappCallingCode;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getFileSequenceNumber() {
		return fileSequenceNumber;
	}

	public void setFileSequenceNumber(String fileSequenceNumber) {
		this.fileSequenceNumber = fileSequenceNumber;
	}

	public String getVideoCallUrl() {
		return videoCallUrl;
	}

	public void setVideoCallUrl(String videoCallUrl) {
		this.videoCallUrl = videoCallUrl;
	}

	public String getPatientRemark() {
		return patientRemark;
	}

	public void setPatientRemark(String patientRemark) {
		this.patientRemark = patientRemark;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		this.healthNumber = healthNumber;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getLegacyRegistrationId() {
		return legacyRegistrationId;
	}

	public void setLegacyRegistrationId(String legacyRegistrationId) {
		this.legacyRegistrationId = legacyRegistrationId;
	}
}
