(function () {
  "use strict";
						function carePlanMasterController($scope, $http, $localStorage, $sessionStorage,
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
							
							
								$scope.selectedIntervention=[];
								
								$scope.onEditCarePlan = false;
								$scope.dummyCarePlanMasterObj = {
										code : "",
										name : "",
										description : "",
										interventions : [],
										interventionVOs : [],
										updatedBy : $scope.userId,
										

								};
								
								$scope.carePlanMasterObj = $scope.dummyCarePlanMasterObj;
								
						
								var data = {};

								
								var URI = BASE_URL + ROOT_URL
										+ INTERVENTIONS;

								
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.interventionList = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.interventionId;
																	data.label = value.label;
																	if(value.carePlanId != undefined)
																	{
																		data.disabled = true;
																	
																	}
																	else {
																		data.disabled = false;
																	}
																	
																	$scope.interventionList.push(data);
																});
													
														console
																.log("intervnetionList",
																		$scope.interventionList);

													}
												});
//								}

								var URI = BASE_URL + ROOT_URL
										+ CAREPLAN;
							
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.carePlanList = [];

													if (response.data.status == "success") {
														$scope.carePlanList = response.data.listObject;
														console.log("carePlanList",
																		$scope.carePlanList);

													}
												});


							}
							

							$scope.init();
						
					
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveCarePlan();
							}

							$scope.editCarePlan = function(carePlanObj) {
								$scope.carePlanMasterObj = carePlanObj;
								
							//	$scope.carePlanMasterObj.selectedIntervention = [];
								$scope.carePlanMasterObj.interventionVOs = carePlanObj.interventionVOs;
								carePlanObj.interventionVOs.forEach(function(value, index){
									$scope.selectedIntervention.push({id : value.interventionId, label: value.label})
								})
								$scope.onEditCarePlan = true
							}
							
							// Function For Save Care Plan
							$scope.saveCarePlan = function() {

								 $scope.$broadcast('show-errors-check-validity');
								// return false;
								 var selectedInterventionArray = [];
									
									for(var i = 0; i< $scope.selectedIntervention.length;i++)
									{
										var intervention = {};
										intervention.interventionId=$scope.selectedIntervention[i].id;
										selectedInterventionArray.push(intervention);
										// selectedInterventionArray.push($scope.selectedIntervention[i].id);
									}
									
									$scope.carePlanMasterObj.interventionVOs = selectedInterventionArray;

								 if ($scope.careplanform.$valid) {
										var URI = BASE_URL + ROOT_URL
									+ CAREPLAN;
										if ($scope.onEditCarePlan) {
											$scope.updateCarePlan()
										} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {

								
									GenericService
											.serviceAction(
													"POST",
													URI,
													$scope.carePlanMasterObj)
											.then(
													function(response) {
														// alert(response.data.status);
														if (response.data.status == "success") {
															growl
																	.success(
																			'Care Plan Added Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Care Plan already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
									 }
										}
								}

							}


							
							$scope.updateStatusCarePlan = function(carePlan) {


							
								var URI = BASE_URL + ROOT_URL + CAREPLAN
								 +"?care_plan_id="
								+ carePlan.carePlanId + "&user_id="
								+ $scope.userId + "&status="
								+ carePlan.status;
								GenericService
										.serviceAction("DELETE", URI)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'CARE PLAN Updated Successfully...!!',
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

							
							$scope.updateCarePlan = function() {

								 $scope.$broadcast('show-errors-check-validity');
								// return false;
								 var selectedInterventionArray = [];
									
									for(var i = 0; i< $scope.selectedIntervention.length;i++)
									{
										
										var carePlan = {};
										carePlan.carePlanId=$scope.selectedIntervention[i].id;
										selectedInterventionArray.push(carePlan);
										
									//	selectedInterventionArray.push($scope.selectedIntervention[i].id);
									}
									
									$scope.carePlanMasterObj.interventionVOs = selectedInterventionArray;

								 if ($scope.careplanform.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ CAREPLAN;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
								GenericService
										.serviceAction("PUT", URI,
												$scope.carePlanMasterObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Care Plan Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Care Plan already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
								 }
								 }
							}
						} 
						 function config($stateProvider) {
					    $stateProvider.state('careplanMaster',{
			                   url: '/careplan',
			                   templateUrl: 'views/master/GlobalMaster/care-plan.html',
			                   controller: "carePlanMasterController",
			                   title: 'Care Plan',
				           })
					  }

  angular
    .module("myApp")
    .config(config)
    .controller("carePlanMasterController", carePlanMasterController);
})();	