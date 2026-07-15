(function () {
  "use strict";

  function workOrderRequestController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, reportService) {
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
//          minDate : date.setDate((new Date()).getDate() - 90),
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
        contractTypes: [{
          "description": "All"
        }],
      };

      cancelReasonService.GetReason().then(function(reason) {
        self.model.cancelReasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetWorkOrderRequests = GetWorkOrderRequests;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectWorkOrder = SelectWorkOrder;
      self.model.isDisable = isDisable;
      self.model.UpdateWorkOrder = UpdateWorkOrder;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
     
      self.model.ModalDismiss = ModalDismiss;
      self.model.isEdit = isEdit;
      self.model.isCancel = isCancel;
      self.model.printWorkOrderRequestReport = printWorkOrderRequestReport;

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
      self.model.contractType = self.model.contractTypes[0];
      self.model.status=self.model.statuses[0];
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
          self.model.statuses=self.model.statuses.concat(response.data.status);
          $rootScope.stopLoader();
          GetWorkOrderRequests();
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

    var GetWorkOrderRequests = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "contractTypeId": self.model.contractType ? self.model.contractType.id : undefined,
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId":$scope.unitId
          // "requestNumber": self.model.requestNumber ? self.model.requestNumber : undefined
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.workOrderRequests = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField( self.model.workOrderRequests, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          }
        );
      }
    }

    var NavigateToAddNew = function () {
      $state.go('addItemWorkorder');
    }

    var NavigateToDetails = function (workOrder) {
      commonDetailService.setDataId(workOrder.id);
      $state.go('detailsWorkorder');
    }

    var NavigateToEdit = function (workOrder) {
      commonDetailService.setDataId(workOrder.id);
      $state.go('addItemWorkorder');
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.workOrderRequests.forEach(function (workOrder) {
          if (!isDisable(workOrder)) {
            workOrder.isSelected = true;
            self.model.SelectedRequestIds.push(workOrder.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.workOrderRequests.forEach(function (workOrder) {
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
        return true;
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

      if (self.model.workOrderRequests.every(isSelected)) {
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

    var UpdateWorkOrder = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.APPROVE_API;

          var data = {
            "workOrderIds": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus("Pending Approval"),
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
          };
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              GetWorkOrderRequests();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
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
          var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.CANCEL_API + self.model.id;

          var data = {
            "statusId": setStatus('Closed'),
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
              $rootScope.stopLoader();
              self.model.SelectedRequestIds = [];
              ModalDismiss('cancelWorkOrder');
              GetWorkOrderRequests();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }
    
    /***Chetana**/
    var printWorkOrderRequestReport = function (datum) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Workorder.rpt",
            "reportFormat": "PDF",
            "exportFileName": "WorkOrderRequest",
            "reportParams": [
                {
                    "paramName": "Workorderid",
                    "paramType": "Integer",
                    "paramValue": datum.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("workorderRequest", {
      url: "/workorderRequest",
      templateUrl: 'views/procurement/workOrder/workorder-request.html',
      controller: "WorkOrderRequest.Controller",
      title: 'Work Order Request',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 187);
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
    .controller("WorkOrderRequest.Controller", workOrderRequestController);
})();