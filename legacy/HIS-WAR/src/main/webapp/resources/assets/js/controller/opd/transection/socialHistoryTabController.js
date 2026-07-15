/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular.module('myApp').controller('socialHistoryTabController',['$scope','$cookies','$rootScope','GenericService',
	'BillingGenericService','$state','$timeout','$interval','$stateParams','GenericServiceParamHeader','growl','$compile',
	'reportService','GenericServiceFileUpload','servicesFactory','CONSTANTS','coversheetTemplateService',
	function($scope, $cookies, $rootScope, GenericService,BillingGenericService, $state, $timeout,$interval,
		$stateParams,GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS,coversheetTemplateService) 
		{

			/***************tab wise code start****************/				
			console.log('$stateParams.appointmentObj',$stateParams.appointmentObj);
			/* init() function for form object create. */
			
			//For cylic
			var cookieObject, cookieObjectAppConfig;
			[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
							
			$scope.init = function() {

				$scope.dateLetter = new Date();
				$scope.deptWiseAppointment='N';

				// object for History family
				$scope.historyFamilyList = [{
					isNoFamilyHistoryStatus:'N',
					diagnosisId:"",
					relationId:"",
					remark:"",
					isEnterInErrorStatus:"N",
					isReviewedFlag:"N",
					isDiagnosisName : 'N',
					diagnosisNameText:'',
					diagnosisCode:''
				}];

				// object for Complaints
				$scope.historyTravelsList = [{
					isNoSignificantTravel:'N',
					description : '',
					isReviewedFlag : 'N',
					isEnterInErrorStatus : 'N',
				}];

				 // object for History Personal
				$scope.historyPersonalList = [{
					isNoSignificantHistoryStatus:'N',
					description:"",
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
				$scope.isAdviceAccess=cookieObjectAppConfig.isAdviceTabVisible;
				$scope.roleName = cookieObjectAppConfig.roleName;
				$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
				$scope.doctorRole = cookieObjectAppConfig.doctorRole;
				$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
				$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
				$scope.configureConsultationSpecialityId=cookieObjectAppConfig.consultationSpecialityId;
				$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
				
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
														
				// -------------- function to check create opd
				// cover sheet based on appointment
				// -------------//
							
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
						BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
						.then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.specialityList = response.data.listObject;
						});
							
						var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						};

						// object for Complaints
						$scope.dataListComplaints = [ {
							diagnosisId : '',
							icpcSystemId : '',
							since :'',
							duration:'Day',
							remark:'',
							isReviewedFlag : 'N',
							complaintAppoId : '0',
							systemName:	'',
							diagnosisName:'',
							icpcCodeText:'',
							nos:'N',
						} ];
							     
						//for Complaint and Diagnosis Template
						$scope.templateListForSave=[];
						var tempObj={};
						tempObj.templateObject={};
						tempObj.diagonisIdforTemplate=0;
						tempObj.icpcSystemId=0;
						tempObj.icpcId=0;
						$scope.templateListForSave.push(tempObj);
	                        	 
						$scope.common = [ {
							diagnosisId : '',
							icpcSystemId : '',
						} ];
				
							     
						if ($scope.encounterDetailsObj == undefined) {
							return;
						}
						var patientId = $scope.encounterDetailsObj.patientId;
						var encounterId = $scope.encounterDetailsObj.encounterId;
							
						if (angular.equals({},$stateParams.obj)) {
							$state.go('OPDcoversheetNew');
							return;
						} else {
							if($stateParams.obj.newCasesheetList != undefined){
								$scope.newCasesheetList=$stateParams.obj.newCasesheetList;
							}
							if($stateParams.obj.objPrint!= undefined ){
								$scope.objPrint = $stateParams.obj.objPrint;
								$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							}
							/*if($stateParams.obj.vitalsList != undefined){
								$scope.vitalsList=$stateParams.obj.vitalsList;
							}*/
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


					/** ***** END TAB FOR ALL PAGES IN TREE LEVEL ****** */
						
					/**FOR SET CODE START****** */
					$scope.tab = 4;
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
					/***Complete consultation****/
					
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
							/*if($scope.diagnosisRequireAppConfig == 'Y'){
								if($scope.patientDiagnosisList.length == 0){
								growl.error("Please add Diagnosis",
								{
									title : 'Error!'
								});
								return false;
								}
							}*/
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
						var datatvgsvx  = data;

						var paramObjForPer={};
						var URI = BASE_URL + ROOT_URL + UPDATEENCOUNTERASSIGNMENT;
						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,
						headerObj).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
							{	// alert("success");
															
								/*	if(roleId==2)
								{
									var data =  {
										"appName": "HIS",
										"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
										"reportDesignFilePath": "OPDCaseSheet.rpt",
										"reportFormat": "PDF",
										"exportFileName": "OPDCoversheet",
										"reportParams": [{
											"paramName": "@EncounterId",
											"paramType": "Integer",
											"paramValue": cookieWObject.encounterId
										}]
									};
									reportService.generate(REPORT_API_URI1,data);
								}*/
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
//                                		SPo2
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
								
					// $scope.checkFocus = function(){
					// 	$timeout(function() {
					// 		document.getElementById("vitalssss_0").focus();
					// 	}, 500);
					// } 
				                    
					$scope.k = function(){
						            	 
						var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						};
				
						var paramObjForVital = {
							//encounterId : $scope.encounterIdVital,
							unitId:$scope.unitId,
							organizationId:$scope.organizationId,
							patientId:$scope.encounterDetailsObj.patientId,
							//roleId:$scope.roleId,
							//flag:'N' 
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
						GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE,data, 
						paramObjForVital,headerObj).then(
				        function(response) {
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
				                        
							// $scope.checkFocus();
				                    
						}
						$scope.k();

						// // HABIT TAB START

						// $scope.setHabitDetails = function() {
						// 	$rootScope.startLoader();
						// 	// START HABIT GET
						// 	var headerObj = {
						// 		unit_id : $scope.unitId,
						// 		organization_id : $scope.organizationId
						// 	};
						// 	var paramObjForPer = {
						// 		patientId:$scope.encounterDetailsObj.patientId,
						// 		encounterId : $scope.encounterDetailsObj.encounterId,
					    // 		roleId : $scope.roleId
						// 	};
						// 	var data = {};
						// 	$scope.habitTypeList = [ {
						// 		"habitId" : 6,
						// 		"habitName" : "Alcohol"
						// 	}, {
						// 		"habitId" : 1,
						// 		"habitName" : "Smoking"
						// 	}, {
						// 		"habitId" : 2,
						// 		"habitName" : "Tobacco chewing"
						// 	}, {
						// 		"habitId" : 3,
						// 		"habitName" : "Gutkha, Pan Masala"
						// 	}, {
						// 		"habitId" : 4,
						// 		"habitName" : "Recreation Drug"
						// 	}, {
						// 		"habitId" : 5,
						// 		"habitName" : "Exercise"
						// 	},];
								
						// 	$scope.calculateSmokingYear=function(idx){
						// 		$scope.packYear=0;
						// 		angular.forEach($scope.habitFinalList,function(value, index) {
						// 			$scope.calculationhabitFinalList=[];
						// 			if (value.habitId == 1) {
						// 				habitId : 1;
						// 				$scope.childHabitObj = {
						// 					habitId : 1,
						// 					cigarettesPerDay : value.childHabitObj[0].cigarettesPerDay,
						// 					yearsSmoked : value.childHabitObj[0].yearsSmoked,
						// 					packYear : (value.childHabitObj[0].cigarettesPerDay/20) * value.childHabitObj[0].yearsSmoked ,
						// 				}
						// 				$scope.habitFinalList[idx].childHabitObj[0].packYear=$scope.childHabitObj.packYear.toFixed(2);
						// 			}			
						// 		});
						// 	}
	                           
						// 	var URI = BASE_URL + ROOT_URL + GETOLDPATIENTHABITDETAILS;
						// 	GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,
						// 	paramObjForPer, headerObj).then(
						// 	function(response) {
						// 		console.log(response);
						// 		if (response.data.status == "success") {
						// 			$scope.patientHabitOldList = response.data.listObject;
						// 			console.log("patinet Habit Old List" + $scope.patientHabitOldList.habitName);
						// 		}
						// 	});						
							
						// 	var headerObj = {
						// 		unit_id : $scope.unitId,
						// 		organization_id : $scope.organizationId
						// 	};
						// 	var paramObjForPer = {
						// 		patientId:$scope.encounterDetailsObj.patientId,
						// 		encounterId : $scope.encounterDetailsObj.encounterId,
						// 		roleId : $scope.roleId
						// 	};
						// 	var data = {};
							
						// 	// For Get ALL patient Habits List
						
						// 	$scope.getActiveHabitDetails=function(){
						// 		$rootScope.startLoader();
						// 		var headerObj = {
						// 			unit_id : $scope.unitId,
						// 			organization_id : $scope.organizationId
						// 		};
									   
						// 		var paramObjForPer = {
						// 			patientId:$scope.encounterDetailsObj.patientId,
						// 			encounterId : $scope.encounterDetailsObj.encounterId,
						// 			roleId : $scope.roleId
						// 		};
											
						// 		var data = {};
											
						// 		var URI = BASE_URL + ROOT_URL + GETCURRENTPATIENTHABITDETAILS;
						// 		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,
						// 		paramObjForPer, headerObj).then(
						// 		function(response) {
						// 			console.log(response);
						// 			// $rootScope.startLoader();
						// 			if (response.data.status == "success")
						// 				$scope.patientHabitList = response.data.listObject;
						// 				// START TEST
						// 				$scope.habitFinalList = [];
						// 				console.log( "HabitList", $scope.habitTypeList);
						// 				console.log( "PatientHabitList", response.data.listObject);
						// 				angular.forEach( $scope.habitTypeList, function( value, key) {
						// 					var checkFlag = false;
						// 					angular.forEach( $scope.patientHabitList, function( value1, key1) {
						// 						if (value.habitId == value1.habitId) {
						// 							checkFlag = true;
						// 							$scope.childObj = [];
						// 							$scope.newArr = {};
						// 							$scope.newArr.habitId = value1.habitId;
						// 							$scope.newArr.habitTypeId = value1.habitTypeId;
						// 							$scope.newArr.remark = value1.remark;
						// 							$scope.newArr.cigarettesPerDay = value1.cigarettesPerDay;
						// 							$scope.newArr.yearsSmoked = value1.yearsSmoked;
						// 							$scope.newArr.leftWhen = value1.leftWhen;
						// 							$scope.newArr.duration = value1.durationOfLeftWhen;
						// 							$scope.newArr.packYear = value1.packYear;
						// 							$scope.newArr.gmsPerDay = value1.gmsPerDay;
						// 							$scope.newArr.yearsUsed = value1.yearsUsed;
						// 							$scope.newArr.mlsPerDay = value1.mlsPerDay;
						// 							$scope.newArr.frequency = value1.frequency;
						// 							$scope.newArr.typeOfExercise = value1.typeOfExercise;
						// 							$scope.newArr.mlsPerDay = value1.mlsPerDay;
						// 							$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
						// 							$scope.newArr.isEnterInErrorStatus = value1.isEnterInErrorStatus;
						// 							if ($scope.roleId == 2) {
						// 								$scope.newArr.patientHabitDetailsId = value1.patientHabitDetailsId;
						// 							}
						// 							if ($scope.roleId == 3) {
						// 								$scope.newArr.patientHabitDetailsId = value1.patientHabitDetailsId;
						// 							}
						// 							$scope.newArr.drug = value1.drug;
						// 							if (value1.habitId == 5) {
						// 								var res = value1.typeOfExercise.split(",");
						// 								// alert(res.length);
						// 								$scope.patient.exercise = [];
						// 								var tdata = {};
						// 								for (var j = 0; j < res.length; j++) {
						// 									if (res[j] != '') {
						// 										$scope.patient.exercise.push(res[j]);
						// 									}
						// 								}
																									
						// 								var check = $scope.patient.exercise;
						// 								if(check.length == 4){
						// 									$scope.habObj.checkAlltest = 'Y';
						// 								}
						// 								// $scope.patient.exercise.push(tdata);
						// 								$scope.newArr.exerciseList = ["Yoga","Weight training","Cardio","Sports",];												
						// 							}
						// 							$scope.childObj.push($scope.newArr);
						// 						}

						// 					});
						// 					if (checkFlag == true) {
						// 						var data = {
						// 							"habitId" : value.habitId,
						// 							"habitName" : value.habitName,
						// 							"childHabitObj" : $scope.childObj
						// 						}
						// 						$scope.habitFinalList.push(data);
						// 					} else {
						// 						$scope.childObj = [];
						// 						var newArr = {};
						// 						newArr.habitId = value.habitId;
						// 						newArr.habitTypeId = '';
						// 						newArr.remark = '';
						// 						newArr.cigarettesPerDay = '';
						// 						newArr.yearsSmoked = '';
						// 						newArr.leftWhen = '';
						// 						newArr.duration = '';
						// 						newArr.packYear = '';
						// 						newArr.gmsPerDay = '';
						// 						newArr.yearsUsed = '';
						// 						newArr.mlsPerDay = '';
						// 						newArr.frequency = '';
						// 						newArr.typeOfExercise = '';
						// 						newArr.mlsPerDay = '';
						// 						newArr.drug = '';
						// 						newArr.isReviewedFlag = 'N';
						// 						newArr.isEnterInErrorStatus = 'N';
						// 						if ($scope.roleId == 2) {
						// 							newArr.patientHabitDetailsId = '';
						// 						}
						// 						if (value.habitId == 5) {
						// 							newArr.exerciseList = ["Yoga","Weight training","Cardio","Sports",];
						// 					}

						// 					$scope.childObj.push(newArr);
						// 					var data = {
						// 						"habitId" : value.habitId,
						// 						"habitName" : value.habitName,
						// 						"childHabitObj" : $scope.childObj
						// 					}
						// 					$scope.habitFinalList.push(data);
						// 					$rootScope.stopLoader();
						// 				}
						// 			});

						// 			console.log("Final Arr Habit",$scope.habitFinalList);			
						// 			// END TEST
						// 		});
						// 	}
						// 	$scope.getActiveHabitDetails();
							
						// 	// STARY HABIT
							
						// 	$scope.updateStatusForAcronymsHabit = function(item)
						// 	{
									
						// 		$scope.habitAcronymsObj = [];
						// 		var perObj = {			
						// 			"habitId": item.habitId,
						// 			"habitTypeId":item.habitTypeId,
						// 			"remark":item.remark,
						// 			"cigarettesPerDay":item.cigarettesPerDay,
						// 			"yearsSmoked":item.yearsSmoked,
						// 			"gmsPerDay":item.gmsPerDay,
						// 			"yearsUsed":item.yearsUsed,
						// 			"mlsPerDay":item.mlsPerDay,
						// 			"frequency":item.frequency,
						// 			"leftWhen":item.leftWhen,
						// 			"packYear":item.packYear,
						// 			"typeOfExercise": item.typeOfExercise,
						// 			"isReviewedFlag":item.isReviewedFlag,
						// 			"isEnterInErrorStatus":item.isEnterInErrorStatus,
						// 			"status":item.status,
						// 			"patientHabitDetailsId":item.patientHabitDetailsId		
						// 		};
						// 		var paramObj = {};
						// 		$scope.habitAcronymsObj.push(perObj);
						// 		var headers={
						// 			'organization_id': $scope.organizationId,
						// 			'unit_id':$scope.unitId,
						// 			'Content-Type':'application/json'
						// 		};
								
						// 		var data = {		
						// 			createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						// 			updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						// 			updatedBy:$scope.userId,
						// 			createdBy:$scope.userId,
						// 			status:'A',
						// 			encounterId : $scope.encounterDetailsObj.encounterId,
						// 			patientId : $scope.encounterDetailsObj.patientId,
						// 			roleId : $scope.roleId,
						// 			listOfPatientHabitDetailsDto : $scope.habitAcronymsObj
						// 		}
										
						// 		console.log(data);
						// 		// return false;
						// 		var URI = BASE_URL + ROOT_URL + UPDATEENTERERRORSTATUSPATIENTHABITDETAILS;
						// 		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,
						// 		paramObj,headers).then(function(response) {
						// 			console.log(response);
						// 			// $rootScope.startLoader();
						// 			if (response.data.status == "success")													
						// 				$scope.init();
						// 				return response.data.listObject;
						// 		});
						// 	}
							
						// 	$scope.savePatientHabits = function()
						// 	{
						// 		$rootScope.startLoader();
						// 		var paramObj = {};
						// 		var headers={
						// 			'organization_id': $scope.organizationId,
						// 			'unit_id':$scope.unitId,
						// 			'Content-Type':'application/json'
						// 		};
						// 		console.log("HHBBTT",$scope.habitFinalList);
						// 		$scope.counterHabit = 0;
						// 		$scope.savePatientHabitFinalArr = [];
						// 		angular.forEach($scope.habitFinalList, function(value, key) {
						// 			if(value.childHabitObj[0].cigarettesPerDay == ""&& value.childHabitObj[0].remark =="" &&
						// 				value.childHabitObj[0].yearsSmoked == ""  &&
						// 				value.childHabitObj[0].gmsPerDay == ""  &&
						// 				value.childHabitObj[0].yearsUsed == ""  &&
						// 				value.childHabitObj[0].mlsPerDay == ""  &&
						// 				value.childHabitObj[0].frequency == ""  &&
						// 				value.childHabitObj[0].leftWhen == ""  &&
						// 				value.childHabitObj[0].packYear == ""  &&
						// 				value.childHabitObj[0].duration == ""  &&
						// 				value.childHabitObj[0].packYear == ""  &&
						// 				value.childHabitObj[0].drug == ""  &&
						// 				value.childHabitObj[0].habitTypeId == ""
						// 			){
						// 				$scope.counterHabit++;
						// 			}
									  
						// 			if(value.childHabitObj[0].habitTypeId > 0 || value.childHabitObj[0].remark != "" )
						// 			{
						// 				var finalExerciseStr = '';
						// 			  	var data = {
						// 					habitId:value.childHabitObj[0].habitId,
						// 					habitTypeId:value.childHabitObj[0].habitTypeId,
						// 					cigarettesPerDay:value.childHabitObj[0].cigarettesPerDay,
						// 					remark:value.childHabitObj[0].remark,
						// 					yearsSmoked:value.childHabitObj[0].yearsSmoked,
						// 					gmsPerDay:value.childHabitObj[0].gmsPerDay,
						// 					yearsUsed:value.childHabitObj[0].yearsUsed,
						// 					mlsPerDay:value.childHabitObj[0].mlsPerDay,
						// 					frequency:value.childHabitObj[0].frequency,
						// 					leftWhen:value.childHabitObj[0].leftWhen,
						// 					packYear:value.childHabitObj[0].packYear,
						// 					typeOfExercise:finalExerciseStr,
						// 					durationOfLeftWhen:value.childHabitObj[0].duration,
						// 					packYear:value.childHabitObj[0].packYear,
						// 					drug:value.childHabitObj[0].drug,
						// 					isReviewedFlag : value.childHabitObj[0].isReviewedFlag,
						// 					isEnterInErrorStatus : value.childHabitObj[0].isEnterInErrorStatus,  
						// 			  	}
						// 				if($scope.roleId == 2)
	                    // 		 		{
	                    //     		 		data.patientHabitDetailsId =value.childHabitObj[0].patientHabitDetailsId;
	                    // 		 		}
						// 				if($scope.roleId == 3)
	                    // 		 		{
	                    //     		 		data.patientHabitDetailsId =value.childHabitObj[0].patientHabitDetailsId;
	                    // 		 		}
						// 				if(value.childHabitObj[0].habitId == 5)
						// 				{
						// 				  	if($scope.patient.exercise)
						// 					{	
						// 						for(var i=0;i < $scope.patient.exercise.length;i++)
						// 						{				
						// 							finalExerciseStr+=$scope.patient.exercise[i]+',';
						// 						}
						// 					}
										  
						// 				  	data.typeOfExercise = finalExerciseStr;
						// 				}
									  
						// 			  	$scope.savePatientHabitFinalArr.push(data);  
						// 		  	}
	                    //        	});
						// 		if($scope.counterHabit == $scope.habitFinalList.length){
						// 			growl.warning("Please Enter Habit",
						// 			{
						// 				title : 'Warning!'
						// 			});
						// 			$rootScope.stopLoader();  
						// 		}
						// 		else{
						// 			console.log("SAVE HBBTT",$scope.savePatientHabitFinalArr);
						// 		  	// return false;
								  
						// 		  	var data = {
						// 				patientId : $scope.encounterDetailsObj.patientId,
	                    //      		 	createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						// 				updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						// 				updatedBy:$scope.userId,
						// 				createdBy:$scope.userId,
						// 				status:'A',
						// 				roleId:$scope.roleId,
						// 				encounterId: $scope.encounterDetailsObj.encounterId,
						// 				listOfPatientHabitDetailsDto : $scope.savePatientHabitFinalArr
						// 		  	}
								  
						// 		  	console.log("Final Habit Save Arr",data);
						// 		 	// return false;
						// 			/*if ($scope.popUpFlag) {
						// 				$scope.showModal = !$scope.showModal;
						// 				$scope.value=2;
						// 			} else {
						// 		  	*/
								  
						// 		  	var URI = BASE_URL + ROOT_URL + SAVEPATIENTHABITDETAILS;
						// 			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,
						// 			paramObj,headers).then(function(response) {
						// 				console.log(response);
						// 				// $rootScope.startLoader();
						// 				if (response.data.status == "success")
						// 					//$scope.init();
						// 					$scope.getActiveHabitDetails();
						// 					$rootScope.stopLoader();
						// 					return response.data.listObject;
						// 			});
						// 		}
									
						// 	}
						// }
						// $scope.setHabitDetails();
						// //End Habit tab

						//Start Family Tab
						$scope.savePatientHabits = function()
						{
							
							$rootScope.startLoader();
							var paramObj = {};
							var headers={
									'organization_id': $scope.organizationId,
									'unit_id':$scope.unitId,
									'Content-Type':'application/json'
							};
							console.log("HHBBTT",$scope.habitFinalList);
							$scope.counterHabit = 0;
							$scope.savePatientHabitFinalArr = [];
							  angular.forEach($scope.habitFinalList, function(value, key) {
								  
									if(value.childHabitObj[0].cigarettesPerDay == ""&& value.childHabitObj[0].remark =="" &&
											value.childHabitObj[0].yearsSmoked == ""  &&
											 value.childHabitObj[0].gmsPerDay == ""  &&
											 value.childHabitObj[0].yearsUsed == ""  &&
											 value.childHabitObj[0].mlsPerDay == ""  &&
											 value.childHabitObj[0].frequency == ""  &&
											value.childHabitObj[0].leftWhen == ""  &&
											 value.childHabitObj[0].packYear == ""  &&
											value.childHabitObj[0].duration == ""  &&
											 value.childHabitObj[0].packYear == ""  &&
										    value.childHabitObj[0].drug == ""  &&
										    value.childHabitObj[0].habitTypeId == ""
									){
										$scope.counterHabit++;
									}
								  
								  if(value.childHabitObj[0].habitTypeId > 0 || value.childHabitObj[0].remark != "" )
									  {
								  var finalExerciseStr = '';
								  var data = {
										  habitId:value.childHabitObj[0].habitId,
										  habitTypeId:value.childHabitObj[0].habitTypeId,
										  cigarettesPerDay:value.childHabitObj[0].cigarettesPerDay,
										  remark:value.childHabitObj[0].remark,
										  yearsSmoked:value.childHabitObj[0].yearsSmoked,
										  gmsPerDay:value.childHabitObj[0].gmsPerDay,
										  yearsUsed:value.childHabitObj[0].yearsUsed,
										  mlsPerDay:value.childHabitObj[0].mlsPerDay,
										  frequency:value.childHabitObj[0].frequency,
										  leftWhen:value.childHabitObj[0].leftWhen,
										  packYear:value.childHabitObj[0].packYear,
										  typeOfExercise:finalExerciseStr,
										  durationOfLeftWhen:value.childHabitObj[0].duration,
										  packYear:value.childHabitObj[0].packYear,
										  drug:value.childHabitObj[0].drug,
										  isReviewedFlag : value.childHabitObj[0].isReviewedFlag,
                      		 			  isEnterInErrorStatus : value.childHabitObj[0].isEnterInErrorStatus,
                      		 	         
										  
								  }
									if($scope.roleId == 2)
                    		 		{
                        		 		data.patientHabitDetailsId =value.childHabitObj[0].patientHabitDetailsId;
                    		 		}
									if($scope.roleId == 3)
                    		 		{
                        		 		data.patientHabitDetailsId =value.childHabitObj[0].patientHabitDetailsId;
                    		 		}
								  
								  if(value.childHabitObj[0].habitId == 5)
									  {
									  
									  if($scope.patient.exercise)
										{
											
											for(var i=0;i < $scope.patient.exercise.length;i++)
												{
													
													finalExerciseStr+=$scope.patient.exercise[i]+',';
												}
										}
									  
									  	data.typeOfExercise = finalExerciseStr;
									  }
								  
								  $scope.savePatientHabitFinalArr.push(data);
								  
							  }
                           });
								 if($scope.counterHabit == $scope.habitFinalList.length){
									  growl.warning("Please Enter Habit",
												{
													title : 'Warning!'
												});
										$rootScope.stopLoader();  
								  }
								 else{
							  console.log("SAVE HBBTT",$scope.savePatientHabitFinalArr);
							  // return false;
							  
							  var data = {
									patientId : $scope.encounterDetailsObj.patientId,
                         		 	createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy:$scope.userId,
									createdBy:$scope.userId,
									status:'A',
									roleId:$scope.roleId,
									encounterId: $scope.encounterDetailsObj.encounterId,
									listOfPatientHabitDetailsDto : $scope.savePatientHabitFinalArr
							  }
							  
							  
							 
							  console.log("Final Habit Save Arr",data);
							 // return false;
						/*	  if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;
									$scope.value=2;

								} else {
							  */
							  
							  var URI = BASE_URL + ROOT_URL
								+ SAVEPATIENTHABITDETAILS;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										data,
										NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
//												$scope.init();
											    $scope.getActiveHabitDetails();
											    $rootScope.stopLoader();
												return response.data.listObject;
										});
								}
								
						}
						
						$scope.relationListData = function() {
							var data = {};
							var URI = BASE_URL + ROOT_URL + GETACTIVERELATIONLIST;
							GenericService.serviceAction("GET", URI, data).then(
							function(response) {
								$scope.relationList = [];
								if (response.data.status == "success") {
									$scope.relationList = response.data.listObject;
								}
							});	
						}
						$scope.relationListData();
						
						$scope.searchGroupListForFamily = function(keyword) {
							var data = {};
							var paramObj = {
								diagnosisName : keyword
							};
							var headerObj = {
								unit_id : $scope.unitId,
								organization_id : $scope.organizationId
							}

							var URI = BASE_URL + ROOT_URL + DIAGNOSISAUTOFILLSEARCH;
							return GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,
							paramObj, headerObj).then(function(response) {
								// $rootScope.startLoader();
								if (response.data.status == "success")
									return response.data.listObject;
							});
						}

						$scope.selectDignosisIdForFamily = function(info, model,label,ind) {
							console.log(info);
							ind = 0;
							$scope.noSignificantFamiliy = 'N';
							$scope.historyFamilyList[ind].diagnosisId = info.diagnosisId;
							$scope.historyFamilyList[ind].diagnosisNameText = info.diagnosisName;
							$scope.familyHistoryConfirm(info);
						}
						
						$scope.selectDignosisIdForUpdateFamily = function(info, model,label,ind) {
							console.log(info);
							$scope.familyUpdateObj.diagnosisId = info.diagnosisId;
						}
							
						$scope.validateFamHisRow =function(idx){
							$scope.checkListFam1=[];
							$scope.checkListFam2=[];
							angular.forEach($scope.historyFamilyList,function(value,index){
								if(idx == index){
									var newArr ={};
									newArr.diagnosisId=value.diagnosisId;
									newArr.diagnosisName=value.diagnosisName;
									newArr.relationId=value.relationId;
									newArr.remark=value.remark;
									$scope.checkListFam1.push(newArr);
								}
							});
							angular.forEach($scope.historyFamilyList,function(value,index){
								if(idx != index){
									var newArr ={};
									newArr.diagnosisId=value.diagnosisId;
									newArr.diagnosisName=value.diagnosisName;
									newArr.relationId=value.relationId;
									newArr.remark=value.remark;
									$scope.checkListFam2.push(newArr);
								}
							});
												
							angular.forEach($scope.checkListFam1, function(value , key) {
								if($scope.historyFamilyList.length > 1){
									if($scope.historyFamilyList!=undefined && $scope.historyFamilyList.length>0){
										var chkStatus= $scope.checkListFam2.filter(function(item)
										{ 
											return (item.diagnosisId==value.diagnosisId && 
											item.relationId==value.relationId);
										})
										if(chkStatus.length>0){
											growl.warning("Family history added.",
											{
												title : 'Warning!'
											});
											$scope.historyFamilyList[idx].diagnosisId="";
											$scope.historyFamilyList[idx].diagnosisName="";
											$scope.historyFamilyList[idx].relationId="";
											$rootScope.stopLoader();
											return false;
										}
									}
								}
							});
						}

						$scope.noSignificantFamiliy = 'N';

						$scope.closeFamilyHistory = function(){
							$scope.historyFamilyList = [{ 
								noSignificantFamiliy : 'N',
								isNoFamilyHistoryStatus : 'N',
								diagnosisId : "",
								relationId : "",
								remark : "",
								isEnterInErrorStatus : "N",
								isReviewedFlag : "N",
							}];
							$("#CheckNosignFamily").hide();
						}
										
						$scope.familyHistoryConfirm = function(info){
							$scope.CheckNosignFamily = true;
							if($scope.noSignificantFamiliy =="N"){
								if(info){
									$scope.CheckNosignFamily = true;
								}
								else{
									angular.forEach($scope.familyHistoryCurrentList, function(value, key) {
										if (value.isNoFamilyHistoryStatus == 'Y') {
											$scope.CheckNosignFamily = false;
										}
									});
								}
							}
							if($scope.CheckNosignFamily){
								$scope.saveHistoryFamily();
							}else{
								$("#CheckNosignFamily").show();
							}
						}
								
						$scope.saveHistoryFamily = function()
						{
							if($scope.noSignificantFamiliy == 'Y')
							{
								// angular.foreach...
								$scope.perFlg = false;
								var count = 0;
								angular.forEach($scope.familyHistoryCurrentList, function(value , key) {
									if(value.isNoFamilyHistoryStatus == 'Y')
									{
										growl.warning("You have already saved.!!!",
										{
											title : 'Warning!'
										});
																		
										$scope.historyFamilyList = [ { 
											noSignificantFamiliy : 'N',
											isNoFamilyHistoryStatus : 'N',
											diagnosisId : "",
											relationId : "",
											remark : "",
											isEnterInErrorStatus : "N",
											isReviewedFlag : "N",
										}];
										// $scope.noSignificantFamiliy = 'N';
										// value.isNoFamilyHistoryStatus = 'N';
										return false;
									} else{
										// $scope.perFlg
										// =
										// false;
										$scope.historyFamilyList = 
										[{
											isNoFamilyHistoryStatus:'Y',
											diagnosisId:"",
											relationId:"",
											remark:"",
											isEnterInErrorStatus:"N",
											isReviewedFlag:"N",
										}];
										count++;
									}
																	
									if($scope.familyHistoryCurrentList.length==count)
									{
										$scope.saveHistoryFamilyFunction();
									}
								});
																 
								if($scope.noSignificantFamiliy == 'Y' && $scope.familyHistoryCurrentList.length==0)
								{
									$scope.historyFamilyList = 
									[{
										isNoFamilyHistoryStatus:'Y',
										diagnosisId:"",
										relationId:"",
										remark:"",
										isEnterInErrorStatus:"N",
										isReviewedFlag:"N",
									}];
									$scope.saveHistoryFamilyFunction();
								}
								else if($scope.familyHistoryCurrentList.length==0){
									$scope.historyFamilyList = 
									[{
										isNoFamilyHistoryStatus:'',
										diagnosisId:"",
										relationId:"",
										remark:"",
										isEnterInErrorStatus:"N",
										isReviewedFlag:"N",
									}];
									$scope.saveHistoryFamilyFunction();									 
								}
							}
							else{
								$scope.saveHistoryFamilyFunction();
							}									
						}

						$scope.removeFamilyHistory = function(item){		
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							var data ={
								updatedBy:$scope.userId,
								updatedDate:$scope.dt,
								familyHistoryId :item.familyHistoryId,
							};
							var URI = BASE_URL + ROOT_URL + "/api/opd/updateStatusRemoveFamilyHistory";
							GenericService.serviceAction("POST",URI,data).then(function(response){
								if (response.data.status == 'success')
								{			
									$rootScope.stopLoader();
									growl.success(response.data.message,
									{
										title : 'Success!'
									});
									$scope.init();
									$scope.getFamilyHistory();
									$scope.setSecondTabLevel(2);
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.warning(response.data.message,
									{
										title : 'Warning!'
									});
								}
							});
						}
								
						$scope.showUpdateFamilyHistory = function(item)
						{
							console.log(item);
							$scope.familyUpdateObj = {
								diagnosisId:item.diagnosisId.toString(),
								relationId:item.relationId,
								remark : item.remark,
								isEnterInErrorStatus : item.isEnterInErrorStatus,
								isReviewedFlag : item.isReviewedFlag,
								familyHistoryId :item.familyHistoryId,
								isNoFamilyHistoryStatus : item.isNoFamilyHistoryStatus,
								diagnosisName:item.diagnosisName,
							};
							$scope.itemRoleId = item.roleId;
							$scope.updateFamilyHistory();
						}
							
							
						$scope.updateFamilyHistory = function()
						{
							$scope.historyFamilyList = [];
							$scope.historyFamilyList.push($scope.familyUpdateObj);
							var data = {
								createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								createdBy:$scope.userId,
								status:'A',
								patientId : $scope.encounterDetailsObj.patientId,
								roleId : $scope.roleId,
								encounterId: $scope.encounterDetailsObj.encounterId,
								listOfPatientFamilyHistoryDtos:$scope.historyFamilyList				
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
							var URI = BASE_URL + ROOT_URL + UPDATEPATIENTFAMILYHISTORY;
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,
							headers).then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.init();
									$scope.getFamilyHistory();
									// growl.success("Updated Successfuully..");
									// $scope.setSecondTabLevel(2);
									// return response.data.listObject;
							});
						}
							
						$scope.updateStatusForAcronymsFamily = function(item)
						{			
							$scope.historyFamilyList = [];
							var perObj = {
								diagnosisId:item.diagnosisId,
								relationId:item.relationId,
								remark : item.remark,
								isEnterInErrorStatus : item.isEnterInErrorStatus,
								isReviewedFlag : item.isReviewedFlag,
								familyHistoryId :item.familyHistoryId,
								isNoFamilyHistoryStatus : item.isNoFamilyHistoryStatus
							};
							var paramObj = {};
								
							$scope.historyFamilyList.push(perObj);
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
								listOfPatientFamilyHistoryDtos : $scope.historyFamilyList
							}			
							console.log(data);
							// return false;
							var URI = BASE_URL + ROOT_URL + UPDATEFAMILYSTATUSENTERINERROR;
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObj,
							headers).then(function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.init();
									$scope.setSecondTabLevel(2);
									$scope.getFamilyHistory();
									return response.data.listObject;
							});	
						}
							
						/*proper function*/
						$scope.savePerFamValiFlag=true;
						$scope.saveHistoryFamilyFunction = function()
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
								roleId : $scope.roleId,									
							};
										
							if($scope.roleId == 3)
							{
								$scope.familyHistoryCurrentListDoctor=[];
								$scope.familyHistoryCurrentListDoctor12=[];
								$scope.savePerFamValiFlag=true;	
								angular.forEach($scope.historyFamilyList, function(value , key) 
								{
									if ((value.diagnosisId > 0 || value.diagnosisNameText != "")) {		
										if($scope.familyHistoryCurrentList!=undefined && $scope.familyHistoryCurrentList.length>0){
	    									var chkStatus= $scope.familyHistoryCurrentList.filter(function(item)
	    									{ 
	    										return (item.diagnosisId==value.diagnosisId && 
												item.relationId==value.relationId);
											})
											if(chkStatus.length>0){
												growl.warning("Already exist",
												{
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												$scope.savePerFamValiFlag=false;
												return false;
											}
										}
										
										if($scope.familyHistoryOldList!=undefined && $scope.familyHistoryOldList.length>0){
	    									var chkStatus= $scope.familyHistoryOldList.filter(function(item)
	    									{ 
												return (item.diagnosisId==value.diagnosisId && 
												item.relationId==value.relationId);
											})
											if(chkStatus.length>0){
												growl.warning("Already exist",
												{
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												$scope.savePerFamValiFlag=false;
												return false;
											}
										}
										var dData = {
											isNoFamilyHistoryStatus : value.isNoFamilyHistoryStatus,
											diagnosisId : value.diagnosisId,
											remark : value.remark,
											isEnterInErrorStatus : value.isEnterInErrorStatus,
											isReviewedFlag : value.isReviewedFlag,
											diagnosisId : value.diagnosisId,
											relationId : value.relationId,
											isDiagnosisName : value.isDiagnosisName,
											diagnosisNameText : value.diagnosisNameText,
											diagnosisCode : value.diagnosisCode,	
										}
										$scope.familyHistoryCurrentListDoctor.push(dData);
										data.listOfPatientFamilyHistoryDtos = $scope.familyHistoryCurrentListDoctor;
									} else if (value.isNoFamilyHistoryStatus == 'Y') {
										var dData = {
											isNoFamilyHistoryStatus : value.isNoFamilyHistoryStatus,
											diagnosisId : value.diagnosisId,
											remark : value.remark,
											isEnterInErrorStatus : value.isEnterInErrorStatus,
											isReviewedFlag : value.isReviewedFlag,
											diagnosisId : value.diagnosisId,
											relationId : value.relationId,
											isDiagnosisName : value.isDiagnosisName,
											diagnosisNameText : value.diagnosisNameText,
											diagnosisCode : value.diagnosisCode,
										}
										$scope.familyHistoryCurrentListDoctor.push(dData);
										data.listOfPatientFamilyHistoryDtos = $scope.familyHistoryCurrentListDoctor;
									} else {
										growl.warning("Please Enter Family History",
										{
											title : 'Warning!'
										});
										$scope.savePerFamValiFlag=false;
										$rootScope.stopLoader();
										return false;
									}
								});
								
								if($scope.CheckNosignFamily == false){
									angular.forEach($scope.familyHistoryCurrentList, function(value, key) {
										if (value.isNoFamilyHistoryStatus == 'Y') {
											var data = {};
											data.isNoFamilyHistoryStatus = 'Y',
											data.diagnosisId = "",
											data.relationId = "",
											data.remark = "",
											data.familyHistoryId = value.familyHistoryId,
											data.isEnterInErrorStatus = "N",
											data.isReviewedFlag = "N",
											data.isDiagnosisName = "N",
											data.diagnosisNameText = "",
											data.diagnosisCode = "",
											$scope.familyHistoryCurrentListDoctor12.push(data);
										}
									});
									
									if($scope.familyHistoryCurrentListDoctor12.length > 0){
										data.listOfPatientFamilyHistoryDtos=[];
										data.listOfPatientFamilyHistoryDtos = $scope.familyHistoryCurrentListDoctor.concat($scope.familyHistoryCurrentListDoctor12);
									}
								}
								
								if($scope.savePerFamValiFlag){
									var paramObj = {};
									var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
									};
									// return false;
									var URI = BASE_URL + ROOT_URL + SAVEPATIENTFAMILYHISTORY;
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_TRUE,
									paramObj,headers).then(function(response) {
										console.log(response);
										if (response.data.status == "success")
											$scope.getFamilyHistory();
											$scope.noSignificantFamiliy='N';
											$scope.init();
											$("#CheckNosignFamily").hide();
											$scope.setSecondTabLevel(2);
											return response.data.listObject;
									});
								}
							}
							else{
								console.log('ds',$scope.familyHistoryCurrentListDoctor);
								$scope.familyHistoryCurrentListDoctor=[];
								$scope.familyHistoryCurrentListDoctor12=[];
						
								$scope.familyHistoryCurrentList
								$scope.savePerFamValiFlag=true;
								angular.forEach($scope.historyFamilyList, function(value , key) 
								{
									if (value.diagnosisId > 0) {
										if($scope.familyHistoryCurrentList!=undefined && $scope.familyHistoryCurrentList.length>0){
    										var chkStatus= $scope.familyHistoryCurrentList.filter(function(item)
    										{ 
    											return (item.diagnosisId==value.diagnosisId && 
												item.relationId==value.relationId);
											})
											if(chkStatus.length>0){
												growl.warning("Already exist",
												{
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												$scope.savePerFamValiFlag=false;
												return false;
											}
										}
									
										if($scope.familyHistoryOldList!=undefined && $scope.familyHistoryOldList.length>0){
    										var chkStatus= $scope.familyHistoryOldList.filter(function(item)
    										{ 
    											return (item.diagnosisId==value.diagnosisId && 
												item.relationId==value.relationId);
											})
											if(chkStatus.length>0){
												growl.warning("Already exist",
												{
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												$scope.savePerFamValiFlag=false;
												return false;
											}
										}
										var dData = {
											isNoFamilyHistoryStatus : value.isNoFamilyHistoryStatus,
											diagnosisId : value.diagnosisId,
											remark : value.remark,
											isEnterInErrorStatus : value.isEnterInErrorStatus,
											isReviewedFlag : value.isReviewedFlag,
											diagnosisId : value.diagnosisId,
											relationId : value.relationId,
											isDiagnosisName : value.isDiagnosisName,
											diagnosisNameText : value.diagnosisNameText,
											diagnosisCode : value.diagnosisCode,	
										}
										$scope.familyHistoryCurrentListDoctor.push(dData);
										data.listOfPatientFamilyHistoryDtos = $scope.familyHistoryCurrentListDoctor;
									} else if (value.isNoFamilyHistoryStatus == 'Y') {
										var dData = {
											isNoFamilyHistoryStatus : value.isNoFamilyHistoryStatus,
											diagnosisId : value.diagnosisId,
											remark : value.remark,
											isEnterInErrorStatus : value.isEnterInErrorStatus,
											isReviewedFlag : value.isReviewedFlag,
											diagnosisId : value.diagnosisId,
											relationId : value.relationId,
											isDiagnosisName : value.isDiagnosisName,
											diagnosisNameText : value.diagnosisNameText,
											diagnosisCode : value.diagnosisCode,
										}
										$scope.familyHistoryCurrentListDoctor.push(dData);
										data.listOfPatientFamilyHistoryDtos = $scope.familyHistoryCurrentListDoctor;
									} else {
										growl.warning("Please Enter Family History",
										{
											title : 'Warning!'
										});
										$scope.savePerFamValiFlag=false;
										$rootScope.stopLoader();
										return false;
									}
								});
							
							
								if($scope.CheckNosignFamily == false){
									angular.forEach($scope.familyHistoryCurrentList, function(value, key) {
										if (value.isNoFamilyHistoryStatus == 'Y') {
											var data = {};
											data.isNoFamilyHistoryStatus = 'Y',
											data.diagnosisId = "",
											data.relationId = "",
											data.remark = "",
											data.familyHistoryId = value.familyHistoryId,
											data.isEnterInErrorStatus = "N",
											data.isReviewedFlag = "N",
											data.isDiagnosisName = "N",
											data.diagnosisNameText = "",
											data.diagnosisCode = "",
											$scope.familyHistoryCurrentListDoctor12.push(data);
										}
									});
								
									if($scope.familyHistoryCurrentListDoctor12.length > 0){
										data.listOfPatientFamilyHistoryDtos=[];
										data.listOfPatientFamilyHistoryDtos = $scope.familyHistoryCurrentListDoctor.concat($scope.familyHistoryCurrentListDoctor12);
									}
								}
							
								if($scope.savePerFamValiFlag){
									var paramObj = {};
									var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
									};
									// return false;
									var URI = BASE_URL + ROOT_URL + SAVEPATIENTFAMILYHISTORY;
					
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,
									paramObj,headers).then(function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
									        $scope.getFamilyHistory();
									        $scope.noSignificantFamiliy='N';
											$scope.famHisData.diagnosisName = "";
									        $("#CheckNosignFamily").hide();
									        $scope.init();
									        // $scope.setSecondTabLevel(2);
									});
							   	}
							}		           
						}	

						$scope.dt = moment(new Date()).format('DD-MM-YYYY');
						var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						};

						var paramObjForHistList = {
							patientId : $scope.encounterDetailsObj.patientId,
							created_date:$scope.dt
						};
							
						$scope.getFamilyHistory = function(){
						var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentPatientFamilyHistoryForCoversheet";
						GenericServiceParamHeader.serviceAction(METHOD_GET, URI,{},NOTIFICATION_MSG_STATUS_FALSE,
						paramObjForHistList,headerObj).then(
						function(response) {
							console.log(response);				
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.familyHistoryCurrentList = response.data.listObject;
								angular.forEach($scope.familyHistoryCurrentList, function(r) {
									if(r.relationId){
										r.relationId = r.relationId.toString();
									}
								});	

								$scope.familyHistoryCurrentListDoctor = [];
								if($scope.roleId == '2' )
								{
									angular.forEach(response.data.listObject, function(value,key) {
										if(value.roleId == 3 && value.isReviewedFlag == 'N')
										{
											var data = {};
											data.isNoFamilyHistoryStatus = value.isNoFamilyHistoryStatus;
											data.remark = value.remark;
											data.isReviewedFlag = 'N',
											data.isEnterInErrorStatus='N',
											data.diagnosisId = value.diagnosisId,
											data.relationId = value.relationId,
											data.familyHistoryId  = value.familyHistoryId;
											$scope.familyHistoryCurrentListDoctor.push(data);
										}
									});
								}			
							});
						}
						$scope.getFamilyHistory();
						// END FAMILY HISTORY

						//START PERSONAL HISTORY
						$scope.dateOptionsForResolvedPersHist =
						{
							formatYear : 'yyyy',
							showWeeks : false,
							maxDate :new Date()
						};

						$scope.openResolvedPersHistDatePicker = function(index) {
							$scope.resolvedPersHistDatepicker = {
								opened : [ ],
							};		
							if($scope.personalHistoryCurrentList != undefined){
								angular.forEach($scope.personalHistoryCurrentList, function(val) {
									$scope.resolvedPersHistDatepicker.opened.push(false);
								});
							}
							for (var i = 0; i < $scope.personalHistoryCurrentList.length; i++) {
								if (i === index) {
									$scope.resolvedPersHistDatepicker.opened[i] = true;
								} else {
									$scope.resolvedPersHistDatepicker.opened[i] = false;
								}
							}
						};
						
						$scope.dt = moment(new Date()).format('DD-MM-YYYY');
						var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						};

						var paramObjForHistList = {
							patientId : $scope.encounterDetailsObj.patientId,
							created_date:$scope.dt
						};

						$scope.saveResolvedPersHistDate=function(ResolvedDatePersHist, resolvedHistoryRemark, info){
							$rootScope.startLoader();
							try
							{
								var data={
									personalHistoryDetailsId: info.personalHistoryDetailsId,
									resolvedDate:moment(ResolvedDatePersHist).format('DD-MM-YYYY HH:mm:ss'),
									resolvedRemark:resolvedHistoryRemark,				
								};
								
								if((ResolvedDatePersHist != null && ResolvedDatePersHist !="" && ResolvedDatePersHist != undefined)&&
									(resolvedHistoryRemark != null && resolvedHistoryRemark !="" && resolvedHistoryRemark != undefined))
								{
									var URI = BASE_URL + ROOT_URL  + "/api/opd/saveResolvedPersonalHistoryDate";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
											{
												title : 'Success!'
											});
										}else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.warning(response.data.message,
											{
												title : 'Warning!'
											});
										}
									});
								}else{
									growl.warning("Please enter date and remark.",
									{
										title : 'warning!'
									});
									$rootScope.stopLoader();
								}
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						$scope.getCurrentPatientpersonalHistory=function(){
							var data = {};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentPatientPersonalHistoryDetailsForCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,
							paramObjForHistList,headerObj).then(
							function(response) {
								console.log(response);			
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.personalHistoryCurrentList = response.data.listObject;
									angular.forEach($scope.personalHistoryCurrentList, function(d) {
										d.createdDate = moment(d.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
									});			
									$scope.personalHistoryCurrentListDoctor = [];
									if($scope.roleId == '2' )
									{
										angular.forEach(response.data.listObject, function(value,key) {					
											if(value.roleId == 3 && value.isReviewedFlag == 'N')
											{
												var data = {};
												data.isNoSignificantHistoryStatus = value.isNoSignificantHistoryStatus;
												data.description = value.description;
												data.remark = value.remark;
												data.isReviewedFlag = 'N',
												data.isEnterInErrorStatus='N',
												data.personalHistoryDetailsId  = value.personalHistoryDetailsId;
												$scope.personalHistoryCurrentListDoctor.push(data);
											}
										});
									}
							});
						}
						$scope.getCurrentPatientpersonalHistory();
							
						$scope.getResolvePersonalHistoryData=function(){
							$rootScope.startLoader();
							var data = {};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getResolvedPatientPersonalHistoryDetailsForCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,
							paramObjForHistList,headerObj).then(
							function(response) {
								console.log(response);
								$scope.resolvedPersonalHistoryList=[];
								if (response.data.status == "success"){
									$scope.resolvedPersonalHistoryList = response.data.listObject;
									angular.forEach($scope.resolvedPersonalHistoryList,function(value,index)
									{
										value.resolvedDate = moment(value.resolvedDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
									});
									$rootScope.stopLoader();
								}else{
									$rootScope.stopLoader();
								}					
							});
						}
						$scope.getResolvePersonalHistoryData();
						
						$scope.showUpdatePerosnalHistory = function(item)
						{
							console.log(item);
							$scope.personalUpdateObj = {		
								description:item.description,
								remark : item.remark,
								isEnterInErrorStatus : item.isEnterInErrorStatus,
								isReviewedFlag : item.isReviewedFlag,
								personalHistoryDetailsId :item.personalHistoryDetailsId,
								isNoSignificantHistoryStatus : item.isNoSignificantHistoryStatus
							};
							$scope.personalHistoryDetailsId = item.personalHistoryDetailsId;
							$scope.itemRoleId = item.roleId;
							$scope.updatePersonalHistory();
						}
						
						$scope.updatePersonalHistory = function()
						{
							$scope.historyPersonalList = [];
							$scope.historyPersonalList.push($scope.personalUpdateObj);
							var data = {
								createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								createdBy:$scope.userId,
								status:'A',
								patientId : $scope.encounterDetailsObj.patientId,
								roleId : $scope.roleId,
								encounterId: $scope.encounterDetailsObj.encounterId,
								listOfPatientPersonalHistoryDtos:$scope.historyPersonalList	
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
							var URI = BASE_URL + ROOT_URL + UPDATEPATIENTPERSONALHISTORYDETAILS;
					
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,
							paramObj,headers).then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.init();
									// $scope.setSecondTabLevel(1);
									$scope.getCurrentPatientpersonalHistory();
									// return response.data.listObject;
							});
						}
						
						
						$scope.updateStatusForAcronyms = function(item)
						{		
							$scope.historyPersonalList = [];
							var perObj = {			
								description:item.description,
								remark : item.remark,
								isEnterInErrorStatus : item.isEnterInErrorStatus,
								isReviewedFlag : item.isReviewedFlag,
								personalHistoryDetailsId :item.personalHistoryDetailsId,
								isNoSignificantHistoryStatus : item.isNoSignificantHistoryStatus
							};
							var paramObj = {};
							
							$scope.historyPersonalList.push(perObj);
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
								listOfPatientPersonalHistoryDtos : $scope.historyPersonalList
							}
									
							console.log(data);
							// return false;
							var URI = BASE_URL + ROOT_URL + UPDATESTATUSENTERINERROR;
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,
							paramObj,headers).then(function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.init();
									$scope.setSecondTabLevel(1);
									return response.data.listObject;
							});
						}
						
						$scope.savePerHisValiFlag=true;
						$scope.saveHistoryPersonalfunction = function()
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
								roleId : $scope.roleId,	
							}
							$scope.personalHistoryCurrentListDoctor=[];
							if($scope.roleId == 3)
							{
								console.log('ds',$scope.personalHistoryCurrentListDoctor);
								angular.forEach($scope.historyPersonalList, function(value , key) {
									if(( value.description != "") && (value.remark =="" || value.remark !="")){
										var dData = {
											isNoSignificantHistoryStatus : value.isNoSignificantHistoryStatus,
											description:value.description,
											remark : value.remark,
											isEnterInErrorStatus:value.isEnterInErrorStatus,
											isReviewedFlag:value.isReviewedFlag
										}
										$scope.personalHistoryCurrentListDoctor.push(dData);
										data.listOfPatientPersonalHistoryDtos = $scope.personalHistoryCurrentListDoctor;

									}else{
										growl.warning("Please Enter Personal History.",
										{
											title : 'Warning!'
										});
										$scope.savePerHisValiFlag=false;
										$rootScope.stopLoader();
										return false;
									}
								});
								var paramObj = {};
								var headers={
									'organization_id': $scope.organizationId,
									'unit_id':$scope.unitId,
									'Content-Type':'application/json'
								};
								// return false;
						
								if($scope.savePerHisValiFlag){
									var URI = BASE_URL + ROOT_URL + SAVEPATIENTPERSONALHISTORYDETAILS;
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,
									NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											// $scope.setSecondTabLevel(1);
											$scope.noSignificantPersonal='N';
											$scope.getCurrentPatientpersonalHistory();
											$scope.info.description = "";
											$scope.init();
											// console.log("$scope.noSignificantPersonalttttttttttttttttttttttttt",$scope.noSignificantPersonal);
											$rootScope.stopLoader();
											// return response.data.listObject;
									});
								}else {
									growl.warning("Please Enter Personal History.",
									{
										title : 'Warning!'
									});
									$scope.savePerHisValiFlag=true;
									$rootScope.stopLoader();
									return false;
								}
							} 
							else 
							{
								console.log('ds',$scope.personalHistoryCurrentListDoctor);
								angular.forEach($scope.historyPersonalList, function(value , key) {
									if(( value.description != "") && (value.remark =="" || value.remark !="")){
										var dData = {
											isNoSignificantHistoryStatus : value.isNoSignificantHistoryStatus,
											description:value.description,
											remark : value.remark,
											isEnterInErrorStatus:value.isEnterInErrorStatus,
											isReviewedFlag:value.isReviewedFlag
										}
										$scope.personalHistoryCurrentListDoctor.push(dData);
										data.listOfPatientPersonalHistoryDtos = $scope.personalHistoryCurrentListDoctor;
									}
									else{
										growl.warning("Please Enter Personal History.",
										{
											title : 'Warning!'
										});
										$scope.savePerHisValiFlag=false;
										$rootScope.stopLoader();
										return false;		
									}
								});
								var paramObj = {};
								var headers={
									'organization_id': $scope.organizationId,
									'unit_id':$scope.unitId,
									'Content-Type':'application/json'
								};
								if($scope.savePerHisValiFlag ){
									var URI = BASE_URL + ROOT_URL + SAVEPATIENTPERSONALHISTORYDETAILS;
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,
									NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
									.then(function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")		
											$scope.noSignificantPersonal='N';
											$scope.getCurrentPatientpersonalHistory();
											$scope.info.description = "";
											$scope.init();
											// $scope.setSecondTabLevel(1);
											// console.log("$scope.noSignificantPersonalttttttttttttttttttttttttt",$scope.noSignificantPersonal);
											$rootScope.stopLoader();
											// return response.data.listObject;
									});
								}else {
									growl.warning("Please Enter Personal History.",
									{
										title : 'Warning!'
									});
									$scope.savePerHisValiFlag=true;
									$rootScope.stopLoader();
									return false;
								}
							}
						}

						$scope.personalCurrentListdata = 3;
						$scope.showListPersonal = function(value){
							if(value == 1){
								$scope.personalAllList = 1;
							}
							if(value == 2){
								$scope.personalResolvedList = 2;
								$scope.personalAllList = '';
								$scope.personalCurrentListdata = '';
							}
							if(value == 3){
								$scope.personalCurrentListdata = 3;
								$scope.personalResolvedList = '';
							}
						}

						$scope.noSignificantPersonal = 'N';
						
						$scope.saveHistoryPersonal = function(info)
						{
							if($scope.noSignificantPersonal == 'Y')
							{
								$scope.perFlg = false;
								var count = 0;
								angular.forEach($scope.personalHistoryCurrentList, function(value , key) {
									if(value.isNoSignificantHistoryStatus == 'Y')
									{
										growl.warning("You have already saved.!!!",
										{
											title : 'Warning!'
										});
										$scope.perFlg = true;
										return false;
									}
									else
									{
										//$scope.perFlg = false;
										$scope.historyPersonalList = 
										[{
											isNoSignificantHistoryStatus:'Y',
											description:"No Significant Personal History",
											remark:"",
											isEnterInErrorStatus:"N",
											isReviewedFlag:"N",
										}];
										count++;
									}
									
									if($scope.personalHistoryCurrentList.length==count)
									{
										$scope.saveHistoryPersonalfunction();
									}
								});
									
								if($scope.personalHistoryCurrentList.length==0)
								{
									$scope.historyPersonalList = 
									[{
										isNoSignificantHistoryStatus:'Y',
										description:"No Significant Personal History",
										remark:"",
										isEnterInErrorStatus:"N",
										isReviewedFlag:"N",
									}];
									$scope.saveHistoryPersonalfunction();
								}
							}
							else{
								if(info.description){
									$scope.historyPersonalList = [{
										isNoSignificantHistoryStatus:'N',
										description: info.description,
										remark:"",
										isEnterInErrorStatus:"N",
										isReviewedFlag:"N"
									}];
								}
								$scope.saveHistoryPersonalfunction();
							}
						}
						// END PERSONAL HISTORY

							// START TRAVEL HISTORY
							
							$scope.travelCurrentHistory=function(){	  
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
								var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentPatientTravelHistoryDetailsForCoversheet";
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj).then(function(response) {
									console.log(response);
											
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.travelHistoryCurrentList = response.data.listObject;	
										$scope.travelHistoryCurrentListDoctor = [];
										if($scope.roleId == '2' )
										{			
											angular.forEach($scope.travelHistoryCurrentList, function(t) {
												t.whenTravel = moment(t.whenTravel, "DD-MM-YYYY").toDate();
												t.datepickerTravel = t.whenTravel;
											});
											angular.forEach(response.data.listObject, function(value,key) {		
												if(value.roleId == 3 && value.isReviewedFlag == 'N')
												{
													var data = {};
													data.whenTravel = moment(value.whenTravel).format('DD-MM-YYYY HH:mm:ss'),
													data.datepickerTravel = data.whenTravel,
													data.isReviewedFlag = 'N',
													data.isEnterInErrorStatus='N',
													data.description = value.description,
													data.travelHistoryDetailsId  = value.travelHistoryDetailsId;
													$scope.travelHistoryCurrentListDoctor.push(data);
												}
											});
										}
								});
							}
							$scope.travelCurrentHistory();
							
							$scope.closeTravelHistory = function(){
								$("#CheckNosignTravel").hide();
								var datalistTravel = $scope.historyTravelsList;
								for (var i = 0; i < datalistTravel.length; i++) {
									$scope.historyTravelsList[i].description = '';
									$scope.historyTravelsList[i].datepickerTravel = '';
								}
							}

							$scope.noSignificantTravel ="N";

							$scope.travelHistoryConfirm = function(){
								$scope.CheckNosignTravel = true;
								if($scope.noSignificantTravel =="N"){
									angular.forEach($scope.travelHistoryCurrentList, function(value, key) {
										if (value.isNoSignificantTravel == 'Y') {
											$scope.CheckNosignTravel = false;
										}
									});
								}
								if($scope.CheckNosignTravel){
									$scope.saveTravelHistory();
								}else{
									$("#CheckNosignTravel").show();
								}
							}

							$scope.selectTravelHistory = function(info, model, label, ind) {
								var ind = 0;
								$scope.historyTravelsList[ind].description = info.description;
								$scope.travelHistoryConfirm();	
							}

							$scope.noSignificantTravel = 'N';
							$scope.saveTravelHistory = function()
							{
								$rootScope.startLoader();
								$scope.validateTrvHisFlag = true;
								$scope.savePatientTravelHistory = [];
								$scope.travelHistoryCurrentListDoctor=[];
								if($scope.info.description && $scope.info.datepickerTravel1){
									angular.forEach($scope.historyTravelsList, function(value, key) {       	
										var newArr = {};
										newArr.description = $scope.info.description;
										newArr.whenTravel = moment(new Date($scope.info.datepickerTravel1)).format('DD-MM-YYYY HH:mm:ss');
										newArr.isEnterInErrorStatus=value.isEnterInErrorStatus;
										newArr.isReviewedFlag=value.isReviewedFlag;
										newArr.isNoSignificantTravel = value.isNoSignificantTravel;
										$scope.savePatientTravelHistory.push(newArr);
									});
								}
								
								var data = {		
									createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy:$scope.userId,
									createdBy:$scope.userId,
									status:'A',
									encounterId: $scope.encounterDetailsObj.encounterId,
									patientId : $scope.encounterDetailsObj.patientId,
									roleId:$scope.roleId,
									listOfPatientTravelHistoryDtos:$scope.savePatientTravelHistory	
								};
								$scope.travelHistoryCurrentListDoctor1=[];
								if($scope.roleId == 3)
								{
									console.log('ds',$scope.travelHistoryCurrentListDoctor);
									$scope.travelHistoryCurrentListDoctor1=[];
									$scope.travelHistoryCurrentListDoctor12 =[];
									if($scope.noSignificantTravel =="N"){		
										if($scope.CheckNosignTravel == false){
											angular.forEach($scope.travelHistoryCurrentList, function(value, key) {
												if (value.isNoSignificantTravel == 'Y') {
													var data = {};
	        										data.whenTravel = moment($scope.info.datepickerTravel1).format('DD-MM-YYYY HH:mm:ss'),
	        										data.isReviewedFlag = 'N',
	        										data.isEnterInErrorStatus='N',
	        										data.description = $scope.info.description,
	        										data.isNoSignificantTravel='Y',
	        										data.travelHistoryDetailsId  = value.travelHistoryDetailsId;
	        										$scope.travelHistoryCurrentListDoctor12.push(data);
												}
											});
										}
										
										angular.forEach($scope.savePatientTravelHistory, function(value, key) {
											if (value.description != "") {
												var dData = {
													description : value.description,
													whenTravel : value.whenTravel,
													isEnterInErrorStatus : value.isEnterInErrorStatus,
													isReviewedFlag : value.isReviewedFlag,
													isNoSignificantTravel:'N'
												}
												$scope.travelHistoryCurrentListDoctor1.push(dData);
												data.listOfPatientTravelHistoryDtos = $scope.travelHistoryCurrentListDoctor1;
													
											} 
											else 
											{
												growl.warning("Please Enter Travel History",
												{
													title : 'Warning!'
												});
												$scope.validateTrvHisFlag = false;
												$rootScope.stopLoader();
												return false;
											}
										});
										if($scope.travelHistoryCurrentListDoctor12.length >0){
											data.listOfPatientTravelHistoryDtos=[];
											data.listOfPatientTravelHistoryDtos = $scope.travelHistoryCurrentListDoctor1.concat($scope.travelHistoryCurrentListDoctor12);
										}
									}
									else
									{
										angular.forEach($scope.travelHistoryCurrentList, function(value, key) {
											if (value.isNoSignificantTravel == 'Y') {
												growl.warning("You have already saved.!!!",
												{
													title : 'Warning!',
												});
												$scope.historyTravelsList = [{
													isNoSignificantTravel:'N',
													description : '',
													isReviewedFlag : 'N',
													isEnterInErrorStatus : 'N',
												}];
												$scope.noSignificantTravel = "N";
												$rootScope.stopLoader();
												$scope.validateTrvHisFlag = false;
												return false;
											}
										});
										if ($scope.validateTrvHisFlag) {
											angular.forEach($scope.historyTravelsList,function(value, key) {
												var dData = {
													description : $scope.info.description,
													whenTravel : moment($scope.info.datepickerTravel1).format('DD-MM-YYYY HH:mm:ss'),
													isEnterInErrorStatus : value.isEnterInErrorStatus,
													isReviewedFlag : value.isReviewedFlag,
													isNoSignificantTravel:'Y'
												}
												$scope.travelHistoryCurrentListDoctor1.push(dData);
												data.listOfPatientTravelHistoryDtos = $scope.travelHistoryCurrentListDoctor1;
											});	
										}
									}
									var paramObj = {};
									var headers = {
										'organization_id' : $scope.organizationId,
										'unit_id' : $scope.unitId,
										'Content-Type' : 'application/json'
									};
									console.log("testData",data);
									//return false;
									if ($scope.validateTrvHisFlag) {
										var URI = BASE_URL + ROOT_URL + SAVEPATIENTTRAVELHISTORYDETAILS;
										$rootScope.startLoader();
										GenericServiceParamHeader.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE,
										paramObj, headers).then(function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$rootScope.stopLoader();
											if (response.data.status == "success")
												$scope.init();
												$scope.travelCurrentHistory();
												$("#CheckNosignTravel").hide();
												$scope.info.description = "";
												$scope.info.datepickerTravel1 = "";
												$scope.noSignificantTravel = 'N';
												// $scope.setSecondTabLevel(7);
												$rootScope.stopLoader();
												// var datalistTravel = $scope.historyTravelsList;
												// for (var i = 0; i < datalistTravel.length; i++) {
												// 	$scope.historyTravelsList[i].description = '';
												// 	$scope.historyTravelsList[i].datepickerTravel = '';
												// }
												// return response.data.listObject;
										});
									}
								}
								else 
								{
									console.log('ds',$scope.travelHistoryCurrentListDoctor);
									$scope.travelHistoryCurrentListDoctor1=[];
									$scope.travelHistoryCurrentListDoctor12 =[];
									if($scope.noSignificantTravel =="N"){
										if($scope.CheckNosignTravel == false){
											angular.forEach($scope.travelHistoryCurrentList, function(value, key) {
												if (value.isNoSignificantTravel == 'Y') {
													var data = {};
        											data.whenTravel = moment($scope.info.datepickerTravel1).format('DD-MM-YYYY HH:mm:ss'),
        											data.isReviewedFlag = 'N',
        											data.isEnterInErrorStatus='N',
        											data.description = value.description,
        											data.isNoSignificantTravel='Y',
        											data.travelHistoryDetailsId  = value.travelHistoryDetailsId;
        											$scope.travelHistoryCurrentListDoctor12.push(data);
												}
											});
										}
									
										angular.forEach($scope.savePatientTravelHistory, function(value, key) {
											if (value.description != "") {
												var dData = {
													description : value.description,
													whenTravel : value.whenTravel,
													isEnterInErrorStatus : value.isEnterInErrorStatus,
													isReviewedFlag : value.isReviewedFlag,
													isNoSignificantTravel:'N'
												}
												$scope.travelHistoryCurrentListDoctor1.push(dData);
												data.listOfPatientTravelHistoryDtos = $scope.travelHistoryCurrentListDoctor1;		
											} 
											else 
											{
												growl.warning("Please Enter Travel History",
												{
													title : 'Warning!'
												});
												$scope.validateTrvHisFlag = false;
												$rootScope.stopLoader();
												return false;
											}
										});
										if($scope.travelHistoryCurrentListDoctor12.length >0){
											data.listOfPatientTravelHistoryDtos=[];
											data.listOfPatientTravelHistoryDtos = $scope.travelHistoryCurrentListDoctor1.concat($scope.travelHistoryCurrentListDoctor12);
										}
									}
									else{
										angular.forEach($scope.travelHistoryCurrentList, function(value, key) {
											if (value.isNoSignificantTravel == 'Y') {
												growl.warning("You have already saved.!!!",
												{
													title : 'Warning!',
												});
												$scope.historyTravelsList = [{
													isNoSignificantTravel:'N',
													description : '',
													isReviewedFlag : 'N',
													isEnterInErrorStatus : 'N',
												}];
												$scope.noSignificantTravel = "N";
												$rootScope.stopLoader();
												$scope.validateTrvHisFlag = false;
												return false;
											}
										});
										if ($scope.validateTrvHisFlag) {
											angular.forEach($scope.historyTravelsList,function(value, key) {
												var dData = {
													description : $scope.info.description,
													whenTravel : moment($scope.info.datepickerTravel1).format('DD-MM-YYYY HH:mm:ss'),
													isEnterInErrorStatus : value.isEnterInErrorStatus,
													isReviewedFlag : value.isReviewedFlag,
													isNoSignificantTravel:'Y'
												}
												$scope.travelHistoryCurrentListDoctor1.push(dData);
												data.listOfPatientTravelHistoryDtos = $scope.travelHistoryCurrentListDoctor1;
											});	
										}
								
									}
									var paramObj = {};
									var headers = {
										'organization_id' : $scope.organizationId,
										'unit_id' : $scope.unitId,
										'Content-Type' : 'application/json'
									};
								 	console.log("testData",data);
									//return false;
									if ($scope.validateTrvHisFlag) {
										var URI = BASE_URL + ROOT_URL + SAVEPATIENTTRAVELHISTORYDETAILS;
										$rootScope.startLoader();
										GenericServiceParamHeader.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE,
										paramObj, headers).then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$rootScope.stopLoader();
											if (response.data.status == "success")
												$scope.init();
												$scope.travelCurrentHistory();
												$scope.info.description = "";
												$scope.info.datepickerTravel1 = "";
												$scope.noSignificantTravel = 'N';
												$("#CheckNosignTravel").hide();
												// $scope.setSecondTabLevel(7);
												$rootScope.stopLoader();
												// var datalistTravel = $scope.historyTravelsList;
												// for (var i = 0; i < datalistTravel.length; i++) {
												// 	$scope.historyTravelsList[i].description = '';
												// 	$scope.historyTravelsList[i].datepickerTravel = '';
												// }
												// return response.data.listObject;
										});
									}

								}
							}
							
							
							$scope.removeTravelHistory = function(item){
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data ={
									updatedBy:$scope.userId,
									updatedDate:$scope.dt,
									travelHistoryDetailsId :item.travelHistoryDetailsId,
								};
								var URI = BASE_URL + ROOT_URL + "/api/opd/updateStatusRemoveTravelHistory";
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{	
										$rootScope.stopLoader();
										growl.success(response.data.message,
										{
											title : 'Success!'
										});
										$scope.init();
										$scope.travelCurrentHistory();
										// $scope.setSecondTabLevel(7);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.warning(response.data.message,
										{
											title : 'Warning!'
										});
									}
								});
							}
							
							
							$scope.showUpdateTravelHistory = function(item)
							{
								console.log(item);
								$scope.travelUpdateObj = {			
									description:item.description,
									isEnterInErrorStatus : item.isEnterInErrorStatus,
									isReviewedFlag : item.isReviewedFlag,
									travelHistoryDetailsId :item.travelHistoryDetailsId,
								};
								$scope.datepickerWhen = moment(item.datepickerTravel, "DD-MM-YYYY").toDate();
								$scope.itemRoleId = item.roleId;
								$scope.updateTravelHistory();
							}
							
							
							$scope.updateTravelHistory = function()
							{
								$rootScope.startLoader();
								$scope.savePatientTravelHistory = [];
								$scope.travelUpdateObj.whenTravel = moment(new Date($scope.datepickerWhen)).format('DD-MM-YYYY HH:mm:ss');
								$scope.savePatientTravelHistory.push($scope.travelUpdateObj);
								var data = {
									createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy:$scope.userId,
									createdBy:$scope.userId,
									status:'A',
									patientId : $scope.encounterDetailsObj.patientId,
									roleId : $scope.roleId,
									encounterId: $scope.encounterDetailsObj.encounterId,
									listOfPatientTravelHistoryDtos:$scope.savePatientTravelHistory	
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
								var URI = BASE_URL + ROOT_URL + UPDATEPATIENTTRAVELHISTORYDETAILS;
								GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,
								NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.init();
										$scope.travelCurrentHistory();
										// $scope.setSecondTabLevel(7);
										// return response.data.listObject;
										$rootScope.stopLoader();
								});
							}
							
							$scope.updateStatusForAcronymsTravel = function(item)
							{		
								$scope.savePatientTravelHistory = [];
								var perObj = {
									description:item.description,
									isEnterInErrorStatus : item.isEnterInErrorStatus,
									isReviewedFlag : item.isReviewedFlag,
									travelHistoryDetailsId :item.travelHistoryDetailsId,
									whenTravel : item.whenTravel
								};
								var paramObj = {};
								
								$scope.savePatientTravelHistory.push(perObj);
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
									listOfPatientTravelHistoryDtos : $scope.savePatientTravelHistory
								}
								
										
								console.log(data);
								// return false;
								var URI = BASE_URL + ROOT_URL + UPDATEENTERERRORSTATUSPATIENTTRAVELHISTORYDETAILS;
								
								GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,
								NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
								.then(function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.init();
										// $scope.setSecondTabLevel(7);
										return response.data.listObject;
									});
							}

							$scope.dateOptionsTravel =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate :new Date()
							};

							$scope.openDatePickerTravel = function(index) {
								$scope.datepickerTravel = {
									opened : [ ],
								};		
								if($scope.travelHistoryCurrentList != undefined){
									angular.forEach($scope.travelHistoryCurrentList, function(val) {
										$scope.datepickerTravel.opened.push(false);
									});
								}
								for (var i = 0; i < $scope.travelHistoryCurrentList.length; i++) {
									if (i === index) {
										$scope.datepickerTravel.opened[i] = true;
									} else {
										$scope.datepickerTravel.opened[i] = false;
									}
								}
							};

							$scope.dateOptionsTravel1 =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate :new Date()
							};

							$scope.datepickerTravel1 = {
								opened : [ false ],
							};		

							$scope.openDatePickerTravel1 = function() {	
								$scope.datepickerTravel1.opened = true;
							}
							

							// END TRAVEL HISTORY
							
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
//	                                		pulse rate
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
	                                		}
	                                	}
	                                	if(value.vitalId == 3){
//	                                		BP
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
	                                		}
	                                	}
	                                	if(value.vitalId == 14){
//	                                		SPo2
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
	                                		}
	                                	}
	                                	if(value.vitalId == 7){
//	                                		SPo2
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
													 
//													$rootScope.stopLoader();
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
//			 							$scope.previousCaseSheetDate=val.encounterDateString;
			 							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
			 							
			 							

			 							}else{
			 								$rootScope.stopLoader();
			 							}
