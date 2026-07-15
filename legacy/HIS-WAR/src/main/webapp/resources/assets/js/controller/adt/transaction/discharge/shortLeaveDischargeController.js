'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:shortLeaveDischargeController
 * @description #shortLeaveDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'shortLeaveDischargeController',
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
						'$stateParams',
						'BillingGenericService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,BillingGenericService) {

							$scope.dateOptions = {
								formatYear : 'yyyy',
								todayHighlight:'TRUE',
								maxDate:new Date(),
								showWeeks : false
							};
							
							$scope.dateOptionss = {
									formatYear : 'yyyy',
									todayHighlight:'TRUE',
									minDate:new Date(),
									showWeeks : false
								};
							
							$scope.dateOptionssTo = {
									formatYear : 'yyyy',
									todayHighlight:'TRUE',
									minDate:new Date(),
									showWeeks : false
								};
							
							$scope.openDatePickerSearch = function() {
								$scope.datepicker2.opened = true;
								};

								$scope.datepicker2 = {
								opened : false
								};
								
							$scope.openDatePicker = function() {
								// alert("in");
								$scope.datepicker.opened = true;
							};
							$scope.datepicker = {
								opened : false
							};

							$scope.openDatePickerTo = function() {
								// alert("in");
								$scope.datepickerTo.opened = true;
							};
							$scope.datepickerTo = {
								opened : false
							};
							
							
							/* init() function for form object create. */
							$scope.init = function() {

								/*	if ($cookies.get('bedManagmentCookies') != null) {
										$scope.bedManagmentCookiesObj = JSON
												.parse($cookies
														.get('bedManagmentCookies'));
										console.log($scope.bedManagmentCookiesObj);
										$scope.patientFullName = $scope.bedManagmentCookiesObj.patientFullName;
										$scope.uhidNum = $scope.bedManagmentCookiesObj.uhidNum;
										$scope.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
										$scope.doctorFullName = $scope.bedManagmentCookiesObj.doctorFullName;
										$scope.wardName = $scope.bedManagmentCookiesObj.wardName;
										$scope.admissionId  =  $scope.bedManagmentCookiesObj.admissionId;
									  } else {
										$scope.bedManagmentCookiesObj = null;
									}*/

								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.patientList = [];
								$scope.patientSearchObj = {
									"uhIdNumber" : "",
									"patientName" : "",
									"genderId" : "",
									"mobileNo" : "",
									"birthDate" : "",
									"identificationNo" : "",
									"patientType" : 1,
									"unitId" : $scope.unitId,
									"organizationId" : $scope.organizationId
								}
								$scope.patientLable = {
									"patientName" : "",
									"genderCode" : "",
									"age" : "",
									"uhIdNumber" : "",
									"birthDate" : ""
								}

								$scope.shortRequest = {
									patientId : '',
									tPatientId : '',
									note : "",
									fromTime : "",
									toTime : "",
									reasonId : "",
									admissionId : 0,
									doctorId : 0

								}

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								};

								var URI = BASE_URL + ROOT_URL
										+ GETSHORTLEAVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});

								$scope.patientDetails = {};
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
									//$scope.patientSearchObj.patientId = $scope.bedManagmentCookiesObj.getPatientId;


									$scope.patientDetails.age = ($scope.bedManagmentCookiesObj.age);
									$scope.patientDetails.patientName = ($scope.bedManagmentCookiesObj.patientName);
									$scope.patientDetails.genderCode = ($scope.bedManagmentCookiesObj.genderCode);
									$scope.patientDetails.uhIdNumber = ($scope.bedManagmentCookiesObj.uhidNum);
									$scope.patientDetails.birthDate = ($scope.bedManagmentCookiesObj.birthDate);

									$scope.shortRequest.patientId = $scope.bedManagmentCookiesObj.patientId;
									
									$scope.shortRequest.admissionId = $scope.bedManagmentCookiesObj.admissionId;
									$scope.shortRequest.doctorId =$scope.bedManagmentCookiesObj.doctorId;
								}
									
								
							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveShortLeaveRequest();
							}

							$scope.noRecordMsg = true;
							$scope.searchPatient = function(patientsearchObj) 
							{
								var patSearchObj = angular.copy($scope.patientSearchObj);
								if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									return false;
								}	
								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
									var dt = new Date($scope.patientSearchObj.birthDate);
									patientsearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" + dt.getFullYear();
								} 
								var URI = BASE_URL
										+ ROOT_URL
										+ PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
								GenericService
										.serviceAction("POST", URI,
												patientsearchObj)
										.then(
												function(response) {
													$scope.patientList = [];

													if (response.data.status == "success") {
														if(response.data.listObject.length>0){
															angular.forEach(response.data.listObject,function(value,index){
																if(value.admissionId !=null && value.admissionId!=undefined && value.visitTypeId != 4){
																	$scope.patientList.push(value);
																
																}
															});
															if($scope.patientList.length>0){
																$scope.noRecordMsg = true;
															}else{
																$scope.noRecordMsg = false;
															}
														}
														$scope.patientSearchObj.birthDate="";
														//$scope.patientList = response.data.listObject;
													}
													else if(response.data.listObject==null){
														growl.warning(response.data.message,{
															title : ERROR_MSG
														});
														$scope.patientSearchObj.birthDate="";
														$scope.patientList=[];
														return false;
													}
												});
							}

							$scope.getPatientDetailsByIndex = function(index) {
								
								

								if($scope.patientList[index].admissionId != null && typeof($scope.patientList[index].admissionId) != "undefined"){
									$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.patientList[index].admissionId;
									BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.patientDetails.age = ($scope.patientList[index].age);
											$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
											$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
											$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
											$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
											$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);

											if ($scope.patientList[index].patientType == 1) {
												$scope.shortRequest.patientId = $scope.patientList[index].patientId;
											} else {
												$scope.shortRequest.tPatientId = $scope.patientList[index].patientId;
											}
											
											$scope.shortRequest.admissionId = $scope.patientList[index].admissionId;
											$scope.shortRequest.doctorId =response.data.object.doctorId;
										}
										$rootScope.stopLoader();
									});	
							}
								else
									{
										growl
										.error(
												'No admission of this Patient ',
												{
													title : 'Error!'
												});
									}

								

							}
							
							$scope.changeToDate = function()
							{
								$scope.dateOptionssTo.minDate = new Date($scope.datepicker);
								$scope.datepickerTo = new Date($scope.datepicker);
									 
							}

							// Function for save Admission Request
							$scope.saveShortLeaveRequest = function() {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
							
								/*var sdt = moment(periodicityServ.fromDate).format('YYYY-MM-DD');
						    	sdt = sdt +' '+periodicityServ.fromTime;
						    	
						    	var tdt = moment(periodicityServ.toDate).format('YYYY-MM-DD');
						    	tdt = tdt +' '+periodicityServ.toTime;
						    	if(sdt > tdt){
						    		growl.warning("To date and time should be greater then start date and time.",{
										title : WARNING_MSG
									});
						    		return false;
					    		}*/
								if($scope.shortRequest.admissionId == 0)
								 {
									growl
									.warning(
											'Please Select Patient...!!',
											{
												title : 'Warning!'
											});
									return false;
								 }
								else if($scope.datepicker.opened == false || $scope.datepicker == null || $scope.datepicker == undefined){
									growl.warning('Please select from date.',
											{
												title : 'Warning!'
											});
									return false;
								}
								else if($scope.datepickerTo.opened == false || $scope.datepickerTo == null || $scope.datepickerTo == undefined){
									growl.warning('Please select to date.',
											{
												title : 'Warning!'
											});
									return false;
								}
								else if($scope.shortRequest.fromTime == '' || $scope.shortRequest.fromTime == null || $scope.shortRequest.fromTime == undefined){
									growl.warning('Please select from time.',
											{
												title : 'Warning!'
											});
									return false;
								}
								else if($scope.shortRequest.toTime == '' || $scope.shortRequest.toTime == null || $scope.shortRequest.toTime == undefined){
									growl.warning('Please select to time.',
											{
												title : 'Warning!'
											});
									return false;
								}
								else if($scope.shortRequest.reasonId == '' || $scope.shortRequest.reasonId == null || $scope.shortRequest.reasonId == undefined){
									growl.warning('Please select reason.',
											{
												title : 'Warning!'
											});
									return false;
								}
								 else
									 {
									 
									 var sdt = moment($scope.datepicker).format('YYYY-MM-DD');
								    	sdt = sdt +' '+$scope.shortRequest.fromTime;
								    	
								    	var tdt = moment($scope.datepickerTo).format('YYYY-MM-DD');
								    	tdt = tdt +' '+$scope.shortRequest.toTime;
								    	if(sdt > tdt){
								    		growl.warning("To date and time should be greater then from date and time.",{
												title : WARNING_MSG
											});
								    		return false;
							    		}
								    	
								    	else{
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										var data = {
											admissionId : $scope.shortRequest.admissionId,
											shortLeaveReasonId : $scope.shortRequest.reasonId,
											fromDate : moment($scope.datepicker)
													.format(
															'DD-MM-YYYY HH:mm:ss'),
											toDate : moment($scope.datepickerTo)
													.format(
															'DD-MM-YYYY HH:mm:ss'),

											note : $scope.shortRequest.note,
											fromTime : $scope.shortRequest.fromTime,
											toTime : $scope.shortRequest.toTime,
											status : 'A',
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											patientId : $scope.shortRequest.patientId,
											tPatientId : $scope.shortRequest.tPatientId,
											updatedDate : $scope.currentDate,
											createdDate : $scope.currentDate,
											shortLeaveStatusId : '1',
											doctorId : $scope.shortRequest.doctorId 

										}
										console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ ADDSHORTLEAVEREQUEST;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Short Leave Request Sent Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.bedManagmentCookiesObj = null;
																$scope.patientDetails = null;
																$stateParams.bedManagmentObj = null;
																$scope.init();
																
																
															} else {
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}

														});
									}
							}
							}

							}

						} ]);
