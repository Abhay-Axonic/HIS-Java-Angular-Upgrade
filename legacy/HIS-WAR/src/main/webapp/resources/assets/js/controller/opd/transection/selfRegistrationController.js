'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:selfRegistrationController
 * @description #selfRegistrationController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'selfRegistrationController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'$stateParams',
						'reportService',
						'$window',
						'paymentCollFactory',
						'loyaltiCardCallFactory',
						'$interval',
						'$timeout',
						'$filter','$sce','GenericServiceParamHeader','$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService,GenericServiceFileUpload, growl, $state,
								$stateParams,reportService,$window,paymentCollFactory,loyaltiCardCallFactory,$interval, $timeout,$filter,$sce,GenericServiceParamHeader,$location) {
							
							/* init() function for form object create. */
							$scope.init = function() {
								$scope.tokePrintEnable=false;
								$scope.tokePrintEnableNurse=true;

								$scope.nurseRoleId = $cookies.getObject('cookieObjectAppConfig').nurseRole;
								var cookieObjectAppConfigSecondInit = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
								if (cookieObjectAppConfigSecondInit != undefined) {
								$scope.receptionId =  cookieObjectAppConfigSecondInit.staffRole;
								}
								$scope.showReceptionKiosBlock=false;
								if($scope.receptionId == 1 && $scope.nurseRoleId == 0 ){
									$scope.showReceptionKiosBlock=true;
								}

								 $scope.ageDay = 0;
									$scope.ageMonth = 0;
									$scope.ageYear = 0;
								$scope.patientName = undefined;
								$scope.age = "";
								$scope.calDOBFlag = 0;
								$scope.snapshotData= "";
								  $('#pieChartContainer').html(''); //remove canvas from container
								  $('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>');
								//for star camera
//								  var _video = null,
//							        patData = null;

//							    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

							    // Setup a channel to receive a video property
							    // with a reference to the video element
							    // See the HTML binding in main.html
//							    $scope.channel = {};
//
//							    $scope.webcamError = false;
//							    $scope.onError = function (err) {
//							        $scope.$apply(
//							            function() {
//							                $scope.webcamError = err;
//							            }
//							        );
//							    };

//							    $scope.onSuccess = function () {
//							        // The video element contains the captured camera data
//							        _video = $scope.channel.video;
//							        _video.width = 117;
//							        _video.height = 100;
//							        $scope.$apply(function() {
//							        	//alert(_video.width);
//							            $scope.patOpts.w = _video.width;
//							            $scope.patOpts.h = _video.height;
//							            //$scope.showDemos = true;
//							        });
//							    };

//							    $scope.onStream = function (stream) {
//							        // You could do something manually with the stream.
//							    };

//								$scope.makeSnapshot = function() {
//							        if (_video) {
//							            var patCanvas = document.querySelector('#snapshot');
//							            if (!patCanvas) return;
//							            //alert(_video.width);
//							            patCanvas.width = _video.width;
//							            patCanvas.height = _video.height;
//							            var ctxPat = patCanvas.getContext('2d');
//
//							            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
//							            ctxPat.putImageData(idata, 0, 0);
//
//							            sendSnapshotToServer(patCanvas.toDataURL());
//
//							            patData = idata;
//							        }
//							    };
							    
							    /**
							     * Redirect the browser to the URL given.
							     * Used to download the image by passing a dataURL string
							     */
//							    $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
//							        window.location.href = dataURL;
//							    };
//							    
//							    var getVideoData = function getVideoData(x, y, w, h) {
//							        var hiddenCanvas = document.createElement('canvas');
//							        hiddenCanvas.width = _video.width;
//							        hiddenCanvas.height = _video.height;
//							        var ctx = hiddenCanvas.getContext('2d');
//							        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
//							        return ctx.getImageData(x, y, w, h);
//							    };

							    /**
							     * This function could be used to send the image data
							     * to a backend server that expects base64 encoded images.
							     *
							     * In this example, we simply store it in the scope for display.
							     */
//							    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
//							        $scope.snapshotData = imgBase64;
//							    };
								
								//for end camera
								
								//for datepicker 1
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
								
								$scope.openDatePicker1 = function() {
									$scope.pwdExpiry.opened = true;
								};
								
								$scope.pwdExpiry = {
									opened : false
								};
								
								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.doctorId=null;
								$rootScope.loginpage = false;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.hideShowSearch = false;
								$scope.isPaidAppointment = 'N';
								$scope.hospitalName = cookieObject.unitNme;
								$scope.currentDate = new Date();
								$scope.validatTextFieldFlag = true;
								$scope.currentDateForKiosk = moment($scope.currentDate).format('DD-MM-YYYY');
								$scope.currentTime = moment(new Date()).format('HH:mm:ss');
								console.log("name date time",$scope.hospitalName+","+$scope.currentDate+","+$scope.currentTime);
								$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
								 if(cookieObjectAppConfig == undefined){
									 $state.go('applicationConfiguration');
								  return;
								 }
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.isAppointmentRequired=cookieObjectAppConfig.isAppointmentRequired;
								$scope.defaultPatientCategoryId=cookieObjectAppConfig.defaultPatientCategoryId;
								$scope.landingScreen = cookieObjectAppConfig.kioskLandingScreen;
								$scope.isAppointmentRequiredForMenu = cookieObjectAppConfig.isAppointmentRequired;
								//$scope.landingScreen = "walkinNew";
								$scope.minAge = cookieObjectAppConfig.minAge;
								$scope.maxAge = cookieObjectAppConfig.maxAge;
								if (cookieObjectAppConfigSecond != undefined) {
								$scope.appointmentUniqueIdSearch = cookieObjectAppConfigSecond.appointmentUniqueIdSearch;
								}
								$rootScope.stopLoader();
								
								
								
								$scope.searchType = 1;
								//alert($scope.searchType);
								
								$scope.searchPatientList = [];
								
								
								
								$scope.searchSelectPatient = {}
								$scope.itemSelecteds = {};
								
								
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
								$scope.ph_numbr = /^\+?\d{10}$/;
						        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
								
								$scope.patientObj = {
									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									genderId : '',
									countryCallingCode : '',
									email : '',
									maritalStatusId : '',
									mobileNumber : '',
									dob : '',
									age : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:$scope.defaultPatientCategoryId,
									registrationTypeId:5,
									imagePath:'',
									patientName: undefined,
								};
								
								$scope.patientAppoObj = {
										patientId : '',
										uhidNumber : '',
										prefixId : '',
										prefixCode : '',
										middleName : '',
										lastName : '',
										genderCode : '',
										age : '',
										appointmentDate : '',
										doctorName : '',
										doctorId : '',
										specialityName : '',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										specialityId : '',
										patientCategoryId:$scope.defaultPatientCategoryId,
										registrationTypeId:5,
										appointmentId:'',
										patientName: undefined,
										age : '',
								
									};
								
								
								$scope.hideShowSearch = false;
								$scope.hideShowPre = false;
								$scope.dept=false;
								$scope.doc=false;
								$scope.slot=false;
								$scope.saveEnable = true;
								$scope.saveAndPayEnable = true;
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var commonObj1 = {
									organizationId : $scope.organizationId
								};

								var data = {};

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPREFIXLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
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
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.genderList = [];
													if (response.data.status == "success")
														$scope.genderList = response.data.listObject;
												});
								
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEMARITALSTATUSLIST;

								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.maritalStatusList = [];
													if (response.data.status == "success")
														$scope.maritalStatusList = response.data.listObject;
												});

								
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;								
								BillingGenericService
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.countryList = [];
											if (response.data.status == "success")
												$scope.countryList = response.data.listObject;
										});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEIDENTIFICATION;
						BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.identificationList = [];
									if (response.data.status == "success")
										$scope.identificationList = response.data.listObject;
								});
								
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
															}
														}												
				
												} else if (response.data.status == 'error') {
													growl.error(response.data.message,
														{
															title: 'Error!'
														});
												}
											});
											
											$scope.searchobj = {};
											$scope.searchobj.appointmentUniqueId = "";
				
								//getting default field configuration start
							}


                                $scope.firstImg;
                                $scope.imageBackground = function(){
                                                var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/4"
                                                    GenericService.serviceAction("GET", URI).then(function(response) {
                                                        if(response.data.object != null){
                                                    $scope.firstImg=response.data.object.imageURL;
                                                        }
                                                    });
                                                }
                                 $scope.imageBackground();
							
							$scope.init2 = function() {
								$scope.patientName = undefined;
									$scope.saveEnable = false;
							$scope.saveAndPayEnable = false;
							$scope.initialAssesment=false;
								//for star camera
//								  var _video = null,
//							        patData = null;
//								  $scope.appointmentUniqueId = null;
//							    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

							    // Setup a channel to receive a video property
							    // with a reference to the video element
							    // See the HTML binding in main.html
//							    $scope.channel = {};
//
//							    $scope.webcamError = false;
//							    $scope.onError = function (err) {
//							        $scope.$apply(
//							            function() {
//							                $scope.webcamError = err;
//							            }
//							        );
//							    };

//							    $scope.onSuccess = function () {
//							        // The video element contains the captured camera data
//							        _video = $scope.channel.video;
//							        _video.width = 117;
//							        _video.height = 100;
//							        $scope.$apply(function() {
//							        	//alert(_video.width);
//							            $scope.patOpts.w = _video.width;
//							            $scope.patOpts.h = _video.height;
//							            //$scope.showDemos = true;
//							        });
//							    };

//							    $scope.onStream = function (stream) {
//							        // You could do something manually with the stream.
//							    };

//								$scope.makeSnapshot = function() {
//							        if (_video) {
//							            var patCanvas = document.querySelector('#snapshot');
//							            if (!patCanvas) return;
//							            //alert(_video.width);
//							            patCanvas.width = _video.width;
//							            patCanvas.height = _video.height;
//							            var ctxPat = patCanvas.getContext('2d');
//
//							            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
//							            ctxPat.putImageData(idata, 0, 0);
//
//							            sendSnapshotToServer(patCanvas.toDataURL());
//
//							            patData = idata;
//							        }
//							    };
							    
							    /**
							     * Redirect the browser to the URL given.
							     * Used to download the image by passing a dataURL string
							     */
//							    $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
//							        window.location.href = dataURL;
//							    };
							    
//							    var getVideoData = function getVideoData(x, y, w, h) {
//							        var hiddenCanvas = document.createElement('canvas');
//							        hiddenCanvas.width = _video.width;
//							        hiddenCanvas.height = _video.height;
//							        var ctx = hiddenCanvas.getContext('2d');
//							        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
//							        return ctx.getImageData(x, y, w, h);
//							    };

							    /**
							     * This function could be used to send the image data
							     * to a backend server that expects base64 encoded images.
							     *
							     * In this example, we simply store it in the scope for display.
							     */
//							    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
//							        $scope.snapshotData = imgBase64;
//							    };
								
								//for end camera
								
								//for datepicker 1
								$scope.dateOptions = {
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
								
								$scope.openDatePicker1 = function() {
									$scope.pwdExpiry.opened = true;
								};
								
								$scope.pwdExpiry = {
									opened : false
								};
								
								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.doctorId=null;
								$rootScope.loginpage = false;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.scheduleAppEnable =true;
								
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
									 $state.go('applicationConfiguration');
								  return;
								 }
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$rootScope.stopLoader();
								
								$scope.searchType = 1;
								//alert($scope.searchType);
								
								$scope.searchPatientList = [];
								
								$scope.datepicker = new Date();
								
								$scope.searchSelectPatient = {}
								$scope.itemSelecteds = {};
								
								
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
								$scope.ph_numbr = /^\+?\d{10}$/;
						        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
								
								$scope.patientObj = {
									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									genderId : '',
									countryCallingCode : '',
									email : '',
									maritalStatusId : '',
									mobileNumber : '',
									dob : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:$scope.defaultPatientCategoryId,
									registrationTypeId:5,
									patientName: undefined,
								};
								
								$scope.patientAppoObj = {
										patientId : '',
										uhidNumber : '',
										prefixId : '',
										prefixCode : '',
										middleName : '',
										lastName : '',
										genderCode : '',
										age : '',
										appointmentDate : '',
										doctorName : '',
										doctorId : '',
										specialityName : '',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										specialityId : '',
										patientCategoryId:$scope.defaultPatientCategoryId,
										registrationTypeId:5,
										appointmentId:'',
										patientName: undefined,
											
									};
								
								$scope.hideShowSearch = false;
								$scope.hideShowPre = false;
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var commonObj1 = {
									organizationId : $scope.organizationId
								};

								var data = {};

								$scope.dept=true;
								$scope.doc=false;
								$scope.slot=false;
								// $scope.saveEnable = true;
								// $scope.saveAndPayEnable = true;
							
									var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										console.log(response);
										if(response.data.status == "success"){
											$scope.specialityList = response.data.listObject;
										}
									});
								
							}
							 $scope.footerImg;
                             $scope.footerImges = function(){
                                             var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/5"
                                                 BillingGenericService.serviceAction("GET", URI).then(function(response) {
                                                	 if(response.data.object!= null){
                                                 $scope.footerImg=response.data.object.imageURL;
                                                 }else{
                                                	 return;
                                                 }
                                                 });
                                             }
                                          $scope.footerImges();

							
							$scope.init3 = function() {
								
								$scope.saveEnable = true;
								$scope.saveAndPayEnable = true;
								$scope.enableSettle = true;
								$scope.patientAppoObjPayment = {
										patientId : '',
										uhidNumber : '',
										prefixId : '',
										prefixCode : '',
										middleName : '',
										lastName : '',
										genderCode : '',
										age : '',
										appointmentDate : '',
										doctorName : '',
										doctorId : '',
										specialityName : '',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										specialityId : '',
										patientCategoryId:$scope.defaultPatientCategoryId,
										registrationTypeId:'',
										appointmentId:'',
										patientName: undefined,
										//addded by dinesh
										dueAmount:undefined,
										payeeDueAmtFinal:undefined,
										payeeDueAmtStatic:undefined,
										paymentDetailsObj:undefined,
										loyaltyCardObj:undefined,
										isPaidAppointment : '',
								
									};
								
								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.mpesaRequired = cookieObject.mpesaRequired;
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
									 $state.go('applicationConfiguration');
								  return;
								 }
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								$rootScope.stopLoader();
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var commonObj1 = {
									organizationId : $scope.organizationId
								};

							}
							
							
							
							if($stateParams.patientAppoObjPayment==null)
							{
								if($stateParams.patientDetailsObj==null)
									$scope.init();
							else{
									$scope.init2();
									$scope.patientObj = $stateParams.patientDetailsObj;
								}
							}else{
								$scope.init3();
								//$scope.patientAppoObjPayment = $stateParams.patientAppoObjPayment;
								$scope.patientAppoObjPaymentPayment = $stateParams.patientAppoObjPayment;
								
								//redirected from pesapal successPage
								var tempAppointmentObj = JSON.parse(sessionStorage.getItem('tempAppointmentObjet'));
									if(tempAppointmentObj != undefined){
										$scope.patientAppoObjPaymentPayment=tempAppointmentObj;
										//$cookies.remove('tempAppointmentObjet');
										//$scope.listPaymentDetails=$scope.patientAppoObjPaymentPayment.listPaymentDetails;
									}
								if($scope.patientAppoObjPaymentPayment.appointmentId!=null || $scope.patientAppoObjPaymentPayment.appointmentId>0){
									$scope.patientAppoObjPayment = $scope.patientAppoObjPaymentPayment;
									//addded by dinesh for multimode payment
									$scope.paymentDetails=$scope.patientAppoObjPayment.paymentDetailsObj;
									$scope.dueAmount=$scope.patientAppoObjPayment.payeeDueAmtFinal;
									$scope.loyaltyCardObj=$scope.patientAppoObjPayment.loyaltyCardObj;
									console.log("payment Details",$scope.paymentDetails);
									console.log("loyaltyCardObj Details",$scope.loyaltyCardObj);
									var patientString=$scope.patientAppoObjPaymentPayment.patientName;
									var patientSplitString=patientString.split(" ");
									$scope.patientAppoObjPaymentPayment.firstName=patientSplitString[1];
									$scope.patientAppoObjPaymentPayment.lastName=patientSplitString[2];
								}else{
									
								
								$scope.patientAppoObjPayment = 
								{
										uhidNumber : $scope.patientAppoObjPaymentPayment.uhidNumber!=null ? $scope.patientAppoObjPaymentPayment.uhidNumber : '                -                ',
										patientName : $scope.patientAppoObjPaymentPayment.prefixCode+" "+$scope.patientAppoObjPaymentPayment.firstName+" "+$scope.patientAppoObjPaymentPayment.middleName+" "+$scope.patientAppoObjPaymentPayment.lastName,
										appointmentDate : $scope.patientAppoObjPaymentPayment.appointmentDate,
										appointmentDate2 : $scope.patientAppoObjPaymentPayment.appointmentDate2,
										specialityName : $scope.patientAppoObjPaymentPayment.specialityName,
										doctorName : $scope.patientAppoObjPaymentPayment.doctorName,
										doctorId : $scope.patientAppoObjPaymentPayment.doctorId,
										appointmentTime : $scope.patientAppoObjPaymentPayment.appointmentTime,
										dob : $scope.patientAppoObjPaymentPayment.dob,
										email : $scope.patientAppoObjPaymentPayment.email,
										countryCallingCode : $scope.patientAppoObjPaymentPayment.countryCallingCode,
										genderId : $scope.patientAppoObjPaymentPayment.genderId,
										organizationId : $scope.patientAppoObjPaymentPayment.organizationId,
										unitId : $scope.patientAppoObjPaymentPayment.unitId,
										patientCategoryId : $scope.patientAppoObjPaymentPayment.patientCategoryId,
										registrationTypeId : $scope.patientAppoObjPaymentPayment.registrationTypeId,
										patientId : $scope.patientAppoObjPaymentPayment.patientId,
										dateOfBirth : $scope.patientAppoObjPaymentPayment.dateOfBirth,
										isFromSelf : $scope.patientAppoObjPaymentPayment.isFromSelf,
										totalBillAmount : $scope.patientAppoObjPaymentPayment.totalBillAmount,
										serviceAmount : $scope.patientAppoObjPaymentPayment.serviceAmount,
										taxAmount : $scope.patientAppoObjPaymentPayment.taxAmount,
										systemDefiedVisitType : $scope.patientAppoObjPaymentPayment.systemDefiedVisitType,
										slotId : $scope.patientAppoObjPaymentPayment.slotId,
										originalFromTime : $scope.patientAppoObjPaymentPayment.originalFromTime,
										specialityId : $scope.patientAppoObjPaymentPayment.specialityId,
										//addded by dinesh
										dueAmount:$scope.patientAppoObjPaymentPayment.dueAmount,
										payeeDueAmtFinal:$scope.patientAppoObjPaymentPayment.payeeDueAmtFinal,
										payeeDueAmtStatic:$scope.patientAppoObjPaymentPayment.payeeDueAmtStatic,
										paymentDetailsObj:$scope.patientAppoObjPaymentPayment.paymentDetailsObj,
										loyaltyCardObj: $scope.patientAppoObjPaymentPayment.loyaltyCardObj,
										deptWiseAppointment : $scope.patientAppoObjPaymentPayment.deptWiseAppointment,
										prefixId : $scope.patientAppoObjPaymentPayment.prefixId,
										firstName : $scope.patientAppoObjPaymentPayment.firstName,
										middleName: $scope.patientAppoObjPaymentPayment.middleName,
										lastName: $scope.patientAppoObjPaymentPayment.lastName,
										mobileNumber : $scope.patientAppoObjPaymentPayment.mobileNumber,

										paymentCompleted : $scope.patientAppoObjPaymentPayment.paymentCompleted,
										encounterId : $scope.patientAppoObjPaymentPayment.encounterId,
										
											
								};
									$scope.paymentDetails=$scope.patientAppoObjPayment.paymentDetailsObj;
									$scope.dueAmount=$scope.patientAppoObjPayment.dueAmount;
									$scope.loyaltyCardObj=$scope.patientAppoObjPayment.loyaltyCardObj;
									console.log("payment Details",$scope.paymentDetails);
								}	
								
							}
							
							
							
							$scope.selfTypeCheck=$stateParams.selfReg;
							console.log('$stateParams.patientObj',$stateParams.patientObj);
							if ($scope.selfTypeCheck!= undefined && $scope.selfTypeCheck.type != undefined) {
								$scope.sRt = $scope.selfTypeCheck.type;
								if($scope.sRt == "T")
									{
									  $scope.hideShowSearch = true;
									  $scope.hideShowPre = false;
									}
								else if($scope.sRt == "R")
								{
								  $scope.hideShowSearch = true;
								  $scope.hideShowPre = true;
								}
								else
									{
									  $scope.hideShowSearch = false;
									  $scope.hideShowPre = true;
									}
								
							}
							
							$scope.redirectSelfT = function()
							{
								var selfObj = {
										type:"T"
								};
								 $state.go("selfRegistration", {
									 selfReg:selfObj
									});
							}
							$scope.redirectSelfR = function()
							{
								var selfObj = {    
										type:"R"
								};
								 $state.go("selfRegistration", {
									 selfReg:selfObj
									});
							}
							$scope.redirectSelfN = function()
							{
								var selfObj = {
										type:"N"
								};
								 $state.go("selfRegistration", {
									 selfReg:selfObj
									});
							}
							$scope.patientListForSearch= [];
							$scope.searchGroupList = function(keyword) {

                                if (keyword!=undefined  &&  (keyword.replace(/\s/g, "").length) == 3  ) {


								var data = {
										patientName : keyword,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										isFromKioskToken : 'Y'
									}
									console.log(data);

									var URI = BASE_URL + ROOT_URL
											+ PATIENTTYPEHEADSEARCHFORSELFREGISTRATION;

								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {

													if (response.data.status == "success")
													$scope.patientListForSearch = response.data.listObject;
														return response.data.listObject;
												});

								}else{
								     if(keyword !=undefined &&  (keyword.replace(/\s/g, "").length) > 0){
                                                   return $filter('filter')( $scope.patientListForSearch, {
                                                                                     $: keyword
                                                                                   });
                                                }

								}

							}
							
							$scope.searchAppointments = function(keyword) {
								
								
								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									isFromKioskToken : 'N'
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTTYPEHEADSEARCHFORSELFREGISTRATION;
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}
							
							
							$scope.enterUniqueCode = function(event){
								 var enterCode = 13;
							      if (event.which === enterCode) {
							    	  $scope.searchAppointmentsUnique();
							      }
							}

              $scope.getInitalAssesmentData = function (uniqAppoId) {

    var data = {
        appointmentUniqueId: uniqAppoId,
        unitId: $scope.unitId,
        organizationId: $scope.organizationId
    };

    var URI = BASE_URL + ROOT_URL + "/api/global/getInitalAssesmentData";

    $rootScope.startLoader();

    return BillingGenericService
        .serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
        .then(function (response) {

            $rootScope.stopLoader();

            if (response.data.status !== "success") {
                return;
            }

            if (!response.data.listObject || response.data.listObject.length === 0) {
                $scope.initialAssesment = false;
                growl.warning(
                    "No Data found for Initial assessment",
                    { title: "WARNING!" }
                );
                return;
            }

            var info = response.data.listObject[0];

            if (!info) {
                return;
            }


            $scope.initialAssesment = true;
            $scope.saveEnable = false;
            $scope.saveAndPayEnable = false;

            if (info.admissionId > 0) {
                growl.warning("Patient is currently admitted...!!", { title: "WARNING!" });
                return;
            }

            $scope.currentDate = moment(new Date()).format('DD-MM-YYYY');

            if (info.currentDate &&
                $scope.currentDate === info.currentDate &&
                info.appointmentStatusId === 1) {
                angular.element('#alreadyHasAppointmentPopUp').modal('show');
            }

            $scope.patientAppoObj.patientId = info.patientId;
            $scope.patientAppoObj.uhidNumber = info.uhidNumber;
            $scope.patientAppoObj.prefixCode = info.prefixCode;
            $scope.patientAppoObj.patientName = info.patientName;
            $scope.patientAppoObj.genderCode = info.genderCode;
            $scope.patientAppoObj.organizationId = $scope.organizationId;
            $scope.patientAppoObj.unitId = $scope.unitId;

            $scope.patientAppoObj.patientCategoryId = info.patientCategoryId;
            $scope.patientAppoObj.dateOfBirth = info.dob;
            $scope.patientAppoObj.appointmentDate = info.appointmentDate;
            $scope.patientAppoObj.appointmentTime = info.appointmentTime;
            $scope.patientAppoObj.age = info.ageString;
            $scope.datepicker = new Date(info.dob);

            $scope.patientAppoObj.appointmentId = info.appointmentId;
            $scope.patientAppoObj.specialityId = info.specialityId;
            $scope.patientAppoObj.isDeptWise = info.deptWiseAppointment;
            $scope.patientAppoObj.specialityName = info.specialityName;
            $scope.patientAppoObj.doctorId = info.doctorId;
            $scope.patientAppoObj.doctorName = info.doctorName;
            $scope.patientAppoObj.isPaidAppointment = info.isPaidAppointment;
            $scope.patientAppoObj.encounterId = info.encounterId;
            $scope.patientAppoObj.typeId = info.typeId;

            $scope.consultingDoctorName = info.doctorName;

			$scope.patientAppoObj.initialAssesmentStarted = info.initialAssesmentStarted;
			$scope.patientAppoObj.orderDetailsId = info.orderDetailsId;

			$scope.patientAppoObj.assignmentStartTime = info.assignmentStartTime;
			$scope.patientAppoObj.assignmentStartBy = info.assignmentStartBy;

            /* Payment buttons */
            $scope.saveEnable = false;
            $scope.saveAndPayEnable = (info.isPaidAppointment !== 'Y');

            if (info.doctorId) {
                $scope.getRegistrationNumber(info.doctorId);
            }

            console.log('patientAppoObj', $scope.patientAppoObj);
        });
};

                            $scope.doctorSpecilityName = null;
							$scope.saveEnable = false;
							$scope.saveAndPayEnable = false;
							$scope.initialAssesment=false;
							$scope.appointmentUniqueId = null;
							$scope.uniqAppoId= null;
							$scope.searchAppointmentsUnique = function() {
								//alert($scope.searchobj.appointmentUniqueId);
								//alert(document.getElementById('appuniq').value);

								if($scope.searchobj.appointmentUniqueId==undefined || $scope.searchobj.appointmentUniqueId==""){
									growl.warning("Please enter appointment unique code...!!",{title : "WARNING!"});
									return false;
								}
								$rootScope.startLoader();
								var data = {
									appointmentUniqueId : $scope.searchobj.appointmentUniqueId,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									isFromKioskToken : 'N'
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL+"/api/global/patientTypeHeadSearchForAppointmentUnique";
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
														$rootScope.stopLoader();
														$scope.uniqAppoId = $scope.searchobj.appointmentUniqueId;
														$scope.searchobj.appointmentUniqueId= null;
													if (response.data.status == "success")
														var info =  response.data.listObject[0];
                                    
													if(info==undefined || info==null){														
														$scope.getInitalAssesmentData($scope.uniqAppoId);
														// $scope.initialAssesment=true;
														// $scope.saveEnable = false;
														// $scope.saveAndPayEnable = false;
													}else{
														$scope.doctorSpecilityName = info.specialityName;
														$scope.saveEnable = true;
														$scope.initialAssesment=false;
													}
													
													if(info.admissionId>0)
													{
														growl.warning("Patient is currently admitted...!!",{title : "WARNING!"});
														return false;
													}
													
													$scope.currentDate = new Date();
													$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY');
													
													$rootScope.startLoader();
													if(info.currentDate!=null && $scope.currentDate==info.currentDate && info.appointmentStatusId==1){
														angular.element('#alreadyHasAppointmentPopUp').modal('show');
													}
													$scope.patientAppoObj.patientId = info.patientId;
													$scope.patientAppoObj.uhidNumber = info.uhidNumber;
													$scope.patientAppoObj.prefixCode = info.prefixCode;
													$scope.patientAppoObj.patientName = info.patientName;
													$scope.patientAppoObj.genderCode = info.genderCode;
													$scope.patientAppoObj.organizationId = $scope.organizationId,
													$scope.patientAppoObj.unitId = info.patientId;
													$scope.patientAppoObj.patientCategoryId= info.patientCategoryId;
													$scope.patientAppoObj.dateOfBirth = info.dob;
													$scope.patientAppoObj.appointmentDate = info.appointmentDate;
													$scope.patientAppoObj.appointmentTime = info.appointmentTime;
													$scope.patientAppoObj.age = info.ageString;
													$scope.datepicker = new Date(info.dob);
													$scope.patientAppoObj.appointmentId = info.appointmentId;
													$scope.patientAppoObj.specialityId = info.specialityId;
													$scope.patientAppoObj.isDeptWise = info.deptWiseAppointment;
													$scope.patientAppoObj.specialityName = info.specialityName;
													$scope.patientAppoObj.doctorId = info.doctorId;
													$scope.patientAppoObj.doctorName = info.doctorName;
													$scope.patientAppoObj.isPaidAppointment = info.isPaidAppointment;
													$scope.patientAppoObj.encounterId = info.encounterId;
													$scope.patientAppoObj.typeId = info.typeId;
													$scope.consultingDoctorName = info.doctorName;
													$rootScope.stopLoader();
													$scope.saveEnable = false;
													if(info.isPaidAppointment=='Y'){
														// $scope.saveAndPayEnable = true;
														$scope.saveAndPayEnable = false;
													}else{
														// $scope.saveAndPayEnable = false;
														$scope.saveAndPayEnable = true;
													}
													
													console.log('patientAppoObj',$scope.patientAppoObj);
													if(info.doctorId!=undefined && info.doctorId!=null){
														$scope.getRegistrationNumber(info.doctorId);
													}
												
													
													
												});

							}
							
							
							$scope.selectGender = function(id)
							{
								if(id=="" || id==undefined){
									$scope.saveEnable = true;
									$scope.saveAndPayEnable = false;
								}
								
								
								var genId = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].id;
								
								var prefixCode = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].prefixDesc;
								
								$scope.patientObj.prefixCode = prefixCode;
								$scope.patientObj.genderId = genId.toString();
								
								if(id >0){
									$scope.saveEnable = false;
									$scope.saveAndPayEnable = true;
								}
							}
							
							//get calling code by using country id start
							$scope.getCallingCode=function(){
								var data = {
										countryId : $scope.unitCountryId,
										organizationId : $scope.organizationId
									};
								var URI = BASE_URL + ROOT_URL
								+ GETSINGLECOUNTRY;
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success" && response.data.listObject.length > 0) 
											{
												$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
											} 
										});
							}
							$scope.getCallingCode();
							//get calling code by using country id end
							
							$scope.selectPatientId = function(info, model,
									label) {
								
								if(info.admissionId > 0)
								{
									 switch (parseInt(info.visitTypeId)) {
							            case 2:
							            	$scope.patientName = '';
								            	growl.warning("Patient is already admitted in IP...!!",{
						    						title : WARNING_MSG
						    					});
								            	return false;
							                break;
							            case 3:
							            	$scope.patientName = '';
							            	growl.warning("Patient is already admitted in DAY-CARE...!!",{
					    						title : WARNING_MSG
					    					});
							            	return false;
							                break;
							            case 4:
							            	growl.warning("Patient is already admitted in ER...!!",{
					    						title : WARNING_MSG
					    					});
							            	return false;
							                break;
							            default:
						
							        }
								}
							
						else{
								//code for absconding patient Start 						
									$scope.abscondPatientFlag="";
									if(info.isAbsconded==undefined || info.isAbsconded==null || info.isAbsconded=='N'){
										
										$scope.currentDate = new Date();
										$scope.currentDate = moment($scope.currentDate)
												.format('DD-MM-YYYY');
										
										$rootScope.startLoader();
										if(info.currentDate!=null && $scope.currentDate==info.currentDate && info.appointmentStatusId==1){
											angular.element('#alreadyHasAppointmentPopUp').modal('show');
										}
										$scope.patientObj.patientId = info.patientId;
										$scope.patientObj.prefixId = info.prefixId;
										$scope.patientObj.prefixCode = info.prefixCode;
										$scope.patientObj.firstName = info.firstName;
										$scope.patientObj.middleName = info.middleName;
										$scope.patientObj.lastName = info.lastName;
										$scope.patientObj.genderId = info.genderId;
										$scope.patientObj.countryCallingCode = info.countryCallingCode;
										$scope.patientObj.email = info.email;
										$scope.patientObj.maritalStatusId = info.maritalStatusId;
										$scope.patientObj.mobileNumber = info.mobileNumber;
										$scope.patientObj.address = info.address;
										$scope.patientObj.countryId = info.countryId;
										$scope.patientObj.stateId = info.stateId;
										$scope.patientObj.districtId = info.districtId;
										$scope.patientObj.cityId = info.cityId;
										$scope.patientObj.areaId = info.areaId;
										$scope.patientObj.zipCode = info.zipCode;
										$scope.patientObj.organizationId = $scope.organizationId,
										$scope.patientObj.unitId = info.patientId;
										$scope.patientObj.updatedBy = info.patientId;
										$scope.patientObj.createdBy = info.patientId;
										$scope.patientObj.patientCategoryId= info.patientCategoryId!=null ? info.patientCategoryId : $scope.defaultPatientCategoryId;
										$scope.patientObj.dateOfBirth = info.dob;
										$scope.patientObj.uhidNumber = info.uhidNumber;
										$scope.age = info.ageString;
										$scope.datepicker = new Date(info.dob);
										$scope.calculateAge();
										$scope.patientObj.appointmentId = info.appointmentId;
										$scope.patientObj.identificationTypeId = info.identificationTypeId;
										$scope.patientObj.identificationNumber = info.identificationNumber;
										$scope.saveEnable = false;
										$scope.hideShowSearch = true;

										if(info.typeId==5){
											$scope.saveAndPayEnable = true;
										}else{
											$scope.saveAndPayEnable = false;
										}
										$rootScope.stopLoader();	
									}else{
										$scope.abscondPatientFlag='Y';
										$scope.infoPatient=info;
										angular.element('#abscondedPatientPopUp').modal('show');
									}
								//code for absconding patient End
							}//ELSE Close
							
							
							$scope.showForgetPasswordModal(info.mobileNumber);
							}
							
							
							$scope.getRegistrationNumber = function(doctorId){
								var data = {
										doctorId : doctorId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									};
								var URI = BASE_URL + ROOT_URL + "/api/global/getDoctorRegistrationNumber";
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success" && response.data.object!=null) 
											{
												$scope.registrationNumber = response.data.object.registrationNumber;
											} 
										});
							}
							
							$scope.selectedPatientIdPreBooked = function(info, model,
									label) 
							{
								
								if(info.admissionId>0)
								{
									growl.warning("Patient is currently admitted...!!",{title : "WARNING!"});
									return false;
								}
								
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY');
								
								$rootScope.startLoader();
								if(info.currentDate!=null && $scope.currentDate==info.currentDate && info.appointmentStatusId==1){
									angular.element('#alreadyHasAppointmentPopUp').modal('show');
								}
								$scope.patientAppoObj.patientId = info.patientId;
								$scope.patientAppoObj.uhidNumber = info.uhidNumber;
								$scope.patientAppoObj.prefixCode = info.prefixCode;
								$scope.patientAppoObj.patientName = info.patientName;
								$scope.patientAppoObj.genderCode = info.genderCode;
								$scope.patientAppoObj.organizationId = $scope.organizationId,
								$scope.patientAppoObj.unitId = info.patientId;
								$scope.patientAppoObj.patientCategoryId= info.patientCategoryId;
								$scope.patientAppoObj.dateOfBirth = info.dob;
								$scope.patientAppoObj.appointmentDate = info.appointmentDate;
								$scope.patientAppoObj.appointmentTime = info.appointmentTime;
								$scope.patientAppoObj.age = info.ageString;
								$scope.datepicker = new Date(info.dob);
								$scope.patientAppoObj.appointmentId = info.appointmentId;
								$scope.patientAppoObj.specialityId = info.specialityId;
								$scope.patientAppoObj.isDeptWise = info.deptWiseAppointment;
								$scope.patientAppoObj.specialityName = info.specialityName;
								$scope.patientAppoObj.doctorId = info.doctorId;
								$scope.patientAppoObj.doctorName = info.doctorName;
								$scope.patientAppoObj.isPaidAppointment = info.isPaidAppointment;
								$scope.patientAppoObj.encounterId = info.encounterId;
								$scope.patientAppoObj.typeId = info.typeId;
								$scope.consultingDoctorName = info.doctorName;
								$rootScope.stopLoader();
								$scope.saveEnable = false;
								if(info.isPaidAppointment=='Y'){
									$scope.saveAndPayEnable = true;
								}else{
									$scope.saveAndPayEnable = false;
								}
								
								console.log('patientAppoObj',$scope.patientAppoObj);
								if(info.doctorId!=undefined && info.doctorId!=null){
									$scope.getRegistrationNumber(info.doctorId);
								}
							}
							//code for creating appointment for absconded patient start
							  $scope.creatingEncounterForAbscondedPatient=function(){
								  var info=$scope.infoPatient;
								  if($scope.infoPatient!="" && $scope.infoPatient!=undefined && $scope.infoPatient!=null){
										$scope.currentDate = new Date();
										$scope.currentDate = moment($scope.currentDate)
												.format('DD-MM-YYYY');
										
										$rootScope.startLoader();
										if(info.currentDate!=null && $scope.currentDate==info.currentDate && info.appointmentStatusId==1){
											angular.element('#alreadyHasAppointmentPopUp').modal('show');
										}
										$scope.patientObj.patientId = info.patientId;
										$scope.patientObj.prefixId = info.prefixId;
										$scope.patientObj.firstName = info.firstName;
										$scope.patientObj.middleName = info.middleName;
										$scope.patientObj.lastName = info.lastName;
										$scope.patientObj.genderId = info.genderId;
										$scope.patientObj.countryCallingCode = info.countryCallingCode;
										$scope.patientObj.email = info.email;
										$scope.patientObj.maritalStatusId = info.maritalStatusId;
										$scope.patientObj.mobileNumber = info.mobileNumber;
										$scope.patientObj.address = info.address;
										$scope.patientObj.countryId = info.countryId;
										$scope.patientObj.stateId = info.stateId;
										$scope.patientObj.districtId = info.districtId;
										$scope.patientObj.cityId = info.cityId;
										$scope.patientObj.areaId = info.areaId;
										$scope.patientObj.zipCode = info.zipCode;
										$scope.patientObj.organizationId = $scope.organizationId,
										$scope.patientObj.unitId = info.patientId;
										$scope.patientObj.updatedBy = info.patientId;
										$scope.patientObj.createdBy = info.patientId;
										$scope.patientObj.patientCategoryId= info.patientCategoryId;
										$scope.patientObj.dateOfBirth = info.dob;
										$scope.patientObj.uhidNumber = info.uhidNumber;
										$scope.age = info.ageString;
										$scope.datepicker = new Date(info.dob);
										$scope.patientObj.appointmentId = info.appointmentId;
										$scope.saveEnable = false;
										$scope.hideShowSearch = true;
	
										if(info.typeId==5){
											$scope.saveAndPayEnable = true;
										}else{
											$scope.saveAndPayEnable = false;
										}
										$rootScope.stopLoader();
								  }
							  }
							//code for creating appointment for absconded patient end
							  $scope.ageDay = 0;
								$scope.ageMonth = 0;
								$scope.ageYear = 0;
								$scope.calDOBFlag = 0;
							$scope.oldAgge = 0;
							/*$scope.calculateAge= function()
							{
								var dtToday = moment(new Date()).format('MM/DD/YYYY');
								var dt = moment($scope.datepicker).format('MM/DD/YYYY');
								var dtToday1=new Date(dtToday).getTime();
								var dt1=new Date(dt).getTime();
								if(dt1 > dtToday1)
									{
										growl.warning("DOB should be less then or equal to today's date",{
											title : WARNING_MSG
											});
										$scope.datepicker = new Date();
										$scope.age = $rootScope.getAge(dtToday);
										$scope.oldAgge = $scope.age;
										
											return false;
									
									}
								else{
									$scope.age = $rootScope.getAge(dt);
									if(dt == moment(new Date()).format('MM/DD/YYYY')){
										$scope.age = 1;
									}
									$scope.oldAgge = $scope.age;
								}
								 
							}
							
							$scope.myFunct = function(age) {
								if($scope.oldAgge == age){
									
								}
								else{
								 if($('#datepicker').val()!=""){
									var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;	
								    $scope.datepicker = new Date("01/01/"+finalYear);
								}else{
									var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;
								    $scope.datepicker = new Date("01/01/"+finalYear);
								}
								}
							    //alert(finalYear);
							  
							}*/

							
							$scope.datediff = function(dateFrom, dateTo)
							{
							    var from = {
							        d: dateFrom.getDate(),
							        m: dateFrom.getMonth(),
							        y: dateFrom.getFullYear()
							    };

							    var to = {
							        d: dateTo.getDate(),
							        m: dateTo.getMonth(),
							        y: dateTo.getFullYear()
							    };

							    var daysFebruary = to.y % 4 != 0 || (to.y % 100 == 0 && to.y % 400 != 0)? 28 : 29;
							    var daysInMonths = [31, daysFebruary, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

							    if (to.d < from.d) {
							        to.d   += daysInMonths[parseInt(to.m)];
							        from.m += 1;
							    }
							    if (to.m < from.m) {
							        to.m   += 12;
							        from.y += 1;
							    }

							    /*return {
							        days:   to.d - from.d,
							        months: to.m - from.m,
							        years:  to.y - from.y
							    };*/
                                if(to.d == from.d && to.m == from.m && to.y== from.y && $scope.datepicker!=undefined 
                                        && $scope.datepicker!=null && $scope.datepicker.opened == undefined){
                                     return {
                                            days:   1,
                                            months: to.m - from.m,
                                            years:  to.y - from.y
                                        };
                                }else{
                                     return {
                                            days:   to.d - from.d,
                                            months: to.m - from.m,
                                            years:  to.y - from.y
                                        };
                                }
							}
							
							$scope.newAge="";
							$scope.calDOBFlag = 0;
							$scope.calculateAge= function()
							{
								if($scope.datepicker == undefined || $scope.datepicker == null || $scope.datepicker == ''){
									 $scope.age = "";
									   growl.warning("Date is invalid",{
											title : "warning"
										});
										$scope.datepicker = new Date();
									
									   return false;
								}else{
								if($scope.datepicker > new Date())
									{
											       $scope.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepicker = new Date();
												
												   return false;
									}else{
										
										var dt = moment($scope.datepicker).format('MM/DD/YYYY');
										var nDt = moment(new Date()).format('MM/DD/YYYY');
										
										
										var array = dt.split('/');
										var array1 = nDt.split('/');
									   
										var curday = array1[1];
										var curmon =array1[0];
										var curyear =array1[2];
										
										var calday =array[1];
										var calmon = array[0];
										var calyear = array[2];
										
											var curd = new Date(curyear,curmon-1,curday);
											var cald = new Date(calyear,calmon-1,calday);
											
									                    var diff =  Date.UTC(curyear,curmon-1,curday,0,0,0) - Date.UTC(calyear,calmon-1,calday,0,0,0);
									    
									                    var dife = $scope.datediff(cald,curd); 
									                    
												      if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 1){
												    	  $scope.ageYear = dife.years;
									                    	 $scope.ageMonth = dife.months;
									                    	 $scope.ageDay = dife.days;
									                    	 if($scope.ageYear!=undefined && $scope.ageYear!=null && $scope.ageYear!="" && $scope.ageYear!=0
									                    			 && $scope.ageMonth!=undefined && $scope.ageMonth!=null && $scope.ageMonth!="" && $scope.ageMonth!=0
									                    			 && $scope.ageDay!=undefined && $scope.ageDay!=null && $scope.ageDay!="" && $scope.ageDay!=0){
									                    		 $scope.calDOBFlag = 1;
									                    	 }
												      }
												     
									}
							}
							
							}
							
							
							$scope.myFunctDay = function(day) {
								if(day>31){
									$scope.ageDay = 0;
									growl.warning("Day should be less then 31 ",{
										title : "warning"
										});
								}else{
									var cntOfYears = 0;
									var cntOfMonths = 0;
									var cntOfDays = 0;
									
									if($scope.ageDay == "" || $scope.ageDay == null || $scope.ageDay == undefined){
										$scope.ageDay = 0;
									}else{
										cntOfDays = $scope.ageDay;
									}
									

									if($scope.ageMonth == "" || $scope.ageMonth == null || $scope.ageMonth == undefined){
										$scope.ageMonth = 0;
									}else{
										cntOfMonths = $scope.ageMonth;
									}
									
									if($scope.ageYear == "" || $scope.ageYear == null || $scope.ageYear == undefined){
										$scope.ageYear = 0;
									}else{
										cntOfYears = $scope.ageYear;
									}
									
									//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays); && (event.keyCode === 84)
									if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
										$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
											if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
												&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
												$scope.calDOBFlag = 2;
											}
									}
									
								}
								  
							}
							$scope.myFunctMonth = function(month) {
								if(month>12){
									$scope.ageMonth = 0;
									growl.warning("Month should be less then 12 ",{
										title : "warning"
										});
								}else{
									var cntOfYears = 0;
									var cntOfMonths = 0;
									var cntOfDays = 0;
									
									if($scope.ageDay == "" || $scope.ageDay == null || $scope.ageDay == undefined){
										$scope.ageDay = 0;
									}else{
										cntOfDays = $scope.ageDay;
									}
									

									if($scope.ageMonth == "" || $scope.ageMonth == null || $scope.ageMonth == undefined){
										$scope.ageMonth = 0;
									}else{
										cntOfMonths = $scope.ageMonth;
									}
									
									if($scope.ageYear == "" || $scope.ageYear == null || $scope.ageYear == undefined){
										$scope.ageYear = 0;
									}else{
										cntOfYears = $scope.ageYear;
									}
									
									//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays);
									if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
										$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
										if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
											&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
											$scope.calDOBFlag = 2;
										}
									}
									
								}
								  
							}
							$scope.myFunctYear = function(age) {

								if(age>=$scope.minAge && age <= $scope.maxAge){
									                                    if($('#datepicker').val()!=""){
                                                                           var dt = moment($scope.datepicker).format('MM/DD/YYYY');
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
                                                                                $scope.datepicker = new Date(m +'/' +d +'/'  +finalYear);
                                                                                }
                                                                              }else{
                                                                        var currentYear = (new Date()).getFullYear();
                                                                        var finalYear = currentYear - age;
                                                                        $scope.datepicker = new Date(m +'/' +d +'/'  +finalYear);
                                                                            }
									var cntOfYears = 0;
									var cntOfMonths = 0;
									var cntOfDays = 0;
									
									if($scope.ageDay == "" || $scope.ageDay == null || $scope.ageDay == undefined){
										$scope.ageDay = 0;
									}else{
										cntOfDays = $scope.ageDay;
									}
									

									if($scope.ageMonth == "" || $scope.ageMonth == null || $scope.ageMonth == undefined){
										$scope.ageMonth = 0;
									}else{
										cntOfMonths = $scope.ageMonth;
									}
									
									if($scope.ageYear == "" || $scope.ageYear == null || $scope.ageYear == undefined){
										$scope.ageYear = 0;
									}else{
										cntOfYears = $scope.ageYear;
									}
									
									//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays);
									if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
										$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
										if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
											&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
											$scope.calDOBFlag = 2;
										}
									}
									
								}else{
									$scope.ageYear = "";
									growl.warning("Year should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
										title : "warning"
										});
								}
								  
							}

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveSeftPatientRegistration();
							}

							$scope.saveSeftPatientRegistration = function(saveFlag)
							{
						        	$scope.storePatientDetails(saveFlag);
						        		
						     }
						        
						    /***============================================Code Image Upload==============================================*/
						    /**PatientImages*/
