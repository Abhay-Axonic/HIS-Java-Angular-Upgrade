/**
 * 
 */

angular.module('myApp')
		.controller("gradeCreationMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
	
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
					contractGradeMasterId:"",
					code:"",
					gradeDesc:""
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
				
				var URI1 = BASE_URL + ROOT_URL +"/api/billing/getContractGradeMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/billing/getContractGradeMasterCount";
					
				
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
				
				var URI = BASE_URL + ROOT_URL + "/api/billing/getContractGradeMasterList";
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
					
					if($scope.popUpFlag==false)
					{
					  $rootScope.startLoader();
					}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');
					var Obj = {
							code : $scope.common.code,
							gradeDesc : $scope.common.gradeDesc,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							status:'A',
							unitId:$scope.orgUnitId,
							orgId:$scope.orgId,
							updatedDate:$scope.dt
					}
					if ($scope.gradeCreationMasterform.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/billing/saveContractGradeMaster";
					console.log("Save-URI-",URI);
					console.log("OBJ",Obj);
					
					GenericService.serviceAction("POST",URI,Obj).then(function(response){
						
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
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/billing/getContractGradeMasterById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
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
					var obj = {
							code : $scope.common.code,
							gradeDesc : $scope.common.gradeDesc,
							contractGradeMasterId : $scope.common.contractGradeMasterId,
							orgId:$scope.orgId,
							unitId:$scope.orgUnitId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt
					}					
					var URI = BASE_URL + ROOT_URL + "/api/billing/updateContractGradeMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",obj);
					$rootScope.startLoader();
					if ($scope.gradeCreationMasterform.$valid) {
					GenericService.serviceAction("POST",URI,obj).then(function(response){
						
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
			
			$scope.updateStatus = function(contractGradeMasterId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitObj = {
							contractGradeMasterId:contractGradeMasterId,
							status:status,
							updatedDate:$scope.dt,
							updatedBy:$scope.updatedBy
					}
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/billing/updateContractGradeMasterStatus";
					
					GenericService.serviceAction("POST",URI,visitObj).then(function(response){
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