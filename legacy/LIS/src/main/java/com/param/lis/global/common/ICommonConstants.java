package com.param.lis.global.common;
public interface ICommonConstants
{
	String hl7Message =
			"MSH|^~\\&|PMS|ClinicA|HIS|HospitalX|20250320123000||ORM^O01|789123|P|2.3.1\r" +
					"PID|1||567890^^^HospitalX^MR||Doe^Jane^A||19900101|F|||123 Main St^^New York^NY^10001||555-1234\r" +
					"PV1|1|O|ClinicA^101^1^HospitalX||||1234^Doe^John^MD||||||||||789123\r" +
					"ORC|NW|ORD12345|LAB54321|||||20250320123000|||1234^Doe^John^MD\r" +
					"OBR|1|ORD12345|LAB54321|CBC^Complete Blood Count^L|||20250320130000||||||1234^Doe^John^MD";
	String SUCCESS = "success";
	String ERROR = "error";

	boolean TRUE = true;
	boolean FALSE = false;

	char ACTIVE = 'A';
	char INACTIVE = 'I';
	

	char YES = 'Y';
	char NO = 'N';

	Object NULL = null;
	
	/*countryMaster*/
	String MASTER_SUCCESS = " Record added successfully.";

	/* MESSAGE ON JSP */
	String COMMON_SUCCESS = "Added successfully.";
	String COMMON_DELETE = "Deleted successfully.";
	String COMMON_UPDATE = "Updated successfully.";
	String ALREADY_EXIST = "Already Exists.";
	String COMMON_ERROR = "Fail";

	/*
	 * / MESSAGE FOR NON REGISTERED PATIENTS /
	 */
	String REGISTERED_CODE = "100";
	String NOT_REGISTERED_CODE = "101";

	/*String SAMPLE_ADD_SUCC = "Sample added successfully.";*/
	String SAMPLE_ADD_SUCC = "Record added successfully.";
	String SAMPLE_ADD_FAIL = "Failed to add sample.";
	String SAMPLE_UPDATE_SUCC = "Sample updated successfully.";
	String SAMPLE_UPDATE_FAIL = "Failed to update sample.";
	String SAMPLE_ACTIVATE_SUCC = "Sample activated successfully.";
	String SAMPLE_ACTIVATE_FAIL = "Failed to activate sample.";
	String SAMPLE_INACTIVATE_SUCC = "Sample inactivated successfully.";
	String SAMPLE_INACTIVATE_FAIL = "Failed to activate Sample.";
	String SAMPLE_NAME_EXISIS = "Sample name is already exists.";
	String SAMPLE_CODE_EXISIS = "Sample code is already exists.";
	String SAMPLE_DESC_EXISIS = "Sample description is already exists.";
	String SAMPLE_NOT_FOUND = "Sample not found.";
	String SAMPLE_RECORDS_NOT_FOUND = "Sample Records not found.";

	// Container Master

	/*String CONTAINER_ADD_SUCC = "Container added successfully.";*/
	String CONTAINER_ADD_SUCC = "Record added successfully.";
	String CONTAINER_ADD_FAIL = "Failed to add Container.";
	String CONTAINER_UPDATE_SUCC = "Container updated successfully.";
	String CONTAINER_UPDATE_FAIL = "Failed to update Container.";
	String CONTAINER_ACTIVATE_SUCC = "Container activated successfully.";
	String CONTAINER_ACTIVATE_FAIL = "Failed to activate Container.";
	String CONTAINER_INACTIVATE_SUCC = "Container inactivated successfully.";
	String CONTAINER_INACTIVATE_FAIL = "Failed to activate Container.";
	String CONTAINER_NAME_EXISIS = "Container name is already exists.";
	String CONTAINER_CODE_EXISIS = "Container code is already exists.";
	String CONTAINER_DESC_EXISIS = "Container discription is already exists.";
	String CONTAINER_NOT_FOUND = "Container not found.";
	String CONTAINER_RECORDS_NOT_FOUND = "Container Records not found.";

	// Techinque Master

	/*String TECHINQUE_ADD_SUCC = "Techinque added successfully.";*/
	String TECHINQUE_ADD_SUCC = "Record added successfully.";
	String TECHINQUE_ADD_FAIL = "Failed to add Techinque.";
	String TECHINQUE_UPDATE_SUCC = "Techinque updated successfully.";
	String TECHINQUE_UPDATE_FAIL = "Failed to add Techinque.";
	String TECHINQUE_ACTIVATE_SUCC = "Techinque activated successfully.";
	String TECHINQUE_ACTIVATE_FAIL = "Failed to activate Techinque.";
	String TECHINQUE_INACTIVATE_SUCC = "Techinque inactivated successfully.";
	String TECHINQUE_INACTIVATE_FAIL = "Failed to activate Techinque.";
	String TECHINQUE_NAME_EXISIS = "Techinque name is already exists.";
	String TECHINQUE_CODE_EXISIS = "Techinque code is already exists.";
	String TECHINQUE_DESC_EXISIS = "Techinque description is already exists.";
	String TECHINQUE_NOT_FOUND = "Techinque not found.";
	String TECHINQUE_RECORDS_NOT_FOUND = "Techinque Records not found.";

	// Prerequisites Master

	/*String PREREQUISITES_ADD_SUCC = "Prerequisites added successfully.";*/
	String PREREQUISITES_ADD_SUCC = "Record added successfully.";
	String PREREQUISITES_ADD_FAIL = "Failed to add Prerequisites.";
	String PREREQUISITES_UPDATE_SUCC = "Prerequisites updated successfully.";
	String PREREQUISITES_UPDATE_FAIL = "Failed to add Prerequisites.";
	String PREREQUISITES_ACTIVATE_SUCC = "Prerequisites activated successfully.";
	String PREREQUISITES_ACTIVATE_FAIL = "Failed to activate Prerequisites.";
	String PREREQUISITES_INACTIVATE_SUCC = "Prerequisites inactivated successfully.";
	String PREREQUISITES_INACTIVATE_FAIL = "Failed to activate Prerequisites.";
	String PREREQUISITES_NAME_EXISIS = "Prerequisites name is already exists.";
	String PREREQUISITES_CODE_EXISIS = "Prerequisites code is already exists.";
	String PREREQUISITES_DESC_EXISIS = "Prerequisites description is already exists.";
	String PREREQUISITES_NOT_FOUND = "Prerequisites not found.";
	String PREREQUISITES_RECORDS_NOT_FOUND = "Prerequisites Records not found.";

	// Media Master

	/*String MEDIA_ADD_SUCC = "Media added successfully.";*/
	String MEDIA_ADD_SUCC = "Record added successfully.";
	String MEDIA_ADD_FAIL = "Failed to add Media.";
	String MEDIA_UPDATE_SUCC = "Media updated successfully.";
	String MEDIA_UPDATE_FAIL = "Failed to add Media.";
	String MEDIA_ACTIVATE_SUCC = "Media activated successfully.";
	String MEDIA_ACTIVATE_FAIL = "Failed to activate Media.";
	String MEDIA_INACTIVATE_SUCC = "Media inactivated successfully.";
	String MEDIA_INACTIVATE_FAIL = "Failed to activate Media.";
	String MEDIA_NAME_EXISIS = "Media name is already exists.";
	String MEDIA_CODE_EXISIS = "Media code is already exists.";
	String MEDIA_DESCRIPTION_EXISIS = "Media Description is already exists.";
	String MEDIA_NOT_FOUND = "Media not found.";
	String MEDIA_RECORDS_NOT_FOUND = "Media Records not found.";

	// Unit Master

