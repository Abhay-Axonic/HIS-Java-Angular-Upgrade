package com.param.data.display;

public interface Quries {
	
	String GET_ENCOUNTER_LIST = "select "
			+ " distinct(care_enc.encounter_nr) as \"encounterNo\", "
			+ "  care_enc.pid as \"patientId\", "
			+ " date_format(care_enc.encounter_date,\"%d-%m-%Y\") as \"encounterDate\", "
			+ "  coalesce(care_enc.consulting_dr,\"-\") as \"primaryDoctor\", "
			+ "  coalesce(doct_mst.NAME,\"-\") as \"reviewDoctor\", "
			+ "  coalesce(case when care_enc.insurance_class_nr=3 then \"Self Pay\" end, "
			+ "  case when care_enc.insurance_class_nr=1 then \"Corporate\" end "
			+ "   ,\"-\") as \"paymentType\", "
			+ "    coalesce(case when care_enc.encounter_class_nr=1 then \"In Patient\" end, "
			+ "  case when care_enc.encounter_class_nr=2 then \"Out Patient\" end "
			+ "   ,\"-\") as \"patientType\" "
			+ "from care_encounter care_enc "
			+ "left join doctors_masters doct_mst on care_enc.review_doc_nr = doct_mst.nr "
			+ "where pid = :pid";

	
	String GET_TRIAGE = "select "
			+ "	care_type.name as \"vitalName\", "
			+ "	concat(enc_measure.value,' ',care_type_unit.id) as \"vitalValue\" "
			+ "from "
			+ "	care_encounter_measurement enc_measure "
			+ "inner join care_type_measurement care_type on "
			+ "	enc_measure.msr_type_nr = care_type.nr "
			+ "inner join care_unit_measurement care_type_unit on "
			+ "	enc_measure.unit_nr = care_type_unit.nr "
			+ "where "
			+ "	enc_measure.encounter_nr = :encounterId ";
	
	String GET_HISTORY="select ctn.name as \"name\", "
			+ "cen.notes  as \"notes\" "
			+ "from care_encounter_notes cen "
			+ "inner join care_type_notes ctn on cen.type_nr = ctn.nr "
			+ "where cen.encounter_nr = :encounterId ";
	
	String GET_PRESCRIPTION = "select "
			+ "	prescription.article as \"genericName\", "
			+ "	prescription.color_marker as \"brandName\", "
			+ "	prescription.dosage as \"dosage\", "
			+ "	concat(prescription.duration,' Days') as \"duration\", "
			+ "	concat('Every ',prescription.frequency,' hrs') as \"frequency\", "
			+ "	prescription.notes as \"notes\" "
			+ "from "
			+ "	care_encounter_prescription prescription "
			+ "where "
			+ "	prescription.encounter_nr = :encounterId";
	
	String GET_PHYSICAL_EXAMINATION = "select coalesce(case when cen.type_nr=113 then \"General Exam\" end, "
			+ "  case when cen.type_nr=114 then \"Respiratory System\" end, "
			+ "   case when cen.type_nr=115 then \"Cardiovascular system\" end, "
			+ "    case when cen.type_nr=116 then \"Central Nervous system\" end, "
			+ "     case when cen.type_nr=117 then \"Ear Nose and Throat\" end, "
			+ "   case when cen.type_nr=118 then \"Eyes\" end, "
			+ "   case when cen.type_nr=119 then \"Skin\" end, "
			+ "   case when cen.type_nr=121 then \"Abdomen\" end, "
			+ "  \"-\")  as \"name\", "
			+ "cen.notes  as \"notes\" "
			+ "from care_encounter_notes cen "
			+ "where cen.encounter_nr =  :encounterId "
			+ "and cen.type_nr in (113,114,115,116,118,119,121) ";
	
	
	String GET_LABORATORY = "";
	String GET_RADIOLOGY = "";
	
	String GET_DIAGNOSYS = "select diagnosis.code as \"icdCode\", "
			+ "icd.description as \"discription\" "
			+ "from care_encounter_diagnosis diagnosis "
			+ "inner join care_icd10_en icd on diagnosis.code = icd.diagnosis_code "
			+ "where diagnosis.encounter_nr =:encounterId ";
	

	String GET_REVIEW_NOTES = "select "
			+ "	cen.notes as \"notes\" "
			+ "from "
			+ "	care_encounter_notes cen "
			+ "where "
			+ "	cen.encounter_nr = :encounterId "
			+ "	and cen.type_nr in (4) ";
	
	String GET_PROCEDURES  = "select "
			+ "	cep.code as \"code\", "
			+ "	cep.code_parent as \"name\" "
			+ "from "
			+ "	care_encounter_procedure cep "
			+ "where "
			+ "	cep.encounter_nr = :encounterId " ;
	
	String GET_PROVISIONAL_DIAGNOSYS = "select date_format(cen.`date`,\"%d-%m-%Y\") as \"date\", "
			+ " coalesce(case when cen.type_nr=125 then \"Provisional diagnosis\" end, "
			+ "  \"-\")  as \"name\", "
			+ "cen.notes  as \"notes\" "
			+ "from care_encounter_notes cen "
			+ "where cen.encounter_nr = :encounterId "
			+ "and cen.type_nr in (125) ";

	
}
