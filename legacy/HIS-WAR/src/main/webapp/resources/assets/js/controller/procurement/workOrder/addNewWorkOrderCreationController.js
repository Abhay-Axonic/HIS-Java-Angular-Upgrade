(function () {
  "use strict";

  function addNewWorkOrderCreationController($scope, $rootScope, $filter, $http, $state,  CONSTANTS, GenericService,PaginationService, commonDetailService, rejectReasonService, StatusService, CurrencyService, growl,$cookies) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      var cookieObject = $cookies.getObject('cookieObject');
      var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
      $scope.storeId = cookieObjectStore.storeId;
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
        stores: [{
          "store": "Select Store"
        }],
        contractTypes: [{
          "description": "Select Contract Type"
        }],
        againstTypes: [{
          "description": "Select Against Type"
        }],
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
        taxListSelection: [{
          "taxName": "Select tax"
        }],
        batchListSelection: [{
          "batchNo": "Select Batch"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        taxCodeValid: [],
        purchaseId: null,
        vendorSearch: '',
        vendorNames: [],
        searchItem: {
          itemName: undefined,
          itemCode: undefined
        },
        // batchText: [{
        //   "batchNo": "Select Batch"
        // }]
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      });
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.isItemChecked = false;
      self.model.RemoveItem = RemoveItem;
      self.model.Expand = Expand;
      //self.model.SaveWorkOrder = SaveWorkOrder;
      //self.model.GetIndentList = GetIndentList;
      self.model.NavigateToWorkOrder = NavigateToWorkOrder;
      self.model.AddAssets = AddAssets;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.GetIndentListAgaintstQR = GetIndentListAgaintstQR;
      self.model.GetIndentListAgaintstWR = GetIndentListAgaintstWR;
      self.model.ValidateBatchNo = ValidateBatchNo;
      self.model.ValidateWoQuantity = ValidateWoQuantity;
      self.model.CalculateDiscOnQty=CalculateDiscOnQty;
      self.model.SelectAllQrIndentItems = SelectAllQrIndentItems;
      self.model.SelectAllWrIndentItems = SelectAllWrIndentItems;

      self.model.SelectQrIndentItem = SelectQrIndentItem;
      self.model.SelectWrIndentItem = SelectWrIndentItem;

      self.model.AddIndentItemsQR = AddIndentItemsQR;
      self.model.AddIndentItemsWR = AddIndentItemsWR;
      self.model.GetWorkOrders = GetWorkOrders;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromContractType = ChangeFromContractType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;

      self.model.SetOldValueAgainstType = SetOldValueAgainstType;
      self.model.ChangeAgainstType = ChangeAgainstType;
      self.model.ConfirmAgainstTypePopup = ConfirmAgainstTypePopup;

      self.model.PopupAction = PopupAction;
      self.model.OpenPopup = OpenPopup;
      self.model.SelectTax = SelectTax;
      self.model.ClosePopup = ClosePopup;
      self.model.SaveTerms = SaveTerms;
      self.model.ViewTerms = ViewTerms;
      self.model.AddInclusion = AddInclusion;
      self.model.SaveWorkOrderCreation = SaveWorkOrderCreation;
      self.model.UpdateWorkOrderCreation = UpdateWorkOrderCreation;
      self.model.SetDecimal = SetDecimal;
      self.model.AddItemDiscount = AddItemDiscount;
      PopulateDropdown();
      PopulateDropdownContractType();
    }



    var setStatus = function (data) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == data;
      });

      return status.id;
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
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
    var AddInclusion = function (item) {
      // self.model.expectedInclusion = item.expectedInclusion;
    }

    var ValidateBatchNo = function (item) {
      var isValid = true;
      if (!item.selectedBatchNo) {
        item.isBatchNoValid = true;
        isValid = false;
        growl.error("Please Select Batch");
      }
      return isValid;
    }
    var ValidateWoQuantity = function (item) {
      var isValid = true;
      if (!item.woQuantity) {
        item.isWoQuantityValid = true;
        growl.error("Please enter workorder quantity");
        isValid = false;
      }
      if(self.model.againstType.id!=1){
      if (item.woQuantity > item.quantity) {
        item.isWoQuantityValid = true;
        growl.error("Workorder quantity should not not be greater than pending Workorder Request qty.");
        isValid = false;
      }
    }
      return isValid;
    }
    var CalculateDiscOnQty = function (item, woQty) {
      item.discount = item.discount || 0;
      item.woAmount = item.woAmount || 0;
      woQty = woQty || 0;

      var baseAmount = parseFloat(item.woAmount) * parseInt(woQty);
      item.discountAmount = baseAmount * (item.discount / 100);
      item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
    }
    
    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
        self.model.againstType = self.model.againstTypes[0];

        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.againstTypes = self.model.againstTypes.concat(response.data.requestType);
            // for(var i=1;self.model.stores.length;i++)
            // {
            //   if(self.model.stores[i].id==$cookies.getObject('selectedTopStore').storeId)
            //   {
            //     self.model.store = self.model.stores[i];
            //   }
            // }
            self.model.store = self.model.stores.find(function (store) {
              return $scope.storeId == store.id;
          });
            var purchaseId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!purchaseId) {
              commonDetailService.setDataId(null);
              GetWorkOrder(purchaseId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var PopulateDropdownContractType = function () {
      try {
        $rootScope.startLoader();
        self.model.contractType = self.model.contractTypes[0];
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var AddAssets = function (against) {
      if (ValidateDetails()) {
        if (against.id == 12) {
          self.model.action = 'Open';
          var popup = angular.element('#againstQRmodel');
          popup.modal('show');
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
          self.model.indents = [];
          self.model.searchIndent = undefined;
          self.model.prNumber = undefined;
        }
        else if (against.id == 13) {
          self.model.action = 'Open';
          var popup = angular.element('#againstWRmodel');
          popup.modal('show');
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
          self.model.indents = [];
          self.model.searchIndent = undefined;
          self.model.prNumber = undefined;
        }
        else if (against.id == 1) {
          self.model.action = 'Open';
          var popup = angular.element('#addNewItemWasteStore');
          popup.modal('show');
          PopulateAssetCategoryDropdown();
          self.model.items = [];
          self.model.pagination.data=[];
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          self.model.searchItem.itemCode = "";
          self.model.searchItem.itemName = "";
        }
        self.model.items = [];
        self.model.itemName="";
        self.model.itemCode="";
        //  self.model.search.itemCode = undefined;
        //self.model.search.itemName = undefined;
      }
    }
    var SaveTerms = function (viewModal) {
      switch (viewModal) {
        case "inclusionDetails":
          self.model.itemTerms.inclusionDetails = self.model.inclusionDetails;
          self.model.itemTerms.isValid = false;
          ClosePopup('Close', 'inclusionDetails');
          break;
        case "exclusionDetails":
          self.model.itemTerms.exclusionDetails = self.model.exclusionDetails;
          self.model.itemTerms.isValidSupportTerm = false;
          ClosePopup('Close', 'exclusionDetails');
          break;
        case "paymentTerms":
          self.model.itemTerms.paymentTerms = self.model.paymentTerms;
          self.model.itemTerms.isValidPaymentTerm = false;
          ClosePopup('Close', 'paymentTerms');
          break;

      }
    }

    var ViewTerms = function (viewModal, item) {
      self.model.itemTerms = item;
      switch (viewModal) {
        case "inclusionDetails":
          self.model.itemDetails.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.inclusionDetails = x.inclusionDetails;
            }
          });
          break;
        case "exclusionDetails":
          self.model.itemDetails.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.exclusionDetails = x.exclusionDetails;
            }
          });
          break;
        case "paymentTerms":
          self.model.itemDetails.find(function (x) {
            if (item.itemId == x.itemId) {
              self.model.paymentTerms = x.paymentTerms;
            }
          });
          break;
      }
    }

    var GetWorkOrder = function (purchaseId) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + purchaseId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {

          self.model.woId = response.data.id;
          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
          });

          self.model.contractType = self.model.contractTypes.find(function (item) {
            return response.data.contractTypeId == item.id;
          });

          self.model.againstType = self.model.againstTypes.find(function (item) {
            return response.data.requestTypeId == item.id;
          });

          self.model.wrNo = response.data.worNo;
          self.model.qrNo = response.data.qrNo;
          self.model.taxList = self.model.taxListSelection.concat(response.data.taxList);
          self.model.workOrderRemark = response.data.remarks;
          self.model.vendorId = response.data.vendorId;
          self.model.vendorName = response.data.vendorName;
          self.model.totalAmount = response.data.totalAmount;
          self.model.totalDiscount = response.data.totalDiscount;
          self.model.netAmount = response.data.netAmount;
          self.model.totalTax = response.data.totalTax;
          self.model.itemDetails = [];
          angular.forEach(response.data.items, function (item) {

            item.bonusQuantity = item.bonusQuantity ? item.bonusQuantity : 0;
            item.discountPercentage= item.discountPercentage? item.discountPercentage:0;

            var woItem = {
              "id": item.id,
              "batchNo": item.batchNo,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "quantity": item.quantity,
              "amount": item.amount,
              "vendorName": item.vendorName,
              "inclusionDetails": item.inclusionDetails,
              "exclusionDetails": item.exclusionDetails,
              "paymentTerms": item.paymentTerm,
              "remark": item.remark,
              "woQuantity": item.woQty,
              "woAmount": item.woAmount,
              "bonusQuantity": item.bonusQuantity,
              "discount": item.discountPercentage,
              "discountAmount": item.discountAmount
            }
            if (self.model.againstType.id == 1) {
              woItem.batchList = self.model.batchListSelection.concat(item.batchList);
              woItem.selectedBatchNo = item.batchList.find(function (val) {
                return item.batchId == val.batchId;
              });
            }
            woItem.selectedTax = self.model.taxList.find(function (val) {
              return item.taxId == val.taxId;
            });
            woItem.selectedTaxPercent = item.taxPercent.toFixed(2);

            self.model.itemDetails.push(woItem);
          });

          $rootScope.stopLoader();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }

    var ValidateDetails = function () {

      var isValid = true;

      if (!self.model.store.id) {
        isValid = false;
        self.model.isStoreValid = true;
      }
      if (!self.model.vendorId || !self.model.vendorName) {
        self.model.isVendorValid = true;
        isValid = false;
      }
      if (!self.model.contractType.id) {
        isValid = false;
        self.model.isContractTypeValid = true;
      }
      if (!self.model.againstType.id) {
        isValid = false;
        self.model.isAgainstTypeValid = true;
      }

      if (!self.model.store.id || !self.model.vendorId || !self.model.contractType.id || !self.model.againstType.id == undefined) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var getSelectedIds = function () {
      var selecteItemIds = self.model.indentItemDetails.map(function (item) {
        return item.itemId;
      });
      return _.uniq(selecteItemIds);
    }

    var SelectedItemsQR = function () {
      angular.forEach(self.model.qrIndents, function (qr) {
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
          self.model.qrDetails = qrData;
      });
    }
    var SelectedItemsWR = function () {
      angular.forEach(self.model.wrIndents, function (wr) {
        var wrData = null;
        if (wr.isChecked) {
          wrData = {
            "wrId": wr.id,
            "itemIds": []
          }
        }

        angular.forEach(wr.items, function (item) {
          if (item.isChecked) {
            wrData.itemIds.push(item.id);
          }
        });

        if (wr.isChecked && wrData.itemIds.length > 0)
          self.model.wrDetails = wrData;
      });
    }

    var AddItems = function () {
      if (!self.model.SelectedItemsIds.length) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.GET_ITEM_DETAIL;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId
        };

        $rootScope.startLoader();
        var popup = angular.element('#addNewItemWasteStore');
        popup.modal('hide');
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function (item, key) {
              var isFound = self.model.itemDetails.find(function (x) {
                return x.itemId == item.itemId;
              });
              if (item.batch) {
                item.batchList = self.model.batchListSelection.concat(item.batch);
                item.selectedBatchNo = item.batchList[0];
              }
              /*  else{
                 item.selectedBatchNo=self.model.batchListSelection[0];
               } */
              if (self.model.taxList == undefined || self.model.taxList.length == 0) {
                self.model.taxList = self.model.taxListSelection.concat(item.taxList);
              }
              if (!!item.taxPercent) {
                item.selectedTaxPercent = item.taxPercent.toFixed(2);
              }
              else {
                item.selectedTaxPercent = 0.00;
              }
              item.selectedTax = self.model.taxList.find(function (val) {
                return item.taxId == val.taxId;
              });

              if (!isFound) {
                self.model.itemDetails.push(item);

              }
            });

            self.model.SelectedItemsIds = [];
            self.model.items = [];
            self.model.itemName="";
            self.model.itemCode="";
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            // self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var AddIndentItemsQR = function () {
      if (self.model.qrIntendItemIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      }
      else {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.GET_ITEM_DETAIL_FOR_QR;
        SelectedItemsQR();

        var data = self.model.qrDetails;
        data.unitId = $scope.unitId;
        data.organizationId = $scope.organizationId;
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.data;
            var purchaseRequestListResponse = responseData.pocreationDetailList;
            self.model.qrNo = purchaseRequestListResponse.qrNo;
            self.model.taxList = self.model.taxListSelection.concat(responseData.taxList);
            updatePrItemDetails(purchaseRequestListResponse);

            self.model.qrIndentItemDetails = [];
            self.model.qrIntendItemIds = [];
            self.model.items = [];
            self.model.qrIndents = [];

            var popup = angular.element('#againstQRmodel');
            popup.modal('hide');
            $rootScope.stopLoader();
          }, function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var AddIndentItemsWR = function () {
      if (self.model.wrIntendItemIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      }
      else {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.GET_ITEM_DETAIL_FOR_WR;

        SelectedItemsWR();
        var data = self.model.wrDetails;
        data.unitId = $scope.unitId;
        data.organizationId = $scope.organizationId;
        

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.data;
            var purchaseRequestListResponse = responseData.pocreationDetailList;
            self.model.wrNo = purchaseRequestListResponse.wrNo;

            self.model.taxList = self.model.taxListSelection.concat(responseData.taxList);
            updatePrItemDetails(purchaseRequestListResponse);

            self.model.wrIndentItemDetails = [];
            self.model.wrIntendItemIds = [];
            self.model.items = [];
            self.model.wrIndents = [];

            var popup = angular.element('#againstWRmodel');
            popup.modal('hide');

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            // self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var updatePrItemDetails = function (prResponseData) {
      var newItemsHash = {};
      var itemDetailsHash = {};
      self.model.itemDetails.forEach(function (itemDetail) {
        itemDetailsHash[itemDetail.itemId] = itemDetail;
      });
      prResponseData.forEach(function (prItem) {
        self.model.woReqOrWoQuotId = prItem.id;
        prItem.items.forEach(function (item) {
          var itemId = item.itemId;
          var prId = item.id;

          if (itemDetailsHash.hasOwnProperty(itemId)) {
            // item found in already saved list
            var itemDetail = itemDetailsHash[itemId];
            if (itemDetail.prIds && itemDetail.prIds.length) {
              // prIds list is present
              if (itemDetail.prIds.indexOf(prId) == -1) {
                itemDetail.prIds.push(prId);
              } else {
              }
            } else {
              itemDetail.prIds = [prId];
            }

          } else if (newItemsHash.hasOwnProperty(itemId)) {
            var savedItem = newItemsHash[itemId];
            savedItem.prIds.push(prId);
          } else {
            var clonedItem = JSON.parse(JSON.stringify(item));
            delete clonedItem.id;
            clonedItem.prIds = [prId];
            newItemsHash[itemId] = clonedItem;
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
        if (self.model.againstType.id == 13) {

        } else if (self.model.againstType.id == 12) {

        }
        self.model.itemDetails.push(newItemsHash[itemId]);

      }
    }

    var AddItemDiscount = function (type, item, value, index) {
      if (type == 'percent') {
        item.discount = parseFloat(item.discount).toFixed(2);
        if (isNaN(item.discount) || !item.discount || item.discount == 0) {
          item.discount = 0;
          item.discountAmount = 0;
        } else {
          if (parseFloat(item.discount) > 100) {
            self.model.discount[index] = true;
            growl.error('Discount must be less than 100% of total amount');
          } else {
            if (!item.woAmount) {
              item.discountAmount = 0;
            } else {
              item.discountAmount = (item.woQuantity * item.woAmount) * (item.discount / 100);
              item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
            }
          }
        }

      } else if (type == 'amount') {
        item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
        if (isNaN(item.discountAmount) || !item.discountAmount || item.discountAmount == 0) {
          item.discountAmount = 0;
          item.discount = 0;
        } else {
            if (!item.woAmount) {
              item.discount = 0;
            } else {
              item.discount = ((parseFloat(item.discountAmount) / (item.woQuantity * item.woAmount)) * 100);
              item.discount = parseFloat(item.discount).toFixed(2);
            }
        }
      }
    }
    var ValidateItems = function () {
      var isValidItem = true;
      self.model.itemDetails;
      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add items');
        isValidItem = false;
      } else {
        angular.forEach(self.model.itemDetails, function (item, index) {
          if (self.model.againstType.id == 1) {
            if (!ValidateBatchNo(item)) {
              isValidItem = false;
            }
          }
          if (!ValidateWoQuantity(item)) {
            isValidItem = false;
          }
          if (!item.selectedTax.taxId) {
            growl.error('Please select valid tax code');
            self.model.taxCodeValid[index] = true;
            isValidItem = false;
          }
          if (parseFloat(item.discount) > 100) {
            self.model.discount[index] = true;
            growl.error('Discount must be less than 100% of total amount');
          }
        })
      }
      return isValidItem;
    }



    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

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

    var GetItemList = function () {
      if(self.model.itemName){
        if(self.model.itemName.length >= 3){    
          var URI = CONSTANTS.GLOBAL.ITEMS_WORKORDER;

          var data = {
            "assetTypeId": 2,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId":$scope.unitId
          }

          self.model.items = [];
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.selectAllRows = false;
            self.model.items = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }
      }
      else if(self.model.itemCode){
        if(self.model.itemCode.length >= 3){
          var URI = CONSTANTS.GLOBAL.ITEMS_WORKORDER;

          var data = {
            "assetTypeId": 2,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId":$scope.unitId
          }

          self.model.items = [];
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.selectAllRows = false;
            self.model.items = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }
      }
      else{
        self.model.items = [];
        self.model.pagination.data = [];
      }
    }

    var SelectTax = function (tax, item) {
      if (!!tax) {
        item.selectedTaxPercent = tax.taxPercent.toFixed(2);
      }
    }
    var SetDecimal = function (name, item, value) {
      if (name == "woAmount") {
        if (isNaN(item.woAmount) || !item.woAmount || item.woAmount == 0) {
          item.woAmount = 0;
        } else {
          item.woAmount = parseFloat(item.woAmount).toFixed(2);
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
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.items.map(function (item) {
          item.isSelected = true;
          self.model.isItemChecked = true;
          return item.itemId;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.items.forEach(function (item) {
          item.isSelected = false;
          self.model.isItemChecked = true;
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

      self.model.isItemChecked = false;
      self.model.items.forEach(function (item) {
        if (item.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });
    }
    var SelectAllQrIndentItems = function () {
      var selecteItem = self.model.isSelectedQrIndent;
      self.model.qrIntendItemIds = [];
      angular.forEach(self.model.qrIndents, function (wo) {
        if (wo.id !== selecteItem.id) {
          wo.items.forEach(function (x) {
            x.isSelectedItem = false;
            x.isQrChecked = false;
            wo.isQrChecked = false;
          });
        } else {
          wo.items.forEach(function (x) {
            self.model.qrIndentItemDetails.push(x);
            x.isSelectedItem = true;
            x.isChecked = true;
            wo.isChecked = true;
            self.model.qrIntendItemIds.push(x.itemId);
          });
        }
      });
    }

    var SelectAllWrIndentItems = function () {
      var selecteItem = self.model.isSelectedWrIndent;
      self.model.wrIntendItemIds = [];
      angular.forEach(self.model.wrIndents, function (wo) {
        if (wo.id !== selecteItem.id) {
          wo.items.forEach(function (x) {
            x.isSelectedItem = false;
            x.isChecked = false;
            wo.isChecked = false;
          });
        } else {
          wo.items.forEach(function (x) {
            self.model.wrIndentItemDetails.push(x);
            x.isSelectedItem = true;
            x.isChecked = true;
            wo.isChecked = true;
            self.model.wrIntendItemIds.push(x.itemId);
          });
        }
      });
    }

    var SelectQrIndentItem = function (item, indent) {
      var itemIds = self.model.qrIntendItemIds;
      if (item.isSelectedItem) {
        item.isChecked = true;
        indent.isChecked = true;
        itemIds.push(item.itemId);
        self.model.qrIndentItemDetails.push(item);
      } else {
        item.isChecked = false;
        indent.isChecked = false;
        var index = itemIds.indexOf(item.isSelectedItem);
        if (index > -1) {
          itemIds.splice(index, 1);
        }

        var itemIndex = self.model.qrIndentItemDetails.indexOf(item);
        if (itemIndex > -1) {
          self.model.qrIndentItemDetails.splice(itemIndex, 1);
        }
      }
      if (indent.items.every(isSelectedItem)) {
        indent.isSelectedQrIndent = true;
      } else {
        indent.isSelectedQrIndent = false;
      }
    }

    var SelectWrIndentItem = function (item, indent) {
      var itemIds = self.model.wrIntendItemIds;
      if (item.isSelectedItem) {
        item.isChecked = true;
        indent.isChecked = true;
        itemIds.push(item.itemId);
        self.model.wrIndentItemDetails.push(item);
      } else {
        item.isChecked = false;
        indent.isChecked = false;
        var index = itemIds.indexOf(item.isSelectedItem);
        if (index > -1) {
          itemIds.splice(index, 1);
        }

        var itemIndex = self.model.wrIndentItemDetails.indexOf(item);
        if (itemIndex > -1) {
          self.model.wrIndentItemDetails.splice(itemIndex, 1);
        }
      }
      if (indent.items.every(isSelectedItem)) {
        indent.isSelectedWrIndent = true;
      } else {
        indent.isSelectedWrIndent = false;
      }
    }

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var Expand = function (item, woItemList) {
      angular.forEach(woItemList, function (value) {
        value.Expanded = false;
      });
      item.Expanded = !item.Expanded;
    }

    var GetWorkOrders = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.QO_SEARCH;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "contractTypeId": self.model.contractType.id,
          "storeId": self.model.store.id,
          "unitId":$scope.unitId,
        }

        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.indents = response.data;
            angular.forEach(self.model.indents, function (pr) {
              pr["isChecked"] = false;
              angular.forEach(pr.items, function (item) {
                item["isChecked"] = false;
              });
            });

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.indents = [];
            growl.error(err.data.message);
          });
      }
    }
    var GetIndentListAgaintstQR = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.SEARCH_BY_QR;
        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "statusId": 2,
          "unitId":$scope.unitId
        }

        GenericService.serviceAction("POST", URI, data).then(
          function (response) {

            self.model.qrIndents = response.data;
            self.model.qrIndentItemDetails = [];
            self.model.intendItemIds = [];
            angular.forEach(self.model.qrIndents, function (pr) {
              pr["isChecked"] = false;
              angular.forEach(pr.items, function (item) {
                item["isChecked"] = false;
              });
            });

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.qrIndents = [];
            growl.error(err.data.message);
          });
      }
    }
    var GetIndentListAgaintstWR = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.SEARCH_BY_WR;

        var data = {
          "storeId": self.model.store.id,
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "statusId": 2,
          "unitId":$scope.unitId
        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();

            self.model.wrIndents = response.data;
            self.model.wrIndentItemDetails = [];
            self.model.intendItemIds = [];
            angular.forEach(self.model.wrIndents, function (pr) {
              pr["isChecked"] = false;
              angular.forEach(pr.items, function (item) {
                item["isChecked"] = false;
              });
            });

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.wrIndents = [];
            growl.error(err.data.message);
          });
      }
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be lesser than to date');
        isValid = false;
      }

      return isValid;
    }
    var SearchVendorNames = function (search) {
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
        if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].vendorFound != undefined && !self.model.vendorNames[0].vendorFound) {
          return nameNotFound(search);
        } else {
          return $filter('filter')(self.model.vendorNames, {
            $: search
          });
        }
      }
    }

    var nameNotFound = function (search) {
      var item = {
        "vendorFound": false,
        "detail": "Vendor not found with name " + search,
        "search": search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }


    var SetOldValueAgainstType = function (againstType) {
      self.model.oldAgainstType = againstType;
      self.model.againstType = self.model.oldAgainstType;
    }

    var ChangeAgainstType = function (againstType) {
      if (!!self.model.itemDetails.length) {
        self.model.againstType = self.model.oldAgainstType;
        self.model.againstTypeNew = JSON.parse(JSON.stringify(againstType));
        PopupAction('Open', 'confirmValidation');
      }
    }

    var ConfirmAgainstTypePopup = function () {
      self.model.againstType = self.model.againstTypeNew;
      PopupAction('close', 'confirmValidation');
      self.model.itemDetails = [];
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
        PopupAction('Open', 'confirmStore');
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      PopupAction('close', 'confirmStore');
      self.model.itemDetails = [];
    }

    var SetOldValueAssetType = function (contractType) {
      self.model.oldContractType = contractType;
      self.model.contractType = self.model.oldContractType;
    }

    var ChangeFromContractType = function (contractType) {
      if (!!self.model.itemDetails.length) {
        self.model.contractType = self.model.oldContractType;
        self.model.ContractNew = JSON.parse(JSON.stringify(contractType));
        PopupAction('Open', 'confirmAssetTypePopup');
      }
    }

    var ConfirmAssetTypePopup = function () {
      self.model.contractType = self.model.ContractNew;
      PopupAction('close', 'confirmAssetTypePopup');
      self.model.itemDetails = [];
    }

    var ConfirmRemove = function (item) {
      self.model.PopupAction('Open', 'confirmRemove');
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
      self.model.PopupAction('Close', 'confirmRemove');
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
    var NavigateToWorkOrder = function () {
      $state.go('workorderCreation');
    }

    var SaveWorkOrderCreation = function () {
      if (ValidateItems()) {
        if (!self.model.woId) {
          CreateWorkOrderCreation();
        } else {
          UpdateWorkOrderCreation();
        }
      }
    }

    var CreateWorkOrderCreation = function () {
      var workOrder = self.model;

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.SAVE_API;

      var data = {
        "storeId": workOrder.store.id,
        "contractTypeId": workOrder.contractType.id,
        "vendorId": workOrder.vendorId,
        "wrNo": workOrder.wrNo,
        "woReqOrWoQuotId": workOrder.woReqOrWoQuotId,
        "qrNo": workOrder.qrNo,
        "woAgainstTypeId": workOrder.againstType.id,
        "total_amount": workOrder.totalAmount,
        "totalDiscount": parseFloat(workOrder.totalDiscount),
        "totalTax": parseFloat(workOrder.totalTax),
        "netAmount": parseFloat(workOrder.totalNetAmount),
        "remark": workOrder.workOrderRemark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(workOrder.itemDetails, function (item, key) {
        var woItem = {
          "itemId": item.itemId,
          "woQty": parseInt(item.woQuantity),
          "bonusQty": parseInt(item.bonusQuantity),
          "amount": parseFloat(item.woAmount),
          "disPercentage": parseFloat(item.discount),
          "disAmount": parseFloat(item.discountAmount),
          "taxId": item.selectedTax.taxId,
          "taxPercentage": item.selectedTax.taxPercent,
          "taxAmount": parseFloat(item.taxAmount),
          "totalAmount": parseFloat(item.totalAmount),
          "netAmount": parseFloat(item.netAmount),
          "inclusionDetails": item.inclusionDetails,
          "exclusionDetails": item.exclusionDetails,
          "paymentTerms": item.paymentTerms, 
          "remark": item.remark,
          "statusId": setStatus('New'),
        }
        if (self.model.againstType.id == 1) {
          woItem.batchId = item.selectedBatchNo.batchId;
        }
        else {
          woItem.woOrQrId = item.prIds[0];
          woItem.batchId = item.batchId;

        }

        data.items.push(woItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToWorkOrder();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateWorkOrderCreation = function () {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.UPDATE_API + self.model.woId;

      var data = {
        "total_amount": self.model.totalAmount,
        "totalDiscount": parseFloat(self.model.totalDiscount),
        "totalTax": parseFloat(self.model.totalTax),
        "netAmount": parseFloat(self.model.totalNetAmount),
        "remark": self.model.workOrderRemark,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        var woItem = {
          "id": item.id,
          "itemId": item.itemId,
          "batchId": item.batchId,
          "woQty": parseInt(item.woQuantity),
          "bonusQty": parseInt(item.bonusQuantity),
          "amount": item.woAmount,
          "disPercentage": parseFloat(item.discount),
          "disAmount": parseFloat(item.discountAmount),
          "taxId": item.selectedTax.taxId,
          "taxPercentage": item.selectedTax.taxPercent,
          "taxAmount": parseFloat(item.taxAmount),
          "totalAmount": parseFloat(item.totalAmount),
          "netAmount": parseFloat(item.netAmount),
          "inclusionDetails": item.inclusionDetails,
          "exclusionDetails": item.exclusionDetails,
          "paymentTerms": item.paymentTerms,
          "remark": item.remark,
        }
      
        data.items.push(woItem);
      });
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToWorkOrder();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addItemWorkorderCreation", {
      url: "/addItemWorkorderCreation",
      templateUrl: 'views/procurement/workOrder/add-item-workorder-creation.html',
      controller: "AddItemWorkorderCreation.Controller",
      controllerAs: "vm",
      title: 'Work Order Creation'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddItemWorkorderCreation.Controller", addNewWorkOrderCreationController);
})();