(function () {
  "use strict";

  function workOrderCreationController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, screenDataService, StatusService, growl, reportService) {
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
          maxDate: new Date()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        selectAllRows: false,
        SelectedRequestIds: [],
        storeList: [{
          "store": "All"
        }],
        statuses: [{
          "status": "All"
        }],
        status: [{
          "status": "Select Status"
        }],
        requestTypes: [{
          "description": "Select Against"
        }],
        contractTypes: [{
          "description": "Select Contract Type"
        }],
      };

      cancelReasonService.GetReason().then(function (reason) {
        self.model.cancelReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetWorkOrderList = GetWorkOrderList;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectWorkOrder = SelectWorkOrder;
      self.model.isDisable = isDisable;
      self.model.UpdateWorkOrder = UpdateWorkOrder;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.ModalDismiss = ModalDismiss;
      self.model.isEdit = isEdit;
      self.model.isCancel = isCancel;
      self.model.AddVendorName = AddVendorName;
      self.model.SelectAllOrders = SelectAllOrders;
      self.model.printWorkOrderCreationReport = printWorkOrderCreationReport
      PopulateDropdown();
      PopulateDropdownContractType();
    }

    var setStatus = function (x) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    function SearchVendorNames(search) {
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;
        var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {})
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

    function AddVendorName(vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }
    var setStatus = function (x) {
      var status = self.model.statusList.find(function (obj) {
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

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        GetWorkOrderList()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'poCreation') {
            var requestData = dataobj.searchrequest;

            self.model.status = self.model.statuses.find(function (status) {
              return requestData.status.id == status.id;
            });
            self.model.store = self.model.storeList.find(function (store) {
              return requestData.store.id == store.id;
            });
            self.model.requestTypes = self.model.requestTypes.find(function (req) {
              return requestData.requestType.id == req.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
            self.model.vendorId = requestData.vendorId;
            self.model.vendorName = requestData.vendorName;
            self.model.workOrderNo = requestData.workOrderNo;
          }
        });

        GetWorkOrderList();
      }
    }

    var PopulateDropdown = function () {
      self.model.store = self.model.storeList[0];
      self.model.requestType = self.model.requestTypes[0];
      self.model.status = self.model.statuses[0];

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.requestTypes = self.model.requestTypes.concat(response.data.requestType);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          

          $rootScope.stopLoader();
          GetWorkOrderList();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateDropdownContractType = function () {
      try {
        $rootScope.startLoader();
        self.model.contractType = self.model.contractTypes[0];
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
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

    var GetWorkOrderList = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.SEARCH_API;
        if (!self.model.vendorName) {
          self.model.vendorId = undefined;
          self.model.vendorName = undefined;
        }

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "contractTypeId": !!self.model.contractType && !!self.model.contractType.id ? self.model.contractType.id : undefined,
          "vendorId": self.model.vendorId ? self.model.vendorId : undefined,
          "woNo": self.model.workOrderNo ? self.model.workOrderNo : undefined,
          "woAgainstTypeId":!!self.model.requestType && !!self.model.requestType.id ? self.model.requestType.id : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
        		  "unitId":$scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.workOrders = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.workOrders, self.model.pagination);
            }
            self.model.SelectedOrderIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.status);
          });

        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "status": self.model.status,
            "store": self.model.store,
            "vendorId": self.model.vendorId,
            "vendorName": self.model.vendorName,
            "workOrderNo": self.model.workOrderNo
          },
          screenName: 'woCreation'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var NavigateToAddNew = function () {
      $state.go('addItemWorkorderCreation');
    }

    var NavigateToDetails = function (workOrder) {
      commonDetailService.setDataId(workOrder.id);
      $state.go('detailsWorkorderCreation');
    }

    var NavigateToEdit = function (workOrder) {
      commonDetailService.setDataId(workOrder.id);
      $state.go('addItemWorkorderCreation');
    }

    var SelectAllOrders = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.workOrders.forEach(function (workOrder) {
          if (!isDisable(workOrder)) {
            workOrder.isSelected = true;
            self.model.SelectedRequestIds.push(workOrder.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.workOrders.forEach(function (workOrder) {
          workOrder.isSelected = false;
        });
      }
    }

    var isDisable = function (workOrder) {
      if (workOrder.status != 'New') {
        return true;
      }
      return false;
    }

    var isEdit = function (workOrder) {
      if (workOrder.status == 'Pending') {
        return true;
      } else if (workOrder.status == 'New') {
        return false;
      } else if (workOrder.status == 'Closed' || workOrder.status == 'Cancelled') {
        return true;
      }
    }

    var isCancel = function (workOrder) {
      if (workOrder.status == 'Pending') {
        return false;
      } else if (workOrder.status == 'New') {
        return false;
      } else if (workOrder.status == 'Closed' || workOrder.status == 'Cancelled') {
        return true;
      }
    }

    var isSelected = function (workOrder) {
      if (!isDisable(workOrder))
        return workOrder.isSelected;
      else
        return true;
    }

    var SelectWorkOrder = function (workOrder) {
      var workOrderIds = self.model.SelectedRequestIds;

      if (workOrder.isSelected) {
        workOrderIds.push(workOrder.id);
      } else {
        var index = workOrderIds.indexOf(workOrder.id);
        if (index > -1) {
          workOrderIds.splice(index, 1);
        }
      }

      if (self.model.workOrders.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var ValidateAndUpdate = function (action, popupName, data) {
      self.model.action = action;
      self.model.id = data.id;
      var popup = angular.element('#' + popupName);
      popup.modal('show');
    }

    var ValidateStatus = function () {
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

    var UpdateWorkOrder = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.APPROVE_API;

          var data = {
            "woCreationIds": self.model.SelectedRequestIds,
            "statusId": 2,
            "approvalStatusId": 10,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              ModalDismiss('confirmSave');
              GetWorkOrderList();
              self.model.selectAllRows = false;
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Select Work Order Request to Approve');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Cancelled'),
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
              ModalDismiss('cancelWO');
              GetWorkOrderList();
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
    var printWorkOrderCreationReport = function (workOrder) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_WorkOrderCreationDetails.rpt",
            "reportFormat": "PDF",
            "exportFileName": "WorkOrderCreation",
            "reportParams": [
                {
                    "paramName": "id",
                    "paramType": "Integer",
                    "paramValue": workOrder.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 
    
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("workorderCreation", {
      url: "/workorderCreation",
      templateUrl: 'views/procurement/workOrder/workorder-creation.html',
      controller: "WorkOrderCreation.Controller",
      title: 'Work Order Creation',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 194);
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
    .controller("WorkOrderCreation.Controller", workOrderCreationController);
})();