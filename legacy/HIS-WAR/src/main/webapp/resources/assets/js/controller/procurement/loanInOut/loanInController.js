(function () {
  "use strict";

  function loanInController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, reportService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId; 
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
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
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
        statuses: [{
          "status": "All"
        }],
        assetTypes: [{
          "type": "Select Asset Type"
        }],
        inwardSearch: '',
        inwardPrevSearch: '',
        inwardNames: []
      };

      cancelReasonService.GetReason().then(function(reason) {
        self.model.cancelReasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.SearchLoanIn = SearchLoanIn;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectLocalLoanInData = SelectLocalLoanInData;
      self.model.isDisable = isDisable;
      self.model.UpdateLoanIn = UpdateLoanIn;
      self.model.SaveApproval = SaveApproval;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      self.model.SearchInwardNames = SearchInwardNames;
      self.model.AddInwardName = AddInwardName;
      self.model.printLoanInReport = printLoanInReport;

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
      self.model.status = self.model.statuses[0];
      self.model.assetType = self.model.assetTypes[0];

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
          $rootScope.stopLoader();
          SearchLoanIn();
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
      } else if (self.model.fromDate == self.model.toDate) {
        isValid = true;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }

    var SearchLoanIn = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "assetTypeId": self.model.assetType.id,
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "vendorId": self.model.vendorId,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "inWardById": self.model.inwardById,
          "inWardBy": self.model.inwardBy ? self.model.inwardBy : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId":$scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.loanInList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.loanInList, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
            self.model.inwardById = undefined;
            self.model.vendorId = undefined;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.inwardById = undefined;
            self.model.vendorId = undefined;
            growl.error(err.data.message);
          }
        );
      }
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

    function AddVendorName (vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewLoanIn');
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailsLoanIn');
    }

    var NavigateToEdit = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('addNewLoanIn');
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.loanInList.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            self.model.SelectedRequestIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.loanInList.forEach(function (purchase) {
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

    var SelectLocalLoanInData = function (purchase) {
      var purchaseIds = self.model.SelectedRequestIds;

      if (purchase.isSelected) {
        purchaseIds.push(purchase.id);
      } else {
        var index = purchaseIds.indexOf(purchase.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.loanInList.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var SaveApproval = function (action, popupName, data) {
      self.model.action = action;
      self.model.id = data.id;
      var popup = angular.element('#' + popupName);
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

    var UpdateLoanIn = function () {
      if (self.model.action == 'Approve') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.APPROVE_API;

          var data = {
            "loanInId": self.model.SelectedRequestIds,
            "statusId": setStatus('Closed'),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              SearchLoanIn();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select loan in to approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Cancelled'),
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
              SearchLoanIn();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    function SearchInwardNames(search) {
      if (search.length < 1) return;

      if ((!self.model.inwardPrevSearch || self.model.inwardPrevSearch != search) && (search.length == 1)) {
        self.model.inwardPrevSearch = search;
        var URI = CONSTANTS.GLOBAL.INWARD_BY + $scope.organizationId + '/' + $scope.unitId + '/' + search;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
          if (!!response.data && response.data.length > 0) {
            self.model.inwardNames = response.data;
            $rootScope.stopLoader();
            return $filter('filter')(self.model.inwardNames, {
              $: search
            });
          } else {
            $rootScope.stopLoader();
            return inwordNotFound(search);
          }
        }, function (err) {
          $rootScope.stopLoader();
          return inwordNotFound(search);
        });
      } else {
        if (!!self.model.inwardNames && self.model.inwardNames.length > 0 && self.model.inwardNames[0].itemFound != undefined && !self.model.inwardNames[0].itemFound) {
          return inwordNotFound(search);
        } else {
          return $filter('filter')(self.model.inwardNames, {
            $: search
          });
        }
      }
    }

    function inwordNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Inward Not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddInwardName (outward) {
      self.model.inwardName = outward.inwardName;
      self.model.inwardById = outward.id;
    }
    
    
    /***Chetana**/
    var printLoanInReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Loan_in.rpt",
            "reportFormat": "PDF",
            "exportFileName": "LoanIn",
            "reportParams": [
                {
                    "paramName": "id",
                    "paramType": "Integer",
                    "paramValue": item.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("loanIn", {
      url: "/loanIn",
      templateUrl: 'views/procurement/consignment/loan-in.html',
      controller: "LoanIn.Controller",
      title: 'Loan In',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 199);
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
    .controller("LoanIn.Controller", loanInController);
})();