package com.param.entity.model.opd;

import java.util.Date;

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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.pharmacy.Instruction;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.opd.model.FrequencyMaster;

//@NamedQuery(name = "GET_MEDICATION_HISTORY_LIST",
//query = "SELECT medicationHistory.medicationHistoryId as medicationHistoryId,"
//		+ " medicationHistory.encounterId as encounterId, "
//		+ " medicationHistory.itemId as itemId, "
//		+ " medicationHistory.roaId as roaId, "
//		+ " medicationHistory.doseId as doseId, "
//		+ " medicationHistory.itemName as itemName, "
//		+ " medicationHistory.genericName as genericName, "
//		+ " medicationHistory.doseId as doseId, "
//		+ " medicationHistory.frequencyId as frequencyId, "
//		+ " medicationHistory.genericId as genericId, "
//		+ " medicationHistory.organizationId as organizationId, "
//		+ " medicationHistory.patientId as patientId, "
//		+ " medicationHistory.duration as duration, "
//		+ " medicationHistory.durationUnit as durationUnit, "
//		+ " medicationHistory.instructionId as instructionId, "
//		+ " medicationHistory.status as status, "
//		+ " medicationHistory.unitId as unitId, "
//		+ " routeOfAdministration.description as description, "
//		+ " instruction.instructionDesc as instructionDesc "
//		+ " FROM MedicationHistory medicationHistory"
//		+ " INNER JOIN medicationHistory.routeOfAdministration routeOfAdministration"
//		+ " INNER JOIN medicationHistory.generic generic"
//		+ " INNER JOIN medicationHistory.instruction instruction"
//		+ " WHERE medicationHistory.organizationId= :organizationId "
//		+ " AND medicationHistory.unitId= :unitId "
//	)
@NamedNativeQueries({
/*@NamedNativeQuery(name="GET_ADMINISTRATION_WORKLIST_LIST", 
									query=" select  distinct(prd.prescription_id) as \"prescriptionId\" ,pres.patient_id AS \"patientId\" , case when (pres.encounter_id is null) then pres.admission_id else  pres.encounter_id end  AS \"encounterId\" , pres.admission_id AS  \"admissionId\", pres.doctor_id AS  \"doctorId\" , "
											+ " concat(ps.first_name,' ',ps.middle_name,' ',ps.last_name) AS \"patientName\" , concat(doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\" ,"
											+ " to_char(prd.prescription_date, 'dd/mm/yyyy') AS \"prescriptionDate\", to_char(ps.dob, 'DD-MM-YYYY hh:mm:ss') as \"dob\" , ps.uhid_number as \"uhidNumber\" ,"
											+ " to_char(admdtl.doa, 'dd/MM/yyyy') as \"doa\",  "
											+ "  to_char(admdtl.pdd, 'dd/MM/yyyy') as \"pdd\", "
											+ "  ward.ward_name as \"wardName\", "
											+ "  bm.bed_number as \"bedNumber\", "
											+ " adm.speciality_id AS \"specialityId\" ,splmst.speciality_name as \"specialityName\" "
											+	" from pharmacy.t_prescription_detail prd "
											+	" INNER JOIN pharmacy.t_prescription pres on pres.id=prd.prescription_id "
											+	" INNER JOIN patient.t_patient_registration ps on pres.patient_id=ps.patient_id "
											+	" INNER JOIN doctor.m_doctor_master doc on pres.doctor_id=doc.doctor_id "
											+	" left join adt.t_admission adm on pres.admission_id=adm.admission_id"
											+	" left join adt.t_admission_details admdtl on admdtl.admission_id=adm.admission_id"
											+	" left join opd.t_encounter_master etm on pres.encounter_id= etm.encounter_id"
											+	" left join doctor.m_speciality_master splmst on adm.speciality_id=splmst.speciality_id"
											+	" left join doctor.m_speciality_master spl on etm.dept_id=spl.speciality_id"
											+ "  left JOIN adt.m_ward_master ward on admdtl.ward_id = ward.ward_id "
											+ "  left JOIN adt.m_bed_master bm on admdtl.bed_id = bm.bed_id "

											+	" WHERE  prd.statflag=true AND prd.is_vaccine=true"
											+ " AND prd.organization_id= :organizationId "
											+ " AND prd.unit_id= :unitId"
										
											),*/

	@NamedNativeQuery(name="GET_ADMINISTRATION_WORKLIST_LIST",
			query=" select "
			+ "distinct(pres.id) as \"prescriptionId\" ,"
			+ " pres.patient_id as \"patientId\" , " +
			" pres.encounter_id AS \"encounterId\"," +
			" pres.doctor_id AS \"doctorId\" , " +
			" concat(ps.first_name,' ',ps.middle_name,' ',ps.last_name) AS \"patientName\" , concat(doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\" ," +
			" to_char(ps.dob, 'DD-MM-YYYY hh:mm:ss') as \"dob\" ," +
			" ps.uhid_number as \"uhidNumber\" ," +
			" gen.gender_code as \"genderDesc\"," +
			" gen.gender_id as \"genderId\"," +
			" to_char(pres.prescription_date, 'dd/MM/yyyy') as \"orderDate\", " +
			" to_char(pres.prescription_date, 'HH24:MI') as \"orderTime\", "
			+ " spl.speciality_name as \"specialityName\", "
			+ "pres.status_id as \"statusId\","
			+ " sts.status as \"statusName\","
			+ " ps.image_path as \"imagePath\", prefixmaster.prefix_code as \"prefixCode\" ,gen.gender_code as \"genderCode\" ,"
			+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, ps.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, ps.dob)),'M ',EXTRACT(day from AGE(CURRENT_TIMESTAMP, ps.dob)),'D ') as \"ageString\" "

			+ " from  pharmacy.t_prescription pres "
			+" INNER JOIN patient.t_patient_registration ps on pres.patient_id=ps.patient_id "
			+" left join public.m_prefix_master prefixmaster on ps.prefix_id = prefixmaster.prefix_id "
			+" INNER JOIN doctor.m_doctor_master doc on pres.doctor_id=doc.doctor_id "
			+" inner join adt.t_admission ta  on ta.admission_id  = pres.admission_id "
			+" left join doctor.m_speciality_master spl on pres.dept_id=spl.speciality_id"
			+" left join public.m_gender_master gen on gen.gender_id=ps.gender_id "
			+ " inner join public.m_status sts on sts.id = pres.status_id "
			+" WHERE   pres.organization_id= :organizationId "
			+ " AND pres.unit_id= :unitId"
			+ " AND (case when (select count(*)from pharmacy.t_prescription_detail tpd where tpd.prescription_id  =pres.id and statflag = true ) > 0 then true else false end ) = true"
			+ " and to_char(pres.prescription_date,'YYYY-MM-DD') >=:fromDate" + 
			"  and to_char(pres.prescription_date,'YYYY-MM-DD') <=:toDate "
			+ "  and ta.admission_id =  :admissionId "
			+ " order by pres.id desc"

			),

