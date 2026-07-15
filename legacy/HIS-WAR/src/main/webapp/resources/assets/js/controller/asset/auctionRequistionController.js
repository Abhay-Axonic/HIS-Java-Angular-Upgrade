(function () {
  "use strict";

  function auctionRequistionController($scope, $rootScope,$state, $http, $cookies, CONSTANTS, GenericService, growl, StatusService, cancelReasonService, commonDetailService, screenDataService,PaginationService) {
    var self = this;
    var auctionId;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject');
    if(cookieObject == undefined){ 
    	$state.go('login');
    	return;
    	}
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId;
    
    
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        stores: [{
          "store": "All Stores"
        }],
        statuses: [{
          "status": "All Status"
        }],
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
        fromDate: new Date(),
        toDate: new Date(),
        SelectedRequestIds: [],
        selectAllRows: false,

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
      StatusService.GetStatus().then(function (status) {
        self.model.statuselect = status;
      });

      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });
      self.model.PopulateDropdown = PopulateDropdown;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.AuctionSearch = AuctionSearch;
      self.model.ApproveAuctionRequistion = ApproveAuctionRequistion;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.isSelected = isSelected;
      self.model.isDisable = isDisable;
      self.model.SelectAuctionRequest = SelectAuctionRequest;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.setStatus = setStatus;
      self.model.EditAuctionChange = EditAuctionChange;
      self.model.EditDetails = EditDetails;
      self.model.AddNewAuction = AddNewAuction;
      // AuctionSearch();
      PopulateDropdown();
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
      var auction = self.model;
      auction.store = auction.stores[0];
      auction.status = auction.statuses[0];
      var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.DROPDOWN_API+ '/' + $scope.unitId  + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          auction.stores = auction.stores.concat(response.data.store);
          auction.statuses = auction.statuses.concat(response.data.status);
          initialCall();
          $rootScope.stopLoader();
          // GetStoreIndents();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        AuctionSearch()
      } else {
          screenDataService.getScreenData().forEach(function (dataobj) {
              if (dataobj.screenName == 'auctionRequistion'){
                  var requestData = dataobj.searchrequest;
              self.model.store = self.model.stores.find(function (store) {
                  if (requestData.store.id == store.id)
                      return store;
              });
              self.model.status = self.model.statuses.find(function (status) {
                  return requestData.status.id == status.id;
              });
              self.model.fromDate = new Date(requestData.fromDate);
              self.model.toDate = new Date(requestData.toDate);
          }
          });
          AuctionSearch();
      }

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

    var AuctionSearch = function () {
      if (ValidateDate()) {
      var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.SEARCH_API;
      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
         "storeId": self.model.store.id, 
        "statusId": self.model.status.id,
        "unitId":$scope.unitId,
      };
      $rootScope.startLoader()
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          for (var i = 0; i < response.data.length; i++) {
            response.data[i]["isChecked"] = false;
          }
          self.model.pagination.data=[];
          self.model.auctionData = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.auctionData, self.model.pagination);
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
        var screenData = {
          searchrequest: {
              "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
              "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
              "status": self.model.status,
              "store": self.model.store
          },
          screenName: 'auctionRequistion',
      }
      screenDataService.setScreenData(screenData);
  }
}

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.auctionData.forEach(function (auction) {
          if (!isDisable(auction)) {
            auction.isSelected = true;
            self.model.isItemChecked = true;
            self.model.SelectedRequestIds.push(auction.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.auctionData.forEach(function (auction) {
          auction.isSelected = false;
          self.model.isItemChecked = false;
        });
      }

    }

    var isSelected = function (auction) {
      if (!isDisable(auction))
        return auction.isSelected;
      else
        return true;
    }

 
    var SelectAuctionRequest = function (auction) {
      var auctionIds = self.model.SelectedRequestIds;

      if (auction.isSelected) {
        auctionIds.push(auction.id);
      } else {
        var index = auctionIds.indexOf(auction.id);
        if (index > -1) {
          auctionIds.splice(index, 1);
        }
      }

      if (self.model.auctionData.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
      self.model.isItemChecked = false;
      self.model.auctionData.forEach(function (auction) {
        if (auction.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });
    }
    var ApproveAuctionRequistion = function () {
      if (self.model.action == 'Update') {
        var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.APPROVE_API;

        var data = {
          "auctionRequisitionId": self.model.SelectedRequestIds,
          "statusId": setStatus('Pending'),
          "approvalStatusId": setStatus('Pending Approval'),
          "userId":$scope.userId,
          
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.SelectedRequestIds = [];
            $rootScope.stopLoader();
            growl.success(response.data.message);
            AuctionSearch();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.msg);
          });
      } else if (self.model.action == 'Cancel') {
        if (ValidateCancelation()) {
        var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.CANCEL_API + auctionId;

        var data = {
          "cancelReasonId": self.model.cancelReason,
          "cancelNote": self.model.cancelNote,
          "statusId": setStatus('Cancelled'),
          "userId": $scope.userId
        };

        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            self.model.SelectedRequestIds = [];
            $rootScope.stopLoader();
            growl.success(response.data.message);
            AuctionSearch();
            ModalDismiss('confirmCancel');
            self.model.cancelReason = "";
            self.model.cancelNote = "";
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.msg);
          });
      }

    }
  }

    var ValidateCancelation = function () {
      var isValid = true;
      if (!self.model.cancelReason) {
        isValid = false;
        growl.error('Please Select Reason to Cancel');
      }
      return isValid;
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    } 
    function setStatus(x) {
      var status = self.model.statuselect.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }
    var ValidateAndUpdate = function (action, popupName, id) {
      auctionId = id;
      self.model.action = action;
      self.model.cancelReason = "";
      self.model.cancelNote = "";
      var popup = angular.element('#' + popupName);
      popup.modal('show');
    }

    var AddNewAuction = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewAuctionRequistion');
  }
    var EditAuctionChange = function (auction) {
      commonDetailService.setDataId(auction.id);
      $state.go('addNewAuctionRequistion');
    }
    var EditDetails = function (auction) {
      commonDetailService.setDataId(auction.id);
      $state.go('detailsauctionRequistion');
    }
    

    var isDisable = function (auction) {
      if (auction.statusId != 1) {
        return true;
      }
      return false;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("auctionRequistion", {
      url: "/auctionRequistion",
      templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/auction-requistion.html',
      controller: "AuctionRequistion.Controller",
      title: 'Auction Requisition',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 208);
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
    .controller("AuctionRequistion.Controller", auctionRequistionController);
})(); 