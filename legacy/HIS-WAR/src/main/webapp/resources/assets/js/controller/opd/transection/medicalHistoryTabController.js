'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular.module('myApp').controller('medicalHistoryTabController',['$scope','$cookies','$rootScope','GenericService',
	'BillingGenericService','$state','$timeout','$interval','$stateParams','GenericServiceParamHeader','growl','$compile',
	'reportService','GenericServiceFileUpload','servicesFactory','CONSTANTS','coversheetTemplateService',
	function($scope, $cookies, $rootScope, GenericService,BillingGenericService, $state, $timeout,$interval,
		$stateParams,GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS,coversheetTemplateService) {

			var cookieObject, cookieObjectAppConfig;
			[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();					
			$scope.init = function() {		
		
				$scope.dateLetter = new Date();
				$scope.deptWiseAppointment='N';		
				// object for Medical History
				$scope.historyMedicalList = [{
					isNoSignificantStatus : 'N',
					isDiagnosisName : 'N',
					diagnosisId : '',
					since : "",
					duration : "Day",
					remark : "",
					isEnterInError : "N",
					isReviewedFlag : "N",
					diagnosisName:'',
					diagnosisNameText:'',
					diagnosisCode :  "",
				}];	
				// object for History Surgical
				$scope.historySurgicalList = [{
					isNoSignificantStatus:'N',
					surgeryId:0,
					surgenName:"",
					diagnosisName:"",
					month:"",
					year:"",
					remark:"",
					isEnterInErrorStatus:"N",
					nonFormulary:false,
					isReviewedFlag:"N"
				}];
				// object for History Treatment
				$scope.historyTreatmentList = [{
					isNoSignificantStatus:'N',
					treatmentName:"",
					surgenName:"",
					month:"",
					year:"",
					remark:"",
					isEnterInErrorStatus:"N",
					isReviewedFlag:"N"
				}];
				$scope.medicationObj = {
					itemId : "",
					searchItem :"",
					searchItemFre:"",
					favorite_status : false,
					medicationStartDate : new Date(),
					roaId : '',
					dose : '',
					doseUnitId : '',
					durationCount : "",
					durationUnit:'',
					instructionId : '',
					medicationEndDate : '',
					frequencyId : '',
					quantity : '',
					uomTypeId : '',
					conversions : '',
					strength : '',
					priorityId : 2,
					isStat : false,
					priorityCode: 'NR',
					isGeneric:'N',
					isInstruction:'N',
					nonFormularyAdvice:'N',
					rate:'',
					prescribedDosageUnitList:[]
				};
								
				$scope.initMedicationObj = function() {
					$scope.searchItemFre = "";
					$scope.searchItem = "";
					$scope.registrationNumber = "";
					$scope.saveBtnFlag = true;
					$scope.updateScope = true;
					$scope.updateBtnFlag = false;
					$scope.roa_options=[];
					$scope.medicationObj = {
						itemId : "",
						searchItem : "",
						formulationTypeId : '',
						favorite_status : false,
						medicationStartDate : new Date(),
						roaId : '',
						dose : '',
						doseUnitId : '',
						durationCount : '',
						durationUnit : 'Day',
						instructionId : '',
						medicationEndDate : '',
						frequencyId : '',
						quantity : '',
						uomTypeId : '',
						conversions : '',
						strength : '',
						priorityId : '2',
						priorityCode : 'NR',
						isGeneric:'N',
						isInstruction:'N',
						nonFormularyAdvice:'N',
						searchItemFre :"",
						isStat : false,
						favorite_status : false,
						isDischarge : false,
						nonFormularyAdvice:'N',
						prescribedDosageUnitList:[]
					};
				}
								
				$scope.medicationDetails = [];
				$scope.favouritMedi = {};
				$scope.itemSelecteds = {};
							
				//$scope.medicationHistoryDetails =[];
				//to show empty row in Medication History 
				$scope.emptyMedicationHistoryRow = {
					itemId:"",
					medicationStartDate:'',
					genericName:'',
					itemName:'',
					genericId:'',
					doctorId:'',
					roaId:'',
					durationCount:'',
					medicationName:'',
					dose:'',
					doseUnitId:'',
					durationUnit:'Day',
					instructionId:'',
					medicationEndDate:'',
					frequencyId:'',
					name:'',
					status:'A',
					authorisedDate:'',
					quantity:'',
					storeId:'',
					nonFormulary:'N',
					genericName:"",
					searchItem:"",
					ongingMedHist:false,
					isMedicationContinue:'N',
					isMedicationStopHis:'N'
				};
				$scope.medicationHistoryDetails = [$scope.emptyMedicationHistoryRow];
							
				$scope.msgRecord="No records found...!!";
				$scope.secondTabLevel = 1;
				// $scope.setCurrentTab(1);			
				// $scope.itemSelectedList.items=[];
				$scope.itemSelectedList = {
					items: []
				};
							
				$scope.itemSelectedListActiveMed = {
					itemsMed: [],
					selectAllActMedi:'N'
				};
		
				$scope.instructionSelectedList = {
					items: []
				};
						
				$scope.allergyShowFlag = true;
				$scope.admissionObj={};
				//$scope.admissionObj.bedOptionFlexible = "N";
				$scope.admissionObj.admissionType = "medical";
				$scope.admissionObj.bedOptionFlexible = "N";
				$scope.searchType = "search";
				$scope.currentTab = "investigation";
				$scope.currentOrganizationList = {"organizationId":1,"organizationName":"RubbyHall"}
				$scope.currentUnitList = {"unitId":1,"unitName":"Rubby Unit1"}
				$scope.selectedValues = {unitId : '',doctorId:'',specialityId:''};
				$scope.cur=moment(new Date()).subtract(1, 'days').toDate();
				$scope.currentDateMedication = moment($scope.cur).format('YYYY-MM-DD')
				$scope.currentDate = moment(new Date()).format('YYYY-MM-DD');
								
				var cookieObject = $cookies.getObject('cookieObject');
				if (cookieObject == undefined) {
					$state.go('login');
					return;
				}
							
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.employeeId = cookieObject.employeeId;
										
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				if(cookieObjectAppConfig == undefined){
					$state.go('applicationConfiguration');
					return;
				}
				else
				{
					console.log('cookieObjectAppConfig',cookieObjectAppConfig);
				}
				$scope.diagnosisRequireAppConfig = cookieObjectAppConfig.diagnosisRequire;
				$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
				$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
				$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
				$scope.allergyButtonFlag = true;
				$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
				$scope.radiologySpecialityId=cookieObjectAppConfig.radiologySpecialityId;
				$scope.vaccinationChartHide=cookieObjectAppConfig.vaccinationChartHide;
				$scope.isAdviceAccess=cookieObjectAppConfig.isAdviceTabVisible;
				$scope.roleName = cookieObjectAppConfig.roleName;
				$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
				$scope.doctorRole = cookieObjectAppConfig.doctorRole;
				$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
				$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
				$scope.configureConsultationSpecialityId=cookieObjectAppConfig.consultationSpecialityId;
				$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
				$scope.dateFormat = cookieObjectAppConfig.dateFormat;		    
				if($scope.isAppointmentRequired  == undefined || $scope.isAppointmentRequired == null)
				{
					$scope.isAppointmentRequired = "N";
				}
				else
				{
					$scope.isAppointmentRequired = $scope.isAppointmentRequired;
				}
				// Start get 50 Previous Year with Current Year
				// and month
							
				var year = new Date().getFullYear();
				var range = [];
				range.push(year);
				$scope.patientDetails = 
				{
					uhIdNumber:"",
					patientName:"",
					genderCode:"",
					age:"",
					birthDate:"",
					encounterId:""
				};
							
				$scope.checkAppointment=function(){
					$scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject')) || {};
					console.log("Encounter Object",$scope.encounterDetailsObj);				
					if ($scope.encounterDetailsObj  != null) {
						$scope.billIdForCheck = $scope.encounterDetailsObj.billId;
						$scope.patientDetails.patientName = $scope.encounterDetailsObj.patientName;
						$scope.patientDetails.specialityName = $scope.encounterDetailsObj.specialityName;
						$scope.patientDetails.uhIdNumber = $scope.encounterDetailsObj.uhidNumber;
						$scope.patientDetails.genderCode = $scope.encounterDetailsObj.genderCode;
						$scope.patientDetails.genderId = $scope.encounterDetailsObj.genderId;
						$scope.patientDetails.age = $scope.encounterDetailsObj.age;
						$scope.patientDetails.ageStr = $scope.encounterDetailsObj.age;
						$scope.patientDetails.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
						$scope.patientDetails.birthDate = $scope.encounterDetailsObj.dob;
						$scope.patientDetails.visitDate = $scope.encounterDetailsObj.encounterDate;
						$scope.patientDetails.visitTime = $scope.encounterDetailsObj.consultationStartTime;
						$scope.patientDetails.encounterId = $scope.encounterDetailsObj.encounterId;
						$scope.roleId = $scope.encounterDetailsObj.roleId;
						$scope.patientDetails.patientCategoryId =$scope.encounterDetailsObj.patientCategoryId;
						//$scope.isCritical = $scope.encounterDetailsObj.isCritical;
						$scope.encounterIdVital =  $scope.encounterDetailsObj.encounterId;
						$scope.patientDetails.doctorFullName =  $scope.encounterDetailsObj.doctorName;
						if($scope.encounterDetailsObj.doctorName!=null)
							$scope.patientDetails.dFirstName  =  $scope.encounterDetailsObj.doctorName;
						$scope.patientDetails.consultationStartTime =  $scope.encounterDetailsObj.consultationStartTime;
						
						$scope.encounterDetailsObj.treatingDoctorSpecialityId = "";
						$scope.encounterDetailsObj.treatingDoctorId = "";
						$scope.encounterDetailsObj.isTreatingDoctor = 'Y';
						
						//For year dropdown
						var ya = new Date().getFullYear()
						var BirthData= $scope.patientDetails.birthDate.split('/');
						$scope.birthYear=BirthData[2];
						var birthMonth=BirthData[1].split('');
						$scope.birthMonth=birthMonth[1];
						$scope.YearDifference = ya-$scope.birthYear;
						for (var i = 1; i < 100; i++) {
							range.push(year - i);
						}
										  
						$scope.yearss = range;
						$scope.years=[];
						angular.forEach($scope.yearss,function(value){
							if(parseInt($scope.birthYear) <= value){
								$scope.years.push(value);
							}
						})
						console.log("KI",$scope.yearss);
						console.log("$scope.yearsss",$scope.years);
						//For Contract
						if($scope.encounterDetailsObj.contractId != $scope.selfPayeeId)
						{
							$scope.contractId = $scope.encounterDetailsObj.contractId;
						}
						$scope.showHeadCircuVital=false;
						if($scope.patientDetails.age >= 1 &&  $scope.patientDetails.age <=3 ){
							$scope.showHeadCircuVital=true;
						}				
					}
					else
					{
						$state.go('encounterQueueManagement');
						return;
					}
				}
				$scope.checkAppointment();

				$rootScope.loginpage = true;
				$scope.saveBtnFlag = false;
				$scope.updateBtnFlag = true;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup
							
				var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
				var data = {};
				var dataDose = {
					organizationId:$scope.organizationId
				}
				// For Get Active Speciaity List
				var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
				BillingGenericService.serviceAction(METHOD_POST, URI,commonObj, NOTIFICATION_MSG_STATUS_FALSE).then(
				function(response) {
					if (response.data.status == "success")
						$scope.specialityList = response.data.listObject;
				});
							     
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};
			
				//for Complaint and Diagnosis Template
				$scope.templateListForSave=[];
				var tempObj={};
				tempObj.templateObject={};
				tempObj.diagonisIdforTemplate=0;
				tempObj.icpcSystemId=0;
				tempObj.icpcId=0;
				$scope.templateListForSave.push(tempObj);
				
				$scope.common = [{
					diagnosisId : '',
					icpcSystemId : '',
				}];
							
							     
				if ($scope.encounterDetailsObj == undefined) {
					return;
				}
				var patientId = $scope.encounterDetailsObj.patientId;
				var encounterId = $scope.encounterDetailsObj.encounterId;
				if (angular.equals({},$stateParams.obj)) {
					$state.go('OPDcoversheetNew');
					return;
				} 
				else {
					if($stateParams.obj.newCasesheetList != undefined){
						$scope.newCasesheetList=$stateParams.obj.newCasesheetList;
					}
					if($stateParams.obj.objPrint!= undefined ){
						$scope.objPrint = $stateParams.obj.objPrint;
						$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
					}
//					if($stateParams.obj.vitalsList != undefined){
//						$scope.vitalsList=$stateParams.obj.vitalsList;
//					}
					if($stateParams.obj.currentEncounterOrderLists!= undefined ){
						$scope.currentEncounterOrderLists = $stateParams.obj.currentEncounterOrderLists;
					}
					if($stateParams.obj.familyHistoryCurrentListDoctor != undefined){
						$scope.familyHistoryCurrentListDoctor=$stateParams.obj.familyHistoryCurrentListDoctor;
					}
					if($stateParams.obj.habitFinalList!= undefined ){
						$scope.habitFinalList = $stateParams.obj.habitFinalList;
					}
					if($stateParams.obj.patientHabitOldList != undefined){
						$scope.patientHabitOldList=$stateParams.obj.patientHabitOldList;
					}
					if($stateParams.obj.templateList!= undefined ){
						$scope.templateList = $stateParams.obj.templateList;
					}
					if($stateParams.obj.icpcSystemList != undefined){
						$scope.icpcSystemList=$stateParams.obj.icpcSystemList;
					}
					if($stateParams.obj.templateTransactionList!= undefined ){
						$scope.templateTransactionList = $stateParams.obj.templateTransactionList;
					}
					if($stateParams.obj.detailedMedicationsHistory != undefined){
						$scope.detailedMedicationsHistory=$stateParams.obj.detailedMedicationsHistory;
					}
					if($stateParams.obj.detailedMedications!= undefined ){
						$scope.detailedMedications = $stateParams.obj.detailedMedications;
					}
					if($stateParams.obj.allergySymbolList != undefined){
						$scope.allergySymbolList=$stateParams.obj.allergySymbolList;
					}
					if($stateParams.obj.allergyOldList!= undefined ){
						$scope.allergyOldList = $stateParams.obj.allergyOldList;
					}
					if($stateParams.obj.patientDiagnosisListIcon != undefined){
						$scope.patientDiagnosisListIcon=$stateParams.obj.patientDiagnosisListIcon;
					}
					if($stateParams.obj.patientDiagnosisList!= undefined ){
					$scope.patientDiagnosisList = $stateParams.obj.patientDiagnosisList;
					}
					if($stateParams.obj.complaintListByPatientId != undefined){
						$scope.complaintListByPatientId=$stateParams.obj.complaintListByPatientId;
					}
					if($stateParams.obj.pastComplaintListByPatientId!= undefined ){
						$scope.pastComplaintListByPatientId = $stateParams.obj.pastComplaintListByPatientId;
					}
					if($stateParams.obj.complaintsPatientListDoctor != undefined){
						$scope.complaintsPatientListDoctor=$stateParams.obj.complaintsPatientListDoctor;
					}
					if($stateParams.obj.vitalFinalList!= undefined ){
						$scope.vitalFinalList = $stateParams.obj.vitalFinalList;
					}
					if($stateParams.obj.patientViatalList != undefined){
						$scope.patientViatalList=$stateParams.obj.patientViatalList;
					}
					if($stateParams.obj.patientViatalListTrends!= undefined ){
						$scope.patientViatalListTrends = $stateParams.obj.patientViatalListTrends;
					}
					if($stateParams.obj.vitalDataTrends != undefined){
						$scope.vitalDataTrends=$stateParams.obj.vitalDataTrends;
					}
					if($stateParams.obj.vitalLabel!= undefined ){
						$scope.vitalLabel = $stateParams.obj.vitalLabel;
					}
					if($stateParams.obj.habitTypeList != undefined){
						$scope.habitTypeList=$stateParams.obj.habitTypeList;
					}
					if($stateParams.obj.patientHabitList!= undefined ){
						$scope.patientHabitList = $stateParams.obj.patientHabitList;
					}
					if($stateParams.obj.complaintsPatientList!= undefined ){
						$scope.complaintsPatientList = $stateParams.obj.complaintsPatientList;
					}
					if($stateParams.obj.medicalHistoryCurrentList!= undefined ){
						$scope.medicalHistoryCurrentList = $stateParams.obj.medicalHistoryCurrentList;
					}
				}
			}
			
			 /****For tab access*****/
            var tabsList = JSON.parse(localStorage.getItem('mappedDataCoversheetByUser'));
			 
			$scope.mappedDataCoversheetByUserss = [];
			 $scope.mappedDataCoversheetByUserss =tabsList;
			 
		$scope.checkAccessCover = function (id) 
		{
				if (!$scope.mappedDataCoversheetByUserss || $scope.mappedDataCoversheetByUserss.length == 0) {
					return true;
				}
				var arr = $scope.mappedDataCoversheetByUserss.filter(function (item) 
				{
					return item.headerId == id;
				});
				return (arr.length > 0) ? true : false;

        }
	$scope.test = [];
	$scope.checkAccessCoverSubTab = function (id,headerId) 
	{
			if (!$scope.mappedDataCoversheetByUserss || $scope.mappedDataCoversheetByUserss.length == 0) {
				return true;
			}
			var arr = $scope.mappedDataCoversheetByUserss.filter(function (item) 
			{
				if(headerId == item.headerId){
					if(item.selectedSubTabId.length > 0 ){
						$scope.test = [];
						$scope.test = angular.copy(item.selectedSubTabId);
					}
				}
			});
			if($scope.test.length > 0){
				var arr1 = $scope.test.filter(function (item1) {
					return item1.subTabId == id;
				});
				return (arr1.length > 0) ? true : false;
			}
    }
			$scope.init();
				
			/*************************Init End****************************/	
			/**FOR SET CODE START****** */
			$scope.tab = 2;
			$scope.isSet = function(tabNum){
				return $scope.tab === tabNum;
			};
							
			$scope.gotoNextSectionLevelOne = function(Tab){
				var obj={
					newCasesheetList:$scope.newCasesheetList,
					objPrint :$scope.objPrint,
					vitalsList:$scope.vitalsList,
					currentEncounterOrderLists : $scope.currentEncounterOrderLists,
					familyHistoryCurrentListDoctor : $scope.familyHistoryCurrentListDoctor,
					habitFinalList : $scope.habitFinalList,
					patientHabitOldList : $scope.patientHabitOldList,
					templateList : $scope.templateList,
					icpcSystemList :$scope.icpcSystemList,
					templateTransactionList: $scope.templateTransactionList,
					detailedMedicationsHistory: $scope.detailedMedicationsHistory,
					detailedMedications : $scope.detailedMedications,
					allergySymbolList :$scope.allergySymbolList,
					allergyOldList : $scope.allergyOldList,
					patientDiagnosisListIcon :$scope.patientDiagnosisListIcon,
					patientDiagnosisList: $scope.patientDiagnosisList,
					complaintListByPatientId : $scope.complaintListByPatientId,
					pastComplaintListByPatientId : $scope.pastComplaintListByPatientId,
					complaintsPatientListDoctor :$scope.complaintsPatientListDoctor,
					vitalFinalList :$scope.vitalFinalList,
					patientViatalList :$scope.patientViatalList,
					patientViatalListTrends :$scope.patientViatalListTrends,
					vitalDataTrends : $scope.vitalDataTrends,
					vitalLabel :$scope.vitalLabel,
					habitTypeList :$scope.habitTypeList,
					patientHabitList :$scope.patientHabitList,
					complaintsPatientList : $scope.complaintsPatientList,
					medicalHistoryCurrentList :$scope.medicalHistoryCurrentList
				};
				var id='';
				if(Tab == 1){
					var id= "complaint_section";
					$state.go('complaintsTab', {obj : obj});
				}
				if(Tab == 2){
					var id= "past_history_section";
					$state.go('medicalHistoryTab', {obj : obj});
				}
				if(Tab == 3){
					var id= "drug_history_tab";
					$state.go('drugHistoryTab', {obj : obj});
				}
				if(Tab == 4){
					var id= "social_history";
					$state.go('socialHistoryTab', {obj : obj});
				}
				if(Tab == 5){
					var id= "allergies_section";
					$state.go('allergiesTab', {obj : obj});
				}
				if(Tab == 6){
					var id= "vitals_section";
					$state.go('vitalsTab', {obj : obj});
				}
				if(Tab == 7){
					var id= "examination_tab";
					$state.go('examinationTab', {obj : obj});
				}
				if(Tab == 8){
					var id= "diagnosis_section";
					$state.go('diagnosisTab', {obj : obj});
				}
				if(Tab == 9){
					var id= "orders_section";
					$state.go('ordersTab', {obj : obj});
				}
				if(Tab == 10){
					var id= "notes_section";
					$state.go('notesTab', {obj : obj});
				}
				if(Tab == 11){
					var id= "advice_section";
					$state.go('adviceTab', {obj : obj});
				}
				if(Tab == 12){
					var id= "followUp_section";
					$state.go('follwUpTab', {obj : obj});
				}
				if(Tab == 13){
					var id= "attach_to_casesheet";
					$state.go('attachToCasesheetTab', {obj : obj});
				}
				if(Tab == 14){
					var id= "letters_section";
					$state.go('letterTab', {obj : obj});
				}	
			}
				
			$scope.getDefaultTabOpen=function(tabNo){
				if(tabNo==5){
					$scope.setSecondTabLevel(5);
				}
				if(tabNo==1){		
					$scope.setSecondTabLevel(1);
					$timeout(function() {
						//$("#searchServiceId").focus();
						$(document).ready(function () {
							$('#searchServiceId').focus();
						});
					}, 1000);
				}
				if(tabNo==21){
					$scope.setSecondTabLevel(21);
				}
			}
			/**FOR SET CODE END*******/
						
			$scope.encounterDetailsList = [];
			$scope.encounterDetails = {
				encounterDocId : "",
				encounterId : "",
				admissionId:"",
				visitTypeId:2,
				patientId : "",
				documentPath : "",
				documentTypeId : "",
				status : 'A',
				createdDate : "",
				createdBy : $scope.userId,
				updatedDate : "",
				updatedBy : $scope.userId,
				unitId : $scope.unitId,
				organisationId : $scope.organizationId,
				file : {}
			}
		/*********Complete consultaion************/
			
			$scope.updateEncounterDetailsMultilingual=function(info){
				var headerObjS={
						'organization_id': $scope.organizationId,
						'unit_id':$scope.unitId,
						'Content-Type':'application/json'
				};
				var paramObjForPer={};
				var URI = BASE_URL + ROOT_URL
				+ "/api/opd/updateEncounterDetailsMultilingual";
				GenericServiceParamHeader.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObjS)
					.then( function(response) {
								if (response.data.status == "success")
								{
									$rootScope.stopLoader();
								}
							});
			}
			
		$scope.completeConsultation=function(roleId){					
			var cookieWObject = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
			if (cookieWObject == undefined) {
				return;
			}
			// alert(cookieWObject.encounterId);
			var headerObj={
				'organization_id': $scope.organizationId,
				'unit_id':$scope.unitId,
				'Content-Type':'application/json'
			};
		
			$rootScope.startLoader();
			if(roleId == 3)
			{
				var data = {
					'assignmentStopTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					'isAssignmentCompStatus':'Y',
					'assignmentStopBy': $scope.userId,
					'assignmentStopRoleId': roleId,
					'encounterId': cookieWObject.encounterId,
					"patientId": cookieWObject.patientId,
					"appointmentId":cookieWObject.appointmentId,
					"updatedBy":$scope.userId,
					"childEncounterId":cookieWObject.childEncounterId
				}
			}
			else
			{					
				var data = {
					'isConsultationStatus': "Y",
					'consultationStopTime': moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					'consultationStopBy': $scope.userId,
					'consultationStopRoleId': roleId,
					'encounterId': cookieWObject.childEncounterId,
					"patientId": cookieWObject.patientId,
					"appointmentId":cookieWObject.appointmentId,
					"updatedBy":$scope.userId,
					"isFromCoverSheet":'Y',
					"childEncounterId":cookieWObject.childEncounterId
				}
			
			}
			console.log('SaveCoversheet',data)		
			var paramObjForPer={};
			var datatvgsvx  = data;
			
			var URI = BASE_URL + ROOT_URL + UPDATEENCOUNTERASSIGNMENT;
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObj)
			.then(function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
				{	
					$rootScope.stopLoader();
					$scope.printCaseSheet();
					$scope.updateEncounterDetailsMultilingual(datatvgsvx);
					sessionStorage.removeItem('cookieEncounterObject');
					if(roleId == 2)
					{
						$state.go("doctorDesk");
						$rootScope.stopLoaderPopup();
						}
						else{
							$state.go("encounterQueueManagement");
							$rootScope.stopLoaderPopup();
						}										
				}
			});
		}
		/***********************FOR VITALS DATA******************/
		
		$scope.onClick = function () {
			$scope.myChartForSearch = false;
			$scope.vitalnameGraphList=[];
			$scope.vitalname=[];
			$scope.createdTime=[];
			$scope.vitalData=[];
			$scope.vitalTime=[];
			$scope.vitalLabel=[];
			//for label
			if($scope.patientViatalList.length > 0){
				angular.forEach($scope.patientViatalList, function(value,key) {
					$scope.flag=false;
					if($scope.vitalnameGraphList.length > 0){
						angular.forEach($scope.vitalnameGraphList, function(value3,key) {
							if(value3.vitalName == value.vitalName){
								$scope.flag = true;
							}
						});
					}
					if(!$scope.flag){
						var newArr={};
						newArr.vitalName=value.vitalName;
						newArr.unitDesc=value.unitDesc;
						$scope.vitalnameGraphList.push(newArr);
					}
					console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);	
				});
				//for time array
				angular.forEach($scope.patientViatalList, function(value,key) {
					$scope.flagTime=false;
					if($scope.createdTime.length > 0){
						angular.forEach($scope.createdTime, function(value3,key) {
							if(value3.createdTime1 == value.postFactoTime){
								$scope.flagTime = true;
							}
						});
					}
					if(!$scope.flagTime){
						var newArr={};
						newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
						newArr.createdTime1=value.postFactoTime;
						newArr.createdDate=value.createdDate;
						$scope.createdTime.push(newArr);
					}
					console.log("$scope.createdTime",$scope.createdTime);
				});
								 
				for (var i = 0; i < $scope.createdTime.length; i++) {
					$scope.vitalLabel.push($scope.createdTime[i].createdTime);
				}
				console.log("graph label",$scope.vitalLabel);
								
				//for data
				angular.forEach($scope.createdTime, function(value1,index) {
					$scope.vitalDataTrends=[];
					angular.forEach($scope.patientViatalList, function(value2,index) {					 
						if(value1.createdDate == value2.createdDate){
							$scope.vitalDataTrends.push(value2);
						}
					});
					value1.vitalDataTrends = $scope.vitalDataTrends;
				});
				console.log("d",$scope.createdTime);
				$scope.finalVitalObjFromAllVitalList = {};
				var hh =  $scope.createdTime.length;
				angular.forEach( $scope.createdTime[0].vitalDataTrends, function( value,key) {
					if(value.roleId == 2){
						if(value.vitalId == 6){
							if(parseInt(value.value) != 0){
								$scope.finalVitalObjFromAllVitalList.temp = value.value;
								$scope.finalVitalObjFromAllVitalList.createdDatetemp = value.createdDate;
								$scope.finalVitalObjFromAllVitalList.createdTimetemp = value.createdTime;
								$scope.finalVitalObjFromAllVitalList.unitDesctemp = value.unitDesc;
							}                		
						}
						if(value.vitalId == 4){
							//pulse rate
							if(parseInt(value.value) != 0){
								$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
								$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
								$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
								$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
							}
						}
						if(value.vitalId == 3){
							//BP
							if(parseInt(value.value) != 0){
								$scope.finalVitalObjFromAllVitalList.bps = value.value;
								$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
								$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
								$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
							}
						}
						if(value.vitalId == 14){
							//SPo2
							if(parseInt(value.value) != 0){
								$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
								$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
								$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
								$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
							}
						}
						if(value.vitalId == 7){
//                    		SPo2
                    		if(parseInt(value.value) != 0){
                    		$scope.finalVitalObjFromAllVitalList.bpd = value.value;
                    		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
                    		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
                    		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
                    		}
                    	}
					}
				});
			}
		};
		                    
		$scope.finalVitalObjFromAllVitalList={
			"temp" :"",
			"createdDatetemp" :"",
			"createdTimetemp" :"",
			"unitDesctemp" :"",
			"pulseRate" :"",
			"createdDatepulseRate" :"",
			"createdTimepulseRate" :"",
			"unitDescpulseRat":"",
			"bps" :"",
			"bpd" :"",
			"createdDatebps" :"",
			"createdTimebps" :"",
			"unitDescbps" :"",
		}
	
		$scope.checkFocus = function(){
			$timeout(function() {
				document.getElementById("vitalssss_0").focus();
			}, 500);
		} 
				                    
		$scope.k = function(){
			var headerObj = {
				unit_id : $scope.unitId,
				organization_id : $scope.organizationId
			};
				                    	
			var paramObjForVital = {
				unitId:$scope.unitId,
				organizationId:$scope.organizationId,
				patientId:$scope.encounterDetailsObj.patientId,
			}
			
			var data={
				unitId:$scope.unitId,
				organizationId:$scope.organizationId,
				patientId:$scope.encounterDetailsObj.patientId,
			}
			console.log("jj",paramObjForVital);
			$scope.finalVitalObjFromAllVitalList = {};
			// return false;
			var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
			.then(function(response) {
				console.log(response);                            
				if (response.data.status == "success")
					$scope.vitalFinalList = [];
					$scope.patientViatalListTrends=[];
					$scope.patientViatalList  = response.data.listObject;
					angular.forEach( $scope.patientViatalList, function( value,key) {
						var newArr = {};
						newArr.createdDate = value.createdDate;
						newArr.createdTime = value.createdTime;
						newArr.data = {
							"vitalId" : value.vitalId,
							"value"   : value.value,
						}
						$scope.patientViatalListTrends.push(newArr);
					});
					$scope.onClick();
					$scope.vitalsListFOR=[];
					if($scope.showHeadCircuVital == false){
						angular.forEach( $scope.vitalsList, function( value,key) {
							if(value.vitalId != 15){
								$scope.vitalsListFOR.push(value);	
							}
						});
						$scope.vitalsList=[];
						$scope.vitalsList=$scope.vitalsListFOR;
					}
											
					angular.forEach($scope.vitalsList, function(value , key) {
						var checkFlag = false;
						angular.forEach($scope.patientViatalList, function(value1 , key1) {                             	
							if(value.vitalId == value1.vitalId)
							{
								checkFlag = true;
								$scope.newArr = {};
								$scope.newArr.vitalId = value.vitalId;
								$scope.newArr.minValue = value.minValue;
								$scope.newArr.maxValue = value.maxValue;
								$scope.newArr.vitalName = value.vitalName;
								$scope.newArr.vitalIconPath = value.vitalIconPath;
								$scope.newArr.unit = value.unit;
								$scope.newArr.vitalValue = value1.value;
								$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
								$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
								$scope.newArr.checkQualifier = true;
								if($scope.roleId == 2)
								{
									$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
								}                           
							}                    	
						});
						if (checkFlag == true) {
							$scope.vitalFinalList.push($scope.newArr);
						} 
						else {
							// alert("fdvfd");
							var newArr = {};
							newArr.vitalId = value.vitalId;
							newArr.minValue = value.minValue;
							newArr.maxValue = value.maxValue;
							newArr.vitalName = value.vitalName;
							newArr.vitalIconPath = value.vitalIconPath;
							newArr.unit = value.unit;
							newArr.vitalValue = value.vitalValue;
							//newArr.vitalValue = 0;
							newArr.isReviewedFlag = 'N';
							newArr.vitalOrderOpd  = value.vitalOrderOpd;
							newArr.checkQualifier = true;
							if($scope.roleId == 2)
							{
								newArr.appointmentVitalMapperId ='';
							}
							$scope.vitalFinalList.push(newArr);
						}                        	       
					});
				                             
				                            
					console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
					var datalistVital = $scope.vitalFinalList;
					for (var i = 0; i < datalistVital.length; i++) {
						$scope.vitalFinalList[i].vitalValue = '';
					}
				});
				                        
				$scope.checkFocus();
			}
			$scope.k();
			/*******************************Birth history*********************************************/
			
			var commonObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
			if (commonObj != undefined) {
				console.log("old encounter==="+commonObj.patientId);
				var paramObjForPer = {
					
						'patientId':commonObj.patientId
				};

				 var headerObj = {
			    		 unit_id : $scope.unitId,
							organization_id : $scope.organizationId
			     };
			
				 var data = {};
			 
				  var paramObjForGen = {
						
							'patientId':commonObj.patientId,
							'typeId':1,
							// 'genderId':commonObj.genderId
					};
			  
				$scope.getActiveGeneralExamination=function(){
					var URI = BASE_URL + ROOT_URL
			            + GETGENERALEXAMLIST;
			        GenericServiceParamHeader
			            .serviceAction(METHOD_GET, URI,
			                    data,
			                    NOTIFICATION_MSG_STATUS_FALSE,paramObjForGen,headerObj)
			            .then(
			                    function(response) {
			                        console.log(response);
			                        // $rootScope.startLoader();
			                        if (response.data.status == "success")
			                            $scope.getSavedGeneralExamList = response.data.listObject;
			                        angular.forEach($scope.getSavedGeneralExamList, function(value,key) {
			                        	$scope.descriptiveExamination=value.descriptiveExamination;
			                        });
			                         console.log("General Exam List", $scope.getSavedGeneralExamList);
			                    });
			        }
			}
			//
			
			$scope.validateexamSys=false;
            $scope.validateExaminationSystemic = function(flag){
            	if(flag== 'Y'){
            		$scope.validateexamSys=true;
            	}
                else if(flag !=""){
            		
            		$scope.validateexamSys=true;
            	}
            	else{
            		 $scope.validateexamSys=false;
            	}
            	
            }
			
			 $scope.PopupOkBtn = function() {
			    	
			    	if($scope.value == 1){
					$scope.popUpFlag = false;
					$scope.savePatientVitals();
			    	}
			    	else if($scope.value == 3){
			    		$scope.popUpFlag = false;
			    		$scope.saveGeneralExamination($scope.typeId);
			    	}
				}
			 
			 $scope.saveGeneralExamination = function(typeId)
             {
             	if ($scope.popUpFlag == false) {
						$rootScope.startLoader();
					}
             	console.log("array Gene",$scope.patientSystemFinalArray);
					var paramObj = {};
					var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
					};
					
					
					$scope.listOfListOfGeneralExamSystemMaster = [];
					if(typeId == 1){
					angular.forEach($scope.patientSystemFinalArray, function(value,index) 
							{
								if(value.isCheckedSys == 'Y')
								{
									var parentObj = {};
									parentObj.systemId = value.systemId;
									parentObj.isNADValue = value.isNADValue;
							         if(value.isNADValue == 'Y')
							        	 {
							        	 	parentObj.listOfGeneralSystemObservation = [];
							        	 }
							         else
							        	 {
							        	 parentObj.listOfGeneralSystemObservation = [];
							        	 angular.forEach(value.systemObervationList, function(value1,index1) 
													{
							        		 			// var
							        		 		if(value1.isCheckedOber == 'Y')
							        		 			{
							        		 				var childObservationObj = {};
							        		 				childObservationObj.systemObervationId = value1.observationId; 
								        		 			if(value1.isPropertyRequired == 'N')
								        		 				{
								        		 					childObservationObj.remark = value1.remark;
								        		 					childObservationObj.listOfGeneralSystemProperty = [];
								        		 				}
								        		 			else
								        		 				{
								        		 					childObservationObj.remark = '';
								        		 					childObservationObj.listOfGeneralSystemProperty = [];
								        		 					angular.forEach(value1.systemObservationPropertList, function(value2,index2) 
																			{
								        		 					 			var subChildObservationPropertyObj = {};
											        		 					if(value2.isCheckedOberPro == true)
													        		 			{
											        		 						subChildObservationPropertyObj.systemObervationPropertyId = value2.propertyId;
											        		 						subChildObservationPropertyObj.remark = value2.remark;
											        		 						childObservationObj.listOfGeneralSystemProperty.push(subChildObservationPropertyObj);
													        		 			}
																			});
								        		 				
								        		 				}
								        		 			parentObj.listOfGeneralSystemObservation.push(childObservationObj);
							        		 			}
							        		 		
													});
							        	 }
							         $scope.listOfListOfGeneralExamSystemMaster.push(parentObj);
								}
							});
					
					/*if($scope.validateexamGen == false ){
						growl.warning("Please Enter General Examination ", {
							title : 'Warning!'
						});
						$rootScope.stopLoader();
						return false;
					}*/
             }
					if(typeId == 2){
						angular.forEach($scope.patientPhysicalSystemFinalArray, function(value,index) 
								{
									if(value.isCheckedSys == 'Y')
									{
										var parentObj = {};
										parentObj.systemId = value.systemId;
										parentObj.isNADValue = value.isNADValue;
								         if(value.isNADValue == 'Y')
								        	 {
								        	 	parentObj.listOfGeneralSystemObservation = [];
								        	 }
								         else
								        	 {
								        	 parentObj.listOfGeneralSystemObservation = [];
								        	 angular.forEach(value.systemObervationList, function(value1,index1) 
														{
								        		 			// var
								        		 		if(value1.isCheckedOber == 'Y')
								        		 			{
								        		 				var childObservationObj = {};
								        		 				childObservationObj.systemObervationId = value1.observationId; 
									        		 			if(value1.isPropertyRequired == 'N')
									        		 				{
									        		 					childObservationObj.remark = value1.remark;
									        		 					childObservationObj.listOfGeneralSystemProperty = [];
									        		 				}
									        		 			else
									        		 				{
									        		 					childObservationObj.remark = '';
									        		 					childObservationObj.listOfGeneralSystemProperty = [];
									        		 					angular.forEach(value1.systemObservationPropertList, function(value2,index2) 
																				{
									        		 					 			var subChildObservationPropertyObj = {};
												        		 					if(value2.isCheckedOberPro == true)
														        		 			{
												        		 						subChildObservationPropertyObj.systemObervationPropertyId = value2.propertyId;
												        		 						subChildObservationPropertyObj.remark = value2.remark;
												        		 						childObservationObj.listOfGeneralSystemProperty.push(subChildObservationPropertyObj);
														        		 			}
																				});
									        		 				
									        		 				}
									        		 			parentObj.listOfGeneralSystemObservation.push(childObservationObj);
								        		 			}
								        		 		
														});
								        	 }
								         $scope.listOfListOfGeneralExamSystemMaster.push(parentObj);
									}
								});
						
					/*	if($scope.validateexamSys == false ){
							
							growl.warning("Please Enter Systemic Examination ", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
							
						}*/
                 }
					  var data = { 
							patientId : $scope.encounterDetailsObj.patientId,
              		 	encounterId : $scope.encounterDetailsObj.encounterId,
              		 	createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedBy:$scope.userId,
							createdBy:$scope.userId,
							status:'A',
							typeId:typeId,
							listOfListOfGeneralExamSystemMaster : $scope.listOfListOfGeneralExamSystemMaster
					  }
					  
					  if($scope.descriptiveExamination != undefined && typeId == 1){
						   data.descriptiveExamination= $scope.descriptiveExamination 
					   }
						 if($scope.sysDescriptiveExamination  != undefined && typeId == 2){
							  data.descriptiveExamination= $scope.sysDescriptiveExamination 
						 }
						 
						 if ($scope.popUpFlag) {
							 $scope.showModal = !$scope.showModal;
							    $scope.typeId=typeId;
								$scope.value=3;
							   } 
						 else {
						
					  console.log("Final General Examination Save Arr",data);
				
					  var URI = BASE_URL + ROOT_URL
						+ SAVEGENERALEXAMINATIONAPPOMAPPER;
					 
						GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI,
								data,
								NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.init();
									    $scope.getActiveGeneralExamination();
									    $scope.getActivephysicalExamination();
									    $scope.getSystemicExaminationForCaseSheeet();
								        $scope.getGenericExaminationForCaseSheeet();
								        $scope.getGen();
									    $scope.getSyst();
										return response.data.listObject;
										$rootScope.stopLoader();
								});
					}
             }
			 //
			 $scope.getAutoRenderedServicesByPatient=function(encounterObj){
					
					$rootScope.startLoader();
					$scope.autoRenderedServicesBillingDetails=[];
					// var URI = BASE_URL + ROOT_URL +
					// "/api/billing/serviceMaster/autoRendered";
					// var URI = BASE_URL + ROOT_URL +
					// "/api/billing/serviceMaster/renderedServices";
					var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
					var obj={
							"visitTypeId":1,
							"encounterId":encounterObj.encounterId
					}
					console.log("AUTORENDER",obj);
					// return false;
					BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					{	
						if(response.data.status == "success")
						{
							$scope.autoRenderedServicesList = response.data.listObject;
							console.log("$scope.autoRenderedServicesList",$scope.autoRenderedServicesList);
							
							
							
							/*
							 * var indexT =
							 * response.data.listObject.findIndex(record =>
							 * record.serviceMasterId ===
							 * $scope.surchargeServiceId);
							 * 
							 * if (indexT >= 0) {
							 * $scope.isPatientClass = "Y"; }
							 */
						    
							// return false;
							  $scope.calculationTestPostBilling($scope.autoRenderedServicesList,1);
							  $rootScope.stopLoader();
						}
						else
							{
							 $rootScope.stopLoader();
							}
					});
				}
			 //
			 var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
					var data = {};
					
					var dataDose = {
							organizationId:$scope.organizationId
					}
					// for add to bill*****************************
		    	    
		    	    var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/"+$scope.encounterDetailsObj.encounterId;
		    		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		    				$scope.billNumber =response.data.billNumber;
		    				console.log("$scope.billNumberrrr", $scope.billNumber);
		    		});
		    	    
		    	  // *************************************
		    	  
		
				
				var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
				GenericService.serviceAction("GET", URI, {}).then(
				function(response) {
				$scope.doseList = [];
				$scope.doseList = response.data;

				});
				//********************************
				 $scope.getActivephysicalExamination=function(){
					 
						

						 var headerObj = {
					    		 unit_id : $scope.unitId,
									organization_id : $scope.organizationId
					     };
						
						 var data = {};
						 
						  var paramObjForGen1 = {
									
									'patientId':commonObj.patientId,
									'typeId':1,
									// 'genderId':commonObj.genderId
							};
						var URI = BASE_URL + ROOT_URL
				            + GETGENERALEXAMLIST;
				        GenericServiceParamHeader
				            .serviceAction(METHOD_GET, URI,
				                    data,
				                    NOTIFICATION_MSG_STATUS_FALSE,paramObjForGen1,headerObj)
				            .then(
				                    function(response) {
				                        console.log(response);
				                        // $rootScope.startLoader();
				                        if (response.data.status == "success")
				                            $scope.getSavedPhysicalExamList = response.data.listObject;
				                        angular.forEach($scope.getSavedPhysicalExamList, function(value,key) {
				                        	$scope.sysDescriptiveExamination=value.descriptiveExamination;
				                        });
				                        console.log("physical Exam List", $scope.getSavedPhysicalExamList);
				                    });
				        }
			
			/**********************Template Show*******************************************/
			 $scope.templateTransaction = {
       			  templateTransactionId : "",
       			  templateId : "",
       			  title : "",
       			  specialityId : "",
       			  subSpecialityId : "",
       			  diagnosisId:"",
       			  templateObject : "",
       			  admissionId : "",
       			  encounterId : "",
       			  doctorId : "",
       			  patientId : "",
       			  status : 'A',
       			  signedBy : $scope.userId,
       			  signedDate : "",
       			  jsonObject : "",
       			  orgId : $scope.organizationId,
       			  unitId : $scope.unitId,
       			  createdBy : $scope.userId,
       			  createdDate : "",
       			  updatedBy : $scope.userId,
       			  updatedDate : "",
       			  diagnosisId :"",
					  mIcpcSystemId:"",
					  mIcpcId:"",
       }
			 $scope.saveTrasactionFlag = false;
		        $scope.updateTrasactionFlag = false;
		        $scope.templateObj=false;
		        $scope.template={};
		        $scope.templateTransactionList = [];
				$scope.templateList=[];
				$scope.templateTransactionList =[];
			 	$scope.diagonisIdforTemplate = 0;
			 	$scope.icpcSystemId = 0;
				$scope.icpcId = 0;
			$scope.getOpdCoverSheetTempltes = function(templateTypeId) 
	    	{
	    		
	    		$rootScope.startLoader();
	        	var data={
	        		  orgId : $scope.organizationId,
	        		  unitId : $scope.unitId,
	        		  doctorId : $scope.encounterDetailsObj.doctorId,
	        		  specialityId :$scope.encounterDetailsObj.specialityId==undefined?0:$scope.encounterDetailsObj.specialityId,
	        		  templateTypeId : templateTypeId
	        		}
	        	
	        	console.log('data',JSON.stringify(data));
	    		var URI = BASE_URL + ROOT_URL + "/api"+GLOBAL+TEMPLATE_LIST_BY_SPECILATIY;
	    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
	    				{
	    	
	    			   if(response.data.status=='success')
	    			      {
	    				   	$scope.templateList = response.data.listObject;
	    				   	$('#addtemplateModal').modal('show');
	    				   	$rootScope.stopLoader();
	    			      }
	    			   else if(response.data.status=='error'){
	    				   $rootScope.stopLoader();
	    			   }
	    		});
	    	}
			//select Template
			$scope.selectTemplate  = function(templateObj)
	    	{
	    		$scope.template = templateObj;
	    	}
			//save Template
			$scope.addTemplate = function()
	    	{
	    		
						   try{
							   $('#addtemplateModal').modal('hide'); 
							   $rootScope.startLoader();
								  var templateTransaction = angular
									.copy($scope.templateTransaction);
							templateTransaction.templateId = $scope.template.templateId;
							templateTransaction.title = $scope.template.title;
							templateTransaction.specialityId = $scope.template.specialityId;
							templateTransaction.templateTransactionId=$scope.template.templateTransactionId==undefined ? 0 :$scope.template.templateTransactionId;
							templateTransaction.subSpecialityId = $scope.template.subSpecialityId;
							templateTransaction.templateTypeId = $scope.template.templateTypeId;
							templateTransaction.encounterId=$scope.encounterDetailsObj.encounterId;
							templateTransaction.doctorId = $scope.encounterDetailsObj.doctorId;
							templateTransaction.patientId = $scope.encounterDetailsObj.patientId;
							templateTransaction.diagnosisId = 0;
							templateTransaction.mIcpcSystemId = 0;
							templateTransaction.mIcpcId = 0;
                            templateTransaction.patientName = $scope.encounterDetailsObj.patientName !== undefined && $scope.encounterDetailsObj.patientName !== null ? $scope.encounterDetailsObj.patientName : "";
                            templateTransaction.doctorName = $scope.encounterDetailsObj.doctorName !== undefined && $scope.encounterDetailsObj.doctorName !== null ? $scope.encounterDetailsObj.doctorName : "";
                            templateTransaction.uhidNumber = $scope.encounterDetailsObj.uhidNumber !== undefined && $scope.encounterDetailsObj.uhidNumber !== null ? $scope.encounterDetailsObj.uhidNumber : $scope.encounterDetailsObj.uhidnumber;
                            templateTransaction.dob = $scope.encounterDetailsObj.dob !== undefined && $scope.encounterDetailsObj.dob !== null ? $scope.encounterDetailsObj.dob : "";
                            templateTransaction.ageString = $scope.encounterDetailsObj.age !== undefined && $scope.encounterDetailsObj.age !== null ? $scope.encounterDetailsObj.age : $scope.encounterDetailsObj.ageYear;
                            templateTransaction.doa = $scope.encounterDetailsObj.datepicker !== undefined && $scope.encounterDetailsObj.datepicker !== null ? $scope.encounterDetailsObj.datepicker : "";
                            templateTransaction.pdd = $scope.encounterDetailsObj.datepickerPdd !== undefined && $scope.encounterDetailsObj.datepickerPdd !== null ? $scope.encounterDetailsObj.datepickerPdd : "";
                            templateTransaction.admissionNumber = $scope.encounterDetailsObj.admissionNumber !== undefined && $scope.encounterDetailsObj.admissionNumber !== null ? $scope.encounterDetailsObj.admissionNumber : "";
                            templateTransaction.gender = $scope.encounterDetailsObj.genderCode !== undefined && $scope.encounterDetailsObj.genderCode !== null ? $scope.encounterDetailsObj.genderCode : "";

									  var URI = BASE_URL + ROOT_URL + NEW_TEMPLATE_SAVE;
										BillingGenericService.serviceAction(METHOD_POST, URI,templateTransaction,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													if (response.data.status == "success"){
														window.open(response.data.object+"&formType=digital",'_blank');	
														 $rootScope.stopLoader();
													}
													else if(response.data.status == "error")
														{
														  $('#addtemplateModal').modal('hide');
														  $rootScope.stopLoader();
														}
													
												});		
								}catch(e){
									console.log(e.message);
								}
	    	 
			}
			//view template
			$scope.viewTemplate = function(template)
	    	{
	    		$scope.saveTrasactionFlag = false;
				$scope.updateTrasactionFlag = false;
				$scope.obj = {};
				$('#viewtemplateModal').modal('hide');
				$('#addviewTemplateModal').modal('show');
				
	    	}
			function isEmpty(obj) {
	    	    for(var key in obj) {
	    	        if(obj.hasOwnProperty(key))
	    	            return true;
	    	    }
	    	    return false;
	    	}
	    	//
	        $scope.saveTemplateTransaction = function()
	        {
				$rootScope.startLoader();

				var templateTransaction = angular
						.copy($scope.templateTransaction);
				templateTransaction.templateId = $scope.template.templateId;
				templateTransaction.title = $scope.template.title;
				templateTransaction.specialityId = $scope.template.specialityId;
				templateTransaction.templateTransactionId=$scope.template.templateTransactionId==undefined ? 0 :$scope.template.templateTransactionId;
				templateTransaction.subSpecialityId = $scope.template.subSpecialityId;
				var dataObj=document.getElementById("htmlContent").innerHTML;
				console.log("sdfsdfdsd",dataObj);
				// $scope.template.templateObject.html=document.getElementById("htmlContent").innerHTML;
				// var temObj=$scope.template.templateObject;
				templateTransaction.templateObject = $scope.template.templateObject;
				templateTransaction.encounterId=$scope.encounterDetailsObj.encounterId;
				templateTransaction.doctorId = $scope.encounterDetailsObj.doctorId;
				templateTransaction.patientId = $scope.encounterDetailsObj.patientId;
				templateTransaction.diagnosisId = 0;
				templateTransaction.mIcpcSystemId = 0;
				templateTransaction.mIcpcId = 0;
		
				if(isEmpty($scope.template.templateObject.obj)) {
					var jsonObject = angular.equals({},
							$scope.template.templateObject.obj) ? JSON
							.parse($scope.template.templateObject.obj)
							: {};
				}
				
						  /*
							 * var templateObj = JSON
							 * .parse($scope.template.templateObject);
							 */
				var templateObj = $scope.template.templateObject;
				templateObj.html=dataObj;
				templateObj.obj = $scope.obj;
				templateTransaction.jsonObject = JSON
						.stringify(jsonObject);
				templateTransaction.templateObject = JSON
						.stringify(templateObj);
				console.log('templateTransaction', JSON
						.stringify(templateTransaction));
				var URI = BASE_URL + ROOT_URL
						+ "/global/template/transaction";
				GenericService
						.serviceAction("POST", URI,
								templateTransaction)
						.then(
								function(response) {
									if (response.data.status == "success") {
										if($scope.template.templateTypeId==4){
											growl.success("Complaint assessment done successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else if($scope.template.templateTypeId==17){
											growl.success("Diagnosis assessment done successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else if($scope.template.templateTypeId==19){
											growl.success("Personal History recorded successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else if($scope.template.templateTypeId==5){
											growl.success("General Examination done successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else if($scope.template.templateTypeId==6){
											growl.success("Systemic Examination done successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else if($scope.template.templateTypeId==2){
											growl.success("Letter documented successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else if($scope.template.templateTypeId==3){
											growl.success("Certificate documented successfully.",{
							    				title : SUCCESS_MSG
							    			 });
										}else{
											growl.success("Template saved successfully.",{
							    				title : SUCCESS_MSG
							    			 });
											
										}	
										if(templateTransaction.encounterId!=0)
											$rootScope.getTemplateCountByAdmissionOrEncounter(templateTransaction.patientId,null,templateTransaction.encounterId);
										$(
												'#addviewTemplateModal')
												.modal('hide');
										$('#addtemplateModal')
												.modal('hide');
										$scope.templateTransactionList = [];
										$scope.templateList = [];
										$scope.templateTransactionList = [];
									}
									$rootScope.stopLoader();
								});
			}
	    	
			 /** ***************************Template List ********************/
	        
	       	$scope.getTemplateList = function(templateTypeId) 
	    	{
	    		
	    		$rootScope.startLoader();
	        	var data={
	        		  orgId : $scope.organizationId,
	        		  unitId : $scope.unitId,
	        		  doctorId : $scope.encounterDetailsObj.doctorId,
	        		  specialityId : $scope.encounterDetailsObj.specialityId,
	        		  templateTypeId : templateTypeId,
	        		  patientId : $scope.encounterDetailsObj.patientId,
	        		  visitTypeId: $scope.encounterDetailsObj.visitTypeId,
	        		  appointmentId:$scope.encounterDetailsObj.appointmentId
	        		}
	        	
	        	console.log('data',JSON.stringify(data));
	    		var URI = BASE_URL + ROOT_URL + "/global/petient/template";
	    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
	    				{
	    	
	    			   if(response.data.status=='success')
	    			      {
	    				   	$scope.templateTransactionList = response.data.listObject;
	    				   	$('#viewtemplateModal').modal('show');
	    				   	$rootScope.stopLoader();
	    			      }
	    			   else if(response.data.status=='error'){
	    				   $rootScope.stopLoader();
	    			   }
	    		});
	    	}
				//
	       	var URI = BASE_URL + ROOT_URL
			+ "/api/common/getSpecialityByDept";
			
				BillingGenericService
						.serviceAction(METHOD_POST, URI,
								commonObj,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.instructions = response.data.listObject;
								});
					             
				//////type1///////
$scope.getGen = function(){
					
					$rootScope.startLoader();
				  var headerObj = {
						    unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						     };
                 
                 
                 var paramObjForGenExa = {
                 		patientId:$scope.encounterDetailsObj.patientId,
                 		typeId : 1
                 		// PatientId:2
                 };
                 var headerObjSys = {
                 		"unit_id" : $scope.unitId,
							"organization_id" : $scope.organizationId,
							"gender_id" : $scope.encounterDetailsObj.genderId,
							"type_id" : 1
                 };
                 
                 var URI = BASE_URL + ROOT_URL
                 + GETGENERALEXAMINATIONAPPOMAPPER;
             GenericServiceParamHeader
                 .serviceAction(METHOD_GET, URI,
                         data,
                         NOTIFICATION_MSG_STATUS_FALSE,paramObjForGenExa,headerObj)
                 .then(
                         function(response) {
                             console.log(response);
                            if (response.data.status == "success")
                         	   {
                         	  // alert("in general");
                         	   		console.log("Added GEn EXA",response.data.listObject);
                         	   		
                         	   		$scope.patGenExaList = response.data.listObject;
                         	   		// $scope.patGenExa =
										// response.data.listObject;
                         	   		$scope.patGenExa = [];
                         	   			var dataSys={
                                 	   				"unitId" : $scope.unitId,
                									"organizationId" : $scope.organizationId,
                									"genderId" : $scope.encounterDetailsObj.genderId,
                									"typeId" : 1
                         	   			}
// var URI = BASE_URL+ ROOT_URL+ GETSYSTEMMASTER;

										var URI = BASE_URL+ ROOT_URL+ "/api/opd/getListOfSystemsAndObservation";
										
										$scope.patientSystemFinalArray = [];
										
									GenericServiceParamHeader.serviceAction(METHOD_POST,URI,dataSys,NOTIFICATION_MSG_STATUS_FALSE)
													.then(function(response1) {
														console.log(response1);
										if (response1.data.status == "success") {
											
											$scope.systemList = response1.data.listObject;
											console.log("systemMaster1",$scope.systemList);
											
											// loop for systenList
											angular.forEach($scope.systemList,function(value,key) {
												var dataObj = {};
												dataObj.systemId = value.systemId;
												dataObj.systemName = value.systemName;
												dataObj.isNADValue = 'N';
												dataObj.isCheckedSys = 'N';
												
												 $scope.systemCheckFlag= false;
													dataObj.isNADValue = 'N';
													dataObj.systemObervationList = [];
													
													// Test Start
// if(value.generalExaminationAppoMapperList.length == 0){
														angular.forEach($scope.getSavedGeneralExamList,function(value3,key3) {
															if(value3.systemId == value.systemId)
															{
																if(value3.isNADValue =='Y'){
																 $scope.systemCheckFlag= true;
																 dataObj.isNADValue = 'Y';
																 dataObj.isCheckedSys = 'Y';
																 dataObj.systemObervationList = [];
																}
															}
														});
														
														angular.forEach($scope.getSavedGeneralExamList, function(value,key) {
								                        	$scope.descriptiveExamination=value.descriptiveExamination;
								                        });
// }
													angular.forEach(value.generalExaminationAppoMapperList,function(value2,key2) {
														
														var dataObjObervation = {};
														dataObjObervation.observationId = value2.observationId;
														dataObjObervation.observationName = value2.observationName;
														dataObjObervation.systemObservationPropertList =[];
														angular.forEach(value2.systemObervationMasterList,function(value4,key4) {
															var dataObjObervationProperty = {};
															dataObjObervationProperty.propertyId = value4.propertyId;
														    dataObjObervationProperty.propertyName = value4.propertyName;
															dataObjObervationProperty.remark = '';
															dataObjObervationProperty.isCheckedOberPro = false;
															dataObjObervation.systemObservationPropertList.push(dataObjObervationProperty);
															console.log("same observation id",dataObjObervation.systemObservationPropertList);
														
													});
														
														angular.forEach($scope.getSavedGeneralExamList,function(value3,key3) {
															if(value3.systemId == value.systemId)
															{
																
																if((value3.systemObervationId == value2.observationId) && (value2.isPropertyRequired == 'Y'))
																{
																	$scope.systemCheckFlag = true;
																	dataObj.isCheckedSys = 'Y';
																	dataObjObervation.remark = '';
																	dataObjObervation.isPropertyRequired = 'Y';
																	dataObjObervation.isCheckedOber = 'Y';
	                                                                dataObjObervation.systemObservationPropertList =  [];
																			angular.forEach(value2.systemObervationMasterList,function(value4,key4) {
																				var dataObjObervationProperty = {};
																				dataObjObervationProperty.propertyId = value4.propertyId;
																				dataObjObervationProperty.propertyName = value4.propertyName;
																				
																					if(value3.systemObervationPropertyId == value4.propertyId)
																						{
																						    dataObjObervationProperty.isCheckedOberPro = true;
																							dataObjObervationProperty.remark = value3.remark;
																						}
																					else
																						{
																							dataObjObervationProperty.remark = '';
																							dataObjObervationProperty.isCheckedOberPro = false;
																						}
																					dataObjObervation.systemObservationPropertList.push(dataObjObervationProperty);
																				
																			});
																			
																		
																}else if((value3.systemObervationId == value2.observationId) && (value2.isPropertyRequired == 'N'))
																{
																	
																	$scope.systemCheckFlag = true;
																	dataObj.isCheckedSys = 'Y';
																	dataObjObervation.isPropertyRequired = 'N';
																	// new
																	// added
																	dataObjObervation.isCheckedOber = 'Y';
																	// end
																	// new
																	// added
																	dataObjObervation.remark = value3.remark;
																	dataObjObervation.systemObservationPropertList = [];
																	// dataObj.systemObervationList.push(dataObjObervation);
// dataObjObervation.systemObservationPropertList.push(dataObjObervation);
																}
															
															}
														});
														
														if(!$scope.systemCheckFlag){
														if(value2.isPropertyRequired == 'Y')
															{
															
															dataObjObervation.isPropertyRequired = 'Y';
															dataObjObervation.remark = '';
															dataObjObervation.isCheckedOber = 'N';
															dataObjObervation.systemObservationPropertList = [];
															angular.forEach(value2.systemObervationMasterList,function(value3,key3) {
																			var dataObjObervationProperty = {};
																			dataObjObervationProperty.propertyId = value3.propertyId;
																			dataObjObervationProperty.propertyName = value3.propertyName;
																			dataObjObervationProperty.remark = '';
																			dataObjObervationProperty.isCheckedOberPro = false;
																			dataObjObervation.systemObservationPropertList.push(dataObjObervationProperty);
																			
																});
															}
														else
															{
															
																dataObjObervation.isPropertyRequired = 'N';
																dataObjObervation.remark = '';
																dataObjObervation.isCheckedOber = 'N';
																dataObjObervation.systemObservationPropertList = [];
															}
														}
														dataObj.systemObervationList.push(dataObjObervation);
														
													});
													
													
													$scope.patientSystemFinalArray.push(dataObj);
											});
											console.log("Finalll System Array",$scope.patientSystemFinalArray);
											
										}
										
									});
	                            
                         	   }
                            $rootScope.stopLoader();
                         });
            
				}
//////////////type2/////////////
$scope.getSyst = function(){
	 $rootScope.startLoader();
var headerObj = {
		    unit_id : $scope.unitId,
			organization_id : $scope.organizationId
		     };
var paramObjForPhyExa = {
		patientId:$scope.encounterDetailsObj.patientId,
		typeId : 2
};
var headerObjSysPhy = {
		    "unit_id" : $scope.unitId,
			"organization_id" : $scope.organizationId,
			"gender_id" : $scope.encounterDetailsObj.genderId,
			"type_id" : 2
};
var URI = BASE_URL + ROOT_URL
+ GETGENERALEXAMINATIONAPPOMAPPER;
GenericServiceParamHeader
.serviceAction(METHOD_GET, URI,
       data,
       NOTIFICATION_MSG_STATUS_FALSE,paramObjForPhyExa,headerObj)
.then(
       function(response) {
           console.log("physical exam======"+response);
          if (response.data.status == "success")
       	   {
       	   // alert("in physica");
       	   		console.log("Added Physical",response.data.listObject);
       	   		
       	   		// $scope.patPhyExa =
						// response.data.listObject;
       	   		$scope.patPhyExa = [];
       	   		// --------------------//
       	   		var dataSys={
        	   				"unitId" : $scope.unitId,
							"organizationId" : $scope.organizationId,
							"genderId" : $scope.encounterDetailsObj.genderId,
							"typeId" : 2
       	   		}
       	   		// ------------------//
//var URI = BASE_URL+ ROOT_URL+ GETSYSTEMMASTER;
					var URI = BASE_URL+ ROOT_URL+ "/api/opd/getListOfSystemsAndObservation";

						$scope.patientPhysicalSystemFinalArray = [];
						
							GenericServiceParamHeader.serviceAction(METHOD_POST,URI,dataSys,NOTIFICATION_MSG_STATUS_FALSE)

									.then(function(response1) {
										console.log(response1);
						if (response1.data.status == "success") {
							
							$scope.systemListPhy = response1.data.listObject;
							console.log("systemMasterPhy",$scope.systemListPhy);
							
							// loop for systenList
							angular.forEach($scope.systemListPhy,function(value,key) {
								
								
								var dataObj = {};
								dataObj.systemId = value.systemId;
								dataObj.systemName = value.systemName;
								dataObj.isNADValue = 'N';
								dataObj.isCheckedSys = 'N';
								$scope.systemCheckFlag= false;
								dataObj.isNADValue = 'N';
								dataObj.systemObervationList = [];
									
									// Test Start
								
//if(value.generalExaminationAppoMapperList.length == 0){
									angular.forEach($scope.getSavedPhysicalExamList,function(value3,key3) {
										if(value3.systemId == value.systemId)
										{
											if(value3.isNADValue =='Y'){
											 $scope.systemCheckFlag= true;
											 dataObj.isNADValue = 'Y';
											 dataObj.isCheckedSys = 'Y';
											 dataObj.systemObervationList = [];
											}
										}
									});
									
									 angular.forEach($scope.getSavedPhysicalExamList, function(value,key) {
				                        	$scope.sysDescriptiveExamination=value.descriptiveExamination;
				                        });
//}
								
									angular.forEach(value.generalExaminationAppoMapperList,function(value2,key2) {
										
										var dataObjObervation = {};
										dataObjObervation.observationId = value2.observationId;
										dataObjObervation.observationName = value2.observationName;
										dataObjObervation.systemObservationPropertList =  [];
										
										angular.forEach(value2.systemObervationMasterList,function(value4,key4) {
												var dataObjObervationProperty = {};
												dataObjObervationProperty.propertyId = value4.propertyId;
											    dataObjObervationProperty.propertyName = value4.propertyName;
												dataObjObervationProperty.remark = '';
												dataObjObervationProperty.isCheckedOberPro = false;
												dataObjObervation.systemObservationPropertList.push(dataObjObervationProperty);
												console.log("same observation id",dataObjObervation.systemObservationPropertList);
											
										});
										
										angular.forEach($scope.getSavedPhysicalExamList,function(value3,key3) {
											if(value3.systemId == value.systemId)
											{
												if((value3.systemObervationId == value2.observationId) && (value2.isPropertyRequired == 'Y'))
												{
													$scope.systemCheckFlag = true
													dataObj.isCheckedSys = 'Y';
													dataObjObervation.remark = '';
													dataObjObervation.isPropertyRequired = 'Y';
													dataObjObervation.isCheckedOber = 'Y';
													dataObjObervation.systemObservationPropertList = [];
														
															angular.forEach(value2.systemObervationMasterList,function(value4,key4) {
																var dataObjObervationProperty = {};
																dataObjObervationProperty.propertyId = value4.propertyId;
																dataObjObervationProperty.propertyName = value4.propertyName;
																
																	if(value3.systemObervationPropertyId == value4.propertyId)
																		{
																		    dataObjObervationProperty.isCheckedOberPro = true;
																		 	dataObjObervationProperty.remark = value3.remark;
																		}
																	else
																		{
																			dataObjObervationProperty.remark = '';
																			dataObjObervationProperty.isCheckedOberPro = false;
																		}
																	dataObjObervation.systemObservationPropertList.push(dataObjObervationProperty);
																	console.log("same observation id",dataObjObervation.systemObservationPropertList);
																
															});
												}else if((value3.systemObervationId == value2.observationId) && (value2.isPropertyRequired == 'N'))
												{
													
													$scope.systemCheckFlag = true
													dataObjObervation.isPropertyRequired = 'N';
													// new
													// added
													dataObjObervation.isCheckedOber = 'Y';
													// end
													// new
													// added
													dataObjObervation.remark = value3.remark;
													dataObjObervation.systemObservationPropertList = [];
													// dataObj.systemObervationList.push(dataObjObervation);
													dataObjObervation.systemObservationPropertList.push(dataObjObervation);
												}
											}
									
										 });
										if(!$scope.systemCheckFlag){
											if(value2.isPropertyRequired == 'Y')
												{
												
												dataObjObervation.isPropertyRequired = 'Y';
												dataObjObervation.remark = '';
												dataObjObervation.isCheckedOber = 'N';
												dataObjObervation.systemObservationPropertList = [];
												angular.forEach(value2.systemObervationMasterList,function(value3,key3) {
																var dataObjObervationProperty = {};
																dataObjObervationProperty.propertyId = value3.propertyId;
																dataObjObervationProperty.propertyName = value3.propertyName;
																dataObjObervationProperty.remark = '';
																dataObjObervationProperty.isCheckedOberPro = false;
																dataObjObervation.systemObservationPropertList.push(dataObjObervationProperty);
																
													});
												}
											else
												{
												
													dataObjObervation.isPropertyRequired = 'N';
													dataObjObervation.remark = '';
													dataObjObervation.isCheckedOber = 'N';
													dataObjObervation.systemObservationPropertList = [];
												}
										}
									
										dataObj.systemObervationList.push(dataObjObervation);
										
									});
									
									
									$scope.patientPhysicalSystemFinalArray.push(dataObj);
							});
							
							console.log("Finalll Physical System Array",$scope.patientPhysicalSystemFinalArray);
						}
					});
				   $rootScope.stopLoader();
       	   }
        
       });
}
/////***********************************
$scope.getGenericExaminationForCaseSheeet=function(){
	 var data = {
			 organizationId:$scope.organizationId,
			 patientId:$scope.encounterDetailsObj.patientId,
			 typeId:1
	 };
	var URI = BASE_URL + ROOT_URL
	+ "/api/opd/getListOfSystemsAndObservationForCaseSheet";
   GenericServiceParamHeader
       .serviceAction(METHOD_POST, URI,
               data,
               NOTIFICATION_MSG_STATUS_FALSE)
       .then(
               function(response) {
                   console.log(response);
                   if (response.data.status == "success")
						$scope.getSavedListOfGeneralExam = response.data.listObject;
					angular.forEach($scope.getSavedListOfGeneralExam, function(value,key) {
                   	$scope.descriptiveExamination=value.descriptiveExamination;
                   });
                   console.log("getSavedListOfGeneralExam", $scope.getSavedListOfGeneralExam);
               });
   }
		$scope.getGenericExaminationForCaseSheeet();

/** *****Systemic Examn for Casesheet ******** */
$scope.getSystemicExaminationForCaseSheeet=function(){
	 var data = {
			 organizationId:$scope.organizationId,
			 patientId:$scope.encounterDetailsObj.patientId,
			 typeId:2
	 };
	var URI = BASE_URL + ROOT_URL
	+ "/api/opd/getListOfSystemsAndObservationForCaseSheet";
   GenericServiceParamHeader
       .serviceAction(METHOD_POST, URI,
               data,
               NOTIFICATION_MSG_STATUS_FALSE)
       .then(
               function(response) {
                   console.log(response);
                   if (response.data.status == "success")
                       $scope.getSavedListOfSystemicExam = response.data.listObject;
                   angular.forEach($scope.getSavedListOfSystemicExam, function(value,key) {
                   	$scope.sysDescriptiveExamination=value.descriptiveExamination;
                   });
                   console.log("getSavedListOfSystemicExam", $scope.getSavedListOfSystemicExam);
               });
   }
$scope.getSystemicExaminationForCaseSheeet();

/** ************************************************** */ 

			/**********FOR MEDICAL HISTORY TAB START***********/
			
			$scope.medicalCurrentListdata = 3;
			$scope.showListMedical = function(value){
				if(value == 1){
					$scope.medicalAllList = 1;
				}
				if(value == 2){
					$scope.medicalResolvedList = 2;
					$scope.medicalAllList = '';
					$scope.medicalCurrentListdata = '';
				}
				if(value == 3){
					$scope.medicalCurrentListdata = 3;
					$scope.medicalResolvedList = '';
				}
			}

			$scope.noSignificantPersonal = 'N';
			$scope.noSignificantFamiliy = 'N';
			$scope.noSignificantSurgical = 'N';
			$scope.noSignificantMedical = 'N';
			$scope.noSignificantTreatment = 'N';
			$scope.noSignificantTravel ="N";
			$scope.noSignificantVaccination ="N";
			$scope.noSignificantMedicationHist ="N";
			
			$scope.getMedicalHistory=function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};

				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
					created_date:$scope.dt
				};
											
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentPatientMedicalHistoryDetailsForCoversheet";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI,{},NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,
				headerObj).then(function(response) {
					console.log(response);
													
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.medicalHistoryCurrentList = response.data.listObject;								
						$scope.medicalHistoryCurrentListDoctor = [];
					if($scope.roleId == '2')
					{			
						angular.forEach(response.data.listObject, function(value,key) {									
							if(value.roleId == 3 &&	value.isReviewedFlag == 'N')
							{
								var data = {};
								data.isNoSignificantStatus = value.isNoSignificantStatus;
								data.remark = value.remark;
								data.isReviewedFlag = 'N',
								data.isEnterInError='N',
								data.diagnosisId = value.diagnosisId,
								data.since = value.since,
								data.duration = value.duration,
								data.patientMedicalHistoryId  = value.patientMedicalHistoryId;
								$scope.medicalHistoryCurrentListDoctor.push(data);
							}
						});
					}								
				});
			}
			$scope.getMedicalHistory();
										
			/************FOR MEDICAL HISTORY SEARCH************/
			$scope.searchGroupList = function(keyword,index) {
				$scope.DiagnosisByGenderList=[];
													
				var data = {
					organizationId : $scope.organizationId,
					diagnosisName : keyword,
					genderId:$scope.patientDetails.genderId
				};
													
				console.log(data);
				$scope.varOr = "|";
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/opd/getDiagnosisByGenderId";									
				return GenericService.serviceAction("POST", URI, data).then(
				function(response) {
					console.log(response);
					$rootScope.stopLoader();
					if (response.data.status == "success")
						$scope.DiagnosisByGenderList = response.data.listObject;
					if($scope.DiagnosisByGenderList.length == 0){
						$scope.flagDigByGen=true;
						var newArr = {};
						newArr.diagnosisName="Diagnosis not applicable";
						newArr.diagnosisId='';
						$scope.DiagnosisByGenderList.push(newArr);
						$scope.varOr="";
						return response.data.listObject= $scope.DiagnosisByGenderList;
					} 
					else{
						$scope.flagDigByGen=false;
					}
					return response.data.listObject;														
				});
			}

			// Selected TypeHead on Medical History
			$scope.selectDignosisIdForMedical = function(info, model, label, ind) {
				var ind = 0;
				$scope.historyMedicalList[ind].diagnosisId = info.diagnosisId;
				$scope.medicalHistoryConfirm();	
			}

			$scope.medicalHistoryConfirm = function(){
				$scope.CheckNosignMedical = true;
				if($scope.noSignificantMedical =="N"){
						angular.forEach($scope.medicalHistoryCurrentList, function(value, key) {
						if (value.isNoSignificantStatus == 'Y') {
							$scope.CheckNosignMedical = false;
						}
					});
				}
				if($scope.CheckNosignMedical){
					$scope.saveHistoryMedical();
				}else{
					$("#CheckNosignMedical").show();
				}
			}

			$scope.closeMedicalHistory = function(){
				$("#CheckNosignMedical").hide();
			}

			$scope.saveHistoryMedical = function(){
				if ($scope.noSignificantMedical == 'Y') {
					$scope.perFlg = false;
					var count = 0;
					angular.forEach($scope.medicalHistoryCurrentList,function(value, key) {
						if (value.isNoSignificantStatus == 'Y') {
							growl.warning( "You have already saved.!!!",{
								title : 'Warning!'
							});
							$scope.historySurgicalList = [ {
								isNoSignificantStatus : 'N'
							} ];
							$scope.perFlg = true;
							return false;
						} 
						else {
							$scope.historyMedicalList = [ {
								isNoSignificantStatus : 'Y',
								diagnosisId : '',
								since : "",
								duration : "",
								remark : "",
								isEnterInError : "N",
								isReviewedFlag : "N",
								diagnosisNameText:'',
								isDiagnosisName : 'N',
								diagnosisCode : "",
							} ];
							count++;
						}
						if ($scope.medicalHistoryCurrentList.length == count) {
							$scope.saveHistoryMedicalFunction();
						}
					});
					if ($scope.noSignificantMedical == 'Y' && $scope.medicalHistoryCurrentList.length == 0) {
						$scope.historyMedicalList = [ {
							isNoSignificantStatus : 'Y',
							diagnosisId : '',
							since : "",
							duration : "",
							remark : "",
							isEnterInError : "N",
							isReviewedFlag : "N",
							diagnosisNameText:'',
							isDiagnosisName : 'N',
							diagnosisCode : "",
						} ];
						$scope.saveHistoryMedicalFunction();
					} else if ($scope.medicalHistoryCurrentList.length == 0) {
						$scope.historyMedicalList = [ {
							isNoSignificantStatus : '',
							diagnosisId : '',
							since : "",
							duration : "",
							remark : "",
							isEnterInError : "N",
							isReviewedFlag : "N",
							diagnosisNameText:'',
							isDiagnosisName : 'N',
							diagnosisCode : "",
						} ];
						$scope.saveHistoryMedicalFunction();
					}
				} else {
					$scope.saveHistoryMedicalFunction();
				}						
			}

			$scope.validateMedicalHisFlag=true;
			$scope.saveHistoryMedicalFunction = function()
			{
				$rootScope.startLoader();
				var data = {					
					created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:$scope.userId,
					created_by:$scope.userId,
					status:'A',
					encounterId : $scope.encounterDetailsObj.encounterId,
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId
				};
				$scope.medicalHistoryCurrentListDoctor=[];
				$scope.validateMedicalHisFlag=true;	

				if($scope.roleId == 3)
				{
										
					console.log('ds',$scope.medicalHistoryCurrentListDoctor);
					// angular.extend($scope.dataListComplaints,$scope.complaintsPatientListDoctor);
					$scope.medicalHistoryCurrentListDoctor12=[];
					angular.forEach($scope.historyMedicalList, function(value , key) 
					{
						if (value.isNoSignificantStatus == 'N') {							
							if(value.diagnosisId > 0 || value.diagnosisNameText != ""){
								var dData = {
									diagnosisId : value.diagnosisId,
									since : value.since,
									remark : value.remark,
									isEnterInError : value.isEnterInError,
									isReviewedFlag : value.isReviewedFlag,
									duration : value.duration,
									isNoSignificantStatus : value.isNoSignificantStatus,
									diagnosisNameText: value.diagnosisNameText,
									isDiagnosisName : value.isDiagnosisName,
									diagnosisCode : value.diagnosisCode
								}
								$scope.medicalHistoryCurrentListDoctor.push(dData);
								data.listOfPatientMedicalHistoryDto = $scope.medicalHistoryCurrentListDoctor;
							}
							else 
							{
								growl.warning( "Please Enter Medical History",
								{
									title : 'Warning!'
								});
								$scope.validateMedicalHisFlag=false;
								$rootScope.stopLoader();
							}
						} else if (value.isNoSignificantStatus == 'Y') {
							var dData = {
								diagnosisId : value.diagnosisId,
								since : value.since,
								remark : value.remark,
								isEnterInError : value.isEnterInError,
								isReviewedFlag : value.isReviewedFlag,
								duration : value.duration,
								isNoSignificantStatus : value.isNoSignificantStatus,
								diagnosisNameText: value.diagnosisNameText,
								isDiagnosisName : value.isDiagnosisName,
								diagnosisCode : value.diagnosisCode
							}
							$scope.medicalHistoryCurrentListDoctor.push(dData);
							data.listOfPatientMedicalHistoryDto = $scope.medicalHistoryCurrentListDoctor;
						} else {
							growl.warning( "Please Enter Medical History",
							{
								title : 'Warning!'
							});
							$scope.validateMedicalHisFlag=false;
							$rootScope.stopLoader();
						}
					});
				
										
					if($scope.CheckNosignMedical == false){
						angular.forEach($scope.medicalHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								var data = {};
								data.isNoSignificantStatus ='Y',
								data.diagnosisId ='',
								data.since ="",
								data.duration ="",
								data.remark ="",
								data.patientMedicalHistoryId = value.patientMedicalHistoryId,
								data.isEnterInError ="N",
								data.isReviewedFlag ="N",
								data.diagnosisNameText= '',
								data.isDiagnosisName ="N",
								data.diagnosisCode= '',
								$scope.medicalHistoryCurrentListDoctor12.push(data);
							}
						});
						
						if($scope.medicalHistoryCurrentListDoctor12.length > 0){
							data.listOfPatientMedicalHistoryDto =[];
							data.listOfPatientMedicalHistoryDto = $scope.medicalHistoryCurrentListDoctor.concat($scope.medicalHistoryCurrentListDoctor12);
						}				
					}
										
					if($scope.validateMedicalHisFlag){
						var paramObj = {};
						var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL + SAVEPATIENTMEDICALHISTORYDETAILS;

						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,
						headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$("#CheckNosignMedical").hide();
								$scope.historyMedicalList.diagnosisName="";
								$scope.historyMedicalList.diagnosisId="";
								$scope.noSignificantMedical='N';
								$scope.getMedicalHistory();
								$scope.info.diagnosisName="";
								$scope.init();
								// $scope.setSecondTabLevel(5);
								/*
								* var datalistMedical=
								* $scope.historyMedicalList; for
								* (var i=0 ; i<
								* datalistMedical.length; i++){
								* $scope.historyMedicalList[i]=""; }
								*/
								return response.data.listObject;
						});
					}
				} 
				else 
				{
					console.log('ds',$scope.medicalHistoryCurrentListDoctor);
					$scope.medicalHistoryCurrentListDoctor12=[];
					angular.forEach($scope.historyMedicalList, function(value , key) 
					{
						if (value.isNoSignificantStatus == 'N') {					
							if(value.diagnosisId > 0 || value.diagnosisNameText != ""){
								var dData = {
									diagnosisId : value.diagnosisId,
									since : value.since,
									remark : value.remark,
									isEnterInError : value.isEnterInError,
									isReviewedFlag : value.isReviewedFlag,
									duration : value.duration,
									isNoSignificantStatus : value.isNoSignificantStatus,
									diagnosisNameText: value.diagnosisNameText,
									isDiagnosisName : value.isDiagnosisName,
									diagnosisCode : value.diagnosisCode
								}
								$scope.medicalHistoryCurrentListDoctor.push(dData);
								data.listOfPatientMedicalHistoryDto = $scope.medicalHistoryCurrentListDoctor;
							}else {
								growl.warning( "Please Enter Medical History",
								{
									title : 'Warning!'
								});
								$scope.validateMedicalHisFlag=false;
								$rootScope.stopLoader();
							}
						} else if (value.isNoSignificantStatus == 'Y') {
							var dData = {
								diagnosisId : value.diagnosisId,
								since : value.since,
								remark : value.remark,
								isEnterInError : value.isEnterInError,
								isReviewedFlag : value.isReviewedFlag,
								duration : value.duration,
								isNoSignificantStatus : value.isNoSignificantStatus,
								diagnosisNameText: value.diagnosisNameText,
								isDiagnosisName : value.isDiagnosisName,
								diagnosisCode : value.diagnosisCode
							}
							$scope.medicalHistoryCurrentListDoctor.push(dData);
							data.listOfPatientMedicalHistoryDto = $scope.medicalHistoryCurrentListDoctor;
						} else {
							growl.warning( "Please Enter Medical History",
							{
								title : 'Warning!'
							});
							$scope.validateMedicalHisFlag=false;
							$rootScope.stopLoader();
						}
					});
								
					if($scope.CheckNosignMedical == false){
						angular.forEach($scope.medicalHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								var data = {};
								data.isNoSignificantStatus ='Y',
								data.diagnosisId ='',
								data.since ="",
								data.duration ="",
								data.remark ="",
								data.patientMedicalHistoryId = value.patientMedicalHistoryId,
								data.isEnterInError ="N",
								data.isReviewedFlag ="N",
								data.diagnosisNameText= '',
								data.isDiagnosisName ="N",
								data.diagnosisCode= '',
								$scope.medicalHistoryCurrentListDoctor12.push(data);
							}
						});
									
						if($scope.medicalHistoryCurrentListDoctor12.length > 0){
							data.listOfPatientMedicalHistoryDto =[];
							data.listOfPatientMedicalHistoryDto = $scope.medicalHistoryCurrentListDoctor.concat($scope.medicalHistoryCurrentListDoctor12);
						}	
					}
								
					if($scope.validateMedicalHisFlag){
						var paramObj = {};
						var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL + SAVEPATIENTMEDICALHISTORYDETAILS;

						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,
						headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$("#CheckNosignMedical").hide();
								$scope.noSignificantMedical='N';
								$scope.getMedicalHistory();
								$scope.info.diagnosisName="";
								$scope.init();
								// $scope.historyMedicalList = [];
								// $scope.setSecondTabLevel(5);
								/*
								* var datalistMedical=
								* $scope.historyMedicalList; for (var i=0 ;
								* i< datalistMedical.length; i++){
								* $scope.historyMedicalList[i]=""; }
								*/
								// return response.data.listObject;
						});
					}
				}
			}

			$scope.checkedRadioMedi = function(info,idx,val)
			{
				// alert(idx);
				if(val =='Day')
				{
					$('#med_day_id_'+idx).prop('checked', true);
					$scope.showUpdateMedicalHistory(info, val);
				}
				else if(val == 'Month')
				{
					$('#med_day_id_'+idx).prop('checked', true);
					$scope.showUpdateMedicalHistory(info, val);
				}
				else
				{
					$('#med_day_id_'+idx).prop('checked', true);
					$scope.showUpdateMedicalHistory(info, val);
				}
			}

			$scope.showUpdateMedicalHistory = function(item, val)
			{
				console.log(item);
				$scope.medicalUpdateObj = {
					diagnosisId : item.diagnosisId,
					since : item.since,
					remark : item.remark,
					duration : val != undefined ? val : item.duration,
					isEnterInError : item.isEnterInError,
					isReviewedFlag : item.isReviewedFlag,
					patientMedicalHistoryId : item.patientMedicalHistoryId,
					isNoSignificantStatus : item.isNoSignificantStatus,
					diagnosisName:item.diagnosisName
				};
				$scope.itemRoleId = item.roleId;
				$scope.updateMedicalHistory();
			}

			$scope.updateMedicalHistory = function()
			{	
				$scope.historyMedicalList = [];
				$scope.historyMedicalList.push($scope.medicalUpdateObj);
				var data = {
					created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:$scope.userId,
					created_by:$scope.userId,
					status:'A',
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId,
					encounterId: $scope.encounterDetailsObj.encounterId,
					listOfPatientMedicalHistoryDto:$scope.historyMedicalList,			
				};
				console.log(data);
				// return false;
				var paramObj = {};
				var headers={
					'organization_id': $scope.organizationId,
					'unit_id':$scope.unitId,
					'Content-Type':'application/json'
				};
				// return false;
				var URI = BASE_URL + ROOT_URL + UPDATEPATIENTMEDICALHISTORYDETAILS;
				GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_TRUE,
				paramObj,headers).then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.init();
						$scope.getMedicalHistory();
						// $scope.historyMedicalList = [];
						// $scope.setSecondTabLevel(5);
						// return response.data.listObject;
				});
			}

			$scope.removeMedicalHistory = function(item){	
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				var data ={
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:$scope.userId,
					patientMedicalHistoryId : item.patientMedicalHistoryId,
				};
				var URI = BASE_URL + ROOT_URL + "/api/opd/updateStatusRemoveMedicalHistory";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$rootScope.stopLoader();
						growl.success(response.data.message,
						{
							title : 'Success!'
						});
						
						$scope.getMedicalHistory();
						$scope.init();
						//  $scope.setSecondTabLevel(5);
					} 
					else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.warning(response.data.message,
						{
							title : 'Warning!'
						});
					}
				});
			}

			$scope.getFrequentlyUsedDiagnosis = function(){
				var data = {
					unitId: $scope.unitId,
					organizationId:$scope.organizationId,
				};
				$scope.frequentlyUsedDiagnosis=[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequentlyUsedDiagnosisList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					if(response.data.status == "success"){
						$scope.frequentlyUsedDiagnosis = response.data.listObject;
						console.log("diag", $scope.frequentlyUsedDiagnosis);
					}
				});
			}
			$scope.getFrequentlyUsedDiagnosis();

			$scope.getPatientDiagnosisForMedicalHistory = function() {
				var data = {};
				$scope.DateFor = moment(new Date(),"DD-MM-YYYY").subtract(1, 'days').toDate()
								
				var headerObjDia = {
					'unit_id' : $scope.unitId,
					'organization_id' : $scope.organizationId,
					'patient_id' : $scope.encounterDetailsObj.patientId,
					'createdDate' :moment(new Date()).format('YYYY-MM-DD'),
					'Content-Type' : 'application/json',
				};
				$scope.patientDiagnosisListForMedicalHistory =[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientDiagnosisByPatientIdForMedicalHistory";
				GenericServiceParamHeader .serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE,data, headerObjDia)
				.then( function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.patientDiagnosisListForMedicalHistory = response.data.listObject;
						angular.forEach($scope.patientDiagnosisListForMedicalHistory,function(value,index)
						{
							value.resolvedDate =moment(value.resolvedDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
						});
						console.log( "patientDiagnosisListForMedicalHistory", $scope.patientDiagnosisListForMedicalHistory);
				});
			}
			$scope.getPatientDiagnosisForMedicalHistory();

			$scope.getOldMedicalHistory = function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};

				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
					created_date:$scope.dt
				};
									
				$scope.medicalHistoryOldList=[];
				var URI = BASE_URL + ROOT_URL + GETOLDPATIENTMEDICALHISTORYDETAILS;
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.medicalHistoryOldList = response.data.listObject;
				});
			}
			$scope.getOldMedicalHistory();
							

			/*******************************FOR SURGICAL HISTORY TAB START*****************************************/

			$scope.getSurgicalHistory = function() {
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};
			
				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
					created_date : $scope.dt
				};
				var data = {};
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentPatientSurgicalHistoryForCoversheet";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,
				paramObjForHistList, headerObj).then(function(response) {
					console.log(response);
			
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.surgicalHistoryCurrentList = response.data.listObject;
						angular.forEach($scope.surgicalHistoryCurrentList,function(value,index)
						{
							value.createdDate = moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
						});
						console.log("Nonformulray",$scope.surgicalHistoryCurrentList);
						$scope.surgicalHistoryCurrentListDoctor = [];
					if ($scope.roleId == '2') {
						angular.forEach(response.data.listObject,function(value,key){
							if (value.roleId == 3 && value.isReviewedFlag == 'N') 
							{
								var data = {};
								data.isNoSignificantStatus = value.isNoSignificantStatus;
								data.remark = value.remark;
								data.isReviewedFlag = 'N',
								data.isEnterInErrorStatus = 'N',
								data.surgenName = value.surgenName,
								data.surgeryId = value.surgeryId,
								data.month = value.month,
								data.year = value.year,
								data.surgicalHistoryDetailsId = value.surgicalHistoryDetailsId;
								$scope.surgicalHistoryCurrentListDoctor.push(data);
							}
						});
					}
				});
			}
			$scope.getSurgicalHistory();
										
			/**********FOR SEARCH SURGERY******************/
			$scope.searchGroupSurgery = function(keyword) {								
				var data = {};
				var paramObj = {
					surgery_name : keyword
				};
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				}
				console.log(data);
				var URI = BASE_URL + ROOT_URL + GETSURGERYMASTERLISTAUTOFILLSERACH;							
				return GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj).then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					$scope.checkListSurgery=[];
					if (response.data.status == "success")
						$scope.checkListSurgery = response.data.listObject;
					if($scope.checkListSurgery.length == 0){												
						growl.warning("The surgery is not available in the hospital and please continue to record by selecting the non-surgical option",
						{
							title : 'warning!'
						});
						$rootScope.stopLoader();
						return false;
					}
					return response.data.listObject;
				});
			}

			// Selected TypeHead on Complaints
			$scope.selectSurgeryId = function(info, model,label,ind) {
				console.log(info);
				if(info != undefined || ($scope.surgeryName != undefined && $scope.surgeryName != null && $scope.surgeryName !="")){
				var ind = 0;
				if(info){
					if(info.surgery_id){
						$scope.historySurgicalList[ind].surgeryId = info.surgery_id;
					}
					else
					{
						$scope.historySurgicalList[ind].surgeryId = info.surgeryId;
					}
					$scope.historySurgicalList[ind].surgeryName = info.surgeryName;
				}
				else{
					$scope.historySurgicalList[ind].surgeryName = $scope.surgeryName;
					$scope.historySurgicalList[ind].nonFormulary = true;
				}
				$scope.surgicalHistoryConfirm();
			}else{
				growl.warning("Please enter surgery.");
				return false;
			}
			}

			// Search Surgen with Typehead
			$scope.searchGroupSurgen = function(keyword) {
				var data = {};
				var paramObj = {
					doctor_name : keyword
				};
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				}
				console.log(data);
				var URI = BASE_URL + ROOT_URL + GETDOCTORMASTERLISTAUTOFILLSERACH;
				return GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,
				headerObj).then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						return response.data.listObject;
				});
			}			

			$scope.validateSurHisRow =function(idx){
				$scope.checkListSur1=[];
				$scope.checkListSur2=[];
				angular.forEach($scope.historySurgicalList,function(value,index){
					if(idx == index){
						var newArr ={};
						newArr.surgeryId=value.surgeryId;
						newArr.surgenName=value.surgenName;
						newArr.month=value.month;
						newArr.year=value.year;
						newArr.remark=value.remark;
						$scope.checkListSur1.push(newArr);
					}
				});
				angular.forEach($scope.historySurgicalList,function(value,index){
					if(idx != index){
						var newArr ={};
						newArr.surgeryId=value.surgeryId;
						newArr.surgenName=value.surgenName;
						newArr.month=value.month;
						newArr.year=value.year;
						newArr.remark=value.remark;
						$scope.checkListSur2.push(newArr);
					}
				});
												
				angular.forEach($scope.checkListSur1, function(value , key) {
					if($scope.historySurgicalList.length > 1){
						if($scope.historySurgicalList!=undefined && $scope.historySurgicalList.length>0){
							var chkStatus= $scope.checkListSur2.filter(function(item)
							{ 
								return (item.surgeryId==value.surgeryId && 
									item.surgenName==value.surgenName &&
									item.year==value.year &&
									item.month==value.month 
								);
							})
							if(chkStatus.length>0){
								growl.warning("Surgical history added.",
								{
									title : 'Warning!'
								});
								$scope.historySurgicalList[idx].surgeryId="";
								$scope.historySurgicalList[idx].diagnosisName="";
								$scope.historySurgicalList[idx].surgenName="";
								$scope.historySurgicalList[idx].year="";
								$scope.historySurgicalList[idx].month="";
								$rootScope.stopLoader();
								return false;
							}
						}
					}
				});
			}

			$scope.getMontListByYear = function(year){										 
				var y = new Date().getFullYear()
				if(year== y){
					var m = new Date().getMonth();
				}
				if(year == parseInt($scope.birthYear)){
					var m = parseInt($scope.birthMonth); 
				}
				var checkfl = false;
				$scope.months = [];
				$scope.monthsTry = [];
				$scope.monthsTry.push({value:1, text:'January'});
				$scope.monthsTry.push({value:2, text:'February'});
				$scope.monthsTry.push({value:3, text:'March'});
				$scope.monthsTry.push({value:4, text:'April'});
				$scope.monthsTry.push({value:5, text:'May'});
				$scope.monthsTry.push({value:6, text:'June'});
				$scope.monthsTry.push({value:7, text:'July'});
				$scope.monthsTry.push({value:8, text:'August'});
				$scope.monthsTry.push({value:9, text:'September'});
				$scope.monthsTry.push({value:10, text:'October'});
				$scope.monthsTry.push({value:11, text:'November'});
				$scope.monthsTry.push({value:12, text:'December'});
				
				angular.forEach($scope.monthsTry,function(value,index){                                  
					if(value.value <= m+1){
						var newArr = {};
						newArr.value = value.value;
						newArr.text = value.text;
						$scope.months.push(newArr);
						checkfl = true;
					}
				});
				if(year == parseInt($scope.birthYear)){
					$scope.months=[];
					var m = parseInt($scope.birthMonth); 
					angular.forEach($scope.monthsTry,function(value,index){
						if(value.value >= m){
							var newArr = {};
							newArr.value = value.value;
							newArr.text = value.text;
							$scope.months.push(newArr);
							checkfl = true;
						}
					});
				}
				if(!checkfl){
					$scope.months = $scope.monthsTry;
				}
				console.log("$scope.months-----------",$scope.months);                        
			}

			$scope.surgicalHistoryConfirm = function(){
				$scope.CheckNosignSurgical = true;
				if($scope.noSignificantSurgical =="N"){
						angular.forEach($scope.surgicalHistoryCurrentList, function(value, key) {
						if (value.isNoSignificantStatus == 'Y') {
							$scope.CheckNosignSurgical = false;
						}
					});
				}
				if($scope.CheckNosignSurgical){
					$scope.saveHistorySurgical();
				}else{
					$("#CheckNosignSurgical").show();
				}
			}

			$scope.closeSurgicalHistory = function(){
				$("#CheckNosignSurgical").hide();
			}

			$scope.saveHistorySurgical = function()
			{
				if($scope.noSignificantSurgical == 'Y')
				{
					var count = 0;
					angular.forEach($scope.surgicalHistoryCurrentList, function(value , key) {	
						if(value.isNoSignificantStatus == 'Y')
						{
							growl.warning("You have already saved.!!!",
							{
								title : 'Warning!'
							});
							return false;
						}
						else
						{
							$scope.historySurgicalList = [{
								isNoSignificantStatus:'Y', 
								surgeryId:'',
								surgenName:"",
								month:"", 
								year:"", 
								remark:"",
								isEnterInErrorStatus:'N',
								isReviewedFlag:'N',
							}];
							count++;
						}
					
						if($scope.surgicalHistoryCurrentList.length==count)
						{
							$scope.saveHistorySurgicalFunction();
						}
					});
					
					if( $scope.noSignificantSurgical== 'Y' && $scope.surgicalHistoryCurrentList.length==0)
					{
						$scope.historySurgicalList = [{
							isNoSignificantStatus:'Y', 
							surgeryId:'',
							surgenName:"", 
							month:"",
							year:"", 
							remark:"",
							isEnterInErrorStatus:'N',
							isReviewedFlag:'N',
						}];
						$scope.saveHistorySurgicalFunction();
					}
					else if ( $scope.surgicalHistoryCurrentList.length==0){
						$scope.historySurgicalList = [{
							isNoSignificantStatus:'', 
							surgeryId:'',
							surgenName:"", 
							month:"",
							year:"", 
							remark:"",
							isEnterInErrorStatus:'N',
							isReviewedFlag:'N',
						}];
						$scope.saveHistorySurgicalFunction();	
					}
				}
				else{
					$scope.saveHistorySurgicalFunction();
				}
			}

			$scope.validateSurHisFlag=true;
			$scope.saveHistorySurgicalFunction = function()
			{
				$rootScope.startLoader();
				var data = {		
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					createdBy:$scope.userId,
					status:'A',
					encounterId : $scope.encounterDetailsObj.encounterId,
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId
				};
							
				if($scope.roleId == 3)
				{
					console.log('ds',$scope.surgicalHistoryCurrentListDoctor);
					$scope.validateSurHisFlag=true;
					$scope.surgicalHistoryCurrentListDoctor1=[];
					$scope.surgicalHistoryCurrentListDoctor12=[];						
					angular.forEach($scope.historySurgicalList, function(value , key) {
						if($scope.historySurgicalList!=undefined && $scope.historySurgicalList.length>0){
							var chkStatus= $scope.surgicalHistoryCurrentList.filter(function(item)
							{ 
								return (item.surgeryId==value.surgeryId && 
									item.surgenName== value.surgenName &&
									item.year == value.year && 
									item.month==value.month);
							})
							if(chkStatus.length>0){
								growl.warning("Already exist",
								{
									title : 'Warning!'
								});
								$scope.validateSurHisFlag=false;
								$rootScope.stopLoader();
								return false;
							}
						}
												
						if( value.nonFormulary == true && value.surgenName != "" && value.surgeryName !="" ){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								surgeryId :0,
								surgeryName:value.diagnosisName,
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.surgicalHistoryCurrentListDoctor1.push(dData);
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1;
						} 
						else if (value.surgeryId > 0 && value.surgenName != ""){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								surgeryId :value.surgeryId,
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.surgicalHistoryCurrentListDoctor1.push(dData);
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1;							
						} 
						else if (value.isNoSignificantStatus == 'Y'){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								surgeryId :0,
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.surgicalHistoryCurrentListDoctor1.push(dData);
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1;													
						}
						else{
							growl.warning("Please Enter Surgical History",
							{
								title : 'Warning!'
							});
							$scope.validateSurHisFlag=false;
							$rootScope.stopLoader();
							return false;
						}
					});
											
					if($scope.CheckNosignSurgical == false){
						angular.forEach($scope.surgicalHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								var data = {};
								data.isNoSignificantStatus='Y', 
								data.surgeryId= 0,
								data.surgenName="",
								data.month="", 
								data.year="", 
								data.remark="",
								data.isEnterInErrorStatus='N',
								data.isReviewedFlag='N',
								data.surgicalHistoryDetailsId = value.surgicalHistoryDetailsId,
								$scope.surgicalHistoryCurrentListDoctor12.push(data);
							}
						});
												
						if($scope.surgicalHistoryCurrentListDoctor12.length > 0){
							data.listOfPatientSurgicalHistoryDto =[];
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1.concat($scope.surgicalHistoryCurrentListDoctor12);
						}
					}
											
					if($scope.validateSurHisFlag){
						var paramObj = {};
						var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL + SAVEPATIENTSURGICALHISTORY;
						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,
						NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")	                 
								$scope.noSignificantSurgical="";
								$("#CheckNosignSurgical").hide();
								$scope.init();
								$scope.getSurgicalHistory();
								$scope.getSugicalProcedureList();
								// $scope.setSecondTabLevel(3);
								return response.data.listObject;
						});
					}
				} 
				else {
					console.log('ds',$scope.surgicalHistoryCurrentListDoctor);
					$scope.validateSurHisFlag=true;
					$scope.surgicalHistoryCurrentListDoctor1=[];
					$scope.surgicalHistoryCurrentListDoctor12=[];
					angular.forEach($scope.historySurgicalList, function(value , key) {							
						if($scope.historySurgicalList!=undefined && $scope.historySurgicalList.length>0){
							var chkStatus= $scope.surgicalHistoryCurrentList.filter(function(item)
							{ 
								return (item.surgeryId==value.surgeryId && 
								item.surgenName== value.surgenName &&
								item.year == value.year && 
								item.month==value.month);
							})
							if(chkStatus.length>0){
								growl.warning("Already exist",
								{
									title : 'Warning!'
								});
								$scope.validateSurHisFlag=false;
								$rootScope.stopLoader();
								return false;
							}
						}
						if( value.nonFormulary == true && value.surgeryName !="" ){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								surgeryId :0,
								surgeryName:value.surgeryName,
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,	
							}
							$scope.surgicalHistoryCurrentListDoctor1.push(dData);
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1;
						} 
						else if (value.surgeryId > 0)
						{					
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								surgeryId :value.surgeryId,
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.surgicalHistoryCurrentListDoctor1.push(dData);
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1;											
						} 
						else if (value.isNoSignificantStatus == 'Y'){						
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								surgeryId :0,
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.surgicalHistoryCurrentListDoctor1.push(dData);
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1;												
						}
						else{
							growl.warning("Please Enter Surgical History",
							{
								title : 'Warning!'
							});
							$scope.validateSurHisFlag=false;
							$rootScope.stopLoader();
							return false;
						}
					});
					if($scope.CheckNosignSurgical == false){
						angular.forEach($scope.surgicalHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								var data = {};
								data.isNoSignificantStatus='Y', 
								data.surgeryId= 0,
								data.surgenName="",
								data.month="", 
								data.year="", 
								data.remark="",
								data.isEnterInErrorStatus='N',
								data.isReviewedFlag='N',
								data.surgicalHistoryDetailsId = value.surgicalHistoryDetailsId,
								$scope.surgicalHistoryCurrentListDoctor12.push(data);
							}
						});							
						if($scope.surgicalHistoryCurrentListDoctor12.length > 0){
							data.listOfPatientSurgicalHistoryDto =[];
							data.listOfPatientSurgicalHistoryDto = $scope.surgicalHistoryCurrentListDoctor1.concat($scope.surgicalHistoryCurrentListDoctor12);
						}						
					}						
					if($scope.validateSurHisFlag){
						var paramObj = {};
						var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL + SAVEPATIENTSURGICALHISTORY;
						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,
						NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.noSignificantSurgical="";
								$scope.surgeryName = "";
								$("#CheckNosignSurgical").hide();
								$scope.getSurgicalHistory();
								$scope.getSugicalProcedureList();
								$scope.init();
								// $scope.setSecondTabLevel(3);
								return response.data.listObject;
						});
					}
				}
			}

			$scope.showUpdateSurgicalHistory = function(item)
			{
				console.log(item);
				$scope.surgicalUpdateObj = {										
					surgeryId : item.surgeryId,
					surgenName : item.surgenName,
					remark : item.remark,
					month : item.month,
					year : item.year,
					isEnterInErrorStatus : item.isEnterInErrorStatus,
					isReviewedFlag : item.isReviewedFlag,
					surgicalHistoryDetailsId : item.surgicalHistoryDetailsId,
					isNoSignificantStatus : item.isNoSignificantStatus,
					//surgeryName:item.surgeryName,
					//surgeryName:item.surgeryNameFormulary
				};
				if(item.surgeryName != null){
					$scope.surgicalUpdateObj.surgeryName=item.surgeryName
				}
				else{
					$scope.surgicalUpdateObj.surgeryName=item.surgeryNameFormulary
				}
				$scope.itemRoleId = item.roleId;
				$scope.updateSurgicalHistory();
			}

			$scope.getSugicalProcedureList = function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					organizationId : $scope.organizationId
				};
				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
				};					
				$scope.sugicalProcedureList=[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCompletedProcedureListForSurgicalHistory";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
				NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj).then(
				function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.sugicalProcedureList = response.data.listObject;
				});
			}
			$scope.getSugicalProcedureList();

			$scope.updateSurgicalHistory = function()
			{
				$scope.historySurgicalList = [];
				$scope.historySurgicalList.push($scope.surgicalUpdateObj);
				var data = {
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					createdBy:$scope.userId,
					status:'A',
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId,
					encounterId: $scope.encounterDetailsObj.encounterId,
					listOfPatientSurgicalHistoryDto:$scope.historySurgicalList
				};				
				console.log(data);
				// return false;
				var paramObj = {};
				var headers={
					'organization_id': $scope.organizationId,
					'unit_id':$scope.unitId,
					'Content-Type':'application/json'
				};
				// return false;
				var URI = BASE_URL + ROOT_URL + UPDATEPATIENTSURGICALHISTORY;
				GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.init();
						$scope.getSurgicalHistory();
						$scope.getSugicalProcedureList();
						// $scope.setSecondTabLevel(3);
						return response.data.listObject;
				});
			}
							
			$scope.updateStatusForAcronymsSurgical = function(item)
			{						
				$scope.historySurgicalList = [];
				var perObj = {						
					surgeryId:item.surgeryId,
					surgenName:item.surgenName,
					remark : item.remark,
					month : item.month,
					year : item.year,
					isEnterInErrorStatus : item.isEnterInErrorStatus,
					isReviewedFlag : item.isReviewedFlag,
					surgicalHistoryDetailsId :item.surgicalHistoryDetailsId,
					isNoSignificantStatus : item.isNoSignificantStatus						
				};
				var paramObj = {};				
				$scope.historySurgicalList.push(perObj);
				var headers={
					'organization_id': $scope.organizationId,
					'unit_id':$scope.unitId,
					'Content-Type':'application/json'
				};				
				var data = {						
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					createdBy:$scope.userId,
					status:'A',
					encounterId : $scope.encounterDetailsObj.encounterId,
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId,
					listOfPatientSurgicalHistoryDto : $scope.historySurgicalList
				}						
				console.log(data);
				// return false;
				var URI = BASE_URL + ROOT_URL + UPDATEENTERERRORSTATUSPATIENTSURGICALHISTORY;
				GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.init();
						$scope.getSurgicalHistory();
						$scope.getSugicalProcedureList();
						// $scope.setSecondTabLevel(3);
						return response.data.listObject;
				});					
			}

			$scope.removeSurgicalHistory = function(item){	
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				var data ={
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					surgicalHistoryDetailsId : item.surgicalHistoryDetailsId,
				};
				var URI = BASE_URL + ROOT_URL + "/api/opd/updateStatusRemoveSurgicalHistory";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$rootScope.stopLoader();
						growl.success(response.data.message,
						{
							title : 'Success!'
						});
						$scope.init();
						$scope.getSurgicalHistory();
						$scope.getSugicalProcedureList();
						// $scope.setSecondTabLevel(3);
					} 
					else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.warning(response.data.message,
						{
							title : 'Warning!'
						});
					}
				});
			}

			$scope.getMostlyUsedSurgeryList = function(){
				var data = {
					unitId: $scope.unitId,
					organizationId:$scope.organizationId,
				};
				$scope.mostlyUsedSurgeryList=[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getMostUsedSurgicalHistory";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					if(response.data.status == "success"){
						$scope.mostlyUsedSurgeryList = response.data.listObject;
					}
				});
			}
			$scope.getMostlyUsedSurgeryList();

			$scope.getOldSurgicalHistory = function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};

				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
					created_date:$scope.dt
				};
								
				$scope.surgicalHistoryOldList=[];
				var URI = BASE_URL + ROOT_URL + GETOLDPATIENTSURGICALHISTORY;
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.surgicalHistoryOldList = response.data.listObject;
						angular.forEach($scope.surgicalHistoryOldList,function(value,index)
						{
							value.createdDate = moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
						});
				});
			}
			$scope.getOldSurgicalHistory();
			/*******************************FOR SURGICAL HISTORY TAB END*****************************************/

			/********************************FOR VACCINATION LIST START*****************************************/
										
			$scope.dateOptionsVaccine =

            {

                formatYear : 'yyyy',

                showWeeks : false,

                maxDate :new Date()

            };



            $scope.openDatePickerVaccine = function(index) {

                $scope.datepickerVaccine = {

                    opened : [ ],

                };      

                if($scope.VaccineFinalList != undefined){

                    angular.forEach($scope.VaccineFinalList, function(val) {

                        $scope.datepickerVaccine.opened.push(false);

                    });

                }

                for (var i = 0; i < $scope.VaccineFinalList.length; i++) {

                    if (i === index) {

                        $scope.datepickerVaccine.opened[i] = true;

                    } else {

                        $scope.datepickerVaccine.opened[i] = false;

                    }

                }

            };

			$scope.getAllActivevaccine = function()
			{
				$rootScope.startLoader();
				var patientId = $scope.encounterDetailsObj.patientId;
				var dateObj = moment(new Date()).format('YYYY-MM-DD');
				$scope.genderId = $scope.patientDetails.genderId;					  
				var URI = BASE_URL + ROOT_URL + "/api/billing/getActiveVaccinationMasterByGenderId/"+$scope.organizationId+"/"+$scope.genderId;
				GenericServiceParamHeader.serviceAction("GET", URI).then(function(response) {
					if (response.data.status == "success") {
						$scope.activevaccine = response.data.listObject;
						console.log("ffrefre>>>>>>>>>>",$scope.activevaccine);
					}
				});
			}
										    
			$scope.getAllActivevaccine();
					
			$scope.getVaccineData = function(){
				$scope.getAllActivevaccine();
				$timeout( function(){
					$rootScope.startLoader();
					var paramObjForVaccine = {                    				
						patientId:$scope.encounterDetailsObj.patientId,
						organizationId:$scope.organizationId,
						unitId : $scope.unitId				
					}	                    		
					console.log("QQQQ",paramObjForVaccine);
						                    		 
					var URI = BASE_URL + ROOT_URL+ "/api/opd/getListOfVaccineImmunizationMapper";
					GenericService.serviceAction("POST", URI, paramObjForVaccine).then(function(response){console.log(response);
					if (response.data.status == "success")
						$scope.VaccineFinalList = [];
						$scope.vaccineMapperListForCaseSheet=[];
						$scope.vaccineMapperList  = response.data.listObject;
						console.log("vaccineMapperList",response.data.listObject);
						console.log("activevaccine",$scope.activevaccine);
//						angular.forEach($scope.activevaccine, function(value , key) {
//							var checkFlag = false;
							angular.forEach($scope.vaccineMapperList, function(value1 , key1) {
								if(value1.isNoSignificantVaccination == 'N'){
									$scope.noSignificantVaccination='N';
								}
								else{
									$scope.noSignificantVaccination='Y';
								}                        	
//								if(value.vaccinationId == value1.vaccinationMasterId)
//								{
//									checkFlag = true;
									value1.genericName=value1.genericName;
									$scope.newArr = {};
									$scope.newArr.vaccinationId = value1.vaccinationId;
									$scope.newArr.name = value1.name;
									$scope.newArr.name1 = value1.name;
									$scope.newArr.itemId = value1.itemId;
									$scope.newArr.vaccineNameText = value1.vaccineNameText;
									$scope.newArr.listOfGenericIdWiseList1 = value1.listOfGenericIdWiseList;
									$scope.newArr.minAge = value1.minAge;
									$scope.newArr.minDay = value1.minDay;
									$scope.newArr.maxAge = value1.maxAge;
									$scope.newArr.maxDay = value1.maxDay;
									$scope.newArr.genericName = value1.genericName;
									$scope.newArr.doseDesc = value1.doseDesc;
									$scope.newArr.strengthDesc = value1.strengthDesc;
									$scope.newArr.units = value1.units;
									$scope.newArr.description = value1.description;
									$scope.newArr.siteName = value1.siteName;
									$scope.newArr.series = value1.series;
									$scope.newArr.abbreviation = value1.abbreviation;
									$scope.newArr.genericId =value1.genericId;
									$scope.newArr.location =value1.location;
									$scope.newArr.calculatedDateShow = value1.plannedDate ;
									$scope.newArr.calculatedDate =value1.calculatedDate;
									$scope.newArr.vaccineImmunMapperId = value1.vaccineImmunMapperId;
									$scope.newArr.plannedDate =moment( value1.plannedDate,"DD-MM-YYYY").toDate();
									$scope.newArr.dateOptionsAdm=
									{
										formatYear : 'yyyy',
										showWeeks : false,
										minDate :moment(value1.plannedDate,"DD-MM-YYYY").toDate(),
										maxDate : new Date(),
									};
									if(value1.actualDate != null)
									{
										$scope.newArr.actualDate =moment(value1.actualDate,"DD-MM-YYYY").toDate();
										$scope.newArr.dateOptionsVacc =
										{
											formatYear : 'yyyy',
											showWeeks : false,
											minDate : $scope.newArr.actualDate,
										};
									}
									else{			
										$scope.newArr.dateOptionsVacc =
										{
											formatYear : 'yyyy',
											showWeeks : false,
											minDate : $scope.newArr.plannedDate
										};
									}               			
									$scope.newArr.remarks = value1.remarks;
									$scope.newArr.vaccineFlagStatus = value1.vaccineFlagStatus;
									/*$scope.vaccinationNote =value1.vaccinationNote;*/
//									$scope.vaccineMapperListForCaseSheet.push(value1);	
//								}
								
									$scope.VaccineFinalList.push($scope.newArr);                      	
							});
//							if (checkFlag == true) {
								
//							} 
							/*else 
							{																					
								$scope.dateOptionsAdm=[];
								$scope.dateOptionsVacc=[];													 
								var newArr = {};
								newArr.vaccinationId = value.vaccinationId;
								newArr.genericId =value.genericId;
								newArr.name = value.name;
								newArr.name1 = value.name;
								newArr.itemId =value.itemId;
								newArr.strengthDesc = value.strengthDesc;
								newArr.units = value.units;
								newArr.listOfGenericIdWiseList1 = value.listOfGenericIdList;
								newArr.doseDesc = value.doseDesc;
								newArr.vaccineNameText = value.vaccineNameText;
								newArr.description = value.description;
								newArr.siteName = value.siteName;
								newArr.minAge = value.minAge;
								newArr.minDay = value.minDay;
								newArr.maxAge = value.maxAge;
								newArr.maxDay = value.maxDay;
								newArr.genericName = value.genericName;
								newArr.series = value.series;
								newArr.abbreviation = value.abbreviation;
								var array = value.minAge.split('-');
								var age=array[0];
								var duration=value.minDay;
								if(duration=="Days"){
									var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('days',age).toDate();
									var year=pDate.getFullYear();
									var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
									var m=pDate.getMonth();
									var month=a[m];
									var dayss=pDate.getDate();
									if(dayss < 9){
										var days="0"+ pDate.getDate();
									}
									else
									{
										var days = pDate.getDate();
									}
									var c=days+"/"+month+"/"+year;
									var d=c.toString();
									//alert("hiii:"+d);
									//newArr.plannedDate = d;
									var ps=days+"-"+month+"-"+year+" 00:00:00";
									var dps=ps.toString();
									newArr.calculatedDate=  dps;
									newArr.calculatedDateShow=d;
									$scope.calculatedDate=newArr.calculatedDate;
									//newArr.plannedDate = moment(dps,"DD-MM-YYYY").toDate();
									//newArr.plannedDateSave = dps;
								}
								if(duration=="Month"){
									var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('months',age).toDate();
									var year=pDate.getFullYear();
									var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
									var m=pDate.getMonth();
									var month=a[m];
									var days=pDate.getDate();
									var c=days+"/"+month+"/"+year;
									var d=c.toString();
									//alert("hiii:"+d);
									//newArr.plannedDate = d;
									var ps=days+"-"+month+"-"+year+" 00:00:00";
									var dps=ps.toString();
									newArr.calculatedDateShow= d;
									newArr.calculatedDate=  dps;
									$scope.calculatedDate=newArr.calculatedDate;
									//newArr.plannedDate =moment(dps,"DD-MM-YYYY").toDate();
									//newArr.plannedDateSave = dps;
								}
								if(duration=="Years"){
									var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('year',age).toDate();
									var year=pDate.getFullYear();
									var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
									var m=pDate.getMonth();
									var month=a[m];
									var days=pDate.getDate();
									var c=days+"/"+month+"/"+year;
									var d=c.toString();
									//alert("hiii:"+d);
									//newArr.plannedDate = d;
									var ps=days+"-"+month+"-"+year+" 00:00:00";
									var dps=ps.toString();
									newArr.calculatedDateShow= d;
									newArr.calculatedDate=  dps;
									$scope.calculatedDate=newArr.calculatedDate;
									//newArr.plannedDate =moment(dps,"DD-MM-YYYY").toDate();
									//newArr.plannedDateSave = dps;
								}
								if(duration=="Weeks"){
									var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('days',age*7).toDate();
									var year=pDate.getFullYear();
									var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
									var m=pDate.getMonth();
									var month=a[m];
									var days=pDate.getDate();
									var c=days+"/"+month+"/"+year;
									var d=c.toString();
									//alert("hiii:"+d);
									//newArr.plannedDate = c;
									var ps=days+"-"+month+"-"+year+" 00:00:00";
									var dps=ps.toString();
									newArr.calculatedDateShow= d;
									newArr.calculatedDate=  dps;
									$scope.calculatedDate=newArr.calculatedDate;
									//newArr.plannedDate =moment(dps,"DD-MM-YYYY").toDate();
									//newArr.plannedDateSave = dps;
								}	            											
								var array = $scope.patientDetails.birthDate.split('-');
								var birthDate = "";
								return birthDate = array[1]+'/'+array[1]+'/'+array[0];
								var birthDate = $rootScope.getFormatedDateeShase(newArr.calculatedDateShow);
								newArr.dateOptionsAdm=
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(birthDate),
									maxDate : new Date(),
								};
								console.log("dateOptionsAdm",$scope.dateOptionsAdm);
								newArr.actualDate = value.actualDate;
								newArr.dateOptionsVacc =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
								};
								newArr.vaccineFlagStatus = 'N';
								newArr.vaccineImmunMapperId = value.vaccineImmunMapperId;
								$scope.VaccineFinalList.push(newArr);
							}        */                    	       
//						});
						/*$scope.VaccineTextList=[];
						angular.forEach($scope.vaccineMapperList, function(value , key) { 
							if(value.vaccineNameText != undefined){
								var newAr={};
								newAr.vaccinationId = value.vaccinationId;
								newAr.vaccineImmunMapperId = value.vaccineImmunMapperId;
								newAr.genericId =value.genericId;
								newAr.name = value.name;
								newAr.name1 = value.name;
								newAr.itemId =value.itemId;
								newAr.strengthDesc = value.strengthDesc;
								newAr.units = value.units;
								newAr.listOfGenericIdWiseList1 = value.listOfGenericIdList;
								newAr.doseDesc = value.doseDesc;
								newAr.vaccineNameText = value.vaccineNameText;
								newAr.description = value.description;
								newAr.siteName = value.siteName;
								newAr.minAge = value.minAge;
								newAr.minDay = value.minDay;
								newAr.maxAge = value.maxAge;
								newAr.maxDay = value.maxDay;
								newAr.genericName = value.genericName;
								newAr.series = value.series;
								newAr.abbreviation = value.abbreviation;
								newAr.vaccineNameText = value.vaccineNameText;
								$scope.VaccineTextList.push(newAr);
							}
							console.log("VaccineTextList",$scope.VaccineTextList);  
							
						}); */ 
						
						console.log("fffffffFinal Vaccine Array",JSON.stringify($scope.VaccineFinalList));                             
						$rootScope.stopLoader();     
					});		                    
				},5000);
			}
			$scope.getVaccineData();

			$scope.showAndAddVaccination = function(info){
				if(info.genericName){
					$scope.VaccineFinalList.push(info);
					info.age = "";
					info.plannedDate = "";
					info.admissionDate = "";
					info.genericName = "";
					info.location = "";
					info.remark = "";
				}
				else{
					growl.warning("Please Enter Vaccine Details ", {
						title : 'Warning!'
					});
					return false;
				}
			}
			$scope.dataVaccineObj={
					vaccineNameText:"",
					name:"",
					actualDate: "",
					calculatedDate: "",
					createdBy:$scope.userId,
					createdDate: moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
					encounterId:$scope.encounterDetailsObj.encounterId,
					isNoSignificantVaccination: "N",
					itemId: "",
					location:"",
					organizationId:$scope.organizationId,
					patientId:$scope.encounterDetailsObj.patientId,
					plannedDate:"",
					remarks:"",
					roleId:$scope.roleId,
					status:"A",
					unitId:$scope.unitId,
					updatedBy:$scope.userId,
					updatedDate:moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
					vaccinationMasterId:"",
					vaccineFlagStatus:"Y",
					age :""
					
			};
			$scope.saveVaccineHistory = function(info, index)
			{
				$rootScope.startLoader();
				$scope.vaccineListDoctor = [];
				if(!info.vaccineImmunMapperId){
					if($scope.dataVaccineObj.vaccineNameText != undefined && $scope.dataVaccineObj.vaccineNameText != null && $scope.dataVaccineObj.vaccineNameText != ""){
						var newArr=[];
						newArr.vaccineNameText=$scope.dataVaccineObj.vaccineNameText;
						newArr.name=$scope.dataVaccineObj.vaccineNameText;
						newArr.actualDate= null;
						newArr.calculatedDate= null;
						newArr.createdBy=$scope.userId;
						newArr.createdDate= moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
						newArr.encounterId=$scope.encounterDetailsObj.encounterId;
						newArr.isNoSignificantVaccination= "N";
						newArr.itemId= null;
						newArr.location= null;
						newArr.organizationId= $scope.organizationId;
						newArr.patientId=$scope.encounterDetailsObj.patientId;
						newArr.plannedDate=moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
						newArr.remarks= null;
						newArr.roleId= $scope.roleId;
						newArr.status= "A";
						newArr.unitId=$scope.unitId;
						newArr.updatedBy= $scope.userId;
						newArr.updatedDate= moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
						newArr.vaccinationMasterId= null;
						newArr.vaccineFlagStatus= "Y";
						newArr.age = $scope.dataVaccineObj.age;
						$scope.VaccineFinalList.push(newArr);
						
						if($scope.VaccineFinalList !=null && $scope.VaccineFinalList.length > 0){

							angular.forEach($scope.VaccineFinalList, function(value , key) {						
								if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
									$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.actualDate=null;
								}
								if(value.calculatedDate != undefined){
									$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
									$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.calculatedDate=null;
								}
													
								var Data = {
									vaccinationMasterId: null,
									vaccinationNote:$scope.vaccinationNote,
									itemId:value.itemId,
									age: value.age,
									location:value.location,
									vaccineNameText:value.vaccineNameText,
									vaccineFlagStatus : 'Y',
									/*vaccinationNote : $scope.vaccinationNote,*/
									plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
									calculatedDate:$scope.calculatedDate,
									roleId:$scope.roleId,
									actualDate:$scope.actualDate,
									remarks: value.remarks,
									createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy:$scope.userId,
									createdBy:$scope.userId,
									status:'A',
									patientId:$scope.encounterDetailsObj.patientId,
									isNoSignificantVaccination:$scope.noSignificantVaccination,
									encounterId: $scope.encounterDetailsObj.encounterId,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId	
								}
								if(Data.vaccineNameText != null){
									$scope.vaccineListDoctor.push(Data);
								}
												 				
							});
												
							
						
						}
					}
					$scope.plannedDate=false;
					if(($scope.dataVaccineObj.vaccineNameText == undefined || $scope.dataVaccineObj.vaccineNameText == null || $scope.dataVaccineObj.vaccineNameText == "")){
					if($scope.noSignificantVaccination =='N'){
						angular.forEach($scope.VaccineFinalList, function(value , key) {
							if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
								$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
							}
							else{
								$scope.actualDate=null;
							}
							if(value.calculatedDate != undefined){
								$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
								$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
							}else{
								$scope.calculatedDate=null;
							}
							if(value.name != null){
								/*	if(value.location != undefined)
								{
									if(value.plannedDate != undefined){*/
									var Data = {
										vaccinationMasterId: value.vaccinationId,
										itemId:value.itemId,
										location:value.location,
										vaccineNameText:value.vaccineNameText,
										vaccineFlagStatus : 'Y',
										plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
										calculatedDate:$scope.calculatedDate,
										roleId:$scope.roleId,
										actualDate:$scope.actualDate,
										remarks: value.remarks,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										status:'A',
										patientId:$scope.encounterDetailsObj.patientId,
										isNoSignificantVaccination:$scope.noSignificantVaccination,
										encounterId: $scope.encounterDetailsObj.encounterId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										age: value.age
									}
									$scope.vaccineListDoctor.push(Data);
								/*}else{
									$scope.plannedDate=true;
									}*/
								/*}*/
							}
						});
											
						if($scope.plannedDate){
							growl.warning(" Please enter date of administration ",
							{
								title : 'Warning!'
							});	
							$rootScope.stopLoader();
							return false;
						}
						
						// if($scope.flagVaccineValidate){
							console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
							var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
							GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor).then(function(response) {
								console.log(response);
								if (response.data.status == 'success') {					
									growl.success(response.data.message,
									{
										title : 'Success!'
									});
									$rootScope.stopLoader();
									info.vaccineNameText="";
									$scope.getVaccineData();
									// $scope.$broadcast('show-errors-reset');
								} else if (response.data.status == 'error') {		
									growl.warning(response.data.message,
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
								}
							});
							console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
						// }
						// else{
						// 	growl.warning("Please Enter Vaccine Details ", {
						// 		title : 'Warning!'
						// 	});
						// 	$rootScope.stopLoader();
						// 	return false;
						// }
					}else{
						angular.forEach($scope.VaccineFinalList, function(value , key) {						
							if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
								$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
							}else{
								$scope.actualDate=null;
							}
							if(value.calculatedDate != undefined){
								$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
								$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
							}else{
								$scope.calculatedDate=null;
							}
												
							var Data = {
								vaccinationMasterId: value.vaccinationId,
								itemId:value.itemId,
								age: value.age,
								location:value.location,
								vaccineNameText:value.vaccineNameText,
								vaccineFlagStatus : 'Y',
								plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
								calculatedDate:$scope.calculatedDate,
								roleId:$scope.roleId,
								actualDate:$scope.actualDate,
								remarks: value.remarks,
								createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								createdBy:$scope.userId,
								status:'A',
								patientId:$scope.encounterDetailsObj.patientId,
								isNoSignificantVaccination:$scope.noSignificantVaccination,
								encounterId: $scope.encounterDetailsObj.encounterId,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId	
							}
							$scope.vaccineListDoctor.push(Data);				 				
						});
											
						
					}
				}
					var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
					GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
					.then(function(response) {
						console.log(response);							 
						if (response.data.status == 'success') {						
							growl.success(response.data.message,
							{
								title : 'Success!'
							});
							$rootScope.stopLoader();
							$scope.dataVaccineObj.vaccineNameText="";
							$scope.getVaccineData();
							// $scope.$broadcast('show-errors-reset');
						} else if (response.data.status == 'error') {							
							growl.warning(response.data.message,
							{
								title : 'Warning!'
							});
							$rootScope.stopLoader();
						}
					});	
				}
				else{
					$scope.plannedDate=false;						
					if($scope.noSignificantVaccination =='N'){
						angular.forEach($scope.VaccineFinalList, function(value , key) {
							if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
								$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
							}
							else{
								$scope.actualDate=null;
							}
							if(value.calculatedDate != undefined){
								$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
								$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
							}else{
								$scope.calculatedDate=null;
							}
							if(value.name != null){
								/*	if(value.location != undefined)
								{
									if(value.plannedDate != undefined){*/
									var Data = {
										vaccinationMasterId: value.vaccinationId,
										itemId:value.itemId,
										age: value.age,
										location:value.location,
										vaccineNameText:value.vaccineNameText,
										vaccineImmunMapperId: value.vaccineImmunMapperId,
										vaccineFlagStatus : 'Y',
										plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
										calculatedDate:$scope.calculatedDate,
										roleId:$scope.roleId,
										actualDate:$scope.actualDate,
										remarks: value.remarks,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										status:'A',
										patientId:$scope.encounterDetailsObj.patientId,
										isNoSignificantVaccination:$scope.noSignificantVaccination,
										encounterId: $scope.encounterDetailsObj.encounterId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
									}
									$scope.vaccineListDoctor.push(Data);
								/*}else{
									$scope.plannedDate=true;
									}*/
								/*}*/
							}
						});
											
						if($scope.plannedDate){
							growl.warning(" Please enter date of administration ",
							{
								title : 'Warning!'
							});	
							$rootScope.stopLoader();
							return false;
						}
						
						// if($scope.flagVaccineValidate){
							console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
							var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
							GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor).then(function(response) {
								console.log(response);
								if (response.data.status == 'success') {					
									growl.success(response.data.message,
									{
										title : 'Success!'
									});
									$rootScope.stopLoader();
									$scope.getVaccineData();
									// $scope.$broadcast('show-errors-reset');
								} else if (response.data.status == 'error') {		
									growl.warning(response.data.message,
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
								}
							});
							console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
						// }
						// else{
						// 	growl.warning("Please Enter Vaccine Details ", {
						// 		title : 'Warning!'
						// 	});
						// 	$rootScope.stopLoader();
						// 	return false;
						// }
					}else{
						angular.forEach($scope.VaccineFinalList, function(value , key) {						
							if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
								$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
							}else{
								$scope.actualDate=null;
							}
							if(value.calculatedDate != undefined){
								$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
								$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
							}else{
								$scope.calculatedDate=null;
							}
												
							var Data = {
								vaccinationMasterId: value.vaccinationId,
								itemId:value.itemId,
								age: value.age,
								location:value.location,
								vaccineNameText:value.vaccineNameText,
								vaccineImmunMapperId: value.vaccineImmunMapperId,
								vaccineFlagStatus : 'Y',
								plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
								calculatedDate:$scope.calculatedDate,
								roleId:$scope.roleId,
								actualDate:$scope.actualDate,
								remarks: value.remarks,
								createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								createdBy:$scope.userId,
								status:'A',
								patientId:$scope.encounterDetailsObj.patientId,
								isNoSignificantVaccination:$scope.noSignificantVaccination,
								encounterId: $scope.encounterDetailsObj.encounterId,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId	
							}
							$scope.vaccineListDoctor.push(Data);				 				
						});
											
						var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
						GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
						.then(function(response) {
							console.log(response);							 
							if (response.data.status == 'success') {						
								growl.success(response.data.message,
								{
									title : 'Success!'
								});
								$rootScope.stopLoader();
								$scope.getVaccineData();
								// $scope.$broadcast('show-errors-reset');
							} else if (response.data.status == 'error') {							
								growl.warning(response.data.message,
								{
									title : 'Warning!'
								});
								$rootScope.stopLoader();
							}
						});
					}
				}	
			}    
			
			$scope.validateVaccinName = function(info){
				var isvalid = true;
				if(!$scope.dataVaccineObj.vaccineNameText){
					growl.error("Please enter brand name..!");
					isvalid = false;
				}
				return isvalid;
			}

			$scope.saveFreeTextVaccineHistory = function(info, index)
			{
				if($scope.validateVaccinName(info))
				{
					$rootScope.startLoader();
					$scope.vaccineListDoctor = [];
					if(!info.vaccineImmunMapperId){
						if($scope.dataVaccineObj.vaccineNameText != undefined && $scope.dataVaccineObj.vaccineNameText != null && $scope.dataVaccineObj.vaccineNameText != ""){
							var newArr=[];
							newArr.vaccineNameText=$scope.dataVaccineObj.vaccineNameText;
							newArr.actualDate= null;
							newArr.calculatedDate= null;
							newArr.createdBy=$scope.userId;
							newArr.createdDate= moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
							newArr.encounterId=$scope.encounterDetailsObj.encounterId;
							newArr.isNoSignificantVaccination= "N";
							newArr.itemId= null;
							newArr.location= null;
							newArr.organizationId= $scope.organizationId;
							newArr.patientId=$scope.encounterDetailsObj.patientId;
							newArr.plannedDate=moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
							newArr.remarks= null;
							newArr.roleId= $scope.roleId;
							newArr.status= "A";
							newArr.unitId=$scope.unitId;
							newArr.updatedBy= $scope.userId;
							newArr.updatedDate= moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
							newArr.vaccinationMasterId= null;
							newArr.vaccineFlagStatus= "Y";
							newArr.age = $scope.dataVaccineObj.age;
							$scope.VaccineFinalList.push(newArr);
							
							if($scope.VaccineFinalList !=null && $scope.VaccineFinalList.length > 0){

								angular.forEach($scope.VaccineFinalList, function(value , key) {						
									if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
										$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
									}else{
										$scope.actualDate=null;
									}
									if(value.calculatedDate != undefined){
										$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
										$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
									}else{
										$scope.calculatedDate=null;
									}
														
									var Data = {
										vaccinationMasterId: null,
										itemId:value.itemId,
										age: value.age,
										location:value.location,
										vaccineNameText:value.vaccineNameText,
										vaccineFlagStatus : 'Y',
										plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
										calculatedDate:$scope.calculatedDate,
										roleId:$scope.roleId,
										actualDate:$scope.actualDate,
										remarks: value.remarks,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										status:'A',
										patientId:$scope.encounterDetailsObj.patientId,
										isNoSignificantVaccination:$scope.noSignificantVaccination,
										encounterId: $scope.encounterDetailsObj.encounterId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId	
									}
									if(value.vaccineImmunMapperId == undefined && Data.vaccineNameText != null){
										$scope.vaccineListDoctor.push(Data);
									}
																	
								});
													
								
							
							}
						}
						$scope.plannedDate=false;
						if(($scope.dataVaccineObj.vaccineNameText == undefined || $scope.dataVaccineObj.vaccineNameText == null || $scope.dataVaccineObj.vaccineNameText == "")){
						if($scope.noSignificantVaccination =='N'){
							angular.forEach($scope.VaccineFinalList, function(value , key) {
								if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
									$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
								}
								else{
									$scope.actualDate=null;
								}
								if(value.calculatedDate != undefined){
									$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
									$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.calculatedDate=null;
								}
								if(value.name != null){
									/*	if(value.location != undefined)
									{
										if(value.plannedDate != undefined){*/
										var Data = {
											vaccinationMasterId: value.vaccinationId,
											itemId:value.itemId,
											location:value.location,
											vaccineNameText:value.vaccineNameText,
											vaccineFlagStatus : 'Y',
											plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
											calculatedDate:$scope.calculatedDate,
											roleId:$scope.roleId,
											actualDate:$scope.actualDate,
											remarks: value.remarks,
											createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											patientId:$scope.encounterDetailsObj.patientId,
											isNoSignificantVaccination:$scope.noSignificantVaccination,
											encounterId: $scope.encounterDetailsObj.encounterId,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											age: value.age
										}
										$scope.vaccineListDoctor.push(Data);
									/*}else{
										$scope.plannedDate=true;
										}*/
									/*}*/
								}
							});
												
							if($scope.plannedDate){
								growl.warning(" Please enter date of administration ",
								{
									title : 'Warning!'
								});	
								$rootScope.stopLoader();
								return false;
							}
							
							// if($scope.flagVaccineValidate){
								console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
								var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
								GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor).then(function(response) {
									console.log(response);
									if (response.data.status == 'success') {					
										growl.success(response.data.message,
										{
											title : 'Success!'
										});
										
										$rootScope.stopLoader();
										$scope.dataVaccineObj.age = '';
										$scope.dataVaccineObj.vaccineNameText = "";
										$scope.getVaccineData();
										// $scope.$broadcast('show-errors-reset');
									} else if (response.data.status == 'error') {		
										growl.warning(response.data.message,
										{
											title : 'Warning!'
										});
										$rootScope.stopLoader();
									}
								});
								console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
							// }
							// else{
							// 	growl.warning("Please Enter Vaccine Details ", {
							// 		title : 'Warning!'
							// 	});
							// 	$rootScope.stopLoader();
							// 	return false;
							// }
						}else{
							angular.forEach($scope.VaccineFinalList, function(value , key) {						
								if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
									$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.actualDate=null;
								}
								if(value.calculatedDate != undefined){
									$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
									$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.calculatedDate=null;
								}
								
								if(!value.vaccineImmunMapperId){
									var Data = {
											vaccinationMasterId: value.vaccinationId,
											itemId:value.itemId,
											age: value.age,
											location:value.location,
											vaccineNameText:value.vaccineNameText,
											vaccineFlagStatus : 'Y',
											plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
											calculatedDate:$scope.calculatedDate,
											roleId:$scope.roleId,
											actualDate:$scope.actualDate,
											remarks: value.remarks,
											createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											patientId:$scope.encounterDetailsObj.patientId,
											isNoSignificantVaccination:$scope.noSignificantVaccination,
											encounterId: $scope.encounterDetailsObj.encounterId,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId	
										}
										$scope.vaccineListDoctor.push(Data);	
								}
																
							});
												
							
						}
					}
						var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
						GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
						.then(function(response) {
							console.log(response);							 
							if (response.data.status == 'success') {						
								growl.success(response.data.message,
								{
									title : 'Success!'
								});
								$rootScope.stopLoader();
								$scope.dataVaccineObj.age = '';
								$scope.dataVaccineObj.vaccineNameText = "";
								$scope.getVaccineData();
								// $scope.$broadcast('show-errors-reset');
							} else if (response.data.status == 'error') {							
								growl.warning(response.data.message,
								{
									title : 'Warning!'
								});
								$rootScope.stopLoader();
							}
						});	
					}
					else{
						$scope.plannedDate=false;						
						if($scope.noSignificantVaccination =='N'){
							angular.forEach($scope.VaccineFinalList, function(value , key) {
								if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
									$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
								}
								else{
									$scope.actualDate=null;
								}
								if(value.calculatedDate != undefined){
									$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
									$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.calculatedDate=null;
								}
								if(value.name != null){
									/*	if(value.location != undefined)
									{
										if(value.plannedDate != undefined){*/
										var Data = {
											vaccinationMasterId: value.vaccinationId,
											itemId:value.itemId,
											age: value.age,
											location:value.location,
											vaccineNameText:value.vaccineNameText,
											vaccineImmunMapperId: value.vaccineImmunMapperId,
											vaccineFlagStatus : 'Y',
											plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
											calculatedDate:$scope.calculatedDate,
											roleId:$scope.roleId,
											actualDate:$scope.actualDate,
											remarks: value.remarks,
											createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											patientId:$scope.encounterDetailsObj.patientId,
											isNoSignificantVaccination:$scope.noSignificantVaccination,
											encounterId: $scope.encounterDetailsObj.encounterId,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
										}
										$scope.vaccineListDoctor.push(Data);
									/*}else{
										$scope.plannedDate=true;
										}*/
									/*}*/
								}
							});
												
							if($scope.plannedDate){
								growl.warning(" Please enter date of administration ",
								{
									title : 'Warning!'
								});	
								$rootScope.stopLoader();
								return false;
							}
							
							// if($scope.flagVaccineValidate){
								console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
								var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
								GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor).then(function(response) {
									console.log(response);
									if (response.data.status == 'success') {					
										growl.success(response.data.message,
										{
											title : 'Success!'
										});
										$rootScope.stopLoader();
										$scope.dataVaccineObj.age = '';
										$scope.dataVaccineObj.vaccineNameText = "";
										$scope.getVaccineData();
										// $scope.$broadcast('show-errors-reset');
									} else if (response.data.status == 'error') {		
										growl.warning(response.data.message,
										{
											title : 'Warning!'
										});
										$rootScope.stopLoader();
									}
								});
								console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
							// }
							// else{
							// 	growl.warning("Please Enter Vaccine Details ", {
							// 		title : 'Warning!'
							// 	});
							// 	$rootScope.stopLoader();
							// 	return false;
							// }
						}else{
							angular.forEach($scope.VaccineFinalList, function(value , key) {						
								if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
									$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.actualDate=null;
								}
								if(value.calculatedDate != undefined){
									$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
									$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
								}else{
									$scope.calculatedDate=null;
								}
													
								var Data = {
									vaccinationMasterId: value.vaccinationId,
									itemId:value.itemId,
									age: value.age,
									location:value.location,
									vaccineNameText:value.vaccineNameText,
									vaccineImmunMapperId: value.vaccineImmunMapperId,
									vaccineFlagStatus : 'Y',
									plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
									calculatedDate:$scope.calculatedDate,
									roleId:$scope.roleId,
									actualDate:$scope.actualDate,
									remarks: value.remarks,
									createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy:$scope.userId,
									createdBy:$scope.userId,
									status:'A',
									patientId:$scope.encounterDetailsObj.patientId,
									isNoSignificantVaccination:$scope.noSignificantVaccination,
									encounterId: $scope.encounterDetailsObj.encounterId,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId	
								}
								$scope.vaccineListDoctor.push(Data);				 				
							});
												
							var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
							GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
							.then(function(response) {
								console.log(response);							 
								if (response.data.status == 'success') {						
									growl.success(response.data.message,
									{
										title : 'Success!'
									});
									$rootScope.stopLoader();
									$scope.dataVaccineObj.age = '';
									$scope.dataVaccineObj.vaccineNameText = "";
									$scope.getVaccineData();
									// $scope.$broadcast('show-errors-reset');
								} else if (response.data.status == 'error') {							
									growl.warning(response.data.message,
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
								}
							});
						}
					}
				}
			}    

			$scope.saveVaccine = function(index)
			{
				$rootScope.startLoader();
				$scope.vaccineListDoctor = [];
				$scope.plannedDate=false;
				angular.forEach($scope.VaccineFinalList, function(value , key) {
					if(index == key){
						if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
							$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
						}else{
							$scope.actualDate=null;
						}
						if(value.calculatedDate != undefined){
							$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
							$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
						}else{
							$scope.calculatedDate=null;
						}
						if(value.name != null){
							/*if(value.location != undefined)
							{*/
								/*if(value.plannedDate != undefined){*/
									var Data = {
										vaccinationMasterId: value.vaccinationId,
										itemId:value.itemId,
										location:value.location,
										vaccineFlagStatus : 'Y',
										plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
										calculatedDate:$scope.calculatedDate,
										roleId:$scope.roleId,
										actualDate:$scope.actualDate,
										remarks: value.remarks,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										status:'A',
										patientId:$scope.encounterDetailsObj.patientId,
										isNoSignificantVaccination:$scope.noSignificantVaccination,
										encounterId: $scope.encounterDetailsObj.encounterId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId		
									}
									$scope.vaccineListDoctor.push(Data);
								/*}else{
									$scope.plannedDate=true;
								}*/
							/*}*/
						}
					}
				});
										
				if($scope.plannedDate){
					growl.warning(" Please enter date of administration ",
					{
						title : 'Warning!'
					});	
					$rootScope.stopLoader();
					return false;
				}
				// if($scope.flagVaccineValidate){
					console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
					var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
					GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
					.then(function(response) {
						console.log(response);							 
						if (response.data.status == 'success') {
							growl.success(response.data.message,
							{
								title : 'Success!'
							});
							$rootScope.stopLoader();
							// $scope.$broadcast('show-errors-reset');
						} else if (response.data.status == 'error') {
							growl.warning(response.data.message,
							{
								title : 'Warning!'
							});
							$rootScope.stopLoader();
						}
					});
					console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
				// }
				// else{
				// 	growl.warning("Please Enter Vaccine Details ", {
				// 		title : 'Warning!'
				// 	});
				// 	$rootScope.stopLoader();
				// 	return false;
				// }
			}
			/********************************FOR VACCINATION LIST END*****************************************/

			/******************************FOR TREATEMENT HISTORY LIST START********************************************/

			$scope.openDatePickerTreatment = function(index) {
				$scope.datepickerTretment.opened = true;
			}

			$scope.datepickerTretment = {
				opened : [ false ]
			}
                                
			$scope.dateOptionsTreatment = {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate:new Date(), // max
			}

			// $scope.dt = moment(new Date()).format('DD-MM-YYYY');
			var headerObj = {
				unit_id : $scope.unitId,
				organization_id : $scope.organizationId
			};

			var paramObjForHistList = {
				patientId : $scope.encounterDetailsObj.patientId,
				created_date:$scope.dt
			};
			var data={};
			
			$scope.getTreatmentHistory=function()
			{
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentPatientTreatmentHistoryDetailsForCoversheet";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
				NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj).then(
				function(response) {
					console.log(response);
					$scope.treatmentHistoryCurrentList = [];
					// $rootScope.startLoader();
					if (response.data.status == "success")
					$scope.treatmentHistoryCurrentList = response.data.listObject;
					angular.forEach($scope.treatmentHistoryCurrentList, function(d) {
						d.treatDate = moment(d.treatDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
					});
					$scope.treatmentHistoryCurrentListDoctor=[];
					if($scope.roleId == '2' )
					{										
						angular.forEach(response.data.listObject, function(value,key) {
							if(value.roleId == 3 && value.isReviewedFlag == 'N')
							{
								var data = {};
								data.isNoSignificantStatus = value.isNoSignificantStatus;
								data.remark = value.remark;
								data.isReviewedFlag = 'N',
								data.isEnterInErrorStatus='N',
								data.surgenName = value.surgenName,
								data.treatmentName = value.treatmentName,
								data.month= value.month,
								data.year = value.year,
								data.TreatmentHistoryDetailsId  = value.TreatmentHistoryDetailsId;
								$scope.treatmentHistoryCurrentListDoctor.push(data);
							}
						});
					}									
				});
			}
			$scope.getTreatmentHistory();
											
			/*******FOR OLD TREATMENT HISTORY LIST**********/								
			$scope.getOldTreatmentHistory = function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};

				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
					created_date:$scope.dt
				};
				var data ={};
				$scope.treatmentHistoryOldList=[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getOldPatientTreatmentHistoryDetails";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data, STATUS_FALSE,paramObjForHistList,headerObj)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.treatmentHistoryOldList = response.data.listObject;
				});
			}
											
			/******Medical procedure list for Treatment History************/
			$scope.getMedicalProcedureForTreatmentHistoryList = function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					organizationId : $scope.organizationId
				};

				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
				};
				
				$scope.listCompletedMedicalProcedures=[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCompletedMedicalProcedures";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,
				headerObj).then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
					$scope.listCompletedMedicalProcedures = response.data.listObject;
				});
			}
			$scope.getMedicalProcedureForTreatmentHistoryList();
			/******************/
			
			/******diagnostic procedure list for Treatment History************/
			$scope.getDiagnosticsProceduresForTreatmentHistoryList = function(){
				$scope.dt = moment(new Date()).format('DD-MM-YYYY');
				var headerObj = {
					organizationId : $scope.organizationId
				};

				var paramObjForHistList = {
					patientId : $scope.encounterDetailsObj.patientId,
				};
				$scope.listCompletedMedicalProcedures=[];
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCompletedDiagnosticsProcedures";
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj).then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
					$scope.listCompletedDiagnosticsProcedures = response.data.listObject;
				});
			}
			$scope.getDiagnosticsProceduresForTreatmentHistoryList();
			/******************/

			$scope.validateTreatment = function(item){
				var isvalid = true;
				if(!item.treatementName){
					growl.error("Please enter treatment name");
					isvalid = false;
				} 
				return isvalid;
			}

			$scope.treatmentHistoryConfirm = function(info){
				if($scope.validateTreatment(info))
				{
					$scope.historyTreatmentList[0].treatmentName = info.treatementName;
					$scope.historyTreatmentList[0].treatDate = info.treatDate;
					$scope.CheckNosignTreatment = true;
					if($scope.noSignificantTreatment =="N"){
						angular.forEach($scope.treatmentHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								$scope.CheckNosignTreatment = false;
							}
						});
					}
					if($scope.CheckNosignTreatment){
						$scope.saveHistoryTreatment();
					}else{
						$("#CheckNosignTreatment").show();
					}
				}
			}
									
			$scope.saveHistoryTreatment = function()
			{
				if($scope.noSignificantTreatment == 'Y')
				{
					var count = 0;
					angular.forEach($scope.treatmentHistoryCurrentList, function(value , key) {					
						if(value.isNoSignificantStatus == 'Y')
						{
							growl.warning("You have already saved.!!!",
							{
								title : 'Warning!'
							});				
							return false;
						}
						else{
							$scope.historyTreatmentList = [{
								isNoSignificantStatus:'Y',
								treatmentName:"",
								surgenName:"",
								month:"",
								year:"",
								remark:"",
								treatDate:"",
								isEnterInErrorStatus:"N",
								isReviewedFlag:"N"
							}];
							count++;
						}
									
						if($scope.treatmentHistoryCurrentList.length==count)
						{
							$scope.saveHistorTreatmentFunction();
						}
					});
								 
					if( $scope.noSignificantTreatment== 'Y' && $scope.treatmentHistoryCurrentList.length==0)
					{
						$scope.historyTreatmentList = [{
							isNoSignificantStatus:'Y',
							treatmentName:"",
							surgenName:"",
							month:"",
							year:"",
							remark:"",
							treatDate:"",
							isEnterInErrorStatus:"N",
							isReviewedFlag:"N"
						}];
						$scope.saveHistorTreatmentFunction();
					}
					else if ( $scope.treatmentHistoryCurrentList.length==0){
						$scope.historyTreatmentList = [{
							isNoSignificantStatus:'N',
							treatmentName:"",
							surgenName:"",
							month:"",
							year:"",
							treatDate:"",
							remark:"",
							isEnterInErrorStatus:"N",
							isReviewedFlag:"N"
						}];
						$scope.saveHistorTreatmentFunction();				 
					}
				}
				else{
					$scope.saveHistorTreatmentFunction();
				}				
			}

			$scope.validateTretHisRow = true;
			$scope.saveHistorTreatmentFunction = function()
			{
				$rootScope.startLoader();
				var data = {					
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					createdBy:$scope.userId,
					status:'A',
					encounterId : $scope.encounterDetailsObj.encounterId,
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId
				};
				$scope.validateTretHisRow = true;
				if($scope.roleId == 3)
				{
					$scope.treatmentHistoryCurrentListDoctor=[];
					$scope.treatmentHistoryCurrentListDoctor12 = [];
					angular.forEach($scope.historyTreatmentList, function(value , key) {							
						if (value.treatmentName != ""){						
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								treatementName :value.treatmentName,
								surgenName : value.surgenName,
								treatDate:moment(value.treatDate).format('DD-MM-YYYY HH:mm:ss'),
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.treatmentHistoryCurrentListDoctor.push(dData);
							data.listOfPatientTreatmentHistoryDto = $scope.treatmentHistoryCurrentListDoctor;												
						} else if (value.isNoSignificantStatus == 'Y'){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								treatementName :value.treatmentName,
								treatDate:moment(value.treatDate).format('DD-MM-YYYY HH:mm:ss'),
								surgenName : value.surgenName,
								isNoSignificantStatus : value.isNoSignificantStatus,										
                           	}
							$scope.treatmentHistoryCurrentListDoctor.push(dData);
							data.listOfPatientTreatmentHistoryDto = $scope.treatmentHistoryCurrentListDoctor;													
						}
						else{
							growl.warning("Please Enter Treatment History",
							{
								title : 'Warning!'
							});
							$scope.validateTretHisRow = false;
							$rootScope.stopLoader();
							return false;
						}
					});						
					if($scope.CheckNosignTreatment == false){
						angular.forEach($scope.treatmentHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								var data = {};
								data.month="",
								data.year="",
								data.remark ="",
								data.isEnterInErrorStatus ='N',
								data.isReviewedFlag ='N',
								data.treatementName = "",
								data.treatDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								data.surgenName = "",
								data.isNoSignificantStatus ='Y',
								data.treatmentHistoryId = value.treatmentHistoryId,
								$scope.treatmentHistoryCurrentListDoctor12.push(data);
							}
						});						
						if($scope.treatmentHistoryCurrentListDoctor12.length > 0){
							data.listOfPatientTreatmentHistoryDto =[];
							data.listOfPatientTreatmentHistoryDto = $scope.treatmentHistoryCurrentListDoctor.concat($scope.treatmentHistoryCurrentListDoctor12);
						}							
					}				
					if($scope.validateTretHisRow){
						var paramObj = {};
						var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL + "/api/opd/savePatientTreatmentHistoryDetails";
						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.noSignificantTreatment="";
								$scope.treHisData.treatDate = '';
								$scope.treHisData.treatementName = '';
								// $("#CheckNosignTreatment").hide();
								$scope.getTreatmentHistory();
								$scope.getMedicalProcedureForTreatmentHistoryList();
								$scope.getDiagnosticsProceduresForTreatmentHistoryList();
								$scope.init();
								//    $scope.setSecondTabLevel(11);
								// return response.data.listObject;
						});
					}
				} 
				else {
					$scope.treatmentHistoryCurrentListDoctor=[];
					$scope.treatmentHistoryCurrentListDoctor12=[];
					angular.forEach($scope.historyTreatmentList, function(value , key) {
						if (value.treatmentName != ""){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								treatementName :value.treatmentName,
								surgenName : value.surgenName,
								treatDate:moment(value.treatDate).format('DD-MM-YYYY HH:mm:ss'),
								isNoSignificantStatus : value.isNoSignificantStatus,
							}
							$scope.treatmentHistoryCurrentListDoctor.push(dData);
							data.listOfPatientTreatmentHistoryDto = $scope.treatmentHistoryCurrentListDoctor;													
						} else if (value.isNoSignificantStatus == 'Y'){
							var dData = {
								month:value.month,
								year:value.year,
								remark : value.remark,
								isEnterInErrorStatus:value.isEnterInErrorStatus,
								isReviewedFlag:value.isReviewedFlag,
								treatementName :value.treatmentName,
								surgenName : value.surgenName,
								treatDate:moment(value.treatDate).format('DD-MM-YYYY HH:mm:ss'),
								isNoSignificantStatus : value.isNoSignificantStatus,			
							}
							$scope.treatmentHistoryCurrentListDoctor.push(dData);
							data.listOfPatientTreatmentHistoryDto = $scope.treatmentHistoryCurrentListDoctor;						
						}
						else{
							growl.warning("Please Enter Treatment History",
							{
								title : 'Warning!'
							});
							$scope.validateTretHisRow = false;
							$rootScope.stopLoader();
							return false;
						}
					});
					if($scope.CheckNosignTreatment == false){
						angular.forEach($scope.treatmentHistoryCurrentList, function(value, key) {
							if (value.isNoSignificantStatus == 'Y') {
								var data = {};
								data.month="",
								data.year="",
								data.remark ="",
								data.isEnterInErrorStatus ='N',
								data.isReviewedFlag ='N',
								data.treatementName = "",
								data.treatDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								data.surgenName = "",
								data.isNoSignificantStatus ='Y',
								data.treatmentHistoryId = value.treatmentHistoryId,
								$scope.treatmentHistoryCurrentListDoctor12.push(data);
							}
						});
												
						if($scope.treatmentHistoryCurrentListDoctor12.length > 0){
							data.listOfPatientTreatmentHistoryDto =[];
							data.listOfPatientTreatmentHistoryDto = $scope.treatmentHistoryCurrentListDoctor.concat($scope.treatmentHistoryCurrentListDoctor12);
						}						
					}
												
					if($scope.validateTretHisRow){
						var paramObj = {};
						var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL + "/api/opd/savePatientTreatmentHistoryDetails";
						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")		                   
								$scope.noSignificantTreatment="";
								$("#CheckNosignTreatment").hide();
								$scope.getTreatmentHistory();
								$scope.getMedicalProcedureForTreatmentHistoryList();
								$scope.getDiagnosticsProceduresForTreatmentHistoryList();
								$scope.init();
								$scope.treHisData.treatementName = ' ';
								$scope.treHisData.treatDate = ' ';
								$scope.treHisData.remark = ' ';
								$scope.treHisData.surgenName = ' ';
								// $scope.setSecondTabLevel(11);
								return response.data.listObject;
						});
					}
				}
			}
							
			$scope.removeTreatmentHistory = function(item){
									
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				var data ={
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					treatmentHistoryId : item.treatmentHistoryId,
				};
				var URI = BASE_URL + ROOT_URL + "/api/opd/updateStatusRemoveTreatmentHistory";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$rootScope.stopLoader();
						growl.success(response.data.message,
						{
							title : 'Success!'
						});
						$scope.init();
						$scope.getTreatmentHistory();
						$scope.getMedicalProcedureForTreatmentHistoryList();
						$scope.getDiagnosticsProceduresForTreatmentHistoryList();
						// $scope.setSecondTabLevel(11);
					} 
					else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.warning(response.data.message,
						{
							title : 'Warning!'
						});
					}
				});
			}
							
			$scope.showUpdateTreatmentHistory = function(item)
			{
				console.log(item);
				$scope.treatmentUpdateObj = {
					treatementName : item.treatementName,
					surgenName : item.surgenName,
					remark : item.remark,
					month : item.month,
					year : item.year,
					isEnterInErrorStatus : item.isEnterInErrorStatus,
					isReviewedFlag : item.isReviewedFlag,
					treatmentHistoryId : item.treatmentHistoryId,
					isNoSignificantStatus : item.isNoSignificantStatus,
				};
				$scope.datepickerWhenTreat = item.treatDate;
				$scope.itemRoleId = item.roleId;
				$scope.updateTreatmentHistory();
			}
							
			$scope.updateTreatmentHistory = function()
			{
				$scope.historyTreatmentList = [];
				$scope.treatmentUpdateObj.treatDate = moment(new Date($scope.datepickerWhenTreat)).format('DD-MM-YYYY HH:mm:ss');
				$scope.historyTreatmentList.push($scope.treatmentUpdateObj);
				var data = {
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:$scope.userId,
					createdBy:$scope.userId,
					status:'A',
					patientId : $scope.encounterDetailsObj.patientId,
					roleId : $scope.roleId,
					encounterId: $scope.encounterDetailsObj.encounterId,
					listOfPatientTreatmentHistoryDto:$scope.historyTreatmentList	
				};
				console.log(data);
				// return false;
				var paramObj = {};
				var headers={
					'organization_id': $scope.organizationId,
					'unit_id':$scope.unitId,
					'Content-Type':'application/json'
				};
				// return false;
				var URI = BASE_URL + ROOT_URL + "/api/opd/updatePatientTreatmentHistoryDetails";
				GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.init();
						$scope.getTreatmentHistory();
						$scope.getMedicalProcedureForTreatmentHistoryList();
						$scope.getDiagnosticsProceduresForTreatmentHistoryList();
						// $scope.setSecondTabLevel(11);
						// $scope.treatmentUpdateObj = {};
						// return response.data.listObject;
				});
			}

			//---------------------------- Custom history tab start ----------------------------------------------

			$scope.example14modelSpecialty = [];
			$scope.example14settingsSpecialty = {
				scrollableHeight: '200px',
				scrollable: true,
				enableSearch: true
			};

			$scope.yourEvents = {
				onItemSelect: function(selectedValue) {
					AddCustomHistoryList($scope.example14modelSpecialty);                     
				},
				onItemDeselect: function(selectedValue) {
					// AddCustomHistoryList($scope.example14modelSpecialty);  
				},
				onDeselectAll:function(selectedValue) {
					// AddCustomHistoryList($scope.example14modelSpecialty);  
				},
				onSelectAll:function(selectedValue) {
					AddCustomHistoryList($scope.example14modelSpecialty);  
				},
			}

			$scope.getSpecialityList = function() {
				// For Get Active Speciaity List
				var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
				var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
				BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
					{
						$scope.specialityList = response.data.listObject;
						$scope.example14data = [];
						$scope.example14dataSpecialty = [];
						angular.forEach($scope.specialityList, function(value,index){
							var data = {};
							data.id=value.specialityId;
							data.label=value.specialityName;
							if(value.isClinicalSpeciality == 'Y'){
								$scope.example14dataSpecialty.push(data);
							}	
						});
					}
				});
			}
			$scope.getSpecialityList();

			$scope.obj ={
				"specialityIdList" : []
			}
			$scope.customHistoryList = [];
			var AddCustomHistoryList = function() {
				$scope.obj.specialityIdList = [];
				var URI = BASE_URL + ROOT_URL + "/global/customHistory/getCustomHistoryConfigurationList";
				angular.forEach($scope.example14modelSpecialty, function(value) {
					$scope.obj.specialityIdList.push(value.id);
				});
				if($scope.customHistoryList.length > 0){	
					$scope.tempspecialityId = [];
					angular.forEach($scope.obj.specialityIdList, function(a) {
						var found = $scope.customHistoryList.find(ar=>ar.specialityId == a);
						if(found == undefined){
							$scope.tempspecialityId.push(a);
						}
					});
					if($scope.tempspecialityId.length == 0){
						growl.error("Selected speciality is already Saved..!");
						return false;
					}
				}
				else{
					$scope.tempspecialityId = [];
					$scope.tempspecialityId = $scope.tempspecialityId.concat($scope.obj.specialityIdList);
				}
				var data ={
					"patientId": $scope.encounterDetailsObj.patientId,
					"visitTypeId": $scope.patientDetails.visitTypeId,
					"encounterId": $scope.encounterDetailsObj.encounterId,
					"admissionId": $scope.encounterDetailsObj.admissionId ? $scope.encounterDetailsObj.admissionId : null,
					"organizationId": $scope.organizationId,
					"unitId": $scope.unitId,
					"createdBy": $scope.userId,
					"updatedBy": $scope.userId,
					"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					"specialityIdList" : $scope.tempspecialityId,
				}
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") 
					{	
						if($scope.customHistoryList.length > 0)
						{
							$scope.customHistoryList = $scope.customHistoryList.concat(response.data.listObject);
						}
						else
						{
							$scope.customHistoryList = response.data.listObject;
						}
						$rootScope.stopLoader();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}
				});
			}

			$scope.saveCustomHistory = function(data, remark) {
				var URI = BASE_URL + ROOT_URL + "/global/customHistory/updateCustomHistoryDetails";
				var data = {
					"remark": remark,
					"customHistoryConfigDetailsId": data.customHistoryConfigDetailsId
				}
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") 
					{	
						$rootScope.stopLoader();
						growl.success(response.data.code);
						$scope.GetCustomHistoryList();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}
				});
			}

			$scope.removeCustomHistory = function(data) {
				var URI = BASE_URL + ROOT_URL + "/global/customHistory/deleteCustomHistoryDetailsRecord";
				var data = {
					"status":"I",
					"customHistoryConfigDetailsId":data.customHistoryConfigDetailsId
				}
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") 
					{	
						$rootScope.stopLoader();
						growl.success("Deleted Successfully..!");
						$scope.GetCustomHistoryList();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}
				});
			}

			$scope.GetCustomHistoryList = function() {
				var URL = BASE_URL + ROOT_URL + "/global/customHistory/getPatientCustomHistoryDetails";
				var data = {
					"encounterId": $scope.encounterDetailsObj.encounterId,
					"admissionId": $scope.encounterDetailsObj.admissionId ? $scope.encounterDetailsObj.admissionId : null,
					"organizationId": $scope.organizationId,
					"unitId": $scope.unitId
				}
				$scope.example14modelSpecialty = [];
				GenericService.serviceAction("POST", URL, data).then(function (response) {
					if (response.data.status == "success") 
					{	
						$scope.customHistoryList = response.data.listObject;
						$scope.selectedStoreList = [];
						angular.forEach(response.data.object.specialityIdList , function(d) {
							angular.forEach($scope.example14dataSpecialty, function(s) {
								if(d == s.id){
									var obj = {
										"id": '',
										"label": ''
									}
									obj.id = d;
									obj.label = s.label;
									$scope.example14modelSpecialty.push(obj);
								}
							});
						});

						$rootScope.stopLoader();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}
				});
			}
			$scope.GetCustomHistoryList();
			$scope.updateVaccineImmunizationMapper = function(data, index) {
				var URI = BASE_URL + ROOT_URL + "/api/opd/updateVaccineImmunizationMapperForNewCov";
				var data = {
					remark: data.remarks != null ? data.remarks :'',
					location: data.location,
					plannedDate:moment(data.plannedDate).format("YYYY-MM-DD HH:mm:ss"),
					vaccineImmunMapperId: data.vaccineImmunMapperId,
				}
				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success")
					{
						$rootScope.stopLoader();
						growl.success("Updated Successfully..!");
						$scope.getVaccineData();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}
				});
			}
			
			  //for vital growth chart graph
			  $scope.getVitalList = function() {
					$scope.startLoader();
					
					  var headerObj = {
                            unit_id : $scope.unitId,
                            organization_id : $scope.organizationId
                        };

                    
                    var paramObjForPer = {
                    };
                    var data ={};
					
					var URI = BASE_URL + ROOT_URL
							+ GETVITALMASTERLIST;
					GenericServiceParamHeader
                  .serviceAction(METHOD_GET, URI, data,
                          NOTIFICATION_MSG_STATUS_FALSE,
                          paramObjForPer, headerObj)
							.then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.vitalsList = response.data.listObject;
										$scope.printListForVitals();
										console.log("Vital List", $scope.vitalsList);
										
									});
					$scope.stopLoader();
				}
				$scope.getVitalList();
			  $scope.printListForVitals=function(){
                $scope.vitalsListForPrint=[];
                  angular.forEach($scope.vitalsList, function(val, key){
                      if(val.vitalId == 1 || val.vitalId == 2 || val.vitalId == 15){
                          $scope.vitalsListForPrint.push(val); 
                      }
                  });
                  console.log( "hhhhhhhh",$scope.vitalsListForPrint);
            }
             
             $scope.printVitalGraph=function(){
                $('#myIframe').removeAttr('src');
                var data = {
                         "appName": "HIS",
                         "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                         "reportDesignFilePath": "GrowthChart.rpt",
                         "reportFormat": "PDF",
                         "exportFileName": "Coversheet",
                         "reportParams": [
                             {
                                  "paramName": "vital_id",
                                  "paramType": "String",
                                  "paramValue": $scope.printVitalId.toString()
                            },
                            {
                                "paramName": "patient_id",
                                "paramType": "Integer",
                                "paramValue": $scope.encounterDetailsObj.patientId
                          }
                 ]
                
      };
                var URI =REPORT_API_URI1;
                 var form = null;
                 
                 form = document.createElement("form");
                 form.setAttribute("id", "reportId");
                 form.setAttribute("name", "formReportBook");
                 form.setAttribute("method", "post");
                 form.setAttribute("action", URI);
                 form.setAttribute("target", "myIframeGrowth");
                 
                 var hiddenField1 = document.createElement("input"); 
                 hiddenField1.setAttribute("type", "hidden");
                 hiddenField1.setAttribute("name", "reportParam");
                 hiddenField1.setAttribute("value", JSON.stringify(data));
                 
                 var hiddenField2 = document.createElement("input"); 
                 hiddenField2.setAttribute("type", "hidden");
                 hiddenField2.setAttribute("name", "orgId");
                 hiddenField2.setAttribute("value",$scope.organizationId);
                 
                 var hiddenField3 = document.createElement("input"); 
                 hiddenField3.setAttribute("type", "hidden");
                 hiddenField3.setAttribute("name", "unitId");
                 hiddenField3.setAttribute("value", $scope.unitId);
                 
                 form.appendChild(hiddenField1);
                 form.appendChild(hiddenField2);
                 form.appendChild(hiddenField3);
                 
                
                 document.body.appendChild(form);
                 $("form[name='formReportBook']").submit();
                 $rootScope.stopLoader();
            }
             
             $timeout( function(){
               	
            	 var headerObj = {
    					    unit_id : $scope.unitId,
    						organization_id : $scope.organizationId
    					     };
            	 
            	 var paramObjForVital = {
               				
                    		   unitId:$scope.unitId,
                   				organizationId:$scope.organizationId,
                   				patientId:$scope.encounterDetailsObj.patientId,
               		}
            	 var data={
        			 	   unitId:$scope.unitId,
           				   organizationId:$scope.organizationId,
           				   patientId:$scope.encounterDetailsObj.patientId,
        	 			}
               		console.log("jj",paramObjForVital);
               		// return false;
                       var URI = BASE_URL + ROOT_URL
                       + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
                GenericServiceParamHeader
                    .serviceAction(METHOD_POST, URI,
                            data,
                            NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
                    .then(
                            function(response) {
                                console.log(response);
                     
                    
                    if (response.data.status == "success")
                        $scope.vitalFinalList = [];
                        $scope.patientViatalList  = response.data.listObject;
                        
                        $scope.vitalDateList = [];
                        $scope.vitalNameList = [];
                        
     
                        angular.forEach($scope.patientViatalList, function(value,key) {
    							 $scope.flagTime=false;
    							  if($scope.vitalDateList.length > 0){
    									angular.forEach($scope.vitalDateList, function(value3,key) {
    										if(value3.createdTime1 == value.postFactoTime){
     										$scope.flagTime = true;
    									}
    									});
    								}
    								 if(!$scope.flagTime){
    								var newArr={};
    								newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
    									 newArr.createdTime1=value.postFactoTime;
    									 newArr.createdDate=value.createdDate;
    									 value.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
    									$scope.vitalDateList.push(newArr);
    								 }
    								 console.log("$scope.createdTime",$scope.vitalDateList);
    								 
    						});
                        
                        $scope.vitalsListTest=[];
                        angular.forEach($scope.vitalsList, function(value1 , key1) {
                        angular.forEach($scope.patientViatalList, function(value,key) {
                              if(value1.vitalId == value.vitalId){
                            	  var newArr = {};
                            	  newArr.value = value.value;
                            	  newArr.vitalId = value.vitalId;
                            	  newArr.appointmentVitalMapperId = value.appointmentVitalMapperId;
                            	  newArr.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
                            	  $scope.vitalsListTest.push(newArr);
                              }
                        });
                        });
                        
                        console.log("sortedDataList",$scope.vitalsListTest);
                        
                        if($scope.forthTabLevel == 12){
                        	$scope.onClick();
                        	}
                        	// $scope.onClick();
                    console.log("patientVitalList",response.data.listObject);
                    console.log("vitalList",$scope.vitalsList);
                    
                    $scope.vitalsListFOR=[];
    				if($scope.showHeadCircuVital == false){
    					angular.forEach( $scope.vitalsList, function( value,key) {
    						if(value.vitalId != 15){
    							$scope.vitalsListFOR.push(value);	
    						}
    					});
    					$scope.vitalsList=[];
    					$scope.vitalsList=$scope.vitalsListFOR;
    				}
    				
                     angular.forEach($scope.vitalsList, function(value , key) {
                    	 var checkFlag = false;
                         angular.forEach($scope.patientViatalList, function(value1 , key1) {
                             	
                        	
                        	 if(value.vitalId == value1.vitalId)
                        		 {
                        		 	checkFlag = true;
                        		 	$scope.newArr = {};
                        		 	$scope.newArr.vitalId = value.vitalId;
                        		 	$scope.newArr.minValue = value.minValue;
                        		 	$scope.newArr.maxValue = value.maxValue;
                        		 	$scope.newArr.vitalName = value.vitalName;
                        		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
                        		 	$scope.newArr.unit = value.unit;
                        		 	$scope.newArr.vitalValue = value1.value;
                        		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
                        		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
    								$scope.newArr.checkQualifier = true;
                        		 	if($scope.roleId == 2)
                    		 		{
                        		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
                    		 		}
                        		 	
                                   
                        		 }
                        	
                         		});
    										if (checkFlag == true) {
    												$scope.vitalFinalList.push($scope.newArr);
    											} else {
    												// alert("fdvfd");
    												var newArr = {};
    												newArr.vitalId = value.vitalId;
                                        		 	newArr.minValue = value.minValue;
                                        		 	newArr.maxValue = value.maxValue;
                                        		 	newArr.vitalName = value.vitalName;
                                        		 	newArr.vitalIconPath = value.vitalIconPath;
                                        		 	newArr.unit = value.unit;
                                        		 	if(value.vitalValue != undefined ){
                                        		 		newArr.vitalValue = value.vitalValue;
                                        		 	}else{
                                        		 		newArr.vitalValue =""
                                        		 	}
    //newArr.vitalValue = "";
                                        		 	newArr.isReviewedFlag = 'N';
                                        		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
                                        			newArr.checkQualifier = true;
                                        		 	if($scope.roleId == 2)
                                    		 		{
                                        		 		newArr.appointmentVitalMapperId ='';
                                    		 		}
    												$scope.vitalFinalList.push(newArr);
    											}
                    	       
                        });
                     
                    
                     console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
                     var counter=0;
                        angular.forEach($scope.vitalFinalList,function(value,key){
                        	if( counter == 0){
                        		if(value.vitalId == 2){
                                	angular.forEach($scope.patientViatalList, function(value1 , key1) {
                                		 if(value.vitalId == value1.vitalId)
                                   		 { 
                                			 if(counter == 0){
                                			 if(value1.value != "0"){
                                				 value.vitalValue = value1.value;
                                    			 counter++;
                                			 }
                                			 }
                                   		 }
                                	 });
                                	}
                        	}
                        	
                        });
                        var datalistVital = $scope.vitalFinalList;
    					for (var i = 0; i < datalistVital.length; i++) {
    						if($scope.vitalFinalList[i].vitalId != 2){
    							$scope.vitalFinalList[i].vitalValue = '';
    						}
    					}
    					console.log("refreshlist",$scope.vitalFinalList);
                   
                                                                              
                            });
            
            },4000);
             
             $scope.onClick = function () {
				   $scope.myChartForSearch = false;
				   $scope.vitalnameGraphList=[];
              	 $scope.vitalname=[];
              	 $scope.createdTime=[];
  				 $scope.vitalData=[];
  				 $scope.vitalTime=[];
  				 $scope.vitalLabel=[];
  				 //for label
  				 if($scope.patientViatalList.length > 0){
					 angular.forEach($scope.patientViatalList, function(value,key) {
						 $scope.flag=false;
						  if($scope.vitalnameGraphList.length > 0){
								angular.forEach($scope.vitalnameGraphList, function(value3,key) {
									if(value3.vitalName == value.vitalName){
										$scope.flag = true;
									}
								});
							}
							 if(!$scope.flag){
								var newArr={};
								 newArr.vitalName=value.vitalName;
								 newArr.unitDesc=value.unitDesc;
								 $scope.vitalnameGraphList.push(newArr);
							 }
							 console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);
							 
					});
					// for data
						
					 angular.forEach($scope.vitalnameGraphList, function(value1,index) {
						$scope.vitalData=[];
						 angular.forEach($scope.patientViatalList, function(value2,index) {
							 
							if(value1.vitalName == value2.vitalName){
								$scope.vitalData.push(value2.value);
								
							}
						 });
						value1.vitalData = $scope.vitalData;
					 });
				
					 
					$scope.colorArray = [];
					 var letters =[ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] ;  // "0F";
				    $scope.colorArray=[ '#803690', '#00ADF9', '#BE2439', '#46BFBD', '#FDB45C', '#006698', '#6A93B0','#992572','#F5FF00','#FF0000','#154889'] ;
					console.log("$scope.vitalname",$scope.vitalnameGraphList);
					console.log("colorArray",$scope.colorArray);
					$scope.dataForDataset=[];
					angular.forEach($scope.vitalnameGraphList, function(value,index) {
						var newArr = {};
						newArr.label = value.vitalName+" ("+ value.unitDesc+")";
						newArr.data = value.vitalData;
						newArr.backgroundColor =$scope.colorArray[index];
						newArr.borderColor =$scope.colorArray[index];
						newArr.fill = false;
						newArr.borderWidth = 2;
						$scope.dataForDataset.push(newArr);
					});
					console.log("$scope.dataForDatasetVital",$scope.dataForDataset);
                var ctx = document.getElementById('myChart').getContext('2d');
                var myChart = new Chart(ctx, {
              	  type: 'line',
              	  data: {
              		    labels: $scope.vitalLabel,
              		    datasets:$scope.dataForDataset
              		  },
              		  options: {
              			  title:{
              				  display: true,
              				  text: 'Vital Graph'
              			  },
              		        legend: {
              		        	 display: true,
              		            position: "top"
              		        },
              		        scales: {
              		        	animation:true,
              		            yAxes: [{
              		                ticks: {
              		                    fontColor: "rgba(0,0,0,0.5)",
              		                    fontStyle: "bold",
              		                    beginAtZero: true,
// maxTicksLimit: 5,
              		                    padding: 10
              		                },
              		                gridLines: {
              		                    drawTicks: false,
              		                    display: false
              		                }

              		            }],
              		            xAxes: [{
              		                gridLines: {
              		                    zeroLineColor: "transparent"
              		                },
              		                ticks: {
              		                    padding: 10,
              		                    fontColor: "rgba(0,0,0,0.5)",
              		                    fontStyle: "bold"
              		                }
              		            }]
              		        }
              		    }

              	});
					 //for time array
					 angular.forEach($scope.patientViatalList, function(value,key) {
						 $scope.flagTime=false;
						  if($scope.createdTime.length > 0){
								angular.forEach($scope.createdTime, function(value3,key) {
									if(value3.createdTime1 == value.postFactoTime){
										$scope.flagTime = true;
									}
								});
							}
							 if(!$scope.flagTime){
								var newArr={};
								newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
								 newArr.createdTime1=value.postFactoTime;
								 newArr.createdDate=value.createdDate;
								 $scope.createdTime.push(newArr);
							 }
							 console.log("$scope.createdTime",$scope.createdTime);
							 
					});
					 
					for (var i = 0; i < $scope.createdTime.length; i++) {
						$scope.vitalLabel.push($scope.createdTime[i].createdTime);
					}
					console.log("graph label",$scope.vitalLabel);
					
					//for data
					 angular.forEach($scope.createdTime, function(value1,index) {
							$scope.vitalDataTrends=[];
							 angular.forEach($scope.patientViatalList, function(value2,index) {
								 
								if(value1.createdDate == value2.createdDate){
									$scope.vitalDataTrends.push(value2);
								}
							 });
							value1.vitalDataTrends = $scope.vitalDataTrends;
						 });
					 console.log("d",$scope.createdTime);
					$scope.finalVitalObjFromAllVitalList = {};
					var hh =  $scope.createdTime.length;
		             angular.forEach( $scope.createdTime[0].vitalDataTrends, function( value,key) {
                      	if(value.roleId == 2){
                      		if(value.vitalId == 6){
                      			if(parseInt(value.value) != 0){
                      				
                      				$scope.finalVitalObjFromAllVitalList.temp = value.value;
                              		$scope.finalVitalObjFromAllVitalList.createdDatetemp = value.createdDate;
                              		$scope.finalVitalObjFromAllVitalList.createdTimetemp = value.createdTime;
                              		$scope.finalVitalObjFromAllVitalList.unitDesctemp = value.unitDesc;
                      			}
                          		
                          	}
                          	if(value.vitalId == 4){
//                          		pulse rate
                          		if(parseInt(value.value) != 0){
                          		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
                          		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
                          		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
                          		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
                          		}
                          	}
                          	if(value.vitalId == 3){
//                          		BP
                          		if(parseInt(value.value) != 0){
                          		$scope.finalVitalObjFromAllVitalList.bps = value.value;
                          		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
                          		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
                          		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
                          		}
                          	}
                          	if(value.vitalId == 14){
//                          		SPo2
                          		if(parseInt(value.value) != 0){
                          		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
                          		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
                          		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
                          		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
                          		}
                          	}
                        	if(value.vitalId == 7){
//                        		SPo2
                        		if(parseInt(value.value) != 0){
                        		$scope.finalVitalObjFromAllVitalList.bpd = value.value;
                        		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
                        		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
                        		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
                        		}
                        	}
                      	}
                      	
                      });
  				 }
  				 };
  				 
  			   /****************Save coversheet as template*******************/
		            $scope.coversheetTemplateName=""; 
					$scope.saveCoversheetAsTemplate=function(){
						$rootScope.startLoaderPopup();
			    		var data={
			    				patientId :$scope.encounterDetailsObj.patientId,
								encounterId :$scope.encounterDetailsObj.encounterId,
								createdBy :$scope.userId,
								createdDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								templateName:$scope.coversheetTemplateName,
								doctorId :$scope.encounterDetailsObj.doctorId,
								updatedBy :$scope.userId,
								updatedDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								organizationId :$scope.organizationId,
								unitId :$scope.unitId,
			    				status:"A",
			    		};
			    		
			    		var URI = BASE_URL + ROOT_URL
						+ "/api/opd/saveCoversheetAsTemaplate";
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.completeConsultation($scope.encounterDetailsObj.roleId);
										 
//										$rootScope.stopLoader();
									}
									
								});
					}
					
					//coversheet As temp list
					  $scope.getListOfCoversheetAStemplate= function(){
				        	$scope.listOfCoversheetAStemplate=[];
				        	var data={
				        			organizationId:$scope.organizationId,
				        			unitId:$scope.unitId
				        	};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getCoversheetAsTemaplateList" 
							
				    		return GenericService.serviceAction("POST",URI,data).then(function(response){
				    			if(response.data.status == "success"){
				    				$scope.listOfCoversheetAStemplate = response.data.listObject;
				    			}
				    			console.log("listOfCoversheetAStemplate", $scope.listOfCoversheetAStemplate);
				    			return $scope.listOfCoversheetAStemplate;
				    		});
				    	}
					  $scope.getListOfCoversheetAStemplate();
					  
					//coversheet As temp list
					/*  $scope.getCovListByTemplateId= function(templateId){
				        	$scope.covListByTemplateId=[];
				        	var data={
				        			covTemplateId:templateId,
				        			organizationId:$scope.organizationId,
				        			unitId:$scope.unitId
				        	};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getAllCoversheetListsByTemplateId" 
							
				    		GenericService.serviceAction("POST",URI,data).then(function(response){
				    			if(response.data.status == "success"){
				    				$scope.covListByTemplateId = response.data.object;
				    				$rootScope.stopLoader();
				    			}
				    			console.log("covListByTemplateId", $scope.covListByTemplateId);
				    				$('#exampleModal').modal('show');
				    			});
				    	}*/
					  
					  
					  
					  $scope.selectedTemplate=function(covTemplateId){
						  $scope.covListByTemplateId=[];
						  if(covTemplateId == 'custom'){
							  $('#customeEmr').modal('show');
							  return false;
						  }
						  else {
							  var data={
					        			covTemplateId:covTemplateId,
					        			organizationId:$scope.organizationId,
					        			unitId:$scope.unitId
					        	};
								var URI = BASE_URL + ROOT_URL + "/api/opd/getAllCoversheetListsByTemplateId" 
								
					    		GenericService.serviceAction("POST",URI,data).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.covListByTemplateId = response.data.object;
					    				$scope.getCustomeList();
					    				$scope.getCustomePatientId();
					    				$rootScope.stopLoader();
					    			}
					    			console.log("covListByTemplateId", $scope.covListByTemplateId);
					    				$('#templateCoversheteetModal').modal('show');
					    			});
						  }
						  
					  }
					  
					  $scope.confirmSave=function(info){
						  $('#templateCoversheteetModal').modal('hide');
						  $('#confirmSave').modal('show');
						  
					  }
					  
					  //for template lists save
					  $scope.covTempObject={
							  complaintTempList:[],
							  medicalTempList:[],
							 surgicalTempList:[],
							  lifeTempStyleList:[],
							   allergyTempList:[],
							   medicationHistoryTempList:[],
							   treatmentTempList:[],
							   familyTempList:[],
							   personalTempList:[],
							   vaccinationTempList:[],
							   travelTempList:[],
							   vitalTempList:[],
							   generalExaminationTempList:[],
							   systemicExaminationTempList:[],
							   diagnosisTempList:[],
							   diagnosticsTempList:[],
							   medicationTempList:[],
							   adviceTempList:[],
							   consultOrderTempList:[],
							   observationTempList:[],
							   ceratedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								createdBy:$scope.userId,
								status :'A',
								roleId : $scope.roleId,
								encounterId : $scope.encounterDetailsObj.encounterId,
								patientId : $scope.encounterDetailsObj.patientId,
								doctorId:$scope.encounterDetailsObj.doctorId
					  };
					  
					  $scope.saveListDataOfCovTemplate=function(info){
						  
						  $scope.covTempObject.ceratedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  $scope.covTempObject.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  $scope.covTempObject.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  $scope.covTempObject.updatedBy = $scope.userId,
						  $scope.covTempObject.createdBy = $scope.userId,
						  $scope.covTempObject.status = 'A',
						  $scope.covTempObject.roleId = $scope.roleId,
						  $scope.covTempObject.doctorId = $scope.encounterDetailsObj.doctorId,
						  $scope.covTempObject.encounterId = $scope.encounterDetailsObj.encounterId,
						  $scope.covTempObject.patientId = $scope.encounterDetailsObj.patientId
						  $scope.covTempObject.complaintTempList=info.complaintsPatientList;
						  $scope.covTempObject.medicalTempList=info.medicalTempList;
						  $scope.covTempObject.surgicalTempList=info.surgicalTempList;
						  $scope.covTempObject.lifeTempStyleList=info.lifeTempStyleList;
						  $scope.covTempObject. allergyTempList=info.allergyTempList;
						  $scope.covTempObject.medicationHistoryTempList=info.medicationHistoryTempList;
						  $scope.covTempObject.treatmentTempList=info.treatmentTempList;
						  $scope.covTempObject.familyTempList=info.familyTempList;
						  $scope.covTempObject.personalTempList=info.personalTempList;
						  $scope.covTempObject.vaccinationTempList=info.vaccinationTempList;
						  $scope.covTempObject.travelTempList=info.travelTempList;
						  $scope.covTempObject.vitalTempList=info.vitalTempList;
						  $scope.covTempObject.generalExaminationTempList=info.generalExaminationTempList;
						  $scope.covTempObject.systemicExaminationTempList=info.systemicExaminationTempList;
						  $scope.covTempObject.diagnosisTempList=info.diagnosisTempList;
						  $scope.covTempObject.diagnosticsTempList=info.diagnosticsTempList;
						  $scope.covTempObject.medicationTempList=info.medicationTempList;
						  $scope.covTempObject.adviceTempList=info.adviceTempList;
						  $scope.covTempObject.consultOrderTempList=info.consultOrderTempList;
						  $scope.covTempObject.observationTempList=info.observationTempList;
						 
						  
						  var response = coversheetTemplateService.saveCovTempListData($scope.covTempObject);
						  if(response == true){
							  growl.success("Added successfully.!");
							  $('#templateCoversheteetModal').modal('show');
						  }
					  }
					  
					  $scope.printCaseSheet=function(){

						  var data =  {
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "NewCoverSheet.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "Coversheet",
						                 "reportParams": [
						                	 {
								                  "paramName": "Patientid",
								                  "paramType": "Integer",
								                  "paramValue": $scope.encounterDetailsObj.patientId
							                },
							                {
								                  "paramName": "Unitid",
								                  "paramType": "Integer",
								                  "paramValue": $scope.unitId
							                },
							                {
									             "paramName": "Encounter_id",
									             "paramType": "Integer",
									             "paramValue": $scope.encounterDetailsObj.encounterId
										      }
				                 ]
				      };
						    reportService.generate(REPORT_API_URI1,data);
						}
					  
						 //previous casesheet
     				 $scope.getEncounterListForNewCasesheet=function(){
							$scope.newCasesheetList =[];
							var sdate = new Date();
							sdate.setDate(sdate.getDate() - 1);
							$scope.yesterday=sdate;

							var encounterObj={

							unitId :$scope.unitId,
							organizationId :$scope.organizationId,
							patientId : $scope.encounterDetailsObj.patientId ,
							toDate:moment($scope.yesterday).format('YYYY-MM-DD')
							}

							var URI = BASE_URL + ROOT_URL + "/api/opd/getEncounterDetailsForCoversheet";

							BillingGenericService.serviceAction(METHOD_POST,URI,encounterObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){

							$scope.newCasesheetList = response.data.listObject;
							if($scope.newCasesheetList.length > 0){
								$scope.index = $scope.newCasesheetList.length;
								$scope.encounterIdPdf = $scope.newCasesheetList[$scope.index-1].encounterId;
							}
							// if($scope.newCasesheetList.length == 0){
							$scope.previousCaseSheetDate=new Date();
							$scope.previousCoversheet();
							// }
							console.log("$scope.encounterListttttttt", $scope.newCasesheetList);
							});


							}
							$scope.getEncounterListForNewCasesheet();
							
							$scope.objPrint="";
							$scope.previousCoversheet=function(){
							try{
							// index = index - 1;
							if($scope.index > 0){
							$rootScope.startLoader();
							$scope.index = $scope.index - 1;
							$scope.previousCaseSheetDate=$scope.newCasesheetList[$scope.index].encounterDateString;

							var data={
							"encounterId": $scope.encounterIdPdf,
							"encounterDateString": moment($scope.previousCaseSheetDate).format("YYYY-MM-DD"),
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId
							}
							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
							if(response.data.status == "success"){
							$scope.objPrint = response.data.object;
							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							$rootScope.stopLoader();

							}
							});
							}
							}catch(e){
							console.log(e.message);
							}
							}


							$scope.getNextCaseShhet=function(){
							try{
							// index = (index + 1) <= listCoversheet.length ?
							// (index + 1): listCoversheet.length;
							if($scope.index < $scope.newCasesheetList.length-1 ){
							$rootScope.startLoader();
							$scope.index = $scope.index + 1
							$scope.previousCaseSheetDate=$scope.newCasesheetList[$scope.index].encounterDateString;
							var data={
							"encounterId": $scope.encounterIdPdf,
							"encounterDateString": moment($scope.previousCaseSheetDate).format("YYYY-MM-DD"),
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId
							}
							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
							if(response.data.status == "success"){
							$scope.objPrint = response.data.object;
							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							$rootScope.stopLoader();
							}
							});
							}
							}catch(e){
							console.log(e);
							}
							}
							 $scope.dateOptionsNewCov =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							$scope.openDatePickerNewCov = function() {
								$scope.datepickerNewCov.opened = true;
							};
	
							$scope.datepickerNewCov = {
									opened : false
							};

							$scope.getDateForCaseSheet=function(date){
								$rootScope.startLoader();
								var flag=false;
								angular.forEach($scope.newCasesheetList ,function(val, i){
									var selectDate=moment(date).format("DD-MM-YYYY");
									var existDate=moment(val.encounterDateString).format("DD-MM-YYYY");
									if(selectDate==existDate){
										
										var data={
							"encounterId": val.encounterId,
							"encounterDateString": selectDate,
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId
							}
							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
							if(response.data.status == "success"){
								$rootScope.stopLoader();
							$scope.objPrint = response.data.object;
//							$scope.previousCaseSheetDate=val.encounterDateString;
							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							
							

							}else{
								$rootScope.stopLoader();
							}
//							$rootScope.stopLoader();
							});
										
//										flag=true;
									}else{
										$rootScope.stopLoader();
									}
//									
								
								});
								
								
							}
							
							$scope.confirmSaveTemplate=function(){
								 $('#templateCoversheteetModal').modal('hide');
								 $('#confirmSaveCovTemp').modal('show');
							}
							
							
							  /************************Custom EMR Code********************************************************************/
							  //For currentcasesheet
							  $scope.getCustomeList=function(){
									 $rootScope.startLoader();
									var data = {
											'doctorId' : $scope.encounterDetailsObj.doctorId,
											'unitId':$scope.unitId,
											'organizationId':$scope.organizationId,
											'patientId' : $scope.encounterDetailsObj.patientId,

										};
									$scope.customList = [];
						    		var encounterId = $scope.encounterDetailsObj.encounterId;
						    		var URI = BASE_URL + ROOT_URL + "/api/global/unit/getCustomEMRCategorySubcategoryList" ;

						    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
						    		.then(
						    			function(response) {
						    				if (response.data.status == "success")
						    					$scope.customList = response.data.listObject;
						    			});
								}
								$scope.getCustomeList();
								
								//Update Custom 
								$scope.updateCustom = function(info, index) {
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateCustomEMRCategorySubcategory";
									var data = {
											customEmrConfigDetailsId:info.customEmrConfigDetailsId,
											createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											patientId : $scope.encounterDetailsObj.patientId,
											doctorId :$scope.encounterDetailsObj.doctorId,
											encounterId: $scope.encounterDetailsObj.encounterId,
											organizationId:$scope.organizationId,
											cusEmrCategoryId:info.cusEmrCategoryId,
											cusEmrSubCategoryId:info.cusEmrSubCategoryId,
											unitId:$scope.unitId,
											remark:info.remark,
											
										
									}
									/*console.log("data",data);
									 return false;*/
									 
									$rootScope.startLoader();
									GenericService.serviceAction("POST", URI, data).then(function (response) {
										if (response.data.status == "success")
											
										{
											$scope.getCustomeList();
											$scope.getCustomePatientId();
											$rootScope.stopLoader();
											growl.success("Updated Successfully..!");
											
										}
										else if (response.data.status == "error") {
											growl.warning(response.data.message,{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
									});
								}
								///
								$scope.getCustomePatientId = function(){

									$rootScope.startLoader();
									$scope.listOfAllCustomEMRData=[];
									try{

									var data = {

											patientId : $scope.encounterDetailsObj.patientId,
										    organizationId:$scope.organizationId,
										    unitId:$scope.unitId,

										};

									
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/getAllListoCustomEMRByPatientId";
									GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$scope.listOfAllCustomEMRData=response.data.listObject;
									$rootScope.stopLoader();
									$scope.getCustomeList();
									
									} else if(response.data.status == 'error')
									{
									$rootScope.stopLoader();
									growl.warning(response.data.message,
									{
									title : 'Warning!'
									});
									}
									});
									

									}catch(e){
									console.log("Exception",e);
									}
									// }
								}
								$scope.getCustomePatientId();
								/********************Custom EMR code end*******************************/
		}
	]);


	
