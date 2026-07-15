angular.module("myApp").controller("corporateStatementController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','reportService','PaginationService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,reportService,PaginationService){
	try{
$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		 if(cookieObjectAppConfig == undefined){
			 $state.go('login');
		  return;
		 }
		 else
		  {
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		  }
		 $scope.isConsolidatedBill=cookieObjectAppConfig.isConsolidatedBill;
		 
		 /**DatePicker Code  */
			
			$scope.fromDate = moment().startOf('day').toDate();
		    $scope.toDate = moment().startOf('day').toDate();
		    $scope.completed='N';
			$scope.pending = 'N';
			$scope.outsourced = 'N';
			$scope.isDisable=false;
			$scope.dateOptions =
			{
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate : "",
			};

			$scope.open = function() {
				$scope.popup.opened = true;
			};

			$scope.popup =
			{
				opened : false
			};
			
			$scope.popup1 =
			{
				opened : false
			};
			$scope.popup7 =
			{
				opened : false
			};
			
			$scope.open1 = function() 
			{
				$scope.popup1.opened = true;
			};
			$scope.open7 = function() 
			{
				$scope.popup7.opened = true;
			};

			 $scope.pagination = {
	                 currentPage: 1,
	                 totalPages: 0,
	                 pageSize: '10',
	                 search: '',
	                 searchData: PaginationService.searchData,
	                 setTotalPages: PaginationService.setTotalPages,
	                 getPagesList: PaginationService.getPagesList,
	                 setPage: PaginationService.setPage,
	                 setFirstPage: PaginationService.setFirstPage,
	                 setPreviousPage: PaginationService.setPreviousPage,
	                 setNextPage: PaginationService.setNextPage,
	                 setLastPage: PaginationService.setLastPage,
	                 sortBy: PaginationService.sortBy
	               }
				
			$scope.init = function(){
				$scope.getPaymentEntitlementList();
				$scope.getCompanyMasterList(3);//companyId 3
				$scope.companyInvoiceObj.paymentEntitlementId="";
				$scope.companyInvoiceObj.companyId="";
				$scope.detailsCompanyInvoiceCreationList=null;
				
			}
			 
			 
			 $scope.NavigateToDetail = function (item) {
		            $state.go('detailsCorporateStatement');
		        }
			 
		
	}catch(e){
		console.log(e.message);
	}
}]);	