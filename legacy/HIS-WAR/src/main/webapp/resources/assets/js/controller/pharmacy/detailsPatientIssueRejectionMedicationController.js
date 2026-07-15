(function () {
    "use strict";

    function detailsPatientIssueRejectionMedicationController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;
        var itemId;
        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isRejectQuantityValid: [],
                SelectedRequestItems: [],

            };

            // Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })


            self.model.GetDetailsPatientIssueRejectionMedication = GetDetailsPatientIssueRejectionMedication;
            self.model.SavePopup = SavePopup;
            self.model.closepopup = closepopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveIssueRejection = SaveIssueRejection;
            self.model.NevigatetoPatientIssueRejection = NevigatetoPatientIssueRejection;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;
            self.model.ApprovedQuantity = ApprovedQuantity;
            self.model.valueOnChange = valueOnChange;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.SaveAcceptReject =SaveAcceptReject;
            self.model.ModalDismissRejectionPopup = ModalDismissRejectionPopup;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.SetFocus = SetFocus;
            self.model.BarcodeScan = BarcodeScan;
            GetDetailsPatientIssueRejectionMedication(commonDetailService.getDataId());
        }
        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = reason;
        });

        var GetDetailsPatientIssueRejectionMedication = function (itemId) {

          var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.MEDICATION_DETAIL_API + itemId;

          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(function (response) {
            self.model.detailsPatientIssueRejectionMedicationList = response.data;
            self.model.requestItems =  self.model.detailsPatientIssueRejectionMedicationList;
            self.model.detailsPatientIssueRejectionMedicationList.items.forEach(function (item) {
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
              // item.acceptQuantity = item.acceptQuantity || item.issueRejectQuantity;
              if (!!item.acceptQuantity && !!item.issueRejectQuantity) {
                ChangeItemStatus(item);
                if (!!item.rejectQuantity) {
                  item.isRejected = true;
                  item.rejectReasonId = 1;
                }
              }
              item.price = commonDetailService.calculateRate(item.uomList, item.uomDatalist.uomTypeId, item.price);
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
                    item.receivedQtyCon=(item.issueRejectQuantity +"*"+ item.uomListdata[0].conversion);
                    item.receivedQtyCalculation =(item.issueRejectQuantity * item.uomListdata[0].conversion);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                  } else if (uomNew.uomDetailsData == "S") {
                  } else if (uomNew.uomDetailsData == "D") {
                    item.receivedQtyCon=(item.issueRejectQuantity);
                    item.receivedQtyCalculation=(item.issueRejectQuantity);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                  }
                }
                else{
                  if (uomNew.uomDetailsData == "P") {
                    item.receivedQtyCon=(item.acceptQuantity +"*"+ item.uomListdata[0].conversion);
                    item.receivedQtyCalculation =(item.acceptQuantity * item.uomListdata[0].conversion);
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
                    item.receivedQtyCon=(item.issueRejectQuantity +"*"+ uomList[0].conversion +"*"+ uomList[1].conversion);
                    item.receivedQtyCalculation=(item.issueRejectQuantity * uomList[0].conversion * uomList[1].conversion);
                    item.uomUnitData = uomList[2].uomUnit;
                  } else if (uomNew.uomDetailsData == "S") {
                    item.receivedQtyCon=(item.issueRejectQuantity +"*"+ uomList[1].conversion);
                    item.receivedQtyCalculation=(item.issueRejectQuantity * uomList[1].conversion);
                    item.uomUnitData = uomList[2].uomUnit;
                  } else if (uomNew.uomDetailsData == "D") {
                    item.receivedQtyCon=(item.issueRejectQuantity);
                    item.receivedQtyCalculation=(item.issueRejectQuantity);
                    item.uomUnitData = uomList[2].uomUnit;
                  }
                }
                else{
                  if (uomNew.uomDetailsData == "P") {
                    item.receivedQtyCon=(item.acceptQuantity +"*"+ uomList[0].conversion +"*"+ uomList[1].conversion);
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
        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }
        var BarcodeScan = function (event, barcode) {
            var enterCode = 13;
            if (!barcode && event.which === enterCode) {
                barcode = self.model.barcode;
            }

            if (!!barcode) {
                var itemId = barcode.split(' ')[0];
                var batchId = barcode.split(' ')[1];

                if (!!itemId && !!batchId) {
                   // AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);
                }

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
        }
        
        
        
        
        
        
        
        
        
        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientIssueRejectionMedicationList.items.forEach(function (item) {
                    item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientIssueRejectionMedicationList.items.forEach(function (item) {
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

            if (self.model.detailsPatientIssueRejectionMedicationList.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function () {
            var isValid = true;
            $scope.saveBtnDisabled = false;
            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
                $scope.saveBtnDisabled = true;
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isRejectQuantityValid'] = false;

                    if (!!item.acceptQuantity) {
                        if (item.issueRejectQuantity < parseInt(item.acceptQuantity)) {
                            item['isRejectQuantityValid'] = true;
                            isValid = false;
                            growl.error("Please enter quantity less than issue reject quantity");
                            $scope.saveBtnDisabled = true;
                        }
                    } 
                    // else if (!item.acceptQuantity) {
                    //     item['isRejectQuantityValid'] = true;
                    //     isValid = false;
                    //     growl.error("Please enter valid quantity");
                    // }

                    if (parseInt(item.acceptQuantity) < 0) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter valid quantity");
                        $scope.saveBtnDisabled = true;
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

        // var RejectQty = function (item) {
        // self.model.selectedItem = item;
        // if (!!item.isRejected) {
        // selectedItemId = item.id;
        // self.model.rejectionFlag = false;

        // self.model.action = 'Open';
        // var popup = angular.element('#qtyaccepatReject');
        // popup.modal('show');
        // }
        // else {
        // item.isRejected = true;
        // item.rejectReasonId = 1;
        // var popup = angular.element('#cancelStatus');
        // popup.modal('show');
        // }
        // }

        var RejectQty = function (item) {
            self.model.onclose = item;
            self.model.rejectQty = 0;
            self.model.rejectReason = '';
            self.model.rejectNote = '';
            if (item.isRejected) {
              itemId = item.id;
              self.model.rejectQty = item.rejectQuantity;
              selectedItemId = item.id;
              $("#qtyaccepatReject").modal()
              self.model.reasons = self.model.rejectReasons;
            } else {
              CancelRejection(item);
            }
          }
        var ModalDismissRejectionPopup = function (item){
            item.items.forEach(function (data){
                var prevRequest = self.model.requestItems.items.find(function(prevItem){
                    return prevItem.id == data.id && selectedItemId == prevItem.id;
                });

                if (prevRequest) {
                    data.acceptQuantity = prevRequest.acceptQuantity + data.rejectQuantity;
                    data.rejectQuantity = 0;
                    data.isRejected = false;
                }
            });
            $('#qtyaccepatReject').modal('hide');
        }
       
      
          var CancelRejection = function (item) {
            // self.model.onclose.isReject = false;
            var prevRequest = self.model.requestItems.items.find(function (prevItem) {
              return prevItem.id == item.id;
            });
      
            if (prevRequest) {
              item.rejectQuantity = 0;
              item.acceptQuantity = prevRequest.issueRejectQuantity;
              item.rejectNote = prevRequest.rejectNote;
              item.rejectReasonId = prevRequest.rejectReasonId;
              item.statusId = prevRequest.statusId;
            }
            $scope.calculateReceivedQty(item);
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

        var ChangeItemStatus = function (item) {
            if (!!item.rejectQuantity) {
                item.isRejected = true;
                item.rejectReasonId = 1;
            }
            if (item.issueRejectQuantity > parseInt(item.acceptQuantity)) {
                // item.statusId = SetStatus('Partially Completed');
            } else if (item.issueRejectQuantity == parseInt(item.acceptQuantity)) {
                item.statusId = SetStatus('Completed');
            }
        }

        $scope.saveBtnDisabled = true;
        $scope.itemListCount = 0;
        var SaveIssueRejection = function (itemList) 
        {
        	$scope.itemListCount = itemList.items.length;
            $scope.saveBtnDisabled = false;
            if (!!ValidateBill()) {
                $scope.saveBtnDisabled = false;
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.MEDICATION_SAVE_API;
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
                            "statusId": 4,
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectQuantity": item.rejectQuantity,
                            "pharmReasonId": item.rejectReasonId,
                            "reasonId": item.reasonId,
                            "note": item.note,
                            "batchMapperId": item.batchMapperId
                        }
                        item.statusId = 4;
                        data.items.push(PatientData);
                    }
                });
                $scope.count = 0;
                itemList.items.forEach(function (d) {
                    if(d.statusId == 4){
                    	$scope.count++;
                    }
                });
                $rootScope.startLoader();
                if($scope.itemListCount == $scope.count){
                	data.statusId = 4;
                }
                else{
                	data.statusId = 2;
                }
                $scope.saveBtnDisabled = false;
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $scope.saveBtnDisabled = true;
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NevigatetoPatientIssueRejection();
                        growl.success('Patient issue Rejection successfully saved');
                    },
                    function (err) {
                        $scope.saveBtnDisabled = true;
                        $rootScope.stopLoader();
                        growl.error('Error while saving patient issue Rejection');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var closepopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('hide');
        }

        var NevigatetoPatientIssueRejection = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () { $state.go('patientIssueRejection'); }, 1000);
        }

        var ApprovedQuantity = function (item) {
            if (item.acceptQuantity > item.issueRejectQuantity) {
                item.rejectQuantity = 0;
                self.model.rejectQty = undefined;
                self.model.rejectQty = undefined;
                item.isRejected = false;
            } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
                item.rejectQuantity = item.issueRejectQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = true;
                RejectQty(item);
            } else if (item.acceptQuantity < item.issueRejectQuantity) {
                item.rejectQuantity = item.issueRejectQuantity - item.acceptQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = true;
                RejectQty(item);
            } else if (item.acceptQuantity == item.issueRejectQuantity) {
                item.rejectQuantity = item.issueRejectQuantity - item.acceptQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = false;
            }
        }

        function valueOnChange(item, isSelectAll) {
            if (isSelectAll)
                self.model.detailsPatientIssueRejectionMedicationList.items.forEach(function (item) {
                    if (self.model.selectAllRows) {
                        item.acceptQuantity = item.issueRejectQuantity;
                        item.rejectQuantity = 0;
                        item.isReject = false;
                    } else
                        item.acceptQuantity = 0;
                });
            else
                if (item.isSelected) {
                    item.acceptQuantity = item.issueRejectQuantity;
                    item.rejectQuantity = 0;
                    item.isReject = false;
                } else
                    item.acceptQuantity = 0;
        }

        var ValidateQuantity = function (item) {
            var isValid = true;

            if (parseInt(item.acceptQuantity) > item.issueRejectQuantity) {
                item.isAcceptQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the rejected issued quantity');
            } else {
                item.acceptQuantity = parseInt(item.acceptQuantity);
                isValid = true;
                // isValid = ValidateMaxOrderQuantity(item);
            }

            return isValid;
        }
        var SaveAcceptReject = function () {
            if (ValidateRejection()) {
              var rejectItemId = itemId;
              self.model.SelectedRequestItems.forEach(function (item) {
                if (item.id == rejectItemId) {
                  if (self.model.rejectQty > item.issueRejectQuantity) {
                    growl.error('Please Enter Reject Quantity Less Than Reject issued quantity');
                    return false;
                  } else if(!self.model.rejectQty || self.model.rejectQty == 0){
                      growl.error('Please Enter Reject Quantity');
                      return false;
                  }
                   else if (self.model.rejectQty == item.issueRejectQuantity) {
                    item.rejectQuantity = item.issueRejectQuantity;
                    item.acceptQuantity = item.issueRejectQuantity - item.rejectQuantity;
                    item.reasonId = self.model.rejectReason.id;
                  } else if (self.model.rejectQty == 0) {
                    item.acceptQuantity = item.issueRejectQuantity;
                    item.rejectQuantity = 0;
                    item.reasonId = self.model.rejectReason.id;
                  } else {
                    item.rejectQuantity = self.model.rejectQty;
                    item.rejectNote = self.model.rejectNote;
                    item.acceptQuantity = item.issueRejectQuantity - item.rejectQuantity;
                    item.reasonId = self.model.rejectReason.id;
                  }
                  item.isRejected = true;
                  var popup = angular.element('#qtyaccepatReject');
                  popup.modal('hide');
                  $scope.calculateReceivedQty(item);
                }
              });
            }
          }

          var ValidateRejection = function () {
            var isValid = true;
            if (!self.model.rejectReason) {
              isValid = false;
              growl.error('Please Select Reason to Reject');
            }
      
            return isValid;
          }
          var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
            self.model.onclose.isRejected = false;
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

           // Method to get the list of Generic
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
        $stateProvider.state("detailsPatientIssueRejectionMedication", {
            url: "/detailsPatientIssueRejectionMedication",
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-issue-rejection-medication.html',
            controller: "DetailsPatientIssueRejectionMedication.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Rejection'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientIssueRejectionMedication.Controller", detailsPatientIssueRejectionMedicationController)
})();