//						    $scope.patientImage1;
						    
						    
//						    $scope.convertBase64ToImage = function (dataurl, filename) {
//							    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
//							        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
//							    while(n--){
//							        u8arr[n] = bstr.charCodeAt(n);
//							    }
//							    return new File([u8arr], filename, {type:mime});
//							}
//						    
//						    $scope.uploadPatientImage = function(inputFile,patientId,orgId,unitId)
//						    {
//						    	    var file = inputFile;
//							        
//							        fd.append('file', file );
//									var URI1 = BASE_URL + ROOT_URL + PATIENT_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+patientId;
//									GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
//									{	
//										if (response.data.status == 'success')
//										{
//											$rootScope.stopLoader();
//										} else if(response.data.status == 'error')
//										{
//											$rootScope.stopLoader();
//										}
//									});
//						    }
						   
						    /***============================================Code Image Upload==============================================*/
							$scope.storePatientDetails = function(saveFlag)
							{
								 if($scope.patientObj.firstName == '' || $scope.patientObj.firstName == null ||  $scope.patientObj.firstName == undefined){
										growl.warning(
												'Please enter first name...!!',
												{
													title : 'Warning!'
												});
										return false;
										
									}else if($scope.patientObj.prefixId == '' || $scope.patientObj.prefixId == null || $scope.patientObj.prefixId == undefined){
										growl.warning(
												'Please select prefix...!!',
												{
													title : 'Warning!'
												});
										return false;
										
									}else if($scope.patientObj.genderId == '' || $scope.patientObj.genderId == null || $scope.patientObj.genderId == undefined){
										growl.warning(
												'Please select gender...!!',
												{
													title : 'Warning!'
												});
										return false;
										
									}else if($('#datepicker').val() == '' || $('#datepicker').val() == null || $('#datepicker').val() == undefined){
										growl.warning(
												'Please enter DOB...!!',
												{
													title : 'Warning!'
												});
										return false;
									}else if($scope.patientObj.mobileNumber == '' || $scope.patientObj.mobileNumber == null || $scope.patientObj.mobileNumber == undefined){
										growl.warning(
												'Please enter contact number...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
									
									var data = null;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
									$scope.patientObj.createdDate = $scope.currentDate;
									$scope.patientObj.updatedDate = $scope.currentDate;
									$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
									$scope.patientObj.middleName =  ($scope.patientObj.middleName!="" || $scope.patientObj.middleName!=undefined || $scope.patientObj.middleName!=null ? $rootScope.capitalize_Words($scope.patientObj.middleName) : "");
									$scope.patientObj.lastName =    ($scope.patientObj.lastName!="" || $scope.patientObj.lastName!=undefined || $scope.patientObj.lastName!=null ? $rootScope.capitalize_Words($scope.patientObj.lastName): "");
									var dob= $('#datepicker').val();
									
									$scope.patientObj.dob = moment($scope.datepicker=="" ? $('#datepicker').val() : $scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
									
									 console.log($scope.patientObj);
									 $scope.$broadcast('show-errors-check-validity');
						        	  if($scope.patientRegistrationform.$valid)
						        	 {
						        		  
									 var contryCode=$scope.patientObj.countryCallingCode;
									 // validating country code
									 var regEx = /^[+-]?\d+$/;
									      if(!regEx.test(contryCode)){
									    	  growl.warning("Please enter valid country code",{
													title : 'warning'
													});
													return false;
									      } 
									 
							        	var mobileNo=$scope.patientObj.mobileNumber;
//							        	if(contryCode==91){
//							        		if(mobileNo != undefined && mobileNo.length==10){
//
//												}else{
//													growl.error("Please enter valid mobile number",{
//														title : ERROR_MSG
//														});
//														$('#mobileNo').focus();
//														return false;
//							        		}
//							        	}else{
								        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
								        			
												}else{
													growl.error("Please enter valid mobile number",{
														title : ERROR_MSG
														});
														$('#mobileNo').focus();
														return false;
							        		}
//							        	}
//									return false;
							        	
									$rootScope.startLoader();
									// update abscond patient as normal patient start
									if($scope.abscondPatientFlag!=undefined  && $scope.abscondPatientFlag!="" && $scope.abscondPatientFlag!=null && $scope.abscondPatientFlag=='Y' && $scope.patientObj.patientId!=null){
									var URI = BASE_URL + ROOT_URL+ "/api/global/updatePatientAsAbscound";					
									var obj={
											patientId:$scope.patientObj.patientId
									}		
									console.log('abscond obj',obj);
									GenericService.serviceAction("POST", URI, obj).then(
											function(response) {	
												if (response.data.status == "success") {
													$scope.abscondPatientFlag="";
													$scope.infoPatient="";
												}
											});
									}
									// update abscond patient as normal patient end
									 if(saveFlag==1)
									 {
											 	
										 		if($scope.patientObj.patientId==null)
												{
												
										 			$scope.patientObj.isFromSelf = 'Y';
										 			$scope.patientObj.appointmentDate=moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
										 			
													//return false;
													
													var URI = BASE_URL + ROOT_URL
											 		+ ADDPATIENTREGISTRATION;

											 	BillingGenericService
											 		.serviceAction(METHOD_POST,
											 				URI, $scope.patientObj,
											 				NOTIFICATION_MSG_STATUS_FALSE)
											 		.then(
											 				function(response) {
											 					console
											 							.log(response);
											 					//$rootScope.startLoader();
											 					if (response.data.status == "success")
											 					{
											 						$scope.tokenNumber = response.data.object.tokenNumber;
											 						
											 						if($scope.snapshotData!=undefined && $scope.snapshotData!="")
																	{
																		if(response.data.object.patientId!=undefined && response.data.object.patientId!=null)
																		{
																			var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
																			  $scope.uploadPatientImage(file, response.data.object.patientId, $scope.organizationId, $scope.unitId);
																			 
																		}
																	  
																	}
											 						
																	$rootScope.stopLoader();
																	angular.element('#tokenDisplayModal').modal('show');
											 					}else if(response.data.code==409){
											 						angular.element('#patientAlreadyExist').modal('show');
											 					}
											 				});
												}
										 		else{
											 		data = {
															isFromKioskToken:'Y',
													        createdDate: $scope.currentDate,
													        unitId : $scope.unitId,
															organizationId : $scope.organizationId,
															createdBy : $scope.userId,
															appointmentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
															patientId : ($scope.patientObj.patientId!=null ? $scope.patientObj.patientId : 0)
															
											 		}
											 		var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/kiosk";
													console.log(data);
											 		BillingGenericService
													.serviceAction(METHOD_POST,
															URI, data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console
																		.log(response);
																//$rootScope.startLoader();
																if (response.data.status == "success")
																{
																	/*growl.success(response.data.message,{
																		title : SUCCESS
																		});*/
																	$scope.tokenNumber = response.data.object.tokenNumber;
//																	
																	if($scope.snapshotData!=undefined && $scope.snapshotData!="")
																	{
																		if(data.patientId!=undefined && data.patientId!=null)
																		{
																			var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
																			  $scope.uploadPatientImage(file, data.patientId, $scope.organizationId, $scope.unitId);
																		}
																	  
																	}
																	
																	
																	$rootScope.stopLoader();
																	angular.element('#tokenDisplayModal').modal('show');
																	$scope.init();
																}else if (response.data.code == 409)
																	{
																		$rootScope.stopLoader();
																		$scope.alreadyExistKioskAppointment = response.data.message;
																		angular.element('#alreadyExistAppointmentKiosk').modal('show');
																	}
																
															});	
											
											 		
											 		
												}
												
									 }else{
										 
										 if($scope.patientObj.patientId==null){
											 $scope.patientObj.registrationTypeId=1;
											 
											 var URI = BASE_URL + ROOT_URL
										 		+ "/api/global/patientRegistration";

										 	BillingGenericService
										 		.serviceAction(METHOD_POST,
										 				URI, $scope.patientObj,
										 				NOTIFICATION_MSG_STATUS_FALSE)
										 		.then(
										 				function(response) {
										 					console
										 							.log(response);
										 					//$rootScope.startLoader();
										 					if (response.data.status == "success")
										 					{
										 						if(response.data.code==409)
										 							{
										 								$rootScope.stopLoader();
										 								angular.element('#patientAlreadyExist').modal('show');
										 							}
										 						else{
										 							$scope.patientObj.patientId=response.data.object;
                                                                    if(response.data.resObject!=null && response.data.resObject!=undefined){
																		$scope.patientObj.uhidNumber=response.data.resObject.uhidNumber;
																	}

										 							$scope.patientObj.isFromSelf = 'Y';
										 							$scope.patientObj.patientCategoryId = $scope.defaultPatientCategoryId;
																	   $scope.patientObj.appointmentDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
																	   $scope.patientObj.snapshotData =  $scope.snapshotData;
																	   $rootScope.stopLoader();
																	   $scope.dept=true;
																	   $scope.doc=false;
																	   $scope.slot=false;
																	   $scope.saveAndPayEnable = true;
																	//    $state.go("walkinAppointment", {
																	// 		patientDetailsObj: $scope.patientObj,
																	// 	});
																	 $scope.showAppointmentPopUp();
										 						
										 						}  
										 					}else{
										 					
										 						$scope.back();
										 						
										 					}
										 				});
											 
											 
										 }else{
											   $scope.patientObj.isFromSelf = 'Y';
											   $scope.patientObj.appointmentDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
											   $rootScope.stopLoader();
											   $scope.dept=true;
											   $scope.doc=false;
											   $scope.slot=false;
											   $scope.saveAndPayEnable = true;
											   $scope.patientId = $scope.patientObj.patientId;
											   $scope.patientObj.snapshotData =  $scope.snapshotData;
											   $state.go("walkinAppointment", {
													patientDetailsObj: $scope.patientObj
											   	})
										 }
										 
									 }
						        }
							}
							$scope.goToPreBooked = function(appointmentId)
							{
								
								if(appointmentId != undefined){
								$(".modal-backdrop").hide();
								$rootScope.startLoader();
								var marArrDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
								 
								 var URI = BASE_URL + ROOT_URL
									+ "/api/opd/markArrival/"+appointmentId+"/"+marArrDate+"/"+$scope.unitId;
								 
								BillingGenericService
										.serviceAction(METHOD_GET,
												URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response)
												{
													console.log("response",response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
														{
														$scope.tokenNumber = response.data.object;
														$rootScope.stopLoader();
														angular.element('#tokenDisplayModal').modal('show');
															$scope.init();
														}
													else if(response.data.code == "409")
														{
															$rootScope.stopLoader();
															angular.element('#pleaseTryAgain').modal('show');
														}
												});
								}
								else{
									console.log('$scope.patientAppoObjPayment',$scope.patientAppoObjPayment);
									
									$scope.patientObj = $scope.patientAppoObjPayment
									console.log('$scope.patientObj',$scope.patientObj);
									$scope.scheduleNormalAppointment();
								}
							
							}
							
							$scope.markArrival = function()
							{
								$rootScope.startLoader();
								var marArrDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
								var URI = null;
								
								if($scope.patientAppoObj.typeId==1 && $scope.patientAppoObj.encounterId !=null && $scope.patientAppoObj.isPaidAppointment=='Y')
								{
									
									URI = BASE_URL + ROOT_URL	+ "/api/opd/markArrival/paidAppointment";
									var data = {
												appointmentId:$scope.patientAppoObj.appointmentId,
												currentDate : marArrDate,
										        unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												encounterId : $scope.patientAppoObj.encounterId,
												deptWiseAppointment : $scope.patientAppoObj.isDeptWise,
												doctorId : $scope.patientAppoObj.doctorId,
												specialityId : $scope.patientAppoObj.specialityId,
												isPaidAppointment : $scope.patientAppoObj.isPaidAppointment
											
							 				}
									BillingGenericService
									.serviceAction(METHOD_POST,
											URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response)
											{
												console.log("response",response);
												//$rootScope.startLoader();
												if (response.data.status == "success")
													{
													$scope.tokenNumber = response.data.object;
													$rootScope.stopLoader();
													angular.element('#tokenDisplayModal').modal('show');
														$scope.init();
													}
												else if(response.data.code == "409")
												{
													$rootScope.stopLoader();
													angular.element('#pleaseTryAgain').modal('show');
												}
											});
								}
								else
								{
									URI = BASE_URL + ROOT_URL	+ "/api/opd/markArrival/"+$scope.patientAppoObj.appointmentId+"/"+marArrDate+"/"+$scope.unitId;
									
									 console.log('URI',URI);
										// return false;	 
									BillingGenericService
											.serviceAction(METHOD_GET,
													URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response)
													{
														console.log("response",response);
														//$rootScope.startLoader();
														if (response.data.status == "success")
															{
															$scope.tokenNumber = response.data.object;
															$rootScope.stopLoader();
															angular.element('#tokenDisplayModal').modal('show');
																$scope.init();
															}
														else if(response.data.code == "409")
														{
															$rootScope.stopLoader();
															angular.element('#pleaseTryAgain').modal('show');
														}
													});
								}
						
							}
							
							$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
							$scope.hitMpesaApi = false;
							$scope.skipMpesaValidation = true;
							$scope.checkmpesaDetails=function(){ 
								
								var data ={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										createdBy : $scope.userId,
										createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
										updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
										updatedBy:$scope.userId,										
										uhid: $scope.uhidNumber,
										mpesaSendermobile:$scope.mpesaObject.mobileNo,
										mpesaAmount:$scope.mpesaObject.mpesaAmount,
										mpesaTxcode:$scope.mpesaObject.transactionId
								};
								console.log('mpesa data',data);
								//return false;
								$rootScope.startLoader();
								var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
								BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									
									if (response.data.status == "success")
										{
										
											$rootScope.stopLoader();
											//added by dinesh for multimode payment
											$scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount);
											 $scope.payeeDueAmtFinal=parseFloat($scope.patientAppoObjPayment.payeeDueAmtFinal);
											$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
											$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
											
											$scope.isBillList = 0;
											$scope.page='billList';
											paymentCollFactory.addPaymentDetails($scope);
											
										}
									else if(response.data.status == "error"){
										
										growl.error(response.data.message,{
											title : ERROR_MSG
										});
										
										$scope.enableSettle = true;
										$scope.mpesaObject.mpesaAmount=null;
										angular.element('#invalidTransaction').modal('show');
										$rootScope.stopLoader();
									}
									
									
									
								});
								
							}
							
							
							
							
							$scope.checkoutRequestId = null;
							$scope.validateMpesaAmount = "";
							$scope.mobileNumber = "";
							$scope.countryCallingCode = "";
							$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
								
								
								if(validateMpesaAmount == undefined || validateMpesaAmount == null){
									
									growl.warning("Please Enter Amount",{
										title : "warning!!!"
									});
									
								}else if(parseInt(validateMpesaAmount) > $scope.patientAppoObjPayment.dueAmount){
									
									growl.warning("Amount should be equel or less than due amount...!!",{
										title : "warning!!!"
									});
									
								}else{
									var data ={
											organizationId :$scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											uhidNumber:$scope.patientAppoObjPayment.uhidNumber,
											phoneNumber:parseInt(countryCallingCode+""+mobileNumber) ,
											partyA:mobileNumber,
											amount:parseInt(validateMpesaAmount)
									};
								
									if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
											(countryCallingCode != undefined && countryCallingCode != null && countryCallingCode != "")){
										
									$rootScope.startLoader();
									
									var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
									BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											$scope.checkoutRequestId = response.data.object.checkoutRequestId;
											growl.success(response.data.message,{
												title : SUCCESS_MSG
											});
											$scope.hitMpesaApi = true;
											$rootScope.stopLoader();
											angular.element('#paymentReqModalInfo').modal('hide');
											}
										else if(response.data.status == "error"){
											
											growl.error(response.data.message != null ? response.data.message : "There is problem in Mpesa payment gatway",{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
										$rootScope.stopLoader();
									});
									}else{
										growl.error("Please enter mobile code and mobile number. ",{
											title : ERROR_MSG
										});
										
										$rootScope.stopLoader();
									}
								}
								
								
								
							}
							
						$scope.validateSTKPushPayment = function(){
								
								var data ={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										uhidNumber:$scope.patientAppoObjPayment.uhidNumber,
										checkoutRequestId:$scope.checkoutRequestId
										
								};
								$rootScope.startLoader();
								var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
								BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									
									if (response.data.status == "success")
										{
										$scope.mpesaObject.mpesaAmount = response.data.object.amount;
										$scope.mpesaObject.transactionId = response.data.object.transactionId;
										$scope.mpesaObject.resTransactionId = response.data.object.transactionId;
										$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
						                if($scope.mpesaObject.transactionId != null ){
						                    $scope.hitMpesaApi = false;
						                    $scope.isBillList = 0;
						                    $scope.payeeDueAmtFinal = $scope.patientAppoObjPayment.dueAmount;
											$scope.page='billList';
											$scope.skipMpesaValidation = true;
											paymentCollFactory.addPaymentDetails($scope);
						                    }
										growl.success(response.data.message,{
											title : SUCCESS_MSG
										});
										$rootScope.stopLoader();
										}
									else if(response.data.status == "error"){
										
										growl.error(response.data.message != null ? response.data.message :"Please wait",{
											title : ERROR_MSG
										});
										$rootScope.stopLoader();
									}
									
								});
								
							}
							

							$interval(refreshPage,1000000);
							function refreshPage()
							{
								if($scope.hitMpesaApi == true){
									$scope.validateSTKPushPayment();
								}

							}
							

							$scope.cancelPaymentReqModal = function(){
								angular.element('#paymentReqModalInfo').modal('hide');
							}
							
							
							
							
							$scope.validateSlot = function()
							{
								var endTime = moment(slobObj.originalFromTime, 'hh:mm');
							     var startTempTime = moment().format("HH:mm");
							     var startTime = moment(moment(moment().add(1,'hours').toDate()).format('HH:mm'));
							    var flag = moment(slobObj.fromTimeString).isBefore(moment().format('YYYY-MM-DD'))
									 console.log(flag);
							}
							
							$scope.scheduleNormalAppointment = function()
							{
								
								var newDate = "";
								if($scope.appointmentDate!=null){
								var array = $scope.appointmentDate.split('-');
								newDate = array[2]+'-'+array[1]+'-'+array[0]+" 00:00:00";
								}
								
								$rootScope.startLoader();
								var data = {
										slotId : ($scope.slotId !=null ? $scope.slotId : $scope.patientObj.slotId),
										patientId : $scope.patientObj.patientId,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										status : 'A',
										appointmentTypeId : 1,
										priorityId : 1,
										slotStatusId : 4,
										generateToken : 'N',
										appointmentDate : ($scope.appointmentDate!=null ? newDate :$scope.patientObj.appointmentDate2+" 00:00:00"), //yyyy-M-dd
										arrivalTime : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								}
								if($scope.patientObj.patientId==null)
								{
									data.patientId = 0,
									data.prefixId = $scope.patientObj.prefixId,
									data.firstName = $scope.patientObj.firstName, 
									data.middleName = $scope.patientObj.middleName,
									data.lastName = $scope.patientObj.lastName,
									data.genderId = $scope.patientObj.genderId,
									data.countryCallingCode = $scope.patientObj.countryCallingCode,
									data.registrationTypeId = 5,
									data.mobileNumber = $scope.patientObj.mobileNumber,
									//data.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss')
									data.dob =$scope.patientObj.dob
								}
								
								$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
								
								if($scope.orignalTimeForSlot != undefined){
									if($scope.slotCheckTimeAddOneHours >= $scope.orignalTimeForSlot)
										{
											data.generateToken  = 'Y';
										}
								}
								if($scope.patientAppoObjPayment != undefined){
									if($scope.slotCheckTimeAddOneHours >= $scope.patientAppoObjPayment.originalFromTime)
									{
										data.generateToken  = 'Y';
									}
								}								
							 console.log('scheduleNormalData',data)
							 //return false;
							 var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/kiosk";
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console
															.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
													{
														if(data.generateToken == 'Y')
														{
															$scope.tokenNumber = response.data.object.tokenNumber;
															$rootScope.stopLoader();
															angular.element('#tokenDisplayModal').modal('show');
															//init();
														}else{
															angular.element('#cannotGenerateToken').modal('show');
														}
														
													}
													
												});
						
							}
							
							
							
							
							
							$scope.scheduleNormalAppointment2 = function()
							{
								$scope.currentDate = moment(new Date()).format('YYYY-MM-DD')
								$rootScope.startLoader();
								var data = {
										slotId : ($scope.slotId !=null ? $scope.slotId : $scope.patientObj.slotId),
										patientId : $scope.patientObj.patientId,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										status : 'A',
										appointmentTypeId : 1,
										priorityId : 1,
										slotStatusId : 4,
										generateToken : 'N',
										appointmentDate : ($scope.appointmentDate+" 00:00:00"), //yyyy-M-dd
								}
								if($scope.patientObj.patientId == undefined)
								{
									data.patientId = 0,
									data.prefixId = $scope.patientObj.prefixId,
									data.firstName = $scope.patientObj.firstName, 
									data.middleName = $scope.patientObj.middleName,
									data.lastName = $scope.patientObj.lastName,
									data.genderId = $scope.patientObj.genderId,
									data.countryCallingCode = $scope.patientObj.countryCallingCode,
									data.registrationTypeId = 5,
									data.mobileNumber = $scope.patientObj.mobileNumber,
									//data.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss')
									data.dob = $scope.patientObj.dob,
									data.identificationTypeId = $scope.patientObj.identificationTypeId,
									data.identificationNumber = $scope.patientObj.identificationNumber,
									data.email = $scope.patientObj.email
									
								}
								
								$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
								
								if($scope.orignalTimeForSlot != undefined){
									if($scope.appointmentDate==$scope.currentDate && $scope.slotCheckTimeAddOneHours >= $scope.orignalTimeForSlot)
										{
											data.generateToken  = 'Y';
											data.arrivalTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										}
									else{
											data.generateToken  = 'N';
										}
								}
								if($scope.patientAppoObjPayment != undefined){
									if($scope.appointmentDate==$scope.currentDate && $scope.slotCheckTimeAddOneHours >= $scope.patientAppoObjPayment.originalFromTime)
									{
										data.generateToken  = 'Y';
										data.arrivalTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									}else{
										data.generateToken  = 'N';
									}
								}								
							 console.log('scheduleNormalData',data)
							 //return false;
							 var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/kiosk";
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console
															.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
													{
														
														if($scope.patientObj.snapshotData!=undefined && $scope.patientObj.snapshotData!="")
														{
															if(response.data.object.patientId!=undefined && response.data.object.patientId!=null)
															{
																var file =$scope.convertBase64ToImage($scope.patientObj.snapshotData,moment().toDate().getTime()+".jpeg");
																  $scope.uploadPatientImage(file, response.data.object.patientId, $scope.organizationId, $scope.unitId);
															}
														  
														}
														
														if(data.generateToken == 'Y')
														{
															$scope.tokenNumber = response.data.object.tokenNumber;
															$rootScope.stopLoader();
															angular.element('#tokenDisplayModal').modal('show');
															//init();
														}
														else{
															angular.element('#cannotGenerateToken').modal('show');
														}
														
													}else if (response.data.status == "error" && response.data.code == 409)
														{
															$rootScope.stopLoader();
															$scope.patientObj.appointmentDate2 = null;
															$scope.slotId = null;
															$scope.saveAndPayEnable = true;
															$scope.saveEnable = true;
															$scope.getAvailableSlots($scope.doctorId!=null ? $scope.doctorId : null ,$scope.specialityId);
															angular.element('#slotIsAlreadyBooked').modal('show');
														}
													
												});
						
							}
							
							
							$scope.saveTokenWithoutPatient = function()
							{
								$scope.tokenNumber = null;
								var isFromKiosk = null;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
							
								var data = {
										isFromKioskToken:'Y',
								        createdDate: $scope.currentDate,
								        unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdBy : $scope.userId,
										appointmentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										patientId : ($scope.patientObj.patientId!=null ? $scope.patientObj.patientId : null)
								}
								$rootScope.startLoaderPopup();
								var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/kiosk";
								//return false;
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console
															.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
													{
														$scope.tokenNumber = response.data.object.tokenNumber;
														$rootScope.stopLoaderPopup();
														angular.element('#tokenDisplayModal').modal('show');
													}
												});
						
							}
							
							
							$scope.getBillAmountForKiosk = function()
							{
								var isFromKiosk = null;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								$rootScope.startLoader();
								var data = {
								        unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										patientId : ($scope.patientAppoObj.patientId!=null ? $scope.patientAppoObj.patientId : null),
										doctorId : $scope.patientAppoObj.doctorId,
										specialityId : $scope.patientAppoObj.specialityId,
										currentDate :  moment(new Date()).format('YYYY-MM-DD'),
										patientCategoryId : $scope.patientAppoObj.patientCategoryId,
										deptWiseAppointment : $scope.patientAppoObj.isDeptWise
								}
								$scope.initPaymentDetails();
								if($scope.patientAppoObj.patientId!=null && $scope.patientAppoObj.patientId!=undefined ){
									$scope.getLoyaltyCardList($scope.patientAppoObj.patientId);
								}
								console.log('$scope.patientObj',$scope.patientAppoObj);
								var URI = BASE_URL + ROOT_URL + "/api/opd/billAmount/kiosk"
								//return false;
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console
															.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
													{
														$scope.patientAppoObj.totalBillAmount = response.data.object.finalPrice;
														//$scope.patientAppoObj.totalBillAmount = 10;
														$scope.patientAppoObj.serviceAmount = response.data.object.serviceRate;
														$scope.patientAppoObj.taxAmount = response.data.object.taxAmt;
														$scope.patientAppoObj.systemDefiedVisitType = response.data.object.systemDefiedVisitType;
														
														// added by dinesh for multimode mode payment start
														$scope.patientAppoObj.dueAmount =parseFloat($scope.patientAppoObj.totalBillAmount);
														$scope.dueAmount =parseFloat($scope.patientAppoObj.totalBillAmount);
														$scope.patientAppoObj.payeeDueAmtFinal=parseFloat($scope.patientAppoObj.totalBillAmount);
														 $scope.patientAppoObj.payeeDueAmtStatic = Number(parseFloat($scope.patientAppoObj.totalBillAmount).toFixed(2));
														 $scope.patientAppoObj.paymentDetailsObj=$scope.paymentDetails;
														 $scope.patientAppoObj.loyaltyCardObj= $scope.loyaltyCardObj;
														 $scope.paymentDetails.loyaltyCardAmt = $scope.patientAppoObj.dueAmount;
														// added by dinesh for multimode mode payment end 
														 $scope.patientAppoObj.deptWiseAppointment = $scope.isDeptWise;
														 
														$rootScope.stopLoader();
														$state.go("paymentInformation", {
															patientAppoObjPayment: $scope.patientAppoObj,
														});
													}else{
														angular.element('#consultationConfigNotExistPre').modal('show');
														$scope.saveAndPayEnable = true;
														return false;
													}
												});
						
							}
				// code for multiimode payment start			
							$scope.dueAmount=0;
							$scope.payeeDueAmtFinal=0;
							$scope.listPaymentDetails=[];
							$scope.paymentModes = {
									1: "Cash",
									2: "Cheque",
									3: "Credit Card",
									4: "E - Transfer",
									5: "M-Pesa",
									6: "Loyalty Card",
									8: "E-Transfer"	,
									9: "PesaPal"
							}
							//-----------------payment Collection----------------//
							$scope.initPaymentDetails=function(){
								$scope.paymentDetails={
										"paymentRecieptDetailsId":"",
										"paymentModeId"			:5,
										"paymentModeName"		:"M-Pesa",
										"amount"				:"",
										"bankId"				:"",
										"bankName"				:"",
										"accountNumber"			:"",
										"checkNumber"			:"",
										"cardNo"				:"",
										"cardHolderName"		:"",
										"dateOfTransaction"		:new Date().getTime(),
										"expiryDate"			:"",
										"paymentVoucherNo"		:"",
										"cvv"					:"",
										"unitId"				:$scope.unitId,
										"orgnisationId"			:$scope.organizationId,
										"status"				:"A",
										"createdDate"			:new Date().getTime(),
										"createdBy"				:$scope.userId,
										"updatedDate"			:new Date().getTime(),
										"updatedBy"				:$scope.userId,
										"chequeDate"			:"",
										"cardTypeId"			:"",
										"isCancle"				:"I",
										"loyaltyCardAmt"        :'',
										"cardBalAmount"         :'',
										"loyaltyCardNo"         :'',
										'otp'                   :'',
										'hospitalAccountId'     : '',
										'transactionNo'         : ""
								}
							};
							
							$scope.setPaymentModeId=function(paymentModeId){
								
						/*		if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
									$scope.addBtnId=true;
									 $scope.pinpadAmounFlag =true;
								}else if(paymentModeId==6){
									$scope.addBtnId=true;
								}
								else{
									$scope.addBtnId=false;
									 $scope.pinpadAmounFlag =false;
								}*/
								$scope.paymentDetails.paymentModeId=paymentModeId;
								$scope.paymentDetails.paymentModeName=$scope.paymentModes[paymentModeId];
							}
							/*** loyalty Card****========================================================================*/
							
							$scope.validateOTPFlag=true;
							$scope.ganerateOTPFlag=true;
							
							$scope.getLoyaltyCardList = function(patientId){
								$scope.resend =false;
								var data = {
										"patientId" : patientId,
										  "organisationId" : $scope.organizationId,
										  "unitId" : $scope.unitId,
										  "status" : "A",
										  "isDeleted" : "N",
										  "isUserDefined":"Y"
								}
								var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getLoyaltiCardDetailsByPatientId";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
										if(response.data.listObject!=null && response.data.listObject.length>0){
											$scope.loyaltyCardObj = response.data.listObject[0];					
                                            $scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount;
											$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
											$scope.ganerateOTPFlag=false;
										}
										
									}
								});
							}
							
					/*		$scope.checkAmt = function(amt)
							{
								if(isNaN(Number(parseFloat(amt))))
									{
									 $scope.paymentDetails.loyaltyCardAmt= 0.00;
									}
								else{
									 $scope.paymentDetails.loyaltyCardAmt= amt;
									 $scope.ganerateOTPFlag=false;
								}
							}*/
							
						/*	$scope.getLoyaltyCardByNo = function(cardNo)
							{
								$scope.resend =false;
								if(cardNo!="" && cardNo.toString().length==16)
									{
									var data = {
											"patientId" : '',
											  "organisationId" : $scope.organizationId,
											  "unitId" : $scope.unitId,
											  "status" : "A",
											  "isDeleted" : "N",
											  "isUserDefined":"Y",
											  "cardNo":cardNo
									}
									var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getPatientDetailsByLoyaltiCardNo";
									BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
											if(response.data.listObject!=null && response.data.listObject.length>0)
											{
												var cardAmtToSub = 0;
										    	$scope.loyaltyCardObj = response.data.listObject[0];
											    $scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
											    if($scope.listPaymentDetails!=null && $scope.listPaymentDetails.length>0)
											    	{
											    	   angular.forEach($scope.listPaymentDetails,function(value,key)
											    	    {
											    		   if(value.paymentModeId==6 && value.cardNumber==cardNo)
											    			   {
											    			     cardAmtToSub = cardAmtToSub + parseFloat(value.amount); 
											    			   }
															
														});
											    	}
											     $scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount-cardAmtToSub;
											}
											$scope.ganerateOTPFlag=false;
										}
									});
									}

							}*/
							
					/*		$socpe.count ;
							if(flag)
								{  
								$socpe.count=0;
								$socpe.count=+1;
								}
							else if(!flag)
								{
								$socpe.count=-1;
								}*/
							/*$scope.generateOtp = function(flag)
							{
								if($scope.paymentDetails.cardBalAmount==0||$scope.paymentDetails.cardBalAmount=='')
									{
									growl.error("Please Recharge Your card.",
											{
												title : 'Error!'
											});
									return false;
						               	}
								if($scope.paymentDetails.loyaltyCardAmt==0||$scope.paymentDetails.loyaltyCardAmt==''||$scope.paymentDetails.loyaltyCardAmt==undefined||
										$scope.paymentDetails.loyaltyCardAmt==null)
									{
									growl.error("Amount Should be greater than zero.",
											{
												title : 'Error!'
											});
									    return false;
									 }
								if(parseFloat($scope.paymentDetails.loyaltyCardAmt)>parseFloat($scope.paymentDetails.cardBalAmount))
									{
									growl.error("Amount Should Be less Than Card Amount.",
											{
												title : 'Error!'
											});
									    return false;
									 }
								if($scope.loyaltyCardObj.mobileNumber==''||$scope.loyaltyCardObj.mobileNumber==undefined||
										$scope.loyaltyCardObj.mobileNumber==null)
									{
									growl.error("Mobile number is not Found..",
											{
												title : 'Error!'
											});
									    return false;
									 }
								
									$rootScope.startLoader();
									if($scope.paymentDetails.loyaltyCardNo!="" && $scope.paymentDetails.loyaltyCardNo.toString().length==16)
										{
										var data = {
												  "patientId" : '',
												  "organisationId" : $scope.organizationId,
												  "unitId" : $scope.unitId,
												  "status" : "A",
												  "isDeleted" : "N",
												  "isUserDefined":"Y",
												  "cardNo":$scope.paymentDetails.loyaltyCardNo,
												  "mobileNumber":$scope.loyaltyCardObj.mobileNumber,
												  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
												  "resendOTP" :flag,
												  "email":$scope.loyaltyCardObj.email,
												  "firstName":$scope.loyaltyCardObj.firstName
										}
										var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/generateOTP";
										BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success")
											{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$scope.validateOTPFlag=false;
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
											}
										});
										}

							}*/
							
						/*	$scope.sendOTP = function(flag)
							{
								if(flag=='N' && !$scope.resend)
									{
									 $scope.resend = (flag=='N')?true:false;
									 $scope.generateOtp(flag);
									}
								else if(flag=='Y' && $scope.resend)
									{
									  $scope.generateOtp(flag);
									}
							}*/
							
							$scope.checkAmt = function(amt)
							{
								$scope.dueAmount=$scope.patientAppoObjPayment.dueAmount;
								//loyaltiCardCallFactory.checkAmt(amt,$scope);
								
							}
							
							$scope.getLoyaltyCardByNo = function(cardNo)
							{
								loyaltiCardCallFactory.getLoyaltyCardByNo(cardNo,$scope);
								
								
							}
							
							$scope.sendOTP = function(flag)
							{
								$scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount);
								loyaltiCardCallFactory.sendOTP(flag,$scope);
								//$scope.send = (flag=='Y')?true:false;
								
							}
							$scope.reSendOTP = function(flag)
							{
								$scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount);
								loyaltiCardCallFactory.sendOTP(flag,$scope);
								//$scope.resend = (flag=='N')?true:false;
								//$scope.send =false;
							}
							
							
							$scope.clearLoylty = function(){
								
								$scope.send = false;
								$scope.validatTextFieldFlag = true;
								$scope.paymentDetails.otp ="";
								$scope.paymentDetails.loyaltyCardAmt='';
								$scope.paymentDetails.cardBalAmount='';
								$scope.paymentDetails.loyaltyCardNo='';
								$scope.calculateTotalDueRefAmount();
								$scope.getLoyaltyCardList($scope.patientAppoObj.patientId);
							}
							
							$scope.validateOtp = function()
							{
								
								if($scope.paymentDetails.otp==0||$scope.paymentDetails.otp==''||$scope.paymentDetails.otp==undefined||
										$scope.paymentDetails.otp==null)
									{
									growl.error("Please enter valid OTP",
											{
												title : 'Error!'
											});
									    return false;
									 }
								 else if($scope.paymentDetails.loyaltyCardNo=='' || $scope.paymentDetails.loyaltyCardNo==undefined){
									 	growl.error("Please enter valid Card no",
											{
												title : 'Error!'
											});
									    return false;
									 }
								else{
								$rootScope.startLoader();
									var data = {
											  "patientId" : '',
											  "organisationId" : $scope.organizationId,
											  "unitId" : $scope.unitId,
											  "status" : "A",
											  "isDeleted" : "N",
											  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt,
											  "isUserDefined":"Y",
											  "otp": $scope.paymentDetails.otp,
											  "cardNo":$scope.paymentDetails.loyaltyCardNo,
											  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
											  "authKey":$scope.loyaltyCardObj.authKey
									}
									var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/validateOTP";
									BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
													$scope.loyaltyCardObj.authKey = 0;
											 		$scope.send=false;
											 		$scope.resend=false;
											 		$scope.validatTextFieldFlag = true;
												    var paymentObj = angular.copy($scope.paymentDetails);
												    $scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount);
												    $scope.payeeDueAmtFinal=parseFloat($scope.patientAppoObjPayment.payeeDueAmtFinal);
												    $scope.patientAppoObjPayment.dueAmount=$scope.dueAmount;
												    $scope.patientAppoObjPayment.payeeDueAmtFinal=$scope.payeeDueAmtFinal;
													$scope.isBillList = 0;
													$scope.page='billList';
													console.log("payment mode id",$scope.paymentDetails.paymentModeId);
													paymentCollFactory.addPaymentDetails($scope);
													
													$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
													$scope.paymentDetails.cardBalAmount = paymentObj.cardBalAmount - parseFloat(paymentObj.loyaltyCardAmt);
													
													
													  /* $scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount)-parseFloat(paymentObj.loyaltyCardAmt);
													    $scope.payeeDueAmtFinal=parseFloat($scope.patientAppoObjPayment.payeeDueAmtFinal)-parseFloat(paymentObj.loyaltyCardAmt);
													    $scope.patientAppoObjPayment.dueAmount=$scope.dueAmount;
													    $scope.patientAppoObjPayment.payeeDueAmtFinal=$scope.payeeDueAmtFinal;*/
													
													/*if(paymentObj.cardBalAmount>= paymentObj.loyaltyCardAmt)
													{
														$scope.enableSettle = false;
													}else
													{
														$scope.enableSettle = true;
														angular.element('#saveNormalAppoitment').modal('show');
													}*/
													$scope.validateOTPFlag=true;
													$scope.ganerateOTPFlag=true;
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
								}
							}
							
							$scope.updateDisabledFlag = function(){
								$scope.validatTextFieldFlag = true;
							}
						/*	$scope.loyaltyCardObj ={
									
									"loyaltyCardNo" :"",
									"loyaltyCardBalance" :"",
									"authKey":0
							}
							*/
							
							//Ganesh Start
							$scope.calculateTotalDueRefAmount = function()
							{
								var amount = 0;
								var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
								$scope.consumedAmt = 0;
								var payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
								
								angular.forEach($scope.listPaymentDetails,function(value,key){
									amount = amount + parseFloat(value.amount); 
								});
								
								if($scope.availableDeposits!=null &&$scope.availableDeposits.length>0)
								{
									angular.forEach($scope.availableDeposits,function(value,key)
											{
												if(value.depositeFlag=='Y')
													{
													  $scope.consumedAmt = $scope.consumedAmt + parseFloat(value.consumedDeposit- value.oldConsumedDeposit); 
													}
											});
								}
								var tempDueAmt = parseFloat( $scope.patientAppoObjPayment.payeeDueAmtStatic) - parseFloat(amount) - parseFloat($scope.consumedAmt);
								var temppayeeDueAmtFinal =  $scope.patientAppoObjPayment.payeeDueAmtStatic - amount - $scope.consumedAmt;
								$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
								$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
								$scope.patientAppoObjPayment.payeeDueAmtFinal= parseFloat($scope.payeeDueAmtFinal).toFixed(2);
								var df = (parseFloat( $scope.patientAppoObjPayment.payeeDueAmtStatic) - (parseFloat(amount) + parseFloat($scope.consumedAmt)));
								if(df > 0)
									{
									$scope.patientAppoObjPayment.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
									$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
									}
								else
									{
									$scope.patientAppoObjPayment.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
//									$scope.dueAmount = parseInt($scope.billDueAmount) - parseInt( $scope.patientAppoObjPayment.payeeDueAmtStatic);
									$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
									}
								$scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
								$scope.amountCollected = amount;
								
								/*if($scope.consumedAmt > 0)
									{
									  $scope.depositeSettleBtnFlag = false;
									}
								else
									{
									$scope.depositeSettleBtnFlag = true;
									}*/
							}
							
							$scope.removePaymentDetails = function(index){
								paymentCollFactory.removePaymentDetails($scope,index);
								
							}
							
							/**====================================================================================*/
							
							$scope.releaseSlot = function()
							{
								/*if($scope.patientAppoObjPaymentPayment.slotId!=undefined || $scope.patientAppoObjPaymentPayment.slotId!=null)
									{
									var URI = BASE_URL + ROOT_URL + "/api/opd/block/slot/4/"+$scope.patientAppoObjPaymentPayment.slotId;
									//return false;
									BillingGenericService
											.serviceAction(METHOD_GET,
													URI, {},
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														
													});
									}*/
								
							}
							
							$scope.getBillAmountForWalkIn = function()
							{
								var isFromKiosk = null;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								$rootScope.startLoader();
								var data = {
								        unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										patientId : ($scope.patientObj.patientId!=null ? $scope.patientObj.patientId : null),
										doctorId : $scope.doctorId,
										specialityId : $scope.specialityId,
										currentDate :  moment(new Date()).format('YYYY-MM-DD'),
										patientCategoryId : $scope.patientObj.patientCategoryId !=null ? $scope.patientObj.patientCategoryId : $scope.defaultPatientCategoryId,
										deptWiseAppointment : $scope.isDeptWise
								}
								console.log('data',data);
								 $scope.initPaymentDetails();
								if($scope.patientObj.patientId!=null && $scope.patientObj.patientId!=undefined ){
									$scope.getLoyaltyCardList($scope.patientObj.patientId);
								}
							
								/*if($scope.slotId!=undefined || $scope.slotId!=null)
								{
									var URI = BASE_URL + ROOT_URL + "/api/opd/block/slot/4/"+$scope.slotId;
									//return false;
									BillingGenericService
											.serviceAction(METHOD_GET,
													URI, {},
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														
													});
								}*/
								
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/billAmount/kiosk"
								//return false;
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console
															.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
													{
														$scope.patientObj.totalBillAmount = response.data.object.finalPrice;
														//$scope.patientObj.totalBillAmount = 10;
														$scope.patientObj.serviceAmount = response.data.object.serviceRate;
														$scope.patientObj.taxAmount = response.data.object.taxAmt;
														$scope.patientObj.systemDefiedVisitType = response.data.object.systemDefiedVisitType;
														
														if($scope.doctorId > 0){
														var docObj = $.grep($scope.doctorList, function (doc) {
										    	            return doc.doctorId == $scope.doctorId;
										    	        })[0];
														$scope.patientObj.doctorName = "Dr. "+docObj.firstName+" "+docObj.lastName;
														$scope.patientObj.doctorId =  $scope.doctorId;
														$scope.patientObj.specialityName = docObj.specialityName;
														}
														else{
															
															var speObj = $.grep($scope.specialityList, function (spe) {
											    	            return spe.specialityId ==  $scope.specialityId;
											    	        })[0];
															$scope.patientObj.specialityName = speObj.specialityName;
															$scope.patientObj.isNursingAssessmentRequired  = speObj.isNursingAssessmentRequired;
														}
														$scope.patientObj.specialityId = $scope.specialityId;
														console.log( $scope.patientObj);
														
														// added by dinesh for multimode mode payment start
														$scope.patientObj.dueAmount =parseFloat($scope.patientObj.totalBillAmount);
														$scope.dueAmount =parseFloat($scope.patientObj.totalBillAmount);
														$scope.patientObj.payeeDueAmtFinal=parseFloat($scope.patientObj.totalBillAmount);
//														$scope.payeeDueAmtFinal = Number(parseFloat(value1.dueAmount).toFixed(2));
														 $scope.patientObj.payeeDueAmtStatic = Number(parseFloat($scope.patientObj.totalBillAmount).toFixed(2));
														 $scope.patientObj.paymentDetailsObj=$scope.paymentDetails;
														 $scope.patientObj.loyaltyCardObj= $scope.loyaltyCardObj;
														 $scope.patientObj.isPaidAppointment = $scope.isPaidAppointment;
														 $scope.patientObj.deptWiseAppointment = $scope.isDeptWise;
														 $scope.paymentDetails.loyaltyCardAmt = $scope.patientObj.totalBillAmount;
														// added by dinesh for multimode mode payment end
														console.log( "new added",$scope.patientObj);
														//return false;
														$rootScope.stopLoader();
														$state.go("paymentInformation", {
															patientAppoObjPayment: $scope.patientObj,
														});
														
														
													}else{
														angular.element('#consultationConfigNotExist').modal('show');
														$scope.saveAndPayEnable = true;
														/*var URI = BASE_URL + ROOT_URL + "/api/opd/block/slot/1/"+$scope.slotId;
														//return false;
														BillingGenericService
																.serviceAction(METHOD_GET,
																		URI, data,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {
																			
																		});*/
														return false;
													}
												});
						
							}
							
							
							$scope.settleTranaction = function()
							{
								
								if($scope.listPaymentDetails.length == 0){
									growl.error("Please pay the amount !! ",{title : "WARNING!"});
									return false;
								}
								var amount=0;
								angular.forEach($scope.listPaymentDetails,function(value,key){
									amount = amount + parseFloat(value.amount); 
								});
								if(amount!=$scope.patientAppoObjPayment.totalBillAmount){
									angular.element('#saveNormalAppoitment').modal('show');
									return false;
								}
//								if($scope.patientAppoObjPayment.totalBillAmount == $scope.mpesaAmount)
								if($scope.patientAppoObjPayment.totalBillAmount <= amount)
								{
									 
									
									
									//Appointment Object
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
									
									  $scope.appointmentDate=$rootScope.getChangedFormatedDateShalseTwo($scope.patientAppoObjPayment.appointmentDate);
									
									var dataAppointmentObj = {
											patientId : $scope.patientAppoObjPayment.patientId,
											remark : "",
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											createdDate : $scope.currentDate,
											updatedDate : $scope.currentDate,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											status : 'A',
											appointmentTypeId : "1",
											priorityId :"1",
											slotId :$scope.patientAppoObjPayment.slotId,
									        appointmentDate2 :$scope.patientAppoObjPayment.appointmentDate,
									        fromTimeString : $scope.patientAppoObjPayment.appointmentTime,
									        appointmentDate :  moment($scope.appointmentDate).format('YYYY-MM-DD'),
									        slotStatusId : 4,
									        isFromEncounter: 'N',
										}
									
									
									var isFromKiosk = null;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									$rootScope.startLoader();
									var data = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											doctorId : $scope.patientAppoObjPayment.doctorId,
											encounterDate : new Date().getTime(),
											isReferal : 'N',
											deptId : $scope.patientAppoObjPayment.specialityId,
											paymentEntitlementTypeId : 1, //self
											createdBy : $scope.userId,
											createdDate : new Date().getTime(),
											updatedDate : new Date().getTime(),
											updatedBy : $scope.userId,
											status : 'A',
											defaultSelfTariffId : $scope.defaultSelfTariffId,
											isScheduledAppointment:'A',
											specialityId : $scope.patientAppoObjPayment.specialityId,
											encounterTypeId: 1,
											isAppointmentRequired : 'Y',
											//counterId : 1, ask vishal
											currentDate :  moment(new Date()).format('YYYY-MM-DD'),
											patientCategoryId : $scope.patientAppoObjPayment.patientCategoryId,
											totalBillAmt : $scope.patientAppoObjPayment.totalBillAmount,
											paidAmount : amount,
//											paidAmount : $scope.mpesaAmount,
											mpesaTxcode : $scope.mpesaTxcode,
											systemGeneratedVisitTypeId:$scope.patientAppoObjPayment.systemDefiedVisitType ,
											userDefinedVisitTypeId:$scope.patientAppoObjPayment.systemDefiedVisitType ,
											isNursingAssessmentRequiredForSpeciality : $scope.patientAppoObjPaymentPayment.isNursingAssessmentRequired,
											listKinDetailsDto : [],
											patientSponsorDetailsDtosList : [{
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												visitTypeId : 1,
												paymentEntitlementId : 1,
												payeeId : $scope.selfPayeeId ,
												priorityId : 1,
												createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												createdBy : $scope.userId,
												updatedBy : $scope.userId,
												status : 'A',
												tariffId : $scope.defaultSelfTariffId,
											}],
											taxAmt : $scope.patientAppoObjPayment.taxAmount,
											serviceAmt : $scope.patientAppoObjPayment.serviceAmount,
											billingBedCategoryId : $scope.opdBillingCatId,	 				
											payeeId : $scope.selfPayeeId,
											isPreOrPostBilling : $scope.isPreOrPostBilling,
											deptWiseAppointment: $scope.patientAppoObjPayment.deptWiseAppointment!=null ? $scope.patientAppoObjPayment.deptWiseAppointment : $scope.patientAppoObjPayment.isDeptWise!=null ? $scope.patientAppoObjPayment.isDeptWise :'N'  ,
											paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
											isTransferToActiveDeposit : $scope.patientAppoObjPayment.totalBillAmount < amount ? 'Y' : 'N',
											registrationTypeId : $scope.patientAppoObjPayment.typeId
									}
									
									if($scope.patientAppoObjPayment.patientId>0)
										{
											data.patientId = $scope.patientAppoObjPayment.patientId;
										}else{
												data.patientId = 0,
												data.prefixId = $scope.patientAppoObjPaymentPayment.prefixId,
												data.firstName = $scope.patientAppoObjPaymentPayment.firstName, 
												data.middleName = $scope.patientAppoObjPaymentPayment.middleName,
												data.lastName = $scope.patientAppoObjPaymentPayment.lastName,
												data.genderId = $scope.patientAppoObjPaymentPayment.genderId,
												data.countryCallingCode = $scope.patientAppoObjPaymentPayment.countryCallingCode,
												data.registrationTypeId = 5,
												data.mobileNumber = $scope.patientAppoObjPaymentPayment.mobileNumber,
												data.dob = $scope.patientAppoObjPaymentPayment.dob,
												data.patientCategoryId = $scope.patientAppoObjPaymentPayment.patientCategoryId,
												data.identificationTypeId = $scope.patientAppoObjPaymentPayment.identificationTypeId,
												data.identificationNumber = $scope.patientAppoObjPaymentPayment.identificationNumber,
												data.email = $scope.patientAppoObjPaymentPayment.email
											}
									
									if($scope.patientAppoObjPayment.appointmentId > 0)
										{
										  data.appointmentId = $scope.patientAppoObjPayment.appointmentId
										}
									else
										{
											//dataAppointmentObj.generateToken  = 'Y';
										    $scope.flag=false;
											$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
											if(moment($scope.appointmentDate).format('YYYY-MM-DD')==moment(new Date()).format('YYYY-MM-DD') && $scope.slotCheckTimeAddOneHours >= $scope.patientAppoObjPayment.originalFromTime)
											{
												 $scope.flag=true;
												dataAppointmentObj.generateToken  = 'Y';
												dataAppointmentObj.arrivalTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
												dataAppointmentObj.isPaidAppointment = 'Y';
											}
											else{
												dataAppointmentObj.generateToken  = 'N';
												data.isPaidAppointment = 'Y';
												dataAppointmentObj.isPaidAppointment = 'Y';
											}
										
											data.appointmentObject = dataAppointmentObj;
										}
										data.patientId = $scope.patientAppoObjPayment.patientId;
										data.paymentCompleted=false;
									var URI = null;
									$rootScope.startLoader();
									
									if($scope.patientAppoObjPaymentPayment.isPaidAppointment == undefined || $scope.patientAppoObjPaymentPayment.isPaidAppointment == 'N' || (moment($scope.appointmentDate).format('YYYY-MM-DD')==moment(new Date()).format('YYYY-MM-DD') && $scope.slotCheckTimeAddOneHours >= $scope.patientAppoObjPayment.originalFromTime))
										URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay";
									else
										URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay/future";
									
									console.log('SavaAndPay',data);
									//return false;
									BillingGenericService
									.serviceAction(METHOD_POST,
											URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console
														.log(response);
												//$rootScope.startLoader();
												if (response.data.status == "success")
												{
													growl.success("Payment Done Successfully!", {
																title: "Success"
															});

													if(response.data.resObject!=null){
														$scope.searchAppointmentsUniqueData(response.data.resObject.appointmentId);
														 if (response.data.listObject && response.data.listObject.length) {
                                                             response.data.listObject.forEach(function(obj) {
                                                                 if (obj.tabId === 44) {
                                                                     $scope.jsonGlobalForQuestion = obj.jsonConfiguration;
                                                                 }
                                                                 if (obj.tabId === 45) {
                                                                     $scope.jsonGlobalForAiTemplate = obj.jsonConfiguration;
                                                                 }
                                                             });
                                                         }
													 }

													if($scope.patientObj==null){
														$scope.openRecordingPopup(response.data.resObject);
													
														// $state.go('kiosk');
														$rootScope.stopLoader();
													}
												
													if($scope.patientObj.snapshotData !=undefined && $scope.patientObj.snapshotData!="")
													{
														if(response.data.object.patientId!=undefined && response.data.object.patientId!=null)
														{
															var file =$scope.convertBase64ToImage($scope.patientObj.snapshotData,moment().toDate().getTime()+".jpeg");
															  $scope.uploadPatientImage(file, response.data.object.patientId, $scope.organizationId, $scope.unitId);
														}
													  
													}
													
													if(response.data.object.object.unitTokenNumber!=null && response.data.object.object.unitTokenNumber>0)
													{
														$scope.tokenNumber = response.data.object.object.unitTokenNumber;	
														angular.element('#tokenDisplayModal').modal('show');
													}
													else
														angular.element('#cannotGenerateToken').modal('show');
													
                                                    $rootScope.stopLoader();

													$scope.openRecordingPopup(response.data.resObject);
													// $state.go('kiosk');
													
													// $state.go('kiosk');
													
													
												}
												else if (response.data.code == 409) {
													
													$scope.appointmentAlreadyExistMsg = response.data.message;
													angular.element('#alreadyExistAppointment').modal('show');
													$rootScope.stopLoader();
													return false;
													
												}
											});
											
								}else{
									
									angular.element('#saveNormalAppoitment').modal('show');
									return false;
									
								}
								
								// $state.go('kiosk');
						
							}
							
							
							$scope.back = function() 
							{
								if($scope.slot==true)
								{
									// go to doctor
									$scope.slot=false;
									if($scope.isDeptWise  == 'N')
										{
										$scope.dept=false;
										$scope.doc=true;
										
										}else{
											$scope.dept=true;
											$scope.doc=false;
										}
									
									return false;
								}
								if($scope.doc==true)
								{
									// go to specialty
									$scope.slot=false;
									$scope.dept=true;
									$scope.doc=false;
									return false;
								}
								
								$state.go('walkinNew');
								
								
							}
							
							$scope.getDoctorBySpeciality = function(id,isDeptWise) {
								$scope.specialityId = id;
								$scope.isDeptWise = isDeptWise;
								if(isDeptWise=='N')
								{
									var data = {
										specialityId : id,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									
									
									//if($scope.isAppointmentRequired=='Y')
									var	URI = BASE_URL + ROOT_URL + "/api/global/appoitments/doctors";
									/*else
										URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;*/
									BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
														console.log(response);
														$scope.doctorList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success"){
															$scope.doctorList = response.data.listObject;
															
															/*if($scope.doctorList.size >0 )
															{
															growl.error("No doctor available for this speciality !! ",{title : "WARNING!"});
															}*/
														console.log('DoctorList',$scope.doctorList);
														$scope.dept=false;
														$scope.doc=true;
													}
									});
								}else{
									$scope.getAvailableSlots(null,id);
								}
							}
							
							$scope.getAvailableSlots = function(doctorId,specialityId) {
								
								$rootScope.startLoader();
                                $scope.slotDetailsList = [];
                                
								$scope.slotCheckTime = moment(new Date()).format("HH:mm");
								$scope.slotCheckDate = moment(new Date()).format("YYYY-MM-DD");
								//return false;
								$scope.datepicker = new Date();
								$scope.slot=true;
								$scope.dept=false;
								$scope.doc=false;
								var slotTypeId = null;
								$scope.doctorIdForSlot = doctorId;
								$scope.doctorspecialityId = specialityId;
								
								if(doctorId>0){
									slotTypeId = 1;
								}else{
									slotTypeId = 4;
								}
									var data = {
										specialityId : $scope.specialityId,
										doctorId : doctorId,
										appointmentDate : moment($scope.date).format('YYYY-MM-DD'),
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId : slotTypeId
									};

								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,function (value,key){
															if(value.isBlockedUnblocked != 1){
																$scope.slotDetailsList.push(value);
															}
														});
														console.log($scope.slotDetailsList);
														$rootScope.stopLoader();
														if($scope.slotDetailsList.size >0)
															{
															growl.error("No slots available for current date !! Please select another date !! ",{title : "WARNING!"});
															}
														
													}else{
														$rootScope.stopLoader();
													}

												});
							}
							
							//Function For Get Date Wise Slots
							$scope.getSlotsOfDate = function() {
								
								$rootScope.startLoader();
								$scope.slotCheckTime = moment(new Date()).format("HH:mm");
								$scope.slotCheckDate = moment(new Date()).format("YYYY-MM-DD");
								console.log('$scope.slotCheckDate',$scope.slotCheckDate);
								var dt = $('#datepicker').val();
								var array = dt.split('/');
								var newDate = "";
								newDate = array[2] + '-' + array[1] + '-'+ array[0];

								$scope.slot=true;
								$scope.dept=false;
								$scope.doc=false;
								var slotTypeId = null;
								
								if($scope.doctorIdForSlot>0){
									slotTypeId = 1;
								}else{
									slotTypeId = 4;
								}
									var data = {
										specialityId : $scope.doctorspecialityId,
										doctorId : $scope.doctorIdForSlot,
										appointmentDate : newDate,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId : slotTypeId
									};

								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,function (value,key){
															if(value.isBlockedUnblocked != 1){
																$scope.slotDetailsList.push(value);
															}
														});
														console.log($scope.slotDetailsList);
														
														if($scope.slotDetailsList.size >0)
															{
															growl.error("No slots available for current date !! Please select another date !! ",{title : "WARNING!"});
															}
													}
													$rootScope.stopLoader();

												});

							}
							
							$scope.exit = function() 
							{
								/*//start
								var printContents = $window.document.getElementById('qmsTokenPrintingId').innerHTML;
						        var popupWin = $window.open('', '_blank', 'width=400,height=400');
						        popupWin.document.open();
						        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
						        popupWin.document.close();
						        // end
*/								$scope.init();
								$scope.init2();
								$scope.init3();
								$('#tokenDisplayModal').hide('hide');
								$(".modal-backdrop").hide();
								/*document.location.reload(true);*/
								$state.go($scope.landingScreen,{reload: true});
								
							/*	$timeout(function () {
									$window.location.reload();
								}, 3000);*/
								
								
								
							}	
							
							
							
							$scope.getSlotDetailsPopup = function(item) 
							{
								
								$rootScope.startLoader();
								
								if($scope.patientObj.patientId>0)
								{
									var data = {
											specialityId : item.specialityId,
											doctorId : item.doctorId,
											patientId : $scope.patientObj.patientId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotId : item.slotId,
											appointmentTypeId : 1,
											appointmentDate : item.appointmentDate2
										};

									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/check";
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.code == 409) {
															
															$scope.appointmentAlreadyExistMsg = response.data.message;
															angular.element('#alreadyExistAppointment').modal('show');
															$rootScope.stopLoader();
															return false;
															
														}else{
															
															$scope.doctorId = item.doctorId;
															$scope.specialityId = item.specialityId;
															$scope.specialityname = item.specialityName;
															$scope.consultingDoctorName = item.doctorName;
															$scope.registrationNumber = item.registrationNumber;
															$scope.fromTime = item.fromTimeString2;
															$scope.toTime = item.toTimeString2;
															$scope.appointmentDate = item.appointmentDate2;
															$scope.slotId = item.slotId;
															$scope.scheduleAppEnable=false;
															$scope.saveEnable = false;
															$scope.orignalTimeForSlot = item.originalFromTime;
															
															$scope.patientObj.appointmentDate2 =  item.appointmentDate2;
															$scope.patientObj.appointmentDate =  item.appointmentDate;
															$scope.patientObj.appointmentTime =  item.fromTimeString2;
															$scope.patientObj.originalFromTime = item.originalFromTime;
															$scope.patientObj.slotId = item.slotId;
															//$scope.regiType = "1";
															$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
															if(item.appointmentDate2==$scope.slotCheckDate && $scope.slotCheckTimeAddOneHours >= item.originalFromTime && $scope.patientObj.patientId > 0)
															{
																$scope.saveAndPayEnable = false;
																$scope.isPaidAppointment = 'N'
															}
															else{
																$scope.saveAndPayEnable = false;
																$scope.isPaidAppointment = 'Y'
															}
															$rootScope.stopLoader();
														}

													});
								}else{
									$scope.doctorId = item.doctorId;
									$scope.specialityId = item.specialityId;
									$scope.specialityname = item.specialityName;
									$scope.consultingDoctorName = item.doctorName;
									$scope.fromTime = item.fromTimeString2;
									$scope.toTime = item.toTimeString2;
									$scope.appointmentDate = item.appointmentDate2;
									$scope.slotId = item.slotId;
									$scope.scheduleAppEnable=false;
									$scope.saveEnable = false;
									$scope.orignalTimeForSlot = item.originalFromTime;
									
									$scope.patientObj.appointmentDate2 =  item.appointmentDate2;
									$scope.patientObj.appointmentDate =  item.appointmentDate;
									$scope.patientObj.appointmentTime =  item.fromTimeString2;
									$scope.patientObj.originalFromTime = item.originalFromTime;
									$scope.patientObj.slotId = item.slotId;
									//$scope.regiType = "1";
									$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
									if(item.appointmentDate2==$scope.slotCheckDate && $scope.slotCheckTimeAddOneHours >= item.originalFromTime && $scope.patientObj.patientId > 0)
									{
										$scope.saveAndPayEnable = false;
										$scope.isPaidAppointment = 'N'
									}
									else{
										$scope.saveAndPayEnable = false;
										$scope.isPaidAppointment = 'Y'
									}
									$rootScope.stopLoader();
								}
								
								
							}	
							
							$scope.clearPatientDetails=function(){
								$scope.init();
							}
							
							/******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
					        	/*First Level*/
						    	$scope.firstTabLevel = 1 ;
								
								$scope.setFirstTabLevel= function(newTab)
								{
									$scope.firstTabLevel = newTab;
								};
								
								$scope.isSetFirstTabLevel = function(tabNum)
								{
									return $scope.firstTabLevel === tabNum;
								};
								
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
								
						/************************Report Token Number*******Chetana******/
								
								/*if($scope.savedInvestigations.labTestResId != ''){*/
								
							
								$scope.printTokenNumberReport=function(){
								
								/*
									  var data =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "TokenNumberPrint.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "TokenNumberPrint",
									                 "reportParams": [
									                	 {
											                  "paramName": "@appointment_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.patientAppoObjPaymentPayment.appointmentId
										                }
							                 ]
									  		};
									
									    reportService.generate(REPORT_API_URI1,data);*/
									
									var printContents = $window.document.getElementById('qmsTokenPrintingId').innerHTML;
							        var popupWin = $window.open('', '_blank', 'width=400,height=400');
							        popupWin.document.open();
							        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print();">' + printContents + '</body></html>');
							        popupWin.document.close();
									
								}
								
								
