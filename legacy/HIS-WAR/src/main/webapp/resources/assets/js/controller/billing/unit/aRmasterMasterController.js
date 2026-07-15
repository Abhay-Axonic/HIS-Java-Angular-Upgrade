/**
 * 
 */

angular.module('myApp')
		.controller("aRmasterMasterController",
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
			
			$scope.unitId = cookieObject.unitId;
			$scope.orgId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						aRCodeId:"",
						aRCode:"",
						aRCodeDesc:""
						
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();

			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
			};
			
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
															
				var URI1 = BASE_URL + ROOT_URL +"/global/billing/getARCodeMasterList/"+$scope.orgId+"/"+$scope.unitId+"/"+$scope.offset+"/"+$scope.noOfRecordsPerPage; 
				var URI2 = BASE_URL + ROOT_URL + "/global/billing/getARCodeMasterCount/"+$scope.orgId+"/"+$scope.unitId;
//					
				
				GenericService.serviceAction("GET",URI1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2,"").then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.masterList = function(offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var URI = BASE_URL + ROOT_URL + "/global/billing/getARCodeMasterList/"+$scope.orgId+"/"+$scope.unitId+"/"+$scope.offset+"/"+$scope.noOfRecordsPerPage;
				console.log("URI", URI);
				GenericService.serviceAction("GET", URI).then(function(response) {
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
				if (flag)
				{
					$scope.masterList($scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				
				try{
					
//					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
					$rootScope.startLoader();
					var data1={
							aRCode:$scope.common.aRCode,
							aRCodeDesc:$scope.common.aRCodeDesc,
							status:'A',
							createdDate:$scope.dt,
							createdBy:$scope.createdBy,
							unitId:$scope.unitId,
							orgId:$scope.orgId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt
													
					}
				
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/global/billing/saveARCodeMaster";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data1);   
					
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							return false;
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$rootScope.stopLoader();
					});
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
					var URI = BASE_URL + ROOT_URL +"/global/billing/getARCodeMasterById/"+id+"/"+$scope.orgId+"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.aRCodeId = response.data.listObject[0].aRCodeId.toString();
							console.log("edit id ",response.data.listObject[0]);
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
					$rootScope.startLoader();
					var data1={	
							aRCodeId:$scope.common.aRCodeId,
							aRCode:$scope.common.aRCode,
							aRCodeDesc:$scope.common.aRCodeDesc,
							unitId:$scope.unitId,
							orgId:$scope.orgId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt
						
						}
									
					var URI = BASE_URL + ROOT_URL + "/global/billing/updateARCodeMaster";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data1);
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
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
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');						
					$rootScope.startLoader();
					var data={
							
							status:status,
							aRCodeId:Id,
							unitId:$scope.unitId,
							orgId:$scope.orgId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt
					}
					var URI = BASE_URL + ROOT_URL + "/global/billing/updateARCodeMasterStatus";
					console.log("update status",data);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
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