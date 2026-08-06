(function () {
    "use strict";

    function detailsPatientReturnAcceptanceConsumableController($scope, $rootScope, $http, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, $cookies, CONSTANTS, rejectReasonService, StatusService,pharamcyContractService) {
        var self = this;
        var selectedItemId;
       

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
        $scope.storeName = cookieObjectStore != null ? cookieObjectStore.storeName : ''; // store null -> empty header
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

            self.model.GetDetailsPatientReturnAcceptanceConsumable = GetDetailsPatientReturnAcceptanceConsumable;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SavePatientReturnAcceptance = SavePatientReturnAcceptance;
            self.model.NavigateToPatientReturnAcceptance = NavigateToPatientReturnAcceptance;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.validateQty = validateQty;
            self.model.validate = validate;
            self.model.ApprovedQuantity =ApprovedQuantity;
            self.model.ModalDismissRejectionPopup = ModalDismissRejectionPopup;

            GetDetailsPatientReturnAcceptanceConsumable(commonDetailService.getDataId());
        }
        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = reason;
        });

         var ApprovedQuantity = function (item) {
            if(validateQty(item)){
                if(self.model.detailsPatientReturnAcceptanceConsumableList.status == 'Pending'){    
                    if (item.acceptQuantity > item.returnQuantity) {
                        item.rejectedQuantity = 0;
                        self.model.qty = undefined;
                        item.isRejected = false;
                        } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
                        item.rejectedQuantity = item.returnQuantity;
                        self.model.qty = item.rejectedQuantity;
                        item.isRejected = true;
                        RejectQty(item)
                        } else if (item.acceptQuantity < item.returnQuantity) {
                        item.rejectedQuantity = item.returnQuantity - item.acceptQuantity;
                        self.model.qty = item.rejectedQuantity;
                        item.isRejected = true;
                        RejectQty(item)
                        } else if (item.acceptQuantity == item.returnQuantity) {
                        item.rejectedQuantity = item.returnQuantity - item.acceptQuantity;
                        self.model.qty = item.rejectedQuantity;
                        item.isRejected = false;
                    }
                }
                else{    
                    if (item.acceptReturnQuantity > item.returnQuantity) {
                        item.rejectQuantity = 0;
                        self.model.qty = undefined;
                        item.isRejected = false;
                        } else if (item.acceptReturnQuantity == 0 || !item.acceptReturnQuantity) {
                        item.rejectQuantity = item.returnQuantity;
                        self.model.qty = item.rejectQuantity;
                        item.isRejected = true;
                        RejectQty(item)
                        } else if (item.acceptReturnQuantity < item.returnQuantity) {
                        item.rejectQuantity = item.returnQuantity - item.acceptReturnQuantity;
                        self.model.qty = item.rejectQuantity;
                        item.isRejected = true;
                        RejectQty(item)
                        } else if (item.acceptReturnQuantity == item.returnQuantity) {
                        item.rejectQuantity = item.returnQuantity - item.acceptReturnQuantity;
                        self.model.qty = item.rejectQuantity;
                        item.isRejected = false;
                    }
                }
            }
        }

        var validateQty = function (item){
            var isValid = true;
            if(item.acceptQuantity > item.returnQuantity){
                growl.error("Accepted quantity is greater than return quantity");
                item.acceptQuantity = 0;
                isValid = false;
            }
            return isValid;
        }

        var validate = function (){
            var isValid = true;
            if(self.model.qty > self.model.selectedItem.acceptQuantity){
                growl.error("Reject quantity is greater than accept quantity");
                self.model.qty = 0;
                isValid = false;
            }
            return isValid;
        }

        var GetDetailsPatientReturnAcceptanceConsumable = function (itemId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.CONSUMABLE_DETAIL_API +$scope.unitId + "/" + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.detailsPatientReturnAcceptanceConsumableList = response.data;
                    self.model.status=self.model.detailsPatientReturnAcceptanceConsumableList.status;
                    self.model.detailsPatientReturnAcceptanceConsumableList.items.forEach(function(item){
                        item.uomList.find(function(d){
                            if(d.uomType == item.uom){
                                item['uomDetails'] = d.uomDetails;
                                item['uomTypeId'] = d.uomTypeId;
                            }
                        });
                        if(item.isThreeUOMApplicable == false){
                            if(item.rejectedQuantity == undefined || item.rejectedQuantity == null || item.rejectedQuantity == 0){
                                if(item.uomTypeId ==1){
                                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion);
                                    item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.returnQuantity);
                                    item.receivedQtyCalculation = (item.returnQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId ==1){
                                    item.receivedQtyCon = (item.returnAcceptedQuantity +"*"+ item.uomList[0].conversion);
                                    item.receivedQtyCalculation = (item.returnAcceptedQuantity * item.uomList[0].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.returnAcceptedQuantity);
                                    item.receivedQtyCalculation = (item.returnAcceptedQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                        }
                        else{
                            if(item.rejectedQuantity == undefined || item.rejectedQuantity == null || item.rejectedQuantity == 0){
                                if(item.uomTypeId ==1){
                                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.returnQuantity);
                                    item.receivedQtyCalculation = (item.returnQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId ==1){
                                    item.receivedQtyCon = (item.returnAcceptedQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.returnAcceptedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.returnAcceptedQuantity +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.returnAcceptedQuantity * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.returnAcceptedQuantity);
                                    item.receivedQtyCalculation = (item.returnAcceptedQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                        }
                    
//                        item.isDisabled = true;
//                        item.acceptQuantity = item.acceptQuantity || item.returnQuantity;
                    	item.isDisabled = true;
                        item.acceptQuantity =  item.returnAcceptedQuantity;
                        if(item.returnAcceptedQuantity > 0 || item.rejectedQuantity > 0)
                        	{
                        	item.isDisabled = true;
                        	// $scope.saveBtnFlag=true;
                        	}
                        else
                        	{
                        	item.isDisabled = false;
                        	// $scope.saveBtnFlag=false;
                        	}
                        if(!!item.acceptQuantity && !!item.returnQuantity){
                            ChangeItemStatus(item);
                            } 
                            if(!!item.rejectQuantity){
                                item.isRejected = true;
                                item.rejectReasonId = 1;
                            }
                            
                            if( item.uom=="Dispense")
                			{
                			item.mrp=((item.rate).toFixed(2)*(item.uomList[2].conversion)).toFixed(2);
                			item.actualLendingPrice = ((item.actualLendingPrice).toFixed(2)*(item.uomList[2].conversion)).toFixed(2);
                			}
	                		else if(item.uom=="Stock")
	            			{
		            			item.mrp=((item.rate).toFixed(2)*(item.uomList[1].conversion*item.uomList[2].conversion)).toFixed(2);
		            			item.actualLendingPrice = ((item.actualLendingPrice).toFixed(2)*(item.uomList[1].conversion*item.uomList[2].conversion)).toFixed(2);
	            			}
	                		else if( item.uom=="Purchase")
	                		{
	                			item.mrp=((item.rate).toFixed(2)*(item.uomList[0].conversion*item.uomList[1].conversion*item.uomList[2].conversion)).toFixed(2);
	                			item.actualLendingPrice = ((item.actualLendingPrice).toFixed(2)*(item.uomList[0].conversion*item.uomList[1].conversion*item.uomList[2].conversion)).toFixed(2);
                			}
                    })
                    $rootScope.stopLoader();
                });
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientReturnAcceptanceConsumableList.items.forEach(function (item) {
                    item.acceptQuantity=item.returnQuantity;
                    item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientReturnAcceptanceConsumableList.items.forEach(function (item) {
                    item.acceptQuantity=0;
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
            	item.acceptQuantity=item.returnQuantity;
            	item.isRejected = false;
                itemIds.push(item);
                item.isDisabled = false;
            } else {
            	item.acceptQuantity=0;
                item.rejectedQuantity = 0;
            	 item.isRejected = false;
                var index = itemIds.indexOf(item);
                item.isDisabled = true;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.detailsPatientReturnAcceptanceConsumableList.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function (itemList) {
            var isValid = true;
            var isValidAllItems=true;
            
            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isRejectQuantityValid'] = false;
                    if(item.acceptQuantity==null || item.acceptQuantity==undefined || item.acceptQuantity=="")
                    	{
                    	item.acceptQuantity=0;
                    	}
                    if(item.rejectedQuantity==null || item.rejectedQuantity==undefined || item.rejectedQuantity=="")
                	{
                	item.rejectedQuantity=0;
                	}
                    if ((item.returnQuantity < (item.acceptQuantity))) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity less than return quantity");
                    }
                    if(item.acceptQuantity==0 && item.rejectedQuantity==0)
                    {
                        growl.error('Accepted & Reject quantity both should not be zero');
                        isValid = false;
                        isValidAllItems=false;
                        item.acceptQty = 0;
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
            if(isValidAllItems==false)
            {
            isValid=false;
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
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
                self.model.qty = item.rejectedQuantity;
                self.model.rejectReason = self.model.rejectReasons[0];
                self.model. rejectionFlag = false;

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
              } 
              else {
                item.isRejected = false;
                item.rejectReasonId = 1;
                var popup = angular.element('#cancelStatus');
                popup.modal('show');
              } 
        }

        var SaveRejectPopup = function () {
        	  if(self.model.qty == undefined || self.model.qty == null || self.model.qty == 0){
                  growl.error('Please Enter Rejected Quantity');
                  return false;
              }

            self.model.SelectedRequestItems.forEach(function (item) {
            	if(item.isRejected){
                    if (item.id == selectedItemId) {
                        if(parseInt(self.model.qty) > item.returnQuantity) {
                            growl.error("Please enter quantity less than or equal to Return Quantity");
                            item.isRejected = false;
                            return false;
                        }
                        item.rejectedQuantity = self.model.qty;
                        item.acceptQuantity = item.returnQuantity - self.model.qty;
                        item['rejectReasonId'] = self.model.rejectReason.id;
                        item['note'] = self.model.rejectNote!=undefined ? self.model.rejectNote : "";
                        $scope.calculateReceivedQty(item);
                    }
                }
            });
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
            var popup = angular.element('#qtyaccepatReject');
            popup.modal('hide');
        }

        $scope.calculateReceivedQty = function (item) {
            if(item.acceptQuantity == undefined || item.acceptQuantity == null || item.acceptQuantity == ""){
                item.acceptQuantity = 0;
            }
            var uomTypeId = item.uomDetails.split(" - ")[0];
            if(item.isThreeUOMApplicable == false){
                if(uomTypeId == "P"){
                    item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomList[0].conversion);
                    item.receivedQtyCalculation = (item.acceptQuantity * item.uomList[0].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(uomTypeId == "S"){
                
                }
                else{
                    item.receivedQtyCon = (item.acceptQuantity);
                    item.receivedQtyCalculation = (item.acceptQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
            else{
                if(uomTypeId == "P"){
                    item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.acceptQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(uomTypeId == "S"){
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
        
        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }
        

        var CancelRejection = function(itemList){
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectedQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.returnQuantity;
            $scope.calculateReceivedQty(self.model.selectedItem);
        }

        var ChangeItemStatus = function(item){
            if(!!item.rejectQuantity){
                item.isRejected = true;
                item.rejectReasonId = 1;
            }
            if(item.rejectQuantity==0)
            {
              item.isRejected = false;      
            }
            if(item.returnQuantity > parseInt(item.acceptQuantity)){
                 item.statusId =  SetStatus('Partially Completed');
            } else if(item.returnQuantity == parseInt(item.acceptQuantity)) {
                item.statusId =  SetStatus('Closed');
            }
            if(item.rejectedQuantity!=undefined && item.rejectedQuantity>0)
            {
             item.isRejected = true;       
            }
        } 
        $scope.saveBtnDisabled=false;
        var SavePatientReturnAcceptance = function (itemList) {
            $scope.saveBtnDisabled=true;
            if (ValidateBill(itemList)) {

                var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.CONSUMABLE_SAVE_API;

                var data = {
                    "id": itemList.id,
                    // "statusId": SetStatus('Cancelled'),
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":self.model.detailsPatientReturnAcceptanceConsumableList.storeId,
                    "wardId":self.model.detailsPatientReturnAcceptanceConsumableList.wardId!=undefined ? self.model.detailsPatientReturnAcceptanceConsumableList.wardId :null,
                    "issueId":itemList.issueId
                }
                var checkStatus = false;
                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        var PatientData = {
                            "id": item.id,
                            "statusId":4,
                            "acceptQuantity": Number(item.acceptQuantity),
                            "rejectedQuantity": item.rejectedQuantity,
                            "rejectReason": item.rejectReasonId,
                            "rejectedNote": item.note,
                            "issueDetailBatchMapper":item.issueDetailBatchMapper,
                        }
                         data.items.push(PatientData);
                    }else{
                    	if(item.isDisabled ==false){
                    		checkStatus = true;
                    	}
                    	
                    }
                });
                
                if(checkStatus == true){
                	data.statusId = 18; // Partially Completed
                }else{
                	
                	  data.items.forEach(function(key){
                          var itemStatusList = [];
                          itemStatusList.push(key.statusId);

                          if(itemStatusList.indexOf(8) !== -1){ // Closed status id
                              data.statusId = 8; // Closed stutus
                          } else {
                              data.statusId = 4; //  Completed
                          }
                      });
                }
                
              
                
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NavigateToPatientReturnAcceptance();
                        growl.success('Patient return acceptance Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        
        var NavigateToPatientReturnAcceptance = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('patientReturnAcceptance');
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
      
      var ModalDismissRejectionPopup = function (item){
          item.items.forEach(function (data){
              var prevRequest = self.model.SelectedRequestItems.find(function(prevItem){
                  return prevItem.id == data.id && selectedItemId == prevItem.id;
              });
      
              if (prevRequest) {
                  data.statusId = prevRequest.statusId;
//                  var acceptReturnQuantity = parseInt(prevRequest.acceptReturnQuantity);
//                  if(data.rejectQuantity == undefined){
//                      data.rejectQuantity = 0;
//                  }
//                  data.acceptReturnQuantity =  acceptReturnQuantity +  data.rejectQuantity;
                  data.acceptQuantity = parseInt(data.acceptQuantity) + data.rejectedQuantity;
                  data.rejectedQuantity = 0;
//                  data.rejectQuantity = 0;
                  data.isRejected = false;
                  $scope.calculateReceivedQty(data);
              }
          });
          $('#qtyaccepatReject').modal('hide');
      }
        
        initializeController();
    }
    
    function config($stateProvider) {
        $stateProvider.state("detailsPatientReturnAcceptanceConsumable", {
            url: "/detailsPatientReturnAcceptanceConsumable",
            templateUrl: 'views/PharmacyInventory/InPatient/details-consumable-patient-return-acceptance.html',
            controller: "DetailsPatientReturnAcceptanceConsumable.Controller",
            controllerAs: "vm",
            title: 'Patient Return Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientReturnAcceptanceConsumable.Controller", detailsPatientReturnAcceptanceConsumableController)
})();