//									
								
								/******************************Report End**************************/
								
								$scope.printToken = function() 
								{									
									//start
									var printContents = $window.document.getElementById('qmsTokenPrintingId').innerHTML;
							        var popupWin = $window.open('', '_blank', 'width=400,height=400');
							        popupWin.document.open();
							        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print();">' + printContents + '</body></html>');
							        popupWin.document.close();
							        // end
									$scope.init();
									$scope.init2();
									$scope.init3();
									$('#tokenDisplayModal').hide('hide');
									 angular.element('.modal-backdrop').remove();
									angular.element('body').removeClass('modal-open');
									angular.element('body').css('padding-right', '');
									$(".modal-backdrop").hide();
									/*document.location.reload(true);*/
									/*$state.go($scope.landingScreen);
									$window.location.reload();*/
									$scope.landingScreen="kiosk";
									$state.go($scope.landingScreen,{reload: true});
									
								}	
								
								$scope.pesapalObject={"transactionId":null,"mobileNo":null,"pesaPalAmount":null,"transactionType":null};
								$scope.processPesapalReq=function(){
									var URI1 = BASE_URL + ROOT_URL + "/api/global/callPesaPalPaymentRequest";
									var data={
											 updatedBy:$scope.userId,
											 createdBy:$scope.userId,
											 unitId:$scope.unitId,
											 organizationId:$scope.organizationId
									    };
									
									   if($scope.pesapalObject.pesaPalAmount==null){
										   growl.warning(
													'amount can not be blank',
													{
														title : 'Warning!'
													});
										return false;
									   }else if($scope.pesapalObject.pesaPalAmount>$scope.patientAppoObjPayment.dueAmount){
										   growl.warning(
													'amount can not be greater than due amount',
													{
														title : 'Warning!'
													});
										return false;
									   }else if($scope.patientAppoObjPaymentPayment.firstName=="" || $scope.patientAppoObjPaymentPayment.firstName==null){
										   growl.warning(
													'Patient name can not blank',
													{
														title : 'Warning!'
													});
										return false;
									   }else if($scope.pesapalObject.mobileNo==null){
										   growl.warning(
													'Mobile number can not blank',
													{
														title : 'Warning!'
													});
										return false;
									   }
									  data.amount=$scope.pesapalObject.pesaPalAmount;
									 // data.transactionReferenceId=$scope.pesapalObject.mobileNo;
									  data.phoneNumber=$scope.pesapalObject.mobileNo;
									  data.firstName=$scope.patientAppoObjPaymentPayment.firstName;
									  data.lastName=$scope.patientAppoObjPaymentPayment.lastName;
									  var dateString = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									  var splitDate=dateString.split(" ");
									  var dateSplit=splitDate[0].split("-");
									  var timeSplit=splitDate[1].split(":");
									  var dateTimeString=dateSplit[0]+dateSplit[1]+dateSplit[2]+timeSplit[0]+timeSplit[1]+timeSplit[2];
									  data.transactionReferenceId=dateTimeString;
									  
										var baseUrl=location.protocol + "//" + location.hostname +
							               (location.port && ":" + location.port);
										 var redirectUrl = baseUrl + ROOT_URL+"/#/pesapalProcessPage";
										 
										 data.callbackUrl=redirectUrl;
									    $rootScope.startLoader();
										GenericService.serviceAction("POST", URI1, data).then(function(response){
											var pesapalResponse=response.data.object;
											$rootScope.stopLoader();
											if(response.data.status=="error"){
												growl.error(pesapalResponse.errorMessage,
														{
															title : 'Error!'
														});
												return false;
											}else{
												
												$scope.patientAppoObjPaymentPayment.listPaymentDetails=$scope.listPaymentDetails;
												 sessionStorage.setItem('tempAppointmentObjet', JSON.stringify($scope.patientAppoObjPaymentPayment));
											
										      var iframe = document.getElementById("frameId");
										      
										     // iframe.contentWindow.document.open();
										      iframe.src=pesapalResponse.pesapalUrl;
										     // iframe.contentWindow.document.close();
										      var popup = angular.element('#paymentReqModal');
										      popup.modal('show');
											}
										});
								}	
								
								$scope.checkPesapalPaymentStatus=function(){
								    $rootScope.startLoader();
								    var URI1 = BASE_URL + ROOT_URL + "/api/global/getPesapayPaymentStatus";
									var data={
											"transactionReferenceId":null,
											"transactionId":$scope.pesapalObject.transactionId,
											"unitId" : $scope.unitId,
											"organizationId" : $scope.organizationId
									}
									var baseUrl=location.protocol + "//" + location.hostname +
						               (location.port && ":" + location.port);
									 var redirectUrl = baseUrl + ROOT_URL+"/#/pesapalProcessPage";
									 
									 data.callbackUrl=redirectUrl;
									 
									if($scope.pesapalObject.transactionId==null){
										 growl.warning(
													'Transaction can not be blank',
													{
														title : 'Warning!'
													});
										return false;
									}
									GenericService.serviceAction("POST", URI1, data).then(function(response){
		 							//var status=response.data.message;
		 							//var res = status.split("=");
		 							if(response.data.object.transactionStatus=='COMPLETED'){
		 								console.log('payment is completed');
		 								$scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount);
										 $scope.payeeDueAmtFinal=parseFloat($scope.patientAppoObjPayment.payeeDueAmtFinal);
										$scope.pesapalObject.pesaPalAmount = response.data.object.amount;
										$scope.pesapalObject.resTransactionId = response.data.object.transactionId;
										$scope.pesapalObject.transactionType = response.data.object.transactionType;
																		
										$scope.isBillList = 0;
										$scope.page='billList';
										paymentCollFactory.addPaymentDetails($scope);
										
		 							}else if(response.data.object.transactionStatus=='PENDING'){
		 								 growl.warning(
													'Transaction  Pending',
													{
														title : 'Warning!'
													});
		 							}else if(response.data.object.transactionStatus=='FAILED'){
		 								 growl.warning(
													'Transaction failed',
													{
														title : 'Warning!'
													});
		 							}else if(response.data.object.transactionStatus=='CONSUMED'){
		 								 growl.warning(
													'Already used ',
													{
														title : 'Warning!'
													});
		 								$rootScope.stopLoader();
		 								 return false;
		 							}else{
		 								growl.warning(
												'Invalid Transaction Id ',
												{
													title : 'Warning!'
												});
		 								$rootScope.stopLoader();
	 								 return false;
		 							}
		 							$rootScope.stopLoader();
									});
									
								}
								var tempAppointmentObj = JSON.parse(sessionStorage.getItem('tempAppointmentObjet'));
								if(tempAppointmentObj != undefined){
									$scope.patientAppoObjPaymentPayment=tempAppointmentObj;
									sessionStorage.removeItem('tempAppointmentObjet');
									$scope.listPaymentDetails=$scope.patientAppoObjPaymentPayment.listPaymentDetails;
								if($scope.patientAppoObjPaymentPayment.isFromPesaPalSuccessPage!=undefined){
									if($scope.patientAppoObjPaymentPayment.pesapalPaymentStatus=="COMPLETED"){
										$scope.dueAmount =parseFloat($scope.patientAppoObjPayment.dueAmount);
										 $scope.payeeDueAmtFinal=parseFloat($scope.patientAppoObjPaymentPayment.payeeDueAmtFinal);
										 $scope.pesapalObject={};
										$scope.pesapalObject.pesaPalAmount = $scope.patientAppoObjPaymentPayment.pesaPalAmount;
										$scope.pesapalObject.resTransactionId = $scope.patientAppoObjPaymentPayment.resTransactionId;
										$scope.pesapalObject.transactionType =$scope.patientAppoObjPaymentPayment.transactionType;
																		
										$scope.isBillList = 0;
										$scope.page='billList';
										
										paymentCollFactory.addPaymentDetails($scope);
									}
								}
						 }
									
								
								
								$scope.filepreview = "";
								$scope.patientImage1= "";
						            $scope.setSecondTabLevel(1);
									  var _video = null,
								        patData = null;

								    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};
									$scope.totalDisplayed = 2;
								    // Setup a channel to receive a video property
								    // with a reference to the video element
								    // See the HTML binding in main.html
								    $scope.channel = {};
							
							    $scope.webcamError = false;
							    $scope.onError = function (err) {
							        $scope.$apply(
							            function() {
							                $scope.webcamError = err;
							            }
							        );
							    };

							    
							    $scope.onSuccess = function () {
							        // The video element contains the captured camera data
							        _video = $scope.channel.video;
							        _video.width = 117;
							        _video.height = 100;
							        $scope.$apply(function() {
							        	//alert(_video.width);
							            $scope.patOpts.w = _video.width;
							            $scope.patOpts.h = _video.height;
							            //$scope.showDemos = true;
							        });
							    };

							    $scope.onStream = function (stream) {
							        // You could do something manually with the stream.
							    };
							    
							    $scope.openUploadPhoto = function(){
							    	$scope.counterSec = "";
							    	$scope.count  = false;
							    	angular.element('#uploadPhoto').modal('show');
							    }
							    
							    $scope.count  = false;
							 
							    $scope.counter = function() {
							    	
							    	if($scope.count == false){
										 $scope.counterSec = 5;
									}
							    	
							    	 if($scope.counterSec>0){
							    		
							    		$timeout(function(){
								         $scope.counterSec--; 
								         $scope.count = true;
								         $scope.counter();  
								         
							    		}, 1000);
							    		
							    	 }
							    	 
							    	 if($scope.counterSec==0 ){
							    		 $timeout.cancel();
							    		$scope.counterSec = "";
							    		$scope.count = false;
							    		$scope.makeSnapshot(1);
							    	}
							    	
							      }
							      $scope.stopCountDown = function(){
							    	  $timeout.cancel();
							    	  $scope.count  = false;
							    	  $scope.counterSec = "";
							    	  angular.element('#uploadPhoto').modal('hide');
							      };
							      
								$scope.makeSnapshot = function(type) {
							        if (_video) {
							        	
							            var patCanvas = document.querySelector('#snapshot'+type);
							            if (!patCanvas) return;
							            //alert(_video.width);
							            patCanvas.width = _video.width;
							            patCanvas.height = _video.height;
							            var ctxPat = patCanvas.getContext('2d');

							            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
							            ctxPat.putImageData(idata, 0, 0);

							            sendSnapshotToServer(patCanvas.toDataURL());

							            patData = idata;
							        }
							    };
							    
							    $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
							        window.location.href = dataURL;
							    };
							    
							    var getVideoData = function getVideoData(x, y, w, h) {
							        var hiddenCanvas = document.createElement('canvas');
							        hiddenCanvas.width = _video.width;
							        hiddenCanvas.height = _video.height;
							        var ctx = hiddenCanvas.getContext('2d');
							        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
							        return ctx.getImageData(x, y, w, h);
							    };

							    /**
							     * This function could be used to send the image data
							     * to a backend server that expects base64 encoded images.
							     *
							     * In this example, we simply store it in the scope for display.
							     */
							    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
							        $scope.snapshotData = imgBase64;
							    };
								
								//for end camera
								   $scope.convertBase64ToImage = function (dataurl, filename) {
									    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
									        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
									    while(n--){
									        u8arr[n] = bstr.charCodeAt(n);
									    }
									    return new File([u8arr], filename, {type:mime});
									}
								   
								  $scope.uploadPatientImage = function(inputFile,patientId,orgId,unitId)
								    {
								    	    var file = inputFile;
								    	    var fd = new FormData();
								    	    fd.append('file', file );
								
								    	    
											var URI1 = BASE_URL + ROOT_URL + PATIENT_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+patientId;
											GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
											{	
												if (response.data.status == 'success')
												{
													$scope.filepreview = "";
													$scope.patientImage1 = null;
													angular.element("input[type='file']").val(null);
													$scope.file = "";
													  $('#pieChartContainer').html(''); //remove canvas from container
													  $('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>');
													$rootScope.stopLoader();
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
												}
											});
								    }
								  
								  $scope.cancelPatientImg = function(){
										$scope.filepreview = "";
										$scope.patientImage1 = null;
										angular.element("input[type='file']").val(null);
										$scope.file = "";
										
								}
								  $scope.cancelCapturedPatientImg = function()
								  {
									  $scope.snapshotData= "";
									  $('#pieChartContainer').html(''); //remove canvas from container
									  $('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
								  }
								  
								  $scope.cancelModalPatientImg = function()
								  {
									  	$scope.snapshotData= "";
									  	$scope.filepreview = "";
										$scope.patientImage1 = null;
										angular.element("input[type='file']").val(null);
										$scope.file = "";
										$('#pieChartContainer').html(''); //remove canvas from container
										$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
								  }
								  
								  
								  $scope.checkImageCaptured = function()
								  {
									  if($scope.snapshotData==undefined || $scope.snapshotData=="")
									  {
										  growl.error("Image not Captured!",
													{
														title: 'Warning!'
													});
									  }
									  else{
										  angular.element('#uploadPhoto').modal('hide');
									  }
								  }


			 $scope.openTriageReceptionist= function(activityName) {
				console.log(BASE_URL + ROOT_URL);
				console.log("BASE_URL ", BASE_URL);
				console.log("ROOT_URL" , ROOT_URL);

				  var protocol = $location.protocol();   // http / https
					var host = $location.host();           // domain
					var port = $location.port();           // port (optional)

					var baseUrl = protocol + "://" + host;

				if (port && port !== 80 && port !== 443) {
					baseUrl += ":" + port;
				}

					console.log(baseUrl);
				
				
                        $rootScope.startLoader();
                        $scope.TriageHeader = "Virtual Secretary";
                        // var URI =  "https://gpxucwzdeijkcnygwakq.supabase.co/functions/v1/generate-receptionist-session";
						var URI =  "https://gpxucwzdeijkcnygwakq.supabase.co/functions/v1/generate-kiosk-session";

					
                        var data={
                                userId: "5c8e0c35-436d-4bd9-bd08-ed9944d2fb3c",
                                environment:"qa",
                                languageCode: "en",
                                workspace: "kiosk",
                                unitId:$scope.unitId,
                                organizationId:$scope.organizationId,
								createdBy:$scope.userId,
								baseurl: baseUrl,
								activityName: activityName
                        }
                        GenericService.serviceAction("POST", URI, data) .then(
                            function(response) {
                                if (response.data.success == true) {
                                 $rootScope.stopLoader();
                                 $scope.iframeUrl = $sce.trustAsResourceUrl(response.data.webhookUrl);
                                 $scope.showIframe = true;
                                 angular.element('#iframForTriage').modal('show');
                                }
                        });
            }












				$scope.registerPatient = function(saveFlag)
							{
								 if($scope.patientObj.firstName == '' || $scope.patientObj.firstName == null ||  $scope.patientObj.firstName == undefined){
										growl.warning(
												'Please enter first name...!!',
												{
													title : 'Warning!'
												});
										return false;
										
									}else if($scope.patientObj.prefixId == '' || $scope.patientObj.prefixId == null || $scope.patientObj.prefixId == undefined){
										growl.warning(
												'Please select prefix...!!',
												{
													title : 'Warning!'
												});
										return false;
										
									}else if($scope.patientObj.genderId == '' || $scope.patientObj.genderId == null || $scope.patientObj.genderId == undefined){
										growl.warning(
												'Please select gender...!!',
												{
													title : 'Warning!'
												});
										return false;
										
									}else if($('#datepicker').val() == '' || $('#datepicker').val() == null || $('#datepicker').val() == undefined){
										growl.warning(
												'Please enter DOB...!!',
												{
													title : 'Warning!'
												});
										return false;
									}else if($scope.patientObj.mobileNumber == '' || $scope.patientObj.mobileNumber == null || $scope.patientObj.mobileNumber == undefined){
										growl.warning(
												'Please enter contact number...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
									
									var data = null;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
									$scope.patientObj.createdDate = $scope.currentDate;
									$scope.patientObj.updatedDate = $scope.currentDate;
									$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
									$scope.patientObj.middleName =  ($scope.patientObj.middleName!="" || $scope.patientObj.middleName!=undefined || $scope.patientObj.middleName!=null ? $rootScope.capitalize_Words($scope.patientObj.middleName) : "");
									$scope.patientObj.lastName =    ($scope.patientObj.lastName!="" || $scope.patientObj.lastName!=undefined || $scope.patientObj.lastName!=null ? $rootScope.capitalize_Words($scope.patientObj.lastName): "");
									var dob= $('#datepicker').val();
									
									$scope.patientObj.dob = moment($scope.datepicker=="" ? $('#datepicker').val() : $scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
									
									 console.log($scope.patientObj);
									 $scope.$broadcast('show-errors-check-validity');
						        	  if($scope.patientRegistrationform.$valid)
						        	 {
						        		  
									 var contryCode=$scope.patientObj.countryCallingCode;
									 // validating country code
									 var regEx = /^[+-]?\d+$/;
									      if(!regEx.test(contryCode)){
									    	  growl.warning("Please enter valid country code",{
													title : 'warning'
													});
													return false;
									      } 
									 
							        	var mobileNo=$scope.patientObj.mobileNumber;

								        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
								        			
												}else{
													growl.error("Please enter valid mobile number",{
														title : ERROR_MSG
														});
														$('#mobileNo').focus();
														return false;
							        		}

							        	
									$rootScope.startLoader();
									// update abscond patient as normal patient start
									if($scope.abscondPatientFlag!=undefined  && $scope.abscondPatientFlag!="" && $scope.abscondPatientFlag!=null && $scope.abscondPatientFlag=='Y' && $scope.patientObj.patientId!=null){
									var URI = BASE_URL + ROOT_URL+ "/api/global/updatePatientAsAbscound";					
									var obj={
											patientId:$scope.patientObj.patientId
									}		
									console.log('abscond obj',obj);
									GenericService.serviceAction("POST", URI, obj).then(
											function(response) {	
												if (response.data.status == "success") {
													$scope.abscondPatientFlag="";
													$scope.infoPatient="";
												}
											});
									}
									// update abscond patient as normal patient end
									 if(saveFlag==1)
									 {
											 	
										 		if($scope.patientObj.patientId==null)
												{
												
										 			$scope.patientObj.isFromSelf = 'Y';
										 			$scope.patientObj.appointmentDate=moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
										 			
													//return false;
													
													var URI = BASE_URL + ROOT_URL
											 		+ ADDPATIENTREGISTRATION;

											 	BillingGenericService
											 		.serviceAction(METHOD_POST,
											 				URI, $scope.patientObj,
											 				NOTIFICATION_MSG_STATUS_FALSE)
											 		.then(
											 				function(response) {
											 					console
											 							.log(response);
											 					//$rootScope.startLoader();
											 					if (response.data.status == "success")
											 					{
											 						$scope.tokenNumber = response.data.object.tokenNumber;
											 						
											 						if($scope.snapshotData!=undefined && $scope.snapshotData!="")
																	{
																		if(response.data.object.patientId!=undefined && response.data.object.patientId!=null)
																		{
																			var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
																			  $scope.uploadPatientImage(file, response.data.object.patientId, $scope.organizationId, $scope.unitId);
																			 
																		}
																	  
																	}
											 						
																	$rootScope.stopLoader();
																	angular.element('#tokenDisplayModal').modal('show');
											 					}else if(response.data.code==409){
											 						angular.element('#patientAlreadyExist').modal('show');
											 					}
											 				});
												}
										 		else{
											 		data = {
															isFromKioskToken:'Y',
													        createdDate: $scope.currentDate,
													        unitId : $scope.unitId,
															organizationId : $scope.organizationId,
															createdBy : $scope.userId,
															appointmentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
															patientId : ($scope.patientObj.patientId!=null ? $scope.patientObj.patientId : 0)
															
											 		}
											 		var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/kiosk";
													console.log(data);
											 		BillingGenericService
													.serviceAction(METHOD_POST,
															URI, data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console
																		.log(response);
																//$rootScope.startLoader();
																if (response.data.status == "success")
																{
																	/*growl.success(response.data.message,{
																		title : SUCCESS
																		});*/
																	$scope.tokenNumber = response.data.object.tokenNumber;
//																	
																	if($scope.snapshotData!=undefined && $scope.snapshotData!="")
																	{
																		if(data.patientId!=undefined && data.patientId!=null)
																		{
																			var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
																			  $scope.uploadPatientImage(file, data.patientId, $scope.organizationId, $scope.unitId);
																		}
																	  
																	}
																	
																	
																	$rootScope.stopLoader();
																	angular.element('#tokenDisplayModal').modal('show');
																	$scope.init();
																}else if (response.data.code == 409)
																	{
																		$rootScope.stopLoader();
																		$scope.alreadyExistKioskAppointment = response.data.message;
																		angular.element('#alreadyExistAppointmentKiosk').modal('show');
																	}
																
															});	
											
											 		
											 		
												}
												
									 }else{
										 
										 if($scope.patientObj.patientId==null){
											 $scope.patientObj.registrationTypeId=1;
											 var URI = BASE_URL + ROOT_URL
										 		+ "/api/global/patientRegistration";

										 	BillingGenericService
										 		.serviceAction(METHOD_POST,
										 				URI, $scope.patientObj,
										 				NOTIFICATION_MSG_STATUS_FALSE)
										 		.then(
										 				function(response) {
										 					console
										 							.log(response);
										 					//$rootScope.startLoader();
										 					if (response.data.status == "success")
										 					{
										 						if(response.data.code==409)
										 							{
										 								$rootScope.stopLoader();
										 								angular.element('#patientAlreadyExist').modal('show');
										 							}
										 						else{
										 							
										 							$scope.patientObj.isFromSelf = 'Y';
										 							$scope.patientObj.patientCategoryId = $scope.defaultPatientCategoryId;
																	   $scope.patientObj.appointmentDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
																	   $scope.patientObj.snapshotData =  $scope.snapshotData;
																	   $rootScope.stopLoader();
																	   $scope.dept=true;
																	   $scope.doc=false;
																	   $scope.slot=false;
																	   $scope.saveAndPayEnable = true;
																	   $state.go("walkinAppointment", {
																			patientDetailsObj: $scope.patientObj,
																		});
										 						
										 						}  
										 					}else{
										 					
										 						$scope.back();
										 						
										 					}
										 				});
											 
											 
										 }else{
											   $scope.patientObj.isFromSelf = 'Y';
											   $scope.patientObj.appointmentDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
											   $rootScope.stopLoader();
											   $scope.dept=true;
											   $scope.doc=false;
											   $scope.slot=false;
											   $scope.saveAndPayEnable = true;
											   $scope.patientId = $scope.patientObj.patientId;
											   $scope.patientObj.snapshotData =  $scope.snapshotData;
											   $state.go("walkinAppointment", {
													patientDetailsObj: $scope.patientObj
											   	})
										 }
										 
									 }
						        }
							}


		$scope.listPaymentDetails=[];
        $scope.trasactionIdFromRazorPay='';							
        $scope.openRazorPay = () =>{
		$rootScope.startLoaderPopup();
		let url= BASE_URL + ROOT_URL + "/getOrderIdUnitSubscriptionPay";
		let data = {
			unitId : $scope.unitId,
			organizationId : $scope.organizationId,
			amount:$scope.patientAppoObjPayment.totalBillAmount,
			razorPayBillAmount:$scope.patientAppoObjPayment.totalBillAmount+"00",
		}
		let orderId = null;
		var httpObject = {
			method : METHOD_POST,
			url : url,
			data : data
		};
		var promise = $http(httpObject).then(
			(response) =>
			{
				if (response.data.status == "success")
				{
					orderId= response.data.object.orderId;
					var options = {
						"key": response.data.resObject,
						"amount": $scope.patientAppoObjPayment.totalBillAmount+"00",
						"currency": "INR",
						"name": "Axonic Healthcare",
						"description": "Test Transaction",
						"image": "https://example.com/your_logo",
						"order_id": orderId,
						
						
					"handler": function (response) {
						console.log("Razor Pay Response:", response);

						// Wrap in $apply to trigger Angular digest
						$scope.$apply(function () {

							// Save transaction ID
							$scope.trasactionIdFromRazorPay = response.razorpay_payment_id;

							// Create payment object
							$scope.paymentRazor = {
								paymentModeId: 8,
								transactionNo: $scope.trasactionIdFromRazorPay,
								transactionType: 'Razor Pay', // or 'Phone Pay' if that's intentional
								amount: $scope.patientAppoObjPayment.totalBillAmount
							};

							// Add to list (do not reset the array)
							$scope.listPaymentDetails.push($scope.paymentRazor);

							$scope.settleTranaction();

							console.log("Updated payment list:", $scope.listPaymentDetails);

							// Optional: call backend to verify/save payment
							// $scope.verifyRazorPayPayment(response);
						});
					},
						// "callback_url": BASE_URL + ROOT_URL + "/unit/subcription",
						"prefill": {
							// "name": this.paymentObj.unitName,
							// "email": this.paymentObj.unitEmailId,
							// "contact": this.paymentObj.unitContact
							"name": "Axon Demo",
							"email": "vivek.satle@axonichealth.com",
							"contact": "9834614511"
						},
						"notes": {
							"address": "Razorpay Corporate Office"
						},
						"theme": {
							"color": "#3399cc"
						}
					};
					var rzp1 = new Razorpay(options);
					rzp1.open();
					$rootScope.stopLoaderPopup();
				}else{
					$rootScope.stopLoaderPopup();
					growl.error(response.data.message,
						{
							title : 'Error!'
						});
				}


			},

			(error) => {
				$rootScope.stopLoaderPopup();
				return Promise.reject(error);
			}
			
		);
		return promise;
	  };

        $scope.patientMobileNumber='';
	  	$scope.showForgetPasswordModal = function(mobileNumber)
	{
		$rootScope.existingPassword = ""; 
		$rootScope.password = ""; 
		$rootScope.confirmPassword = "";
		$rootScope.emailId = "";
		$rootScope.otp = "";
		$scope.patientMobileNumber= mobileNumber;
		$('#forgotPasswordModal').modal('show');
		
	}



	$scope.getOTPForValidation = function () 
	{		
		$rootScope.startLoader();
		if($scope.patientMobileNumber!='' && $scope.patientMobileNumber!=null && $scope.patientMobileNumber!=undefined){ {
		var data = {"mobileNumber": $scope.patientMobileNumber,};
		var URI = BASE_URL + ROOT_URL + "/adt/generateSMSOtp";
		GenericService.serviceAction("POST", URI, data).then(function (response) {
			if (response.data.status == "success") 
			{
				growl.success("OTP send Successfully. Please check your email.",{
					title : SUCCESS_MSG
				});
				$('#forgotPasswordModal').modal('hide');
				$('#forgotPasswordModal_enterOTP').modal('show');
				$rootScope.stopLoader();
			}
			else{
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$rootScope.stopLoader();
			}

		  });
		}
	}
}

	$scope.smsOtpPatient="";
	$scope.showPatientInfo = false;
	$scope.OTPVerification = function () 
	{
		if($scope.smsOtpPatient==undefined || $scope.smsOtpPatient=="" || $scope.smsOtpPatient == null){
			growl.warning("Please enter valid OTP.",{
				title : "warning"
			});
			return false;
		}
		if($scope.smsOtpPatient.length!=4){
			growl.warning("Please enter valid OTP.",{
				title : "warning"
			});
			return false;
		}
	
		$rootScope.startLoader();
		var data = {"mobileNumber": $scope.patientMobileNumber, 
				   "otp":$scope.smsOtpPatient
			}

		var URI = BASE_URL + ROOT_URL + "/adt/validateSMSOtp";
		GenericService.serviceAction("POST", URI, data).then(function (response) {
			var counterList1 = [];
			if (response.data.status == "success") 
			{
				$scope.showPatientInfo = true;
				growl.success("OTP Verified Sucessfully.",{
					title : SUCCESS_MSG
				});
				$('#forgotPasswordModal_enterOTP').modal('hide');
				// $('#forgotPasswordModal_newPassword').modal('show');
				
				$rootScope.stopLoader();
			
				
				// $rootScope.userIdFrogetPwd=response.data.code;
			}
			else if (response.data.status == "error") {
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$rootScope.stopLoader();
			}
	    	});
		
	}
	

	$scope.patientCalled= function(payload){
      var data = {
				encounterId : payload.encounterId,
				isCalled : 'Y',
				orderDetailId : payload.orderDetailId,
			}
			var URI = BASE_URL + ROOT_URL+"/api/opd/patientCalled";
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
														$rootScope.stopLoader();											
													
													
												});
				}
	$scope.encounterDetails = function(payload){
       var data = {
				assignmentStartTime : payload.assignmentStartTime,
				isAssignmentCompStatus : 'N',
				assignmentStartBy : payload.assignmentStartBy,
				assignmentStartRoleId : 3,
				isConsultationStatus :'N',
				encounterId : payload.encounterId,
				
				patientId: payload.patientId

			}

			var headerObj={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
					};

			var URI = BASE_URL + ROOT_URL+"/api/opd/encounterDetails";	
								
								// return BillingGenericService
								// 		.serviceAction(METHOD_POST, URI,
								// 				data,headerObj,
								// 				NOTIFICATION_MSG_STATUS_FALSE)
                             var paramObj = {};

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
												
												
										.then(
												function(response) {
														$rootScope.stopLoader();											
													
													
												});


	}


		$scope.searchAppointmentsUniqueData = function(appointmentId) {
								//alert($scope.searchobj.appointmentUniqueId);
								//alert(document.getElementById('appuniq').value);
								$rootScope.startLoader();
								var data = {
									appointmentId : appointmentId,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									isFromKioskToken : 'N'
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL+"/api/global/patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn";
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
														$rootScope.stopLoader();
														// $scope.searchobj.appointmentUniqueId= null;
													if (response.data.status == "success")
														if(response.data.listObject.length==0){
															growl.warning("Appointment not found for the given unique ID.",{title : "WARNING!"});
															return false;
														}
														
														
														var info =  response.data.listObject[0];

														console.log("@@@@@@@@@@@@@@@@@ info ",info);
														$scope.patientCalled(info);
														$scope.encounterDetails(info);
													
													
													if(info.admissionId>0)
													{
														growl.warning("Patient is currently admitted...!!",{title : "WARNING!"});
														return false;
													}
													
													$scope.currentDate = new Date();
													$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY');
													
													$rootScope.startLoader();
													if(info.currentDate!=null && $scope.currentDate==info.currentDate && info.appointmentStatusId==1){
														angular.element('#alreadyHasAppointmentPopUp').modal('show');
													}
													// $scope.patientAppoObj.patientId = info.patientId;
													// $scope.patientAppoObj.uhidNumber = info.uhidNumber;
													// $scope.patientAppoObj.prefixCode = info.prefixCode;
													// $scope.patientAppoObj.patientName = info.patientName;
													// $scope.patientAppoObj.genderCode = info.genderCode;
													// $scope.patientAppoObj.organizationId = $scope.organizationId,
													// $scope.patientAppoObj.unitId = info.patientId;
													// $scope.patientAppoObj.patientCategoryId= info.patientCategoryId;
													// $scope.patientAppoObj.dateOfBirth = info.dob;
													// $scope.patientAppoObj.appointmentDate = info.appointmentDate;
													// $scope.patientAppoObj.appointmentTime = info.appointmentTime;
													// $scope.patientAppoObj.age = info.ageString;
													// $scope.datepicker = new Date(info.dob);
													// $scope.patientAppoObj.appointmentId = info.appointmentId;
													// $scope.patientAppoObj.specialityId = info.specialityId;
													// $scope.patientAppoObj.isDeptWise = info.deptWiseAppointment;
													// $scope.patientAppoObj.specialityName = info.specialityName;
													// $scope.patientAppoObj.doctorId = info.doctorId;
													// $scope.patientAppoObj.doctorName = info.doctorName;
													// $scope.patientAppoObj.isPaidAppointment = info.isPaidAppointment;
													// $scope.patientAppoObj.encounterId = info.encounterId;
													// $scope.patientAppoObj.typeId = info.typeId;
													// $scope.consultingDoctorName = info.doctorName;
													$rootScope.stopLoader();
													$scope.saveEnable = false;
													if(info.isPaidAppointment=='Y'){
														$scope.saveAndPayEnable = true;
													}else{
														$scope.saveAndPayEnable = false;
													}
													
													console.log('patientAppoObj',$scope.patientAppoObj);
													if(info.doctorId!=undefined && info.doctorId!=null){
														$scope.getRegistrationNumber(info.doctorId);
													}
												
													
													
												});

							}


							  $scope.showAppointmentPopUp = function(){
								angular.element('#showAppointmentPopUp').modal('show');
								// alert("showAppointmentPopUp");
							}

							$scope.saveUserInput = function(saveFlag){
								angular.element('#showAppointmentPopUp').modal('hide');
								angular.element('.modal-backdrop').remove();
								angular.element('body').removeClass('modal-open');
								angular.element('body').css('padding-right', '');
								
								if(saveFlag == true){
									
									$state.go("walkinAppointment", {
									patientDetailsObj: $scope.patientObj,
								});
										 						
								}else{
									angular.element('#showAppointmentPopUp').modal('hide');
									$state.go('kiosk');
								}
							}





	$scope.bookAppoitmentFromKiosWithoutBilling = function()
		{
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
			$rootScope.startLoader();
			var data = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					patientId : ($scope.patientObj.patientId!=null ? $scope.patientObj.patientId : null),
					doctorId : $scope.doctorId,
					specialityId : $scope.specialityId,
					currentDate :  moment(new Date()).format('YYYY-MM-DD'),
					patientCategoryId : $scope.patientObj.patientCategoryId !=null ? $scope.patientObj.patientCategoryId : $scope.defaultPatientCategoryId,
					deptWiseAppointment : $scope.isDeptWise
			}
			console.log('data',data);
				$scope.initPaymentDetails();
			if($scope.patientObj.patientId!=null && $scope.patientObj.patientId!=undefined ){
				$scope.getLoyaltyCardList($scope.patientObj.patientId);
			}
		
		
			var URI = BASE_URL + ROOT_URL + "/api/opd/billAmount/kiosk"
			BillingGenericService
					.serviceAction(METHOD_POST,
							URI, data,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								console
										.log(response);
								if (response.data.status == "success")
								{
									
									$scope.patientObj.totalBillAmount = response.data.object.finalPrice;
									$scope.patientObj.serviceAmount = response.data.object.serviceRate;
									$scope.patientObj.taxAmount = response.data.object.taxAmt;
									$scope.patientObj.systemDefiedVisitType = response.data.object.systemDefiedVisitType;
									
									if($scope.doctorId > 0){
									var docObj = $.grep($scope.doctorList, function (doc) {
										return doc.doctorId == $scope.doctorId;
									})[0];
									$scope.patientObj.doctorName = "Dr. "+docObj.firstName+" "+docObj.lastName;
									$scope.patientObj.doctorId =  $scope.doctorId;
									$scope.patientObj.specialityName = docObj.specialityName;
									}
									else{
										
										var speObj = $.grep($scope.specialityList, function (spe) {
											return spe.specialityId ==  $scope.specialityId;
										})[0];
										$scope.patientObj.specialityName = speObj.specialityName;
										$scope.patientObj.isNursingAssessmentRequired  = speObj.isNursingAssessmentRequired;
									}
									$scope.patientObj.specialityId = $scope.specialityId;
									console.log( $scope.patientObj);
									
									$scope.patientObj.dueAmount =parseFloat($scope.patientObj.totalBillAmount);
									$scope.dueAmount =parseFloat($scope.patientObj.totalBillAmount);
									$scope.patientObj.payeeDueAmtFinal=parseFloat($scope.patientObj.totalBillAmount);
										$scope.patientObj.payeeDueAmtStatic = Number(parseFloat($scope.patientObj.totalBillAmount).toFixed(2));
										$scope.patientObj.paymentDetailsObj=$scope.paymentDetails;
										$scope.patientObj.loyaltyCardObj= $scope.loyaltyCardObj;
										$scope.patientObj.isPaidAppointment = $scope.isPaidAppointment;
										$scope.patientObj.deptWiseAppointment = $scope.isDeptWise;
										$scope.paymentDetails.loyaltyCardAmt = $scope.patientObj.totalBillAmount;
									console.log( "new added",$scope.patientObj);
									// $rootScope.stopLoader();
									// $state.go("paymentInformation", {
									// 	patientAppoObjPayment: $scope.patientObj,
									// });
									$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ SCHEDULEAPPOINTMENT;
                                var patientAppointmentBookingObj = {
									patientId : ($scope.patientObj.patientId!=null ? $scope.patientObj.patientId : null),
                                    fromTimeString :$scope.patientObj.appointmentTime,
									slotId :$scope.patientObj.slotId,
									remark :"",
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate :$scope.patientObj.createdDate,
									updatedDate :$scope.patientObj.updatedDate,
									createdBy :$scope.patientObj.createdBy,
									updatedBy :$scope.patientObj.updatedBy,
									status :'A',
									firstName :$scope.patientObj.firstName,
									mobileNumber :$scope.patientObj.mobileNumber,
									doctorName :$scope.patientObj.doctorName,
									appointmentDate2 :$scope.patientObj.appointmentDate,
									doctorId :$scope.patientObj.doctorId,
									specialityId :$scope.patientObj.specialityId,
									isTeleconsultationAppointment:0,
									specialityName :$scope.patientObj.specialityName,
									prefixId :$scope.patientObj.prefixId,
									middleName :$scope.patientObj.middleName,
									lastName :$scope.patientObj.lastName,
									dob :$scope.patientObj.dob,
									email :$scope.patientObj.email,
									genderId :$scope.patientObj.genderId,
									bloodGroup :"",
									registrationTypeId :$scope.patientObj.registrationTypeId,
									appointmentTypeId:"1",
									slotStatusId:4,
									priorityId:1,
									allowedOverBookingSlots:"5"

								}
								BillingGenericService
								.serviceAction(
									METHOD_POST,
									URI,
									patientAppointmentBookingObj,
									NOTIFICATION_MSG_STATUS_TRUE
								)
								.then(function (response) {
									console.log(response);

									if (response.data && response.data.status === "success") {
//									   growl.success(response.data.message, {
//											title: 'success!',
//										});
											$rootScope.stopLoader();
										
										// $state.go("kiosk");
										$scope.openQrPopup(response.data.object.appointmentUniqueId);
										// $state.go("kiosk", {}, { reload: true });
									}
								})
								.catch(function (error) {
									console.error("Schedule appointment failed", error);
								});


									
								}else{
									angular.element('#consultationConfigNotExist').modal('show');
									$scope.saveAndPayEnable = true;
									
									return false;
								}
							});
	
		}



		$scope.openQrPopup = function (uniqueId) {

        if (!uniqueId) {
            return;
        }

        // Build QR URL
        $scope.qrUrl =
            BASE_URL + ROOT_URL + "/adt/br/" + uniqueId;

        // Open modal
        angular.element('#showQrPopup').modal('show');
    };

    /* ---------------- CLOSE POPUP ---------------- */
    $scope.closeQrPopup = function () {
        $scope.qrUrl = null;
        angular.element('#showQrPopup').modal('hide');
		angular.element('.modal-backdrop').remove();
		angular.element('body').removeClass('modal-open');
		angular.element('body').css('padding-right', '');
		$state.go("kiosk", {}, { reload: true });
    };

    /* ---------------- PRINT QR ---------------- */
    $scope.printQr = function () {

        var printContents =
            document.getElementById('printQrSection').innerHTML;

        var popupWin = window.open('', '_blank', 'width=400,height=500');

        popupWin.document.open();
        popupWin.document.write(`
            <html>
                <head>
                    <title>Print QR</title>
                    <style>
                        body {
                            text-align: center;
                            font-family: Arial, sans-serif;
                            margin-top: 30px;
                        }
                        h4 {
                            margin-bottom: 20px;
                            font-size: 16px;
                        }
                        img {
                            width: 200px;
                            height: 200px;
                        }
                    </style>
                </head>
                <body onload="window.print(); window.close();">
                    ${printContents}
                </body>
            </html>
        `);
		
        popupWin.document.close();
		$scope.closeQrPopup();

    };




	 $scope.mediaRecorder = null;
    $scope.enableAllSaveCalls = true;
  $scope.audioChunks = [];
  $scope.timer = null;
  $scope.seconds = 0;
  $scope.recordTime = "00:00:00";
  $scope.audioURL = null;      // for audio preview
  $scope.audioBase64 = null;   // for base64 string


  $scope.formatTime = function(sec) {
    var h = String(Math.floor(sec / 3600)).padStart(2, '0');
    var m = String(Math.floor((sec % 3600) / 60)).padStart(2, '0');
    var s = String(sec % 60).padStart(2, '0');
    return h + ":" + m + ":" + s;
  };


  $scope.startRecording = async function() {
    $('#micIcon').attr('style', 'color: red !important;');

   $scope.enableAllSaveCalls = false;
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    $scope.mediaRecorder = new MediaRecorder(stream);
    $scope.audioChunks = [];
    $scope.audioURL = null;
    $scope.audioBase64 = null;

    $scope.mediaRecorder.ondataavailable = function(e) {
      $scope.audioChunks.push(e.data);
    };

    $scope.mediaRecorder.start();
    $scope.seconds = 0;

    $scope.timer = $interval(function() {
      $scope.seconds++;
      $scope.$applyAsync(function() {
        $scope.recordTime = $scope.formatTime($scope.seconds);
      });
    }, 1000);
  };



