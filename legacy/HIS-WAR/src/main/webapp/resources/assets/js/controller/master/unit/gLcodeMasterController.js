/**
 * 
 */

angular.module('myApp')
		.controller("gLcodeMasterController",
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
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						generalLedgerId:"",
						generalLedgerCode:"",
						generalLedgerName:""
						
			
				};
				$scope.searchKeyword = "";
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();

			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organizationId:organizationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								//unitId:unitId
							};
				
				var data2 = {
						      organizationId:organizationId
						     // unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/org/getGeneralLedgerMasterList/"+$scope.organizationId+"/"+offset+"/"+noOfRecordsPerPage; 
				var URI2 = BASE_URL + ROOT_URL + "/org/getGeneralLedgerMasterCount";
//					
				
				GenericService.serviceAction("GET",URI1,data1).then(function(response){
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
						if($scope.updateBtnFlag==true){
							console.log($scope.pageNo)
							$scope.setPage($scope.pageNo,true);
						}
						else{
							console.log("else"+$scope.pageNo)
							$scope.setPage(1,false);
						}
						$scope.saveBtnFlag = true;
						$scope.updateBtnFlag=false;
					}
				});
				
			}		
			var pageOffset='';
			$scope.masterList = function(organizationId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				pageOffset=offset;
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:organizationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						//unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL +"/org/getGeneralLedgerMasterList/"+$scope.organizationId+"/"+offset+"/"+noOfRecordsPerPage; 
				console.log("URI", URI);
				GenericService.serviceAction("GET", URI, data).then(function(response) {
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
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
			
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
							generalLedgerCode:$scope.common.generalLedgerCode,
							generalLedgerName:$scope.common.generalLedgerName,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:'A',
							organizationId:$scope.organizationId,
							//unitId:$scope.unitId,
							createdDate:$scope.dt,
							createdBy:$scope.createdBy
													
					}
				
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/org/saveGeneralLedgerMaster";
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
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
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
				$scope.saveBtnFlag = false;
				$scope.updateBtnFlag=true;
				try{
					$rootScope.startLoader();
					$scope.updateScope = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/org/getGeneralLedgerMasterById/"+id+"/"+$scope.organizationId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.generalLedgerId = response.data.listObject[0].generalLedgerId.toString();
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
			
			$scope.pageNo='';
			$scope.update = function(){
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
					$rootScope.startLoader();
					var data1={	
							generalLedgerId:$scope.common.generalLedgerId,
							generalLedgerCode:$scope.common.generalLedgerCode,
							generalLedgerName:$scope.common.generalLedgerName,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
																				
						}
									
					var URI = BASE_URL + ROOT_URL + "/org/updateGeneralLedgerMaster";
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
						/*	$scope.initMaster();*/
							$scope.common={}
							
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						
						$scope.offset=pageOffset;
						console.log($scope.offset)
					   $scope.initMasterList(
									$scope.organizationId,
									$scope.offset,
									$scope.noOfRecordsPerPage
									);
						$scope.pageNo=($scope.offset/10)+1;
					    $scope.setPage($scope.pageNo,true)
					    console.log("pageNo"+$scope.pageNo)
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
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:status,
							generalLedgerId:Id
					}
					var URI = BASE_URL + ROOT_URL + "/org/updateGeneralLedgerMasterStatus";
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