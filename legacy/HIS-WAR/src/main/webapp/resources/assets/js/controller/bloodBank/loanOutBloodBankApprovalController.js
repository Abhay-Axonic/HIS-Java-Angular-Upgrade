angular
		.module('myApp')
		.controller(
				'loanOutBloodBankApprovalController',
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
							
							$scope.searchObj = {
									"unitId"          :$scope.unitId,
									"organizationId"  :$scope.organizationId,
									"bloodBagNo":"",
									"fromDate":"",
									"toDate":""
									
							}
							
						
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.searchKeyword = "";
				
							$scope.searchObj.toDate=new Date();
							$scope.searchObj.fromDate=new Date();
							
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
							$scope.searchList=[];
							$scope.searchDetails = function(){
								if(($scope.searchObj.fromDate=="") && ($scope.searchObj.toDate=="")){
									$scope.fDate=$scope.searchObj.fromDate;
									$scope.tDate=$scope.searchObj.toDate;
								}
								else
									{
									$scope.fDate = moment($scope.searchObj.fromDate).format('YYYY-MM-DD');
									$scope.tDate = moment($scope.searchObj.toDate).format('YYYY-MM-DD');
									}
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										fromDate:$scope.fDate,
										toDate:$scope.tDate	
								};
								$scope.searchList=[];
								$rootScope.startLoader();
								var URI1 = BASE_URL + ROOT_URL + "/api/bloodbag/loanOut/getLoanOutList"; 
								GenericService.serviceAction("POST",URI1,data).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.searchList = response.data.listObject;
									}
								});

							}
							$scope.searchDetails();
							
							
							$scope.getLoanOutDetails = function(info){

								$state.go('detailsLoanOutBloodBank',
										{
										loanOutDetailObj: info
										});
									
							}
							
			}]);
							