	/*String UNIT_ADD_SUCC = "Unit added successfully.";*/
	String UNIT_ADD_SUCC = "Record added successfully.";
	String UNIT_ADD_FAIL = " Failed to add Unit.";
	String UNIT_UPDATE_SUCC = "Unit updated successfully.";
	String UNIT_UPDATE_FAIL = "Failed to add Unit.";
	String UNIT_ACTIVATE_SUCC = "Unit activated successfully.";
	String UNIT_ACTIVATE_FAIL = "Failed to activate Unit.";
	String UNIT_INACTIVATE_SUCC = "Unit inactivated successfully.";
	String UNIT_INACTIVATE_FAIL = "Failed to activate Unit.";
	String UNIT_NAME_EXISIS = "Unit name is already exists.";
	String UNIT_CODE_EXISIS = "Unit code is already exists.";
	String UNIT_DESC_EXISIS = "Unit description is already exists.";
	String UNIT_NOT_FOUND = "Unit not found.";
	String UNIT_RECORDS_NOT_FOUND = "Unit Records not found.";

	// reagent Master

	/*String REAGENT_ADD_SUCC = "Reagent added successfully.";*/
	String REAGENT_ADD_SUCC = "Record added successfully.";
	String REAGENT_ADD_FAIL = " Failed to add Reagent.";
	String REAGENT_UPDATE_SUCC = "Reagent updated successfully.";
	String REAGENT_UPDATE_FAIL = "Failed to add Reagent.";
	String REAGENT_ACTIVATE_SUCC = "Reagent activated successfully.";
	String REAGENT_ACTIVATE_FAIL = "Failed to activate Reagent.";
	String REAGENT_INACTIVATE_SUCC = "Reagent inactivated successfully.";
	String REAGENT_INACTIVATE_FAIL = "Failed to activate Reagent.";
	String REAGENT_NAME_EXISIS = "Reagent name is already exists.";
	String REAGENT_CODE_EXISIS = "Reagent code is already exists.";
	String REAGENT_DESCRIPTION_EXISIS = "Reagent Description is already exists.";
	String REAGENT_NOT_FOUND = "Reagent not found.";
	String REAGENT_RECORDS_NOT_FOUND = "Reagent Records not found.";

	// reportType Master

	/*String REPORT_TYPR_ADD_SUCC = "Report Type added successfully.";*/
	String REPORT_TYPR_ADD_SUCC = "Record added successfully.";
	String REPORT_TYPRADD_FAIL = "Failed to add Report Type.";
	String REPORT_TYPRUPDATE_SUCC = "Report Type updated successfully.";
	String REPORT_TYPRUPDATE_FAIL = "Failed to add Report Type.";
	String REPORT_TYPRACTIVATE_SUCC = "Report Type activated successfully.";
	String REPORT_TYPRACTIVATE_FAIL = "Failed to activate Report Type.";
	String REPORT_TYPRINACTIVATE_SUCC = "Report Type inactivated successfully.";
	String REPORT_TYPRINACTIVATE_FAIL = "Failed to activate Report Type.";
	String REPORT_TYPRNAME_EXISIS = "Report Type name is already exists.";
	String REPORT_TYPRCODE_EXISIS = "Report Type name or Report Type code is already exists.";
	String REPORT_TYPRNOT_FOUND = "Report Type not found.";
	String REPORT_TYPRRECORDS_NOT_FOUND = "Report Type Records not found.";

	// Bacteria Master

	/*String BACTERIA_ADD_SUCC = "Bacteria added successfully.";*/
	String BACTERIA_ADD_SUCC = "Record added successfully.";
	String BACTERIA_ADD_FAIL = "Failed to add Bacteria.";
	String BACTERIA_UPDATE_SUCC = "Bacteria updated successfully.";
	String BACTERIA_UPDATE_FAIL = "Failed to add Bacteria.";
	String BACTERIA_ACTIVATE_SUCC = "Bacteria activated successfully.";
	String BACTERIA_ACTIVATE_FAIL = "Failed to activate Bacteria.";
	String BACTERIA_INACTIVATE_SUCC = "Bacteria inactivated successfully.";
	String BACTERIA_INACTIVATE_FAIL = "Failed to activate Bacteria.";
	String BACTERIA_NAME_EXISIS = "Bacteria name is already exists.";
	String BACTERIA_CODE_EXISIS = "Bacteria name or Bacteria code is already exists.";
	String BACTERIA_NOT_FOUND = "Bacteria not found.";
	String BACTERIA_CORDS_NOT_FOUND = "Bacteria Records not found.";

	// Antibiotics Master

	/*String ANTIBIOTICS_ADD_SUCC = "Antibiotics added successfully.";*/
	String ANTIBIOTICS_ADD_SUCC = "Record added successfully.";
	String ANTIBIOTICS_ADD_FAIL = "Failed  to add Antibiotics.";
	String ANTIBIOTICS_UPDATE_SUCC = "Antibiotics updated successfully.";
	String ANTIBIOTICS_UPDATE_FAIL = "Failed to add Antibiotics.";
	String ANTIBIOTICS_ACTIVATE_SUCC = "Antibiotics activated successfully.";
	String ANTIBIOTICS_ACTIVATE_FAIL = "Failed to activate Antibiotics.";
	String ANTIBIOTICS_INACTIVATE_SUCC = "Antibiotics inactivated successfully.";
	String ANTIBIOTICS_INACTIVATE_FAIL = "Failed to activate Antibiotics.";
	String ANTIBIOTICS_NAME_EXISIS = "Antibiotics name is already exists.";
	String ANTIBIOTICS_CODE_EXISIS = "Antibiotics code is already exists.";
	String ANTIBIOTICS_DESCRIPTION_EXISIS = "Antibiotics Description is already exists.";
	String ANTIBIOTICS_NOT_FOUND = "Antibiotics not found.";
	String ANTIBIOTICS__CORDS_NOT_FOUND = "Antibiotics Records not found.";

	// Colony Master

	/*String COLONY_ADD_SUCC = "Colony added successfully.";*/
	String COLONY_ADD_SUCC = "Record added successfully.";
	String COLONY_ADD_FAIL = "Failed  to add Colony.";
	String COLONY_UPDATE_SUCC = "Colony updated successfully.";
	String COLONY_UPDATE_FAIL = "Failed to add Colony.";
	String COLONY_ACTIVATE_SUCC = "Colony activated successfully.";
	String COLONY_ACTIVATE_FAIL = "Failed to activate Colony.";
	String COLONY_INACTIVATE_SUCC = "Colony inactivated successfully.";
	String COLONY_INACTIVATE_FAIL = "Failed to activate Colony.";
	String COLONY_NAME_EXISIS = "Colony name is already exists.";
	String COLONY_CODE_EXISIS = "Colony code is already exists.";
	String COLONY_DESCRIPTION_EXISIS = "Colony Description is already exists.";
	String COLONY_NOT_FOUND = "Colony not found.";
	String COLONY__CORDS_NOT_FOUND = "Colony Records not found.";

	// SlideProcedure Master

	/*String SLIDEPROCEDURE_ADD_SUCC = "Slide Procedure added successfully.";*/
	String SLIDEPROCEDURE_ADD_SUCC = "Record added successfully.";
	String SLIDEPROCEDURE_ADD_FAIL = "Failed  to add Slide Procedure.";
	String SLIDEPROCEDURE_UPDATE_SUCC = "Slide Procedure updated successfully.";
	String SLIDEPROCEDURE_UPDATE_FAIL = "Failed to add Slide Procedure.";
	String SLIDEPROCEDURE_ACTIVATE_SUCC = "Slide Procedure activated successfully.";
	String SLIDEPROCEDURE_ACTIVATE_FAIL = "Failed to activate Slide Procedure.";
	String SLIDEPROCEDURE_INACTIVATE_SUCC = "Slide Procedure inactivated successfully.";
	String SLIDEPROCEDURE_INACTIVATE_FAIL = "Failed to activate Slide Procedure.";
	String SLIDEPROCEDURE_NAME_EXISIS = "Slide Procedure name is already exists.";
	String SLIDEPROCEDURE_CODE_EXISIS = "Slide Procedure code is already exists.";
	String SLIDEPROCEDURE_NOT_FOUND = "Slide Procedure not found.";
	String SLIDEPROCEDURE__CORDS_NOT_FOUND = "Slide Procedure Records not found.";

