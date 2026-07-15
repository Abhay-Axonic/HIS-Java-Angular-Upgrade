'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:intiateTransferCareController
 * @description #intiateTransferCareController Controller of the myApp
 */

angular
		.module('myApp')
		.controller(
				'intiateTransferCareController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,GenericServiceParamHeader, growl,
								$state,$stateParams) {

							/* init() function for form object create. */
							
							
							$scope.disableFlagTC = true;
							$scope.comorbidityFlag = false;
		                	$scope.notifiableFlag = false;
		                	$scope.billing = true;
							
							  var isAlreadyExists;
								function checkAlreadyExistsOrNot(arr,temp) {
									return arr
											.some(function(arr) {
												if(parseInt(temp.id) === parseInt(arr.transferStatusId))
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
									};
								
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
							$scope.getPatientDetailsById = function(patientId){
								$rootScope.startLoader();
								var index = $scope.patientList.map(function (obj) { return obj.patientId; }).indexOf(patientId);
								$scope.patientId = patientId;
								$scope.patientDetails ={};
								$scope.admissionDetails = {};
								$('input[value="patientId"]:checked');
								$scope.admissionId = ($scope.patientList[index].admissionId);
								//$scope.icuFlag=true;
								//get admission details by id
								if($scope.admissionId != null && typeof($scope.admissionId) != "undefined")
								{
								 var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
									GenericService.serviceAction("GET",URI, null, {}).then(function(response){
										if(response.data.status == "success"){
											$scope.admissionDetails = response.data.object;
											$rootScope.stopLoader();
											if($scope.admissionDetails.dischargeStatusId!=null){
												$scope.showTrasnferReqBtn=true;
												growl
												.warning(
														'You cannot transfer this patient as discharge process for this patient is already initiated !!!',
														{
															title : 'WARNING!'
														});
											}else{
												$scope.showTrasnferReqBtn=false;
											}
											$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
											$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
											$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
											$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
											$scope.patientDetails.age = ($scope.patientList[index].age);
											$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
											$scope.patientDetails.dob =  ($scope.patientList[index].birthDate);
										    $scope.uhidNum = ($scope.patientList[index].uhIdNumber);
											$scope.patientDetails.patientId=($scope.patientList[index].patientId);
											$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
											$scope.visitTypeId = $scope.patientList[index].visitTypeId;
											$scope.patientDetails.dateOfAdmission = $scope.admissionDetails.doa;
											$scope.patientDetails.patientDischargeDate = $scope.admissionDetails.pdd;
											$scope.bedNumber = $scope.admissionDetails.bedNo;
											$scope.patientDetails.bedNumber = $scope.admissionDetails.bedNo;
											$scope.patientDetails.doctorFullName = $scope.admissionDetails.doctorName;
											$scope.wardName = $scope.admissionDetails.wardNo;
											$scope.patientDetails.wardNameInitiate = $scope.admissionDetails.wardNo;
											$scope.admissionId  =  $scope.admissionDetails.admissionId;
											$scope.patientDetails.admissionId = $scope.admissionDetails.admissionId;
											$scope.patientDetails.doctorId = $scope.admissionDetails.doctorId;
											$scope.doctorId = $scope.admissionDetails.doctorId;
											$scope.patientDetails.wardId = $scope.admissionDetails.wardId;
											$scope.patientDetails.bedCategoryId = $scope.admissionDetails.bedCategoryId;
											$scope.patientDetails.billingBedCategoryId = $scope.admissionDetails.bedBillingCategoryId;
											$scope.patientDetails.floorId = $scope.admissionDetails.floorId;
											$scope.patientDetails.bedId = $scope.admissionDetails.bedId;
											$scope.patientDetails.roomId = $scope.admissionDetails.roomId;
											$scope.patientDetails.initiateTransferOfCare = true;
											
											var dateDoa = moment($scope.admissionDetails.doa, 'DD/MM/YYYY');
											var datePdd = moment($scope.admissionDetails.pdd, 'DD/MM/YYYY');
											
											$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
											
											$rootScope.allergydata($scope.patientDetails);
											$scope.getAllPatientDiagnosis(patientId);
											$scope.billing = false;
											//$cookies.put('bedManagmentCookies',null);
											
											var commonObj2= {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId,
													transferTypeId:4,
													patientId:patientId
												
												}
											
											var URI = BASE_URL + ROOT_URL
											+ GETTRANSFEROFCAREREQUESTLIST;
									GenericService
											.serviceAction("POST", URI, commonObj2)
											.then(
													function(response) {
														$scope.transferOfCareRequestList = [];

														if (response.data.status == "success") {
															$scope.transferOfCareRequestList = response.data.listObject;
															console.log("transferOfCareRequestList",$scope.transferOfCareRequestList );
															var temp = {id :1};
															isAlreadyExists=checkAlreadyExistsOrNot($scope.transferOfCareRequestList,temp)
															if (isAlreadyExists) {
																$scope.disableFlagTC = true;
															}
															else
																{
																   $scope.disableFlagTC = false;
																}
															

														}
													});
											
											
										}
									});
								}
							}
							
							$scope.init = function() {
								
								$rootScope.patientFullName = '';
								$rootScope.uhidNum = '';
								$rootScope.bedNumber = '';
								$rootScope.doctorFullName = '';
								$rootScope.wardName ='';
								$scope.treatingDoctorId = '';
								$scope.admissionId = '';
								$scope.patientId = '';
								
								$scope.patientSearchObj ={};
								$scope.specialityId = '';
								$scope.requestDoctorId= '';
								$scope.reasonId= '';
								$scope.note= '';
								$scope.bedManagmentCookiesObj = null;
								/*
								 * $scope.unitId =
								 * sessionStorage.getItem("unitId");
								 * $scope.organizationId =
								 * sessionStorage.getItem("organizationId");
								 * $scope.userId =
								 * sessionStorage.getItem("userId");
								 */
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.doctorId = cookieObject.doctorId;
							  //  $scope.bedManagmentCookiesObj = $cookies.get('bedManagmentCookies');
							    //if ($scope.bedManagmentCookiesObj != null && $scope.bedManagmentCookiesObj != undefined) 
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								
								
								if ($scope.bedManagmentCookiesObj != null) 
								{
								
									//$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
									console.log($scope.bedManagmentCookiesObj);
									//$cookies.remove('bedManagmentCookies');
									$scope.patientFullName = $scope.bedManagmentCookiesObj.patientFullName;
									$scope.uhidNum = $scope.bedManagmentCookiesObj.uhidNum;
									$scope.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
									$scope.doctorFullName = $scope.bedManagmentCookiesObj.doctorFullName;
									$scope.wardName = $scope.bedManagmentCookiesObj.wardName;
									$scope.admissionId = $scope.bedManagmentCookiesObj.admissionId;
									$scope.doctorId = $scope.bedManagmentCookiesObj.doctorId;
									
									$scope.patientSearchObj.patientId=$scope.bedManagmentCookiesObj.patientId;
									$scope.patientId = $scope.bedManagmentCookiesObj.patientId;
									$rootScope.startLoader();
									$scope.transferOfCareRequestList = [];
									$scope.patientDetails ={};
									$scope.admissionDetails = {};
									$scope.patientList = [];
									var patientsearchObj=$scope.patientSearchObj;
									patientsearchObj.visitTypeId =$scope.bedManagmentCookiesObj.visitTypeId;
									patientsearchObj.organizationId = $scope.organizationId;
									patientsearchObj.unitId = $scope.unitId;
									patientsearchObj.patientType = 1;
									patientsearchObj.doctorId=$scope.doctorId;
									patientsearchObj.wardList =[];
									patientsearchObj.bedList =[];
									if($scope.rmoDoctorIP!=undefined || $scope.rmoDoctorIP!=null){
										patientsearchObj.wardList.push($scope.rmoDoctorIP)
									}
									
									var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByForTransferICU";
									
									if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
										patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
									}
									
									GenericService.serviceAction("POST",URI, patientsearchObj).then(function(response)
											{
										$scope.patientList = response.data.listObject;
										$scope.getPatientDetailsById($scope.patientSearchObj.patientId);
										$rootScope.stopLoader();
									});
									
									
									

								} else {
									$scope.bedManagmentCookiesObj = null;
								}

								$rootScope.loginpage = true;

								$scope.patientSurgeryOrder = {}
								$scope.itemSelecteds = {};

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var data = {};
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									}
								var commonObj2= {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId:4,
										doctorId:$scope.doctorId
									
									}

									var URI = BASE_URL + ROOT_URL
											+ GETACTIVESPECIALITY;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.specialityList = [];

														if (response.data.status == "success") {
															$scope.specialityList = response.data.listObject;

														}
													});
									
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
							
							}
							$scope.init();
							 $scope.getSexMasterList = function() {
							    	var data = {
							    			organizationId : $scope.organizationId
									};
							    	$scope.sexMasterList=[];
									var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
									GenericService.serviceAction("GET", URI, data).then(function(response) {
										$scope.sexMasterList = response.data.listObject;
										console.log($rootScope.sexMasterList);
									});
								}
							    $scope.getSexMasterList();
								$scope.getIdentificationTypeList = function(){
									var data = {
											organizationId : $scope.organizationId
										};
									$scope.identificationTypeList=[];
									var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
									GenericService.serviceAction("GET", URI, data).then(function(response) {
										if (response.data.status == "success")
											$scope.identificationTypeList = response.data.listObject;
										console.log($rootScope.identificationTypeList);
									});
								}
								$scope.getIdentificationTypeList();
							
							
							
							$scope.getDoctorBySpeciality = function(id) {

								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorList = [];
													if (response.data.status == "success") {
														$scope.doctorList = response.data.listObject;
													}
												});

							}
							

							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ GETADMITTEDPATIENTBYKEWWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										// alert("ff");
										// console.log(response.data.listObject);
										return response.data.listObject;

									}
								});
								// return data="[{name:'test'}]";
							}

							$scope.selectPatientId = function(info, model,
									label) {
								console.log(info);
								var data = {
									admissionId : info.admissionId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								};

								if (info.tPatientId > 0) {
									$rootScope.patientFullName = info.patientName
											+ ' ' + info.dob + '/'
											+ info.genderName;
								} else {
									var age = $rootScope.getAge(info.dob);
									$rootScope.patientFullName = info.patientName
											+ ' ' + age + '/' + info.genderName;
								}

								$rootScope.uhidNum = info.uhidnumber;
								$rootScope.bedNumber = info.bedNumber;
								$rootScope.doctorFullName = info.dFirstName;
								$rootScope.wardName = info.wardName;
								$scope.treatingDoctorId = info.doctorId;
								$scope.admissionId = info.admissionId;
								
							}

						
							$scope.patientList=[];
							$scope.searchPatientTransfer = function(){
								$rootScope.startLoader();
								$scope.transferOfCareRequestList = [];
								$scope.patientDetails ={};
								$scope.admissionDetails = {};
								var patientsearchObj = {};
								var patientsearchObj=$scope.patientSearchObj;
								patientsearchObj.visitTypeId =2;
								patientsearchObj.organizationId = $scope.organizationId;
								patientsearchObj.unitId = $scope.unitId;
								patientsearchObj.patientType = 1;
								patientsearchObj.doctorId=$scope.doctorId;
								patientsearchObj.wardList =[];
								patientsearchObj.bedList =[];
								$scope.patientId = '';
								if($scope.rmoDoctorIP!=undefined || $scope.rmoDoctorIP!=null){
									patientsearchObj.wardList.push($scope.rmoDoctorIP)
								}
								
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByForTransferICU";
								
								if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
									patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
								}
								
								GenericService.serviceAction("POST",URI, patientsearchObj).then(function(response)
										{
									$scope.patientList = response.data.listObject;
									$rootScope.stopLoader();
								});
							}
							
							
							
							

							
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveTransferOfCareRequest();
							}
							
							$scope.getAllPatientDiagnosis=function(patientId){
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
							
							$scope.saveTransferOfCareRequest = function() 
							{
								try {
									if($scope.specialityId== undefined || $scope.specialityId==null || $scope.specialityId=='')
										{
											growl.warning('Please select speciality...!!!',
													{
														title : 'Warning!'
													});
										}else if($scope.requestDoctorId== undefined || $scope.requestDoctorId==null || $scope.requestDoctorId=='')
										{
											growl.warning('Please select doctor...!!!',
													{
														title : 'Warning!'
													});
										}else if($scope.reasonId== undefined || $scope.reasonId==null || $scope.reasonId=='')
										{
											growl.warning('Please select reason...!!!',
													{
														title : 'Warning!'
													});
										}else if($scope.note== undefined || $scope.note==null || $scope.note=='')
										{
											growl.warning('Please add note...!!!',
													{
														title : 'Warning!'
													});
										}else if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

										} else {
										$rootScope.startLoader();
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');
										var data = {
											admissionId :$scope.admissionId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											requestBy : $scope.doctorId,
											specialityId : $scope.specialityId,
											requestTo : $scope.requestDoctorId,
											reasonId : $scope.reasonId,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											createdDate : $scope.currentDate,
											updatedDate : $scope.currentDate,
											status : 'A',
											note : $scope.note,
											transferTypeId:4,
											transferStatusId:1
										};
										console.log(data);
										var URI = BASE_URL + ROOT_URL
												+ SAVETRANSFEROFCAREREQUEST;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Request Sent Successfully...!!!',
																				{
																					title : 'Success!'
																				});
																
																var commonObj2= {
																		unitId : $scope.unitId,
																		organizationId : $scope.organizationId,
																		transferTypeId:4,
																		patientId:$scope.patientId
																	
																	}
																
																var URI = BASE_URL + ROOT_URL
																+ GETTRANSFEROFCAREREQUESTLIST;
														GenericService
																.serviceAction("POST", URI, commonObj2)
																.then(
																		function(response) {
																			$scope.transferOfCareRequestList = [];

																			if (response.data.status == "success") {
																				$scope.transferOfCareRequestList = response.data.listObject;
																				var temp = {id :1};
																				isAlreadyExists=checkAlreadyExistsOrNot($scope.transferOfCareRequestList,temp)
																				if (isAlreadyExists) {
																					$scope.disableFlagTC = true;
																				}
																				else
																					{
																					   $scope.disableFlagTC = false;
																					}
																			}
																		});
																$scope.init();
																$rootScope.stopLoader();
															} else {
																growl
																		.error(
																				'Something went wrong...!!!',
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
															}

														});

									              }
								     } catch (e) {
									 console.log("Exception",e.message);
								  }
								}
							/*}*/
							
							$scope.displayNote=function(note)
							{
								$scope.noteDoctor = note;
								
							}
							$scope.displayRejectionNote=function(remark)
							{
								$scope.remarkDoctor = remark;
								
							}
						
							
							/*$scope.acceptRequest = function(transferId) {
							
											if ($scope.popUpFlag) {
												$scope.showModal = !$scope.showModal;

											} else {
												
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
										
										
											}
										
								
								
							}*/

						} ]);

