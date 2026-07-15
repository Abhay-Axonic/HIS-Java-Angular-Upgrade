/**
 * 
 */

angular.module('myApp')
		.controller("visitTypeMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.LISDynamicLabel = "Visit Type Master";
	
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
			
			
			$scope.initVisitTypeMaster = function(){
				
				$scope.common = {
					id:"",
					code:"",
					desc:""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initVisitTypeMaster();
			
			// code for display ing name on top side bar for common page only start
				$(".selectedPageName").text(" Visit Type Master ");
				$state.current.title="Visit Type Master";
				$(".sidebar-nav li").each(function() {
					$(this).find("a").parents("li").removeClass("active");
				});
				$('a[href$="visitType"]').parents("li").addClass("active");
			// code for display ing name on top side bar for common page only End
//			alert("hello");
			/**Crud Operations Start**/
			
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initVisitTypeMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initVisitTypeMasterList = function(orgId,offset, noOfRecordsPerPage)
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
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/visitTypeMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/visitTypeMasterListCount";
					
				
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
			
			/*$scope.initVisitTypeMasterList = function(orgId,
					offset, noOfRecordsPerPage) {
				var data = "";
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
				var URI = BASE_URL + ROOT_URL
						+ "/api/global/visitTypeMasterList";
				var URI1 = BASE_URL + ROOT_URL
						+ "/api/global/visitTypeMasterListCount";

				var data = {
					organizationId : orgId,
					offset : offset,
					noOfRecordsPerPage : noOfRecordsPerPage
				}
				console.log("data", data);

				var data1 = {
					organizationId : $scope.organizationId
				}

				// For Get designation List Using Offset
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									$scope.commonList = [];

									if (response.data.status == "success") {
										$scope.commonList = response.data.listObject;
										console
												.log(
														"commonList",
														$scope.commonList);
									}
								});

				// For Count designation List
				GenericService
						.serviceAction("POST", URI1, data1)
						.then(
								function(response) {
									if (response.data.status == "success") {
										console
												.log(response.data);
										$scope.commonListCount = response.data.object;
										$scope
												.setPage(1,
														false);

									}
								});
			}*/
			
			
			$scope.PriorityMasterList = function(orgId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + GETVISITTYPELIST;
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
					$scope.PriorityMasterList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initVisitTypeMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			$scope.save = function(){
				try{
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitTypeObj = {
							visitTypeCode : $scope.common.code,
							visitTypeName : $scope.common.desc,
							createdBy:$scope.createdBy,
							updatedBy:$scope.createdBy,
							createdDate:$scope.dt,
							status:'A',
							organizationId:$scope.orgId,
							updatedDate:$scope.dt
					}
					var URI = BASE_URL + ROOT_URL + SAVEVISITTYPE;
					console.log("Save-URI-",URI);
					console.log("OBJ",visitTypeObj);
					
					GenericService.serviceAction("POST",URI,visitTypeObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initVisitTypeMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
					console.log("Exception-",e);
				}
			}
			
			$scope.showUpdateBtn = function(visitId){
				try{
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL + GETVISITTYPEBYID+"/visitTypeId/"+visitId+"/orgId/"+$scope.orgId;
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
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitTypebj = {
							visitTypeCode : $scope.common.code,
							visitTypeName : $scope.common.desc,
							visitTypeId : $scope.common.id,
							updatedBy:$scope.createdBy,
							updatedDate:$scope.dt
					}					
					var URI = BASE_URL + ROOT_URL + UPDATEVISITTYPE;
					console.log("Update-URI-",URI);
					console.log("OBJ",visitTypebj);
					
					GenericService.serviceAction("POST",URI,visitTypebj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initVisitTypeMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(visitId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitObj = {
							visitTypeId:visitId,
							status:status,
							updatedDate:$scope.dt,
							updatedBy:$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + UPDATEVISITTYPESTATUS;
					
					GenericService.serviceAction("POST",URI,visitObj).then(function(response){
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