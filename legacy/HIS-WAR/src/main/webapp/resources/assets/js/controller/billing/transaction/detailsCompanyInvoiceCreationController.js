angular.module("myApp").controller("detailsCompanyInvoiceCreationController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$stateParams','growl','servicesFactory','itemFactory',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$stateParams,growl,servicesFactory,itemFactory){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = 1;//cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		/**Redirect Page Code*/
		if(angular.equals({},$stateParams.detailsCompanyInvoiceList))
			{
			/*$state.go('microscopicExamination');*/
			$state.go('companyInvoiceCreation');
		} else 
		{
			$scope.detailsCompanyInvoiceCreationList=[];
			$scope.detailsCompanyInvoiceCreationList  = $stateParams.detailsCompanyInvoiceList;
			
		}
		
	}catch(e){
		console.log(e.message);
	}
}]);	