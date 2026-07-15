/**
 * 
 */

angular.module('myApp')
		.controller("hospitalAccountMasterController",
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
			
			$scope.initHospitalAccountMaster = function(){
				
				$scope.common = {
						hospitalAccountId:"",				
						hospitalAccountCode:"",
						hospitalAccountDesc:"",
						isDefault:"N"
				};
				
				var data = {
						orgnizationId:$scope.orgId
						};
				
			}
			$scope.initHospitalAccountMaster();
			
			
//			alert("hello"); return false;
			/**Crud Operations Start**/
			
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initHospitalAccountMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initHospitalAccountMasterList = function(orgId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data1 = {
						orgnizationId:orgId,
						unitId:$scope.orgUnitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var data2 = {
						orgnizationId:orgId
						};
				
				var URI1 = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERLIST; 
				var URI2 = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERCOUNT;
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.hospitalAccountList = response.data.listObject;
					console.log("Hospital Account List",$scope.hospitalAccountList);
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
						unitId:$scope.orgUnitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERLIST;
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.hospitalAccountList = response.data.listObject;
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

			$scope.initHospitalAccountMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			
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
					
					
					
					 if ($scope.bankForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
						$rootScope.startLoader();
						
						var hospitalAccountObj = {
								hospitalAccountId : $scope.common.hospitalAccountId,
								hospitalAccountCode : $scope.common.hospitalAccountCode,
								hospitalAccountDesc : $scope.common.hospitalAccountDesc,
								isDefault : $scope.common.isDefault,
								createdBy:$scope.createdBy,
								updatedBy:$scope.updatedBy,
								createdDate:$scope.dt,
								status:'A',
								orgnizationId:$scope.orgId,
								unitId:$scope.orgUnitId,
								updatedDate:$scope.dt
						}
						
						var URI = BASE_URL + ROOT_URL + SAVEHOSPITALACCOUNTMASTER;
						console.log("Save-URI-",URI);
						console.log("OBJ",hospitalAccountObj);
						GenericService.serviceAction("POST",URI,hospitalAccountObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							return false;
							$scope.initHospitalAccountMaster();
							$scope.initHospitalAccountMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
			 }	
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.showUpdateBtn = function(hospitalAccountId){
				try{
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERBYID+"/"+hospitalAccountId+"/"+$scope.orgId+"/"+$scope.orgUnitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.object;
							console.log($scope.common);
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
					
					 if ($scope.bankForm.$valid) {
						 
						 $rootScope.startLoader();
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							$scope.$broadcast('show-errors-check-validity');
							var hospitalAccountObj = {
									hospitalAccountId : $scope.common.hospitalAccountId,
									hospitalAccountCode : $scope.common.hospitalAccountCode,
									hospitalAccountDesc : $scope.common.hospitalAccountDesc,
									isDefault : $scope.common.isDefault,
									updatedBy:$scope.updatedBy,
									status:'A',
									orgnizationId:$scope.orgId,
									unitId:$scope.orgUnitId,
									updatedDate:$scope.dt
							}
									
							var URI = BASE_URL + ROOT_URL + UPDATEHOSPITALACCOUNTMASTER;
							console.log("Update-URI-",URI);
							console.log("OBJ",hospitalAccountObj);
						 
					GenericService.serviceAction("POST",URI,hospitalAccountObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();							
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.initHospitalAccountMaster();
							$scope.initHospitalAccountMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
					
					var hospitalAccountObj = {
							hospitalAccountId:id,
							status:status,
							updatedDate:$scope.dt,
							updatedBy:$scope.updatedBy,
							orgnizationId:$scope.orgId,
							unitId:$scope.orgUnitId
					}
					var URI = BASE_URL + ROOT_URL + UPDATEHOSPITALACCOUNTMASTERSTATUS;
					
					GenericService.serviceAction("POST",URI,hospitalAccountObj).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
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