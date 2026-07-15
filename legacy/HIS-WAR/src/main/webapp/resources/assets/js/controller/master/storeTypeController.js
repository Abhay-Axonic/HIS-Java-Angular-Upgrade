/**
 * 
 */

angular.module('myApp')
		.controller("storeTypeController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			$scope.initStoreTypeMaster = function() {
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;

				//** Variables for pagination *//*
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				$rootScope.loginpage = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup

				$scope.common = {
						storeTypeId:"",
						code:"",
						type:"",
						addedBy:$scope.userId,
						updatedBy:$scope.userId,
						organizationId:$scope.orgId,
						unitId:$scope.orgUnitId					
					};
				
			}
			$scope.initStoreTypeMaster();
				
						
			//**List And Pagination Start**//*
			$scope.setNoOfRecords = function() {
				$scope.initStoreTypeMasterList($scope.orgId,$scope.orgUnitId	,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initStoreTypeMasterList = function(orgId,unitId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data1 = {
						organizationId:orgId,
						unitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var data2 = {
						organizationId:orgId,
						unitId:unitId,
						};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getStoreTypeList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getStoreTypeCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();
					console.log("response1",response);
					$scope.commonList = response.data.listObject;
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
			
			
			$scope.getStoreTypeList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:orgId,
						unitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getStoreTypeList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
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
					$scope.getStoreTypeList($scope.orgId ,$scope.orgUnitId,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initStoreTypeMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
			
			//**List And Pagination End**//*
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			$scope.save = function(){
				try{
					
					
					$scope.$broadcast('show-errors-check-validity');
					var URI = BASE_URL + ROOT_URL + "/api/global/saveStoreType";
					console.log("OBJ",$scope.common);					
						if ($scope.storeTypeForm.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								$rootScope.startLoader();
								GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
									
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initStoreTypeMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									$scope.initStoreTypeMaster();
								});
							}
						
					}
							
				}catch(e){
					console.log("Exception-",e);
				}
			
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					$scope.common={
							storeTypeId:id,
							organizationId:$scope.orgId,
							unitId:$scope.orgUnitId		
					}
					
					var URI = BASE_URL + ROOT_URL + "/api/global/getStoreTypeById";
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							
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
					
													
					var URI = BASE_URL + ROOT_URL + "/api/global/updateStoreType";
					console.log("Update-URI-",URI);
					console.log("OBJ",$scope.common);
					$scope.common.addedBy=$scope.userId;
					$scope.common.updatedBy=$scope.userId;
					$scope.common.organizationId=$scope.orgId;
					$scope.common.unitId=$scope.orgUnitId;	
					if ($scope.storeTypeForm.$valid) {
						$rootScope.startLoader();	
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initStoreTypeMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$scope.initStoreTypeMaster();
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
					
					var obj = {
							storeTypeId:id,
							isActive:status,
							updatedBy:$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateStoreTypeStatus";
					
					GenericService.serviceAction("POST",URI,obj).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
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
			
			//**Crud Operations End**/
}]);