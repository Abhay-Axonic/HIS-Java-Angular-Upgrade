'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:staffMasterController
 * @description #staffMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'staffRegistrationController',
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
						'$timeout',
						'promiseFactoryWithObject',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService,GenericServiceFileUpload, growl, $state,
								$stateParams,$timeout,promiseFactoryWithObject,PagerService) {

							/* init() function for form object create. */
							$scope.init = function() {
								
								
								
								$scope.filepreview = "";
								$scope.staffRegistrationFlag  = false;
								$scope.addressFlag =false;
								$scope.sameAsLocal='N';
								$scope.numberToDisplay = 10;
								$scope.numberToDisplayPer = 10;

								//for datepicker 1
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,

								};
								$scope.dateOptionsDob = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate: new Date()
									};
								$scope.openDatePicker = function() {
									$scope.datepicker1.opened = true;
								};

								$scope.datepicker1 = {
										opened : false
									};
								$scope.openDatePicker1 = function() {
									$scope.pwdExpiry.opened = true;
								};
								
								$scope.pwdExpiry = {
									opened : false
								};
								
								// for datePicker2
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
//									maxDate: new Date()
								};
								$scope.openDatePickerExpiry = function(index) {
								     //console.log(event)
									//$scope.datepickerExpiry1.opened = true;
									for (var i = $scope.idList.length - 1; i >= 0; i--) {
                                        if (i === index) {
                                            $scope.datepickerExpiry1.opened[i] = true;
                                        } else {
                                            $scope.datepickerExpiry1.opened[i] = false;
                                        }
                                     }
								};

								$scope.datepickerExpiry1 = {
									opened : [false]
								};
								
								
								//for date of joining
								$scope.openDatePickerDOJ = function() {
									$scope.datepickerDOJ1.opened = true;
								};

								$scope.datepickerDOJ1 = {
									opened : false
								};
								
								
								
								$scope.docLength = 0;
								$scope.lengthForDoc= function(identificationTypeId){
									angular.forEach($scope.identificationList, function(value){
										if(value.identificationId == parseInt(identificationTypeId[0])){
											$scope.docLength = value.definedLength;
										}
									});
						}
								
								//for next of kin
								$scope.openDatePickerDob = function(index) {
									// alert("in");
									// $scope.expiry.opened = true;
									for (var i = $scope.dataList.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.dobKin.opened[i] = true;
										} else {
											$scope.dobKin.opened[i] = false;
										}
									}
								};

								$scope.dobKin = {
									opened : [ false ],

								};
								
								
								/**Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.commonList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								
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
//								$scope.unitAreaId = cookieObject.unitAreaId;							
								$scope.isSuperUser=cookieObject.isSuperUser;


								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								$scope.fieldsDisable = false;
								$scope.shouldHideDiv=false;
								$scope.aliseFlag = false;
								
								$scope.expiryDateDisabled = true;
								
								$scope.searchType = 0;
								
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
								$scope.dataList = [];
								$scope.tab=1;
								
								$scope.ph_numbr = /^\+?\d{10}$/;
						        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
						        
						        $scope.employeeSearchObj = {
						        		"employeeName":'',
						        		"specialityId":'0',
						        		"empDesignationId":'0',
						        		"empCategoryId":'0',
						        		"organizationId":$scope.organizationId,
						        		"unitId" : $scope.unitId,
						        		"typeId" : 2,
						        		"identificationTypeId":0
						        }
								
								$scope.employeeObj = {

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
									datepickerExpiry : '',
									fileName : '',
									isVip : 'N',
									address : '',
									countryId : '',
									stateId : '',
									districtId : '',
									cityId : '',
									areaId : '',
									zipCode : '',
									phoneCode : '',
									phoneNumber : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
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
									employeeId:'',
									employeeDetailsId:'',
									specialityId:'',
									subSpecialityId:'',
									empCategoryId:'',
									empDesignationId:'',
									empCode:'',
									dateOfJoining:'',
									accessCardNumber:'',
									bankName:'',
									accountNumber:'',
									bankBranchName:'',
									ifscCode:'',
									barCode:'',
									vipRemark:'',
									typeId:'2',
								    isUser:'Y',
								    userId:'',
								    employeeImage:'',
								    registrationNumber:'',
								    pwdExpiry:'',
								    labPin:'',
								    selectedUserUnitList:[],
								    selectedHisRoleList:[],
								    selectedOTIMSRoleList:[]
								    
								};
								
								//For Kin Object
								$scope.dataList = [ {
									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									mobileNo : '',
									genderId : '',
									dobKin : '',
									occupationId : '',
									relationId : '',
									isNok : 'N',
									typeId:'2',
									identificationTypeId:'',
									identificationNumber:'',
									createdDate:moment(new Date())
									.format('DD-MM-YYYY HH:mm:ss'),
									createdBy:$scope.userId,
									updatedDate:moment(new Date())
									.format('DD-MM-YYYY HH:mm:ss'),
									updatedBy:$scope.userId,
									status:'A',
									organizationId:$scope.organizationId,
									unitId:$scope.unitId
									
								} ];
								

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var commonObj1 = {
									organizationId : $scope.organizationId
								};
								
								var commonObj2 = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										typeId : '2'
									};
								var data = {};
								
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
								+ GETSPECIALITYLIST;
					
						BillingGenericService
								.serviceAction(METHOD_POST, URI,
										commonObj1,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.specialityList = response.data.listObject;
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

								
								
								
								var data = {
										organizationId : $scope.organizationId,
					
								}

								var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getReportGroupHeaderForUserRoleManagement";

								GenericService.serviceAction("POST", URI, data).then(function(response) {

								$rootScope.stopLoader();
								$scope.getHeaderReportList = [];
								$scope.getHeaderReportList = response.data.listObject;
								
								
								var prevId = 0;
								var tempObj = null;
								var tempList = [];
								var finalList = [];
								angular.forEach($scope.getHeaderReportList, function(value , key) {
									
									
									var reportIds = null;
									var reportIdList = [];
									angular.forEach(value.listReportDetails, function(value1 , key) {
										reportIds = {
												 reportId:value1.reportId
										 }
										reportIdList.push(reportIds);
									});
									
									 tempObj = {
									 			reportGroupHeaderId:value.reportGroupHeaderId,
									 			selectedReprtId : reportIdList
							 			   };
									
									 finalList.push(tempObj)
									
								});
								//console.log("getHeaderReportList",JSON.stringify(finalList));
								$scope.defaultMISRights = JSON.stringify(finalList);
								
								});
								
								
								$scope.getHeaderTabList=[];
								var finalList1 = [];
								var URI = BASE_URL + ROOT_URL + "/api/opd/getActiveClinicalHeaderTabList";
								var data = {}
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if(response.data.status == "success"){
									$scope.getHeaderTabList = response.data.listObject;
									angular.forEach($scope.getHeaderTabList ,function(value,index){
										if(index == 0){
											 value.defualtTab = 'Y';
										}else{
											 value.defualtTab = 'N';
										}
										var subTabIds = null;
										var selectedSubTabId = [];
										angular.forEach(value.listSubTabDetails, function(value1 , index1) {
											
												 value1.defualtSubTab = 'N';
											
											subTabIds = {
													subTabId:value1.subTabId,
													defualtSubTab:value1.defualtSubTab, 
											 }
											selectedSubTabId.push(subTabIds);
										});
										
										var tempObj = {
												    headerId:value.headerId,
												    defualtTab:value.defualtTab,
												    selectedSubTabId :selectedSubTabId
								 			   };
										
										 finalList1.push(tempObj)
									});
									$scope.defaultCoversheetRights = JSON.stringify(finalList1);
									console.log("finalList1",finalList1);
									console.log("defaultCoversheetRightst",$scope.defaultCoversheetRights);
									console.log("json tab coversheet",$scope.getHeaderTabList);
									}
									});
								
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
							
					//getting default field configuration start
								
							}

							$scope.init();
							
							$scope.specialityIdsCheck = [];
							$scope.subSpecialityListCheck = [];
							$scope.getSubSpecialityListCheckBox = function(item){
								
								if(item.selectedDepts == 'Y')
									{
										if($scope.specialityIdsCheck.indexOf(item.specialityId) !== -1) {
										}else{
											$scope.specialityIdsCheck.push(item.specialityId);
										}
									
									}else{
										var index = $scope.specialityIdsCheck.indexOf(item.specialityId);
										$scope.specialityIdsCheck.splice(index, 1);
									}
								  if($scope.specialityIdsCheck != undefined && $scope.specialityIdsCheck != null && $scope.specialityIdsCheck.length > 0){
								    var data = {
                                                organizationId : $scope.organizationId,
                                                specialityIds : $scope.specialityIdsCheck
                                            };

                                            var URI = BASE_URL + ROOT_URL
                                                    + "/adt/getSubSpecialityBySpecialityList";


                                                BillingGenericService
                                                        .serviceAction(METHOD_POST, URI, data,
                                                                NOTIFICATION_MSG_STATUS_FALSE)
                                                        .then(
                                                                function(response) {
                                                                    console.log(response);

                                                                    // $rootScope.startLoader();
                                                                    if (response.data.status == "success"){
                                                                        //$scope.subSpecialityListCheck = response.data.listObject;
                                                                        $scope.subSpecialityListCheckTemp = [];
                                                                        var i = 1;

                                                                          angular.forEach(response.data.listObject, function(value , key) {
                                                                              var flg = false;
                                                                              if($scope.subSpecialityListCheck.length > 0){
                                                                                  var k = 1;
                                                                              angular.forEach($scope.subSpecialityListCheck, function(value1 , key1) {
                                                                                    if(value.subSpecialityMasterId == value1.subSpecialityMasterId)
                                                                                        {
                                                                                        flg = true;
                                                                                          $scope.subSpecialityListCheckTemp.push(value1);
                                                                                        }
                                                                                    if(k == $scope.subSpecialityListCheck.length && flg == false)
                                                                                        {
                                                                                         $scope.subSpecialityListCheckTemp.push(value);
                                                                                        }
                                                                                    k++;
                                                                              });
                                                                          }else{
                                                                              $scope.subSpecialityListCheckTemp.push(value);
                                                                          }

                                                                              if(i == response.data.listObject.length){
                                                                                  $scope.subSpecialityListCheck= [];
                                                                                  $scope.subSpecialityListCheck =  $scope.subSpecialityListCheckTemp;
                                                                              }

                                                                             i++;
                                                                          });
                                                                    }

                                                                });
								        }
								        else{
                                         	$scope.subSpecialityListCheck = [];
                                         	}
								

								
							}


							                      // for added addIdentification
                            								$scope.idList = [];
                            								var data = {
                            										identificationTypeId : "",
                            										identificationNumber : "",
                            										identificationExpiryDate :undefined,
                            										organizationId:$scope.organizationId,
                            										unitId:$scope.unitId
                            								};
                            								$scope.idList.push(data);

                            								$scope.addIdentification = function(index) {
                            								    $scope.employeeObj.identificationTypeId="";
                            								    $scope.employeeObj.identificationNumber="";
                            									var data = {
                            											identificationTypeId : $scope.employeeObj.identificationTypeId,
                            											identificationNumber : $scope.employeeObj.identificationNumber,
                            											organizationId:$scope.organizationId,
                            											unitId:$scope.unitId
                            									};
                            									var len = $scope.idList.length;

                            									angular.forEach($scope.idList,function(value,index1){
                            										if(index == index1){
                            											if(value.identificationTypeId == "")
                            											{
                            												growl.warning("Please Enter identificationTypeId.",
                            				    	    							{
                            				    	    							title : 'Warning!'
                            				    	    							});
                            												return false;
                            											}
                            											else{
                            												$scope.idList.push(data);
                            											}
                            										}
                            									});
                            								};
                            								//remove
                            								$scope.removeList = function(index) {
                            									var newList = [];
                            									var le = $scope.idList.length;
                            									var i = 1;
                            									if(le != i){
                            									angular.forEach($scope.idList, function(v) {
                            										if (index != i) {
                            											newList.push(v);
                            										}
                            										i++;
                            									});

                            									$scope.idList = newList;
                            									}

                            									   if(le==index){
                                                                      if($scope.idList.length==le){
                                                                           angular.forEach($scope.idList, function(v) {
                                                                           v.identificationTypeId="";
                                                                           v.identificationNumber="";
                                                                           v.identificationExpiryDate="";
                                                                           v.fileName="";
                                                                           angular.element("input[type='file']").val(null);
                                                                          });
                                                                                }
                                                                            }

                            								};


                            								$scope.getIdentifDeta = function (employeeId){
                            									var data1={
                            											"organizationId":$scope.organizationId ,
                            										     "unitId":$scope.unitId,
                            										     "employeeId":employeeId ,
                            									}
                            									var URI = BASE_URL + ROOT_URL+ "/api/global/getListOfDoctorIdentificationDocuumentList";
                            									GenericService.serviceAction("POST", URI, data1).then(function(response) {
                            										if(response.data.listObject != null && response.data.listObject.length > 0){	
                            											$scope.idList=[];
                            										 angular.forEach(response.data.listObject, function(value,key) {
                            											 $scope.idList.push(value);
                            										 });
                            										 angular.forEach($scope.idList, function(value,key) {
                            											 angular.forEach($scope.identificationList, function(value1,key1) {
                            												 if(value.identificationTypeId == value1.identificationId)
                            												 value.identificationTypeId = value1.identificationId+'_'+value1.isExpiryRequired
                            											 });
                            											 $scope.disableEnableExpiry(value);
                            										 });
                            										console.log("idList",$scope.idList);
                            										}
                            									});
                            									
                            								}
                            								
                            								
                            								$scope.removeDoc = function(info){
                            									
                            									$scope.list = $scope.idList;
                            									
                            									angular.forEach($scope.list, function(item, index){
																	if(item.userIndetificationDetailsId == info.userIndetificationDetailsId){
																		item.doctorIndetificationFile = '';
																		item.fileName = '';
																	}
																});
                            									   
                            									$scope.idList;
                            									var data={
                            											userIndetificationDetailsId:$scope.userIndetificationDetailsId,
                            											organizationId:$scope.organizationId,
                            											unitId:$scope.unitId,
                            									};
                            									
                            									$scope.removeClicked = true;

                                							}
                            								
                            								
                            								$scope.cancelStaffUploadIdentification = function(id,index){
                                                            					        	if(id == undefined){
                                                            					        		$scope.removeList(index);
                                                            					        	}else{
                                                            								$scope.userIndetificationDetailsId=id;
                                                            								$rootScope.startLoader();
                                                            								try{
                                                            									var date = new Date();
                                                            									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
                                                            									var data={
                                                            											userIndetificationDetailsId:$scope.userIndetificationDetailsId,
                                                            											organizationId:$scope.organizationId,
                                                            											unitId:$scope.unitId,
                                                            									};

                                                            									var URI = BASE_URL + ROOT_URL + "/api/global/updateDoctorIdentificationDetails";
                                                            									GenericService.serviceAction("POST",URI,data).then(function(response){
                                                            										if (response.data.status == 'success')
                                                            										{
                                                            										   $scope.removeList(index);
                                                            											$rootScope.stopLoader();
                                                            											growl.success(response.data.message,
                                                            													{
                                                            														title : 'Success!'
                                                            													});
                                                            											$scope.getIdentifDeta(response.data.object);
                                                            										} else if(response.data.status == 'error')
                                                            										{
                                                            											$rootScope.stopLoader();
                                                            											growl.warning(response.data.message,
                                                            													{
                                                            														title : 'Warning!'
                                                            													});
                                                            										}
                                                            									});


                                                            								}catch(e){
                                                            									console.log("Exception",e);
                                                            								}
                                                            					        	}
                                                            							}

							/*$scope.searchEmployee = function()
							{
								//console.log($scope.employeeSearchObj);
								
								var URI = BASE_URL + ROOT_URL
								+ SEARCHEMPLOYEEBYCRITERIA;

						BillingGenericService
								.serviceAction(METHOD_POST,
										URI, $scope.employeeSearchObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											$scope.searchEmployeeList = [];
											if (response.data.status == "success")
												$scope.searchEmployeeList = response.data.listObject;
										});
							}*/
					$scope.searchAreaByCityId = function(searchKeyword) {
								$scope.areaList = [];
								var serData = {
									cityId : $scope.employeeObj.cityId,
									searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL
										+ "/global/serviceAreaBysearchKeyword";
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												serData,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.areaList = response.data.listObject;
														if ($scope.areaList.length == 0) {
															/*
															 * if($scope.patientObj.area !=
															 * ''&
															 * $scope.patientObj.area !=
															 * null ){
															 * $scope.patientObj.areaName
															 * =$scope.patientObj.area;
															 * }else{
															 */
															$scope.employeeObj.areaName = null;
															$scope.employeeObj.areaId = null;
															$scope.employeeObj.area = null;
															/* } */
															growl
																	.warning(
																			"No record found for serarch crateria please add new area",
																			{
																				title : 'warning!'
																			});
														}
													}
													return $scope.areaList;
												});
							}

							$scope.searchAreaByCityIdPermanent = function(
									searchKeyword) {

								$scope.areaList = [];
								var serData = {
									cityId : $scope.employeeObj.permanentCityId,
									searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL
										+ "/global/serviceAreaBysearchKeyword";
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												serData,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.permanentAreaList = response.data.listObject;
														if ($scope.permanentAreaList.length == 0) {

															$scope.employeeObj.permanentAreaId = null;

															growl
																	.warning(
																			"No record found for serarch crateria please add new area",
																			{
																				title : 'warning!'
																			});
														}
													}
													return $scope.permanentAreaList;
												});
							}

					
							
							
							$scope.addAreaById = function($item, $model,
									$label, flag) {
								$scope.employeeObj.areaId = $item.areaId;
								$scope.employeeObj.areaName = $item.areaName;
								$scope.employeeObj.zipCode = $item.postCode;

							}
							         $scope.addAreaByIdPermanant = function($item,
									$model, $label, flag) {
								$scope.employeeObj.permanentAreaId = $item.areaId;
								$scope.employeeObj.permanentAreaName = $item.areaName;
								$scope.employeeObj.permanentZipCode = $item.postCode;
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
																 $scope.logEvents = [];
																 $scope.logEvents = response.data.listObject;
																
															}
														});

							}	
							
							 $scope.initPermanenetAreaMasterList = function(offset, noOfRecordsPerPage)
								{
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
													var data = {
															offset:offset,
															noOfRecordsPerPage:noOfRecordsPerPage,
														    cityId : $scope.employeeObj.permanentCityId,
													};
													var URI = BASE_URL + ROOT_URL
															+  "/adt/getAreaByCityIdPagination";
													BillingGenericService
													.serviceAction(METHOD_POST, URI, data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																$scope.permanentAreaList = [];
																if (response.data.status == "success")
																{
																	$scope.logEventsPer = [];
																	 $scope.logEventsPer = response.data.listObject;
																	
																}
															});

								}	
								
								 $scope.loadMore = function()
							       {
							    	   $scope.numberToDisplay += 5;
							           $scope.initAreaMasterList($scope.offset,$scope.numberToDisplay);
							       }
								 
								 $scope.loadMore1 = function()
							       {
							    	   $scope.numberToDisplayPer += 5;
							           $scope.initPermanenetAreaMasterList($scope.offset,$scope.numberToDisplayPer);
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
								 
								 
							$scope.searchEmployee = function()
							{
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							}
							
							$scope.add = function() {
								var data = {
										prefixId : '',
										firstName : '',
										middleName : '',
										lastName : '',
										mobileNo : '',
										genderId : '',
										dobKin : '',
										occupationId : '',
										relationId : '',
										isNok : 'N',
										typeId:'2',
										identificationTypeId:'',
										identificationNumber:'',
										createdDate:moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss'),
										createdBy:$scope.userId,
										updatedDate:moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										status:'A',
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
								};
								$scope.dataList.push(data);
							};

							$scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.dataList.length;
								var i = 1;
								if(le > 1){
									$scope.dataList.splice(index,1);
								}
							};
							$scope.getSubSpecialityBySpeciality = function(id)
							{
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									data.specialityId = id;
									var URI = BASE_URL + ROOT_URL
											+ "/adt/subspeciality/orgId/"+$scope.organizationId+"/specialityId/"+id;

									
									BillingGenericService
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.subSpecialityList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.subSpecialityList = response.data.listObject;
													});
								
							}
							
							$scope.getEmployeeCategoryList = function(){
						  		
						  		var URI = BASE_URL + ROOT_URL + GET_EMPLOYEE_CATEGORY_MASTER_LIST;
						  		//var URI = BASE_URL + ROOT_URL + GETACTIVEEMPLOYEECATEGORYMASTERLIST; //added by minesh
						  		var data = {
						  				organizationId:$scope.organizationId,
						  		}
						  		GenericService.serviceAction("POST",URI,data).then(function(response){
						  			if(response.data.status == "success"){
								  			$rootScope.stopLoader();					
								  			$scope.employeeCategoryList = [];
								  			$scope.employeeCategoryList=response.data.listObject;

						  			console.log("employeeCategoryList",$scope.getEmployeeCategoryList);
						  			}
						  		});
						  		}
						  		$scope.getEmployeeCategoryList();
						  		
						  		$scope.getEmployeeDesignationList = function(){
							  		
							  		var URI = BASE_URL + ROOT_URL + GETACTIVEDESIGNATIONMASTERLIST;//minesh
							  		var data = {
							  				organizationId:$scope.organizationId,
							  		}
							  		GenericService.serviceAction("POST",URI,data).then(function(response){
							  			if(response.data.status == "success"){
									  			$rootScope.stopLoader();					
									  			$scope.employeeDesignationList = [];
									  			$scope.employeeDesignationList=response.data.listObject;

							  			console.log("employeeDesignationList",$scope.getEmployeeDesignationList);
							  			}
							  		});
							  		}
							  		$scope.getEmployeeDesignationList();
						  		
						  		
							$scope.showStaffRegistrationSearch = function()
							{
								$state.reload();
								$rootScope.startLoader();
								$scope.staffRegistrationFlag  = false;
								$timeout( function(){
	                            	  $rootScope.stopLoader();
	                            },2000);
								
							}
							
							$scope.showStaffRegistrationDiv = function()
							{
								$rootScope.startLoader();
								$scope.setFirstTabLevel(1);
								$scope.employeeObj = {

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
										isVip : 'N',
										address : '',
										countryId : $scope.unitCountryId!=null && $scope.unitCountryId!=undefined ? $scope.unitCountryId.toString() : '',
										stateId : $scope.unitStateId!=null && $scope.unitStateId!=undefined ? $scope.unitStateId.toString() : '',
										districtId : $scope.unitDistrictId!=null && $scope.unitDistrictId!=undefined ? $scope.unitDistrictId.toString() : '',
										cityId : $scope.unitCityId!=null && $scope.unitCityId!=undefined ? $scope.unitCityId.toString() : '',
										areaId :  $scope.unitAreaId!=null && $scope.unitAreaId!=undefined ? $scope.unitAreaId.toString() : '',
										zipCode : '',
										phoneCode : '',
										phoneNumber : '',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										status : 'A',
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
										employeeId:'',
										employeeDetailsId:'',
										specialityId:'',
										subSpecialityId:'',
										empCategoryId:'',
										empDesignationId:'',
										empCode:'',
										dateOfJoining:'',
										accessCardNumber:'',
										bankName:'',
										accountNumber:'',
										bankBranchName:'',
										ifscCode:'',
										barCode:'',
										vipRemark:'',
										typeId:'2',
									    isUser:'Y',
									    userId:'',
									    employeeImage:'',
									    registrationNumber:'',
									    pwdExpiry:'',
									    labPin:''
									};

									//For Kin Object
									$scope.dataList = [ {
										prefixId : '',
										firstName : '',
										middleName : '',
										lastName : '',
										mobileNo : '',
										genderId : '',
										dobKin : '',
										occupationId : '',
										relationId : '',
										isNok : 'N',
										typeId:'2',
										identificationTypeId:'',
										identificationNumber:'',
										createdDate:moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
										createdBy:$scope.userId,
										updatedDate:moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										status:'A',
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
										
									} ];
								$scope.staffRegistrationFlag  = true;
								$scope.fieldsDisable = false;
								//$scope.getAreaByCity($scope.unitCityId,'L');
								
								
								//getting default race start
								var URI = BASE_URL + ROOT_URL+ "/adt/getDefaultRaceList/"+$scope.organizationId;
						BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success"){
												if(response.data.listObject.length>0){
													$scope.employeeObj.raceId = response.data.listObject[0].raceId;
												}
												
											}
												
										});
						//getting default race end
						//get nationality code by using country id start
						if($scope.unitCountryId!=null && $scope.unitCountryId!=undefined){
							var data = {
									countryId : $scope.unitCountryId,
									organizationId : $scope.organizationId
								};								
								
								var URI = BASE_URL + ROOT_URL+ "/adt/getNationalityByCountryId";
								GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											if (response.data.status == "success") {
												$scope.employeeObj.nationalityId = response.data.listObject[0].nationalityId.toString();

											} else {
//												alert("Error!!");
											}
										});
						}
						
						$scope.getActiveHISRoleList();
						$scope.getActiveRoleList();
						$scope.setFirstTabLevel(1);
						$scope.datepicker=undefined;
						$scope.datepickerDOJ=undefined;
						$scope.age="";
						$scope.defaultValue();
						//get nationality code by using country id end
								$timeout( function(){
	                            	  $rootScope.stopLoader();
	                            },2000);
							}
							
							$scope.newAge="";
							/*$scope.calculateAge= function()
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
							}*/
							$scope.calculateAge= function()
							{
								if($scope.datepicker == undefined || $scope.datepicker == null || $scope.datepicker == ''){
									 $scope.age = "";
//									   growl.warning("Date is invalid",{
//											title : "warning"
//										});
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
							
							
							$scope.validateEmpCode = function(code,employeeId)
							{
								var doctorId = 0 ;
								if(employeeId == undefined || employeeId == null || employeeId == ""){
									employeeId = 0 ;
								}
								var URI = BASE_URL + ROOT_URL + "/api/global/validateEmpCode/"+code+"/"+$scope.organizationId+"/"+doctorId+"/"+employeeId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE)
								.then(
								function(response) {
									
									if (response.data.status == "success")
									{
										if(response.data.object!=null && response.data.object > 0) 
										{
											$scope.employeeObj.empCode = "";
											growl.warning("Employee code is already used..!!");
										}
										$scope.stopLoader();
									}else{
										$scope.stopLoader();
									}
									
								});
							}
							
							
								$scope.checkDependentDob = function(index){
								
								if(!angular.isDate($scope.dataList[index].dobKin))
									{
										growl.warning("Dob is invalid",{
											title : WARNING_MSG
										});
										$scope.dataList[index].dobKin = new Date();
										return false;
									}else if($scope.dataList[index].dobKin > new Date())
									{
									       $scope.age = "";
										   growl.warning("DOB should not be greater than today's date",{
												title : "warning"
											});
										   $scope.dataList[index].dobKin = new Date();
										
										   return false;
									}
							}
							
							//$scope.myFunct = function(age) {
							//	if($('#datepicker').val()!=""){
								//	var currentYear = (new Date()).getFullYear();
								//    var finalYear = currentYear - age;									    
								//}else{
								//	var currentYear = (new Date()).getFullYear();
								//    var finalYear = currentYear - age;
								//    $scope.datepicker = new Date("01/01/"+finalYear);
								//}
									
									/*var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;
								    //alert(finalYear);
								    $scope.datepicker = new Date("01/01/"+finalYear);*/
								//}
							
							/*$scope.myFunct = function(age) {
								if(age>=$scope.minAge && age <= $scope.maxAge){
									 if($('#datepicker').val()!=""){
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;			
										   $scope.datepicker = new Date("01/01/"+finalYear);
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
								  
							}*/
								
								
								$scope.myFunct = function(age) {
									if(age>=$scope.minAge && age <= $scope.maxAge){
										 if($('#datepicker').val()!=""){
												/*var currentYear = (new Date()).getFullYear();
											    var finalYear = currentYear - age;*/
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
									}else{
										$scope.age = "";
										growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
											title : "warning"
											});
									}
									  
								}
								
							
							
							$scope.disableEnableExpiry = function(isExpiryVal,flag)
							{
							          
							          $scope.splitString = isExpiryVal.identificationTypeId.split('_');
                                            if($scope.splitString[1]=='Y' || $scope.splitString[1]=='Y' || isExpiryVal.identificationExpiryDate==true ){
                                             isExpiryVal.expiryDateRequired = true;
                                              }
                                             else{
                                                 isExpiryVal.expiryDateRequired = false;
                                             }
                                            $scope.lengthForDoc($scope.splitString);
                                            if(flag==true){
                                                isExpiryVal.identificationNumber="";
                                                isExpiryVal.identificationExpiryDate="";
                                                isExpiryVal.doctorIndetificationFile="";
                                                //angular.element("input[type='file']").val(null);
                                               // isExpiryVal.angular.element("input[type='file']").val(null);
                                                
                                                
                                            }
                                            


//								if(isExpiryVal.identificationTypeId == '126_Y' || isExpiryVal.identificationTypeId =='109_Y')
//                                {
//                                    isExpiryVal.expiryDateRequired = true;
//                                }
//                                else
//                                    {
//                                     isExpiryVal.expiryDateRequired = false;
//                                    }
							}

							$scope.openImageInNewTab=function(index){
							var imageUrl = $scope.idList[index].doctorIndetificationFile;
							window.open(imageUrl, '_blank');
							}
							
							$scope.hideShowAlise = function(vipVal)
							{
								if(vipVal == 'Y')
									$scope.aliseFlag = true;
								else
									$scope.aliseFlag = false;
									
							}
							
							$scope.sameAsLocalCopy = function(sameLoc)
							{
								if(sameLoc == 'Y')
									{
									if($scope.employeeObj.address =="" || $scope.employeeObj.address ==null
											||  $scope.employeeObj.countryId =="" ||  $scope.employeeObj.countryId ==null 
											||  $scope.employeeObj.stateId ==null ||  $scope.employeeObj.stateId =="" 
											|| $scope.employeeObj.districtId =="" || $scope.employeeObj.districtId == null
											|| $scope.employeeObj.cityId ==""  ||$scope.employeeObj.cityId ==null
											|| $scope.employeeObj.areaId == "" || $scope.employeeObj.areaId == null
											|| $scope.employeeObj.phoneCode =="" || $scope.employeeObj.phoneNumber ==""){
									
										 $scope.sameAsLocal='N';
										 growl.warning("Please Select All Required fields",{
												title : 'Warning !'
												});
												return false;
									}
									
									if($scope.employeeObj.countryId != '' )
									$scope.getStateByCountry($scope.employeeObj.countryId,'LTOP');
									
									if($scope.employeeObj.stateId != '' )
									$scope.getDistrictByState($scope.employeeObj.stateId,'LTOP');
									
									if($scope.employeeObj.districtId != '' )
									$scope.getCityByDistrict($scope.employeeObj.districtId,'LTOP');
									
									if($scope.employeeObj.cityId != '' )
									$scope.getAreaByCity($scope.employeeObj.cityId,'LTOP');
									
									$scope.employeeObj.permanentAddress = $scope.employeeObj.address;
									$scope.employeeObj.permanentCountryId = $scope.employeeObj.countryId;
									$scope.employeeObj.permanentStateId = $scope.employeeObj.stateId;
									$scope.employeeObj.permanentDistrictId = $scope.employeeObj.districtId;
									$scope.employeeObj.permanentCityId = $scope.employeeObj.cityId;
                                    $scope.employeeObj.permanentAreaName = $scope.employeeObj.areaName;
									$scope.employeeObj.permanentAreaId = parseInt($scope.employeeObj.areaId);
									$scope.employeeObj.permanentZipCode = $scope.employeeObj.zipCode;
									$scope.employeeObj.permanentPhoneCode = $scope.employeeObj.phoneCode;
									$scope.employeeObj.permanentPhoneNumber = $scope.employeeObj.phoneNumber;
									$scope.addressFlag =true;
									}
								else
									{
									/*$scope.employeeObj.permanentAddress ='';
									$scope.employeeObj.permanentCountryId = '';
									$scope.employeeObj.permanentStateId ='';
									$scope.employeeObj.permanentDistrictId ='';
									$scope.employeeObj.permanentCityId ='';
									$scope.employeeObj.permanentAreaId ='';
									$scope.employeeObj.permanentZipCode ='';
									$scope.employeeObj.permanentPhoneCode ='';
									$scope.employeeObj.permanentPhoneNumber ='';*/
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
												$scope.employeeObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
												
												if(type=="PA"){
													$scope.employeeObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
												}else{
													$scope.employeeObj.phoneCode = response.data.listObject[0].countryCallingCode;
												}
											} else {
												//alert("Error!!");
											}
										});
							}

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
													
													$scope.employeeObj.stateId="";
													$scope.employeeObj.districtId="";
													$scope.employeeObj.cityId="";
													$scope.employeeObj.areaId="";
													$scope.employeeObj.zipCode="";
													
													}
												else if(type == 'PA')
													{
													$scope.permanentStateList = [];
													$scope.permanentStateList = response.data.listObject;
													
													$scope.employeeObj.permanentStateId="";
													$scope.employeeObj.permanentDistrictId="";
													$scope.employeeObj.permanentCityId="";
													$scope.employeeObj.permanentAreaId="";
													$scope.employeeObj.permanentZipCode="";
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
												
												$scope.employeeObj.districtId="";
												$scope.employeeObj.cityId="";
												$scope.employeeObj.areaId="";
												$scope.employeeObj.zipCode="";
												
												}
												else if(type == 'PA')
												{
												$scope.permanentDistrictList = [];
												$scope.permanentDistrictList = response.data.listObject;
												
												$scope.employeeObj.permanentDistrictId="";
												$scope.employeeObj.permanentCityId="";
												$scope.employeeObj.permanentAreaId="";
												$scope.employeeObj.permanentZipCode="";
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
												
												 $scope.employeeObj.cityId="";
												$scope.employeeObj.areaId="";
												$scope.employeeObj.zipCode="";
												}
												else if(type == 'PA')
												{
												$scope.permanentCityList = [];
												$scope.permanentCityList = response.data.listObject;
												$scope.employeeObj.permanentCityId="";
												$scope.employeeObj.permanentAreaId="";
												$scope.employeeObj.permanentZipCode="";
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
								/*var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;*/
								 
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
													 $scope.logEvents = response.data.listObject;
												
												 $scope.employeeObj.areaId="";
												$scope.employeeObj.zipCode="";
												}
												else if(type == 'PA')
												{
													$scope.permanentAreaList = [];
													$scope.logEventsPer = [];
													$scope.logEventsPer = response.data.listObject;
	                                                $scope.permanentAreaList = response.data.listObject;
												$scope.employeeObj.permanentAreaId="";
												$scope.employeeObj.permanentZipCode="";
												}else if(type == 'L')
												{
													$scope.areaList = [];
													$scope.logEvents = [];
													 $scope.logEvents = response.data.listObject;
													 $scope.areaList =response.data.listObject;
													
												}else if(type == 'LTOP')
												{
													$scope.permanentAreaList = [];
													$scope.logEventsPer = [];
													$scope.logEventsPer = response.data.listObject;
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
							
							//HIS Role List Start
						   $scope.HISRoleList=[];
						   $scope.roleFinalList = [];
							$scope.getActiveHISRoleList = function(){
								var data= {
										 	organisationId:$scope.organizationId,
										 	unitId:$scope.unitId
										};
						
								var URI1 = BASE_URL + ROOT_URL +"/api/global/getActiveUserRoleMasterList"; 
								$rootScope.startLoader();
								GenericService.serviceAction("POST",URI1,data).then(function(response){
									if(response.data.status == "success"){
										$scope.HISRoleList = response.data.listObject;
										
										angular.forEach($scope.HISRoleList, function(value , key) {
											value.roleIsSelected='N';
										});
										$scope.roleFinalList=$scope.HISRoleList;
										console.log("HIS Role List::", $scope.roleFinalList);
										$rootScope.stopLoader();
									}
								});
									
							}
						$scope.getActiveHISRoleList();
							//His Role List End
						
						//OTIMS Role List 
						$scope.OTIMSRoleList=[];
						$scope.OtimsRoleFinalList=[];
						$scope.getActiveRoleList = function(){
							var data= {
									 organisationId:$scope.organizationId,
									 unitId:$scope.unitId
									};
					
							var URI1 = BASE_URL + ROOT_URL +"/api/global/getActiveOTIMSRoleMasterList"; 
							$rootScope.startLoader();
							GenericService.serviceAction("POST",URI1,data).then(function(response){
								if(response.data.status == "success"){												
									$scope.OTIMSRoleList = response.data.listObject;
									angular.forEach($scope.OTIMSRoleList, function(value , key) {
										value.roleIsSelected='N';
									});
									$scope.OtimsRoleFinalList=$scope.OTIMSRoleList;
									$rootScope.stopLoader();
								}
							});
								
						}
					$scope.getActiveRoleList();
						//OTIMS Role List End
					
					//Get Unit List by Organization Id
					$scope.unitList=[];
					$scope.finalUnitList=[];
					$scope.getUnitList = function () {
						var data = {
							organizationId: $scope.organizationId
						};

						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + UNITLIST;
						GenericService.serviceAction("POST", URI, data).then(function (response) {
							if (response.data.status == "success") {
								$scope.unitList = response.data.listObject;
								angular.forEach($scope.unitList, function(value , key) {
									value.isUnitSelected='N';
								});
								$scope.finalUnitList=$scope.unitList;
								console.log("Unit List::", $scope.unitList);
								$rootScope.stopLoader();
							}
							else {
								$rootScope.stopLoader();
							}

						});
					};
					$scope.getUnitList();
//get Unit List End
					
					 
					
						$scope.defaultValue = function()
						{
							if($scope.unitCountryId > 0)
							{
							  $scope.employeeObj.countryId  = $scope.unitCountryId.toString();
							  $scope.employeeObj.stateId  =  $scope.unitStateId.toString();
							  $scope.employeeObj.districtId = $scope.unitDistrictId.toString();
							  $scope.employeeObj.cityId = $scope.unitCityId.toString();
//							  $scope.employeeObj.areaId = $scope.unitAreaId.toString();
							  $scope.getStateByCountry($scope.employeeObj.countryId,'L');
							  $scope.getDistrictByState($scope.employeeObj.stateId,'L');
							  $scope.getCityByDistrict($scope.employeeObj.districtId,'L');
							  $scope.getAreaByCity($scope.employeeObj.cityId,'L');
							}
						}
						$scope.defaultValue();
							
							$scope.selectGender = function(id)
							{
								
								var genId = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].id;
								
								$scope.employeeObj.genderId = genId.toString();
								
							}
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {

								$scope.popUpFlag = false;
								$scope.saveEmployeeRegistration();
								console.log($scope.idList);

							}


							$scope.updateStatus = function(empId,type,userId)
							{
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									employeeId : empId,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId:$scope.organizationId,
									unitId : $scope.unitId,
									typeId : "2",
									userId:userId
								}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ UPDATEEMPLOYEESTATUS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {

												});
							}

							$scope.showUpdateBtn = function(item,type)
							{

								//To get user saved HIS role List
								$scope.roleFinalList=[];
								$scope.setFirstTabLevel(1);
								$timeout( function(){
			                 		var paramObj = {
			                 			   userId:item.userId,
			                 			   organisationId:$scope.organizationId,
								           unitId : $scope.unitId

			                 		}

			                         var URI = BASE_URL + ROOT_URL+ "/api/global/getHisRoleMapperListByUserId";
			                     GenericService.serviceAction("POST", URI, paramObj)
			                         .then(function(response){console.log(response);
			                         if (response.data.status == "success")

			                             $scope.userRoleMapperList  = response.data.listObject;
			                         console.log("userRoleMapperList",response.data.listObject);
			                         console.log("activeRoleList",$scope.HISRoleList);
			                        /*  angular.forEach($scope.HISRoleList, function(value , key) {
			                         	 var checkFlag = false;
			                              angular.forEach($scope.userRoleMapperList, function(value1 , key1) {
			                             	 if(value.roleId == value1.roleId)
			                             		 {
			                             		 	checkFlag = true;
			                             		 	$scope.newArr = {};
			                             		 	$scope.newArr.roleId = value.roleId;
			                             		 	$scope.newArr.roleName =value.roleName;
			                             		 	$scope.newArr.organisationId=$scope.organizationId;
			                             		 	$scope.newArr.unitId =$scope.unitId;
			                             		 	$scope.newArr.updatedBy= $scope.userId;
			                             		 	$scope.newArr.createdBy= $scope.userId;
			                             		 	$scope.newArr.roleIsSelected = 'Y';
			                             		 	$scope.newArr.status='A';
			                             		 	$scope.newArr.createdDate =$scope.currentDate;
			                             		 	$scope.newArr.updatedDate = $scope.currentDate;

			                             		 }

			                              		});
																if (checkFlag == true) {
																		$scope.roleFinalList.push($scope.newArr);
																	} else {

																		var newArr = {};
																		newArr.roleId = value.roleId;
				                                            		 	newArr.roleIsSelected = 'N';
																		newArr.roleName =value.roleName;
								                             		 	newArr.organisationId=$scope.organizationId;
								                             		 	newArr.unitId =$scope.unitId;
								                             		 	newArr.updatedBy= $scope.userId;
								                             		 	newArr.createdBy= $scope.userId;
								                             		 	newArr.status='A';
																		newArr.createdDate =$scope.currentDate;
																		newArr.updatedDate = $scope.currentDate;
								                            		 	$scope.roleFinalList.push(newArr);
																	}

			                             });

			                          console.log("Final Role List",JSON.stringify($scope.roleFinalList));*/
			                                 });

			                 },1000);


								//To get user saved OTIMS role List
								$scope.OtimsRoleFinalList=[];
								$timeout( function(){
			                 		var paramObj = {
			                 			   userId:item.userId,
			                 			   organisationId:$scope.organizationId,
								           unitId : $scope.unitId

			                 		}

			                         var URI = BASE_URL + ROOT_URL+ "/api/global/getOTIMSRoleMapperListByUserId";
			                     GenericService.serviceAction("POST", URI, paramObj)
			                         .then(function(response){console.log(response);
			                         if (response.data.status == "success")
			                        	 $scope.OTIMSUserRoleMapperList=[];
			                             $scope.OTIMSUserRoleMapperList  = response.data.listObject;
			                         console.log("OTIMSuserRoleMapperList",response.data.listObject);
			                         console.log("AllOtimsRoleMasterList",$scope.OTIMSRoleList);
			                          angular.forEach($scope.OTIMSRoleList, function(value , key) {
			                         	 var checkFlag = false;
			                              angular.forEach($scope.OTIMSUserRoleMapperList, function(value1 , key1) {
			                             	 if(value.roleId == value1.roleId)
			                             		 {
			                             		 	checkFlag = true;
			                             		 	$scope.newArr = {};
			                             		 	$scope.newArr.roleId = value.roleId;
			                             		 	$scope.newArr.roleName =value.roleName;
			                             		 	$scope.newArr.organisationId=$scope.organizationId;
			                             		 	$scope.newArr.unitId =$scope.unitId;
			                             		 	$scope.newArr.updatedBy= $scope.userId;
			                             		 	$scope.newArr.createdBy= $scope.userId;
			                             		 	$scope.newArr.roleIsSelected = 'Y';
			                             		 	$scope.newArr.status='A';
			                             		 	$scope.newArr.createdDate =$scope.currentDate;
			                             		 	$scope.newArr.updatedDate = $scope.currentDate;

			                             		 }

			                              		});
																if (checkFlag == true) {
																		$scope.OtimsRoleFinalList.push($scope.newArr);
																	} else {

																		var newArr = {};
																		newArr.roleId = value.roleId;
				                                            		 	newArr.roleIsSelected = 'N';
																		newArr.roleName =value.roleName;
								                             		 	newArr.organisationId=$scope.organizationId;
								                             		 	newArr.unitId =$scope.unitId;
								                             		 	newArr.updatedBy= $scope.userId;
								                             		 	newArr.createdBy= $scope.userId;
								                             		 	newArr.status='A';
																		newArr.createdDate =$scope.currentDate;
																		newArr.updatedDate = $scope.currentDate;
								                            		 	$scope.OtimsRoleFinalList.push(newArr);
																	}

			                             });

			                          console.log("Final OTIMS Role List",JSON.stringify($scope.OtimsRoleFinalList));
			                                 });

			                 },1000);

								$timeout( function(){
									var specDocObj = {
							    			organizationId:$scope.organizationId,
							    			userId:item.userId
							    }
							    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
						BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success"){
												$scope.docSubSpecialityList = [];
			                             $scope.docSpecialityList = response.data.listObject;
											$scope.docSubSpecialityList = response.data.object;
											$scope.specialityListCopy = angular.copy($scope.specialityList);
											$scope.specialityList = [];
			                          angular.forEach($scope.specialityListCopy, function(value , key)
			                          {
			                         	 var checkFlag = false;
			                              angular.forEach($scope.docSpecialityList, function(value1 , key1)
			                              {
			                             	 if(value.specialityId == value1.specialityId)
			                             		 {
			                             		 	checkFlag = true;
			                             		 	$scope.newArr = {};
			                             		 	$scope.newArr.specialityId = value1.specialityId;
			                             		 	$scope.newArr.doctorId =value1.doctorId;
			                             		 	$scope.newArr.specialityName =value1.specialityName;
			                             		 	$scope.newArr.unitId =$scope.unitId;
			                             		 	$scope.newArr.updatedBy= $scope.userId;
			                             		 	$scope.newArr.createdBy= $scope.userId;
			                             		 	$scope.newArr.selectedDepts = 'Y';
			                             		 	$scope.newArr.status='A';
			                             		 	$scope.newArr.createdDate =$scope.currentDate;
			                             		 	$scope.newArr.updatedDate = $scope.currentDate;

			                             		 }

			                              	});
												if (checkFlag == true) {
														$scope.specialityList.push($scope.newArr);
													} else {

														$scope.newArr = {};
				                             		 	$scope.newArr.specialityId = value.specialityId;
				                             		 	$scope.newArr.specialityName =value.specialityName;
				                             		 	$scope.newArr.doctorId =value.doctorId;
				                             		 	$scope.newArr.unitId =$scope.unitId;
				                             		 	$scope.newArr.updatedBy= $scope.userId;
				                             		 	$scope.newArr.createdBy= $scope.userId;
				                             		 	$scope.newArr.selectedDepts = 'N';
				                             		 	$scope.newArr.status='A';
				                             		 	$scope.newArr.createdDate =$scope.currentDate;
				                             		 	$scope.newArr.updatedDate = $scope.currentDate;
				                            		 	$scope.specialityList.push($scope.newArr);
													}

			                             });
			              			//for Sub Speciality Id Start

										$timeout( function(){

											$scope.specialityIdsCheck = [];
											$scope.subSpecialityListCheck = [];


					                                 angular.forEach( $scope.docSpecialityList , function(value , key) {
					                                	 $scope.specialityIdsCheck.push(value.specialityId);
					                                 });

					                             	var data = {
															organizationId : $scope.organizationId,
															specialityIds : $scope.specialityIdsCheck
														};

														var URI = BASE_URL + ROOT_URL
																+ "/adt/getSubSpecialityBySpecialityList";


														BillingGenericService
																.serviceAction(METHOD_POST, URI, data,
																		NOTIFICATION_MSG_STATUS_FALSE)
																.then(
																		function(response) {
																			console.log(response);
																			if (response.data.status == "success"){
																				$scope.subSpecialityListCheckTemp = [];
																				var i = 1;

																			      angular.forEach(response.data.listObject, function(value , key) {
																			    	         var flg = false;
																						       var k = 1;
																		    	  angular.forEach($scope.docSubSpecialityList, function(value1 , key1) {
																			    	  	if(value.subSpecialityMasterId == value1.subSpecialityMasterId)
																			    	  		{
																			    	  		flg = true;
																			    	  		value1.selectedSubDepts = 'Y';
																			    	  		  $scope.subSpecialityListCheckTemp.push(value1);
																			    	  		}
																			    	  	if(k == $scope.docSubSpecialityList.length && flg == false)
																			    	  		{
																			    	  		value.selectedSubDepts = 'N';
																			    	  		 $scope.subSpecialityListCheckTemp.push(value);
																			    	  		}
																			    	  	k++;
																			      });
																			    	  if(i == response.data.listObject.length){
																			    	  $scope.subSpecialityListCheck = [];
																			    		  $scope.subSpecialityListCheck =  $scope.subSpecialityListCheckTemp;
																			    	  }
																			    	 i++;
																			      });
																			}

																		});


					                 },3000);
											}
			                          console.log("Final Role List",JSON.stringify($scope.specialityList));
			                                 });

			                 },1000);



								//FOr Sub Speciality Id End

								//To get user saved Unit  List
								$scope.finalUnitList=[];
								 $scope.finalListOfRole = [];
								$timeout( function(){
			                 		var paramObj = {
			                 			   userId:item.userId,
			                 			   organisationId:$scope.organizationId

			                 		}

			                         var URI = BASE_URL + ROOT_URL+ "/api/global/getUserUnitListByUserId";
			                     GenericService.serviceAction("POST", URI, paramObj)
			                         .then(function(response){console.log(response);
			                         if (response.data.status == "success"){
			                        	 $scope.userUnitMapperList=[];
			                             $scope.userUnitMapperList  = response.data.listObject;
			                         console.log("userUnitMapperList",response.data.listObject);
			                         console.log("All unit List ",$scope.unitList);
			                          angular.forEach($scope.unitList, function(value , key) {
			                         	 var checkFlag = false;
			                              angular.forEach($scope.userUnitMapperList, function(value1 , key1) {
			                             	 if(value.unitId == value1.unitId)
			                             		 {
			                             		 	checkFlag = true;
			                             		 	$scope.newArr = {};
			                             		 	$scope.newArr.organisationId=$scope.organizationId;
			                             		 	$scope.newArr.unitId =value.unitId;
			                             		 	$scope.newArr.unitName=value.unitName;
			                             		 	$scope.newArr.updatedBy= $scope.userId;
			                             		 	$scope.newArr.createdBy= $scope.userId;
			                             		 	$scope.newArr.isUnitSelected = 'Y';
			                             		 	$scope.newArr.status='A';
			                             		 	$scope.newArr.createdDate =$scope.currentDate;
			                             		 	$scope.newArr.updatedDate = $scope.currentDate;

			                             		 }

			                              		});
																if (checkFlag == true) {
																		$scope.finalUnitList.push($scope.newArr);
																	} else {

																		var newArr = {};
				                                            		 	newArr.isUnitSelected = 'N';
								                             		 	newArr.organisationId=$scope.organizationId;
								                             		 	newArr.unitId =value.unitId;
								                             		 	newArr.unitName=value.unitName;
								                             		 	newArr.updatedBy= $scope.userId;
								                             		 	newArr.createdBy= $scope.userId;
								                             		 	newArr.status='A';
																		newArr.createdDate =$scope.currentDate;
																		newArr.updatedDate = $scope.currentDate;
								                            		 	$scope.finalUnitList.push(newArr);
																	}

			                             });
			                          console.log("Final OTIMS Role List",JSON.stringify($scope.OtimsRoleFinalList));



			                          angular.forEach($scope.userUnitMapperList, function(valueU , key) {

			                        	  var data= {
													 organisationId:$scope.organizationId,
													 unitId:valueU.unitId
													};

											$scope.unitNameRole = $.grep($scope.unitList, function (mppr) {
						                         return mppr.unitId == valueU.unitId;
						                     })[0].unitName;

											var obj = {};
											obj = {
													unitId:valueU.unitId,
													unitName:$scope.unitNameRole,
													//roleList : [{}];
											}
											obj.roleList = [];

										$scope.roleFinalList = [];

										var URI1 = BASE_URL + ROOT_URL +"/api/global/getActiveUserRoleMasterList";

										$rootScope.startLoader();

										GenericService.serviceAction("POST",URI1,data).then(function(response){
											if(response.data.status == "success"){

												$scope.roleFinalList = [];
												$scope.HISRoleList = [];
												$scope.HISRoleList = response.data.listObject;
												//obj.roleList = $scope.HISRoleList;
				                            	  //$scope.finalListOfRole.push(obj);
												//es
												var i = 1;
												  angular.forEach($scope.HISRoleList, function(value , key) {

									                         	 var checkFlag = false;
									                              angular.forEach($scope.userRoleMapperList, function(value1 , key1)
									                            		  {
									                            	  if(value.roleId == value1.roleId && value.unitId == value1.unitId)
									                             		 {
									                             		 	checkFlag = true;
									                             		 	$scope.newArr = {};
									                             		 	$scope.newArr.roleId = value.roleId;
									                             		 	$scope.newArr.roleName =value.roleName;
									                             		 	$scope.newArr.organisationId=$scope.organizationId;
									                             		 	$scope.newArr.unitId =value.unitId;
									                             		 	$scope.newArr.updatedBy= $scope.userId;
									                             		 	$scope.newArr.createdBy= $scope.userId;
									                             		 	$scope.newArr.roleIsSelected = 'Y';
									                             		 	$scope.newArr.status='A';
									                             		 	$scope.newArr.createdDate =$scope.currentDate;
									                             		 	$scope.newArr.updatedDate = $scope.currentDate;

									                             		 }

									                              		});
																						if (checkFlag == true) {
																								$scope.roleFinalList.push($scope.newArr);
																							} else {

																								var newArr = {};
																								newArr.roleId = value.roleId;
										                                            		 	newArr.roleIsSelected = 'N';
																								newArr.roleName =value.roleName;
														                             		 	newArr.organisationId=$scope.organizationId;
														                             		 	newArr.unitId =value.unitId;
														                             		 	newArr.updatedBy= $scope.userId;
														                             		 	newArr.createdBy= $scope.userId;
														                             		 	newArr.status='A';
																								newArr.createdDate =$scope.currentDate;
																								newArr.updatedDate = $scope.currentDate;
														                            		 	$scope.roleFinalList.push(newArr);
																							}

									                              if(i == $scope.HISRoleList.length){
									                            	  obj.roleList = $scope.roleFinalList;
									                            	  $scope.finalListOfRole.push(obj);
									                              }
							                         	       i++;
							                             });
												  angular.forEach($scope.finalListOfRole, function(value , key)
						                        		  {
													  var newRoleList=[];
													  angular.forEach(value.roleList, function(value1 , key1)
							                        		  {
												if($scope.isSuperUser=='Y')
												{
													newRoleList.push(value1);
												}
												else 
												{
												if(value1.roleId!=response.data.object)
													{
													newRoleList.push(value1);
													}
												}
						                        		  });
													  value.roleList=newRoleList;
						                        		  });

												//es

											}
										});


										$rootScope.stopLoader();

			                          });



			                         }

			                                 });

			                 },1000);


