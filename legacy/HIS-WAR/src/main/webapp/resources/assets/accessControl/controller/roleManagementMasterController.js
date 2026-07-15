/**
 * 
 */

angular.module('myApp')
		.controller("roleManagementMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','PaginationService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,PaginationService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
			$state.go('login');
			return;
			}

			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
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
			 
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						roleId:""
					};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.getActiveRoleList = function(){
				
					var data= {
							 	organisationId:$scope.organisationId,
							 	unitId:$scope.unitId
							};
			
					var URI1 = BASE_URL + ROOT_URL +"/api/global/getActiveUserRoleMasterList"; 
					$rootScope.startLoader();
					GenericService.serviceAction("POST",URI1,data).then(function(response){
						if(response.data.status == "success"){	
							
							$scope.pagination.data=[];
							
							$scope.commonList = response.data.listObject;
							
							 if($scope.commonList.length > 0){            
		                         PaginationService.searchData($scope.commonList, $scope.pagination);
		                       }
							console.log("commonList",$scope.commonList);
							$rootScope.stopLoader();
						}
					});
						
				}
			$scope.getActiveRoleList();
			
			// for getting user list by role id
			$scope.getUserDetails = function(info){
				$scope.userList=[];
				var data= {
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							roleId:info.roleId
						};
			console.log("userDetails",info);
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserListByRoleId"; 
					$rootScope.startLoader();
					GenericService.serviceAction("POST",URI1,data).then(function(response){
						if(response.data.status == "success"){											
							$scope.userList = response.data.listObject;
							console.log("userList",$scope.userList);
							$rootScope.stopLoader();
						}
					});
						
				}

			$scope.updateUserStatus = function(Id){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					
					$rootScope.startLoader();
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:'I',
							userRoleMpprId:Id
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateUserRoleMapperStatus";
					console.log("update status",data);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
			$scope.updateRoleStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.roleId=Id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateUserRoleMasterStatus";
					console.log("update status",$scope.common); //return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
}]);