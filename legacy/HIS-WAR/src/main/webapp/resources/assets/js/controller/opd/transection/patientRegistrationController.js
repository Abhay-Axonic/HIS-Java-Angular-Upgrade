'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:patientRegistrationController
 * @description #patientRegistrationController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'patientRegistrationController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'$timeout',
						'GenericService',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, $timeout,GenericService,
								BillingGenericService,GenericServiceFileUpload, growl, $state,
								$stateParams,$modalInstance) {

							/* init() function for form object create. */
                             var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
                             $scope.showAdmissionScreenFlag = true;
							$scope.selectItemData = function()
							{
								let item={};
								$scope.screenType=$stateParams.screenType
								if($scope.screenType==undefined || $scope.screenType=='' || $scope.screenType==null){
									$scope.screenType='Main-Registration';
								}

								else if($scope.screenType=='ER-Registration'){
									$scope.patientObj = angular.copy($stateParams.patientObj);
									item=angular.copy($stateParams.patientObj);
								}

								else if($scope.screenType=='Admission-Request'){
								     $scope.showAdmissionScreenFlag = false;
									// alert('Admission-Request');
								}
								else if($scope.screenType=='PatientVisit-Screen'){

								}

								// setData code start

								if(item!=undefined || item!=null || item!=""){
									$scope.patientObj.firstName=(item.firstName)
									$scope.patientObj.middleName=(item.middleName)
									$scope.patientObj.lastName=(item.lastName)
									$scope.patientObj.mobileNumber=(item.mobileNumber);
									$scope.patientObj.genderId=(item.genderId);
									$scope.patientObj.address=(item.address);
								}

								// setData code end
							}




							$scope.init = function() {
								$scope.selectFlag = true;
								$scope.storeEmailId = null;
								$scope.filepreview = "";
								$scope.isConversion=false;
								$scope.isOTCRegistrationHideShow=false;
	                            $scope.addressFlag =false;
	                            $scope.updateRegflag=true;

	                            //for infinite scroll
	                            $scope.numberToDisplay = 15;
								//for star camera
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
let stream;
let video = document.getElementById("cameraVideoForDoc");

$scope.showVideo = false;    // Initially hidden
$scope.showImage = false;    // Initially hidden
$scope.imageframe = false;
$scope.capturedImage = "";

// TOGGLE CAMERA / CAPTURE
$scope.toggleCamera = function () {
	$scope.imageframe = true;
  if (!$scope.showVideo && !$scope.showImage) {
    // OPEN CAMERA
    navigator.mediaDevices.getUserMedia({ video: true })
      .then(function (s) {
        stream = s;
        video.srcObject = stream;
        $scope.showVideo = true;
        $scope.showImage = false;
        $scope.$apply();
      })
      .catch(function (err) {
        console.error("Camera error", err);
      });
  } else if ($scope.showVideo) {
    // CAPTURE IMAGE
    let canvas = document.createElement("canvas");
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    let ctx = canvas.getContext("2d");
    ctx.drawImage(video, 0, 0);

    $scope.capturedImage = canvas.toDataURL("image/png");


    // STOP CAMERA
    stream.getTracks().forEach(track => track.stop());

    $scope.showVideo = false;
    $scope.showImage = true;
    $scope.$apply();
  }
};

// SEND IMAGE TO API (AJAX)
$scope.sendImage = function () {
  if (!$scope.capturedImage) return;
 $scope.capturedImage =  $scope.capturedImage.replace(/^data:image\/png;base64,/, "")
 angular.element('#Registration_With_Id').modal('hide');
 $scope.startLoader();
  $.ajax({
    url: "https://aidata.axonichealth.com/kycocr",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify({
      file: $scope.capturedImage,
      documentType: "",
    }),
    success: function (res) {
      $scope.$apply(function () {
        $scope.imageframe = false;
        $scope.showImage = false;
        angular.element('#Registration_With_Id').modal('hide');
		//remove hardcoded data after getting  valid response
         $scope.stopLoader();
		$scope.patientObj.address = "";
/*         $scope.patientObj.firstName = "";
        $scope.patientObj.middleName = "";
        $scope.patientObj.lastName = ""; */

        let fullName = res.extractedData.fullName;

        let parts = fullName.trim().split(/\s+/);

        $scope.patientObj.firstName = parts[0] || "";
        $scope.patientObj.lastName  = parts.length > 1 ? parts[parts.length - 1] : "";
        $scope.patientObj.middleName =
          parts.length > 2 ? parts.slice(1, parts.length - 1).join(" ") : "";

        $scope.patientObj.dob = res.extractedData.dateOfBirth;
        $scope.patientObj.mobileNumber = res.extractedData.phoneNumbers;
		  $scope.datepicker = moment(
   res.extractedData.dateOfBirth,
   "DD/MM/YYYY"
 ).toDate();

$scope.$watch('datepicker', function (newVal) {
  if (newVal) {
    $scope.calculateAge();
  }
});


        $scope.patientObj.email =(res.extractedData.emailIds && res.extractedData.emailIds > 0) ? res.extractedData.emailIds : null;
        if (res.extractedData.gender) {
          const gender = res.extractedData.gender.trim().toLowerCase();
          if (gender === "male") {
            $scope.patientObj.genderId = 1;
//            $scope.patientObj.prefixId=239;
          } else if (gender === "female") {
            $scope.patientObj.genderId = 2;
//            $scope.patientObj.prefixId=244;
          } else {
            $scope.patientObj.genderId = 3;
          }
        }

      });
      console.log("Image uploaded", res);
    },
    error: function (err) {
      $scope.$apply(function () {
        $scope.imageframe = false;
        $scope.showImage = false;

        $scope.patientObj.address = "";
        $scope.patientObj.firstName = "";
        $scope.patientObj.middleName = "";
        $scope.patientObj.lastName = "";
        $scope.patientObj.dob = "";
        $scope.patientObj.mobileNumber = "";
        $scope.patientObj.email = "";
        $scope.patientObj.genderId = "";
      });
      console.error("Upload failed", err);
    }
  });
};



$scope.calculateAge = function () {
  if (!$scope.datepicker) return;

  const dob = moment($scope.datepicker);
  const today = moment();

  const years = today.diff(dob, 'years');
  dob.add(years, 'years');

  const months = today.diff(dob, 'months');
  dob.add(months, 'months');

  const days = today.diff(dob, 'days');

  $scope.ageYear = years;
  $scope.ageMonth = months;
  $scope.ageDay = days;

  // Optional: bind to patient object
  $scope.patientObj.ageYear = years;
  $scope.patientObj.ageMonth = months;
  $scope.patientObj.ageDay = days;
};


			    
							    /**
							     * Redirect the browser to the URL given.
							     * Used to download the image by passing a dataURL string
							     */
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
								
								//for datepicker 1
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate:new Date()
								};

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};


								
								//Patient Search DOB Start
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearchS.opened = true;
									if($scope.newobj.datepickerSearch != undefined && $scope.newobj.datepickerSearch != null && $scope.newobj.datepickerSearch !='')
									{$scope.datepickerSearch = $scope.newobj.datepickerSearch};
									
								};
								
								$scope.datepickerSearchS = {
										opened : false
									};
								//Patient Search DOB End
								
								$scope.datepicker = {
									opened : false
								};
								
								$scope.focusdata1 = function(event,item){
						            var idfocus_name = 'focus_name';
						            document.getElementById(idfocus_name).focus();
						         
						    }
								$scope.focusdata8 = function(event,item){
						            var idfocus_name1 = 'focus_name1';
						            document.getElementById(idfocus_name1).focus();
						         
						    }
								$scope.focusdata3 = function(event,item){
									
									if($scope.ageDay.length == 2){
										  var idphone_no = 'phone_no';
									        document.getElementById(idphone_no).focus();
									}
								  
								}
								$scope.focusdataP = function(event,item){
									
									if($scope.ageDay.length == 2){
										  var idphone_no1 = 'phone_no1';
									        document.getElementById(idphone_no1).focus();
									}
								  
								}
