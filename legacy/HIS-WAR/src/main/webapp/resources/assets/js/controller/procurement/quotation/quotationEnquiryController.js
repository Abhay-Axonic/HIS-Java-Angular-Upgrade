(function () {
  "use strict";

  function quotationEnquiryController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, StatusService, cancelReasonService, growl, screenDataService, reportService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      var date = new Date();
      self.model = {
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
          minDate : date.setDate((new Date()).getDate() - 180),
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
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      cancelReasonService.GetReason().then(function(reason) {
        self.model.cancelReasons = reason;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetQuotations = GetQuotations;
      self.model.UpdateEnquiry = UpdateEnquiry;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectQuotation = SelectQuotation;
      self.model.isDisable = isDisable;
      self.model.SendForTechApproval = SendForTechApproval;
      self.model.PopupAction = PopupAction;
      self.model.printQuotationEnquiryReport = printQuotationEnquiryReport;

      PopulateDropdown();
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
          GetQuotations()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'quotationEnquiryC'){
            var requestData = dataobj.searchrequest;
          
            self.model.status = self.model.statuses.find(function (status) {
                return requestData.status.id == status.id;
            });

            self.model.store = self.model.storeList.find(function (store) {
                return requestData.store.id == store.id;
            });

            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
          }
        });

        GetQuotations();
      }
    }

    var SendForTechApproval = function (item) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.SEND_FOR_TECHNICAL_APPROVAL;

      var data = {
        "qrId": item.id,
        "organizationId": $scope.organizationId,
        "userId": $scope.userId,
        "unitId": $scope.unitId
      };

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          growl.success(response.data.message);
          GetQuotations();
          $rootScope.stopLoader();
        }, function (err){
          item.technicalApproval = !item.technicalApproval;
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
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

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
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

    var GetQuotations = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId": $scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.quotationEnquiries = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.quotationEnquiries, self.model.pagination);
            }
            self.model.SelectedRequestIds = [];
            self.model.selectAllRows = false;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
        // var screenData = {
        //   searchrequest: {
        //     "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        //     "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        //     "status": self.model.status,
        //     "store": self.model.store
        //   },
        //   screenName: 'quotationEnquiryC'
        // }
        // screenDataService.setScreenData(screenData);
      }
    }

    var SelectAllRequests = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.quotationEnquiries.forEach(function (quotation) {
          if (!isDisable(quotation)) {
            quotation.isSelected = true;
            self.model.SelectedRequestIds.push(quotation.id);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.quotationEnquiries.forEach(function (quotation) {
          quotation.isSelected = false;
        });
      }
    }

    var UpdateEnquiry = function (record) {
      if (record == 'Save') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.APPROVE_QUOTATION_API;

          var data = {
            "qrId": self.model.SelectedRequestIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus("Approved"),
            "userId": $scope.userId
          };
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              GetQuotations();
              self.model.selectAllRows = false;

              $rootScope.stopLoader();
              growl.success(response.data.message);
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select quotation enquiry to approve');
          return;
        }
      } else if (record == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.CANCEL_QUOTATION_API + self.model.id;

          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "cancelNote": self.model.cancelNote,
            "userId": $scope.userId
          }
          
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];

              PopupAction('Close', 'cancelQuotation', {});
              GetQuotations();
              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    var isDisable = function (quotation) {
      if (quotation.status != 'New') {
        return true;
      } else {
        return false;
      }
    }

    var isSelected = function (quotation) {
      if (!isDisable(quotation))
        return quotation.isSelected;
      else
        return true;
    }

    var SelectQuotation = function (quotation) {
      var quotationIds = self.model.SelectedRequestIds;

      if (quotation.isSelected) {
        quotationIds.push(quotation.id);
      } else {
        var index = quotationIds.indexOf(quotation.id);
        if (index > -1) {
          quotationIds.splice(index, 1);
        }
      }

      if (self.model.quotationEnquiries.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var PopupAction = function (action, modal, data) {
      var popup = angular.element('#' + modal);
      if (action == 'Open') {
        self.model.id = data.id;
        popup.modal('show');
      } else if(action == "Close"){
        popup.modal('hide');
        self.model.cancelNote = undefined;
        self.model.cancelReason = undefined;
      }
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

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addItemquotationEnquiry');
    }

    var NavigateToDetails = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('quotationEDetails');
    }

    var NavigateToEdit = function (purchase) {
      commonDetailService.setDataId(purchase.id);
      $state.go('addItemquotationEnquiry');
    }

    var setStatus = function (x){
      var status = self.model.statusList.find(function(obj){
        return obj.status == x;
      });
      
      return status.id;
    }


    /***Chetana**/
    var printQuotationEnquiryReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Quotation_enquiry.rpt",
            "reportFormat": "PDF",
            "exportFileName": "QuotationEnquiry",
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
    $stateProvider.state("quotationEnquiry", {
      url: "/quotationEnquiry",
     templateUrl: 'views/procurement/quotation/quotation-enquiry.html',
      controller: "QuotationEnquiry.Controller",
      title: 'Quotation Enquiry',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 173);
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
    .controller("QuotationEnquiry.Controller", quotationEnquiryController);
})();