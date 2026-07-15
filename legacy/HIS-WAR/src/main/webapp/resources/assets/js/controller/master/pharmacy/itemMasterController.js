(function () {
  "use strict";
  function itemMasterController(
    $rootScope,$scope,$state,$cookies,$http,
    CONSTANTS,
    GenericService,
    PaginationService,
    growl,
    commonDetailService,
    BillingGenericService
  ) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); 
    var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig'); 
    if(cookieObject == undefined){ 
    	$state.go('login'); return;
    	}
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId; 
    $scope.cimsOption = cookieObjectAppConfig.cimsOption;
    
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        itemDetails: commonDetailService.getDataId(),
        itemId: null,
        isitemReusable: false,
        isVaccine: false,
        isCombination: false,
        batchRequired: false,
        highRisk: false,
        psychotropic: false,
        isKit: false,
        isInfusion: false,
        isNarcotics: false,
        hospitalFormulary: false,
        isConsumable: false,
        isConsignment: false,
        isOTC: false,
        isEmergencyDrug:false,
        isInjection: false,
        threeUOMApplicable: true,
        isDoseCalculationApplicable: false,
        minqty: 1,
        maxqty: 10000000,
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '100',
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
        assetTypes: [
          {
            type: "Select Asset Type"
          }
        ],
        assetCategories: [
          {
            category: "Select Asset Category"
          }
        ],
        productCategories: [
          {
            category: "Select Product Category"
          }
        ],
        formulationTypes: [
          {
            type: "Select Formulation Type"
          }
        ],
        Generics: [{}],
        Units: [
          {
            unit: "Select Unit"
          }
        ],
        Manufactures: [{}],
        Pharmalogicals: [{}],
        PurchaseTaxes: [
          {
            type: "Select Tax"
          }
        ],
        SaleTaxes: [
          {
            type: "Select Tax"
          }
        ],
        StorageUnits: [
          {
            unit: "Select Storage Unit"
          }
        ],
        ispQuantity: [],
        issQuantity: [],
        isdQuantity: [],
      };
      self.model.SaveItemMaster = SaveItemMaster;
      self.model.PopulateItemMaster = PopulateItemMaster;
      self.model.PopulateAssetCategory = PopulateAssetCategory;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.PopulateFormulationType = PopulateFormulationType;
      self.model.PopulatePharamalogicalClassification = PopulatePharamalogicalClassification;
      self.model.PopulateManufacture = PopulateManufacture;
      self.model.PopulateGenerics = PopulateGenerics;
      self.model.GetItemDetails = GetItemDetails;
      self.model.ValidateItem = ValidateItem;
      self.model.ToggleStatus = ToggleStatus;
      self.model.ClearFields = ClearFields;
      self.model.PopulateDropdown = PopulateDropdown;
      self.model.PopulateDropdownStore = PopulateDropdownStore;
      self.model.clearSearch = clearSearch;
      self.model.GetGenericMaster = GetGenericMaster;
      self.model.reusableItem = reusableItem;
      self.model.addDoseCalculationData = addDoseCalculationData;
      self.model.itemDetails = [];
      self.model.uomUnits = [];
      self.model.selectUOM = selectUOM;
      self.model.uomUnits = [];
      self.model.itemObject = {
      		itemId:null,
      		puom : null,
      		purchaseQuantity : null,
      		suom : null,
      		stockQuantity : null,
      		duom : null,
      		dispenseQuantity : 1,
      		ipuom: null,
      		opuom :null,
      		storeuom : null,
        }
    

      self.model.Item_Details = {
        code: "",
        manufacture: "",
        salesTax: "",
        storageUnit: "",
        isVaccine: false,
        isCombination: false,
        isHighrisk: false,
        isKit: false,
        isNarcotic: false,
        pharmacologicalClassification: "",
        purchaseTax: false,
        hsnCode: "",
        cimsCode : "",
        cimsType :"",
        isReusable: false,
        isOtc: false,
        isEmergencyDrug :false,
        isBatchRequired: false,
        isPsychotropic: false,
        isInfusion: false,
        threeUOMApplicable: false,
        isHospitalFormulary: false
      };
      self.model.GetStrengthUnitList = GetStrengthUnitList;
      self.model.ViewCombinationDetails = ViewCombinationDetails;
      self.model.reusableCount = reusableCount;
      self.model.applyDoseCalculation = applyDoseCalculation;
      PopulateDropdown();
      PopulateDropdownStore();
      PopulateAssetCategory(1);
      GetUOMUnitList();
      
      //code uom
      if(self.model.threeUOMApplicable == true){
        self.model.uomTypes = [
          {
        	  "id": 3,
              "uomType": "Dispense" 
            },
            {
              "id": 1,
              "uomType": "Purchase"
            },
            {
              "id": 2,
              "uomType": "Stock"
          }
        ]
      }
      else{  
        self.model.uomTypes = [
          {
            "id": 1,
            "uomType": "Purchase"
          },
          {
            "id": 3,
            "uomType": "Dispense"
          }
        ]
      }
      
      self.model.itemObject.uomList = [{
          "uomTypeId": 1,
          "uomUnitId": 1,
          "conversion": null,
          "ipUom": true,
          "opUom": true,
          "storeUom": true,
          "status": true,
          "id":''
        }, {
          "uomTypeId": 2,
          "uomUnitId": 1,
          "conversion": null,
          "ipUom": false,
          "opUom": false,
          "storeUom": false,
          "status": true,
          "id":''
        }, {
          "uomTypeId": 3,
          "uomUnitId": 1,
          "conversion": null,
          "ipUom": false,
          "opUom": false,
          "storeUom": false,
          "status": true,
          "id":''
        }
      ]
      self.model.itemDetails.push(self.model.itemObject);
      $scope.selectedStoreList=[];
    }

    $scope.selectedStoreList = [];
    $scope.storeMultiSelectList =[];
    var PopulateDropdownStore = function() {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
      var data = "";
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        if (response.status == 200) {
          $scope.storeList = response.data.deliveryStores;    
          angular.forEach(response.data.deliveryStores,
              function(value,index) {
                var data = {};
                data.id = value.id;
                data.label = value.store;
                $scope.storeMultiSelectList.push(data);
          });
        }
      });	
    }
    $scope.selectedStoreList1=[];
    $scope.yourEvents = {
      onItemSelect: function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);
      },
      onItemDeselect: function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);
    	  $scope.selectedStoreList1=$scope.selectedStoreList;
    	  $scope.checkTransactionWithStore($scope.selectedStoreList);
      },
      onDeselectAll:function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);
    	  $scope.selectedStoreList=[];
    	  $scope.checkTransactionWithStore($scope.selectedStoreList);
      },
      onSelectAll:function(selectedValue) {
        // GetStoreGenericList($scope.selectedStoreList);  
      },
    }

    $scope.example6settings = {
      enableSearch: true,
      scrollableHeight: '150px', 
      scrollable: true
    }

    var selectUOM = function (item) {
        
        item.puom = self.model.uomUnits[0];
        
        item.uomList[0].uomUnitId = item.puom.id;
        
        item.suom = self.model.uomUnits[0];
        
        item.uomList[1].uomUnitId = item.suom.id;
      
        item.duom = self.model.uomUnits[0];
        
        item.uomList[2].uomUnitId = item.duom.id;
        
        item.ipuom = self.model.uomTypes[1];
        item.opuom = self.model.uomTypes[1];
        item.storeuom = self.model.uomTypes[1];
        
        
        $scope.ChangeUOMTypeNew(item, 'ipuom');
      }
    
    
    var GetUOMUnitList = function () {
        var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId;

        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.uomUnits = response.data;
            selectUOM(self.model.itemObject);
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
    
    var reusableItem = function () {
      angular.element('#itemReusable1').modal('show');
    }
    
    /**********For dose calculations********/
    var applyDoseCalculation=function(){
    	 angular.element('#doseCalculationApplicableModal').modal('show');
    }
    /*******************/
    var reusableCount = function () {
      self.model.reusablePercentage = [];
       
      for(var i=1; i <= self.model.count; i++){
    	  $scope.obj = {
    		        'patient': 0
    		      }
        self.model.reusablePercentage.push($scope.obj);
      }
      console.log('', self.model.reusablePercentage);
    }
    
   

    $scope.searchMedicationFreItems = function(searchKeyword) {

		$scope.itemList = [];
		var unitId = $scope.unitId
		var orgId = $scope.organizationId

		var URI = BASE_URL + ROOT_URL
				+ "/api/opd/getFrequencyList"
				+ "?unitId=" + unitId + "&orgId="
				+ orgId + "&searchPar=" + searchKeyword
		return BillingGenericService
				.serviceAction(METHOD_GET, URI, "",
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success") {
								$scope.itemList = response.data.listObject;
							}
							if($scope.itemList.length > 0)
								{
								return $scope.itemList;
								}
							else{
								$scope.medicationObj.searchItemFre='';
								$scope.medicationObj.frequencyId='';
							}
							
						});
	}
    
	$scope.selectedFrequencyItemList = function($item, $model, $label, idx) {
		$scope.obj.searchItemFre=$item.frequency;
		$scope.obj.frequencyForDoseCal = $item.frequency_id;
		$scope.obj.noOfAdministrationTimeForDoseCal = $item.noOfAdministrationTime;
	}
	
	$scope.obj = {
	        	'strengthForDoseCal': '',
		        'doseForDoseCal': '',
		        'quantityForDoseCal': '', 
		        'frequencyForDoseCal': '',
		        'searchItemFre': '',
		        'durationForDoseCal': '',
		        'durationCountForDoseCal': '',
		        'noOfAdministrationTimeForDoseCal':''
		      }
	
	
	 var addDoseCalculationData = function () {
			self.model.doseCalculationData = [];
	      	self.model.doseCalculationData.push($scope.obj);
	      }
	
    var PopulateDropdown = function () {
      $rootScope.startLoader();
      var itemMaster = self.model;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DROPDOWN_API +"/"+ $scope.organizationId + "/"+ $scope.unitId ;
      // itemMaster.manufactureName = itemMaster.Manufactures[0];
      // itemMaster.classificationName = itemMaster.Pharmalogicals[0];
      itemMaster.purchaseTax = itemMaster.PurchaseTaxes[0];
      itemMaster.saleTax = itemMaster.SaleTaxes[0];
      itemMaster.unit = itemMaster.StorageUnits[0];
      // itemMaster.assetType = itemMaster.assetTypes[0];
      itemMaster.formulationType = itemMaster.formulationTypes[0];
      itemMaster.unitName = itemMaster.Units[0];
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          GetItemMasterList();
          self.model.Manufactures = response.data.manufacturer;
          self.model.Pharmalogicals = response.data.pharmacologicalClassification;
          self.model.PurchaseTaxes = self.model.PurchaseTaxes.concat(response.data.tax);
          self.model.SaleTaxes = self.model.SaleTaxes.concat(response.data.tax);
          self.model.StorageUnits = self.model.StorageUnits.concat(response.data.storageUnit);
          self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
          self.model.formulationTypes = self.model.formulationTypes.concat(response.data.formulationType);
          self.model.Units = self.model.Units.concat(response.data.strengthUnit);
          self.model.assetType = self.model.assetTypes[1];
          var itemId = commonDetailService.getDataId();
          if (!!itemId) {
            commonDetailService.setDataId(null);
            GetItemName(itemId);
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });

    }

    var GetItemName = function (data) {
      self.model.assetType = self.model.assetTypes.find(function (dt) {
        return data.assetTypeId == dt.id;
      });
      PopulateAssetCategory(data,true);
      self.model.itemId=data.id;
      self.model.itemName = data.itemName;
      if (data.manufacturerId) {
        self.model.manufactureName = self.model.Manufactures.find(function (dt) {
          return data.manufacturerId == dt.id;
        });
      }
    }

    var GetItemDetails = function (itemMaster) {
      self.model.Item_Details.code = itemMaster.code;
      self.model.Item_Details.manufacture = itemMaster.manufacturer ? itemMaster.manufacturer : itemMaster.manufacture;
      self.model.Item_Details.salesTax = itemMaster.saleTax ? itemMaster.saleTax : itemMaster.salesTax;
      self.model.Item_Details.storageUnit = itemMaster.storageUnitName ? itemMaster.storageUnitName : itemMaster.storageUnit;
      self.model.Item_Details.isVaccine = itemMaster.isVaccine;
      self.model.Item_Details.isCombination = itemMaster.isCombination;
      self.model.Item_Details.isHighrisk = itemMaster.isHighrisk;
      self.model.Item_Details.isKit = itemMaster.isKit;
      self.model.Item_Details.isNarcotic = itemMaster.isNarcotic;
      self.model.Item_Details.pharmacologicalClassification = 
      itemMaster.pharmalogicalClassification ? itemMaster.pharmalogicalClassification : itemMaster.pharmacologicalClassification;
      self.model.Item_Details.purchaseTax = itemMaster.purchaseTax;
      self.model.Item_Details.hsnCode = itemMaster.hsnCode;
      self.model.Item_Details.cimsCode = itemMaster.cimsCode;
      self.model.Item_Details.cimsType = itemMaster.cimsType;
      self.model.Item_Details.isReusable = itemMaster.isReusable;
      self.model.Item_Details.isOtc = itemMaster.isOtc;
      self.model.Item_Details.isEmergencyDrug =itemMaster.isEmergencyDrug;
      self.model.Item_Details.isBatchRequired = itemMaster.isBatchRequired;
      self.model.Item_Details.isPsychotropic = itemMaster.isPsychotropic;
      self.model.Item_Details.isInfusion = itemMaster.isInfusion;
      self.model.Item_Details.threeUOMApplicable = itemMaster.threeUOMApplicable;
      self.model.Item_Details.isHospitalFormulary =itemMaster.isHospitalFormulary;
      self.model.Item_Details.isConsumable = itemMaster.isConsumable;
      self.model.Item_Details.isConsignment = itemMaster.isConsignment;
      self.model.Item_Details.isInjection = self.model.isInjection;
    };

    //Method to get the list of Vendors
    $scope.noOfRecordsPerPage=100;
    $scope.offset=0;
    $scope.getRecords = function()
    {
    	$scope.offset=0;
    	GetItemMasterList();
    }
    $scope.getPreviousRecords = function()
    {
    	if($scope.offset>=100)
    		{
    		$scope.offset=$scope.offset-100;
    		GetItemMasterList();
    		}
    }
    $scope.getNextRecords = function()
    {
		$scope.offset=$scope.offset+100;
		GetItemMasterList();
    }
    
    var GetItemMasterList = function () {
      $rootScope.startLoader();
      if(self.model.pagination.search=="")
    	  {
    		  self.model.pagination.search=null;
    	  }
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.SEARCH_API + "/" +$scope.organizationId +"/"+$scope.offset+"/"+$scope.noOfRecordsPerPage+"/"+self.model.pagination.search;

      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.ItemMasterDataList = response.data;
          
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.ItemMasterDataList, self.model.pagination);
          }

          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to populate VendorMaster for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getItemMasterById(id) {
      return findInRecordInArray(self.model.ItemMasterDataList, "id", id);
    }
    $scope.transactionalStoreList;
    $scope.newItemId;
    function PopulateItemMaster(id) {
    	$scope.newItemId=id;
      $rootScope.startLoader();
      $scope.isValidFlag=true;
      self.model.isassetTypeValid = false;
      self.model.isassetCategoryValid = false;
      self.model.isproductCatgeoryValid = false;
      self.model.isformulationTypeValid = false;
      self.model.isitemCodeValid = false;
      self.model.isproductCodeValid = false;
      self.model.isitemNameValid = false;
      self.model.isgenericNameValid = false;
      self.model.isstrengthValid = false;
      self.model.isunitValid = false;
      self.model.ispharmalogicalClassificationValid = false;
      self.model.ismanufacturerValid = false;
      self.model.ispurchaseTaxValid = false;
      self.model.issaleTaxValid = false;
      self.model.ishsnCodeValid = false;
      self.model.isstorageUnitValid = false;

      self.model.activeItemMasterId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(function (response) {
        var vendor = getItemMasterById(id);
        vendor = response.data;
        var assetType = self.model.assetTypes.find(function (assettype) {
          return response.data.assetTypeId == assettype.id;
        });
        var unitName = self.model.Units.find(function (unitname) {
          return response.data.strengthUnitId == unitname.id;
        });
        var classificationName = self.model.Pharmalogicals.find(function (
          classificationname
        ) {
          return (
            response.data.pharmalogicalClassificationId == classificationname.id
          );
        });
        if(classificationName!=undefined && classificationName!=null)
		  {
			self.model.classificationId = classificationName.id;  
		  }
        var manufactureName = self.model.Manufactures.find(function (
          manufacturename
        ) {
          return response.data.manufacturerId == manufacturename.id;
        });
        self.model.manufactureId = manufactureName.id;
        var purchaseTax = self.model.PurchaseTaxes.find(function (purchasetax) {
          return response.data.purchaseTaxId == purchasetax.id;
        });
        var saleTax = self.model.SaleTaxes.find(function (saletax) {
          return response.data.saleTaxId == saletax.id;
        });
        var storageUnit = self.model.StorageUnits.find(function (sunit) {
          return response.data.storageUnit == sunit.id;
        });
        var formulationType = self.model.formulationTypes.find(function (type) {
          return response.data.formulationTypeId == type.id;
        });
        PopulateAssetCategory(response.data, true);
        PopulateProductCategory(response.data, true);
        //  PopulateFormulationType(response.data, true);
        PopulateGenerics(response.data, true);
        $scope.selectedStoreList = [];
        $scope.list = [];
        $scope.NewList=[];
        $scope.NewList=response.data.storeItemMapperListResponse;
        $scope.transactionalStoreList=$scope.NewList;
        var uniqueLocations = _.uniq($scope.NewList, 'storeId');
        console.log("uniqueLocations", uniqueLocations);
        angular.forEach(uniqueLocations, function(d){
          var newArr={};
          newArr.id = d.storeId;
          $scope.selectedStoreList.push(newArr);
          newArr.id1 = d.id;
          $scope.list.push(newArr);
          self.model.minqty = d.minQty;
          self.model.maxqty = d.maxQty;
         
        });
        
        self.model.id = response.data.id;
        self.model.assetType = assetType;
        self.model.itemCode = response.data.code;
        self.model.productCode = response.data.productCode;
        self.model.itemName = response.data.name;
        self.model.strength = parseFloat(response.data.strength);
        self.model.classificationName = classificationName;
        self.model.manufactureName = manufactureName;
        self.model.purchaseTax = purchaseTax;
        self.model.saleTax = saleTax;
        self.model.unitName = unitName;
        self.model.unit = storageUnit;
        self.model.formulationType = formulationType;
        self.model.hsnCode = response.data.hsnCode;
        self.model.cimsCode = response.data.cimsCode;
        self.model.cimsType = response.data.cimsType;
        // self.model.strength = response.data.strength;
        self.model.status = response.data.status;
        self.model.storageUnit = storageUnit;
        self.model.isitemReusable = response.data.isReusable;
        self.model.isVaccine = response.data.isVaccine;
        self.model.isCombination = response.data.isCombination;
        self.model.batchRequired = response.data.isBatchRequired;
        self.model.highRisk = response.data.isHighrisk;
        self.model.psychotropic = response.data.isPsychotropic;
        self.model.isKit = response.data.isKit;
        self.model.isInfusion = response.data.isInfusion;
        self.model.threeUOMApplicable = response.data.isThreeUOMApplicable;
        self.model.isNarcotics = response.data.isNarcotic;
        self.model.hospitalFormulary = response.data.isHospitalFormulary;
        self.model.isConsumable = response.data.isConsumable;
        self.model.isConsignment = response.data.isConsignment;
        self.model.isInjection = response.data.isInjection;
        self.model.isOTC = response.data.isOtc;
        self.model.isEmergencyDrug = response.data.isEmergencyDrug;
        self.model.isLasa = response.data.isLasa;
        self.model.isEdit = true;
        self.model.count = response.data.reusableCount;
        self.model.reusablePercentage = response.data.reusablePercentage1 != undefined ? JSON.parse(response.data.reusablePercentage1): null;
        self.model.isDoseCalculationApplicable=response.data.isDoseCalculationApplicable;
        if(self.model.isDoseCalculationApplicable != undefined && (response.data.doseCalculationApplicableData != undefined && response.data.doseCalculationApplicableData.length > 0)){
        	 self.model.doseCalculationApplicableData1=JSON.parse(response.data.doseCalculationApplicableData);
             angular.forEach(self.model.doseCalculationApplicableData1, function(val, key){
             	$scope.obj.quantityForDoseCal=val.quantityForDoseCal;
             	$scope.obj.doseForDoseCal=val.doseForDoseCal;
             	$scope.obj.frequencyForDoseCal=val.frequencyForDoseCal;
             	$scope.obj.durationForDoseCal= val.durationForDoseCal;
             	$scope.obj.durationCountForDoseCal= val.durationCountForDoseCal;
             	$scope.obj.strengthForDoseCal=val.strengthForDoseCal;
             	$scope.obj.searchItemFre=val.searchItemFre;
             	$scope.obj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal
             });
        }
       
      
        self.model.ChangeUOMType = ChangeUOMType;
        // self.model.reusablePercentage = response.data.reusablePercentage1;
        
        if(self.model.threeUOMApplicable == true) {
          self.model.uomTypes = [
            {
            	"id": 3,
                "uomType": "Dispense" 
              },
              {
                "id": 1,
                "uomType": "Purchase"
              },
              {
                "id": 2,
                "uomType": "Stock"
            }
          ]
        }
        else{  
          self.model.uomTypes = [
            {
              "id": 1,
              "uomType": "Purchase"
            },
            {
              "id": 3,
              "uomType": "Dispense"
            }
          ]
        }
        if(response.data.unitOfMeasurementDetailsResponse!=null)
        {
        	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){
	            var selectedipUom = item.uomList.find(function(uom){
	              return uom.ipUom == true;
	            });

	            var selectedopUom = item.uomList.find(function(uom){
	              return uom.opUom == true;
	            });

	            var selectedstoreUom = item.uomList.find(function(uom){
	              return uom.storeUom == true;
	            });

              self.model.itemObject.ipuom = self.model.uomTypes.find(function (data) {
                return data.id == selectedipUom.uomTypeId;
              });
              self.model.itemObject.opuom = self.model.uomTypes.find(function (data) {
                return data.id == selectedopUom.uomTypeId;
              });
              self.model.itemObject.storeuom = self.model.uomTypes.find(function (data) {
                return data.id == selectedstoreUom.uomTypeId;
              });

	            self.model.itemObject.purchaseQuantity = item["uomList"][0].conversion;
	            self.model.itemObject.stockQuantity = item["uomList"][1].conversion;
	            self.model.itemObject.dispenseQuantity = item["uomList"][2].conversion;
	            self.model.itemObject.uomList = item.uomList;
	            self.model.itemObject.puom = self.model.uomUnits.find(function(units){
	              return self.model.itemObject["uomList"][0].uomUnitId == units.id;
	            });

	            self.model.itemObject.suom = self.model.uomUnits.find(function(units){
	              return self.model.itemObject["uomList"][1].uomUnitId == units.id;
	            });

	            self.model.itemObject.duom = self.model.uomUnits.find(function(units){
	              return self.model.itemObject["uomList"][2].uomUnitId == units.id;
	            });

	            if (self.model.itemObject.uomList[0].id) {
	            	//self.model.itemObject["isDisabled"] = true;
	            }
	            
	            
	          });
        	}

        $rootScope.stopLoader();
      },
      function errorCallback(response) {
        $rootScope.stopLoader();
      });
    }

    //Method to Populate Asset Type Dropdown
    var PopulateAssetType = function () {

      $rootScope.startLoader();
      var itemMaster = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId ;

      itemMaster.assetType = itemMaster.assetTypes[0];
      itemMaster.assetCategory = itemMaster.assetCategories[0];
      itemMaster.productCategory = itemMaster.productCategories[0];
      // itemMaster.formulationType = itemMaster.formulationTypes[0];
      // itemMaster.genericName = itemMaster.Generics[0];

      itemMaster.assetTypes.splice(1);
      itemMaster.assetCategories.splice(1);
      itemMaster.productCategories.splice(1);
      // itemMaster.formulationTypes.splice(1);
      itemMaster.Generics.splice(1);
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.assetTypes = itemMaster.assetTypes.concat(response.data);
          // self.model.itemId = self.model.itemDetails.id ? self.model.itemDetails.id : null;
          self.model.itemName = self.model.itemDetails.itemName;
          self.model.itemCode = self.model.itemDetails.itemCode;
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };
    
    var ChangeUOMType = function (item, uomName) {
        if (uomName == 'ipuom') {
          if (item.ipuom.id == 1) {
            item.uomList[0].ipUom = true;
            item.uomList[1].ipUom = false;
            item.uomList[2].ipUom = false;
          } else if (item.ipuom.id == 2) {
            item.uomList[0].ipUom = false;
            item.uomList[1].ipUom = true;
            item.uomList[2].ipUom = false;
          } else if (item.ipuom.id == 3) {
            item.uomList[0].ipUom = false;
            item.uomList[1].ipUom = false;
            item.uomList[2].ipUom = true;
          }
        } else if (uomName == 'opuom') {
          if (item.opuom.id == 1) {
            item.uomList[0].opUom = true;
            item.uomList[1].opUom = false;
            item.uomList[2].opUom = false;
          } else if (item.opuom.id == 2) {
            item.uomList[0].opUom = false;
            item.uomList[1].opUom = true;
            item.uomList[2].opUom = false;
          } else if (item.opuom.id == 3) {
            item.uomList[0].opUom = false;
            item.uomList[1].opUom = false;
            item.uomList[2].opUom = true;
          }
        } else if (uomName == 'storeuom') {
          if (item.storeuom.id == 1) {
            item.uomList[0].storeUom = true;
            item.uomList[1].storeUom = false;
            item.uomList[2].storeUom = false;
          } else if (item.storeuom.id == 2) {
            item.uomList[0].storeUom = false;
            item.uomList[1].storeUom = true;
            item.uomList[2].storeUom = false;
          } else if (item.storeuom.id == 3) {
            item.uomList[0].storeUom = false;
            item.uomList[1].storeUom = false;
            item.uomList[2].storeUom = true;
          }
        }
        
        console.log(item.uomList);
      }
    
    $scope.ChangeUOMTypeNew = function (item, uomName) {
        if (uomName == 'ipuom') {
          if (item.ipuom.id == 1) {
            item.uomList[0].ipUom = true;
            item.uomList[1].ipUom = false;
            item.uomList[2].ipUom = false;
          } else if (item.ipuom.id == 2) {
            item.uomList[0].ipUom = false;
            item.uomList[1].ipUom = true;
            item.uomList[2].ipUom = false;
          } else if (item.ipuom.id == 3) {
            item.uomList[0].ipUom = false;
            item.uomList[1].ipUom = false;
            item.uomList[2].ipUom = true;
          }
          
          if (item.opuom.id == 1) {
              item.uomList[0].opUom = true;
              item.uomList[1].opUom = false;
              item.uomList[2].opUom = false;
            } else if (item.opuom.id == 2) {
              item.uomList[0].opUom = false;
              item.uomList[1].opUom = true;
              item.uomList[2].opUom = false;
            } else if (item.opuom.id == 3) {
              item.uomList[0].opUom = false;
              item.uomList[1].opUom = false;
              item.uomList[2].opUom = true;
            }

          if (item.storeuom.id == 1) {
            item.uomList[0].storeUom = true;
            item.uomList[1].storeUom = false;
            item.uomList[2].storeUom = false;
          } else if (item.storeuom.id == 2) {
            item.uomList[0].storeUom = false;
            item.uomList[1].storeUom = true;
            item.uomList[2].storeUom = false;
          } else if (item.storeuom.id == 3) {
            item.uomList[0].storeUom = false;
            item.uomList[1].storeUom = false;
            item.uomList[2].storeUom = true;
          }
        
          
        } else if (uomName == 'opuom') {
            if (item.ipuom.id == 1) {
                item.uomList[0].ipUom = true;
                item.uomList[1].ipUom = false;
                item.uomList[2].ipUom = false;
              } else if (item.ipuom.id == 2) {
                item.uomList[0].ipUom = false;
                item.uomList[1].ipUom = true;
                item.uomList[2].ipUom = false;
              } else if (item.ipuom.id == 3) {
                item.uomList[0].ipUom = false;
                item.uomList[1].ipUom = false;
                item.uomList[2].ipUom = true;
              }
              
              if (item.opuom.id == 1) {
                  item.uomList[0].opUom = true;
                  item.uomList[1].opUom = false;
                  item.uomList[2].opUom = false;
                } else if (item.opuom.id == 2) {
                  item.uomList[0].opUom = false;
                  item.uomList[1].opUom = true;
                  item.uomList[2].opUom = false;
                } else if (item.opuom.id == 3) {
                  item.uomList[0].opUom = false;
                  item.uomList[1].opUom = false;
                  item.uomList[2].opUom = true;
                }

              if (item.storeuom.id == 1) {
                item.uomList[0].storeUom = true;
                item.uomList[1].storeUom = false;
                item.uomList[2].storeUom = false;
              } else if (item.storeuom.id == 2) {
                item.uomList[0].storeUom = false;
                item.uomList[1].storeUom = true;
                item.uomList[2].storeUom = false;
              } else if (item.storeuom.id == 3) {
                item.uomList[0].storeUom = false;
                item.uomList[1].storeUom = false;
                item.uomList[2].storeUom = true;
              }
            
              
            } else if (uomName == 'storeuom') {
                if (item.ipuom.id == 1) {
                    item.uomList[0].ipUom = true;
                    item.uomList[1].ipUom = false;
                    item.uomList[2].ipUom = false;
                  } else if (item.ipuom.id == 2) {
                    item.uomList[0].ipUom = false;
                    item.uomList[1].ipUom = true;
                    item.uomList[2].ipUom = false;
                  } else if (item.ipuom.id == 3) {
                    item.uomList[0].ipUom = false;
                    item.uomList[1].ipUom = false;
                    item.uomList[2].ipUom = true;
                  }
                  
                  if (item.opuom.id == 1) {
                      item.uomList[0].opUom = true;
                      item.uomList[1].opUom = false;
                      item.uomList[2].opUom = false;
                    } else if (item.opuom.id == 2) {
                      item.uomList[0].opUom = false;
                      item.uomList[1].opUom = true;
                      item.uomList[2].opUom = false;
                    } else if (item.opuom.id == 3) {
                      item.uomList[0].opUom = false;
                      item.uomList[1].opUom = false;
                      item.uomList[2].opUom = true;
                    }

                  if (item.storeuom.id == 1) {
                    item.uomList[0].storeUom = true;
                    item.uomList[1].storeUom = false;
                    item.uomList[2].storeUom = false;
                  } else if (item.storeuom.id == 2) {
                    item.uomList[0].storeUom = false;
                    item.uomList[1].storeUom = true;
                    item.uomList[2].storeUom = false;
                  } else if (item.storeuom.id == 3) {
                    item.uomList[0].storeUom = false;
                    item.uomList[1].storeUom = false;
                    item.uomList[2].storeUom = true;
                  }
                
                  
                }
        
        console.log(item.uomList);
      }

    //Method to Populate Asset Category Dropdown
    var PopulateAssetCategory = function (assetType, selectedvalue) {
      var assettypeId = 0;
      if(assetType){
        assettypeId = assetType;
      }
      if (!!assetType.assetTypeId) {
        assettypeId = assetType.assetTypeId
      }
      else if (!!assetType.id) {
        assettypeId = assetType.id
      }
      if (selectedvalue) {
        assettypeId = assetType.assetTypeId;
      }
      var itemMaster = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + assettypeId +"/"+$scope.unitId + "/"+$scope.organizationId;;

      itemMaster.assetCategory = itemMaster.assetCategories[0];
      itemMaster.productCategory = itemMaster.productCategories[0];
      //  itemMaster.formulationType = itemMaster.formulationTypes[0];
      // itemMaster.genericName = itemMaster.Generics[0];

      itemMaster.assetCategories.splice(1);
      itemMaster.productCategories.splice(1);
      // itemMaster.formulationTypes.splice(1);
      itemMaster.Generics.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        

          if (selectedvalue) {
            if(itemMaster.assetCategories.id){
              var assetCategory = itemMaster.assetCategories.find(function (
                assetcategory
              ) {
                return assetType.assetCategoryId == assetcategory.id;
              });
              self.model.assetCategory = assetCategory;
            }
            else{
              itemMaster.assetCategories = itemMaster.assetCategories.concat(
                response.data
              );
              var assetCategory = itemMaster.assetCategories.find(function (
                assetcategory
              ) {
                return assetType.assetCategoryId == assetcategory.id;
              });
              self.model.assetCategory = assetCategory;
            }
          }
          else{
            itemMaster.assetCategories = itemMaster.assetCategories.concat(
              response.data
            );
          }

          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to Populate Product Category Dropdown
    var PopulateProductCategory = function (assetCategory, selectedvalue) {
      var assetcategoryId = 0;
      if (!!assetCategory.assetCategoryId) {
        assetcategoryId = assetCategory.assetCategoryId
      }
      else if (!!assetCategory.id) {
        assetcategoryId = assetCategory.id
      }
      if (selectedvalue) {
        assetcategoryId = assetCategory.assetCategoryId;
      }
      var itemMaster = self.model;

      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + assetcategoryId + '/'+$scope.organizationId;

      itemMaster.productCategory = itemMaster.productCategories[0];
      //   itemMaster.formulationType = itemMaster.formulationTypes[0];
      // itemMaster.genericName = itemMaster.Generics[0];

      itemMaster.productCategories.splice(1);
      //  itemMaster.formulationTypes.splice(1);
      itemMaster.Generics.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.productCategories = itemMaster.productCategories.concat(
            response.data
          );
          $rootScope.stopLoader();

          if (selectedvalue) {
            var productCategory = itemMaster.productCategories.find(function (
              productcategory
            ) {
              return assetCategory.productCategoryId == productcategory.id;
            });
            self.model.productCategory = productCategory;
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };



    //Method to Populate Formulation Type Dropdown
    var PopulateFormulationType = function () {
      var itemMaster = self.model;

      itemMaster.formulationType = itemMaster.formulationTypes[0];
      // itemMaster.genericName = itemMaster.Generics[0];

      itemMaster.formulationTypes.splice(1);
      itemMaster.Generics.splice(1);

      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.FORMULATION_TYPE_API + $scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.formulationTypes = itemMaster.formulationTypes.concat(
            response.data
          );
          $rootScope.stopLoader();

          // if (selectedvalue) {
          //   var formulationType = itemMaster.formulationTypes.find(function (formulationtype) {
          //     return itemMaster.formulationTypeId == formulationtype.id;
          //   });
          //   self.model.formulationType = formulationType;
          // }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to Populate Generic Dropdown
    var PopulateGenerics = function (productCategory, selectedvalue) {
      var productcategoryId = 0;
      self.model.genericId = undefined;
      if (!!self.model.productCategory.productcategoryId) {
        productcategoryId = self.model.productCategory.productcategoryId
      }
      else if (!!self.model.productCategory.id) {
        productcategoryId = self.model.productCategory.id
      }

      if (selectedvalue) {
        productcategoryId = productCategory.productCategoryId;
        self.model.genericName = productCategory.genericName;
      }
      var itemMaster = self.model;

      // itemMaster.genericName = itemMaster.Generics[0];

      itemMaster.Generics.splice(1);

      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.GENERIC_API + $scope.organizationId + "/"+ productcategoryId + "/" + self.model.genericName;

      $rootScope.startLoader();
      return GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.Generics = response.data;
          $rootScope.stopLoader();

          if (selectedvalue) {
            var genericName = itemMaster.Generics.find(function (genericname) {
              return productCategory.genericId == genericname.id;
            });
            self.model.genericName = genericName;
            self.model.genericId = productCategory.genericId;
          }
          return itemMaster.Generics;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    $scope.AddGeneric = function(data, item, model, label, index){
      self.model.genericId = item.id;
    }
 $scope.genericName1=function(){
 console.log("work");
 }

    $scope.isValidFlag=true;
	$scope.checkTransactionWithStore=function()
	{
		
        var ids=[];
        var storeIds=[];
        console.log("$scope.selectedStoreList",$scope.selectionFlag,$scope.selectedStoreList1,$scope.transactionalStoreList);
        for (let generic of $scope.selectedStoreList1){
            ids.push(generic.id);
          }  
        for (let generic of $scope.transactionalStoreList){
            if(!ids.includes(generic.storeId)){
            	var data={
            			"id":generic.storeId
            	}
                storeIds.push(data);
            }
          }   
        
		var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.STORE_ITEM_MAPPER.MULTIPLE_STORE_ITEM_MAPPER_DROPDOWN_API +"/"+$scope.newItemId;
        $rootScope.startLoader();
        GenericService.serviceAction("POST",URI,storeIds).then(function(response){
          $scope.storeNameToDisplay="";
          var counter=0;
          if(!!response && !!response.data && response.data.length > 0){
        		  for (var j = 0; j < response.data.length; j++) {
        			  for (var i = 0; i < $scope.transactionalStoreList.length; i++) {
        	        if($scope.transactionalStoreList[i].storeId==response.data[j][1] && response.data[j][0]>0)
        	        	{
        	        	counter++;
        	        	$scope.storeNameToDisplay=$scope.storeNameToDisplay+","+$scope.transactionalStoreList[i].storeName;
        	        	}
        	        	}
        	      } 
        	  if(counter>=1)
        		  {
        		  growl.error("Can Not Unmap "+$scope.storeNameToDisplay);
  	        	  $rootScope.stopLoader();
  	        	$scope.isValidFlag=false;
        		  }
          }
          if(storeIds && response.data.length==0)
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
        
        );
//        return isValidFlag;
	}
    //Method to Populate Unit Dropdown
    var PopulateUnit = function () {
      var itemMaster = self.model;

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API+ '/'+$scope.organizationId;

      itemMaster.unitName = itemMaster.Units[0];

      itemMaster.Units.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.Units = itemMaster.Units.concat(response.data);
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to Populate Storage Unit Dropdown
    var PopulateStorageUnit = function () {
      var itemMaster = self.model;

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.STORAGE_API +$scope.organizationId;

      itemMaster.unit = itemMaster.StorageUnits[0];

      itemMaster.StorageUnits.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.StorageUnits = itemMaster.StorageUnits.concat(
            response.data
          );
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to Populate Manufacture Dropdown
    var PopulateManufacture = function () {
      var itemMaster = self.model;
       self.model.manufactureId = undefined;

      var URI =  CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DROPDOWN_API +"Manufacture/"+ $scope.organizationId + "/" + $scope.unitId +"/"+ self.model.manufactureName;

      // itemMaster.manufactureName = itemMaster.Manufactures[0];

//      itemMaster.Units.splice(1);

      $rootScope.startLoader();
      return GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.Manufactures = response.data.manufacturer;
          $rootScope.stopLoader();
          return itemMaster.Manufactures;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    $scope.AddManufacture = function(data, item, model, label, index){
      self.model.manufactureId = item.id;
    }

    //Method to Populate Pharamalogical Classification Dropdown
    var PopulatePharamalogicalClassification = function () {
      var itemMaster = self.model;
      self.model.classificationId = undefined;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DROPDOWN_API +"/"+ $scope.organizationId + "/" + $scope.unitId +"/"+ self.model.classificationName;

      // itemMaster.classificationName = itemMaster.Pharmalogicals[0];

      itemMaster.Pharmalogicals.splice(1);

      $rootScope.startLoader();
      return GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.Pharmalogicals = response.data.pharmacologicalClassification;
          $rootScope.stopLoader();
          return itemMaster.Pharmalogicals;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    $scope.AddPharmacological = function(data, item, model, label, index){
      self.model.classificationId = item.id;
    }

    //Method to Populate Purchase and Sale Tax Dropdown
    var PopulateTax = function () {
      var itemMaster = self.model;

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.TAX_API+$scope.organizationId;

      itemMaster.purchaseTax = itemMaster.PurchaseTaxes[0];
      itemMaster.saleTax = itemMaster.SaleTaxes[0];

      itemMaster.Pharmalogicals.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          itemMaster.PurchaseTaxes = itemMaster.PurchaseTaxes.concat(
            response.data
          );
          itemMaster.SaleTaxes = itemMaster.SaleTaxes.concat(response.data);
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    
    var ValidateUOM = function (){
      var isValidItem = true;
      for (var i = 0; i < self.model.itemDetails.length; i++) {
        if(self.model.threeUOMApplicable == false){
          self.model.itemDetails[i].stockQuantity = 1;
        }
        if(parseInt(self.model.itemDetails[i].purchaseQuantity)>0 && parseInt(self.model.itemDetails[i].stockQuantity)>0 && parseInt(self.model.itemDetails[i].dispenseQuantity)>0){
          return isValidItem = true;
        }
        else if(!parseInt(self.model.itemDetails[i].purchaseQuantity)>0 && !parseInt(self.model.itemDetails[i].stockQuantity)>0 && !parseInt(self.model.itemDetails[i].dispenseQuantity)>0)
        {
          return isValidItem = true;
        }
        else{
          growl.error('All fields are required');
          isValidItem = false;
        }
      }
      return isValidItem;
    }

    var SaveItemMaster = function () {
      if (ValidateItem()  && ValidateUOM() && $scope.validateMaxqty()) {
        if (!self.model.id) {
          CreateItem();
        } else {
          UpdateItem();
        }
      }
    };

    var ValidateItemCode = function () {
      var isValid = true;

    /*  self.model.ItemMasterDataList.forEach(function (element) {
        if (element.itemCode == self.model.itemCode) {
          growl.error("Duplicate Item code");
          isValid = false;
        }
      });*/

      return isValid;
    };

    var ValidateItemCodeForUpdate = function () {
      var isValid = true;

      /*self.model.ItemMasterDataList.forEach(function (element) {
        if (element.id != self.model.activeItemMasterId) {
          if (element.itemCode == self.model.itemCode) {
            growl.error("Duplicate Item code");
            isValid = false;
          }
        }
      });*/

      return isValid;
    };

    var ValidateProductCode = function () {
      var isValid = true;

     
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCode)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        isValid = false;
      }
      return isValid;
    };

    var ValidateProductCodeForUpdate = function () {
      var isValid = true;
/*
      self.model.ItemMasterDataList.forEach(function (element) {
        if (element.id != self.model.activeItemMasterId) {
          if (element.productCode == self.model.productCode) {
            growl.error("Duplicate Product code");
            isValid = false;
          }
        }
      });*/
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.productCode)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        isValid = false;
      }
      return isValid;
    };

    var CreateItem = function () {
      if (ValidateItemCode() && ValidateProductCode()) {
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.SAVE_API;
        
        if(self.model.manufactureId == undefined){
        	
       	 self.model.manufactureId =  self.model.manufactureName.id;
       	 
       }
        
        var data = {
          code: self.model.itemCode,
          formulationTypeId: self.model.formulationType.id,
          genericId: self.model.genericId,
          hsnCode: self.model.hsnCode,
          cimsCode: self.model.cimsCode,
          cimsType: self.model.cimsType,
          isBatchRequired: self.model.batchRequired,
          isCombination: self.model.isCombination,
          isConsignment: self.model.isConsignment,
          isInjection: self.model.isInjection,
          isConsumable: self.model.isConsumable,
          isHighrisk: self.model.highRisk,
          isHospitalFormulary: self.model.hospitalFormulary,
          isInfusion: self.model.isInfusion,
          isThreeUOMApplicable: true,
          isKit: self.model.isKit,
          isNarcotic: self.model.isNarcotics,
          isOtc: self.model.isOTC,
          isEmergencyDrug : self.model.isEmergencyDrug,
          isPsychotropic: self.model.psychotropic,
          isReusable: self.model.isitemReusable,
          isVaccine: self.model.isVaccine,
          itemRequestId: self.model.itemId,
          manufacturerId: self.model.manufactureId,
          name: self.model.itemName,
          pTaxId: self.model.purchaseTax.id,
          pharmacologicalClassificationId: self.model.classificationId,
          productCode: self.model.productCode,
          sTaxId: self.model.saleTax.id,
          storageUnitId: self.model.unit.id,
          strength: self.model.strength,
          strengthUnitId: self.model.unitName.id,
          productCategoryId : self.model.productCategory.id,
          status: true,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          userId:$scope.userId,
          storeId:$scope.storeId,
          reusableCount: self.model.count,
          reusablePercentage: [],
          unitOfMeasurementRequest :{},
          isDoseCalculationApplicable:self.model.isDoseCalculationApplicable,
          doseCalculationApplicableData1:[],
          storeIds: [],
          isLasa : self.model.isLasa
        };

        angular.forEach($scope.selectedStoreList, function(d){
          var store = {
            "storeId": d.id,
            "minQty": parseInt(self.model.minqty),
            "maxQty": parseInt(self.model.maxqty)
          }
          data.storeIds.push(store);
        });
        
        angular.forEach(self.model.itemDetails, function (item) {          
          if (!item.uomList[0].id) {
            var uomItem = {
              "itemId": null,
              "uomList": item.uomList
          }
          angular.forEach(item.uomList, function(d){
            if(self.model.threeUOMApplicable == false){
              if(d.uomTypeId == 2){
                d.conversion = 1;
              }  
            }
            if(d.uomTypeId == 3){
              d.conversion = 1;
            }
          })
          data.unitOfMeasurementRequest = uomItem;
        }
      });
        
        angular.forEach(self.model.reusablePercentage, function(d){
          var obj = {
            patient: d.patient
          }
          data.reusablePercentage.push(obj);
        });
        
        angular.forEach(self.model.doseCalculationData, function(d){
            var objDoseData = {
            		'strengthForDoseCal': self.model.strength,
    		        'doseForDoseCal': d.doseForDoseCal,
    		        'quantityForDoseCal': d.quantityForDoseCal, 
    		        'frequencyForDoseCal': d.frequencyForDoseCal,
    		        'durationForDoseCal': d.durationForDoseCal,
    		        'durationCountForDoseCal': d.durationCountForDoseCal,
    		        'searchItemFre': d.searchItemFre,
    	        	'noOfAdministrationTimeForDoseCal': d.noOfAdministrationTimeForDoseCal

            }
            data.doseCalculationApplicableData1.push(objDoseData);
          });
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var assetType = self.model.assetTypes.find(function (type) {
              return type.id == self.model.assetType.id;
            });
            var assetCategory = self.model.assetCategories.find(function (
              category
            ) {
              return category.id == self.model.assetCategory.id;
            });
            var productCatgeory = self.model.productCategories.find(function (
              category
            ) {
              return category.id == self.model.productCategory.id;
            });
            var formulationType = self.model.formulationTypes.find(function (
              type
            ) {
              return type.id == self.model.formulationType.id;
            });
            var genericName = self.model.Generics.find(function (name) {
              return name.id == self.model.genericId;
            });
            var unitName = self.model.Units.find(function (name) {
              return name.id == self.model.unitName.id;
            });
            var classificationName = self.model.Pharmalogicals.find(function (
              name
            ) {
              return name.id == self.model.classificationId;
            });
            var manufactureName = self.model.Manufactures.find(function (name) {
              return name.id == self.model.manufactureId;
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
            //GetItemMasterList();
            $rootScope.stopLoader();
            if(response.data.id!=undefined || response.data.id!=null){
            	growl.success(response.data.message);	
            
            	 var insertedData = {
                         id: response.data.id,
                         productCode: self.model.productCode,
                         name: self.model.itemName,
                         genericName: genericName.name,
                         assetType: assetType.type,
                         assetCategory: assetCategory.category,
                         productCategory: productCatgeory.category,
                         formulationType: formulationType.type,
                         strength: self.model.strength,
                         strengthUnit: unitName.unit,
                         code: self.model.itemCode,
                         manufacture: manufactureName.name,
                         salesTax: saleTax.type,
                         storageUnit: storageUnit.unit,
                         purchaseTax: purchaseTax.type,
                         hsnCode: self.model.hsnCode,
                         isVaccine: self.model.isVaccine,
                         isCombination: self.model.isCombination,
                         isHighrisk: self.model.highRisk,
                         isKit: self.model.isKit,
                         isNarcotic: self.model.isNarcotics,
                         pharmacologicalClassification: classificationName.classification,
                         isReusable: self.model.isitemReusable,
                         isOtc: self.model.isOTC,
                         isEmergencyDrug : self.model.isEmergencyDrug,
                         isBatchRequired: self.model.batchRequired,
                         isPsychotropic: self.model.psychotropic,
                         isInfusion: self.model.isInfusion,
                         threeUOMApplicable: self.model.threeUOMApplicable,
                         isHospitalFormulary: self.model.hospitalFormulary,
                         status: true
                       };
            	 
            	 self.model.ItemMasterDataList.push(insertedData);
                 PaginationService.searchData(self.model.ItemMasterDataList, self.model.pagination);
                 PaginationService.setLastPage(self.model.pagination);
            	 ClearFields();
            	 $state.reload();
            }else if(response.data.status =="error"){
            	growl.error(response.data.message);
            }
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error("Something Went Wrong");
          }
        );
      }
    };
    
    $scope.checkItemExists = function(){
    	
    	 
    	 if( self.model.itemName != undefined && self.model.itemName != null && self.model.itemName.length >=3){
    		 
    		 $rootScope.startLoader();
    		 
    		 var data={
    	    			id : (self.model.id != undefined && self.model.id != null ?  self.model.id : null),
    	    			name : self.model.itemName,
    	    			organizationId : $scope.organizationId
    	    			
    				}
    			var URI = BASE_URL + ROOT_URL + "/api/globals/items/checkForUniqueItemName"; 
    	    	 GenericService.serviceAction("POST", URI, data).then(function(response){
    				if (response.data.status == "success")
    				{
    					if(response.data.listObject != null &&  response.data.listObject.length >0 ){
    						
    						growl.error("Item Name is Already Exists.",
    								{
    									title : 'error!'
    										
    								});
    						
    						self.model.itemName = null;
    					}
    					  $rootScope.stopLoader();
    				} 
    				  $rootScope.stopLoader();
    			});
    	 }
    	
	
	
    }
    
    
    // Generic name
    $scope.testFlg = false;
     $scope.selectFlag1 = false;
     $scope.flgArea1 =false;
		$scope.onClickDropdownmy = function(){
          //alert("1-"+$scope.selectFlag);
			if(self.model.Generics.length>1){
              if($scope.selectFlag1 == false || $scope.selectFlag1== undefined){
            	  $scope.testFlg = true;
                   $scope.selectFlag1 = true;
                   $scope.flgArea1 =false;
              }
		}
          }
		$scope.searchTerm1="";
		$scope.applySizePermanant = function()
	       {
	    	   $('#selRegsPermanant').focus(function () {
	    		   if(self.model.Generics.length>1){
	    		   $scope.testFlg = true;
	    	 	    $('#selRegsPermanant').attr("size", "4");
	    		   }
	    	 	});
	    	   if(self.model.isgenericNameValid==false)
	    		   {
	    		   $scope.testFlg = false;
	    	 		$scope.searchTerm1="";
	    	 	    $('#selRegsPermanant').attr("size", "1");
	    	 	    $("#selRegsPermanant").blur();
	    		   }
	    	   else if(self.model.genericName.id==undefined || self.model.genericName.id==null)
	    		   {
	    		   $scope.testFlg = false;
	    	 		$scope.searchTerm1="";
	    	 	    $('#selRegsPermanant').attr("size", "1");
	    	 	    $("#selRegsPermanant").blur();
	    		   }
	    	 	$('#selRegsPermanant').change(function () {
	    	 		$scope.testFlg = false;
	    	 		$scope.searchTerm1="";
	    	 	    $('#selRegsPermanant').attr("size", "1");
	    	 	    $("#selRegsPermanant").blur();
 	 	});	
	    	 	/*$scope.myFunctionForToggle(true);*/
	    	 	}
		// Pharmalogical Classification
		$scope.testFlg1 = false;
	     $scope.selectFlag2 = false;
	     $scope.flgArea2 =false;
			$scope.onClickDropdownmy1 = function(){
	          //alert("1-"+$scope.selectFlag);
				if(self.model.Pharmalogicals.length>1){
	              if($scope.selectFlag2 == false || $scope.selectFlag2== undefined){
	            	  $scope.testFlg1 = true;
	                   $scope.selectFlag2 = true;
	                   $scope.flgArea2 =false;
	              }
			}
	          }
			$scope.searchTerm2="";
			$scope.applySizePermanant1 = function()
		       {
		    	   $('#selRegsPermanant1').focus(function () {
		    		   if(self.model.Pharmalogicals.length>1){
		    		   $scope.testFlg1 = true;
		    	 	    $('#selRegsPermanant1').attr("size", "4");
		    		   }
		    	 	});
		    	   if(self.model.ispharmalogicalClassificationValid==false)
		    		   {
		    		   $scope.testFlg1 = false;
		    	 		$scope.searchTerm2="";
		    	 	    $('#selRegsPermanant1').attr("size", "1");
		    	 	    $("#selRegsPermanant1").blur();
		    		   }
		    	   else if(self.model.classificationName.id==undefined || self.model.classificationName.id==null)
		    		   {
		    		   $scope.testFlg1 = false;
		    	 		$scope.searchTerm2="";
		    	 	    $('#selRegsPermanant1').attr("size", "1");
		    	 	    $("#selRegsPermanant1").blur();
		    		   }
		    	 	$('#selRegsPermanant1').change(function () {
		    	 		$scope.testFlg1 = false;
		    	 		$scope.searchTerm2="";
		    	 	    $('#selRegsPermanant1').attr("size", "1");
		    	 	    $("#selRegsPermanant1").blur();
	 	 	});	
		    	 	/*$scope.myFunctionForToggle(true);*/
		    	 	}
			
			// manufacturer
			$scope.testFlg2 = false;
		     $scope.selectFlag3 = false;
		     $scope.flgArea3 =false;
				$scope.onClickDropdownmy2 = function(){
		          //alert("1-"+$scope.selectFlag);
					if(self.model.Manufactures.length>1){
		              if($scope.selectFlag3 == false || $scope.selectFlag3== undefined){
		            	  $scope.testFlg2 = true;
		                   $scope.selectFlag3 = true;
		                   $scope.flgArea3 =false;
		              }
				}
		          }
				$scope.searchTerm3="";
				$scope.applySizePermanant2 = function()
			       {
			    	   $('#selRegsPermanant2').focus(function () {
			    		   if(self.model.Manufactures.length>1){
			    		   $scope.testFlg2 = true;
			    	 	    $('#selRegsPermanant2').attr("size", "4");
			    		   }
			    	 	});
			    	   if(self.model.ismanufacturerValid==false)
			    		   {
			    		   $scope.testFlg2 = false;
			    	 		$scope.searchTerm3="";
			    	 	    $('#selRegsPermanant2').attr("size", "1");
			    	 	    $("#selRegsPermanant2").blur();
			    		   }
			    	   else if(self.model.manufactureName.id==undefined || self.model.manufactureName.id==null)
			    		   {
			    		   $scope.testFlg2 = false;
			    	 		$scope.searchTerm3="";
			    	 	    $('#selRegsPermanant2').attr("size", "1");
			    	 	    $("#selRegsPermanant2").blur();
			    		   }
			    	 	$('#selRegsPermanant2').change(function () {
			    	 		$scope.testFlg2 = false;
			    	 		$scope.searchTerm3="";
			    	 	    $('#selRegsPermanant2').attr("size", "1");
			    	 	    $("#selRegsPermanant2").blur();
		 	 	});	
			    	 	/*$scope.myFunctionForToggle(true);*/
			    	 	}
    var UpdateItem = function () {
    	if($scope.isValidFlag==false)
    		{
    		growl.error("Can Not Unmap "+$scope.storeNameToDisplay);
    		return false;
    		}
      if (ValidateItemCodeForUpdate() && ValidateProductCodeForUpdate()) {
        var URI =
          CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API +$scope.organizationId + '/' +
          self.model.id;
        var data = {
          code: self.model.itemCode,
          formulationTypeId: self.model.formulationType.id,
          genericId: self.model.genericId,
          hsnCode: self.model.hsnCode,
          cimsCode: self.model.cimsCode,
          cimsType: self.model.cimsType,
          isBatchRequired: self.model.batchRequired,
          isCombination: self.model.isCombination,
          isConsignment: self.model.isConsignment,
          isInjection: self.model.isInjection,
          isConsumable: self.model.isConsumable,
          isHighrisk: self.model.highRisk,
          isHospitalFormulary: self.model.hospitalFormulary,
          isInfusion: self.model.isInfusion,
          isThreeUOMApplicable: true,
          isKit: self.model.isKit,
          isNarcotic: self.model.isNarcotics,
          isOtc: self.model.isOTC,
          isEmergencyDrug :self.model.isEmergencyDrug,
          isPsychotropic: self.model.psychotropic,
          isReusable: self.model.isitemReusable,
          isVaccine: self.model.isVaccine,
          itemRequestId: self.model.itemId,
          manufacturerId: self.model.manufactureId,
          name: self.model.itemName,
          pTaxId: self.model.purchaseTax.id,
          pharmacologicalClassificationId: self.model.classificationId,
          productCode: self.model.productCode,
          sTaxId: self.model.saleTax.id,
          storageUnitId: self.model.unit.id,
          strength: self.model.strength,
          strengthUnitId: self.model.unitName.id,
          unitId:$scope.unitId,
          status: true,
          reusableCount: self.model.count,
          reusablePercentage: [],
          isDoseCalculationApplicable:self.model.isDoseCalculationApplicable,
          doseCalculationApplicableData1:[],
          unitOfMeasurementRequest :{},
          storeIds: [],
          isLasa : self.model.isLasa
        };

        if($scope.selectedStoreList.length != $scope.list.length){
        	if( $scope.list.length >0)	{
        	if($scope.selectedStoreList.length > $scope.list.length ){
        	angular.forEach($scope.selectedStoreList, function(l){
            	var found = $scope.list.find(ar=>ar.id==l.id);
    			   if(found==undefined)
    				   {
    				   var store = {
                               "storeId": l.id,
                               "minQty": parseInt(self.model.minqty),
                               "maxQty": parseInt(self.model.maxqty),
                               "id": null,
                               "isActive": false
                             }
                             data.storeIds.push(store);
    				   }
    			   else{
    				   var store = {
                               "storeId": l.id,
                               "minQty": parseInt(self.model.minqty),
                               "maxQty": parseInt(self.model.maxqty),
                               "id": found.id1,
                               "isActive": true
                             }
                             data.storeIds.push(store);
    			   }
    			   });
        	}else if($scope.selectedStoreList.length < $scope.list.length ){
        		
            	angular.forEach($scope.list, function(l){
                	var found = $scope.selectedStoreList.find(ar=>ar.id==l.id);
        			   if(found==undefined)
        				   {
        				   var store = {
                                   "storeId": l.id,
                                   "minQty": parseInt(self.model.minqty),
                                   "maxQty": parseInt(self.model.maxqty),
                                   "id": l.id1,
                                   "isActive": false
                                 }
                                 data.storeIds.push(store);
        				   }
        			   else{
        				   var store = {
                                   "storeId": l.id,
                                   "minQty": parseInt(self.model.minqty),
                                   "maxQty": parseInt(self.model.maxqty),
                                   "id": found.id1,
                                   "isActive": true
                                 }
                                 data.storeIds.push(store);
        			   }
        			   });
        	}
        	}else{
        		
        		angular.forEach($scope.selectedStoreList, function(l){
        		 var store = {
                         "storeId": l.id,
                         "minQty": parseInt(self.model.minqty),
                         "maxQty": parseInt(self.model.maxqty),
                         "id": l.id1 ? !l.id1 : null,
                         "isActive": false
                       }
                       data.storeIds.push(store);
        		});
        	}
        }
        else{
          angular.forEach($scope.selectedStoreList, function(l){
            var store = {
              "storeId": l.id,
              "minQty": parseInt(self.model.minqty),
              "maxQty": parseInt(self.model.maxqty),
              "id": l.id1,
              "isActive": true
            }
            data.storeIds.push(store);
          });
        }
        
        angular.forEach(self.model.itemDetails, function (item) {
          var itemObj ={};
          var updatedUomList = [];
          item.uomList.forEach(function(uom){
            if(self.model.threeUOMApplicable == false){
              if(uom.uomTypeId == 2){
                uom.conversion = 1;
              }  
            }
            if(uom.uomTypeId==3)
            {
              if(uom.conversion==null)
              {
                uom.conversion=1;
              }
            }
            updatedUomList.push({
              "id": uom.id,
              "uomTypeId": uom.uomTypeId,
              "uomUnitId": uom.uomUnitId,
              "conversion": parseInt(uom.conversion),
              "ipUom": uom.ipUom,
              "opUom": uom.opUom,
              "storeUom": uom.storeUom,
              "status": true
            });
          });
          itemObj.uomList = updatedUomList;
          data.unitOfMeasurementRequest = itemObj;
        });
        
        angular.forEach(self.model.reusablePercentage, function(d){
          var obj = {
            patient: d.patient
          }
          data.reusablePercentage.push(obj);
         });
        
        angular.forEach(self.model.doseCalculationData, function(d){
            var objDoseData = {
            		'strengthForDoseCal': self.model.strength,
    		        'doseForDoseCal': d.doseForDoseCal,
    		        'quantityForDoseCal': d.quantityForDoseCal, 
    		        'frequencyForDoseCal': d.frequencyForDoseCal,
    		        'durationForDoseCal': d.durationForDoseCal,
    		        'durationCountForDoseCal': d.durationCountForDoseCal,
    		        'searchItemFre': d.searchItemFre,
    		        'noOfAdministrationTimeForDoseCal': d.noOfAdministrationTimeForDoseCal

            }
            data.doseCalculationApplicableData1.push(objDoseData);
          });
     
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            var assetType = self.model.assetTypes.find(function (
            		type) {
              return type.id == self.model.assetType.id;
            });
            var assetCategory = self.model.assetCategories.find(function (
              category
            ) {
              return category.id == self.model.assetCategory.id;
            });
            var productCatgeory = self.model.productCategories.find(function (
              category
            ) {
              return category.id == self.model.productCategory.id;
            });
            var formulationType = self.model.formulationTypes.find(function (
              type
            ) {
              return type.id == self.model.formulationType.id;
            });
            var genericName = self.model.Generics.find(function (name) {
              return name.id == self.model.genericId;
            });
            var unitName = self.model.Units.find(function (name) {
              return name.id == self.model.unitName.id;
            });
            var classificationName = self.model.Pharmalogicals.find(function (
              name
            ) {
              return name.id == self.model.classificationId;
            });
            var manufactureName = self.model.Manufactures.find(function (name) {
              return name.id == self.model.manufactureId;
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
            var itemMaster = self.model.ItemMasterDataList.find(function (item) {
              return item.id == self.model.id;
            });

            itemMaster.productCode = self.model.productCode;
            itemMaster.name = self.model.itemName;
            itemMaster.genericName = genericName.name;
            itemMaster.assetType = assetType.type;
            itemMaster.assetCategory = assetCategory.category;
            itemMaster.productCategory = productCatgeory.category;
            itemMaster.formulationType = formulationType.type;
            itemMaster.strength = self.model.strength;
            itemMaster.strengthUnit = unitName.unit;
            itemMaster.code = self.model.itemCode;
            itemMaster.manufacture = manufactureName.name;
            itemMaster.salesTax = saleTax.code;
            itemMaster.storageUnit = storageUnit.unitName;
            itemMaster.purchaseTax = purchaseTax.code;
            itemMaster.hsnCode = self.model.hsnCode;
            itemMaster.isVaccine = self.model.isVaccine;
            itemMaster.isCombination = self.model.isCombination;
            itemMaster.isHighrisk = self.model.highRisk;
            itemMaster.isKit = self.model.isKit;
            itemMaster.isNarcotic = self.model.isNarcotics;
            itemMaster.pharmacologicalClassification =
              classificationName.classification;
            itemMaster.isReusable = self.model.isitemReusable;
            itemMaster.isOtc = self.model.isOTC;
            itemMaster.isEmergencyDrug = self.model.isEmergencyDrug;
            itemMaster.isLasa = self.model.isLasa;
            itemMaster.isBatchRequired = self.model.batchRequired;
            itemMaster.isPsychotropic = self.model.psychotropic;
            itemMaster.isInfusion = self.model.isInfusion;
            itemMaster.threeUOMApplicable = self.model.threeUOMApplicable;
            itemMaster.isHospitalFormulary = self.model.hospitalFormulary;
            $rootScope.stopLoader();
            if(response.data.id!=undefined || response.data.id!=null){
            	growl.success(response.data.message);	
                $('.modal-backdrop').remove()
                $(document.body).removeClass("modal-open");
            	 ClearFields();
            	 self.model.assetType = self.model.assetTypes[1];
            	 
//                 GetItemMasterList();
                 $state.reload();
            }else if(response.data.status =="error"){
            	growl.error(response.data.message);
            }
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(response.data.message);
          }
        );
      }
    };

    //Method to activate or deactivate Main Store
    var ToggleStatus = function (itemMaster) {
      var URI =
        CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API + $scope.organizationId + '/' + itemMaster.id;
      var data = {
        "status": itemMaster.status,
        "strength": itemMaster.strength
      };
      if (itemMaster.status == false) {
        ClearFields();
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          if (response.data.code == 406) {
            growl.error(response.data.message);
            itemMaster.status = true;
            $rootScope.stopLoader();
          } 
        else{
         // growl.success(response.data.message);
          growl.success('Status changed successfully');
          $rootScope.stopLoader();
        }
         
        }, function (err) {
       

            growl.error('Item mapped to store, Unable to deactivate item');
       
        });
      $rootScope.stopLoader();
    };

    var ValidateItem = function () {
      var isValid = true;
      if (!self.model.assetType.id) {
        isValid = false;
        self.model.isassetTypeValid = true;
      }
      if (!self.model.assetCategory.id) {
        isValid = false;
        self.model.isassetCategoryValid = true;
      }
      if (!self.model.productCategory.id) {
        isValid = false;
        self.model.isproductCatgeoryValid = true;
      }
      if (!self.model.formulationType.id) {
        isValid = false;
        self.model.isformulationTypeValid = true;
      }
   
      if (!self.model.itemName) {
        isValid = false;
        self.model.isitemNameValid = true;
      }
      if (!self.model.genericId) {
        isValid = false;
        self.model.isgenericNameValid = true;
      }
      
      if (!self.model.classificationId ) {
        isValid = false;
        self.model.ispharmalogicalClassificationValid = true;
      }
      if (!self.model.manufactureId && !self.model.manufactureName.id) {
        isValid = false;
        self.model.ismanufacturerValid = true;
      }
      if (!self.model.purchaseTax.id) {
        isValid = false;
        self.model.ispurchaseTaxValid = true;
      }
      if (!self.model.saleTax.id) {
        isValid = false;
        self.model.issaleTaxValid = true;
      }
      if (!self.model.hsnCode) {
        isValid = false;
        self.model.ishsnCodeValid = true;
      }
      if (!self.model.unit.id) {
        isValid = false;
        self.model.isstorageUnitValid = true;
      }
      return isValid;
    };
    
    //Upload CSV START 
    
	$scope.fileObj={
			csvfile:$scope.csvfile,
	}
	
	$scope.filepreview = "";
	$scope.filepreview = $scope.csvfile;
		$scope.csvfile;
		 
	$scope.failureRecordCount=0;		
	$scope.uploadfile = function() {
			try {
			   $rootScope.startLoader();
				var fileName = $scope.csvfile;
		        var data = new FormData();
		        data.append('file',fileName );
		        console.log("listData", data);
		        var config = {
		        	   	transformRequest: angular.identity,
		        	   	transformResponse: angular.identity,
		    	   		headers : {
		    	   			unitId:$scope.unitId,
							organizationId:$scope.organizationId,
							updatedBy : $scope.userId,
							createdBy : $scope.userId,
							storeId   : $scope.storeId,
		    	   			'Content-Type': undefined
		    	   	    }
		           }
			

                if($scope.csvfile){
					var url=BASE_URL + ROOT_URL + "/api/globals/items/uploadItemMaster";
			          $http.post(url, data, config)
			           .then(function (response)  
			        	{
			        	   $rootScope.stopLoader();
			        	   var response= JSON.parse(response.data);
			        	   if(response.status == 'success'){
			        		var successCount=response.object.successCount;
			        		var failedCount=response.object.failureCount;
			        		$scope.failureRecordCount=failedCount;
			        		$('#csvfile').val(undefined);
                           var message="";
			        		if(failedCount==0){
			        			message="All records saved successfully with count="+successCount;
			        			growl.success(message,
										{
											title : 'SUCCESS'
												
										});
			        		}else if(successCount==0){
			        			message="All record failed with count="+failedCount;
			        			growl.warning(message,
										{
											title : 'Warning!'
												
										});
			        		}else{
			        			message="Saved record count="+successCount+" failed record count="+failedCount;
			        			growl.warning(message,
										{
											title : 'Warning!'
												
										});
			        		}
			        	 
			        	   }
			        	   else if(response.status== 'error'){
			        			growl.error(response.message,
										{
											title : 'Already Exist'
												
										});
				           }
					});
		
				
                }
                else
              	  {
              	  growl.error("Please choose .CSV file",
								{
									title : 'ERROR'
										
								});
              	   $rootScope.stopLoader();
              	  }
			} catch (e) {
				console.log("Exception-", e);
			}
		}
	
	
    
    //Upload CSV END

    //Method to clear the fileds
    function ClearFields() {
      self.model.id = 0;
     // self.model.assetType = self.model.assetTypes[0];
      self.model.assetCategory = self.model.assetCategories[0];
      self.model.assetCategories.splice(1);
      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);
      self.model.genericName = "";
      self.model.Generics.splice(1);
      self.model.itemCode = "";
      self.model.productCode = "";
      self.model.itemName = "";
      self.model.strength = "";
      self.model.unitName = "";
      self.model.formulationType = self.model.formulationTypes[0];
      self.model.classificationName = "";
      self.model.manufactureName = "";
      self.model.purchaseTax = self.model.PurchaseTaxes[0];
      self.model.saleTax = self.model.SaleTaxes[0];
      self.model.unitName = self.model.Units[0];
      self.model.unit = self.model.StorageUnits[0];
      self.model.hsnCode = "";
      self.model.cimsCode = "";
      self.model.cimsType = "";
      self.model.storageUnit = "";
      self.model.isitemReusable = false;
      self.model.isVaccine = false;
      self.model.isCombination = false;
      self.model.batchRequired = false;
      self.model.highRisk = false;
      self.model.psychotropic = false;
      self.model.isKit = false;
      self.model.isInfusion = false;
      self.model.isNarcotics = false;
      self.model.hospitalFormulary = false;
      self.model.isConsumable = false;
      self.model.isConsignment = false;
      self.model.isInjection = false;
      self.model.threeUOMApplicable = false;
      self.model.isDoseCalculationApplicable= false;
      self.model.isOTC = false;
      self.model.isEmergencyDrug = false;
      self.model.pagination.search = '';
      self.model.itemDetails = [];
      self.model.itemObject = {
        		itemId:null,
        		puom : null,
        		purchaseQuantity : null,
        		suom : null,
        		stockQuantity : null,
        		duom : null,
        		dispenseQuantity : null,
        		ipuom: null,
        		opuom :null,
        		storeuom : null,
          };
      self.model.itemObject.uomList = [{
          "uomTypeId": 1,
          "uomUnitId": 1,
          "conversion": null,
          "ipUom": true,
          "opUom": true,
          "storeUom": true,
          "status": true,
          "id":''
        }, {
          "uomTypeId": 2,
          "uomUnitId": 1,
          "conversion": null,
          "ipUom": false,
          "opUom": false,
          "storeUom": false,
          "status": true,
          "id":''
        }, {
          "uomTypeId": 3,
          "uomUnitId": 1,
          "conversion": null,
          "ipUom": false,
          "opUom": false,
          "storeUom": false,
          "status": true,
          "id":''
        }
      ];
      $scope.selectedStoreList = [];
      self.model.itemDetails.push(self.model.itemObject);
    }

    var clearSearch = function(){
      self.model.pagination.search = "";
      PaginationService.searchData(self.model.ItemMasterDataList, self.model.pagination);
    }

    //Method to get the list of Generic 
    var GetGenericList = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API + $scope.organizationId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.genericList = response.data;
          GetGenericMaster();
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }
    GetGenericList();

    var genericMasterList = [];
    var GetGenericMaster = function () {
      var arraylist = self.model.genericList.filter(x => x.status == true);
      for (let generic of arraylist){
        genericMasterList.push(generic);
      }       
      $rootScope.stopLoader();
    } 

    var GetStrengthUnitList = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.strengthUnitList = response.data;
            console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            console.log("Error");
            console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          });
    }
    GetStrengthUnitList();

    var ViewCombinationDetails = function(item) {
      self.model.genericCombinationList = [];
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          for(let combination of response.data) {
            let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
            combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
            combination.code = item.code;
            combination.genericName = item.name;
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

    $scope.validateMaxqty = function () {
      var isValid = true
      if(self.model.maxqty > 10000000){
        growl.error("Max order quantity is greater than 10000000")
        isValid = false;
      }
      return isValid;
    }
    $scope.uomTypesHideAndShow = function () {
      if(self.model.threeUOMApplicable == true){
        self.model.uomTypes = [
          {
        	  "id": 3,
              "uomType": "Dispense" 
            },
            {
              "id": 1,
              "uomType": "Purchase"
            },
            {
              "id": 2,
              "uomType": "Stock"
          }
        ]
      }
      else{  
        self.model.uomTypes = [
          {
            "id": 1,
            "uomType": "Purchase"
          },
          {
            "id": 3,
            "uomType": "Dispense"
          }
        ]
      }
      angular.forEach(self.model.itemDetails, function(item){
        var selectedipUom = item.uomList.find(function(uom){
          return uom.ipUom == true;
        });

        var selectedopUom = item.uomList.find(function(uom){
          return uom.opUom == true;
        });

        var selectedstoreUom = item.uomList.find(function(uom){
          return uom.storeUom == true;
        });
        item.ipuom = self.model.uomTypes.find(function (data) {
          return data.id == selectedipUom.uomTypeId;
        });
        item.opuom = self.model.uomTypes.find(function (data) {
          return data.id == selectedopUom.uomTypeId;
        });
        item.storeuom = self.model.uomTypes.find(function (data) {
          return data.id == selectedstoreUom.uomTypeId;
        });
      });
      if(self.model.threeUOMApplicable == false){
      angular.forEach(self.model.itemDetails, function(item){
    	  item.ipuom={
              "id": 3,
              "uomType": "Dispense"
            }
    	  item.opuom={
              "id": 3,
              "uomType": "Dispense"
            }
    	  item.storeuom={
    	            "id": 3,
    	            "uomType": "Dispense"
    	          }
    	  
      $scope.ChangeUOMTypeNew(item,"ipuom");
      $scope.ChangeUOMTypeNew(item,"opuom");
      $scope.ChangeUOMTypeNew(item,"storeuom");
      });
      }
    }

    initializeController();
    $scope.uomTypesHideAndShow();
  
  }

  function config($stateProvider) {
    $stateProvider.state("itemMasterGlobal", {
      url: "/itemMasterGlobal",
      templateUrl:"views/master/GlobalMaster/AssetManagenent/Master/item-master.html",
      controller: "ItemMasterGlobal.Controller",
      title: 'Item Master',
  	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 464);
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
    .controller("ItemMasterGlobal.Controller", itemMasterController);
})();
