(function () {
    "use strict";

    function prefixConfigurationMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            
            self.model = {

                screens: [{
                    "screen": "Select Screen"
                }],
                resets: [{
                    "resetName": "Select Reset"
                }],
                formats: [{
                    "formatName": "Select Format"
                }],


            };
            self.model.globalLevel = true;
            self.model.unitLevel = false;
            PopulateDropdown();
            PrefixConfigurationSearch();
            self.model.generatedCode = '';
            self.model.example = '';
            self.model.GetConfiguration = GetConfiguration;
            self.model.setTabAccordingToLevel = setTabAccordingToLevel;
            self.model.saveRequest = saveRequest;
            self.model.addToGeneratedCode = addToGeneratedCode;
            self.model.clearFields = clearFields;
            self.model.openPopup = openPopup;
           
            
        }
        var setTabAccordingToLevel = function (tab) {
        	if(tab == 1 ){
        		 self.model.globalLevel = true;
                 self.model.unitLevel = false;
        	}else{
        		self.model.globalLevel = false;
                self.model.unitLevel = true;
        	}
        	
        	PrefixConfigurationSearch();
        	PopulateDropdown();
        } 

        var PopulateDropdown = function () {
            var prefixConfig = self.model;
            prefixConfig.screen = prefixConfig.screens[0];
            prefixConfig.reset = prefixConfig.resets[0];
            prefixConfig.format = prefixConfig.formats[0];
            var URI = CONSTANTS.MASTER.UNIT.PREFIX_CONFIGURATION.DROPDOWN_API;
            $rootScope.startLoader();
            self.model.globalLevelScreen=[];
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	angular.forEach(response.data.screen,function(value,key){
                		
                		prefixConfig.screens = [];
                		prefixConfig.resets = [];
                		prefixConfig.formats = [];
                		if(self.model.globalLevel == true){
	                		if(value.isGlobal == true){
	                			self.model.globalLevelScreen.push(value);
	                		}
                		 }
                		else{
                			if(value.isGlobal == false){
	                			self.model.globalLevelScreen.push(value);
	                		}
                		 }
                	});
                    prefixConfig.screens = prefixConfig.screens.concat(self.model.globalLevelScreen);
                    console.log("screen data",response.data.screen);
                    prefixConfig.resets = prefixConfig.resets.concat(response.data.reset);
                    prefixConfig.formats = prefixConfig.formats.concat(response.data.format);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var PrefixConfigurationSearch = function () {
        	if(self.model.unitLevel == true){
        		var flag=true;
        	}else{
        		var flag=false;
        	}
//            var URI = CONSTANTS.MASTER.UNIT +"/api/units/configuration/prefixconfiguration/orgUnitWiseList/"+ flag ;
            
            var URI = BASE_URL + ROOT_URL + "/api/units/configuration/prefixconfiguration/orgUnitWiseList/"+ $scope.unitId + "/"+$scope.organizationId + "/" + flag ;

            
            
            $rootScope.startLoader()
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.searchprefixConfiguration = response.data;

                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        var GetConfiguration = function (prefixConfig) {

            var URI = CONSTANTS.MASTER.UNIT.PREFIX_CONFIGURATION.DETAIL_API + prefixConfig.id;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.prefixId = response.data.id;
                    self.model.screen = self.model.screens.find(function (prefix) {
                        return response.data.screenId == prefix.id;
                    });

                    self.model.reset = self.model.resets.find(function (prefix) {
                        return response.data.resetId == prefix.id;
                    });

                    // self.model.format = self.model.formats.find(function (prefix) {
                    //     return response.data.formatId == prefix.id;
                    // });
                    self.model.startFrom = response.data.runningNumber;
                    self.model.length = response.data.length;
                    self.model.generatedCode = response.data.generatedCode;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
    
        var ValidateGenerateCode=function(){
            var isValid = true;
            if (self.model.generatedCode.indexOf('[SF]') == -1) {
                self.model.isPrefixFormatValid = true;
                isValid = false;
                growl.error("Please Select Format as start from");
            }
            if(self.model.generatedCode.length > 90){
            	isValid = false;
				growl.warning("Entered generated code is too large!!",
						{
							title : 'Warning!'
						});
            }
            return isValid;
        }
     
        var saveRequest = function () {
            if (ValidateGenerateCode()) {
                if (!self.model.prefixId) {
                    saveNewRequest();
                }
                else {
                    updateRequest();
                }
            }
        }

        var saveNewRequest = function () {

            var URI = CONSTANTS.MASTER.UNIT.PREFIX_CONFIGURATION.SAVE_API;

            var data = {
                "screenId": self.model.screen.id,
                "resetId": self.model.reset.id,
                "runningNumber": self.model.startFrom,
                "length": self.model.length,
                "generatedCode": self.model.generatedCode,
                "flag" : self.model.screen.isGlobal,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();

                    PrefixConfigurationSearch();
                    clearFields();
                	PopulateDropdown();
                    if(response.data.code=="409")
                    	growl.warning(response.data.message);
                    else
                    	growl.success(response.data.message);
                },

                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }


        var updateRequest = function () {

            var URI = CONSTANTS.MASTER.UNIT.PREFIX_CONFIGURATION.UPDATE_API + self.model.prefixId;

            var data = {
                "screenId": self.model.screen.id,
                "resetId": self.model.reset.id,
                "runningNumber": self.model.startFrom,
                "length": self.model.length,
                "generatedCode": self.model.generatedCode,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    PrefixConfigurationSearch();
                    clearFields();
                    growl.success(response.data.message);
                },

                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var addToGeneratedCode = function () {
            var a = '';
            if (self.model.format.id != 9) {
                a = a.concat(self.model.format.code);
                examplePreview();
                self.model.generatedCode = self.model.generatedCode + "[" + a + "]" + "|";
                self.model.format = self.model.formats[0];
                
                if(self.model.generatedCode.length > 90){
					growl.warning("Entered generated code is too large!!",
							{
								title : 'Warning!'
							});
                }
                self.model.format={
                		code:"",
                		formatName:"",
                		id:""
                };
            }
            else {
                a = a.concat(self.model.formatCode);
                self.model.generatedCode = self.model.generatedCode + "{" + a + "}" + "|";
                examplePreview();
                self.model.format = self.model.formats[0];
                self.model.formatCode = '';

            }
        }

        var clearFields = function () {
            self.model.generatedCode = '';
            self.model.example = '';
            self.model.format = self.model.formats[0];
            self.model.reset = self.model.resets[0];
            self.model.length = '';
            self.model.startFrom = '';
        }

        var openPopup = function () {
            $("#confirmSave").modal();
        }

        var examplePreview = function () {
            if (self.model.format.id == 1) {
                self.model.example = self.model.example + "2018";
            }
            else if (self.model.format.id == 2) {
                self.model.example = self.model.example + "18";
            }
            else if (self.model.format.id == 3) {
                self.model.example = self.model.example + "MARCH";
            }
            else if (self.model.format.id == 4) {
                self.model.example = self.model.example + "MAR";
            }
            else if (self.model.format.id == 5) {
                self.model.example = self.model.example + "03";
            }
            else if (self.model.format.id == 6) {
                self.model.example = self.model.example + "15";
            }
            else if (self.model.format.id == 7) {
                self.model.example = self.model.example + "PUN";
            }
            else if (self.model.format.id == 8) {
                self.model.example = self.model.example + "PH";
            }
            else if (self.model.format.id == 9) {
                self.model.example = self.model.example + "ABC";
            }
            else if (self.model.format.id == 10) {
                self.model.example = self.model.example + "/";
            }
            else if (self.model.format.id == 11) {
                self.model.example = self.model.example + "-";
            }
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("prefixConfiguration", {
            url: "/prefixConfiguration",
            templateUrl: 'views/master/UnitMaster/configuration/prefix-master.html',
            controller: "PrefixConfiguration.Controller",
            title:"Prefix Configuration",
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 699);
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

    angular
        .module("myApp")
        .config(config)
        .controller("PrefixConfiguration.Controller", prefixConfigurationMasterController);
})();