//			 							$rootScope.stopLoader();
			 							});
			 										
//			 										flag=true;
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
			 							
			 							$scope.valHabit="";
			 							
			 							$scope.selectedStatus=function(val){
			 								$scope.habitTypeId="";
			 								if(val == 1){
			 									$scope.selValHabit=1;	
			 								}
											if(val == 2){
												$scope.habitTypeId=2;		 									
												}
											if(val == 3){
												$scope.habitTypeId=3;	
												}
			 							}
			 							
			 							
			 							 /*$scope.getSearchHabits = function(searchKeyword)
									        {
									        	var data ={
									        			searchKeyword :searchKeyword,
									        			organizationId :$scope.organizationId,
									        			unitId : $scope.unitId
									        	};
									        	
									    		var URI = BASE_URL + ROOT_URL + "/api/opd/getSearchedHabits";
									    		return GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
														data, NOTIFICATION_MSG_STATUS_FALSE)
														.then(function(response) {
															if(response.data.status == "success"){
																$scope.searchedList = [];
																$scope.searchedList = response.data.listObject;
															}
															return $scope.searchedList;
												});
									        }*/
			 							
			 						     //Start for ICPC Master By icpc system Id
			 							$scope.habitDesct="";
			 					        $scope.getSearchHabits = function(searchKeyword){
			 					        	$scope.searchedList=[];
			 								var data={
			 										searchKeyword :searchKeyword,
								        			organizationId :$scope.organizationId,
								        			unitId : $scope.unitId
			 					        	};
			 								var URI = BASE_URL + ROOT_URL + "/api/opd/getSearchedHabits" 
			 								
			 					    		return GenericService.serviceAction("POST",URI,data).then(function(response){
			 					    			if(response.data.status == "success"){
			 					    				$scope.searchedList = response.data.listObject;
			 					    				if($scope.searchedList.length == 0){
			 					    					growl.warning("No habit found",
			 					    							{
			 					    							title : 'Warning!'
			 					    							});
			 					    							$rootScope.stopLoader();
			 					    							return false;
			 					    				}
			 					    			}
			 					    			return $scope.searchedList;
			 					    		});
			 					    	}
			 					       $scope.habitIdSelected=null;
			 							 $scope.getHabitId = function($item, $model, $label,initVal)
									        {
			 								 	
									        	$scope.habitIdSelected=$item.habitId;
									    		
									        }
			 							 
									     $scope.getSavedHabitPatientList = function()
									        {
									        	var data ={
									        			patientId :$scope.encounterDetailsObj.patientId,
									        			organizationId :$scope.organizationId,
									        			unitId : $scope.unitId
									        	};
									        	
									    		var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientHabitsForNewCov";
												GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
														data, NOTIFICATION_MSG_STATUS_FALSE)
														.then(function(response) {
															if(response.data.status == "success"){
																$scope.savedHabitPatientList = [];
																$scope.savedHabitPatientList = response.data.listObject;
															}
												});
									        }
									     $scope.getSavedHabitPatientList();
			 							
			 							
			 							
									     $scope.searchICPCBySystemId = function(searchKeyword,index){
									        	$scope.icpcMasterList=[];
									        	$scope.searchKeywordCodeIndex = index;
									        	var icpcSystemId = $scope.dataListComplaints[index].icpcSystemId;
									        	var icpcId = $scope.dataListComplaints[index].diagnosisId;
												var data={
									        			icpcSyatemId:icpcSystemId,
									        			searchICPC:searchKeyword
									        	};
												var URI = BASE_URL + ROOT_URL + "/api/opd/getMasterListOfICPCSystemForCoversheet" 
												
									    		return GenericService.serviceAction("POST",URI,data).then(function(response){
									    			if(response.data.status == "success"){
									    				$scope.icpcMasterList = response.data.listObject;
									    				if($scope.icpcMasterList.length == 0){
									    					$scope.icpcId = 0;
									    					growl.warning("No complaint found with the selected ICPC system",
									    							{
									    							title : 'Warning!'
									    							});
									    							$rootScope.stopLoader();
									    							return false;
									    				}
									    			}
									    			return $scope.icpcMasterList;
									    		});
									    	}
			 							
			 							
									     $scope.removeFromAdded = function(id)
									        {
									        	var data  = {
									        			patientHabitDetailsId:id,
									        			updatedBy : $scope.userId,
									        			updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									        	}
									        	var URI = BASE_URL + ROOT_URL + "/api/opd/removePatientHabits";
												
												GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
														data, NOTIFICATION_MSG_STATUS_TRUE)
															.then(function(response) {
																if(response.data.status == "success"){
																	 $scope.getSavedHabitPatientList();
																}
												});
												
									        }
			 							$scope.habitObj={
			 										habitId:"",
												  habitTypeId:"",
												  cigarettesPerDay:"",
												  remark:"",
												  yearsSmoked:"",
												  gmsPerDay:"",
												  yearsUsed:"",
												  mlsPerDay:"",
												  frequency:"",
												  leftWhen:"",
												  packYear:"",
												  typeOfExercise:"",
												  durationOfLeftWhen:"",
												  packYear:"",
												  drug:"",
												  isReviewedFlag : "",
		                      		 			  isEnterInErrorStatus :"",
		                      		 			habitDesc:""
			 							};
			 							$scope.habitSave=false;
			 							$scope.habitUpdate=false;
			 							$scope.saveHabitDetailsOfPatient=function(){
			 								$rootScope.startLoader();
			 								
			 								 var finalExerciseStr = '';
			 								 var headers={};
			 								 var paramObj={};
			 								  var data = {
//			 										 patientHabitDetailsId :id != null ? id : null,
			 										patientId : $scope.encounterDetailsObj.patientId,
			 	                         		 	createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			 										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			 										updatedBy:$scope.userId,
			 										createdBy:$scope.userId,
			 										organizationId: $scope.organizationId,
			 										unitId:$scope.unitId,
			 										status:'A',
			 										roleId:$scope.roleId,
			 										encounterId: $scope.encounterDetailsObj.encounterId,
			 										habitId:$scope.habitIdSelected,
													  habitTypeId:$scope.habitTypeId,
													  cigarettesPerDay:$scope.habitObj.cigarettesPerDay,
													  remark:$scope.habitObj.remark,
													  yearsSmoked:$scope.habitObj.yearsSmoked,
													  gmsPerDay:$scope.habitObj.gmsPerDay,
													  yearsUsed:$scope.habitObj.yearsUsed,
													  mlsPerDay:$scope.habitObj.mlsPerDay,
													  frequency:$scope.habitObj.frequency,
													  leftWhen:$scope.habitObj.leftWhen,
													  packYear:$scope.habitObj.packYear,
													  typeOfExercise:$scope.habitObj.finalExerciseStr,
													  durationOfLeftWhen:$scope.habitObj.duration,
													  packYear:$scope.habitObj.packYear,
													  drug:$scope.habitObj.drug,
													  isReviewedFlag : 'N',
			                      		 			  isEnterInErrorStatus : 'N',
			 								  }
			 								  
			 								  var URI = BASE_URL + ROOT_URL
			 									+ "/api/opd/savePatientHabitsDetails";
			 							
			 									GenericServiceParamHeader
			 									.serviceAction(METHOD_POST, URI,
			 											data,
			 											NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
			 									.then(
			 											function(response) {
			 												console.log(response);
			 												if (response.data.status == "success")
			 													$scope.habitObj={
			 				 										habitId:"",
			 													  habitTypeId:"",
			 													  cigarettesPerDay:"",
			 													  remark:"",
			 													  yearsSmoked:"",
			 													  gmsPerDay:"",
			 													  yearsUsed:"",
			 													  mlsPerDay:"",
			 													  frequency:"",
			 													  leftWhen:"",
			 													  packYear:"",
			 													  typeOfExercise:"",
			 													  durationOfLeftWhen:"",
			 													  packYear:"",
			 													  drug:"",
			 													  isReviewedFlag : "",
			 			                      		 			  isEnterInErrorStatus :"",
			 			                      		 			habitDesc:""
			 				 							};
			 												$scope.habitId="";
			 												$scope.habitTypeId="";
			 												$scope.valHabit="";
			 												 $scope.getSavedHabitPatientList();
			 												    $rootScope.stopLoader();
			 											});
			 									}
			 							$scope.showUpdateHabitDetails=function(info){
			 										$scope.habitTypeId=info.habitTypeId;
			 							$scope.habitObj.cigarettesPerDay=info.cigarettesPerDay;
			 							$scope.habitObj.remark=info.remark;
			 							$scope.habitObj.yearsSmoked=info.yearsSmoked;
			 							$scope.habitObj.gmsPerDay=info.gmsPerDay;
			 							$scope.habitObj.yearsUsed=info.yearsUsed;
			 							$scope.habitObj.mlsPerDay=info.mlsPerDay;
			 							$scope.habitObj.frequency=info.frequency;
			 							$scope.habitObj.leftWhen=info.leftWhen;
			 							$scope.habitObj.packYear=info.packYear;
			 							$scope.habitObj.typeOfExercise=info.typeOfExercise;
			 							$scope.habitObj.durationOfLeftWhen=info.durationOfLeftWhen;
			 							$scope.habitObj.packYear=info.packYear;
			 							$scope.habitObj.drug=info.drug;
			 							$scope.habitObj.isReviewedFlag = info.isReviewedFlag;
			 							$scope.habitObj.isEnterInErrorStatus =info.isEnterInErrorStatus;
			 							$scope.habitObj.habitDesc=info.habitDesc;
			 							$scope.habitIdSelected=info.habitId;
			 							}
			 							
			 							$scope.removeHabitDetailsOfPatient=function(id){
			 								$rootScope.startLoader();
			 								 var finalExerciseStr = '';
			 								 var headers={};
			 								 var paramObj={};
			 								  var data = {
			 										updatedBy:$scope.userId,
			 										createdBy:$scope.userId,
			 										organizationId: $scope.organizationId,
			 										unitId:$scope.unitId,
			 										status:'I',
			 										patientHabitDetailsId:id,
			 										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			 								  }
			 								  
			 								  var URI = BASE_URL + ROOT_URL
			 									+ "/api/opd/removePatientHabitsDetails";
			 							
			 									GenericServiceParamHeader
			 									.serviceAction(METHOD_POST, URI,
			 											data,
			 											NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
			 									.then(
			 											function(response) {
			 												console.log(response);
			 												if (response.data.status == "success")
			 												
			 												 $scope.getSavedHabitPatientList();
			 												    $rootScope.stopLoader();
			 											});
			 									}
												$scope.getHabitTemplate=function(){
												
//														$scope.tempTemplateIndex=index;
													
													var data={
														  orgId : $scope.organizationId,
														  unitId : $scope.unitId,
														  doctorId : $scope.encounterDetailsObj.doctorId,
														  specialityId : $scope.encounterDetailsObj.specialityId,
//														  templateTypeId : templateTypeId,
														  patientId : $scope.encounterDetailsObj.patientId,
														  visitTypeId: $scope.encounterDetailsObj.visitTypeId,
														  encounterId: $scope.encounterDetailsObj.encounterId,
														}
													
													var flag=false;
													
													
													if(flag==true){
														growl.warning(
																"Template is already added with this complaint!! Click save to view",
														{
															title : 'Warning!'
															});
													return false;
												}
												
												
												$rootScope.startLoader();
												console.log('data',JSON.stringify(data));
												var URI = BASE_URL + ROOT_URL + "/api/global/templates/habit";
												BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
														{
													   if(response.data.status=='success')
												      {
													   $scope.templateList = response.data.listObject;
													   if($scope.templateList.length==0){
															growl.warning(
																	"No template mapped with selected Habit..!",
																	{
																		title : 'Warning!'
																	});
															return false;
														}
													    
													   	$('#files').modal('show');
													   	$rootScope.stopLoader();
												      }
												   else if(response.data.status=='error'){
													   $rootScope.stopLoader();
													   growl.warning("Template is already filled..!",{
															title : 'Warning!'
												    			 });
														   }
															
													});
												
												}
											   	
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
										        
										        $scope.getTemplateByPatientId=function(templateTypeId,obj){
						                        	 var docId=$scope.encounterDetailsObj.doctorId;
														$rootScope.startLoader();
														var data = {
															orgId : $scope.organizationId,
															unitId : $scope.unitId,
															doctorId : $scope.encounterDetailsObj.doctorId,
															specialityId : $scope.encounterDetailsObj.specialityId == undefined ? 0
																	: $scope.encounterDetailsObj.specialityId,
															templateTypeId : 25,
															patientId : $scope.encounterDetailsObj.patientId,
															visitTypeId : $scope.encounterDetailsObj.visitTypeId,
															encounterId : $scope.encounterDetailsObj.encounterId,
															mIcpcId :obj.diagnosisId
														}

														console.log('data', JSON.stringify(data));
														var URI = BASE_URL + ROOT_URL
																+ "/global/petient/template";
														BillingGenericService
																.serviceAction(METHOD_POST, URI, data,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {

																			if (response.data.status == 'success') {
																				$scope.templateTransactionList = response.data.listObject;
																				$('#viewtemplateModal').modal('show');
																				$rootScope.stopLoader();
																			} else if (response.data.status == 'error') {
																				$rootScope.stopLoader();
																			}
																		});
													
						                         }
										        
										    	$scope.updateHabitRemarkForPatient=function(info){
					 								$rootScope.startLoader();
					 								 var finalExerciseStr = '';
					 								 var headers={};
					 								 var paramObj={};
					 								  var data = {
					 										updatedBy:$scope.userId,
					 										createdBy:$scope.userId,
					 										organizationId: $scope.organizationId,
					 										unitId:$scope.unitId,
					 										status:'I',
					 										patientHabitDetailsId:info.patientHabitDetailsId,
					 										remark:info.remark,
					 										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					 								  }
					 								  
					 								  var URI = BASE_URL + ROOT_URL
					 									+ "/api/opd/removePatientHabitsDetails";
					 							
					 									GenericServiceParamHeader
					 									.serviceAction(METHOD_POST, URI,
					 											data,
					 											NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
					 									.then(
					 											function(response) {
					 												console.log(response);
					 												if (response.data.status == "success")
					 												
					 												 $scope.getSavedHabitPatientList();
					 												    $rootScope.stopLoader();
					 											});
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
//														console.log(headers);
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
//													$scope.getCustomeList();
													
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
