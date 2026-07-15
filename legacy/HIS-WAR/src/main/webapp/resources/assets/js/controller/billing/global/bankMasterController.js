/**
 * 
 */

angular.module('myApp')
		.controller("bankMasterController",
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
			
			$scope.initBankMaster = function(){
				
				$scope.saveBtnFlag = true; //added by minesh
				$scope.updateBtnFlag = false; //added by minesh
				$scope.updateScope = false; //added by minesh
				
				$scope.common = {
					bankId:"",				
					branchId:"",
					bankCode:"",
					bankName:"",
					bankAddress:"",
					branchDesc:""
				};
				
				var data = {
						orgnisationId:$scope.orgId
						};
				
				var URI = BASE_URL + ROOT_URL + GETACTIVEBANKBRANCHMASTERLIST; 
			
				GenericService.serviceAction("POST",URI,data).then(function(response){
					$scope.branchList=[];
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.branchList = response.data.listObject;
					console.log("branchList",$scope.branchList);
					}
				});
				
			}
			$scope.initBankMaster();
			
			
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
						orgnisationId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var data2 = {
						orgnisationId:orgId
						};
				
				var URI1 = BASE_URL + ROOT_URL + GETBANKMASTERLIST; 
				var URI2 = BASE_URL + ROOT_URL + GETBANKMASTERCOUNT;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.bankList = response.data.listObject;
					console.log("bankList",$scope.bankList);
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
						orgnisationId:orgId,// change by minesh from organisationId to orgnisationId
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + GETBANKMASTERLIST;
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
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');
					if($scope.common.branchId == undefined || $scope.common.branchId == "Select Branch" || $scope.common.branchId == ""){
						$rootScope.stopLoader();
						growl.error("Please Select Branch",
								{
									title : 'error'
								});
						return false;
					}
					var visitTypeObj = {
							branchId : $scope.common.branchId,
							bankCode : $scope.common.bankCode,
							bankName : $scope.common.bankName,
							branchDesc : $scope.common.branchDesc,
							bankAddress : $scope.common.bankAddress,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							status:'A',
							orgnisationId:$scope.orgId,
							unitId:$scope.orgUnitId,
							updatedDate:$scope.dt
					}
					
					var URI = BASE_URL + ROOT_URL + SAVEBANKMASTER;
					console.log("Save-URI-",URI);
					console.log("OBJ",visitTypeObj);
					 if ($scope.bankForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					
					GenericService.serviceAction("POST",URI,visitTypeObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							return false;
							$scope.initBankMaster();
							$scope.initPaymentMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
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
			
			$scope.showUpdateBtn = function(bankId){
				try{
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL + GETBANKMASTERBYID+"/"+bankId+"/"+$scope.orgId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.branchId=response.data.listObject[0].branchId.toString();
							console.log(response.data.listObject[0].branchId);
							console.log($scope.common.branchId);
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
					$scope.$broadcast('show-errors-check-validity');
					if($scope.common.branchId == undefined || $scope.common.branchId == "Select Branch"){
						$rootScope.stopLoader();
						growl.error("Please Select Branch",
								{
									title : 'error'
								});
						return false;
					}
					var visitTypebj = {
							bankId : $scope.common.bankId,
							branchId : $scope.common.branchId,
							bankCode : $scope.common.bankCode,
							bankName : $scope.common.bankName,
							bankAddress : $scope.common.bankAddress,
							updatedBy:$scope.updatedBy,
							orgnisationId:$scope.orgId,
							unitId:$scope.orgUnitId,// getActiveBankList depend on unitId so unitId pass added by minesh
							updatedDate:$scope.dt
					}					
					var URI = BASE_URL + ROOT_URL + UPDATEBANKMASTER;
					console.log("Update-URI-",URI);
					console.log("OBJ",visitTypebj);
					 if ($scope.bankForm.$valid) {
					GenericService.serviceAction("POST",URI,visitTypebj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();							
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.initBankMaster();
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
					 }
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var bankObj = {
							bankId:id,
							status:status,
							updatedDate:$scope.dt,
							updatedBy:$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + UPDATEBANKMASTERSTATUS;
					
					GenericService.serviceAction("POST",URI,bankObj).then(function(response){
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