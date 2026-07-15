angular
		.module('myApp')
		.controller(
				'rejectedDietListController',
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
								
								$scope.datepicker = new Date();
								
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
							
								$scope.datepickerFlag = {
										opened : false
									};
								
								$scope.openDatePicker = function() {
									$scope.datepickerFlag.opened = true;
								}
								
								$scope.floorMasterObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								
								
								//Start : Getting initial diet issue info on current date
								
								$scope.currDate = new Date();
								
								var date ='';
								
								date ="'" +("0" + $scope.currDate.getDate()).slice(-2) +'-'+("0" + ($scope.currDate.getMonth()+1)).slice(-2)+'-'+$scope.currDate.getFullYear()+"'";
								
								var rejectedDietUrl = BASE_URL + ROOT_URL + GETINITIALREJECTEDDIETLIST + '?dietDate=' + date + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var rejectedDietUrl = BASE_URL + ROOT_URL + GETINITIALREJECTEDDIETLIST + '?dietDate=' + date;*/
								
								GenericService
								.serviceAction("GET", rejectedDietUrl, data)
								.then(
										function(response) {
											$scope.rejectedDietList = [];

											if (response.data.status == "success") {
												$scope.rejectedDietList = response.data.listObject;
												console.log("rejectedDietList",$scope.rejectedDietList);

											}
										});
								
								
								//End
								
								
								

								var data = {};

								
						
					
								// For Get Meal Master Preference from ADT List Using Offset
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEFLOORMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETACTIVEFLOORMASTERLIST;*/
								
								GenericService
								.serviceAction("POST", URI, $scope.floorMasterObj)
								.then(
										function(response) {
								$scope.floorMasterList = [];

									if (response.data.status == "success") {
										$scope.floorMasterList = response.data.listObject;
										console
											.log(
													"floorMasterList",
													$scope.floorMasterList);

								}
									$scope.editTemplate = false;
									
									if($stateParams.dietTemplateId){
										$scope.editTemplate = true;
										$scope.getDietTemplate();
								
									}
									
							});
							
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEWARDMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETACTIVEWARDMASTERLIST;*/
								
								GenericService
								.serviceAction("POST", URI, $scope.floorMasterObj)
								.then(
										function(response) {
								$scope.wardMasterList = [];

									if (response.data.status == "success") {
										$scope.wardMasterList = response.data.listObject;
										console.log("wardMasterList",$scope.wardMasterList);

								}
									$scope.editTemplate = false;
									
									if($stateParams.dietTemplateId){
										$scope.editTemplate = true;
										$scope.getDietTemplate();
								
									}
									
							});
							
								var URI = BASE_URL + ROOT_URL
								+ ACTIVEMEALMASTER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var URI = BASE_URL + ROOT_URL
								+ ACTIVEMEALMASTER;*/
								
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
								$scope.mealMasterList = [];

									if (response.data.status == "success") {
										$scope.mealMasterList = response.data.listObject;
										$scope.mealMasterList.unshift({ mealId: 0, name: 'All' });
										console.log("mealMasterList",$scope.mealMasterList);

								}
									$scope.editTemplate = false;
									
									if($stateParams.dietTemplateId){
										$scope.editTemplate = true;
										$scope.getDietTemplate();
								
									}
									
							});
							}
							
							
							
							
							
							//For Get Diet Issue List
							
							$scope.getRejectedDietIssueList = function() {

							    $rootScope.startLoader();
								data = {};
								var date ='';
								date ="'" +("0" + $scope.datepicker.getDate()).slice(-2) +'-'+("0" + ($scope.datepicker.getMonth()+1)).slice(-2)+'-'+$scope.datepicker.getFullYear()+"'";
								
								/*var URI = BASE_URL + ROOT_URL 
										 + DIETREJECT + '?dietDate=' + date + '&floorId=' + $scope.selectedFloorId 
										 + '&wardId=' + $scope.selectedWardListId + '&mealId=' + $scope.selectedMealId
										 + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
								
								
								var rejectedDietListUrl = BASE_URL + ROOT_URL + DIETREJECT + '?dietDate=' + date + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								if($scope.selectedFloorId != undefined )
								{
									rejectedDietListUrl = rejectedDietListUrl + '&floorId=' + $scope.selectedFloorId; 
								}
								if($scope.selectedWardListId != undefined )
								{
									rejectedDietListUrl =  rejectedDietListUrl + '&wardId=' + $scope.selectedWardListId;
								}
							
								if($scope.selectedMealId != undefined )
								{
									rejectedDietListUrl =  rejectedDietListUrl + '&mealId=' + $scope.selectedMealId;
								}
								
								GenericService
								.serviceAction("GET", rejectedDietListUrl, data)
								.then(
										function(response) {
											$scope.rejectedDietList = [];

											if (response.data.status == "success") {
												$scope.rejectedDietList = response.data.listObject;
												console.log("rejectedDietList",$scope.rejectedDietList);

											}
											$rootScope.stopLoader();
										});
								
							}
							
							$scope.showDishListForPatient = function(info) {
								$scope.dishListForPatient = info
							}
							
							
							$scope.dietList = [];
							$scope.selectDietReIssue= function (order) {
								for (var i =0; i<order.mealDetails.dishList.length; i++) {
					        			
					        				if (order.isChecked) {
					        					$scope.dietList.push(order.mealDetails.dishList[i].orderId);
					        			
					        				} else {
					        					  var index = null;
												  $scope.dietList.findIndex(x => x.orderId==order.mealDetails.dishList[i].orderId);
												  $scope.dietList.splice(index, 1);
												  
												  
					        				}
					        				
					        				console.log($scope.dietList);
					        		}
								
					        };
							
						  // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveRejectedDietOrder();
								
							}
							
							$scope.saveRejectedDietOrder = function(dietOrder) {
								$scope.$broadcast('show-errors-check-validity');
                                $rootScope.startLoader();
								
								if ($scope.rejectedDietListForm.$valid && ($scope.dietList != undefined && $scope.dietList != null
                                                                      										&& $scope.dietList.length > 0)) {

									var URI = BASE_URL + ROOT_URL
									+ DIETREJECT;
									
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService
												.serviceAction("POST",URI,$scope.dietList)
												.then(function(response) {
															if (response.data.status == "success") {
																$scope.getRejectedDietIssueList();
																growl.success('Diet Order Rejected Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
																$rootScope.stopLoader();
															} else {
															    $rootScope.stopLoader();
																growl.error(
																				'Diet Order already exits...!!',
																				{
																					title : 'Error!'
																				});
																				$rootScope.stopLoader();
															}
															$scope.dietList = [];
														});
										}
									}else{
									    $rootScope.stopLoader();
                                        growl.warning(
                                                'Please select Diet...!!',
                                                {
                                                    title : 'Warning!'
                                                });
                                    }
								}

							
							$scope.init();
							
						
						}]);

