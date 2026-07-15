(function() {
  "use strict";
  function vendorMasterController(
    $scope,
    $rootScope,
    $http,
    $state,
    CONSTANTS,
    GenericService,
    BillingGenericService,
    GenericServiceFileUpload,
    PaginationService,
    growl,
    $cookies,
    $timeout
  ) {
    var self = this;
    // var apiUrl = constants.apiurl + constants.unitsApi;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
	$scope.unitCountryId = cookieObject.unitCountryId;
	$scope.unitStateId = cookieObject.unitStateId;
	$scope.unitDistrictId = cookieObject.unitDistrictId;
	$scope.unitCityId = cookieObject.unitCityId;
	$scope.unitAreaId = cookieObject.unitAreaId;
	$scope.unitZipCode = cookieObject.unitZipCode;
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        isLocalPurchase: false,
        Manufactures: [
          {
            name: "Select Manufacturer"
          }
        ],
        modeOfPayments: [
          {
            paymentModeDesc: "Select Mode of Payment"
          }
        ],
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
        }
      };

      self.model.ToggleStatus = ToggleStatus;
      self.model.PopulateVendorMaster = PopulateVendorMaster;
      self.model.ValidateVendor = ValidateVendor;
      self.model.SaveVendorMaster = SaveVendorMaster;
      self.model.PopulateStates = PopulateStates;
      self.model.PopulateCities = PopulateCities;
      self.model.PopulateAreas = PopulateAreas;
      self.model.ClearFields = clearFields;
      self.model.getAddressDetails = getAddressDetails;
      self.model.ValidateEmail = ValidateEmail;
      self.model.ValidateVendorName = ValidateVendorName;
      self.model.ValidateGst = ValidateGst;
      self.model.ValidateLicNo = ValidateLicNo;
      self.model.clearSearch = clearSearch;
      self.model.PopulateDropdown = PopulateDropdown;
     // self.model.validatePhoneNo = validatePhoneNo;
      self.model.checkPhoneNumDigit = checkPhoneNumDigit;
      
      self.model.showDocUPloadPoup = showDocUPloadPoup;
      
      self.model.detail_address = {
        address: "",
        country: "",
        state: "",
        city: "",
        area: "",
        zipcode: ""
      };
      $scope.selectedManufacturesList=[];      
      PopulateDropdown();
    }
    $('.modal-backdrop').remove()
	  $(document.body).removeClass("modal-open");
    $scope.manufacturesMultiSelectList =[];
    var PopulateDropdown = function(){
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId ;
      var vendorMaster = self.model;

      // vendorMaster.manufactureName = vendorMaster.Manufactures[0];
      vendorMaster.countryName = vendorMaster.Countries[0];
      vendorMaster.modeOfPayment = vendorMaster.modeOfPayments[0];
      vendorMaster.stateName = vendorMaster.States[0];
      vendorMaster.cityName = vendorMaster.Cities[0];
      vendorMaster.areaName = vendorMaster.Areas[0];
      
    
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
          function (response) {

              // self.model.Manufactures = self.model.Manufactures.concat(response.data.manufacturer);
              self.model.Countries = self.model.Countries.concat(response.data.country);
              self.model.modeOfPayments = self.model.modeOfPayments.concat(response.data.paymentMode);
              self.model.mobileDigit = response.data.mobileDigit;
            //  $scope.manufacturesList = response.data.manufacturer;    
             /* angular.forEach(response.data.manufacturer,
                  function(value,index) {
                    var data = {};
                    data.id = value.id;
                    data.label = value.name;
                    $scope.manufacturesMultiSelectList.push(data);
              });*/
              getVendorMasterList();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
    }
    
    $scope.example6settings = {
      enableSearch: true,
      scrollableHeight: '150px', 
      scrollable: true
    };

    $scope.yourEvents = {
      onItemSelect: function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);                     
      },
      onItemDeselect: function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);  
      },
      onDeselectAll:function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);  
      },
      onSelectAll:function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);  
      },
    };

    //Method to get the list of Vendors
    var getVendorMasterList = function() {
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API+ $scope.organizationId + '/'+$scope.unitId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.vendorMasterDataList = response.data;
          angular.forEach(self.model.vendorMasterDataList, function(value1 , key) {
          angular.forEach($scope.ledgerList, function(value , key) {
  			if(value1.generalLedgerId==value.generalLedgerId)
  				{
  				value1.generalLedgerName=value.generalLedgerName;
  				}
  		});
          });
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.vendorMasterDataList, self.model.pagination);
          }

          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Getting  State List
    function PopulateStates(country, selectedvalue) {
      if (country.countryId == undefined) {
        country.countryId = 0;
      }
      var vendorMaster = self.model;

      var URI = CONSTANTS.GLOBAL.STATE_API +$scope.organizationId + '/'+ country.countryId;

      vendorMaster.stateName = vendorMaster.States[0];
      vendorMaster.cityName = vendorMaster.Cities[0];
      vendorMaster.areaName = vendorMaster.Areas[0];

      vendorMaster.States.splice(1);
      vendorMaster.Cities.splice(1);
      vendorMaster.Areas.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        vendorMaster.States = vendorMaster.States.concat(response.data);
        $rootScope.stopLoader();

        if (selectedvalue) {
          var State = vendorMaster.States.find(function(state) {
            return country.stateId == state.stateId;
          });
          self.model.stateName = State;
        }
      });
      self.model.zipCode =  "";
    }

    //Getting City List
    function PopulateCities(state, selectedvalue) {
      if (state.stateId == undefined) {
        state.stateId = 0;
      }
      var vendorMaster = self.model;

      var URI = CONSTANTS.GLOBAL.CITY_API +$scope.organizationId + '/' + state.stateId;

      vendorMaster.cityName = vendorMaster.Cities[0];
      vendorMaster.areaName = vendorMaster.Areas[0];

      vendorMaster.Cities.splice(1);
      vendorMaster.Areas.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        vendorMaster.Cities = vendorMaster.Cities.concat(response.data);
        $rootScope.stopLoader();

        if (selectedvalue) {
          var City = vendorMaster.Cities.find(function(city) {
            return state.cityId == city.cityId;
          });
          self.model.cityName = City;
        }
      });
      self.model.zipCode =  "";
    }

    //get Areas with zip code
    $scope.areaListWithZipCode = [];
    $scope.getAreaByCity = function(id) {
		$scope.startLoader();
		var data = {
			cityId : id
		};
		var URI = BASE_URL + ROOT_URL + GETAREALISTBYCITY;
		GenericService .serviceAction(METHOD_POST, URI, data)
		.then(function(response) {
					if (response.data.status == "success")
					{
						$scope.areaListWithZipCode = [];
						 $scope.areaListWithZipCode = response.data.listObject;
					}
					$scope.stopLoader();
				});
	}
    if($scope.unitCityId>0){
    	$scope.getAreaByCity($scope.unitCityId);
    }
    
    //Getting City List
    function PopulateAreas(city, selectedvalue) {
      if (city.cityId == undefined) {
        city.cityId = 0;
      }
      var vendorMaster = self.model;

      var URI = CONSTANTS.GLOBAL.AREA_API +$scope.organizationId + '/'  + city.cityId;

      vendorMaster.areaName = vendorMaster.Areas[0];

      vendorMaster.Areas.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function(response) {
        vendorMaster.Areas = vendorMaster.Areas.concat(response.data);
        $rootScope.stopLoader();

        if (selectedvalue) {
          var Area = vendorMaster.Areas.find(function(area) {
            return city.areaId == area.areaId;
          });
          self.model.areaName = Area;
        }
      });
      $scope.getAreaByCity(city.cityId);
      self.model.zipCode =  "";
      
    }

    //Method to populate VendorMaster for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getVendorMasterById(id) {
      return findInRecordInArray(self.model.vendorMasterDataList, "id", id);
    }

    function PopulateVendorMaster(id) {
      // self.model.isManufacturerValid = false;
      self.model.isvendorCodeValid = false;
      self.model.isvendorNameValid = false;
      self.model.iscontactPersonNameValid = false;
      self.model.isdrugLiscenceNoValid = true;
      self.model.isgstNoValid = true;
      self.model.isaddressValid = false;
      self.model.iszipCodeValid = true;
      self.model.isphoneNumberValid = false;
      self.model.isemailValid = true;
      self.model.ismodofPaymentValid = false;
      self.model.ispaymentTermsValid = false;
      self.model.iscountryNameValid = false;
      self.model.isstateNameValid = true;
      self.model.iscityNameValid = true;
      self.model.isareaNameValid = true;

      self.model.activeVendorMasterId = id;
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        var vendor = getVendorMasterById(id);
        vendor = response.data;
      /*  var mnufacturerName = self.model.Manufactures.find(function(
          manufacturer
        ) {
          return response.data.manufacturerId == manufacturer.id;
        });*/
        var paymentMode = self.model.modeOfPayments.find(function(paymentmode) {
          return response.data.paymentModeId == paymentmode.paymentModeId;
        });
        var Country = self.model.Countries.find(function(country) {
          return response.data.countryId == country.countryId;
        });
        PopulateStates(response.data, true);
        PopulateCities(response.data, true);
        PopulateAreas(response.data, true);
        
        $scope.selectedManufacturesList = [];
        var newArr={};
        newArr.id = response.data.manufacturerId;
        $scope.selectedManufacturesList.push(newArr);
        
        self.model.id = response.data.id;
        self.model.vendorCode = response.data.code;
        self.model.vendorName = response.data.name;
        self.model.address = response.data.address;
        self.model.contactPersonName = response.data.contactPersonName;
        self.model.creditDays = response.data.creditDays;  
        self.model.drugLicenseNo = response.data.drugLicenseNo;
        self.model.emailAddress = response.data.emailId;
        self.model.gstNo = response.data.gstNo;
        self.model.paymentTerms = response.data.paymentTerm;
        self.model.phoneNumber = response.data.phoneNumber;
        self.model.manufactureName = response.data.name;
        self.model.modeOfPayment = paymentMode;
        self.model.status = response.data.status;
        self.model.zipCode = response.data.zipcode;
        self.model.isLocalPurchase = response.data.isLocalPurchase;
        self.model.countryName = Country;
        self.model.isEdit = true;
        self.model.generalLedgerName = response.data.generalLedgerName;
        self.model.generalLedgerId = response.data.generalLedgerId;
        angular.forEach($scope.ledgerList, function(value , key) {
			if(self.model.generalLedgerId==value.generalLedgerId)
				{
				self.model.generalLedgerName=value.generalLedgerName;
				}
		});
        /* start code for resolved unknwon bug during test HIS-10048  Vendor Master : Updated Manufacturer Name not Getting Display. Need to Refresh Page */
       /* var manufacturerName = $scope.manufacturesMultiSelectList.find(function(manufacturer){
        	return response.data.manufacturerId == manufacturer.id;
        });
        self.model.manufactureName = manufacturerName;
        $scope.selectedManufacturesList.push(manufacturerName);*/  //for unknown bug comment
        /* end code for resolved unknwon bug during test HIS-10048  Vendor Master : Updated Manufacturer Name not Getting Display. Need to Refresh Page */
        
      });
    }
    
    

    //Method to activate or deactivate Main Store
    var ToggleStatus = function(vendorMaster) {
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API +
        vendorMaster.id;
    var manufacturerIds = [];
    var obj = {
    		id : vendorMaster.manufacturerId
    };
    manufacturerIds.push(obj);
        var data = {
          "status": vendorMaster.status,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        };
        // if (vendorMaster.status == false) {
        //   ClearFields();
        // }
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success('Status changed successfully');
          }, function (err) {
            if(err.status == 406){
              growl.error(err.data.message);
              vendorMaster.status = true;
            }else{
           
            growl.error('Something Went Wrong');
          }
          });
    };

    //Method to add or update the VendorMaster
    function SaveVendorMaster() {
      if (ValidateVendor()) {
        if (!self.model.id) {
        	createVendorMaster();
        } else {
          updateVendorMaster();
        }
      }
    }
    var ValidateVendorName = function() {
      var isValid = true;
      if (/[^ a-z A-Z \.]/.test(self.model.vendorName)) {
        growl.error("Invalid Vendor Name");
        isValid = false;
      }
      if (/[^ a-z A-Z \.]/.test(self.model.contactPersonName)) {
        growl.error("Invalid Contact Person Name");
        isValid = false;
      }
      return isValid;
    };
    var ValidateCode = function() {
      var isValid = true;

      self.model.vendorMasterDataList.forEach(function(element) {
        if (element.code == self.model.vendorCode) {
          growl.error("Duplicate Vendor code");
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.vendorCode)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        return;
      }
      if (self.model.zipCode < 6) {
        growl.error("Enter Valid Zip code.");
        return;
      }
      if(self.model.zipCode < 6){
        growl.error('Enter Valid Zip code.');
        return;
       }
      return isValid;
    };

    var ValidateGst = function() {
      var isValid = true;
      return isValid;
    };

    var ValidateLicNo = function() {
      var isValid = true;
      return isValid;
    };

    var ValidateCodeForUpdate = function() {
      var isValid = true;

      self.model.vendorMasterDataList.forEach(function(element) {
        if (element.id != self.model.activeVendorMasterId) {
          if (element.code == self.model.vendorCode) {
            growl.error("Duplicate Vendor code");
            isValid = false;
          }
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.vendorCode)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        return;
      }
      
      if (parseInt(self.model.phoneNumber.length) < self.model.mobileDigit) {
        growl.error("Enter Valid Phone no.");
        return;
      }
      return isValid;
    };

    var ValidateEmail = function() {
     
       return true;
    };

    //Method to add the VendorMaster
    function createVendorMaster() {
      if (
        ValidateCode() &&
        ValidateEmail() &&
        ValidateVendorName() &&
        ValidateGst() &&
        ValidateLicNo()&&
        checkPhoneNumDigit()
      ) {
        var URI =
          CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API;
        var data = {
          code: self.model.vendorCode,
          name: self.model.vendorName,
          address: self.model.address,
          countryId: self.model.countryName.countryId,
          stateId: self.model.stateName.stateId,
          cityId: self.model.cityName.cityId,
          areaId: self.model.areaName.areaId,
          zipcode: self.model.zipCode,
          contactPersonName: self.model.contactPersonName,
          creditDays: self.model.creditDays,  
          drugLicenseNo: self.model.drugLicenseNo,
          // manufacturerId: self.model.manufactureName.id,
          manufacturerIds: [],
          paymentModeId: self.model.modeOfPayment.paymentModeId,
          gstNo: self.model.gstNo,
          phoneNumber: self.model.phoneNumber,
          emailId: self.model.emailAddress,
          paymentTerm: self.model.paymentTerms,
          isLocalPurchase: self.model.isLocalPurchase,
          status: true,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          userId:$scope.userId,
          generalLedgerId :self.model.generalLedgerId
        };
      /*  angular.forEach($scope.selectedManufacturesList, function(dataManu){
          var obj = {
            id: ''
          }
          obj.id = dataManu.id;
          data.manufacturerIds.push(obj);
        });*/
        GenericService.serviceAction("POST", URI, data).then(
          function(response) {
            // var manufacturerName = self.model.Manufactures.find(function(
            //   manufactures
            // ) {
            //   return manufactures.id == self.model.manufactureName.id;
            // });
            var paymentModes = self.model.modeOfPayments.find(function(
              paymentMode
            ) {
              return (
                paymentMode.paymentModeId ==
                self.model.modeOfPayment.paymentModeId
              );
            });
            var countryName = self.model.Countries.find(function(country) {
              return country.countryId == self.model.countryName.countryId;
            });
            var stateName = self.model.States.find(function(state) {
              return state.stateId == self.model.stateName.stateId;
            });
            var cityName = self.model.Cities.find(function(city) {
              return city.cityId == self.model.cityName.cityId;
            });
            var areaName = self.model.Areas.find(function(area) {
              return area.areaId == self.model.areaName.areaId;
            });
            var insertedData = {
              id: response.data.id,
              // manufacturerName: manufacturerName.name,
              name: self.model.vendorName,
              code: self.model.vendorCode,
              contactPersonName: self.model.contactPersonName,
              creditDays: self.model.creditDays,  
              drugLicenseNo: self.model.drugLicenseNo,
              gstNo: self.model.gstNo,
              address: self.model.address,
              country: countryName.countryName,
              state: stateName.stateName,
              city: cityName.cityName,
              area: areaName.areaName,
              zipcode: self.model.zipCode,
              phoneNumber: self.model.phoneNumber,
              emailId: self.model.emailAddress,
              paymentModeDesc: paymentModes.paymentModeDesc,
              paymentTerm: self.model.paymentTerms,
              isLocalPurchase: self.model.isLocalPurchase,
              status: true
            };
            self.model.vendorMasterDataList.push(insertedData);
            PaginationService.searchData(self.model.vendorMasterDataList, self.model.pagination);
            PaginationService.setLastPage(self.model.pagination);
           // self.model.getAddressDetails();
            clearFields();
            $state.reload();
            if(response.data.code=="200"){
            	growl.success(response.data.message);
            }else{
            	growl.error(response.data.message);
            }
            
          },
          function(err) {
            $rootScope.stopLoader();
            growl.error("Something Went Wrong");
          }
        );
      }
    }

    
 /***********************************************************Document Upload --> Avishkar*******************************************************/
	
    $scope.getPatientDocumentList = function(obj)
    {
     
     $rootScope.startLoader();
     var data =
       {
       "patientId":0,
       "unitId":$scope.unitId,
       "organisationId":$scope.organizationId,
       "userId":undefined,
       "visitTypeId":undefined,
       "admissionId":undefined,
       "vendorId" : obj.id,
       
      }
     var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
     BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
       $scope.patientDocumentList = [];
       if (response.data.status == "success") 
       {
       	$scope.patientDocumentList = response.data.listObject;  
          
           $rootScope.stopLoader();
       }
       else if(response.data.status == "error")
        {
       	$rootScope.stopLoader();
        }
      });
    }
    
    
    $scope.vendorDocList = [];
	$scope.vendorDetails = {
		  encounterDocId : "",
		  admissionId:"",
		  visitTypeId:2,
		  patientId : "",
		  documentPath : "",
		  documentTypeId : "",
		  status : 'A',
		  createdDate : "",
		  createdBy : $scope.userId,
		  updatedDate : "",
		  updatedBy : $scope.userId,
		  unitId : $scope.unitId,
		  organisationId : $scope.organizationId,
		  file : {},
          remark : "",
          id : ""
		}
	var showDocUPloadPoup   = function(vendorObj)
	{
		 $('#fileId').val(undefined);
		$scope.vendorDocList=[];
		$scope.vendorDetails.id = vendorObj.id;	
		if($scope.vendorDocList.length>0)
		{
		  $('#uploadDetails').modal('show');
		}
	else{
		 $('#uploadDetails').modal('show');
		var vendorDetails = angular.copy($scope.vendorDetails);
		vendorDetails.id = vendorObj.id;
		$scope.vendorDocList.push(vendorDetails);
	}
		$scope.getPatientDocumentList(vendorObj);
			
	}
	$scope.addImageDetails = function()
	{ 
		var vendorDetails = angular.copy($scope.vendorDetails);
		$scope.vendorDocList.push(vendorDetails);
	};

  $scope.removeImageDetails = function(deleteIndex)
	{ 
	   $scope.vendorDocList.splice(deleteIndex, 1);
    }	
  
  $scope.getDocumentType = function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.documentTypeList = [];
				if (response.data.status == "success") {
					$scope.documentTypeList = response.data.listObject;
				}
			});
	}
	$scope.getDocumentType();
	
	 $scope.uploadPatientImage = function()
	    {
		  $scope.finalDocUploadList=[];
		  var count=0;
		 angular.forEach($scope.vendorDocList, function(value, key) {  
			  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
					 growl.warning("Please select document type .",
												{
													title : 'warning!'
												});
									   $rootScope.stopLoader();
					return false;
				}
			  if (angular.equals(value.file, {}) || value.file==undefined) {
					growl.warning('Please select file.',
									{
										title : 'warning!'
									});
					 $rootScope.stopLoader();
					return false;
				}
//				 var ext = value.file.name.split(".").pop().toLowerCase();
//					if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
//						growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
//								{
//									title : 'warning!'
//								});
//						$rootScope.stopLoader();
//						return false;
//					}
					$scope.finalDocUploadList.push(value);
					 console.log("final list",$scope.finalDoc);
		});
		 
		 if($scope.vendorDocList.length==$scope.finalDocUploadList.length){
			   $('#uploadDetails').modal('hide');
		
		 $rootScope.startLoader();
		  $scope.count = 0;
		 angular.forEach($scope.vendorDocList, function(value, key) 
			{
				var promise = $scope.uploadEncounterDoc(value);
				promise.then(function(response)
						{
						  if(response.data.status == "success")
							  {
							    $scope.count++;
							    if($scope.vendorDocList.length==$scope.count)
								 {
								   growl.success("Document Uploaded Successfully.",
											{
												title : 'Success!'
											});
								   $scope.finalDocUploadList=[];
								   $scope.vendorDocList = [];
								   $rootScope.stopLoader();
								 }
						      }
						  else 
							{
								growl.error('Something went wrong.',
										{
											title : 'Error!'
										});
							}
					    });
					
			});
		 }
		
	    }
		 $scope.uploadEncounterDoc = function(Obj)
		{
			 var fd = new FormData();
		        fd.append('encounterId', 0);
		        fd.append('visitTypeId', 1);
		        fd.append('admissionId', 0);
		        fd.append('patientId', 0);
		        fd.append('documentTypeId', Obj.documentTypeId);
		        fd.append('createdBy',Obj.createdBy );
		        fd.append('updatedBy', Obj.updatedBy);
		        fd.append('unitId', Obj.unitId);
		        fd.append('organisationId', Obj.organisationId);
		        fd.append('expenseId', 0);
		        fd.append('file',Obj.file );
		        fd.append('expenseId', 0);
		        fd.append('grnId',0);
				fd.append('poId',0);
		        fd.append('incidentReportingMapperId',0);
		        fd.append('companyExpenseId',0);
                fd.append('remark', null);
                fd.append('createdDate',null);
				fd.append('fileEditedName',null);
				fd.append('opbId',0);
				fd.append('vendorId',Obj.id);
		        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
		        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
				{	
					return response;
				});
		}
    
    //Method to update the  VendorMaster
    function updateVendorMaster() {
      if (
        ValidateCodeForUpdate() &&
        ValidateVendorName() &&
        ValidateGst() &&
        ValidateLicNo()&&
         ValidateEmail() &&
         checkPhoneNumDigit()
      ) {
        var URI =
          CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API +
          self.model.id;
        var data = {
          code: self.model.vendorCode,
          name: self.model.vendorName,
          address: self.model.address,
          countryId: self.model.countryName.countryId,
          stateId:  self.model.stateName!=undefined? self.model.stateName.stateId :null,
          cityId: self.model.cityName!=undefined ? self.model.cityName.cityId:null,
          areaId: self.model.areaName.areaId !=undefined? self.model.areaName.areaId:null,
          zipcode: self.model.zipCode,
          contactPersonName: self.model.contactPersonName,
          creditDays: self.model.creditDays,  
          drugLicenseNo: self.model.drugLicenseNo,
          // manufacturerId: self.model.manufactureName.id,
          manufacturerIds: [],
          paymentModeId: self.model.modeOfPayment.paymentModeId,
          gstNo: self.model.gstNo,
          phoneNumber: self.model.phoneNumber,
          emailId: self.model.emailAddress,
          paymentTerm: self.model.paymentTerms,
          isLocalPurchase: self.model.isLocalPurchase,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          userId:$scope.userId,
          generalLedgerId :self.model.generalLedgerId
        };
      /*  angular.forEach($scope.selectedManufacturesList, function(dataManu){
          var obj = {
            id: ''
          }
          obj.id = dataManu.id;
          data.manufacturerIds.push(obj);
        });
        
         start code for resolved HIS-10048  Vendor Master : Updated Manufacturer Name not Getting Display. Need to Refresh Page 
        data.manufacturerId = data.manufacturerIds[0].id;*/
        /* end code for resolved HIS-10048  Vendor Master : Updated Manufacturer Name not Getting Display. Need to Refresh Page */
        
        GenericService.serviceAction("PATCH", URI, data).then(
          function(response) {
            // var manufacturerName = self.model.Manufactures.find(function(
            //   manufactures
            // ) {
            //   return manufactures.id == self.model.manufactureName.id;
            // });
            var paymentModes = self.model.modeOfPayments.find(function(
              paymentMode
            ) {
              return (
                paymentMode.paymentModeId ==
                self.model.modeOfPayment.paymentModeId
              );
            });
            var countryName = self.model.Countries.find(function(country) {
              return country.countryId == self.model.countryName.countryId;
            });
            var stateName = self.model.States.find(function(state) {
              return state.stateId == (self.model.stateName !=undefined ? self.model.stateName.stateId :null);
            });
            var cityName = self.model.Cities.find(function(city) {
              return city.cityId == (self.model.cityName !=undefined ? self.model.cityName.cityId :null);
            });
            var areaName = self.model.Areas.find(function(area) {
              return area.areaId == (self.model.areaName.areaId !=undefined ? self.model.areaName.areaId :null);
            });
            var vendorMaster = self.model.vendorMasterDataList.find(function(
              vendor
            ) {
              return vendor.id == self.model.id;
            });
            // vendorMaster.manufacturerName = manufacturerName.name;
            vendorMaster.name = data.name;
            vendorMaster.contactPersonName = data.contactPersonName;
            vendorMaster.creditDays = data.creditDays; 
            vendorMaster.code = data.code;
            vendorMaster.drugLicenseNo = data.drugLicenseNo;
            vendorMaster.gstNo = data.gstNo;
            vendorMaster.address = data.address;
            vendorMaster.country = countryName.countryName;
            vendorMaster.state = stateName.stateName;
            vendorMaster.city = cityName.cityName;
            vendorMaster.area = areaName.areaName;
            vendorMaster.zipcode = data.zipcode;
            vendorMaster.phoneNumber = data.phoneNumber;
            vendorMaster.emailId = data.emailId;
            vendorMaster.paymentModeDesc = paymentModes.paymentModeDesc;
            vendorMaster.paymentTerm = data.paymentTerm;
            vendorMaster.isLocalPurchase = data.isLocalPurchase;
            $state.reload();
            clearFields();
            growl.success(response.data.message);
            self.model.activeVendorMasterId = undefined;
            self.model.isEdit = false;
            getVendorMasterList();
          },
          function(err) {
            $rootScope.stopLoader();
            growl.error("Something Went Wrong");
          }
        );
      }
    }

    //Method to clear the fileds
    function clearFields() {
      self.model.vendorId = 0;
      self.model.manufactureName = self.model.Manufactures[0];
      self.model.countryName = self.model.Countries[0];
      self.model.stateName = self.model.States[0];
      self.model.States.splice(1);
      self.model.cityName = self.model.Cities[0];
      self.model.Cities.splice(1);
      self.model.areaName = self.model.Areas[0];
      self.model.Areas.splice(1);
      self.model.modeOfPayment = self.model.modeOfPayments[0];
      self.model.vendorName = "";
      self.model.vendorCode = "";
      self.model.contactPersonName = "";
      self.model.creditDays="";
      self.model.drugLicenseNo = "";
      self.model.gstNo = "";
      self.model.address = "";
      self.model.countryId = "";
      self.model.stateId = "";
      self.model.cityId = "";
      self.model.areaId = "";
      self.model.zipCode = "";
      self.model.phoneNumber = "";
      self.model.emailAddress = "";
      self.model.paymentModeId = "";
      self.model.paymentTerms = "";
      self.model.isLocalPurchase = false;
      self.model.id = undefined;
    }

    var clearSearch = function(){
      self.model.pagination.search = "";
      PaginationService.searchData(self.model.vendorMasterDataList, self.model.pagination);
    }

    function getAddressDetails(vendorMaster) {
      self.model.detail_address.address = vendorMaster.address;
      self.model.detail_address.country = vendorMaster.country;
      self.model.detail_address.state = vendorMaster.state;
      self.model.detail_address.city = vendorMaster.city;
      self.model.detail_address.area = vendorMaster.area;
      self.model.detail_address.zipcode = vendorMaster.zipcode;
    }

    // var validatePhoneNo = function(){
    //   if (/^\+?\d+$/ .test(self.model.phoneNumber)) {
    //     return true;
    //   }
    //   growl.error("Enter valid phoneNo");
    //   return false;
    // }
   var checkPhoneNumDigit = function(){
     self.model.mobileDigit = 10;
      if (self.model.mobileDigit == self.model.phoneNumber.length && /^\+?\d+$/ .test(self.model.phoneNumber)) {
        return true;
      }
      else{
        growl.error("Enter valid phone no");
        return false;
      }
     
    }
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
    var ValidateVendor = function() {
      var isValid = true;
     /* if ($scope.selectedManufacturesList.length == 0) {
        isValid = false;
        growl.error("Please select manufacturer");
        self.model.isManufacturerValid = true;
      }*/
      if (!self.model.vendorCode) {
        isValid = false;
        self.model.isvendorCodeValid = true;
      }
      if (!self.model.vendorName) {
        isValid = false;
        self.model.isvendorNameValid = true;
      }
      if (!self.model.contactPersonName) {
        isValid = false;
        self.model.iscontactPersonNameValid = true;
      }
     
     
      if (!self.model.address) {
        isValid = false;
        self.model.isaddressValid = true;
      }
      if (!self.model.countryName.countryId) {
        isValid = false;
        self.model.iscountryNameValid = true;
      }
     
     
      if (!self.model.phoneNumber) {
        isValid = false;
        self.model.isphoneNumberValid = true;
      }
      
      if (!self.model.modeOfPayment.paymentModeId) {
        isValid = false;
        self.model.ismodofPaymentValid = true;
      }
      if (!self.model.paymentTerms) {
        isValid = false;
        self.model.ispaymentTermsValid = true;
      }
      return isValid;
    };

    initializeController();
   
    $scope.setPostCode = function(areaId){
			 angular.forEach($scope.areaListWithZipCode,function(value,key){
					
					if(value.areaId == areaId)
					{
						 self.model.zipCode= value.postCode;
					}
				});
		}
    
    if($scope.unitCountryId > 0)
	{
      var vendorCountyObj = {};
  	  var vendorStateObj = {};
  	  var vendorCityObj = {};
  	  
  	  vendorCountyObj.countryId = $scope.unitCountryId;
  	  PopulateStates(vendorCountyObj, true);
  	  
  	  vendorStateObj.stateId = $scope.unitStateId;
      PopulateCities(vendorStateObj , true);
      
      vendorCityObj.cityId = $scope.unitCityId;
      PopulateAreas(vendorCityObj, true);
     
      $timeout(function () {
    	  //for country
    	  var unitCountry = self.model.Countries.find(function(countryObj) {
    		  return $scope.unitCountryId == countryObj.countryId;
    	  });
	  
    	  if(unitCountry!=undefined){
    		  self.model.countryName  = unitCountry;
    	  }
	  
    	  //for state
    	  var unitState = self.model.States.find(function(stateObj) {
    		  return $scope.unitStateId == stateObj.stateId;
    	  });
    	  if(unitState!=undefined){
    		  self.model.stateName = unitState;
    	  }
	  
    	  //for city
    	  var unitCity = self.model.Cities.find(function(cityObj) {
    		  return $scope.unitCityId == cityObj.cityId;
    	  });
    	  if(unitCity!=undefined){
    		  self.model.cityName = unitCity;
    	  }

    	  // for area 
    	  var unitArea = self.model.Areas.find(function(areaObj) {
    		  return $scope.unitAreaId == areaObj.areaId;
    	  });
    	  if(unitArea!=undefined){
    		  self.model.areaName = unitArea;
    	  }
      }, 3000); 
	  
	  // for zipcode
	  self.model.zipCode =  $scope.unitZipCode;
    	
	}
   
  }

  function config($stateProvider) {
    $stateProvider.state("vendormaster", {
      url: "/vendormaster",
      templateUrl: "views/PharmacyInventory/StockAdjustment/vendor-master.html",
      controller: "VendorMaster.Controller",
      title: 'Vendor Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 629);
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
    .controller("VendorMaster.Controller", vendorMasterController)
    .directive("showFocus", function($timeout) {
      return function(scope, element, attrs) {
        scope.$watch(
          attrs.showFocus,
          function(newValue, oldValue) {
            $timeout(function() {
              newValue != oldValue && element.focus();
            });
          },
          true
        );
      };
    });
})();
