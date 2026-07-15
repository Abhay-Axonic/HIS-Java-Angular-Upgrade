(function () {
  "use strict";

  function genericMasterController($scope, $rootScope,$state, $http, $filter, $cookies, CONSTANTS, GenericService, PaginationService, growl) {
    var self = this;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        assetTypeList: [{
          "type": "Select Asset Type"
        }],
        assetCategoryList: [{
          "category": "Select Asset Category"
        }],
        productCategoryList: [{
          "category": "Select Product Category"
        }],
        genericMasterList: [{
            "name": "Select Generic"
        }],
        strengthUnitList: [{
          "unit": "Select Unit"
        }],
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '10',
          search: '',
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
        isCombination: false,
        genericIsCombinationList: [],
        isGenericValid: [],
        isStrengthValid: [],
        genericCombinationList: []
      };

      //Methods

      // self.model.PopulateGenericName = PopulateGenericName;
      self.model.ToggleStatus = ToggleStatus;
      self.model.SaveGeneric = SaveGeneric;
      self.model.PopulateAssetTypeDropdown = PopulateAssetTypeDropdown;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategoryDropdown = PopulateProductCategoryDropdown;
      self.model.ClearFields = ClearFields;
      self.model.GetGenericData = GetGenericData;
      self.model.ValidateGenericMaster = ValidateGenericMaster;
      self.model.clearSearch = clearSearch;
      self.model.ChangeCombination = ChangeCombination;
      self.model.GetGenericMaster = GetGenericMaster;
      self.model.AddCombination = AddCombination;
      self.model.RemoveCombination = RemoveCombination;
      self.model.SetGeneric = SetGeneric;
      self.model.SetDecimal = SetDecimal;
      self.model.SaveGenericDetails = SaveGenericDetails;
      self.model.CloseCombination = CloseCombination;
      self.model.OpenCombination = OpenCombination;
      self.model.ViewCombinationDetails = ViewCombinationDetails;
      self.model.getPrefix =getPrefix;
      self.model.searchGeneric = searchGeneric;
      self.model.previousGenericCode= null; 
      PopulateAssetTypeDropdown();
      PopulateAssetCategoryDropdown(1,"");
  	  getPrefix();
    }
    

    //Method to get the list of Generic 
    var GetGenericList = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API + $scope.organizationId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.genericList = response.data;

          if(!!response && !!response.data && response.data.length > 0){            
              searchGeneric(self.model.genericList, self.model.pagination);
          }
          GetGenericMaster();
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }
    $scope.codeDisableFlag=true;
    var getPrefix = function()
    {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_PREFIX_API +$scope.organizationId+"/"+178;

        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            
            if(!!response && !!response.data ){            
            	self.model.genericCode= response.data.code; 
            	self.model.previousGenericCode= response.data.code; 
            }
            
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      
    	
    }
    var GetStrengthUnitList = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.strengthUnitList = response.data;

