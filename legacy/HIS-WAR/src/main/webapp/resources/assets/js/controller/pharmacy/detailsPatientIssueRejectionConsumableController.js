(function () {
    "use strict";

    function detailsPatientIssueRejectionConsumableController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isAcceptQuantityValid: [],
                SelectedRequestItems: [],
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })


            self.model.GetDetailsPatientIssueRejectionConsumable = GetDetailsPatientIssueRejectionConsumable;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveIssueRejection = SaveIssueRejection;
            self.model.NavigatetoPatientIssueRejection = NavigatetoPatientIssueRejection;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;

            GetDetailsPatientIssueRejectionConsumable(commonDetailService.getDataId());
        }
        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = reason;
        });

        var GetDetailsPatientIssueRejectionConsumable = function (itemId) {

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.CONSUMABLE_DETAIL_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.detailsPatientIssueRejectionConsumableList = response.data;
                    self.model.detailsPatientIssueRejectionConsumableList.items.forEach(function(item){
                        item.uomDatalist = item.uomList.find(function (uom) {
                            return item.uom == uom.uomType;
                          });     
                          item.uom = item.uomDatalist.uomDetails;
                          if(item.isThreeUOMApplicable == false){
                              item.uomListdata = item.uomList;
                              item.uomList = [];
                              angular.forEach(item.uomListdata, function(data){
                                if(data.uomType != 'Stock'){
                                  item.uomList.push(data);
                                }
                              });
                            } 
                          CalculateStock(item, item.uomList, item.uomDatalist);
                        item.isDisabled = true;
                        item.acceptQuantity = item.acceptQuantity || item.issueRejectQuantity;
                        if(!!item.acceptQuantity && !!item.issueRejectQuantity){
                            ChangeItemStatus(item);
                            } 
                            if(!!item.rejectQuantity){
                                item.isRejected = true;
                                item.rejectReasonId = 1;
                            }
                    })
                    $rootScope.stopLoader();

                });
        }
        var CalculateStock = function (item,uomList, uomNew) {
            if (!!uomNew.uomTypeId) {
                uomNew.uomDetailsData = uomNew.uomDetails.split(' ')[0];
              if(item.isThreeUOMApplicable == false){
                if (uomNew.uomDetailsData == "P") {
                  item.receivedQtyCon=(item.uomListdata[0].conversion+"*"+ item.uomListdata[2].conversion);
                } else if (uomNew.uomDetailsData == "S") {
                } else if (uomNew.uomDetailsData == "D") {
                  item.receivedQtyCon=(item.uomListdata[2].conversion);
                }
              }
              else{
                if (uomNew.uomDetailsData == "P") {
                  item.receivedQtyCon=(uomList[0].conversion+"*"+ uomList[1].conversion +"*"+ uomList[2].conversion);
                } else if (uomNew.uomDetailsData == "S") {
                  item.receivedQtyCon=(uomList[1].conversion +"*"+ uomList[2].conversion);
                } else if (uomNew.uomDetailsData == "D") {
                  item.receivedQtyCon=(uomList[2].conversion);
                }
              }
            } 
          }
        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientIssueRejectionConsumableList.items.forEach(function (item) {
                    item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientIssueRejectionConsumableList.items.forEach(function (item) {
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

            if (self.model.detailsPatientIssueRejectionConsumableList.items.every(isSelected)) {
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
                growl.error("Please add some items in the bill");
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isAcceptQuantityValid'] = false;

                    if ((item.issueRejectQuantity < (item.acceptQuantity))) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity less than issue reject quantity");
                    }

                    if (!item.acceptQuantity) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity");
                    }

                    if (item.acceptQuantity == 0) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than zero");
                    }
                });
            }
            return isValid;
        }

        var ClearFields = function (itemList) {

            itemList.Id = "1" + (new Date()).getTime();
            itemList.issueRejectNo = null;
            itemList.issueRejectDate = null;
            itemList.issueNo = null;
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
                    item.acceptQuantity = item.issueRejectQuantity - self.model.qty;
                    item['rejectReasonId'] = self.model.rejectReason.id;
                    item['note'] = self.model.rejectNote;
                }
            });
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
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
            if(item.issueRejectQuantity > parseInt(item.acceptQuantity)){
                // item.statusId =  SetStatus('Partially Completed');
            } else if(item.issueRejectQuantity == parseInt(item.acceptQuantity)) {
                item.statusId =  SetStatus('Completed');
            }
        } 

        var SaveIssueRejection = function (itemList) {
            if (ValidateBill(itemList)) {

                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.CONSUMABLE_SAVE_API;
                var data = {
                    "id": itemList.id,
                    "issueId": itemList.issueId,
                    "wardId": itemList.wardId,
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
                            "batchMapperId": item.batchMapperId,
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectQuantity": item.rejectQuantity,
                            "reasonId": item.rejectReasonId,
                            "note": item.note
                        }
                         data.items.push(PatientData);
                    }
                });
                data.items.forEach(function(key){
                    var itemStatusList = [];
                    itemStatusList.push(key.statusId);

                    if(itemStatusList.indexOf(4) !== -1){ // Completed status id
                        data.statusId = 4; // Completed stutus
                    } else {
                        data.statusId = 2; //  Pending stutus
                    }
                });
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NavigatetoPatientIssueRejection();
                        growl.success('Patient issue Rejection successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving patient issue Rejection');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var NavigatetoPatientIssueRejection = function () {
            document.getElementById('popupClose').click();
            setTimeout(function(){ $state.go('patientIssueRejection'); }, 1000);
          }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsPatientIssueRejectionConsumable", {
            url: "/detailsPatientIssueRejectionConsumable",
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-issue-rejection-consumable.html',
            controller: "DetailsPatientIssueRejectionConsumable.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Rejection'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientIssueRejectionConsumable.Controller", detailsPatientIssueRejectionConsumableController)
})();

