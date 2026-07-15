(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function AIButtonMaster($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {

        //Default Constructor
        var self = this;
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;


        $scope.configButton = function(){
           $("#aiButtonConfig").modal("show");
        }
        $scope.editFlag=false;
        $scope.getConfig = function()
        {
        	if($scope.checkValidation())
			{
			if($scope.editFlag==false || ($scope.model==undefined || $scope.model==null || $scope.model==""))
				{
				var data={
						unitId:$scope.unitId
				}
        	var URI = BASE_URL + ROOT_URL+"/api/global/getAiButtonConfigMaster";
    		GenericService.serviceAction("POST", URI, data).then(
    				function(response) {
    					var data1=JSON.parse(response.data.object);
    					$scope.url = data1.urlObject.URL;
    		             $scope.model = data1.aiPayload;
    		            const keys = [];
    		            const values = [];

    		            $.each($scope.model, function(key, value) {
    		                keys.push(key);
    		                values.push(value);
    		            });
    		            $("#aiButtonConfig").modal("show");
    					console.log("response",response);
    				})
				}
			else
				{
				if($scope.model!=undefined && $scope.editFlag!=null)
					{
					$("#aiButtonConfig").modal("show");
					}
				}
			}
        }
        $scope.closePopUp = function()
        {
        	$("#aiButtonConfig").modal('hide');
        }
        $scope.checkValidation = function()
        {
        	if($scope.buttonLabel==undefined || $scope.buttonLabel==null || $scope.buttonLabel=="")
			{
			growl.error("Please enter label");
			return false;
			}
		
        	else if($scope.systemPrompt==undefined || $scope.systemPrompt==null || $scope.systemPrompt=="")
		{
			growl.error("Please enter system prompt");
		return false;
		}
        	else if($scope.userPrompt==undefined || $scope.userPrompt==null || $scope.userPrompt=="")
		{
			growl.error("Please enter user prompt");
		return false;
		}
        	else
        		{
        		return true;
        		}
        }
        $scope.saveButtonMaster = function(){
        	if($scope.checkValidation())
			{
			var data={
					"label":$scope.buttonLabel,
					"systemPrompt":$scope.systemPrompt,
					"userPrompt":$scope.userPrompt,
			}
			var jsonObject1={
					  "urlObject": {
						    "URL": $scope.url
						  }
						};
			var jsonObject2={
						  "aiPayload":
						    $scope.model
						};
			if($scope.model==undefined || $scope.model==null || $scope.model=='')
				{
				growl.error("Please add AI configuration");
				return false;
				}
			$rootScope.startLoader();
			const combined = Object.assign({}, jsonObject1, jsonObject2);
			// Convert the merged object into a JSON string
			const jsonString = JSON.stringify(combined, null, 2);
			data.jsonObject=jsonString;
			data.hospitalId=$scope.organizationId;
			data.unitId=$scope.unitId;
			var URI = BASE_URL + ROOT_URL+"/api/global/saveButtonMaster";
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.status == "success") {
							$scope.systemPrompt="";
							$scope.userPrompt="";
							$scope.tabId=10;
							$scope.buttonId="";
							$rootScope.stopLoader();
							$scope.buttonLabel="";
							growl.success("Saved successfully");
							$scope.getAllData()
							
						} else {
							$rootScope.stopLoader();
							$scope.getAllData()
						}
					});
			}
		}
        $scope.getAllData = function() {
    		var data = {};
    		$rootScope.startLoader();
    		data.hospitalId=$scope.organizationId;
			data.unitId=$scope.unitId;
    		var URI = BASE_URL + ROOT_URL+"/api/global/getButtonMaster";
    		GenericService.serviceAction("POST", URI, data).then(
    				function(response) {
    					if (response.data.status == "success") {
    						$scope.buttonData=response.data.listObject;
    					    $rootScope.stopLoader();
    					} else {
    						$rootScope.stopLoader();
    					}
    				});
    		}
    		$scope.getAllData();
    		$scope.searchByLabel = function(button) {
                if (!$scope.searchBillFilter) return true;
                return button.label.toLowerCase().includes($scope.searchBillFilter.toLowerCase());
            };


    		$scope.editFlag=false;
    		$scope.editButtonMaster = function(id,label,systemP,userP,tabId,object,status){
    		    if(status==false){
    		     $scope.editFlag=false;
    		      return false;
    		    }

    			$scope.buttonLabel=label;
    			$scope.systemPrompt=systemP;
    			$scope.userPrompt=userP;
    			$scope.tabId=tabId;
    			$scope.buttonId=id;
    			$scope.editFlag=true;
    			var data1=JSON.parse(object);
    			$scope.url = data1.urlObject.URL;
                 $scope.model = data1.aiPayload;
                const keys = [];
                const values = [];

                $.each($scope.model, function(key, value) {
                    keys.push(key);
                    values.push(value);
                });
    		}
    		$scope.updateButtonMaster = function(id,label,systemP,userP,tabId){
    			if($scope.checkValidation())
    				{
    			var data={
    					"label":$scope.buttonLabel,
    					"systemPrompt":$scope.systemPrompt,
    					"userPrompt":$scope.userPrompt,
    					"tabId":$scope.tabId,
    					"buttonId":$scope.buttonId
    			}
    			var jsonObject1={
    					  "urlObject": {
    						    "URL": $scope.url
    						  }
    						};
    			var jsonObject2={
    						  "aiPayload":
    						    $scope.model
    						};
    			const combined = Object.assign({}, jsonObject1, jsonObject2);
    			// Convert the merged object into a JSON string
    			const jsonString = JSON.stringify(combined, null, 2);
    			data.jsonObject=jsonString;
    			$rootScope.startLoader();
    			var URI = BASE_URL + ROOT_URL+"/api/global/updateButtonMaster";
    			GenericService.serviceAction("POST", URI, data).then(
    					function(response) {
    						if (response.data.status == "success") {
    							$scope.getAllData();
    							$scope.systemPrompt="";
    							$scope.userPrompt="";
    							$scope.tabId=10;
    							$scope.buttonId="";
    							$rootScope.stopLoader();
    							$scope.buttonLabel="";
    							growl.success("Updated successfully");
    							$scope.editFlag=false;
    							
    						} else {
    							$rootScope.stopLoader();
    							$scope.editFlag=false;
    						}
    					});
    		}
    		}
    		$scope.clearFields = function()
    		{
    			if($scope.editFlag==false)
    				{
    				$scope.buttonLabel="";
    				}
    			$scope.systemPrompt="";
				$scope.userPrompt="";
    		}
    		$scope.AIInfo = function(object)
    		{
    			var data1=JSON.parse(object);
    			$scope.url = data1.urlObject.URL;
                 $scope.model = data1.aiPayload;
                 $('#AIConfigInfo').modal('show');
    		}
    		$scope.getUserPromptInfo = function(object)
    		{
    			   $scope.userPromptInfo=object;
                 $('#userPromptInfo').modal('show');
    		}
    		$scope.getSystemPromptInfo = function(object)
    		{
    			 $scope.systemPromptInfo=object;
                 $('#systemPromptInfo').modal('show');
    		}
    		
    		$scope.statusChanged =function(id,status)
    		{
    			var data={
    					"isStatus":status,
    					"buttonId":id
    			}
    			$rootScope.startLoader();
    			var URI = BASE_URL + ROOT_URL+"/api/global/updateButtonMasterStatus";
    			GenericService.serviceAction("POST", URI, data).then(
    					function(response) {
    						if (response.data.status == "success") {
    							$scope.getAllData();
    							$rootScope.stopLoader();
    							growl.success("status changed successfully");
    							
    						} else {
    							$rootScope.stopLoader();
    						}
    					});
    		}
}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("aIButtonMaster", {
            url: "/aIButtonMaster",
            title: 'AI Button Master',
            templateUrl: 'views/master/UnitMaster/organizationMaster/ai-button-master.html',
            controller: "AIButtonMaster.Controller",
            controllerAs: "vm"
        });
    }

    angular
    .module("myApp")
    .config(config)
    .controller("AIButtonMaster.Controller",AIButtonMaster);

})();