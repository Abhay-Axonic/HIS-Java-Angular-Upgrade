'use strict';

/**
 * @Author
 * @name myApp.controller:bedMasterController
 * @description #bedMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'bedMasterController',
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
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.searchMovingBedString="";
								$scope.commonListCount;

								$rootScope.loginpage = true;
							    $scope.bedMasterObj = {
							    		billingBedDesc:"",
							    		billingBedCategoryId:"",
							    		earMarking:"N",
							    		isVirtual:"N"
							    }
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup	
								
								// For Get Ward  List 
								   var URI =  BASE_URL + ROOT_URL+GETACTIVEWARDMASTERLIST;
							  	var data = {
							  			unitId:$scope.unitId,
							  			organizationId:$scope.organizationId
							  	}
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.wardList = [];
		
													if (response.data.status == "success") {
														$scope.wardList = response.data.listObject;
														console.log("wardList",$scope.wardList);
													}
												});
							  	
									 // For Get bed Category List 
										var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDCATEGORYLIST;
											var data = { }
								GenericService.serviceAction("GET", URI, data).then(
												function(response) {
													$scope.bedCategoryList = [];
													if (response.data.status == "success") {
														$scope.bedCategoryList = response.data.listObject;	
														console.log("bedCategoryList",$scope.bedCategoryList);
													}
												});
											
											//set the first level tab for default selection
											$scope.firstTabLevel=1;
											
											
							}
							//calling init() method for initialization
							$scope.init();
							
							 $scope.hideBed="N";
							
//							===================================normal bed start=====================
							$scope.normalBed=function(){
								// normal bed For Paginations Start
								$rootScope.startLoader();
								$scope.setNoOfRecords = function() {
									$scope.initBedMasterList(
											$scope.organizationId, $scope.offset,
											$scope.noOfRecordsPerPage,$scope.unitId);
								};
								$scope.initBedMasterList = function(orgId,
										offset, noOfRecordsPerPage,unitId) {
									var data = "";
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 10;
									var URI = BASE_URL + ROOT_URL
											+ BEDLIST;
									var URI1 = BASE_URL + ROOT_URL
											+ BEDLISTCOUNT;

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
														$scope.bedMasterList = [];

														if (response.data.status == "success") {
															$scope.bedMasterList = response.data.listObject;
															console.log("bedMasterList qq",$scope.bedMasterList);
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

								$scope.getBedMasterList = function(orgId,
										offset, noOfRecordsPerPage,unitId) {
									console.log("offset", offset);
									var data = "";
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 10;
									var URI = BASE_URL + ROOT_URL
											+ BEDLIST;
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
														$scope.bedMasterList = [];

														if (response.data.status == "success") {
															$scope.bedMasterList = response.data.listObject;														
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
										$scope.getBedMasterList(
												$scope.organizationId,
												$scope.pager.startIndex,
												$scope.pager.pageSize,$scope.unitId);
									}
								}

								$scope.initBedMasterList($scope.organizationId,
										$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								$rootScope.stopLoader();
								// For Paginations End
						}
								
							$scope.normalBed();
						
							//to get the room details by ward id
							$scope.showDialysisSelect='N';
							$scope.getWardId=function(wardId){							
							console.log(wardId)
							$scope.bedMasterObj.roomId="";
							angular.forEach($scope.wardList,function(value,index)
									{
					  		 console.log("$scope.wardMasterList")
					  		        if(value.wardId == wardId){
					  		        	console.log(value.islaborWard);
								    		$scope.isLaborWardcheck=value.islaborWard;
								    		$scope.isHemodialysWardcheck=value.ishemodialysisward;
								    		   
								    		   if($scope.isLaborWardcheck=="Y"){
		                                               $scope.hideBed = "Y";
		                                               $scope.showDialysisSelect='N';
									    		   }else if($scope.isHemodialysWardcheck=="Y"){
									    			   $scope.hideBed = "Y";
									    			   $scope.showDialysisSelect='Y';
									    		   }
									    		   else{
									    			   $scope.hideBed="N";
									    			   $scope.showDialysisSelect='N';
									    		   }
								    		   
								    	}
						            			  
						            			
						            });
							
								   var URI =  BASE_URL + ROOT_URL+GETROOMBYWARDID;
					  	var data = {
					  			unitId:$scope.unitId,
					  			organizationId:$scope.organizationId,
					  			wardId:wardId
					  	}
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.roomList = [];

											if (response.data.status == "success") {
												$scope.roomList = response.data.listObject;
												console.log("roomList",$scope.roomList);
											}
										});
					  	angular.forEach($scope.wardList,function(value,index){
					  				if(value.wardId == wardId){
									console.log("floor id",value.floorId);
									$scope.bedMasterObj.floorId=value.floorId;
										}
					       	});
					  	 
								 
							}

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
												$scope.bedMasterObj.billingBedDesc=response.data.listObject[0].billingBedDesc;
												$scope.bedMasterObj.billingBedCategoryId=response.data.listObject[0].billingBedCategoryId;
												console.log("billingBedCategoryList",$scope.billingBedCategoryList);
											}
										});
							}
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								if($scope.firstTabLevel==1)
									{
									$scope.saveBed();
									}
								else if($scope.firstTabLevel==2)
									{ 
									$scope.saveMovingBed();
									}
									
							}

							// Function For  save room master
							$scope.saveBed= function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								 $scope.$broadcast('show-errors-check-validity');
								var data = {
									floorId     :$scope.bedMasterObj.floorId,
									wardId	:$scope.bedMasterObj.wardId,
									roomId    :$scope.bedMasterObj.roomId,
									//bedCategoryId :$scope.bedMasterObj.bedCategoryId,
									//billingBedCategoryId :$scope.bedMasterObj.billingBedCategoryId,
									bedCode       :$scope.bedMasterObj.bedCode,
									bedDesc    :$scope.bedMasterObj.bedDesc,
									isEarMarking    :$scope.bedMasterObj.earMarking,	
									isVirtual    :$scope.bedMasterObj.isVirtual,
									earmarkingDesc    :$scope.bedMasterObj.earMarkingDesc,								
									status 		:'A',
									createdBy	:$scope.userId,
									updatedBy 	:$scope.userId,
									unitId		:$scope.unitId,
									organizationId :$scope.organizationId,
									createdDate	:$scope.currentDate,
									updatedDate	:$scope.currentDate,
									isMovingBed	:'N',
									bedStatusId:3,
									bedTypeId:1
								}
								if($scope.bedMasterObj.roomId==undefined || $scope.bedMasterObj.roomId==null || $scope.bedMasterObj.roomId=="" ){
									growl.warning('Please Select Room..!!');
					                  return false; 
								 }
								if($scope.bedMasterObj.bedCategoryId!=null){
									data.bedCategoryId=$scope.bedMasterObj.bedCategoryId;
								}
								if($scope.bedMasterObj.billingBedCategoryId!=null){
									data.billingBedCategoryId=$scope.bedMasterObj.billingBedCategoryId;
								}
								if($scope.bedMasterObj.itemId!=null){
									data.itemId=$scope.bedMasterObj.itemId;
								}
								 if ($scope.bedForm.$valid) {

								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									console.log("save", data); 
//									return false;
									var URI = BASE_URL + ROOT_URL
											+ ADDBED;
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
																.initBedMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}
								 }

							}

							$scope.dialysischair = function() {
								console.log("ihanhd")
								var data={}
								var URI = BASE_URL + ROOT_URL
								+ "/api/opd/getAsset";								
								GenericService
								.serviceAction("GET", URI, data)
						.then(
								function(response) {
									console.log(response);
									$scope.assetList = [];
									if (response.data.status == "success")
										$scope.assetList = response.data.listObject;
									console.log($scope.assetList)
									
								});
							}
							$scope.dialysischair();
							// Function For get single room  entry
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

								}
								var URI = BASE_URL + ROOT_URL
										+ GETBEDBYID + "/"+ id ;
										
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log("By id",response.data.listObject);
													   $scope.bedMasterObj.wardId = response.data.listObject[0].wardId.toString();
													   $scope.getWardId(response.data.listObject[0].wardId);
													   $scope.bedMasterObj.floorId = response.data.listObject[0].floorId;
														$scope.bedMasterObj.roomId = response.data.listObject[0].roomId.toString();	
														
														if(response.data.listObject[0].bedCategoryId!=null){
														$scope.bedMasterObj.bedCategoryId = response.data.listObject[0].bedCategoryId.toString();
														$scope.getBedCategory(response.data.listObject[0].bedCategoryId);
														$scope.bedMasterObj.billingBedDesc=$scope.bedMasterObj.billingBedDesc;
														}
														$scope.bedMasterObj.bedCode = response.data.listObject[0].bedCode;
														$scope.bedMasterObj.bedDesc = response.data.listObject[0].bedDesc;
														$scope.bedMasterObj.isVirtual = response.data.listObject[0].isVirtual;
														$scope.bedMasterObj.earMarking = response.data.listObject[0].isEarMarking;
														$scope.bedMasterObj.earMarkingDesc = response.data.listObject[0].earmarkingDesc;
														$scope.bedMasterObj.bedId = response.data.listObject[0].bedId;
														
													} else {
														
														if(response.data.code == '409'){
															
															growl.warning("Bed is used, you can't change status.",
																	{
																		title : 'Warning!'
																	});
															$scope.saveBtnFlag = true;
															$scope.updateBtnFlag = false;
														}else{
															growl
															.error(
																	"Error!!",
																	{
																		title : 'Error!'
																	});
															$scope.saveBtnFlag = true;
															$scope.updateBtnFlag = false;
														}
														
													}
												});							

							};

							// Function For update room status
							$scope.updateStatus = function(id, type,idx) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									bedId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ UPDATESTATUS;
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
														
														if(response.data.code == '409'){
															
															var typeTmp = (type == "A") ? "I" : "A";
															growl.warning("Bed is used, you can't change status.",
																	{
																		title : 'Warning!'
																	});
															$scope.bedMasterList[idx].status = typeTmp;
														}else{
															growl
															.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
														}
														
													}
												});
							}
							
							$scope.getBillingAndBedCategoryId = function(id)
							{
								var bedCategoryId = $.grep($scope.roomList, function (rm) {
						            return rm.roomId == id;
						        })[0].bedCategoryId;
								
								var billingBedCategoryObj = $.grep($scope.roomList, function (rm) {
									 return rm.roomId == id;
						        })[0];
								
								if( bedCategoryId!=null)
								$scope.bedMasterObj.bedCategoryId = bedCategoryId.toString();
								$scope.bedMasterObj.billingBedCategoryId = billingBedCategoryObj.billingBedCategoryId;
								$scope.bedMasterObj.billingBedDesc = billingBedCategoryObj.billingBedDesc;
							}
							
							// Function For updateBed
							$scope.updateBed = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
										floorId     :$scope.bedMasterObj.floorId,
										wardId	:$scope.bedMasterObj.wardId,
										roomId    :$scope.bedMasterObj.roomId,
										bedCategoryId :$scope.bedMasterObj.bedCategoryId,
										billingBedCategoryId :$scope.bedMasterObj.billingBedCategoryId,
										bedCode       :$scope.bedMasterObj.bedCode,
										bedDesc    :$scope.bedMasterObj.bedDesc,
										isEarMarking    :$scope.bedMasterObj.earMarking,	
										isVirtual    :$scope.bedMasterObj.isVirtual,
										earmarkingDesc    :$scope.bedMasterObj.earMarkingDesc,								
										updatedBy 	:$scope.userId,
										updatedDate	:$scope.currentDate,
										unitId		:$scope.unitId,
										organizationId :$scope.organizationId,
										bedId :$scope.bedMasterObj.bedId,
										
										isMovingBed	:'N'
								}	
								if($scope.bedMasterObj.roomId==undefined || $scope.bedMasterObj.roomId==null || $scope.bedMasterObj.roomId=="" ){
									growl.warning('Please Select Room..!!');
					                  return false; 
								 }
							if($scope.bedMasterObj.itemId!=null){
								data.itemId=$scope.bedMasterObj.itemId;
							}
								if ($scope.bedForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEBED;
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
																.initBedMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}

							}
//						}
							
						
//							==========================
							$scope.movingBed=function(){
								console.log("second tab");
								// function for popup ok button call
								
								$rootScope.startLoader();
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initMovingBedMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initMovingBedMasterList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ "/api/adt/bedMovingBedList";
								var URI1 = BASE_URL + ROOT_URL
										+ "/api/adt/movingBedListCount";

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
													$scope.movingBedMasterList = [];

													if (response.data.status == "success") {
														$scope.movingBedMasterList = response.data.listObject;
														console
																.log(
																		"movingBedMasterList qq",
																		$scope.movingBedMasterList);
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
														$scope.movingCommonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);

													}
												});
							}

							$scope.getMovingBedMasterList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ "/api/adt/bedMovingBedList";
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
													$scope.movingBedMasterList = [];

													if (response.data.status == "success") {
														$scope.movingBedMasterList = response.data.listObject;														
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
										$scope.movingCommonListCount, page,
										$scope.noOfRecordsPerPage,$scope.unitId);
								if (flag) {
									$scope.getMovingBedMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMovingBedMasterList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							$rootScope.stopLoader();
							// For Paginations End
							}
							
						

							// Function For  save room master
							$scope.saveMovingBed= function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								 $scope.$broadcast('show-errors-check-validity');
								var data = {
									
									bedCode       :$scope.bedMasterObj.bedCode,
									bedDesc    :$scope.bedMasterObj.bedDesc,																
									status 		:'A',
									createdBy	:$scope.userId,
									updatedBy 	:$scope.userId,
									unitId		:$scope.unitId,
									organizationId :$scope.organizationId,
									createdDate	:$scope.currentDate,
									updatedDate	:$scope.currentDate,
									isMovingBed	:'Y',
									bedStatusId:3,
									bedTypeId:1
								}
									if ($scope.bedFormMoving.$valid) {

								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									console.log("save", data); 
//									return false;
									var URI = BASE_URL + ROOT_URL
											+ ADDBED;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
//															$scope.init();
															$scope.bedMasterObj.bedCode="";
															$scope.bedMasterObj.bedDesc="";
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

														$scope.initMovingBedMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}
							 }

							}
							
							
							// Function For updateBed
							$scope.updateMovingBed = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
//								$scope.$broadcast('show-errors-check-validity');
								var data = {											
										bedCode       :$scope.bedMasterObj.bedCode,
										bedDesc    :$scope.bedMasterObj.bedDesc,																			
										updatedBy 	:$scope.userId,
										updatedDate	:$scope.currentDate,
										unitId		:$scope.unitId,
										organizationId :$scope.organizationId,
										bedId :$scope.bedMasterObj.bedId,
										isMovingBed	:'Y'
								}		
							
//								if ($scope.bedForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEBED;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
//															$scope.init();
															$scope.bedMasterObj.bedCode="";
															$scope.bedMasterObj.bedDesc="";
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
														$scope.initMovingBedMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}

//							}
							
							
							
					
						
						// Function For get single room  entry
						$scope.showUpdateBtnMovingBed = function(id) {

							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;

							var data = {

							}
							var URI = BASE_URL + ROOT_URL
									+ GETBEDBYID + "/"+ id ;
									
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													console.log("By id",response.data.listObject);													 
													$scope.bedMasterObj.bedCode = response.data.listObject[0].bedCode;
													$scope.bedMasterObj.bedDesc = response.data.listObject[0].bedDesc;												
													$scope.bedMasterObj.bedId = response.data.listObject[0].bedId;
													
												} else {
													
													if(response.data.code == '409'){
														
														growl.warning("Bed is used, you can't change status.",
																{
																	title : 'Warning!'
																});
														$scope.saveBtnFlag = true;
														$scope.updateBtnFlag = false;
													}else{
														growl
														.error(
																"Error!!",
																{
																	title : 'Error!'
																});
														$scope.saveBtnFlag = true;
														$scope.updateBtnFlag = false;
													}
													
												}
											});							

						};
						
						

						
						
						$scope.setFirstTabLevel = function(newTab) {
							$scope.firstTabLevel = newTab;
							console.log("firsrt tab="+newTab);
							
							if (newTab == '2') {
								
								$scope.movingBed();
							}
							if (newTab == '1') {
								
								$scope.normalBed();
							}
							
						};

						$scope.isSetFirstTabLevel = function(tabNum) {
							return $scope.firstTabLevel === tabNum;
							
						};
						
						$scope.searchBedMaster = function(searchString){
							$rootScope.startLoader();
							$scope.bedMasterList=[];
							$scope.loadingMsg="Loading...";
							if(searchString!=''){
								var data = {
									organizationId : $scope.organizationId,
									bedDesc : searchString,
									 unitId        :$scope.unitId,
									 isMovingBed : 'N'
								};
							var URI = BASE_URL + ROOT_URL + "/api/adt/bedMasterUniversalSearch"
									
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
												$rootScope.stopLoader();
												$scope.bedMasterList = response.data.listObject;
												if($scope.bedMasterList.length==0)
													$scope.loadingMsg="No record found...";
								});
							}else{
								$scope.initBedMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);

							}
							$rootScope.stopLoader();
						}
						
						$scope.searchMovingBedMaster = function(searchString){
							$rootScope.startLoader();
							$scope.bedMasterList=[];
							$scope.loadingMsg="Loading...";
							if(searchString!=''){
								var data = {
									organizationId : $scope.organizationId,
									bedDesc : searchString,
									 unitId        :$scope.unitId,
									 isMovingBed : 'Y'
								};
							var URI = BASE_URL + ROOT_URL + "/api/adt/bedMasterUniversalSearch"
									
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
												$rootScope.stopLoader();
												$scope.movingBedMasterList = response.data.listObject;
												if($scope.movingBedMasterList.length==0)
													$scope.loadingMsg="No record found...";
								});
							}else{
								$scope.initMovingBedMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);

							}
							$rootScope.stopLoader();
						}

							
						} ]);
