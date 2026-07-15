/**
 * 
 */

angular.module('myApp')
		.controller("doctorCategoryMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
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
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						doctorCategoryId:"",
						doctorCategoryCode:"",
						doctorCategoryDescription:"",
						
							
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
					      	organizationId:organizationId,
							offset:offset,
							noOfRecordsPerPage:noOfRecordsPerPage,
							
							};
				
				var data2 = {
						      organizationId:organizationId,
							};
				
				var URI1 = BASE_URL + ROOT_URL + GET_DOCTOR_CATEGORY_MASTER_LIST; 
				var URI2 = BASE_URL + ROOT_URL + GET_DOCTOR_CATEGORY_MASTER_COUNT;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("POST",URI2,data2).then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:organizationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,

						};
				
				var URI = BASE_URL + ROOT_URL +  GET_DOCTOR_CATEGORY_MASTER_LIST;
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
				});
			}

			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
				if (flag)
				{
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');
					var data={	
							
							doctorCategoryCode:$scope.common.doctorCategoryCode,
							doctorCategoryDescription:$scope.common.doctorCategoryDescription,
							organizationId:$scope.organizationId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						
						}
					if ($scope.doctorCateMaasterForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + SAVE_DOCTOR_CATEGORY_MASTER;
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					}
					$rootScope.stopLoader();
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + GET_DOCTOR_CATEGORY_MASTER_BY_ID +S+ id +S+ $scope.organizationId ;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.doctorCategoryId=response.data.listObject[0].doctorCategoryId;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$scope.$broadcast('show-errors-check-validity');
					$rootScope.startLoader();

					var data={	
								doctorCategoryId:$scope.common.doctorCategoryId,
								doctorCategoryCode:$scope.common.doctorCategoryCode,
								doctorCategoryDescription:$scope.common.doctorCategoryDescription,
								organizationId:$scope.organizationId,
								
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt
							
						}
					
					if ($scope.doctorCateMaasterForm.$valid) {
					var URI = BASE_URL + ROOT_URL + UPDATE_DOCTOR_CATEGORY_MASTER;
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.doctorCategoryId=id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_DOCTOR_CATEGORY_MASTER_STATUS;
					console.log("update status",$scope.common);
					//return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
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
			

			/**Crud Operations End**/
}]);