//							    $scope.focusdata3 = function(event,item){
//							         var idphone_no = 'phone_no';
//							            document.getElementById(idphone_no).focus(); 
//							     }
							    $scope.focusdata9 = function(event,item){
							         var idphone_no1 = 'phone_no1';
							            document.getElementById(idphone_no1).focus(); 
							     }
							    $scope.focusdata2 = function(event,item){
							        if($scope.patientObj.mobileNumber.length == 10){
							              var idcode_focus = 'code_focus';
							                document.getElementById(idcode_focus).focus();
							        }
							    }
								
							    $scope.focusdata10 = function(event,item){
							        if($scope.patientObj.mobileNumber.length == 10){
							              var idcode_focus1 = 'code_focus1';
							                document.getElementById(idcode_focus1).focus();
							        }
							    }
							    $scope.focusdata11 = function(event,item){
							        if($scope.patientObj.mobileNumber.length == 10){
							              var idcode_focus2 = 'code_focus2';
							                document.getElementById(idcode_focus2).focus();
							        }
							    }
								// for datePicker1
								$scope.dateOptionsExpiry1 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
								$scope.openDatePickerExpiry1 = function() {
									$scope.datepickerExpiry1.opened = true;
								};

								$scope.datepickerExpiry1 = {
									opened : false
								};
								$scope.dateOptionsKinDetails = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
									$scope.openDatePickerExpiry = function(index) {
										// alert("in");
										// $scope.expiry.opened = true;
										for (var i = $scope.kinDetailsList.length - 1; i >= 0; i--) {
											if (i === index) {

												$scope.expiry.opened[i] = true;
											} else {
												$scope.expiry.opened[i] = false;
											}
										}
									};

									$scope.expiry = {
										opened : [ false ],

									};
								
								// for datePicker2
								$scope.dateOptionsExpiry2 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
								$scope.openDatePickerExpiry2 = function() {
									$scope.datepickerExpiry2.opened = true;
								};

								$scope.datepickerExpiry2 = {
									opened : false
								};
								
								// for datePicker3
								$scope.dateOptionsExpiry3 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
								$scope.openDatePickerExpiry3 = function() {
									$scope.datepickerExpiry3.opened = true;
								};

								$scope.datepickerExpiry3 = {
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
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
								$scope.unitZipCode = cookieObject.unitZipCode;
								$scope.isUpdatePatientReg = cookieObject.isUpdatePatientReg;
								
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								$scope.minAge = cookieObjectAppConfig.minAge;
								$scope.maxAge = cookieObjectAppConfig.maxAge;
								$scope.defaultPatientCategoryId=cookieObjectAppConfig.defaultPatientCategoryId;
								$scope.isKiosk = cookieObjectAppConfig.isKiosk;
								$scope.dateFormat = cookieObjectAppConfig.dateFormat;
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								  $('#mobNumber').focus();
								$scope.aliseFlag = false;
								 $scope.emailValidationRequired = cookieObjectAppConfig.emailValidationRequired;
								 $scope.mobileValidationRequired = cookieObjectAppConfig.mobileValidationRequired;
								$scope.expiryDateDisabled = true;
								/*if($scope.emailValidationRequired == 'Y'){
									$scope.otpvalidated = true;
								}else{
									$scope.otpvalidated = false;
								}*/
		                            $scope.emailOtp = null;
	                                $scope.entredOpt = "";
								$rootScope.stopLoader();
								
								$scope.searchType = 1;
								//alert($scope.searchType);
								
								$scope.searchPatientList = [];
								$scope.searchEmployeeList = [];
								$scope.searchDependentList =[];
								
								$scope.isRedirect = 'N';
								
								$scope.isPendingRequest = "N";
								
								$scope.birthDateSearchFlag = true;
								$scope.preReg=true;
								
								$scope.searchSelectPatient = {}
								$scope.itemSelecteds = {};
								
								$scope.datepickerExpiry = new Date(moment(new Date()).format('MM/DD/YYYY'));
								//alert($scope.datepickerExpiry );
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								 $scope.forthTabLevel=1;
								 $scope.sameAsLocal = 'Y';
								
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
									nationalityId : '',
									raceId : '',
									maritalStatusId : '',
									mobileNumber : '',
									dob : '',
									identificationTypeId : '',
									identificationNumber : '',
									isVip : '',
									address : '',
									countryId : '',
									stateId : '',
									districtId : '',
									cityId :'',
									areaId : '',
									zipCode : '',
									phoneCode : '',
									phoneNumber : '',
									companyName : '',
									companyAddress : '',
									companyCountryId : '',
									companyStateId : '',
									companyDistrictId : '',
									companyCityId : '',
									companyAreaId : '',
									companyZipCode : '',
									companyMobileNumber : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
									permanentAddress:'',
									permanentCountryId:'',
									permanentStateId:'',
									permanentDistrictId:'',
									permanentCityId:'',
									permanentAreaId:'',
									permanentZipCode:'',
									permanentPhoneCode:'',
									permanentPhoneNumber:'',
									aliseName:'',
									identificationExpiryDate:'',
									occupationId:'',
									registrationTypeId:1,
									previousId:'',
									patientId:0,
									patientDetailsId:'',
									uhidNumber:'',
									healthNumber:'',
									healthId:'',									
									imagePath:'',
									thumbImage:'',
									whatsappCallingCode : '',
									whatsappNo:'',
									skypeId:'',
									area:'',
									permanentArea:'',
									kinDetailsId:'',
									fileSequenceNumber:'',
									bloodGroup :'',
									isAlreadyExist:'N'
								};

                                $scope.erPatientObj = {
                                    priorityId : null,
                                    modeOfArrival : ''
                                };

								$scope.areaNotExist = 'N';
								$scope.areaNotExistPermanent = 'N';
								/*$scope.patientSearchObj = {
										"uhidNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNumber"        : "",
										"dob"       : "",
										"identificationNumber":"",
										"identificationTypeId":"",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										
								}
								$scope.dependentSearchObj = {
										"empCode"      : "",
										"employeeName"     : "",
										"dependentName" : "",
										"identificationNumber":"",
										"identificationTypeId":0,
										"unitId" :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"typeId" : "1"
										
								}
								$scope.employeeSearchObj = {
										"empCode"      : "",
										"employeeName"     : "",
										"specialityId" : "0",
										"identificationNumber":"",
										"identificationTypeId":"0",
										"unitId" :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"typeId" : "1"
								}*/
								
								$scope.initKinDetails = function(){
									  /* Add Next Of Kin Details */
								    $scope.kinDetailsList = [];
								    $scope.kinDetails = {
											prefixId   		: '',
											kinName  		: '',
											relationId 		: '',
											mobileNo 		: '',
											phoneNo 		: '',
											expiry 			: '',
											identificationId: '',
											identificationNo: '',
											isGuarantor 	: 'N',
											address 		: '',
											countryId 		: '',
											stateId 		: '',
											districtId 		: '',
											cityId 			: '',
											areaId 			: '',
											postCode 		: '',
											patientId       : '',
											unitId			: $scope.unitId,
											organizationId  : $scope.organizationId,
											createdBy		: $scope.userId,
											updatedBy		: $scope.userId,
											createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											isExpiry		:'N'
										};
									
									$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
								}
								$scope.initKinDetails();
								
							     $scope.addNewKinDetails = function(){
								    	$scope.kinDetails.patientId = $scope.patientDetails.patientId;
								    	$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
								    };
								    $scope.removeKinDetails = function(indexToBeDeleted){	
										  $scope.kinDetailsList.splice(indexToBeDeleted, 1);
								    };
								    
									$scope.setKinId = function(kinId){
										$scope.patientObj.kinDetailsId = kinId;
									}
								
								//kin details
								$scope.getKinDetailsByPatientId = function(){
									var URI = BASE_URL + ROOT_URL + KIN_DETAILS + PATIENT + SLASH + $scope.patientObj.patientId  + UNIT + SLASH +  $scope.unitId  + ORG + SLASH + $scope.organizationId;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success") {
											$scope.kinsList = response.data.listObject;
										}
									});
								}
								
								
								// kin details update and remove start
								$scope.getKinDetailsById = function(kinDetailsId){
									if($scope.patientObj.kinDetailsId != ""){
									$rootScope.startLoader();
									$scope.patientObj.kinDetailsId = kinDetailsId;
									var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success") {
													$scope.kinDetailsList[0]=(angular.copy(response.data.object));
													$scope.kinDetailsList[0].prefixId = ($scope.kinDetailsList[0].prefixId).toString();
													$scope.getStateByCountry($scope.kinDetailsList[0].countryId);
													$scope.getDistrictByState($scope.kinDetailsList[0].stateId);
													$scope.getCityByDistrict($scope.kinDetailsList[0].districtId);
													$scope.getAreaByCity($scope.kinDetailsList[0].cityId);
													$scope.kinDetailsList[0].identificationId = ($scope.kinDetailsList[0].identificationId).toString();
													console.log($scope.kinDetailsList);
													$scope.nextOfKinForm.$pristine=false;
												}
											});
									}else{
										growl.warning(" Please select details. ",
												{
													title : 'warning!'
												});
									}
								}
								$scope.updateKinDetailsStatus = function(kinDetailsId){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId + STATUS + SLASH + 'I';
									BillingGenericService.serviceAction(METHOD_PUT, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success") {
											$scope.patientObj.kinDetailsId="";
											$scope.getKinDetailsByPatientId();
										}
									});
								}
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var commonObj1 = {
									organizationId : $scope.organizationId
								};

								var data = {};
								
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
								
						
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVERELATIONLIST;
				GenericService
						.serviceAction("GET", URI, data)
						.then(
								function(response) {
									$scope.relationList = [];

									if (response.data.status == "success") {
										$scope.relationList = response.data.listObject;

									}
								});
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORY;
						GenericService
								.serviceAction("GET", URI, data)
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
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.prefixList = [];
													if (response.data.status == "success")
														angular.forEach(response.data.listObject, function(prefix) {
															if(prefix.isShowPatientRegistration == true){
																$scope.prefixList.push(prefix);
															}
														});
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
								+ GETACTIVEOCCUPATIONLIST;
						BillingGenericService
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.occupationList = [];
											if (response.data.status == "success")
												$scope.occupationList = response.data.listObject;
										});
						
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVENATIONALITYLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.nationalityList = [];
													if (response.data.status == "success")
														$scope.nationalityList = response.data.listObject;
													console.log("$scope.nationalityList",$scope.nationalityList);
												});

								var URI = BASE_URL + ROOT_URL + GETRACELIST;

								BillingGenericService
										.serviceAction(METHOD_POST, URI, commonObj1,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.raceList = [];
													if (response.data.status == "success")
														$scope.raceList = response.data.listObject;
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
								//getting default race start
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getDefaultRaceList/"+$scope.organizationId;
						BillingGenericService.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.defaultRaceList = [];
											if (response.data.status == "success" && response.data.listObject.length > 0)
											{
												$scope.patientObj.raceId = response.data.listObject[0].raceId;
												console.log("defaultRaceList=",response.data.listObject[0]);
											}
												
										});
						//getting default race end
						
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
															email:"N",
															relationId:"N",
															kinprefix:"N",
															kinName:"N",
															kinMobileNo:"N",
															kinAddress:"N",
															phoneNo:"N",
														    identificationId:"N",
														    identificationNo:"N",
														    countryId:"N",
														    stateId:"N",
														    districtId:"N",
														    cityId:"N",
														    areaId:"N",
														    postCode:"N"
													}
												}												
		
											/*if($scope.registrationFieldObj.email == "Y"){
												if($scope.emailValidationRequired == 'Y'){
													$scope.otpvalidated = true;
												}else{
													$scope.otpvalidated = false;
												}
											}else{
												$scope.otpvalidated = false;
											}*/
										} else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
										}
									});

									var URI = BASE_URL + ROOT_URL + GETACTIVEPRIORITYLIST;
                                    GenericService
                                        .serviceAction("GET", URI, data)
                                        .then(
                                            function(response) {
                                                $scope.priorityListForMLC = [];

                                                if (response.data.status == "success") {
                                                    $scope.priorityListForMLC = response.data.listObject;

                                                }
                                        });
								$scope.selectItemData();
						//getting default field configuration start
							}

							$scope.init();
							
							$scope.initSearchObj = function()
							{
								
								$scope.openDatePicker = function() {
							    $scope.datepicker=new Date();
								$scope.datepicker.opened = true;
								};
								$scope.datepicker = {
										opened : false
									};
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate:new Date()
									};
								$scope.patientSearchObj = {
										"uhidNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNumber"        : "",
										"dob"       : "",
										"identificationNumber":"",
										"identificationTypeId":"",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										
								}
								$scope.dependentSearchObj = {
										"empCode"      : "",
										"employeeName"     : "",
										"dependentName" : "",
										"identificationNumber":"",
										"identificationTypeId":0,
										"unitId" :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"typeId" : "1"
										
								}
								$scope.employeeSearchObj = {
										"empCode"      : "",
										"employeeName"     : "",
										"specialityId" : "0",
										"identificationNumber":"",
										"identificationTypeId":"0",
										"unitId" :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"typeId" : "1"
								}
							}
							$scope.initSearchObj();
								 
								$scope.checkFocus = function(){
                                      document.getElementById("genderId").focus();
							}
							
							$scope.checkDateValid= function()
							{
								if($scope.datepickerSearch == undefined || $scope.datepickerSearch == null || $scope.datepickerSearch == ''){
									   growl.warning("Date is invalid",{
											title : "warning"
										});
										$scope.datepickerSearch = new Date();

									   return false;
								}
								else{
								if($scope.datepickerSearch > new Date())
									{
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepickerSearch = new Date();
												
												   return false;
									}
							}
							
							}
							
							
							
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
							        to.d   += daysInMonths[parseInt(to.m+1)];
							        from.m += 1;
							    }
							    if (to.m < from.m) {
							        to.m   += 12;
							        from.y += 1;
							    }


                                if(to.d == from.d && to.m == from.m && to.y== from.y && $scope.datepicker!=undefined 
                                        && $scope.datepicker!=null && $scope.datepicker.opened == undefined){
                                     return {
                                            //days:   1,
                                            days:   0,
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
								$scope.flagTrue =0;
								$scope.calculateAge= function(flag)
								{


									if($scope.datepicker == undefined || $scope.datepicker == null || $scope.datepicker == ''){
										 $scope.age = "";
										 $scope.datepicker="";

										
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
										                   // $("#r1").val(dife.years+" yrs, "+dife.months+" m, & "+dife.days+" d");
										                    
													      if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 1){
													    	  $scope.ageYear = dife.years;
													    	      if(flag==true){
                                                                        $scope.ageMonth = 0;
                                                                        $scope.ageDay = 0;
                                                                    }
                                                                    else {
                                                                        $scope.ageMonth = dife.months;
                                                                        $scope.ageDay = dife.days;
                                                                    }

										                    	 if($scope.ageYear!=undefined && $scope.ageYear!=null && $scope.ageYear!="" && $scope.ageYear!=0
										                    			 && $scope.ageMonth!=undefined && $scope.ageMonth!=null && $scope.ageMonth!="" && $scope.ageMonth!=0
										                    			 && $scope.ageDay!=undefined && $scope.ageDay!=null && $scope.ageDay!="" && $scope.ageDay!=0){
										                    		 $scope.calDOBFlag = 1;
										                    	 }
													         }
													         else if ($scope.calDOBFlag == 2 && flag==true){
                                                                 $scope.ageYear = dife.years;
                                                                 $scope.ageMonth = 0;
                                                                   $scope.ageDay = 0;
                                                              }
                                                                  else if($scope.calDOBFlag == 2){
                                                                 $scope.ageYear = dife.years;
                                                                 $scope.ageMonth = dife.months;
                                                                   $scope.ageDay = dife.days;
                                                                  }
													     

										}
								}
								
								}

								
								$scope.myFunctDay = function(day) {
								   if( $scope.ageYear==undefined || $scope.ageYear==null ||  $scope.ageYear==0){
                                             $scope.ageYear="";
                                             $scope.datepicker="";
                                             $scope.ageDay = 0;
                                             $scope.ageMonth = "";
                                                 growl.warning("It is advisable to enter actual DOB",{
                                                               title : "warning"
                                                               });
                                                               return false;
                                                }
									if(day>31){
										$scope.ageDay = 0;
										growl.warning("Day should be less then 31 ",{
											title : "warning"
											});
									}else{
										var cntOfYears = 0;
										var cntOfMonths = 0;
										var cntOfDays = 0;
										
										if($scope.ageDay == undefined || $scope.ageDay == null ||  $scope.ageDay == "" ){
											$scope.ageDay = 0;
										}else{
											cntOfDays = $scope.ageDay;
										}
										

										if($scope.ageMonth == undefined || $scope.ageMonth == null || $scope.ageMonth == "" ){
											$scope.ageMonth = 0;
										}else{
											cntOfMonths = $scope.ageMonth;
										}
										
										if($scope.ageYear == undefined || $scope.ageYear == null ||  $scope.ageYear == ""){
											$scope.ageYear = 0;
										}else{
											cntOfYears = $scope.ageYear;
										}

										
									}
									  
								}
								$scope.myFunctMonth = function(month) {
								      if( $scope.ageYear==undefined ||  $scope.ageYear==null || $scope.ageYear==0  ){
                                                   $scope.ageYear="";
                                                   $scope.datepicker="";
                                                   $scope.ageDay = "";
                                                   $scope.ageMonth = 0;
                                                       growl.warning("It is advisable to enter actual DOB",{
                                                                     title : "warning"
                                                                     });
                                                                     return false;
                                                      }
									if(month>12){
										$scope.ageMonth = 0;
										growl.warning("Month should be less then 12 ",{
											title : "warning"
											});
									}else{
										var cntOfYears = 0;
										var cntOfMonths = 0;
										var cntOfDays = 0;
										
										if($scope.ageDay == undefined || $scope.ageDay == null || $scope.ageDay == "" ){
											$scope.ageDay = 0;
										}else{
											cntOfDays = $scope.ageDay;
										}
										

										if( $scope.ageMonth == undefined || $scope.ageMonth == null ||  $scope.ageMonth == "" ){
											$scope.ageMonth = 0;
										}else{
											cntOfMonths = $scope.ageMonth;
										}
										
										if( $scope.ageYear == undefined || $scope.ageYear == null || $scope.ageYear == "" ){
											$scope.ageYear = 0;
										}else{
											cntOfYears = $scope.ageYear;
										}
										

										if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
											$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
											if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
												&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
												$scope.calDOBFlag = 2;
											}
										}
										
									}
									  
								}
								$scope.myFunctYear = function(age,flag) {

                                  if(age==undefined || age ==""){
                                            $scope.ageYear="";
                                            $scope.ageMonth="";
                                            $scope.datepicker = "";
                                            //return false;
                                        }


									if(age>=$scope.minAge && age <= $scope.maxAge){
										 if($('#datepicker').val()!=""){

                                                     var dt = moment($scope.datepicker).format('MM/DD/YYYY');
                                                        var newAgeE = $rootScope.getAge(dt);

                                                        if(newAgeE != age)
                                                            {
                                                            var currentYear = (new Date()).getFullYear();
                                                            var finalYear = currentYear - age;
                                                            $scope.datepicker = new Date("01/01/"+finalYear);
                                                            }

                                                    }else{
                                                        var currentYear = (new Date()).getFullYear();
                                                        var finalYear = currentYear - age;
                                                        $scope.datepicker = new Date("01/01/"+finalYear);
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
											
										 if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
												//$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
												if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
													&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
													$scope.calDOBFlag = 2;
												}
											}
									}

								}
								$scope.birthAge = function (age){
								if(age<10 || age<=10){
								         $scope.ageYear=0;
								         $scope.datepicker = "";
								         $scope.ageDay = "";
                                          growl.warning("It is advisable to enter actual DOB",{
                                                        title : "warning"
                                                        });
                                                        return false;
                                         }
								}

								$scope.objCheck={
										isNumerical:'N' ,
	                                    isAlphanumeric:'N',
	                                    isLength:'N'
								}
							
							$scope.disableEnableExpiry = function(isExpiryVal)
							{
								 var splittedStringArray = isExpiryVal.split("_");
								if(splittedStringArray[1] == 'Y')
								{
									$scope.expiryDateDisabled = false;
//									$scope.datepickerExpiry=null;
								}
								else
									{
									 $scope.expiryDateDisabled = true;
									}
								$scope.patientObj.identificationNumber = '';
								//by chetana 
								var idNo=($scope.patientObj.identificationTypeId).split("_")
								angular.forEach($scope.identificationList, function(value, key){
									if(parseInt(idNo[0])== value.identificationId){
										$scope.objCheck.isNumerical=value.isNumerical;
										$scope.objCheck.isAlphanumeric=value.isAlphanumeric;
										$scope.objCheck.isLength=value.isLength;
										$scope.objCheck.definedLength=value.definedLength;

									}
								});
							}
							
							$scope.disableEnableExpiryKin = function(id,idx)
							{
								
								angular.forEach($scope.identificationList,function(value,key){
									if(value.identificationId == id){
									   if(value.isExpiryRequired == 'Y'){
										   $scope.kinDetailsList[idx].isExpiry='Y';
									   }else{
										   $scope.kinDetailsList[idx].isExpiry='N';
									   }
									}
								});
							
							}
							
							$scope.hideShowAlise = function(vipVal)
							{
								if(vipVal == 'Y')
									$scope.aliseFlag = true;
								else
									$scope.aliseFlag = false;
									
							}
							
							$scope.sameAsLocalCopy = function(vipVal)
							{
								if(vipVal == 'Y')
									{
									
									/*if($scope.patientObj.address =="" || $scope.patientObj.address ==null
										||  $scope.patientObj.countryId =="" ||  $scope.patientObj.countryId ==null 
										||  $scope.patientObj.stateId ==null ||  $scope.patientObj.stateId =="" 
										|| $scope.patientObj.districtId =="" || $scope.patientObj.districtId == null
										|| $scope.patientObj.cityId ==""  ||$scope.patientObj.cityId ==null
										|| $scope.patientObj.areaId == "" || $scope.patientObj.areaId == null){
										//|| $scope.patientObj.phoneCode =="" || $scope.patientObj.phoneNumber ==""){
										$scope.sameAsLocal='N';
										 growl.warning("Please Select All Required fields",{
												title : 'Warning !'
												});
												return false;
									}*/
									
									if($scope.patientObj.countryId != '' )
									$scope.getStateByCountry($scope.patientObj.countryId,'LTOP');
									
									if($scope.patientObj.stateId != '' )
									$scope.getDistrictByState($scope.patientObj.stateId,'LTOP');
									
									if($scope.patientObj.districtId != '' )
									$scope.getCityByDistrict($scope.patientObj.districtId,'LTOP');
									
									if($scope.patientObj.cityId != '' )
									$scope.getAreaByCity($scope.patientObj.cityId,'LTOP');
									
									$scope.patientObj.permanentAddress = $scope.patientObj.address;
									$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
									$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
									$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
									$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
									$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
									$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
									$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
									$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
									
                                    $scope.addressFlag =true;
									}
								else
									{
									/*$scope.patientObj.permanentAddress ='';
									$scope.patientObj.permanentCountryId = '';
									$scope.patientObj.permanentStateId ='';
									$scope.patientObj.permanentDistrictId ='';
									$scope.patientObj.permanentCityId ='';
									$scope.patientObj.permanentAreaId ='';
									$scope.patientObj.permanentZipCode ='';
									$scope.patientObj.permanentPhoneCode ='';
									$scope.patientObj.permanentPhoneNumber ='';*/
                                    $scope.addressFlag =false;
									}
							}
							//get calling code by using country id start
							$scope.getCallingCode=function(data,type){
								var URI = BASE_URL + ROOT_URL
								+ GETSINGLECOUNTRY;
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success" && response.data.listObject.length >0) {
												$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
												$scope.patientObj.whatsappCallingCode = response.data.listObject[0].countryCallingCode;
												
												if(type=="PA"){
													$scope.patientObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
												}else{
													$scope.patientObj.phoneCode = response.data.listObject[0].countryCallingCode;
													if($scope.sameAsLocal == 'Y'){
														$scope.patientObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
													}
												}
											} else {
												//alert("Error!!");
											}
										});
							}
							//get calling code by using country id end
							//get nationality code by using country id start
							$scope.getNationalityByContryId=function(data){
								data.organizationId = $scope.organizationId;
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getNationalityByCountryId";
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success" && response.data.listObject.length > 0) {
												console.log("nationality List ",response.data.listObject[0]);
												$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;

											} else {
//												alert("Error!!");
											}
										});
							}
							//get nationality code by using country id end
							
							// for get state by country ID
							$scope.getStateByCountry = function(id,type) {
								$scope.startLoader();
								var data = {
									countryId : id
								};								
								
								var dataorg = {
										countryId : id,
										organizationId : $scope.organizationId
									};	
								
								$scope.getCallingCode(dataorg,type);
							
								$scope.getNationalityByContryId(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSTATELISTBYCOUNTRYID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
												if(type == 'P')
													{
													$scope.stateList = [];
													$scope.stateList = response.data.listObject;
													$scope.patientObj.stateId="";
													$scope.patientObj.districtId="";
													$scope.patientObj.cityId="";
													$scope.patientObj.areaId="";
													$scope.patientObj.area="";
													$scope.patientObj.areaName="";
													$scope.patientObj.zipCode="";
													/*if($scope.sameAsLocal == 'Y'){
														$scope.patientObj.permanentCountryId = id;
													}*/
													}
												else if(type == 'PA')
													{
													$scope.permanentStateList = [];
													$scope.permanentStateList = response.data.listObject;
													
													$scope.patientObj.permanentStateId="";
													$scope.patientObj.permanentDistrictId="";
													$scope.patientObj.permanentCityId="";
													$scope.patientObj.permanentAreaId="";
													$scope.patientObj.permanentZipCode="";
													}
												else if(type == 'L')
													{
													$scope.stateList = [];
													$scope.stateList = response.data.listObject;
													
													}else if(type == 'LTOP')
													{
														$scope.permanentStateList = [];
														$scope.permanentStateList = response.data.listObject;
														
													}else
													{
													$scope.CompanyStateList = [];
													$scope.CompanyStateList = response.data.listObject;
													}
											}
											
											
											$scope.stopLoader();
										});

							}

							$scope.getDistrictByState = function(id,type) {
								$scope.startLoader();
								var data = {
									stateId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDISTRICTLISTBYSTATEID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
											{
												//$scope.districtList = response.data.listObject;
												if(type == 'P')
												{
												$scope.districtList = [];
												$scope.districtList = response.data.listObject;
												$scope.patientObj.districtId="";
												$scope.patientObj.cityId="";
												$scope.patientObj.areaId="";
												$scope.patientObj.area="";
												$scope.patientObj.areaName="";
												$scope.patientObj.zipCode="";
												}
												else if(type == 'PA')
												{
												$scope.permanentDistrictList = [];
												$scope.permanentDistrictList = response.data.listObject;
												
												$scope.patientObj.permanentDistrictId="";
												$scope.patientObj.permanentCityId="";
												$scope.patientObj.permanentAreaId="";
												$scope.patientObj.permanentZipCode="";
												}else if(type == 'L')
												{
													$scope.districtList = [];
													$scope.districtList = response.data.listObject;
													
												}
											else  if(type == 'LTOP')
											{
												$scope.permanentDistrictList = [];
												$scope.permanentDistrictList = response.data.listObject;
												
											}else
												{
												$scope.companyDistrictList = [];
												$scope.companyDistrictList = response.data.listObject;
												}
											}
											$scope.stopLoader();
										});
								
							}

							$scope.getCityByDistrict = function(id,type) {
								$scope.startLoader();
								var data = {
									districtId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETCITYLISTBYDISTRICTID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
												//$scope.cityList = response.data.listObject;
											{
												//$scope.districtList = response.data.listObject;
												if(type == 'P')
												{
												 $scope.cityList = [];
												 $scope.cityList = response.data.listObject;
												 $scope.patientObj.cityId="";
												$scope.patientObj.areaId="";
												$scope.patientObj.area="";
												$scope.patientObj.areaName="";
												$scope.patientObj.zipCode="";
												}
												else if(type == 'PA')
												{
												$scope.permanentCityList = [];
												$scope.permanentCityList = response.data.listObject;
												
												$scope.patientObj.permanentCityId="";
												$scope.patientObj.permanentAreaId="";
												$scope.patientObj.permanentZipCode="";
												}else if(type == 'L')
												{
													 $scope.cityList = [];
													 $scope.cityList = response.data.listObject;
													
												}else if(type == 'LTOP')
												{
													$scope.permanentCityList = [];
													$scope.permanentCityList = response.data.listObject;
												}	
											else
												{
												$scope.CompanyCityList = [];
												$scope.CompanyCityList = response.data.listObject;
												}
											}
											$scope.stopLoader();
										});
							}
							
							$scope.getAreaByCity = function(id,type) {
								$scope.startLoader();
								$scope.idd = id;
								
								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
												
											{
												$scope.areaNoramalList =[];
												$scope.areaNoramalList = response.data.listObject;
												angular.forEach($scope.areaNoramalList,function(v,k){
													if($scope.patientObj.areaId == v.areaId){
														$scope.patientObj.areaName= v.areaName;
													}
												});
											}
										});
								
								
								var data = {
										offset:0,
										noOfRecordsPerPage:10,
									    cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ "/adt/getAreaByCityIdPagination";
								
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
												//$scope.areaList = response.data.listObject;
											{
												if(type == 'P')
												{
													$scope.areaList = [];
													$scope.logEvents = [];
													 $scope.logEvents =  response.data.listObject;
												console.log($scope.areaList)
												$scope.patientObj.areaId="";
												$scope.patientObj.zipCode="";
												}
												else if(type == 'PA')
												{
												$scope.permanentAreaList = [];
												$scope.permanentAreaList = response.data.listObject;
												
												$scope.patientObj.permanentAreaId="";
												$scope.patientObj.permanentZipCode="";
												}else if(type == 'L')
												{
													$scope.areaList = [];
													$scope.logEvents = [];
//													$scope.areaList =response.data.listObject;
													 $scope.logEvents = response.data.listObject;
													 if($scope.savedAreaId != null && $scope.savedAreaId != ''){
													 angular.forEach($scope.logEvents,function(v,k){
																 if(v.areaId != $scope.patientObj.areaId){
																	 var neeww = {};
																	 neeww.areaCode=null;
																	 neeww.areaId=$scope.savedAreaId ;
																	 neeww.areaName=$scope.savedAreaName ;
																	 $scope.logEvents.push(neeww); 
																 }
													 });
													 }
													console.log("dss",$scope.logEvents);
												}else if(type == 'LTOP')
												{
													$scope.permanentAreaList = [];
													$scope.permanentAreaList = response.data.listObject;
												}	
											else
												{
												$scope.CompanyAreaList = [];
												$scope.CompanyAreaList = response.data.listObject;
												}
											}
											
											//$scope.patientObj.areaId = $scope.patientObjAreaId;
											$scope.stopLoader();
										});
							}
							   
							
							$scope.initAreaMasterList = function(offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
												var data = {
														offset:offset,
														noOfRecordsPerPage:noOfRecordsPerPage,
													    cityId : $scope.idd
												};
												var URI = BASE_URL + ROOT_URL
														+  "/adt/getAreaByCityIdPagination";
												BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.areaList = [];
															if (response.data.status == "success")
															{
																$scope.areaList = [];
																$scope.logEvents = [];
																$scope.areaList =response.data.listObject;
																 $scope.logEvents = response.data.listObject;
																console.log($scope.areaList)
																
															}
														});

							}	
							
							  //for infinite scroll
							 $scope.loadMore = function()
						       {
						    	   $scope.numberToDisplay += 5;
						           $scope.initAreaMasterList($scope.offset,$scope.numberToDisplay);
						       }
							 
							 $scope.applySize = function()
						       {
						    	   $('#selRegsPer').focus(function () {
						    	 	    $('#selRegsPer').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsPer').change(function () {
						    	 	    $('#selRegsPer').attr("size", "1");
						    	 	    $("#selRegsPer").blur();
					    	 	});						       
						    	 	}
							 
							 $scope.applySize1 = function()
						       {
						    	   $('#selRegsthird').focus(function () {
						    	 	    $('#selRegsthird').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsthird').change(function () {
						    	 	    $('#selRegsthird').attr("size", "1");
						    	 	    $("#selRegsthird").blur();
					    	 	});						       
						    	 	}
							 
							 
							 $scope.applySizeKinDetails1 = function()
						       {
						    	   $('#selRegsKinDetails1').focus(function () {
						    	 	    $('#selRegsKinDetails1').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsKinDetails1').change(function () {
						    	 	    $('#selRegsKinDetails1').attr("size", "1");
						    	 	    $("#selRegsKinDetails1").blur();
					    	 	});						       
						    	 	}
							 $scope.applySizeKinDetails2 = function()
						       {
						    	   $('#selRegsKinDetails2').focus(function () {
						    	 	    $('#selRegsKinDetails2').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsKinDetails2').change(function () {
						    	 	    $('#selRegsKinDetails2').attr("size", "1");
						    	 	    $("#selRegsKinDetails2").blur();
					    	 	});						       
						    	 	}
							 $scope.applySizeKinDetails3 = function()
						       {
						    	   $('#selRegsKinDetails3').focus(function () {
						    	 	    $('#selRegsKinDetails3').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsKinDetails3').change(function () {
						    	 	    $('#selRegsKinDetails3').attr("size", "1");
						    	 	    $("#selRegsKinDetails3").blur();
					    	 	});						       
						    	 	}
							 $scope.applySizePermanant = function()
						       {
						    	   $('#selRegsPermanant').focus(function () {
						    	 	    $('#selRegsPermanant').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsPermanant').change(function () {
						    	 	    $('#selRegsPermanant').attr("size", "1");
						    	 	    $("#selRegsPermanant").blur();
					    	 	});	
						    	 	/*$scope.myFunctionForToggle(true);*/
						    	 	}
							 
							 $scope.applySizePermanant1 = function()
						       {
						    	   $('#selRegsPermanant1').focus(function () {
						    	 	    $('#selRegsPermanant1').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsPermanant1').change(function () {
						    	 	    $('#selRegsPermanant1').attr("size", "1");
						    	 	    $("#selRegsPermanant1").blur();
					    	 	});						       
						    	 	}
							 
							 $scope.applySizePermanant2 = function()
						       {
						    	   $('#selRegsPermanant2').focus(function () {
						    	 	    $('#selRegsPermanant2').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsPermanant2').change(function () {
						    	 	    $('#selRegsPermanant2').attr("size", "1");
						    	 	    $("#selRegsPermanant2").blur();
					    	 	});						       
						    	 	}
							 
							 
							 
							if($scope.unitCountryId > 0)
								{
								$scope.patientObj.countryId  = $scope.unitCountryId;
								  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
								  $scope.patientObj.stateId  =  $scope.unitStateId;
								  $scope.patientObj.districtId = $scope.unitDistrictId;
								  $scope.patientObj.cityId = $scope.unitCityId;
								  $scope.patientObj.areaId = $scope.unitAreaId;
								  $scope.patientObj.zipCode = $scope.unitZipCode;
								  $scope.getStateByCountry($scope.patientObj.countryId,'L');
								  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
								  $scope.getDistrictByState($scope.patientObj.stateId,'L');
								  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
								  $scope.getAreaByCity($scope.patientObj.cityId,'L');
//								  $scope.selectFlag = false;
//								  $scope.selectFlag1 =false;
								}

							// function for popup ok button call
							$scope.PopupOkBtn = function () {
							  $scope.popUpFlag = false;
							  $scope.savePatientRegistration();
                               /* angular.element('#confirmLogout').modal('hide');
                                if ($scope.screenType === "Admission-Request") {
                                    $scope.patientObj.age=$scope.age;
                                    $timeout(function () {
                                        $state.go('admissionRequest', {
                                            screenType: "Admission-Request",
                                            patientObj: $scope.patientObj
                                        }, { reload: true });

                                    }, 200);

                                } else {
                                    $scope.savePatientRegistration();

                                }*/
                            };

							/*	$scope.selectItem = function(item) {

                                    $scope.itemKinSelecteds = [];
                                    // If checkbox is checked
                                    if ($scope.kinDetails) {
                                        $scope.itemKinSelecteds = item;
                                    }
                                    console.log($scope.itemKinSelecteds);
                                }*/
							$scope.savePatientRegistration = function(flag)
							{
							if($scope.registrationFieldObj.identificationDoc=="Y"){
							if($scope.patientObj.identificationTypeId==undefined || $scope.patientObj.identificationTypeId==undefined || $scope.patientObj.identificationTypeId=="" )
                                     {
                                         growl.error("Please Select Identification Type.",
                                                 {
                                                     title : 'Error!'
                                                 });
                                                 return false;
                                            }


                    if($scope.patientObj.identificationNumber==undefined || $scope.patientObj.identificationNumber==undefined || $scope.patientObj.identificationNumber=="" )
                                     {
                                         growl.error("Please Enter Identification Number.",
                                                 {
                                                     title : 'Error!'
                                                 });
                                                 return false;
                                            }
							}




								
//							if (!$scope.nextOfKinForm.$valid) {
//									growl.warning("Please Add Kin",{
//										title : WARNING_MSG
//									});
//									return false;
//								}
								//alert("IN SAVE"+ $scope.datepickerExpiry);
								//console.log($scope.patientRegistrationform.$error.required);
								//console.log($scope.patientRegistrationform);
								//console.log($scope.patientObj);
								
								if($scope.areaNotExist  =='Y'){
									
									if($scope.patientObj.countryId == undefined || $scope.patientObj.countryId== null || $scope.patientObj.countryId ===" " || isNaN($scope.patientObj.countryId)){
	                     				growl.error(" Please fill country",{
	                     					title : ERROR_MSG
	                     					});
	                     					return false;
	                     			}
									
//									if($scope.patientObj.area != null && $scope.patientObj.area != ''){
									$scope.patientObj.areaId='';
									if($scope.patientObj.countryId == undefined || $scope.patientObj.countryId== null || $scope.patientObj.countryId ===" " || isNaN($scope.patientObj.countryId)){
	                     				growl.error(" Please fill country, state, district, city details",{
	                     					title : ERROR_MSG
	                     					});
	                     					return false;
	                     			}
									if($scope.patientObj.stateId == undefined || $scope.patientObj.stateId== null || $scope.patientObj.stateId ===" " || isNaN($scope.patientObj.stateId)){
	                     				growl.error(" Please fill country, state, district, city details",{
	                     					title : ERROR_MSG
	                     					});
	                     					return false;
	                     			}
									if($scope.patientObj.districtId == undefined || $scope.patientObj.districtId== null || $scope.patientObj.districtId ===" "|| isNaN($scope.patientObj.districtId)){
	                     				growl.error(" Please fill country, state, district, city details",{
	                     					title : ERROR_MSG
	                     					});
	                     					return false;
	                     			}
									 if (angular.equals({},$stateParams.patientRegistrationObjAbdm)) {
									if($scope.patientObj.cityId == undefined || $scope.patientObj.cityId== null || $scope.patientObj.cityId ===" " || isNaN($scope.patientObj.cityId)){
	                     				growl.error(" Please fill country, state, district, city details",{
	                     					title : ERROR_MSG
	                     					});
	                     					return false;
	                     			}
									 }
//									}
								}

								if($scope.patientObj.prefixId == undefined || $scope.patientObj.prefixId== null || $scope.patientObj.prefixId ==""){
                                    growl.error(" Please select prefix",{
                                        title : ERROR_MSG
                                        });
                                        return false;
                                    }

                                if($scope.patientObj.firstName == undefined ||$scope.patientObj.firstName== null || $scope.patientObj.firstName ==""){
                                    growl.error(" Please enter first name",{
                                        title : ERROR_MSG
                                        });
                                        return false;
                                }

                                if($scope.datepicker == undefined ||$scope.datepicker== null || $scope.datepicker ==""){
                                    growl.error(" Please enter birthDate",{
                                        title : ERROR_MSG
                                        });
                                        return false;
                                }
								
								
							    if($scope.patientObj.mobileNumber == undefined || $scope.patientObj.mobileNumber== null || $scope.patientObj.mobileNumber ==""){
                     				growl.error(" Please enter valid mobile number",{
                     					title : ERROR_MSG
                     					});
                     					return false;
                     			}
							    if($scope.areaNotExist  =='N'){
							    	
							    	
							    	if($scope.patientObj.countryId == undefined || $scope.patientObj.countryId== null || $scope.patientObj.countryId ===" " || isNaN($scope.patientObj.countryId)){
	                     				growl.error(" Please fill country",{
	                     					title : ERROR_MSG
	                     					});
	                     					return false;
	                     			}
							    	
							    	if($scope.patientObj.areaName == null || $scope.patientObj.areaName == '' || $scope.patientObj.areaName == "undefined"){
							    		$scope.patientObj.areaName=null;
						    		    $scope.patientObj.areaId =null;
						    		    $scope.patientObj.area = null;
						    		}
//							    	if($scope.patientObj.areaId != null && $scope.patientObj.areaId != ''){
							    		if($scope.patientObj.areaName == null || $scope.patientObj.areaName == '' || $scope.patientObj.areaName ==  "undefined"){
							    			$scope.patientObj.areaName  = null;
							    			$scope.patientObj.area  = null;
							    		    $scope.patientObj.areaId =null;
							    		}
							    		if($scope.registrationFieldObj.address=="Y"){
                                            if($scope.patientObj.address== undefined ||  $scope.patientObj.address== null ||  $scope.patientObj.address== "" ){
                                                 growl.error(" Please fill address",{
                                                    title : ERROR_MSG
                                                    });
                                                    return false;
                                             }
                                        }
							    		if($scope.patientObj.countryId == undefined || $scope.patientObj.countryId== null || $scope.patientObj.countryId ===" " || isNaN($scope.patientObj.countryId)){
		                     				growl.error(" Please fill country, state, district, city details",{
		                     					title : ERROR_MSG
		                     					});
		                     					return false;
		                     			}
										if($scope.patientObj.stateId == undefined || $scope.patientObj.stateId== null || $scope.patientObj.stateId ===" " || isNaN($scope.patientObj.stateId)){
		                     				growl.error(" Please fill country, state, district, city details",{
		                     					title : ERROR_MSG
		                     					});
		                     					return false;
		                     			}
										if($scope.patientObj.districtId == undefined || $scope.patientObj.districtId== null || $scope.patientObj.districtId ===" " || isNaN($scope.patientObj.districtId)){
		                     				growl.error(" Please fill country, state, district, city details",{
		                     					title : ERROR_MSG
		                     					});
		                     					return false;
		                     			}
										 if (angular.equals({},$stateParams.patientRegistrationObjAbdm)) {
										if($scope.patientObj.cityId == undefined || $scope.patientObj.cityId== null || $scope.patientObj.cityId =="" || isNaN($scope.patientObj.cityId)){
		                     				growl.error(" Please fill country, state, district, city details",{
		                     					title : ERROR_MSG
		                     					});
		                     					return false;
		                     			}
										 }
//							    	}
							  
							    }
								if(flag==true && $scope.updateRegflag==false ){
									$scope.popUpFlag=false;
									return false;
									}
								$scope.$broadcast('show-errors-check-validity');
								 $scope.forthTabLevel=1;
								 if($scope.patientRegistrationform.$valid || $scope.patientObj.area !=""){
						        	var contryCode=$scope.patientObj.countryCallingCode;
						        	 // validating country code
									 var regEx = /^[+-]?\d+$/;
									      if(!regEx.test(contryCode)){
									    	  growl.warning("Please enter valid country calling code",{
													title : 'Warning !'
													});
													return false;
									      } 
						        	
						        	var mobileNo=$scope.patientObj.mobileNumber;
						        	if(contryCode==91){
						        		if(mobileNo.length==$scope.registrationFieldObj.mobileDigit){
						        			
											}else{
												growl.warning("Please enter valid mobile number",{
													title :'Warning !'
													});
													$('#mobileNo').focus();
													return false;
						        		}
						        	}else{
							        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
							        			
											}else{
												growl.warning("Please enter valid mobile number",{
													title : 'Warning !'
													});
													$('#mobileNo').focus();
													return false;
						        		}
						        	}
						        	
						        	
						      
						        	

						           $scope.forthTabLevel=2;			           

						        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
						        		growl.warning("Please select patient category",{
													title : 'Warning !'
													});
													
													return false;
						        	}
									/*if($scope.patientRegistrationform2.$valid){*/
						        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
						        			 var phoneCode=$scope.patientObj.phoneCode;
								        	 // validating country code
											 var regEx = /^[+-]?\d+$/;
											      if(!regEx.test(phoneCode)){
											    	  growl.warning("Please enter valid country calling code",{
															title : 'Warning !'
															});
															return false;
											      } 
						        		 }
						        		 //test start
						        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
					        				 
					        				/* if($scope.patientObj.zipCode.length!=6){
					        					 growl.error("Please enter valid zip code",{
														title : ERROR_MSG
														});
														//$('#mobileNo').focus();
														return false;
					        				 }*/
					        			 }
						        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
					        				 
					        				/* if($scope.patientObj.permanentZipCode.length!=6){
					        					 growl.error("Please enter valid zip code",{
														title : ERROR_MSG
														});
														//$('#mobileNo').focus();
														return false;
					        				 }*/
					        			 }
						        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
						        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
								        	 // validating country code
											 var regEx = /^[+-]?\d+$/;
											      if(!regEx.test(permanentPhoneCode)){
											    	  growl.warning("Please enter valid country calling code",{
															title : 'Warning !'
															});
															return false;
											      } 
						        		 }
						        		 if($scope.patientObj.email == null){
						        			 $scope.patientObj.email = "";
						        		 }
						        		 if($scope.patientObj.email != "")
						           			{
							           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
							           		 var email = $scope.patientObj.email;
							           		    if (!filter.test(email)) {

							           		     $scope.forthTabLevel=1;
						        				    growl.error(EMAIL_MSG,{
														title : ERROR_MSG

													});
						        				    $('#email').focus();
						        				    return false;
							           		    }
							           		    else{
							           		    	if($scope.patientObj.phoneNumber == undefined)
							           		    			$scope.patientObj.phoneNumber='';
							           		    	if($scope.patientObj.phoneNumber == null)
							           		    			$scope.patientObj.phoneNumber='';
							           		     if($scope.patientObj.phoneNumber != '')
									        		{
							           		    	 
							           		    	 
							           		    	/*if($scope.kinDetailsList.length >0)
													{
														var i=1;
														var flagKin=false;
														angular.forEach($scope.kinDetailsList,function(value,key){
															
															if(value.prefixId!="" && value.kinName=="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId!="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName=="" && value.relationId!="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
																if((i==$scope.kinDetailsList.length) && flagKin==true)
																	{
																	growl.warning('Please select kin mandatory fields',
																			{
																				title : 'Warning!'
																			});
																	$scope.setSecondTabLevel(2);
																	return false;
																	}else{
																		if(i==$scope.kinDetailsList.length){
																			$scope.storePatientDetails();
																		}
																	}
																i++;
														});
													}*/
							           		    	 //$scope.storePatientDetails();
							           		      // removed validation as per mphesha requirement
							           		    	/* var val = $scope.patientObj.phoneNumber;
									        			 if (/^\d{10}$/.test(val)) {

									        				 $scope.storePatientDetails();
									        				} else {
									        				   
									        				    $scope.tab=2;
									        				    growl.error(PHONE_NUMBER_MSG,{
																	title : ERROR_MSG
																});
									        				    $('#phoneNumber').focus();
									        				    return false;
									        				}*/
									        		}
							           		     else
							           		    	 {
							           		    	 //$scope.storePatientDetails();
							           		    /*	if($scope.kinDetailsList.length >0)
													{
														var i=1;
														var flagKin=false;
														angular.forEach($scope.kinDetailsList,function(value,key){
															
															if(value.prefixId!="" && value.kinName=="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId!="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName=="" && value.relationId!="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
																if((i==$scope.kinDetailsList.length) && flagKin==true)
																	{
																	growl.warning('Please select kin mandatory fields',
																			{
																				title : 'Warning!'
																			});
																	$scope.setSecondTabLevel(2);
																	return false;
																	}else{
																		if(i==$scope.kinDetailsList.length){
																			$scope.storePatientDetails();
																		}
																	}
																i++;
														});
													}*/
							           		    	 }
							           		    }
						           			}
						        		 else
						        			 {
						        			 if($scope.patientObj.phoneNumber == undefined)
						        				 $scope.patientObj.phoneNumber='';
						        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
						        				 
						        				/* if($scope.patientObj.zipCode.length!=6){
						        					 growl.error("Please enter valid zip code",{
															title : ERROR_MSG
															});
															//$('#mobileNo').focus();
															return false;
						        				 }*/
						        			 }
						        			 if($scope.patientObj.phoneNumber != '')
								        		{
						        				   
						        				 
						        				 //$scope.storePatientDetails();
						        				   // removed validation as per mphesha requirement
								        			/* var val = $scope.patientObj.phoneNumber;
								        			 if (/^\d{10}$/.test(val)) {

								        				 $scope.storePatientDetails();
								        				} else {
								        				   
								        				    $scope.tab=2;
								        				    growl.error(PHONE_NUMBER_MSG,{
																title : ERROR_MSG
															});
								        				    $('#phoneNumber').focus();
								        				    return false;
								        				}*/
						        				 
						        				/* if($scope.kinDetailsList.length >0)
													{
														var i=1;
														var flagKin=false;
														angular.forEach($scope.kinDetailsList,function(value,key){
															
															if(value.prefixId!="" && value.kinName=="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId!="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName=="" && value.relationId!="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
																if((i==$scope.kinDetailsList.length) && flagKin==true)
																	{
																	growl.warning('Please select kin mandatory fields',
																			{
																				title : 'Warning!'
																			});
																	$scope.setSecondTabLevel(2);
																	return false;
																	}else{
																		if(i==$scope.kinDetailsList.length){
																			$scope.storePatientDetails();
																		}
																	}
																i++;
														});
													}*/
								        		}
						        			 else
						        				 {
						        				 //$scope.storePatientDetails();
						        				/* if($scope.kinDetailsList.length >0)
													{
														var i=1;
														var flagKin=false;
														angular.forEach($scope.kinDetailsList,function(value,key){
															
															if(value.prefixId!="" && value.kinName=="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId!="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName=="" && value.relationId!="")
															{
																
																flagKin = true;
															}
															if(value.prefixId=="" && value.kinName!="" && value.relationId=="")
															{
																
																flagKin = true;
															}
																if((i==$scope.kinDetailsList.length) && flagKin==true)
																	{
																	growl.warning('Please select kin mandatory fields',
																			{
																				title : 'Warning!'
																			});
																	$scope.setSecondTabLevel(2);
																	return false;
																	}else{
																		if(i==$scope.kinDetailsList.length){
																			$scope.storePatientDetails();
																		}
																	}
																i++;
														});
													}*/
						        				 }
						        			 }
						        		 var flagKin=false;
						        		 flagKin = true;
					        			 if($scope.kinDetailsList.length >0){
					        				 var i=1;
					        				 angular.forEach($scope.kinDetailsList,function(value,key){

													if($scope.registrationFieldObj.kinprefix== "Y"){
														if(value.prefixId == undefined || value.prefixId == null || value.prefixId ==""){
															growl.warning("Please Add Prefix For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.kinName== "Y"){
														if(value.kinName == undefined || value.kinName == null || value.kinName ==""){
															growl.warning("Please Add Name For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													if($scope.registrationFieldObj.relationId== "Y"){
														if(value.relationId == undefined || value.relationId == null || value.relationId ==""){
															growl.warning("Please Add Relation For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.kinMobileNo== "Y"){
														if(value.mobileNo == undefined || value.mobileNo == null || value.mobileNo ==""){
															growl.warning("Please Add Mobile No For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if ($scope.registrationFieldObj.kinMobileNo === "Y") {
														    angular.forEach($scope.kinDetailsList, function (value, index) {
														        var mobileNo = value.mobileNo;

														        if (mobileNo === undefined || mobileNo === null || mobileNo === "") {
														            growl.warning("Please Add Mobile No For Next Of KIN");
														            flagKin = false;
														            return false; 
														        } else if (mobileNo.length !== $scope.registrationFieldObj.mobileDigit) {
														            growl.warning("Mobile No For Next Of KIN should be " + $scope.registrationFieldObj.mobileDigit + " digits long");
														            flagKin = false;
														            return false; 
														        }
														    });
														}

													 if($scope.registrationFieldObj.kinAddress== "Y"){
														if(value.address == undefined || value.address == null || value.address ==""){
															growl.warning("Please Add Address For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.countryId== "Y"){
														if(value.countryId == undefined || value.countryId == null || value.countryId ==""){
															growl.warning("Please Add Country For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.stateId== "Y"){
														if(value.stateId == undefined || value.stateId == null || value.stateId ==""){
															growl.warning("Please Add State For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.districtId== "Y"){
														if(value.districtId == undefined || value.districtId == null || value.districtId==""){
															growl.warning("Please Add District For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.cityId== "Y"){
														if(value.cityId == undefined || value.cityId == null || value.cityId ==""){
															growl.warning("Please Add City For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.areaId== "Y"){
														if(value.areaId == undefined || value.areaId == null || value.areaId ==""){
															growl.warning("Please Add Area For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.postCode== "Y"){
														if(value.postCode == undefined || value.postCode == null ||value.postCode ==""){
															growl.warning("Please Add Post Code For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.identificationId== "Y"){
														if(value.identificationId == undefined || value.identificationId == null ||value.identificationId ==""){
															growl.warning("Please Add Identification Type For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
													 if($scope.registrationFieldObj.identificationNo== "Y"){
														if(value.identificationNo == undefined || value.identificationNo == null || value.identificationNo ==""){
															growl.warning("Please Add Identification No For Next Of KIN",{
															});
															flagKin=false;
															return false;
														}
													}
												
													
					        					 if((i==$scope.kinDetailsList.length) && flagKin==false)
													{
					        						 $scope.setSecondTabLevel(2);
					        						 return false;
													}else{
														if(i==$scope.kinDetailsList.length){
															$scope.storePatientDetails();
														}
													}
					        					 i++;
					        				 });
					        			 }
					        			 else{
					        				 flagKin==false;
						        			 $scope.storePatientDetails();
						        		 }
						        		
						        }else{
							        	 if($scope.patientObj.registrationTypeId==1){
							        		 $scope.setSecondTabLevel(1);
		           		    	 	 }else if($scope.patientObj.registrationTypeId==2){
												$scope.setThirdTabLevel(1);
		           		    	 	 }else{
		           		    	 	 	   $scope.setForthTabLevel(1);
		           		    	 	 }
						        }
							}
							
							
						    /***============================================Code Image Upload==============================================*/
						    /**PatientImages*/
						    $scope.patientImage1;
						    $scope.patientImage2;
						    $scope.patientImage3;
						    
						    
						    $scope.convertBase64ToImage = function (dataurl, filename) {
							    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
							        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
							    while(n--){
							        u8arr[n] = bstr.charCodeAt(n);
							    }
							    return new File([u8arr], filename, {type:mime});
							}
						    
						    
						    $scope.bucket="";
						    $scope.uploadPatientImage = function(inputFile,patientId,orgId,unitId)
						    {
//						    	$scope.bucket="";
						    	    var file = inputFile;
							        var fd = new FormData();
							        fd.append('file', file );
									var URI1 = BASE_URL + ROOT_URL + PATIENT_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+patientId;
									GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$scope.bucket=response.data.resObject;
											$scope.patientObj.bucketFlag=response.data.resObject;
											
											$rootScope.stopLoader();
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
										}
									});
						    }
						    
						    $scope.copyMobileToWhatsappNo = function () {
						    	$scope.patientObj.whatsappNo = $scope.patientObj.mobileNumber;
					        }
						    $scope.copyCountryCodeToWhatsappCode = function(){
						    	
						    	$scope.patientObj.whatsappCallingCode = $scope.patientObj.countryCallingCode;
						    }
						   
						    /***============================================Code Image Upload==============================================*/
							$scope.storePatientDetails = function()
							{
								//alert("IN STORE");
								console.log("PatientOBJ",$scope.patientObj);
								console.log("ERPatientOBJ",$scope.erPatientObj);
								$scope.patientObj.unitId = $scope.patientObj.unitId !== undefined && $scope.patientObj.unitId !== null ? $scope.patientObj.unitId : $scope.unitId;
								$scope.patientObj.organizationId = $scope.patientObj.organizationId !== undefined && $scope.patientObj.organizationId !== null ? $scope.patientObj.organizationId : $scope.organizationId;
								//return false;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									//validating images
									if($scope.patientObj.registrationTypeId == 1){
										if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
										   {

											     var ext = $scope.patientImage1.name.split(".").pop().toLowerCase();
													if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
														growl.warning('You can only upload *.jpg, *.jpeg, *.png',
																{
																	title : 'warning!'
																});
														$rootScope.stopLoader();
														return false;
													}
										   }
									}
									if($scope.patientObj.registrationTypeId == 2){
										if($scope.patientImage2!=undefined &&  !angular.equals({}, $scope.patientImage2))
										   {
											     var ext = $scope.patientImage2.name.split(".").pop().toLowerCase();
													if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
														growl.warning('You can only upload *.jpg, *.jpeg, *.png',
																{
																	title : 'warning!'
																});
														$rootScope.stopLoader();
														return false;
													}
										   }
									}
									if($scope.patientObj.registrationTypeId == 3){
										if($scope.patientImage3!=undefined &&  !angular.equals({}, $scope.patientImage3))
										   {
											     var ext = $scope.patientImage3.name.split(".").pop().toLowerCase();
													if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
														growl.warning('You can only upload *.jpg, *.jpeg, *.png',
																{
																	title : 'warning!'
																});
														$rootScope.stopLoader();
														return false;
													}
										   }
									}
									
																		
									var flag=false;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									$scope.patientObj.createdDate = $scope.currentDate;
									$scope.patientObj.updatedDate = $scope.currentDate;
									$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
									$scope.patientObj.middleName =  ($scope.patientObj.middleName == null || $scope.patientObj.middleName.trim() == "" || $scope.patientObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.middleName));
									$scope.patientObj.lastName =   ($scope.patientObj.lastName == null || $scope.patientObj.lastName.trim() == "" || $scope.patientObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.lastName));
									$scope.patientObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
							
									if( $scope.kinDetailsList[0].kinName != ""  ){
										$scope.patientObj.kinList = $scope.kinDetailsList;
									}
									
									
									$scope.patientObj.kinDetailsId = ($scope.patientObj.kinDetailsId != "" && $scope.patientObj.kinDetailsId != undefined ) ? $scope.patientObj.kinDetailsId : 0;
									/*if($scope.datepickerExpiry != '')
										{
											$scope.patientObj.identificationExpiryDate = moment($scope.datepickerPdd).format('DD-MM-YYYY HH:mm:ss');
										}*/
									var ConDate = $('#datepickerExpiry').val();
									
//									var ConDate = $scope.datepickerExpiry;
									if($scope.expiryDateDisabled==false && (ConDate != '' && ConDate != undefined)){
										var newExDate = new Date($rootScope.getChangedFormatedDateShalseTwo(ConDate));
										$scope.patientObj.identificationExpiryDate = moment(newExDate).format('DD-MM-YYYY HH:mm:ss');
									}
									
//									 var splittedStringArray = $scope.patientObj.identificationTypeId.split("_");
//									 $scope.patientObj.identificationTypeId = splittedStringArray[0]; 
									if($scope.patientObj.identificationTypeId==undefined || $scope.patientObj.identificationTypeId=="")
										$scope.patientObj.identificationTypeId=null;
									
									
									if($scope.patientObj.identificationTypeId !=null)
									{
										 $scope.patientObjCopy = angular.copy($scope.patientObj);
								      var splittedStringArray = $scope.patientObjCopy.identificationTypeId.split("_");
								      $scope.patientObjCopy.identificationTypeId = splittedStringArray[0];
								      if(splittedStringArray[1]=='Y'){
									      if($scope.patientObjCopy.identificationExpiryDate==undefined || $scope.patientObjCopy.identificationExpiryDate==null || $scope.patientObjCopy.identificationExpiryDate==""){
											   flag=true;
										   }
								      }
								     
									}
									else
									{
										$scope.patientObj.identificationTypeId = null;
										$scope.patientObj.videoCallUrl = $scope.patientDetailsObj.videoCallUrl;
										$scope.patientObjCopy = angular.copy($scope.patientObj);
									}
									if(flag){
										growl.error(
												'Please select expiry date',
												{
													title : 'Error!'
												});
										return;
									}

									if($scope.patientObj.isVip=='N')
										$scope.patientObj.aliseName='';
									
									if($scope.sameAsLocal == 'Y'){
									$scope.patientObj.permanentAddress = $scope.patientObj.address;
									$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
									$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
									$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
									$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
									$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
									$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
									$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
									$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
									}
									$scope.patientObj.uhidNumber=$scope.patientObj.uhidNumber;
									$scope.patientObj.legacyRegistrationId = $scope.patientObj.legacyRegistrationId;
									//$scope.patientObjCopy.area =$scope.patientObjarea;
									 console.log($scope.patientObj);
//									return false;
									 $rootScope.startLoader();
									 if($scope.patientObjCopy.registrationTypeId == 1 && $scope.patientObjCopy.patientId > 0)
										 {
										 
										 
										 
										 var URI = BASE_URL + ROOT_URL
											+ UPDATEPATIENTREGISTRATION;

									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, $scope.patientObjCopy,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response)
													{
														console.log("response",response);
														//$rootScope.startLoader();
														if (response.data.status == "success")
															{
															if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
																   {
																    $scope.uploadPatientImage($scope.patientImage1,response.data.object,$scope.organizationId,$scope.unitId);
																   }
															if($scope.snapshotData!=undefined && $scope.snapshotData!="")
															{
															  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
															  $scope.uploadPatientImage(file,$scope.patientObjCopy.patientId,$scope.organizationId,$scope.unitId);
															}
															//$rootScope.stopLoader();
															}
														// $scope.init();
														//$modalInstance.close();
														$state.reload();
														 if (!angular.equals({},$stateParams.patientRegistrationObjAbdm)) {
														 $state.go("patientRegistration",{
									    					 patientRegistrationObjAbdm :{}
															});
														 }
														$('.modal-backdrop').remove();
														$(document.body).removeClass("modal-open");
														$scope.getDatabaseLinkToCheck();
															
													});
										 }
									 else if($scope.patientObjCopy.registrationTypeId == 2 && $scope.patientObjCopy.patientId > 0)
										 {
										 var URI = BASE_URL + ROOT_URL
											+ UPDATEPATIENTREGISTRATION;
										 console.log("$scope.patientObj",$scope.patientObjCopy);
									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, $scope.patientObjCopy,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														console
														.log(response);
												//$rootScope.startLoader();
												if (response.data.status == "success")
													{
													if($scope.patientImage2!=undefined &&  !angular.equals({}, $scope.patientImage2))
														   {
														    $scope.uploadPatientImage($scope.patientImage2,response.data.object,$scope.organizationId,$scope.unitId);
														   }
													if($scope.snapshotData!=undefined && $scope.snapshotData!="")
													{
													  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
													  $scope.uploadPatientImage(file,$scope.patientObjCopy.patientId,$scope.organizationId,$scope.unitId);
													}
													if($scope.snapshotData!=undefined && $scope.snapshotData!="")
													{
													  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
													  $scope.uploadPatientImage(file,$scope.patientObjCopy.patientId,$scope.organizationId,$scope.unitId);
													}
													
													//$rootScope.stopLoader();
													}
												 //$scope.init();
												//$modalInstance.close();
												$state.reload();
												 if (!angular.equals({},$stateParams.patientRegistrationObjAbdm)) {
													 $state.go("patientRegistration",{
								    					 patientRegistrationObjAbdm :{}
														});
													 }
												$scope.getDatabaseLinkToCheck();
												$('.modal-backdrop').remove();
												$(document.body).removeClass("modal-open");
														 
															
													});
										 }
									 else if($scope.patientObjCopy.registrationTypeId == 4  || $scope.patientObjCopy.registrationTypeId == 3 || $scope.patientObjCopy.registrationTypeId == 6 )
										 {
										 var URI = BASE_URL + ROOT_URL
											+ CONVERTPATIENT;
										
//										 return false;
									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, $scope.patientObjCopy,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														console
																.log(response);
														//$rootScope.startLoader();
														if (response.data.status == "success")
														{	
															if($scope.patientImage3!=undefined &&  !angular.equals({}, $scope.patientImage3))
																{
																 $scope.uploadPatientImage($scope.patientImage3,response.data.object,$scope.organizationId,$scope.unitId);
																}
															if($scope.snapshotData!=undefined && $scope.snapshotData!="")
															{
															  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
															  $scope.uploadPatientImage(file,response.data.object,$scope.organizationId,$scope.unitId);
															}
															$scope.isConversion=false;
															if($scope.isRedirect == 'Y')
																{
																   
																	if($scope.patientDetailsObj.encounterId!=null && $scope.patientDetailsObj.encounterId>0)
																	{
																		
																		var URI = BASE_URL + ROOT_URL
																		+ "/api/opd/paidAppointment/afterConversion";
																		
																		var statusChangeObj={
																				appointmentId : $scope.patientDetailsObj.appointmentId,
																				encounterId : $scope.patientDetailsObj.encounterId,
																				unitTokenNumber : $scope.patientDetailsObj.tokenNumber,
																				deptWiseAppointment : $scope.patientDetailsObj.deptWiseAppointment,
																				unitId : $scope.unitId,
																				organizationId : $scope.organizationId,
																				doctorId : $scope.patientDetailsObj.doctorId,
																				isKioskApplicable : $scope.isKiosk
																				
																		}
																		GenericService
																		.serviceAction("POST", URI, statusChangeObj)
																		.then(
																				function(response) {
																					$scope.reScheduleObjList = [];

																					if (response.data.status == "success") {
																						
																						$state.go("appointmentList");

																					}
																				});
																		
																	}else{
																		$scope.patientDetailsObj.uhidNumber = response.data.code;
																		$scope.patientDetailsObj.patientCategoryId=$scope.patientDetailsObj.patientCategoryId!=null && $scope.patientDetailsObj.patientCategoryId!=undefined ? $scope.patientDetailsObj.patientCategoryId : $scope.patientObj.patientCategoryId;
																		$state.go("encounter.encounterDetails", {
																			appointmentObj: $scope.patientDetailsObj,
																		});
																	}
																}
															else if($scope.isPendingRequest == 'Y')
																{
																	$scope.patientDetailsObj.uhidnumber = response.data.code;
																	$scope.patientDetailsObj.fromPendingAdmissions = 1;
																	$scope.patientDetailsObj.frombed = 1;
																	if($scope.patientDetailsObj.doctorFullName != undefined &&  $scope.patientDetailsObj.doctorFullName != null ){
																		$scope.patientDetailsObj.doctorFullName = $scope.patientDetailsObj.doctorFullName;
																	}else{
																		$scope.patientDetailsObj.doctorFullName = $scope.patientDetailsObj.dFirstName+" "+$scope.patientDetailsObj.dLastName;
																	}
																	$state.go("admission", {
																		admissionObj: $scope.patientDetailsObj,
																	});
																}
															else{
																 //$scope.init();
																//$modalInstance.close();
																$state.reload();
																 if (!angular.equals({},$stateParams.patientRegistrationObjAbdm)) {
																	 $state.go("patientRegistration",{
												    					 patientRegistrationObjAbdm :{}
																		});
																	 }
																$('.modal-backdrop').remove();
																$(document.body).removeClass("modal-open");
															}
															//$rootScope.stopLoader();
														 
														}
															
													});
										 	
										 }
									 else{
										var URI = BASE_URL + ROOT_URL
												+ ADDPATIENTREGISTRATION;
										
										if($scope.patientObjCopy.registrationTypeId == 2)
											{
											$scope.patientObjCopy.isOtcReg = 'Y';
											if($scope.patientImage2!=undefined &&  !angular.equals({}, $scope.patientImage2))
												{
													$scope.patientImage1=$scope.patientImage2;
												}
											}
											
										if($scope.isRedirect == 'Y')
										{
										    $scope.patientObjCopy.appointmentId = $scope.patientDetailsObj.appointmentId;
										}
										
										$scope.patientObjCopy.isAlreadyExist = $scope.patientObj.isAlreadyExist;
										console.log($scope.patientObj);
										//return false;
										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.patientObjCopy,
														NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) {
															console
																	.log(response);
															$scope.patientExists=response.data.message
															console.log($scope.patientExists);
															//$rootScope.startLoader();
															if (response.data.status == "success")
															{
																if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
																{
																  $scope.uploadPatientImage($scope.patientImage1,response.data.object,$scope.organizationId,$scope.unitId);
																}
																if($scope.snapshotData!=undefined && $scope.snapshotData!="")
																{
																  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
																  $scope.uploadPatientImage(file,response.data.object,$scope.organizationId,$scope.unitId);
																}
																
																if($scope.isRedirect == 'Y')
																{
																	var toknNum = $scope.patientDetailsObj.tokenNumber;
																    $scope.patientDetailsObj = response.data.object;
																    $scope.patientDetailsObj.tokenNumber = toknNum;
																	$state.go("encounter.encounterDetails", {
																		appointmentObj: $scope.patientDetailsObj,
																	});
																}
																else{
																	//$state.reload();
																	if($scope.patientObjCopy.registrationTypeId == 1){
																		$scope.patientIdForEnc = response.data.object;
																		$scope.patientNameForAdm = response.data.resObject.firstName;
																		if($scope.screenType=='ER-Registration'){
																		var erPatientDataRoute = response.data.resObject;
																		erPatientDataRoute.priorityId = $scope.erPatientObj.priorityId;
																		erPatientDataRoute.modeOfArrival = $scope.erPatientObj.modeOfArrival;
                                                                           $state.go("unknownPatientReg", {
                                                                                   patientDataERFlow: erPatientDataRoute
                                                                               });

                                                                        	      }
																		else if($scope.screenType=='PatientVisit-Screen'){
																		var patientVisitData = response.data.resObject;
                                                                           $state.go("encounter", {
                                                                                   patientVisitDataScreen: patientVisitData
                                                                               });

                                                                        	      }

                                                                          else if($scope.screenType === "Admission-Request"){
                                                                          var admissionRequestData = response.data.resObject;
                                                                          $state.go("admissionRequest", {
                                                                                 patientObj: admissionRequestData
                                                                             });

                                                                          }

                                                                        	      else{
                                                                        	        angular.element('#afterRegistrationPopup').modal('show');
                                                                        	      }

																	}
																	else
																		{
																		  $state.reload();
																		}
																	
																}
															}else if (response.data.status == "error"){
																$scope.patientObj.isAlreadyExist ='Y';
																angular.element('#patientAlradyExistPopUp').modal('show'); 
															}
														});
								}
										
								}
							}

							$scope.RegistrationId =function () {
                                 angular.element('#Registration_With_Id').modal('show');
                                }

							$scope.gotoEncounterFromRegistration = function()
							{
								$('#afterRegistrationPopup').hide('hide');
								$(".modal-backdrop").hide();
								var data = {
										patientId : $scope.patientIdForEnc,
								       appointmentId : null,
								       appointmentTypeId : 1,
								       slotId : null,
								       fromNewRegistration : 1
								       
								}
								$state.go("encounter.encounterDetails", {
									appointmentObj: data,
								});
							}
							
							$scope.gotoAdmissionFromRegistration = function()
							{
								$('#afterRegistrationPopup').hide('hide');
								$(".modal-backdrop").hide();
								var data = {
									   patientId : $scope.patientIdForEnc,
								       fromNewRegistration : 1,
								       depositAmount : 0,
								       patientName : $scope.patientNameForAdm
								}
								$state.go("admission", {
									admissionObj: data,
								});
							}
							
							$scope.stillOnRegistration = function()
							{
								$('#closeRequisitionPopup').hide('hide');
								$(".modal-backdrop").hide();
								$state.reload();
							}
							
							
							$scope.selectGender = function(id)
							{
								if(id > 0){
								var genId = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].id;
								
								$scope.patientObj.genderId = genId;
								}
								else
									{
									$scope.patientObj.prefixId = '';
									$scope.patientObj.genderId = '';
									}
								
							}
						    $scope.loadMoreData = function()
						    {
						        $scope.offset+=200;
						        $scope.loadMoreFlag=true;
						        $scope.searchPatient($scope.patientSearchObj);
						    }
						    $scope.offset=0;
						    $scope.loadMoreFlag=false;
						    $scope.noOfRecordsPerPage=200;
						    $scope.searchDependentList = [];
				            $scope.searchEmployeeList = [];
				            $scope.searchPatientList = [];
							$scope.searchPatient = function()
							{
								//Validate for tab1
								//alert($scope.patientObj.registrationTypeId);
								//alert($scope.searchType);
								//return false;
								
								if($scope.checkCurrentTab == 1){
									$("#case-sheet").css("background-color",
									"#5F9EA0");
									$("#complaints").css("background-color",
											"#5ab7c8");
									$("#History").css("background-color",
											"#5ab7c8");
									$scope.patientObj.registrationTypeId = 1;
									$scope.searchType = 1;
						        }else if($scope.checkCurrentTab == 2){
									$("#case-sheet").css("background-color",
									"#5ab7c8");
									$("#complaints").css("background-color",
											"#5F9EA0");
									$("#History").css("background-color",
											"#5ab7c8");
									$scope.patientObj.registrationTypeId = 2;
									$scope.searchType = 1;
									$scope.isOTCRegistrationHideShow=true;
								}else if($scope.checkCurrentTab == 3){
									$("#case-sheet").css("background-color",
									"#5ab7c8");
									$("#complaints").css("background-color",
											"#5ab7c8");
									$("#History").css("background-color",
											"#5F9EA0");
									$scope.patientObj.registrationTypeId = 3;
									
								}
								if($scope.patientSearchObj.mobileNumber == undefined || $scope.patientSearchObj.mobileNumber == null){
									$scope.patientSearchObj.mobileNumber = "";	
								}
								if($scope.patientObj.registrationTypeId == 1 && $scope.searchType == 1){
								if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
				           			{
			        			 			//alert($('#datepickerSearch').val());
											growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
											});
											return false;
					           		}
								}
								if($scope.patientObj.registrationTypeId == 1 && $scope.searchType == 2){
									if($scope.employeeSearchObj.typeId == '')
				           			{
											growl.warning("select employee type",{title : "WARNING!"
											});
											return false;
					           		}
									if($scope.employeeSearchObj.typeId == '' && $scope.employeeSearchObj.empCode == '' &&  $scope.employeeSearchObj.employeeName == '' && $scope.employeeSearchObj.specialityId == '' && $scope.employeeSearchObj.identificationTypeId == '' && $scope.employeeSearchObj.identificationNumber == '' )
					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								if($scope.patientObj.registrationTypeId == 1 && $scope.searchType == 3){

									if($scope.dependentSearchObj.typeId == ''&& $scope.datepickerSearch =='' && $scope.employeeSearchObj.empCode == '' &&  $scope.dependentSearchObj.employeeName == '' && $scope.dependentSearchObj.dependentName == '' && $scope.dependentSearchObj.identificationTypeId == '' && $scope.dependentSearchObj.identificationNumber == ''  )

					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
					        		
									}
								//Validate for tab2
								if($scope.patientObj.registrationTypeId == 2 && $scope.searchType == 1){
									if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
					           			{
				        			 			//alert($('#datepickerSearch').val());
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								
								if($scope.patientObj.registrationTypeId == 2 && $scope.searchType == 2){
									if($scope.employeeSearchObj.typeId == '')
				           			{
											growl.warning("select employee type",{title : "WARNING!"
											});
											return false;
					           		}
									if($scope.employeeSearchObj.typeId == '' && $scope.employeeSearchObj.empCode == '' &&  $scope.employeeSearchObj.employeeName == '' && $scope.employeeSearchObj.specialityId == '' && $scope.employeeSearchObj.identificationTypeId == '' && $scope.employeeSearchObj.identificationNumber == '' )
					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								if($scope.patientObj.registrationTypeId == 2 && $scope.searchType == 3){
									if($scope.dependentSearchObj.typeId == '')
				           			{
											growl.warning("select dependent type",{title : "WARNING!"
											});
											return false;
					           		}
									if($scope.employeeSearchObj.typeId == '' && $scope.employeeSearchObj.empCode == '' &&  $scope.employeeSearchObj.employeeName == '' && $scope.employeeSearchObj.specialityId == '' && $scope.employeeSearchObj.identificationTypeId == '' && $scope.employeeSearchObj.identificationNumber == '' )
					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								//Validate for 3
								if($scope.patientObj.registrationTypeId == 3 && $scope.searchType == 1){
									if($scope.patientSearchObj.uhidNumber == '' && $scope.datepickerSearch=='' && $scope.patientSearchObj.patientName == '' &&  $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '' )
					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								if($scope.patientObj.registrationTypeId == 3 && $scope.searchType == 2){
									if($scope.patientSearchObj.uhidNumber == '' && $scope.datepickerSearch=='' && $scope.patientSearchObj.patientName == '' &&  $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' )
					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								if(($scope.patientObj.registrationTypeId == 3 || $scope.patientObj.registrationTypeId == 6) && $scope.searchType == 3){
									$scope.patientObj.registrationTypeId = 3;
									//var dob=$("input:text[name=birthdatepicker]").val();
									if($scope.patientSearchObj.uhidNumber == '' &&  $('#datepickerSearch').val() == '' && $scope.patientSearchObj.patientName == '' &&  $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == ''  && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
					           			{
				        			 			//alert("Invalid");
												growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
												});
												return false;
						           		}
									}
								
								//alert($scope.patientObj.registrationTypeId);
								
								switch (parseInt($scope.patientObj.registrationTypeId)) {
								case 3:
								if($scope.searchType == 0)
									{
									 growl.error("Please Select Conversion Type!!!",{title : "WARNING!"
										});
									 return false;
									}
								else
									{
									
									switch (parseInt($scope.searchType)) {
									case 1:
										/*var ConDate = $('#datepickerSearch').val();
										var dob=$("input:text[name=birthdatepicker]").val();;
										if(dob != '')
											$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(dob);*/
										var ConDate = $('#datepickerSearch').val();
										if(ConDate != '')
										$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
											
								        if($scope.loadMoreFlag==true)
							            {
								        	$scope.patientSearchObj.offset=$scope.offset;
							            $scope.loadMoreFlag=false;
							            }
							        else
							            {
							        	$scope.patientSearchObj.offset=0;
							            $scope.loadMoreFlag=false;
							            $scope.searchPatientList = [];
							            }
								        $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
										$rootScope.startLoader();
										 $scope.patientSearchObj.registrationTypeId = 5;
											//return false;
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATION;
												
												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success")
																		{
																		$scope.searchPatientList = [];
																			angular.forEach(response.data.listObject,function(value,key){
																				$scope.searchPatientList.push(value);
																			});
																			if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																				{
																				  /* $scope.patientObj = {};
																				   $scope.patientObj.registrationTypeId = 1;
																				  
																				   if($scope.unitCountryId > 0)
																					{
																					   var dataorg = {
																								countryId :$scope.unitCountryId,
																								organizationId : $scope.organizationId
																							};	
																						
																					 $scope.getCallingCode(dataorg,type);
																					$scope.patientObj.countryId  = $scope.unitCountryId;
																					  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
																					  $scope.patientObj.stateId  =  $scope.unitStateId;
																					  $scope.patientObj.districtId = $scope.unitDistrictId;
																					  $scope.patientObj.cityId = $scope.unitCityId;
																					  $scope.patientObj.areaId = $scope.unitAreaId;
																					  $scope.patientObj.zipCode = $scope.unitZipCode;
																					  $scope.getStateByCountry($scope.patientObj.countryId,'L');
																					  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
																					  $scope.getDistrictByState($scope.patientObj.stateId,'L');
																					  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
																					  $scope.getAreaByCity($scope.patientObj.cityId,'L');
																					}*/
																				$scope.patientObj = {

																						prefixId : '',
																						firstName : '',
																						middleName : '',
																						lastName : '',
																						genderId : '',
																						countryCallingCode : $scope.patientObj.countryCallingCode,
																						email : '',
																						nationalityId : $scope.patientObj.nationalityId,
																						raceId : $scope.patientObj.raceId,
																						maritalStatusId : '',
																						mobileNumber : '',
																						dob : '',
																						identificationTypeId : '',
																						identificationNumber : '',
																						isVip : '',
																						address : '',
																						bucketFlag : '',
																						countryId : $scope.unitCountryId,
																						stateId : $scope.unitStateId,
																						districtId : $scope.unitDistrictId,
																						cityId :$scope.unitCityId,
																						areaId : $scope.unitAreaId,
																						stateName : '',
																						countryName : '',
																						zipCode : $scope.unitZipCode,
																						phoneCode : '',
																						phoneNumber : '',
																						companyName : '',
																						companyAddress : '',
																						companyCountryId : '',
																						companyStateId : '',
																						companyDistrictId : '',
																						companyCityId : '',
																						companyAreaId : '',
																						companyZipCode : '',
																						companyMobileNumber : '',
																						organizationId : $scope.organizationId,
																						unitId : $scope.unitId,
																						updatedBy : $scope.userId,
																						createdBy : $scope.userId,
																						status : 'A',
																						patientCategoryId:$scope.defaultPatientCategoryId,
																						permanentAddress:'',
																						permanentCountryId:$scope.unitCountryId,
																						permanentStateId:'',
																						permanentDistrictId:'',
																						permanentCityId:'',
																						permanentAreaId:'',
																						permanentZipCode:'',
																						permanentPhoneCode:'',
																						permanentPhoneNumber:'',
																						aliseName:'',
																						bucketFlag:'',
																						identificationExpiryDate:'',
																						occupationId:'',
																						registrationTypeId:3,
																						previousId:'',
																						patientId:0,
																						patientDetailsId:'',
																						uhidNumber:'',
																						imagePath:'',
																						thumbImage:'',
																						whatsappCallingCode : $scope.patientObj.countryCallingCode,
																						whatsappNo: '',
																						skypeId:'',
																						fileSequenceNumber:'',
																						bloodGroup :'',
																						area:'',
																						permanentArea:'',
																						age:'',
																					};
																				$scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																				   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																				   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																				}else{
																					/*$scope.patientObj = {};
																					$scope.patientObj.registrationTypeId = 1;
																					   $scope.patientObj.mobileNumber = "";
																					   $scope.patientObj.whatsappNo  = "";*/
																					$scope.patientObj = {

																							prefixId : '',
																							firstName : '',
																							middleName : '',
																							lastName : '',
																							genderId : '',
																							countryCallingCode : $scope.patientObj.countryCallingCode,
																							email : '',
																							nationalityId : $scope.patientObj.nationalityId,
																							raceId : $scope.patientObj.raceId,
																							maritalStatusId : '',
																							mobileNumber : '',
																							dob : '',
																							identificationTypeId : '',
																							identificationNumber : '',
																							isVip : '',
																							address : '',
																							countryId : $scope.unitCountryId,
																							stateId : $scope.unitStateId,
																							districtId : $scope.unitDistrictId,
																							cityId :$scope.unitCityId,
																							areaId : $scope.unitAreaId,
																							zipCode : $scope.unitZipCode,
																							phoneCode : '',
																							phoneNumber : '',
																							companyName : '',
																							companyAddress : '',
																							companyCountryId : '',
																							companyStateId : '',
																							companyDistrictId : '',
																							companyCityId : '',
																							companyAreaId : '',
																							companyZipCode : '',
																							companyMobileNumber : '',
																							bucketFlag : '',
																							organizationId : $scope.organizationId,
																							unitId : $scope.unitId,
																							updatedBy : $scope.userId,
																							createdBy : $scope.userId,
																							status : 'A',
																							patientCategoryId:$scope.defaultPatientCategoryId,
																							permanentAddress:'',
																							permanentCountryId:$scope.unitCountryId,
																							permanentStateId:'',
																							permanentDistrictId:'',
																							permanentCityId:'',
																							permanentAreaId:'',
																							permanentZipCode:'',
																							permanentPhoneCode:'',
																							permanentPhoneNumber:'',
																							aliseName:'',
																							identificationExpiryDate:'',
																							occupationId:'',
																							registrationTypeId:3,
																							previousId:'',
																							patientId:0,
																							patientDetailsId:'',
																							uhidNumber:'',
																							imagePath:'',
																							thumbImage:'',
																							whatsappCallingCode : $scope.patientObj.countryCallingCode,
																							whatsappNo: '',
																							skypeId:'',
																							fileSequenceNumber:'',
																							bloodGroup :'',
																							area:'',
																							permanentArea:'',
																							age:'',
																						};
																					$scope.openDatePicker = function() {
																						$scope.datepicker.opened = true;
																					};
																					$scope.datepicker = {
																							opened : false
																						};
																					$scope.dateOptions = {
																							formatYear : 'yyyy',
																							showWeeks : false,
																							maxDate:new Date()
																						};
																					$scope.ageDay = 0;
																					$scope.ageMonth = 0;
																					$scope.ageYear = 0;
																				}
																			$rootScope.stopLoader();
																		}else{
																			$rootScope.stopLoader();
																		}
																		
																});
										break;
									case 2:
										/*var dob=$("input:text[name=birthdatepicker]").val();
										if(dob != '')
											$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(dob);
										 console.log($scope.patientSearchObj);*/
										 $scope.patientSearchObj.registrationTypeId = 4;
											//return false;
										 if($scope.loadMoreFlag==true)
								            {
									        	$scope.patientSearchObj.offset=$scope.offset;
								            $scope.loadMoreFlag=false;
								            }
								        else
								            {
								        	$scope.patientSearchObj.offset=0;
								            $scope.loadMoreFlag=false;
								            $scope.searchPatientList = [];
								            }
									        $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATION;
												$rootScope.startLoader();
												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success"){
																		$scope.searchPatientList =[];
																		angular.forEach(response.data.listObject,function(value,key){
																			$scope.searchPatientList.push(value);
																		});
																		if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																		{
																			/*$scope.patientObj = {};
																			$scope.patientObj.registrationTypeId = 1;*/
																			$scope.patientObj = {

																					prefixId : '',
																					firstName : '',
																					middleName : '',
																					lastName : '',
																					genderId : '',
																					countryCallingCode : $scope.patientObj.countryCallingCode,
																					email : '',
																					nationalityId : $scope.patientObj.nationalityId,
																					raceId : $scope.patientObj.raceId,
																					maritalStatusId : '',
																					mobileNumber : '',
																					dob : '',
																					identificationTypeId : '',
																					identificationNumber : '',
																					isVip : '',
																					address : '',
																					countryId : $scope.unitCountryId,
																					stateId : $scope.unitStateId,
																					districtId : $scope.unitDistrictId,
																					cityId :$scope.unitCityId,
																					areaId : $scope.unitAreaId,
																					zipCode : $scope.unitZipCode,
																					phoneCode : '',
																					phoneNumber : '',
																					companyName : '',
																					companyAddress : '',
																					companyCountryId : '',
																					companyStateId : '',
																					companyDistrictId : '',
																					companyCityId : '',
																					companyAreaId : '',
																					companyZipCode : '',
																					companyMobileNumber : '',
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					updatedBy : $scope.userId,
																					createdBy : $scope.userId,
																					status : 'A',
																					patientCategoryId:$scope.defaultPatientCategoryId,
																					permanentAddress:'',
																					permanentCountryId:$scope.unitCountryId,
																					permanentStateId:'',
																					permanentDistrictId:'',
																					permanentCityId:'',
																					permanentAreaId:'',
																					permanentZipCode:'',
																					permanentPhoneCode:'',
																					permanentPhoneNumber:'',
																					aliseName:'',
																					bucketFlag:'',
																					identificationExpiryDate:'',
																					occupationId:'',
																					registrationTypeId:3,
																					previousId:'',
																					patientId:0,
																					patientDetailsId:'',
																					uhidNumber:'',
																					imagePath:'',
																					thumbImage:'',
																					whatsappCallingCode : $scope.patientObj.countryCallingCode,
																					whatsappNo: '',
																					skypeId:'',
																					fileSequenceNumber:'',
																					bloodGroup :'',
																					area:'',
																					permanentArea:'',
																					age:'',
																				};
																			  $scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																		   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																		   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																		}else{
																			/*$scope.patientObj = {};
																			$scope.patientObj.registrationTypeId = 1;
																			   $scope.patientObj.mobileNumber = "";
																			   $scope.patientObj.whatsappNo  = "";*/
																			$scope.patientObj = {

																					prefixId : '',
																					firstName : '',
																					middleName : '',
																					lastName : '',
																					genderId : '',
																					countryCallingCode : $scope.patientObj.countryCallingCode,
																					email : '',
																					nationalityId : $scope.patientObj.nationalityId,
																					raceId : $scope.patientObj.raceId,
																					maritalStatusId : '',
																					mobileNumber : '',
																					dob : '',
																					identificationTypeId : '',
																					identificationNumber : '',
																					isVip : '',
																					address : '',
																					countryId : $scope.unitCountryId,
																					stateId : $scope.unitStateId,
																					districtId : $scope.unitDistrictId,
																					cityId :$scope.unitCityId,
																					areaId : $scope.unitAreaId,
																					zipCode : $scope.unitZipCode,
																					phoneCode : '',
																					phoneNumber : '',
																					companyName : '',
																					companyAddress : '',
																					companyCountryId : '',
																					companyStateId : '',
																					companyDistrictId : '',
																					companyCityId : '',
																					companyAreaId : '',
																					companyZipCode : '',
																					companyMobileNumber : '',
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					updatedBy : $scope.userId,
																					createdBy : $scope.userId,
																					status : 'A',
																					patientCategoryId:$scope.defaultPatientCategoryId,
																					permanentAddress:'',
																					permanentCountryId:$scope.unitCountryId,
																					permanentStateId:'',
																					permanentDistrictId:'',
																					permanentCityId:'',
																					permanentAreaId:'',
																					permanentZipCode:'',
																					permanentPhoneCode:'',
																					permanentPhoneNumber:'',
																					aliseName:'',
																					identificationExpiryDate:'',
																					occupationId:'',
																					registrationTypeId:3,
																					previousId:'',
																					bucketFlag : '',
																					patientId:0,
																					patientDetailsId:'',
																					uhidNumber:'',
																					imagePath:'',
																					thumbImage:'',
																					whatsappCallingCode : $scope.patientObj.countryCallingCode,
																					whatsappNo: '',
																					skypeId:'',
																					fileSequenceNumber:'',
																					bloodGroup :'',
																					area:'',
																					permanentArea:'',
																					age:'',
																				};
																			  $scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																		}
																		$rootScope.stopLoader();
																	}else{
																	
																		$rootScope.stopLoader();
																	}
																		
																});

										break;
									case 3:
										//alert($scope.datepickerSearch);
										/*var ConDate = $('#datepickerSearch').val();
										var dob=$("input:text[name=birthdatepicker]").val();
										if(dob != '')
										$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(dob);*/
										
										var ConDate = $('#datepickerSearch').val();
										if(ConDate != '')
										$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
										
										
										 $scope.patientSearchObj.registrationTypeId = 2;
											//return false;
										 if($scope.loadMoreFlag==true)
								            {
									        	$scope.patientSearchObj.offset=$scope.offset;
								            $scope.loadMoreFlag=false;
								            }
								        else
								            {
								        	$scope.patientSearchObj.offset=0;
								            $scope.loadMoreFlag=false;
								            $scope.searchPatientList = [];
								            }
									        $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
										 $rootScope.startLoader();
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATION;

												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success"){
																		$scope.searchPatientList = [];
																		angular.forEach(response.data.listObject,function(value,key){
																			$scope.searchPatientList.push(value);
																		});
																		if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																		{
																			/*$scope.patientObj = {};
																			$scope.patientObj.registrationTypeId = 1;*/
																			$scope.patientObj = {

																					prefixId : '',
																					firstName : '',
																					middleName : '',
																					lastName : '',
																					genderId : '',
																					countryCallingCode : $scope.patientObj.countryCallingCode,
																					email : '',
																					nationalityId : $scope.patientObj.nationalityId,
																					raceId : $scope.patientObj.raceId,
																					maritalStatusId : '',
																					mobileNumber : '',
																					dob : '',
																					identificationTypeId : '',
																					identificationNumber : '',
																					isVip : '',
																					address : '',
																					countryId : $scope.unitCountryId,
																					stateId : $scope.unitStateId,
																					districtId : $scope.unitDistrictId,
																					cityId :$scope.unitCityId,
																					areaId : $scope.unitAreaId,
																					zipCode : $scope.unitZipCode,
																					phoneCode : '',
																					phoneNumber : '',
																					companyName : '',
																					companyAddress : '',
																					companyCountryId : '',
																					companyStateId : '',
																					companyDistrictId : '',
																					companyCityId : '',
																					companyAreaId : '',
																					companyZipCode : '',
																					companyMobileNumber : '',
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					updatedBy : $scope.userId,
																					createdBy : $scope.userId,
																					status : 'A',
																					patientCategoryId:$scope.defaultPatientCategoryId,
																					permanentAddress:'',
																					permanentCountryId:$scope.unitCountryId,
																					permanentStateId:'',
																					permanentDistrictId:'',
																					permanentCityId:'',
																					permanentAreaId:'',
																					permanentZipCode:'',
																					permanentPhoneCode:'',
																					permanentPhoneNumber:'',
																					aliseName:'',
																					identificationExpiryDate:'',
																					occupationId:'',
																					registrationTypeId:3,
																					previousId:'',
																					patientId:0,
																					patientDetailsId:'',
																					uhidNumber:'',
																					imagePath:'',
																					thumbImage:'',
																					whatsappCallingCode : $scope.patientObj.countryCallingCode,
																					whatsappNo: '',
																					skypeId:'',
																					fileSequenceNumber:'',
																					bloodGroup :'',
																					area:'',
																					permanentArea:'',
																					age:'',
																				};
																			  $scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																		   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																		   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																		}else{
																			/*$scope.patientObj = {};
																			$scope.patientObj.registrationTypeId = 1;
																			   $scope.patientObj.mobileNumber = "";
																			   $scope.patientObj.whatsappNo  = "";*/
																			$scope.patientObj = {

																					prefixId : '',
																					firstName : '',
																					middleName : '',
																					lastName : '',
																					genderId : '',
																					countryCallingCode : $scope.patientObj.countryCallingCode,
																					email : '',
																					nationalityId : $scope.patientObj.nationalityId,
																					raceId : $scope.patientObj.raceId,
																					maritalStatusId : '',
																					mobileNumber : '',
																					dob : '',
																					identificationTypeId : '',
																					identificationNumber : '',
																					isVip : '',
																					address : '',
																					countryId : $scope.unitCountryId,
																					stateId : $scope.unitStateId,
																					districtId : $scope.unitDistrictId,
																					cityId :$scope.unitCityId,
																					areaId : $scope.unitAreaId,
																					zipCode : $scope.unitZipCode,
																					phoneCode : '',
																					phoneNumber : '',
																					companyName : '',
																					companyAddress : '',
																					companyCountryId : '',
																					companyStateId : '',
																					companyDistrictId : '',
																					companyCityId : '',
																					companyAreaId : '',
																					companyZipCode : '',
																					companyMobileNumber : '',
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					updatedBy : $scope.userId,
																					createdBy : $scope.userId,
																					status : 'A',
																					patientCategoryId:$scope.defaultPatientCategoryId,
																					permanentAddress:'',
																					permanentCountryId:$scope.unitCountryId,
																					permanentStateId:'',
																					permanentDistrictId:'',
																					permanentCityId:'',
																					permanentAreaId:'',
																					permanentZipCode:'',
																					permanentPhoneCode:'',
																					permanentPhoneNumber:'',
																					aliseName:'',
																					identificationExpiryDate:'',
																					occupationId:'',
																					registrationTypeId:3,
																					previousId:'',
																					patientId:0,
																					patientDetailsId:'',
																					uhidNumber:'',
																					imagePath:'',
																					thumbImage:'',
																					whatsappCallingCode : $scope.patientObj.countryCallingCode,
																					whatsappNo: '',
																					skypeId:'',
																					fileSequenceNumber:'',
																					bloodGroup :'',
																					area:'',
																					permanentArea:'',
																				};
																			  $scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																		}
																		$rootScope.stopLoader();
																	}else{
																		$rootScope.stopLoader();
																	}
																		
																});
										break;
									default:

									}
										
									}
								break;
								
								case 1:
									if(parseInt($scope.searchType == 0))
										{
										 growl.error("Please Select Type!!!",{title : "WARNING!"
											});
										 return false;
										}
									else
										{
										switch (parseInt($scope.searchType)) {
										case 1:
											
											var ConDate = $('#datepickerSearch').val();
											if(ConDate != '')
											$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
											
											 $scope.patientSearchObj.registrationTypeId = 1;
												//return false;
											 if($scope.loadMoreFlag==true)
									            {
										        	$scope.patientSearchObj.offset=$scope.offset;
									            $scope.loadMoreFlag=false;
									            }
									        else
									            {
									        	$scope.patientSearchObj.offset=0;
									            $scope.loadMoreFlag=false;
									            $scope.searchPatientList = [];
									            }
										        $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
													var URI = BASE_URL + ROOT_URL
															+ GETTEMPORYPATIENTFORPREREGISTRATION;
													$rootScope.startLoader();
													BillingGenericService
															.serviceAction(METHOD_POST,
																	URI, $scope.patientSearchObj,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		if (response.data.status == "success"){
																			$scope.searchPatientList = [];
																			angular.forEach(response.data.listObject,function(value,key){
																				$scope.searchPatientList.push(value);
																			});
																			if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																			{
																				/*$scope.patientObj = {};
																				$scope.patientObj.registrationTypeId = 1;*/
																				$scope.patientObj = {

																						prefixId : '',
																						firstName : '',
																						middleName : '',
																						lastName : '',
																						genderId : '',
																						countryCallingCode : $scope.patientObj.countryCallingCode,
																						email : '',
																						nationalityId : $scope.patientObj.nationalityId,
																						raceId : $scope.patientObj.raceId,
																						maritalStatusId : '',
																						mobileNumber : '',
																						dob : '',
																						identificationTypeId : '',
																						identificationNumber : '',
																						isVip : '',
																						address : '',
																						countryId : $scope.unitCountryId,
																						stateId : $scope.unitStateId,
																						districtId : $scope.unitDistrictId,
																						cityId :$scope.unitCityId,
																						areaId : $scope.unitAreaId,
																						zipCode : $scope.unitZipCode,
																						phoneCode : '',
																						phoneNumber : '',
																						companyName : '',
																						companyAddress : '',
																						companyCountryId : '',
																						companyStateId : '',
																						companyDistrictId : '',
																						companyCityId : '',
																						companyAreaId : '',
																						companyZipCode : '',
																						companyMobileNumber : '',
																						organizationId : $scope.organizationId,
																						unitId : $scope.unitId,
																						updatedBy : $scope.userId,
																						createdBy : $scope.userId,
																						status : 'A',
																						patientCategoryId:$scope.defaultPatientCategoryId,
																						permanentAddress:'',
																						permanentCountryId:$scope.unitCountryId,
																						permanentStateId:'',
																						permanentDistrictId:'',
																						permanentCityId:'',
																						permanentAreaId:'',
																						permanentZipCode:'',
																						permanentPhoneCode:'',
																						permanentPhoneNumber:'',
																						aliseName:'',
																						bucketFlag: $scope.searchPatientList[0].bucketFlag,
																						identificationExpiryDate:'',
																						occupationId:'',
																						registrationTypeId:1,
																						previousId:'',
																						patientId:0,
																						patientDetailsId:'',
																						uhidNumber:'',
																						imagePath:'',
																						thumbImage:'',
																						whatsappCallingCode : $scope.patientObj.countryCallingCode,
																						whatsappNo: '',
																						skypeId:'',
																						fileSequenceNumber:'',
																						bloodGroup :'',
																						area:'',
																						permanentArea:'',
																					};
																				  $scope.openDatePicker = function() {
																						$scope.datepicker.opened = true;
																					};
																					$scope.datepicker = {
																							opened : false
																						};
																					$scope.dateOptions = {
																							formatYear : 'yyyy',
																							showWeeks : false,
																							maxDate:new Date()
																						};
																					$scope.ageDay = 0;
																					$scope.ageMonth = 0;
																					$scope.ageYear = 0;
																			   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																			   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																			}else{
																			/*	$scope.patientObj = {};
																				$scope.patientObj.registrationTypeId = 1;
																				   $scope.patientObj.mobileNumber = "";
																				   $scope.patientObj.whatsappNo  = "";*/
																				$scope.patientObj = {

																						prefixId : '',
																						firstName : '',
																						middleName : '',
																						lastName : '',
																						genderId : '',
																						countryCallingCode : $scope.patientObj.countryCallingCode,
																						email : '',
																						nationalityId : $scope.patientObj.nationalityId,
																						raceId : $scope.patientObj.raceId,
																						maritalStatusId : '',
																						mobileNumber : '',
																						dob : '',
																						identificationTypeId : '',
																						identificationNumber : '',
																						isVip : '',
																						address : '',
																						bucketFlag: $scope.searchPatientList[0].bucketFlag,
																						countryId : $scope.unitCountryId,
																						stateId : $scope.unitStateId,
																						districtId : $scope.unitDistrictId,
																						cityId :$scope.unitCityId,
																						areaId : $scope.unitAreaId,
																						stateName : $scope.patientObj.stateName,
																						countryName : $scope.patientObj.countryName,
																						zipCode : $scope.unitZipCode,
																						phoneCode : '',
																						phoneNumber : '',
																						companyName : '',
																						companyAddress : '',
																						companyCountryId : '',
																						companyStateId : '',
																						companyDistrictId : '',
																						companyCityId : '',
																						companyAreaId : '',
																						companyZipCode : '',
																						companyMobileNumber : '',
																						organizationId : $scope.organizationId,
																						unitId : $scope.unitId,
																						updatedBy : $scope.userId,
																						createdBy : $scope.userId,
																						status : 'A',
																						patientCategoryId:$scope.defaultPatientCategoryId,
																						permanentAddress:'',
																						permanentCountryId:$scope.unitCountryId,
																						permanentStateId:'',
																						permanentDistrictId:'',
																						permanentCityId:'',
																						permanentAreaId:'',
																						permanentZipCode:'',
																						permanentPhoneCode:'',
																						permanentPhoneNumber:'',
																						aliseName:'',
																						identificationExpiryDate:'',
																						occupationId:'',
																						registrationTypeId:1,
																						previousId:'',
																						patientId:0,
																						patientDetailsId:'',
																						uhidNumber:'',
																						imagePath:'',
																						thumbImage:'',
																						whatsappCallingCode : $scope.patientObj.countryCallingCode,
																						whatsappNo: '',
																						skypeId:'',
																						fileSequenceNumber:'',
																						bloodGroup :'',
																						area:'',
																						permanentArea:'',
																						age:'',
																					};
																				  $scope.openDatePicker = function() {
																						$scope.datepicker.opened = true;
																					};
																					$scope.datepicker = {
																							opened : false
																						};
																					$scope.dateOptions = {
																							formatYear : 'yyyy',
																							showWeeks : false,
																							maxDate:new Date()
																						};
																					$scope.ageDay = 0;
																					$scope.ageMonth = 0;
																					$scope.ageYear = 0;
																			}
																			$rootScope.stopLoader();
																		}else{
																			$rootScope.stopLoader();
																		}
																			
																		if($scope.searchPatientList.length == 0){
																			growl.warning("No Result Found!",{
																				title : ERROR_MSG
																			});
																		}
																	});
											break;
										case 2:
											//alert('dvdf');
											var data = {};
											data.organizationId = $scope.organizationId;
											data.identificationTypeId = $scope.employeeSearchObj.identificationTypeId;
											data.identificationNumber = $scope.employeeSearchObj.identificationNumber;
											data.specialityId = $scope.employeeSearchObj.specialityId;
											data.unitId = $scope.unitId;
											data.typeId = $scope.employeeSearchObj.typeId;
											data.isPatientSearch='Y';
											if($scope.employeeSearchObj.typeId == 1)
												{
												data.doctorName = $scope.employeeSearchObj.employeeName;
												data.docCode =  $scope.employeeSearchObj.empCode;
												data.docDesignationId = '0';
								        		data.docCategoryId = '0';
												var URI = BASE_URL + ROOT_URL
												+ SEARCHPLAINDOCTORDETAILSBYCRITERIA;
												}
											if($scope.employeeSearchObj.typeId == 2)
												{
												data.employeeName = $scope.employeeSearchObj.employeeName;
												data.empCode =  $scope.employeeSearchObj.empCode;
												data.empDesignationId = 0;
												data.empCategoryId = 0;
												
												var URI = BASE_URL + ROOT_URL
												+ GETPLAINEMPLOYEEDETAILS;
												}
											
											$rootScope.startLoader();
											if($scope.loadMoreFlag==true)
								            {
												data.offset=$scope.offset;
								            $scope.loadMoreFlag=false;
								            $scope.searchEmployeeList = [];
								            }
								        else
								            {
								        	data.offset=0;
								            $scope.loadMoreFlag=false;
								            $scope.searchEmployeeList = [];
								            }
											data.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success"){
															$scope.searchEmployeeList = [];
															angular.forEach(response.data.listObject,function(value,key){
																$scope.searchEmployeeList.push(value);
															});
															if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
															{
																/*$scope.patientObj = {};
																$scope.patientObj.registrationTypeId = 1;*/
																$scope.patientObj = {

																		prefixId : '',
																		firstName : '',
																		middleName : '',
																		lastName : '',
																		genderId : '',
																		countryCallingCode : $scope.patientObj.countryCallingCode,
																		email : '',
																		nationalityId : $scope.patientObj.nationalityId,
																		raceId : $scope.patientObj.raceId,
																		maritalStatusId : '',
																		mobileNumber : '',
																		dob : '',
																		identificationTypeId : '',
																		identificationNumber : '',
																		isVip : '',
																		address : '',
																		countryId : $scope.unitCountryId,
																		stateId : $scope.unitStateId,
																		districtId : $scope.unitDistrictId,
																		cityId :$scope.unitCityId,
																		areaId : $scope.unitAreaId,
																		zipCode : $scope.unitZipCode,
																		phoneCode : '',
																		phoneNumber : '',
																		companyName : '',
																		companyAddress : '',
																		companyCountryId : '',
																		companyStateId : '',
																		companyDistrictId : '',
																		companyCityId : '',
																		companyAreaId : '',
																		companyZipCode : '',
																		companyMobileNumber : '',
																		organizationId : $scope.organizationId,
																		unitId : $scope.unitId,
																		updatedBy : $scope.userId,
																		createdBy : $scope.userId,
																		status : 'A',
																		patientCategoryId:$scope.defaultPatientCategoryId,
																		permanentAddress:'',
																		permanentCountryId:$scope.unitCountryId,
																		permanentStateId:'',
																		permanentDistrictId:'',
																		permanentCityId:'',
																		permanentAreaId:'',
																		permanentZipCode:'',
																		permanentPhoneCode:'',
																		permanentPhoneNumber:'',
																		aliseName:'',
																		identificationExpiryDate:'',
																		occupationId:'',
																		registrationTypeId:1,
																		previousId:'',
																		patientId:0,
																		patientDetailsId:'',
																		uhidNumber:'',
																		imagePath:'',
																		thumbImage:'',
																		whatsappCallingCode : $scope.patientObj.countryCallingCode,
																		whatsappNo: '',
																		skypeId:'',
																		fileSequenceNumber:'',
																		bloodGroup :'',
																		area:'',
																		permanentArea:'',
																		age:'',
																	};
																  $scope.openDatePicker = function() {
																		$scope.datepicker.opened = true;
																	};
																	$scope.datepicker = {
																			opened : false
																		};
																	$scope.dateOptions = {
																			formatYear : 'yyyy',
																			showWeeks : false,
																			maxDate:new Date()
																		};
																	$scope.ageDay = 0;
																	$scope.ageMonth = 0;
																	$scope.ageYear = 0;
															   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
															   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
															}else{
															/*	$scope.patientObj = {};
																$scope.patientObj.registrationTypeId = 1;
																   $scope.patientObj.mobileNumber = "";
																   $scope.patientObj.whatsappNo  = "";*/
																$scope.patientObj = {

																		prefixId : '',
																		firstName : '',
																		middleName : '',
																		lastName : '',
																		genderId : '',
																		countryCallingCode : $scope.patientObj.countryCallingCode,
																		email : '',
																		nationalityId : $scope.patientObj.nationalityId,
																		raceId : $scope.patientObj.raceId,
																		maritalStatusId : '',
																		mobileNumber : '',
																		dob : '',
																		identificationTypeId : '',
																		identificationNumber : '',
																		isVip : '',
																		address : '',
																		countryId : $scope.unitCountryId,
																		stateId : $scope.unitStateId,
																		districtId : $scope.unitDistrictId,
																		cityId :$scope.unitCityId,
																		areaId : $scope.unitAreaId,
																		zipCode : $scope.unitZipCode,
																		phoneCode : '',
																		phoneNumber : '',
																		companyName : '',
																		companyAddress : '',
																		companyCountryId : '',
																		companyStateId : '',
																		companyDistrictId : '',
																		companyCityId : '',
																		companyAreaId : '',
																		companyZipCode : '',
																		companyMobileNumber : '',
																		organizationId : $scope.organizationId,
																		unitId : $scope.unitId,
																		updatedBy : $scope.userId,
																		createdBy : $scope.userId,
																		status : 'A',
																		patientCategoryId:$scope.defaultPatientCategoryId,
																		permanentAddress:'',
																		permanentCountryId:$scope.unitCountryId,
																		permanentStateId:'',
																		permanentDistrictId:'',
																		permanentCityId:'',
																		permanentAreaId:'',
																		permanentZipCode:'',
																		permanentPhoneCode:'',
																		permanentPhoneNumber:'',
																		aliseName:'',
																		identificationExpiryDate:'',
																		occupationId:'',
																		registrationTypeId:1,
																		previousId:'',
																		patientId:0,
																		patientDetailsId:'',
																		uhidNumber:'',
																		imagePath:'',
																		thumbImage:'',
																		whatsappCallingCode : $scope.patientObj.countryCallingCode,
																		whatsappNo: '',
																		skypeId:'',
																		fileSequenceNumber:'',
																		bloodGroup :'',
																		area:'',
																		permanentArea:'',
																		age:'',
																	};
																  $scope.openDatePicker = function() {
																		$scope.datepicker.opened = true;
																	};
																	$scope.datepicker = {
																			opened : false
																		};
																	$scope.dateOptions = {
																			formatYear : 'yyyy',
																			showWeeks : false,
																			maxDate:new Date()
																		};
																	$scope.ageDay = 0;
																	$scope.ageMonth = 0;
																	$scope.ageYear = 0;
															}
															$rootScope.stopLoader();
														}else{
															$rootScope.stopLoader();
														}
															
														console.log($scope.searchEmployeeList);
													});
											break;
										case 3:
											
											console.log("ff",$scope.dependentSearchObj);
											var URI = BASE_URL + ROOT_URL
											+ SEARCHDEPENDENTDETAILSBYCRITERIA;
											$rootScope.startLoader();
											if($scope.loadMoreFlag==true)
								            {
									        	$scope.dependentSearchObj.offset=$scope.offset;
								            $scope.loadMoreFlag=false;
								            }
								        else
								            {
								        	$scope.dependentSearchObj.offset=0;
								            $scope.loadMoreFlag=false;
								            $scope.searchDependentList = [];
								            }
									        $scope.dependentSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
											BillingGenericService
													.serviceAction(METHOD_POST,
															URI, $scope.dependentSearchObj,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																if (response.data.status == "success"){
																	$scope.searchDependentList = [];
																	angular.forEach(response.data.listObject,function(value,key){
																		$scope.searchDependentList.push(value);
																	});
																	if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																	{
																		/*$scope.patientObj = {};
																		$scope.patientObj.registrationTypeId = 1;*/
																		$scope.patientObj = {

																				prefixId : '',
																				firstName : '',
																				middleName : '',
																				lastName : '',
																				genderId : '',
																				countryCallingCode : $scope.patientObj.countryCallingCode,
																				email : '',
																				nationalityId : $scope.patientObj.nationalityId,
																				raceId : $scope.patientObj.raceId,
																				maritalStatusId : '',
																				mobileNumber : '',
																				dob : '',
																				identificationTypeId : '',
																				identificationNumber : '',
																				isVip : '',
																				address : '',
																				countryId : $scope.unitCountryId,
																				stateId : $scope.unitStateId,
																				districtId : $scope.unitDistrictId,
																				cityId :$scope.unitCityId,
																				areaId : $scope.unitAreaId,
																				zipCode : $scope.unitZipCode,
																				phoneCode : '',
																				phoneNumber : '',
																				companyName : '',
																				companyAddress : '',
																				companyCountryId : '',
																				companyStateId : '',
																				companyDistrictId : '',
																				companyCityId : '',
																				companyAreaId : '',
																				companyZipCode : '',
																				companyMobileNumber : '',
																				organizationId : $scope.organizationId,
																				unitId : $scope.unitId,
																				updatedBy : $scope.userId,
																				createdBy : $scope.userId,
																				status : 'A',
																				patientCategoryId:$scope.defaultPatientCategoryId,
																				permanentAddress:'',
																				permanentCountryId:$scope.unitCountryId,
																				permanentStateId:'',
																				permanentDistrictId:'',
																				permanentCityId:'',
																				permanentAreaId:'',
																				permanentZipCode:'',
																				permanentPhoneCode:'',
																				permanentPhoneNumber:'',
																				aliseName:'',
																				identificationExpiryDate:'',
																				occupationId:'',
																				registrationTypeId:1,
																				previousId:'',
																				patientId:0,
																				patientDetailsId:'',
																				uhidNumber:'',
																				imagePath:'',
																				thumbImage:'',
																				whatsappCallingCode : $scope.patientObj.countryCallingCode,
																				whatsappNo: '',
																				skypeId:'',
																				fileSequenceNumber:'',
																				bloodGroup :'',
																				area:'',
																				permanentArea:'',
																				age:'',
																			};
																		  $scope.openDatePicker = function() {
																				$scope.datepicker.opened = true;
																			};
																			$scope.datepicker = {
																					opened : false
																				};
																			$scope.dateOptions = {
																					formatYear : 'yyyy',
																					showWeeks : false,
																					maxDate:new Date()
																				};
																			$scope.ageDay = 0;
																			$scope.ageMonth = 0;
																			$scope.ageYear = 0;
																	   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																	   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																	}else{
																		/*$scope.patientObj = {};
																		$scope.patientObj.registrationTypeId = 1;
																		   $scope.patientObj.mobileNumber = "";
																		   $scope.patientObj.whatsappNo  = "";*/
																		$scope.patientObj = {

																				prefixId : '',
																				firstName : '',
																				middleName : '',
																				lastName : '',
																				genderId : '',
																				countryCallingCode : $scope.patientObj.countryCallingCode,
																				email : '',
																				nationalityId : $scope.patientObj.nationalityId,
																				raceId : $scope.patientObj.raceId,
																				maritalStatusId : '',
																				mobileNumber : '',
																				dob : '',
																				identificationTypeId : '',
																				identificationNumber : '',
																				isVip : '',
																				address : '',
																				countryId : $scope.unitCountryId,
																				stateId : $scope.unitStateId,
																				districtId : $scope.unitDistrictId,
																				cityId :$scope.unitCityId,
																				areaId : $scope.unitAreaId,
																				zipCode : $scope.unitZipCode,
																				phoneCode : '',
																				phoneNumber : '',
																				companyName : '',
																				companyAddress : '',
																				companyCountryId : '',
																				companyStateId : '',
																				companyDistrictId : '',
																				companyCityId : '',
																				companyAreaId : '',
																				companyZipCode : '',
																				companyMobileNumber : '',
																				organizationId : $scope.organizationId,
																				unitId : $scope.unitId,
																				updatedBy : $scope.userId,
																				createdBy : $scope.userId,
																				status : 'A',
																				patientCategoryId:$scope.defaultPatientCategoryId,
																				permanentAddress:'',
																				permanentCountryId:$scope.unitCountryId,
																				permanentStateId:'',
																				permanentDistrictId:'',
																				permanentCityId:'',
																				permanentAreaId:'',
																				permanentZipCode:'',
																				permanentPhoneCode:'',
																				permanentPhoneNumber:'',
																				aliseName:'',
																				identificationExpiryDate:'',
																				occupationId:'',
																				registrationTypeId:1,
																				previousId:'',
																				patientId:0,
																				patientDetailsId:'',
																				uhidNumber:'',
																				imagePath:'',
																				thumbImage:'',
																				whatsappCallingCode : $scope.patientObj.countryCallingCode,
																				whatsappNo: '',
																				skypeId:'',
																				fileSequenceNumber:'',
																				bloodGroup :'',
																				area:'',
																				permanentArea:'',
																				age:'',
																			};
																		  $scope.openDatePicker = function() {
																				$scope.datepicker.opened = true;
																			};
																			$scope.datepicker = {
																					opened : false
																				};
																			$scope.dateOptions = {
																					formatYear : 'yyyy',
																					showWeeks : false,
																					maxDate:new Date()
																				};
																			$scope.ageDay = 0;
																			$scope.ageMonth = 0;
																			$scope.ageYear = 0;
																	}
																	$rootScope.stopLoader();
																}else{
																	
																	$rootScope.stopLoader();
																}
																	
																console.log($scope.searchDependentList);
															});
											break;
										default:

										}
											
										}
									break;
								case 2:
									
									if($scope.searchType == 0)
									{
										growl.error("Please Select Type!!!",{title : "WARNING!"
										});
									 return false;
									}
								else
									{
									switch (parseInt($scope.searchType)) {
									case 1:
										//alert('fdvfdv');
										var ConDate = $('#datepickerSearch').val();
										if(ConDate != '')
										$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
										$rootScope.startLoader();
										 $scope.patientSearchObj.registrationTypeId = 2;
										 if($scope.loadMoreFlag==true)
								            {
									        	$scope.patientSearchObj.offset=$scope.offset;
								            $scope.loadMoreFlag=false;
								            }
								        else
								            {
								        	$scope.patientSearchObj.offset=0;
								            $scope.loadMoreFlag=false;
								            $scope.searchPatientList = [];
								            }
									        $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
											//return false;
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATION;

												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success"){
																		$scope.searchPatientList = [];
																		$scope.searchPatientList = response.data.listObject;
																		
																		if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																		{
																			/*$scope.patientObj = {};
																			$scope.patientObj.registrationTypeId = 1;*/
																			var registrationType = $scope.patientObj.registrationTypeId;
																			$scope.patientObj = {

																					prefixId : '',
																					firstName : '',
																					middleName : '',
																					lastName : '',
																					genderId : '',
																					countryCallingCode : $scope.patientObj.countryCallingCode,
																					email : '',
																					nationalityId : $scope.patientObj.nationalityId,
																					raceId : $scope.patientObj.raceId,
																					maritalStatusId : '',
																					mobileNumber : '',
																					dob : '',
																					identificationTypeId : '',
																					identificationNumber : '',
																					isVip : '',
																					address : '',
																					countryId : $scope.unitCountryId,
																					stateId : $scope.unitStateId,
																					districtId : $scope.unitDistrictId,
																					cityId :$scope.unitCityId,
																					areaId : $scope.unitAreaId,
																					zipCode : $scope.unitZipCode,
																					phoneCode : '',
																					phoneNumber : '',
																					companyName : '',
																					companyAddress : '',
																					companyCountryId : '',
																					companyStateId : '',
																					companyDistrictId : '',
																					companyCityId : '',
																					companyAreaId : '',
																					companyZipCode : '',
																					companyMobileNumber : '',
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					updatedBy : $scope.userId,
																					createdBy : $scope.userId,
																					status : 'A',
																					patientCategoryId:$scope.defaultPatientCategoryId,
																					permanentAddress:'',
																					permanentCountryId:$scope.unitCountryId,
																					permanentStateId:'',
																					permanentDistrictId:'',
																					permanentCityId:'',
																					permanentAreaId:'',
																					permanentZipCode:'',
																					permanentPhoneCode:'',
																					permanentPhoneNumber:'',
																					bucketFlag:'',
																					aliseName:'',
																					identificationExpiryDate:'',
																					occupationId:'',
																					registrationTypeId:registrationType,
																					previousId:'',
																					patientId:0,
																					patientDetailsId:'',
																					uhidNumber:'',
																					imagePath:'',
																					thumbImage:'',
																					whatsappCallingCode : $scope.patientObj.countryCallingCode,
																					whatsappNo: '',
																					skypeId:'',
																					fileSequenceNumber:'',
																					bloodGroup :'',
																					area:'',
																					permanentArea:'',
																					age:'',
																				};
																			  $scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																		   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																		   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																		}else{
																			/*$scope.patientObj = {};
																			$scope.patientObj.registrationTypeId = 1;
																			   $scope.patientObj.mobileNumber = "";
																			   $scope.patientObj.whatsappNo  = "";*/
																			var registrationType = $scope.patientObj.registrationTypeId;
																			$scope.patientObj = {

																					prefixId : '',
																					firstName : '',
																					middleName : '',
																					lastName : '',
																					genderId : '',
																					countryCallingCode : $scope.patientObj.countryCallingCode,
																					email : '',
																					nationalityId : $scope.patientObj.nationalityId,
																					raceId : $scope.patientObj.raceId,
																					maritalStatusId : '',
																					mobileNumber : '',
																					dob : '',
																					identificationTypeId : '',
																					identificationNumber : '',
																					isVip : '',
																					address : '',
																					countryId : $scope.unitCountryId,
																					stateId : $scope.unitStateId,
																					districtId : $scope.unitDistrictId,
																					cityId :$scope.unitCityId,
																					areaId : $scope.unitAreaId,
																					zipCode : $scope.unitZipCode,
																					phoneCode : '',
																					phoneNumber : '',
																					companyName : '',
																					companyAddress : '',
																					companyCountryId : '',
																					companyStateId : '',
																					companyDistrictId : '',
																					companyCityId : '',
																					companyAreaId : '',
																					companyZipCode : '',
																					companyMobileNumber : '',
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					updatedBy : $scope.userId,
																					createdBy : $scope.userId,
																					status : 'A',
																					patientCategoryId:$scope.defaultPatientCategoryId,
																					permanentAddress:'',
																					permanentCountryId:$scope.unitCountryId,
																					permanentStateId:'',
																					permanentDistrictId:'',
																					permanentCityId:'',
																					permanentAreaId:'',
																					permanentZipCode:'',
																					permanentPhoneCode:'',
																					permanentPhoneNumber:'',
																					aliseName:'',
																					identificationExpiryDate:'',
																					occupationId:'',
																					registrationTypeId:registrationType,
																					previousId:'',
																					patientId:0,
																					patientDetailsId:'',
																					uhidNumber:'',
																					imagePath:'',
																					thumbImage:'',
																					whatsappCallingCode : $scope.patientObj.countryCallingCode,
																					whatsappNo: '',
																					skypeId:'',
																					fileSequenceNumber:'',
																					bloodGroup :'',
																					area:'',
																					permanentArea:'',
																					age:'',
																				};
																			  $scope.openDatePicker = function() {
																					$scope.datepicker.opened = true;
																				};
																				$scope.datepicker = {
																						opened : false
																					};
																				$scope.dateOptions = {
																						formatYear : 'yyyy',
																						showWeeks : false,
																						maxDate:new Date()
																					};
																				$scope.ageDay = 0;
																				$scope.ageMonth = 0;
																				$scope.ageYear = 0;
																		}
																		$rootScope.stopLoader();
																	}else{
																		
																		$rootScope.stopLoader();
																	}
																		
																});
										break;
									case 2:
										var data = {};
										data.organizationId = $scope.organizationId;
										data.identificationTypeId = $scope.employeeSearchObj.identificationTypeId;
										data.identificationNumber = $scope.employeeSearchObj.identificationNumber;
										data.specialityId = $scope.employeeSearchObj.specialityId;
										data.unitId = $scope.unitId;
										data.typeId = $scope.employeeSearchObj.typeId;
										data.isPatientSearch='Y';
										if($scope.employeeSearchObj.typeId == 1)
											{
											data.doctorName = $scope.employeeSearchObj.employeeName;
											data.docCode =  $scope.employeeSearchObj.empCode;
											data.docDesignationId = '0';
							        		data.docCategoryId = '0	';
											var URI = BASE_URL + ROOT_URL
											+ SEARCHPLAINDOCTORDETAILSBYCRITERIA;
											}
										if($scope.employeeSearchObj.typeId == 2)
											{
											data.employeeName = $scope.employeeSearchObj.employeeName;
											data.empCode =  $scope.employeeSearchObj.empCode;
											data.empDesignationId = 0;
											data.empCategoryId = 0;
											
											var URI = BASE_URL + ROOT_URL
											+ GETPLAINEMPLOYEEDETAILS;
											}
										
										$rootScope.startLoader();
										if($scope.loadMoreFlag==true)
							            {
											data.offset=$scope.offset;
							            $scope.loadMoreFlag=false;
							            }
							        else
							            {
							        	data.offset=0;
							            $scope.loadMoreFlag=false;
							            $scope.searchEmployeeList = [];
							            }
										data.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success"){
														$scope.searchEmployeeList= [];
														angular.forEach(response.data.listObject,function(value,key){
															$scope.searchEmployeeList.push(value);
														});
														if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
														{
															/*$scope.patientObj = {};
															$scope.patientObj.registrationTypeId = 1;*/
															var registrationType = $scope.patientObj.registrationTypeId;
															$scope.patientObj = {

																	prefixId : '',
																	firstName : '',
																	middleName : '',
																	lastName : '',
																	genderId : '',
																	countryCallingCode : $scope.patientObj.countryCallingCode,
																	email : '',
																	nationalityId : $scope.patientObj.nationalityId,
																	raceId : $scope.patientObj.raceId,
																	maritalStatusId : '',
																	mobileNumber : '',
																	dob : '',
																	identificationTypeId : '',
																	identificationNumber : '',
																	isVip : '',
																	address : '',
																	countryId : $scope.unitCountryId,
																	stateId : $scope.unitStateId,
																	districtId : $scope.unitDistrictId,
																	cityId :$scope.unitCityId,
																	areaId : $scope.unitAreaId,
																	zipCode : $scope.unitZipCode,
																	phoneCode : '',
																	phoneNumber : '',
																	companyName : '',
																	companyAddress : '',
																	companyCountryId : '',
																	companyStateId : '',
																	companyDistrictId : '',
																	companyCityId : '',
																	companyAreaId : '',
																	companyZipCode : '',
																	companyMobileNumber : '',
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	updatedBy : $scope.userId,
																	createdBy : $scope.userId,
																	status : 'A',
																	patientCategoryId:$scope.defaultPatientCategoryId,
																	permanentAddress:'',
																	permanentCountryId:$scope.unitCountryId,
																	permanentStateId:'',
																	permanentDistrictId:'',
																	permanentCityId:'',
																	permanentAreaId:'',
																	permanentZipCode:'',
																	permanentPhoneCode:'',
																	permanentPhoneNumber:'',
																	aliseName:'',
																	identificationExpiryDate:'',
																	occupationId:'',
																	registrationTypeId:registrationType,
																	previousId:'',
																	patientId:0,
																	patientDetailsId:'',
																	uhidNumber:'',
																	imagePath:'',
																	thumbImage:'',
																	whatsappCallingCode : $scope.patientObj.countryCallingCode,
																	whatsappNo: '',
																	skypeId:'',
																	fileSequenceNumber:'',
																	bloodGroup :'',
																	area:'',
																	permanentArea:'',
																	age:'',
																};
															  $scope.openDatePicker = function() {
																	$scope.datepicker.opened = true;
																};
																$scope.datepicker = {
																		opened : false
																	};
																$scope.dateOptions = {
																		formatYear : 'yyyy',
																		showWeeks : false,
																		maxDate:new Date()
																	};
																$scope.ageDay = 0;
																$scope.ageMonth = 0;
																$scope.ageYear = 0;
														   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
														   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
														}else{
														/*	$scope.patientObj = {};
															$scope.patientObj.registrationTypeId = 1;
															   $scope.patientObj.mobileNumber = "";
															   $scope.patientObj.whatsappNo  = "";*/
															var registrationType = $scope.patientObj.registrationTypeId;
															$scope.patientObj = {

																	prefixId : '',
																	firstName : '',
																	middleName : '',
																	lastName : '',
																	genderId : '',
																	countryCallingCode : $scope.patientObj.countryCallingCode,
																	email : '',
																	nationalityId : $scope.patientObj.nationalityId,
																	raceId : $scope.patientObj.raceId,
																	maritalStatusId : '',
																	mobileNumber : '',
																	dob : '',
																	identificationTypeId : '',
																	identificationNumber : '',
																	isVip : '',
																	address : '',
																	countryId : $scope.unitCountryId,
																	stateId : $scope.unitStateId,
																	districtId : $scope.unitDistrictId,
																	cityId :$scope.unitCityId,
																	areaId : $scope.unitAreaId,
																	zipCode : $scope.unitZipCode,
																	phoneCode : '',
																	phoneNumber : '',
																	companyName : '',
																	companyAddress : '',
																	companyCountryId : '',
																	companyStateId : '',
																	companyDistrictId : '',
																	companyCityId : '',
																	companyAreaId : '',
																	companyZipCode : '',
																	companyMobileNumber : '',
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	updatedBy : $scope.userId,
																	createdBy : $scope.userId,
																	status : 'A',
																	patientCategoryId:$scope.defaultPatientCategoryId,
																	permanentAddress:'',
																	permanentCountryId:$scope.unitCountryId,
																	permanentStateId:'',
																	permanentDistrictId:'',
																	permanentCityId:'',
																	permanentAreaId:'',
																	permanentZipCode:'',
																	permanentPhoneCode:'',
																	permanentPhoneNumber:'',
																	aliseName:'',
																	identificationExpiryDate:'',
																	occupationId:'',
																	registrationTypeId:registrationType,
																	previousId:'',
																	patientId:0,
																	patientDetailsId:'',
																	uhidNumber:'',
																	imagePath:'',
																	thumbImage:'',
																	whatsappCallingCode : $scope.patientObj.countryCallingCode,
																	whatsappNo: '',
																	skypeId:'',
																	fileSequenceNumber:'',
																	bloodGroup :'',
																	area:'',
																	permanentArea:'',
																	age:'',
																};
															  $scope.openDatePicker = function() {
																	$scope.datepicker.opened = true;
																};
																$scope.datepicker = {
																		opened : false
																	};
																$scope.dateOptions = {
																		formatYear : 'yyyy',
																		showWeeks : false,
																		maxDate:new Date()
																	};
																$scope.ageDay = 0;
																$scope.ageMonth = 0;
																$scope.ageYear = 0;
														}
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
														
													console.log($scope.searchEmployeeList);
												});
										break;
									case 3:
										
										console.log("ff",$scope.dependentSearchObj);
										$rootScope.startLoader();
										if($scope.loadMoreFlag==true)
							            {
								        	$scope.dependentSearchObj.offset=$scope.offset;
							            $scope.loadMoreFlag=false;
							            }
							        else
							            {
							        	$scope.dependentSearchObj.offset=0;
							            $scope.loadMoreFlag=false;
							            $scope.searchDependentList = [];
							            }
								        $scope.dependentSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
										var URI = BASE_URL + ROOT_URL
										//+ SEARCHDOCTORDETAILSBYCRITERIA;
										+SEARCHDEPENDENTDETAILSBYCRITERIA;
										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.dependentSearchObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															if (response.data.status == "success"){
																angular.forEach(response.data.listObject,function(value,key){
																	$scope.searchDependentList.push(value);
																});
																if(response.data.listObject.length == 0 && $scope.patientSearchObj.mobileNumber.length > 0)
																{
																	/*$scope.patientObj = {};
																	$scope.patientObj.registrationTypeId = 1;*/
																	var registrationType = $scope.patientObj.registrationTypeId;
																	$scope.patientObj = {

																			prefixId : '',
																			firstName : '',
																			middleName : '',
																			lastName : '',
																			genderId : '',
																			countryCallingCode : $scope.patientObj.countryCallingCode,
																			email : '',
																			nationalityId : $scope.patientObj.nationalityId,
																			raceId : $scope.patientObj.raceId,
																			maritalStatusId : '',
																			mobileNumber : '',
																			dob : '',
																			identificationTypeId : '',
																			identificationNumber : '',
																			isVip : '',
																			address : '',
																			countryId : $scope.unitCountryId,
																			stateId : $scope.unitStateId,
																			districtId : $scope.unitDistrictId,
																			cityId :$scope.unitCityId,
																			areaId : $scope.unitAreaId,
																			zipCode : $scope.unitZipCode,
																			phoneCode : '',
																			phoneNumber : '',
																			companyName : '',
																			companyAddress : '',
																			companyCountryId : '',
																			companyStateId : '',
																			companyDistrictId : '',
																			companyCityId : '',
																			companyAreaId : '',
																			companyZipCode : '',
																			companyMobileNumber : '',
																			organizationId : $scope.organizationId,
																			unitId : $scope.unitId,
																			updatedBy : $scope.userId,
																			createdBy : $scope.userId,
																			status : 'A',
																			patientCategoryId:$scope.defaultPatientCategoryId,
																			permanentAddress:'',
																			permanentCountryId:$scope.unitCountryId,
																			permanentStateId:'',
																			permanentDistrictId:'',
																			permanentCityId:'',
																			permanentAreaId:'',
																			permanentZipCode:'',
																			permanentPhoneCode:'',
																			permanentPhoneNumber:'',
																			aliseName:'',
																			identificationExpiryDate:'',
																			occupationId:'',
																			registrationTypeId:registrationType,
																			previousId:'',
																			patientId:0,
																			patientDetailsId:'',
																			uhidNumber:'',
																			imagePath:'',
																			thumbImage:'',
																			whatsappCallingCode : $scope.patientObj.countryCallingCode,
																			whatsappNo: '',
																			skypeId:'',
																			fileSequenceNumber:'',
																			bloodGroup :'',
																			area:'',
																			permanentArea:'',
																			age:'',
																		};
																	  $scope.openDatePicker = function() {
																			$scope.datepicker.opened = true;
																		};
																		$scope.datepicker = {
																				opened : false
																			};
																		$scope.dateOptions = {
																				formatYear : 'yyyy',
																				showWeeks : false,
																				maxDate:new Date()
																			};
																		$scope.ageDay = 0;
																		$scope.ageMonth = 0;
																		$scope.ageYear = 0;
																   $scope.patientObj.mobileNumber = $scope.patientSearchObj.mobileNumber;
																   $scope.patientObj.whatsappNo  = $scope.patientSearchObj.mobileNumber;
																}else{
																/*	$scope.patientObj = {};
																	$scope.patientObj.registrationTypeId = 1;
																	   $scope.patientObj.mobileNumber = "";
																	   $scope.patientObj.whatsappNo  = "";*/
																	var registrationType = $scope.patientObj.registrationTypeId;
																	$scope.patientObj = {

																			prefixId : '',
																			firstName : '',
																			middleName : '',
																			lastName : '',
																			genderId : '',
																			countryCallingCode : $scope.patientObj.countryCallingCode,
																			email : '',
																			nationalityId : $scope.patientObj.nationalityId,
																			raceId : $scope.patientObj.raceId,
																			maritalStatusId : '',
																			mobileNumber : '',
																			dob : '',
																			identificationTypeId : '',
																			identificationNumber : '',
																			isVip : '',
																			address : '',
																			countryId : $scope.unitCountryId,
																			stateId : $scope.unitStateId,
																			districtId : $scope.unitDistrictId,
																			cityId :$scope.unitCityId,
																			areaId : $scope.unitAreaId,
																			zipCode : $scope.unitZipCode,
																			phoneCode : '',
																			phoneNumber : '',
																			companyName : '',
																			companyAddress : '',
																			companyCountryId : '',
																			companyStateId : '',
																			companyDistrictId : '',
																			companyCityId : '',
																			companyAreaId : '',
																			companyZipCode : '',
																			companyMobileNumber : '',
																			organizationId : $scope.organizationId,
																			unitId : $scope.unitId,
																			updatedBy : $scope.userId,
																			createdBy : $scope.userId,
																			status : 'A',
																			patientCategoryId:$scope.defaultPatientCategoryId,
																			permanentAddress:'',
																			permanentCountryId:$scope.unitCountryId,
																			permanentStateId:'',
																			permanentDistrictId:'',
																			permanentCityId:'',
																			permanentAreaId:'',
																			permanentZipCode:'',
																			permanentPhoneCode:'',
																			permanentPhoneNumber:'',
																			aliseName:'',
																			identificationExpiryDate:'',
																			occupationId:'',
																			registrationTypeId:registrationType,
																			previousId:'',
																			patientId:0,
																			patientDetailsId:'',
																			uhidNumber:'',
																			imagePath:'',
																			thumbImage:'',
																			whatsappCallingCode : $scope.patientObj.countryCallingCode,
																			whatsappNo: '',
																			skypeId:'',
																			fileSequenceNumber:'',
																			bloodGroup :'',
																			area:'',
																			permanentArea:'',
																			age:'',
																		};
																	  $scope.openDatePicker = function() {
																			$scope.datepicker.opened = true;
																		};
																		$scope.datepicker = {
																				opened : false
																			};
																		$scope.dateOptions = {
																				formatYear : 'yyyy',
																				showWeeks : false,
																				maxDate:new Date()
																			};
																		$scope.ageDay = 0;
																		$scope.ageMonth = 0;
																		$scope.ageYear = 0;
																}
																$rootScope.stopLoader();
															}else{
															
																$rootScope.stopLoader();
															}
																
															console.log($scope.searchDependentList);
														});
										break;
									default:

									}
										
									}
									break;
								default:
							}
									
							}
							
							
							$scope.getDepartment = function(item){
								var specDocObj = {
						    			organizationId:$scope.organizationId,
						    			doctorId:item.doctorId
						    }
								$scope.docSpecialityList = [];
						    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByDoctorIdByOrg";
					BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
										console.log(response);
										if (response.data.status == "success"){
											$scope.docSpecialityList = [];
		                                  $scope.docSpecialityList = response.data.listObject;
										}
		                                 });
							}
							
							$scope.filterFieldsHideShow = function()
							{
								
								$scope.initSearchObj();
								$scope.searchPatientList = []; 
								$scope.searchEmployeeList = []; 
								$scope.searchDependentList =[];
								
								//alert($scope.searchType);
								switch (parseInt($scope.searchType)) {
								case 1:
									
									$scope.searchPatientList=[];
									$scope.birthDateSearchFlag = false;
									$scope.preReg=true;
									break;
								case 2:
									
									$scope.searchPatientList=[];
									$scope.birthDateSearchFlag = false;
									$scope.preReg=false;
									break;
								case 3:
									
									$scope.searchPatientList=[];
									$scope.birthDateSearchFlag = true;
									$scope.preReg=true;
									break;
								default:

								}
								
								if($scope.unitCountryId > 0)
								{
								  $scope.patientObj.countryId  = $scope.unitCountryId;
								  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
								  $scope.patientObj.stateId  =  $scope.unitStateId;
								  $scope.patientObj.districtId = $scope.unitDistrictId;
								  $scope.patientObj.cityId = $scope.unitCityId;
								  $scope.patientObj.areaId = $scope.unitAreaId;
								  $scope.patientObj.zipCode = $scope.unitZipCode;
								  $scope.getStateByCountry($scope.patientObj.countryId,'L');
								  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
								  $scope.getDistrictByState($scope.patientObj.stateId,'L');
								  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
								  $scope.getAreaByCity($scope.patientObj.cityId,'L');
								}
								$scope.addressFlag =false;
								$scope.sameAsLocal='Y';

								$scope.patientObj = {

                                											prefixId : '',
                                											firstName : '',
                                											middleName : '',
                                											lastName : '',
                                											genderId : '',
                                											countryCallingCode : $scope.patientObj.countryCallingCode,
                                											email : '',
                                											nationalityId : $scope.patientObj.nationalityId,
                                											raceId : $scope.patientObj.raceId,
                                											maritalStatusId : '',
                                											mobileNumber : '',
                                											dob : '',
                                											identificationTypeId : '',
                                											identificationNumber : '',
                                											isVip : '',
                                											address : '',
                                											countryId : $scope.unitCountryId,
                                											stateId : $scope.unitStateId,
                                											districtId : $scope.unitDistrictId,
                                											cityId :$scope.unitCityId,
                                											areaId : $scope.unitAreaId,
                                											zipCode : $scope.unitZipCode,
                                											phoneCode : '',
                                											phoneNumber : '',
                                											companyName : '',
                                											companyAddress : '',
                                											companyCountryId : '',
                                											companyStateId : '',
                                											companyDistrictId : '',
                                											companyCityId : '',
                                											companyAreaId : '',
                                											companyZipCode : '',
                                											companyMobileNumber : '',
                                											organizationId : $scope.organizationId,
                                											unitId : $scope.unitId,
                                											updatedBy : $scope.userId,
                                											createdBy : $scope.userId,
                                											status : 'A',
                                											patientCategoryId:$scope.defaultPatientCategoryId,
                                											permanentAddress:'',
                                											permanentCountryId:$scope.unitCountryId,
                                											permanentStateId:'',
                                											permanentDistrictId:'',
                                											permanentCityId:'',
                                											permanentAreaId:'',
                                											permanentZipCode:'',
                                											permanentPhoneCode:'',
                                											permanentPhoneNumber:'',
                                											aliseName:'',
                                											identificationExpiryDate:'',
                                											occupationId:'',
                                											registrationTypeId:1,
                                											previousId:'',
                                											patientId:0,
                                											patientDetailsId:'',
                                											uhidNumber:'',
                                											imagePath:'',
                                											thumbImage:'',
                                											whatsappCallingCode : $scope.patientObj.countryCallingCode,
                                											whatsappNo: '',
                                											skypeId:'',
                                											fileSequenceNumber:'',
                                											bloodGroup :'',
                                											area:'',
                                											permanentArea:'',
                                											age:'',
                                										};

							}
							
							 $scope.setPostCode = function(areaId,flag){
									
								 if(flag == 0 ){
									 
									 angular.forEach($scope.areaNoramalList,function(value,key){
											
											if(value.areaId==areaId)
											{
												$scope.patientObj.zipCode = value.postCode;
											}
										});
								 }else{
									 angular.forEach($scope.areaNoramalList,function(value1,key){
											
											if(value1.areaId==areaId)
											{
												$scope.patientObj.permanentZipCode = value1.postCode;
											}
										});
									 
								 }
								 $scope.selectFlag = true;
								 $scope.selectFlag1 =true;
                                 $scope.flgHideShow = false;
                                 $scope.testFlg =false;
                                 setTimeout(function(){
                                      $scope.selectFlag = false;
                                      $scope.selectFlag1 =false;
                                    },500);
									
								}
							
							$scope.selectItem = function(item) {
								if($scope.isUpdatePatientReg==true){
									$scope.updateRegflag=true;
								}else{
									$scope.updateRegflag=false;
								}

								$scope.selectedPatientData = item;
								if($scope.patientObj.registrationTypeId==3 || $scope.patientObj.registrationTypeId==6){
									$scope.isConversion=true;
								}else{
									$scope.isConversion=false;
								}
								$scope.otpvalidated = false;
								$scope.otpvalidatedMobile = false;
								/*if( $scope.patientObj.registrationTypeId==2 && ($scope.searchType==2 || $scope.searchType==3)){
									$scope.isOTCRegistrationHideShow=true;
								}else{
									$scope.isOTCRegistrationHideShow=false;
								}*/
								console.log("yyy",item);
								
								
								if(item.areaId != null && item.areaId !='' && item.areaId != undefined){
									$scope.savedAreaId = item.areaId;
									$scope.savedAreaName = item.areaName;
								}
								//$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.searchSelectPatient) {
									//alert();
									//if()
									
									if(parseInt($scope.searchType) == 2)
										{
										if($scope.patientObj.registrationTypeId == 3)
										{
											$scope.patientObj.isUnknownReg = 'Y';
											$scope.patientObj.firstName = item.firstName;
											$scope.patientObj.mobileNumber = item.mobileNumber;
											$scope.patientObj.registrationTypeId = 4;
											$scope.patientObj.previousId = item.patientId;
											$scope.patientObj.patientId = item.patientId;
											$scope.patientObj.prefixId = item.prefixId;
										}
										if($scope.patientObj.registrationTypeId == 1){
											$scope.patientObj.firstName = item.firstName;
											$scope.patientObj.mobileNumber = item.mobileNumber;
											$scope.patientObj.registrationTypeId = 1;
											if( $scope.employeeSearchObj.typeId == 2){
												$scope.patientObj.empDocId = item.employeeId;
											}
											else
												{
													$scope.patientObj.empDocId = item.doctorId;
												}
											$scope.patientObj.empDepTypeId = $scope.employeeSearchObj.typeId;
											//$scope.patientObj.empDocDepId = 0;
											}
										if($scope.patientObj.registrationTypeId == 2){
											$scope.patientObj.firstName = item.firstName;
											$scope.patientObj.mobileNumber = item.mobileNumber;
											$scope.patientObj.registrationTypeId = 2;
											if( $scope.employeeSearchObj.typeId == 2){
												$scope.patientObj.empDocId = item.employeeId;
											}
											else
												{
													$scope.patientObj.empDocId = item.doctorId;
												}
											$scope.patientObj.empDepTypeId = $scope.employeeSearchObj.typeId;
											//$scope.patientObj.empDocDepId = 0;
											
											}
										}
									else if(parseInt($scope.searchType) == 3)
										{
										if($scope.patientObj.registrationTypeId == 3){
										$scope.patientObj.isOtcReg = 'Y';
										$scope.patientObj.firstName = item.firstName;
										$scope.patientObj.mobileNumber = item.mobileNumber;
										$scope.patientObj.registrationTypeId = 3;
										$scope.patientObj.previousId = item.patientId;
										}
										if($scope.patientObj.registrationTypeId == 1){
											$scope.patientObj.firstName = item.firstName;
											$scope.patientObj.mobileNumber = item.mobileNumber;
											$scope.patientObj.registrationTypeId = 1;
											
													$scope.patientObj.empDocDepId = item.dependentDetailsId;
												
											$scope.patientObj.empDepTypeId = $scope.dependentSearchObj.typeId;
											//$scope.patientObj.empDocDepId = 0;
											}
										if($scope.patientObj.registrationTypeId == 2){
											$scope.patientObj.firstName = item.firstName;
											$scope.patientObj.mobileNumber = item.mobileNumber;
											$scope.patientObj.registrationTypeId = 2;
											
													$scope.patientObj.empDocDepId = item.dependentDetailsId;
												
											$scope.patientObj.empDepTypeId = $scope.dependentSearchObj.typeId;
											//$scope.patientObj.empDocDepId = 0;
											}
										
										if(item.prefixId > 0){
											$scope.patientObj.prefixId = item.prefixId;
									 }
										
										}
									else
										{
											if($scope.patientObj.registrationTypeId == 3){
												$scope.patientObj.patientId = item.patientId;
												$scope.patientObj.isPreReg = 'Y';
												$scope.patientObj.firstName = item.firstName;
												$scope.patientObj.mobileNumber = item.mobileNumber;
												//alert(item.maritalStatusId);
												if(item.maritalStatusId > 0 ){
													
														$scope.patientObj.maritalStatusId = item.maritalStatusId;
													
												}
												if( item.countryCallingCode > 0){
													$scope.patientObj.countryCallingCode = item.countryCallingCode.toString();
													$scope.patientObj.whatsappCallingCode = item.countryCallingCode.toString();
												}
												$scope.patientObj.registrationTypeId = 6;
											}
											else
												{
												
												$scope.patientObj.patientId = item.patientId;
												$scope.patientObj.patientDetailsId = item.patientDetailsId;
												$scope.patientObj.firstName = item.firstName;
												$scope.patientObj.mobileNumber = item.mobileNumber;
												$scope.patientObj.uhidNumber = item.uhidNumber;
												$scope.patientObj.prefixId = (item.prefixId > 0 ? item.prefixId : '');
												}
										}
									
									$scope.patientObj.genderId = item.genderId;
									if(item.patientCategoryId != null)
                                    {
                                    $scope.patientObj.patientCategoryId = item.patientCategoryId;
                                    }else
                                    {
                                    $scope.patientObj.patientCategoryId = $scope.defaultPatientCategoryId;
                                    }									
									//$scope.age = item.ageString;
									//alert(new Date(item.dob));
									$scope.datepicker =  new Date(item.dob);
									$scope.calculateAge($scope.datepicker);
									$scope.patientObj.address = item.address;
									$scope.patientObj.aliseName = item.aliseName;
									$scope.patientObj.fileSequenceNumber = item.fileSequenceNumber;
									$scope.patientObj.bloodGroup = item.bloodGroup;
									if(($scope.patientObj.registrationTypeId != 1 && $scope.searchType != 3) && ($scope.patientObj.registrationTypeId != 6 && $scope.searchType == 3)){
									if(item.countryId != '' && item.countryId != null ){
										$scope.patientObj.countryId = item.countryId;
										
									}
									
									if(item.stateId != '' && item.stateId != null){
										$scope.getStateByCountry(item.countryId,'L');
										$scope.patientObj.stateId = item.stateId;
									}
									
									if(item.districtId != '' && item.districtId != null){
										$scope.getDistrictByState(item.stateId,'L');
										$scope.patientObj.districtId = item.districtId;
									}
									
									if(item.cityId != '' && item.cityId != null)
										$scope.getCityByDistrict(item.districtId,'L');
										$scope.patientObj.cityId = item.cityId;
									
									if(item.areaId != '' && item.areaId != null){
										
										$scope.patientObj.areaId = item.areaId;
										$scope.patientObj.area = item.areaName;
										$scope.patientObj.areaName = item.areaName;
									}
									
									if(item.companyCountryId != null){
										$scope.patientObj.companyCountryId = item.companyCountryId;
									}
									
									if(item.companyStateId != null){
										$scope.getStateByCountry(item.companyCountryId,'C');
										$scope.patientObj.companyStateId = item.companyStateId;
									}
									

									if(item.companyDistrictId != null){
										$scope.getDistrictByState(item.companyStateId,'C');
										$scope.patientObj.companyDistrictId = item.companyDistrictId;
									}
									
									if(item.companyCityId != null){
										$scope.getCityByDistrict(item.companyDistrictId,'C');
										$scope.patientObj.companyCityId = item.companyCityId;
									}
									
									if(item.companyAreaId != null){
										$scope.getAreaByCity(item.companyCityId,'C');
										$scope.patientObj.companyAreaId = item.companyAreaId;
									}
									
									if(item.permanentCountryId != '' && item.permanentCountryId != null){
										$scope.patientObj.permanentCountryId = item.permanentCountryId;
									}
									
									if(item.permanentStateId != '' && item.permanentStateId != null){
										$scope.getStateByCountry(item.permanentCountryId,'LTOP');
										$scope.patientObj.permanentStateId = item.permanentStateId;
									}
									
									if(item.permanentDistrictId != '' && item.permanentDistrictId != null){
										$scope.getDistrictByState(item.permanentStateId,'LTOP');
										$scope.patientObj.permanentDistrictId = item.permanentDistrictId;
									}
									
									if(item.permanentCityId != '' && item.permanentCityId != null){
										$scope.getCityByDistrict(item.permanentDistrictId,'LTOP');
										$scope.patientObj.permanentCityId = item.permanentCityId;
									}
									
									if(item.permanentAreaId != '' && item.permanentAreaId != null){
										$scope.getAreaByCity(item.permanentCityId,'LTOP');
										$scope.patientObj.permanentAreaId = item.permanentAreaId;
									}
									
									if(item.healthId != '' && item.healthId != null){
										$scope.patientObj.healthId = item.healthId;
									}
									
									if(item.healthNumber != '' && item.healthNumber != null){
										$scope.patientObj.healthNumber = item.healthNumber;
									}
									
									if(item.maritalStatusId != '' && item.maritalStatusId != null)
										$scope.patientObj.maritalStatusId = item.maritalStatusId;
									
									if(item.nationalityId != '' && item.nationalityId != null)
										$scope.patientObj.nationalityId = item.nationalityId;
									
									
									if(item.raceId != '' && item.raceId != null)
										$scope.patientObj.raceId = item.raceId;
									
								}
									
							//test
									else if(($scope.patientObj.registrationTypeId == 1 && $scope.searchType != 3))
									 {
										
										if(item.prefixId > 0){
											$scope.patientObj.prefixId = item.prefixId;
									 }
									
										
										if(item.countryId != '' && item.countryId != null){
											$scope.patientObj.countryId = item.countryId;
											
										}
										
										if(item.countryName != '' && item.countryName != null){
											$scope.patientObj.countryName = item.countryName;
										}
										
										if(item.stateId != '' && item.stateId != null){
											$scope.getStateByCountry(item.countryId,'L');
											$scope.patientObj.stateId = item.stateId;
										}
										
										if(item.stateName != '' && item.stateName != null){
											$scope.patientObj.stateName = item.stateName;
										}else{
											$scope.patientObj.stateName='';
										}
										
										if(item.districtId != '' && item.districtId != null){
											$scope.getDistrictByState(item.stateId,'L');
											$scope.patientObj.districtId = item.districtId;
										}
										
										if(item.cityId != '' && item.cityId != null){
											$scope.getCityByDistrict(item.districtId,'L');
											$scope.patientObj.cityId = item.cityId;
										}
										
										if(item.cityName != '' && item.cityName != null){
											$scope.patientObj.cityName = item.cityName;
										}else{
											$scope.patientObj.cityName='';
										}
											
										
										if(item.areaId != '' && item.areaId != null){
											$scope.getAreaByCity(item.cityId,'L');
											$scope.patientObj.areaId = item.areaId;
											$scope.patientObj.area = item.areaName;
											$scope.patientObj.areaName = item.areaName;
										}
										
										if(item.companyCountryId != null){
											$scope.patientObj.companyCountryId = item.companyCountryId;
										}
										
										if(item.companyStateId != null){
											$scope.getStateByCountry(item.companyCountryId,'C');
											$scope.patientObj.companyStateId = item.companyStateId;
										}
										

										if(item.companyDistrictId != null){
											$scope.getDistrictByState(item.companyStateId,'C');
											$scope.patientObj.companyDistrictId = item.companyDistrictId;
										}
										
										if(item.companyCityId != null){
											$scope.getCityByDistrict(item.companyDistrictId,'C');
											$scope.patientObj.companyCityId = item.companyCityId;
										}
										
										if(item.companyAreaId != null){
											$scope.getAreaByCity(item.companyCityId,'C');
											$scope.patientObj.companyAreaId = item.companyAreaId;
										}
										
										if(item.permanentCountryId != '' && item.permanentCountryId != null){
											$scope.patientObj.permanentCountryId = item.permanentCountryId;
										}
										
										if(item.permanentStateId != '' && item.permanentStateId != null){
											$scope.getStateByCountry(item.permanentCountryId,'LTOP');
											$scope.patientObj.permanentStateId = item.permanentStateId;
										}
										
										if(item.permanentDistrictId != '' && item.permanentDistrictId != null){
											$scope.getDistrictByState(item.permanentStateId,'LTOP');
											$scope.patientObj.permanentDistrictId = item.permanentDistrictId;
										}
										
										if(item.permanentCityId != '' && item.permanentCityId != null){
											$scope.getCityByDistrict(item.permanentDistrictId,'LTOP');
											$scope.patientObj.permanentCityId = item.permanentCityId;
										}
										
										if(item.permanentAreaId != '' && item.permanentAreaId != null){
											if(item.permanentCityId != null){
												$scope.getAreaByCity(item.permanentCityId,'LTOP');
												$scope.patientObj.permanentAreaId = item.permanentAreaId;
											}
											
										}
										
										if(item.maritalStatusId != '' && item.maritalStatusId != null)
											$scope.patientObj.maritalStatusId = item.maritalStatusId;
										
										if(item.nationalityId != '' && item.nationalityId != null)
											$scope.patientObj.nationalityId = item.nationalityId;
										
										
										if(item.raceId != '' && item.raceId != null)
											$scope.patientObj.raceId = item.raceId;
										
										if(item.occupationId != '' && item.occupationId != null)
											$scope.patientObj.occupationId = item.occupationId;
										
									}
									else if(($scope.patientObj.registrationTypeId == 3 && $scope.searchType == 3))
									 {
										if(item.countryId != '' && item.countryId != null ){
											$scope.patientObj.countryId = item.countryId;
											
										}
										
										if(item.stateId != '' && item.stateId != null){
											$scope.getStateByCountry(item.countryId,'L');
											$scope.patientObj.stateId = item.stateId;
										}
										
										if(item.districtId != '' && item.districtId != null){
											$scope.getDistrictByState(item.stateId,'L');
											$scope.patientObj.districtId = item.districtId;
										}
										
										if(item.cityId != '' && item.cityId != null)
											$scope.getCityByDistrict(item.districtId,'L');
											$scope.patientObj.cityId = item.cityId;
										
										if(item.areaId != '' && item.areaId != null){
											$scope.getAreaByCity(item.cityId,'L');
											$scope.patientObj.areaId = item.areaId;
											$scope.patientObj.area = item.areaName;
											$scope.patientObj.areaName = item.areaName;
										}
										
										if(item.companyCountryId != null){
											$scope.patientObj.companyCountryId = item.companyCountryId;
										}
										
										if(item.companyStateId != null){
											$scope.getStateByCountry(item.companyCountryId,'C');
											$scope.patientObj.companyStateId = item.companyStateId;
										}
										

										if(item.companyDistrictId != null){
											$scope.getDistrictByState(item.companyStateId,'C');
											$scope.patientObj.companyDistrictId = item.companyDistrictId;
										}
										
										if(item.companyCityId != null){
											$scope.getCityByDistrict(item.companyDistrictId,'C');
											$scope.patientObj.companyCityId = item.companyCityId;
										}
										
										if(item.companyAreaId != null){
											$scope.getAreaByCity(item.companyCityId,'C');
											$scope.patientObj.companyAreaId = item.companyAreaId;
										}
										
										if(item.permanentCountryId != '' && item.permanentCountryId != null){
											$scope.patientObj.permanentCountryId = item.permanentCountryId;
										}
										
										if(item.permanentStateId != '' && item.permanentStateId != null){
											$scope.getStateByCountry(item.permanentCountryId,'LTOP');
											$scope.patientObj.permanentStateId = item.permanentStateId;
										}
										
										if(item.permanentDistrictId != '' && item.permanentDistrictId != null){
											$scope.getDistrictByState(item.permanentStateId,'LTOP');
											$scope.patientObj.permanentDistrictId = item.permanentDistrictId;
										}
										
										if(item.permanentCityId != '' && item.permanentCityId != null){
											$scope.getCityByDistrict(item.permanentDistrictId,'LTOP');
											$scope.patientObj.permanentCityId = item.permanentCityId;
										}
										
										if(item.permanentAreaId != '' && item.permanentAreaId != null){
											$scope.getAreaByCity(item.permanentCityId,'LTOP');
											$scope.patientObj.permanentAreaId = item.permanentAreaId;
										}
										
										if(item.maritalStatusId != '' && item.maritalStatusId != null)
											$scope.patientObj.maritalStatusId = item.maritalStatusId;
										
										if(item.nationalityId != '' && item.nationalityId != null)
											$scope.patientObj.nationalityId = item.nationalityId;
										
										
										if(item.raceId != '' && item.raceId != null)
											$scope.patientObj.raceId = item.raceId;
										
										if(item.occupationId != '' && item.occupationId != null)
										$scope.patientObj.occupationId = item.occupationId;
										
									}
									else if($scope.patientObj.registrationTypeId == 2 && ($scope.searchType == 2 || $scope.searchType == 1) )
									 {
										if(item.countryId != '' && item.countryId != null ){
											$scope.patientObj.countryId = item.countryId;
											
										}
										
										if(item.stateId != '' && item.stateId != null){
											$scope.getStateByCountry(item.countryId,'L');
											$scope.patientObj.stateId = item.stateId;
										}
										
										if(item.districtId != '' && item.districtId != null){
											$scope.getDistrictByState(item.stateId,'L');
											$scope.patientObj.districtId = item.districtId;
										}
										
										if(item.cityId != '' && item.cityId != null)
											$scope.getCityByDistrict(item.districtId,'L');
											$scope.patientObj.cityId = item.cityId;
										
										if(item.areaId != '' && item.areaId != null){
											$scope.getAreaByCity(item.cityId,'L');
											$scope.patientObj.areaId = item.areaId;
											$scope.patientObj.area = item.areaName;
											$scope.patientObj.areaName = item.areaName;
										}
										
										if(item.companyCountryId != null){
											$scope.patientObj.companyCountryId = item.companyCountryId;
										}
										
										if(item.companyStateId != null){
											$scope.getStateByCountry(item.companyCountryId,'C');
											$scope.patientObj.companyStateId = item.companyStateId;
										}
										

										if(item.companyDistrictId != null){
											$scope.getDistrictByState(item.companyStateId,'C');
											$scope.patientObj.companyDistrictId = item.companyDistrictId;
										}
										
										if(item.companyCityId != null){
											$scope.getCityByDistrict(item.companyDistrictId,'C');
											$scope.patientObj.companyCityId = item.companyCityId;
										}
										
										if(item.companyAreaId != null){
											$scope.getAreaByCity(item.companyCityId,'C');
											$scope.patientObj.companyAreaId = item.companyAreaId;
										}
										
										if(item.permanentCountryId != '' && item.permanentCountryId != null){
											$scope.patientObj.permanentCountryId = item.permanentCountryId;
										}
										
										if(item.permanentStateId != '' && item.permanentStateId != null){
											$scope.getStateByCountry(item.permanentCountryId,'LTOP');
											$scope.patientObj.permanentStateId = item.permanentStateId;
										}
										
										if(item.permanentDistrictId != '' && item.permanentDistrictId != null){
											$scope.getDistrictByState(item.permanentStateId,'LTOP');
											$scope.patientObj.permanentDistrictId = item.permanentDistrictId;
										}
										
										if(item.permanentCityId != '' && item.permanentCityId != null){
											$scope.getCityByDistrict(item.permanentDistrictId,'LTOP');
											$scope.patientObj.permanentCityId = item.permanentCityId;
										}
										
										if(item.permanentAreaId != '' && item.permanentAreaId != null){
											$scope.getAreaByCity(item.permanentCityId,'LTOP');
											$scope.patientObj.permanentAreaId = item.permanentAreaId;
										}
										
										if(item.maritalStatusId != '' && item.maritalStatusId != null)
											$scope.patientObj.maritalStatusId = item.maritalStatusId;
										
										if(item.nationalityId != '' && item.nationalityId != null)
											$scope.patientObj.nationalityId = item.nationalityId;
										
										
										if(item.raceId != '' && item.raceId != null)
											$scope.patientObj.raceId = item.raceId;
										
										if(item.occupationId != '' && item.occupationId != null)
										$scope.patientObj.occupationId = item.occupationId;
										
									}
									
									if(($scope.patientObj.registrationTypeId == 6 && $scope.searchType == 1))
									 {
										
										if(item.prefixId != '' && item.prefixId > 0){
											$scope.patientObj.prefixId = item.prefixId;
									 }
									
										if(item.countryId > 0 ){
											$scope.patientObj.countryId = item.countryId;
											
										}
										
										if(item.stateId > 0){
											$scope.getStateByCountry(item.countryId,'L');
											$scope.patientObj.stateId = item.stateId;
										}
										
										if(item.districtId > 0){
											$scope.getDistrictByState(item.stateId,'L');
											$scope.patientObj.districtId = item.districtId;
										}
										
										if(item.cityId > 0){
											$scope.getCityByDistrict(item.districtId,'L');
											$scope.patientObj.cityId = item.cityId;
										}
										if(item.areaId > 0){
											$scope.getAreaByCity(item.cityId,'L');
											$scope.patientObj.areaId = item.areaId;
											$scope.patientObj.area = item.areaName;
										}
										
										if(item.patientCategoryId > 0)
                                        {
                                           $scope.patientObj.patientCategoryId = item.patientCategoryId;
                                        }else
                                            {
                                            $scope.patientObj.patientCategoryId = $scope.defaultPatientCategoryId;
                                            }

										
									}
							//end test
									// alert($scope.patientObj.registrationTypeId+'fff'+ $scope.searchType);
									if($scope.patientObj.registrationTypeId != 4 && $scope.searchType == 2 )
									 {
										if(item.prefixId != '')
											$scope.patientObj.prefixId = item.prefixId;
									 }
									
									
									if(item.genderId != '')
										$scope.patientObj.genderId = item.genderId;
									
									
									if($scope.patientObj.registrationTypeId != 6 && $scope.searchType == 3){
										if(item.identificationExpiryDate != ''){
											$scope.datepickerExpiry = new Date(item.identificationExpiryDate);
											
										}
										}
									
									$scope.patientObj.companyAddress = item.companyAddress;
									$scope.patientObj.companyMobileNumber = item.companyMobileNumber;
									$scope.patientObj.companyName = item.companyName;
									$scope.patientObj.companyZipCode = item.companyZipCode;
									$scope.patientObj.countryCallingCode = item.countryCallingCode;
									$scope.patientObj.email = item.email;
									$scope.patientObj.identificationNumber = item.identificationNumber;
									$scope.patientObj.isVip = item.isVip;
									$scope.patientObj.lastName = item.lastName;
									$scope.patientObj.middleName = item.middleName;
									$scope.patientObj.bloodGroup = item.bloodGroup;
									$scope.patientObj.permanentAddress = item.permanentAddress;
									$scope.patientObj.permanentPhoneCode = item.permanentPhoneCode;
									$scope.patientObj.permanentPhoneNumber = item.permanentPhoneNumber;
									$scope.patientObj.permanentZipCode = item.permanentZipCode;
									$scope.patientObj.phoneCode = item.phoneCode;
									$scope.patientObj.phoneNumber = item.phoneNumber;
									$scope.patientObj.zipCode = item.zipCode;
									$scope.patientObj.healthId = item.healthId;
									$scope.patientObj.healthNumber = item.healthNumber;
									$scope.patientObj.imagePath = item.imagePath;
									$scope.patientObj.whatsappCallingCode =  item.whatsappCallingCode;
									$scope.patientObj.whatsappNo =  item.whatsappNo;
									$scope.patientObj.skypeId =  item.skypeId;
									$scope.patientObj.legacyRegistrationId =  item.legacyRegistrationId;
//									$scope.patientObj.identificationTypeId = item.identificationTypeId+'_'+item.isExpiryRequired;
									if(item.identificationTypeId > 0){
										$scope.patientObj.identificationTypeId = item.identificationTypeId+'_'+item.isExpiryRequired;
										}
										else
											{
											  $scope.patientObj.identificationTypeId = null;
											}
									if(($scope.patientObj.registrationTypeId != 1 && $scope.searchType != 3) && ($scope.patientObj.registrationTypeId != 6 && $scope.searchType == 3)){
									$scope.patientObj.occupationId = item.occupationId;
									}
									if(item.isVip == 'Y'){
									$scope.aliseFlag = true;
									$scope.patientObj.aliseName = item.aliseName;
									}
									
									if(item.isExpiryRequired == 'Y'){
										$scope.expiryDateDisabled = false;
										$scope.datepickerExpiry = new Date(item.identificationExpiryDate);
									}else{
										$scope.expiryDateDisabled = true;
									}
	                                var abc="data:image/png;base64,";
									if( $scope.searchPatientList.bucketFlag == undefined || $scope.searchPatientList.bucketFlag == 1 ){
										$scope.filepreview = item.imagePath;
										   console.log("Sakshiiiiiiiiii bucket pathhh:"+$scope.filepreview);
									}else{
									   $scope.filepreview = abc+item.imagePath;
									   }
									   $scope.getKinDetailsByPatientId();

										
								}
							}
							
							
							
							$scope.cancelPatientImg = function(info){
								
								if(info.patientId > 0){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+"/api/global/patient/deletePatientImage/"+info.patientId+"/"+$scope.organizationId;
										BillingGenericService
												.serviceAction(METHOD_GET,
														URI, "",
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															if (response.data.status == "success"){
																$scope.filepreview = "";
																$scope.patientImage1 = null;
																$scope.patientImage2 = null;
																$scope.patientImage3 = null;
																$scope.snapshotData = "";
																$('#pieChartContainer').html(''); //remove canvas from container
																$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
																$('#pieChartContainer2').html(''); //remove canvas from container
																$('#pieChartContainer2').html('<canvas id="snapshot2" height="217" width="217"></canvas>'); 
																$('#pieChartContainer3').html(''); //remove canvas from container
																$('#pieChartContainer3').html('<canvas id="snapshot3" height="217" width="217"></canvas>'); 
																angular.element("input[type='file']").val(null);
																$rootScope.stopLoader();
															}else{
																$rootScope.stopLoader();
															}
														});
								}else{
									$scope.filepreview = "";
									$scope.snapshotData = "";
									$('#pieChartContainer').html(''); //remove canvas from container
									$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
									$('#pieChartContainer2').html(''); //remove canvas from container
									$('#pieChartContainer2').html('<canvas id="snapshot2" height="217" width="217"></canvas>'); 
									$('#pieChartContainer3').html(''); //remove canvas from container
									$('#pieChartContainer3').html('<canvas id="snapshot3" height="217" width="217"></canvas>'); 
									$scope.patientImage1 = null;
									$scope.patientImage2 = null;
									$scope.patientImage3 = null;
									angular.element("input[type='file']").val(null);
								}
							}


								$scope.clearDatatSD = function(){
                                    $scope.searchPatientList = [];
                                    $scope.searchEmployeeList = [];
                                    $scope.searchDependentList =[];
                                    $scope.patientObj.prefixId = '';
                                    $scope.patientObj.firstName = '';
                                    $scope.patientObj.middleName = '',
                                    $scope.patientObj.lastName = '';
                                    $scope.patientObj.genderId = '';
                                    $scope.patientObj.bloodGroup ='';
                                    $scope.patientObj.countryCallingCode = '';
                                    $scope.patientObj.email = '';
                                    $scope.patientObj.nationalityId = '';
                                    $scope.patientObj.raceId = '';
                                    $scope.patientObj.maritalStatusId = '';
                                    $scope.patientObj.mobileNumber = '';
                                    $scope.patientObj.dob = '';
                                    $scope.patientObj.identificationTypeId = '';
                                    $scope.patientObj.identificationNumber = '';
                                    $scope.patientObj.isVip = '';
                                    $scope.patientObj.address = '';
                                    $scope.patientObj.countryId = '';
                                    $scope.patientObj.stateId = '';
                                    $scope.patientObj.districtId = '';
                                    $scope.patientObj.cityId = '';
                                    $scope.patientObj.areaId = '';
                                    $scope.patientObj.zipCode = '';
                                    $scope.patientObj.phoneCode = '';
                                    $scope.patientObj.phoneNumber = '';
                                    $scope.patientObj.companyName = '';
                                    $scope.patientObj.companyAddress = '';
                                    $scope.patientObj.companyCountryId = '';
                                    $scope.patientObj.companyStateId = '';
                                    $scope.patientObj.companyDistrictId = '';
                                    $scope.patientObj.companyCityId = '';
                                    $scope.patientObj.companyAreaId = '';
                                    $scope.patientObj.companyZipCode = '';
                                    $scope.patientObj.companyMobileNumber = '';
                                    $scope.patientObj.bucketFlag = '';
                                    $scope.patientObj.organizationId = $scope.organizationId;
                                    $scope.patientObj.unitId = $scope.unitId;
                                    $scope.patientObj.updatedBy = $scope.userId;
                                    $scope.patientObj.createdBy = $scope.userId;
                                    $scope.patientObj.status = 'A';
                                    $scope.patientObj.patientCategoryId=$scope.defaultPatientCategoryId;
                                    $scope.patientObj.permanentAddress='';
                                    $scope.patientObj.permanentCountryId='';
                                    $scope.patientObj.permanentStateId='';
                                    $scope.patientObj.permanentDistrictId='';
                                    $scope.patientObj.permanentCityId='';
                                    $scope.patientObj.permanentAreaId='';
                                    $scope.patientObj.permanentZipCode='';
                                    $scope.patientObj.permanentPhoneCode='';
                                    $scope.patientObj.permanentPhoneNumber='';
                                    $scope.patientObj.aliseName='';
                                    $scope.patientObj.identificationExpiryDate='';
                                    $scope.patientObj.occupationId='';
                                    $scope.patientObj.previousId='';
                                    $scope.patientObj.patientId=0;
                                    $scope.patientObj.patientDetailsId='';
                                    $scope.patientObj.imagePath='';
                                    $scope.patientObj.whatsappCallingCode='';
                                    $scope.patientObj.whatsappNo='';
                                    $scope.patientObj.skypeId='';
                                    $scope.patientObj.datepickerExpiry='';
                                    $scope.patientObj.datepicker='';
                                    $scope.patientObj.fileSequenceNumber='';
                                    $scope.datepicker="";
                                    $scope.ageDay = 0;
                                    $scope.ageMonth = 0;
                                    $scope.ageYear = 0;
                                    $scope.datepicker="";
                                    $scope.datepickerExpiry="";
                                    $scope.expiryDateDisabled=true;
                                    $scope.aliseFlag = false;
                                     $scope.filepreview = "";
                                    angular.element('#datepickerExpiry').scope().datepickerExpiry = "";
                                    angular.element('#datepicker').scope().datepicker = "";

                                    $('#pieChartContainer').html(''); //remove canvas from container
                                     $('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>');
                                     $scope.patientImage1 = null;
                                    angular.element("input[type='file']").val(null);

                                    //OTC Registration
//                                    angular.element('#datepickerExpiry').scope().datepickerExpiry = null;
                                    $('#pieChartContainer2').html(''); //remove canvas from container
                                     $('#pieChartContainer2').html('<canvas id="snapshot1" height="217" width="217"></canvas>');
                                     $scope.patientImage2 = null;
                                    angular.element("input[type='file']").val(null);

                                  //next of kin

                                   // $scope.clearFields();
                            	}
							
							/******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
					        	/*First Level*/
						    	$scope.firstTabLevel = 1 ;
						    	$scope.checkCurrentTab = "";
						    	$scope.newobj={
						    			datepickerSearch :''
						    	};
						    	$scope.cleartirdtab= function(newTab)
								{
						    		$scope.newobj.datepickerSearch = undefined
									$scope.newobj.datepickerSearch = '';
						    		$scope.newobj.datepickerSearch = null;
								}
								$scope.setFirstTabLevel= function(newTab)
								{
									
									$scope.addressFlag =false;
									$scope.sameAsLocal ='Y';
									$scope.filepreview = "";
									$scope.datepickerSearch = undefined
									$scope.datepickerSearch = '';
									$scope.newobj.datepickerSearch ='';
									$scope.checkCurrentTab = newTab;
									//to blank patient data
									$scope.patientObj = {

											prefixId : '',
											firstName : '',
											middleName : '',
											lastName : '',
											genderId : '',
											countryCallingCode : $scope.patientObj.countryCallingCode,
											email : '',
											nationalityId : $scope.patientObj.nationalityId,
											raceId : $scope.patientObj.raceId,
											maritalStatusId : '',
											mobileNumber : '',
											dob : '',
											identificationTypeId : '',
											identificationNumber : '',
											isVip : '',
											address : '',
											countryId : $scope.unitCountryId,
											stateId : $scope.unitStateId,
											districtId : $scope.unitDistrictId,
											cityId :$scope.unitCityId,
											areaId : $scope.unitAreaId,
											zipCode : $scope.unitZipCode,
											phoneCode : '',
											phoneNumber : '',
											companyName : '',
											companyAddress : '',
											companyCountryId : '',
											companyStateId : '',
											companyDistrictId : '',
											companyCityId : '',
											companyAreaId : '',
											companyZipCode : '',
											companyMobileNumber : '',
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											updatedBy : $scope.userId,
											createdBy : $scope.userId,
											status : 'A',
											patientCategoryId:$scope.defaultPatientCategoryId,
											permanentAddress:'',
											permanentCountryId:$scope.unitCountryId,
											permanentStateId:'',
											permanentDistrictId:'',
											permanentCityId:'',
											permanentAreaId:'',
											permanentZipCode:'',
											permanentPhoneCode:'',
											permanentPhoneNumber:'',
											aliseName:'',
											identificationExpiryDate:'',
											occupationId:'',
											registrationTypeId:1,
											previousId:'',
											patientId:0,
											patientDetailsId:'',
											uhidNumber:'',
											imagePath:'',
											thumbImage:'',
											whatsappCallingCode : $scope.patientObj.countryCallingCode,
											whatsappNo: '',
											skypeId:'',
											fileSequenceNumber:'',
											bloodGroup:'',
											area:'',
											permanentArea:'',
											age:'',
										};
									
									
									//$scope.patientObj.registrationTypeId = 1;
									//alert($scope.patientObj.registrationTypeId);
									$scope.initSearchObj();
									$scope.searchPatientList = [];
									$scope.searchEmployeeList = [];
									$scope.searchDependentList =[];
									if (newTab == 1) {
									$("#case-sheet").css("background-color",
											"#5F9EA0");
									$("#complaints").css("background-color",
											"#5ab7c8");
									$("#History").css("background-color",
											"#5ab7c8");
									$scope.patientObj.registrationTypeId = 1;
									$scope.searchType = 1;
									//alert($scope.searchType);
									//$(".PatientOTCRadio").click();
								} else if (newTab == 2) {
									$("#case-sheet").css("background-color",
											"#5ab7c8");
									$("#complaints").css("background-color",
											"#5F9EA0");
									$("#History").css("background-color",
											"#5ab7c8");
									$scope.patientObj.registrationTypeId = 2;
									$scope.searchType = 1;
									$scope.isOTCRegistrationHideShow=true;
									//$(".PatientOTCRadio").click();
								} else {
									$("#case-sheet").css("background-color",
											"#5ab7c8");
									$("#complaints").css("background-color",
											"#5ab7c8");
									$("#History").css("background-color",
											"#5F9EA0");
									$scope.patientObj.registrationTypeId = 3;
									$scope.searchType = 1;
									
									 $scope.patientDetailsObjAd=$stateParams.patientRegistrationObjForAdmission;
										if ($scope.patientDetailsObjAd.patientId != undefined) {
											if($scope.patientDetailsObjAd.registrationTypeId == 4){
												$scope.searchType = 2;
											}
										}
									
									//$(".preRegRadio").click();
								}
									$scope.firstTabLevel = newTab;
								};
								
								$scope.isSetFirstTabLevel = function(tabNum)
								{
									
									return $scope.firstTabLevel === tabNum;
								};
								
								$scope.searchTypeClickFun = function()
								{
									//alert('j');
									$scope.initSearchObj();
									$scope.searchPatientList = []; 
									$scope.searchEmployeeList = []; 
									$scope.searchDependentList =[];
									/*if($scope.registrationFieldObj.email == "Y"){
										if($scope.emailValidationRequired == 'Y'){
											$scope.otpvalidated = true;
										}else{
											$scope.otpvalidated = false;
										}
									}else{
										$scope.otpvalidated = false;
									}*/
									$scope.patientObj.prefixId = '';
									$scope.patientObj.firstName = '';
									$scope.patientObj.middleName = '',
									$scope.patientObj.lastName = '';
									$scope.patientObj.genderId = '';
									$scope.patientObj.bloodGroup = '';
									$scope.patientObj.countryCallingCode = '';
									$scope.patientObj.email = '';
									$scope.patientObj.nationalityId = '';
									$scope.patientObj.raceId = '';
									$scope.patientObj.maritalStatusId = '';
									$scope.patientObj.mobileNumber = '';
									$scope.patientObj.dob = '';
									$scope.patientObj.identificationTypeId = '';
									$scope.patientObj.identificationNumber = '';
									$scope.patientObj.isVip = '';
									$scope.patientObj.address = '';
									$scope.patientObj.countryId = '';
									$scope.patientObj.stateId = '';
									$scope.patientObj.districtId = '';
									$scope.patientObj.cityId = '';
									$scope.patientObj.areaId = '';
									$scope.patientObj.zipCode = '';
									$scope.patientObj.phoneCode = '';
									$scope.patientObj.phoneNumber = '';
									$scope.patientObj.companyName = '';
									$scope.patientObj.companyAddress = '';
									$scope.patientObj.companyCountryId = '';
									$scope.patientObj.companyStateId = '';
									$scope.patientObj.companyDistrictId = '';
									$scope.patientObj.companyCityId = '';
									$scope.patientObj.companyAreaId = '';
									$scope.patientObj.companyZipCode = '';
									$scope.patientObj.companyMobileNumber = '';
									$scope.patientObj.bucketFlag = '';
									$scope.patientObj.organizationId = $scope.organizationId;
									$scope.patientObj.unitId = $scope.unitId;
									$scope.patientObj.updatedBy = $scope.userId;
									$scope.patientObj.createdBy = $scope.userId;
									$scope.patientObj.status = 'A';
									$scope.patientObj.patientCategoryId=$scope.defaultPatientCategoryId;
									$scope.patientObj.permanentAddress='';
									$scope.patientObj.permanentCountryId='';
									$scope.patientObj.permanentStateId='';
									$scope.patientObj.permanentDistrictId='';
									$scope.patientObj.permanentCityId='';
									$scope.patientObj.permanentAreaId='';
									$scope.patientObj.permanentZipCode='';
									$scope.patientObj.permanentPhoneCode='';
									$scope.patientObj.permanentPhoneNumber='';
									$scope.patientObj.aliseName='';
									$scope.patientObj.identificationExpiryDate='';
									$scope.patientObj.occupationId='';
									$scope.patientObj.previousId='';
									$scope.patientObj.patientId=0;
									$scope.patientObj.patientDetailsId='';
									$scope.patientObj.imagePath='';
									$scope.patientObj.whatsappCallingCode='';
									$scope.patientObj.whatsappNo='';
									$scope.patientObj.skypeId='';


									if( $scope.patientObj.registrationTypeId==2 && ($scope.searchType==2 || $scope.searchType==3)){
										$scope.isOTCRegistrationHideShow=false;
									}else{
										$scope.isOTCRegistrationHideShow=true;
									}

									if($scope.unitCountryId > 0)
									{
									 /* $scope.patientObj.countryId  = $scope.unitCountryId;
									  $scope.patientObj.stateId  =  $scope.unitStateId;
									  $scope.patientObj.districtId = $scope.unitDistrictId;
									  $scope.patientObj.cityId = $scope.unitCityId;
									  $scope.patientObj.areaId = $scope.unitAreaId;
									  $scope.getStateByCountry($scope.patientObj.countryId,'L');
									  $scope.getDistrictByState($scope.patientObj.stateId,'L');
									  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
									  $scope.getAreaByCity($scope.patientObj.cityId,'L');*/
									  
									  
									  $scope.patientObj.countryId  = $scope.unitCountryId;
									  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
									  $scope.patientObj.stateId  =  $scope.unitStateId;
									  $scope.patientObj.districtId = $scope.unitDistrictId;
									  $scope.patientObj.cityId = $scope.unitCityId;
									  $scope.patientObj.areaId = $scope.unitAreaId;
									  $scope.patientObj.zipCode = $scope.unitZipCode;
									  $scope.getStateByCountry($scope.patientObj.countryId,'L');
									  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
									  $scope.getDistrictByState($scope.patientObj.stateId,'L');
									  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
									  $scope.getAreaByCity($scope.patientObj.cityId,'L');
									}
									$scope.addressFlag=false;
									$scope.sameAsLocal='Y';
								}
								
								/*Second Level
								$scope.secondTabLevel = 1 ;
								
								$scope.setSecondTabLevel = function(newTab)
								{
								
									$scope.secondTabLevel = newTab;
								};
								
								$scope.isSetSecondTabLevel = function(tabNum)
								{
									return $scope.secondTabLevel === tabNum;
								};
								
								
								Third Level
								$scope.thirdTabLevel = 1 ;
								
								$scope.setThirdTabLevel = function(newTab)
								{
									
									$scope.thirdTabLevel = newTab;
								};
								
								$scope.isSetThirdTabLevel = function(tabNum)
								{
									return $scope.thirdTabLevel === tabNum;
								};
								
								*/
								
								//FORTH Level
								
								
								$scope.getStateByCountryAbdm= function(id,type,obj) {
									$scope.startLoader();
									var data = {
										countryId : id
									};								
									
									var dataorg = {
											countryId : id,
											organizationId : $scope.organizationId
										};	
									
									$scope.getCallingCode(dataorg,type);
								
									$scope.getNationalityByContryId(data);
									var URI = BASE_URL + ROOT_URL
											+ GETSTATELISTBYCOUNTRYID;
									BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												//$scope.stateList = [];
												if (response.data.status == "success"){
														$scope.stateList = [];
														$scope.stateList = response.data.listObject;
														if(obj.stateName != undefined && obj.stateName!= ""){
															
															var found = $scope.stateList.find(ar =>ar.stateName.toLowerCase() === obj.stateName.toLowerCase()); 
														  if (found == undefined) {
															  $scope.newArr = {};
															  $scope.newArr.stateId =0;
															  $scope.newArr.stateName = obj.stateName;
															  $scope.stateList.push($scope.newArr);
															  $scope.patientObj.stateId =0;
															  $scope.patientObj.stateName = obj.stateName;
															  $scope.patientObj.stateCode =obj.stateCode;
														  }else{
															  $scope.patientObj.stateName = found.stateName;
															  $scope.patientObj.stateId = found.stateId;
															  $scope.patientObj.stateCode =obj.stateCode;
														  }
														  $scope.patientObj.cityId =0;
															var dataDist = {
																	stateId : $scope.patientObj.stateId
																};
																var URI = BASE_URL + ROOT_URL
																		+ GETDISTRICTLISTBYSTATEID;
																BillingGenericService
																.serviceAction(METHOD_POST, URI, dataDist,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {
																			
																			if (response.data.status == "success")
																			{
																			
																					$scope.districtList = [];
																					$scope.districtList = response.data.listObject;
																					
																					var found = $scope.districtList.find(ar =>ar.districtName.toLowerCase() === obj.districtName.toLowerCase()); 
																					  if (found == undefined) {
																						  $scope.newArr = {};
																						  $scope.newArr.districtId =0;
																						  $scope.newArr.districtName = obj.districtName;
																						  $scope.newArr.districtCode = obj.districtCode;
																						  $scope.districtList.push($scope.newArr);
																						  $scope.patientObj.districtId =0;
																						  $scope.patientObj.districtName =obj.districtName;
																						  $scope.patientObj.districtCode =obj.districtCode;
																					  }else{
																						  $scope.patientObj.districtId =found.districtId;
																						  $scope.patientObj.districtName =found.districtName;
																						  $scope.patientObj.districtCode =found.districtCode;
																					  }
																					
																				}
																	
																		});

														}
														
												}
												$scope.stopLoader();
											});

								};
								$scope.forthTabLevel = 1 ;
								
								$scope.setForthTabLevel = function(newTab)
								{
									$scope.forthTabLevel = newTab;
								};
								
								$scope.isSetForthTabLevel = function(tabNum)
								{
									return $scope.forthTabLevel === tabNum;
								};
								
								 console.log("PatientObjFORAppointment", $stateParams.patientRegistrationObj);
								 console.log("patientRegistrationObjAbdm", $stateParams.patientRegistrationObjAbdm);
								 $scope.patientDetailsObj=$stateParams.patientRegistrationObj;
								 if (!angular.equals({},$stateParams.patientRegistrationObjAbdm)) {

									//  $scope.patientObj.countryId  = "";
									  $scope.patientObj.permanentCountryId  = "";
									  $scope.patientObj.stateId  = "";
									  $scope.patientObj.districtId = "";
									  $scope.patientObj.cityId = "";
									  $scope.patientObj.areaId = "";
									  $scope.patientObj.zipCode= "";

								      $scope.getStateByCountryAbdm($scope.patientObj.countryId,'L',$stateParams.patientRegistrationObjAbdm);
									//  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
									//  $scope.getDistrictByState($scope.patientObj.stateId,'L');
									//  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
									//  $scope.getAreaByCity($scope.patientObj.cityId,'L');
									  
									 var genderId = 1;
									 if($stateParams.patientRegistrationObjAbdm.gender =="M")
										{
											genderId = 1;
											var found = $stateParams.patientRegistrationObjAbdm.prefixList.find(ar=>ar.id == genderId && ar.prefixDesc =="Mr.");
											var prefixIdTest = found.prefixId;
										}else if($stateParams.patientRegistrationObjAbdm.gender =="F")
										{
											genderId = 2;
											var found = $stateParams.patientRegistrationObjAbdm.prefixList.find(ar=>ar.id == genderId && ar.prefixDesc =="Mrs.");
											var prefixIdTest = found.prefixId;
										}
										
									 $scope.patientDetailsObj.address=$stateParams.patientRegistrationObjAbdm.address;
									 $scope.patientDetailsObj.dob=$stateParams.patientRegistrationObjAbdm.monthOfBirth+"/"+$stateParams.patientRegistrationObjAbdm.dayOfBirth+"/"+$stateParams.patientRegistrationObjAbdm.yearOfBirth;
									 $scope.patientDetailsObj.districtName=$stateParams.patientRegistrationObjAbdm.districtName;
									 $scope.patientDetailsObj.firstName=$stateParams.patientRegistrationObjAbdm.firstName;
									 $scope.patientDetailsObj.genderId=genderId;
									 $scope.patientDetailsObj.healthId=$stateParams.patientRegistrationObjAbdm.preferredAbhaAddress;
									 $scope.patientDetailsObj.healthNumber=$stateParams.patientRegistrationObjAbdm.ABHANumber;
									 $scope.patientObj.uhidNumber=$scope.patientObj.uhidNumber;
									 $scope.patientDetailsObj.lastName=$stateParams.patientRegistrationObjAbdm.lastName;
									 $scope.patientDetailsObj.middleName=$stateParams.patientRegistrationObjAbdm.middleName;
									 $scope.patientDetailsObj.mobileNumber=$stateParams.patientRegistrationObjAbdm.mobile;
									 $scope.patientDetailsObj.pinCode=$stateParams.patientRegistrationObjAbdm.pinCode != undefined ? $stateParams.patientRegistrationObjAbdm.pinCode : $stateParams.patientRegistrationObjAbdm.pincode ;;
									 $scope.snapshotData="data:image/png;base64,"+$stateParams.patientRegistrationObjAbdm.profilePhoto;
									 $scope.patientDetailsObj.stateName=$stateParams.patientRegistrationObjAbdm.stateName;
									      $scope.patientObj.zipCode=$stateParams.patientRegistrationObjAbdm.pinCode != undefined ? $stateParams.patientRegistrationObjAbdm.pinCode : $stateParams.patientRegistrationObjAbdm.pincode ;;
									      $scope.patientObj.mobileNumber =$scope.patientDetailsObj.mobileNumber;
									      $scope.patientObj.healthId =$scope.patientDetailsObj.healthId;
									      $scope.patientObj.healthNumber =$scope.patientDetailsObj.healthNumber;
									      $scope.patientObj.uhidNumber=$scope.patientDetailsObj.uhidNumber;
										  $scope.patientObj.registrationTypeId = 1;
											$scope.searchType = 1;
											$scope.patientObj.prefixId = ($scope.patientDetailsObj.prefixId != null ? $scope.patientDetailsObj.prefixId : '');
											$scope.patientObj.firstName = $scope.patientDetailsObj.firstName;
											$scope.patientObj.middleName = $scope.patientDetailsObj.middleName;
											$scope.patientObj.lastName = $scope.patientDetailsObj.lastName;
											$scope.patientObj.patientCategoryId = $scope.patientDetailsObj.patientCategoryId!=undefined ? $scope.patientDetailsObj.patientCategoryId : $scope.patientObj.patientCategoryId
											$scope.datepicker = new Date($scope.patientDetailsObj.dob);
											$scope.calculateAge($scope.datepicker);
											$scope.patientObj.uuid=$scope.patientDetailsObj.uuid;
											$scope.patientObj.address = $scope.patientDetailsObj.address;
//											$scope.patientObj.email = "info@axonic.com";
											$scope.patientObj.prefixId =prefixIdTest;
											$scope.patientObj.genderId = $scope.patientDetailsObj.genderId;
											$scope.patientSearchObj.healthNumber =$scope.patientObj.healthNumber;
											$scope.patientSearchObj.patientName = $scope.patientObj.firstName+" "+$scope.patientObj.lastName;
										    $scope.patientSearchObj.offset=0;
										    $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
										    $scope.patientSearchObj.registrationTypeId=1;
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATION;
												$rootScope.startLoader();
												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success"){
																		if(response.data.listObject.length > 0){
																			$scope.patientObj.patientId = response.data.listObject[0].patientId;
																			$scope.patientObj.uhidNumber = response.data.listObject[0].uhidNumber;
																			$scope.patientSearchObj = {
																					"uhidNumber"      : "",
																					"patientName"     : "",
																					"genderId"        : "",
																					"mobileNumber"        : "",
																					"dob"       : "",
																					"identificationNumber":"",
																					"identificationTypeId":"",
																					"unitId"          :$scope.unitId,
																					"organizationId"  :$scope.organizationId,
																					
																			}
																		}
																	}
																});
								 }
								
								if ($scope.patientDetailsObj.patientId != undefined || $scope.patientDetailsObj.patientId == 0) {  
									$scope.datepickerExpiry = new Date(moment(new Date()).format('MM/DD/YYYY'));
									if($scope.patientDetailsObj.patientId > 0)
									{
										$scope.patientObj.registrationTypeId = 3;
										$scope.searchType = 1;
										//$scope.firstTabLevel = '1';
										$scope.patientObj.prefixId = ($scope.patientDetailsObj.prefixId != null ? $scope.patientDetailsObj.prefixId : '');
										$scope.patientObj.firstName = $scope.patientDetailsObj.firstName;
										$scope.patientObj.middleName = $scope.patientDetailsObj.middleName;
										$scope.patientObj.lastName = $scope.patientDetailsObj.lastName;
										$scope.patientObj.patientCategoryId = $scope.patientDetailsObj.patientCategoryId!=undefined ? $scope.patientDetailsObj.patientCategoryId : $scope.patientObj.patientCategoryId
										$scope.datepicker = new Date($scope.patientDetailsObj.dob);
										$scope.patientObj.uuid=$scope.patientDetailsObj.uuid;
										$scope.patientObj.address = $scope.patientDetailsObj.address;
										
										
										
										
										if($scope.patientDetailsObj.countryId > 0){
											$scope.getStateByCountry($scope.patientDetailsObj.countryId,'L');
											 $scope.patientObj.countryId = $scope.patientDetailsObj.countryId;
										}
										else{
											$scope.patientDetailsObj.countryId  = $scope.unitCountryId;
											  $scope.patientDetailsObj.stateId  =  $scope.unitStateId;
											  $scope.patientDetailsObj.districtId = $scope.unitDistrictId;
											  $scope.patientDetailsObj.cityId = $scope.unitCityId;
											  $scope.patientDetailsObj.areaId = $scope.unitAreaId;
											  $scope.patientDetailsObj.zipCode = $scope.unitZipCode;
											  $scope.getStateByCountry($scope.patientDetailsObj.countryId,'L');
											  $scope.getDistrictByState($scope.patientDetailsObj.stateId,'L');
											  $scope.getCityByDistrict($scope.patientDetailsObj.districtId,'L');
											  $scope.getAreaByCity($scope.patientDetailsObj.cityId,'L');
										}
										if($scope.patientDetailsObj.stateId > 0){
											$scope.getDistrictByState($scope.patientDetailsObj.stateId,'L');
											$scope.patientObj.stateId = $scope.patientDetailsObj.stateId;
										}
										if($scope.patientDetailsObj.districtId > 0){
											$scope.getCityByDistrict($scope.patientDetailsObj.districtId,'L');
											$scope.patientObj.districtId =$scope.patientDetailsObj.districtId;
										}
										if($scope.patientDetailsObj.cityId > 0){
											$scope.getAreaByCity($scope.patientDetailsObj.cityId,'L');
											$scope.patientObj.cityId = $scope.patientDetailsObj.cityId;
											$scope.patientObj.areaId = $scope.patientDetailsObj.areaId;
										}
										if($scope.patientDetailsObj.maritalStatusId > 0){
											 $scope.patientObj.maritalStatusId = $scope.patientDetailsObj.maritalStatusId;
										}
										$scope.patientObj.zipCode =$scope.patientDetailsObj.zipCode;
										//alert($scope.datepicker);
										//$scope.calculateAge();
										var dt = new Date(moment($scope.datepicker).format('MM/DD/YYYY'));
										//$scope.patientDetailsObj.ageString = $rootScope.getAge(dt);
										$scope.calculateAge(dt);
										$scope.patientObj.genderId = $scope.patientDetailsObj.genderId;
										$scope.patientObj.mobileNumber = $scope.patientDetailsObj.mobileNumber;
										$scope.patientObj.email = $scope.patientDetailsObj.email;
										$scope.patientObj.patientId = $scope.patientDetailsObj.patientId;
										//ageString

										if($scope.patientDetailsObj.fromAdmission != undefined && $scope.patientDetailsObj.fromAdmission == true){
                                                                                $scope.isPendingRequest = 'Y';
                                                                                  $scope.isRedirect = 'N';
                                                                                                 }else{
                                                                                   $scope.isRedirect = 'Y';
                                                                                    }
										$scope.setFirstTabLevel(3);
										$scope.isSetFirstTabLevel(3);
										$scope.selectItem($scope.patientDetailsObj);
										$scope.datepickerExpiry = new Date(moment(new Date()).format('MM/DD/YYYY'));
										angular.element(document.querySelector("#nor")).removeClass("active");
								}
								else
									{
									   $scope.patientObj.registrationTypeId =1;
									   $scope.searchType = 1;
									   
									   $scope.isRedirect = 'Y'; 
									   $scope.setFirstTabLevel(1);
									   $scope.isSetFirstTabLevel(1);
									   
									   if($scope.patientDetailsObj.countryId > 0){
											$scope.getStateByCountry($scope.patientDetailsObj.countryId,'L');
											 $scope.patientObj.countryId = $scope.patientDetailsObj.countryId;
										}
										else{
											$scope.patientObj.countryId  = $scope.unitCountryId;
											  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
											  $scope.patientObj.stateId  =  $scope.unitStateId;
											  $scope.patientObj.districtId = $scope.unitDistrictId;
											  $scope.patientObj.cityId = $scope.unitCityId;
											  $scope.patientObj.areaId = $scope.unitAreaId;
											  $scope.patientObj.zipCode = $scope.unitZipCode;
											  $scope.getStateByCountry($scope.patientObj.countryId,'L');
											  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
											  $scope.getDistrictByState($scope.patientObj.stateId,'L');
											  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
											  $scope.getAreaByCity($scope.patientObj.cityId,'L');
										}
									   $scope.datepickerExpiry = new Date(moment(new Date()).format('MM/DD/YYYY'));
									   angular.element(document.querySelector("#nor")).removeClass("active");
									
									}
										
								}
								else{
								 console.log("PatientObjFORADMISSION", $stateParams.patientRegistrationObjForAdmission);
								 $scope.patientDetailsObj=$stateParams.patientRegistrationObjForAdmission;
								 if (!angular.equals({},$stateParams.patientRegistrationObjAbdm)) {
									 

									  //$scope.patientObj.countryId  = "";
									  $scope.patientObj.permanentCountryId  = "";
									  $scope.patientObj.stateId  = "";
									  $scope.patientObj.districtId = "";
									  $scope.patientObj.cityId = "";
									  $scope.patientObj.areaId = "";
									  $scope.patientObj.zipCode= "";

									   $scope.getStateByCountryAbdm($scope.patientObj.countryId,'L',$stateParams.patientRegistrationObjAbdm);
									//  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
									//  $scope.getDistrictByState($scope.patientObj.stateId,'L');
									//  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
									//  $scope.getAreaByCity($scope.patientObj.cityId,'L');
									  
									 var genderId = 1;
									 if($stateParams.patientRegistrationObjAbdm.gender =="M")
										{
											genderId = 1;
											var found = $stateParams.patientRegistrationObjAbdm.prefixList.find(ar=>ar.id == genderId && ar.prefixDesc =="Mr.");
											var prefixIdTest = found.prefixId;
										}else if($stateParams.patientRegistrationObjAbdm.gender =="F")
										{
											genderId = 2;
											var found = $stateParams.patientRegistrationObjAbdm.prefixList.find(ar=>ar.id == genderId && ar.prefixDesc =="Mrs.");
											var prefixIdTest = found.prefixId;
										}
										
									 $scope.patientDetailsObj.address=$stateParams.patientRegistrationObjAbdm.address;
									 $scope.patientDetailsObj.dob=$stateParams.patientRegistrationObjAbdm.monthOfBirth+"/"+$stateParams.patientRegistrationObjAbdm.dayOfBirth+"/"+$stateParams.patientRegistrationObjAbdm.yearOfBirth;
									 $scope.patientDetailsObj.districtName=$stateParams.patientRegistrationObjAbdm.districtName;
									 $scope.patientDetailsObj.firstName=$stateParams.patientRegistrationObjAbdm.firstName;
									 $scope.patientDetailsObj.genderId=genderId;
									 $scope.patientDetailsObj.healthId=$stateParams.patientRegistrationObjAbdm.preferredAbhaAddress;
									 $scope.patientDetailsObj.healthNumber=$stateParams.patientRegistrationObjAbdm.ABHANumber;
									 $scope.patientDetailsObj.lastName=$stateParams.patientRegistrationObjAbdm.lastName;
									 $scope.patientDetailsObj.middleName=$stateParams.patientRegistrationObjAbdm.middleName;
									 $scope.patientDetailsObj.mobileNumber=$stateParams.patientRegistrationObjAbdm.mobile;
									 $scope.patientDetailsObj.pinCode=$stateParams.patientRegistrationObjAbdm.pinCode != undefined ? $stateParams.patientRegistrationObjAbdm.pinCode : $stateParams.patientRegistrationObjAbdm.pincode ;
									 $scope.snapshotData="data:image/png;base64,"+$stateParams.patientRegistrationObjAbdm.profilePhoto;
									 $scope.patientDetailsObj.stateName=$stateParams.patientRegistrationObjAbdm.stateName;
									 $scope.patientObj.zipCode=$stateParams.patientRegistrationObjAbdm.pinCode != undefined ? $stateParams.patientRegistrationObjAbdm.pinCode : $stateParams.patientRegistrationObjAbdm.pincode ;
									 $scope.patientObj.mobileNumber =$scope.patientDetailsObj.mobileNumber;
								      $scope.patientObj.healthId =$scope.patientDetailsObj.healthId;
								      $scope.patientObj.healthNumber =$scope.patientDetailsObj.healthNumber;
									
										  $scope.patientObj.registrationTypeId = 1;
											$scope.searchType = 1;
											$scope.patientObj.prefixId = ($scope.patientDetailsObj.prefixId != null ? $scope.patientDetailsObj.prefixId : '');
											$scope.patientObj.firstName = $scope.patientDetailsObj.firstName;
											$scope.patientObj.middleName = $scope.patientDetailsObj.middleName;
											$scope.patientObj.lastName = $scope.patientDetailsObj.lastName;
											$scope.datepicker = new Date($scope.patientDetailsObj.dob);
											$scope.calculateAge($scope.datepicker);
											$scope.patientObj.uuid=$scope.patientDetailsObj.uuid;
											$scope.patientObj.address = $scope.patientDetailsObj.address;
//											$scope.patientObj.email = "info@axonic.com";
											$scope.patientObj.prefixId =prefixIdTest;
											$scope.patientObj.genderId = $scope.patientDetailsObj.genderId;
											$scope.patientSearchObj.healthNumber =$scope.patientObj.healthNumber;
											$scope.patientSearchObj.patientName =$scope.patientObj.firstName+" "+$scope.patientObj.lastName;
											$scope.patientSearchObj.offset=0;
											$scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
											$scope.patientSearchObj.registrationTypeId=1;
											
												var URI = BASE_URL + ROOT_URL
														+ GETTEMPORYPATIENTFORPREREGISTRATION;
												$rootScope.startLoader();
												BillingGenericService
														.serviceAction(METHOD_POST,
																URI, $scope.patientSearchObj,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	if (response.data.status == "success"){
																		if(response.data.listObject.length > 0){
																			$scope.patientObj.patientId = response.data.listObject[0].patientId;
																			$scope.patientObj.uhidNumber = response.data.listObject[0].uhidNumber;
																			$scope.patientSearchObj = {
																					"uhidNumber"      : "",
																					"patientName"     : "",
																					"genderId"        : "",
																					"mobileNumber"        : "",
																					"dob"       : "",
																					"identificationNumber":"",
																					"identificationTypeId":"",
																					"unitId"          :$scope.unitId,
																					"organizationId"  :$scope.organizationId,
																					
																			}
																		}
																	}
																});
								 }
								if ($scope.patientDetailsObj.patientId != undefined) {
									console.log("$scope.patientDetailsObj",$scope.patientDetailsObj);
										$scope.patientObj.registrationTypeId = 3;
										$scope.searchType = 1;
										if($scope.patientDetailsObj.registrationTypeId == 4){
											$scope.searchType = 2;
										}
										
										//$scope.firstTabLevel = '1';
										$scope.patientObj.prefixId = $scope.patientDetailsObj.prefixId;
										$scope.patientObj.firstName = $scope.patientDetailsObj.firstName;
										$scope.patientObj.middleName = $scope.patientDetailsObj.middleName;
										$scope.patientObj.lastName = $scope.patientDetailsObj.lastName;
										
									     //OLD $scope.datepicker = new Date($scope.patientDetailsObj.dob);
										$scope.datepicker =$scope.patientDetailsObj.dob;
										//alert($scope.datepicker);
										//$scope.calculateAge();
										var dt = new Date(moment($scope.datepicker).format('MM/DD/YYYY'));
										$scope.patientDetailsObj.ageString = $rootScope.getAge(dt);
										//$scope.age = $rootScope.getAge(dt);
										$scope.calculateAge(dt);
										$scope.patientObj.genderId = $scope.patientDetailsObj.genderId;
										$scope.patientObj.mobileNumber = $scope.patientDetailsObj.mobileNumber;
										$scope.patientObj.email = $scope.patientDetailsObj.email;
										$scope.patientObj.patientId = $scope.patientDetailsObj.patientId;
										//ageString
										$scope.isPendingRequest = 'Y'; 
										$scope.setFirstTabLevel(3);
										$scope.isSetFirstTabLevel(3);
										$scope.selectItem($scope.patientDetailsObj);
										$scope.datepickerExpiry = new Date(moment(new Date()).format('MM/DD/YYYY'));
										angular.element(document.querySelector("#nor")).removeClass("active");
										
								 }
								}
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
								
								$scope.clearFields=function(){

									$scope.updateRegflag=true;
									$scope.sameAsLocal = 'Y';
									 $scope.addressFlag =false;
									/* if($scope.registrationFieldObj.email == "Y"){
										 if($scope.emailValidationRequired == 'Y'){
												$scope.otpvalidated = true;
											}else{
												$scope.otpvalidated = false;
											}
										}else{
											$scope.otpvalidated = false;
										}*/
									 $scope.emailOtp = null;
									 $scope.storeEmailId = null;
	                                 $scope.entredOpt = "";
									$scope.searchPatientList=[];
									$scope.searchDependentList=[];
									$scope.searchEmployeeList=[]; 
									$scope.age = '';
									
									var registrationTypeId = 1;
									if($scope.patientObj.registrationTypeId != undefined && $scope.patientObj.registrationTypeId != null && $scope.patientObj.registrationTypeId != ""){
										registrationTypeId = $scope.patientObj.registrationTypeId;
									}
									
									$scope.patientObj = {

											prefixId : '',
											firstName : '',
											middleName : '',
											lastName : '',
											genderId : '',
											countryCallingCode : '',
											email : '',
											nationalityId : '',
											raceId : '',
											maritalStatusId : '',
											mobileNumber : '',
											dob : '',
											age : '',
											identificationTypeId : '',
											identificationNumber : '',
											isVip : '',
											address : '',
											countryId : '',
											stateId : '',
											districtId : '',
											cityId :'',
											areaId : '',
											zipCode : '',
											phoneCode : '',
											phoneNumber : '',
											companyName : '',
											companyAddress : '',
											companyCountryId : '',
											companyStateId : '',
											companyDistrictId : '',
											companyCityId : '',
											companyAreaId : '',
											companyZipCode : '',
											companyMobileNumber : '',
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											updatedBy : $scope.userId,
											createdBy : $scope.userId,
											status : 'A',
											patientCategoryId:$scope.defaultPatientCategoryId,
											permanentAddress:'',
											permanentCountryId:'',
											permanentStateId:'',
											permanentDistrictId:'',
											permanentCityId:'',
											permanentAreaId:'',
											permanentZipCode:'',
											permanentPhoneCode:'',
											permanentPhoneNumber:'',
											aliseName:'',
											identificationExpiryDate:'',
											occupationId:'',
											registrationTypeId:registrationTypeId,
											previousId:'',
											patientId:0,
											patientDetailsId:'',
											uhidNumber:'',
											imagePath:'',
											thumbImage:'',	
											fileSequenceNumber:'',
											bloodGroup:'',
											area:'',
											permanentArea:'',
											isAlreadyExist:'N'
											
										};
									$scope.initSearchObj();
									if($scope.unitCountryId > 0)
									{
										$scope.patientObj.countryId  = $scope.unitCountryId;
										  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
										  $scope.patientObj.stateId  =  $scope.unitStateId;
										  $scope.patientObj.districtId = $scope.unitDistrictId;
										  $scope.patientObj.cityId = $scope.unitCityId;
										  $scope.patientObj.areaId = $scope.unitAreaId;
										  $scope.patientObj.zipCode = $scope.unitZipCode;
										  $scope.getStateByCountry($scope.patientObj.countryId,'L');
										  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
										  $scope.getDistrictByState($scope.patientObj.stateId,'L');
										  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
										  $scope.getAreaByCity($scope.patientObj.cityId,'L');
									}
									
									$scope.filepreview = "";
									$scope.patientImage1 = null;
									$scope.patientImage2 = null;
									$scope.patientImage3 = null;
									angular.element("input[type='file']").val(null);
                                    $scope.initKinDetails();
                                    $scope.kinsList =[];	
                                    $scope.ageDay = 0;
									$scope.ageMonth = 0;
									$scope.ageYear = 0;
									$scope.calDOBFlag = 0;
									$scope.expiryDateDisabled=true;
                                    $scope.aliseFlag = false;
									$scope.snapshotData = "";
									$scope.areaNotExist = 'N';
									$('#pieChartContainer').html(''); //remove canvas from container
									$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>');
									$('#pieChartContainer2').html(''); //remove canvas from container
									$('#pieChartContainer2').html('<canvas id="snapshot2" height="217" width="217"></canvas>');
									$('#pieChartContainer3').html(''); //remove canvas from container
									$('#pieChartContainer3').html('<canvas id="snapshot3" height="217" width="217"></canvas>');
									$scope.patientImage1 = null;
									$scope.patientImage2 = null;
									$scope.patientImage3 = null;
									angular.element("input[type='file']").val(null);
								}
								
				/** Code for finger pring Scanner*/		
								$scope.getDataByPinCode = function(){
									
									if($scope.patientObj.zipCode != null && $scope.patientObj.zipCode != undefined && $scope.patientObj.zipCode!="" && $scope.areaNotExist=='N' ){
										$rootScope.startLoader();
										var zipC = $scope.patientObj.zipCode;
										
									var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
									BillingGenericService
											.serviceAction(METHOD_GET,
													URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success"){
															
															if(response.data.object.countryId != '' ){
																$scope.patientObj.countryId = response.data.object.countryId;
															}
															
															if(response.data.object.stateId != ''){
																$scope.getStateByCountry(response.data.object.countryId,'L');
																$scope.patientObj.stateId = response.data.object.stateId;
															}
															
															if(response.data.object.districtId != ''){
																$scope.getDistrictByState(response.data.object.stateId,'L');
																$scope.patientObj.districtId = response.data.object.districtId;
															}
															
															if(response.data.object.cityId != '')
																$scope.getCityByDistrict(response.data.object.districtId,'L');
																$scope.patientObj.cityId = response.data.object.cityId;
															
															if(response.data.object.areaId != ''){
																$scope.getAreaByCity(response.data.object.cityId,'L');
																$scope.patientObj.areaId = response.data.object.areaId;
															}
															var checkAreaName = false;
															angular.forEach($scope.areaList,function(value,key){
																
																if(value.areaId==$scope.patientObj.areaId)
																{
																	checkAreaName = true;
																	$scope.areaNew  = value.areaName;
																}
															});
															
															if(checkAreaName == false){
																$scope.areaNew = "";
															}
															
															$scope.selectArea = false;
															$scope.flgArea = true;
															 setTimeout(function(){
							                                      $scope.selectArea = true;
							                                    },500);
															$rootScope.stopLoader();
														}else{
															$scope.areaNew='';
															$scope.patientObj.areaId='';
															$rootScope.stopLoader();
														}
													});
									}
									
								}
								
								$scope.changePermAswell = function(){
									$scope.areaNotExistPermanent = $scope.areaNotExist;
								}
								$scope.appendPermArea = function(){
									$scope.patientObj.permanentArea = $scope.patientObj.area;
								}
								
								$scope.getDataByPinCodePermanent = function(){
									
									if($scope.patientObj.permanentZipCode != null && $scope.patientObj.permanentZipCode != undefined && $scope.patientObj.permanentZipCode != "" && $scope.areaNotExistPermanent=='N'){
										$rootScope.startLoader();
										var zipC = $scope.patientObj.permanentZipCode;
										
									var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
									BillingGenericService
											.serviceAction(METHOD_GET,
													URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success"){
															
															if(response.data.object.countryId != '' ){
																$scope.patientObj.permanentCountryId = response.data.object.countryId;
															}
															
															if(response.data.object.stateId != ''){
																$scope.getStateByCountry(response.data.object.countryId,'LTOP');
																$scope.patientObj.permanentStateId = response.data.object.stateId;
															}
															
															if(response.data.object.districtId != ''){
																$scope.getDistrictByState(response.data.object.stateId,'LTOP');
																$scope.patientObj.permanentDistrictId = response.data.object.districtId;
															}
															
															if(response.data.object.cityId != '')
																$scope.getCityByDistrict(response.data.object.districtId,'LTOP');
																$scope.patientObj.permanentCityId = response.data.object.cityId;
															
															if(response.data.object.areaId != ''){
																$scope.getAreaByCity(response.data.object.cityId,'LTOP');
																$scope.patientObj.permanentAreaId = response.data.object.areaId;
															}
																var checkArea = false;
																angular.forEach($scope.areaList,function(value,key){
																	if(value.areaId==$scope.patientObj.permanentAreaId)
																{
																	var checkArea = true;
																	$scope.areaNew1  = value.areaName;
																}
															});
																
																if(checkArea == false){
																	$scope.areaNew1 = "";
																}
																
															$scope.selectArea = false;
															$scope.flgArea1 = true;
															 setTimeout(function(){
							                                      $scope.selectArea = true;
							                                    },500);
															$rootScope.stopLoader();
														}else{
															$scope.areaNew1='';
															$scope.patientObj.permanentAreaId='';
															$rootScope.stopLoader();
														}
													});
									}
									
								}
								
								
								 $scope.capture = function(id) {
						            try {
						            	var quality = 60; //(1 to 100) (recommanded minimum 55)
						                var timeout = 10; // seconds (minimum=10(recommanded), maximum=60, unlimited=0 )
						                var res = CaptureFinger(quality, timeout);	
						                if (res.httpStaus) {
						                    if (res.data.ErrorCode == "0") {
						                        document.getElementById('imgFinger'+id).src = "data:image/bmp;base64," + res.data.BitmapData;
						                        $scope.thumbImage = res.data.BitmapData;
						                        $scope.patientObj.thumbImage = res.data.BitmapData;
												//alert(res.data.BitmapData);
						                    }
						                }
						                else {
						                    alert(res.err);
						                }
						            }
						            catch (e) {
						                alert(e);
						            }
						            return false;
						        }
						
                                 //get Patient Document List
                                 $scope.patientPreviosDocList = [];
                                 $scope.getPatientPreviousDocumentList = function(){
                                        var data ={
                                                patientId : $scope.patientObj.patientId,
                                                unitId : $scope.unitId,
                                                organizationId :     $scope.organizationId
                                        }; 
                                            
                                        if($scope.patientObj.patientId==null ||$scope.patientObj.patientId==0
                                        || $scope.patientObj.patientId == undefined || $scope.patientObj.patientId==""){
                                            growl.warning("Please Select Patient",{
                                                title : "warning"
                                            });
                                            $rootScope.stopLoader();
                                            return false;
                                        }
                                        var URI = BASE_URL + ROOT_URL+"/api/global/getPatientDocumentDetailList";
                                        BillingGenericService
                                                .serviceAction(METHOD_POST,URI, data)
                                                .then(
                                                        function(response) {
                                                            if (response.data.status == "success"){
                                                                $scope.patientPreviosDocList = [];
                                                                $scope.patientPreviosDocList = response.data.listObject;
                                                                 angular.element('#previousPictureModal').modal('show');
                                                                $rootScope.stopLoader();
                                                            }else{
                                                                $rootScope.stopLoader();
                                                            }
                                                        });
                                        }
                                 
                             /*    if($scope.patientObj.email != ""){
                                	 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        			           		 var email = $scope.patientObj.email;
        			           		    if (!filter.test(email)) {
        			           		    growl.error("Please enter email",{
                        					title : ERROR_MSG
                        					});
                        					return false;;
        			           		    }
                                 }*/
                                 $scope.openEmailPopup = function(){
                                	 
                                	 if($scope.patientObj.email == undefined){
                                		 $scope.patientObj.email ="";
                                	 }
                                	 var filter = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/; 
        			           		 var email = $scope.patientObj.email;
                                	 if($scope.patientObj.email == 'filter')
         		           			{
         		           			}
                                	else{
                                		 if (!(email.match(filter))) {
             			           		    growl.error("Please enter valid email id.",{
                             					title : ERROR_MSG
                             					});
                             					return false;;
             			           		    }
                                		 $('#sendOtpOnMail').modal('show');
                         			}
                                 }
                                 
                                 $scope.openMobilePopup = function(){
                                	 if($scope.patientObj.mobileNumber.length == $scope.registrationFieldObj.mobileDigit){
                                		 $('#sendOtpOnMobile').modal('show');
                         			}else{
                         				growl.error(" Please enter valid mobile number",{
                         					title : ERROR_MSG
                         					});
                         					return false;
                         			}
                                  }
                                 
                                 $scope.clearEmail = function(){
                                 	
                                	 $('#sendOtpOnMail').modal('hide');
                                		 $scope.entredOpt = null;
                                 }
                                 
                                 
                                 $scope.clearMobile = function(){
                                  	
                                	 $('#sendOtpOnMobile').modal('hide');
                                		 $scope.entredOptMobile = null;
                                 }
                                 
                                 
                                 $scope.sendMailOtp = function(){
                                	 
                                	 $rootScope.startLoader();
                                	 
                                     var data ={
                                    		 toMailId : $scope.patientObj.email,
                                             unitId : $scope.unitId,
                                             organizationId : $scope.organizationId
                                     }; 
                                         
                                     var URI = BASE_URL + ROOT_URL+"/autoMail/sendOTP";
                                     BillingGenericService
                                             .serviceAction(METHOD_POST,URI, data)
                                             .then(
                                                     function(response) {
                                                         if (response.data.status == "success"){
                                                        	 
                                                        	 growl.success("OTP send on entered email id.",
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
                                 
                                 
                          $scope.sendMobileOtp = function(){
                                	 
                                	 $rootScope.startLoader();
                                	 
                                     var data ={
                                    		 mobileNumber : $scope.patientObj.mobileNumber,
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
                                 $scope.validateOtp = function(){
                                  	
                                	 if($scope.emailOtp == $scope.entredOpt){
                                		 $('#sendOtpOnMail').modal('hide');
                                		 $scope.emailOtp = null;
                                		 $scope.otpvalidated = false;
                                		 $scope.storeEmailId = $scope.patientObj.email;
                                	}else{
                                		$scope.otpvalidated = true;
                                			growl.warning("Please enter valid OTP",{title : "WARNING!"
										});
										return false;
                                	}
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
                                 
                                 $scope.checkEmail = function(){
                                	 
                                	// if($scope.registrationFieldObj.email == "Y"){
                                		 
                                		 if($scope.storeEmailId != null  && $scope.storeEmailId == $scope.patientObj.email ){
                                			 $scope.otpvalidated = false;
                                		 }else{
                                			 if( $scope.patientObj.email != null && $scope.patientObj.email != "" && $scope.emailValidationRequired == 'Y'){
													$scope.otpvalidated = true;
												}else{
													$scope.otpvalidated = false;
												}
                                		 }
											
										//}
                                	 
                                 }
                                 
                                 
                                 $scope.checkMobileNumber = function(){
                         			 if( $scope.patientObj.mobileNumber != null && $scope.patientObj.mobileNumber != "" && $scope.mobileValidationRequired == 'Y'){
											$scope.otpvalidatedMobile = true;
										}else{
											$scope.otpvalidatedMobile = false;
										}
                                  }
               /********************************zipcode***************************************************/
                                 $scope.selectFlag = false;
                                 $scope.onClickDropdownmyFun = function(){
                                     if($scope.patientObj.areaId != null && $scope.patientObj.areaId != ''){
                                         angular.forEach($scope.logEvents,function(v,k){
                                                     if(v.areaId == $scope.patientObj.areaId){
                                                         $scope.patientObj.area = v.areaName;
                                                     }
                                         });
                                         }

 									$scope.flgArea =false;
 	                                if($scope.selectFlag == false || $scope.selectFlag== undefined){
 	                                    $scope.flgHideShow = true;
 	                                    $scope.selectFlag = true;
 	                                }
 	                            }
                            $scope.selectFlag1 =false;
 							$scope.onClickDropdownmy = function(){
 	                            //alert("1-"+$scope.selectFlag);
 	                                if($scope.selectFlag1 == false || $scope.selectFlag1== undefined){
 	                                    $scope.testFlg = true;
 	                                    $scope.selectFlag1 = true;
 	                                    $scope.flgArea1 =false;
 	                                }
 	                            }
                                 $scope.getNewAreaInput = function(){
 									if($scope.selectArea == false){
 	                                    $scope.flgArea = true;
 	                                    $scope.selectArea = true;
 	                                }
 								}
                                 $scope.getNewAreaInput1 = function(){
 									if($scope.selectArea == false){
 	                                    $scope.flgArea1 = true;
 	                                    $scope.selectArea = true;
 	                                }
 								}
                                 $scope.myFunctionForToggle = function(flag) {
      								
      								if ($scope.tempFlg == true) {
      									$scope.testFlg = false;
      									$scope.tempFlg = false;
      									return;
      								}
      								if (flag == true) {
      									$scope.testFlg = true;
      									$scope.tempFlg=true;
      								}

      							}
//                                 $scope.myFunctionForToggle = function(flag) {
//                                      
//                                      if ($scope.tempFlg == true) {
//                                          $scope.testFlg = false;
//                                          $scope.tempFlg = false;
//                                          return;
//                                      }
//                                      if (flag == true) {
//                                          $scope.testFlg = true;
//                                          $scope.tempFlg=true;
//                                      }
//                                  }
                                 $scope.setFlagFalse=function(flag){
                 					if(flag==false){
                 						$scope.testFlg = false;
                                         $scope.selectFlag1 = true;
                 					}
                 					
                 				}
                                 $scope.setFlag=function(flag){
                 					if(flag==false){
                 						$scope.flgHideShow = false;
                                         $scope.selectFlag = true;
                 					}
                 					
                 				}
                                 
                                 $scope.addAreaById = function($item, $model, $label,flag){
                     	         	$scope.patientObj.areaId = $item.areaId;
                     	         	$scope.patientObj.area = $item.areaName;
                     	         	$scope.patientObj.permanentAddress = $scope.patientObj.address;
									$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
									$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
									$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
									$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
									$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
									$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
									$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
									$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
									$scope.setPostCode($scope.patientObj.areaId,0);
									$scope.setFlag(false);
                     	         }
                                
                                 
                                 
                                 $scope.searchAreaByCityId = function(searchKeyword){
                                	 
                     				$scope.areaList=[];
                     				var serData = {
                     						cityId : $scope.patientObj.cityId,
                     						searchKeyword : searchKeyword
                     				}
                     				var URI = BASE_URL + ROOT_URL + "/global/serviceAreaBysearchKeyword";
                     				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                     					if(response.data.status == "success"){
                     						$scope.areaList = response.data.listObject;
                     						if($scope.areaList.length == 0){
                     					/*		if($scope.patientObj.area != ''& $scope.patientObj.area != null ){
                     								$scope.patientObj.areaName =$scope.patientObj.area;
                     							}else{*/
                     							$scope.patientObj.areaName =null;
                     							$scope.patientObj.areaId=null;
                     							$scope.patientObj.area =null;
                     						/*	}*/
                     							growl.warning("No record found for serarch crateria please add new area",
        												{
        													title : 'warning!'
        												});
                     						}
                     					}
                     					return $scope.areaList;
                     				});
                     			}
                                 
                                 $scope.mysqlOnp = null;
                                 
                                   $scope.getDatabaseLinkToCheck = function(){
                                	 $rootScope.startLoader();
                                	 
                                     var data ={
                                             unitId : $scope.unitId,
                                     }; 
                                         
                                     var URI = BASE_URL + ROOT_URL+"/api/global/getDatabaseLink";
                                     BillingGenericService
                                             .serviceAction(METHOD_POST,URI, data)
                                             .then(
                                                     function(response) {
                                                         if (response.data.status == "success"){
                                                        	 $scope.mysqlOnp  = response.data.object.mysqlOnp;
                                                             $rootScope.stopLoader();
                                                         }else{
                                                             $rootScope.stopLoader();
                                                         }
                                                     });
                                     }
                                   $scope.getDatabaseLinkToCheck();


                                   $scope.updatePatientDetailsInSQL = function(){
                                  	 $rootScope.startLoader();
                                  	  var data ={}; 
                                  	   data = $scope.selectedPatientData;
                                  	   data.unitId =$scope.unitId;
                                  	   data.organizationId =$scope.organizationId;
                                  	   data.updatedBy = $scope.userId;
                                  	  data.createdBy = $scope.userId;
                                  	var convertedDate =  moment($scope.selectedPatientData.birthDate, "DD/MM/YYYY").toDate();
                                 	 data.dob = moment(convertedDate).format('DD-MM-YYYY HH:mm:ss');
                                  	   
                                       var URI = BASE_URL + ROOT_URL+"/api/global/checkSqlConnectionForUpdate";
                                       BillingGenericService
                                               .serviceAction(METHOD_POST,URI, data)
                                               .then(
                                                       function(response) {
                                                           if (response.data.status == "success"){
                                                          	 growl.success("Updated successfully",
               														{
               															title : 'Success!'
               														});
                                                               $rootScope.stopLoader();
                                                           }else{
                                                               $rootScope.stopLoader();
                                                           }
                                                       });
                                       }
                                   
                                   
                                   $scope.notifyToABDM = function(){
                                	   if($scope.patientObj.mobileNumber != undefined && $scope.patientObj.mobileNumber != null){
                         				$rootScope.startLoader();
                         				
                         				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/link/patient/links/sms/notify2";
                         				$scope.mobile="";
                         				 var data={
                          					    "mobileNumber" : $scope.patientObj.mobileNumber,
                          					    "organizationId" : $scope.organizationId,
                      						    "unitId" : $scope.unitId,
                          					    "eventId":31
                          					}
                         				GenericService.serviceAction("POST", URI, data).then(
                         					function (response) {
                         						if (response.data.status == "success") {
                         							growl.success("Message send succesfully on aadhar linked mobile number");
                         							 $rootScope.stopLoader();
                         						}
                        		    				else if(response.data.status == "error")
                        		    					{
                        		    					   $rootScope.stopLoader();
                        			   	   						$rootScope.stopLoader();
                        			   	   						growl.error(response.data.message);
                        		    					}
                         					
                         					},
                         					function errorCallback(response) {
                         						$rootScope.stopLoader();
                         						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
                         					});
                                	   }else{
                                		   growl.error("Please select patient and mobile number is required !!!");
                                	   }
                         			}
                                   
						} ]);



