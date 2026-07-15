(function () {
    "use strict";
  
    function addNewAuctionQuotationFillinController($scope, $rootScope, reportService, $http, $filter, $state, GenericService, commonDetailService, CurrencyService, growl, StatusService, CONSTANTS) {
      var self = this;
  
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
      function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
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
          dateOptions: {
            formatYear: 'yyyy',
            showWeeks: false,
            maxDate: new Date()
          },
        };
  
        self.model.OpenDueDate = OpenDueDate;
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.OpenRefDate = OpenRefDate;
        self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
        self.model.PopulateProductCategory = PopulateProductCategory;
        self.model.NavigateToBack = NavigateToBack;
        self.model.SearchVendorNames = SearchVendorNames;
        self.model.AddVendorName = AddVendorName;
        self.model.OpenPopup = OpenPopup;
        self.model.ClosePopup = ClosePopup;
        self.model.ViewAccessories = ViewAccessories;
        self.model.ViewTerms = ViewTerms;
        self.model.SetOldValueStore = SetOldValueStore;
        self.model.ChangeFromStore = ChangeFromStore;
        self.model.ConfirmStorePopup = ConfirmStorePopup;
        self.model.SetOldValueAssetType = SetOldValueAssetType;
        self.model.ChangeFromAssetType = ChangeFromAssetType;
        self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
  
        PopulateDropdown();
      }
  
      var PopulateDropdown = function () {
        try {
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
  
      var ValidVendorDetails = function () {
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
  
        return isValid;
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
        if (!wp || !es || !ep || !ts) {
          growl.error('Please enter all support terms');
          isValid = false;
        }
  
        return isValid;
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
  
      var NavigateToBack = function () {
        $state.go('auctionQuationFillin');
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
      
      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("addNewAuctionQuotationFillin", {
        url: "/addNewAuctionQuotationFillin",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/add-new-auction-quotation-fill-in.html',
        controller: "addNewAuctionQuotationFillin.Controller",
        controllerAs: "vm"
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("addNewAuctionQuotationFillin.Controller", addNewAuctionQuotationFillinController);
  })();