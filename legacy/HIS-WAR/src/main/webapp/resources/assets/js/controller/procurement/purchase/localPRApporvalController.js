(function () {
  "use strict";

  function localPRApprovalController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, rejectReasonService, StatusService, growl, screenDataService, reportService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      cancelReasonService.GetReason().then(function(reason){
        self.model.cancelReasons = reason
      });

      rejectReasonService.GetReason().then(function(reason) {
        self.model.rejectReasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model = {
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        selectAllRows: false,
        SelectedRequestIds: [],
        storeList: [{
          "store": "All"
        }],
        fromStoreList: [{
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

      //Methods
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
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.ModalDismiss = ModalDismiss;
      self.model.PrintLocalPRApprovalReport = PrintLocalPRApprovalReport;

      PopulateDropdown();
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetPurchaseRequests()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'localPRApproval'){
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
            self.model.fromstore = self.model.fromStoreList.find(function (store) {
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
      purchaseRequests.fromstore = purchaseRequests.fromStoreList[0];
      purchaseRequests.status = purchaseRequests.statuses[0];
      purchaseRequests.priority = purchaseRequests.priorities[0];

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          purchaseRequests.storeList = purchaseRequests.storeList.concat(response.data.deliveryStores);
          purchaseRequests.fromStoreList = purchaseRequests.fromStoreList.concat(response.data.requestingStores);
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
        growl.error('From Date should lesser than To Date');
        isValid = false;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Enter Valid Date');
        isValid = false;
      }

      return isValid;
    }

    var GetPurchaseRequests = function () {
      if (ValidateDate()) {
        var localRequests = self.model;
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_APPROVAL.SEARCH_API;

        var data = {
          "fromDate": moment(localRequests.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(localRequests.toDate).format('YYYY-MM-DD'),
          "priorityId": localRequests.priority ? localRequests.priority.id : undefined,
          "requestingStoreId": !!localRequests.fromstore && !!localRequests.fromstore.id ? localRequests.fromstore.id : undefined,
          "storeId": !!localRequests.store && !!localRequests.store.id ? localRequests.store.id : undefined,
          "statusId": !!localRequests.status && !!localRequests.status.id ? localRequests.status.id : undefined,
          "unitId":$scope.unitId
        }
        
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            localRequests.localRequests = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( localRequests.localRequests, self.model.pagination);
            }
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
            "priority": self.model.priority,
            "store": self.model.store,
            "fromstore": self.model.fromstore
          },
          screenName: 'localPRApproval'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var NavigateToAddNew = function () {
      $state.go('addNewpurchaseRequest');
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailslocalPurchhaseRequestApproval');
    }

    var NavigateToEdit = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailslocalPurchhaseRequestApproval');
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.localRequests.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            self.model.SelectedRequestIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.localRequests.forEach(function (purchase) {
          purchase.isSelected = false;
        });
      }
    }

    var isDisable = function (purchase) {
      if (purchase.status == 'Cancelled' || purchase.status == 'Closed' || purchase.approvalStatus == 'Approved') {
      // if (purchase.status != 'New' || purchase.status != 'Pending') {
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

      if (self.model.localRequests.every(isSelected)) {
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

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }

    var UpdatePurchaseRequest = function () {
      if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_APPROVAL.CANCEL_API + self.model.id;

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
    /***Chetana***/
    var PrintLocalPRApprovalReport= function(purchase){
    	var data = {
		        "appName": "HIS",
		        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		        "reportDesignFilePath":  "get_Local_Purchase_Approval.rpt",
		        "reportFormat": "PDF",
		        "exportFileName": "IndentReport",
		        "reportParams": [
		          {
		            "paramName": "Purchase_request_id",
		            "paramType": "Integer",
		            "paramValue": purchase.id
		          }
		        ]
		      };
		      reportService.generate(REPORT_API_URI1, data);
    }
    /******/
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("localPurchhaseRequestApproval", {
      url: "/localPurchhaseRequestApproval",
      templateUrl: 'views/procurement/purchase/local-purchase-approval.html',
      controller: "LocalPRApproval.Controller",
      title: 'Local Purchase Request Approval',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 171);
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
    .controller("LocalPRApproval.Controller", localPRApprovalController);
})();