$scope.isPaused = false;

$scope.pauseRecording = function () {
    if (!$scope.mediaRecorder) return;

    if ($scope.mediaRecorder.state === "recording") {
       
        $scope.mediaRecorder.pause();
        $interval.cancel($scope.timer);
        $scope.isPaused = true;
    $scope.enableAllSaveCalls = true;

    } else if ($scope.mediaRecorder.state === "paused") {
       
        $scope.mediaRecorder.resume();

        $scope.timer = $interval(function () {
            $scope.seconds++;
            $scope.recordTime = $scope.formatTime($scope.seconds);
        }, 1000);

        $scope.isPaused = false;
    $scope.enableAllSaveCalls = false;
    }
};


$scope.stopAndSave = function () {
  $('#micIcon').attr('style', 'color: blue !important;');
    if (!$scope.mediaRecorder) return;
    $rootScope.startLoader();
    $scope.mediaRecorder.stop();
    $interval.cancel($scope.timer);

    $scope.mediaRecorder.onstop = function () {

        var audioBlob = new Blob($scope.audioChunks, { type: 'audio/webm' });

        // AUDIO PREVIEW (safe apply)
        $scope.audioURL = URL.createObjectURL(audioBlob);

        // CONVERT TO BASE64
        var reader = new FileReader();

        reader.onloadend = function () {
            var base64String = reader.result.split(',')[1];

            $scope.$applyAsync(function () {
                $scope.audioBase64 = base64String;
            });

            console.log("Base64 Audio:", base64String);

          
            $scope.uploadAudio(base64String);
        };

   
        reader.readAsDataURL(audioBlob);
    };
};



  $scope.processData=[];

  $scope.processData=JSON.parse(`[
    {
        "Complaints": {
            "admissionId": 26726,
            "createdBy": 3494,
            "createdDate": "25-12-2025 05:28:00",
            "listComplaintAdmissionDetailsDto": [
                {
                    "complaintAdmissionId": "0",
                    "duration": "2-3 days",
                    "icpcCode": "R05",
                    "icpcCodeText": "Cough",
                    "icpcSystemId": 3,
                    "isReviewedFlag": "Y",
                    "remark": "Associated with white sputum.",
                    "since": ""
                },
                {
                    "complaintAdmissionId": "0",
                    "duration": "2-3 days",
                    "icpcCode": "R74",
                    "icpcCodeText": "Common cold",
                    "icpcSystemId": 3,
                    "isReviewedFlag": "Y",
                    "remark": "Associated with sore throat and a history of mild fever.",
                    "since": ""
                }
            ],
            "patientId": 325953,
            "roleId": 2,
            "status": "A",
            "updatedBy": 3494,
            "updatedDate": "25-12-2025 05:28:00"
        }
    },
    {
        "Diagnosis": {
            "admissionId": 26726,
            "createdBy": 3494,
            "createdDate": "25-12-2025 05:28:00",
            "listDiagnosisPatientAppointmentDetailsDto": [
                {
                    "code": "",
                    "diagnosisCode": "",
                    "diagnosisName": "",
                    "diagnosisNameText": "Viral Upper Respiratory Infection",
                    "diagnosisType": "provisional",
                    "icd10Code": "J06",
                    "isDiagnosisName": "N",
                    "remark": "",
                    "selectedOrderSet": [],
                    "type": "1"
                }
            ],
            "status": "A",
            "updatedBy": 3494,
            "updatedDate": "25-12-2025 05:28:00"
        }
    },
    {
        "PhysicianNote": {
            "admissionId": 26726,
            "createdBy": 3494,
            "createdDate": "25-12-2025 05:28:00",
            "doctorId": 1449,
            "observationDesc": "Patient presents with cough and cold for 2-3 days, sore throat, and white sputum. History of mild fever, now resolved. No dyspnea or chest pain. History of contact with sick colleagues at the office. Provisional diagnosis is Viral Upper Respiratory Infection. Advised Paracetamol for fever or body ache as needed (max 3 times a day, not on an empty stomach) and a cough syrup 2-3 times a day after meals. Advised for a CBC test if symptoms do not improve or worsen in 3-4 days. Recommended avoiding cold water, ice cream, and fried food, increasing fluid intake, and taking rest. Advised to return immediately if there is no improvement in 3-4 days, fever increases, or if breathing difficulty occurs.",
            "organizationId": 18,
            "patientId": 325953,
            "status": "A",
            "unitId": 18,
            "updatedBy": 3494,
            "updatedDate": "25-12-2025 05:28:00"
        }
    }
]`);


 $scope.getDianosisCodeFromIc10Code = function (keyword) {
  console.log("Calling API...");

  var data = {
                        diagnosisName: keyword.split('.')[0]
                };
                var URI = BASE_URL + ROOT_URL
                        + "/api/opd/searchDiagnosisByCodeOrDescription";
                
                return GenericService
                        .serviceAction("POST", URI, data)
                        .then(
                                function(response) {
                                    console.log(response);
                                    // $rootScope.startLoader();
                                    if (response.data.status == "success")
                                        if (response.data.status == "success")
                    if(response.data.listObject!=null && response.data.listObject.length>0)
                                        return response.data.listObject[0].diagnosisId;
                                });               
 }


 $scope.getICPCIdByCode = function (code) {
  console.log("Calling API...");

  var data = {
                       
                };
                var URI = BASE_URL + ROOT_URL
                        + "/api/opd/getICPCIdByCode?icpcCode="+code;
                
                return GenericService
                        .serviceAction("GET", URI, data)
                        .then(
                                function(response) {
                                    console.log(response);
                                    // $rootScope.startLoader();
                                    if (response.data.status == "success")
                    if(response.data.listObject!=null && response.data.listObject.length>0)
                                        return response.data.listObject[0].icpcId;
                                });               
 }




 $scope.saveDianosisFromAIdata  = function (diagnosisDataArray) {
var paramObj = {};
var headers = {
  'organization_id': $scope.organizationId,
  'unit_id': $scope.unitId,
  'patient_id':  $scope.patientAppoObjPayment.patientId,
  'Content-Type': 'application/json'
};

var data = {

                  created_date : moment(new Date()).format(
                      'DD-MM-YYYY HH:mm:ss'),
                  updated_date : moment(new Date()).format(
                      'DD-MM-YYYY HH:mm:ss'),
                  updated_by : $scope.userId,
                  created_by : $scope.userId,
                  status : 'A',
                  admissionId :$scope.recordingPayload.encounterId,
                  listDiagnosisPatientAppointmentDetailsDto: diagnosisDataArray

                };

var URI = BASE_URL + ROOT_URL + SAVEPATIENTDIAGNOSIS;

GenericServiceParamHeader
  .serviceAction(
    METHOD_POST,
    URI,
    data,
    NOTIFICATION_MSG_STATUS_FALSE,
    paramObj,
    headers
  )
  .then(
    function (response) {
      console.log("Save Dianosis Response:", response);

      if (response.data && response.data.status === "success") {
        console.log("Complaints saved successfully");
    // $scope.getPatientDiagnosis();
      } else {
        console.error("Save failed:", response.data.message);
    // $scope.getPatientDiagnosis();
      }
    },
    function (error) {
      console.error("API error while saving complaints", error);
    }
  );



}





