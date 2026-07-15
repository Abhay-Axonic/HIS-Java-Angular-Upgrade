(function () {
  "use strict";

  function POFinalApprovalController($scope, $rootScope, $http, $filter, $state, screenDataService,PaginationService, $cookies, CONSTANTS, GenericService, commonDetailService, rejectReasonService, StatusService, growl, reportService) {
    var self = this;
    var rejectData;

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
          maxDate: new Date(),
          /*minDate : date.setDate((new Date()).getDate() - 90),*/
          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        selectAllRows: false,

        SelectedOrderIds: [],
        selectedItems: [],
        SelectedRequestCancelId: null,
        vendorId: null,
        storeList: [{
          "store": "Select Store"
        }],
        fromStoreList: [{
          "store": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }],
        search: '',
        prevSearch: '',
        vendorNames: []
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });

      //Methods
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetPurchaseOrderList = GetPurchaseOrderList;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.SelectAllOrders = SelectAllOrders;
      self.model.SelectPurchaseOrder = SelectPurchaseOrder;
      self.model.isDisable = isDisable;
      self.model.OpenPopup = OpenPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.RejectPO = RejectPO;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.AcceptReject = AcceptReject;
      self.model.selectedApproveRejectList = [];
      self.model.selectedPOId = 0;
      self.model.PrintPOApprovalReport=PrintPOApprovalReport;
      self.model.ModalDismissOnClose = ModalDismissOnClose;
      self.model.PurchaseOrderApproval=PurchaseOrderApproval;
      self.model.pharmacyGet=pharmacyGet;
      PopulateDropdown();
      pharmacyGet();
    }

    var setStatus = function (x) {
      var status = self.model.statusList.find(function (obj) {
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
          if (dataobj.screenName == 'poApproval'){
            var requestData = dataobj.searchrequest;
            
            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
                return requestData.store.id == store.id;
            });
            self.model.fromstore = self.model.fromStoreList.find(function (store) {
              return requestData.fromstore.id == store.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
            self.model.vendorId = requestData.vendorId;
            self.model.vendorName = requestData.vendorName;
            self.model.purchaseOrderNo = requestData.purchaseOrderNo;
          }
        });

        GetPurchaseOrderList();
      }
    }

    var PopulateDropdown = function () {
      var purchaseRequests = self.model;
      purchaseRequests.store = purchaseRequests.storeList[0];
      purchaseRequests.fromstore = purchaseRequests.fromStoreList[0];
      purchaseRequests.status = purchaseRequests.statuses[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          purchaseRequests.storeList = purchaseRequests.storeList.concat(response.data.deliveryStores);
          purchaseRequests.fromStoreList = purchaseRequests.fromStoreList.concat(response.data.requestingStores);
          purchaseRequests.statuses = purchaseRequests.statuses.concat(response.data.status);
          $rootScope.stopLoader();
          initialCall();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    var PurchaseOrderApproval = function (id) {
         
    	 
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.APPROVAL_API + '/' +id;
        var data={
        		"userId":$scope.userId
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
        	  growl.success(response.data.message);
        	  GetPurchaseOrderList();
            $rootScope.stopLoader();
            
          },
          function (err) {
        	  growl.error(err.data.message);
            $rootScope.stopLoader();
          });
    	 
      }
    
    var pharmacyGet = function () {
		var confignew = self.model;
		var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.APPLICATION_CONFIGURATION_GET_API + $scope.unitId;
		$rootScope.startLoader();

		GenericService.serviceAction("GET", URI, {}).then(
			function (response) {
				$rootScope.isPoApproval = response.data[0].isPoApproval;
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
    var NavigateToEdit = function (purchase) {
        purchase.title = 'Final Approval';
        commonDetailService.setDataId(purchase);
        $state.go('addItemPO');
      }
    function nameNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor Not found with name " + search,
        "search": search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName(vendor) {
      if (!vendor.id) {
        self.model.vendorName = vendor.search;
        self.model.vendorId = undefined;
      } else {
        self.model.vendorName = vendor.vendorName;
        self.model.vendorId = vendor.id;
      }
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be less than to date');
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
        var purchaseOrder = self.model;
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.Final_SEARCH_API;

        var data = {
          "fromDate": moment(purchaseOrder.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(purchaseOrder.toDate).format('YYYY-MM-DD'),
          "requestingStoreId": !!purchaseOrder.fromstore && !!purchaseOrder.fromstore.id ? purchaseOrder.fromstore.id : undefined,
          "storeId": !!purchaseOrder.store && !!purchaseOrder.store.id ? purchaseOrder.store.id : undefined,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "vendorId": self.model.vendorId ? self.model.vendorId : undefined,
          "poNumber": self.model.purchaseOrderNo ? self.model.purchaseOrderNo : undefined,
          "statusId": !!purchaseOrder.status && !!purchaseOrder.status.id ? purchaseOrder.status.id : undefined,
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            purchaseOrder.purchaseOrders = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.purchaseOrders, self.model.pagination);
            }
            self.model.SelectedOrderIds = [];
            self.model.selectAllRows = [];
            angular.forEach(purchaseOrder.purchaseOrders, function (item) {
              item["disableFlag"] = true;
              item['rejectReasonId'] = 1;
              item['rejectNote'] = '';
              if (item.approvalStatus == 'Rejected') {
                item.isReject = true;
              }
            });
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
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
            screenName: 'poApproval'
          }
          screenDataService.setScreenData(screenData);
      }
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailsPOFinalApproval');
    }

    var SelectPurchaseOrder = function (purchase) {
      var purchaseIds = self.model.SelectedOrderIds;
      if (purchase.isSelected) {
        purchase.disableFlag = false;
        purchaseIds.push(purchase.id);
        self.model.selectedItems.push(purchase);
      } else {
        purchase.isReject = false;
        purchase.disableFlag = true;
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

    var SelectAllOrders = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedOrderIds = [];
        self.model.purchaseOrders.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            purchase.disableFlag = false;
            self.model.SelectedOrderIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedOrderIds = [];
        self.model.purchaseOrders.forEach(function (purchase) {
          purchase.isSelected = false;
          purchase.disableFlag = true;
          if(!!purchase.isSelected){
            purchase.isReject = false;
          }
        });
      }
    }

    var isDisable = function (purchase) {
      // if (purchase.status != 'Approved') {
      if (purchase.status == 'Closed' || purchase.status == 'Approved' || purchase.approvalStatus == 'Approved' || purchase.approvalStatus == 'Partially Approved') {
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

    var OpenPopup = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.id = data.id;
      popup.modal('show');
    }

    var RejectPO = function (action, popupName, data) {
      self.model.selectedPOId = data.poId;
      self.model.itemTrueFalse = data;
      if (data.isReject) {
        self.model.action = action;
        var popup = angular.element('#' + popupName);
        popup.modal('show');
      } else {
        return;
      }
    }

    var AcceptReject = function () {
      if (ValidetReason()) {
        self.model.purchaseOrders.forEach(function (purchase) {
          if(purchase.poId == self.model.selectedPOId) {
            purchase.rejectReasonId = self.model.cancelReason;
            purchase.rejectNote = self.model.rejectNote;
          }
        });
        ModalDismiss("rejectPO");
      }
    }

    var ValidateApproval = function () {
      var isValid = true;
      if(!self.model.SelectedOrderIds || self.model.SelectedOrderIds.length == 0){
        growl.error('Please select PO to approve');

        isValid = false;
      }

      return isValid;
    }

    var SaveAcceptReject = function () {
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.APPROVE_API;

        var data = {
          "userId": $scope.userId
        };
        self.model.data1=[];
        self.model.purchaseOrders.forEach(function (purchase) {
          if (purchase.isSelected) {
            if (purchase.isReject) {
            	 self.model.data1.push({
                "poId": purchase.id,
                "statusId": setStatus('Closed'),
                "approvalStatusId": setStatus('Rejected'),
                "rejectReasonId": purchase.rejectReasonId,
                "rejectNote": purchase.rejectNote,
                "userId": $scope.userId
              });
            }
            else {
            	 self.model.data1.push({
                "poId": purchase.id,
                "statusId": setStatus('Pending'),
                "approvalStatusId": setStatus('Approved'),
                "rejectReasonId": self.model.reasons[0].id,
                "rejectNote": null,
                "userId": $scope.userId
              });
            }
          }
        });

        GenericService.serviceAction("PATCH", URI, self.model.data1).then(
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
          }
        );
      }
    }

    var ValidateStatus = function () {
      var isValid = true;
      if (!self.model.SelectedOrderIds || self.model.SelectedOrderIds.length == 0) {
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
      // self.model.purchaseOrders.forEach(function (purchase) {
      //   if (purchase.isSelected) {
      //     if (purchase.isReject) {
      //       purchase.isReject = false;
      //     }
      //   }
      // });
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }
    
    var ModalDismissOnClose = function(popupName){
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      var test = self.model.itemTrueFalse;
      test.isReject= false;
     // test.approvedQuantity = test.quantity;
    }

    var PrintPOApprovalReport = function (grn) {
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
    $stateProvider.state("POFinalApproval", {
      url: "/POFinalApproval",
      templateUrl: 'views/procurement/quotation/PO-final-approval.html',
      controller: "POFinalApproval.Controller",
      title: 'Purchase Order Final Approval',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 1022);
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
    .controller("POFinalApproval.Controller", POFinalApprovalController);
})();