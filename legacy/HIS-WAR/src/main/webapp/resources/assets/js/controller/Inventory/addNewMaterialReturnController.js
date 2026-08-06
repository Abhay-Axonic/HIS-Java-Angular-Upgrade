(function () {
    "use strict";

    function addNewMaterialReturnController($scope, $rootScope, $http, $state, commonDetailService, CONSTANTS,PaginationService, GenericService, growl,$cookies) {
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
                fromStores: [{
                    "store": "Select Stores"
                }],
                toStores: [{
                    "store": "Select Stores"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                carriers: [{
                    "name": "Select Carrier "
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                returnReasonList : [ {
					"reason" : "Select Return Reason"
				} ],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                   // maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    //maxDate: new Date()
                },
                fromDate: new Date(),
                toDate: new Date(),
                SelectedItemsIds: [],
                selectAllRows: false,
                NewItemList: [],
                itemList: [],
                dataList: []
            };
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.searchByNearExpiry = searchByNearExpiry;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.getAllItemDetails1 = getAllItemDetails1;
            self.model.isSelected = isSelected;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.GetItemList = GetItemList;
            self.model.SaveMaterialReturn = SaveMaterialReturn;
            self.model.NavigteToBack = NavigteToBack;
            self.model.getReturnReasonList = getReturnReasonList;
            self.model.GetMaterialReturnDetails = GetMaterialReturnDetails;
            self.model.removeRowData = removeRowData;
            self.model.onNearExpiryChange = onNearExpiryChange;
            self.model.confirmClear = confirmClear;
            self.model.SaveNewMaterialReturn = SaveNewMaterialReturn;
            self.model.UpdateMaterialReturn = UpdateMaterialReturn;
            self.model.validateSave = validateSave;
            self.model.itemRemovePopup = itemRemovePopup;

            self.model.SetOldValueFromStore = SetOldValueFromStore;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.ConfirmFromStorePopup = ConfirmFromStorePopup;

            self.model.SetOldValueToStore = SetOldValueToStore;
            self.model.ChangeToStore = ChangeToStore;
            self.model.ConfirmToStorePopup = ConfirmToStorePopup;

            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;

            self.model.SetOldValueIsConsignment = SetOldValueIsConsignment;
            self.model.ChangeIsConsignment = ChangeIsConsignment;
            self.model.ConfirmIsConsignmentPopup = ConfirmIsConsignmentPopup;

            self.model.SetOldValueNearExpiry = SetOldValueNearExpiry;
            self.model.ChangeNearExpiry = ChangeNearExpiry;
            self.model.ConfirmNearExpiryPopup = ConfirmNearExpiryPopup;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateReason = ValidateReason;
            self.model.Expand = Expand;
            self.model.SelectIndentItem = SelectIndentItem;
            self.model.getListByUserName = getListByUserName;
            getDropdownsList();
            getReturnReasonList();
        }

        $scope.common = {
            userId:"",
            empCode:"",
            empName:"",    
        };

        var getListByUserName = function(name){	
            var URI = BASE_URL + ROOT_URL + "/api/global/searchEmployeeByName";
            var data = {
                organizationId: $scope.organizationId,
                unitId: $scope.unitId,
                firstName: name
            }
            $rootScope.startLoader();
            return GenericService.serviceAction("POST",URI,data).then(function(response){
                if (response.data.status == "success")
                    $rootScope.stopLoader();
                    console.log("getListByUserName", response.data.listObject);
                return response.data.listObject;
            });
        }

        $scope.selectUserId = function(info, model,label) {
            console.log(info);
            $scope.common.userId = info.userId;
            $scope.common.empName = info.empName + '-' + info.empCode;
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            //self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
           // self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var Expand = function (item) {
            item.Expanded = !item.Expanded;
        }

        var getDropdownsList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var mreturn = self.model;
           // mreturn.fromStore = mreturn.fromStores[0];
            mreturn.toStore = mreturn.toStores[0];
            mreturn.carrier = mreturn.carriers[0];

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.ADD_NEW_DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    mreturn.fromStores = mreturn.fromStores.concat(response.data.fromStore);
                    mreturn.toStores = mreturn.toStores.concat(response.data.toStore);
                    mreturn.carriers = mreturn.carriers.concat(response.data.carrier);

                    var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                    var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
                    for(var i=0;i<mreturn.fromStores.length;i++)
                    {
                        if(mreturn.fromStores[i].id==selectedStoreId)
                        {
                            mreturn.fromStore = mreturn.fromStores[i];
                        }
                    }

                    $rootScope.stopLoader();
                    var mreturnId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!mreturnId) {
                        commonDetailService.setDataId(null);
                        GetMaterialReturnDetails(mreturnId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetAssetCategoryList = function () {
            var mreturn = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            mreturn.assetCategory = mreturn.assetCategories[0];
            mreturn.productCategory = mreturn.productCategories[0];

            mreturn.assetCategories.splice(1);
            mreturn.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    mreturn.assetCategories = mreturn.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var mreturn = self.model;

            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            mreturn.productCategory = mreturn.productCategories[0];
            mreturn.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    mreturn.productCategories = mreturn.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var getReturnReasonList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            self.model.reason = self.model.returnReasonList[0];
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.RETURN_REASON_API + "/" + $scope.unitId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    // mreturn.returnReasons = response.data.returnReason;
                    self.model.returnReasonList = self.model.returnReasonList
											.concat(response.data.returnReason);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function searchNewItem() {
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.SEARCH_API_NEW;
            const data = {
                "storeId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "issueNo": self.model.issueNo,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
            }

            // self.model.NewItemList = [];
            self.model.selectAllRows  =false;
            self.model.isItemChecked =false;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoaderPopup();
                // self.model.pagination.data=[];
                self.model.pagination.totalPages=0;
                self.model.NewItemList = response.data;
                    
                /* start code by minesh for club batch and consignment logic */
                let UniqueItemList = self.model.NewItemList;
                let batchNos=[];
                /* angular.forEach(self.model.NewItemList, function(item) {                    	
                    var index = batchNos.indexOf(item.batchNo);                    			 
                    if(index === -1)
                    {
                        batchNos.push(item.batchNo);
                        UniqueItemList.push(item);
                    }else{
                        UniqueItemList[index].availableQuantity += item.availableQuantity;
                        item.availableQuantity = UniqueItemList[index].availableQuantity ;
                    }
                });*/                    
                self.model.NewItemList = UniqueItemList;
                /* end code by minesh for club batch and consignment logic */
                    
                /*start code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
                angular.forEach(self.model.NewItemList,function(NewItemList,index){
                    if(!NewItemList.items)
                    {
                        self.model.NewItemList.splice(index,1);
                    }
                    angular.forEach(NewItemList.items,function(item,index){
                        item.availableReturnQty = item.issueQty-item.totalComsumedQty-item.totalReturnedQty  - parseInt(item.rejectQuantity )-item.totalTransitQty ; // HIS-11092
                        item.uom.find(function(d){
                            if(d.uomTypeId == item.uomTypeId){
                                item.uomType = d.uomDetails;
                            }
                        });
                    });
                }); 
                angular.forEach(self.model.NewItemList,function(NewItemList,index){
                    if(!NewItemList.items)
                    {
                        self.model.NewItemList.splice(index,1);
                    }
                    angular.forEach(NewItemList.items,function(item,index){
                    	if(item.availableReturnQty==undefined || item.availableReturnQty==null)
                    		{
                    		item.availableReturnQty = item.issueQty-item.totalComsumedQty-item.totalReturnedQty  - parseInt(item.rejectQuantity )-item.totalTransitQty ; // HIS-11092
                    		}
                    	 if(item.availableReturnQty==0)
                         {
                             NewItemList.items.splice(index,1);
                         }
                    })
                })
                /*end code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
                    
                if(!!response && !!response.data && response.data.length > 0){            
                    PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                }
                self.model.selectAllRows = false;
            },
            function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error('Error');
            });
        }
        
        var ValidateDate = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        function searchByNearExpiry() {
            if (ValidateDate()) {
                // var URI = CONSTANTS.GLOBAL.NEAR_EXPIRY;
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.Near_expiry_search_api;
                const data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    // "assetTypeId": 1,
                    "storeId": self.model.fromStore.id,
                    "toStoreId": self.model.toStore.id,
                    // "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
                    "unitId": $scope.unitId,
                    "organizationId": $scope.organizationId,
                    "userId": $scope.userId 		
                }
                self.model.selectAllRows = false;
                self.model.isItemChecked = false;
                self.model.NewItemList = [];
                $rootScope.startLoaderPopup();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    angular.forEach(response.data, function(d){
                        if(d.items != undefined){
                            angular.forEach(d.items, function(val){
                                val.uom.find(function(u){
                                    if(val.uomTypeId == u.uomTypeId){
                                        val.uomType = u.uomDetails;
                                    }
                                });
                            })
                            self.model.NewItemList.push(d);
                        }
                    });
                    $rootScope.stopLoaderPopup();
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
            }
        }

        /*var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    if(item.items != undefined && item.items != null && item.items.length > 0){
                    	item.items.forEach(function (data) {
                            data.isSelectedItem = true;
                            data.isItemChecked = true;
                        });
                    }
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.NewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
//            console.log("Selected Item Ids : ",self.model.pagination.data.item.id);
        }*/
        
        var SelectAllItems = function() {
			if (self.model.selectAllRows != undefined && self.model.selectAllRows != null  && self.model.selectAllRows == true) {
				self.model.SelectedItemsIds = self.model.NewItemList.map(function(item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    item.items.forEach(function(data){
                		data.isSelectedItem = true;
                	});
                    return item.id;
                });
			} else {
				self.model.SelectedItemsIds = [];
				self.model.NewItemList.forEach(function(item) {
					self.model.isItemChecked = false;
					item.isSelected = false;
                    item.items.forEach(function(data){
                		data.isSelectedItem = false;
                	});
				});
			}
		}

        var SelectItem = function (item, indent) {
            var itemIds = self.model.SelectedItemsIds;
            if(indent.length==0)
        	{
        	growl.error("No Pending Quantity")
        	item.isSelected=false;
        	}
            indent.forEach(function(data){
                if (item.isSelected) {
                    data.isSelectedItem = true;
                    itemIds.push(data.itemId);
                } else {
                    var index = itemIds.indexOf(data.itemId);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                    }
                }
            });

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

        var SelectIndentItem = function (item, indent) {
            var itemIds = self.model.SelectedItemsIds;
            if (item.isSelectedItem) {
                item.isSelectedItem = true;
                itemIds.push(item.itemId);
                self.model.isItemChecked = true;
            } else {
                item.isSelectedItem = false;
                var index = itemIds.indexOf(item.isSelectedItem);
                if (index > -1) {
                itemIds.splice(index, 1);
                }
            }
            if (indent.items.every(isSelectedItem)) {
                indent.isSelected = true;
                self.model.isItemChecked = true;
            } else {
                indent.isSelected = true;/*false*/
            }
        }

        var isSelectedItem = function (item) {
            return item.isSelectedItem;
        }

        var isSelected = function (item) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            return item.isSelected;
        }

        $scope.addBtnDisabled = false;
        function getAllItemDetails() {
            $scope.addBtnDisabled = true;
            $rootScope.startLoader();
            self.model.getIssueIdList=[];
            self.model.itemList=[];
            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                	item.items.forEach(function(data){
                		if(data.isSelectedItem == true){
							self.model.getIssueIdList.push(data.issuedetailsid);
						}
                	});
                }
            });
            
            var URI =CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.GET_MATERIAL_RETURN_AGAINST_ISSUE_NUMBER+self.model.getIssueIdList+"/"+self.model.fromStore.id+"/"+self.model.toStore.id;
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                // ClearRequests();
                $rootScope.stopLoader();
                if(response.data.object>0 && $scope.isFromEdit==false)
                {
                    growl.error("Issue Number already Exist");
                    $scope.addBtnDisabled = false;
                    return false;
                }
                else
                {
                    $rootScope.startLoader();               
                    self.model.NewItemList.forEach(function (item) {
                        if (item.isSelected == true) {
                            item.items.forEach(function(data){
                                if(data.isSelectedItem == true){
                                    data.comsumedQty=0;
                                    data.issueNo = item.issueno;
                                    data.issueDate = item.isuedate;
                                    data.issuedetailsid = data.issuedetailsid ? data.issuedetailsid : item.issuedetailsid;
                                    if(data.totalTransitQty == undefined){
                                        data.totalTransitQty = 0;
                                    }
                                    if(data.returnQuantity == undefined || data.returnQuantity == null || data.returnQuantity == ""){
                                        data.returnQuantity = 0;
                                    }
                                    data.availableReturnQty = data.acceptQuantity - data.totalComsumedQty - data.totalReturnedQty-data.totalTransitQty;
                                    if(data.isThreeUOMApplicable == false){
                                        if(data.uomTypeId == 1){
                                            data.receivedQtyCon = (data.returnQuantity +"*"+ data.uom[0].conversion);
                                            data.receivedQtyCalculation = (data.returnQuantity * data.uom[0].conversion);
                                            data.uomUnitData = data.uom[2].uomUnit;
                                        }
                                        else if(data.uomTypeId == 2){

                                        }
                                        else{
                                            data.receivedQtyCon = (data.returnQuantity);
                                            data.receivedQtyCalculation = (data.returnQuantity);
                                            data.uomUnitData = data.uom[2].uomUnit;
                                        }
                                    }
                                    else{
                                        if(data.uomTypeId == 1){
                                            data.receivedQtyCon = (data.returnQuantity +"*"+ data.uom[0].conversion +"*"+ data.uom[1].conversion);
                                            data.receivedQtyCalculation = (data.returnQuantity * data.uom[0].conversion * data.uom[1].conversion);
                                            data.uomUnitData = data.uom[2].uomUnit;
                                        }
                                        else if(data.uomTypeId == 2){
                                            data.receivedQtyCon = (data.returnQuantity +"*"+ data.uom[1].conversion);
                                            data.receivedQtyCalculation = (data.returnQuantity * data.uom[1].conversion);
                                            data.uomUnitData = data.uom[2].uomUnit;
                                        }
                                        else{
                                            data.receivedQtyCon = (data.returnQuantity);
                                            data.receivedQtyCalculation = (data.returnQuantity);
                                            data.uomUnitData = data.uom[2].uomUnit;
                                        }
                                    }
                                    self.model.itemList.push(data);
                                }
                                $rootScope.stopLoader();
                            });
                        }
                    });
                    var popup = angular.element('#additemlist');
                    popup.modal('hide');
                    $scope.addBtnDisabled = false;
                }            
            },
            function (err) {
                $rootScope.stopLoader();
                $scope.addBtnDisabled = false;
                growl.error('Something Went Wrong');
            });    
        }

        $scope.addBtnDisabledNearExp = false;
        function getAllItemDetails1() {
            $scope.addBtnDisabledNearExp = true;
            $rootScope.startLoader();
            self.model.NewItemList.forEach(function (item) {
                item.items.forEach(function(data){
                    if(data.isSelectedItem == true){    
                        data.issueNo = item.issueno;
                        data.issueDate = item.isuedate;
                        data.issuedetailsid = data.issuedetailsid ? data.issuedetailsid : item.issuedetailsid;
                        if(data.totalTransitQty == undefined || data.totalTransitQty == null){
                            data.totalTransitQty = 0;
                        }
                        if(data.availableReturnQty == undefined || data.availableReturnQty == null){
                            data.availableReturnQty = 0;
                        }
                        if(data.returnQuantity == undefined || data.returnQuantity == null || data.returnQuantity == ""){
                            data.returnQuantity = 0;
                        }
                        // data.availableReturnQty = data.availableReturnQty - data.totalTransitQty;
                        data.availableReturnQty = data.acceptQuantity - data.totalComsumedQty - data.totalReturnedQty - data.totalTransitQty;
                        if(data.isThreeUOMApplicable == false){
                            if(data.uomTypeId == 1){
                                data.receivedQtyCon = (data.returnQuantity +"*"+ data.uom[0].conversion);
                                data.receivedQtyCalculation = (data.returnQuantity * data.uom[0].conversion);
                                data.uomUnitData = data.uom[2].uomUnit;
                            }
                            else if(data.uomTypeId == 2){

                            }
                            else{
                                data.receivedQtyCon = (data.returnQuantity);
                                data.receivedQtyCalculation = (data.returnQuantity);
                                data.uomUnitData = data.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(data.uomTypeId == 1){
                                data.receivedQtyCon = (data.returnQuantity +"*"+ data.uom[0].conversion +"*"+ data.uom[1].conversion);
                                data.receivedQtyCalculation = (data.returnQuantity * data.uom[0].conversion * data.uom[1].conversion);
                                data.uomUnitData = data.uom[2].uomUnit;
                            }
                            else if(data.uomTypeId == 2){
                                data.receivedQtyCon = (data.returnQuantity +"*"+ data.uom[1].conversion);
                                data.receivedQtyCalculation = (data.returnQuantity * data.uom[1].conversion);
                                data.uomUnitData = data.uom[2].uomUnit;
                            }
                            else{
                                data.receivedQtyCon = (data.returnQuantity);
                                data.receivedQtyCalculation = (data.returnQuantity);
                                data.uomUnitData = data.uom[2].uomUnit;
                            }
                        }
                        self.model.itemList.push(data);
                    }
                });
            });
            $scope.addBtnDisabledNearExp = false;
            $rootScope.stopLoader();
        }
        
        

        function GetItemList() {
            self.model.NewItemList = [];
            self.model.pagination.data=[];
            self.model.isItemChecked = false;
            self.model.selectAllRows = false;
            self.model.SelectedItemsIds = [];
            self.model.search = {};
        	if(self.model.fromStore.id == undefined || self.model.fromStore.id == null || self.model.fromStore.id =='')
			{
			growl.error("Please Select Stores",
					{
						title : 'Error!'
					});
			    return false;
			 }
            if (self.model.fromStore.id == self.model.toStore.id) {
                growl.error('Please Select Different Stores')
            }
            else {
                if (self.model.nearExpiry == true) {
                    self.model.selectAllRows = false;
                    self.model.fromDate = new Date();
                    self.model.toDate = new Date();
                    self.model.search = '';
                    self.model.action = 'Open';
                    var popup = angular.element('#nearExpiry1');
                    popup.modal('show');
                }
                else {
                    self.model.action = 'Open';
                    var popup = angular.element('#additemlist');
                    popup.modal('show');
                    GetAssetCategoryList();
                }
                self.model.NewItemList = [];
            }
        }
        var SaveNewMaterialReturn = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            $scope.saveDisabled = false;
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.SAVE_API;

            var data = {
                "fromStoreId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
                "carrierId": $scope.common.userId,
                "statusId": 1,
                "remark": self.model.remark,
                "approvalStatusId": 1,
                "nearByExpiry": self.model.nearExpiry ? self.model.nearExpiry : false,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId

            };

            angular.forEach(store.itemList, function (item, key) {
                var storeItem = {
                    "batchId": item.batchId,
                    "returnQuantity": parseInt(item.returnQuantity),
                    "returnReasonId": item.returnReason.id,
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "statusId": 1,
                    "itemId": item.itemId,
                    "issueQty": item.issueQty,
                    "comsumedQty": item.comsumedQty,
                    "issueDetailId": item.issuedetailsid
                }
                data.items.push(storeItem);

            });
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    // ClearRequests();
                    $rootScope.stopLoader();
                    NavigteToBack();
                    if(response.data.exceptionMessage!=undefined && response.data.exceptionMessage!=null && response.data.exceptionMessage.length>0 )
                    {
                       growl.error(response.data.exceptionMessage);     
                    }
                    else
                    {
                       growl.success(response.data.message);     
                    }
                    $scope.saveDisabled = true;
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var NavigteToBack = function () {
            $state.go('materialReturn');
        }

        var onNearExpiryChange = function () {
            if (self.model.itemList.length > 0) {
                $("#confirmLogout1").modal()
            }
        }
        var confirmClear = function () {
            self.model.itemList = [];

        }
        $scope.isFromEdit=false;
        var GetMaterialReturnDetails = function (mreturnId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.DETAILS_API + $scope.unitId + '/' + mreturnId;
            $scope.isFromEdit=true;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.mreturnId = response.data.id;
                    self.model.remark = response.data.remark;
                    self.model.itemList = [];
                    self.model.itemList = response.data.items;

                    self.model.fromStore = self.model.fromStores.find(function (mreturn) {
                        return response.data.fromStoreId == mreturn.id;
                    });
                    self.model.toStore = self.model.toStores.find(function (mreturn) {
                        return response.data.toStoreId == mreturn.id;
                    });

                    self.model.carrier = self.model.carriers.find(function (mreturn) {
                        return response.data.carrierId == mreturn.id;
                    });
                    
                    if (self.model.carrier) {
                        $scope.common.empName = self.model.carrier.name;
                    } else {
                        $scope.common.empName = '';
                    }

                    angular.forEach(self.model.itemList, function (item, key) {
                        item.returnReason = self.model.returnReasonList.find(function (mreturn) {
                            return mreturn.id == item.returnReasonId;
                        });
                        
                        /*start code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
                        item.availableReturnQty = item.acceptQuantity-item.totalComsumedQty-item.totalReturnedQty - parseInt(item.comsumedQty ? item.comsumedQty : 0) - parseInt(item.returnQuantity ? item.returnQuantity : 0);
                        /*end code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
                        item.uom.find(function(d){
                            if(item.uomTypeId == d.uomTypeId){
                                item.uomType = d.uomDetails;
                            }
                        })
                        if(item.isThreeUOMApplicable == false){
                            if(item.uomTypeId == 1){
                                item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion);
                                item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){

                            }
                            else{
                                item.receivedQtyCon = (item.returnQuantity);
                                item.receivedQtyCalculation = (item.returnQuantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uomTypeId == 1){
                                item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.returnQuantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.returnQuantity);
                                item.receivedQtyCalculation = (item.returnQuantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    });
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }


        function removeRowData(item) {
            var index = self.model.itemList.indexOf(item);
            self.model.itemList.splice(index, 1);
        }

        $scope.saveDisabled = true;
        $scope.saveBtnDisabled=false;
        var SaveMaterialReturn = function () {
            $scope.saveDisabled = false;
            $scope.saveBtnDisabled=true;
            if (validateSave()) {
                $scope.saveDisabled = false;
                if (!self.model.mreturnId) {
                    SaveNewMaterialReturn();
                } else {
                    UpdateMaterialReturn();
                }
            }
        }
        var UpdateMaterialReturn = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            $scope.saveDisabled = false;
            var mreturn = self.model;

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.UPDATE_API + mreturn.mreturnId;
            var data = {
                "carrierId": $scope.common.userId,
                "remark": self.model.remark,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "items": []
            };

            angular.forEach(self.model.itemList, function (item, key) {
                var storeItem = {
                    "id": item.id,
                    "returnQuantity": parseInt(item.returnQuantity),
                    "returnReasonId": item.returnReason.id,
                    "itemId": item.itemId,
                    "issueQty": item.issueQty,
                    "comsumedQty": item.comsumedQty,
                    "issueDetailId": item.issuedetailsid
                }

                if (item.id == '' || item.id == null) {
                        storeItem.batchId = item.batchId,
                        storeItem.returnQuantity = item.returnQuantity,
                        storeItem.returnReasonId = item.returnReason.id,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.statusId = 1
                }
                data.items.push(storeItem);


            });
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    NavigteToBack();
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    $scope.saveDisabled = true;
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var itemRemovePopup = function (item) {
            self.model.itemRemove = '';
            self.model.itemRemove = item;
            $("#itemRemovePopup").modal()

        }

        var validateSave = function () {
            var isValid = true;
            var ItemDetailList = self.model.itemList;
            if (ItemDetailList.length < 1) {
                growl.error("Please validate all required fields");
                isValid = false;
                $scope.saveDisabled = true;
            }
            if (self.model.fromStore.id == self.model.toStore.id) {
                growl.warning("Please Select Different Stores");
                isValid = false;
                $scope.saveDisabled = true;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                    isValid = false;
                    $scope.saveDisabled = true;
                }
                if (!ValidateReason(ItemDetail)) {
                    isValid = false;
                    $scope.saveDisabled = true;
                }
            }

            if (!$scope.common.userId) {
                growl.error("Please Select Carrier");
                isValid = false;
                $scope.saveDisabled = true;
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }


        var SetOldValueFromStore = function (fromStore) {
            self.model.oldFromStore = fromStore;
            self.model.fromStore = self.model.oldFromStore;
        }

        var ChangeFromStore = function (fromStore) {
            if (!!self.model.itemList.length) {
                self.model.fromStore = self.model.oldFromStore;
                self.model.fromStoreNew = JSON.parse(JSON.stringify(fromStore));
                $("#storeChange").modal()
            }
        }

        var ConfirmFromStorePopup = function () {
            self.model.fromStore = self.model.fromStoreNew;
            self.model.itemList = [];
            ModalDismiss('storeChange');
        }


        var SetOldValueToStore = function (toStore) {
            self.model.oldToStore = toStore;
            self.model.toStore = self.model.oldToStore;
        }

        var ChangeToStore = function (toStore) {
            if (!!self.model.itemList.length) {
                self.model.toStore = self.model.oldToStore;
                self.model.toStoreNew = JSON.parse(JSON.stringify(toStore));
                $("#tostoreChange").modal()
            }
        }

        var ConfirmToStorePopup = function () {
            self.model.toStore = self.model.toStoreNew;
            self.model.itemList = [];
            ModalDismiss('tostoreChange');
        }


        var ConfirmRemove = function (item) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = item;
        }

        var RemoveItem = function (item) {
            var newArray = [];

            self.model.itemList.map(function (obj) {
                if (obj !== item) {
                    newArray.push(obj);
                }
            });
            self.model.itemList = newArray;
            var popup = angular.element('#itemRemovePopup');
            popup.modal('hide');
        }


        var SetOldValueIsConsignment = function (isConsignment) {
            self.model.oldIsConsignment = isConsignment;
            self.model.isConsignment = self.model.oldIsConsignment;
        }

        var ChangeIsConsignment = function (isConsignment) {
            if (!!self.model.itemList.length) {
                self.model.isConsignment = self.model.oldIsConsignment;
                self.model.isConsignmentNew = JSON.parse(JSON.stringify(isConsignment));
                $("#IsConsignment").modal()
            }
        }

        var ConfirmIsConsignmentPopup = function () {
            self.model.isConsignment = self.model.isConsignmentNew;
            self.model.itemList = [];
            ModalDismiss('IsConsignment');
        }


        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }


        var SetOldValueNearExpiry = function (nearExpiry) {
            self.model.oldNearExpiry = nearExpiry;
            self.model.nearExpiry = self.model.oldNearExpiry;
        }

        var ChangeNearExpiry = function (nearExpiry) {
            if (!!self.model.itemList.length) {
                self.model.nearExpiry = self.model.oldNearExpiry;
                self.model.nearExpiryNew = JSON.parse(JSON.stringify(nearExpiry));
                $("#NearExpiry").modal()
            }
        }

        var ConfirmNearExpiryPopup = function () {
            self.model.nearExpiry = self.model.nearExpiryNew;
            self.model.itemList = [];
            ModalDismiss('NearExpiry');
        }

        var ValidateQuantity = function (item) {
            var isValid = true;

            // if (isNaN(parseFloat(mreturn.returnQuantity))) {
            //     mreturn.isQuantityValid = true;
            //     isValid = false;
            //     growl.error("Please Enter Return Quantity");
            // } else if (parseInt(mreturn.returnQuantity) == 0) {
            //     mreturn.isQuantityValid = true;
            //     isValid = false;
            //     growl.error('Return Quantity cannot be zero');
            // } else if (parseInt(mreturn.returnQuantity) > mreturn.availableQuantity) {
            //     mreturn.isQuantityValid = true;
            //     isValid = false;
            //     growl.error('Entered return quantity is exceeding the stock available quantity');
            // } else {
            //     mreturn.returnQuantity = parseInt(mreturn.returnQuantity);
            //     isValid = true;
            // }
            if(item.patientReturnedQuantity == undefined){
                item.patientReturnedQuantity = 0;
            }
            item.isReturnQuantityValid = false;
            if(item.acceptQuantity == undefined || item.acceptQuantity == null || item.acceptQuantity == ""){
                item.acceptQuantity = 0;
            }
            if(item.totalTransitQty == undefined || item.totalTransitQty == null || item.totalTransitQty == ""){
                item.totalTransitQty = 0;
            }
            //item.returnQuantity = item.issueQty - parseInt(item.comsumedQty) - item.patientReturnedQuantity;
            
            /*start code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
            /*if(item.changeconsumedQty){
            	item.returnQuantity = item.issueQty - parseInt(item.comsumedQty) - item.patientReturnedQuantity;
            	item.changeconsumedQty = false;
            }else if(item.changeReturnQty){
            	item.comsumedQty = item.issueQty - parseInt(item.returnQuantity) - item.patientReturnedQuantity;
            	item.changeReturnQty = false;
            }*/
            //as per new requirement 
           // item.availableReturnQty = item.issueQty - parseInt(item.comsumedQty) - item.patientReturnedQuantity;
         //   item.availableReturnQty = item.issueQty - parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty) - item.patientReturnedQuantity;
            //HIS-14430   - item.patientReturnedQuantity
            item.availableReturnQty = (item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);
                if(item.availableReturnQty < 0)
            {
              isValid = false;
				growl.error("Please enter valid quantity");
				item.comsumedQty=0;
				item.returnQuantity=0;
				item.availableReturnQty = (item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);
	              
            }
            // item.availableReturnQty = item.acceptQuantity-item.totalComsumedQty-item.totalReturnedQty - parseInt(item.comsumedQty ? item.comsumedQty : 0 ) - item.patientReturnedQuantity - item.returnQuantity;
            /*end code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
           //  item.returnQuantity = Number(item.returnQuantity);
                item.returnQuantity = Number(item.returnQuantity!=undefined ? item.returnQuantity :0);
			if ((Number(item.returnQuantity) > (item.acceptQuantity - item.comsumedQty) - item.patientReturnedQuantity)) {
				item.isReturnQuantityValid = true;
				isValid = false;
				growl.error('Entered quantity is exceeding the max returned quantity');
//				item.availableReturnQty=item.acceptQuantity -item.totalReturnedQty;


				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);

//				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);


				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);

			} else if (!item.returnQuantity || item.returnQuantity == 0) {
				item.isReturnQuantityValid = true;
				isValid = false;
				growl.error("Please enter quantity greater than zero");
//				item.availableReturnQty=item.acceptQuantity-item.totalReturnedQty;


				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);

//				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);


				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);

			}
			if(isValid==true)
				{
				item.availableReturnQty=(item.acceptQuantity -item.totalComsumedQty -item.totalReturnedQty-item.totalTransitQty) - (parseInt(item.comsumedQty == undefined ? 0 :item.comsumedQty)- item.patientReturnedQuantity )-parseInt(item.returnQuantity!=undefined ? item.returnQuantity : 0);
				}
			var issueQty = item.issueQty - item.patientReturnedQuantity;
			var consumeqty = parseInt(item.comsumedQty);
			var returnQuantity =item.returnQuantity;
			var availableReturnQty = parseInt(item.availableReturnQty);

			if(issueQty <= consumeqty || consumeqty < 0){
				growl.error("Please enter valid consumed quantity");
				item.comsumedQty = 0;
			}
			/*start code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
			if(issueQty < returnQuantity || returnQuantity < 0){
				growl.error("Please enter valid return quantity");
				//item.returnQuantity = '';
				isValid = false;
			}
			
			/*end code for resolved HIS-9310 Material return - Updating return quantity should update the qty consumed column*/
			/* start code for resolved HIS-10134 Material Return : Available Qty Showing NaN After Enter Consumption Qty */
			//item.availableReturnQty = item.issueQty-item.totalComsumedQty-item.totalReturnedQty - parseInt(item.comsumedQty ? item.comsumedQty : 0 ) - item.patientReturnedQuantity;
			/* start code for resolved HIS-10134 Material Return : Available Qty Showing NaN After Enter Consumption Qty */

            return isValid;
        }

        var ValidateReason = function (mreturn) {
            var isValid = true;


            if(mreturn.returnReason == undefined)
            {
              mreturn.isReasonValid = true;
                isValid = false;
                growl.error("Please Select Reason");	
            }
            else  if (!mreturn.returnReason.id) {
                mreturn.isReasonValid = true;
                isValid = false;
                growl.error("Please Select Reason");
            }
            return isValid;
        }

        $scope.calculateReceivedQty = function (item) {
            if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
                item.returnQuantity = 0;
            }
            if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                
                }
                else{
                    item.receivedQtyCon = (item.returnQuantity);
                    item.receivedQtyCalculation = (item.returnQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
            }
            else{
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
                else{
                    item.receivedQtyCon = (item.returnQuantity);
                    item.receivedQtyCalculation = (item.returnQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
            }
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewMaterialReturn", {
            url: "/addNewMaterialReturn",
            templateUrl: 'views/PharmacyInventory/InPatient/add-new-material-return.html',
            controller: "AddNewMaterialReturn.Controller",
            controllerAs: "vm",
            title: 'Material Return'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewMaterialReturn.Controller", addNewMaterialReturnController);
})(); 