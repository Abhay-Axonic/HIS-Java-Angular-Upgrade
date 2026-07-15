    (function () {
        "use strict";
        var CACHE = {};

        function addNewbillableConsumptionController($scope, $state, $rootScope, $http, $filter, $cookies, CONSTANTS, StatusService,PaginationService, GenericService, growl, commonDetailService) {
            var self = this;
            var mainStoreList = [];
            var visitNumber = '';
            var cookieObjectAppConfig = {};
            [cookieObjectAppConfig] = $rootScope.checkCookies();

            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');

            if (cookieObjectAppConfig == undefined) {
            $state.go('login');
            return;
            }
            //Default Constructor
            var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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
                    storeList: [{
                        "store": "All"
                    }],
                    assetCategories: [{
                        "category": "Select Asset Category"
                    }],
                    productCategories: [{
                        "category": "Select Product Category"
                    }],
                    wardList: [{
                        "wardName": "All"
                    }],
                    visitDetails: [{
                        "visitNo": "Select Visit No"
                    }],
                    visitTypeList: [{
                        "type": "Select VisitType"
                    }],
                    SelectedItemsIds: [],
                    selectAllRows: false,
                    SelectedItemIds: [],
                    ItemDetailList: [],
                    selectedStore: '',
                    // indentId: null,
                    consignment: false,
                    itemSearch: {
                        itemCode : undefined,
                        itemName : undefined,
                        batchNo : undefined
                    }
                };

                self.model.taxInclusive = cookieObjectAppConfig.taxInclusive;

                StatusService.GetStatus().then(function (status) {
                    self.model.Status = status;
                })

                self.model.SearchpatientNames = SearchpatientNames;
                self.model.SearchUhid = SearchUhid;
                self.model.AddPatientDetails = AddPatientDetails;
                self.model.search = '';
                self.model.prevSearch = '';
                self.model.patientNames = [];
                self.model.GetItemList = GetItemList;
                self.model.GetAssetCategoryList = GetAssetCategoryList;
                self.model.GetProductCategoryList = GetProductCategoryList;
                self.model.SearchNewItem = SearchNewItem;
                self.model.NewItemList = [];
                self.model.SelectItem = SelectItem;
                self.model.SelectAllItems = SelectAllItems;
                self.model.getAllItemDetails = getAllItemDetails;
                self.model.RemoveRowData = RemoveRowData;
                self.model.Validate = Validate;
                self.model.ValidateQuantity = ValidateQuantity;
                self.model.SaveNewbillableConsumption = SaveNewbillableConsumption;
                self.model.NavigteToBillableConsumption = NavigteToBillableConsumption;
                self.model.ChangeVisitNumber = ChangeVisitNumber;
                self.model.ChangeVisitType = ChangeVisitType;
                self.model.PatientNameKeyPress = PatientNameKeyPress;
                self.model.uHIDKeyPress = uHIDKeyPress;
                self.model.ConfirmRemove = ConfirmRemove;
                self.model.PopupAction=PopupAction;
                self.model.CalculateLeaseQuantity=CalculateLeaseQuantity;


                // self.model.indentId = commonDetailService.getDataId();
                // GetPatientIndentDetails(commonDetailService.getDataId());

                PopulateDropdown();
                // GetAssetCategoryList();
            }

            // var GetPatientIndentDetails = function (itemId) {

            //     var URI = CONSTANTS.PHARMACY.OP.PATIENT_INDENT.DETAIL_API + itemId;
            //     $rootScope.startLoader();
            //     GenericService.serviceAction("GET", URI, {}).then(
            //         function (response) {
            //             self.model.indentId = response.data.id;
            //             self.model.ItemDetailList = response.data.items;
            //             $rootScope.stopLoader();
            //         });

            // }

            var PopulateDropdown = function () {

                var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;                
                self.model.visitType = self.model.visitTypeList[0];
                self.model.visitNumber = self.model.visitDetails[0];
                 self.model.store = self.model.storeList[0];
                self.model.ward = self.model.wardList[0];

                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                        self.model.storeList = self.model.storeList.concat(response.data.store);
                        self.model.wardList = self.model.wardList.concat(response.data.ward);
                        var storedata = JSON.parse($cookies.get("cookieObject"));
                        var storename = self.model.storeList.filter(x => x.id == storedata.storeId);
                        self.model.selectedStore = storename[0].store;
                        self.model.storeId = storedata.storeId;
                        $rootScope.stopLoader();
                    });
            }

            var SearchpatientNames = function (search) {

                if (search.length < 1) return;

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + self.model.visitType.id;

                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
                                self.model.patientId = response.data.patientId;
                                self.model.uhid = response.data.uhid;
                                $rootScope.stopLoader();
                                //console.log(self.model.patientNames.itemName);
                                return $filter('filter')(self.model.patientNames, {
                                    $: search
                                });
                            } else {
                                $rootScope.stopLoader();
                                return PatientNameNotFound(search);
                            }
                        }, function (err) {
                            $rootScope.stopLoader();
                            return PatientNameNotFound(search);
                        });
                } else {
                    if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
                        self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
                        return PatientNameNotFound(search);
                    } else {
                        return $filter('filter')(self.model.patientNames, {
                            $: search
                        });
                    }
                }
            }

            var PatientNameNotFound = function (search) {
                var item = {
                    "itemFound": false,
                    "details": "Patient Not found with name " + search
                };
                self.model.patientNames = [item];
                return self.model.patientNames;
            }

            var SearchUhid = function (search) {

                if (search.length < 1) return;

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search +'/' + $scope.organizationId;

                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
                                self.model.patientId = response.data.patientId;
                                self.model.uhid = response.data.uhid;
                                $rootScope.stopLoader();
                                //console.log(self.model.patientNames.itemName);
                                return $filter('filter')(self.model.patientNames, {
                                    $: search
                                });
                            } else {
                                $rootScope.stopLoader();
                                return UHIDNotFound(search);
                            }
                        }, function (err) {
                            $rootScope.stopLoader();
                            return UHIDNotFound(search);
                        });
                } else {
                    if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
                        self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
                        return UHIDNotFound(search);
                    } else {
                        return $filter('filter')(self.model.patientNames, {
                            $: search
                        });
                    }
                }
            }

            var UHIDNotFound = function (search) {
                var item = {
                    "itemFound": false,
                    "details": "UHID Not found with search " + search
                };
                self.model.patientNames = [item];
                return self.model.patientNames;
            }

            var ChangeVisitNumber = function (visitData) {
                self.model.encounterId = visitData.encounterId;
                self.model.admissionId = visitData.admissionId;
                self.model.doctorId = visitData.doctorId;
            }
    
            var AddPatientDetails = function (item) {
    
                if (!!item.patientId) {
                    var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_SEARCH_API +'/' + $scope.organizationId + '/'+$scope.unitId;
    
                    var data = {
                        "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : 0,
                        "patientId": !!item.patientId ? item.patientId : undefined,
                        // "uhid": !!item.uhid ? item.uhid : undefined,
                    };
    
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data)
                        .then(function (response) {
    
                                self.model.patientInfo = response.data,
                                self.model.patientName = response.data.patientName,
                                self.model.patientId = response.data.patientId,
                                self.model.uhid = response.data.uhid,
                                self.model.bedNo = response.data.bedNo,
                                self.model.bedId = response.data.bedId
                            // self.model.encounterId = response.data.encounterId,
                            // self.model.admissionId = response.data.admissionId
                            if(!!response.data.visitDetails.length){
                                if (self.model.visitType.id == 1) {
                                    self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
                                } else if (self.model.visitType.id != 1) {
                                    self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                                    self.model.encounterId = response.data.visitDetails[0].encounterId,
                                    self.model.admissionId = response.data.visitDetails[0].admissionId,
                                    self.model.doctorId = response.data.visitDetails[0].doctorId
                                }
                            } 
                            self.model.ward = self.model.wardList.find(function (ward) {
                                return response.data.wardId == ward.id;
                            });
    
    
                            $rootScope.stopLoader();
                        }, function (err) {
                            growl.error(err.data.summary ? err.data.summary : err.data.message);
                            $rootScope.stopLoader();
                        });
                }
            }
            
            var ClearPatientInfo = function (){
                self.model.patientData = null;
                self.model.patientId = null;
                self.model.bedNo = null;
                self.model.bedId = null;
                self.model.ward = self.model.wardList[0];
                self.model.visitNumberText = null;
                self.model.encounterId = null;
                self.model.admissionId = null;
                self.model.doctorId = null; 
                self.model.visitNumber = self.model.visitDetails.splice(1);
                self.model.visitNumber = self.model.visitDetails[0];
                self.model.patientInfo = null;
            }
    
            var ChangeVisitType = function(){
                self.model.patientName = null;   
                self.model.uhid = null;
                ClearPatientInfo();
                self.model.ItemDetailList = [];
            }
            
            var PatientNameKeyPress = function (event) {
                var search = $filter('filter')(self.model.patientNames, {
                    $: self.model.patientName
                });
                if (!self.model.patientName) {      
                    self.model.uhid = null;
                    ClearPatientInfo();
                }
            }
    
            var uHIDKeyPress = function (event) {
                var search = $filter('filter')(self.model.patientNames, {
                    $: self.model.uhid
                });
                if (!self.model.uhid) {
                    self.model.patientName = null;
                    ClearPatientInfo();
                }
            }

            var ValidateToGetItems = function () {
                var isValid = true;

                if (!self.model.storeId) {
                    self.model.isStoreValid = true;
                    isValid = false;
                    growl.error('Select store 1');
                }
                if(!self.model.visitType.id) {
                    self.model.isVisitTypeValid = true;
                    isValid = false;
                    growl.error('Select Visit Type');
                }
               
                return isValid;
            }

            var GetItemList = function () {
               /*if(ValidateToGetItems()) {*/
                GetAssetCategoryList();
                self.model.NewItemList = [];
                self.model.selectAllRows = false,
                self.model.itemSearch.itemCode = undefined;
                self.model.itemSearch.itemName = undefined;
                self.model.itemSearch.batchNo = undefined;
                self.model.pagination.data=[];
                self.model.pagination.totalPages = 0;
                self.model.selectAllRows = false;
                self.model.action = 'Open';
                var popup = angular.element('#ItemListPopUp');
                popup.modal('show');
               /* }*/
            }

            var GetAssetCategoryList = function () {
                var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

                self.model.assetCategory = self.model.assetCategories[0];
                self.model.productCategory = self.model.productCategories[0];

                self.model.assetCategories.splice(1);
                self.model.productCategories.splice(1);

                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        self.model.assetCategories = self.model.assetCategories.concat(response.data);
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
            }

            var GetProductCategoryList = function (id) {

                var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
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

            var SearchNewItem = function () { 

                // var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId; + '/false/false';
                var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;; 
    
                if(self.model.visitType){
                    if (self.model.visitType.id == 1) {
                        visitNumber = self.model.visitNumber.visitNo;
                    } else {
                        visitNumber = self.model.visitNumberText;
                    }
                }
                var data = {
                    "assetTypeId": 1,
                    "toStoreId": self.model.storeId ? self.model.storeId : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,

                    // "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    // "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    // "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    // "uhid": !!self.model.uhid ? self.model.uhid : undefined, 
                    // "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    // "bedNo": !!self.model.bedNo ? self.model.bedNo : undefined, 
                    // "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                    // "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                }
                self.model.NewItemList = [];
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.pagination.data=[];
					    self.model.pagination.totalPages=0;
                        self.model.NewItemList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                        self.model.SelectedItemsIds = [];
                        self.model.selectAllRows = false;
                        // var popup = angular.element('#ItemListPopUp');
					    // popup.modal('hide'); 
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
            }

            var SelectAllItems = function () {
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

            var isSelected = function (item) {
                return item.isSelected;
            }

            var getAllItemDetails = function () {
                var list = []
                self.model.NewItemList.forEach(function (item) {                    
                    if (item.isSelected == true) {
                        if(self.model.visitType.id == 2 || self.model.visitType.id == 3 || self.model.visitType.id == 4) {
                            var isUOM = item.uomResponses.find(function(x){
                                return !!x.ipUom;
                            })
                            if(!!isUOM){
                                list.push(item)
                            }
                        } else {
                            var isUOM = item.uomResponses.find(function(x){
                                return !!x.opUom;
                            })
                            if(!!isUOM){
                                list.push(item)
                            }
                        }                     
                    }
                });
                for(let item of list) {
                    self.model.ItemDetailList.push(item);
                }
                // self.model.ItemDetailList = list;
            }
            var PopupAction = function (action, modal) {
                var popup = angular.element('#' + modal);
                self.model.action = action;
                if (action == "Open") {
                    popup.modal('show');
                } else {
                    popup.modal('hide');
                }
            }
    
            var ConfirmRemove = function (index) {
                self.model.PopupAction('Open', 'confirmRemove');
                self.model.itemForRemove = index;
            }

            var RemoveRowData = function (index) {
                // var ItemDetailList = self.model.ItemDetailList;
                // self.model.ItemDetailList = ItemDetailList.slice(0, index).concat(ItemDetailList.slice(index + 1));
                self.model.ItemDetailList.splice(index, 1)
                self.model.PopupAction('Close', 'confirmRemove');
            }

            var ValidateQuantity = function (item) {
                var isValid = true;
                item['isQuantityValid'] = false;

                if (!item.quantity) {
                    item['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Please enter quantity')
                }

                if (item.availableQuantity < item.quantity) {
                    item['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the max available quantity');
                }

                if (item.quantity == 0) {
                    item['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Enter quantity greater than zero')
                }
                return isValid;
            }
            
            var Validate = function () {
                var isValid = true;

                self.model.isPatientNameValid = false;
                self.model.isVisitTypeValid = false;
                self.model.isVisitNoValid = false;
                self.model.isStoreValid = false;

                if (!self.model.patientName) {
                    self.model.isPatientNameValid = true;
                    isValid = false;
                    growl.error('Enter patient name')
                }
                if (!self.model.visitType.id) {
                    self.model.isVisitTypeValid = true;
                    isValid = false;
                    growl.error('Select visit type')
                }
                if (!self.model.store.id) {
                    self.model.isStoreValid = true;
                    isValid = false;
                    growl.error('Select Store * ')
                }
                if(self.model.visitType){
                    if (self.model.visitType.id == 1) {
                        if (!self.model.visitNumber.visitNo) {
                            self.model.isVisitNoValid = true;
                            isValid = false;
                            growl.error('Select visit no')
                        }
                        else if (self.model.visitNumber.visitNo == 'Select Visit No') {
                            self.model.isVisitNoValid = true;
                            isValid = false;
                            growl.error('Select visit no')
                        }
                    } else {
                        if (!self.model.visitNumberText) {
                            self.model.isVisitNoValid = true;
                            isValid = false;
                            growl.error('Select visit no')
                        }
                    }
                }

                var ItemDetailList = self.model.ItemDetailList;
                if (!ItemDetailList || ItemDetailList.length <= 0) {
                    isValid = false;
                    growl.error("Please add some items in the bill");
                } else {
                    ItemDetailList.forEach(function (item) {

                        item['isQuantityValid'] = false;

                        if (!item.quantity) {
                            item['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Please enter quantity')
                        }

                        if (item.availableQuantity < item.quantity) {
                            item['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Entered quantity is exceeding the max available quantity');
                        }

                        if (item.quantity == 0) {
                            item['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Enter quantity greater than zero')
                        }

                        if (!ValidateQuantity(item)){
                            isValid = false;
                        }
                    });
                }
                return isValid;
            }

            var SetStatus = function (x) {
                var status = self.model.Status.find(function (obj) {
                    return obj.status == x;
                });
    
                return status.id;
            }

            var SaveNewbillableConsumption = function () {
                var ItemDetailList = self.model.ItemDetailList;
                if (Validate()) {
                    var URI = CONSTANTS.PHARMACY.NURSING.BILLABLE_CONSUMPTION.SAVE_API;

                    var data = {
                        "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                        "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                        "admissionId": !!self.model.admissionId ? self.model.admissionId : undefined,
                        "encounterId": !!self.model.encounterId ? self.model.encounterId : undefined,
                        "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                        "storeId": !!self.model.store.id ? self.model.store.id : undefined,
                        "bedId": !!self.model.bedId ? self.model.bedId : undefined,
                        "statusId": SetStatus('Pending'),
                        "approvedStatusId": SetStatus('Pending'),
                        "items": []
                    }

                    var netAmount = 0;
                    var taxAmount = 0;
                    var totalAmount = 0;
                    var totalDiscountAmount = 0; 

                    angular.forEach(ItemDetailList, function (item, key) {
                        var item = {
                            "batchId": item.batchId,
                            "uomTypeId": item.uomTypeId,
                            "uomUnitId": item.uomUnitId,
                            "consumableQuantity": parseInt(item.quantity),
                            "statusId": SetStatus('Pending'),
                            "leaseQuantity": item.leaseQuantity,
                            "netAmount": item.netAmount,
                            "quantity": parseInt(item.quantity),
                            "rate": item.rate,
                            "taxAmount": item.taxAmount,
                            "taxId": item.taxId,
                            "taxPercentage": item.taxPercentage,
                            "totalAmount": item.totalAmount,
                            "productCategoryId":item.prodCatId,
                            "itemId":item.itemId,
                            "creditPayable":0,
                            "selfPayable":item.netAmount
                        }

                        netAmount = netAmount + item.netAmount;
                        taxAmount = taxAmount + item.taxAmount;
                        totalAmount = totalAmount + item.totalAmount;

                        data.items.push(item);
                    });

                    data.netAmount = netAmount;
                    data.roundOffAmount = parseFloat((netAmount - Math.round(netAmount)).toFixed(2));
                    data.taxAmount = taxAmount;
                    data.totalAmount = totalAmount;
                    data.totalDiscountAmount = totalDiscountAmount;                

                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            NavigteToBillableConsumption();
                            $rootScope.stopLoader();
                            growl.success('New billable consumption Successfully saved');
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Error while saving new billable consumption ');
                        });
                    }
            }

            var CalculateLeaseQuantity = function (item) {
                item.isQuantityValid = false;

                var conversion = 1;
                var flag = false;
        
                angular.forEach(item.uomResponses, function (value, key) {
                    if (value.uomTypeId == item.uomTypeId || flag) {
                        conversion = conversion * value.conversion;
                        flag = true;
                    }
                });
        
                item.leaseQuantity = conversion * parseInt(item.quantity);

                if(self.model.taxInclusive){
                    item.tax = (item.mrp * item.taxPercentage / 100);
                    item.rate = item.mrp - item.tax;
                } else {                    
                    item.rate = item.mrp;
                    item.tax = (item.mrp * item.taxPercentage / 100);
                }

                item.amount = item.leaseQuantity * item.rate;
                item.taxAmount = item.leaseQuantity * item.tax;
                item.netAmount = item.amount + item.taxAmount;
                item.totalAmount = item.amount + item.taxAmount;
            }

            var NavigteToBillableConsumption = function () {        
                    $state.go('billableConsumption');
            }

            // var NavigteToBillableConsumption = function () {
            //     document.getElementById('popupClose').click();
            //     setTimeout(function () {
            //         $state.go('billableConsumption');
            //     }, 1000);
            // }

            initializeController();
        }


        function config($stateProvider) {
            $stateProvider.state("addNewbillableConsumption", {
                url: "/addNewbillableConsumption",
                templateUrl: 'views/PharmacyInventory/InPatient/add-new-billable-consumption.html',
                controller: "AddNewbillableConsumption.Controller",
                controllerAs: "vm"
            });
        }

        angular
            .module("myApp")
            .config(config)
            .controller("AddNewbillableConsumption.Controller", addNewbillableConsumptionController);
    })();