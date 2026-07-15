angular
		.module('myApp')
		.controller(
				'viewPreSterilizationKitFormationController',
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
						'$stateParams',
						'$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams, $location) {
							
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

								/***********************************************/
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								if($stateParams.transitionNumber){
									$scope.viewPreKitFormationDetails();
								}
								$scope.firstTabSelected = true;
							}
							
							$scope.setTab = function(tabNumber){
								if(tabNumber == 1){
									$scope.firstTabSelected = true;
								}else{
									$scope.firstTabSelected = false;
								}
							}
							
							$scope.selectedWrapper = {};
							
							$scope.viewPreKitObj = {};
							
							$scope.viewPreKitFormationDetails = function() {
						
								data = {};
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITFORMATIONLISTBYTRANSNO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&transitionNumber=" + $stateParams.transitionNumber;
								
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.viewPreKitObj = response.data.object;
												$scope.selectedWrapper = $scope.viewPreKitObj.wrapper;
											}else{
												$rootScope.stopLoader();
											}
											
										});
							}
							
							$scope.init();
							
							//For Search Wrapper
							
                            $scope.wrapperList = [];
							
                            $scope.loadingMsg="";
							$scope.searchWrapperByCode = function() 
							{
								$scope.showLoadingMsg='Y';
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEWRAPPEREXPIRYBYCODE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId +"&code=" + $scope.searchWrapperField;// search instrument 
																								
								var data = {};
								 $scope.loadingMsg="Loading...";
								// GET instrument list by search
								if($scope.searchWrapperField === "") {
									
									$scope.wrapperList = [] ;
								}
								else {
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.wrapperList = response.data.listObject;
													if($scope.wrapperList.length==0)
													 $scope.loadingMsg="No record found...";
												}
											});	
								}
							}
							$scope.showLoadingMsg='N';
							$scope.clearPreviousSearch =function(){
								$scope.showLoadingMsg='N';
								$scope.searchWrapperField = "";
								$scope.wrapperList = [] ;
							}
							
							//For Selected Kit Details
							$scope.selectedWrapper = {};
							$scope.tempWrapperData = {};
							
                            $scope.selectWrapperInfo = function(wrapper) {
                            	$scope.tempWrapperData = wrapper; 
                            }		
                            
                         // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveFormKit();
							}
							
							$scope.toSaveFormKit = {
									kitId : "",
									updated_by: $scope.userId,
									created_by : $scope.userId,
									kitInstrumentList: []
                            };
							
							$scope.saveFormKit = function() {
								$scope.$broadcast('show-errors-check-validity');
								
									var URI = BASE_URL + ROOT_URL + SAVEFORMKIT;
									
											 $scope.data = {};
											 $scope.viewPreKitObj.wrapperId = $scope.selectedWrapper.id;
											 $scope.viewPreKitObj.wrapperQuantity = $scope.selectedWrapper.wrapperQuantity;
											 $scope.viewPreKitObj.updated_by = $scope.userId;
											
											$rootScope.startLoader();
											GenericService
												.serviceAction("PUT",URI,$scope.viewPreKitObj)
												.then(function(response) {
															if (response.data.status == "success") {
																/*var res = $scope.reduceAvailableStock($scope.viewPreKitObj.wrapperId,
																		$scope.selectedWrapper.selectedBatch.batchId,
																		$scope.storeId,$scope.viewPreKitObj.wrapperQuantity);
																$('.modal-backdrop').remove();*/
																NavigteToPreSterilizationKitFormation();
																$rootScope.stopLoader();
																growl.success('Kit Formed Successfully...!!',{title : 'Success!'});
																$scope.$broadcast('show-errors-reset');
															} else {
																$rootScope.stopLoader();
																growl.error('Something went wrong...!!',{title : 'Error!'});
															}
														});
								}
                            
							var NavigteToPreSterilizationKitFormation =function(){
								$state.go('preSterilizationKitFormation');
							}
							
							
							$scope.reduceAvailableStock = function(itemId,batchId,storeId,reqQuantity){
								
								var URI = BASE_URL + ROOT_URL + "/api/global/stock/consumption";
								var data = {};
								
								/*data.storeeId = storeId;*/
								
								data.storeeId = storeId;
								data.batchId = batchId;
								data.uomTypeId = $scope.selectedWrapper.selectedBatch.selectedUOM.uomTypeId;
								data.consumedQuantity = reqQuantity;
								data.screenId = 138;
								data.unitId = $scope.unitId;
								data.userId = $scope.userId;
								
								GenericService
									.serviceAction("PATCH",URI,data)
									.then(function(response) {
												if (response.data.status == "success") {
													return true;
												} else {
													return false;
												}
											});
							}

							/*$scope.wrapperBatchList = [{batchId : 1, batchName:"BCH1001",availableStock : 180  },{batchId : 2, batchName:"BCH1002",availableStock : 200},{batchId : 3, batchName:"BCH1003",availableStock : 120 }];*/
							
                            $scope.setAvlStock = function(item){
                            	$scope.wrapperStock = item.availableStock;
                            }
                            
                            $scope.wrapperStock = 0;
                            $scope.wrapperBatchList = [];
                            
                            $scope.assignWrapperToKit=function(){
                            	$scope.selectedWrapper = $scope.tempWrapperData;
                            }
                            
                            $scope.showWrapperInfo = function() {
                            	if($scope.selectedWrapper==null){
                            		$scope.selectedWrapper = $scope.tempWrapperData;
                            	  // $('#searchWrapperModal').hide();
                            	}
                            	else{
                            		$('#showVerifyPopup').modal();
                            	}
                            /*	var URI = BASE_URL + ROOT_URL + "/api/global/items/cssd/batches/details";
								
								 var data = {};
								 data.itemIds = [ $scope.selectedWrapper.itemId ];
								 data.storeId = $scope.storeId;
								 data.unitId = $scope.unitId;
			
								 data.itemIds =   data.itemIds;
								 data.storeId =  1;
								 data.unitId =  data.unitId;
								
								$rootScope.startLoader();
								GenericService
									.serviceAction("POST",URI,data)
									.then(function(response) {
												if (response.data.length > 0) {
													$scope.wrapperStock = response.data[0].stockAvailable;
													$scope.wrapperBatchList = response.data[0].batch;
													$rootScope.stopLoader();
													
												} else {
													$rootScope.stopLoader();
												}
											});*/
                            }

                            $scope.checkAgainstAvlStock = function(){
                            	/*
                            	 * if($scope.selectedWrapper.wrapperQuantity > $scope.selectedWrapper.selectedBatch.availableStockQuantity){
                            		if( confirm("Requested quantity can not be more than Available Stock.") == true ) {
                            			$scope.selectedWrapper.wrapperQuantity ="";
									} else {
										document.getElementById('saveFormKitButton').disabled = true;
									}
                            	}else if(document.getElementById('saveFormKitButton').disabled && ! $scope.isSaveDisabled() )
                            		{
                            			document.getElementById('saveFormKitButton').disabled = false;
                            		}
                            }*/
                            
                           /* if($scope.selectedWrapper.wrapperQuantity==null){
                            	 growl.warning("Enter quantity for wrapper",
											
											{
												title : 'Warning!'
											});
                            	 document.getElementById('saveFormKitButton').disabled = false;
                            	}else{
                            		document.getElementById('saveFormKitButton').disabled = false;
                            	}*/
                            }
                            /*if($scope.item != null && $scope.item != undefined){
                            	$scope.wrapperStock = $scope.item.availableStock;
                            }else{
                            	$scope.wrapperStock = "--"
                            }
                            */
                            $scope.checkPhyQue = function(obj){
                            	if(obj.physicalQuantity<=obj.quantity){
                            		//$scope.isSaveDisabled();
                            		if($scope.isSaveDisabled())
                            		{
                            			document.getElementById('saveFormKitButton').disabled = true;
                            		}else
                            			{
                            			document.getElementById('saveFormKitButton').disabled = false;
                            			}
                             	 
                             	}else{
	                             	
                             			growl.warning("Enter valid quantity",
 											
 											{
 												title : 'Warning!'
 											});
                             			obj.physicalQuantity="";
                             			if($scope.isSaveDisabled())
	                            		{
	                            			document.getElementById('saveFormKitButton').disabled = true;
	                            		}
                             	}
                            }
                            
                            $scope.isSaveDisabled = function(){
								
								var flag = false;
								
								if($scope.selectedWrapper == undefined){
									flag = true
								}
								if($scope.selectedWrapper != undefined && $scope.selectedWrapper.wrapperQuantity == undefined){
									flag = true
								}
								/*if($scope.selectedWrapper != undefined && $scope.selectedBatch != undefined && $scope.selectedWrapper.wrapperQuantity != undefined && $scope.selectedWrapper.wrapperQuantity > $scope.selectedBatch.availableStock ){
									flag = true
								}*/
								for(var i = 0; i < $scope.viewPreKitObj.kitInstrumentList.length; i++){
									if($scope.viewPreKitObj.kitInstrumentList[i].physicalQuantity == undefined || $scope.viewPreKitObj.kitInstrumentList[i].physicalQuantity==""){
										flag = true;
										break;
										}
								}
								
								if(flag){
									return true;
								}else{
									return false;
								}
							
							}
						
						}]);



