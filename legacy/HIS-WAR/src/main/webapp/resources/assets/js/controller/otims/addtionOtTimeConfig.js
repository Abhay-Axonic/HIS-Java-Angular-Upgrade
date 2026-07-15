(function(){
	'use strict';
	
	function additionalOtTimeConfiguration($scope, $rootScope, BillingGenericService, growl, $cookies, $state,PaginationService){
		var self = this;

		function init(){
			$rootScope.loginpage = true;
			self.model = {
			        pagination: {
	                    currentPage: 1,
	                    totalPages: 0,
	                    pageSize: '10',
	                    search: '',
	                    searchData: PaginationService.searchData,
	                    setTotalPages: PaginationService.setTotalPages,
	                    getPagesList: PaginationService.getPagesList,
	                    setPage: PaginationService.setPage,
	                    setFirstPage: PaginationService.setFirstPage,
	                    setPreviousPage: PaginationService.setPreviousPage,
	                    setNextPage: PaginationService.setNextPage,
	                    setLastPage: PaginationService.setLastPage,
	                    sortBy: PaginationService.sortBy
	                  }
			};
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
			self.model.getProcedureTypes = getProcedureTypes;
			self.model.getAddtionOtTimeConfigList = getAddtionOtTimeConfigList;
			self.model.editAddtionalOtTimeConfig = editAddtionalOtTimeConfig;
			self.model.saveAddOtTimeConfig = saveAddOtTimeConfig;
			self.model.changeStatus = changeStatus;
			self.model.updateFlag = false;
			self.model.otTimeConfig = 
			{
					additionalOtTimeId:"",
					orgId:self.model.organizationId,
					unitId:self.model.unitId,
					procedureTypeId:"",
					timeMin:"",
					rate:"",
					status:"A",
					createdBy:self.model.userId,
					createdDate:"",
					updatedBy:self.model.userId,
					updatedDate:"",
					}
			self.model.getProcedureTypes();
			self.model.getAddtionOtTimeConfigList();
		}
		
		
		var getProcedureTypes = function(){
			var URI = BASE_URL + ROOT_URL + "/api/procedure/types/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.procedureTypes = response.data.listObject;
			});
		}
		var getAddtionOtTimeConfigList = function(){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/otims/additionalOtTime/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					
					self.model.pagination.data = [];
					self.model.additionalOtTimeList = response.data.listObject;
					
					if( self.model.additionalOtTimeList.length > 0){            
                        PaginationService.searchData(self.model.additionalOtTimeList, self.model.pagination);
                      }
					
				$rootScope.stopLoader();
				}
				else if(response.data.status == "error")
				{
				$rootScope.stopLoader();
				}
			});
		}
		
		
		var saveAddOtTimeConfig = function()
		{
			$scope.$broadcast('show-errors-check-validity');
			 var URI ="";
			 if ($scope.additionalOtTimeConfigForm.$valid) {
			if(self.model.updateFlag)
				{
				URI = BASE_URL + ROOT_URL + "/api/otims/updateAdditionalOtTime";
				self.model.otTimeConfig.updatedBy = self.model.userId ;
				self.model.otTimeConfig.orgId = self.model.organizationId ;
				self.model.otTimeConfig.unitId = self.model.unitId ;
				}
			else{
				URI=  BASE_URL + ROOT_URL + "/api/otims/additionalOtTime";
			}
		
/*			self.model.procConf.multidelete("service");*/
			var method =  METHOD_POST;
			$rootScope.startLoader();
			BillingGenericService.serviceAction(method,URI,self.model.otTimeConfig,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					{
					  init();
					  $rootScope.stopLoader();
					  $scope.$broadcast('show-errors-reset');
					}
				else if(response.data.status == "error")
					{
					$rootScope.stopLoader();
					}
					 
				
			});
			 }
		}
		
		var changeStatus = function(id,status)
		{
			var URI = BASE_URL + ROOT_URL + "/api/otims/updateStatusAdditionalOtTime";
			var data = {
					additionalOtTimeId:id,
					status : status
					
			}
			$rootScope.startLoader();
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				$rootScope.stopLoader();
			});
		}
		
		var editAddtionalOtTimeConfig = function(addtionalOtTimeConfig){
			self.model.otTimeConfig = angular.copy(addtionalOtTimeConfig);
			self.model.updateFlag = true;
		}
		
		init();
	}
	
	function config($stateProvider){
		$stateProvider.state('additionalOtTimeConfiguration', {
			 url: '/additionalOtTimeConfiguration',
			 templateUrl: 'views/master/GlobalMaster/OTIMS/addtionalOtTimeConfig.html',
			 controller: "addtionalOtTimeConfig.Controller",
			 title: 'Additional OT Time Configuration',
			 controllerAs : "vm"
		})
	}
	
	angular.module("myApp").config(config).controller("addtionalOtTimeConfig.Controller",additionalOtTimeConfiguration);
})();