(function(){
	'use strict';
	
	function dataObjectController($scope, $rootScope, BillingGenericService, growl, $cookies, $state,$stateParams){
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
			$scope.saveBtnFlag=true;
			self.model.unitId = cookieObject.unitId;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.regex = /[^a-zA-Z_0-9]/g;
			self.model.dataObject = {dataObject:{},title:"",dataObjectId:0};
			self.model.setName = setName;
			self.model.saveDataObject = saveDataObject;
			
			self.model.updateDataObject=updateDataObject;
			self.model.compileObject = compileObject;
		
			$scope.common = $stateParams.dataObjectObj;
			document.cookie="updateFlagDataObj=false";
		
			//for update
		if($scope.common.dataObjectId!=undefined && $scope.common.dataObjectId!=null){
			document.cookie="updateFlagDataObj=true";
			self.model.dataObject.dataObjectId=$scope.common.dataObjectId;
			self.model.dataObject.title=$scope.common.title;
			console.log("data fsfs:",$scope.common.dataObject);
			
			$scope.obj = {};
			document.getElementById("dynamicHtmlContent").innerHTML = JSON
					.parse($scope.common.dataObject).html;
			$scope.obj = JSON
					.parse($scope.common.dataObject).obj;
			$scope.saveBtnFlag=false;
/* 
			var element = angular.element(document
					.getElementById("dynamicHtmlContent"));
			$compile(element)($scope);*/
		  }
		}
		
		$(document).ready(function(){
			$('.fa-trash-o').hide();
			$('.fa-pencil-square-o').hide();
			
			$('#sticky').stick_in_parent();
		});

		var i=1;
		 $( document ).on('click', ".copy-data", function() {
			 i++;
			 if(i%2==0){
				 $(this).find('.fa-trash-o').show();
				  $(this).find('.fa-pencil-square-o').show();

			 }else{
				 $(this).find('.fa-trash-o').hide();
				  $(this).find('.fa-pencil-square-o').hide();
		 
			 }
			 }); 

		 var j=1;
		 $( document ).on('dblclick', ".copy-table-data", function() {
			 j++;
			 if(j%2==0){
				 $(this).find('.fa-trash-o').show();
				  $(this).find('.fa-pencil-square-o').show();

			 }else{
				 $(this).find('.fa-trash-o').hide();
				  $(this).find('.fa-pencil-square-o').hide();
		 
			 }}); 

		/*$(document).on('click', ".txtbox", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			$(document).on('click', ".textarea", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			$(document).on('click', ".checkboxcol1", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			$(document).on('click', ".headercol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			$(document).on('click', ".fieldsetcol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			
			$(document).on('click', ".checkboxcol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			$(document).on('click', ".radiocol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			$(document).on('click', ".dropdowncol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

			 $(document).on('dblclick', ".tabelcol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});
		  
			$(document).on('click', ".calendarcol", function () {
			$(this).find('.fa-trash-o').toggle();
			$(this).find('.fa-pencil-square-o').toggle();
			});

		*/
	
		
		var setName = function(label,obj){
			var tempLabel = angular.copy(label);
			obj['name'] = tempLabel.replace(self.model.regex, '').split(' ').join('');
		}
		
		var compileObject = function(){
			var dynamicHtml = document.getElementById("dynamicHtmlContent").innerHTML;
			
			document.querySelectorAll('#dynamicHtmlContent .fa-pencil-square-o').forEach(function(item,i) {
				var temp = item.className;
				if(i!=0)
				if((temp != "fa fa-pencil-square-o readonly")){
					item.remove();
				}
			});
			
			document.querySelectorAll('#dynamicHtmlContent .fa-trash-o').forEach(function(item,i) {
				var temp = item.className;
				if(i!=0)
				if((temp != "fa fa-trash-o readonly")){
					item.remove();
				}
			});
			
			$("#dynamicHtmlContent td").each(function() {
				  var text = $(this).html();
				  if(text != ""){
					$(this).removeAttr('ng-model');
				  }
			});
			
			
			
			
			//$scope.htmlForm = $sce.trustAsHtml(dynamicHtml);
			
			var elem = angular.element(document.getElementById("dynamicHtmlContent"));
			
			var parentObj = new Array();
			var siblingArray = new Array();
			var elements = document.getElementById("dynamicHtmlContent").querySelectorAll('[ng-model]'); 
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
			//console.log(siblingArray);
			
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
			//console.log(parentObj);
			//console.log(childObjects);
			
			var formObj = {};
			
		      angular.forEach(parentObj, function(value, key) {
		           var parentJson = {};
		          	angular.forEach(childObjects[key], function(value1, key1) {
		          		//$scope.listVal = angular.copy({flag:"",list:"",val:""});
		           	if(value1.includes(".val")){
		           		var element = document.querySelector('[ng-model="'+value+"."+value1.replace(".val",".flag")+'"]');
		           		parentJson[value1.replace(".val","")] = angular.copy({flag:element.value,list:"",val:""});
		           	}else{
		           		parentJson[value1] = '';
		           	}
		       });
		       formObj[value] = $scope.parentJson;
		       self.model.dataObject[value] = angular.copy(parentJson);
		       
		      // delete $scope.
		   });
		      
		      self.model.dataObject.dataObject.html = dynamicHtml;
		      angular.forEach(siblingArray,function(val,i){
		    	  formObj[val]="";
		      });
		      
		}
		
		var saveDataObject = function(){
			var keys = ["createdBy","orgId","unitId","dataObject","title"];
			//var dynamicHtml = document.getElementById("dynamicHtmlContent").innerHTML;
			self.model.dataObject.orgId = self.model.organizationId;
			self.model.dataObject.unitId = self.model.unitId;
			self.model.dataObject.createdBy = self.model.userId;
			self.model.dataObject.dataObjectId=self.model.dataObject.dataObjectId!=0 ? self.model.dataObject.dataObjectId : 0 ;
			self.model.compileObject();
			var removedKeys = new Array();
			self.model.dataObject.dataObject.obj = {};
			
			angular.forEach(self.model.dataObject,function(value,key){
				if(keys.indexOf(key) == -1){
					
			    	if(key == 'obj' && typeof(value) == 'object'){
						angular.forEach(value,function(value1,key1){
							self.model.dataObject.dataObject.obj[key1] = value1;
						});
						removedKeys.push(key);
                    }else{
                    	self.model.dataObject.dataObject.obj[key] = value;
						removedKeys.push(key);
                    }
				}
			});
			self.model.dataObject.dataObject.obj.multidelete('obj');
			removedKeys.forEach(function(value,index){
				self.model.dataObject.multidelete(value);
			})
			
			self.model.dataObject.dataObject = JSON.stringify(self.model.dataObject.dataObject);
			//return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/global/dataobject";
			BillingGenericService.serviceAction(METHOD_POST,URI, self.model.dataObject, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success"){
					init();
					document.getElementById("dynamicHtmlContent").innerHTML = "";
					 $rootScope.stopLoader();
				}
				else if(response.data.status == "error")
					{
					   $rootScope.stopLoader();
					}
				
			});
		}
		
		var updateDataObject=function(){

			var keys = ["createdBy","orgId","unitId","dataObject","title","dataObjectId"];
			self.model.dataObject.orgId = self.model.organizationId;
			self.model.dataObject.unitId = self.model.unitId;
			self.model.dataObject.createdBy = self.model.userId;
			self.model.dataObject.dataObjectId=self.model.dataObject.dataObjectId!=0 ? self.model.dataObject.dataObjectId : 0 ;
			self.model.compileObject();
			var removedKeys = new Array();
			self.model.dataObject.dataObject.obj = {};
			
			angular.forEach(self.model.dataObject,function(value,key){
				if(keys.indexOf(key) == -1){
					
			    	if(key == 'obj' && typeof(value) == 'object'){
						angular.forEach(value,function(value1,key1){
							self.model.dataObject.dataObject.obj[key1] = value1;
						});
						removedKeys.push(key);
                    }else{
                    	self.model.dataObject.dataObject.obj[key] = value;
						removedKeys.push(key);
                    }
				}
			});
			self.model.dataObject.dataObject.obj.multidelete('obj');
			removedKeys.forEach(function(value,index){
				self.model.dataObject.multidelete(value);
			})
			
			self.model.dataObject.dataObject = JSON.stringify(self.model.dataObject.dataObject);
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/global/updateDataObject";
			BillingGenericService.serviceAction(METHOD_POST,URI, self.model.dataObject, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success"){
					$scope.common.dataObjectId=null;
					init();
					document.getElementById("dynamicHtmlContent").innerHTML = "";
					 $rootScope.stopLoader();
				}
				else if(response.data.status == "error")
					{
					   $rootScope.stopLoader();
					}
				
			});
		
		}
		
		init();
	}
	
	
	
	function config($stateProvider){
		$stateProvider.state("dataObject", {
	    	url: '/dataObject',
			templateUrl: 'views/template/data-object.html',
			controller: "DataObject.Controller",
			title: 'Create Clinical Section',
			resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 668);
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
			controllerAs: "vm",
			params: 
			{
				dataObjectObj: {}
			} 
	    });
	}
	
	angular.module("myApp").config(config).controller('DataObject.Controller',dataObjectController)
})();