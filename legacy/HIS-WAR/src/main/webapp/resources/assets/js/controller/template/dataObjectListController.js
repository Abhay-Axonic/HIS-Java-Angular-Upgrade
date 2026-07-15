(function(){
	'use strict';
	
	function dataObjectListController($scope, $rootScope, BillingGenericService, growl, $cookies, $state){
		var self = this;

		function init(){
			$rootScope.loginpage = true;
			self.model = {specialityId:"0",subSpecialityId:"0"};
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
			self.model.dataObjectList=dataObjectList;
			self.model.updateStatus=updateStatus;
			self.model.viewDataObject = viewDataObject;
			//self.model.filterList = filterList;	 	
			//self.model.deactivateTemplate = deactivateTemplate;
			self.model.editDataObject = editDataObject;
			self.model.dataObjectList();
			$scope.value = '';
			self.model.listDataObject=[];
		}
		
		var updateStatus = function(dataObjectId,status){
			$rootScope.startLoader();
			var data={
					orgId:self.model.organizationId,
					unitId:self.model.unitId,
					dataObjectId:dataObjectId,
					status:status
			}
			var URI = BASE_URL + ROOT_URL + "/api/global/updateDataObjectStatus";
			BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					{
					self.model.listDataObject = response.data.listObject;
					growl.success("Status Updated Successfully",
							{
								title : 'Success!'
							});

					$rootScope.stopLoader();
					init();
					}
				else if(response.data.status == "error"){
					$rootScope.stopLoader();
					init();
				}
					
			});
		}
		
		var dataObjectList = function(){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/global/getAllDataObject/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					{
					self.model.listDataObject = response.data.listObject;
					$rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					$rootScope.stopLoader();
				}
					
			});
		}
	
		var viewDataObject = function(template)
		{
				document.getElementById("htmlContent").innerHTML = JSON.parse(template.dataObject).html;
			
		}
		
		
		var editDataObject = function(dataObject){
			$state.go('dataObject',{dataObjectObj:dataObject});
		}
		
		init();
	}
	
	function config($stateProvider){
		$stateProvider.state("clinicalSectionSummary", {
	    	url: '/clinicalSectionSummary',
			templateUrl: 'views/template/data-object-list.html',
			controller: "DataObjectList.Controller",
			title: 'Clinical Section Summary',
			controllerAs: "vm"
	    });
	}
	
	angular.module("myApp").config(config).controller("DataObjectList.Controller",dataObjectListController);
})();