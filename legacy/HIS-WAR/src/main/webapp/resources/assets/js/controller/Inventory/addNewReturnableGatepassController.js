(function () {
    "use strict";

    function addNewReturnableGatepassController($scope, $filter, $rootScope, commonDetailService,PaginationService, CurrencyService, $http, $state, CONSTANTS, GenericService, growl,$cookies) {
        var self = this;

        //Default Constructor
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
                stores: [{

                    "store": "Select Store"
                }],
                assetTypeId: 1,
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false
                },
                fromDate: new Date(),
                toDate: new Date(),
                SelectedItemsIds: [],
                selectAllRows: false,
                SelectedItemIds: [],
                NewItemList: [],
                itemList: [],
                bonusBatchExpiryData:[],
                bonusSelectedItemBatchExpiryIndex:0,
                
                againstList: [{
                    requestedType: "Select Against"
                }]
            };
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.nameNotFound = nameNotFound;
            self.model.AddVendorName = AddVendorName;
            self.model.AddVendorNameGRN = AddVendorNameGRN;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.vendorNames = [];
            // self.model.againstId;
            self.model.addItemModal = addItemModal;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isSelected = isSelected;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.searchByNearExpiry = searchByNearExpiry;
            self.model.validateSave = validateSave;
            self.model.SaveReturnableGatepass = SaveReturnableGatepass;
            self.model.SaveNewGatepass = SaveNewGatepass;
            self.model.removeRowData = removeRowData;
            self.model.removeRowDataPopup = removeRowDataPopup;
            self.model.searchNewGRN = searchNewGRN;
            self.model.GetReturnableGatepassDetails = GetReturnableGatepassDetails;
            self.model.SelectAllGRNItems = SelectAllGRNItems;
            self.model.SelectGRNItem = SelectGRNItem;
            self.model.getAllGRNItemDetails = getAllGRNItemDetails;
            self.model.UpdateGatepass = UpdateGatepass;
            self.model.confirmClear = confirmClear;
            self.model.itemRemovePopup = itemRemovePopup;
            self.model.ValidateQuantity = ValidateQuantity;

            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ChangeStore = ChangeStore;
            self.model.ConfirmPopup = ConfirmPopup;

            self.model.SetOldValueAgainst = SetOldValueAgainst;
            self.model.ChangeAgainst = ChangeAgainst;
            self.model.ConfirmAgainstPopup = ConfirmAgainstPopup;
            self.model.againstPopupClose = againstPopupClose;
            self.model.accessoryModal = accessoryModal;
            self.model.SaveAccessoryDetails = SaveAccessoryDetails;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.SelectAllItemsChecked = SelectAllItemsChecked;
            self.model.SelectAllItemsExpired = SelectAllItemsExpired;
            self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;
            self.model.bonusBatchExpiryModalOnClick=bonusBatchExpiryModalOnClick;
            
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


        var PopulateDropdown = function () {
            var returnable = self.model;
           // returnable.store = returnable.stores[0];
            returnable.requestType = returnable.againstList[0];
            var agList = [{
                requestTypeId: 7, requestedType: "Against Expiry"
            },{
            	 requestTypeId: 17, requestedType: "Against GRN"
            },{
            requestTypeId: 6, requestedType: "Against Non-Moving"
         },{
                requestTypeId: 15, requestedType: "Rejected Against GRN"
            }]
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    returnable.stores = returnable.stores.concat(response.data.store);
                    returnable.againstList = returnable.againstList.concat(agList);
                    for(var i=0;i<returnable.stores.length;i++)
                    {
                        if(returnable.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
                        {
                            returnable.store = returnable.stores[i];
                        }
                    }
                    $rootScope.stopLoader();
                    var returnableId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!returnableId) {
                        commonDetailService.setDataId(null);
                        GetReturnableGatepassDetails(returnableId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetReturnableGatepassDetails = function (returnableId) {
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.DETAIL_API +  $scope.unitId + '/'+$scope.organizationId + '/' + returnableId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.returnableId = response.data.id;
                    $scope.requestTypeToCheck=response.data.requestedType;
                    self.model.remark = response.data.remark;
                    self.model.store = self.model.stores.find(function (returnable) {
                        return response.data.storeId == returnable.id;
                    });
                    self.model.requestType = self.model.againstList.find(function (against) {
                        return response.data.requestTypeId == against.requestTypeId;
                    })
                    //self.model.againstId = response.data.againstId.toString();
                    self.model.assetTypeId = response.data.assetTypeId;
                    self.model.referenceNo = response.data.referenceNo;
                    self.model.totalAmount = response.data.totalAmount;
                    self.model.totalTaxAmount = response.data.totalTaxAmount;
                    self.model.netAmount = response.data.netAmount;
                    self.model.vendorName = response.data.vendor;
                    self.model.vendorId = response.data.vendorId;
                    self.model.vendorIdGRN = response.data.vendorId;
                    // self.model.againstId = response.data.requestTypeId;
                    self.model.itemList = [];
                    self.model.itemList = response.data.items;
                    angular.forEach(self.model.itemList, function(value){
                    	if(value.approvedQuantity==undefined || value.approvedQuantity==null || value.approvedQuantity==0)
                    		{
                    		value.approvedQuantity=value.approvedReturnedQty!=undefined && value.approvedReturnedQty!=null ? value.approvedReturnedQty:0
                    		}
                    	if($scope.requestTypeToCheck=="Rejected Against GRN")
                    		{
                    	value.availableQuantity=(value.totalGRNQuantity-(value.totalGRNQuantity-(value.phyRejectQuantity!=undefined && value.phyRejectQuantity!=null ? value.phyRejectQuantity :0 )))-(value.approvedReturnedQty!=undefined && value.approvedReturnedQty!=null ? value.approvedReturnedQty :0);
                    		}
                        if(value.uomList!=undefined && value.uomList!=null)
                    	{
                            value.uomList.find(function(d){
                                if(d.uomTypeId == value.uomTypeId){
                                    value.uomType = d.uomDetails;
                                }
                                if(value.bonusUomTypeId == d.uomTypeId){
                                    value.bonusUom = d.uomDetails;
                                }
                            });
                        }
                        value.grnBonusBatchExpiryData=value.returnableBonusBatchExpiry;
                        value.grnBonusBatchExpiry = value.grnBonusBatchExpiryData;
                    	var bonusReturnQuantity=0;
                        var bonusReturnedQuantity=0;
                        angular.forEach(value.grnBonusBatchExpiryData, function(bonus){
                            bonus.bonusUom = value.bonusUom;
                            bonusReturnQuantity=bonusReturnQuantity+parseInt(bonus.returnQuantity);
                            bonusReturnedQuantity=bonusReturnedQuantity+parseInt(bonus.returnedQuantity);
                            bonus.bonusQuantityLease = bonus.bonusQuantity;
                            if(value.bonusUomTypeId == 1){
                                bonus.bonusQuantity = (bonus.bonusQuantity / (value.uomList[0].conversion * value.uomList[1].conversion * value.uomList[2].conversion)); 
                            }
                            else if(value.bonusUomTypeId == 2){
                                bonus.bonusQuantity = (bonus.bonusQuantity / (value.uomList[1].conversion * value.uomList[2].conversion));
                            }
                            else{
                                bonus.bonusQuantity = (bonus.bonusQuantity / (value.uomList[2].conversion));
                            }
                        })
                        value.bonusReturnedQuantity=bonusReturnedQuantity!=undefined && bonusReturnedQuantity!=null ? bonusReturnedQuantity:0;
                        value.bonusReturnQuantity=bonusReturnQuantity!=undefined && bonusReturnQuantity!=null ? bonusReturnQuantity:0;
                        if(value.returnableBonusBatchExpiry.length==0)
                        {
                            if(value.bonusApprovedQuantity > 0 && self.model.requestType.requestedType == 'Against GRN')
                            {
                                var date = Date.parse(value.expiry+"GMT");
                                var obj={
                                    returnableBonusBatchExpiryId:null,
                                    batchId: value.batchId,
                                    batchNo: value.batchNo,
                                    expiryDate: date,
                                    bonusQuantity: value.bonusApprovedQuantity,
                                    returnedQuantity: "",
                                    returnQuantity: ""
                                }
                                value.grnBonusBatchExpiryData.push(obj);
                            }
                        }
                      
                        if(value.returnQuantity == undefined || value.returnQuantity == null || value.returnQuantity == ""){
                            value.returnQuantity = 0;
                        }
                        if(value.isThreeUOMApplicable == false){
                            if(value.uomTypeId ==1){
                                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[0].conversion);
                                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[0].conversion);
                            }
                            else if(value.uomTypeId == 2){
                                
                            }
                            else{
                                value.receivedQtyCon = (value.returnQuantity);
                                value.receivedQtyCalculation = (value.returnQuantity);
                            }
                        }
                        else{
                            if(value.uomTypeId ==1){
                                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[0].conversion +"*"+ value.uomList[1].conversion);
                                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[0].conversion * value.uomList[1].conversion);
                            }
                            else if(value.uomTypeId == 2){
                                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[1].conversion);
                                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[1].conversion);
                            }
                            else{
                                value.receivedQtyCon = (value.returnQuantity);
                                value.receivedQtyCalculation = (value.returnQuantity);
                            }
                        }
                        value.discount=value.actualDiscountPercentage;
                        value.discountAmountCal = ((value.discount * value.totalAmount)/100);
                        value.taxAmountCal = (((value.totalAmount - value.discountAmountCal) * (value.taxPercentage)/100));
                        value.netAmountCal = ((value.totalAmount - value.discountAmountCal) + (value.taxAmountCal));
                    })
                    copConversion(self.model.itemList);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var copConversion = function (item) {
            angular.forEach(item, function(data){
                data.cop = commonDetailService.calculateRate(data.uomList, data.uomTypeId, data.cop);
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
            self.model.vendorIdGRN = vendor.id;
        }

        function AddVendorNameGRN(vendor) {
            self.model.vendorNameGRN = vendor.vendorName;
            self.model.vendorIdGRN = vendor.id;
        }
        var validatePopup = function () {
            var isValid = true;
            if (!self.model.store.id) {
                growl.error("Please select valid store");
                isValid = false;
            }
            else if (!self.model.vendorId) {
                growl.error("Please Enter Valid Vendor");
                isValid = false;
            }
            else if (!self.model.assetTypeId) {
                growl.error("Please Enter Valid Asset Type");
                isValid = false;
            }
            else if (!self.model.requestType) {
                isValid = false;
                growl.error("Please Select Against");
            }

            return isValid;
        }
        
        var addItemModal = function () {
        	$scope.okBtnDisabled = false;
            self.model.pagination.data=[];
            self.model.NewItemList = [];
         //   self.model.itemList=[];
            self.model.itemnew=[];
            allSelectedGrnList=[];
            self.model.selectAllRows = false;
            if (validatePopup()) {
                if (self.model.requestType.requestTypeId == 15 || self.model.requestType.requestTypeId == 17) {
                    $("#getGRNModal").modal()
                    self.model.NewItemList = [];
                    self.model.fromDate = new Date();
                    self.model.toDate = new Date();
                }
                else if (self.model.requestType.requestTypeId == 6) {
                    $("#additemlist").modal()
                    GetAssetCategoryList();
                    self.model.NewItemList = [];
                    self.model.isItemChecked = false;

                }
                else if (self.model.requestType.requestTypeId == 7) {
                    $("#addItemExpiryWise").modal();
                    self.model.NewItemList = [];
                    self.model.fromDate = new Date();
                    self.model.toDate = new Date();
                    self.model.isItemChecked = false;
                }
            }
        }

        var GetAssetCategoryList = function () {
            var returnable = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetTypeId+ '/'+$scope.unitId+ '/'+$scope.organizationId;


            returnable.assetCategory = returnable.assetCategories[0];
            returnable.productCategory = returnable.productCategories[0];

            returnable.assetCategories.splice(1);
            returnable.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    returnable.assetCategories = returnable.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var returnable = self.model;

            //TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    returnable.productCategories = returnable.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function searchNewItem() {
            if(self.model.search.itemName){
                if(self.model.search.itemName!=undefined  &&  (self.model.search.itemName.replace(/\s/g, "").length) == 2 ){
                    var URI = CONSTANTS.GLOBAL.BATCH_API_STORE_WISE;
                    const data = {
                        "assetTypeId": self.model.assetTypeId,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId
                        
                    }

                    self.model.NewItemList = [];
                    self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.NewItemList = response.data;
                        angular.forEach(self.model.NewItemList, function(value){
                            if(value.uomTypeId ==1){
                            	value.cop = (value.cop *( value.uomResponses[0].conversion * value.uomResponses[1].conversion));
                            }
                            else if(value.uomTypeId == 2){
                            	value.cop = (value.cop *(value.uomResponses[1].conversion));
                            }
                            value.uomResponses.find(function(d){
                                if(d.uomTypeId == value.uomTypeId){
                                    value.uomType = d.uomDetails;
                                }
                            });
                            if(value.returnQuantity == undefined || value.returnQuantity == null || value.returnQuantity == ""){
                                value.returnQuantity = 0;
                            }
                            if(value.isThreeUOMApplicable == false){
                                if(value.uomTypeId ==1){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[0].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[0].conversion);
                                }
                                else if(value.uomTypeId == 2){
                                    
                                }
                                else{
                                    value.receivedQtyCon = (value.returnQuantity);
                                    value.receivedQtyCalculation = (value.returnQuantity);
                                }
                            }
                            else{
                                if(value.uomTypeId ==1){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[0].conversion +"*"+ value.uomResponses[1].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[0].conversion * value.uomResponses[1].conversion);
                                }
                                else if(value.uomTypeId == 2){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[1].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[1].conversion);
                                }
                                else{
                                    value.receivedQtyCon = (value.returnQuantity);
                                    value.receivedQtyCalculation = (value.returnQuantity);
                                }
                            }
                        });
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }else{
                 			 if(self.model.search.itemName !=undefined &&  (self.model.search.itemName.replace(/\s/g, "").length) > 0){
                                    var list =  $filter('filter')(self.model.NewItemList, {
                                                        $: self.model.search.itemName
                                                       });

                                      PaginationService.searchDataByField(list, self.model.pagination);
                                  }

                 	}
            }
            else if(self.model.search.itemCode){
                  if(self.model.search.itemCode!=undefined  &&  (self.model.search.itemCode.replace(/\s/g, "").length) == 2 ){
                    var URI = CONSTANTS.GLOBAL.BATCH_API_STORE_WISE;
                    const data = {
                        "assetTypeId": self.model.assetTypeId,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId
                        
                    }

                    self.model.NewItemList = [];
                    self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.NewItemList = response.data;
                        let UniqueItemList = [];
                        let objFound=undefined;
                        angular.forEach(self.model.NewItemList, function(item) {
                            objFound = UniqueItemList.find(function (uniqueItem) {
                                return uniqueItem.batchNo == item.batchNo && uniqueItem.itemId ==item.itemId;
                                });
                            if(!objFound)
                            {
                                UniqueItemList.push(item);
                                } else{
                                    objFound.availableQuantity += item.availableQuantity;
                                    }
                            });                    
                        self.model.NewItemList = UniqueItemList;
                        /* end code by minesh for club batch and consignment logic */
                        
                        angular.forEach(self.model.NewItemList, function(value){
                            value.uomResponses.find(function(d){
                                if(d.uomTypeId == value.uomTypeId){
                                    value.uomType = d.uomDetails;
                                }
                            });
                            if(value.returnQuantity == undefined || value.returnQuantity == null || value.returnQuantity == ""){
                                value.returnQuantity = 0;
                            }
                            if(value.isThreeUOMApplicable == false){
                                if(value.uomTypeId ==1){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[0].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[0].conversion);
                                }
                                else if(value.uomTypeId == 2){
                                    
                                }
                                else{
                                    value.receivedQtyCon = (value.returnQuantity);
                                    value.receivedQtyCalculation = (value.returnQuantity);
                                }
                            }
                            else{
                                if(value.uomTypeId ==1){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[0].conversion +"*"+ value.uomResponses[1].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[0].conversion * value.uomResponses[1].conversion);
                                }
                                else if(value.uomTypeId == 2){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[1].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[1].conversion);
                                }
                                else{
                                    value.receivedQtyCon = (value.returnQuantity);
                                    value.receivedQtyCalculation = (value.returnQuantity);
                                }
                            }
                        });

                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }else{
                                  			 if(self.model.search.itemCode !=undefined &&  (self.model.search.itemCode.replace(/\s/g, "").length) > 0){
                                                     var list =  $filter('filter')(self.model.NewItemList, {
                                                                         $: self.model.search.itemCode
                                                                        });

                                                       PaginationService.searchDataByField(list, self.model.pagination);
                                                   }

                                  	}
            }
            else if(self.model.search.batchNo){
                  if(self.model.search.batchNo!=undefined  &&  (self.model.search.batchNo.replace(/\s/g, "").length) == 2 ){
                    var URI = CONSTANTS.GLOBAL.BATCH_API_STORE_WISE;
                    const data = {
                        "assetTypeId": self.model.assetTypeId,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId
                        
                    }

                    self.model.NewItemList = [];
                    self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.NewItemList = response.data;
                        let UniqueItemList = [];
                        let objFound=undefined;
                        angular.forEach(self.model.NewItemList, function(item) {
                            objFound = UniqueItemList.find(function (uniqueItem) {
                                return uniqueItem.batchNo == item.batchNo && uniqueItem.itemId ==item.itemId;
                                });
                            if(!objFound)
                            {
                                UniqueItemList.push(item);
                                } else{
                                    objFound.availableQuantity += item.availableQuantity;
                                    }
                            });                    
                        self.model.NewItemList = UniqueItemList;
                        /* end code by minesh for club batch and consignment logic */
                        
                        angular.forEach(self.model.NewItemList, function(value){
                            value.uomResponses.find(function(d){
                                if(d.uomTypeId == value.uomTypeId){
                                    value.uomType = d.uomDetails;
                                }
                            });
                            if(value.returnQuantity == undefined || value.returnQuantity == null || value.returnQuantity == ""){
                                value.returnQuantity = 0;
                            }
                            if(value.isThreeUOMApplicable == false){
                                if(value.uomTypeId ==1){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[0].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[0].conversion);
                                }
                                else if(value.uomTypeId == 2){
                                    
                                }
                                else{
                                    value.receivedQtyCon = (value.returnQuantity);
                                    value.receivedQtyCalculation = (value.returnQuantity);
                                }
                            }
                            else{
                                if(value.uomTypeId ==1){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[0].conversion +"*"+ value.uomResponses[1].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[0].conversion * value.uomResponses[1].conversion);
                                }
                                else if(value.uomTypeId == 2){
                                    value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomResponses[1].conversion);
                                    value.receivedQtyCalculation = (value.returnQuantity * value.uomResponses[1].conversion);
                                }
                                else{
                                    value.receivedQtyCon = (value.returnQuantity);
                                    value.receivedQtyCalculation = (value.returnQuantity);
                                }
                            }
                        });

                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }else{
                                  			 if(self.model.search.batchNo !=undefined &&  (self.model.search.batchNo.replace(/\s/g, "").length) > 0){
                                                     var list =  $filter('filter')(self.model.NewItemList, {
                                                                         $: self.model.search.batchNo
                                                                        });

                                                       PaginationService.searchDataByField(list, self.model.pagination);
                                                   }

                                  	}
            }
            else{
                self.model.UniqueItemList = [];
                self.model.NewItemList = [];
                self.model.pagination.data=[];
            }
        }

        var SelectAllItemsChecked = function () {
            if (self.model.selectAllItemChecked) {
                self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                    item.isItemChecked = true;
                    return item.id;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.NewItemList.forEach(function (item) {
                    item.isItemChecked = false;
                });
            }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.pagination.data.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectAllItemsExpired = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.NewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
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

            if (self.model.NewItemList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }

            self.model.isItemChecked = false;
            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        var selectedGrns=[];
        var allSelectedGrnList = [];
        var SelectAllGRNItems = function (newItem) {
            var counter=0;
            if (newItem.selectAllRows) {
                self.model.SelectedItemsIds = newItem.items.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                newItem.items.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
            newItem.items.forEach(function (items) {
                  allSelectedGrnList.push(items)
              })

            self.model.itemnew = allSelectedGrnList;
//            self.model.itemnew = newItem.items;
            if (newItem.selectAllRows) {
              	selectedGrns.push(newItem);
              } else {
            	  var index = selectedGrns.indexOf(newItem);
                  if (index > -1) {
                	  selectedGrns.splice(index, 1);
                  }
              }
              selectedGrns.forEach(function (data) {
            	  if(data.selectAllRows==true)
            		  {
            		  self.model.isItemChecked = true;
            		  }
            	  else
            		  
            		  {
            		  self.model.isItemChecked = false;
            		  }
              })
        }
        $scope.closeBonusBatchExpiry =function()
        {
            self.model.bonusBatchExpiryData=[];
        }
        $scope.lessBonusQuantity = function (item) {
            var index = self.model.bonusBatchExpiryData.indexOf(item);
            self.model.bonusBatchExpiryData.splice(index, 1);
            self.model.bonusBatchExpiryData.length-1;
            if(self.model.bonusBatchExpiryData.length<2){
                    $scope.disablMinusSign=false;
            }
          }
        var bonusBatchExpiryInfoOnClick = function (batchItem, index) {
            $scope.count = 0;
        	$scope.disablMinusSign=false;
            $scope.totalReturnQty = batchItem.returnQuantity;
            $scope.totalAvailQty = batchItem.availableQuantity;
            $scope.approvedQuantityData = batchItem.approvedQuantity; 
            self.model.bonusSelectedItemBatchExpiryIndex = index;
            var sumOfBonusReceivedQuantity=0;
            
            if(batchItem.grnBonusBatchExpiryData.length>1){
                $scope.disablMinusSign=true;
            }
            
            if(batchItem.grnBonusBatchExpiryData!=undefined && batchItem.grnBonusBatchExpiryData!=null && batchItem.grnBonusBatchExpiryData.length>0)
                {
            angular.forEach(batchItem.grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
                sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
                self.model.bonusBatchExpiryData.push({
                	returnableBonusBatchExpiryId:grnBonusBatchExpiryCount.returnableBonusBatchExpiryId,
                    batchNo: grnBonusBatchExpiryCount.batchNo,
                    expiryDate: grnBonusBatchExpiryCount.expiryDate,
                    batchId:grnBonusBatchExpiryCount.batchId,
                    bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity,
                    bonusReturnedQuantity: grnBonusBatchExpiryCount.returnedQuantity,
                    bonusReturnQuantity: grnBonusBatchExpiryCount.returnQuantity,
                    bonusUom: batchItem.bonusUom,
                    bonusQuantityLease: grnBonusBatchExpiryCount.bonusQuantityLease
                });
              });
            batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
                }
            else
                {
                sumOfBonusReceivedQuantity=0;
                self.model.bonusBatchExpiryData.push({
                	returnableBonusBatchExpiryId:null,
                    batchId:null,
                    batchNo: "",
                    expiryDate: "",
                    bonusQuantity: "",
                    bonusReturnedQuantity: "",
                    bonusReturnQuantity: "",
                });
                }
            
        }

        $scope.validate = function () {
            var isValid = true;
            $scope.totalBonusQty = 0;
            angular.forEach(self.model.bonusBatchExpiryData, function(d){
                $scope.totalBonusQty += parseInt(d.bonusReturnQuantity);
                if(d.bonusReturnQuantity == undefined || d.bonusReturnQuantity == null){
                    growl.error("Please enter return quantity..!");
                    isValid = false;
                }
            });
                
//            if(((parseInt($scope.totalReturnQty) + parseInt($scope.totalBonusQty)) - parseInt($scope.approvedQuantityData)) > parseInt($scope.totalAvailQty))
//            {
//                isValid = false;
//                growl.error('Entered  return quantity is exceeding the available for return quantity');
//            }

            return isValid;
        }

         $scope.count = 0;
        var bonusBatchExpiryModalOnClick = function () {
            $scope.count ++;
            if($scope.count == 1){
                if($scope.validate()){
                    self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData = [];
                    var modal_list = self.model.bonusBatchExpiryData;
                    var bonusReturnQuantity=0;
                    modal_list.forEach(function (modal, index) {
                        bonusReturnQuantity=bonusReturnQuantity+parseInt(modal.bonusReturnQuantity);
                        if(modal.bonusReturnQuantity>=0)
                        {
                            self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.push({
                                returnableBonusBatchExpiryId:modal.returnableBonusBatchExpiryId!=null ? modal.returnableBonusBatchExpiryId :null ,
                                batchNo: modal.batchNo,
                                expiryDate: modal.expiryDate,
                                bonusQuantity: modal.bonusQuantity,
                                batchId:modal.batchId,
                                returnedQuantity: modal.bonusReturnedQuantity,
                                returnQuantity: modal.bonusReturnQuantity,
                                bonusQuantityLease: modal.bonusQuantityLease
                            });
                        }
                    });
                    self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex].bonusReturnQuantity=bonusReturnQuantity;
                    self.model.bonusBatchExpiryData=[];
                    $('#bouns_selectBatchExpiry_modal').modal('hide');
                }
            }
            else{
                $('#bouns_selectBatchExpiry_modal').modal('hide');
                $scope.count = 0;
            }
        }

        $scope.closeBonusBatchExpiry =function()
        {
            self.model.bonusBatchExpiryData=[];
            $scope.count = 0;
        }
        $scope.checkBonusReturnQty=function(item)
        {
        	angular.forEach(self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData, function (qr) {
        		if(qr.batchId==item.batchId)
        			{
        			qr.returnQuantity=parseInt(item.bonusReturnQuantity);
        			}
        	})
        	ValidateQuantity(self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex]);
        	if(parseInt(item.bonusReturnQuantity)>(parseInt(item.bonusQuantity)-parseInt(item.bonusReturnedQuantity!="" && item.bonusReturnedQuantity!=null && item.bonusReturnedQuantity!=undefined ? item.bonusReturnedQuantity :0)))
        		{
        		growl.error("Enter Quantity Must Be Less Than Or Equal To Available For Return");
        		item.bonusReturnQuantity=0;
        		}
        }
        var SelectGRNItem = function (item, newItem) {
            var itemIds = self.model.SelectedItemsIds;

            if (item.isSelected) {
                itemIds.push(item.itemId);
            } else {
                var index = itemIds.indexOf(item.itemId);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (newItem.items.every(isSelected)) {
            	newItem.selectAllRows = true;
            } else {
            	newItem.selectAllRows = false;
            }

            self.model.isItemChecked = false;
            newItem.items.forEach(function (data) {
                if (data.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
          self.model.itemnew = newItem.items;
          if (newItem.selectAllRows) {
          	selectedGrns.push(newItem);
          } else {
        	  var index = selectedGrns.indexOf(newItem);
              if (index > -1) {
            	  selectedGrns.splice(index, 1);
              }
          }
          selectedGrns.forEach(function (data) {
        	  if(data.selectAllRows==true)
        		  {
        		  self.model.isItemChecked = true;
        		  }
        	  else
        		  
        		  {
        		  self.model.isItemChecked = false;
        		  }
          })
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        function getAllItemDetails() {
            if (self.model.requestType.requestTypeId == 6) {
                self.model.NewItemList.forEach(function (item) {
                    if (item.isSelected == true) {
                        var isFound = self.model.itemList.find(function (x) {
                            return x.batchId == item.batchId;
                        });
                        if (!isFound) {
                            self.model.itemList.push(item);
                            angular.forEach(self.model.itemList, function (item, key) {
                                item.cop = item.cop;
                            });
                        }
                    }
                    if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
                        item.returnQuantity = 0;
                    }
                    if(item.uomList == undefined || item.uomList == null || item.uomList.length != 0){
                        item.uomList = item.uomResponses;
                    }
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion);
                            item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion);
                        }
                        else if(item.uomTypeId == 2){
                            
                        }
                        else{
                            item.receivedQtyCon = (item.returnQuantity);
                            item.receivedQtyCalculation = (item.returnQuantity);
                        }
                    }
                    else{
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                            item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
                            item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
                        }
                        else{
                            item.receivedQtyCon = (item.returnQuantity);
                            item.receivedQtyCalculation = (item.returnQuantity);
                        }
                    }
                });
            }
            else {
                self.model.NewItemList.forEach(function (item) {
                    if (item.isSelected == true) {
                        var isFound = self.model.itemList.find(function (x) {
                            return x.batchId == item.batchId;
                        });
                        if (!isFound) {
                            self.model.itemList.push(item);
                            angular.forEach(self.model.itemList, function (item, key) {
                                item.cop = item.cop;
                            });

                        }
                    }
                    if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
                        item.returnQuantity = 0;
                    }
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion);
                            item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion);
                        }
                        else if(item.uomTypeId == 2){
                            
                        }
                        else{
                            item.receivedQtyCon = (item.returnQuantity);
                            item.receivedQtyCalculation = (item.returnQuantity);
                        }
                    }
                    else{
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                            item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
                            item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
                        }
                        else{
                            item.receivedQtyCon = (item.returnQuantity);
                            item.receivedQtyCalculation = (item.returnQuantity);
                        }
                    }
                });
            }
            addDecimal();
            
            /* start code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
            ClearSearch();
            /* end code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
        }

        var addDecimal = function () {
            angular.forEach(self.model.itemList, function (item) {
                //item.taxPercentage = item.taxPercentage.toFixed(2);
            	/* start code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
                item.taxPercentage = parseFloat(item.taxPercentage).toFixed(2);
                /* end code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
            })
        }

        $scope.okBtnDisabled = false;
        function getAllGRNItemDetails() {
        	$scope.okBtnDisabled = true;
            self.model.itemnew.forEach(function (item) {
                if (item.isSelected == true) {
                	item.returnQuantityFlag=false;
                    if(item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
                        item.uomList.find(function(d){
                            if(d.uomTypeId == item.uomTypeId){
                                item.uomType = d.uomDetails;
                            }
                            if(item.bonusUomTypeId == d.uomTypeId){
                                item.bonusUom = d.uomDetails;
                            }
                        });
                        if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
                            item.returnQuantity = 0;
                        }
                        if(item.isThreeUOMApplicable == false){
                            if(item.uomTypeId ==1){
                                item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion);
                                item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion);
                            }
                            else if(item.uomTypeId == 2){
                                
                            }
                            else{
                                item.receivedQtyCon = (item.returnQuantity);
                                item.receivedQtyCalculation = (item.returnQuantity);
                            }
                        }
                        else{
                            if(item.uomTypeId ==1){
                                item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                            }
                            else if(item.uomTypeId == 2){
                                item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
                                item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
                            }
                            else{
                                item.receivedQtyCon = (item.returnQuantity);
                                item.receivedQtyCalculation = (item.returnQuantity);
                            }
                        }
                    }
                    if(item.phyRejectQuantity==null || item.phyRejectQuantity==undefined)
                    	{
                    	item.phyRejectQuantity=(item.grnRejectQuantity!=null && item.grnRejectQuantity!=undefined ? item.grnRejectQuantity:0)
                    	}
                    if(item.grnBonusBatchExpiry != undefined && item.grnBonusBatchExpiry.length != 0){
                        angular.forEach(item.grnBonusBatchExpiry, function(b){
                            b.bonusUom = item.bonusUom;
                            b.bonusQuantityLease = b.bonusQuantity;
                            if(item.bonusUomTypeId == 1){
                                b.bonusQuantity = (b.bonusQuantity / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)); 
                                b.returnedQuantity = (b.returnedQuantity / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)); 
                            }
                            else if(item.bonusUomTypeId == 2){
                                b.bonusQuantity = (b.bonusQuantity / (item.uomList[1].conversion * item.uomList[2].conversion));
                                b.returnedQuantity = (b.returnedQuantity / (item.uomList[1].conversion * item.uomList[2].conversion));
                            }
                            else{
                               if(item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
                                b.bonusQuantity = (b.bonusQuantity / (item.uomList[2].conversion));
                                b.returnedQuantity = (b.returnedQuantity / (item.uomList[2].conversion));
                               }
                            }
                            b.returnQuantity = b.bonusQuantity-b.returnedQuantity;
                            item.bonusReturnQuantity = item.bonusReturnQuantity + b.returnQuantity;
                            item.bonusReturnedQuantity = b.returnedQuantity;
                        })
                    }
                    self.model.itemList.push(item);
                }
            });
            	var uniqueByKey = [  ...new Map(
                    self.model.itemList
                      .filter(Boolean)
                      .map(item => [item['grnDetailId'], item])
            		).values()
                  ];
            	self.model.itemList = (uniqueByKey);
            	var popup = angular.element('#getGRNModal');
                popup.modal('hide');
            /* start code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
            ClearSearch();
            /* end code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
        }

        function removeRowDataPopup(item) {
            var index = self.model.accessoryList.indexOf(item);
            self.model.accessoryList.splice(index, 1);
        }
        $scope.saveBtnDisabled=false;
        var SaveReturnableGatepass = function () {
        	$scope.saveBtnDisabled=true;
            if (validateSave()) {
                if (!self.model.returnableId) {
                    SaveNewGatepass();
                } else {
                    UpdateGatepass();
                }
            }
        }

        var SaveNewGatepass = function () {
        	$scope.saveBtnDisabled=true;
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.SAVE_API;
            var data = {
                "storeId": self.model.store.id,
                "vendorId": self.model.vendorId,
                "assetTypeId": parseInt(self.model.assetTypeId),
                "againstId": parseInt(self.model.requestType.requestTypeId),
                "referenceNo": self.model.referenceNo,
                "statusId": 1,
                "approvalStatusId": 1,
                "totalAmount": self.model.totalAmount,
                "totalTaxAmount": self.model.totalTaxAmount,
                "netAmount": self.model.netAmount,
                "remark": self.model.remark,
                "grnId": self.model.grnId,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId

            };

            angular.forEach(store.itemList, function (item, key) {
                if (self.model.assetTypeId == 2) {
                    var itemAccessories = [];
                    if( item.accessories!=undefined)
                    {
                      item.accessories.forEach(function (element) {
                        itemAccessories.push({
                            "batchId": element.batchId,
                            "returnQuantity": parseInt(element.quantity)
                        });
                    });       
                    }
                    else
                    {
                        item.accessories=[];
                    }
                }
                var array=[];
                angular.forEach(item.grnBonusBatchExpiryData, function (item1, key) {
                	if(item1.returnQuantity!="" && item1.returnQuantity>=0)
                    {
                        array.push({
                            returnableBonusBatchExpiryId: item1.returnableBonusBatchExpiryId!=null ? item1.returnableBonusBatchExpiryId :null ,
                            batchNo: item1.batchNo,
                            expiryDate: item1.expiryDate,
                            bonusQuantity: item1.bonusQuantityLease,
                            batchId: item1.batchId,
                            returnedQuantity: item1.bonusReturnedQuantity,
                            returnQuantity: item1.returnQuantity
                        });
                    }
                })
                
                	item.grnBonusBatchExpiryData=array;
                var storeItem = {
                    "batchId": item.batchId,
                    "vendorId": item.vendorId,
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "taxId": item.taxId,
                    "taxPercentage": parseFloat((isNaN(item.taxPercentage) || item.taxPercentage==undefined ? 0 : item.taxPercentage)),
                    "returnQuantity": parseInt(item.returnQuantity),
                    "totalAmount": item.totalAmount,
                    "totalTaxAmount": (isNaN(item.taxAmount) || item.taxAmount==undefined ? 0 : item.taxAmount),
                    "netAmount": item.netAmount,
                    "statusId": 1,
                    "grnDetailId": item.grnDetailId,
                    "accessories":item.accessories,
                    "totalDiscount":item.discountPercentage,
                    "returnableBonusBatchExpiry":item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null ? item.grnBonusBatchExpiryData:[],
                    "bonusUomTypeId": item.bonusUomTypeId,
                }
                data.items.push(storeItem);
            });
            /*  var acessoriesObj = angular.forEach(storeItem, function (item, key) {
                 var accessories = {
                     "batchId": item.batchId,
                     "returnQuantity": parseInt(item.returnQuantity),
                 }
                 storeItem.push(accessories);
             });
  */
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

        var UpdateGatepass = function () {
        	$scope.saveBtnDisabled=true;
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.UPDATE_API + self.model.returnableId;
            var data = {
                "assetTypeId": parseInt(self.model.assetTypeId),
                /*start code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass*/ 
                "vendorId": self.model.vendorId,
                /*end code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass*/ 
                "totalAmount": self.model.totalAmount,
                "assetTypeId": parseInt(self.model.assetTypeId),
                "totalTaxAmount": self.model.totalTaxAmount,
                "netAmount": self.model.netAmount,
                "remark": self.model.remark,
                "referenceNo": self.model.referenceNo,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };
            angular.forEach(self.model.itemList, function (item, key) {
            	var array=[];
                angular.forEach(item.grnBonusBatchExpiryData, function (item1, key) {
                	if(item1.returnQuantity!="" && item1.returnQuantity>=0)
                    {
                		// array.push(item1);
                        array.push({
                            returnableBonusBatchExpiryId: item1.returnableBonusBatchExpiryId!=null ? item1.returnableBonusBatchExpiryId :null ,
                            batchNo: item1.batchNo,
                            expiryDate: item1.expiryDate,
                            bonusQuantity: item1.bonusQuantityLease,
                            batchId: item1.batchId,
                            returnedQuantity: item1.bonusReturnedQuantity != undefined && item1.bonusReturnedQuantity != null ? item1.bonusReturnedQuantity : 0,
                            returnQuantity: item1.returnQuantity
                        });
                    }
                })
                
                item.grnBonusBatchExpiryData=array;
                var storeItem = {
                    "id": item.id,
                    "returnQuantity": item.returnQuantity,
                    "totalAmount": item.totalAmount,
                    "totalTaxAmount": item.totalTaxAmount,
                    "netAmount": item.netAmount,
                    "uomTypeId" : item.uomTypeId,
                    "uomUnitId" : item.uomUnitId,
                    "returnableBonusBatchExpiry":item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null ? item.grnBonusBatchExpiryData:[]
                }
                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.vendorId = item.vendorId,
                        storeItem.taxId = item.taxId,
                        storeItem.taxPercentage = item.taxPercentage,
                        storeItem.returnQuantity = item.returnQuantity,
                        storeItem.totalAmount = item.totalAmount,
                        //storeItem.totalTaxAmount = item.totalTaxAmount,                        
                        storeItem.netAmount = item.netAmount,
                        storeItem.statusId = 1,
                        /* start code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
                        storeItem.totalTaxAmount = item.taxAmount,
                        storeItem.grnDetailId = item.grnDetailId
                        /* end code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
                }
                data.items.push(storeItem);


            });
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    NavigteToBack();
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                },
                function (err) {
                	 $scope.saveBtnDisabled=false;
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }


        var NavigteToBack = function () {
            $state.go('returnableGatepass');
        }
        $scope.noOfRecordsPerPage=100;
        $scope.offset=0;
        function searchByNearExpiry() {

            var URI = CONSTANTS.GLOBAL.DATE_NEAR_EXPIRY;
            const data = {
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "assetTypeId": self.model.assetTypeId,
                "fromStoreId": self.model.store.id,
                "itemName": self.model.itemNameSearch,
                "unitId":$scope.unitId,
                "offset":$scope.offset,
                "noOfRecordsPerPage":$scope.noOfRecordsPerPage
            }

            self.model.NewItemList = [];
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.NewItemList = response.data;
                                        
                    /* start code by minesh for club batch and consignment logic with item id*/
                    let UniqueItemList = [];
                    let objFound=undefined;
                    angular.forEach(self.model.NewItemList, function(value) {
                    	if(value.uomTypeId ==1){
                    		value.cop = (value.cop *( value.uomList[0].conversion * value.uomList[1].conversion));
                        }
                        else if(value.uomTypeId == 2){
                        	value.cop = (value.cop *(value.uomList[1].conversion));
                        }
                    	value.uomList.find(function(d){
                            if(d.uomTypeId == value.uomTypeId){
                                value.uomType = d.uomDetails;
                            }
                        });
                        if(value.returnQuantity == undefined || value.returnQuantity == null || value.returnQuantity == ""){
                            value.returnQuantity = 0;
                        }
                        if(value.isThreeUOMApplicable == false){
                            if(value.uomTypeId ==1){
                                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[0].conversion);
                                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[0].conversion);
                            }
                            else{
                                value.receivedQtyCon = (value.returnQuantity);
                                value.receivedQtyCalculation = (value.returnQuantity);
                            }
                        }
                        else{
                            if(value.uomTypeId ==1){
                                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[0].conversion +"*"+ value.uomList[1].conversion);
                                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[0].conversion * value.uomList[1].conversion);
                            }
                            else if(value.uomTypeId == 2){
                                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[1].conversion);
                                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[1].conversion);
                            }
                            else{
                                value.receivedQtyCon = (value.returnQuantity);
                                value.receivedQtyCalculation = (value.returnQuantity);
                            }
                        }
                    	objFound = UniqueItemList.find(function (uniqueItem) {
                    		return uniqueItem.batchNo == value.batchNo && uniqueItem.itemId ==value.itemId;
                        });
                    	if(!objFound)
                    	{
                    		UniqueItemList.push(value);
                        } else{
                            objFound.availableQuantity += value.availableQuantity;
                        }
                    });                    
                    self.model.NewItemList = UniqueItemList;
                    /* end code by minesh for club batch and consignment logic */
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
        }

        var validateSave = function () {
            var isValid = true;
            var ItemDetailList = self.model.itemList;
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 
            if (ItemDetailList.length < 1) {
                growl.error("Please Add Items");
                isValid = false;
            }
            if (!self.model.assetTypeId) {
                growl.error("Please Enter Valid Asset Type");
                isValid = false;
            }
            if (!self.model.vendorId) {
                growl.error("Please Enter Valid Vendor");
                isValid = false;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                    isValid = false;
                }
            }
            if (!self.model.referenceNo) {
                growl.error("Please Enter Reference No");
                isValid = false;
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }

        var ValidateQuantity = function (item) {
            var isValid = true;
            if(item.returnQuantity!=undefined && item.returnQuantity!=null && item.returnQuantity!='' && item.returnQuantity>0)
            	{
            	item.returnQuantityFlag=true;
            	}
            if(item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null)
            	{
            	var totalBonusQty=0;
            	var actualBonusQuantityLease=0;
            	var actualBonusQty=0;
            angular.forEach(item.grnBonusBatchExpiryData, function (qr) {
            	if(item.bonusUomTypeId == 1){
            		qr.returnQuantityLease = (parseInt(qr.returnQuantity) * (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)); 
                }
                else if(item.bonusUomTypeId == 2){
                	qr.returnQuantityLease = (parseInt(qr.returnQuantity) * (item.uomList[1].conversion * item.uomList[2].conversion));
                }
                else{
                     if( item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
                        qr.returnQuantityLease = (parseInt(qr.returnQuantity) * (item.uomList[2].conversion));
                     }

                }
            	if(qr.batchId==item.batchId || qr.batchNo==item.batchNo)
            		{
            		totalBonusQty+=parseInt(qr.returnQuantityLease);
            		actualBonusQuantityLease+=parseInt(qr.bonusQuantityLease!=undefined && qr.bonusQuantityLease!=null ? qr.bonusQuantityLease :0);
                    actualBonusQty+=qr.bonusQuantity;
            		}
            	if(item.uomTypeId!=item.bonusUomTypeId)
            	{
            	if(item.uomTypeId==1)
            		{
            		totalBonusQty = (totalBonusQty / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion));
            		actualBonusQty = (actualBonusQty / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion));
            		actualBonusQuantityLease = (actualBonusQuantityLease / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion));
            		}
            	if(item.uomTypeId==2)
            		{
            		totalBonusQty = (totalBonusQty / ( item.uomList[1].conversion * item.uomList[2].conversion));
            		actualBonusQty = (actualBonusQty / ( item.uomList[1].conversion * item.uomList[2].conversion));
            		actualBonusQuantityLease = (actualBonusQuantityLease / ( item.uomList[1].conversion * item.uomList[2].conversion));
            		}
            	}
            })
            if(totalBonusQty<1)
            	{
            	totalBonusQty=0;
            	}
            if(actualBonusQuantityLease<1)
        	{
            	actualBonusQuantityLease=0;
        	}
            if(actualBonusQty<1)
        	{
            	actualBonusQty=0;
        	}
            totalBonusQty=Math.floor(totalBonusQty);
            actualBonusQuantityLease=Math.floor(actualBonusQuantityLease);
            actualBonusQty=Math.floor(actualBonusQty);
            if((parseInt(item.returnQuantity)+totalBonusQty)>parseInt(item.availableQuantity)+actualBonusQuantityLease)
            	{
            	item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error('Entered  return quantity is exceeding the available for return quantity');
                return false;
            	}
            }
            if(parseInt(item.returnQuantity)>parseInt(item.availableQuantity))
              {
            	item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error('Entered  return quantity is exceeding the available for return quantity');
                return false;
                
            }
            if((parseInt(item.totalGRNQuantity) - parseInt(item.approvedQuantity)) < parseInt(item.returnQuantity))
            {
            	item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error('Entered  return quantity is exceeding the available for return quantity');
            }
            if(((parseInt(item.returnQuantity)+actualBonusQty) - parseInt(item.approvedQuantity)) > parseInt(item.availableQuantity))
            {
            	item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error('Entered  return quantity is exceeding the available for return quantity');
            }
            if (isNaN(parseFloat(item.returnQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error("Please Enter Return Quantity");
            } else if (parseInt(item.returnQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error('Return Quantity cannot be zero');
            }
            else if (item.availableQuantity <= 0) {
                item.isQuantityValid = true;
                isValid = false;
                item.returnQuantityFlag=false;
                growl.error('Available quantity must be greater than zero.');
            }
            else {
                item.returnQuantity = parseInt(item.returnQuantity);
                isValid = true;
            }
            
            if($scope.requestTypeToCheck=='Rejected Against GRN')
            {
            	if (parseInt(item.returnQuantity) > (item.availableQuantity)) {
                  item.isQuantityValid = true;
                  isValid = false;
                  item.returnQuantityFlag=false;
                  growl.error('Entered  return quantity is exceeding the stock available quantity');
                } 
            }
            else
            {
                if (parseInt(item.returnQuantity) > (item.availableQuantity)) {
                    item.isQuantityValid = true;
                    isValid = false;
                    item.returnQuantityFlag=false;
                    growl.error('Entered  return quantity is exceeding the stock available quantity');
                }
            }
            return isValid;
        }

        $scope.requestTypeToCheck;
        function searchNewGRN() {
            if(self.model.requestType.requestTypeId == 17){
                var URI = CONSTANTS.GLOBAL.GRNAPI_AGAINST;
            }
            else{
                var URI = CONSTANTS.GLOBAL.GRN_API;
            }
            $scope.requestTypeToCheck=self.model.requestType.requestedType;
            const data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "vendorId": self.model.vendorIdGRN,
                "assetTypeID":self.model.assetTypeId,
                "unitId":$scope.unitId
            }

            self.model.NewItemList = [];
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    console.log("self.model.requestType.requestTypeId",self.model.requestType.requestTypeId);
                    self.model.NewItemList = response.data;
                    angular.forEach(self.model.NewItemList, function (qr) {
                        qr.isItemChecked = false;
                    });
                    $scope.noActualBonusBatchPresent=false;
                    angular.forEach(self.model.NewItemList, function (grn) {
                    	var newArray=[];
                		angular.forEach(grn.items, function (item) {
                			if(item.phyRejectQuantity==null || item.phyRejectQuantity==undefined)
                        	{
                        	item.phyRejectQuantity=(item.grnRejectQuantity!=null && item.grnRejectQuantity!=undefined ? item.grnRejectQuantity:0)
                        	}
                            if(self.model.requestType.requestTypeId == 17 || self.model.requestType.requestTypeId == 15){
                                    newArray.push(item);
                            }
                			 else{
                                 if(item.phyRejectQuantity>0)
                                 {
                                	 newArray.push(item);
                                 } 
                             
                		 	}
                			
                			item.grnBonusBatchExpiryData=item.grnBonusBatchExpiry;
                			var bonusReturnedQty=0;
                			var bonusReturnQuantity=0;
                			angular.forEach(item.grnBonusBatchExpiryData, function (bonus) {
                				bonusReturnedQty=bonusReturnedQty+bonus.returnedQuantity;
                                if(bonus.returnQuantity == undefined || bonus.returnQuantity == null){
                                    bonus.returnQuantity = 0;
                                }
                				bonusReturnQuantity=bonusReturnQuantity+parseInt(bonus.returnQuantity);
                			})
                			item.bonusReturnedQuantity=bonusReturnedQty!=undefined && bonusReturnedQty!=null ? bonusReturnedQty:0;
                			item.bonusReturnQuantity=bonusReturnQuantity!=undefined && bonusReturnQuantity!=null ? bonusReturnQuantity:0;
                			if(item.grnBonusBatchExpiry!=undefined && item.grnBonusBatchExpiry!=null && item.grnBonusBatchExpiry.length==0)
                				{
                				if(item.bonusApprovedQuantity>0)
                					{
                					$scope.noActualBonusBatchPresent=true;
                					var date = Date.parse(item.expiry+"GMT");
                				var obj={
                						returnableBonusBatchExpiryId:null,
                	                    batchId:item.batchId,
                	                    batchNo: item.batchNo,
                	                    expiryDate: date,
                	                    bonusQuantity: item.bonusApprovedQuantity,
                	                    returnedQuantity: "",
                	                    returnQuantity: ""
                				}
                				item.grnBonusBatchExpiryData.push(obj);
                				}
                				}
                		});
                		grn.items=newArray;
                	});

                	$scope.checkReturnableGatePass();

                    /* start code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the accepted quantity at physical inspection as available quantity*/
                    if(self.model.assetTypeId==1){
                    	angular.forEach(self.model.NewItemList, function (grn) {
                    		angular.forEach(grn.items, function (item) {
                    			let avaliblestockUom = CalculateStock(item, item.uomTypeId, item.uomList);
                    			 item.availableQuantity = avaliblestockUom;                    		
                    		});
                    	});
                    	
                    }
                    /* start code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the accepted quantity at physical inspection as available quantity*/
                    
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('No Records Found');
                });
        }


        $scope.checkReturnableGatePass = function(){
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.SAVE_API + 'checkReturnableGatePass';
            $scope.newArray1 = [];
                angular.forEach(self.model.NewItemList, function (grn) {

                    angular.forEach(grn.items, function (item) {

                        var data ={
                            'grnDetailId': item.grnDetailId,
                            'requestTypeId' : self.model.requestType.requestTypeId,
                        };
                        $scope.newArray1.push(data);
                    });
                });

                GenericService.serviceAction("POST", URI, $scope.newArray1).then(
                     function (response) {
                        $rootScope.stopLoaderPopup();

                        var responseList = response.data;

                        angular.forEach(self.model.NewItemList ,function (grn, index){
                            var resItemList = [];
                                angular.forEach(grn.items ,function (item, index){

                                    angular.forEach(responseList ,function (resItem, index){

                                        if(resItem.grnDetailId == item.grnDetailId){

                                       	    if(!resItem.checkReturnableGatePassFlag || item.isApprovalPending == 'N'){

                                       			resItemList.push(item);

                                       		}
                                       	}

                                    });
                                });
                                grn.items = resItemList;
                        });
                     },
                     function errorCallback(response)
                     {
                        $rootScope.stopLoaderPopup();
                        growl.error('No Records Found');
                });
        }


        var itemRemovePopup = function (item) {
            self.model.itemRemove = '';
            self.model.itemRemove = item;
            $("#itemRemovePopup").modal()

        }
        function removeRowData() {
            var index = self.model.itemList.indexOf(self.model.itemRemove);
            self.model.itemList.splice(index, 1);
        }


        var confirmClear = function () {
            self.model.itemList = [];

        }



        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeStore = function (store) {
            if (!!self.model.itemList.length) {
                self.model.store = self.model.oldStore;
                self.model.storeNew = JSON.parse(JSON.stringify(store));
                $("#storeChange").modal()
            }
        }

        var ConfirmPopup = function () {
            self.model.store = self.model.storeNew;
            self.model.itemList = [];
            ModalDismiss('storeChange');
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var SetOldValueAgainst = function (againstId) {
            self.model.againstId = againstId;
            self.model.oldAgainstId = againstId.requestTypeId;
            self.model.requestType.requestTypeId = self.model.oldAgainstId;
        }

        var ChangeAgainst = function (againstId) {
            if (!!self.model.itemList.length) {
//                self.model.requestType.requestTypeId = self.model.oldAgainstId;
                self.model.againstIdNew = JSON.parse(JSON.stringify(againstId));
                $("#against").modal()
            }
        }

        var ConfirmAgainstPopup = function () {
            self.model.requestType = self.model.againstIdNew;
            self.model.itemList = [];
            ModalDismiss('#against');
        }

        var againstPopupClose = function () {
            self.model.requestType = self.model.againstId;
            ModalDismiss('#against');
        }

        var accessoryModal = function (asset) {
            $("#add_accessories_modal").modal()
            self.model.accessoryList = [];
            self.model.accessoryList = asset.accessoriesResponse;
        }

        var SaveAccessoryDetails = function () {
            if (validateAccessory()) {
                self.model.ItemDetailList.forEach(function (obj) {
                    if (obj.itemId == self.model.currentItem.itemId) {
                        self.model.accessoryList.forEach(function (item) {
                            obj.accessories.push({
                                "itemId": item.itemId,
                                "itemCode": item.itemCode,
                                "itemName": item.itemName,
                                "quantity": item.quantity
                            })
                        });
                    }
                });

                var popup = angular.element('#add_accessories_modal');
                popup.modal('hide');
            }
        }
        var validateAccessory = function () {
            var isValid = true;
            self.model.accessoryList.forEach(function (item) {
                if (!validateAccessoryPopup(item)) {
                    isValid = false;
                }
            });
            return isValid;
        }

        var ConfirmRemove = function (item) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = item;
        }

        var RemoveItem = function (item) {
            var newArray = [];

            self.model.ItemDetailList.map(function (obj) {
                if (obj !== item) {
                    newArray.push(obj);
                }
            });
            self.model.ItemDetailList = newArray;
            var popup = angular.element('#itemRemovePopup');
            popup.modal('hide');
        }

        var validateAccessoryPopup = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.quantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.quantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            else if (parseInt(item.quantity) > item.newMaxOrderQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max order quantity');
            } else {
                item.quantity = parseInt(item.quantity);
                isValid = true;
            }

            return isValid;

        }
        
        var CalculateStock = function (item, uomTypeId, uomList) {
        	var availableQuantity = angular.copy(item.availableQuantity);
        	var uom = [];
        	uom = angular.copy(uomList);
        	if($scope.requestTypeToCheck!='Rejected Against GRN')
        		{
        		if (!!uomList && uomTypeId && uom.length > 0) {              
                    var newAvailableQuantity = 0;
                    if (uomTypeId == 1 ) {
                  	  newAvailableQuantity = parseInt(availableQuantity / (uom[0].conversion * uom[1].conversion));
                    } else if (uomTypeId ==2 ) {               
                  	  newAvailableQuantity = parseInt(availableQuantity / (uom[1].conversion * uom[2].conversion));
                    } else if (uomTypeId==3) {
                  	  newAvailableQuantity = parseInt(availableQuantity / (uom[2].conversion));
                    }else{
                  	  newAvailableQuantity=parseInt(availableQuantity);
                    }
                    item.availableQuantity=newAvailableQuantity;
                    return newAvailableQuantity;
                  }
        		}
        	
            
            return availableQuantity;
          }
        
        /* start code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */
        var ClearSearch = function (){
        	$scope.search='';
        	self.model.itemNameSearch='';
        }
        /* end code for resolved HIS-10188 Returnable Gate Pass Master : After Update Item Unable to Save Returnable Gate Pass */

        $scope.calculateReceivedQty = function (item) {
			if(item.isThreeUOMApplicable == false){
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion);
					item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion);
				}
				else if(item.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.returnQuantity);
					item.receivedQtyCalculation = (item.returnQuantity);
				}
			}
			else{
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
					item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
				}
				else if(item.uomTypeId == 2){
					item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
					item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
				}
				else{
					item.receivedQtyCon = (item.returnQuantity);
					item.receivedQtyCalculation = (item.returnQuantity);
				}
			}
		}
        
        $scope.calculateAmount = function(item){
        	item.totalAmount = ((item.cop.toFixed(2)) * item.returnQuantity);
        	item.discountAmount = ((item.discount * item.totalAmount)/100);
        	item.taxAmount = (((item.totalAmount - item.discountAmount) * (item.taxPercentage)/100));
        	item.netAmount = ((item.totalAmount-item.discountAmount) + (item.taxAmount));
        	item.discountAmountCal = item.discountAmount;
        	item.taxAmountCal = item.taxAmount;
        	item.netAmountCal = item.netAmount;
        }
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewReturnableGatepass", {
            url: "/addNewReturnableGatepass",
            templateUrl: 'views/PharmacyInventory/InPatient/add-new-returnable-gatepass.html',
            controller: "addNewReturnableGatepass.Controller",
            controllerAs: "vm",
            title: 'Returnable GatePass Master'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("addNewReturnableGatepass.Controller", addNewReturnableGatepassController);
})();
