/**
 * 
 */

angular.module('myApp')
		.controller("QMSkioskMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			$scope.initQMSMaster = function() {
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;

				/** Variables for pagination */
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
						qmsKioskMasterId:"",
						qmsCode:"",
						qmsDesc:"",
						createdBy:$scope.createdBy,
						updatedBy:$scope.updatedBy,
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId					
					};
				
			}
			$scope.initQMSMaster();
			
						
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initQMSkioskMasterList($scope.orgId,$scope.orgUnitId	,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initQMSkioskMasterList = function(orgId,unitId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data1 = {
						organisationId:orgId,
						unitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var data2 = {
						organisationId:orgId,
						unitId:unitId,
						};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/pharmacy/getQMSMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSMasterCount";
					
				
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
			
			
			$scope.qmsMasterList = function(orgId,unitId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:orgId,
						unitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSMasterList";
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
					$scope.qmsMasterList($scope.orgId ,$scope.orgUnitId,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initQMSkioskMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			$scope.save = function(){
				try{
					
					
					$scope.$broadcast('show-errors-check-validity');
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/saveQMSMaster";
					console.log("OBJ",$scope.common);					
						if ($scope.qmsForm.$valid) {

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
										$scope.initQMSkioskMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									$scope.initQMSMaster();
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
							qmsKioskMasterId:id,
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId		
					}
					
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSMasterById";
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
					
													
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateQMSMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",$scope.common);
					$scope.common.organisationId=$scope.orgId;
					$scope.common.unitId=$scope.orgUnitId;	
					if ($scope.qmsForm.$valid) {
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
							$scope.initQMSkioskMasterList($scope.orgId,$scope.orgUnitId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$scope.initQMSMaster();
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
							qmsKioskMasterId:id,
							status:status,
							updatedBy:$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateQMSMasterStatus";
					
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
			
			/**Crud Operations End**/
}]);