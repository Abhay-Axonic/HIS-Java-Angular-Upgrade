(function () {
  "use strict";

  function localPurchaseRequestController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, screenDataService, reportService) {
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

      cancelReasonService.GetReason().then(function(reason) {
        self.model.cancelReasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetLocalPurchaseRequests = GetLocalPurchaseRequests;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectLocalPurchaseRequest = SelectLocalPurchaseRequest;
      self.model.isDisable = isDisable;
      self.model.UpdateLocalPR = UpdateLocalPR;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.ModalDismiss = ModalDismiss;
      self.model.PrintLocalPurchaseReqReport = PrintLocalPurchaseReqReport;
      PopulateDropdown();
    }

    var PopulateDropdown = function () {
      var localPurchase = self.model;
      localPurchase.store = localPurchase.storeList[0];
      localPurchase.fromstore = localPurchase.fromstoreList[0];
      localPurchase.status = localPurchase.statuses[0];
      localPurchase.priority = localPurchase.priorities[0];

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          localPurchase.storeList = localPurchase.storeList.concat(response.data.deliveryStores);
          localPurchase.fromstoreList = localPurchase.fromstoreList.concat(response.data.requestingStores);
          localPurchase.statuses = localPurchase.statuses.concat(response.data.status);
          localPurchase.priorities = localPurchase.priorities.concat(response.data.priority);
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

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetLocalPurchaseRequests()
      } else {
        GetLocalPurchaseRequests();
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'localPurchaseRequest'){
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
      }
    }

    var GetLocalPurchaseRequests = function () {
      if (ValidateDate()) {
        var localPurchase = self.model;
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.SEARCH_API;

        var data = {
          "fromDate": moment(localPurchase.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(localPurchase.toDate).format('YYYY-MM-DD'),
          "priorityId": localPurchase.priority ? localPurchase.priority.id : undefined,
          "requestingStoreId": !!localPurchase.fromstore && !!localPurchase.fromstore.id ? localPurchase.fromstore.id : undefined,
          "storeId": !!localPurchase.store && !!localPurchase.store.id ? localPurchase.store.id : undefined,
          "statusId": !!localPurchase.status && !!localPurchase.status.id ? localPurchase.status.id : undefined,
          "unitId":$scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            localPurchase.localPurchaseRequests = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( localPurchase.localPurchaseRequests, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          }
        );
        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "status": self.model.status,
            "priority": self.model.priority,
            "store": self.model.store
          },
          screenName: 'localPurchaseRequest'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        console.log("All Items : ",self.model.localPurchaseRequests);
        console.log("Pagination Items : ",self.model.pagination.data);
        self.model.pagination.data.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            self.model.SelectedRequestIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.localPurchaseRequests.forEach(function (purchase) {
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

    var SelectLocalPurchaseRequest = function (purchase) {
      var purchaseIds = self.model.SelectedRequestIds;

      if (purchase.isSelected) {
        purchaseIds.push(purchase.id);
      } else {
        var index = purchaseIds.indexOf(purchase.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.localPurchaseRequests.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var ValidateAndUpdate = function (action, popupName, data) {
      self.model.action = action;
      self.model.id = data.id;
      var popup = angular.element('#' + popupName);
      self.model.cancelNote = undefined;
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
      self.model.cancelNote = undefined;
      self.model.cancelReason = undefined;
    }

    var UpdateLocalPR = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.APPROVE_API;

          var data = {
            "prId": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus("Pending Approval"),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              GetLocalPurchaseRequests();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select local purchase request to approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "cancelNote": self.model.cancelNote,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          }
          
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];

              ModalDismiss('cancelPurchaseReqest');
              GetLocalPurchaseRequests();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    var setStatus = function (value){
      var status = self.model.statusList.find(function(obj){
        return obj.status == value;
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

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewlocalPurchhaseRequest');
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailslocalPurchhaseRequest');
    }

    var NavigateToEdit = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('addNewlocalPurchhaseRequest');
    }
    
    /***Chetana***/
    var PrintLocalPurchaseReqReport= function(purchase){
    	 var data = {
    		        "appName": "HIS",
    		        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
    		        "reportDesignFilePath":  "get_Local_Purchase_Request.rpt",
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
    $stateProvider.state("localPurchhaseRequest", {
      url: "/localPurchhaseRequest",
      templateUrl: 'views/procurement/purchase/local-purchase-request.html',
      controller: "LocalPurchaseRequest.Controller",
      title: 'Local Purchase Request',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 170);
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
    .controller("LocalPurchaseRequest.Controller", localPurchaseRequestController);
})();