@NamedNativeQuery(name="GET_MEDICATION_HISTORY_LIST", 
									query=" select "
									+" itm.id as \"itemId\","
									+ " itm.formulation_type_id as \"formulationTypeId\", "
									+ " itm.generic_id as \"genericId\", "
									+ " medHis.medication_history_id as \"medicationHistoryId\","
									+" medHis.item_name as \"itemName\" ,"
									+" su.unit as \"doseUnit\","
									+" medHis.frequency_id as \"frequencyId\" ,"
									+ " mft.type as \"formulationType\", "
									+" freq.description as \"frequencyDesc\" ,"
									+" freq.no_of_administration_time as \"noOfAdministrationTime\" ,"
									+" medHis.roa_id as \"roaId\" ,"
									+" medHis.is_non_formulary as \"isNonFormulary\" ,"
									+" roa.description as \"roaDesc\" ,"
									+ " medHis.is_medication_stop as \"isMedicationStop\" , "
									+" to_char( medHis.start_date,"
									+" 'dd/mm/yyyy' ) as \"startDate\" ,"
									+"  medHis.start_date as \"startDateNew\" ,"
									+" to_char( medHis.updated_date,"
									+" 'dd/mm/yyyy' ) as \"updatedDate\" ,"
									+" to_char(medHis.end_date,"
									+" 'dd/mm/yyyy' ) as \"endDate\" ,"
									+" medHis.end_date as \"endDateNew\" ,"
									+" medHis.instruction_id as \"instructionId\" ,"
									+" instr.instruction_desc as \"instructionDesc\" ,"
									+" medHis.generic_name as \"genericName\","
									+" medHis.dose_unit_id as \"doseUnitId\","
									+" medHis.dose_id as \"doseId\","
									+" medHis.order_qty as \"orderQty\" ,"
									+" medHis.duration as \"duration\", "
									+" medHis.duration_unit as \"durationUnit\", "
									+" to_char(medHis.created_date, 'DD/MM/YYYY') as \"createdDate\" ,"
									+ " (case "
									+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
									+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
									+ " else 'Admin' end) as \"userName\", "
									+" medHis.is_medication_continue as \"isMedicationContinue\","
									+" medHis.is_no_significant_medication as \"isNoSignificantMedication\","
									+ " medHis.is_medication_stop as \"isMedicationStopHis\",  "
									+ " medHis.justification_stop as \"justificationStopHis\"  "
								+" from "
									+" opd.t_medication_history medHis "
								+" left join public.m_strength_unit su on "
								+"	medHis.dose_unit_id = su.id "
								+" left join public.m_item itm on "
								+"	itm.id = medHis.item_id "
								+ " left JOIN public.m_formulation_type mft on mft.id=itm.formulation_type_id "
								+" left join public.m_route_of_administration roa on "
									+"roa.id = medHis.roa_id "
								+" left join public.m_generic gen on "
									+" gen.id  = medHis.generic_id "
								+" left join public.m_frequency freq on "
									+"freq.id = medHis.frequency_id "
								+" left join public.m_instruction instr on "
								+"	instr.instruction_id = medHis.instruction_id "
								+ " 	left join PUBLIC.m_employee_master empmst on " 
								 + " 		medHis.created_by = empmst.user_id " 
								 + " 	left join doctor.m_doctor_master docmst on " 
								 + " 		medHis.created_by = docmst.user_id " 
								+" where "
								+"	medHis.unit_id=:unitId "
									+" and medHis.organization_id=:organizationId "
									+" and medHis.patient_id=:patientId "
									+" and medHis.status='A' "
									+ " order by medHis.medication_history_id asc "									
										),

		@NamedNativeQuery(name = "GET_MEDICATION_HISTORY_LIST_TEMPLATE", query = " select " + " itm.id as \"itemId\","
				+ " itm.formulation_type_id as \"formulationTypeId\", " + " itm.generic_id as \"genericId\", "
				+ " medHis.medication_history_id as \"medicationHistoryId\"," + " medHis.item_name as \"itemName\" ,"
				+ " su.unit as \"doseUnit\"," + " medHis.frequency_id as \"frequencyId\" ,"
				+ " freq.description as \"frequencyDesc\" ," + " medHis.roa_id as \"roaId\" ,"
				+ " roa.description as \"roaDesc\" ," + " medHis.is_medication_stop as \"isMedicationStop\" , "
				+ " to_char( medHis.start_date," + " 'dd/mm/yyyy' ) as \"startDate\" ,"
				+ " to_char( medHis.updated_date," + " 'dd/mm/yyyy' ) as \"updatedDate\" ,"
				+ " to_char(medHis.end_date," + " 'dd/mm/yyyy' ) as \"endDate\" ,"
				+ " medHis.instruction_id as \"instructionId\" ," + " instr.instruction_desc as \"instructionDesc\" ,"
				+ " medHis.generic_name as \"genericName\"," + " medHis.dose_unit_id as \"doseUnitId\","
				+ " medHis.dose_id as \"doseId\"," + " medHis.order_qty as \"orderQty\" ,"
				+ " medHis.duration as \"duration\", " + " medHis.duration_unit as \"durationUnit\", " + " (case "
				+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
				+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
				+ " else 'Admin' end) as \"userName\", " + " medHis.is_medication_continue as \"isMedicationContinue\","
				+ " medHis.is_no_significant_medication as \"isNoSignificantMedication\","
				+ " medHis.is_medication_stop as \"isMedicationStopHis\",  "
				+ " medHis.justification_stop as \"justificationStopHis\"  " + " from "
				+ " opd.t_medication_history medHis " + " left join public.m_strength_unit su on "
				+ "	medHis.dose_unit_id = su.id " + " left join public.m_item itm on " + "	itm.id = medHis.item_id "
				+ " left join public.m_route_of_administration roa on " + "roa.id = medHis.roa_id "
				+ " left join public.m_generic gen on " + " gen.id  = medHis.generic_id "
				+ " left join public.m_frequency freq on " + "freq.id = medHis.frequency_id "
				+ " left join public.m_instruction instr on " + "	instr.instruction_id = medHis.instruction_id "
				+ " 	left join PUBLIC.m_employee_master empmst on " + " 		medHis.created_by = empmst.user_id "
				+ " 	left join doctor.m_doctor_master docmst on " + " 		medHis.created_by = docmst.user_id "
				+ " where " + "	medHis.unit_id=:unitId " + " and medHis.organization_id=:organizationId "
				+ " and medHis.cov_template_id=:covTemplateId " + " and medHis.status='A' "
				+ " order by medHis.medication_history_id asc "),

