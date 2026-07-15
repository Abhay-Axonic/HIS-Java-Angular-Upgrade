(function () {
    "use strict";
  
    function consignmentReturnController($scope, $rootScope, $http, $filter, $state, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, $cookies, CONSTANTS, reportService) {
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
            minDate : moment(new Date()).subtract(180, 'days').toDate()
//            minDate : date.setDate((new Date()).getDate() - 90),
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
            /*{"id": 0,"status": "All"},
            {"id": 1, "status": "Pending"},
            {"id": 2, "status": "Partially Completed"},
            {"id": 3, "status": "Completed"}*/
        	  /*changed by minesh*/
        	  {"id": 0,"status": "All"},
            {"id": 8, "status": " Closed "},
            {"id": 4, "status": "Completed"},
            {"id": 18, "status": "Partially Completed"},
            {"id": 2, "status": "Pending"}
          ],
          assetTypes: [{
            "type": "Select GRN Type"
          }],
          
          SelectedOrderIds: [],
          selectedItems: [],
          
        };
  
        cancelReasonService.GetReason().then(function (reason) {
          self.model.reasons = reason;
        });
  
        StatusService.GetStatus().then(function (status) {
          self.model.statusList = status;
        });
  
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.NavigateToDetails = NavigateToDetails;
        self.model.SearchVendorNames = SearchVendorNames;
        self.model.AddVendorName = AddVendorName;
        self.model.OpenPopup = OpenPopup;
        PopulateDropdown();
        
        self.model.GetConsignmentStockList=GetConsignmentStockList;
        self.model.SelectAllCRItems = SelectAllCRItems;
        self.model.SelectCRItem = SelectCRItem;
        self.model.SaveConsignmentStockList=SaveConsignmentStockList;
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
            GetConsignmentStockList();//minesh
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
      
      
      var GetConsignmentStockList = function () {
          if (ValidateDate()) {
            //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.SEARCH_API;
            var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.SEARCH_API;
            
            var data = {
              "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
              "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
              "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
              "assetType": self.model.grnType ? self.model.grnType.id : undefined,
              "statusId": self.model.status ? self.model.status.id : undefined,
              "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
              "vendorId": self.model.vendorId ? self.model.vendorId : undefined,
              "unitId": $scope.unitId
            }

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
              function (response) {
                self.model.pagination.data=[];
                self.model.consignmentReturnList = response.data;
                console.log(self.model.consignmentReturnList);
                if(!!response && !!response.data && response.data.length > 0){            
                  PaginationService.searchData( self.model.consignmentReturnList, self.model.pagination);
                }
                
                self.model.SelectedOrderIds = [];
                self.model.selectedItems = [];
                self.model.selectAllRows = false;
                angular.forEach(self.model.consignmentReturnList, function (item) {
                  item["disableFlag"] = true;
                });
                
                $rootScope.stopLoader();
              },
              function (err) {
                $rootScope.stopLoader();
                growl.error(err.data.message);
              });
          }
        }
      $scope.saveBtnDisabled=false;
      var SaveConsignmentStockList = function(){
    	  $scope.saveBtnDisabled=true;
    	  var URI =  CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.SAVE_API;
    	  
          var data = [];
          self.model.selectedItems.forEach(function (crItem) {
            if (crItem.isSelected) {
                data.push({
                  "id": crItem.id,
                  "assetTypeId": crItem.id,
                  "statusId": crItem.statusId,
                  "storeId": crItem.storeId,
                  "unitId": $scope.unitId,
                  "userId": $scope.userId
                  });
                }
          });
          
          $rootScope.startLoader();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
            	 $rootScope.stopLoader();
            	 self.model.SelectedOrderIds = [];
            	 self.model.selectedItems = [];
            	 self.model.selectAllRows = false;
            	 
            	 angular.forEach(self.model.consignmentReturnList, function (item) {
                     item["disableFlag"] = true;
                     item.isSelected = false;
                   });
            	 
            	 growl.success(response.data.message);
            	 GetConsignmentStockList();
            	 },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
              }
            );
      }
      
      var SelectCRItem = function (crItem) {
          var crIds = self.model.SelectedOrderIds;
          if (crItem.isSelected) {
        	  crItem.disableFlag = false;
        	  crIds.push(crItem.id);
        	  self.model.selectedItems.push(crItem);        	 
          } else {
        	  crItem.disableFlag = true;
            var index = crIds.indexOf(crItem.id);
            if (index > -1) {
              crIds.splice(index, 1);
              
              var indexOfRemovItem = self.model.selectedItems.findIndex(function(element){
  				  return element.id===crItem.id;
  				  })
  				  if(indexOfRemovItem !== -1){
  					  self.model.selectedItems.splice(indexOfRemovItem, 1)
  					  }
            }
          }
          if (self.model.consignmentReturnList.every(isSelected)) {
              self.model.selectAllRows = true;
            } else {
              self.model.selectAllRows = false;
            }
          console.log(self.model.selectedItems);
        }
      
      var SelectAllCRItems = function () {
          if (self.model.selectAllRows) {
            //self.model.SelectedOrderIds = [];
            self.model.consignmentReturnList.forEach(function (cr) {
              if (!isDisable(cr) && cr.status != "Completed" ) {
                cr.isSelected = true;
                cr.disableFlag = false;
                self.model.SelectedOrderIds.push(cr.id);
                self.model.selectedItems.push(cr);
              }
            });
          } else {
            self.model.SelectedOrderIds = [];
            self.model.selectedItems = [];
            self.model.consignmentReturnList.forEach(function (cr) {
              cr.isSelected = false;
              cr.disableFlag = true;
            });
          }
          console.log(self.model.selectedItems);
        }
      
      var isSelected = function (crItem) {
          if (!isDisable(crItem) && crItem.status != "Completed" )
            return crItem.isSelected;
          else
            return true;
        }
      
        var isDisable = function (crItem) {
          if ((crItem.grnStatus == 'Closed' || crItem.grnStatus == 'Approved') && crItem.isSelected ) {
            return true;
          }
          return false;
        }


      
      var NavigateToDetails = function (item) {
    	  var data = {
    			  "id": item.id,
    			  "storeId": item.storeId
    			  }
    	  commonDetailService.setDataId(data)
    	  
        $state.go('detailsConsignmentReturn');
      }

      var OpenPopup = function (action, popupName, data) {
        self.model.action = action;
        var popup = angular.element('#' + popupName);
        self.model.id = data.id;
        popup.modal('show');
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
      $stateProvider.state("consignmentReturn", {
        url: "/consignmentReturn",
        templateUrl: 'views/procurement/quotation/consignment-return.html',
        controller: "consignmentReturn.Controller",
        title: 'Consignment Return',
          resolve : {
  
              data: function($q, $state, $timeout) {
                  var deferred = $q.defer();
          
                  $timeout(function() {
                      var arr = JSON.parse(localStorage.getItem('permissions'))
                       .modules.filter(function(item){
                         return (item.id == 863);
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
      .controller("consignmentReturn.Controller", consignmentReturnController);
  })();