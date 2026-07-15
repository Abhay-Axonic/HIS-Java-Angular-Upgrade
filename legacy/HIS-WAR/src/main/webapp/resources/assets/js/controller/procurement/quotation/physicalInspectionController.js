(function () {
  "use strict";

  function physicalInspectController($scope, $rootScope, $http, $filter, $state, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, $cookies, CONSTANTS, reportService) {
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
//          minDate : date.setDate((new Date()).getDate() - 90),
          minDate : moment(new Date()).subtract(360, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        storeList: [{
          "store": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }],
        assetTypes: [{
          "type": "Select GRN Store"
        }]
      };

      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetPhysicalInspectionList = GetPhysicalInspectionList;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.printPhysicalInspectReport = printPhysicalInspectReport;
      PopulateDropdown();
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

    var PopulateDropdown = function () {
      self.model.store = self.model.storeList[0];
      self.model.status = self.model.statuses[0];
      self.model.grnType = self.model.assetTypes[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storeList = self.model.storeList.concat(response.data.store);
          self.model.statuses = self.model.statuses.concat(response.data.status);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);

          $rootScope.stopLoader();
          GetPhysicalInspectionList();
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

    var GetPhysicalInspectionList = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.SEARCH_API;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "assetType": self.model.grnType ? self.model.grnType.id : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId": $scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.physicalInspectionList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.physicalInspectionList, self.model.pagination);
            }
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var NavigateToDetails = function (pi) {
      var data = {
        "id": pi.id,
        "storeId": pi.storeId
      }
      commonDetailService.setDataId(data)
      $state.go('detailsphysicalInspec');
    }

    var ValidetReason = function () {
      var isValid = true;

      if (!self.model.cancelReason) {
        self.model.isReasonValid = true;
        isValid = false;
      }

      return isValid;
    }
    
    /***Chetana**/
    var printPhysicalInspectReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_physical_Inspection_details.rpt",
            "reportFormat": "PDF",
            "exportFileName": "PhysicalInspect",
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
    $stateProvider.state("physicalInspec", {
      url: "/physicalInspec",
      templateUrl: 'views/procurement/quotation/physical-inspection.html',
      controller: "PhysicalInspect.Controller",
      title: 'Physical Inspection',
  	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 184);
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
    .controller("PhysicalInspect.Controller", physicalInspectController);
})();