@NamedNativeQuery(name="GET_MEDICATION_HISTORY_LIST_FOR_DOCTOR_APP", 
		query=" select "
		+" itm.id as \"itemId\","
		+ " medHis.medication_history_id as \"medicationHistoryId\","
		+" medHis.item_name as \"itemName\" ,"
		+" su.unit as \"doseUnit\","
		+" medHis.frequency_id as \"frequencyId\" ,"
		+" freq.description as \"frequencyDesc\" ,"
		+" medHis.roa_id as \"roaId\" ,"
		+" roa.description as \"roaDesc\" ,"
		+ " medHis.is_medication_stop as \"isMedicationStop\" , "
		+" to_char( medHis.start_date,"
		+" 'dd/mm/yyyy' ) as \"startDate\" ,"
		+" to_char( medHis.updated_date,"
		+" 'dd/mm/yyyy' ) as \"updatedDate\" ,"
		+" to_char(medHis.end_date,"
		+" 'dd/mm/yyyy' ) as \"endDate\" ,"
		+" medHis.instruction_id as \"instructionId\" ,"
		+" instr.instruction_desc as \"instructionDesc\" ,"
		+" medHis.generic_name as \"genericName\","
		+" medHis.dose_unit_id as \"doseUnitId\","
		+" medHis.dose_id as \"doseId\","
		+" medHis.order_qty as \"orderQty\" ,"
		+" medHis.duration as \"duration\", "
		+ " (case "
		+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
		+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
		+ " else 'Admin' end) as \"userName\", "
		+" medHis.is_medication_continue as \"isMedicationContinue\","
		+" medHis.is_no_significant_medication as \"isNoSignificantMedication\","
		+ " medHis.is_medication_stop as \"isMedicationStopHis\",  "
		+ " medHis.justification_stop as \"justificationStopHis\"  "
		+" from "
		+" opd.t_medication_history medHis "
		+" left join public.m_strength_unit su on "
		+"	medHis.dose_unit_id = su.id "
		+" left join public.m_item itm on "
		+"	itm.id = medHis.item_id "
		+" left join public.m_route_of_administration roa on "
		+"roa.id = medHis.roa_id "
		+" left join public.m_generic gen on "
		+" gen.id  = medHis.generic_id "
		+" left join public.m_frequency freq on "
		+"freq.id = medHis.frequency_id "
		+" left join public.m_instruction instr on "
		+"	instr.instruction_id = medHis.instruction_id "
		+ " 	left join PUBLIC.m_employee_master empmst on " 
		+ " 		medHis.created_by = empmst.user_id " 
		+ " 	left join doctor.m_doctor_master docmst on " 
		+ " 		medHis.created_by = docmst.user_id " 
		+" where "
		+"	medHis.unit_id=:unitId "
		+" and medHis.organization_id=:organizationId "
		+" and medHis.patient_id=:patientId "
		+ " order by medHis.is_medication_continue ='Y' desc "									
	),
