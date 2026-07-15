'use strict';

angular
		.module('myApp')
		.controller(
				'addNewSterilizationLoadBatchController',
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
						'$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService, $location) {

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
								if (cssdStoreObj == undefined) {
									$rootScope.cssdStorePopup();
									
								}
								
								$scope.storeId = cssdStoreObj.storeId;
								
								/***********************************************/
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditAutoClave = false;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var data = {};
																																
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVESTERILIZATION + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;

								$rootScope.startLoader();
								GenericService.serviceAction("GET", URI, data)
									.then(function(response) {
									$scope.sterilizationList = [];

									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.sterilizationList = response.data.listObject;
										console.log("sterilizationList",$scope.sterilizationList);
										}else{
											$rootScope.stopLoader();
										}
									});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEAUTOCLAVESLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								
								// For Get Auto Clave List Using Offset
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
									$scope.autoClaveList = [];

									if (response.data.status == "success") {
										$scope.autoClaveList = response.data.listObject;
										console.log("autoClaveList",$scope.autoClaveList);
									}
								});
								
								document.getElementById('isBiological').onchange = function() {
								    document.getElementById('control').disabled = !this.checked;
								    document.getElementById('test').disabled = !this.checked;
								};
							}
							
							$scope.selectedMachine ="";
							
							$scope.selectedAutoclave = {};
							
							$scope.showSterilizaitonType=function()
							{
								for(var i = 0; i < $scope.autoClaveList.length; i++)
									{
									if($scope.selectedMachine == $scope.autoClaveList[i].id)
										{
										$scope.selectedAutoclave = $scope.autoClaveList[i];
										}
									}
								
								var URI = BASE_URL + ROOT_URL
								+ GETRECENTBIOINDIDATE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&autoclaveId=" + $scope.selectedMachine;	
									
								var data = {};
								
								$rootScope.startLoader();
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													var epoch = response.data.object;
												if( epoch != null )
												{
													$scope.dueDate = new Date(epoch);
													$scope.dueDate.setDate($scope.dueDate.getDate() + $scope.selectedAutoclave.biological_indicator_day);
													$scope.dueDate = ("0" + $scope.dueDate.getDate()).slice(-2) +'-'+("0" + ($scope.dueDate.getMonth()+1)).slice(-2)+'-'+$scope.dueDate.getFullYear();
												}
												else
											    {
											        $scope.dueDate = new Date();
											        $scope.dueDate = ("0" + $scope.dueDate.getDate()).slice(-2) +'-'+("0" + ($scope.dueDate.getMonth()+1)).slice(-2)+'-'+$scope.dueDate.getFullYear();
											    }
												
												var currDate = new Date();
												var dueDateByEpoch = new Date(epoch)
												dueDateByEpoch.setDate(dueDateByEpoch.getDate() + $scope.selectedAutoclave.biological_indicator_day);
												
												if( dueDateByEpoch <= currDate ) {
													
													if( confirm("Biological Indicator Needed For this machine. Do you want to proceed?") == true ) {
														$scope.biologicalIndicator = true;
														document.getElementById('control').disabled = false;
													    document.getElementById('test').disabled = false;
													} else {
														$scope.selectedMachine = null;
														$scope.selectedAutoclave = null;
														$scope.dueDate = null;
														$scope.biologicalIndicator = false;
														document.getElementById('control').disabled = true;
													    document.getElementById('test').disabled = true;
													}
												}
												else
												{
													$scope.biologicalIndicator = false;
													document.getElementById('control').disabled = true;
												    document.getElementById('test').disabled = true;
												}
												
												}else{
													$rootScope.stopLoader();
												}
											});	
							}
							
							$scope.isLoadBatchDisabled = function(){
								var flag = false;
								var empty = true;
								
								if($scope.selectedMachine == undefined){
									flag = true;
								}
								if($scope.selectedMachine == undefined){
									flag = true;
								}
								
								if($scope.kitList != undefined){
									for(var i = 0; i < $scope.kitList.length; i++){
										if($scope.kitList[i].Selected)
										{
											empty = false;
											break;
										}
									}
									
									if(empty){
										flag = true;
									}
								}
								
								if(flag){
									return true;
								}else{
									return false;
								}
							}
							
							 $scope.kitDetailsList = [];
								
							    $scope.loadingMsg="";
							    $scope.showLoadingMsg='N';
								$scope.searchKitByName = function() 
								{
									 $scope.loadingMsg="Loading...";
									 $scope.showLoadingMsg='Y';
									 $scope.kitDetailsList = [];
									var URI = BASE_URL + ROOT_URL
									+ SEARCHKITSTOLOADBATCH + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&name=" + $scope.searchKitField;// search instrument	

									var data = {};
									
									if($scope.searchKitField=="") {
										$scope.kitDetailsList = [] ;
										$scope.loadingMsg="No record found";
									}
									else {
										
										GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.kitDetailsList = response.data.listObject;
														if($scope.kitDetailsList.length==0){
															 $scope.loadingMsg="No record found";
														}
													}
												});	
									}
								}
								
								$scope.clearPreviousSearch = function(){
									$scope.searchKitField = "";
									$scope.kitDetailsList = [] ;
								}
								
								//For Selected Kit Details
								$scope.selectedKit = {};
								$scope.tempKitData = [];
								
	                            $scope.selectKitInfo = function(kitInfo) {
	                            	
	                            	var index = $scope.tempKitData.indexOf(kitInfo);
	                            	
	                            	if(index != -1) {
										$scope.tempKitData.splice(index, 1);
									}
									else {
										$scope.tempKitData.push(kitInfo);
									}
	                            }		
	                            
	                            $scope.isCloseKitAddPopup='Y';
	                            $scope.closeVerifyModalFun=function(){
	                            	$('#closeBtnVerifyModal').trigger('click');
                            		$('#closeBtnKitModal').trigger('click');
	                            }
	                            
	                            $scope.finalKitList = [];
	                            $scope.kitList = [];
	                            $scope.showKitInfo = function() {
	                            	if($scope.kitDetailsList.length==0){
	                            		 growl.warning(
													'Select Kit for batch load!!',
													{
														title : 'Warning!'
													});
										return false;
	                            	}
	                            	
	                            	if($scope.kitList.length>0){
	                            		 $scope.isCloseKitAddPopup='N';
	                            		$('#verifyKitModal').modal();
	                            	}
	                            	for(var i = 0; i < $scope.kitDetailsList.length;i++)
	                            	{
	                            		if($scope.kitDetailsList[i].Selected)
	                            		{
	                            		    $scope.finalKitList.push($scope.kitDetailsList[i]);
	                            		}
	                            		
	                            	}
	                            	$scope.kitList = angular.copy($scope.finalKitList);
	                            	
	                            	for(var i = 0;i<$scope.kitList.length;i++)
                            		{
	                            		$scope.kitList[i].Selected = false; 
                            		}
	                            	//$scope.kitDetailsList = [];
	                            	$scope.searchKitField = "";
	                            	$scope.finalKitList = [];
	                            	$scope.IsAllChecked = false;
	                            	
	                            	if($scope.isCloseKitAddPopup=='Y'){
	                            	$('#closeBtnKitModal').trigger('click');
	                            	}
	                            	$scope.showLoadingMsg='N';     	
	                            }
														
							$scope.checkUncheckKit = function() {
								$scope.IsAllChecked = true;
				                for (var i = 0; i < $scope.kitDetailsList.length; i++) {
				                    if (!$scope.kitDetailsList[i].Selected) {
				                        $scope.IsAllChecked = false;
				                        break;
				                    }
				                };
							   
							  }
							
							$scope.checkUncheckAll = function() {
								 for (var i = 0; i < $scope.kitDetailsList.length; i++) {
					                    $scope.kitDetailsList[i].Selected = $scope.IsAllChecked;
					                }
							}
							
							$scope.checkUncheckKitToLoad = function() {
								$scope.IsAllKitsChecked = true;
				                for (var i = 0; i < $scope.kitList.length; i++) {
				                    if (!$scope.kitList[i].Selected) {
				                        $scope.IsAllKitsChecked = false;
				                        break;
				                    }
				                };
							   
							  }
							
							$scope.checkUncheckAllKitsToLoad = function() {
								 for (var i = 0; i < $scope.kitList.length; i++) {
					                    $scope.kitList[i].Selected = $scope.IsAllKitsChecked;
					                }

							}

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.loadBatch();
							}

							$scope.loadBatchObj = {
									updated_by: $scope.userId,
									created_by : $scope.userId,
									kitList: []
                            };
							
							//To Load Batch
							$scope.loadBatch = function() {
								$scope.$broadcast('show-errors-check-validity');
								if($scope.selectedMachine==""){
									 growl.warning(
												'Select machine for load batch !!',
												{
													title : 'Warning!'
												});
									return false;
								}
								
								$rootScope.startLoader();
								if ($scope.addNewSterilizationLoadBatchForm.$valid) {
									var URI = BASE_URL + ROOT_URL + LOADBATCH;
									
											 $scope.data = {};
											 $scope.kitListToSend = [];
											 
											 for(var i = 0; i < $scope.kitList.length;i++)
				                            	{
				                            		if($scope.kitList[i].Selected)
				                            		{
				                            			delete $scope.kitList[i].Selected;
				                            		    $scope.kitListToSend.push($scope.kitList[i]);
				                            		}
				                            	}
											 
											 for(var i = 0; i < $scope.kitListToSend.length;i++)
				                            	{
												 var expDate = new Date();
												 expDate.setDate(expDate.getDate() + $scope.kitListToSend[i].expiryDuration);
												 $scope.kitListToSend[i].expiryDate = expDate.toString();
												 console.log("expDate",$scope.kitListToSend[i].expiryDate)
				                            	}
											 
											$scope.loadBatchObj.autoclaveNumber = $scope.selectedMachine; 
											$scope.loadBatchObj.updated_by = $scope.userId;
											$scope.loadBatchObj.created_by = $scope.userId;
											$scope.loadBatchObj.kitList = $scope.kitListToSend;
											$scope.loadBatchObj.organizationId = $scope.organizationId;
											$scope.loadBatchObj.unitId = $scope.unitId;
											$scope.loadBatchObj.storeId = $scope.storeId;
											$scope.loadBatchObj.biologicalIndicator = $scope.biologicalIndicator;
											$scope.loadBatchObj.control = $scope.control;
											$scope.loadBatchObj.test = $scope.test;
											//return false;
											GenericService
												.serviceAction("POST",URI,$scope.loadBatchObj)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigteToSterilizationLoadBatch();
																$rootScope.stopLoader();
																growl.success('Batch Loaded Successfully...!!',{title : 'Success!'});
																$scope.$broadcast('show-errors-reset');
															} else {
																$rootScope.stopLoader();
																growl.error('Something Went Wrong !',{title : 'Error!'});
															}
														});
									     
									}
								}
							
							var NavigteToSterilizationLoadBatch = function () {        
			                    $state.go('sterilizationLoadBatch');
							}

							$scope.init();
							
						} ]);

