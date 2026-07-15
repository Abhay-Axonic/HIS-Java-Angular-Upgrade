'use strict';

/**
 * @Author By  Nitin More
 * @name myApp.controller:encounterQueueManagementController
 * @description #encounterQueueManagementController Controller of the myApp
 */
angular.module('myApp')
		.controller(
				'triageDisplayController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceForWithoutLogin',
						'GenericServiceParamHeaderWithouLogin',
						'growl',
						'orgId',
						'unitId',
						'nursingStationId',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceForWithoutLogin,GenericServiceParamHeaderWithouLogin,growl,orgId,unitId,nursingStationId) {


							$scope.unitId = unitId;
							$scope.organizationId = orgId;
							$scope.nursingStationId = nursingStationId
							$scope.nursingDescAndPatientName = "";
							$scope.init = function() {
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();

								$rootScope.loginpage = false;
								
								var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.nursingStationId +"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericServiceForWithoutLogin.serviceAction("GET",URI,{}).then(function(response){
									if (response.data.status == "success") {
										$scope.departmentNursingStationMapperDtoslist = response.data.listObject;					  
									}
								});
						
								$scope.showUpdateBtn = function() {
									var data = {
									}
									var URI = BASE_URL + ROOT_URL+ "/api/global/getNursingStationMasterById/"+ $scope.nursingStationId + "/" + $scope.organizationId+"/"+$scope.unitId;
									GenericServiceForWithoutLogin.serviceAction("GET", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.nursingStationDesc = response.data.listObject[0].nursingStationDesc;
															$scope.nursingDescAndPatientName = $scope.nursingStationDesc;
														} else {
															alert("Error!!");
														}
													});							
								};
								
								$scope.showUpdateBtn();
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								var headerObj = {
										organization_id:$scope.organizationId,
										unit_id : $scope.unitId
								}
								var paramObj = {};
			
								     $scope.encounterSearchObject = {
								    		 'doctorId':0,
								    		 'specialityId':0,
								    		 'patientId':0,
								    		 'specialityId':0,
								    		 'statusId':0,
								    		 'clinicId':0,
								    		 'nursingStationId':0
								     }
								     
								     $scope.unitBillingAppConfig = {};
										var URI = BASE_URL + ROOT_URL
										+ "/api/unit/applicationConfiguration/billing/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
										GenericServiceForWithoutLogin.serviceAction(METHOD_GET, URI,"",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													if (response.data.status == "success")
														{
															$scope.unitBillingAppConfig = response.data.object;
														}
														
												});
										
								     
								     
							}
                                 $scope.firstImg;
                                 $scope.imageBackground = function(){
                                                 var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/3"
                                                     GenericServiceForWithoutLogin.serviceAction("GET", URI).then(function(response) {
                                                    	 if(response.data.object != null){
                                                     $scope.firstImg=response.data.object.imageURL;
                                                    	 }
                                                     });
                                                 }
                                  $scope.imageBackground();


							$scope.getCoEmpVerificationReq = function(){
								try{
									var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									GenericServiceForWithoutLogin.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.unitConfigurationObject = response.data.listObject[0];
											$scope.tokenNumberReq = $scope.unitConfigurationObject.tokenNumber;
											$scope.init();
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							
							$scope.getCoEmpVerificationReq();
							
							
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								
								
								var paramObj = {};
								var headers={
										'organizationId': $scope.organizationId,
										'unitId':$scope.unitId,
										'Content-Type':'application/json'
								};
									$scope.encounterSearchObject.specialityIdList =$scope.departmentNursingStationMapperDtoslist;
									
							    var URI = BASE_URL + ROOT_URL + GETSEARCHEDLISTENCOUNTERMASTERFORNURSINGASSTOKEN;
						
							    GenericServiceParamHeaderWithouLogin
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											if (response.data.status == "success"){
												var checkIn = false;
												if(response.data.listObject != null){
													var checkLoop = false;
													angular.forEach(response.data.listObject,function(value,index){
														if(value.callCount!=null && value.isCalled == null && value.latestCall == true){
															checkLoop = true;
															$scope.nursingDescAndPatientName = value.prefixCode+" "+ value.patientName;
														}
													}); 
													if(checkLoop == false){
														$scope.nursingDescAndPatientName = $scope.nursingStationDesc;
													}
													
												}
												
											}
												
												
										});
								//	}
						
							}
						
							
							$interval(function() {
								$scope.searchEncounter();
					          }, 10000);
			
						
						} ]);