@NamedNativeQuery(name="GET_CONTINUED_MEDICATION_LIST", 
				query=" select" + 
						" itm.id as \"itemId\"," + 
						" medHis.medication_history_id as \"medicationHistoryId\"," + 
						" medHis.item_name as \"itemName\" ," + 
						" medHis.is_non_formulary as \"isNonFormulary\" ," + 
						" itm.formulation_type_id as \"formulationTypeId\"," + 
						" su.unit as \"doseUnit\"," + 
						" medHis.frequency_id as \"frequencyId\" ," + 
						" freq.description as \"frequencyDesc\" ," + 
						" freq.no_of_administration_time as \"noOfAdministrationTime\" ," + 
						" medHis.roa_id as \"roaId\" ," + 
						" roa.description as \"roaDesc\" ," + 
						" to_char( medHis.start_date," + 
						" 'dd/mm/yyyy' ) as \"startDate\" ," + 
						"  medHis.start_date as \"startDateNew\" ," + 
						" to_char(medHis.end_date," + 
						" 'dd/mm/yyyy' ) as \"endDate\" ," + 
						" medHis.end_date as \"endDateNew\" ," + 
						"   coalesce((select CAST((b.rate) as decimal(10, 2))     " + 
						" from inventory.t_batch b    " + 
						" inner join inventory.t_store_stock S    " + 
						" on B.id = S.batch_id    " + 
						" where b.item_id = itm.id   " + 
						" and s.store_id in (   " + 
						"  select store_id    " + 
						" from m_store_item_mapper msim    " + 
						" where item_id = itm.id and unit_id = :unitId and store_id in    " + 
						" ( select ms.store_id from m_store ms   " + 
						" where ms.unit_id = :unitId and ms.is_pharmacy = true))    " + 
						" and b.unit_id = :unitId   " + 
						" and s.unit_id = :unitId   " + 
						" and to_char(b.expiry_date, 'yyyy-mm-dd') >=:expDate   " + 
						" and b.is_quarantine = false   " + 
						" order by b.id desc limit 1),0) as \"rate\"," +
						" medHis.instruction_id as \"instructionId\" ," + 
						" instr.instruction_desc as \"instructionDesc\" ," + 
						" medHis.generic_name as \"genericName\"," + 
						" medHis.generic_id as \"genericId\"," + 
						" medHis.dose_unit_id as \"doseUnitId\"," + 
						" medHis.dose_id as \"doseId\"," + 
						" medHis.order_qty as \"orderQty\" ," + 
						" medHis.duration as \"duration\", "+
						" medHis.duration as \"durationCount\", "
							+ " medHis.duration_unit as \"durationUnit\", "+
						" medHis.is_medication_continue as \"isMedicationContinue\"," + 
						" medHis.is_medication_stop as \"isMedicationStopHis\", " + 
						"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\", " + 
						" medHis.justification_stop as \"justificationStopHis\", " +
						" itm.strength as \"strengthVal\"," +
						" msu.unit as \"strengthUnit\" " +
						" from" + 
						" opd.t_medication_history medHis" + 
						" left join public.m_strength_unit su on" + 
						"	medHis.dose_unit_id = su.id" + 
						" left join public.m_item itm on" + 
						"	itm.id = medHis.item_id" + 
						" LEFT join public.m_route_of_administration roa on" + 
						" roa.id = medHis.roa_id" + 
						" left join public.m_generic gen on" + 
						" gen.id  = medHis.generic_id" + 
						" inner join public.m_frequency freq on" + 
						" freq.id = medHis.frequency_id" + 
						" left join public.m_instruction instr on" + 
						"	instr.instruction_id = medHis.instruction_id "
						+ " left join doctor.m_doctor_master doc on " + 
						"	medHis.doctor_id = doc.doctor_id " +
						" left join public.m_strength_unit msu on" +
						" 	itm.strength_unit_id = msu.id" +
						" where" + 
						"	medHis.unit_id=:unitId" + 
						" and medHis.organization_id=:organizationId" + 
						" and medHis.patient_id=:patientId" + 
						" and medHis.is_medication_continue='Y'" + 
						" and medHis.is_medication_stop='N'" + 
						" and medHis.status='A' "+
						" order by medHis.medication_history_id asc "									
							),
