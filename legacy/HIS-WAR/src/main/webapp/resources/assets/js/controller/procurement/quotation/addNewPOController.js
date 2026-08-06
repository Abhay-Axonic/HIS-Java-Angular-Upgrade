(function () {
  "use strict";

  function addNewPOController($scope,$cookies, $rootScope, $http, $filter, $state, CONSTANTS, BillingGenericService, GenericServiceFileUpload, GenericService,PaginationService, commonDetailService, growl, CurrencyService, StatusService, $timeout) {
    var self = this;
    var getNextMonth = new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1);

    $scope.vqlNo="";
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
        isLocalPurchase: false,
        Manufactures: [
          {
            name: "Select Manufacturer"
          }
        ],
        modeOfPayments: [
          {
            paymentModeDesc: "Select Mode of Payment"
          }
        ],
        Countries: [
          {
            countryName: "Select Country"
          }
        ],
        States: [
          {
            stateName: "Select State"
          }
        ],
        Cities: [
          {
            cityName: "Select City"
          }
        ],
        Areas: [
          {
            areaName: "Select Area"
          }
        ],
        fromDate: new Date(),
        toDate: new Date(),
        dueDate: new Date(),
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
        isConsignment: false,
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
          "uomType": "Select UOM",
          "uomDetails": "Select UOM"
        }],
        PRType: 1,
        prRequestTypeId: 1,
        taxListSelection: [{
          "taxName": "Select tax"
        }]
      };
      $scope.updateFlag = false;
      $scope.allvendors = function(vendorNameAfterEditInitiate){

          var URI;
          var data = "";
          if(vendorNameAfterEditInitiate!=undefined && vendorNameAfterEditInitiate!=null && vendorNameAfterEditInitiate!="")
    	  {
        	  $scope.updateFlag = true;
        	  URI = CONSTANTS.GLOBAL.ALL_VENDOR_SEARCH_API + "unit/"+$scope.unitId+"/vendorName/"+vendorNameAfterEditInitiate;
    	  }
          else
        	  {
        	  vendorNameAfterEditInitiate="noSelectedVendorFound";
        	  URI = CONSTANTS.GLOBAL.ALL_VENDOR_SEARCH_API + "unit/"+$scope.unitId+"/vendorName/"+vendorNameAfterEditInitiate;
        	  }
          GenericService.serviceAction("GET", URI, data).then(function(response) {
            if (response.status == 200) {
             $scope.allVendorList = response.data;    
             /* angular.forEach(response.data,
                  function(value,index) {
                    var data = {};
                    data.id = value.id;
//                    data.detail = value.detail;
                    data.label = value.detail;
                    $scope.venderMultiSelectList.push(data);
              });*/
            
            }
          });	
        
      	
      }
      
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
      self.model.OpenDueDate = OpenDueDate;
      self.model.OpenStagedQtyDate = OpenStagedQtyDate;
      self.model.OpenToDate = OpenToDate;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.GetReorderPoItemList=GetReorderPoItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SavePurchaseOrder = SavePurchaseOrder;
      self.model.NavigateToPO = NavigateToPO;
      self.model.ChangePRType = ChangePRType;
      self.model.ChangePoReOrderPRType=ChangePoReOrderPRType;
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
      self.model.CalculateStock1 = CalculateStock1;
      self.model.CalculateStock2 = CalculateStock2;
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
      self.model.calculateOtherChargesAmountForItem = calculateOtherChargesAmountForItem;
      PopulateDropdown();
      
      self.model.RemoveViewAddedItems=RemoveViewAddedItems;
      self.model.ResetPOQuantity=ResetPOQuantity;
      self.model.PopulateVendorDropdown = PopulateVendorDropdown;
      PopulateVendorDropdown();
      $scope.selectedManufacturesList=[]; 
      self.model.PopulateStates = PopulateStates;
      self.model.PopulateCities = PopulateCities;
      self.model.PopulateAreas = PopulateAreas;
      self.model.ValidateVendor = ValidateVendor;
      self.model.SaveVendorMaster = SaveVendorMaster;
      self.model.ValidateEmail = ValidateEmail;
      self.model.ValidateVendorName = ValidateVendorName;
      self.model.ValidateGst = ValidateGst;
      self.model.ValidateLicNo = ValidateLicNo;
      self.model.isShop1=isShop1;
      self.model.isShop1Hide=isShop1Hide;
      self.model.SelectIndentAllItem = SelectIndentAllItem;
      self.model.uploadDoccument = uploadDoccument;
    }

    var OpenDueDate = function () {
      self.model.dueDateOpened = true;
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
      $scope.encounterDetails.poId = $scope.grnIdForDoccumentUpload;
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
      if($scope.grnIdForDoccumentUpload != undefined && $scope.grnIdForDoccumentUpload != null && $scope.grnIdForDoccumentUpload != 0)
      {
        $rootScope.startLoader();
        var data =
        {
          "patientId":0,
          "unitId":$scope.unitId,
          "organisationId":$scope.organizationId,
          "userId":undefined,
          "visitTypeId":undefined,
          "admissionId":undefined,
          "poId":$scope.grnIdForDoccumentUpload,
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
      fd.append('grnId', 0);
      fd.append('poId', $scope.grnIdForDoccumentUpload);
      fd.append('incidentReportingMapperId',0);
      fd.append('companyExpenseId',0);
      fd.append('createdDate',null);
	    fd.append('fileEditedName',null);
	    fd.append('vendorId',0);
      fd.append('opbId',0);
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
      // item.poAmount = item.poAmount || 0;
      item.poAmount = item.poQuantity * item.costOfPurchase; 
      item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
      item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
      $timeout( function(){
        $scope.calculateRoundoff1();
        if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
          self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1);//was adding other charges two times
        }
      },1000);
    }

    $scope.amountround1 = 0;

    $scope.calculateRoundoff1 = function () {
      if($scope.amountround1 != 0){
        $scope.amountround1 = 0;
      }
      self.model.totalNetAmount1 = 0;
      $scope.roundOff1 = 0;
      self.model.totalAmount = 0;
      self.model.totalDiscount = 0;
      self.model.totalTax = 0;
      angular.forEach(self.model.itemDetails, function(item){
        if(item.otherChargesAmount == undefined)
        {
          item.otherChargesAmount = 0;
        }
        if(item.discountAmount == undefined){
          item.discountAmount = 0;
        }
        // item.costOfPurchase = parseFloat(item.costOfPurchase);
        item.discountAmount = item.discountAmount;
        item.poAmount = item.costOfPurchase * item.poQuantity;
        if(item.selectedTaxPercent){
          item.taxAmount = ((item.poAmount - parseFloat(item.discountAmount)) * (item.selectedTaxPercent/100)); 
        }
        else{
          item.taxAmount = ((item.poAmount - parseFloat(item.discountAmount)) * (item.taxPercent/100)); 
        }
        item.netAmount = (item.poAmount - parseFloat(item.discountAmount)) + (item.taxAmount) + (item.otherChargesAmount);
        self.model.totalAmount += item.poAmount;
        self.model.totalDiscount += parseFloat(item.discountAmount);
        self.model.totalTax += item.taxAmount;
        $scope.amountround1 = $scope.amountround1 + (item.poAmount - parseFloat(item.discountAmount)) + item.taxAmount;
        $scope.amountround = $scope.amountround1.toFixed();
        $scope.amount1 = parseFloat($scope.amountround1).toFixed(2);
        $scope.roundOff1 = parseFloat($scope.amountround) - $scope.amount1;
        if($scope.roundOff1 < 0.50){
          self.model.totalNetAmount1 = $scope.amountround;
          $scope.roundOff = parseFloat($scope.amountround) - $scope.amount1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
        }
        if($scope.roundOff1 >= 0.50){
          self.model.totalNetAmount1 = $scope.amountround;
          $scope.roundOff = self.model.totalNetAmount1 - $scope.amountround1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
        }
      });
      if(self.model.otherChargesAmount == undefined){
        self.model.otherChargesAmount = 0;
      }
      self.model.totalOtherCharges=self.model.otherChargesAmount;
      self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1) + parseFloat(self.model.otherChargesAmount);
      $rootScope.stopLoader();
    }

    $scope.roundoffCalculation = function(){
      self.model.totalNetAmount1 = 0;
      angular.forEach(self.model.itemDetails, function(val){
        self.model.totalNetAmount1 += val.netAmount;
      });
      // self.model.totalNetAmount1 = self.model.totalNetAmount1 + self.model.otherChargesAmount;
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

    var ViewItems = function (item) {
      PopulateAssetCategoryDropdown();
      self.model.addItemFlag = false;
      self.model.selectedItemId = item.itemId;
      
      // start code for resolved HIS-9490 PO for asset details - Accessories shows same items in both the line item
      // rejected reason resolved Second add button pre selects the accessories item added in the first line.
      angular.forEach(self.model.pagination.data,function(data){
    	  data.isSelected = false;
      });
      self.model.accessoryItems=[];
      self.model.addedAccessoryItems = item.childAccessoryItems;
      self.model.selectAllRows = false;
      // end code for resolved HIS-9490 PO for asset details - Accessories shows same items in both the line item
      
    }
    $scope.manufacturesMultiSelectList =[];
    var PopulateVendorDropdown = function(){
        var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId ;
        var vendorMaster = self.model;

        // vendorMaster.manufactureName = vendorMaster.Manufactures[0];
       
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {

                // self.model.Manufactures = self.model.Manufactures.concat(response.data.manufacturer);
                self.model.Countries = self.model.Countries.concat(response.data.country);
                self.model.modeOfPayments = self.model.modeOfPayments.concat(response.data.paymentMode);
                self.model.mobileDigit = response.data.mobileDigit;
                $scope.manufacturesList = response.data.manufacturer;    
                angular.forEach(response.data.manufacturer,
                    function(value,index) {
                      var data = {};
                      data.id = value.id;
                      data.label = value.name;
                      $scope.manufacturesMultiSelectList.push(data);
                });
                // getVendorMasterList();
            },
            function errorCallback(response) {
              $rootScope.stopLoader();
            });
      }
    var ViewAddedItems = function (item) {
      self.model.addedAccessoryItems = item.childAccessoryItems;
      self.model.ViewAddedItemsIdForRemove=item.itemId;
      //self.model.ViewAddedItemsPrIdForRemove=item.prIds[0];
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
        self.model.totalAmount = 0;
        self.model.totalNetAmount1 = 0;
        $scope.roundOff1 = 0;
        self.model.totalAmount = 0;
        self.model.totalDiscount = 0;
        self.model.totalTax = 0;
        self.model.otherChargesAmount = 0;
        self.model.paymentMode="";
        self.model.finalDiscount=0;
        self.model.discountTypeFlag=false;
        self.model.purchaseOrderRemark="";
        self.model.otherChargesList = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
        $scope.roundOff = 0;
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
      self.model.paymentMode = '';
      self.model.purchaseOrderRemark = '';
      self.model.finalDiscount = 0;
      self.model.discountType = 0;
      self.model.otherChargesAmount = 0;
      $scope.roundOff = 0;
      self.model.totalNetAmount1 = 0;
      self.model.totalOtherAmount = 0;

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

    var fillOtherChargesList = function (item) {
      angular.forEach(item, function(data){
        self.model.selectedOtherChargeIndex = self.model.itemDetails.indexOf(data);
        if(data.selectedOtherCharges==undefined || data.selectedOtherCharges==null)
        	{
        	data.selectedOtherCharges=[{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
        	}
        angular.forEach(data.selectedOtherCharges, function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            return key.id == value.otherChargesId;
          });
        });
        if(data.selectedOtherCharges!=undefined && data.selectedOtherCharges!=null && data.selectedOtherCharges.length>0)
        	{
        data.selectedOtherCharges.forEach(function (othercharge) {
          var amountNumber = parseFloat(othercharge.amount);
          othercharge.amount = amountNumber.toFixed(2);
          othercharge.savedAmount = amountNumber.toFixed(2);
        });
        	}
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
          if(charges.otherCharge == undefined){
            if (charges.id) {
              if (!parseFloat(charges.amount)) {
                charges['isAmountValid'] = true;
                growl.error('Please enter amount');
                isValidItem = false;
              }
            }
          }
          else{
            if (charges.otherCharge.id) {
              if (!parseFloat(charges.amount)) {
//                charges['isAmountValid'] = true;
//                growl.error('Please enter amount');
//                isValidItem = false;
              }
            }
          }
          if (!!parseFloat(charges.amount)) {
            if(charges.otherCharge == undefined){  
              if (!charges.id) {
                charges['isChargesValid'] = true;
                growl.error('Please select other charges');
                isValidItem = false;
              }
            }
            else{  
              if (!charges.otherCharge.id) {
                charges['isChargesValid'] = true;
                growl.error('Please select other charges');
                isValidItem = false;
              }
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
        if(item.otherCharge == undefined){
          if (!!item.id) {
            if (!object[item.id])
              object[item.id] = 0;
            object[item.id] += 1;
          }
        }
        else{
          if (!!item.otherCharge.id) {
            if (!object[item.otherCharge.id])
              object[item.otherCharge.id] = 0;
            object[item.otherCharge.id] += 1;
          }
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
    $scope.cancelOtherCharges = function ()
    {
    	if(self.model.otherChargesList!=undefined && self.model.otherChargesList!=null && self.model.otherChargesList.length>0)
    		{
    		self.model.otherChargesList.forEach(function (othercharge) {
                othercharge.amount=(othercharge.savedAmount!=undefined && othercharge.savedAmount!=null ? othercharge.savedAmount :0);
            });
    		}
//    	var total = 0;
//    	var otherList=[];
//        self.model.otherChargesList.forEach(function (value) {
//          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
//            if(value.otherCharge == undefined){
//              if (value.id == key.id) {
//                value.otherChargesId = key.id;
//                return key;
//              }
//            }
//            else{
//              if (value.otherCharge.id == key.id) {
//            	  
//            	  if(value.otherChargesId==undefined)
//            		  {
//            		  value.otherChargesId = key.id;
//            		  }
//            	  else
//            		  {
//            		  value.otherChargesId = key.id;
//            		  }
//                return key;
//              }
//            }
//          });
//        });
//
//        angular.forEach(self.model.otherChargesList, function (value, key) {
//          total = total + parseFloat(value.amount);
//          if(value.amount!=undefined && value.amount!=null && value.amount>0)
//          {
//          otherList.push(value)
//          }
//    });
//    self.model.otherChargesList=otherList;
//        self.model.itemDetails[self.model.selectedOtherChargeIndex].otherChargesTotal = total;
//        self.model.otherChargesAmount = total;
        self.model.ClosePopup('close', 'otherCharges_Modal');
//        calculateOtherChargesAmountForItem();
    }
    var sumOtherCharges = function (chargeList) {
    	$rootScope.startLoader();
      if (validateCharges() && DuplicateData(chargeList)) {
        var total = 0;
        self.model.otherChargesList.forEach(function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            if(value.otherCharge == undefined){
              if (value.id == key.id) {
                value.otherChargesId = key.id;
                return key;
              }
            }
            else{
              if (value.otherCharge.id == key.id) {
            	  
            	  if(value.otherChargesId==undefined)
            		  {
            		  value.otherChargesId = key.id;
            		  }
            	  else
            		  {
            		  value.otherChargesId = key.id;
            		  }
                return key;
              }
            }
          });
        });

        angular.forEach(self.model.otherChargesList, function (value, key) {
          total = total + parseFloat(value.amount);
        });
        self.model.itemDetails[self.model.selectedOtherChargeIndex].otherChargesTotal = total;
        self.model.otherChargesAmount = total;
        self.model.ClosePopup('close', 'otherCharges_Modal');
        calculateOtherChargesAmountForItem();
      }
    }

    var calculateOtherChargesAmountForItem = function () {
      angular.forEach(self.model.itemDetails, function(val){
        if(val.netAmount){
          self.model.otherChargesPer = ((val.netAmount * 100) / self.model.totalNetAmount); 
          self.model.calculateOtherCharges = (self.model.otherChargesAmount * (self.model.otherChargesPer / 100));
          val.otherChargesAmount = self.model.calculateOtherCharges;
        }
        $timeout( function(){
          $scope.calculateRoundoff1();
        },1000);
      });
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
     
      //resolved unknown bug during resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
      self.model.itemForResetStageQuantityList = item;
      self.model.resetStageQuantityList = angular.copy(self.model.stageQuantityList);
    //resolved unknown bug during resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
    }
    
    //resolved unknown bug during resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
    var ResetPOQuantity = function () {
    	self.model.itemForResetStageQuantityList.stageQuantityList =angular.copy(self.model.resetStageQuantityList);
      }
    //resolved unknown bug during resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity

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

      // if (self.model.PRType == 3) {
      //   if (self.model.poType.id == 1) {
      //     qtyValue = selectedItem.newPRQty;
      //   } else if (self.model.poType.id == 2) {
      //     qtyValue = selectedItem.prQuantity;
      //   }

      //   if (total > qtyValue) {
      //     growl.error("Total staged quantity should be less than PR quantity");
      //     isValid = false;
      //   }
      // }

      if (self.model.PRType == 1) {
        if (total > selectedItem.newMaxOrderQuantity) {
          growl.error("Total staged quantity should be less than pending quantity");
          isValid = false;
        }
      } else if (self.model.PRType == 14) {
        if (total > selectedItem.newPRQty) {
          growl.error("Total staged quantity should be less than QR quantity");
          isValid = false;
        }
      }
      else {
        if (total > selectedItem.newPRQty) {
          growl.error("Total staged quantity should be less than PR quantity");
          isValid = false;
        }
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

        if($scope.updateFlag){
        	self.model.itemDetails[self.model.selectedIndex].poApprovedQuantity = total;
        	self.model.itemDetails[self.model.selectedIndex].poQuantity = total;
        }
        else{
        	self.model.itemDetails[self.model.selectedIndex].poApprovedQuantity = total;
        	self.model.itemDetails[self.model.selectedIndex].poQuantity = total;
        }
        // self.model.itemDetails[self.model.selectedIndex].poQty = total;
        // for update discount
        selectedItem.discountPercent = selectedItem.discountPercent || 0;
        selectedItem.costOfPurchase = selectedItem.costOfPurchase || 0;

        var baseAmount = parseFloat(selectedItem.costOfPurchase) * total;
        if(self.model.discountType != 2){
          selectedItem.discountAmount = baseAmount * (selectedItem.discountPercent / 100);
          selectedItem.discountAmount = parseFloat(selectedItem.discountAmount).toFixed(2);
        }

        if(self.model.poType.id != 2){
          CalculateStock2(self.model.itemDetails[self.model.selectedIndex], self.model.itemDetails[self.model.selectedIndex].uom, self.model.itemDetails[self.model.selectedIndex].selectedUom);
        }

       // CalculateAmount(selectedItem);
        $timeout( function(){
          $scope.calculateRoundoff1();
        },1000);
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

    var OpenPopup = function (action, modal, accessory) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
      self.model.items = [];
      self.model.pagination.data = [];
      $scope.Accessory = accessory;
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

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
        var purchaseOrder = self.model;
        purchaseOrder.store = purchaseOrder.stores[0];
      //  purchaseOrder.fromStore = purchaseOrder.fromStores[0];
        purchaseOrder.poType = purchaseOrder.poTypes[0];

        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            purchaseOrder.stores = purchaseOrder.stores.concat(response.data.deliveryStores);
            purchaseOrder.fromStores = purchaseOrder.fromStores.concat(response.data.requestingStores);
            purchaseOrder.poTypes = purchaseOrder.poTypes.concat(response.data.assetType);
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            for(var i=0;i<purchaseOrder.fromStores.length;i++)
            {
              if(purchaseOrder.fromStores[i].id==selectedStoreId)
              {
                purchaseOrder.fromStore = purchaseOrder.fromStores[i];
              }
            }

            var poId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if(poId!=undefined && poId!=null)
            	{
            	$scope.vendorNameAfterEditInitiate=poId.vendorName;
            	if (!!poId.id) {
                    $scope.heading = poId.title;
                    commonDetailService.setDataId(null);
                    if($scope.vendorNameAfterEditInitiate!=undefined && $scope.vendorNameAfterEditInitiate!=null && $scope.vendorNameAfterEditInitiate!="")
                	  {
                	  $scope.allvendors($scope.vendorNameAfterEditInitiate);
                	  }
                    setTimeout(GetPO(poId.id), 2000);
                  }
            	}
            else
        	  {
          $scope.allvendors($scope.vendorNameAfterEditInitiate);
        	  }
          },
          function (err) {
            $rootScope.stopLoader();
            if($scope.vendorNameAfterEditInitiate!=undefined && $scope.vendorNameAfterEditInitiate!=null && $scope.vendorNameAfterEditInitiate!="")
        	  {
        	  $scope.allvendors($scope.vendorNameAfterEditInitiate);
        	  }
          else
        	  {
          $scope.allvendors($scope.vendorNameAfterEditInitiate);
        	  }
          });
      } catch (error) {
        alert(error);
      }
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
        "detail": "Vendor not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName(vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
      $scope.vm.model.vendorName =angular.copy(vendor.vendorName);
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

    var ChangePRType = function (prType) {
      if (ValidateDetails()) {
        if (prType == 1) {
          self.model.addItemFlag = true;
          self.model.action = 'Open';
          var popup = angular.element('#addNewItemWasteStore');
          popup.modal('show');
          PopulateAssetCategoryDropdown();
          self.model.items = [];
          self.model.pagination.data=[];
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          self.model.searchItem.itemCode = undefined;
          self.model.searchItem.itemName = undefined;
        } else if (prType == 14) {
          self.model.action = 'Open';
          var popup = angular.element('#approvedQRmodal');
          popup.modal('show');
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
          self.model.indents = [];
          self.model.searchIndent = undefined;
          self.model.prNumber = undefined;
        } else if (prType == 3) {
          self.model.action = 'Open';
          var popup = angular.element('#againstPRmodal');
          popup.modal('show');
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
          self.model.indents = [];
          self.model.searchIndent = undefined;
          self.model.prNumber = undefined;
        }
      }
    }
    
    var ChangePoReOrderPRType = function (prType) {
        if (ValidateDetails()) {
          if (prType == 1) {
        	  
            self.model.addItemFlag = true;
            self.model.action = 'Open';
            var popup = angular.element('#addPOReorder_NewItemWasteStore');
            popup.modal('show');
            PopulateAssetCategoryDropdown();
            self.model.items = [];
            self.model.pagination.data=[];
            self.model.selectAllRows = false;
            self.model.SelectedItemsIds = [];
            self.model.searchItem.itemCode = undefined;
            self.model.searchItem.itemName = undefined;
            GetReorderPoItemList();
          } 
        }
      }

    var GetPO = function (poId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API + $scope.unitId + '/'+$scope.organizationId + '/' + poId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.discountTypeFlag = false;
          self.model.isDiscountAppliedValid = false;
          self.model.isAppliedDiscountValid = false;
          self.model.isDiscountApplied = true;
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });
          self.model.fromStore = self.model.fromStores.find(function (item) {
            return response.data.requestingStoreId == item.id;
          });
          self.model.poType = self.model.poTypes.find(function (potype) {
            return response.data.assetTypeId == potype.id;
          });
          self.model.vendorName = $scope.allVendorList.find(function (item) {
            return response.data.vendorName == item.vendorName;
          });
          angular.forEach(response.data.items, function (item) {
            item.warrantyDate = new Date(item.warrantyDate);
          });
          self.model.dueDate = new Date(response.data.dueDate);
          self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
          self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);

          self.model.poId = response.data.id;
          self.model.PRType = response.data.poTypeId;
          self.model.prRequestTypeId = response.data.poTypeId;
          self.model.vendorId = response.data.vendorId;
         // self.model.vendorName = response.data.vendorName;
          self.model.purchaseOrderRemark = response.data.remark;
          self.model.isConsignment = response.data.isConsignment;
          self.model.discountType = response.data.discountTypeId;
          if (!!parseFloat(response.data.discount)) {
            if(self.model.discountType == 1){
              self.model.finalDiscount = response.data.discount.toFixed(2);
            }
            if(self.model.discountType == 2){
              self.model.finalDiscount = response.data.totalDiscount.toFixed(2);
            }
          }
          self.model.totalNetAmount = response.data.netAmount;
          self.model.totalNetAmount1 = response.data.netAmount;
          $scope.roundOff = response.data.roundOffAmount.toFixed(2);
          self.model.totalAmount = response.data.totalAmount;
          self.model.totalDiscount = response.data.totalDiscount;
          self.model.totalOtherCharges = response.data.totalOtherAmount;
          self.model.totalTax = response.data.totalTaxAmount;
          self.model.paymentMode = response.data.paymentTerm;
          self.model.itemDetails = [];
          
          let otherChargeCalculate=false;

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
              "poApprovedQuantity": item.approvedQuantity != null ? item.approvedQuantity : item.poQuantity,
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
              "discountAmount":(!isNaN(item.totalDiscount) && item.totalDiscount != undefined && item.totalDiscount != null && item.totalDiscount != "") ? item.totalDiscount.toFixed(2) : 0,
              "otherChargesTotal": item.totalOtherAmount,
              "specification": item.specification,
              "remark": item.remark,
              "stageQuantityList": item.purchaseOrderStagedDto,
              "prNumbersList": prDetailsList,
              "warrantyDate": item.warrantyDate,
              "technicalSpecs": item.technicalSpecification,
              //"warrantyPeriod": item.warrentyPeriod,
              "warrantyPeriod": item.warrantyPeriod,
              "supportTerms": item.supportTerms,
              "paymentTerms": item.expectedPayment,
              "childAccessoryItems": item.poAccessoriesMapper,
              "serviceProvider": item.serviceProvider,
              "serviceProviderId": item.serviceProviderId,
              "providerId": item.serviceProviderId,
              "totalPrQty": (item.prQuantity!=undefined && item.prQuantity!=null && item.prQuantity>0 ? item.prQuantity :item.poQuantity) ,
              "totalPrQty1":(item.prQuantity!=undefined && item.prQuantity!=null && item.prQuantity>0 ? item.prQuantity :item.poQuantity) ,
              "poQuantity1": item.poQuantity,
              "prMaxStoreQty": item.poQuantity,
              "stockAvailable": item.stockAvailable,
              "stockAvailable1": item.stockAvailable,
              "uomTypeId": item.poUomTypeId,
              "isThreeUOMApplicable": item.isThreeUOMApplicable,
              "poAccessoriesMapper": {
                "itemIds": []
              }
            }
            poItem.selectedTax = self.model.taxList.find(function (val) {
              return item.taxId == val.taxId;
            });

            // if (!item.otherCharges.length) {
            //   poItem.selectedOtherCharges = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
            // } else {
            //   poItem.selectedOtherCharges = item.otherCharges;
            // }
             if (response.data.otherCharges.length > 0) {
            	 otherChargeCalculate=true;
            	 poItem.selectedOtherCharges = response.data.otherCharges; // added for resolved HIS-9375
             }else{
            	 self.model.otherChargesList = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
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
              poItem.uom[0].uomDetails = poItem.uom[0].uomType; 
              if(poItem.isThreeUOMApplicable == false){
                poItem.uomListdata = poItem.uom;
                poItem.uom = [];
                angular.forEach(poItem.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    poItem.uom.push(data);
                  }
                });
              }
              CalculateStock1(poItem, poItem.uom, poItem.selectedUom);
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
          
          if(otherChargeCalculate){
            fillOtherChargesList( self.model.itemDetails);// added for resolved HIS-9375
     	      // sumOtherCharges(self.model.otherChargesList);
            self.model.otherChargesAmount = response.data.totalOtherAmount;
          }
          self.model.otherChargesAmount = 0;
          if(response.data.otherCharges != null){
        	  angular.forEach(response.data.otherCharges, function(d){
                  self.model.otherChargesAmount += parseFloat(d.amount);
                });
          }
         
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
          if (parseFloat(self.model.finalDiscount) > 100) {
            isValidItem = false;
            growl.error('Discount must be less than 100% of total amount');
          }
        } 
