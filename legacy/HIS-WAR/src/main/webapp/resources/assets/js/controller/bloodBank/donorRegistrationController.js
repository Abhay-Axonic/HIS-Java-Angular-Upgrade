'use strict';

angular
		.module('myApp')
		.controller(
				'donorRegistrationController',
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
				
					function($scope, $http, $localStorage, $sessionStorage,
							$cookies, $rootScope, GenericService,
							BillingGenericService,GenericServiceFileUpload, growl, $state,
							$stateParams,$modalInstance) {

							
							$scope.search = function(){
								
							//	alert("fbgjdsfbgdfiusg");
							}
							/* init() function for form object create. */
							$scope.init = function() {
								
								$scope.filepreview = "";
								   
								//for star camera
								
								  var _video = null,
							        patData = null;

							    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

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
									showWeeks : false
								};

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								// for datePicker2
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
								$scope.openDatePickerExpiry = function() {
									$scope.datepickerExpiry.opened = true;
								};

								$scope.datepickerExpiry = {
									opened : false
								};
								
								//for datepicker Search
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};

								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};
								

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								$scope.minAge = cookieObjectAppConfig.minAge;
								$scope.maxAge = cookieObjectAppConfig.maxAge;
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
								
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.addressFlag =false;
								
								$scope.aliseFlag = false;
								
								$scope.expiryDateDisabled = true;
								
								$rootScope.stopLoader();
								
								$scope.searchType = 1;
								//alert($scope.searchType);
								
								$scope.searchPatientList = [];
								$scope.searchDonorList = [];
								$scope.searchEmployeeList = [];
								$scope.searchDependentList =[];
								
								$scope.isRedirect = 'N';
								
								$scope.isPendingRequest = "N";
								
								$scope.birthDateSearchFlag = true;
								
								
								$scope.searchSelectPatient = {}
								$scope.itemSelecteds = {};
								
								$scope.datepickerExpiry = new Date(moment(new Date()).format('MM/DD/YYYY'));
								//alert($scope.datepickerExpiry );
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.tab=1;
								
								$scope.ph_numbr = /^\+?\d{10}$/;
						        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
								
								
								
								
								$scope.donorObj = {

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
										address : '',
										countryId : '',
										stateId : '',
										districtId : '',
										cityId :'',
										areaId : '',
										zipCode : '',
										phoneCode : '',
										phoneNumber : '',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										status : 'A',
										patientCategoryId:'1',
										permanentAddress:'',
										permanentCountryId:'',
										permanentStateId:'',
										permanentDistrictId:'',
										permanentCityId:'',
										permanentAreaId:'',
										permanentZipCode:'',
										permanentPhoneCode:'',
										permanentPhoneNumber:'',
										identificationExpiryDate:'',
										occupationId:'',
										registrationTypeId:1,
										patientId:0,
										imagePath:'',
										thumbImage:'',
										//bloodGroup:'',
										donoTypeId:'',
										donorId:0,
										donorDetailsId:"",
										isAutologous:false,
										bloodGroupId:"",
										exportedToScreening:false
									};
								$scope.sameAsLocal='Y';
								
								//********************* tab level************//
								$scope.firstTabLevel = 1;

								
								$scope.setFirstTabLevel = function(newTab) {
									$scope.firstTabLevel = newTab;
									$scope.setFirstLevelTab(newTab);
									if (newTab == '1') {
										//$scope.searchPatientNewDonor(1);
										//$scope.searchDonor();
										$scope.donorObj={};
										
									}
									if (newTab == '2') {
										//$scope.searchPatientRegisteredDonor();
										//$scope.searchPatient();
										$scope.donorObj={};
										$scope.searchPatientList=[];
										$scope.searchDonorList=[];
										$scope.donorSearchObj.patientName="";
										$scope.donorSearchObj.donorName="";
										$scope.donorImage=null;
									}
									if (newTab == '3') {
										//$scope.searchPatient();
										//$scope.searchPatientRegisteredPatient();
										$scope.donorObj={};
										$scope.searchDonorList=[];
										$scope.searchPatientList=[];
										$scope.donorSearchObj.patientName="";
										$scope.donorSearchObj.donorName="";
										$scope.donorImage=null;
									}
									
									if($scope.unitCountryId > 0)
									{
									  $scope.donorObj.countryId  = $scope.unitCountryId;
									  $scope.donorObj.stateId  =  $scope.unitStateId;
									  $scope.donorObj.districtId = $scope.unitDistrictId;
									  $scope.donorObj.cityId = $scope.unitCityId;
									  $scope.donorObj.areaId = $scope.unitAreaId;
									  $scope.getStateByCountry($scope.donorObj.countryId,'L');
									  $scope.getDistrictByState($scope.donorObj.stateId,'L');
									  $scope.getCityByDistrict($scope.donorObj.districtId,'L');
									  $scope.getAreaByCity($scope.donorObj.cityId,'L');
									}
									$scope.addressFlag = false;
									$scope.sameAsLocal='Y';
								};

								$scope.isSetFirstTabLevel = function(tabNum) {
									return $scope.firstTabLevel === tabNum;
									
								};
								
								$scope.setCurrentTab = function(newTab) {
									switch(newTab) {
										case 1 : $scope.currentTab = "NewDonor"
										break;
										case 2 : $scope.currentTab = "RegisteredDonor"
										break;
										case 3 : $scope.currentTab = "RegisteredPatient"
										break;
										
									}
								}
								$scope.setCurrentTab(1);
								
								$scope.setFirstLevelTab = function(newTab) {
									switch (newTab) {
									case 1:
										$scope.currentTab = "NewDonor"
										break;
									case 2:
										$scope.currentTab = "RegisteredDonor"
										break;
										
									case 3:
										$scope.currentTab = "RegisteredPatient"
										break;
									}
								}

								
								
								$scope.searchPatientNewDonor=function(){
								//	alert("searchPatientNewDonor");
								}
								$scope.searchPatientRegisteredDonor=function(){
									//alert("searchPatientRegisteredDonor");
								}
								$scope.searchPatientRegisteredPatient=function(){
								//	alert("searchPatientRegisteredPatient");
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
					
								BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
										NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											if (response.data.status == "success")
												$scope.specialityList = response.data.listObject;
										});
								
							
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORY;
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
											$scope.patientCategoryList = [];

											if (response.data.status == "success") {
												$scope.patientCategoryList = response.data.listObject;
												console.log($scope.patientCategoryList);
											}
										});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPREFIXLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													$scope.prefixList = [];
													if (response.data.status == "success")
														$scope.prefixList = response.data.listObject;
												});

								var URI = BASE_URL + ROOT_URL+ GETACTIVEGENDERLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													$scope.genderList = [];
													if (response.data.status == "success")
														$scope.genderList = response.data.listObject;
												});
								
								var URI = BASE_URL + ROOT_URL+ GETACTIVEOCCUPATIONLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											$scope.occupationList = [];
											if (response.data.status == "success")
												$scope.occupationList = response.data.listObject;
										});
						
								var URI = BASE_URL + ROOT_URL+ GETACTIVENATIONALITYLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													$scope.nationalityList = [];
													if (response.data.status == "success")
														$scope.nationalityList = response.data.listObject;
														console.log("$scope.nationalityList",$scope.nationalityList);
												});

								var URI = BASE_URL + ROOT_URL + GETRACELIST;
								BillingGenericService.serviceAction(METHOD_POST, URI, commonObj1,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													$scope.raceList = [];
													if (response.data.status == "success")
														$scope.raceList = response.data.listObject;
												});
								
								var URI = BASE_URL + ROOT_URL+ GETACTIVEMARITALSTATUSLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													$scope.maritalStatusList = [];
													if (response.data.status == "success")
														$scope.maritalStatusList = response.data.listObject;
												});

								var URI = BASE_URL + ROOT_URL+ GETACTIVEIDENTIFICATION;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											$scope.identificationList = [];
											if (response.data.status == "success")
												$scope.identificationList = response.data.listObject;
										});
								
								var URI = BASE_URL + ROOT_URL+ GETACTIVECOUNTRYLIST+$scope.organizationId;								
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											$scope.countryList = [];
											if (response.data.status == "success")
												$scope.countryList = response.data.listObject;
										});
								//getting default race start
								var URI = BASE_URL + ROOT_URL+ "/adt/getDefaultRaceList/"+$scope.organizationId;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											$scope.defaultRaceList = [];
											if (response.data.status == "success")
												//$scope.patientObj.raceId = response.data.listObject[0].raceId;
												$scope.donorObj.raceId = response.data.listObject[0].raceId;
												console.log("defaultRaceList=",response.data.listObject[0]);
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
													}
												}												
		
										} else if (response.data.status == 'error') {
											growl.error(response.data.message,
												{
													title: 'Error!'
												});
										}
									});
								
							}

							$scope.init();
							
							$scope.initSearchObj = function()
							{
								$scope.donorSearchObj = {
										"uhidNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNumber"        : "",
										"dob"       : "",
										"identificationNumber":"",
										"identificationTypeId":"",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"donorName":"",
										"donorNumber"      : ""
										
								}
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
							$scope.initSearchObj();
							
					    
							
							$scope.bloodGroupList = [];
							$scope.getBloodGroupList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getActiveBloodGroupMaster/"+$scope.organizationId +"/"+ $scope.unitId;;
									GenericService
									.serviceAction(METHOD_GET, URI )
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodGroupList = response.data.listObject;
													console.log("bloodGroupList ",response.data.listObject);
												}
											});
							}
							
							$scope.getBloodGroupList();
							
							$scope.newAge="";
							$scope.calculateAge= function()
							{
								var dt = moment($scope.datepicker).format('MM/DD/YYYY');
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
							
							$scope.myFunct = function(age) {
								if(age>=$scope.minAge && age <= $scope.maxAge){
									 if($('#datepicker').val()!=""){
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;									    
										}else{
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;
										    $scope.datepicker = new Date("01/01/"+finalYear);
										}
								}else{
									$scope.age = "";
									growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
										title : "warning"
										});
								}
								  
							}
							
							
							$scope.disableEnableExpiry = function(isExpiryVal)
							{
								 var splittedStringArray = isExpiryVal.split("_");
								if(splittedStringArray[1] == 'Y')
								{
									$scope.expiryDateDisabled = false;
									
								}
								else
									{
									 $scope.expiryDateDisabled = true;
									}
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
									
									if($scope.donorObj.countryId =="" ||  $scope.donorObj.countryId ==null 
											||  $scope.donorObj.stateId ==null ||  $scope.donorObj.stateId =="" 
											|| $scope.donorObj.districtId =="" || $scope.donorObj.districtId == null
											|| $scope.donorObj.cityId ==""  ||$scope.donorObj.cityId ==null
											|| $scope.donorObj.areaId == "" || $scope.donorObj.areaId == null
											|| $scope.donorObj.phoneCode =="" ){
									
										 $scope.sameAsLocal='Y';
										 growl.warning("Please Select All Required fields",{
												title : 'Warning !'
												});
												return false;
									}
									
									
									if($scope.donorObj.countryId != '' )
									$scope.getStateByCountry($scope.donorObj.countryId,'LTOP');
									
									if($scope.donorObj.stateId != '' )
									$scope.getDistrictByState($scope.donorObj.stateId,'LTOP');
									
									if($scope.donorObj.districtId != '' )
									$scope.getCityByDistrict($scope.donorObj.districtId,'LTOP');
									
									if($scope.donorObj.cityId != '' )
									$scope.getAreaByCity($scope.donorObj.cityId,'LTOP');
									
									$scope.donorObj.permanentAddress = $scope.donorObj.address;
									$scope.donorObj.permanentCountryId = $scope.donorObj.countryId;
									$scope.donorObj.permanentStateId = $scope.donorObj.stateId;
									$scope.donorObj.permanentDistrictId = $scope.donorObj.districtId;
									$scope.donorObj.permanentCityId = $scope.donorObj.cityId;
									$scope.donorObj.permanentAreaId = $scope.donorObj.areaId;
									$scope.donorObj.permanentZipCode = $scope.donorObj.zipCode;
									$scope.donorObj.permanentPhoneCode = $scope.donorObj.phoneCode;
									$scope.donorObj.permanentPhoneNumber = $scope.donorObj.phoneNumber;
									$scope.addressFlag =true;
									}
								else
									{
									/*$scope.donorObj.permanentAddress ='';
									$scope.donorObj.permanentCountryId = '';
									$scope.donorObj.permanentStateId ='';
									$scope.donorObj.permanentDistrictId ='';
									$scope.donorObj.permanentCityId ='';
									$scope.donorObj.permanentAreaId ='';
									$scope.donorObj.permanentZipCode ='';
									$scope.donorObj.permanentPhoneCode ='';
									$scope.donorObj.permanentPhoneNumber ='';*/
									$scope.addressFlag = false;
									}
							}	
							//get calling code by using country id start
							$scope.getCallingCode=function(data,type){
								var URI = BASE_URL + ROOT_URL+ GETSINGLECOUNTRY;
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success" && response.data.listObject.length >0 ) {
												$scope.donorObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
												if(type=="PA"){
													$scope.donorObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
												}else{
													$scope.donorObj.phoneCode = response.data.listObject[0].countryCallingCode;
												}

											} else {
												//alert("Error!!");
											}
										});
							}
							$scope.getNationalityByContryId=function(data){
								data.organizationId = $scope.organizationId;
								var URI = BASE_URL + ROOT_URL+ "/adt/getNationalityByCountryId";
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success") {
												console.log("nationality List ",response.data.listObject[0]);
												//$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;
												$scope.donorObj.nationalityId = response.data.listObject[0].nationalityId;

											} else {
  
											}
										});
							}
							
							$scope.getStateByCountry = function(id,type) {
								$scope.startLoader();
								var data = {
									countryId : id
								};								
								
								var dataorg = {
										countryId : id,
										organizationId : $scope.organizationId,
									};
								
								$scope.getCallingCode(dataorg,type);
								$scope.getNationalityByContryId(data);
								var URI = BASE_URL + ROOT_URL+ GETSTATELISTBYCOUNTRYID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
												if(type == 'P')
													{
													$scope.stateList = [];
													$scope.stateList = response.data.listObject;
													
													$scope.donorObj.stateId="";
													$scope.donorObj.districtId="";
													$scope.donorObj.cityId="";
													$scope.donorObj.areaId="";
													$scope.donorObj.zipCode="";
													}
												else if(type == 'PA')
													{
													$scope.permanentStateList = [];
													$scope.permanentStateList = response.data.listObject;
													
													$scope.donorObj.permanentStateId="";
													$scope.donorObj.permanentDistrictId="";
													$scope.donorObj.permanentCityId="";
													$scope.donorObj.permanentAreaId="";
													$scope.donorObj.permanentZipCode="";
													}else if(type == 'L')
													{
														$scope.stateList = [];
														$scope.stateList = response.data.listObject;
														
													}else if(type == 'LTOP')
													{
														$scope.permanentStateList = [];
														$scope.permanentStateList = response.data.listObject;
															
													}
												else
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
								var URI = BASE_URL + ROOT_URL+ GETDISTRICTLISTBYSTATEID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success")
											{
												if(type == 'P')
												{
													$scope.districtList = [];
													$scope.districtList = response.data.listObject;
													$scope.donorObj.districtId="";
													$scope.donorObj.cityId="";
													$scope.donorObj.areaId="";
													$scope.donorObj.zipCode="";
													}
													else if(type == 'PA')
													{
													$scope.permanentDistrictList = [];
													$scope.permanentDistrictList = response.data.listObject;
													
													$scope.donorObj.permanentDistrictId="";
													$scope.donorObj.permanentCityId="";
													$scope.donorObj.permanentAreaId="";
													$scope.donorObj.permanentZipCode="";
													}else if(type == 'L')
													{
														$scope.districtList = [];
														$scope.districtList = response.data.listObject;
														
													}else if(type == 'LTOP')
													{
														$scope.permanentDistrictList = [];
														$scope.permanentDistrictList = response.data.listObject;
															
													}
											else
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
								var URI = BASE_URL + ROOT_URL+ GETCITYLISTBYDISTRICTID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											
											if (response.data.status == "success")
											{
												if(type == 'P')
												{
													$scope.cityList = [];
													$scope.cityList = response.data.listObject;
												
													$scope.donorObj.cityId="";
													$scope.donorObj.areaId="";
													$scope.donorObj.zipCode="";
													}
													else if(type == 'PA')
													{
													$scope.permanentCityList = [];
													$scope.permanentCityList = response.data.listObject;
													
													$scope.donorObj.permanentCityId="";
													$scope.donorObj.permanentAreaId="";
													$scope.donorObj.permanentZipCode="";
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
								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL+ GETAREALISTBYCITY;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											
											if (response.data.status == "success")
											{
												if(type == 'P')
												{
													$scope.areaList = [];
													$scope.areaList = response.data.listObject;
												
													 $scope.donorObj.areaId="";
													 $scope.donorObj.zipCode="";
												}else if(type == 'PA'){
													$scope.permanentAreaList = [];
													$scope.permanentAreaList = response.data.listObject;
													
													$scope.donorObj.permanentAreaId="";
													$scope.donorObj.permanentZipCode="";
												}else if(type == 'L'){
													$scope.areaList = [];
													$scope.areaList = response.data.listObject;
															
												}else if(type == 'LTOP'){
													$scope.permanentAreaList = [];
													$scope.permanentAreaList = response.data.listObject;
																
												}
											else
												{
													$scope.CompanyAreaList = [];
													$scope.CompanyAreaList = response.data.listObject;
												}
											}
											$scope.stopLoader();
										});

							}
							
							if($scope.unitCountryId > 0)
								{
								  $scope.donorObj.countryId  = $scope.unitCountryId;
								  $scope.donorObj.stateId  =  $scope.unitStateId;
								  $scope.donorObj.districtId = $scope.unitDistrictId;
								  $scope.donorObj.cityId = $scope.unitCityId;
								  $scope.donorObj.areaId = $scope.unitAreaId;
								  $scope.getStateByCountry($scope.donorObj.countryId,'L');
								  $scope.getDistrictByState($scope.donorObj.stateId,'L');
								  $scope.getCityByDistrict($scope.donorObj.districtId,'L');
								  $scope.getAreaByCity($scope.donorObj.cityId,'L');
								}

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDonorRegistration();
							}
							
							//get blood donor type list
							$scope.donorTypeList = [];
							$scope.getBloodDonorTypeList = function()
							{
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getBloodDonorTypeMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											
											if (response.data.status == "success")
											{
												$scope.donorTypeList = response.data.listObject;
												console.log("$scope.donorTypeList",$scope.donorTypeList);
											}
										});

							}
							
							$scope.getBloodDonorTypeList(); 
							
							$scope.saveDonorRegistration = function()
							{
								console.log("PatientOBJ",$scope.donorObj);
								
								$scope.$broadcast('show-errors-check-validity');
								$scope.tab=1;
						      //  if($scope.patientRegistrationform.$valid){
						        	var contryCode=$scope.donorObj.countryCallingCode;
						        	var mobileNo=$scope.donorObj.mobileNumber;
						        	if(contryCode==91){
						        		if(mobileNo.length==10){
						        			
											}else{
												growl.error("Please enter valid mobile number",{
													title : ERROR_MSG
													});
													$('#mobileNo').focus();
													return false;
						        		}
						        	}else{
							        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
							        			
											}else{
												growl.error("Please enter valid mobile number",{
													title : ERROR_MSG
													});
													$('#mobileNo').focus();
													return false;
						        		}
						        	}
						           $scope.tab=2;			           
						        	 //if($scope.patientRegistrationform2.$valid){
						        		 //test start
						        		 if($scope.donorObj.email == null){
						        			 $scope.donorObj.email = "";
						        		 }
						        		 if($scope.donorObj.email != "")
						           			{
							           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
							           		 var email = $scope.donorObj.email;
							           		    if (!filter.test(email)) {
							           		     $scope.tab=1;
						        				    growl.error(EMAIL_MSG,{
														title : ERROR_MSG
													});
						        				    $('#email').focus();
						        				    return false;
							           		    }
							           		    else{
							           		    	if($scope.donorObj.phoneNumber == undefined)
							           		    			$scope.donorObj.phoneNumber='';
							           		    	if($scope.donorObj.phoneNumber == null)
							           		    			$scope.donorObj.phoneNumber='';
							           		     if($scope.donorObj.phoneNumber != '')
									        		{
									        			 var val = $scope.donorObj.phoneNumber;
									        			 if (/^\d{10}$/.test(val)) {

									        				 $scope.storeDonorDetails();
									        				} else {
									        				   
									        				    $scope.tab=2;
									        				    growl.error(PHONE_NUMBER_MSG,{
																	title : ERROR_MSG
																});
									        				    $('#phoneNumber').focus();
									        				    return false;
									        				}
									        		}
							           		     else
							           		    	 {
							           		    	 $scope.storeDonorDetails();
							           		    	 }
							           		    }
						           			}
						        		 else
						        			 {
						        			 if($scope.donorObj.phoneNumber == undefined)
						        				 $scope.donorObj.phoneNumber='';
						        			 if($scope.donorObj.phoneNumber != '')
								        		{
								        			 var val = $scope.donorObj.phoneNumber;
								        			 if (/^\d{10}$/.test(val)) {

								        				 $scope.storeDonorDetails();
								        				} else {
								        				   
								        				    $scope.tab=2;
								        				    growl.error(PHONE_NUMBER_MSG,{
																title : ERROR_MSG
															});
								        				    $('#phoneNumber').focus();
								        				    return false;
								        				}
								        		}
						        			 else
						        				 {
						        				 $scope.storeDonorDetails();
						        				 }
						        			 }
						        		 //test end
								      //  };
						     //   }
						        
							}
							
							 /***============================================Code Image Upload==============================================*/
						    /**PatientImages*/
						    $scope.donorImage;
						    $scope.patientImage2;
						    $scope.patientImage3;
						    
						    
						    $scope.disableEnableExpiry = function(isExpiryVal)
							{
								 var splittedStringArray = isExpiryVal.split("_");
								if(splittedStringArray[1] == 'Y')
								{
									$scope.expiryDateDisabled = false;
								}
								else
									{
									 $scope.expiryDateDisabled = true;
									}
							}
						    
						    $scope.convertBase64ToImage = function (dataurl, filename) {
							    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
							        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
							    while(n--){
							        u8arr[n] = bstr.charCodeAt(n);
							    }
							    return new File([u8arr], filename, {type:mime});
							}
						    
						    $scope.uploadDonorImage = function(inputFile,donorId,orgId,unitId)
						    {
						    	    var file = inputFile;
							        var fd = new FormData();
							        fd.append('file', file );
									var URI1 = BASE_URL + ROOT_URL + DONOR_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+donorId;
									GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
										}
									});
						    }
						   
						    /***============================================Code Image Upload==============================================*/
							$scope.storeDonorDetails = function()
							{
//								$rootScope.startLoader();
								
								console.log("DonorOBJ",$scope.donorObj);
								/*//return false;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {*/
									
									//$rootScope.startLoader();
									
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
							
									
									$scope.donorObj.createdDate = $scope.currentDate;
									$scope.donorObj.organizationId=$scope.organizationId;
									$scope.donorObj.unitId=$scope.unitId;
									$scope.donorObj.createdBy=$scope.userId;
									$scope.donorObj.updatedBy=$scope.userId;
									$scope.donorObj.updatedDate = $scope.currentDate;
									
									if($scope.sameAsLocal == 'Y'){
										$scope.donorObj.permanentAddress = $scope.donorObj.address;
										$scope.donorObj.permanentCountryId = $scope.donorObj.countryId;
										$scope.donorObj.permanentStateId = $scope.donorObj.stateId;
										$scope.donorObj.permanentDistrictId = $scope.donorObj.districtId;
										$scope.donorObj.permanentCityId = $scope.donorObj.cityId;
										$scope.donorObj.permanentAreaId = $scope.donorObj.areaId;
										$scope.donorObj.permanentZipCode = $scope.donorObj.zipCode;
										$scope.donorObj.permanentPhoneCode = $scope.donorObj.phoneCode;
										$scope.donorObj.permanentPhoneNumber = $scope.donorObj.phoneNumber;
										}
									
									 if (!$scope.donorObj.prefixId || $scope.donorObj.prefixId == ''|| $scope.donorObj.prefixId == undefined) {
								            growl.error('Please select a prefix.', 
								            		{ 
								            	title: 'Validation Error' 
								            		});
								            return false;
								        } 
									  if (!$scope.donorObj.firstName || $scope.donorObj.firstName == '' || $scope.donorObj.firstName == undefined) {
								            growl.error('Please enter a first name.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if (!$scope.donorObj.lastName || $scope.donorObj.lastName == '' || $scope.donorObj.lastName == undefined ) {
								            growl.error('Please enter a last name.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if($scope.datepicker.opened == false ){
		                                    growl.error(" Please enter Birth Date",{
		                                        title : ERROR_MSG
		                                        });
		                                        return false;
		                                }
									  
									  if (!$scope.donorObj.genderId) {
								            growl.error('Please select a gender.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if (!$scope.donorObj.countryCallingCode) {
								            growl.error('Please enter a country calling code.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  var mobileNumberPattern = /^\d{10}$/;
									  if (!$scope.donorObj.mobileNumber || !$scope.donorObj.mobileNumber.match(mobileNumberPattern)) {
								            growl.error('Please enter a valid 10-digit mobile number.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if (!$scope.donorObj.nationalityId) {
								            growl.error('Please select a nationality.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if (!$scope.donorObj.bloodGroupId) {
								            growl.error('Please select a blood group.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if (!$scope.donorObj.identificationTypeId) {
								            growl.error('Please select an identification type Doc.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  if (!$scope.donorObj.identificationNumber || $scope.donorObj.identificationNumber.trim() === '') {
								            growl.error('Please enter an identification number.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
								        if ($scope.datepickerExpiry == null ||$scope.datepickerExpiry == ''|| $scope.datepickerExpiry == undefined) {
								            growl.error('Please select Document Expiry date.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
								        
									    if (!$scope.donorObj.donoTypeId) {
								            growl.error('Please select a donor type.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.address) {
								            growl.error('Please enter an address.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.countryId) {
								            growl.error('Please select a country.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.stateId) {
								            growl.error('Please select a state.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.districtId) {
								            growl.error('Please select a district.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.cityId) {
								            growl.error('Please select a city.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.areaId) {
								            growl.error('Please select an area.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.zipCode) {
								            growl.error('Please enter a zip code.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.phoneCode) {
								            growl.error('Please enter a phone code.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.phoneNumber) {
								            growl.error('Please enter a phone number.', 
								            		{ title: 'Validation Error' });
								            return false;
								        }
									    if (!$scope.donorObj.permanentAddress) {
								            growl.error('Please enter a permanent address.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									  
									    if (!$scope.donorObj.permanentCountryId) {
								            growl.error('Please select a permanent country.', { title: 'Validation Error' });
								            return false;
								        }
									   
									    if (!$scope.donorObj.permanentStateId) {
								            growl.error('Please select a permanent state.', { title: 'Validation Error' });
								            return false;
								        }
									    if (!$scope.donorObj.permanentDistrictId) {
								            growl.error('Please select a permanent district.', { title: 'Validation Error' });
								            return false;
								        }
									    if (!$scope.donorObj.permanentCityId) {
								            growl.error('Please select a permanent city.', { title: 'Validation Error' });
								            return false;
								        }
									    if (!$scope.donorObj.permanentAreaId) {
								            growl.error('Please select a permanent area.', 
								            		{ title: 'Validation Error' 
								            		});
								            return false;
								        }
									    if (!$scope.donorObj.permanentCountryId) {
								            growl.error('Please select a permanent country.', { title: 'Validation Error' });
								            return false;
								        }
									    if (!$scope.donorObj.permanentPhoneCode) {
								            growl.error('Please enter a permanent phone code.', { title: 'Validation Error' });
								            return false;
								        }
									    if (!$scope.donorObj.permanentPhoneNumber) {
								            growl.error('Please enter a permanent phone number.', { title: 'Validation Error' });
								            return false;
								        }
									   
									    if (!$scope.donorObj.permanentZipCode) {
								            growl.error('Please enter a permanent zip code.', { title: 'Validation Error' });
								            return false;
								        }
									    
									$scope.donorObj.firstName =   $rootScope.capitalize_Words($scope.donorObj.firstName);
									$scope.donorObj.middleName =  ($scope.donorObj.middleName == null || $scope.donorObj.middleName.trim() == "" || $scope.donorObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.donorObj.middleName));
									$scope.donorObj.lastName =   ($scope.donorObj.lastName == null || $scope.donorObj.lastName.trim() == "" || $scope.donorObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.donorObj.lastName));
									$scope.donorObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
									var ConDate = $('#datepickerExpiry').val();
									if(ConDate != '')
										$scope.donorObj.identificationExpiryDate = moment($scope.datepickerExpiry).format('DD-MM-YYYY HH:mm:ss');
									
									if($scope.donorObj.identificationTypeId==undefined || $scope.donorObj.identificationTypeId=="")
										$scope.donorObj.identificationTypeId=null;
									
									if($scope.donorObj.identificationTypeId !=null)
									{
								      var splittedStringArray = $scope.donorObj.identificationTypeId.split("_");
								      $scope.donorObj.identificationTypeId = splittedStringArray[0]; 
								  
									}
									else
									{
										$scope.donorObj.identificationTypeId = null;
									}
									
									 console.log($scope.donorObj);
//									return false;
									 
									 if($scope.donorObj.patientId > 0)
										 {
										 
										 var URI = BASE_URL + ROOT_URL
											+ SAVE_DONOR_REGISTRATION_MASTER;
									
									console.log($scope.donorObj);
									//return false;
									
									GenericService.serviceAction("POST", URI, $scope.donorObj).then(function(response) {
														console.log("save response:",response);
														
														if (response.data.status == "success")
														{
															if($scope.donorImage!=undefined &&  !angular.equals({}, $scope.donorImage))
															{
															  $scope.uploadDonorImage($scope.donorImage,response.data.object.donorId,$scope.organizationId,$scope.unitId);
															}
															if($scope.snapshotData!=undefined && $scope.snapshotData!="")
															{
															  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getYear()+".jpeg");
															  $scope.uploadDonorImage(file,response.data.object.donorId,$scope.organizationId,$scope.unitId);
															}
															
														}
														$scope.donorImage=null;
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.donorObj={};
														$scope.donorImage="";
														$scope.datepickerExpiry="";
														$scope.age="";
														$scope.datepicker="";
														$scope.filepreview="";
														$scope.donorObj.countryId="";
														$scope.donorObj.stateId="";
														$scope.donorObj.districtId="";
														$scope.donorObj.cityId="";
														$scope.donorObj.areaId="";
														$scope.donorObj.nationalityId="";
														$scope.sameAsLocal='Y';
														$scope.addressFlag = false;
														$rootScope.stopLoader();
													});
									
										 }
									
									 else if($scope.donorObj.donorId > 0){
										 var URI = BASE_URL + ROOT_URL
											+ UPDATE_DONOR_REGISTRATION_MASTER;
										 $scope.donorObj.patientId=null;
									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, $scope.donorObj,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response)
													{
														console.log("response",response);
														
														if (response.data.status == "success")
															{
															if($scope.donorImage!=undefined &&  !angular.equals({}, $scope.donorImage))
																   {
																    $scope.uploadDonorImage($scope.donorImage,response.data.object.donorId,$scope.organizationId,$scope.unitId);
																   }
															if($scope.snapshotData!=undefined && $scope.snapshotData!="")
															{
															  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getYear()+".jpeg");
															  $scope.uploadDonorImage(file,$scope.donorObj.donorId,$scope.organizationId,$scope.unitId);
															}
															$rootScope.stopLoader();
															}
														// $scope.init();
														//$modalInstance.close();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.donorImage=null;
														$scope.donorObj={};
														$scope.donorImage="";
														$scope.datepickerExpiry="";
														$scope.age="";
														$scope.datepicker="";
														$scope.filepreview="";
														$scope.donorObj.countryId="";
														$scope.donorObj.stateId="";
														$scope.donorObj.districtId="";
														$scope.donorObj.cityId="";
														$scope.donorObj.areaId="";
														$scope.sameAsLocal='Y';
														$scope.donorObj.nationalityId="";
														$state.reload();
															
													});
									 }
									 else{
										var URI = BASE_URL + ROOT_URL
												+ SAVE_DONOR_REGISTRATION_MASTER;
										console.log($scope.donorObj);
										//return false;
										$scope.donorObj.patientId=null;
										GenericService.serviceAction("POST", URI, $scope.donorObj).then(function(response) {
									
															console.log("save response:",response);
															//$rootScope.startLoader();
															if (response.data.status == "success")
															{
																if($scope.donorImage!=undefined &&  !angular.equals({}, $scope.donorImage))
																{
																  $scope.uploadDonorImage($scope.donorImage,response.data.object.donorId,$scope.organizationId,$scope.unitId);
																}
																if($scope.snapshotData!=undefined && $scope.snapshotData!="")
																{
																  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getYear()+".jpeg");
																  $scope.uploadDonorImage(file,response.data.object.donorId,$scope.organizationId,$scope.unitId);
																}
																growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
																$scope.donorObj={};
																$scope.donorImage="";
																$scope.datepickerExpiry="";
																$scope.age="";
																$scope.datepicker="";
																$scope.filepreview="";
																$scope.donorObj.countryId="";
																$scope.donorObj.stateId="";
																$scope.donorObj.districtId="";
																$scope.donorObj.cityId="";
																$scope.donorObj.areaId="";
																$scope.sameAsLocal='Y';
																$scope.donorObj.nationalityId="";
																$scope.donorImage=null;
																$rootScope.stopLoader();
															}
															else if (response.data.status == 'error') {
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
															   }
															$rootScope.stopLoader();
														});
								}
										
								//}
							}
							
							
							
							
							/*
							$scope.selectGender = function(id)
							{
								
								var genId = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].id;
								
								$scope.donorObj.genderId = genId;
								
							}	*/
						  
							
						
							$scope.searchPatient = function()
							{
								
								var ConDate = $('#datepickerSearch').val();
								if(ConDate != '')
								$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
								
								 $scope.patientSearchObj.registrationTypeId = 1;
									//return false;
								 $rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL
												+ GETTEMPORYPATIENTFORPREREGISTRATION;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.patientSearchObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.searchPatientList = [];
															if (response.data.status == "success")
																$rootScope.stopLoader();
																$scope.searchPatientList = response.data.listObject;
															
															console.log("$scope.searchPatientList",$scope.searchPatientList);
															if($scope.searchPatientList.length == null || $scope.searchPatientList.length ==0){
																$rootScope.stopLoader();
																growl.warning("No Result Found!",{
																	title : ERROR_MSG
																});
																$scope.patientSearchObj={};
															}
														});
							}
							
							
							//$scope.chkStatus=true;
							$scope.selectItem = function(item) {
								console.log("yyy",item);
								//$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.searchSelectPatient) {
									$scope.donorObj.patientId = item.patientId;
									$scope.donorObj.donorId=item.donorId;
									$scope.donorObj.bloodGroupId=item.bloodGroupId;
									// $scope.donorObj.bloodGroupId=item.bloodGroupId;
									console.log("$scope.donorObj.bloodGroupId---------------",$scope.donorObj.bloodGroupId);
									$scope.donorObj.donoTypeId=item.donoTypeId;
									$scope.donorObj.firstName = item.firstName;
									$scope.donorObj.mobileNumber = item.mobileNumber;
									$scope.donorObj.uhidNumber = item.uhidNumber;
									$scope.donorObj.donorNumber = item.donorNumber;
									$scope.donorObj.donorDetailsId=item.donorDetailsId;
									$scope.donorObj.organizationId = $scope.organizationId;
									$scope.donorObj.unitId = $scope.unitId;
									$scope.donorObj.updatedBy = $scope.userId;
									$scope.donorObj.isAutologous = item.isAutologous;
									$scope.donorObj.createdBy = $scope.userId;
									$scope.donorObj.status = 'A';
									$scope.donorObj.patientCategoryId='1';
									$scope.donorObj.exportedToScreening = item.exportedToScreening;
									if($scope.donorObj.patientId == null)
									{
										$scope.chkStatus=true;
									}else if($scope.donorObj.patientId !=0 )
									{
										$scope.chkStatus=false;
									}
									$scope.donorObj.genderId = item.genderId;
									
									$scope.age = item.ageString;
									//alert(new Date(item.dob));
									$scope.datepicker =  new Date(item.dob);
									$scope.donorObj.address = item.address;
									//$scope.donorObj.aliseName = item.aliseName;
									
									if(item.countryId != '' ){
										$scope.donorObj.countryId = item.countryId;
										
									}
									
									if(item.stateId != ''){
										$scope.getStateByCountry(item.countryId,'L');
										$scope.donorObj.stateId = item.stateId;
									}
									
									if(item.districtId != ''){
										$scope.getDistrictByState(item.stateId,'L');
										$scope.donorObj.districtId = item.districtId;
									}
									
									if(item.cityId != '')
										$scope.getCityByDistrict(item.districtId,'L');
										$scope.donorObj.cityId = item.cityId;
									
									if(item.areaId != ''){
										$scope.getAreaByCity(item.cityId,'L');
										$scope.donorObj.areaId = item.areaId;
									}
									
									if(item.companyCountryId != null){
										//$scope.donorObj.companyCountryId = item.companyCountryId;
									}
									
									if(item.companyStateId != null){
										
										$scope.getStateByCountry(item.companyCountryId,'C');
									//	$scope.donorObj.companyStateId = item.companyStateId;
									}
									

									if(item.companyDistrictId != null){
										$scope.getDistrictByState(item.companyStateId,'C');
									//	$scope.donorObj.companyDistrictId = item.companyDistrictId;
									}
									
									if(item.companyCityId != null){
										$scope.getCityByDistrict(item.companyDistrictId,'C');
									//	$scope.donorObj.companyCityId = item.companyCityId;
									}
									
									if(item.companyAreaId != null){
										$scope.getAreaByCity(item.companyCityId,'C');
									//	$scope.donorObj.companyAreaId = item.companyAreaId;
									}
									
									if(item.permanentCountryId != ''){
									//	$scope.donorObj.permanentCountryId = item.permanentCountryId;
									}
									
									if(item.permanentStateId != ''){
										$scope.getStateByCountry(item.permanentCountryId,'LTOP');
										$scope.donorObj.permanentStateId = item.permanentStateId;
									}
									
									if(item.permanentDistrictId != ''){
										$scope.getDistrictByState(item.permanentStateId,'LTOP');
										$scope.donorObj.permanentDistrictId = item.permanentDistrictId;
									}
									
									if(item.permanentCityId != ''){
										$scope.getCityByDistrict(item.permanentDistrictId,'LTOP');
										$scope.donorObj.permanentCityId = item.permanentCityId;
									}
									
									if(item.permanentAreaId != ''){
										$scope.getAreaByCity(item.permanentCityId,'LTOP');
										$scope.donorObj.permanentAreaId = item.permanentAreaId;
									}
									
									if(item.maritalStatusId != '')
										$scope.donorObj.maritalStatusId = item.maritalStatusId;
									
									if(item.nationalityId != '')
										$scope.donorObj.nationalityId = item.nationalityId;
									
									
									if(item.raceId != '')
										$scope.donorObj.raceId = item.raceId;
									
								
										if(item.prefixId != '')
											$scope.donorObj.prefixId = item.prefixId;
									   if(item.genderId != '')
										$scope.donorObj.genderId = item.genderId;
										if(item.identificationExpiryDate != ''){
											$scope.datepickerExpiry = new Date(item.identificationExpiryDate);
											
										}
									
								
								/*	$scope.donorObj.companyAddress = item.companyAddress;
									$scope.donorObj.companyMobileNumber = item.companyMobileNumber;
									$scope.donorObj.companyName = item.companyName;
									$scope.donorObj.companyZipCode = item.companyZipCode;*/
									$scope.donorObj.countryCallingCode = item.countryCallingCode;
									$scope.donorObj.email = item.email;
									$scope.donorObj.identificationNumber = item.identificationNumber;
		                           
								//	$scope.donorObj.isVip = item.isVip;
									$scope.donorObj.lastName = item.lastName;
									$scope.donorObj.middleName = item.middleName;
									$scope.donorObj.permanentAddress = item.permanentAddress;
									$scope.donorObj.permanentPhoneCode = item.permanentPhoneCode;
									$scope.donorObj.permanentPhoneNumber = item.permanentPhoneNumber;
									$scope.donorObj.permanentZipCode = item.permanentZipCode;
									$scope.donorObj.phoneCode = item.phoneCode;
									$scope.donorObj.phoneNumber = item.phoneNumber;
									$scope.donorObj.zipCode = item.zipCode;
//									$scope.donorObj.identificationTypeId = item.identificationTypeId+'_'+item.isExpiryRequired;
									if(item.identificationTypeId > 0){
										$scope.donorObj.identificationTypeId = item.identificationTypeId+'_'+item.isExpiryRequired;
										}
										else
											{
											  $scope.donorObj.identificationTypeId = null;
											}
									$scope.donorObj.occupationId = item.occupationId;
									if(item.isVip == 'Y'){
									$scope.aliseFlag = true;
								//	$scope.donorObj.aliseName = item.aliseName;
									
									if(item.isExpiryRequired == 'Y')
										$scope.expiryDateDisabled = false;
									}
									
									   $scope.filepreview = item.imagePath;
								}
							}
							
							
							
							$scope.searchDonor=function(){
								var ConDate = $('#datepickerSearch').val();
								if(ConDate != '')
								$scope.donorSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
								
								 $scope.donorSearchObj.registrationTypeId = 1;
								
								 $scope.donorSearchObj.organizationId=$scope.organizationId;
								 $scope.donorSearchObj.unitId=$scope.unitId;
									//return false;
								 $rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL
												+ SEARCH_DONOR;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.donorSearchObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.searchDonorList = [];
															if (response.data.status == "success")
																$rootScope.stopLoader();
																$scope.searchDonorList = response.data.listObject;
															
															console.log("$scope.searchDonorList",$scope.searchDonorList);
															if($scope.searchDonorList.length == 0){
																$rootScope.stopLoader();
																growl.warning("No Result Found!",{
																	title : ERROR_MSG
																});
															}
															$scope.donorSearchObj={};
														});
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
					                        $scope.donorObj.thumbImage = res.data.BitmapData;
											//alert(res.data.BitmapData);
					                    }
					                }
					                else {
					                 //   alert(res.err);
					                	console.log("res.err",res.err);
					                }
					            }
					            catch (e) {
					              //  alert(e);
					            	console.log("e",e);
					            }
					            return false;
					        }
							
							$scope.setPostCode = function(areaId,flag){
								
								 if(flag ==0 ){
									 
									 angular.forEach($scope.areaList,function(value,key){
											
											if(value.areaId==areaId)
											{
												$scope.donorObj.zipCode = value.postCode;
											}
										});
								 }else{
									 angular.forEach($scope.permanentAreaList,function(value1,key){
											
											if(value1.areaId==areaId)
											{
												$scope.donorObj.permanentZipCode = value1.postCode;
											}
										});
									 
								 }
							}	
					

						} ]);



