angular
.module('myApp')
.controller(
		"dayCareWorklistMasterController",
		[
			'$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'GenericService',
			'PagerService',
			'promiseFactory',
			'$sessionStorage',
			'growl',
			'BillingGenericService',
			function($scope, $rootScope, $state, $cookies,GenericService, PagerService, promiseFactory,$sessionStorage, growl, BillingGenericService) {
				var cookieObject = $cookies
				.getObject('cookieObject');
					if (cookieObject == undefined) {
						$state.go('login');
						return;
					}
				
					$scope.unitId = cookieObject.unitId;
					$scope.organizationId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					
					$rootScope.loginpage = true;
					$scope.updateScope = false;
					$scope.popUpFlag = true;
					
					 $scope.searchKeyword="";
					 $scope.isSetFirstTabLevel =1;
//					 $scope.isSetFirstTabLevel=1;
					 
				/*	 $scope.isSetFirstTabLevel = function(tabNum) {
							return $scope.firstTabLevel === tabNum;
							
						};
						*/
					 $scope.setFirstTabLevel = function(Tab){
						 if(Tab == 1){
							 $scope.isSetFirstTabLevel = 1;
							 $state.go("dayCareWorklist.dayCareNewAdmission")
						 }else{
							 $scope.isSetFirstTabLevel = 2;
							 $state.go("dayCareWorklist.dayCareAdmittedPatientList")
						 }
					 }
				
	
			} ]);