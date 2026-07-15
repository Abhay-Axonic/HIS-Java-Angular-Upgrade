(function () {
    "use strict";

    function generalInsuranceMasterController($scope, $rootScope, $cookies, CONSTANTS, $http, GenericService, growl) {
        var self = this;
        // var apiUrl = constants.apiurl + constants.unitsApi;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                Countries: [
                    {
                        countryName: "Select Country"
                    }
                ],
                States: [
                    {
                        stateName: "Select State"
                    }
                ],
                Cities: [
                    {
                        cityName: "Select City"
                    }
                ],
                Areas: [
                    {
                        areaName: "Select Area"
                    }
                ]
            };
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.PopulateStates = PopulateStates;
            self.model.PopulateCities = PopulateCities;
            self.model.PopulateAreas = PopulateAreas;
            self.model.getAddressDetails = getAddressDetails;
            self.model.PopulateGenInsMaster = PopulateGenInsMaster;
            self.model.SaveGenInsMaster = SaveGenInsMaster;
            self.model.ToggleStatus=ToggleStatus;
            self.model.detail_address = {
                address: "",
                country: "",
                state: "",
                city: "",
                area: "",
                zipCode: ""
            };

            PopulateDropdown();
        }

        var PopulateDropdown = function () {
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId;
            var genInsMaster = self.model;

            genInsMaster.countryName = genInsMaster.Countries[0];
            genInsMaster.stateName = genInsMaster.States[0];
            genInsMaster.cityName = genInsMaster.Cities[0];
            genInsMaster.areaName = genInsMaster.Areas[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.Countries = self.model.Countries.concat(response.data.country);
                    $rootScope.stopLoader();
                    getGenInsMasterList();

                });

        }
        var getGenInsMasterList = function () {
            var URI = CONSTANTS.MASTER.UNIT.GENERAL_INSURANCE_MASTER.GET_ALL;
            // var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.genInsMasterDataList = response.data;
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                }
            );
        };


        function PopulateGenInsMaster(id) {
            self.model.iscontactPersonNameValid = false;
            self.model.iszipCodeValid = false;
            self.model.isphoneNumberValid = false;
            self.model.isemailValid = false;
            self.model.iscountryNameValid = false;
            self.model.isstateNameValid = false;
            self.model.iscityNameValid = false;
            self.model.isareaNameValid = false;

            self.model.activeGenInsMasterId = id;
            var URI = CONSTANTS.MASTER.UNIT.GENERAL_INSURANCE_MASTER.DETAILS_API + id;

            GenericService.serviceAction("GET", URI, {}).then(function (response) {

                var Country = self.model.Countries.find(function (country) {
                    return response.data.countryId == country.countryId;
                });

                PopulateStates(response.data, true);
                PopulateCities(response.data, true);
                PopulateAreas(response.data, true);

                self.model.id = id;
                self.model.insuranceCompany = response.data.insuranceCompanyName;
                self.model.contactNo = response.data.contactNo;
                self.model.emailAddress = response.data.email;
                self.model.address = response.data.address;
                self.model.contactPersonName = response.data.contactPersonName;
                self.model.contactPersonNo = response.data.contactPersonNumber;
                self.model.status = response.data.status;
                self.model.zipCode = response.data.zipCode;
                self.model.countryName = Country;

                self.model.isEdit = true;
            });
        }

        function getAddressDetails(data) {
            self.model.detail_address.address = data.address;
            self.model.detail_address.country = data.countryName;
            self.model.detail_address.state = data.stateName;
            self.model.detail_address.city = data.cityName;
            self.model.detail_address.area = data.areaName;
            self.model.detail_address.zipCode = data.zipCode;
        }

        function PopulateStates(country, selectedvalue) {
            if (country.countryId == undefined) {
                country.countryId = 0;
            }
            var genInsMaster = self.model;

            var URI = CONSTANTS.GLOBAL.STATE_API +$scope.organizationId + '/'+ country.countryId;

            genInsMaster.stateName = genInsMaster.States[0];
            genInsMaster.cityName = genInsMaster.Cities[0];
            genInsMaster.areaName = genInsMaster.Areas[0];

            genInsMaster.States.splice(1);
            genInsMaster.Cities.splice(1);
            genInsMaster.Areas.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(function (response) {
                genInsMaster.States = genInsMaster.States.concat(response.data);
                $rootScope.stopLoader();

                if (selectedvalue) {
                    var State = genInsMaster.States.find(function (state) {
                        return country.stateId == state.stateId;
                    });
                    self.model.stateName = State;
                }
            });
        }

        //Getting City List
        function PopulateCities(state, selectedvalue) {
            if (state.stateId == undefined) {
                state.stateId = 0;
            }
            var genInsMaster = self.model;

            var URI = CONSTANTS.GLOBAL.CITY_API +$scope.organizationId + '/' + state.stateId;

            genInsMaster.cityName = genInsMaster.Cities[0];
            genInsMaster.areaName = genInsMaster.Areas[0];

            genInsMaster.Cities.splice(1);
            genInsMaster.Areas.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(function (response) {
                genInsMaster.Cities = genInsMaster.Cities.concat(response.data);
                $rootScope.stopLoader();

                if (selectedvalue) {
                    var City = genInsMaster.Cities.find(function (city) {
                        return state.cityId == city.cityId;
                    });
                    self.model.cityName = City;
                }
            });
        }

        //Getting City List
        function PopulateAreas(city, selectedvalue) {
            if (city.cityId == undefined) {
                city.cityId = 0;
            }
            var genInsMaster = self.model;

            var URI = CONSTANTS.GLOBAL.AREA_API +$scope.organizationId + '/'  + city.cityId;

            genInsMaster.areaName = genInsMaster.Areas[0];

            genInsMaster.Areas.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(function (response) {
                genInsMaster.Areas = genInsMaster.Areas.concat(response.data);
                $rootScope.stopLoader();

                if (selectedvalue) {
                    var Area = genInsMaster.Areas.find(function (area) {
                        return city.areaId == area.areaId;
                    });
                    self.model.areaName = Area;
                }
            });
        }

        var ValidateEmail = function () {
            if (
                /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(
                    self.model.emailAddress
                )
            ) {
                return true;
            }
            growl.error("Enter valid Email");
            return false;
        };

        var validatePhoneNo = function () {
            if (/^\+?\d+$/.test(self.model.contactNo)) {
                return true;
            }
            growl.error("Enter Valid Contact No");
            return false;
        }

        var validateContactPersonNo = function () {
            if (/^\+?\d+$/.test(self.model.contactPersonNo)) {
                return true;
            }
            growl.error("Enter valid Contact Person No.");
            return false;
        }

        var ValidateGenIns = function () {
            var isValid = true;

            if (!self.model.insuranceCompany) {
                isValid = false;
                self.model.isInsuranceCompanyValid = true;
            }
            if (!self.model.contactNo) {
                isValid = false;
                self.model.isContactNoValid = true;
            }
            if (!self.model.emailAddress) {
                isValid = false;
                self.model.isemailValid = true;
            }
            if (!self.model.contactPersonName) {
                isValid = false;
                self.model.iscontactPersonNameValid = true;
            }
            if (!self.model.contactPersonNo) {
                isValid = false;
                self.model.isContactPersonNoValid = true;
            }
            if (!self.model.address) {
                isValid = false;
                self.model.isaddressValid = true;
            }
            if (!self.model.countryName.countryId) {
                isValid = false;
                self.model.iscountryNameValid = true;
            }
            if (!self.model.stateName.stateId) {
                isValid = false;
                self.model.isstateNameValid = true;
            }
            if (!self.model.cityName.cityId) {
                isValid = false;
                self.model.iscityNameValid = true;
            }
            if (!self.model.areaName.areaId) {
                isValid = false;
                self.model.isareaNameValid = true;
            }
            if (!self.model.zipCode) {
                isValid = false;
                self.model.iszipCodeValid = true;
            }


            return isValid;
        };
        function SaveGenInsMaster() {
            if (ValidateGenIns()) {
                if (!self.model.id) {
                    createGenInsMaster();
                } else {
                    updateGenInsMaster();
                }
            }
        }
        //Method to add the VendorMaster
        function createGenInsMaster() {
            if (validatePhoneNo() && validateContactPersonNo() && ValidateEmail()) {
                var URI = CONSTANTS.MASTER.UNIT.GENERAL_INSURANCE_MASTER.SAVE_API;
                var data = {
                    "insuranceCompanyName": self.model.insuranceCompany,
                    "contactNo": self.model.contactNo,
                    "email": self.model.emailAddress,
                    "contactPersonName": self.model.contactPersonName,
                    "contactPersonNumber": self.model.contactPersonNo,
                    "address": self.model.address,
                    "areaId": self.model.areaName.areaId,
                    "zipCode": self.model.zipCode,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                };
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {

                        var countryName = self.model.Countries.find(function (country) {
                            return country.countryId == self.model.countryName.countryId;
                        });
                        var stateName = self.model.States.find(function (state) {
                            return state.stateId == self.model.stateName.stateId;
                        });
                        var cityName = self.model.Cities.find(function (city) {
                            return city.cityId == self.model.cityName.cityId;
                        });
                        var areaName = self.model.Areas.find(function (area) {
                            return area.areaId == self.model.areaName.areaId;
                        });
                        var insertedData = {
                            id: response.data.id,
                            insuranceCompanyName: self.model.insuranceCompany,
                            contactNo: self.model.contactNo,
                            email: self.model.emailAddress,
                            contactPersonName: self.model.contactPersonName,
                            contactPersonNumber: self.model.contactPersonNo,
                            address: self.model.address,
                            countryName: countryName.countryName,
                            stateName: stateName.stateName,
                            cityName: cityName.cityName,
                            areaName: areaName.areaName,
                            zipCode: self.model.zipCode,
                            status: true
                        };
                        self.model.genInsMasterDataList.push(insertedData);
                        clearFields();
                        growl.success(response.data.message);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error("Something Went Wrong");
                    }
                );
            }
        }

        var ValidateGenInsForUpdate = function () {
            var isValid = true;

            if (parseInt(self.model.zipCode.length) < 6) {
                growl.error("Enter Valid Zip code.");
                return;
            }
            if (parseInt(self.model.contactNo.length) < 10) {
                growl.error("Enter Valid Contact no.");
                return;
            }
            if (parseInt(self.model.contactPersonNo.length) < 10) {
                growl.error("Enter Valid Contact Person no.");
                return;
            }
            return isValid;
        };


        //Method to update the  VendorMaster
        function updateGenInsMaster() {
            if (ValidateGenInsForUpdate() && validatePhoneNo() && validateContactPersonNo() && ValidateEmail()) {
                var URI = CONSTANTS.MASTER.UNIT.GENERAL_INSURANCE_MASTER.UPDATE_API + self.model.id;
                var data = {
                    "id": self.model.id,
                    "insuranceCompanyName": self.model.insuranceCompany,
                    "contactNo": self.model.contactNo,
                    "email": self.model.emailAddress,
                    "contactPersonName": self.model.contactPersonName,
                    "contactPersonNumber": self.model.contactPersonNo,
                    "address": self.model.address,
                    "areaId": self.model.areaName.areaId,
                    "zipCode": self.model.zipCode,
                    "status": self.model.status,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {

                        var countryName = self.model.Countries.find(function (country) {
                            return country.countryId == self.model.countryName.countryId;
                        });
                        var stateName = self.model.States.find(function (state) {
                            return state.stateId == self.model.stateName.stateId;
                        });
                        var cityName = self.model.Cities.find(function (city) {
                            return city.cityId == self.model.cityName.cityId;
                        });
                        var areaName = self.model.Areas.find(function (area) {
                            return area.areaId == self.model.areaName.areaId;
                        });
                        var insMaster = self.model.genInsMasterDataList.find(function (vendor) {
                            return vendor.id == self.model.id;
                        });

                        insMaster.insuranceCompanyName = data.insuranceCompanyName;
                        insMaster.contactNo=data.contactNo;
                        insMaster.email = data.email;
                        insMaster.contactPersonName = data.contactPersonName;
                        insMaster.contactPersonNumber = data.contactPersonNumber;
                        insMaster.address = data.address;
                        insMaster.countryName = countryName.countryName;
                        insMaster.stateName = stateName.stateName;
                        insMaster.cityName = cityName.cityName;
                        insMaster.areaName = areaName.areaName;
                        insMaster.zipCode = data.zipCode;                   
                        insMaster.status = data.status;

                        clearFields();
                        growl.success(response.data.message);
                        self.model.activeGenInsMasterId = undefined;
                        self.model.isEdit = false;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error("Something Went Wrong");
                    }
                );
            }
        }

        var ToggleStatus = function(insMaster) {
            
            var URI = CONSTANTS.MASTER.UNIT.GENERAL_INSURANCE_MASTER.UPDATE_API + insMaster.id;
       
              var data = {
                "status": insMaster.status,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
              }
          
              $rootScope.startLoader();
              GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                  $rootScope.stopLoader();
                  growl.success('Status changed successfully');
                }, function (err) {
                  if(err.status == 406){
                    growl.error(err.data.message);
                    insMaster.status = true;
                  }else{
                 
                  growl.error('Something Went Wrong');
                }
                });
          };
        //Method to clear the fileds
        function clearFields() {
            self.model.countryName = self.model.Countries[0];
            self.model.stateName = self.model.States[0];
            self.model.States.splice(1);
            self.model.cityName = self.model.Cities[0];
            self.model.Cities.splice(1);
            self.model.areaName = self.model.Areas[0];
            self.model.Areas.splice(1);
            self.model.insuranceCompany = "";
            self.model.contactNo = "";
            self.model.emailAddress = "";
            self.model.contactPersonName = "";
            self.model.contactPersonNo = "";
            self.model.address = "";
            self.model.countryId = "";
            self.model.stateId = "";
            self.model.cityId = "";
            self.model.areaId = "";
            self.model.zipCode = "";
            self.model.id = undefined;
        }

        var clearSearch = function () {
            self.model.search = "";
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("generalInsuranceMaster", {
            url: "/generalInsuranceMaster",
            templateUrl: "views/master/GlobalMaster/AssetManagenent/Master/general-insurance-master.html",
            controller: "GeneralInsuranceMaster.Controller",
            title: 'General Insurance Master',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 646);
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
        .controller("GeneralInsuranceMaster.Controller", generalInsuranceMasterController)
})();