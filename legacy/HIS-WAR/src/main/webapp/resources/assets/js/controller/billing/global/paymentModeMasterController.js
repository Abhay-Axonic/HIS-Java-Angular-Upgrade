/**
 * 
 */

angular.module('myApp')
		.controller("paymentModeMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.LISDynamicLabel = "Payment Mode";
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.orgId = 1;
			$scope.orgUnitId = 1;
			$scope.createdBy =1 ;
			$scope.updatedBy =1 ;
			
			
			$scope.initpaymentModeMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
					id:"",
					code:"",
					desc:""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initpaymentModeMaster();
			// code for display ing name on top side bar for common page only start
			$(".selectedPageName").text("Payment Mode");
			$state.current.title="Payment Mode";
			$(".sidebar-nav li").each(function() {
				$(this).find("a").parents("li").removeClass("active");
			});
			$('a[href$="paymentMode"]').parents("li").addClass("active");
		// code for display ing name on top side bar for common page only End
			
//			alert("hello"); return false;
			/**Crud Operations Start**/
			
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initPaymentMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initPaymentMasterList = function(orgId,offset, noOfRecordsPerPage)
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
				
				var URI1 = BASE_URL + ROOT_URL +GETPAYMENTMODEMASTERLIST; 
				var URI2 = BASE_URL + ROOT_URL + GETPAYMENTMODEMASTERCOUNT;
					
				
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
				
				var URI = BASE_URL + ROOT_URL + GETPAYMENTMODEMASTERLIST;
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

			$scope.initPaymentMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			$scope.save = function(){
				try{
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitTypeObj = {
							paymentEntitlementCode : $scope.common.code,
							paymentEntitlementDesc : $scope.common.desc,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							status:'A',
							organizationId:$scope.orgId,
							updateDate:$scope.dt
					}
					var URI = BASE_URL + ROOT_URL + SAVEPAYMENTMODEMASTER;
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
							$scope.initpaymentModeMaster();
							$scope.initPaymentMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
			
			$scope.showUpdateBtn = function(paymentModeId){
				try{
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL + GETPAYMENTMODEMASTERBYID+"/"+paymentModeId+"/"+$scope.orgId;
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
							paymentEntitlementCode : $scope.common.code,
							paymentEntitlementDesc : $scope.common.desc,
							paymentEntitlementId : $scope.common.id,
							organizationId:$scope.orgId,
							updatedBy:$scope.updatedBy,
							updateDate:$scope.dt
					}					
					var URI = BASE_URL + ROOT_URL + UPDATEPAYMENTMODEMASTER;
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
							$scope.initpaymentModeMaster();
							$scope.initPaymentMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
			
			$scope.updateStatus = function(paymentModeId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitObj = {
							paymentEntitlementId:paymentModeId,
							status:status,
							updateDate:$scope.dt,
							updatedBy:$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + UPDATEPAYMENTMODEMASTERSTATUS;
					
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