/*@NamedNativeQuery(name="GET_LIST_OF_MEDICATIONS",  
									query="select  itm.id as \"itemId\" , itm.generic_id as \"genericId\", "
											+ " itm.name as \"itemName\", gen.name as \"genericName\" ,preFreDtl.dose as \"doseDesc\", "
											+ " su.unit as \"strengthDesc\" ,roa.description as \"roaDesc\" "
											+" from public.m_item itm"
											+" inner join public.m_generic gen on itm.generic_id=gen.id"
											+" inner join pharmacy.t_prescription_detail pdl on itm.id=pdl.item_id "
											+" inner join pharmacy.t_prescription pres on pres.id=pdl.prescription_id "
											+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=pdl.id "
											+ " INNER JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
											+ " INNER JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "

											+" where pdl.statflag=true and pdl.is_vaccine=true"
											+" and itm.organization_id=:organizationId"
											+ " and itm.unit_id =:unitId "
											+ " AND pres.patient_id= :patientId"
)  ,*/

@NamedNativeQuery(name="GET_LIST_OF_MEDICATIONS",  
query="select  itm.id as \"itemId\" , itm.generic_id as \"genericId\", " + 
		" itm.name as \"itemName\", gen.name as \"genericName\" ,preFreDtl.dose as \"doseDesc\", " + 
		" su.unit as \"strengthDesc\" ,roa.description as \"roaDesc\" ,  itmMppr.store_id as \"storeId\" "+
		" from public.m_item itm" + 
		" inner join public.m_generic gen on itm.generic_id=gen.id" + 
		" inner join pharmacy.t_prescription_detail pdl on itm.id=pdl.item_id " + 
		" inner join pharmacy.t_prescription pres on pres.id=pdl.prescription_id " + 
		" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=pdl.id " + 
		" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
		" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
		"   inner join public.m_store_item_mapper itmMppr on itmMppr.item_id = itm.id "+
		" where pdl.statflag=true and pdl.is_vaccine=true" + 
		" and itm.organization_id=:organizationId" + 
		" and itm.unit_id =:unitId " + 
		" AND pres.patient_id= :patientId" + 
		" and itmMppr.store_id=:storeId"),

