angular
		.module('myApp')
		.controller(
				'dietIssueController',
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
								$scope.dishListForPatient = [];
								$scope.datepickerFlag  = {
										opened : false
									};
								
								$scope.openDatePicker = function() {
									$scope.datepickerFlag.opened = true;
								}
								
								$scope.floorMasterObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
							

								var data = {};

								
						
					
				// For Get Meal Master PReference from ADT List Using Offset
								/*var URI = BASE_URL + ROOT_URL
								+ GETACTIVEFLOORMASTERLIST;*/	
								
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEFLOORMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							
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
							
							
							
							//Start : Getting initial diet issue info on current date
							
							$scope.currDate = new Date();
							
							var date ='';
							
							date ="'" +("0" + $scope.currDate.getDate()).slice(-2) +'-'+("0" + ($scope.currDate.getMonth()+1)).slice(-2)+'-'+$scope.currDate.getFullYear()+"'";
							
							var dietIssueUrl = BASE_URL + ROOT_URL + GETINITIALDIETISSUELIST + '?dietDate=' + date + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							
							/*var dietIssueUrl = BASE_URL + ROOT_URL + GETINITIALDIETISSUELIST + '?dietDate=' + date;*/
							$rootScope.startLoader();
							
							GenericService
							.serviceAction("GET", dietIssueUrl, data)
							.then(
									function(response) {
										$scope.dietIssueList = [];

										if (response.data.status == "success") {
										    $rootScope.stopLoader();
											$scope.dietIssueList = response.data.listObject;
											console.log("dietIssueList",$scope.dietIssueList);

										}
										$rootScope.stopLoader();
									});
							
							
							//End
							
							/*var URI = BASE_URL + ROOT_URL
							+ GETACTIVEWARDMASTERLIST;*/
							
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEWARDMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							
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
							
							/*var URI = BASE_URL + ROOT_URL
							+ ACTIVEMEALMASTER;*/
							
							var URI = BASE_URL + ROOT_URL
							+ ACTIVEMEALMASTER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							
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
							
							$scope.getDietIssueList = function() {
								data = {};
							    $rootScope.startLoader();
								var date ='';
								
								date ="'" +("0" + $scope.datepicker.getDate()).slice(-2) +'-'+("0" + ($scope.datepicker.getMonth()+1)).slice(-2)+'-'+$scope.datepicker.getFullYear()+"'";
								
								/*var URI = BASE_URL + ROOT_URL 
										 + GETDIETISSUELIST + '?dietDate=' + date + '&floorId=' + $scope.selectedFloorId + '&wardId=' + $scope.selectedWardListId + '&mealId=' + $scope.selectedMealId;*/
								
								var dietIssueUrl = BASE_URL + ROOT_URL + GETDIETISSUELIST + '?dietDate=' + date + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var dietIssueUrl = BASE_URL + ROOT_URL + GETDIETISSUELIST + '?dietDate=' + date;*/
								
								if($scope.selectedFloorId != undefined )
								{
									dietIssueUrl = dietIssueUrl + '&floorId=' + $scope.selectedFloorId; 
								}
								if($scope.selectedWardListId != undefined )
								{
									dietIssueUrl =  dietIssueUrl + '&wardId=' + $scope.selectedWardListId;
								}
							
								if($scope.selectedMealId != undefined )
								{
									dietIssueUrl =  dietIssueUrl + '&mealId=' + $scope.selectedMealId;
								}
								
								GenericService
								.serviceAction("GET", dietIssueUrl, data)
								.then(
										function(response) {
											$scope.dietIssueList = [];

											if (response.data.status == "success") {

												$scope.dietIssueList = response.data.listObject;

												if($scope.dietIssueList==null){
                                                     growl.success('No Diet Order is available to issue.',
                                                        {
                                                            title : 'Success!'
                                                        });
												}
												console.log("dietIssueList",$scope.dietIssueList);
												$rootScope.stopLoader();

											}
											$rootScope.stopLoader();
										});
								
							}
						 	$scope.dietList = [];
						   
					        $scope.selectDietIssue= function (order) {
					        	
					        	
					        		for (var i =0; i<order.mealDetails.dishList.length; i++) {
					        			/*for(var j=0; j<$scope.dietIssueList[i].mealDetails.dishList.length; j++) {*/
					        				if (order.isChecked) {
					        					$scope.dietList.push(order.mealDetails.dishList[i].orderId);
					        			
					        				} else {
					        					  var index = null;
												  $scope.dietList.findIndex(x => x.orderId==order.mealDetails.dishList[i].orderId);
												  $scope.dietList.splice(index, 1);
												  
												  
					        				}
					        				
					        				console.log($scope.dietList);
					        				
					        			//}
					        		}
								
					        };
					
							
							
						  // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDietOrder();
							}
							
							$scope.showDishListForPatient = function(info) {
								$scope.dishListForPatient = info
							}
							
							
							
							
							/*$scope.dietIssueObj = {
									orderId : $scope.orderId,
								
								};*/
							$scope.saveDietOrder = function(dietOrder) {
								$scope.$broadcast('show-errors-check-validity');

								$rootScope.startLoader();
								if ($scope.dietOrderForm.$valid && ($scope.dietList != undefined && $scope.dietList.length > 0)) {
									var URI = BASE_URL + ROOT_URL
									+ DIETISSUE;
									
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService
												.serviceAction("POST",URI,$scope.dietList)
												.then(function(response) {
															if (response.data.status == "success") {
															    $scope.datepicker = new Date();
															    $scope.selectedFloorId = "";
															    $scope.selectedMealId = "";
																$scope.getDietIssueList();
																$rootScope.stopLoader();
																growl.success('Diet Order added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
															    $rootScope.stopLoader();
																growl.error(
																				'Diet Order already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope.dietList = [];
														});
										}
									}else{
									    $rootScope.stopLoader();
										growl.warning(
												'Please select Diet Issue...!!',
												{
													title : 'Warning!'
												});
									}
								}

							
							
							
							

							$scope.updatedDietObj = {};
							$scope.updateDietOrderStatus = function(order) {

								$scope.dietList = [];
								for (var i =0; i<order.mealDetails.dishList.length; i++) {
				        		    $scope.dietList.push(order.mealDetails.dishList[i].orderId);
								}
								var updatedDietObj = {
								    status : order.mealDetails.status,
								    patientId : order.patientId,
                                    mealId : order.mealDetails.mealId,
                                    dietList : $scope.dietList
								}
								
								var URI = BASE_URL + ROOT_URL
										+ DIETISSUELISTUPDATESTATUS;
								GenericService
										.serviceAction("PUT", URI, updatedDietObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.getDietIssueList();
														$scope.updatedDietList = [];
														growl.success(
																		'Diet Order Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
													    $scope.updatedDietList = [];
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							
							$scope.init();
						
						}]);

