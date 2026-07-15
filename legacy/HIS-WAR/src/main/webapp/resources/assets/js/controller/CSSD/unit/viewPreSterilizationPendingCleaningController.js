angular
		.module('myApp')
		.controller(
				'viewPreSterilizationPendingCleaningController',
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
									//$scope.viewPreKitEquipDetails();
									
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
								if( info.status == 'Cleaned')
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
									var URI = BASE_URL + ROOT_URL + UPDATEEQUIPMENTCLEANINGSTATUS;
									
									GenericService
											.serviceAction("PUT", URI,$scope.selection)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Kit Cleaning Updated Successfully...!!',
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
							
						}
							
							
							$scope.viewPreKitObj = {};
							
							$scope.viewPreInspectionKitDetails = function() {
						
								data = {};
								
								var URI = BASE_URL + ROOT_URL
								+ VIEWPREKITBYTRANSITIONNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&transitionNumber=" + $stateParams.transitionNumber;
																						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.viewPreKitObj = response.data.object;
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
											
											
											
											GenericService
												.serviceAction("POST",URI,$scope.toSaveCleanedWorklist)
												.then(function(response) {
															if (response.data.status == "success") {
																
																growl.success('Inspected Worklist Saved Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl.error(
																				'Inspected Worklist already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
									     
									}
								}
							
													
							$scope.changeInspectInstrumentStatus = function() {
								var instrumentList = $scope.viewPreKitObj.kitInstrumentList
								if($scope.selectedAll)
									{
										for(var i = 0; i<instrumentList.length; i++ ) {
											instrumentList[i].status = "Cleaned";
									    }
									}else
									{
										for(var i = 0; i<instrumentList.length; i++ ) {
											instrumentList[i].status = "Uncleaned";
										}
									}
								$scope.viewPreKitObj.kitInstrumentList = instrumentList;
								
							}
							
							
							$scope.cancelInstrumentCleaning = function() {
								$scope.selectedAll = false;
								$scope.changeInspectInstrumentStatus();
							}
							
							
							
							
							
							/*$scope.changeInspectInstrumentStatus = function() {
								$scope.list = $scope.viewPreKitObj.kitInstrumentList
								for(var i = 0; i<list.length; i++ ) {
									list[i].status = "Inspected"
								}
							}*/
							
							
							
							$scope.inspectInstrumentObj = {
									id : ""
							};
							
							
							$scope.inspectionStatus = function(info) {
								$scope.inspectInstrumentObj.id = info.id;
							}
							
							$scope.list = [];
							
							// Function For update inspection status
							$scope.updateInspectionStatus = function() {
								var URI = BASE_URL + ROOT_URL + UPDATEEQUIPMENTCLEANINGSTATUS;
								
								var instrumentArray = [];
								for(var i = 0; i<$scope.viewPreKitObj.kitInstrumentList.length; i++){
									var kitInstrumentObj = $scope.viewPreKitObj.kitInstrumentList[i];
									if(kitInstrumentObj['status'] == 'Cleaned') {
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
														growl
																.success(
																		'Cleaning Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});

							}
							
							$scope.instrumentListForReplcement = [];
							
							$scope.searchInstrumentByMultipleFields = function() {
								
								
								var URI = BASE_URL + ROOT_URL
								+ SEARCHINSTRUMENTBYMANYFIELDS + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId ;
								
								
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
								
								
																						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.instrumentListForReplcement = response.data.listObject;
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
																						
								GenericService
								.serviceAction("PUT", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Instrument replaced Successfully...!!',
																{
																	title : 'Success!'
																});
												$scope.init();
											} else {
												growl
														.error(
																'Something went wrong...!!',
																{
																	title : 'Error!'
																});
											}
										});
								
								
							}
							
							
							
							
								
								
								
						
						}]);



