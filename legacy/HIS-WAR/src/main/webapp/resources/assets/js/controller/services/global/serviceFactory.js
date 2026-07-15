angular.module("myApp").factory('servicesFactory',['$rootScope','BillingGenericService','growl',function($rootScope,BillingGenericService,growl){
	var factory = {};
	
	factory.searchServices = function(searchObj,scope)
	{
		var packageObj = scope.packageObj;
		if(typeof(scope.packageObj) != "undefined"){
		if(scope.packageObj.packageTypeId != "" && typeof(scope.packageObj.packageTypeId) != "undefined"){
			searchObj.patientTypeId = packageObj.patientTypeId;
			if(scope.packageObj.sexId!==undefined && scope.packageObj.sexId!=="")
				{
				  searchObj.sexId = packageObj.sexId;
				}
			searchObj.patientTypeId = packageObj.patientTypeId;
		  }
		}
		
		scope.startLoaderPopup();
		var URI = BASE_URL + ROOT_URL + "/api/packages/searchServicesForPackage";
		searchObj.orgId = scope.orgId;
		searchObj.unitId= scope.unitId;
		BillingGenericService.serviceAction(METHOD_POST,URI, searchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			scope.searchPatientList = [];
			if (response.data.status == "success")
				{
				 scope.searchServiceList = response.data.listObject;
			     searchObj = {};
			     scope.stopLoaderPopup();
				}
			else if(response.data.status == "error")
				{
				  scope.stopLoaderPopup();
				  searchObj = {};
				}
		});
	}
	
	factory.initServiceSearch = function(scope){
		var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
		var obj = {organizationId:scope.orgId, unitId:scope.unitId};
		BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			scope.searchPatientList = [];
			if (response.data.status == "success")
				scope.specialityList = response.data.listObject;
		});
	}
	
	factory.getSubSpecialityBySpecialityId = function(specialityId,scope){
		if(specialityId != ""){
		var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+scope.orgId+"/unitId/"+scope.unitId+"/specialityId/"+specialityId;
		BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			scope.searchPatientList = [];
			if (response.data.status == "success")
				scope.subSpecialityList = response.data.listObject;
			scope.searchServObj.subGroupId = "";
		});
		}
	}
	
	factory.selectAllServices = function(flag,scope){
		var scope = angular.element(document.getElementById('uiView')).scope();
			angular.forEach(scope.searchServiceList,function(value,key){
				value.isSelected = flag;
			});
		}
	
	factory.setOrgUnit = function(orgId,unitId,scope){
		var scope = angular.element(document.getElementById('uiView')).scope();
		scope.orgId = orgId;
		scope.unitId = unitId;
	}
	
	//for search packages 
	factory.searchPackageForBilling = function(searchObj,scope){
		var packageObj = scope.packageObj;
		if(typeof(scope.packageObj) != "undefined"){
		if(scope.packageObj.packageTypeId != "" && typeof(scope.packageObj.packageTypeId) != "undefined"){
			searchObj.patientTypeId = packageObj.patientTypeId;
			searchObj.visitTypeId = packageObj.visitTypeId;
			searchObj.billingBedCategoryId = packageObj.billingBedCategoryId;
			searchObj.sexId = (packageObj.sexId!=null &&  packageObj.sexId!='' ? packageObj.sexId : 0 );
			searchObj.packagetypeId=scope.packageObj.packageTypeId ;			
			}
		}		
		scope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/search";
//		searchObj.orgId = scope.orgId;
//		searchObj.unitId= scope.unitId;
		return BillingGenericService.serviceAction("POST", URI, searchObj).then(function(response){
			if(response.data.status == "success")
				 scope.searchPackageList=[];
				return scope.searchPackageList = response.data.listObject;
		});
	}
	
	//for search packages 
	factory.searchPackageForEHC = function(searchObj,scope){
		var packageObj = scope.packageObj;
		scope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/parentPackage/search";
		return BillingGenericService.serviceAction("POST", URI, searchObj).then(function(response){
			if(response.data.status == "success")
				 scope.searchPackageList=[];
				return response.data.listObject;
		});
	 }
	// for searching auto complete group details
	factory.initSpecailityGroupSearch = function(scope){
		var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
		var obj = {organizationId:scope.orgId, unitId:scope.unitId};
		BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			scope.specialityGroupList = [];
			if (response.data.status == "success")
				scope.specialityGroupList = response.data.listObject;
		});
	}
	return factory;
}]);