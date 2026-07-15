angular.module('myApp').constant('CONSTANTS', function () {


  var host = '';

  var root = '';
  var rootUrl = host + root;
  var globalApi = rootUrl + '/api/global';
  var globalsApi = rootUrl + '/api/globals';
  var pharmacyApi = rootUrl + '/api/pharmacy';
  var inventoryApi = rootUrl + '/api/inventory';
  var procurementApi = rootUrl + '/api/procurements';
  var assetApi = rootUrl + '/api/asset';
  var unitsApi = rootUrl + '/api/units';
  var unitApi = rootUrl + '/api/unit';
  var linenApi = rootUrl + '/api/linenLaundry';
  
  return {
    HOST: host,
    ROOT_URL: rootUrl,
    GLOBAL: {
      USER_STORE_API: globalApi + '/user/stores',
      USER_STORE_BY_ID_API: globalApi + '/store/getStoreListById/',
      ASSET_TYPE_API: globalApi + '/assets/types',
      ASSET_CATEGORY_API: globalApi + '/assets/categories/',
      PRODUCT_CATEGORY_API: globalApi + '/products/categories/',
      MASTER_PRODUCT_CATEGORY_API: globalsApi + '/products/categories/',
      ITEM_API: globalApi + '/items/',
      ACCESSORIES_SPARES_ITEM_API: globalApi + '/items/flagwiseitemslist/',
      ITEM_API_ASSET_INDENT: globalApi + '/items/storeIndent',
      STORE_WISE_ITEM_API: globalApi + '/items/getlistofitems',
      Item_Api_store: globalApi + '/items/listWithMappedStores/',
      Item_Api_store_listWithMappedStoreAccessories: globalApi + '/items/listWithMappedStoreAccessories/',
      Item_Api_store_withcomparequantity: globalApi + '/items/listWithMappedStoresWithCompareAvalablity/',
      GENERIC_API_SEARCH: globalApi + '/items/generic/',
      ITEM_API_FOR_MASTER: globalsApi + '/items/list/',
      MULTI_ITEM_API_FOR_MASTER: globalsApi + '/items/multiplestorelist/',
      BATCH_API: globalApi + '/items/batches/billableConsumption/',
      BATCH_API_STORE_WISE: globalApi + '/items/batches/billableConsumption/storeWise',
      BATCH_API_WITHOUT_GENERIC: globalApi + '/items/batches/billableConsumption/storeWise/withoutGeneric',
      WALK_IN_SALES_BATCH_API: globalApi + '/items/expiryAndAvailableQuantityWisebatches/',
      NEAR_EXPIRY: globalApi + '/items/batches/near/expired',
      DATE_NEAR_EXPIRY: globalApi + '/items/batches/near/dateexpired',
      EXPIRY: globalApi + '/items/batches/expiredmedications',
      PATIENT_SEARCH_API: globalApi + '/patient/search/',
      PATIENT_SEARCH_API_PATIENT_ISSUE_RETURN: globalApi + '/patient/search/issue/return/',
      PATIENT_SEARCH_API_PATIENT_INDENT: globalApi + '/patient/search/patient/indent/',
      DOCTOR_SEARCH_API: globalApi + '/doctor/search/',
      SALE_TYPE_DROPDOWN_API: globalApi + '/sale/type',
      FLOOR_API: globalApi + '/floor',
      WARD_API: globalApi + '/ward',
      VENDOR_SEARCH_API: globalApi + '/vendor/search/',
      ALL_VENDOR_SEARCH_API: globalApi + '/allvendor/search/',
      MANUFACTURER_SEARCH_API: globalApi + '/manufacturer/search/',
      GET_INDENTS_API: globalApi + '/stores/indents/',
      AGAINST_PO_SEARCH: globalApi + '/against/purchase/order/search/',
      AGAINST_PO_DETAILS: globalApi + '/against/purchase/order/details',
      AGAINST_PR_SEARCH: globalApi + '/against/purchase/request/search',
      AGAINST_PR_DETAILS: globalApi + '/against/purchase/request/details',
      DIRECT_PR_DETAILS: globalApi + '/direct/purchase/request/details',
      POCreation_AddItems : globalApi + '/direct/purchase/requestWithPendingQty/details',
      STATUS_API: globalApi + '/status',
      KIT_ITEM_DETAILS_API: globalApi + '/items/kit/details',
      ITEM_DETAILS_API: globalApi + '/items/details',
      ITEM_DETAILS_API_WALKIN: globalApi + '/items/details/walkin',
      DETAILS_API_IP: globalApi + '/items/details/ip',
      ITEM_PATIENT_CONSATION: globalApi + '/items/details',
      ITEM_DETAILS_API_COSUMABLE_INDENT: globalApi + '/items/details/consumable/indent',
      ITEM_DETAILS_API_NEW: globalApi + '/items/details/opening/balance',
      CANCEL_REASON_API: globalApi + '/cancel/reasons',
      REJECT_REASON_API: globalApi + '/reject/reasons',
      CURRENCY_API: globalApi + '/currencies',
      UHID_SEARCH_API: globalApi + '/patient/search/uhid/',
      UHID_SEARCH_API_SALES_RETURN: globalApi + '/patient/search/sales/return/uhid/',
      BATCH_ITEM_API: globalApi + '/items/batches/details',
      DETAILS_BATCH: globalApi + '/items/details/batches',
      STORE_TYPE_API: globalApi + '/store/type',
      PATIENT_DETAILS_SEARCH_API: globalApi + '/patient/search/patientIndent',
      PATIENT_DETAILS_NURSING_SEARCH_API: globalApi + '/patient/search/patientIndentForNursing',
      PATIENT_ISSUE_DROPDOWN_API: globalApi + '/pharmacy/patient/issue/dropdown',
      RACK_API: globalApi + '/racks/',
      SHELF_API: globalApi + '/racks/shelves/',
      PAYMENT_MODE_API: unitApi + '/vendors/paymentModes',
      COUNTRY_API: unitApi + '/vendors/countries',
      STATE_API: unitApi + '/vendors/states/',
      CITY_API: unitApi + '/vendors/cities/',
      AREA_API: unitApi + '/vendors/areas/',
      GRN_API: globalApi + '/against/grn',
      GRNAPI_AGAINST: globalApi + '/against/grn/return',
      GRN_API_FOR_BARCODE: globalApi + '/against/grn/ApprovedQty',
      SERVICE_PROVIDER: globalApi + '/service/provider/search/',
      INSAURANCE_COMPANY_API: globalApi + '/insurance/company/search/',
      INWARD_BY: globalApi + '/inWardBy/search/',
      //GLOBAL.SERVICE_PROVIDER: globalApi + '/service/provider/search/',
      //INSAURANCE_COMPANY_API: globalApi + '/insurance/company/search/',
      //INWARD_BY: globalApi + '/inWardBy/search/',
      ROLE_API: root + '/role',
      MODULE_API: root + '/module',
      MODULE_API_BY_PARENT: root + '/module/getZeroParentModule',
      SUBMODULE_API: root + '/submodule/',
      LINK_API: root + '/screen/',
      PRIVILEGE_API: root + "/privilege",
      UOM_UNITS: globalApi + '/uom/units',
      PO_HISTORY: globalApi + '/purchase/order/poHistory/',
      ITEMS_WORKORDER: globalApi + '/items/workOrder',
      SMART_ISSUE: globalApi + '/items/smartissue',
      ITEM_CONSUMABLE_INDENT_API: globalApi + '/items/consumableIndent',
      AGAINST_QR_SEARCH: globalApi + '/against/quotation/request/search',
      AGAINST_QR_DETAILS: globalApi + '/against/quotation/request/details',
      PURCHASE_ITEM_DETAILS_API: globalApi + '/items/directPR/details',
      STORE_INDENT_ITEM_API: globalApi + '/items/storeIndent',
      ITEM_PATIENT_INDENT_API: globalApi + '/items/consumableIndent/consignment',
      ITEM_PATIENT_INDENT_API_NEW: globalApi + '/items/consumableIndent/consignment/indent',
      NEW_BATCH_API: globalApi + '/items/batches/',

    },

    MASTER: {
      GLOBAL: {
        PHARAMACY: {
          PRODUCT_CATEGORY_MASTER: {
        	  PRODUCT_CATEGORY_API: globalsApi + '/products/categories/',
          },
          GENERIC_MASTER: {
        	GENERIC_MASTER_API: globalsApi + '/generics/',
        	GENERIC_MASTER_IS_COMBINATION_API: globalsApi + '/generics/IsCombination/true',
        	GENERIC_MASTER_IS_COMBINATION_DETAILS_API: globalsApi + '/generics/getCombinationDetail/'
          },
          FORMULATION_TYPE: {
        	  FORMULATION_TYPE_API: globalsApi + '/formulationTypes/',
          },
          ITEM_MASTER: {
        	DROPDOWN_API: globalsApi + '/items/dropdown',
        	SEARCH_API: globalsApi + '/items',
        	DETAILS_API: globalsApi + '/items/',
            SAVE_API: globalsApi + '/items',
            FORMULATION_TYPE_API: globalsApi + '/items/formulation/type/',
            GENERIC_API: globalsApi + '/items/generic/',
            UNIT_API: globalsApi + '/strength/units',
            MANUFACTURE_API: globalsApi + '/manufacturers/',
            TAX_API: globalsApi + '/tax/',
            STORAGE_API: globalsApi + '/storage/units/',
          },
          MANUFACTURER_MASTER: {
        	  MANUFATURER_API: globalsApi + '/manufacturers/',
        	  MANUFATURER_PREFIX_API : globalsApi + '/manufacturers/prefix/'
          },
          TAX_COMPONENT_MASTER: {
        	TAX_COMPONENT_API: globalsApi + '/tax/components/',
        	TAX_COMPONENT_MASTER: globalsApi + '/tax/',
        	TAX_COMPONENT_MASTER_DROPDOWN: globalsApi + '/tax/components/taxcomponentlist/',
        	TAX_MASTER_SAVE_API: globalsApi + '/tax/save',
          },
          UNIT_MASTER: {
        	  UNIT_API: globalsApi + '/uom/units/',
          },
          STORAGE_UNIT_MASTER: {
        	  STORAGE_UNIT_API: globalsApi + '/storage/units/',
          },
          STRENGTH_UNIT_MASTER: {
        	  STRENGTH_UNIT_API: globalsApi + '/strength/units/',
          },
          OTHER_CHARGES_MASTER: {
        	   OTHER_CHARGES_API: globalsApi + '/other/charges/',
          },
          UOM_MASTER: {
        	   SAVE_UOM: globalApi + '/unitOfMeasurement/'
          }
        },
        CLINICAL: {
          FREQUENCY_API: globalsApi + '/frequency/',
          FREQUENCY_SAVE: globalsApi + '/frequency/save',
          ROUTE_OF_ADMINSTRATION_API: globalApi + '/route/of/administration/',
          FORMULATION_ROA_API: globalsApi + '/formulationTypeAndROA/',
          FORMULATION_ROA_DROPDOWN_API: globalsApi + '/formulationTypeAndROA/dropdown/',
          FORMULATION_ROA_SAVE_API: globalsApi + '/formulationTypeAndROA/save'
        }
      },
      UNIT: {
        KIT_TYPE_MASTER: {
          GET_ALL: unitsApi + '/kit/',
          SAVE_API: unitsApi + '/kit/save',
          UPDATE_API: unitsApi + '/kit/update/',
          DETAILS_API: unitsApi + '/kit/',
        },

        KIT_MASTER: {
          DROPDOWN_API: unitsApi + '/kit/master/dropdowns',
          GET_ALL: unitsApi + '/kit/master/',
          SAVE_API: unitsApi + '/kit/master/save',
          UPDATE_API: unitsApi + '/kit/master/update/',
          DETAILS_API: unitsApi + '/kit/master/',
        },

        GENERAL_INSURANCE_MASTER: {
          GET_ALL: unitApi + '/generalinsurance',
          SAVE_API: unitApi + '/generalinsurance/save',
          UPDATE_API: unitApi + '/generalinsurance/',
          DETAILS_API: unitApi + '/generalinsurance/',
        },

        IMPLANT_MASTER: {
          DROPDOWN_API: globalsApi + '/item/implantItemMaster/dropdown',
          GET_ALL: globalsApi + '/item/implantItemMaster',
          SAVE_API: globalsApi + '/item/implantItemMaster/save',
          UPDATE_API: globalsApi + '/item/implantItemMaster/',
          DETAILS_API: globalsApi + '/item/implantItemMaster/',
        },
        FIXED_ASSET: {
          ASSET_CATEGORIES_API: unitsApi + '/assets/categories/',
          FIXED_ASSET_MASTER: unitApi + '/fixed/asset/',
          TYPE_DROPDOWN_API: globalApi + '/fixed/assets/types',
          FIXED_ASSET_TYPE_MASTER_API: unitApi + '/fixed/asset/type/',
        },

        FIXED_ASSET_TYPE_MASTER: {
          FIXED_ASSET_TYPE_LS_API: unitApi + '/fixed/asset/type',
          FIXED_ASSET_TYPE_DU_API: unitApi + '/fixed/asset/type/',
        },
        ITEM: {
          ASSET_CATEGORIES_API: unitsApi + '/assets/categories/',
        },
        PHARAMACY: {
          PHARMALOGICAL_CLASSIFICATION_MASTER: {
            PHARMALOGICAL_CLASSIFICATION_API: unitsApi + '/pharmacological/classifications/',
          },
          MAIN_STORE_MASTER: {
            MAIN_STORE_MASTER_LS_API: unitApi + '/main/stores',
            MAIN_STORE_MASTER_DU_API: unitApi + '/main/stores/',
            MAIN_STORE_MASTER_DU_UPDATE_API: unitApi + '/main/stores/',
          },
          VENDOR_MASTER: {
            VENDOR_MASTER_API: unitApi + '/vendors/',
            DROPDOWN_API: unitApi + '/vendors/dropdown'
          },
          STORE_ITEM_MAPPER: {
            STORE_ITEM_MAPPER_DROPDOWN_API: unitApi + '/store/item/mapper/store/',
            MULTIPLE_STORE_ITEM_MAPPER_DROPDOWN_API: unitApi + '/store/item/mapper/multiplestore',
            STORE_ITEM_MAPPER_API: unitApi + '/store/item/mapper/',
            STORE_ITEM_REMOVE_API: unitApi + '/store/item/mapper/remove',
          },
          STORE_WISE_GENERIC_MAPPER: {
        	STORE_WISE_GENERIC_DROPDOWN_API: globalApi + '/storewiseGenericMapper/dropdown',
            STORE_WISE_GENERIC_API: unitApi + '/storewise/generic/mapper/',
            STORE_WISE_GENERIC_DROPDOWN: unitApi + '/storewise/generic/mapper/dropdown',
            STORE_WISE_GENERIC_SEARCH_API: unitApi + '/storewise/generic/mapper/storeWiseGenericMapper/search/'

          },
          RACK_MASTER: {
            RACK_MASTER_API: unitsApi + '/rack/'
          },
          PRODUCT_CATEGORY_MAPPING: {
            PRODUCT_CATEGORY_MAPPING_API: unitApi + '/product/restrictions/product/category/mapper/',
            PRODUCT_CATEGORY_MAPPING_SAVE: unitApi + '/product/restrictions/pcMapper/',
          },
          ITEM_RESTRICTION: {
            ITEM_RESTRICTION_API: unitApi + '/product/restrictions/item/restrictions/',
            GET_ITEM_RESTRICTION: unitApi + '/product/restrictions/itemRestrictions/',
            SAVE_API: unitApi + '/item/restrictions/',
          }
        },
        APPLICATION_CONFIGURATION: {
          APPLICATION_CONFIGURATION_SAVE_API: unitsApi + '/applications/configurations/save',
          APPLICATION_CONFIGURATION_UPDATE_API: unitsApi + '/applications/configurations/',
          APPLICATION_CONFIGURATION_GET_API: unitsApi + '/applications/configurations/',
          TAX_DROPDOWN: globalsApi + '/tax/dropdown',
        },
        CONSUMABLE_BARCODE_CONFIG: {
          CONSUMABLE_BARCODE_CONFIG_GET_API: globalsApi + '/consumableBarcodeConfig/',
          CONSUMABLE_BARCODE_CONFIG_SAVE_API: globalsApi + '/consumableBarcodeConfig/save',
          CONSUMABLE_BARCODE_CONFIG_UPDATE_API: globalsApi + '/consumableBarcodeConfig/',
        },
        PREFIX_CONFIGURATION: {
          DROPDOWN_API: unitsApi + '/configuration/prefixconfiguration/dropdowns',
          SEARCH_API: unitsApi + '/configuration/prefixconfiguration/',
          DETAIL_API: unitsApi + '/configuration/prefixconfiguration/',
          SAVE_API: unitsApi + '/configuration/prefixconfiguration/save',
          UPDATE_API: unitsApi + '/configuration/prefixconfiguration/'
        }
      },
      
    },

    PHARMACY: {
      OP: {
        WALK_IN_SALES: {
          DROPDOWN_API: pharmacyApi + '/sales/walkins/dropdowns',
          ALLOCATE_API: pharmacyApi + '/sales/walkins/items/1/',
          ALLOCATE_BATCH_API: pharmacyApi + '/sales/walkins/batch/allocate/1/',
          SAVE_API: pharmacyApi + '/sales/walkins',
          REMOVE_FROM_CONTRACT: pharmacyApi + '/sales/walkins/removeContractItems',
          STOCK_CHECK: pharmacyApi + '/sales/walkins/items/alldata/',
          EXPIRED_STOCK_CHECK: pharmacyApi +'/sales/walkins/items/allexpiredstockdata/'
        },
        BILL_LIST: {
          DROPDOWN_API: pharmacyApi + '/sales/bills/dropdowns',
          SEARCH_API: pharmacyApi + '/sales/bills/search',
          DETAILS_API: pharmacyApi + '/sales/bills/',
          CANCEL_API: pharmacyApi + '/sales/walkins/revertQty/'
        },
        SALES_RETURN: {
          SEARCH_API: pharmacyApi + '/op/sale/return/search',
          DETAILS_API: pharmacyApi + '/op/sale/return/',
          DETAIL_SEARCH_API: pharmacyApi + '/op/sale/return/get/bill/search',
          ADD_BILL_API: pharmacyApi + '/op/sale/return/add/bill/',
          SAVE_API: pharmacyApi + '/op/sale/return/save',
          DROPDOWN_API: pharmacyApi + '/op/sale/return/dropdowns',
          CANCEL_API: pharmacyApi + '/op/sale/return/cancel',
          UPDATE_RETURN_API : pharmacyApi + '/op/sale/return/update'
        },
        SALES_WORKLIST: {
          DROPDOWN_API: pharmacyApi + '/op/sale/workList/dropdowns',
          SEARCH_API: pharmacyApi + '/op/sale/workList/search',
          DETAILS_API: pharmacyApi + '/op/sale/workList/',
          SUBSTITUTE_API: pharmacyApi + '/op/sale/workList/substitute/',
          AVAILABLE_QTY_API: pharmacyApi + '/sales/walkins/items/availableqty/',
          CANCEL_API: pharmacyApi + '/op/sale/workList/cancel'
        },
        PHARMACOLOGICAL_VALIDATION: {
          SEARCH_API: pharmacyApi + '/op/pharmacology/validation/search',
          ACTIVE_MEDICATION_API: pharmacyApi + '/ip/pharmacology/validation/',
          DETAIL_SEARCH_API: pharmacyApi + '/op/pharmacology/validation/',
          SAVE_API: pharmacyApi + '/op/pharmacology/validation/save',
          DROPDOWN_API: pharmacyApi + '/op/pharmacology/validation/dropdowns'
        }
      },

      IP: {
        PHARMACOLOGICAL_VALIDATION: {
          SEARCH_API: pharmacyApi + '/ip/pharmacology/validation/search',
          ACTIVE_MEDICATION_API: pharmacyApi + '/ip/pharmacology/validation/',
          DETAIL_SEARCH_API: pharmacyApi + '/ip/pharmacology/validation/',
          SAVE_API: pharmacyApi + '/ip/pharmacology/validation/save',
          DROPDOWN_API: pharmacyApi + '/ip/pharmacology/validation/dropdowns'
        },
        ASSIGN_PICKER_DISPENSER: {
          SEARCH_API: pharmacyApi + '/ip/assign/picker/search',
          DETAIL_API: pharmacyApi + '/ip/assign/picker/',
          SUBSTITUTE_API: pharmacyApi + '/ip/assign/picker/substitute/',
          SAVE_API: pharmacyApi + '/ip/assign/picker/save',
          DROPDOWN_API: pharmacyApi + '/ip/assign/picker/dropdowns'
        },
        PENDING_ISSUES: {
          SEARCH_API: pharmacyApi + '/ip/pending/issues/search',
          DETAIL_API: pharmacyApi + '/ip/pending/issues/',
          SAVE_API: pharmacyApi + '/ip/pending/issues/save',
          DROPDOWN_API: pharmacyApi + '/ip/pending/issues/dropdowns',
          CANCEL_PENDING_ISSUE_REQUEST : pharmacyApi + '/ip/pending/issues/cancelPendingIssue',
          Previous_Prescription : pharmacyApi + '/ip/pending/issues/pendingpreviousissues'
        },
        PENDING_HANDOVER: {
          SEARCH_API: pharmacyApi + '/ip/pending/handover/worklist/search',
          DETAIL_API: pharmacyApi + '/ip/pending/handover/worklist/',
          SAVE_API: pharmacyApi + '/ip/pending/handover/worklist/save',
          DROPDOWN_API: pharmacyApi + '/ip/pending/handover/worklist/dropdowns'
        },
        PATIENT_ISSUE_ACCEPTANCE: {
          MEDICATION_SEARCH_API: pharmacyApi + '/ip/pending/issues/acceptance/medication/search',
          MEDICATION_DROPDOWN_API: pharmacyApi + '/ip/pending/issues/acceptance/consumable/dropdowns',
          MEDICATION_DETAIL_API: pharmacyApi + '/ip/pending/issues/acceptance/medication/',
          MEDICATION_SAVE_API: pharmacyApi + '/ip/pending/issues/acceptance/medication/save',
          CONSUMABLE_SEARCH_API: pharmacyApi + '/ip/pending/issues/acceptance/consumable/search',
          CONSUMABLE_DROPDOWN_API: pharmacyApi + '/ip/pending/issues/acceptance/consumable/dropdowns',
          UPDATED_CONSUMABLE_SAVE_API: pharmacyApi + '/ip/pending/issues/acceptance/consumable/save',
          CONSUMABLE_DETAIL_API: pharmacyApi + '/ip/pending/issues/acceptance/consumable/',
          CONSUMABLE_SAVE_API: pharmacyApi + '/ip/pending/issues/acceptance/medication/save',
          NEW_CONSUMABLE_SEARCH_API: pharmacyApi + '/in/patient/consumable/issue/search',
          NEW_CONSUMABLE_SEARCH_API_FOR_ISSUE_ACCEPTANCE: pharmacyApi + '/in/patient/consumable/issue/search/issue/acceptance',
          NEW_CONSUMABLE_DROPDOWN_API: pharmacyApi + '/in/patient/consumable/issue/dropdowns',
          NEW_CONSUMABLE_DETAIL_API: pharmacyApi + '/in/patient/consumable/issue/',
          NEW_CONSUMABLE_SAVE_API: pharmacyApi + '/in/patient/consumable/issue/save',
          NEW_CONSUMABLE_ACCEPTANCE_DETAIL_API: pharmacyApi + '/in/patient/consumable/issue/acceptance'
        },
        PATIENT_ISSUE_REJECTION: {
          MEDICATION_DROPDOWN_API: pharmacyApi + '/ip/patient/issues/rejection/medication/dropdown',
          MEDICATION_SEARCH_API: pharmacyApi + '/ip/patient/issues/rejection/medication/search',
          MEDICATION_DETAIL_API: pharmacyApi + '/ip/patient/issues/rejection/medication/',
          MEDICATION_SAVE_API: pharmacyApi + '/ip/patient/issues/rejection/medication/save',
      	  MEDICATION_DROPDOWN_API: pharmacyApi + '/ip/patient/issues/rejection/medication/dropdowns',
          CONSUMABLE_SEARCH_API: pharmacyApi + '/ip/patient/issues/rejection/consumable/search',
          CONSUMABLE_DETAIL_API: pharmacyApi + '/ip/patient/issues/rejection/consumable/',
          CONSUMABLE_SAVE_API: pharmacyApi + '/ip/patient/issues/rejection/consumable/save',
          CONSUMABLE_DROPDOWN_API: pharmacyApi + '/ip/patient/issues/rejection/consumable/dropdowns'
        },
        PENDING_REJECTED_REJECTION: {
          MEDICATION_SEARCH_API: pharmacyApi + '/ip/pending/rejected/rejection/medication/search',
          CONSUMABLE_SEARCH_API: pharmacyApi + '/ip/pending/rejected/rejection/consumable/search',
          MEDICATION_DETAIL_API: pharmacyApi + '/ip/pending/rejected/rejection/medication/',
          MEDICATION_DROPDOWN_API: pharmacyApi + '/ip/pending/rejected/rejection/medication/dropdowns',
          CONSUMABLE_DETAIL_API: pharmacyApi + '/ip/pending/rejected/rejection/consumable/',
          MEDICATION_SAVE_API: pharmacyApi + '/ip/pending/rejected/rejection/medication/save',
          CONSUMABLE_SAVE_API: pharmacyApi + '/ip/pending/rejected/rejection/consumable/save',
          CONSUMABLE_DROPDOWN_API: pharmacyApi + '/ip/pending/rejected/rejection/consumable/dropdowns'
        },
        PATIENT_INDENT: {
          SEARCH_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/search',
          PATIENT_SEARCH_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/patient/search',
          DROPDOWN_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/dropdowns',
          SAVE_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/save',
          DETAIL_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/',
          UPDATE_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/',
          APPROVE_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/approve',
          CANCEL_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/cancel/',
          Previous_Indent : pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/previousindentdetails'
        },
        PATIENT_RETURN_ACCEPTANCE: {
          MEDICATION_SEARCH_API: pharmacyApi + '/ip/patient/return/acceptance/medication/search',
          MEDICATION_DETAIL_API: pharmacyApi + '/ip/patient/return/acceptance/medication/',
          MEDICATION_SAVE_API: pharmacyApi + '/ip/patient/return/acceptance/medication/save',
          MEDICATION_DROPDOWN_API: pharmacyApi + '/ip/patient/return/acceptance/medication/dropdowns',
          CONSUMABLE_SEARCH_API: pharmacyApi + '/ip/patient/return/acceptance/consumable/search',
          CONSUMABLE_DETAIL_API: pharmacyApi + '/ip/patient/return/acceptance/consumable/',
          CONSUMABLE_SAVE_API: pharmacyApi + '/ip/patient/return/acceptance/consumable/save',
          CONSUMABLE_DROPDOWN_API: pharmacyApi + '/ip/patient/return/acceptance/consumable/dropdowns'
        }
      },

      IN_PATIENT_PHARMACY: {
        PATIENT_CONSUMABLE_ISSUE: {
          SEARCH_API: pharmacyApi + '/in/patient/consumable/issue/search',
          DETAIL_API: pharmacyApi + '/in/patient/consumable/issue/',
          SAVE_API: pharmacyApi + '/in/patient/consumable/issue/save',
          DROPDOWN_API: pharmacyApi + '/in/patient/consumable/issue/dropdowns',
          ISSUE_SEARCH_API: pharmacyApi + '/in/patient/consumable/issue/list/search',
          ISSUE_SEARCH_FOR_DRUG_RECALL_API: pharmacyApi + '/in/patient/consumable/issue/list/search/drug/recall',
          ISSUE_DETAIL_API: pharmacyApi + '/in/patient/consumable/issue/list/',
          // CANCEL_API: pharmacyApi + '/nursing/pharmacy/patient/indent/consumable/cancel/',
          ISSUE_DROPDOWN_API: pharmacyApi + '/in/patient/consumable/issue/list/dropdowns'
        }
      },

      NURSING: {
        PATIENT_ISSUE_RETURN: {
          ITEM_AUTOPOPULATE_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/item/search/',
          MEDICATION_DROPDOWN_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/dropdown',
          MEDICATION_SEARCH_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/search',
          MEDICATION_ISSUE_SEARCH_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/againstIssue/search',
          MEDICATION_ITEM_SEARCH_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/againstItem/search',
          MEDICATION_ADD_ITEMS_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/add/new',
          MEDICATION_SAVE_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/save',
          MEDICATION_DETAIL_API: pharmacyApi + '/nursing/workstation/patient/issue/return/medication/',
          CONSUMABLE_SEARCH_API: pharmacyApi + '/nursing/workstation/patient/issue/return/consumable/search',
          CONSUMABLE_ISSUE_SEARCH_API: pharmacyApi + '/nursing/workstation/patient/issue/return/consumable/againstIssue/search',
          CONSUMABLE_ITEM_SEARCH_API: pharmacyApi + '/nursing/workstation/patient/issue/return/consumable/againstItem/search',
          CONSUMABLE_ADD_ITEMS_API: pharmacyApi + '/nursing/workstation/patient/issue/return/consumable/add/new',
          CONSUMABLE_SAVE_API: pharmacyApi + '/nursing/workstation/patient/issue/return/consumable/save',
          CONSUMABLE_DROPDOWN_API: pharmacyApi + '/nursing/workstation/patient/issue/return/consumable/dropdown',
          CONSUMABLE_DETAIL_API: pharmacyApi + ''
        },
        RETURN_EMPTY_AMPOULES: {
          SEARCH_API: pharmacyApi + '/nursing/narcoticsStore/returnEmptyAmpoules/search',
          DETAIL_API: pharmacyApi + '/nursing/narcoticsStore/returnEmptyAmpoules/',
          DROPDOWN_API: pharmacyApi + '/nursing/narcoticsStore/returnEmptyAmpoules/dropdowns',
          SAVE_API: pharmacyApi + '/nursing/narcoticsStore/returnEmptyAmpoules/save'
        },
        BILLABLE_CONSUMPTION: {
          SEARCH_API: pharmacyApi + '/nursing/pharmacy/billable/consumption/search',
          DETAIL_API: pharmacyApi + '/nursing/pharmacy/billable/consumption/',
          SAVE_API: pharmacyApi + '/nursing/pharmacy/billable/consumption/save',
          DROPDOWN_API: pharmacyApi + '/nursing/pharmacy/billable/consumption/dropdowns'
        },
        RETURN_BILLABLE_CONSUMPTION: {
          SEARCH_API: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/search',
          DETAIL_API: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/',
          SEARCH_NEW_API: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/search/new',
          ADD_NEW_API: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/add/new',
          ADD_MULTIPLE_ITEMS: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/add/new',
          SAVE_API: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/save',
          DROPDOWN_API: pharmacyApi + '/nursing/pharmacy/return/billable/consumption/dropdowns'
        },
        CRASH_CART: {
          SEARCH_API: pharmacyApi + '/nursing/crashCart/crash/cart/comsumption/search',
          DETAIL_API: pharmacyApi + '/nursing/crashCart/crash/cart/comsumption/',
          SAVE_API: pharmacyApi + '/nursing/crashCart/crash/cart/comsumption',
          CANCEL_API: pharmacyApi + '/nursing/crashCart/crash/cart/comsumption/cancel/'
        },
        ADMINISTRATION_APPROVAL: {
          SEARCH_API: pharmacyApi + '/nursing/clinicalWorkstation/order/crashCart/medications/approval/search',
          DETAIL_API: pharmacyApi + '/nursing/clinicalWorkstation/order/crashCart/medications/approval/',
          SAVE_API: pharmacyApi + '/nursing/clinicalWorkstation/order/crashCart/medications/approval/save/',
          CANCEL_API: pharmacyApi + '/nursing/clinicalWorkstation/order/crashCart/medications/approval/cancel'
        },
        CRASH_CART_REQUEST_APPROVAL: {
          SEARCH_API: pharmacyApi + '/nursing/crashCart/crash/cart/request/approval/search',
          DETAIL_API: pharmacyApi + '/nursing/crashCart/crash/cart/request/approval/',
          SAVE_API: pharmacyApi + '/nursing/crashCart/crash/cart/request/approval'
        },
        DRUG_ADMINISTRATOR: {
          SEARCH_API: pharmacyApi + '/nursing/workstation/drug/administration/search',
          REASON_DROPDOWN_API: pharmacyApi + '/nursing/workstation/drug/administration/reasondropdown',
          SAVE_API: pharmacyApi + '/nursing/workstation/drug/administration/save'
        },
        DISCHARGE_RECONCILATION: {
          SEARCH_API: pharmacyApi + '/nursing/discharge/medication/reconciliation/search',
          DROPDOWN_API: pharmacyApi + '/nursing/discharge/medication/reconciliation/dropdowns',
          SAVE_API: pharmacyApi + '/nursing/discharge/medication/reconciliation/save',
          DETAIL_API: pharmacyApi + '/nursing/discharge/medication/reconciliation/',
        }
      },

      NARCOTICS: {
        NARCOTICS_ISSUE_WORKLIST: {
          DROPDOWN_API: pharmacyApi + '/narcotics/issue/worklist/dropdowns',
          SEARCH_API: pharmacyApi + '/narcotics/issue/worklist/search',
          DETAIL_API: pharmacyApi + '/narcotics/issue/worklist/',
          SAVE_API: pharmacyApi + '/narcotics/issue/worklist/save'
        },
        DELIVERY_WORKLIST: {
          DROPDOWN_API: pharmacyApi + '/narcotics/delivery/worklist/dropdowns',
          SEARCH_API: pharmacyApi + '/narcotics/delivery/worklist/search',
          DETAIL_API: pharmacyApi + '/narcotics/delivery/worklist/',
          SAVE_API: pharmacyApi + '/narcotics/delivery/worklist/save'
        },
        ACCEPTANCE_EMPTY_AMPOULES: {
          SEARCH_API: pharmacyApi + '/narcotics/acceptanceEmptyAmpoules/search',
          DETAIL_API: pharmacyApi + '/narcotics/acceptanceEmptyAmpoules/',
          SAVE_API: pharmacyApi + '/narcotics/acceptanceEmptyAmpoules/save',
          DROPDOWN_API: pharmacyApi + '/narcotics/acceptanceEmptyAmpoules/dropdowns'
        }
      }
    },

    INVENTORY: {
      STORE: {
        STORE_INDENT: {
          DROPDOWN_API: inventoryApi + '/stores/indents/dropdowns',
          SEARCH_API: inventoryApi + '/stores/indents/search',
          APPROVE_API: inventoryApi + '/stores/indents/approve',
          CANCEL_API: inventoryApi + '/stores/indents/cancel/',
          DETAILS_API: inventoryApi + '/stores/indents/',
          SAVE_API: inventoryApi + '/stores/indents',
          UPDATE_API: inventoryApi + '/stores/indents/',
        },
        STORE_INDENT_APPROVAL: {
          DROPDOWN_API: inventoryApi + '/stores/indents/approval/dropdowns',
          SEARCH_API: inventoryApi + '/stores/indents/approval/search',
          APPROVE_API: inventoryApi + '/stores/indents/approval/approve/',
          CANCEL_API: inventoryApi + '/stores/indents/approval/cancel/',
          DETAILS_API: inventoryApi + '/stores/indents/approval/',
        },
        PENDING_STORE_INDENT: {
          DROPDOWN_API: inventoryApi + '/stores/indents/pending/dropdowns',
          SEARCH_API: inventoryApi + '/stores/indents/pending/search',
          CANCEL_API: inventoryApi + '/stores/indents/pending/close/',
          DETAILS_API: inventoryApi + ' /stores/indents/pending/',

        },
        STORE_STOCK_DASHBOARD: {
          SEARCH_API: inventoryApi + '/stores/stock/dashboard/search',
          BATCH_API: inventoryApi + '/stores/stock/dashboard/batch/',
          nextexpiredbatch: inventoryApi + '/stores/stock/dashboard/nextexpiredbatch/',
          Expired_BATCH_API: inventoryApi + '/stores/stock/dashboard/expiredbatch/',
          SEARCH_API_DATE: inventoryApi + '/stores/stock/dashboard/searchdatewise',
          ITEM_WISE_BATCH_API:inventoryApi + '/stores/stock/dashboard/item/batch/',
          BATCH_WISE_BATCH_API:inventoryApi + '/stores/stock/dashboard/item/batch/batch/',
          EXPIRED_STOCK_DATA: inventoryApi + '/stores/stock/dashboard/expiredstorestockdashboard',
          ZERO_STOCK_DATA: inventoryApi + '/stores/stock/dashboard/searchdatewisezerostock',
          ZERO_STOCK_BATCH_API: inventoryApi + '/stores/stock/dashboard/batch/zerostock/',
          ZERO_WITH_STOCK_BATCH_API: inventoryApi + '/stores/stock/dashboard/searchdatewisezerowithstock',
          ExpiredNear_Months: inventoryApi + '/stores/stock/dashboard/nextexpiredstorestockdashboard',
          Stock_in_transit: inventoryApi + '/stores/stock/dashboard/stockintransit',
          SUBSTITUTE_LIST_FOR_DASHBOARD: inventoryApi + '/stores/stock/dashboard/getSubstituteDetailsForStoreStockDashboard'
        },
        MATERIAL_RETURN: {
          SEARCH_API: inventoryApi + '/stores/material/return/search',
          SEARCH_API_NEW: inventoryApi + '/stores/material/return/againstIssue/search',
          SAVE_API: inventoryApi + '/stores/material/return',
          DETAILS_API: inventoryApi + '/stores/material/return/',
          DROPDOWN_API: inventoryApi + '/stores/material/return/dropdowns',
          RETURN_REASON_API: inventoryApi + '/stores/material/return//dropdowns/return/reason',
          APPROVE_API: inventoryApi + '/stores/material/return/approve',
          ADD_NEW_DROPDOWN_API: inventoryApi + '/stores/material/return/carrier/dropdowns',
          UPDATE_API: inventoryApi + '/stores/material/return/',
          CANCEL_API: inventoryApi + '/stores/material/return/cancel/',
          Near_expiry_search_api: inventoryApi + '/stores/material/return/againstExpireItem/search',
          GET_MATERIAL_RETURN_AGAINST_ISSUE_NUMBER:inventoryApi +'/stores/material/return/materialReturn/getDetailsByIssueId/'
        },
        MATERIAL_RETURN_ACCEPTANCE: {
          DROPDOWN_API: inventoryApi + '/stores/material/return/acceptance/dropdowns',
          SEARCH_API: inventoryApi + '/stores/material/return/acceptance/search',
          APPROVE_API: inventoryApi + '/stores/material/return/acceptance/',
          DETAIL_API: inventoryApi + '/stores/material/return/acceptance/'
        },
        MATERIAL_PICKER: {
          SEARCH_API: inventoryApi + '/stores/material/picker/search',
          DROPDOWN_API: inventoryApi + '/stores/material/picker/dropdowns',
          DETAILS_API: inventoryApi + '/stores/material/picker/',
          SAVE_API: inventoryApi + '/stores/material/picker'
        },
        MATERIAL_ISSUE: {
          MATERIAL_PENDING_ISSUE: {
            DROPDOWN_API: inventoryApi + '/stores/material/issue/pending/dropdowns',
            SEARCH_API: inventoryApi + '/stores/material/issue/pending/search',
            DETAILS_API: inventoryApi + '/stores/material/issue/pending/',
            SAVE_API: inventoryApi + '/stores/material/issue/pending',
            MATERIAL_LIST_API:  inventoryApi + '/stores/material/issue/list/',
            NEW_DETAILS_API: procurementApi + '/consignments/patient/consumption/list/',
          },
          MATERIAL_ISSUE_LIST: {
        	DROPDOWN_API: inventoryApi + '/stores/material/issue/list/dropdowns',
            SEARCH_API: inventoryApi + '/stores/material/issue/list/search',
            DETAILS_API: inventoryApi + '/stores/material/issue/list/',
            SAVE_API: inventoryApi + '/stores/material/issue/list',
            DIRECT_ISSUE_DROPDOWN: inventoryApi + '/stores/material/issue/list/direct/issue/dropdowns',
          }
        },
        MATERIAL_ISSUE_ACCEPTANCE: {
          DROPDOWN_API: inventoryApi + '/stores/material/issue/acceptance/dropdowns',
          SEARCH_API: inventoryApi + '/stores/material/issue/acceptance/search',
          DETAILS_API: inventoryApi + '/stores/material/issue/acceptance/',
          SAVE_API: inventoryApi + '/stores/material/issue/acceptance/',
        },
        REJECTED_MATERIAL_ACCEPTANCE: {
          DROPDOWN_API: inventoryApi + '/stores/reject/material/acceptance/dropdowns',
          SEARCH_API: inventoryApi + '/stores/reject/material/acceptance/search',
          DETAILS_API: inventoryApi + '/stores/reject/material/acceptance/',
          SAVE_API: inventoryApi + '/stores/reject/material/acceptance/'
        },
        BATCH_CHANGE: {
          SEARCH_API: inventoryApi + '/stores/batch/change/search',
          APPROVE_API: inventoryApi + '/stores/batch/change/submit',
          CANCEL_API: inventoryApi + '/stores/batch/change/cancel/',
          DETAILS_API: inventoryApi + '/stores/batch/change/',
          SAVE_API: inventoryApi + '/stores/batch/change',
          UPDATE_API: inventoryApi + '/stores/batch/change/',
          CANCEL_API: inventoryApi + '/stores/batch/change/cancel/'
        },
        BATCH_CHANGE_APPROVAL: {
          SEARCH_API: inventoryApi + '/stores/batch/change/approval/search',
          APPROVE_API: inventoryApi + '/stores/batch/change/approval/approve/',
          DETAILS_API: inventoryApi + '/stores/batch/change/approval/',
          BATCH_APPROVE_API: inventoryApi + '/stores/batch/change/approval/approve/'
        },
        PENDING_SHELF_ALLOCATION: {
          SEARCH_API: inventoryApi + '/stores/shelf/allocation/search',
          STATUS_UPDATE_API: inventoryApi + '/stores/shelf/allocation/status/',
          ALLOCATED_ITEMS: inventoryApi + '/stores/shelf/allocation/',
          UPDATE_API: inventoryApi + '/stores/shelf/allocation/'
        },
        RETURNABLE_GATEPASS: {
          DROPDOWN_API: inventoryApi + '/stores/returnable/gatepass/dropdowns',
          SEARCH_API: inventoryApi + '/stores/returnable/gatepass/search',
          APPROVE_API: inventoryApi + '/stores/returnable/gatepass/submit',
          CANCEL_API: inventoryApi + '/stores/returnable/gatepass/cancel/',
          SAVE_API: inventoryApi + '/stores/returnable/gatepass/',
          DETAIL_API: inventoryApi + '/stores/returnable/gatepass/',
          UPDATE_API: inventoryApi + '/stores/returnable/gatepass/'
        },
        RETURNABLE_GATEPASS_APPROVAL: {
          DROPDOWN_API: inventoryApi + '/stores/returnable/gatepass/approval/dropdowns',
          SEARCH_API: inventoryApi + '/stores/returnable/gatepass/approval/search',
          APPROVE_API: inventoryApi + '/stores/returnable/gatepass/approval/approve/',
          CANCEL_API: inventoryApi + '/stores/returnable/gatepass/approval/cancel/',
          DETAIL_API: inventoryApi + '/stores/returnable/gatepass/approval/'
        },
        MATERIAL_INWARDS: {
          DROPDOWN_API: inventoryApi + '/stores/material/inward/dropdowns',
          SEARCH_API: inventoryApi + '/stores/material/inward/search',
          SAVE_API: inventoryApi + '/stores/material/inward',
          CANCEL_API: inventoryApi + '/stores/material/inward/cancel/',
          DETAIL_API: inventoryApi + '/stores/material/inward/',
          UPDATE_API: inventoryApi + '/stores/material/inward/',
          RETURNABLE_GATEPASS_SEARCH: inventoryApi + '/stores/material/inward/returnablegatepass/search',
          ADD_ITEM_API: inventoryApi + '/stores/material/inward/returnableGatepass/detail/',
          ADD_MULTIPLE_ITEM_API: inventoryApi + '/stores/material/inward/returnableGatepass/multiplegatepassdetail',
          GET_CREDIT_NOTE_AMOUNT_API: inventoryApi + '/stores/material/inward/returnableGatepass/vendorwisecreditnote/',
        },
        MATERIAL_INWARDS_APPROVAL: {
          DROPDOWN_API: inventoryApi + '/stores/material/inward/approval/dropdowns',
          SEARCH_API: inventoryApi + '/stores/material/inward/approval/search',
          DETAIL_API: inventoryApi + '/stores/material/inward/approval/',
          APPROVE_API: inventoryApi + '/stores/material/inward/approval/'
        },
        DRUG_RECALL: {
          SEARCH_API: inventoryApi + '/stores/drug/recall/search',
          DETAIL_API: inventoryApi + '/stores/drug/recall/',
          SAVE_API: inventoryApi + '/stores/drug/recall',
          UPDATE_API: inventoryApi + '/stores/drug/recall/',
          RECALL_DRUG: inventoryApi + '/stores/recall/drug/search',
          APPROVE_API: inventoryApi + '/stores/drug/recall/approve',
          CANCEL_API: inventoryApi + '/stores/drug/recall/cancel/'
        }
      },
      NURSING: {
        NON_BILLABLE_CONSUMPTION: {
          SEARCH_API: inventoryApi + '/nursing/non/billable/consumption/search',
          DETAIL_API: inventoryApi + '/nursing/non/billable/consumption/',
          SAVE_API: inventoryApi + '/nursing/non/billable/consumption',
          UPDATE_API: inventoryApi + '/nursing/non/billable/consumption/',
          APPROVE_API: inventoryApi + '/nursing/non/billable/consumption/submit',
          CANCEL_API: inventoryApi + '/nursing/non/billable/consumption/cancel/',
          DROPDOWN_API: inventoryApi + '/nursing/non/billable/consumption/dropdowns'
        },
        CONSUMABLE_ACCEPTANCE: {
          SEARCH_API: inventoryApi + '/nursing/consumable/acceptance/search',
          DETAIL_API: inventoryApi + '/nursing/consumable/acceptance/',
          APPROVE_API: inventoryApi + '/nursing/consumable/acceptance/approve/',
          DETAIL_APPROVE_API: inventoryApi + '/nursing/consumable/acceptance/approve/',
          CANCEL_API: inventoryApi + '/nursing/consumable/acceptance/cancel/',
          DROPDOWN_API: inventoryApi + '/nursing/consumable/acceptance/dropdowns'
        },
      },
      STOCK: {
        OPENING_BALANCE: {

          SEARCH_API: inventoryApi + '/stock/opening/balances/search',
          DETAILS_API: inventoryApi + '/stock/opening/balances/',
          APPROVE_API: inventoryApi + '/stock/opening/balances/approve/',
          CANCEL_API: inventoryApi + '/stock/opening/balances/cancel/',
          UPDATE_API: inventoryApi + '/stock/opening/balances/',
          SAVE_API: inventoryApi + '/stock/opening/balances',
        },
        IMPLANT_OPENING_BALANCE: {
          SEARCH_ITEM: inventoryApi + '/stock/opening/implant/balances/list',
          SEARCH_API: inventoryApi + '/stock/opening/implant/balances/search',
          DETAILS_API: inventoryApi + '/stock/opening/implant/balances/',
          APPROVE_API: inventoryApi + '/stock/opening/implant/balances/approve',
          CANCEL_API: inventoryApi + '/stock/opening/implant/balances/cancel/',
          UPDATE_API: inventoryApi + '/stock/opening/implant/balances/update/',
          SAVE_API: inventoryApi + '/stock/opening/implant/balances/save',
        }
      },
      SCRAP: {
        SCRAP_REQUEST: {
          DROPDOWN_API: inventoryApi + '/scraps/scrap/request/dropdowns',
          //DROPDOWN_BY_USER_ID_API: inventoryApi + '/scraps/scrap/request/dropdowns/',
          APPROVE_API: inventoryApi + '/scraps/scrap/request/approve',
          SEARCH_API: inventoryApi + '/scraps/scrap/request/search',
          DETAIL_API: inventoryApi + '/scraps/scrap/request/',
          UPDATE_API: inventoryApi + '/scraps/scrap/request/',
          CANCEL_API: inventoryApi + '/scraps/scrap/request/cancel/',
          SAVE_API: inventoryApi + '/scraps/scrap/request',
          SCRAP_REASON_API: globalApi + '/scrap/reasons'
        },
        SCRAP_REQUEST_APPROVAL: {
          SEARCH_API: inventoryApi + '/scraps/scrap/request/approval/search',
          DETAIL_API: inventoryApi + '/scraps/scrap/request/approval/',
          DROPDOWN_API: inventoryApi + '/scraps/scrap/request/approval/dropdowns',
          //DROPDOWN_BY_USER_ID_API: inventoryApi + '/scraps/scrap/request/approval/dropdowns/',
          APPROVE_API: inventoryApi + '/scraps/scrap/request/approval/',
          CANCEL_API: inventoryApi + '/scraps/scrap/request/approval/cancel/',
        },
        REQUEST_FOR_ASSET_DISPOSAL: {
          DROPDOWN_API: inventoryApi + '/scrap/request/asset/disposal/worklist/dropdowns',
          SEARCH_API: inventoryApi + '/scrap/request/asset/disposal/worklist/search',
          SAVE_API: inventoryApi + '/scrap/request/asset/disposal/worklist/',
          DETAIL_API: inventoryApi + '/scrap/request/asset/disposal/worklist/'
        },
        WASTE_ASSET_DISPOSAL: {
          DROPDOWN_API: inventoryApi + '/scrap/waste/asset/disposal/worklist/dropdowns',
          SEARCH_API: inventoryApi + '/scrap/waste/asset/disposal/worklist/search',
          SAVE_API: inventoryApi + '/scrap/waste/asset/disposal/worklist/',
          DETAIL_API: inventoryApi + '/scrap/waste/asset/disposal/worklist/'
        },
        WITNESS_APPROVAL: {
          DROPDOWN_API: inventoryApi + '/scrap/witness/approval/dropdowns',
          SEARCH_API: inventoryApi + '/scrap/witness/approval/search',
          SAVE_API: inventoryApi + '/scrap/witness/approval/',
          DETAIL_API: inventoryApi + '/scrap/witness/approval/'
        }
      },
      STOCK_ADJUSTMENT: {
        STOP_TRANSACTION: {
          SEARCH_API: inventoryApi + '/stock/adjustment/stop/transaction/search',
          ZERO_STOCK_SEARCH : inventoryApi + '/stock/adjustment/stop/transaction/add/new/nonzerosearch',
          SAVE_API: inventoryApi + '/stock/adjustment/stop/transaction/add/new',
          SEARCH_ITEM: inventoryApi + '/stock/adjustment/stop/transaction/add/new/search',
          PRODUCT_CATEGORY_API: inventoryApi + '/stock/adjustment/stop/transaction/productCategory/search/',
          UPDATE_API: inventoryApi + '/stock/adjustment/stop/transaction/',
          DETAIL_API: inventoryApi + '/stock/adjustment/stop/transaction/',
          APPROVE_API: inventoryApi + '/stock/adjustment/stop/transaction/approve',
          CANCEL_API: inventoryApi + '/stock/adjustment/stop/transaction/cancel/',         
          STOCK_TRANSIT_API: inventoryApi + '/stock/adjustment/stop/transaction/stockTransit', 
          CHECK_STOP_TRANSCTION: inventoryApi + '/stock/adjustment/stop/transaction/CheckStopTransaction',
          CHECK_STOP_TRANSCTION_BATCH: inventoryApi + '/stock/adjustment/stop/transaction/CheckStopTransactionBatchWise',
          CHECK_STOP_TRANSCTION_BY_STORE: inventoryApi + '/stock/adjustment/stop/transaction/CheckStopTransactionDuration',
          SEND_ALERT: inventoryApi + '/stock/adjustment/stop/transaction/sendAlert/',
          REMOVE_ITEM_BY_PRODUCTCATEGORY: inventoryApi + '/stock/adjustment/stop/transaction/removeItembyProduct/',   
          REMOVE_ITEM_BY_ITEMID : inventoryApi + '/stock/adjustment/stop/transaction/removeItembyItemId',
          CHECK_STOP_TRANSCTION_DETAIL_API : inventoryApi + '/stock/adjustment/stop/transaction/CheckStopTransactionDetail'
        },
        UPLOAD_STOCK: {
          SEARCH_API: inventoryApi + '/stock/adjusments/upload/stock/search',
          DROPDOWN_API: inventoryApi + '/stock/adjusments/upload/stock/dropdown',
          DETAIL_API: inventoryApi + '/stock/adjusments/upload/stock/',
          APPROVE_API: inventoryApi + '/stock/adjusments/upload/stock/approve',
          SAVE_API: inventoryApi + '/stock/adjusments/upload/stock/save',
          CANCEL_API: inventoryApi + '/stock/adjusments/upload/stock/cancel/',
          ADD_ITEM: inventoryApi + '/stock/adjusments/upload/stock/add/item',
          GET_REQUEST: inventoryApi + '/stock/adjusments/upload/stock/get/request',
          UPDATE_API: inventoryApi + '/stock/adjusments/upload/stock/',
          GET_OLD_BATCH:inventoryApi + '/stock/adjusments/upload/stock/get/batch' 
        },
        UPLOAD_STOCK_APPROVAL: {
          SEARCH_API: inventoryApi + '/stock/adjusments/upload/stock/approval/search',
          DROPDOWN_API: inventoryApi + '/stock/adjusments/upload/stock/approval/dropdown',
          DETAIL_API: inventoryApi + '/stock/adjusments/upload/stock/approval/',
          APPROVE_API: inventoryApi + '/stock/adjusments/upload/stock/approval/approve/',
          NEW_APPROVE_API: inventoryApi + '/stock/adjusments/upload/stock/approval/newapprove/',
          CANCEL_API: inventoryApi + '/stock/adjusments/upload/stock/approval/cancel/'
        },
        VARIANCE_APPROVAL: {
          SEARCH_API: inventoryApi + '/stock/adjusments/variance/approval/search',
          DROPDOWN_API: inventoryApi + '/stock/adjusments/variance/approval/dropdown',
          DETAIL_API: inventoryApi + '/stock/adjusments/variance/approval/',
          APPROVE_API: inventoryApi + '/stock/adjusments/variance/approval/approve/',
          CANCEL_API: inventoryApi + '/stock/adjusments/variance/approval/cancel/'
        },
        COMPUTE_STOCK: {
          SEARCH_API: inventoryApi + '/stock/adjustment/compute/stock/search',
          DROPDOWN_API: inventoryApi + '/stock/adjustment/compute/stock/dropdowns',
          CANCEL_API: inventoryApi + '/stock/adjustment/compute/stock/cancel/',
          APPROVE_API: inventoryApi + '/stock/adjustment/compute/stock/approve',
          DETAIL_API: inventoryApi + '/stock/adjustment/compute/stock/',
          DETAIL_APPROVE_API: inventoryApi + '/stock/adjustment/compute/stock/save/'
        },
        CONFIRM_STOCK: {
          SEARCH_API: inventoryApi + '/stock/adjusments/confirm/stock/search',
          DROPDOWN_API: inventoryApi + '/stock/adjusments/confirm/stock/dropdown',
          APPROVE_API: inventoryApi + '/stock/adjusments/confirm/stock/approve',
          CANCEL_API: inventoryApi + '/stock/adjusments/confirm/stock/cancel/',
          DETAIL_API: inventoryApi + '/stock/adjusments/confirm/stock/',
          DETAIL_APPROVE_API: inventoryApi + '/stock/adjusments/confirm/stock/approve/',
          REJECT_CONFIRM_STOCK:inventoryApi + '/stock/adjusments/confirm/stock/rejectconfirmstock/'
        }

      }
    },

    PROCUREMENT: {
      QUOTATION: {
        GET_BATCHES_API: procurementApi + '/quotation/barcode/batches',
        PURCHSE_ORDER: {
          DROPDOWN_API: procurementApi + '/purchase/order/creation/dropdowns',
          GET_PO_API: procurementApi + '/purchase/order/creation/search',
          APPROVE_PO_API: procurementApi + '/purchase/order/creation/approve',
          CANCEL_PO_API: procurementApi + '/purchase/order/creation/cancel/',
          SAVE_API: procurementApi + '/purchase/order/creation/',
          COP_HISTORY: procurementApi + '/purchase/order/creation/',
          FORCEFULLY_CLOSED_API: procurementApi + '/purchase/order/creation/forcefulyclosed/'
        },
        PURCHSE_ORDER_APPROVAL: {
          DROPDOWN_API: procurementApi + '/purchase/order/approval/dropdowns',
          Final_SEARCH_API: procurementApi + '/purchase/order/approval/finalsearch',
          SEARCH_API: procurementApi + '/purchase/order/approval/search',
          APPROVE_API: procurementApi + '/purchase/order/approval/approve',
          DETAILS_API: procurementApi + '/purchase/order/approval/',
          SAVE_API: procurementApi + '/purchase/order/approval/approve/',
          APPROVAL_API: procurementApi + '/purchase/order/approval/secondlevel/approval'
        },
        GRN: {
          GRN_DROPDOWN_API: procurementApi + '/quotation/grn/dropdowns',
          SEARCH_GRN_API: procurementApi + '/quotation/grn/search',
          APPROVE_GRN_API: procurementApi + '/quotation/grn/approve',
          CANCEL_GRN_API: procurementApi + '/quotation/grn/cancel/',
          DROPDOWN_API: procurementApi + '/purchase/order/creation/dropdowns',
          GET_GRN_API: procurementApi + '/quotation/grn/',
          SAVE_GRN_API: procurementApi + '/quotation/grn/',
          GRN_HISTORY: procurementApi + '/quotation/grn/previousgrndetails/',
          PRE_GRN_HISTORY: procurementApi + '/quotation/grn/newpreviousgrndetails/',
        },
        GRN_APPROVAL: {
          DROPDOWN_API: procurementApi + '/quotation/grn/approval/dropdowns',
          SEARCH_API: procurementApi + '/quotation/grn/approval/search',
          APPROVE_API: procurementApi + '/quotation/grn/approval/approve/',
          DETAILS_API: procurementApi + '/quotation/grn/approval/'
        },
        GRN_SETTLEMENT: {
          SEARCH_GRN_SETTLEMENT_API: procurementApi + '/quotation/grn/approval/settlementsearch',
          DETAILS_GRN_SETTLEMENT_API: procurementApi + '/quotation/grn/approval/settlementdetails/',
          SEARCH_GRN_SETTLED_API: procurementApi + '/quotation/grn/approval/settledsearch',
          DETAILS_GRN_SETTLED_API: procurementApi + '/quotation/grn/approval/settleddetails/',
          SETTLE_BILL_API: procurementApi + '/quotation/grn/grnsettled'
        },
        PHYSICAL_INSPECTION: {
          DROPDOWN_API: procurementApi + '/quotation/physical/inspection/dropdown',
          SEARCH_API: procurementApi + '/quotation/physical/inspection/search',
          DETAILS_API: procurementApi + '/quotation/physical/inspection/',
          SAVE_API: procurementApi + '/quotation/physical/inspection/save'
        },
        CONSIGNMENT_STOCK_CONVERSION: {
        	SEARCH_API: procurementApi + '/quotation/consignmentstockconversion/search',
        	DETAILS_API: procurementApi + '/quotation/consignmentstockconversion/',
        	STOCK_CONVERSION_DETAILS_API: procurementApi + '/quotation/consignmentstockconversion/detailsConsignmentStockConversion',
        	SAVE_API: procurementApi + '/quotation/consignmentstockconversion/save',
        	STORE_STOCK_API: procurementApi + '/quotation/consignmentstockconversion/storestocktransfereddata'
        },
        CONSIGNMENT_STOCK_RETURN: {
        	DETAILS_API: procurementApi + '/quotation/consignmentstockreturn/',
        	SEARCH_API: procurementApi + '/quotation/consignmentstockreturn/search',
        	GET_CONSIGNMENT_GRN : procurementApi + '/quotation/consignmentstockreturn/addNewConsignmentReturn/getgrn',
        	GET_CONSIGNMENT_ALL_GRN : procurementApi + '/quotation/consignmentstockreturn/addNewConsignmentReturn/getallgrn',
        	SAVE_CONSIGNMENT_STOCK_RETURN_DETAIL : procurementApi + '/quotation/consignmentstockreturn/save/returndetail',
        	UPDATE_CONSIGNMENT_STOCK_RETURN_DETAIL : procurementApi + '/quotation/consignmentstockreturn/update/returndetail',
        	STOCK_RETURN_DETAILS_API: procurementApi + '/quotation/consignmentstockreturn/detailsConsignmentStockReturn',
        	SAVE_API: procurementApi + '/quotation/consignmentstockreturn/save',
        	HISTORY_API: procurementApi + '/quotation/consignmentstockreturn/history'
        },
        CONSIGNMENT_STOCK_PO_CREATION: {
        	GET_PO_API : procurementApi + '/quotation/consignmentstockpocreation/search',
        	GET_CONSIGNMENT_GRN : procurementApi + '/quotation/consignmentstockpocreation/getgrn',
        	SAVE_DETAIL_API: procurementApi + '/quotation/consignmentstockpocreation/save',
            DETAILS_API: procurementApi + '/quotation/consignmentstockpocreation/',
        	UPDATE_DETAIL_API: procurementApi + '/quotation/consignmentstockpocreation',        	
        	HISTORY_API: procurementApi + '/quotation/consignmentstockpocreation/consignmentpohistory'        	
        },
        CONSIGNMENT_STOCK_PO_INVOICE: {
        	SEARCH_API : procurementApi + '/quotation/consignmentstockpoinvoice/search',
        	GET_APPROVED_PO_API : procurementApi + '/quotation/consignmentstockpoinvoice/getconsignmentpolist',
        	SAVE_API : procurementApi + '/quotation/consignmentstockpoinvoice/save',
        	DETAILS_API : procurementApi + '/quotation/consignmentstockpoinvoice/details',
        	UPDATE_DETAIL_API : procurementApi + '/quotation/consignmentstockpoinvoice/updatedetails',
        	UPDATE_API : procurementApi + '/quotation/consignmentstockpoinvoice/update',
          Cancel_Api : procurementApi + '/quotation/consignmentstockpoinvoice/cancel/'
        },
        QUOTATION_ENQUIRY: {
          DROPDOWN_API: procurementApi + '/quotation/enquiry/dropdowns',
          SEARCH_API: procurementApi + '/quotation/enquiry/search',
          SAVE_API: procurementApi + '/quotation/enquiry/',
          DETAILS_API: procurementApi + '/quotation/enquiry/',
          APPROVE_QUOTATION_API: procurementApi + '/quotation/enquiry/approve',
          CANCEL_QUOTATION_API: procurementApi + '/quotation/enquiry/cancel/',
          SEND_FOR_TECHNICAL_APPROVAL: procurementApi + '/quotation/technical/approval'
        },
        ENQUIRY_SENT_TO_VENDOR: {
          DROPDOWN_API: procurementApi + '/quotation/enquiry/sentToVendor/dropdowns',
          SEARCH_API: procurementApi + '/quotation/enquiry/sentToVendor/search',
          SAVE_API: procurementApi + '/quotation/enquiry/sentToVendor/',
          APPROVE_QUOTATION_API: procurementApi + '/quotation/enquiry/sentToVendor/approve/',
          CANCEL_QUOTATION_API: procurementApi + '/quotation/enquiry/sentToVendor/cancel/',
          SEARCH_QR_API: procurementApi + '/quotation/enquiry/sentToVendor/qr/search/',
          MULTI_SEARCH_QR_API: procurementApi + '/quotation/enquiry/sentToVendor/qr/multiplesearch/',
          DETAILS_QR_ITEMS: procurementApi + '/quotation/enquiry/sentToVendor/qr/details'
        },
        QUOTATION_FILL_IN: {
          DROPDOWN_API: procurementApi + '/quotation/fill/dropdowns',
          SEARCH_API: procurementApi + '/quotation/fill/search',
          SEARCH_VQR_API: procurementApi + '/quotation/fill/vendor/search',
          DETAILS_API: procurementApi + '/quotation/fill/',
          DETAILS_VQR_ITEMS: procurementApi + '/quotation/fill/vendor/details',
          SAVE_API: procurementApi + '/quotation/fill/',
          APPROVE_API: procurementApi + '/quotation/fill/approve',
          CANCEL_API: procurementApi + '/quotation/fill/cancel/'
        },
        TECHNICAL_APPROVAL: {
          DROPDOWN_API: procurementApi + '/quotation/technical/approval/dropdowns',
          SEARCH_API: procurementApi + '/quotation/technical/approval/search',
          DETAILS_API: procurementApi + '/quotation/technical/approval/'
        },
        VENDOR_NEGOTIATION: {
          DROPDOWN_API: procurementApi + '/quotation/vendor/negotiation/dropdowns',
          SEARCH_API: procurementApi + '/quotation/vendor/negotiation/search',
          DETAILS_API: procurementApi + '/quotation/vendor/negotiation/'
        },
        VENDOR_SELECTION: {
          DROPDOWN_API: procurementApi + '/quotation/vendor/selection/dropdowns',
          SEARCH_API: procurementApi + '/quotation/vendor/selection/search',
          DETAILS_API: procurementApi + '/quotation/vendor/selection/'
        }
      },
      PURCHASE: {
        ITEM_REQUEST: {
          DROPDOWN_API: procurementApi + '/purchase/item/request/dropdowns',
          SEARCH_API: procurementApi + '/purchase/item/request/search',
          DETAILS_API: procurementApi + '/purchase/item/request/',
          SAVE_API: procurementApi + '/purchase/item/request/',
          CANCEL_API: procurementApi + '/purchase/item/request/cancel/',
          APPROVE_API: procurementApi + '/purchase/item/request/approve/'
        },
        ITEM_APPROVAL: {
          DROPDOWN_API: procurementApi + '/purchase/item/request/approval/dropdowns',
          SEARCH_API: procurementApi + '/purchase/item/request/approval/search',
          DETAILS_API: procurementApi + '/purchase/item/request/approval/',
          APPROVE_API: procurementApi + '/purchase/item/request/approval/approve',
          CANCEL_API: procurementApi + '/purchase/item/request/approval/cancel/',
          SAVE_API: procurementApi + '/purchase/item/request/approval/approve/'
        },
        ITEM_CREATION: {
          DROPDOWN_API: procurementApi + '/purchase/item/creation/pending/dropdowns',
          SEARCH_API: procurementApi + '/purchase/item/creation/pending/search',
          DETAILS_API: procurementApi + '/purchase/item/creation/pending/'
        },
        PURCHASE_REQUEST: {
          DROPDOWN_API: procurementApi + '/purchase/requests/dropdowns',
          SEARCH_API: procurementApi + '/purchase/requests/search',
          SAVE_API: procurementApi + '/purchase/requests/',
          APPROVE_API: procurementApi + '/purchase/requests/approve',
          CANCEL_API: procurementApi + '/purchase/requests/cancel/'
        },
        PURCHASE_REQUEST_APPROVAL: {
          DROPDOWN_API: procurementApi + '/purchase/requests/approval/dropdowns',
          SEARCH_API: procurementApi + '/purchase/requests/approval/search',
          APPROVE_API: procurementApi + '/purchase/requests/approval/approve',
          CANCEL_API: procurementApi + '/purchase/requests/approval/cancel/',
          SAVE_API: procurementApi + '/purchase/requests/approval/'
        },
        LOCAL_PURCHASE_REQUEST: {
          DROPDOWN_API: procurementApi + '/local/purchase/requests/dropdowns',
          SEARCH_API: procurementApi + '/local/purchase/requests/search',
          SAVE_API: procurementApi + '/local/purchase/requests/',
          APPROVE_API: procurementApi + '/local/purchase/requests/approve',
          CANCEL_API: procurementApi + '/local/purchase/requests/cancel/'
        },
        LOCAL_PURCHASE_APPROVAL: {
          DROPDOWN_API: procurementApi + '/local/purchase/requests/approval/dropdowns',
          SEARCH_API: procurementApi + '/local/purchase/requests/approval/search',
          CANCEL_API: procurementApi + '/local/purchase/requests/approval/cancel/',
          DETAILS_API: procurementApi + '/local/purchase/requests/approval/',
          SAVE_API: procurementApi + '/local/purchase/requests/approval/approve/'
        },
        GRN_AGAINST_PO: {
          DROPDOWN_API: procurementApi + '/purchase/grn/against/po/dropdowns',
          SEARCH_API: procurementApi + '/purchase/grn/against/po/search',
          DETAILS_API: procurementApi + '/purchase/grn/against/po/',
          CANCEL_API: procurementApi + '/purchase/grn/against/po/cancel/'
        }
      },
      LOAN_IN_OUT: {
        LOAN_IN: {
          DROPDOWN_API: procurementApi + '/loan/in/dropdown',
          SEARCH_API: procurementApi + '/loan/in/search',
          APPROVE_API: procurementApi + '/loan/in/approve',
          CANCEL_API: procurementApi + '/loan/in/cancel/',
          SAVE_API: procurementApi + '/loan/in/save/',
          DETAILS_API: procurementApi + '/loan/in/'
        },
        LOAN_INOUT: {
          DROPDOWN_API: procurementApi + '/loan/in/out/dropdown',
          SEARCH_API: procurementApi + '/loan/in/out/search',
          SAVE_API: procurementApi + '/loan/in/out/save/',
          DETAILS_API: procurementApi + '/loan/in/out/',
          APPROVE_API: procurementApi + '/loan/in/out/approve',
          CANCEL_API: procurementApi + '/loan/in/out/cancel/',
          SEARCH_LOAN_IN: procurementApi + '/loan/in/out/get/item',
          ADD_LOAN_IN_ITEM: procurementApi + '/loan/in/out/add/item'
        },
        LOAN_OUT: {
          DROPDOWN_API: procurementApi + '/loanInOut/loan/out/dropdown',
          GET_ITEM_SEARCH_API:globalApi + '/items/batches/billableConsumption/loanOut',
          SEARCH_API: procurementApi + '/loanInOut/loan/out/search',
          SAVE_API: procurementApi + '/loanInOut/loan/out/save/',
          DETAILS_API: procurementApi + '/loanInOut/loan/out/',
          APPROVE_API: procurementApi + '/loanInOut/loan/out/approve',
          CANCEL_API: procurementApi + '/loanInOut/loan/out/cancel/',
          //GET_ITEM_SEARCH_API:  globalApi + '/items/batches/billableConsumption/loanOut'
        },
        LOAN_OUT_RECEIVE: {
          DROPDOWN_API: procurementApi + '/loanInOut/loan/out/receive/dropdown',
          SEARCH_API: procurementApi + '/loanInOut/loan/out/receive/search',
          SAVE_API: procurementApi + '/loanInOut/loan/out/receive/save/',
          DETAILS_API: procurementApi + '/loanInOut/loan/out/receive/',
          APPROVE_API: procurementApi + '/loanInOut/loan/out/receive/approve',
          CANCEL_API: procurementApi + '/loanInOut/loan/out/receive/cancel/',
          SEARCH_LOAN_OUT: procurementApi + '/loanInOut/loan/out/receive/get/item',
          ADD_LOAN_OUT_ITEM: procurementApi + '/loanInOut/loan/out/receive/add/item'
        }

      },
      WORK_ORDER: {
        WORK_ORDER_REQUEST: {
          DROPDOWN_API: procurementApi + '/workOrderRequest/dropdowns',
          SEARCH_API: procurementApi + '/workOrderRequest/search',
          DETAILS_API: procurementApi + '/workOrderRequest/',
          APPROVE_API: procurementApi + '/workOrderRequest/approve',
          CANCEL_API: procurementApi + '/workOrderRequest/cancel/'
        },
        WORK_ORDER_APPROVAL: {
          DROPDOWN_API: procurementApi + '/workOrderRequest/approval/dropdowns',
          SEARCH_API: procurementApi + '/workOrderRequest/approval/search',
          DETAILS_API: procurementApi + '/workOrderRequest/approval/',
          APPROVE_API: procurementApi + '/workOrderRequest/approval/approve/',
          CANCEL_API: procurementApi + '/workOrderRequest/approval/cancel/'
        },
        WORK_ORDER_QUOTATION_REQUEST: {
          DROPDOWN_API: procurementApi + '/workOrder/quotationRequest/dropdowns',
          SEARCH_API: procurementApi + '/workOrder/quotationRequest/search',
          GET_ITEM_LIST: globalApi + '/items/workOrder',
          DETAILS_API: procurementApi + '/workOrder/quotationRequest/',
          CANCEL_API: procurementApi + '/workOrder/quotationRequest/cancel/',
          APPROVE_API: procurementApi + '/workOrder/quotationRequest/approve/',
          SEND_FOR_SELECTION: procurementApi + '/workOrder/quotationRequest/approval',
          QO_SEARCH: procurementApi + '/workOrder/quotationRequest/quotationOrder/search',
          ADD_ITEM_QO: procurementApi + '/workOrder/quotationRequest/quotationOrder/details'
        },
        QUOTATION_SENT_TO_VENDOR: {
          DROPDOWN_API: procurementApi + '/workOrder/quotationVendor/dropdowns',
          SEARCH_API: procurementApi + '/workOrder/quotationVendor/search',
          SEARCH_API_NEW: procurementApi + '/workOrder/quotationVendor/search/new',
          DETAILS_API: procurementApi + '/workOrder/quotationVendor/',
          DETAILS_API_NEW: procurementApi + '/workOrder/quotationVendor/save/new',
          CANCEL_API: procurementApi + '/workOrder/quotationVendor/cancel/',
          APPROVE_API: procurementApi + '/workOrder/quotationVendor/approve/',
          QR_SEARCH: procurementApi + '/workOrder/quotationVendor/quotationRequest/search',
          ADD_ITEM_QR: procurementApi + '/workOrder/quotationVendor/quotationRequest/details'
        },
        QUOTATION_ENTRY: {
          DROPDOWN_API: procurementApi + '/workOrder/quotationEntry/dropdowns',
          SEARCH_API: procurementApi + '/workOrder/quotationEntry/search',
          DETAILS_API: procurementApi + '/workOrder/quotationEntry/',
          CANCEL_API: procurementApi + '/workOrder/quotationEntry/cancel/',
          APPROVE_API: procurementApi + '/workOrder/quotationEntry/approve/',
          QV_SEARCH: procurementApi + '/workOrder/quotationEntry/quotationVendor/search',
          ADD_ITEM_QV: procurementApi + '/workOrder/quotationEntry/quotationVendor/details'
        },
        QUOTATION_NEGOTIATION: {
          DROPDOWN_API: procurementApi + '/workOrder/quotationNegotiation/dropdowns',
          SEARCH_API: procurementApi + '/workOrder/quotationNegotiation/search',
          DETAILS_API: procurementApi + '/workOrder/quotationNegotiation/'
        },
        QUOTATION_SELECTION: {
          DROPDOWN_API: procurementApi + '/workOrder/quotationSelection/dropdowns',
          SEARCH_API: procurementApi + '/workOrder/quotationSelection/search',
          DETAILS_API: procurementApi + '/workOrder/quotationSelection/',
          SAVE_API: procurementApi + '/workOrder/quotationSelection/'
        },
        WORK_ORDER_CREATION: {
          DROPDOWN_API: procurementApi + '/WOCreation/dropdowns',
          SEARCH_API: procurementApi + '/WOCreation/search',
          SEARCH_BY_QR:procurementApi + '/WOCreation/searchByQR',
          SEARCH_BY_WR:procurementApi + '/WOCreation/searchByWR',
          GET_ITEM_DETAIL:globalApi + '/items/workOrder/details/batches',
          GET_ITEM_DETAIL_FOR_QR:procurementApi + '/WOCreation/searchByQRById',
          GET_ITEM_DETAIL_FOR_WR:procurementApi + '/WOCreation/searchByWRById',
          DETAILS_API: procurementApi + '/WOCreation/',
          SAVE_API:procurementApi + '/WOCreation/save',
          UPDATE_API:procurementApi + '/WOCreation/update/',
          CANCEL_API: procurementApi + '/WOCreation/cancel/',
          APPROVE_API: procurementApi + '/WOCreation/approve',
         
        },
      },
      CONSIGNMENT: {
        CONSIGNMENT_INWARD: {
          DROPDOWN_API: procurementApi + '/consignments/consignment/inword/dropdown',
          SEARCH_API: procurementApi + '/consignments/consignment/inword/search',
          SAVE_API: procurementApi + '/consignments/consignment/inword/save',
          DETAIL_API: procurementApi + '/consignments/consignment/inword/',
          UPDATE_API: procurementApi + '/consignments/consignment/inword/',
          APPROVE_API: procurementApi + '/consignments/consignment/inword/approve',
          CANCEL_API: procurementApi + '/consignments/consignment/inword/cancel/'
        },
        CONSIGNMENT_OUTWARD: {
          DROPDOWN_API: procurementApi + '/consignment/consignment/outward/dropdown',
          SEARCH_API: procurementApi + '/consignment/consignment/outward/search',
          SAVE_API: procurementApi + '/consignment/consignment/outward/save',
          DETAILS_API: procurementApi + '/consignment/consignment/outward/',
          UPDATE_API: procurementApi + '/consignment/consignment/outward/',
          APPROVE_API: procurementApi + '/consignment/consignment/outward/approve',
          CANCEL_API: procurementApi + '/consignment/consignment/outward/cancel/'
        },
        CONSIGNMENT_PO_CREATION: {
          DROPDOWN_API: procurementApi + '/consignments/POCreation/dropdowns',
          SEARCH_API: procurementApi + '/consignments/POCreation/search',
          DETAILS_API: procurementApi + '/consignments/POCreation/',
          GET_ITEM: procurementApi + '/consignments/POCreation/issue/search',
          SAVE_API: procurementApi + '/consignments/POCreation/save',
          UPDATE_API: procurementApi + '/consignments/POCreation/update/',
          APPROVE_PO_API: procurementApi + '/consignments/POCreation/approve',
          CANCEL_PO_API: procurementApi + '/consignments/POCreation/cancel/',
        },
        CONSIGNMENT_PO_APPROVAL: {
          DROPDOWN_API: procurementApi + '/consignments/POCreation/dropdowns',
          SEARCH_API: procurementApi + '/consignments/POCreation/approval/search',
          DETAILS_API: procurementApi + '/consignments/POCreation/approval/',
          UPDATE_API: procurementApi + '/consignments/POCreation/approval/approve/',
        }
      },

      IMPLANT_INDENT: {
        SEARCH_ITEM: inventoryApi + '/stock/opening/implant/balances/consignment/list',
        DROPDOWN_API: procurementApi + '/consignments/implant/indent/dropdowns',
        SEARCH_API: procurementApi + '/consignments/implant/indent/search',
        SAVE_API: procurementApi + '/consignments/implant/indent/save',
        DETAILS_API: procurementApi + '/consignments/implant/indent/',
        UPDATE_API: procurementApi + '/consignments/implant/indent/',
        APPROVE_API: procurementApi + '/consignments/implant/indent/approve',
        CANCEL_API: procurementApi + '/consignments/implant/indent/cancel/'
      },
      IMPLANT_INDENT_ACCEPATNCE: {
    	DROPDOWN_API: procurementApi + '/consignments/implant/indent/acceptance/dropdowns',
        SEARCH_API: procurementApi + '/consignments/implant/indent/acceptance/search',
        DETAILS_API: procurementApi + '/consignments/implant/indent/acceptance/',
        UPDATE_API: procurementApi + '/consignments/implant/indent/acceptance/',
      },
      IMPLANT_INDENT_RETURN: {
        CONSIGNMENT_SEARCH_API: procurementApi + '/consignments/implantreturn/consignment/search',
        IMPLANT_SEARCH_API: procurementApi + '/consignments/implantreturn/implant/search',
        DROPDOWN_API: procurementApi + '/consignments/implantreturn/dropdown',
        SAVE_API: procurementApi + '/consignments/implantreturn/save',
        DETAILS_API: procurementApi + '/consignments/implantreturn/',
        AGAINST_ITEMS_SEARCH_API: procurementApi + '/consignments/implantreturn/ConsAndImplagainstItem/search',
        AGAINST_ISSUE_SEARCH_API: procurementApi + '/consignments/implantreturn/ConsAndImplagainstIssue/search',
        ADD_NEW_API: procurementApi + '/consignments/implantreturn/AgainstIssueAndAgainstItem/new',
      },
      IMPLANT_INDENT_RETURN_ACCEPTANCE: {
        CONSIGNMENT_SEARCH_API: procurementApi + '/consignments/ir/acceptance/consignment/search',
        IMPLANT_SEARCH_API: procurementApi + '/consignments/ir/acceptance/implant/search',
        DROPDOWN_API: procurementApi + '/consignments/ir/acceptance/dropdowns',
        SAVE_API: procurementApi + '/consignments/ir/acceptance/save',
        DETAILS_API: procurementApi + '/consignments/ir/acceptance/',
      },
      PATIENT_CONSIGNMENT_ISSUE: {
    	DROPDOWN_API: procurementApi + '/consignments/patient/consumption/dropdowns',
        PENDING_SEARCH_API: procurementApi + '/consignments/patient/consumption/search',
        ISSUE_SEARCH_API: procurementApi + '/consignments/patient/consumption/issue/search',
        PENDING_DETAILS_API: procurementApi + '/consignments/patient/consumption/',
        ISSUE_DETAILS_API: procurementApi + '/consignments/patient/consumption/list/',
        SAVE_API: procurementApi + '/consignments/patient/consumption/update/',
      }
    },

    ASSET: {
      FIXED_ASSET: {
        ADD_FIXED_ASSET: {
          SEARCH_API: assetApi + '/fixedAsset/addFixedAsset/search',
          DROPDOWN_API: assetApi + '/fixedAsset/addFixedAsset/dropdowns',
          DETAIL_API: assetApi + '/fixedAsset/addFixedAsset/',
          SAVE_API: assetApi + '/fixedAsset/addFixedAsset/save',
          CANCEL_API: assetApi + '/fixedAsset/addFixedAsset/cancel/',
          ITEM_APPROVE_API: assetApi + '/fixedAsset/addFixedAsset/approve/',
          APPROVE_API: assetApi + '/fixedAsset/addFixedAsset/approve',
          UPDATE_API: assetApi + '/fixedAsset/addFixedAsset/'
        },
      },
      AUCTION_REQUISITION: {
        DROPDOWN_API: assetApi + '/management/auction/requisition/dropdowns',
        SEARCH_API: assetApi + '/management/auction/requisition/search',
        DETAILS_API: assetApi + '/management/auction/requisition/',
        SAVE_API: assetApi + '/management/auction/requisition/save',
        APPROVE_API: assetApi + '/management/auction/requisition/approve',
        CANCEL_API: assetApi + '/management/auction/requisition/cancel/',
      },
      AUCTION_REQUISITION_APPROVAL: {
    	DROPDOWN_API: assetApi + '/management/auction/requisition/approval/dropdowns',
        DETAILS_API: assetApi + '/management/auction/requisition/approval/',
        SAVE_API: assetApi + '/management/auction/requisition/approval/save',
        APPROVE_API: assetApi + '/management/auction/requisition/approval/approve',
        SEARCH_API: assetApi + '/management/auction/requisition/approval/search'
      },
      ITEMS_FOR_AUCTION: {
    	DROPDOWN_API: assetApi + '/item/for/auction/dropdowns',
        SEARCH_API: assetApi + '/item/for/auction/search',
        DETAILS_API: assetApi + '/item/for/auction/',
        SAVE_API: assetApi + '/item/for/auction/save',
        APPROVE_API: assetApi + '/item/for/auction/approve'
      },
      AUCTION_VENDOR_APPROVAL: {
    	DROPDOWN_API: assetApi + '/auction/vendor/approval/dropdowns',
        SEARCH_API: assetApi + '/auction/vendor/approval/search',
        DETAILS_API: assetApi + '/auction/vendor/approval/',
        SAVE_API: assetApi + '/auction/vendor/approval/save/'
      },
      NON_RETURNABLE_GATEPASS: {
        SEARCH_API: assetApi + '/non/returnable/gatepass/search',
        DETAILS_API: assetApi + '/non/returnable/gatepass/',
        SAVE_API: assetApi + '/non/returnable/gatepass/save',
        GET_REQUEST_API: assetApi + '/non/returnable/gatepass/get/request',
        UPDATE_API: assetApi + '/non/returnable/gatepass/update',
        APPROVE_API: assetApi + '/non/returnable/gatepass/approve',
        CANCEL_API: assetApi + '/non/returnable/gatepass/cancel/'
      },
      ASSET_INDENT: {
        SEARCH_API: assetApi + '/indent/assetIndent/search',
        DROPDOWN_API: assetApi + '/indent/assetIndent/dropdowns',
        DETAIL_API: assetApi + '/indent/assetIndent/',
        SAVE_API: assetApi + '/indent/assetIndent/save',
        UPDATE_API: assetApi + '/indent/assetIndent/update/',
        APPROVE_API: assetApi + '/indent/assetIndent/approve',
        CANCEL_API: assetApi + '/indent/assetIndent/cancel/'
      },
      ASSET_INDENT_APPROVAL: {
        SEARCH_API: assetApi + '/indent/assetIndent/approval/search',
        DROPDOWN_API: assetApi + '/indent/assetIndent/approval/dropdowns',
        DETAIL_API: assetApi + '/indent/assetIndent/approval/',
        SAVE_API: assetApi + '/indent/assetIndent/approval/save/',
        CANCEL_API: assetApi + '/indent/assetIndent/approval/cancel',
      },
      ASEET_ISSUE: {
        SEARCH_API: assetApi + '/issue/pending/search',
        DROPDOWN_API: assetApi + '/issue/pending/dropdowns',
        DETAIL_API: assetApi + '/issue/pending/',
        SAVE_API: assetApi + '/issue/pending',
        ISSUELIST_SEARCH_API: assetApi + '/issue/list/search',
        ISSUELIST_DROPDOWN_API: assetApi + '/issue/list/dropdowns',
        ISSUELIST_DETAIL_API: assetApi + '/issue/list/',
        ISSUELIST_SAVE_API: assetApi + '/issue/list',
        DirectIssue_Details_Api : assetApi + '/issue/list/batches/details',
        DirectIssue_Save_Api : assetApi + '/issue/list'
      },
      ASEET_ISSUE_ACCEPTANCE: {
        SEARCH_API: assetApi + '/issue/acceptance/search',
        DROPDOWN_API: assetApi + '/issue/acceptance/dropdowns',
        DETAIL_API: assetApi + '/issue/acceptance/',
        SAVE_API: assetApi + '/issue/acceptance/',
      },
      ASSET_RETURN: {
    	DROPDOWN_API: assetApi + '/management/asset/return/dropdowns',
        SEARCH_API: assetApi + '/management/asset/return/search',
        APPROVE_API: assetApi + '/management/asset/return/approve',
        DETAIL_API: assetApi + '/management/asset/return/',
        CANCEL_API: assetApi + '/management/asset/return/cancel/',
        UPDATE_API: assetApi + '/management/asset/return/',
        SAVE_API: assetApi + '/management/asset/return',
        ISSUELIST_API: assetApi + '/management/asset/return/againstIssue/search'
      },
      ASSET_RETURN_ACCEPTANCE: {
    	DROPDOWN_API: assetApi + '/management/asset/return/acceptance/dropdowns',
        SEARCH_API: assetApi + '/management/asset/return/acceptance/search',
        SAVE_API: assetApi + '/management/asset/return/acceptance/',
        DETAIL_API: assetApi + '/management/asset/return/acceptance/',
        UPDATE_API: assetApi + '/management/asset/return/acceptance/'
      },
      REJECTED_ASSET_ACCEPTANCE: {
    	DROPDOWN_API: assetApi + '/indent/assetIssue/reject/asset/acceptance/dropdowns',
        SEARCH_API: assetApi + '/indent/assetIssue/reject/asset/acceptance/search',
        SAVE_API: assetApi + '/indent/assetIssue/reject/asset/acceptance/',
        DETAIL_API: assetApi + '/indent/assetIssue/reject/asset/acceptance/',
        UPDATE_API: assetApi + '/indent/assetIssue/reject/asset/acceptance/'
      }
    },

    LINEN_LAUNDRY: {
      LINEN_INDENT: {
        SEARCH_API: linenApi + '/linen/indent/search',
        DROPDOWN_API: linenApi + '/linen/indent/dropdowns',
        DETAIL_API: linenApi + '/linen/indent/',
        SAVE_API: linenApi + '/linen/indent',
        UPDATE_API: linenApi + '/linen/indent/',
        CANCEL_API: linenApi + '/linen/indent/cancel/',
        APPROVE_API: linenApi + '/linen/indent/approve'
      },
      LINEN_INDENT_APPROVAL: {
        SEARCH_API: linenApi + '/linen/indent/approval/search',
        DROPDOWN_API: linenApi + '/linen/indent/approval/dropdowns',
        DETAIL_APPROVE_API: linenApi + '/linen/indent/approval/approve/',
        DETAIL_API: linenApi + '/linen/indent/approval/',
        CANCEL_API: linenApi + '/linen/indent/approval/cancel/'
      },
      LINEN_ISSUE: {
        LINEN_PENDING_ISSUE: {
          SEARCH_API: linenApi + '/linen/issue/search',
          DROPDOWN_API: linenApi + '/linen/issue/dropdowns',
          DETAIL_API: linenApi + '/linen/issue/',
          SAVE_API: linenApi + '/linen/issue'
        },
        LINEN_ISSUE_LIST: {
          SEARCH_API: linenApi + '/linen/issue/list/search',
          DROPDOWN_API: linenApi + '/linen/issue/list/dropdowns',
          DETAIL_API: linenApi + '/linen/issue/list/',
          SAVE_API: linenApi + '/linen/issue/list/direct/issue'
        }
      },
      LINEN_ISSUE_ACCEPTANCE: {
        SEARCH_API: linenApi + '/linen/issue/acceptance/search',
        DROPDOWN_API: linenApi + '/linen/issue/acceptance/dropdowns',
        DETAIL_API: linenApi + '/linen/issue/acceptance/',
        SAVE_API: linenApi + '/linen/issue/acceptance/'
      }
    },

    OPERATION_THEATRE: {
      SEARCH_API: pharmacyApi + '/theatre/management/',
      SAVE_API: pharmacyApi + '/theatre/management',
    }
  };
}());
