/**
 * 
 */
angular.module('myApp')
		.controller("departmentNursingStationMapperController",
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
			$scope.recordsPerPage = 10;
			$scope.commonList = [];
			$scope.departmentsList=[];
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
			$scope.specialityId = cookieObject.specialityId;
			$scope.createdBy =cookieObject.specialityId;
			$scope.updatedBy =cookieObject.specialityId;
			
			
			$scope.departmentNursingStationMppr=[];
			$scope.nursingStationList = [];
			$scope.selectdepartmentNursingStationMapperList=[];
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						departmentNursingStationMpprId:"",
						specialityId:"",
						nursingStationId:"",
						specialityId:"",
						nursingStationDesc:"",
						specialityName:"",
						isDeleted:""
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.initdepartmentNursingStationMapper = function(){
				$scope.departmentNursingStations =
				{
					 departmentNursingStationMpprId:$scope.common.departmentNursingStationMpprId,
					 nursingStationId:$scope.common.nursingStationId,
					 specialityId:$scope.common.specialityId,
					 specialityId:$scope.common.specialityId,
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
			$scope.departmentSelectNursingStations=[];
			$scope.addNursingStation = function(nursingStationId,nursingStationDesc){
				
				var flag = false;
				angular.forEach($scope.departmentSelectNursingStations,function(value,index){
					if(value.nursingStationId == nursingStationId){
						$scope.departmentSelectNursingStations.splice(index,1);
						$scope.departmentNursingStationMppr[value.nursingStationId] = false;
						flag = true;
					}
				})
				if(!flag){
					var temp = {nursingStationId:nursingStationId,nursingStationDesc:nursingStationDesc};
					$scope.departmentSelectNursingStations.push(temp);
				}
			}
			$scope.selectNursingStation = function()
			{
//				alert();
				$scope.selectdepartmentNursingStationMapperList = angular.copy($scope.departmentSelectNursingStations);
			}
			//Get Diagnosis List end
			
			//Get Template List 
			$scope.getdepartmentsList = function(){
		  		
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
		  		var data = {
		  				unitId : $scope.unitId,
						organizationId : $scope.organisationId
		  		}
		  		GenericService.serviceAction("POST",URI,data).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.departmentsList = [];
				  			$scope.departmentsList=response.data.listObject;

		  			console.log("departmentsList",$scope.getdepartmentsList);
		  			}
		  		});
//		  		$scope.usrDetails(common.specialityId);
		  		}
		  		$scope.getdepartmentsList();
		  	
		  	//Get Template List end
		  		
		  	//Get Detailed List By Template Id
			$scope.getNursingStationMappedByspecialityId = function(specialityId) {
				try {
					var data = "";
					$rootScope.startLoader();	
					$scope.getListObject=[];
					var URI = BASE_URL + ROOT_URL+"/api/global/getDepartmentNursingStationMapperById"+S+specialityId+S+$scope.organisationId+S+$scope.unitId;
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
		  			    noOfRecordsPerPage:noOfRecordsPerPage,
		  			    offset:offset
		  		};
								
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getDepartmentNursingStationMapperlist"; 
				var URI2 = BASE_URL + ROOT_URL +"/api/global/getDepartmentNursingStationMapperCount/"+$scope.organisationId+"/"+$scope.unitId;
					
				
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
			  			    noOfRecordsPerPage:noOfRecordsPerPage,
			  			    offset:offset
		  		};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getDepartmentNursingStationMapperlist";
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
						if($scope.common.specialityId == '' || $scope.common.specialityId == null || $scope.common.specialityId == undefined){
							growl.warning("Please select specialty..!!",
									{
										title : 'Warning!'
									});
							return false;
							
						}else if($scope.selectdepartmentNursingStationMapperList.length == 0){
							growl.warning("Please assign nursing station..!!",
									{
										title : 'Warning!'
									});
							return false;
						}
						else{
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						
						$rootScope.startLoader();
						$scope.initdepartmentNursingStationMapper();
						$scope.listdepartmentNursingStationMapper =
						{
								specialityId:$scope.common.specialityId,
							     organisationId:$scope.organisationId,
							     unitId:$scope.unitId,
							     status:"A",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
							     selectdepartmentNursingStationMapperList:[]
						     
						 }
						angular.forEach($scope.selectdepartmentNursingStationMapperList,function(value,index){
								$scope.listdepartmentNursingStationMapper.selectdepartmentNursingStationMapperList.push(value.nursingStationId);
						})
						console.log("departmentNursingStations",JSON.stringify($scope.listdepartmentNursingStationMapper));
						var URI = BASE_URL + ROOT_URL + "/api/global/saveDepartmentNursingStationMapper";
						GenericService.serviceAction("POST", URI, $scope.listdepartmentNursingStationMapper).then(function(response) 
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
								$scope.departmentNursingStationMppr=[];
								$scope.selectdepartmentNursingStationMapperList=[];
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
			
			
			$scope.showUpdateBtn = function(id)
			{
				try {
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					var URI = BASE_URL + ROOT_URL +"/api/global/getDepartmentNursingStationMapperById/"+id+"/"+$scope.organisationId+"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
					{
						console.log("data",response.data);
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common.specialityId = response.data.listObject[0].specialityId + "";
							$scope.selectdepartmentNursingStationMapperList = [];
							$scope.tempDiagnosis = [];
							$scope.departmentSelectNursingStations=[];
							angular.forEach($scope.departmentNursingStationMppr,function(value,index){
								$scope.departmentNursingStationMppr[index] = false;
							var i = $scope.departmentSelectNursingStations.indexOf(value);
							$scope.departmentSelectNursingStations.splice(i,1);
							})
							
							angular.forEach(response.data.listObject,function(value,index){
								var temp = {nursingStationId:value.nursingStationId,nursingStationDesc:value.nursingStationDesc};
								$scope.departmentSelectNursingStations.push(temp);
								$scope.departmentNursingStationMppr[value.nursingStationId] = true;
								$scope.selectdepartmentNursingStationMapperList.push(temp);
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
						if($scope.common.specialityId == '' || $scope.common.specialityId == null || $scope.common.specialityId == undefined){
							growl.warning("Please select specialty..!!",
									{
										title : 'Warning!'
									});
							return false;
							
						}else if($scope.selectdepartmentNursingStationMapperList.length == 0){
							growl.warning("Please assign nursing station..!!",
									{
										title : 'Warning!'
									});
							return false;
						}
						else{
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');		
						
						$rootScope.startLoader();
						$scope.initdepartmentNursingStationMapper();
						$scope.listdepartmentNursingStationMapper =
						{
								specialityId:$scope.common.specialityId,
						     organisationId:$scope.organisationId,
						     unitId:$scope.unitId,
						     status:"A",
							 createdBy:$scope.createdBy,
							 createdDate:$scope.dt,
							 updatedBy:$scope.updatedBy,
							 updatedDate:$scope.dt,
						     selectdepartmentNursingStationMapperList:[]
						     
						 }
						angular.forEach($scope.selectdepartmentNursingStationMapperList,function(value,index){
								$scope.listdepartmentNursingStationMapper.selectdepartmentNursingStationMapperList.push(value.nursingStationId);
						})
						console.log("departmentNursingStations",JSON.stringify($scope.listdepartmentNursingStationMapper));
					
						var URI = BASE_URL + ROOT_URL +"/api/global/updateDepartmentNursingStationMapper";
					    GenericService.serviceAction("POST", URI, $scope.listdepartmentNursingStationMapper).then(function(response) 
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
								$scope.departmentNursingStationMppr=[];
								$scope.selectdepartmentNursingStationMapperList=[];
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
					$scope.initdepartmentNursingStationMapper();
					var URI = BASE_URL + ROOT_URL +"/api/global/updateDepartmentNursingStationMapperStatus/"+id+"/"+$scope.organisationId+"/"+$scope.unitId+"/"+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.departmentNursingStations).then(function(response) 
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
							$scope.departmentNursingStationMppr=[];
							$scope.selectdepartmentNursingStationMapperList=[];
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