	/** LIS TRANSACTION */
	
	String REPORT_RELEASE_SUCC = "Report Release Successfully.";
    String REPORT_RELEASE_FAIL = "Fail To Release Report.";
	
	String SAMPLE_RESULT_ENTRY_SUCC = "Sample Result Entry Done Successfully.";
	String SAMPLE_RESULT_ENTRY_FAIL = "Fail To Done Sample Result Entry.";
	
	String SAMPLE_RESULT_ENTRY_VALIDATION_SUCC = "Result Entry Validated Successfully.";
	String SAMPLE_RESULT_ENTRY_VALIDATION_FAIL = "Failed To Validate Result Entry.";

	String SAMPLE_COLLECT_SUCC = "Sample Collected Successfully.";
	String SAMPLE_COLLECT_FAIL = "Failed To Collect Sample.";
	
	String SAMPLE_RECOLLECT_SUCC = "Sample Recollected Successfully.";
	String SAMPLE_RECOLLECT_FAIL = "Failed To Recollect Sample.";

	String SAMPLE_SENDTOCR_SUCC = "Sample Send to Central Receiving Successfully.";
	String SAMPLE_SENDTOCR_FAIL = "Failed To Send Sample to Central Receiving.";

	String SAMPLE_ACCEPT_SUCC = "Sample Processed Successfully.";
	String SAMPLE_ACCEPT_FAIL = "Failed To Processed Sample.";

	String SAMPLE_REJECT_SUCC = "Sample Rejected Successfully.";
	String SAMPLE_REJECT_FAIL = "Failed to Reject Sample.";
	
	String SAMPLE_SEND_TO_DEPT_SUCC = "Sample Send to department successfully.";
	String SAMPLE_SEND_TO_DEPT_FAIL = "Failed To Send Sample to Department.";
	
	String SAMPLE_SEND_TO_RES_ENTRY_SUCC = "Sample Send To Report Entry Successfully.";
	String SAMPLE_SEND_TO_RES_ENTRY_FAIL ="Fail To Send Sample To Report Entry.";
	
	String SAMPLE_RETEST_SUCC = "Sample Send To Retesting Successfully.";
	String SAMPLE_RETEST_FAIL ="Fail To Send Sample To Retest.";
	
	String SAMPLE_SEND_RECOLLECT_SUCC = "Sample Send To Sample Recollection Successfully.";
	String SAMPLE_SEND_RECOLLECT_FAIL ="Fail To Send Worklist To Retest.";
	
	
	/**MicroBiology Transaction*/
	String SAMPLE_SEND_TO_INCUBATION_OBJSERVATION_SUCC = "Sample Send To Incubation Observation.";
    String SAMPLE_SEND_TO_INCUBATION_OBJSERVATION_FAIL ="Fail To Send Sample For Incubation Observation.";

	// WtfilmStudyMasterService Master

	/*String WTIFILMSTUDY_ADD_SUCC = "Wet film Study added successfully.";*/
    String WTIFILMSTUDY_ADD_SUCC = "Record added successfully.";
	String WTIFILMSTUDY_ADD_FAIL = "Failed  to add Wet film Study .";
	String WTIFILMSTUDY_UPDATE_SUCC = "Wet film Study updated successfully.";
	String WTIFILMSTUDY_UPDATE_FAIL = "Failed to add Wet film Study.";
	String WTIFILMSTUDY_ACTIVATE_SUCC = "Wet film Study activated successfully.";
	String WTIFILMSTUDY_ACTIVATE_FAIL = "Failed to Activate Wet film Study.";
	String WTIFILMSTUDY_INACTIVATE_SUCC = "Wet film Study inactivated successfully.";
	String WTIFILMSTUDY_INACTIVATE_FAIL = "Failed to Activate Wet film Study.";
	String WTIFILMSTUDY_NAME_EXISIS = "Wet film Study name is already exists.";
	String WTIFILMSTUDY_CODE_EXISIS = "Wet film Study code is already exists.";
	String WTIFILMSTUDY_DESCRIPTION_EXISIS = "Wet film Study Description is already exists.";
	String WTIFILMSTUDY_NOT_FOUND = "Wet film Study not found.";
	String WTIFILMSTUDY__CORDS_NOT_FOUND = "Wet film Study Records not found.";

	// SpecimanMaster Master

	/*String SPECIMAN_ADD_SUCC = "Specimen added successfully.";*/
	String SPECIMAN_ADD_SUCC = "Record added successfully.";
	String SPECIMAN_ADD_FAIL = "Failed  to add Specimen.";
	String SPECIMAN_UPDATE_SUCC = "Specimen updated successfully.";
	String SPECIMAN_UPDATE_FAIL = "Failed to add Specimen.";
	String SPECIMAN_ACTIVATE_SUCC = "Specimen activated successfully.";
	String SPECIMAN_ACTIVATE_FAIL = "Failed to activate Specimen.";
	String SPECIMAN_INACTIVATE_SUCC = "Specimen inactivated successfully.";
	String SPECIMAN_INACTIVATE_FAIL = "Failed to activate Specimen.";
	String SPECIMAN_NAME_EXISIS = "Specimen name is already exists.";
	String SPECIMAN_CODE_EXISIS = "Specimen code is already exists.";
	String SPECIMAN_NOT_FOUND = "Specimen not found.";
	String SPECIMAN__CORDS_NOT_FOUND = "Specimen Records not found.";
	String SPECIMAN_BLOCK_SUCC = "Specimen block activated successfully.";
	String SPECIMAN_BLOCK_FAIL = "Failed to activate Specimen block.";
	String SPECIMAN_GROSS_SUCC = "Specimen gross activated successfully.";
	String SPECIMAN_GROSS_FAIL = "Failed to activate Specimen gross.";

	// SensitivityTestResult Master

	/*String SENSITIVITY_ADD_SUCC = "Sensitivity Test Result added successfully.";*/
	String SENSITIVITY_ADD_SUCC = "Record added successfully.";
	String SENSITIVITY_ADD_FAIL = "Failed  to add Sensitivity Test Result.";
	String SENSITIVITY_UPDATE_SUCC = "Sensitivity Test Result updated successfully.";
	String SENSITIVITY_UPDATE_FAIL = "Failed to add Sensitivity Test Result.";
	String SENSITIVITY_ACTIVATE_SUCC = "Sensitivity Test Result activated successfully.";
	String SENSITIVITY_ACTIVATE_FAIL = "Failed to activate Sensitivity Test Result.";
	String SENSITIVITY_INACTIVATE_SUCC = "Sensitivity Test Result inactivated successfully.";
	String SENSITIVITY_INACTIVATE_FAIL = "Failed to activate Sensitivity Test Result.";
	String SENSITIVITY_NAME_EXISIS = "Sensitivity Test Result name is already exists.";
	String SENSITIVITY_CODE_EXISIS = "Sensitivity Test Result code is already exists.";
	String SENSITIVITY_NOT_FOUND = "Sensitivity Test Result not found.";
	String SENSITIVITY__CORDS_NOT_FOUND = "Sensitivity Test Result Records not found.";

	// Incubation test Master

