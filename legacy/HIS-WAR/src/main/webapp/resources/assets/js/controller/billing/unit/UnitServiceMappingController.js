/**
 * @author Nikhil
 * @date 20-12-2017
 * 		Controller to Mapping Org level Service Master to Unit (Unit Level)
 */
angular.module("myApp").controller("UnitServiceMappingController", [ '$scope','$rootScope','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject','growl',
	function($scope,$rootScope,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject,growl){
	
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	$rootScope.loginpage = true;
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.specialityMasterList=[];
	$scope.subSpecialityMasterList=[];
	$scope.servicesList =[];
	$scope.selectedServiceList = [];
	$scope.tempServiceList = [];
	$scope.serviceMst=[];
	$scope.unitService={};
	
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.unitServiceMapperList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.page;
	$scope.userId = cookieObject.userId;
	$scope.isChecked = false;
	
	
	//------------ Get Speciality Master List ------------//
	$scope.getSpecialityMasterList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
			var obj = {
						organizationId : $scope.organizationId,
						unitId         :$scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.specialityMasterList = response.data.listObject;
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getSpecialityMasterList();
	
	//------------ Get SubSpeciality By SpecialityId ----------//
	/*$scope.getSubSpecialityBySpecialityId = function(specialityId){
		$rootScope.startLoader();
		$scope.unitService.subSpecialityId="";
		$scope.servicesList=[];
		var URI = BASE_URL + ROOT_URL + GET_SUBSPECIALITY_BY_SPECIALITY_ID;
		var obj = {
					organizationId : $scope.organizationId,
					unitId         :$scope.unitId,
					specialityId   :specialityId
			    }
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				$scope.subSpecialityMasterList = response.data.listObject;
			console.log("subSpecialityMasterList",$scope.subSpecialityMasterList);
		});
	}*/
	$scope.getSubSpecialityBySpecialityId = function(specialityId){
		if(specialityId != null){
			$scope.subSpecialityMasterList= [];
		var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/specialityId/"+specialityId;
		BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.subSpecialityMasterList= [];
			if (response.data.status == "success")
				$scope.subSpecialityMasterList = response.data.listObject;
		});
		}
	}
	
	//------------- Get All Services by Speciality and SubSpeciality ---------------//
	$scope.getServicesBySpecialityAndSubSpeciality = function(specialityId, subSpecialityId){
		try{
			if(!isNaN(subSpecialityId)){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + BILLING + ORGANIZATION_MASTER + SPECIALITY + SLASH + specialityId + SUB_SPECIALITY + SLASH + subSpecialityId + ORG + SLASH + $scope.organizationId + UNIT + SLASH + $scope.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
					$scope.servicesList = response.data.listObject;
				console.log("servicesList",$scope.servicesList);
			});
			}else{
				growl.warning("please select sub speciality",{
					title : WARNING_MSG
				});
			}
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	
	function uncheckServiceListList(){
		angular.forEach($scope.servicesList,function(value,index){
			$scope.serviceMst[value.serviceMasterId] = false;
		})
	}
	
	$scope.selectServices = function(){
		$scope.selectedServiceList = [];
		angular.forEach($scope.servicesList,function(value,index){
			if($scope.serviceMst[value.serviceMasterId] == true){
				var temp = {serviceId:value.serviceMasterId,serviceStandardCode:value.serviceStandardCode,serviceStandardName:value.serviceStandardName};
				$scope.selectedServiceList.push(temp)
			}
		});
//		$scope.selectedServiceList = angular.copy($scope.tempServiceList);
	}
	
	//--------------- Select Specuality --------------//
	$scope.addTempService = function(serviceMasterId, serviceStandardCode,serviceStandardName){
		var flag = false;
		angular.forEach($scope.tempServiceList,function(value,index){
			if(value.serviceId == serviceMasterId){
				$scope.tempServiceList.splice(index,1);
				$scope.serviceMst[value.specialityId] = false;
				flag = true;
			}
		})
		if(!flag){
			var temp = {serviceId:serviceMasterId,serviceStandardCode:serviceStandardCode,serviceStandardName:serviceStandardName};
			$scope.tempServiceList.push(temp);
		}
		console.log("$scope.selectedServiceList ",$scope.selectedServiceList);
		console.log("$scope.tempServiceList ",$scope.tempServiceList);
	}
	
	$scope.saveUnitServiceMapper = function(){
		try{
				if($scope.selectedServiceList.length > 0){
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + GLOBAL + UNIT_SERVICE_MAPPER;
					var obj={
							"listUnitServiceMapperDto":$scope.selectedServiceList,
							"orgnisationId":$scope.organizationId,
							"status":"A",
							"unitId": $scope.unitId,
							"createdBy" : $scope.userId,
							"updatedBy" :  $scope.userId,
							"createdDate" : new Date().getTime(),
							"updatedDate" : new Date().getTime()
					}
					console.log("save",obj);
					BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
						if(response.data.status == "success"){
							//$scope.getServicesBySpecialityAndSubSpeciality($scope.unitService.specialityId,$scope.unitService.subSpecialityId);
							$scope.selectedServiceList = [];
							$scope.tempServiceList = [];
							uncheckServiceListList();
							$scope.unitService={
									specialityId:"",
									subSpecialityId:"",
							};
							$scope.servicesList = [];
							$scope.initUnitServiceMapperList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
						}
					});
				}else{
					growl.warning("please select at least one service",{
						title : WARNING_MSG
					});
				}
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	
	
	$scope.changeUnitServiceMapperStatus = function(serviceId,status){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + GLOBAL + UNIT_SERVICE_MAPPER + "/serviceId"+ SLASH + serviceId + STATUS + SLASH + status + UNIT + SLASH + $scope.unitId + ORG + SLASH + $scope.organizationId ;
		BillingGenericService.serviceAction(METHOD_PUT,URI, "", NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			if(response.data.status == "success"){
				
			}
		});
	}
	
	//======================= Unit Service Mapper Details List Pagination (Start)=====================//
	$scope.setNoOfRecords = function() {
		$scope.initUnitServiceMapperList($scope.organizationId, $scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
	};
	
	$scope.initUnitServiceMapperList = function(orgId, unitId, offset, noOfRecordsPerPage){
		$rootScope.startLoader();
		var obj = "";
		offset = offset != null ? offset : 0;
		noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
		var URI_TO_GET_UNIT_SERVICE_MAPPER_LIST = BASE_URL + ROOT_URL + GLOBAL + UNIT_SERVICE_MAPPER + UNIT + SLASH + unitId + ORG + SLASH + orgId  + SLASH + offset + SLASH + noOfRecordsPerPage;
		var URI_TO_GET_UNIT_SERVICE_MAPPER_COUNT = BASE_URL + ROOT_URL + GLOBAL + UNIT_SERVICE_MAPPER + COUNT + UNIT + SLASH + unitId + ORG + SLASH + orgId;
		promiseFactory.setPromises(URI_TO_GET_UNIT_SERVICE_MAPPER_LIST, URI_TO_GET_UNIT_SERVICE_MAPPER_COUNT, "GET", "GET").then(function(response) 
		{
			$rootScope.stopLoader();
			$scope.unitServiceMapperList = response[0].listObject;
			$scope.commonListCount = response[1].object;
			$scope.setPage(1, false);
		});
	}
	
	$scope.getUnitServiceMapperList = function(orgId, unitId, offset, noOfRecordsPerPage){
		$rootScope.startLoader();
		var obj = "";
		offset = offset != null ? offset : 0;
		noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
		var URI =  BASE_URL + ROOT_URL + GLOBAL + UNIT_SERVICE_MAPPER + UNIT + SLASH + unitId + ORG + SLASH + orgId  + SLASH + offset + SLASH + noOfRecordsPerPage;
		console.log("URI", URI);
		BillingGenericService.serviceAction(METHOD_GET, URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$rootScope.stopLoader();
			$scope.unitServiceMapperList = response.data.listObject;
			console.log($scope.unitServiceMapperList);
		});
	}
	
	$scope.serachunitServicemapping = function(searchKeyword){
		//orgId, unitId, offset, noOfRecordsPerPage
		$scope.loadingMsg="";
		if(searchKeyword!=''){
			
			var orgId =$scope.organizationId;
			var unitId=$scope.unitId;
			$scope.unitServiceMapperList=[];
			$scope.loadingMsg="Loading...";
		var URI =  BASE_URL + ROOT_URL + GLOBAL + UNIT_SERVICE_MAPPER  + SLASH + unitId  + SLASH + orgId  + SLASH + searchKeyword;
		console.log("URI", URI);
		GenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			
			$scope.unitServiceMapperList = response.data.listObject;
			if($scope.unitServiceMapperList.length==0)
			$scope.loadingMsg="No record found...";
//			console.log($scope.unitServiceMapperList);
		});
		}else{
			
			$scope.initUnitServiceMapperList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
		}
	}
	$scope.setPage = function(page, flag) {
		if (page < 1 || page > $scope.pager.totalPages)
		{
			return;
		}
		$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
		if (flag)
		{
			$scope.getUnitServiceMapperList($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
		}
	}
	$scope.initUnitServiceMapperList($scope.organizationId,$scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);
	//=================== Unit Service Mapper Pagination (End)====================//
	
/*	$scope.getAllServices=function(){
			if($scope.chkAll){		
			$scope.selectedServiceList=[];			
				angular.forEach($scope.servicesList,function(value,index){
					$scope.addTempService(value.serviceMasterId,value.serviceStandardCode,value.serviceStandardName);	
					$scope.selectedServiceList.push(value.serviceMasterId);			
					$scope.selectedServiceList.indexOf(service.serviceMasterId) != -1;
				});
			}else{
				angular.forEach($scope.servicesList,function(value,index){
					$scope.addTempService(value.serviceMasterId,value.serviceStandardCode,value.serviceStandardName);	
					$scope.selectedServiceList=[];		
				});
			}
			
		}*/

	
	$scope.enableDisableAll =function(){
		console.log("$scope.isChecked ",$scope.isChecked);
			angular.forEach($scope.servicesList,function(value,index){
				$scope.serviceMst[value.serviceMasterId] = $scope.isChecked;
			if($scope.serviceMst[value.serviceMasterId])
					$scope.addTempService(value.serviceMasterId,value.serviceStandardCode,value.serviceStandardName);
			
			});
		
	}
	
}]);