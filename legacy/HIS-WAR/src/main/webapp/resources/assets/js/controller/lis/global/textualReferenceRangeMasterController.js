/**
 * 
 */

angular.module('myApp')
		.controller("textualReferenceRangeMasterController",
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
			
			
			//$scope.orgId = 1;
			//$scope.orgUnitId = 1;
			//$scope.createdBy =1 ;
			//$scope.updatedBy =1 ;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}			
			
			$scope.orgId = cookieObject.organizationId;
			$scope.orgUnitId = cookieObject.unitId;		
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
					textualRangeName:"",
					textualRangeCode:"",
					textualRangeId:"",
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
								orgUnitId:unitId
							};
				
				var data2 = {
						      orgId:orgId,
						      orgUnitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/LIS/global/getTextualRangeMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/LIS/global/getTextualRangeMasterCount"; 
					
				
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
						orgUnitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/LIS/global/getTextualRangeMasterList"; 
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
					$scope.$broadcast('show-errors-check-validity');
					
					$scope.common.createdBy=$scope.createdBy;
					$scope.common.updatedBy=$scope.updatedBy;
					$scope.common.status='A';
					$scope.common.orgUnitId=$scope.orgUnitId;
					$scope.common.orgId=$scope.orgId;
					if ($scope.textualRefRangeMasterForm.$valid) {
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;
						} else {
							$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/LIS/global/saveTextualRangeMaster"; 
						console.log("Save-URI-",URI);
						console.log(" save OBJ",$scope.common); 
					
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						$rootScope.stopLoader();
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						} else if(response.data.status == 'error')
						{
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
					var URI = BASE_URL + ROOT_URL +"/LIS/global/getTextualRangeMasterById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							if(response.data.listObject[0].status == "I"){
								growl.warning("This entry is deactivated please active first then update.",
										{
											title : 'warning!'
										});
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.updateScope = false;
								return false;
							}else{
							$scope.common = response.data.listObject[0];
							}
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
					if($scope.common.status=='I'){
						growl.warning("Can Not Update Deactivated Records",
								{
									title : 'Warning..!'
								});
						return false;
					}
					var date = new Date();
					$scope.common.updatedBy=$scope.updatedBy;
					$scope.common.orgUnitId=$scope.orgUnitId;
					$scope.common.orgId=$scope.orgId;
					$scope.$broadcast('show-errors-check-validity');				
					var URI = BASE_URL + ROOT_URL + "/LIS/global/updateTextualRangeMaster"; 
					console.log("Update-URI-",URI);
					console.log("Update OBJ",$scope.common);
					$rootScope.startLoader(); 
					if ($scope.textualRefRangeMasterForm.$valid) {
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
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.status=status,
					$scope.common.textualRangeId=Id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/LIS/global/updateTextualRangeMasterStatus"; 
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