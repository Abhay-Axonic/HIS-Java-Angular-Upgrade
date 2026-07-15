

(function () {
    "use strict";

    function detailsDeliveryWokrlistController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,GenericServiceParamHeader, $filter, $state, growl, CurrencyService, StatusService, commonDetailService) {
        var self = this;
       
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;


            self.model = {
                carrierList: [{
                    "carrierName": "Select Carrier"
                }]               
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.GetDetailDeliveryWorklist = GetDetailDeliveryWorklist;
            self.model.SavePopup = SavePopup;
            self.model.SaveDeliveryWorklist = SaveDeliveryWorklist;
            self.model.ClearFields = ClearFields;
            self.model.NavigateToDeliveryWorklist = NavigateToDeliveryWorklist;
            self.model.narcoticDeliveryId = commonDetailService.getDataId();
            self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
            GetDetailDeliveryWorklist(commonDetailService.getDataId());
        }
      
        var GetDetailDeliveryWorklist = function (itemId) {
            var URI =  CONSTANTS.PHARMACY.NARCOTICS.DELIVERY_WORKLIST.DETAIL_API +$scope.organizationId + "/" + $scope.unitId+"/"+$scope.storeId+"/" + itemId;
            // self.model.carrier = self.model.carrierList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	  self.model.isCarrierValid = false //HIS-10980
                      self.model.deliveryWorklistDetails = response.data;
                	  $rootScope.allergydata(response.data);
		              getAllPatientDiagnosisForIcon(response.data.patientId);
                    response.data.carriers.forEach(function(carrierData){
                            self.model.carrierList = self.model.carrierList.concat(carrierData);
                            self.model.carrier = self.model.carrierList[1];
                    })
                    $rootScope.stopLoader();
                });
        }


        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var SaveDeliveryWorklist = function (bill) {

            if (ValidateBill(bill)) {
                var URI = CONSTANTS.PHARMACY.NARCOTICS.DELIVERY_WORKLIST.SAVE_API;

                var data = {
                    "id": !!bill.id ? bill.id : undefined,
                    "carrierId": !!self.model.carrier && !!self.model.carrier.id ? self.model.carrier.id : undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                }

                console.log(data)

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        ClearFields(bill);
                        growl.success('Delivery worklist Successfully saved');
                        $rootScope.stopLoader();
                        NavigateToDeliveryWorklist();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving delivery worklist');
                    });
            }
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
        
        var NavigateToDeliveryWorklist = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('deliveryWorklist',{
                    obj: self.model.narcoticDeliveryId
                });
            }, 1000);
        }

        var ClearFields = function (bill) {

            bill.Id = "1" + (new Date()).getTime();
            bill.uhid = null;
            bill.patientDetail = null;
            bill.dob = null;
            bill.pdd = null;
            bill.doa = null;
            bill.wardName = null;
            bill.bedNo = null;
            bill.prescriptionDate = null;
            bill.doctorDetail = null;
            bill.prescriptionId = null;
           
            bill.items = [];
        }

        var ValidateBill = function (bill) {

            var isValid = true;

            if (!bill.items || bill.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            }
            if (!self.model.carrier.id) {
                isValid = false;
                self.model.isCarrierValid = true
                growl.error("Please select carrier");
            }
       
            return isValid;
        }


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsDeliveryWokrlist", {
            url: "/detailsDeliveryWorklist",
            templateUrl: 'views/PharmacyInventory/NarcoticsStore/details-delivery-wokrlist.html',
            controller: "DetailsDeliveryWokrlistController.Controller",
            controllerAs: "vm",
            title: 'Delivery Worklist'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsDeliveryWokrlistController.Controller", detailsDeliveryWokrlistController)
})();
