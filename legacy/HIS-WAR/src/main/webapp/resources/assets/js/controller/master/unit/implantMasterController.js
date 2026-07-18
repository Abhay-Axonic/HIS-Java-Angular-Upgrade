
(function () {
    "use strict";
    function implantMasterController($scope, $rootScope, $http, $filter, $cookies, $state, CONSTANTS, GenericService,PaginationService, growl, commonDetailService) {
        var self = this;

        //Default Constructor
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
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                },
                isSpecialityComponentValid: [],
                specialityDetailList: [],
                specialityList: [{
                    specialityName: "Select Speciality"
                }],
                itemDetails: commonDetailService.getDataId(),
                itemId: 0,

                assetCategoryList: [{
                    "category": "Select Asset Category Type"
                }],
                productCategoryList: [{
                    "category": "Select Product Category"
                }],
                StorageUnits: [{
                    unit: "Select Storage Unit"
                }],
                PurchaseTaxes: [{
                    type: "Select Tax"
                }],
                SaleTaxes: [{
                    type: "Select Tax"
                }],
                manufacturerSearch: '',
                prevSearch: '',
                mPrevSearch: '',
                manufacturerNames: [],
                isConsignment: false,
                isSterilized:false,
                isBatchRequired:false
            };
            self.model.SearchManufacturer = SearchManufacturer;
            self.model.AddManufacturer = AddManufacturer;
            self.model.SaveFixedAsset = SaveFixedAsset;
            self.model.updateStatusFixedAsset = updateStatusFixedAsset;
            self.model.CreateFixedAssetType = CreateFixedAssetType;
            self.model.UpdateFixedAssetType = UpdateFixedAssetType;
            self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
            self.model.PopulateProductCategoryDropdown = PopulateProductCategoryDropdown;
            self.model.getFixedAssetComponent = getFixedAssetComponent;
            self.model.GetImplantMasterData = GetImplantMasterData;
            self.model.ViewTax = ViewTax;
            self.model.ValidateImplantMaster = ValidateImplantMaster;
            self.model.ClearFields = ClearFields;
            self.model.SaveSpecialityDetails = SaveSpecialityDetails;
            self.model.getActiveSpecialityList = getActiveSpecialityList;
            self.model.AddSpeciality = AddSpeciality;
            self.model.LessSpeciality = LessSpeciality;
            self.model.PopulateAll = PopulateAll;
            self.model.isOk = false;

            getFixedAssetComponent();
            self.model.specialityDetailList = [
                { specialityName: self.model.specialityList[0] }
            ]
        }

        //Method to get fixed asset type
        var getFixedAssetComponent = function () {
            PopulateAssetCategoryDropdown();
            PopulateSpecialityMaster();
            PopulateAll();
              var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.GET_ALL + "/unitId" + "/" + $scope.unitId;
           // var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.itemId = self.model.itemDetails.id;
                    self.model.assetName = self.model.itemDetails.itemName;
                    self.model.implantMasterList = response.data;
                    
                    /* start code for resolved HIS-10158 Unable to save implant master */
                    if(self.model.implantMasterList == undefined){
                    	self.model.implantMasterList = [];
                    }
                    /* end code for resolved HIS-10158 Unable to save implant master */
                    
                    if (!!response && !!response.data && response.data.length > 0) {
                        PaginationService.searchData(self.model.implantMasterList, self.model.pagination);
                      }
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });

        }

        //Method to populate Fixed Asset Master for editing 

        var GetImplantMasterData = function (implantId,index) {
            self.model.activeImplant = implantId;
              var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.DETAILS_API+ implantId;
              $scope.UpdateDataIndex = index; 
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    var assetCategory = self.model.assetCategoryList.find(function (category) {
                        return response.data.assetCategoryId == category.id;
                    });
                    
                    var saleTax = self.model.SaleTaxes.find(function (saletax) {
                        return response.data.sTaxId == saletax.id;
                    });
                    
                    var purchaseTax = self.model.PurchaseTaxes.find(function (purchasetax) {
                        return response.data.pTaxId == purchasetax.id;
                    });
                   

                    var storageUnit = self.model.StorageUnits.find(function (sunit) {
                        return response.data.storageUnitId == sunit.id;
                    });

                    PopulateProductCategoryDropdown(response.data, true);
                    self.model.implantId = response.data.id;
                    self.model.assetCategory = assetCategory;
                    self.model.purchaseTax = purchaseTax;
                    self.model.saleTax = saleTax;
                    self.model.itemCode = response.data.itemCode;
                    self.model.productCode = response.data.productCode;
                    self.model.assetName = response.data.name;
                    self.model.manufacturer = response.data.manufactureName;
                    self.model.manufactureId = response.data.manufactureId;
                    self.model.unit = storageUnit;
                  //  self.model.speciality = response.data.speciality.length;
                    self.model.isConsignment = response.data.isConsignment;
                    self.model.isSterilized = response.data.isSterilized;
                    self.model.status = response.data.status;
                    self.model.isBatchRequired = response.data.isBatchRequired;

                    var listSpeciality = [];
                    angular.forEach(response.data.speciality, function (abc) {
                        var selectedSpeciality = self.model.specialityList.find(function (data) {
                            return abc.id == data.specialityId;
                        });
                        listSpeciality.push({
                            id: abc.id,
                            specialityName: selectedSpeciality,
                        });
                    });
                    self.model.specialityDetailList = listSpeciality;
                    $rootScope.stopLoader();
                });
        };

        //Method to Populate Asset Category Dropdown

        var PopulateAssetCategoryDropdown = function () {
            var fixedAssetMaster = self.model;
            fixedAssetMaster.assetCategory = fixedAssetMaster.assetCategoryList[0];
            fixedAssetMaster.productCategory = fixedAssetMaster.productCategoryList[0];

            self.model.assetCategoryList.splice(1);
            self.model.productCategoryList.splice(1);

            var assettypeId = 1;
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
                    // PopulateProductCategoryDropdown(assetType, true);
                },
                function (err) {
                });
        }

        //Method to get the list of ProductCategory
        var PopulateProductCategoryDropdown = function (assetCategory, selectedValue) {
            var assetcategory = 0;
            if (!!assetCategory.assetCategoryId) {
                assetcategory = assetCategory.assetCategoryId
            }
            else if (!!assetCategory.id) {
                assetcategory = assetCategory.id
            }
            if (selectedValue) {
                assetcategory = assetCategory.assetCategoryId;
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
        }

        var PopulateSpecialityMaster = function () {
            var fixedAssetMaster = self.model;
            var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
            fixedAssetMaster.specialityName = fixedAssetMaster.specialityList[0];
            fixedAssetMaster.specialityList.splice(1);
            var data = {
                "unitId": 1,
                "organizationId": 1
            }
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {

                    fixedAssetMaster.specialityList = fixedAssetMaster.specialityList.concat(response.data.listObject);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                }
            );
        };
        var PopulateAll = function () {
            var fixedAssetMaster = self.model;
            var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId;
            //var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.STORAGE_API;
            fixedAssetMaster.unit = fixedAssetMaster.StorageUnits[0];
            fixedAssetMaster.purchaseTax = fixedAssetMaster.PurchaseTaxes[0];
            fixedAssetMaster.saleTax = fixedAssetMaster.SaleTaxes[0];
            fixedAssetMaster.StorageUnits.splice(1);
            fixedAssetMaster.PurchaseTaxes.splice(1);
            fixedAssetMaster.SaleTaxes.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    
                    fixedAssetMaster.StorageUnits = fixedAssetMaster.StorageUnits.concat(response.data.storageUnit);
                    fixedAssetMaster.PurchaseTaxes = fixedAssetMaster.PurchaseTaxes.concat(response.data.tax);
                    fixedAssetMaster.SaleTaxes = fixedAssetMaster.SaleTaxes.concat(response.data.tax);
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                }
            );
        };
        var AddSpeciality = function (item, idx) {
            self.model.isOk = false;
            self.model.specialityDetailList.push({ specialityName: item.id });
            self.model.specialityDetailList[idx + 1].specialityName = self.model.specialityList[0];
        };

        var LessSpeciality = function (item, idx) {
            self.model.isOk = false;
            var index = self.model.specialityDetailList.indexOf(item);
            self.model.specialityDetailList.splice(index, 1);
        };

        var getActiveSpecialityList = function () { };

        var SaveSpecialityDetails = function (taxDuplicateData) {
            if (checkEmptyTaxDetails() && DuplicateData(self.model.specialityDetailList)) {
                self.model.isOk = true;
                var test = [];
                //var taxData = self.model.specialityList.find(function (type) {
                //     return type.id == self.model.specialityName;
                //});
                angular.forEach(self.model.specialityDetailList, function (tax) {
                    test.push = {
                        specialityName: tax.specialityName,
                    };
                });
                var specialityarray = [];
                angular.forEach(self.model.specialityDetailList, function (x) {
                    specialityarray.push({
                        id: x.specialityName.specialityId,
                    });
                });
                self.model.specialityarray = specialityarray;
                var popup = angular.element("#addSpecialityComponent");
                popup.modal("hide");
                self.model.speciality = self.model.specialityarray.length;
                ViewTax();
            }
        };

        function DuplicateData(specialityDuplicateData) {
            var isValid = true;
            var object = {};
            var result = [];

            specialityDuplicateData.forEach(function (item) {
                //  if (!!item.id) {
                if (!object[item.specialityName.specialityId]) object[item.specialityName.specialityId] = 0;
                object[item.specialityName.specialityId] += 1;
                //  }
            });
            for (var prop in object) {
                if (object[prop] >= 2) {
                    result.push(prop);
                }
            }
            if (!!result.length) {
                isValid = false;
                growl.error("Duplicate Speciality");
            }
            return isValid;
        }

        var checkEmptyTaxDetails = function () {
            var isValid = true;
            self.model.specialityDetailList.forEach(function (element, key) {
                if (!element.specialityName.specialityId) {
                    isValid = false;
                    self.model.isSpecialityComponentValid[key] = true;
                    growl.error("Speciality is required");
                }

            });
            return isValid;
        };

        var ViewTax = function (item) {

            angular.forEach(self.model.implantMasterList, function (x) {
                if (item.id == x.id) {
                    self.model.specialityDetails = item.speciality;
                }
            });
        };

        var SearchManufacturer = function (search) {
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

      

        var ValidateItemCode = function () {
            var isValid = true;
            if(self.model.implantMasterList){
                self.model.implantMasterList.forEach(function (element) {
                    if (element.itemCode == self.model.itemCode) {
                        growl.error('Duplicate Item code');
                        isValid = false;
                    }
                });
            }

            return isValid;
        }

        var ValidateItemCodeForUpdate = function () {
            var isValid = true;
            if(self.model.implantMasterList){    
                self.model.implantMasterList.forEach(function (element) {
                    if (element.id != self.model.activeImplant) {
                        if (element.itemCode == self.model.itemCode) {
                            growl.error('Duplicate Item code');
                            isValid = false;
                        }
                    }
                });
            }

            return isValid;
        }

        var ValidateProductCode = function () {
            var isValid = true;
            if(self.model.implantMasterList){
                self.model.implantMasterList.forEach(function (element) {
                    if (element.productCode == self.model.productCode) {
                        growl.error('Duplicate Product code');
                        isValid = false;
                    }
                });
            }
            if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCode)) {
                growl.error('Code can only be alpha numeric with single hypen.');
                return;
            }
            return isValid;
        }

        var ValidateProductCodeForUpdate = function () {
            var isValid = true;
            if(self.model.implantMasterList){    
                self.model.implantMasterList.forEach(function (element) {
                    if (element.id != self.model.activeImplant) {
                        if (element.productCode == self.model.productCode) {
                            growl.error('Duplicate Product code');
                            isValid = false;
                        }
                    }
                });
            }
            if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCode)) {
                growl.error('Code can only be alpha numeric with single hypen.');
                return;
            }
            return isValid;
        }
   
        var SaveFixedAsset = function () {
            if (ValidateImplantMaster()) {
                if (!self.model.implantId) {
                    CreateFixedAssetType();
                } else {
                    UpdateFixedAssetType();
                }
            }
        };

        var CreateFixedAssetType = function () {
            if (ValidateItemCode() && ValidateProductCode()) {
                var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.SAVE_API;
                //var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER;
                var data = {
                    "productCategoryId": self.model.productCategory.id,
                    "manufactureId": self.model.manufactureId,
                    "itemCode": self.model.itemCode,
                    "sTaxId": self.model.saleTax.id,
                    "pTaxId": self.model.purchaseTax.id,
                    "productCode": self.model.productCode,
                    "itemName": self.model.assetName,
                    "storageUnitId": self.model.unit.id,
                    "status": true,
                    "isConsignment": self.model.isConsignment,
                    "isSterilize": self.model.isSterilized,
                    "specialeety": self.model.specialityarray,
                    "isBatchRequired":self.model.isBatchRequired,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }

                var assetCategoryData = self.model.assetCategoryList.find(function (type) {
                    return type.id == self.model.assetCategory.id;
                });
                var productCategoryData = self.model.productCategoryList.find(function (type) {
                    return type.id == self.model.productCategory.id;
                });
                var purchaseTax = self.model.PurchaseTaxes.find(function (tax) {
                    return tax.id == self.model.purchaseTax.id;
                });
                var saleTax = self.model.SaleTaxes.find(function (tax) {
                    return tax.id == self.model.saleTax.id;
                });
                var storageUnit = self.model.StorageUnits.find(function (sunit) {
                    return sunit.id == self.model.unit.id;
                });

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        var implantData = {
                            "id": response.data.id,
                            "assetCategory": assetCategoryData.category,
                            "productCategory": productCategoryData.category,
                            "itemCode": self.model.itemCode,
                            "productCode": self.model.productCode,
                            "name": self.model.assetName,
                            "sTaxName": saleTax.type,
                            "storageUnit": storageUnit.unit,
                            "pTaxName": purchaseTax.type,
                            "manufactureId": self.model.manufactureId,
                            "manufactureName": self.model.manufacturer,
                            "status": true,
                            "isConsignment": self.model.isConsignment,
                            "isSterilized": self.model.isSterilized,
                            "isBatchRequired":self.model.isBatchRequired
                        };
                        self.model.implantMasterList.push(implantData);
                        PaginationService.searchData(self.model.implantMasterList, self.model.pagination);
                        PaginationService.setLastPage(self.model.pagination);
                        ClearFields();

                        $rootScope.stopLoader();
                        getFixedAssetComponent();
                        growl.success(response.data.message);
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Something went wrong');
                    });

            }
        };

        var UpdateFixedAssetType = function () {
            if (ValidateItemCodeForUpdate() && ValidateProductCodeForUpdate()) {
                var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.DETAILS_API + self.model.implantId;
                //var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER + self.model.implantId;
                var specialityarray = [];
                angular.forEach(self.model.specialityDetailList, function (x) {
                    specialityarray.push({
                        id: x.id,

                    });
                });
                self.model.specialityarray = specialityarray;
                var data = {
                    "productCategoryId": self.model.productCategory.id,
                    "manufactureId": self.model.manufactureId,
                    "itemCode": self.model.itemCode,
                    "productCode": self.model.productCode,
                    "itemName": self.model.assetName,
                    "sTaxId": self.model.saleTax.id,
                    "pTaxId": self.model.purchaseTax.id,
                    "storageUnitId": self.model.unit.id,
                    "status": self.model.status,
                    "isConsignment": self.model.isConsignment,
                    "isSterilize": self.model.isSterilized,
                    "specialeety": self.model.specialityarray,
                    "isBatchRequired" :self.model.isBatchRequired
                }
                
                var assetCategoryData = self.model.assetCategoryList.find(function (type) {
                    return type.id == self.model.assetCategory.id;
                });
                var productCategoryData = self.model.productCategoryList.find(function (type) {
                    return type.id == self.model.productCategory.id;
                });
                var purchaseTax = self.model.PurchaseTaxes.find(function (tax) {
                    return tax.id == self.model.purchaseTax.id;
                });
                var saleTax = self.model.SaleTaxes.find(function (tax) {
                    return tax.id == self.model.saleTax.id;
                });
                var storageUnit = self.model.StorageUnits.find(function (sunit) {
                    return sunit.id == self.model.unit.id;
                });
                
               
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
//                        var implantData = self.model.implantMasterList.find(function (type) {
//                            return type.id == self.model.implantId;
//                        });
//
//                        var assetCategoryData = self.model.assetCategoryList.find(function (type) {
//                            return type.id == self.model.assetCategory.id;
//                        });
//                        var productCategoryData = self.model.productCategoryList.find(function (type) {
//                            return type.id == self.model.productCategory.id;
//                        });
//                        var purchaseTax = self.model.PurchaseTaxes.find(function (tax) {
//                            return tax.id == self.model.purchaseTax.id;
//                        });
//                        var saleTax = self.model.SaleTaxes.find(function (tax) {
//                            return tax.id == self.model.saleTax.id;
//                        });
//                        var storageUnit = self.model.StorageUnits.find(function (sunit) {
//                            return sunit.id == self.model.unit.id;
//                        });
//                        var listSpeciality = [];
//                        angular.forEach(response.data.items, function (abc) {
//                            var selectedSpeciality = self.model.specialityList.find(function (data) {
//                                if (abc.id == 1) {
//                                    return abc;
//                                }
//                            });
//                            listSpeciality.push({
//                                id: abc.id,
//                                specialityName: selectedSpeciality,
//                            });
//                        });
//                        self.model.specialityDetailList = listSpeciality;
//
//                        implantData.itemCode = data.itemCode;
//                        implantData.productCode = data.productCode;
//                        implantData.name = data.itemName;
//                        implantData.manufactureId = self.model.manufactureId;
//                        implantData.manufactureName = self.model.manufacturer;
//                        implantData.sTaxName = saleTax.type;
//                        implantData.storageUnit = storageUnit.unit;
//                        implantData.pTaxName = purchaseTax.type;
//                        implantData.category = productCategoryData.category;
//                        implantData.assetCategory = assetCategoryData.category;
                    	
                    	 var implantData = {
                                 "id": response.data.id,
                                 "assetCategory": assetCategoryData.category,
                                 "productCategory": productCategoryData.category,
                                 "itemCode": self.model.itemCode,
                                 "productCode": self.model.productCode,
                                 "name": self.model.assetName,
                                 "sTaxName": saleTax.type,
                                 "storageUnit": storageUnit.unit,
                                 "pTaxName": purchaseTax.type,
                                 "manufactureId": self.model.manufactureId,
                                 "manufactureName": self.model.manufacturer,
                                 "status": true,
                                 "isConsignment": self.model.isConsignment,
                                 "isSterilized": self.model.isSterilized,
                                 "isBatchRequired":self.model.isBatchRequired
                             };
                       
                        self.model.implantMasterList[$scope.UpdateDataIndex] = implantData
                        
                        PaginationService.searchData(self.model.implantMasterList, self.model.pagination);
                        PaginationService.setLastPage(self.model.pagination);
                        ClearFields();
                        
                        $rootScope.stopLoader();
                        getFixedAssetComponent();
                        growl.success(response.data.message);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Something Went Wrong');
                    });
            }
        }

        var updateStatusFixedAsset = function (item) {
            var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.DETAILS_API + item.id;
            var data = {
                "status": item.status
            }
            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {    
                $rootScope.stopLoader();
                getFixedAssetComponent();
                growl.success(response.data.message);
            },
            function (err) {
                $rootScope.stopLoader();
                growl.error('Something Went Wrong');
            });
        }

        //Method to clear the fileds
        function ClearFields() {
            self.model.assetCategory = [self.model.assetCategoryList[0]];
            self.model.assetCategory = self.model.assetCategoryList[0];
            self.model.productCategory = [self.model.productCategoryList[0]];
            self.model.productCategory = self.model.productCategoryList[0];
            self.model.purchaseTax = [self.model.PurchaseTaxes[0]];
            self.model.purchaseTax = self.model.PurchaseTaxes[0];
            self.model.saleTax = [self.model.SaleTaxes[0]];
            self.model.saleTax = self.model.SaleTaxes[0];
            self.model.unit = [self.model.StorageUnits[0]];
            self.model.unit = self.model.StorageUnits[0];
            self.model.itemCode = "";
            self.model.productCode = "";
            self.model.assetName = "";
            self.model.manufacturer = "";
            self.model.assetCategoryList.splice(1);
            self.model.productCategoryList.splice(1);
            self.model.PurchaseTaxes.splice(1);
            self.model.SaleTaxes.splice(1);
            self.model.StorageUnits.splice(1);
            self.model.pagination.search = '';
            self.model.isConsignment = false;
            self.model.isSterilized = false;
            self.model.isBatchRequired = false;
            self.model.description = "";
            self.model.specialityDetailList = [
                { specialityName: self.model.specialityList[0] }
            ];
            self.model.isOk = false;
        }
        var clearSearch = function () {
            self.model.pagination.search = '';
            PaginationService.searchData(self.model.implantMasterList, self.model.pagination);
        }

        var ValidateImplantMaster = function () {
            var isValid = true;

            if (!self.model.assetCategory.id) {
                isValid = false;
                self.model.isValidCode = true;
            }
            if (!self.model.productCategory.id) {
                isValid = false;
                self.model.isValidProductCat = true;
            }
            if (!self.model.purchaseTax.id) {
                isValid = false;
                self.model.ispurchaseTaxValid = true;
            }
            if (!self.model.saleTax.id) {
                isValid = false;
                self.model.issaleTaxValid = true;
            }
            if (!self.model.unit.id) {
                isValid = false;
                self.model.isstorageUnitValid = true;
            }
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
        $stateProvider.state("implantMaster", {
            url: "/implantMaster",
            templateUrl: 'views/master/UnitMaster/Inventory/implant-master.html',
            controller: "ImplantMaster.Controller",
            title: 'Implant Master',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 806);
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
        .controller("ImplantMaster.Controller", implantMasterController)
})();