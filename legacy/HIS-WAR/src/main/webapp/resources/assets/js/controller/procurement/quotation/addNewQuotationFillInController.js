(function () {
  "use strict";

  function addNewQuotationFillInController($scope, $rootScope, reportService, $http, $filter, $state, GenericService, commonDetailService, CurrencyService, growl, StatusService, CONSTANTS,$cookies) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        fromDate: new Date(),
        toDate: new Date(),
        dueDate: new Date(),
        refDate: new Date(),
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
          showWeeks: false
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
          "storeName": "Select Store"
        }],
        toStores: [{
          "storeName": "Select Store"
        }],
        assetTypes: [{
          "type": "Select Type"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        intendItemIds: [],
        indentItemDetails: [],
        dateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        expectedAccessories: [],
        accessoryItem: [],
        termsVisible: 1,
        vendordetailsValid: [],
        vendorNameValid: [],
        vendorCOP: [],
        qrSearch: undefined
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
      self.model.OpenRefDate = OpenRefDate;
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
      self.model.ViewAccessories = ViewAccessories;
      self.model.FillAccessoryDetails = FillAccessoryDetails;
      self.model.AddAccessory = AddAccessory;
      self.model.LessAccessory = LessAccessory;
      self.model.SetDecimal = SetDecimal;
      self.model.SaveAccessoryDetails = SaveAccessoryDetails;
      self.model.ViewTerms = ViewTerms;
      self.model.SaveTerms = SaveTerms;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      self.model.rePrintSlip=rePrintSlip;

      PopulateDropdown();
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
        //self.model.store = self.model.stores[0];
        self.model.assetType = self.model.assetTypes[0];

        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.requestingStores);
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
            for(var i=0;i<self.model.stores.length;i++)
            {
              if(self.model.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
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
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType.id;

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
        "detail": "Vendor not found with name " + search
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
        if (!!self.model.enquiryId) {
          self.model.fromDate = new Date(self.model.qrDateNumber);
          self.model.vqrNumberSearch = self.model.vqrNumber;
        } else {
          self.model.fromDate = new Date();
          self.model.vqrNumberSearch = undefined;
        }

        self.model.qrItemList = [];
        self.model.toDate = new Date();
        self.model.qrSearch = undefined;
        self.model.action = 'Open';
        var popup = angular.element('#GetVQRList');
        popup.modal('show');
      }
    }

    var GetItemList = function () {
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
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_FILL_IN.SEARCH_VQR_API;
      var getVqr = undefined;
      if (!!self.model.enquiryId) {
        getVqr = false;
      }

      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "assetTypeId": self.model.assetType.id,
        "vqrNumber": self.model.vqrNumberSearch ? self.model.vqrNumberSearch : undefined,
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "getVqr": getVqr,
        "vqrId": self.model.selectedvqrId,
        "id": self.model.enquiryId,
        "unitId": $scope.unitId
      }
      $scope.startLoaderPopup();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          self.model.qrItemList = response.data;
          angular.forEach(self.model.qrItemList, function (qr) {
            qr["isChecked"] = false;
            qr.isSelectedIndent = false;
            angular.forEach(qr.items, function (item) {
              item["isChecked"] = false;
            });
          });

          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
          self.model.qrItemList = [];
          growl.error(err.data.message);
        });
    }

    var AddApprovedPOItems = function () {
      SelectedQRItems();
      if (self.model.intendItemIds.length == 0 || self.model.qrDetails[0].itemIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_FILL_IN.DETAILS_VQR_ITEMS;

        var data = self.model.qrDetails;

        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var qrResponse = response.data;
            self.model.tempQrId = response.data[0].qrNumber;
            if (self.model.tempQrId != self.model.qrNumber) {
              self.model.itemDetails = [];
            }

            updateItemDetails(qrResponse);
            self.model.qrNumber = response.data[0].qrNumber;
            self.model.qrDate = moment(response.data[0].qrDate).format('DD/MM/YYYY');
            self.model.vqrNumber = response.data[0].vqrNumber;
            self.model.vqrDate = moment(response.data[0].vqrDate).format('DD/MM/YYYY');
            self.model.qrId = response.data[0].qrId;
            self.model.vqrId = response.data[0].id;

            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            self.model.items = [];
            self.model.qrItemList = [];

            self.model.dateOptions.minDate = new Date(response.data[0].qrDate);
            self.model.ClosePopup('close', 'GetVQRList');
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var AddItems = function () {
      if (self.model.SelectedItemsIds.length == 0) {
        growl.error('Please select items to add');
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
        poItem.items.forEach(function (item) {
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
                itemDetail.poDetailId = poId;
                itemDetail['poDetailId'] = poId;
              } else {
                //DO nothing, Item is already preset in saved table
              }
            } else {
              // poIds list not present
              itemDetail.poNumbersList = [poObj]
              itemDetail.poIds = [poId];
              itemDetail.poDetailId = poId;
            }

          } else if (newItemsHash.hasOwnProperty(itemId)) {
            // item found in new item hash
            var savedItem = newItemsHash[itemId];
            savedItem.poNumbersList.push(poObj);
            savedItem.poIds.push(poId);
            savedItem.poDetailId = poId;
          } else {
            // item not found in item hash
            var clonedItem = JSON.parse(JSON.stringify(item));
            delete clonedItem.id;
            delete clonedItem.poQuantity;
            delete clonedItem.bonusQuantity;
            clonedItem.poNumbersList = [poObj]
            clonedItem.poIds = [poId];
            clonedItem.poDetailId = poId;
            newItemsHash[itemId] = clonedItem;
            newItemsHash[itemId].detailsId = poId;
            newItemsHash[itemId].vqrDetailId = poId;
          }
        });
      });

      for (var itemId in newItemsHash) {
        self.model.itemDetails.push(newItemsHash[itemId]);
      }

      console.log(self.model.itemDetails, "self.model.itemDetails");
    }

    var SelectedQRItems = function () {
      var qr = self.model.isSelectedIndent;
      if (qr) {
        self.model.qrDetails = [];
        var qrData = {
          "vqrId": qr.id,
          "itemIds": []
        }
        angular.forEach(qr.items, function (item) {
          if (item.isChecked) {
            qrData.itemIds.push(item.itemId);
          }
        });
        // if (qr.isChecked && qrData.itemIds.length > 0) {
        self.model.qrDetails.push(qrData);
        // } 
      }
    }

    var GetEnquiry = function (enquiryId) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_FILL_IN.DETAILS_API + enquiryId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.enquiryId = response.data.id;
          self.model.selectedvqrId = response.data.vqrId;
          self.model.vqrNumberSearch = response.data.vqrNumber;
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });

          self.model.assetType = self.model.assetTypes.find(function (assetType) {
            return response.data.assetTypeId == assetType.id;
          });

          self.model.refDate = response.data.refDate;
          self.model.refNumber = response.data.refNumber;
          self.model.qrNumber = response.data.qrNumber;
          self.model.qrDateNumber = response.data.qrDate;
          self.model.qrDate = moment(response.data.qrDate).format('DD/MM/YYYY');
          self.model.vqrNumber = response.data.vqrNumber;
          self.model.vqrDate = moment(response.data.vqrDate).format('DD/MM/YYYY');
          self.model.vendorName = response.data.vendorName;
          self.model.vendorId = response.data.vendorId;
          self.model.remark = response.data.remark;
          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var enquiryItem = {
              "id": item.id,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "uomType": item.uomType,
              "uomUnit": item.uomUnit,
              "vqrQuantity": item.qrQuantity,
              "quantity": item.quantity,
              "vendorCOP": item.vendorCop.toFixed(2),
              "remark": item.remark,
              "prList": item.prList,
              "accessoriesDetails": item.expectedAccessories,
              "warrantyPeriod": item.warrantyPeriod,
              "supportTerms": item.supportTerms,
              "paymentTerms": item.paymentTerms,
              "technicalSpecification": item.technicalSpecification,
              "vendorWarrantyPeriod": item.vendorWarrantyPeriod,
              "vendorExpectedSupport": item.vendorExpectedSupport,
              "vendorExpectedPayment": item.vendorExpectedPayment,
              "vendorTechnicalSpecification": item.vendorTechnicalSpecification
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
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_FILL_IN.SAVE_API;

      var data = {
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "assetTypeId": self.model.assetType.id,
        "qrId": self.model.qrId,
        "vqrId": self.model.vqrId,
        "refNumber": self.model.refNumber,
        "refDate": moment(self.model.refDate).format('YYYY-MM-DD'),
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "remark": self.model.remark,
        "items": [],
        "organizationId": $scope.organizationId,
        "userId": $scope.userId,
        "unitId": $scope.unitId
      };

      angular.forEach(self.model.itemDetails, function (item, key) {
        var accessoriesDetail = [];

        angular.forEach(item.accessoriesDetails, function (details) {
          accessoriesDetail.push({
            "qrAccessMapperId": details.id,
            "name": details.name,
            "vendorDetails": details.vendorDetails,
            "details":details.details //added to resolved HIS-9538 Change mode of the quotation fill in clears the second line accessories details
          });
        });

        var enquiryItem = {
          "vendorCop": item.vendorCOP,
          "remark": item.remark,
          "qrDetailId": item.qrDetailId,
          "vqrDetailId": item.vqrDetailId,
          "accessoriesDetails": accessoriesDetail,
          "vendorWarrantyPeriod": item.vendorWarrantyPeriod,
          "vendorExpectedSupport": item.vendorExpectedSupport,
          "vendorExpectedPayment": item.vendorExpectedPayment,
          "vendorTechnicalSpecification": item.vendorTechnicalSpecification,
          "statusId": setStatus('New'),
        }

        data.items.push(enquiryItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          rePrintSlip(response.data);
          $rootScope.stopLoader();
          NavigateToBack();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateEnquiry = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_FILL_IN.SAVE_API + self.model.enquiryId;

      var data = {
        "id": self.model.enquiryId,
        "remark": self.model.remark,
        "refNumber": self.model.refNumber,
        "refDate": moment(self.model.refDate).format('YYYY-MM-DD'),
        "items": [],
        "organizationId": $scope.organizationId,
        "userId": $scope.userId,
        "unitId": $scope.unitId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        var accessoriesDetails = [];

        angular.forEach(item.accessoriesDetails, function (details) {

          accessoriesDetails.push({
            "id": details.id,
            "qrAccessMapperId": details.qrAccessMapperId,
            "vendorDetails": details.vendorDetails,
            "name": details.name,
            "details":details.details
          });
        });

        if (!item.id) {
          var enquiryItem = {
            "vendorCop": item.vendorCOP,
            "remark": item.remark,
            "qrDetailId": item.qrDetailId,
            "vqrDetailId": item.vqrDetailId,
            "accessoriesDetails": accessoriesDetails,
            "vendorWarrantyPeriod": item.vendorWarrantyPeriod,
            "vendorExpectedSupport": item.vendorExpectedSupport,
            "vendorExpectedPayment": item.vendorExpectedPayment,
            "vendorTechnicalSpecification": item.vendorTechnicalSpecification,
            "statusId": setStatus('New'),
          }
        } else {
          var enquiryItem = {
            "id": item.id,
            "vendorCop": item.vendorCOP,
            "remark": item.remark,
            // "qrDetailId": item.qrDetailId,
            // "vqrDetailId": item.vqrDetailId,
            "accessoriesDetails": accessoriesDetails,
            "vendorWarrantyPeriod": item.vendorWarrantyPeriod,
            "vendorExpectedSupport": item.vendorExpectedSupport,
            "vendorExpectedPayment": item.vendorExpectedPayment,
            "vendorTechnicalSpecification": item.vendorTechnicalSpecification,
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

    var FillAccessoryDetails = function (item) {
      self.model.accessoryItem = item;
      self.model.expectedAccessories = item.accessoriesDetails;
    }

    var AddAccessory = function () {
      self.model.expectedAccessories.push({ "name": "", "details": "", "vendorDetails": "" });
    }

    var LessAccessory = function (item) {
      var index = self.model.expectedAccessories.indexOf(item);
      self.model.expectedAccessories.splice(index, 1);
    }

    var SetDecimal = function (item) {
      if (isNaN(item.vendorCOP) || !item.vendorCOP || item.vendorCOP == 0) {
        item.vendorCOP = 0;
      } else {
        item.vendorCOP = parseFloat(item.vendorCOP).toFixed(2);
      }
    }

    var ValidVendorDetails = function () {
      if(self.model.assetType.id == 2){
        var isValid = true;
        angular.forEach(self.model.expectedAccessories, function (detail, index) {
          if (!detail.vendorDetails) {
            self.model.vendordetailsValid[index] = true;
            isValid = false;
          }
  
          if (!detail.name) {
            self.model.vendorNameValid[index] = true;
            isValid = false;
          }
  
          if (!detail.name || !detail.vendorDetails) {
            growl.error('Please fill required fields');
          }
        });
  
       
      }else{
        isValid = true;
      }
      return isValid;
    }

    var SaveAccessoryDetails = function () {
      if (ValidVendorDetails()) {
        self.model.accessoryItem.accessoriesDetails = self.model.expectedAccessories;
        self.model.accessoryItem.allAccessoriesValid = false;
        ClosePopup('Close', 'AccessoriesDetailsQEModal');
      }
    }

    var ViewAccessories = function (item) {
      self.model.itemDetails.find(function (x) {
        if (item.itemId == x.itemId) {
          self.model.expectedAccessories = x.expectedAccessories;
        }
      });
    }

    var ViewTerms = function (item) {
      var popup = angular.element('#vendorSelectionDetailsModal2');
      popup.modal('show');
      self.model.termsVisible = 1;
      self.model.termsOfItem = item;
      self.model.itemDetails.find(function (x) {
        if (item.itemId == x.itemId) {
          self.model.warrantyPeriod = x.warrantyPeriod;
          self.model.supportTerms = x.supportTerms;
          self.model.paymentTerms = x.paymentTerms;
          self.model.technicalSpecification = x.technicalSpecification;
          self.model.vendorWarrantyPeriod = x.vendorWarrantyPeriod;
          self.model.vendorExpectedSupport = x.vendorExpectedSupport;
          self.model.vendorExpectedPayment = x.vendorExpectedPayment;
          self.model.vendorTechnicalSpecification = x.vendorTechnicalSpecification;
        }
      });
    }

    var ValidTerms = function () {
      var wp = self.model.termsOfItem["vendorWarrantyPeriod"] = self.model.vendorWarrantyPeriod;
      var es = self.model.termsOfItem["vendorExpectedSupport"] = self.model.vendorExpectedSupport;
      var ep = self.model.termsOfItem["vendorExpectedPayment"] = self.model.vendorExpectedPayment;
      var ts = self.model.termsOfItem["vendorTechnicalSpecification"] = self.model.vendorTechnicalSpecification;

      var isValid = true;
      if(self.model.assetType.id ==2){
      if (!wp || !es || !ep || !ts) {
        growl.error('Please enter all support terms');
        isValid = false;
      }
    }
    else{
      if (!ep) {
        growl.error('Please enter expected payments terms');
        isValid = false;
      }
    }

      return isValid;
    }

    var SaveTerms = function () {
      if (ValidTerms()) {
        self.model.termsOfItem["vendorWarrantyPeriod"] = self.model.vendorWarrantyPeriod;
        self.model.termsOfItem["vendorExpectedSupport"] = self.model.vendorExpectedSupport;
        self.model.termsOfItem["vendorExpectedPayment"] = self.model.vendorExpectedPayment;
        self.model.termsOfItem["vendorTechnicalSpecification"] = self.model.vendorTechnicalSpecification;
        self.model.termsOfItem.allTermsValid = false;
        ClosePopup('Close', 'vendorSelectionDetailsModal2');
      }
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

    var OpenRefDate = function () {
      self.model.refDateOpened = true;
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

      if (!self.model.vendorId || !self.model.vendorName) {
        self.model.isVendorValid = true;
        isValid = false;
      }

      if (!self.model.assetType.id) {
        self.model.isValidAssetType = true;
        isValid = false;
      }

      if (!self.model.store.id || !self.model.vendorId || !self.model.vendorName || !self.model.assetType.id) {
        growl.error('Please select required fields');
        isValid = false;
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add items to save quotation fill in');
        isValidItem = false;
      } else {
        angular.forEach(self.model.itemDetails, function (item, index) {
          if (!parseFloat(item.vendorCOP)) {
            self.model.vendorCOP[index] = true;
            growl.error("Please enter vendor COP");
            isValidItem = false;
          }

          angular.forEach(item.accessoriesDetails, function (accessory, idx) {
            if (!accessory.vendorDetails || !accessory.name) {
              if(self.model.assetType.id == 2){
                growl.error("Please enter accessory details");
                item.allAccessoriesValid = true;
                isValidItem = false;
              }
            
            }
          });
         if(self.model.assetType.id == 2){
          if (!item.vendorWarrantyPeriod || !item.vendorExpectedSupport || !item.vendorExpectedPayment || !item.vendorTechnicalSpecification) {
            growl.error("Please enter all support terms");
            item.allTermsValid = true;
            isValidItem = false;
          }
         }
         else{
       if (!item.vendorExpectedPayment) {
            growl.error("Please enter expected payment terms");
            item.allTermsValid = true;
            isValidItem = false;
          }
         }
         
         
        });

        if (!self.model.vendorId || !self.model.vendorName) {
          self.model.isVendorValid = true;
          growl.error('Please enter vendor name');
          isValidItem = false;
        }

        if (!validateReferenceDate()) {
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
          if (!!po.items) {
            po.items.forEach(function (x) {
              x.isSelectedItem = false;
              x.isChecked = false;
              po.isChecked = false;
            });
          }
        } else {
          if (!!po.items) {
            po.items.forEach(function (x) {
              self.model.indentItemDetails.push(x);
              x.isSelectedItem = true;
              x.isChecked = true;
              po.isChecked = true;
              self.model.intendItemIds.push(x.itemId);
            });
          }
        }
      });
    }

    var SelectIndentItem = function (item, indent) {
      var itemIds = self.model.intendItemIds;
      if (self.model.isSelectedIndent != indent) {
        angular.forEach(self.model.qrItemList, function (po) {
          if (po.id !== indent.id) {
            if (!!po.items) {
              po.items.forEach(function (x) {
                x.isSelectedItem = false;
                x.isChecked = false;
                po.isChecked = false;
              });
            }
          }
        });
      }

      if (item.isSelectedItem) {
        item.isChecked = true;
        indent.isChecked = true;
        itemIds.push(item.itemId);
        self.model.isSelectedIndent = indent;
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
        //  self.model.isSelectedIndent = null;
      }
    }

    var NavigateToBack = function () {
      $state.go('quotationFillIn');
    }

    var Expand = function (item, qrItemList) {
      angular.forEach(qrItemList, function (value) {
        value.Expanded = false;
      });

      item.Expanded = !item.Expanded;
    }

    var ConfirmRemove = function (item) {
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var newArray = [];

      self.model.itemDetails.map(function (obj) {
        if (obj !== item) {
          newArray.push(obj);
        }
      });
      self.model.itemDetails = newArray;
      var popup = angular.element('#confirmRemove');
      popup.modal('hide');
    }

    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function (store) {
      if (self.model.itemDetails.length > 0) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        $("#confirmStore").modal()
      }
    } 

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      ClosePopup('close', 'confirmStore');
      self.model.itemDetails = [];
      self.model.vqrNumber = '';
      self.model.vqrDate = new Date().format('DD/MM/YYYY');
      self.model.qrDate = new Date().format('DD/MM/YYYY');
      self.model.qrNumber = '';
    }

    // asset type change confirmation
    var SetOldValueAssetType = function (assetType) {
      self.model.oldAssetType = assetType;
      self.model.assetType = self.model.oldAssetType;
    }

    var ChangeFromAssetType = function (assetType) {
      if (!!self.model.itemDetails.length) {
        self.model.assetType = self.model.oldAssetType;
        self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
        OpenPopup('Open', 'confirmAssetTypePopup');
      }
    }

    var ConfirmAssetTypePopup = function () {
      self.model.assetType = self.model.assetTypeNew;
      ClosePopup('close', 'confirmAssetTypePopup');
      self.model.itemDetails = [];
    }
    var validateReferenceDate = function () {
      var isValidItem = true;
      if (!self.model.refDate) {
        growl.error('Please enter reference date');
        isValidItem = false;
      }
      else if (!self.model.refNumber) {
        growl.error('Please enter reference number');
        isValidItem = false;
      }
      return isValidItem;
    }

    var rePrintSlip = function (quotationFill) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_Vendor_quotation.rpt",
        "reportFormat": "PDF",
        "exportFileName": "SalesReturnBillSlip",
        "reportParams": [
          {
            "paramName": "Id",
            "paramType": "Integer",
            "paramValue": quotationFill.id
          }
        ]
      };
    reportService.generate(REPORT_API_URI1, data);
  }
  
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewquotationFillIn", {
      url: "/addNewquotationFillIn",
      templateUrl: 'views/procurement/quotation/add-new-quotation-fill-in.html',
      controller: "AddNewQuotationFillIn.Controller",
      controllerAs: "vm",
      title: 'Quotation Fill In'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewQuotationFillIn.Controller", addNewQuotationFillInController);
})();