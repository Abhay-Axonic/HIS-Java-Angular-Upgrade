angular
		.module('myApp')
		.controller(
				'dietAcceptanceController',
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
								
								
								//$scope.getDietAcceptanceList();
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								
								$scope.selectedOrderObj = {"accepted" : false}

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
								
															

								var data = {};

								
								//Start : Getting initial diet acceptance info on current date
								
								$scope.currDate = new Date();
								
								var date ='';
								
								date ="'" +("0" + $scope.currDate.getDate()).slice(-2) +'-'+("0" + ($scope.currDate.getMonth()+1)).slice(-2)+'-'+$scope.currDate.getFullYear()+"'";
								
								var dietAcceptUrl = BASE_URL + ROOT_URL + GETINITIALDIETACCEPTLIST + '?dietDate=' + date + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var dietAcceptUrl = BASE_URL + ROOT_URL + GETINITIALDIETACCEPTLIST + '?dietDate=' + date;*/
								
								GenericService
								.serviceAction("GET", dietAcceptUrl, data)
								.then(
										function(response) {
											$scope.dietAcceptList = [];

											if (response.data.status == "success") {
												$scope.dietAcceptList = response.data.listObject;
												if($scope.dietAcceptList==null){
                                                     growl.success('No Diet Order is available to acceptance.',
                                                        {
                                                            title : 'Success!'
                                                        });
                                                }
												console.log("dietAcceptList",$scope.dietAcceptList);

											}
										});
								
								
								//End
								
						
					
								// For Get Meal Master Preference from ADT List Using Offset
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
							
							
														
							
							//For Get Diet Acceptance List
					
						$scope.getDietAcceptanceList = function() {
							data = {};
						    $rootScope.startLoader();
							var date ='';
							
							date ="'" +("0" + $scope.datepicker.getDate()).slice(-2) +'-'+("0" + ($scope.datepicker.getMonth()+1)).slice(-2)+'-'+$scope.datepicker.getFullYear()+"'";
							
							/*var dietAcceptUrl = BASE_URL + ROOT_URL 
							 + DIETACCPETANCE + '?dietDate=' + date;*/
							
							var dietAcceptUrl = BASE_URL + ROOT_URL 
							 + DIETACCPETANCE + '?dietDate=' + date + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							
							if($scope.selectedFloorId != undefined )
								{
								dietAcceptUrl = dietAcceptUrl + '&floorId=' + $scope.selectedFloorId; 
								}
							if($scope.selectedWardListId != undefined )
								{
								dietAcceptUrl =  dietAcceptUrl + '&wardId=' + $scope.selectedWardListId;
								}
							
							if($scope.selectedMealId != undefined )
							{
								dietAcceptUrl =  dietAcceptUrl + '&mealId=' + $scope.selectedMealId;
							}
							
							if($scope.selectedDietStatus != undefined )
							{
								dietAcceptUrl =  dietAcceptUrl + '&transition=' + $scope.selectedDietStatus;
							}
							
														
							GenericService
							.serviceAction("GET", dietAcceptUrl, data)
							.then(function(response) {
										$scope.dietAcceptList = [];
										if (response.data.status == "success") {
											
											$scope.dietAcceptList = response.data.listObject;
											if($scope.dietAcceptList==null){
                                                 growl.success('No Diet Order is available to acceptance.',
                                                    {
                                                        title : 'Success!'
                                                    });
                                            }
                                            $rootScope.stopLoader();
											console.log("dietAcceptList",$scope.dietAcceptList);

										}
									});
							
						}
						
						
						
						
						$scope.init();
						
					
						
						$scope.patientDietOrderList = []
						
						$scope.onChangeChoice = function(patientDietOrder) {
						    if($scope.patientDietOrderList.length>0){
//                                $scope.patientDietOrderList[0].accepted=false;
                                $scope.patientDietOrderList = [];
                            }
							if($scope.patientDietOrderList.indexOf(patientDietOrder) == -1) {
								$scope.patientDietOrderList.push(patientDietOrder)
							}
						}
						
						$scope.onChangeRemark = function($event) {
							$scope.selectedOrderObj.remarks = $event.remark
						}
						
						$scope.dietList = [];
						$scope.selectDietIssue = function(order) {

			        		/*for (var i =0; i<order.mealDetails.dishList.length; i++) {*/
			        			
			        				if (order.isChecked) {
			        					order.accepted = accepted;
			        					order.remarks = remarks;
			        					$scope.dietList.push(order);
			        			
			        				} else {
			        					  var index = null;
										  $scope.dietList.findIndex(x => x.orderId==order.mealDetails.dishList[i].orderId);
										  $scope.dietList.splice(index, 1);	  
			        				}
			        				
			        				console.log($scope.dietList);
			        	//	}
						
						}
						
						$scope.isDisabled = function(info)
						{
							if(info.transition != 'Ready for Acceptance')
								{
								return true;
								}
							
								return false;
								
						}
						
						
						  // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDietAcceptance();
							}
							
							$scope.showDishListForPatient = function(info) {
								$scope.dishListForPatient = info
							}
							
							
							
							$scope.saveDietAcceptance = function() {
								$scope.$broadcast('show-errors-check-validity');
									
                                $rootScope.startLoader();
								if ($scope.dietAcceptanceForm.$valid && ($scope.patientDietOrderList != undefined && $scope.patientDietOrderList != null  
										&& $scope.patientDietOrderList.length > 0)) {
									var URI = BASE_URL + ROOT_URL
									+ DIETACCPETANCE;
									
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService
												.serviceAction("POST",URI,$scope.patientDietOrderList)
												.then(function(response) {
															if (response.data.status == "success") {
															    $rootScope.stopLoader();
															    angular.forEach($scope.patientDietOrderList,function(v,k){
                                                                    if(v.accepted== true){
                                                                        growl.success('Diet Accepted Successfully...!!',
                                                                                {
                                                                                    title : 'Success!'
                                                                                });
                                                                    }
                                                                    if(v.accepted== false){
                                                                        growl.success('Diet Rejected Successfully...!!',
                                                                                {
                                                                                    title : 'Success!'
                                                                                });
                                                                    }
                                                                });
																$scope.getDietAcceptanceList();
//
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
															$scope.patientDietOrderList = [];
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

							
						
						}]);

