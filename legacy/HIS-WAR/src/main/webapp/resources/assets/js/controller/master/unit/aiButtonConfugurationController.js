(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function AIButtonConfiguration($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {

        //Default Constructor
        var self = this;
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.showDoctor=true;
		$scope.showSpeciality=false;
		$scope.tabId=1;
        $scope.configButton = function(){
           $("#aiButtonConfigPop").modal("show");
        }
        $scope.getDoctor = function()
    	{
    		$scope.specialityId='';
    		$scope.specialityName='';
    		$scope.doctorChecked=true;
    		$("#radio9").prop('checked', true);
			$("#radio10").prop('checked', false);
    		$scope.showDoctor=true;
    		$scope.showSpeciality=false;
    	}
    	$scope.getSpeciality = function()
    	{
    		$scope.doctorId='';
    		$scope.doctorName='';
    		$("#radio9").prop('checked', false);
			$("#radio10").prop('checked', true);
    		$scope.showSpeciality=true;
    		$scope.showDoctor=false;
    	}
    	$scope.getDoctorAndSpeciality = function()
    	{
    		var data={};
    		data.organizationId=$scope.organizationId;
			data.unitId=$scope.unitId;
    		var URI = BASE_URL + ROOT_URL+"/api/global/getDoctor";
    		GenericService.serviceAction("POST", URI, data).then(
    				function(response) {
    					if (response.data.status == "success") {
    						$scope.doctorList=response.data.listObject;
    						
    					}
    				});
    		var URI = BASE_URL + ROOT_URL+"/api/global/getSpeciality";
    		GenericService.serviceAction("POST", URI, data).then(
    				function(response) {
    					if (response.data.status == "success") {
    						$scope.specialityList=response.data.listObject;
    					} 
    				});
    	}
    	$scope.getDoctorAndSpeciality();
    	$scope.getAllData = function() {
    		var data={};
    		data.hospitalId=$scope.organizationId;
			data.unitId=$scope.unitId;
    		var URI = BASE_URL + ROOT_URL+"/api/global/getButtonMaster";
    		GenericService.serviceAction("POST", URI, data).then(
    				function(response) {
    					if (response.data.status == "success") {
    						$scope.buttonData=response.data.listObject;
    					}
    				});
    		}
    	$scope.getAllData();
    	$scope.getDoctorChange = function(doctor)
    	{
    		$scope.doctorId=doctor.doctorId;
    		$scope.doctorName=doctor.doctorName;
    		$scope.doctorSelected=true;
    	}
    	$scope.getSpecialityChange = function(speciality)
    	{
    		$scope.specialityId=speciality.doctorId;
    		$scope.specialityName=speciality.firstName;
    		$scope.specialitySelected=true;
    	}
    	$scope.setButton = function(button)
    	{
    		$scope.buttonId=button.buttonId;
    		$scope.buttonName=button.label;
    	}
    	$scope.userPrompt="";
    	$scope.systemPrompt="";
    	$scope.input=true;
    	$scope.gender=false;
    	$scope.age=false;
    	$scope.assignUserPrompt = function(key)
    	{
    		$scope.model[key]=$scope.userPrompt;
    	}
    	$scope.assignSystemPrompt = function(key)
    	{
    		$scope.model[key]=$scope.systemPrompt;
    	}
    	$scope.assignUserPromptEdit = function(key)
    	{
    		$scope.userPrompt=$scope.model[key];
    	}
    	$scope.assignSystemPromptEdit = function(key)
    	{
    		$scope.systemPrompt=$scope.model[key];
    	}
    	$scope.editFlag=false;
    	$scope.getConfig = function(buttonId)
        {
    		if(($scope.specialitySelected==undefined || $scope.specialitySelected==null) && ($scope.doctorSelected==undefined || $scope.doctorSelected==null))
    			{
    			growl.error("Please select doctor or speciality");
    			return false;
    			}
    		if($scope.buttonId==undefined || $scope.buttonId==null)
    			{
    			growl.error("Please select button");
    			return false;
    			}
    		var data ={
    				"buttonId":$scope.buttonId!=null ? $scope.buttonId :buttonId
    		}
    		 
    		if($scope.editFlag==false)
    			{
    			data.hospitalId=$scope.organizationId;
        	var URI = BASE_URL + ROOT_URL+"/api/global/getAiButtonConfigMasterForDocAndSpeciality";
    		GenericService.serviceAction("POST", URI, data).then(
    				function(response) {
    					if(response.data.object!=null)
    						{
    					var data1=JSON.parse(response.data.object);
    					$scope.url = data1.urlObject.URL;
    		             $scope.model = data1.aiPayload;
    		            const keys = [];
    		            const values = [];

    		            $.each($scope.model, function(key, value) {
    		                keys.push(key);
    		                values.push(value);
    		            });
    		            angular.forEach($scope.buttonData, function (optionItem,index) {
    		   			 if(optionItem.buttonId==$scope.buttonId)
    		   				 {
    		   				     $scope.userPrompt=optionItem.userPrompt;
    		   					 $scope.systemPrompt=optionItem.systemPrompt;
    		   					 $scope.assignUserPrompt("User_Prompt");
    		   					 $scope.assignSystemPrompt("System_prompt");
    		   				 }
    		   		 });
    		            $("#aiButtonConfigPop").modal('show');
    					console.log("response",response);
    						}
    					else
    						{
    						$.notify("Data is not configured", "error");

    						$rootScope.stopLoader();
    						return false;
    						}
    				})
    			}
    		else
    			{
    			if($scope.model!=undefined && $scope.editFlag!=null)
    				{
    				 $scope.assignUserPromptEdit("User_Prompt");
    					 $scope.assignSystemPromptEdit("System_prompt");
    				$("#aiButtonConfigPop").modal('show');
    				}
    			}
        }
    	$scope.inputString=[];
    	$scope.inputString.push("@Input_from_application");
    	$scope.getCheckBox = function(key,value)
    	{
    		if(key==true)
    			{
    			$scope.inputString.push(value);
    	         var arrayWithoutDuplicates = [...new Set($scope.inputString)];
    	         $scope.inputString=arrayWithoutDuplicates;
    			}
    		else if(key==false)
    			{
    			angular.forEach($scope.inputString, function (optionItem,index) {
    				if (optionItem === value) {
    					$scope.inputString.splice(index, 1);
    				}
    			});
    	         var arrayWithoutDuplicates = [...new Set($scope.inputString)];
    	         $scope.inputString=arrayWithoutDuplicates;
    			}
    	}

    	$scope.saveConfig = function()
    	{
    		var input='';
    			angular.forEach($scope.inputString, function (optionItem) {
    			input=input.concat(optionItem+",");
    		});
    		$scope.model["Input_from_application"]=input;
    		var data={
    				"doctorId":$scope.doctorId,
    				"specialityId":$scope.specialityId,
    				"doctorName":$scope.doctorName,
    				"specialityName":$scope.specialityName,
    				"label":$scope.buttonName,
    				"buttonId":$scope.buttonId,
    				"configId":$scope.configId
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
    		const jsonString = JSON.stringify(combined, null, 2);
    		
    		data.jsonObject=jsonString;
    		data.hospitalId=$scope.organizationId;
    		data.unitId=$scope.unitId;
    		if($scope.tabId==1)
    			{
    			$rootScope.startLoader();
    			var URI = BASE_URL + ROOT_URL+"/api/global/saveConfig";
    			GenericService.serviceAction("POST", URI, data).then(
    					function(response) {
    						if(response.data.status=="success")
    							{
    							$scope.editFlag=false;
    							$scope.doctor="";
    							$scope.speciality="";
    							$scope.button="";
    							$scope.specialitySelected=null;
    							$scope.doctorSelected=null;
    							$scope.buttonId=null;
    							$scope.showSpeciality=false;
    							$scope.showDoctor=true;
    							$("#radio9").prop('checked', true);
    							$("#radio10").prop('checked', false);
    							 $scope.getSavedConfig();
    							 if(response.data.code=="1")
    								 {
    								 growl.error(response.data.message);
    								 }
    							 else
    								 {
    								 growl.success(response.data.message);
    								 }
    							 $rootScope.stopLoader();
    							}
    						else
    							{
    							$scope.editFlag=false;
    							$rootScope.stopLoader();
    							}
    			            $("#aiButtonConfigPop").modal('hide');
    					})
    			}
    	}


    	$scope.isSaving = false;
        $scope.saveConfig = function() {
            if ($scope.isSaving) return;
            $scope.isSaving = true;

            var input = '';
            angular.forEach($scope.inputString, function(optionItem) {
                input = input.concat(optionItem + ",");
            });
            $scope.model["Input_from_application"] = input;

            var data = {
                "doctorId": $scope.doctorId,
                "specialityId": $scope.specialityId,
                "doctorName": $scope.doctorName,
                "specialityName": $scope.specialityName,
                "label": $scope.buttonName,
                "buttonId": $scope.buttonId,
                "configId": $scope.configId,
                "hospitalId": $scope.organizationId,
                "unitId": $scope.unitId
            };

            var jsonObject1 = {
                "urlObject": {
                    "URL": $scope.url
                }
            };
            var jsonObject2 = {
                "aiPayload": $scope.model
            };
            const combined = Object.assign({}, jsonObject1, jsonObject2);
            const jsonString = JSON.stringify(combined, null, 2);

            data.jsonObject = jsonString;

            if ($scope.tabId == 1) {
                $rootScope.startLoader();

                var URI = BASE_URL + ROOT_URL + "/api/global/saveConfig";
                GenericService.serviceAction("POST", URI, data).then(
                    function(response) {
                        if (response.data.status == "success") {
                            $scope.editFlag = false;
                            $scope.doctor = "";
                            $scope.speciality = "";
                            $scope.button = "";
                            $scope.specialitySelected = null;
                            $scope.doctorSelected = null;
                            $scope.buttonId = null;
                            $scope.showSpeciality = false;
                            $scope.showDoctor = true;
                            $("#radio9").prop('checked', true);
                            $("#radio10").prop('checked', false);
                            $scope.getSavedConfig();

                            if (response.data.code == "1") {
                                growl.error(response.data.message);
                            } else {
                                growl.success(response.data.message);
                            }
                        } else {
                            $scope.editFlag = false;
                        }

                        $rootScope.stopLoader();
                        $("#aiButtonConfigPop").modal('hide');
                    }
                ).catch(function(error) {
                }).finally(function() {
                    $scope.isSaving = false;
                });
            }
        };


    	$scope.getSavedConfig = function()
    	{
    		var data={};
    		data.hospitalId=$scope.organizationId;
    		data.unitId=$scope.unitId;
    			var URI = BASE_URL + ROOT_URL+"/api/global/getSavedConfig";
    			GenericService.serviceAction("POST", URI, data).then(
    					function(response) {
    						$scope.buttonSavedData=response.data.listObject;
    					})
    	}
    	$scope.getSavedConfig();


    	$scope.searchByLabel = function(button) {
                        if (!$scope.searchBillFilter) return true;
                        return button.label.toLowerCase().includes($scope.searchBillFilter.toLowerCase());
                    };

    	$scope.AIInfo = function(object)
    	{
    		var data1=JSON.parse(object);
    		$scope.url = data1.urlObject.URL;
             $scope.model = data1.aiPayload;
             $('#AIConfigInfo').modal('show');
    	}
    	$scope.doctorChecked=true;
    	$scope.specialityChecked=false;
    	$scope.editButtonMaster = function(button){
    		if(button.doctorId!=undefined && button.doctorId!=null)
    			{
    			$scope.doctorSelected=true;
    			$scope.doctorChecked=true;
    			$scope.specialityChecked=false;
    			$("#radio9").prop('checked', true);
    			$("#radio10").prop('checked', false);
    			$scope.showSpeciality=false;
    			$scope.showDoctor=true;
    			$scope.specialityId='';
    			$scope.specialityName='';
    			$scope.doctor = $scope.doctorList.find(spec => spec.doctorId === button.doctorId);
    			}
    		else if(button.specialityId!=undefined && button.specialityId!=null)
    			{
    			$scope.specialitySelected=true;
    			$scope.specialityChecked=true;
    			$scope.doctorChecked=false;
    			$("#radio9").prop('checked', false);
    			$("#radio10").prop('checked', true);
    			$scope.showSpeciality=true;
    			$scope.showDoctor=false;
    			$scope.doctorId='';
    			$scope.doctorName='';
    			$scope.speciality = $scope.specialityList.find(spec => spec.doctorId === button.specialityId);
    			}
    		$scope.button = $scope.buttonData.find(spec => spec.buttonId === button.buttonId);
    		$scope.buttonId=button.buttonId;
    		$scope.doctorId=button.doctorId;
    		$scope.doctorName=button.doctorName;
    		$scope.specialityId=button.specialityId;
    		$scope.specialityName=button.specialityName;
    		$scope.buttonName=button.label;
    		$scope.buttonLabel=button.label;
    		$scope.editFlag=true;
    		$scope.configId=button.configId;
    		var data1=JSON.parse(button.jsonObject);
    		$scope.url = data1.urlObject.URL;
             $scope.model = data1.aiPayload;
             if($scope.model.Input_from_application.includes("@Gender"))
            	 {
            	 $scope.gender=true;
            	 }
             if($scope.model.Input_from_application.includes("@Age"))
        	 {
            	 $scope.age=true;
        	 }
             if($scope.model.Input_from_application.includes("@Location"))
        	 {
        	 $scope.location=true;
        	 }
    	}

        $scope.statusChanged =function(id,status)
        {
            var data={
                    "isStatus":status,
                    "buttonId":id
            }
            data.hospitalId=$scope.organizationId;
    		data.unitId=$scope.unitId;
    		$rootScope.startLoader();
            var URI = BASE_URL + ROOT_URL+"/api/global/updateButtonMasterConfigStatus";
            GenericService.serviceAction("POST", URI, data).then(
                    function(response) {
                        if (response.data.status == "success") {
                            $scope.getAllData();
                            $rootScope.stopLoader();
                            
                        } else {
                        	$rootScope.stopLoader();
                        }
                    });
        }

}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("aiButtonConfiguration", {
            url: "/aiButtonConfiguration",
            title: 'AI Button Configuration',
            templateUrl: 'views/master/UnitMaster/organizationMaster/aiButtonConfiguration.html',
            controller: "AIButtonConfiguration.Controller",
            controllerAs: "vm"
        });
    }

    angular
    .module("myApp")
    .config(config)
    .controller("AIButtonConfiguration.Controller",AIButtonConfiguration);

})();