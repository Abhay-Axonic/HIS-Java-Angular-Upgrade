(function(){
	'use strict';
	
	function templateListController($scope, $rootScope, BillingGenericService, GenericService,growl, $cookies, $state){
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
			self.model.templateList = templateList;
			self.model.getSpecialityList = getSpecialityList;
			self.model.getSubSpecialityBySpecialityId = getSubSpecialityBySpecialityId;
			self.model.viewTemplate = viewTemplate;
			self.model.filterList = filterList;	 	
			self.model.deactivateTemplate = deactivateTemplate;
			self.model.editTemplate = editTemplate;
			self.model.templateConfigObject = {};
			self.model.templateList();
			self.model.getSpecialityList();
			$scope.value = '';
		}
		
		
		var getTemplateConfig = function(){
			$rootScope.startLoader();
			var obj = {};
			obj.unitId = self.model.unitId;
			var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
			BillingGenericService.serviceAction(METHOD_POST,URI,obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 self.model.templateConfigObject = response.data.object;
					 $rootScope.stopLoader();
				}
				else if(response.data.status == "error")
				{
					 $rootScope.stopLoader();	
				}
			});
		}
		$scope.previewTemplate = function(formTemplateId){

		
		} 
		
		var templateList = function(){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/global/templates/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					{
					self.model.templates = response.data.listObject;
					self.model.filteredTemplates = angular.copy(self.model.templates);
					$rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					$rootScope.stopLoader();
				}
					
			});
		}
		
		var getSpecialityList = function(){
			var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			var obj = {organizationId:self.model.organizationId, unitId:self.model.unitId};
			BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.specialityList = response.data.listObject;
			});
		}
		
		var getSubSpecialityBySpecialityId = function(specialityId){
			if(specialityId != "0"){
				var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+self.model.organizationId+"/specialityId/"+specialityId;
				BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						self.model.subSpecialityList = response.data.listObject;
					self.model.subSpecialityId = "0";
					self.model.filterList();
				});
				}else{
					self.model.subSpecialityId = "0";
					self.model.subSpecialityList = [];
					self.model.filterList();
					
				}
			}
		
		var filterList = function(){
			$rootScope.startLoader();
			var temp = [];
			self.model.templates.forEach((elem) => {
				if(self.model.subSpecialityId != "0" && (elem.subSpecialityId == self.model.subSpecialityId)){
					temp.push(elem);
				}else if ((self.model.specialityId != "0" && self.model.subSpecialityId == "0") && (self.model.specialityId == elem.specialityId)){
					temp.push(elem);
				}else if(self.model.specialityId == "0" && self.model.subSpecialityId == "0"){
					temp.push(elem);
				}
			});
			self.model.filteredTemplates = angular.copy(temp);
			$rootScope.stopLoader();
		}
		
		var viewTemplate = function(template)
		{
			$scope.templateFormatId = template.templateFormatId;
			if(template.templateFormatId==1)
				{
				 document.getElementById("htmlContent").innerHTML = JSON.parse(template.templateObject).html;
				 $('#viewTemplateModal').modal('show');
				}
			else if(template.templateFormatId==2)
				{
				 $scope.value = template.templateObject;
				 $('#viewTemplateModal').modal('show');
				}
			else if(template.templateFormatId==3)
			{
				var landingUrl =  self.model.templateConfigObject.templateForm + template.formTemplateId +"&formType=digital";
		 		window.open( landingUrl,  '_blank'  );
			}else if(template.templateFormatId==4)
			{

				$rootScope.startLoader();
				$scope.savedList=[];
				$scope.savedEditedList = [];
				var URI = BASE_URL + ROOT_URL+ "/api/global/unit/template/tabular/mapper/getTabularTemplateDataMapperById/"+parseInt(template.formTemplateId) +"/"+self.model.organizationId+"/"+self.model.unitId;
				GenericService
				.serviceAction(METHOD_GET, URI)
				.then(
						function(response) {

							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								$scope.rowList = [];
								$scope.columnList = [];
								$scope.columnHeaderList = [];
								$scope.savedEditedList = [];
								$scope.savedEditedList = response.data.listObject;
								$scope.comObj = {};
								$scope.savedList = response.data.listObject;
								angular.forEach($scope.savedEditedList,function(value1,key1){
									value1.organizationId=$scope.organizationId;
									value1.unitId=$scope.unitId;
									value1.createdBy=$scope.userId;
									value1.updatedBy=$scope.userId;
									value1.status='A';
									value1.createdDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									value1.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								});
								console.log("$scope.columnList",$scope.columnList);
								$scope.comObj.numberOfRow = response.data.listObject[0].numberOfRow;
								$scope.comObj.numberOfCoulmn = response.data.listObject[0].numberOfCoulmn;
								$scope.tableName = response.data.listObject[0].tableName;
								
									for(var i = 0; i < $scope.comObj.numberOfRow ;i++){
										var newArr = {};
										newArr.rowId = i;
										newArr.coulmnId = i;
										newArr.rowName = '';
										newArr.tabularTemplateId = $scope.tabularTemplateId;
										$scope.rowList.push(newArr);
								     }
									for(var i = 0; i < $scope.comObj.numberOfCoulmn ;i++){
										var newArr = {};
										newArr.coulmnId = i;
										newArr.rowId = i;
										newArr.columnName = '';
										newArr.inputValue = '';
										newArr.tabularTemplateId = $scope.tabularTemplateId;
										$scope.columnList.push(newArr);
										$scope.columnHeaderList.push(newArr);
								     }
									$scope.tableName = response.data.listObject[0].tableName;
									angular.forEach($scope.rowList,function(value,key){
										angular.forEach($scope.savedEditedList,function(value1,key1){
											if(value.rowId == value1.rowId){
												value.rowName = value1.rowName;
											}
										});
									});
									angular.forEach($scope.columnList,function(value,key){
										angular.forEach($scope.savedEditedList,function(value1,key1){
											if(value.coulmnId == value1.coulmnId){
												value.columnName = value1.columnName;
											}
										});
									});
									angular.forEach($scope.columnHeaderList,function(value,key){
										angular.forEach($scope.savedEditedList,function(value1,key1){
											if(value.coulmnId == value1.coulmnId){
												value.columnName = value1.columnName;
											}
										});
									});
									$('#viewTemplateModal').modal('show');
									console.log("$scope.rowList",$scope.rowList);
									console.log("$scope.columnList",$scope.columnList);
		
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
			
		}
		
		var deactivateTemplate = function(templateId){
			var URI = BASE_URL + ROOT_URL + "/api/global/template/"+templateId+"/I";
			BillingGenericService.serviceAction(METHOD_PATCH,URI, null,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					self.model.templateList();
				$("#confirmRemoveTemplate").hide();
			});
		}
		
		var editTemplate = function(templateId){
			$rootScope.updateTemplateId = templateId;
			$state.go('templateUtility');
		}
		
		init();
		getTemplateConfig();
		
		$scope.templateId = "";
		$scope.tamplateName = "";
		$scope.deactivateOpenPopup = function(obj){
			$scope.templateId = obj.templateId;
			$scope.tamplateName = obj.title;
			$("#confirmRemoveTemplate").show();
		}
		
		$scope.clearTemplateDerails = function(){
			$scope.templateId = "";
			$scope.tamplateName = "";
			$("#confirmRemoveTemplate").hide();
		}
	}
	
	function config($stateProvider){
		$stateProvider.state("templates", {
	    	url: '/templates',
			templateUrl: 'views/template/template-list.html',
			controller: "TemplateList.Controller",
			title: 'Clinical Form Summary',
			resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 670);
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
			controllerAs: "vm"
	    });
	}
	
	angular.module("myApp").config(config).controller("TemplateList.Controller",templateListController);
})();