//        else if (self.model.discountType == 2) {
//          if (parseFloat(self.model.finalDiscount) > (parseFloat(self.model.totalAmount) / 2)) {
//            isValidItem = false;
//            growl.error('Please enter discount amount less than half of total amount');
//          }
//        }
      }

      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add items to save purchase order');
        isValidItem = false;
      } else {
        angular.forEach(self.model.itemDetails, function (item, index) {
          if (!item.costOfPurchase) {
            self.model.costOfPurchase[index] = false;
            item.costOfPurchase=0;
           // growl.error("Please enter valid COP");
            isValidItem = true;
          }

          if (parseFloat(item.POAmountMRP) < parseFloat(item.costOfPurchase) || !item.POAmountMRP) {
            self.model.POAmountMRP[index] = false;
            item.POAmountMRP=0.0;
           // growl.error('MRP should be greater than COP');
            isValidItem = true;
          }

          if (!item.poQuantity) {
            item.isPOQuantityValid = true;
            growl.error('Please enter valid PO quantity');
            isValidItem = false;
          }
          // if ((!item.bonusQuantity || item.bonusQuantity == 0)  && item.bonusSelectedUom.uomTypeId) {
          //   item.isPOBonusQuantityValid = true;
          //   growl.error('Please Enter Valid Bonus quantity');
          //   isValidItem = false;
          // }
          // if (item.bonusQuantity > 0 && (!item.bonusSelectedUom || item.bonusSelectedUom.uomTypeId == undefined)) {
          //   item.isPOBonusSelectedUomValid = true;
          //   growl.error('Please Select Bonus UOM');
          //   isValidItem = false;
          // }
          if (item.discountPercent > 100) {
            self.model.discountPercent[index] = true;
            growl.error('Disount should be less than 100%');
            isValidItem = false;
          }

          // if (item.discountAmount > (parseFloat(item.poAmount) / 2)) {
          //   self.model.discountAmount[index] = true;
          //   growl.error('Discount amount should be less than half of total amount');
          //   isValidItem = false;
          // }

           
          if (!item.selectedTax.taxId) {
            growl.error('Please select valid tax code');
            self.model.taxCodeValid[index] = true;
            isValidItem = false;
          }

          // if (!item.selectedTaxPercent) {
          //   growl.error('Please enter valid tax %');
          //   self.model.taxPercent[index] = true;
          //   isValidItem = false;
          // }
          if (self.model.poType.id == 2) {
            if (!item.serviceProvider || !item.providerId) {
              growl.error('Please enter valid service provider');
              self.model.serviceProviderValid[index] = true;
              isValidItem = false;
            }

            if (!item.poQuantity) {
              item.isPOQuantityValid = true;
              growl.error('Please enter valid PO quantity');
              isValidItem = false;
            }

            if (!item.warrantyDate) {
              item.warrantyDateValid = true;
              growl.error('Please select warranty date');
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


          } else if (self.model.poType.id == 1) {
            /*  if (!item.taxPercent) {
               growl.error('Please enter valid tax %');
               self.model.taxPercent[index] = true;
               isValidItem = false;
             } */

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
          }

          if (self.model.prRequestTypeId == 3) {
            if (item.newPRQty < item.poQuantity) {
              item.isPOQuantityValid = true;
              growl.error('Total staged quantity should be less than PR quantity');
              isValidItem = false;
            }
          } else if (self.model.prRequestTypeId == 1) {
            if (item.poQuantity > item.newMaxOrderQuantity) {
              item.isPOQuantityValid = true;
              growl.error('Total staged quantity should be less than pending quantity');
              isValidItem = false;
            }
          }
        });

        if (!self.model.vendorName || !self.model.vendorId) {
          growl.error('Please select required fields');
          isValidItem = false;
          self.model.isVendorValid = true;
        }
      }
      if(!isValidItem)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValidItem;
    }

    var ApplyDiscountValidation = function () {
      self.model.discountTypeFlag = false;
    }

    var AddItemDiscount = function (type, item, value, index) {
      if (type == 'percent') {
    	  item.discountPercentToSave =item.discountPercent;
        // item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
        if (isNaN(item.discountPercent) || !item.discountPercent || item.discountPercent == 0) {
          // item.discountPercent = 0;
          item.discountAmount = 0;
          if(self.model.discountType == 1){
            self.model.finalDiscount = 0;
          }
          $timeout( function(){
            $scope.calculateRoundoff1();
            if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
              self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1) //was adding other charges two times;
            }
          },1000);
        } else {
          if (parseFloat(item.discountPercent) > 100) {
            self.model.discountPercent[index] = true;
            if(self.model.discountType == 1){
              self.model.finalDiscount = 0;
            }
            item.discountPercent=0;
            item.discountPercentToSave=0
            growl.error('Discount must be less than 100% of total amount');
            item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
            item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
            $timeout( function(){
              $scope.calculateRoundoff1();
              if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
                self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1) //was adding other charges two times;
              }
            },1000);
          } else {
            if (!item.poAmount) {
              item.discountAmount = 0;
              if(self.model.discountType == 1){
                self.model.finalDiscount = 0;
              }
            } else {
              if(self.model.discountType == 1){
                self.model.finalDiscount = 0;
              }
              item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
              item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
              $timeout( function(){
                $scope.calculateRoundoff1();
                if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
                  self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1) //was adding other charges two times;
                }
              },1000);
            }
          }
        }

      }
      else if (type == 'amount') {
        // item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
                if(parseFloat(item.discountAmount) > item.poAmount){
                              item.discountAmount=0
                               item.discountPercent = 0;
                               item.discountPercentToSave=0
                               growl.error('Discount amount should be less than total amount');
                              item.discountAmount=0;
                              return false
                                       }
        if (isNaN(item.discountAmount) || !item.discountAmount || item.discountAmount == 0) {
          // item.discountAmount = 0;
          item.discountPercent = 0;
          $timeout( function(){
            $scope.calculateRoundoff1();
            if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
              self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1) //was adding other charges two times;
            }
          },1000);
        } else {
          // if (parseFloat(item.discountAmount) > (parseFloat(item.poAmount) / 2)) {
          //   self.model.discountAmount[index] = true;
          //   growl.error('Discount amount should be less than half of total amount');
          // } else {
            if (!item.poAmount) {
              item.discountPercent = 0;
            } else {
              item.discountPercent = ((parseFloat(item.discountAmount) / item.poAmount) * 100);
              item.discountPercentToSave =item.discountPercent;
              item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
              $timeout( function(){
                $scope.calculateRoundoff1();
                if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
                  self.model.totalNetAmount1 = parseFloat(self.model.totalNetAmount1) //was adding other charges two times;
                }
              },1000);
            }
          // }
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
                value.discountPercentToSave =value.discountPercent;
                value.discountPercent = value.discountPercent.toFixed(2);
                self.model.discountPercent[key] = false;
                self.model.discountAmount[key] = false;
                $timeout( function(){
                  $scope.calculateRoundoff1();
                },1000);
              } else {
                value.discountAmount = 0;
              }
            });
            // if(self.model.otherChargesAmount != undefined){
            //   $timeout(function (){
            //     self.model.totalNetAmount1 = $scope.amountround + self.model.otherChargesAmount;
            //   }, 2000);
            // }
          }
          if( discount > parseFloat(self.model.totalAmount)){
                                     growl.error('Please enter discount amount less than total amount');
                                      return false
                                }
