'use strict';

/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:dayCarePatientForDischargeController
 * @description # dayCarePatientForDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'dayCarePatientForDischargeController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'BillingGenericService',
						'reportService',
						'GenericServiceParamHeader',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,BillingGenericService,reportService,GenericServiceParamHeader) {

							/* init() function for form object create. */
							
							$scope.visitTypeIdflag = "3"; 
							$scope.dropdown = 'Y';
							
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId =cookieObject.userId;
								
								
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig'); 
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								 
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								
								$scope.wardList = [];
								$scope.dischargeMedicationlist=[];
								var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
								 if(nursingStationsWardsList == undefined){
									 growl.warning(
												'No ward mapped with selected nursing station !!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   console.log('nursingStationsWardsList',nursingStationsWardsList);
								   $scope.wardList = nursingStationsWardsList; 
								   /*console.log('nursingStationCookie',nursingStationCookie);
								   $scope.nursingStationDesc=nursingStationCookie;
								   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
									console.log("departmentNursingStationMapperDtoslist",$scope.departmentNursingStationMapperDtoslist);*/
								  }
								
								 $scope.nurseBedList = [];
									var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
									 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y' ){
										 growl.warning(
													'No bed mapped with nurse !!',
													{
														title : 'Success!'
													});
									 }
									 else
									  {
									   $scope.nurseBedList =mappedNurseBedList; 
									  console.log('Nurse Bed List',$scope.nurseBedList);
									  }
								 

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								//$rootScope.billingFlag = false;
								/*angular.element(document.getElementById('billing'))[0].disabled = true;
								angular.element(document.getElementById('damaContent'))[0].disabled = true;
								angular.element(document.getElementById('vacateBed'))[0].disabled = true;
								angular.element(document.getElementById('deathId'))[0].disabled = true;
								angular.element(document.getElementById('dischargeSlip'))[0].disabled = true;
								angular.element(document.getElementById('dischargeSummary'))[0].disabled = true;
								angular.element(document.getElementById('dischargeMedication'))[0].disabled = true;*/
								$scope.pendingServicePopupFlag =false;

								$scope.dischargePatient = {};
								$scope.bedTr={};
								$scope.itemSelecteds = {};
								$scope.visitorPassPopUpFlag = false;
								$scope.deathPdfViewPopupFlag = false;
								$scope.damaPdfViewPopupFlag = false;
								$scope.dischargePdfViewPopupFlag = false;
								$scope.dischargeSlipPdfViewPopupFlag   = false;
								
								
								$scope.templateObj = {};

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								
								$scope.cD = new Date();
								$scope.cD = moment($scope.cD)
										.format('DD-MM-YYYY');
								
								var commonObjForDischargeList = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										dischargeDate : $scope.cD,
										wardList :$scope.wardList
									};
								
								if ($scope.isShowPatientByBed == 'Y') {
									if($scope.nurseBedList.length>0){
										commonObjForDischargeList.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								
								//patient redirected from bed management
								if ($scope.bedManagmentCookiesObj !=null) 
								{
									commonObjForDischargeList.admissionId=$scope.bedManagmentCookiesObj.admissionId;
								}
								//end

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});

								var URI = BASE_URL + ROOT_URL + GETACTIVEVISITORPASSTYPELIST ;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.visitorPassTypeList = [];

													if (response.data.status == "success") {
														$scope.visitorPassTypeList = response.data.listObject;

													}
												});
								if($scope.nurseRole == 1 && $scope.wardList.length > 0){
								var URI = BASE_URL + ROOT_URL
										+ GETDISCHARGEPATIENTLISTFORNURSING;
								GenericService
										.serviceAction("POST", URI, commonObjForDischargeList)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
														$scope.filterDate = new Date();
														$scope.filterDate = moment($scope.filterDate)
																.format('DD/MM/YYYY');
														$scope.admittedPatientList = response.data.listObject;
														angular
																.forEach(
																		$scope.admittedPatientList,
																		function(
																				value,
																				index) {

																			var newArr = {};
																			newArr.specialityName = value.specialityName;
																			newArr.dFirstName = value.dFirstName;
																			newArr.pFirstName = value.pFirstName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.dob = value.dob;
																			newArr.datepicker = value.doa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.dischargeId = value.dischargeId;
																			newArr.futureDischargeDate = value.futureDischargeDate;
																			newArr.dischargeStatusId = value.dischargeStatusId;
																			newArr.dischargeTypeId = value.dischargeTypeId;
																			newArr.dischargeTypeName = value.dischargeTypeName;
																			newArr.dischargeDesc = value.dischargeDesc;
																			newArr.doa = value.doa;
																			newArr.pdd = value.pdd;
																			newArr.age = value.ageString;
																			newArr.wardName = value.wardName;
																			newArr.bedId = value.bedId;
																			newArr.dischargeSummaryId = value.dischargeSummaryId;
																			newArr.genderId=value.genderId;
																			newArr.isFinancialClearance = value.isFinancialClearance;
																			
																			 if($scope.visitTypeIdflag == "2" ){
																					if(value.visitTypeId==2){
																						 $scope.listOfAdmittedPatientList = [];
																					}
		                                                                          }
		                                                                            //day care
                                                                         if($scope.visitTypeIdflag == "3" ){
 																			if(value.visitTypeId==3){
 																				 $scope.listOfAdmittedPatientList.push(newArr);
 																			}
                                                                             }
                                                                         
																			console.log("dayacaredata",$scope.listOfAdmittedPatientList);

																		});

													}
												});
							}

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEFLOORLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.activeFloorList = [];

													if (response.data.status == "success") {
														$scope.activeFloorList = response.data.listObject;

													}
												});

							}

							$scope.init();
							
							// function for popup ok button call
							/*$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.cancelPendingService();
							}*/
							
							
							$scope.passIssuedList = [];
							$scope.passDetails = function() {
								console.log($scope.itemSelecteds);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								}
								else{
								
								if($scope.itemSelecteds.patientId > 0)
								{
								var age = $rootScope
								.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName =$scope.itemSelecteds.pFirstName
								+ " "
								+age
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
							else
								{
								$scope.patientFullName = $scope.itemSelecteds.pFirstName
								+ " "
								+ $scope.itemSelecteds.dob
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName =$scope.itemSelecteds.dFirstName;
								$scope.bedNumber = $scope.itemSelecteds.bedNumber;
								$scope.dateOfAdmission =$scope.itemSelecteds.datepicker;
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.identificationNo = $scope.itemSelecteds.identificationNo;
								$scope.admissionId = $scope.itemSelecteds.admissionId;

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									admissionId : $scope.itemSelecteds.admissionId
								}

								var URI = BASE_URL + ROOT_URL
										+ GETPASSISSUEDLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.passIssuedList = response.data.listObject;

													}
												});
							}
							}
							
							$scope.pendingServies = function()
							{
								
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								}
								else{
									
									$rootScope.startLoader();
								console.log($scope.itemSelecteds);
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										admissionId : $scope.itemSelecteds.admissionId,
										//patientId : $scope.itemSelecteds.patientId
								};
								var myEl3 = angular.element( document.querySelector( '#movingBed' ) );
				            	myEl3.removeClass('disabled');
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
								+ GETPATIENTPENDINGSERVICESLIST;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											

											if (response.data.status == "success") {

												$rootScope.stopLoader();
												$scope.pendingServicesList = [];
												$scope.pendingServicesList = response.data.listObject;
												//added for remove disabled only when patient is selected start
												var myEl3 = angular.element( document.querySelector( '#pendingServies' ) );
								            	myEl3.removeClass('disabled');
								            	var myEl3 = angular.element( document.querySelector( '#medicationHandOver' ) );
								            	myEl3.removeClass('disabled');
								            	/*var myEl3 = angular.element( document.querySelector( '#visitorsPass' ) );
								            	myEl3.removeClass('disabled');*/
								            	//added for remove disabled only when patient is selected end
								            	
												if($scope.pendingServicesList.length == 0)
													{
													/*$rootScope.billingFlag = false;
													alert($rootScope.billingFlag);*/
													//angular.element(document.getElementById('billing'))[0].disabled = false;
													var myEl = angular.element( document.querySelector( '#billing' ) );
									            	myEl.removeClass('disabled');
									            /*	var myEl1 = angular.element( document.querySelector( '#dischargeMedication' ) );
									            	myEl1.removeClass('disabled');*/
									            	
									            	var myEl3 = angular.element( document.querySelector( '#dischargeSlip' ) );
									            	myEl3.removeClass('disabled');
									            	var myEl4 = angular.element( document.querySelector( '#pendingServies' ) );
									            	myEl4.addClass('disabled');
													}
												else
													{
													/*$rootScope.billingFlag = true;
													alert($rootScope.billingFlag);*/
													//angular.element(document.getElementById('billing'))[0].disabled = true;
													var myEl = angular.element( document.querySelector( '#billing' ) );
									            	myEl.addClass('disabled');
													}
												
												
												  switch ($scope.itemSelecteds.dischargeTypeId) {
										            case 1:
										               // alert("Selected Case Number is 1");
										                break;
										            case 2:
										               ///alert("Selected Case Number is 2");
										            	var myEl = angular.element( document.querySelector( '#damaContent' ) );
										            	myEl.removeClass('disabled');
										                break;
										            case 3:
										               // alert("Selected Case Number is 2");
										            	//angular.element(document.querySelector('#deathId')).removeClass = "disabled";
										            	var myEl = angular.element( document.querySelector( '#deathId' ) );
										            	myEl.removeClass('disabled');
										                break;
										            case 4:
										                //alert("Selected Case Number is 2");
										                break;
										            default:
										            	
										        }
												  
												  if($scope.itemSelecteds.dischargeStatusId==3 && $scope.itemSelecteds.isFinancialClearance == 'Y')
												  {
												  	var myEl = angular.element( document.querySelector( '#vacateBed' ) );
									            	myEl.removeClass('disabled');
									            	var myEl1 = angular.element( document.querySelector( '#movingBed' ) );
									            	myEl1.removeClass('disabled');
												  }else{
													  var myEl = angular.element( document.querySelector( '#movingBed' ) );
										            	myEl.addClass('disabled');
										            	
										            	var myE2 = angular.element( document.querySelector( '#vacateBed' ) );
										            	myE2.addClass('disabled');
												  }

											}
											else{
												 var myEl = angular.element( document.querySelector( '#movingBed' ) );
									            	myEl.addClass('disabled');
											}
										});
							}
									
							}
							
							$scope.selectPatient = function(item) {
								$scope.patientDetailsObj={};
								$scope.dischargeSummaryId=null;
								$scope.itemSelecteds = [];
								$scope.patientDetails = {};
								console.log("hh",item);
								$scope.patientDetails.uhidNumber = item.uhidnumber;
								$scope.patientDetails.patientName = item.pFirstName;
								$scope.patientDetails.dob = item.dob;
								$scope.patientDetails.doctorFullName = item.dFirstName;
								$scope.patientDetails.dateOfAdmission = item.doa;
								$scope.patientDetails.patientDischargeDate = item.pdd;
								$scope.patientDetails.wardName = item.wardName;
								$scope.patientDetails.bedNumber = item.bedNumber;
								$scope.patientDetails.age = item.age;
								$scope.patientDetails.dischargeId = item.dischargeId;
								$scope.dischargeSummaryId = item.dischargeSummaryId;
								$scope.patientDetails.admissionId=item.admissionId;
								$scope.patientDetails.bedId=item.bedId;
								
								
								// for re direct required data
								$scope.patientDetailsObj.age = item.age;
								$scope.patientDetailsObj.patientName = item.pFirstName;
								$scope.patientDetailsObj.genderCode = item.genderCode;
								$scope.patientDetailsObj.uhidNumber = item.uhidnumber;
								$scope.patientDetailsObj.patientId = item.patientId;
								$scope.patientDetailsObj.dob = item.dob;
								$scope.patientDetailsObj.admissionId = item.admissionId;
								$scope.patientDetailsObj.genderId = item.genderId;
								$scope.patientDetailsObj.visitTypeId = 3;
								
								$scope.getAllPatientDiagnosis($scope.patientDetailsObj.patientId);
								$rootScope.allergydata($scope.patientDetailsObj);
						
								
								
								// If checkbox is checked
								if ($scope.dischargePatient) {
									$scope.itemSelecteds = item;
									$scope.pendingServicePopupFlag =true;
									$scope.deathPdfViewPopupFlag = true;
									$scope.damaPdfViewPopupFlag = true;
									$scope.dischargePdfViewPopupFlag = true;
									$scope.dischargeSlipPdfViewPopupFlag   = true;
									$scope.pendingServies();

								}
								
								$scope.passIssuedList = [];
								$scope.dischargeMedicationlist=[];
								$scope.passDetails();
								$scope.dischargeMedication();
							}
							
							
							$scope.getAllPatientDiagnosis=function(patientId){
								 var headerObjDia = {
										 	'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : patientId,
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
							
							$scope.finalDischarge = function()
							{
								$state.go('dayCareFinalDischarge');
							}
							
							
							$scope.commonFuntion = function(templateTypeId) {
								console.log($scope.itemSelecteds);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								}
								else{
								
								if($scope.itemSelecteds.patientId > 0)
								{
								var age = $rootScope
								.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName =$scope.itemSelecteds.pFirstName
								+ " "
								+age
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
							else
								{
								$scope.patientFullName = $scope.itemSelecteds.pFirstName
								+ " "
								+ $scope.itemSelecteds.dob
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName =$scope.itemSelecteds.dFirstName;
								$scope.bedNumber = $scope.itemSelecteds.bedNumber;
								$scope.dateOfAdmission =$scope.itemSelecteds.datepicker;
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.identificationNo = $scope.itemSelecteds.identificationNo;
								$scope.admissionId = $scope.itemSelecteds.admissionId;
								
								var templeteData  = {	
								};
								
								var URI = BASE_URL + ROOT_URL+LIS_UNIT
								+ "/labTemplatesbydoctorForDischargeScreen"+"/"+$scope.organizationId+"/"+$scope.unitId+"/0/"+templateTypeId;
								GenericService
								.serviceAction("GET", URI, templeteData)
								.then(
										function(response) {
											$scope.templateList = [];

											if (response.data.status == "success") {
												$scope.templateList = response.data.listObject;
												//$scope.value =$scope.templateList[0].html;
												
												$scope.templateObj = {
														templetText:$scope.templateList[0].html,
														templeId  : $scope.templateList[0].labTemplateId,
														organizationId : $scope.organizationId,
														unitId : $scope.unitId,
														admissionId : $scope.admissionId,
														typeId  : templateTypeId
												}
												
												console.log("Templete For Dama",$scope.templateList);
												
												/*CKEDITOR.addTemplates("default", {
												    imagesPath:CKEDITOR.getUrl(CKEDITOR.plugins.getPath("templates")+"templates/images/"),
												    templates: $scope.templateList
												   });*/
												
											}
										});


							}
							}
							
							$scope.saveTemplate = function()
							{
								console.log('Final Object Template',$scope.templateObj);
								
								 var popupWinindow = window.open('', '_blank', 'width=600,height=700,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
							        popupWinindow.document.open();
							        popupWinindow.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">'+$scope.templateObj.templetText+'</html>');
							        popupWinindow.document.close();
							}
							
							$scope.redirectBilling = function()
							{
								$state.go('IPDCharges.serviceDetailsForIPDCharge',
								{
									patientDetailsObj :$scope.patientDetailsObj
								});
							}
							
							$scope.redirectMedicationhandOver = function()
							{
								$state.go('dischargeMedicationHandover',
								{
									patientDetailsObj :$scope.patientDetailsObj
								});
							}
							
							/*$scope.visitorPass = function() {

								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								} else {

									var age = $rootScope
											.getAge($scope.itemSelecteds.dob);
									$scope.patientFullName = $scope.itemSelecteds.pFirstName
											+ " "
											+ $scope.itemSelecteds.pMiddleName
											+ " "
											+ $scope.itemSelecteds.pLastName
											+ " "
											+ age
											+ "/"
											+ $scope.itemSelecteds.genderCode;
									$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
									$scope.doctorFullName = 'Dr. '
											+ $scope.itemSelecteds.dFirstName
											+ " "
											+ $scope.itemSelecteds.dMiddleName
											+ " "
											+ $scope.itemSelecteds.dLastName;
									$scope.bedNumber = $scope.itemSelecteds.bedNumber;
									$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
									$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
									$scope
											.getPassIssuedList($scope.itemSelecteds.admissionId);

								}

							}*/

							$scope.cancelPendingService = function(reasonId,item) {
						    		
								
									if(angular.isUndefined(reasonId))
										{
										growl
										.warning(
												'Please select reason...!!',
												{
													title : 'Warning!'
												});
										}
									else
										{
										
										$scope.popuCancelReasonId = reasonId;
										$scope.itemForCancelPopup = item; 
										
										 angular.element('#cancelConfirmPopup').modal('show');
										 
										/*
										$rootScope.startLoader();
								   var data = {};
								   var cancelList =[];
								   var orderedServicesDtosList = {};
								   orderedServicesDtosList.orderDetailsId = item.orderDetailsId;
								   orderedServicesDtosList.ordCancelReasonId = reasonId;
								   orderedServicesDtosList.ordCancelDatetime = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								   orderedServicesDtosList.updatedBy = $scope.userId;
								   cancelList.push(orderedServicesDtosList);
								   data.orderedServicesDtosList = cancelList;
						    		var URI = BASE_URL + ROOT_URL
									+ "/api/billing/cancelUnRenderedServices";
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.pendingServies();
													
													if(item.contractId > 0)
														{
														
															var data = {
																	admissionId :$scope.itemSelecteds.admissionId,
																	subSpecialityId : item.serviceSubSpecialityId,
																	contractId : item.contractId,
																	creaditPayable : item.creditPayable
															}
															console.log('AddCapAmt',data);
															//return false;
															$rootScope.startLoader();
															var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
															BillingGenericService
																	.serviceAction(METHOD_POST,
																			URI, data,
																			NOTIFICATION_MSG_STATUS_FALSE)
																	.then(
																			function(response) {
																				if (response.data.status == "success")
																				{
																					growl
																					.success(
																							'Cancelled Successfully!!',
																							{
																								title : 'Success!'
																							});
																					$rootScope.stopLoader();
																				}
																			});
														}
													
												}
												else
													{
													$rootScope.stopLoader();
													}
											});*/
										}
								
							}
							
							
							$scope.finalCancelService = function()
							{
								 
								   $rootScope.startLoader();
								   var data = {};
								   var cancelList =[];
								   var orderedServicesDtosList = {};
														
									var data = 
									{
										admissionId :$scope.itemSelecteds.admissionId,
										subSpecialityId : $scope.itemForCancelPopup.serviceSubSpecialityId,
										specialityId : $scope.itemForCancelPopup.serviceSpecialityId,
                                        serviceId : $scope.itemForCancelPopup.serviceMasterId,
										contractId : $scope.itemForCancelPopup.contractId,
										orderDetailsId:$scope.itemForCancelPopup.orderDetailsId,
										creaditPayable : $scope.itemForCancelPopup.creditPayable,
										ordCancelReasonId : $scope.popuCancelReasonId,
										createdBy : $scope.userId,
										orderDetailsPayeeMapperDtosList : $scope.itemForCancelPopup.orderDetailsPayeeMapperDtosList,
					    				orderDetailsPackageMapperDtosList : $scope.itemForCancelPopup.orderDetailsPackageMapperDtosList
									}
									console.log('AddCapAmt',data);
									//return false;
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success")
														{
															$scope.pendingServies();
															growl
															.success(
																	'Cancelled Successfully!!',
																	{
																		title : 'Success!'
																	});
															$rootScope.stopLoader();
														}
													});
								}
													
							
							$scope.getActiveMovingBed=function(){
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
								}
								//return false;
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +  "/api/adt/activeMovingBedList";
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													if (response.data.status == "success")
													{
														$scope.activeMovingBedList = response.data.listObject;
														console.log("activeMoving =",$scope.activeMovingBedList);
														$rootScope.stopLoader();
													}
												});
							}
							$scope.movingBed=function(){
								$scope.objMovingBed=$scope.patientDetails;
								console.log($scope.objMovingBed);
								
								$scope.getActiveMovingBed();
							}
							
							$scope.selectMovingItemBed=function(item){
								$scope.selectedMovingBedObjForSelect=item;
								console.log("selected Moving Bed= ",$scope.selectedMovingBedObjForSelect);
							}
							
							console.log("selected Moving Bed= ",$scope.selectedMovingBedObjForSelect);
							$scope.selectedMovingBedObject=function(){
								if ($scope.selectedMovingBedObjForSelect != undefined || $scope.selectedMovingBedObjForSelect != null ) {
									
									
										var data = {
												fromBedId    :$scope.objMovingBed.bedId,
												toBedId     :$scope.selectedMovingBedObjForSelect.bedId,
												admissionId :$scope.objMovingBed.admissionId,
												dischargeId :$scope.objMovingBed.dischargeId,
//												patientId   :$scope.objMovingBed.patientId,
												organizationId : $scope.organizationId,
												unitId         : $scope.unitId,
												createdDate    :moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
												createdBy      : $scope.userId,
												updatedDate    :moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
												updatedBy      : $scope.userId
										}
										console.log("dadd  dssd=",data);
//										return false;
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL +  "/adt/saveMovingBed";
										BillingGenericService
												.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
															if (response.data.status == "success")
															{
																console.log("moved succesfully");
																growl.success('Bed moved succesfully...!!',
																		{
																			title : 'success!'
																		});
																$scope.selectedMovingBedObjForSelect = {};
																$scope.getActiveMovingBed();
																//for closing pop up
																$('#movingBedTransfer').hide('hide');
																$(".modal-backdrop").hide();
																$scope.init();
//																$scope.activeMovingBedList = response.data.listObject;
//																console.log("activeMoving =",$scope.activeMovingBedList);
//																$rootScope.stopLoader();
															}
														});
										
										$rootScope.stopLoader();
								}
							
								else{
									growl.error('Please select atleast one bed...!!',
											{
												title : 'Error!'
											});
							
								}
							}
							
							/***************Discharge Summary Report***********************/
							$scope.printDischargeSumReport=function()
							{
								if($scope.dischargeSummaryId!=null){ 
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
							     hiddenField1.setAttribute("value",$scope.patientDetails.admissionId);
							     
							     var hiddenField2 = document.createElement("input"); 
							     hiddenField2.setAttribute("type", "hidden");
							     hiddenField2.setAttribute("name", "dischargeSummaryId");
							     hiddenField2.setAttribute("value",$scope.dischargeSummaryId);
							     
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
							     $("form[name='Discharge Card']").submit();
							     }
								else{
									growl.error("Discharge summary report not generated",
											{
												title : 'Error!'
											});
									}
								
						}

							/*************Discharge Summary Report END*************************/	

						/*	$scope.getPassIssuedList = function(admissionId) {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									admissionId : admissionId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETPASSISSUEDLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.passIssuedList = [];

													if (response.data.status == "success") {
														$scope.passIssuedList = response.data.listObject;

													}
												});
							}*/

							
							/****Get discharge medication****************/
							$scope.showDischargeMedication = function(){
							if($scope.dischargeMedicationlist.length > 0 ){
								$('#dischargeMedicationDetails').modal('show');
								$scope.dischargeMedication();
							}
							}
							$scope.dischargeMedication = function(){
							var medicationobj = {
									//patientId:$scope.patientId,
									admissionId:$scope.patientDetails.admissionId,
									unitId:$scope.unitId,
									organizationId:$scope.organizationId,
									dischargeMedicationFlag:true
							};
							$scope.dischargeMedicationlist=[];
							
							var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getDischargeMedication";
							BillingGenericService
									.serviceAction("POST",URI,medicationobj)
									.then(
											function(response) {
												if (response.data.status == "success") {
													
													$scope.dischargeMedicationlist = response.data.listObject;
												}
											});
							}
							
							
						} ]);
