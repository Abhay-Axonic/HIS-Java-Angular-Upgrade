'use strict';

/**
 * @Author
 * @name myApp.controller:wardMasterController
 * @description #wardMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'roomModalityController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'growl',
						'$state',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,BillingGenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */

							$scope.init = function() {

								var cookieObject,cookieObjectAppConfig;
								[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
								$state.go('login');
								return;
								}
								console.log("cookieObject",cookieObject);
								console.log("cookieObjectAppConfig",cookieObjectAppConfig); 
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.radiologySpecialityId=cookieObjectAppConfig.radiologySpecialityId;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
							    $scope.roomModalityObj = {
							    		roomModalityMasterId:"",
							    		roomModalityId:"",
							    		specialityId:$scope.radiologySpecialityId,
							    		subSpecialityId:"",
							    		modalityId:"",
							    		unitId:$scope.unitId,
							    		organizationId:$scope.organizationId,
							    		updatedBy:$scope.userId,
							    		createdBy:$scope.userId,
							    		status:'A'
							    }
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
								// For Get room modality List 
								   var URI =  BASE_URL + ROOT_URL+"/api/adt/getActiveModalityRoom";
							  	var data = {
							  			unitId:$scope.unitId,
							  			organizationId:$scope.organizationId
							  	      }
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											$scope.floorList = [];

											if (response.data.status == "success") {
												$scope.modalityRoomList = response.data.listObject;
												console.log("room modality List",$scope.modalityRoomList);
											}
										});
					  	
						var data = {
								organizationId : $scope.organizationId,
								unitId:$scope.unitId
						};
						
				
						//Function For Get radiology sub speciality list
						
							var data = {
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								specialityId : $scope.radiologySpecialityId
							}
							console.log(data);
							
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYID;
							BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
												console.log(response);
												 $rootScope.startLoader();
												$scope.subSpecialityList = [];

												if (response.data.status == "success") {
													$scope.subSpecialityList = response.data.listObject;													
													
													$rootScope.stopLoader();
												}
												
											});
						

							}
							
							$scope.init();	
							
							//Function For Get Modality List by Multiple Modality Type Id
							$scope.getModalityBySubSpeciality = function(subSpecialityId) {
								//alert('assa');
//								var array = id.split('_');
								
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : $scope.radiologySpecialityId,
									subSpecialityId :subSpecialityId
								}
								$scope.roomModalityObj.subSpecialityId=subSpecialityId;
								console.log(data);
								$rootScope.startLoader();
								//return false;
								var URI = BASE_URL + ROOT_URL+ GETMODALITYBYSUBSPECIALITYID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													$scope.modalityMaster = [];


													if (response.data.status == "success") {
														$scope.modalityMasterList = response.data.listObject;													
														
														$rootScope.stopLoader();
													}

												});
							}

							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveRoomModality();
							}
							// Function For  save room master
							$scope.saveRoomModality = function() {
								 $scope.$broadcast('show-errors-check-validity');
								
								 if ($scope.modalityForm.$valid) {
									
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else
								{
									$rootScope.startLoader();
									console.log("save", $scope.roomModalityObj); 
									
//									return false;
									var URI = BASE_URL + ROOT_URL+ "/api/global/saveRoomModalityMasterMaster";
									GenericService.serviceAction("POST", URI, $scope.roomModalityObj)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.init();
															growl.success(response.data.message,
																			{
																				title : 'Success!'
																			});
															$rootScope.stopLoader();
														} else {
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}

														$scope.initRoomModalityList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);

													});
								
							}
								 }
							}
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initRoomModalityList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initRoomModalityList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL+ "/api/global/getRoomModalityList";
								var URI1 = BASE_URL + ROOT_URL+ "/api/global/updateRoomModalityMasterCount";

								var data = {
									organizationId : orgId,
									unitId:unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
									unitId:unitId,
									organizationId : $scope.organizationId
								}

								// For Get roomMasterList Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.roomModalityMasterList = [];

													if (response.data.status == "success") {
														$scope.roomModalityMasterList = response.data.listObject;
														console.log("roomModalityMasterList",$scope.roomModalityMasterList);
													}
												});

								// For Count roomMasterList 
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

													}
												});
							}

							$scope.getRoomModalityList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL+ "/api/global/getRoomModalityList";
								var data = {
									organizationId : $scope.organizationId,
									unitId:$scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get roomMasterList List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.wardMasterList = [];

													if (response.data.status == "success") {
														$scope.roomModalityMasterList = response.data.listObject;														
													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage,$scope.unitId);
								if (flag) {
									$scope.getRoomModalityList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initRoomModalityList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End

							// function for popup ok button call
							/*$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveWardMaster();
							}*/

							

							// Function For get single room  entry
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

								}
								var URI = BASE_URL + ROOT_URL+ "/api/global/getRoomModalityById/" + id + "/" + $scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, data).then(
												function(response) {
													if (response.data.status == "success") {
														 $scope.roomModalityObj=response.data.listObject[0]; 
														 $scope.getModalityBySubSpeciality($scope.roomModalityObj.subSpecialityId);
														console.log("By id",$scope.roomModalityObj);
													 	
													} else {
														alert("Error!!");
													}
												});							

							};

							// Function For update room status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									roomModalityMasterId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL+ "/api/global/updateRoomModalityMasterStatus";
								GenericService.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.init();
														growl
																.success(
																		response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		});
													}
												});
							}

							// Function For UPDATEROOMMASTER
							$scope.updateRoomModality = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								$scope.roomModalityObj.unitId=$scope.unitId;
								$scope.roomModalityObj.organizationId=$scope.organizationId;
								
								if ($scope.modalityForm.$valid) {									
								
										$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+ "/api/global/updateRoomModalityMaster";
									GenericService.serviceAction("POST", URI, $scope.roomModalityObj)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$scope.init();
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$rootScope.stopLoader();
														} else {
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														$scope.initRoomModalityList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
									
								}

							}
							
						} ]);
