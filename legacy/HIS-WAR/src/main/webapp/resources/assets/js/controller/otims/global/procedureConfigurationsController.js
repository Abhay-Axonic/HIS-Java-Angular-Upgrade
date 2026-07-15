angular
		.module('myApp')
		.controller(
				'procedureConfigurationsController',
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
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PaginationService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
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
							
							
							$scope.procedureConfigs  = [];
							$scope.getProcedureConfList = function(){
								var URI = BASE_URL + ROOT_URL + "/api/otims/getProcedureConfigList/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										
										 $scope.pagination.data=[];
										$scope.procedureConfigs = response.data.listObject;
										
										if($scope.procedureConfigs.length > 0){            
					                         PaginationService.searchData($scope.procedureConfigs, $scope.pagination);
					                       }
									console.log("$scope.procedureConfigs",$scope.procedureConfigs);
								});
							}	
							
							$scope.getProcedureConfList();
							
							$scope.changeStatus = function(id,status){
								var URI = BASE_URL + ROOT_URL + "/api/procedure/config/status/"+id+"/"+status;
								$rootScope.startLoader();
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									$rootScope.stopLoader();
								});
							}
							
							$scope.editProcConf = function(procConf){
								$state.go('procedureTemplate',
										{
											commonObj: procConf
										});
							}
							
							$scope.viewProcConf = function(procConf){
								
								$state.go('procedureTemplate',
										{
											commonObj: procConf,
											infoObj : {isView:true}
										});
							}
			}]);
							