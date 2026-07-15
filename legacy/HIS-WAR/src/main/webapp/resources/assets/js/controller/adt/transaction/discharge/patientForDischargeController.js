'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:patientForDischargeController
 * @description # patientForDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'patientForDischargeController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'GenericServiceParamHeader',
						'growl',
						'$state',
						'$stateParams',
						'$timeout',
						'BillingGenericService',
						'PagerService',
						'reportService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, GenericServiceParamHeader,growl,
								$state,$stateParams,$timeout,BillingGenericService,PagerService,reportService,$window) {

							/* init() function for form object create. */
							$scope.visitTypeIdflag = "2";
							$scope.dropdown = 'N' ;
							
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId =cookieObject.userId;
								
								$scope.searchKeyword='';
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								$scope.movingBedMsg = "Please wait..";
								 
								
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
								var mappedNurseBedList = JSON.parse(sessionStorage
										.getItem('mappedNurseBedList'));
								if ( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y') {
									growl.warning(
											'No bed mapped with nurse !!', {
												title : 'Success!'
											});
								} else {
									$scope.nurseBedList = mappedNurseBedList;
									console.log('Nurse Bed List',
											$scope.nurseBedList);
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
										wardList :$scope.wardList,
										visitTypeId:$scope.visitTypeIdflag
									};
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForDischargeList.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
									//$scope.visitTypeIdflag = $scope.bedManagmentCookiesObj.visitTypeId;
								}
								
								//patient redirected from bed management
								if ($scope.bedManagmentCookiesObj != null) 
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
							/*	if($scope.nurseRole == 1 && $scope.wardList.length > 0){
								var URI = BASE_URL + ROOT_URL
										+ GETDISCHARGEPATIENTLISTFORNURSING;
//								        +"/adt/getDischargePatientListForNursingByVisitTypeId"
								GenericService
										.serviceAction("POST", URI, commonObjForDischargeList)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];
													$rootScope.startLoader();
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
																			newArr.imagePath = value.imagePath;
																			newArr.prefixDesc = value.prefixDesc;
//																			if(value.visitTypeId==2){
																				newArr.visitTypeId=value.visitTypeId;
//																			}
																			$scope.listOfAdmittedPatientList
																					.push(newArr);
																			
																			 if($scope.visitTypeIdflag == "2" ){
																					if(value.visitTypeId==2){
																						$scope.listOfAdmittedPatientList.push(newArr);
																					}
		                                                                          }
		                                                                            //day care
                                                                            if($scope.visitTypeIdflag == "3" ){
    																			if(value.visitTypeId==3){
    																				$scope.listOfAdmittedPatientList.push(newArr);
    																			}
                                                                                }
		                                                                            

																		});
														$timeout(function () {
														$rootScope.stopLoader();
														 }, 1000);
															console.log("list data",$scope.listOfAdmittedPatientList);
													}else{
														$rootScope.stopLoader();
													}
												});
							}*/

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
							
							
							//For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.getDischargePatientList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getDischargePatientList = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
							
								
								var commonObjForDischargeList = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										dischargeDate : $scope.cD,
										wardList :$scope.wardList,
										visitTypeId:$scope.visitTypeIdflag
									};
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForDischargeList.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
									//$scope.visitTypeIdflag = $scope.bedManagmentCookiesObj.visitTypeId;
								}
								
								//patient redirected from bed management
								if ($scope.bedManagmentCookiesObj != null) 
								{
									commonObjForDischargeList.admissionId=$scope.bedManagmentCookiesObj.admissionId;
								}
								
									commonObjForDischargeList.offset = offset;
									commonObjForDischargeList.noOfRecordsPerPage = noOfRecordsPerPage;
								
								if($scope.nurseRole == 1 && $scope.wardList.length > 0){
									var URI = BASE_URL + ROOT_URL
//											+ GETDISCHARGEPATIENTLISTFORNURSING;
									        +"/adt/getDischargePatientListForNursingByVisitTypeId"
									GenericService
											.serviceAction("POST", URI, commonObjForDischargeList)
											.then(
													function(response) {
														$scope.listOfAdmittedPatientList = [];
														$scope.admittedPatientList = [];
														$rootScope.startLoader();
														if (response.data.status == "success") {
															$scope.filterDate = new Date();
															$scope.filterDate = moment($scope.filterDate)
																	.format('DD/MM/YYYY');
															$scope.admittedPatientList = response.data.listObject;
															angular.forEach( $scope.admittedPatientList, function( value, index) {

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
																				newArr.imagePath = value.imagePath;
																				newArr.prefixDesc = value.prefixDesc;
																				newArr.companyName = value.companyName;
																				newArr.paymentEntitlementId = value.paymentEntitlementId;
//																				if(value.visitTypeId==2){
																					newArr.visitTypeId=value.visitTypeId;
//																				}
																				$scope.listOfAdmittedPatientList.push(newArr);
																			});
															$scope.commonListCount = response.data.object;
									            			$scope.pager = {};
									            			$scope.page;
									            			$scope.setPage(1,false);
									            			
															$timeout(function () {
															$rootScope.stopLoader();
															 }, 1000);
																console.log("list data",$scope.listOfAdmittedPatientList);
														}else{
															$rootScope.stopLoader();
														}
													});
								}


							}

							$scope.getDischargePatientListMaster = function(orgId, offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								
								var commonObjForDischargeList = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										dischargeDate : $scope.cD,
										wardList :$scope.wardList,
										visitTypeId:$scope.visitTypeIdflag
									};
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForDischargeList.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
									//$scope.visitTypeIdflag = $scope.bedManagmentCookiesObj.visitTypeId;
								}
								
								//patient redirected from bed management
								if ($scope.bedManagmentCookiesObj != null) 
								{
									commonObjForDischargeList.admissionId=$scope.bedManagmentCookiesObj.admissionId;
								}
								commonObjForDischargeList.offset = offset;
								commonObjForDischargeList.noOfRecordsPerPage = noOfRecordsPerPage;
								
								if($scope.nurseRole == 1 && $scope.wardList.length > 0){
									var URI = BASE_URL + ROOT_URL
//											+ GETDISCHARGEPATIENTLISTFORNURSING;
									        +"/adt/getDischargePatientListForNursingByVisitTypeId"
									GenericService
											.serviceAction("POST", URI, commonObjForDischargeList)
											.then(
													function(response) {
														$scope.listOfAdmittedPatientList = [];
														$scope.admittedPatientList = [];
														$rootScope.startLoader();
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
																				newArr.imagePath = value.imagePath;
																				newArr.prefixDesc = value.prefixDesc;
																				newArr.visitTypeId=value.visitTypeId;
																				newArr.companyName = value.companyName;
																				$scope.listOfAdmittedPatientList.push(newArr);
																			});
															$timeout(function () {
															$rootScope.stopLoader();
															 }, 1000);
																console.log("list data",$scope.listOfAdmittedPatientList);
														}else{
															$rootScope.stopLoader();
														}
													});
								}
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getDischargePatientListMaster($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.getDischargePatientList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End
							
							
							
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
								$scope.genderCode = $scope.itemSelecteds.genderCode;

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
											

											if (response.data.status == "success") 
											{

												$rootScope.stopLoader();
												$scope.pendingServicesList = [];
												$scope.pendingServicesList = response.data.listObject;
												//added for remove disabled only when patient is selected start
												var myEl3 = angular.element( document.querySelector( '#pendingServies' ) );
								            	myEl3.removeClass('disabled');
								            	var myEl3 = angular.element( document.querySelector( '#medicationHandOver' ) );
								            	myEl3.removeClass('disabled');
								            	/*
								            	var myEl3 = angular.element( document.querySelector( '#visitorsPass' ) );
								            	myEl3.removeClass('disabled');*/
								            	//added for remove disabled only when patient is selected end
								            	
												if($scope.pendingServicesList.length == 0)
													{
													/*$rootScope.billingFlag = false;
													alert($rootScope.billingFlag);*/
													//angular.element(document.getElementById('billing'))[0].disabled = false;
													var myEl = angular.element( document.querySelector( '#billing' ) );
									            	myEl.removeClass('disabled');
									            	/*var myEl1 = angular.element( document.querySelector( '#dischargeMedication' ) );
									            	myEl1.removeClass('disabled');*/
									            /*	var myEl2 = angular.element( document.querySelector( '#dischargeSummary' ) );
									            	myEl2.removeClass('disabled');*/
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
												  
												  if($scope.itemSelecteds.dischargeStatusId==3)
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
													 

											}else{
												 var myEl = angular.element( document.querySelector( '#movingBed' ) );
									            	myEl.addClass('disabled');
											}
										});
							}
									
							}
							
							
							$scope.getAllPatientDiagnosisForIcon=function(patientId){
								$scope.patientDiagnosisListIcon =[];
								 var headerObjDia = {
										 	'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : patientId,
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
	                                         	$scope.comorbidityFlag = false;
	                                         	$scope.notifiableFlag = false;
												
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
																	$scope.comorbidityFlag = true;
																}
															});
														}
	                                         
	                                     });
								}
							
							$scope.selectPatient = function(item) {
								var dsDama = angular.element(document.querySelector( '#damaContent' ) );
				            	dsDama.addClass('disabled');
								var dsDisSlip = angular.element(document.querySelector( '#dischargeSlip' ) );
								dsDisSlip.addClass('disabled');
				            	var dsDeathCer = angular.element(document.querySelector( '#deathId' ) );
				            	dsDeathCer.addClass('disabled');
				            	
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
								$scope.patientDetails.genderId = item.genderId;
								
								// for re direct required data
								$scope.patientDetailsObj.age = item.age;
								$scope.patientDetailsObj.patientName = item.pFirstName;
								$scope.patientDetailsObj.genderCode = item.genderCode;
								$scope.patientDetailsObj.uhidNumber = item.uhidnumber;
								$scope.patientDetailsObj.patientId = item.patientId;
								$scope.patientDetailsObj.dob = item.dob;
								$scope.patientDetailsObj.admissionId = item.admissionId;
								$scope.patientDetailsObj.genderId = item.genderId;
								$scope.patientDetailsObj.visitTypeId = $scope.visitTypeIdflag;
								
								$scope.getAllPatientDiagnosisForIcon($scope.patientDetailsObj.patientId);
								$scope.getPrintType($scope.dischargeSummaryId);
								$rootScope.allergydata(item);
								
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
							
							$scope.finalDischarge = function()
							{
								$state.go('finalDischarge');
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
								$scope.genderCode = $scope.itemSelecteds.genderCode;
								
								var templeteData  = {};
								
								var URI = BASE_URL + ROOT_URL+LIS_UNIT
								+ "/labTemplatesbydoctorForDischargeScreen"+"/"+$scope.organizationId+"/"+$scope.unitId+"/0/"+templateTypeId;
								GenericService.serviceAction("GET", URI, templeteData).then(
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
												
											} else if(response.data.status == 'error')
	    									{
	    										$rootScope.stopLoader();
	    										growl.warning("Sample Records not found",
	    												{
	    													title : 'Warning!'
	    												});
	    									}
										});

							  }
							}
							
							$scope.getTemplateListByTempType=function(tempTypeId){
								$scope.templateList=[];
								$rootScope.startLoader();
								var data = {
									orgId : $scope.organizationId,
									unitId : $scope.unitId,
									doctorId :0,
									specialityId : 0,
									templateTypeId : tempTypeId
								}
								
								console.log('data', JSON.stringify(data));
								var URI = BASE_URL + ROOT_URL
										+ "/api/global/coversheet/templates";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														angular.forEach(response.data.listObject,function(value,index){
															if(value.templateFormatId==2){
																$scope.templateList.push(value);
															}
														});
														$scope.templateObj = {};
														if($scope.templateList.length==0){
															growl.warning("No template mapped..!",
																	{
																		title : 'Warning!'
																	});
														}else{
															$("#addtemplateModal").modal();
														}
														$rootScope.stopLoader();
													}

												});
							}
							
							
							$scope.disableSaveBtn=false;
							$scope.disablePrintBtn=false;
							$scope.addTemplate = function() {
								if(angular.equals({},$scope.templateObj)){
									growl.warning("Select template",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								$scope.diagonisIdforTemplate = 0;
								$scope.icpcSystemId = 0;
								$scope.icpcId = 0;
								$scope.templateTransList=[];
								$rootScope.startLoader();
								var data = {
									orgId : $scope.organizationId,
									unitId : $scope.unitId,
									doctorId : 0,
									specialityId : 0,
									templateTypeId :$scope.templateObj.templateTypeId,
									patientId : $scope.itemSelecteds.patientId,
									visitTypeId : $scope.itemSelecteds.visitTypeId,
									admissionId : $scope.itemSelecteds.admissionId
								}

								console.log('data', JSON.stringify(data));
								var URI = BASE_URL + ROOT_URL + "/global/petient/template";;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.templateObj.templateTransactionId=null;
													if (response.data.status == 'success') {
														$scope.templateTransList = response.data.listObject;
													} 
													if($scope.templateTransList.length>0){
														angular.forEach($scope.templateTransList,function(value,indec){
															if(value.templateId==$scope.templateObj.templateId){
																$scope.templateObj.templateTransactionId=value.templateTransactionId;
																$scope.templateObj.templateObject=value.templateObject;
															}
														});
														if($scope.templateObj.templateTransactionId!=null){
															$scope.disablePrintBtn=false;
															$scope.disableSaveBtn=false;
														}
													}else{
														$scope.disableSaveBtn=false;
														$scope.disablePrintBtn=true;
													}
													$('#addtemplateModal').modal('hide');
													$("#templateDataModal").modal();
													$rootScope.stopLoader();
												
												});
                              }
							
							$scope.selectTemplate = function(templateObj) {
								$scope.templateObj  = templateObj;
							}
							
							$scope.saveTemplateTransaction=function(){
								
								$rootScope.startLoader();

							var templateTransaction = {};
							templateTransaction.signedBy = $scope.userId;
							templateTransaction.orgId = $scope.organizationId;
							templateTransaction.unitId = $scope.unitId;
							templateTransaction.createdBy = $scope.userId;
							templateTransaction.updatedBy = $scope.userId;
							templateTransaction.templateId = $scope.templateObj.templateId;
							templateTransaction.title = $scope.templateObj.title;
							templateTransaction.specialityId = $scope.templateObj.specialityId;
							templateTransaction.templateTransactionId=$scope.templateObj.templateTransactionId==null ? 0 :$scope.templateObj.templateTransactionId;
							templateTransaction.subSpecialityId = $scope.templateObj.subSpecialityId;
							templateTransaction.templateObject = $scope.templateObj.templateObject;
							templateTransaction.admissionId = $scope.itemSelecteds.admissionId;
							templateTransaction.doctorId = 0;
							templateTransaction.patientId = $scope.itemSelecteds.patientId;
							templateTransaction.diagnosisId = 0;
							templateTransaction.mIcpcSystemId = 0;
							templateTransaction.mIcpcId = 0;
					
							templateTransaction.templateObject =  $scope.templateObj.templateObject;
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
													if($scope.templateObj.templateTypeId==21){
														growl.success("Discharge slip saved successfully.",{
										    				title : SUCCESS_MSG
										    			 });
													}else if($scope.templateObj.templateTypeId==3){
														growl.success("Death certificate saved successfully.",{
										    				title : SUCCESS_MSG
										    			 });
														
													}else{
														growl.success("Template saved successfully.",{
										    				title : SUCCESS_MSG
										    			 });
														
													}

													$('#templateDataModal').modal('hide');
													$('#addtemplateModal').modal('hide');
													$scope.templateTransactionList = [];
													$scope.templateList = [];
													$scope.templateTransactionList = [];
												}
												$rootScope.stopLoader();
											});
								
							}
							
							$scope.printTemplateData=function(tempObj){
								console.log(tempObj);
								var data =  {
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "Discharge_Slip.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "Discharge_Slip",
						                 "reportParams": [
						                   {
						                  "paramName": "template_transaction_id",
						                  "paramType": "Integer",
						                  "paramValue": tempObj.templateTransactionId
						                }
						                 ]
						      };
								reportService.generate(REPORT_API_URI1,data);
							}
							
							$scope.saveTemplate = function()
							{
								/*console.log('Final Object Template',$scope.templateObj);
								
								 var popupWinindow = window.open('', '_blank', 'width=600,height=700,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
							        popupWinindow.document.open();
							        popupWinindow.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">'+$scope.templateObj.templetText+'</html>');
							        popupWinindow.document.close();*/
								var data =  {
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "Discharge_Slip.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "Discharge_Slip",
						                 "reportParams": [
						                   {
						                  "paramName": "admission_id",
						                  "paramType": "Integer",
						                  "paramValue": $scope.patientDetails.admissionId
						                }
						                 ]
						      };
								reportService.generate(REPORT_API_URI1,data);
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
								$scope.movingBedMsg = "Please wait..!!";
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
														$scope.activeMovingBedList = [];
														$scope.activeMovingBedList = response.data.listObject;
														console.log("activeMoving =",$scope.activeMovingBedList);
														if($scope.activeMovingBedList.length == 0)
															{
															  $scope.movingBedMsg = "No record found..!!";
															}
														
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
										$rootScope.startLoaderPopup();
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
																$scope.getDischargePatientList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
//																$scope.activeMovingBedList = response.data.listObject;
//																console.log("activeMoving =",$scope.activeMovingBedList);
//																$rootScope.stopLoader();
																$rootScope.stopLoaderPopup();
															}
															else
																{
																$rootScope.stopLoaderPopup();
																}
														});
										
										
								}
							
								else{
									growl.error('Please select atleast one bed...!!',
											{
												title : 'Error!'
											});
							
								}
							}
							
							/***************Discharge Summary Report***********************/
							$scope.getPrintType =  function(id){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										dischargeSummaryId : id
									};
								$scope.printTypeList=[];
									var URI = BASE_URL + ROOT_URL
											+ "/adt/discharge/getDischargeSummaryPrintType";
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.printTypeList = response.data.listObject;
															$scope.isHtml = $scope.printTypeList[0].isHtml;
														}
													});
							}
							
							
							$scope.checkPrint =  function(){
								
								if($scope.dischargeSummaryId!=null && $scope.dischargeSummaryId!=undefined){
									
									if($scope.isHtml!= null && $scope.isHtml == 'Y'){
										
											$("#printHeaderReportModal").show();
										
									}else{
										$("#printHeaderReportModal").show();
									}
								}else{
									  growl.warning(
												'First save discharge summary',
												{
													title : 'Warning!'
												});
								}
								
							}
							
							$scope.printDischargeSumReport=function(header)
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
							     }
								else{
									growl.error("Discharge summary report not generated",
											{
												title : 'Error!'
											});
									}
								
						}

							/***************Discharge Summary Report**************Chetana*********/
							$scope.printDischargeSumReportNew=function(flag)
							{
								//$scope.save();
								if($scope.dischargeSummaryId!=null){
									var data =  {
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "Dischargesummary.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "DischargeSummary",
								            "reportParams": [
								              {
									             "paramName": "Discharge_summary_Id",
									             "paramType": "Integer",
									             "paramValue": $scope.dischargeSummaryId
								              },
								              {
										             "paramName": "selectedHeader",
										             "paramType": "Integer",
										             "paramValue":  parseInt(flag)
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
							
							$scope.printVisitorPass=function(info){
								  var data =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "PatientGatepass.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "Coversheet",
							                 "reportParams": [
							                	 {
									                  "paramName": "visitor_patient_id",
									                  "paramType": "Integer",
									                  "paramValue":  info.visitorPatientId
								                },
								                
					                 ]
					      };
							    reportService.generate(REPORT_API_URI1,data);
							}
							
							$scope.hideModel=function(){
								$("#printHeaderReportModal").hide();
							}
							
							$scope.patientDocumnetInformation = function(info) {
								$scope.patientDocumentFlipBook ={};
								$scope.patientDocumentFlipBook =info;
								
									var data =
									{
									"encounterId":$scope.patientDocumentFlipBook.encounterId,
									"patientId":$scope.patientDocumentFlipBook.patientId,
									"unitId":$scope.unitId,
									"organisationId":$scope.organizationId,
									"userId":$scope.userId
									}
									
								var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
								BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.patientDocumentListTest = [];
										if (response.data.status == "success") 
										{
											$scope.patientDocumentListTest = response.data.listObject;
											//console.log($scope.patientDocumentListTest.length);
											if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
												$('#patientDocumentOption').modal('show');
											}
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											$rootScope.stopLoader();
											}
									});
						
					          };
				          
				          $scope.convertDateToTimestamp = function(dateString) {
				                let parts = dateString.split("-");
				                let day = parseInt(parts[0], 10);
				                let month = parseInt(parts[1], 10) - 1; 
				                let year = parseInt(parts[2], 10);
				                let date = new Date(year, month, day);
				                let timestampInMilliseconds = date.getTime();
				                let timestampInSeconds = timestampInMilliseconds / 1000;
				                return timestampInSeconds;
				            };
				            
					     		$scope.generateFlipbookData = function() {
					                  let flipbookDatalist = [];

						     			var filteredlist=[];
						     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
						     		    	
						     		    	if(filterObj.documentPath != null){
						     				var flipbookDataobj={};
						     				 let createdDate = filterObj.createdDate;
						     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
						     				flipbookDataobj.timestamp=timestamp;
						     				flipbookDataobj.url=filterObj.documentPath;
						     				flipbookDataobj.tags=[];
						     				flipbookDataobj.tags.push("Document");
						     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
						     				flipbookDatalist.push(flipbookDataobj);
						     		    	}
						     				
						     			});

					                let data = {
					                    flipbookData: flipbookDatalist,
					                    logo: $scope.firstImgaa,
					                    privacy: "public"
					                };

					                let baseUrlLocation = $window.location.href;
					                let temp = baseUrlLocation.split('/');
					                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

					                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
					                
					        		try {
					        			var param={};
					        				$.ajax({
					        			        url : generateFlipbookurl,
					        					type : "POST",
					        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
					        					contentType : "application/json",
					        					dataType : "json",
					        					data : JSON.stringify(data),
					        			        //headers:getHeaders(),
					        					success : function(response){
					        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
					        							  window.open(''+response.flipbookUrl, '_blank');
					        						}else{
					        							$.notify("Error occurred","error");
					        						}
					        			    	}
					        			});
					        	     } catch (e) {
					        	 		console.log(e );
					        		}
					            };

							
						} ]);
