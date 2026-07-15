/**
 * 
 */

angular.module('myApp')
		.controller("wingMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
	
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
			
			$scope.popUpFlag = true;
			
			$scope.initWingMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						wingId:"",
						wingCode:"",
						wingDesc :""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initWingMaster();			
			
			/**Crud Operations Start**/
			
		/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initWingList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			};
			
			$scope.initWingList = function(orgId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organizationId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId
							};
				
				var data2 = {
								organizationId:orgId ,
								unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/adt/getWingMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/adt/getWingMasterListCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("LISt ",$scope.commonList);
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
			
			$scope.getWingList = function(orgId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/adt/getWingMasterList";
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
					$scope.getWingList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
				}
			}

			$scope.initWingList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			
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
					
					var wingObj = {
							wingCode		:   $scope.common.wingCode,
							wingDesc		:   $scope.common.wingDesc,
							createdBy		:	$scope.createdBy,
							updatedBy		:	$scope.updatedBy,
							createdDate		:	$scope.dt,
							status			:	'A',
							organizationId	:	$scope.orgId,
							unitId			:	$scope.orgUnitId,
							updatedDate		:	$scope.dt
					}
					var URI = BASE_URL + ROOT_URL + "/adt/saveWingMaster";
					console.log("Save-URI-",URI);
					console.log("OBJ",wingObj);
					 $scope.$broadcast('show-errors-check-validity');
					 if ($scope.wingForm.$valid) {
					 if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
					GenericService.serviceAction("POST",URI,wingObj).then(function(response){
						$rootScope.startLoader();
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initWingMaster();
							$scope.initWingList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL +"/adt/getWingMasterListById/"+id;
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
					
					var wingObj = {
							wingCode 		:  $scope.common.wingCode,
							wingDesc 		:  $scope.common.wingDesc,
							wingId 			:  $scope.common.wingId,
							organizationId	:	$scope.orgId,
							unitId			:	$scope.orgUnitId,
							updatedBy		:  $scope.updatedBy,
							updatedDate		:  $scope.dt
					}			
					 $scope.$broadcast('show-errors-check-validity');
					 if ($scope.wingForm.$valid) {
						 $rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/adt/updateWingMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",wingObj);
					
					GenericService.serviceAction("POST",URI,wingObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initWingMaster();
							$scope.initWingList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					
					var wingObj = {
							wingId			: 	id,
							status			:	status,
							updatedDate		:	$scope.dt,
							updatedBy		:	$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/adt/updateWingMasterStatus";
					
					GenericService.serviceAction("POST",URI,wingObj).then(function(response){
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
			
			/**Crud Operations End**/
}]);