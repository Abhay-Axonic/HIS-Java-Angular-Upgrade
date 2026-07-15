'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:encounterQueueManagementController
 * @description #encounterQueueManagementController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nursingAssesmentDisplayController',
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


                                $scope.firstImg;
                                $scope.imageBackground = function(){
                                                var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/3"
                                                    GenericService.serviceAction("GET", URI).then(function(response) {
                                                        if(response.data.object != null){
                                                    $scope.firstImg=response.data.object.imageURL;
                                                        }
                                                    });
                                                }
                                 $scope.imageBackground();
							
							
							$scope.queueNumber = "";
							$scope.uhidNumber = "";
							$scope.patientName = "";
							$scope.specialityName = "";
							$scope.nursingDesc = "";
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								
								console.log($scope.encounterSearchObject);
								
								var paramObj = {};
								var headers={
										'organizationId': $scope.organizationId,
										'unitId':$scope.unitId,
										'Content-Type':'application/json'
								};
								//alert($scope.roleName);
								/*if($scope.nurseRole == 1)
									{*/
									$scope.encounterSearchObject.specialityIdList =$scope.departmentNursingStationMapperDtoslist;
									console.log("ff",$scope.encounterSearchObject);
									// return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETSEARCHEDLISTENCOUNTERMASTERFORNURSINGASSTOKEN;
						
							    GenericServiceParamHeaderWithouLogin
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success"){
												var checkIn = false;
												if(response.data.listObject != null){
													
													angular.forEach(response.data.listObject,function(value,index){
														if(value.callCount!=null && value.isCalled == null && value.latestCall == true){
														    $scope.allEncounterList.push(value);
														    checkIn = true;
														    $scope.queueNumber = ((value.appointmentTypeId == 5 || (value.doctorId == null || value.doctorId == '')) ? value.specialityQueueNumber : (value.appointmentTypeId == 1 && value.doctorId > 1) ? value.queueNumber : "" )
															$scope.uhidNumber = value.uhidNumber;
															$scope.patientName = value.patientName;
															$scope.specialityName = value.specialityName;
															$scope.nursingDesc = value.nursingStationDesc; 
															$scope.finalString="";
															 if( value.patientName != null &&  value.patientName != ""){
				                                        	   var String = value.patientName;
				                                        	   $scope.finalString= $scope.finalString.concat(String);
				                                        	 }
				                                        	 if(value.specialityName != null &&  value.specialityName != ""){
				                                        	      var String = value.specialityName ;
					                                        	   $scope.finalString= $scope.finalString.concat("  |  "+String);
				                                        	 }
				                                        	 if(value.nursingStationDesc != null &&  value.nursingStationDesc != ""){
				                                        	      var String = value.nursingStationDesc ;
					                                        	   $scope.finalString= $scope.finalString.concat("  |  "+String);
				                                        	 }
														}
													}); 
													
													angular.forEach(response.data.listObject,function(value,index){
														if(value.callCount!=null && value.latestCall == false && value.isCalled == null){
														    $scope.allEncounterList.push(value);
														}
													});
													
													angular.forEach(response.data.listObject,function(value,index){
														if(value.callCount==null && value.isCalled == null){
														    $scope.allEncounterList.push(value);
														}
													});
													
													
												}
												
												if(checkIn == false){
													
													$scope.queueNumber = "";
													$scope.uhidNumber = "";
													$scope.patientName = "";
													$scope.specialityName = "";
													$scope.nursingDesc = "";
													
												}
												
											}
												
												
										});
								//	}
						
							}
						
							
							$interval(function() {
								$scope.searchEncounter();
					          }, 10000);
			
						
						} ]);