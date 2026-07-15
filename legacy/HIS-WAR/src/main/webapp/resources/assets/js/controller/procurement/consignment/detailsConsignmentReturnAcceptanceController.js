(function () {
    "use strict";

    function detailsConsignmentReturnAcceptanceController($scope, $rootScope, $http, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, $cookies, CONSTANTS, rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;

        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isRejectQuantityValid: [],
                SelectedRequestItems: [],
              
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
              });
            self.model.GetDetailsPatientReturnAcceptanceMedication = GetDetailsPatientReturnAcceptanceMedication;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveImplantReturnAcceptance = SaveImplantReturnAcceptance;
            self.model.NevigateToPatientReturnAcceptance = NevigateToPatientReturnAcceptance;
            self.model.CancelRejection = CancelRejection;
            self.model.ChangeItemStatus = ChangeItemStatus;
            
            GetDetailsPatientReturnAcceptanceMedication(commonDetailService.getDataId());
        }

        var GetDetailsPatientReturnAcceptanceMedication = function (itemId) {
        //var URI =CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN_ACCEPTANCE.DETAILS_API+ itemId;
            var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_DETAIL_API + $scope.unitId + '/' + itemId;
          var data={
            "bedNo": "BED-0010020020022072",
            "doctorDetail": "Benedict | A Drinkwater | Orthopaedics",
            "id": 45,
            "indentId": 53,
            "indentNo": "IND-001",
            "patientDetail": "Mr Dev Datta Pandey | 37 | M",
            "returnDate": 1544723413799,
            "returnNo": "ImpRet-001",
            "status": "Pending",
            "statusId": 2,
            "store": "Main Store",
            "uhid": "PC-18-0000629",
            "visitType": "IP",
            "wardId": 2,
            "wardName": "Ward2",
            "items": [
                {
                    "batchNo": "bat-4444",
                    "expiry": "2019-12-31",
                    "genericName": "Gen-Pharma",
                    "id": 26,
                    "indentDetailId": 62,
                    "issueDetailBatchMapper": 51,
                    "itemId": null,
                    "itemName": "azeal",
                    "rate": 100,
                    "reasonForReturn": "DAMAGE",
                    "returnQuantity": 30,
                    "status": "Pending",
                    "statusId": 2,
                    "uom": "Purchase",
                    "uomUnit": "BOX"
                }
            ]
           
        };
            self.model.detailsImplantReturnAcceptanceConsignmentList = data;
            self.model.detailsImplantReturnAcceptanceConsignmentList.items.forEach(function(item){
                item.isDisabled = true;
                item.acceptQuantity =  item.acceptQuantity || item.returnQuantity;
                if(!!item.acceptQuantity && !!item.returnQuantity){
                    ChangeItemStatus(item);
                    } 
                    if(!!item.rejectQuantity){
                        item.isRejected = true;
                        item.rejectReasonId = 1;
                    }
            })
          /*   $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.detailsImplantReturnAcceptanceConsignmentList = response.data;
                    self.model.detailsImplantReturnAcceptanceConsignmentList.items.forEach(function(item){
                        item.isDisabled = true;
                        item.acceptQuantity =  item.acceptQuantity || item.returnQuantity;
                        if(!!item.acceptQuantity && !!item.returnQuantity){
                            ChangeItemStatus(item);
                            } 
                            if(!!item.rejectQuantity){
                                item.isRejected = true;
                                item.rejectReasonId = 1;
                            }
                    })
                });
            $rootScope.stopLoader(); */
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsImplantReturnAcceptanceConsignmentList.items.forEach(function (item) {
                    item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsImplantReturnAcceptanceConsignmentList.items.forEach(function (item) {
                    item.isSelected = false;
                    item.isDisabled = true;
                });
            }
            // console.log(self.model.SelectedRequestItems);
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectRequestItem = function (item) {

            var itemIds = self.model.SelectedRequestItems;

            if (item.isSelected) {
                itemIds.push(item);
                item.isDisabled = false;
            } else {
                var index = itemIds.indexOf(item);
                item.isDisabled = true;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.detailsImplantReturnAcceptanceConsignmentList.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function (itemList) {
            var isValid = true;

            if(self.model.rejectionFlag == false){
                var isValid = false;
                growl.error("Please save rejection reason");
            }
            
            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items.");
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isRejectQuantityValid'] = false;

                    if ((item.returnQuantity < (item.acceptQuantity)) || !item.acceptQuantity) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter valid quantity");
                    }

                    if (item.acceptQuantity == 0) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than zero");
                    }
                });
            }
            return isValid;
        }

        var ClearFields = function (itemList) {

            itemList.Id = "1" + (new Date()).getTime();
            itemList.rejectedNo = null;
            itemList.rejectedDate = null;
            itemList.wardName = null;
            itemList.uhid = null;
            itemList.patientDetail = null;
            itemList.doctorDetail = null;
            itemList.store = null;
            itemList.items = [];
        }

        var RejectQty = function (item) {
            self.model.selectedItem = item;

            if (item.isRejected) {
                selectedItemId = item.id;
                self.model.qty = item.rejectQuantity;
                self.model.rejectReason = self.model.rejectReasons[0];
                self.model. rejectionFlag = false;

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
              } 
              else {
                item.isRejected = true;
                item.rejectReasonId = 1;
                var popup = angular.element('#cancelStatus');
                popup.modal('show');
              } 
        }

        var SaveRejectPopup = function () {

            self.model.SelectedRequestItems.forEach(function (item) {
                
                if (item.id == selectedItemId) {
                    item.rejectQuantity = self.model.qty;
                    item.acceptQuantity = item.returnQuantity - self.model.qty;
                    item['rejectReasonId'] = self.model.rejectReason.id;
                    item['note'] = self.model.rejectNote;
                }
            });
            
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
        }

        var CancelRejection = function(itemList){
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.quantityIssued;
        }

        var ChangeItemStatus = function(item){
            if(!!item.rejectQuantity){
                item.isRejected = true;
                item.rejectReasonId = 1;
            }
            if(item.returnQuantity > parseInt(item.acceptQuantity)){
                 item.statusId =  SetStatus('Partially Completed');
            } else if(item.returnQuantity == parseInt(item.acceptQuantity)) {
               item.statusId =  SetStatus('Closed');
            }
        } 

        var SaveImplantReturnAcceptance = function (itemList) {
            
            if (ValidateBill(itemList)) {
                //var URI =CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN_ACCEPTANCE.SAVE_API;
                var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_SAVE_API;

                var data = {
                    "id": itemList.id,
                     "statusId": SetStatus('Closed'),
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                }
                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        var PatientData = {
                            "id": item.id,
                            "statusId": item.statusId,
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectedQuantity": item.rejectQuantity,
                            "rejectReason": item.rejectReasonId,
                            "rejectedNote": item.note,
                            "issueDetailBatchMapper":item.issueDetailBatchMapper,
                        }
                         data.items.push(PatientData);
                    }
                });
                data.items.forEach(function(key){
                    var itemStatusList = [];
                    itemStatusList.push(key.statusId);

                    if(itemStatusList.indexOf(8) !== -1){ // Closed status id
                        data.statusId = 8; // Closed stutus
                    } else {
                        data.statusId = undefined; //  no stutus
                    }
                });
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NevigateToPatientReturnAcceptance();
                        growl.success('Implant return acceptance Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving implant return acceptance');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var NevigateToPatientReturnAcceptance = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('returnAcceptance');
            }, 1000);
        }
        function SetStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
              return obj.status == x;
            });
      
            return status.id;
          }
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsConsignmentReturnAcceptance", {
            url: "/detailsConsignmentReturnAcceptance",
            templateUrl: 'views/procurement/consignment/details-consignment-return-acceptance.html',
            controller: "DetailsConsignmentReturnAcceptance.Controller",
            controllerAs: "vm",
            title: 'Return Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsConsignmentReturnAcceptance.Controller", detailsConsignmentReturnAcceptanceController)
})();
