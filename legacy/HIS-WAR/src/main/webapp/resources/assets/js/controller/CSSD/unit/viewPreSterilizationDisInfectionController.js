angular
		.module('myApp')
		.controller(
				'viewPreSterilizationDisInfectionController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams) {
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}
								
								/******For initial cssd store selection*********/

								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj == undefined) {
									$rootScope.cssdStorePopup();
									
								}
								
								$scope.storeId = cssdStoreObj.storeId;
								
								/***********************************************/

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								if($stateParams.transitionNumber){
									$scope.viewPreDisInfectionKitDetails();
								}
							}

							$scope.selection = [];
							
							$scope.disInfectionStatus = function(selectedEquipment) {
								
									var index = $scope.selection.indexOf(selectedEquipment);
									if(index != -1) {
										$scope.selection.splice(index, 1);
									}
									else {
										$scope.selection.push(selectedEquipment);
									}
							}
							
                            $scope.selectAll = false;
							$scope.selectAll = function() {
								if($scope.selectedAll) {
									$scope.selectAll = true;
								}
								else {
									$scope.selectAll = false;
								}
							}
							
							$scope.disInfectedEquipments = function() {
								
								if($scope.selection.length > 0)
									{
									var URI = BASE_URL + ROOT_URL + UPDATEEQUIPMENTDISINFECTIONSTATUS;
									
									$rootScope.startLoader();
									GenericService
											.serviceAction("PUT", URI,$scope.selection)
											.then(function(response) {
												if (response.data.status == "success"){
															$rootScope.stopLoader();
															growl.success('Kit DisInfection Updated Successfully...!!',{title : 'Success!'});
														} else {
															$rootScope.stopLoader();
															growl.error('Something Went Wrong...!!',{title : 'Error!'});
														}
													});
									}
						}
							
							
							$scope.viewPreKitObj = {};
							$scope.viewPreDisInfectionKitDetails = function() {
						
								data = {};
								var URI = BASE_URL + ROOT_URL
								+ VIEWPREKITBYTRANSITIONNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&transitionNumber=" + $stateParams.transitionNumber;
																								
						// For Get Diet List Using Offset
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.viewPreKitObj = response.data.object;
											}else{
												$rootScope.stopLoader();
											}
											
										});
							}
							
							
							$scope.viewPreKitEquipList = [];
							
							$scope.viewPreKitEquipDetails = function() {
						
								data = {};
																
								var URI = BASE_URL + ROOT_URL
								+ VIEWPREKITEQUIPBYID + '?kitId=' + $stateParams.kitId;
								
						// For Get Diet List Using Offset
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.viewPreKitEquipList = response.data.listObject;
											}else{
												$rootScope.stopLoader();
											}
											
										});
							}

							$scope.init();
							
							$scope.changeInstrumentStatus = function() {
								var instrumentList = $scope.viewPreKitObj.kitInstrumentList
								for(var i = 0; i<instrumentList.length; i++ ) {
									instrumentList[i].status = "Disinfected"
								}
								$scope.viewPreKitObj.kitInstrumentList = instrumentList;
							}
							
							$scope.disInfectInstrumentObj = {
									id : ""
							};
							
							$scope.disInfectionStatus = function(info) {
								$scope.disInfectInstrumentObj.id = info.id;
							}
							
							$scope.list = [];
							
							// Function For update inspection status
							$scope.updateDisInfectionStatus = function() {
								var URI = BASE_URL + ROOT_URL + UPDATEEQUIPMENTDISINFECTIONSTATUS;
								
								$rootScope.startLoader();
								var instrumentArray = [];
								for(var i = 0; i<$scope.viewPreKitObj.kitInstrumentList.length; i++){
									var kitInstrumentObj = $scope.viewPreKitObj.kitInstrumentList[i];
									if(kitInstrumentObj['status'] == 'Disinfected') {
										$scope.local_instrument = {};
										$scope.local_instrument['id'] = kitInstrumentObj['id'];
										instrumentArray.push($scope.local_instrument);
									}
								}
								$scope.list = instrumentArray;
								
								GenericService
										.serviceAction("PUT", URI, $scope.list)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$('.modal-backdrop').remove();
														NavigteToPreSterilizationDisInfection();
														$rootScope.stopLoader();
														growl.success('DisInfection Updated Successfully...!!',{title : 'Success!'});
													} else {
														$rootScope.stopLoader();
														growl.error('Something went wrong...!!',{title : 'Error!'});
													}
												});
							}
							
							var NavigteToPreSterilizationDisInfection = function(){
								$state.go('preSterilizationDisInfection');
							}

							$scope.isDisabled = function()
							{
								if($scope.viewPreKitObj.kitInstrumentList.length == 0)
									{
									return true;
									}
								
								var flag = false;
								for(var i = 0;i<$scope.viewPreKitObj.kitInstrumentList.length;i++)
									{
										if($scope.viewPreKitObj.kitInstrumentList[i].status != 'Disinfected')
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
								
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDisInfectedList();
							}
							
							$scope.toSaveDisInfectedList = {
                            		
									kitId : "",
									updated_by: $scope.userId,
									created_by : $scope.userId,
									kitInstrumentList: []
                            };
								
							// to save disinfected list
							$scope.saveDisInfectedList = function() {
								$scope.$broadcast('show-errors-check-validity');
							
								if ($scope.viewPreSterilizationDisInfectionForm.$valid) {
									var URI = BASE_URL + ROOT_URL + SAVEDISINFECTEDLIST;
									
											$scope.data = {};
											
											$rootScope.startLoader();
											$scope.toSaveDisInfectedList.kitId = $scope.selectedKit.kitId;
											$scope.toSaveDisInfectedList.updated_by = $scope.userId;
											$scope.toSaveDisInfectedList.created_by = $scope.userId;
											$scope.toSaveDisInfectedList.kitInstrumentList = $scope.preKitEquipmentList;
										
											GenericService
												.serviceAction("POST",URI,$scope.toSaveDisInfectedList)
												.then(function(response) {
															if (response.data.status == "success") {
																$rootScope.stopLoader();
																growl.success('Disinfected List Saved Successfully...!!',{title : 'Success!'});
																$location.path('/preSterilizationDisInfection');
																$scope.$broadcast('show-errors-reset');
															} else {
																$rootScope.stopLoader();
																growl.error('Cleaned Worklist already exits...!!',{title : 'Error!'});
															}
														});
									}
								}
						
						}]);



