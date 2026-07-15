(function () {
  "use strict";

  function WorkOrderApprovalWorklistController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, PaginationService,GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      cancelReasonService.GetReason().then(function(reason){
        self.model.cancelReasons = reason
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });
      var date = new Date();
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
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
//          minDate : date.setDate((new Date()).getDate() - 90),
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        selectAllRows: false,
        SelectedRequestIds: [],
        storeList: [{
          "store": "All"
        }],
        contractTypes: [{
          "code": "All"
        }]
      };

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetWorkOrderApprovalList = GetWorkOrderApprovalList;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectPurchaseRequest = SelectPurchaseRequest;
      self.model.isDisable = isDisable;
      self.model.UpdatePurchaseRequest = UpdatePurchaseRequest;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.ModalDismiss = ModalDismiss;

      PopulateDropdown();
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

    var PopulateDropdown = function () {
      self.model.store = self.model.storeList[0];
      self.model.contractType = self.model.contractTypes[0];

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_APPROVAL.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
          $rootScope.stopLoader();
          GetWorkOrderApprovalList();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From Date should lesser than To Date');
        isValid = false;
      } else if (self.model.fromDate == self.model.toDate) {
        isValid = true;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Enter Valid Date');
        isValid = false;
      }

      return isValid;
    }

    var GetWorkOrderApprovalList = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_APPROVAL.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "contractTypeId": self.model.contractType.id,
          "unitId":$scope.unitId
        }
        
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.workOrderApprovalList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.workOrderApprovalList, self.model.pagination);
            }
         
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var NavigateToDetails = function (order) {
      commonDetailService.setDataId(order.id);
      $state.go('detailsWorkorderApprovalWorklist');
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.localRequests.forEach(function (order) {
          if (!isDisable(order)) {
            order.isSelected = true;
            self.model.SelectedRequestIds.push(order.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.localRequests.forEach(function (order) {
          order.isSelected = false;
        });
      }
    }

    var isDisable = function (order) {
      if (order.status == 'Cancelled' || order.status == 'Closed') {
      // if (purchase.status != 'New' || purchase.status != 'Pending') {
        return true;
      }
      return false;
    }

    var isSelected = function (order) {
      if (!isDisable(order))
        return order.isSelected;
      else
        return true;
    }

    var SelectPurchaseRequest = function (order) {
      var purchaseIds = self.model.SelectedRequestIds;

      if (order.isSelected) {
        purchaseIds.push(order.id);
      } else {
        var index = purchaseIds.indexOf(order.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.localRequests.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var ValidateStatus = function (){
      var isValid = true;
      if (!self.model.SelectedRequestIds || self.model.SelectedRequestIds.length == 0) {
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

    var ValidateAndUpdate = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.id = data.id;
      popup.modal('show');
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }

    var UpdatePurchaseRequest = function () {
      if (ValidetReason()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_APPROVAL.CANCEL_API + self.model.id;

        var data = {
          "statusId": setStatus('Closed'),
          "approvalStatusId": setStatus('Cancelled'),
          "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
          "cancelNote": self.model.rejectNote,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        }
        
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.SelectedRequestIds = [];
            ModalDismiss('cancelPurchaseReqest');
            GetWorkOrderApprovalList();

            $rootScope.stopLoader();
            growl.success(response.data.message);
          }, function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("workorderApprovalWorklist", {
      url: "/workorderApprovalWorklist",
      templateUrl: 'views/procurement/workOrder/workorder-approval-worklist.html',
      controller: "WorkorderApprovalWorklist.Controller",
      title: 'Work Order Approval Worklist',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 189);
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
    .controller("WorkorderApprovalWorklist.Controller", WorkOrderApprovalWorklistController);
})();