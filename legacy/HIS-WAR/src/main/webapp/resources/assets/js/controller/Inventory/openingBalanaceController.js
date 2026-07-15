(function () {
  "use strict";

  function openingBalanceController($scope, $rootScope, $http, CONSTANTS, GenericService,PaginationService, screenDataService, $state, cancelReasonService, openingBalanceService, productService, $filter, growl, reportService,  $cookies) {
    var self = this;
    var selectItemList = [];
    var storeIndentDetailsIndex;
    var masterId;
    var opbId;
    //Default Constructor
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
        selectItemList: [],
        SelectedRequestIds: [],
        detailsOpeningBalance: [],
        opStoreIndentData: undefined,
        selectAllRows: false,
        stores: [{
          "storeName": "All Stores"
        }],
        reasons: [{
          "reason": "Select  Reason"
        }],
        hash: {},
        requestData: [],
        approvedItemList: [],
        approveIndent: [],
        storeData: 1,
        selectedStoreId: 1,
        cancelId: 0
      };

      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = self.model.reasons.concat(reason);
      });

      self.model.editItem = null;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.OpeningBalanceDetails = OpeningBalanceDetails;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectAllApprove = SelectAllApprove;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      // self.model.EditOPBDetails = EditOPBDetails;
      self.model.EditOpeningBalanceDetails = EditOpeningBalanceDetails;
      self.model.OpeningBalanceDetailsAddNew = OpeningBalanceDetailsAddNew;

      self.model.SearchByStore = SearchByStore;
      self.model.isDisable = isDisable;
      self.model.SelectOpeningBalance = SelectOpeningBalance;
      self.model.ApproveOpb = ApproveOpb;
      self.model.printOpeningBalanceReport = printOpeningBalanceReport;

      PopulateDropdown();
      SearchByStore();
    }


    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }
    var isDisable = function (Item) {
      if (Item.status != 'New') {
        return true;
      }
      return false;
    }
    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        SearchByStore()
      }
      else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'Opening Balance') {

            var requestData = dataobj.searchrequest;
            self.model.store = self.model.stores.find(function (store) {
              return requestData.store.id == store.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);

          }
        });
        SearchByStore();
      }
    }
    var PopulateDropdown = function () {

      var obp = self.model;

      // var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
      var URI = CONSTANTS.GLOBAL.USER_STORE_BY_ID_API+$scope.userId+"/"+ $cookies.getObject('cookieObject').unitId;
      obp.store = obp.stores[0];

      obp.stores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          obp.stores = obp.stores.concat(response.data.stores);
          initialCall();
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    var ValidateDate = function () {
      var isValid = true;

      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Enter Valid Date');
        isValid = false;
      }

      return isValid;
    }

    var SearchByStore = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.SEARCH_API;
        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": self.model.store.storeId,
          "unitId":$scope.unitId,
          "userId":$scope.userId,

        };
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            self.model.opStoreIndentData = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.opStoreIndentData, self.model.pagination);
            }
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            growl.error('Error');
            $rootScope.stopLoader();
          });
        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "store": self.model.store
          },
          screenName: 'Opening Balance',
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var ValidateAndUpdate = function (action, popupName, opbid) {
      self.model.cancelReason = undefined;
      opbId = opbid;
      // opbId = opb.id;
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      popup.modal('show');
    }
    $scope.saveBtnDisabled=false;
    var ApproveOpb = function () {
    	$scope.saveBtnDisabled=true;
      if (self.model.action == 'Update') {
        var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.APPROVE_API;

        var data = {
          "openingBalanceId": self.model.SelectedRequestIds,
          "statusId": 8,
          "userId":$scope.userId,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.SelectedRequestIds = [];
            $rootScope.stopLoader();
            // GetStoreIndents();
            growl.success(response.data.message);
            self.model.selectAllRows = false; 
            self.model.isItemChecked = false;
            SearchByStore();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.msg);
          });
      } else if (self.model.action == 'Cancel') {
    	  $scope.saveBtnDisabled=false;
        if (ValidateCancelation()) {
          var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.CANCEL_API + opbId;

          var data = {
            "cancelReasonId": self.model.cancelReason,
            "cancelNote": self.model.cancelNote,
            "statusId": 9,
            "userId":$scope.userId,
          };

          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              $rootScope.stopLoader();
              // GetStoreIndents();
              growl.success(response.data.message);
              ModalDismiss('confirmCancel');
              SearchByStore();
              self.model.cancelReason = '';
              self.model.cancelNote = '';
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.msg);
            });
        }
      }
    }
    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var ValidateCancelation = function () {
      var isValid = true;
      if (!self.model.cancelReason) {
        isValid = false;
        growl.error('Please Select Reason to Cancel');
      }
      return isValid;
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.opStoreIndentData.forEach(function (opb) {
          if (!isDisable(opb)) {
            opb.isSelected = true;
            self.model.SelectedRequestIds.push(opb.id);
            self.model.isItemChecked = true;
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.opStoreIndentData.forEach(function (opb) {
          opb.isSelected = false;
          self.model.isItemChecked = false;
        });
      }
    }

    var isSelected = function (opb) {
      if (!isDisable(opb))
        return opb.isSelected;
      else
        return true;
    }

    var SelectOpeningBalance = function (opb) {

      var opbIds = self.model.SelectedRequestIds;

      if (opb.isSelected) {
        opbIds.push(opb.id);
      } else {
        var index = opbIds.indexOf(opb.id);
        if (index > -1) {
          opbIds.splice(index, 1);
        }
      }

      if (self.model.opStoreIndentData.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
      self.model.isItemChecked = false;
      self.model.opStoreIndentData.forEach(function (opb) {
        if (opb.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });

    }
    var SelectAllApprove = function () {
      self.model.selectItemList = [];
      self.model.opStoreIndentData.forEach(function (x) {
        if (self.model.selectAllApprove) {
          x.isApprove = true
          self.model.selectItemList.push(x.id);
        } else {
          x.isApprove = false;
        }
      });
    }

    var EditOpeningBalanceDetails = function (Item) {
      openingBalanceService.setOpeningBalanceId(Item.id);
      $state.go('addNewOpeningBalance');
    }

    var OpeningBalanceDetails = function (Item) {
      openingBalanceService.setOpeningBalanceId(Item.id);
      $state.go('DetailsOpeningBalance');;
    }

    var OpeningBalanceDetailsAddNew = function (Item) {
      openingBalanceService.setOpeningBalanceId(null);
      $state.go('addNewOpeningBalance');;
    }
    
    /***Chetana**/
    var printOpeningBalanceReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_OpeningBal.rpt",
            "reportFormat": "PDF",
            "exportFileName": "OpeningBalance",
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
    $stateProvider.state("openingBalance", {
      url: "/openingBalance",
      templateUrl: 'views/PharmacyInventory/Store/opening-balance.html',
      controller: "OpeningBalance.Controller",
      title: 'Opening Balance',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 154);
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
    .controller("OpeningBalance.Controller", openingBalanceController)
})();