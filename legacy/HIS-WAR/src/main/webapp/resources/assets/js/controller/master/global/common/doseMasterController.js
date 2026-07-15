/**
 * 
 */

angular.module('myApp')
		.controller("doseMasterController",
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
			
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			
			$scope.initdoseMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						doseId:"",
						doseCode:"",
						doseDesc :""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initdoseMaster();			
			
			/**Crud Operations Start**/
			
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initDoseMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initDoseMasterList = function(orgId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organizationId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage
							};
				
				var data2 = {
								organizationId:orgId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/common/getDoseMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/common/getDoseMasterCount";
					
				
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
			
			$scope.getInspectionList = function(orgId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/common/getDoseMasterList";
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
					$scope.getInspectionList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initDoseMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			
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
					var doseObj = {
							doseCode 		:   $scope.common.doseCode,
							doseDesc 		:   $scope.common.doseDesc,
							createdBy		:	$scope.createdBy,
							updatedBy		:	$scope.updatedBy,
							createdDate		:	$scope.dt,
							status			:	'A',
							organizationId	:	$scope.orgId,
							updatedDate		:	$scope.dt
					}
					if ($scope.doseForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/common/saveDoseMaster";
					console.log("Save-URI-",URI);
					console.log("OBJ",doseObj);
					$rootScope.startLoader();
					GenericService.serviceAction("POST",URI,doseObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initdoseMaster();
							$scope.initDoseMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
					
					var URI = BASE_URL + ROOT_URL +"/api/common/getDoseMasterListById/"+id+"/"+$scope.orgId;
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
					$scope.$broadcast('show-errors-check-validity');
					var doseObj = {
							doseCode 		:  $scope.common.doseCode,
							doseDesc 		:  $scope.common.doseDesc,
							doseId 			:  $scope.common.doseId,
							updatedBy		:  $scope.updatedBy,
							updatedDate		:  $scope.dt,
							organizationId	:  $scope.orgId
					}		
					if ($scope.doseForm.$valid) {
					var URI = BASE_URL + ROOT_URL + "/api/common/updateDoseMaster";
					$rootScope.startLoader();
					console.log("Update-URI-",URI);
					console.log("OBJ",doseObj);
					
					GenericService.serviceAction("POST",URI,doseObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initdoseMaster();
							$scope.initDoseMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
					
					var doseObj = {
							doseId		 	: 	id,
							status			:	status,
							updatedDate		:	$scope.dt,
							updatedBy		:	$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/common/updateDoseMasterStatus";
					
					GenericService.serviceAction("POST",URI,doseObj).then(function(response){
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