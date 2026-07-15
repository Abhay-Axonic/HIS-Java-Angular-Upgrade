'use strict';

/**
 * @Author
 * @name myApp.controller:roomMasterController
 * @description #roomMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'roomMasterController',
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
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.init = function() {

								/*
								 * $scope.unitId =
								 * sessionStorage.getItem("unitId");
								 * $scope.organizationId =
								 * sessionStorage.getItem("organizationId");
								 * $scope.userId =
								 * sessionStorage.getItem("userId");
								 */
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
								$state.go('login');
								return;
								}

								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								/** Variables for pagination */
								
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
							    $scope.roomMasterObj = {
							    		bedCategoryId:"",
							    		isIsolation : "N"
							    }
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup	

								$scope.roomMasterObj={
										isModality:'N',
										isIsolation:'N'
								}
								
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								// For Get bed Category List 
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBEDCATEGORYLISTBYUNIT;
						
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.bedCategoryList = [];

											if (response.data.status == "success") {
												$scope.bedCategoryList = response.data.listObject;	
												console.log("bedCategoryList",$scope.bedCategoryList);
											}
										});
						
						// For Get ward master List 
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEWARDMASTERLIST;
				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId
				}
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									$scope.wardMasterList = [];

									if (response.data.status == "success") {
										$scope.wardMasterList = response.data.listObject;	
										console.log("wardMasterList",$scope.wardMasterList);
									}
								});


							}
							
							$scope.init();
							//get billing bed category by bed category id
							$scope.getBedCategory=function(id){
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBILLINGBEDCATEGORYBYBEDCATEGORYID;
						var data = {
								organizationId:$scope.organizationId,
								bedCategoryId:id
						}
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.billingBedCategoryList = [];

											if (response.data.status == "success") {
												$scope.billingBedCategoryList = response.data.listObject;
												$scope.roomMasterObj.billingBedDesc=response.data.listObject[0].billingBedDesc;
												$scope.roomMasterObj.billingBedCategoryId=response.data.listObject[0].billingBedCategoryId;
												console.log("billingBedCategoryList",$scope.billingBedCategoryList);
											}
										});
							}
							 $scope.hideBed="N";
							$scope.checkIsLaborward= function(id) {
							    angular.forEach($scope.wardMasterList,function(value,index)
								{
							    	if(id==value.wardId){
							    		$scope.isLaborWardcheck=value.islaborWard;
							    		$scope.isHemodialysWardcheck=value.ishemodialysisward;
							    		   
								    		   if($scope.isLaborWardcheck=="Y" || $scope.isHemodialysWardcheck=="Y"){
								    			  
	                                               $scope.hideBed = "Y";
								    		   }
								    		   else{
								    			   $scope.hideBed="N";
								    		   }
							    		   
							    	}
					            			  
					            			
					            		});
							 
								
							}
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initRoomMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initRoomMasterList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								$scope.hideBed="N";
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETROOMMASTERLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ GETROOMMASTERCOUNT;

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
													$scope.roomMasterList = [];

													if (response.data.status == "success") {
														$scope.roomMasterList = response.data.listObject;
														console
																.log(
																		"roomMasterList",
																		$scope.roomMasterList);
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
														$scope.hideBed="N";
													}
												});
							}

							$scope.getRoomMasterList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETROOMMASTERLIST;
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
													$scope.roomMasterList = [];

													if (response.data.status == "success") {
														$scope.roomMasterList = response.data.listObject;														
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
									$scope.getRoomMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initRoomMasterList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveRoomMaster();
							}

							// Function For  save room master
							$scope.saveRoomMaster = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								// $scope.$broadcast('show-errors-check-validity');
								var data = {
									//bedCategoryId       :$scope.roomMasterObj.bedCategoryId,
									//billingBedCategoryId:$scope.roomMasterObj.billingBedCategoryId,
									erCategoryId        :$scope.roomMasterObj.erCategoryId==false ? null : $scope.roomMasterObj.erCategoryId,
									isIsolation         :$scope.roomMasterObj.isIsolation,
									roomCode            :$scope.roomMasterObj.roomCode,
									roomDesc            :$scope.roomMasterObj.roomDesc,
									wardId              :$scope.roomMasterObj.wardId,	
									isModality          :$scope.roomMasterObj.isModality!=undefined ? $scope.roomMasterObj.isModality:'N' ,
									status 				:'A',
									createdBy			:$scope.userId,
									updatedBy 			:$scope.userId,
									unitId				:$scope.unitId,
									organizationId 		:$scope.organizationId,
									createdDate			:$scope.currentDate,
									updatedDate			:$scope.currentDate
								}
								if($scope.roomMasterObj.billingBedCategoryId!=null){
									data.billingBedCategoryId=$scope.roomMasterObj.billingBedCategoryId;
								}
								if($scope.roomMasterObj.bedCategoryId!=null && $scope.roomMasterObj.bedCategoryId!="" ){
									data.bedCategoryId=$scope.roomMasterObj.bedCategoryId;
								}
								 if ($scope.roomForm.$valid) {

								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									console.log("save", data); 
                                  //return false;
									var URI = BASE_URL + ROOT_URL
											+ SAVEROOMMASTER;
									GenericService
											.serviceAction("POST", URI, data)
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

														$scope.initRoomMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}
								 }

							}

							// Function For get single room  entry
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

								}
								var URI = BASE_URL + ROOT_URL
										+ GETROOMMASTERBYID + "/"
										+ id + "/" + $scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log("By id",response.data.listObject);
														if(response.data.listObject[0].bedCategoryId!=null)
													    $scope.roomMasterObj.bedCategoryId = response.data.listObject[0].bedCategoryId.toString();
														$scope.roomMasterObj.billingBedDesc = response.data.listObject[0].billingBedDesc;
														$scope.roomMasterObj.billingBedCategoryId = response.data.listObject[0].billingBedCategoryId;
														$scope.roomMasterObj.isIsolation = response.data.listObject[0].isIsolation;
														$scope.roomMasterObj.roomCode = response.data.listObject[0].roomCode;
														$scope.roomMasterObj.roomDesc = response.data.listObject[0].roomDesc;
														$scope.roomMasterObj.erCategoryId=response.data.listObject[0].erCategoryId;
														$scope.roomMasterObj.roomId = response.data.listObject[0].roomId;
														$scope.roomMasterObj.wardId = response.data.listObject[0].wardId.toString();
														$scope.islaborWard=response.data.listObject[0].islaborWard;
														$scope.roomMasterObj.isModality = response.data.listObject[0].isModality;
														$scope.ishemodialysisward =response.data.listObject[0].ishemodialysisward;
														if($scope.islaborWard=="Y" ||$scope.ishemodialysisward=="Y"){
											    			  
				                                               $scope.hideBed = "Y";
											    		   }
											    		   else{
											    			   $scope.hideBed="N";
											    		   }
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
									roomId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ UPDATEROOMMASTERSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
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
							$scope.roomForm;
							// Function For UPDATEROOMMASTER
							$scope.updateRoomMaster = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
							//	$scope.$broadcast('show-errors-check-validity');
								var data = {
										//bedCategoryId       :$scope.roomMasterObj.bedCategoryId,
									//billingBedCategoryId:$scope.roomMasterObj.billingBedCategoryId,
										isIsolation         :$scope.roomMasterObj.isIsolation,
										roomCode            :$scope.roomMasterObj.roomCode,
										roomDesc            :$scope.roomMasterObj.roomDesc,
										roomId              :$scope.roomMasterObj.roomId,
										wardId              :$scope.roomMasterObj.wardId,
										erCategoryId        :$scope.roomMasterObj.erCategoryId==false ? null : $scope.roomMasterObj.erCategoryId,
									    updatedBy           :$scope.userId,
									    organizationId      :$scope.organizationId,
									    unitId				:$scope.unitId,
									    updatedDate         :$scope.currentDate,
									    isModality          :$scope.roomMasterObj.isModality!=undefined ? $scope.roomMasterObj.isModality:'N' ,
								}	
								if($scope.roomMasterObj.billingBedCategoryId!=null){
									data.billingBedCategoryId=$scope.roomMasterObj.billingBedCategoryId;
								}
								if($scope.roomMasterObj.bedCategoryId!=null && $scope.roomMasterObj.bedCategoryId!="" ){
									data.bedCategoryId=$scope.roomMasterObj.bedCategoryId;
								}
								if ($scope.roomForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEROOMMASTER;
									GenericService
											.serviceAction("POST", URI, data)
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
														$scope
																.initRoomMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}

							}
						} ]);