	/*String INCUBATION_TEST_MASTER_ADD_SUCC = "Incubation test added successfully.";*/
	String INCUBATION_TEST_MASTER_ADD_SUCC = "Record added successfully.";
	String INCUBATION_TEST_MASTER_ADD_FAIL = "Failed  to add Incubation test.";
	String INCUBATION_TEST_MASTER_UPDATE_SUCC = "Incubation test updated successfully.";
	String INCUBATION_TEST_MASTER_UPDATE_FAIL = "Failed to add Incubation test.";
	String INCUBATION_TEST_MASTER_ACTIVATE_SUCC = "Incubation test activated successfully.";
	String INCUBATION_TEST_MASTER_ACTIVATE_FAIL = "Failed to activate Incubation test.";
	String INCUBATION_TEST_MASTER_INACTIVATE_SUCC = "Incubation test inactivated successfully.";
	String INCUBATION_TEST_MASTER_INACTIVATE_FAIL = "Failed to activate Incubation test.";
	String INCUBATION_TEST_MASTER_NAME_EXISIS = "Incubation test name is already exists.";
	String INCUBATION_TEST_MASTER_CODE_EXISIS = "Incubation test code is already exists.";
	String INCUBATION_TEST_MASTER_NOT_FOUND = "Incubation test not found.";
	String INCUBATION_TEST_MASTER__CORDS_NOT_FOUND = "Incubation test Records not found.";

	// Block Master

	/*String BLOCK_ADD_SUCC = "Block added successfully.";*/
	String BLOCK_ADD_SUCC = "Record added successfully.";
	String BLOCK_ADD_FAIL = "Failed to add Block.";
	String BLOCK_UPDATE_SUCC = "Block updated successfully.";
	String BLOCK_UPDATE_FAIL = "Failed to add Block.";
	String BLOCK_ACTIVATE_SUCC = "Block activated successfully.";
	String BLOCK_ACTIVATE_FAIL = "Failed to activate Block.";
	String BLOCK_INACTIVATE_SUCC = "Block inactivated successfully.";
	String BLOCK_INACTIVATE_FAIL = "Failed to activate Block.";
	String BLOCK_NAME_EXISIS = "Block name is already exists.";
	String BLOCK_CODE_EXISIS = "Block code is already exists.";
	String BLOCK_NOT_FOUND = "Block not found.";
	String BLOCK_CORDS_NOT_FOUND = "Block Records not found.";

	// Antibiotics group Master

	/*String ANTIBIOTICS_GROUP_ADD_SUCC = "Antibiotics group added successfully.";*/
	String ANTIBIOTICS_GROUP_ADD_SUCC = "Record added successfully.";
	String ANTIBIOTICS_GROUPADD_FAIL = "Failed  to add Antibiotics group.";
	String ANTIBIOTICS_GROUPUPDATE_SUCC = "Antibiotics group updated successfully.";
	String ANTIBIOTICS_GROUPUPDATE_FAIL = "Failed to add Antibiotics group.";
	String ANTIBIOTICS_GROUPACTIVATE_SUCC = "Antibiotics group activated successfully.";
	String ANTIBIOTICS_GROUPACTIVATE_FAIL = "Failed to activate Antibiotics group.";
	String ANTIBIOTICS_GROUPINACTIVATE_SUCC = "Antibiotics inactivated successfully.";
	String ANTIBIOTICS_GROUPINACTIVATE_FAIL = "Failed to activate Antibiotics group.";
	String ANTIBIOTICS_GROUPNAME_EXISIS = "Antibiotics group name is already exists.";
	String ANTIBIOTICS_GROUPCODE_EXISIS = "Antibiotics group code is already exists.";
	String ANTIBIOTICS_GROUPDESCRIPTION_EXISIS = "Antibiotics group Description is already exists.";
	String ANTIBIOTICS_GROUPNOT_FOUND = "Antibiotics group not found.";
	String ANTIBIOTICS_GROUP_CORDS_NOT_FOUND = "Antibiotics group Records not found.";

	// Antibiotic Master

	/*String ANTIBIOTIC_ADD_SUCC = "Antibiotic added successfully.";*/
	String ANTIBIOTIC_ADD_SUCC = "Record added successfully.";
	String ANTIBIOTIC_ADD_FAIL = "Failed  to add Antibiotic.";
	String ANTIBIOTIC_UPDATE_SUCC = "Antibiotic group updated successfully.";
	String ANTIBIOTIC_UPDATE_FAIL = "Failed to add Antibiotic.";
	String ANTIBIOTIC_ACTIVATE_SUCC = "Antibiotics group activated successfully.";
	String ANTIBIOTIC_ACTIVATE_FAIL = "Failed to activate Antibiotic.";
	String ANTIBIOTIC_INACTIVATE_SUCC = "Antibiotics inactivated successfully.";
	String ANTIBIOTIC_INACTIVATE_FAIL = "Failed to activate Antibiotic.";
	String ANTIBIOTIC_NAME_EXISIS = "Antibiotic name is already exists.";
	String ANTIBIOTIC_CODE_EXISIS = "Antibiotic code is already exists.";
	String ANTIBIOTIC_NOT_FOUND = "Antibiotic not found.";
	String ANTIBIOTIC__CORDS_NOT_FOUND = "Antibiotic Records not found.";
	
	/* 
	 * 
	 * Antibiotic Addition Master
	 */
	String   ANTIBIOTICS_ADDITION_ADD_SUCC = "Record added successfully.";
	String  ANTIBIOTICS_ADDITION_ADD_FAIL = "Failed  to add Antibiotic Addition group.";
	String  ANTIBIOTICS_ADDITION_UPDATE_SUCC = "Antibiotic Addition group updated successfully.";
	String  ANTIBIOTICS_ADDITION_UPDATE_FAIL = "Failed to add Antibiotic Addition group.";
	String  ANTIBIOTICS_ADDITION_ACTIVATE_SUCC = "Antibiotic Addition group activated successfully.";
	String  ANTIBIOTICS_ADDITION_ACTIVATE_FAIL = "Failed to activate Antibiotic Addition group.";
	String  ANTIBIOTICS_ADDITION_INACTIVATE_SUCC = "Antibiotic Addition inactivated successfully.";
	String  ANTIBIOTICS_ADDITION_INACTIVATE_FAIL = "Failed to activate Antibiotic Addition group.";
	String  ANTIBIOTICS_ADDITION_NAME_EXISIS = "Antibiotic Addition group name is already exists.";
	String  ANTIBIOTICS_ADDITION_CODE_EXISIS = "Antibiotic Addition group code is already exists.";
	String  ANTIBIOTICS_ADDITION = "Antibiotic Addition group not found.";
	String  ANTIBIOTICS_ADDITION_CORDS_NOT_FOUND = "Antibiotic Addition group Records not found.";
	
	
	/* 
	 * ANTIBIOTICS ORGANISM MAPPER Addition Master
	 * 
	 */
	String   ANTIBIOTICS_ORGANISM_ADD_SUCC = "Antibiotic Organism group added successfully.";
	String  ANTIBIOTICS_ORGANISM_ADD_FAIL = "Failed  to add Antibiotic Organism group.";
	String  ANTIBIOTICS_ORGANISM_UPDATE_SUCC = "Antibiotic Organism group updated successfully.";
	String  ANTIBIOTICS_ORGANISM_UPDATE_FAIL = "Failed to add Antibiotic Organism group.";
	String  ANTIBIOTICS_ORGANISM_ACTIVATE_SUCC = "Antibiotic Organism group activated successfully.";
	String  ANTIBIOTICS_ORGANISM_ACTIVATE_FAIL = "Failed to activate Antibiotic Organism group.";
	String  ANTIBIOTICS_ORGANISM_INACTIVATE_SUCC = "Antibiotic Organism inactivated successfully.";
	String  ANTIBIOTICS_ORGANISM_INACTIVATE_FAIL = "Failed to activate Antibiotic Organism group.";
	String  ANTIBIOTICS_ORGANISM_NAME_EXISIS = "Antibiotic Organism group name is already exists.";
	String  ANTIBIOTICS_ORGANISM_CODE_EXISIS = "Antibiotic Organism group code is already exists.";
	String  ANTIBIOTICS_ORGANISM = "Antibiotic Organismgroup not found.";
	String  ANTIBIOTICS_ORGANISM_CORDS_NOT_FOUND = "Antibiotic Organism group Records not found.";
	
	
	
	
	/* 
	 * Media Colony Mapper Master
	 * 
	 */
	String  MEDIA_COLONY_MAPPER_ADD_SUCC = " REcord added successfully.";
	String  MEDIA_COLONY_MAPPER_ADD_FAIL = "Failed  to add  Media Colony Mapper group.";
	String  MEDIA_COLONY_MAPPER_UPDATE_SUCC = " Media Colony Mapper group updated successfully.";
	String  MEDIA_COLONY_MAPPER_UPDATE_FAIL = "Failed to add  Media Colony Mapper group.";
	String  MEDIA_COLONY_MAPPER_ACTIVATE_SUCC = " Media Colony Mapper group activated successfully.";
	String  MEDIA_COLONY_MAPPER_ACTIVATE_FAIL = "Failed to activate  Media Colony Mapper group.";
	String  MEDIA_COLONY_MAPPER_INACTIVATE_SUCC = " Media Colony Mapper inactivated successfully.";
	String  MEDIA_COLONY_MAPPER_INACTIVATE_FAIL = "Failed to activate  Media Colony Mapper group.";
	String  MEDIA_COLONY_MAPPER_NAME_EXISIS = " Media Colony Mapper group name is already exists.";
	String  MEDIA_COLONY_MAPPER_CODE_EXISIS = " Media Colony Mapper group code is already exists.";
	String  MEDIA_COLONY_MAPPER = " Media Colony Mapper group not found.";
	String  MEDIA_COLONY_MAPPER_CORDS_NOT_FOUND = " Media Colony Mapper group Records not found.";
	
	
	