$scope.saveComplaintFromAIdata  = function (complaintsDataArray) {
var paramObj = {};
var headers = {
  'organization_id': $scope.organizationId,
  'unit_id': $scope.unitId,
  'Content-Type': 'application/json'
};

var data = {

                  createdDate : moment(new Date()).format(
                      'DD-MM-YYYY HH:mm:ss'),
                  updatedDate : moment(new Date()).format(
                      'DD-MM-YYYY HH:mm:ss'),
                  updatedBy : $scope.userId,
                  createdBy : $scope.userId,
                  status : 'A',
                  roleId : $scope.nurseRoleId=null ||$scope.nurseRoleId==undefined ? $cookies.getObject('cookieObjectAppConfig').nurseRole : $scope.nurseRoleId,
                  admissionId : $scope.recordingPayload.encounterId,
                  patientId : $scope.patientAppoObjPayment.patientId,
                  listComplaintAdmissionDetailsDto: complaintsDataArray

                };

var URI = BASE_URL + ROOT_URL + SAVE_COMPLAINT_ADMISSION;

GenericServiceParamHeader
  .serviceAction(
    METHOD_POST,
    URI,
    data,
    NOTIFICATION_MSG_STATUS_FALSE,
    paramObj,
    headers
  )
  .then(
    function (response) {
      console.log("Save Complaint Response:", response);

      if (response.data && response.data.status === "success") {
        console.log("Complaints saved successfully");
    // $scope.getActiveComplaintlist();
      } else {
        console.error("Save failed:", response.data.message);
    // $scope.getActiveComplaintlist();
      }
    },
    function (error) {
      console.error("API error while saving complaints", error);
    }
  );



}




