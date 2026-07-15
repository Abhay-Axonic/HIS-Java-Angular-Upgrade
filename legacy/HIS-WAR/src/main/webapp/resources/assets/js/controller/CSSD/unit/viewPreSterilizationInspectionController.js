angular
		.module('myApp')
		.controller(
				'viewPreSterilizationInspectionController',
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
								$state, PagerService,$stateParams,$location) {
							
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
								
								
								if($stateParams.transitionNumber){
									$scope.viewPreInspectionKitDetails();
								}
							}
							
							$scope.selection = [];
							
							$scope.inspectionStatus = function(selectedEquipment) {

								var index = $scope.selection.indexOf(selectedEquipment);
									
									if(index != -1) {
										$scope.selection.splice(index, 1);
									}
									else {
										$scope.selection.push(selectedEquipment);
									}
								
							}
							
							$scope.isDisabled = function(info)
							{
								if( info.status == 'Inspected')
									{
									return true;
									}
								
									return false;
									
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
							
							$scope.inspectEquipments = function() {
								
								if($scope.selection.length > 0)
									{
									var URI = BASE_URL + ROOT_URL + UPDATEEQUIPMENTINSPECTIONSTATUS;
									$rootScope.startLoader();
									GenericService
											.serviceAction("PUT", URI,$scope.selection)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$rootScope.stopLoader();
															growl.success('Kit Inspection Updated Successfully...!!',{title : 'Success!'});
														} else {
															$rootScope.stopLoader();
															growl.error('Something Went Wrong...!!',{title : 'Error!'});
														}
													});
									}
						}
							
							
							$scope.viewPreKitObj = {};
							
							$scope.viewPreInspectionKitDetails = function() {
						
								data = {};
								
								var URI = BASE_URL + ROOT_URL
								+ VIEWPREKITBYTRANSITIONNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&transitionNumber=" + $stateParams.transitionNumber;
								
								$rootScope.startLoader();														
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.viewPreKitObj = response.data.object;
											}else{
												$rootScope.stopLoader();
											}
										});
							}
							
							$scope.init();

							$scope.toSaveInspectedWorklist = {
									kitId : "",
									updated_by: $scope.userId,
									created_by : $scope.userId,
									kitInstrumentList: []
                            };
								
							$scope.saveInspectionWorklist = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								if ($scope.viewPreSterilizationInspectionForm.$valid) {
									var URI = BASE_URL + ROOT_URL + SAVEINSPECTIONWORKLIST;
									
											 $scope.data = {};
											
											$scope.toSaveInspectedWorklist.kitId = $scope.selectedKit.kitId;
											$scope.toSaveInspectedWorklist.updated_by = $scope.userId;
											$scope.toSaveInspectedWorklist.created_by = $scope.userId;
											$scope.toSaveInspectedWorklist.kitInstrumentList = $scope.preKitEquipmentList;
											$scope.toSaveInspectedWorklist.organizationId = $scope.organizationId;
											$scope.toSaveInspectedWorklist.unitId = $scope.unitId;

											$rootScope.startLoader();
											GenericService
												.serviceAction("POST",URI,$scope.toSaveCleanedWorklist)
												.then(function(response) {
															if (response.data.status == "success") {
																$rootScope.stopLoader();
																growl.success('Inspected Worklist Saved Successfully...!!',{title : 'Success!'});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																$rootScope.stopLoader();
																growl.error('Inspected Worklist already exits...!!',{title : 'Error!'});
															}
														});
									}
								}
							
							$scope.changeInspectInstrumentStatus = function() {
								var instrumentList = $scope.viewPreKitObj.kitInstrumentList
								for(var i = 0; i<instrumentList.length; i++ ) {
									instrumentList[i].status = "Inspected"
								}
								$scope.viewPreKitObj.kitInstrumentList = instrumentList;
							}
							
							$scope.inspectInstrumentObj = {
									id : ""
							};
							
							$scope.inspectionStatus = function(info) {
								$scope.inspectInstrumentObj.id = info.id;
							}
							
							$scope.list = [];
							
							// Function For update inspection status
							$scope.updateInspectionStatus = function() {
								var URI = BASE_URL + ROOT_URL + UPDATEEQUIPMENTINSPECTIONSTATUS;
								
								$rootScope.startLoader();
								var instrumentArray = [];
								for(var i = 0; i<$scope.viewPreKitObj.kitInstrumentList.length; i++){
									var kitInstrumentObj = $scope.viewPreKitObj.kitInstrumentList[i];
									if(kitInstrumentObj['status'] == 'Inspected') {
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
														NavigteToPreSterilizationInspection();
														$rootScope.stopLoader();
														growl.success('Inspection Updated Successfully...!!',{title : 'Success!'});
													} else {
														$rootScope.stopLoader();
														growl.error('Something went wrong...!!',{title : 'Error!'});
													}
												});

							}
							
							var NavigteToPreSterilizationInspection = function(){
								$state.go('preSterilizationInspection');
							}
							
							$scope.instrumentListForReplcement = [];
							
							$scope.searchInstrumentByMultipleFields = function() {
								
								var URI = BASE_URL + ROOT_URL
								+ SEARCHINSTRUMENTBYMANYFIELDS + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								
								if($scope.instCodeToReplace != undefined )
								{
									URI = URI + "&code=" + $scope.instCodeToReplace;
								}
								if($scope.instNameToReplace != undefined )
								{
									URI = URI + "&name=" + $scope.instNameToReplace;
								}
								if($scope.instNoToReplace != undefined )
								{
									URI = URI + "&instrumentNo=" + $scope.instNoToReplace;
								}
								
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.instrumentListForReplcement = response.data.listObject;
											}else{
												$rootScope.stopLoader();
											}
										});
							}
							
							$scope.toBeReplacedInst = {};
							
							$scope.selectToBeReplacedInst = function(info)
							{
								$scope.toBeReplacedInst = info;
							}
							
							$scope.replaceWithInst = {};
							
							$scope.selectInstInfo = function(inst)
							{
								$scope.replaceWithInst = inst;
							}
							
							$scope.replaceInstrument = function()
							{
								var data = {};
								
								var URI = BASE_URL + ROOT_URL
								+ REPLACEINSTRUMENT + "?toBeReplaced=" + $scope.toBeReplacedInst.id + "&replaceWith=" + $scope.replaceWithInst.instrumentId ;
								
								$rootScope.startLoader();
								GenericService
								.serviceAction("PUT", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												growl.success('Instrument replaced Successfully...!!',{title : 'Success!'});
												$scope.init();
											} else {
												$rootScope.stopLoader();
												growl.error('Something went wrong...!!',{title : 'Error!'});
											}
										});
							}
						}]);



