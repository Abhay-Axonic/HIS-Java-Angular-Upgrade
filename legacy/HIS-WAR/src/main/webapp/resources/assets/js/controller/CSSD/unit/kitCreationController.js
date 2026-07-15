'use strict';

angular
		.module('myApp')
		.controller(
				'kitCreationController',
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
								$cookies, $rootScope,GenericService,growl,
								$state,PagerService) {

							/* init() function for form object create. */

							$scope.init = function() {
																
																
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								/******For initial cssd store selection*********/
								
								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj != undefined) {
									$scope.storeId = cssdStoreObj.storeId;
								}
								
								
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.commonList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.kitListCount;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								/***********************************************/
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditKit = false;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
                                
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								// for popup
								$scope.showModal = false;
								$scope.popUpFlag = true;
								// end for popup

								$scope.dummyKitMasterObj = {
										code : "",
										name : "",
										surgeon: "",
										kitInstrumentList: [],
										created_by : $scope.userId,
										updated_by : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										storeId : $scope.storeId
									};

								$scope.kitMasterObj = $scope.dummyKitMasterObj;
								/*
								

								var URI = BASE_URL + ROOT_URL
										+ GETALLKIT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;*/// get intrument url

							/*	
								$scope.surgeonList = [];
								var data = {};
																
								var URI = BASE_URL + ROOT_URL
										+ GETSURGEONLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;// get surgeon url

								// For Get All surgeon Using Offset
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
									if (response.data.status == "success") {
										$scope.surgeonList = response.data.listObject;
									}
								});
								*/
								
								$scope.getAllInstrumentList();
								
								
							}//end of init
							
							
							$scope.instrumentList = [];
							$scope.filteredInstrumentList = [];
							
							$scope.getAllInstrumentList = function() 
							{
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ GETALLINSTRUMENTLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId; 
								
								var data = {};
									
								GenericService.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.instrumentList = response.data.listObject;
														angular.copy($scope.instrumentList,$scope.filteredInstrumentList);
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});

							}
							
							//For Add Equipment Functionality
							
							$scope.isSelectionEmpty = function isSelectionEmpty()
							{
								if( $scope.selection.length > 0 )
									{
									    return true;
									}
								else
									{
									return false;
									}
							}
							
							
							$scope.selection = [];
							
							$scope.instrumentSelection = function instrumentSelection(instrument) {
								var index = $scope.selection.indexOf(instrument);
								
								if(index != -1) {
									$scope.selection.splice(index, 1);
								}
								else {
									$scope.selection.push(instrument);
								}
							}
							
														
							
							$scope.selectedEquipments=[];
							
							$scope.showSelected = function showSelected() {
								
									for(var i = 0;i < $scope.selection.length;i++)
									{
										if($scope.selection[i].quantity !=undefined){
										var index = $scope.instrumentList.indexOf($scope.selection[i]);
										$scope.instrumentList.splice(index, 1);
										$scope.selectedEquipments.push($scope.selection[i])
										}
										else{
											var massage='Enter quantity for Instrument - '+$scope.selection[i].instrumentName;
											 growl.warning(massage,
														
														{
															title : 'Warning!'
														});
										}
										
									}
									
									$scope.selection = [];
															
							}
							
							
							//For Remove Equipment Functionality
							
							$scope.removal = [];
							
							$scope.instrumentRemoval = function instrumentRemoval(instrument) {
								var index = $scope.removal.indexOf(instrument);
								
								if(index != -1) {
									$scope.removal.splice(index, 1);
								}
								else {
									$scope.removal.push(instrument);
								}
								
								
							}
							
                            							
							$scope.showRemoved = function showRemoved() {
								
								
								for(var i = 0;i < $scope.removal.length;i++)
								{
									var index = $scope.selectedEquipments.indexOf($scope.removal[i]);
									$scope.selectedEquipments.splice(index, 1);
									$scope.instrumentList.push($scope.removal[i]);
								}
								$scope.removal = [];
															
							}
							
							
							$scope.isRemovalEmpty = function isRemovalEmpty()
							{
								if( $scope.removal.length > 0 )
					 				{
									    return true;
									}
								else
									{ 
									return false;
									}
							}
							
							// For Equipment List On Info Modal 
							$scope.getEquipmentList = function(info) {
								var kitId=info.kitId;
								var data = {
										kitId:kitId
										};
								//var kitId=info.kitId;
								console.log(kitId)
								$scope.equipmentList = [];
								var URI = BASE_URL + ROOT_URL + "/cssd/getEquipmentList"; 
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.equipmentList = response.data.listObject;
													}
						           });
						
								
								//$scope.equipmentList = info.kitInstrumentList;
							}
												
							
							
							
							$scope.init();

							$scope.getKitCreationMstrlist=function(orgId,offset, noOfRecordsPerPage){
								$rootScope.startLoader();
								var offset1 = offset != null ? offset : 0;
								var perPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								
								var data2 = {
										organizationId:orgId
										};
								
								//var URI1 = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERLIST; 
								var URI2 = BASE_URL + ROOT_URL + "/cssd/getKitListcount";
								var data = {};
								
								$rootScope.startLoader();
								var data = {};
								$scope.kitList = [];
																
								var URI = BASE_URL + ROOT_URL
										+ GETALLKIT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId+"&offset="+offset1+"&noOfRecordsPerPage="+perPage;

								// For Get All Kit List Using Offset
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.kitList = response.data.listObject;
									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){	
										$scope.kitListCount={};
										$rootScope.stopLoader();
										$scope.kitListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
							}
							
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.getKitCreationMstrlist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getKitCreationMstrlist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.kitListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getKitCreationMstrlist($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
								}
							}
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveKit();
							}

							//Function for editing the entries
							$scope.editKit = function(kitObj) {
								$rootScope.startLoader();
								$scope.kitMasterObj = kitObj;
								$scope.kitMasterObj.id = kitObj.id;
								$scope.kitMasterObj.kitInstrumentList = kitObj.kitInstrumentList;
//								$scope.selectedEquipments = kitObj.kitInstrumentList;
								$scope.kitMasterObj.updated_by = $scope.userId;
								$scope.onEditKit = true;
								

								var kitId=kitObj.kitId;
								var data = {
										kitId:kitId
										};
								//var kitId=info.kitId;
								console.log(kitId)
								$scope.equipmentList = [];
								var URI = BASE_URL + ROOT_URL + "/cssd/getEquipmentList"; 
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.selectedEquipments = response.data.listObject;
														for(var i=0;i<$scope.selectedEquipments.length;i++){
															for(var j=0;j<$scope.instrumentList.length;j++){
																if($scope.selectedEquipments[i].instrumentId==$scope.instrumentList[j].instrumentId){
																	$scope.instrumentList.splice(j, 1);
																}
															}
														}
//														$scope.removal.splice(index, 1);
													}
													$rootScope.stopLoader();
						           });
						
								
								//$scope.equipmentList = info.kitInstrumentList;
							
							}
							
							// Function For Save Kit Master
							$scope.saveKit = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								if ($scope.kitForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ CREATEKIT;
									if ($scope.onEditKit) {
										$scope.updateKit()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											$rootScope.startLoader();
											$scope.kitMasterObj.kitInstrumentList = $scope.selectedEquipments;
											$scope.kitMasterObj.isInstrument = false;
											GenericService.serviceAction("POST", URI, $scope.kitMasterObj)
											.then(function(response) {
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.selectedEquipments = [];
													growl.success('Kit added Successfully...!!',{
														title : 'Success!'
													});
													$scope.$broadcast('show-errors-reset');
													$scope.getAllInstrumentList();
													$scope.getKitCreationMstrlist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
//													$scope.init();
												} else {
													$rootScope.stopLoader();
													$scope.selectedEquipments = [];
													growl.error('Kit already exits...!!',{
														title : 'Error!'
													});
												}
											});
										}
									}
								}

							}
							
							$scope.cssdStorePopupFromController = function(){
								$rootScope.currentCSSDScope = $scope;
								$rootScope.cssdStorePopup();
								angular.element('#cssdStoreSelectionPopup').modal('show');
							}
							
							// For Update Kit Status
							
							$scope.updateStatus = function(kitMaster) {

								var data = {
										kitId : kitMaster.kitId,
										status : kitMaster.status,
										updated_by : $scope.userId,
										
									}
								var URI = BASE_URL + ROOT_URL + UPDATEKITMASTERSTATUS;
									
								GenericService
										.serviceAction("PUT", URI,data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Kit Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		'Something Went Wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							
						
							// Function For update Kit
							$scope.updateKit = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.kitForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEKIT;
									$rootScope.startLoader();
									$scope.kitMasterObj.kitInstrumentList = $scope.selectedEquipments;
									GenericService.serviceAction("PUT", URI, $scope.kitMasterObj)
									.then(function(response) {
										if (response.data.status == "success") {
											$rootScope.stopLoader();
											$scope.selectedEquipments=[];
										growl.success('Kit updated Successfully...!!',{
												title : 'Success!'
											});
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											$rootScope.stopLoader();
											$scope.selectedEquipments=[];
											growl.error('Updated Kit already exits...!!',{
												title : 'Error!'
											});
										}
									});
								}
							}
						} ]);
