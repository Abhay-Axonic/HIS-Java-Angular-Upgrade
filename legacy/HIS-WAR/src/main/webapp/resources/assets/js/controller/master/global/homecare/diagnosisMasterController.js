angular
		.module('myApp')
		.controller(
				'diagnosisMasterController',
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
							
							
							
								$scope.selectedCarePlan = [];
								$scope.onEditDiagnosis = false;
								$scope.dummyDiagnosisMasterObj = {
										domain : "",
										className : "",
										description : "",
									//	carePlans: [],
										carePlanVOs: [],
										updatedBy : $scope.userId,
								
								};
								
								$scope.diagnosisMasterObj = $scope.dummyDiagnosisMasterObj;
								
						
								var data = {};

								
								var URI = BASE_URL + ROOT_URL
										+ CAREPLAN;

								
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.carePlanList = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.carePlanId;
																	data.label = value.code;
																	if(value.diagnosisId != undefined)
																	{
																		data.disabled = true;
																	
																	}
																	else {
																		data.disabled = false;
																	}
																	
																	$scope.carePlanList.push(data);
																});
														console
																.log("carePlanList",
																		$scope.carePlanList);

													}
												});
//								}

								var URI = BASE_URL + ROOT_URL
										+ DIAGNOSIS;
							
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.diagnosisList = [];

													if (response.data.status == "success") {
														$scope.diagnosisList = response.data.listObject;
														console.log("diagnosisList",
																		$scope.diagnosisList);

													}
												});


							}
							

							$scope.init();
						
					
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDiagnosis();
							}

							$scope.editDiagnosis = function(diagnosisObj) {
								$scope.diagnosisMasterObj = diagnosisObj;
								/*$scope.diagnosisMasterObj.code = diagnosisObj.code;
								$scope.diagnosisMasterObj.name = diagnosisObj.name;
								$scope.diagnosisMasterObj.description = diagnosisObj.description;
								$scope.diagnosisMasterObj.carePlanVOs = diagnosisObj.carePlanVOs;*/
								
							//	$scope.diagnosisMasterObj.selectedCarePlan = [];
								$scope.diagnosisMasterObj.carePlanVOs = diagnosisObj.carePlanVOs;
								diagnosisObj.carePlanVOs.forEach(function(value, index){
									$scope.selectedCarePlan.push({id : value.carePlanId, label: value.code})
								})
								
								
								$scope.onEditDiagnosis = true
							}
							
							// Function For Save Diagnosis
							$scope.saveDiagnosis = function() {

								 $scope.$broadcast('show-errors-check-validity');
								// return false;
								 var selectedCarePlanArray = [];
									
									for(var i = 0; i< $scope.selectedCarePlan.length;i++)
									{
									//	selectedCarePlanArray.push($scope.selectedCarePlan[i].id);
										var carePlan = {};
										carePlan.carePlanId=$scope.selectedCarePlan[i].id;
										selectedCarePlanArray.push(carePlan);
									}
									
									$scope.diagnosisMasterObj.carePlanVOs = selectedCarePlanArray;

								 if ($scope.diagnosisform.$valid) {
										var URI = BASE_URL + ROOT_URL
									+ DIAGNOSIS;
										if ($scope.onEditDiagnosis) {
											$scope.updateDiagnosis()
										} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {

								
									GenericService
											.serviceAction(
													"POST",
													URI,
													$scope.diagnosisMasterObj)
											.then(
													function(response) {
														// alert(response.data.status);
														if (response.data.status == "success") {
															growl
																	.success(
																			'Diagnosis Added Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Diagnosis already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
									 }
										}
								}

							}


							
							$scope.updateStatusDiagnosis = function(diagnosis) {


								var URI = BASE_URL + ROOT_URL + DIAGNOSIS
								 +"?diagnosis_id="
								+ diagnosis.diagnosisId + "&user_id="
								+ $scope.userId + "&status="
								+ diagnosis.status;
								GenericService
										.serviceAction("DELETE", URI)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'diagnosisId Updated Successfully...!!',
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

							
							$scope.updateDiagnosis = function() {

								 $scope.$broadcast('show-errors-check-validity');
								 var selectedCarePlanArray = [];
									
									for(var i = 0; i< $scope.selectedCarePlan.length;i++)
									{
										selectedCarePlanArray.push($scope.selectedCarePlan[i].id);
									}
									
									$scope.diagnosisObj.carePlans = selectedCarePlanArray;

								 if ($scope.diagnosisform.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ DIAGNOSIS;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
								GenericService
										.serviceAction("PUT", URI,
												$scope.diagnosisMasterObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Diagnosis Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Diagnosis already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
								 }
								 }
							}
						} ]);