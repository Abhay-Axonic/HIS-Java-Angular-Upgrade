'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:autoServicePostingConfigurationController
 * @description #autoServicePostingConfigurationController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'autoServicePostingConfigurationController',
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
						'reportService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,BillingGenericService,reportService) {

							/* init() function for form object create. */
							$scope.init = function() {
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId =cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.serviceObj = {
										serviceId : "",
										postingTime : "",
								}
								
								$scope.periodicServiceObj = {
										serviceId : ""
								},
								
								$scope.servicesConfigurationList = [];
								$scope.periodicServicesList = [];
								$scope.periodicServicesConfigurationList = [];
								$scope.periodicServiceId = null;
								///service/autoRendered/{orgId}/unit/{unitId}
								var URI = BASE_URL + ROOT_URL+ "/billing/service/autoRendered/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success")
										$scope.consultationServiceList = response.data.listObject;
										$scope.consultationServiceListForPeriodic = response.data.listObject;
										console.log('service list',$scope.consultationServiceList)
								});
								
							}

							$scope.init();
							
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAutoServicePosting();
							}

							
							
							
							//Start for checking 0 phatmacy quantity 
							  var isServiceExists;
								function checkServiceExists(arr,id) {
									return arr
											.some(function(arr) {
												if(parseInt(arr.serviceId) == id)
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
								
							$scope.addService = function()
							{
								if($scope.serviceObj.serviceId == "" || $scope.serviceObj.serviceId == null || $scope.serviceObj.postingTime == undefined)
									{
										growl.warning('Please select service.',
												{
													title : 'Warning!'
												});
										return false;
									}
								else if($scope.serviceObj.postingTime == "" || $scope.serviceObj.postingTime == null || $scope.serviceObj.postingTime == undefined){
									  growl.warning('Please select posting time.',
											{
												title : 'Warning!'
											});
									  return false;
								}
								else{
									
									if($scope.servicesConfigurationList.length > 0){
									
										
										isServiceExists=checkServiceExists($scope.servicesConfigurationList,$scope.serviceObj.serviceId)
	 									  if (isServiceExists) {
	 										
	 										  growl.warning("Service already exist.",{	
	 											title : "Warning"
	 										});
	 										return;
	 										
	 									  }
	 									  else{
												var serviceObject = $.grep($scope.consultationServiceList, function (service) {
								    	            return service.serviceMasterId == $scope.serviceObj.serviceId;
								    	        })[0];
												var newArr = {};
												newArr.serviceId  = $scope.serviceObj.serviceId;
												newArr.serviceStandardName  = serviceObject.serviceStandardName;
												newArr.postingTime  = $scope.serviceObj.postingTime;
												newArr.isHalfDay = "N";
												newArr.halfDayHrs = "";
												newArr.bufferTimeApplicable = "N";
												newArr.bufferTimeForAdmission = "";
												newArr.bufferTimeForDischarge = "";
												newArr.status = "A";
												newArr.createdBy = $scope.userId;
												newArr.id = null;
												newArr.isBedChargesService="N"
												$scope.servicesConfigurationList.push(newArr);
												$scope.serviceObj = {
														serviceId : "",
														postingTime : "",
												}
								}
									
								}
									else
										{
											var serviceObject = $.grep($scope.consultationServiceList, function (service) {
							    	            return service.serviceMasterId == $scope.serviceObj.serviceId;
							    	        })[0];
											var newArr = {};
											newArr.serviceId  = $scope.serviceObj.serviceId;
											newArr.serviceStandardName  = serviceObject.serviceStandardName;
											newArr.postingTime  = $scope.serviceObj.postingTime;
											newArr.isHalfDay = "N";
											newArr.halfDayHrs = "";
											newArr.bufferTimeApplicable = "N";
											newArr.bufferTimeForAdmission = "";
											newArr.bufferTimeForDischarge = "";
											newArr.status = "A";
											newArr.isSelected="N";
											newArr.createdBy = $scope.userId;
											newArr.updatedBy = $scope.userId;
											newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.orgId = $scope.organizationId
											newArr.unitId = $scope.unitId;
											newArr.isBedChargesService="N"
											
											newArr.id = null;
											$scope.servicesConfigurationList.push(newArr);
											$scope.serviceObj = {
													serviceId : "",
													postingTime : "",
											}
										}
							}
								
							}
							
							$scope.getDetails = function(item,idx)
							{
								$scope.listIndex = idx;
								$scope.serviceId = item.serviceId;
								$scope.postingTime = item.postingTime;
							}
							
							$scope.removeFromList = function(item,index)
							{
								if(item.id > 0)
									{
									  $scope.servicesConfigurationList.splice(index,1);
									}
								else
									{
									  $scope.servicesConfigurationList.splice(index,1);
									}
									
							}
							
							$scope.submitDetails = function()
							{
								
								$scope.servicesConfigurationList[$scope.listIndex].postingTime = $scope.postingTime;
							}
							
							$scope.saveAutoServicePosting = function()
							{
								//console.log()
								if($scope.servicesConfigurationList.length == 0)
									{
										growl.warning('Please add service',
												{
													title : 'Warning!'
												});
										  return false;
									}
								else
									{
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;

										} else {
											$scope.selectedServicePostingList = [];
											angular.forEach($scope.servicesConfigurationList, function(value , key) {
										 			if(value.isSelected == 'Y')
										 				{
										 				
										 				var Data = {
										 						id:value.id,
																serviceId: value.serviceId,
																postingTime:value.postingTime,
																serviceStandardName:value.serviceStandardName,
																isHalfDay:value.isHalfDay,
																halfDayHrs:value.halfDayHrs =="" ? 0 : value.halfDayHrs,
																bufferTimeApplicable:value.bufferTimeApplicable,
																bufferTimeForAdmission:value.bufferTimeForAdmission=="" ? 0 : value.bufferTimeForAdmission,
																bufferTimeForDischarge : value.bufferTimeForDischarge=="" ? 0 : value.bufferTimeForDischarge,
																status:value.status,
																createdBy:$scope.userId,
																updatedBy:$scope.userId,
										 						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																orgId:$scope.organizationId,
																unitId:$scope.unitId,
																isPeriodic : 'N',
																isBedChargesService : value.isBedChargesService!=null ? value.isBedChargesService : 'N'
																
										 				}
										 				$scope.selectedServicePostingList.push(Data);
										 				}
										 			if(value.bufferTimeApplicable=='Y' && (value.bufferTimeForAdmission=="" &&  value.bufferTimeForDischarge==""))	
										 			{
										 				growl.warning(
																'Please select Buffer Time',
																{
																	title : 'Warning!'
																});
														return false;
										 			}
										 			
										 			if(value.isHalfDay=='Y' && (value.halfDayHrs=="") )	
										 			{
										 				growl.warning(
																'Please select Half Day Hours',
																{
																	title : 'Warning!'
																});
														return false;
										 			}
										 		
				                     		});
											console.log("selected Service Posting List:",$scope.selectedServicePostingList);
											if($scope.selectedServicePostingList.length>0){
											var param = {
													postingServicesConfigDtosList : $scope.selectedServicePostingList
											}
											console.log("dd",param);
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/config/billing/master";
										BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success"){
												$scope.init();
												$scope.getServicePostingConfigList();
											}
												
										});
									}else{
										growl.error(
												"Select Checkbox",
												{
													title : 'Error!'
												});
									}
										
									}
									
										
									}
								
							}
							
							$scope.savePeriodicBilling = function()
							{
								
								var selectedServiceList=[];	
								var data = 
					 				{
											serviceId: $scope.periodicServiceId,
											status:'A',
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											orgId:$scope.organizationId,
											unitId:$scope.unitId,
											isPeriodic : 'Y'
					 				}
					 				
					 				selectedServiceList.push(data);
									
									var param = {
											postingServicesConfigDtosList : selectedServiceList
									}
					 				
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/config/billing/master";
									BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
										if (response.data.status == "success")
											$scope.init();
										$rootScope.stopLoader();
										$scope.getServicePostingConfigList();
									});
										
							}
							
							
							$scope.getServicePostingConfigList=function(){
								var URI = BASE_URL + ROOT_URL+ "/config/billing/list/"+$scope.organizationId+"/"+$scope.unitId;
								var data = { };
								GenericService.serviceAction("GET", URI, data).then(function(response) {
											if (response.data.status == "success") {
												$scope.servicesConfigurationList = response.data.listObject;
												$scope.periodicServicesConfigurationList = response.data.object;
											}
										});
							}
							$scope.getServicePostingConfigList();
							
							
							$scope.updateStatus = function(id, status) {
								try {

									var data={
											updatedBy:$scope.userId,
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											status: status,
											id:id
									}
									
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+ "/config/billing/updateServiceAutoPostingStatus";
									// return false;
									GenericService.serviceAction("POST", URI,data).then(
													function(response) {
														if (response.data.status == 'success') 
														{
															$rootScope.stopLoader();
															$scope.init();
															$scope.getServicePostingConfigList();
															growl.success("Status Changed Successfully",
																	{
																		title : 'Success!'
																	});
														} else if (response.data.status == 'error') {
															$rootScope.stopLoader();
															growl.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
														}
													});
								} catch (e) {
									console.log("Exception", e);
								}
							}
							
							$scope.resetHalfDayHrs=function(service,idx){
								if(service.isHalfDay=='N'){
									$scope.servicesConfigurationList[idx].halfDayHrs="";
									$scope.servicesConfigurationList[idx].bufferTimeApplicable='N';
									$scope.servicesConfigurationList[idx].bufferTimeForAdmission="";	
									$scope.servicesConfigurationList[idx].bufferTimeForDischarge="";
								}
							};

							$scope.resetBufferTimeHrs=function(service,idx){
								if(service.bufferTimeApplicable=='N'){
									$scope.servicesConfigurationList[idx].bufferTimeForAdmission="";	
									$scope.servicesConfigurationList[idx].bufferTimeForDischarge="";
								}
							};
							
						} ]);
