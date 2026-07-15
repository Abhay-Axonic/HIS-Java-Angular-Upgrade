angular
		.module('myApp')
		.controller(
				'dietOrderController',
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
								
								
								$scope.getDietOrderList();
								
								
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
								$scope.patientListForSelectedDish = []
							
								
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
								
								
								
								
								
								$scope.dayWiseDietObj = $scope.dummyDayWiseDietObj;
								

								var data = {};

								
						
					
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
							
							var URI = BASE_URL + ROOT_URL
							+ ACTIVEMEALMASTER;
							
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
							
							
							
							
							
							//For Get Diet Order List
							
							$scope.getDietOrderList = function() {
								data = {};
							     $rootScope.startLoader();
								var date ='';
								
								date ="'" +("0" + $scope.datepicker.getFullYear() +'-'+("0" + ($scope.datepicker.getMonth()+1)).slice(-2)+'-'+  $scope.datepicker.getDate()).slice(-2) +"'";
								var date1 =moment($scope.datepicker).format("YYYY-MM-DD");

								/*var URI = BASE_URL + ROOT_URL 
								 + GETDIETORDERLIST + '?dietDate=' + date  + '&floorId=' + $scope.selectedFloorId + '&wardId=' + $scope.selectedWardListId + '&mealId=' + $scope.selectedMealId;*/
								
								/*var dietOrderUrl = BASE_URL + ROOT_URL 
								 + GETDIETORDERLIST + '?dietDate=' + date;*/
								
								var dietOrderUrl = BASE_URL + ROOT_URL 
										 + GETDIETORDERLIST + '?dietDate=' + date1 + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								
										 if($scope.selectedFloorId != undefined )
											{
											 dietOrderUrl = dietOrderUrl + '&floorId=' + $scope.selectedFloorId; 
											}
										if($scope.selectedWardListId != undefined )
											{
											dietOrderUrl =  dietOrderUrl + '&wardId=' + $scope.selectedWardListId;
											}
										
										if($scope.selectedMealId != undefined )
										{
											dietOrderUrl =  dietOrderUrl + '&mealId=' + $scope.selectedMealId;
										}		 
										 
										 
										 
								GenericService
								.serviceAction("GET", dietOrderUrl, data)
								.then(
										function(response) {
											$scope.dietOrderList = [];
											if (response.data.status == "success") {
												$scope.dietOrderList = response.data.listObject;
												if($scope.dietOrderList==null){
                                                     growl.success('No Diet Order is available.',
                                                        {
                                                            title : 'Success!'
                                                        });
                                                }
												console
														.log(
																"dietOrderList",
																$scope.dietOrderList);
																$rootScope.stopLoader();

											}
										});
								
							}
							
							$scope.init();
							
				           // Function For Info of Diet Order Details
							/*$scope.getDietOrderedDetails = function(dishId) {
								var data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETDIETORDERDETAILS + '?dishId=' + dishId;
								// For Get Diet Order Details List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.dietOrderDetailsList = [];

													if (response.data.status == "success") {
														$scope.dietOrderDetailsList = response.data.listObject;
														console
																.log(
																		"dietOrderDetailsList",
																		$scope.dietOrderDetailsList);

													}
												});
							}*/
							
			
							
							
						
							
								  
								  // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveMealMaster();
							}
							
							$scope.showPatientListForDish = function(patientList) {
								$scope.patientListForSelectedDish = patientList
							}
							
			
						
						}]);

