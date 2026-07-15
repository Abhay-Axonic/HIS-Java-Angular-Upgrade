(function () {
    "use strict";
  
    function addNewAuctionEnquirySentToVendorController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, CurrencyService, growl, StatusService, CONSTANTS,$cookies) {
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
          dateOptions: {
            formatYear: 'yyyy',
            showWeeks: false
          },
        };
  
        self.model.OpenDueDate = OpenDueDate;
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
        self.model.PopulateProductCategory = PopulateProductCategory;
        self.model.NavigateToBack = NavigateToBack;
        self.model.SearchVendorNames = SearchVendorNames;
        self.model.AddVendorName = AddVendorName;
        self.model.SetOldValueStore = SetOldValueStore;
        self.model.SetOldValueAssetType = SetOldValueAssetType;
        self.model.GetScrapPopup = GetScrapPopup;
        self.model.ViewTerms = ViewTerms;
        self.model.ConfirmRemove = ConfirmRemove;
        
        PopulateDropdown();
      }
  
      var PopulateDropdown = function () {
        try 
        {
          $rootScope.startLoader();
          self.model.store = self.model.stores[0];
          self.model.assetType = self.model.assetTypes[0];
          
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
  
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.stores = self.model.stores.concat(response.data.store);
              self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
  
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
          "detail": "Vendor not found with name " + search
        };
        self.model.vendorNames = [item];
        return self.model.vendorNames;
      }
  
      function AddVendorName(vendor) {
        self.model.vendorName = vendor.vendorName;
        self.model.vendorId = vendor.id;
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
      
      var GetScrapPopup = function () {
        if (ValidateDetails()) {
          if (!!self.model.enquiryId) {
            self.model.fromDate = new Date(self.model.selectedScrapDate);
          } else {
            self.model.fromDate = new Date();
            self.model.scrapNumber = undefined;
          }

          self.model.scrapItemList = [];
          self.model.toDate = new Date();
          self.model.searchScrap = undefined;
          self.model.scrapDetails = [];
          self.model.intendItemIds = [];
          var popup = angular.element('#GetScrapList');
          self.model.action = 'Open';
          popup.modal('show');
        }
      }
      
      var ViewTerms = function (item) {
        // self.model.itemDetails.find(function(x){
        //   if (item.itemId == x.itemId) {
        //     self.model.warrantyPeriod = x.warrantyPeriod;
        //     self.model.supportTerms = x.supportTerms;
        //     self.model.paymentTerms = x.paymentTerms;
        //     self.model.technicalSpecification = x.technicalSpecification;
        //   }
        // });
  
        var popup = angular.element('#vendorSelectionDetailsModal2');
        popup.modal('show');
      }

      var ConfirmRemove = function(item){
        var popup = angular.element('#confirmRemove');
        popup.modal('show');
        self.model.itemForRemove = item;
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
      
      var NavigateToBack = function () {
        $state.go('auctionEnquirySentToVendor');
      }

      var SetOldValueStore = function(store){
        self.model.oldStore = store;
        self.model.store = self.model.oldStore;
      }
      
      // asset type change confirmation
      var SetOldValueAssetType = function(assetType){
        self.model.oldAssetType = assetType;
        self.model.assetType = self.model.oldAssetType;
      }
      
      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("addNewAuctionEnquirySentToVendor", {
        url: "/addNewAuctionEnquirySentToVendor",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/add-new-auction-enquiry-sent-to-vendor.html',
        controller: "addNewAuctionEnquirySentToVendor.Controller",
        controllerAs: "vm",
        title: 'Auction Enquiry Sent to Vendor'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("addNewAuctionEnquirySentToVendor.Controller", addNewAuctionEnquirySentToVendorController);
  })();