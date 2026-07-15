/**
 * 
 * Sneha Dalave
 */
angular.module('myApp')
		.controller("userStoreMapperController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.LISDynamicLabel = "Antibiotic Addition";
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
		
			$scope.storeList = [];
		
		/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.usersList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
		
		var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
		
		
		$scope.selectedStoreList1=[];
		$scope.userStoreList = [];
		
		
		$scope.initCommon = function()
					{
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.common =
						{
							 "userStoreMapperId":"",
							 "storeId":"",
							 "userId":"",
						     "isActive":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "organisationId":$scope.organisationId,
						     "storeName":"", 
						     "userName":"", 
						     "isDeleted":"", 
						     "empCode":"",
						     "employeeName":"", 
						     "userName":"" 

						}
					}
					$scope.initCommon();
		
		$scope.initUserStoreMapper = function(){
					$scope.userStoreMapper =
					{
						 "userNursingStationMpprId":$scope.common.userNursingStationMpprId,
						 "storeId":$scope.common.storeId,
						 "userId":$scope.common.userId,
					     "isActive":$scope.common.isActive,
						 "addedBy":$scope.common.createdBy,
						 "addedDate":$scope.common.createdDate,
						 "updatedBy":$scope.common.updatedBy,
						 "updatedDate":$scope.common.updatedDate,
					     "organisationId":$scope.organisationId,
					     "unitId":$scope.unitId,
					     "storeName":$scope.common.storeName, 
					     "userName":$scope.common.userName,
					     "empCode":$scope.common.empCode,
					     "employeeName":$scope.common.employeeName, 
					     "isDeleted": false
					 }
					
					}
		
		//get user store list
		
		
		$scope.getStoreList = function() {
						/*try {
							
							var URI = BASE_URL + ROOT_URL+ "/api/global/getStoreList/"+$scope.organisationId+"/"+$scope.unitId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.getStoreList  = response.data.listObject;
												console.log("getStoreListtttt",$scope.getStoreList);
											});
						} catch (e) {
							console.log(e.message)
						}*/
			
			var data = {};
			var paramObj = {
					storeName : ""
			};
			var headerObj = {									
				unit_id : $scope.unitId,
				organisation_id : $scope.organisationId
			}
		
			var URI = BASE_URL + ROOT_URL+ "/api/global/getStoreList/"+$scope.organisationId+"/"+$scope.unitId;
			
			return GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,"","")
					.then(function(response) {
						$scope.getStoreList=[];
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.getStoreList= response.data.listObject;
								console.log("nursingStationList",$scope.getStoreList);
							});

	
						

					}
					$scope.getStoreList();
		//end user store list
		//get user List
		$scope.getusersList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + GET_USER_LIST_FOR_STORE+S+$scope.organisationId+S+$scope.unitId;
		  		var data = {
		  				orgId:$scope.organisationId,
		  			      unitId:$scope.unitId
		  		}
		  		GenericService.serviceAction("GET",URI,data).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.usersList = [];
				  			$scope.usersList=response.data.listObject;

		  			console.log("usersList",$scope.getusersList);
		  			}
		  		});

		  		}
		  		$scope.getusersList();
				
				$scope.usrDetails=function(id){
		  			angular.forEach($scope.usersList,function(value,index){
							if(value.userId == id){
								$scope.common.empCode=value.empCode;
								$scope.common.employeeName = value.employeeName;
							}
		  			
		  						})
		  			
		  			}
		//end user List
		
		
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				/*offset = offset != null ? offset : 0;*/
				offset =  0;
				/*noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;	*/	
				noOfRecordsPerPage = 10;	
				var data1 = {
		  			      organizationId:$scope.organisationId,
		  			      unitId:$scope.unitId,
		  			      offset:offset,
		  			      noOfRecordsPerPage:noOfRecordsPerPage
		  		};
								
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserStoreMapperlist/userNameWise";
				var URI2 = BASE_URL + ROOT_URL +"/api/global/getUserStoreMapperCount/"+$scope.organisationId+"/"+$scope.unitId;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2).then(function(response){
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
				
				var data = {
						  organizationId:$scope.organisationId,
		  			      unitId:$scope.unitId,
		  			      offset:offset,
		  			      noOfRecordsPerPage:noOfRecordsPerPage
		  		};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getUserStoreMapperlist"; 
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
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
			
			
			//code for status Update
					$scope.updateStatus = function(id,isActive)
					{
						try {
							$rootScope.startLoader();
							$scope.initUserStoreMapper();
							var URI = BASE_URL + ROOT_URL +"/api/global/updateUserStoreMapperStatus/"+id+"/"+$scope.organizationId+"/"+$scope.unitId+"/"+isActive;
								console.log("ACT_INACT_URI",URI);
								GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									$scope.$broadcast('show-errors-reset');
									$scope.initCommon();
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
			//end update Status
			
			//code for show update 
			$scope.showUpdateBtn = function(userId)
					{
						try {
							$rootScope.startLoader();
							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;
							var URI = BASE_URL + ROOT_URL +"/api/global/getUserStoreMapperById/"+userId+"/"+$scope.organisationId+"/"+$scope.unitId;
								console.log("EDIT",URI);
								GenericService.serviceAction("GET", URI, $scope.common).then(function(response)  
							{
								console.log("data",response.data);
								
								if (response.data.status == 'success')
								{
									
									$rootScope.stopLoader();
									$scope.common.userId = response.data.listObject[0].userId + "";
									$scope.usrDetails($scope.common.userId);
									$scope.selectedStoreList1 = [];
									$scope.tempStore = [];
									angular.forEach($scope.storeList,function(value,index){
										$scope.storeList[index] = false;
										var i = $scope.tempSelectStore.indexOf(value);
										$scope.tempSelectStore.splice(i,1);
									})
									angular.forEach(response.data.listObject,function(value,index){
										var temp = {storeId:value.storeId,storeName:value.storeName};
										$scope.tempSelectStore.push(temp);
										$scope.storeList[value.storeId] = true;
										$scope.selectedStoreList1.push(temp);
										console.log("$scope.selectedStoreList1111111",$scope.selectedStoreList1);
									});
									/*$scope.selectStore($scope.selectedStoreList1);*/
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
			//end show update
			
			//code for save user store mapper
	
			$scope.save = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
//						if ($scope.commonform.$valid)
//						{
							try {
								$rootScope.startLoader();
								$scope.initUserStoreMapper();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								
								if( $scope.common.userId == ""){
									growl.warning(" Please select user. ",
											{
												title : 'warning!'
											});
									$rootScope.stopLoader();
									return false;
								}
                               if( $scope.common.employeeName == ""){
                            	   growl.warning(" Please enter employee name",
											{
												title : 'warning!'
											});
                            	   $rootScope.stopLoader();
									return false;
								}
                               if( $scope.common.empCode == ""){
                            	   growl.warning(" Please enter employee code",
											{
												title : 'warning!'
											});
                            	   $rootScope.stopLoader();
									return false;
								}
                               if( $scope.selectedStoreList1.length == 0){
                            	   growl.warning(" Please select store",
											{
												title : 'warning!'
											});
                            	   $rootScope.stopLoader();
									return false;
								}
								$scope.listUserStoreMapper =
								{
									 "userId":$scope.common.userId,
								     "organizationId":$scope.organisationId,
								     "unitId":$scope.unitId,
								     "isActive":true,
									 "isDeleted":false,
									 "addedBy":$scope.common.createdBy,
									 "addedDate":$scope.dt,
									 "updatedBy":$scope.common.updatedBy,
									 "updatedDate":$scope.dt,
								     "selectUserStoreMapperList":[]
																	     
								 }
								angular.forEach($scope.selectedStoreList1,function(value,index){
										$scope.listUserStoreMapper.selectUserStoreMapperList.push(value.storeId);
								})
								console.log("UserStoreMapper",JSON.stringify($scope.listUserStoreMapper));
								var URI = BASE_URL + ROOT_URL + "/api/global/saveUserStoreMapper";
								GenericService.serviceAction("POST", URI, $scope.listUserStoreMapper).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.selectedStoreList1=[];
										$scope.storeList = [];
										$scope.tempSelectStore = [];
										$scope.initMasterList($scope.organisationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
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
							
//						}
					}
					
					//end save store mapper
			//code for update
			$scope.update = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
//						if ($scope.commonform.$valid)
//						{
							try {
								$rootScope.startLoader();
								$scope.initUserStoreMapper();
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								$scope.listUserStoreMapper =
								{
									 "userId":$scope.common.userId,
								     "organizationId":$scope.organisationId,
								     "unitId":$scope.unitId,
								     "isActive":true,
									 "isDeleted":false,
									 "addedBy":$scope.common.createdBy,
									 "addedDate":$scope.dt,
									 "updatedBy":$scope.common.updatedBy,
									 "updatedDate":$scope.dt,
								     "selectUserStoreMapperList":[]
								     
								 }
								angular.forEach($scope.selectedStoreList1,function(value,index){
										$scope.listUserStoreMapper.selectUserStoreMapperList.push(value.storeId);
								})
								console.log("UserStoreMapper",JSON.stringify($scope.listUserStoreMapper));
							
								var URI = BASE_URL + ROOT_URL +"/api/global/updateUserStoreMapper";
							    GenericService.serviceAction("POST", URI, $scope.listUserStoreMapper).then(function(response) 
								{
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.selectedStoreList1=[];
										$scope.storeList = [];
										$scope.tempSelectStore = [];
										$scope.initMasterList($scope.organisationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
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
							
//						}
					}
			//end update
			
			//code for select and add store
				$scope.tempSelectStore = [];
					$scope.addTempStore = function(storeId,storeName){
						
						var flag = false;
						angular.forEach($scope.tempSelectStore,function(value,index){
							if(value.storeId == storeId){
								$scope.tempSelectStore.splice(index,1);
								$scope.storeList[value.storeId] = false;
								flag = true;
							}
						})
						if(!flag){
							var temp = {storeId:storeId,storeName:storeName};
							$scope.tempSelectStore.push(temp);
						}
					}
					
					$scope.selectStore = function()
					{
						$scope.selectedStoreList1 = angular.copy($scope.tempSelectStore);
					}
			//end code
			
			//code for get store list by user id
			$scope.getStoreByUserId = function(userId) {
						try {
							var URI = BASE_URL + ROOT_URL+"/api/global/getDetailedStoreList/"+userId+"/"+$scope.organisationId+"/"+$scope.unitId;
					console.log("URI", URI);
					GenericService
							.serviceAction("GET", URI)
									.then(
											function(response) {
												$scope.listStoreByUserId = response.data.listObject;
												console.log("listStoreByUserIddddddd",JSON.stringify($scope.listStoreByUserId));
											});
						} catch (e) {
							console.log(e.message)
						}

					}
			//end code
			
}]);