$scope.complaints = null;
$scope.diagnosis = null;
$scope.physicianNote = null;
$scope.observationObj = {
								observationDesc : ""
							};

 $scope.processCaseSheetData = function (processDataArray) {

  angular.forEach(processDataArray, function (item) {

    /* ---------------- COMPLAINTS ---------------- */
    if (item.Complaints) {

      $scope.complaints = item.Complaints;
      $scope.AibasedComplaints = [];

      var total = $scope.complaints.listComplaintAdmissionDetailsDto.length;
      var completed = 0;

      if (total === 0) {
        $scope.saveComplaintFromAIdata([]);
        return;
      }

      angular.forEach($scope.complaints.listComplaintAdmissionDetailsDto, function (complaint) {

        $scope.getICPCIdByCode(complaint.icpcCode).then(function (icpcId) {
      if(!icpcId){
        return;
      }

          var tempObject = {};
          tempObject.diagnosisId    = icpcId;              // ✅ correct
      var parts = (complaint.duration || "").trim().split(" "); // ["2-3", "days"]
      var rangePart = parts[0] || "";                             // "2-3"

// Take only the first number before "-"
      tempObject.since = rangePart.split("-")[0] || "";           // "2"
      tempObject.duration = parts[1] ? parts[1].charAt(0).toUpperCase() + parts[1].slice(1) : ""; // "Days"

          tempObject.remark         = complaint.remark;
          tempObject.isReviewedFlag = 'Y';
          tempObject.icpcSystemId   = complaint.icpcSystemId;
          tempObject.icpcCodeText   = complaint.icpcCodeText;

          $scope.AibasedComplaints.push(tempObject);

        }).finally(function () {

          completed++;

          // ✅ call save ONLY when all async calls finish
          if (completed === total) {
            console.log('ALL DONE', $scope.AibasedComplaints);
            $scope.saveComplaintFromAIdata($scope.AibasedComplaints);
          }
        });
      });
    }

        /* ---------------- EXAMINATION ---------------- */
     if (item.Examination) {

          if (item.Examination.GeneralExamination) {
            var genExam = item.Examination.GeneralExamination;
            $scope.descriptiveExamination = genExam.descriptiveExamination || "AI Generated Response";
            $scope.validateexamGen = true;
            $scope.popUpFlag = false;
            $scope.saveGeneralExamination(1);
          }
          if (item.Examination.SystemicExamination) {
            var sysExam = item.Examination.SystemicExamination;
            $scope.sysDescriptiveExamination = sysExam.descriptiveExamination || "AI Generated Response";
            $scope.validateexamSys = true;
            $scope.popUpFlag = false;
            $scope.saveGeneralExamination(2); // typeId = 2
          }
    }

    /* ---------------- DIAGNOSIS ---------------- */
    if (item.Diagnosis) {
       $scope.diagnosis = item.Diagnosis;
      $scope.AibasedDiagnosis = [];

      var total = $scope.diagnosis.listDiagnosisPatientAppointmentDetailsDto.length;
      var completed = 0;

      if (total === 0) {
        $scope.saveDianosisFromAIdata([]);
        return;
      }

      angular.forEach($scope.diagnosis.listDiagnosisPatientAppointmentDetailsDto, function (diagnosis) {

         $scope.getDianosisCodeFromIc10Code(diagnosis.icd10Code).then(function (diagnosisId) {

        if(!diagnosisId){
        return;
      }
          var tempObject = {};
          tempObject.diagnosisId    = diagnosisId;              // ✅ correct


          tempObject.diagnosisType = diagnosis.diagnosisType=="provisional"?"1":"2";
          tempObject.type = diagnosis.type;
          tempObject.remark   = diagnosis.remark;
          tempObject.isDiagnosisName   = "N";
      tempObject.diagnosisName= diagnosis.isDiagnosisName+"|"+diagnosis.icd10Code;
      tempObject.diagnosisCode= diagnosis.icd10Code;
      tempObject.diagnosisNameText="";
      tempObject.selectedOrderSet=[];
      tempObject.code=diagnosis.icd10Code;
          $scope.AibasedDiagnosis.push(tempObject);

        }).finally(function () {

          completed++;

          // ✅ call save ONLY when all async calls finish
          if (completed === total) {
            console.log('ALL DONE', $scope.AibasedDiagnosis);
      if($scope.AibasedDiagnosis.length > 0){
               $scope.saveDianosisFromAIdata($scope.AibasedDiagnosis);
      }
          }
        });
      });
    }

    /* ---------------- PHYSICIAN NOTE ---------------- */
    if (item.PhysicianNote) {
      $scope.physicianNote = item.PhysicianNote;
    $scope.observationObj.observationDesc = $scope.physicianNote.observationDesc;
    $scope.saveObservation();
    }
    if (item.NursingNote) {
          $scope.nursingNoteObj.nursingNoteDesc = "AI Generated Response";
          $scope.nursingNoteObj.template = item.NursingNote.nursingNoteDesc;
          $scope.saveNursingNote();
        }

  });
  $rootScope.stopLoader();
};



 $scope.uploadAudio = function (base64) {
  //   $rootScope.startLoader();

    console.log("Calling API...");
	console.log($scope.recordingPayload);
	  $scope.recordTime = "00:00:00";
      $scope.isPaused = false;
      $scope.enableAllSaveCalls = true;
	

$.ajax({
  url: "https://aidata.axonichealth.com/scribeprocess",
  type: "POST",
  contentType: "application/json",
  dataType: "json",
  data: JSON.stringify({
    file: base64,
    admissionId:$scope.recordingPayload.encounterId,
    patientId: $scope.patientAppoObjPayment.patientId,
    doctorId: $scope.patientAppoObjPayment.doctorId,
    created_by: $scope.userId,
    updated_by: $scope.userId,
    organizationId: $scope.organizationId,
    unitId: $scope.unitId,
    status: "A",
    roleId: $scope.nurseRoleId=null ||$scope.nurseRoleId==undefined ? $cookies.getObject('cookieObjectAppConfig').nurseRole : $scope.nurseRoleId
  }),

  success: function (res) {
    $scope.$applyAsync(function () {

    console.log("RES ",res);
    
       $scope.processData = res;
      //REOVE LATER BELOW ONE AND UNCOMMENT UPPER ONE
    //$scope.processData = $scope.processData;
      console.log("processData:", JSON.stringify($scope.processData));
        if (!angular.isArray($scope.processData)) {
          $scope.processData = [$scope.processData];
        }
      $scope.processCaseSheetData($scope.processData);
      // (Optional) reset UI
      $scope.recordTime = "00:00:00";
      $scope.isPaused = false;
      $scope.enableAllSaveCalls = true;



    });

    $rootScope.stopLoader();
    console.log("SUCCESS", res);
  },

  error: function (err) {
    console.error("ERROR", err);

    $scope.$applyAsync(function () {
      $scope.recordTime = "00:00:00";
      $scope.isPaused = false;
      $scope.enableAllSaveCalls = true;
    });

    $rootScope.stopLoader();
  }
});

};