//To get doccument saved List

								$scope.idList = [];
								var data = {
										identificationTypeId : "",
										identificationNumber : "",
										identificationExpiryDate :undefined,
										organizationId:$scope.organizationId,
										fileName:"",
										unitId:$scope.unitId
								};
								$scope.idList.push(data);
								$scope.doedo = item.doctorId;
								$timeout( function(){
									var data1={
											"organizationId":$scope.organizationId ,
										     "unitId":$scope.unitId,
										     "employeeId":item.employeeId
									}
									var URI = BASE_URL + ROOT_URL+ "/api/global/getListOfDoctorIdentificationDocuumentList";
									GenericService.serviceAction("POST", URI, data1).then(function(response) {
										if (response.data.status == "success") {
											$rootScope.stopLoader();
											if(response.data.listObject != null && response.data.listObject.length > 0){
												$scope.idList=[];
											 angular.forEach(response.data.listObject, function(value,key) {
											 if(value.identificationExpiryDate!=undefined && value.identificationExpiryDate!=null)
											 {
											     var date=value.identificationExpiryDate.split("/");
											     var conDate=date[2]+"-"+date[1]+"-"+date[0];
											     var actualDate= new Date(conDate);
											     value.identificationExpiryDate=actualDate;
											 }
												 $scope.idList.push(value);
											 });
											 angular.forEach($scope.idList, function(value,key) {
												 angular.forEach($scope.identificationList, function(value1,key1) {
													 if(value.identificationTypeId == value1.identificationId)
													 value.identificationTypeId = value1.identificationId+'_'+value1.isExpiryRequired
												 });
												 $scope.disableEnableExpiry(value);
											 });
											console.log("idList",$scope.idList);
											$scope.idList.fileName=$scope.idList[0].fileName;
											}
										}
									});
								},1000);
