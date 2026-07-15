(function () {
  "use strict";

  function formulationTypeROAMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl,promiseFactory,PagerService) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {

        formulationTypes: [
          {
            type: "Select Formulation Type"
          }
        ],
        roaDropdownList: [
          {
            description: "Select Route Of Administration"
          }
        ],
        roaTypeList: [],
        formROADetailList: [],
        pager : {},
        page:0,
        offset : 0,
        noOfRecordsPerPage :10,
        totalNoOfRecords :0,

      };
      self.model.isEdit = false;
      self.model.formROADetailList = [
        { formRoa: self.model.roaDropdownList[0] }
      ];
      
      self.model.offset = 0;
      self.model.noOfRecordsPerPage = 10;
      self.model.totalNoOfRecords;
      //self.model.roaSearch = roaSearch;
  	  self.model.roaList = [];
      self.model.PopulateFormulationType = PopulateFormulationType;
      self.model.PopulateROA = PopulateROA;
      self.model.onAddButtonClick = onAddButtonClick;
      self.model.commonListCount;
      self.model.AddDuplicateRow = AddDuplicateRow;
      self.model.removeRowData = removeRowData;
      self.model.onAddClick = onAddClick;
      self.model.SaveFormulationROA = SaveFormulationROA;
      self.model.getDetail = getDetail;
      self.model.ClearFields = ClearFields;
      self.model.UpdateFormulationROA = UpdateFormulationROA;
      self.model.ToggleStatus = ToggleStatus;
      self.model.ValidateRoa = ValidateRoa;
      self.model.setPage = setPage;
      self.model.setNoOfRecords = setNoOfRecords;
      initMasterList(self.model.offset, self.model.noOfRecordsPerPage);
      //roaSearch();
      PopulateFormulationType();
      PopulateROA();
    }



    //Method to get the list of Generic
