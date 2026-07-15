/*(function(){
	'use strict';
	
	function procedureConfigController($scope, $rootScope, BillingGenericService, growl, $cookies, $state){
		var self = this;

		function init(){
			$rootScope.loginpage = true;
			self.model = {};
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			self.model.unitId = cookieObject.unitId;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.getSpecialityList = getSpecialityList;
			self.model.getProcedureTypes = getProcedureTypes;
			self.model.getAnesthesiaTypes = getAnesthesiaTypes;
			self.model.getProcedureConfList = getProcedureConfList;
			self.model.searchProcServices = searchProcServices;
			self.model.selectProcService = selectProcService;
			self.model.savePeriodicityServConf = savePeriodicityServConf;
			self.model.editProcConf = editProcConf;
			self.model.changeStatus = changeStatus;
			self.model.updateFlag = false;
			self.model.procConf = {orgId:self.model.organizationId,unitId:self.model.unitId,createdBy:self.model.userId};
			self.model.getSpecialityList();
			self.model.getProcedureTypes();
			self.model.getAnesthesiaTypes();
			self.model.getProcedureConfList();
		}
		
		var getSpecialityList = function(){
			var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			var obj = {organizationId:self.model.organizationId, unitId:self.model.unitId};
			BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.specialityList = response.data.listObject;
			});
		}
		
		var getProcedureTypes = function(){
			var URI = BASE_URL + ROOT_URL + "/api/procedure/types/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.procedureTypes = response.data.listObject;
			});
		}
		
		var getAnesthesiaTypes = function(){
			var URI = BASE_URL + ROOT_URL + "/api/procedure/anesthesia/types/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.anesthesiaTypes = response.data.listObject;
			});
		}
		
		var getProcedureConfList = function(){
			var URI = BASE_URL + ROOT_URL + "/api/procedure/configs/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.procedureConfigs = response.data.listObject;
			});
		}
		
		var searchProcServices = function(serviceName){
			var URI = BASE_URL + ROOT_URL + "/api/global/search/services/procedure/"+self.model.organizationId+"/"+self.model.unitId+"/"+serviceName;
			return BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					return response.data.listObject;
			});
		}
		
		var selectProcService = function(item, model, label){
			self.model.procConf.serviceId = item.serviceId;
		}
		
		var savePeriodicityServConf = function(){
			var URI = BASE_URL + ROOT_URL + "/api/procedure/config";
			self.model.procConf.multidelete("service");
			var method = self.model.updateFlag ? METHOD_PUT : METHOD_POST;
			self.model.procConf.updatedBy = method == METHOD_PUT ? self.model.userId : null;
			$rootScope.startLoader();
			BillingGenericService.serviceAction(method,URI,self.model.procConf,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					 init();
				$rootScope.stopLoader();
			});
		}
		
		var changeStatus = function(id,status){
			var URI = BASE_URL + ROOT_URL + "/api/procedure/config/status/"+id+"/"+status;
			$rootScope.startLoader();
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				$rootScope.stopLoader();
			});
		}
		
		var editProcConf = function(procConf){
			self.model.procConf = angular.copy(procConf);
			self.model.updateFlag = true;
		}
		
		init();
	}
	
	function config($stateProvider){
		$stateProvider.state('procedureConfiguration', {
			 url: '/procedureConfiguration',
			 templateUrl: 'views/master/GlobalMaster/OTIMS/procedure-configuration-master.html',
			 controller: "ProcedureConfig.Controller",
				resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 515);
		   				 	});
		   					if(arr.length <= 0){
		   						$state.go("accessDenied")
		   						deferred.reject();
		   					 } else {
		   						deferred.resolve();
		   					 }
		   		        });
		   		
		   		        return deferred.promise;
		   		      }
		   		 },
			 controllerAs : "vm"
		})
	}
	
	angular.module("myApp").config(config).controller("ProcedureConfig.Controller",procedureConfigController);
})();*/