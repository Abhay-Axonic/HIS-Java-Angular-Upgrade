'use strict';

/**
 * @Author
 * @name myApp.controller:wardMasterController
 * @description #wardMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'wardMasterController',
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
							    $scope.wardMasterObj = {
							    		isIcu:"N",
							    		isEr:"N",
							    		isDayCare:"N",
							    		islaborWard:"N",
							    		isIpWard:'N',
							    		ishemodialysisward:'N'
							    }
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.selectedGenderList = [];
								
								// For Get floor List 
								   var URI =  BASE_URL + ROOT_URL+GETACTIVEFLOORLIST;
					  	var data = {
					  			unitId:$scope.unitId,
					  			organizationId:$scope.organizationId
					  	}
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.floorList = [];

											if (response.data.status == "success") {
												$scope.floorList = response.data.listObject;
												console.log("floorList",$scope.floorList);
											}
										});
					  	
						var data = {
								organizationId : $scope.organizationId,
								unitId:$scope.unitId
						};
						
						
						
				
				//var URI = BASE_URL + ROOT_URL+ GETACTIVEUNITICUTYPELIST;
						var URI = BASE_URL + ROOT_URL+'/adt/getActiveICUTypeList';	
		GenericService
				.serviceAction("POST", URI, data)
				.then(
						function(response) {
							$scope.icuTypeList = [];

							if (response.data.status == "success") {
								$scope.icuTypeList = response.data.listObject;

							}
						});
					  	
					 // For Get Gender List 
						   var URI =  BASE_URL + ROOT_URL+GETACTIVEGENDERLIST;
			  	var data = {			  			
			  	}
				GenericService
						.serviceAction("GET", URI, data)
						.then(
								function(response) {
									$scope.genderList = [];
									$scope.genderListOptions = [];
									if (response.data.status == "success") {
										//$scope.genderList = response.data.listObject;
										//console.log("genderList",$scope.genderList);
										//
										angular.forEach(response.data.listObject,
												function(value,index) {
													var data = {};
													data.id = value.id;
													data.label = value.desc;
													$scope.genderListOptions.push(data);
												});
									}
								});	
			  	
			  	
			  	
			  	
			/* // For Get store List 
				   var URI =  BASE_URL + ROOT_URL+"/api/global/user/stores";
	  	var data = {	  			
	  	}
		GenericService
				.serviceAction("GET", URI, data)
				.then(
						function(response) {
							$scope.storeList = [];													
							$scope.storeList = response.data;
							console.log("store list",$scope.storeList);
														
						});*/

							}
							
							$scope.init();				

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initWardMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initWardMasterList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETWARDMASTERLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ GETWARDMASTERCOUNT;

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
													$scope.wardMasterList = [];

													if (response.data.status == "success") {
														$scope.wardMasterList = response.data.listObject;
														console
																.log(
																		"wardMasterList",
																		$scope.wardMasterList);
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
														$scope
																.setPage(1,
																		false);

													}
												});
							}

							$scope.getWardMasterList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETWARDMASTERLIST;
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
														$scope.wardMasterList = response.data.listObject;														
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
									$scope.getWardMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initWardMasterList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveWardMaster();
							}

							// Function For  save room master
							$scope.saveWardMaster = function() {
								
								//console.log("$scope.selectedGenderList",$scope.selectedGenderList);
								//return false;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								 $scope.$broadcast('show-errors-check-validity');
								 if($scope.wardMasterObj.floorId == undefined || $scope.wardMasterObj.floorId == "Select Floor"){
									 $rootScope.stopLoader();
									 growl.error("Please Select Floor",
												{
													title : 'Error!'
												});
									 return false;
								 }
								var data = {
									floorId     :$scope.wardMasterObj.floorId,
									wardCode	:$scope.wardMasterObj.wardCode,
									wardName    :$scope.wardMasterObj.wardName,
									isDayCare   :$scope.wardMasterObj.isDayCare,
									isEr        :$scope.wardMasterObj.isEr,
									isIcu       :$scope.wardMasterObj.isIcu,
									genderIdList :$scope.selectedGenderList,	
//									storeId    :$scope.wardMasterObj.id	,
									islaborWard :$scope.wardMasterObj.islaborWard,
									ishemodialysisward :$scope.wardMasterObj.ishemodialysisward,
									status 		:'A',
									createdBy	:$scope.userId,
									updatedBy 	:$scope.userId,
									unitId		:$scope.unitId,
									organizationId :$scope.organizationId,
									createdDate	:$scope.currentDate,
									updatedDate	:$scope.currentDate,
									isIpWard    :$scope.wardMasterObj.isIpWard,
								}
								if($scope.wardMasterObj.isIcu == 'Y')
									{
										data.icuTypeId  = $scope.wardMasterObj.icuTypeId;
									}
								else
									{
									  data.icuTypeId  = null;
									}
								 if ($scope.wardForm.$valid) {
									 if($scope.selectedGenderList.length == 0)
										 {
										 growl
											.error("Please Select Gender",
													{
														title : 'Error!'
													});
										 }
									 else{
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									$rootScope.startLoader();
									console.log("save", data); 
//									return false;
									var URI = BASE_URL + ROOT_URL
											+ SAVEWARDMASTERMASTER;
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
															$rootScope.stopLoader();
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}

														$scope
																.initWardMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}
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
										+ GETWARDMASTERBYID + "/"
										+ id + "/" + $scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log("By id",response.data.listObject);
													   $scope.wardMasterObj.floorId = response.data.listObject[0].floorId.toString();
														$scope.wardMasterObj.wardCode = response.data.listObject[0].wardCode;
														$scope.wardMasterObj.wardName = response.data.listObject[0].wardName;
														$scope.wardMasterObj.isDayCare = response.data.listObject[0].isDayCare;
														$scope.wardMasterObj.isEr = response.data.listObject[0].isEr;
														$scope.wardMasterObj.isIcu = response.data.listObject[0].isIcu;
														$scope.wardMasterObj.wardId = response.data.listObject[0].wardId;
														$scope.wardMasterObj.ishemodialysisward= response.data.listObject[0].ishemodialysisward;
														$scope.wardMasterObj.icuTypeId = response.data.listObject[0].icuTypeId;
														$scope.wardMasterObj.islaborWard= response.data.listObject[0].islaborWard;
														$scope.selectedGenderList = response.data.listObject[0].genderIdList;
														$scope.wardMasterObj.isIpWard = response.data.listObject[0].isIpWard;
//														$scope.wardMasterObj.id=response.data.listObject[0].storeId.toString();	
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
									wardId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ UPDATEWARDMASTERSTATUS;
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

							// Function For UPDATEROOMMASTER
							$scope.updateWardMaster = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								 if($scope.wardMasterObj.floorId == undefined || $scope.wardMasterObj.floorId == "Select Floor"){
									 $rootScope.stopLoader();
									 growl.error("Please Select Floor",
												{
													title : 'Error!'
												});
									 return false;
								 }
								var data = {
										floorId     :$scope.wardMasterObj.floorId,
										wardCode	:$scope.wardMasterObj.wardCode,
										wardName    :$scope.wardMasterObj.wardName,
										isDayCare   :$scope.wardMasterObj.isDayCare,
										isEr        :$scope.wardMasterObj.isEr,
										isIcu       :$scope.wardMasterObj.isIcu,
										wardId      :$scope.wardMasterObj.wardId,
										genderIdList :$scope.selectedGenderList,
										islaborWard :$scope.wardMasterObj.islaborWard,
										ishemodialysisward :$scope.wardMasterObj.ishemodialysisward,
//										storeId     :$scope.wardMasterObj.id,	
									    updatedBy   :$scope.userId,
									    updatedDate :$scope.currentDate,
									    unitId		:$scope.unitId,
										organizationId :$scope.organizationId,
										isIpWard    :$scope.wardMasterObj.isIpWard,
								}	
								if($scope.wardMasterObj.isIcu == 'Y')
								{
									data.icuTypeId  = $scope.wardMasterObj.icuTypeId;
								}
							else
								{
								  data.icuTypeId  = null;
								}
							   console.log(data)
								if ($scope.wardForm.$valid) {
									 if($scope.selectedGenderList.length == 0)
									 {
									 growl
										.error("Please Select Gender",
												{
													title : 'Error!'
												});
									 }
								 else{
										$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ UPDATEWARDMASTER;
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
															$rootScope.stopLoader();
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														$scope
																.initWardMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
									}
								}

							}
							$scope.genderDetailsPopup = function(item)
							{
								$scope.genderPopupList = item;
							}
						} ]);
