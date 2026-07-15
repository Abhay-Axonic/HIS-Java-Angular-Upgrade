/**
 * 
 */

angular.module('myApp')
		.controller("operationTheatreTypeController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
	
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
			
			$scope.orgUnitId = cookieObject.unitId;
			$scope.orgId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
					theatreTypeDesc:"",
					theatreTypeName:"",
					theatreTypeCode:"",
					operationTheatreTypeId:""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();				

			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			};
			
			$scope.initMasterList = function(orgId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								orgId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId
							};
				
				var data2 = {
						      orgId:orgId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/otims/getOperationTheatreTypeList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreTypeCount";
					
				
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
			
			$scope.masterList = function(orgId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						orgId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreTypeList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.orgUnitId);
				if (flag)
				{
					$scope.masterList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
				}
			}

			$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
										
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');  //dilip
					
					var data={
							theatreTypeCode:$scope.common.theatreTypeCode,
							theatreTypeDesc:$scope.common.theatreTypeDesc,
							theatreTypeName:$scope.common.theatreTypeName,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							status:'A',
							unitId:$scope.orgUnitId,
							orgId:$scope.orgId,
							updatedDate:$scope.dt
					};
					if ($scope.operationTheatreTypeform.$valid) { //dilip
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/otims/saveOperationTheatreType";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
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
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}
		
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/otims/getOperationTheatreTypeById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
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
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					/*$scope.$broadcast('show-errors-check-validity');*/
					
					var data={
							operationTheatreTypeId:$scope.common.operationTheatreTypeId,
							theatreTypeCode:$scope.common.theatreTypeCode,
							theatreTypeDesc:$scope.common.theatreTypeDesc,
							theatreTypeName:$scope.common.theatreTypeName,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							status:'A',
							unitId:$scope.orgUnitId,
							orgId:$scope.orgId,
							updatedDate:$scope.dt
							
					};
				
							console.log("updatedData", data);		
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateOperationTheatreType";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",$scope.common);
					$rootScope.startLoader(); 
					/*if ($scope.operationTheatreTypeform.$valid) {*/
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							/*$scope.$broadcast('show-errors-reset');*/
							$scope.initMaster();
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					/*}*/
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.operationTheatreTypeId=Id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateOperationTheatreTypeStatus";
					console.log("update status",$scope.common);
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
			
			/**Crud Operations End**/
}]);