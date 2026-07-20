/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'bookViewMasterController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$window',
						'$timeout',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'growl',
						'$compile',
						'reportService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state,$window,$timeout,$interval,
								$stateParams,GenericServiceParamHeader,growl,$compile,reportService) {
							
							var cookieObject = $cookies
							.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.unitName = cookieObject.unitNme;
							$(".modal-backdrop").hide();
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							if(cookieObjectAppConfig == undefined){
								$state.go('applicationConfiguration');
								return;
							}
							else
								{
									console.log('cookieObjectAppConfig',cookieObjectAppConfig);
									
								}
							$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
							$scope.allergyButtonFlag = true;
							$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
							$scope.radiologySpecialityId= cookieObjectAppConfig.radiologySpecialityId;
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
							$scope.isAcceptanceInDepartment = labConfig.isAcceptanceInDepartment;
						    $scope.subDeptId = labConfig.microbiologyId;
						    $scope.printLabReportBeforRelease = labConfig.printLabReportBeforRelease;
							$scope.patientDetails = 
							{
								uhIdNumber:"",
								patientName:"",
								genderCode:"",
								age:"",
								birthDate:"",
								encounterId:""
							};
						
							 $scope.setFirstTabLevel(50);
					
							// Date For From Date on Search
							$scope.dateOptionsFrom = {
								
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date(), //max 
								};
							$scope.dateOptionsTo = {
									
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date(), //max 
								};

							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
									opened : false
							};
							
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
								
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							
							/*$scope.setSecondTabLevel= function(info){
								
							}*/
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
								
							};

							$scope.toDatepicker = {
									opened : false
							};
							
						/*	var pastSevenDay = new Date();
							$scope.fromDatepicker= pastSevenDay.setDate(pastSevenDay.getDate()- 7);*/
							$scope.fromDatepicker=new Date();
							$scope.toDatepicker=new Date();
							
						       
							//for admission - DOA date picker & PDD date picker
							
							$scope.checkAppointment=function(){


								$scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject')) || {};

								console.log("Encounter Object",$scope.encounterDetailsObj);

								// Only trust sessionStorage.cookieEncounterObject if it actually
								// matches the patient/encounter this transition was navigated
								// for (docs/DECISION_NEEDED_BookViewStalePatientContext.md,
								// docs/ANGULAR_PENDING_FINDINGS.md entry 28) -- that key is
								// written by 14 other, unrelated screens for their own
								// purposes and can be stale. redirectBookViewUrl() already
								// passes encounterId/patientId in $stateParams.bookViewObj;
								// this is the first code that reads them for anything beyond
								// an empty-object check.
								var hasStateContext = !angular.equals({}, $stateParams.bookViewObj);
								var contextMatches = hasStateContext
									&& $scope.encounterDetailsObj
									&& Object.keys($scope.encounterDetailsObj).length > 0
									&& String($scope.encounterDetailsObj.encounterId) === String($stateParams.bookViewObj.encounterId)
									&& String($scope.encounterDetailsObj.patientId) === String($stateParams.bookViewObj.patientId);

								if(contextMatches) {
										$scope.patientDetails.patientName = $scope.encounterDetailsObj.patientName;
										$scope.patientDetails.uhIdNumber = $scope.encounterDetailsObj.uhIdNumber;
										$scope.patientDetails.genderCode = $scope.encounterDetailsObj.genderCode;
										$scope.patientDetails.age = $scope.encounterDetailsObj.age;
										$scope.patientDetails.birthDate = $scope.encounterDetailsObj.birthDate;
										$scope.patientDetails.patientId = $scope.encounterDetailsObj.patientId;
										$scope.patientDetails.encounterId = $scope.encounterDetailsObj.encounterId;
										$scope.patientDetails.admissionId = $scope.encounterDetailsObj.admissionId;
										$scope.patientDetails.genderId = $scope.encounterDetailsObj.genderId
										if($scope.encounterDetailsObj.visitTypeId==null){
											$scope.patientDetails.visitTypeId =1;

										}else{
											$scope.patientDetails.visitTypeId = $scope.encounterDetailsObj.visitTypeId;

										}
										$scope.roleId = $scope.encounterDetailsObj.roleId;
										$scope.patientDetails.patientCategoryId =$scope.encounterDetailsObj.patientCategoryId;
										$scope.isCritical = $scope.encounterDetailsObj.isCritical;


								}
								else
									{
										$state.go(hasStateContext ? 'bedManagement' : 'encounterQueueManagement');
										return false;
									}

							}


							$scope.checkAppointment();


							/**Redirect Page Code*/
							if(angular.equals({},$stateParams.bookViewObj))
 							{
								$state.go('bedManagement');
							} else {
								$scope.bookViewObj = $stateParams.bookViewObj;
							}
							
							$scope.patientSearchObj = {
									"uhIdNumber"      : "",
									"patientName"     : "",
									"genderId"        : "",
									"mobileNo"        : "",
									"birthDate"       : "",
									"identificationNo":"",
									"patientType"	  :"1",
									"unitId"          :$scope.unitId,
									"organizationId"  :$scope.organizationId
							}
						  
							$scope.getPatientDetailsByIndex = function(index){
								$rootScope.startLoader();
								$scope.patientDetails.age=($scope.patientList[index].age);
								$scope.patientDetails.patientName=($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
								$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.patientId=($scope.patientList[index].patientId);
								$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
								$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
								$scope.patientDetails.patientTypeId = ($scope.patientList[index].patientCategoryId);
								$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
								var data = {};
								var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientList[index].patientId;
								BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										console.log("Encouner List",response.data.listObject);
										$scope.patientEncounterList = response.data.listObject;
										if($scope.patientEncounterList.length == 0){
											growl.warning("No Result Found!",{
												title : ERROR_MSG
											});
										}
									}
								});
							
									$(".patienttSearchResult").toggle();
								
									$rootScope.stopLoader();
							}
							
							  $scope.clearSearchFilter = function(){
		                        	 $scope.searchKeyword="";
		                         }
							
							$scope.masterObj = {};
							$scope.patientId=null;
							$scope.getPatientDetailsById = function(patientId){
								$scope.patientId=patientId;
								$scope.patientDetailsList = [];
								$scope.patientDetails = {};
								$scope.masterObj = {};
								$scope.patientEncounterList=[];
								var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.patientDetailsList = response.data.listObject;
										$scope.patientDetails = response.data.listObject[0];
										console.log("PatientDetailList:",$scope.patientDetailsList);
										console.log("PatientDetail:",$scope.patientDetails);
									}
								});
								
							
							
							}
							
					/*		var URI = BASE_URL + ROOT_URL + "/adt/getListOfEncounterIdByPatientId/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId;
							BillingGenericService.serviceAction(METHOD_GET,URI).then(function(response) {
								if (response.data.status == 'success'){
									$scope.patientEncounterList = response.data.listObject;
									$scope.encounterId=response.data.listObject[0].encounterId;
									$scope.getActiveComplaintlist();
									$scope.getSurgicalHistory();
									$scope.getCurrentPatientpersonalHistory();
									$scope.getMedicalHistory();
									$scope.getActiveImmunization();
									$scope.getFamilyHistory();
									$scope.getActiveHabitDetails();
									$scope.getAllPatientDiagnosis();
									$scope.getDetailedMedicationHistoryList();
									$scope.getTravelHistoryList();
									$scope.getOrderProcedureList();
									console.log("tttttttt",$scope.patientEncounterList );
								}
							});
							*/
							
							 $scope.get=function(index){
									var data={
												patientId:$scope.patientDetails.patientId,
												unitId:$scope.unitId,
												organizationId:$scope.organizationId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
												visitTypeId:$scope.patientDetails.visitTypeId
												
									};
									
									 $scope.getiteratedlist();
									 $scope.getPatientDocumentList();
									 $scope.getPastVisitData();
									 $scope.getInstructions();
									 $scope.consultOrderList();
									 $scope.getListPackageOrder();
									 $scope.getOPDAdmissionDetails();
									 $scope.getObservationList();
									 $scope.getPastComplaintlist();
									 $scope.getActiveComplaintlist();
									 $scope.getMedicationListForMedicalHistory();
							 }