	/* 
	 * Media OrganismGroupOrganismMapper Master
	 * 
	 */
	String  ORGANIGROUP_ORGANI_MAPPER_ADD_SUCC = "Record added successfully.";
	String  ORGANIGROUP_ORGANI_MAPPER_ADD_FAIL = "Failed  to add  Organism Group Mapping With Organism.";
	String  ORGANIGROUP_ORGANI_MAPPER_UPDATE_SUCC = " Organism Group Mapping With Organism updated successfully.";
	String  ORGANIGROUP_ORGANI_MAPPER_UPDATE_FAIL = "Failed to add  Organism Group Mapping With Organism.";
	String  ORGANIGROUP_ORGANI_MAPPER_ACTIVATE_SUCC = "Organism Group Mapping With Organism activated successfully.";
	String  ORGANIGROUP_ORGANI_MAPPER_ACTIVATE_FAIL = "Failed to activate  Organism Group Mapping With Organism.";
	String  ORGANIGROUP_ORGANI_MAPPER_INACTIVATE_SUCC = " Organism Group Mapping With Organism inactivated successfully.";
	String  ORGANIGROUP_ORGANI_MAPPER_INACTIVATE_FAIL = "Failed to activate  Organism Group Mapping With Organism.";
	String  ORGANIGROUP_ORGANI_MAPPER_NAME_EXISIS = " Organism Group Mapping With Organism name is already exists.";
	String  ORGANIGROUP_ORGANI_MAPPER_CODE_EXISIS = " Organism Group Mapping With Organism code is already exists.";
	String  ORGANIGROUP_ORGANI_MAPPER = " Organism Group Mapping With Organism not found.";
	String  ORGANIGROUP_ORGANI_MAPPER_CORDS_NOT_FOUND = " Organism Group Mapping With Organism Records not found.";

	// Hour Master

	String HOUR_GROUP_ADD_SUCC = "Record added successfully.";
	String HOUR_GROUPADD_FAIL = "Failed to add Hour.";
	String HOUR__RECORDS_NOT_FOUND = "Hour Records not found.";

	// Incubation Period Master

	String INCUBATION_PERIOD_MASTER_ADD_SUCC = "Record added successfully.";
	String INCUBATION_PERIOD_MASTER_ADD_FAIL = "Failed  to add Incubation Period.";
	String INCUBATION_PERIOD_MASTER_UPDATE_SUCC = "Incubation Period updated successfully.";
	String INCUBATION_PERIOD_MASTER_UPDATE_FAIL = "Failed to add Incubation Period.";
	String INCUBATION_PERIOD_MASTER_ACTIVATE_SUCC = "Incubation Period activated successfully.";
	String INCUBATION_PERIOD_MASTER_ACTIVATE_FAIL = "Failed to activate Incubation Period.";
	String INCUBATION_PERIOD_MASTER_INACTIVATE_SUCC = "Incubation Period inactivated successfully.";
	String INCUBATION_PERIOD_MASTER_INACTIVATE_FAIL = "Failed to activate Incubation Period.";
	String INCUBATION_PERIOD_MASTER_NAME_EXISIS = "Incubation Period name is already exists.";
	String INCUBATION_PERIOD_MASTER_CODE_EXISIS = "Incubation Period code is already exists.";
	String INCUBATION_PERIOD_MASTER_TIME_EXISIS = "Incubation Time is already exists.";
	String INCUBATION_PERIOD_MASTER_NOT_FOUND = "Incubation Period not found.";
	String INCUBATION_PERIOD_MASTER__CORDS_NOT_FOUND = "Incubation Period Records not found.";

	// Sample type media mapping Master

	String SAMPLE_TYPE_ADD_SUCC = "Record added successfully.";
	String SAMPLE_TYPE_ADD_FAIL = "Failed to add Sample type media mapping.";
	String SAMPLE_TYPE_USAMPLE_TYPE_SUCC = "Sample type media mapping updated successfully.";
	String SAMPLE_TYPE_UPDATE_FAIL = "Failed to update Sample type media mapping.";
	String SAMPLE_TYPE_ACTIVATE_SUCC = "Sample type media mapping activated successfully.";
	String SAMPLE_TYPE_ACTIVATE_FAIL = "Failed to activate Sample type media mapping.";
	String SAMPLE_TYPE_INACTIVATE_SUCC = "Sample type media mapping inactivated successfully.";
	String SAMPLE_TYPE_INACTIVATE_FAIL = "Failed to activate Sample type media mapping.";
	String SAMPLE_TYPE_NAME_EXISIS = "Sample type media mapping name is already exists.";
	String SAMPLE_TYPE_CODE_EXISIS = "Sample type media mapping code is already exists.";
	String SAMPLE_TYPE_NOT_FOUND = "Sample type media mapping not found.";
	String SAMPLE_TYPE_RECORDS_NOT_FOUND = "Sample type media mapping Records not found.";

	// Age Group Master

	/*String AGE_GROUP_ADD_SUCC = " Age Group added successfully.";*/
	String AGE_GROUP_ADD_SUCC = " Record added successfully.";
	String AGE_GROUP_ADD_FAIL = " Failed to add Age Group.";
	String AGE_GROUP_UPDATE_SUCC = "Age Group updated successfully.";
	String AGE_GROUP_UPDATE_FAIL = "Failed to add Age Group.";
	String AGE_GROUP_ACTIVATE_SUCC = "Age Group activated successfully.";
	String AGE_GROUP_ACTIVATE_FAIL = "Failed to activate Age Group.";
	String AGE_GROUP_INACTIVATE_SUCC = "Age Group inactivated successfully.";
	String AGE_GROUP_INACTIVATE_FAIL = "Failed to activate Age Group.";
	String AGE_GROUP_NAME_EXISIS = "Age Group name is already exists.";
	String AGE_GROUP_CODE_EXISIS = "Age Group code is already exists.";
	String AGE_GROUP_TYPE_EXISIS = "Age Group Type is already exists.";
	String AGE_GROUP_NOT_FOUND = "Unit not found.";
	String AGE_GROUP_RECORDS_NOT_FOUND = "Unit Records not found.";

	// Bacteria Classification Master

