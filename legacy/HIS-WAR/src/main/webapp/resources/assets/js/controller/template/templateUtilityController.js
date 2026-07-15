(function(){
	'use strict';
	
	function templateUtilityController($scope, $rootScope, BillingGenericService,GenericServiceForFormsEngine, GenericService,growl, $cookies, $state,$timeout){
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
		
			var cookieObject = $cookies.getObject("cookieObject");
			if(cookieObject!=undefined){
			var authToken = cookieObject.authToken;
			}
			 $scope.accessToken=authToken;
			self.model.unitId = cookieObject.unitId;
			self.model.consentTypeId = "" ;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.template = {templateObject:{},templateFormatId:'',orgId:self.model.organizationId,unitId:self.model.unitId,createdBy:self.model.userId};
			self.model.getSpecialityList = getSpecialityList;
			self.model.getSubSpecialityBySpecialityId = getSubSpecialityBySpecialityId;
			self.model.dataObjectList = dataObjectList;
			self.model.getDoctorList = getDoctorList;
			self.model.getTemplateById = getTemplateById;
			self.model.getSpecialityList();
			self.model.dataObjectList();
			self.model.saveTemplate = saveTemplate;
			self.model.getDoctorList();
			self.model.type = 'all';
			self.model.templateTypes = [];
			self.model.getTemplateTypes = getTemplateTypes;
			self.model.getTemplateTypes();
			self.model.templateConfigObject = {};
			self.model.reset = reset;
			$scope.value = '';
			$scope.section3 = [];
			$scope.section4 = [];
			$scope.section5 = [];
			$scope.section6 = [];
			$scope.section9 = [];
			self.model.isHtmlPrint=false;
			
			
			//check if template is for update
			self.model.updateFlag = false;
			if($rootScope.updateTemplateId != null){
				self.model.getTemplateById($rootScope.updateTemplateId);
				self.model.updateFlag = true;
			}
			else{
				self.model.template.templateFormatId = 1;
			}
			getTemplateConfig();
		}
		
		var getTemplateConfig = function(){
			$rootScope.startLoader();
			var obj = {};
			obj.unitId = self.model.unitId;
			var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
			BillingGenericService.serviceAction(METHOD_POST,URI,obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 self.model.templateConfigObject = response.data.object;
					 getNewTemplateList();
					 getNewTemplateListForTabularTemplate();
					 $rootScope.stopLoader();
				}
				else if(response.data.status == "error")
				{
					 $rootScope.stopLoader();	
				}
			});
		}
		
		var getSpecialityList = function(){
			var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			var obj = {organizationId:self.model.organizationId, unitId:self.model.unitId};
			BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
                    // self.model.specialityList = response.data.listObject;
                    if (response.data.status == "success"){
                        var specialityListTemp = []
                        angular.forEach(response.data.listObject, function(value, key) {
                            if(value.isClinicalSpeciality == 'Y'){
                                specialityListTemp.push(value);
                            };
                        });
                        self.model.specialityList = specialityListTemp;
                    }
                }
			});
		}
		var getSubSpecialityBySpecialityId = function(specialityId){
			if(specialityId != ""){
				var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId"+"/"+self.model.organizationId+"/specialityId/"+specialityId;
				BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						self.model.subSpecialityList = response.data.listObject;
				});
				}
			}
		
		var dataObjectList = function(){
			var URI = BASE_URL + ROOT_URL + "/api/global/dataobjects/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					self.model.dataObjects = response.data.listObject;
			});
		}
		
		
		

		var getNewTemplateList = function(){
			       $rootScope.startLoader();
			       if(self.model.templateConfigObject != null){
			    	   if(self.model.templateConfigObject.apiKey != undefined && self.model.templateConfigObject.apiKey != null){
			    		   var  data={unitId:self.model.unitId};
							 var templateFormurl= BASE_URL + ROOT_URL + "/api/global/getTemplateListFormEngine";
							 GenericServiceForFormsEngine.serviceAction(METHOD_POST ,templateFormurl, data,self.model.templateConfigObject.apiKey).then(
									function(response) {
										if(response.data.status=="success"){
											$rootScope.stopLoader();
											if(response.data.object!=null){
											 $scope.newTemplateList=JSON.parse(response.data.object).results;
											}
										}
										else{
											$rootScope.stopLoader();
										}
									}); 
			    	   }
			    	   
			       }else{
			    	   $rootScope.stopLoader();
			       }
					
		}
		
		var getNewTemplateListForTabularTemplate = function(){
			var URI = BASE_URL + ROOT_URL + "/api/global/unit/template/tabular/getActiveTabularTemplateMaster"+"/"+ self.model.organizationId +"/"+ self.model.unitId;
			GenericService.serviceAction("GET",URI).then(function(response){
				
				if(response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.newTemplateListTabular = [];
					$scope.newTemplateListTabular = response.data.listObject;
					
					
				}else if(response.data.status == 'error'){
					$rootScope.stopLoader();
					growl.error(response.data.message,
							{
								title : 'Error!'
							});
				}
			});
		}
		
		$scope.setSectionObject = function(id,dataObject) {
			switch(id){
				case 'dynamicHtmlContent3':
					$scope.section3.push(dataObject);
					break;
				case 'dynamicHtmlContent4':
					$scope.section4.push(dataObject);
					break;
				case 'dynamicHtmlContent5':
					$scope.section5.push(dataObject);
					break;
				case 'dynamicHtmlContent6':
					$scope.section6.push(dataObject);
					break;
				case 'dynamicHtmlContent9':
					$scope.section9.push(dataObject);
					break;
			}
		};
		
		$scope.deleteDataObject = function(sectionId,randomId) {
			switch(sectionId){
				case 'dynamicHtmlContent3':
					index = $scope.section3.findIndex(x => x.randomId == randomId);
					$scope.section3.splice(index, 1);
					break;
				case 'dynamicHtmlContent4':
					index = $scope.section4.findIndex(x => x.randomId == randomId);
					$scope.section4.splice(index, 1);
					break;
				case 'dynamicHtmlContent5':
					index = $scope.section5.findIndex(x => x.randomId == randomId);
					$scope.section5.splice(index, 1);
					break;
				case 'dynamicHtmlContent6':
					index = $scope.section6.findIndex(x => x.randomId == randomId);
					$scope.section6.splice(index, 1);
					break;
				case 'dynamicHtmlContent9':
					index = $scope.section9.findIndex(x => x.randomId == randomId);
					$scope.section9.splice(index, 1);
					break;
			}
		};
		
		var saveTemplate = function(){
			if(self.model.template.title == "" || typeof(self.model.template.title) == "undefined"){
				growl.error("Please Enter a title",
						{
					title : 'Error!'});
				return;
			}
			
			 if(self.model.template.templateFormatId==1)
	    	  {
				var elem = angular.element(document.getElementById("dynamicHtmlContent3"));
				var parentObj = new Array();
				var siblingArray = new Array();
				var elements = document.getElementById("dynamicHtmlContent3").querySelectorAll('[ng-model]'); 
				for (var i = 0, element; element = elements[i++];) {
					var obj = element.getAttribute("ng-model");
					obj = obj.toString().split(".");
					var index = parentObj.indexOf(obj[0]);
					if(obj.length != 1){
					if(index == -1){
						parentObj.push(obj[0]);
					}
					}else{
						siblingArray.push(obj[0]);
					}
				}
				
				var childObjects = new Array();
				for (var i = 0, element; element = elements[i++];) {
					var obj = element.getAttribute("ng-model");
					obj = obj.toString().split(".");
					var index = parentObj.indexOf(obj[0]);
					childObjects[index] = typeof(childObjects[index]) != 'undefined' ? childObjects[index] :  [];
					if(obj.length == 3){
						if(obj[2] != "flag"){
							childObjects[index].push(obj[1] + "." + obj[2]);
						}
					}else{
						childObjects[index].push(obj[1]);
					}
				}
				
				 var formObj = {};
			      angular.forEach(parentObj, function(value, key) {
			           $scope.parentJson = {};
			          	angular.forEach(childObjects[key], function(value1, key1) {
			           	if(value1.includes(".val")){
			           		var element = document.querySelector('[ng-model="'+value+"."+value1.replace(".val",".flag")+'"]');
			           		$scope.parentJson[value1.replace(".val","")] = angular.copy({flag:element.value,list:"",val:""});
			           	}else{
			           		$scope.parentJson[value1] = '';
			           	}
			       });
			       formObj[value] = $scope.parentJson;
			   });
			      
			     
			    	   self.model.template.templateObject.html = document.getElementById("dynamicHtmlContent3").innerHTML;
			    	   if(self.model.template.templateObject.html==""){
					    	  growl.warning(
										'Add at least one data object!!!',
										{
											title : 'Warning!'
										});
							return false;
					      } 
				       self.model.template.templateObject.obj = formObj;			       
				       self.model.template.templateObject = JSON.stringify(self.model.template.templateObject);				   
				       //dilip
				       self.model.template.templateObjectHtml = document.getElementById("dynamicHtmlContent3").innerHTML;   
				       
				}
		      else if(self.model.template.templateFormatId==2)
		    	  {
			       self.model.template.templateObject = $scope.value;
		    	  }
		      else if(self.model.template.templateFormatId==3)
	    	  {
		       self.model.template.templateObject = '';
	    	  }
		      else if(self.model.template.templateFormatId==4)
	    	  {
		       self.model.template.templateObject = 'test';
		       self.model.template.formTemplateId = $scope.tabularTemplateId;
	    	  }
			 self.model.template.isHtmlPrint=self.model.isHtmlPrint;
		      //check template type and change ids
		      switch(self.model.type){
		      case 'all':
		    	  self.model.template.specialityId = null;
		    	  self.model.template.subSpecialityId = null;
		    	  self.model.template.doctorId = null;
		    	  break;
		      case 'subSpc':
		    	  self.model.template.doctorId = null;
		    	  break;
		      case 'doctor':
		    	  self.model.template.specialityId = null;
		    	  self.model.template.subSpecialityId = null;
		    	  break;
		      }
		      
		      $rootScope.startLoader();
		      if(!self.model.updateFlag){
		    	 //save template
				var URI = BASE_URL + ROOT_URL + "/api/global/template";
				BillingGenericService.serviceAction(METHOD_POST,URI, self.model.template, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					if(response.data.status == "success"){
						init();
						document.getElementById("dynamicHtmlContent3").innerHTML = "";
						$scope.value="";
					}
					$rootScope.stopLoader();
				});
		      }else if(self.model.updateFlag){
		    	  //update template
		    	  self.model.template.updatedBy = self.model.userId;
		    	  var URI = BASE_URL + ROOT_URL + "/api/global/template";
		    	  BillingGenericService.serviceAction(METHOD_PUT,URI, self.model.template, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
						if(response.data.status == "success"){
							$rootScope.updateTemplateId = null;
							$state.go("templates")
						}
						$rootScope.stopLoader();
					});
		      }	
		      
		      
		      if(self.model.template.templateTypeId == 1 && self.model.template.templateFormatId == 3){
		    	  
					$scope.currentDate = new Date();
					$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');

					var data = {
						consentMasterCode : self.model.template.formTemplateKeywords,
						consentMasterName : self.model.template.title,
						consentTypeMasterId : self.model.consentTypeId,
						status : 'A',
						createdBy : $scope.userId,
						updatedBy : $scope.userId,
						organizationId : self.model.organizationId,
						unitId :self.model.unitId,
						createdDate : $scope.currentDate,
						updatedDate : $scope.currentDate,
						templateType : 3,
						template : "",
						formTemplateId : self.model.template.formTemplateId
					}
							var URI = BASE_URL + ROOT_URL+ ADDCONSENT;
							BillingGenericService.serviceAction("POST", URI,data).then(
											function(response) {
												self.model.consentTypeId = "";
											/*	if (response.data.status == "success") {
														
														growl.success('Consent save Successfully...!!',
																{
																	title : 'Success!'
																});
												} else {
														growl.warning('Consent not save ...!!',
																	{
																		title : 'Warning!'
																	});
												}
*/
											});
					}
		      }
		      
		      
		
		
		var getDoctorList = function(){
			var param = {unitId : self.model.unitId,organizationId : self.model.organizationId};
			var URI = BASE_URL + ROOT_URL +"/adt/getActiveDoctorList";
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
					self.model.doctorList = response.data.listObject;
			});
		}
		
		var getTemplateById = function(templateId){
			var URI = BASE_URL + ROOT_URL +"/api/global/template/"+templateId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
				{
					self.model.template = response.data.object;
					self.model.isHtmlPrint=response.data.object.isHtmlPrint;
					$scope.tabularTemplateId=response.data.object.formTemplateId;
					self.model.template.templateFormatId =response.data.object.templateFormatId;
					if(self.model.template.subSpecialityId != null){
						self.model.getSubSpecialityBySpecialityId(self.model.template.specialityId);
						self.model.type = "subSpc";
					}else if(self.model.template.doctorId != null){
						self.model.type = "doctor";
					}
					
					if(response.data.object.templateFormatId==2 || response.data.object.templateFormatId==4)
						{
						  $timeout(function () {
							  $scope.value = response.data.object.templateObject;
								  } ,2000);
						}
					else{
						self.model.template.templateObject = JSON.parse(self.model.template.templateObject);
						animation('dataObject');
						document.getElementById("dynamicHtmlContent3").innerHTML = self.model.template.templateObject.html;
					}
				}
			});
		}
		
		var getTemplateTypes = function(){
			var URI = BASE_URL + ROOT_URL +"/api/global/template/types";
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					self.model.templateTypes = response.data.listObject;
				}
			});
		}
		 
		
		
		$scope.consentTypeList = [];
		
		$scope.getConsentType = function(templateType){
		
			if(templateType == 1){
				var URI = BASE_URL + ROOT_URL+ GETACTIVECONSENTTYPELIST+"/"+self.model.organizationId+"/"+self.model.unitId;
				BillingGenericService.serviceAction("GET", URI, "").then(
						function(response) {
							$scope.consentTypeList = [];
							if (response.data.status == "success") {
								$scope.consentTypeList = response.data.listObject;
							}
						});

			}
			
		}
		
		var reset = function(){
			self.model.template.templateFormatId = "";
			self.model.template.templateTypeId = "";
			self.model.template.title = "";
			self.model.template.specialityId = "";
			 self.model.template.subSpecialityId = "";
			 self.model.template.doctorId = "";
			//document.getElementById("dynamicHtmlContent3").innerHTML = "";
			$rootScope.updateTemplateId = null;
			init();
		}
		
		
		
		$scope.templateSelect = function(optionval){
	 		 $scope.searchText = optionval.templateName ;
	 		self.model.template.title= optionval.templateName ;
	 		self.model.template.formTemplateKeywords="-";
	 		if(optionval.keywords!=undefined){
	 			self.model.template.formTemplateKeywords= optionval.keywords.toString();
	 		}
	 		self.model.template.formTemplateId = optionval.uId;
		} 
	 	$scope.previewTemplate = function(){
	 		var landingUrl =  self.model.templateConfigObject.templateForm + self.model.template.formTemplateId +"&formType=digital";
	 		window.open( landingUrl,  '_blank'  );
//			$window.location.href = landingUrl; _blank
		}
	 	
	
	 	
	 	$scope.templateSelectTabular = function(optionval){
	 		 $scope.searchText = optionval.tabularTemplateDesc ;
	 		self.model.template.title= optionval.tabularTemplateDesc ;
	 		$scope.tabularTemplateId = optionval.tabularTemplateId;
	 		self.model.template.formTemplateKeywords="-";
	 		if(optionval.keywords!=undefined){
	 			self.model.template.formTemplateKeywords= optionval.keywords.toString();
	 		}
		} 
		
		init();
		getTemplateConfig();
		
		$scope.showpopup = function()
		{
			$('#prefinedDataHintModal').modal('show');
		}
		
		$scope.previewTemplateTabularTamplate = function()
		{
			$rootScope.startLoader();
			$scope.savedList=[];
			$scope.savedEditedList = [];
			$scope.rowList = [];
			$scope.columnList = [];
			$scope.columnHeaderList = [];
			$scope.savedEditedList = [];
			var URI = BASE_URL + ROOT_URL+ "/api/global/unit/template/tabular/mapper/getTabularTemplateDataMapperById/"+$scope.tabularTemplateId +"/"+self.model.organizationId+"/"+self.model.unitId;
			GenericService
			.serviceAction(METHOD_GET, URI)
			.then(
					function(response) {

						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							if(response.data.listObject != null && response.data.listObject.length > 0){
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
								
								console.log("$scope.rowList",$scope.rowList);
								console.log("$scope.columnList",$scope.columnList);
							}
	
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
	
	function config($stateProvider){
		$stateProvider.state("templateUtility", {
	    	url: '/templateUtility',
			templateUrl: 'views/template/template-utility.html',
			controller: "TemplateUtility.Controller",
			title: 'Create Clinical Form',
			resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 669);
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
	
	angular.module("myApp").config(config).controller('TemplateUtility.Controller',templateUtilityController)
})();