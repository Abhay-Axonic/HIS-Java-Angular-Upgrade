angular
		.module('myApp')
		.controller(
				'loanOutBloodBankController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.searchKeyword="";
							
							$scope.comObj={
									fromDate:""	,
									toDate:"",
									bloodBagNo:""
							};
							
							$scope.comObj.fromDate = new Date();
							$scope.comObj.toDate = new Date();
							
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
							
							//search Details 
							$scope.fDate="";
							$scope.tDate="";
							$scope.searchDetails = function()
							{
								$scope.startLoader();
								if(($scope.comObj.fromDate=="") && ($scope.comObj.toDate=="")){
									$scope.fDate=$scope.comObj.fromDate;
									$scope.tDate=$scope.comObj.toDate;
								}
								else
									{
									$scope.fDate = moment($scope.comObj.fromDate).format('YYYY-MM-DD');
									$scope.tDate = moment($scope.comObj.toDate).format('YYYY-MM-DD');
									}
								
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										fromDate:$scope.fDate,
										toDate:$scope.tDate,
								};
								var URI = BASE_URL + ROOT_URL + "/api/bloodbag/loanOut/getLoanOutList";

								GenericService.serviceAction(METHOD_POST, URI, data)
								.then(
										function(response) {
													$scope.searchDonorList = [];
													if (response.data.status == "success") {
														$scope.searchList = response.data.listObject;
														console.log("$scope.searchList",$scope.searchList);
														$scope.stopLoader();
													
													} else {
														$scope.stopLoader();
														growl.warning("No Result Found!",
																		{
																			title : ERROR_MSG
																		});
													}
												});
							}
							$scope.searchDetails();
							
							$scope.loanOutDetailsList = [];
							$scope.getLoanOutDetails = function(obj){
								$scope.startLoader();
								$scope.loanOutDetailsList = [];
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										bloodBagLoanOutId : obj.bloodBagLoanOutId,
								};
								var URI = BASE_URL + ROOT_URL + "/api/bloodbag/loanOut/getLoanOutDeatilsList";

								GenericService.serviceAction(METHOD_POST, URI, data)
										.then(function(response) {
													$scope.loanOutDetailsList = [];
													if (response.data.status == "success") {
														$scope.loanOutDetailsList = response.data.listObject;
														$scope.stopLoader();
													} else {
														$scope.stopLoader();
														growl.warning("No Result Found!",
																		{
																			title : ERROR_MSG
																		});
													}
												});
								$scope.stopLoader();
								
							}
							
			}]);
							