(function () {
  "use strict";

  function poCreationController($scope, $rootScope, reportService ,$http, $filter, $state, screenDataService,PaginationService, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      var date = new Date();
      self.model = {
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '10',
          search: '',
          searchData: PaginationService.searchData,
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
//          minDate : date.setDate((new Date()).getDate() - 90),
          minDate : moment(new Date()).subtract(180, 'days').toDate(),
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        selectAllRows: false,
        SelectedOrderIds: [],
        storeList: [{
          "store": "Select Store"
        }],
        fromstoreList: [{
          "store": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }]
      };

      cancelReasonService.GetReason().then(function(reason) {
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetPurchaseOrderList = GetPurchaseOrderList;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllOrders = SelectAllOrders;
      self.model.SelectPurchaseOrder = SelectPurchaseOrder;
      self.model.isDisable = isDisable;
      self.model.UpdatePurchaseOrder = UpdatePurchaseOrder;
      self.model.forcefullyClosed = forcefullyClosed;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.rePrintBillSlip=rePrintBillSlip;
       self.model.AddItemName = AddItemName;
      self.model.SearchItems = SearchItems;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];

      PopulateDropdown();
    }

    var SearchItems = function (search) {
       if ((!self.model.prevSearch || self.model.prevSearch != search) ) {
        self.model.prevSearch = search;

        var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId + "/" + false + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
          .then(function (response) {
            $rootScope.stopLoader();
            if (!!response.data && response.data.length > 0) {
              self.model.items = response.data;
              return $filter('filter')(self.model.items, {
                $: search
              });
            } else {
              NoItemFound(search);
              return self.model.items;
            }
          }, function (err) {
            NoItemFound(search);
            $rootScope.stopLoader();
            return self.model.items;
          });
      } else {
        if (!!self.model.items && self.model.items.length > 0 &&
          self.model.items[0].itemFound != undefined && !self.model.items[0].itemFound) {
          return NoItemFound(search);
        } else {
          return $filter('filter')(self.model.items, {
            $: search
          });
        }
      }
    }

    var NoItemFound = function (search) {
      var item = {
        "itemFound": false,
        "details": "No item found with item code or name " + search
      };
      self.model.items = [item];
    }

    function AddItemName (vendor) {
      var abc = vendor.details.split('|');
      self.model.itemName = abc[1];
      self.model.itemId = vendor.id;
    }

    var setStatus = function (x){
      var status = self.model.statusList.find(function(obj){
        return obj.status == x;
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

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetPurchaseOrderList()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'poCreation'){
            var requestData = dataobj.searchrequest;
            
            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
                return requestData.store.id == store.id;
            });
            self.model.fromstore = self.model.fromstoreList.find(function (store) {
              return requestData.fromstore.id == store.id;
            });
           /* self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
            self.model.vendorId = requestData.vendorId;
            self.model.vendorName = requestData.vendorName;
            self.model.purchaseOrderNo = requestData.purchaseOrderNo;  */
            self.model.fromDate = new Date();
            self.model.toDate = new Date();
          }
        });

        GetPurchaseOrderList();
      }
    }

    var PopulateDropdown = function () {
      self.model.store = self.model.storeList[0];
      self.model.fromstore = self.model.fromstoreList[0];
      self.model.status = self.model.statuses[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.deliveryStores);
          self.model.fromstoreList = self.model.fromstoreList.concat(response.data.requestingStores);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          $rootScope.stopLoader();
          initialCall();
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

    function AddVendorName (vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be less than to Date');
        isValid = false;
      } else if (self.model.fromDate == self.model.toDate) {
        isValid = true;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }

    var GetPurchaseOrderList = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.GET_PO_API;

        if (!self.model.vendorName) {
          self.model.vendorId = undefined;
          self.model.vendorName = undefined;
        }

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "vendorId": self.model.vendorId,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "poNumber": self.model.purchaseOrderNo ? self.model.purchaseOrderNo : undefined,
          "requestingStoreId": !!self.model.fromstore && !! self.model.fromstore.id ? self.model.fromstore.id : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId": $scope.unitId,
          "screenId": 1,
          "itemId": self.model.itemId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.purchaseOrders = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.purchaseOrders, self.model.pagination);
            }
            self.model.SelectedOrderIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.status);
          });

        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "status": self.model.status,
            "store": self.model.store,
            "fromstore": self.model.fromstore,
            "vendorId": self.model.vendorId,
            "vendorName": self.model.vendorName,
            "purchaseOrderNo": self.model.purchaseOrderNo
          },
          screenName: 'poCreation'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addItemPO');
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailsPOCreation');
    }

    var NavigateToEdit = function (purchase) {
      purchase.title = 'Creation';
      commonDetailService.setDataId(purchase);
      $state.go('addItemPO');
    }

    var SelectAllOrders = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedOrderIds = [];
        self.model.purchaseOrders.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            self.model.SelectedOrderIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedOrderIds = [];
        self.model.purchaseOrders.forEach(function (purchase) {
          purchase.isSelected = false;
        });
      }
    }

    var isDisable = function (purchase) {
      if (purchase.status != 'Saved') {
        return true;
      }
      return false;
    }

    var isSelected = function (purchase) {
      if (!isDisable(purchase))
        return purchase.isSelected;
      else
        return true;
    }

    var SelectPurchaseOrder = function (purchase) {
      var purchaseIds = self.model.SelectedOrderIds;

      if (purchase.isSelected) {
        purchaseIds.push(purchase.id);
      } else {
        var index = purchaseIds.indexOf(purchase.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.purchaseOrders.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var ValidateAndUpdate = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.id = data.id;
      popup.modal('show');
    }

    var ValidateStatus = function (){
      var isValid = true;
      if (!self.model.SelectedOrderIds.length) {
        isValid = false;
      }
      return isValid;
    }

    var ValidetReason = function () {
      var isValid = true;

      if (!self.model.cancelReason) {
        self.model.isReasonValid = true;
        isValid = false;
      }
      
      return isValid;
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }

    var forcefullyClosed = function (purchase) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.FORCEFULLY_CLOSED_API + purchase.id;
      var data = {
        "userId": $scope.userId
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
      function (response) {
        $rootScope.stopLoader();
        GetPurchaseOrderList();
        growl.success(response.data.message);
      },
      function (err) {
        $rootScope.stopLoader();
        growl.error(err.data.message);
      });
    }

    var UpdatePurchaseOrder = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.APPROVE_PO_API;

          var data = {
            "poIdList": self.model.SelectedOrderIds,
            "statusId": setStatus('New'),
            "approvalStatusId": setStatus('Pending Approval'),
            "userId": $scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedOrderIds = [];
              ModalDismiss('confirmSave');
              $rootScope.stopLoader();
              GetPurchaseOrderList();
              self.model.selectAllRows = false;
              growl.success(response.data.message);
            },
            function (err) {
              ModalDismiss('confirmSave');
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select PO to approve');
          ModalDismiss('confirmSave');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.CANCEL_PO_API + self.model.id;
          
          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "cancelNote": self.model.rejectNote,
            "userId": $scope.userId
          }

          $rootScope.startLoader();         
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedOrderIds = [];
              ModalDismiss('cancelPO');
              GetPurchaseOrderList();

              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    var rePrintBillSlip = function (grn) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_PO.rpt",
        "reportFormat": "PDF",
        "exportFileName": "Grn",
        "reportParams": [
          {
            "paramName": "POId",
            "paramType": "Integer",
            "paramValue": grn.id
          },
          {
            "paramName": "StoreId",
            "paramType": "Integer",
            "paramValue": grn.storeId
          }
        ]
      };
      reportService.generate(REPORT_API_URI1, data);
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("POCreation", {
      url: "/POCreation",
      templateUrl: 'views/procurement/quotation/PO-creation.html',
      controller: "PoCreation.Controller",
      title: 'Purchase Order Creation',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 179);
				 	});
					if(arr.length <= 0){
						$state.go("accessDenied")
						deferred.reject();
					 } else {
						deferred.resolve();
					 }
		        });
		
		        return deferred.promise;
		      }
		 },
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("PoCreation.Controller", poCreationController);
})();