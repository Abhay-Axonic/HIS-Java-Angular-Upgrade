(function () {
  "use strict";

  function itemApprovalController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS,PaginationService, GenericService, growl, cancelReasonService, StatusService, commonDetailService, screenDataService, reportService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      cancelReasonService.GetReason().then(function(reason){
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

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
          minDate : moment(new Date()).subtract(171, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(171, 'days').toDate()
        },
        selectAllRows: false,
        SelectedRequestIds: [],
        storeList: [{
          "storeName": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }],
        priorities: [{
          "priority": "All"
        }]
      };

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetItemRequests = GetItemRequests;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectItemRequest = SelectItemRequest;
      self.model.isDisable = isDisable;
      self.model.UpdateItemRequest = UpdateItemRequest;
      self.model.OpenPopup = OpenPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.PrintItemAprovalReport = PrintItemAprovalReport;
      PopulateDropdown();
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetItemRequests()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'itemApproval'){
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

    var NavigateToDetails = function (item) {
      commonDetailService.setDataId(item.id);
      $state.go('itemApprovalDetails');
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

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_APPROVAL.DROPDOWN_API;

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
        growl.error('From date should be lesser than to date');
        isValid = false;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }

    var GetItemRequests = function () {
      if (ValidateDate()) {
        var itemRequest = self.model;
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_APPROVAL.SEARCH_API;

        var data = {
          "fromDate": moment(itemRequest.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(itemRequest.toDate).format('YYYY-MM-DD'),
          "statusId": !!itemRequest.status && !!itemRequest.status.id ? itemRequest.status.id : undefined,
          "unitId":$scope.unitId
        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            itemRequest.items = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.items, self.model.pagination);
            }
            self.model.selectAllRows = false;
            self.model.SelectedRequestIds = [];
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
            screenName: 'itemApproval'
          }
          screenDataService.setScreenData(screenData);
      }
    }

    var isSelected = function (item) {
      if (!isDisable(item))
        return item.isSelected;
      else
        return true;
    }

    var SelectItemRequest = function (item) {
      var itemRequestIds = self.model.SelectedRequestIds;

      if (item.isSelected) {
        itemRequestIds.push(item.id);
      } else {
        var index = itemRequestIds.indexOf(item.id);
        if (index > -1) {
          itemRequestIds.splice(index, 1);
        }
      }

      if (self.model.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.items.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            self.model.SelectedRequestIds.push(item.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.items.forEach(function (item) {
          item.isSelected = false;
        });
      }
    }

    var isDisable = function (item) {
      if (item.status == 'Cancelled' || item.status == 'Closed' ||  item.approvalStatus == "Approved") {
        return true;
      }
      return false;
    }

    var OpenPopup = function (action, popupName, data) {
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

      if (self.model.cancelReason == undefined) {
        self.model.isReasonValid = true;
        isValid = false;
      }
      
      return isValid;
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.cancelReason = undefined;
      self.model.rejectNote = undefined;
      self.model.isReasonValid = false;
    }

    var UpdateItemRequest = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_APPROVAL.APPROVE_API;

          var data = {
            "itemRequestId": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus('Approved'),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              $rootScope.stopLoader();
              GetItemRequests();
              self.model.selectAllRows = false;
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select item to approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_APPROVAL.CANCEL_API + self.model.id;

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
              self.model.rejectNote = undefined;
              self.model.cancelReason = undefined;
              ModalDismiss('cancelItemRequest');
              GetItemRequests();

              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
        else{
         
        }
      }
    }
    /***Chetana***/
    var PrintItemAprovalReport= function(item){
    	 var data = {
    		        "appName": "HIS",
    		        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
    		        "reportDesignFilePath":  "get_Item_Approval.rpt",
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
    $stateProvider.state("itemApproval", {
      url: "/itemApproval",
      templateUrl: 'views/procurement/purchase/item-approval.html',
      controller: "itemApproval.Controller",
      title: 'Item Approval',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 164);
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
    .controller("itemApproval.Controller", itemApprovalController);
})();