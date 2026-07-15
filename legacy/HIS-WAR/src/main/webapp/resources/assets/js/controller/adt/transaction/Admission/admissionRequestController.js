'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:admissionRequestController
 * @description # admissionRequestController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'admissionRequestController',
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
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, BillingGenericService, $stateParams) {

							// for datePicker
							
							$scope.admissionNoteDataER = JSON
									.parse(sessionStorage
											.getItem("admissionNoteObjER"));

							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
							
							var date = new Date();
							$scope.dateOptionsDao = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
									minDate : date.setDate((new Date()).getDate() - 2),
								};
							
							$scope.dateOptionsPdd = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};

							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};

							 $scope.openDatePicker = function() {
									// alert("in");
								      $scope.datepicker.opened = true;
								    };
								    $scope.datepicker = {
										      opened: false
										    };

							$scope.openDatePickerForNewDOA = function() {
								$scope.datepickerForNewDOA.opened = true;
							};

							$scope.datepickerForNewDOA = {
								opened : false
							};

							// for datePicker2
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date()
							};
							$scope.openDatePickerPdd = function() {
								$scope.datepickerPdd.opened = true;
							};

							$scope.datepickerPdd = {
								opened : false
							};

							$scope.openDatePickerForNewDOA = function() {
								$scope.datepickerForNewDOA.opened = true;
							};

							$scope.datepickerForNewDOA = {
								opened : false
							};
							
							
							//for New/Registerd
							$scope.openDatePickerDOB = function() 
							{
								$scope.datepickerDOBOpen.opened = true;
							};

							$scope.datepickerDOBOpen = {
								opened : false
							};
							
							$scope.datepicker = new Date();
							 var new_date = moment($scope.datepicker).add(5, 'days');
							 $scope.datepickerPdd = new Date();

							$scope.selectedDataForPatientRegi = function () {
								$scope.screenType = $stateParams.screenType;
								if ($scope.screenType === "Admission-Request") {
									if (!$stateParams.patientObj) {
										console.warn('patientObj missing in stateParams');
										return;
									}
									$scope.regType=1;
									const selectedDataRegistraction = angular.copy($stateParams.patientObj);

									$scope.admissionRequest.firstName     = selectedDataRegistraction.firstName || '';
									$scope.admissionRequest.lastName      = selectedDataRegistraction.lastName || '';
									$scope.admissionRequest.middleName    = selectedDataRegistraction.middleName || '';
									$scope.admissionRequest.emailId       = selectedDataRegistraction.email || '';
									$scope.admissionRequest.birthDate     = selectedDataRegistraction.birthDate || '';
									$scope.admissionRequest.mobileNumber  = selectedDataRegistraction.mobileNumber || '';
									$scope.admissionRequest.bloodGroup    = selectedDataRegistraction.bloodGroup || '';
									$scope.admissionRequest.genderId      = selectedDataRegistraction.genderId || '';
									$scope.admissionRequest.prefixId      = selectedDataRegistraction.prefixId || '';
									$scope.age                            = selectedDataRegistraction.age || '';
									if(selectedDataRegistraction.genderId==1){
									   $scope.admissionRequest.id=1;
									}
									else if(selectedDataRegistraction.genderId==2){
									$scope.admissionRequest.id=2;
									}
									else if(selectedDataRegistraction.genderId==3){
									$scope.admissionRequest.id=3;
									}
								}
							};


                         // patient searching admission flow start
                         $rootScope.searchPatientAdmission = function(patientsearchObj){

                                if(patientsearchObj.patientId=="" && patientsearchObj.uhIdNumber == "" && patientsearchObj.patientName == "" && patientsearchObj.genderId == "" && patientsearchObj.mobileNo == ""  && patientsearchObj.identificationTypeId == "" && patientsearchObj.identificationNo == "" && patientsearchObj.birthDate=="")
                                {
                                    growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
                                    return false;
                                }
                                if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 2)
                                {
                                    growl.warning("Please enter atleast 2 characters..!",{title : "WARNING!"});
                                    $scope.patientList = [];
                                    return false;
                                }


                                if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
                                    patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
                                }

                                $rootScope.startLoader();
                                if($scope.loadMoreFlag==true)
                                {
                                patientsearchObj.offset=$scope.offset;
                                $scope.loadMoreFlag=false;
                                }
                            else
                                {
                                patientsearchObj.offset=0;
                                $scope.loadMoreFlag=false;
                                $scope.patientList = [];
                                }
                            patientsearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
                                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
                                BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                                    if(response.data.listObject==null){
                                        growl.warning(response.data.message,{
                                            title : ERROR_MSG
                                        });
                                        $scope.patientSearchObj.birthDate="";
                                        return false;
                                    }

                                    if(Object.keys(response.data.listObject).length  == 0){
                                        growl.warning("No Result Found!",{
                                            title : ERROR_MSG
                                        });
                                        $scope.patientSearchObj.birthDate="";
                                        return false;
                                    }else if(patientsearchObj.patientId!=null && patientsearchObj.patientId>0 && (response.data.listObject).length  == 1){
                                        $scope.patientList.push(response.data.listObject[0]);
                                        $stateParams.patientObj.uhidNumber;
                                        $scope.getPatientDetailsByIndex(0);
                                    }else{
                                         $scope.patientList = [];
                                        angular.forEach(response.data.listObject, function (value, key) {
                                        if(value.registrationTypeId != 5 && value.registrationTypeId !=2)
                                            {
                                                $scope.patientList.push(value);
                                            }
                                        });
                                        $scope.patientSearchObj.birthDate="";
                                    }

                                });

                            }
                                              //patient searching admission flow end

							/* init() function for form object create. */
							$scope.init = function() {
								
								$scope.LISDynamicLabel = sessionStorage
										.getItem('selectedPageName');
								$scope.selectedPageNameForAdmissionNote = sessionStorage
										.getItem('selectedPageNameForAdmissionNote');
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								$scope.minAge = cookieObjectAppConfig.minAge;
								$scope.maxAge = cookieObjectAppConfig.maxAge;
								$scope.defaultPatientCategoryId=cookieObjectAppConfig.defaultPatientCategoryId;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.emailValidationRequired = cookieObjectAppConfig.emailValidationRequired;
								$scope.mobileValidationRequired = cookieObjectAppConfig.mobileValidationRequired;
								
		                         $scope.emailOtp = null;
	                             $scope.entredOpt = "";
								$scope.groupName = "";
								$scope.UHIDNumber = "";
								$scope.firstName = "";
								$scope.middleName = "";
								$scope.lastName = "";
								$scope.genderCode = "";
								$scope.age = "";
								$scope.patientFullName = "";
								
								$scope.patientList = [];
								$scope.noRecordMsg = true;
								$scope.otpvalidated = false;
								$scope.otpvalidatedMobile = false;
								$scope.age = null;
								$scope.regType  = 2;
								
								$scope.patientSelectedIdx = null;
								
								$scope.fieldDisabled = false;
								$scope.disablePDD=false;
								$scope.ph_numbr = /^\+?\d{10}$/;
						        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
								
								var data = {};
								$scope.patientList = [];
								$scope.patientSearchObj = {
									"uhIdNumber" : "",
									"patientName" : "",
									"genderId" : "",
									"mobileNo" : "",
									"birthDate" : "",
									//"indentificationTypeId":"",
									"identificationNo" : "",
									"patientType" : 1,
									"unitId" : $scope.unitId,
									"organizationId" : $scope.organizationId
								}
								$scope.patientDetails = {
									"patientName" : "",
									"genderCode" : "",
									"age" : "",
									"uhIdNumber" : "",
									"birthDate" : ""
								}

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVESPECIALITY;
							
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.specialityList = response.data.listObject;
												});
								$scope.allBedCategoryList=[];
								/*var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDCATEGORYLISTBYUNIT;*/
								var URI = BASE_URL + ROOT_URL +"/adt/bedCategory/availableBeds";
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success"){
														$scope.bedCategoryList=[];
													$scope.allBedCategoryList=response.data.listObject;
													$scope.dayCareIpBillingBedCateList=response.data.object.dayCareIpBedCategoryList;
													angular.forEach($scope.allBedCategoryList,function(value,index){
														var flag=false;
														angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
															//for IP
															if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==2){
																flag=true;
															}
														});
														if(flag==true){
															$scope.bedCategoryList.push(value);
														}
													});
												  }
												});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.reasonList = response.data.listObject;
												});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEIDENTIFICATION;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.identificationList = [];

											if (response.data.status == "success") {
												$scope.identificationList = response.data.listObject;
												$scope.identificationTypeList = response.data.listObject;

											}
										});
						
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORY;
						GenericService
								.serviceAction("GET", URI, {})
								.then(
										function(response) {
											$scope.patientCategoryList = [];

											if (response.data.status == "success") {
												$scope.patientCategoryList = response.data.listObject;
												console
														.log($scope.patientCategoryList);

											}
										});
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEPREFIXLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, {},
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.prefixList = [];
									if (response.data.status == "success")
										$scope.prefixList = response.data.listObject;
								});

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEGENDERLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, {},
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.genderList = [];
									if (response.data.status == "success")
										$scope.genderList = response.data.listObject;
								});
				
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVEADMISSIONTYPELIST+"/"+$scope.organizationId;
		GenericService
				.serviceAction("GET", URI, {})
				.then(
						function(response) {
							$scope.admissionTypeList = [];

							if (response.data.status == "success") {
								$scope.admissionTypeList = response.data.listObject;
								console.log("$scope.admissionTypeList",$scope.admissionTypeList);

							}
						});
		

								$scope.searchGroupList = function(keyword) {

									var data = {
										firstName : keyword,
										unitId : $scope.unitId,
										// visitType :
										// $scope.selectedPageNameForAdmissionNote,
										organizationId : $scope.organizationId
									}
									console.log(data);

									var URI = BASE_URL + ROOT_URL
											+ GETPATIENTBYKEYWORD;
									
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
									// return data="[{name:'test'}]";

								}

								$rootScope.loginpage = true;
								$scope.admissionRequest = {
									isFlexiable : "N",
									visitTypeId : 2, //IP
									patientId : '',
									tPatientId : '',
									patientCategoryId : $scope.defaultPatientCategoryId,
									genderId : 0,
									note : "",
									id : '',
									prefixId : '',
									emailId : '',
									admissionTypeId : '',
									isTreatingDoctor :"N",
									treatingDoctorSpecialityId : '',
									treatingDoctorId : '',
								}
								$scope.isDayCareChkSelected='N';
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								if ($scope.selectedPageNameForAdmissionNote == 'ER') {

									if ($scope.admissionNoteDataER != null) {

										if ($scope.admissionNoteDataER.patientId > 0) {
											$scope.admissionRequest.patientId = $scope.admissionNoteDataER.patientId;
										} else {
											$scope.admissionRequest.tPatientId = $scope.admissionNoteDataER.tPatientId;
										}

										$scope.admissionRequest.id = $scope.admissionNoteDataER.genderId;
										// $scope.UHIDNumber =
										// $scope.admissionNoteDataER.uhidnumber;
										$scope.patientDetails.uhIdNumber = $scope.admissionNoteDataER.uhidnumber;
										$scope.age = $rootScope
												.getAge($scope.admissionNoteDataER.dob);
										// $scope.patientFullName =
										// $scope.admissionNoteDataER.pFirstName
										// + " " + $scope.age+ "/" +
										// $scope.admissionNoteDataER.genderCode;
										$scope.patientDetails.patientName = $scope.admissionNoteDataER.pFirstName + $scope.admissionNoteDataER.genderCode;
										$scope.patientDetails.age = $scope.admissionNoteDataER.dob;
										$scope.patientDetails.genderCode = $scope.admissionNoteDataER.genderCode;
										$scope.patientDetails.birthDate = "-";
										$scope.admissionRequest.visitTypeId = $scope.admissionNoteDataER.visitTypeId;
										$scope.admissionRequest.patientCategoryId = $scope.admissionNoteDataER.patientCategoryId;
										$scope.admissionRequest.prefixId = $scope.admissionNoteDataER.prefixId;

										var arrayN = $scope.admissionNoteDataER.doa
												.split(' ');
										var array = arrayN[0].split('/');
										var newDateDoa = array[1] + '/'
												+ array[0] + '/' + array[2];
										$scope.datepicker = new Date(newDateDoa);
									}
								}
								
								//getting default field configuration start
								//code for registration field configuration start
							
									
											var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
													
											GenericService.serviceAction("GET", URI).then(function (response) {
												if (response.data.status == 'success') {
													
													$scope.fieldConfigList=response.data.listObject;
													if($scope.fieldConfigList.length>0)
														{
														 $scope.registrationFieldObj=$scope.fieldConfigList[0];
														 console.log("registrationFieldObj",$scope.registrationFieldObj);
														}else{
															$scope.registrationFieldObj={
																	prefix: "Y",
																	firstName: "Y",
																	middleName: "N",
																	lastName: "Y",
																	birthDate: "Y",
																	sex: "Y",
																	callingCode: "Y",
																	mobileNo: "Y",
																	mobileDigit:"10",
																	nationality:"Y",
																	identificationDoc:"Y",
																	race:"Y",
																	maritalStatus:"Y",
																	occupation:"Y",
																	patientCategory:"Y",
																	email:"N"
															}
														}												
													

												} else if (response.data.status == 'error') {
													growl.error(response.data.message,
														{
															title: 'Error!'
														});
												}
											});

								$scope.selectedDataForPatientRegi();
											
								//getting default field configuration start
							}

							$scope.init();



							
							$scope.initAdmissionReqObj = function()
							{
								$scope.admissionRequest = {
										isFlexiable : "N",
										visitTypeId : 2, //IP
										patientId : '',
										tPatientId : '',
										patientCategoryId : $scope.defaultPatientCategoryId,
										genderId : 0,
										note : "",
										id : '',
										prefixId : '',
										emailId : '',
										admissionTypeId : '',
										isTreatingDoctor :"N",
										treatingDoctorSpecialityId : '',
										treatingDoctorId : '',
									}
							}
							
							
							$scope.changePDD = function()
							{
								
								$scope.dateOptionsPdd.minDate = new Date($scope.datepicker);
									 var newDatePdd = moment($scope.datepicker).add(5, 'days');
									 $scope.datepickerPdd = new Date(newDatePdd);
									 
									 
							}
							$scope.checkNewReg = function(type)
							{
								if(type == 1)
									{
									
								
									  $scope.fieldDisabled = false;
									  $scope.patientDetails = {};
									  $scope.initAdmissionReqObj();
									  $scope.patientList = [];
									  $scope.patientSearchObj = {
												"uhIdNumber" : "",
												"patientName" : "",
												"genderId" : "",
												"mobileNo" : "",
												"birthDate" : "",
												//"indentificationTypeId":"",
												"identificationNo" : "",
												"patientType" : 1,
												"unitId" : $scope.unitId,
												"organizationId" : $scope.organizationId
											}

										$state.go('patientRegistration', {
											screenType: "Admission-Request",
										});
									  
									}
								else 
									{
									$scope.otpvalidated = false;
										$scope.admissionRequest.patientCategoryId = '';
										$scope.fieldDisabled = true;
										$scope.initAdmissionReqObj();
										if($scope.patientSelectedIdx != null)
											{
											 //alert();
											 $scope.getPatientDetailsByIndex($scope.patientSelectedIdx);
											}
									}


							}
							
							$scope.selectGender = function(id)
							{
								var genId = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].id;
								
								$scope.admissionRequest.id = genId;
								
							}
							
							$scope.getSexMasterList = function(){
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
								BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									$scope.sexMasterList = response.data.listObject;
								});
							}
							$scope.getSexMasterList();
							
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAdmissionRequest();
							}

							$scope.searchPatient = function(patientsearchObj) {
								console.log(patientsearchObj)
								if(patientsearchObj.uhIdNumber=="" && patientsearchObj.patientName=="" && patientsearchObj.genderId==""
									 && patientsearchObj.mobileNo == "" && patientsearchObj.birthDate=="" && patientsearchObj.identificationTypeId=="" && patientsearchObj.identificationNo=="")
								  {
										growl.warning("Please enter searching criteria details",{title : "warning!"
										});
										return false;
									}
								if(patientsearchObj.birthDate!="")
								patientsearchObj.birthDate= moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
								
								console.log("patientsearchObj.birthDate ",patientsearchObj.birthDate);
								//return false;
									$scope.noRecordMsg = false;
									$rootScope.startLoader();
								var URI = BASE_URL
										+ ROOT_URL
										+ PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												patientsearchObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.patientList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.patientList = response.data.listObject;
//														if($stateParams.patientObj || $scope.patientList.length > 0){
//                                                                $scope.getPatientDetailsByIndex(0);
//                                                                $(".patienttSearchResult").css("display","none");
//                                                            }
													$scope.patientSearchObj.birthDate="";
													$rootScope.stopLoader();
												});
							}

							$scope.getPatientDetailsByIndex = function(index) {
								if($scope.patientList[index].admissionId){
									var message = "";
									if($scope.unitId==$scope.patientList[index].admissionUnitId)
					            		message = "Patient is already admitted in IP...!!";
					            	else
					            		message = "Patient is already admitted in another unit...!!";
									
									growl.warning(message,{
										title : WARNING_MSG
									});

									$scope.patientList = [];
									return false;
								}
								$scope.otpvalidated = false;
								$scope.initAdmissionReqObj();
								$scope.abscondPatientFlag="";
								if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
									$scope.patientSelectedIdx = index;
									$scope.patientDetails.age = ($scope.patientList[index].age);
									$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
									$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
									$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
									$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
									
									$scope.admissionRequest.firstName = ($scope.patientList[index].firstName);
									$scope.admissionRequest.lastName = ($scope.patientList[index].lastName);
									$scope.admissionRequest.middleName = ($scope.patientList[index].middleName);
									$scope.admissionRequest.emailId = ($scope.patientList[index].email);
									$scope.admissionRequest.birthDate = ($scope.patientList[index].birthDate);
									$scope.admissionRequest.mobileNumber = ($scope.patientList[index].mobileNo);
									$scope.admissionRequest.bloodGroup = ($scope.patientList[index].bloodGroup);
	//								$scope.admissionRequest.emailId = ($scope.patientList[index].emailId);
									
									 var array = $scope.patientList[index].birthDate.split('/');
									 var newDate = "";
								     newDate = array[1]+'/'+array[0]+'/'+array[2];
									 $scope.datepickerDOB = new Date(newDate);
									
									//var dt = $scope.patientList[index].birthDate;
									$scope.age = $rootScope.getAge(newDate);
									
	
									if ($scope.patientList[index].patientType == 1) {
										$scope.admissionRequest.patientId = $scope.patientList[index].patientId;
									} else {
										$scope.admissionRequest.tPatientId = $scope.patientList[index].patientId;
									}
	
									$scope.admissionRequest.prefixId = $scope.patientList[index].prefixId;
									$scope.admissionRequest.id = $scope.patientList[index].genderId;
									$scope.admissionRequest.patientCategoryId = $scope.patientList[index].patientCategoryId;
									
									$scope.regType = 2;
									$scope.fieldDisabled = true;
								}else{
									$scope.abscondIndex=index;
									$scope.abscondPatientFlag='Y';
									angular.element('#abscondedPatientPopUp').modal('show');
								}

							}
							$scope.creatingEncounterForAbscondedPatient=function(){
								
								var index=$scope.abscondIndex;
								$scope.patientDetails.age = ($scope.patientList[index].age);
								$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
								$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
								
								$scope.admissionRequest.firstName = ($scope.patientList[index].firstName);
								$scope.admissionRequest.lastName = ($scope.patientList[index].lastName);
								$scope.admissionRequest.middleName = ($scope.patientList[index].middleName);
								$scope.admissionRequest.emailId = ($scope.patientList[index].email);
								$scope.admissionRequest.birthDate = ($scope.patientList[index].birthDate);
								$scope.admissionRequest.mobileNumber = ($scope.patientList[index].mobileNo);
								$scope.admissionRequest.bloodGroup = ($scope.patientList[index].bloodGroup);
//								$scope.admissionRequest.emailId = ($scope.patientList[index].emailId);
								
								 var array = $scope.patientList[index].birthDate.split('/');
								 var newDate = "";
							     newDate = array[1]+'/'+array[0]+'/'+array[2];
								 $scope.datepickerDOB = new Date(newDate);
								
								//var dt = $scope.patientList[index].birthDate;
								$scope.age = $rootScope.getAge(newDate);
								

								if ($scope.patientList[index].patientType == 1) {
									$scope.admissionRequest.patientId = $scope.patientList[index].patientId;
								} else {
									$scope.admissionRequest.tPatientId = $scope.patientList[index].patientId;
								}

								$scope.admissionRequest.prefixId = $scope.patientList[index].prefixId;
								$scope.admissionRequest.id = $scope.patientList[index].genderId;
								$scope.admissionRequest.patientCategoryId = $scope.patientList[index].patientCategoryId;
								
								$scope.regType = 2;
								$scope.fieldDisabled = true;
							}
							$scope.getDoctorBySpeciality = function(id) {

								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
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
							
							
							
							$scope.getDoctorBySpecialityTreating = function(id) {

								
								
								var pddConfig = $.grep($scope.specialityList, function (mppr) {
			                         return mppr.specialityId == id;
			                     })[0].pddDays;
								
								var new_date = moment($scope.datepicker).add(pddConfig, 'days');
								$scope.datepickerPdd = new Date(new_date);
								
								
								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorTreatingList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.doctorTreatingList = response.data.listObject;
												});

							}
							
							
							$scope.getIsTreatingDocVal = function(item){
								
								if(item.isTreatingDoctor == 'Y'){
									
									
									
									var pddConfig = $.grep($scope.specialityList, function (mppr) {
				                         return mppr.specialityId == item.specialityId;
				                     })[0].pddDays;
									
									var new_date = moment($scope.datepicker).add(pddConfig, 'days');
									$scope.datepickerPdd = new Date(new_date);
									
								}
							}
							
							
							$scope.selectPatientId = function(info, model,
									label) {
								var data = {
									patientId : info.patientId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTDETAILSBYPATIENTID;

								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.admissionRequest.patientId = response.data.listObject[0].patientId;
													$scope.admissionRequest.prefixId = response.data.listObject[0].prefixId;
													$scope.admissionRequest.id = response.data.listObject[0].id;
													$scope.admissionRequest.patientCategoryId = response.data.listObject[0].patientCategoryId;
													$scope.UHIDNumber = response.data.listObject[0].uhidnumber;
													$scope.firstName = response.data.listObject[0].firstName;
													$scope.middleName = response.data.listObject[0].middleName;
													$scope.lastName = response.data.listObject[0].lastName;
													$scope.genderCode = response.data.listObject[0].genderCode;
													$scope.age = $rootScope
															.getAge(response.data.listObject[0].dob);
													$scope.patientFullName = $scope.firstName
															+ " "
															+ $scope.middleName
															+ " "
															+ $scope.lastName
															+ " "
															+ $scope.age
															+ "/"
															+ $scope.genderCode;
													$scope.groupName = "";
												});

							}
							
							/*$scope.newAge="";
							$scope.calculateAge= function()
							{
								var dt = moment($scope.datepickerDOB).format('MM/DD/YYYY');
								$scope.newAge = $rootScope.getAge(dt);
								if($scope.newAge != undefined){
									if($scope.newAge>=$scope.minAge && $scope.newAge <= $scope.maxAge){
										$scope.age=$scope.newAge;	
									}else{
										$scope.age = "";
										growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
											title : "warning"
											});
									}
								}
							}*/
							$scope.newAge="";
							$scope.calculateAge= function()
							{
								if($scope.datepickerDOB != undefined || $scope.datepickerDOB != null || $scope.datepickerDOB != ''){
								
								if($scope.datepickerDOB > new Date())
									{
											       $scope.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepickerDOB = new Date();
												
												   return false;
									}else{
										
										var dt = moment($scope.datepickerDOB).format('MM/DD/YYYY');
										$scope.newAge = $rootScope.getAge(dt);
										if($scope.newAge != undefined){
											if($scope.newAge>=$scope.minAge && $scope.newAge <= $scope.maxAge){
												$scope.age=$scope.newAge;	
											}else{
												$scope.age = "";
												growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
													title : "warning"
													});
											}
										}
										
									}
							
								}
							}

							// Function for save Admission Request
							$scope.saveAdmissionRequest = function() {
								
								 
	                         		 if($scope.otpvalidated == true ){
	                         			 
	                         			growl.warning("Please validate email Id ",{
											title : "warning!!!"
										});
	                         			//$scope.setSecondTabLevel(111);
										return false;
	                         		 }	
	                         		 
	                         		 if($scope.otpvalidatedMobile == true ){
	                         			 
		                         			growl.warning("Please validate mobile number ",{
												title : "warning!!!"
											});
		                         			//$scope.setSecondTabLevel(111);
											return false;
		                         		 }	
								
								if($scope.patientDetails.patientName == '' && $scope.regType == 2)
									{
									growl.warning("Please Search And Select Patient!!! ",{
										title : 'Warning!'
									});
									
									}
								else
									{
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');

								if ($scope.admissionrequestform.$valid) {
									
									if($scope.admissionRequest.mobileNumber != '')
									{
										 var mobileNo = $scope.admissionRequest.mobileNumber;
										if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
						        			
										}else{
											growl.warning("Please enter valid mobile number",{
												title : 'Warning!'
												});
												return false;
					        		}
									}
									
									/*if( $scope.admissionRequest.emailId != ''){
										 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
						           		 var email =$scope.admissionRequest.emailId;
						           		    if (!filter.test(email)) {
					        				    growl.error(EMAIL_MSG,{
													title : ERROR_MSG
												});
					        				    return false;
						           		    }
									}*/
									  /*if($scope.admissionRequest.mobileNumber != '')
										{
										  var val = $scope.admissionRequest.phoneNumber;
						        			 if (/^\d{10}$/.test(val)) {

						        				} else {
						        				   
						        				    growl.error(PHONE_NUMBER_MSG,{
														title : ERROR_MSG
													});
						        				    $('#mobileNumber').focus();
						        				    return false;
						        				}
										}
									else if( $scope.admissionRequest.emailId != ''){
									 var email = $scope.admissionRequest.email;
					           		    if (!filter.test(email)) {
				        				    growl.error(EMAIL_MSG,{
												title : ERROR_MSG
											});
				        				    $('#emailId').focus();
				        				    return false;
					           		    }
									}
									*/
									
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										
										$rootScope.startLoader();
										
										var admTime = moment(new Date()).format("HH:mm:ss");
										var dateOfAdm  =  moment($scope.datepicker).format('DD-MM-YYYY');
										var dischargeDate = moment($scope.datepickerPdd).format('DD-MM-YYYY');
										/*if ($scope.admissionRequest.isFlexiable == true) {
											$scope.admissionRequest.isFlexiable = "Y";
										} else {
											$scope.admissionRequest.isFlexiable = "N";
										}*/

										var data = {
											doctorSpecialityId : $scope.admissionRequest.specialityId,
											requestToDoctorId : $scope.admissionRequest.doctorId,
											reasonId : $scope.admissionRequest.reasonId,
											doa : dateOfAdm+' '+admTime,
											pdd : dischargeDate+' '+admTime,
											status : 'A',
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											organizationId : $scope.organizationId,
											activeStatus : 'P',
											visitTypeId : $scope.isDayCareChkSelected=='N' ? $scope.admissionRequest.visitTypeId : 3,
											requestBy : $scope.userId,
											// admissionId : '0',
											unitId : $scope.unitId,
											patientId : $scope.admissionRequest.patientId,
											tPatientId : $scope.admissionRequest.tPatientId,
											preVisitId : '0',
											isFlexiableDate : $scope.admissionRequest.isFlexiable,
											admissionStatus : 'P',
											isCancel : 'N',
											bedCategoryId : $scope.admissionRequest.bedCategoryId,
											paymentEntitlementId : '1',
											patientCategoryId : $scope.admissionRequest.patientCategoryId,
											// patientCategoryId : '1',
											prefixId : $scope.admissionRequest.prefixId,
											id : $scope.admissionRequest.id,
											updatedDate : $scope.currentDate,
											createdDate : $scope.currentDate,
											note : $scope.admissionRequest.note,
											admissionTypeId : $scope.admissionRequest.admissionTypeId,
											treatingDoctorSpecialityId : $scope.admissionRequest.treatingDoctorSpecialityId,
										    treatingDoctorId : $scope.admissionRequest.treatingDoctorId,
										    isAdmissionReqFromCoversheet : 'N'

										}
										
										if($scope.regType == 1)
											{											
												data.prefixId = $scope.admissionRequest.prefixId;
												data.pFirstName = $rootScope.capitalize_Words($scope.admissionRequest.firstName);
												data.pMiddleName =  ($scope.admissionRequest.middleName == null || $scope.admissionRequest.middleName.trim() == "" || $scope.admissionRequest.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.admissionRequest.middleName));
												data.pLastName = ($scope.admissionRequest.lastName == null || $scope.admissionRequest.lastName.trim() == "" || $scope.admissionRequest.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.admissionRequest.lastName));
												data.dob =moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss');
												data.genderId =$scope.admissionRequest.id;
												data.mobileNumber =$scope.admissionRequest.mobileNumber;
												data.email =$scope.admissionRequest.emailId;
												data.registrationTypeId = 5;
												data.patientId = 0;
												data.patientCategoryId = $scope.defaultPatientCategoryId;
												data.bloodGroup =$scope.admissionRequest.bloodGroup;
												data.kinList = [];
											}
										
									     if($scope.admissionRequest.isTreatingDoctor == 'Y')
											{
											  data.treatingDoctorSpecialityId = $scope.admissionRequest.specialityId;
											  data.treatingDoctorId = $scope.admissionRequest.doctorId;
											}
										
										console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ ADDADMISSIONREQUEST;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, data,
														NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) {
															console
																	.log(response);
															//$rootScope.startLoader();
															if (response.data.status == "success")
																{
																// update abscond patient as normal patient start
																if($scope.abscondPatientFlag!=undefined  && $scope.abscondPatientFlag!="" && $scope.abscondPatientFlag!=null && $scope.abscondPatientFlag=='Y'){
																var URI = BASE_URL + ROOT_URL+ "/api/global/updatePatientAsAbscound";					
																var obj={
																		patientId:$scope.admissionRequest.patientId
																}		
																console.log('abscond obj',obj);
//																return false;
																GenericService.serviceAction("POST", URI, obj).then(
																		function(response) {	
																			if (response.data.status == "success") {
																				$scope.abscondPatientFlag="";
																				$scope.datepickerDOB='';
																			}
																		});
																}
																// update abscond patient as normal patient end
																
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															    $scope.datepicker=new Date();
																 var dtPdd = moment($scope.datepicker).add(5, 'days');
																 $scope.datepickerPdd = new Date();
																 $scope.datepickerDOB = null;
															   // $scope.datepickerDOB=new Date();
															   $scope.admissionRequest="";
															    $rootScope.stopLoader();
																}
															else if(response.data.status == "error")
																{
															 	 $rootScope.stopLoader();
																}
															
														});
									}
								}
							}

							}
							/*********/
						//	$scope.myFunct = function(age) {
						//		 if($('#datepicker').val()!=""){
						//				var currentYear = (new Date()).getFullYear();
						//			    var finalYear = currentYear - age;									    
						//			}else{
						//				var currentYear = (new Date()).getFullYear();
						//			    var finalYear = currentYear - age;
						//			    $scope.datepicker = new Date("01/01/"+finalYear);
						//			}
								/* if($('#datepicker').val()!=""){
									var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;									    
								}else{
									var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;
								    var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
        							var m=new Date().getMonth();
        							var month=a[m];
        							var days=new Date().getDate();
        							var c=days+"/"+month+"/"+finalYear;
								    $scope.datepickerDOB = moment(c,"DD-MM-YYYY").toDate();
								}*/
							    //alert(finalYear);
							  
						//	}
							
							$scope.myFunct = function(age) {
								if(age>=$scope.minAge && age <= $scope.maxAge){
									 if($('#datepickerDOB').val()!=""){
							 
										 var dt = moment($scope.datepickerDOB).format('MM/DD/YYYY');
											var newAgeE = $rootScope.getAge(dt);
											var tArr = dt.split("/");
                                             var m = tArr[0];
                                             var d=tArr[1];
											if(newAgeE != age)
												{
												var currentYear = (new Date()).getFullYear();
											    var finalYear = currentYear - age;
											    var mon=$scope.ageMonth
                                                 var dat=$scope.ageDay
                                                 $scope.datepickerDOB = new Date(m +'/' +d +'/'  +finalYear);
												}
											

										}else{
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;
										     $scope.datepickerDOB = new Date(m +'/' +d +'/'  +finalYear);
										}
								}else{
									$scope.age = "";
									growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
										title : "warning"
										});
								}
								  
							}
							
							/**************/
							$scope.getbedStatus = function()
							{
								var data = {
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								}
								
								var URI = BASE_URL + ROOT_URL
								+ "/api/adt/bedCategoryAvailabilityCount/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;

							BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.bedStatusList = [];
												// $rootScope.startLoader();
												if (response.data.status == "success")
													{
													  $scope.bedStatusList =  response.data.listObject;
													}
													
													
											});
						
							}
							
							$scope.getBillingBedCategoryListByDayCareVisit=function(isDayCare){
								
							$scope.bedCategoryList =[];
							 if(isDayCare=='Y'){
								 $scope.disablePDD=true;
								  $scope.datepicker = new Date();
								  $scope.datepickerPdd= new Date();
								 //$scope.datepickerPdd=$scope.datepicker;
								 $scope.admissionRequest.isFlexiable='N';
								angular.forEach($scope.allBedCategoryList,function(value,index){
									var flag=false;
									angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
										//for day Care
										if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==3){
											flag=true;
										}
									});
									if(flag==true){
										$scope.bedCategoryList.push(value);
									}
								});
							}else{
								 $scope.disablePDD=false;
								 
								 var dateChange = moment($scope.datepicker).add(5, 'days');
								 $scope.datepickerPdd = new Date(dateChange);
								angular.forEach($scope.allBedCategoryList,function(value,index){
									var flag=false;
									angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
										//for day Care
										if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==2){
											flag=true;
										}
									});
									if(flag==true){
										$scope.bedCategoryList.push(value);
									}
								});
							}
								
							}
							
							
							
							 $scope.openEmailPopup = function(){
								 
								 if($scope.admissionRequest.email == undefined){
                            		 $scope.admissionRequest.email ="";
                            	 }
								 
				             	 var filter = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/; 
					           		 var emailId = $scope.admissionRequest.emailId;
				             	 if($scope.admissionRequest.emailId == 'filter')
				         			{
				         			}
				             	else{
				             		 if (!(emailId.match(filter))) {
						           		    growl.error("Please enter valid email id.",{
				          					title : ERROR_MSG
				          					});
				          					return false;;
						           		    }
				             		 $('#sendOtpOnMail').modal('show');
				      			}
				              }
                           
                           $scope.clearEmail = function(){
                           	
                          	 $('#sendOtpOnMail').modal('hide');
                          		 $scope.emailOtp = null;
                           }
                           
                           
                           
                           
                           $scope.sendMailOtp = function(){
                          	 
                          	 $rootScope.startLoader();
                          	 
                               var data ={
                              		 toMailId : $scope.admissionRequest.emailId,
                                       unitId : $scope.unitId,
                                       organizationId : $scope.organizationId
                               }; 
                                   
                               var URI = BASE_URL + ROOT_URL+"/autoMail/sendOTP";
                               BillingGenericService
                                       .serviceAction(METHOD_POST,URI, data)
                                       .then(
                                               function(response) {
                                                   if (response.data.status == "success"){
                                                  	 
                                                  	 growl.success("OTP send on enterd email id.",
       														{
       															title : 'Success!'
       														});
                                                  	 
                                                  	 $scope.emailOtp  = response.data.object;
                                                       $rootScope.stopLoader();
                                                   }else{
                                                       $rootScope.stopLoader();
                                                   }
                                               });
                               }
                           
                           $scope.validateOtp = function(){
                            	
                          	 if($scope.emailOtp == $scope.entredOpt){
                          		 $('#sendOtpOnMail').modal('hide');
                          		 $scope.emailOtp = null;
                          		 $scope.otpvalidated = false;
                          		 $scope.storeEmailId = $scope.admissionRequest.emailId;
                          	}else{
                          		$scope.otpvalidated = true;
                          			growl.warning("Please enter valid OTP",{title : "WARNING!"
									});
									return false;
                          	}
                           }
                           
                           $scope.checkEmail = function(){
                          	 
                          	// if($scope.registrationFieldObj.email == "Y"){
                          		 
                          		 if($scope.storeEmailId != null  && $scope.storeEmailId == $scope.admissionRequest.emailId){
                          			 $scope.otpvalidated = false;
                          		 }else{
                          			if($scope.admissionRequest.emailId != null && $scope.admissionRequest.emailId != "" && $scope.emailValidationRequired == 'Y'){
                          				$scope.otpvalidated = true;
                          			}else{
                          				$scope.otpvalidated = false;
                          			}
                          		 }
										
									//}
                          	 
                           }
                         
                           $scope.openMobilePopup = function(){
                        	   if($scope.admissionRequest.mobileNumber != undefined && $scope.admissionRequest.mobileNumber.length ==10){
                            		 $('#sendOtpOnMobile').modal('show');
                     			}else{
                     				growl.error(" Please enter valid mobile number",{
                     					title : ERROR_MSG
                     					});
                     					return false;
                     			}
                              }
                           
                           $scope.clearMobile = function(){
                             	
                          	 $('#sendOtpOnMobile').modal('hide');
                          		 $scope.entredOptMobile = null;
                           }
                           
                           $scope.sendMobileOtp = function(){
                          	 
                          	 $rootScope.startLoader();
                          	 
                               var data ={
                              		 mobileNumber : $scope.admissionRequest.mobileNumber,
                                       unitId : $scope.unitId,
                                       organizationId : $scope.organizationId
                               }; 
                                   
                               var URI = BASE_URL + ROOT_URL+"/autoMail/sendOTPonMobile";
                               BillingGenericService
                                       .serviceAction(METHOD_POST,URI, data)
                                       .then(
                                               function(response) {
                                                   if (response.data.status == "success"){
                                                  	 
                                                  	 growl.success("OTP send on entered mobile number.",
       														{
       															title : 'Success!'
       														});
                                                  	 
                                                  	 $scope.mobileOtp  = response.data.object;
                                                       $rootScope.stopLoader();
                                                   }else{
                                                       $rootScope.stopLoader();
                                                   }
                                               });
                               }
                           
                           $scope.validateOtpMobile = function(){
                              	
                          	 if($scope.mobileOtp == $scope.entredOptMobile){
                          		 $('#sendOtpOnMobile').modal('hide');
                          		 $scope.mobileOtp = null;
                          		 $scope.otpvalidatedMobile = false;
                          	}else{
                          		$scope.otpvalidatedMobile = true;
                          			growl.warning("Please enter valid OTP",{title : "WARNING!"
									});
									return false;
                          	}
                           }
                           
                           $scope.checkMobileNumber = function(){
                   			 if( $scope.admissionRequest.mobileNumber != null && $scope.admissionRequest.mobileNumber != "" && $scope.mobileValidationRequired == 'Y'){
										$scope.otpvalidatedMobile = true;
									}else{
										$scope.otpvalidatedMobile = false;
									}
                            }


                            if ($stateParams.patientObj) {
                                    $scope.patientSearchObj = {
                                              "uhIdNumber"      : $stateParams.patientObj.uhidNumber,
                                              "patientName"     : "",
                                              "genderId"        : "",
                                              "mobileNo"        : "",
                                              "birthDate"       : "",
                                              "identificationNo":"",
                                              "patientType" : 1,
                                              "unitId"          :$scope.unitId,
                                              "organizationId"  :$scope.organizationId
                                                  }
                                      $scope.searchPatient($scope.patientSearchObj);
                                      }



							
							
							  
						} ]);