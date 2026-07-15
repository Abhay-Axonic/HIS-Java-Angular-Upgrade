(function () {
  "use strict";
						function interventionMasterController($scope, $http, $localStorage, $sessionStorage,
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
							
								$scope.interventionId;
								$scope.selectedActivity=[];
								
								$scope.onEditIntervention = false;
								$scope.dummyInterventionMasterObj = {
										code : "",
										label : "",
										definition : "",
										activities: [],
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										
										activityVOs: []
										

								};
								
								$scope.interventionMasterObj = $scope.dummyInterventionMasterObj;
								
						
								var data = {};

								
								var URI = BASE_URL + ROOT_URL
										+ ACTIVITIES;

								
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.activityList = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.activityId;
																	data.label = value.code;
																	if(value.interventionId != undefined)
																	{
																		data.disabled = true;
																	
																	}
																	else {
																		data.disabled = false;
																	}
																	
																	$scope.activityList.push(data);
																});
													
														
														
														console
																.log("activityList",
																		$scope.activityList);

													}
												});
//								}

								var URI = BASE_URL + ROOT_URL
										+ INTERVENTIONS;
							
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.interventionList = [];

													if (response.data.status == "success") {
														$scope.interventionList = response.data.listObject;
														console.log("interventionList",
																		$scope.interventionList);

													}
												});


							}
							

							$scope.init();
						
					
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveIntervention();
							}
							
							
							// For Checkbox onChange
						/*	$scope.changedValue = function(item) {
								  var obj = {
										  "activityId" : item.activityId
								  }	
								 
							
								  if(item.isChecked){
								    $scope.selectedActivity.push(obj);
						
								  }
								  else {
									  var index = null;
									  $scope.selectedActivity.findIndex(x => x.activityId==item.activityId);
									  $scope.selectedActivity.splice(index, 1);
									  
								  }
							  };
							
							
							
							*/
						
							
							
							

							$scope.editIntervention = function(interventionObj) {
								$scope.interventionMasterObj = interventionObj;
							
								//$scope.interventionMasterObj.selectedActivity = [];
								$scope.interventionMasterObj.activityVOs = interventionObj.activityVOs;
								interventionObj.activityVOs.forEach(function(value, index){
									$scope.selectedActivity.push({id : value.activityId, label: value.code})
								})
								$scope.onEditIntervention = true
							}
							
							// Function For Save Ingrediants
							$scope.saveIntervention = function() {

								
								var selectedActivitiesArray = [];
								
								for(var i = 0; i< $scope.selectedActivity.length;i++)
								{
									var activity = {};
									activity.activityId=$scope.selectedActivity[i].id;
									selectedActivitiesArray.push(activity);
								}
								
								$scope.interventionMasterObj.activityVOs = selectedActivitiesArray;
								
								 $scope.$broadcast('show-errors-check-validity');
								// return false;

								 if ($scope.interventionform.$valid) {
										var URI = BASE_URL + ROOT_URL
									+ INTERVENTIONS;
										if ($scope.onEditIntervention) {
											$scope.updateIntervention()
										} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {

								
									GenericService
											.serviceAction(
													"POST",
													URI,
													$scope.interventionMasterObj)
											.then(
													function(response) {
														// alert(response.data.status);
														if (response.data.status == "success") {
															growl
																	.success(
																			'Intervention Added Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Intervention already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
									 }
										}
								}

							}


							
							$scope.updateStatusIntervention = function(intervention) {


								var URI = BASE_URL + ROOT_URL + INTERVENTIONS
								+ "?intervention_id="
								+ intervention.interventionId + "&user_id="
								+ $scope.userId + '&status=' + intervention.status;
								GenericService
										.serviceAction("DELETE", URI)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Intervention Updated Successfully...!!',
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

							
							$scope.updateIntervention = function() {

								 $scope.$broadcast('show-errors-check-validity');
								 
									
									var selectedActivitiesArray = [];
									
									for(var i = 0; i< $scope.selectedActivity.length;i++)
										
									{
										var activity = {};
										activity.activityId=$scope.selectedActivity[i].id;
										selectedActivitiesArray.push(activity);
										
										
									//	selectedActivitiesArray.push($scope.selectedActivity[i].id);
									}
									
									$scope.interventionMasterObj.activityVOs = selectedActivitiesArray;

								 if ($scope.interventionform.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ INTERVENTIONS;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
								GenericService
										.serviceAction("PUT", URI,
												$scope.interventionMasterObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Intervention Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Intervention already exits...!!',
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
					    $stateProvider.state('interventionsMaster',{
			                   url: '/intervention',
			                   templateUrl: 'views/master/GlobalMaster/intervention.html',
			                   controller: "interventionMasterController",
			                   title: 'Intervention',
				           })
					  }

  angular
    .module("myApp")
    .config(config)
    .controller("interventionMasterController", interventionMasterController);
})();	