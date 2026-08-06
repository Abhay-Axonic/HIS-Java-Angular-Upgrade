(function () {
  "use strict";

  function addNewEnquirySentController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, CurrencyService, growl, StatusService, $cookies, CONSTANTS) {
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
          "storeName": "Select Store"
        }],
        toStores: [{
          "storeName": "Select Store"
        }],
        assetTypes: [{
          "type": "Select Asset Type"
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

      CurrencyService.GetCurrency().then(function(currency){
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
      self.model.ViewAccessories = ViewAccessories;
      self.model.ViewTerms = ViewTerms;
      
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      $scope.selectedVendersList=[];
      $scope.selectedStoreList = [];
      $scope.selecteddata ={};
    
      PopulateDropdown();
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
        self.model.assetType = self.model.assetTypes[0];
        
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.requestingStores);
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
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

    
    $scope.venderMultiSelectList =[];
    $scope.displayVenderData="";
    $scope.allvendors = function(){

        var data = "";
        var vendorNameAfterEditInitiate="noSelectedVendorFound";
        var URI = CONSTANTS.GLOBAL.ALL_VENDOR_SEARCH_API + "unit/"+$scope.unitId+"/vendorName/"+vendorNameAfterEditInitiate;
        GenericService.serviceAction("GET", URI, data).then(function(response) {
          if (response.status == 200) {
           // $scope.storeList = response.data;    
            angular.forEach(response.data,
                function(value,index) {
                  var data = {};
                  data.id = value.id;
//                  data.detail = value.detail;
                  data.label = value.detail;
                  $scope.venderMultiSelectList.push(data);
            });
          
          }
        });	
      
    	
    }
    $scope.allvendors();
    
    
    
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
          self.model.fromDate = new Date(self.model.selectedQRDate);
          self.model.qrNumber = null; 
        } else {
          self.model.fromDate = new Date();
     
        }

        self.model.qrItemList = [];
        self.model.toDate = new Date();
        self.model.searchQR = undefined;
        self.model.qrDetails = [];
        self.model.intendItemIds = [];
        var popup = angular.element('#GetQRList');
        self.model.action = 'Open';
        popup.modal('show');
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
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.ENQUIRY_SENT_TO_VENDOR.MULTI_SEARCH_QR_API;
      var getQr = undefined;
      if (!!self.model.enquiryId) {
        getQr = false;
      }

      var venders = [];
      $scope.displayVenderData  = $scope.selectedVendersList;
      angular.forEach($scope.selectedVendersList, function (item) {
        var temp1 = {
          "vendorId": item.id
        }
      
        venders.push(temp1);
      });
      // todo
      $rootScope.startLoaderPopup();
      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "assetTypeId": self.model.assetType.id,
        "qrNumber": self.model.qrNumber ? self.model.qrNumber : undefined,
        "storeId": self.model.store.id,
        "venders" : venders,
        "getQr": getQr,
        "qrId": self.model.selectedQRId,
        "id": self.model.enquiryId,
        "unitId": $scope.unitId
      }

      if(data.getQr != undefined && data.qrId != undefined && data.id != undefined){
        data.getQr = undefined;
        data.qrId = undefined;
        data.id = undefined;
      }

      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          self.model.qrItemList = response.data;
          self.model.qrDetails = [];
          self.model.intendItemIds = [];
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
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.ENQUIRY_SENT_TO_VENDOR.DETAILS_QR_ITEMS;

        var data = self.model.qrDetails;

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var qrResponse = response.data;
            self.model.tempQrId = response.data[0].id;
            if (self.model.tempQrId != self.model.qrId) {
              if (!self.model.enquiryId) {
                self.model.itemDetails = [];
              }
            }
            
            
            updateItemDetails(qrResponse);
            self.model.qrId = response.data[0].id;
            self.model.requestTypeId = response.data[0].requestTypeId;
            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            self.model.qrItemList = [];

            self.model.ClosePopup('close', 'GetQRList');
            angular.forEach($scope.selectedVendersList, function (vendor,key) 
                    {
            	  angular.forEach(self.model.itemDetails, function (value,key)
            			   {
            		   var obj = angular.copy(value);
                 	  obj.vid=vendor.id
                       $scope.listOfData.push(obj);
            		   
            			   });
            			
                     });
            console.log('scope.listOfData',JSON.stringify($scope.listOfData));
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
    	 $scope.listOfData=[];
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
    }

    var SelectedQRItems = function () {
      var qr = self.model.isSelectedIndent;
      if (qr) {
        self.model.qrDetails = [];
        var qrData = {
          "qrId": qr.id,
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
    //$scope.listOfData=[];
    var GetEnquiry = function (enquiryId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.ENQUIRY_SENT_TO_VENDOR.SAVE_API + $scope.organizationId  + '/' + enquiryId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.enquiryId = response.data.id;
          self.model.selectedQRId = response.data.qrId;
          self.model.qrNumber = response.data.qrNumber;
          self.model.selectedQRDate = response.data.qrDate;
          self.model.requestTypeId = response.data.requestTypeId;
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });

          self.model.assetType = self.model.assetTypes.find(function (asset) {
            return response.data.assetTypeId == asset.id;
          });

          self.model.dueDate = new Date(response.data.dueDate);
          self.model.vendorName = response.data.vendorName;
          self.model.vendorId = response.data.vendorId;
          self.model.itemDetails = [];
          $scope.listOfData = [];
          $scope.selectedVendersList=[];
          var selectedData={
         		 id:response.data.vendorId,
                  label:response.data.vendorName
                    
           }
          $scope.selectedVendersList.push(selectedData);          
          angular.forEach(response.data.items, function (value, key) {
            var enquiryItem = {
              "id": value.id,
              "itemId": value.itemId,
              "itemCode": value.itemCode,
              "itemName": value.itemName,
              "uomType": value.uomType,
              "uomUnit": value.uomUnit,
              "qrQuantity": value.qrQuantity,
              "prList": value.prList,
              "expectedAccessories": value.expectedAccessories,
              "warrantyPeriod": value.warrantyPeriod,
              "supportTerms": value.supportTerms,
              "paymentTerms": value.paymentTerms,
              "technicalSpecification": value.technicalSpecification,
              "vid" :response.data.vendorId
            }

            $scope.listOfData.push(enquiryItem);
          });

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

   
    $scope.selectedVendorsList =[];
    var CreateEnquiry = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.ENQUIRY_SENT_TO_VENDOR.SAVE_API;
      var venders = [];

      angular.forEach($scope.selectedVendersList, function (item) {
        var temp1 = {
          "vendorId": item.id
        }
      
        venders.push(temp1);
        
      });
      
      var data = {
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        // todo: API changes
        "assetTypeId": self.model.assetType.id,
        "qrId": self.model.qrId,
        "dueDate": moment(self.model.dueDate).format('YYYY-MM-DD'),
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "venders" :venders,
        "organizationId": $scope.organizationId,
        "userId": $scope.userId,
        "unitId": $scope.unitId
      };

     /* angular.forEach(self.model.itemDetails, function (item, key) {
        var enquiryItem = {
          "itemId": item.itemId,
          // "qrQuantity": item.qrQuantity,
          // todo: API changes
          "qrDetailId": item.qrDetailId,
          "statusId": setStatus('New'),
        }

        if (self.model.assetType.id == 1) {
          enquiryItem.uomTypeId = item.uomTypeId;
          enquiryItem.uomUnitId = item.uomUnitId;
        } else {
          enquiryItem.uomTypeId = undefined;
          enquiryItem.uomUnitId = undefined;
        }

        data.items.push(enquiryItem);
       
      });*/
      
      
      angular.forEach($scope.listOfData, function (item, key) {
    	  var enquiryItem = {
    			  "vendorId" : item.vid,
    	          "itemId": item.itemId,
    	          // "qrQuantity": item.qrQuantity,
    	          // todo: API changes
    	          "qrDetailId": item.qrDetailId,
    	          "statusId": setStatus('New'),
    	        }

    	        if (self.model.assetType.id == 1) {
    	          enquiryItem.uomTypeId = item.uomTypeId;
    	          enquiryItem.uomUnitId = item.uomUnitId;
    	        } else {
    	          enquiryItem.uomTypeId = undefined;
    	          enquiryItem.uomUnitId = undefined;
    	        } 	
    	  data.items.push(enquiryItem);
    }); 
      
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
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

    var UpdateEnquiry = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.ENQUIRY_SENT_TO_VENDOR.SAVE_API + self.model.enquiryId;

      var data = {
        "dueDate": moment(self.model.dueDate).format('YYYY-MM-DD'),
        // "assetTypeId": self.model.assetType.id,
        "items": [],
        "organizationId": $scope.organizationId,
        "userId": $scope.userId,
        "unitId": $scope.unitId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        if (!!item.id) {
          var enquiryItem = {
            "id": item.id
          }
        } else {
          var enquiryItem = {
            "itemId": item.itemId,
            "qrDetailId": item.qrDetailId,
            "statusId": setStatus('New')
          }
        }

        if (self.model.assetType.id == 1) {
          enquiryItem.uomTypeId = item.uomTypeId;
          enquiryItem.uomUnitId = item.uomUnitId;
        } else {
          enquiryItem.uomTypeId = undefined;
          enquiryItem.uomUnitId = undefined;
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
    	$scope.listOfData.find(function(x){
        if (item.itemId == x.itemId) {
          self.model.prDetailList = x.prList;
        }
      });
    }

    var ViewAccessories = function (item) {
     
          self.model.expectedAccessories = item;
     
    }
    
    
    var ViewAccessories1 = function (item) {
        self.model.itemDetails.find(function(x){
          if (item.itemId == x.itemId) {
            self.model.expectedAccessories = x.expectedAccessories;
          }
        });
      }

    var ViewTerms = function (x) {
      
          self.model.warrantyPeriod = x.warrantyPeriod;
          self.model.supportTerms = x.supportTerms;
          self.model.paymentTerms = x.paymentTerms;
          self.model.technicalSpecification = x.technicalSpecification;
       
     

      var popup = angular.element('#vendorSelectionDetailsModal2');
      popup.modal('show');
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

      if (!self.model.store.id) {
        self.model.isStoreValid = true;
        isValid = false;
      }

      if (!self.model.assetType.id) {
        self.model.isValidAssetType = true;
        isValid = false;
      }

      if (!self.model.store.id || !self.model.assetType.id) {
        growl.error('Please select required fields');
        isValid = false;
      }

      if($scope.selectedVendersList.length == 0){
        growl.error('Please select vendor');
        isValid = false;
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      self.model.itemDetails =  $scope.listOfData;
      if (self.model.itemDetails.length <= 0 ) {
        growl.error('Please add items to save enquiry sent to vendor');
        isValidItem = false;
      } else {
        // if ($scope.selectedVendersList.length == 0) {
        //   self.model.isVendorValid = true;
        //   growl.error('Please select required fields');
        //   isValidItem = false;
        // }
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
      angular.forEach(self.model.qrItemList, function(po){
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
      if(self.model.isSelectedIndent != indent){
        angular.forEach(self.model.qrItemList, function(po){
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
        self.model.isSelectedIndent = indent;
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
      }
    }

    var NavigateToBack = function () {
      $state.go('enquirySentToVendor');
    }

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }

    var ConfirmRemove = function(item, index){
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.itemForRemove = item;
      $scope.itemIndex=index;
    }

   /* var RemoveItem = function (item) {
      var newArray = [];

      self.model.itemDetails.map(function(obj){
        if(obj !== item){
          newArray.push(obj);
        }
      });
      self.model.itemDetails = newArray;
      var popup = angular.element('#confirmRemove');
      popup.modal('hide');
    }*/
    
    
    var RemoveItem = function (item) {
    	$scope.listOfData.splice($scope.itemIndex, 1);
        var newArray = [];

        self.model.itemDetails.map(function(obj){
          if(obj !== item){
            newArray.push(obj);
          }
        });
        self.model.itemDetails = newArray;
        var popup = angular.element('#confirmRemove');
        popup.modal('hide');
      }

    var SetOldValueStore = function(store){
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function(store){
      if (!!self.model.itemDetails.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        OpenPopup('Open', 'confirmStore');
      }
    }

    var ConfirmStorePopup = function(){
      self.model.store = self.model.storeNew;
      ClosePopup('close', 'confirmStore');
      self.model.itemDetails = [];
    }

    // asset type change confirmation
    var SetOldValueAssetType = function(assetType){
      self.model.oldAssetType = assetType;
      self.model.assetType = self.model.oldAssetType;
    }

    var ChangeFromAssetType = function(assetType){
      if (!!self.model.itemDetails.length) {
        self.model.assetType = self.model.oldAssetType;
        self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
        OpenPopup('Open', 'confirmAssetTypePopup');
      }
    }

    var ConfirmAssetTypePopup = function(){
      self.model.assetType = self.model.assetTypeNew;
      ClosePopup('close', 'confirmAssetTypePopup');
      self.model.itemDetails = [];
    }

    initializeController();
    
    $scope.getVendorName=function(id){
    	  var label = $.grep($scope.venderMultiSelectList, function (vendor) {			
    	      return vendor.id  == id;	      
    	  })[0].label;
    	  return label;
      }
  }
 
  function config($stateProvider) {
    $stateProvider.state("addNewEnquirySentToVendor", {
      url: "/addNewEnquirySentToVendor",
      templateUrl: 'views/procurement/quotation/add-new-enquiry-sentto-vendor.html',
      controller: "AddNewEnquirySent.Controller",
      controllerAs: "vm",
      title: 'Enquiry Sent to Vendor'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewEnquirySent.Controller", addNewEnquirySentController);
})();