(function(){
	'use strict';
	
	function otBillCompletionController($scope, $rootScope, BillingGenericService, growl, $cookies,dataService,$state){
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			self.model = {searchObj : {
					fromDate: new Date(),
			        toDate: new Date(),
			        fromDateOptions: {
			          formatYear: 'yyyy',
			          showWeeks: false,
			          maxDate: new Date()
			        },
			        toDateOptions: {
			          formatYear: 'yyyy',
			          showWeeks: false,
			          maxDate: new Date()
			        }
			        }};
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
			self.model.searchObj.unitId = cookieObject.unitId;
			self.model.searchObj.orgId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.searchOtBill = searchOtBill;
			self.model.OpenFromDate = OpenFromDate;
		    self.model.OpenToDate = OpenToDate;
		    self.model.getActiveProcedureStatusList = getActiveProcedureStatusList;
		    self.model.setProcedureOrderId = setProcedureOrderId;
		    
		    self.model.getActiveProcedureStatusList();
		}
		var OpenFromDate = function () {
	      self.model.fromDateOpened = true;
	      //self.model.searchObj.toDateOptions.minDate = self.model.fromDate;
	    }

	    var OpenToDate = function () {
	      self.model.toDateOpened = true;
	      //self.model.searchObj.toDateOptions.minDate = self.model.fromDate;
	    }
		var searchOtBill = function(){
			self.model.searchObj.fromDate=moment(self.model.searchObj.fromDate).format('YYYY-MM-DD');
			self.model.searchObj.toDate=moment(self.model.searchObj.toDate).format('YYYY-MM-DD');
			self.model.searchObj.visitType=self.model.searchObj.visitTypeId;
			var URI = OTIMS_ROOT_URL +"/hisProcedureDetails?fromDate="+ self.model.searchObj.fromDate + "&toDate=" + self.model.searchObj.toDate + "&visitType=" + self.model.searchObj.visitType;
			
			var procedureType={};
			/*var searchObjCopy = angular.copy(self.model.searchObj);		
			searchObjCopy.multidelete("fromDateOptions" ,"toDateOptions");
			, searchObjCopy,NOTIFICATION_MSG_STATUS_FALSE*/
			BillingGenericService.serviceAction("GET",URI,{}).then(function(response) {
					if (response.data.status == "success"){
						self.otBillCompletionList = response.data.listObject;
					
					self.otBillCompletionList.forEach(function (value, key) {
						value.type.forEach(function(name){
							if(procedureType[value.id] == null)
								procedureType[value.id] = name;
							else
								procedureType[value.id] = procedureType[value.id] + "-" + name;
						});
					});
					
			 }
			});
		}
		var setProcedureOrderId=function(id)
		{
			$scope.procedureOrderId = id;
			var URI = BASE_URL + ROOT_URL +"/api/procedure/order/details/"+ $scope.procedureOrderId ;
			BillingGenericService.serviceAction("GET",URI,{}).then(function(response) {
					if (response.data.status == "success")
						self.otBillCompletionDetails = response.data.listObject;
					
				});
			
		}
		
		var getActiveProcedureStatusList = function(){
			var URI = BASE_URL + ROOT_URL +"/api/otims/getActiveProcedureOrderStatus";
			var obj = {};
			BillingGenericService.serviceAction("GET",URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.getActiveProcedureStatus = response.data.listObject;
			});
		}
		init();
	}
	
	
	/*
	var updateOTIMS = function(){
		
		self.model.searchObj.visitType=self.model.searchObj.visitTypeId;
		var URI = OTIMS_ROOT_URL +"/api/hisAcknowledge?id=313d99f6-e02d-43c9-a645-54270ac83d85";
	
		$http({
			            method: 'GET',
			             url: URI,
			             headers : { 'Content-Type' : 'application/json' }
			           
			       }).then(function(response) {
			    	   console.log("Response="+response);
		});
	}
	*/
	function config($stateProvider){
		$stateProvider.state('OTBillingCompletion', {
			url: '/OTBillingCompletion',
			templateUrl: 'views/master/TheatreManagement/OT-billing-completion.html',
			controller: "OTBillingCompletion.Controller",
			controllerAs : "vm"
		})
	}
	
	angular.module("myApp").config(config).controller("OTBillingCompletion.Controller",otBillCompletionController);
	
})();