$scope.openInfoPopup = function () {

    var pdfUrl = "https://axon-buddies.s3.ap-south-1.amazonaws.com/Medical_Speaking_Guidance_EN_HI.pdf#toolbar=0&navpanes=0&scrollbar=0";
   	$scope.trustedPdfUrl = $sce.trustAsResourceUrl(pdfUrl);
    angular.element('#infoPopup').modal('show');
};

  $scope.recordingPayload = null;
  $scope.openRecordingPopup = function (payload) {
		$scope.recordingPayload = payload;
		$scope.mainTitle = "Patient Complaints"
		$scope.mainDiv = false;
    };

    $scope.closeRecordingPopup = function () {

        angular.element('#recordingPopup').modal('hide');
		angular.element('.modal-backdrop').remove();
		angular.element('body').removeClass('modal-open');
		angular.element('body').css('padding-right', '');
		$state.go("kiosk", {}, { reload: true });
    };

	$scope.closeInfoPopup = function () {

		angular.element('#infoPopup').modal('hide');
		angular.element('.modal-backdrop').remove();
		angular.element('body').removeClass('modal-open');
		angular.element('body').css('padding-right', '');
	};





			$scope.saveNursingNote = function(){				
				
				if($scope.nursingNoteObj.nursingNoteDesc=="" || $scope.nursingNoteObj.nursingNoteDesc==null ||
						$scope.nursingNoteObj.nursingNoteDesc==undefined){
					growl.warning("Please Enter Name", {
						title : 'Warning!'
					});
					$rootScope.stopLoader();
					return false;
				}
				
				if($scope.nursingNoteObj.template=="" || $scope.nursingNoteObj.template==null ||
						$scope.nursingNoteObj.template==undefined){
					growl.warning("Please Enter Template Details", {
						title : 'Warning!'
					});
					$rootScope.stopLoader();
					return false;
				}
				
				
				var data = {
						nursingNoteId:$scope.nursingNoteObj.nursingNoteId!=undefined? $scope.nursingNoteObj.nursingNoteId:null,
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedBy:$scope.userId,
						createdBy:$scope.userId,
						status:'A',
						patientId : $scope.patientAppoObjPayment.patientId,
						nursingNoteDesc: $scope.nursingNoteObj.nursingNoteDesc,
						template : $scope.nursingNoteObj.template,
						doctorId:$scope.patientAppoObjPayment.doctorId,
						admissionId: $scope.recordingPayload.encounterId,
				};
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/nursingNote/saveNursingNote";
		
				GenericServiceParamHeader
				.serviceAction(METHOD_POST, URI,data)
				.then(function(response) {
							if (response.data.status == "success")
									growl.success(response.data.message, {
										title : 'success!'
									});
							$scope.init();
							$scope.nursingNoteObj = {
										nursingNoteDesc:"",
										template:""
								};
								// $scope.getNursingNoteList();
								// $scope.setFirstTabLevel(1);
								$rootScope.stopLoader();
						});
		}	




			$scope.saveObservation = function() {
								$rootScope.startLoader();
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate : moment(new Date()).format(
											'DD-MM-YYYY HH:mm:ss'),
									updatedDate : moment(new Date()).format(
											'DD-MM-YYYY HH:mm:ss'),
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientId : $scope.patientAppoObjPayment.patientId,
									observationDesc : $scope.observationObj.observationDesc,
									doctorId : $scope.patientAppoObjPayment.doctorId,
									admissionId : $scope.recordingPayload.encounterId,
								};

								var paramObj = {};
								var headers = {
									'organization_id' : $scope.organizationId,
									'unit_id' : $scope.unitId,
									'Content-Type' : 'application/json'
								};
								// return false;
								var strObj = $scope.observationObj.observationDesc;
								if(strObj=="" || strObj==undefined || strObj==null){
									growl.warning("Please enter observation", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
									
								} else {
									var URI = BASE_URL + ROOT_URL
											+ "/api/opd/saveObservation";

									GenericServiceParamHeader
											.serviceAction(
													METHOD_POST,
													URI,
													data,
													NOTIFICATION_MSG_STATUS_TRUE,
													paramObj, headers)
											.then(
													function(response) {
														console.log(
																"savedData",
																response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															// $scope.init();
														// $scope.getObservationList();
														// $scope.setFirstTabLevel(1);
														// $scope.setFirstTabLevel(19);
														// $scope.observationObj.observationDesc = "";
														// $rootScope.stopLoader();
														// return
														// response.data.listObject;
														console.log("Done");
														
													});
								}

							}



							$scope.saveGeneralExamination = function(typeId) {
								
							
								console.log("array Gene",
										$scope.patientSystemFinalArray);
								var paramObj = {};
								var headers = {
									'organization_id' : $scope.organizationId,
									'unit_id' : $scope.unitId,
									'Content-Type' : 'application/json'
								};

								$scope.listOfListOfGeneralExamSystemMaster = [];
								if (typeId == 1) {
									angular
											.forEach(
													$scope.patientSystemFinalArray,
													function(value, index) {
														if (value.isCheckedSys == 'Y') {
															var parentObj = {};
															parentObj.systemId = value.systemId;
															parentObj.isNADValue = value.isNADValue;
															if (value.isNADValue == 'Y') {
																parentObj.listOfGeneralSystemObservation = [];
															} else {
																parentObj.listOfGeneralSystemObservation = [];
																angular
																		.forEach(
																				value.systemObervationList,
																				function(
																						value1,
																						index1) {
																					// var
																					if (value1.isCheckedOber == 'Y') {
																						var childObservationObj = {};
																						childObservationObj.systemObervationId = value1.observationId;
																						if (value1.isPropertyRequired == 'N') {
																							childObservationObj.remark = value1.remark;
																							childObservationObj.listOfGeneralSystemProperty = [];
																						} else {
																							childObservationObj.remark = '';
																							childObservationObj.listOfGeneralSystemProperty = [];
																							angular
																									.forEach(
																											value1.systemObservationPropertList,
																											function(
																													value2,
																													index2) {
																												var subChildObservationPropertyObj = {};
																												if (value2.isCheckedOberPro == true) {
																													subChildObservationPropertyObj.systemObervationPropertyId = value2.propertyId;
																													subChildObservationPropertyObj.remark = value2.remark;
																													childObservationObj.listOfGeneralSystemProperty
																															.push(subChildObservationPropertyObj);
																												}
																											});

																						}
																						parentObj.listOfGeneralSystemObservation
																								.push(childObservationObj);
																					}

																				});
															}
															$scope.listOfListOfGeneralExamSystemMaster
																	.push(parentObj);
														}
													});
									
									if($scope.validateexamGen == false ){
										growl.warning("Please Enter General Examination ", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
									}
								}
								if (typeId == 2) {
									angular
											.forEach(
													$scope.patientPhysicalSystemFinalArray,
													function(value, index) {
														if (value.isCheckedSys == 'Y') {
															var parentObj = {};
															parentObj.systemId = value.systemId;
															parentObj.isNADValue = value.isNADValue;
															if (value.isNADValue == 'Y') {
																parentObj.listOfGeneralSystemObservation = [];
															} else {
																parentObj.listOfGeneralSystemObservation = [];
																angular
																		.forEach(
																				value.systemObervationList,
																				function(
																						value1,
																						index1) {
																					// var
																					if (value1.isCheckedOber == 'Y') {
																						var childObservationObj = {};
																						childObservationObj.systemObervationId = value1.observationId;
																						if (value1.isPropertyRequired == 'N') {
																							childObservationObj.remark = value1.remark;
																							childObservationObj.listOfGeneralSystemProperty = [];
																						} else {
																							childObservationObj.remark = '';
																							childObservationObj.listOfGeneralSystemProperty = [];
																							angular
																									.forEach(
																											value1.systemObservationPropertList,
																											function(
																													value2,
																													index2) {
																												var subChildObservationPropertyObj = {};
																												if (value2.isCheckedOberPro == true) {
																													subChildObservationPropertyObj.systemObervationPropertyId = value2.propertyId;
																													subChildObservationPropertyObj.remark = value2.remark;
																													childObservationObj.listOfGeneralSystemProperty
																															.push(subChildObservationPropertyObj);
																												}
																											});

																						}
																						parentObj.listOfGeneralSystemObservation
																								.push(childObservationObj);
																					}

																				});
															}
															$scope.listOfListOfGeneralExamSystemMaster
																	.push(parentObj);
														}
													});
									
									
									if($scope.validateexamSys == false ){
										
										growl.warning("Please Enter Systemic Examination ", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
										return false;
										
									}
								}
								var data = {
									patientId : $scope.patientAppoObjPayment.patientId,
									admissionId : $scope.recordingPayload.encounterId,
									createdDate : moment(new Date()).format(
											'DD-MM-YYYY HH:mm:ss'),
									updatedDate : moment(new Date()).format(
											'DD-MM-YYYY HH:mm:ss'),
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									typeId : typeId,
									listOfListOfGeneralExamSystemMaster : $scope.listOfListOfGeneralExamSystemMaster
								}
								console.log(
										"Final General Examination Save Arr",
										data);
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
								 
								// return false;
									 
									
								var URI = BASE_URL + ROOT_URL
										+ SAVEGENERALEXAMINATIONAPPOMAPPER;
								// console.log("URI",URI);
								// return false;
								GenericServiceParamHeader.serviceAction(
										METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_TRUE, paramObj,
										headers).then(function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										// $scope.init();
									    // $scope.getActiveGeneralExamination();
									    // $scope.getActivephysicalExamination();
									    // $scope.getSystemicExaminationForCaseSheeet();
								        // $scope.getGenericExaminationForCaseSheeet();
										return response.data.listObject;
										$rootScope.stopLoader();
								});
									  
								 } 

							}

//Kiosk Save On popUp
$scope.mainDiv = true;
$scope.followupQuestions = `{
              "questions": [
                {"question":"When did the stomachache first begin?","id":1},
                {"question":"Can you describe the nature of the pain (e.g., sharp, dull, cramping)?","id":2},
                {"question":"How would you rate the pain on a scale from 1 to 10?","id":3},
                {"question":"Is the pain constant or does it come and go?","id":4},
                {"question":"Have you noticed any specific triggers or factors that worsen or relieve the pain?","id":5},
                {"question":"Are there any associated symptoms such as nausea, vomiting, or diarrhea?","id":6},
                {"question":"Have you experienced any recent changes in appetite or weight?","id":7},
                {"question":"Do you have a history of gastrointestinal issues or surgeries?","id":8},
                {"question":"Have you taken any medications or treatments for the stomachache?","id":9},
                {"question":"Have you experienced any recent stress or lifestyle changes?","id":10}
              ],
              "title": "Patient Symptom Questionnaire"
            }`;


$scope.replaceInObject = function(obj, search, replace) {
  for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
          if (typeof obj[key] === 'string') {
            obj[key] = obj[key].replace(search, replace);
          } else if (typeof obj[key] === 'object' && obj[key] !== null) {
            replaceInObject(obj[key], search, replace);
          }
        }
      }
}

 $scope.nursingNoteObj = {
         nursingNoteDesc:"",
         template:""
 };


