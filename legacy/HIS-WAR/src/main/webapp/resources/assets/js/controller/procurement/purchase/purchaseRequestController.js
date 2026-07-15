(function () {
  "use strict";

  function purchaseRequestController($scope, $rootScope, $http, $filter, $state, GenericService,PaginationService, PurchaseRequestService, cancelReasonService, rejectReasonService, StatusService, growl, $cookies, CONSTANTS, screenDataService, reportService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate(),
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
        fromstoreList: [{
          "store": "All"
        }],
        statuses: [{
          "status": "All"
        }],
        priorities: [{
          "priority": "All"
        }],
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
        }
      };

      rejectReasonService.GetReason().then(function(reason) {
        self.model.rejectReasons = reason;
      });

      cancelReasonService.GetReason().then(function(reason) {
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetPurchaseRequests = GetPurchaseRequests;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectPurchaseRequest = SelectPurchaseRequest;
      self.model.isDisable = isDisable;
      self.model.UpdatePurchaseRequest = UpdatePurchaseRequest;
      self.model.OpenPopup = OpenPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.printPurchaseRequestReport = printPurchaseRequestReport;

      PopulateDropdown();
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetPurchaseRequests()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'purchaseRequest'){
            var requestData = dataobj.searchrequest;
            self.model.priority = self.model.priorities.find(function (priority) {
              if (requestData.priority.id == priority.id)
                return priority;
            });
            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
                return requestData.store.id == store.id;
            });
            self.model.fromstore = self.model.fromstoreList.find(function (store) {
              return requestData.fromstore.id == store.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
          }
        });

        GetPurchaseRequests();
      }
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
      var purchaseRequests = self.model;
      purchaseRequests.store = purchaseRequests.storeList[0];
      purchaseRequests.fromstore = purchaseRequests.fromstoreList[0];
      purchaseRequests.status = purchaseRequests.statuses[0];
      purchaseRequests.priority = purchaseRequests.priorities[0];

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.DROPDOWN_API + '/' + $scope.unitId+ '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          purchaseRequests.storeList = purchaseRequests.storeList.concat(response.data.requestingStores);
          purchaseRequests.fromstoreList = purchaseRequests.fromstoreList.concat(response.data.deliveryStores);
          purchaseRequests.statuses = purchaseRequests.statuses.concat(response.data.status);
          purchaseRequests.priorities = purchaseRequests.priorities.concat(response.data.priority);
          $rootScope.stopLoader();
          initialCall();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be lesser than to date');
        isValid = false;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }

    var GetPurchaseRequests = function () {
      if (ValidateDate()) {
        var purchaseRequests = self.model;
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.SEARCH_API;

        var data = {
          "fromDate": moment(purchaseRequests.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(purchaseRequests.toDate).format('YYYY-MM-DD'),
          "priorityId": purchaseRequests.priority ? purchaseRequests.priority.id : undefined,
          "storeId": !!purchaseRequests.store && !!purchaseRequests.store.id ? purchaseRequests.store.id : undefined,
          "requestingStoreId": !!purchaseRequests.fromstore && !!purchaseRequests.fromstore.id ? purchaseRequests.fromstore.id : undefined,
          "statusId": !!purchaseRequests.status && !!purchaseRequests.status.id ? purchaseRequests.status.id : undefined,
          "unitId":$scope.unitId		  
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            purchaseRequests.purchaseRequests = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( purchaseRequests.purchaseRequests, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          }
        );
        // var screenData = {
        //   searchrequest: {
        //     "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        //     "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        //     "status": self.model.status,
        //     "priority": self.model.priority,
        //     "store": self.model.store,
        //     "fromstore": self.model.fromstore
        //   },
        //   screenName: 'purchaseRequest'
        // }
        // screenDataService.setScreenData(screenData);
      }
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.purchaseRequests.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            self.model.SelectedRequestIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.purchaseRequests.forEach(function (purchase) {
          purchase.isSelected = false;
        });
      }
    }

    var isDisable = function (purchase) {
      if (purchase.status != 'New') {
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

    var SelectPurchaseRequest = function (purchase) {
      var purchaseIds = self.model.SelectedRequestIds;

      if (purchase.isSelected) {
        purchaseIds.push(purchase.id);
      } else {
        var index = purchaseIds.indexOf(purchase.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.purchaseRequests.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var UpdatePurchaseRequest = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.APPROVE_API;

          var data = {
            "prId": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus('Pending Approval'),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              GetPurchaseRequests();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select purchase request to approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "reasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "note": self.model.rejectNote,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          }
          
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              ModalDismiss('cancelPurchaseReqest');
              self.model.rejectNote = undefined;
              self.model.cancelReason = undefined;

              GetPurchaseRequests();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
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

    var OpenPopup = function (action, popupName, data) {
      self.model.action = action;
      self.model.id = data.id;
      var popup = angular.element('#' + popupName);

      self.model.isReasonValid = false;
      self.model.rejectNote = undefined;
      popup.modal('show');
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
    }

    var NavigateToAddNew = function () {
      $state.go('addNewpurchaseRequest');
    }

    var NavigateToDetails = function (purchase) {
      PurchaseRequestService.SetPurchaseId(purchase.id);
      $state.go('detailsPurchaseRequest');
    }

    var NavigateToEdit = function (purchase) {
      PurchaseRequestService.SetPurchaseId(purchase.id);
      $state.go('addNewpurchaseRequest');
    }

    /***Chetana**/
    var printPurchaseRequestReport = function (purchaseRequest) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Purchase_request.rpt",
            "reportFormat": "PDF",
            "exportFileName": "PurchaseRequest",
            "reportParams": [
                {
                    "paramName": "id",
                    "paramType": "Integer",
                    "paramValue": purchaseRequest.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("purchaseRequest", {
      url: "/purchaseRequest",
      templateUrl: 'views/procurement/purchase/purchase-request.html',
      controller: "PurchaseRequest.Controller",
      title: 'Purchase Request',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 168);
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
    .controller("PurchaseRequest.Controller", purchaseRequestController);
})();