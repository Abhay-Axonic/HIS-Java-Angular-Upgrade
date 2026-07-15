/**
 * 
 */

angular.module('myApp')
		.controller("allergyTypeMasterController",
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
			
			$scope.initAllergyTypeMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						allergyTypeId:"",
						allergyTypeDesc:"",
						allergyTypeName :"",
						isGeneric : 'N'
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initAllergyTypeMaster();			
			
			/**Crud Operations Start**/
			
		/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			};
			
			$scope.initAllergyList = function(orgId,offset, noOfRecordsPerPage,unitId)
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
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getAllergyTypeMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getAllergyTypeMasterListCount";
					
				
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
			
			$scope.getAllergyList = function(orgId,offset, noOfRecordsPerPage,unitId) {
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
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getAllergyTypeMasterList";
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
					$scope.getAllergyList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
				}
			}

			$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			
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
					
					var allergyObj = {
							allergyTypeName :   $scope.common.allergyTypeName,
							allergyTypeDesc :   $scope.common.allergyTypeDesc,
							isGeneric 	:  $scope.common.isGeneric,
							createdBy		:	$scope.createdBy,
							updatedBy		:	$scope.updatedBy,
							createdDate		:	$scope.dt,
							status			:	'A',
							organizationId	:	$scope.orgId,
							unitId			:	$scope.orgUnitId,
							updatedDate		:	$scope.dt
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/saveAllergyTypeMaster";
					console.log("Save-URI-",URI);
					console.log("OBJ",allergyObj);
					 $scope.$broadcast('show-errors-check-validity');
					 if ($scope.allergyTypeForm.$valid) {
					 if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
					GenericService.serviceAction("POST",URI,allergyObj).then(function(response){
						$rootScope.startLoader();
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initAllergyTypeMaster();
							$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					
					var URI = BASE_URL + ROOT_URL +"/api/global/getAllergyTypeMasterListById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
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
					
					var allergyObj = {
							allergyTypeName :  $scope.common.allergyTypeName,
							allergyTypeDesc :  $scope.common.allergyTypeDesc,
							allergyTypeId 	:  $scope.common.allergyTypeId,
							isGeneric 	:  $scope.common.isGeneric,
							organizationId	:	$scope.orgId,
							unitId			:	$scope.orgUnitId,
							updatedBy		:  $scope.updatedBy,
							updatedDate		:  $scope.dt
					}			
					 $scope.$broadcast('show-errors-check-validity');
					 if ($scope.allergyTypeForm.$valid) {
						 $rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateAllergyTypeMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",allergyObj);
					
					GenericService.serviceAction("POST",URI,allergyObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initAllergyTypeMaster();
							$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					
					var allergyObj = {
							allergyTypeId	: 	id,
							status			:	status,
							updatedDate		:	$scope.dt,
							updatedBy		:	$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateAllergyTypeMasterStatus";
					
					GenericService.serviceAction("POST",URI,allergyObj).then(function(response){
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