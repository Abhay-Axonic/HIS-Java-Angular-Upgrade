(function() {
  "use strict";
  function taxMasterController(
    $scope,
    $rootScope,
    $http,
    $cookies,
    CONSTANTS,
    GenericService,
    PaginationService,
    growl
  ) {
    var self = this;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); 
    if(cookieObject == undefined){ 
    	$state.go('login'); return;
    	}
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId;
    
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        taxDetailList: [],
        istaxComponentValid: [],
        istaxPerValid: [],
        taxList: [
          {
            component: "Select Tax Component"
          }
        ],
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
      generalLedgerName:""
      };
      //Methods
      self.model.ToggleStatus = ToggleStatus;
      self.model.SaveTax = SaveTax;
      self.model.ClearFields = ClearFields;
      self.model.PopulateTaxComponent = PopulateTaxComponent;
      self.model.ValidateTaxComponent = ValidateTaxComponent;
      self.model.getTaxComponent = getTaxComponent;
      self.model.ViewTax = ViewTax;
      self.model.GetTaxMasterData = GetTaxMasterData;
      self.model.getTaxDetails = getTaxDetails;
      self.model.AddTax = AddTax;
      self.model.LessTax = LessTax;
      self.model.SaveTaxDetails = SaveTaxDetails;
      self.model.CreateTaxComponent = CreateTaxComponent;
      self.model.checkEmptyTaxDetails = checkEmptyTaxDetails;
      self.model.isAbleSave = isAbleSave;
      self.model.clearSearch = clearSearch;
      self.model.SetDecimal = SetDecimal;
      self.model.isOk = false;
      PopulateTaxMaster();
      getTaxComponent();
      self.model.taxDetailList = [
        { component: self.model.taxList[0], taxPercent: null }
      ];
    }

    var SaveTax = function() {
      // if (ValidateTaxComponent()) {
      if (!self.model.taxId) {
        CreateTaxComponent();
      } else {
        updateTaxComponent();
      }
      // }
    };

    //Method to get the list of Tax Component
    var getTaxComponent = function() {
      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_MASTER +"org/"+$scope.organizationId;;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.taxMasterList = response.data;
          angular.forEach(self.model.taxMasterList, function(value1 , key) {
              angular.forEach($scope.ledgerList, function(value , key) {
                  if(value1.generalLedgerId==value.generalLedgerId)
                      {
                      value1.generalLedgerName=value.generalLedgerName;
                      }
              });
              });
          if (!!response && !!response.data && response.data.length > 0) {
            PaginationService.searchData(self.model.taxMasterList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };
    var PopulateTaxMaster = function() {
      var taxMaster = self.model;

      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER
          .TAX_COMPONENT_MASTER_DROPDOWN;
      taxMaster.componet = taxMaster.taxList[0];

      taxMaster.taxList.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          taxMaster.taxList = taxMaster.taxList.concat(response.data);
          $rootScope.stopLoader();
        },
        function(err) {
          $rootScope.stopLoader();
        }
      );
    };
    var ViewTax = function(item) {
      angular.forEach(self.model.taxMasterList, function(x) {
        if (item.id == x.id) {
          self.model.taxDetails = item.items;
        }
      });
    };
    // Method to edit tax data
    var GetTaxMasterData = function(taxId) {
      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER
          .TAX_COMPONENT_MASTER + taxId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        self.model.taxId = response.data.id;
        self.model.code = response.data.code;
        self.model.description = response.data.name;
        self.model.taxPercentage = response.data.taxPercentage;
        var listTax = [];
        angular.forEach($scope.ledgerList, function(value , key) {
            if(response.data.generalLedgerId==value.generalLedgerId)
                {
                self.model.generalLedgerName=value.generalLedgerName;
                }
        });
        angular.forEach(response.data.items, function(data) {
          var selectedTax = self.model.taxList.find(function(taxData) {
            if (taxData.id == data.taxComponentId) {
              return taxData;
            }
          });

          listTax.push({
            id: data.id,
            component: selectedTax,
            taxPercent: data.taxPercentage
          });
        });
        self.model.taxDetailList = listTax;
        $rootScope.stopLoader();
      });
    };
    var getTaxDetails = function() {};

    var AddTax = function(item, idx) {
      self.model.isOk = false;
      // var otherCharge = self.model.otherChargesDropdownList[0];
      self.model.taxDetailList.push({ component: item.id, taxPercent: null });
      self.model.taxDetailList[idx + 1].component = self.model.taxList[0];
    };

    var LessTax = function(item, idx) {
      self.model.isOk = false;
      var index = self.model.taxDetailList.indexOf(item);
      self.model.taxDetailList.splice(index, 1);
    };

    var SaveTaxDetails = function(taxDuplicateData) {
      if (checkEmptyTaxDetails() && DuplicateData(self.model.taxDetailList)) {
        self.model.isOk = true;
        var test = [];
        var total = 0;
        var taxData = self.model.taxList.find(function(type) {
          return type.id == self.model.component;
        });
        angular.forEach(self.model.taxDetailList, function(tax) {
          test.push = {
            component: tax.component,
            taxPercent: tax.taxPercent
          };
          total = total + parseFloat(tax.taxPercent);
        });
        self.model.taxPercentage = total;
        var taxArray = [];
        angular.forEach(self.model.taxDetailList, function(x) {
          taxArray.push({
            taxComponentId: x.component.id,
            taxPercentage: parseFloat(x.taxPercent)
          });
        });
        self.model.taxArray = taxArray;
        var popup = angular.element("#addtaxcomponent");
        popup.modal("hide");
        ViewTax();
      }
    };

    function DuplicateData(taxDuplicateData) {
      var isValid = true;
      var object = {};
      var result = [];

      taxDuplicateData.forEach(function(item) {
      //  if (!!item.id) {
          if (!object[item.component.id]) object[item.component.id] = 0;
          object[item.component.id] += 1;
      //  }
      });

      for (var prop in object) {
        if (object[prop] >= 2) {
          result.push(prop);
        }
      }

      if (!!result.length) {
        isValid = false;
        growl.error("Duplicate Tax Component");
      }

      return isValid;
    }

    //Method to populate Tax Component for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getTaxComponentById(id) {
      return findInRecordInArray(self.model.taxComponentList, "id", id);
    }

    function PopulateTaxComponent(id) {
      self.model.istaxComponentCodeValid = false;
      self.model.isdescriptionValid = false;
      self.model.activeTaxComponentId = id;
      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER
          .TAX_COMPONENT_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        var taxComponent = getTaxComponentById(id);
        taxComponent = response.data;
        self.model.code = response.data.code;
        self.model.component = response.data.component;
        self.model.status = response.data.status;
        self.model.id = response.data.id;
        self.model.isEdit = true;
      });
    }

    //Method to activate or deactivate Tax Component
    var ToggleStatus = function(taxComponent) {
      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER
          .TAX_COMPONENT_MASTER + taxComponent.id;

  
          var data = {
            "status": taxComponent.status
          };
          if (taxComponent.status == false) {
            ClearFields();
          }
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              $rootScope.stopLoader();
              growl.success('Status changed successfully');
            }, function (err) {
              if(err.status == 406){
                growl.error(err.data.message);
                taxComponent.status = true;
              }else{
             
              growl.error('Something Went Wrong');
            }
            });
            $rootScope.stopLoader();
    };
    $scope.getLedgerList = function(keyword)
    {
        $scope.ledgerList=[];
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
       var data = {
               
           }
        GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       $scope.ledgerList=response.data.listObject;
                   }
               });
    }
    $scope.getLedgerList();
    $scope.ledgerNameSearch = function(keyword)
    {
    	self.model.generalLedgerId=null;
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
       var data = {
               "generalLedgerName":keyword
           }
       return GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       return response.data.listObject;
                   }
               });
    }
    $scope.SelectLedgerName =function(ledger)
   {
        self.model.generalLedgerId=ledger.generalLedgerId;
   }
    $scope.clearText = function()
    {
    	if(self.model.generalLedgerId==undefined || self.model.generalLedgerId==null || self.model.generalLedgerId==0)
    		{
    		self.model.generalLedgerName="";
    		}
    }
    var ValidateCode = function() {
      var isValid = true;

      self.model.taxMasterList.forEach(function(element) {
        if (element.code == self.model.code) {
          growl.error("Duplicate Tax code");
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error("Tax Code can only be alpha numeric with single hypen.");
        return;
      }
      if (self.model.taxPercentage > 99) {
        growl.error("Tax % should be less than 99");
        return;
      }
      if (self.model.isOk == false) {
        growl.error("Save Items in Tax Details");
        return;
      }
      return isValid;
    };

    var ValidateCodeForUpdate = function() {
      var isValid = true;

      self.model.taxMasterList.forEach(function(element) {
        if (element.id != self.model.taxId) {
          if (element.code == self.model.code) {
            growl.error("Duplicate Tax code");
            isValid = false;
          }
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error("Tax Code can only be alpha numeric with single hypen.");
        return;
      }
      if (self.model.taxPercentage > 99) {
        growl.error("Tax % should be less than 99%");
        return;
      }
      return isValid;
    };

    //Method to add the TaxComponent
    var CreateTaxComponent = function() {
      if (ValidateCode()) {
        var URI =
          CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_MASTER_SAVE_API;
        var data = {
          taxPercentage: self.model.taxPercentage,
          code: self.model.code,
          name: self.model.description,
          items: self.model.taxArray,
          generalLedgerId:self.model.generalLedgerId
        };
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function(response) {
            var taxComponent = {
              id: response.data.id,
              code: self.model.code,
              name: self.model.description,
              taxPercentage: self.model.taxPercentage,
              items: self.model.taxArray,
              status: true
            };
            self.model.taxMasterList.push(taxComponent);
            PaginationService.searchData(self.model.taxMasterList, self.model.pagination);
            PaginationService.setLastPage(self.model.pagination);
            ClearFields();
            getTaxComponent();
            ClearFields();
            self.model.generalLedgerName="";
            $rootScope.stopLoader();
            growl.success(response.data.message);
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    };

    //Method to update the Tax Component
    var updateTaxComponent = function() {
      if (ValidateCodeForUpdate()) {
        var URI =
          CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_MASTER + self.model.taxId;
        var taxArray = [];
        angular.forEach(self.model.taxDetailList, function(x) {
          taxArray.push({
            id: x.id,
            taxComponentId: x.component.id,
            taxPercentage: parseFloat(x.taxPercent)
          });
        });
        self.model.taxArray = taxArray;
        var data = {
          taxPercentage: self.model.taxPercentage,
          code: self.model.code,
          name: self.model.description,
          items: self.model.taxArray,
          generalLedgerId:self.model.generalLedgerId
        };

        // $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(function(
          response
        ) {
          var taxComponent = self.model.taxMasterList.find(function(type) {
            return type.id == self.model.taxId;
          });
          taxComponent.code = data.code;
          taxComponent.name = data.name;
          taxComponent.taxPercentage = data.taxPercentage;

          var listTax = [];
          angular.forEach(response.data.items, function(data) {
            var selectedTax = self.model.taxList.find(function(taxData) {
              if (taxData.id == 1) {
                return taxData;
              }
            });

            listTax.push({
              id: data.id,
              component: selectedTax,
              taxPercent: data.taxPercentage
            });
          });
          self.model.taxDetailList = listTax;
          getTaxComponent();

          ClearFields();
          self.model.generalLedgerName="";
          growl.success(response.data.message);
          // self.model.activeTaxComponentId = null;
          //  self.model.isEdit = false;
        });
      }
    };

    //Method to clear the fileds
    function ClearFields() {
      self.model.code = "";
      self.model.description = "";
      self.model.taxPercentage = 0;
      self.model.taxDetailList = [
        { component: self.model.taxList[0], taxPercent: null }
      ];
      self.model.taxId = undefined;
      self.model.isOk = false;
      self.model.pagination.search = '';

    }

    var clearSearch = function() {
      self.model.pagination.search = '';
      PaginationService.searchData(self.model.taxMasterList, self.model.pagination);
    };

    var ValidateTaxComponent = function() {
      var isValid = true;
      if (!self.model.code) {
        isValid = false;
        self.model.istaxComponentCodeValid = true;
      }
      if (!self.model.component) {
        isValid = false;
        self.model.isdescriptionValid = true;
      }
      return isValid;
    };

    var checkEmptyTaxDetails = function() {
      var isValid = true;
      self.model.taxDetailList.forEach(function(element, key) {
        if (!element.component.id) {
          isValid = false;
          self.model.istaxComponentValid[key] = true;
          growl.error("Tax Component is required");
        }
        if (
          (!element.taxPercent && element.taxPercent < 0) ||
          element.taxPercent == null
        ) {
          self.model.istaxPerValid[key] = true;
          isValid = false;
          growl.error("Tax % is required");
        }
      });
      return isValid;
    };

    var isAbleSave = function() {
      var isValid = true;
      var taxPercent =
        self.model.taxPercentage == undefined ||
        self.model.taxPercentage == null;
      if (!self.model.code || !self.model.description || taxPercent) {
        isValid = true;
      } else {
        isValid = false;
      }

      return isValid;
    };

    var SetDecimal = function(item){
      if (isNaN(item.taxPercent) || !item.taxPercent || item.taxPercent == 0) {
        item.taxPercent = 0;
      } else {
        item.taxPercent = parseFloat(item.taxPercent).toFixed(2);
      }
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("taxMaster", {
      url: "/taxMaster",
      templateUrl: "views/master/GlobalMaster/tax-master.html",
      controller: "TaxMaster.Controller",
      title: 'Tax Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 474);
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
    .controller("TaxMaster.Controller", taxMasterController);
})();
