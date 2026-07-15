'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:deathRegistrationController
 * @description #deathRegistrationController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'deathRegistrationController',
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
								$cookies, $rootScope, GenericService, BillingGenericService, GenericServiceFileUpload, growl,
								$state, $stateParams) {

							/* init() function for form object create. */
							$scope.init = function() {
								
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}

                                var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));

								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
								if (cookieObjectAppConfigSecond != undefined) {
								$scope.registrationFieldMobileDigit = cookieObjectAppConfigSecond.mobileDigit;
								}

								//for datepicker 1
								$scope.dateOptions1 = {
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
									
									
									// for datePicker2 expiry
									$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false
									};
									$scope.openDatePickerPdd = function() {
										$scope.datepickerPdd.opened = true;
									};

									$scope.datepickerPdd = {
										opened : false
									};
									

									//for datepicker 3 expected
									$scope.dateOptions = {
											formatYear : 'yyyy',
											showWeeks : false
										};

										$scope.openDatePickerExpected = function() {
											$scope.datepickerExpected.opened = true;
										};

										$scope.datepickerExpected = {
											opened : false
										};
							
								$scope.docList = [ {
									documentTypeId : 0,
									documentPath : ''
								} ];
								
								$scope.deathObject = {
									firstName : '',
									lastName : '',
									genderId:0,
									markOnDecease:'',
									dateOfDeath:'',
									durationFormat:0,
									durationValue:0,
									isMedicoLegal:'N',
									expectedArrivalTime :'',
									expectedArrivalDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									broughtFrom : '',
									deathReason : '',
									prefixId : 0,
									address : '',
									countryId : $scope.unitCountryId,
									stateId : $scope.unitStateId,
									districtId : $scope.unitDistrictId,
									cityId :$scope.unitCityId,
									areaId : $scope.unitAreaId,
									zipCode : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdBy:$scope.userId,
									updatedBy:$scope.userId,
									status:'A',
									mortuaryStatusId :1,
									visitTypeId : 1
									
									
								};
								
								$scope.kinObject = {
										prefixId : '',
										kinName : '',
										relationId : '',
										mobileNo : '',
										phoneNo : '',
										expiry : '',
										identificationId : '',
										identificationNo : '',
										isGuarantor : '',
										address : '',
										countryId : $scope.unitCountryId,
										stateId : $scope.unitStateId,
										districtId : $scope.unitDistrictId,
										cityId :$scope.unitCityId,
										areaId : $scope.unitAreaId,
										postCode : ''
									};


							
							
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

							
								$scope.expiryDateDisabled = true;
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
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.genderList = [];
											if (response.data.status == "success") {
												$scope.genderList = response.data.listObject;

											}
										});
						

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPREFIXLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.prefixList = [];

													if (response.data.status == "success") {
														$scope.prefixList = response.data.listObject;

													}
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
										+ GETACTIVEIDENTIFICATION;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.identificationList = [];

													if (response.data.status == "success") {
														$scope.identificationList = response.data.listObject;

													}
												});
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.countryList = [];

													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;
														//console.log($scope.countryList);

													}
													$scope.kinObject.countryId = 1;
													$scope.deathObject.countryId = 1;
													$scope.getStateByCountry($scope.kinObject.countryId);
												});
								
									var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.documentTypeList = [];
											if (response.data.status == "success") {
												$scope.documentTypeList = response.data.listObject;
											}
										});

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
									

							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDeathRegistration();
							}

							// for get state by country ID
							$scope.getStateByCountry = function(id) {
								var data = {
									countryId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETSTATELISTBYCOUNTRYID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.stateList = [];

													if (response.data.status == "success") {
														$scope.stateList = response.data.listObject;
														//console.log($scope.stateList);

													}
													$scope.kinObject.stateId = 1;
													$scope.deathObject.stateId = 1;
													$scope.getDistrictByState($scope.kinObject.stateId);
												});

							}

							$scope.getDistrictByState = function(id) {
								var data = {
									stateId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDISTRICTLISTBYSTATEID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.districtList = [];

													if (response.data.status == "success") {
														$scope.districtList = response.data.listObject;
														//console.log($scope.districtList);

													}
													$scope.kinObject.districtId = 1;
													$scope.deathObject.districtId = 1;
													$scope.getCityByDistrict($scope.kinObject.districtId);
												});

							}

							$scope.getCityByDistrict = function(id) {

								var data = {
									districtId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETCITYLISTBYDISTRICTID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.cityList = [];

													if (response.data.status == "success") {
														$scope.cityList = response.data.listObject;
														//console.log($scope.cityList);

													}
													$scope.kinObject.cityId = 1;
													$scope.deathObject.cityId = 1;
													$scope.getAreaByCity($scope.kinObject.cityId);
												});

							}
							$scope.getAreaByCity = function(id) {

								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.areaList = [];

													if (response.data.status == "success") {
														var areaData = response.data.listObject;
                                                        var array = areaData
                                                        var filtered = array.filter(function (el) {
                                                          return el.areaName != "";
                                                        });
                                                        $scope.areaList=filtered;
													}
												});

							}

							$scope.add = function(element) {
								var flagValue = $scope.validateAndUpload(element);
								if(flagValue == false){
									return;
								}
								var data = {
									documentTypeId : $scope.deathObject.documentTypeId,
									documentPath : element.files[0].name,
									file : element.files[0]
								};
								//console.log(data);
								$scope.docList.push(data);
								$scope.deathObject.document = '';
							};

							$scope.remove = function() {
								var newDataList = [];
								var le = $scope.dataList.length;
								var i = 1;
								angular.forEach($scope.dataList, function(v) {
									if (le != i) {
										newDataList.push(v);
									}
									i++;
								});
								$scope.docList = newDataList;
							};
							
							$scope.getById = function(id){
								var name = "";
								angular.forEach($scope.documentTypeList, function(value, key) {
									if (value.documentTypeId == id) {
										name = value.documentTypeName;
									}
								});
								return name;
							}
							
							$scope.disableEnableExpiry = function(value)
							{
								if(value == 4 || value == 1 || value == 2005)
								{
									$scope.expiryDateDisabled = false;
								}
								else
									{
									 $scope.expiryDateDisabled = true;
									}
							}
							

							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}
							
							$scope.saveDeathRegistration = function() {
								
								if($scope.deathObject.prefixId == '' || $scope.deathObject.prefixId == undefined || $scope.deathObject.prefixId == null){
									growl.warning('Please Select Prefix...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.deathObject.firstName == '' || $scope.deathObject.firstName == undefined || $scope.deathObject.firstName == null){
									growl.warning('Please Enter First Name ...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.lastName == '' || $scope.deathObject.lastName == undefined || $scope.deathObject.lastName == null){
									growl.warning('Please Enter Last Name ...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.genderId == '' || $scope.deathObject.genderId == undefined || $scope.deathObject.genderId == null){
									growl.warning('Please Select Gender ...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.age == '' || $scope.deathObject.age == undefined || $scope.deathObject.age == null){
									growl.warning('Please Enter Age...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.markOnDecease == '' || $scope.deathObject.markOnDecease == undefined || $scope.deathObject.markOnDecease == null){
									growl.warning('Please Enter Mark On Decease...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.datepicker == '' || $scope.datepicker == undefined || $scope.datepicker == null){
									growl.warning('Please Select Death Date Mark...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.deathReason == '' || $scope.deathObject.deathReason == undefined || $scope.deathObject.deathReason == null){
									growl.warning('Please Enter Death Reason...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.countryId == '' || $scope.deathObject.countryId == undefined || $scope.deathObject.countryId == null){
									growl.warning('Please Select Country...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.countryId == '' || $scope.deathObject.countryId == undefined || $scope.deathObject.countryId == null){
									growl.warning('Please Select Country...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.stateId == '' || $scope.deathObject.stateId == undefined || $scope.deathObject.stateId == null){
									growl.warning('Please Select State...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.districtId == '' || $scope.deathObject.districtId == undefined || $scope.deathObject.districtId == null){
									growl.warning('Please Select District...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.cityId == '' || $scope.deathObject.cityId == undefined || $scope.deathObject.cityId == null){
									growl.warning('Please Select City...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if($scope.deathObject.areaId == '' || $scope.deathObject.areaId == undefined || $scope.deathObject.areaId == null){
									growl.warning('Please Select Area...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
					
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								
								$scope.dataList = [];
								
								$scope.kinObject.expiry =moment($scope.datepickerPdd).format('DD-MM-YYYY HH:mm:ss');
								$scope.dataList.push($scope.kinObject);
								
								$scope.deathObject.dateOfDeath = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
								
								$scope.deathObject.expectedArrivalDate = moment($scope.datepickerExpected).format('DD-MM-YYYY HH:mm:ss');
								$scope.deathObject.documentTypeId = parseInt($scope.deathObject.documentTypeId);
								$scope.deathObject.durationValue = parseInt($scope.deathObject.durationValue);
								
								
								if($scope.deathObject.durationFormat == '1')
									{
									$scope.deathObject.durationValue = $scope.timeValueOfDuration;
									}
								else
									{
									$scope.deathObject.durationValue = $scope.timeValueOfDay;
									}
								
								$scope.deathObject.createdDate = $scope.currentDate;
								$scope.deathObject.updatedDate = $scope.currentDate;
								if($scope.dataList.length > 0){
									if($scope.dataList[0].prefixId == '' || $scope.dataList[0].prefixId  == undefined || $scope.dataList[0].prefixId  == null){
										growl.warning('Please Enter Kin Prefix...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
									if($scope.dataList[0].kinName == '' || $scope.dataList[0].kinName  == undefined || $scope.dataList[0].kinName  == null){
										growl.warning('Please Enter Kin Name...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
									if($scope.dataList[0].relationId == '' || $scope.dataList[0].relationId  == undefined || $scope.dataList[0].relationId  == null){
										growl.warning('Please Enter Kin Relation ...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
									if($scope.dataList[0].mobileNo == '' || $scope.dataList[0].mobileNo  == undefined || $scope.dataList[0].mobileNo  == null){
										growl.warning('Please Enter Mobile No ...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
									else if($scope.dataList[0].mobileNo.length > $scope.registrationFieldMobileDigit || $scope.dataList[0].mobileNo.length < $scope.registrationFieldMobileDigit){
                                        growl.warning('Please Enter Valid Mobile No ...!!',
                                        {
                                            title : 'Warning!'
                                        });
                                        return false;
									}

									if($scope.dataList[0].mobileNo !== undefined && $scope.dataList[0].mobileNo !== null && $scope.dataList[0].mobileNo !== ''){
									    if($scope.dataList[0].mobileNo.length > $scope.registrationFieldMobileDigit || $scope.dataList[0].mobileNo.length < $scope.registrationFieldMobileDigit){
                                            growl.warning('Please Enter Valid Phone No ...!!',
                                            {
                                                title : 'Warning!'
                                            });
                                            return false;
                                        }
									}
								}
								$scope.deathObject.kinArray = $scope.dataList;
								
								//$scope.deathObject.docArray = $scope.docList; 
								/*console.log($scope.deathObject);
								return;*/
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + SAVEDEATHREGISTATION;
								GenericService.serviceAction("POST", URI,$scope.deathObject).then(
												function(response) {
													//console.log(response.data.object);
													if (response.data.status == "success") {
														growl.success('Death Registration Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$rootScope.stopLoader();
														var savedData = {};
														savedData = response.data.object;
														angular.forEach($scope.docList, function(v,i) {
															if($scope.docList[i].documentTypeId != 0 && $scope.docList[i].documentTypeId != ''){
																var fd = new FormData();
																 fd.append('encounterId', 0);
																 fd.append('visitTypeId', 0);
																 fd.append('admissionId', 0);
																 fd.append('patientId', 0);//response.data.object.dPatientId 
																 fd.append('documentTypeId', $scope.docList[i].documentTypeId);
																 fd.append('createdBy',$scope.userId );
																 fd.append('updatedBy', $scope.userId);
																 fd.append('unitId', $scope.unitId);
																 fd.append('organisationId', $scope.organizationId);
																 fd.append('file',$scope.docList[i].file );
																 fd.append('expenseId', 0);
																 fd.append('grnId',0);
																 fd.append('poId',0);
																 fd.append('incidentReportingMapperId',0);
																 fd.append('companyExpenseId',0);
																 fd.append('deathPatientId',savedData.dPatientId);
																 fd.append('createdDate',null);
																 fd.append('fileEditedName',null);
																 fd.append('opbId',0);
																 fd.append('vendorId',0);
																 var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
																 GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
																 {	
																 return response;
																 });
															}

														});
														/*growl.success('Death Registration sucessfully...!!',{title : 'Success!'});*/
														$scope.init();
													}
													 else if (response.data.status == 'error') {
														 $rootScope.stopLoader();
															growl.error(response.data.message,
																{
																	title: 'Error!'
																});
														}else {
														$rootScope.stopLoader();
														growl.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}

												});
								}

							}

							$scope.validateAndUpload = function(input) {

									var file = input.files[0];

									if (!file) {
										return false;
									}

									var allowedExtensions = ['pdf', 'jpg', 'jpeg', 'png'];

									var allowedMimeTypes = [
										'application/pdf',
										'image/jpeg',
										'image/png'
									];

									// Max size 100 MB
									var maxSize = 100 * 1024 * 1024;

									// Extract extension
									var fileName = file.name;
									var extension = fileName.split('.').pop().toLowerCase();

									// Validate extension
									if (allowedExtensions.indexOf(extension) === -1) {
										growl.error("Invalid file extension.", {title: 'Error!'});
										input.value = "";
										return false;
									}

									// Validate MIME type
									if (allowedMimeTypes.indexOf(file.type) === -1) {
										growl.error("Invalid file type.", {title: 'Error!'});
										input.value = "";
										return false;
									}

									// Validate file size
									if (file.size > maxSize) {
										growl.error("File size exceeds 100 MB.", {title: 'Error!'});
										input.value = "";
										return false;
									}


								};														
							
							 
							
						} ]);