	String BACTERIA_CLASSFICATION_ADD_SUCC = "Record added successfully.";
	String BACTERIA_CLASSFICATION_ADD_FAIL = "Failed to add Bacteria Classification.";
	String BACTERIA_CLASSFICATION_UPDATE_SUCC = "Bacteria Classification updated successfully.";
	String BACTERIA_CLASSFICATION_UPDATE_FAIL = "Failed to add Bacteria Classification.";
	String BACTERIA_CLASSFICATION_ACTIVATE_SUCC = "Bacteria Classification activated successfully.";
	String BACTERIA_CLASSFICATION_ACTIVATE_FAIL = "Failed to activate Bacteria Classification.";
	String BACTERIA_CLASSFICATION_INACTIVATE_SUCC = "Bacteria Classification inactivated successfully.";
	String BACTERIA_CLASSFICATION_INACTIVATE_FAIL = "Failed to activate Bacteria Classification.";
	String BACTERIA_CLASSFICATION_NAME_EXISIS = "Bacteria Classification name is already exists.";
	String BACTERIA_CLASSFICATION_CODE_EXISIS = "Bacteria Classification code is already exists.";
	String BACTERIA_CLASSFICATION_DESCRIPTION_EXISIS = "Bacteria Classification Description is already exists.";
	String BACTERIA_CLASSFICATION_NOT_FOUND = "Bacteria Classification not found.";
	String BACTERIA_CLASSFICATION_CORDS_NOT_FOUND = "Bacteria Classification Records not found.";

	// Department Master

	String DEPARTMENT_ADD_SUCC = "Record added successfully.";
	String DEPARTMENT_ADD_FAIL = "Failed to add Department.";
	String DEPARTMENT_UPDATE_SUCC = "Department updated successfully.";
	String DEPARTMENT_UPDATE_FAIL = "Failed to add Department.";
	String DEPARTMENT_ACTIVATE_SUCC = "Department activated successfully.";
	String DEPARTMENT_ACTIVATE_FAIL = "Failed to activate Department.";
	String DEPARTMENT_INACTIVATE_SUCC = "Department inactivated successfully.";
	String DEPARTMENT_INACTIVATE_FAIL = "Failed to activate Department.";
	String DEPARTMENT_NAME_EXISIS = "Department name is already exists.";
	String DEPARTMENT_CODE_EXISIS = "Department code is already exists.";
	String DEPARTMENT_NOT_FOUND = "Department not found.";
	String DEPARTMENT_RECORDS_NOT_FOUND = "Department Records not found.";
	
	// Header Master

	String HEADER_ADD_SUCC = "Record added successfully.";
	String HEADER_ADD_FAIL = "Failed to add Header.";
	String HEADER_UPDATE_SUCC = "Header updated successfully.";
	String HEADER_UPDATE_FAIL = "Failed to add Header.";
	String HEADER_ACTIVATE_SUCC = "Header activated successfully.";
	String HEADER_ACTIVATE_FAIL = "Failed to activate Header.";
	String HEADER_INACTIVATE_SUCC = "Header inactivated successfully.";
	String HEADER_INACTIVATE_FAIL = "Failed to activate Header.";
	String HEADER_NAME_EXISIS = "Header name is already exists.";
	String HEADER_CODE_EXISIS = "Header code is already exists.";
	String HEADER_NOT_FOUND = "Header not found.";
	String HEADER_RECORDS_NOT_FOUND = "Header Records not found.";
// Language Master
	String LANGUAGE_ADD_SUCC = "Record added successfully.";
	String LANGUAGE_ADD_FAIL = "Failed to add Language.";
	String LANGUAGE_UPDATE_SUCC = "Language updated successfully.";
	String LANGUAGE_UPDATE_FAIL = "Failed to add Language.";
	String LANGUAGE_ACTIVATE_SUCC = "Language activated successfully.";
	String LANGUAGE_ACTIVATE_FAIL = "Failed to activate Language.";
	String LANGUAGE_INACTIVATE_SUCC = "Language inactivated successfully.";
	String LANGUAGE_INACTIVATE_FAIL = "Failed to activate Language.";
	String LANGUAGE_NAME_EXISIS = "Language name is already exists.";
	String LANGUAGE_CODE_EXISIS = "Language code is already exists.";
	String LANGUAGE_NOT_FOUND = "Language not found.";
	String LANGUAGE_RECORDS_NOT_FOUND = "Language Records not found.";
	// Serology Master SEROLOGY TEST
	String SEROLOGY_TEST_ADD_SUCC = "Record added successfully.";
	String SEROLOGY_TEST_ADD_FAIL = "Failed to add Header.";
	String SEROLOGY_TEST_UPDATE_SUCC = "Serology Test updated successfully.";
	String SEROLOGY_TEST_UPDATE_FAIL = "Failed to add Header.";
	String SEROLOGY_TEST_ACTIVATE_SUCC = "Serology Test activated successfully.";
	String SEROLOGY_TEST_ACTIVATE_FAIL = "Failed to activate Header.";
	String SEROLOGY_TEST_INACTIVATE_SUCC = "Serology Test inactivated successfully.";
	String SEROLOGY_TEST_INACTIVATE_FAIL = "Failed to activate Header.";
	String SEROLOGY_TEST_NAME_EXISIS = "Serology Test name is already exists.";
	String SEROLOGY_TEST_CODE_EXISIS = "Serology Test code is already exists.";
	String SEROLOGY_TEST_NOT_FOUND = "Serology Test not found.";
	String SEROLOGY_TEST_RECORDS_NOT_FOUND = "Serology Test Records not found.";

	// Serology Master SEROLOGY TEST
	String MACHINE_TEST_ADD_SUCC = "Record added successfully.";
	String MACHINE_TEST_ADD_FAIL = "Failed to add Machine.";
	String MACHINE_TEST_UPDATE_SUCC = "Machine Test updated successfully.";
	String MACHINE_TEST_UPDATE_FAIL = "Failed to add Header.";
	String MACHINE_TEST_ACTIVATE_SUCC = "Machine Test activated successfully.";
	String MACHINE_TEST_ACTIVATE_FAIL = "Failed to activate Machine.";
	String MACHINE_TEST_INACTIVATE_SUCC = "Machine Test inactivated successfully.";
	String MACHINE_TEST_INACTIVATE_FAIL = "Failed to activate Machine.";
	String MACHINE_TEST_NAME_EXISIS = "Machine name is already exists.";
	String MACHINE_TEST_CODE_EXISIS = "Machine code is already exists.";
	String MACHINE_TEST_NOT_FOUND = "Machine Test not found.";
	String MACHINE_TEST_RECORDS_NOT_FOUND = "Machine Test Records not found.";

	String TEST_ADD_SUCC = "Record added successfully.";
	String TEST_ADD_FAIL = "Failed to add Test.";
	String TEST_UPDATE_SUCC = "Test updated successfully.";
	String TEST_UPDATE_FAIL = "Failed to Update Test.";
	String TEST_ACTIVATE_SUCC = "Test activated successfully.";
	String TEST_ACTIVATE_FAIL = "Test deactivated successfully";
	String TEST_INACTIVATE_SUCC = "Test inactivated successfully.";
	/*String TEST_INACTIVATE_FAIL = "Failed to inactivated Test.";*/
	String TEST_INACTIVATE_FAIL = "Test inactivated successfully.";
	String TEST_NAME_EXISIS = "Test name is already exists.";
	String TEST_CODE_EXISIS = "Test code is already exists.";
	String TEST_NOT_FOUND = "Test not found.";

	// Phlebotomy Master

