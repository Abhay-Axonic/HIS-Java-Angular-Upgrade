(function () {
    "use strict";
  
    function auctionRequisitionApprovalController($scope, $rootScope, $http, $state, $cookies, CONSTANTS,StatusService, GenericService, commonDetailService, growl, screenDataService , PaginationService) {
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
                "store": "Select Store"
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
    self.model.PopulateDropdown = PopulateDropdown;
    self.model.OpenFromDate = OpenFromDate;
    self.model.OpenToDate = OpenToDate;
    self.model.AuctionApprovalSearch = AuctionApprovalSearch;
    self.model.SelectAllRequests = SelectAllRequests;
    self.model.isSelected = isSelected;
    self.model.isDisable = isDisable;
    self.model.SelectAuctionRequest = SelectAuctionRequest;
    self.model.EditAuctionChange = EditAuctionChange;
    self.model.RejectionPopup = RejectionPopup;
    self.model.ApproveAuctionRequistion = ApproveAuctionRequistion;
    self.model.ValidateAndUpdate = ValidateAndUpdate;
    
    PopulateDropdown();
   // AuctionApprovalSearch();
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
        var URI = CONSTANTS.ASSET.AUCTION_REQUISITION_APPROVAL.DROPDOWN_API + '/'+$scope.unitId + '/' + $scope.organizationId + '/' + $scope.userId;
        auction.store = auction.stores[0];
        auction.status = auction.statuses[0];
        auction.stores.splice(1);
        initialCall();
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                auction.stores = auction.stores.concat(response.data.store);
                auction.statuses = auction.statuses.concat(response.data.status);
                $rootScope.stopLoader();
            },
            function (err) {
                $rootScope.stopLoader();
            });
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        AuctionApprovalSearch()
      } else {
          screenDataService.getScreenData().forEach(function (dataobj) {
              if (dataobj.screenName == 'auctionRequisitionApproval'){
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
          AuctionApprovalSearch();
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

    var AuctionApprovalSearch = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.ASSET.AUCTION_REQUISITION_APPROVAL.SEARCH_API;
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
            
            self.model.pagination.data = [];
            self.model.auctionApprovalData = response.data;
            
            if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchData(self.model.auctionApprovalData , self.model.pagination);
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
            screenName: 'auctionRequisitionApproval',
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
      var isDisable = function (auction) {
        if (auction.status != 'Pending') {
          return true;
        }
        return false;
      }
  
      var SelectAuctionRequest = function (auction) {
        self.model.SelectedItems = auction;
        var auctionIds = self.model.SelectedRequestIds;
  
        if (auction.isSelected) {
          auctionIds.push(auction.id);
        } else {
          var index = auctionIds.indexOf(auction.id);
          if (index > -1) {
            auctionIds.splice(index, 1);
          }
        }
  
        if (self.model.auctionApprovalData.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
        self.model.isItemChecked = false;
        self.model.auctionApprovalData.forEach(function (auction) {
          if (auction.isSelected == true) {
            self.model.isItemChecked = true;
            return;
          }
        });
      }

      var EditAuctionChange = function (auction) {
        commonDetailService.setDataId(auction.id);
        $state.go('detailsAuctionRequisitionApproval');
      }

      var RejectionPopup = function (item) {
        if (item.isReject) {
          itemId = item.id;
          self.model.action = 'Open';
          var popup = angular.element('#qtyaccepatReject');
          popup.modal('show');
          self.model.reasons = self.model.rejectReasons;
          self.model.rejectReason = self.model.reasons[0];
          // self.model.rejectQty = item.rejectQuantity;
          self.model.rejectNote = undefined;
        } else {
          CancelRejection(item);
        }
      }

      var CancelRejection = function (item) {
        var prevRequest = self.model.requestItems.items.find(function(prevItem){
          return prevItem.id == item.id;
        });
  
        if (prevRequest) {
          item.rejectQuantity = prevRequest.rejectQuantity;
          item.approvedQuantity = prevRequest.approvedQuantity;
          item.rejectNote = prevRequest.rejectNote;
          item.rejectReasonId = prevRequest.rejectReasonId;
          item.statusId = prevRequest.statusId;
        }
      }


      var ApproveAuctionRequistion = function () {
        if (self.model.action == 'Update') {
          var URI = CONSTANTS.ASSET.AUCTION_REQUISITION_APPROVAL.APPROVE_API;
  
          var data = {
            "auctionRequisitionId": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus('Partially Approved'),
            "userId":$scope.userId,
            "organizationId":$scope.organizationId,
			"unitId":$scope.unitId,
          };
  
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              $rootScope.stopLoader();
              growl.success(response.data.message);
              AuctionApprovalSearch()
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.msg);
            });
        }
  
      }

      var ValidateAndUpdate = function (action, popupName, id) {
        auctionId = id;
        self.model.action = action;
        var popup = angular.element('#' + popupName);
        popup.modal('show');
      }
      var setStatus = function (x){
        var status = self.model.statuses.find(function(obj){
          return obj.status == x;
        });
        
        return status.id;
      }
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("auctionRequisitionApproval", {
        url: "/auctionRequisitionApproval",
        templateUrl: 'views/Asset Management/auction-requisition-approval.html',
        controller: "AuctionRequisitionApproval.Controller",
        title: 'Auction Requisition Approval',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 209);
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
      .controller("AuctionRequisitionApproval.Controller", auctionRequisitionApprovalController);
  })();

//   .state('auctionRequisitionApproval', {
// 		 url: '/auctionRequisitionApproval',
// 		 templateUrl: 'views/Asset Management/auction-requisition-approval.html',
// 		 controller: "MasterController"
// 	})