/*    var roaSearch = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FORMULATION_ROA_API;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.roaList = response.data;
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };*/
    //Method to Populate Formulation Type Dropdown
    var PopulateFormulationType = function () {
      var roaMaster = self.model;

      roaMaster.formulationType = roaMaster.formulationTypes[0];

      roaMaster.formulationTypes.splice(1);

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.FORMULATION_TYPE_API + $scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          roaMaster.formulationTypes = roaMaster.formulationTypes.concat(
            response.data
          );
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    var PopulateROA = function () {
      var roaMaster = self.model;
      roaMaster.roa = roaMaster.roaDropdownList[0];
      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FORMULATION_ROA_DROPDOWN_API + $scope.organizationId;
      roaMaster.roaDropdownList.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          roaMaster.roaDropdownList = roaMaster.roaDropdownList.concat(
            response.data
          );
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    var onAddButtonClick = function () {
      $("#addFormulationRoa").modal();
    }

    var AddDuplicateRow = function (item) {
      var data = angular.copy(item);
      data.formRoa = self.model.roaDropdownList[0];
      self.model.formROADetailList.push(data);
    }

    function removeRowData(item) {
      var index = self.model.formROADetailList.indexOf(item);
      self.model.formROADetailList.splice(index, 1);
    }

    var onAddClick = function () {
      if (validatePopup()) {
        if (self.model.isEdit == false) {
          self.model.newRoa = [];
          self.model.formROADetailList.forEach(function (value) {
            self.model.newRoa.push(value.formRoa.id);
          });
        }
        else {
          self.model.newRoa = self.model.formROADetailList[0].formRoa.id;
        }
        $('#addFormulationRoa').modal('hide');
      }
    }



    var SaveFormulationROA = function () {
      if (validatePopup()) {
        if (!self.model.id) {
          SaveNewFormulationROA();
        } else {
          UpdateFormulationROA();
        }
      }
    }
    var SaveNewFormulationROA = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FORMULATION_ROA_SAVE_API;

      var data = {
        formulationtypeid: self.model.formulationType.id,
        routeOfAdministrationId: self.model.newRoa,
        status: true,
        unitId:$scope.unitId,
        organizationId:$scope.organizationId,
        userId:$scope.userId
      };

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          ClearFields();
          //roaSearch();
          initMasterList(self.model.offset, self.model.noOfRecordsPerPage);
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error("Something went wrong");
        }
      );
    }

    function ClearFields() {
      self.model.formulationType = self.model.formulationTypes[0];
      self.model.roaTypeList = [];
      self.model.id = undefined;
      self.model.isEdit = false;
      self.model.formROADetailList = [
        { formRoa: self.model.roaDropdownList[0] }
      ];
      self.model.search = '';
    }

    var getDetail = function (formROAId) {
      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FORMULATION_ROA_API +$scope.organizationId + '/' + formROAId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function (response) {
        self.model.id = response.data.id;
        self.model.formulationType = self.model.formulationTypes.find(function (item) {
          return response.data.formulationTypeID == item.id;
        });
        self.model.formROADetailList[0].formRoa = self.model.roaDropdownList.find(function (item) {
          return response.data.routeOfAdministrationID == item.id;
        });
        self.model.isEdit = true;
        $rootScope.stopLoader();
      });
    }

    var UpdateFormulationROA = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FORMULATION_ROA_API + self.model.id;

      var data = {
        "formulationtypeid": self.model.formulationType.id,
        "routeOfAdministrationId": self.model.newRoa,
        "status": "true"
      };

      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          ClearFields();
         // roaSearch();
          initMasterList(self.model.offset, self.model.noOfRecordsPerPage);
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }
    var ToggleStatus = function (formRoa) {

      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FORMULATION_ROA_API + formRoa.id;

      var data = {
        "status": formRoa.status
      };

      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var ValidateRoa = function (item) {
      var isValid = true;
      if (!item.formRoa.id) {
        item.isRoaValid = true;
        isValid = false;
        growl.error("Please Select Roa");
      }
      return isValid;
    }
    var validatePopup = function () {
      var isValid  = true;
      self.model.formROADetailList.forEach(function (item) {
        if (!ValidateRoa(item)) {
          isValid = false;
        }
      });
      return isValid;
    }
    
    
    var setNoOfRecords = function() {
		initMasterList(self.model.offset, self.model.noOfRecordsPerPage);
	};
	
	
	var initMasterList = function(offset, noOfRecordsPerPage)
	{
		$rootScope.startLoader();
		
		self.model.offset = offset != null ? offset : 0;
		self.model.noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
		var URI1 = BASE_URL + ROOT_URL + "/api/globals/formulationTypeAndROA/"+$scope.organizationId;
		var URI2 = BASE_URL + ROOT_URL + "/api/globals/formulationTypeAndROA/getCount"+"/"+$scope.organizationId;
		
		promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
				{
					$rootScope.stopLoader();
					self.model.roaList = [];
					self.model.roaList = response[0];
					self.model.commonListCount = response[1];
					setPage(1, false);
				});
	}		
	
	var masterList = function(offset, noOfRecordsPerPage) {
		$rootScope.startLoader();
		self.model.offset = offset != null ? offset : 0;
		self.model.noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;

		var URI = BASE_URL + ROOT_URL + "/api/globals/formulationTypeAndROA/get/formulationTypeAndROA/"+$scope.organizationId+"/"+self.model.offset+"/"+self.model.noOfRecordsPerPage;
		
		console.log("URI", URI);
		GenericService.serviceAction("GET", URI).then(function(response) {
			$rootScope.stopLoader();
			self.model.roaList = [];
			self.model.roaList= response.data;
		});
	}

	

	var setPage = function(page, flag) {
		if (page < 1 || page > self.model.pager.totalPages)
		{
			return;
		}
		self.model.pager = PagerService.GetPager(self.model.commonListCount, page, self.model.noOfRecordsPerPage);
		if (flag)
		{
			masterList(self.model.pager.startIndex, self.model.pager.pageSize);
		}
	}

	

	
	
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("formulationTypeROAMaster", {
      url: "/formulationTypeROAMaster",
      templateUrl: "views/master/GlobalMaster/AssetManagenent/Master/formulation-type-ROA-master.html",
      controller: "FormulationTypeROAMaster.Controller",
      title: 'Formulation Type And ROA Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 479);
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
    .controller("FormulationTypeROAMaster.Controller", formulationTypeROAMasterController);
})();