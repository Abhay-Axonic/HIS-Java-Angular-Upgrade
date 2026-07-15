(function () {
    "use strict";
  
    function consignmentStockConversionController($scope, $rootScope, $http, $filter, $state, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, $cookies, CONSTANTS, reportService) {
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
           /* minDate : date.setDate((new Date()).getDate() - 90),*/
            minDate : moment(new Date()).subtract(180, 'days').toDate()
          },
          toDateOptions: {
            formatYear: 'yyyy',
            showWeeks: false,
            maxDate: new Date()
          },
          storeList: [{
            "store": "Select Store"
          }],
          statuses: [
           /* {"id": 0,"status": "All"},
            {"id": 1, "status": "Pending"},
            {"id": 2, "status": "Partially Completed"},
            {"id": 3, "status": "Completed"}*/
        	  {"id": 0,"status": "All"},
              {"id": 4, "status": " Completed"},
              {"id": 18, "status": "Partially Completed"},
              {"id": 2, "status": " Pending"}
          ],
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
        self.model.GetConsignmentStockConversionList = GetConsignmentStockConversionList;
        self.model.NavigateToDetails = NavigateToDetails;
        PopulateDropdown();
        GetConsignmentStockConversionList();
      }
      
      /*added By Minesh*/
      var GetConsignmentStockConversionList = function () {
          if (ValidateDate()) {
            //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.SEARCH_API;            
            var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_CONVERSION.SEARCH_API;

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
                self.model.consignmentStockConversionList = response.data;
                console.log(self.model.consignmentStockConversionList);
                if(!!response && !!response.data && response.data.length > 0){            
                  PaginationService.searchData( self.model.consignmentStockConversionList, self.model.pagination);
                }
                $rootScope.stopLoader();
              },
              function (err) {
                $rootScope.stopLoader();
                growl.error(err.data.message);
              });
          }
        }
      /* added By Minesh */
  
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
        self.model.grnType = self.model.assetTypes[0];
  
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.storeList = self.model.storeList.concat(response.data.store);
            self.model.statuses = self.model.statuses;
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
            self.model.status = self.model.statuses[0];
           
            $rootScope.stopLoader();
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
        //added by minesh on 2-12-2019 on monday
       /* if(self.model.grnType.id !== 1){
        	 growl.error('Grn type must be Consumable');
             isValid = false;
        }
        if(self.model.status.id !== 0 ){
        	 growl.error('Conversion status must be close');
             isValid = false;
        }*/
      //end added by minesh on 2-12-2019 on monday
  
        return isValid;
      }
  
      var NavigateToDetails = function (item) {
    	  var data = {
    			  "id": item.id,
    			  "storeId": item.storeId
    			  }
    	  commonDetailService.setDataId(data)
    	  $state.go('detailsConsignmentStockConversion');
    	  }
  
      var ValidetReason = function () {
        var isValid = true;
  
        if (!self.model.cancelReason) {
          self.model.isReasonValid = true;
          isValid = false;
        }
  
        return isValid;
      }
  
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("consignmentStockConversion", {
        url: "/consignmentStockConversion",
        templateUrl: 'views/procurement/quotation/consignment-stock-conversion.html',
        controller: "consignmentStockConversion.Controller",
        title: 'Consignment Stock Conversion',
          resolve : {
  
              data: function($q, $state, $timeout) {
                  var deferred = $q.defer();
          
                  $timeout(function() {
                      var arr = JSON.parse(localStorage.getItem('permissions'))
                       .modules.filter(function(item){
                         return (item.id == 858);
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
      .controller("consignmentStockConversion.Controller", consignmentStockConversionController);
  })();