//          else {
//            growl.error('Please enter discount amount less than half of total amount');
//            return false;
//          }
        } else if (self.model.discountType == '1') {
          if (discount <= 100) {
            angular.forEach(items, function (value, key) {
              value.discountAmount = value.poAmount * discount * 0.01;
              value.discountPercent = ((parseFloat(value.discountAmount) / value.poAmount) * 100);
              value.discountAmount = value.discountAmount.toFixed(2);
              value.discountPercentToSave =value.discountPercent;
              value.discountPercent = value.discountPercent.toFixed(2);
              self.model.discountPercent[key] = false;
              self.model.discountAmount[key] = false;
              $timeout( function(){
                $scope.calculateRoundoff1();
              },1000);
            });
            // if(self.model.otherChargesAmount != undefined){
            //   $timeout(function (){
            //     self.model.totalNetAmount1 = $scope.amountround + self.model.otherChargesAmount;
            //   }, 2000);
            // }
          }

           else {
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
          // item.costOfPurchase = 0;
        } else {
          item.costOfPurchase = item.costOfPurchase;
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

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.poType.id+ '/'+$scope.unitId+ '/'+$scope.organizationId;

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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if(self.model.searchItem.itemName){
         if (self.model.searchItem.itemName!=undefined  &&  (self.model.searchItem.itemName.replace(/\s/g, "").length) == 2  ) {

          if($scope.Accessory){
            var URI =  CONSTANTS.GLOBAL.Item_Api_store_listWithMappedStoreAccessories + "unit/"+$scope.unitId; 
          }
          else{
            var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;
          }
        
          var data = {
            // "isConsignment": self.model.isConsignment,
            "assetTypeId": self.model.poType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "requestingStoreId": self.model.fromStore.id,
            "itemCode": self.model.searchItem.itemCode,
            "itemName": self.model.searchItem.itemName,
            "organizationId" : $scope.organizationId
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

        
          if($scope.Accessory){
            var URI =  CONSTANTS.GLOBAL.Item_Api_store_listWithMappedStoreAccessories + "unit/"+$scope.unitId; 
          }
          else{
            var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;
          }
        
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

    var GetReorderPoItemList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        var URI = CONSTANTS.GLOBAL.Item_Api_store_withcomparequantity + "unit/"+$scope.unitId;

//      Item_Api_store;
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
      }
    var SelectTax = function (tax, item) {
      if (!!tax.taxId) {
        item.selectedTaxPercent = tax.taxPercent.toFixed(2);
        $timeout( function(){
          $scope.calculateRoundoff1();
          if(self.model.otherChargesAmount != undefined && !item.otherChargesAmount){
            self.model.totalNetAmount1 = parseInt(self.model.totalNetAmount1); //was adding other charges two times;
          }
        },1000);
      }
      else {
        item.selectedTaxPercent = "";
      }
    }

    var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if (self.model.SelectedItemsIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
        if (self.model.addItemFlag) {
          var URI = CONSTANTS.GLOBAL.POCreation_AddItems+ "/"+$scope.organizationId + "/"+$scope.unitId;
          var data = {
            "itemIds": self.model.SelectedItemsIds,
            "storeId": self.model.store.id,
            "isPoOrGrn":2
          };

          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
              if (!self.model.taxList) {
                self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
              }
              
            
              response.data.itemDetailsList.forEach(function (value, key) {
            	  
            	  // if(self.model.taxList.length > 1){
            		//   value['taxId'] =self.model.taxList[1].taxId; 
                // 	  value['selectedTax'] =self.model.taxList[1].taxName; 
                // 	  value['selectedTaxPercent'] =self.model.taxList[1].taxPercent; 
                //   }
            	  value.stockAvailable1 = value.stockAvailable; 
                value['poQuantity'] = 0;
                value['poApprovedQuantity'] = 0;
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
                  if(value.selectedTax.taxPercent || value.selectedTax.taxPercent == 0) {
                    value.selectedTaxPercent = value.selectedTax.taxPercent.toFixed(2);
                  }
                  if(value.selectedTax.taxPercent == undefined){
                    angular.forEach(self.model.taxList, function(taxdata){
                      if(taxdata.taxName == "Tax 0%"){
                        value.selectedTax = taxdata;
                        value.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                      }
                    });
                  }
                  if (self.model.poType.id == 1) {
                	  //start code for resolved HIS-9612 Incorrect view of the max store quantity.
                	  value.newQty = 0;
                      value.newMaxStoreQuantity = value.maxStoreQuantity;
                      //end code for resolved HIS-9612 Incorrect view of the max store quantity.
                	  
                    selectDefaultUOM(value);
                    self.model.CalculateStock(value, value.uom, value.selectedUom);

                  } else if (self.model.poType.id == 2) {
                    value.newMaxOrderQuantity = value.maxOrderQuantity;
                    value.newMaxStoreQuantity = value.maxStoreQuantity;
                    /* value.selectedTax = self.model.taxList[0];
                    value.selectedTaxPercent = 0; */
                  }
                }
                if(value.goodReceiptNoteDetailDto!=null){
                	value.POAmountMRP = value.goodReceiptNoteDetailDto.lastMrp;
                	value.costOfPurchase = value.goodReceiptNoteDetailDto.lastCOP;
                }
              });

              var popup = angular.element('#addNewItemWasteStore');
              popup.modal('hide');

              var popup1 = angular.element('#addPOReorder_NewItemWasteStore');
              popup1.modal('hide');
              
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

    $scope.bonusConversionQty = function (item) {
      if(item.bonusQuantity == undefined || item.bonusQuantity == null || item.bonusQuantity == ""){
        item.bonusQuantity = 0;
      }
      var bonusUom = item.bonusSelectedUom.uomDetails.split(' ')[0];
      if(item.isThreeUOMApplicable == false){
        if (bonusUom == "P") {
          item.receivedQtyBonusCon=(item.bonusQuantity +"*"+ item.uomListdata[1].conversion);
          item.receivedQtyBonusCalculation = (item.bonusQuantity * item.uomListdata[1].conversion);
          item.uomUnitDataBonus = item.uomListdata[3].uomUnit;
        } else if (bonusUom == "D") {
          item.receivedQtyBonusCon=(item.bonusQuantity);
          item.receivedQtyBonusCalculation = (item.bonusQuantity);
          item.uomUnitDataBonus = item.uomListdata[3].uomUnit;
        }
      }
      else{
        if (bonusUom == "P") {
          item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
          item.receivedQtyBonusCalculation = (item.bonusQuantity * item.uom[1].conversion * item.uom[2].conversion);
          item.uomUnitDataBonus = item.uom[3].uomUnit;
        } else if (bonusUom == "S") {
          item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.uom[2].conversion);
          item.receivedQtyBonusCalculation = (item.bonusQuantity * item.uom[2].conversion);
          item.uomUnitDataBonus = item.uom[3].uomUnit;
        } else if (bonusUom == "D") {
          item.receivedQtyBonusCon=(item.bonusQuantity);
          item.receivedQtyBonusCalculation = (item.bonusQuantity);
          item.uomUnitDataBonus = item.uom[3].uomUnit;
        }
      }
    }

    var selectDefaultUOM = function (item) {
      item.uom = self.model.uomArray.concat(item.uom);
      item.selectedUom = item.uom[0];
      if(item.goodReceiptNoteDetailDto!=null){
    	  item.goodReceiptNoteDetailDto.lastGrnUomTypeId;
    	  item.selectedUom = item.uom.find(function (val) {
    		  return val.uomTypeId ==item.goodReceiptNoteDetailDto.lastGrnUomTypeId;
      	 });
      }
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
      if(item.isThreeUOMApplicable == false){
        item.uomListdata = item.uom;
        item.uom = [];
        angular.forEach(item.uomListdata, function(data){
          if(data.uomType != 'Stock'){
            item.uom.push(data);
          }
        });
      }
    }

    var bonusUomInfoOnClick = function (batchItem, index) {
      self.model.bonusSelectedUOMList = [];
      self.model.bonusSelectedUOMUnit = [];
      self.model.bonusSelectedItemIndex = index;
      if (batchItem)
        var _boUom = batchItem.bonusUom.filter(function (uom) {
          if(batchItem.isThreeUOMApplicable == false){
            if (!!uom.uomTypeId && uom.uomTypeId != 2) {
              return uom;
            }
          }
          else{
            if (!!uom.uomTypeId) {
              return uom;
            }
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
          uomTypeId: item.uomTypeId,
          id : _boUom[index].id
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
              uomTypeId: modal.uomTypeId,
              id: modal.id
            });
          });
          $scope.calculateReceivedQtyBonus(self.model.itemDetails[self.model.bonusSelectedItemIndex].bonusUom, self.model.itemDetails[self.model.bonusSelectedItemIndex]);
          self.model.ClosePopup('Close', 'bouns_selectUOM_modal');
        }
      }
    }

    $scope.calculateReceivedQtyBonus = function(uom, item){
      if(item.isThreeUOMApplicable == false){
        if(item.bonusSelectedUom.uomTypeId == 1){
          item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ uom[0].conversion);
          item.receivedQtyBonusCalculation = (item.bonusQuantity * uom[0].conversion);
          item.uomUnitDataBonus = item.uomListdata[3].uomUnit;
        }
        else if(item.bonusSelectedUom.uomTypeId == 2){
        
        }
        else{
          item.receivedQtyBonusCon = (item.bonusQuantity);
          item.receivedQtyBonusCalculation = (item.bonusQuantity);
          item.uomUnitDataBonus = item.uomListdata[3].uomUnit;
        }
      }
      else{
        if(item.bonusSelectedUom.uomTypeId == 1){
          item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
          item.receivedQtyBonusCalculation = (item.bonusQuantity * uom[0].conversion * uom[1].conversion);
          item.uomUnitDataBonus = item.uom[3].uomUnit;
        }
        else if(item.bonusSelectedUom.uomTypeId == 2){
          item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ uom[1].conversion);
          item.receivedQtyBonusCalculation = (item.bonusQuantity * uom[1].conversion);
          item.uomUnitDataBonus = item.uom[3].uomUnit;
        }
        else{
          item.receivedQtyBonusCon = (item.bonusQuantity);
          item.receivedQtyBonusCalculation = (item.bonusQuantity);
          item.uomUnitDataBonus = item.uom[3].uomUnit;
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
          if(batchItem.isThreeUOMApplicable == false){
            if (!!uom.uomTypeId && uom.uomTypeId != 2) {
              return uom;
            }
          }
          else{
            if (!!uom.uomTypeId) {
              return uom;
            }
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
          uomTypeId: item.uomTypeId,
          id: _poUom[index].id
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
              uomTypeId: modal.uomTypeId,
              id: modal.id
            });
          });
          CalculateStock2(self.model.itemDetails[self.model.selectedItemIndex], self.model.itemDetails[self.model.selectedItemIndex].poUom, self.model.itemDetails[self.model.selectedItemIndex].selectedUom);
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
          self.model.isSelectedAllItem = true;
          return self.model.intendItemIds.push(x.itemId);
        });
      } else {
        self.model.intendItemIds = [];
        items.forEach(function (x) {
          x.isSelectedItem = false;
          x.isChecked = false;
          self.model.isSelectedAllItem = false;
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
        // indent.isChecked = false;
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
        self.model.isSelectedAllItem = true;
      } else {
        indent.isSelectedIndent = false;
        self.model.isSelectedAllItem = false;
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
          var prQuantity = Number(item.prQuantity);
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
        newItemsHash[itemId].poQuantity = total;
        
        if(newItemsHash[itemId]['poQuantity']>0){
        	newItemsHash[itemId]['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: newItemsHash[itemId]['poQuantity'] }];
        }else{
        	newItemsHash[itemId]['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
        }

        newItemsHash[itemId]['prList'] = [];


        self.model.itemDetails.push(newItemsHash[itemId]);
        newItemsHash[itemId].prMaxStoreQty = 0;
        if (self.model.poType.id == 1) {
          selectDefaultUOM(newItemsHash[itemId]);
          if(newItemsHash[itemId].goodReceiptNoteDetailDto!=undefined )
        	  {
        	  newItemsHash[itemId].POAmountMRP= newItemsHash[itemId].goodReceiptNoteDetailDto.lastMrp;
        	  newItemsHash[itemId].costOfPurchase= newItemsHash[itemId].goodReceiptNoteDetailDto.lastCOP;
        	  }
          
          newItemsHash[itemId].selectedUom = newItemsHash[itemId].uom.find(function (uom) {
            return newItemsHash[itemId].uomTypeId == uom.uomTypeId;
          });

          newItemsHash[itemId].bonusSelectedUom = newItemsHash[itemId].uom.find(function (uom) {
            return newItemsHash[itemId].uomTypeId == uom.uomTypeId;
          });

          self.model.CalculateStock1(newItemsHash[itemId], newItemsHash[itemId].uom, newItemsHash[itemId].selectedUom);
          discountCalc(newItemsHash[itemId]);
        } else if (self.model.poType.id == 2) {
          //   newItemsHash[itemId].selectedTax = self.model.taxList[0];
          newItemsHash[itemId].newMaxOrderQuantity = newItemsHash[itemId].maxOrderQuantity;
          newItemsHash[itemId].newMaxStoreQuantity = newItemsHash[itemId].maxStoreQuantity;
        }
        // if(self.model.prRequestTypeId == 3){
        //   newItemsHash[itemId].newQty = newItemsHash[itemId].pendingQuantity;
        // }
      }
      // console.log(self.model.itemDetails, "self.model.itemDetails");
    }

    var CalculateStock = function (item, uom, uomList, oldValue) {
    	item.stageQuantityList=[{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: 0 }];;
    	item.poApprovedQuantity=0;
      if (!!uomList) {
        if(!item.stockAvailable){
          item.stockAvailable = 0
        }
          if(item.id){
            var prQuantity = item.poQuantity1;
          }
          else{
            var prQuantity = item.totalPrQty1;
          }
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

          if(self.model.prRequestTypeId == 3){
            var oldValue1 = JSON.parse(oldValue);
          }
          var qtylease = 0;
          if(item.isThreeUOMApplicable == false){
            if(self.model.prRequestTypeId == 3){
              if(item.uomTypeId == 3){
                if(oldValue1.uomTypeId == 3){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(prQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                }else if(oldValue1.uomTypeId == 1){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(item.totalPrQty * (item.uomListdata[1].conversion * item.uomListdat[3].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (item.uomListdata[3].conversion));
                  }
                }
                else if(oldValue1.uomTypeId == 2){
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (item.uomListdata[3].conversion));
                  }
                }
              }
  
              if(item.uomTypeId == 2){
                if(oldValue1.uomTypeId == 3){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(prQuantity * item.uomListdata[3].conversion);
                  }
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (item.uomListdata[1].conversion * item.uomListdata[3].conversion));
                  }
                }else if(oldValue1.uomTypeId == 1){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(item.totalPrQty * (item.uomListdata[1].conversion * item.uomListdata[3].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                }
                else if(oldValue1.uomTypeId == 2){
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (item.uomListdata[1].conversion * item.uomListdata[3].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                }
              }
  
              if(item.uomTypeId == 1){
                if(oldValue1.uomTypeId == 3){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(prQuantity * (item.uomListdata[1].conversion * item.uomListdata[3].conversion));
                  }
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity * item.uomListdata[3].conversion);
                  }
                }else if(oldValue1.uomTypeId == 1){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(item.totalPrQty * (item.uomListdata[1].conversion * item.uomListdata[3].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity *  (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                }
                else if(oldValue1.uomTypeId == 2){
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity * item.uomListdata[3].conversion);
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
                  }
                }
              }
            }  

            if (uomList.uomDetailsData == "P") {
              if(self.model.prRequestTypeId == 3){
                item.poQuantity = qtylease;
                item.totalPrQty = qtylease;
              }
              else{
                item.poQuantity = 0;
              }
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.receivedQtyCon = (item.poQuantity +"*"+ item.uomListdata[1].conversion);
              item.receivedQtyCalculation = (item.poQuantity * item.uomListdata[1].conversion);
              item.uomUnitData = item.uomListdata[3].uomUnit;
            } else if (uomList.uomDetailsData == "S") {
              if(self.model.prRequestTypeId == 3){
                item.totalPrQty = qtylease;
                item.poQuantity = qtylease;
              }
              else{
                item.poQuantity = 0;
              }
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            } else if (uomList.uomDetailsData == "D") {
              if(self.model.prRequestTypeId == 3){
                item.totalPrQty = qtylease;
                item.poQuantity = qtylease;
              }
              else{
                item.poQuantity = 0;
              }
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[3].conversion));
              item.receivedQtyCon=(item.poQuantity);
              item.receivedQtyCalculation = (item.poQuantity);
              item.uomUnitData = item.uomListdata[3].uomUnit;
            }
          }
          else{
            if(self.model.prRequestTypeId == 3){
              if(item.uomTypeId == 3){
                if(oldValue1.uomTypeId == 3){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(prQuantity / (uom[1].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                  }
                }else if(oldValue1.uomTypeId == 1){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(item.totalPrQty * (uom[0].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (uom[2].conversion));
                  }
                }
                else if(oldValue1.uomTypeId == 2){
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (uom[2].conversion));
                  }
                }
              }
  
              if(item.uomTypeId == 2){
                if(oldValue1.uomTypeId == 3){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(prQuantity * uom[2].conversion);
                  }
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (uom[0].conversion * uom[2].conversion));
                  }
                }else if(oldValue1.uomTypeId == 1){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(item.totalPrQty * (uom[0].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
                  }
                }
                else if(oldValue1.uomTypeId == 2){
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity / (uom[0].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
                  }
                }
              }
  
              if(item.uomTypeId == 1){
                if(oldValue1.uomTypeId == 3){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(prQuantity * (uom[0].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity * uom[2].conversion);
                  }
                }else if(oldValue1.uomTypeId == 1){
                  if(uomList.uomTypeId == 2){
                    qtylease = parseInt(item.totalPrQty * (uom[0].conversion * uom[2].conversion));
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity *  (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                  }
                }
                else if(oldValue1.uomTypeId == 2){
                  if(uomList.uomTypeId == 1){
                    qtylease = parseInt(prQuantity * uom[2].conversion);
                  }
                  if(uomList.uomTypeId == 3){
                    qtylease = parseInt(prQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                  }
                }
              }
            }  

            if (uomList.uomDetailsData == "P") {
              if(self.model.prRequestTypeId == 3){
                item.poQuantity = qtylease;
                item.totalPrQty = qtylease;
              }
              else{
                item.poQuantity = 0;
              }
              item.newQty = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon = (item.poQuantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.poQuantity * uom[0].conversion * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            } else if (uomList.uomDetailsData == "S") {
              if(self.model.prRequestTypeId == 3){
                item.totalPrQty = qtylease;
                item.poQuantity = qtylease;
              }
              else{
                item.poQuantity = 0;
              }
              item.newQty = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon=(item.poQuantity +"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.poQuantity * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            } else if (uomList.uomDetailsData == "D") {
              if(self.model.prRequestTypeId == 3){
                item.totalPrQty = qtylease;
                item.poQuantity = qtylease;
              }
              else{
                item.poQuantity = 0;
              }
              item.newQty = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[2].conversion));
              item.receivedQtyCon=(item.poQuantity);
              item.receivedQtyCalculation = (item.poQuantity);
              item.uomUnitData = uom[2].uomUnit;
            }
          }
          item.poApprovedQuantity=item.poQuantity;
          $scope.calculateRoundoff1();
        }
      // } 
      else {
        item.newPRQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    var CalculateStock1 = function (item, uom, uomList) {
      if(item.isThreeUOMApplicable == false){
        if(item.uomTypeId==1)
        {
            item.totalPrQty=item.totalPrQty*(item.uomListdata[1].conversion*item.uomListdata[2].conversion*item.uomListdata[3].conversion);
        }
        if(item.uomTypeId==2)
        {
            item.totalPrQty=item.totalPrQty*(item.uomListdata[2].conversion*item.uomListdata[3].conversion);
        }
        if(item.uomTypeId==3)
        {
            item.totalPrQty=item.totalPrQty*(item.uomListdata[3].conversion);
        }
      }
      else{
        if(item.uomTypeId==1)
        {
            item.totalPrQty=item.totalPrQty*(item.uom[1].conversion*item.uom[2].conversion*item.uom[3].conversion);
        }
        if(item.uomTypeId==2)
        {
            item.totalPrQty=item.totalPrQty*(item.uom[2].conversion*item.uom[3].conversion);
        }
        if(item.uomTypeId==3)
        {
            item.totalPrQty=item.totalPrQty*(item.uom[3].conversion);
        }
      }
      item.poApprovedQuantity = item.totalPrQty;
      if(item.bonusQuantity == undefined || item.bonusQuantity == null){
        item.bonusQuantity = 0;
      }
        
      if (!!uomList) {
        if(!item.stockAvailable){
          item.stockAvailable = 0
        }
        // var prQuantity = item.stockAvailable1;
        var prQuantity = item.totalPrQty;
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

        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.poQuantity +"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.poQuantity * item.poUom[1].conversion);
            item.uomUnitData = item.uomListdata[3].uomUnit;
//            if(item.bonusUom[0].conversion == undefined || item.bonusUom[0].conversion == null || item.bonusUom[0].conversion == ''){
//            	item.receivedQtyBonusCon=(item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
//            }
//            else{
//            	item.receivedQtyBonusCon=(item.bonusQuantity +"*"+ item.bonusUom[0].conversion);
//            }
//            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
//            item.uomUnitDataBonus = item.uomListdata[3].uomUnit;
            item.poApprovedQuantity=parseInt(item.poApprovedQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.totalPrQty=parseInt(item.totalPrQty / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.poApprovedQuantity=parseInt(item.poApprovedQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.totalPrQty=parseInt(item.totalPrQty / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
            item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.poQuantity);
            item.receivedQtyCalculation = (item.poQuantity);
            item.uomUnitData = item.uomListdata[3].uomUnit;
//            item.receivedQtyBonusCon=(item.bonusQuantity);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity);
//            item.uomUnitDataBonus = item.uomListdata[3].uomUnit;
            item.poApprovedQuantity=parseInt(item.poApprovedQuantity / (item.uomListdata[3].conversion));
            item.totalPrQty=parseInt(item.totalPrQty / (item.uomListdata[3].conversion));
          }
        }
        else{
        	if(item.poUom.length != 0){
        		if(item.poUom[0].conversion == undefined || item.poUom[0].conversion == null){
                    item.poUomData = item.poUom;
                    item.poUom = [];
                    angular.forEach(item.poUomData, function (dataUom){
                      if(dataUom.conversion != undefined){
                        item.poUom.push(dataUom);
                      }
                    })
                  }
        	}
        	if(item.bonusUom.length != 0){
        		if(item.bonusUom[0].conversion == undefined || item.bonusUom[0].conversion == null){
                    item.bonusUomData = item.bonusUom;
                    item.bonusUom = [];
                    angular.forEach(item.bonusUomData, function (dataBonuUom){
                      if(dataBonuUom.conversion != undefined){
                        item.bonusUom.push(dataBonuUom);
                      }
                    })
                  }
        	}
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.stockAvailable = parseInt(item.stockAvailable1 / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.poQuantity +"*"+ item.poUom[0].conversion+"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.poQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
//            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
//            item.uomUnitDataBonus = uom[2].uomUnit;
            item.poApprovedQuantity=parseInt(item.poApprovedQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.totalPrQty=parseInt(item.totalPrQty / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.stockAvailable = parseInt(item.stockAvailable1 / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.poQuantity +"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.poQuantity * item.poUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
//            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
//            item.uomUnitDataBonus = uom[2].uomUnit;
            item.poApprovedQuantity=parseInt(item.poApprovedQuantity / (uom[1].conversion * uom[2].conversion));
            item.totalPrQty=parseInt(item.totalPrQty / (uom[1].conversion * uom[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.stockAvailable = parseInt(item.stockAvailable1 / (uom[2].conversion));
            item.receivedQtyCon=(item.poQuantity);
            item.receivedQtyCalculation = (item.poQuantity);
            item.uomUnitData = uom[2].uomUnit;
//            item.receivedQtyBonusCon=(item.bonusQuantity);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity);
//            item.uomUnitDataBonus = uom[2].uomUnit;
            item.poApprovedQuantity=parseInt(item.poApprovedQuantity / (uom[2].conversion));
            item.totalPrQty=parseInt(item.totalPrQty / (uom[2].conversion));
          }
        }
        if(item.bonusSelectedUom!=undefined && item.bonusSelectedUom!=null)
        	{
        	if(item.bonusSelectedUom.uomTypeId==1)
        		{
        		item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
                item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
        		}
        	else if(item.bonusSelectedUom.uomTypeId==2)
        		{
        		item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
                item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
        		}
        	
        	}
      } else {
        item.newPRQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    var CalculateStock2 = function (item, uom, uomList) {
      if (!!uomList) {
        if(!item.stockAvailable){
          item.stockAvailable = 0
        }
          if(item.id){
            var prQuantity = item.poQuantity1;
          }
          else{
            var prQuantity = item.totalPrQty1;
          }
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

          if(item.isThreeUOMApplicable == false){

            if (uomList.uomDetailsData == "P") {
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.receivedQtyCon = (item.poQuantity +"*"+ uom[0].conversion);
              item.receivedQtyCalculation = (item.poQuantity * uom[0].conversion);
              item.uomUnitData = item.uomListdata[3].uomUnit;
            } else if (uomList.uomDetailsData == "S") {
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            } else if (uomList.uomDetailsData == "D") {
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[3].conversion));
              item.receivedQtyCon=(item.poQuantity);
              item.receivedQtyCalculation = (item.poQuantity);
              item.uomUnitData = item.uomListdata[3].uomUnit;
            }
          }
          else{
            if (uomList.uomDetailsData == "P") {
              item.newQty = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon = (item.poQuantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.poQuantity * uom[0].conversion * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            } else if (uomList.uomDetailsData == "S") {
              item.newQty = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon=(item.poQuantity +"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.poQuantity * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            } else if (uomList.uomDetailsData == "D") {
              item.newQty = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[2].conversion));
              item.receivedQtyCon=(item.poQuantity);
              item.receivedQtyCalculation = (item.poQuantity);
              item.uomUnitData = uom[2].uomUnit;
            }
          }
          
          $scope.calculateRoundoff1();
        }
      // } 
      else {
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
            angular.forEach(purchaseRequestListResponse, function(item){
              angular.forEach(item.items, function(item1){
                item1.stockAvailable1 = item1.stockAvailable;
                item1.totalPrQty1 = item1.prQuantity;
                angular.forEach(self.model.taxList, function(t){
                  if(t.taxId == item1.taxId){
                    if(!item1.selectedTaxPercent){
                      item1.selectedTaxPercent = 0.00;
                    }
                  }
                });
                // if(item1.taxName == "Tax 0%" || item1.taxName == "0"){
                //   item1.selectedTaxPercent = 0.00;
                // }
                if(item1.taxName == undefined){
                  angular.forEach(self.model.taxList, function(taxdata){
                    if(taxdata.taxName == "Tax 0%" || taxdata.taxName == "0" || taxdata.taxName == "GST 0%"){
                      item1.selectedTax = taxdata;
                      item1.taxId=taxdata.taxId;
                      item1.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                    }
                  });
                }
              });
            });

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

        self.model.accessoryItems = self.model.items.map(function (x) {
          x.isSelected = true;
          return x;
        });

      } else {
        self.model.SelectedItemsIds = [];
        self.model.accessoryItems = [];
        self.model.pagination.data.forEach(function (item) {
          item.isSelected = false;
        });
      }
    }

    var SelectIndentAllItem = function (data) {
      if (self.model.isSelectedAllItem) {
        // data.items.forEach(function (list){
          self.model.intendItemIds = data.items.map(function (item) {
            item.isSelectedItem = true;
            item.isChecked = true;
            data.isChecked = true;
            return item.id;
          });
        // })
        
      } else {
        self.model.intendItemIds = [];
        // self.model.indents.forEach(function (item) {
          data.items.forEach(function (list){
            list.isSelectedItem = false;
            data.isChecked = false;
            list.isChecked = false;
          });
        // });
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
      
      // start code for resolved HIS-9490 PO for asset details - Accessories shows same items in both the line item
      // rejected reason resolved Second add button pre selects the accessories item added in the first line.
      if (!self.model.addItemFlag && self.model.items.length == 0 && self.model.pagination.data.length > 0) {
    	  if (self.model.pagination.data.every(isSelected)) {
    	        self.model.selectAllRows = true;
    	      } else {
    	        self.model.selectAllRows = false;
    	      }
        }
      // start code for resolved HIS-9490 PO for asset details - Accessories shows same items in both the line item
      // rejected reason resolved Second add button pre selects the accessories item added in the first line.
      
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
      $scope.calculateRoundoff1();
    }

    var SavePurchaseOrder = function () {
      $scope.saveBtnDisabled=true;
      if (ValidateItems()) {
        if (!self.model.poId) {
          CreatePurchaseOrder();
        } else {
          UpdatePurchaseOrder();
        }
      }
    }
    $scope.saveBtnDisabled=false;
    var CreatePurchaseOrder = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        $scope.saveBtnDisabled=true;
      var purchaseOrder = self.model;

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API;
      self.model.otherchargesData = [];
      self.model.otherChargesList.forEach(function(othercharData){
        var obj = {
          otherChargesId : '',
          amount: '',
          isFromPo:''
        }
        obj.otherChargesId = othercharData.otherChargesId;
        obj.amount = othercharData.amount;
        obj.isFromPo=true;
        self.model.otherchargesData.push(obj);
      });
      if($scope.roundOff == "" || $scope.roundOff == null || $scope.roundOff == undefined){
    	  $scope.roundOff = 0;
      }
      var data = {
        "storeId": purchaseOrder.store.id,
        "requestingStoreId": (purchaseOrder.fromStore.id!=undefined || purchaseOrder.fromStore.id!=null )? purchaseOrder.fromStore.id : purchaseOrder.store.id,
        "vendorId": purchaseOrder.vendorId,
        "assetTypeId": purchaseOrder.poType.id,
        "poTypeId": purchaseOrder.PRType,
        "dueDate": moment(purchaseOrder.dueDate).format('DD-MM-YYYY'),
        "totalAmount":  (!isNaN(purchaseOrder.totalAmount) && purchaseOrder.totalAmount != undefined && purchaseOrder.totalAmount != null && purchaseOrder.totalAmount != "") ? (purchaseOrder.totalAmount).toFixed(2) : 0,
        "discountTypeId": purchaseOrder.discountType,
        "discount": parseFloat(purchaseOrder.finalDiscount),
        "totalDiscount": (parseFloat(purchaseOrder.totalDiscount)).toFixed(2),
        "totalTaxAmount": (parseFloat(purchaseOrder.totalTax)).toFixed(2),
        "totalOtherAmount": (parseFloat(purchaseOrder.otherChargesAmount)).toFixed(2),
        "netAmount": parseFloat(purchaseOrder.totalNetAmount1) ? (parseFloat(purchaseOrder.totalNetAmount1)).toFixed(2) : 0,
        "roundOffAmount": (!isNaN($scope.roundOff) && $scope.roundOff!=undefined && $scope.roundOff!=null && $scope.roundOff!="") ? $scope.roundOff : 0,
        "isConsignment": purchaseOrder.isConsignment,
        "remark": purchaseOrder.purchaseOrderRemark,
        "paymentTerm": self.model.paymentMode,
        "statusId": setStatus('Saved'),
        "approvalStatusId": setStatus('Saved'),
        "otherCharges": self.model.otherchargesData,
        "items": [],
        "unitId": $scope.unitId,
        "userId": $scope.userId,
        "organizationId": $scope.organizationId
      };

      angular.forEach(purchaseOrder.itemDetails, function (item, key) {
        if (self.model.poType.id == 2) {
          var expDateMonth = item.warrantyDate.getMonth() + 1;
          var expDateYear = item.warrantyDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();

          if(expDateMonth < 10){
            expDateMonth = "0"+expDateMonth;
          }
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

        if(self.model.PRType == 14){
          var poItem = {
            "itemId": item.itemId,
            "poQuantity": parseInt(item.poQuantity),
            "bonusQuantity": parseInt(item.bonusQuantity),
            "manufactureId": item.manufacturerId,
            "cop": parseFloat(item.costOfPurchase),
            "mrp": parseFloat(item.POAmountMRP) ? parseFloat(item.POAmountMRP) : 0,
            "discount": parseFloat(item.discountPercentToSave!=null || item.discountPercentToSave!=undefined ? item.discountPercentToSave :0),
            "taxAmount":(!isNaN(item.taxAmount) && item.taxAmount != undefined && item.taxAmount != null && item.taxAmount != "") ? item.taxAmount.toFixed(2) : 0,
            "totalAmount": parseFloat(item.poAmount) ? parseFloat(item.poAmount).toFixed(2) : 0,
            "totalDiscount": parseFloat(item.discountAmount).toFixed(2),
            "totalTaxAmount": (!isNaN(item.taxAmount) && item.taxAmount != undefined && item.taxAmount != null && item.taxAmount != "") ? item.taxAmount.toFixed(2) : 0,
            "totalOtherAmount": (!isNaN(item.otherChargesAmount)) ? parseFloat(item.otherChargesAmount).toFixed(2):0.00,
            "netAmount": item.netAmount.toFixed(2) ? item.netAmount.toFixed(2) : 0,
            "statusId": setStatus('New'),
            "specification": item.specification,
            "remark": item.remark,
            "purchaseOrderStaged": item.stageQuantityList,
            "qrId": item.prIds,
            "taxId": item.selectedTax.taxId,
            "taxPercentage": parseFloat(item.selectedTaxPercent),
            "pendingQuantity": item.newMaxOrderQuantity,
          }
        }
        else{
          var poItem = {
            "itemId": item.itemId,
            "poQuantity": parseInt(item.poQuantity),
            "bonusQuantity": parseInt(item.bonusQuantity),
            "manufactureId": item.manufacturerId,
            "cop": parseFloat(item.costOfPurchase),
            "mrp": parseFloat(item.POAmountMRP) ? parseFloat(item.POAmountMRP) : 0,
            "discount": parseFloat(item.discountPercentToSave!=null || item.discountPercentToSave!=undefined ? item.discountPercentToSave :0),
            "taxAmount":(!isNaN(item.taxAmount) && item.taxAmount != undefined && item.taxAmount != null && item.taxAmount != "") ? item.taxAmount.toFixed(2) : 0,
            "totalAmount": parseFloat(item.poAmount) ? parseFloat(item.poAmount).toFixed(2) : 0,
            "totalDiscount": parseFloat(item.discountAmount).toFixed(2),
            "totalTaxAmount":(!isNaN(item.taxAmount) && item.taxAmount != undefined && item.taxAmount != null && item.taxAmount != "") ? item.taxAmount.toFixed(2) : 0,
            "totalOtherAmount": (!isNaN(item.otherChargesAmount)) ? parseFloat(item.otherChargesAmount).toFixed(2):0.00,
            "netAmount": (!isNaN(item.netAmount) && item.netAmount != undefined && item.netAmount != null && item.netAmount != "") ? item.netAmount.toFixed(2) : 0,
            "statusId": setStatus('New'),
            "specification": item.specification,
            "remark": item.remark,
            "purchaseOrderStaged": item.stageQuantityList,
            "prId": item.prIds,
            "taxId": item.selectedTax.taxId,
            "taxPercentage": parseFloat(item.selectedTaxPercent),
            "pendingQuantity": item.newMaxOrderQuantity,
          }
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
          poItem.warrantyDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
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
          poItem.poUomTypeId = item.selectedUom.uomTypeId;
          poItem.poUomUnitId = item.selectedUom.uomUnitId;
          poItem.bonusUomTypeId = item.bonusSelectedUom.uomTypeId;
          poItem.bonusUomUnitId = item.bonusSelectedUom.uomUnitId;
          poItem.warrantyDate = undefined;
          poItem.serviceProviderId = undefined;
          poItem.poAccessoriesMapper = undefined;
          /*      poItem.taxId = item.taxId;
               poItem.taxPercentage = parseFloat(item.taxPercent); */
        }

        data.items.push(poItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $scope.grnIdForDoccumentUpload=response.data.id;
          $rootScope.stopLoader();
          NavigateToPO();
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
          growl.error(err.data.message);
        });
    }

    var UpdatePurchaseOrder = function () {
    	$scope.saveBtnDisabled=true;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API + self.model.poId;
      self.model.otherchargesData = [];
      $scope.grnIdForDoccumentUpload = self.model.poId;
      self.model.otherChargesList.forEach(function(othercharData){
        var obj = {
          id : othercharData.id ? othercharData.id : undefined,
          otherChargesId : '',
          amount: '',
          isFromPo:''
        }
        if(othercharData.otherChargesId!=undefined && othercharData.otherChargesId!=null)
        {
        obj.otherChargesId = othercharData.otherChargesId;
        obj.amount = othercharData.amount;
        obj.isFromPo=true;
        self.model.otherchargesData.push(obj);
        }
      });
      var data = {
        "vendorId": self.model.vendorId,
        "totalAmount": (self.model.totalAmount).toFixed(2),
        "discountTypeId": parseInt(self.model.discountType),
        "discount": parseFloat(self.model.finalDiscount),
        "totalDiscount": (parseFloat(self.model.totalDiscount)).toFixed(2),
        "totalTaxAmount": (parseFloat(self.model.totalTax)).toFixed(2),
        "totalOtherAmount": (parseFloat(self.model.totalOtherCharges)).toFixed(2),
        "netAmount": parseFloat(self.model.totalNetAmount1) ? (parseFloat(self.model.totalNetAmount1)).toFixed(2) : 0,
        "roundOffAmount": (!isNaN($scope.roundOff) && $scope.roundOff!=undefined && $scope.roundOff!=null && $scope.roundOff!="") ? $scope.roundOff : 0,
        "isConsignment": self.model.isConsignment,
        "remark": self.model.purchaseOrderRemark,
        "poTypeId": self.model.poType.id,
        "dueDate": moment( self.model.dueDate).format('DD-MM-YYYY'),
        "paymentTerm": self.model.paymentMode,
        "otherCharges": self.model.otherchargesData,
        "items": [],
        "unitId": $scope.unitId,
        "userId": $scope.userId,
        "organizationId": $scope.organizationId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        if (self.model.poType.id == 2) {
          var expDateMonth = item.warrantyDate.getMonth() + 1;
          var expDateYear = item.warrantyDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();

          if(expDateMonth < 10){
            expDateMonth = "0"+expDateMonth;
          }
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

        if(item.discountPercentToSave == undefined || item.discountPercentToSave == null){
          item.discountPercentToSave = item.discountPercent;
        }

        var poItem = {
          "id": item.id,
          "itemId": item.itemId,
          "poApprovedQuantity": parseInt(item.poApprovedQuantity),
          "poQuantity": parseInt(item.poQuantity),
          "bonusQuantity": parseInt(item.bonusQuantity),
          // "manufactureId": item.manufacturerId,
          "cop": parseFloat(item.costOfPurchase),
          "mrp": parseFloat(item.POAmountMRP) ? parseFloat(item.POAmountMRP) : 0,
          "discount": parseFloat(item.discountPercentToSave!=null || item.discountPercentToSave!=undefined ? item.discountPercentToSave :0),
          // "taxId": item.taxId,
          // "taxPercentage": item.taxPercent,
          "taxId": item.selectedTax.taxId,
          "taxPercentage": parseFloat(item.selectedTaxPercent),
          "taxAmount": parseFloat(item.taxAmount).toFixed(2),
          "totalAmount": parseFloat(item.poAmount) ? parseFloat(item.poAmount).toFixed(2) : 0,
          "totalDiscount": parseFloat(item.discountAmount).toFixed(2),
          "totalTaxAmount": parseFloat(item.taxAmount).toFixed(2),
          //"totalOtherAmount": parseFloat(item.otherChargesTotal),
          "totalOtherAmount": (!isNaN(item.otherChargesTotal)) ? parseFloat(item.otherChargesTotal).toFixed(2):0.00,
          "netAmount": parseFloat(item.netAmount) ? parseFloat(item.netAmount).toFixed(2) : 0,
          "specification": item.specification,
          "remark": item.remark,
          "purchaseOrderStaged": stagedQtyList,
          "prId": item.prIds,
          "pendingQuantity": item.newMaxOrderQuantity,
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
          poItem.poUomTypeId = item.selectedUom.uomTypeId;
          poItem.poUomUnitId = item.selectedUom.uomUnitId;
          poItem.bonusUomTypeId = item.bonusSelectedUom ? item.bonusSelectedUom.uomTypeId : null;
          poItem.bonusUomUnitId = item.bonusSelectedUom ? item.bonusSelectedUom.uomUnitId : null;
          poItem.manufactureId = item.manufacturerId,
            poItem.poAccessoriesMapper = undefined;
          poItem.warrantyDate = undefined;
          poItem.serviceProviderId = undefined;
          // poItem.taxId = item.taxId;
          // poItem.taxPercentage = parseFloat(item.taxPercent);
        }

        data.items.push(poItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToPO();
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
          growl.error(err.data.message);
        });
    }

    var updateDiscountForEachItems = function (percentageDiscount, items, discountType) {
      var itemDiscount = (percentageDiscount / self.model.totalAmount) * 100;

      angular.forEach(items, function (item, key) {
        if (!!itemDiscount) {
          item.discountPercent = itemDiscount;
          item.discountPercentToSave=item.discountPercent;
          item.discountPercent = parseFloat(item.discountPercent).toFixed(2);
          item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
          item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
        } else if (!itemDiscount) {
          item.discountPercent = itemDiscount;
          item.discountPercentToSave=item.discountPercent;
          item.discountAmount = (item.poAmount) * (item.discountPercent / 100);
        }
      });
    }

    var ViewHistory = function (item) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.COP_HISTORY + item.itemId + "/" + self.model.vendorId + "/last/"+self.model.poType.id;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastOrderDetails = response.data;
          angular.forEach(self.model.lastOrderDetails,function(item,key){
              item.discount=item.discount!=undefined && item.discount!=null ? item.discount.toFixed(2) : 0;
              item.netamount=item.netamount!=undefined && item.netamount!=null ? item.netamount.toFixed(2) : 0;
              item.otheramount=item.otheramount!=undefined && item.otheramount!=null ? item.otheramount.toFixed(2): 0;
              item.taxamount=item.taxamount!=undefined && item.taxamount!=null ? item.taxamount.toFixed(2) : 0 ;
              item.totalamount=item.totalamount!=undefined && item.totalamount!=null ? item.totalamount.toFixed(2) : 0;
              item.totaldiscount=item.totaldiscount!=undefined && item.totaldiscount!=null ? item.totaldiscount.toFixed(2) : 0;
              });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var NavigateToPO = function () {
      if($scope.heading == 'Sumbmit'){
        $state.go('POSubmit');
      }
      else if($scope.heading == 'Final Approval') {
        $state.go('POFinalApproval');
      }
      else if($scope.heading == 'Approval'){
        $state.go('POApproval');
      }
      else{
        $state.go('POCreation');
      }
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
      } else if (item.stagedDate == undefined) {
        // item.isQuantityValid = true;
        isValid = false;
        growl.error('Please Select Date');
      } else {
        item.quantity = parseInt(item.quantity);
        isValid = true;
        // isValid = ValidateMaxOrderQuantity(item);
      }

      // self.model.itemDetails.forEach(function(data){
        if(item.quantity > self.model.itemDetails[self.model.selectedIndex].newMaxOrderQuantity){
          growl.error("Quantity should be less than pending quantity");
          item.quantity = "";
        }
        if(self.model.PRType == 14){
          if(item.quantity > self.model.itemDetails[self.model.selectedIndex].newMaxOrderQuantity){
            growl.error("Quantity should be less than QR quantity");
            item.quantity = "";
          }
          // start code for resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
          let totalStageQuantity = self.model.stageQuantityList.map(obj => obj.quantity).reduce((a, b) => { return a + b });
          if(totalStageQuantity > self.model.itemDetails[self.model.selectedIndex].newMaxOrderQuantity){
              growl.error("Sum of Quantity should be less than QR quantity");
              isValid = false;
              item.quantity = "";
            }
          // end code for resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
        }
        if(self.model.PRType == 3){
          if(item.quantity > self.model.itemDetails[self.model.selectedIndex].poQuantity){
            growl.error("Quantity should be less than PR quantity");
            item.quantity = "";
          }
          // start code for resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
          let totalStageQuantity = self.model.stageQuantityList.map(obj => obj.quantity).reduce((a, b) => { return a + b });
          if(totalStageQuantity > self.model.itemDetails[self.model.selectedIndex].poQuantity){
              growl.error("Sum of Quantity should be less than PR quantity");
              isValid = false;
              item.quantity = "";
            }
          // end code for resolved HIS-9659 Fixed asset PO VQR /Against PR - Staged quantity allows more than approved quantity
          
        }
      // });

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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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
          "vqrNumber" : $scope.vqlNo,
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
        var URI = CONSTANTS.GLOBAL.AGAINST_QR_DETAILS  + "/"+ $scope.organizationId + "/"+ $scope.unitId +"/"+self.model.vendorId;

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
            angular.forEach(purchaseRequestListResponse, function(item){
              angular.forEach(item.items, function(item1){
                item1.stockAvailable1 = item1.stockAvailable;
                if(item1.taxName = "Tax 0%"){
                  item1.selectedTaxPercent = 0.00;
                }
                if(item1.taxName == undefined){
                  angular.forEach(self.model.taxList, function(taxdata){
                    if(taxdata.taxName == "Tax 0%"){
                      item1.selectedTax = taxdata;
                      item1.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                    }
                  });
                }
              })
            })

            updateQRItemDetails(purchaseRequestListResponse);
            $scope.calculateRoundoff1();

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
        newItemsHash[itemId]['poApprovedQuantity'] = 0;
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

        newItemsHash[itemId].poQuantity = newItemsHash[itemId].qrQuantity;
        newItemsHash[itemId].costOfPurchase = newItemsHash[itemId].vendorCop;
        
        if(newItemsHash[itemId]['poQuantity']>0){
        	newItemsHash[itemId]['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: newItemsHash[itemId]['poQuantity'] }];
        }else{
        	newItemsHash[itemId]['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];
        }
        
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

    var RemoveViewAddedItems = function (item) {
        let newArrayForCAI = [];
        
          self.model.itemDetails.forEach(function (obj) {
          	if(obj.itemId == self.model.ViewAddedItemsIdForRemove){
          		obj.childAccessoryItems.map(function (childAccessoryItem) {
                        if (childAccessoryItem.itemId !== item.itemId) {
                        	newArrayForCAI.push(childAccessoryItem);
                        }else{
                        	let index = (obj.poAccessoriesMapper.itemIds).indexOf(childAccessoryItem.itemId);
                            if (index > -1) {
                            	(obj.poAccessoriesMapper.itemIds).splice(index, 1);
                            }
                        }
                                                
                      });
          		obj.childAccessoryItems = newArrayForCAI;
          		self.model.addedAccessoryItems=obj.childAccessoryItems;
          	}
            });
          
         /* self.model.addedAccessoryItems.map(function (childAccessoryItem) {
              if (childAccessoryItem.itemId !== item.itemId) {
              	newArrayForCAI.push(childAccessoryItem);
              }                                      
            });
          self.model.addedAccessoryItems=newArrayForCAI;*/
          
                    
        /*self.model.itemDetails.forEach(function (obj) {
        	if(obj.itemId == self.model.ViewAddedItemsIdForRemove){
            	  self.model.addedAccessoryItems.map(function (accessoryItem) {
                      if (accessoryItem.itemId !== item.itemId) {
                        newArray.push(obj);
                      }
                    });
            	  self.model.addedAccessoryItems = newArray;
                  obj.childAccessoryItems=newArray;
        	}
          });*/       
      }
    //Getting  State List
    function PopulateStates(country, selectedvalue) {
      if (country.countryId == undefined) {
        country.countryId = 0;
      }
      var vendorMaster = self.model;

      var URI = CONSTANTS.GLOBAL.STATE_API +$scope.organizationId + '/'+ country.countryId;

      vendorMaster.stateName = vendorMaster.States[0];
      vendorMaster.cityName = vendorMaster.Cities[0];
      vendorMaster.areaName = vendorMaster.Areas[0];

      vendorMaster.States.splice(1);
      vendorMaster.Cities.splice(1);
      vendorMaster.Areas.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        vendorMaster.States = vendorMaster.States.concat(response.data);
        $rootScope.stopLoader();

        if (selectedvalue) {
          var State = vendorMaster.States.find(function(state) {
            return country.stateId == state.stateId;
          });
          self.model.stateName = State;
        }
      });
    }

    //Getting City List
    function PopulateCities(state, selectedvalue) {
      if (state.stateId == undefined) {
        state.stateId = 0;
      }
      var vendorMaster = self.model;

      var URI = CONSTANTS.GLOBAL.CITY_API +$scope.organizationId + '/' + state.stateId;

      vendorMaster.cityName = vendorMaster.Cities[0];
      vendorMaster.areaName = vendorMaster.Areas[0];

      vendorMaster.Cities.splice(1);
      vendorMaster.Areas.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        vendorMaster.Cities = vendorMaster.Cities.concat(response.data);
        $rootScope.stopLoader();

        if (selectedvalue) {
          var City = vendorMaster.Cities.find(function(city) {
            return state.cityId == city.cityId;
          });
          self.model.cityName = City;
        }
      });
    }

    //Getting City List
    function PopulateAreas(city, selectedvalue) {
      if (city.cityId == undefined) {
        city.cityId = 0;
      }
      var vendorMaster = self.model;

      var URI = CONSTANTS.GLOBAL.AREA_API +$scope.organizationId + '/'  + city.cityId;

      vendorMaster.areaName = vendorMaster.Areas[0];

      vendorMaster.Areas.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        vendorMaster.Areas = vendorMaster.Areas.concat(response.data);		
        $rootScope.stopLoader();

        if (selectedvalue) {
          var Area = vendorMaster.Areas.find(function(area) {
            return city.areaId == area.areaId;
          });
          self.model.areaName = Area;
        }
      });
    }
    function SaveVendorMaster() {
        if (ValidateVendor()) {
          if (!self.model.id) {
          	createVendorMaster();
          } else {
            //updateVendorMaster();
          }
        }
      }
    var ValidateVendor = function() {
        var isValid = true;
        // if (!self.model.manufactureName.id) {
        //   isValid = false;
        //   self.model.isManufacturerValid = true;
        // }
        if (!self.model.vendorCode) {
          isValid = false;
          self.model.isvendorCodeValid = true;
        }
        if (!self.model.vendorName) {
          isValid = false;
          self.model.isvendorNameValid = true;
        }
        if (!self.model.contactPersonName) {
          isValid = false;
          self.model.iscontactPersonNameValid = true;
        }
       
       
        if (!self.model.address) {
          isValid = false;
          self.model.isaddressValid = true;
        }
        if (!self.model.countryName.countryId) {
          isValid = false;
          self.model.iscountryNameValid = true;
        }
       
       
        if (!self.model.phoneNumber) {
          isValid = false;
          self.model.isphoneNumberValid = true;
        }
        
        if (!self.model.modeOfPayment.paymentModeId) {
          isValid = false;
          self.model.ismodofPaymentValid = true;
        }
        if (!self.model.paymentTerms) {
          isValid = false;
          self.model.ispaymentTermsValid = true;
        }
        return isValid;
      };
      //Method to add the VendorMaster
      function createVendorMaster() {
        if (
          ValidateCode() &&
          ValidateEmail() &&
          ValidateVendorName() &&
          ValidateGst() &&
          ValidateLicNo()&&
          checkPhoneNumDigit()
        ) {
          var URI =
            CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API;
          var data = {
            code: self.model.vendorCode,
            name: self.model.vendorName,
            address: self.model.address,
            countryId: self.model.countryName.countryId,
            stateId: self.model.stateName.stateId,
            cityId: self.model.cityName.cityId,
            areaId: self.model.areaName.areaId,
            zipcode: self.model.zipCode,
            contactPersonName: self.model.contactPersonName,
            drugLicenseNo: self.model.drugLicenseNo,
            // manufacturerId: self.model.manufactureName.id,
            manufacturerIds: [],
            paymentModeId: self.model.modeOfPayment.paymentModeId,
            gstNo: self.model.gstNo,
            phoneNumber: self.model.phoneNumber,
            emailId: self.model.emailAddress,
            paymentTerm: self.model.paymentTerms,
            isLocalPurchase: self.model.isLocalPurchase,
            status: true,
            unitId:$scope.unitId,
            organizationId:$scope.organizationId,
            userId:$scope.userId
          };
          angular.forEach($scope.selectedManufacturesList, function(dataManu){
            var obj = {
              id: ''
            }
            obj.id = dataManu.id;
            data.manufacturerIds.push(obj);
          });
          GenericService.serviceAction("POST", URI, data).then(
            function(response) {
            	 self.model.vendorId = response.data.id;
              growl.success(response.data.message);
              $("#addShopModel").hide();
              $('.modal-backdrop').remove();
            },
            function(err) {
              $rootScope.stopLoader();
              growl.error("Something Went Wrong");
            }
          );
        }
      }
      var ValidateEmail = function() {
    	     
          return true;
       };
       var ValidateCode = function() {
    	      var isValid = true;

    	      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.vendorCode)) {
    	        growl.error("Code can only be alpha numeric with single hypen.");
    	        return;
    	      }
    	      if (self.model.zipCode < 6) {
    	        growl.error("Enter Valid Zip code.");
    	        return;
    	      }
    	      if(self.model.zipCode < 6){
    	        growl.error('Enter Valid Zip code.');
    	        return;
    	       }
    	      return isValid;
    	    };
    	    var ValidateVendorName = function() {
    	        var isValid = true;
    	        if (/[^ a-z A-Z \.]/.test(self.model.vendorName)) {
    	          growl.error("Invalid Vendor Name");
    	          isValid = false;
    	        }
    	        if (/[^ a-z A-Z \.]/.test(self.model.contactPersonName)) {
    	          growl.error("Invalid Contact Person Name");
    	          isValid = false;
    	        }
    	        return isValid;
    	      };
    	      var ValidateGst = function() {
    	          var isValid = true;
    	          return isValid;
    	        };

    	        var ValidateLicNo = function() {
    	          var isValid = true;
    	          return isValid;
    	        };
    	        var checkPhoneNumDigit = function(){
    	            self.model.mobileDigit = 10;
    	             if (self.model.mobileDigit == self.model.phoneNumber.length && /^\+?\d+$/ .test(self.model.phoneNumber)) {
    	               return true;
    	             }
    	             else{
    	               growl.error("Enter valid phone no");
    	               return false;
    	             }
    	            
    	           }
    	        
    	       var  isShop1= function()
    	        {
    	        	
    	        	if($scope.isShop == true)
    	        		{
    	        		  $("#addShopModel").show(); 
    	        		}
    	        	else
    	        		{
    	        		  $("#addShopModel").hide();
    	                  $('.modal-backdrop').remove();
    	        		}
    	        	
    	        }
    	       var  isShop1Hide= function()
   	        {
    	    	      $scope.isShop =false;
   	        		  $("#addShopModel").hide();
   	                  $('.modal-backdrop').remove();
   	         	
   	        }
    	       
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addItemPO", {
      url: "/addItemPO",
      templateUrl: 'views/procurement/quotation/add-item-PO-creation.html',
      controller: "addNewPO.Controller",
      controllerAs: "vm",
      title: 'Purchase Order Creation'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("addNewPO.Controller", addNewPOController);
})();