(function () {
    "use strict";

    function addNewMaterialInwardController($scope, $rootScope, $http, $cookies, CONSTANTS, $state, $filter, CurrencyService, commonDetailService, GenericService, growl,stopTransactionService) {
        var self = this;

        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                stores: [{
                    "store": "Select Store"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                replacementTypes: [
                    {
                    	 "id": 9,
                         "type": "Credit Note"
                    },{
                        "id": 8,
                        "type": "Replacement"
                    },],
                dateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false
                },
                documentDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                expiryDateOptions: {
                    minDate: new Date(),
                    minMode: "month",
                    maxMode: "year",
                    datepickerMode: "month"
                },
                fromDate: new Date(),
                toDate: new Date(),
                documentDate: new Date(),
                dateOpen: [],
                SelectedItemsIds: [],
                selectAllRows: false,
                SelectedItemIds: [],
                NewItemList: [],
                itemList: [],
                assetTypeId: 1,
                searchNewItemList: [],
                // mrpUOM: [
                //     {
                //         "mrpUomTypeId": 1,
                //         "uomType": "Purchase"
                //     },
                //     {
                //         "mrpUomTypeId": 2,
                //         "uomType": "Stock"
                //     },
                //     {
                //         "mrpUomTypeId": 3,
                //         "uomType": "Dispense"
                //     },
                // ],
                isCreditNoteValid: false,
                isCreditTaxValid: false,
                isCreditNetValid: false
            };

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.OpenDocumentDate = OpenDocumentDate;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.nameNotFound = nameNotFound;
            self.model.AddVendorName = AddVendorName;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.vendorNames = [];
            self.model.RetuurnableGatepassSearch = RetuurnableGatepassSearch;
            self.model.GetItemList = GetItemList;
            self.model.Expand = Expand;
            self.model.SelectItem = SelectItem;
            self.model.AddItems = AddItems;
            self.model.SelectAllIndentItems = SelectAllIndentItems;
            self.model.uomSelectEvent = uomSelectEvent;
            self.model.uomInfoOnClick = uomInfoOnClick;
            self.model.modalUOMSelectEvent = modalUOMSelectEvent;
            self.model.uomModalOnClick = uomModalOnClick;
            self.model.OpenExpiryDate = OpenExpiryDate;
            self.model.setSelectedDate = setSelectedDate;
            self.model.fillItemList = fillItemList;
            self.model.SaveNewMaterialInward = SaveNewMaterialInward;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.removeRowData = removeRowData;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.openItemModal = openItemModal;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectItemPopup = SelectItemPopup;
            self.model.SelectAllItemsPopup = SelectAllItemsPopup;
            self.model.SelectAllItems = SelectAllItems;
            self.model.setReplacement = setReplacement;
            self.model.calculateStock = calculateStock;
            self.model.clearFields = clearFields;


            self.model.ValidateMrpUom = ValidateMrpUom;
            self.model.ValidateCOP = ValidateCOP;
            self.model.ValidateMRP = ValidateMRP;
            self.model.CalculateCOP = CalculateCOP;
            self.model.ValidateBatch = ValidateBatch;
            self.model.ValidateSpecification = ValidateSpecification;
            self.model.ValidateExpiry = ValidateExpiry;
            self.model.ValidateUom = ValidateUom;
            self.model.ValidateTax = ValidateTax;
            self.model.ValidateQuantity = ValidateQuantity;

            self.model.AddItemsPopup = AddItemsPopup;
            self.model.ValidateCredit = ValidateCredit;
            self.model.SelectAllItemsData = SelectAllItemsData;

            PopulateDropdown();
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenDocumentDate = function () {
            self.model.documentDateOpened = true;
        }

        var PopulateDropdown = function () {
            var inward = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
          // inward.store = inward.stores[0];


            inward.stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    inward.stores = inward.stores.concat(response.data);
                    inward.replacementTypes = inward.replacementTypes;

                    var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                    var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
                    for(var i=0;i<inward.stores.length;i++)
                    {
                        if(inward.stores[i].id==selectedStoreId)
                        {
                            inward.store = inward.stores[i];
                        }
                    }
                    // $rootScope.stopLoader();
                    var inwardId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    console.log("Inward Id : ",inwardId);
                    if (!!inwardId) {
                        commonDetailService.setDataId(null);
                        GetInwardDetails(inwardId);
                    } else {
                        $rootScope.stopLoader();
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetInwardDetails = function (inwardId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            // var opbId = openingBalanceService.getOpeningBalanceId();

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + inwardId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.inwardId = response.data.id;
                    self.model.searchData = response.data;
                    self.model.approvalStatusId = response.data.statusId;
                    self.model.assetTypeId=response.data.assetTypeId;
                    self.model.store = self.model.stores.find(function (item) {
                        return response.data.storeId == item.id;
                    });
                    self.model.replacementType = self.model.replacementTypes.find(function (item) {
                        return response.data.replacementTypeId == item.id;
                    });
                    // self.model.searchData.returnableGatePassNo =
					// response.data.returnableGatePassNo;
                    self.model.vendorName = response.data.vendor;
                    self.model.returnableGatePassNo = response.data.returnableGatePassNo;
                    self.model.documentNo = response.data.documentNo;
                    self.model.remark = response.data.remark;
                    self.model.documentDate = new Date(response.data.documentDate);
                    self.model.totalAmount = response.data.totalAmount;
                    self.model.totalNetAmount = response.data.totalNetAmount;
                    self.model.totalTaxAmount = response.data.totalTaxAmount;
                    self.model.itemList = response.data.items;
                    if (response.data.replacementTypeId == 8) {
                        angular.forEach( self.model.itemList, function (item, key) {
                            item.returnQuantity = item.returnQuantity;
                            item.expiryDate = new Date(item.expiryDate);
                            item.cop = item.cop;
                            item.mrp = item.mrp;
                            item.markup = parseFloat(item.markup ? item.markup : 0);
                            item.returnAmount = item.returnAmount;
                            item.taxPercentage = item.taxPercentage;
                            item.mrpUOM = item.uom;
                            item.selectedMrpUom = item.mrpUOM.find(function (mrp) {
                                return item.mrpUomTypeId == mrp.uomTypeId;
                            });
                            if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
                    		{
                            item.selectedUom = item.uom.find(function (u) {
                                item.uomUnitreturnable = u.uomUnit;
                                return item.uomTypeId  == u.uomTypeId;
                            });

                            item.uom.find(function(u){
                                if(u.uomType == item.uomType){
                                   item.uomType = u.uomDetails;
                                }
                            })
                    		}
                            
                            item.selectedTax = item.taxList.find(function (t) {
                               item.taxName = t.taxName;
                               item.taxPercent = t.taxPercent;
                               return item.taxId  == t.taxId;
                            });

                            if(item.isThreeUOMApplicable == false){
                                item.uomListData = item.uom;
                                if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
                        		{
                                    item.uom = [];
                                    item.mrpUOM = [];
                                    angular.forEach(item.uomListData, function(data){
                                        if(data.uomType != 'Stock'){
                                            item.uom.push(data);
                                            item.mrpUOM.push(data);
                                        }
                                    });
                                    if(item.uomTypeId == 1){
                                        item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uomListData[0].conversion);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uomListData[0].conversion);
                                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uomListData[0].conversion);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uomListData[0].conversion);
                                        item.uomUnitData = item.uomListData[2].uomUnit;
                                    }
                                    else if(item.uomTypeId == 2){
                                        
                                    }
                                    else{
                                        item.receivedQtyConForReturn = (item.returnQuantity);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity);
                                        item.receivedQtyConForReceived = (item.receivedQuantity);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                                        item.uomUnitData = item.uomListData[2].uomUnit;
                                    }
                                }
                                else{
                                    if(item.uomTypeId == 1){
                                        item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else if(item.uomTypeId == 2){
                                        item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[1].conversion);
                                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[1].conversion);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else{
                                        item.receivedQtyConForReturn = (item.returnQuantity);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity);
                                        item.receivedQtyConForReceived = (item.receivedQuantity);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                    item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[1].conversion);
                                    item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyConForReturn = (item.returnQuantity);
                                    item.receivedQtyCalculationForReturn = (item.returnQuantity);
                                    item.receivedQtyConForReceived = (item.receivedQuantity);
                                    item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }

                            if (item.netAmount == 0)
                                item.isDisable = true;
                        });
                        /*
						 * if (response.data.items)
						 * self.model.fillItemList(response.data.items);
						 */
                    }
                    else {
                        self.model.searchData = [];
                        // self.model.searchData.totalAmount =
						// self.model.searchData.totalAmount.toFixed(2),
                        // self.model.searchData.totalTaxAmount =
						// self.model.searchData.totalTaxAmount.toFixed(2),
                        // self.model.searchData.netAmount =
						// self.model.searchData.netAmount.toFixed(2),
                        angular.forEach(response.data.items, function (item, key) {
                            response.data.returnableGatePassDate = new Date(item.returnableGatepassDate);
                            self.model.creditNoteId = item.id;
                            self.model.creditNetAmount = item.creditNetAmount;
                            self.model.creditNoteAmount = item.creditNoteAmount;
                            self.model.creditTaxAmount = item.creditTaxAmount;
                            response.data.totalAmount = item.totalAmount;
                            response.data.totalTaxAmount = item.taxAmount;
                            response.data.totalNetAmount = item.netAmount;
                            var obj = {
                                "returnableGatePassNo": '',
                                "returnableGatePassDate": '',
                                "vendor": '',
                                "totalAmount": '',
                                "totalTaxAmount": '',
                                "totalNetAmount": '',
                                "creditNetAmount" :'',
                                "creditNoteAmount" :'',
                                "creditTaxAmount" :''
                            }
                            obj.returnableGatePassNo = item.returnableGatePassNo;
                            obj.returnableGatePassDate = item.returnableGatepassDate;
                            obj.vendor = response.data.vendor;
                            obj.totalAmount = item.totalAmount;
                            obj.totalTaxAmount = item.taxAmount;
                            obj.totalNetAmount = item.netAmount;
                            obj.creditNetAmount = item.creditNetAmount;
                            obj.creditNoteAmount = item.creditNoteAmount;
                            obj.creditTaxAmount = item.creditTaxAmount;
                            obj.id = item.id;
                            self.model.searchData.push(obj);
                        });
                    }
                    $rootScope.stopLoader();

                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function SearchVendorNames(search) {
            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.vendorNames = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.vendorNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return nameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return nameNotFound(search);
                    });
            } else {
                if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
                    return nameNotFound(search);
                } else {
                    return $filter('filter')(self.model.vendorNames, {
                        $: search
                    });
                }
            }
        }

        function nameNotFound(search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
        }

        function AddVendorName(vendor) {
            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }


        function GetItemList() {
        	$scope.saveBtnDisabled=false;
            if (!self.model.vendorName) {
                growl.error("Please Select Vendor");
            }
            else if (!self.model.replacementType) {
                growl.error("Please Select Replacement Type ");
            }
            else {
                self.model.action = 'Open';
                var popup = angular.element('#returnableGatePassModal');
                popup.modal('show');
                // GetAssetCategoryList();
                RetuurnableGatepassSearch();
                self.model.NewItemList = [];
            }

        }

        var RetuurnableGatepassSearch = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.RETURNABLE_GATEPASS_SEARCH;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "vendorId": self.model.vendorId,
                "organizationId":$scope.organizationId,
                "unitId":$scope.unitId,
                "assetTypeId":self.model.assetTypeId
            };
            $rootScope.startLoaderPopup()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.returnableSearchData = response.data;
                    angular.forEach(self.model.returnableSearchData, function(value){
                        angular.forEach(value.items, function(item){
                            item.uom.find(function(d){
                                if(d.uomTypeId == item.uomTypeId){
                                    item.uomType = d.uomDetails;
                                }
                            });
                        });
                    })
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
            console.log("returnableSearchData : ",self.model.returnableSearchData);
        }

        var Expand = function (item) {
            item.Expanded = !item.Expanded;
        }

        function SelectItem(Item) {
            var itemIds = self.model.SelectedItemIds;
            if (Item.isSelected) {
                itemIds.push(Item.id);
            } else {
                var index = itemIds.indexOf(Item.id);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }
            self.model.selectedList = itemIds;

            var allselected = self.model.isSelectedIndent.items.findIndex(x => x.isSelected != true)
            if (allselected < 0) { // As is returns -1 if al rows selected
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var SelectedItems = function () {
            self.model.materialDetails = [];
            if(self.model.isSelectedIndent == undefined){
              angular.forEach(self.model.returnableSearchData, function (material) {
                var materialData = null;
                if (material.isSelectedIndent) {
                  materialData = {
                    "id": material.id,
                    "itemsId": []
                  }
                }
      
                angular.forEach(material.items, function (item) {
                  if (item.isSelected) {
                    materialData.itemsId.push(item.itemId);
                  }
                });
      
                if (material.isSelectedIndent && materialData.itemsId.length > 0)
                  self.model.materialDetails.push(materialData);
              });
            }
            else if(!self.model.isSelectedIndent.id){
              angular.forEach(self.model.returnableSearchData, function (material) {
                var materialData = null;
                if (material.isSelectedIndent) {
                  materialData = {
                    "id": material.id,
                    "itemsId": []
                  }
                }
      
                angular.forEach(material.items, function (item) {
                  if (item.isSelected) {
                    materialData.itemsId.push(item.itemId);
                  }
                });
      
                if (material.isSelectedIndent && materialData.itemsId.length > 0)
                  self.model.materialDetails.push(materialData);
              });
            }
            else{
                self.model.materialDetails = [];
                var material = [];
                angular.forEach(self.model.returnableSearchData, function(dataList){
                    var materialData = null;
                    if(dataList.isSelectedIndent == true){
                        materialData = {
                            "id": dataList.id,
                            "itemsId": []
                        }
                    }
                    angular.forEach(dataList.items, function(itemList){
                        if(itemList.isSelected){
                            materialData.itemsId.push(itemList.itemId);
                        }
                    });
                    if (dataList.isSelectedIndent && materialData.itemsId.length > 0)
                        self.model.materialDetails.push(materialData);
                });
            }
        }
        $scope.saveBtnDisabled=false;
        var AddItems = function () {
            $scope.saveBtnDisabled=true;
        	 ActualAddItems();
             angular.element('#returnableGatePassModal').modal('hide');
        	/*
        	self.model.selectedStopIds = [];
        	if(self.model.isSelectedIndent == undefined){
        	 angular.forEach(self.model.returnableSearchData, function (material) {
        		 angular.forEach(material.items, function (item) {
        			  self.model.selectedStopIds.push(item.itemId);
        		    });        			
        	  });
        	}
        	 var stopData = {
 	                "itemIds": self.model.selectedStopIds,
 	                "storeId": self.model.store.id 	      		 
 	              };
  	    
  	 	  stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
           var StopTransactionData = responseData;  
           if(StopTransactionData.length > 0 ){    
                    var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                    var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                    growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                   
                    $rootScope.stopLoader();
                    self.model.selectedStopIds = [];
                    self.model.SelectedItems = [];
                    return false;
            }
           else{
        	   ActualAddItems();
           }
  	  });
        */}

        var ActualAddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            SelectedItems();
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.ADD_MULTIPLE_ITEM_API + '/'+$scope.unitId + '/'+$scope.organizationId;

            // var data = {
            // "id":
            // "itemsId": self.model.selectedList ? self.model.selectedList : []
            // };
            $rootScope.startLoaderPopup();
            $scope.arrayList=[]
            angular.forEach(self.model.materialDetails, function(item,key){
            	angular.forEach(item.itemsId, function(item1,key1){
            		var obj={
            				"id":item.id,
            				"itemId":item1
            		}
            		$scope.arrayList.push(obj);
            	})
            });
            GenericService.serviceAction("POST", URI, self.model.materialDetails).then(
                function (response) {
                    self.model.searchData = response.data;
                    self.model.itemList = [];

                    angular.forEach( self.model.searchData, function (dataList, key) {
                    	dataList.totalAmount = dataList.totalAmount.toFixed(2);
                    	dataList.totalTaxAmount = dataList.totalTaxAmount.toFixed(2);
                    	dataList.totalNetAmount = dataList.totalNetAmount.toFixed(2);
                        angular.forEach(dataList.items, function(item,key){
                            item.returnQuantity = item.returnQuantity;
                            item.expiryDate = new Date(item.expiryDate);
                            item.cop = item.cop;
                            item.mrp = item.mrp;
                            item.markup = parseFloat(item.markup ? item.markup : '');
                            item.returnAmount = item.returnAmount;
                            item.taxPercentage = item.taxPercentage;
                            item.mrpUOM = item.uom;
                            item.selectedMrpUom = item.mrpUOM.find(function (mrp) {
                                return item.uomTypeId == mrp.uomTypeId;
                            });
                            if(item.isThreeUOMApplicable == false){
                            	if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
                            	{
                                    item.uomListData = item.uom;
                                    item.uom = [];
                                    item.mrpUOM = [];
                                    item.uomListData.find(function(d){
                                        if(d.uomTypeId != 2){
                                            item.uom.push(d);
                                            item.mrpUOM.push(d);
                                        }
                                    })
                                    item.uomListData.find(function(d){
                                        if(d.uomTypeId == item.uomTypeId){
                                            item.uomType = d.uomDetails;
                                        }
                                    });
                                    item.selectedUom = item.uomListData.find(function (u) {
                                        item.uomUnitreturnable = u.uomUnit;
                                    // item.selectedMrpUom = u.uomTypeId;
                                        return item.uomTypeId  == u.uomTypeId;
                                    });
                            	}
                            }
                            else{
                            	if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
                            	{
                                    item.uom.find(function(d){
                                        if(d.uomTypeId == item.uomTypeId){
                                            item.uomType = d.uomDetails;
                                        }
                                    });
                                    item.selectedUom = item.uom.find(function (u) {
                                        item.uomUnitreturnable = u.uomUnit;
                                    // item.selectedMrpUom = u.uomTypeId;
                                        return item.uomTypeId  == u.uomTypeId;
                                    });
                            	}
                            }
                            item.selectedTax = item.taxList.find(function (t) {
                                item.taxName = t.taxName;
                                item.taxPercent = t.taxPercent;
                                return item.taxId  == t.taxId;
                            });
                            if(item.receivedQuantity == undefined || item.receivedQuantity == null || item.receivedQuantity == ""){
                                item.receivedQuantity = 0;
                            }
                            if(item.isThreeUOMApplicable == false){
                            	if(item.uomListData!=undefined && item.uomListData!=null && item.uomListData.length!=0)
                            	{
                                    if(item.uomTypeId ==1){
                                        item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uomListData[0].conversion);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uomListData[0].conversion);
                                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uomListData[0].conversion);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uomListData[0].conversion);
                                        item.uomUnitData = item.uomListData[2].uomUnit;
                                    }
                                    else if(item.uomTypeId == 2){
                                        
                                    }
                                    else{
                                        item.receivedQtyConForReturn = (item.returnQuantity);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity);
                                        item.receivedQtyConForReceived = (item.receivedQuantity);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                                        item.uomUnitData = item.uomListData[2].uomUnit;
                                    }
                            	}
                            }
                            else{
                            	if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
                            	{
                                    if(item.uomTypeId ==1){
                                        item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else if(item.uomTypeId == 2){
                                        item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[1].conversion);
                                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[1].conversion);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[1].conversion);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else{
                                        item.receivedQtyConForReturn = (item.returnQuantity);
                                        item.receivedQtyCalculationForReturn = (item.returnQuantity);
                                        item.receivedQtyConForReceived = (item.receivedQuantity);
                                        item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                            	}
                            }
                            angular.forEach($scope.arrayList, function(itemList,key){
                            	if(dataList.id==itemList.id && item.itemId==itemList.itemId)
                            		{
                            		self.model.itemList.push(item);
                            		}
                            });
                            console.log("repalcement", self.model.itemList);
                        });
                        
                    });

                    self.model.SelectedItemsIds = [];
                    $rootScope.stopLoaderPopup();
                },
                function (err) {
                    $rootScope.stopLoaderPopup();
                    growl.error(err.data.message);
                });
            self.model.returnableSearchData = [];
        }

        $scope.conversionCalculate = function (item, value) {
            if(value.returnQuantity == undefined || value.returnQuantity == null || value.returnQuantity == ""){
                value.returnQuantity = 0;
            }
            if(value.receivedQuantity == undefined || value.receivedQuantity == null || value.receivedQuantity == ""){
                value.receivedQuantity = 0;
            }
            if(value.isThreeUOMApplicable == false){
                if(item.uomTypeId ==1){
                    // value.receivedQtyConForReturn = (value.returnQuantity +"*"+ value.uomListData[0].conversion);
                    // value.receivedQtyCalculationForReturn = (value.returnQuantity * value.uomListData[0].conversion);
                    value.receivedQtyConForReceived = (value.receivedQuantity +"*"+ value.uomListData[0].conversion);
                    value.receivedQtyCalculationForReceived = (value.receivedQuantity * value.uomListData[0].conversion);
                    value.uomUnitData = value.uomListData[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                    // value.receivedQtyConForReturn = (value.returnQuantity);
                    // value.receivedQtyCalculationForReturn = (value.returnQuantity);
                    value.receivedQtyConForReceived = (value.receivedQuantity);
                    value.receivedQtyCalculationForReceived = (value.receivedQuantity);
                    value.uomUnitData = value.uomListData[2].uomUnit;
                }
            }
            else{
                if(item.uomTypeId ==1){
                    // value.receivedQtyConForReturn = (value.returnQuantity +"*"+ value.uom[0].conversion +"*"+ value.uom[1].conversion);
                    // value.receivedQtyCalculationForReturn = (value.returnQuantity * value.uom[0].conversion * value.uom[1].conversion);
                    value.receivedQtyConForReceived = (value.receivedQuantity +"*"+ value.uom[0].conversion +"*"+ value.uom[1].conversion);
                    value.receivedQtyCalculationForReceived = (value.receivedQuantity * value.uom[0].conversion * value.uom[1].conversion);
                    value.uomUnitData = value.uom[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    // value.receivedQtyConForReturn = (value.returnQuantity +"*"+ value.uom[1].conversion);
                    // value.receivedQtyCalculationForReturn = (value.returnQuantity * value.uom[1].conversion);
                    value.receivedQtyConForReceived = (value.receivedQuantity +"*"+ value.uom[1].conversion);
                    value.receivedQtyCalculationForReceived = (value.receivedQuantity * value.uom[1].conversion);
                    value.uomUnitData = value.uom[2].uomUnit;
                }
                else{
                    // value.receivedQtyConForReturn = (value.returnQuantity);
                    // value.receivedQtyCalculationForReturn = (value.returnQuantity);
                    value.receivedQtyConForReceived = (value.receivedQuantity);
                    value.receivedQtyCalculationForReceived = (value.receivedQuantity);
                    value.uomUnitData = value.uom[2].uomUnit;
                }
            }
        }

        var calculateStock = function (item) {
            var conversionPurchase;
            var conversionStock;
            var conversionDespense
            item.rcvdQty = item.receivedQuantity
            item.uom.forEach(function (uom) {
            	if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
            		{
                if (uom.uomTypeId == 1) {
                    conversionPurchase = uom.conversion;
                }
                else if (uom.uomTypeId == 2) {
                    conversionStock = uom.conversion
                }
                else {
                    conversionDespense = uom.conversion
                }
            		}
            });
            if(item.selectedUom!=undefined && item.selectedUom!=null && item.selectedUom.length!=0)
    		{
            if (item.selectedUom.uomTypeId == 1) {
                item.rcvdQty = Math.round((item.rcvdQty * conversionPurchase * conversionStock * conversionDespense));
            }
            else if (item.selectedUom.uomTypeId == 2) {
                item.rcvdQty = Math.round((item.rcvdQty * conversionStock * conversionDespense));
            }
            else {
                item.rcvdQty = Math.round((item.rcvdQty * conversionDespense));
            }
    		}
        }
        var SelectAllIndentItems = function (item) {
            self.model.returnableSearchData.forEach(function (x) {
                x.Expanded = false;
            });
            item.Expanded = !item.Expanded;
            var selectedItem = self.model.isSelectedIndent;
            self.model.isSelectedIndent = item;
            self.model.SelectedItemsIds = [];
        }

        var SelectAllItemsData = function () {
            if (self.model.selectAllRowsItems) 
            {
                angular.forEach(self.model.returnableSearchData, function(item){
                    item.isSelectedIndent = true;
                    item.Expanded = true;
                    angular.forEach(item.items, function(data){
                        data.isSelected = true;
                        self.model.selectAllRows = true;
                    });
                });
            } 
            else 
            {
                angular.forEach(self.model.returnableSearchData, function(item){
                    item.isSelectedIndent = false;
                    item.Expanded = false;
                    angular.forEach(item.items, function(data){
                        data.isSelected = false;
                        self.model.selectAllRows = false;
                    });
                });
            }
        }

        var uomSelectEvent = function (datum) {
            datum.uom.forEach(function (uom) {
                if (uom.uomTypeId == datum.uomTypeId)
                    datum.selectedUom = uom;
            });
        }

        var modalUOMSelectEvent = function (datum) {
            self.model.selectedUOMUnit.forEach(function (uom) {
                if (uom.uomTypeId == datum.uomTypeId)
                    datum.selectedUOM = uom;
            });
        }

        var uomInfoOnClick = function (uomList, index, data) {
            self.model.selectedUOMList = [];
            self.model.selectedUOMUnit = [];
            self.model.selectedItemIndex = index;
            if (uomList)
                uomList.forEach(function (item, k) {
                    if(data.isThreeUOMApplicable == false){
                        if(item.uomTypeId != 2){
                            self.model.selectedUOMList.push(item);
                        }
                    }
                    else{
                        self.model.selectedUOMList.push(item);
                    }
                    
                    self.model.selectedUOMUnit.push({ 'uomType': item.uomUnit, 'uomTypeId': item.uomTypeId });

                    if (item.selectedUOM && item.selectedUOM.uomTypeId == item.uomTypeId)
                        item['selectedUOM'] = self.model.selectedUOMUnit[k];
                });
        }

        var uomModalOnClick = function () {
            self.model.itemList[self.model.selectedItemIndex].defaultUOM = [];
            if (self.model.selectedUOMList) {
                self.model.selectedUOMList.forEach(function (item) {
                    self.model.itemList[self.model.selectedItemIndex].defaultUOM.push({
                        uomTypeId: item.uomTypeId,
                        uomUnitId: item.uomUnitId,
                        conversion: item.conversion,
                        ipUom: item.ipUom,
                        storeUom: item.storeUom,
                        opUom: item.opUom
                    });
                });
            }
            $('#selectUOM_modal').modal('hide');
        }

        var OpenExpiryDate = function (idx) {
            self.model.dateOpen[idx] = true;
        }

        var setSelectedDate = function (item) {
            var expDateYear = item.expiryDate.getFullYear();
            var expDateMonth = item.expiryDate.getMonth() + 1;
            var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
            item.expiry = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
        }

        var fillItemList = function (list) {
            list.forEach(function (item) {
                var defaultUOM = [];
                for (let uom of item.uom) {
                    defaultUOM.push({
                        uomTypeId: uom.uomTypeId,
                        uomUnitId: uom.uomUnitId,
                        conversion: uom.conversion,
                        ipUom: uom.ipUom,
                        opUom: uom.opUom,
                        storeUom: uom.storeUom
                    });
                }

                item['defaultUOM'] = defaultUOM;
                self.model.itemList.push(item);
            });
        }

        var NavigteToBack = function () {
            $state.go('materialInwards');
        }

        
        function validateSave() {
            var isValid = true;
            // var ItemDetailList = self.model.ItemDetailList;
            
            
             if (self.model.replacementType.id == 9) {
            	 $scope.count = 0;
            	angular.forEach(self.model.searchData, function(data){
            		 if (self.model.replacementType.id == 9 && data.creditNetAmount == undefined && data.creditNoteAmount == undefined  && data.creditTaxAmount == undefined) {
                         growl.error("Please validate all required fields");
                         isValid = false;
                         $scope.count++;
                     }
            		 if(!data.creditNoteAmount || parseInt(data.creditNoteAmount)<=0)
         			{
                 		growl.error("Please validate all required fields");
                 		 isValid = false;
                          $scope.count++;
         			}
            	});
            	if($scope.count == 0){
            		isValid = true;
            	}
            	else{
            		isValid = false;
            		
            		
            	}
            }
            else if (self.model.documentNo == undefined) {
                growl.error("Please Enter Document Number");
                isValid = false;
            }
            else if (self.model.replacementType.id == 8) {
                for (let ItemDetail of self.model.itemList) {
                    if (!ItemDetail.isDisable)
                    {
                        if (!ItemDetail.receivedQuantity) {
                            growl.error("Please Enter Received Quantity");
                            isValid = false;
                        }
                        if(ItemDetail.selectedTax == undefined || ItemDetail.selectedTax == null){
                            growl.error("Please Select Tax");
                            isValid = false;
                        }
                        if(self.model.assetTypeId!=2)
                        	{
                        if (!ValidateUom(ItemDetail)) {
                            isValid = false;
                        }
                        	}
                        if (!ValidateBatch(ItemDetail)) {
                            isValid = false;
                        }

                        if (!ValidateExpiry(ItemDetail)) {
                            isValid = false;
                        }
                        if (!ValidateCOP(ItemDetail)) {
                            isValid = false;
                        }

                        if (!ValidateMRP(ItemDetail)) {
                            isValid = false;
                        }
                        if(self.model.assetTypeId!=2)
                    	{
                        if (!ValidateMrpUom(ItemDetail)) {
                            isValid = false;
                        }
                    	}
                    }
                }
                var expYear = new Date().getFullYear();
                var expMonth = new Date().getMonth() + 1;
                for (let ItemDetail of self.model.itemList) {
                    var selectedYear = new Date(ItemDetail.expiryDate).getFullYear();
                    var selectedMonth = new Date(ItemDetail.expiryDate).getMonth() + 1;
                    if (selectedYear < expYear) {
                        growl.error('Please Enter Valid Expiry Date');
                        isValid = false;
                    } else if (selectedYear == expYear) {
                        if (selectedMonth < expMonth) {
                            growl.error('Please Enter Valid Expiry Date');
                            isValid = false;
                        }
                    }
                }
            }


            // for (let ItemDetail of ItemDetailList) {
            // if (ItemDetail.quantity > ItemDetail.maxOrderQuantity) {
            // growl.error('Error', {
            // title: "Stock Indent Quantity must be less than Max Order
			// Quantity"
            // });
            // isValid = false;
            // }
            // }

            return isValid;
        }

        var ValidateMrpUom = function (item) {
            var isValid = true;
            if (!item.selectedMrpUom) {
                item.isMrpUomValid = true;
                isValid = false;
                growl.error("Please Select MRP UOM");
            }
            return isValid;
        }

        var ValidateCOP = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.cop))) {
                item.cop = '';
                item.isCopValid = true;
                isValid = false;
                growl.error("Please Enter COP");
            } else if (parseFloat(item.cop) == 0) {
                item.isCopValid = true;
                isValid = false;
                growl.error('COP cannot be zero');
            } else if (item.cop > 0) {
                item.cop = parseFloat(item.cop).toFixed(2);
            }

            if (parseFloat(item.cop) > 0 && parseFloat(item.mrp) > 0) {
                if (parseFloat(item.cop) > parseFloat(item.mrp)) {
                    item.isCopValid = true;
                    isValid = false;
                    growl.error('COP cannot be greater than MRP');
                } else {
                    item.isCopValid = false;
                    item.isMrpValid = false;
                }
            }

            return isValid;
        }

        var ValidateMRP = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.mrp))) {
                item.mrp = '';
                item.isMrpValid = true;
                isValid = false;
                growl.error("Please Enter MRP");
            } else if (parseFloat(item.mrp) == 0) {
                item.isMrpValid = true;
                isValid = false;
                growl.error('MRP cannot be zero');
            } else if (parseFloat(item.mrp) > 0) {
                item.mrp = parseFloat(item.mrp).toFixed(2);
            }

            if (parseFloat(item.cop) > 0 && parseFloat(item.mrp) > 0) {
                if (parseFloat(item.cop) > parseFloat(item.mrp)) {
                    item.isMrpValid = true;
                    isValid = false;
                    growl.error('MRP cannot be less than COP');
                } else {
                    item.isCopValid = false;
                    item.isMrpValid = false;
                }
            }

            return isValid;
        }

        var CalculateCOP = function (item) {
            if (parseInt(item.quantity) > 0 && parseFloat(item.cop) > 0) {
                item.copAmount = (parseInt(item.quantity) * parseFloat(item.cop)).toFixed(2);
            } else {
                item.copAmount = 0;
            }
        }

        var ValidateBatch = function (item) {
            var isValid = true;
            if (!item.batchNo) {
                item.isBatchNoValid = true;
                isValid = false;
                growl.error("Please Enter Batch No.");
            }

            return isValid;
        }

        var ValidateSpecification = function (item) {
            var isValid = true;
            if (!item.specification) {
                item.isSpecificationValid = true;
                isValid = false;
                growl.error("Please Enter Specification");
            }

            return isValid;
        }

        var ValidateExpiry = function (item) {
            var isValid = true;
            if (!item.expiryDate) {
                item.isExpiryDateValid = true;
                isValid = false;
                growl.error("Please Select Expiry Date");
            } else {
                var expYear = new Date().getFullYear();
                var expMonth = new Date().getMonth() + 1;
                var selectedYear = new Date(item.expiryDate).getFullYear();
                var selectedMonth = new Date(item.expiryDate).getMonth() + 1;

                if (selectedYear <= expYear && selectedMonth < expMonth) {
                    growl.error('Expiry date should be later than current month and year');
                    item.isExpiryDateValid = true;
                    isValid = false;
                }
            }

            return isValid;
        }

        var ValidateUom = function (item) {
            var isValid = true;
            if (!item.selectedUom) {
                item.isUomValid = true;
                isValid = false;
                growl.error("Please Select UOM");
            }
            return isValid;
        }

        var ValidateTax = function (item) {
            var isValid = true;
            if (!item.selectedTax) {
                item.isTaxValid = true;
                isValid = false;
                growl.error("Please Select Tax");
            }
            return isValid;
        }

        var SaveNewMaterialInward = function () {          
                if (!self.model.inwardId) {
                    SaveMaterialInward();
                } else {
                    UpdateMaterialInward();
                }            
        }

        var SaveMaterialInward = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var totalAmount = 0.00;
            var totalTaxAmount = 0.00;
            var netAmount = 0.00;
            var returnAmt = 0.00;
            var data = {};
            if (validateSave()) {
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.SAVE_API;
                angular.forEach(self.model.searchData, function(list,key){
                    $scope.returnableGetPassId = list.id;
                    data = {
                        "storeId": self.model.store.id,
                        "vendorId": self.model.vendorId,
                        "assetTypeId": self.model.assetTypeId,
                        "statusId": 2,
                        "approvalStatusId": 10,
                        "requestTypeId": self.model.replacementType.id,
                        "returnableGetPassId": list.id,
                        "remark": self.model.remark,
                        "documentNo": self.model.documentNo,
                        "documentDate": moment(self.model.documentDate).format('YYYY-MM-DD'),
                        "item": [],
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId,
                        "userId":$scope.userId,

                    };

                    if (self.model.replacementType.id == 8) {
                        angular.forEach(list.items, function(datalist,key){
                            if(datalist.id){
                                $scope.returnableGatepassDetailId = datalist.id;
                                $scope.returnableGetPassId = $scope.returnableGetPassId; 
                            }
                            else{
                                datalist.returnableGatepassDetailId = datalist.id;
                                datalist.id = $scope.returnableGetPassId; 
                            }
                            if(!datalist.selectedTax){
                                datalist.selectedTaxId = 0;
                                datalist.selectedTaxPercent = 0;
                            }
                            else{
                                datalist.selectedTaxId = datalist.selectedTax.taxId;
                                datalist.selectedTaxPercent = datalist.selectedTax.taxPercent;
                            }

                            datalist.expiry = datalist.expiry.split("-");
                            var y = datalist.expiry[0];
                            var m = datalist.expiry[1];
                            var d = datalist.expiry[2];

                            m = parseInt(m);
                            if(m < 10){
                                m = "0"+ m;
                            }

                            datalist.expiry = y + "-" + m + "-" + d;
                         	// var item=self.model.itemList[0];
                            // angular.forEach(datalist.items, function (item, key) {
                                if (true) // changed from details to request
											// level discussed with vishal,
											// divyesh
                                {
                                    var storeItem = {
                                        "returnableGetPassId": $scope.returnableGetPassId,
                                        "itemId": datalist.itemId,
                                        "quantity": parseInt(datalist.receivedQuantity),
                                        "uomTypeId": datalist.selectedUom!=undefined && datalist.selectedUom!=null ? datalist.selectedUom.uomTypeId:undefined,
                                        "uomUnitId": datalist.selectedUom!=undefined && datalist.selectedUom!=null ? datalist.selectedUom.uomUnitId:undefined,
                                        "mrpUomTypeId": datalist.selectedMrpUom!=undefined && datalist.selectedMrpUom!=null ? datalist.selectedMrpUom.uomTypeId : undefined,
                                        "markup": datalist.markup,
                                        "batchNo": datalist.batchNo,
                                        "expiryDate": datalist.expiry,
                                        "cop": parseInt(datalist.cop),
                                        "mrp": parseInt(datalist.mrp),
                                        "taxId": datalist.selectedTaxId ? datalist.selectedTaxId : 0,
                                        "taxPercent": datalist.selectedTaxPercent ? datalist.selectedTaxPercent : 0,
                                        "taxAmount": datalist.taxAmount,
                                        "totalAmount": datalist.totalAmount,
                                        "netAmount": datalist.netAmount,
                                        "specification": datalist.specification,
                                        "uom": datalist.defaultUOM!=undefined && datalist.defaultUOM!=null ? datalist.defaultUOM :datalist.uom,
                                        "returnQuantity": datalist.returnQuantity,
                                        "returnAmount": datalist.returnAmount ? datalist.returnAmount : 0.00
                                        /*
										 * start code for resolved HIS-5496
										 * Material Inward - Replacement - Not
										 * working
										 */
                                        ,"returnableGatepassDetailId" :  $scope.returnableGatepassDetailId
                                        /*
										 * end code for resolved HIS-5496
										 * Material Inward - Replacement - Not
										 * working
										 */
                                    }
                                    
                                }
                                else
                                {
                                    var storeItem = {
                                        "returnableGetPassId": $scope.returnableGetPassId,
                                        "itemId": datalist.itemId,
                                        "quantity": parseInt(datalist.receivedQuantity),
                                        "uomTypeId": datalist.selectedUom!=undefined && datalist.selectedUom!=null ? datalist.selectedUom.uomTypeId:undefined,
                                        "uomUnitId": datalist.selectedUom!=undefined && datalist.selectedUom!=null ? datalist.selectedUom.uomUnitId:undefined,
                                        "mrpUomTypeId": datalist.selectedMrpUom!=undefined && datalist.selectedMrpUom!=null ? datalist.selectedMrpUom.uomTypeId : undefined,
                                        "markup": datalist.markup,
                                        "batchNo": datalist.batchNo,
                                        "expiryDate": datalist.expiry,
                                        "cop": parseInt(datalist.cop),
                                        "mrp": parseInt(datalist.mrp),
                                        "taxId": datalist.selectedTaxId ? datalist.selectedTaxId : undefined,
                                        "taxPercent": datalist.selectedTaxPercent ? datalist.selectedTaxPercent : undefined,
                                        "taxAmount": datalist.taxAmount,
                                        "totalAmount": datalist.totalAmount,
                                        "netAmount": datalist.netAmount,
                                        "specification": datalist.specification,
                                        "uom": datalist.defaultUOM!=undefined && datalist.defaultUOM!=null ? datalist.defaultUOM :datalist.uom,
                                        "returnQuantity": datalist.returnQuantity,
                                        "returnAmount": datalist.returnAmount ? datalist.returnAmount : 0.00
                                         /*
											 * start code for resolved HIS-5496
											 * Material Inward - Replacement -
											 * Not working
											 */
                                         ,"returnableGatepassDetailId" :  $scope.returnableGatepassDetailId
                                         /*
											 * end code for resolved HIS-5496
											 * Material Inward - Replacement -
											 * Not working
											 */
                                    }
                                
                                }
                                data.item.push(storeItem);

                                totalAmount = totalAmount + datalist.totalAmount;
                                totalTaxAmount = totalTaxAmount + datalist.taxAmount;
                                netAmount = netAmount + datalist.netAmount;
                                returnAmt = returnAmt + (datalist.returnAmount ? datalist.returnAmount : 0.00);
                              // break;
                            // });
                        });
                        data.totalAmount = totalAmount;
                        data.totalTaxAmount = totalTaxAmount;
                        data.netAmount = netAmount;
                        data.returnAmt = returnAmt;
                    }
                    else {
                        angular.forEach(self.model.searchData, function (item, key) {
                        	var itemdata=item.items[0];
                          // angular.forEach(data12, function(itemdata,key){
                                var storeItem = {
                                		"returnableGetPassId": item.id,
                                		"creditNetAmount":parseInt(item.creditNetAmount) ? item.creditNetAmount : undefined,
                                	    "creditNoteAmount": parseInt(item.creditNoteAmount) ? item.creditNoteAmount : undefined,
                                		"creditTaxAmount": parseFloat(item.creditTaxAmount) ? item.creditTaxAmount : undefined,
                                        "totalAmount": item.totalAmount ? item.totalAmount : undefined,
                                        "totalTaxAmount": item.totalTaxAmount ? item.totalTaxAmount : undefined,
                                        "totalNetAmount": item.totalNetAmount ? item.totalNetAmount : undefined,
                                     /*
										 * start code for resolved HIS-5496
										 * Material Inward - Replacement - Not
							        "creditNetAmount": parseInt(item.creditNetAmount) ? item.creditNetAmount : undefined,
                           			 * working
										 */
                                      "returnableGatepassDetailId" :  itemdata.id
                                     /*
										 * end code for resolved HIS-5496
										 * Material Inward - Replacement - Not
										 * working
										 */
                                }
                                data.item.push(storeItem);
                               // break;
                            // });
                        });
                    }
                });

                if(data === undefined || data === null || data === {} || data.item === undefined || data.item === null || data.item.length === 0){
                    growl.warning('Please add item first!');
                    return false;
                }
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    // ClearRequests();
                    $rootScope.stopLoader();
                    NavigteToBack();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
            }
        }

        var UpdateMaterialInward = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.UPDATE_API + self.model.inwardId;

            var data = {
               "storeId": self.model.store.id,
                "statusId": 2,
                "totalAmount": self.model.totalAmount ? self.model.totalAmount : self.model.totalAmount,
                "totalTaxAmount": self.model.totalTaxAmount ? self.model.totalTaxAmount : self.model.totalTaxAmount,
                "totalNetAmount": self.model.netAmount ? self.model.netAmount : self.model.totalNetAmount,
                "remark": self.model.remark,
                "assetTypeId": self.model.assetTypeId,
                "documentNo": self.model.documentNo,
                "documentDate": moment(self.model.documentDate).format('YYYY-MM-DD'),
                "item": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            if (self.model.replacementType.id == 8) {
                angular.forEach(self.model.itemList, function (item, key) {
                	if(item.expiry!=undefined)
                		{
                    item.expiry = item.expiry.split("-");
                    var y = item.expiry[0];
                    var m = item.expiry[1];
                    var d = item.expiry[2];

                    m = parseInt(m);
                    if(m < 10){
                        m = "0"+ m;
                    }

                    item.expiry = y + "-" + m + "-" + d;
                		}
                    var storeItem = {
                    	"returnableGetPassId": $scope.returnableGatepassDetailId,
                        "id": item.id,
                        "batchNo": item.batchNo,
                        "expiryDate": item.expiry,
                        "cop": parseInt(item.cop),
                        "mrp": parseInt(item.mrp),
                        "markup": item.markup,
                        "mrpUomTypeId": item.selectedMrpUom!=undefined && item.selectedMrpUom!=null ? item.selectedMrpUom.uomTypeId : undefined,
                        "taxAmount": item.taxAmount,
                        "totalAmount": item.totalAmount,
                        "netAmount": item.netAmount,
                        "receivedQuantity": parseInt(item.receivedQuantity),
                        "uomTypeId": item.selectedMrpUom!=undefined && item.selectedMrpUom!=null ? item.selectedUom.uomTypeId : undefined,
                        "uomUnitId": item.selectedMrpUom!=undefined && item.selectedMrpUom!=null ? item.selectedUom.uomUnitId : undefined,
                        "specification": item.specification,
                        "uom": item.defaultUOM!=undefined && item.defaultUOM!=null ? item.defaultUOM:item.uom
                    }
                    data.item.push(storeItem);
                });
            }
            else{
            	 angular.forEach(self.model.searchData, function (item, key) {
            		 var creditNetAmount=(item.creditTaxAmount != undefined && item.creditTaxAmount != null ? parseFloat(item.creditTaxAmount) : 0)+
            		 (item.creditNoteAmount != undefined && item.creditNoteAmount != null ?  parseFloat(item.creditNoteAmount): 0);
                         var storeItem = {
                        		 "returnableGetPassId": item.id,
                        		 "id": item.id,
                                 "creditNoteAmount":item.creditNoteAmount != undefined && item.creditNoteAmount != null ?  parseFloat(item.creditNoteAmount): undefined,
                                 "creditTaxAmount": item.creditTaxAmount != undefined && item.creditTaxAmount != null ? parseFloat(item.creditTaxAmount) : undefined,
                                 "creditNetAmount": creditNetAmount != undefined && creditNetAmount != null ? parseFloat(creditNetAmount) : undefined,
                         }
                         data.item.push(storeItem);
                 });
        
            }
            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {

                    $rootScope.stopLoader();
                    NavigteToBack();
                    growl.success(response.data.message);
                },

                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var AddDuplicateRow = function (item) {
            var data = angular.copy(item);

            data.receivedQuantity = '';
            data.isQuantityValid = false;
            data.batchNo = '';
            data.expiryDate = '';
            data.specification = '';
            data.mrp = '';
            data.cop = '';
            data.taxAmount = 0;
            data.markup = '';
            data.totalAmount = 0;
            data.netAmount = 0;
            data.isBatchNoValid = false;
            data.isCopValid = false;
            data.isExpiryDateValid = false;
            data.isMrpValid = false;
            data.isQuantityValid = false;
            data.isUomValid = false;

            delete data.id;
            delete data.selectedUom;

            self.model.itemList.push(data);
        }

        function removeRowData() {
            var index = self.model.itemList.indexOf(self.model.itemRemove);
            self.model.itemList.splice(index, 1);
        }
        
        
       

        var ConfirmRemove = function (index,item) {
            self.model.itemForRemove = item;
            $scope.indexToRemoveItem=index;
            var popup = angular.element('#confirmRemove');
            popup.modal('show');

        }
        var RemoveItem = function (item) {
            self.model.itemList.splice($scope.indexToRemoveItem, 1);
            self.model.searchData.splice($scope.indexToRemoveItem, 1);
           
            var popup = angular.element('#confirmRemove');
            popup.modal('hide');
          }
        
        /*$scope.RemoveItem = function(index){
			if(index != 0){
				$scope.model.itemList.splice(index,1);
			}*/

        /*var RemoveItem = function (item) {

        	 var index=self.model.searchData.indexOf(item);
        	 if(self.model.replacementType.id ==9)
        	 {
             self.model.searchData.splice(index,1);
        	 }
        	 else
        	 {
        	   if(self.model.itemList.length >0)
             {
                 var index=self.model.itemList.indexOf(item);
               self.model.itemList.splice(index,1);     

             }       
        	 }
          
            
            var popup = angular.element('#confirmRemove');
            popup.modal('hide');
        }*/

        var GetAssetCategoryList = function () {
            var inward = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetTypeId + "/" + $scope.unitId + "/" + $scope.organizationId;

            inward.assetCategory = inward.assetCategories[0];
            inward.productCategory = inward.productCategories[0];

            inward.assetCategories.splice(1);
            inward.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    inward.assetCategories = inward.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var inward = self.model;

            // TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    inward.productCategories = inward.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function searchNewItem() {
            if(self.model.itemName){
                if(self.model.itemName.length >= 3){        
                    var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;
                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.itemCode,
                        "itemName": self.model.itemName
                    }

                    self.model.searchNewItemList = [];
                
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.searchNewItemList = response.data ? response.data : [];
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.itemCode){
                if(self.model.itemCode.length >= 3){
                    var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;
                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.itemCode,
                        "itemName": self.model.itemName
                    }

                    self.model.searchNewItemList = [];
                
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.searchNewItemList = response.data ? response.data : [];
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                }
            }
            else{
                self.model.searchNewItemList = [];
            }
        }

        var setReplacement = function (item) {
            if (self.model.canReplace == true) {
                item.isReplaced = true;
            }
        }

        var AddItemsPopup = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        	$scope.saveBtnDisabled=true;
            // new test api
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.store.id
            };

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.substitutedItem = response.data;

                    setSubstitute(self.model.substitutedItem);
                    var selectedUom;
                    angular.forEach(self.model.substitutedItem, function (x) {
                        x.selectedTax = x.taxList.find(function (t) {
                            x.taxName = t.taxName;
                            x.taxPercent = t.taxPercent;
                            return x.taxId  == t.taxId;
                        });
                        if (x.uom) {
                            selectedUom = x.uom.find(function (sUom) {
                                return sUom.storeUom == true;
                            });
                            x["uomType"] = selectedUom.uomDetails;
                            x["uomUnit"] = selectedUom.uomUnit;
                            x["uomTypeId"] = selectedUom.uomTypeId;
                            x["uomUnitId"] = selectedUom.uomUnitId;
                        }
                        else {
                            x["uomType"] = x.uomDetails;
                            x["uomUnit"] = x.uomUnit;
                            x["uomTypeId"] = x.uomTypeId;
                            x["uomUnitId"] = x.uomUnitId;
                        }
                        if(x.isThreeUOMApplicable == false){
                            x.uomListData = x.uom;
                            x.uom = [];
                            x.mrpUOM = [];
                            x.uomListData.find(function(d){
                                if(d.uomTypeId != 2){
                                    x.uom.push(d);
                                    x.mrpUOM.push(d);
                                }
                            })
                        }
                    });
                    // self.model.ItemDetailList =
					// self.model.ItemDetailList.concat(response.data);
                    self.model.SelectedItemsIds = [];
                    self.model.items = [];

                    $rootScope.stopLoader();
                    // getAllItemDetails();
                    // growl.success(response.data.message);

                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.message);
                });
            angular.element('#addNewItemWasteStore').modal('hide');
        }

        var SelectAllItemsPopup = function () {
            if (self.model.selectAllRowsPopup) {
                self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.searchNewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.isSelectedIndent.items.forEach(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.isSelectedIndent.items.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectItemPopup = function (item) {
            var itemIds = self.model.SelectedItemsIds;

            if (item.isSelected) {
                itemIds.push(item.itemId);
            } else {
                var index = itemIds.indexOf(item.itemId);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.searchNewItemList.every(isSelected)) {
                self.model.selectAllRowsPopup = true;
            } else {
                self.model.selectAllRowsPopup = false;
            }

            self.model.isItemChecked = false;
            self.model.searchNewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var openItemModal = function (index, item) {
        	$scope.saveBtnDisabled=false;
            self.model.SelectedItemsIds = [];
            self.model.substituteIndex = index;
            self.model.substituteItem = item;
            $("#addNewItemWasteStore").modal();
            GetAssetCategoryList();
        }

        var setSubstitute = function (list) {
            var newList = list[0];

            var defaultUOM = [];
            for (let uom of newList.uom) {
                defaultUOM.push({
                    uomTypeId: uom.uomTypeId,
                    uomUnitId: uom.uomUnitId,
                    conversion: uom.conversion,
                    ipUom: uom.ipUom,
                    opUom: uom.opUom,
                    storeUom: uom.storeUom
                });
            }
            newList['defaultUOM'] = defaultUOM;
            var index = self.model.substituteIndex;
            self.model.itemList.splice(index + 1, 0, newList);
            var disabled = 0;
            self.model.itemList.forEach(function (item) {
                if (item.isDisable)
                disabled++;
            });
            if (disabled == 0)
            {
                self.model.itemList[index].isDisable = true;
                clearFields(self.model.itemList[index]);
            }

        }

        var ValidateQuantity = function (item) {
            var isValid = true;
            calculateStock(item);
            if (isNaN(parseFloat(item.receivedQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Received Quantity");
            } else if (parseInt(item.receivedQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }

            else if (parseInt(item.rcvdQty) > item.rtnQty) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered received quantity is exceeding the return quantity');
            } else {
                item.quantity = parseInt(item.receivedQuantity);
                isValid = true;
            }

            return isValid;
        }
        var clearFields = function (item) {
            item.markup = '';
            item.receivedQuantity='';
           // item.quantity = 0;
           // item.selectedUom.uomTypeId = 1;
           // item.selectedUom.uomUnitId = 74;
           // item.selectedMrpUom.mrpUomTypeId = 1;
            item.batchNo = '';
            item.expiryDate = '';
            item.cop = '';
            item.mrp = '';
            // item.selectedTax.taxId = '';
           // item.selectedTax.taxPercent = '';
            item.taxAmount = 0.00;
            item.totalAmount = 0.00;
            item.netAmount = 0.00;
            item.specification = '';
           // item.uomUnitreturnable = '';
          // item.taxName = '';
           // item.returnAmount = 0.00;
            item.taxId = '';
            item.taxPercent = '';
          // item.defaultUOM = '';
        }

        /*
		 * var RemoveItem = function () { /!*self.model.creditNoteAmount = null;
		 * self.model.creditTaxAmount = null; self.model.creditNetAmount =
		 * null;*!/ self.model.items = []; console.log("Items :
		 * ",self.model.items); }
		 */

        var ValidateCredit = function(type, item) {
            if(type == "Note") {
                if(Number(item.creditNoteAmount) > Number(item.totalAmount)){
                    item.isCreditNoteValid = true;
                    growl.error("Credit Note " + self.model.Currency + " must be less than or equals to Total " + self.model.Currency);
                    item.creditNoteAmount = item.totalAmount;
                } else {
                    item.isCreditNoteValid = false;
                    var taxamount = item.creditTaxAmount ? item.creditTaxAmount : 0;
                    item.creditNetAmount = Number(item.creditNoteAmount) + Number(taxamount);
                    if(Number(item.creditNetAmount) > Number(item.totalNetAmount)){
                        item.isCreditNetValid = true;
                        growl.error("Credit Net " + self.model.Currency + " must be less than or equals to Net " + self.model.Currency);
                        item.creditNetAmount = item.totalNetAmount;
                    } else {
                        item.isCreditNetValid = false;
                    }
                }
            } else if(type == "Tax") {
                if(Number(item.creditTaxAmount) > Number(item.totalTaxAmount)){
                    item.isCreditTaxValid = true;
                    growl.error("Credit Tax " + self.model.Currency + " must be less than or equals to Total Tax " + self.model.Currency);
                    item.creditTaxAmount = item.totalTaxAmount;
                } else {
                    item.isCreditTaxValid = false;
                    var creditmount = item.creditNoteAmount ? item.creditNoteAmount : 0;
                    item.creditNetAmount = Number(item.creditTaxAmount) + Number(creditmount);
                    item.creditNetAmount = item.creditNetAmount.toFixed(2);
                    if(Number(item.creditNetAmount) > Number(item.totalNetAmount)){
                        item.isCreditNetValid = true;
                        growl.error("Credit Net " + self.model.Currency + " must be less than or equals to Net " + self.model.Currency);
                        item.creditNetAmount = item.totalNetAmount;
                    } else {
                        item.isCreditNetValid = false;
                    }
                }
            } else if(type == "Net") {
                if(Number(item.creditNetAmount) > Number(item.totalNetAmount)){
                    item.isCreditNetValid = true;
                    growl.error("Credit Net " + self.model.Currency + " must be less than or equals to Net " + self.model.Currency);
                    item.creditNetAmount=item.totalNetAmount;
                } else {
                    item.isCreditNetValid = false;
                }
            }
            item.creditNetAmount=(item.creditNoteAmount!=undefined && item.creditNoteAmount!=null ? parseFloat(item.creditNoteAmount) :0)+(item.creditTaxAmount!=undefined && item.creditTaxAmount!=null ? parseFloat(item.creditTaxAmount):0);
            item.creditNetAmount = item.creditNetAmount.toFixed(2);
        }

        $scope.calculateReceivedQty = function (item) {
            if(self.model.assetTypeId != 2){
                if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
                    item.returnQuantity = 0;
                }
                if(item.receivedQuantity == undefined || item.receivedQuantity == null || item.receivedQuantity == ""){
                    item.receivedQuantity = 0;
                }
                if(item.isThreeUOMApplicable == false){
                    if(item.selectedUom.uomTypeId == 1){
                        // item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uomListData[0].conversion);
                        // item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uomListData[0].conversion);
                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uomListData[0].conversion);
                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uomListData[0].conversion);
                        item.uomUnitData = item.uomListData[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        
                    }
                    else{
                        // item.receivedQtyConForReturn = (item.returnQuantity);
                        // item.receivedQtyCalculationForReturn = (item.returnQuantity);
                        item.receivedQtyConForReceived = (item.receivedQuantity);
                        item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                        item.uomUnitData = item.uomListData[2].uomUnit;
                    }
                }
                else{
                    if(item.selectedUom.uomTypeId == 1){
                        // item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                        // item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        // item.receivedQtyConForReturn = (item.returnQuantity +"*"+ item.uom[1].conversion);
                        // item.receivedQtyCalculationForReturn = (item.returnQuantity * item.uom[1].conversion);
                        item.receivedQtyConForReceived = (item.receivedQuantity +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculationForReceived = (item.receivedQuantity * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                    else{
                        // item.receivedQtyConForReturn = (item.returnQuantity);
                        // item.receivedQtyCalculationForReturn = (item.returnQuantity);
                        item.receivedQtyConForReceived = (item.receivedQuantity);
                        item.receivedQtyCalculationForReceived = (item.receivedQuantity);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                }
            }
		}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addItemMaterialInwards", {
            url: "/addItemMaterialInwards",
            templateUrl: 'views/PharmacyInventory/Store/add-item-material-inwords.html',
            controller: "AddNewMaterialInward.Controller",
            controllerAs: "vm",
            title: 'Material Inwards'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewMaterialInward.Controller", addNewMaterialInwardController);
})(); 