//							 $scope.get();
							 $scope.patientEncounterList=[];
							 $scope.complaintsPatientListDoctor = [];
							 $scope.personalHistoryCurrentListDoctor = [];
							 $scope.familyHistoryCurrentListDoctor = [];
							 
							 $scope.months = [];
								$scope.months.push({
									value : 1,
									text : 'January'
								});
								$scope.months.push({
									value : 2,
									text : 'February'
								});
								$scope.months.push({
									value : 3,
									text : 'March'
								});
								$scope.months.push({
									value : 4,
									text : 'April'
								});
								$scope.months.push({
									value : 5,
									text : 'May'
								});
								$scope.months.push({
									value : 6,
									text : 'June'
								});
								$scope.months.push({
									value : 7,
									text : 'July'
								});
								$scope.months.push({
									value : 8,
									text : 'August'
								});
								$scope.months.push({
									value : 9,
									text : 'September'
								});
								$scope.months.push({
									value : 10,
									text : 'October'
								});
								$scope.months.push({
									value : 11,
									text : 'November'
								});
								$scope.months.push({
									value : 12,
									text : 'December'
								});
							 
						// $timeout( function(){
							 $scope.getiteratedlist=function(){
//							 angular.forEach($scope.patientEncounterList, function(value,key) {	
							
										
							 // For Get Active Complaints List
						     $scope.getActiveComplaintlist=function(){
						    	 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined)
									{
									growl.warning("Please select Patient..",{
										title : ERROR_MSG
										
									});
									return false;
									}
						    	 $scope.complaintsPatientList=[];
								     var data={
								    		 	patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
												organizationId : $scope.organizationId
								     };
							var URI = BASE_URL + ROOT_URL
								+ "/api/opd/getComplaintsByPatientIdForBookView";
							GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.complaintsPatientList = response.data.listObject;
											console.log("complaintsPatientList", $scope.complaintsPatientList);
										});
						     }
						     $scope.getActiveComplaintlist();
						     
						     // For Get Past Complaints List
						     $scope.getPastComplaintlist=function(){
						    	 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
										return;
								}
						    	 $scope.pastComplaintsPatientList=[];
								     var data={
								    		 	patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
												organizationId : $scope.organizationId
								     };
							var URI = BASE_URL + ROOT_URL
								+ "/api/opd/getPastComplaintAppointmentForBookView";
							GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.pastComplaintsPatientList = response.data.listObject;
											console.log("complaintsPatientList", $scope.pastComplaintsPatientList);
										});
						     }
						     $scope.getPastComplaintlist();
						     
								// START PERSONAL HISTORY
								
								
						     
						     //for get active history list
						  $scope.getCurrentPatientpersonalHistory=function(){
							  if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
									return;
							}
								$scope.dt = moment(new Date()).format('DD-MM-YYYY');
								$rootScope.getTemplateCountByAdmissionOrEncounter($scope.patientDetails.patientId,null,null);
							  var headerObj = {
										organizationId : $scope.organizationId
									};

									var paramObjForHistList = {
										patientId : $scope.patientDetails.patientId,
										fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
										toDate:moment($scope.toDatepicker).format('YYYY-MM-DD')
									};
									
									var data={};
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getPatientPersonalHistoryForBookView";
								GenericServiceParamHeader
									.serviceAction(METHOD_GET, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
									.then(
											function(response) {
												console.log(response);
												
												// $rootScope.startLoader();
												if (response.data.status == "success")
												$scope.personalHistoryCurrentList = response.data.listObject;
												
												
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
		                        	  if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
		                        	  $scope.dt = moment(new Date()).format('DD-MM-YYYY');
		                        	  var headerObj = {
		                        			    organization_id : $scope.organizationId,
												unit_id : $scope.unitId,
											};

											var paramObjForHistList = {
													patientId : $scope.patientDetails.patientId,
													created_date:$scope.dt
												};
											
											var data={};
										 $rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getResolvedPatientPersonalHistoryDetailsForCoversheet";
										GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
											.then(
													function(response) {
														console.log(response);
														$scope.resolvedPersonalHistoryList=[];
														if (response.data.status == "success"){
															
															$scope.fromDate = moment($scope.fromDatepicker).format('DD/MM/YYYY');
														    $scope.toDate = moment($scope.toDatepicker).format('DD/MM/YYYY');
															angular.forEach(response.data.listObject,function(value,key){
																if($scope.fromDate <= value.createdDate && value.createdDate <=$scope.toDate){
																	$scope.resolvedPersonalHistoryList.push(value);
																}
															});
															
															
															$rootScope.stopLoader();
														}else{
															$rootScope.stopLoader();
														}
															
													});
				                          }
		                          
		                          $scope.getResolvePersonalHistoryData();
		                       // START FAMILY HISTORY
									
								
									
									$scope.getFamilyHistory=function(){
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										$scope.dt = moment(new Date()).format('DD-MM-YYYY');

										var headerObj = {
												organizationId : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD')
											};
										 var data={};
										
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getPatientFamilyHistoryForBookView";
								GenericServiceParamHeader
									.serviceAction(METHOD_GET, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
									.then(
											function(response) {
												console.log(response);
												
												// $rootScope.startLoader();
												if (response.data.status == "success")
												$scope.familyHistoryCurrentList = response.data.listObject;
												
												
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
									
									//to get surgical history
									$scope.getSurgicalHistory=function(){
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										$scope.dt = moment(new Date()).format('DD-MM-YYYY');

										var headerObj = {
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD')
											};
										var data={};
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getPatientSurgicalHistoryForBookView";

								GenericServiceParamHeader
									.serviceAction(METHOD_GET, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
									.then(
											function(response) {
												console.log(response);
												
												// $rootScope.startLoader();
												if (response.data.status == "success")
												$scope.surgicalHistoryCurrentList = response.data.listObject;
												
												$scope.surgicalHistoryCurrentListDoctor = [];
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
																data.surgeryId = value.surgeryId,
																data.month= value.month,
																data.year = value.year,
																data.surgicalHistoryDetailsId  = value.surgicalHistoryDetailsId;
																$scope.surgicalHistoryCurrentListDoctor.push(data);
														}
														});
													}
												
											});
									}
									$scope.getSurgicalHistory();
									
									
									//get medical history
									$scope.getMedicalHistory=function(){
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										$scope.dt = moment(new Date()).format('DD-MM-YYYY');

										var headerObj = {
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD')
											};
										
										 var data={};
										 
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getPatientMedicalHistoryForBookView";

								GenericServiceParamHeader
									.serviceAction(METHOD_GET, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
									.then(
											function(response) {
												console.log(response);
												
												// $rootScope.startLoader();
												if (response.data.status == "success")
												$scope.medicalHistoryCurrentList = response.data.listObject;
												
												$scope.medicalHistoryCurrentListDoctor = [];
												if($scope.roleId == '2' )
													{
														
														angular.forEach(response.data.listObject, function(value,key) {
															
																if(value.roleId == 3 && value.isReviewedFlag == 'N')
																	{
																var data = {};
																data.isNoSignificantStatus = value.isNoSignificantStatus;
																data.remark = value.remark;
																data.isReviewedFlag = 'N',
																data.isEnterInError='N',
																data.diagnosisId = value.diagnosisId,
																data.since= value.since,
																data.duration = value.duration,
																data.patientMedicalHistoryId  = value.patientMedicalHistoryId;
																$scope.medicalHistoryCurrentListDoctor.push(data);
														}
														});
													}
												
											});
									}
									$scope.getMedicalHistory();
									
									
									$scope.getPatientDiagnosisForMedicalHistory = function() {
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										var data = {};
										$scope.DateFor = moment(new Date(),"DD-MM-YYYY").subtract(1, 'days').toDate()
										
										var headerObjDia = {
											'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : $scope.patientDetails.patientId,
											'createdDate' :moment(new Date()).format('YYYY-MM-DD'),
											'Content-Type' : 'application/json',
										};
										$scope.patientDiagnosisListForMedicalHistory =[];
										var URI = BASE_URL + ROOT_URL
												+ "/api/opd/getPatientDiagnosisByPatientIdForMedicalHistory";
										GenericServiceParamHeader .serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE,
														data, headerObjDia)
												.then( function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																
																$scope.fromDate = moment($scope.fromDatepicker).format('DD/MM/YYYY');
															    $scope.toDate = moment($scope.toDatepicker).format('DD/MM/YYYY');
																angular.forEach(response.data.listObject,function(value,key){
																	if($scope.fromDate <= value.created_date && value.created_date <=$scope.toDate){
																		$scope.patientDiagnosisListForMedicalHistory.push(value);
																	}
																});
															console.log( "patientDiagnosisListForMedicalHistory", $scope.patientDiagnosisListForMedicalHistory);

														});
									}
									$scope.getPatientDiagnosisForMedicalHistory();
									//to get Immunization
									
									$scope.getTreatmentHistory=function(){
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										$scope.dt = moment(new Date()).format('DD-MM-YYYY');

										var headerObj = {
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD')
											};
										 var data={};
										var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getPatientTreatmentHistoryForBookView";
									GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
										.then(
												function(response) {
													console.log(response);
													$scope.treatmentHistoryCurrentList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
													$scope.treatmentHistoryCurrentList = response.data.listObject;
													console.log("treatment",$scope.treatmentHistoryCurrentList);
													
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
	                              $scope.getActiveImmunization=function(){
	                            	  if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										
										var headerObj = {
												unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											};

										
										var paramObjForPer = {
												patientId:$scope.patientDetails.patientId,
//												encounterId : value.encounterId,
									    		roleId : 1
										};
										
										 var data={};
										 
					        			var URI = BASE_URL + ROOT_URL
					        			+ GETCURRENTIMMUNIZATIONHISTORYMAPPER;
					        		GenericServiceParamHeader
					        			.serviceAction(METHOD_GET, URI,
					        					data,
					        					NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
					        			.then(
					        					function(response) {
					        						console.log(response);
					        						
					        						// $rootScope.startLoader();
					        						if (response.data.status == "success"){
					        						$scope.immunizationHistoryCurrentList = response.data.listObject;
					        						console.log("immunizationHistoryCurrentList",$scope.immunizationHistoryCurrentList);
					        						}
					        						
					        					});
									}
									$scope.getActiveImmunization();
									
									
									// For Get ALL patient Habits List
									$scope.getActiveHabitDetails=function(){
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										 var headerObj = {
													organization_id : $scope.organizationId
												};
												var paramObjForPer = {
												patientId:$scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD')
												};
												var data = {};

									var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getPatientHabitsForBookView";
									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE,
													paramObjForPer, headerObj)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.patientHabitList = response.data.listObject;

														// START TEST
														$scope.habitFinalList = [];
														console.log("HabitList",$scope.habitTypeList);
														console.log("PatientHabitList",response.data.listObject);
														angular.forEach($scope.habitTypeList,function(value,key) {
														var checkFlag = false;
														angular.forEach($scope.patientHabitList,function(value1,key1) {

																								if (value.habitId == value1.habitId) {
																									checkFlag = true;
																									$scope.childObj = [];
																									$scope.newArr = {};
																									$scope.newArr.habitId = value1.habitId;
																									$scope.newArr.habitTypeId = value1.habitTypeId;
																									$scope.newArr.remark = value1.remark;
																									$scope.newArr.cigarettesPerDay = value1.cigarettesPerDay;
																									$scope.newArr.yearsSmoked = value1.yearsSmoked;
																									$scope.newArr.leftWhen = value1.leftWhen;
																									$scope.newArr.duration = value1.durationOfLeftWhen;
																									$scope.newArr.packYear = value1.packYear;
																									$scope.newArr.gmsPerDay = value1.gmsPerDay;
																									$scope.newArr.yearsUsed = value1.yearsUsed;
																									$scope.newArr.mlsPerDay = value1.mlsPerDay;
																									$scope.newArr.frequency = value1.frequency;
																									$scope.newArr.typeOfExercise = value1.typeOfExercise;
																									$scope.newArr.mlsPerDay = value1.mlsPerDay;
																									$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
																									$scope.newArr.isEnterInErrorStatus = value1.isEnterInErrorStatus;
																									if ($scope.roleId == 2) {
																										$scope.newArr.patientHabitDetailsId = value1.patientHabitDetailsId;
																									}
																									$scope.newArr.drug = value1.drug;
																									if (value1.habitId == 5) {

																										var res = value1.typeOfExercise
																												.split(",");
																										// alert(res.length);
																										$scope.patient.exercise = [];
																										var tdata = {};
																										for (var j = 0; j < res.length; j++) {
																											if (res[j] != '') {
																												$scope.patient.exercise
																														.push(res[j]);
																											}
																										}
																										// $scope.patient.exercise.push(tdata);
																										$scope.newArr.exerciseList = [
																												"Yoga",
																												"WeightTraining",
																												"Cardio",
																												"Sports", ];
																									}
																									$scope.childObj
																											.push($scope.newArr);
																								}

																							});
																			if (checkFlag == true) {
																				var data = {
																					"habitId" : value.habitId,
																					"habitName" : value.habitName,
																					"childHabitObj" : $scope.childObj
																				}
																				$scope.habitFinalList
																						.push(data);
																			} else {

																				$scope.childObj = [];
																				var newArr = {};
																				newArr.habitId = value.habitId;
																				newArr.habitTypeId = '';
																				newArr.remark = '';
																				newArr.cigarettesPerDay = '';
																				newArr.yearsSmoked = '';
																				newArr.leftWhen = '';
																				newArr.duration = '';
																				newArr.packYear = '';
																				newArr.gmsPerDay = '';
																				newArr.yearsUsed = '';
																				newArr.mlsPerDay = '';
																				newArr.frequency = '';
																				newArr.typeOfExercise = '';
																				newArr.mlsPerDay = '';
																				newArr.drug = '';
																				newArr.isReviewedFlag = 'N';
																				newArr.isEnterInErrorStatus = 'N';
																				if ($scope.roleId == 2) {
																					newArr.patientHabitDetailsId = '';
																				}

																				if (value.habitId == 5) {
																					newArr.exerciseList = [
																							"Yoga",
																							"WeightTraining",
																							"Cardio",
																							"Sports", ];

																				}

																				$scope.childObj
																						.push(newArr);
																				var data = {
																					"habitId" : value.habitId,
																					"habitName" : value.habitName,
																					"childHabitObj" : $scope.childObj
																				}
																				$scope.habitFinalList
																						.push(data);

																			}

																		});

														$rootScope.stopLoader();
														// END TEST
													});
									}
									$scope.getActiveHabitDetails();
									
									 // For Get ALL Patient Diagnosis List
									$scope.getAllPatientDiagnosis=function(){
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
									 var headerObjDia = {
											 	'unit_id' : $scope.unitId,
												'organization_id' : $scope.organizationId,
												'patient_id' : $scope.patientDetails.patientId,
												 'fromDate':moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												 'toDate':moment($scope.toDatepicker).format('YYYY-MM-DD'),
												'Content-Type':'application/json',
								     };
									 var data={};
		                             var URI = BASE_URL + ROOT_URL
		                             + "/api/opd/getPatientDiagnosisByPatientIdForBookView";
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
		                                         console.log("patientDiagnosisList",$scope.patientDiagnosisList);
		                                         	$scope.comorbidityFlag = false;
		                                         	$scope.notifiableFlag = false;
													
														if($scope.patientDiagnosisList.length > 0)
															{
															
																angular.forEach($scope.patientDiagnosisList,function(value,key) {
																	if(value.isNotifiable == 'Y')
																	{
																		$scope.notifiableFlag = true;
																	}
																	else if(value.isComorbidity == 'Y')
																	{
																		$scope.comorbidityFlag = true;
																	}
																});
															}
		                                         
		                                     });
									}
									$scope.getAllPatientDiagnosis();
									
									 //start order procedure order
									$scope.getOrderProcedureList = function()
									{
										if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
											return;
									}
										var headerObj = {
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.patientDetails.patientId,
												fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
												toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
												organization_id : $scope.organizationId
											};
										
										 var data={};
										$scope.orderedProcedureList = [];
										var URI = BASE_URL + ROOT_URL+ "/api/procedure/getProcedureOrderListForBookView";
//										+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+fromDate+"/"+toDate;

								/*GenericService.serviceAction("GET", URI, paramObjForHistList)*/
										GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
								.then(
										function(response) {
											$scope.templateListSickList = [];

											if (response.data.status == "success") {
												$scope.orderedProcedureList=response.data.listObject;
												console.log("$scope.orderedProcedureList", $scope.orderedProcedureList);
											}
										});
									}
									
									$scope.getOrderProcedureList();
									
									 $scope.getDetailedMedicationHistoryList = function()
								        {
										 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
												return;
										}
										 $scope.detailedMedicationsHistory = [];
										 var headerObj = {
												 organizationId : $scope.organizationId
												};

											
											var paramObjForPer = {
													patientId:$scope.patientDetails.patientId,
													fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
													toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
											};
								        	var patientId = $scope.patientDetails.patientId;
								    		var dateObj = moment(new Date()).format('YYYY-MM-DD');
								      		var data={
								      				organizationId:$scope.organizationId,
								      				patientId: $scope.patientDetails.patientId,
								      				
								      				
								      		};
								    		var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationHistoryListForBookView";
								    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
								    		.then(
								    			function(response) {
								    				if (response.data.status == "success") {
								    					$scope.detailedMedicationsHistory1 = response.data.listObject;
								    					console.log("medication history list",$scope.detailedMedicationsHistory1);

								    				}
								    			});
								        }
									 $scope.getDetailedMedicationHistoryList();
									 $scope.getMedicationListByEncounter = function(item)
								        {
								        	console.log(item);
								        	$scope.medicationHistoryList = [];
								        	var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationHistoryByEncounterId" + "?encounterId=" + 
								        	item.encounterId + "&stopDate=" + moment(new Date()).format('DD-MM-YYYY');
								    		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								    			if(response.data.status == "success"){
								    				$scope.medicationHistoryList = response.data.listObject;
								    				console.log("mmmmmmmm",$scope.medicationHistoryList);
								    			}
								    		});
								    		
								        }
									 
									 $scope.getBillList = function()
								        {
										 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
												return;
										}
										 $scope.billList = [];
										  var fromDate=moment($scope.fromDatepicker).format('YYYY-MM-DD');
										  var toDate=moment($scope.toDatepicker).format('YYYY-MM-DD');
										  
								    		var URI = BASE_URL + ROOT_URL + "/api/billing/billlistForBookView/"+$scope.patientDetails.patientId
								    		+"/"+$scope.unitId+"/"+$scope.organizationId+"/"
								    		+fromDate+"/"+toDate;
								    		
								    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE)
								    		.then(
								    			function(response) {
								    				if (response.data.status == "success") {
								    					$scope.billList = response.data.listObject;
								    					console.log("bbbbbbb", $scope.billList)
								    				}
								    			});
								        }
									 $scope.getBillList();
									 
									 
									 //to get travel history
									 $scope.getTravelHistoryList= function(){
										 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
												return;
										}
											var headerObj = {
													organization_id : $scope.organizationId
												};

											
											var paramObjForPer = {
													patientId:$scope.patientDetails.patientId,
													fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
													toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
											};
										 
											
											var data={};
											var URI = BASE_URL + ROOT_URL
						        			+ "/api/opd/getPatientTravelHistoryForBookView";
						        		GenericServiceParamHeader
						        			.serviceAction(METHOD_GET, URI,
						        					data,
						        					NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
						        			.then(
						        					function(response) {
						        						console.log(response);
						        						
						        						// $rootScope.startLoader();
						        						if (response.data.status == "success")
						        						$scope.travelHistoryCurrentList = response.data.listObject;
						        						
						        						$scope.travelHistoryCurrentListDoctor = [];
						        						if($scope.roleId == '2' )
						        							{
						        								
						        								angular.forEach(response.data.listObject, function(value,key) {
						        									
						        										if(value.roleId == 3 && value.isReviewedFlag == 'N')
						        											{
						        										var data = {};
						        										data.whenTravel = moment(value.whenTravel).format('DD-MM-YYYY HH:mm:ss'),
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
										 
										 $scope.getTravelHistoryList();
										 
										
									
											
											$scope.getActiveAllergy=function(){
												if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
													return;
											}
												$scope.dt = moment(new Date()).format('DD-MM-YYYY');

												var headerObj = {
														organization_id : $scope.organizationId
													};

													var paramObjForHistList = {
														patientId :$scope.patientDetails.patientId,
														fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
														toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
													};
												var data = {};
											var URI = BASE_URL + ROOT_URL
											/*+ GETCURRENTALLERGYPATIENTMAPPER;*/
											+ "/api/opd/getAllergyPatientMapperForBookView";
										GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
											.then(
													function(response) {
														console.log(response);
														
														// $rootScope.startLoader();
														if (response.data.status == "success")
														$scope.allergyCurrentList = response.data.listObject;
														
														$scope.allergyCurrentListDoctor = [];
														if($scope.roleId == '2' )
															{
																
																angular.forEach(response.data.listObject, function(value,key) {
																	
																		if(value.roleId == 3 && value.isReviewedFlag == 'N')
																			{
																		var data = {};
																		data.isNoKnownAllergies = value.isNoKnownAllergies;
																		data.remark = value.remark;
																		data.isReviewedFlag = 'N';
																		data.isEnterInErrorStatus='N';
																		data.allergyId = value.allergyId;
																		data.allergyTypeId= value.allergyTypeId;
																		data.type = value.type;
																		data.natureOfReaction = value.natureOfReaction;
																		data.severityId  = value.severityId;
																		data.month = value.month;
																		data.year = value.year;
																		data.allergyPatientMapperId = value.allergyPatientMapperId;
																		$scope.allergyCurrentListDoctor.push(data);
																}
																});
																

																if($scope.allergyCurrentList.length > 0)
																{
																	$scope.allergySymbolList=[];
																   angular.forEach($scope.allergyCurrentList,function(value,index){
																	   if(value.isNoKnownAllergies !='Y'){
																		   $scope.allergySymbolList.push(value);
																	   }
																	   if($scope.allergySymbolList.length > 0){
																		   $scope.allergyFlag = true;
																	   }
																	  
																   });
																	
																}
															}
														
													});
											}
											$scope.getActiveAllergy();
										 
											
											 $scope.getResolveAllergyData=function(){
												 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
												 $rootScope.startLoader();
													var headerObj1 = {
															unit_id : $scope.unitId,
															organization_id : $scope.organizationId
														};

														var paramObjForHistList1 = {
															patientId : $scope.patientDetails.patientId,
														};
														var data = {};
													var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getResolvedAllergyPatientMapperForCoversheet";
												GenericServiceParamHeader
													.serviceAction(METHOD_GET, URI,
															data,
															NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList1,headerObj1)
													.then(
															function(response) {
																console.log(response);
																$scope.resolvedAllergyHistoryList=[];
																if (response.data.status == "success"){
																	$scope.resolvedAllergyHistoryList = response.data.listObject;
																	$rootScope.stopLoader();
																}else{
																	$rootScope.stopLoader();
																}
																	
															});
						                          }
											 $scope.getResolveAllergyData();
										  // For Get Patient Vitals List
								
					                    $timeout( function(){
					                    	  $rootScope.stopLoader();
					                    },5000);
											
											  $timeout( function(){
												  if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
						                    		var paramObjForVital = {
						                    				
						                    				patientId:$scope.patientDetails.patientId,
						                    		}
						                    		
						                    		
						                    		var headerObj = {
						                    				unit_id : $scope.unitId,
															organization_id : $scope.organizationId
														    
						                    		
						                    		};
						                    		
						                    		 var data={
						                    			 	   unitId:$scope.unitId,
						                       				   organizationId:$scope.organizationId,
						                       				   patientId:$scope.patientDetails.patientId,
							                       			   fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
															   toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
						                    	 			}
						                    		
						                            var URI = BASE_URL + ROOT_URL
						                            + "/api/opd/getListOfVitalsByPatientIdForBookView";
						                        GenericServiceParamHeader
						                            .serviceAction(METHOD_POST, URI,
						                                    data,
						                                    NOTIFICATION_MSG_STATUS_FALSE,data)
						                            .then(
						                                    function(response) {
						                                        console.log(response);
						                             
						                            
						                            if (response.data.status == "success")
						                                $scope.vitalFinalList = [];
						                                $scope.patientViatalList  = response.data.listObject;
						                            console.log("patientVitalList",response.data.listObject);
						                            console.log("vitalList",$scope.vitalsList);
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
							                                            		 	newArr.vitalValue = 0;
							                                            		 	newArr.isReviewedFlag = 'N';
							                                            		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
							                                            		 	if($scope.roleId == 2)
						                                            		 		{
							                                            		 		newArr.appointmentVitalMapperId ='';
						                                            		 		}
																					$scope.vitalFinalList.push(newArr);
																				}
						                            	       
						                                });
						                             
						                             if($scope.vitalFinalList.lenght>0)
						                               {
						                                 //$scope.calculateBMI();
						                            	    var height = 0.00;
						                                    var weight = 0.00;
						                                    var bmi = 0.00;
						                                     angular.forEach($scope.vitalFinalList, function(value , key) 
						                                     {
						                                          if(value.vitalId ==2)
						                                           {
						                                             height = value.vitalValue;
						                                           }
						                                          if(value.vitalId ==1)
						                                       {
						                                           weight = value.vitalValue;
						                                       }
						                                          if(value.vitalId ==8)
						                                       {
						                                           if(height!=null && height!=0)
						                                            {
						                                             var ht = height / 100;
						                                                      bmi = weight/(ht * ht);
						                                                      value.vitalValue = parseFloat(bmi).toFixed(2);
						                                            }
						                                       }
						                                          
						                                          
						                                       });
						                               }
						                             console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
						                             
						                           
						                                                                                      
						                                    });
						                    
						                    },4000);
											  
											  
										         $timeout( function(){
										        	 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
															return;
													}
								                		var paramObjForVital = {
								                				
								                				patientId:$scope.patientDetails.patientId,
								                					
								                					
								                		}
								                		
								                		console.log("jj",paramObjForVital);
								                		//return false;
								                		 var data={
							                    			 	   unitId:$scope.unitId,
							                       				   organizationId:$scope.organizationId,
							                       				   patientId:$scope.patientDetails.patientId,
							                       				   fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
																   toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
							                    	 			}
							                    		
							                            var URI = BASE_URL + ROOT_URL
							                            + "/api/opd/getListOfVitalsByPatientIdForBookView";
							                        GenericServiceParamHeader
							                            .serviceAction(METHOD_POST, URI,
							                                    data,
							                                    NOTIFICATION_MSG_STATUS_FALSE,data)
								                        .then(
								                                function(response) {
								                                    console.log(response);
								                         
								                        
								                        if (response.data.status == "success")
								                            $scope.vitalFinalList = [];
								                            $scope.patientViatalList  = response.data.listObject;
								                        console.log("patientVitalList",response.data.listObject);
								                        console.log("vitalList",$scope.vitalsList);
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
								                            		 	if($scope.roleId == 3)
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
								                                            		 	newArr.vitalValue = 0;
								                                            		 	newArr.isReviewedFlag = 'N';
								                                            		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
								                                            		 	if($scope.roleId == 3)
								                                        		 		{
								                                            		 		newArr.appointmentVitalMapperId ='';
								                                        		 		}
																						$scope.vitalFinalList.push(newArr);
																					}
								                        	       
								                            });
								                         
								                       
								                         console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
								                         
								                       
								                                                                                  
								                                });
								                
								                },4000);
											  
											  
											 /* $scope.searchMedicationEncounter = function()
										        {
												 var headerObj = {
															unit_id : $scope.unitId,
															organization_id : $scope.organizationId
														};

													
													var paramObjForPer = {
															patientId:$scope.patientDetails.patientId,
//															encounterId : $scope.encounterId,
												    		roleId : 1
													};
												 
													
													var data={};
										        	
										    		$scope.visitTypeDesc = "OP";
										        	
												 var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getMedicationEncountersList/"+$scope.patientDetails.patientId+"/"
													+ $scope.encounterId+"/"+$scope.organizationId+"/"+$scope.unitId;
												 GenericServiceParamHeader
								        			.serviceAction(METHOD_GET, URI,
								        					data,
								        					NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.medicationEncounterList = response.data.listObject;
															console.log("gggggggggggggg",$scope.medicationEncounterList);
														});
											 }
											 $scope.searchMedicationEncounter();	*/
									
									
											 
											  $scope.getDateWiseEncounterIdByPatientId = function()
										        {
												  if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
												 var headerObj = {
															unit_id : $scope.unitId,
															organization_id : $scope.organizationId
														};

													
													var paramObjForPer = {
															patientId:$scope.patientDetails.patientId,
													};
												 
													
													var data={};
										        	
													var fromDate=moment($scope.fromDatepicker).format('YYYY-MM-DD');
													var toDate=moment($scope.toDatepicker).format('YYYY-MM-DD');
												 var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getDateWiseListOfPatientMedication/"+$scope.patientDetails.patientId+"/"
													+$scope.organizationId+"/"+fromDate+"/"+toDate;
												 GenericServiceParamHeader
								        			.serviceAction(METHOD_GET, URI,
								        					data,
								        					NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.listOfMedicationEncounters = response.data.listObject;
															console.log("aaaaaaaaaaa",$scope.listOfMedicationEncounters);
														});
											 }
											 $scope.getDateWiseEncounterIdByPatientId();	
											 $scope.clinObj = {
														clinicalNote :"",
														showclinicalNote : ""
												}
												$scope.setClinicalNote = function(item,indx) {
													$scope.clinObj.clinicalNote ="";
												}
												
												$scope.showClinicalNote = function(item) {
													
													$scope.clinObj.showclinicalNote =item.ordRemarks;
												}
											 $scope.getInvestigationList=function(){
												 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
													var data={};
													var paramObj={};
													var headers = {
															patientId : $scope.patientDetails.patientId,
															organizationId : $scope.organizationId,
														};
													  var fromDate=moment($scope.fromDatepicker).format('YYYY-MM-DD');
													  var toDate=moment($scope.toDatepicker).format('YYYY-MM-DD');
													$scope.currentEncounterOrderLists = [];
										    		var URI = BASE_URL + ROOT_URL + "/api/opd/getInvestigationsListForBookView/" +$scope.patientDetails.patientId
										    		+"/"+$scope.organizationId+"/"
										    		+fromDate+"/"+toDate;
										    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI,"",NOTIFICATION_MSG_STATUS_FALSE)
										    		.then(
										    			function(response) {
										    				if (response.data.status == "success") {
										    					
										    					$scope.currentEncounterOrderLists = response.data.listObject;

										    				}
										    			});
												}
												$scope.getInvestigationList();
												
												
												  /*******General Examn for Casesheet *******/
										        $scope.getActiveGeneralExamination=function(){
										        	if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
										        	 var data = {
										        			 patientId:$scope.patientDetails.patientId,
										        			 organizationId:$scope.organizationId,
																'typeId':1,
																fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
																toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
										        	 };
													var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getListOfSystemsAndObservationForBookView";
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
										        $scope.getActiveGeneralExamination();
												
												/*******Systemic Examn for Casesheet *********/
										        $scope.getSystemicExaminationForCaseSheeet=function(){
										        	if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
										        	 var data = {
										        			 patientId:$scope.patientDetails.patientId,
										        			 organizationId:$scope.organizationId,
																'typeId':2,
																fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
																toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
										        	 };
													var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getListOfSystemsAndObservationForBookView";
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
										      
										 /*****************************************************/ 
										        
										       /* $scope.getActiveGeneralExamination=function(){
										        	var paramObjForGen = {
															
										        			patientId:$scope.patientDetails.patientId,
															'typeId':1,
															fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
															toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
															
													};
											         var data={};
											         
											         var headerObj = {
																unit_id : $scope.unitId,
																organization_id : $scope.organizationId
															};

											         
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
										                         console.log("General Exam List", $scope.getSavedGeneralExamList);
										                    });
										        }
										        $scope.getActiveGeneralExamination();*/
										        
										      /*  $scope.getActiveGeneralExamination=function(){
										        var paramObjForGen1 = {
														
														patientId:$scope.patientDetails.patientId,
														'typeId':2,
														fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
														toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
														// 'genderId':commonObj.genderId
												};
										        
										        var data={};
										         
										         var headerObj = {
															unit_id : $scope.unitId,
															organization_id : $scope.organizationId
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
										                        console.log("physical Exam List", $scope.getSavedPhysicalExamList);
										                    });
									
										        }
										        $scope.getActiveGeneralExamination();*/
									
//							 });
							 }
							 $scope.getiteratedlist();
							 
							 $scope.onClick = function () {
								   $scope.myChartForSearch = false;
								   $scope.vitalnameGraphList=[];
			                    	 $scope.vitalname=[];
			                    	 $scope.createdTime=[];
			        				 $scope.vitalData=[];
			        				 $scope.vitalTime=[];
			        				 $scope.vitalLabel=[];
			        				 //for label
			        				 if($scope.patientViatalList != undefined ){
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
	     							/* var letters =['#22EBC8','#00D1DB','#00B3E2','#0093D6','#5372B9','#6F518F']; //"0F"; 
	     							for(var j=0;j<16;j++){
										var color = ''; 
										for (var i = 0; i < 6; i++) {
										color = letters[i];//letters[(Math.floor(Math.random() * 16))];
										$scope.colorArray.push(color);
									    } 
									    }*/
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
//			                    		                    maxTicksLimit: 5,
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
			        				 }
			                    };
								 
								
							   // },4000);
								/************************Report Prescription*******Chetana******/
								$scope.rePrescriptionCaseSheet=function(info){

								/*	  var data =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "prescrptioncasesheet.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "Prescription",
									                 "reportParams": [
									                	 {
											                  "paramName": "@encounterid",
											                  "paramType": "Integer",
											                  "paramValue": $scope.encounterId
										                },
										                {
											                  "paramName": "@patient_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.encounterDetailsObj.patientId
										                },
										                {
												             "paramName": "@organizationid",
												             "paramType": "Integer",
												             "paramValue": $scope.organizationId
													       },
													       {
												             "paramName": "@unitid",
												             "paramType": "Integer",
												             "paramValue": $scope.unitId
													       }
							                 ]
							      };
									    reportService.generate(REPORT_API_URI1,data);*/
									var prescriptionDate = info.prescriptionDate;
									/*var encounterDate = info.encounterDate;*/
									$scope.printprescriptionDate =  moment(info.prescriptionDate, "DD-MM-YYYY").toDate();
									var newPrintprescriptionDate = moment($scope.printprescriptionDate).format('YYYY/MM/DD');
									var data =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "prescrptioncasesheetDetails.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "Prescription",
							                 "reportParams": [
							                	 {
							                		 "paramName": "@prescription_date",
									                  "paramType": "string", 
									                  "paramValue":newPrintprescriptionDate
								                },
								                {
								                	"paramName": "@patient_id",
									                  "paramType": "Integer",
									                  "paramValue": $scope.encounterDetailsObj.patientId
								                },
								                {
								                	"paramName": "@organizationid",
										             "paramType": "Integer",
										             "paramValue": $scope.organizationId
											       },
											       {
											    	   "paramName": "@unitid",
											             "paramType": "Integer",
										             "paramValue": $scope.unitId
											       }
					                 ]
									};
									reportService.generate(REPORT_API_URI1,data);
									}
								
								
								/********************Report***********************Added By Chetana*******************/
								
								$scope.printBill = function(bill)
								{
									var URIVALI = BASE_URL + ROOT_URL + "/api/billing/printLog/1/"+$scope.userId+"/"+bill.billingMasterId;
									BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
									{
										$rootScope.stopLoader();
										if (response.data.status == "success")
										{
											if(bill.visitTypeId==1)
											{
												var data =  {
										                 "appName": "HIS",
										                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
										                 "reportDesignFilePath": "opdbillreceipt.rpt",
										                 "reportFormat": "PDF",
										                 "exportFileName": "PatientBill",
										                 "reportParams": [
										                   {
											                  "paramName": "@billid",
											                  "paramType": "Integer",
											                  "paramValue": bill.billingMasterId
										                   },
										                   {
											                  "paramName": "@isDuplicate",
											                  "paramType": "Integer",
											                  "paramValue": 1
											               }
										                 ]
										      };
												}else{
													var data =  {
												  			
											                 "appName": "HIS",
											                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											                 "reportDesignFilePath": "Ipdbill.rpt",
											                 "reportFormat": "PDF",
											                 "exportFileName": "IPDBillReciept",
											                 "reportParams": [
											                	 {
													                  "paramName": "Billid",
													                  "paramType": "Integer",
													                  "paramValue": bill.billingMasterId
												                },
												                {
													                  "paramName": "@patient_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.patientDetails.patientId
												                },
												                {
													                  "paramName": "IsSummary",
													                  "paramType": "String",
													                  "paramValue": "N"
												                },
												                {
													                  "paramName": "@isDuplicate",
													                  "paramType": "Integer",
													                  "paramValue": 1
													            }
									                ]
													};
												}
												reportService.generate(REPORT_API_URI1,data);
										}
									});
									
								
								}
						/*************************************************************************/
								/************************Report Investigation*******Chetana******/
								$scope.labTestResId=false;
								
								var labConfig  =    $scope.checkLabConfig();
							    $scope.datasetOverride = [{
							        yAxisID: 'y-axis-1'
							    }, {
							        yAxisID: 'y-axis-2'
							    }];
							    $scope.options = {
							        scales: {
							            yAxes: [{
							                id: 'y-axis-1',
							                type: 'linear',
							                display: true,
							                position: 'left'
							            }]
							        }
							    };
							    
						        
		                         $scope.printHTMLReport=function(info,printWithHeader){
		                        	 if(info.isHtmlPrint==true){
		                        			info.organizationId = $scope.organizationId;
								        	info.unitId =  $scope.unitId;
								        	info.printWithHeader = printWithHeader;
			                                info.unitName =  $scope.unitName;
			                                if(info.isFinalRelease == null){
			                                	info.isFinalRelease = 'Y';
			                                }
			                                if(info.isProvisionalRelease == null){
			                                	info.isProvisionalRelease = 'Y';
			                                }
			                                info.email="";
		                        		if(info.specialityId == $scope.radiologySpecialityId){
							            var rdData={};
							            rdData=info;
		                                delete rdData.collectedUserName;
		                                delete rdData.createdDateNew;
		                                delete rdData.isAutoRender;
		                                delete rdData.isPackageService;
		                                delete rdData.isPanel;
		                                delete rdData.isProcedureService;
		                                delete rdData.labSampleDtlsId;
		                                delete rdData.labTestResId;
		                                delete rdData.labTestResId;
		                                delete rdData.microTemplateStatusId;
		                                delete rdData.orderDetailsPackageMapperDtosList;
		                                delete rdData.orderId;
		                              //  delete rdData.orderSchDate;
		                                delete rdData.orderSchDateNew;
		                                delete rdData.orderSetId;
		                               // delete rdData.orderStatus;
		                                delete rdData.packageId;
		                                delete rdData.panelId;
		                                delete rdData.proReqId;
		                                delete rdData.procedureReqId;
		                                delete rdData.sampleGenDatetime;
		                                delete rdData.sampleGenDatetimeNew;
		                                delete rdData.sampleStatusId;
		                                delete rdData.serviceIsBilled;
		                                delete rdData.serviceMasterId;
		                                delete rdData.serviceStandardCode;
		                              //  delete rdData.serviceStandardName;
		                                delete rdData.slotId;
		                               // delete rdData.statusName;
		                                delete rdData.subDeptId;
		                                delete rdData.subSpecialityId;
		                                delete rdData.subDeptId;
		                                delete rdData.userName;
		                             //   delete rdData.createdDate;
		                                delete rdData.createdDateBookView;
		                                delete rdData.title ;
		                                delete rdData.labSampleNo ;
		                                delete rdData.isProcedure ;
		                                delete rdData.reportEntryDate ;
		                                delete rdData.reportValidationDate ;
		                                delete rdData.reportReleaseDate ;
		                               
							        	var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTest";
							    		BillingGenericService.serviceAction(METHOD_POST, URI, rdData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				   console.log("gg",response.data.object);
							    				   var headerSize = "";
							    					 var letterImgAndHeader="";
							    					 var patientDetails = "";
							    					 var completeTemplateContent = "";
							    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
							    						 //print patient details on all pages
							    						 headerSize = "220px;";
							    						 letterImgAndHeader = response.data.object.latterHeadImg+response.data.object.header;
							    						 completeTemplateContent = response.data.object.templateContent+response.data.object.footer;
							    					 }else{
							    						 //print patient details on first page
							    						 headerSize = "105px;";
							    						 letterImgAndHeader = response.data.object.latterHeadImg;
							    						 if(printWithHeader == 0){
							    							 patientDetails = response.data.object.header;
							    						 }else{
							    							 patientDetails = response.data.object.headerWithoutImg;
							    						 }
							    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
							    					 }
							    				   
							    				   
							    				   var printWindow = window.open('', '', 'height=500,width=700');  
		                                           printWindow.document.write('<html><head></head>');  
		                                           printWindow.document.write(' <style> '
		                                                   +'.page-header, .page-header-space {height: '+headerSize+'} '
		                                                   +' .page-footer, .page-footer-space { height: 105px;} '
		                                                   + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
		                                                   + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
		                                                   + ' .page {page-break-after: auto;} '
		                                                   + '  @page { margin: 20mm}'
		                                                   + 'p { margin:0px 0px 0px 0px; }'
		                                                   + ' </style>' );
		                                           printWindow.document.write('<body>');
		                                           if(printWithHeader == 0){
		                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
		                                                 printWindow.document.write(letterImgAndHeader);  
		                                                 printWindow.document.write('</div>');
		                                                 
		                                                 printWindow.document.write('<div class="page-footer" style="text-align: center">');
		                                                 printWindow.document.write(response.data.object.footerImg);  
		                                                 printWindow.document.write('</div>');
		                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
		                                        	 //skip this part during first page bcoz we append patient details allready
		                                               printWindow.document.write('<div class="page-header" style="text-align: center">');
		                                               printWindow.document.write(response.data.object.headerWithoutImg);  
		                                               printWindow.document.write('</div>');
		                                           }
		                                           
		                                           printWindow.document.write('<table> <thead> <tr><td><div class="page-header-space"></div> </td></tr> </thead>');
		                                           printWindow.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
		                                           printWindow.document.write(completeTemplateContent);  
		                                           printWindow.document.write('</div> </td></tr></tbody>');
		                                           printWindow.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');
		                                           printWindow.document.write('</body>');
		                                           printWindow.document.write('</html>');
		                                           printWindow.document.close();  
		                                           $timeout( function(){
		                                                printWindow.print();  
		                                           },2000);
									            
										            
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
		                        	 }else if(info.specialityId == $scope.deptId){
		                        			var newObj = {};
								        	  newObj.organizationId = $scope.organizationId;
								        	  newObj.uhidNumber = info.uhidNumber;
											  newObj.unitId =  $scope.unitId;
											  newObj.patientName = info.patientName;
											  newObj.orderDate = info.orderDate;
											  newObj.orderDtTime = info.orderDateTime;
											  newObj.prefixCode = info.prefixCode;
											  newObj.referralName = null;
											  newObj.email = info.email;
											  newObj.ageString = info.ageString;
											  newObj.genderCode = info.genderCode;
											  newObj.templateContent = info.templateObject; 
											  newObj.genderId = info.genderId;
											  newObj.labSampleNo = info.labSampleNo;
											  newObj.orderNo = info.orderNo;
											  newObj.visitNo = info.visitNo;
											  newObj.printWithHeader = 0;
											  newObj.companyDesc = info.companyDesc;
											  newObj.comAddress = null;
											  newObj.paymentEntitlementId = info.paymentEntitlementId;
											  newObj.isProvisionalRelease = 'N';
											  newObj.isFinalRelease = 'Y';
											  newObj.registerDate = info.registerDate;
											  newObj.unitName = info.unitName;
											  newObj.reportEntryDate = info.reportEntryDate;
											  newObj.reportValidationDate = info.reportValidationDate;
											  newObj.reportReleaseDate = info.reportReleaseDate;
											  newObj.sampleType = info.sampleType;
											  newObj.isReportRecall = true;
											  newObj.labSampleDetailsId = info.labSampleDtlsId;
											  newObj.orderDetailsId = info.orderDetailsId;
//											  newObj.sampleType="Whole Blood";
											  
											  var URI = BASE_URL + ROOT_URL + "/api/opd/RIS/printReportTestLab";
									    		BillingGenericService.serviceAction(METHOD_POST, URI, newObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
									    				{
									    	
									    			   if(response.data.status=='success')
									    			      {
									    				   console.log("gg",response.data.object);
									    				   $rootScope.stopLoader();
									    				   var headerSize = "";
									    					 var letterImgAndHeader="";
									    					 var patientDetails = "";
									    					 var completeTemplateContent = "";
									    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
									    						 //print patient details on all pages
									    						 headerSize = "220px;";
									    						 letterImgAndHeader = response.data.object.latterHeadImg+response.data.object.header;
									    						 completeTemplateContent = response.data.object.templateContent+response.data.object.footer;
									    					 }else{
									    						 //print patient details on first page
									    						 headerSize = "105px;";
									    						 letterImgAndHeader = response.data.object.latterHeadImg;
									    						 if(printWithHeader == 0){
									    							 patientDetails = response.data.object.header;
									    						 }else{
									    							 patientDetails = response.data.object.labHeaderWithoutImg;
									    						 }
									    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
									    					 }
									    				   
									    				   
									    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                                           printWindow.document.write('<html><head></head>');  
				                                           printWindow.document.write(' <style> '
				                                                   +'.page-header, .page-header-space {height: '+headerSize+'} '
				                                                   +' .page-footer, .page-footer-space { height: 105px;} '
				                                                   + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
				                                                   + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
				                                                   + ' .page {page-break-after: auto;} '
				                                                   + 'p { margin:0px 0px 0px 0px; }'
				                                                   + '  @page { margin: 20mm}'
				                                                   + ' </style>' );
				                                           printWindow.document.write('<body>');
				                                           if(printWithHeader == 0){
				                                                 printWindow.document.write('<div class="page-header" style="text-align: center">');
				                                                 printWindow.document.write(letterImgAndHeader);  
				                                                 printWindow.document.write('</div>');
				                                                 
				                                                 printWindow.document.write('<div class="page-footer" style="text-align: center">');
						                                         printWindow.document.write(response.data.object.footerImg);  
						                                         printWindow.document.write('</div>');
				                                           }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
				                                        	   //skip this part during first page bcoz we append patient details allready
				                                                printWindow.document.write('<div class="page-header" style="text-align: center">');
				                                                printWindow.document.write(response.data.object.labHeaderWithoutImg);  
				                                                printWindow.document.write('</div>');
				                                            }
				                                           
				                                           printWindow.document.write('<table> <thead> <tr><td><div class="page-header-space"></div> </td></tr> </thead>');
				                                           printWindow.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
				                                           printWindow.document.write(completeTemplateContent);  
				                                           printWindow.document.write('</div> </td></tr></tbody>');
				                                           printWindow.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');
				                                           printWindow.document.write('</body>');
				                                           printWindow.document.write('</html>');
				                                           printWindow.document.close();  
				                                           $timeout( function(){
				                                                printWindow.print();  
				                                           },2000);
									    			      }
									    			   else if(response.data.status=='error'){
									    				   $rootScope.stopLoader();
									    			   }
									    		});
		                        	 }else if(info.isProcedure == 'Y'){
		 					        	var infoNew = {};
							        	infoNew.ageString = info.ageString;
							        	infoNew.encounterId = info.encounterId;
							        	infoNew.admissionId = info.admissionId;
							        	infoNew.encounterTypeId = info.encounterTypeId;
							        	infoNew.genderCode= info.genderCode;
							        	infoNew.genderId= info.genderId;
							        	infoNew.isProvisionalRelease= info.isProvisionalRelease;
							        	infoNew.isReportRecall= true;
							        	infoNew.orderDate= info.orderDate;
							        	infoNew.orderDetailsId= info.orderDetailsId;
				    					infoNew.orderNo= info.orderNo;
				    					infoNew.patientCategory= info.patientCategory;
				    					infoNew.patientId= info.patientId;
				    					infoNew.patientName= info.patientName;
				    					infoNew.prefixCode= info.prefixCode;
				    					infoNew.referralName= info.referralName;
				    					infoNew.registerDate= info.registerDate;
				    					infoNew.serviceAmt= info.serviceAmt;
				    					infoNew.serviceCode= info.serviceCode;
				    					infoNew.serviceId= info.serviceId;
				    					infoNew.serviceName= info.serviceName;
				    					infoNew.serviceRendered= info.serviceRendered;
				    					infoNew.specialityId= info.specialityId;
				    					infoNew.specialityName= info.specialityName;
				    					infoNew.subSpecialityId= info.subSpecialityId;
				    					infoNew.templateContent = info.templateContent;		
				    					infoNew.templateDesc= info.templateDesc;
				    					infoNew.templateDoctorId= info.templateDoctorId;
				    					infoNew.templateDoctorName= info.templateDoctorName;
				    					infoNew.templateFormatId= info.templateFormatId;
				    					infoNew.templateObject = info.templateObject;
				    					infoNew.infoId= info.infoId;
				    					infoNew.templateTypeId= info.templateTypeId;
				    					infoNew.title= info.title;
				    					infoNew.uhidNumber= info.uhidNumber;
				    					infoNew.visitName= info.visitName;
				    					infoNew.visitNumber= info.visitNumber;
				    					infoNew.visitTypeId= info.visitTypeId;
				    					infoNew.wardName= info.wardName;
				    					infoNew.billNo = info.billNo;
				    					infoNew.organizationId = $scope.organizationId;
				    					infoNew.orderDtTime = info.orderDtTime;
								        	infoNew.unitId =  $scope.unitId;
								        	infoNew.printWithHeader = 0; //print with header
								        	infoNew.unitName =  $scope.unitName;
								        	var URI = BASE_URL + ROOT_URL + "/api/dignostic/printDiagnosisReport";
								    		BillingGenericService.serviceAction(METHOD_POST, URI, infoNew,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								    				{
								    	
								    			   if(response.data.status=='success')
								    			      {
								    				   var headerSize = "";
								    					 var letterImgAndHeader="";
								    					 var patientDetails = "";
								    					 var completeTemplateContent = "";
								    					 if($scope.isRisPatientDetailsOnFirstPage == 'N'){
								    						 //print patient details on all pages
								    						 headerSize = "220px;";
								    						 letterImgAndHeader = response.data.object.latterHeadImg+response.data.object.header;
								    						 completeTemplateContent = response.data.object.templateContent+response.data.object.footer;
								    					 }else{
								    						 //print patient details on first page
								    						 headerSize = "105px;";
								    						 letterImgAndHeader = response.data.object.latterHeadImg;
								    						 if(info.printWithHeader == 0){
								    							 patientDetails = response.data.object.header;
								    						 }else{
								    							 patientDetails = response.data.object.headerWithoutImg;
								    						 }
								    						 completeTemplateContent = patientDetails + response.data.object.templateContent+response.data.object.footer;
								    					 }
								    				   
								    				   var printWindow = window.open('', '', 'height=500,width=700');  
			                                         printWindow.document.write('<html><head></head>');  
			                                         printWindow.document.write(' <style> '
			                                                 +'.page-header, .page-header-space {height: '+headerSize+'} '
			                                                 +' .page-footer, .page-footer-space { height: 105px;} '
			                                                 + ' .page-footer { position: fixed; bottom: 0; width: 100%;} '
			                                                 + ' .page-header { position: fixed; top: 0mm; width: 100%;} '
			                                                 + ' .page {page-break-after: auto;} '
			                                                 + '  @page { margin: 20mm}'
			                                                 + 'p { margin:0px 0px 0px 0px; }'
			                                                 + ' </style>' );
			                                         printWindow.document.write('<body>');
			                                         if(info.printWithHeader == 0){
			                                               printWindow.document.write('<div class="page-header" style="text-align: center">');
			                                               printWindow.document.write(letterImgAndHeader);  
			                                               printWindow.document.write('</div>');
			                                               
			                                               printWindow.document.write('<div class="page-footer" style="text-align: center">');
			                                               printWindow.document.write(response.data.object.footerImg);  
			                                               printWindow.document.write('</div>');
			                                         }else if($scope.isRisPatientDetailsOnFirstPage == 'N'){
			                                      	 //skip this part during first page bcoz we append patient details allready
			                                             printWindow.document.write('<div class="page-header" style="text-align: center">');
			                                             printWindow.document.write(response.data.object.headerWithoutImg);  
			                                             printWindow.document.write('</div>');
			                                         }
			                                         
			                                         printWindow.document.write('<table> <thead> <tr><td><div class="page-header-space"></div> </td></tr> </thead>');
			                                         printWindow.document.write(' <tbody><tr><td><div class="page" style="line-height: 1.2;">');
			                                         printWindow.document.write(completeTemplateContent);  
			                                         printWindow.document.write('</div> </td></tr></tbody>');
			                                         printWindow.document.write(' <tfoot> <tr><td><div class="page-footer-space"></div></td></tr></tfoot></table>');
			                                         printWindow.document.write('</body>');
			                                         printWindow.document.write('</html>');
			                                         printWindow.document.close();  
			                                         $timeout( function(){
			                                              printWindow.print();  
			                                         },2000);
											            
											            
								    			      }
								    			   else if(response.data.status=='error'){
								    				   $rootScope.stopLoader();
								    			   }
								    		});
								       

							        }
		                        	 }
							    		

							        }
								
								$scope.rePrintInvestigation=function(labObj,idx,printFlag)
								{
									if(labObj.sampleStatusId==8)
									{
									if(labConfig.biochemistryId==labObj.subDeptId||labConfig.clinicalPathologyId==labObj.subDeptId||labConfig.hematologyId==labObj.subDeptId)
									{
									var data =  
									{
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "pathologyReport.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "pathologyReport",
								            "reportParams": [
								              {
									             "paramName": "labtesresid",
									             "paramType": "Integer",
									             "paramValue": labObj.labTestResId
								              },{
										             "paramName": "isOriginal",
										             "paramType": "Integer",
										             "paramValue": printFlag
									           }
								            ]
								      };
									  reportService.generate(REPORT_API_URI1,data);
									}
								else if(labConfig.microbiologyId==labObj.subDeptId)
									{
									var data =  
									{
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "pathologyReport_Sensitivity.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "pathologyReport",
								            "reportParams": [
								              {
									             "paramName": "lab_sample_dtls_id",
									             "paramType": "Integer",
									             "paramValue": labObj.labSampleDtlsId
								              },{
										             "paramName": "isOriginal",
										             "paramType": "Integer",
										             "paramValue": printFlag
									           }
								            ]
								      };
									   reportService.generate(REPORT_API_URI1,data);
									}
								else if(labConfig.histopathologyId==labObj.subDeptId||labConfig.cystopathologyId==labObj.subDeptId)
								{
								 var data =  
								   {
							            "appName": "HIS",
							            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							            "reportDesignFilePath": "GetHistopathologyReport.rpt",
							            "reportFormat": "PDF",
							            "exportFileName": "pathologyReport",
							            "reportParams": [
							              {
								             "paramName": "labtesresid",
								             "paramType": "Integer",
								             "paramValue": labObj.labSampleDtlsId
							              },{
									             "paramName": "isOriginal",
									             "paramType": "Integer",
									             "paramValue": printFlag
								           }
							            ]
							       };
								      reportService.generate(REPORT_API_URI1,data);
								  }else if(labConfig.serologyId==labObj.subDeptId){
										var data =  
										{
									            "appName": "HIS",
									            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									            "reportDesignFilePath": "SerologyReport.rpt",
									            "reportFormat": "PDF",
									            "exportFileName": "pathologyReport",
									            "reportParams": [
									              {
										             "paramName": "lab_sample_dtls_id",
										             "paramType": "Integer",
										             "paramValue": labObj.labSampleDtlsId
									              },
									              {
											             "paramName": "isOriginal",
											             "paramType": "Integer",
											             "paramValue": printFlag
										           }
									            ]
									      };
										reportService.generate(REPORT_API_URI1,data);
								}else{
									  var data =  
										{
									            "appName": "HIS",
									            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									            "reportDesignFilePath": "pathologyReport.rpt",
									            "reportFormat": "PDF",
									            "exportFileName": "pathologyReport",
									            "reportParams": [
									              {
										             "paramName": "labtesresid",
										             "paramType": "Integer",
										             "paramValue": labObj.labTestResId
									              },{
											             "paramName": "isOriginal",
											             "paramType": "Integer",
											             "paramValue": printFlag
										           }
									            ]
									      };
										  reportService.generate(REPORT_API_URI1,data);
								  }	
								}
									
									else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
										{
						    			var data =  {
						    		            "appName": "HIS",
						    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						    		            "reportDesignFilePath": "RadiologyReport.rpt",
						    		            "reportFormat": "PDF",
						    		            "exportFileName": "RadiologyReport",
						    		            "reportParams": [
						    		              {
						    			             "paramName": "template_transaction_id",
						    			             "paramType": "Integer",
						    			             "paramValue": labObj.templateTransactionId
						    		              },{
										        		"paramName": "isOriginal",
										        		"paramType": "Integer",
										        		"paramValue": 1
										        		},{
										        		"paramName": "isHeader",
										        		"paramType": "Integer",
										        		"paramValue": 0
										        		}
						    		            ]
						    		    };
						    	           reportService.generate(REPORT_API_URI1,data);
										}
								 else
								 {
								  growl.warning("Final Report not released!!",
										{
											title : 'Warning!'
										});
								  }
							 }
								
								
								$scope.checkReport=function(labObj,idx,printFlag)
								{
									if(labObj.sampleStatusId==8)
									{
										/*if(labConfig.biochemistryId==labObj.subDeptId||labConfig.clinicalPathologyId==labObj.subDeptId||labConfig.hematologyId==labObj.subDeptId)
										{
											 return true;
										}
										else if(labConfig.microbiologyId==labObj.subDeptId)
											{
											 return true;
											}
										else if(labConfig.histopathologyId==labObj.subDeptId || labConfig.cystopathologyId==labObj.subDeptId)
										{ 
											return true;
										}
										else if(labConfig.serologyId==labObj.subDeptId)
											{
											  return true;
											}*/
										return true;
								   }
									else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
										{
						    	         return true;
									 }else if(labObj.microTemplateStatusId!=null && labObj.microTemplateStatusId == 41 ){
                                            return true;
									 }
								 else
								 {
								     return false;
								  }
							 }
								
								
								/******************************Report End**************************/
								/******************************Document Type List**************************/
								$scope.getDocumentType = function(){
									if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
										return;
								}
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.documentTypeList = [];
											if (response.data.status == "success") {
												$scope.documentTypeList = response.data.listObject;
												console.log("dddddddddddddddd",$scope.documentTypeList)
											}
										});
								}
								$scope.getDocumentType();
								/*********************************************************************/
								
								 $scope.getPastVisitData = function()
							        {
							         $rootScope.startLoader();
							         var data =
							           {
							           "patientId":$scope.patientDetails.patientId,
							           "organizationId":$scope.organizationId,
							           "fromDate":moment($scope.fromDatepicker).format('YYYY-MM-DD'),
							           "toDate":moment($scope.toDatepicker).format('YYYY-MM-DD'),
							          }
							         var URI = BASE_URL + ROOT_URL + "/api/global/getAllPatientVisits";
							         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							           $scope.patientPastVisitList = [];
							           if (response.data.status == "success") 
							           {
							            $scope.opVisitList = response.data.object.encounterList;
							            console.log("opd list",$scope.opVisitList);
							            $scope.ipVisitList = response.data.object.admissionList;
							            console.log("ip list",$scope.ipVisitList);
							            $rootScope.stopLoader();
							           }
							           else if(response.data.status == "error")
							            {
							            $rootScope.stopLoader();
							            }
							          });
							        }
								 
								 //discharge summary print call
								 $scope.getDataForPrint=function(obj,dischargeSummaryId,admissionId){
									 $scope.isHtml=obj.isHtml;
									 $scope.dischargeSumId = dischargeSummaryId;
									 $scope.admId = admissionId;
									 
									 if($scope.isHtml != null && $scope.isHtml == 'Y'){
											$("#printHeaderReportModal").show();
										}else{
											var data =  
											{
										            "appName": "HIS",
										            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
										            "reportDesignFilePath": "Dischargesummary.rpt",
										            "reportFormat": "PDF",
										            "exportFileName": "pathologyReport",
										            "reportParams": [
										              {
											             "paramName": "discharge_summary_id",
											             "paramType": "String",
											             "paramValue": dischargeSummaryId
										              },
										              {
												             "paramName": "selectedHeader",
												             "paramType": "Integer",
												             "paramValue":  1
											              }
										            ]
										      };
											reportService.generate(REPORT_API_URI1,data);
										}
									 
								 }
									$scope.printDischargeCard=function(dischargeSummaryId,admissionId){
										if(dischargeSummaryId!=null){ 
											var elementId = document.getElementById('dischargeCardId');
										    if(elementId!=null)
										    	{
										    	 document.body.removeChild(elementId);
										    	}
										   	
										var URI = BASE_URL + ROOT_URL + "/adt/discharge/dischargeSummaryCard";
									     var form = null;
									     form = document.createElement("form");
									     form.setAttribute("id", "dischargeCardId");
									     form.setAttribute("name", "Discharge Card");
									     form.setAttribute("method", "post");
									     form.setAttribute("action", URI);
									     form.setAttribute("target", "_blank");
									     
									     
									     var hiddenField1 = document.createElement("input"); 
									     hiddenField1.setAttribute("type", "hidden");
									     hiddenField1.setAttribute("name", "admissionId");
									     hiddenField1.setAttribute("value",$scope.admId);
									     
									     var hiddenField2 = document.createElement("input"); 
									     hiddenField2.setAttribute("type", "hidden");
									     hiddenField2.setAttribute("name", "dischargeSummaryId");
									     hiddenField2.setAttribute("value",$scope.dischargeSumId);
									     
									     var hiddenField3 = document.createElement("input"); 
									     hiddenField3.setAttribute("type", "hidden");
									     hiddenField3.setAttribute("name", "orgUnitId");
									     hiddenField3.setAttribute("value",$scope.unitId);
									     
									     var hiddenField4 = document.createElement("input"); 
									     hiddenField4.setAttribute("type", "hidden");
									     hiddenField4.setAttribute("name", "orgId");
									     hiddenField4.setAttribute("value", $scope.organizationId);
									     
									     var hiddenField5 = document.createElement("input"); 
					                     hiddenField5.setAttribute("type", "hidden");
					                     hiddenField5.setAttribute("name", "headerType");
					                     hiddenField5.setAttribute("value", $scope.selectedHeader);
					                     
					                     
									     form.appendChild(hiddenField1);
									     form.appendChild(hiddenField2);
									     form.appendChild(hiddenField3);
									     form.appendChild(hiddenField4);
									     form.appendChild(hiddenField5);
									     
									     document.body.appendChild(form);
									     $("form[name='Discharge Card']").submit();
									     $("#printHeaderReportModal").hide();
									     }
										else{
											growl.error("discharge summary not generated for selected date",
													{
														title : 'Error!'
													});
											}
										
									}
									
									//past progress note report call (IP)
									$scope.printPastAdmissionReport = function(info) {

										var data = {
											"appName" : "HIS",
											"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
											"reportDesignFilePath" : "NewCoverSheetIPD.rpt",
											"reportFormat" : "PDF",
											"exportFileName" : "CompleteConsultation",
											"reportParams" : [
													{
														"paramName" : "Patientid",
														"paramType" : "Integer",
														"paramValue" :  $scope.encounterDetailsObj.patientId
													},
													{
														"paramName" : "Unitid",
														"paramType" : "Integer",
														"paramValue" : $scope.unitId
													},
													{
														"paramName" : "admission_id",
														"paramType" : "Integer",
														"paramValue" :info.admissionId
													}

											]
										};

										reportService.generate(REPORT_API_URI1, data);
									}
									
									//past encounter report opd
									$scope.printOldEncounter=function(info){
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
												                  "paramValue":  $scope.encounterDetailsObj.patientId
											                },
											                {
												                  "paramName": "Unitid",
												                  "paramType": "Integer",
												                  "paramValue": $scope.unitId
											                },
											                {
													             "paramName": "Encounter_id",
													             "paramType": "Integer",
													             "paramValue": info.encounterId
														      }
								                 ]
								      };
										    reportService.generate(REPORT_API_URI1,data);
										}
								/****************************List Of Documents*******************************/
								$scope.docObj={
										documentTypeId:""
										}
								 $scope.getPatientDocumentList = function()
							        {
							         $rootScope.startLoader();
							         if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
												return;
										}
							         var data =
							           {
							           "patientId":$scope.encounterDetailsObj.patientId,
							           "unitId":$scope.unitId,
							           "organizationId":$scope.organizationId,
							           "userId":$scope.userId,
//							           "visitTypeId":1,
//							           "encounterId":$scope.encounterDetailsObj.encounterId,
							           "documentTypeId":$scope.docObj.documentTypeId,
							           "fromDate":moment($scope.fromDatepicker).format('YYYY-MM-DD'),
							           "toDate":moment($scope.toDatepicker).format('YYYY-MM-DD'),
							          }
							         var URI = BASE_URL + ROOT_URL + "/adt/getListOfDocumentForBookView";
							         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							           $scope.patientDocumentList = [];
							           $scope.patientCorporateDocumentList = [];
							           if (response.data.status == "success") 
							           {
							            $scope.patientDocumentList = response.data.listObject;
							            
							            angular.forEach($scope.patientDocumentList, function(value, key) 
												{  
							            			if(value.documentTypeId==19){				            				
							            			
								            			var data={
								            					documentTypeId:value.documentTypeId,
								            					documentName:value.documentName,
								            					documentPath:value.documentPath,
								            					bucketFlag:value.bucketFlag
								            			}
								            			$scope.patientCorporateDocumentList.push(data);
							            			}
												});
							            console.log("ppppppppppppppppppppp", $scope.patientDocumentList);
							            $rootScope.stopLoader();
							           }
							           else if(response.data.status == "error")
							            {
							            $rootScope.stopLoader();
							            }
							          });
							        }
								 $scope.getPatientDocumentList();
								 
									$scope.openImage = function(index)
		                              { 
										
										$rootScope.startLoader();
										var imageUrl = $scope.patientDocumentList[index].documentPath;
										var href = $rootScope.currentHrefDoccument();
										$.fancybox.open({
											width: 1000, // or whatever
											height: 1000,
											href:href+imageUrl,
											type: 'iframe'
										});
										
										$timeout(function() {
											$rootScope.stopLoader();
										}, 5000);
		                            }
									
										 //----------For past visit-------------//
										$scope.getAllPastEncounteredData = function(info) {
											$scope.dischargeSummary=[];
											$scope.allPastEncounteredData=[];
											 $scope.doctorId=info.doctorId,
											 $scope.patientId=info.patientId,
											 $scope.visitTypeId=info.visitTypeId,
											 $scope.encounterId=info.encounterId,
											 $scope.admissionId=info.admissionId,
											 $scope.specialityId=info.specialityId
											
											if(info.dischargeSummaryId!=null){
											 $scope.dischargeSummary.push(info);
											}
											 console.log("$scope.dischargeSummaryffffffffffff", $scope.dischargeSummary);
//											 angular.forEach( $scope.dischargeSummary, function(value, key) 
//														{  
//														});
											 
											var data = {
													 "organizationId": $scope.organizationId,
													 "unitId":$scope.unitId,
													 "patientId": $scope.patientDetails.patientId,
													 "fromDate": moment($scope.fromDatepicker).format('YYYY-MM-DD'),
													 "toDate": moment($scope.toDatepicker).format('YYYY-MM-DD'),
													 "doctorId":  $scope.doctorId,
													 "specialityId": $scope.specialityId,
													 "createdDate": moment(new Date()).format('DD-MM-YYYY'),
													 "encounterId": $scope.encounterId,
													 "admissionId":$scope.admissionId

												}

											var URI = BASE_URL + ROOT_URL
											+ "/api/global/getAllPastVisitsDataForInfo";
											BillingGenericService
													.serviceAction(METHOD_POST, URI,
															data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																$scope.allPastEncounteredData = response.data.listObject[0];
																
															});

										}
