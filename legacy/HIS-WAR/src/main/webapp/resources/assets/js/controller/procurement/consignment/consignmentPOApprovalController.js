(function () {
    "use strict";
  
    function consignmentPOApprovalController($scope, $rootScope, $http, $filter, $state, screenDataService,PaginationService, $cookies, CONSTANTS, GenericService, commonDetailService, rejectReasonService, StatusService, growl) {
      var self = this;
      var rejectData;
  
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
          fromDate: new Date(),
          toDate: new Date(),
          fromDateOptions: {
            formatYear: 'yyyy',
            showWeeks: false,
            maxDate: new Date(),
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
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.GetPurchaseOrderList = GetPurchaseOrderList;
        self.model.poApprovalDetails = poApprovalDetails;
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
  
        PopulateDropdown();
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
        self.model.store = self.model.storeList[0];
        self.model.status = self.model.statuses[0];
  
        var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.storeList = self.model.storeList.concat(response.data.requestingStores);
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
          var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_APPROVAL.SEARCH_API;
  
          var data = {
            "fromDate": moment(purchaseOrder.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(purchaseOrder.toDate).format('YYYY-MM-DD'),
            "storeId": !!purchaseOrder.store && !!purchaseOrder.store.id ? purchaseOrder.store.id : undefined,
            "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
            "vendorId": self.model.vendorId ? self.model.vendorId : undefined,
            "poNumber": self.model.purchaseOrderNo ? self.model.purchaseOrderNo : undefined,
            "statusId": !!purchaseOrder.status && !!purchaseOrder.status.id ? purchaseOrder.status.id : undefined,
            "unitId":$scope.unitId
          }
  
          $rootScope.startLoader();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.pagination.data=[];
              self.model.pagination.totalPages=0;
              purchaseOrder.purchaseOrders = response.data;
              if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchData(self.model.purchaseOrders, self.model.pagination);
              }
              self.model.SelectedOrderIds = [];
              self.model.selectAllRows = [];
              angular.forEach(purchaseOrder.purchaseOrders, function (item) {
                item["disableFlag"] = true;
                item['rejectReasonId'] = 1;
                item['rejectNote'] = '';
                if (item.approvalStatus == 'Reject') {
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
                "vendorId": self.model.vendorId,
                "vendorName": self.model.vendorName,
                "purchaseOrderNo": self.model.purchaseOrderNo
              },
              screenName: 'poApproval'
            }
            screenDataService.setScreenData(screenData);
        }
      }
  
      var poApprovalDetails = function (purchase) {
        commonDetailService.setDataId(purchase.id);
        $state.go('detailsConsignmentPOApproval');
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
          self.model.purchaseOrders.forEach(function (purchase) {
            if (purchase.isSelected) {
              if (purchase.isReject) {
                data.push({
                  "poId": purchase.id,
                  "statusId": setStatus('Closed'),
                  "approvalStatusId": setStatus('Rejected'),
                  "rejectReasonId": purchase.rejectReasonId,
                  "rejectNote": purchase.rejectNote
                });
              }
              else {
                data.push({
                  "poId": purchase.id,
                  "statusId": setStatus('Pending'),
                  "approvalStatusId": setStatus('Approved'),
                  "rejectReasonId": self.model.reasons[0].id,
                  "rejectNote": null
                });
              }
            }
          });
  
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
        self.model.rejectNote = undefined;
        self.model.cancelReason = undefined;
      }
  
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("consignmentPOApproval", {
        url: "/consignmentPOApproval",
        templateUrl: 'views/procurement/consignment/consignment-po-approval.html',
        controller: "ConsignmentPOApproval.Controller",
        controllerAs: "vm",
        title: 'Consignment PO Approval'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("ConsignmentPOApproval.Controller", consignmentPOApprovalController);
  })();