/*
            if(!!response && !!response.data && response.data.length > 0){
              PaginationService.searchData(self.model.strengthUnitList, self.model.pagination);
            }
*/
            console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            console.log("Error");
            console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          });
    }

    //Method to get the list of ProductCategory
    var PopulateProductCategoryDropdown = function (assetCategory, selectedValue) {
      
      var assetcategoryId = 0;
      if(!!assetCategory.assetCategoryId){
        assetcategoryId = assetCategory.assetCategoryId
      } 
      else if (!!assetCategory.id){
        assetcategoryId = assetCategory.id
      }
      // if (assetCategory.id == undefined) {
      //   assetcategoryId = 0;
      // }
      // else {
      //   assetcategoryId = assetCategory.id;
      // }
      var genericMaster = self.model;
      genericMaster.productCategory = genericMaster.productCategoryList[0];

      genericMaster.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + assetcategoryId + '/'+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          genericMaster.productCategoryList = genericMaster.productCategoryList.concat(response.data);
          
          // GetAssetCategories();

          if (selectedValue) {
            
            var productCategory = genericMaster.productCategoryList.find(function (type) {
              return assetCategory.productCategoryId == type.id;
            });
            genericMaster.productCategory = productCategory;
          }
        },
        function (err) {
          $rootScope.stopLoader();
        });
        $rootScope.stopLoader();
    }

    var PopulateAssetTypeDropdown = function () {
      var genericMaster = self.model;
      genericMaster.assetCategory = genericMaster.assetCategoryList[0];
      genericMaster.productCategory = genericMaster.productCategoryList[0];

      genericMaster.assetTypeList.splice(1);
      self.model.assetCategoryList.splice(1);
      self.model.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId ;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          genericMaster.assetTypeList = genericMaster.assetTypeList.concat(response.data);
          genericMaster.assetType = genericMaster.assetTypeList[1];
          GetGenericList();
          GetStrengthUnitList();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });

    }

    //Method to Populate Asset Category Dropdown
    var PopulateAssetCategoryDropdown = function (assetType, selectedValue) {
      var assettypeId = 0;
      if(!!assetType.assetTypeId){
        assettypeId = assetType.assetTypeId
      } 
      else if (!!assetType.id){
        assettypeId = assetType.id
      }
      // if (assetType.id == undefined) {
      //   assettypeId = 0;
      // }
      // else {
      //   assettypeId = assetType.id;
      // }

      if(assettypeId == 0) {
        assettypeId = 1;
      }

      var genericMaster = self.model;
      genericMaster.assetCategory = self.model.assetCategoryList[0];
      genericMaster.assetCategoryList.splice(1);
      self.model.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + assettypeId +"/"+$scope.unitId + "/"+$scope.organizationId;;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          genericMaster.assetCategoryList = genericMaster.assetCategoryList.concat(response.data);
          $rootScope.stopLoader();
          if (selectedValue) {
            var assetCategory = genericMaster.assetCategoryList.find(function (type) {
              return assetType.assetCategoryId == type.id;
            });
            genericMaster.assetCategory = assetCategory;
            PopulateProductCategoryDropdown(assetType, true);
          }
        },
        function (err) {

        });
    }


    //Method to populate ProductCategory for editing
    var GetGenericData = function (genericId) {
      self.model.genericIsCombinationList = [];
      self.model.isAssetTypeIdValid = false;
      self.model.isValidCode = false;
      self.model.isValidProductCat = false;
      self.model.isValidGenericCode = false;
      self.model.isValidGenericName = false;
      self.model.activeGenericId = genericId;
      $scope.codeDisableFlag=false;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId + "/" + genericId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          var assetType = self.model.assetTypeList.find(function (type) {
            return response.data.assetTypeId == type.id;
          });
          if(response.data.items.length > 0) {
        	
            for(let combination of response.data.items) {
              var genericCombination = self.model.genericMasterList.find(function (generic) {
                return combination.subGenericId == generic.id;
              });
              var strength = self.model.strengthUnitList.find(function (data){
                return combination.strength == data.id;
              });
              self.model.genericIsCombinationList.push({
                "id": combination.id,
                "subGenericId": combination.subGenericId,
                "name": genericCombination,
                "strengthUnit": combination.strengthUnit,
                "strengthUnitName": strength
              });
            }
          
            var popup = angular.element("#iscombination");
            popup.modal("show");
          }
          
          self.model.assetType = assetType;
          PopulateAssetCategoryDropdown(response.data, true);
          self.model.genericId = response.data.id;
          self.model.genericCode = response.data.code;
          self.model.genericName = response.data.name;
          self.model.isCombination = response.data.items.length > 0 ? true : false;
          $rootScope.stopLoader();
        });
    };

    var ToggleStatus = function (generic) {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API + generic.id;

      var data = {
        "status": generic.status,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "storeId":$scope.storeId
      };
      if (generic.status == false) {
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
            generic.status = true;
          }else{
         
          growl.error('Something Went Wrong');
        }
        });
        $rootScope.stopLoader();
    }

    var ValidateCode = function () {
      var isValid = true;

      self.model.genericList.forEach(function (element) {
        if (element.code == self.model.genericCode) {
          growl.error('Duplicate Generic code');
          isValid = false;
        }
      });
    /*  if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.genericCode)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }*/
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.genericList.forEach(function (element) {
        if(element.id != self.model.activeGenericId){
          if (element.code == self.model.genericCode) {
            growl.error('Duplicate Generic code');
            isValid = false;
          }
        }
      });
     /* if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.genericCode)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }*/
      return isValid;
    }

    //Method to add the Generic
    var CreateGeneric = function () {
      var list1= [];
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API;
      for(let dt of self.model.genericIsCombinationList){
        list1.push({
          id: dt.null,
          strengthUnit: dt.strengthUnit,
          subGenericId: dt.subGenericId,
          strength: dt.strengthUnitName.id
        })
      }
      var data = {
        "productCategoryId": self.model.productCategory.id,
        "code": self.model.genericCode,
        "name": self.model.genericName,
        "status": true,
        "isCombination": self.model.isCombination,
        "items": list1
      }

      var assetTypeData = self.model.assetTypeList.find(function (type) {
        return type.id == self.model.assetType.id;
      });
      var assetCategoryData = self.model.assetCategoryList.find(function (type) {
        return type.id == self.model.assetCategory.id;
      });
      var productCategoryData = self.model.productCategoryList.find(function (type) {
        return type.id == self.model.productCategory.id;
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var genericData = {
            "id": response.data.id,
            "assetType": assetTypeData.type,
            "assetCategory": assetCategoryData.category,
            "productCategory": productCategoryData.category,
            "code": self.model.genericCode,
            "name": self.model.genericName,
            "status": true
          };
          self.model.genericList.push(genericData);
          searchGeneric(self.model.genericList, self.model.pagination);
          PaginationService.setLastPage(self.model.pagination);
          GetGenericList();
          ClearFields();
          self.model.genericIsCombinationList = [];
          $rootScope.stopLoader();
          growl.success('Record saved successfully');
          getPrefix();
          $scope.codeDisableFlag=true;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Something went wrong');
        });
      }
    };

    //Method to add or update the Generic
    var SaveGeneric = function () {
      if (ValidateGenericMaster()) {
        if (!self.model.genericId) {
          CreateGeneric();
        } else {
          UpdateGeneric();
        }
      }
    };
    //Method to update the  Generic
    var UpdateGeneric = function () {
      var list1= [];
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API + self.model.genericId;
      for(let dt of self.model.genericIsCombinationList){
        list1.push({
          id: dt.id,
          strengthUnit: dt.strengthUnit,
          subGenericId: dt.subGenericId,
          strength: dt.strengthUnitName.id
        })
      }
      var data = {
        "productCategoryId": self.model.productCategory.id,
        "code": self.model.genericCode,
        "name": self.model.genericName,
        "status": true,
        "isCombination": self.model.isCombination,
        "items": list1,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "storeId":$scope.storeId
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          growl.success(response.data.message);  
          ClearFields();        
          self.model.genericIsCombinationList = [];
          GetGenericList();
          //getPrefix();
          
          if(self.model.previousGenericCode!=undefined)
        	  {
        	   self.model.genericCode = self.model.previousGenericCode;
        	  }
          else{
        	  getPrefix();
          }
          $scope.codeDisableFlag=true;
          //$state.reload("genericMaster");
        });
      }
    };



    //Method to clear the fileds
    function ClearFields() {
      self.model.assetType = self.model.assetTypeList[0];
      self.model.assetCategory = [self.model.assetCategoryList[0]];
      self.model.assetCategory = self.model.assetCategoryList[0];
      self.model.productCategory = [self.model.productCategoryList[0]];
      self.model.productCategory = self.model.productCategoryList[0];
//      self.model.genericCode = "";
      self.model.genericName = "";
      self.model.genericId = undefined;
      self.model.assetCategoryList.splice(1);
      self.model.productCategoryList.splice(1);
      self.model.isCombination = false;
    }

    var clearSearch = function(){
      self.model.pagination.search = "";
      searchGeneric(self.model.genericList, self.model.pagination);
    }

    // Methode to Search the Generic
        var searchGeneric = function (genericList, pagination) {
                var data = genericList;
                var searchQuery = pagination.search.toLowerCase();

                if (searchQuery) {
                    pagination.filteredData = $filter('filter')(data, function (item) {
                        return (
        					(item.assetType != undefined ? item.assetType.toLowerCase().includes(searchQuery) : "")||
                            (item.assetCategory != undefined ? item.assetCategory.toLowerCase().includes(searchQuery) : "") ||
        					(item.code != undefined ? item.code.toLowerCase().includes(searchQuery) : "") ||
        					(item.productCategory != undefined ?  item.productCategory.toLowerCase().includes(searchQuery) : "") ||
        					(item.name != undefined ? item.name.toLowerCase().includes(searchQuery) : "")
                        );
                    });
                } else {
                    pagination.filteredData = data;
                }

                setTotalPages(pagination);
            }


        var setTotalPages = function (pagination) {
                pagination.currentPage = 1;
                pagination.totalPages = Math.ceil(pagination.filteredData.length / parseInt(pagination.pageSize, 10));
               return setPagedData(pagination);
        }

        var setPagedData = function (pagination) {
              var limit = parseInt(pagination.pageSize, 10);
              var offset = (pagination.currentPage - 1) * limit;
              pagination.data = pagination.filteredData.slice(offset, offset + limit);
        }

    var ValidateGenericMaster = function () {
      var isValid = true;

      if (!self.model.assetType.id) {
        isValid = false;
        self.model.isAssetTypeIdValid = true;
      }
      if (!self.model.assetCategory.id) {
        isValid = false;
        self.model.isValidCode = true;
      }
      if (!self.model.productCategory.id) {
        isValid = false;
        self.model.isValidProductCat = true;
      }
      if (!self.model.genericCode) {
        isValid = false;
        self.model.isValidGenericCode = true;
      }
      if (!self.model.genericName) {
        isValid = false;
        self.model.isValidGenericName = true;
      }
      if(self.model.isCombination && self.model.genericIsCombinationList.length == 0) {
        isValid = false;
        growl.error('Add Generic Combination');
      }
      if(self.model.isCombination && self.model.genericIsCombinationList.length > 0) {
        self.model.genericIsCombinationList.forEach(function(element, key) {
          var validData = self.model.genericIsCombinationList.filter(x => x.subGenericId == element.subGenericId && x.strengthUnit == element.strengthUnit);
          if (!element.subGenericId) {
            isValid = false;
            self.model.isGenericValid[key] = true;
            growl.error("Generic is required");
          } else if ((!element.strengthUnit && element.strengthUnit < 1) || element.strengthUnit == null) {
            self.model.isStrengthValid[key] = true;
            isValid = false;
            growl.error("Strength Unit is required");
          } else if (validData && validData.length > 1) {
            isValid = false;
            self.model.isGenericValid[key] = true;
            self.model.isStrengthValid[key] = true;
            growl.error("Duplicate Generic Combination");
          }
        });
      }
      return isValid;
    }

    function ChangeCombination(isCombination) {
      self.model.genericIsCombinationList = [];
      if (isCombination){
        var genericMaster = self.model; 
        self.model.genericIsCombinationList.push({
          "id": null,
          "subGenericId": null,
          "name": genericMaster.genericMasterList[0],
          "strengthUnit": 0
        })     
        var popup = angular.element("#iscombination");
        popup.modal("show");
      } else{
        var popup = angular.element("#iscombination");
        popup.modal("hide");
      }     
    }

    var GetGenericMaster = function () {
        var arraylist = self.model.genericList.filter(x => x.status == true);
        for (let generic of arraylist){
          self.model.genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
    }

    var AddCombination = function() {
      var genericMaster = self.model; 
      self.model.genericIsCombinationList.push({
        "id": null,
        "subGenericId": null,
        "name": genericMaster.genericMasterList[0],
        "strengthUnitName":genericMaster.strengthUnitList,
        "strengthUnit": 0
      })
    };

    var RemoveCombination = function(index) {
      self.model.genericIsCombinationList.splice(index, 1);
    };

    var SaveGenericDetails = function() {
      if(ValidateCombination()){
        var popup = angular.element("#iscombination");
        popup.modal("hide");
      }
    }

    var ValidateCombination = function() {
      var isValid = true;
      self.model.genericIsCombinationList.forEach(function(element, key) {
        var validData = self.model.genericIsCombinationList.filter(x => x.subGenericId == element.subGenericId && x.strengthUnit == element.strengthUnit);
        if (!element.subGenericId) {
          isValid = false;
          self.model.isGenericValid[key] = true;
          growl.error("Generic is required");
        } else if ((!element.strengthUnit && element.strengthUnit < 1) || element.strengthUnit == null) {
          self.model.isStrengthValid[key] = true;
          isValid = false;
          growl.error("Strength Unit is required");
        } else if (validData && validData.length > 1) {
          isValid = false;
          self.model.isGenericValid[key] = true;
          self.model.isStrengthValid[key] = true;
          growl.error("Duplicate Generic Combination");
        }
        return isValid;
      });
      return isValid;
    }

    var SetGeneric = function(generics) {
      generics.subGenericId = generics.name.id;
    }

    var SetDecimal = function(generic){
      if (isNaN(generic.strengthUnit) || !generic.strengthUnit || generic.strengthUnit == 0) {
        generic.strengthUnit = 0;
      } else {
        generic.strengthUnit = parseFloat(generic.strengthUnit).toFixed(2);
      }
    }

    var CloseCombination = function() {
      var popup = angular.element("#confirmClose");
      popup.modal("hide");
      var popups = angular.element("#iscombination");
      popups.modal("hide");
      self.model.genericIsCombinationList = [];
    }

    var OpenCombination = function() {
      var popup = angular.element("#confirmClose");
      popup.modal("show");
    }

    var ViewCombinationDetails = function(generic) {
      self.model.genericCombinationList = [];
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + generic.id;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          for(let combination of response.data) {
            let combinationName = self.model.genericMasterList.filter(x => x.id == combination.subGenericId);
            combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
            combination.code = generic.code;
            combination.genericName = generic.name;
          }
          self.model.strengthUnitList.forEach(function (strength){
            response.data.forEach(function (data){
              if(data.strength == strength.id){
                $scope.strength = strength.unit;
                var obj = {
                  strength: "",
                  code:"",
                  genericId: "",
                  genericName: "",
                  id:"",
                  name:"",
                  strengthUnit:"",
                  subGenericId: "",
                }
                obj.strength = $scope.strength;
                obj.code = data.code;
                obj.genericId = data.genericId;
                obj.genericName = data.genericName;
                obj.id = data.id;
                obj.name = data.name;
                obj.strengthUnit = data.strengthUnit;
                obj.subGenericId = data.subGenericId;
                self.model.genericCombinationList.push(obj);
              }
            });
          });
         
          var popup = angular.element("#combinationDetails");
          popup.modal("show");
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("genericMaster", {
      url: "/genericMaster",
      templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/generic-master.html',
      controller: "GenericMaster.Controller",
      title: 'Generic Master',
   	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 463);
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
    .controller("GenericMaster.Controller", genericMasterController);
})();