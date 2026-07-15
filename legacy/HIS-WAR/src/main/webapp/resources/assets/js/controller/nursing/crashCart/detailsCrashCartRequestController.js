(function () {
    "use strict";

    function detailsCrashCartRequestController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, GenericServiceParamHeader , commonDetailService) {
        var self = this;
        var selectedItemId;
       
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
           
            self.model.GetDetailsCrashCartRequest = GetDetailsCrashCartRequest;
            self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
           

            GetDetailsCrashCartRequest(commonDetailService.getDataId());
        }


        var GetDetailsCrashCartRequest = function (itemId) {

            var URI = CONSTANTS.PHARMACY.NURSING.CRASH_CART.DETAIL_API+ $scope.unitId + "/" + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                self.model.crashCartRequestDetailslist = response.data;
                angular.forEach(self.model.crashCartRequestDetailslist.items, function(item){
                    item.uom.find(function(d){
                        if(d.uomType == item.uomType){
                            item.uomType = d.uomDetails;
                        }
                    });
                    var uomTypeData = item.uomType.split("-");
                    if(item.isThreeUOMApplicable == false){
                        if(uomTypeData[0] == "P "){
                            item.receivedQtyCon = (item.requiredQuantity +"*"+ item.uom[0].conversion);
					        item.receivedQtyCalculation = (item.requiredQuantity * item.uom[0].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else if(uomTypeData[0] == "S "){
                            
                        }
                        else{
                            item.receivedQtyCon = (item.requiredQuantity);
					        item.receivedQtyCalculation = (item.requiredQuantity);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    else{
                        if(uomTypeData[0] == "P "){
                            item.receivedQtyCon = (item.requiredQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
					        item.receivedQtyCalculation = (item.requiredQuantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else if(uomTypeData[0] == "S "){
                            item.receivedQtyCon = (item.requiredQuantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.requiredQuantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.requiredQuantity);
					        item.receivedQtyCalculation = (item.requiredQuantity);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                });
                $rootScope.allergydata(response.data);
                getAllPatientDiagnosisForIcon(response.data.patientId);
                $rootScope.stopLoader();
            });
        }
        
        var getAllPatientDiagnosisForIcon= function(patientId){
            self.model.patientDiagnosisListIcon =[];
            var cookieObject = $cookies.getObject('cookieObject');
             var headerObjDia = {
                         'unit_id' : cookieObject.unitId,
                        'organization_id' : cookieObject.organizationId,
                        'patient_id' : patientId,
                       'Content-Type':'application/json',
             };
             var data={};
             var URI = CONSTANTS.ROOT_URL
             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
             GenericServiceParamHeader
             .serviceAction(METHOD_GET, URI,
                data,
                NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
             .then(
                     function(response) {
                         console.log(response);
                         // $rootScope.startLoader();
                         if (response.data.status == "success")
                         self.model.patientDiagnosisListIcon = response.data.listObject; 
                         console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                             self.model.comorbidityFlag = false;
                             self.model.notifiableFlag = false;
                            
                                if(self.model.patientDiagnosisListIcon.length > 0)
                                    {
                                        self.model.complaintDelete=false;
                                        angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                            if(value.isNotifiable == 'Y')
                                            {
                                                self.model.notifiableFlag = true;
                                            }
                                            else if(value.isComorbidity == 'Y')
                                            {
                                                self.model.comorbidityFlag = true;
                                            }
                                        });
                                    }
                         
                     });
            }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsCrashCartRequest", {
            url: "/detailsCrashCartRequest",
            templateUrl:'views/PharmacyInventory/InPatient/details-crash-cart-request.html',
            controller: "DetailsCrashCartRequest.Controller",
            controllerAs: "vm",
            title: 'Crash Cart Request'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsCrashCartRequest.Controller", detailsCrashCartRequestController)
})();