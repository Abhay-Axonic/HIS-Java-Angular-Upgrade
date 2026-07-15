(function () {
  "use strict";
						function scoreMasterController($scope, $http, $localStorage, $sessionStorage,
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
							
							
								
								
								$scope.onEditScore = false;
								$scope.dummyScoreMasterObj = {
										scoreName : "",
										
										parameterList: [{
											parameterName: '',
											subParameterList: [{
												 subParameterName: '',
												 value: '',
												}],
										}],
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										

								};
								
								$scope.scoreMasterObj = $scope.dummyScoreMasterObj;
								
						
								var data = {};

								
								var URI = BASE_URL + ROOT_URL
										+ SCORE;

								
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.scoreDetailsList = [];

													if (response.data.status == "success") {
														$scope.scoreDetailsList = response.data.listObject;
														
														
														console
																.log("scoreDetailsList",
																		$scope.scoreDetailsList);

													}
												});
										


							}
							

							$scope.init();
						

							  $scope.addParamDetails = function() {
									
									$scope.scoreMasterObj.parameterList.push({
										/*scoreName : "",*/
										
									/*	parameterList: [{
											parameterName: '',*/
											subParameterList: [{
												 subParameterName: '',
												 value: '',
												}],
									//	}],
									

								});
								};

								$scope.removeParamDetails = function(index) {
									 if ($scope.scoreMasterObj.parameterList.length > 1) {
										 $scope.scoreMasterObj.parameterList.splice(index, 1);
									    }
								};

							
							
								 $scope.addSubParamDetails = function(itemIndex) {
									    $scope.scoreMasterObj.parameterList[itemIndex].subParameterList.push({
												 subParameterName: '',
												 value: '',
										});
									  }


								 $scope.removeSubParamDetails = function(itemIndex, subParamListIndex) {
									    if ($scope.scoreMasterObj.parameterList[itemIndex].subParameterList.length > 1) {
									    	$scope.scoreMasterObj.parameterList[itemIndex].subParameterList.splice(subParamListIndex, 1);
									    }
									  }

							
							

								 $scope.getScoreMasterDetailsById = function(scoreMaster) { 
							 var data = {};

							var URI = BASE_URL + ROOT_URL + SCORE + "/getById/" +  scoreMaster.scoreId;

							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.selectedScore = [];

												if (response.data.status == "success") {
													$scope.selectedScore = response.data.object;

													console
															.log(
																	"selectedScore",
																	$scope.selectedScore);

												}
											});
								 }
								 
								 
								 
							
					
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveScoreMaster();
							}
							
							
							
							

							$scope.editScore = function(scoreObj) {
								$scope.scoreMasterObj = scoreObj;
							
							/*	$scope.interventionMasterObj.selectedActivity = [];
								$scope.interventionMasterObj.activityVOs = interventionObj.activityVOs;
								interventionObj.activityVOs.forEach(function(value, index){
									$scope.selectedActivity.push(value.activityId)
								})*/
								$scope.onEditScore = true
							}
							
							// Function For Save Ingrediants
							$scope.saveScoreMaster = function() {

							
								
								 $scope.$broadcast('show-errors-check-validity');
								// return false;

								 if ($scope.scoremasterform.$valid) {
										var URI = BASE_URL + ROOT_URL
									+ SCORE;
										if ($scope.onEditScore) {
											$scope.updateScoreMaster()
										} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {

								
									GenericService
											.serviceAction(
													"POST",
													URI,
													$scope.scoreMasterObj)
											.then(
													function(response) {
														// alert(response.data.status);
														if (response.data.status == "success") {
															growl
																	.success(
																			'Score Added Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
									 }
										}
								}

							}


							
							$scope.updateStatusScoreMaster = function(score) {


								var data = {
									scoreId : score.scoreId,
									status : score.status,
									updatedBy : $scope.userId,
									
								}
								
								var URI = BASE_URL + ROOT_URL + SCORE + "/updateStatus";
								GenericService
										.serviceAction("PUT", URI,data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Score Updated Successfully...!!',
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

							
							$scope.updateScoreMaster = function() {

								 $scope.$broadcast('show-errors-check-validity');
								 
									
									/*var selectedActivitiesArray = [];
									
									for(var i = 0; i< $scope.selectedActivity.length;i++)
									{
										selectedActivitiesArray.push($scope.selectedActivity[i].id);
									}
									
									$scope.interventionMasterObj.activities = selectedActivitiesArray;*/

								 if ($scope.scoremasterform.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ SCORE;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
								GenericService
										.serviceAction("PUT", URI,
												$scope.scoreMasterObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Score updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Score already exits...!!',
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
					    $stateProvider.state('scoreMaster',{
			                   url: '/score',
			                   templateUrl: 'views/master/GlobalMaster/score-master.html',
			                   controller: "scoreMasterController",
			                   title: 'Score Master',
				           })
					  }

  angular
    .module("myApp")
    .config(config)
    .controller("scoreMasterController", scoreMasterController);
})();	