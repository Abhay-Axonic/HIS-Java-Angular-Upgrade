/* */


angular.module('myApp')
		.controller("userCounterMapperController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.userList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.organisationId = $scope.organizationId;
			$scope.orgId = $scope.organizationId;
			$scope.unitId = $scope.unitId;
			$scope.createdBy =$scope.userId ;
			$scope.updatedBy =$scope.userId ;
			$scope.userCounterMapper=[];
			$scope.counterList= [];
			$scope.selectedCounterList=[];
			
//			var cookieObject = $cookies.getObject('cookieObject');
//			if(cookieObject == undefined){
//				$state.go('login');
//				return;
//			}
//			
//			$scope.unitId = cookieObject.unitId;
//			$scope.organisationId = cookieObject.organisationId;
//			$scope.organizationId = cookieObject.organizationId;
//			$scope.orgId=cookieObject.organizationId;
//			$scope.userId = cookieObject.userId;
//			$scope.createdBy =cookieObject.userId;
//			$scope.updatedBy =cookieObject.userId;
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						userCounterMpprId:"",
						counterId:"",
						userId:"",
						empName:"",
						employeeName:"",
						employeeCode:"",
						counterCode:"",
						counterDesc:"",
						isDeleted:"",
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.initCounterMaster = function(){
				$scope.counterMaster =
				{
					 userCounterMpprId:$scope.common.userCounterMpprId,
					 counterId:$scope.common.counterId,
					 userId:$scope.common.userId,
				     status:$scope.common.status,
					 createdBy:$scope.createdBy,
					 updatedBy:$scope.updatedBy,
				     organisationId:$scope.organisationId,
				     unitId:$scope.unitId,
				     counterDesc:$scope.common.counterDesc, 
				     counterCode:$scope.common.counterCode, 
				     employeeName:$scope.common.employeeName, 
				     employeeCode:$scope.common.employeeCode,
					 empName:$scope.common.empName,

				     isDeleted: "N"
				 }
				
				}
			
	
			  		
			$scope.searchGroupList = function() {
				
				var data = {};
				var paramObj = {
						counterDesc : ""
				};
				var headerObj = {									
					unit_id : $scope.unitId,
					organisation_id : $scope.organisationId
				}

				var URI = BASE_URL + ROOT_URL+ "/api/global/unit/counter/organization/"+$scope.organisationId+"/unit/"+$scope.unitId;
				
				return GenericServiceParamHeader.serviceAction("GET", URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
						.then(function(response) {
							$scope.counterList=[];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.counterList= response.data.listObject;
									console.log("counterList",$scope.counterList);
								});

			}
	
			$scope.searchGroupList();
			
			//Get Counter List
			$scope.selectCounter=[];
			$scope.addCounter = function(counterId,counterDesc,counterCode){
				
				var flag = false;
				angular.forEach($scope.selectCounter,function(value,index){
					if(value.id == counterId){
						$scope.selectCounter.splice(index,1);
						$scope.userCounterMapper[value.id] = false;
						flag = true;
					}
				})
				if(!flag){
					var temp = {id:counterId,name:counterDesc,code:counterCode};
					$scope.selectCounter.push(temp);
				}
			}
			$scope.listOfCounters = function()
			{
                    $scope.selectCounter = $scope.selectCounter.filter((value, index, self) =>
                        index === self.findIndex((t) => (
                            t.id === value.id
                        ))
                    );
                    $scope.selectedCounterList = angular.copy($scope.selectCounter);
			}
			//Get user List end
			
			//Get Users List 
			$scope.getUserList = function(){
		  		
				var URI = BASE_URL + ROOT_URL + GET_USER_LIST+S+$scope.organisationId+S+$scope.unitId;
			  		var data = {
			  				orgId:$scope.organisationId,
			  			      unitId:$scope.unitId
			  		}
			  		GenericService.serviceAction("GET",URI,data).then(function(response){
			  			if(response.data.status == "success"){
					  			$rootScope.stopLoader();					
					  			$scope.userList = [];
					  			$scope.userList=response.data.listObject;
	
			  			console.log("usersList",$scope.getusersList);
			  			}
			  		});
		  		}
		  		$scope.getUserList();
		  
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = "";
				
//				var data2 = "";
				
				var URI1 = BASE_URL + ROOT_URL +GET_USER_COUNTER_MASTER_LIST+S+$scope.orgId+S+$scope.unitId+S+offset+S+noOfRecordsPerPage; 
				var URI2 = BASE_URL + ROOT_URL +GET_USER_COUNTER_MASTER_COUNT+S+$scope.organisationId+S+$scope.unitId;
					
				
				GenericService.serviceAction("GET",URI1,"").then(function(response){
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
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = "";
				
				var URI = BASE_URL + ROOT_URL + GET_USER_COUNTER_MASTER_LIST+S+$scope.orgId+S+$scope.unitId+S+offset+S+noOfRecordsPerPage; 
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
	
			
			$scope.save = function() 
			{
				$scope.$broadcast('show-errors-check-validity');
//				if ($scope.commonform.$valid)
//				{
					try {
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						
						$rootScope.startLoader();
						$scope.initCounterMaster();
						$scope.listCounterMaster =
						{
//								 counterId:$scope.common.counterId,
								 userId:$scope.common.userId,
							     status:$scope.common.status,
								 counterDesc:$scope.common.counterDesc, 
							     counterCode:$scope.common.counterCode, 
							     employeeName:$scope.common.employeeName, 
							     employeeCode:$scope.common.employeeCode, 
							     isDeleted: "N",
							     organisationId:$scope.organisationId,
							     unitId:$scope.unitId,
							     status:"A",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
								 empName:$scope.common.empName,
								 selectedCounterList:[]
						     
						 }
						angular.forEach($scope.selectedCounterList,function(value,index){
								$scope.listCounterMaster.selectedCounterList.push(value.id);
						})
						console.log("TemplateDiagnosisMaster",JSON.stringify($scope.listCounterMaster));
						
						
						if($scope.listCounterMaster.userId=="" || $scope.listCounterMaster.userId==null || $scope.listCounterMaster.userId==undefined){
							
							growl.warning("Please select user.",
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;

						}
						else if($scope.listCounterMaster.selectedCounterList.length <= 0){
							
							growl.warning("Please select counter.",
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;

						}
						
						var URI = BASE_URL + ROOT_URL + ADD_USER_COUNTER_MASTER;
						GenericService.serviceAction("POST", URI, $scope.listCounterMaster).then(function(response) 
						{	
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.selectedCounterList=[];
								$scope.counterList=[];
								$scope.searchGroupList();
								$scope.userCounterMapper=[];
								$scope.selectCounter=[];

								$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
							
						});
					} catch (e) {
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			
			$scope.showUpdateBtn = function(userId)
			{
				try {
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					var URI = BASE_URL + ROOT_URL +GET_USER_COUNTER_MASTER_BY_ID+S+$scope.orgId+S+$scope.unitId+S+userId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
					{
						console.log("data",response.data);
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common.userId = response.data.object[0].userId+"";
							console.log("userId",$scope.common.userId);
							$scope.selectedCounterList = [];
							$scope.countersList = [];
							
							angular.forEach($scope.userCounterMapper,function(value,index){
								$scope.userCounterMapper[index] = false;
							var i = $scope.selectCounter.indexOf(value);
							$scope.selectCounter.splice(i,1);
							})
							
							angular.forEach(response.data.listObject,function(value,index){
								var temp = {id:value.counterId,name:value.name, code:value.counterCode};
								$scope.selectCounter.push(temp);
								$scope.userCounterMapper[value.counterId] = true;
								$scope.selectedCounterList.push(temp);
							});
							
							console.log("$scope.common",$scope.common);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							alert("Error In Fetching Data");
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
				
			}
			
			$scope.update = function() 
			{
				$scope.$broadcast('show-errors-check-validity');
//				if ($scope.commonform.$valid)
//				{
					try {
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');		
						
						$rootScope.startLoader();
						$scope.initCounterMaster();
						$scope.listCounterMaster =
						{
//								 counterId:$scope.common.counterId,
								 userId:$scope.common.userId,
							     status:$scope.common.status,
								 counterDesc:$scope.common.counterDesc, 
							     counterCode:$scope.common.counterCode, 
							     employeeName:$scope.common.employeeName, 
							     employeeCode:$scope.common.employeeCode, 
							     isDeleted: "N",
							     status:"A",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 organisationId:$scope.organisationId,
								 unitId:$scope.unitId,
								 updatedDate:$scope.dt,
								 empName:$scope.common.empName,
								 selectedCounterList:[]
						     
						 }
						angular.forEach($scope.selectedCounterList,function(value,index){
								$scope.listCounterMaster.selectedCounterList.push(value.id);
						})
						console.log("CounterMaster",JSON.stringify($scope.listCounterMaster));
					
						var URI = BASE_URL + ROOT_URL+ UPDATE_USER_COUNTER_MASTER;
					    GenericService.serviceAction("PUT", URI, $scope.listCounterMaster).then(function(response) 
						{
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.selectedCounterList=[];
								$scope.counterList=[];
								$scope.searchGroupList();
								$scope.userCounterMapper=[];
								$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
					} catch (e) 
					{
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			$scope.updateStatus = function(id,status)
			{
				try {
					$rootScope.startLoader();
					$scope.initCounterMaster();
					var URI = BASE_URL + ROOT_URL +UPDATE_USER_COUNTER_MASTER_STATUS+S+id+S+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.counterMaster).then(function(response) 
					{
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.selectedCounterList=[];
							$scope.counterList=[];
							$scope.searchGroupList();
							$scope.userCounterMapper=[];
							$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
				
			}
			
			//Get Detailed List By user Id
			$scope.getCounterListMasterByUserId = function(userId) {
				try {
					var data = "";
					$rootScope.startLoader();	
					$scope.getListObject=[];
					var URI = BASE_URL + ROOT_URL+GET_DETAILED_LIST_USER_COUNTER_MASTER+S+$scope.orgId+S+$scope.unitId+S+userId;
					console.log("URI", URI);
					GenericService
							.serviceAction("GET", URI, "")
							.then(
									function(response) {
										$scope.getListObject  = response.data.listObject;
										console.log("getListObject",JSON.stringify($scope.getListObject));
									});
					$rootScope.stopLoader();
				} catch (e) {
					console.log(e.message)
				}

			}
		
			
			
			/**Crud Operations End**/
}]);