//										$scope.getLastEncounterDetailsForIpd();
										//-----------//
										
										//----Daily progress note report-----//
										/** **************************************** */
										$scope.printPastAdmissionReport = function(info) {

											var data = {
												"appName" : "HIS",
												"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
												"reportDesignFilePath" : "NewCoverSheetIPD.rpt",
												"reportFormat" : "PDF",
												"exportFileName" : "CompleteConsultation",
												"reportParams" : [
														{
															"paramName" : "Patientid",
															"paramType" : "Integer",
															"paramValue" : $scope.patientDetails.patientId
														},
														{
															"paramName" : "Unitid",
															"paramType" : "Integer",
															"paramValue" : $scope.unitId
														},
														{
															"paramName" : "admission_id",
															"paramType" : "Integer",
															"paramValue" :info.admissionId
														}

												]
											};

											reportService.generate(REPORT_API_URI1, data);
										}
										/** **************************************** */
										 $scope.printCertificateReport=function(temp)
					              			{
					              				var tempTransactionId=temp.templateTransactionId;
					              				//$scope.save();
					              				if(tempTransactionId!=null){
					              					var data =  {
					              				            "appName": "HIS",
					              				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					              				            "reportDesignFilePath": "getCertificate.rpt",
					              				            "reportFormat": "PDF",
					              				            "exportFileName": "Certificate",
					              				            "reportParams": [
					              				              {
					              				            	 "paramName": "template_transaction_id",
					 							                  "paramType": "Integer",
					 							                  "paramValue": tempTransactionId
					 							                
					              				              }
					              				            ]
					              				 };
					              					reportService.generate(REPORT_API_URI1,data);
					              				}
					              				else{
					              					growl.error("can not print the report",
					              							{
					              								title : 'Error!'
					              							});
					              					}
					              				
					              		}
					                         
					                         $scope.printCertificateReport=function(temp)
					              			{
					              				var tempTransactionId=temp.templateTransactionId;
					              				//$scope.save();
					              				if(tempTransactionId!=null){
					              					var data =  {
					              				            "appName": "HIS",
					              				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					              				            "reportDesignFilePath": "getCertificate.rpt",
					              				            "reportFormat": "PDF",
					              				            "exportFileName": "Certificate",
					              				            "reportParams": [
					              				              {
					              				            	 "paramName": "template_transaction_id",
					 							                  "paramType": "Integer",
					 							                  "paramValue": tempTransactionId
					 							                
					              				              }
					              				            ]
					              				 };
					              					reportService.generate(REPORT_API_URI1,data);
					              				}
					              				else{
					              					growl.error("can not print the report",
					              							{
					              								title : 'Error!'
					              							});
					              					}
					              				
					              		}
												//added by sidheshwar end
										//------------------------------------//
					                         
					                      // ------------//
												$scope.getInstructions = function() {
													if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
													var patientId =$scope.patientDetails.patientId;
													/*var data = {
														admissionId : $scope.patientDetails.admissionId
													}*/
													var data={
															patientId:$scope.patientDetails.patientId,
															fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
															toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
															
													};
//													if($scope.patientDetails.admissionId!=null){
//														$scope.data.admissionId=$scope.patientDetails.admissionId;
														// var URI = BASE_URL + ROOT_URL +
														// GETINSTRUCTIONS + "?encounterId=" +
														// $scope.encounterDetailsObj.encounterId;
														var URI = BASE_URL + ROOT_URL + "/api/opd/coversheetInstructionListForBookView";

														GenericServiceParamHeader
																.serviceAction(METHOD_POST, URI, data,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				$scope.savedInstructions = response.data.listObject;
																				console
																						.log("savedInstructions="
																								+ $scope.savedInstructions);
																			}
																		});
