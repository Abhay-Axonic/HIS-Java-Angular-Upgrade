(function () {
  "use strict";

  function addNewImplantIndentController($rootScope, StatusService, $scope, $http, $filter, $cookies, CONSTANTS, $state, GenericService,PaginationService, CurrencyService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); 
    if(cookieObject == undefined){ 
         $state.go('login'); 
          return;
    }
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId; 
    
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
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        storeList: [{
          "store": "Select Store"
        }],
        toStoreList: [{
          "store": "Select Store"
        }],
        priorityList: [{
          "priority": "Select Priority"
        }],
        visitTypeList: [{
          "type": "Select VisitType"
        }],

        SelectedItemsIds: [],
        itemList: [],
        ItemDetailList: [],

      };

      self.model.SearchpatientNames = SearchpatientNames;
      self.model.SearchUhid = SearchUhid;
      self.model.PatientNameKeyPress = PatientNameKeyPress;
      self.model.uHIDKeyPress = uHIDKeyPress;
      self.model.AddPatientDetails = AddPatientDetails;
      self.model.GetItemList = GetItemList;
      self.model.ValidateDetails = ValidateDetails;
      self.model.ValidateIndentQuantity = ValidateIndentQuantity;
      self.model.GetImplantIndentDetails = GetImplantIndentDetails;
      self.model.NavigteToBack = NavigteToBack;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.SearchNewItem = SearchNewItem;
      self.model.GetAllItemDetails = GetAllItemDetails;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.RemoveItem = RemoveItem;
      self.model.SaveImplantIndent = SaveImplantIndent;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.SetOldValueToStore = SetOldValueToStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ChangeToStore = ChangeToStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueIsConsignment = SetOldValueIsConsignment;
      self.model.ChangeIsConsignment = ChangeIsConsignment;
      self.model.ConfirmIsConsignmentPopup = ConfirmIsConsignmentPopup;
      self.model.SetOldValueIsImplant = SetOldValueIsImplant;
      self.model.ChangeIsImplant = ChangeIsImplant;
      self.model.ConfirmIsImplantPopup = ConfirmIsImplantPopup;
      self.model.SetOldValueIsKit = SetOldValueIsKit;
      self.model.ChangeIsKit = ChangeIsKit;
      self.model.ConfirmIsKitPopup = ConfirmIsKitPopup;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.PopupAction = PopupAction;
      self.model.ChangeVisitType = ChangeVisitType;
      PopulateDropdown();
    }

    var setStatus = function (x) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    StatusService.GetStatus().then(function (status) {
      self.model.statusList = status;
    });

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();

        self.model.store = self.model.storeList[0];
        self.model.toStore = self.model.toStoreList[0];
        self.model.priority = self.model.priorityList[0];
        self.model.visitType = self.model.visitTypeList[0];

        var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {

            self.model.storeList = self.model.storeList.concat(response.data.toStore);
            self.model.toStoreList = self.model.toStoreList.concat(response.data.store);
            self.model.priorityList = self.model.priorityList.concat(response.data.priority);
            self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);

            var indentId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!indentId) {
              commonDetailService.setDataId(null);
              GetImplantIndentDetails(indentId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }


    var GetImplantIndentDetails = function (indentId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.DETAILS_API +$scope.unitId+ "/"+$scope.storeId +"/" + indentId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {

          self.model.indentId = response.data.id;

          self.model.store = self.model.storeList.find(function (store) {
            return store.id == response.data.storeId;
          });

          self.model.priority = self.model.priorityList.find(function (priority) {
            return priority.id == response.data.priorityId;
          });
          self.model.visitType = self.model.visitTypeList.find(function (visitType) {
            return visitType.id == response.data.visitedTypeId;
          });
          self.model.uhid = response.data.uhid;
          self.model.patientName = response.data.patientName;
          self.model.indentTypeId = response.data.indentTypeId;
          self.model.doctorId = response.data.doctorId;
          self.model.patientId = response.data.patientId;
          self.model.admissionId = response.data.admissionId;
          self.model.isConsignment = response.data.isConsignmnt;
          self.model.isImplant = response.data.isImplant;
          self.model.isKit = response.data.isKit;
          self.model.remark = response.data.remark;

          self.model.ItemDetailList = response.data.items;

          var conversionPurchase;
          var conversionStock;
          var conversionDespense;

          self.model.ItemDetailList.forEach(function (item) {
            if (item.assetCategoryId == 2) {
              item.newStockAvailableQuantity = item.stockAvailable;
            }
            else {
              if (item.uom) {
                item.uom.forEach(function (uom) {
                  if (uom.uomTypeId == 1) {
                    conversionPurchase = uom.conversion;
                  }
                  else if (uom.uomTypeId == 2) {
                    conversionStock = uom.conversion
                  }
                  else {
                    conversionDespense = uom.conversion
                  }

                });

                if (item.uomTypeId == 1) {
                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
                }
                else if (item.uomTypeId == 2) {
                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
                }
                else {
                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
                }
              }
            }

          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }


    var ValidateDetails = function () {
      var isValid = true;

      if (!self.model.store.id) {
        isValid = false;
        self.model.isStoreValid = true;
      }
      if (!self.model.store.id) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var GetItemList = function () {
      if (ValidateDetails()) {
        self.model.selectAllRows = false;
        self.model.NewItemList = [];
        self.model.pagination.data=[];
        self.model.pagination.totalPages=0;
        self.model.action = 'Open';
        var popup = angular.element('#additemlist');
        popup.modal('show');
        self.model.isItemChecked = false;
        self.model.search = {};
        PopulateAssetCategoryDropdown();
      }
    }

    var PopulateAssetCategoryDropdown = function () {
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
      self.model.selectAllRows = false;
      self.model.isItemChecked = false;
      self.model.SelectedItemsIds = [];
     if(self.model.isImplant){
      var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.SEARCH_ITEM_IMPLANT;

      }else{
        var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.SEARCH_ITEM;
      }
     

      const data = {
        "assetTypeId": 1,
        "storeId": self.model.store.id ? self.model.store.id : undefined,
        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
        "isConsignment": self.model.isConsignment ? self.model.isConsignment.id : undefined,
        "unitId":$scope.unitId		
      }
      self.model.NewItemList = [];
      self.model.pagination.data=[];
      self.model.pagination.totalPages=0;
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.NewItemList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
    }
    var GetAllItemDetails = function () {

      self.model.NewItemList.forEach(function (item) {
        if (item.isSelected == true) {
          var isFound = self.model.itemList.find(function (x) {
            return x.batchId == item.batchId;

          });
          if (!isFound) {
            item.mrp = item.mrp.toFixed(2);
            self.model.itemList.push(item);
          }
          else {
            growl.error('Item already present in the list')
          }
        }
      });
    }

    var AddItems = function () {
      if (!self.model.SelectedItemsIds.length) {
        growl.error('Please select items');
      } else {
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id
        };
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function (item) {

              var isFound = self.model.ItemDetailList.find(function (x) {
                return x.itemId == item.itemId;
              });
              if (!isFound) {
                self.model.ItemDetailList.push(item);
              } else {
                growl.error('Item Present In the List')
              }

            });
            self.model.SelectedItemsIds = [];
            self.model.items = [];
            var selectedUom;
            angular.forEach(self.model.ItemDetailList, function (x) {
              if (x.assetCategoryId == 2) {
                //  x.newStockAvailableQuantity=25;
                x.newStockAvailableQuantity = x.stockAvailable;
              }
              else {
                if (x.uom) {
                  selectedUom = x.uom.find(function (sUom) {
                    return sUom.storeUom == true;
                  });
                  x["uomType"] = selectedUom.uomType;
                  x["uomUnit"] = selectedUom.uomUnit;
                  x["uomTypeId"] = selectedUom.uomTypeId;
                  x["uomUnitId"] = selectedUom.uomUnitId;
                }
                else {
                  x["uomType"] = x.uomType;
                  x["uomUnit"] = x.uomUnit;
                  x["uomTypeId"] = x.uomTypeId;
                  x["uomUnitId"] = x.uomUnitId;
                }

                var conversionPurchase;
                var conversionStock;
                var conversionDespense;
                self.model.itemArray = [];
                self.model.ItemDetailList.forEach(function (item) {
                  item.uom.forEach(function (uom) {
                    if (uom.uomTypeId == 1) {
                      conversionPurchase = uom.conversion;
                    }
                    else if (uom.uomTypeId == 2) {
                      conversionStock = uom.conversion
                    }
                    else {
                      conversionDespense = uom.conversion
                    }

                  });

                  if (item.uomTypeId == 1) {
                    item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                    item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
                    item.newCurrenMonthSale = Math.round((((item.currentMonthSale / conversionPurchase) / conversionStock) / conversionDespense));
                    item.newLastMonthSale = Math.round((((item.lastMonthSale / conversionPurchase) / conversionStock) / conversionDespense));
                    item.newStockInTransit = Math.round((((item.stockInTransit / conversionPurchase) / conversionStock) / conversionDespense));
                  }
                  else if (item.uomTypeId == 2) {
                    item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
                    item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
                    item.newCurrenMonthSale = Math.round((item.currentMonthSale / conversionStock) / conversionDespense);
                    item.newLastMonthSale = Math.round((item.lastMonthSale / conversionStock) / conversionDespense);
                    item.newStockInTransit = Math.round((item.stockInTransit / conversionStock) / conversionDespense);
                  }
                  else {
                    item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
                    item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
                    item.newCurrenMonthSale = Math.round(item.currentMonthSale / conversionDespense);
                    item.newLastMonthSale = Math.round(item.lastMonthSale / conversionDespense);
                    item.newStockInTransit = Math.round(item.stockInTransit / conversionDespense);
                  }
                });
              }


            });

            $rootScope.stopLoader();
            // getAllItemDetails();
            self.model.searchNewItemList = [];
            var popup = angular.element('#addNewItemWasteStore');
            popup.modal('hide');
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
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

    var ValidateIndentQuantity = function (item) {
      var isValid = true;
      if (!item.indentQuantity) {
        item.isIndentQuantityValid = true;
        isValid = false;
        growl.error("Please Enter Indent Quantity.");

      } else if (parseInt(item.indentQuantity) == 0) {
        item.isIndentQuantityValid = true;
        isValid = false;
        growl.error('Indent Quantity Cannot Be Zero');
      }
      else if (item.indentQuantity > item.newStockAvailableQuantity) {
        growl.error('Indent Quantity should be less than or equal to available quantity');
        item.isIndentQuantityValid = true;
        isValid = false;
      }
      return isValid;
    }

    var validateSave = function () {
      var isValidItem = true;
      var items = self.model.ItemDetailList;
      if (!items.length) {
        growl.error('Please add items');
        isValidItem = false;
      }
      else {
        angular.forEach(items, function (item) {
          if (!item.indentQuantity) {
            item.isIndentQuantityValid = true;
            growl.error("Please Enter Indent Quantity.");
            isValidItem = false;
          }
          else if (parseInt(item.indentQuantity) == 0) {
            item.isIndentQuantityValid = true;
            isValidItem = false;
            growl.error('Indent Quantity Cannot Be Zero');
          }
          else if (item.indentQuantity > item.newStockAvailableQuantity) {
            growl.error('Indent Quantity should be less than or equal to available quantity');
            item.isIndentQuantityValid = true;
            isValidItem = false;
          }
        })
      }
      return isValidItem;
    }

    var SaveImplantIndent = function () {
      if (validateSave()) {
        if (!self.model.indentId) {
          saveNewImplantIndentRequest();
        } else {
          UpdateImplantIndent();
        }
      }
    }

    var NavigteToBack = function () {
      $state.go('implantIndent');
    }

    var saveNewImplantIndentRequest = function () {
      var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.SAVE_API;

      var data = {
        "visitType": self.model.visitType.id,
        "patientId": self.model.patientId,
        "doctorId": 1,
        "indentTypeId": 1,
        "admissionId": self.model.admissionId,
        "storeId": self.model.store.id,
        "priorityId": self.model.priority.id,
        "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
        "isImplant": self.model.isImplant ? self.model.isImplant : false,
        "isKit": self.model.isKit ? self.model.isKit : false,
        "remark": self.model.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "cookieStoreId":$scope.storeId,
        "items": []
      };

      angular.forEach(self.model.ItemDetailList, function (item, key) {
        var implantItem = {
          "itemId": item.itemId,
          "uomTypeId": item.uomTypeId,
          "uomUnitId": item.uomUnitId,
          "indentQuantity": parseInt(item.indentQuantity),
          "statusId": setStatus('New')
        }
        data.items.push(implantItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
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

    var UpdateImplantIndent = function () {

      var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT.UPDATE_API + self.model.indentId;
      var data = {
        "id": 2,
        "remark": self.model.remark,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "storeId":$scope.storeId,
        "items": []
      };

      angular.forEach(self.model.ItemDetailList, function (item, key) {

        var indentItem = {
          "id": item.id,
          "itemId": item.itemId,
          "uomTypeId": item.uomTypeId,
          "uomUnitId": item.uomUnitId,
          "indentQuantity": parseInt(item.indentQuantity),
        }
        if (item.id == '' || item.id == null) {
          indentItem.statusId = setStatus('New')
        }

        data.items.push(indentItem);
      });

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

    var ChangeVisitType = function () {
      self.model.patientName = null;
      self.model.uhid = null;
      self.model.patientNames = [];
      ClearPatientInfo();
  }

    // $scope.validatesearch = function () {
    //   var isValid = true;
    //   if(self.model.visitType.id == 0){
    //     growl.error("Please select visit type");
    //     isValid = false;
    //   }
    //   return isValid;
    // }
    
    var SearchpatientNames = function (search) {
      // if($scope.validatesearch()){
        if (search.length < 3) return;

        if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3) && (self.model.visitType.id)) {
          self.model.prevSearch = search;

          var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" +self.model.visitType.id;

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
          if(!self.model.visitType.id){
            growl.error('Please select visit type for fetching the patient name');
        }
          if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
            self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
            return PatientNameNotFound(search);
          } else {
            return $filter('filter')(self.model.patientNames, {
              $: search
            });
          }
        }
      // }
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
            if (!!response.data.visitDetails.length) {
              if (self.model.visitType.id == 1) {
                self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
              } else if (self.model.visitType.id != 1) {
                self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                self.model.encounterId = response.data.visitDetails[0].encounterId,
                  self.model.admissionId = response.data.visitDetails[0].admissionId,
                  self.model.doctorId = response.data.visitDetails[0].doctorId
              }
            }


            $rootScope.stopLoader();
          }, function (err) {
            growl.error(err.data.summary);
            $rootScope.stopLoader();
          });
      }
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
    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }
    var SetOldValueToStore = function (store) {
      self.model.oldToStore = store;
      self.model.toStoreNew = self.model.oldToStore;
    }

    var ChangeFromStore = function (store) {
      if (!!self.model.ItemDetailList.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        PopupAction('Open', 'confirmStore');
      }
    }

    var ChangeToStore = function (store) {
      if (!!self.model.ItemDetailList.length) {
        self.model.toStore = self.model.oldToStore;
        self.model.toStoreNew = JSON.parse(JSON.stringify(store));
        PopupAction('Open', 'confirmStore');
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      PopupAction('close', 'confirmStore');
      self.model.ItemDetailList = [];
    }

    var SetOldValueIsConsignment = function (isConsignment) {
      self.model.oldIsConsignment = isConsignment;
      self.model.isConsignment = self.model.oldIsConsignment;
    }

    var ChangeIsConsignment = function (isConsignment) {
      if (!!self.model.ItemDetailList.length) {
        self.model.isConsignment = self.model.oldIsConsignment;
        self.model.isConsignmentNew = JSON.parse(JSON.stringify(isConsignment));
        $("#IsConsignment").modal()
      }
    }
    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var ConfirmIsConsignmentPopup = function () {
      self.model.isConsignment = self.model.isConsignmentNew;
      self.model.ItemDetailList = [];
      ModalDismiss('IsConsignment');
    }

    var SetOldValueIsImplant = function (isImplant) {
      self.model.oldIsImplant = isImplant;
      self.model.isImplant = self.model.oldIsConsignment;
    }

    var ChangeIsImplant = function (isImplant) {
      if (!!self.model.ItemDetailList.length) {
        self.model.isImplant = self.model.oldIsImplantNew;
        self.model.isImplantNew = JSON.parse(JSON.stringify(isImplant));
        $("#IsImplant").modal()
      }
    }

    var ConfirmIsImplantPopup = function () {
      self.model.isImplant = self.model.isImplantNew;
      self.model.ItemDetailList = [];
      ModalDismiss('IsImplant');
    }
    var SetOldValueIsKit = function (isKit) {
      self.model.oldIsKit = isKit;
      self.model.isKit = self.model.oldIsKit;
    }

    var ChangeIsKit = function (isKit) {
      if (!!self.model.ItemDetailList.length) {
        self.model.isKit = self.model.oldIsKit;
        self.model.isKitNew = JSON.parse(JSON.stringify(isKit));
        $("#IsKit").modal()
      }
    }

    var ConfirmIsKitPopup = function () {
      self.model.isKit = self.model.isKitNew;
      self.model.ItemDetailList = [];
      ModalDismiss('IsKit');
    }

    var SearchdoctorNames = function (search) {

      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;
        var URI = CONSTANTS.GLOBAL.DOCTOR_SEARCH_API +$scope.organizationId +"/"+$scope.unitId + "/" + search;

        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
          .then(function (response) {
            $rootScope.stopLoader();
            if (!!response.data && response.data.length > 0) {
              self.model.doctorNames = response.data;
              $rootScope.stopLoader();
              return $filter('filter')(self.model.doctorNames, {
                $: search
              });
            } else {
              $rootScope.stopLoader();
              return DoctorNameNotFound(search);

            }
          }, function (err) {
            $rootScope.stopLoader();
            return DoctorNameNotFound(search);
          });
      } else {
        if (!!self.model.doctorNames && self.model.doctorNames.length > 0 &&
          self.model.doctorNames[0].itemFound != undefined && !self.model.doctorNames[0].itemFound) {
          return DoctorNameNotFound(search);
        } else {
          return $filter('filter')(self.model.doctorNames, {
            $: search
          });
        }
      }
    }

    var DoctorNameNotFound = function (search) {
      var item = {
        "itemFound": false,
        "details": "Doctor Not found with name " + search
      };
      self.model.patientNames = [item];
      return self.model.patientNames;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewImplantIndent", {
      url: "/addNewImplantIndent",
      templateUrl: 'views/master/TheatreManagement/add-new-implant-indent.html',
      controller: "AddNewImpalntIndent.Controller",
      controllerAs: "vm"
    });

  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewImpalntIndent.Controller", addNewImplantIndentController);
})();