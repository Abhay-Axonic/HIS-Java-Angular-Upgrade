angular
		.module('myApp')
		.controller(
				'issueBloodBagController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.commonObj={
									
									"fromDate":"",
									"toDate":""
								}; 
							
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.searchKeyword = "";
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};
							$scope.commonObj.fromDate = new Date();
							$scope.commonObj.toDate = new Date();
							//search  
							$scope.search = function() {
								$scope.startLoader();
							
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										fromDate:moment($scope.commonObj.fromDate).format('YYYY-MM-DD'),
										toDate:moment($scope.commonObj.toDate).format('YYYY-MM-DD')
									//	requisitionDate:moment(new Date()).format('DD-MM-YYYY'),
								};
								var URI = BASE_URL + ROOT_URL + "/api/bloodbank/crossMatch/getCrossMatchRegisteredOnIssue";

								GenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													
													$scope.searchResultList = [];
													if (response.data.status == "success"){
														$scope.searchResultList = response.data.listObject;
														console.log("$scope.searchResultList",$scope.searchResultList);
														$scope.stopLoader();
													}
													else {
														$scope.stopLoader();
														growl
																.warning(
																		"No Result Found!",
																		{
																			title : ERROR_MSG
																		});
													}
												});
							}
							$scope.search();
							//getBloodComponentDetails
							$scope.getBloodComponentDetails = function(info){
								$state.go('detailsIssueBloodBag',
										{
											bloodComponentObj: info
										});
							}

						}]);
							