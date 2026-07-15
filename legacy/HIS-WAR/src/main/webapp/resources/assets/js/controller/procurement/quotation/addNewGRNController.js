(function () {
  "use strict";

  function addNewGRNController($window, $scope, $rootScope, $http, reportService, $filter, $state, GenericService,BillingGenericService,GenericServiceFileUpload,PaginationService, commonDetailService, CurrencyService, growl, StatusService, CONSTANTS,$cookies,stopTransactionService,$timeout) {
    var self = this;
    var getNextMonth = new Date(new Date().getFullYear(), new Date().getMonth() , 1);
    var getCurrentMonth = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
    var cookieObject = $cookies.getObject('cookieObject'); 
    if(cookieObject == undefined){ 
      $state.go('login'); return;
    }
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId;

    function initializeController() {
      $rootScope.loginpage = true;
      
      var cookieObject = $cookies
		.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;

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
        invoiceDate: new Date(),
        dueDate: new Date(),
        inwordDate: new Date(),
        dcDate: new Date(),
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
        dueDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          minDate: new Date(),
          maxDate : moment(new Date()).add(360, 'days').toDate()
        },
        invoiceDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        inwordDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        stagedQtyDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          minDate: new Date()
        },
        dcDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        expiryDateOptions: {
          minDate: getNextMonth,
          minMode: "month"
        },
       /*
		 * warrantyDateOptions: { minDate: getNextMonth, minMode: "month" },
		 */
        // start code for change for resolved HIS-9916 GRN for asset - Warranty
		// from should allow to select current month
        /*
		 * warrantyDateOptions: { minDate: getCurrentMonth, minMode: "month" },
		 */
        // end code for change for resolved HIS-9916 GRN for asset - Warranty
		// from should allow to select current month
          
        // start code for change for resolved HIS-9636 Expiry on GRN for asset -
		// Should be able to select current month and past months
          warrantyDateOptions: {
              minMode: "month"
            },
        // end code for change for resolved HIS-9636 Expiry on GRN for asset -
		// Should be able to select current month and past months
        warrantyFromOptions: {
          maxDate: getCurrentMonth,
          minMode: "month"
        },
        warrantyToOptions: {
          minDate: getNextMonth,
          minMode: "month"
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
        poTypes: [{
          "type": "Select Type"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        intendItemIds: [],
        indentItemDetails: [],
        bonusReceivedQuantity: [],
        costOfPurchase: [],
        mrp: [],
        grnReceivedQuantity: [],
        batchNumber: [],
        expiryDate: [],
        warrantyDate: [],
        warrantyDateTo: [],
        taxPercent: [],
        taxCodeValid: [],
        isUOMValid: [],
        selectedUOMList: [],
        bonusBatchExpiryData:[],
        selectedUOMUnit: [],
        bonusSelectedUOMList: [],
        bonusSelectedUOMUnit: [],
        stageQuantityList: [],
        stagedQuantity: [],
        selectedItemIndex: 0,
        bonusSelectedItemIndex: 0,
        bonusSelectedItemBatchExpiryIndex:0,
        selectedIndex: null,
        selectedOtherChargeIndex: null,
        otherChargesDropdownList: [],
        otherChargesList: [],
        prDetails: [],
        isConsignment: false,
        dateOpen: [],
        bonusDateOpen:[],
        wFromDateOpen: [],
        wToDateOepn: [],
        wToDateOepnAccessories: [],
        serviceProviderValid: [],
        creditNoteAmountListTest :[],
        dateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
        },
        dateOptionsTo: {
          formatYear: 'yyyy',
          showWeeks: false,
        },
        searchGRN: {
          itemName: undefined,
          itemCode: undefined
        },
        discountPercent: [],
        discountAmount: [],
        accessoryList: [],
        defaultOtherCharge: [{
          "otherCharges": "Select Other Charge"
        }],
        taxListSelection: [{
          "taxName": "Select tax"
        }],
        // PRType: 1,
        requestType: 1,
        prRequestTypeId: 1,
        isSaveStaged: false,
        isSelectedAllItem: false
      };
      $scope.billType= [{
          "id": 1,
          "name":"Cash"
        },
        {
            "id": 2,
            "name":"Credit"
          }];
      
      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      });

      self.model.OpenInvoiceDate = OpenInvoiceDate;
      self.model.OpenDueDate = OpenDueDate;
      self.model.OpenInwordDate = OpenInwordDate;
      self.model.OpenDCDate = OpenDCDate;
      self.model.OpenExpiryDate = OpenExpiryDate;
      self.model.OpenBonusExpiryDate=OpenBonusExpiryDate;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenStagedQtyDate = OpenStagedQtyDate;
      self.model.OpenToDate = OpenToDate;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.GetItemList1 = GetItemList1;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.AddItems1 = AddItems1;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SaveGRN = SaveGRN;
      self.model.NavigateToGRN = NavigateToGRN;
      self.model.ChangeRequestType = ChangeRequestType;
      self.model.GetApprovedPOList = GetApprovedPOList;
      self.model.Expand = Expand;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.selectAllIndentRecords = selectAllIndentRecords;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.SelectIndentAllItem = SelectIndentAllItem;
      self.model.AddApprovedPOItems = AddApprovedPOItems;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.SearchVendorForGRN = SearchVendorForGRN;
      self.model.AddVendorName = AddVendorName;
      self.model.AddVendor = AddVendor;
      self.model.SearchProviderNames = SearchProviderNames;
      self.model.AddProviderName = AddProviderName;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.uomInfoOnClick = uomInfoOnClick;
      self.model.uomModalOnClick = uomModalOnClick;
      self.model.bonusUomInfoOnClick = bonusUomInfoOnClick;
      self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;
      self.model.bonusUomModalOnClick = bonusUomModalOnClick;
      self.model.bonusBatchExpiryModalOnClick=bonusBatchExpiryModalOnClick;
      self.model.isConsignmentFlag = isConsignmentFlag;
      self.model.fillPOQuantity = fillPOQuantity;
      self.model.addPOQuantity = addPOQuantity;
      self.model.lessPOQuantity = lessPOQuantity;
      self.model.sumPOQuantity = sumPOQuantity;
      self.model.AddOtherCharge = AddOtherCharge;
      self.model.lessOtherCharge = lessOtherCharge;
      self.model.sumOtherCharges = sumOtherCharges;
      self.model.ApplyDiscount = ApplyDiscount;
      self.model.AddItemDiscount = AddItemDiscount;
      self.model.fillOtherChargesList = fillOtherChargesList;
      self.model.fillOtherChargesListShow = fillOtherChargesListShow;
      self.model.FillStagedQuantity = FillStagedQuantity;
      self.model.SetDecimal = SetDecimal;
      self.model.OpenWarrantyDate = OpenWarrantyDate;
      self.model.OpenWarrantyToDate = OpenWarrantyToDate;
      self.model.OpenWarrantyToDateAccessories = OpenWarrantyToDateAccessories;
      self.model.OpenWarrantyFromDate = OpenWarrantyFromDate;
      self.model.fillPOModalList = fillPOModalList;
      self.model.SaveStagedQuantity = SaveStagedQuantity;
      self.model.ViewTerms = ViewTerms;
      self.model.SaveTerms = SaveTerms;
      self.model.AddChildItems = AddChildItems;
      self.model.validate = validate;
      self.model.SaveAccessory = SaveAccessory;
      self.model.AddAccessories = AddAccessories;
      self.model.OpenChildItems = OpenChildItems;
      self.model.RemoveAccessoryItem = RemoveAccessoryItem;
      self.model.ViewAccessory = ViewAccessory;
      self.model.SetDecimalDiscount = SetDecimalDiscount;
      self.model.ValidateConsumableAmount = ValidateConsumableAmount;
      self.model.ValidateItemDiscount = ValidateItemDiscount;
      self.model.ApplyDiscountValidation = ApplyDiscountValidation;
      self.model.SetDecimalOtherCharge = SetDecimalOtherCharge;
      self.model.CalculateStock = CalculateStock;
      self.model.CalculateStockForBonus = CalculateStockForBonus;
      self.model.CalculateBonusQuantity = CalculateBonusQuantity;
      self.model.CalculateStock1 = CalculateStock1;
      self.model.CalculateStock2 = CalculateStock2;
      self.model.SelectTax = SelectTax;
      self.model.receivedQtyChange = receivedQtyChange;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      self.model.ConfirmAssetTypePopup1 = ConfirmAssetTypePopup1;
      self.model.ConfirmValidation = ConfirmValidation;
      self.model.EmptyList = EmptyList;
      self.model.discountCalc = discountCalc;
      self.model.CalculateDiscOnQty = CalculateDiscOnQty;
      self.model.ViewHistory = ViewHistory;
      self.model.ViewLastGrnDetails = ViewLastGrnDetails;
      self.model.GetCreditNoteAmount = GetCreditNoteAmount;
      self.model.ValidateQty = ValidateQty;
      self.model.rePrintBillSlip = rePrintBillSlip;
      self.model.validateRecQty = validateRecQty;
      self.model.ValidateWarrantyDate = ValidateWarrantyDate;
      self.model.ValidateWarrantyFromDate =ValidateWarrantyFromDate;
      self.model.AddItemForBatch = AddItemForBatch;
       self.model.AddItemForBatch1 = AddItemForBatch1;
      self.model.dateChange = dateChange;
      self.model.SetOldType=SetOldType;
      self.model.selectAllCreditNoteAmount = selectAllCreditNoteAmount;
      self.model.selectCreditNoteAmount = selectCreditNoteAmount;
      self.model.addCreditNote = addCreditNote;
      self.model.removeCreditNote = removeCreditNote;
      self.model.saveCreditNoteAmount = saveCreditNoteAmount;
      self.model.calculateOtherChargesAmountForItem = calculateOtherChargesAmountForItem;
      self.model.billType=$scope.billType[1];
      self.model.totalCreditNoteAmount = 0
      self.model.cnAmountValidation = cnAmountValidation;
      self.model.uploadDoccument = uploadDoccument;
      self.model.SetFocus1 = SetFocus1;
      self.model.focusitemname = focusitemname;
      self.model.getInvoiceNumber = getInvoiceNumber;
      self.model.clear = clear;
      self.model.focusdata = focusdata;
      self.model.focusService = focusService;
      self.model.wartodate = wartodate;
      
      // self.model.totalReceivedQTy = totalReceivedQTy;
      if(self.model.prRequestTypeId == 1){
        var obj = {
          itemId: '',
          itemCode: '',
          itemName: '',
          poBonusQuantity: '',
          bonusQuantityList: '',
          bonusSelectedUom: '',
          bonusUom: '',
          bonusUomType: '',
          bonusUomTypeId: '',
          bonusUomUnit: '',
          bonusUomUnitId: '',
          genericId: '',
          genericName: '',
          grnAccessoriesMapper: '',
          grnAmount: '',
          grnStagedLists: '',
          manufacturerId: '',
          manufacturerName: '',
          maxOrderQuantity: '',
          maxStoreQuantity: '',
          netAmount: '',
          newMaxOrderQuantity: '',
          newMaxStoreQuantity: '',
          newPOQty: '',
          poBonusQuantity: '',
          poDetailId: '',
          poGrnQuantity: '',
          poIds: '',
          poLeaseQuantity: '',
          poNumbersList: '',
          poOtherCharges: '',
          poPendingQuantity: '',
          poQuantity: '',
          poStagedList: '',
          poUom: '',
          poUomType: '',
          poUomTypeId: '',
          poUomUnit: '',
          poUomUnitId: '',
          prList: '',
          selectedTax: '',
          selectedTaxPercent: '',
          selectedUom: '',
          totalPoQty: '',
          uom: '',
          mrp: '',
          taxAmount: '',
          pendingQuantity: '',
          disableBonusTextField:false
        };
        
        self.model.itemDetails.push(obj);
      }
      PopulateDropdown();
      SetFocus1();
    }

    var clear = function(item){
      item.itemId = '',
      item.itemCode = '',
      item.itemName = '',
      item.poBonusQuantity = '',
      item.bonusQuantityList = '',
      item.batchNumber = '',
      item.bonusSelectedUom = '',
      item.bonusUom = '',
      item.bonusUomType = '',
      item.bonusUomTypeId = '',
      item.bonusUomUnit = '',
      item.bonusUomUnitId = '',
      item.genericId = '',
      item.genericName = '',
      item.grnAccessoriesMapper = '',
      item.grnReceivedQuantity = '',
      //item.grnAmount = '',
      item.grnStagedLists = '',
      item.manufacturerId = '',
      item.manufacturerName = '',
      item.maxOrderQuantity = '',
      item.maxStoreQuantity = '',
      //item.netAmount = '',
      item.newMaxOrderQuantity = '',
      item.newMaxStoreQuantity = '',
      item.newPOQty = '',
      item.poBonusQuantity = '',
      item.poDetailId = '',
      item.poGrnQuantity = '',
      item.poIds = '',
      item.poLeaseQuantity = '',
      item.poNumbersList = '',
      item.poOtherCharges = '',
      item.poPendingQuantity = '',
      item.poQuantity = '',
      item.poStagedList = '',
      item.poUom = '',
      item.poUomType = '',
      item.poUomTypeId = '',
      item.poUomUnit = '',
      item.poUomUnitId = '',
      item.prList = '',
      item.selectedTax = '',
      item.selectedTaxPercent = '',
      item.selectedUom = '',
      item.serviceProvider ='',
      item.warrantyDate ='',
      item.warrantyDateTo= '',
      item.totalPoQty = '',
      item.uom = '',
      item.mrp = '',
      //item.taxAmount = '',
      item.pendingQuantity = '',
      self.model.totalNetAmount1 = (self.model.totalNetAmount1 - item.netAmount) - $scope.roundOff;
      self.model.totalAmount = self.model.totalAmount - item.grnAmount;
      self.model.totalDiscount = self.model.totalDiscount - item.discountAmount;
      self.model.totalTax = self.model.totalTax - item.Currency;
      item.receivedQtyCon = '';
      item.receivedQtyCalculation = '';
      item.uomUnitData = '';
      item.receivedQtyBonusCon = '';
      item.receivedQtyBonusCalculation = '';
      item.uomUnitDataBonus = '';
      item.netAmount = '';
      $scope.roundOff = 0;
      item.bonusReceivedQuantity = '';
      item.expiryDate = '';
      item.costOfPurchase = '';
      self.model.otherChargesAmount=0;
      self.model.otherChargesList=[];
      $scope.otherchargesmapper=[];
    }
    
    var SetFocus1 = function () {
      $window.document.getElementById('fromStore').focus();
    }
    var getInvoiceNumber = function (invoiceNumber){
    	 console.log("invoiceNumber...",invoiceNumber);
    	 $scope.flag=false;
         angular.forEach($scope.invoiceNumbers, function (invoice) {
                if(invoice!=null && invoice != "" && invoice==invoiceNumber)
                {
          		  
                   growl.warning("Invoice Number Already Exist"); 
                  
                  //  setTimeout(function(){
                	//    self.model.invoiceNumber='';
                	//  },2000);
                   $scope.flag = true;
                  
                }
               
         });
         return $scope.flag;
      }
    var focusitemname = function (event){
      if(event.which == 9){
        if(self.model.poType.id == 1){
          $timeout(function(){
            var itemSearch = 'item-name_'+self.model.itemDetails.length;
            document.getElementById(itemSearch).focus();
          },1);
        } 
        if(self.model.poType.id == 2){
          $timeout(function(){
            var abc = 'abc_'+self.model.itemDetails.length;
            document.getElementById(abc).focus();
          },500);
        }
      }
    }

    var focusdata = function(event, item){
      if(!item.batchNumber){
        var idbatch = 'batch_'+self.model.itemDetails.length;
        document.getElementById(idbatch).focus();
      }
      else if(!item.expiryDate){
        var expiry = 'expiry_'+self.model.itemDetails.length;
        document.getElementById(expiry).focus();
      }
      else if(!item.cop){
        var cop = 'cop_'+self.model.itemDetails.length;
        document.getElementById(cop).focus();
      }
      else if(!item.mrp){
        var mrp = 'mrp_'+self.model.itemDetails.length;
        document.getElementById(mrp).focus();
      }
      else{
        var addrBtn = 'addrBtn_'+self.model.itemDetails.length;
        document.getElementById(addrBtn).focus();
      }
    }

    var focusService = function (event){
      if(event.which == 9){
        $timeout(function(){
          var service = 'ser_'+self.model.itemDetails.length;
          document.getElementById(service).focus();
        },500);
      }
    }

    var wartodate = function (event){
      if(event.which == 9){
        $timeout(function(){
          var wartodate = 'wartodate_'+self.model.itemDetails.length;
          document.getElementById(wartodate).focus();
        },500);
      }
    }
    
    var dateChange = function (date) {
      angular.forEach(self.model.grnStagedList, function (stagedQty) {
        if (!!stagedQty.isEditable) {
          stagedQty.stagedDcDate = moment(date).format('YYYY-MM-DD');
        }
      });
      var invoiceDate = moment(self.model.invoiceDate).format('DD/MM/YYYY');
      var dcDate = moment(date).format('DD/MM/YYYY');
      if(invoiceDate > dcDate){
        growl.warning("DC Date should not be below Invoice Date");
        self.model.dcDate = "";
      }
    }
    var fillPOModalList = function (list) {
      self.model.poNumbersList = list;
    }

    var OpenWarrantyDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var OpenWarrantyToDate = function (idx) {
      self.model.wToDateOepn[idx] = true;
    }

    var OpenWarrantyToDateAccessories = function (idx) {
      self.model.wToDateOepnAccessories[idx] = true;
    }

    var OpenWarrantyFromDate = function (idx) {
      self.model.wFromDateOpen[idx] = true;
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
      $scope.roundOff = 0.00;
      self.model.totalNetAmount = 0.00;
      self.model.totalNetAmount1 = 0.00;
    }

    // asset type change confirmation
    var SetOldValueAssetType = function (assetType) {
      self.model.oldAssetType = assetType;
      self.model.poType = self.model.oldAssetType;
    }

    var ChangeFromAssetType = function (assetType) {
      if (self.model.itemDetails[0].itemId) {
        self.model.poType = self.model.oldAssetType;
        self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
        if(assetType.id == 1){
          OpenPopup('Open', 'confirmAssetTypePopup');
        }
        if(assetType.id == 2){
          OpenPopup('Open', 'confirmAssetTypePopup1');
        }
        document.getElementById('invoiceno').focus();
      }
      $timeout( function(){
        document.getElementById('invoiceno').focus();
      },1000);
    }

    var ConfirmAssetTypePopup = function () {
      self.model.poType = self.model.assetTypeNew;
      ClosePopup('close', 'confirmAssetTypePopup');
      // self.model.itemDetails = [];
      $scope.roundOff = 0.00;
      self.model.totalNetAmount = 0.00;
      self.model.totalNetAmount1 = 0.00;
      if(self.model.prRequestTypeId == 1){
        var obj = {
          itemId: '',
          itemCode: '',
          itemName: '',
          poBonusQuantity: '',
          bonusQuantityList: '',
          bonusSelectedUom: '',
          bonusUom: '',
          bonusUomType: '',
          bonusUomTypeId: '',
          bonusUomUnit: '',
          bonusUomUnitId: '',
          genericId: '',
          genericName: '',
          grnAccessoriesMapper: '',
          grnAmount: '',
          grnStagedLists: '',
          manufacturerId: '',
          manufacturerName: '',
          maxOrderQuantity: '',
          maxStoreQuantity: '',
          netAmount: '',
          newMaxOrderQuantity: '',
          newMaxStoreQuantity: '',
          newPOQty: '',
          poBonusQuantity: '',
          poDetailId: '',
          poGrnQuantity: '',
          poIds: '',
          poLeaseQuantity: '',
          poNumbersList: '',
          poOtherCharges: '',
          poPendingQuantity: '',
          poQuantity: '',
          poStagedList: '',
          poUom: '',
          poUomType: '',
          poUomTypeId: '',
          poUomUnit: '',
          poUomUnitId: '',
          prList: '',
          selectedTax: '',
          selectedTaxPercent: '',
          selectedUom: '',
          totalPoQty: '',
          uom: '',
          mrp: '',
          taxAmount: '',
          pendingQuantity: ''
        };
        self.model.itemDetails.push(obj);
      }
      var itemSearch = 'item-name_'+self.model.itemDetails.length;
      document.getElementById(itemSearch).focus();
    }

    var ConfirmAssetTypePopup1 = function () {
      self.model.poType = self.model.assetTypeNew;
      ClosePopup('close', 'confirmAssetTypePopup1');
      // self.model.itemDetails = [];
      $scope.roundOff = 0.00;
      self.model.totalNetAmount = 0.00;
      self.model.totalNetAmount1 = 0.00;
      var abc = 'abc_'+self.model.itemDetails.length;
      document.getElementById(abc).focus();
    }

    // direct request change confirmation
    var ConfirmValidation = function (prType,oldValue) {
      self.model.oldPrRequestType=oldValue;
      self.model.prRequestTypeId = prType;
      if (!!self.model.itemDetails.length) {
        OpenPopup('open', 'confirmValidation');
      } else {
        self.model.requestType = prType;
      }
    }
    var SetOldType=function () {
      self.model.prRequestTypeId =  self.model.oldPrRequestType;
    }
    var EmptyList = function () {
      self.model.requestType = self.model.prRequestTypeId;
      self.model.itemDetails = [];
      $scope.roundOff = 0;
      self.model.totalNetAmount1 = 0;
      self.model.totalOtherAmount = 0;

      ClosePopup('close', 'confirmValidation');
    }

    var discountCalc = function (item) {
      if (!parseFloat(item.discountPercent)) {
        item.discountPercent = 0;
      }
      if(isNaN(item.grnReceivedQuantity) || item.grnReceivedQuantity == undefined){
        item.grnReceivedQuantity = 0;
      }
      item.grnAmount = item.grnAmount || 0;
      if(item.poQuantity){
          item.grnAmount = item.costOfPurchase * item.poQuantity;
      }
      if(self.model.prRequestTypeId == 4){
        item.grnAmount = item.costOfPurchase * item.grnReceivedQuantity;
      }
      else{
        item.grnAmount = item.costOfPurchase * item.grnReceivedQuantity;
      }
      item.discountAmount = (item.grnAmount) * (item.discountPercent / 100);
      self.model.disAmount = item.discountAmount;
      item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
      item.taxAmount = ((item.grnAmount-self.model.disAmount) * (item.selectedTaxPercent/100));
      item.lcopAmount = (((item.grnAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + (item.receivedQtyBonusCalculation!=undefined && item.receivedQtyBonusCalculation!=null ? parseInt(item.receivedQtyBonusCalculation):0)));
      $timeout( function(){
        $scope.calculateRoundoff1();
        if(self.model.discountType == '2'){
          $scope.calculateAmountDiscount();
        }
      },1000);
    }

    var CalculateDiscOnQty = function (item, grnQty,inx) {
      item.discountPercent = item.discountPercent || 0;
      item.costOfPurchase = item.costOfPurchase || 0;
      grnQty = grnQty || 0;
      item.newPOQty=grnQty;
      var baseAmount = parseFloat(item.costOfPurchase) * parseInt(grnQty);
      item.discountPercent = ((parseFloat(item.discountAmount) / baseAmount) * 100);
      if(item.taxPercent == undefined){
        item.taxAmount = ((baseAmount - item.discountAmount) * (item.selectedTaxPercent/100));
      }
      else{
        item.taxAmount = ((baseAmount - item.discountAmount) * (item.taxPercent/100));
      }
      if(self.model.poType.id != 2){
        item.lcopAmount = (((baseAmount - item.discountAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + (item.receivedQtyBonusCalculation!=undefined && item.receivedQtyBonusCalculation!=null ? parseInt(item.receivedQtyBonusCalculation):0)));
      }
      else{
        item.lcopAmount = (((baseAmount - item.discountAmount) + item.taxAmount) / (parseInt(item.grnReceivedQuantity) + parseInt(item.bonusReceivedQuantity)));
      }
      if(self.model.discountType != 2){
        item.discountAmount = baseAmount * (item.discountPercent / 100);
        item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
      }
      $timeout( function(){
        $scope.calculateRoundoff1();
        if(self.model.discountType == '2'){
          $scope.calculateAmountDiscount();
        }
      },1000);
      if(self.model.poType.id == 1){
    	 var inxNew  = parseFloat(inx)+1;
        var idbatch = 'batch_'+inxNew;
        document.getElementById(idbatch).focus();
      }
      if(self.model.poType.id == 2){
        var idbatchs = 'batchs_'+self.model.itemDetails.length;
        document.getElementById(idbatchs).focus();
      }
    }

    $scope.calculateAmountDiscount = function(){
      self.model.finalDiscount = self.model.totalDiscount.toFixed(2);
      $rootScope.stopLoader();
    }

    var OpenChildItems = function () {
      PopulateAssetCategoryDropdown();
      self.model.SelectedItemsIds = [];
      self.model.items = [];
    }

    var validate = function () {
      var isValidAccItem = true;
      if(self.model.SelectedItemsIds.length == 0){
        growl.error("Please select item..");
        isValidAccItem = false;
      }
      return isValidAccItem;
    }

    var AddChildItems = function (x) {
      if(validate()){
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id
        };

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          response.data.forEach(function (value, key) {
            self.model.accessoryList = [];
            // var isFound = self.model.accessoryList.find(function (x) {
            // return x.itemId == value.itemId;
            // });

            // if (!isFound) {
              self.model.accessoryList.push(value);
            // }
          });

          var popup = angular.element('#childItems');
          popup.modal('hide');

          self.model.SelectedItemsIds = [];
          self.model.items = [];

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });    
      }
    }

    var AddAccessories = function (item) {
      if($scope.grnId == 0 || $scope.grnId == null){
        self.model.itemDetails.forEach(function (x) {
          if (x.itemId == item.itemId) {
            $scope.itemForAccessories = item;
          }
        });      
     
        // self.model.accessoryList = [];
        if($scope.itemForAccessories.accessories && $scope.itemForAccessories.accessories.length > 0){
          angular.forEach($scope.itemForAccessories.accessories,function(accessory){
            if(!accessory.serialNo || !accessory.warrentyFrom || !accessory.warrentyTo || !accessory.quantity){
              self.model.accessoryList.push(accessory);
            }
          });
        }
      }
      else{
        self.model.accessoryList = $scope.accessoryList;
      }
    }

    var ViewHistory = function (item) {
      var grn = self.model;

      var URI = CONSTANTS.GLOBAL.PO_HISTORY + $scope.unitId + '/' + item.itemId+ '/' + item.poId;

      $rootScope.stopLoaderPopup();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          grn.lastOrderDetails = response.data;
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
        });
    }

    var ViewLastGrnDetails = function (item) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.PRE_GRN_HISTORY + self.model.vendorId + "/" + item.itemId;
      $rootScope.stopLoaderPopup();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastGRNDetailsList = response.data;
          angular.forEach(self.model.lastGRNDetailsList,function(value, key) {
        	  if(value.expirydate != undefined && value.expirydate != null){
        		  if(value.expirydate.toString().includes("-"))
                  {
        			  value.stringDate=true;
                  }
        	  }
          });
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
        });
    }
    $scope.checkInvoiceDate = function (){
    	self.model.dueDate=new Date(self.model.dueDate.getTime() + ($scope.creditDays)*24*60*60*1000);
    }
    var GetCreditNoteAmount = function (){
      self.model.creditNoteAmountList = [];
      self.model.creditNoteAmountListTest = [];
      var URI =  CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.GET_CREDIT_NOTE_AMOUNT_API +  self.model.vendorId;
      $rootScope.startLoaderPopup();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	$scope.totalAvailableNNetAmount = 0;
        	$scope.totalConsumedAmount=0;
        	$scope.creditDays=0;
        	$scope.isConsignmentFunctionCalled=true;
        	angular.forEach(self.model.addCreditNoteList, function(value2){
              if(value2.id==undefined)
              {
            	  $scope.totalConsumedAmount +=  parseFloat(value2.consumenetamount);
              }
        	});
            angular.forEach(response.data, function(item){
            	$scope.creditDays=item.creditDays;
            	if(item.rgDetails!=undefined && item.rgDetails!=null && item.rgDetails.length>0)
            		{
            		var details=item.rgDetails;
            	item.rgDetails = item.rgDetails.filter((value, index, self) =>
            	  index === self.findIndex((t) => (
            	    t.mateinwardid === value.mateinwardid
            	  ))
            	)
            	angular.forEach(item.rgDetails, function(rgItems){
                    let crew ;
                    if(item.rgDetails.length>1)
                    {
                         crew = details.filter(person => {
                		return person.mateinwardid == rgItems.mateinwardid;
                	}).reduce( (prevVal,currVal,idx) => {
                		return idx == 1 ? prevVal.rgnumber + ',' + currVal.rgnumber : prevVal + ',' + currVal.rgnumber;
                	});
                        rgItems.rgnumber=crew;
                    }
                    
            	});
            		}
            	
                angular.forEach(item.rgDetails, function(rgItems){
                    rgItems.inwordno = item.inwordno;
                    rgItems.mateinwardid = item.mateinwardid;
                    rgItems.replacenumber = item.replacenumber;
                    rgItems.creditnoteamount = parseFloat(item.creditnoteamount).toFixed(2);
                    rgItems.availablenetamount = item.availablenetamount;
                    rgItems.availablenetamounttetet = item.availablenetamount;
                    rgItems.creditnetamount = item.creditnetamount;
                    rgItems.consumenetamount = 0;
                    self.model.creditNoteAmountListTest.push(rgItems);
                       
                });
            });
            self.model.dueDate=new Date(self.model.dueDate.getTime() + ($scope.creditDays)*24*60*60*1000);
          angular.forEach(response.data, function(item){
            angular.forEach(item.rgDetails, function(rgItems){
              rgItems.inwordno = item.inwordno;
              rgItems.mateinwardid = item.mateinwardid;
              rgItems.replacenumber = item.replacenumber;
              rgItems.creditnoteamount = item.creditnoteamount;
              rgItems.availablenetamount = item.availablenetamount;
              //rgItems.creditnetamount = item.creditnetamount;
              rgItems.creditnetamount = parseFloat(item.creditnoteamount).toFixed(2);
              rgItems.consumenetamount = 0;
              if(rgItems.availablenetamount != 0){
                if(self.model.addCreditNoteList == undefined){
                  self.model.creditNoteAmountList.push(rgItems);
                 $scope.totalAvailableNNetAmount += parseFloat(rgItems.availablenetamount);
                  console.log("creditNoteAmountList................", self.model.creditNoteAmountList);
                }
                else{
                /*	if(!$scope.id){
                		   if(self.model.creditNoteAmountList.consumenetamount!=0){*/
                	
                         	  angular.forEach(self.model.addCreditNoteList, function(addeditem){
                         		 if(addeditem.id == undefined){
                         		  if( rgItems.mateinwardid == addeditem.mateinwardid){
                                        rgItems.availablenetamount =  rgItems.availablenetamount -  parseFloat(addeditem.consumenetamount).toFixed(2);
                                   }
                         	  }
                               });
                			$scope.totalAvailableNNetAmount += parseFloat(rgItems.availablenetamount);
     		                 self.model.creditNoteAmountList.push(rgItems);
     		                  console.log("creditNoteAmountList................", self.model.creditNoteAmountList);
                     /*}
                	}*/
                }
              }
            });
          });
         if(self.model.addCreditNoteList){
            angular.forEach(self.model.addCreditNoteList, function(data){
              if(data.isSelected == true){
                angular.forEach(response.data, function(i){
                  if(data.mateinwardid == i.mateinwardid){
                    i.isSelected = true;
                  }
                });
              }
            });
//            angular.forEach(response.data, function(r){
//            	if(!r.isSelected){
//              	  if(self.model.creditNoteAmountList.consumenetamount==0 || !self.model.creditNoteAmountList.consumenetamount){
//                  self.model.creditNoteAmountList.push(r);
//              	  }
//                }
//            })
          }
          self.model.creditNoteAmountList.consumenetamount = 0;
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
      });
    }

    var selectAllCreditNoteAmount = function () {
      if (self.model.selectAllCreditRows) {
        self.model.isSelectedCredit=[];
        self.model.SelectedCreditIds = self.model.creditNoteAmountList.map(function (item) {
          item.isSelectedCredit = true;
          item.isChecked = true;
          self.model.isSelectedCredit.push(item);
          return item.id;
        });
      } else {
        self.model.SelectedCreditIds = [];
        self.model.creditNoteAmountList.forEach(function (item) {
          item.isSelectedCredit = false;
          item.isChecked = false;
        });
      }
    }

    var selectCreditNoteAmount = function (item) {
      self.model.creditItemIds = [];
      if (item.isSelectedCredit) {
        item.isSelectedCredit = true;
        item.isChecked = true;
      } else {
       item.isSelectedCredit = false;
       item.isChecked = false;
      }
    }

    var addCreditNote = function(){
      if(cnAmountValidation()){
        if(!self.model.addCreditNoteList){
          self.model.addCreditNoteList = [];
        }
        var unCheckedArray=[];
        self.model.creditNoteAmountList.forEach(function(data){

        	var totalConsumedNetAmt = 0;
        	
          if(data.isChecked){
        	    totalConsumedNetAmt = totalConsumedNetAmt +parseFloat(data.consumenetamount);

                if(totalConsumedNetAmt > self.model.totalNetAmount || totalConsumedNetAmt > self.model.totalNetAmount1){
                    growl.warning('Amount should not be greater than net amount.');
                    unCheckedArray.push(data);
                    return false;
                }
                else{
                	
                    data.availablenetamount = data.availablenetamount -  parseFloat(data.consumenetamount).toFixed(2);
                    self.model.addCreditNoteList.push(data);
//                  	            var index = self.model.creditNoteAmountList.indexOf(data);
//                  	            self.model.creditNoteAmountList.splice(index, 1);
                }

          }
          else
        	  {
        	  unCheckedArray.push(data);
        	  }
        });
        self.model.creditNoteAmountList=unCheckedArray;
      }
    }

    
    $scope.getPandCard = function(item){
    	if(item.id!=undefined && item.id!=null)
        {
        
		var URI = BASE_URL + ROOT_URL + "/billing/PanCard/inwardDataupdate/"+item.consumenetamount+"/"+item.mateinwardid+"/"+item.id; //1 for Deposit screen
		BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if (response.data.status == "success")
			{
				
			} 

		});
        }
	}
    
    $scope.itemRemoved=false;
    var removeCreditNote = function (item) {
      var index = self.model.addCreditNoteList.indexOf(item);
      self.model.addCreditNoteList.splice(index, 1);
      $scope.itemRemoved=true;
     if(item.mateinwardid == undefined){
    	  var obj = {
    		        "inwordno": '',
    		        "rgnumber": '',
    		        "replacenumber": '',
    		        "creditnetamount": '',
    		        "availablenetamount": '',
    		        "consumenetamount": ''
    		      }
    	  		
    		      obj.inwordno = item.inwordno;
		    	  obj.rgnumber = item.rgnumber;
		    	  obj.replacenumber = item.replacenumber;
		    	  obj.creditnetamount = item.creditnetamount;
		    	  obj.availablenetamount = item.availablenetamount;
		    	  obj.consumenetamount = item.consumenetamount;
		    	  obj.availablenetamount = item.availablenetamount +  parseFloat(item.consumenetamount);
		    	  obj.availablenetamounttetet = item.availablenetamount +  parseFloat(item.consumenetamount);
		    	  obj.consumenetamount = "";
		    	  self.model.creditNoteAmountList.push(obj);
      }else{
    	  var counter=0;
    	  var savedCounter=0;
    	  angular.forEach(self.model.creditNoteAmountList, function (val , key) {
    		  
    		  if(item.inwordno == val.inwordno){
    			  val.creditnoteamount = val.creditnoteamount - parseFloat(item.consumenetamount);
    			  val.availablenetamount = val.availablenetamount + parseFloat(item.consumenetamount); 
    			  val.availablenetamounttetet = val.availablenetamount;
    			  val.consumenetamount = 0;
    			  savedCounter++;
    		  }
    		  else
    			  {
    			  counter++;
    			  }
    	  });
    	  if(item.inwordno!=undefined && item.inwordno!=null && item.inwordno!=''&& self.model.creditNoteAmountList.length==0)
          {
              counter++;
          }
    	  if(counter>0 && savedCounter==0)
    		  {
    		  var obj = {
	    		        "inwordno": '',
	    		        "rgnumber": '',
	    		        "replacenumber": '',
	    		        "creditnetamount": '',
	    		        "availablenetamount": '',
	    		        "consumenetamount": ''
	    		      }
	    	  		
	    		      obj.inwordno = item.inwordno;
			    	  obj.rgnumber = item.rgnumber;
			    	  obj.replacenumber = item.replacenumber;
			    	  obj.creditnetamount = item.creditnetamount;
			    	  obj.availablenetamount = item.availablenetamount;
			    	  obj.consumenetamount = item.consumenetamount;
			    	  obj.availablenetamount = item.availablenetamount +  parseFloat(item.consumenetamount);
			    	  obj.availablenetamounttetet = item.availablenetamount +  parseFloat(item.consumenetamount);
			    	  obj.consumenetamount = 0;
			    	  self.model.creditNoteAmountList.push(obj);
    		  }
    	  $scope.getPandCard(item);
    	  
      }
     
    }

    var saveCreditNoteAmount = function(){
      var popup = angular.element('#creditNoteAmount_Modal');
      popup.modal('hide');
      $scope.calculateRoundoff1();
      $scope.totalAvailableNNetAmount = 0;
      $scope.totalAvailablexxsxsAmount = 0;
      $scope.totalconsumenetamountssssssdsAmount = 0;
      
      self.model.creditNoteAmountListTest.forEach(function(value){
        $scope.totalAvailablexxsxsAmount += parseFloat(value.availablenetamounttetet);
      });
      self.model.addCreditNoteList.forEach(function(value2){
//    	  if(value2.id==undefined && $scope.itemRemoved==false)
//    		  {
          $scope.totalconsumenetamountssssssdsAmount +=  parseFloat(value2.consumenetamount);
//    		  }
          });
      $scope.totalAvailableNNetAmount =  $scope.totalAvailablexxsxsAmount - $scope.totalconsumenetamountssssssdsAmount;
    }

    var ValidateAccessories = function (list) {
      var isValid = true;
      angular.forEach(self.model.accessoryList, function (key) {
        if (!key.serialNumber) {
          isValid = false;
          key.isSerialValid = true;
          growl.error('Please enter valid serial number');
        }

        // if (!key.warrantyFrom) {
        // isValid = false;
        // key.isWarrantyFromValid = true;
        // growl.error('Please enter valid warranty from date');
        // }

        // if (!key.warrantyTo) {
        // isValid = false;
        // key.isWarrantyToValid = true;
        // growl.error('Please enter valid warranty to date');
        // }

        if (!key.quantity) {
          isValid = false;
          key.isQuantityValid = true;
          growl.error('Please enter valid quantity');
        }
      });

      return isValid;
    }

    var SaveAccessory = function () {
      if (ValidateAccessories()) {
        self.model.itemDetails.forEach(function(data){
        	data.accessories ? data.accessories: data.accessories = [];
          self.model.accessoryList.forEach(function (value) {
            if($scope.itemForAccessories.itemId == data.itemId){
              // data.accessories = [];
              // $scope.itemForAccessories.accessories.push(value);
            	
            	// start code for HIS-9505 GRN form asset against PO issues -
				// Approve quantity is showing 0
            	let index = data.accessories.findIndex(obj => obj.itemId == value.itemId);
                if (index > -1) {
                	data.accessories.splice(index, 1);
                }
                // end code for HIS-9505 GRN form asset against PO issues -
				// Approve quantity is showing 0
            	
              data.accessories.push({
                "itemId": value.itemId,
                "itemCode": value.itemCode,
                "itemName": value.itemName,
                "specification": value.specification,
                "serialNo": value.serialNumber,
                "warrentyFrom": moment(value.warrantyFrom).format('YYYY-MM-DD'),
                // "warrentyTo": moment(value.warrentyTo).format('YYYY-MM-DD'),
                // change code for resolved HIS-10538 'Warranty To' date showing
				// incorrect on Accessories View
                "warrentyTo": moment(value.warrantyTo).format('YYYY-MM-DD'),
                
                "quantity": parseInt(value.quantity)
              });
            }
          });
        });

        // itemForAccessories.accessories.forEach(function (item) {
          
        // });
        self.model.ClosePopup('Close', 'accessoryAddModal');
        growl.success("Save Successfully");
      }
    }

    var ViewAccessory = function (item) {
      self.model.itemDetails.forEach(function (key) {
        if (key.itemId == item.itemId) {
          self.model.getAccessoryList = item.accessories;
        }
      });
    }

    var SaveTerms = function (viewModal, item) {
      $scope.idx = self.model.itemDetails.length; 
      self.model.itemTerms = self.model.itemDetails[$scope.idx - 1];
      switch (viewModal) {
        case "warrantyPeriodModal":
          self.model.itemTerms.warrantyPeriod = self.model.warrantyPeriod;
          self.model.itemTerms.warrentyPeriod = self.model.warrantyPeriod;
          self.model.itemTerms.isValidWarrantyTerm = false;
          ClosePopup('Close', 'warrantyPeriodModal');
          OpenPopup('Open', 'expectedSuportTermsModal');
          break;
        case "expectedSuportTermsModal":
          self.model.itemTerms.supportTerms = self.model.supportTerms;
          self.model.itemTerms.isValidSupportTerm = false;
          ClosePopup('Close', 'expectedSuportTermsModal');
          OpenPopup('Open', 'expectedPaymentTermsModal');
          break;
        case "expectedPaymentTermsModal":
          self.model.itemTerms.paymentTerms = self.model.paymentTerms;
          self.model.itemTerms.expectedPayment = self.model.paymentTerms;
          self.model.itemTerms.isValidPaymentTerm = false;
          ClosePopup('Close', 'expectedPaymentTermsModal');
          OpenPopup('Open', 'expectedTechSpecificationModal');
          break;
        case "expectedTechSpecificationModal":
          self.model.itemTerms.technicalSpecs = self.model.technicalSpecs;
          self.model.itemTerms.technicalSpecification = self.model.technicalSpecs;
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
              self.model.warrantyPeriod = x.warrantyPeriod || x.warrentyPeriod;
            }
          });
          break;
        case "expectedSuportTermsModal":
          self.model.itemDetails.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.supportTerms = x.supportTerms;
            }
          });
          $timeout(function(){
            var expPay = 'expPay_'+self.model.itemDetails.length;
            document.getElementById(expPay).focus();
          },3000);
          break;
        case "expectedPaymentTermsModal":
          self.model.itemDetails.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.paymentTerms = x.paymentTerms || x.expectedPayment;
            }
          });
          $timeout(function(){
            var expTec = 'expTec_'+self.model.itemDetails.length;
            document.getElementById(expTec).focus();
          },3000);
          break;
        case "expectedTechSpecificationModal":
          self.model.itemDetails.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.technicalSpecs = x.technicalSpecs || x.technicalSpecification;
            }
          });
          break;
      }
    }

      var FillStagedQuantity = function (items, item, list, grnlist) {
      var totalQuantity = 0
        $scope.receivedQty1 = 0;
      	$scope.activeStagedQty=0;
//       angular.forEach(items.filter(x => x != item && x.poId === item.poId), function (value, key) {
        angular.forEach(item.grnStagedLists, function (dt, key) {
          if (dt.id) {
        	  if(item.isThreeUOMApplicable==true)
    		  {
    	      if(item.grnUomTypeId==1)
    		  {
    	    	  dt.stagedReceivedQuantity=dt.originalStagedReceivedQuantity*(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
    		  }
	    	  if(item.grnUomTypeId==2)
			  {
	    		  dt.stagedReceivedQuantity=dt.originalStagedReceivedQuantity*(item.uom[1].conversion * item.uom[2].conversion)
			  }
	    	  if(item.grnUomTypeId==3)
			  {
	    		  dt.stagedReceivedQuantity=dt.originalStagedReceivedQuantity*(item.uom[2].conversion)
			  }
    		  }
    	  else
    		  {
    		  if(item.grnUomTypeId==1)
    		  {
    			  dt.stagedReceivedQuantity=dt.originalStagedReceivedQuantity*(item.uom[0].conversion * item.uom[1].conversion)
    		  }
	    	  if(item.grnUomTypeId==3)
			  {
	    		  dt.stagedReceivedQuantity=dt.originalStagedReceivedQuantity*(item.uom[1].conversion)
			  }
    		  }
    	  if(item.selectedUom!=undefined && item.selectedUom!=null)
    		  {
    	  if(item.isThreeUOMApplicable==true)
		  {
	      if(item.selectedUom.uomTypeId==1)
		  {
	    	  dt.stagedReceivedQuantity=dt.stagedReceivedQuantity/(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==2)
		  {
    		  dt.stagedReceivedQuantity=dt.stagedReceivedQuantity/(item.uom[1].conversion * item.uom[2].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==3)
		  {
    		  dt.stagedReceivedQuantity=dt.stagedReceivedQuantity/(item.uom[2].conversion)
		  }
		  }
	  else
		  {
		  if(item.selectedUom.uomTypeId==1)
		  {
			  dt.stagedReceivedQuantity=dt.stagedReceivedQuantity/(item.uom[0].conversion * item.uom[1].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==3)
		  {
    		  dt.stagedReceivedQuantity=dt.stagedReceivedQuantity/(item.uom[1].conversion)
		  }
		  }
          }
          if(dt.isEditable==undefined || dt.isEditable==null || dt.isEditable==false )
        	  {
        	  $scope.receivedQty1 = $scope.receivedQty1 + dt.stagedReceivedQuantity;
        	  }
            totalQuantity = totalQuantity + !!dt.stagedReceivedQuantity ? parseInt(dt.stagedReceivedQuantity, 10) : 0;
          }
        });
//       });
        	if($scope.editQtyFlag==true)
        		{
        		self.model.totalPOQTY=item.poPendingQuantity;
        		}
        	else
        		{
        		self.model.totalPOQTY = item.poQuantity;
        		}
      // self.model.totalPendingQty = item.poPendingQuantity - totalQuantity;
      // self.model.totalPendingQty = item.poPendingQuantity - $scope.receivedQty1;
      self.model.totalPendingQty = item.poQuantity - $scope.receivedQty1;
      // self.model.totalRecQtyAgnGrn = item.poGrnQuantity;
      self.model.totalRecQtyAgnGrn = $scope.receivedQty1;
      self.model.stagedQtyListIndex = self.model.itemDetails.indexOf(item);
     
      angular.forEach(list, function(data){
        // data.poApprovedQuantity = self.model.totalPOQTY;
    	  data.poApprovedQuantity=data.orginalApprovedQty != undefined ? data.orginalApprovedQty : data.poApprovedQuantity;
    	  if((item.poUomTypeId!=undefined && item.poUomTypeId!=null) ||(item.poGrnUomTypeId!=undefined && item.poGrnUomTypeId!=null))
    		  {
    	  if(item.isThreeUOMApplicable==true)
    		  {
    	      if((item.poUomTypeId != undefined && item.poUomTypeId != null && item.poUomTypeId == 1) || (item.poGrnUomTypeId != undefined && item.poGrnUomTypeId != null && item.poGrnUomTypeId == 1))
    		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
    		  }
	    	  if((item.poUomTypeId != undefined && item.poUomTypeId != null && item.poUomTypeId ==2) || (item.poGrnUomTypeId != undefined && item.poGrnUomTypeId != null && item.poGrnUomTypeId == 2))
			  {
	    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[1].conversion * item.uom[2].conversion)
			  }
	    	  if((item.poUomTypeId != undefined && item.poUomTypeId != null && item.poUomTypeId ==3) || (item.poGrnUomTypeId != undefined && item.poGrnUomTypeId != null && item.poGrnUomTypeId == 3))
			  {
	    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[2].conversion)
			  }
    		  }
    	  else
    		  {
    		  if((item.poUomTypeId != undefined && item.poUomTypeId != null && item.poUomTypeId ==1) || (item.poGrnUomTypeId != undefined && item.poGrnUomTypeId != null && item.poGrnUomTypeId == 1))
    		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[0].conversion * item.uom[1].conversion)
    		  }
	    	  if((item.poUomTypeId != undefined && item.poUomTypeId != null && item.poUomTypeId ==3) || (item.poGrnUomTypeId != undefined && item.poGrnUomTypeId != null && item.poGrnUomTypeId == 3))
			  {
	    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[1].conversion)
			  }
    		  }
      }
      else
    	  {
    	  if(item.selectedUom!=undefined && item.selectedUom!=null)
    		  {
    	  if(item.isThreeUOMApplicable==true)
		  {
	      if(item.selectedUom.uomTypeId==1)
		  {
		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==2)
		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[1].conversion * item.uom[2].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==3)
		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[2].conversion)
		  }
		  }
	  else
		  {
		  if(item.selectedUom.uomTypeId==1)
		  {
		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[0].conversion * item.uom[1].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==3)
		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity*(item.uom[1].conversion)
		  }
		  }
    	  }
      }
    	  if(item.selectedUom!=undefined && item.selectedUom!=null)
    		  {
    	  if(item.isThreeUOMApplicable==true)
		  {
	      if(item.selectedUom.uomTypeId==1)
		  {
		  data.poApprovedQuantity=data.poApprovedQuantity/(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==2)
		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity/(item.uom[1].conversion * item.uom[2].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==3)
		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity/(item.uom[2].conversion)
		  }
		  }
	  else
		  {
		  if(item.selectedUom.uomTypeId==1)
		  {
		  data.poApprovedQuantity=data.poApprovedQuantity/(item.uom[0].conversion * item.uom[1].conversion)
		  }
    	  if(item.selectedUom.uomTypeId==3)
		  {
    		  data.poApprovedQuantity=data.poApprovedQuantity/(item.uom[1].conversion)
		  }
		  }
      }
    	  if(item.selectedUom!=undefined && item.selectedUom!=null)
    		  {
    	  if(item.poUomTypeId==undefined || item.poUomTypeId==null)
    	  {
    		  if(item.isThreeUOMApplicable==true)
    		  {
    	      if(item.poGrnUomTypeId==1)
    		  {
    	    	  self.model.totalPOQTY=self.model.totalPOQTY*(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
    		  }
	    	  if(item.poGrnUomTypeId==2)
			  {
	    		  self.model.totalPOQTY=self.model.totalPOQTY*(item.uom[1].conversion * item.uom[2].conversion)
			  }
	    	  if(item.poGrnUomTypeId==3)
			  {
	    		  self.model.totalPOQTY=self.model.totalPOQTY*(item.uom[2].conversion)
			  }
    		  }
    	  else
    		  {
    		  if(item.poGrnUomTypeId==1)
    		  {
    			  self.model.totalPOQTY=self.model.totalPOQTY*(item.uom[0].conversion * item.uom[1].conversion)
    		  }
	    	  if(item.poGrnUomTypeId==3)
			  {
	    		  self.model.totalPOQTY=self.model.totalPOQTY*(item.uom[1].conversion)
			  }
    		  }
    		  if(item.isThreeUOMApplicable==true)
    		  {
    	      if(item.selectedUom.uomTypeId==1)
    		  {
    	    	  self.model.totalPOQTY=self.model.totalPOQTY/(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
    		  }
	    	  if(item.selectedUom.uomTypeId==2)
			  {
	    		  self.model.totalPOQTY=self.model.totalPOQTY/(item.uom[1].conversion * item.uom[2].conversion)
			  }
	    	  if(item.selectedUom.uomTypeId==3)
			  {
	    		  self.model.totalPOQTY=self.model.totalPOQTY/(item.uom[2].conversion)
			  }
    		  }
    	  else
    		  {
    		  if(item.selectedUom.uomTypeId==1)
    		  {
    			  self.model.totalPOQTY=self.model.totalPOQTY/(item.uom[0].conversion * item.uom[1].conversion)
    		  }
	    	  if(item.selectedUom.uomTypeId==3)
			  {
	    		  self.model.totalPOQTY=self.model.totalPOQTY/(item.uom[1].conversion)
			  }
    		  }
           self.model.totalPendingQty = self.model.totalPOQTY - $scope.receivedQty1;
    	  }
      }
      });
      self.model.poStagedList = list;
      self.model.grnStagedList = grnlist;
      var stagedList=[];
      $scope.grnIssuedQty=0;
      angular.forEach(self.model.grnStagedList, function(grn){
    	  if(grn.isEditable==true)
    		  {
    		  stagedList.push(grn);
    		  }
    	  else
    		  {
    		  $scope.grnIssuedQty=$scope.grnIssuedQty+parseInt(grn.stagedReceivedQuantity);
    		  }
      });
      self.model.grnStagedList=stagedList;
      angular.forEach(self.model.grnStagedList, function(grn){
    	  if(grn.id && $scope.receivedQty1 && grn.isEditable==true){
    		  grn.stagedReceivedQuantity = item.grnReceivedQuantity!=undefined && item.grnReceivedQuantity!=null ? item.grnReceivedQuantity :$scope.receivedQty1;
              if($scope.editQtyFlag==true)
            	  {
    		  self.model.totalPendingQty = self.model.totalPOQTY - item.grnReceivedQuantity;
            	  }
	        }
    	  else
    		  {
    		  if(grn.stagedReceivedQuantity)
    			  {
    			  if(item.poUomTypeId!=undefined && item.poUomTypeId!=null)
    	    	  {
    				  //if(item.selectedUom.uomTypeId != item.poUomTypeId){
		    	    		  if(item.isThreeUOMApplicable==true)
		    	    		  {
		    	    	      if(item.poUomTypeId==1)
		    	    		  {
		    	    	    	  self.model.totalPOQTYs=item.grnReceivedQuantity*(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
		    	    		  }
		    		    	  if(item.poUomTypeId==2)
		    				  {
		    		    		  self.model.totalPOQTYs=item.grnReceivedQuantity*(item.uom[1].conversion * item.uom[2].conversion)
		    				  }
		    		    	  if(item.poUomTypeId==3)
		    				  {
		    		    		  self.model.totalPOQTYs=item.grnReceivedQuantity*(item.uom[2].conversion)
		    				  }
		    	    		  }
		    	    	  else
		    	    		  {
		    	    		  if(item.poUomTypeId==1)
		    	    		  {
		    	    			  self.model.totalPOQTYs=item.grnReceivedQuantity*(item.uom[0].conversion * item.uom[1].conversion)
		    	    		  }
		    		    	  if(item.poUomTypeId==3)
		    				  {
		    		    		  self.model.totalPOQTYs=item.grnReceivedQuantity*(item.uom[1].conversion)
		    				  }
		    	    		  }
		    	    		  
		    	    		  if(item.isThreeUOMApplicable==true)
		    	    		  {
		    	    	      if(item.selectedUom.uomTypeId==1)
		    	    		  {
		    	    	    	  self.model.totalPOQTYN=self.model.totalPOQTYs/(item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion)
		    	    		  }
		    		    	  if(item.selectedUom.uomTypeId==2)
		    				  {
		    		    		  self.model.totalPOQTYN=self.model.totalPOQTYs/(item.uom[1].conversion * item.uom[2].conversion)
		    				  }
		    		    	  if(item.selectedUom.uomTypeId==3)
		    				  {
		    		    		  self.model.totalPOQTYN=self.model.totalPOQTYs/(item.uom[2].conversion)
		    				  }
		    	    		  }
		    	    	  else
		    	    		  {
		    	    		  if(item.selectedUom.uomTypeId==1)
		    	    		  {
		    	    			  self.model.totalPOQTYN=self.model.totalPOQTYs/(item.uom[0].conversion * item.uom[1].conversion)
		    	    		  }
		    		    	  if(item.selectedUom.uomTypeId==3)
		    				  {
		    		    		  self.model.totalPOQTYN=self.model.totalPOQTYs/(item.uom[1].conversion)
		    				  }
		    	    		  }
		    	    		  grn.stagedReceivedQuantity=parseInt(self.model.totalPOQTYN);
		    	    		  /*}else{
		    	                  grn.stagedReceivedQuantity=parseInt(grn.stagedReceivedQuantity);

		    	    		  }*/
    	    	          }
    			  
    			  
    			   
    			
    			  }
    		  else
    			  {
    			  grn.stagedReceivedQuantity = self.model.totalPendingQty;
    			  }
    		  }
      });
//      if(item.grnReceivedQuantity!=undefined && item.grnReceivedQuantity!=null)
//    	  {
//    	  self.model.totalPendingQty=self.model.totalPOQTY-item.grnReceivedQuantity;
//    	  self.model.totalRecQtyAgnGrn=item.grnReceivedQuantity;
//    	  }
    }


    var validateRecQty = function (data) {
      angular.forEach(self.model.grnStagedList, function (d){
        if(self.model.grnStagedList.length > 1){
          if(data.stagedReceivedQuantity > self.model.totalPendingQty){
            growl.error('Received quantity should not greater than total pending quantity');
            data.stagedReceivedQuantity = 0;
            return false;
          }
          else{
            self.model.isSaveStaged = false;
          }
        }
        else{
        	var issuedQty=0;
        	if($scope.editQtyFlag==false)
        		{
        		issuedQty=issuedQty+($scope.grnIssuedQty!=undefined && $scope.grnIssuedQty!=null ? $scope.grnIssuedQty:0)
        		}
          if (data.stagedReceivedQuantity > (self.model.totalPOQTY-issuedQty)) {
            self.model.isSaveStaged=true;
            growl.error('Received quantity should not greater than total pending quantity');
            data.stagedReceivedQuantity = 0;
            return false;
          }
          else{
            self.model.isSaveStaged=false;
          }
        }
      });
    }

    var ValidateWarrantyDate = function (item) {
      var isValid = true;
      if (item.warrantyDate > item.warrantyDateTo) {
        growl.error('Warranty From date should be lesser than Warranty to date');
        item.warrantyDate = '';
        item.warrantyDateTo = '';
        isValid = false;
      }
      if(item.warrantyDateTo){
        OpenPopup('Open', 'warrantyPeriodModal');
        // ViewTerms('warrantyPeriodModal', item);
      }
      return isValid;
    }

    var ValidateWarrantyFromDate = function (item) {
        var isValid = true;
        if (item.warrantyDate > item.warrantyDateTo) {
          growl.error('Warranty From date should be lesser than Warranty to date');
          item.warrantyDate = '';
          isValid = false;
        }
        return isValid;
      }
    
    var receivedQtyChange = function (data) {
      data.isEditable = true;
    }

    var SaveStagedQuantity = function () {
      var total = 0;
      var totalPoApprovedQuantity = 0;
      var itemIndex = self.model.itemDetails[self.model.stagedQtyListIndex];
      angular.forEach(self.model.grnStagedList, function (value) {
        total = total + parseInt(!!value.stagedReceivedQuantity ? value.stagedReceivedQuantity : 0);
        value.originalStagedReceivedQuantity=parseInt(value.stagedReceivedQuantity);
        if (value.isEditable) {
          itemIndex.grnReceivedQuantity = value.stagedReceivedQuantity;
          // itemIndex.grnReceivedQuantity = total;
        }
      });

      angular.forEach(self.model.poStagedList, function (value) {
        totalPoApprovedQuantity = totalPoApprovedQuantity + parseInt(value.poApprovedQuantity);
      });
      var abc = self.model.grnStagedList.find(function (val) {
        if (val.isEditable) {
          if (val.stagedReceivedQuantity > self.model.totalPOQTY) {
            return true;
          }
          else
          return false;
        }
      });
      if (!total) {
        self.model.grnReceivedQuantity[self.model.stagedQtyListIndex] = true;
        growl.error('Please enter valid received quantity');
        return false;
      }
      else if (abc) {
        self.model.isSaveStaged=true;
        growl.error('Received quantity should not greater than total pending quantity');
        return false;
      }
      /*
		 * else if (total > totalPoApprovedQuantity) { growl.error('Received
		 * quantity should not greater than PO quantity'); return false; }
		 */
      else {
        self.model.grnReceivedQuantity[self.model.stagedQtyListIndex] = false;
        $timeout( function(){
          $scope.calculateRoundoff1();
          if(self.model.discountType == '2'){
            $scope.calculateAmountDiscount();
          }
        },1000);
        self.model.ClosePopup('close', 'AddQtyModal_ETOitemConfig_one');
      }
      itemIndex.grnAmount = itemIndex.costOfPurchase * itemIndex.grnReceivedQuantity;
      if(self.model.discountType != 2){
        itemIndex.discountAmount = parseFloat((itemIndex.grnAmount) * (itemIndex.discountPercent / 100)).toFixed(2);
        self.model.disAmount = parseInt(itemIndex.discountAmount);
      }
      self.model.itemDetails[self.model.stagedQtyListIndex].lcopAmount = (((itemIndex.grnAmount - self.model.disAmount) + itemIndex.taxAmount) / (parseInt(itemIndex.receivedQtyCalculation) + (itemIndex.receivedQtyBonusCalculation!=undefined && itemIndex.receivedQtyBonusCalculation!=null ? parseInt(itemIndex.receivedQtyBonusCalculation):0)));
      $scope.calculateReceivedQty(itemIndex);
    }

    $scope.calculateReceivedQty = function (item) {
      if(item.isThreeUOMApplicable == false){
        if(item.selectedUom.uomTypeId == 1){
          item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.uomListdata[0].conversion);
          item.receivedQtyCalculation = (item.grnReceivedQuantity * item.uomListdata[0].conversion);
          item.uomUnitData = item.uomListdata[2].uomUnit;
        }
        else if(item.selectedUom.uomTypeId == 2){
        
        }
        else{
          item.receivedQtyCon = (item.grnReceivedQuantity);
          item.receivedQtyCalculation = (item.grnReceivedQuantity);
          item.uomUnitData = item.uomListdata[2].uomUnit;
        }
      }
      else{
        if(item.selectedUom.uomTypeId == 1){
          item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.grnReceivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else if(item.selectedUom.uomTypeId == 2){
          item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.grnReceivedQuantity * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else{
          item.receivedQtyCon = (item.grnReceivedQuantity);
          item.receivedQtyCalculation = (item.grnReceivedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
        }
      }
    }
    

    var fillOtherChargesListShow = function (item) {
      if (!!item.id) {
        self.model.otherChargesListShow = item.otherCharges;
      } else {
        self.model.otherChargesListShow = item.poOtherCharges;
      }
    }

    var fillOtherChargesList = function (item) {
      angular.forEach(item, function(data){
        if(data.selectedOtherCharges != undefined){
          angular.forEach(data.selectedOtherCharges, function (value) {
            value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
              return key.id == value.otherChargesId;
            });
          });
          data.selectedOtherCharges.forEach(function (othercharge) {
            var amountNumber = parseFloat(othercharge.amount);
            if (amountNumber == 0) {
              amountNumber = 0;
            } else {
              othercharge.amount = amountNumber.toFixed(2);
              othercharge.savedAmount = amountNumber.toFixed(2);
            }
          });
        }
        else if($scope.otherchargesmapper.length > 0){
        	if($scope.otherchargesmapper!=undefined && $scope.otherchargesmapper!=null && $scope.selectedOtherCharges!=undefined && $scope.selectedOtherCharges!=null && $scope.otherchargesmapper.length!=$scope.selectedOtherCharges.length && $scope.otherchargesmapper.length<=$scope.selectedOtherCharges.length)
        		{
        		$scope.otherchargesmapper=$scope.selectedOtherCharges;
        		}
          $scope.selectedOtherCharges = [];
          angular.forEach($scope.otherchargesmapper, function(data){
            var obj = {
              otherChargesId: '',
              amount: ''
            }
            obj.otherChargesId = data.otherChargesId;
            obj.amount = data.amount;
            $scope.selectedOtherCharges.push(obj);
            // data['selectedOtherCharges'] = [{ otherChargesId:
			// data.otherChargesId, amount: data.amount }];
            angular.forEach($scope.selectedOtherCharges, function (value) {
              value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
                return key.id == value.otherChargesId;
              });
            });
            $scope.selectedOtherCharges.forEach(function (othercharge) {
              var amountNumber = parseFloat(othercharge.amount);
              if (amountNumber == 0) {
                amountNumber = 0;
              } else {
                othercharge.amount = amountNumber.toFixed(2);
                othercharge.savedAmount = amountNumber.toFixed(2);
              }
            });
            $scope.dataother = $scope.selectedOtherCharges;
          });
        }
        else{
          angular.forEach(item, function(data){
            data['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
            angular.forEach(data.selectedOtherCharges, function (value) {
              value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
                return key.id == value.otherChargesId;
              });
            });
            data.selectedOtherCharges.forEach(function (othercharge) {
              var amountNumber = parseFloat(othercharge.amount);
              if (amountNumber == 0) {
                amountNumber = 0;
              } else {
                othercharge.amount = amountNumber.toFixed(2);
                othercharge.savedAmount = amountNumber.toFixed(2);
              }
            });
          });
        }
        if(data.selectedOtherCharges){
          self.model.otherChargesList = data.selectedOtherCharges;
        }
        else{
          self.model.otherChargesList = $scope.dataother;
        }
        if(self.model.otherChargesList==undefined || self.model.otherChargesList==null)
        	{
        	self.model.otherChargesList=[{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
        	}
        self.model.selectedOtherChargeIndex = self.model.itemDetails.indexOf(data);
        if(self.model.otherChargesList){
          if(self.model.otherChargesList.length > 1){
            $scope.showMinus = true;
          }
          else{
            $scope.showMinus = false;
          }
        }
      });
    }

    var AddOtherCharge = function (ocId, index) {
      otherCharge: self.model.otherChargesDropdownList[0]
      self.model.otherChargesList.push({ otherChargesId: ocId, amount: 0 });
      self.model.otherChargesList[index + 1].otherCharge = self.model.otherChargesDropdownList[0];
      if(self.model.otherChargesList.length > 1){
        $scope.showMinus = true;
      }
      else{
        $scope.showMinus = false;
      }
    }

    var lessOtherCharge = function (item, idx) {
      var index = self.model.otherChargesList.indexOf(item);
      self.model.otherChargesList.splice(index, 1);
      if(self.model.otherChargesList.length > 1){
        $scope.showMinus = true;
      }
      else{
        $scope.showMinus = false;
      }
    }


    var ValidateConsumableAmount = function (item, index) {
      var isValidItem = true;
      
      if(isNaN(item.grnReceivedQuantity) || item.grnReceivedQuantity==undefined || parseFloat(item.grnReceivedQuantity)==0 || item.grnReceivedQuantity=='')
      {
        self.model.grnReceivedQuantity[index] = true;
        isValidItem = false;
        growl.error('Please enter received quantity');
        return isValidItem;
      }
    	  item.cop=item.costOfPurchase;
      if (parseFloat(item.mrp) < parseFloat(item.costOfPurchase)) {
        growl.error('MRP should greater than COP');
        self.model.costOfPurchase[index] = true;
        self.model.mrp[index] = true;
        isValidItem = false;
      } else {
        self.model.costOfPurchase[index] = false;
        self.model.mrp[index] = false;
      }
      return isValidItem;
    }

    var cnAmountValidation = function (){
      var isValidItem = true;
      var count = 0;
      angular.forEach(self.model.creditNoteAmountList, function(item){
        if(item.isSelectedCredit){
          count = count + 1;
          if(item.consumenetamount == null || item.consumenetamount == undefined || item.consumenetamount == 0){
            growl.error('Enter consume net amount');
            isValidItem = false;
          } 
        }
      });

      if(count == 0){
        growl.error("Please select credit note");
        isValidItem = false;
      }
      return isValidItem;
    }

    var ValidateItemDiscount = function (item, index) {
      var isValidItem = true;
      if (!!parseFloat(item.discountPercent)) {
        if (parseFloat(item.discountPercent) > 100) {
          self.model.discountPercent[index] = true;
          growl.error('Disount should be less than 100%');
          isValidItem = false;
        }
      }

      return isValidItem;
    }

    var validateCharges = function () {
      var isValidItem = true;
      if (!self.model.otherChargesList.length) {
        growl.error('Please add other charges and amount');
        isValidItem = false;
      } else {
        angular.forEach(self.model.otherChargesList, function (charges) {

          charges['isChargesValid'] = false;
          charges['isAmountValid'] = false;

          if (charges.otherCharge.id) {
//            if (!parseFloat(charges.amount)) {
//              charges['isAmountValid'] = true;
//              growl.error('Please enter amount');
//              isValidItem = false;
//            }
          }
          if (parseFloat(charges.amount)) {
            if (!charges.otherCharge.id) {
              charges['isChargesValid'] = true;
              growl.error('Please select other charges');
              isValidItem = false;
            }
          }
        });
      }
      $rootScope.stopLoader();
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
        growl.error("Duplicate other charges");
      }

      return isValid;
    }

    $scope.cancelOtherCharges = function ()
    {
    	 var total = 0;
    	 var otherList=[];
         self.model.otherChargesList.forEach(function (value) {
        	 value.amount=(value.savedAmount!=undefined && value.savedAmount!=null ? value.savedAmount :0);
           value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
             if (value.otherCharge.id == key.id) {
               value.otherChargesId = key.id;
               return key;
             }
           });
         });

         angular.forEach(self.model.otherChargesList, function (value, key) {
           // delete value['otherCharge'];
           total = total + parseFloat(value.amount);
           if(value.amount!=undefined && value.amount!=null && value.amount>0)
        	   {
        	   otherList.push(value)
        	   }
         });
         self.model.otherChargesList=otherList;
         self.model.itemDetails[self.model.selectedOtherChargeIndex].validateOCFlag = false;
         self.model.itemDetails[self.model.selectedOtherChargeIndex].otherChargesTotal = total;
         self.model.otherChargesAmount = total;
         self.model.ClosePopup('close', 'otherCharges_Modal');
         calculateOtherChargesAmountForItem();
    }
    var sumOtherCharges = function (chargeList) {
      if (validateCharges() && DuplicateData(chargeList)) {
    	  $rootScope.startLoader();
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
          // delete value['otherCharge'];
          total = total + parseFloat(value.amount);
        });
        $scope.otherchargesmapper=self.model.otherChargesList;
        self.model.itemDetails[self.model.selectedOtherChargeIndex].validateOCFlag = false;
        self.model.itemDetails[self.model.selectedOtherChargeIndex].otherChargesTotal = total;
        self.model.otherChargesAmount = total;
        self.model.ClosePopup('close', 'otherCharges_Modal');
        $scope.editFlag=false;
        $rootScope.stopLoader();
        calculateOtherChargesAmountForItem();
      }
    }

    var fillPOQuantity = function (item) {
      item.stageQuantityList.forEach(function (x) {
        x.stagedDate = new Date(x.stagedDate);
      });
      self.model.stageQuantityList = item.stageQuantityList;
      self.model.selectedIndex = self.model.itemDetails.indexOf(item);
    }

    var addPOQuantity = function (date) {
      var qudate = moment(date).format('YYYY-MM-DD')
      self.model.stageQuantityList.push({ stagedDate: qudate, quantity: parseInt(0) });
    }

    var lessPOQuantity = function (item) {
      var index = self.model.stageQuantityList.indexOf(item);
      self.model.stageQuantityList.splice(index, 1);
    }

    var sumPOQuantity = function () {
      var total = 0;
      angular.forEach(self.model.stageQuantityList, function (value, key) {
        delete value['stagedQtyDate'];
        value['stagedDate'] = moment(value['stagedDate']).format('YYYY-MM-DD');
        self.model.grnReceivedQuantity[key] = false;
        if (!value.quantity) {
          value.quantity = 0;
        }
        total = total + parseInt(value.quantity);
      });
      self.model.itemDetails[self.model.selectedIndex].poQuantity = total;
      self.model.ClosePopup('close', 'AddQtyModal_ETOitemConfig');
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

    var OpenInvoiceDate = function () {
      self.model.invoiceDateOpened = true;
    }

    var OpenDueDate = function () {
      self.model.dueDateOpened = true;
    }

    var OpenInwordDate = function () {
      self.model.inwordDateOpened = true;
    }

    var OpenDCDate = function () {
      self.model.dcDateOpened = true;
    }

    var OpenExpiryDate = function (idx, item) {
      self.model.dateOpen[idx] = true;
      if(self.model.prRequestTypeId != 4){  
        if(item.expiryDate && !$scope.grnId && item.costOfPurchase == 0){  
          item.costOfPurchase = '';
          var cop = 'cop_'+self.model.itemDetails.length;
          document.getElementById(cop).focus();
        }
      }
    }
    var OpenBonusExpiryDate = function (idx, item) {
        self.model.bonusDateOpen[idx] = true;
        if(self.model.prRequestTypeId != 4){  
          if(item.expiryDate && !$scope.grnId && item.costOfPurchase == 0){  
            item.costOfPurchase = '';
            var cop = 'cop_'+self.model.itemDetails.length;
            document.getElementById(cop).focus();
          }
        }
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
        var creditAmount=self.model.totalCreditNoteAmount!=undefined && self.model.totalCreditNoteAmount!=null ? self.model.totalCreditNoteAmount:0;
        self.model.totalCreditNoteAmount=0;
        for(var i=0;i<self.model.addCreditNoteList.length;i++)
        	{
        	self.model.removeCreditNote(self.model.addCreditNoteList[i]);
        	i--;
        	}
//        angular.forEach(self.model.addCreditNoteList, function (val , key) {
//        	self.model.removeCreditNote(val);
//        });
        self.model.saveCreditNoteAmount();
        $scope.totalAvailableNNetAmount=$scope.totalAvailableNNetAmount-creditAmount;
      } else {
        self.model.isConsignment = false;
        self.model.GetCreditNoteAmount();
      }
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
        self.model.store = self.model.stores[0];
        self.model.fromStore = self.model.fromStores[0];
        self.model.poType = self.model.poTypes[0];

        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.deliveryStores);
            self.model.fromStores = self.model.fromStores.concat(response.data.requestingStores);
            self.model.poTypes = self.model.poTypes.concat(response.data.assetType);
            // for(var i=0;i<self.model.fromStores.length;i++)
            // {
            //   if(self.model.fromStores[i].id==$cookies.getObject('selectedTopStore').storeId)
            //   {
            //     self.model.fromStore = self.model.fromStores[i];
            //   }
            // }
            self.model.store = self.model.stores.find(function (store) {
                return $scope.storeId == store.id;
            });
            self.model.fromStore = self.model.fromStores.find(function (store) {
              return $scope.storeId == store.id;
            });
            var grnId = commonDetailService.getDataId();
            $scope.isFromApprovalEdit=commonDetailService.getIsFromApprovalEdit();
            $rootScope.stopLoader();
            $scope.grnId = grnId;
            if (!!grnId) {
              commonDetailService.setDataId(null);
              GetGRN(grnId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    function SearchVendorNames(search) {
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length <= 1)) {
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
   $scope.invoiceNumbers;
    function AddVendorName(vendor) {
    	$scope.invoiceNumbers;
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.SEARCH_GRN_API+"/invoiceNumber";
      
      var data = {
        "unitId": $scope.unitId,
        "orgId":$scope.organizationId,
        "vendorId": self.model.vendorId
      }  
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
      	  if(response.data.length>0)
            {
      		$scope.invoiceNumbers= response.data;
//      		GetCreditNoteAmount();
            }
      	GetCreditNoteAmount();
        })
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
        "detail": "Provider not found with name " + search
      };
      self.model.serviceProviderNames = [item];
      return self.model.serviceProviderNames;
    }

    function AddProviderName(provider, item) {
      item.serviceProvider = provider.vendorName;
      item.providerId = provider.id;
      $timeout(function(){
        var wardate = 'wardate_'+self.model.itemDetails.length;
        document.getElementById(wardate).focus();
      },2000);
    }

    var ChangeRequestType = function (requestType) {
      if (ValidateDetails(requestType)) {
        if (requestType == 4) {

          self.model.action = 'Open';
          self.model.vendorNameForPO = self.model.vendorName;
          self.model.vendorIdForPO = self.model.vendorId;
          var popup = angular.element('#approvedPO');
          popup.modal('show');
          self.model.searchPO = undefined;
          self.model.poItemList = [];
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
        } else if (requestType == 1) {
          self.model.action = 'Open';
          var popup = angular.element('#addNewItemWasteStore');
          popup.modal('show');
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          PopulateAssetCategoryDropdown();
          self.model.searchGRN.itemCode = undefined;
          self.model.searchGRN.itemName = undefined;
          self.model.items = [];
          self.model.pagination.data=[];
        }
      }
    }
    $scope.calculateProfitMargin = function (item) {
    	setTimeout(function() {
    	if(item.newPOQty==undefined || item.newPOQty==null)
    		{
    		item.newPOQty=item.grnReceivedQuantity;
    		}
    	if(item.costOfPurchase==undefined || item.costOfPurchase==null)
		{
		item.costOfPurchase=item.cop;
		}
    	if(item.discountAmount==undefined || item.discountAmount==null)
		{
		item.discountAmount=item.totalDiscount;
		}
    	var tax=item.taxAmount/item.newPOQty;
    	var dis=item.discountAmount/item.newPOQty;
    	var mrp=(item.mrp!=undefined && item.mrp!=null ? item.mrp :0)-(tax!=undefined && tax!=null ? tax:0);
    	var cop=(item.costOfPurchase!=undefined && item.costOfPurchase!=null ? item.costOfPurchase:0)-(dis!=undefined && dis!=null ? dis :0);
    	var sub=mrp-cop;
    	item.profit=(sub*100)/cop;
    	if(isNaN(item.profit) || item.profit==undefined || item.profit==null)
    		{
    		item.profit=0.00;
    		}
    	item.profit=item.profit.toFixed(2);
    	}, 1000);
    }
    $scope.updateEditFlag = function (item) {
    	$scope.editFlag=false;
    }
    
    $scope.validateDiscount = function (item){
        var regexp = /^(\d{0,4}(\.\d{0,2})?)$/;
               
        if(!regexp.test(Number(item.discountPercent))){
        	item.discountPercent = 0;
            item.discountAmount = 0;
         }
    }
    
    $scope.editFlag=false;
    $scope.editQtyFlag=false;
    var GetGRN = function (grnId) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GET_GRN_API + "/" + $scope.unitId + "/" + $scope.organizationId +"/" + grnId;
      $scope.editFlag=true;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.discountTypeFlag = false;
          self.model.isDiscountAppliedValid = false;
          self.model.isAppliedDiscountValid = false;
          self.model.isDiscountApplied = true;
          $scope.id = response.data.id;
          angular.forEach(response.data.items, function (item, key) {
            if(item.batchRequired == false && !item.expiryDate){
              item.expiryDate  = '';
            }
            else{
              item.expiryDate = new Date(item.expiryDate);
            }
            $scope.amountround1 = (item.totalAmount - item.discountAmount) + (item.taxAmount) + (item.otherChargesAmount);
          });

          angular.forEach(response.data.items, function (item, key) {
            item.warrantyDate = new Date(item.warrantyDate);
          });

          angular.forEach(response.data.items, function (item, key) {
            item.warrantyDateTo = new Date(item.warrantyDateTo);
          });

          self.model.grnId = response.data.id;
          
           if( response.data.billTypeName=='Cash'){
        	   self.model.billType=$scope.billType[0];
           }
           else{
        	   self.model.billType=$scope.billType[1];  
           }
          
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });
          self.model.fromStore = self.model.fromStores.find(function (item) {
            return response.data.requestingStoreId == item.id;
          });
          self.model.poType = self.model.poTypes.find(function (potype) {
            return response.data.assetTypeId == potype.id;
          });

          self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
          self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
          self.model.prRequestTypeId = response.data.grnTypeId;
          self.model.requestType = response.data.grnTypeId;
          self.model.dcDate = new Date(response.data.dcDate)
          self.model.dcNumber = response.data.dcNumber;
          self.model.finalDiscount = response.data.discount;
          self.model.dueDate = new Date(response.data.dueDate);
          self.model.invoiceDate = new Date(response.data.invoiceDate);
          self.model.invoiceNumber = response.data.invoiceNumber;
          self.model.inwordDate = new Date(response.data.inwardDate);
          self.model.inwordNumber = response.data.inwardNumber;
          self.model.isConsignment = response.data.isConsignment;
          self.model.totalNetAmount = response.data.netAmount;
          self.model.totalNetAmount1 = response.data.netAmount
          self.model.totalAmount = response.data.totalAmount;
          self.model.totalDiscount = response.data.totalDiscount;
          self.model.otherChargesAmount = response.data.totalOtherAmount;
          self.model.totalTax = response.data.totalTaxAmount;
          self.model.vendorName = response.data.vendorName;
          self.model.vendorId = response.data.vendorId;
          self.model.GRNRemark = response.data.remark;
          self.model.discountType = response.data.discountTypeId;
          if(response.data.roundOff != undefined && response.data.roundOff != null && response.data.roundOff != 0){
            $scope.roundOff = response.data.roundOff.toFixed(2);
          }
          self.model.vendorId = response.data.vendorId;
          self.model.addCreditNoteList = response.data.creditNoteAmountList; 
          self.model.itemDetails = [];
          if(response.data.totalOtherAmount == undefined || response.data.totalOtherAmount == null || response.data.totalOtherAmount ==0){
        	  var totalOtherCharges=0;
              angular.forEach(response.data.grnOtherChargesMapper, function (otherCharge) {
            	  totalOtherCharges=totalOtherCharges+otherCharge.amount;
              })
              self.model.otherChargesAmount=totalOtherCharges;
          }
          angular.forEach(response.data.items, function (item) {
            self.model.accessoryList = item.grnAccessoriesMapper;
            $scope.accessoryList = item.grnAccessoriesMapper;
            angular.forEach($scope.accessoryList, function(acc){
              acc.serialNumber = acc.serialNo;
              acc.warrantyFrom = acc.warrentyFrom;
              acc.warrantyTo = acc.warrentyTo;
            })
            var grnUom = [];
            angular.forEach(item.grnUom, function (uom) {
              grnUom.push({
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

            var getotalStagedData = [];
            angular.forEach(item.grnStagedLists, function (data) {
              getotalStagedData.push({
                id: data.id,
                stagedDcDate: data.stagedDcDate,
                stagedReceivedQuantity: data.stagedReceivedQuantity,
                originalStagedReceivedQuantity: data.stagedReceivedQuantity,
                isEditable: !!data.isEditable ? data.isEditable : false,
                orginalApprovedQty: data.stagedReceivedQuantity
              });
            });
            var getStagedData = [];
            angular.forEach(item.grnStagedQuantity, function (stagedQty) {
              getStagedData.push({
                id: stagedQty.id,
                stagedDate: stagedQty.poStagedDate,
                stagedQuantity: stagedQty.stagedQuantity,
                receivedQuantity: stagedQty.receivedQuantity,
                pendingQuantity: stagedQty.pendingQuantity,
                poApprovedQuantity: stagedQty.poApprovedQuantity,
                isEditable: !!stagedQty.isEditable ? stagedQty.isEditable : false,
                orginalApprovedQty: stagedQty.poApprovedQuantity
              });
            });
            var sumOfBonusReceivedQuantity=0;
            if(item.grnBonusBatchExpiry!=undefined && item.grnBonusBatchExpiry!=null && item.grnBonusBatchExpiry.length>0)
            	{
            angular.forEach(item.grnBonusBatchExpiry, function (grnBonusBatchExpiryCount) {
            	grnBonusBatchExpiryCount.expiryDate = new Date(grnBonusBatchExpiryCount.expiryDate);
            	sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+grnBonusBatchExpiryCount.bonusQuantity;
              });
            item.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
                }
            var poList = [];
            angular.forEach(item.poList, function (po) {
              poList.push({
                poNo: po.poNo,
                poQuantity: po.approvedQuantity,
                poDate:po.poDate
              });
            });

            var poTotal = 0;
            angular.forEach(item.poList, function (x) {
              poTotal += x.approvedQuantity
            });

            item.discount = item.discount ? item.discount : 0;
            item.selectedTaxPercent = item.taxPercentage.toFixed(2) ? item.taxPercentage.toFixed(2) : 0;
            // item.taxPercentage = item.taxPercentage ? item.taxPercentage : 0;
            item.totalDiscount = item.totalDiscount ? item.totalDiscount : 0;

            var poItem = {
              "id": item.id,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              // "poQuantity": item.grnApprovedQuantity,
              "poBonusQuantity": item.poBonusQuantity,
              "grnReceivedQuantity": item.grnReceivedQuantity,
              "bonusReceivedQuantity": item.bonusReceivedQuantity,
              "costOfPurchase": item.cop.toFixed(2),
              "mrp": item.mrp.toFixed(2),
              "discountPercent": item.discount,
              "selectedTaxPercent": item.selectedTaxPercent,
              // "taxPercent": item.taxPercentage.toFixed(2),
              // "taxId": item.taxId,
              // "taxName": item.taxCode,
              "poAmount": item.totalAmount,
              "taxAmount": item.taxAmount,
              "manufacturerId": item.manufacturerId,
              "netAmount": item.netAmount,
              "discountAmount": !isNaN(item.totalDiscount) && item.totalDiscount!=undefined && item.totalDiscount!=null ? item.totalDiscount.toFixed(2):0,
              "specification": item.specification,
              "itemRemark": item.remark,
              "stageQuantityList": item.purchaseOrderStagedDto,
              // "selectedOtherCharges": item.otherCharges,
              "batchNumber": item.batchNo,
              "expiryDate": item.expiryDate,
              "manufacturerName": item.manufacturerName,
              "itemSpecification": item.grnSpecification,
              "poStagedList": getStagedData,
              "grnStagedLists": getotalStagedData,
              "poNumbersList": poList,
              "warrentyPeriod": item.warrentyPeriod,
              "supportTerms": item.supportTerms,
              "technicalSpecification": item.technicalSpecification,
              "expectedPayment": item.expectedPayment,
              "warrantyDate": item.warrantyDate,
              "warrantyDateTo": item.warrantyDateTo,
              "serviceProviderId": item.serviceProviderId,
              "serviceProvider": item.serviceProvider,
              "accessories": item.grnAccessoriesMapper,
              "warrantyPeriod": item.warrentyPeriod,
              "paymentTerms": item.expectedPayment,
              "technicalSpecs": item.technicalSpecification,
              "grnAccessoriesMapper": item.grnAccessoriesMapper,
              "poQuantity": item.poQuantity,
              "poLeaseQuantity": item.poQuantity,
              "totalPoQty": poTotal,
              "totalOtherAmount": item.totalOtherAmount,
              "otherCharges": item.otherCharges,
              "providerId": item.serviceProviderId,
              "specification": item.poSpecification,
              "poPendingQuantity": item.poPendingQuantity,
              "poGrnQuantity": item.poGrnQuantity,
              "vendorId": self.model.vendorId,
              "grnAmount": item.totalAmount,
              "poId":item.poId,
              "pendingQuantity" : item.pendingQuantity,
              "pendingQty" : item.pendingQuantity,
              "isThreeUOMApplicable": item.isThreeUOMApplicable,
              "grnBonusBatchExpiryData":item.grnBonusBatchExpiry,
              "selectedTax": self.model.taxList.find(function (x) {
                return item.taxId == x.taxId;
              })
            }
            poItem.taxPercent=item.taxPercentage;
            var bonusQuantity = Number(item.poBonusQuantity);
            var bonusQtyList = { bonusQuantity: bonusQuantity };
            poItem['bonusQuantityList'] = [bonusQtyList];

            poItem.maxOrderQuantity = item.maxOrderQuantity;
            poItem.maxStoreQuantity = item.maxStoreQuantity;
            poItem.otherChargesAmount = item.totalOtherAmount;
            poItem.lcopAmount = item.lcop;

            if (self.model.poType.id == 1) {
              poItem.uom = item.uom;
              poItem.selectedUom = item.uom.find(function (uom) {
                return item.grnUomTypeId == uom.uomTypeId;
              });
              poItem.bonusSelectedUom = item.uom.find(function (uom) {
                return item.bonusUomTypeId == uom.uomTypeId;
              });
              poItem.poUom = grnUom;
              poItem.bonusUom = bonusUom;
              if(poItem.isThreeUOMApplicable == false){
                poItem.uomListdata = poItem.uom;
                poItem.uom = [];
                angular.forEach(poItem.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    poItem.uom.push(data);
                  }
                });
              }
              poItem.grnUomTypeId=item.grnUomTypeId;
              poItem.poGrnUomTypeId=item.poGrnUomTypeId;
              self.model.CalculateStock2(poItem, poItem.uom, poItem.selectedUom);
              CalculateBonusQuantity(poItem);
            } else if (self.model.poType.id == 2) {

              poItem.newMaxOrderQuantity = item.maxOrderQuantity;
              poItem.newMaxStoreQuantity = item.maxStoreQuantity;
              // poItem.selectedTaxPercent = item.taxPercentage.toFixed(2);
            }
            poItem.pendingQuantity = item.pendingQuantity;
            poItem.disableBonusTextField=true;
            if(poItem.grnBonusBatchExpiryData!=undefined && poItem.grnBonusBatchExpiryData!=null && poItem.grnBonusBatchExpiryData.length==1 && poItem.batchNumber.toUpperCase()==poItem.grnBonusBatchExpiryData[0].batchNo.toUpperCase() )
        	{
            	poItem.disableBonusTextField=false;
        	}
            if(poItem.grnBonusBatchExpiryData==undefined || poItem.grnBonusBatchExpiryData==null || poItem.grnBonusBatchExpiryData.length==0)
            	{
            	poItem.disableBonusTextField=false;
            	}
            $scope.calculateProfitMargin(poItem);
            poItem.grnUomTypeId=item.grnUomTypeId;
            poItem.poGrnUomTypeId=item.poGrnUomTypeId;
            if(poItem.poGrnUomTypeId!=undefined && poItem.poGrnUomTypeId!=null)
            	{
            	$scope.editQtyFlag=true;
            	}
            if(poItem.isThreeUOMApplicable == false){
                if (poItem.poGrnUomTypeId==1) {
                	poItem.poFinalLeaseQty = parseInt(poItem.poPendingQuantity * (poItem.uom[0].conversion * poItem.uom[1].conversion * poItem.uom[2].conversion));
                	
                }
                else if(item.poGrnUomTypeId==3)
                	{
                	poItem.poFinalLeaseQty = parseInt(poItem.poPendingQuantity * (poItem.uom[2].conversion));
                	}
            	}
            else
        	{
        	 if (item.poGrnUomTypeId==1) {
        		 poItem.poFinalLeaseQty = parseInt(poItem.poPendingQuantity * (poItem.uom[0].conversion * poItem.uom[1].conversion * poItem.uom[2].conversion));
             	
             }
             else if(item.poGrnUomTypeId==2)
             	{
            	 poItem.poFinalLeaseQty = parseInt(poItem.poPendingQuantity * (poItem.uom[1].conversion * poItem.uom[2].conversion));
             	}
             else if(item.poGrnUomTypeId==3)
          	{
            	 poItem.poFinalLeaseQty = parseInt(poItem.poPendingQuantity * (poItem.uom[2].conversion));
          	}
        	}
            if(poItem.poQuantity!=undefined && poItem.poQuantity!=null && poItem.poQuantity>poItem.grnReceivedQuantity)
            	{
            	poItem.poQuantity = poItem.poQuantity;
            	}
            else
            	{
            poItem.poQuantity = parseInt(poItem.actualGrnQty!=undefined && poItem.actualGrnQty!=null ? poItem.actualGrnQty :0);
            	}
            if(poItem.poGrnUomTypeId==undefined || poItem.poGrnUomTypeId==null)
        	{
            	poItem.FinalPoQuantityForGrn=poItem.grnReceivedQuantity;
        	}
            self.model.itemDetails.push(poItem);
            $scope.otherchargesmapper = response.data.grnOtherChargesMapper;
          });
          GetCreditNoteAmount();
          calculateOtherChargesAmountForItem();
          var vendor = {
            		"id" : self.model.vendorId,
            		"vendorName" : self.model.vendorName
            };
              if(self.model.vendorId != undefined){
            AddVendorName(vendor);
            $scope.oldVendor = self.model.vendorId;
            $scope.oldInvoiceNumber = self.model.invoiceNumber;
          }
          $rootScope.stopLoader();
        },
        
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }
    
    $scope.getDocumentType = function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.documentTypeList = [];
				if (response.data.status == "success") {
					$scope.documentTypeList = response.data.listObject;
				}
			});
	}
	$scope.getDocumentType();
	
	$scope.addImageDetails = function()
	{ 
		var encounterDetails = angular.copy($scope.encounterDetails);
		$scope.encounterDetailsList.push(encounterDetails);
	};

  $scope.removeImageDetails = function(deleteIndex)
	{ 
	   $scope.encounterDetailsList.splice(deleteIndex, 1);
    }	
  		
  $scope.encounterDetailsList=[];
  $('#fileId').val(undefined);
    	var uploadDoccument =  function(encounterObj)
			{
			 $scope.forView = false;
			 $scope.encounterDetails={};
				 
				
				$scope.encounterDetails.patientId = 0;	
				$scope.encounterDetails.visitTypeId = 1;	
				$scope.encounterDetails.createdBy = $scope.userId;
				$scope.encounterDetails.updatedBy = $scope.userId;
				$scope.encounterDetails.unitId = $scope.unitId;
				$scope.encounterDetails.organisationId = $scope.organizationId;
				$scope.encounterDetails.grnId = $scope.grnIdForDoccumentUpload;
				$scope.encounterDetails.uploadDoccumentDate=new Date();
				if($scope.encounterDetailsList.length>0)
				{
				  $('#uploadDetails').modal('show');
				}
			else{
				 $('#uploadDetails').modal('show');
				var encounterDetails = angular.copy($scope.encounterDetails);
				encounterDetails.patientId = encounterObj.patientId;
				$scope.encounterDetailsList.push(encounterDetails);
			}
					
				$scope.getPatientDocumentList();
				
			}
		 $scope.forView = false;
		 
		 $scope.uploadPatientImage = function()
		    {
			  $scope.finalDocUploadList=[];
			  var count=0;
			  $scope.forView = false;
			 angular.forEach($scope.encounterDetailsList, function(value, key) {  
				  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
						 growl.warning("Please select document type .",
													{
														title : 'warning!'
													});
										   $rootScope.stopLoader();
						return false;
					}
				  if (angular.equals(value.file, {}) || value.file==undefined) {
						growl.warning('Please select file.',
										{
											title : 'warning!'
										});
						 $rootScope.stopLoader();
						return false;
					}
					 var ext = value.file.name.split(".").pop().toLowerCase();
						if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
							growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
									{
										title : 'warning!'
									});
							$rootScope.stopLoader();
							return false;
						}
						$scope.finalDocUploadList.push(value);
						 console.log("final list",$scope.finalDoc);
			});
			 
			 if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
				   $('#uploadDetails').modal('hide');
			
			 $rootScope.startLoader();
			  $scope.count = 0;
			 angular.forEach($scope.encounterDetailsList, function(value, key) 
				{
					var promise = $scope.uploadEncounterDoc(value);
					promise.then(function(response) 
							{
							  if(response.data.status == "success")
								  {
								    $scope.count++;
								    if($scope.encounterDetailsList.length==$scope.count)
									 {
								       $('#fileId').val(undefined);
									   $scope.finalDocUploadList=[];
									   $scope.encounterDetailsList = [];
									   $rootScope.stopLoader();
									 }
							      }
							  else 
								{
									growl.error('Something went wrong.',
											{
												title : 'Error!'
											});
								}
						    });
						
				});
			 }
			
		    }
		 
			$scope.getPatientDocumentList = function()
      {
        if($scope.grnIdForDoccumentUpload != undefined && $scope.grnIdForDoccumentUpload != null && $scope.grnIdForDoccumentUpload != 0){
          $rootScope.startLoader();
		      var data =
          {
		        "patientId":0,
		        "unitId":$scope.unitId,
		        "organisationId":$scope.organizationId,
		        "userId":undefined,
		        "visitTypeId":undefined,
		        "admissionId":undefined,
		        "grnId":$scope.grnIdForDoccumentUpload,
          }
		      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
		      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		        $scope.patientDocumentList = [];
		        if (response.data.status == "success") 
		        {
		        	$scope.patientDocumentList = response.data.listObject;  
		        	angular.forEach($scope.patientDocumentList,function(value, key) {
		        		var filepath = value.documentPath
		        		var arr = filepath.split("/");
		        		var length=arr.length;
		        		var fileName1=arr[length-1];
		        		var arr1 = fileName1.split("_");
		        		var length1=arr1.length;
		        		var fileName=arr1[length1-1];
		        		value.fileName=fileName;
		        	});
		        		
		        	
		           
		            $rootScope.stopLoader();
		        }
		        else if(response.data.status == "error")
            {
		        	$rootScope.stopLoader();
            }
          });
        }
      }
		$scope.goToItemMaster =function ()
		{
			$window.open('#/itemMasterGlobal', '_blank');
		}
		$scope.goToVendorMaster =function ()
		{
			$window.open('#/vendormaster', '_blank');
		}
      $scope.uploadEncounterDoc = function(Obj)
			{
				 var fd = new FormData();
			        fd.append('encounterId', 0);
			        fd.append('visitTypeId', Obj.visitTypeId);
			        fd.append('admissionId', 0);
			        fd.append('patientId',0);
			        fd.append('documentTypeId', Obj.documentTypeId);
			        fd.append('createdBy',Obj.createdBy );
			        fd.append('updatedBy', Obj.updatedBy);
			        fd.append('unitId', Obj.unitId);
			        fd.append('organisationId', Obj.organisationId);
			        fd.append('uploadDoccumentDate', moment(Obj.uploadDoccumentDate).format('DD-MM-YYYY HH:mm:ss'));
			        fd.append('file',Obj.file );
            fd.append('expenseId', 0);
            fd.append('grnId', $scope.grnIdForDoccumentUpload);
            fd.append('poId',0);
            fd.append('incidentReportingMapperId',0);
            fd.append('companyExpenseId',0);
            fd.append('createdDate',null);
					  fd.append('fileEditedName',null);
            fd.append('opbId',0);
            fd.append('vendorId',0);
			        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
			        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
					{	
						return response;
					});
			}
			$scope.viewUploadedDoccument = function(encounterObj)
			{
				$scope.forView = true;
				$scope.getPatientDocumentList(encounterObj);
				$('#uploadDetails').modal('show');
			}
		 
			
			
    var SetFocus = function () {
      self.model.barcode = undefined;
      // $window.document.getElementById('barcode').focus();
    }
    var AddItemForBatch = function (index, data) {
      data.splice(index + 1, 0, CopyItem(index, data));
      SetFocus();
    }

    var AddItemForBatch1 = function (index, data) {
      data.splice(index + 1, 0, CopyItem1(index, data));
      SetFocus();
    }

    var CopyItem = function (index, data) {
      var item = data[index];
      if (self.model.prRequestTypeId == 4) {
        var list = angular.copy(item.grnStagedLists);
        list.splice(-1);
        list.push(
          {
            "stagedDcDate": self.model.dcDate,
            "stagedReceivedQuantity": "",
            "isEditable": true,
          });
      }
      else if (self.model.prRequestTypeId == 1) {
      }
      $timeout( function(){
        if(self.model.poType.id == 1){
          var qty = 'qty_'+self.model.itemDetails.length;
          document.getElementById(qty).focus();
        }
        if(self.model.poType.id == 2){
          var qtys = 'qtys_'+self.model.itemDetails.length;
          document.getElementById(qtys).focus();
        }
      },2000);
      return {
        itemId: item.itemId,
        itemCode: item.itemCode,
        itemName: item.itemName,
        poBonusQuantity: item.poBonusQuantity,
        bonusQuantityList: item.bonusQuantityList,
        bonusSelectedUom: item.bonusSelectedUom,
        bonusUom: item.bonusUom,
        bonusUomType: item.bonusUomType,
        bonusUomTypeId: item.bonusUomTypeId,
        bonusUomUnit: item.bonusUomUnit,
        bonusUomUnitId: item.bonusUomUnitId,
        genericId: item.genericId,
        genericName: item.genericName,
        grnAccessoriesMapper: item.grnAccessoriesMapper,
        grnAmount: item.grnAmount,
        grnStagedLists: list,
        manufacturerId: item.manufacturerId,
        manufacturerName: item.manufacturerName,
        maxOrderQuantity: item.maxOrderQuantity,
        maxStoreQuantity: item.maxStoreQuantity,
        netAmount: item.netAmount,
        newMaxOrderQuantity: item.newMaxOrderQuantity,
        newMaxStoreQuantity: item.newMaxStoreQuantity,
        newPOQty: item.newPOQty,
        poBonusQuantity: item.poBonusQuantity,
        poDetailId: item.poDetailId,
        poGrnQuantity: item.poGrnQuantity,
        poIds: item.poIds,
        poLeaseQuantity: item.poLeaseQuantity,
        poNumbersList: item.poNumbersList,
        poOtherCharges: item.poOtherCharges,
        poPendingQuantity: item.poPendingQuantity,
        poQuantity: item.poQuantity,
        poStagedList: item.poStagedList,
        poUom: item.poUom,
        poUomType: item.poUomType,
        poUomTypeId: item.poUomTypeId,
        poUomUnit: item.poUomUnit,
        poUomUnitId: item.poUomUnitId,
        prList: item.prList,
        selectedTax: item.selectedTax,
        selectedTaxPercent: item.selectedTaxPercent,
        selectedUom: item.selectedUom,
        totalPoQty: item.totalPoQty,
        uom: item.uom,
        mrp: item.mrp,
        taxAmount: item.taxAmount,
        pendingQuantity: item.pendingQuantity,
        grnReceivedQuantity: item.grnReceivedQuantity,
        isThreeUOMApplicable: item.isThreeUOMApplicable,
        receivedQtyCon: item.receivedQtyCon,
        receivedQtyCalculation: item.receivedQtyCalculation,
        uomUnitData: item.uomUnitData,
        bonusReceivedQuantity: item.bonusReceivedQuantity,
        receivedQtyBonusCon: item.receivedQtyBonusCon,
        receivedQtyBonusCalculation: item.receivedQtyBonusCalculation,
        uomUnitDataBonus: item.uomUnitDataBonus,
        batchNumber: item.batchNumber,
        expiryDate: item.expiryDate,
        costOfPurchase: item.costOfPurchase,
        newBonusReceivedQuantity: item.newBonusReceivedQuantity,
        uomListdata: item.uomListdata
      }
    }

    var CopyItem1 = function (index, data) {
      var item = data[index];
      if (self.model.prRequestTypeId == 4) {
        var list = angular.copy(item.grnStagedLists);
        list.splice(-1);
        list.push(
          {
            "stagedDcDate": self.model.dcDate,
            "stagedReceivedQuantity": "",
            "isEditable": true,
          });
      }
      else if (self.model.prRequestTypeId == 1) {
      }
      $timeout( function(){
        if(self.model.poType.id == 1){
          var itemSearch = 'item-name_'+self.model.itemDetails.length;
          document.getElementById(itemSearch).focus();
        }
        if(self.model.poType.id == 2){
          var abc = 'abc_'+self.model.itemDetails.length;
          document.getElementById(abc).focus();
        }
      },2000);
      return {
        itemId: '',
        itemCode: '',
        itemName: '',
        poBonusQuantity: '',
        bonusQuantityList: '',
        bonusSelectedUom: '',
        bonusUom: '',
        bonusUomType: '',
        bonusUomTypeId: '',
        bonusUomUnit: '',
        bonusUomUnitId: '',
        genericId: '',
        genericName: '',
        grnAccessoriesMapper: '',
        grnAmount: '',
        grnStagedLists: '',
        manufacturerId: '',
        manufacturerName: '',
        maxOrderQuantity: '',
        maxStoreQuantity: '',
        netAmount: '',
        newMaxOrderQuantity: '',
        newMaxStoreQuantity: '',
        newPOQty: '',
        poBonusQuantity: '',
        poDetailId: '',
        poGrnQuantity: '',
        poIds: '',
        poLeaseQuantity: '',
        poNumbersList: '',
        poOtherCharges: '',
        poPendingQuantity: '',
        poQuantity: '',
        poStagedList: '',
        poUom: '',
        poUomType: '',
        poUomTypeId: '',
        poUomUnit: '',
        poUomUnitId: '',
        prList: '',
        selectedTax: '',
        selectedTaxPercent: '',
        selectedUom: '',
        totalPoQty: '',
        uom: '',
        mrp: '',
        taxAmount: '',
        pendingQuantity: '',
        disableBonusTextField:false
      }
    }

    var ValidateDetails = function (requestType) {
      var isValid = true;
      if(requestType!=4)

      if (!self.model.poType.id) {
        self.model.isValidPOType = true;
        isValid = false;
      }

     /*
		 * if (!self.model.vendorId || !self.model.vendorName) {
		 * self.model.isVendorValid = true; isValid = false; }
		 */

      if(requestType == 4)
      {
      if ( !self.model.poType.id  ) {
     growl.error('Please select required fields');
     isValid = false;
         }
      }
      else
      {
        if (!self.model.store.id || !self.model.poType.id || !self.model.vendorId || !self.model.vendorName) {
     growl.error('Please select required fields');
     isValid = false;
   }   
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;
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
      }

      if (!self.model.itemDetails.length) {
        growl.error('Please add items to save GRN');
        isValidItem = false;
      } else {
        angular.forEach(self.model.itemDetails, function (item, index) {
          if (!parseFloat(item.costOfPurchase)) {
            // growl.error('Please enter valid COP');
            self.model.costOfPurchase[index] = true;
//            isValidItem = false;
          }

          if (!parseFloat(item.mrp)) {
            growl.error('Please enter valid MRP');
            self.model.mrp[index] = true;
            isValidItem = false;
          }

          if (!parseInt(item.grnReceivedQuantity)) {
            growl.error("Please enter valid quantity");

            self.model.grnReceivedQuantity[index] = true;
            isValidItem = false;
          }

          if (parseFloat(item.mrp) < parseFloat(item.costOfPurchase)) {
            growl.error('MRP should be greater than COP');
            isValidItem = false;
          }

          if (self.model.prRequestTypeId == 1) {
            if (parseInt(item.grnReceivedQuantity) > item.newMaxOrderQuantity) {
              isValidItem = false;
              self.model.grnReceivedQuantity[index] = true;
              growl.error('Received quantity should less than pending quantity');
            }
          }

          if (self.model.prRequestTypeId == 4) {
            if (item.bonusReceivedQuantity) {
              if(item.bonusSelectedUom == undefined || item.bonusSelectedUom == null){
                growl.error('Please select valid Bonus UOM');
                isValidItem = false;
              }
            }
          }

          if (!item.selectedTax.taxId) {
            growl.error('Please select valid tax code');
            self.model.taxCodeValid[index] = true;
            isValidItem = false;
          }

          if (item.selectedTaxPercent == undefined && item.selectedTaxPercent == null && item.selectedTaxPercent == "" && item.selectedTaxPercent != 0) {
            growl.error('Please enter valid tax %');
            self.model.taxPercent[index] = true;
            isValidItem = false;
          }
          if (self.model.poType.id == 1) {
            if (item.isBatchRequired || item.isBatchRequired == undefined) {
              if (!item.expiryDate) {
            	  if(item.batchNumber!="NA")
                {
                  growl.error('Please select expiry date');
                  self.model.expiryDate[index] = true;
                  isValidItem = false;
                }
              }
            }
            // else
            // {
            //   isValidItem = true;
            // }
            // if (item.isBatchRequired || item.isBatchRequired == undefined) {
              if (!item.batchNumber) {
                growl.error("Please enter batch number");
                self.model.batchNumber[index] = true;
                isValidItem = false;
              }
            // }
            // else
            // {
              // isValidItem = true;
            // }
        } else if (self.model.poType.id == 2) {
          if (!item.serviceProvider || !item.providerId) {
            growl.error('Please enter valid service provider');
            self.model.serviceProviderValid[index] = true;
            isValidItem = false;
          }

          if (!item.warrantyDate) {
            growl.error('Please select warranty date');
            self.model.warrantyDate[index] = true;
            isValidItem = false;
          }

          if (!item.warrantyDateTo) {
            growl.error('Please select warranty date');
            self.model.warrantyDateTo[index] = true;
            isValidItem = false;
          }

          if (!item.warrantyPeriod) {
            item.isValidWarrantyTerm = true;
            growl.error('Please enter warranty period');
            isValidItem = false;
          }

          if (!item.supportTerms) {
            item.isValidSupportTerm = true;
            growl.error('Please enter expected support terms');
            isValidItem = false;
          }

          if (!item.technicalSpecs) {
            item.isValidTechSpec = true;
            growl.error('Please enter expected technical specifications');
            isValidItem = false;
          }

          if (!item.paymentTerms) {
            item.isValidPaymentTerm = true;
            growl.error('Please enter expected payment terms');
            isValidItem = false;
          }
        }
      });

        if (!self.model.vendorId || !self.model.vendorName) {
          self.model.isVendorValid = true;
          isValidItem = false;
        }

        if (!self.model.invoiceDate) {
          self.model.isInvoiceDateValid = true;
          isValidItem = false;
        }

        if (!self.model.invoiceNumber && !self.model.dcNumber) {
          growl.error('Please enter invoice number');
          self.model.isInvoiceNumberValid = true;
          isValidItem = false;
          $scope.saveBtnDisabled = false;
        }
        if (!self.model.invoiceNumber && !self.model.dcNumber) {
          growl.error('Please enter dc number');
          self.model.isdcNumberValid = true;
          isValidItem = false;
          $scope.saveBtnDisabled = false;
        }

        // if (!!self.model.dcNumber) {
        // if (!/^[a-z0-9]*[-]*[/]?[a-z0-9]*$/i.test(self.model.dcNumber)) {
        // growl.error("Please enter valid DC number");
        // isValidItem = false;
        // }
        // }

        // if (!!self.model.invoiceNumber) {
        // if (!/^[a-z0-9]*[-]*[/]?[a-z0-9]*$/i.test(self.model.invoiceNumber))
		// {
        // growl.error("Please enter valid invoice number");
        // isValidItem = false;
        // }
        // }

        if (!self.model.vendorId || !self.model.vendorName) {
          self.model.isVendorValid = true;
          growl.error('Please enter vendor name');
          isValidItem = false;
        }

        if (self.model.discountType == 1) {
          if (parseFloat(self.model.finalDiscount) > 100) {
            isValidItem = false;
            growl.error('Discount must be less than 100% of total amount');
          }
        } 
        // else if (self.model.discountType == 2) {
        // if (parseFloat(self.model.finalDiscount) >
		// (parseFloat(self.model.totalAmount) / 2)) {
        // isValidItem = false;
        // growl.error('Please enter discount amount less than half of total
		// amount');
        // }
        // }
      }
      if(!isValidItem)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValidItem;
    }

    var ValidateQty = function (item, index) {
      if (parseInt(item.grnReceivedQuantity) > item.newMaxOrderQuantity) {
        self.model.grnReceivedQuantity[index] = true;
        growl.error('Received quantity should less than pending quantity');
      }
    }

    $scope.validateAmount = function(info){
      if(info.consumenetamount > info.availablenetamount){
        growl.error("Consume net amount is greater than available net amount");
        info.consumenetamount = 0;
      }
    }

    var ApplyDiscountValidation = function () {
      self.model.discountTypeFlag = false;
      // if (!self.model.finalDiscount) {
      // if (self.model.discountType == '1') {
      // growl.error('Please enter % discount');
      // }
      // if (self.model.discountType == '2') {
      // growl.error('Please enter discount amount');
      // }
      // }
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
          //item.costOfPurchase = 0;
        } else {
          item.costOfPurchase = item.costOfPurchase;
        }
      }
    	
      $scope.removeMrpOnBackspace = function (event , item, idx) {
        if(event.which == 9){
          $timeout( function(){
            if(self.model.poType.id == 1){
              var addrBtn = 'addrBtn_'+self.model.itemDetails.length;
              document.getElementById(addrBtn).focus();
            }
          },10);
        }
        if(event.which == 13){
          AddItemForBatch1(idx,self.model.itemDetails);
          $scope.focusInput=true;
        }
     	}
     	
     	 $scope.removeDiscountOnBackspace = function (event , item ) {
      	    if (event.keyCode === 8) {
      	        console.log('here!');
//      	        item.discountPercent=0;
      	    }
      	};
     	
      if (name == "mrp") {
        if (isNaN(item.mrp) || !item.mrp || item.mrp == 0) {
          item.mrp = 0;
        } else {
          item.mrp = parseFloat(item.mrp).toFixed(2);
        }
      }

      if (name == "taxPercent") {
        if (isNaN(item.taxPercent) || !item.taxPercent) {
          item.taxPercent = 0;
        } else {
          item.taxPercent = parseFloat(item.taxPercent).toFixed(2);
        }
      }

      if (name == "taxPercentFixed") {
        if (isNaN(item.selectedTaxPercent) || !item.selectedTaxPercent) {
          item.selectedTaxPercent = 0;
        } else {
          item.selectedTaxPercent = parseFloat(item.selectedTaxPercent).toFixed(2);
        }
      }

      if (name == "discountPercent") {
        if (isNaN(item.discountPercent) || !item.discountPercent || item.discountPercent == 0) {
          item.discountPercent = 0;
        } else {
          item.discountPercent = parseFloat(item.discountPercent);
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
      var grn = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.poType.id+ '/'+$scope.unitId+ '/'+$scope.organizationId;


      grn.assetCategory = grn.assetCategories[0];
      grn.productCategory = grn.productCategories[0];

      grn.assetCategories.splice(1);
      grn.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          grn.assetCategories = grn.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateProductCategory = function (assetCategory) {
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id+ '/'+$scope.organizationId;

      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.productCategories = self.model.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
$scope.prevSearch="";
    var GetItemList1 = function (itemname) {

         if ((!$scope.prevSearch  || $scope.prevSearch  != itemname ) && itemname!=undefined  &&  (itemname.replace(/\s/g, "").length) == 2  ) {

            $scope.prevSearch = itemname;
            $scope.itemsList = [];
            var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;;
                   var data = {
                     "assetTypeId":  self.model.poType.id,
                     "storeId": self.model.store.id,
                     "requestingStoreId": (self.model.fromStore.id!=undefined || self.model.fromStore.id!=null) ? self.model.fromStore.id :self.model.store.id,
                     "itemName": itemname
                   }

                   self.model.items = [];
                   self.model.pagination.data=[];
                   self.model.pagination.totalPages=0;
                   return GenericService.serviceAction("POST", URI, data).then(
                   function (response) {
                     // self.model.items = response.data;
                     if (!!response.data && response.data.length > 0) {
                      $scope.itemsList = response.data;
                                   return $filter('filter')( $scope.itemsList, {
                                     $: itemname
                                   });
                     }else{
                        return response.data;
                     }
                   });
         }else{

            if(itemname !=undefined &&  (itemname.replace(/\s/g, "").length) > 0){
               return $filter('filter')( $scope.itemsList, {
                                                 $: itemname
                                               });
            }

         }

    }

    var GetItemList = function () {
      if(self.model.searchGRN.itemName){

         if (self.model.searchGRN.itemName!=undefined  &&  (self.model.searchGRN.itemName.replace(/\s/g, "").length) == 2  ) {

          var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;;

          var data = {
            "assetTypeId": self.model.poType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "requestingStoreId": (self.model.fromStore.id!=undefined || self.model.fromStore.id!=null) ? self.model.fromStore.id :self.model.store.id,
            "itemCode": self.model.searchGRN.itemCode,
            "itemName": self.model.searchGRN.itemName
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
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }else{
         if( self.model.searchGRN.itemName !=undefined &&  ( self.model.searchGRN.itemName.replace(/\s/g, "").length) > 0){
                        var list = $filter('filter')(self.model.items, {
                                                         $: self.model.searchGRN.itemName
                                                       });
                        PaginationService.searchDataByField( list, self.model.pagination);
                    }

        }
      }
      else if(self.model.searchGRN.itemCode){

         if ( self.model.searchGRN.itemCode!=undefined  &&  (self.model.searchGRN.itemCode.replace(/\s/g, "").length) == 2  ) {

          var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;;

          // self.model.SelectedItemsIds = [];

          var data = {
            // "isConsignment": self.model.isConsignment,
            "assetTypeId": self.model.poType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "requestingStoreId": self.model.fromStore.id,
            "itemCode": self.model.searchGRN.itemCode,
            "itemName": self.model.searchGRN.itemName
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
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }else{
                 if( self.model.searchGRN.itemCode !=undefined &&  ( self.model.searchGRN.itemCode.replace(/\s/g, "").length) > 0){
                                var list = $filter('filter')(self.model.items, {
                                                                 $: self.model.searchGRN.itemCode
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

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be less than to date');
        isValid = false;
      }

      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }

    var GetApprovedPOList = function () {
      if (ValidateDate()) {
    	  var URI = CONSTANTS.GLOBAL.AGAINST_PO_SEARCH + (self.model.store.id ==undefined ? 0 :self.model.store.id);
        if (!self.model.vendorNameForPO) {
          self.model.vendorIdForPO = undefined;
        }
        var data = {
          // "isConsignment": self.model.isConsignment,
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "vendorId": self.model.vendorIdForPO,
          "assetTypeId": self.model.poType.id,
          "unitId" : $scope.unitId,
          "poNumber" :self.model.searchPONumber
        }
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.poItemList = response.data;
            angular.forEach(self.model.poItemList, function (po) {
              po.disFlag = false;
              po["isChecked"] = false;
              po.isSelectedIndent = false;
              var dueDate = response.data.dueDate;
              angular.forEach(po.items, function (item) {
                item["isChecked"] = false;
              });
            });

            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            self.model.poItemList = [];
            growl.error(err.data.message);
          });
      }
    }

    function SearchVendorForGRN(search) {
      if (search.length < 1) return;

      if ((!self.model.grnPrevSearch || self.model.grnPrevSearch != search) && (search.length == 1)) {
        self.model.grnPrevSearch = search;
        var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
          .then(function (response) {
            if (!!response.data && response.data.length > 0) {
              self.model.grnVendorNames = response.data;
              $rootScope.stopLoader();
              return $filter('filter')(self.model.grnVendorNames, {
                $: search
              });
            } else {
              $rootScope.stopLoader();
              return vendorNotFound(search);
            }
          }, function (err) {
            $rootScope.stopLoader();
            return vendorNotFound(search);
          });
      } else {
        if (!!self.model.grnVendorNames && self.model.grnVendorNames.length > 0 && self.model.grnVendorNames[0].itemFound != undefined && !self.model.grnVendorNames[0].itemFound) {
          return vendorNotFound(search);
        } else {
          return $filter('filter')(self.model.grnVendorNames, {
            $: search
          });
        }
      }
    }

    function vendorNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor not found with name " + search,
        "search": search
      };
      self.model.grnVendorNames = [item];
      return self.model.grnVendorNames;
    }

    function AddVendor(vendor) {
      if (!vendor.id) {
        self.model.vendorNameForPO = vendor.search;
        self.model.vendorIdForPO = undefined;
      } else {
        self.model.vendorNameForPO = vendor.vendorName;
        self.model.vendorIdForPO = vendor.id;
      }
    }

    var Expand = function (item, poItemList) {
      angular.forEach(poItemList, function (value) {
        value.Expanded = false;
      });

      item.Expanded = !item.Expanded;
    }

    var SelectTax = function (tax, item) {
      if (!!tax.taxId) {
        item.selectedTaxPercent = tax.taxPercent.toFixed(2);
        item.discountAmount = parseFloat((item.grnAmount) * (item.discountPercent / 100)).toFixed(2);
        self.model.disAmount = parseInt(item.discountAmount);
        item.taxAmount = ((item.grnAmount-self.model.disAmount) * (item.selectedTaxPercent/100));
        item.lcopAmount = (((item.grnAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + (item.receivedQtyBonusCalculation!=undefined && item.receivedQtyBonusCalculation!=null ? parseInt(item.receivedQtyBonusCalculation):0)));
        $timeout( function(){
          $scope.calculateRoundoff1();
          if(self.model.discountType == '2'){
            $scope.calculateAmountDiscount();
          }
        },1000);
      }
      else {
        item.selectedTaxPercent = "";
      }
    }
    
    var AddItems = function () {
    	if (self.model.SelectedItemsIds.length == 0) {
            growl.error('Please select items to add');
            return false;
          }else {
        	  
        	  ActualAddItems();
        	  
        	  /*
        	    var stopData = {
    	                "itemIds": self.model.SelectedItemsIds,
    	                "storeId": self.model.store.id    	      		 
    	              };
        	    
        	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                 var StopTransactionData = responseData;  
                 if(StopTransactionData.length > 0 ){    
                          var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                          var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                          growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");
                          self.model.SelectedItemsIds = [];
                          self.model.items = [];
                          $rootScope.stopLoader();                     
                          return false;
                  }
                 else{
                	 ActualAddItems();
                 }
        	  });
          */}    	
    }
    
    var ActualAddItems = function () {    	
    	var URI = CONSTANTS.GLOBAL.POCreation_AddItems+ "/"+$scope.organizationId + "/"+$scope.unitId;    
      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.store.id,
        "isPoOrGrn":1
      };            
           
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
              angular.forEach(self.model.itemDetails, function(i,index){
                if(i.itemName == undefined || i.itemName == null || i.itemName ==''){
                  self.model.itemDetails.splice(index, 1);
                }
              });
         	 self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
              if (self.model.taxList == undefined || self.model.taxList.length == 0) {
                self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
              }
              response.data.itemDetailsList.forEach(function (value, key) {
                // self.model.itemDetails.push(value);
                value['poQuantity'] = 0;
                value['prQuantity'] = '-';
                value['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];

                
              /*
				 * value['grnReceivedQuantity'] = 0; value['costOfPurchase'] =
				 * 0; value['mrp'] = 0;
				 */
                $scope.roundOff = ($scope.roundOff==undefined || $scope.roundOff==null ||isNaN($scope.roundOff)) ? 0.00 : $scope.roundOff;
                self.model.totalNetAmount = (self.model.totalNetAmount==undefined || self.model.totalNetAmount==null || isNaN(self.model.totalNetAmount)) ? 0.00 : self.model.totalNetAmount;
                self.model.totalNetAmount1 = (self.model.totalNetAmount1==undefined || self.model.totalNetAmount1==null || isNaN(self.model.totalNetAmount1)) ? 0.00 : self.model.totalNetAmount1;
                value['otherChargesTotal'] = 0;
                value['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];

                var isFound = self.model.itemDetails.find(function (val) {
                  return val.itemId == value.itemId;
                });
                value.selectedTax = self.model.taxList.find(function (val) {
                  return value.taxId == val.taxId;
                });
                if(value.selectedTax.taxPercent != 0 && value.selectedTax.taxPercent != undefined) {
                  value.selectedTaxPercent = value.selectedTax.taxPercent.toFixed(2);
                }
                if(value.selectedTax.taxPercent == 0) {
                  value.selectedTaxPercent = value.selectedTax.taxPercent;
                }
                if(value.selectedTax.taxPercent == undefined){
                  angular.forEach(self.model.taxList, function(taxdata){
                	  if(taxdata.taxName == "Tax 0%" || taxdata.taxName == "0" ){
                          value.selectedTax = taxdata;
                          value.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                            value.taxId= taxdata.taxId;
                        }
                  });
                }
                if(value.isBatchRequired == false){
                  value.batchNumber = 'NA';
                }
                if (self.model.poType.id == 1) {
                  selectDefaultUOM(value);
                  if(value.isThreeUOMApplicable == false){
                    value.uomListdata = value.uom;
                    value.uom = [];
                    angular.forEach(value.uomListdata, function(data){
                      if(data.uomType != 'Stock'){
                        value.uom.push(data);
                      }
                    });
                  }
                  CalculateStock(value, value.uom, value.selectedUom);
                } else if (self.model.poType.id == 2) {
                  value.newMaxOrderQuantity = value.maxOrderQuantity;
                  value.newMaxStoreQuantity = value.maxStoreQuantity;
                }
                if (value.maxOrderQuantity <= 0) {
                  growl.error("Maxorder Quantity is less than 0 for an item.");
                  if(self.model.prRequestTypeId == 1){
                    var obj = {
                      itemId: '',
                      itemCode: '',
                      itemName: '',
                      poBonusQuantity: '',
                      bonusQuantityList: '',
                      bonusSelectedUom: '',
                      bonusUom: '',
                      bonusUomType: '',
                      bonusUomTypeId: '',
                      bonusUomUnit: '',
                      bonusUomUnitId: '',
                      genericId: '',
                      genericName: '',
                      grnAccessoriesMapper: '',
                      grnAmount: '',
                      grnStagedLists: '',
                      manufacturerId: '',
                      manufacturerName: '',
                      maxOrderQuantity: '',
                      maxStoreQuantity: '',
                      netAmount: '',
                      newMaxOrderQuantity: '',
                      newMaxStoreQuantity: '',
                      newPOQty: '',
                      poBonusQuantity: '',
                      poDetailId: '',
                      poGrnQuantity: '',
                      poIds: '',
                      poLeaseQuantity: '',
                      poNumbersList: '',
                      poOtherCharges: '',
                      poPendingQuantity: '',
                      poQuantity: '',
                      poStagedList: '',
                      poUom: '',
                      poUomType: '',
                      poUomTypeId: '',
                      poUomUnit: '',
                      poUomUnitId: '',
                      prList: '',
                      selectedTax: '',
                      selectedTaxPercent: '',
                      selectedUom: '',
                      totalPoQty: '',
                      uom: '',
                      mrp: '',
                      taxAmount: '',
                      pendingQuantity: '',
                      disableBonusTextField:false
                    };
                    self.model.itemDetails.push(obj);
                  }
                }
                if (!isFound && (value.maxOrderQuantity > 0)) {
                  var obj = angular.copy(value);
                  obj.vendorId = self.model.vendorId;
                  // start code to resolved bug HIS-8115 GRN - Select item
					// does not get tax code in the dropdown default selected
					// from item master
                  obj.selectedTax = self.model.taxList.find(function (val) {
                      return value.taxId == val.taxId;
                    });
                // end code to resolved bug HIS-8115 GRN - Select item does not
				// get tax code in the dropdown default selected from item
				// master
                  
                  if(value.goodReceiptNoteDetailDto!=null){
                	  obj.mrp = value.goodReceiptNoteDetailDto.lastMrp;
                	  obj.costOfPurchase = value.goodReceiptNoteDetailDto.lastCOP;
                  	value.goodReceiptNoteDetailDto.lastGrnUomTypeId;
                  }
                  if(value.isBatchRequired == false){
                    obj.batchNumber = 'NA';
                  }
                  console.log(value.goodReceiptNoteDetailDto,JSON.stringify(value.goodReceiptNoteDetailDto));
                  
                  self.model.itemDetails.push(obj);
                  value.grnAccessoriesMapper = [];
                }
              });

              var popup = angular.element('#addNewItemWasteStore');
              popup.modal('hide');

              self.model.SelectedItemsIds = [];
              self.model.items = [];

              $rootScope.stopLoader();

        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
     }

     var AddItems1 = function (itemDetails, itemsearch, model, label, index) {
       self.model.SelectedItemsIds.push(itemsearch.itemId);
    	if (self.model.SelectedItemsIds.length == 0) {
            growl.error('Please select items to add');
            return false;
          }else {
        	  

              ActualAddItems1(itemDetails, itemsearch, model, label, index);
              $timeout( function(){
                if(self.model.poType.id == 1){
                  var qty = 'qty_'+self.model.itemDetails.length;
                  document.getElementById(qty).focus();
                }
                if(self.model.poType.id == 2){
                  var quantity = 'qtys_'+self.model.itemDetails.length;
                  document.getElementById(quantity).focus();
                }
              },3000);
             
        	  
        	  /*
        	    var stopData = {
    	                "itemIds": self.model.SelectedItemsIds,
    	                "storeId": self.model.store.id    	      		 
    	              };
        	    
        	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                 var StopTransactionData = responseData;  
                 if(StopTransactionData.length > 0 ){    
                          var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                          var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                          growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");
                          self.model.SelectedItemsIds = [];
                          self.model.items = [];
                          $rootScope.stopLoader();                     
                          return false;
                  }
                 else{
                  ActualAddItems1(itemDetails, itemsearch, model, label, index);
                  $timeout( function(){
                    if(self.model.poType.id == 1){
                      var qty = 'qty_'+self.model.itemDetails.length;
                      document.getElementById(qty).focus();
                    }
                    if(self.model.poType.id == 2){
                      var quantity = 'qtys_'+self.model.itemDetails.length;
                      document.getElementById(quantity).focus();
                    }
                  },3000);
                 }
        	  });
          */}    	
    }
    
    var ActualAddItems1 = function (itemDetails, itemsearch, model, label, index) {
      self.model.SelectedItemsIds.push(itemsearch.itemId);    	
    	var URI = CONSTANTS.GLOBAL.POCreation_AddItems+ "/"+$scope.organizationId + "/"+$scope.unitId;    
      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.store.id,
        "isPoOrGrn":1
      };            
           
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          angular.forEach(self.model.itemDetails, function(i,key){
            if(i.itemName == ''){
              self.model.itemDetails.splice(index, 1);
            }
          });
         	 self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
              if (self.model.taxList == undefined || self.model.taxList.length == 0) {
                self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
              }
              response.data.itemDetailsList.forEach(function (value, key) {
                // self.model.itemDetails.push(value);
//            	  var sumOfBonusReceivedQuantity=0;
//                  if(value.grnBonusBatchExpiry!=undefined && value.grnBonusBatchExpiry!=null && value.grnBonusBatchExpiry.length>0)
//                  	{
//                  angular.forEach(value.grnBonusBatchExpiry, function (grnBonusBatchExpiryCount) {
//                  	sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+grnBonusBatchExpiryCount.bonusQuantity;
//                  	self.model.bonusBatchExpiryData.push({
//                  		grnBonusBatchExpiryId:grnBonusBatchExpiryCount.grnBonusBatchExpiryId,
//                  		batchNo: grnBonusBatchExpiryCount.batchNo,
//                  		expiryDate: grnBonusBatchExpiryCount.expiryDate,
//                          bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity
//                      });
//                    });
//                      }
//                  else
//                  	{
//                  	self.model.bonusBatchExpiryData.push({
//                  		grnBonusBatchExpiryId:null,
//                  		batchNo: "",
//                  		expiryDate: "",
//                  		bonusQuantity: ""
//                      });
//                  	}
//                  value.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
                value['poQuantity'] = 0;
                value['prQuantity'] = '-';
                value['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];

                
              /*
				 * value['grnReceivedQuantity'] = 0; value['costOfPurchase'] =
				 * 0; value['mrp'] = 0;
				 */
                $scope.roundOff = ($scope.roundOff==undefined || $scope.roundOff==null ||isNaN($scope.roundOff)) ? 0.00 : $scope.roundOff;
                self.model.totalNetAmount = (self.model.totalNetAmount==undefined || self.model.totalNetAmount==null || isNaN(self.model.totalNetAmount)) ? 0.00 : self.model.totalNetAmount;
                self.model.totalNetAmount1 = (self.model.totalNetAmount1==undefined || self.model.totalNetAmount1==null || isNaN(self.model.totalNetAmount1)) ? 0.00 : self.model.totalNetAmount1;
                value['otherChargesTotal'] = 0;
                value['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];

                var isFound = self.model.itemDetails.find(function (val,k) {
                  return val.itemId == value.itemId && k==index;
                });
                value.selectedTax = self.model.taxList.find(function (val) {
                  return value.taxId == val.taxId;
                });
                if(value.selectedTax.taxPercent != 0 && value.selectedTax.taxPercent != undefined) {
                  value.selectedTaxPercent = value.selectedTax.taxPercent.toFixed(2);
                }
                if(value.selectedTax.taxPercent == 0) {
                  value.selectedTaxPercent = value.selectedTax.taxPercent;
                }
                if(value.selectedTax.taxPercent == undefined){
                  angular.forEach(self.model.taxList, function(taxdata){
                	  if(taxdata.taxName == "Tax 0%" || taxdata.taxName == "0" ){
                          value.selectedTax = taxdata;
                          value.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                            value.taxId= taxdata.taxId;
                        }
                  });
                }
                if(value.isBatchRequired == false){
                  value.batchNumber = 'NA';
                }
                if(value.isThreeUOMApplicable == false){
                  value.uomListdata = value.uom;
                  value.uom = [];
                  angular.forEach(value.uomListdata, function(d){
                    if(d.uomTypeId != 2){
                      value.uom.push(d);
                    }
                  })
                }
                if (self.model.poType.id == 1) {
                  selectDefaultUOM(value);
                  if(value.isThreeUOMApplicable == false){
                    CalculateStock(value, value.uomListdata, value.selectedUom);
                  }
                  else{
                    CalculateStock(value, value.uom, value.selectedUom);
                  }
                } else if (self.model.poType.id == 2) {
                  value.newMaxOrderQuantity = value.maxOrderQuantity;
                  value.newMaxStoreQuantity = value.maxStoreQuantity;
                }
                if (value.maxOrderQuantity <= 0) {
                  growl.error("Maxorder Quantity is less than 0 for an item.");
                  if(self.model.prRequestTypeId == 1){
                    var obj = {
                      itemId: '',
                      itemCode: '',
                      itemName: '',
                      poBonusQuantity: '',
                      bonusQuantityList: '',
                      bonusSelectedUom: '',
                      bonusUom: '',
                      bonusUomType: '',
                      bonusUomTypeId: '',
                      bonusUomUnit: '',
                      bonusUomUnitId: '',
                      genericId: '',
                      genericName: '',
                      grnAccessoriesMapper: '',
                      grnAmount: '',
                      grnStagedLists: '',
                      manufacturerId: '',
                      manufacturerName: '',
                      maxOrderQuantity: '',
                      maxStoreQuantity: '',
                      netAmount: '',
                      newMaxOrderQuantity: '',
                      newMaxStoreQuantity: '',
                      newPOQty: '',
                      poBonusQuantity: '',
                      poDetailId: '',
                      poGrnQuantity: '',
                      poIds: '',
                      poLeaseQuantity: '',
                      poNumbersList: '',
                      poOtherCharges: '',
                      poPendingQuantity: '',
                      poQuantity: '',
                      poStagedList: '',
                      poUom: '',
                      poUomType: '',
                      poUomTypeId: '',
                      poUomUnit: '',
                      poUomUnitId: '',
                      prList: '',
                      selectedTax: '',
                      selectedTaxPercent: '',
                      selectedUom: '',
                      totalPoQty: '',
                      uom: '',
                      mrp: '',
                      taxAmount: '',
                      pendingQuantity: ''
                    };
                    self.model.itemDetails.push(obj);
                  }
                }
                if (!isFound && (value.maxOrderQuantity > 0)) {
                  var obj = angular.copy(value);
                  obj.vendorId = self.model.vendorId;
                  obj.bonusReceivedQuantity=0;
                  // start code to resolved bug HIS-8115 GRN - Select item
					// does not get tax code in the dropdown default selected
					// from item master
                  obj.selectedTax = self.model.taxList.find(function (val) {
                      return value.taxId == val.taxId;
                    });
                // end code to resolved bug HIS-8115 GRN - Select item does not
				// get tax code in the dropdown default selected from item
				// master
                  
                  if(value.goodReceiptNoteDetailDto!=null){
                	  obj.mrp = value.goodReceiptNoteDetailDto.lastMrp;
                	  obj.costOfPurchase = value.goodReceiptNoteDetailDto.lastCOP;
                  	value.goodReceiptNoteDetailDto.lastGrnUomTypeId;
                  }
                  if(value.isBatchRequired == false){
                    obj.batchNumber = 'NA';
                  }
                  console.log(value.goodReceiptNoteDetailDto,JSON.stringify(value.goodReceiptNoteDetailDto));
                  angular.forEach(self.model.itemDetails, function(val,key){
                    if(key == index){
                      self.model.itemDetails.splice(key, 1);
                    }
                  })
                  self.model.itemDetails.push(obj);
                  value.grnAccessoriesMapper = [];
                }
              });

              var popup = angular.element('#addNewItemWasteStore');
              popup.modal('hide');

              self.model.SelectedItemsIds = [];
              self.model.items = [];

              $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
        if(self.model.poType.id == 1){
          var qty = 'qty_'+self.model.itemDetails.length;
          document.getElementById(qty).focus();
        }
        if(self.model.poType.id == 2){
          var quantity = 'qtys_'+self.model.itemDetails.length;
          document.getElementById(quantity).focus();
        }
     }
    
    var selectDefaultUOM = function (item) {
      item.selectedUom = item.uom[0];
      if(item.goodReceiptNoteDetailDto!=null){
    	  item.goodReceiptNoteDetailDto.lastGrnUomTypeId;
    	  item.selectedUom = item.uom.find(function (val) {
    		  return val.uomTypeId ==item.goodReceiptNoteDetailDto.lastGrnUomTypeId;
      	 });
      }
      
      item.bonusSelectedUom = item.uom[0];
      var uomData = [];
      if(item.isThreeUOMApplicable == false){
        if(item.uomListdata == undefined || item.uomListdata == null){
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
        }
        else{
          angular.forEach(item.uomListdata, function (data) {
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
        }
      }
      else{
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
      }
      item.bonusUom = uomData;
      item.poUom = uomData;
    }
    
    $scope.checkBatch=function(item,index)
    {
//    	if(self.model.bonusBatchExpiryData.length>1)
//    		{
    		for (let i = 0; i < self.model.bonusBatchExpiryData.length - 1; i++) {
                for (let j = i + 1; j < self.model.bonusBatchExpiryData.length; j++) {
                	if (self.model.bonusBatchExpiryData[i].batchNo.toUpperCase() === self.model.bonusBatchExpiryData[j].batchNo.toUpperCase()) {
                		self.model.bonusBatchExpiryData[j].batchNo="";
                     }
                }
    		}
//    	angular.forEach(self.model.bonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
//    		var a=item.batchNo.toUpperCase();
//    		var b=grnBonusBatchExpiryCount.batchNo.toUpperCase();
//    		if(a!="" && a==b)
//    			{
//    			growl.warning("Bonus Batch Should Not Be Same");
//    			item.batchNo="";
//    			self.model.bonusBatchExpiryData[index].batchNo="";
//    			}
//    	})
//    		}
    }
    $scope.closeBonusBatchExpiry =function()
    {
    	self.model.bonusBatchExpiryData=[];
    }
    $scope.addBonusQuantity = function (date) {
        self.model.bonusBatchExpiryData.push({ grnBonusBatchExpiryId:null,batchNo:'',expiryDate: date, bonusQuantity: parseInt(0) });
      }
    $scope.lessBonusQuantity = function (item) {
        var index = self.model.bonusBatchExpiryData.indexOf(item);
        self.model.bonusBatchExpiryData.splice(index, 1);
      }
    var bonusBatchExpiryInfoOnClick = function (batchItem, index) {
    	if(batchItem.bonusSelectedUom==undefined || batchItem.bonusSelectedUom==null)
    		{
    		growl.error("Please Select Bonus UOM");
    		return false;
    		}
        self.model.bonusSelectedItemBatchExpiryIndex = index;
        var sumOfBonusReceivedQuantity=0;
        if(batchItem.grnBonusBatchExpiryData!=undefined && batchItem.grnBonusBatchExpiryData!=null && batchItem.grnBonusBatchExpiryData.length>0)
        	{
        angular.forEach(batchItem.grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
        	sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
        	self.model.bonusBatchExpiryData.push({
        		grnBonusBatchExpiryId:grnBonusBatchExpiryCount.grnBonusBatchExpiryId,
        		batchNo: grnBonusBatchExpiryCount.batchNo,
        		expiryDate: grnBonusBatchExpiryCount.expiryDate,
                bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity
            });
          });
        batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
            }
        else
        	{
        	sumOfBonusReceivedQuantity=0;
        	self.model.bonusBatchExpiryData.push({
        		grnBonusBatchExpiryId:null,
        		batchNo: "",
        		expiryDate: "",
        		bonusQuantity: ""
            });
        	}
        
    }

    var bonusBatchExpiryModalOnClick = function () {
      self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData = [];
      var modal_list = self.model.bonusBatchExpiryData;
      var count=0;
      modal_list.forEach(function (modal, index) {
    	  if(modal.batchNo == undefined || modal.batchNo == null || modal.batchNo == "")
          {
    		  count++;
          }
    	  if(self.model.poType.id != 2){
    		  if(modal.expiryDate == undefined || modal.expiryDate == null || modal.expiryDate == "")
        	  {
        		  count++;
        	  }
    	  }
    	  if(modal.bonusQuantity == undefined || modal.bonusQuantity == null || modal.bonusQuantity == "")
    	  {
    		  count++;
    	  }
      });
      if(count>0)
    	  {
    	  growl.error("Please Add All Required Fields");
    	  return false;
    	  }
      modal_list.forEach(function (modal, index) {
        if(modal.batchNo=="" || modal.bonusQuantity==0)
        {
          $scope.lessBonusQuantity(modal);
        }
      });
      modal_list.forEach(function (modal, index) {
        self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.push({
          grnBonusBatchExpiryId:modal.grnBonusBatchExpiryId!=null ? modal.grnBonusBatchExpiryId :null ,
          batchNo: modal.batchNo,
          expiryDate: modal.expiryDate,
          bonusQuantity: modal.bonusQuantity
        });
      });
      if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom==undefined || 
    		  self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom==null || 
    		  self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom.length==0)
      {
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom=self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uom
      }
      var sumOfBonusReceivedQuantity=0;
      if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=undefined && self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=null && self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.length>0)
      {
        angular.forEach(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
          sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
        });
      }
      self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity=sumOfBonusReceivedQuantity;
      if(self.model.poType.id != 2){
      if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].isThreeUOMApplicable==true)
      {
        if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusSelectedUom.uomType=="Purchase")
        {
    	    self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCon = (self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity+"*"+ self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion+"*"+ self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCalculation = (self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity * self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion * self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[2].uomUnit;
        }
        if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusSelectedUom.uomType=="Stock")
        {
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity+"*"+ self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCalculation =(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity * self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[2].uomUnit;
        }
        if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusSelectedUom.uomType=="Dispense")
        {
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCalculation=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[2].uomUnit;
        }
      }
      else
      {
    	  if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusSelectedUom.uomType=="Purchase")
        {
    		  self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity +"*"+ self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCalculation=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity * self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomListdata[2].uomUnit;
        }
        if(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusSelectedUom.uomType=="Dispense")
        {
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].receivedQtyBonusCalculation=(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex].uomListdata[2].uomUnit;
        }
      }
    }
      if(self.model.poType.id != 2){
      CalculateBonusQuantity(self.model.itemDetails[self.model.bonusSelectedItemBatchExpiryIndex]);
      }
      self.model.bonusBatchExpiryData=[];
      self.model.ClosePopup('Close', 'bouns_selectBatchExpiry_modal');
    }

    var bonusUomInfoOnClick = function (batchItem, index) {
      self.model.bonusSelectedUOMList = [];
      self.model.bonusSelectedUOMUnit = [];
      self.model.bonusSelectedUOMListData = [];
      self.model.bonusSelectedItemIndex = index;
      if (batchItem)
      {
        var _boUom = batchItem.bonusUom.filter(function (uom) {
          return uom;
        });

        if(batchItem.isThreeUOMApplicable == false){
          var b_uom = batchItem.uomListdata.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
        }
        else{
          var b_uom = batchItem.uom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
        }

        b_uom.forEach(function (item, index) {
          var data = { 'uomUnit': item.uomUnit, 'uomUnitId': item.uomUnitId };
          self.model.bonusSelectedUOMUnit.push(data);

          var uomUnitId = null, conversion = 0;
          if (!_boUom || _boUom.length == 0) {
            uomUnitId = data;
            conversion = item.conversion;
          }
          else {
            uomUnitId = _boUom[index].uomUnitId;
            conversion = _boUom[index].conversion;
          }

          self.model.bonusSelectedUOMListData.push({
            uomType: item.uomType,
            uomUnitId: uomUnitId,
            conversion: conversion,
            ipUom: item.ipUom,
            opUom: item.opUom,
            storeUom: item.storeUom,
            uomTypeId: item.uomTypeId,
            id: _boUom[index].id
          });
        });
        angular.forEach(self.model.bonusSelectedUOMListData, function(val){
          if(batchItem.isThreeUOMApplicable == false){
            if(val.uomTypeId != 2){
              self.model.bonusSelectedUOMList.push(val);
            }
          }
          else{
            self.model.bonusSelectedUOMList.push(val);
          }
        })
      }
    }

    var bonusUomModalOnClick = function () {
      self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusUom = [];
      var modal_list = [];
      if(self.model.itemDetails[self.model.bonusSelectedItemIndex].isThreeUOMApplicable == false){
        modal_list = self.model.bonusSelectedUOMListData;
      }
      else{
        modal_list = self.model.bonusSelectedUOMList;
      }
      if(self.model.itemDetails[self.model.bonusSelectedItemIndex].isThreeUOMApplicable==true)
      {
        if(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusSelectedUom.uomType=="Purchase")
        {
    	    self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCon = (self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity+"*"+modal_list[0].conversion+"*"+ modal_list[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCalculation = (self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity * modal_list[0].conversion * modal_list[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].uomUnitDataBonus = modal_list[2].uomUnitId.uomUnit;
        }
        if(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusSelectedUom.uomType=="Stock")
        {
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity+"*"+modal_list[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCalculation =(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity * modal_list[1].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].uomUnitDataBonus = modal_list[2].uomUnitId.uomUnit;
        }
        if(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusSelectedUom.uomType=="Dispense")
        {
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCalculation=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].uomUnitDataBonus = modal_list[2].uomUnitId.uomUnit;
        }
      }
      else
      {
    	  if(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusSelectedUom.uomType=="Purchase")
        {
    		  self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity +"*"+ modal_list[0].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCalculation=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity * modal_list[0].conversion);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemIndex].uomListdata[2].uomUnit;
        }
        if(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusSelectedUom.uomType=="Dispense")
        {
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCon=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].receivedQtyBonusCalculation=(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusReceivedQuantity);
          self.model.itemDetails[self.model.bonusSelectedItemIndex].uomUnitDataBonus = self.model.itemDetails[self.model.bonusSelectedItemIndex].uomListdata[2].uomUnit;
        }
      }
      if (ValidConversionFactor(self.model.bonusSelectedUOMList)) {
        if (modal_list) {
          modal_list.forEach(function (modal, index) {
            self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusUom.push({
              uomUnitId: modal.uomUnitId,
              conversion: parseInt(modal.conversion),
              ipUom: modal.ipUom,
              opUom: modal.opUom,
              storeUom: modal.storeUom,
              uomTypeId: modal.uomTypeId,
              id: modal.id
            });
          });
          self.model.ClosePopup('Close', 'bouns_selectUOM_modal');
        }
      }
    }

    var uomInfoOnClick = function (batchItem, index) {
      self.model.selectedUOMList = [];
      self.model.selectedUOMUnit = [];
      self.model.selectedUOMListData = [];
      self.model.selectedItemIndex = index;
      if (batchItem){
        var _poUom = batchItem.poUom.filter(function (uom) {
          return uom;
        });

        var p_uom = [];
        if(batchItem.isThreeUOMApplicable == false){
          p_uom = batchItem.uomListdata.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
        }
        else{
          p_uom = batchItem.uom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
        }

        p_uom.forEach(function (item, index) {
          var data = { 'uomUnit': item.uomUnit, 'uomUnitId': item.uomUnitId };
          self.model.selectedUOMUnit.push(data);

          var uomUnitId = null, conversion = 0, id = 0;
          if (!_poUom || _poUom.length == 0) {
            uomUnitId = data;
            conversion = item.conversion;
          }
          else {
            uomUnitId = _poUom[index].uomUnitId;
            conversion = _poUom[index].conversion;
          }

          self.model.selectedUOMListData.push({
            uomType: item.uomType,
            uomUnitId: uomUnitId,
            conversion: conversion,
            id: _poUom[index].id,
            ipUom: item.ipUom,
            opUom: item.opUom,
            storeUom: item.storeUom,
            uomTypeId: item.uomTypeId
          });
        });
        angular.forEach(self.model.selectedUOMListData, function(val){
          if(batchItem.isThreeUOMApplicable == false){
            if(val.uomTypeId != 2){
              self.model.selectedUOMList.push(val);
            }
          }
          else{
            self.model.selectedUOMList.push(val);
          }
        })
      }
    }

    var ValidConversionFactor = function (data) {
      var isValid = true;
      data.find(function (x) {
        if (!parseInt(x.conversion)) {
          growl.error('Conversion factor should valid');
          isValid = false;
        }
      });

      return isValid;
    }

    var uomModalOnClick = function () {
      self.model.itemDetails[self.model.selectedItemIndex].poUom = [];
      var modal_list = [];
      if(self.model.itemDetails[self.model.selectedItemIndex].isThreeUOMApplicable == false){
        modal_list = self.model.selectedUOMListData;
      } 
      else{
        modal_list = self.model.selectedUOMList;
      }
      if(self.model.itemDetails[self.model.selectedItemIndex].isThreeUOMApplicable==true)
      {
        if(self.model.itemDetails[self.model.selectedItemIndex].selectedUom.uomType=="Purchase")
        {
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCon=(self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity +"*"+ modal_list[0].conversion +"*"+ modal_list[1].conversion);
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCalculation = (self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity * modal_list[0].conversion * modal_list[1].conversion);
          self.model.itemDetails[self.model.selectedItemIndex].uomUnitData = modal_list[2].uomUnitId.uomUnit;
        }
        if(self.model.itemDetails[self.model.selectedItemIndex].selectedUom.uomType=="Stock")
        {
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCon=(self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity +"*"+ modal_list[1].conversion);
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCalculation = (self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity * modal_list[1].conversion);
          self.model.itemDetails[self.model.selectedItemIndex].uomUnitData = modal_list[2].uomUnitId.uomUnit;
        }
        if(self.model.itemDetails[self.model.selectedItemIndex].selectedUom.uomType=="Dispense")
        {
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCon=(self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity);
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCalculation = (self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity);
          self.model.itemDetails[self.model.selectedItemIndex].uomUnitData = modal_list[2].uomUnitId.uomUnit;
        }
      }
      else
      {
    	  if(self.model.itemDetails[self.model.selectedItemIndex].selectedUom.uomType=="Purchase")
        {
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCon=(self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity +"*"+ modal_list[0].conversion);
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCalculation = (self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity * modal_list[0].conversion);
          self.model.itemDetails[self.model.selectedItemIndex].uomUnitData = self.model.itemDetails[self.model.selectedItemIndex].uomListdata[2].uomUnit;
        }
        if(self.model.itemDetails[self.model.selectedItemIndex].selectedUom.uomType=="Dispense")
        {
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCon=(self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity);
          self.model.itemDetails[self.model.selectedItemIndex].receivedQtyCalculation = (self.model.itemDetails[self.model.selectedItemIndex].grnReceivedQuantity);
          self.model.itemDetails[self.model.selectedItemIndex].uomUnitData = self.model.itemDetails[self.model.selectedItemIndex].uomListdata[2].uomUnit;
        }
      }
      if (ValidConversionFactor(self.model.selectedUOMList)) {
        if (modal_list) {
          modal_list.forEach(function (modal, index) {
            self.model.itemDetails[self.model.selectedItemIndex].poUom.push({
              uomUnitId: modal.uomUnitId,
              id : modal.id,
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
      self.model.intendItemIds = [];
      if (indent.isSelectedIndent) {
        self.model.poItemList.forEach(function(data) {
					if (indent.vendorName == data.vendorName && (indent.fromStoreName == data.fromStoreName && indent.storeName == data.storeName)) {
						data.disFlag = false;
					} else {
						data.disFlag = true;
					}
				});
        items.map(function (x) {
          self.model.indentItemDetails.push(x);
          x.isSelectedItem = true;
          x.isChecked = true;
          indent.isChecked = true;
          self.model.isSelectedAllItem = true;
          return self.model.intendItemIds.push(x.itemId);
        });
      } else {
        self.model.poItemList.forEach(function(data) {
          data.disFlag = false;
				});
        items.forEach(function (x) {
          x.isSelectedItem = false;
          x.isChecked = false;
        });
        indent.isChecked = false;
        self.model.isSelectedAllItem = false;
        items.map(function (x) {
          var idx = self.model.indentItemDetails.indexOf(x);
          if (idx > -1) {
            self.model.indentItemDetails.splice(idx, 1);
          }
        });
      }
    }

    var selectAllIndentRecords = function () {
      if (self.model.selectAllRows) {
        self.model.isSelectedIndent=[];
        self.model.SelectedItemsIds = self.model.poItemList.map(function (item) {
          item.isSelectedIndent = true;
          item.isChecked = true;
          item.items.forEach(function (list){
            list.isSelectedItem = true;
            self.model.isSelectedAllItem = true;
            list.isChecked = true;
          });
          self.model.isSelectedIndent.push(item);
          return item.id;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.poItemList.forEach(function (item) {
          item.isSelectedIndent = false;
          item.isChecked = false;
          item.items.forEach(function (list){
            list.isSelectedItem = false;
            self.model.isSelectedAllItem = false;
            list.isChecked = false;
            self.model.indentItemDetails.push(list);
          });
        });
      }
    }

    var SelectIndentItem = function (item, indent) {
      var itemIds = self.model.intendItemIds;
      if (self.model.isSelectedIndent != indent) {
        angular.forEach(self.model.poItemList, function (po) {
          if (po.id !== indent.id) {
            po.items.forEach(function (x) {
              x.isSelectedItem = false;
              x.isChecked = false;
              po.isChecked = false;
            });
          }
        });
      }

      if (item.isSelectedItem) {
        item.isChecked = true;
        indent.isChecked = true;
        itemIds.push(item.itemId);
        self.model.isSelectedIndent = indent
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
        self.model.isSelectedIndent = indent;
      }
      // else {
      // self.model.isSelectedIndent = null;
      // }
    }

    var SelectIndentAllItem = function () {
      if (self.model.isSelectedAllItem) {
        self.model.poItemList.forEach(function (list){
          self.model.SelectedItemsIds = list.items.map(function (item) {
            item.isSelectedItem = true;
            item.isChecked = true;
            list.isChecked = true;
            return item.id;
          });
        })
        
      } else {
        self.model.SelectedItemsIds = [];
        self.model.poItemList.forEach(function (item) {
          item.items.forEach(function (list){
            list.isSelectedItem = false;
            item.isChecked = false;
            list.isChecked = false;
          });
        });
      }
    }

    var updatePOItemDetails = function (poResponseData) {
      var newItemsHash = {};
      $scope.list = [];
      var itemDetailsHash = {};
      self.model.itemDetails.forEach(function (itemDetail) {
        itemDetailsHash[itemDetail.itemId] = itemDetail;
      });

      poResponseData.forEach(function (poItem) {
        self.model.vendorId = poItem.vendorId;
        self.model.vendorName = poItem.vendorName;
        var poNo = poItem.poNo;
        self.model.poId = poItem.id;
        var poDate = poItem.poDate;
        poItem.items.forEach(function (item) {
          var itemId = item.itemId;
          var poId = item.id;
          var poQuantity = Number(item.poQuantity);
          var bonusQuantity = Number(item.bonusQuantity);


          var poObj = { id: poId, poNo: poNo, poQuantity: poQuantity, poDate: poDate };
          var bonusQtyList = { bonusQuantity: bonusQuantity };

          if (itemDetailsHash.hasOwnProperty(itemId)) {
            // item found in already saved list
            var itemDetail = itemDetailsHash[itemId];
            if (itemDetail.poIds && itemDetail.poIds.length) {
              // poIds list is present
              if (itemDetail.poIds.indexOf(poId) == -1) {
                itemDetail.poNumbersList.push(poObj);
                itemDetail.bonusQuantityList.push(bonusQtyList);

                itemDetail.poIds.push(poId);
                itemDetail.poDetailId = poId;
                itemDetail['poDetailId'] = poId;
              } else {
                // DO nothing, Item is already preset in saved table
              }
            } else {
              // poIds list not present
              itemDetail.poNumbersList = [poObj]
              itemDetail.bonusQuantityList = [bonusQtyList]
              itemDetail.poIds = [poId];
              itemDetail.poDetailId = poId;
            }

          } 
          else if (newItemsHash.hasOwnProperty(poId)) {
            // item found in new item hash
            var savedItem = newItemsHash[poId];
            savedItem.poNumbersList.push(poObj);
            savedItem.bonusQuantityList.push(bonusQtyList);
            savedItem.poIds.push(poId);
            savedItem.poDetailId = poId;
          } else {
            // item not found in item hash
            var clonedItem = JSON.parse(JSON.stringify(item));
            delete clonedItem.id;
            // delete clonedItem.poQuantity;
            // delete clonedItem.bonusQuantity;
            clonedItem.poNumbersList = [poObj]
            clonedItem.bonusQuantityList = [bonusQtyList]
            clonedItem.poIds = [poId];
            clonedItem.poDetailId = poId;
            newItemsHash[itemId] = clonedItem;
            $scope.list.push(newItemsHash[itemId]);
          }
        });
      });

      // for (var item in $scope.list) {
      angular.forEach($scope.list, function(item){
        if (item.poPendingQuantity > 0) {
          item.grnStagedLists.push(
            {
              "stagedDcDate": self.model.dcDate,
              "stagedReceivedQuantity": "",
              "isEditable": true,
              "orginalApprovedQty":""
            }
          );
        }
        if(item.taxId){
          item.selectedTax = self.model.taxList.find(function (val) {
            return item.taxId == val.taxId;
          });
          if(item.taxPercent != undefined){
            item.selectedTaxPercent = item.taxPercent.toFixed(2);
          } else if (item.taxPercentage != undefined){
            item.selectedTaxPercent = item.taxPercentage.toFixed(2);
          }
          else{
            item.selectedTaxPercent = item.taxPercent;
          }
        }

        // if (!!newItemsHash[itemId].taxPercent) {
        // newItemsHash[itemId].selectedTaxPercent =
		    // newItemsHash[itemId].taxPercent.toFixed(2);
        // }
        
        // newItemsHash[itemId]['poQuantity'] = 0;
        item.otherChargesTotal = 0;
//        item.bonusReceivedQuantity = 0;
        item.selectedOtherCharges = [{
          otherChargesId: self.model.otherChargesDropdownList[0].id,
          amount: 0
        }];
        item.grnAccessoriesMapper = [];
        item.prList = [];
        item.costOfPurchase = item.cop;
        item.discountPercent = item.discount;
        
        // start code for HIS-9505 GRN form asset against PO issues - Approve
		    // quantity is showing 0
        if(item.specification != undefined){
        	item.itemSpecification = item.specification;
        }
        if(item.remark != undefined){
        	item.itemRemark = item.remark;
        }
        if(item.poAccessoriesMapper != undefined && item.poAccessoriesMapper.length > 0 ){
        	item.accessories = item.poAccessoriesMapper;
        	// self.model.accessoryList =
			    // newItemsHash[itemId].poAccessoriesMapper
        }
        let po = poResponseData.find(function (val) {
        	return item.poId == val.id;
        	});
        if(po.otherCharges != undefined && po.otherCharges.length > 0 ){
          item.selectedOtherCharges = po.otherCharges;
        }
        if(item.serviceProvider != undefined || item.serviceProviderId != undefined){
        	item.serviceProvider = item.serviceProvider;
        	item.providerId = item.serviceProviderId;
        }
        if(item.expectedPayment != undefined){
        	item.paymentTerms = item.expectedPayment
        }
        if(item.technicalSpecification != undefined){
        	item.technicalSpecs = item.technicalSpecification
        }
        // end code for HIS-9505 GRN form asset against PO issues - Approve
		    // quantity is showing 0
        
        
        self.model.itemDetails.push(item);

        var total = 0;
        angular.forEach(item.poNumbersList, function (value) {
          total = total + parseFloat(value.poQuantity);
        });
        item.totalPoQty = total;

        if (self.model.poType.id == 1) {
          selectDefaultUOM(item);
          item.selectedUom = item.uom.find(function (uom) {
            return item.poUomTypeId == uom.uomTypeId;
          });
          if (item.bonusUomTypeId && item.bonusUomTypeId == undefined) {
            item.bonusSelectedUom = item.uom.find(function (uom) {
              return item.poUomTypeId == uom.uomTypeId;
            });
          }
          else {
            item.bonusSelectedUom = item.uom.find(function (uom) {
              return item.bonusUomTypeId == uom.uomTypeId;
            });
          }
          if(item.isThreeUOMApplicable == false){
            item.uomListdata = item.uom;
            item.uom = [];
            angular.forEach(item.uomListdata, function(data){
              if(data.uomType != 'Stock'){
                item.uom.push(data);
              }
            });
          }
          $scope.isUpdatePOItemDetails=true;
          self.model.CalculateStock1(item, item.uom, item.selectedUom);
          $scope.uomselectlist = item.selectedUom;
        } else if (self.model.poType.id == 2) {

          item.newMaxOrderQuantity = item.maxOrderQuantity;
          item.newMaxStoreQuantity = item.maxStoreQuantity;
        }
      });
      console.log(self.model.itemDetails, "self.model.itemDetails");
      $scope.calculateRoundoff1();
      angular.forEach(poResponseData, function(val){
        $scope.roundOff = val.roundOffAmount;
        self.model.totalNetAmount1 = val.netAmount;
      });
    }

    $scope.calculateRoundoff1 = function () {
      if($scope.amountround1 != 0){
        $scope.amountround1 = 0;
      }
      if(self.model.prRequestTypeId == 4){
    	  self.model.otherChargesAmount = 0;
      }
      self.model.totalNetAmount = 0;
      self.model.totalNetAmount1 = 0;
      self.model.totalAmount = 0;
      self.model.totalDiscount = 0;
      self.model.totalTax = 0;
      angular.forEach(self.model.itemDetails, function(item){
        if(item.otherChargesAmount == undefined)
        {
          item.otherChargesAmount = 0;
        }
        if(item.grnAmount == undefined){
          item.grnAmount = 0;
        }
        if(self.model.otherChargesAmount == undefined){
          self.model.otherChargesAmount = 0;
        }
        if(!item.discountAmount){
          item.discountAmount = 0;
        }
        if(item.grnReceivedQuantity == undefined){
          item.grnReceivedQuantity = 0;
        }
        if(item.costOfPurchase == undefined){
          item.costOfPurchase = " ";
        }
        if(self.model.prRequestTypeId == 4){
          if(item.grnReceivedQuantity == undefined){
            item.grnReceivedQuantity = item.newPOQty;
          }
          item.grnAmount = item.grnReceivedQuantity * item.costOfPurchase;
          self.model.otherChargesAmount += item.otherChargesAmount;
        }
        else{
          if(item.newPOQty){
            if(item.grnReceivedQuantity == undefined || item.grnReceivedQuantity == null){
              item.grnReceivedQuantity = item.newPOQty;
            }
            if(item.cop == undefined){
              item.grnAmount = item.newPOQty * item.costOfPurchase;
            }
            else{
              item.grnAmount = item.newPOQty * item.cop;
            }
          }
          else{
            item.grnAmount = item.grnReceivedQuantity * item.costOfPurchase;
          }
        }
        if(isNaN(item.otherChargesAmount) || item.otherChargesAmount==undefined || item.otherChargesAmount==null)
        	{
        	item.otherChargesAmount=0;
        	}
        item.grnAmount  = (item.grnAmount==null||item.grnAmount==undefined||isNaN(item.grnAmount)) ? 0 : item.grnAmount;
        item.discountAmount  = (item.discountAmount==null||item.discountAmount==undefined||isNaN(item.discountAmount)) ? 0 : item.discountAmount;
        item.discountAmount = parseFloat(item.discountAmount);
        item.taxAmount = ((item.grnAmount - item.discountAmount) * (item.selectedTaxPercent/100));
        item.lcopAmount = (((item.grnAmount - item.discountAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.receivedQtyBonusCalculation)));
        item.netAmount = ((item.grnAmount - item.discountAmount) + parseFloat(item.taxAmount) +(!isNaN(item.otherChargesAmount) && item.otherChargesAmount!=undefined && item.otherChargesAmount!=null ? parseFloat(item.otherChargesAmount):0));
        self.model.totalAmount += item.grnAmount;
        self.model.totalDiscount += item.discountAmount;
        self.model.totalTax += item.taxAmount;
        self.model.totalNetAmount = self.model.totalNetAmount + item.netAmount;
        $scope.amountround1 = $scope.amountround1 + (item.grnAmount - parseFloat(item.discountAmount) + item.taxAmount + ((!isNaN(item.otherChargesAmount)) ? item.otherChargesAmount :0));
        $scope.amountround = $scope.amountround1.toFixed();
        $scope.amount1 = parseFloat($scope.amountround1).toFixed(2);
        $scope.roundOff1 = parseFloat($scope.amountround) - $scope.amount1;
        if($scope.roundOff1 < 0.50){
          self.model.totalNetAmount1 = $scope.amountround;
			          if($scope.editFlag==true)
			  		{
			        	  $scope.roundOff=$scope.roundOff;
			  		}
			          else
			        	  {
			        	  $scope.roundOff = parseFloat($scope.amountround) - $scope.amount1;
			              $scope.roundOff = $scope.roundOff.toFixed(2);
			        	  }
        }
        if($scope.roundOff1 >= 0.50){
          self.model.totalNetAmount1 = math.round($scope.amount1);
          if($scope.editFlag==true)
	  		{
	        	  $scope.roundOff=$scope.roundOff;
	  		}
	          else
	        	  {
          $scope.roundOff = self.model.totalNetAmount1 - $scope.amount1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
	        	  }
        }
      });
      if(self.model.totalCreditNoteAmount){
        $scope.amountround1 = $scope.amountround1 - self.model.totalCreditNoteAmount;
        $scope.amountround = $scope.amountround1.toFixed();
        $scope.amount1 = parseFloat($scope.amountround1).toFixed(2);
        $scope.roundOff1 = parseFloat($scope.amountround) - $scope.amount1;
        if($scope.roundOff1 < 0.50){
          self.model.totalNetAmount1 = $scope.amountround;
          if($scope.editFlag==true)
	  		{
	        	  $scope.roundOff=$scope.roundOff;
	  		}
	          else
	        	  {
          $scope.roundOff = parseFloat($scope.amountround) - $scope.amount1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
	        	  }
        }
        if($scope.roundOff1 >= 0.50){
          self.model.totalNetAmount1 = math.round($scope.amount1);
          if($scope.editFlag==true)
	  		{
	        	  $scope.roundOff=$scope.roundOff;
	  		}
	          else
	        	  {
          $scope.roundOff = self.model.totalNetAmount1 - $scope.amount1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
	        	  }
        }
      }
      $rootScope.stopLoader();
    }

    var CalculateStock1 = function (item, uom, uomList) {
      if (!!uomList) {
        var poQuantity = item.poLeaseQuantity;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;

        var uom = uom.filter(function (uom) {
          if (!!uom.uomTypeId) {
            return uom;
          }
        });

        if (!!uomList.uomDetails) {
          uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        }
        if($scope.isUpdatePOItemDetails!=true)
        	{
        if(item.isThreeUOMApplicable == true){
        if(item.poUomTypeId==1)
        	{
        	poQuantity = parseInt(poQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
        	}
        else if(item.poUomTypeId==2)
        	{
        	poQuantity =parseInt(poQuantity * (uom[1].conversion * uom[2].conversion));
        	}
        else
        	{
        	poQuantity =parseInt(poQuantity * (uom[2].conversion));
        	}
        	}
        else
        	{
        	 if(item.poUomTypeId==1)
         	{
         	poQuantity = parseInt(poQuantity * (uom[0].conversion *  uom[1].conversion));
         	}
         else
         	{
         	poQuantity =parseInt(poQuantity * (uom[1].conversion));
         	}
        	}
      }
        if(item.poGrnQuantity!=undefined && item.poGrnQuantity!=null)
        	{
        if(item.isThreeUOMApplicable == true){
            if(item.poUomTypeId==1)
            	{
            	item.poGrnQuantityLease = parseInt(item.poGrnQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            	}
            else if(item.poUomTypeId==2)
            	{
            	item.poGrnQuantityLease =parseInt(item.poGrnQuantity * (uom[1].conversion * uom[2].conversion));
            	}
            else
            	{
            	item.poGrnQuantityLease =parseInt(item.poGrnQuantity * (uom[2].conversion));
            	}
            	}
            else
            	{
            	 if(item.poUomTypeId==1)
             	{
             	item.poGrnQuantityLease = parseInt(item.poGrnQuantity * (uom[0].conversion *  uom[1].conversion));
             	}
             else
             	{
             	item.poGrnQuantityLease =parseInt(item.poGrnQuantity * (uom[1].conversion));
             	}
            	}
      }
        var poGrnLeaseQty=(!isNaN(item.poGrnQuantityLease) && item.poGrnQuantityLease!=undefined && item.poGrnQuantityLease!=null ? item.poGrnQuantityLease:0);
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newPOQty = parseInt(poQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.poQuantity = parseInt(poQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.grnReceivedQuantity = parseInt((poQuantity-poGrnLeaseQty) / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.uomListdata[0].conversion);
            item.receivedQtyCalculation = (item.grnReceivedQuantity * item.uomListdata[0].conversion);
            item.uomUnitData = item.uomListdata[2].uomUnit;
            item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.uomListdata[0].conversion);
            item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.uomListdata[0].conversion);
            item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newPOQty = parseInt(poQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.poQuantity = parseInt(poQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.grnReceivedQuantity = parseInt((poQuantity-poGrnLeaseQty) / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newPOQty = parseInt(poQuantity / (item.uomListdata[2].conversion));
            item.poQuantity = parseInt(poQuantity / (item.uomListdata[2].conversion));
            item.grnReceivedQuantity = parseInt((poQuantity-poGrnLeaseQty) / (item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity);
            item.receivedQtyCalculation = (item.grnReceivedQuantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
            item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
            item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
            item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newPOQty = parseInt(poQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.poQuantity = parseInt(poQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.grnReceivedQuantity = parseInt((poQuantity-poGrnLeaseQty) / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ uom[0].conversion +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.grnReceivedQuantity * uom[0].conversion * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
            item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ uom[0].conversion +"*"+ uom[1].conversion);
            item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * uom[0].conversion * uom[1].conversion);
            item.uomUnitDataBonus = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newPOQty = parseInt(poQuantity / (uom[1].conversion * uom[2].conversion));
            item.poQuantity = parseInt(poQuantity / (uom[1].conversion * uom[2].conversion));
            item.grnReceivedQuantity = parseInt((poQuantity-poGrnLeaseQty) / (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.grnReceivedQuantity * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
            item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ uom[1].conversion);
            item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * uom[1].conversion);
            item.uomUnitDataBonus = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newPOQty = parseInt(poQuantity / (uom[2].conversion));
            item.poQuantity = parseInt(poQuantity / (uom[2].conversion));
            item.grnReceivedQuantity = parseInt((poQuantity-poGrnLeaseQty) / (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity);
            item.receivedQtyCalculation = (item.grnReceivedQuantity);
            item.uomUnitData = uom[2].uomUnit;
            item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
            item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
            item.uomUnitDataBonus = uom[2].uomUnit;
          }
        }
      } else {
        item.newPOQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    var CalculateStock2 = function (item, uom, uomList) {
        if (!!uomList) {
          var poQuantity = item.poLeaseQuantity;
          var maxOrderQuantity = item.maxOrderQuantity || 0;
          var maxStoreQuantity = item.maxStoreQuantity || 0;
          var UOMCheck;
          var uom = uom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });

          if(item.grnReceivedQuantity == undefined || item.grnReceivedQuantity == null || item.grnReceivedQuantity == ""){
            item.grnReceivedQuantity = 0;
          }
  
          if(poQuantity == undefined || poQuantity == null){
            poQuantity = item.grnReceivedQuantity;
          }
          
          if (!!uomList.uomDetails) {
            uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
          }

          if(item.bonusReceivedQuantity == undefined || item.bonusReceivedQuantity == null || item.bonusReceivedQuantity == ''){
            item.bonusReceivedQuantity = 0;
          }

          var grnQty = parseInt(item.grnReceivedQuantity);
          item.actualGrnQty=grnQty;
          item.grnQtyData = grnQty;

          if(item.isThreeUOMApplicable == false){
            if(item.uomListdata != undefined && item.uomListdata != null){
              if (uomList.uomDetailsData == "P") {
                item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.poUom[0].conversion);
                item.receivedQtyCalculation = (item.grnReceivedQuantity * item.poUom[0].conversion);
                item.uomUnitData = item.uomListdata[2].uomUnit;
                item.grnQtyData = item.grnQtyData*(item.uomListdata[0].conversion * item.uomListdata[1].conversion* item.uomListdata[2].conversion);
//                item.poQuantity=item.grnQtyData/(item.uomListdata[0].conversion * item.uomListdata[1].conversion* item.uomListdata[2].conversion);
              } else if (uomList.uomDetailsData == "S") {
                item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                item.grnQtyData = item.grnQtyData*(item.uomListdata[1].conversion * item.uomListdata[2].conversion);
//                item.poQuantity=item.grnQtyData/(item.uomListdata[1].conversion* item.uomListdata[2].conversion);
              } else if (uomList.uomDetailsData == "D") {
                item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
                item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
                item.receivedQtyCon=(item.grnReceivedQuantity);
                item.receivedQtyCalculation = (item.grnReceivedQuantity);
                item.uomUnitData = item.uomListdata[2].uomUnit;
                item.grnQtyData = item.grnQtyData * (item.uomListdata[2].conversion);
//                item.poQuantity=item.grnQtyData/(item.uomListdata[2].conversion);
              }
            }
            else{
              if (uomList.uomDetailsData == "P") {
                item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uom[0].conversion * item.uom[1].conversion));
                item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uom[0].conversion * item.uom[1].conversion));
                item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.poUom[0].conversion);
                item.receivedQtyCalculation = (item.grnReceivedQuantity * item.poUom[0].conversion);
                item.uomUnitData = item.uom[1].uomUnit;
                item.grnQtyData = item.grnQtyData*( item.uom[0].conversion * item.uom[1].conversion);
//                item.poQuantity=item.grnQtyData/( item.uom[0].conversion * item.uom[1].conversion);
              } else if (uomList.uomDetailsData == "S") {
                item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uom[1].conversion * item.uom[2].conversion));
                item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uom[1].conversion * item.uom[2].conversion));
                item.grnQtyData = item.grnQtyData*(item.uom[1].conversion * item.uom[2].conversion);
//                item.poQuantity=item.grnQtyData/( item.uom[1].conversion * item.uom[2].conversion);
              } else if (uomList.uomDetailsData == "D") {
                item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uom[1].conversion));
                item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uom[1].conversion));
                item.receivedQtyCon=(item.grnReceivedQuantity);
                item.receivedQtyCalculation = (item.grnReceivedQuantity);
                item.uomUnitData = item.uom[1].uomUnit;
                item.grnQtyData = item.grnQtyData * (item.uom[1].conversion);
//                item.poQuantity=item.grnQtyData/( item.uom[1].conversion);
              }
            }
          }
          else{
            if (uomList.uomDetailsData == "P") {
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.poUom[0].conversion +"*"+ item.poUom[1].conversion);
              item.receivedQtyCalculation = (item.grnReceivedQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
              item.grnQtyData = item.grnQtyData * (uom[0].conversion * uom[1].conversion * uom[2].conversion);
//              item.poQuantity=item.grnQtyData / (uom[0].conversion * uom[1].conversion * uom[2].conversion);
            } else if (uomList.uomDetailsData == "S") {
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.poUom[1].conversion);
              item.receivedQtyCalculation = (item.grnReceivedQuantity * item.poUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
              item.grnQtyData = item.grnQtyData*(uom[1].conversion * uom[2].conversion);
//              item.poQuantity=item.grnQtyData / (uom[1].conversion * uom[2].conversion);
            } else if (uomList.uomDetailsData == "D") {
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
              item.receivedQtyCon=(item.grnReceivedQuantity);
              item.receivedQtyCalculation = (item.grnReceivedQuantity);
              item.uomUnitData = uom[2].uomUnit;
              item.grnQtyData = item.grnQtyData * (uom[2].conversion);
//              item.poQuantity=item.grnQtyData / ( uom[2].conversion);
            }
          }
          if(item.bonusSelectedUom != undefined && item.bonusSelectedUom != null){
        	  if(item.bonusUom == undefined || item.bonusUom == null || item.bonusUom.length == 0){
            	  item.bonusUom = item.uom;
              }
            if(item.isThreeUOMApplicable == false){
              if(item.uomListdata != undefined && item.uomListdata != null){
                if (item.bonusSelectedUom.uomTypeId == 1) {
                  item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion);
                  item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
                } else if (item.bonusSelectedUom.uomTypeId == 2) {
                 
                } else if (item.bonusSelectedUom.uomTypeId == 3) {
                  item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                  item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
                }
              }
              else{
                if (item.bonusSelectedUom.uomTypeId == 1) {
                  item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion);
                  item.uomUnitDataBonus = item.uom[1].uomUnit;
                } else if (item.bonusSelectedUom.uomTypeId == 2) {
                 
                } else if (item.bonusSelectedUom.uomTypeId == 3) {
                  item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                  item.uomUnitDataBonus = item.uom[1].uomUnit;
                }
              }
            }
            else{
              if (item.bonusSelectedUom.uomTypeId == 1) {
                item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion +"*"+ item.bonusUom[1].conversion);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
              } else if (item.bonusSelectedUom.uomTypeId == 2) {
                item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[1].conversion);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
              } else if (item.bonusSelectedUom.uomTypeId == 3) {
                item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
            } 
          }
        } else {
          item.newPOQty = 0;
          item.newMaxOrderQuantity = 0;
          item.newMaxStoreQuantity = 0;
        }
      }

    $scope.uomChangeDis = function(item){
      if(self.model.discountType == 2){
        item.discountAmount = 0;
        item.discountPercent = 0;
        self.model.finalDiscount = 0;
      }
    }

    var CalculateStock = function (item, uom, uomList) {
      // if(item.bonusSelectedUom != undefined && item.bonusSelectedUom != null){
      //   CalculateStockForBonus(item, item.bonusUom, uomList);
      // }
      item.grnReceivedQuantity = 0;
      item.grnQtyData = 0;
      if (!!uomList) {
    	  var poQuantity =0;
    	  if(self.model.prRequestTypeId != 1)
    	  {
    		  if($scope.editQtyFlag==true)
    		  {
    			  poQuantity = item.poFinalLeaseQty!=undefined && item.poFinalLeaseQty!=null ? item.poFinalLeaseQty: item.FinalPoQuantityForGrn;
    		  }
    		  else
    		  {
    			  $scope.leaseQtyConvertedFlag=true;
    			  poQuantity =item.poLeaseQuantity!=undefined && item.poLeaseQuantity!=null ? item.poLeaseQuantity: item.FinalPoQuantityForGrn;
    		  }
    	  }    	  
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        var UOMCheck;
        var uom = uom.filter(function (uom) {
          if (!!uom.uomTypeId) {
            return uom;
          }
        });

        if(item.grnReceivedQuantity == undefined || item.grnReceivedQuantity == null){
          item.grnReceivedQuantity = 0;
        }

        if(poQuantity == undefined || poQuantity == null){
          poQuantity = item.grnReceivedQuantity;
        }

        if(item.grnQtyData == undefined || item.grnQtyData == null || item.grnQtyData == ""){
          item.grnQtyData = poQuantity;
        }
        else
        {
        	poQuantity=item.grnQtyData;
        }

        if (!!uomList.uomDetails) {
        	UOMCheck = uomList.uomDetails.split(' ')[0];
        }
        if($scope.editQtyFlag==false)
        	{
        	if($scope.leaseQtyConvertedFlag!=true)
        		{
        if(item.grnUomTypeId!=undefined && item.grnUomTypeId!=null)
        	{
        if(item.isThreeUOMApplicable == false){
            if (item.grnUomTypeId==1) {
            	item.grnQtyData = parseInt(item.grnQtyData * (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
            	
            }
            else if(item.grnUomTypeId==2)
            	{
            	item.grnQtyData = parseInt(item.grnQtyData * (item.uomListdata[1].conversion));
            	}
        	}
        else
    	{
    	 if (item.grnUomTypeId==1) {
    		 item.grnQtyData = parseInt(item.grnQtyData * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
         	
         }
         else if(item.grnUomTypeId==2)
         	{
        	 item.grnQtyData = parseInt(item.grnQtyData * (uom[1].conversion * uom[2].conversion));
         	}
         else if(item.grnUomTypeId==3)
      	{
        	 item.grnQtyData = parseInt(item.grnQtyData * (uom[2].conversion));
      	}
    	}
        	}
        	}
      }
        if(item.isThreeUOMApplicable == false){
        	if(item.uomListdata != undefined && item.uomListdata != null){
            if (UOMCheck == "P") {
              item.newPOQty = parseInt(poQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
              item.poQuantity = parseInt(poQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
              item.grnReceivedQuantity = parseInt(item.grnQtyData / (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
              item.pendingQuantity = parseInt(item.pendingQty / (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion));
              item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.uomListdata[0].conversion);
              item.receivedQtyCalculation = (item.grnReceivedQuantity * item.uomListdata[0].conversion);
              item.uomUnitData = item.uomListdata[1].uomUnit;
           } 
//            else if (UOMCheck == "S") {
//              item.newPOQty = parseInt(poQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//              item.poQuantity = parseInt(poQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//              item.grnReceivedQuantity = parseInt(item.grnQtyData / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//              if(!$scope.uomselectlist){
//                item.pendingQuantity = parseInt(item.pendingQty / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//              }
//              else{
//                item.pendingQuantity = parseInt(item.pendingQty / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//              }
//              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
//            } 
            else if (UOMCheck == "D") {
              item.newPOQty = parseInt(poQuantity / (item.uomListdata[1].conversion));
              item.poQuantity = parseInt(poQuantity / (item.uomListdata[1].conversion));
              item.grnReceivedQuantity = parseInt(item.grnQtyData / (item.uomListdata[1].conversion));
              item.pendingQuantity = parseInt(item.pendingQty / (item.uomListdata[1].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion));
              item.receivedQtyCon=(item.grnReceivedQuantity);
              item.receivedQtyCalculation = (item.grnReceivedQuantity);
              item.uomUnitData = item.uomListdata[1].uomUnit;
            }
          }
          else{
            if (UOMCheck == "P") {
              item.newPOQty = parseInt(poQuantity / (item.uom[0].conversion * item.uom[1].conversion));
              item.poQuantity = parseInt(poQuantity / (item.uom[0].conversion * item.uom[1].conversion));
              item.grnReceivedQuantity = parseInt(item.grnQtyData / (item.uom[0].conversion * item.uom[1].conversion));
              item.pendingQuantity = parseInt(item.pendingQty / (item.uom[0].conversion * item.uom[1].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uom[0].conversion * item.uom[1].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uom[0].conversion * item.uom[1].conversion));
              item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.uom[0].conversion);
              item.receivedQtyCalculation = (item.grnReceivedQuantity * item.uom[0].conversion);
              item.uomUnitData = item.uom[1].uomUnit;
            }
//            } else if (UOMCheck == "S") {
//              item.newPOQty = parseInt(poQuantity / (item.uom[1].conversion * item.uom[2].conversion));
//              item.poQuantity = parseInt(poQuantity / (item.uom[1].conversion * item.uom[2].conversion));
//              item.grnReceivedQuantity = parseInt(item.grnQtyData / (item.uom[1].conversion * item.uom[2].conversion));
//              if(!$scope.uomselectlist){
//                item.pendingQuantity = parseInt(item.pendingQty / (item.uom[1].conversion * item.uom[2].conversion));
//              }
//              else{
//                item.pendingQuantity = parseInt(item.pendingQty / (item.uom[1].conversion * item.uom[2].conversion));
//              }
//              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uom[1].conversion * item.uom[2].conversion));
//              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uom[1].conversion * item.uom[2].conversion));
//            }
        	else if (UOMCheck == "D") {
              item.newPOQty = parseInt(poQuantity / (item.uom[1].conversion));
              item.poQuantity = parseInt(poQuantity / (item.uom[1].conversion));
              item.grnReceivedQuantity = parseInt(item.grnQtyData / (item.uom[1].conversion));
              item.pendingQuantity = parseInt(item.pendingQty / (item.uom[1].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uom[1].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uom[1].conversion));
              item.receivedQtyCon=(item.grnReceivedQuantity);
              item.receivedQtyCalculation = (item.grnReceivedQuantity);
              item.uomUnitData = item.uom[1].uomUnit;
            }
          }
        }
        else{
          if (UOMCheck == "P") {
            item.newPOQty = parseInt(item.grnQtyData / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.poQuantity = parseInt(item.grnQtyData / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.grnReceivedQuantity = parseInt(item.grnQtyData / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.pendingQuantity = parseInt(item.pendingQty / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ uom[0].conversion +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.grnReceivedQuantity * uom[0].conversion * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
          } else if (UOMCheck == "S") {
            item.newPOQty = parseInt(item.grnQtyData / (uom[1].conversion * uom[2].conversion));
            item.poQuantity = parseInt(item.grnQtyData / (uom[1].conversion * uom[2].conversion));
            item.grnReceivedQuantity = parseInt(item.grnQtyData / (uom[1].conversion * uom[2].conversion));
            if(!$scope.uomselectlist){
              item.pendingQuantity = parseInt(item.pendingQty / (uom[1].conversion * uom[2].conversion));
            }
            else{
              item.pendingQuantity = parseInt(item.pendingQty / (uom[1].conversion * uom[2].conversion));
            }
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.grnReceivedQuantity * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
          } else if (UOMCheck == "D") {
            item.newPOQty = parseInt(item.grnQtyData / (uom[2].conversion));
            item.poQuantity = parseInt(item.grnQtyData / (uom[2].conversion));
            item.grnReceivedQuantity = parseInt(item.grnQtyData / (uom[2].conversion));
            item.pendingQuantity = parseInt(item.pendingQty / (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity);
            item.receivedQtyCalculation = (item.grnReceivedQuantity);
            item.uomUnitData = uom[2].uomUnit;
          }
        }
      } else {
        item.newPOQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
      item.grnUomTypeId=uomList.uomTypeId;
      $scope.calculateRoundoff1();
    }

    var CalculateBonusQuantity = function (item) {
      if(item.bonusReceivedQuantity == undefined || item.bonusReceivedQuantity == null){
        item.bonusReceivedQuantity = 0;
      }
      if(item.bonusUom == undefined || item.bonusUom == null || item.bonusUom.length == 0){
    	  item.bonusUom = item.uom;
      }
      item.newBonusReceivedQuantity=item.bonusReceivedQuantity;
      if(self.model.poType.id != 2){
        if(item.bonusUom.length != 0){
          if(item.isThreeUOMApplicable == false){
            if(item.bonusSelectedUom != undefined && item.bonusSelectedUom != null){
              if(item.bonusUom[2] == undefined && item.bonusUom[2] == null){
                if(item.bonusSelectedUom.uomType=="Dispense")
                {
                  item.newBonusReceivedQuantity=item.newBonusReceivedQuantity*(item.uomListdata[2].conversion);
                  item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                  item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
                }
                else if(item.bonusSelectedUom.uomType=="Stock")
                {
                  item.newBonusReceivedQuantity=item.newBonusReceivedQuantity*(item.uomListdata[2].conversion*(item.uomListdata[1].conversion));
                }
                else if(item.bonusSelectedUom.uomType=="Purchase")
                {
                  item.newBonusReceivedQuantity=item.newBonusReceivedQuantity*(item.uomListdata[2].conversion* item.uomListdata[1].conversion*(item.uomListdata[0].conversion));
                  item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.uomListdata[0].conversion);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.uomListdata[0].conversion);
                  item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
                }
              }
              else{
                if(item.bonusSelectedUom.uomType=="Dispense")
                {
                  item.newBonusReceivedQuantity= item.newBonusReceivedQuantity*(item.bonusUom[2].conversion);
                  item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                  if(item.bonusUom[2].uomUnitId.uomUnit == undefined || item.bonusUom[2].uomUnitId.uomUnit == null){
                  	item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
                  }
                  else{
                  	item.uomUnitDataBonus = item.bonusUom[2].uomUnitId.uomUnit;
                  }
                }
                else if(item.bonusSelectedUom.uomType=="Stock")
                {
                  item.newBonusReceivedQuantity= item.newBonusReceivedQuantity*(item.bonusUom[2].conversion*(item.bonusUom[1].conversion));
                }
                else if(item.bonusSelectedUom.uomType=="Purchase")
                {
                  item.newBonusReceivedQuantity= item.newBonusReceivedQuantity*(item.bonusUom[2].conversion* item.bonusUom[1].conversion*(item.bonusUom[0].conversion));
                  item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion);
                  item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion);
                  if(item.bonusUom[2].uomUnitId.uomUnit == undefined || item.bonusUom[2].uomUnitId.uomUnit == null){
                  	item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
                  }
                  else{
                  	item.uomUnitDataBonus = item.bonusUom[2].uomUnitId.uomUnit;
                  }
                }
              }
            }
          }
          else{
            if(item.bonusSelectedUom != undefined && item.bonusSelectedUom != null){
              if(item.bonusSelectedUom.uomType=="Dispense")
              {
                item.newBonusReceivedQuantity= item.newBonusReceivedQuantity*(item.bonusUom[2].conversion);
                item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                if(item.bonusUom[2].uomUnitId.uomUnit == undefined || item.bonusUom[2].uomUnitId.uomUnit == null){
                	item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
                }
                else{
                	item.uomUnitDataBonus = item.bonusUom[2].uomUnitId.uomUnit;
                }
              }
              else if(item.bonusSelectedUom.uomType=="Stock")
              {
                item.newBonusReceivedQuantity= item.newBonusReceivedQuantity*(item.bonusUom[2].conversion*(item.bonusUom[1].conversion));
                item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[1].conversion);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[1].conversion);
                if(item.bonusUom[2].uomUnitId.uomUnit == undefined || item.bonusUom[2].uomUnitId.uomUnit == null){
                	item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
                }
                else{
                	item.uomUnitDataBonus = item.bonusUom[2].uomUnitId.uomUnit;
                }
              }
              else if(item.bonusSelectedUom.uomType=="Purchase")
              {
                item.newBonusReceivedQuantity= item.newBonusReceivedQuantity*(item.bonusUom[2].conversion* item.bonusUom[1].conversion*(item.bonusUom[0].conversion));
                item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion +"*"+ item.bonusUom[1].conversion);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
                if(item.bonusUom[2].uomUnitId.uomUnit == undefined || item.bonusUom[2].uomUnitId.uomUnit == null){
                	item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
                }
                else{
                	item.uomUnitDataBonus = item.bonusUom[2].uomUnitId.uomUnit;
                }
              }
            }
          }
        }
        item.lcopAmount = (((item.grnAmount - item.discountAmount) + item.taxAmount) / ((item.receivedQtyCalculation!=undefined && item.receivedQtyCalculation!=null ? parseInt(item.receivedQtyCalculation) :0) + (item.receivedQtyBonusCalculation!=undefined && item.receivedQtyBonusCalculation!=null ? parseInt(item.receivedQtyBonusCalculation) : 0)));
        // CalculateStockForBonus(item, item.bonusUom, item.bonusSelectedUom);
      }
      else{
        item.lcopAmount = (((item.grnAmount - item.discountAmount) + item.taxAmount) / (parseInt(item.grnReceivedQuantity) + (item.bonusReceivedQuantity!=undefined && item.bonusReceivedQuantity!=null ? parseInt(item.bonusReceivedQuantity) :0)));
      }
    }

    var CalculateStockForBonus = function (item, uom, uomList) {
        item.bonusReceivedQuantity = 0;
        item.newBonusReceivedQuantity = 0;
        if (!!uomList) {
          var poQuantity = item.poLeaseQuantity;
          var maxOrderQuantity = item.maxOrderQuantity || 0;
          var maxStoreQuantity = item.maxStoreQuantity || 0;
          var UOMCheck;
          var uom = uom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });
          if (!!uomList.uomDetails) {
          	UOMCheck = item.bonusSelectedUom.uomDetails.split(' ')[0];
          }
          if(item.bonusReceivedQuantity==undefined || item.bonusReceivedQuantity==null)
          {
        	  item.bonusReceivedQuantity=0;
          }

          if(item.newBonusReceivedQuantity == undefined || item.newBonusReceivedQuantity == null){
            item.newBonusReceivedQuantity = item.bonusReceivedQuantity;
          }

          if(item.isThreeUOMApplicable == false){
        	  if(uom[2] == undefined && uom[2] == null){
              if (UOMCheck == "P") {
                item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ item.uomListdata[0].conversion);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * item.uomListdata[0].conversion);
                item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
              } else if (UOMCheck == "S") {
                item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              } else if (UOMCheck == "D") {
                item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (item.uomListdata[2].conversion));
                item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
              }
            }
            else{
              if (UOMCheck == "P") {
                item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ uom[0].conversion);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * uom[0].conversion);
                item.uomUnitDataBonus = uom[2].uomUnitId.uomUnit;
              } else if (UOMCheck == "S") {
                item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (uom[1].conversion * uom[2].conversion));
              } else if (UOMCheck == "D") {
                item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity:0) / (uom[2].conversion));
                item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
                item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
                item.uomUnitDataBonus = uom[2].uomUnitId.uomUnit;
              }
            }
          }
          else{
            if (UOMCheck == "P") {
              item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ uom[0].conversion +"*"+ uom[1].conversion);
              item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * uom[0].conversion * uom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnitId.uomUnit;
            } else if (UOMCheck == "S") {
              item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (uom[1].conversion * uom[2].conversion));
              item.receivedQtyBonusCon = (item.bonusReceivedQuantity +"*"+ uom[1].conversion);
              item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity * uom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnitId.uomUnit;
            } else if (UOMCheck == "D") {
              item.bonusReceivedQuantity = parseInt((item.newBonusReceivedQuantity!=undefined ? item.newBonusReceivedQuantity : 0) / (uom[2].conversion));
              item.receivedQtyBonusCon=(item.bonusReceivedQuantity);
              item.receivedQtyBonusCalculation = (item.bonusReceivedQuantity);
              item.uomUnitDataBonus = uom[2].uomUnitId.uomUnit;
            }
          }
          item.lcopAmount = (((item.grnAmount - item.discountAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.receivedQtyBonusCalculation)));
        } 
      }
    

    var getSelectedIds = function () {
      var selecteItemIds = self.model.indentItemDetails.map(function (item) {
        return item.id;
      });
      return _.uniq(selecteItemIds);
    }

     var SelectedItems = function () {
      self.model.poDetails = [];
      
      if(self.model.isSelectedIndent == undefined){
        angular.forEach(self.model.poItemList, function (po) {
          var poData = null;
          if (po.isChecked) {
        	  self.model.fromStoreName = po.storeName;
            self.model.fromStoreId = po.storeId;
            self.model.toStoreName = po.fromStoreName;
            self.model.toStoreId = po.fromStoreId;
            poData = {
              "poId": po.id,
              "itemIds": []
            }
            
            if (po.isChecked && poData.itemIds.length > 0)
              self.model.poDetails.push(poData);
          }

          $scope.poData = {
            "poId": po.id,
            "itemIds": []
          }

          if(po.isSelectedIndent){
            angular.forEach(po.items, function (item) {
              if (item.isChecked) {
                self.model.fromStoreName = po.storeName;
                self.model.fromStoreId = po.storeId;
                self.model.toStoreName = po.fromStoreName;
                self.model.toStoreId = po.fromStoreId;
                $scope.poData.itemIds.push(item.itemId);
              }
            });
            if ((po.isChecked || po.isSelectedIndent) && $scope.poData.itemIds.length > 0)
              self.model.poDetails.push($scope.poData);
          } 
        });
      }
      else if(!self.model.isSelectedIndent.id){
        angular.forEach(self.model.indentItemDetails, function (po) {
          var poData = null;
          if (po.isChecked) {
             self.model.fromStoreName = po.storeName;
              self.model.fromStoreId = po.storeId;
               self.model.toStoreName = po.fromStoreName;
              self.model.toStoreId = po.fromStoreId;
            poData = {
              "poId":self.model.isSelectedIndent.id,
              "itemIds": []
            }
          }

          angular.forEach(po.items, function (item) {
            if (item.isChecked) {
              poData.itemIds.push(item.itemId);
            }
          });

          if (po.isChecked && poData.itemIds.length > 0)
            self.model.poDetails.push(poData);
        });
      }
      else{
         if(self.model.indentItemDetails.length !=0 ){
               var po = self.model.indentItemDetails;
        var po1 = self.model.isSelectedIndent
        var poData = null;
         self.model.fromStoreName = po1.storeName;
              self.model.fromStoreId = po1.storeId;
               self.model.toStoreName = po1.fromStoreName;
              self.model.toStoreId = po1.fromStoreId;
        poData = {
          "poId": self.model.isSelectedIndent.id,
          "itemIds": []
        }
        angular.forEach(po, function(data){
          if (data.isChecked) {
            poData.itemIds.push(data.itemId);
          }
        });
        if (poData.itemIds.length > 0)
          self.model.poDetails.push(poData);
          }
          else{
              var po = self.model.indentItemDetails;
        var po1 = self.model.isSelectedIndent
        var poData = null;
         self.model.fromStoreName = po1.storeName;
              self.model.fromStoreId = po1.storeId;
               self.model.toStoreName = po1.fromStoreName;
              self.model.toStoreId = po1.fromStoreId;
        poData = {
          "poId": self.model.isSelectedIndent.id,
          "itemIds": []
        }
//         angular.forEach(po1, function(data){
//           if (po1.isChecked) {
              angular.forEach(po1.items, function(data){
                  if(data.isChecked){
                   
                 poData.itemIds.push(data.itemId);   
                  } 
              });
//           }
//         });
        if (poData.itemIds.length > 0)
          self.model.poDetails.push(poData);
          }

      }
    }


    var AddApprovedPOItems = function () {
      self.model.otherChargesAmount = 0;
      if(self.model.indentItemDetails.length==0){
          growl.error('Please select items to proceed');
          return false;
      }
      SelectedItems();
      if (self.model.isSelectedIndent != undefined && self.model.poDetails[0].itemIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.AGAINST_PO_DETAILS + "/org/"+$scope.organizationId + "/unit/"+$scope.unitId;

        var selectedIds = getSelectedIds();

        var data = self.model.poDetails;
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.data;
            var grnResponse = responseData.purchaseOrderList;
            for(var i=0;i<self.model.stores.length;i++)
            {
              if(self.model.stores[i].id == self.model.fromStoreId)
              {
                self.model.store = self.model.stores[i];
              }
            }
            
            for(var i=0;i<self.model.fromStores.length;i++)
            {
              if(self.model.fromStores[i].id == self.model.toStoreId)
              {
                self.model.fromStore = self.model.fromStores[i];
              }
            }
            angular.forEach(grnResponse, function(itemData){
              $scope.roundOff = itemData.roundOffAmount;
//              self.model.dueDate = new Date(itemData.dueDate);
              self.model.dueDate=new Date(self.model.dueDate.getTime() + ($scope.creditDays)*24*60*60*1000);
              self.model.totalNetAmount1 = itemData.netAmount;
              angular.forEach(itemData.items, function(itemList){
            	  itemList.bonusReceivedQuantity=itemList.bonusQuantity;
            	  angular.forEach(itemList.poStagedList, function(poStaged){
            		  poStaged.orginalApprovedQty=poStaged.poApprovedQuantity;
            	  });
                itemList.isBatchRequired = itemList.batchRequired;
                $scope.qty = 0;
                if(itemList.isBatchRequired == false){
                  itemList.batchNumber = 'NA';
                }
                if(itemList.grnStagedLists){
                  angular.forEach(itemList.grnStagedLists, function(g){
                    $scope.qty = $scope.qty + g.stagedReceivedQuantity;
                  }); 
                }
                itemList.grnReceivedQuantity = itemList.poQuantity - $scope.qty;
                itemList.pendingQty = itemList.pendingQuantity;
                itemList.otherChargesAmount = itemList.totalOtherAmount;
                self.model.otherChargesAmount += itemList.totalOtherAmount;
                if (!!parseFloat(itemData.discount)) {
                  if(itemData.discountTypeId == 1){
                    self.model.finalDiscount = itemData.discount;
                  }
                  if(itemData.discountTypeId == 2){
                    self.model.finalDiscount = itemData.totalDiscount.toFixed(2);
                  }
                }
                self.model.discountType = itemData.discountTypeId;
                if(itemList.selectedTax == undefined){
                  angular.forEach(self.model.taxList, function(taxdata){
                    if(taxdata.taxName == "Tax 0%"){
                      itemList.selectedTax = taxdata;
                      itemList.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                    }
                  });
                }
              })
            });
            self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(responseData.otherChargesList);
            self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
            angular.forEach(grnResponse, function(d){
              self.model.otherChargesList1 = d.otherCharges;
            }); 
            updatePOItemDetails(grnResponse);
            angular.forEach(self.model.itemDetails, function (item) {
            	if(item.grnUomTypeId==undefined || item.grnUomTypeId==null)
            		{
            		item.grnUomTypeId=item.poUomTypeId;
            		}
            	angular.forEach(item.grnStagedLists, function (data) {
                  data.originalStagedReceivedQuantity= data.stagedReceivedQuantity;
                });
              });
            if(self.model.finalDiscount){
              self.model.isDiscountApplied = true;
              self.model.discountTypeFlag = false;
              self.model.isAppliedDiscountValid = false;
              // angular.forEach(grnResponse, function(d){
              //   angular.forEach(d.items, function(s){
              //     s.tamount = s.cop * s.poQuantity;
              //     self.model.totalAmount += s.tamount;
              //     self.model.totalDiscount += s.discountAmount;
              //     self.model.taxAmount += s.taxAmount;
              //   });
              // });
              // self.model.totalDiscount1 = parseInt(self.model.totalDiscount);
              // self.model.totalNetAmount1 = self.model.totalAmount - self.model.totalDiscount1 + self.model.taxAmount;
            }
            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            self.model.items = [];
            self.model.poItemList = [];
            angular.forEach(grnResponse, function(itemData){
              self.model.otherChargesAmount = 0;
              angular.forEach(itemData.items, function(itemList){
                itemList.otherChargesAmount = itemList.totalOtherAmount;
                self.model.otherChargesAmount += itemList.totalOtherAmount;
              });
              var totalOtherAmount=0;
              angular.forEach(itemData.otherCharges, function(otherCharge){
            	  totalOtherAmount=totalOtherAmount+(otherCharge.amount!=undefined && otherCharge.amount!=null ? otherCharge.amount:0);
              });
              self.model.otherChargesAmount=totalOtherAmount;
            });
            calculateOtherChargesAmountForItem();
            angular.forEach(self.model.itemDetails, function(i){
            	if(i.cop==undefined || i.cop==null)
            		{
            		i.cop=(i.costOfPurchase!=undefined && i.costOfPurchase!=null ? parseFloat(i.costOfPurchase) : 0);
            		}
              i.lcopAmount = 0;
              i.grnAmount = i.grnReceivedQuantity * i.cop;
              i.taxAmount = ((i.grnAmount - i.discountAmount) * ((i.taxPercent!=undefined && i.taxPercent!=null ? i.taxPercent :0) /100));
              i.netAmount = i.netAmount + i.otherChargesAmount;
              i.lcopAmount = (((i.grnAmount - i.discountAmount) + i.taxAmount) / ((i.receivedQtyCalculation!=undefined && i.receivedQtyCalculation!=null ? parseInt(i.receivedQtyCalculation) :0) + (i.receivedQtyBonusCalculation!=undefined && i.receivedQtyBonusCalculation!=null ? parseInt(i.receivedQtyBonusCalculation):0)));
            });
            var resArr = [];
            self.model.itemDetails.filter(function(item){
              var i = resArr.findIndex(x => (x.itemId == item.itemId));
              if(i <= -1){
                    resArr.push(item);
              }
              return null;
            });
            self.model.itemDetails=resArr;
            var popup = angular.element('#approvedPO');
            popup.modal('hide');
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        console.log("All Items : ",self.model.items);
        console.log("Pagination Data: ",self.model.pagination.data);
        console.log("Pagination Filtered Data: ",self.model.pagination.filteredData);
        if(self.model.SelectedItemsIds){
          self.model.data = self.model.pagination.data.map(function (item) {
            item.isSelected = true;
            self.model.SelectedItemsIds.push(item.itemId);
            return self.model.SelectedItemsIds;
          });
        }
        else{
          self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
            item.isSelected = true;
            return item.itemId;
          });
        }
      } else {
        self.model.SelectedItemsIds = [];
        self.model.items.forEach(function (item) {
          item.isSelected = false;
        });
      }
    }

    var SelectItem = function (item) {
      var itemIds = self.model.SelectedItemsIds;

      if (item.isSelected) {
        itemIds.push(item.itemId);
      } else {
        var index = itemIds.indexOf(item.itemId);
        if (index > -1) {
          itemIds.splice(index, 1);
        }
      }

      if (self.model.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }
    
    $scope.saveBtnDisabled=false;
    var SaveGRN = function () {
      $scope.saveBtnDisabled=true;
    /*	if(getInvoiceNumber(self.model.invoiceNumber))
	      {*/
	        if (ValidateItems()) {
	        	
	          if($scope.validatecop(self.model.itemDetails)){
	        	  
	            if (!self.model.grnId) {
	            	
	            	if(!getInvoiceNumber(self.model.invoiceNumber)){
	            		 CreateGRN();
	            	} else {
	            		
	            		 $scope.saveBtnDisabled=false;
	            	}
	             
	            } else {
	            		if($scope.oldVendor == self.model.vendorId && $scope.oldInvoiceNumber == self.model.invoiceNumber){
	            			
	            			UpdateGRN();
	            		}
	            		
	            		else{
	            			if(!getInvoiceNumber(self.model.invoiceNumber)){
	            				
	            				UpdateGRN();
	            			}
	            			else {
	    	            		
	   	            		 $scope.saveBtnDisabled=false;
	   	            	}
	            	}
	            }
	          }
	          
	        }
	     // }
    }

    var CreateGRN = function () {
      var grn = self.model;
      var dcDate;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.SAVE_GRN_API;
      self.model.otherchargesData = [];
      if(self.model.otherChargesList1){
        if(self.model.otherChargesList1.length > 0){
          self.model.otherChargesList = self.model.otherChargesList1;
        }
      }
      if(self.model.otherChargesList!=undefined && self.model.otherChargesList!=null && self.model.otherChargesList.length>0)
    	  {
      self.model.otherChargesList.forEach(function(othercharData){
        var obj = {
          otherChargesId : '',
          amount: '',
          isFromGrn:''
        }
        obj.otherChargesId = othercharData.otherChargesId;
        obj.amount = othercharData.amount;
        obj.isFromGrn=true;
        self.model.otherchargesData.push(obj);
      });
    	  }
      self.model.creditNoteAmountData = [];
      if(self.model.addCreditNoteList != undefined){
        self.model.addCreditNoteList.forEach(function(creditData){
        	if(creditData.id==undefined)
            {
          var obj1 = {
            mateinwardid: '',
            rgnumber: '',
            replacenumber: '',
            creditnetamount: '',
            availablenetamount: '',
            consumenetamount: '',
            isSelectedCredit: '',
            isChecked: ''
          }
          obj1.mateinwardid = creditData.mateinwardid;
          obj1.rgnumber = creditData.rgnumber;
          obj1.replacenumber = creditData.replacenumber;
          obj1.creditnetamount = creditData.creditnetamount;
          obj1.availablenetamount = creditData.availablenetamount;
          obj1.consumenetamount = creditData.consumenetamount;
          obj1.isSelectedCredit = creditData.isSelectedCredit;
          obj1.isChecked = creditData.isChecked;
          self.model.creditNoteAmountData.push(obj1);
        }
        });
      }
      var data = {
        "orgId": cookieObject.organizationId,
    	  "unitId":cookieObject.unitId,
    	  "addedById":cookieObject.userId,
    	  "updatedById":cookieObject.userId,
    	  "userId":cookieObject.userId,
        "assetTypeId": grn.poType.id,
        "grnTypeId": grn.requestType,
        "invoiceNumber": grn.invoiceNumber,
        "dueDate": moment(grn.dueDate).format('DD-MM-YYYY'),
        "invoiceDate": moment(grn.invoiceDate).format('YYYY-MM-DD'),
        "inwardNumber": grn.inwordNumber,
        "inwardDate": moment(grn.inwordDate).format('YYYY-MM-DD'),
        "dcNumber": grn.dcNumber,
        "dcDate": grn.dcDate,
        "vendorId": grn.vendorId,
        "totalAmount": parseFloat(grn.totalAmount).toFixed(2),
        "discountTypeId": grn.discountType,
        "discount": parseFloat(grn.finalDiscount),
        "totalDiscount": parseFloat(grn.totalDiscount).toFixed(2),
        "totalTaxAmount": parseFloat(grn.totalTax).toFixed(2),
        "totalOtherAmount": parseFloat(grn.otherChargesAmount).toFixed(2),
        "totalCreditNoteAmount": parseFloat(grn.totalCreditNoteAmount).toFixed(2),
        "netAmount": parseFloat(grn.totalNetAmount1).toFixed(2),
        "roundOff": (!isNaN($scope.roundOff) && $scope.roundOff!=undefined && $scope.roundOff!=null && $scope.roundOff!="") ? $scope.roundOff : 0,
        "isConsignment": grn.isConsignment,
        "remark": grn.GRNRemark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "storeId": grn.store.id,
        "requestingStoreId": (grn.fromStore.id!=undefined || grn.fromStore.id!=null) ? grn.fromStore.id:grn.store.id,
        "poId": grn.poId,
        // "lcop": grn.lcopAmount,
        "items": [],
        "billTypeName":grn.billType.name,
        "creditNoteAmountList": self.model.creditNoteAmountData ? self.model.creditNoteAmountData : [],
        "otherCharges": self.model.otherchargesData ? self.model.otherchargesData : []
      };

      angular.forEach(grn.itemDetails, function (item, key) {
        if (self.model.poType.id == 2) {
          var expDateMonth = item.warrantyDate.getMonth() + 1;
          var expDateYear = item.warrantyDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
          var expDateMonth1 = item.warrantyDateTo.getMonth() + 1;
          var expDateYear1 = item.warrantyDateTo.getFullYear();
          var expDateDay1 = new Date(expDateYear1, expDateMonth1, 0).getDate();
        } else if (self.model.poType.id == 1) {
          var expDateMonth = item.expiryDate !=undefined ? item.expiryDate.getMonth() + 1 : item.expiryDate=null;
          var expDateYear = item.expiryDate !=undefined ? item.expiryDate.getFullYear() : null;
          var expDateDay = item.expiryDate !=undefined ? new Date(expDateYear, expDateMonth, 0).getDate() :null;
          if(item.bonusReceivedQuantity!=undefined && item.bonusReceivedQuantity!=null && item.bonusReceivedQuantity>0 &&(item.grnBonusBatchExpiryData==undefined || item.grnBonusBatchExpiryData==null || item.grnBonusBatchExpiryData.length==0))
        	  {
        	  if(item.grnBonusBatchExpiryData==undefined || item.grnBonusBatchExpiryData==null || item.grnBonusBatchExpiryData.length==0)
        		  {
        		  item.grnBonusBatchExpiryData=[];
        		  }
        	  item.grnBonusBatchExpiryData.push({
	          		grnBonusBatchExpiryId:null,
	          		batchNo: item.batchNumber,
	          		expiryDate: item.expiryDate,
	                bonusQuantity: item.bonusReceivedQuantity!=undefined && item.bonusReceivedQuantity!=null ? item.bonusReceivedQuantity:0
	              });
        	  }
          if(item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null)
          {
          angular.forEach(item.grnBonusBatchExpiryData, function (bonusExpiry) {
           var expDateMonth = bonusExpiry.expiryDate.getMonth() + 1;
           var expDateYear = bonusExpiry.expiryDate.getFullYear();
           var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
           if(expDateMonth<10){
               expDateMonth = "0"+expDateMonth;
           }
           bonusExpiry.expiryDate=(expDateYear + "-" + expDateMonth + "-" + expDateDay);
           
          });
          }
        }

        var bonusUom = [];
        angular.forEach(item.bonusUom, function (uom) {
          bonusUom.push({
            "conversion": uom.conversion,
            "id": uom.id,
            "ipUom": uom.ipUom,
            "opUom": uom.opUom,
            "storeUom": uom.storeUom,
            "uomTypeId": uom.uomTypeId,
            "uomUnitId": uom.uomUnitId.uomUnitId
          });
        });

        var grnUom = [];
        angular.forEach(item.poUom, function (uom) {
          grnUom.push({
            "conversion": uom.conversion,
            "id": uom.id,
            "ipUom": uom.ipUom,
            "opUom": uom.opUom,
            "storeUom": uom.storeUom,
            "uomTypeId": uom.uomTypeId,
            "uomUnitId": uom.uomUnitId.uomUnitId
          });
        });

        var setStagedData = [];
        angular.forEach(item.grnStagedLists, function (stagedQty) {
        	delete stagedQty.orginalApprovedQty;
        	delete stagedQty.originalStagedReceivedQuantity;
          if (!!stagedQty.isEditable) {
            setStagedData.push(stagedQty);
          }
        });
        if(item.bonusReceivedQuantity == 0 || item.bonusReceivedQuantity == undefined){
          item.bonusReceivedQuantity = null;
        }
        var convertedGrnReceivedQuantity=0;
        if(item.poUomTypeId!=undefined && item.poUomTypeId!=null)
        	{
        if(item.poUomTypeId!=item.selectedUom.uomTypeId)
        	{
        	var grnQtyForConversion=0;
        	if(item.isThreeUOMApplicable==true)
    		{
        	if(item.selectedUom.uomTypeId==1)
        	{
        		grnQtyForConversion=item.grnReceivedQuantity*(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
        	}
        	else if(item.selectedUom.uomTypeId==2)
    		{
        		grnQtyForConversion=item.grnReceivedQuantity*(item.uom[1].conversion*item.uom[2].conversion);
    		}
        	else
        		{
        		grnQtyForConversion=item.grnReceivedQuantity
        		}
    		}
    	else
    		{
    		if(item.selectedUom.uomTypeId==1)
    		{
    			grnQtyForConversion=item.grnReceivedQuantity*(item.uom[0].conversion*item.uom[1].conversion)
    		}
    		else
    			{
    			grnQtyForConversion=item.grnReceivedQuantity
    			}
    		}
        	
        	if(item.isThreeUOMApplicable==true)
    		{
        	if(item.poUomTypeId==3)
        	{
        		convertedGrnReceivedQuantity=grnQtyForConversion/(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
        	}
        	else if(item.poUomTypeId==2)
    		{
        		convertedGrnReceivedQuantity=grnQtyForConversion/(item.uom[1].conversion*item.uom[2].conversion);
    		}
        	else
        		{
        		convertedGrnReceivedQuantity=grnQtyForConversion
        		}
    		}
    	else
    		{
    		if(item.poUomTypeId==1)
    		{
    			convertedGrnReceivedQuantity=item.grnReceivedQuantity/(item.uom[0].conversion*item.uom[1].conversion)
    		}
    		}
        	}
        else
    	{
    	convertedGrnReceivedQuantity=item.grnReceivedQuantity;
    	}
         }
        else
        	{
        	convertedGrnReceivedQuantity=item.grnReceivedQuantity;
        	}
        var GRNItem = {
          "itemId": item.itemId,
          "grnReceivedQuantity": item.grnReceivedQuantity,
          "bonusReceivedQuantity": item.bonusReceivedQuantity,
          "manufacturerId": item.manufacturerId,
          "cop": item.costOfPurchase,
          "mrp": item.mrp,
          "discount": item.discountPercent,
          "taxId": item.selectedTax.taxId,
          "taxPercentage": parseFloat(item.selectedTaxPercent),
          "taxAmount": parseFloat(item.taxAmount).toFixed(2),
          "totalAmount": parseFloat(item.grnAmount).toFixed(2),
          "totalDiscount": parseFloat(item.discountAmount).toFixed(2),
          "totalOtherAmount": parseFloat(item.otherChargesAmount).toFixed(2),
          "netAmount": parseFloat(item.netAmount).toFixed(2),
          "statusId": setStatus('New'),
          "grnSpecification": item.itemSpecification,
          "remark": item.itemRemark,
          "grnStagedQuantity": item.poReceivedQuantity,
          "grnStagedLists": setStagedData,
          "poDetailId": item.poDetailId,
          "batchNo": item.batchNumber !=undefined ?  item.batchNumber :null,
          "lcop": item.lcopAmount,
          "pendingQuantity": 0,
          "grnBonusBatchExpiry":item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null ? item.grnBonusBatchExpiryData:[],
          "grnQtyUomTypeWise":convertedGrnReceivedQuantity!=undefined && convertedGrnReceivedQuantity!=null ? convertedGrnReceivedQuantity:0
        }

        if(self.model.prRequestTypeId == 4){
          GRNItem.pendingQuantity = item.pendingQuantity;
        }
        else{
          GRNItem.pendingQuantity = item.newMaxOrderQuantity
        }

        if (self.model.poType.id == 2) {
          GRNItem.warrentyPeriod = item.warrantyPeriod;
          GRNItem.supportTerms = item.supportTerms;
          GRNItem.technicalSpecification = item.technicalSpecs;
          GRNItem.expectedPayment = item.paymentTerms;
          GRNItem.grnUomTypeId = undefined;
          GRNItem.grnUomUnitId = undefined;
          GRNItem.bonusUomUnitId = undefined;
          GRNItem.bonusUomTypeId = undefined;
          if(expDateMonth < 10){
            expDateMonth = "0"+expDateMonth;
          }
          if(expDateMonth1 < 10){
            expDateMonth1 = "0"+expDateMonth1;
          }
          GRNItem.warrantyDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
          GRNItem.warrantyDateTo = (expDateYear1 + "-" + expDateMonth1 + "-" + expDateDay1);
          GRNItem.serviceProviderId = item.providerId;
          // GRNItem.poAccessoriesMapper = item.poAccessoriesMapper;
          GRNItem.expiryDate = undefined;
          GRNItem.grnAccessoriesMapper = item.accessories;
          GRNItem.grnUom = undefined;
          GRNItem.bonusUom = undefined;
          /*
			 * GRNItem.taxId = item.selectedTax.taxId; GRNItem.taxPercentage =
			 * parseFloat(item.selectedTaxPercent);
			 */
        } else if (self.model.poType.id == 1) {
          GRNItem.warrentyPeriod = undefined;
          GRNItem.supportTerms = undefined;
          GRNItem.technicalSpecification = undefined;
          GRNItem.expectedPayment = undefined;
          GRNItem.grnUomTypeId = item.selectedUom.uomTypeId;
          GRNItem.grnUomUnitId = item.selectedUom.uomUnitId;
         // GRNItem.bonusUomUnitId = item.bonusSelectedUom.uomUnitId;
         // GRNItem.bonusUomTypeId = item.bonusSelectedUom.uomTypeId;
          if(item.bonusSelectedUom){
        	  GRNItem.bonusUomUnitId = item.bonusSelectedUom.uomUnitId;
        	  GRNItem.bonusUomTypeId = item.bonusSelectedUom.uomTypeId;
        	  GRNItem.bonusUom = bonusUom;
          }
          else{
        	  GRNItem.bonusUomUnitId = null;
        	  GRNItem.bonusUomTypeId = null;
        	  GRNItem.bonusUom = null;
          }
          GRNItem.warrantyDate = undefined;
          GRNItem.warrantyDateTo = undefined;
          GRNItem.poAccessoriesMapper = item.poAccessoriesMapper;
          if(expDateMonth < 10){
            expDateMonth = "0"+expDateMonth;
          }
          GRNItem.expiryDate = expDateYear !=null ?  (expDateYear + "-" + expDateMonth + "-" + expDateDay) : null;
          GRNItem.serviceProviderId = undefined;
          GRNItem.grnAccessoriesMapper = undefined;
          GRNItem.grnUom = grnUom;
         // GRNItem.bonusUom = bonusUom;
          /*
			 * GRNItem.taxId = item.taxId; GRNItem.taxPercentage =
			 * parseFloat(item.taxPercent);
			 */
        }
        if(self.model.prRequestTypeId == 4){
          angular.forEach(GRNItem.grnStagedLists, function(d){
          //  d.stagedReceivedQuantity = item.poQuantity;
        	  if(!d.stagedReceivedQuantity){
        		  d.stagedReceivedQuantity = item.poQuantity;
            }
          });
        }


        data.items.push(GRNItem);
      });

      $rootScope.startLoader();
      console.log("data", data);
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          rePrintBillSlip(response.data, data.storeId);
          $scope.grnIdForDoccumentUpload=response.data.id;
          $rootScope.stopLoader();
          NavigateToGRN();
          if($scope.encounterDetailsList.length == 1){
        	  angular.forEach($scope.encounterDetailsList,function(value,key){
        		  if (!angular.equals(value.file, {}) && value.file != undefined) {
        		      $scope.uploadPatientImage();
        		  }
        	  });
          }
          if($scope.encounterDetailsList.length > 1){
          $scope.uploadPatientImage();
          }
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          $scope.saveBtnDisabled=false;
          growl.error(err.data.summary);
        });
    }

    var UpdateGRN = function () {
      var grn = self.model;
      
      $scope.grnIdForDoccumentUpload = grn.grnId;
      
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.SAVE_GRN_API + grn.grnId;
      self.model.otherchargesData = [];
      if(self.model.otherChargesList==undefined || self.model.otherChargesList==null || self.model.otherChargesList.length==0)
    	  {
    	  self.model.otherChargesList=$scope.otherchargesmapper;
    	  }
      if( self.model.otherChargesList!=undefined)
      {
        self.model.otherChargesList.forEach(function(othercharData){
        var obj = {
          otherChargesId : '',
          amount: '',
          isFromGrn:''
        }
        obj.otherChargesId = othercharData.otherChargesId;
        obj.amount = othercharData.amount;
        obj.isFromGrn=true;
        self.model.otherchargesData.push(obj);
      });   
      }
      self.model.creditNoteAmountData = [];
      if(self.model.addCreditNoteList != undefined){
        self.model.addCreditNoteList.forEach(function(creditData){
        	if(creditData.id==undefined)
            {
          var obj1 = {
            inwordno: '',
            rgnumber: '',
            replacenumber: '',
            creditnetamount: '',
            availablenetamount: '',
            consumenetamount: '',
            isSelectedCredit: '',
            isChecked: ''
          }
          obj1.inwordno = creditData.inwordno;
          obj1.rgnumber = creditData.rgnumber;
          obj1.replacenumber = creditData.replacenumber;
          obj1.creditnetamount = creditData.creditnetamount;
          obj1.availablenetamount = creditData.availablenetamount;
          obj1.consumenetamount = creditData.consumenetamount;
          obj1.isSelectedCredit = creditData.isSelectedCredit;
          obj1.isChecked = creditData.isChecked;
          self.model.creditNoteAmountData.push(obj1);
        }
        });
      }
      var data = {
        "id": grn.grnId,
        "vendorId": grn.vendorId,
        "totalAmount": grn.totalAmount.toFixed(2),
        "discountTypeId": grn.discountType!=undefined && grn.discountType!=null ? parseInt(grn.discountType) : undefined,
        "discount": parseInt(grn.finalDiscount),
        "totalDiscount": grn.totalDiscount.toFixed(2),
        "totalTaxAmount": grn.totalTax.toFixed(2),
        "totalOtherAmount": parseFloat(grn.otherChargesAmount).toFixed(2),
        "totalCreditNoteAmount": parseFloat(grn.totalCreditNoteAmount).toFixed(2),
        "netAmount": parseFloat(grn.totalNetAmount1).toFixed(2),
        "roundOff": (!isNaN($scope.roundOff) && $scope.roundOff!=undefined && $scope.roundOff!=null && $scope.roundOff!="") ? $scope.roundOff : 0,
        "isConsignment": grn.isConsignment,
        "remark": grn.GRNRemark,
        "invoiceNumber": grn.invoiceNumber,
        "inwardNumber": grn.inwordNumber,
        "inwardDate": moment(grn.inwordDate).format('YYYY-MM-DD'),
        "dcNumber": grn.dcNumber,
        // "lcop": grn.lcopAmount,
        "dcDate": moment(grn.dcDate).format('YYYY-MM-DD'),
        "items": [],
        "billTypeName":grn.billType.name,
        "creditNoteAmountList": self.model.creditNoteAmountData ? self.model.creditNoteAmountData : [],
        "otherCharges": self.model.otherchargesData ? self.model.otherchargesData : [],
        "organizationId": $scope.organizationId,
        "unitId": $scope.unitId,
        "userId": $scope.userId,
        "vendorId": self.model.vendorId,
        "dueDate": moment(self.model.dueDate).format('DD-MM-YYYY'),
        "invoiceDate": moment(grn.invoiceDate).format('YYYY-MM-DD'),
        "isFromApprovalEdit":$scope.isFromApprovalEdit!=undefined && $scope.isFromApprovalEdit!=null ? $scope.isFromApprovalEdit :null
      };
      angular.forEach(grn.itemDetails, function (item, key) {
        if (self.model.poType.id == 2) {
          var expDateMonth = item.warrantyDate.getMonth() + 1;
          var expDateYear = item.warrantyDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
          var expDateMonth1 = item.warrantyDateTo.getMonth() + 1;
          var expDateYear1 = item.warrantyDateTo.getFullYear();
          var expDateDay1 = new Date(expDateYear1, expDateMonth1, 0).getDate();
        } else if (self.model.poType.id == 1) {
        	if(item.expiryDate!=undefined && item.expiryDate!=null && item.expiryDate!="")
  		  {
          var expDateMonth = item.expiryDate.getMonth() + 1;
          var expDateYear = item.expiryDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
  		  }
        	if(item.bonusReceivedQuantity!=undefined && item.bonusReceivedQuantity!=null && item.bonusReceivedQuantity>0 &&(item.grnBonusBatchExpiryData==undefined || item.grnBonusBatchExpiryData==null || item.grnBonusBatchExpiryData.length==0))
      	  {
        		if(item.grnBonusBatchExpiryData==undefined || item.grnBonusBatchExpiryData==null || item.grnBonusBatchExpiryData.length==0)
      		  {
      		  item.grnBonusBatchExpiryData=[];
      		  }
      	  item.grnBonusBatchExpiryData.push({
	          		grnBonusBatchExpiryId:null,
	          		batchNo: item.batchNumber,
	          		expiryDate: item.expiryDate,
	                bonusQuantity: item.bonusReceivedQuantity!=undefined && item.bonusReceivedQuantity!=null ? item.bonusReceivedQuantity:0
	              });
      	  }
        	if(item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null && item.grnBonusBatchExpiryData.length==1 && item.batchNumber.toUpperCase() ==item.grnBonusBatchExpiryData[0].batchNo.toUpperCase() )
            {
                item.grnBonusBatchExpiryData[0].bonusQuantity=parseInt(item.bonusReceivedQuantity);
            }
        	if(item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null)
            {
            angular.forEach(item.grnBonusBatchExpiryData, function (bonusExpiry) {
             var expDateMonth = bonusExpiry.expiryDate.getMonth() + 1;
             var expDateYear = bonusExpiry.expiryDate.getFullYear();
             var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
             if(expDateMonth<10){
            	 expDateMonth = "0"+expDateMonth;
             }
             bonusExpiry.expiryDate=(expDateYear + "-" + expDateMonth + "-" + expDateDay);
             
            });
            }
        }

        var bonusUom = [];
        angular.forEach(item.bonusUom, function (bonus) {
          bonusUom.push({
            "conversion": bonus.conversion,
            "id": bonus.id,
            "ipUom": bonus.ipUom,
            "opUom": bonus.opUom,
            "storeUom": bonus.storeUom,
            "uomTypeId": bonus.uomTypeId,
            "uomUnitId": bonus.uomUnitId.uomUnitId
          });
        });

        var poUom = [];
        angular.forEach(item.poUom, function (uom) {
          poUom.push({
            "conversion": uom.conversion,
            "id": uom.id,
            "ipUom": uom.ipUom,
            "opUom": uom.opUom,
            "storeUom": uom.storeUom,
            "uomTypeId": uom.uomTypeId,
            "uomUnitId": uom.uomUnitId.uomUnitId
          });
        });

        var setgrnStagedData = [];
        angular.forEach(item.grnStagedLists, function (data) {
        	delete data.orginalApprovedQty;
        	delete data.originalStagedReceivedQuantity;
          if (!!data.isEditable) {
            data.stagedReceivedQuantity = parseInt(data.stagedReceivedQuantity);
            setgrnStagedData.push(data);
          }
        });
        if(item.bonusReceivedQuantity == 0 || item.bonusReceivedQuantity == undefined){
          item.bonusReceivedQuantity = null;
        }
        var convertedGrnReceivedQuantity=0;
        if(item.poUomTypeId!=undefined && item.poUomTypeId!=null)
    	{
        if(item.poUomTypeId!=item.selectedUom.uomTypeId)
        	{
        	var grnQtyForConversion=0;
        	if(item.isThreeUOMApplicable==true)
    		{
        	if(item.selectedUom.uomTypeId==1)
        	{
        		grnQtyForConversion=item.grnReceivedQuantity*(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
        	}
        	else if(item.selectedUom.uomTypeId==2)
    		{
        		grnQtyForConversion=item.grnReceivedQuantity*(item.uom[1].conversion*item.uom[2].conversion);
    		}
        	else
        		{
        		grnQtyForConversion=item.grnReceivedQuantity
        		}
    		}
    	else
    		{
    		if(item.selectedUom.uomTypeId==1)
    		{
    			grnQtyForConversion=item.grnReceivedQuantity*(item.uom[0].conversion*item.uom[1].conversion)
    		}
    		else
    			{
    			grnQtyForConversion=item.grnReceivedQuantity
    			}
    		}
        	
        	if(item.isThreeUOMApplicable==true)
    		{
        	if(item.poUomTypeId==3)
        	{
        		convertedGrnReceivedQuantity=grnQtyForConversion/(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
        	}
        	else if(item.poUomTypeId==2)
    		{
        		convertedGrnReceivedQuantity=grnQtyForConversion/(item.uom[1].conversion*item.uom[2].conversion);
    		}
        	else
        		{
        		convertedGrnReceivedQuantity=grnQtyForConversion
        		}
    		}
    	else
    		{
    		if(item.poUomTypeId==1)
    		{
    			convertedGrnReceivedQuantity=item.grnReceivedQuantity/(item.uom[0].conversion*item.uom[1].conversion)
    		}
    		}
        	}
        else
    	{
    	convertedGrnReceivedQuantity=item.grnReceivedQuantity;
    	}
      }
        else
        	{
        	convertedGrnReceivedQuantity=item.grnReceivedQuantity;
        	}
        var GRNItem = {
          "id": item.id,
          "itemId": item.itemId,
          "bonusReceivedQuantity":  (item.bonusReceivedQuantity != undefined && item.bonusReceivedQuantity != null) ? item.bonusReceivedQuantity :0,
          "grnReceivedQuantity": item.grnReceivedQuantity,
          "manufacturerId": item.manufacturerId,
          "cop": item.costOfPurchase,
          "mrp": item.mrp,
          "discount": item.discountPercent,
          "taxAmount": !isNaN(item.taxAmount) ? item.taxAmount.toFixed(2) :0.00,
          "totalAmount":!isNaN(item.grnAmount) ?  item.grnAmount.toFixed(2):0.00,
          "totalDiscount": (!isNaN(item.discountAmount)  || item.discountAmount!=0.00) ? parseFloat(item.discountAmount).toFixed(2) : 0.00,
          "totalOtherAmount": (!isNaN(item.otherChargesAmount))? item.otherChargesAmount.toFixed(2):0.00,
          "netAmount": !isNaN(item.netAmount) ? item.netAmount.toFixed(2) :0.00,
          "remark": item.itemRemark,
          "grnSpecification": item.itemSpecification,
          "grnStagedLists": setgrnStagedData,
          "poDetailId": item.poDetailId,
          "batchNo": item.batchNumber,
          "lcop": !isNaN(item.lcopAmount) ?  item.lcopAmount:0.00,
          "taxId": item.selectedTax.taxId,
          "taxPercentage": parseFloat(item.selectedTaxPercent),
          "pendingQuantity": 0,
          "grnBonusBatchExpiry":item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null ? item.grnBonusBatchExpiryData:[],
          "grnQtyUomTypeWise":convertedGrnReceivedQuantity!=undefined && convertedGrnReceivedQuantity!=null ? convertedGrnReceivedQuantity:0
        }

        if(self.model.prRequestTypeId == 4){
          GRNItem.pendingQuantity = item.pendingQuantity;
        }
        else{
          GRNItem.pendingQuantity = item.newMaxOrderQuantity
        }

        if (!!item.id) {
          GRNItem.statusId = undefined;
        } else {
          GRNItem.statusId = setStatus("New");
        }

        if (self.model.poType.id == 2) {
          var grnAccessories = [];
          if( item.grnAccessoriesMapper!=undefined)
          {
            item.grnAccessoriesMapper.forEach(function (value) {
            grnAccessories.push({
              "id": value.id,
              "itemId": value.itemId,
              "serialNo": value.serialNo,
              "warrentyFrom": value.warrentyFrom,
              "warrentyTo": value.warrentyTo,
              "quantity": value.quantity
            });
          })   
          }
          GRNItem.warrentyPeriod = item.warrantyPeriod;
          GRNItem.supportTerms = item.supportTerms;
          GRNItem.technicalSpecification = item.technicalSpecs;
          GRNItem.expectedPayment = item.paymentTerms;
          GRNItem.grnUomTypeId = undefined;
          GRNItem.grnUomUnitId = undefined;
          GRNItem.bonusUomUnitId = undefined;
          GRNItem.bonusUomTypeId = undefined;
          if(expDateMonth < 10){
            expDateMonth = "0"+expDateMonth;
          }
          if(expDateMonth1 < 10){
            expDateMonth1 = "0"+expDateMonth1;
          }
          GRNItem.warrantyDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
          GRNItem.warrantyDateTo = (expDateYear1 + "-" + expDateMonth1 + "-" + expDateDay1);
          GRNItem.serviceProviderId = item.serviceProviderId || item.providerId;
          GRNItem.expiryDate = undefined;
          GRNItem.grnAccessoriesMapper = grnAccessories;
          GRNItem.grnUom = undefined;
          GRNItem.bonusUom = undefined;
          /*
			 * GRNItem.taxId = item.selectedTax.taxId; GRNItem.taxPercentage =
			 * parseFloat(item.selectedTaxPercent);
			 */
        } else if (self.model.poType.id == 1) {
          GRNItem.warrentyPeriod = undefined;
          GRNItem.supportTerms = undefined;
          GRNItem.technicalSpecification = undefined;
          GRNItem.expectedPayment = undefined;
          GRNItem.grnUomTypeId = item.selectedUom.uomTypeId;
          GRNItem.grnUomUnitId = item.selectedUom.uomUnitId;
          if(item.bonusSelectedUom){
        	  GRNItem.bonusUomUnitId = item.bonusSelectedUom.uomUnitId;
        	  GRNItem.bonusUomTypeId = item.bonusSelectedUom.uomTypeId;
        	  GRNItem.bonusUom = bonusUom;
          }
          else{
        	  GRNItem.bonusUomUnitId = null;
        	  GRNItem.bonusUomTypeId = null;
        	  GRNItem.bonusUom = null;
          }
          GRNItem.warrantyDate = undefined;
          GRNItem.warrantyDateTo = undefined;
          if(expDateMonth < 10){
            expDateMonth = "0"+expDateMonth;
          }
          if(expDateYear!=undefined && expDateMonth!=undefined && expDateDay!=undefined)
    	  {
          GRNItem.expiryDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
    	  }
          GRNItem.serviceProviderId = undefined;
          GRNItem.grnAccessoriesMapper = undefined;
          GRNItem.grnUom = poUom;
          // GRNItem.bonusUom = bonusUom;
          /*
			 * GRNItem.taxId = item.taxId; GRNItem.taxPercentage =
			 * parseFloat(item.taxPercent);
			 */
        }

        data.items.push(GRNItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          if($scope.isFromApprovalEdit==true)
    	  {
    	  $state.go('GRNApproval');
    	  }
      else
    	  {
    	  NavigateToGRN();
    	  }
          if($scope.encounterDetailsList.length == 1){
        	  angular.forEach($scope.encounterDetailsList,function(value,key){
        		  if (!angular.equals(value.file, {}) && value.file != undefined) {
        		      $scope.uploadPatientImage();
        		  }
        	  });
          }
          if($scope.encounterDetailsList.length > 1){
          $scope.uploadPatientImage();
          }
          growl.success(response.data.message);
        },
        function (err) {
        	 $scope.saveBtnDisabled=false;
          $rootScope.stopLoader();
          growl.error(err.data.summary);
        });
    }

    var AddItemDiscount = function (type, item, value, index) {
      if (type == 'percent') {
        // item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
        if (isNaN(item.discountPercent) || !item.discountPercent || item.discountPercent == 0) {
          // item.discountPercent = 0;
          if(self.model.discountType == 1){
            self.model.finalDiscount = 0;
          }
          item.discountAmount = 0;
          $timeout( function(){
            $scope.calculateRoundoff1();
            if(self.model.discountType == '2'){
              $scope.calculateAmountDiscount();
            }
          },1000);
        } else {
          if (parseFloat(item.discountPercent) > 100){
                          growl.error('Discount must be less than 100% of total amount');
                                  item.discountPercent=0;
                      }

          if (parseFloat(item.discountPercent) > 100) {
            if(self.model.discountType == 1){
              self.model.finalDiscount = 0;
            }
            self.model.discountPercent[index] = true;

          } else {
            if (!item.grnAmount) {
              item.discountAmount = 0;
              item.lcopAmount = 0;
              if(self.model.discountType == 1){
                self.model.finalDiscount = 0;
              }
            } else {
              if(self.model.discountType == 1){
                self.model.finalDiscount = 0;
              }
              item.discountAmount = (item.grnAmount) * (item.discountPercent / 100);
              self.model.disAmount = item.discountAmount;
              item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
              item.discountAmount = parseFloat(item.discountAmount);
              item.lcopAmount = (((item.grnAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + (item.receivedQtyBonusCalculation!=undefined && item.receivedQtyBonusCalculation!=null ? parseInt(item.receivedQtyBonusCalculation):0)));
              $timeout( function(){
                $scope.calculateRoundoff1();
                if(self.model.discountType == '2'){
                  $scope.calculateAmountDiscount();
                }
              },1000);
            }
          }
        }

      } else if (type == 'amount') {
        // item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
        if (isNaN(item.discountAmount) || !item.discountAmount || item.discountAmount == 0) {
          // item.discountAmount = 0;
          item.discountPercent = 0;
          $timeout( function(){
            $scope.calculateRoundoff1();
            if(self.model.discountType == '2'){
              $scope.calculateAmountDiscount();
            }
          },1000);
        } else {
          if (parseFloat(item.discountAmount) > (parseFloat(item.grnAmount))) {
            self.model.discountAmount[index] = true;
            item.discountPercent=0;
                        item.discountAmount=0
                        growl.error('Discount amount should be less than of total amount');
                         return false
          } else {
            if (!item.grnAmount) {
              item.discountPercent = 0;
              item.lcopAmount = 0;
            } else {
              item.discountPercent = ((parseFloat(item.discountAmount) / item.grnAmount) * 100);
              self.model.disAmount = item.discountAmount;
              item.discountPercent = parseFloat(item.discountPercent);
              item.lcopAmount = (((item.grnAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + (item.receivedQtyBonusCalculation!=undefined && item.receivedQtyBonusCalculation!=null ? parseInt(item.receivedQtyBonusCalculation):0)));
              $timeout( function(){
                $scope.calculateRoundoff1();
                if(self.model.discountType == '2'){
                  $scope.calculateAmountDiscount();
                }
              },1000);
            }
          }
        }
      }
    }

    $scope.validatecop = function (items) {
      var isValid = true;
      angular.forEach(items, function(d){
    	  if(d.grnAmount != undefined && !d.grnAmount){
          growl.error("Please enter cost of purchase");
          isValid = false;
          $scope.saveBtnDisabled = false;
        }
      });
      return isValid;
    }

    var ApplyDiscount = function (items) {
      if($scope.validatecop(items)){
        var discount = parseFloat(self.model.finalDiscount);
        if (!isNaN(discount)) {
          if (self.model.discountType == '2') {
            // if (discount < (parseFloat(self.model.totalAmount) / 2)) {
              var discountPercent = discount / self.model.totalAmount;
              angular.forEach(items, function (value, key) {
                if (value.grnAmount > discountPercent) {
                  value.discountAmount = value.grnAmount * discountPercent;
                  if(value.discountAmount>value.grnAmount)
                  {
                	  growl.error('Discount Amount must be less than or equal to total amount');
                	  value.discountAmount=0;
                	  value.discountPercent=0;
                	  self.model.finalDiscount=0;
                  }
                  value.discountPercent = ((parseFloat(value.discountAmount) / value.grnAmount) * 100);
                  value.discountAmount = value.discountAmount.toFixed(2);
                  value.discountPercent = value.discountPercent;
                  self.model.discountPercent[key] = false;
                  self.model.discountAmount[key] = false;
                  $timeout( function(){
                    $scope.calculateRoundoff1();
                    if(value.discountAmount == value.grnAmount){
                      self.model.totalNetAmount1 = 0.00;   
                    }
                    if(self.model.discountType == '2'){
                      $scope.calculateAmountDiscount();
                    }
                  },1000);
                } else {
                  value.discountAmount = 0;
                }
                $scope.updateEditFlag(value);
              });
          } else if (self.model.discountType == '1') {
            if (discount <= 100) {
              angular.forEach(items, function (value, key) {
                value.discountAmount = value.grnAmount * discount * 0.01;
                value.discountPercent = ((parseFloat(value.discountAmount) / value.grnAmount) * 100);
                value.discountAmount = value.discountAmount.toFixed(2);
                value.discountPercent = value.discountPercent;
                self.model.discountPercent[key] = false;
                self.model.discountAmount[key] = false;
                $timeout( function(){
                  $scope.calculateRoundoff1();
                  if(discount == 100){
                    self.model.totalNetAmount1 = 0.00;
                  } 
                  if(self.model.discountType == '2'){
                    $scope.calculateAmountDiscount();
                  }
                },1000);
                $scope.updateEditFlag(value);
              });
            }
            else {
              growl.error('Discount must be less than 100% of total amount');
            }
          }
        }
      }
    }

    var NavigateToGRN = function () {
      $state.go('GRN');
    }
    $scope.removeItemFlag=false;
    var ConfirmRemove = function (item) {
      self.model.OpenPopup('Open', 'confirmRemove');
      $scope.removeItemFlag=true;
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var index = self.model.itemDetails.indexOf(item);
      if($scope.removeItemFlag==true)
    	  {
    	  self.model.itemDetails.splice(index, 1);
    	  $scope.removeItemFlag=false;
    	  }
      self.model.ClosePopup('Close', 'confirmRemove');
      $scope.calculateRoundoff1();
    }

    var RemoveAccessoryItem = function (item) {
      var newArray = [];

      self.model.accessoryList.map(function (obj) {
        if (obj.itemId !== item.itemId) {
          newArray.push(obj);
        }
      });

      self.model.accessoryList = newArray;
    }

    var calculateOtherChargesAmountForItem = function () {
    	self.model.totalNetAmount=0;
    	angular.forEach(self.model.itemDetails, function(val){
    		if(val.grnReceivedQuantity!=undefined && val.grnReceivedQuantity!=null && val.grnReceivedQuantity>0)
    			{
    			self.model.totalNetAmount=self.model.totalNetAmount+val.netAmount;
    			}
    	});
      angular.forEach(self.model.itemDetails, function(val){
    	  self.model.otherChargesPer = ((val.netAmount * 100) /( self.model.totalNetAmount + self.model.totalCreditNoteAmount)); 
    	  self.model.otherChargesPer = parseFloat(self.model.otherChargesPer).toFixed(2);
        self.model.calculateOtherCharges = (self.model.otherChargesAmount * (self.model.otherChargesPer / 100));
        val.otherChargesAmount = parseFloat(self.model.calculateOtherCharges).toFixed(2);
        val.otherChargesAmount = parseFloat(val.otherChargesAmount);
        val.discountAmount = (val.grnAmount) * (val.discountPercent / 100);
        self.model.disAmount = val.discountAmount;
        val.lcopAmount = (((val.grnAmount - self.model.disAmount) + (val.taxAmount + val.otherChargesAmount)) / ((val.receivedQtyCalculation!=undefined && val.receivedQtyCalculation!=null ? parseInt(val.receivedQtyCalculation) : 0) + (val.receivedQtyBonusCalculation!=undefined && val.receivedQtyBonusCalculation!=null ? parseInt(val.receivedQtyBonusCalculation) : 0)));
        $timeout( function(){
        if($scope.editFlag==false)
        {
    		$scope.calculateRoundoff1();
        }
          if(self.model.discountType == '2'){
            $scope.calculateAmountDiscount();
          }
        },1000);
      })
    }

    $scope.roundoffCalculation = function(){
      self.model.totalNetAmount1 = 0;
      angular.forEach(self.model.itemDetails, function(val){
        self.model.totalNetAmount1 += val.netAmount;
      });
      if($scope.roundOff < 0){
        var roundoff = $scope.roundOff.split("-");
        self.model.totalNetAmount1 = self.model.totalNetAmount1 - parseFloat(roundoff[1]);
      } 
      if($scope.roundOff > 0){
        self.model.totalNetAmount1 = self.model.totalNetAmount1 + parseFloat($scope.roundOff);
      }
      if($scope.roundOff == 0){
        self.model.totalNetAmount1 = 0;
        angular.forEach(self.model.itemDetails, function(val){
          self.model.totalNetAmount1 += val.netAmount;
        });
      }
    }


    $scope.batchExpiryMsgShow = function(item){
    	
    	if(item.expiryDate != undefined && item.expiryDate != null){
    		
    		 var year = moment(new Date()).format("YYYY");
        	 var expYear = moment(item.expiryDate).format("YYYY");
        	 
        	 if(expYear == year){
        		 
        		  	var date = moment(new Date()).format("MM");
        		    var dateCal = parseInt(date) + 3;
        		    var expDate = moment(item.expiryDate).format("MM");
        		    
        		 if(dateCal > expDate){
        			 growl.warning(" Please select after 3 month's date from current date ");
        		 }
        	 }
    		
    	}
    	
    	
    }
    
    
    var rePrintBillSlip = function (grn, storeId) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_GRN.rpt",
        "reportFormat": "PDF",
        "exportFileName": "Grn",
        "reportParams": [
          {
            "paramName": "Id",
            "paramType": "Integer",
            "paramValue": grn.id
          },
          {
            "paramName": "Storeid",
            "paramType": "Integer",
            "paramValue": storeId
          }
        ]
      };
      reportService.generate(REPORT_API_URI1, data);
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addItemGRN", {
      url: "/addItemGRN",
      templateUrl: 'views/procurement/quotation/add-item-GRN.html',
      controller: "AddNewGRN.Controller",
      controllerAs: "vm",
      title: 'Good Receipt Note'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewGRN.Controller", addNewGRNController);
})();
