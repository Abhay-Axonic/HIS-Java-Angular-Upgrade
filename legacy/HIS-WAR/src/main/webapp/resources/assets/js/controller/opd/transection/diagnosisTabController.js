/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular.module('myApp').controller('diagnosisTabController',['$scope','$cookies','$rootScope','GenericService',
	'BillingGenericService','$state','$timeout','$interval','$stateParams','GenericServiceParamHeader','growl','$compile',
	'reportService','GenericServiceFileUpload','servicesFactory','CONSTANTS','coversheetTemplateService',
	function($scope, $cookies, $rootScope, GenericService,BillingGenericService, $state, $timeout,$interval,$stateParams,
		GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS,coversheetTemplateService) {
						
		var cookieObject, cookieObjectAppConfig;
		[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
					
		$scope.init = function() 
		{					
							
							 $scope.dateLetter = new Date();
								$scope.deptWiseAppointment='N';
							
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
							
							
					
							var cookieObject = $cookies
									.getObject('cookieObject');
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
//										$scope.isCritical = $scope.encounterDetailsObj.isCritical;
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
								var URI = BASE_URL + ROOT_URL
									+ GETACTIVESPECIALITY;
							     BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
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
//					    		if($stateParams.obj.vitalsList != undefined){
//					    			$scope.vitalsList=$stateParams.obj.vitalsList;
//					    		}
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
						$scope.tab = 8;
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
					  
						/************Complete consultation**************/
						
						
						$scope.updateEncounterDetailsMultilingual=function(info){
							var headerObjS={
									'organization_id': $scope.organizationId,
									'unit_id':$scope.unitId,
									'Content-Type':'application/json'
							};
							
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
										var datatvgsvx  = data;
										
										var paramObjForPer={};
										var URI = BASE_URL + ROOT_URL
										+ UPDATEENCOUNTERASSIGNMENT;
										GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObj)
											.then(
													function(response) {
														console.log(response);
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
//		                                		pulse rate
		                                		if(parseInt(value.value) != 0){
		                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
		                                		}
		                                	}
		                                	if(value.vitalId == 3){
//		                                		BP
		                                		if(parseInt(value.value) != 0){
		                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
		                                		}
		                                	}
		                                	if(value.vitalId == 14){
//		                                		SPo2
		                                		if(parseInt(value.value) != 0){
		                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
		                                		}
		                                	}
		                                	if(value.vitalId == 7){
//		                                		SPo2
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
		                    

			                  /*******For Vitals data**********************/  
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
//			                                		pulse rate
			                                		if(parseInt(value.value) != 0){
			                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
			                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
			                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
			                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
			                                		}
			                                	}
			                                	if(value.vitalId == 3){
//			                                		BP
			                                		if(parseInt(value.value) != 0){
			                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
			                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
			                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
			                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
			                                		}
			                                	}
			                                	if(value.vitalId == 14){
//			                                		SPo2
			                                		if(parseInt(value.value) != 0){
			                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
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
			                       				
//			                       				encounterId : $scope.encounterIdVital,
			                            		   unitId:$scope.unitId,
				                       				organizationId:$scope.organizationId,
				                       				patientId:$scope.encounterDetailsObj.patientId,
//			                       				roleId:$scope.roleId,
//			                       				flag:'N' 
			                       		}
			                    	 var data={
		                    			 	   unitId:$scope.unitId,
		                       				   organizationId:$scope.organizationId,
		                       				   patientId:$scope.encounterDetailsObj.patientId,
		                    	 			}
			                       		console.log("jj",paramObjForVital);
			                    	 $scope.finalVitalObjFromAllVitalList = {};
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
																	} else {
																		// alert("fdvfd");
																		var newArr = {};
																		newArr.vitalId = value.vitalId;
				                                            		 	newArr.minValue = value.minValue;
				                                            		 	newArr.maxValue = value.maxValue;
				                                            		 	newArr.vitalName = value.vitalName;
				                                            		 	newArr.vitalIconPath = value.vitalIconPath;
				                                            		 	newArr.unit = value.unit;
				                                            		 	newArr.vitalValue = value.vitalValue;
//				                                            		 	newArr.vitalValue = 0;
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
				             //shubhangi
		                  //for resolved diagnosis date
							$scope.datepicker = {
										opened : false
									};
									$scope.openDt = function() {
										$scope.popup.opened = true;
									};

									$scope.popup =
									{
										opened : false
									};
									
									
							
						       /* 
						        $scope.resolvedDiagDatepicker = {
										opened : [false],
								};
								
								$scope.openResolvedDiagDatePicker = function(index) {
						        	for (var i = $scope.patientDiagnosisList.length - 1; i >= 0; i--){
						        		if (i === index){
						        			$scope.resolvedDiagDatepicker.opened[i] = false;
						        		}else {
											$scope.resolvedDiagDatepicker.opened[i] = true;
										}
						        	}
								
								};
								
								*/
								/* $scope.dateOptionsForResolvedDiag =
									{
										formatYear : 'yyyy',
										showWeeks : false,
										minDate :new Date()
									};

				             $scope.openResolvedDiagDatePicker = function() {
									$scope.resolvedDiagDatepicker.opened = true;
								};
								$scope.resolvedDiagDatepicker = {
										opened : false
								};*/
									
									
									
									
									$scope.dateOptionsForResolvedDiag =
									{
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate :new Date()
									};

									$scope.openResolvedDiagDatePicker = function(index) {
										$scope.ResolvedDiagDatepicker = {
											opened : [ ],
										};		
										if($scope.patientDiagnosisList != undefined){
											angular.forEach($scope.patientDiagnosisList, function(val) {
												$scope.ResolvedDiagDatepicker.opened.push(false);
											});
										}
										for (var i = 0; i < $scope.patientDiagnosisList.length; i++) {
											if (i === index) {
												$scope.ResolvedDiagDatepicker.opened[i] = true;
											} else {
												$scope.ResolvedDiagDatepicker.opened[i] = false;
											}
										}
									};
									
						
		                    /******************Diagnosis start****************/
	                        //--------Resolved Diagnosis-----------//
	                        $scope.selectedResolvedDiagDate=function(info, resolvedDiagDate, resolvedRemark){
								$scope.patientDiagnosisId= info.patientDiagnosisId;
								$scope.saveResolvedDiagDate(resolvedDiagDate, resolvedRemark);
							}
							
							$scope.saveResolvedDiagDate=function(resolvedDiagDate, resolvedRemark){
								$rootScope.startLoader();
								try{
									var data={
												patientDiagnosisId:$scope.patientDiagnosisId,
												resolvedDate:moment(resolvedDiagDate).format('DD-MM-YYYY HH:mm:ss'),
												resolvedRemark:resolvedRemark,
												isResolved:'Y',
											
									};
									
									if((resolvedDiagDate != null && resolvedDiagDate !="" && resolvedDiagDate != undefined)&&
											(resolvedRemark != null && resolvedRemark !="" && resolvedRemark != undefined)){
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveResolvedDiagnosisDate";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.resolvedDiagDate="";
											$scope.resolvedRemark="";
											$scope.getAllPatientDiagnosis();
											$scope.getAllPatientDiagnosisForIcon();
											$scope.getListOfCancelledDiagnosis();
											$scope.getPatientDiagnosisForMedicalHistory();
											  
										} else if(response.data.status == 'error')
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
							//----------------//
	                        $scope.allReadyExistDia=function(index){
	    								$scope.isDiagnosisExists= true;
	    								$scope.created_date=moment(new Date()).format('DD/MM/YYYY');
	    								angular.forEach($scope.dataListDiagnosis, function(value , key) {
	    								if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
	    									var chkStatus= $scope.patientDiagnosisList.filter(function(item)
	    									{ 
	    										return (item.diagnosisId==value.diagnosisId && 
	    												item.type==value.type &&
	    												item.created_date==$scope.created_date);
	    	    									})
	        	    								if(chkStatus.length>0){
	        	    									growl.warning("Diagnosis already exists.",
	        			    	    							{
	        			    	    							title : 'Warning!'
	        			    	    							});
	        	    								        	$scope.isDiagnosisExists= false;
	        	    								        	value.type='';
	        			    	    							$rootScope.stopLoader();
	        			    	    							return false;
	        	    								}
	    	    								}
	    								});
	    								
	    								$scope.list=[];
	    								$scope.list2=[];
	    								angular.forEach($scope.dataListDiagnosis, function(value , index1) {
	    									if(index1 == index){
	    										var newArr ={};
	    										newArr.diagnosisId=value.diagnosisId;
	    										newArr.type=value.type;
	    										$scope.list.push(newArr);
	    									}
	    								});
	    								angular.forEach($scope.dataListDiagnosis, function(value , index2) {
	    									if(index2 != index){
	    										var newArr ={};
	    										newArr.diagnosisId=value.diagnosisId;
	    										newArr.type=value.type;
	    										$scope.list2.push(newArr);
	    									}
	    								});
	    								
	    								angular.forEach($scope.list, function(value , key) {
	    									if($scope.dataListDiagnosis.length > 1){
	    									if($scope.dataListDiagnosis!=undefined && $scope.dataListDiagnosis.length>0){
	    										var chkStatus= $scope.list2.filter(function(item)
	    										{ 
	    											return (item.diagnosisId==value.diagnosisId && 
	    													item.type==value.type);
	    		    									})
	    	    	    								if(chkStatus.length>0){
	    	    	    									growl.warning("Diagnosis already added.",
	    	    			    	    							{
	    	    			    	    							title : 'Warning!'
	    	    			    	    							});
	    	    	    								        	$scope.isDiagnosisExists= false;
	    	    	    								        	$scope.dataListDiagnosis[index].type='';
	    	    			    	    							$rootScope.stopLoader();
	    	    			    	    							return false;
	    	    	    								}
	    		    								}
	    								     }
	    									});
	    								angular.forEach($scope.list, function(value , key) {
	    									if($scope.dataListDiagnosis.length > 1){
	    									if($scope.dataListDiagnosis!=undefined && $scope.dataListDiagnosis.length>0){
	    										var chkStatus= $scope.list2.filter(function(item)
	    										{ 
	    											return (item.diagnosisId==value.diagnosisId && 
	    													item.type==2);
	    		    									})
	    	    	    								if(chkStatus.length>0){
	    	    	    									growl.warning("Diagnosis is already added as Final diagnosis.",
	    	    			    	    							{
	    	    			    	    							title : 'Warning!'
	    	    			    	    							});
	    	    	    								        	$scope.isDiagnosisExists= false;
	    	    	    								        	$scope.dataListDiagnosis[index].type='';
	    	    			    	    							$rootScope.stopLoader();
	    	    			    	    							return false;
	    	    	    								}
	    		    								}
	    								     }
	    									});
	    								if($scope.isDiagnosisExists){
	    									angular.forEach($scope.dataListDiagnosis, function(value , key) {
	    										if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
	    											var chkStatus= $scope.patientDiagnosisList.filter(function(item)
	    											{ 
	    												return (item.diagnosisId==value.diagnosisId && 
	    														item.type == 2 &&
	    														item.created_date==$scope.created_date);
	    			    									})
	    		    	    								if(chkStatus.length>0){
	    		    	    									growl.warning("Diagnosis is already saved as Final diagnosis.",
	    		    			    	    							{
	    		    			    	    							title : 'Warning!'
	    		    			    	    							});
	    		    	    									$scope.dataListDiagnosis[index].type='';
	    		    	    									$scope.isDiagnosisExists= false;
	    		    			    	    							$rootScope.stopLoader();
	    		    			    	    							return false;
	    		    	    								}
	    			    								}
	    										});
	    									}
	    								
	    							}
	                        
	                        
	                        function isEmpty(obj) {
					    	    for(var key in obj) {
					    	        if(obj.hasOwnProperty(key))
					    	            return true;
					    	    }
					    	    return false;
					    	}
	                        
	                    	$scope.saveDiagnosis = function()
							{
	                    		
	                    		$rootScope.startLoader();
	                    		var selectedOrderSet = [];
	                    		$scope.isDiagnosisExists= true;
	                    		angular.forEach( $scope.dataListDiagnosis, function(value, key) {
	                    			if((value.diagnosisId=="" && value.diagnosisNameText =="") || value.type==""){
	                    				$scope.isDiagnosisExists= false;
		    							$rootScope.stopLoader();
		    							return false;
	                    			}
	                    		});
	                    		if($scope.isDiagnosisExists==false){
	                    			growl.warning("Please Enter Diagnosis Details", {
										title : 'Warning!'
									});
	                    			return false;
	                    		}
	                    		if ($scope.dataListDiagnosis[0].diagnosisId =="" && $scope.dataListDiagnosis[0].diagnosisNameText =="" ){
									growl.warning("Please Enter Diagnosis", {
										title : 'Warning!'
									});
									return false;
								}
								else if($scope.dataListDiagnosis[0].type == "") {
									growl.warning("Please Enter Diagnosis Type", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								}else{
									$scope.isDiagnosisExists= true;
									$scope.created_date=moment(new Date()).format('DD/MM/YYYY');
									angular.forEach($scope.dataListDiagnosis, function(value , key) {
									if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
										var chkStatus= $scope.patientDiagnosisList.filter(function(item)
										{ 
											return (item.diagnosisId==value.diagnosisId && 
													item.type==value.type &&
													item.created_date==$scope.created_date);
		    									})
	    	    								if(chkStatus.length>0){
	    	    									growl.warning("Diagnosis already exists.",
	    			    	    							{
	    			    	    							title : 'Warning!'
	    			    	    							});
	    	    									
	    	    									$scope.isDiagnosisExists= false;
	    			    	    							$rootScope.stopLoader();
	    			    	    							return false;
	    	    								}
		    								}
									});
									
									
									if($scope.isDiagnosisExists){
										angular.forEach($scope.dataListDiagnosis, function(value , key) {
											if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
												var chkStatus= $scope.patientDiagnosisList.filter(function(item)
												{ 
													return (item.diagnosisId==value.diagnosisId && 
															item.type == 2 &&
															item.admissionId == $scope.patientDetails.admissionId);
				    									})
			    	    								if(chkStatus.length>0){
			    	    									growl.warning("Diagnosis already saved as final diagnosis.",
			    			    	    							{
			    			    	    							title : 'Warning!'
			    			    	    							});
			    	    									
			    	    									$scope.isDiagnosisExists= false;
			    			    	    							$rootScope.stopLoader();
			    			    	    							return false;
			    	    								}
				    								}
											});
										}
									
//							if($scope.complaintsPatientList.length > 0){
								if($scope.isDiagnosisExists){
								var data = {
										
										created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updated_by:$scope.userId,
										created_by:$scope.userId,
										status:'A',
										listDiagnosisPatientAppointmentDetailsDto:$scope.dataListDiagnosis,
										encounterId: $scope.encounterDetailsObj.encounterId,
										justificationCancel: 'N',
										
								};
								console.log(data);
								
								var paramObj = {};
								var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json',
										'patient_id' : $scope.encounterDetailsObj.patientId,
								};
								// return false;
								
								var URI = BASE_URL + ROOT_URL
								+ SAVEPATIENTDIAGNOSIS;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
										/*	 $rootScope.startLoader();*/
											if (response.data.status == "success")
                                                $scope.diagnosisName= "";//after add data in list clear search feild
//												$scope.init();
												
												var dataDiagnosisList = $scope.dataListDiagnosis;
											
											var dataDiagnosisList = $scope.dataListDiagnosis;
											for (var i = 0; i < dataDiagnosisList.length; i++) 
											{
												if($scope.dataListDiagnosis[i].selectedOrderSet!=null && $scope.dataListDiagnosis[i].selectedOrderSet.length>0)
													{
													var data = {
															ordersetMasterId : $scope.dataListDiagnosis[i].selectedOrderSet,
															orgId : $scope.organizationId,
															unitId : $scope.unitId,
															doctorId : $scope.encounterDetailsObj.doctorId,
															specialityId :  $scope.encounterDetailsObj.specialityId,
															diagnosisId : $scope.dataListDiagnosis[i].diagnosisId
														}
													  selectedOrderSet.push(data);
													}
										
												
												$scope.dataListDiagnosis[i].diagnosisName = '';
												$scope.dataListDiagnosis[i].type = '';
												$scope.dataListDiagnosis[i].remark = '';
												$scope.dataListDiagnosis[i].diagnosisNameText = '';
												$scope.dataListDiagnosis[i].isDiagnosisName = 'N';
											}
											
											//for Diagnosis template save
											var i;
											$scope.selectedTempList=[];
											for(i=0;i<$scope.dataListDiagnosis.length;i++){
											 if(isEmpty($scope.templateListForSave[i].templateObject)==true){
												 $scope.selectedTempList.push($scope.templateListForSave[i]);
											  }
											}
											if($scope.selectedTempList.length > 0 && selectedOrderSet.length>0){
												 growl.success("Diagnosis,assessment and orders recorded successfully.",{
									    				title : 'Success!'
									    			 });
											}else if(selectedOrderSet.length>0){
												growl.success("Diagnosis and orders recorded successfully.",{
								    				title : 'Success!'
								    			 });
											}else if($scope.selectedTempList.length>0){
												growl.success("Diagnosis and assessment recorded successfully.",{
								    				title : 'Success!'
								    			 });
											}else{
												growl.success("Diagnosis recorded successfully.",{
								    				title : 'Success!'
								    			 });
											}
											
											if(selectedOrderSet.length>0)
												{
												$scope.addOrderSetOrders(selectedOrderSet);
												}
											
										    var promise={};
											if($scope.selectedTempList.length > 0)
											promise=$scope.saveTemplateTransactionList($scope.selectedTempList);
											if(isEmpty(promise)==false){
												 $rootScope.stopLoader();
												$scope.getAllPatientDiagnosis();
												$scope.getAllPatientDiagnosisForIcon();
												 $scope.getListOfCancelledDiagnosis();
												    $rootScope.stopLoader();
											}else{
										     	promise.then(function(response) {
										     		$scope.getAllPatientDiagnosis();
													$scope.getAllPatientDiagnosisForIcon();
													 $scope.getListOfCancelledDiagnosis();
											});
											}
											
											for (var i = 0; i < dataDiagnosisList.length; i++) {
												$scope.dataListDiagnosis[i].diagnosisName = '';
												$scope.dataListDiagnosis[i].type = '';
												$scope.dataListDiagnosis[i].remark = '';
												$scope.dataListDiagnosis[i].diagnosisNameText = '';
												$scope.dataListDiagnosis[i].isDiagnosisName = 'N';
											}
											$scope.dataListDiagnosis = [{
									    	 	diagnosisId : '',
												type :'',
												remark:'',
												diagnosisName:null,
												isDiagnosisName : 'N',
												diagnosisCode:'',
												diagnosisName :null
											} ];
											 $scope.templateListForSave=[];
											 var tempObj={};
				                        	 tempObj.templateObject={};
				                        	 tempObj.diagonisIdforTemplate=0;
				                        	 tempObj.icpcSystemId=0;
				                        	 tempObj.icpcId=0;
				                        	 $scope.templateListForSave.push(tempObj);
											    $scope.selectedOrderSet= [];
												return response.data.listObject;
												  $rootScope.stopLoader();
										});
								
	                    		}
								/*}else{
					        	growl.warning("Please enter the complaint to proceed with the diagnosis recording.",
										{
											title : 'warning!'
										});
								$rootScope.stopLoader();
								return;
					        }*/
								}
	                    		
							}
							
	                    	/***************diagnosis end************************/
	                    	 //cancel Diagnosis
	                		$scope.removeDigOrdderset= function(){
	                			var item=$scope.stopDiagnosisData
//	                			$scope.stopDiagnosis(item);
	                			$( '#cancelDiagnosis').modal('show');
	                		}
	                			
	                		$scope.diagnosisIdForCancelDiagnosis = false;
	                		
	                		$scope.stopDiagnosis=function(item){
	                			
	                			$scope.patientDiagnosisId=item.patientDiagnosisId;
	                			$scope.diagnosisType=item.diagnosisType;
	                			$scope.diagnosisIdForOrderSet=item.diagnosisId;
	                			var data = {
	                					ordersetMasterId:[]	,
	                	        		  orgId : $scope.organizationId,
	                	        		  unitId : $scope.unitId,
	                	        		  doctorId : $scope.encounterDetailsObj.doctorId,
	                	        		  specialityId : $scope.encounterDetailsObj.specialityId,
	                	        		  diagnosisId:$scope.diagnosisIdForOrderSet
	                				}
//	                			if($scope.diagnosisIdForCancelDiagnosis == false){
	                				var URI = BASE_URL + ROOT_URL + GET_ORDER_SET;
	                				BillingGenericService .serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
	                						.then( function(response) {
	                									if (response.data.status == 'success') 
	                									{
	                										$scope.orderSetList = response.data.listObject;
	                										//for cancel order set
	                										if ($scope.orderSetList != null && $scope.orderSetList.length > 0) {
	                											
	                											angular.forEach($scope.orderSetList,function(value,key){
	                												angular.forEach($scope.currentEncounterOrderLists,function(value1,key1){
	                													
	                													if(value.ordersetMasterId != null){
	                														if(value.ordersetMasterId == value1.orderSetId){
	                															if(value1.orderStatus == 'A' || value1.serviceRendered == 0 || value1.serviceIsBilled== 0){
	                															$scope.diagnosisIdForCancelDiagnosis=true;
	                															console.log("$scope.diagnosisIdForCancelDiagnosis",$scope.diagnosisIdForCancelDiagnosis);
	                															$( '#cancelDiagnosisHavingOrderSet').modal('show');
	                															$scope.stopDiagnosisData=item;
	                															}
	                															
	                														}
	                													}
	                												});
	                											});
	                											if($scope.diagnosisIdForCancelDiagnosis == false){
	                												$( '#cancelDiagnosis').modal('show');
	                											}
	                										
	                										}else{
	                											$( '#cancelDiagnosis').modal('show');
	                										}
	                									} 
	                						});
//	                		}
	                			
	                		}
	                		
	                		$scope.cancelDiagnosis = function(justificationCancel){
	                			$scope.justificationCancel=justificationCancel;
	                			$rootScope.startLoader();
	                			try{
//	                				$rootScope.startLoader();
	                				var date = new Date();
	                				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
	                				var data={
	                						updated_by:$scope.userId,
	                						updated_date:$scope.dt,
	                						isDiagnosisCancel:'Y',
	                						justificationCancel:$scope.justificationCancel,
	                						patientDiagnosisId:$scope.patientDiagnosisId,
	                						
	                				};
	                				
	                				if(justificationCancel != null && justificationCancel !="" && justificationCancel != undefined){
	                				var URI = BASE_URL + ROOT_URL + "/api/opd/savePatientCancelledDiagnosis";
	                				GenericService.serviceAction("POST",URI,data).then(function(response){
	                					if (response.data.status == 'success')
	                					{
	                						$rootScope.stopLoader();
	                						growl.success(response.data.message,
	                								{
	                									title : 'Success!'
	                								});
	                						 $scope.getAllPatientDiagnosis();
	                						 $scope.getListOfCancelledDiagnosis();
	                						$scope.justificationCancel="";
	                						$scope.getActiveComplaintlist();
	                						$scope.getAllPatientDiagnosisForIcon();
	                						
	                						angular.forEach($scope.orderSetList,function(value,key){
	                							angular.forEach($scope.currentEncounterOrderLists,function(value1,key1){
	                								
	                								if(value.ordersetMasterId != null){
	                									if(value.ordersetMasterId == value1.orderSetId){
	                										if(value1.orderStatus == 'A' && value1.serviceRendered == 0 && value1.serviceIsBilled== 0){
	                												   $rootScope.startLoader();
	                												   var data = {};
	                												   data.orderDetailsId = value1.orderDetailsId;
	                												   data.ordCancelReasonId = 8;
	                												   data.userId = $scope.userId;
	                												   data.encounterId = $scope.encounterDetailsObj.encounterId;
	                												   data.specialityId =value1.subDeptId;
	                												   data.visitTypeId=$scope.patientDetails.visitTypeId;
	                												   data.preOrPostBilling=cookieObjectAppConfig.isPreOrPostBilling;
	                												   
	                									    		var URI = BASE_URL + ROOT_URL
	                												+ "/api/billing/bill/service/cancel";
	                									    		GenericService.serviceAction("POST", URI, data)
	                												.then(
	                														function(response) {
	                															if (response.data.status == "success") {
	                																growl.success( "Ordered Services from this Orderset cancelled",
	                																		{
	                																			title : SUCCESS_MSG
	                																		});
	                																$scope.getInvetigation();
	                															}
	                														});
	                											
	                											
	                										}
	                										
	                									}
	                								}
	                							});
	                						});
	                						$scope.orderSetList=[];
	                						$scope.diagnosisIdForCancelDiagnosis = false;
	                						
	                					} else if(response.data.status == 'error')
	                					{
	                						$rootScope.stopLoader();
	                						growl.warning(response.data.message,
	                								{
	                									title : 'Warning!'
	                								});
	                					}
	                				});
	                			}else{
	                				growl.warning("Please enter reason to cancel diagnosis.  ",
	                					{
	                						title : 'warning!'
	                					});
	                				$rootScope.stopLoader();
	                		}
	                			}catch(e){
	                				console.log("Exception",e);
	                			}
	                		}
	                		// end cancel
		                  /*  binding-table-data*/
		                    $scope.flagDigByGen=false;
							$scope.searchGroupList = function(keyword,index) {
								  $scope.DiagnosisByGenderList=[];
										
										var data = {
												organizationId : $scope.organizationId,
												diagnosisName : keyword,
												genderId:$scope.patientDetails.genderId
												};
										
										console.log(data);
										$scope.varOr = "|";
										var URI = BASE_URL + ROOT_URL
												+ "/api/opd/getDiagnosisByGenderId";
										
										return GenericService.serviceAction("POST", URI,data)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																 $scope.DiagnosisByGenderList = response.data.listObject;
															
															 $scope.textDiagnosisList=[];
															 $scope.textDiagnosisUniqueList=[];
															 
															 var data={
										    		        			organizationId:$scope.organizationId,
										    		        			searchKeywordText:keyword,
										    		        	};
										    					var URI = BASE_URL + ROOT_URL + "/api/opd/serchFreeTexPatientDiagnosis";
										    					
										    		    		return GenericService.serviceAction("POST",URI,data).then(function(response){
										    		    			if(response.data.status == "success"){
										    		    				$scope.textDiagnosisList = response.data.listObject;
										    		    			}
										    		    			$scope.textDiagnosisList.forEach(function(val) {

										      		    	          var unique = true;

										      		    	          $scope.textDiagnosisUniqueList.forEach(function(val2) {

										      		    	            if (_.isEqual(val.diagnosisName, val2.diagnosisName)) unique = false;

										      		    	          });

										      		    	          if (unique)  $scope.textDiagnosisUniqueList.push(val);

										      		    	        });

										      		    	        $scope.textDiagnosisList = $scope.textDiagnosisUniqueList;
										    		    			return $scope.DiagnosisByGenderList.concat($scope.textDiagnosisList);

										    		    		});
										    		    		
															if($scope.DiagnosisByGenderList.length == 0){
																
																$scope.flagDigByGen=true;
																var newArr = {};
																newArr.diagnosisName="Diagnosis not applicable";
																newArr.diagnosisId='';
																$scope.DiagnosisByGenderList.push(newArr);
																$scope.varOr="";
																return response.data.listObject= $scope.DiagnosisByGenderList;
															} else{
																$scope.flagDigByGen=false;
															}
																return response.data.listObject;
																
														});

									}
		                    
		                	$scope.getFrequentlyUsedDiagnosis = function(){
								var data = {
											unitId: $scope.unitId,
											organizationId:$scope.organizationId,
										
										};
								$scope.frequentlyUsedDiagnosis=[];
								var URI = BASE_URL + ROOT_URL +  "/api/opd/getFrequentlyUsedDiagnosisList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									if(response.data.status == "success"){
										$scope.frequentlyUsedDiagnosis = response.data.listObject;
										console.log("diag", $scope.frequentlyUsedDiagnosis);
									}
									
								});
								}
							$scope.getFrequentlyUsedDiagnosis();
							
							// Selected TypeHead on Diagnosis
							$scope.selectDignosisIdForDiagnosis = function(info, model,
									label,ind) {
								if(info.diagnosisId != undefined && info.diagnosisId != null){
									var ind= "0";
//									console.log(info);
									 $scope.dataListDiagnosis = [{
								    	 	diagnosisId : '',
											type :'',
											remark:'',
											diagnosisName:null,
											isDiagnosisName : 'N',
											diagnosisType : 1,
											diagnosisCode:'',
											diagnosisName :null,
											
										} ];
									$scope.dataListDiagnosis[ind].diagnosisId = info.diagnosisId;
									$scope.dataListDiagnosis[ind].diagnosisName = $scope.diagnosisName;
									$scope.diagonisIdforTemplate = $scope.dataListDiagnosis[ind].diagnosisId;
									$scope.dataListDiagnosis[ind].type = "1";
									$scope.dataListDiagnosis[ind].selectedOrderSet = [];
									$scope.saveDiagnosis();
								}else{
									$scope.saveDiagnosisFreeText();
								}
								
								
							}
							$scope.getAllPatientDiagnosis=function(){
								 var headerObjDia = {
										 	'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : $scope.encounterDetailsObj.patientId,
											'Content-Type':'application/json',
							     };
								 var data={};
	                             var URI = BASE_URL + ROOT_URL
	                             + GETPATIENTDIAGNOSISBYPATIENTID;
	                         GenericServiceParamHeader
	                             .serviceAction(METHOD_GET, URI,
	                                     data,
	                                     NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	                             .then(
	                                     function(response) {
	                                         console.log(response);
	                                         // $rootScope.startLoader();
	                                         if (response.data.status == "success")
	                                             $scope.patientDiagnosisList = response.data.listObject; 
	                                             $scope.complaintDelete=true;
	                                         if($scope.patientDiagnosisList.length > 0)
												{
												  $scope.complaintDelete=false;
												  $scope.comorbidityFlag = true;
												}
	                                         
	                                         $scope.finalString="";
	                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
	                                        	 if(index == 0){
	                                        		 if(value.diagnosisType == 1){
	                                        			 var ext =  "(Prov)";
	                                        		 }else{
	                                        			 var ext =  "(F)";
	                                        		 }
	                                        		 
	                                        	   var String = value.diagnosisName +" "+ext+" ";
	                                        	   $scope.finalString= $scope.finalString.concat(String);
	                                        	 }
	                                        	 else{
	                                        		 
	                                        		 if(value.diagnosisType == 1){
	                                        			 var ext =  "(Prov)";
	                                        		 }else{
	                                        			 var ext =  "(F)";
	                                        		 }
	                                        		 
	                                        	      var String = value.diagnosisName +" "+ext+" ";
		                                        	   $scope.finalString= $scope.finalString.concat(","+String);
	                                        	 }
	                                         });
	                                        
	                                     });
								}
							$scope.getAllPatientDiagnosis();
							$scope.getAllPatientDiagnosisForIcon=function(){
								$scope.patientDiagnosisListIcon =[];
								 var headerObjDia = {
										 	'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : $scope.encounterDetailsObj.patientId,
											'Content-Type':'application/json',
							     };
								 var data={};
	                             var URI = BASE_URL + ROOT_URL
	                             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
	                         GenericServiceParamHeader
	                             .serviceAction(METHOD_GET, URI,
	                                     data,
	                                     NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	                             .then(
	                                     function(response) {
	                                         console.log(response);
	                                         // $rootScope.startLoader();
	                                         if (response.data.status == "success")
	                                             $scope.patientDiagnosisListIcon = response.data.listObject; 
	                                         console.log("patientDiagnosisListIcon",$scope.patientDiagnosisListIcon);
//	                                         	$scope.comorbidityFlag = false;
	                                         	$scope.notifiableFlag = false;
	                                         	$scope.complaintDelete=true;
													if($scope.patientDiagnosisListIcon.length > 0)
														{
														    $scope.complaintDelete=false;
															angular.forEach($scope.patientDiagnosisListIcon,function(value,key) {
																if(value.isNotifiable == 'Y')
																{
																	$scope.notifiableFlag = true;
																}
																else if(value.isComorbidity == 'Y')
																{
//																	$scope.comorbidityFlag = true;
																}
															});
														}
	                                         
	                                     });
								}
							$scope.getAllPatientDiagnosisForIcon();
							//List Of Cancelled Diagnosis
							$scope.getListOfCancelledDiagnosis = function() {
								$scope.cancelledDiagnosisList = [];
								var data = {
										organizationId:$scope.organizationId,
					      				unitId:$scope.unitId,
					      				patientId:$scope.encounterDetailsObj.patientId,
								};
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getListPatientCancelledDiagnosis";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.cancelledDiagnosisList = response.data.listObject;
														console.log("cancelledDiagnosisList",$scope.cancelledDiagnosisList);
													}
												});
							}
							$scope.getListOfCancelledDiagnosis();
							// end List Of Cancelled Diagnosis
							  $scope.updateComplaintIndex="";
								$scope.showUpdateComplaints = function(item,idx)
								{
									$scope.freeTextFlag=false;
									if(item.icpcCodeText != ""){
										$scope.freeTextFlag=true;
									}
									$scope.updateComplaintIndex=idx;
									$scope.complaintsUpdateObj = {
											diagnosisName:item.diagnosisName,
											diagnosisId : item.diagnosisId,
											duration:item.duration,
											since:item.since,
											remark : item.remark,
											icpcSystemId  : item.icpcSystemId.toString(),
											icpcCodeText:item.icpcCodeText,
											systemName:item.systemName
											
									};
									$scope.diagnosisNameUpdate = item.diagnosisName;
									$scope.complaintAppoId = item.complaintAppoId;
									$scope.systemNameUpdate = item.systemName;
									$scope.itemRoleId = item.roleId;
									if(item.remark != null){
										$scope.updateComplaints();
									}
									
								}
								
								$scope.changeDiagnosisType = function(info, ind){

									$scope.isPrimaryExist=false;
									$scope.diagnosisType=1;
									$scope.type=info.type;
									$scope.updateList = [];
									angular.forEach($scope.patientDiagnosisList,function(value,index){
									if(ind == index){
									value.diagnosisType = 0;
									value.type=value.type;
									value.remark=(value.remark != undefined && value.remark != null) ? value.remark:"";
									$scope.patientDiagnosisId=value.patientDiagnosisId;
									$scope.updateList.push(value);
									}
									
									});

								
									$rootScope.startLoader();
									try{

									var data = {
									organizationId:$scope.organizationId,
									patientDiagnosisId:$scope.patientDiagnosisId,
									unitId:$scope.unitId,
									patientId:$scope.encounterDetailsObj.patientId,
									created_date : moment(new Date())
									.format('DD-MM-YYYY HH:mm:ss'),
									updated_date : moment(new Date())
									.format('DD-MM-YYYY HH:mm:ss'),
									updated_by : $scope.userId,
									created_by : $scope.userId,
									status : 'A',
									listDiagnosisPatientAppointmentDetailsDto : $scope.updateList,
									encounterId : $scope.encounterDetailsObj.encounterId,

									};

									if(!$scope.isPrimaryExist){
									var URI = BASE_URL + ROOT_URL + "/api/opd/updatePatientDiagnosisTypeOPD";
									GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{

									growl.success(response.data.message,
									{
									title : 'Success!'
									});
									$rootScope.stopLoader();
									$scope.getAllPatientDiagnosis();
									$scope.getAllPatientDiagnosisForIcon();
									$scope.getListOfCancelledDiagnosis();
									$scope.diagnosisTypeNew="";
									$scope.newType="";
									// $scope.$broadcast('show-errors-reset');
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

									}catch(e){
									console.log("Exception",e);
									}
									// }
								}
							
				                    $scope.k = function(){
						            	 
				                    	  var headerObj = {
												    unit_id : $scope.unitId,
													organization_id : $scope.organizationId
												     };
				                    	
				                    	 var paramObjForVital = {
				                       				
//				                       				encounterId : $scope.encounterIdVital,
				                            		   unitId:$scope.unitId,
					                       				organizationId:$scope.organizationId,
					                       				patientId:$scope.encounterDetailsObj.patientId,
//				                       				roleId:$scope.roleId,
//				                       				flag:'N' 
				                       		}
				                    	 var data={
			                    			 	   unitId:$scope.unitId,
			                       				   organizationId:$scope.organizationId,
			                       				   patientId:$scope.encounterDetailsObj.patientId,
			                    	 			}
				                       		console.log("jj",paramObjForVital);
				                    	 $scope.finalVitalObjFromAllVitalList = {};
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
																		} else {
																			// alert("fdvfd");
																			var newArr = {};
																			newArr.vitalId = value.vitalId;
					                                            		 	newArr.minValue = value.minValue;
					                                            		 	newArr.maxValue = value.maxValue;
					                                            		 	newArr.vitalName = value.vitalName;
					                                            		 	newArr.vitalIconPath = value.vitalIconPath;
					                                            		 	newArr.unit = value.unit;
					                                            		 	newArr.vitalValue = value.vitalValue;
//					                                            		 	newArr.vitalValue = 0;
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
//				                                		pulse rate
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
				                                		}
				                                	}
				                                	if(value.vitalId == 3){
//				                                		BP
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
				                                		}
				                                	}
				                                	if(value.vitalId == 14){
//				                                		SPo2
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
				                                		}
				                                	}
				                                	if(value.vitalId == 7){
//				                                		SPo2
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
																 
//																$rootScope.stopLoader();
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
											  
											  $scope.selectedTemplate=function(covTemplateId){
												  $scope.covListByTemplateId=[];
												  if(covTemplateId == 'custom'){
													  $('#customeEmr').modal('show');
													  return false;
												  }else{
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
											  //
												$scope.getDiagonosis = function(templateTypeId,obj,index) {
													$scope.icpcSystemId = 0;
													$scope.icpcId = 0;
													$scope.tempTemplateIndex=index;
													$scope.diagonisIdforTemplate=obj.diagnosisId;
													if ($scope.diagonisIdforTemplate == 0 ||$scope.diagonisIdforTemplate== 0 ) 
													{
														growl.warning(
																"Please select ICD 10 first...!!",
																{
																	title : 'Warning!'
																});
														return;
													}
													$rootScope.startLoader();
													var data = {
														orgId : $scope.organizationId,
														unitId : $scope.unitId,
														doctorId : $scope.encounterDetailsObj.doctorId,
										        		specialityId : $scope.encounterDetailsObj.specialityId,
										        		templateTypeId : templateTypeId,
										        		patientId : $scope.encounterDetailsObj.patientId,
										        		visitTypeId: $scope.encounterDetailsObj.visitTypeId,
										        		encounterId: $scope.encounterDetailsObj.encounterId,
														diagnosisId : $scope.diagonisIdforTemplate,
														mIcpcSystemId : $scope.icpcSystemId,
														mIcpcId :  $scope.diagonisIdforTemplate
													}

													angular.forEach($scope.templateListForSave,function(value,index){
														if(index==$scope.tempTemplateIndex){
					                            		value.diagonisIdforTemplate=$scope.diagonisIdforTemplate;
					                            		value.icpcSystemId=0;
					                            		value.icpcId=0;
														}
													});
													
													console.log('data', JSON.stringify(data));
													var URI = BASE_URL + ROOT_URL
															+ "/api/global/templates/diagnosis";
													BillingGenericService
															.serviceAction(METHOD_POST, URI, data,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		if (response.data.status == 'success') {
																			$scope.templateList = response.data.listObject;
																			if($scope.templateList.length==0){
																				growl.warning(
																						"No template mapped with selected ICD10 code..!",
																						{
																							title : 'Warning!'
																						});
																				return false;
																			}
																			$('#addtemplateModal')
																					.modal('show');
																			$rootScope.stopLoader();
																		} else if (response.data.status == 'error') {
																			$rootScope.stopLoader();
																			growl
																					.warning(
																							"Template is already filled..!",
																							{
																								title : 'Warning!'
																							});
																		}

																	});
												}
												//
											  $scope.commonFuntion = function(templateTypeId) {
													var templeteData  = {	
													};
													
													var URI = BASE_URL + ROOT_URL+LIS_UNIT
													+ GET_TEMPLATE+"/"+$scope.organizationId+"/"+$scope.unitId+"/0/"+templateTypeId;
													GenericService
													.serviceAction("GET", URI, templeteData)
													.then(
															function(response) {
																$scope.templateList = [];

																if (response.data.status == "success") {
																	$scope.templateList = response.data.listObject;
																
																	
																	CKEDITOR.addTemplates("default", {
																	    imagesPath:CKEDITOR.getUrl(CKEDITOR.plugins.getPath("templates")+"templates/images/"),
																	    templates: $scope.templateList
																	   });
																	
																}
															});

												}
											   /** Template List*/
										        
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
										        		  encounterId: $scope.encounterDetailsObj.encounterId
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
										        		  encounterId: $scope.encounterDetailsObj.encounterId
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
											  //template
											  
											 	$scope.tempTemplateIndex="";
										       	$scope.getComplaintTemplates = function(templateTypeId,obj,index) 
										    	{
										       		$scope.tempTemplateIndex=index;
													$scope.icpcId=obj.diagnosisId;
					                            	$scope.icpcSystemId=obj.icpcSystemId;
												$scope.diagonisIdforTemplate = 0;
												if ($scope.icpcSystemId == 0
														|| $scope.icpcId == 0) {
													growl.warning(
															"Please select ICPC first...!!", {
																title : 'Warning!'
																});
													return;
												}
										    		
										        	var data={
										        		  orgId : $scope.organizationId,
										        		  unitId : $scope.unitId,
										        		  doctorId : $scope.encounterDetailsObj.doctorId,
										        		  specialityId : $scope.encounterDetailsObj.specialityId,
										        		  templateTypeId : templateTypeId,
										        		  patientId : $scope.encounterDetailsObj.patientId,
										        		  visitTypeId: $scope.encounterDetailsObj.visitTypeId,
										        		  encounterId: $scope.encounterDetailsObj.encounterId,
										        		  diagnosisId:$scope.diagonisIdforTemplate,
										        		  mIcpcSystemId : $scope.icpcSystemId,
														  mIcpcId :  $scope.icpcId
										        		}
										        	
										        	var flag=false;
													angular.forEach($scope.templateListForSave,function(value,index){
														if(index==$scope.tempTemplateIndex){
															if(angular.equals({},value.templateObject)){
																value.diagonisIdforTemplate=0;
							                            		value.icpcSystemId=$scope.icpcSystemId;
							                            		value.icpcId=$scope.icpcId;
															}else{
																flag=true;
															}
					                            		
														}
					                            	});
													
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
										    		var URI = BASE_URL + ROOT_URL + "/api/global/templates/complaints";
										    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
										    				{
										    			   if(response.data.status=='success')
										    			      {
										    				   $scope.templateList = response.data.listObject;
										    				   if($scope.templateList.length==0){
																	growl.warning(
																			"No template mapped with selected ICPC code..!",
																			{
																				title : 'Warning!'
																			});
																	return false;
																}
										    				    
										    				   	$('#addtemplateModal').modal('show');
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
										       	//
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
											 	$scope.selectTemplate  = function(templateObj)
										    	{
										    		$scope.template = templateObj;
										    	}
										    	
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
										    	
										    	
										    	$scope.viewTemplate = function(template)
										    	{
										    		$scope.saveTrasactionFlag = false;
													$scope.updateTrasactionFlag = false;
										    		$scope.obj = {};
										    		document.getElementById("htmlContent").innerHTML = JSON.parse(template.templateObject).html;
										    		
										    		$('#viewtemplateModal').modal('hide');
										    		$('#addviewTemplateModal').modal('show');
										    		$scope.obj = JSON.parse(template.templateObject).obj;
										    		var element = angular.element(document.getElementById("htmlContent"));
													   $compile(element)($scope);
													
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
//						 							$scope.previousCaseSheetDate=val.encounterDateString;
						 							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
						 							
						 							

						 							}else{
						 								$rootScope.stopLoader();
						 							}
//						 							$rootScope.stopLoader();
						 							});
						 										
//						 										flag=true;
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
		                                                     
		                                                     //FILTER
		                                                      $scope.diagnosisListFlagCurrent ="Current";
		                                                      $scope.selectedDiagnosisList=function(val){
		                                                          if(val == 1){
		                                                              $scope.diagnosisListFlagAll="All";
		                                                          }
		                                                          if(val == 2){
		                                                              $scope.diagnosisListFlagResolved="Resolved";
		                                                              $scope.diagnosisListFlagAll="";
		                                                              $scope.diagnosisListFlagCurrent =""; 

		                                                          }
		                                                          if(val == 3){
		                                                              $scope.diagnosisListFlagCurrent ="Current";
		                                                              $scope.diagnosisListFlagAll="";
		                                                              $scope.diagnosisListFlagResolved =""; 
		                                                          }
		                                                          
		                                                      }
														/**************************************Order Set*******************************************************************/
								 						// for added diagnosis
								 							$scope.selectedOrderSet= [];
															
															$scope.callOrderset=function(info,index){
																$scope.diagnosisIdForOrderSet=0;
																/*$scope.diagnosisIdForOrderSet = $scope.patientDiagnosisList[index].diagnosisId;*/
																$scope.ordersetFlag=true;
																$scope.getOrderset(info.diagnosisId,index);
															}
															 $scope.addDiagnosis = function(index) 
															 {
																 
																 console.log("index add",index);
																	var data = {
																			diagnosisId : '',
																			type :'',
																			remark:'',
																			diagnosisNameText:'',
																			isDiagnosisName : 'N',
																			diagnosisCode:'',
																			diagnosisName :null
																	};
																	var len = $scope.patientDiagnosisList.length;
																	
																	
																	angular.forEach($scope.patientDiagnosisList,function(value,index1){
																		if(index == index1){
																			if(value.isDiagnosisName == 'N'){
																				if(value.diagnosisId == "" || value.type == "")
																				{
																					growl.warning("Please Enter Diagnosis.",
													    	    							{
													    	    							title : 'Warning!'
													    	    							});
																					return false;
																				}
																				else{
																					$scope.patientDiagnosisList.push(data);
																				}
																				}else{
																					if(value.diagnosisNameText == "" || value.type == "")
																					{
																						growl.warning("Please Enter Diagnosis.",
														    	    							{
														    	    							title : 'Warning!'
														    	    							});
																						return false;
																					}
																					else{
																						$scope.patientDiagnosisList.push(data);
																					}
																					}
																			}
																	});
																	
																	// for diagnosis template
																	var tempObj={};
										                        	 tempObj.templateObject={};
										                        	 tempObj.diagonisIdforTemplate=0;
										                        	 tempObj.icpcSystemId=0;
										                        	 tempObj.icpcId=0;
										                        	 $scope.templateListForSave.push(tempObj);
																};
															
															$scope.orderSetList = [];
													       	$scope.diagnosisList = [];
													       	$scope.selectedOrderSet = [];
													       	$scope.orderSetOrderList = [];
													       	$scope.addbutDis=false;
													       	$scope.getOrderset = function(id,index) 
													    	{

													       		
													       		if($scope.diagnosisIdForOrderSet==0)
													       			{
													       			/*growl.warning("Please select Diagnosis first..!",{
													    				title : 'Warning!'
													    			 });
													       			return;*/
													       			}
													       		
													       		$scope.orderSetIndex = index;
													    		$rootScope.startLoader();
													        	var data={
													        		  ordersetMasterId:[]	,
													        		  orgId : $scope.organizationId,
													        		  unitId : $scope.unitId,
													        		  doctorId : $scope.encounterDetailsObj.doctorId,
													        		  specialityId : $scope.encounterDetailsObj.specialityId,
													        		  diagnosisId:id,
													        		}
													        
													        	
													        	console.log('data',JSON.stringify(data));
													    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET;
													    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
													    				{
													    			   if(response.data.status=='success')
													    			      {
													    				    $scope.orderSetList = response.data.listObject;
													    				    console.log("yes order set ",$scope.orderSetList);
													    				
													    				    if($scope.orderSetList!=null && $scope.orderSetList.length>0)
																			{
																				$scope.counts=0;
																				$scope.countcheck =$scope.orderSetList.length;
																				$scope.selectedlist = $scope.selectedOrderSet;
																				angular.forEach($scope.selectedlist,function(value,index){
																					
																					angular.forEach($scope.orderSetList,function(value1,index1){
																						if(value == value1.ordersetMasterId)
																							{
																							$scope.counts++;
																							}
																						
																					});
																				});
																				
																				if($scope.addSelectedOrderSet == true){
																					if($scope.counts == $scope.countcheck){
																						$scope.addbutDis=true;
																					}else{
																						$scope.addbutDis=false;
																					}
																				}
																				$( '#addOrdersetModal').modal('show');
																			}
													    				    else{
													    				    	growl.warning(
																						"No order set mapped with this diagnosis ..!",
																						{
																							title : 'Warning!'
																						});
													    				    	$rootScope.stopLoader();
																				return;
													    				    }
													    				   	$rootScope.stopLoader();
													    			      }
													    			   else if(response.data.status=='error'){
													    				   $rootScope.stopLoader();
													    				   growl.warning("Something went wrong...",{
															    				title : 'Warning!'
															    			 });
													    			   }
													    				
													    		});
													    	
													    	}
													       	//
													    	$scope.addDuplicateSerives =function()
															{
																$scope.addedServiceOrdersForDisplay = $scope.addedServiceOrdersForDisplay.concat($scope.dublicateSerivceList);
																$scope.totalServiceAmount = 0;
																  angular.forEach($scope.addedServiceOrdersForDisplay,function(value,key){
																	  $scope.totalServiceAmount += value.finalPrice;
																  });
															}
													    	//
													    	$scope.totalOrderSetids = [];
															$scope.fromOrderSetServices = false;
															$scope.addSelectedOrderSet = false;
															$scope.addSelectedOrderSetAdd = function(index)
															{
																if ($scope.selectedOrderSet.length == 0) {
																	growl.warning("Please select Order set..!", {
																		title : 'Warning!'
																	});
																	return;
																}
																else{ 
																	$scope.addSelectedOrderSet = true;
																	 /*$scope.dataListDiagnosis[index].selectedOrderSet = $scope.selectedOrderSet;*/
																    }
															}
															//
													       	$scope.addOrderSetOrders = function(orderSetObjList)
													       	{
													       		$scope.dublicateSerivceList = [];
													       		if($scope.selectedOrderSet.lengh==0)
												       			{
												       			growl.warning("Please select Order set..!",{
												    				title : 'Warning!'
												    			 });
												       			return;
												       			}
												    		$rootScope.startLoader();
												        	var data={
												        		  ordersetMasterId:$scope.selectedOrderSet	,
												        		  orgId : $scope.organizationId,
												        		  unitId : $scope.unitId,
												        		  doctorId : $scope.encounterDetailsObj.doctorId,
												        		  specialityId : $scope.encounterDetailsObj.specialityId,
												        		  diagnosisId:$scope.diagnosisIdForOrderSet
												        		}
												        
												        	
												    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET_ORDERS;
												    		console.log('dataget',JSON.stringify(data));
												    		console.log('URI',URI);
												    		BillingGenericService.serviceAction(METHOD_POST, URI, orderSetObjList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
												    				{
												    			   if(response.data.status=='success')
												    			      {
												    				    $scope.orderSetOrderList = response.data.listObject;
												    				    console.log('$scope.orderSetOrderLis',JSON.stringify($scope.orderSetOrderList));
												    				    angular.forEach($scope.orderSetOrderList, function(value, key) 
												    				    		{
												    				    	
												    				    	  angular.forEach(value.ordersetInvestigations, function(value, key) 
														    				    		{
												    				    		  if($scope.addedServiceOrdersForDisplay!=null)
												    				    			  {
																			        	var newArr = {};
																			        	newArr.serviceId = value.serviceId;
																						newArr.specialityId = value.specialityId;
																						newArr.selectedForFuture = 'N';
																						newArr.specialityName = value.specialityName;
																						newArr.orderSetId = value.ordersetMasterId;
																						newArr.subSpecialityId = value.subSpecilaityId;
																						newArr.serviceStandardName = value.serviceStandardName;
																						newArr.selectedForFuture = $scope.selectedForFuture;
																						
																						newArr.specialityId = value.specialityId;
																						newArr.serviceSpecialityId = value.specialityId;
																						newArr.subSpecialityId = value.subSpecialityId;
																						
																						newArr.serviceMasterId = value.serviceId;
																						newArr.specialityName = value.specialityName;
																						newArr.serviceRenderingDeptId = value.subSpecialityId;
																						newArr.serviceSubSpecialityId = value.subSpecialityId;
																						
																						newArr.isAppointable = value.isAppointable;
																						newArr.createdBy = $scope.userId;
																						newArr.createdDate = new Date().getTime();
																						newArr.selfPayable = "0";
																						newArr.creditPayable = "0";
																						newArr.serviceRendered = "0";
																						newArr.doctorId = $scope.encounterDetailsObj.doctorId;
																						newArr.discountPercentage = 0;
																						newArr.discount = 0;
																						newArr.quantity = 1;
																						newArr.basePrice = 0;
																						newArr.basePriceOld = 0;
																						newArr.totalAmt = 0;
																						newArr.concession = 0;
																						newArr.payeeId = $scope.encounterDetailsObj.payeeId;
																						newArr.apportionedPrice = null;
																						newArr.packageId = null;
																						newArr.packageTypeId = null;
																						newArr.packageEitherOrGroupId = null;
																						newArr.packageCapDetailsId = null;
																						newArr.isCapService = null;
																						newArr.status = 'A';
																						newArr.isRefDoctorShare = value.isRefDoctorShare;
																						newArr.packageEitherOrGroupId = null;
																						newArr.isPackageService = 'N';
																						newArr.discountDetailsList = [];
																						
																						$scope.fromOrderSetServices = true;
																						newArr.finalPrice =0;
																						newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
																						
																						var obj = {
																								"encounterId" :$scope.encounterDetailsObj.encounterId,
																								"contractId" :$scope.contractId,
																								"serviceId" :newArr.serviceMasterId,
																								"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
																								"organizationId":$scope.organizationId,
																								"unitId":$scope.unitId,
																								"visitTypeId":1,
																								"billingBedCategoryId":$scope.opdBillingCatId,
																								"patientCategoryId":$scope.patientDetails.patientCategoryId,
																								"defaultSelfTariffId":$scope.defaultSelfTariffId,
																							    "selfPayeeId" :$scope.selfPayeeId,
																							    "quantity":1,
																							    "patientId":$scope.patientDetails.patientId,
																							    "tariffId" :$scope.defaultSelfTariffId,
																							    "paymentEntitlementId": $scope.encounterDetailsObj.paymentEntitlementTypeId
																							};
																						$scope.totalServiceAmount = 0;
																						isServiceExistsForNotSelected=checkServiceIdExistsForNotSelected($scope.currentEncounterOrderLists,value.serviceId)
																							  if (!isServiceExistsForNotSelected) 
																							  {
																								  isServiceExistsForNotSelected=checkServiceIdExistsForNotSelectedTemeporaryAdd($scope.addedServiceOrdersForDisplay,value.serviceId)
																								  if (!isServiceExistsForNotSelected) 
																								  {
																									  var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
																										BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
																										{
																											if(response.data.object != "undefined" && response.data.object != null)
																											{
																												$scope.finalPriceService = response.data.object.finalPrice;
																												var idx = $scope.addedServiceOrdersForDisplay.findIndex(item => item.serviceMasterId == value.serviceId);
																												$scope.addedServiceOrdersForDisplay[idx].finalPrice = response.data.object.finalPrice;
											                                                                    $scope.totalServiceAmount += $scope.addedServiceOrdersForDisplay[idx].finalPrice;

																											}
																											});
																										
																									  $scope.addedServiceOrdersForDisplay.push(newArr);
																								  }
																								  else{
																									  var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
																										BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
																										{
																											if(response.data.object != "undefined" && response.data.object != null)
																											{
																												$scope.finalPriceService = response.data.object.finalPrice;
																												var idx = $scope.dublicateSerivceList.findIndex(item => item.serviceMasterId == value.serviceId);
																												$scope.dublicateSerivceList[idx].finalPrice = response.data.object.finalPrice;
								// $scope.totalServiceAmount += $scope.dublicateSerivceList[idx].finalPrice;

																											}
																											});
																										
																									  $scope.dublicateSerivceList.push(newArr);
																								  }
																							  }
																							  else
																								  {
																								  var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
																									BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
																									{
																										if(response.data.object != "undefined" && response.data.object != null)
																										{
																											$scope.finalPriceService = response.data.object.finalPrice;
																											var idx = $scope.dublicateSerivceList.findIndex(item => item.serviceMasterId == value.serviceId);
																											$scope.dublicateSerivceList[idx].finalPrice = response.data.object.finalPrice;
								// $scope.totalServiceAmount += $scope.dublicateSerivceList[idx].finalPrice;

																										}
																										});
																								    $scope.dublicateSerivceList.push(newArr);
																								  }
												    				    			         }
														    				    	    });
														    				    
												    				    	  
												    				   
												    				    	  angular.forEach(value.ordersetMedications, function(value, index) 
														    				    		{
																					if ($scope.medicationDetails != null) {
																						$scope.medicationObj.itemId = value.itemMasterId;
																						$scope.medicationObj.doseUnitId = value.doseUnitId;
																						$scope.medicationObj.dose = value.dose;
																						$scope.medicationObj.roaId = value.routeId;
																						$scope.medicationObj.frequencyId = value.frequencyId;
																						$scope.medicationObj.formulationTypeId = value.formulationTypeId;
																						$scope.medicationObj.itemName = value.itemName;
																						$scope.medicationObj.searchItem = value.itemName;
																						$scope.medicationObj.medicationName = value.itemName;
																						$scope.medicationObj.roaDesc = value.roaDesc;
																						$scope.medicationObj.strengthDesc = value.strengthDesc;
																						$scope.medicationObj.frequencyDesc = value.frequencyDesc;
																						$scope.medicationObj.searchItemFre = value.frequencyDesc;
																						$scope.medicationObj.priorityId = 1;
																						$scope.medicationObj.genericName = value.genericName;
																						$scope.medicationObj.genericId = value.genericId;
																						$scope.medicationObj.instructionId = value.instructionId;
																						$scope.medicationObj.noOfAdministrationTime = value.noOfAdministrationTime;
																						$scope.medicationObj.rate = value.rate;
																						$scope.medicationObj.totalRate= parseInt($scope.medicationObj.rate * (($scope.medicationObj.quantity!=undefined && $scope.medicationObj.quantity!="") ? $scope.medicationObj.quantity :0));
																						console.log("$scope.medicationInstructions=" + $scope.medicationInstructions)
																						
																						if ($scope.medicationObj.instructionId != null) {
																							var instructionDesc = $.grep( $scope.medicationInstructions, function(
																													disCat) {
																												return disCat.instructionId == $scope.medicationObj.instructionId;
																											})[0].instructionDesc;

																							$scope.medicationObj.instructionDesc = instructionDesc;
																						} else {
																							$scope.medicationObj.instructionDesc = "No instruction";
																						}
																						$scope.medicationObj.isGeneric = 'N';
																						$scope.medicationObj.isInstruction='N' ;
																						$scope.medicationObj.nonFormularyAdvice = 'N';
																						var checkFlagValidation = true;
																					    $scope.medicationObj.checkFlagValidation = checkFlagValidation;
																					    $scope.medicationObj.medicationStartDate = moment(new Date()).format('YYYY-MM-DD');
																					    
																					    $scope.medicationObj.durationUnit = 'Day';
																						var found = $scope.medicationDetails.find(ar=>ar.itemId==value.itemMasterId);
																						   if(found==undefined)
																							   {
																							      
																							      $scope.medicationDetails.push($scope.medicationObj);
																							      $scope.totalSumOfRate = 0;
																									angular.forEach($scope.medicationDetails , function(value1,key1){
																										$scope.totalSumOfRate += parseInt(value1.totalRate);

																								   	});
																							      $scope.updateFlagValidation = true;
																							   }
																						   
																						$scope.initMedicationObj();

																					}

																				});
												    				    	  
												    				  		
												    				    	  angular.forEach($scope.medicationDetails,function(value,idx){
												    				    		// get formuation dosage list
												    				    	$scope.prescribedDosageUnitListorderswet=[];
												    				  			var dataobj = {
																						organizationId : $scope.organizationId,
																						unitId : $scope.unitId,
																						formulationTypeId: value.formulationTypeId
																				};
																				value.prescribeDose="";
																				var URIForPresc = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
																				GenericService.serviceAction("POST", URIForPresc, dataobj).then(function(response) {
																							$rootScope.stopLoader();
																							$scope.counterFormulation=1;
																							$scope.medicationDetails[idx].formulationCounterIndex="";
																							$scope.prescribedDosageUnitListorderswet=[];
																							angular.forEach(response.data.listObject,function(value,index){
																								var unitObj={};
																								unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																								unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																								unitObj.formulationDosageToUnit=value.toConversionUnit;
																								unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																								unitObj.counterIndex=$scope.counterFormulation++;
																								unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																								unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																								unitObj.formulationFactorId=value.factorId;
																								unitObj.formulationDosage=value.dosageForm;
																								unitObj.formulationDosageMappId=value.formulationDosageMappId;
																								$scope.prescribedDosageUnitListorderswet.push(unitObj);
																							});
																							
																							angular.forEach(response.data.listObject,function(value,index){
																								var unitObj={};
																								if(value.factorId==1){
																									unitObj.formulationDosageFromUnit=value.toConversionUnit;
																									unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																									unitObj.formulationDosageToUnit=value.fromConversionUnit;
																									unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																									unitObj.counterIndex=$scope.counterFormulation++;
																									unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																									unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																									unitObj.formulationFactorId=value.factorId;
																									unitObj.formulationDosage=value.dosageForm;
																									unitObj.formulationDosageMappId=value.formulationDosageMappId;
																									$scope.prescribedDosageUnitListorderswet.push(unitObj);
																								}
																							});
																							value.prescribedDosageUnitList=$scope.prescribedDosageUnitListorderswet;
																							var list=[];
																							if(value.prescribedDosageUnitList.length>0 && value.prescribedDosageUnitList[0].formulationFactorId==1){
																								 list=$.grep(value.prescribedDosageUnitList, function (obj) {
																							        return value.doseUnitId === obj.formulationDosageFromUnitId;
																							    });
																								if(list.length>0){	
																									value.formulationCounterIndex=list[0].counterIndex;
																								}
																							}
																				});
												    				    		  
												    				    	  })
												    				    	  
												    				    	    });
												    					 $timeout(function () {

																				if($scope.dublicateSerivceList.length>0)
																				{
																				   angular.element('#dublicateServiceAddOPD').modal('show');
																				}

																			 }, 3000);
												    				    $('#addOrdersetModal').modal('hide');
												    				   /*
																		 * growl.success("Orders Added
																		 * Successfully.",{ title : SUCCESS_MSG
																		 * });
																		 */
												    				   	$rootScope.stopLoader();
												    			      }
												    			   else if(response.data.status=='error'){
												    				   $rootScope.stopLoader();
												    				   growl.warning("Something went wrong...",{
														    				title : 'Warning!'
														    			 });
												    			   }
												    				
												    		});
													       	}
							/************************************Orderset end******************************************************************************/
														
														$scope.saveDiagnosisFreeText = function(){
															try{
																$rootScope.startLoader();
																var date = new Date();
																$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
																
																var data={	
																		diagnosisText:$scope.diagnosisName,
																		diagnosisType:1,
																		patientId: $scope.encounterDetailsObj.patientId,
																		encounterId: $scope.encounterDetailsObj.encounterId,																		organizationId:$scope.organizationId,
																		unitId:$scope.unitId,
																		created_by:$scope.userId,
																		updated_by:$scope.userId,
																		status:'A',
																		created_date:$scope.dt,
																		updated_date:$scope.dt
																	
																	}
																var URI = BASE_URL + ROOT_URL + "/api/opd/saveFreeTexPatientDiagnosis";
																GenericService.serviceAction("POST",URI,data).then(function(response){
																	
																	if (response.data.status == 'success')
																	{
																		growl.success("Added Successfully.",
																				{
																					title : 'Success!'
																				});
																		$scope.diagnosisName ='';
																		$scope.getAllPatientDiagnosis();
																		$scope.getAllPatientDiagnosisForIcon();
																		$scope.getListOfCancelledDiagnosis();
																		$rootScope.stopLoader();
																		
																	} else if(response.data.status == 'error')
																	{
																		growl.error(response.data.message,
																				{
																					title : 'Error!'
																				});
																		$rootScope.stopLoader();
																	}
																});
																$rootScope.stopLoader();
															
																
															}catch(e){
																console.log("Exception-",e);
															}
														}
				        				 
					 }
					]);

