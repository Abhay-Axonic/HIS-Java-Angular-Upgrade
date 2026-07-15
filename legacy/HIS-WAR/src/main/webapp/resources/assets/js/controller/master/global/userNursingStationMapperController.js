/**
 * 
 */
angular.module('myApp')
		.controller("userNursingStationMapperController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
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
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.UserNursingStationMppr=[];
			$scope.nursingStationList = [];
			$scope.selectUserNursingStationMapperList=[];
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						userNursingStationMpprId:"",
						nursingStationId:"",
						userId:"",
						nursingStationDesc:"",
						empCode:"",
						userName:"",
						isDeleted:"",
						employeeName:""
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.initUserNursingStationMapper = function(){
				$scope.UserNursingStations =
				{
					 userNursingStationMpprId:$scope.common.userNursingStationMpprId,
					 nursingStationId:$scope.common.nursingStationId,
					 userId:$scope.common.userId,
				     status:$scope.common.status,
					 createdBy:$scope.createdBy,
					 updatedBy:$scope.updatedBy,
				     organisationId:$scope.organisationId,
				     unitId:$scope.unitId,
				     nursingStationDesc:$scope.common.nursingStationDesc, 
				     title:$scope.common.title,
				     isDeleted: "N"
				 }
				
				}
			
			//Get Nursing Station List
			  		
			$scope.searchGroupList = function() {
				
				var data = {};
				var paramObj = {
						nursingStationDesc : ""
				};
				var headerObj = {									
					unit_id : $scope.unitId,
					organisation_id : $scope.organisationId
				}
			
				var URI = BASE_URL + ROOT_URL+ GET_NURSING_STATIONS+S+$scope.organisationId+S+$scope.unitId;
				
				return GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
						.then(function(response) {
							$scope.nursingStationList=[];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.nursingStationList= response.data.listObject;
									console.log("nursingStationList",$scope.nursingStationList);
								});

			}
	
			$scope.searchGroupList();
			
			//Get Diagnosis List
			$scope.UserSelectNursingStations=[];
			$scope.addNursingStation = function(nursingStationId,nursingStationDesc){
				var flag = false;
				angular.forEach($scope.UserSelectNursingStations,function(value,index){
					if(value.nursingStationId == nursingStationId){
						$scope.UserSelectNursingStations.splice(index,1);
						$scope.UserNursingStationMppr[value.nursingStationId] = false;
						flag = true;
					}
				})
				if(!flag){
					var temp = {nursingStationId:nursingStationId,nursingStationDesc:nursingStationDesc};
					$scope.UserSelectNursingStations.push(temp);
				}
			}
			$scope.selectNursingStation = function()
			{
//				alert();
				$scope.selectUserNursingStationMapperList = angular.copy($scope.UserSelectNursingStations);
			}
			//Get Diagnosis List end
			
			//Get Template List 
			$scope.getusersList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + GET_USER_LIST+S+$scope.organisationId+S+$scope.unitId;
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
//		  		$scope.usrDetails(common.userId);
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
		  	//Get Template List end
		  		
		  	//Get Detailed List By Template Id
			$scope.getNursingStationMappedByUserId = function(userId) {
				try {
					var data = "";
					$rootScope.startLoader();	
					$scope.getListObject=[];
					var URI = BASE_URL + ROOT_URL+GET_DETAILED_NURSING_STATION_LIST+S+userId+S+$scope.organisationId+S+$scope.unitId;
					console.log("URI", URI);
					GenericService
							.serviceAction("GET", URI, data)
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
				var data1 = {
		  				organisationId:$scope.organisationId,
		  			      unitId:$scope.unitId,
		  			    offset : offset,
						noOfRecordsPerPage : noOfRecordsPerPage
		  		};
								
				var URI1 = BASE_URL + ROOT_URL +GET_USER_NURSING_STATION_MAPPER_LIST; 
				var URI2 = BASE_URL + ROOT_URL +GET_USER_NURSING_STATION_MAPPER_COUNT+S+$scope.organisationId+S+$scope.unitId;
					
				
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
		  				organisationId:$scope.organisationId,
		  			      unitId:$scope.unitId,
		  			    offset : offset,
						noOfRecordsPerPage : noOfRecordsPerPage
		  		};
				
				var URI = BASE_URL + ROOT_URL + GET_USER_NURSING_STATION_MAPPER_LIST;
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
	
			
			$scope.save = function() 
			{
				$scope.$broadcast('show-errors-check-validity');
//				if ($scope.commonform.$valid)
//				{
					try {
						
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						
						$scope.initUserNursingStationMapper();
						$scope.listUserNursingStationMapper =
						{
								 userId:$scope.common.userId,
							     organisationId:$scope.organisationId,
							     unitId:$scope.unitId,
							     status:"A",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
							     selectUserNursingStationMapperList:[]
						     
						 }
						angular.forEach($scope.selectUserNursingStationMapperList,function(value,index){
								$scope.listUserNursingStationMapper.selectUserNursingStationMapperList.push(value.nursingStationId);
						})
						
						if($scope.common.userId == "" || $scope.common.userId == null){
							growl.warning("Please select user...!!",{
								title : 'Warning!'
							});
							return;
						}else if ($scope.listUserNursingStationMapper.selectUserNursingStationMapperList == null || $scope.listUserNursingStationMapper.selectUserNursingStationMapperList.length == 0  ){
							growl.warning("Please select at least one nursing station...!!",{
								title : 'Warning!'
							});
							return;
						}else{
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + SAVE_USER_NURSING_STATION_MAPPER;
							GenericService.serviceAction("POST", URI, $scope.listUserNursingStationMapper).then(function(response) 
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
									$scope.UserSelectNursingStations=[];
									$scope.UserNursingStationMppr=[];
									$scope.selectUserNursingStationMapperList=[];
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
						}
						
					} catch (e) {
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			
			$scope.showUpdateBtn = function(info)
			{
				try {
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					var URI = BASE_URL + ROOT_URL +GET_USER_NURSING_STORE_MAPPER_BY_ID+S+info.userId+S+$scope.organisationId+S+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
					{
						console.log("data",response.data);
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							if($scope.common==undefined){
								$scope.common = {
										userNursingStationMpprId:"",
										nursingStationId:"",
										userId:"",
										nursingStationDesc:"",
										empCode:info.empCode,
										userName:"",
										isDeleted:"",
										employeeName:info.employeeName							
								};
							}else{
								$scope.common = response.data.listObject[0];
							}
							
							$scope.common.userId = info.userId.toString();
							
							$scope.selectUserNursingStationMapperList = [];
							$scope.tempDiagnosis = [];
							
							angular.forEach($scope.UserNursingStationMppr,function(value,index){
								$scope.UserNursingStationMppr[index] = false;
							var i = $scope.UserSelectNursingStations.indexOf(value);
							$scope.UserSelectNursingStations.splice(i,1);
							})
							
							angular.forEach(response.data.listObject,function(value,index){
								var temp = {nursingStationId:value.nursingStationId,nursingStationDesc:value.nursingStationDesc};
								$scope.UserSelectNursingStations.push(temp);
								$scope.UserNursingStationMppr[value.nursingStationId] = true;
								$scope.selectUserNursingStationMapperList.push(temp);
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
						$scope.initUserNursingStationMapper();
						$scope.listUserNursingStationMapper =
						{
							 userId:$scope.common.userId,
						     organisationId:$scope.organisationId,
						     unitId:$scope.unitId,
						     status:"A",
							 createdBy:$scope.createdBy,
							 createdDate:$scope.dt,
							 updatedBy:$scope.updatedBy,
							 updatedDate:$scope.dt,
						     selectUserNursingStationMapperList:[]
						     
						 }
						angular.forEach($scope.selectUserNursingStationMapperList,function(value,index){
								$scope.listUserNursingStationMapper.selectUserNursingStationMapperList.push(value.nursingStationId);
						})
						console.log("UserNursingStations",JSON.stringify($scope.listUserNursingStationMapper));
						if($scope.common.userId == "" || $scope.common.userId == null){
							growl.warning("Please select user...!!",{
								title : 'Warning!'
							});
							return;
						}else if ($scope.listUserNursingStationMapper.selectUserNursingStationMapperList == null || $scope.listUserNursingStationMapper.selectUserNursingStationMapperList.length == 0  ){
							growl.warning("Please select at least one nursing station...!!",{
								title : 'Warning!'
							});
							return;
						}else{
							var URI = BASE_URL + ROOT_URL +UPDATE_USER_NURSING_STATION_MAPPER;
						    GenericService.serviceAction("POST", URI, $scope.listUserNursingStationMapper).then(function(response) 
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
									$scope.UserSelectNursingStations=[];
									$scope.UserNursingStationMppr=[];
									$scope.selectUserNursingStationMapperList=[];
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
						}
						
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
					$scope.initUserNursingStationMapper();
					var URI = BASE_URL + ROOT_URL +UPDATE_USER_NURSING_STATION_MAPPER_STATUS+S+id+S+$scope.organisationId+S+$scope.unitId+S+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.UserNursingStations).then(function(response) 
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
							$scope.UserNursingStationMppr=[];
							$scope.selectUserNursingStationMapperList=[];
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
			
		
			
			
			/**Crud Operations End**/
}]);