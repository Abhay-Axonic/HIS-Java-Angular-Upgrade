angular
		.module('myApp')
		.controller(
				'couponTemplateMasterController',
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
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,PaginationService) {
							
							$(".selectedPageName").text("Coupon Template Master")
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
							$scope.searchKeyword = null;
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
					
						
						$scope.getActivePatientTypeList = function()
						{
								 var data={

											"organizationId":$scope.organizationId,
											"unitId":$scope.unitId
								 }
								
							var URI = BASE_URL + ROOT_URL +"/api/billing/coupon/getCouponTemplateMaster";
						GenericService
							.serviceAction("POST", URI,data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										if (response.data.status == "success"){
											
											 $scope.pagination.data=[];
											$scope.couponList = response.data.listObject;
											
											
											 if($scope.couponList.length > 0){            
						                         PaginationService.searchData($scope.couponList, $scope.pagination);
						                       }
											console.log("list",$scope.couponList);
										}
										
									});
							
						}
						
							$scope.updateStatus = function(couponTemplateMasterId,status)
							{
									 var data={
												"couponTemplateMasterId":couponTemplateMasterId,
												"status":status
									 }
									
								var URI = BASE_URL + ROOT_URL +"/api/billing/coupon/updateStatus";
							GenericService.serviceAction("POST", URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											if (response.data.status == "success"){
												growl.success(" Status Updated Successfully..!");
											}
											
										});
								
							}
							
							$scope.couponTemplateDetailsList = [];
							$scope.getcouponTemplateDetailsById = function(couponTemplateMasterId)
							{
									 var data={
												"couponTemplateMasterId":couponTemplateMasterId
									 }
									
								var URI = BASE_URL + ROOT_URL +"/api/billing/coupon/getCouponTemplateDetails";
							GenericService .serviceAction("POST", URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											if (response.data.status == "success"){
												$scope.couponTemplateDetailsList  = response.data.object.couponTemplateDetailsDtoList;
												$("#couponTemplateDetails").modal("show");
											}
											
										});
								
							}
							
							
							
						$scope.getActivePatientTypeList();
						
						$scope.showUpdateBtn = function(info){
							 $state.go('couponTemplate',
										{
								 			searchObj  : info
										  
										});
						}
						
						$scope.NavigateToAddNew = function(){
							
							  $state.go('couponTemplate');
							
						}
					
						
			}]);
							