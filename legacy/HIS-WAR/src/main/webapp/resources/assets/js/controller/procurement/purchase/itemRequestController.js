(function () {
  "use strict";

  function itemRequestController($scope, $rootScope, $http, $filter, $state, GenericService,PaginationService,commonDetailService, cancelReasonService, StatusService, $cookies, CONSTANTS, growl, screenDataService, reportService) {
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
          maxDate: new Date(),
          minDate : date.setDate((new Date()).getDate() - 180),
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date(),
          minDate : date.setDate((new Date()).getDate() - 180),
        },
        statuses: [{
          "status": "All"
        }],
        itemDetails: [],
        SelectedRequestIds: []
      };

      cancelReasonService.GetReason().then(function(reason){
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetItemRequests = GetItemRequests;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.UpdateItemRequest = UpdateItemRequest;
      self.model.OpenPopup = OpenPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectItemRequest = SelectItemRequest;
      self.model.isDisable = isDisable;
      self.model.PrintItemRequestReport = PrintItemRequestReport;
      PopulateDropdown();
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetItemRequests();
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'itemRequest'){
            var requestData = dataobj.searchrequest;

            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
          }
        });

        GetItemRequests();
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
      self.model.status = self.model.statuses[0];

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.DROPDOWN_API;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.statuses = self.model.statuses.concat(response.data.status);
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

    var GetItemRequests = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId" : $scope.unitId,
          "organizationId" : $scope.organizationId
        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.itemRequests = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.itemRequests, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
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
              "status": self.model.status
            },
            screenName: 'itemRequest'
          }
          screenDataService.setScreenData(screenData);
      }
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.itemRequests.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            self.model.SelectedRequestIds.push(item.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.itemRequests.forEach(function (item) {
          item.isSelected = false;
        });
      }
    }

    var SelectItemRequest = function (item) {
      var requestIds = self.model.SelectedRequestIds;

      if (item.isSelected) {
        requestIds.push(item.id);
      } else {
        var index = requestIds.indexOf(item.id);
        if (index > -1) {
          requestIds.splice(index, 1);
        }
      }

      if (self.model.itemRequests.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var isSelected = function (item) {
      if (!isDisable(item))
        return item.isSelected;
      else
        return true;
    }

    var isDisable = function (item) {
      if (item.status != 'New') {
        return true;
      }
      return false;
    }
    
    var UpdateItemRequest = function () {
      if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "cancelNote": self.model.rejectNote,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId

          }
          
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              ModalDismiss('cancelItemReqest');
              self.model.rejectNote = undefined;
              self.model.cancelReason = undefined;

              GetItemRequests();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      } else if(self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.APPROVE_API;
          var data = {
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus('Pending Approval'),
            "itemRequestId": self.model.SelectedRequestIds,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId

          }
            
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              ModalDismiss('confirmSave');
              self.model.SelectedRequestIds = [];
              GetItemRequests();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select item request to approve');
          return;
        }
      }
    }

    var OpenPopup = function (action, popupName, data) {
      self.model.action = action;
      self.model.id = data.id;
      var popup = angular.element('#' + popupName);
      popup.modal('show');
    }

    var ValidetReason = function () {
      var isValid = true;

      if (!self.model.cancelReason) {
        self.model.isReasonValid = true;
        isValid = false;
      }
      
      return isValid;
    }

    var ValidateStatus = function (){
      var isValid = true;

      if (!self.model.SelectedRequestIds || self.model.SelectedRequestIds.length == 0) {
        isValid = false;
      }

      return isValid;
    }

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewitemRequest');
    }

    var NavigateToEdit = function (itemRequest) {
      commonDetailService.setDataId(itemRequest.id);
      $state.go('addNewitemRequest');
    }

    var NavigateToDetails = function (itemRequest) {
      commonDetailService.setDataId(itemRequest.id);
      $state.go('itemRequestDetails');
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }
    /***Chetana***/
    var PrintItemRequestReport= function(item){
    	 var data = {
    		        "appName": "HIS",
    		        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
    		        "reportDesignFilePath":  "get_Item_request.rpt",
    		        "reportFormat": "PDF",
    		        "exportFileName": "IndentReport",
    		        "reportParams": [
    		          {
    		            "paramName": "requestid",
    		            "paramType": "Integer",
    		            "paramValue": item.id
    		          }
    		        ]
    		      };
    		      reportService.generate(REPORT_API_URI1, data);
    }
    /******/
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("itemRequest", {
      url: "/itemRequest",
      templateUrl: 'views/procurement/purchase/item-request.html',
      controller: "ItemRequest.Controller",
      title: 'Item Request',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 163);
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
    .controller("ItemRequest.Controller", itemRequestController);
})();