//						------------------------------------------------------------------------------------------------------

								if(type == 'V')
									{
										$scope.fieldsDisable = true;
										$scope.shouldHideDiv= true;
										$scope.shouldHideRemoveButton = true; 
									}
								else
									{
										$scope.fieldsDisable = false;
										$scope.shouldHideDiv= false;
									}
								$scope.employeeObj = {

										prefixId :item.prefixId.toString(),
										firstName :item.firstName,
										middleName : item.middleName,
										lastName : item.lastName,
										genderId :  item.genderId.toString(),
										countryCallingCode : item.countryCallingCode,
										email : item.email,
										nationalityId : item.nationalityId.toString(),
										raceId : item.raceId,
										maritalStatusId : item.maritalStatusId,
										mobileNumber : item.mobileNumber,
										dob : '',
										identificationTypeId : item.identificationTypeId+'_'+item.isExpiryRequired,
										identificationNumber : item.identificationNumber,
										isVip : item.isVip,
										address : item.address,
										countryId : item.countryId.toString(),
										stateId : item.stateId.toString(),
										districtId : item.districtId.toString(),
										cityId : item.cityId.toString(),
										areaId : item.areaId,
										areaName :item.areaName,
										zipCode : item.zipCode,
										phoneCode : item.phoneCode,
										phoneNumber : item.phoneNumber,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy :  item.createdBy,
										status : 'A',
										permanentAddress:item.permanentAddress,
										permanentCountryId:item.permanentCountryId.toString(),
										permanentStateId:item.permanentStateId.toString(),
										permanentDistrictId:item.permanentDistrictId.toString(),
										permanentCityId:item.permanentCityId.toString(),
										permanentAreaId:item.permanentAreaId,
										permanentAreaName:item.areaName,
										permanentZipCode:item.permanentZipCode,
										permanentPhoneCode:item.permanentPhoneCode,
										permanentPhoneNumber:item.permanentPhoneNumber,
										aliseName:item.aliseName,
										identificationExpiryDate:item.identificationExpiryDate,
										occupationId:item.occupationId,
										employeeId:item.employeeId,
										employeeDetailsId:item.employeeDetailsId,
										specialityId:item.specialityId.toString(),
										subSpecialityId:item.subSpecialityId.toString(),
										empCategoryId:item.empCategoryId.toString(),
										empDesignationId:item.empDesignationId.toString(),
										empCode:item.empCode,
										dateOfJoining:'',
										accessCardNumber:item.accessCardNumber,
										bankName:item.bankName,
										accountNumber:item.accountNumber,
										bankBranchName:item.bankBranchName,
										ifscCode:item.ifscCode,
										barCode:item.barCode,
										vipRemark:item.vipRemark,
										typeId:'2',
										createdDate : item.createdDate,
										registrationNumber: item.registrationNumber,
										userId: item.userId,
										pwdExpiry: item.pwdExpiry,
										labPin:item.labPin

									};

								if(item.userId > 0)
									{
									  $scope.employeeObj.isUser = "Y";
									}else{
									     $scope.employeeObj.isUser = "N";
									}
								$scope.age = item.ageString;
								$scope.datepicker = new Date(item.dob);
								//alert($scope.datepicker);
								$scope.datepickerDOJ = new Date(item.dateOfJoining);
								$scope.datepickerExpiry = new Date(item.identificationExpiryDate);
								//$scope.dataList = item.dependentDetailsDtosList;
								$scope.getSubSpecialityBySpeciality(item.specialityId);
								if(item.countryId > 0)
									{
										$scope.getStateByCountry(item.countryId,'L');
										$scope.getDistrictByState(item.stateId,'L');
										$scope.getCityByDistrict(item.districtId,'L');
										$scope.getAreaByCity(item.cityId,'L');
									}
								if(item.permanentCountryId > 0)
								{
									$scope.getStateByCountry(item.permanentCountryId,'LTOP');
									$scope.getDistrictByState(item.permanentStateId,'LTOP');
									$scope.getCityByDistrict(item.permanentDistrictId,'LTOP');
									$scope.getAreaByCity(item.permanentCityId,'LTOP');
								}

								$scope.dataList = [];
								if(item.dependentDetailsDtosList.length > 0)
									{
								angular.forEach(item.dependentDetailsDtosList, function(value,key) {
									var data = {};
									data.prefixId = value.prefixId.toString();
									data.firstName = value.firstName;
									data.middleName  = value.middleName;
									data.lastName = value.lastName;
									data.mobileNo = value.mobileNo;
									data.genderId = value.genderId.toString();
									data.dobKin  = new Date(value.dobKin);
									data.occupationId = value.occupationId.toString();
									data.relationId  = value.relationId.toString();
									data.isNok = value.isNok;
									data.typeId = '2',
									data.identificationTypeId = value.identificationTypeId.toString();
									data.identificationNumber = value.identificationNumber;
									data.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								//	data.updatedDate = $scope.currentDate;
									data.updatedBy=$scope.userId;
									data.status='A';
									data.organizationId=$scope.organizationId;
									data.unitId = $scope.unitId;
									data.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								//	data.createdDate = $scope.currentDate;
									data.createdBy=$scope.userId;
									$scope.dataList.push(data);
								});
							}
								else
									{
									var data = {
											prefixId : '',
											firstName : '',
											middleName : '',
											lastName : '',
											mobileNo : '',
											genderId : '',
											dobKin : '',
											occupationId : '',
											relationId : '',
											isNok : 'N',
											typeId:'2',
											identificationTypeId:'',
											identificationNumber:'',
											createdDate:moment(new Date())
											.format('DD-MM-YYYY HH:mm:ss'),
											createdBy:$scope.userId,
											updatedDate:moment(new Date())
											.format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											status:'A',
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									$scope.dataList.push(data);
									}
								$scope.tab = 1;
								$scope.staffRegistrationFlag = true;
								$scope.filepreview = "";
								$scope.filepreview = item.employeeImage;
							}

							$scope.finalListOfRole = [];
							$scope.addUnitsRole = function(item,idx){

								if(item.isUnitSelected == 'Y')
								{
								var data= {
										 organisationId:$scope.organizationId,
										 unitId:item.unitId
										};

								$scope.unitNameRole = $.grep($scope.finalUnitList, function (mppr) {
			                         return mppr.unitId == item.unitId;
			                     })[0].unitName;

								var obj = {};
								obj = {
										unitId:item.unitId,
										unitName:$scope.unitNameRole,
								}
								obj.roleList = [];
								$scope.roleFinalList1 = [];
							var URI1 = BASE_URL + ROOT_URL +"/api/global/getActiveUserRoleMasterList";
							$rootScope.startLoader();
							GenericService.serviceAction("POST",URI1,data).then(function(response){
								if(response.data.status == "success"){
									$scope.HISRoleList = response.data.listObject;

									var i = 1;
									  angular.forEach($scope.HISRoleList, function(value , key) {

						                         	 var checkFlag = false;
						                              angular.forEach($scope.roleFinalList, function(value1 , key1)
						                            		  {
						                            	  if(value.roleId == value1.roleId && value.unitId == value1.unitId)
						                             		 {
						                             		 	checkFlag = true;
						                             		 	$scope.newArr = {};
						                             		 	$scope.newArr.roleId = value.roleId;
						                             		 	$scope.newArr.roleName =value.roleName;
						                             		 	$scope.newArr.organisationId=$scope.organizationId;
						                             		 	$scope.newArr.unitId =value.unitId;
						                             		 	$scope.newArr.updatedBy= $scope.userId;
						                             		 	$scope.newArr.createdBy= $scope.userId;
						                             		 	$scope.newArr.roleIsSelected = 'N';
						                             		 	$scope.newArr.status='A';
						                             		 	$scope.newArr.createdDate =$scope.currentDate;
						                             		 	$scope.newArr.updatedDate = $scope.currentDate;

						                             		 }

						                              		});
																			if (checkFlag == true) {
																					$scope.roleFinalList1.push($scope.newArr);
																				} else {

																					var newArr = {};
																					newArr.roleId = value.roleId;
							                                            		 	newArr.roleIsSelected = 'N';
																					newArr.roleName =value.roleName;
											                             		 	newArr.organisationId=$scope.organizationId;
											                             		 	newArr.unitId =value.unitId;
											                             		 	newArr.updatedBy= $scope.userId;
											                             		 	newArr.createdBy= $scope.userId;
											                             		 	newArr.status='A';
																					newArr.createdDate =$scope.currentDate;
																					newArr.updatedDate = $scope.currentDate;
											                            		 	$scope.roleFinalList1.push(newArr);
																				}

						                              if(i == $scope.HISRoleList.length){
						                            	  obj.roleList = $scope.roleFinalList1;
						                            	  $scope.finalListOfRole.push(obj);
						                              }
				                         	       i++;
				                             });
									  angular.forEach($scope.finalListOfRole, function(value , key)
			                        		  {
										  var newRoleList=[];
										  angular.forEach(value.roleList, function(value1 , key1)
				                        		  {
									if($scope.isSuperUser=='Y')
									{
										newRoleList.push(value1);
									}
									else 
									{
									if(value1.roleId!=response.data.object)
										{
										newRoleList.push(value1);
										}
									}
			                        		  });
										  value.roleList=newRoleList;
			                        		  });

									/*angular.forEach($scope.HISRoleList, function(value , key) {
										value.roleIsSelected='N';
									});
									$scope.roleFinalList=$scope.HISRoleList;
									console.log("HIS Role List::", $scope.roleFinalList);
									var obj = {
											unitId:item.unitId,
											unitName:$scope.unitNameRole,
											roleList:$scope.roleFinalList
									}
									$scope.finalListOfRole.push(obj);*/
									$rootScope.stopLoader();
								}
							});
							}else{
								var list = [];
								 angular.forEach($scope.finalListOfRole, function(value1 , key) {
									 if(value1.unitId != item.unitId ){
										 list.push(value1);
									 }
								 });
								 $scope.finalListOfRole = [];
								 $scope.finalListOfRole = list;
							}
							}

							 /***============================================Code Image Upload==============================================*/
						    $scope.staffImage;

						    $scope.convertBase64ToImage = function (dataurl, filename) {
							    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
							        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
							    while(n--){
							        u8arr[n] = bstr.charCodeAt(n);
							    }
							    return new File([u8arr], filename, {type:mime});
							}

						    $scope.uploadImage = function(inputFile,employeeId,orgId,unitId)
						    {
						    	    var file = inputFile;
							        var fd = new FormData();
							        fd.append('file', file );
									var URI1 = BASE_URL + ROOT_URL + EMPLOYEE_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+employeeId;
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

						    $scope.checkPingExist  =function()
						    {
						    	if($scope.employeeObj.labPin.length==4)
						    		{
						    		$rootScope.startLoader();
						    		var URI = BASE_URL + ROOT_URL + "/lab/checkpin/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.employeeObj.labPin+"/0";
									BillingGenericService
											.serviceAction(METHOD_GET, URI, {},
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response)
													{
														console.log(response);
														if (response.data.status == "success")
															{
															  $rootScope.stopLoader();
															}
														else if(response.data.status == "error")
															{
															growl
															.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
															   $rootScope.stopLoader();
															}
													});
						    		}

						    }

						    /***============================================Code Image Upload==============================================*/

							$scope.saveEmployeeRegistration = function()
							{
							     var count=0;
							    angular.forEach($scope.idList, function(val,id) {
                                          if((val.fileName == undefined || val.fileName == null ) && (val.doctorIndetificationFile==undefined || val.doctorIndetificationFile==null || val.doctorIndetificationFile=="")  ){
                                                                                         count++;
                                                                                      }
                                      })
                                      if(count>1)
                                     {
                                     growl.warning("Please Select File",{
                                             title : "warning"
                                             });
                                             //return false;
                                       }



								if($scope.employeeObj.isUser=='Y'){
									if($scope.employeeObj.labPin ==""  || $scope.employeeObj.labPin ==null){
										growl.warning("Please enter Pin",{
											title : "warning"
											});
											return false;
									}
								}else{
									$scope.selectHisRoleList = [];
									$scope.selectOTIMSRoleList = [];
								}

//                                  if($scope.employeeObj.fileName==undefined || $scope.employeeObj.fileName=="" || $scope.employeeObj.fileName==null ){
//                                       growl.warning("Please Select File",{
//                                                title : "warning"
//                                                });
//                                                return false;
//
//                                  }
								
								 var regEx = /^[+-]?\d+$/; // regex for validating calling country code
								$scope.$broadcast('show-errors-check-validity');
								$scope.tab=1;
						        if($scope.employeeRegistrationform.$valid){
						        	//alert('dss00');
						           $scope.tab=2;			           
						        	 if($scope.employeeRegistrationform2.$valid){
						        		 $scope.tab=4;
						        		 if($scope.employeeRegistrationform4.$valid){
						        			/* if($scope.employeeObj.zipCode !='' && $scope.employeeObj.zipCode!=undefined && $scope.employeeObj.zipCode!=null){
						        				 
						        				 if($scope.employeeObj.zipCode.length!=6){
						        					 growl.error("Please enter valid zip code",{
															title : ERROR_MSG
															});
															//$('#mobileNo').focus();
															return false;
						        				 }
						        			 }*/
							        		/* if($scope.employeeObj.permanentZipCode !='' && $scope.employeeObj.permanentZipCode!=undefined && $scope.employeeObj.permanentZipCode!=null){
						        				 
						        				 if($scope.employeeObj.permanentZipCode.length!=6){
						        					 growl.error("Please enter valid zip code",{
															title : ERROR_MSG
															});
															//$('#mobileNo').focus();
															return false;
						        				 }
						        			 }*/
						        			 // validating country code
						        			 var contryCode= $scope.employeeObj.countryCallingCode;
						        			 if($scope.employeeObj.countryCallingCode!=null && $scope.employeeObj.countryCallingCode!=undefined && $scope.employeeObj.countryCallingCode!=''){
												  if(!regEx.test($scope.employeeObj.countryCallingCode)){
													  growl.warning("Please enter valid country code",{
															title : 'warning'
															});
															return false;
												  } 
						        			 }
						        			 //validating mobile no 
						        			 var mobileNo=$scope.employeeObj.mobileNumber;
									        	if(contryCode==91){
									        		if(mobileNo.length==10){
									        			
														}else{
															growl.error("Please enter valid mobile number",{
																title : ERROR_MSG
																});
//																$('#mobileNo').focus();
																return false;
									        		}
									        	}else{
										        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
										        			
														}else{
															growl.error("Please enter valid mobile number",{
																title : ERROR_MSG
																});
//																$('#mobileNo').focus();
																return false;
									        		}
									        	}
						        		
						        			 if($scope.employeeObj.email != '')
												           			{
														           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
														           		 var email = $scope.employeeObj.email;
														           		    if (!filter.test(email)) {
														           		     $scope.tab=1;
													        				    growl.error(EMAIL_MSG,{
																					title : ERROR_MSG
																				});
													        				    $('#email').focus();
													        				    
													        				    return false;
														           		    }
														           		    else{
														           		    	    //validation for calling country code start
																			 // validating country code
														           		     if($scope.employeeObj.phoneCode!=null && $scope.employeeObj.phoneCode!=undefined && $scope.employeeObj.phoneCode!=''){
																				  if(!regEx.test($scope.employeeObj.phoneCode)){
																					  growl.warning("Please enter valid country code",{
																							title : 'warning'
																							});
																							return false;
																				  } 
														           		     }
																			 // validating country code
															           		  if($scope.employeeObj.phoneCode!=null && $scope.employeeObj.phoneCode!=undefined && $scope.employeeObj.phoneCode!=''){
																					  if(!regEx.test($scope.employeeObj.permanentPhoneCode)){
																						  growl.warning("Please enter valid country code",{
																								title : 'warning'
																								});
																								return false;
																					  } 
															           		  }
																				  //validation for calling country code end
															           		  
															           		  if($scope.employeeObj.permanentPhoneNumber != '')
																        		{
																        			 var permanentPhoneNumber = $scope.employeeObj.permanentPhoneNumber;
																        			 var permanentPhoneCode = $scope.employeeObj.permanentPhoneCode;
																        			 //validating mobile no 
																			        	if(permanentPhoneCode==91){
																			        		if(permanentPhoneNumber.length==10){
																								}else{
																									growl.error("Please enter valid mobile number",{
																										title : ERROR_MSG
																										});
																										return false;
																			        		}
																			        	}else{
																				        		if(permanentPhoneNumber.length==parseInt($scope.registrationFieldObj.mobileDigit)){
																								}else{
																									growl.error("Please enter valid mobile number",{
																										title : ERROR_MSG
																										});
																										return false;
																			        		}
																			        	}
													
																        		}
															           		  
																           		     if($scope.employeeObj.phoneNumber != '')
																		        		{
																		        			 var phoneNumber = $scope.employeeObj.phoneNumber;
																		        			 var phoneCode = $scope.employeeObj.phoneCode;
																		        			 //validating mobile no 
																					        	if(phoneCode==91){
																					        		if(phoneNumber.length==10){
																					        			 $scope.storePatientDetails();
																										}else{
																											growl.error("Please enter valid mobile number",{
																												title : ERROR_MSG
																												});
//																												$('#mobileNo').focus();
																												return false;
																					        		}
																					        	}else{
																						        		if(phoneNumber.length==parseInt($scope.registrationFieldObj.mobileDigit)){
																						        			 $scope.storePatientDetails();
																										}else{
																											growl.error("Please enter valid mobile number",{
																												title : ERROR_MSG
																												});
//																												$('#mobileNo').focus();
																												return false;
																					        		}
																					        	}
																					   /*     var val = $scope.employeeObj.phoneNumber;
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
																           		    	 $scope.storePatientDetails();
																           		    	 }
														           		    	}
												           				}
													        		 else
													        			 {
													        			 
													        			 
													        			 if($scope.employeeObj.phoneCode!=null && $scope.employeeObj.phoneCode!=undefined && $scope.employeeObj.phoneCode!=''){
													        				 // validating country code
																			  if(!regEx.test($scope.employeeObj.phoneCode)){
																				  growl.warning("Please enter valid country code",{
																						title : 'warning'
																						});
																						return false;
																			  } 
													        			 }
													        			 if($scope.employeeObj.permanentPhoneCode!=null && $scope.employeeObj.permanentPhoneCode!=undefined || $scope.employeeObj.permanentPhoneCode!=''){
																		 // validating country code
																			  if(!regEx.test($scope.employeeObj.permanentPhoneCode)){
																				  growl.warning("Please enter valid country code",{
																						title : 'warning'
																						});
																						return false;
																			  } 
													        			 }	  
													        			 if($scope.employeeObj.phoneNumber != '')
															        		{
															        			 var val = $scope.employeeObj.phoneNumber;
															        			 if (/^\d{10}$/.test(val)) {
							
															        				 $scope.storePatientDetails();
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
													        				 $scope.storePatientDetails();
													        				 }
													        			 }
						        		/* } else
				           		    	 {
					           		    	 $scope.storePatientDetails();
					           		    	 }*/
//						        	 }
						        		 //test end
								        };
						        }
						        
							}
						        }
							
							$scope.storePatientDetails = function()
							{
								//return false;
								console.log($scope.employeeObj);
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else 
								{
									
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									 if($scope.employeeObj.employeeId == ''){
									$scope.employeeObj.createdDate = $scope.currentDate;
									 }
									 
									 $scope.employeeObj.firstName=$rootScope.capitalize_Words($scope.employeeObj.firstName);
									 $scope.employeeObj.middleName=($scope.employeeObj.middleName == null ||  $scope.employeeObj.middleName.trim() == "" ||  $scope.employeeObj.middleName == undefined ? "": $rootScope.capitalize_Words($scope.employeeObj.middleName));
									 $scope.employeeObj.lastName=  ($scope.employeeObj.lastName == null ||  $scope.employeeObj.lastName.trim() == "" ||  $scope.employeeObj.lastName == undefined ? "": $rootScope.capitalize_Words($scope.employeeObj.lastName));
									// kin details start
									 console.log($scope.dataList);
									 var tempDataList = [];
										angular.forEach($scope.dataList, function(
												value, index) {
											console.log('value',value);
											value.dobKin = moment(value.dobKin).format(
													'DD-MM-YYYY HH:mm:ss');
											
											if(value.prefixId!=null && value.prefixId!=''  
												&& value.firstName!=null && value.firstName!=''  
												&& value.lastName!=null && value.lastName!='' 
												&& value.mobileNo!=null && value.mobileNo!=''
												&& value.genderId!=null && value.genderId!=''
												&& value.dobKin!=null && value.dobKin!=''
												&& value.occupationId!=null && value.occupationId!=''
												&& value.relationId!=null && value.relationId!=''
												&& value.isNok!=null && value.isNok!=''
												&& value.typeId!=null && value.typeId!=''
												&& value.identificationTypeId!=null && value.identificationTypeId!=''
												&& value.identificationNumber!=null && value.identificationNumber!=''){
												 tempDataList.push(value);
												}
											/*var isEmpty = !(Object.values(value).every(x =>(x != null &&  x != '')));
											 if(!isEmpty)
												 {
												   tempDataList.push(value);
												 }*/
										});
										var dependantFlag=false; 
										if(tempDataList.length>0){
											 angular.forEach(tempDataList, function(value , key) {													
												 var mobileNo=value.mobileNo;
												 if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
									        			
													}else{														
														 dependantFlag=true;
								        		}
												 
											 });
										}
										
									 // kin details end
									 //List for Role
									 $scope.selectHisRoleList=[];
									/* angular.forEach($scope.roleFinalList, function(value , key) {
										
										 if(value.roleIsSelected=='Y'){
											var data={ 
											 roleId : value.roleId,
											 roleName :value.roleName,
											 status:'A',
											 updatedBy : $scope.userId,
											 createdBy :$scope.userId,
											 createdDate :$scope.currentDate,
											 updatedDate : $scope.currentDate,
											 organisationId:$scope.organizationId,
											 unitId : $scope.unitId
											}
											$scope.selectHisRoleList.push(data);
										 }
										 
									 });
									 */
									 
									 
									 angular.forEach($scope.finalListOfRole, function(value1 , key) {
										 angular.forEach(value1.roleList, function(value , key) {
											 if(value.roleIsSelected=='Y'){
												var data={ 
												 roleId : value.roleId,
												 roleName :value.roleName,
												 status:'A',
												 updatedBy : $scope.userId,
												 createdBy :$scope.userId,
												 createdDate :$scope.currentDate,
												 updatedDate : $scope.currentDate,
												 organisationId:$scope.organizationId,
												 unitId : value1.unitId
												}
												$scope.selectHisRoleList.push(data);
											 }
										 });
										 
									 });
									 
									 
									 
									 //List for OTIMS Role
									 $scope.selectOTIMSRoleList=[];
									 angular.forEach($scope.OtimsRoleFinalList, function(value , key) {
										
										 if(value.roleIsSelected=='Y'){
											var data={ 
											 roleId : value.roleId,
											 roleName :value.roleName,
											 status:'A',
											 updatedBy : $scope.userId,
											 createdBy :$scope.userId,
											 createdDate :$scope.currentDate,
											 updatedDate : $scope.currentDate,
											 organisationId:$scope.organizationId,
											 unitId : $scope.unitId
											}
											$scope.selectOTIMSRoleList.push(data);
										 }
										 
									 });
									
									//List for Unit selected
									 
										//List for Unit selected
									 $scope.staffSpecialityMapperDtosList=[];
									 angular.forEach($scope.specialityList, function(value , key) {
										
										 if(value.selectedDepts=='Y'){
											var data=
											{ 
											 status:'A',
											 updatedBy : $scope.userId,
											 createdBy :$scope.userId,
											 unitId : $scope.unitId,
											 specialityId : value.specialityId
											}
											$scope.staffSpecialityMapperDtosList.push(data);
										 }
										 
									 });
									 
									 
									//List for Unit selected
									 $scope.staffSubSpecialityMapperDtosList=[];
									 angular.forEach($scope.subSpecialityListCheck, function(value , key) {
										
										 if(value.selectedSubDepts=='Y'){
											var data=
											{ 
											 status:'A',
											 updatedBy : $scope.userId,
											 createdBy :$scope.userId,
											 unitId : $scope.unitId,
											 subSpecialityId : value.subSpecialityMasterId
											}
											$scope.staffSubSpecialityMapperDtosList.push(data);
										 }
										 
									 });
									 
									 $scope.selectedUnitList=[];
									 angular.forEach($scope.finalUnitList, function(value , key) {
										
										 if(value.isUnitSelected=='Y'){
											var data={ 
											 status:'A',
											 updatedBy : $scope.userId,
											 createdBy :$scope.userId,
											 organisationId:$scope.organizationId,
											 unitId : value.unitId
											}
											$scope.selectedUnitList.push(data);
										 }
										 
									 });
                                     if($scope.selectedUnitList == undefined || $scope.selectedUnitList == null || $scope.selectedUnitList == ""|| $scope.selectedUnitList.length==0){
                                         growl.warning('Please select Unit Mapping..!!');
                                         return false;
                                     	}
									 $scope.employeeObj.selectedHisRoleList=$scope.selectHisRoleList;
									 $scope.employeeObj.selectedOTIMSRoleList=$scope.selectOTIMSRoleList;
									 $scope.employeeObj.selectedUserUnitList=$scope.selectedUnitList;
									 $scope.employeeObj.staffSpecialityMapperDtosList=$scope.staffSpecialityMapperDtosList;
									 $scope.employeeObj.staffSubSpecialityMapperDtosList=$scope.staffSubSpecialityMapperDtosList;
									 
									 if($scope.employeeObj.isUser=='Y'){
									/* if($scope.selectHisRoleList.length == 0 && $scope.selectOTIMSRoleList.length == 0){
											growl.warning("Please Select Atleast one Role",{
												title : "warning"
												});
												return false;
										}*/
									 }else{
										 $scope.selectHisRoleList=[];
										 $scope.selectOTIMSRoleList=[];
									 }
									$scope.employeeObj.updatedDate = $scope.currentDate;
									$scope.employeeObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
									//$scope.employeeObj.pwdExpiry = moment($scope.pwdExpiry).format('DD-MM-YYYY HH:mm:ss');
									$scope.employeeObj.pwdExpiry = moment(new Date()).add(365, 'days').format('DD-MM-YYYY HH:mm:ss');
									$scope.employeeObj.dateOfJoining = moment($scope.datepickerDOJ).format('DD-MM-YYYY HH:mm:ss');
									/*if($scope.datepickerExpiry != '')
										{
											$scope.employeeObj.identificationExpiryDate = moment($scope.datepickerPdd).format('DD-MM-YYYY HH:mm:ss');
										}*/

										console.log($scope.datepickerExpiry);
                                        console.log("+D==================="+moment($scope.datepickerExpiry).format('DD-MM-YYYY HH:mm:ss'));
									var ConDate = $('#datepickerExpiry').val();
									if($scope.expiryDateDisabled==false && (ConDate != '' && ConDate != undefined))
										$scope.employeeObj.identificationExpiryDate = moment($scope.datepickerExpiry).format('DD-MM-YYYY HH:mm:ss');
									
									if($scope.employeeObj.identificationTypeId != ''){
									 var splittedStringArray = $scope.employeeObj.identificationTypeId.split("_");
									 $scope.employeeObj.identificationTypeId = splittedStringArray[0]; 
									}
									 
									if(dependantFlag){
										growl.error("Please enter valid mobile number",{
											title : ERROR_MSG
											});
											return false;
									}
									  $scope.employeeObj.dependentDetailsDtosList =tempDataList;
									  $scope.employeeObj.defaultMISRights = $scope.defaultMISRights;
									  
										
										var flag = false;
						            	angular.forEach($scope.selectHisRoleList,function(value,key){
						            		
						            		if(value.roleName == 'Doctor' || value.roleName == 'doctor'){
						            			flag = true;
						            		}
						            		if(value.roleName == 'Nurse' || value.roleName == 'nurse'){
						            			flag = true;
						            		}
						            	});
						            	if(flag){
						            		  $scope.employeeObj.defaultCoversheetRights = $scope.defaultCoversheetRights 
						            	}else{
						            		$scope.employeeObj.defaultCoversheetRights = null;
						            	}
						            	
									 console.log("EMP OBJ",$scope.employeeObj);
									//return false;

									 $rootScope.startLoader();
									 if($scope.employeeObj.employeeId > 0)
										 {
										 var URI = BASE_URL + ROOT_URL
											+ UPDATEEMPLOYEEDETAILS;

									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, $scope.employeeObj,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														console
																.log(response);
														$rootScope.stopLoader();
														//$rootScope.startLoader();
														if (response.data.status == "success")
															{
															
															if($scope.staffImage!=undefined &&  !angular.equals({}, $scope.staffImage))
															   {
															    $scope.uploadImage($scope.staffImage,$scope.employeeObj.employeeId,$scope.organizationId,$scope.unitId);
															   }
															
															// staffRegistration multiple time
															if($scope.idList.length>0){

					                                     			 angular.forEach($scope.idList,function(value,key){

//

                                                                                          var splittedStringArray = value.identificationTypeId.split("_");
                                                                                             $scope.employeeObj.identificationTypeId = splittedStringArray[0];
                                                                                            // $scope.employeeObj.identificationFileName=value.doctorIndetificationFile.name;
                                                                                            // $scope.idList.doctorIndetificationFile=value.doctorIndetificationFile.name;
                                                                                            var fd = new FormData();
                                                                                            //fd.append('identificationTypeId',$scope.employeeObj.identificationTypeId);
                                                                                            fd.append('doctorId', $scope.employeeObj.doctorId=0);
                                                                                            fd.append('employeeId',$scope.employeeObj.employeeId);
                                                                                            fd.append('userId', $scope.userId);
                                                                                            fd.append('identificationNumber',value.identificationNumber );
                                                                                            fd.append('identificationTypeId',parseInt(splittedStringArray[0]));
                                                                                           fd.append('datepickerExpiry', value.identificationExpiryDate != null ? moment(value.identificationExpiryDate).format('DD-MM-YYYY HH:mm:ss') : "");
                                                                                            fd.append('createdBy',$scope.userId);
                                                                                            fd.append('updatedBy', $scope.userId);
                                                                                            fd.append('createdDate', $scope.currentDate);
                                                                                            fd.append('updatedDate', $scope.currentDate);
                                                                                            fd.append('unitId', $scope.unitId);
                                                                                            fd.append('organizationId', $scope.organizationId);
                                                                                            fd.append('doctorIndetificationFile',value.doctorIndetificationFile);
                                                                                            fd.append('file',(value.fileName != undefined) ? value.fileName : "");
                                                                                            if(value.fileName != undefined){
                                                                                                fd.append('retrieveFileName',(value.fileName.name != undefined) ? value.fileName.name : value.fileName);
                                                                                                fd.append('fileName',(value.fileName.name != undefined) ? value.fileName.name : "");
                                                                                            }else{
//                                                                                                fd.append('retrieveFileName',undefined);
//                                                                                                fd.append('fileName',undefined);
                                                                                                fd.append('retrieveFileName',"");
                                                                                                fd.append('fileName',"");
                                                                                            }
                                                                                            fd.append('userIndetificationDetailsId',value.userIndetificationDetailsId != null ? value.userIndetificationDetailsId : 0 );

                                                                                         var URI1 = BASE_URL + ROOT_URL +'/api/global/doctorIdentificationDocumets';
                                                                                            return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                                                                            {
                                                                                                return response;
                                                                                                console.log(response);
                                                                                            });

					                                                                              });

					                                                                                  }
															$scope.OtimsRoleFinalList=[];
															$scope.finalUnitList=[];
															$scope.roleFinalList=[];
															$scope.finalListOfRole = [];
															$scope.employeeObj={};
															$scope.init();
															$scope.searchEmployeeList = [];
															$scope.otList = [];
															$scope.defaultValue();
															$scope.showStaffRegistrationSearch();
															$scope.age="";
															$scope.noOfRecordsPerPage='10';
															$scope.offset=0;
															$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
															$scope.getUnitList();
														  }

													});
										 }
									 else{
										var URI = BASE_URL + ROOT_URL
												+ EMPLOYEEREGISTRATION;
										/*var URI = BASE_URL + ROOT_URL
										+ "/api/global/order";*/

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.employeeObj,
														NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) {
															console
																	.log(response);
															$rootScope.stopLoader();
															//$rootScope.startLoader();
															if (response.data.status == "success")
																{
																if($scope.staffImage!=undefined &&  !angular.equals({}, $scope.staffImage))
																   {
																    $scope.uploadImage($scope.staffImage,response.data.object,$scope.organizationId,$scope.unitId);
																   }
																
																
																
																// staffRegistration multiple time
																if($scope.idList.length>0){
						                                     			 angular.forEach($scope.idList,function(value,key){

//						                                     			if( value.doctorIndetificationFile == undefined || value.doctorIndetificationFile.name==undefined){

						                                     																		  var splittedStringArray = value.identificationTypeId.split("_");
						                                     																			 $scope.employeeObj.identificationTypeId = splittedStringArray[0];
						                                     																			// $scope.employeeObj.identificationFileName=value.doctorIndetificationFile.name;
						                                     																			// $scope.idList.doctorIndetificationFile=value.doctorIndetificationFile.name;
						                                     																			var fd = new FormData();
						                                     																	        //fd.append('identificationTypeId',$scope.employeeObj.identificationTypeId);
						                                     																	        fd.append('doctorId', $scope.employeeObj.doctorId=0);
						                                     																	        fd.append('employeeId',response.data.object);
						                                     																	        fd.append('userId', $scope.userId);
						                                     																	        fd.append('identificationNumber',value.identificationNumber );
						                                     																	        fd.append('identificationTypeId',parseInt(splittedStringArray[0]));
						                                     																	       fd.append('datepickerExpiry', value.identificationExpiryDate != null ? moment(value.identificationExpiryDate).format('DD-MM-YYYY HH:mm:ss') : "");
						                                     																	        fd.append('createdBy',$scope.userId);
						                                     																	        fd.append('updatedBy', $scope.userId);
						                                     																	        fd.append('createdDate', $scope.currentDate);
						                                     																	        fd.append('updatedDate', $scope.currentDate);
						                                     																	        fd.append('unitId', $scope.unitId);
						                                     																	        fd.append('organizationId', $scope.organizationId);
//						                                     																	        fd.append('file',value.doctorIndetificationFile);
//						                                     																	        fd.append('fileName',value.doctorIndetificationFile.name);
						                                     																	        fd.append('file',(value.fileName != undefined) ? value.fileName : "");
                                                                                                                                        if(value.fileName != undefined){
                                                                                                                                            fd.append('retrieveFileName',(value.fileName.name != undefined) ? value.fileName.name : value.fileName);
                                                                                                                                            fd.append('fileName',(value.fileName.name != undefined) ? value.fileName.name : "");
                                                                                                                                        }else{
                                            //                                                                                                fd.append('retrieveFileName',undefined);
                                            //                                                                                                fd.append('fileName',undefined);
                                                                                                                                            fd.append('retrieveFileName',"");
                                                                                                                                            fd.append('fileName',"");
                                                                                                                                        }
						                                     																	        fd.append('userIndetificationDetailsId',value.userIndetificationDetailsId != null ? value.userIndetificationDetailsId : 0 );

						                                     															        	 var URI1 = BASE_URL + ROOT_URL +'/api/global/doctorIdentificationDocumets';
						                                     																	        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
						                                     																			{
						                                     																				return response;
						                                     																				console.log(response);
						                                     																			});

						                                                                              });

						                                                                                  }
																
																$scope.OtimsRoleFinalList=[];
																$scope.finalUnitList=[];
																$scope.roleFinalList=[];
																$scope.finalListOfRole = [];
																$scope.employeeObj={};
																$scope.init();
																$scope.otList = [];
																$scope.getUnitList();
																$scope.searchEmployeeList = [];
																$scope.defaultValue();
																$scope.showStaffRegistrationSearch();
																$scope.age="";
																$scope.noOfRecordsPerPage='10';
																$scope.offset=0;
																$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
																}
															
														});


                                                                             }								 							
                                                                           }
                                                                       }

							
							//Only one role Selection for OTIMS
							$scope.otList=[];
							var flag=true;
							$scope.validateOtimsRoleSelection=function(otimsRole){
								
								if($scope.OTIMSUserRoleMapperList!=undefined){
								 if($scope.OTIMSUserRoleMapperList.length>0 && flag==true){
									$scope.otList=$scope.OTIMSUserRoleMapperList;
									flag=false;
								}
							}
								
								if(otimsRole.roleIsSelected=='Y'){
									$scope.otList.push(otimsRole);
								}
								else{
									$scope.otList.splice(otimsRole, 1);
								}
								if($scope.otList.length>1){
									$scope.otList.splice(otimsRole, 1);
									otimsRole.roleIsSelected='N';
									growl.error("can not select two roles",
											{
												title : 'error!'
											});
									return false;
								}
								
							}
							
							 $scope.setPostCode = function(areaId,flag){
									
								 if(flag ==0 ){
									 
									 angular.forEach($scope.areaList,function(value,key){
											
											if(value.areaId==areaId)
											{
												$scope.employeeObj.zipCode = value.postCode;
											}
										});
								 }else{
									 angular.forEach($scope.permanentAreaList,function(value1,key){
											
											if(value1.areaId==areaId)
											{
												$scope.employeeObj.permanentZipCode = value1.postCode;
											}
										});
									 
								 }
								 $scope.selectFlag = true;
								 $scope.selectFlag1 = true;
	                             $scope.flgHideShow = false;
	                             $scope.flgHide = false;
	                             setTimeout(function(){
	                                  $scope.selectFlag = false;
	                                  $scope.selectFlag1 = false;
	                                },500);
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
								
								//Second Level
								$scope.secondTabLevel = 1 ;
								
								$scope.setSecondTabLevel = function(newTab)
								{
								
									$scope.secondTabLevel = newTab;
								};
								
								$scope.isSetSecondTabLevel = function(tabNum)
								{
									return $scope.secondTabLevel === tabNum;
								};
								
								
								//Third Level
								$scope.thirdTabLevel = 1 ;
								
								$scope.setThirdTabLevel = function(newTab)
								{
									
									$scope.thirdTabLevel = newTab;
								};
								
								$scope.isSetThirdTabLevel = function(tabNum)
								{
									return $scope.thirdTabLevel === tabNum;
								};
								
								
								
								//FORTH Level
								$scope.forthTabLevel = 1 ;
								
								$scope.setForthTabLevel = function(newTab)
								{
									$scope.forthTabLevel = newTab;
								};
								
								$scope.isSetForthTabLevel = function(tabNum)
								{
									return $scope.forthTabLevel === tabNum;
								};
							
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
						
								/**List And Pagination Start**/
								$scope.setNoOfRecords = function() {
									$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								};
								
								$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
								{
									$rootScope.startLoader();
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
									$scope.employeeSearchObj.offset = offset;
									$scope.employeeSearchObj.noOfRecordsPerPage = noOfRecordsPerPage;
									
									var data2 = {
											      organizationId:organizationId,
											      unitId:unitId
												};
									
									var URI1 = BASE_URL + ROOT_URL + SEARCHEMPLOYEEBYCRITERIA;
									var URI2 = BASE_URL + ROOT_URL + GET_EMPLOYEE_REGISTRATION_COUNT;
									
									promiseFactoryWithObject.setPromisesWithObject(URI1, URI2, "POST", "POST",$scope.employeeSearchObj,data2).then(function(response) 
											{
												$rootScope.stopLoader();
												$scope.searchEmployeeList = [];
												$scope.searchEmployeeList = response[0].listObject;
												$scope.commonListCount = response[1].object;
												$scope.setPage(1, false);
											});
								}		
								
								$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									
									$scope.employeeSearchObj.offset = offset;
									$scope.employeeSearchObj.noOfRecordsPerPage = noOfRecordsPerPage;
									
									var URI = BASE_URL + ROOT_URL + SEARCHEMPLOYEEBYCRITERIA;
									console.log("URI", URI);
									GenericService.serviceAction("POST", URI,$scope.employeeSearchObj).then(function(response) {
										$rootScope.stopLoader();
										$scope.searchEmployeeList = response.data.listObject;
									});
								}

								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages)
									{
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
									if (flag)
									{
										$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
									}
								}

								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								
								/**List And Pagination End**/		
								
								$scope.universalSearchForEmployee = function(searchString){
										$rootScope.startLoader();
										$scope.loadingMsg="Loading...";
										$scope.searchEmployeeList=[];
										if(searchString!=''){
										$scope.employeeSearchObj.offset = $scope.offset;
										$scope.employeeSearchObj.noOfRecordsPerPage = $scope.noOfRecordsPerPage;
										$scope.employeeSearchObj.mobileNumber = searchString;
										
										var URI = BASE_URL + ROOT_URL + "/api/global/universalSearchForEmployee";
										console.log("URI", URI);
										GenericService.serviceAction("POST", URI,$scope.employeeSearchObj).then(function(response) {
											$rootScope.stopLoader();
											if(response.data.listObject.length==0){
												$scope.loadingMsg="No record found...";
											}else{
												$scope.searchEmployeeList = response.data.listObject;
											}
										});
										}else{
											$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
										}
									}
								
								$scope.selectDependentGender = function(prefixId,index){
									var genId = $.grep($scope.prefixList, function (doc) {
					    	            return doc.prefixId == prefixId;
					    	        })[0].id;
									
									$scope.dataList[index].genderId = genId.toString();
								}
				/**********************************************zipcode************************************************************/
								$scope.getDataByPinCode = function(){
		                     		
		                     		if($scope.employeeObj.zipCode != null && $scope.employeeObj.zipCode != undefined && $scope.employeeObj.zipCode!=""){
		                     			$rootScope.startLoader();
		                     			var zipC = $scope.employeeObj.zipCode;
		                     			
		                     		var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
		                     		BillingGenericService
		                     				.serviceAction(METHOD_GET,
		                     						URI, "",
		                     						NOTIFICATION_MSG_STATUS_FALSE)
		                     				.then(
		                     						function(response) {
		                     							if (response.data.status == "success"){
		                     								
		                     								if(response.data.object.areaId != ''){
		                     									$scope.getAreaByCity(response.data.object.cityId,'L');
		                     									$scope.employeeObj.areaId = response.data.object.areaId;
		                     								}
		                     			///searchDoctorDetailsByCriteria		 /doctorRegistration			/searchEmployeeByCriteria
		                     								var checkAreaName = false;
		                     								angular.forEach($scope.areaList,function(value,key){
																
																if(value.areaId==$scope.employeeObj.areaId)
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
		                     								$scope.employeeObj.areaId='';
		                     								$rootScope.stopLoader();
		                     							}
		                     						});
		                     		}
		                     		
		                     	}
								 $scope.getDataByPinCodePermanent = function(){
										
										if($scope.employeeObj.permanentZipCode != null && $scope.employeeObj.permanentZipCode != undefined && $scope.employeeObj.permanentZipCode != ""){
											$rootScope.startLoader();
											var zipC = $scope.employeeObj.permanentZipCode;
											
										var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
										BillingGenericService
												.serviceAction(METHOD_GET,
														URI, "",
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															if (response.data.status == "success"){
																if(response.data.object.areaId != ''){
																	$scope.getAreaByCity(response.data.object.cityId,'LTOP');
																	$scope.employeeObj.permanentAreaId = response.data.object.areaId;
																}
																
																var checkArea = false;
																angular.forEach($scope.areaList,function(value,key){
																	
																	if(value.areaId==$scope.employeeObj.permanentAreaId)
																	{
																		checkArea = true;
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
																$scope.employeeObj.permanentAreaId='';
																$rootScope.stopLoader();
															}
														});
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
							      $scope.selectFlag == false
							      $scope.onClickDropdownmyFun = function(){
							    	  $scope.flgArea =false;
			                                if($scope.selectFlag == false || $scope.selectFlag== undefined){
			                                    $scope.flgHideShow = true;
			                                    $scope.selectFlag = true;
			                                    
			                                }
			                            }
							      	$scope.selectFlag1 == false
							        $scope.onClickDropdownmy = function(){
							        	$scope.flgArea1 =false;
							        	if($scope.selectFlag1 == false || $scope.selectFlag1== undefined){
		                                    $scope.flgHide = true;
		                                    $scope.selectFlag1 = true;
		                                }
			                           }
							      $scope.setFlagFalse=function(flag){
					   					if(flag==false){
					   						$scope.flgHide=false;
					                           $scope.selectFlag = true;
					   					}
					   					
					   				}
							        $scope.setFlag=function(flag){
					   					if(flag==false){
					   						$scope.flgHideShow=false;
					                           $scope.selectFlag1 = true;
					   					}
					   					
					   				}
								
						} ]);