	String PHLEBOTOMY_ADD_SUCC = "Record added successfully.";
	String PHLEBOTOMY_ADD_FAIL = "Failed to add Phlebotomy.";
	String PHLEBOTOMY_UPDATE_SUCC = "Phlebotomy updated successfully.";
	String PHLEBOTOMY_UPDATE_FAIL = "Failed to add Phlebotomy.";
	String PHLEBOTOMY_ACTIVATE_SUCC = "Phlebotomy activated successfully.";
	String PHLEBOTOMY_ACTIVATE_FAIL = "Failed to activate Phlebotomy.";
	String PHLEBOTOMY_INACTIVATE_SUCC = "Phlebotomy inactivated successfully.";
	String PHLEBOTOMY_INACTIVATE_FAIL = "Failed to activate Phlebotomy.";
	String PHLEBOTOMY_NAME_EXISIS = "Phlebotomy name is already exists.";
	String PHLEBOTOMY_CODE_EXISIS = "Phlebotomy code is already exists.";
	String PHLEBOTOMY_NOT_FOUND = "Phlebotomy not found.";
	String PHLEBOTOMY_RECORDS_NOT_FOUND = "Phlebotomy Records not found.";

	// Storage Location Master

	String STORAGE_LOCATION_ADD_SUCC = "Record added successfully.";
	String STORAGE_LOCATION_ADD_FAIL = "Failed to add Storage Location.";
	String STORAGE_LOCATION_UPDATE_SUCC = "Storage Location updated successfully.";
	String STORAGE_LOCATION_UPDATE_FAIL = "Failed to add Storage Location.";
	String STORAGE_LOCATION_ACTIVATE_SUCC = "Storage Location activated successfully.";
	String STORAGE_LOCATION_ACTIVATE_FAIL = "Failed to activate Storage Location.";
	String STORAGE_LOCATION_INACTIVATE_SUCC = "Storage Location inactivated successfully.";
	String STORAGE_LOCATION_INACTIVATE_FAIL = "Failed to activate Storage Location.";
	String STORAGE_LOCATION_NAME_EXISIS = "Storage Location name is already exists.";
	String STORAGE_LOCATION_CODE_EXISIS = "Storage Location code is already exists.";
	String STORAGE_LOCATION_NOT_FOUND = "Storage Location not found.";
	String STORAGE_LOCATION_RECORDS_NOT_FOUND = "Storage Location Records not found.";
	
	
	
	// Paramater Master

	String PARAMATER_ADD_SUCC = "Record added successfully.";
	String PARAMATER_ADD_FAIL = "Failed to add Paramater.";
	String PARAMATER_UPDATE_SUCC = "Paramater updated successfully.";
	String PARAMATER_UPDATE_FAIL = "Failed to add Paramater.";
	String PARAMATER_ACTIVATE_SUCC = "Paramater activated successfully.";
	String PARAMATER_ACTIVATE_FAIL = "Failed to activate Paramater.";
	String PARAMATER_INACTIVATE_SUCC = "Paramater inactivated successfully.";
	String PARAMATER_INACTIVATE_FAIL = "Failed to activate Paramater.";
	String PARAMATER_NAME_EXISIS = "Paramater name is already exists.";
	String PARAMATER_CODE_EXISIS = "Paramater code is already exists.";
	String PARAMATER_NOT_FOUND = "Paramater not found.";
	String PARAMATER_RECORDS_NOT_FOUND = "Paramater Records not found.";
		
		
		// OrganisumGroup Master

		String ORGANISUM_GROUP_ADD_SUCC = "Record added successfully.";
		String ORGANISUM_GROUP_ADD_FAIL = "Failed  to add Organism Group.";
		String ORGANISUM_GROUP_UPDATE_SUCC = "Organisum Group updated successfully.";
		String ORGANISUM_GROUP_UPDATE_FAIL = "Failed to add Organism Group.";
		String ORGANISUM_GROUP_ACTIVATE_SUCC = "Organism Group activated successfully.";
		String ORGANISUM_GROUP_ACTIVATE_FAIL = "Failed to activate Organism Group.";
		String ORGANISUM_GROUP_INACTIVATE_SUCC = "Organism Group inactivated successfully.";
		String ORGANISUM_GROUP_INACTIVATE_FAIL = "Failed to activate Organism Group.";
		String ORGANISUM_GROUP_NAME_EXISIS = "Organism Group name is already exists.";
		String ORGANISUM_GROUP_CODE_EXISIS = "Organism Group code is already exists.";
		String ORGANISUM_GROUP_NOT_FOUND = "Organism Group not found.";
		String ORGANISUM_GROUP__CORDS_NOT_FOUND = "Organism Group Records not found.";
		
		
		// Organisum Master

				String ORGANISUM_ADD_SUCC = "Record added successfully.";
				String ORGANISUM_ADD_FAIL = "Failed  to add Organism.";
				String ORGANISUM_UPDATE_SUCC = "Organisum updated successfully.";
				String ORGANISUM_UPDATE_FAIL = "Failed to add Organism.";
				String ORGANISUM_ACTIVATE_SUCC = "Organism activated successfully.";
				String ORGANISUM_ACTIVATE_FAIL = "Failed to activate Organism.";
				String ORGANISUM_INACTIVATE_SUCC = "Organism inactivated successfully.";
				String ORGANISUM_INACTIVATE_FAIL = "Failed to activate Organism.";
				String ORGANISUM_NAME_EXISIS = "Organism name is already exists.";
				String ORGANISUM_CODE_EXISIS = "Organism code is already exists.";
				String ORGANISUM_NOT_FOUND = "Organism not found.";
				String ORGANISUM_CORDS_NOT_FOUND = "Organism Records not found.";
				
				
				// Organisum Master

				String  STAINIG_ADD_SUCC = "Record added successfully.";
				String  STAINIG_ADD_FAIL = "Failed  to add Staining.";
				String  STAINIG_UPDATE_SUCC = "Staining updated successfully.";
				String  STAINIG_UPDATE_FAIL = "Failed to add Staining.";
				String  STAINIG_ACTIVATE_SUCC = "Staining activated successfully.";
				String  STAINIG_ACTIVATE_FAIL = "Failed to activate Staining.";
				String  STAINIG_INACTIVATE_SUCC = "Staining inactivated successfully.";
				String  STAINIG_INACTIVATE_FAIL = "Failed to activate Staining.";
				String  STAINIG_NAME_EXISIS = "Staining name is already exists.";
				String  STAINIG_CODE_EXISIS = "Staining code is already exists.";
				String  STAINIG_NOT_FOUND = "Staining not found.";
				String  STAINIG_CORDS_NOT_FOUND = "Organism Records not found.";
		//Microbiology Staining 
				String  MICRO_STAINIG_ADD_SUCC = "Recod added successfully.";
				String  MICRO_STAINIG_ADD_FAIL = "Failed  to add Microbiology Staining.";
				String  MICRO_STAINIG_UPDATE_SUCC = "Microbiology Staining updated successfully.";
				String  MICRO_STAINIG_UPDATE_FAIL = "Failed to add Microbiology Staining.";
				String  MICRO_STAINIG_ACTIVATE_SUCC = "Microbiology Staining activated successfully.";
				String  MICRO_STAINIG_ACTIVATE_FAIL = "Failed to activate Microbiology Staining.";
				String  MICRO_STAINIG_INACTIVATE_SUCC = "Microbiology Staining inactivated successfully.";
				String  MICRO_STAINIG_INACTIVATE_FAIL = "Failed to activate Microbiology Staining.";
				String  MICRO_MICRO_STAINIG_NAME_EXISIS = "Microbiology Staining name is already exists.";
				String  MICRO_STAINIG_CODE_EXISIS = "Microbiology Staining code is already exists.";
				String  MICRO_STAINIG_DESC_EXISIS = "Microbiology Staining Name is already exists.";
				String  MICRO_STAINIG_NOT_FOUND = "Microbiology Staining not found.";
				String  MICRO_STAINIG_CORDS_NOT_FOUND = "Microbiology Records not found.";
	//Patient arrival
		
