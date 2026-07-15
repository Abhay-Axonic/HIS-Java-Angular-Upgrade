(function () {
  "use strict";

  function loanOutController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, screenDataService, growl,reportService) {
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
      self.model.SearchLoanOut = SearchLoanOut;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectLoanOutData = SelectLoanOutData;
      self.model.isDisable = isDisable;
      self.model.UpdateLoanOut = UpdateLoanOut;
      self.model.SaveApproval = SaveApproval;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      self.model.SearchInwardNames = SearchInwardNames;
      self.model.AddInwardName = AddInwardName;
      self.model.LoanOutUpdate = LoanOutUpdate;
      self.model.printLoanOutReport = printLoanOutReport;

      PopulateDropdown();
    }

    var setStatus = function (data){
      var status = self.model.statusList.find(function(obj){
        return obj.status == data;
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

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
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

    var SearchLoanOut = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "assetTypeId": self.model.assetType.id,
          "vendorId": self.model.vendorId,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "searchKeyword": self.model.outwardBy ? self.model.outwardBy : undefined,
          "outWardById": self.model.outwardId,
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
            self.model.outWardById = undefined;
            self.model.vendorId = undefined;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.outWardById = undefined;
            self.model.vendorId = undefined;
            growl.error(err.data.message);
          }
        );

        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "status": self.model.status,
            "store": self.model.store,
            "assetType": self.model.assetType,
            "vendorId": self.model.vendorId,
            "vendorName": self.model.vendorName,
            "outwardBy": self.model.outwardBy,
            "outWardById": self.model.outWardById,
          },
          screenName: 'loanOut'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          SearchLoanOut();
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'loanOut'){
            var requestData = dataobj.searchrequest;
            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });
            self.model.assetType = self.model.assetTypes.find(function (type) {
                return requestData.assetType.id == type.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
                return requestData.store.id == store.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
            self.model.vendorId = requestData.vendorId;
            self.model.vendorName = requestData.vendorName;
            self.model.outwardBy = requestData.outwardBy;
            self.model.outWardById = requestData.outWardById;
          }
        });

        SearchLoanOut();
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
      $state.go('addNewLoanOut');
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('detailsLoanOut');
    }

    var NavigateToEdit = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('addNewLoanOut');
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.loanOutList.forEach(function (purchase) {
          if (!isDisable(purchase)) {
            purchase.isSelected = true;
            self.model.SelectedRequestIds.push(purchase.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.loanOutList.forEach(function (purchase) {
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

    var SelectLoanOutData = function (purchase) {
      var purchaseIds = self.model.SelectedRequestIds;

      if (purchase.isSelected) {
        purchaseIds.push(purchase.id);
      } else {
        var index = purchaseIds.indexOf(purchase.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
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

    var UpdateLoanOut = function () {
      if (self.model.action == 'Approve') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.APPROVE_API;

          var data = {
            "loanOutId": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              SearchLoanOut();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select loan out to approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Closed'),
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

              ModalDismiss('cancelLoanOut');
              SearchLoanOut();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    var LoanOutUpdate = function () {
      if (self.model.action == 'Approve') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.APPROVE_API;

          var data = {
            "loanOutId": self.model.SelectedRequestIds,
            "statusId": setStatus('Closed'),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              SearchLoanOut();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select loan out to approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.CANCEL_API + self.model.id;

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

              ModalDismiss('cancelLoanOut');
              SearchLoanOut();
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
     //   var URI = CONSTANTS.GLOBAL.INWARD_BY + search;
        var URI = BASE_URL + ROOT_URL +"/api/global/inWardBy/searchUser/"+$scope.organizationId + '/' + $scope.unitId +'/'+ search;
        
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
        "name": "Inward not found with name " + search
      };
      self.model.inwardNames = [item];
      return  self.model.inwardNames;
    }

    function AddInwardName (outward) {
      self.model.outwardBy = outward.name;
      self.model.outwardId = outward.id;
    }

    /***Chetana**/
    var printLoanOutReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Loan_out.rpt",
            "reportFormat": "PDF",
            "exportFileName": "LoanOut",
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
    $stateProvider.state("loanOut", {
      url: "/loanOut",
      templateUrl: 'views/procurement/consignment/loan-out.html',
      controller: "LoanOut.Controller",
      title: 'Loan Out',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 200);
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
  .controller("LoanOut.Controller", loanOutController);
})();