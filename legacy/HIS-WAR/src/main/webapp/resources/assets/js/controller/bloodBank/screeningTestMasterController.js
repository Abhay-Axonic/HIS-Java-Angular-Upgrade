/* */

angular.module('myApp')
		.controller("screeningTestMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$rootScope.loginpage = true;
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.createdBy = cookieObject.userId;
			$scope.updatedBy = cookieObject.userId;
			
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						screeningTestId:"",
						screeningTestCode:"",
						screeningTestDesc:"",
						testId:''
							
				};
				$scope.obj = {
						testId : "",
						testCode : "",
						testDesc : ""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
					      	organizationId:organizationId,
							offset:offset,
							noOfRecordsPerPage:noOfRecordsPerPage,
							unitId:unitId
							};
				
				var data2 = {
						      organizationId:organizationId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL + GET_SCREENING_TEST_MASTER_LIST; 
				var URI2 = BASE_URL + ROOT_URL + GET_SCREENING_TEST_MASTER_COUNT;
					
				
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
			
			$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:organizationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL +  GET_SCREENING_TEST_MASTER_LIST;
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					if($scope.common.screeningTestDesc == undefined || $scope.common.screeningTestDesc == ""){
						growl.error("Please select Screening Test  Name",
								{
									title : 'error'
								});
						return false;
					}
					if($scope.common.screeningTestCode == undefined || $scope.common.screeningTestCode == ""){
						growl.error("Please Enter Screening Test Code",
								{
									title : 'error'
								});
						return false;
					}
					
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');

					var data={	
							testId:$scope.common.testId,
							screeningTestCode:$scope.common.screeningTestCode,
							screeningTestDesc:$scope.common.screeningTestDesc,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						
						}
				
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + SAVE_SCREENING_TEST_MASTER;
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$scope.common={};
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					$rootScope.stopLoader();
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + GET_SCREENING_TEST_MASTER_BY_ID +S+ id +S+ $scope.organizationId +S+ $scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.screeningTestId=response.data.listObject[0].screeningTestId;
							$scope.obj.testId = response.data.listObject[0].screeningTestId;
							$scope.obj.testDesc = response.data.listObject[0].screeningTestDesc;
							$scope.obj.testCode = response.data.listObject[0].screeningTestCode;
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
					if($scope.common.screeningTestDesc == undefined || $scope.common.screeningTestDesc == ""){
						growl.error("Please select Screening Test  Name",
								{
									title : 'error'
								});
						return false;
					}
					
					if($scope.common.screeningTestCode == undefined || $scope.common.screeningTestCode == ""){
						growl.error("Please Enter Screening Test Code",
								{
									title : 'error'
								});
						return false;
					}
					
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$rootScope.startLoader();

					var data={	
								screeningTestId:$scope.common.screeningTestId,
								screeningTestCode:$scope.common.screeningTestCode,
								screeningTestDesc:$scope.common.screeningTestDesc,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt
							
						}
								
					var URI = BASE_URL + ROOT_URL + UPDATE_SCREENING_TEST_MASTER;
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					$rootScope.startLoader(); 
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
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.screeningTestId=id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_SCREENING_TEST_MASTER_STATUS;
					console.log("update status",$scope.common);
					//return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
			
			//get Test Master List
			$scope.getTestList = function()
			{
				try 
				{
					var data = {
							"orgId":$scope.organizationId,
							"orgUnitId":$scope.unitId
					};
					
					var URI = BASE_URL 
					        + ROOT_URL
							+ LIS_HISTOPATHOLOGY
							+ HISTO_MICRO_TEST_LIST
							+ S + $scope.organizationId
							+S + $scope.unitId
					console.log("URI", URI);
					 GenericService.serviceAction("GET", URI).then(function(response) {
						console.log("response.data.listObject",response.data.listObject);
						$scope.testList =  response.data.listObject;
					});
				} catch (e) {
					console.log(e.message)
				}
			}
			$scope.getTestList();
			//get test Code by id
			$scope.getTestCode = function(obj)
			{
				$scope.common.screeningTestCode = obj.testCode;
				$scope.common.screeningTestDesc = obj.testDesc;
				$scope.common.testId = obj.testId;
				
			}
}]);