@NamedNativeQuery(name="GET_MEDICATION_HISTORY_LIST_FOR_BOOKVIEW",  
query=" select "
		+" itm.id as \"itemId\","
		+ " medHis.medication_history_id as \"medicationHistoryId\","
		+" medHis.item_name as \"itemName\" ,"
		+" medHis.cov_template_id as \"covTemplateId\" ,"
		+" su.unit as \"doseUnit\","
		+" medHis.frequency_id as \"frequencyId\" ,"
		+" freq.description as \"frequencyDesc\" ,"
		+" medHis.roa_id as \"roaId\" ,"
		+" roa.description as \"roaDesc\" ,"
		+" to_char( medHis.start_date,"
		+" 'dd/mm/yyyy' ) as \"startDate\" ,"
		+" to_char(medHis.end_date,"
		+" 'dd/mm/yyyy' ) as \"endDate\" ,"
		+ "	to_char( medHis.updated_date, 'dd/mm/yyyy') as \"updatedDate\","
		+" medHis.instruction_id as \"instructionId\" ,"
		+" instr.instruction_desc as \"instructionDesc\" ,"
		+" medHis.generic_name as \"genericName\","
		+" medHis.dose_unit_id as \"doseUnitId\","
		+" medHis.dose_id as \"doseId\","
		+" medHis.order_qty as \"orderQty\" ,"
		+" medHis.duration as \"duration\", "
		+" medHis.is_medication_continue as \"isMedicationContinue\","
		+ " medHis.is_medication_stop as \"isMedicationStopHis\",  "
		+" medHis.is_no_significant_medication as \"isNoSignificantMedication\","
		+ " medHis.justification_stop as \"justificationStopHis\"  "
		+" from "
			+" opd.t_medication_history medHis "
		+" left join public.m_strength_unit su on "
		+"	medHis.dose_unit_id = su.id "
		+" left join public.m_item itm on "
		+"	itm.id = medHis.item_id "
		+" left join public.m_route_of_administration roa on "
			+"roa.id = medHis.roa_id "
		+" left join public.m_generic gen on "
			+" gen.id  = medHis.generic_id "
		+" left join public.m_frequency freq on "
			+"freq.id = medHis.frequency_id "
		+" left join public.m_instruction instr on "
		+"	instr.instruction_id = medHis.instruction_id "
		+" where "
		+" medHis.organization_id=:organizationId "
		+" and medHis.patient_id=:patientId "
		+" and medHis.status='A' "
		+ " order by medHis.medication_history_id asc "									
	),
})

