(function () {
  "use strict";

  function GRNController($scope, $rootScope, $http, $filter, $state, screenDataService,reportService, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, CONSTANTS, $cookies) {
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
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
        fromDate: new Date($scope.selectedDate),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
          //minDate : moment(new Date()).subtract(360, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        selectAllRows: false,
        SelectedGRNIds: [],
        storeList: [{
          "store": "Select Store"
        }],
        fromstoreList: [{
          "store": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }],
        assetTypes: [{
          "type": "Select GRN Type"
        }],
        vendorId: null
      };

      cancelReasonService.GetReason().then(function(reason) {
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });
      //$scope.selectedDate= new Date();
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetGRNList = GetGRNList;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllGRN = SelectAllGRN;
      self.model.SelectGRN = SelectGRN;
      self.model.isDisable = isDisable;
      self.model.isDisableButton = isDisableButton;
      self.model.UpdateGRN = UpdateGRN;
      self.model.OpenPopup = OpenPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.rePrintBillSlip=rePrintBillSlip;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.AddItemName = AddItemName;
      self.model.SearchItems = SearchItems;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
     // self.model.OpenPopup = OpenPopup;

      PopulateDropdown();
      GetGRNList();
    }

    var SearchItems = function (search) {
       if ((!self.model.prevSearch || self.model.prevSearch != search) ) {
        self.model.prevSearch = search;

        var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId + "/" + false + "/" + search + "/" + self.model.fromstore.id;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
          .then(function (response) {
            $rootScope.stopLoader();
            if (!!response.data && response.data.length > 0) {
              self.model.items = response.data;
              return $filter('filter')(self.model.items, {
                $: search
              });
            } else {
              NoItemFound(search);
              return self.model.items;
            }
          }, function (err) {
            NoItemFound(search);
            $rootScope.stopLoader();
            return self.model.items;
          });
      } else {
        if (!!self.model.items && self.model.items.length > 0 &&
          self.model.items[0].itemFound != undefined && !self.model.items[0].itemFound) {
          return NoItemFound(search);
        } else {
          return $filter('filter')(self.model.items, {
            $: search
          });
        }
      }
    }

    var NoItemFound = function (search) {
      var item = {
        "itemFound": false,
        "details": "No item found with item code or name " + search
      };
      self.model.items = [item];
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

       $scope.selectedDate = new Date();
       $scope.threeMonthsLater = new Date();

      $scope.calculateThreeMonthsLater = function() {
        var selectedMoment = moment(self.model.fromDate);
            var curMoment = moment(); // Use moment.js for handling dates and time
            var diff = curMoment.diff(selectedMoment, 'months', true); // Calculate the difference in months with decimal precision
        if(diff>=3)
        {
        if (selectedMoment.isValid()) {
            $scope.threeMonthsLater = selectedMoment.add(3, 'months').toDate();
             $scope.threeMonthsLater = selectedMoment.subtract(1, 'day').toDate();
              self.model.toDate=$scope.threeMonthsLater
        }
        }
        if(diff<3)
        {
        self.model.toDate=new Date();
        }

      }



    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetGRNList();
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'grn'){
            var requestData = dataobj.searchrequest;
            self.model.grnType = self.model.assetTypes.find(function (type) {
              if (requestData.grnType.id == type.id)
                return type;
            });
            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
                return $scope.storeId == store.id;
            });
            self.model.fromstore = self.model.fromstoreList.find(function (store) {
              return $scope.storeId == store.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
            self.model.grnNumber = requestData.grnNumber;
          }
        });

        GetGRNList();
      }
    }

    var PopulateDropdown = function () {
      self.model.store = self.model.storeList[0];
      self.model.fromstore = self.model.fromstoreList[0];
      self.model.status = self.model.statuses[0];
      self.model.grnType = self.model.assetTypes[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GRN_DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.deliveryStores);
          self.model.fromstoreList = self.model.fromstoreList.concat(response.data.requestingStores);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
          $rootScope.stopLoader();
          
          self.model.store = self.model.storeList.find(function (store) {
              return $scope.storeId == store.id;
          });
          self.model.fromstore = self.model.fromstoreList.find(function (store) {
            return $scope.storeId == store.id;
          });
          initialCall();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
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

    function AddItemName (vendor) {
      var abc = vendor.details.split('|');
      self.model.itemName = abc[1];
      self.model.itemId = vendor.id;
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

    var GetGRNList = function () {
      if (ValidateDate()) {
        var grn = self.model;
        
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.SEARCH_GRN_API;

        if (!self.model.vendorName) {
          self.model.vendorId = undefined;
          self.model.vendorName = undefined;
        }
        
        var data = {
          "fromDate": moment(grn.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(grn.toDate).format('YYYY-MM-DD'),
          "storeId": !!grn.store && !!grn.store.id ? grn.store.id : undefined,
          "requestingStoreId": !!grn.fromstore && !! grn.fromstore.id ? grn.fromstore.id : undefined,
          "grnNumber": grn.grnNumber ? grn.grnNumber : undefined,
          "grnTypeId": grn.grnType ? grn.grnType.id : undefined,
          "vendorName": grn.vendorName ? grn.vendorName : undefined,
          "vendorId": grn.vendorId ? grn.vendorId : undefined,
          "statusId": !!grn.status && !!grn.status.id ? grn.status.id : undefined,
          "unitId": $scope.unitId,
          "itemId": self.model.itemId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            grn.grnList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.grnList, self.model.pagination);
            }
            self.model.SelectedGRNIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            growl.error(err.data.message);
            $rootScope.stopLoader();
          }
        );

        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "status": self.model.status,
            "grnNumber": self.model.grnNumber,
            "store": self.model.store,
            "fromstore": self.model.fromstore,
            "vendorName": self.model.vendorName,
            "grnType": self.model.grnType,
            "vendorId": self.model.vendorId
          },
          screenName: 'grn'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addItemGRN');
    }

    var NavigateToDetails = function (grn) {
      commonDetailService.setDataId(grn.id);
      $state.go('GRNdetails');
    }

    var NavigateToEdit = function (grn) {
      commonDetailService.setDataId(grn.id);
      commonDetailService.setIsFromApprovalEdit(false);
      $state.go('addItemGRN');
    }

    var SelectAllGRN = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedGRNIds = [];
        self.model.grnList.forEach(function (grn) {
          if (!isDisableButton(grn)) {
            grn.isSelected = true;
            self.model.SelectedGRNIds.push(grn.id);
          }
        });
      } else {
        self.model.SelectedGRNIds = [];
        self.model.grnList.forEach(function (grn) {
          grn.isSelected = false;
        });
      }
    }

    var isDisable = function (grn) {
      if (grn.approvalStatus == 'Approved' || grn.approvalStatus == 'Rejected' || grn.approvalStatus == 'Cancelled' ) {
        return true;
      }
      return false;
    }
    var isDisableButton = function (grn) {
        if (grn.status != 'New') {
          return true;
        }
        return false;
      }
    var isSelected = function (grn) {
      if (!isDisableButton(grn))
        return grn.isSelected;
      else
        return true;
    }

    var SelectGRN = function (grn) {
      var purchaseIds = self.model.SelectedGRNIds;

      if (grn.isSelected) {
        purchaseIds.push(grn.id);
      } else {
        var index = purchaseIds.indexOf(grn.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.grnList.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var OpenPopup = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.id = data.id;
      popup.modal('show');
    }

    var ValidateStatus = function (){
      var isValid = true;
      if (!self.model.SelectedGRNIds || self.model.SelectedGRNIds.length == 0) {
        isValid = false;
      }
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    var ValidetReason = function () {
      var isValid = true;

      if (!self.model.cancelReason) {
        self.model.isReasonValid = true;
        isValid = false;
      }
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }
    $scope.saveBtnDisabled=false;
    var UpdateGRN = function () {
        $scope.saveBtnDisabled=true;
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.APPROVE_GRN_API;

          var data = {
            "grnIdList": self.model.SelectedGRNIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus('Pending Approval'),
            "userId" :cookieObject.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedGRNIds = [];
              ModalDismiss('confirmSave');
              $rootScope.stopLoader();
              GetGRNList();
              self.model.selectAllRows = false;
              growl.success(response.data.message);
            },
            function (err) {
              ModalDismiss('confirmSave');
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select GRN to approve');
          ModalDismiss('confirmSave');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.CANCEL_GRN_API + self.model.id;;

          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "cancelNote": self.model.rejectNote,
            "userId" :cookieObject.userId
          }

          $rootScope.startLoader();         
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedGRNIds = [];
              ModalDismiss('cancelGRN');
              GetGRNList();

              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              growl.error(err.data.message);
              $rootScope.stopLoader();
            });
        }
      }
      $scope.saveBtnDisabled=false;
    }

    var rePrintBillSlip = function (grn) {
      var data = {
          "appName": "HIS",
          "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
          "reportDesignFilePath": "get_GRN_listview.rpt",
          "reportFormat": "PDF",
          "exportFileName": "Grn",
          "reportParams": [
              {
                  "paramName": "Id",
                  "paramType": "Integer",
                  "paramValue": grn.id
              },
              {
                "paramName": "Storeid",
                "paramType": "Integer",
                "paramValue": grn.storeId
            }
          ]
      };
      reportService.generate(REPORT_API_URI1, data);
  }
  
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("GRN", {
      url: "/GRN",
      templateUrl: 'views/procurement/quotation/GRN.html',
      controller: "GRN.Controller",
      title: 'Good Receipt Note',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 181);
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
    .controller("GRN.Controller", GRNController);
})();