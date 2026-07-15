(function () {
    "use strict";

    function detailsPendingRejectedRejectionConsumableController($scope, $rootScope, $http, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, $cookies, CONSTANTS, rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;
       

        //Default Constructor
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

            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.GetDetailsPendingRejectedRejectionsConsumable = GetDetailsPendingRejectedRejectionsConsumable;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveRejectedRejection = SaveRejectedRejection;
            self.model.NavigateToPendingRejectedRejections = NavigateToPendingRejectedRejections;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.ApprovedQuantity = ApprovedQuantity;
            self.model.ValidateAcceptQuantity =ValidateAcceptQuantity;

            GetDetailsPendingRejectedRejectionsConsumable(commonDetailService.getDataId());
        }
        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = reason;
        });

        var GetDetailsPendingRejectedRejectionsConsumable = function (itemId) {

            var URI =  CONSTANTS.PHARMACY.IP.PENDING_REJECTED_REJECTION.CONSUMABLE_DETAIL_API + $scope.unitId + "/" + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.detailsPendingRejectedRejectionsConsumableList = response.data;
                    self.model.detailsPendingRejectedRejectionsConsumableList.items.forEach(function(item){
                    	item.uomListdata1=item.uomList;
                        item.uomDatalist = item.uomList.find(function (uom) {
                            return item.uom == uom.uomType;
                          });     
                          item['uomDetails'] = item.uomDatalist.uomDetails;
                          if(item.isThreeUOMApplicable == false){
                              item.uomListdata = item.uomList;
                              item.uomList = [];
                              angular.forEach(item.uomListdata, function(data){
                                if(data.uomType != 'Stock'){
                                  item.uomList.push(data);
                                }
                              });
                            }
                        item.isDisabled = true;
                        if(  self.model.detailsPendingRejectedRejectionsConsumableList.status=='Completed')
                        {
                            item.isSelected = true;    

                        }
                        if(item.status == 'Pending'){
                            item.acceptQuantity = item.rejectedQuantity;
                        }
                        else{
                            item.acceptQuantity = item.acceptQuantity || item.rejectedQuantity;
                        }
                        if(!!item.acceptQuantity && !!item.rejectedQuantity){
                            ChangeItemStatus(item);
                        } 
                        if(!!item.rejectQuantity){
                            item.isRejected = true;
                            item.rejectReasonId = 1;
                        }
                        CalculateStock(item, item.uomList, item.uomDatalist);
                        item.price = commonDetailService.calculateRate(item.uomListdata1, item.uomListdata1.uomTypeId, item.price);
                        if(item.markUpDownPer != undefined && item.markUpDownPer != null && item.markUpDownPer != 0){
                            if(item.markUpDown == 1){
                              item.price = fixedDecimal(item.price + (item.price * (item.markUpDownPer/100)));
                            } else if (item.markUpDown == 0){
                              item.price = fixedDecimal(item.price - (item.price * (item.markUpDownPer/100)));
                            } else {
                              item.price = fixedDecimal(item.price);
                            }
                        }
                        else{
                            item.price = fixedDecimal(item.price);
                        }
                    })
                    $rootScope.stopLoader();
                });
        }

        var fixedDecimal = function(amount){
            //Fixed amount to 2 decimal places
            if(amount != undefined || amount != null){
              return Number(parseFloat(amount.toString()).toFixed(2));
            }
            else{ 
              return Number(parseFloat(amount));
            }
        }

        var CalculateStock = function (item,uomList, uomNew) {
            if (!!uomNew.uomTypeId) {
                uomNew.uomDetailsData = uomNew.uomDetails.split(' ')[0];
              if(item.isThreeUOMApplicable == false){
                if(item.acceptQuantity == undefined || item.acceptQuantity == null){
                    if (uomNew.uomDetailsData == "P") {
                        item.receivedQtyCon=(item.rejectedQuantity +"*"+ item.uomListdata[0].conversion);
                        item.receivedQtyCalculation=(item.rejectedQuantity * item.uomListdata[0].conversion);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                    } else if (uomNew.uomDetailsData == "S") {
                    } else if (uomNew.uomDetailsData == "D") {
                        item.receivedQtyCon=(item.rejectedQuantity);
                        item.receivedQtyCalculation=(item.rejectedQuantity);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                    }
                }
                else{
                    if (uomNew.uomDetailsData == "P") {
                        item.receivedQtyCon=(item.acceptQuantity +"*"+ item.uomListdata[0].conversion);
                        item.receivedQtyCalculation=(item.acceptQuantity * item.uomListdata[0].conversion);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                    } else if (uomNew.uomDetailsData == "S") {
                    } else if (uomNew.uomDetailsData == "D") {
                        item.receivedQtyCon=(item.acceptQuantity);
                        item.receivedQtyCalculation=(item.acceptQuantity);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                    }
                }
                
              }
              else{
                if(item.acceptQuantity == undefined || item.acceptQuantity == null){
                    if (uomNew.uomDetailsData == "P") {
                        item.receivedQtyCon=(item.rejectedQuantity +"*"+ uomList[0].conversion+"*"+ uomList[1].conversion);
                        item.receivedQtyCalculation=(item.rejectedQuantity * uomList[0].conversion * uomList[1].conversion);
                        item.uomUnitData = uomList[2].uomUnit;
                    } else if (uomNew.uomDetailsData == "S") {
                        item.receivedQtyCon=(item.rejectedQuantity +"*"+ uomList[1].conversion);
                        item.receivedQtyCalculation=(item.rejectedQuantity * uomList[1].conversion);
                        item.uomUnitData = uomList[2].uomUnit;
                    } else if (uomNew.uomDetailsData == "D") {
                        item.receivedQtyCon=(item.rejectedQuantity);
                        item.receivedQtyCalculation=(item.rejectedQuantity);
                        item.uomUnitData = uomList[2].uomUnit;
                    }
                }
                else{
                    if (uomNew.uomDetailsData == "P") {
                        item.receivedQtyCon=(item.acceptQuantity +"*"+ uomList[0].conversion+"*"+ uomList[1].conversion);
                        item.receivedQtyCalculation=(item.acceptQuantity * uomList[0].conversion * uomList[1].conversion);
                        item.uomUnitData = uomList[2].uomUnit;
                    } else if (uomNew.uomDetailsData == "S") {
                        item.receivedQtyCon=(item.acceptQuantity +"*"+ uomList[1].conversion);
                        item.receivedQtyCalculation=(item.acceptQuantity * uomList[1].conversion);
                        item.uomUnitData = uomList[2].uomUnit;
                    } else if (uomNew.uomDetailsData == "D") {
                        item.receivedQtyCon=(item.acceptQuantity);
                        item.receivedQtyCalculation=(item.acceptQuantity);
                        item.uomUnitData = uomList[2].uomUnit;
                    }
                }
              }
            } 
        }

        $scope.calculateReceivedQty = function (item) {
            if(item.isThreeUOMApplicable == false){
                if(item.uomDatalist.uomTypeId == 1){
                    item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomListdata[0].conversion);
                    item.receivedQtyCalculation = (item.acceptQuantity * item.uomListdata[0].conversion);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                }
                else if(item.uomDatalist.uomTypeId == 2){
                
                }
                else{
                    item.receivedQtyCon = (item.acceptQuantity);
                    item.receivedQtyCalculation = (item.acceptQuantity);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                }
            }
            else{
                if(item.uomDatalist.uomTypeId == 1){
                    item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.acceptQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomDatalist.uomTypeId == 2){
                    item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.acceptQuantity * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                    item.receivedQtyCon = (item.acceptQuantity);
                    item.receivedQtyCalculation = (item.acceptQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsPendingRejectedRejectionsConsumableList.items.forEach(function (item) {
                    item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsPendingRejectedRejectionsConsumableList.items.forEach(function (item) {
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
                item.rejectQuantity = 0;
                item.isRejected = false;
                var index = itemIds.indexOf(item);
                item.isDisabled = true;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.detailsPendingRejectedRejectionsConsumableList.items.every(isSelected)) {
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
                    item['isRejectQuantityValid'] = false;

                    if ((item.rejectedQuantity < (item.acceptQuantity))) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity less than rejected quantity");
                    }

                    // if (!item.acceptQuantity) {
                    //     item['isRejectQuantityValid'] = true;
                    //     isValid = false;
                    //     growl.error("Please enter quantity");
                    // }

                    // if (item.acceptQuantity == 0) {
                    //     item['isRejectQuantityValid'] = true;
                    //     isValid = false;
                    //     growl.error("Please enter quantity greater than zero");
                    // }
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
            var isInvalid = false;
            var isInvalid1 = false;
            self.model.SelectedRequestItems.forEach(function (item) {

                if (item.id == selectedItemId) {
                    if(item.rejectedQuantity < parseInt(self.model.qty)){
                        isInvalid = true;
                    }
                    if(!self.model.qty || self.model.qty == 0){
                        isInvalid1 = true;
                    }
                    if(isInvalid){
                        growl.error('Please Enter Reject Quantity Less Than Accepted Quantity');
                        item.isRejected = false;
                        return false;
                    }
                    if(isInvalid1){
                        growl.error("Please enter rejected quantity");
                        item.isRejected = false;
                        return false;
                    }
                    if(!isInvalid || !isInvalid1){
                        item.rejectQuantity = self.model.qty;
                        if(item.isRejected){
                            item.acceptQuantity = item.rejectedQuantity - self.model.qty;
                        }
                        item['rejectReasonId'] = self.model.rejectReason.id;
                        item['note'] = self.model.rejectNote;
                        $scope.calculateReceivedQty(item);
                    }
                }
            });
           
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
            var popup = angular.element('#qtyaccepatReject');
            popup.modal('hide');
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
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.rejectedQuantity;
            $scope.calculateReceivedQty(self.model.selectedItem);
        }
        
        var ChangeItemStatus = function(item){
            if(!!item.rejectQuantity){
                item.isRejected = true;
                // item.rejectReasonId = 1;
            }
            // if(item.rejectedQuantity > parseInt(item.acceptQuantity)){
            //     item.statusId =  SetStatus('Partially Completed');
            // } else if(item.rejectedQuantity == parseInt(item.acceptQuantity)) {
            //     item.statusId =  SetStatus('Completed');
            // }
        }

        var ApprovedQuantity = function (item) {
            if (item.acceptQuantity > item.rejectedQuantity) {
                item.rejectQuantity = 0;
                self.model.rejectQty = undefined;
                item.isRejected = false;
            } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
                item.rejectQuantity = item.rejectedQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = true;
                RejectQty(item);
            } else if (item.acceptQuantity < item.rejectedQuantity) {
                item.rejectQuantity = item.rejectedQuantity - item.acceptQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = true;
                RejectQty(item);
            } else if (item.acceptQuantity == item.rejectedQuantity) {
                item.rejectQuantity = item.rejectedQuantity - item.acceptQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = false;
            }
        }

        var ValidateAcceptQuantity = function (item) {
            var isValid = true;
            if (parseInt(item.acceptQuantity) > item.rejectedQuantity) {
                item.isRejectQuantityValid = true;
                isValid = false;
                growl.error('Entered accept quantity is exceeding the quantity issued');
                item.acceptQuantity = 0;
            } else {
                item.acceptQuantity = parseInt(item.acceptQuantity);
                isValid = true;
            }

            return isValid;
        }

        var SaveRejectedRejection = function (itemList) {
            if (ValidateBill(itemList)) {

                var URI =  CONSTANTS.PHARMACY.IP.PENDING_REJECTED_REJECTION.CONSUMABLE_SAVE_API;

                var data = {
                    "id": itemList.id,
                    "issueId": itemList.issueId,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId,
                    "statusId": SetStatus('Completed'),
                }
                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        var PatientData = {
                            "id": item.id,
                            "statusId": SetStatus('Completed'),
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectQuantity": item.rejectQuantity  ? item.rejectQuantity:0,
                            "reasonId": item.rejectReasonId,
                            "note": item.note,
                            "batchMapperId": item.batchMapperId
                        }
                         data.items.push(PatientData);
                    }
                });
                // data.items.forEach(function(key){
                //     var itemStatusList = [];
                //     itemStatusList.push(key.statusId);

                //     if(itemStatusList.indexOf(4) !== -1){ // Completed status id
                //         data.statusId = 4; // Completed stutus
                //     } else {
                //         data.statusId = 18; //  Partially Completed stutus
                //     }
                // });
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NavigateToPendingRejectedRejections();
                        growl.success('Patient Rejected Rejection Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving patient Rejected Rejection');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        // var NavigateToPendingRejectedRejections = function () {
        //     $state.go('pendingRejectedRejections');
        //   }
          var NavigateToPendingRejectedRejections = function () {
            // $state.go('pendingRejectedRejections');
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('pendingRejectedRejections');
            }, 1000);
          }

              //Method to get the list of Generic 
    var GetGenericList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.genericList = response.data;
            GetGenericMaster();
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }
      GetGenericList();
  
      var genericMasterList = [];
      var GetGenericMaster = function () {
        var arraylist = self.model.genericList.filter(x => x.status == true);
        for (let generic of arraylist){
          genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
      } 

      var GetStrengthUnitList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.strengthUnitList = response.data;
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            },
            function errorCallback(response) {
              console.log("Error");
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            });
      }
      GetStrengthUnitList();
  
      var ViewCombinationDetails = function(item) {
        self.model.genericCombinationList = [];
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            for(let combination of response.data) {
              let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
              combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
              combination.code = item.code;
              combination.genericName = item.name;
            }
            self.model.strengthUnitList.forEach(function (strength){
                response.data.forEach(function (data){
                  if(data.strength == strength.id){
                    $scope.strength = strength.unit;
                    var obj = {
                      strength: "",
                      code:"",
                      genericId: "",
                      genericName: "",
                      id:"",
                      name:"",
                      strengthUnit:"",
                      subGenericId: "",
                    }
                    obj.strength = $scope.strength;
                    obj.code = data.code;
                    obj.genericId = data.genericId;
                    obj.genericName = data.genericName;
                    obj.id = data.id;
                    obj.name = data.name;
                    obj.strengthUnit = data.strengthUnit;
                    obj.subGenericId = data.subGenericId;
                    self.model.genericCombinationList.push(obj);
                  }
                });
              });
            var popup = angular.element("#combinationDetails");
            popup.modal("show");
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }

        initializeController();
    }
    
    function config($stateProvider) {
        $stateProvider.state("detailsPendingRejectedRejectionConsumable", {
            url: "/detailsPendingRejectedRejectionConsumable",
            templateUrl: 'views/PharmacyInventory/InPatient/details-pending-rejected-rejection-consumable.html',
            controller: "DetailsPendingRejectedRejectionConsumable.Controller",
            controllerAs: "vm",
            title: 'Pending Rejected Rejection'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPendingRejectedRejectionConsumable.Controller", detailsPendingRejectedRejectionConsumableController)
})();
