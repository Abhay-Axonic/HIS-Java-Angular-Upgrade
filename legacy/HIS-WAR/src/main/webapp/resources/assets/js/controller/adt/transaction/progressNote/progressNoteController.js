'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:progressNoteController
 * @description #progressNoteController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'progressNoteController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader) {


							/* init() function for form object create. */
							$scope.init = function() {					
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

							$scope.init();
							
							
							//Function For Get Doctor List By Speciality
							$scope.getDoctorBySpeciality = function(id) {
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									specialityId : id
								};
								
								//Condition For All Doctor Or By Speciality ID
								
										var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.doctorList = response.data.listObject;
												});
									}
							
							
							
							$scope.nursingStationChange = function()
							{
								$scope.encounterSearchObject.doctorId = '0';
								$scope.encounterSearchObject.clinicId = '0';
							}
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.organizationId = $scope.organizationId;
								$scope.encounterSearchObject.unitId = $scope.unitId;

								
								console.log($scope.encounterSearchObject);
								
								var paramObj = {};
								var headers={
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETSEARCHEDLISTENCOUNTERMASTER;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success")
												
												$scope.allEncounterList =response.data.listObject;
										});
						
							}
							//$scope.searchEncounter();
						
							$scope.clearSearchObject = function()
							{
								$scope.encounterSearchObject = {
							    		 'doctorId':0,
							    		 'specialityId':0,
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'patientId':0,
							    		 'specialityId':0,
							    		 'statusId':0,
							    		 'nursingStationId':0,
							    		 'clinicId':0
							     }
								
								$scope.clinicList = [];
								
								$scope.patientName ='';
								$scope.uhidNumber = '';
								$scope.mobileNumber = '';
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}
							
							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}
							
							$scope.selectPatientId = function(info, model,
									label) {
								
								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;
								
								$scope.admissionSearchObject.patientId = info.patientId;
							}
						
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							$scope.getAppointmentDetails = function(item)
							{
								$scope.appointmentDate = item.appointmentDate;
								$scope.appointmentFromTimeString = item.fromTimeString;
								$scope.appointmentRemark = item.remark;
							}
							
							$scope.getRefralDetails = function(item)
							{
								$scope.referralTypeName = item.referralTypeName;
								$scope.referralName = item.referralName;
								$scope.referralRemark = item.referralRemark;
							}
							
							$scope.gotoCoverSheet = function(encounterObj)
							{
								//alert();
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '2' ){
								if(encounterObj.consultationStartTime == null || encounterObj.consultationStartTime == '')
								{
									//alert("CONS START TIME NULL");
									var headerObj={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
										var data = {
											'assignmentStartTime':encounterObj.assignmentStartTimeDate,
											'assignmentStopTime':encounterObj.assignmentStopTimeDate,
											'isAssignmentCompStatus':encounterObj.isAssignmentCompStatus,
											'assignmentStartBy':encounterObj.assignmentStartBy,
											'assignmentStopBy': encounterObj.assignmentStopBy,
											'assignmentStartRoleId':encounterObj.assignmentStartRoleId,
											'assignmentStopRoleId': encounterObj.assignmentStopRoleId,
											'isConsultationStatus': "N",
											'consultationStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											'consultationStopTime': encounterObj.consultationStopTime,
											'consultationStartBy': $scope.userId,
											'consultationStopBy': "",
											'consultationStartRoleId':$scope.roleId,
											'consultationStopRoleId': "",
											'encounterId': encounterObj.encounterId,
											'patientId': encounterObj.patientId
										}
										
										
										console.log('Consultant DATA',data);
										//return false;
										var paramObjForPer={};
										var URI = BASE_URL + ROOT_URL
										+ ENCOUNTERDETAILS;
										GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
											.then(
													function(response) {
														console.log(response);
														console.log(encounterObj.encounterId);
														
														// $rootScope.startLoader();
														if (data.status == "success")
															console.log("in success");
															var cookieObject = 
															{
																	"patientName" :encounterObj.patientName,
																	"uhidNumber" : encounterObj.uhidNumber,
																	"genderCode" : encounterObj.genderCode,
																	"age":  encounterObj.age,
																	"dob": encounterObj.dob,
																	"encounterId":encounterObj.encounterId,
																	"genderId":encounterObj.genderId,
																	"roleId" :$scope.roleId,
																	"patientId":encounterObj.patientId,
																	"doctorId":encounterObj.doctorId,
																	"specialityId":encounterObj.specialityId,
																	"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																	"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1
																	
															}
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$state.go("OPDCoverSheet");
													});
										
								}
								else
								{
									
									//alert("CON ADDED");
									//return false;
									var cookieObject = 
									{
											"patientName" :encounterObj.patientName,
											"uhidNumber" : encounterObj.uhidNumber,
											"genderCode" : encounterObj.genderCode,
											"age":  encounterObj.age,
											"dob": encounterObj.dob,
											"encounterId":encounterObj.encounterId,
											"genderId":encounterObj.genderId,
											"roleId" :$scope.roleId,
											"patientId":encounterObj.patientId,
											"doctorId":encounterObj.doctorId,
											"specialityId":encounterObj.specialityId,
											"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
											"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							$scope.gotoCoverSheetAssigment = function(encounterObj)
							{
								//alert();
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '3'){
								
								if(encounterObj.assignmentStartTime == null || encounterObj.assignmentStartTime == '')
								{
									//alert('ASSIGMENT START NULL');
									var headerObj={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
										var data = {
											'assignmentStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											'assignmentStopTime':null,
											'isAssignmentCompStatus':'N',
											'assignmentStartBy':$scope.userId,
											'assignmentStopBy': "",
											'assignmentStartRoleId':$scope.roleId,
											'assignmentStopRoleId':"",
											'isConsultationStatus': "N",
											'consultationStartTime':encounterObj.consultationStartTimeDate,
											'consultationStopTime': null,
											'consultationStartBy': encounterObj.consultationStartBy,
											'consultationStopBy': "",
											'consultationStartRoleId':encounterObj.consultationStartRoleId,
											'consultationStopRoleId': "",
											'encounterId': encounterObj.encounterId,
											'patientId': encounterObj.patientId
										}
										
										console.log('ASSIGMENT DATA',data);
										//return false;
										
										var paramObjForPer={};
										var URI = BASE_URL + ROOT_URL
										+ ENCOUNTERDETAILS;
										GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
											.then(
													function(response) {
														console.log(response);
														console.log(encounterObj.encounterId);
														
														// $rootScope.startLoader();
														if (data.status == "success")
															console.log("in success");
															var cookieObject = 
															{
																	"patientName" :encounterObj.patientName,
																	"uhidNumber" : encounterObj.uhidNumber,
																	"genderCode" : encounterObj.genderCode,
																	"age":  encounterObj.age,
																	"dob": encounterObj.dob,
																	"encounterId":encounterObj.encounterId,
																	"genderId":encounterObj.genderId,
																	"roleId" :$scope.roleId,
																	"patientId":encounterObj.patientId,
																	"doctorId":encounterObj.doctorId,
																	"specialityId":encounterObj.specialityId,
																	"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																	"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1
																	
															}
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$state.go("OPDCoverSheet");
													});
										
								}
								else
								{
									//alert('ASSIGMENT START MILA');
									//return false;
									var cookieObject = 
									{
											"patientName" :encounterObj.patientName,
											"uhidNumber" : encounterObj.uhidNumber,
											"genderCode" : encounterObj.genderCode,
											"age":  encounterObj.age,
											"dob": encounterObj.dob,
											"encounterId":encounterObj.encounterId,
											"genderId":encounterObj.genderId,
											"roleId" :$scope.roleId,
											"patientId":encounterObj.patientId,
											"doctorId":encounterObj.doctorId,
											"specialityId":encounterObj.specialityId,
											"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
											"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							
							$scope.generateIpdCoverSheetUrl = function(patientDetailsObj)
							{
								if(patientDetailsObj!=null)
								  {
									patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									console.log("patientDetails",JSON.stringify(patientDetailsObj));
												$state.go('IPDCoverSheet',
													{
													patientDetailsObj :patientDetailsObj
													});
								  }
							  else
							     {
								    growl.error("Something went Wrong.",{title : 'Error!'});
							     }
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
//	 							$scope.previousCaseSheetDate=val.encounterDateString;
	 							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
	 							
	 							

	 							}else{
	 								$rootScope.stopLoader();
	 							}
//	 							$rootScope.stopLoader();
	 							});
	 										
//	 										flag=true;
	 									}else{
	 										$rootScope.stopLoader();
	 									}
//	 									
	 								
	 								});
	 								
	 								
	 							}
						} ]);