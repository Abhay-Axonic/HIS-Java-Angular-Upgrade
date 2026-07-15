(function () {
  "use strict";
						function activityMasterController($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

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

								$scope.onEditActivity = false;

								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for save update popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;

								// for update status popup
								$scope.showUpdateStatusModal = false;
								$scope.updateStatusPopUpFlag = true;

								// end for popup
								$scope.dummyActivityObj = {
									definition : "",
									description : "",
									code : "",
									updatedBy : $scope.userId

								};

								$scope.activityMasterObj = $scope.dummyActivityObj;

								/* $scope.getActivities = function() { */

								var data = {};

								var URI = BASE_URL + ROOT_URL + ACTIVITIES

								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.activityList = [];
													if (response.data.status == "success") {
														$scope.activityList = response.data.listObject;
														console
																.log(
																		"activityList",
																		$scope.activityList);

													}
												});

								// }
							} // end of init
							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveActivity();
							}


							$scope.editActivity = function(activityObj) {
								$scope.activityMasterObj = activityObj;
								$scope.activityMasterObj.activityId = activityObj.activityId;
								$scope.activityMasterObj.updatedBy = $scope.userId;
								$scope.onEditActivity = true
							}

						
							$scope.saveActivity = function() {
								$scope.$broadcast('show-errors-check-validity');

								if ($scope.activityform.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ ACTIVITIES;
									if ($scope.onEditActivity) {
										$scope.updateActivity()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService
												.serviceAction(
														"POST",
														URI,
														$scope.activityMasterObj)
												.then(
														function(response) {
															if (response.data.status == "success") {
																growl
																		.success(
																				'Activity added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl
																		.error(
																				'Activity already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
										}
									}
								}

							}
                            
							$scope.updateStatusActivity = function(activity) {


								var URI = BASE_URL + ROOT_URL + ACTIVITIES
								+ "?activity_id="
								+ activity.activityId + "&user_id="
								+ $scope.userId + '&status=' + activity.status;
								GenericService
										.serviceAction("DELETE", URI)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Activity Updated Successfully...!!',
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

							
							
							$scope.updateActivity = function() {

								$scope.$broadcast('show-errors-check-validity');
								if ($scope.activityform.$valid) {

									var URI = BASE_URL + ROOT_URL + ACTIVITIES;
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										GenericService
												.serviceAction(
														"PUT",
														URI,
														$scope.activityMasterObj)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Activity updated Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl
																		.error(
																				'Activity already exits...!!',
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
					    $stateProvider.state('activitiesMaster',{
			                   url: '/activity',
			                   templateUrl: 'views/master/GlobalMaster/activity.html',
			                   controller: "activityMasterController",
			                   title: 'Activity',
				           })
					  }

  angular
    .module("myApp")
    .config(config)
    .controller("activityMasterController", activityMasterController);
})();	
						