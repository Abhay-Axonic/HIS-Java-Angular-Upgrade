(function () {
  "use strict";

  function addNewQuotationSentController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, CurrencyService, growl, StatusService, $cookies, CONSTANTS) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
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
          maxDate: new Date()
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
        contractTypes: [{
          "code": "Select Contract Type"
        }],
        fromStores: [{
          "storeName": "Select Store"
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
        dateOptions: {
          formatYear: 'yyyy',
          showWeeks: false
        },
        prDetailList: [],
        expectedAccessories: [],
        termsVisible: 1
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      });

      self.model.OpenDueDate = OpenDueDate;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SaveEnquiry = SaveEnquiry;
      self.model.NavigateToBack = NavigateToBack;
      self.model.GetQRPopup = GetQRPopup;
      self.model.GetQRList = GetQRList;
      self.model.Expand = Expand;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.AddApprovedPOItems = AddApprovedPOItems;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ViewPRList = ViewPRList;
      self.model.AddInclusion = AddInclusion;
      self.model.ViewRequestDetails = ViewRequestDetails;

      PopulateDropdown();
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
        self.model.poType = self.model.poTypes[0];
        self.model.contractType = self.model.contractTypes[0];

        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.poTypes = self.model.poTypes.concat(response.data.assetType);
            self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            for(var i=0;i<self.model.stores.length;i++)
            {
              if(self.model.stores[i].id==selectedStoreId)
              {
                self.model.store = self.model.stores[i];
              }
            }

            var enquiryId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!enquiryId) {
              commonDetailService.setDataId(null);
              GetEnquiry(enquiryId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
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

    var PopulateProductCategory = function (assetCategory) {
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id + '/'+$scope.organizationId;

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

    var GetQRPopup = function () {
      if (ValidateDetails()) {
        self.model.qrItemList = [];
        self.model.fromDate = new Date();
        self.model.toDate = new Date();
        self.model.qrNumber = undefined;
        self.model.searchQR = undefined;

        var popup = angular.element('#GetQRList');
        self.model.action = 'Open';
        popup.modal('show');
        GetQRList();
      }
    }

    var GetItemList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

      var data = {
        "assetCategoryId": self.model.assetCategory.id,
        "productCategoryId": self.model.productCategory.id,
        "storeId": self.model.store.id
      }

      self.model.items = [];
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          self.model.items = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var GetQRList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.QR_SEARCH;
      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "qrNumber": self.model.qrNumber,
        "storeId": self.model.store.id,
        // todo: vendorId
        "vendorId": self.model.vendorId, //self.model.vendorId,
        "contractTypeId": self.model.contractType.id,
        "unitId":$scope.unitId
      }

      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          self.model.qrItemList = response.data;
          angular.forEach(self.model.qrItemList, function (qr) {
            qr["isChecked"] = false;
            qr.isSelectedIndent = false;
            angular.forEach(qr.details, function (item) {
              item["isChecked"] = false;
            });
          });

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          self.model.qrItemList = [];
          growl.error(err.data.message);
        });
    }

    var AddApprovedPOItems = function () {
      if (self.model.intendItemIds.length == 0) {
        growl.error('Please Select Items to Add');
        return false;
      }
      else {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.ADD_ITEM_QR;

        SelectedQRItems();

        var data = self.model.qrDetails;

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.itemDetails = [];
            var qrResponse = response.data;
            updateItemDetails(qrResponse);
            self.model.woQuotationRequestId = response.data[0].id;
            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            self.model.items = [];
            self.model.qrItemList = [];

            self.model.ClosePopup('close', 'GetQRList');
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.SelectedItemsIds = [];
            self.model.intendItemIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if (self.model.SelectedItemsIds.length == 0) {
        growl.error('Please Select Items to Add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.DIRECT_PR_DETAILS+ "/"+$scope.organizationId + "/"+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id
        };

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.otherChargesDropdownList = response.data.otherChargesList;
            response.data.itemDetailsList.forEach(function (value, key) {
              value['poQuantity'] = 0;
              value['prQuantity'] = '-';
              value['stageQuantityList'] = [{ stagedDate: moment(new Date()).format('YYYY-MM-DD'), quantity: parseInt(0) }];

              value['otherChargesTotal'] = 0;
              value['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
              value.discountPercent = 0;
              var isFound = self.model.itemDetails.find(function (x) {
                return x.itemId == value.itemId;
              });
              if (!isFound) {
                self.model.itemDetails.push(value);
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
            self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var updateItemDetails = function (responseData) {
      var newItemsHash = {};
      var itemDetailsHash = {};
      self.model.itemDetails.forEach(function (itemDetail) {
        itemDetailsHash[itemDetail.itemId] = itemDetail;
      });

      responseData.forEach(function (poItem) {
        var poNo = poItem.poNo;
        self.model.poId = poItem.id;
        poItem.details.forEach(function (item) {
          var itemId = item.itemId;
          var poId = item.id;
          var poQuantity = Number(item.poQuantity);
          var bonusQuantity = Number(item.bonusQuantity);

          var poObj = { id: poId, poNo: poNo, poQuantity: poQuantity };

          if (itemDetailsHash.hasOwnProperty(itemId)) {
            // item found in already saved list
            var itemDetail = itemDetailsHash[itemId];
            if (itemDetail.poIds && itemDetail.poIds.length) {
              // poIds list is present
              if (itemDetail.poIds.indexOf(poId) == -1) {
                itemDetail.poNumbersList.push(poObj);

                itemDetail.poIds.push(poId);
                itemDetail.qrDetailId = poId;
                itemDetail['qrDetailId'] = poId;
              } else {
                //DO nothing, Item is already preset in saved table
              }
            } else {
              // poIds list not present
              itemDetail.poNumbersList = [poObj]
              itemDetail.poIds = [poId];
              itemDetail.qrDetailId = poId;
            }

          } else if (newItemsHash.hasOwnProperty(itemId)) {
            // item found in new item hash
            var savedItem = newItemsHash[itemId];
            savedItem.poNumbersList.push(poObj);
            savedItem.poIds.push(poId);
            savedItem.qrDetailId = poId;
          } else {
            // item not found in item hash
            var clonedItem = JSON.parse(JSON.stringify(item));
            delete clonedItem.id;
            delete clonedItem.poQuantity;
            delete clonedItem.bonusQuantity;
            clonedItem.poNumbersList = [poObj]
            clonedItem.poIds = [poId];
            clonedItem.qrDetailId = poId;
            newItemsHash[itemId] = clonedItem;
          }
        });
      });

      for (var itemId in newItemsHash) {
        self.model.itemDetails.push(newItemsHash[itemId]);
      }

      // console.log(self.model.itemDetails, "self.model.itemDetails");
    }

    var SelectedQRItems = function () {
      var qr = self.model.isSelectedIndent;
      if (qr) {
        self.model.qrDetails = [];
        var qrData = {
          "id": qr.id,
          "qrIds": []
        }
        angular.forEach(qr.details, function (item) {
          if (item.isChecked) {
            qrData.qrIds.push(item.id);
          }
        });
        if (qr.isChecked && qrData.qrIds.length > 0) {
          self.model.qrDetails.push(qrData);
        }
      }
    }

    var GetEnquiry = function (enquiryId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.DETAILS_API + enquiryId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.enquiryId = response.data.id;
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });

          self.model.contractType = self.model.contractTypes.find(function (type) {
            return response.data.contractTypeId == type.id;
          });

          self.model.dueDate = new Date(response.data.dueDate);
          self.model.vendorName = response.data.vendorName;
          self.model.vendorId = response.data.vendorId;
          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var enquiryItem = {
              "id": item.id,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "quantity": item.quantity,
              "batchNo": item.batchNo,
              "expectedInclusion": item.expectedInclusion,
              "requestDetails": item.requestDetails,
              "currentVendor": item.currentVendorName,
              "specification": item.specification,
              "remark": item.remark
            }
            self.model.itemDetails.push(enquiryItem);
          });

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var CreateEnquiry = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.DETAILS_API_NEW;

      var data = {
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "woQuotationRequestId": self.model.woQuotationRequestId,
        "remark": self.model.remark,
        "dueDate": moment(self.model.dueDate).format('YYYY-MM-DD'),
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item, key) {
        var enquiryItem = {
          "workOrderQRDetailId": item.qrDetailId,
          "remark": item.remark,
          "statusId": setStatus('New')
        }

        data.items.push(enquiryItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToBack();
          growl.success("Quotation sent to vendor saved successfully..!");
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateEnquiry = function () {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.DETAILS_API + self.model.enquiryId;

      var data = {
        "remark": self.model.remark,
        "dueDate": moment(self.model.dueDate).format('YYYY-MM-DD'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        if (!!item.id) {
          var enquiryItem = {
            "id": item.id,
            "remark": item.remark,
          }
        } else {
          var enquiryItem = {
            "workOrderQRDetailId": item.qrDetailId,
            "remark": item.remark,
            "statusId": setStatus('New')
          }
        }

        data.items.push(enquiryItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToBack();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var SaveEnquiry = function () {
      if (ValidateItems()) {
        if (!self.model.enquiryId) {
          CreateEnquiry();
        } else {
          UpdateEnquiry();
        }
      }
    }

    var ViewPRList = function (item) {
      self.model.itemDetails.find(function (x) {
        if (item.itemId == x.itemId) {
          self.model.prDetailList = x.prList;
        }
      });
    }

    var setStatus = function (x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var OpenDueDate = function () {
      self.model.dueDateOpened = true;
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

    var ValidateDetails = function () {
      var isValid = true;

      if (!self.model.vendorName) {
        self.model.vendorId = undefined;
      }

      if (!self.model.store.id) {
        self.model.isStoreValid = true;
        isValid = false;
      }

      if (!self.model.contractType.id) {
        self.model.isContractTypeValid = true;
        isValid = false;
      }

      if (!self.model.vendorId) {
        self.model.isVendorValid = true;
        isValid = false;
      }

      if (!self.model.store.id || !self.model.vendorId || !self.model.contractType.id) {
        growl.error('Select Required Fields');
        isValid = false;
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      if (self.model.itemDetails.length <= 0) {
        growl.error('Add Items to Save Enquiry');
        isValidItem = false;
      } else {
        if (!self.model.vendorId || !self.model.vendorName) {
          self.model.isVendorValid = true;
          growl.error('Select Required Fields');
          isValidItem = false;
        }
      }

      return isValidItem;
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

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var SelectAllIndentItems = function () {
      var selecteItem = self.model.isSelectedIndent;
      self.model.intendItemIds = [];
      angular.forEach(self.model.qrItemList, function (po) {
        if (po.id !== selecteItem.id) {
          po.details.forEach(function (x) {
            x.isSelectedItem = false;
            x.isChecked = false;
            po.isChecked = false;
          });
        } else {
          po.details.forEach(function (x) {
            self.model.indentItemDetails.push(x);
            x.isSelectedItem = true;
            x.isChecked = true;
            po.isChecked = true;
            self.model.intendItemIds.push(x.itemId);
          });
        }
      });
    }

    var SelectIndentItem = function (item, indent) {
      var itemIds = self.model.intendItemIds;
      if (self.model.isSelectedIndent != indent) {
        angular.forEach(self.model.qrItemList, function (po) {
          if (po.id !== indent.id) {
            po.details.forEach(function (x) {
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
      } else {
        self.model.isSelectedIndent = null;
      }
    }

    var NavigateToBack = function () {
      $state.go('quotationSendToVendor');
    }

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }
    var ConfirmRemove = function (item) {
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var newArray = [];
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.itemDetails.map(function (obj) {
        if (obj.itemId !== item.itemId) {
          newArray.push(obj);
        }
      });

      self.model.itemDetails = newArray;
      var popup = angular.element('#confirmRemove');
      popup.modal('hide');
    }

    var AddInclusion = function (item) {
      self.model.expectedInclusion = item.expectedInclusion;
    }

    var ViewRequestDetails = function (item) {
      self.model.requestList = item.requestDetails;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewquotationS", {
      url: "/addNewquotationS",
      templateUrl: 'views/procurement/workOrder/add-new-quotation-send-to-vendor.html',
      controller: "AddNewquotationS.Controller",
      controllerAs: "vm",
      title: "Quotation Sent To Vendor"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewquotationS.Controller", addNewQuotationSentController);
})();