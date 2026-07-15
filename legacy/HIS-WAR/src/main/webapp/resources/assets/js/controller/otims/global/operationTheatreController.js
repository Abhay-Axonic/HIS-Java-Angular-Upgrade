/**
 * 
 */

angular.module('myApp')
		.controller("operationTheatreController",
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
					theatreDesc:"",
					theatreName:"",
					theatreCode:"",
					theatreTypeId:"",
					operationTheatreId:"",
					storeId:""
				};
				console.log("Common-init",$scope.common);
				// For Get store List 
				   var URI =  BASE_URL + ROOT_URL+"/api/global/user/stores"+"/"+$scope.orgId + '/'+$scope.orgUnitId+"/"+$scope.userId;
					  	var data = {	  			
					  	}
				GenericService.serviceAction("GET", URI, data)
						.then(function(response) {
									$scope.storeList = [];													
									$scope.storeList = response.data;
									console.log("store list",$scope.storeList);
																
								});
				
			}
			$scope.initMaster();				
			//get active table type List
			$scope.getActiveTheatreTypeList=function(){
					$scope.theatreTypeList=[];
					var data = {
							      orgId:$scope.orgId,
							      unitId:$scope.orgUnitId
							};
				
				var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTheatreTypeList";
				
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){								
						$scope.theatreTypeList = response.data.listObject;
						console.log("theatreTypeList",$scope.theatreTypeList);
					}
				});
			}
			$scope.getActiveTheatreTypeList();
			
			
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
				
				var URI1 = BASE_URL + ROOT_URL +"/api/otims/getOperationTheatreList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreCount";
					
				
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
				
				var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreList";
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
					$scope.$broadcast('show-errors-check-validity');
					var data={
								theatreDesc:$scope.common.theatreDesc,
								theatreTypeId:$scope.common.theatreTypeId,
								theatreCode:$scope.common.theatreCode,
								theatreName:$scope.common.theatreName,
								updatedBy:$scope.common.updatedBy,
								createdDate:$scope.dt,
								status:'A',
								unitId:$scope.orgUnitId,
								orgId:$scope.orgId,
								updatedDate:$scope.dt,
								storeId:$scope.common.storeId,
								createdBy:$scope.createdBy, 
								updatedBy:$scope.updatedBy
							};
					if ($scope.operation_Theatreform.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/otims/saveOperationTheatre";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",$scope.common); 
					
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
					var URI = BASE_URL + ROOT_URL +"/api/otims/getOperationTheatreById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.theatreTypeId = response.data.listObject[0].theatreTypeId.toString();
							$scope.common.storeId = response.data.listObject[0].storeId.toString();
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
								operationTheatreId:$scope.common.operationTheatreId,
								theatreDesc:$scope.common.theatreDesc,
								theatreTypeId:$scope.common.theatreTypeId,
								theatreCode:$scope.common.theatreCode,
								theatreName:$scope.common.theatreName,
								updatedBy:$scope.common.updatedBy,
								createdDate:$scope.dt,
								status:'A',
								unitId:$scope.orgUnitId,
								orgId:$scope.orgId,
								updatedDate:$scope.dt,
								storeId:$scope.common.storeId
						};			
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateOperationTheatre";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",$scope.common);
					$rootScope.startLoader(); 
//				    if ($scope.operation_Theatreform.$valid) { 
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
				//	}
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
					$scope.common.operationTheatreId=Id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateOperationTheatreStatus";
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