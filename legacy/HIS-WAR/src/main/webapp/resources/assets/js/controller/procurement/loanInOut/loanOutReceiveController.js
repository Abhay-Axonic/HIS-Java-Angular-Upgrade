(function () {
  "use strict";

  function loanOutReceiveController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, reportService) {
    var self = this;

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
        search: '',
        prevSearch: '',
        inwardSearch: '',
        inwardPrevSearch: '',
        vendorNames: [],
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
      self.model.SearchLoanOutReceive = SearchLoanOutReceive;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectLoanOutData = SelectLoanOutData;
      self.model.isDisable = isDisable;
      self.model.UpdateLoanOutReceive = UpdateLoanOutReceive;
      self.model.SaveApproval = SaveApproval;
      self.model.ModalDismiss = ModalDismiss;
      self.model.isEdit = isEdit;
      self.model.isCancel = isCancel;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.SearchInwardNames = SearchInwardNames;
      self.model.AddInwardName = AddInwardName;
      self.model.printLoanOutReceiveReport = printLoanOutReceiveReport;

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

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
          $rootScope.stopLoader();
          SearchLoanOutReceive();
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

    var SearchLoanOutReceive = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.SEARCH_API;
        
        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "assetTypeId": self.model.assetType.id,
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "vendorId": self.model.vendorId,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "inwardById": self.model.inwardId,
          "inWardBy": self.model.inwardBy ? self.model.inwardBy : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
        		  "unitId":$scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.loanOutList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.loanOutList, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
            self.model.vendorId = undefined;
            self.model.inwardId = undefined;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.vendorId = undefined;
            self.model.inwardId = undefined;
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
        "detail": "Vendor Not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName (vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
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

    function AddInwardName (inward) {
      self.model.inwardName = inward.inwardName;
      self.model.inwardId = inward.id;
    }

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewLoanOutReceive');
    }

    var NavigateToDetails = function (loan) {
      commonDetailService.setDataId(loan.id);
      $state.go('detailsLoanOutReceive');
    }

    var NavigateToEdit = function (loan) {
      commonDetailService.setDataId(loan.id);
      $state.go('addNewLoanOutReceive');
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.loanOutList.forEach(function (loan) {
          if (!isDisable(loan)) {
            loan.isSelected = true;
            self.model.SelectedRequestIds.push(loan.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.loanOutList.forEach(function (loan) {
          loan.isSelected = false;
        });
      }
    }

    var isDisable = function (loan) {
      if (loan.status != 'New') {
        return true;
      }
      return false;
    }

    var isEdit = function (loan) {
      if (loan.status == 'Pending') {
        return true;
      } else if (loan.status == 'New') {
        return false;
      } else if (loan.status == 'Closed' || loan.status == 'Cancelled') {
        return true;
      }
    }

    var isCancel = function (loan) {
      if (loan.status == 'Pending') {
        return false;
      } else if (loan.status == 'New') {
        return false;
      } else if (loan.status == 'Closed' || loan.status == 'Cancelled') {
        return true;
      }
    }

    var isSelected = function (loan) {
      if (!isDisable(loan))
        return loan.isSelected;
      else
        return true;
    }

    var SelectLoanOutData = function (loan) {
      var loanIds = self.model.SelectedRequestIds;

      if (loan.isSelected) {
        loanIds.push(loan.id);
      } else {
        var index = loanIds.indexOf(loan.id);
        if (index > -1) {
          loanIds.splice(index, 1);
        }
      }

      if (self.model.loanOutList.every(isSelected)) {
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

    var UpdateLoanOutReceive = function () {
      if (self.model.action == 'Approve') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.APPROVE_API;

          var data = {
            "loanOutReceiveId": self.model.SelectedRequestIds,
            "statusId": setStatus('Closed'),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              SearchLoanOutReceive();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Select Loan Out Receive to Close');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.CANCEL_API + self.model.id;

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

              ModalDismiss('cancelLoanInOut');
              SearchLoanOutReceive();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    
    /***Chetana**/
    var printLoanOutReceiveReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Loan_Out_rec.rpt",
            "reportFormat": "PDF",
            "exportFileName": "LoanOutReceive",
            "reportParams": [
                {
                    "paramName": "Id",
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
    $stateProvider.state("loanOutReceive", {
      url: "/loanOutReceive",
      templateUrl: 'views/procurement/consignment/loan-out-receive.html',
      controller: "LoanOutReceive.Controller",
      title: 'Loan Out Receive',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 202);
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
  .controller("LoanOutReceive.Controller", loanOutReceiveController);
})();