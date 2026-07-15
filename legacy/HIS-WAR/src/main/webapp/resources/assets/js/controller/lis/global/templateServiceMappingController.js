/*
 * 
 * */
angular.module('myApp')
.controller("templateServiceMappingController",
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
	$scope.organizationId = cookieObject.organizationId;
	$scope.templateId = cookieObject.templateId;
	$scope.createdBy =cookieObject.userId;
	$scope.updatedBy =cookieObject.userId;
	
	
	$scope.UserNursingStationMppr=[];
	$scope.nursingStationList = [];
	$scope.selectTemplateServiceMapperList=[];
	
	
	$scope.initMaster = function(){
		$scope.saveBtnFlag = true;
		$scope.updateBtnFlag = false;
		$scope.common = {
							templateServiceMpperId:"",
							serviceId:"",
							templateId:"",
							title:"",
							serviceStandardName:"",
							serviceStandardCode:"",
							specialityId:"",
							specialityName:"",
							subSpecialityId:"",
							subSpecialityName:"",
							isDeleted:"",
						};
		console.log("Common-init",$scope.common);
	}
	$scope.initMaster();
	
	$scope.initUserNursingStationMapper = function(){
		$scope.UserNursingStations =
		{
			 templateServiceMpperId:$scope.common.templateServiceMpperId,
			 serviceId:$scope.common.serviceId,
			 templateId:$scope.common.templateId,
			 specialityId:$scope.common.specialityId,
			 subSpecialityId:$scope.common.subSpecialityId,
			 specialityName:$scope.common.specialityName,
			 subSpecialityName:$scope.common.subSpecialityName,
		     status:$scope.common.status,
			 createdBy:$scope.createdBy,
			 updatedBy:$scope.updatedBy,
		     organizationId:$scope.organizationId,
		     unitId:$scope.unitId,
		     serviceStandardName:$scope.common.serviceStandardName, 
		     serviceStandardCode:$scope.common.serviceStandardCode, 
		     title:$scope.common.title,
		     isDeleted: "N"
		 }
		
		}
	
	
	//Get Diagnosis List
	$scope.UserSelectNursingStations=[];
	$scope.addNursingStation = function(templateId,title){
		
		var flag = false;
		angular.forEach($scope.UserSelectNursingStations,function(value,index){
			if(value.templateId == templateId){
				$scope.UserSelectNursingStations.splice(index,1);
				$scope.UserNursingStationMppr[value.templateId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {templateId:templateId,title:title};
			$scope.UserSelectNursingStations.push(temp);
		}
	}
	$scope.selectNursingStation = function()
	{
//		alert();
		$scope.selectTemplateServiceMapperList = angular.copy($scope.UserSelectNursingStations);
	}
	//Get Diagnosis List end
	
	//Get Template List 
	$scope.getTemplateList = function()
	{
		$rootScope.startLoader();
  		var URI = BASE_URL + ROOT_URL + '/api/global/templates/isRenderedingSpecilaity';
  		
  		var data = {
  				  orgId:$scope.organizationId,
  			      unitId:$scope.unitId
  		}
  		GenericService.serviceAction("POST",URI,data).then(function(response){
  			if(response.data.status == "success"){
		  			$rootScope.stopLoader();					
		  			$scope.templateList = [];
		  			$scope.templateList=response.data.listObject;

  			console.log("templateList",$scope.getTemplateList);
  			$rootScope.stopLoader();
  			}
  			else if(response.data.status == "error")
  				{
  				  $rootScope.stopLoader();
  				}
  		});
  	}
  	$scope.getTemplateList();
  		
  		$scope.getNursingStationMappedByUserId = function(serviceId) {
			try {
				var data = "";
				$rootScope.startLoader();	
				$scope.getListObject=[];
				var URI = BASE_URL + ROOT_URL+"/api/lis/global/getDetailedServiceList/"+serviceId+"/"+$scope.organizationId+"/"+$scope.unitId;
				console.log("URI", URI);
				GenericService
						.serviceAction("GET", URI, data)
						.then(
								function(response) 
								{
									if(response.data.status == "success"){
									$scope.getListObject  = response.data.listObject;
									console.log("getListObject",JSON.stringify($scope.getListObject));
									 $rootScope.stopLoader();
									}
									else if(response.data.status == "error")
					  				{
					  				  $rootScope.stopLoader();
					  				}
									
								});
				$rootScope.stopLoader();
			} catch (e) {
				console.log(e.message)
			}

		}
  	/************************************************/	
  		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
  		var URI = BASE_URL + ROOT_URL
		+ GETACTIVESPECIALITY;

BillingGenericService
		.serviceAction(METHOD_POST, URI,
				commonObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(
				function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.specialityList = response.data.listObject;
				});

  		$scope.getSubSpecialityBySpeciality = function(specialityId)
		{
  			$rootScope.startLoader();	
			var data = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
				data.specialityId = specialityId;
				var URI = BASE_URL + ROOT_URL
						+ GETSUBSPECIALITYBYSPECIALITYID;

				
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.subSpecialityList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success")
									{$rootScope.stopLoader();
										$scope.subSpecialityList = response.data.listObject;
									}
									else if(response.data.status == "error")
									{
										$rootScope.stopLoader();
									}
								});
			
		}
  		
  	
  		$scope.getServiceBySubSpecilatity = function(subSpecialityId) {
  			$rootScope.startLoader();	
			 $scope.data={

						"subSpecialityIdList":[],
						"orgnisationId":$scope.organizationId,
						"unitId":$scope.unitId
						
			 }
					$scope.data.subSpecialityIdList.push(subSpecialityId);
			console.log(" $scope.dataa", $scope.data);
			var URI = BASE_URL + ROOT_URL+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
			
			return GenericService.serviceAction(METHOD_POST, URI,  $scope.data)
					.then(function(response) 
							{
						$scope.serviceList=[];
								// $rootScope.startLoader();
								if (response.data.status == "success"){
									$scope.serviceList= response.data.listObject;
									$scope.common.serviceId =$scope.common.serviceId+"" ;
									console.log("serviceList",$scope.serviceList);
									$rootScope.stopLoader();
								}else if(response.data.status == "error")
								{
									$rootScope.stopLoader();
								}
								
							});

		}
        /*********************************************************/
  		
	
	/**Crud Operations Start**/

	/**List And Pagination Start**/
	$scope.setNoOfRecords = function() {
		$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
	};
	
	$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
	{
		$rootScope.startLoader();
						
		var data1 = {
  				organizationId:$scope.organizationId,
  			    unitId:$scope.unitId,
  			    noOfRecordsPerPage: noOfRecordsPerPage != null ? noOfRecordsPerPage : 10,
  			    offset : offset != null ? offset : 0
  		};
						
		var URI1 = BASE_URL + ROOT_URL +"/api/lis/global/getTemplateServiceMapperlist"; 
		var URI2 = BASE_URL + ROOT_URL +"/api/lis/global/getTemplateServiceMapperCount/"+$scope.organizationId+"/"+$scope.unitId;
			
		
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
	
	$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
		$rootScope.startLoader();
		console.log("offset", offset);
		offset = offset != null ? offset : 0;
		noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
		
		var data = {
  				organizationId:$scope.organizationId,
  			      unitId:$scope.unitId,
  			    offset:offset,
  			  noOfRecordsPerPage:noOfRecordsPerPage
  			    
  		};
		
		var URI = BASE_URL + ROOT_URL + "/api/lis/global/getTemplateServiceMapperlist";
		console.log("URI", URI);
		GenericService.serviceAction("POST", URI, data).then(function(response) {
			
			if(response.data.status == "success"){
				$rootScope.stopLoader();					
				$scope.commonList = response.data.listObject;
				console.log("commonList",$scope.commonList);
				}
			else if(response.data.status == "error")
				{
				 $rootScope.stopLoader();
				}
			
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

	
	$scope.save = function() 
	{
		$scope.$broadcast('show-errors-check-validity');
//		if ($scope.commonform.$valid)
//		{
			try {
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
				
				$rootScope.startLoader();
				$scope.initUserNursingStationMapper();
				
				if($scope.common.specialityId==undefined || $scope.common.specialityId == null || $scope.common.specialityId ==""){
					$rootScope.stopLoader();
					growl.warning("Please Select Speciality ",
							{
								title : 'warning'
							});
					return false;
				}
				
				if($scope.common.subSpecialityId == undefined || $scope.common.subSpecialityId == null || $scope.common.subSpecialityId ==""){
					$rootScope.stopLoader();
					growl.warning("Please Select subSpeciality ",
							{
								title : 'warning'
							});
					return false;
				}
				
				if($scope.common.serviceId == undefined || $scope.common.serviceId == null || $scope.common.serviceId ==""){
					$rootScope.stopLoader();
					growl.warning("Please Select Service ",
							{
								title : 'warning'
							});
					return false;
				}
				
				$scope.listUserNursingStationMapper =
				{
						 templateId:$scope.common.templateId,
						 specialityId:$scope.common.specialityId,
						 subSpecialityId:$scope.common.subSpecialityId,
						 serviceId:$scope.common.serviceId,
					     organizationId:$scope.organizationId,
					     unitId:$scope.unitId,
					     status:"A",
						 createdBy:$scope.createdBy,
						 createdDate:$scope.dt,
						 updatedBy:$scope.updatedBy,
						 updatedDate:$scope.dt,
					     selectTemplateServiceMapperList:[]
				     
				 }
				if($scope.selectTemplateServiceMapperList == undefined || $scope.selectTemplateServiceMapperList == null || $scope.selectTemplateServiceMapperList.length == 0){
					$rootScope.stopLoader();
					growl.warning("Please Select Template Services ",
							{
								title : 'warning'
							});
					return false;
				}
				angular.forEach($scope.selectTemplateServiceMapperList,function(value,index){
						$scope.listUserNursingStationMapper.selectTemplateServiceMapperList.push(value.templateId);
				})
				console.log("UserNursingStations",JSON.stringify($scope.listUserNursingStationMapper));
				var URI = BASE_URL + ROOT_URL + "/api/lis/global/saveTemplateServiceMapper";
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
						$scope.UserNursingStationMppr=[];
						$scope.selectTemplateServiceMapperList=[];
					    $scope.UserSelectNursingStations=[];
	                    $scope.getTemplateList();
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
			} catch (e) {
				console.log("Exception",e.message);
			}
			
//		}
	}
	
	
	$scope.showUpdateBtn = function(serviceId)
	{
		try {
			$rootScope.startLoader();
			$scope.saveBtnFlag = false;
			$scope.updateBtnFlag = true;
			var URI = BASE_URL + ROOT_URL +"/api/lis/global/getTemplateServiceMapperById/"+serviceId+"/"+$scope.organizationId+"/"+$scope.unitId;
			console.log("EDIT",URI);
			GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
			{
				console.log("data",response.data);
				
				if (response.data.status == 'success')
				{
					$scope.selectTemplateServiceMapperList = [];
					$rootScope.stopLoader();
					$scope.common = response.data.listObject[0];
					$scope.common.specialityId = response.data.listObject[0].specialityId.toString();
					$scope.common.subSpecialityId = response.data.listObject[0].subSpecialityId.toString();
					$scope.selectTemplateServiceMapperList = [];
					$scope.tempDiagnosis = [];
					$scope.list=[];
					
					angular.forEach($scope.UserNursingStationMppr,function(value,index){
						$scope.UserNursingStationMppr[index] = false;
					var i = $scope.UserSelectNursingStations.indexOf(value);
					$scope.UserSelectNursingStations.splice(i,1);
					})
					
					angular.forEach(response.data.listObject,function(value,index){
						var temp = {templateId:value.templateId,title:value.title};
						$scope.UserSelectNursingStations.push(temp);
						$scope.UserNursingStationMppr[value.templateId] = true;
						$scope.selectTemplateServiceMapperList.push(temp);
					});
					
					angular.forEach(response.data.listObject,function(value,index){
						var temp = {specialityId:value.specialityId,subSpecialityId:value.subSpecialityId};
						
						$scope.UserNursingStationMppr[value.templateId] = true;
						
						$scope.list.push(temp);
						//$scope.searchGroupList($scope.list);
					});
					$scope.getSubSpecialityBySpeciality($scope.common.specialityId);
			  		$scope.getServiceBySubSpecilatity($scope.common.subSpecialityId);
					
					console.log("$scope.common",$scope.common);
				} 
				else if(response.data.status == 'error')
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
//		if ($scope.commonform.$valid)
//		{
			try {
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');		
				
				$rootScope.startLoader();
				$scope.initUserNursingStationMapper();
				$scope.listUserNursingStationMapper =
				{
						 templateId:$scope.common.templateId,
						 specialityId:$scope.common.specialityId,
						 serviceId:$scope.common.serviceId,
						 subSpecialityId:$scope.common.subSpecialityId,
					     organizationId:$scope.organizationId,
					     unitId:$scope.unitId,
					     status:"A",
						 createdBy:$scope.createdBy,
						 createdDate:$scope.dt,
						 updatedBy:$scope.updatedBy,
						 updatedDate:$scope.dt,
					     selectTemplateServiceMapperList:[]
				     
				 }
				angular.forEach($scope.selectTemplateServiceMapperList,function(value,index){
						$scope.listUserNursingStationMapper.selectTemplateServiceMapperList.push(value.templateId);
				})
				console.log("UserNursingStations",JSON.stringify($scope.listUserNursingStationMapper));
			
				var URI = BASE_URL + ROOT_URL +"/api/lis/global/updateTemplateServiceMapper";
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
						$scope.UserNursingStationMppr=[];
						$scope.selectTemplateServiceMapperList=[];
						$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
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
			
//		}
	}
	
	$scope.updateStatus = function(serviceId,status)
	{
		try {
			$rootScope.startLoader();
			$scope.initUserNursingStationMapper();
			var URI = BASE_URL + ROOT_URL +"/api/lis/global/updateTemplateServiceMapperStatus/"+serviceId+"/"+$scope.organizationId+"/"+$scope.unitId+"/"+status;
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
					$scope.selectTemplateServiceMapperList=[];
					$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
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