@Entity
@Table(name="t_medication_history", schema="opd")
@SequenceGenerator(name = "t_medication_history_seq", sequenceName = "opd.t_medication_history_seq", allocationSize = 1)
public class MedicationHistory {
	@Id
	@Column(name="medication_history_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_medication_history_seq")
	private Integer medicationHistoryId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name="dose_id")
	private Integer doseId;
	
	@Column(name="roa_id")
	private Integer roaId;
	
	@Column(name="frequency_id")
	private Integer frequencyId;
	
	@Column(name="duration")
	private Integer duration;
	
	@Column(name="duration_unit")
	private String durationUnit;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="instruction_id")
	private Integer instructionId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="generic_id")
	private Integer genericId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="dose_unit_id")
	private Integer doseUnitId;
	
	@Column(name="authorised_date")
	private Date authorisedDate;
	
	@Column(name="order_qty")
	private Integer orderQty;
	
	@Column(name="store_id")
	private Integer storeId;
	
	@Column(name="item_name")
	private String itemName;
	
	@Column(name="generic_name")
	private String genericName;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="is_non_formulary")
	private Character isNonFormulary;
	
	@Column(name="is_medication_continue")
	private Character isMedicationContinue;
	
	@Column(name="is_medication_stop")
	private Character isMedicationStopHis;
	
	@Column(name="justification_stop")
	private String justificationStopHis;
	
	@Column(name="is_no_significant_medication")
	private Character isNoSignificantMedication;

	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="item_id", insertable=false, updatable=false)
	private Item itemMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roa_id", insertable=false, updatable=false)
	private RouteOfAdministration routeOfAdministration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="generic_id", insertable=false, updatable=false)
	private Generic generic;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="frequency_id", insertable=false, updatable=false)
	private FrequencyMaster frequencyMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="instruction_id", insertable=false, updatable=false)
	private Instruction instruction;
	
	public Integer getMedicationHistoryId() {
		return medicationHistoryId;
	}

	public void setMedicationHistoryId(Integer medicationHistoryId) {
		this.medicationHistoryId = medicationHistoryId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public Date getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(Date authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Item getItemMaster() {
		return itemMaster;
	}

	public void setItemMaster(Item itemMaster) {
		this.itemMaster = itemMaster;
	}

	public RouteOfAdministration getRouteOfAdministration() {
		return routeOfAdministration;
	}

	public void setRouteOfAdministration(RouteOfAdministration routeOfAdministration) {
		this.routeOfAdministration = routeOfAdministration;
	}

	public Generic getGeneric() {
		return generic;
	}

	public void setGeneric(Generic generic) {
		this.generic = generic;
	}

	public FrequencyMaster getFrequencyMaster() {
		return frequencyMaster;
	}

	public void setFrequencyMaster(FrequencyMaster frequencyMaster) {
		this.frequencyMaster = frequencyMaster;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public Character getIsMedicationContinue() {
		return isMedicationContinue;
	}

	public void setIsMedicationContinue(Character isMedicationContinue) {
		this.isMedicationContinue = isMedicationContinue;
	}

	public Character getIsMedicationStopHis() {
		return isMedicationStopHis;
	}

	public void setIsMedicationStopHis(Character isMedicationStopHis) {
		this.isMedicationStopHis = isMedicationStopHis;
	}

	public String getJustificationStopHis() {
		return justificationStopHis;
	}

	public void setJustificationStopHis(String justificationStopHis) {
		this.justificationStopHis = justificationStopHis;
	}

	public Character getIsNoSignificantMedication() {
		return isNoSignificantMedication;
	}

	public void setIsNoSignificantMedication(Character isNoSignificantMedication) {
		this.isNoSignificantMedication = isNoSignificantMedication;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	

	
}