//													}else if($scope.patientDetails.encounterId!=null){
//														$scope.data.encounterId=$scope.encounterDetailsObj.encounterId;
//														// var URI = BASE_URL + ROOT_URL +
//														// GETINSTRUCTIONS + "?encounterId=" +
//														// $scope.encounterDetailsObj.encounterId;
//														var URI = BASE_URL + ROOT_URL + GETINSTRUCTIONS+S+$scope.encounterDetailsObj.encounterId;
//
//														GenericServiceParamHeader
//																.serviceAction(METHOD_POST, URI, $scope.data,
//																		NOTIFICATION_MSG_STATUS_FALSE)
//																.then(
//																		function(response) {
//																			if (response.data.status == "success") {
//																				$scope.savedInstructions = response.data.listObject;
//																				console
//																						.log("savedInstructions="
//																								+ $scope.savedInstructions);
//																			}
//																		})
//													}
													
													
													/*
													 * GenericServiceParamHeader.serviceAction(METHOD_GET,
													 * URI, NOTIFICATION_MSG_STATUS_TRUE)
													 * .then(function(response) {
													 * if(response.data.status == "success"){
													 * $scope.savedInstructions =
													 * response.data.listObject } })
													 */
												}
												$scope.getInstructions();
												// ---------------//
												
												$scope.consultOrderList = function()
										        {
													if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
														return;
												}
													var data={
															patientId:$scope.patientDetails.patientId,
															fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
															toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
															
													};
													
														
														var URI = BASE_URL + ROOT_URL + "/api/opd/consultDoctorListForBookView";
														
														GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
																data, NOTIFICATION_MSG_STATUS_FALSE)
																	.then(function(response) {
																		if(response.data.status == "success"){
																			$scope.consultOrdersList = response.data.listObject;
																			console.log("consultOrdersListresulttt", $scope.consultOrdersList);
																		}
														});
														
													/*}else if($scope.patientDetails.encounterId!=null){
														$scope.data.encounterId=$scope.encounterDetailsObj.encounterId;
														
														var URI = BASE_URL + ROOT_URL + CONSULTDOCTORLISTBYENCOUNTER;
														
														GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
																$scope.data, NOTIFICATION_MSG_STATUS_FALSE)
																	.then(function(response) {
																		if(response.data.status == "success"){
																			$scope.consultOrdersList = response.data.listObject;
																			console.log("consultOrdersList", $scope.consultOrdersList);
																		}
														});
													}
										        	*/
										        	
										        }
												$scope.consultOrderList();
												// get Admission
												
												 $scope.getOPDAdmissionDetails = function(encounterId, patientId)
											        {
													 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
															return;
													}
											        	$scope.listAdmissionDetails = [];
											        	
											        	var data={
																patientId:$scope.patientDetails.patientId,
																fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
																toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
																
														};
														
															var URI = BASE_URL + ROOT_URL + "/adt/getAdmissionRequestDetailsForBookView";
												    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data)
												    		.then(
												    			function(response) {
												    				if (response.data.status == "success") {
												    					$scope.listAdmissionDetails = response.data.listObject;
												    					console.log("admissionlist",$scope.listAdmissionDetails);

												    				}
												    			});
											    		
											        }
												 $scope.getOPDAdmissionDetails();
												//package transaction view start
													$scope.getAllPackageList=function(){
														
														  $('#commonPackageDetailsPopup').modal('show');
													}
													
													$scope.getPackagesDetails = function(packageId,consumptionMasterId)
													{
														 
														var data={};
														data[consumptionMasterId]=packageId;   
													
													//get package details
														var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package";
														BillingGenericService.serviceAction("POST", URI, data).then(function(response){
															if(response.data.status == "success")
																{
																
																   console.log(response.data.listObject);
																   $scope.packageTypeId = response.data.listObject[0].mPackageMasterDto.packageTypeId;
																   if($scope.packageTypeId==1 || $scope.packageTypeId==5){
																	   $scope.setFirstTabLevel(101);
																   }
																   else if($scope.packageTypeId==2){
																	   $scope.setFirstTabLevel(401);
																   }
																   else if($scope.packageTypeId==3){
																	   $scope.setFirstTabLevel(201);
																   }else if($scope.packageTypeId==4){
																	   $scope.setFirstTabLevel(301);
																   }
																   $scope.packageDetailsResponse=response.data.listObject[0];
																   $('#packageServiceDetailsPopup').modal('show');
															     /*  if($scope.packageDetailsList!=undefined && $scope.packageDetailsList.length>0 && response.data.listObject!=null)
															    	   {
															    	   angular.forEach(response.data.listObject,function(value,key){
															    		   var found = $scope.packageDetailsList.find(ar=>ar.mPackageMasterDto.packageMasterId==value.mPackageMasterDto.packageMasterId);
															    		   if(found==undefined)
																		   {
															    			   $scope.packageDetailsList.push(response.data.listObject.mPackageMasterDto);
																		   }
																		});
															    	   }
															       else{
															    	     $scope.packageDetailsList = response.data.listObject;
															          }*/
																}
																
														});
													}
													
													
													$scope.getListPackageOrder = function() {							
														if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
															return;
													}
														
														var URI = BASE_URL+ ROOT_URL
														+ "/api/packages/getPackageListForIPDPackageOrderForBookView/"+$scope.patientDetails.patientId+"/"+ moment($scope.fromDatepicker).format('YYYY-MM-DD')+"/"
														+moment($scope.toDatepicker).format('YYYY-MM-DD');


														GenericService.serviceAction("GET", URI)
																.then(
																		function(response) {

																			$rootScope.stopLoader();
																			$scope.listPackageOrder = [];
																			$scope.listPackageOrder = response.data.listObject==null?[]:response.data.listObject;
																			console.log("ListPackageOrderresult",
																							$scope.listPackageOrder);

																		});
													}
													$scope.getListPackageOrder();
													
													
													//-----For Procedure Note--//
													$scope.printProcedureNote = function(info) {

														var data = {
															"appName" : "HIS",
															"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
															"reportDesignFilePath" : "get_procedure_req.rpt",
															"reportFormat" : "PDF",
															"exportFileName" : "CompleteConsultation",
															"reportParams" : [
																	{
																		"paramName" : "patient_id",
																		"paramType" : "Integer",
																		"paramValue" :  $scope.patientDetails.patientId
																	}

															]
														};

														reportService.generate(REPORT_API_URI1, data);
													}
													
													
													 $scope.noSignificantVaccination='N';
													$scope.vaccinationSavedList=function(){
														if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
															return;
													}
														var paramObjForVaccine = {
							                    				
							                    				patientId: $scope.patientDetails.patientId,
							                    				organizationId:$scope.organizationId,
												        		unitId : $scope.unitId
							                    				
							                    		}
							                    		
							                    		console.log("QQQQ",paramObjForVaccine);
							                    		 
							                            var URI = BASE_URL + ROOT_URL+ "/api/opd/getListOfVaccineImmunizationMapper";
							                        GenericService.serviceAction("POST", URI, paramObjForVaccine)
							                            .then(function(response){console.log(response);
							                            if (response.data.status == "success")
							                                $scope.vaccineMapperListForCaseSheet=[];
							                                $scope.vaccineMapperList  = response.data.listObject;
							                                angular.forEach($scope.vaccineMapperList, function(value1 , key1) {
							                                	 if(value1.isNoSignificantVaccination == 'N'){
							                                		 $scope.noSignificantVaccination='N';
							                                	 }else{
							                                		 $scope.noSignificantVaccination='Y';
							                                	 }
							                                });
							                                console.log("$scope.vaccineMapperListttt", $scope.vaccineMapperList);
							                            });
													}
													$scope.vaccinationSavedList();
													
													/*******************************Observation Start*****************************************/
													 $scope.getObservationList = function()
												        {
														 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
																return;
														}
														 $scope.observationList = [];
														 var patientId = $scope.encounterDetailsObj.patientId;
														 var data={
												      				organizationId:$scope.organizationId,
												      				unitId:$scope.unitId,
												      				patientId : $scope.patientDetails.patientId,
																	fromDate:moment($scope.fromDatepicker).format('YYYY-MM-DD'),
																	toDate:moment($scope.toDatepicker).format('YYYY-MM-DD'),
												      				encounterId:null,
												      				admissionId:null
												      		};
												    		var URI = BASE_URL + ROOT_URL + "/api/opd/getObservationList";
												    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
												    		.then(
												    			function(response) {
												    				if (response.data.status == "success") {
												    					$scope.observationList = response.data.listObject;
												    					console.log("observationList", $scope.observationList);
												    				}
												    			});
												        }
													 $scope.getObservationList();
													 /***********/
													 /****For template*******/
													 $scope.getComplaintTemplateByIcpcId=function(templateTypeId,obj){

															$rootScope.startLoader();
															var data = {
																orgId : $scope.organizationId,
																unitId : $scope.unitId,
//																doctorId : $stateParams.patientDetailsObj.doctorId == undefined ? 0:$stateParams.patientDetailsObj.doctorId,
//																specialityId : $stateParams.patientDetailsObj.specialityId == undefined ? 0
//																		: $stateParams.patientDetailsObj.specialityId,
																templateTypeId : templateTypeId,
																patientId : $scope.patientDetails.patientId,
//																visitTypeId : $stateParams.patientDetailsObj.visitTypeId,
//																admissionId : $stateParams.patientDetailsObj.admissionId,
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
													 
													 	$scope.getTemplateList = function(templateTypeId) 
												    	{
												    		
												    		$rootScope.startLoader();
												        	var data={
												        		  orgId : $scope.organizationId,
												        		  unitId : $scope.unitId,
												        		  templateTypeId : templateTypeId,
												        		  patientId : $scope.patientDetails.patientId,
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
													 	
													 	
													 	  $scope.getDiagnosisByDiagnosisId=function(templateTypeId,obj){
									                        	 var docId=$scope.encounterDetailsObj.doctorId;
																	$rootScope.startLoader();
																	var data = {
																		orgId : $scope.organizationId,
																		unitId : $scope.unitId,
																		doctorId : $scope.encounterDetailsObj.doctorId,
																		specialityId : $scope.encounterDetailsObj.specialityId == undefined ? 0
																				: $scope.encounterDetailsObj.specialityId,
																		templateTypeId : templateTypeId,
																		patientId : $scope.encounterDetailsObj.patientId,
																		visitTypeId : $scope.encounterDetailsObj.visitTypeId,
																		encounterId : $scope.encounterDetailsObj.encounterId,
																		diagnosisId :obj.diagnosisId
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
																
													 /***********/
													 	 $scope.getMedicationListForMedicalHistory = function()
													        {
													 		if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
																return;
														}
															 $scope.medicationListForMedicalHistoryList = [];
													      	var headerObj = {
																unitId : $scope.unitId,
																organizationId : $scope.organizationId,
																prescriptionDate : moment(new Date()).format('YYYY-MM-DD')
															};
														
															var paramObjForPer = {
																	patientId : $scope.patientDetails.patientId,

																};
															 
													    		var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsForMedicationHistory";
													    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, {},
																		NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
													    		.then(
													    			function(response) {
													    				if (response.data.status == "success") {
//													    					$scope.medicationListForMedicalHistoryList = response.data.listObject;
													    					
													    					$scope.fromDate = moment($scope.fromDatepicker).format('DD/MM/YYYY');
																		    $scope.toDate = moment($scope.toDatepicker).format('DD/MM/YYYY');
																			angular.forEach(response.data.listObject,function(value,key){
																				if($scope.fromDate <= value.createdDate && value.createdDate <=$scope.toDate){
																					$scope.medicationListForMedicalHistoryList.push(value);
																				}
																			});
													    					console.log("medicationListForMedicalHistoryList", $scope.medicationListForMedicalHistoryList)
													    				}
													    			});
													        }
														 $scope.getMedicationListForMedicalHistory();
														 
															//get outSource service  Report 
															$scope.patientOutSourceReport = [];
															 $scope.getPatientOutSourceReportList = function(labSampleDtlsId){
															    $rootScope.startLoader();
															    var data ={
																  "unitId":$scope.unitId,
																  "organisationId":$scope.organizationId,
																  "labSampleDtlsId":labSampleDtlsId
															    }
																var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
																BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																$scope.patientOutSourceReport = [];
																if (response.data.status == "success") 
																{
																	$scope.patientOutSourceReport = response.data.listObject;				            
																        
																    $rootScope.stopLoader();
																}
																else if(response.data.status == "error")
																{
																	$rootScope.stopLoader();
																}
															  });
															}						
																	
															$scope.getOutsourceServiceReport = function(obj){
																$('#outSourceServiceModal').modal('show');
																$scope.getPatientOutSourceReportList(obj.labSampleDtlsId);
															}
															
															
															//for BookView PDF
															
															
															 $scope.opdCashSheetPDF = function(patId,unitId,encounterId){
																	
																 $rootScope.startLoader();	
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
																                  "paramValue": patId
															                },
															                {
																                  "paramName": "Unitid",
																                  "paramType": "Integer",
																                  "paramValue": unitId
															                },
															                {
																	             "paramName": "Encounter_id",
																	             "paramType": "Integer",
																	             "paramValue": encounterId
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
															     form.setAttribute("target", "my_iframe");
															     
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
															 
															 $scope.ipdCashSheetPDF = function(patId,unitId,admissionId){
																	
																 $rootScope.startLoader();
																 var data = {
																			"appName" : "HIS",
																			"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																			"reportDesignFilePath" : "NewCoverSheetIPD.rpt",
																			"reportFormat" : "PDF",
																			"exportFileName" : "CompleteConsultation",
																			"reportParams" : [
																					{
																						"paramName" : "Patientid",
																						"paramType" : "Integer",
																						"paramValue" : patId
																					},
																					{
																						"paramName" : "Unitid",
																						"paramType" : "Integer",
																						"paramValue" : unitId
																					},
																					{
																						"paramName" : "admission_id",
																						"paramType" : "Integer",
																						"paramValue" :admissionId
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
															     form.setAttribute("target", "my_iframe");
															     
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
															 
															 
															 $scope.dischargeSummeryPDF = function(admissionId,dischargeSummeryId){
																 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
																		return;
																}
																	
																 $rootScope.startLoader();
																
															    
																  var URI = BASE_URL + ROOT_URL + "/adt/discharge/dischargeSummaryCard";
																     var form = null;
																     form = document.createElement("form");
																     form.setAttribute("id", "reportId");
																     form.setAttribute("name", "formReportBook");
																     form.setAttribute("method", "post");
																     form.setAttribute("action", URI);
																     form.setAttribute("target", "my_iframe");
																     
																     
																     var hiddenField1 = document.createElement("input"); 
																     hiddenField1.setAttribute("type", "hidden");
																     hiddenField1.setAttribute("name", "admissionId");
																     hiddenField1.setAttribute("value",admissionId);
																     
																     var hiddenField2 = document.createElement("input"); 
																     hiddenField2.setAttribute("type", "hidden");
																     hiddenField2.setAttribute("name", "dischargeSummaryId");
																     hiddenField2.setAttribute("value",dischargeSummeryId);
																     
																     var hiddenField3 = document.createElement("input"); 
																     hiddenField3.setAttribute("type", "hidden");
																     hiddenField3.setAttribute("name", "orgUnitId");
																     hiddenField3.setAttribute("value",$scope.unitId);
																     
																     var hiddenField4 = document.createElement("input"); 
																     hiddenField4.setAttribute("type", "hidden");
																     hiddenField4.setAttribute("name", "orgId");
																     hiddenField4.setAttribute("value", $scope.organizationId);
																     
																     form.appendChild(hiddenField1);
																     form.appendChild(hiddenField2);
																     form.appendChild(hiddenField3);
																     form.appendChild(hiddenField4);
																     
																     document.body.appendChild(form);
																     $("form[name='formReportBook']").submit();
																     
															     $rootScope.stopLoader();
															}
															
															 $scope.patientReportListByType = [];
															 $scope.activeIndexForBookView = 0;
															 $scope.getPatientDataForBookView = function(){
																 if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
																		return;
																}
															    $rootScope.startLoader();
															    
															    var data ={
																  "patientId":$scope.patientDetails.patientId,
															    }
																var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientDataForBookView";
															    
																BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																$scope.patientReportListByType = [];
																if (response.data.status == "success") 
																{
																	var i = 0;
																	angular.forEach(response.data.listObject,function(value,key){
																		//typeId 1 for OPD
																		//typeId 2 for IPD
																		//typeid 3 for dischage Summery
																		
																		if(i == 0){
																			
																			$scope.activeIndexForBookView = i;
																			$scope.patientReportListByType.push(value);
																			if(value.typeId==1){
																				$('#myIframe').removeAttr('src');
																				$scope.opdCashSheetPDF($scope.patientDetails.patientId,value.unitId,value.visitId);
																				
																			}else if(value.typeId==2){
																				$('#myIframe').removeAttr('src');
																				$scope.ipdCashSheetPDF($scope.patientDetails.patientId,value.unitId,value.visitId);
																				
																			}else if(value.typeId==3){
																				$('#myIframe').removeAttr('src');
																				$scope.dischargeSummeryPDF(value.visitId,value.dischargeSummeryId);
																			}
																			else if(value.typeId==4){
																				
																				$('#myIframe').attr('src', value.documentPath);
																				
																				
																			}
																			
																		}else{
																			$scope.patientReportListByType.push(value);
																		}
																		i++;
																		
																	});
																	 
																        
																    $rootScope.stopLoader();
																}
																else if(response.data.status == "error")
																{
																	$rootScope.stopLoader();
																}
															  });
															}	
															 $scope.getPatientDataForBookView();
															 
															 $scope.prevoiusReport = function(){
																 
																// $scope.patientReportListByType
																 if($scope.activeIndexForBookView == 0){
																	 var objTemp = $scope.patientReportListByType[$scope.patientReportListByType.length - 1];
																	 $scope.activeIndexForBookView = $scope.patientReportListByType.length;
																	 if(objTemp.typeId==1){
																		 $('#myIframe').removeAttr('src');
																			$scope.opdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==2){
																			$('#myIframe').removeAttr('src');
																			$scope.ipdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==3){
																			$('#myIframe').removeAttr('src');
																			$scope.dischargeSummeryPDF(objTemp.visitId,objTemp.dischargeSummeryId);
																		}else if(objTemp.typeId==4){
																			$('#myIframe').attr('src',objTemp.documentPath);
																		}
																	 
																 }
																 else if($scope.patientReportListByType.length >= $scope.activeIndexForBookView){
																	 
																	 var objTemp = $scope.patientReportListByType[$scope.activeIndexForBookView - 1];
																	 $scope.activeIndexForBookView = $scope.activeIndexForBookView - 1;
																	 if(objTemp.typeId==1){
																		 $('#myIframe').removeAttr('src');
																			$scope.opdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==2){
																			$('#myIframe').removeAttr('src');
																			$scope.ipdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==3){
																			$('#myIframe').removeAttr('src');
																			$scope.dischargeSummeryPDF(objTemp.visitId,objTemp.dischargeSummeryId);
																		}
																		else if(objTemp.typeId==4){
																			$('#myIframe').attr('src',objTemp.documentPath);
																		}
																 }
																 //if($scope.patientReportListByType.length)
															 }
															 
															 $scope.nextReport = function(){
																 
																 if($scope.activeIndexForBookView == $scope.patientReportListByType.length){
																	 var objTemp = $scope.patientReportListByType[0];
																	 $scope.activeIndexForBookView = 0;
																	 if(objTemp.typeId==1){
																		 $('#myIframe').removeAttr('src');
																			$scope.opdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==2){
																			$('#myIframe').removeAttr('src');
																			$scope.ipdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==3){
																			$('#myIframe').removeAttr('src');
																			$scope.dischargeSummeryPDF(objTemp.visitId,objTemp.dischargeSummeryId);
																		}
																		else if(objTemp.typeId==4){
																			$('#myIframe').attr('src',objTemp.documentPath);
																		}
																	 
																 }
																 
																 else if($scope.activeIndexForBookView == 0){
																	 
																	 var objTemp = $scope.patientReportListByType[$scope.activeIndexForBookView + 1];
																	 $scope.activeIndexForBookView = $scope.activeIndexForBookView + 1;
																	 if(objTemp.typeId==1){
																		 $('#myIframe').removeAttr('src');
																			$scope.opdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==2){
																			$('#myIframe').removeAttr('src');
																			$scope.ipdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==3){
																			$('#myIframe').removeAttr('src');
																			$scope.dischargeSummeryPDF(objTemp.visitId,objTemp.dischargeSummeryId);
																		}
																		else if(objTemp.typeId==4){
																			$('#myIframe').attr('src',objTemp.documentPath);
																		}
																 }
																 else if($scope.patientReportListByType.length > $scope.activeIndexForBookView){
																	 var objTemp = $scope.patientReportListByType[$scope.activeIndexForBookView + 1];
																	 $scope.activeIndexForBookView = $scope.activeIndexForBookView + 1;
																	 if(objTemp.typeId==1){
																		 $('#myIframe').removeAttr('src');
																			$scope.opdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==2){
																			$('#myIframe').removeAttr('src');
																			$scope.ipdCashSheetPDF($scope.patientDetails.patientId,objTemp.unitId,objTemp.visitId);
																			
																		}else if(objTemp.typeId==3){
																			$('#myIframe').removeAttr('src');
																			$scope.dischargeSummeryPDF(objTemp.visitId,objTemp.dischargeSummeryId);
																		}
																		else if(objTemp.typeId==4){
																			$('#myIframe').attr('src',objTemp.documentPath);
																		}
																 }
																 
																 
															 }
															
															 
															//End BookView PDF
															$scope.openOutSourceReportImage = function(index)
									                        { 
																$rootScope.startLoader();
																var imageUrl = $scope.patientOutSourceReport[index].documentPath;
																var href = $rootScope.currentHrefDoccument();
																$.fancybox.open({
																	width: 600, // or whatever
																	height: 320,
																	href:href+imageUrl,
																	type: 'iframe'
																});

																$timeout(function() {
																	$rootScope.stopLoader();
																}, 5000);
									                        } 
															
															$scope.testFunction = function(){
																
																
																  var data =  {
															                 "appName": "HIS",
															                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
															                 "reportDesignFilePath": "InvestigationSlip.rpt",
															                 "reportFormat": "PDF",
															                 "exportFileName": "InvestigationSlip",
															                 "reportParams": [
															                	 {
																	                  "paramName": "@Encounter_id",
																	                  "paramType": "Integer",
																	                  "paramValue": 1726
																                },
																                {
																	                  "paramName": "@UserID",
																	                  "paramType": "Integer",
																	                  "paramValue": 2
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
															     form.setAttribute("target", "my_iframe");
															     
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
															     
															}											
										$scope.hideModel=function(){
											$("#printHeaderReportModal").hide();
										}
						 }]);
						