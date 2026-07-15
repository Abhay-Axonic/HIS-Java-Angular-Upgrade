(function () {
    "use strict";
        /**
         * @Author By  Akshay Narale
         */
    function QualityParameterMaster($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload, GenericServiceParamHeader) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){
            $state.go('login'); return;
        }

        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId;
        $scope.UOMListFiltered = [];

        //$scope.formData.bodyMass="";

        function initializeController() {
            $rootScope.loginpage = true;


            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;

            $scope.uomSelectObj = {
                "id":"",
                "name":""
            }


            $scope.UOMList = []; // Initialize uomList

            $scope.getUOMList = function() {
                var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + LIST_UNIT_MASTER + S + $scope.organizationId;

                return GenericService.serviceAction("GET", URI, {}).then(function(response) {
                    if (response.data.status == "success") {
                        $scope.UOMList = response.data.listObject;
                        console.log("uomList=========", $scope.UOMList);

                        angular.forEach($scope.UOMList, function(val,idx){
                            var uomSelectObj = angular.copy($scope.uomSelectObj);
                            uomSelectObj.id = val.id;
                            uomSelectObj.desc = val.desc;
                            $scope.UOMListFiltered.push(uomSelectObj);
                        })
                        console.log("$scope.UOMListFiltered======",$scope.UOMListFiltered);
                    } else if (response.data.status == 'error') {
                        growl.error(response.data.message, {
                            title: 'Error!'
                        });
                    }
                });
            }

            $scope.getUOMListAfterGetting = [];

            // Call the function to fetch data
            $scope.getUOMList().then(function() {
                // Do something after data is loaded if needed
                $scope.getUOMListAfterGetting = $scope.UOMList;
                console.log("getUOMListAfterGetting=========", $scope.getUOMListAfterGetting);
            });


            $scope.getModules = function() {
                var URI = CONSTANTS.GLOBAL.MODULE_API_BY_PARENT;

                GenericService.serviceAction("GET", URI, {}).then(function (response) {
                    $scope.moduleList = [];
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                       $scope.moduleList = response.data.listObject;
                       console.log("moduleList=========",$scope.moduleList);
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });
            }
            $scope.getModules();

//            $scope.saveQualityParam = function() {
//                alert("save quality=======")
//                // Add your save functionality here
//                console.log('Button clicked'); // Log a message to check if the function is being called
//            };

//            $scope.isNumber = function($event) {
//                var charCode = $event.which ? $event.which : $event.keyCode;
//                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
//                    $event.preventDefault();
//                }
//            };

            $scope.checkThresholdPercentage = function(percent){
                if( $scope.targetPercentage == "" ||  $scope.targetPercentage == undefined ||  $scope.targetPercentage == null){
                    growl.warning("Please fill-up Target Percentage first...",{
                        title : "warning"
                    });
                    $scope.thresholdPercentage = "";
                    return false;
                }
                if(percent>$scope.targetPercentage){
                    growl.warning("Threshold Percentage should be less than Target Percentage ",{
                        title : "warning"
                    });
                    $scope.thresholdPercentage = "";
                    return false;
                }else if(percent<0){
                    growl.warning("Threshold Percentage should be greater than 0 ",{
                        title : "warning"
                    });
                    $scope.thresholdPercentage = "";
                    return false;
                }

            }

            $scope.checkTargetPercentage = function(percent){
                if(percent>100){
                    growl.warning("Target Percentage should be less than 100 ",{
                        title : "warning"
                    });
                    $scope.targetPercentage = "";
                    return false;
                }else if(percent<0){
                    growl.warning("Target Percentage should be greater than 0 ",{
                        title : "warning"
                    });
                    $scope.targetPercentage = "";
                    return false;
                }

            }

            $scope.getNames = function(uomId, moduleId){
                var selectedUOM = $scope.UOMListFiltered.find(function(uom) {
                    return uom.id === Number(uomId);
                });
                var selectedModule = $scope.moduleList.find(function(module) {
                    return module.moduleId === Number(moduleId);
                });

                $scope.selectedUOMDesc = selectedUOM.desc;
                $scope.selectedModuleName = selectedModule.moduleName;
                console.log("selectedUOMDesc======",$scope.selectedUOMDesc);
                console.log("selectedModuleName======",$scope.selectedModuleName);

            }

            $scope.update = 'N';
            $scope.saveQualityParam = function(){

                console.log("ppppppppppp ======",$scope.UOMListFiltered);

                if($scope.selectedUOMId==null || $scope.selectedUOMId==undefined || $scope.selectedUOMId==""){
                    growl.error("Please select UOM", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.selectModuleId==null || $scope.selectModuleId==undefined || $scope.selectModuleId==""){
                    growl.error("Please select Module", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.activityId==null || $scope.activityId==undefined || $scope.activityId==""){
                    growl.error("Please fill up Activity Id", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.activityName==null || $scope.activityName==undefined || $scope.activityName==""){
                    growl.error("Please fill up Activity", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.parameterId==null || $scope.parameterId==undefined || $scope.parameterId==""){
                    growl.error("Please fill up Parameter Id", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.standard==null || $scope.standard==undefined || $scope.standard==""){
                    growl.error("Please fill up Standard", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.thresholdPercentage==null || $scope.thresholdPercentage==undefined || $scope.thresholdPercentage==""){
                    growl.error("Please fill up Threshold Percentage", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.targetPercentage==null || $scope.targetPercentage==undefined || $scope.targetPercentage==""){
                    growl.error("Please fill up Target Percentage", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                $scope.getNames($scope.selectedUOMId, $scope.selectModuleId);

                $rootScope.startLoader();

                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    moduleId:$scope.selectModuleId,
                    moduleName:$scope.selectedModuleName,
                    activityId:$scope.activityId,
                    activityName:$scope.activityName,
                    parameterId:$scope.parameterId,
                    standard:$scope.standard,
                    uomId:$scope.selectedUOMId,
                    uomDesc:$scope.selectedUOMDesc,
                    thresholdPercentage:$scope.thresholdPercentage,
                    targetPercentage:$scope.targetPercentage,
                    update:$scope.update,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                if($scope.update=='Y'){
                    saveObj.qualityParameterMasterId = $scope.qualityParameterMasterId;
                    saveObj.status = $scope.status;
                    $scope.status = "";
                    $scope.qualityParameterMasterId = "";
                    $scope.update = 'N';
                }

                 console.log('saveObj====',saveObj);

                var URI = BASE_URL + ROOT_URL +"/api/unit/saveQualityParam";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                         {
                            title : 'Success!'
                         });
                         $scope.blankParameters();
                         $scope.getQualityParamList();
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });
            }

            $scope.blankParameters = function(){
                $scope.selectModuleId = "";
                $scope.activityId = "";
                $scope.activityName = "";
                $scope.parameterId = "";
                $scope.standard = "";
                $scope.thresholdPercentage = "";
                $scope.targetPercentage = "";
                $scope.selectedUOMId = "";
            }

            $scope.editQualityParam = function(info){
                $scope.update = 'Y';
//                $scope.selectedUOMId = info.uomId;
//                $scope.selectModuleId = info.moduleId;
                $scope.activityId = info.activityId;
                $scope.activityName = info.activityName;
                $scope.parameterId = info.parameterId;
                $scope.standard = info.standard;
                $scope.thresholdPercentage = info.thresholdPercentage;
                $scope.targetPercentage = info.targetPercentage;
                $scope.status = info.status;
                $scope.qualityParameterMasterId = info.qualityParameterMasterId;

                angular.forEach($scope.UOMListFiltered, function(value,index) {
                      if (value.id === info.uomId) {
                          $scope.value= info.uomId; // Assign the entire UOM object
                      }
                  });
                $scope.selectedUOMId=$scope.value;

                  angular.forEach($scope.moduleList, function(item,index) {
                      if (item.moduleId == info.moduleId) {
                          $scope.item= info.moduleId; // Assign the entire UOM object
                      }
                  });
                 $scope.selectModuleId=$scope.item;

            }

            $scope.getQualityParamList = function(){

                $rootScope.startLoader();
                var URI = BASE_URL + ROOT_URL +"/api/unit/getQualityParamList/"+$scope.unitId+"/"+$scope.organizationId;

                GenericService.serviceAction("GET", URI, {}).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         $scope.qualityParamList = response.data.listObject;
                         console.log("$scope.qualityParamList============",$scope.qualityParamList);
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });

            }

            $scope.getQualityParamList();

            $scope.updateStatus = function(info){
                $scope.update = 'Y';
                $rootScope.startLoader();
                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    qualityParameterMasterId:info.qualityParameterMasterId,
                    status:info.status,
                    update:$scope.update,
                    updatedBy : $scope.userId,
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                 console.log('saveObj====',saveObj);

                var URI = BASE_URL + ROOT_URL +"/api/unit/updateStatus";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                         {
                            title : 'Success!'
                         });
                         $scope.getQualityParamList();
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });
            }

        }


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("qualityParameterMaster", {
            url: "/qualityParameterMaster",
            title: 'Quality Parameter Master',
            templateUrl: 'views/master/UnitMaster/organizationMaster/quality-parameter-master.html',
            controller: "QualityParameterMaster.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("QualityParameterMaster.Controller", QualityParameterMaster);
})();