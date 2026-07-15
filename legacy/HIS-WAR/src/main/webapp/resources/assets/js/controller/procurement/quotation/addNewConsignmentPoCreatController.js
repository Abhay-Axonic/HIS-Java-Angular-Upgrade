(function () {
    "use strict";
  
    function addNewConsignmentPoCreatController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, growl, CurrencyService, StatusService) {
      var self = this;
      var getNextMonth = new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1);
  
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
          pagination: {
            currentPage: 1,
            totalPages: 0,
            pageSize: '10',
            search: '',
            searchData: PaginationService.searchData,
            searchDataByField:PaginationService.searchDataByField,
            setTotalPages: PaginationService.setTotalPages,
            getPagesList: PaginationService.getPagesList,
            setPage: PaginationService.setPage,
            setFirstPage: PaginationService.setFirstPage,
            setPreviousPage: PaginationService.setPreviousPage,
            setNextPage: PaginationService.setNextPage,
            setLastPage: PaginationService.setLastPage,
            sortBy: PaginationService.sortBy
          },
          fromDate: new Date(),
          toDate: new Date(),
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
          stagedQtyDateOptions: {
            formatYear: 'yyyy',
            showWeeks: false,
            minDate: new Date()
          },
          assetCategories: [{
            "category": "Select Asset Category"
          }],
          productCategories: [{
            "category": "Select Product Category"
          }],
          stores: [{
            "store": "Select Store"
          }],
          fromStores: [{
            "store": "Select Store"
          }],
          toStores: [{
            "storeName": "Select Store"
          }],
          paymentModes: [{
            "code": "Select Payment Mode"
          }],
          poTypes: [{
            "type": "Select PO Type"
          }],
          SelectedItemsIds: [],
          accessoryItems: [],
          addedAccessoryItems: [],
          itemDetails: [],
          intendItemIds: [],
          indentItemDetails: [],
          bonusQuantity: [],
          costOfPurchase: [],
          POAmountMRP: [],
          selectedUOMList: [],
          selectedUOMUnit: [],
          bonusSelectedUOMList: [],
          bonusSelectedUOMUnit: [],
          stageQuantityList: [],
          selectedItemIndex: 0,
          bonusSelectedItemIndex: 0,
          selectedIndex: null,
          selectedOtherChargeIndex: null,
          otherChargesDropdownList: [],
          otherChargesList: [],
          prDetails: [],
          taxCodeValid: [],
          taxPercent: [],
          isConsignment: true,
          prModalList: [],
          dateOpen: [],
          serviceProviderValid: [],
          warrantyPeriodValid: [],
          searchItem: {
            itemName: undefined,
            itemCode: undefined
          },
          discountPercent: [],
          discountAmount: [],
          defaultOtherCharge: [{
            "otherCharges": "Select Other Charge"
          }],
          warrantyDateOptions: {
            minDate: getNextMonth,
            minMode: "month"
          },
          uomArray: [{
            "uomType": "Select UOM"
          }],
          PRType: 16, //Against Consignment
          prRequestTypeId: 16,//Against Consignment
          taxListSelection: [{
            "taxName": "Select tax"
          }]
          
          
          	// added by minesh
			,SelectedOrderIds : [],
			SelectedGRNOrderIds : [],
			selectedItems : [],
			setSelectedItems : [],
			selectedGRN : [],
			setSelectedGRN : [],
			selectAllGRNRows : false,
			//totalAmount : 0.00,
			//totalTaxAmount : 0.00,
			//totalNetAmount : 0.00,
			// end
        };
  
        $scope.allvendors = function(){
          var data = "";
          var vendorNameAfterEditInitiate="noSelectedVendorFound";
          var URI = CONSTANTS.GLOBAL.ALL_VENDOR_SEARCH_API + "unit/"+$scope.unitId+"/vendorName/"+vendorNameAfterEditInitiate;
          GenericService.serviceAction("GET", URI, data).then(function(response) {
            if (response.status == 200) {
              $scope.allVendorList = response.data;    
            }
          });	
        }
        $scope.allvendors();
        
        var cookieObject = {};
        var cookieObjectAppConfig = {};
  
        [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
        self.model.userId = cookieObject.userId;
  
        var cookieObject = $cookies.getObject('cookieObject');
        if (cookieObject == undefined) {
          $state.go('login');
          return;
        }
  
        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        if (cookieObjectAppConfig == undefined) {
          $state.go('login');
          return;
        }
  
        $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
  
        StatusService.GetStatus().then(function (status) {
          self.model.statuses = status;
        });
  
        CurrencyService.GetCurrency().then(function (currency) {
          self.model.Currency = currency;
        });
  
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenStagedQtyDate = OpenStagedQtyDate;
        self.model.OpenToDate = OpenToDate;
        self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
        self.model.PopulateProductCategory = PopulateProductCategory;
        self.model.GetItemList = GetItemList;
        self.model.SelectAllItems = SelectAllItems;
        self.model.SelectItem = SelectItem;
        self.model.AddItems = AddItems;
        self.model.RemoveItem = RemoveItem;
        self.model.ConfirmRemove = ConfirmRemove;
        self.model.SavePurchaseOrder = SavePurchaseOrder;
        self.model.NavigateToPO = NavigateToPO;
        self.model.GetGRNPopup = GetGRNPopup;
        self.model.GetIndentList = GetIndentList;
        self.model.Expand = Expand;
        self.model.SelectAllIndentItems = SelectAllIndentItems;
        self.model.SelectIndentItem = SelectIndentItem;
        self.model.AddIndentItems = AddIndentItems;
        self.model.SearchVendorNames = SearchVendorNames;
        self.model.AddVendorName = AddVendorName;
        self.model.SearchProviderNames = SearchProviderNames;
        self.model.AddProviderName = AddProviderName;
        self.model.OpenPopup = OpenPopup;
        self.model.ClosePopup = ClosePopup;
        self.model.uomInfoOnClick = uomInfoOnClick;
        self.model.uomModalOnClick = uomModalOnClick;
        self.model.bonusUomInfoOnClick = bonusUomInfoOnClick;
        self.model.bonusUomModalOnClick = bonusUomModalOnClick;
        self.model.isConsignmentFlag = isConsignmentFlag;
        self.model.fillPOQuantity = fillPOQuantity;
        self.model.addPOQuantity = addPOQuantity;
        self.model.lessPOQuantity = lessPOQuantity;
        self.model.sumPOQuantity = sumPOQuantity;
        self.model.AddOtherCharge = AddOtherCharge;
        self.model.lessOtherCharge = lessOtherCharge;
        self.model.sumOtherCharges = sumOtherCharges;
        self.model.ApplyDiscount = ApplyDiscount;
        self.model.fillOtherChargesList = fillOtherChargesList;
        self.model.fillPRModalList = fillPRModalList;
        self.model.SetDecimal = SetDecimal;
        self.model.OpenWarrantyDate = OpenWarrantyDate;
        self.model.ViewTerms = ViewTerms;
        self.model.ViewItems = ViewItems;
        self.model.ViewAddedItems = ViewAddedItems;
        self.model.SaveTerms = SaveTerms;
        self.model.ValidateConsumableAmount = ValidateConsumableAmount;
        self.model.ApplyDiscountValidation = ApplyDiscountValidation;
        self.model.SetDecimalDiscount = SetDecimalDiscount;
        self.model.SetDecimalOtherCharge = SetDecimalOtherCharge;
        self.model.CalculateStock = CalculateStock;
        self.model.AddItemDiscount = AddItemDiscount;
        self.model.SelectTax = SelectTax;
  
        self.model.SetOldValueStore = SetOldValueStore;
        self.model.ChangeFromStore = ChangeFromStore;
        self.model.ConfirmStorePopup = ConfirmStorePopup;
        self.model.SetOldValueAssetType = SetOldValueAssetType;
        self.model.ChangeFromAssetType = ChangeFromAssetType;
        self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
        self.model.ConfirmValidation = ConfirmValidation;
        self.model.EmptyList = EmptyList;
        self.model.discountCalc = discountCalc;
        self.model.ValidatePOQuantity = ValidatePOQuantity;
        self.model.ViewHistory = ViewHistory;
        self.model.ValidateStagedQuantity = ValidateStagedQuantity;
        //self.model.CalculateAmount = CalculateAmount;
        self.model.SetOldType=SetOldType;
        self.model.GetQRIndentList = GetQRIndentList;
        self.model.AddQRIndentItems = AddQRIndentItems;
        self.model.updateQRItemDetails = updateQRItemDetails;
        self.model.SelectedQRItems = SelectedQRItems;
        PopulateDropdown();
        
        // minesh
		self.model.GetConsignmentGRNList = GetConsignmentGRNList;
		self.model.SelectItemForGRN = SelectItemForGRN;
		self.model.SelectAllGRNItems = SelectAllGRNItems;
		self.model.SelectAllGRN = SelectAllGRN;
		self.model.SelectGRN = SelectGRN;
		self.model.SetSelectedItem = SetSelectedItem;
		self.model.RemoveItem = RemoveItem;
		//self.model.CalculateAmounts = CalculateAmounts;
		self.model.ClearId = ClearId;
		self.model.ConsignmentStockPOHistory=ConsignmentStockPOHistory;
		
		self.model.Expand=Expand;
		self.model.SelectAllItemsOfGRN = SelectAllItemsOfGRN;
      }
      
      var fillPRModalList = function (list) {
        self.model.prModalList = list;
      }
     
      var OpenWarrantyDate = function (idx) {
        self.model.dateOpen[idx] = true;
      }
  
      var discountCalc = function (item) {
        if (!parseFloat(item.discountPercent)) {
          item.discountPercent = 0;
        }
        item.poAmount = item.poAmount || 0;
  
        item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
        item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
      }
  
      var ViewItems = function (item) {
        //PopulateAssetCategoryDropdown();
        self.model.addItemFlag = false;
        self.model.selectedItemId = item.itemId;
      }
  
      var ViewAddedItems = function (item) {
        self.model.addedAccessoryItems = item.childAccessoryItems;
      }
  
      // store change confirmation
      var SetOldValueStore = function (store) {
        self.model.oldStore = store;
        self.model.store = self.model.oldStore;
      }
  
      var ChangeFromStore = function (store) {
        if (!!self.model.itemDetails.length) {
          self.model.store = self.model.oldStore;
          self.model.storeNew = JSON.parse(JSON.stringify(store));
          OpenPopup('Open', 'confirmStore');
        }
      }
  
      var ConfirmStorePopup = function () {
        self.model.store = self.model.storeNew;
        ClosePopup('close', 'confirmStore');
        self.model.itemDetails = [];
      }
  
      // asset type change confirmation
      var SetOldValueAssetType = function (assetType) {
        self.model.oldAssetType = assetType;
        self.model.poType = self.model.oldAssetType;
      }
  
      var ChangeFromAssetType = function (assetType) {
        if (!!self.model.itemDetails.length) {
          self.model.poType = self.model.oldAssetType;
          self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
          OpenPopup('Open', 'confirmAssetTypePopup');
        }
      }
  
      var ConfirmAssetTypePopup = function () {
        self.model.poType = self.model.assetTypeNew;
        ClosePopup('close', 'confirmAssetTypePopup');
        self.model.itemDetails = [];
      }
  
      // direct request change confirmation
      var ConfirmValidation = function (prType, oldValue) {
        self.model.oldPrRequestType = oldValue;
        self.model.prRequestTypeId = prType;
        if (!!self.model.itemDetails.length) {
          OpenPopup('open', 'confirmValidation');
        } else {
          self.model.PRType = prType;
        }
      }
  
      var SetOldType=function () {
        self.model.prRequestTypeId =  self.model.oldPrRequestType;
      }
  
      var EmptyList = function () {
        self.model.PRType = self.model.prRequestTypeId;
        self.model.itemDetails = [];
  
        ClosePopup('close', 'confirmValidation');
      }
  
      var SaveTerms = function (viewModal) {
        switch (viewModal) {
          case "warrantyPeriodModal":
            self.model.itemTerms.warrantyPeriod = self.model.warrantyPeriod;
            self.model.itemTerms.isValidWarrantyTerm = false;
            ClosePopup('Close', 'warrantyPeriodModal');
            break;
          case "expectedSuportTermsModal":
            self.model.itemTerms.supportTerms = self.model.supportTerms;
            self.model.itemTerms.isValidSupportTerm = false;
            ClosePopup('Close', 'expectedSuportTermsModal');
            break;
          case "expectedPaymentTermsModal":
            self.model.itemTerms.paymentTerms = self.model.paymentTerms;
            self.model.itemTerms.isValidPaymentTerm = false;
            ClosePopup('Close', 'expectedPaymentTermsModal');
            break;
          case "expectedTechSpecificationModal":
            self.model.itemTerms.technicalSpecs = self.model.technicalSpecs;
            self.model.itemTerms.isValidTechSpec = false;
            ClosePopup('Close', 'expectedTechSpecificationModal');
            break;
        }
      }
  
      var ViewTerms = function (viewModal, item) {
        self.model.itemTerms = item;
        switch (viewModal) {
          case "warrantyPeriodModal":
            self.model.itemDetails.find(function (x) {
              if (item.itemId == x.itemId) {
                self.model.warrantyPeriod = x.warrantyPeriod;
              }
            });
            break;
          case "expectedSuportTermsModal":
            self.model.itemDetails.find(function (x) {
              if (item.itemId == x.itemId) {
                self.model.supportTerms = x.supportTerms;
              }
            });
            break;
          case "expectedPaymentTermsModal":
            self.model.itemDetails.find(function (x) {
              if (item.itemId == x.itemId) {
                self.model.paymentTerms = x.paymentTerms;
              }
            });
            break;
          case "expectedTechSpecificationModal":
            self.model.itemDetails.find(function (x) {
              if (item.itemId == x.itemId) {
                self.model.technicalSpecs = x.technicalSpecs;
              }
            });
            break;
        }
      }
  
      /*var fillOtherChargesList = function (item) {
        self.model.selectedOtherChargeIndex = self.model.itemDetails.indexOf(item);
  
        angular.forEach(item.selectedOtherCharges, function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            return key.id == value.otherChargesId;
          });
        });
  
        item.selectedOtherCharges.forEach(function (othercharge) {
          var amountNumber = parseFloat(othercharge.amount);
          othercharge.amount = amountNumber.toFixed(2);
        });
        self.model.otherChargesList = item.selectedOtherCharges;
      }*/
      
      var fillOtherChargesList = function (item) {
          angular.forEach(item, function(data){
            self.model.selectedOtherChargeIndex = self.model.itemDetails.indexOf(data);

            angular.forEach(data.selectedOtherCharges, function (value) {
              value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
                return key.id == value.otherChargesId;
              });
            });

            data.selectedOtherCharges.forEach(function (othercharge) {
              var amountNumber = parseFloat(othercharge.amount);
              var am = amountNumber.toFixed(2);
              othercharge.amount = parseInt(am);
            });
            self.model.otherChargesList = data.selectedOtherCharges;
          });
        }
  
      var validateCharges = function () {
        var isValidItem = true;
        if (self.model.otherChargesList.length <= 0) {
          growl.error('Please add other charges and amount');
          isValidItem = false;
        } else {
          angular.forEach(self.model.otherChargesList, function (charges) {
            charges['isChargesValid'] = false;
            charges['isAmountValid'] = false;
  
            if (charges.otherCharge.id) {
              if (!parseFloat(charges.amount)) {
                charges['isAmountValid'] = true;
                growl.error('Please enter amount');
                isValidItem = false;
              }
            }
            if (!!parseFloat(charges.amount)) {
              if (!charges.otherCharge.id) {
                charges['isChargesValid'] = true;
                growl.error('Please select other charges');
                isValidItem = false;
              }
            }
  
          });
        }
        return isValidItem;
      }
  
      function DuplicateData(chargeList) {
        var isValid = true;
        var object = {};
        var result = [];
  
        chargeList.forEach(function (item) {
          if (!!item.otherCharge.id) {
            if (!object[item.otherCharge.id])
              object[item.otherCharge.id] = 0;
            object[item.otherCharge.id] += 1;
          }
        })
  
        for (var prop in object) {
          if (object[prop] >= 2) {
            result.push(prop);
          }
        }
  
        if (!!result.length) {
          isValid = false;
          growl.error("Duplicate other charges not allowed");
        }
  
        return isValid;
      }
  
      var sumOtherCharges = function (chargeList) {
        if (validateCharges() && DuplicateData(chargeList)) {
          var total = 0;
          self.model.otherChargesList.forEach(function (value) {
            value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
              if (value.otherCharge.id == key.id) {
                value.otherChargesId = key.id;
                return key;
              }
            });
          });
  
          angular.forEach(self.model.otherChargesList, function (value, key) {
            total = total + parseFloat(value.amount);
          });
          self.model.itemDetails[self.model.selectedOtherChargeIndex].otherChargesTotal = total;
          self.model.ClosePopup('close', 'otherCharges_Modal');
        }
      }
  
      var AddOtherCharge = function (ocId, index) {
        self.model.otherChargesList.push({ otherChargesId: ocId, amount: 0 });
        self.model.otherChargesList[index + 1].otherCharge = self.model.otherChargesDropdownList[0];
      }
  
      var lessOtherCharge = function (item, idx) {
        var index = self.model.otherChargesList.indexOf(item);
        self.model.otherChargesList.splice(index, 1);
      }
  
      var fillPOQuantity = function (item) {
        item.stageQuantityList.forEach(function (x) {
          x.stagedDate = new Date(x.stagedDate);
        });
        self.model.stageQuantityList = item.stageQuantityList;
        self.model.selectedIndex = self.model.itemDetails.indexOf(item);
      }
  
      var addPOQuantity = function (date) {
        self.model.stageQuantityList.push({ stagedDate: date, quantity: parseInt(0) });
      }
  
      var lessPOQuantity = function (item) {
        var index = self.model.stageQuantityList.indexOf(item);
        self.model.stageQuantityList.splice(index, 1);
      }
  
      var ValidStagedDate = function (selectedItem) {
        var isValid = true;
        self.model.stageQuantityList.forEach(function (value) {
          if (!ValidateStagedQuantity(value)) {
            isValid = false;
          }
        });
  
        var total = 0;
        var qtyValue;
        self.model.stageQuantityList.forEach(function (val) {
          total = total + parseInt(val.quantity);
        });
        
          if (total > selectedItem.pendingPOQuantity) {
            growl.error("Total staged quantity should be less than pending quantity");
            isValid = false;
          }       
        return isValid;
      }
  
      var sumPOQuantity = function (selectedItem) {
        if (ValidStagedDate(selectedItem)) {
          var total = 0;
          angular.forEach(self.model.stageQuantityList, function (value, key) {
            delete value['stagedQtyDate'];
            if (!!value['stagedDate']) {
              value['stagedDate'] = moment(value['stagedDate']).format('YYYY-MM-DD');
            }
            // self.model.isValidPoQuantity[key] = false;
            selectedItem.isPOQuantityValid = false;
  
            if (!value.quantity) {
              value.quantity = 0;
            }
            total = total + parseInt(value.quantity);
          });
  
          self.model.itemDetails[self.model.selectedIndex].poQuantity = total;
          // for update discount
          selectedItem.discountPercent = selectedItem.discountPercent || 0;
          selectedItem.costOfPurchase = selectedItem.costOfPurchase || 0;
  
          var baseAmount = parseFloat(selectedItem.costOfPurchase) * total;
          selectedItem.discountAmount = baseAmount * (selectedItem.discountPercent / 100);
          selectedItem.discountAmount = parseFloat(selectedItem.discountAmount).toFixed(2);
         // CalculateAmount(selectedItem);
          self.model.ClosePopup('close', 'AddQtyModal_ETOitemConfig');
        }
      }
  
      var setStatus = function (x) {
        var status = self.model.statuses.find(function (obj) {
          return obj.status == x;
        });
  
        return status.id;
      }
  
      var OpenStagedQtyDate = function (quantity) {
        quantity.stagedQtyDate = true;
      }
  
      var OpenFromDate = function () {
        self.model.fromDateOpened = true;
        self.model.toDateOptions.minDate = self.model.fromDate;
      }
  
      var OpenToDate = function () {
        self.model.toDateOpened = true;
        self.model.toDateOptions.minDate = self.model.fromDate;
      }
  
      var OpenPopup = function (action, modal) {
        self.model.action = action;
        var popup = angular.element('#' + modal);
        popup.modal('show');
      }
  
      var ClosePopup = function (action, modal) {
        self.model.action = action;
        var popup = angular.element('#' + modal);
        popup.modal('hide');
      }
  
      var isConsignmentFlag = function (flag) {
        if (flag) {
          self.model.isConsignment = true;
        } else {
          self.model.isConsignment = false;
        }
      }
  
      /*var PopulateDropdown = function () {
        try {
          $rootScope.startLoader();
          var purchaseOrder = self.model;
          purchaseOrder.store = purchaseOrder.stores[0];
        //  purchaseOrder.fromStore = purchaseOrder.fromStores[0];
          purchaseOrder.poType = purchaseOrder.poTypes[0];
  
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.DROPDOWN_API;
  
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              purchaseOrder.stores = purchaseOrder.stores.concat(response.data.deliveryStores);
              purchaseOrder.fromStores = purchaseOrder.fromStores.concat(response.data.requestingStores);
              purchaseOrder.poTypes = purchaseOrder.poTypes.concat(response.data.assetType);
              for(var i=0;i<purchaseOrder.fromStores.length;i++)
              {
                if(purchaseOrder.fromStores[i].id==$cookies.getObject('selectedTopStore').storeId)
                {
                  purchaseOrder.fromStore = purchaseOrder.fromStores[i];
                }
              }
  
              var poId = commonDetailService.getDataId();
              $rootScope.stopLoader();
              if (!!poId) {
                commonDetailService.setDataId(null);
                GetPO(poId);
              }
            },
            function (err) {
              $rootScope.stopLoader();
            });
        } catch (error) {
          alert(error);
        }
      }*/
      
      //start code by minesh
      var PopulateDropdown = function () {
          self.model.store = self.model.stores[0];
          self.model.grnType = self.model.assetCategories[0];
          self.model.poType = self.model.poTypes[0];
          var purchaseOrder = self.model;
          //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.DROPDOWN_API;
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
    
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.stores = self.model.stores.concat(response.data.store);
              self.model.assetCategories = self.model.assetCategories.concat(response.data.assetType);
              
              //purchaseOrder.stores = purchaseOrder.stores.concat(response.data.store);
              purchaseOrder.fromStores = purchaseOrder.fromStores.concat(response.data.store);
              purchaseOrder.poTypes = purchaseOrder.poTypes.concat(response.data.assetType);
              
              $rootScope.stopLoader();
              var poId = commonDetailService.getDataId();
              if (!!poId) {
                  commonDetailService.setDataId(null);
                  GetPO(poId);
                }

            },
            function (err) {
              $rootScope.stopLoader();
            });
        }
      
      var ValidateSearchItems = function () {
          var isValidItem = true;
          if (!self.model.poType || !self.model.poType.id) {
                isValidItem = false;
                growl.error('Please select Type');
              }
          if (!self.model.vendorName || !self.model.vendorId) {
              growl.error('Please select Vendor');
              isValidItem = false;
              self.model.isVendorValid = true;
            }
          
          if (!self.model.store || !self.model.store.id) {
        	  isValidItem = false;
        	  growl.error('Please select Store');
          	}
    
          return isValidItem;
        }

	 var GetConsignmentGRNList = function() {
			ClearId();
			
			if(ValidateSearchItems()) {
			
			self.model.grnType = self.model.poType ? self.model.poType : undefined
			
			var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.GET_CONSIGNMENT_GRN;

			var data = {
				"fromDate" : moment(self.model.fromDate).format('YYYY-MM-DD'),
				"toDate" : moment(self.model.toDate).format('YYYY-MM-DD'),
				"storeId" : !!self.model.store && !!self.model.store.id ? self.model.store.id
						: undefined,
				"assetTypeId" : self.model.grnType ? self.model.grnType.id
						: undefined,
				"vendorName" : self.model.vendorNameGRN ? self.model.vendorNameGRN
						: undefined,
				"vendorId" : self.model.vendorIdGRN ? self.model.vendorIdGRN
						: undefined,
				"grnNo" : self.model.grnNo ? self.model.grnNo : undefined,
				"unitId":$scope.unitId		
			}

			$rootScope.startLoaderPopup();
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
								$rootScope.stopLoaderPopup();
								self.model.grnList = response.data;
								self.model.grnListItems = [];
								self.model.taxList = self.model.taxListSelection.concat(response.data[0].taxList);
								self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data[0].otherChargesList);
								self.model.grnList.forEach(function(grn) {
											grn.isDisabled = true;
											grn.items.forEach(function(item) {
														item.poId=grn.id;
														item.isDisabled = true;
														item.vendorName = grn.vendorName;
														item.grnNumber = grn.grnNumber;
														item.grnDate = grn.grnDate;
														
														item.grnDetailId = item.goodReceiptNoteDetailId;
														
														item.costOfPurchase=item.cop;
														item.POAmountMRP=item.mrp;
														item.discount = item.discount ? item.discount : 0;
														item.discountPercent = item.discountPercent ? item.discountPercent : 0;														
														item.discountPercent = item.discountPercent.toFixed(2);
														item.discountAmount =  item.discountAmount ? item.discountAmount : (item.POAmountMRP) * (item.discountPercent / 100);
														item.discountAmount =  item.discountAmount.toFixed(2);
											            item.taxPercentage = item.taxPercentage ? item.taxPercentage : 0;
											            item.totalDiscount = item.totalDiscount ? item.totalDiscount : 0;
														item.selectedTax = self.model.taxList.find(function (val) {
											                return item.taxId == val.taxId;
											              });
														item.selectedTaxPercent = item.selectedTax.taxPercent.toFixed(2);
														
														item['poQuantity'] = 0;
														item['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
																												
														var uomList = null;
														if (self.model.assetTypeId == 1) {
															uomList = item.uom.find(function(uom) {
																		return item.grnUomTypeId == uom.uomTypeId;
																	});
															}

														CalculateStock(item,item.uom,uomList);
														self.model.grnListItems.push(item);
														
														});
											});
								$rootScope.stopLoader();
							}, function(err) {
								$rootScope.stopLoaderPopup();
								growl.error(err.data.message);
							});
		      }//end ValidateSearchItems
		}
		      var CalculateStock = function(item, uom, uomList) {
					if (!!uomList) {
						var prQuantity = item.prQuantity;
						var maxOrderQuantity = item.maxOrderQuantity || 0;
						var maxStoreQuantity = item.maxStoreQuantity || 0;

						uomList.uomDetails = uomList.uomDetails.split(' ')[0];
						if (uomList.uomDetails == "P") {
							item.newMaxOrderQuantity = parseInt(maxOrderQuantity
									/ (uom[0].conversion * uom[1].conversion * uom[2].conversion));
							item.newMaxStoreQuantity = parseInt(maxStoreQuantity
									/ (uom[0].conversion * uom[1].conversion * uom[2].conversion));
						} else if (uomList.uomDetails == "S") {
							item.newMaxOrderQuantity = parseInt(maxOrderQuantity
									/ (uom[1].conversion * uom[2].conversion));
							item.newMaxStoreQuantity = parseInt(maxStoreQuantity
									/ (uom[1].conversion * uom[2].conversion));
						} else if (uomList.uomDetails == "D") {
							item.newMaxOrderQuantity = parseInt(maxOrderQuantity
									/ (uom[2].conversion));
							item.newMaxStoreQuantity = parseInt(maxStoreQuantity
									/ (uom[2].conversion));
						}
					} else {
						item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
						item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
					}
				}
		      
		      
		      var SelectItemForGRN = function(item) {
					var itemIds = self.model.SelectedOrderIds;
					if (item.isSelected) {
						item.isDisabled = false;
						itemIds.push(item.id);
						self.model.setSelectedItems.push(item);
					} else {
						item.isDisabled = true;
						var index = itemIds.indexOf(item.id);
						if (index > -1) {
							itemIds.splice(index, 1);
							var indexOfRemovItem = self.model.setSelectedItems.findIndex(function(element) {
										return element.id === item.id;
									})
							if (indexOfRemovItem !== -1) {
								self.model.setSelectedItems.splice(indexOfRemovItem, 1)
							}
						}
					}
					if (self.model.grnListItems.every(isSelectedForGrnItem)) {
						self.model.selectAllRows = true;
						self.model.selectAllGRNRows = true;
					} else {
						self.model.selectAllRows = false;
						self.model.selectAllGRNRows = false;
					}
					
					//start code for isSelectedAllItems for particular grn
					self.model.grnList.forEach(function(grn) {
						if (grn.items.every(isSelectedForGrnItem)){
							grn.isSelectedAllItems = true;
						}else{
							grn.isSelectedAllItems = false;
						}
					});
					//end code for isSelectedAllItems for particular grn
					
					// for grn checkbox releated code start
					// for grn check box
					self.model.grnList.forEach(function(grn) {
						var anyOneSelected = false;
						var grnIds = self.model.SelectedGRNOrderIds;
						grn.items.forEach(function(item) {
							if (item.isSelected) {
								anyOneSelected = true;
							}
						});
						if (anyOneSelected) {
							grn.isSelected = true;
							grn.isDisable = false;
							var index = grnIds.indexOf(grn.id);
							if (index < 0) {
								grnIds.push(grn.id);
								self.model.setSelectedGRN.push(grn);
							}
						} else {
							grn.isSelected = false;
							grn.isDisable = true;

							var index = grnIds.indexOf(grn.id);
							if (index > -1) {
								grnIds.splice(index, 1);
								var indexOfRemovItem = self.model.setSelectedGRN.findIndex(function(element) {
											return element.id === grn.id;
										})
								if (indexOfRemovItem !== -1) {
									self.model.setSelectedGRN.splice(indexOfRemovItem,1)
								}
							}
						}
					});

					// for grn checkbox releated code end
				}

				var isSelectedForGrnItem = function(item) {
					if (!isDisableForGrnItem(item))
						return item.isSelected;
					else
						return true;
				}

				var isDisableForGrnItem = function(item) {
					if (item.status != 'New' && item.isSelected) {
						return true;
					}
					return false;
				}
				
				
				var SelectAllGRNItems = function() {
					if (self.model.selectAllRows) {
						self.model.selectAllGRNRows = self.model.selectAllRows;
						self.model.grnListItems.forEach(function(item) {
							if (!isDisableForGrnItem(item)) {
								item.isSelected = true;
								item.isDisabled = false;
								self.model.SelectedOrderIds.push(item.id);
								self.model.setSelectedItems.push(item);

							}
						});

						self.model.grnList.forEach(function(grn) {
							if (!isDisableForGrnItem(grn)) {
								grn.isSelected = true;
								grn.isSelectedAllItems = true;
								grn.isDisable = false;
								self.model.SelectedGRNOrderIds.push(grn.id);
								self.model.setSelectedGRN.push(grn);
							}
						});

					} else {
						self.model.selectAllGRNRows = self.model.selectAllRows;
						self.model.SelectedOrderIds = [];
						self.model.SelectedGRNOrderIds = [];
						// self.model.selectedItems = [];
						self.model.setSelectedItems = [];
						self.model.setSelectedGRN = [];
						self.model.grnListItems.forEach(function(item) {
							item.isSelected = false;
							item.isDisabled = true;
						});
						self.model.grnList.forEach(function(grn) {
							grn.isSelected = false;
							grn.isSelectedAllItems = false;
							grn.isDisable = true;
						});
					}
					
				}

				// start grn check box functionality
				var SelectGRN = function(grn) {
					var itemIds = self.model.SelectedOrderIds;
					var grnIds = self.model.SelectedGRNOrderIds;
					grn.isSelectedAllItems=grn.isSelected;
					if (grn.isSelected) {
						grn.isDisabled = false;
						grn.items.forEach(function(item) {
							if (!isDisableForGrnItem(item)) {
								item.isSelected = true;
								item.isDisabled = false;
								self.model.SelectedOrderIds.push(item.id);
								self.model.setSelectedItems.push(item);
							}
						});

						self.model.SelectedGRNOrderIds.push(grn.id);
						self.model.setSelectedGRN.push(grn);

					} else {
						grn.isDisabled = true;
						grn.items.forEach(function(item) {
							var index = itemIds.indexOf(item.id);
							if (index > -1) {
								itemIds.splice(index, 1);
								var indexOfRemovItem = self.model.setSelectedItems
										.findIndex(function(element) {
											return element.id === item.id;
										})
								if (indexOfRemovItem !== -1) {
									self.model.setSelectedItems.splice(
											indexOfRemovItem, 1)
								}
							}
							item.isSelected = false;
							item.isDisabled = true;
						});

						var grnIndex = grnIds.indexOf(grn.id);
						if (grnIndex > -1) {
							grnIds.splice(grnIndex, 1);
							var indexOfRemovItem = self.model.setSelectedGRN
									.findIndex(function(element) {
										return element.id === grn.id;
									})
							if (indexOfRemovItem !== -1) {
								self.model.setSelectedGRN.splice(indexOfRemovItem, 1)
							}
						}

					}

					if (self.model.grnListItems.every(isSelectedForGrnItem)) {
						self.model.selectAllRows = true;
						self.model.selectAllGRNRows = true;
					} else {
						self.model.selectAllRows = false;
						self.model.selectAllGRNRows = false;
					}
				}
				var SelectAllGRN = function() {
					self.model.selectAllRows = self.model.selectAllGRNRows;
					SelectAllGRNItems();

				}
				// end grn check box functionality

				var SetSelectedItem = function() {
					self.model.setSelectedItems.forEach(function(item) {
						let isFoundItem = self.model.itemDetails.find(function (x) {
			                  return x.goodReceiptNoteDetailId == item.goodReceiptNoteDetailId;
			                });
						
						if (item.isSelected && !isFoundItem) {
							item.isSelected = false;
							item.isDisabled= true;
							self.model.selectedItems.push(item);
							//added for po Creation
							item.poQuantity = 0;
							item.stageQuantityList= [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
							item.otherChargesTotal = 0;
							item.discountAmount = 0.00;
							item.selectedOtherCharges= [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
							//added for po Creation							
				            self.model.itemDetails.push(item);
						}
					});
					self.model.setSelectedGRN.forEach(function(grn) {
						let isFoundGRN = self.model.selectedGRN.find(function (x) {
			                  return x.id == grn.id;
			                });
						if (grn.isSelected && !isFoundGRN) {
							grn.isSelected = false;
							self.model.selectedGRN.push(grn);
						}
					});
				}
				function ClearId() {
					self.model.vendorIdGRN = (self.model.vendorNameGRN == undefined ? undefined
							: self.model.vendorId);
					self.model.vendorIdGRN = (self.model.vendorNameGRN == "" ? undefined
							: self.model.vendorId);
				}
				var ClearGrnModal = function() {
					self.model.grnList = null;
					self.model.grnListItems = null;
					self.model.grnNo = null;
					self.model.SelectedOrderIds = [];
					self.model.SelectedGRNOrderIds = [];
					self.model.setSelectedItems = [];
					self.model.setSelectedGRN = [];
					self.model.selectAllRows = false;
					self.model.selectAllGRNRows = false;

				}
      //end code by minesh
  
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
          "detail": "Vendor not found with name " + search
        };
        self.model.vendorNames = [item];
        return self.model.vendorNames;
      }
  
      function AddVendorName(vendor) {
        self.model.vendorName = vendor.vendorName;
        self.model.vendorId = vendor.id;
        $scope.vendorName =angular.copy(vendor.vendorName);
      }
      $scope.vName="";
      $scope.addVendorName1 = function(vendor){
           self.model.vendorName = vendor;
           self.model.vendorId = vendor.id;
      }
     
      function SearchProviderNames(search) {
        if (search.length < 1) return;
  
        if ((!self.model.prevSearchProvider || self.model.prevSearchProvider != search) && (search.length == 1)) {
          self.model.prevSearchProvider = search;
          var URI = CONSTANTS.GLOBAL.SERVICE_PROVIDER + $scope.organizationId + '/' + search;
          $rootScope.startLoader();
          return GenericService.serviceAction("GET", URI, {})
            .then(function (response) {
              if (!!response.data && response.data.length > 0) {
                self.model.serviceProviderNames = response.data;
                $rootScope.stopLoader();
                return $filter('filter')(self.model.serviceProviderNames, {
                  $: search
                });
              } else {
                $rootScope.stopLoader();
                return providerNotFound(search);
              }
            }, function (err) {
              $rootScope.stopLoader();
              return providerNotFound(search);
            });
        } else {
          if (!!self.model.serviceProviderNames && self.model.serviceProviderNames.length > 0 && self.model.serviceProviderNames[0].itemFound != undefined && !self.model.serviceProviderNames[0].itemFound) {
            return providerNotFound(search);
          } else {
            return $filter('filter')(self.model.serviceProviderNames, {
              $: search
            });
          }
        }
      }
  
      function providerNotFound(search) {
        var item = {
          "itemFound": false,
          "detail": "Service provider not found with name " + search
        };
        self.model.serviceProviderNames = [item];
        return self.model.serviceProviderNames;
      }
  
      function AddProviderName(provider, item) {
        item.serviceProvider = provider.vendorName;
        item.providerId = provider.id;
      }
  
      var GetGRNPopup = function () {    
        if(ValidateSearchItems()){
          var popup = angular.element('#getGRNModal');
          popup.modal('show');
          ClearGrnModal();
        }
      }
  
      var GetPO = function (poId) {
        //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API + poId;
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.DETAILS_API+ $scope.unitId + '/'+$scope.organizationId + '/'+poId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.discountTypeFlag = false;
            self.model.isDiscountAppliedValid = false;
            self.model.isAppliedDiscountValid = false;
            self.model.isDiscountApplied = true;
            $scope.consignmentPO=response.data;
            $scope.isFromEdit=true;
            self.model.store = self.model.stores.find(function (store) {
              return response.data.storeId == store.id;
            });
            self.model.fromStore = self.model.fromStores.find(function (item) {
              return response.data.requestingStoreId == item.id;
            });
            self.model.poType = self.model.poTypes.find(function (potype) {
              return response.data.assetTypeId == potype.id;
            });
  
            angular.forEach(response.data.items, function (item) {
              item.warrantyDate = new Date(item.warrantyDate);
            });
  
            self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
            self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
  
            self.model.poId = response.data.id;
            self.model.PRType = response.data.poTypeId;
            self.model.prRequestTypeId = response.data.poTypeId;
            self.model.vendorId = response.data.vendorId;
            self.model.vendorName = response.data.vendorName;
            self.model.purchaseOrderRemark = response.data.remark;
            self.model.isConsignment = response.data.isConsignment;
            self.model.discountType = response.data.discountTypeId;
            if (!!parseFloat(response.data.discount)) {
              self.model.finalDiscount = response.data.discount.toFixed(2);
            }
            self.model.totalNetAmount = response.data.netAmount;
            self.model.roundOffAmount = response.data.roundOffAmount;
            self.model.totalAmount = response.data.totalAmount;
            self.model.totalDiscount = response.data.totalDiscount;
            self.model.totalOtherCharges = response.data.totalOtherAmount;
            self.model.totalTax = response.data.totalTaxAmount;
            self.model.paymentMode = response.data.paymentTerm;
            self.model.itemDetails = [];
  
            angular.forEach(response.data.items, function (item) {
              var poUom = [];
              angular.forEach(item.poUom, function (uom) {
                poUom.push({
                  id: uom.id,
                  uomUnitId: { uomUnit: uom.uomUnit, uomUnitId: uom.uomUnitId },
                  conversion: parseInt(uom.conversion),
                  ipUom: uom.ipUom,
                  opUom: uom.opUom,
                  storeUom: uom.storeUom,
                  uomTypeId: uom.uomTypeId
                });
              });
  
              var bonusUom = [];
              angular.forEach(item.bonusUom, function (uom) {
                bonusUom.push({
                  id: uom.id,
                  uomUnitId: { uomUnit: uom.uomUnit, uomUnitId: uom.uomUnitId },
                  conversion: parseInt(uom.conversion),
                  ipUom: uom.ipUom,
                  opUom: uom.opUom,
                  storeUom: uom.storeUom,
                  uomTypeId: uom.uomTypeId
                });
              });
  
              var prDetailsList = [];
              angular.forEach(item.prId, function (prData) {
                prDetailsList.push({
                  "prNo": prData.prNo,
                  "prQuantity": prData.approvedQuantity
                });
              });
  
              item.discount = item.discount ? item.discount : 0;
              item.taxPercentage = item.taxPercentage ? item.taxPercentage : 0;
              item.totalDiscount = item.totalDiscount ? item.totalDiscount : 0;
  
              var poItem = {
                "id": item.id,
                "itemId": item.itemId,
                "itemCode": item.itemCode,
                "itemName": item.itemName,
                "poQuantity": item.poQuantity,
                "bonusQuantity": item.bonusQuantity,
                "costOfPurchase": item.cop.toFixed(2),
                "POAmountMRP": item.mrp.toFixed(2),
                "discountPercent": item.discount.toFixed(2),
                "selectedTaxPercent": item.taxPercentage.toFixed(2),
                // "taxName": item.taxCode,
                // "taxPercent": item.taxPercentage.toFixed(2),
                "poAmount": item.totalAmount,
                "taxAmount": item.taxAmount,
                "manufacturerId": item.manufacturerId,
                "manufacturerName": item.manufacturerName,
                "netAmount": item.netAmount,
                "discountAmount": item.totalDiscount.toFixed(2),
                "otherChargesTotal": item.totalOtherAmount,
                "specification": item.specification,
                "remark": item.remark,
                "stageQuantityList": item.purchaseOrderStagedDto,
                "prNumbersList": prDetailsList,
                "warrantyDate": item.warrantyDate,
                "technicalSpecs": item.technicalSpecification,
                "warrantyPeriod": item.warrentyPeriod,
                "supportTerms": item.supportTerms,
                "paymentTerms": item.expectedPayment,
                "childAccessoryItems": item.poAccessoriesMapper,
                "serviceProvider": item.serviceProvider,
                "serviceProviderId": item.serviceProviderId,
                "providerId": item.serviceProviderId,
                "totalPrQty": item.prQuantity,
                "prMaxStoreQty": item.poQuantity,
                "poAccessoriesMapper": {
                  "itemIds": []
                },
                
                "grnId":item.grnId,
                "grnDetailId":item.grnDetailId,
                "grnNumber":item.grnNumber,
                "grnDate":item.grnDate,
                "batchNo":item.batchNo,
                "batchId":item.batchId,
                "lotNo":item.lotNo,
                "totalConsignmentStock":item.totalConsignmentStock,
                "consumedQty":item.consumedQty,
                "convertedConsignmentStock":item.convertedConsignmentStock,
                "availableConsignmentStock":item.availableConsignmentStock,
                "returnedConsignmentStock":item.returnedConsignmentStock,
                "storeStockTransfered":item.storeStockTransfered,
                "poApprovedQuantity" :item.poApprovedQuantity-item.poQuantity
              }
              poItem.poId=response.data.id;
              poItem.selectedTax = self.model.taxList.find(function (val) {
                return item.taxId == val.taxId;
              });
  
              if (!response.data.otherCharges.length) {
                poItem.selectedOtherCharges = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
              } else {
                poItem.selectedOtherCharges = response.data.otherCharges;
              }
  
              poItem.maxOrderQuantity = item.maxOrderQuantity;
              poItem.maxStoreQuantity = item.maxStoreQuantity;
  
              if (self.model.poType.id == 1) {
                poItem.uom = self.model.uomArray.concat(item.uom);
                poItem.selectedUom = item.uom.find(function (uom) {
                  return item.poUomTypeId == uom.uomTypeId;
                });
                poItem.bonusSelectedUom = item.uom.find(function (uom) {
                  return item.bonusUomTypeId == uom.uomTypeId;
                });
                poItem.poUom = poUom;
                poItem.bonusUom = bonusUom;
  
                self.model.CalculateStock(poItem, poItem.uom, poItem.selectedUom);
              }
              else if (self.model.poType.id == 2) {
                poItem.newMaxOrderQuantity = item.maxOrderQuantity;
                poItem.newMaxStoreQuantity = item.maxStoreQuantity;
                // poItem.selectedTaxPercent = item.taxPercentage.toFixed(2);
              }
  
              angular.forEach(response.data.items, function(data){
                angular.forEach(data.qrDateDto, function(qrItem){
                  var qrNo = qrItem.qrno;
                  var qrId1 = qrItem.id;
                  var qrDate = qrItem.qrdate;
                  var qrObj = { id: qrId1, qrNo: qrNo, qrDate: qrDate };
                  poItem.prNumbersList.push(qrObj);
                });
              });
              
              self.model.itemDetails.push(poItem);
            });
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
  
      var ValidatePOQuantity = function (item, poQuantity) {
        var isValidItem = true;
        var poQty = parseInt(poQuantity);
        var newMaxOrderQuantity = item.newMaxOrderQuantity + item.prMaxStoreQty;
        if (newMaxOrderQuantity > item.newMaxStoreQuantity) {
          newMaxOrderQuantity = item.newMaxStoreQuantity;
        }
  
        if (poQty > newMaxOrderQuantity) {
          growl.error('PO quantity should be less than pending quantity');
          item.isPOQuantityValid = true;
          isValidItem = false;
        }
  
        if (!poQty) {
          growl.error('Please enter valid PO quantity');
          item.isPOQuantityValid = true;
          isValidItem = false;
        }
  
        return isValidItem;
      }
  
      var ValidateDetails = function () {
        var isValid = true;
  
        if (!self.model.store.id) {
          self.model.isValidStore = true;
          isValid = false;
        }
  
        if (!self.model.poType.id) {
          self.model.isValidPOType = true;
          isValid = false;
        }
  
        if (!self.model.PRType) {
          self.model.isPRTypeValid = true;
          isValid = false;
        }
  
        if (!self.model.vendorId || !self.model.vendorName) {
          self.model.isVendorValid = true;
          isValid = false;
        }
  
        if (!self.model.store.id || !self.model.poType.id || !self.model.PRType || !self.model.vendorId || !self.model.vendorName) {
          growl.error('Please select required fields');
          isValid = false;
        }
  
        return isValid;
      }
  
      var ValidateItems = function () {
        var isValidItem = true;
        
        
        if (!self.model.poType || !self.model.poType.id) {
              isValidItem = false;
              growl.error('Please select Type');
            }
        if (!self.model.store || !self.model.store.id) {
        	isValidItem = false;
        	growl.error('Please select GRN Store');
        }
        
        if (self.model.finalDiscount) {
          if (!self.model.isDiscountApplied) {
            isValidItem = false;
            self.model.isDiscountAppliedValid = true;
            growl.error('Please apply discount');
          }
  
          if (self.model.discountTypeFlag == true) {
            isValidItem = false;
            self.model.isDiscountAppliedValid = true;
            growl.error('Please apply discount');
          }
  
          if (self.model.isAppliedDiscountValid == true) {
            isValidItem = false;
            growl.error('Please apply discount');
          }
  
          if (self.model.discountType == 1) {
            if (parseFloat(self.model.finalDiscount) > 50) {
              isValidItem = false;
              growl.error('Discount must be less than 50% of total amount');
            }
          } else if (self.model.discountType == 2) {
            if (parseFloat(self.model.finalDiscount) > (parseFloat(self.model.totalAmount) / 2)) {
              isValidItem = false;
//              growl.error('Please enter discount amount less than half of total amount');HIS-19131
                growl.error('Please enter Discount amount should be less than or equals to total amount.');
            }
          }
        }
  
        if (self.model.itemDetails.length <= 0) {
          growl.error('Please add items to save purchase order');
          isValidItem = false;
        } else {
          angular.forEach(self.model.itemDetails, function (item, index) {
            if (!item.costOfPurchase) {
              self.model.costOfPurchase[index] = true;
              growl.error("Please enter valid COP");
              isValidItem = false;
            }
  
            if (!item.poQuantity) {
              item.isPOQuantityValid = true;
              growl.error('Please enter valid PO quantity');
              isValidItem = false;
            }
  
            if (item.discountPercent > 100) {
              self.model.discountPercent[index] = true;
              growl.error('Disount should be less than 100%');
              isValidItem = false;
            }
  
            if (item.discountAmount > (parseFloat(item.poAmount))) {
              self.model.discountAmount[index] = true;
              growl.error('Discount amount should be less than or equals to total amount.');
              isValidItem = false;
            }
  
            if (parseFloat(item.POAmountMRP) < parseFloat(item.costOfPurchase) || !item.POAmountMRP) {
              self.model.POAmountMRP[index] = true;
              growl.error('MRP should be greater than COP');
              isValidItem = false;
            }
            if (!item.selectedTax.taxId) {
              growl.error('Please select valid tax code');
              self.model.taxCodeValid[index] = true;
              isValidItem = false;
            }
  
            if (!item.selectedTaxPercent) {
              growl.error('Please enter valid tax %');
              self.model.taxPercent[index] = true;
              isValidItem = false;
            }
            if (self.model.poType.id == 2) {
//               if (!item.serviceProvider || !item.providerId) {
//                 growl.error('Please enter valid service provider');
//                 self.model.serviceProviderValid[index] = true;
//                 isValidItem = false;
//               }
  
              if (!item.poQuantity) {
                item.isPOQuantityValid = true;
                growl.error('Please enter valid PO quantity');
                isValidItem = false;
              }
  
//               if (!item.warrantyDate) {
//                 item.warrantyDateValid = true;
//                 growl.error('Please select warranty date');
//                 isValidItem = false;
//               }
  
//               if (!item.warrantyPeriod) {
//                 item.isValidWarrantyTerm = true;
//                 growl.error('Please enter warranty period');
//                 isValidItem = false;
//               }
  
//               if (!item.supportTerms) {
//                 item.isValidSupportTerm = true;
//                 growl.error('Please enter expected support terms');
//                 isValidItem = false;
//               }
  
//               if (!item.technicalSpecs) {
//                 item.isValidTechSpec = true;
//                 growl.error('Please enter expected technical specifications');
//                 isValidItem = false;
//               }
  
//               if (!item.paymentTerms) {
//                 item.isValidPaymentTerm = true;
//                 growl.error('Please enter expected payment terms');
//                 isValidItem = false;
//               }
  
  
            } else if (self.model.poType.id == 1) {
              /*  if (!item.taxPercent) {
                 growl.error('Please enter valid tax %');
                 self.model.taxPercent[index] = true;
                 isValidItem = false;
               } 
  
              if (!item.selectedUom.uomTypeId) {
                growl.error('Please select valid UOM');
                item.valid_p_uom = true;
                isValidItem = false;
              }
              if (item.bonusQuantity) {
                if (!item.bonusSelectedUom.uomTypeId) {
                  growl.error('Please select valid Bonus UOM');
                  item.valid_b_uom = true;
                  isValidItem = false;
                }
              }
              */
            }
             
              // if (item.poQuantity > item.pendingPOQuantity) {
            	//   item.isPOQuantityValid = true;
            	//   growl.error('po quantity should be less than or equal pending quantity');
            	//   isValidItem = false;
              // }
          });
  
          if (!self.model.vendorName || !self.model.vendorId) {
            growl.error('Please select required fields');
            isValidItem = false;
            self.model.isVendorValid = true;
          }
        }
  
        return isValidItem;
      }
  
      var ApplyDiscountValidation = function () {
        self.model.discountTypeFlag = false;
      }
  
      var AddItemDiscount = function (type, item, value, index) {
        if (type == 'percent') {
          item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
          if (isNaN(item.discountPercent) || !item.discountPercent || item.discountPercent == 0) {
            item.discountPercent = 0;
            item.discountAmount = 0;
          } else {
            if (parseFloat(item.discountPercent) > 100) {
              self.model.discountPercent[index] = true;
              growl.error('Discount must be less than 100% of total amount');
            } else {
              if (!item.poAmount) {
                item.discountAmount = 0;
              } else {
                item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
                item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
              }
            }
          }
  
        } else if (type == 'amount') {
          item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
          if (isNaN(item.discountAmount) || !item.discountAmount || item.discountAmount == 0) {
            item.discountAmount = 0;
            item.discountPercent = 0;
          } else {
            if (parseFloat(item.discountAmount) > (parseFloat(item.poAmount))) {
              self.model.discountAmount[index] = true;
              growl.error('Discount amount should be less than or equals to total amount.');
            } else {
              if (!item.poAmount) {
                item.discountPercent = 0;
              } else {
                item.discountPercent = ((parseFloat(item.discountAmount) / item.poAmount) * 100);
                item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
              }
            }
          }
        }
      }
  
      var ApplyDiscount = function (items) {
        var discount = parseFloat(self.model.finalDiscount);
        if (!isNaN(discount)) {
          if (self.model.discountType == '2') {
            if (discount <= (parseFloat(self.model.totalAmount))) {
              var discountPercent = discount / self.model.totalAmount;
              angular.forEach(items, function (value, key) {
                if (value.poAmount > discountPercent) {
                  value.discountAmount = value.poAmount * discountPercent;
                  value.discountPercent = ((parseFloat(value.discountAmount) / value.poAmount) * 100);
                  value.discountAmount = value.discountAmount.toFixed(2);
                  value.discountPercent = value.discountPercent.toFixed(2);
                  self.model.discountPercent[key] = false;
                  self.model.discountAmount[key] = false;
                } else {
                  value.discountAmount = 0;
                }
              });
            } else {
//              growl.error('Please enter discount amount less than half of total amount');HIS-19131
            	growl.error('Please enter Discount amount should be less than or equals to total amount.');
              return false;
            }
          } else if (self.model.discountType == '1') {
            if (discount <= 100) {
              angular.forEach(items, function (value, key) {
                value.discountAmount = value.poAmount * discount * 0.01;
                value.discountPercent = ((parseFloat(value.discountAmount) / value.poAmount) * 100);
                value.discountAmount = value.discountAmount.toFixed(2);
                value.discountPercent = value.discountPercent.toFixed(2);
                self.model.discountPercent[key] = false;
                self.model.discountAmount[key] = false;
              });
            } else {
              growl.error('Discount must be less than 100% of total amount');
              return false;
            }
          }
        }
      }
  
      var ValidateConsumableAmount = function (item, index) {
        var isValidItem = true;
        if (parseFloat(item.POAmountMRP) < parseFloat(item.costOfPurchase)) {
          growl.error('MRP should be greater than COP');
          self.model.costOfPurchase[index] = true;
          self.model.POAmountMRP[index] = true;
          isValidItem = false;
        } else {
          self.model.costOfPurchase[index] = false;
          self.model.POAmountMRP[index] = false;
        }
        return isValidItem;
      }
  
      var SetDecimalOtherCharge = function (item) {
        if (isNaN(item.amount) || !item.amount || item.amount == 0) {
          item.amount = 0;
        } else {
          item.amount = parseFloat(item.amount).toFixed(2);
        }
      }
  
      var SetDecimal = function (name, item, value) {
        if (name == "costOfPurchase") {
          if (isNaN(item.costOfPurchase) || !item.costOfPurchase || item.costOfPurchase == 0) {
            item.costOfPurchase = 0;
          } else {
            item.costOfPurchase = parseFloat(item.costOfPurchase).toFixed(2);
          }
        }
  
        if (name == "POAmountMRP") {
          if (isNaN(item.POAmountMRP) || !item.POAmountMRP || item.POAmountMRP == 0) {
            item.POAmountMRP = 0;
          } else {
            item.POAmountMRP = parseFloat(item.POAmountMRP).toFixed(2);
          }
        }
  
        if (name == "taxPercent") {
          if (isNaN(item.taxPercent) || !item.taxPercent ) {
            item.taxPercent = 0;
          } else {
            item.taxPercent = parseFloat(item.taxPercent).toFixed(2);
          }
        }
  
        if (name == "taxPercentFixed") {
          if (isNaN(item.selectedTaxPercent) || !item.selectedTaxPercent ) {
            item.selectedTaxPercent = 0;
          } else {
            item.selectedTaxPercent = parseFloat(item.selectedTaxPercent).toFixed(2);
          }
        }
      }
  
      var SetDecimalDiscount = function () {
        if (isNaN(self.model.finalDiscount) || !self.model.finalDiscount || self.model.finalDiscount == 0) {
          self.model.finalDiscount = 0;
        } else {
          self.model.finalDiscount = parseFloat(self.model.finalDiscount).toFixed(2);
        }
      }
  
      var PopulateAssetCategoryDropdown = function () {
        var purchaseOrder = self.model;
  
        var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.poType.id;
  
        purchaseOrder.assetCategory = purchaseOrder.assetCategories[0];
        purchaseOrder.productCategory = purchaseOrder.productCategories[0];
  
        purchaseOrder.assetCategories.splice(1);
        purchaseOrder.productCategories.splice(1);
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            purchaseOrder.assetCategories = purchaseOrder.assetCategories.concat(response.data);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
  
      var PopulateProductCategory = function (assetCategory) {
        var purchaseOrder = self.model;
  
        var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id + '/'+$scope.organizationId;
  
        purchaseOrder.productCategory = purchaseOrder.productCategories[0];
        purchaseOrder.productCategories.splice(1);
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            purchaseOrder.productCategories = purchaseOrder.productCategories.concat(response.data);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
  
      var ValidateDate = function () {
        var isValid = true;
        if (self.model.fromDate > self.model.toDate) {
          growl.error('From date should be lesser than to date');
          isValid = false;
        }
  
        return isValid;
      }
  
      var GetIndentList = function () {
        if (ValidateDate()) {
          var URI = CONSTANTS.GLOBAL.AGAINST_PR_SEARCH;
  
          var data = {
            // "isConsignment": self.model.isConsignment,
            "storeId": self.model.store.id,
            "assetTypeId": self.model.poType.id,
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "vendorId": self.model.vendorId,
            "prNo": self.model.prNumber,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId
          }
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.indents = response.data;
              self.model.indentItemDetails = [];
              self.model.intendItemIds = [];
              angular.forEach(self.model.indents, function (pr) {
                pr["isChecked"] = false;
                angular.forEach(pr.items, function (item) {
                  item["isChecked"] = false;
                });
              });
  
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              self.model.indents = [];
              growl.error(err.data.message);
            });
        }
      }
  
      var Expand = function (item) {
        item.Expanded = !item.Expanded;
      }

      var GetItemList = function () {
        if(self.model.searchItem.itemName){
           if (self.model.searchItem.itemName!=undefined  &&  (self.model.searchItem.itemName.replace(/\s/g, "").length) == 2  ) {

            var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;;
      
            var data = {
              // "isConsignment": self.model.isConsignment,
              "assetTypeId": self.model.poType.id,
              "assetCategoryId": self.model.assetCategory.id,
              "productCategoryId": self.model.productCategory.id,
              "storeId": self.model.store.id,
              "requestingStoreId": self.model.fromStore.id,
              "itemCode": self.model.searchItem.itemCode,
              "itemName": self.model.searchItem.itemName
            }
      
            self.model.items = [];
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              
              self.model.items = response.data;
              if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchDataByField( self.model.items, self.model.pagination);
              }
              self.model.selectAllRows = false;
              // self.model.SelectedItemsIds = [];
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              growl.error(err.data.message);
            });
          }else{
                   if( self.model.searchItem.itemName !=undefined &&  (self.model.searchItem.itemName.replace(/\s/g, "").length) > 0){
                                                   var list = $filter('filter')(self.model.items, {
                                                                                    $: self.model.searchItem.itemName
                                                                                  });
                                                   PaginationService.searchDataByField( list, self.model.pagination);
                                               }
                   }
        }
        else if(self.model.searchItem.itemCode){
          if (self.model.searchItem.itemCode!=undefined  &&  (self.model.searchItem.itemCode.replace(/\s/g, "").length) == 2  ) {

            var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;;
      
            var data = {
              // "isConsignment": self.model.isConsignment,
              "assetTypeId": self.model.poType.id,
              "assetCategoryId": self.model.assetCategory.id,
              "productCategoryId": self.model.productCategory.id,
              "storeId": self.model.store.id,
              "requestingStoreId": self.model.fromStore.id,
              "itemCode": self.model.searchItem.itemCode,
              "itemName": self.model.searchItem.itemName
            }
      
            self.model.items = [];
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              
              self.model.items = response.data;
              if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchDataByField( self.model.items, self.model.pagination);
              }
              self.model.selectAllRows = false;
              // self.model.SelectedItemsIds = [];
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              growl.error(err.data.message);
            });
          }else{
           if( self.model.searchItem.itemCode !=undefined &&  ( self.model.searchItem.itemCode.replace(/\s/g, "").length) > 0){
                                                           var list = $filter('filter')(self.model.items, {
                                                                                            $: self.model.searchItem.itemCode
                                                                                          });
                                                           PaginationService.searchDataByField( list, self.model.pagination);
                                                       }

           }

          }

        else{
          self.model.items = [];
          self.model.pagination.data=[];
        }
      }
  
      var SelectTax = function (tax, item) {
        if (!!tax.taxId) {
          item.selectedTaxPercent = tax.taxPercent.toFixed(2);
        }
        else {
          item.selectedTaxPercent = "";
        }
      }
  
      var AddItems = function () {
        if (self.model.SelectedItemsIds.length == 0) {
          growl.error('Please select items to add');
          return false;
        } else {
          if (self.model.addItemFlag) {
            var URI = CONSTANTS.GLOBAL.POCreation_AddItems+ "/"+$scope.organizationId + "/"+$scope.unitId;
            var data = {
              "itemIds": self.model.SelectedItemsIds,
              "storeId": self.model.store.id
            };
  
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
              function (response) {
                self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
                if (!self.model.taxList) {
                  self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
                }
                response.data.itemDetailsList.forEach(function (value, key) {
                  value['poQuantity'] = 0;
                  value['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
                  value['otherChargesTotal'] = 0;
                  value['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
  
                  var isFound = self.model.itemDetails.find(function (x) {
                    return x.itemId == value.itemId;
                  });
  
                  if(value.maxOrderQuantity <= 0){
                    growl.error(" Maxorder Quantity for selected item 0 or less than 0.");
                  }
                  if (!isFound && (value.maxOrderQuantity > 0)) {
                    self.model.itemDetails.push(value);
                    value.prMaxStoreQty = 0;
                    value.newQty = value.stockAvailable;
                    value.poAccessoriesMapper = { "itemIds": [] }
                    value.childAccessoryItems = [];
  //                  if (!!value.taxPercent) {
  //                    value.selectedTaxPercent = value.taxPercent.toFixed(2);
  //                  }
                    value.selectedTax = self.model.taxList.find(function (val) {
                      return value.taxId == val.taxId;
                    });
                    if(value.selectedTax) {
                      value.selectedTaxPercent = value.selectedTax.taxPercent.toFixed(2);
                    }
                    if (self.model.poType.id == 1) {
                      selectDefaultUOM(value);
                      self.model.CalculateStock(value, value.uom, value.selectedUom);
  
                    } else if (self.model.poType.id == 2) {
                      value.newMaxOrderQuantity = value.maxOrderQuantity;
                      value.newMaxStoreQuantity = value.maxStoreQuantity;
                      /* value.selectedTax = self.model.taxList[0];
                      value.selectedTaxPercent = 0; */
                    }
                  }
                });
  
                var popup = angular.element('#addNewItemWasteStore');
                popup.modal('hide');
                self.model.SelectedItemsIds = [];
                self.model.items = [];
                $rootScope.stopLoaderPopup();
              },
              function (err) {
                $rootScope.stopLoaderPopup();
                self.model.SelectedItemsIds = [];
                growl.error(err.data.message);
              });
          } else {
            self.model.itemDetails.find(function (obj) {
              if (obj.itemId == self.model.selectedItemId) {
                self.model.accessoryItems.forEach(function (item) {
                  var isFound = self.model.addedAccessoryItems.find(function (value) {
                    return item.itemId == value.itemId;
                  });
                  if (!isFound) {
                    obj.childAccessoryItems.push(item);
                  }
                });
                obj.poAccessoriesMapper.itemIds = obj.poAccessoriesMapper.itemIds.concat(self.model.SelectedItemsIds);
              }
            });
  
            var popup = angular.element('#addNewItemWasteStore');
            popup.modal('hide');
            self.model.SelectedItemsIds = [];
            self.model.items = [];
          }
        }
      }
  
      var selectDefaultUOM = function (item) {
        item.uom = self.model.uomArray.concat(item.uom);
        item.selectedUom = item.uom[0];
        item.bonusSelectedUom = item.uom[0];
        var uomData = [];
        angular.forEach(item.uom, function (data) {
          uomData.push({
            "uomUnitId": {
              "uomUnit": data.uomUnit,
              "uomUnitId": data.uomUnitId
            },
            "conversion": data.conversion,
            "ipUom": data.ipUom,
            "opUom": data.opUom,
            "storeUom": data.storeUom,
            "uomTypeId": data.uomTypeId
          });
        });
        item.bonusUom = uomData;
        item.poUom = uomData;
      }
  
      var bonusUomInfoOnClick = function (batchItem, index) {
        self.model.bonusSelectedUOMList = [];
        self.model.bonusSelectedUOMUnit = [];
        self.model.bonusSelectedItemIndex = index;
        if (batchItem)
          var _boUom = batchItem.bonusUom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
  
        var b_uom = batchItem.uom.filter(function (uom) {
          if (!!uom.uomTypeId) {
            return uom;
          }
        });
  
        b_uom.forEach(function (item, index) {
          var data = { 'uomUnit': item.uomUnit, 'uomUnitId': item.uomUnitId };
          self.model.bonusSelectedUOMUnit.push(data);
  
          var uomUnitId = null, conversion = 0;
          if (!_boUom.length) {
            uomUnitId = data;
            conversion = item.conversion;
          }
          else {
            uomUnitId = _boUom[index].uomUnitId;
            conversion = _boUom[index].conversion;
          }
  
          self.model.bonusSelectedUOMList.push({
            uomType: item.uomType,
            uomUnitId: uomUnitId,
            conversion: conversion,
            ipUom: item.ipUom,
            opUom: item.opUom,
            storeUom: item.storeUom,
            uomTypeId: item.uomTypeId
          });
        });
      }
  
      var bonusUomModalOnClick = function () {
        self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusUom = [];
        var modal_list = self.model.bonusSelectedUOMList;
  
        if (ValidConversionFactor(self.model.bonusSelectedUOMList)) {
          if (modal_list) {
            modal_list.forEach(function (modal, index) {
              self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusUom.push({
                uomUnitId: modal.uomUnitId,
                conversion: parseInt(modal.conversion),
                ipUom: modal.ipUom,
                opUom: modal.opUom,
                storeUom: modal.storeUom,
                uomTypeId: modal.uomTypeId
              });
            });
            self.model.ClosePopup('Close', 'bouns_selectUOM_modal');
          }
        }
      }
  
      var ValidConversionFactor = function (data) {
        var isValid = true;
        data.find(function (obj) {
          if (!parseInt(obj.conversion)) {
            growl.error('Conversion factor should valid');
            isValid = false;
          }
        });
  
        return isValid;
      }
  
      var uomInfoOnClick = function (batchItem, index) {
        self.model.selectedUOMList = [];
        self.model.selectedUOMUnit = [];
        self.model.selectedItemIndex = index;
        if (batchItem)
          var _poUom = batchItem.poUom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
  
        var p_uom = batchItem.uom.filter(function (uom) {
          if (!!uom.uomTypeId) {
            return uom;
          }
        });
  
        p_uom.forEach(function (item, index) {
          var data = { 'uomUnit': item.uomUnit, 'uomUnitId': item.uomUnitId };
          self.model.selectedUOMUnit.push(data);
  
          var uomUnitId = null, conversion = 0;
          if (!_poUom.length) {
            uomUnitId = data;
            conversion = item.conversion;
          } else {
            uomUnitId = _poUom[index].uomUnitId;
            conversion = _poUom[index].conversion;
          }
  
          self.model.selectedUOMList.push({
            uomType: item.uomType,
            uomUnitId: uomUnitId,
            conversion: conversion,
            ipUom: item.ipUom,
            opUom: item.opUom,
            storeUom: item.storeUom,
            uomTypeId: item.uomTypeId
          });
        });
      }
  
      var uomModalOnClick = function () {
        self.model.itemDetails[self.model.selectedItemIndex].poUom = [];
        var modal_list = self.model.selectedUOMList;
  
        if (ValidConversionFactor(self.model.selectedUOMList)) {
          if (modal_list) {
            modal_list.forEach(function (modal, index) {
              self.model.itemDetails[self.model.selectedItemIndex].poUom.push({
                uomUnitId: modal.uomUnitId,
                conversion: parseInt(modal.conversion),
                ipUom: modal.ipUom,
                opUom: modal.opUom,
                storeUom: modal.storeUom,
                uomTypeId: modal.uomTypeId
              });
            });
            self.model.ClosePopup('Close', 'selectUOM_modal');
          }
        }
      }
  
      var isSelectedItem = function (item) {
        return item.isSelectedItem;
      }
  
      var SelectAllIndentItems = function (indent, items) {
        if (indent.isSelectedIndent) {
          items.map(function (x) {
            self.model.indentItemDetails.push(x);
            x.isSelectedItem = true;
            x.isChecked = true;
            indent.isChecked = true;
            return self.model.intendItemIds.push(x.itemId);
          });
        } else {
          self.model.intendItemIds = [];
          items.forEach(function (x) {
            x.isSelectedItem = false;
            x.isChecked = false;
          });
          indent.isChecked = false;
          items.map(function (x) {
            var idx = self.model.indentItemDetails.indexOf(x);
            if (idx > -1) {
              self.model.indentItemDetails.splice(idx, 1);
            }
          });
        }
      }
  
      var SelectIndentItem = function (item, indent) {
        var itemIds = self.model.intendItemIds;
        if (item.isSelectedItem) {
          item.isChecked = true;
          indent.isChecked = true;
          itemIds.push(item.itemId);
          self.model.indentItemDetails.push(item);
        } else {
          item.isChecked = false;
          indent.isChecked = false;
          var index = itemIds.indexOf(item.isSelectedItem);
          if (index > -1) {
            itemIds.splice(index, 1);
          }
  
          var itemIndex = self.model.indentItemDetails.indexOf(item);
          if (itemIndex > -1) {
            self.model.indentItemDetails.splice(itemIndex, 1);
          }
        }
  
        if (indent.items.every(isSelectedItem)) {
          indent.isSelectedIndent = true;
        } else {
          indent.isSelectedIndent = false;
        }
      }
  
      var updatePrItemDetails = function (prResponseData) {
        var newItemsHash = {};
        var itemDetailsHash = {};
        self.model.itemDetails.forEach(function (itemDetail) {
          itemDetailsHash[itemDetail.itemId] = itemDetail;
        });
  
        prResponseData.forEach(function (prItem) {
          var prNo = prItem.prNo;
          prItem.items.forEach(function (item) {          
            var itemId = item.itemId;
            var prId = item.id;
            var prQuantity = Number(item.pendingPOqty);
            var prObj = { id: prId, prNo: prNo, prQuantity: prQuantity };
  
            if (itemDetailsHash.hasOwnProperty(itemId)) {
              // item found in already saved list
              var itemDetail = itemDetailsHash[itemId];
              if (itemDetail.prIds && itemDetail.prIds.length) {
                // prIds list is present
                if (itemDetail.prIds.indexOf(prId) == -1) {
                  itemDetail.prNumbersList.push(prObj);
                  itemDetail.prIds.push(prId);
                } else {
                  //DO nothing, Item is already preset in saved table
                }
              } else {
                // prIds list not present
                itemDetail.prNumbersList = [prObj]
                itemDetail.prIds = [prId];
              }
  
            } else if (newItemsHash.hasOwnProperty(itemId)) {
              // item found in new item hash
              var savedItem = newItemsHash[itemId];
              savedItem.prNumbersList.push(prObj);
              savedItem.prIds.push(prId);
            } else {
              // item not found in item hash
              var clonedItem = JSON.parse(JSON.stringify(item));
              delete clonedItem.id;
              delete clonedItem.prQuantity;
              clonedItem.prNumbersList = [prObj]
              clonedItem.prIds = [prId];
              newItemsHash[itemId] = clonedItem;
              newItemsHash[itemId].poAccessoriesMapper = { "itemIds": [] }
              newItemsHash[itemId].childAccessoryItems = [];
            }
  
          });
        });
  
        for (var itemId in newItemsHash) {
  
          if (!!newItemsHash[itemId].taxPercent) {
            newItemsHash[itemId].selectedTaxPercent = newItemsHash[itemId].taxPercent.toFixed(2);
          }
          newItemsHash[itemId].selectedTax = self.model.taxList.find(function (val) {
            return newItemsHash[itemId].taxId == val.taxId;
          });
  
          newItemsHash[itemId]['poQuantity'] = self.model.PRType == 3 ? newItemsHash[itemId]["pendingPOqty"] : 0;
          newItemsHash[itemId]['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
          newItemsHash[itemId]['otherChargesTotal'] = 0;
          newItemsHash[itemId]['selectedOtherCharges'] = [{
            otherChargesId: self.model.otherChargesDropdownList[0].id,
            amount: 0
          }];
  
          var total = 0;
          angular.forEach(newItemsHash[itemId].prNumbersList, function (value) {
            total = total + parseFloat(value.prQuantity);
          });
          newItemsHash[itemId].totalPrQty = total;
  
          newItemsHash[itemId]['prList'] = [];
  
  
          self.model.itemDetails.push(newItemsHash[itemId]);
          newItemsHash[itemId].prMaxStoreQty = 0;
          if (self.model.poType.id == 1) {
            selectDefaultUOM(newItemsHash[itemId]);
  
            newItemsHash[itemId].selectedUom = newItemsHash[itemId].uom.find(function (uom) {
              return newItemsHash[itemId].uomTypeId == uom.uomTypeId;
            });
  
            newItemsHash[itemId].bonusSelectedUom = newItemsHash[itemId].uom.find(function (uom) {
              return newItemsHash[itemId].uomTypeId == uom.uomTypeId;
            });
  
            self.model.CalculateStock(newItemsHash[itemId], newItemsHash[itemId].uom, newItemsHash[itemId].selectedUom);
          } else if (self.model.poType.id == 2) {
            //   newItemsHash[itemId].selectedTax = self.model.taxList[0];
            newItemsHash[itemId].newMaxOrderQuantity = newItemsHash[itemId].maxOrderQuantity;
            newItemsHash[itemId].newMaxStoreQuantity = newItemsHash[itemId].maxStoreQuantity;
          }
        }
        // console.log(self.model.itemDetails, "self.model.itemDetails");
      }
  
      var CalculateStock = function (item, uom, uomList) {
        if (!!uomList) {
          var prQuantity = item.totalPrQty;
          var maxOrderQuantity = item.maxOrderQuantity || 0;
          var maxStoreQuantity = item.maxStoreQuantity || 0;
  
          var uom = uom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
  
          if (!!uomList.uomDetails) {
            uomList.uomDetails = uomList.uomDetails.split(' ')[0];
          }
  
          if (uomList.uomDetails == "P") {
            item.newPRQty = parseInt(prQuantity * (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          } else if (uomList.uomDetails == "S") {
            item.newPRQty = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
          } else if (uomList.uomDetails == "D") {
            item.newPRQty = parseInt(prQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
          }
        } else {
          item.newPRQty = 0;
          item.newMaxOrderQuantity = 0;
          item.newMaxStoreQuantity = 0;
        }
      }
  
      var getSelectedIds = function () {
        var selecteItemIds = self.model.indentItemDetails.map(function (item) {
          return item.itemId;
        });
        return _.uniq(selecteItemIds);
      }
  
      var SelectedItems = function () {
        self.model.prDetails = [];
        var itemIdArray = [];
        angular.forEach(self.model.indents, function (pr) {
          var prData = null;
          if (pr.isChecked) {
            prData = {
              "prId": pr.id,
              "itemIds": []
            }
          }
  
          angular.forEach(pr.items, function (item) {
            if (item.isChecked) {
              prData.itemIds.push(item.id);
            }
          });
  
          if (pr.isChecked && prData.itemIds.length > 0)
            self.model.prDetails.push(prData);
        });
      }
  
      var AddIndentItems = function () {
        if (self.model.intendItemIds.length == 0) {
          growl.error('Please select items to add');
          return false;
        } else {
          var URI = CONSTANTS.GLOBAL.AGAINST_PR_DETAILS + "/"+$scope.organizationId + "/"+$scope.unitId;
  
          SelectedItems();
          var selectedIds = getSelectedIds();
  
          var data = self.model.prDetails;
  
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              var responseData = response.data;
              var purchaseRequestListResponse = responseData.purchaseRequestList;
              self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(responseData.otherChargesList);
              if (self.model.taxList == undefined || self.model.taxList.length == 0) {
                self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
              }
  
              updatePrItemDetails(purchaseRequestListResponse);
  
              self.model.indentItemDetails = [];
              self.model.intendItemIds = [];
              self.model.items = [];
              self.model.indents = [];
  
              var popup = angular.element('#againstPRmodal');
              popup.modal('hide');
  
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              // self.model.SelectedItemsIds = [];
              growl.error(err.data.message);
            });
        }
      }
  
      var isSelected = function (item) {
        return item.isSelected;
      }
  
      var SelectAllItems = function () {
        if (self.model.selectAllRows) {
          self.model.SelectedItemsIds = self.model.items.map(function (item) {
            item.isSelected = true;
            return item.itemId;
          });
  
          self.model.accessoryItems = self.model.items.map(function (x) {
            item.isSelected = true;
            return item;
          });
  
        } else {
          self.model.SelectedItemsIds = [];
          self.model.accessoryItems = [];
          self.model.items.forEach(function (item) {
            item.isSelected = false;
          });
        }
      }
  
      var SelectItem = function (item) {
        var itemIds = self.model.SelectedItemsIds;
        var items = self.model.accessoryItems;
  
        if (item.isSelected) {
          itemIds.push(item.itemId);
          if (!self.model.addItemFlag) {
            items.push(item);
          }
        } else {
          var index = itemIds.indexOf(item.itemId);
          if (index > -1) {
            itemIds.splice(index, 1);
          }
  
          if (!self.model.addItemFlag) {
            var itemIndex = items.indexOf(item);
            if (itemIndex > -1) {
              items.splice(itemIndex, 1);
            }
          }
        }
  
        if (self.model.items.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
      }
  
      var ConfirmRemove = function (item) {
        self.model.OpenPopup('Open', 'confirmRemove');
        self.model.itemForRemove = item;
      }
  
      var RemoveItem = function (item) {
        var newArray = [];
  
        self.model.itemDetails.map(function (obj) {
          if (obj.itemId !== item.itemId) {
            newArray.push(obj);
          }
        });
        self.model.itemDetails = newArray;
        // self.model.ApplyDiscount(self.model.itemDetails);
        self.model.ClosePopup('Close', 'confirmRemove');
      }
  
      var SavePurchaseOrder = function () {
        if (ValidateItems()) {
          if (!self.model.poId) {
            CreatePurchaseOrder();
          } else {
            UpdatePurchaseOrder();
          }
        }
      }
  
      var CreatePurchaseOrder = function () {
        var purchaseOrder = self.model;
  
        //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API;
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.SAVE_DETAIL_API;
  
        var data = {
          "storeId": purchaseOrder.store.id,
          "requestingStoreId": purchaseOrder.store.id,
          "vendorId": purchaseOrder.vendorId,
          "assetTypeId": purchaseOrder.poType.id,
          "poTypeId": purchaseOrder.PRType,
          "totalAmount": purchaseOrder.totalAmount,
          "discountTypeId": purchaseOrder.discountType,
          "discount": parseFloat(purchaseOrder.finalDiscount),
          "totalDiscount": parseFloat(purchaseOrder.totalDiscount),
          "totalTaxAmount": parseFloat(purchaseOrder.totalTax),
          "totalOtherAmount": parseFloat(purchaseOrder.totalOtherCharges),
          "netAmount": parseFloat(purchaseOrder.totalNetAmount),
          "roundOffAmount": 0,
          "isConsignment": purchaseOrder.isConsignment,
          "remark": purchaseOrder.purchaseOrderRemark,
          "paymentTerm": self.model.paymentMode,
          "statusId": setStatus('New'),
          "approvalStatusId": setStatus('New'),
          "items": [],
          "organizationId": $scope.organizationId,
          "unitId": $scope.unitId,
          "userId": $scope.userId
        };
  
        angular.forEach(purchaseOrder.itemDetails, function (item, key) {
          if (self.model.poType.id == 2) {
//             var expDateMonth = item.warrantyDate.getMonth() + 1;
//             var expDateYear = item.warrantyDate.getFullYear();
//             var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
          }
  
          var bonusUom = [];
          angular.forEach(item.bonusUom, function (bonus) {
            if (!!bonus.uomTypeId) {
              bonusUom.push({
                "conversion": bonus.conversion,
                "id": bonus.id,
                "ipUom": bonus.ipUom,
                "opUom": bonus.opUom,
                "storeUom": bonus.storeUom,
                "uomTypeId": bonus.uomTypeId,
                "uomUnitId": bonus.uomUnitId.uomUnitId
              });
            }
          });
  
          var poUom = [];
          angular.forEach(item.poUom, function (puom) {
            if (!!puom.uomTypeId) {
              poUom.push({
                "conversion": puom.conversion,
                "id": puom.id,
                "ipUom": puom.ipUom,
                "opUom": puom.opUom,
                "storeUom": puom.storeUom,
                "uomTypeId": puom.uomTypeId,
                "uomUnitId": puom.uomUnitId.uomUnitId
              });
            }
          });
  
          var otherCharges = [];
          angular.forEach(item.selectedOtherCharges, function (othercharge) {
            otherCharges.push({
              "otherChargesId": othercharge.otherChargesId || null,
              "amount": parseInt(othercharge.amount)
            });
          });
          data.otherCharges = otherCharges;
            var poItem = {
              "itemId": item.itemId,
              "poQuantity": parseInt(item.poQuantity),
              "bonusQuantity": parseInt(item.bonusQuantity),
              "manufactureId": item.manufacturerId,
              "cop": parseFloat(item.costOfPurchase),
              "mrp": parseFloat(item.POAmountMRP),
              "discount": parseFloat(item.discountPercent),
              "taxAmount": item.taxAmount.toFixed(2),
              "totalAmount": parseFloat(item.poAmount),
              "totalDiscount": parseFloat(item.discountAmount),
              "totalTaxAmount": item.taxAmount.toFixed(2),
              "totalOtherAmount": parseFloat(item.otherChargesTotal),
              "netAmount": item.netAmount.toFixed(2),
              "statusId": setStatus('New'),
              "specification": item.specification,
              "remark": item.remark,
              "otherCharges": otherCharges,
              "purchaseOrderStaged": item.stageQuantityList,
              "prId": item.prIds,
              "taxId": item.selectedTax.taxId,
              "taxPercentage": parseFloat(item.selectedTaxPercent),
              "grnDetailId" : item.goodReceiptNoteDetailId, //added by minesh
              "pendingQuantity":item.pendingPOQuantity,
              "isFromConsignmentPoCreation":true
            }
  
          if (self.model.poType.id == 2) {
            poItem.warrentyPeriod = item.warrantyPeriod;
            poItem.supportTerms = item.supportTerms;
            poItem.technicalSpecification = item.technicalSpecs;
            poItem.expectedPayment = item.paymentTerms;
            poItem.poUom = undefined;
            poItem.bonusUom = undefined;
            poItem.poUomTypeId = undefined;
            poItem.poUomUnitId = undefined;
            poItem.bonusUomTypeId = undefined;
            poItem.bonusUomUnitId = undefined;
           // poItem.warrantyDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
            poItem.serviceProviderId = item.providerId;
            poItem.poAccessoriesMapper = item.poAccessoriesMapper;
            // poItem.taxId = item.selectedTax.taxId;
            // poItem.taxPercentage = parseFloat(item.selectedTaxPercent);
          } else if (self.model.poType.id == 1) {
            poItem.warrentyPeriod = undefined;
            poItem.supportTerms = undefined;
            poItem.technicalSpecification = undefined;
            poItem.expectedPayment = undefined;
            poItem.poUom = poUom;
            poItem.bonusUom = bonusUom;
            poItem.poUomTypeId = item.selectedUom ? item.selectedUom.uomTypeId : 3; //consignment po get dispence value
            poItem.poUomUnitId = item.selectedUom ? item.selectedUom.uomUnitId : 3; //consignment po get dispence value
            poItem.bonusUomTypeId = item.bonusSelectedUom ? item.bonusSelectedUom.uomTypeId : 3; //consignment po get dispence value
            poItem.bonusUomUnitId = item.bonusSelectedUom ? item.bonusSelectedUom.uomUnitId : 3; //consignment po get dispence value
            poItem.warrantyDate = undefined;
            poItem.serviceProviderId = undefined;
            poItem.poAccessoriesMapper = undefined;
            /*      poItem.taxId = item.taxId;
                 poItem.taxPercentage = parseFloat(item.taxPercent); */
          }
  
          data.items.push(poItem);
        });
        
        console.log(data);
  
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            NavigateToPO();
            growl.success(response.data.message);
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
  
      var UpdatePurchaseOrder = function () {
        //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API + self.model.poId;
    	  var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.UPDATE_DETAIL_API +"/"+ self.model.poId;
  
        var data = {
          "totalAmount": self.model.totalAmount,
          "discountTypeId": parseInt(self.model.discountType),
          "discount": parseFloat(self.model.finalDiscount),
          "totalDiscount": parseFloat(self.model.totalDiscount),
          "totalTaxAmount": parseFloat(self.model.totalTax),
          "totalOtherAmount": parseFloat(self.model.totalOtherCharges),
          "netAmount": parseFloat(self.model.totalNetAmount),
          "roundOffAmount": 0,
          "isConsignment": self.model.isConsignment,
          "remark": self.model.purchaseOrderRemark,
          "poTypeId": self.model.poType.id,
          "paymentTerm": self.model.paymentMode,
          "items": [],
          "organizationId": $scope.organizationId,
          "unitId": $scope.unitId,
          "userId": $scope.userId,
          "otherCharges": []
        };
        angular.forEach(self.model.itemDetails, function (item, key) {
          if (self.model.poType.id == 2) {
            var expDateMonth = item.warrantyDate.getMonth() + 1;
            var expDateYear = item.warrantyDate.getFullYear();
            var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
          }
  
          var stagedQtyList = [];
          angular.forEach(item.stageQuantityList, function (staged) {
            stagedQtyList.push({
              "id": staged.id,
              "quantity": parseInt(staged.quantity),
              "stagedDate": staged.stagedDate
            });
          });
  
          var otherCharges = [];
          angular.forEach(item.selectedOtherCharges, function (othercharge) {
            if (!!parseFloat(othercharge.amount)) {
              otherCharges.push({
                "id": othercharge.id,
                "otherChargesId": othercharge.otherChargesId || null,
                "amount": parseFloat(othercharge.amount)
              });
            }
          });
  
          var bonusUom = [];
          angular.forEach(item.bonusUom, function (bonus) {
            if (!!bonus.uomTypeId) {
              bonusUom.push({
                "conversion": bonus.conversion,
                "id": bonus.id,
                "ipUom": bonus.ipUom,
                "opUom": bonus.opUom,
                "storeUom": bonus.storeUom,
                "uomTypeId": bonus.uomTypeId,
                "uomUnitId": bonus.uomUnitId.uomUnitId
              });
            }
          });
  
          var poUom = [];
          angular.forEach(item.poUom, function (uom) {
            if (!!uom.uomTypeId) {
              poUom.push({
                "conversion": uom.conversion,
                "id": uom.id,
                "ipUom": uom.ipUom,
                "opUom": uom.opUom,
                "storeUom": uom.storeUom,
                "uomTypeId": uom.uomTypeId,
                "uomUnitId": uom.uomUnitId.uomUnitId
              });
            }
          });
          data.otherCharges = otherCharges;
          var poItem = {
            "id": item.id,
            "itemId": item.itemId,
            "poQuantity": parseInt(item.poQuantity),
            //"bonusQuantity": parseInt(item.bonusQuantity),
            // "manufactureId": item.manufacturerId,
            "cop": parseFloat(item.costOfPurchase),
            "mrp": parseFloat(item.POAmountMRP),
            "discount": parseFloat(item.discountPercent),
            // "taxId": item.taxId,
            // "taxPercentage": item.taxPercent,
            "taxId": item.selectedTax.taxId,
            "taxPercentage": parseFloat(item.selectedTaxPercent),
            "taxAmount": parseFloat(item.taxAmount),
            "totalAmount": parseFloat(item.poAmount),
            "totalDiscount": parseFloat(item.discountAmount),
            "totalTaxAmount": parseFloat(item.taxAmount),
            "totalOtherAmount": parseFloat(item.otherChargesTotal),
            "netAmount": parseFloat(item.netAmount),
            "otherCharges": otherCharges,
            "specification": item.specification,
            "remark": item.remark,
            "purchaseOrderStaged": stagedQtyList,
            "prId": item.prIds,
            "grnDetailId" : item.grnDetailId ,//added by minesh
            "pendingQuantity":item.pendingPOQuantity,
            "isFromConsignmentPoCreation":true
          }
  
          if (!!item.id) {
            poItem.statusId = undefined;
          } else {
            poItem.statusId = setStatus("New");
          }
  
          if (self.model.poType.id == 2) {
            poItem.warrentyPeriod = item.warrantyPeriod;
            poItem.supportTerms = item.supportTerms;
            poItem.technicalSpecification = item.technicalSpecs;
            poItem.expectedPayment = item.paymentTerms;
            poItem.poAccessoriesMapper = item.poAccessoriesMapper;
            poItem.manufactureId = item.manufacturerId,
            poItem.poUom = undefined;
            poItem.bonusUom = undefined;
            poItem.poUomTypeId = undefined;
            poItem.poUomUnitId = undefined;
            poItem.bonusUomTypeId = undefined;
            poItem.bonusUomUnitId = undefined;
            poItem.warrantyDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
            // poItem.taxId = item.selectedTax.taxId;
            // poItem.taxPercentage = parseFloat(item.selectedTaxPercent);
            if (!item.id) {
              poItem.serviceProviderId = item.providerId;
            } else {
              poItem.serviceProviderId = item.serviceProviderId;
            }
          } else if (self.model.poType.id == 1) {
            poItem.warrentyPeriod = undefined;
            poItem.supportTerms = undefined;
            poItem.technicalSpecification = undefined;
            poItem.expectedPayment = undefined;
            poItem.poUom = poUom;
            poItem.bonusUom = bonusUom;
            poItem.poUomTypeId = item.selectedUom ? item.selectedUom.uomTypeId : 3 ; //consignment po get dispence value
            poItem.poUomUnitId = item.selectedUom ? item.selectedUom.uomUnitId : 3 ; //consignment po get dispence value
            poItem.bonusUomTypeId = item.bonusSelectedUom ? item.bonusSelectedUom.uomTypeId : 3; //consignment po get dispence value
            poItem.bonusUomUnitId = item.bonusSelectedUom ? item.bonusSelectedUom.uomUnitId : 3; //consignment po get dispence value
            poItem.manufactureId = item.manufacturerId,
            poItem.poAccessoriesMapper = undefined;
            poItem.warrantyDate = undefined;
            poItem.serviceProviderId = undefined;
            // poItem.taxId = item.taxId;
            // poItem.taxPercentage = parseFloat(item.taxPercent);
          }
  
          data.items.push(poItem);
        });
        
        

        console.log(data);
  
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            NavigateToPO();
            growl.success(response.data.message);
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
  
      var updateDiscountForEachItems = function (percentageDiscount, items, discountType) {
        var itemDiscount = (percentageDiscount / self.model.totalAmount) * 100;
  
        angular.forEach(items, function (item, key) {
          if (!!itemDiscount) {
            item.discountPercent = itemDiscount;
            item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
            item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
            item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
          } else if (!itemDiscount) {
            item.discountPercent = itemDiscount;
            item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
          }
        });
      }
  
      var ViewHistory = function (item) {
        var URI = CONSTANTS.GLOBAL.PO_HISTORY + $scope.unitId + '/' + item.itemId + '/' + item.poId;
  
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.lastOrderDetails = response.data;
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
          });
      }
      
      var ConsignmentStockPOHistory = function (item) {
    	  
    	  var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.HISTORY_API +"/"+item.grnDetailId + "/" + $scope.unitId;
    
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.consignmentPOHistory = response.data;
              var newArray=[];
              self.model.consignmentPOHistory.forEach(function (qrItem) {
            	  if($scope.isFromEdit!=undefined && $scope.isFromEdit!=null && $scope.isFromEdit==true)
				  {
            	  if(qrItem.poNumber!=$scope.consignmentPO.poNumber)
            		  {
            		  newArray.push(qrItem);
            		  }
				  }
				  else
				  {
					   newArray.push(qrItem);
				  }
              })
              self.model.consignmentPOHistory=newArray;
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
            });
        }
  
      var NavigateToPO = function () {
        $state.go('consignmentPoCreat');
      }
  
      var ValidateStagedQuantity = function (item) {
        var isValid = true;
  
        if (isNaN(parseFloat(item.quantity))) {
          // item.isQuantityValid = true;
          isValid = false;
          growl.error("Please Enter Quantity");
        } else if (parseInt(item.quantity) == 0) {
          // item.isQuantityValid = true;
          isValid = false;
          growl.error('Quantity cannot be zero');
        } else {
          item.quantity = parseInt(item.quantity);
          isValid = true;
          // isValid = ValidateMaxOrderQuantity(item);
        }
  
        return isValid;
      }
  
  
      var CalculateAmount = function (item) {
        if ($scope.taxInclusive == true) {
          item.poAmount = (item.poQuantity ? item.poQuantity : 0 )* (item.costOfPurchase ? item.costOfPurchase : 0);
          item.netAmount = ((item.poAmount - item.discountAmount) + (item.otherChargesTotal));
          //  item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
          //  item.netAmount = (((item.totalAmount) - (item.itemDiscount))); 
        }
        else {
          item.poAmount = (item.poQuantity * item.costOfPurchase);
          item.taxAmount = ((item.poAmount) * (item.selectedTaxPercent / 100));
          item.netAmount = ((item.poAmount - item.discountAmount) + (item.taxAmount) + (item.otherChargesTotal))
          // item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
          // item.taxAmount = (((item.totalAmount) - (item.itemDiscount)) * (item.taxPercent / 100));
          // item.netAmount = (((item.totalAmount) - (item.itemDiscount) + item.taxAmount)); 
  
  
        }
      }
  
      var GetQRIndentList = function () {
        if (ValidateDate()) {
          var URI = CONSTANTS.GLOBAL.AGAINST_QR_SEARCH;
  
          var data = {
            // "isConsignment": self.model.isConsignment,
            "storeId": self.model.store.id,
            "assetTypeId": self.model.poType.id,
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "vendorId": self.model.vendorId,
            "qrNo": self.model.prNumber,
            "unitId":$scope.unitId
          }
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.indents = response.data;
              self.model.indentItemDetails = [];
              self.model.intendItemIds = [];
              angular.forEach(self.model.indents, function (pr) {
                pr["isChecked"] = false;
                angular.forEach(pr.items, function (item) {
                  item["isChecked"] = false;
                });
              });
  
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              self.model.indents = [];
              growl.error(err.data.message);
            });
        }
      }
  
      var AddQRIndentItems = function () {
        if (self.model.intendItemIds.length == 0) {
          growl.error('Please select items to add');
          return false;
        } else {
          var URI = CONSTANTS.GLOBAL.AGAINST_QR_DETAILS + "/"+ $scope.organizationId + "/"+ $scope.unitId;
  
          SelectedQRItems();
          var selectedIds = getSelectedIds();
  
          var data = self.model.prDetails;
  
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              var responseData = response.data;
              var purchaseRequestListResponse = responseData.quotationRequestList;
              self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(responseData.otherChargesList);
              if (self.model.taxList == undefined || self.model.taxList.length == 0) {
                self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
              }
  
              updateQRItemDetails(purchaseRequestListResponse);
  
              self.model.indentItemDetails = [];
              self.model.intendItemIds = [];
              self.model.items = [];
              self.model.indents = [];
  
              var popup = angular.element('#approvedQRmodal');
              popup.modal('hide');
  
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              // self.model.SelectedItemsIds = [];
              growl.error(err.data.message);
            });
        }
      }
  
      var updateQRItemDetails = function (qrResponseData) {
        var newItemsHash = {};
        var itemDetailsHash = {};
        self.model.itemDetails.forEach(function (itemDetail) {
          itemDetailsHash[itemDetail.itemId] = itemDetail;
        });
  
        qrResponseData.forEach(function (qrItem) {
          var qrNo = qrItem.qrNo;
          var qrId1 = qrItem.id;
          var qrDate = qrItem.qrDate;
          qrItem.items.forEach(function (item) {
  
            var itemId = item.itemId;
            var qrId = qrId1;
            var prQuantity = Number(item.pendingPOqty);
            var qrObj = { id: qrId1, qrNo: qrNo, qrDate: qrDate };
  
            if (itemDetailsHash.hasOwnProperty(itemId)) {
              // item found in already saved list
              var itemDetail = itemDetailsHash[itemId];
              if (itemDetail.prIds && itemDetail.prIds.length) {
                // prIds list is present
                if (itemDetail.prIds.indexOf(prId) == -1) {
                  itemDetail.prNumbersList.push(qrObj);
                  itemDetail.prIds.push(qrId);
                } else {
                  //DO nothing, Item is already preset in saved table
                }
              } else {
                // prIds list not present
                itemDetail.prNumbersList = [qrObj]
                itemDetail.prIds = [qrId];
              }
  
            } else if (newItemsHash.hasOwnProperty(itemId)) {
              // item found in new item hash
              var savedItem = newItemsHash[itemId];
              savedItem.prNumbersList.push(qrObj);
              savedItem.prIds.push(qrId);
            } else {
              // item not found in item hash
              var clonedItem = JSON.parse(JSON.stringify(item));
              delete clonedItem.id;
              delete clonedItem.prQuantity;
              clonedItem.prNumbersList = [qrObj]
              clonedItem.prIds = [qrId];
              newItemsHash[itemId] = clonedItem;
              newItemsHash[itemId].poAccessoriesMapper = { "itemIds": [] }
              newItemsHash[itemId].childAccessoryItems = [];
            }
  
          });
        });
  
        for (var itemId in newItemsHash) {
  
          if (!!newItemsHash[itemId].taxPercent) {
            newItemsHash[itemId].selectedTaxPercent = newItemsHash[itemId].taxPercent.toFixed(2);
          }
          newItemsHash[itemId].selectedTax = self.model.taxList.find(function (val) {
            return newItemsHash[itemId].taxId == val.taxId;
          });
  
          newItemsHash[itemId]['poQuantity'] = 0;
          newItemsHash[itemId]['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
          newItemsHash[itemId]['otherChargesTotal'] = 0;
          newItemsHash[itemId]['selectedOtherCharges'] = [{
            otherChargesId: self.model.otherChargesDropdownList[0].id,
            amount: 0
          }];
  
          var total = 0;
          angular.forEach(newItemsHash[itemId].prNumbersList, function (value) {
            total = total + parseFloat(value.prQuantity);
          });
          newItemsHash[itemId].totalPrQty = total;
  
          newItemsHash[itemId]['prList'] = [];
  
  
          self.model.itemDetails.push(newItemsHash[itemId]);
          newItemsHash[itemId].prMaxStoreQty = 0;
          if (self.model.poType.id == 1) {
            selectDefaultUOM(newItemsHash[itemId]);
  
            newItemsHash[itemId].selectedUom = newItemsHash[itemId].uom.find(function (uom) {
              return newItemsHash[itemId].uomTypeId == uom.uomTypeId;
            });
  
            newItemsHash[itemId].bonusSelectedUom = newItemsHash[itemId].uom.find(function (uom) {
              return newItemsHash[itemId].uomTypeId == uom.uomTypeId;
            });
  
            self.model.CalculateStock(newItemsHash[itemId], newItemsHash[itemId].uom, newItemsHash[itemId].selectedUom);
          } else if (self.model.poType.id == 2) {
            //   newItemsHash[itemId].selectedTax = self.model.taxList[0];
            newItemsHash[itemId].newMaxOrderQuantity = newItemsHash[itemId].maxOrderQuantity;
            newItemsHash[itemId].newMaxStoreQuantity = newItemsHash[itemId].maxStoreQuantity;
          }
        }
      }
  
      var SelectedQRItems = function () {
        self.model.prDetails = [];
        var itemIdArray = [];
        angular.forEach(self.model.indents, function (qr) {
          var qrData = null;
          if (qr.isChecked) {
            qrData = {
              "qrId": qr.id,
              "itemIds": []
            }
          }
  
          angular.forEach(qr.items, function (item) {
            if (item.isChecked) {
              qrData.itemIds.push(item.id);
            }
          });
  
          if (qr.isChecked && qrData.itemIds.length > 0)
            self.model.prDetails.push(qrData);
        });
      }
      
      var Expand = function (grn, grnItemList) {
	      angular.forEach(grnItemList, function (value) {
	        value.Expanded = false;
	      });

	      grn.Expanded = !grn.Expanded;
	    }
      
      var SelectAllItemsOfGRN = function(grn) {
			var itemIds = self.model.SelectedOrderIds;
			var grnIds = self.model.SelectedGRNOrderIds;
			grn.isSelected = grn.isSelectedAllItems;
			if (grn.isSelected) {
				grn.isDisabled = false;
				grn.items.forEach(function(item) {
					if (!isDisableForGrnItem(item)) {
						item.isSelected = true;
						item.isDisabled = false;
						self.model.SelectedOrderIds.push(item.id);
						self.model.setSelectedItems.push(item);
					}
				});

				self.model.SelectedGRNOrderIds.push(grn.id);
				self.model.setSelectedGRN.push(grn);

			} else {
				grn.isDisabled = true;
				grn.items.forEach(function(item) {
					var index = itemIds.indexOf(item.id);
					if (index > -1) {
						itemIds.splice(index, 1);
						var indexOfRemovItem = self.model.setSelectedItems
								.findIndex(function(element) {
									return element.id === item.id;
								})
						if (indexOfRemovItem !== -1) {
							self.model.setSelectedItems.splice(
									indexOfRemovItem, 1)
						}
					}
					item.isSelected = false;
					item.isDisabled = true;
				});

				var grnIndex = grnIds.indexOf(grn.id);
				if (grnIndex > -1) {
					grnIds.splice(grnIndex, 1);
					var indexOfRemovItem = self.model.setSelectedGRN
							.findIndex(function(element) {
								return element.id === grn.id;
							})
					if (indexOfRemovItem !== -1) {
						self.model.setSelectedGRN.splice(indexOfRemovItem, 1)
					}
				}

			}

			if (self.model.grnListItems.every(isSelectedForGrnItem)) {
				self.model.selectAllRows = true;
				self.model.selectAllGRNRows = true;
			} else {
				self.model.selectAllRows = false;
				self.model.selectAllGRNRows = false;
			}
		}
  
      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("addNewConsignmentPoCreat", {
        url: "/addNewConsignmentPoCreat",
        templateUrl: 'views/procurement/quotation/add-New-Consignment-Po-Creat.html',
        controller: "addNewConsignmentPoCreat.Controller",
        controllerAs: "vm",
        title: 'Consignment Purchase Order Creation'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("addNewConsignmentPoCreat.Controller", addNewConsignmentPoCreatController);
  })();