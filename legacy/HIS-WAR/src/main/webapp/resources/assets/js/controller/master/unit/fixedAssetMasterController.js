
(function () {
    "use strict";
    function fixedAssetMasterController($scope, $rootScope, $http, $filter, $cookies, CONSTANTS, GenericService, growl,PaginationService, commonDetailService) {
      var self = this;

      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
      function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
          itemDetails: commonDetailService.getDataId(),
          manufacturer :  commonDetailService.getManufacturerName(),
          manufactureId :  commonDetailService.getManufacturerId(),
          itemRequestId: commonDetailService.getItemRequestId(),
          itemId: 0,
          assetTypeList: [{
            "type": "Select Asset Type"
          }],
          assetCategoryList: [{
            "category": "Select Asset Category Type"
          }],
          productCategoryList: [{
            "category": "Select Product Category"
          }],
          fixedAssetTypeList: [{
            "type": "Select Fixed Asset Type"
          }],
          manufacturerSearch: '',
          prevSearch: '',
          mPrevSearch: '',
        //  manufacturerNames: []
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
        };
        self.model.SearchManufacturer = SearchManufacturer;
        self.model.AddManufacturer = AddManufacturer;
        self.model.SaveFixedAsset = SaveFixedAsset;
        self.model.CreateUpdateFixedAssetType = CreateFixedAssetType;
        self.model.UpdateFixedAssetType = UpdateFixedAssetType;
        self.model.PopulateAssetTypeDropdown = PopulateAssetTypeDropdown;
        self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
        self.model.PopulateProductCategoryDropdown = PopulateProductCategoryDropdown;
        self.model.getFixedAssetComponent = getFixedAssetComponent;
        self.model.getFixedAssetComponent1 = getFixedAssetComponent1;
        self.model.GetFixedAssetMasterData = GetFixedAssetMasterData;
        self.model.ToggleStatus = ToggleStatus;
        self.model.ValidateFixedAssetMaster = ValidateFixedAssetMaster;
        self.model.ClearFields = ClearFields;
        self.model.clearSearch = clearSearch;
        getFixedAssetComponent();
        
      }

     //Method to get fixed asset type
    var getFixedAssetComponent = function () {
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER;
      PopulateAssetTypeDropdown();
    //  PopulateAssetCategoryDropdown(2, true);
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.itemId = self.model.itemDetails.id;
          self.model.assetName = self.model.itemDetails.itemName;
          if(self.model.assetName){
            self.model.assetType = self.model.assetType[1];
            PopulateAssetCategoryDropdown(self.model.assetType, undefined);
          }
          self.model.fixedAssetList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.fixedAssetList, self.model.pagination);
          }
          //self.model.manufacturer =  commonDetailService.getManufacturer();
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });

    }

    var getFixedAssetComponent1 = function () {
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER;
      PopulateAssetTypeDropdown();
    //  PopulateAssetCategoryDropdown(2, true);
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.fixedAssetList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.fixedAssetList, self.model.pagination);
          }
          //self.model.manufacturer =  commonDetailService.getManufacturer();
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });

    }

    
    //Method to populate Fixed Asset Master for editing
    var GetFixedAssetMasterData = function (fixedAssetId) {
      self.model.activeFixedAsset = fixedAssetId;
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER + fixedAssetId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          var assetType = self.model.assetTypeList.find(function (type) {

            return response.data.assetTypeId == type.id;
            
          });
          self.model.assetType = assetType;
          PopulateAssetCategoryDropdown(response.data, true);
          self.model.fixedAssetId = response.data.id;
          self.model.itemCode = response.data.itemCode;
          self.model.productCode = response.data.productCode;
          self.model.assetName = response.data.name;
          self.model.manufacturer = response.data.manufactureName;
          self.model.manufactureId = response.data.manufactureId;
          self.model.isCSSD = response.data.isCssd;
          self.model.isModality = response.data.isModality;
          self.model.isChargeableEquipment = response.data.isChargeableEqupiment;
          self.model.isAccessories = response.data.isAccessories;
          self.model.isSpares = response.data.isSpares;
          self.model.isDialyser=response.data.isDialyser;
          self.model.isCritical=response.data.isCritical;
          $rootScope.stopLoader();
        });
    };

       //Method to get the list of ProductCategory
    var PopulateProductCategoryDropdown = function (assetCategory, selectedValue) {
      var assetcategory = 0;
      if(!!assetCategory.assetCategoryId){
        assetcategory = assetCategory.assetCategoryId
      } 
      else if (!!assetCategory.id){
        assetcategory = assetCategory.id
      }
      var fixedAssetMaster = self.model;
      fixedAssetMaster.productCategory = fixedAssetMaster.productCategoryList[0];

      fixedAssetMaster.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + assetcategory + '/'+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          fixedAssetMaster.productCategoryList = fixedAssetMaster.productCategoryList.concat(response.data);
          $rootScope.stopLoader();
          // GetAssetCategories();

          if (selectedValue) {
            var productCategory = fixedAssetMaster.productCategoryList.find(function (type) {
              return assetCategory.productCategoryId == type.id;
            });
            fixedAssetMaster.productCategory = productCategory;
          }
        },
        function (err) {

        });
          //  self.model.itemCode = self.model.itemDetails.itemCode;
    }

    var PopulateAssetTypeDropdown = function () {
      var fixedAssetMaster = self.model;
      fixedAssetMaster.assetType = fixedAssetMaster.assetTypeList[0];
      fixedAssetMaster.assetCategory = fixedAssetMaster.assetCategoryList[0];
      fixedAssetMaster.productCategory = fixedAssetMaster.productCategoryList[0];

      fixedAssetMaster.assetTypeList.splice(1);
      self.model.assetCategoryList.splice(1);
      self.model.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId ;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
         
        	angular.forEach(response.data,function(value,index){
        		   if (value.id == 2) {
        			   		fixedAssetMaster.assetTypeList.push(value); 
        	            }
        	});
          self.model.assetType = fixedAssetMaster.assetTypeList;
        
          $rootScope.stopLoader();
         
           PopulateAssetCategoryDropdown(self.model.assetType, undefined);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });

    }

    //Method to Populate Asset Category Dropdown
    var PopulateAssetCategoryDropdown = function (assetType, selectedValue) {
      var assettypeId = 0;
      if (!!assetType.assetTypeId) {
        assettypeId = assetType.assetTypeId;
      } else if (!!assetType.id) {
        assettypeId = assetType.id;
      }
      var fixedAssetMaster = self.model;
      fixedAssetMaster.assetCategory = self.model.assetCategoryList[0];
      fixedAssetMaster.productCategory = fixedAssetMaster.productCategoryList[0];
      fixedAssetMaster.assetCategoryList.splice(1);
      self.model.productCategoryList.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + assettypeId +"/"+$scope.unitId + "/"+$scope.organizationId;;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          fixedAssetMaster.assetCategoryList = fixedAssetMaster.assetCategoryList.concat(response.data);
          $rootScope.stopLoader();
          if (selectedValue) {
            var assetCategory = fixedAssetMaster.assetCategoryList.find(function (type) {
              return assetType.assetCategoryId == type.id;
            });
            fixedAssetMaster.assetCategory = assetCategory;
            PopulateProductCategoryDropdown(assetType, true);
          }
        },
        function (err) {

        });
    }

    var SearchManufacturer = function(search) {
      if (search.length < 1) return;
      if ((!self.model.mPrevSearch || self.model.mPrevSearch != search) && (search.length == 1)) {
        self.model.mPrevSearch = search;
        var URI = CONSTANTS.GLOBAL.MANUFACTURER_SEARCH_API +$scope.organizationId + "/" + search;

        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
          if (!!response.data && response.data.length > 0) {
            self.model.manufacturerNames = response.data;
            $rootScope.stopLoader();
            return $filter('filter')(self.model.manufacturerNames, {
              $: search
            });
          } else {
            $rootScope.stopLoader();
            return manufacturerNotFound(search);
          }
        }, function (err) {
          $rootScope.stopLoader();
          return manufacturerNotFound(search);
        });
      } else {
        if (!!self.model.manufacturerNames && self.model.manufacturerNames.length > 0 && self.model.manufacturerNames[0].manufacturerFound != undefined && !self.model.manufacturerNames[0].manufacturerFound) {
          return manufacturerNotFound(search);
        } else {
          return $filter('filter')(self.model.manufacturerNames, {
            $: search
          });
        }
      }
    }

    var manufacturerNotFound = function (search) {
      var item = {
        "manufacturerFound": false,
        "detail": "Manufacturer Not Found with Name " + search,
        "search": search
      };
      self.model.manufacturerNames = [item];
      return self.model.manufacturerNames;
    }

    var AddManufacturer = function (manufacture) {
      self.model.manufacturer = manufacture.manufacturerName;
      self.model.manufactureId = manufacture.id;
    }

   //Method to add or update
      var SaveFixedAsset = function () {
        if (ValidateFixedAssetMaster()) {
        if(!self.model.fixedAssetId) {
          CreateFixedAssetType();
        } else{
          UpdateFixedAssetType();
        }
      }
    };

    var ValidateItemCode = function () {
      var isValid = true;

      self.model.fixedAssetList.forEach(function (element) {
        if (element.itemCode == self.model.itemCode) {
          growl.error('Duplicate Item code');
          isValid = false;
        }
      });
      
      return isValid;
    }

    var validateManufacture = function () {
      var isValid = true;
      if(!self.model.manufactureId){
        growl.warning("Please Select valid Manufacturer");
        self.model.manufacturer = "";
        isValid = false;
      }
      return isValid;
    }

    var ValidateItemCodeForUpdate = function () {
      var isValid = true;

      self.model.fixedAssetList.forEach(function (element) {
        if(element.id != self.model.activeFixedAsset){
          if (element.itemCode == self.model.itemCode) {
            growl.error('Duplicate Item code');
            isValid = false;
          }
        }
      });
      
      return isValid;
    }

    var ValidateProductCode = function () {
      var isValid = true;

      self.model.fixedAssetList.forEach(function (element) {
        if (element.productCode == self.model.productCode) {
          growl.error('Duplicate Product code');
          isValid = false;
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCode)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var ValidateProductCodeForUpdate = function () {
      var isValid = true;

      self.model.fixedAssetList.forEach(function (element) {
        if(element.id != self.model.activeFixedAsset){
          if (element.productCode == self.model.productCode) {
            growl.error('Duplicate Product code');
            isValid = false;
          }
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCode)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

      var CreateFixedAssetType = function () {
        if(ValidateItemCode() && ValidateProductCode() && validateManufacture()){
          
        var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER;
        
              var data = {
                "productCategoryId": self.model.productCategory.id,
                "manufactureId": self.model.manufactureId,
                "itemCode": self.model.itemCode,
                "productCode": self.model.productCode,
                "name": self.model.assetName,
                "status": true,
                "isCssd":self.model.isCSSD ? self.model.isCSSD : false,
                "isModality":self.model.isModality ? self.model.isModality : false,
                "isChargeableEqupiment": self.model.isChargeableEquipment ? self.model.isChargeableEquipment : false,
                "isAccessories": self.model.isAccessories ? self.model.isAccessories : false,
                "isSpares": self.model.isSpares ? self.model.isSpares : false,
                "isDialyser" : self.model.isDialyser ? self.model.isDialyser : false,	
                "isCritical" : self.model.isCritical ? self.model.isCritical : false,
                //"itemRequestId" : self.model.itemRequestId,
                "itemRequestId" : self.model.itemDetails.id,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
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
              // var fixedAssetTypeData = self.model.fixedAssetList.find(function (type) {
              //   return type.id == self.model.type.id;
              // });
        
              $rootScope.startLoader();
              GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                  var fixedAssetData = {
                    "id": response.data.id,
                    "assetType": assetTypeData.type,
                    "assetCategory": assetCategoryData.category,
                    "productCategory": productCategoryData.category,
                  //  "fixedAssetType": fixedAssetTypeData.type,
                    "itemCode": self.model.itemCode,
                    "productCode": self.model.productCode,
                    "name": self.model.assetName,
                    "manufactureId": self.model.manufactureId,
                    "manufactureName": self.model.manufacturer,
                    "status": true,
                    "isCssd":self.model.isCSSD,
                    "isModality":self.model.isModality,
                    "isChargeableEqupiment": self.model.isChargeableEquipment,
                    "isAccessories": self.model.isAccessories,
                    "isSpares": self.model.isSpares,
                    "isDialyser":self.model.isDialyser,
                    "isCritical":self.model.isCritical
                  };
                  self.model.fixedAssetList.push(fixedAssetData);
                  PaginationService.searchData(self.model.fixedAssetList, self.model.pagination);
                  PaginationService.setLastPage(self.model.pagination);
                  ClearFields();
                  getFixedAssetComponent1();
                  $rootScope.stopLoader();
                  growl.success(response.data.message);
                },
                function errorCallback(response) {
                  $rootScope.stopLoader();
                  growl.error('Something went wrong');
                });
                
        }
      };

      var UpdateFixedAssetType = function () {
        if(ValidateItemCodeForUpdate() && ValidateProductCodeForUpdate()){
        var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER + self.model.fixedAssetId;
        var data = {
                "productCategoryId": self.model.productCategory.id,
                "manufactureId": self.model.manufactureId,
                "itemCode": self.model.itemCode,
                "productCode": self.model.productCode,
                "name": self.model.assetName,
                "status": true,
                "isCssd":self.model.isCSSD ? self.model.isCSSD : false,
                "isModality":self.model.isModality ? self.model.isModality :  false,
                "isChargeableEqupiment": self.model.isChargeableEquipment ? self.model.isChargeableEquipment : false,
                "isAccessories": self.model.isAccessories ? self.model.isAccessories : false,
                "isSpares": self.model.isSpares ? self.model.isSpares : false,
                "isDialyser" : self.model.isDialyser ? self.model.isDialyser : false,
                "isCritical" : self.model.isCritical ? self.model.isCritical : false,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
        }
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            var fixedAssetData = self.model.fixedAssetList.find(function (type) {
              return type.id == self.model.fixedAssetId;
            });
            var assetTypeData = self.model.assetTypeList.find(function (type) {
              return type.id == self.model.assetType.id;
            });
            var assetCategoryData = self.model.assetCategoryList.find(function (type) {
              return type.id == self.model.assetCategory.id;
            });
            var productCategoryData = self.model.productCategoryList.find(function (type) {
              return type.id == self.model.productCategory.id;
            });
            var manufactureData = self.model.productCategoryList.find(function (type) {
              return type.id == self.model.productCategory.id;
            });
  
  
            fixedAssetData.itemCode = data.itemCode;
            fixedAssetData.productCode = data.productCode;
            fixedAssetData.name = data.name;
            fixedAssetData.manufactureId = self.model.manufactureId;
            fixedAssetData.manufactureName = self.model.manufacturer;
            fixedAssetData.category = productCategoryData.category;
            fixedAssetData.assetType = assetTypeData.type;
            fixedAssetData.assetCategory = assetCategoryData.category;
            ClearFields();
            getFixedAssetComponent();
            $rootScope.stopLoader();
            growl.success(response.data.message);
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error('Something Went Wrong');
          });
        }
      };

      var ToggleStatus = function (fixedAsset) {
        var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER + fixedAsset.id;
  
        var data = {
          "status": fixedAsset.status,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        };
        if (fixedAsset.status == false) {
          ClearFields();
        }
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success('Status changed successfully');
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Something went wrong');
          });
      }

    //Method to clear the fileds
    function ClearFields() {
      self.model.assetType = self.model.assetTypeList[0];
      self.model.assetCategory = [self.model.assetCategoryList[0]];
      self.model.assetCategory = self.model.assetCategoryList[0];
      self.model.productCategory = [self.model.productCategoryList[0]];
      self.model.productCategory = self.model.productCategoryList[0];
      self.model.itemCode = "";
      self.model.productCode = "";
      self.model.assetName = "";
      self.model.manufacturer ="";
      self.model.assetCategoryList.splice(1);
      self.model.productCategoryList.splice(1);
      self.model.search = '';
      self.model.isCSSD = false;
      self.model.isModality = false;
      self.model.isChargeableEquipment = false;
      self.model.isAccessories = false;
      self.model.isSpares = false;
      self.model.isDialyser=false;
      self.model.isCritical=false;
      self.model.fixedAssetId=undefined;
    }
   
    var clearSearch = function(){
      self.model.pagination.search = "";
      PaginationService.searchData(self.model.genericList, self.model.pagination);
    }

      var ValidateFixedAssetMaster = function () {
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
        // if (!self.model.type) {
        //   isValid = false;
        //   self.model.isValidFixedAssetType = true;
        // }
        if (!self.model.itemCode) {
          isValid = false;
          self.model.isValidItemCode = true;
        }
        if (!self.model.productCode) {
          isValid = false;
          self.model.isValidProductCode = true;
        }
        if (!self.model.assetName) {
          isValid = false;
          self.model.isValidAssetName = true;
        }
        if (!self.model.manufacturer) {
          isValid = false;
          self.model.isValidManufacturer = true;
        }
        return isValid;
      }
      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("fixedAssetMaster", {
        url: "/fixedAssetMaster",
        templateUrl: 'views/master/UnitMaster/assetManagement/fixed-asset-master.html',
        controller: "FixedAssetMaster.Controller",
        title: 'Fixed Asset Master',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 644);
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
      .controller("FixedAssetMaster.Controller", fixedAssetMasterController)
  })();