		String PATIENT_NOT_ARRVIAL = "Patient not arrvial .";
		String PATIENT_NOT_FOUND="Patient not found";
		String PATIENT_ARRIVAL_SUCC = "Patient arrvial successfully.";
		String PATIENT_ARRIVAL_FAIL= "Failed Patient arrvial.";

		
		
/** Microbiology Transaction Messages*/
		
		String FINAL_RESULT_SAVE_SUCCESS = "Send to Final Release Successfully.";
		String FINAL_RESULT_SAVE_FAIL = "Fail to Send for Final Release";
		
		String PARTIAL_RESULT_SAVE_SUCCESS = " Partial Release Result save Successfully.";
		String PARTIAL_RESULT_SAVE_FAIL = "Failed To Save Partial Release Result.";
		
		String SEND_SENSITIVITY_TEST_SUCCESS = "Send To Sensitivity Testing Successfully.";
		String SEND_SENSITIVITY_TEST_FAIL = "Failed To Send To Sensitivity Testing";
		
		String RESULT_SAVE_SUCCESS = "Result Save Successfully.";
		String RESULT_SAVE_SUCCESS_FAIL = "Failed To Save Result.";
		

		// REPORT HISTORY
		String REPORT_HISTORY_ADD_SUCC = " Rport histrory added successfully.";
		String REPORT_HISTORY_ADD_FAIL = " Failed to add  Rport histror.";
		
		String SENSITIVITY_FINAL_RESULT_SAVE = "Sensitivity Final Result Save Successfully.";
		String SENSITIVITY_SAVE = "Sensitivity Deatils Save Successfully.";
		String SENSITIVITY_FINAL_RESULT_FAIL = "Failed To Save Sensitivity Final Result.";
		
		String COLONY_MEDIA_ALREADY_MAP = "Colony is already mapped with some media.";
		
		
		/** Gross Creation Messages */
		
		String GROSS_CREATE_SUCCESS = "Gross Created Successfully.";
		String GROSS_CREATE_FAIL = "Failed to Create Gross.";
		
		String SUB_SPECIMAN_CREATE_SUCCESS = "Sub Speciman Created Successfully.";
		String SUB_SPECIMAN_CREATE_FAIL = "Failed to Create Sub Speciman.";
		

		String BLOCK_CREATE_SUCCESS = "Blocks Created Successfully.";
		String BLOCK_CREATE_FAIL = "Failed to Create Blocks.";
		
		String SLIDES_CREATE_SUCCESS = "Slides Created Successfully.";
        String SLIDES_CREATE_FAIL = "Failed to Create Slides.";
        
        String SLIDES_EXAMINATION_SAVE_SUCCESS = "Slides Examination Save Successfully.";
        String SLIDES_EXAMINATION_SAVE_FAIL = "Failed to Save Slides Examination.";
        
        String RESTAIN_REQUEST_SAVE_SUCCESS = "Restain Request Added Successfully.";
        String RESTAIN_REQUEST_SAVE_FAIL = "Failed To Add Restain Request.";
        
        String OUT_SOURCE_DETAILS_SAVE_SUCCESS = "Out Source Details Added Successfully.";
        String OUT_SOURCE_DETAILS_SAVE_FAIL = "Failed To Add Out Source Details .";
        
        //Lab Template Master
        String LAB_TEMP_ACTIVATE_SUCC = "Template activated successfully.";
    	String LAB_TEMP_ACTIVATE_FAIL = "Failed to activate Template.";
    	String LAB_TEMP_INACTIVATE_SUCC = "Template inactivated successfully.";
    	String LAB_TEMP_INACTIVATE_FAIL = "Failed to activate Template.";
    	
    	
    	// Rack Master

    	String RACK_ADD_SUCC = "Record added successfully.";
    	String RACK_ADD_FAIL = "Failed to add Rack.";
    	String RACK_UPDATE_SUCC = "Rack updated successfully.";
    	String RACK_UPDATE_FAIL = "Failed to add Rack.";
    	String RACK_ACTIVATE_SUCC = "Rack activated successfully.";
    	String RACK_ACTIVATE_FAIL = "Failed to activate Rack.";
    	String RACK_INACTIVATE_SUCC = "Rack inactivated successfully.";
    	String RACK_INACTIVATE_FAIL = "Failed to activate Rack.";
    	String RACK_NAME_EXISIS = "Rack name is already exists.";
    	String RACK_CODE_EXISIS = "Rack code is already exists.";
    	String RACK_NOT_FOUND = "Rack not found.";
    	String RACK_RECORDS_NOT_FOUND = "Rack Records not found.";
    	
    	
    	String SLIDE_ACCEPT_SUCC = "Slide Storage Details Saved Successfully.";
    	String SLIDE_ACCEPT_FAIL = "Failed To Save Slide Storage Details.";

    	
    	String FROZEN_SECTION_REQ_SUCC = "Frozen Section Request Added Successfully.";
    	String FROZEN_SECTION_REQ_FAIL = "Failed To Add Frozen Section Request.";
    	String FROZEN_SECTION_REQ_ACCEPT_SUCC = "Request Aknowledged Sucessfully.";
    	String FROZEN_SECTION_REQ_ACCEPT_FAIL = "Failed To Aknowledged Request.";
    	
    	
    	//Panel Test Master
    	   String PANEL_ADD_SUCC = "Record added successfully.";
           String PANEL_ADD_FAIL = "Failed to Panel Test";
           String PANEL_UPDATE_SUCC = "Panel Test Updated Successfully.";
           String PANEL_UPDATE_FAIL = "Failed To Update Panel Test.";
           String PANEL_ACTIVATE_SUCC = "Panel Test Activated successfully.";
           String PANEL_ACTIVATE_FAIL = "Failed To Activate Panel Test";
           String PANEL_INACTIVATE_SUCC = "Panel Test Inactivated successfully.";
           String PANEL_INACTIVATE_FAIL = "Failed To Inactivate Panel Test";
           String PANEL_SERVICE_EXISIS = "Panel Test Service Is Already Exists.";
           String PANEL_ALIAS_NAME_EXISIS = "Panel Alias Name is Already Exists.";
           String PANEL_ALIAS_CODE_EXISIS = "Panel Alias Code is Already Exists.";
           String PANEL_TEST_EXISIS = "Panel Test is Already Exists.";
           String PANEL_NOT_FOUND = "Panel Test Not Found.";
           String PANEL_RECORDS_NOT_FOUND = "Panel Test Records Not Found.";
           
           
           //Formula Messages
           String FORMULA_ADD_SUCC = "Record added successfully.";
           String FORMULA_ADD_FAIL = "Failed to Formula";
           String FORMULA_UPDATE_SUCC = "Formula Updated Successfully.";
           String FORMULA_UPDATE_FAIL = "Failed To Update Formula.";
           String FORMULA_ACTIVATE_SUCC = "Formula Activated successfully.";
           String FORMULA_ACTIVATE_FAIL = "Failed To Activate Formula";
           String FORMULA_INACTIVATE_SUCC = "Formula Inactivated successfully.";
           String FORMULA_INACTIVATE_FAIL = "Failed To Inactivate Formula";
           String FORMULA_TEST_EXISIS = "Formula is Already Exists.";
           String FORMULA_NOT_FOUND = "Formula Not Found.";
           String FORMULA_RECORDS_NOT_FOUND = "Formula Records Not Found.";
    	
           String TEST_COST_HEAD_CODE_EXISIS = "Test Cost Head Code Is Already Exists.";
           String TEST_COST_HEAD_DESC_EXISIS = "Test Cost Head Description Is Already Exists.";
           
         //Visit Type Constant
       	public Integer OP = 1;
       	public Integer IP = 2;
       	public Integer DC = 3;
       	public Integer ER = 4;
       	
      //Package Type Constant
		public Integer SELF=1; //SELF
		public Integer INSURANCE=2; //INSURANCE
		public Integer COMPANY=3; //COMPANY
           
}
