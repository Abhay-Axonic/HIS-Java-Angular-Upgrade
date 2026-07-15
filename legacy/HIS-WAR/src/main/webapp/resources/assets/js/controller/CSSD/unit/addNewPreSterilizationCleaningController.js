angular
		.module('myApp')
		.controller(
				'addNewPreSterilizationCleaningController',
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
								if (cssdStoreObj == undefined) {
									$rootScope.cssdStorePopup();
									cssdStoreObj= JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								}
								
								$scope.storeId = cssdStoreObj.storeId;
								
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
								
								$scope.dishListForPatient = [];
								$scope.datepickerFlag  = {
										opened : false
									};
								
								$scope.openDatePicker = function() {
									$scope.datepickerFlag.opened = true;
								}
								
							

							}
							

														
							//For Search Pre Kit
							
                            $scope.kitDetailsList = [];
							$scope.isInstrument = false;
							$scope.loadingMsg="";
							$scope.showLoadingMsg='N';
							$scope.searchKitByName = function() 
							{
								$scope.kitDetailsList = [];
								$scope.showLoadingMsg='Y';
								$scope.loadingMsg="Loading...";
								if(!$scope.isInstrument){
									var URI = BASE_URL + ROOT_URL
									+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&name=" + $scope.searchKitField;// search instrument	
									
									var data = {};

									// GET instrument list by search
									if($scope.searchKitField === "") {
										$scope.loadingMsg="No record found";
										$scope.kitDetailsList = [] ;
									}
									
									else {
										GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.kitDetailsList = response.data.listObject;
														if($scope.kitDetailsList.length==0)
															$scope.loadingMsg="No record found";
													}
												});	
									}
									
								}else{
									
									var URI = BASE_URL + ROOT_URL
									+ SEARCHINSTRUMENTASKIT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&name=" + $scope.searchKitField;// search instrument	
									
									var data = {};

									// GET instrument list by search
									if($scope.searchKitField === "") {
										$scope.loadingMsg="No record found";
										$scope.kitDetailsList = [] ;
									}
									
									else {
										GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.kitDetailsList = response.data.listObject;
														if($scope.kitDetailsList.length==0)
															$scope.loadingMsg="No record found";
													}
												});	
									}
								}
							}
							
							$scope.isChecked = false; 
							
							$scope.selection = [];
							
							$scope.changeKitCleaningStatus = function() {
								
								if( $scope.isChecked == true ){
								for(var i = 0; i<$scope.preKitEquipmentList.length; i++ ) {
									$scope.preKitEquipmentList[i].status = "Cleaned"
								 }
								}else {
									for(var i = 0; i<$scope.preKitEquipmentList.length; i++ ) {
										$scope.preKitEquipmentList[i].status = "Uncleaned"
									 }
								}
							}
							
							
							
							$scope.isDisabled = function()
							{
								if($scope.preKitEquipmentList.length == 0)
									{
									return true;
									}
								
								
								var flag = false;
								for(var i = 0;i<$scope.preKitEquipmentList.length;i++)
									{
										if($scope.preKitEquipmentList[i].status != 'Cleaned')
											{
											flag = true;
											break;
											}
									}
								
								if(flag == true)
									{
									return true;
									}
								else
									{
									return false;
									}
								
							}
							
                           //For View Pre Kit Details
							$scope.list = [];
                           							
							$scope.getPreSterilizationEquipmentList = function(kitInfo) 
							{
								$scope.list = kitInfo.kitInstrumentList;
							}
							
							
							//For Selected Kit Details
							$scope.selectedKit = {};
							$scope.tempKitData =null;
							
                            $scope.selectKitInfo = function(kitInfo) {
                            	
                            	$scope.tempKitData = kitInfo; 
                            	
                            	$scope.getPreSterilizationEquipmentList(kitInfo);
                            	
                            }		
                            
                            $scope.preKitEquipmentList = [];
							
                            $scope.showKitInfo = function() {
                            	if($scope.tempKitData==null){
                           		 growl.warning(
												'Select Kit for cleaning!!',
												{
													title : 'Warning!'
												});
									return false;
                           	}
                            	$scope.showLoadingMsg='N';
                            	if($scope.preKitEquipmentList.length > 0)
                            	$('#verifyKitModal').modal();
                            	else{
                            	 $('#searchKitPopup').trigger('click');
                            	  $scope.selectedKit = angular.copy($scope.tempKitData); 
                            	  $scope.preKitEquipmentList = angular.copy($scope.list);
                            	  $scope.list = [];
                            	  $scope.tempKitData =null;
                            	}
                            	$scope.searchKitField = "";
                            }
                            
                            $scope.closeVerifyModalFun=function(){
                            	 $scope.selectedKit = angular.copy($scope.tempKitData); 
                            	 $scope.preKitEquipmentList = angular.copy($scope.list);
                            	$('#closeBtnVerifyModal').trigger('click');
                            	$('#searchKitPopup').trigger('click');
                            }
                            
                            $scope.clearPreviousSearchData=function(){
                            	 $scope.tempKitData =null;
                            	$scope.list = [];
                            	$scope.showLoadingMsg='N';
                            	$scope.kitDetailsList=[];
                            }
                         // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveCleanedWorklist();
							}
							
							
							$scope.toSaveCleanedWorklist = {
                            		
									kitId : "",
									updated_by: $scope.userId,
									created_by : $scope.userId,
									kitInstrumentList: []
                            };
							
							$scope.clearSearchDetails = function(){
								
								$scope.kitDetailsList = [];
								$scope.selectedKit = {};
								//$scope.preKitEquipmentList = [];
								
							}
							
							
							$scope.saveCleanedWorklist = function() {
								$scope.$broadcast('show-errors-check-validity');
									
								
								
								if ($scope.addNewPreSterilizationCleaningForm.$valid) {
									$scope.showModal = !$scope.showModal;
									var URI = BASE_URL + ROOT_URL + SAVECLEANEDWORKLIST;
									
											 $scope.data = {};
											
											$scope.toSaveCleanedWorklist.kitId = $scope.selectedKit.kitId;
											$scope.toSaveCleanedWorklist.code = $scope.selectedKit.code;
											$scope.toSaveCleanedWorklist.name = $scope.selectedKit.name;
											$scope.toSaveCleanedWorklist.updated_by = $scope.userId;
											$scope.toSaveCleanedWorklist.created_by = $scope.userId;
											$scope.toSaveCleanedWorklist.kitInstrumentList = $scope.preKitEquipmentList;
											$scope.toSaveCleanedWorklist.organizationId = $scope.organizationId;
											$scope.toSaveCleanedWorklist.unitId = $scope.unitId;
											$scope.toSaveCleanedWorklist.storeId = $scope.storeId;
											$scope.toSaveCleanedWorklist.isInstrument = $scope.isInstrument;
											
											$rootScope.startLoader();
											GenericService
												.serviceAction("POST",URI,$scope.toSaveCleanedWorklist)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigteToPreSterilizationCleaning();
																$rootScope.stopLoader();
																growl.success('Cleaned Worklist Saved Successfully...!!',{title : 'Success!'});
															} else {
																$rootScope.stopLoader();
																if(response.data.message=="No stock availbale"){
																	growl.error('No stock available...!!',{title : 'Error!'});
																}
																//growl.error('Cleaned Worklist already exits...!!',{title : 'Error!'});
															}
														});
									     
									}
								}
 
							var NavigteToPreSterilizationCleaning = function () {        
			                    $state.go('preSterilizationCleaning');
							}
                            $scope.init();

						}]);