$scope.getTabValue = function(val) {
             $scope.tabId = val;
             if($scope.tabId == 1) {
                 const label1 = document.querySelector('label[for="digitalRadio"]');
                 label1.innerHTML = `<b>${label1.innerHTML}</b>`;
                 const label2 = document.querySelector('label[for="chatRadio"]');
                 label2.innerHTML = label2.innerHTML.replace(/<b>(.*?)<\/b>/, '$1');
                 $scope.set1 = true;
                 $scope.set2 = false;
                 $scope.set3 = false;
                 $scope.set4 = false;
                 $("#digitalRadio").prop('checked', true);
                 $("#chatRadio").prop('checked', false);
                 $("#consentRadio").prop('checked', false);
                 $("#feedbackRadio").prop('checked', false);
             } else if($scope.tabId == 2) {
                 const label2 = document.querySelector('label[for="chatRadio"]');
                 label2.innerHTML = `<b>${label2.innerHTML}</b>`;
                 const label1 = document.querySelector('label[for="digitalRadio"]');
                 label1.innerHTML = label1.innerHTML.replace(/<b>(.*?)<\/b>/, '$1');
                 $scope.set1 = false;
                 $scope.set2 = true;
                 $scope.set3 = false;
                 $scope.set4 = false;
                 $("#digitalRadio").prop('checked', false);
                 $("#chatRadio").prop('checked', true);
                 $("#consentRadio").prop('checked', false);
                 $("#feedbackRadio").prop('checked', false);
             } else if($scope.tabId == 3) {
                 $scope.set1 = false;
                 $scope.set2 = false;
                 $scope.set3 = true;
                 $scope.set4 = false;
                 $("#digitalRadio").prop('checked', false);
                 $("#chatRadio").prop('checked', false);
                 $("#consentRadio").prop('checked', true);
                 $("#feedbackRadio").prop('checked', false);
             } else if($scope.tabId == 4) {
                 $scope.set1 = false;
                 $scope.set2 = false;
                 $scope.set3 = false;
                 $scope.set4 = true;
             }
           $("#samrtQuestionsModal").modal({
               backdrop: 'static',
               keyboard: false
           });
}


$scope.generateQuestionnaireForKiosk = function (inputText) {
    $scope.mainTitle = "Smart Questionnaire";
    if (!inputText) {
          growl.warning("Please Enter Complaint", {
                                title : 'Warning!'
          });
        return;
    }
    $rootScope.startLoader();
    var data1 = JSON.parse($scope.jsonGlobalForQuestion);
    var URI = data1.urlObject.URL;
    var model = data1.aiPayload;
    $scope.replaceInObject(model, /@Input_from_application/g, inputText);
    model.Input_from_application = inputText;
    model.Input_file_URL = "";
    model.Input_type = "audio";
    var data = JSON.stringify(model);
   $.ajax({
       url: URI,
       type: "POST",
       data: data,
       contentType: "application/json",
       dataType: "json",
       success: function (response) {
           $scope.$apply(function () {
               try {
                   if (response.status === "success") {
                       console.log("AI Response:", response);
                      $scope.followupQuestions = response.output;
                      $scope.questions = $scope.followupQuestions.questions;
                      $scope.title = $scope.followupQuestions.title;
                      $scope.getTabValue(2);
                      angular.element('#samrtQuestionsModal').modal('show');
                      $rootScope.stopLoader();
                   }
               } catch (e) {
                   console.error("Success block error:", e);
               } finally {
                   $rootScope.stopLoader();
               }
           });
       },
       error: function (xhr, status, error) {
           $scope.$apply(function () {
               try {
                   console.error("API Error:", error);
                   if ($scope.followupQuestions) {
                       var questionnaireObj = JSON.parse($scope.followupQuestions);
                       $scope.questions = questionnaireObj.questions;
                       $scope.title = questionnaireObj.title;
                       $scope.getTabValue(2);
                   }
               } catch (e) {
                   console.error("Error block parsing issue:", e);
               } finally {
                   $rootScope.stopLoader();
               }
           });
       }
   });
};

$scope.saveQuestionnaireForKiosk = function () {
        $('#samrtQuestionsModal').hide('hide');
        $rootScope.startLoader();
         var result = $scope.questions.map(function(q, i){
            return {
                id: q.id,
                question: q.question,
                answer: $scope.patientAnswers[i]
            };
         });
         var data1 = JSON.parse($scope.jsonGlobalForAiTemplate);
         var URI = data1.urlObject.URL;
         var model = data1.aiPayload;
         model.Input_from_application = result
             .map(r => `Question: ${r.question}\nAnswer: ${r.answer}`)
             .join("\n\n")
         model.Input_type = "text";
         model.Input_file_URL = "No";
         $.ajax({
             url: URI,
             type: "POST",
             data: JSON.stringify(model),
             contentType: "application/json",
             dataType: "json",
             success: function(response){
                 $scope.$apply(function () {
                     console.log("Success:", response);
                     $scope.nursingNoteObj.nursingNoteDesc = "AI Summary";
                     $scope.nursingNoteObj.template =response.output;
                     $scope.saveNursingNoteForAiSummary();
                 });
             },
             error: function(err){
               // console.error(err);
                $scope.nursingNoteObj.nursingNoteDesc = "AI Summary";
                $scope.nursingNoteObj.template = "Response From Ai";
                $scope.saveNursingNoteForAiSummary();
             }
         });
};


$scope.updateAssesmentStopTime = function() {
	var flagEncouter = $scope.patientAppoObjPayment.encounterId!=null && $scope.patientAppoObjPayment.encounterId!=undefined ? true : false;
       
	var data = {
		encounterId:flagEncouter?  $scope.patientAppoObjPayment.encounterId :$scope.recordingPayload.encounterId,	
		unitId: $scope.unitId
	};
	var URI = BASE_URL + ROOT_URL + "/api/opd/updateEncounterStopTime";
	// GenericServiceParamHeader
	// .serviceAction(METHOD_POST, URI, data)
	// .then(function(response) {	
	// 	console.log("Assesment Stop Time Updated:", response);
	// });

	try {
		GenericServiceParamHeader
			.serviceAction(METHOD_POST, URI, data)
			.then(function (response) {
			console.log("Assessment Stop Time Updated:", response);
			})
			.catch(function (error) {
				angular.element('.modal-backdrop').remove();
				angular.element('body').removeClass('modal-open');
				angular.element('body').css('padding-right', '');
				$state.go("kiosk", {}, { reload: true });

			});
		} catch (e) {
				console.error("JS Error:", e);
				angular.element('.modal-backdrop').remove();
				angular.element('body').removeClass('modal-open');
				angular.element('body').css('padding-right', '');
				$state.go("kiosk", {}, { reload: true });
		}
}

$scope.saveNursingNoteForAiSummary = function(){
     if($scope.nursingNoteObj.nursingNoteDesc=="" || $scope.nursingNoteObj.nursingNoteDesc==null ||
             $scope.nursingNoteObj.nursingNoteDesc==undefined){
         growl.warning("Please Enter Name", {
                title : 'Warning!'
            });
            $rootScope.stopLoader();
            return false;
     }
     if($scope.nursingNoteObj.template=="" || $scope.nursingNoteObj.template==null ||
             $scope.nursingNoteObj.template==undefined){
                 growl.warning("Please Enter Template Details", {
                        title : 'Warning!'
                    });
                    $rootScope.stopLoader();
                    return false;
            }
			console.log("@@@@@@@@@@@@@@@",$scope.patientAppoObjPayment.encounterId);
			var flagEncouter = $scope.patientAppoObjPayment.encounterId!=null && $scope.patientAppoObjPayment.encounterId!=undefined ? true : false;
        var data = {
                organizationId:$scope.organizationId,
                unitId:$scope.unitId,
                createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                updatedBy:$scope.userId,
                createdBy:$scope.userId,
                status:'A',
                patientId : $scope.patientAppoObjPaymentPayment.patientId,
                nursingNoteDesc: $scope.nursingNoteObj.nursingNoteDesc,
                template : $scope.nursingNoteObj.template,
                doctorId: $scope.patientAppoObjPaymentPayment.doctorId,
                encounterId:flagEncouter?  $scope.patientAppoObjPayment.encounterId :$scope.recordingPayload.encounterId,
        };
        var URI = BASE_URL + ROOT_URL + "/api/opd/nursingNote/saveNursingNote";
        GenericServiceParamHeader
        .serviceAction(METHOD_POST, URI,data)
        .then(function(response) {
                    if (response.data.status == "success")
                         growl.success(response.data.message, {
                                title : 'success!'
                            });
                       $rootScope.stopLoader();
					   $scope.updateAssesmentStopTime();
                       angular.element('.modal-backdrop').remove();
                       angular.element('body').removeClass('modal-open');
                       angular.element('body').css('padding-right', '');
                       $state.go("kiosk", {}, { reload: true });
                });
}
  $scope.currentQuestion = 0;
  $scope.patientAnswers = [];

$scope.nextQuestion = function () {
    if ($scope.currentQuestion < $scope.questions.length - 1) {
        $scope.currentQuestion++;
    }
    else if ($scope.currentQuestion == $scope.questions.length - 1) {
        $scope.set1 = false;
        $scope.set2 = false;
        $scope.set3 = true;
        $scope.set4 = false;
        $scope.audioUrls = [];
        angular.forEach($scope.patientAudioAnsLink, function (link) {
            if (link && link !== '') {
                $scope.audioUrls.push($sce.trustAsResourceUrl(link));
            } else {
                $scope.audioUrls.push('');
            }
        });
    }
};

  $scope.previousQuestion = function () {
      if ($scope.currentQuestion > 0) {
          $scope.currentQuestion--;
      }
  };

  $scope.resetAnswersData = function () {
      $scope.patientAnswers = [];
      $scope.currentQuestion = 0;
      $scope.set1 = true;
      $scope.set2 = false;
      $scope.set3 = false;
  };
                $scope.recStart = false;
                $scope.recognition = null;
                $scope.startSpeechRecognitionKiosk = function () {
                    if ($scope.recStart) {
                        $scope.recStart = false;
                        if ($scope.recognition) {
                            $scope.recognition.stop();
                            $scope.recognition = null;
                        }
                        return;
                    }
                    if (!('webkitSpeechRecognition' in window)) {
                        $.notify("Speech recognition not supported", "error");
                        return;
                    }
                    $scope.recStart = true;
                    var recognition = new webkitSpeechRecognition();
                    $scope.recognition = recognition;
                    recognition.continuous = true;
                    recognition.interimResults = false;
                    recognition.lang = "en-US";
                    recognition.onstart = function () {
                        console.log("Recording started");
                    };
                    recognition.onresult = function (event) {
                        var transcript = event.results[event.results.length - 1][0].transcript;
                        $scope.$apply(function () {
                            var current = $scope.patientAnswers[$scope.currentQuestion] || '';
                            $scope.patientAnswers[$scope.currentQuestion] =
                                (current + " " + transcript).trim();
                        });
                    };
                    recognition.onerror = function (event) {
                        console.error(event);
                        if (event.error === 'not-allowed') {
                            $.notify("Microphone permission denied", "error");
                        }
                        $scope.$apply(function () {
                            $scope.recStart = false;
                        });
                    };
                    recognition.onend = function () {
                        console.log("🎤 Recording stopped");
                        $scope.$apply(function () {
                            $scope.recStart = false;
                        });
                    };
                    recognition.start();
                };

                 $scope.inputFromPatient = "";
                 $scope.patientRecognition = null;
                 $scope.recStartPatient = false;
                 $scope.startPatientCompliantSpeechRecognition = function () {

                     if ($scope.recStartPatient) return;

                     if (!('webkitSpeechRecognition' in window)) {
                         $.notify("Speech recognition not supported", "error");
                         return;
                     }

                     $scope.recStartPatient = true;

                     var recognition = new webkitSpeechRecognition();
                     $scope.patientRecognition = recognition;

                     recognition.continuous = true;
                     recognition.lang = "en-US";

                     recognition.onresult = function (event) {

                         var transcript = event.results[event.results.length - 1][0].transcript;

                         $scope.$apply(function () {
                             $scope.inputFromPatient =
                                 ($scope.inputFromPatient || '') + " " + transcript;
                         });
                     };

                     recognition.onend = function () {
                         $scope.$apply(function () {
                             $scope.recStartPatient = false;
                         });
                     };

                     recognition.start();
                 };

                 $scope.stopPatientSpeech = function () {
                     if ($scope.patientRecognition) {
                         $scope.patientRecognition.stop();
                         $scope.patientRecognition = null;
                     }
                     $scope.recStartPatient = false;
                 };

                      $scope.recordingState = {};
                      var recognition = null;
                      var activeIndex = null;
                      var isFirstRecognitionChunk = true;
                      $scope.initSpeechRecognition = function () {
                          var SpeechRecognition =
                              window.SpeechRecognition || window.webkitSpeechRecognition;
                          if (!SpeechRecognition) {
                              alert("Speech Recognition not supported");
                              return;
                          }
                          recognition = new SpeechRecognition();
                          recognition.lang = 'en-IN';
                          recognition.continuous = true;
                          recognition.interimResults = false;
                          recognition.onresult = function (event) {
                              var transcript = '';

                              for (var i = event.resultIndex; i < event.results.length; i++) {
                                  if (event.results[i].isFinal) {
                                      transcript += event.results[i][0].transcript;
                                  }
                              }

                              if (!transcript) return;

                              if (isFirstRecognitionChunk) {
                                  transcript =
                                      transcript.charAt(0).toUpperCase() + transcript.slice(1);
                                  isFirstRecognitionChunk = false;
                              }

                              $scope.$applyAsync(function () {
                                  if (angular.isNumber(activeIndex)) {

                                      if (!$scope.dataListComplaints[activeIndex].remark) {
                                          $scope.dataListComplaints[activeIndex].remark = transcript;
                                      } else {
                                          $scope.dataListComplaints[activeIndex].remark += ' ' + transcript;
                                      }

                                  }
                                  else {

                                      if (!$scope.complaintsUpdateObj.remark) {
                                          $scope.complaintsUpdateObj.remark = transcript;
                                      } else {
                                          $scope.complaintsUpdateObj.remark += ' ' + transcript;
                                      }

                                  }
                              });
                          };

                          recognition.onend = function () {
                              if (
                                  angular.isNumber(activeIndex)
                                      ? $scope.recordingState[activeIndex]
                                      : $scope.recordingState['single']
                              ) {
                                  recognition.start();
                              }
                          };
                      };

                      $scope.initSpeechRecognition();





					  $scope.getInitialAssesment = function() {
						$scope.patientAppoObj.paymentCompleted=true;

						  if($scope.patientAppoObj.initialAssesmentStarted !=undefined &&$scope.patientAppoObj.initialAssesmentStarted==false){
                               var payload = {
								"encounterId":$scope.patientAppoObj.encounterId,
								"isCalled": "Y",
								"orderDetailId":$scope.patientAppoObj.orderDetailsId,
								"patientId":$scope.patientAppoObj.patientId,
								"assignmentStartTime": $scope.patientAppoObj.assignmentStartTime,
								"isAssignmentCompStatus": 'N',
								"assignmentStartBy": $scope.patientAppoObj.assignmentStartBy,
								"assignmentStartRoleId" : 3,
								"isConsultationStatus":'N'
							   }
                                $scope.patientCalled(payload);
                                $scope.encounterDetails(payload);

                        }



							$state.go("paymentInformation", {
							     patientAppoObjPayment: $scope.patientAppoObj,
														});
						}




						$scope.startInitialAssementWithoutPayment = function () {
		$scope.patientAppoObjPayment.encounterId;
    var URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay";

    var data = {
        patientId: $scope.patientAppoObjPayment.patientId,
        organizationId: $scope.organizationId,
        unitId: $scope.unitId,
        doctorId: $scope.patientAppoObjPayment.doctorId,
        deptId: $scope.patientAppoObjPayment.specialityId,
        specialityId: $scope.patientAppoObjPayment.specialityId,
        paymentCompleted: true   // ✅ FIXED
    };

    $rootScope.startLoader();

    BillingGenericService
        .serviceAction(
            METHOD_POST,
            URI,
            data,
            NOTIFICATION_MSG_STATUS_FALSE
        )
        .then(function (response) {

            console.log(response);

            if (response.data.status === "success") {

                if (response.data.listObject != null) {

                    if (response.data.listObject && response.data.listObject.length) {
                        response.data.listObject.forEach(function (obj) {
                            if (obj.tabId === 44) {
                                $scope.jsonGlobalForQuestion = obj.jsonConfiguration;
                            }
                            if (obj.tabId === 45) {
                                $scope.jsonGlobalForAiTemplate = obj.jsonConfiguration;
                            }
                        });
                    }
                }

                if ($scope.patientObj == null) {
                    $scope.openRecordingPopup(response.data.resObject);
                }
            }

            $rootScope.stopLoader();
        });
};

					$scope.startInitialAssesment = function() {		
						if($scope.patientAppoObjPayment!=undefined && $scope.patientAppoObjPayment!=null){
                            if($scope.patientAppoObjPayment.paymentCompleted==true){ 
									$scope.startInitialAssementWithoutPayment();				
							}
						}
							
					}

                     $scope.startInitialAssesment();

                } ]);


