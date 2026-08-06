'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:loginController 
 * @description #loginController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'loginController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'$timeout',
						'$cookieStore',
						'$compile',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$timeout,$cookieStore,$compile,$modalInstance) {
							
							
							$('.modal-backdrop').remove()
							$(document.body).removeClass("modal-open");
							angular.element('#pharmacyPopupBeforLeftexpand').modal('hide'); 
							angular.element('#nursingStationModal').modal('hide'); 
							angular.element('#counterDetailsModal').modal('hide');
							
							$scope.organizationId = '';
							$scope.unitId = '';
							$scope.disableOrgList = true;
							$rootScope.doctorCabinObject = undefined;  
				$scope.name = "";
				$rootScope.loginpage = false;
				var data = "";
				sessionStorage.removeItem('specialtyNursingStationCookies');
				sessionStorage.removeItem('specialtyNursingStationCookiesNur');
				 var cookies = $cookies.getAll();
				   angular.forEach(cookies, function (v, k) {
					   
				       $cookies.remove(k);
				   });
				   sessionStorage.removeItem("permissions");
				   sessionStorage.removeItem("roleIds");
				   
				   var baseUrlLocation = window.location.href;
                   //console.log(baseUrlLocation);
	               var temp = baseUrlLocation.split('//');
	               //console.log(temp);
	               var BASE_URL_CALL = temp[1].split('.');
	               //console.log(BASE_URL_CALL);
	               //var t = '';
	               var isLocalhost =  (/:/.test(temp[1]) || window.location.hostname.includes("localhost") );
	               //console.log(BASE_URL_CALL2.length);
                   
	               var subdomain = (isLocalhost==true || BASE_URL_CALL[0]=='smeabc' || BASE_URL_CALL[0]=='qa' || BASE_URL_CALL[0]=='intdev' || BASE_URL_CALL[0]=='demo' ) ? 'localhost' :  BASE_URL_CALL[0]; 
	               
	              // $scope.img = "https://s3.ap-south-1.amazonaws.com/his-docs/organization-logo/"+subdomain+".jpg";
	               $scope.img="resources/assets/images/logo/loginImgD.png";
				var URI = BASE_URL + ROOT_URL + ORGANIZATIONLIST + "/"+subdomain ;
				GenericService
					.serviceAction("GET", URI, data)
					.then(
						function (response) {
							$scope.organizationList = [];
							if (response.data.status == "success") {
								$scope.organizationList = response.data.listObject;

//								$scope.applicationNameFun();
//								$scope.copyWriteNameFun();
//								$scope.navBarImgFun();


								if($scope.organizationList.length == 1)
									{
										$scope.organizationId = $scope.organizationList[0].organizationId;
										$scope.disableOrgList = true;
										$scope.img = ($scope.organizationList[0].organizationLogo !=null && $scope.organizationList[0].organizationLogo !="") ? $scope.organizationList[0].organizationLogo : "resources/assets/images/logo/login_screen1.jpg";
										//$scope.getUnit($scope.organizationId);
									}else{
										$scope.disableOrgList = false;
//										$scope.img = "/resources/assets/images/logo/login.png";
									}
							}
						});




				$scope.getUnitByUserCredentials = function () {

//                    $scope.applicationNameFun();
//                    $scope.copyWriteNameFun();
//                    $scope.navBarImgFun();
					var data = {
							organizationId: $scope.organizationId,
							userName: $scope.userName,
							password: $scope.password,
						}

					var found = $scope.organizationList.find(ar => ar.organizationId == $scope.organizationId);
                    if (found && found.differentTypesScreenImageList && found.differentTypesScreenImageList.length > 0) {
                        angular.forEach(found.differentTypesScreenImageList, function(item, index) {
                            if (item.imageTypeId == 2) {
                                $scope.navBarImg = item.imageURL;
                            }
                            else if (item.imageTypeId == 6) {
                                $scope.loginPageImg = item.imageURL;
                            }
                            else if (item.imageTypeId == 7) {
                                $scope.applicationName = item.logoName;
                            }
                            else if (item.imageTypeId == 8) {
                                $scope.copyWrite = item.logoName;
                            }
                        });
                    }
                    $scope.img = (found.organizationLogo !=null && found.organizationLogo !="") ? found.organizationLogo : "resources/assets/images/logo/loginImgD.png";
                    console.log($scope.img);
					
					if(found != undefined){
						data.isDuoUserLogin=true;
					}
					
					if($scope.organizationId!=undefined && $scope.organizationId!=null && $scope.organizationId>0 
							&& $scope.userName!=undefined && $scope.userName!=null && $scope.userName!="" 
								&& $scope.password!=undefined && $scope.password!=null && $scope.password!=""	)
					{
						//$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/adt/getUnitByCredentials";
						GenericService.serviceAction("POST", URI, data).then(function (response) {
							
							$scope.unitList = [];
							if (response.data.status == "success") {
								$scope.unitList = response.data.listObject;
								if($scope.unitList!=null && $scope.unitList.length>0)
								{
									if($scope.unitList.length == 1)
									{
										$scope.unitId = $scope.unitList[0].unitId;
									}
									/*$scope.amountPaid=$scope.unitList[0].amountPaid;         
									if($scope.unitList[0].subscriptionDate != null && $scope.unitList[0].subscriptionDate !='' && $scope.unitList[0].isSuperUser==='Y' && $scope.unitList[0].amountPaid =='false'){
										$scope.subscriptionEndDate = new Date($scope.unitList[0].subscriptionDate);
	                                var currentDate = new Date();
	                                currentDate.setHours(0, 0, 0, 0);
	                                var remainingDays = Math.floor(($scope.subscriptionEndDate - currentDate) / (1000 * 60 * 60 * 24));
	                                if (remainingDays === 0) {
	     	                           growl.warning('Last reminder. Your subscription will end at 12:00 am. No user will have access to the application.',
	     	   							   {
	     	   								title : ''
	     	   							    });
	     	   	                              }else if(remainingDays > 0 && remainingDays <= 15) {
	                           growl.warning('Your subscription will expire in ' + remainingDays + ' days. Please recharge.',
							   {
								title : ''
							    });
	                              }
	                          
									}*/
	                          
								}
								else{
									growl.error(response.data.message);
								}
								$rootScope.stopLoader();
							}
							else {
								growl.error(response.data.message);
								$rootScope.stopLoader();
							}
						});
					}


				};


                    console.log($scope.organizationId)
				
				$scope.login = function () {
					// $rootScope.loginpage= true;
					$rootScope.startLoader();
					// code for generating random number for perticular user login start
					var cookieMacAdress =localStorage.getItem('macAddress');
					var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*()_+><";
					var string_length = 16;
					var randomstring = '';
					for (var i=0; i<string_length; i++) {
						var rnum = Math.floor(Math.random() * chars.length);
						randomstring += chars.substring(rnum,rnum+1);
					}
//					//console.log("user random number",randomstring);
					// code for generating random number for perticular user login start
					var data = {
						isDuoUserLogin:false,
						organizationId: $scope.organizationId,
						unitId: $scope.unitId,
						// userName: $scope.userName,
						// password: $scope.password,
						userName: $scope.encryptData($scope.userName),
						password: $scope.encryptData($scope.password),
						macAddress: cookieMacAdress!=undefined && cookieMacAdress !=null && cookieMacAdress !="" ? cookieMacAdress :randomstring
					}
					var macAddress=cookieMacAdress!=undefined && cookieMacAdress !=null && cookieMacAdress !="" ? cookieMacAdress :randomstring;
					$scope.tempRoleIds = [];
					$scope.departmentNursingStationMapperDtoslist = [];
					
					var found = $scope.organizationList.find(ar=>ar.organizationId == $scope.organizationId && ar.isDuoAuthenticationRequired == true);
					
					if(found != undefined){
						data.isDuoUserLogin=true;
					}
					
//					if(found != undefined){
//						$scope.isDuoLogin=true;
//						var URI = BASE_URL + ROOT_URL + "/adt/duo/login";

//					}else{
						var URI = BASE_URL + ROOT_URL + USER_LOGIN;
//					}
					
					GenericService.serviceActionForSubscription("POST", URI, data)
						.then(function (response) {
//							$window.open(response.data, '_blank');
//							if($scope.isDuoLogin == true){
//								document.location.href=response.data;
//							}
							
							if (response.data.object != null) {	
								// Code for setting cookies value for login user start 
								if(response.data.object==1){
									$rootScope.stopLoader();
									angular.element('#loginContinuePopUp').modal('show');
									/*growl.error(response.data.message,
											{
												title : 'Error!'
											});*/
									return false;
								}
							/*	if(response.data.object.subscrptionMsg != null && response.data.object.subscrptionMsg != ''){
									$rootScope.morqueeMsg = "";
									$rootScope.morqueeMsg = response.data.object.subscrptionMsg;
									growl.warning(response.data.object.subscrptionMsg,
											{
												title : 'warning!'
											});
								}*/
								
								/*if(response.data.object.subscriptionEndDateUnitMaster!= null){
								var today = new Date();
								var year = today.getFullYear();
								var month = ('0' + (today.getMonth() + 1)).slice(-2); // Adding 1 to month because January is 0
								var day = ('0' + today.getDate()).slice(-2);

								// Format the date string with only year, month, and day
								var formattedDate = year + '-' + month + '-' + day;
								var subscriptionEndDateNew = response.data.object.subscriptionEndDateUnitMaster.substring(0, 10);
								console.log(subscriptionEndDateNew);
								if (subscriptionEndDateNew < formattedDate) 
									{
										$rootScope.stopLoader();
										growl.error("Your subscription has ended. Please renew your subscription to continue.");
										return;
									}
							    
								
								if(response.data.object.subscriptionEndDateUnitMaster!= null && response.data.object.subscriptionEndDateUnitMaster!= '' && response.data.object.isSuperUser==='Y' && $scope.amountPaid==='false'){	
								$scope.subscriptionEndDateNew = new Date(response.data.object.subscriptionEndDateUnitMaster);
                                var currentDate = new Date();
                                currentDate.setHours(0, 0, 0, 0);
                                var remainingDays = Math.floor(($scope.subscriptionEndDateNew - currentDate) / (1000 * 60 * 60 * 24));
                                if (remainingDays === 0) {
	     	                           growl.warning('Last reminder. Your subscription will end at 12:00 am. No user will have access to the application.',
	     	   							   {
	     	   								title : ''
	     	   							    });
	     	   	                              }else if(remainingDays > 0 && remainingDays <= 15) {
	                           growl.warning('Your subscription will expire in ' + remainingDays + ' days. Please recharge.',
							   {
								title : ''
							    });
	                              }
								}
								
								}*/
								var expireDate = new Date();
								expireDate.setDate(expireDate.getDate() + 60);
							    localStorage.setItem('macAddress', macAddress);
//							    $cookies.put('macAddress', randomstring);
								// Code for setting cookies value for login user End  
								// Setting a cookie

								var roleIds = [];
								$scope.tempRoleIds = response.data.object.roleIds;
								$scope.departmentNursingStationMapperDtoslist = response.data.object.departmentNursingStationMapperDtoslist;
								var isCounterApplicable = false;
								
								$scope.orgNme = $.grep($scope.organizationList, function (doc) {
					                return doc.organizationId == $scope.organizationId;
					            })[0].organizationName;
								
								$scope.unitNme = $.grep($scope.unitList, function (doc) {
					                return doc.unitId == $scope.unitId;
					            })[0].unitName;
								
								sessionStorage.setItem("organizationId", $scope.organizationId);
								sessionStorage.setItem("unitId", $scope.unitId);
								sessionStorage.setItem("userId", response.data.object.userId);
								sessionStorage.setItem("token",response.data.object.authToken);
								//console.log("response.data.object.roleIds",response.data.object.roleIds);
												 sessionStorage.setItem("roleIds",response.data.object.roleIds);
												 var cookieObject = {'organizationId':$scope.organizationId, 'unitId':$scope.unitId,'userId':response.data.object.userId, 'authToken':response.data.object.authToken}
												 $cookies.putObject('cookieObject',cookieObject, {'domain': 'localhost', 'secure' : false});	
												 //console.log(data);
												 sessionStorage.setItem("organizationId",$scope.organizationId);
												 sessionStorage.setItem("unitId",$scope.unitId);
												 $localStorage.userId = response.data.object.userId;
												//console.log(data);
									            var data = {
									                roleIds: response.data.object.roleIds
									            }

								if (response.data.object.roleIds != null) {
									angular.forEach(response.data.object.roleIds, function (value, key) {

										roleIds.push(value.roleId);
										if (value.isCounterApplicable == 'Y') {
											isCounterApplicable = true;
										}

									});
								}
								var isUserCounterApplicable = isCounterApplicable == true ? 'Y' : 'N';
								sessionStorage.setItem('isUserCounterApplicable', JSON.stringify(isUserCounterApplicable));

								//console.log("roleIds", roleIds);
								//console.log("isUserCounterApplicable", isUserCounterApplicable);
								sessionStorage.setItem("roleIds", roleIds);

								var cookieObject = { 'organizationId': $scope.organizationId, 'unitId': $scope.unitId, 'userId': response.data.object.userId, 'doctorId': response.data.object.doctorId }
								$cookies.putObject('cookieObject', cookieObject, {'domain': 'localhost', 'secure' : false});
								//console.log(data);

								sessionStorage.setItem("userName", $scope.userName);
								sessionStorage.setItem("organizationId", $scope.organizationId);
								sessionStorage.setItem("unitId", $scope.unitId);
								sessionStorage.setItem("userId", response.data.object.userId);
								
								$scope.unitNmeNew = $scope.unitNme.replace(/[()]/g,'');
								
								var cookieObject = {
										'organizationId': $scope.organizationId, 
										'unitId': $scope.unitId, 
										'userId': response.data.object.userId, 
										'userName': $scope.userName, 
										'loginName': $scope.userName, 
										'doctorId': response.data.object.doctorId,
										'isRMO'   :response.data.object.isRMO,
										'unitCountryId':response.data.object.unitCountryId,
										'unitStateId':response.data.object.unitStateId,
										'unitDistrictId':response.data.object.unitDistrictId,
										'unitCityId':response.data.object.unitCityId,
										'unitAreaId':response.data.object.unitAreaId,
										'unitZipCode':response.data.object.unitZipCode,
										'orgNme':$scope.orgNme,
										'unitNme':$scope.unitNmeNew,
										'employeeId':response.data.object.employeeId,
										'authToken':response.data.object.authToken,
										'isSuperUser' : response.data.object.isSuperUser !=null ? response.data.object.isSuperUser :'N',
										'dashboard' : response.data.object.dashboardState,
										'isRenderedServiceCancelled' : response.data.object.renderedServiceCancelled,
										'updateDischargeSummary' : response.data.object.updateDischargeSummary,
										'isUpdatePatientReg' : response.data.object.isUpdatePatientReg,
										'autoBatch' : response.data.object.autoBatch,
										'atCopRequired' : response.data.object.atCopRequired,
										'autoPrescribedQty' : response.data.object.autoPrescribedQty,
										'isUpdateIpdChargesRemark' : response.data.object.isUpdateIpdChargesRemark,
										'mpesaRequired' : response.data.object.mpesaRequired,
										'opdBillCancellation' : response.data.object.opdBillCancellation,
										'ipdBillCancellation' : response.data.object.ipdBillCancellation,
										'billSettlement' : response.data.object.billSettlement,
										'ipBillSettlement' : response.data.object.ipBillSettlement,
										'serviceRateEditable' : response.data.object.serviceRateEditable,
										'isForceUpdateServicePackage' : response.data.object.isForceUpdateServicePackage,
										'nursingOpScan' : response.data.object.nursingOpScan,
										'nursingIpScan' : response.data.object.nursingIpScan,
										'doctorOpScan' : response.data.object.doctorOpScan,
										'doctorIpScan' : response.data.object.doctorIpScan,
										'changePayeeRights' : response.data.object.changePayeeRights,
										'extendPdd' : response.data.object.extendPdd,
										'isBillManipulationAllowedUserLevel' : response.data.object.isBillManipulationAllowed
										
								}
								
								if(response.data.object.doctorId > 0)
									{
									  cookieObject.userName = response.data.object.doctorName;
									  cookieObject.firstName = response.data.object.docFirstName;
									}
								else if(response.data.object.employeeId > 0)
									{
									  cookieObject.userName = response.data.object.employeeName;
									  cookieObject.firstName = response.data.object.firstName;
									}
								else 
									{
									  cookieObject.userName = 'Admin';
									  cookieObject.firstName = 'Admin';
									}
								$scope.roleId=response.data.object.roleIds[0].roleId;
								$cookies.putObject('cookieObject', cookieObject, {'domain': 'localhost', 'secure' : false});
								console.log('cookieObjecttttttttttttt', cookieObject);
								//console.log(data);
								$scope.checkABDMaccess();
								
								$rootScope.stopLoader();
	/*1*/						$scope.getCounterListByUser(response.data.object.userCounterMapperDtosList);
	/*2*/						$scope.getUnitApplicationBillingConfigByOrgUnit(response.data.object);
	/*3*/						$scope.getLabConfiguration(response.data.object.labConfigurationDto);
								$scope.getTemplateConfig(response.data.object.unitTemplateConfigDto);
								
	
								$scope.getUserOtTabs(response.data.object.userId);
								$scope.getMpesaRoles(response.data.object.userId);
								$rootScope.showTwilioNotification();
								var data = {
									roleIds: roleIds,
									organizationId:parseInt($scope.organizationId),
									unitId: parseInt($scope.unitId)
								}


								  $scope.getRmoRoster(cookieObject.isRMO,cookieObject.doctorId);
								  
								if (response.data.object.responseEntity.body.status = "success") {
											//console.log(response.data.object.responseEntity);
											//sessionStorage.setItem("permissions", JSON.stringify(response.data));
											localStorage.setItem("permissions", JSON.stringify(response.data.object.responseEntity.body));
											
											//Duo Login
											

											var found = $scope.organizationList.find(ar=>ar.organizationId == $scope.organizationId && ar.isDuoAuthenticationRequired == true);
											if(found != undefined){
												var cookieMacAdress =localStorage.getItem('macAddress');
												var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*()_+><";
												var string_length = 16;
												var randomstring = '';
												for (var i=0; i<string_length; i++) {
													var rnum = Math.floor(Math.random() * chars.length);
													randomstring += chars.substring(rnum,rnum+1);
												}
//												//console.log("user random number",randomstring);
												// code for generating random number for perticular user login start
												var data1 = {
													organizationId: $scope.organizationId,
													unitId: $scope.unitId,
													userName: $scope.userName,
//													username: $scope.userName,
													password: $scope.password,
													macAddress: cookieMacAdress!=undefined && cookieMacAdress !=null && cookieMacAdress !="" ? cookieMacAdress :randomstring
												}
												var URI1 = BASE_URL + ROOT_URL + "/ldapUserCredentialCheck";
												GenericService.serviceAction("POST", URI1, data1)
												.then(function (response) {
													if (response.data.status == 'success')
													{
														var cookieMacAdress =localStorage.getItem('macAddress');
														var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*()_+><";
														var string_length = 16;
														var randomstring = '';
														for (var i=0; i<string_length; i++) {
															var rnum = Math.floor(Math.random() * chars.length);
															randomstring += chars.substring(rnum,rnum+1);
														}
//														//console.log("user random number",randomstring);
														// code for generating random number for perticular user login start
														var data1 = {
															organizationId: $scope.organizationId,
															unitId: $scope.unitId,
															userName: $scope.userName,
//															username: $scope.userName,
															password: $scope.password,
															macAddress: cookieMacAdress!=undefined && cookieMacAdress !=null && cookieMacAdress !="" ? cookieMacAdress :randomstring
														}
														var URI1 = BASE_URL + ROOT_URL + "/login";
														GenericService.serviceAction("POST", URI1, data1)
														.then(function (response) {
//															if($scope.isDuoLogin == true){
																document.location.href=response.data;
//															}
														});
														
														
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.error("Invalid Credentials..!");
													}
												});
												/*var URI1 = BASE_URL + ROOT_URL + "/login";
												GenericService.serviceAction("POST", URI1, data1)
												.then(function (response) {
//													if($scope.isDuoLogin == true){
														document.location.href=response.data;
//													}
												});*/
											}else{
												if(response.data.object.dashboardState !=null)
												{
													if($scope.roleName =="doctor" && response.data.object.doctorId > 0){
														 $state.go('doctorDashboard');
													 }
													 else if($scope.roleName =="nurse" && response.data.object.employeeId > 0){
														 $state.go('nurseDashboard');
													 }else{
														 $state.go('dashboard');
													 }
												}else{
													$state.go("home");
												}
//												$state.go(response.data.object.dashboardState);
											}
											
										
											
											 /*if($scope.roleName =="doctor" && response.data.object.doctorId > 0){
												 $state.go('doctorDashboard');
											 }
											 else if($scope.roleName =="nurse" && response.data.object.employeeId > 0){
												 $state.go('nurseDashboard');
											 }else{
												 $state.go('dashboard');
											 }*/
											
											
											 
											//$rootScope.stopLoader();
										}
										else {
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									/*})*/
								
								
								$rootScope.stopLoader();
							}
							else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								$scope.subMsg = "";
								$scope.subMsg = response.data.message;
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}else{
								$rootScope.stopLoader();
							}
							
						});
						//$rootScope.stopLoader();
											


							};
							// $rootScope.skipPharmacySettlement=true;
							$rootScope.isPoApproval=false;
							$scope.getUnitApplicationBillingConfigByOrgUnit = function(mainObject) {
										  if(mainObject!=null)
										  {
												// $rootScope.skipPharmacySettlement= mainObject.skipPharmacySettlement;
												localStorage.setItem('isSkipPharmacySettlement', JSON.stringify(mainObject.skipPharmacySettlement));
												$rootScope.isPoApproval=mainObject.poApproval;
												
											 var object = mainObject.unitApplicationConfigurationDto;

											  var i = 1;
											  angular.forEach($scope.tempRoleIds, function (value, key) {

												  if(object.docRoleId == value.roleId && mainObject.doctorId > 0)
													{
													  $scope.doctorRole = 1;
													}
												 if(object.nurseRoleId == value.roleId && mainObject.employeeId > 0)
													{
													   $scope.nurseRole = 1;
													}
													 if(object.staffRoleId == value.roleId && mainObject.employeeId > 0)
                                                    {
                                                    	 $scope.staffRoleId = 1;
                                                    }
												 	
												 if(i==$scope.tempRoleIds.length)
												 {
													 if($scope.doctorRole==1 && mainObject.doctorId > 0)
													  {
														  $scope.roleName = "doctor";
													  }else if($scope.nurseRole==1 && mainObject.employeeId > 0)
													  {
														  $scope.roleName = "nurse";
													  }
													  else if($scope.staffRoleId==1 && mainObject.employeeId > 0)
                                                       {
                                                      	$scope.roleName = "Receptionist";
                                                       }
													  else
													  {
														  $scope.roleName = "admin";
													  }
												 }
												 i++;
												});
											  
											 
											  
											 var cookieObjectAppConfig = 
											 {		 
													 'vaccinationChartHide' :object.vaccinationChartHide,
													 'defaultSelfTariffId':object.defaultSelfTariffId, 
													 'isServiceWiseSplitBill':object.isServiceWiseSplitBill,
													 'isPreOrPostBilling':object.isPreOrPostBilling,
													 'isAppointmentRequired':object.isAppointmentRequired,
													 'isCounterApplicable':object.isCounterApplicable,
													 'radiologySpecialityId':object.radiologySpecialityId,
													 'hemodialysisSpecialityId':object.hemodialysisSpecialityId,
													 'assignPickerRequired':object.assignPickerRequired,
													 'patientCategoryId':object.patientCategoryId,
													 'issueMaterialPickerRequired':object.issueMaterialPickerRequired,
													 'taxInclusive':object.taxInclusive,
													 'isMarkup': object.isMarkup,
													 'roleName':$scope.roleName,
													 'doctorRole':$scope.doctorRole != null ? $scope.doctorRole : 0,
													 'nurseRole':$scope.nurseRole != null ? $scope.nurseRole : 0,
													 'doctorRoleId':object.docRoleId,
													 'nurseRoleId':object.nurseRoleId,
													 'opdBillingCatId': object.opdBillingCatId,
													 'casualtySpecialityId':object.casualtySpecialityId,
													 'paediatricsSpecialityId':object.paediatricsSpecialityId,
													 'selfPayeeId' : object.selfPayeeId,
													 'pharmacyServiceId' : object.pharmacyServiceId,
													 'companyPatientCategory':object.companyPatientCategory,
													 'insurancePatientCategory':object.insurancePatientCategory,
													 'isKiosk': object.isKioskRequired,
													 'surchargeServiceId' : object.surchargeServiceId,
													 'senderId' : object.senderId,
													 'isRenderedServiceRateEditable' :object.isRenderedServiceRateEditable,
													 'specialityId' :object.erSpecialityId,
													 'isNursingAssessment' : object.isNursingAssessment,
													 "isShowPatientByBed" :object.isShowPatientByBed,
													 "groupPracticeNo" :object.groupPracticeNo,
													 "groupPracticeName" :object.groupPracticeName,
													 "practiceNo" :object.practiceNo,
													 "startDateFormat" :object.startDateFormat,
													 "isAdviceTabVisible" :object.isAdviceTabVisible,
													 'isPinPadRequired' : object.isPinPadRequired,
													 'serviceWiseDoctorListReq' : object.serviceWiseDoctorListReq,
													 'gradeWiseSeviceApplicable' : object.gradeWiseSeviceApplicable,
													 'registeredPinPadDevice' : object.registeredPinPadDevice,
													 'isSmartCardRequired' : object.isSmartCardRequired,
													 'isPACSRequired' : object.isPACSRequired,
													 'maxAge' :125,
													 'minAge' :0,
													 'isConsolidatedBill':object.isConsolidatedBill,
													 'assetMaintenanceSpecialityId':object.assetMaintenanceSpecialityId,
													 'consultationSpecialityId':object.consultationSpecialityId,
													 'defaultPatientCategoryId':object.defaultPatientCategoryId,
													 'physiotherapySpecialityId':object.physiotherapySpecialityId,
													 //'isProcessShareAgainstSettledBill':object.isProcessShareAgainstSettledBill,
													 'badDebitDuration':object.badDebtsDays,
													 'morningFromTime':object.morningFromTime,
													 'morningToTime':object.morningToTime,
													 'eveningFromTime':object.eveningFromTime,
													 'eveningToTime':object.eveningToTime,
													 'pharmacyServiceId':object.pharmacyServiceId,
													 'otRentServiceId':object.otRentServiceId,
													 'refreshPageTime' : object.refreshTime,
													 'isSchemeDiscount' : object.isSchemeDiscount,
													 'isBackDatedAdmission' : object.isBackDatedAdmission,
													 'skipReadyForBilling' : object.skipReadyForBilling,
													 'isFreeVisitReq' : object.isFreeVisitReq,
													 'isHospitalVisitReq' : object.isHospitalVisitReq,
													 'isReviewAppReq' : object.isReviewAppReq,
													 'isLabReq' : object.isLabReq,
													 'isEHCReq' : object.isEHCReq,
													 'isRadiologyReq' : object.isRadiologyReq,
													 'isDocConsultReq' : object.isDocConsultReq,
													 'host':object.host,
													 'port':object.port,
													 'id':object.id,
													 'password':object.password,
													 'skipAdmissionAcceptance' : object.skipAdmissionAcceptance,
													 'isGstVisible' : object.isGstVisible,
													 'skipHousekeeping' : object.skipHousekeeping,
													 'optimizeTransferFlow' : object.optimizeTransferFlow,
													 'allowOtherDoctorToConsult' : object.allowOtherDoctorToConsult,
													 'diagnosisRequire' : object.diagnosisRequire,
													 'skipRISReportEntry' : object.skipRISReportEntry,
													 'defaultDiscountOwnership' : object.defaultDiscountOwnership,
													 'isPharmacologicalValidationSkipIp' : object.isPharmacologicalValidationSkipIp != null ? object.isPharmacologicalValidationSkipIp :false,
													 'opStatNotRequired' : object.opStatNotRequired != null ? object.opStatNotRequired :false,
													 'ipStatNotRequired' : object.ipStatNotRequired != null ? object.ipStatNotRequired :false,
													 "isRateVisibleOnCoversheet" :object.isRateVisibleOnCoversheet,	 
													 "isSendMailOnMailPrescription" :object.isSendMailOnMailPrescription,
													 "isVitalQualifierVisible" :object.isVitalQualifierVisible,	 
													 "isAdvisedDateVisible" :object.isAdvisedDateVisible,
													 "kioskLandingScreen" :object.kioskLandingScreen,
													 "doctorSlipTDS" :object.doctorSlipTDS,
													 "cashAmountValidation" :object.cashAmountValidation,
													 'skipTransferAcceptance' : object.skipTransferAcceptance,
													 'isRisPatientDetailsOnFirstPage':object.isRisPatientDetailsOnFirstPage,
													 'isPackageApplicableDateEditable':object.isPackageApplicableDateEditable,
													 'isKioskAnnouncementRequired':object.isKioskAnnouncementRequired,
													 "cashAmountLimit" :object.cashAmountLimit,
													 "blockReportPrint" :object.blockReportPrint,
													 "payGatewayProviderId" :object.payGatewayProviderId,
													 "emailValidationRequired" :object.emailValidationRequired,
													 "dateFormat" :object.dateFormat,
													 'neuronPlus' : object.neuronPlus,
													 "depositTypeWiseRequired" :object.depositTypeWiseRequired,
													 "cimsOption" :object.cimsOption,
													 "tokenNumber":object.tokenNumber,
													 "mobileValidationRequired" :object.mobileValidationRequired,

												 }
											 	 $rootScope.skipRISReportEntry= object.skipRISReportEntry;
												 sessionStorage.setItem('isCounterApplicable', JSON.stringify(object.isCounterApplicable));	
												 $cookies.putObject('cookieObjectAppConfig',cookieObjectAppConfig);
												console.log('cookieObjectAppConfiggggggg',cookieObjectAppConfig);
												var cookieObjectAppConfigSecond =
	                                             {
	                                                     "exceededBillAmountSettlement":object.exceededBillAmountSettlement,
	                                                     "runTimeRateChangeUsingTarrif":object.runTimeRateChangeUsingTarrif,
	                                                     "qmsPrnPrint" : object.qmsPrnPrint,
	                                                     "dischargeSummaryDataViewOptionId":object.dischargeSummaryDataViewOptionId,//sneha
	                                                     "isEmrRequired" : object.isEmrRequired,
	                                                     "ambulanceChargesServiceId" : object.ambulanceChargesServiceId,
	                                                     "tableHeaderColor": object.tableHeaderColor,
	                                                     "gstForBillingBedCategory":object.gstForBillingBedCategory,
	                                                     "singlePageRefresh" : object.singlePageRefresh,
	                                                     "roa" : object.roa,
	                                                     "dosage" : object.dosage,
	                                                     "mandatoryScan" :object.mandatoryScan,
														 'isBillManipulationAllowedAppLevel' : object.isBillManipulationAllowed,
														 "backDateAdmissionDays" : object.backDateAdmissionDays,
														 "appointmentUniqueIdSearch":object.appointmentUniqueIdSearch,
														 "couponSystemApplicable":object.couponSystemApplicable,
														 'admissionCancellationTime':object.admissionCancellationTime,
														 'timeZone':mainObject.unitTimeZone,
														 'roleId':$scope.roleId,
														 'invoiceCreationInsurance':object.invoiceCreationInsurance,
														 'invoiceCreationCorporate':object.invoiceCreationCorporate,
														 'isAiButtonConfigc':object.isAiButtonConfigc,
														 'drugTodrugCheck':object.drugTodrugCheck,
														 'navBarImg':$scope.navBarImg,
														 "whatsAppOption" :object.whatsAppOption,
														 "duplicateTab":object.duplicateTab,
														 "ayushmanBharatIntegration":object.ayushmanBharatIntegration,
														 "mobileDigit":object.mobileDigit,
														 "staffRoleId":object.staffRoleId,
														 'staffRole':$scope.staffRoleId != null ? $scope.staffRoleId : 0,
														 'maxInputAmount':object.maxInputAmount,
														 'isAiDoctorEnabled':object.isAiDoctorEnabled,
														 'erPatientNormalObj':{},
	                                             }
												localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));
												
											 }
											// //console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							}
							
							$scope.getCounterListByUser = function(object)
							{
									    var counterList = [];
									    var data = {"id":"","name":""}
										if (object!=null) 
										{
											 angular.forEach(object, function(value, key) 
												{
												   var dataObj = angular.copy(data); 
												   dataObj.id =  value.counterId;
												   dataObj.name = value.counterDesc;
												   counterList.push(dataObj);
												});
											 localStorage.setItem('counterList', counterList!=null?JSON.stringify(counterList):"");	
										 }
							
							}
							
							$rootScope.accessOfAbdm = false;
							
							$scope.checkABDMaccess = function()
							{
								$scope.startLoader();
								var data ={
										organizationId: $scope.organizationId,
										unitId: $scope.unitId,
								}

								var URI = BASE_URL+ ROOT_URL+ "/api/abdm/checkAccessOfABDMOfUnit";
							 	GenericService
					  			.serviceAction("POST", URI, data)
					  			.then(function(response) {
									$rootScope.stopLoader();
									if(response.data.object != null){

										$rootScope.accessOfAbdm = true;
										 // Retrieve cookieObjectAppConfigSecond from cookies
						                var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond')) || {};

						                // Add the new value to the object
						                cookieObjectAppConfigSecond.accessOfAbdm = $rootScope.accessOfAbdm; // Replace `newKey` and `newValue` with your actual key and value

						                // Save the updated object back to cookies
						                localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));


									}

								});
							}
							
				$scope.getLabConfiguration = function(object)
				{
					if (object!=null) 
					{
						
                      var labConfig = 
                             {
								    "labSpecialityId": object.labSpecialityId,
							        "biochemistryId": object.biochemistryId,
							        "hematologyId": object.hematologyId,
							        "microbiologyId": object.microbiologyId,
							        "histopathologyId": object.histopathologyId,
							        "cystopathologyId": object.cystopathologyId,
							        "serologyId": object.serologyId,
							        "isPrebarcodeGenerantion": object.isPrebarcodeGenerantion,
							        "isPinApplicable": object.isPinApplicable,
							        "clinicalPathologyId": object.clinicalPathologyId,
							        "isSendToCrApplicable":object.isSendToCrApplicable,
							        "isSampleAcceptanceApplicable":object.isSampleAcceptanceApplicable,
							        "isAcceptanceInDepartment":object.isAcceptanceInDepartment,
							        "isWorklistDepartment":object.isWorklistDepartment,
							        "isReportRecall":object.isReportRecall,
							        "isValidationAndRelease":object.isValidationAndRelease,
							        "reportReleasePin":object.reportReleasePin,
							        "photoRequired" : object.photoRequired,
							        "consolidatedResultEntry" : object.consolidatedResultEntry,
							        "groupRelease" :object.groupRelease,
							        "triggerReleaseMail" :object.triggerReleaseMail,
							        "isPRNPrint" :object.isPRNPrint,
							        "prnFormat" :object.prnFormat,
							        "prnUrl" :object.prnUrl,
							        "collectSampleOnOPBilling" :object.collectSampleOnOPBilling,
							        "labSortingId" :object.labSortingId,
							        "skipValidation" :object.skipValidation,
							        "skipValidation" :object.skipValidation,
							        "sendMailOnOrderCompletion" : object.sendMailOnOrderCompletion,
							        "manualSampleCollectionTime" : object.manualSampleCollectionTime,
							        "incubationMasterApplicable" : object.incubationMasterApplicable,
							        "macroscopicExaminationApplicable" : object.macroscopicExaminationApplicable,
							        "biochemicalTestingApplicable" : object.biochemicalTestingApplicable,
							        "outsourceLabId" : object.outsourceLabId,
							        "microscopicExaminationApplicable" : object.microscopicExaminationApplicable,
							        "printLabReportBeforRelease" : object.printLabReportBeforRelease
 						     }
                      
                         $rootScope.isSendToCrApplicable= object.isSendToCrApplicable;
                       	 $rootScope.isSampleAcceptanceApplicable= object.isSampleAcceptanceApplicable;
                       	 $rootScope.isAcceptanceInDepartment= object.isAcceptanceInDepartment;
                       	 $rootScope.isWorklistDepartment= object.isWorklistDepartment;
                       	 $rootScope.isReportRecall= object.isReportRecall;
                       	 $rootScope.isValidationAndRelease= object.isValidationAndRelease;
                    	 $rootScope.skipValidation= object.skipValidation;
                       	 $rootScope.consolidatedResultEntry = object.consolidatedResultEntry;
                       	$rootScope.incubationMasterApplicable = object.incubationMasterApplicable;
                       	$rootScope.macroscopicExaminationApplicable = object.macroscopicExaminationApplicable;
                       	$rootScope.biochemicalTestingApplicable = object.biochemicalTestingApplicable;
                       	$rootScope.microscopicExaminationApplicable = object.microscopicExaminationApplicable;
						 localStorage.setItem('labConfig', JSON.stringify(labConfig));
						 console.log('labConfig',labConfig);
					}	
						 
					$rootScope.getUnitByOrganization();
				}
				
				
				
				$scope.getTemplateConfig = function(object)
				{
					if (object!=null) 
					{
						
                      var templateConfig = 
                             {
								    "apiKey": object.apiKey,
							        "templateUrl": object.templateUrl,
							        "templateForm": object.templateForm
						     }
                      
						 localStorage.setItem('templateConfig', JSON.stringify(templateConfig));
					}	
						 
				}
				
				
				$rootScope.userOtTabs = {
						otDetails:false,
						procedureDetails:false,
						service:false,
						inventry:false,
						equipment:false,
						cssdKit:false,
						notes:false,
						timeSheet:false,
                        medication:false,
                        implant:false,
                        medicalGas:false,
                        anesthesiaNotes: false,
                        surgeryNotes: false,
                        dischargeNotes : false,
                        followUpNotes : false
				};
				
				$scope.getUserOtTabs = function(userId){
						$rootScope.startLoader();
					var URI = BASE_URL+ ROOT_URL+ "/api/global/getUserOtTabsByUserId/"+userId;
					GenericService.serviceAction("GET", URI,{}).then(function(response) {
						$rootScope.stopLoader();
						if(response.data.listObject != null && response.data.listObject.length>0){
							
							$rootScope.userOtTabs.cssdKit = response.data.listObject[0].cssdKit;
							$rootScope.userOtTabs.equipment = response.data.listObject[0].equipment;
							$rootScope.userOtTabs.inventry = response.data.listObject[0].inventry;
							$rootScope.userOtTabs.notes = response.data.listObject[0].notes;
							$rootScope.userOtTabs.otDetails = response.data.listObject[0].otDetails;
							$rootScope.userOtTabs.procedureDetails = response.data.listObject[0].procedureDetails;
							$rootScope.userOtTabs.service = response.data.listObject[0].service;
							$rootScope.userOtTabs.timeSheet = response.data.listObject[0].timeSheet;
                            $rootScope.userOtTabs.medication = response.data.listObject[0].medication;
                            $rootScope.userOtTabs.implant = response.data.listObject[0].implant;
                            $rootScope.userOtTabs.medicalGas = response.data.listObject[0].medicalGas;
                            $rootScope.userOtTabs.anesthesiaNotes = response.data.listObject[0].anesthesiaNotes;
                            $rootScope.userOtTabs.surgeryNotes = response.data.listObject[0].surgeryNotes;
                            $rootScope.userOtTabs.dischargeNotes  = response.data.listObject[0].dischargeNotes;
                            $rootScope.userOtTabs.followUpNotes  = response.data.listObject[0].followUpNotes;
						}
						
					});
				}
				
				
				
				$scope.getMpesaRoles = function(userId){
					$rootScope.startLoader();
				var URI = BASE_URL+ ROOT_URL+ "/api/global/getUserById/"+userId+"/"+$scope.unitId;
				GenericService.serviceAction("GET", URI,{}).then(function(response) {
					$rootScope.stopLoader();
					if(response.data.listObject != null && response.data.listObject.length>0){
						
						localStorage.setItem('mpesaRigths', JSON.stringify(true));
					}else{
						
						localStorage.setItem('mpesaRigths', JSON.stringify(false));
					}
					
				});
			}
				
				
				// continue to login 
				$scope.continueLogin=function(){

					// $rootScope.loginpage= true;
					$rootScope.startLoader();
					// code for generating random number for perticular user login start
					var cookieMacAdress =localStorage.getItem('macAddress');
					var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*()_+><";
					var string_length = 16;
					var randomstring = '';
					for (var i=0; i<string_length; i++) {
						var rnum = Math.floor(Math.random() * chars.length);
						randomstring += chars.substring(rnum,rnum+1);
					}
					//console.log("user random number",randomstring);
					//console.log("Cookied mac ",cookieMacAdress);
					// code for generating random number for perticular user login start
					var data = {
						organizationId: $scope.organizationId,
						unitId: $scope.unitId,
						// userName: $scope.userName,
						// password: $scope.password,
						userName: $scope.encryptData($scope.userName),
						password: $scope.encryptData($scope.password),
						isContinueLogin:'Y',
						macAddress: cookieMacAdress!=undefined && cookieMacAdress !=null && cookieMacAdress !="" ? cookieMacAdress :randomstring
					}
					var macAddress=cookieMacAdress!=undefined && cookieMacAdress !=null && cookieMacAdress !="" ? cookieMacAdress :randomstring;
					$scope.tempRoleIds = [];
					$scope.departmentNursingStationMapperDtoslist = [];
					var URI = BASE_URL + ROOT_URL + USER_LOGIN;
					GenericService.serviceActionForSubscription("POST", URI, data)
						.then(function (response) {

							if (response.data.object != null) {	
								// Code for setting cookies value for login user start 
							/*	if(response.data.object==1){
									$rootScope.stopLoader();
									angular.element('#loginContinuePopUp').modal('show');
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
									return false;
								}*/
								var expireDate = new Date();
								expireDate.setDate(expireDate.getDate() + 60);
							    localStorage.setItem('macAddress', macAddress);
//							    $cookies.put('macAddress', randomstring);
								// Code for setting cookies value for login user End  
								// Setting a cookie

								var roleIds = [];
								$scope.tempRoleIds = response.data.object.roleIds;
								$scope.departmentNursingStationMapperDtoslist = response.data.object.departmentNursingStationMapperDtoslist;
								var isCounterApplicable = false;
								
								$scope.orgNme = $.grep($scope.organizationList, function (doc) {
					                return doc.organizationId == $scope.organizationId;
					            })[0].organizationName;
								
								$scope.unitNme = $.grep($scope.unitList, function (doc) {
					                return doc.unitId == $scope.unitId;
					            })[0].unitName;
								
								sessionStorage.setItem("organizationId", $scope.organizationId);
								sessionStorage.setItem("unitId", $scope.unitId);
								sessionStorage.setItem("userId", response.data.object.userId);
								sessionStorage.setItem("token",response.data.object.authToken);
								//console.log("response.data.object.roleIds",response.data.object.roleIds);
												 sessionStorage.setItem("roleIds",response.data.object.roleIds);
												 var cookieObject = {'organizationId':$scope.organizationId, 'unitId':$scope.unitId,'userId':response.data.object.userId, 'authToken':response.data.object.authToken}
												 $cookies.putObject('cookieObject',cookieObject, {'domain': 'localhost', 'secure' : false});	
												// //console.log(data);
												 sessionStorage.setItem("organizationId",$scope.organizationId);
												 sessionStorage.setItem("unitId",$scope.unitId);
												 $localStorage.userId = response.data.object.userId;
												//console.log(data);
									            var data = {
									                roleIds: response.data.object.roleIds
									            }

								if (response.data.object.roleIds != null) {
									angular.forEach(response.data.object.roleIds, function (value, key) {

										roleIds.push(value.roleId);
										if (value.isCounterApplicable == 'Y') {
											isCounterApplicable = true;
										}

									});
								}
								var isUserCounterApplicable = isCounterApplicable == true ? 'Y' : 'N';
								sessionStorage.setItem('isUserCounterApplicable', JSON.stringify(isUserCounterApplicable));

								//console.log("roleIds", roleIds);
								//console.log("isUserCounterApplicable", isUserCounterApplicable);
								sessionStorage.setItem("roleIds", roleIds);

								var cookieObject = { 'organizationId': $scope.organizationId, 'unitId': $scope.unitId, 'userId': response.data.object.userId, 'doctorId': response.data.object.doctorId }
								$cookies.putObject('cookieObject', cookieObject, {'domain': 'localhost', 'secure' : false});
								//console.log(data);

								sessionStorage.setItem("userName", $scope.userName);
								sessionStorage.setItem("organizationId", $scope.organizationId);
								sessionStorage.setItem("unitId", $scope.unitId);
								sessionStorage.setItem("userId", response.data.object.userId);
								$scope.unitNmeNew = $scope.unitNme.replace(/[()]/g,'');
								var cookieObject = {
                                										'organizationId': $scope.organizationId,
                                										'unitId': $scope.unitId,
                                										'userId': response.data.object.userId,
                                										'userName': $scope.userName,
                                										'loginName': $scope.userName,
                                										'doctorId': response.data.object.doctorId,
                                										'isRMO'   :response.data.object.isRMO,
                                										'unitCountryId':response.data.object.unitCountryId,
                                										'unitStateId':response.data.object.unitStateId,
                                										'unitDistrictId':response.data.object.unitDistrictId,
                                										'unitCityId':response.data.object.unitCityId,
                                										'unitAreaId':response.data.object.unitAreaId,
                                										'unitZipCode':response.data.object.unitZipCode,
                                										'orgNme':$scope.orgNme,
                                										'unitNme':$scope.unitNmeNew,
                                										'employeeId':response.data.object.employeeId,
                                										'authToken':response.data.object.authToken,
                                										'isSuperUser' : response.data.object.isSuperUser !=null ? response.data.object.isSuperUser :'N',
                                										'dashboard' : response.data.object.dashboardState,
                                										'isRenderedServiceCancelled' : response.data.object.renderedServiceCancelled,
                                										'updateDischargeSummary' : response.data.object.updateDischargeSummary,
                                										'isUpdatePatientReg' : response.data.object.isUpdatePatientReg,
                                										'autoBatch' : response.data.object.autoBatch,
                                										'atCopRequired' : response.data.object.atCopRequired,
                                										'autoPrescribedQty' : response.data.object.autoPrescribedQty,
                                										'isUpdateIpdChargesRemark' : response.data.object.isUpdateIpdChargesRemark,
                                										'mpesaRequired' : response.data.object.mpesaRequired,
                                										'opdBillCancellation' : response.data.object.opdBillCancellation,
                                										'ipdBillCancellation' : response.data.object.ipdBillCancellation,
                                										'billSettlement' : response.data.object.billSettlement,
                                										'ipBillSettlement' : response.data.object.ipBillSettlement,
                                										'serviceRateEditable' : response.data.object.serviceRateEditable,
                                										'isForceUpdateServicePackage' : response.data.object.isForceUpdateServicePackage,
                                										'nursingOpScan' : response.data.object.nursingOpScan,
                                										'nursingIpScan' : response.data.object.nursingIpScan,
                                										'doctorOpScan' : response.data.object.doctorOpScan,
                                										'doctorIpScan' : response.data.object.doctorIpScan,
                                										'changePayeeRights' : response.data.object.changePayeeRights,
                                										'extendPdd' : response.data.object.extendPdd,
                                										'isBillManipulationAllowedUserLevel' : response.data.object.isBillManipulationAllowed

                                								}
								  
								
								if(response.data.object.doctorId > 0)
									{
									  cookieObject.userName = response.data.object.doctorName;
									  cookieObject.firstName = response.data.object.docFirstName;
									}
								else if(response.data.object.employeeId > 0)
									{
									  cookieObject.userName = response.data.object.employeeName;
									  cookieObject.firstName = response.data.object.firstName;
									}
								else 
									{
									  cookieObject.userName = 'Admin';
									  cookieObject.firstName = 'Admin';
									}
								$cookies.putObject('cookieObject', cookieObject, {'domain': 'localhost', 'secure' : false});
								$scope.getRmoRoster(cookieObject.isRMO,cookieObject.doctorId);
								//console.log(data);
								$rootScope.stopLoader();
	/*1*/						$scope.getCounterListByUser(response.data.object.userCounterMapperDtosList);
	/*2*/						$scope.getUnitApplicationBillingConfigByOrgUnit(response.data.object);
	/*3*/						$scope.getLabConfiguration(response.data.object.labConfigurationDto);
								$scope.getTemplateConfig(response.data.object.unitTemplateConfigDto);
	
	
		
								$scope.getUserOtTabs(response.data.object.userId);
								$scope.getMpesaRoles(response.data.object.userId);
								
								var data = {
									roleIds: roleIds,
									organizationId:parseInt($scope.organizationId),
									unitId: parseInt($scope.unitId)
								}

								if (response.data.object.responseEntity.body.status = "success") {
											//console.log(response.data.object.responseEntity);
											//sessionStorage.setItem("permissions", JSON.stringify(response.data));
											localStorage.setItem("permissions", JSON.stringify(response.data.object.responseEntity.body));
											
											/*if($scope.roleName =="doctor" && response.data.object.doctorId > 0){
												 $state.go('doctorDashboard');
											 }
											 else if($scope.roleName =="nurse" && response.data.object.employeeId > 0){
												 $state.go('nurseDashboard');
											 }else{
												 $state.go('dashboard');
											 }*/
											//$rootScope.stopLoader();
											if(response.data.object.dashboardState !=null)
											{
												$state.go(response.data.object.dashboardState);
											}else{
												$state.go("home");
											}
										}
										else {
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									/*})*/
								
								
								$rootScope.stopLoader();
							}
							else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}else{
								$rootScope.stopLoader();
							}
						});
						//$rootScope.stopLoader();
											


							
				}
				
				  $scope.getRmoRoster=function(isRMO,doctorId){
				    $scope.isRMO=isRMO;
				    $scope.cookieDoctorId =doctorId;
				  if($scope.isRMO=='Y'){
					  
				  	var URI =  BASE_URL + ROOT_URL+"/api/unit/clinical/getActiveRmoAllocationMasterByRMODoctorId";
				  	var data={
				  			rmoDoctorId:$scope.cookieDoctorId,
				  			organizationId:$scope.organizationId,
				  			unitId:$scope.unitId
				  	}
				  	GenericService
				  			.serviceAction("POST", URI, data)
				  			.then(
				  					function(response) {
				  						if (response.data.status == "success") 
				  						{
				  							if(response.data.listObject !=null && response.data.listObject.length > 0)
				  							{
				  								 localStorage.setItem('rmoMappedOPDoctorList', JSON.stringify(response.data.listObject));
				  							}
				  							if(response.data.object !=null && response.data.object.length > 0)
				  							{
					  							 localStorage.setItem('rmoMappedIPWard', JSON.stringify(response.data.object));
				  							}
				  							//	$cookies.putObject('rmoVisit',$scope.visitTypeId);
				  							
				  						}
				  					});	
				   }
				  }


				  $scope.subScriptionPay = () =>{
					  GenericService.subScriptionPay()
				  }
				  $scope.clearSubMsg = () =>{
					$scope.subMsg = null;
				  }
				  
				  
					$rootScope.showTwilioNotification = function(){
						var cookieObject = $cookies.getObject('cookieObject');
							$rootScope.cookieObjectTEST = $cookies.getObject('cookieObject');
						if(cookieObject != undefined && cookieObject.doctorId != null & cookieObject.doctorId>0){
						var url= BASE_URL+ ROOT_URL+'/api/getVideoataForNotification';
						var totalNotifications = 0;
						 var twiliobaseUrlLocation = window.location.href;
							var twiliotemp = twiliobaseUrlLocation.split('/');
							var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
						var data =
						{
						    "unitId": cookieObject.unitId,
				            "doctorId": cookieObject.doctorId
						}
						var totalNotifications = 0;
						$.ajax({
							type : "post",
							url : url,
							contentType : "application/json",
					        data : JSON.stringify(data),
							success : function(response) {
					
									var status = response.status;
									var list = response.listObject;
									 $('#twilioNotificationDiv').empty();
									 angular.forEach(list, function(val) {
										 
										 totalNotifications = totalNotifications + 1;
								            var src = BASE_URL + ROOT_URL + "resources/assets/images/tempImages/m.jpg";
								            if (val.patientPhoto != null) {
								                src = val.patientPhoto;
								            } else if (val.genderId == 1) {
								                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/m.jpg";
								            } else {
								                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/patient.png";
								            }

								            var checkinbutton = "Join";
								            if (val.appointmentStatusId == 1) {
								                checkinbutton = "Checkin and Join";
								            }

								            var myvar = '<div class="row padding-top10">' +
								                '<div class="notificationBoxContaint padding-top-bottom5"' +
								                'style="padding-left: 0px !important; padding-right: 15px !important; padding-bottom: 10px;">' +
								                '<div class="row padding-top-bottom5">' +
								                '<div class="col-lg-1 col-md-1 col-sm-1">' +
								                '<hr' +
								                'style="margin-top: 7px !important; margin-bottom: 0px !important; border-top: 10px solid rgba(84, 249, 106, 1) !important; width: 10px; border-radius: 20px !important;">' +
								                '</div>' +
								                '<div class="col-lg-6 col-md-8 col-sm-8">' +
								                '<span>Teleconsultation Alert</span>' +
								                '</div>' +
								                '<div class="col-lg-5 col-md-3 col-sm-3 text-right">' +
								                '<span>' + val.appointmentDate + '</span>' +
								                '</div>' +
								                '</div>' +
								                '<div class="row">' +
								                ' <div class="col-lg-3 col-md-3 col-sm-3">'+
												 '					<div class="row text-center" style="padding-top: 4px;">'+
												 '						<img'+
												 '							src="'+src+'"'+
												 '							style="width: 70px; height: 70px; border-radius: 50px;">'+
												 '					</div>'+
												 '				</div>'+
								                '<div class="col-lg-9 col-md-9 col-sm-9 text-right">' +
								                '<div class="row text-center patientDetailsMsgs"' +
								                'style="font-size: 12px !important;">' +
								                '<span class="patientDetailsMsg"' +
								                'style="font-size: 12px !important;">' + val.patientName +
								                '</span> is ready and waiting in the room for the' +
								                '<span class="patientDetailsMsg"' +
								                'style="font-size: 12px !important;">Teleconsultation' +
								                'appointment at ' + val.timeSlot + '</span>' +
								                '</div>' +
								                '<div class="row padding-top10">' +
								                '<button type="button" class="btn btn-primary width-auto" style="border-radius: 10px; min-width: 110px;" ng-click="joinByDoctorByNotification(' + val.appointmentIdMyLife + ',' + val.userChatDetailsId + ');">' + checkinbutton + '</button>' +
								                '</div>' +
								                '</div>' +
								                '</div>' +
								                '</div>' +
								                '</div>';

								            // Append the compiled HTML
								            var compiledElement = $compile(myvar)($rootScope);
								            angular.element(document.getElementById('twilioNotificationDiv')).append(compiledElement);
								           
								        });
									     $('#totalNotifications').text(totalNotifications);
								        // Apply the changes to the scope
									     $timeout(function() {
									            $rootScope.$apply();
									        });
									if(list.length==0){
										 $('#twilioNotificationDiv').append('<div style="text-align: center;">No notification.</div>');
									}
								},error : function(){
									console.log("Error");
									  stopLoader();
								}
							})
						
						}
					}
					$rootScope.showTwilioNotification();
					
					
					$scope.clearVari = function(){
						$scope.emailId = "";
						$scope.otp = "";
					}

					$scope.encryptData = function (plainText) {

						var publicKey = `MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyqnkfveWPGIrafL0irZeg4xZI/9DtdaWkxOiXhIp1RWtsgzDj3zSTNCEfr6s1YJCJImmRI/+lDzlv/9Y0nHyMM/7FfeYllQ6FGGGL/08vn/2Pyfmf5DJpvbboV2qcqwUTax1Ffwfx3Lgp8N1BTYciQG3C9fZJsT1jG7DDNk/N1UZYwgHyykKQR89OQC+J+fvxf8+qXWYTgnIR57Cofu6V5+mkVzXef6C49p9gSapI7ANd5/tOzVr1xg+g9f07r3GdZlF11XBZDJG9uRKlP1znY+LLFiFZT43trNRXk/ff05KXJIx21Ru/xq+BpsfXSZtdqurBbwxpapg67cqr0JknQIDAQAB`;

						var encrypt = new JSEncrypt();
						encrypt.setPublicKey(publicKey);

						var encrypted = encrypt.encrypt(plainText);

						return encrypted; // Base64 encrypted string
					};

			}]);
