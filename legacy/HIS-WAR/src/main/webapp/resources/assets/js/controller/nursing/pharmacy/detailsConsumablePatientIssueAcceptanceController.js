(function () {
    "use strict";

    function detailsConsumablePatientIssueAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, detailService, commonDetailService,rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;
        var selectedBatchRejectId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isAcceptQuantityValid: [],
                SelectedRequestItems: [],
                rejectReasons: [{
                    "reason": "Select Reason"
                }],
            };                                         

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })


            self.model.GetConsumableIssueAcceptanceDetails = GetConsumableIssueAcceptanceDetails;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveIssueAcceptance = SaveIssueAcceptance;
            self.model.NavigateToBack = NavigateToBack;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;
            self.model.ApprovedQuantity = ApprovedQuantity;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.valueOnChange = valueOnChange;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.SetFocus = SetFocus;
            self.model.BarcodeScan = BarcodeScan;
            self.model.ClosePopup = ClosePopup;
            GetConsumableIssueAcceptanceDetails(detailService.getId());
            GetPatientIndentDetails(commonDetailService.getDataId());

        }

        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = self.model.rejectReasons.concat(reason);
        });

        var ClosePopup = function (item) {
            item.isRejected = false;
            item.rejectReasonId = null;
            self.model.rejectionFlag = false;
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.issueQuantity;
        }



      var GetPatientIndentDetails = function (itemId) {

        var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.DETAIL_API + $scope.unitId + '/' + itemId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            $rootScope.startLoader();
             self.model.ItemDetailList =[];
             self.model.ItemDetailList = response.data.items;
               $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });

      }

       $scope.actualCount=0;
        var GetConsumableIssueAcceptanceDetails = function (itemId) {

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.NEW_CONSUMABLE_ACCEPTANCE_DETAIL_API + "/" + $scope.unitId + "/"+$scope.storeId+"/" + itemId +"/2";

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.consumableIssueAcceptanceDetailslist = response.data;
                    $rootScope.allergydata(response.data);
                      // $scope.actualCount=  self.model.consumableIssueAcceptanceDetailslist.items.length;
                    self.model.consumableIssueAcceptanceDetailslist.items.forEach(function (item) {
                        item.isDisabled = true;
                        
                        item.batch.forEach(function (bat){
                        	if(bat.batchId == item.batchId){
                        		item.expiry = bat.expiry;
                        	}
                        })
                         
                        if(item.acceptedQuantity==undefined || item.acceptedQuantity==0)
                        	{
                        	 $scope.actualCount =  $scope.actualCount+1;
                        	  item.isDisabled = false;
                        	}
                        
                        item.acceptQuantity = item.acceptedQuantity!=undefined ? item.acceptedQuantity :0;
                        if (!!item.acceptQuantity && !!item.issueQuantity) {
                            ChangeItemStatus(item);
                        }
                        item.rejectQuantity= item.rejectedQuantity!=undefined ? item.rejectedQuantity :0;
                        if (!!item.rejectQuantity) {
                            item.isRejected = true;
                            item.isDisabled = true;
                        }
                    });

                });
            $rootScope.stopLoader();
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
                    //AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);
                }

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
        }
        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.consumableIssueAcceptanceDetailslist.items.forEach(function (item) {
                    /*item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);*/
                	 if(item.acceptedQuantity==0 ||item.acceptedQuantity==undefined )
                     {
                      item.isSelected = true;
                      item.isDisabled = false;
                      self.model.SelectedRequestItems.push(item);      
                     }

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.consumableIssueAcceptanceDetailslist.items.forEach(function (item) {
                   // item.isSelected = false;
                  //  item.isDisabled = false;
                	  if(item.acceptedQuantity==0 ||item.acceptedQuantity==undefined )
                      {
                     item.isSelected = false;
                     item.isDisabled = false;      
                      }
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
                item.isDisabled = false;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.consumableIssueAcceptanceDetailslist.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function () {
            var isValid = true;
            $scope.saveButtonDis = true;
            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
                $scope.saveButtonDis = false;
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isAcceptQuantityValid'] = false;
                    // as discussed with swapnil and vishal
                    if ((item.issueQuantity < (item.acceptQuantity))) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity less than issued quantity");
                        $scope.saveButtonDis = false;
                    }
       // as discussed with swapnil and vishal
                   /* if (item.quantity == 0) {
                        item['isAcceptQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than zero");
                    }*/

                });

            }

            return isValid;
        }

        var ClearFields = function (itemList) {
            itemList.Id = "1" + (new Date()).getTime();
            itemList.uhid = null;
            itemList.patientDetail = null;
            itemList.wardName = null;
            itemList.dob = null;
            itemList.pdd = null;
            itemList.doa = null;
            itemList.bedNo = null;
            itemList.doctorDetail = null;
            itemList.issueNo = null;
            itemList.issueDate = null;
            itemList.indentNo = null;
            itemList.store = null;
            itemList.items = [];
        }

        var CancelRejection = function (itemList) {
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.issueQuantity;
        }

        var RejectQty = function (item, idx) {
            self.model.selectedItem = item;
            $scope.idx = idx;
            if (item.isRejected) {
                selectedItemId = item.id;
                selectedBatchRejectId = item.batchId;
                self.model.qty = item.rejectQuantity;
                self.model.rejectReason = self.model.rejectReasons[0];

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');

            }
            else {
                item.isRejected = true;
                var popup = angular.element('#cancelStatus');
                popup.modal('show');
            }
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
            }
            if (item.issueQuantity > parseInt(item.acceptQuantity)) {
//                 item.statusId =  SetStatus('Partially Completed');
            } else if (item.issueQuantity == parseInt(item.acceptQuantity)) {
                item.statusId = SetStatus('Closed');
            }
        }

        var ValidateRejection = function () {
            var isValid = true;
            if (!self.model.rejectReason.id) {
              isValid = false;
              growl.error('Please Select Reason to Reject');
            }
            if(self.model.qty == 0 || !self.model.qty){
                isValid = false;
                growl.error("Please enter reject quantity");
            }
            return isValid;
        }

        var SaveRejectPopup = function () {
            if (ValidateRejection()) {
                self.model.SelectedRequestItems.forEach(function (item, key) {
                    if (item.id == selectedItemId && item.batchId == selectedBatchRejectId && key == $scope.idx) {
                        item.rejectQuantity = self.model.qty;
                        item.acceptQuantity = item.issueQuantity - self.model.qty;
                        item['rejectReasonId'] = self.model.rejectReason.id;
                        item['note'] = self.model.rejectNote;
                    }
                });    
                self.model.rejectReason = undefined;
                self.model.rejectNote = undefined;
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('hide');
            }
        }

        $scope.saveButtonDis = true;
        var SaveIssueAcceptance = function () {
            $scope.saveButtonDis = false;
            if (ValidateBill()) {
                $scope.saveButtonDis = false;
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.UPDATED_CONSUMABLE_SAVE_API;
                var partiallyCompleted = 0 ;
                var completed = 0 ;
                var data = {

                    "id": self.model.consumableIssueAcceptanceDetailslist.id,
                    "fromStoreId":self.model.consumableIssueAcceptanceDetailslist.storeId,
                    "storeId":self.model.consumableIssueAcceptanceDetailslist.issuingStoreId,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }
                $scope.realValue=0;
                angular.forEach(self.model.consumableIssueAcceptanceDetailslist.items, function (item, key) {
                    if (item.isSelected) {
                            $scope.realValue = $scope.realValue+1;
                        var PatientData = {
                            "id": item.id,
                            "statusId": 8,
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectQuantity": item.rejectQuantity,
                            "reasonId": item.rejectReasonId ? item.rejectReasonId : 1,
                            "note": item.note,
                            "batchMapperId": item.batchMapperId
                        }
                        /*if(item.acceptQuantity == item.issueQuantity)
                        {
                            PatientData.statusId = SetStatus("Completed");
                            completed++;
                        }
                        else
                            if (item.acceptQuantity < item.issueQuantity)
                            {
                                PatientData.statusId = SetStatus("Partially Completed") ;
                                partiallyCompleted++;
                            }
                            console.log("Status : ",PatientData.statusId);
                        */
                        data.items.push(PatientData);
                    }
                    /*console.log("Completed : ",completed);
                    console.log("Partially Completed : ",partiallyCompleted);
                   */
                });
                $scope.acceptAddition = 0;
                $scope.indentqty = 0;
                $scope.dummyList = [];
               
                var cleaned = [];
                self.model.consumableIssueAcceptanceDetailslist.items.forEach(function(itm) {
                    var unique = true;
                    cleaned.forEach(function(itm2) {
                        if (_.isEqual(itm.itemId, itm2.itemId)) unique = false;
                    });
                    if (unique)  cleaned.push(itm);
                });
                $scope.dummyList = cleaned;
                angular.forEach($scope.dummyList, function (item, key) {
                    angular.forEach(self.model.consumableIssueAcceptanceDetailslist.items, function (i, key) {
                        if(!i.rejectQuantity){
                            i.rejectQuantity = 0;
                        }
                        if(item.itemId == i.itemId){
                            var amt = parseInt(i.acceptQuantity) + parseInt(i.rejectQuantity);
                            $scope.acceptAddition += amt;
                        }
                    });
                });
                //  console.log(data);

                if($scope.actualCount ==  $scope.realValue)
                {
                    data.statusId = 8;
                }
                else
                {
                    data.statusId = 18;     
                }

                angular.forEach($scope.dummyList, function (d, key) {
                    $scope.indentqty += d.indentQuantity;
                });
                if($scope.indentqty > $scope.acceptAddition){
                    data.statusId = 18;
                }
                else{
                    data.statusId = 8;
                }
                // check indent Quantity
                $scope.totalPendingIndentQuantity =0;

                 angular.forEach(self.model.ItemDetailList, function (d, key) {
                          $scope.totalPendingIndentQuantity += d.quantity;
                  });

                if($scope.totalPendingIndentQuantity > $scope.acceptAddition){
                    data.statusId = 18;
                }
                else{
                    data.statusId = 8;
                }
                $scope.saveButtonDis = false;
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        // ClearFields(itemList);    
                        growl.success('Patient issue acceptance successfully saved');
                        var popup = angular.element('#confirmSave');
                        popup.modal('hide');
                        $('.modal-backdrop').remove();
                        $(document.body).removeClass("modal-open");
                        $rootScope.stopLoader();
                        $scope.saveButtonDis = true;
                        NavigateToBack();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        $scope.saveButtonDis = true;
                        growl.error('Error while saving patient issue acceptance');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var NavigateToBack = function () {
            $state.go('patientIssueAcceptance');
        }
        
        var ApprovedQuantity = function (item, idx) {
            if (item.acceptQuantity > item.issueQuantity) {
                item.rejectQuantity = 0;
                self.model.rejectQty = undefined;
                item.isRejected = false;
            } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
                item.rejectQuantity = item.issueQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = true;
                RejectQty(item, idx)
            } else if (item.acceptQuantity < item.issueQuantity) {
                item.rejectQuantity = item.issueQuantity - item.acceptQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = true;
                RejectQty(item, idx)
            } else if (item.acceptQuantity == item.issueQuantity) {
                item.rejectQuantity = item.issueQuantity - item.acceptQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isRejected = false;
            }
        }

        var ValidateQuantity = function (item) {
            var isValid = true;

            if (parseInt(item.acceptQuantity) > item.issueQuantity) {
                item.isAcceptQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the quantity issued');
            } else {
                item.acceptQuantity = parseInt(item.acceptQuantity);
                isValid = true;
                // isValid = ValidateMaxOrderQuantity(item);
            }

            return isValid;
        }

        $scope.ValidateQty = function () {
            var isValid = true;
//            self.model.consumableIssueAcceptanceDetailslist.items.forEach(function (item) {
                if(self.model.qty > self.model.consumableIssueAcceptanceDetailslist.items[$scope.idx].acceptQuantity){
                    growl.error('Entered quantity is exceeding the accept quantity');
                    isValid = false;
                    self.model.qty = 0;
                }
                if(self.model.qty > self.model.qty > self.model.consumableIssueAcceptanceDetailslist.items[$scope.idx].issueQuantity){
                    growl.error('Entered quantity is exceeding the issue quantity');
                    isValid = false;
                    self.model.qty = 0;
                }
//            });
            return isValid;
        }

        function valueOnChange(item, isSelectAll) {
            if (isSelectAll)
                self.model.consumableIssueAcceptanceDetailslist.items.forEach(function (item) {
                    if (self.model.selectAllRows) {
                     /*   item.acceptQuantity = item.issueQuantity;
                        item.rejectQuantity = 0;
                        item.isRejected = false;*/
                        if(item.acceptedQuantity == 0)
                        {
                    item.acceptQuantity = item.issueQuantity;
                    item.rejectQuantity = 0;
                    item.isRejected = false;       
                        }
                    } else
                        //item.acceptQuantity = 0;
                    	if(item.acceptedQuantity == 0)
                        {
                           item.acceptQuantity = 0;      
                        }
                });
            else
                if (item.isSelected) {
                    item.acceptQuantity = item.issueQuantity;
                    item.rejectQuantity = 0;
                    item.isRejected = false;
                } else
                {
                    item.acceptQuantity = 0;
                    item.rejectQuantity = 0;
                    item.isRejected = false;
                }

        }

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

        var GetGenericList = function () {
            var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API + $scope.organizationId;
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
        $stateProvider.state("detailsConsumablePatientIssueAcceptance", {
            url: "/detailsConsumablePatientIssueAcceptance",
            templateUrl: 'views/PharmacyInventory/InPatient/details-consumable-patient-issue-acceptance.html',
            controller: "DetailsConsumablePatientIssueAcceptance.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsConsumablePatientIssueAcceptance.Controller", detailsConsumablePatientIssueAcceptanceController)
})();
