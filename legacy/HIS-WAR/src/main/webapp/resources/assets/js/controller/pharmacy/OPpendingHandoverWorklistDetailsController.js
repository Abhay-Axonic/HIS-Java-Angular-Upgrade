(function () {
    "use strict";

    function OPpendingHandoverWorklistDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, detailService, StatusService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                receiverDisabled : false
            };
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            //Methods
            self.model.SavePopup = SavePopup;
            self.model.NavigateToPendingHandoverWorkList = NavigateToPendingHandoverWorkList;
        }

        var Validation = function (itemList) {
            var isValid = true;

            if (!itemList.items || itemList.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            }
            else {
                if(self.model.receiverName == undefined || self.model.receiverName == ''){
                    isValid = false;
                    self.model.isReceiverValid = true;
                }
            }
            return isValid;
        }

        var ClearFields = function (itemList) {
            itemList.uhid = null;
            itemList.doa = null;
            itemList.patientDetail = null;
            itemList.doctorDetail = null;
            itemList.pdd = null;
            itemList.dob = null;
            itemList.wardName = null;
            itemList.bedNo = null;
            self.model.receiverName = null;
            itemList.items = [];
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var NavigateToPendingHandoverWorkList = function () {
            $state.go('OPpendingHandoverWorklist');
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        initializeController();
    }


    function config($stateProvider) {
        $stateProvider.state("OPpendingHandoverWorklistDetails", {
            url: "/OPpendingHandoverWorklistDetails",
            templateUrl: 'views/PharmacyInventory/InPatient/op-pending-handover-worklist-details.html',
            controller: "OPpendingHandoverWorklistDetails.Controller",
            controllerAs: "vm",
            title: 'Pending Handover Worklist'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("OPpendingHandoverWorklistDetails.Controller", OPpendingHandoverWorklistDetailsController)
})();
