(function () {
  "use strict";

  function detailsGRNController($rootScope, $scope, $http, CONSTANTS, $state, GenericService,BillingGenericService, commonDetailService,$cookies,growl, CurrencyService) {
    var self = this;
    var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
    //Default Constructor
    function initializeController() {
      $rootScope.loginpage = true;
      
      

      self.model = {
        otherCharges: [],
        GRNUom: [],
        bonusUoms: [],
        grnStagedQuantity: [],
        poList: [],
        getAccessoryList: [],
        bonusBatchExpiryData:[]
      };

      self.model.NavigateToBack = NavigateToBack;
      self.model.ViewOtherCharges = ViewOtherCharges;
      self.model.ViewGRNUom = ViewGRNUom;
      self.model.ViewBonusUom = ViewBonusUom;
      self.model.ViewStagedQuantity = ViewStagedQuantity;
      self.model.ViewPOList = ViewPOList;
      self.model.ViewItems = ViewItems;
      self.model.OpenPopup = OpenPopup;
      self.model.ViewTerms = ViewTerms;
      self.model.ViewHistory = ViewHistory;
      self.model.ViewLastGrnDetails = ViewLastGrnDetails;
      self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;

      GetGRN(commonDetailService.getDataId());
    }

    CurrencyService.GetCurrency().then(function(currency){
      self.model.Currency = currency;
    });

    var GetGRN = function (grnId) {
      commonDetailService.setDataId(null);
      $scope.grnIdForDoccumentUpload = grnId;

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GET_GRN_API  + "/" + $scope.unitId + "/" + $scope.organizationId +"/"+ grnId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.grnList = response.data;
                    $scope.totalCreditAmountForCal = self.model.grnList.totalCreditNoteAmount;
          			angular.forEach(self.model.grnList.creditNoteAmountList, function( value,key){
                   // self.model.grnList.creditNoteAmountList[key].availablenetamount = parseFloat($scope.totalCreditAmountForCal) - parseFloat(self.model.grnList.creditNoteAmountList[key].consumenetamount);
                 	$scope.totalCreditAmountForCal = self.model.grnList.creditNoteAmountList[key].availablenetamount;
                  		   
          });
          
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.grnType = response.data.grnTypeId;
          if(self.model.grnList.grnOtherChargesMapper==undefined || self.model.grnList.grnOtherChargesMapper==null || self.model.grnList.grnOtherChargesMapper.length==0)
        	  {
        	  self.model.grnList.totalOtherAmount=0;
        	  }
          self.model.grnList.items.forEach(function(item){
        	  item.grnStagedQuantity.forEach(function(poStaged){
					poStaged.originalPOApprovedQty=poStaged.poApprovedQuantity
			  });
        	  item.poPendingQuantity=item.poPendingQuantity-item.grnApprovedQuantity;
            var poTotal = 0;
            item.grnBonusBatchExpiryData=item.grnBonusBatchExpiry,
            angular.forEach(item.poList, function(grn){
              poTotal += grn.approvedQuantity;
            });
            item.grnApprovedQuantity = poTotal;
            
            var uomList = null;
            if (self.model.assetTypeId == 1) {
              uomList = item.uom.find(function(uom){
                return item.grnUomTypeId == uom.uomTypeId;
              });
              if(item.isThreeUOMApplicable == false){
                item.uomListdata = item.uom;
                item.uom = [];
                angular.forEach(item.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                  }
                });
              }
            }
            $scope.updateEditFlag(item);
            CalculateStock(item, item.uom, uomList);
          });

          $rootScope.stopLoader();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }
    $scope.updateEditFlag = function (item) {
    	$scope.editFlag=false;
    	if(item.newPOQty==undefined || item.newPOQty==null)
    		{
    		item.newPOQty=item.grnReceivedQuantity;
    		}
    	if(item.costOfPurchase==undefined || item.costOfPurchase==null)
		{
		item.costOfPurchase=item.cop;
		}
    	if(item.discountAmount==undefined || item.discountAmount==null)
		{
		item.discountAmount=item.totalDiscount;
		}
    	var tax=item.taxAmount/item.newPOQty;
    	var dis=item.discountAmount/item.newPOQty;
    	var mrp=(item.mrp!=undefined && item.mrp!=null ? item.mrp :0)-(tax!=undefined && tax!=null ? tax:0);
    	var cop=(item.costOfPurchase!=undefined && item.costOfPurchase!=null ? item.costOfPurchase:0)-(dis!=undefined && dis!=null ? dis :0);
    	var sub=mrp-cop;
    	item.profit=(sub*100)/cop;
    	if(isNaN(item.profit) || item.profit==undefined || item.profit==null)
    		{
    		item.profit=0.00;
    		}
    	item.profit=item.profit.toFixed(2);
    }
    var ViewOtherCharges = function (item){
      self.model.otherCharges = item.grnOtherChargesMapper;
       $scope.otherData=item.grnOtherChargesMapper;
    }

    var ViewGRNUom = function (item) {
      self.model.GRNUom = [];
      self.model.grnList.items.find(function(x){
        if (item.id == x.id) {
          if(x.isThreeUOMApplicable == false){
            x.grnUom.find(function(d){
              if(d.uomTypeId != 2){
                self.model.GRNUom.push(d);      
              }
            })
          }
          else{
            self.model.GRNUom = x.grnUom;
          }
        }
      });
    }

    var ViewBonusUom = function (item) {
      self.model.bonusUoms = [];
      self.model.grnList.items.find(function(x){
        if (item.id == x.id) {
          if(x.isThreeUOMApplicable == false){
            x.bonusUom.find(function(d){
              if(d.uomTypeId != 2){
                self.model.bonusUoms.push(d);      
              }
            })
          }
          else{
            self.model.bonusUoms = x.bonusUom;
          }
        }
      });
    }

    var ViewStagedQuantity = function (item) {
    	
    if(item.grnUomTypeId == item.poGrnUomTypeId){
        self.model.tototalPOQTY=item.poPendingQuantity;
        self.model.totalRecQtyAgnGrn = item.grnReceivedQuantity;
        self.model.totalPendingQty = item.poPendingQuantity-item.grnReceivedQuantity;
        self.model.grnList.items.find(function(x){
          if (item.id == x.id) {
            self.model.grnStagedQuantity = x.grnStagedQuantity;
            self.model.grnStagedList=x.grnStagedLists;
          }
        });
    }else{
    	
    	 var quantityCalculated = 0;
    	 var poQuantity = 0;
    	if(item.isThreeUOMApplicable == true){
            if(item.poGrnUomTypeId==1)
            	{
            	poQuantity = parseInt(item.poPendingQuantity * (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
            	}
            else if(item.poGrnUomTypeId==2)
            	{
            	poQuantity =parseInt(item.poPendingQuantity * (item.uom[1].conversion * item.uom[2].conversion));
            	}
            else
            	{
            	poQuantity =parseInt(item.poPendingQuantity * (item.uom[2].conversion));
            	}
            	}
            else
            	{
            	 if(item.poGrnUomTypeId==1)
             	{
             	poQuantity = parseInt(item.poPendingQuantity * (item.uom[0].conversion *  item.uom[2].conversion));
             	}
             else
             	{
             	poQuantity =parseInt(item.poPendingQuantity * (item.uom[2].conversion));
             	}
            	}
    	if(item.isThreeUOMApplicable == true){
            if(item.grnUomTypeId==1)
            	{
            	poQuantity = parseInt(poQuantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
            	}
            else if(item.grnUomTypeId==2)
            	{
            	poQuantity =parseInt(poQuantity / (item.uom[1].conversion * item.uom[2].conversion));
            	}
            else
            	{
            	poQuantity =parseInt(poQuantity / (item.uom[2].conversion));
            	}
            	}
            else
            	{
            	 if(item.grnUomTypeId==1)
             	{
             	poQuantity = parseInt(poQuantity / (item.uom[0].conversion *  item.uom[2].conversion));
             	}
             else
             	{
             	poQuantity =parseInt(poQuantity / (item.uom[2].conversion));
             	}
            	}
		 var quantityCalculated = poQuantity ;
		 
		    self.model.tototalPOQTY= quantityCalculated;
		    
	        self.model.totalRecQtyAgnGrn = item.grnReceivedQuantity;
	        
	        self.model.totalPendingQty = quantityCalculated -item.grnReceivedQuantity;
	        

	        item.grnStagedQuantity.find(function(x){
	        var quantityCalculatedx = 0;
       	 var poQuantityx = 0;
       	
       	if(item.isThreeUOMApplicable == true){
               if(item.poGrnUomTypeId==1)
               	{
               	poQuantityx = parseInt(x.originalPOApprovedQty * (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
               	}
               else if(item.poGrnUomTypeId==2)
               	{
               	poQuantityx =parseInt(x.originalPOApprovedQty  * (item.uom[1].conversion * item.uom[2].conversion));
               	}
               else
               	{
               	poQuantityx =parseInt(x.originalPOApprovedQty  * (item.uom[2].conversion));
               	}
               	}
               else
               	{
               	 if(item.poGrnUomTypeId==1)
                	{
               		 poQuantityx = parseInt(x.originalPOApprovedQty  * (item.uom[0].conversion *  item.uom[2].conversion));
                	}
                else
                	{
               	 poQuantityx =parseInt(x.originalPOApprovedQty  * (item.uom[2].conversion));
                	}
               	}
       	
       	
       	if(item.isThreeUOMApplicable == true){
               if(item.grnUomTypeId==1)
               	{
               	poQuantityx = parseInt(poQuantityx / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
               	}
               else if(item.grnUomTypeId==2)
               	{
               	poQuantityx =parseInt(poQuantityx / (item.uom[1].conversion * item.uom[2].conversion));
               	}
               else
               	{
               	poQuantityx =parseInt(poQuantityx / (item.uom[2].conversion));
               	}
               	}
               else
               	{
               	 if(item.grnUomTypeId==1)
                	{
               		 poQuantityx = parseInt(poQuantityx / (item.uom[0].conversion *  item.uom[2].conversion));
                	}
                else
                	{
               	 poQuantityx =parseInt(poQuantityx / (item.uom[2].conversion));
                	}
               	}
   		 var quantityCalculatedx = poQuantityx ;
   		 x.poApprovedQuantity = quantityCalculatedx;       
    });
	        
	        self.model.grnList.items.find(function(x){
		          if (item.id == x.id) {
		         	
		    		    
		            self.model.grnStagedQuantity = x.grnStagedQuantity;
		            self.model.grnStagedList=x.grnStagedLists;
		          }
		        });
    }

    }

    var ViewItems = function (item) {
      self.model.grnList.items.find(function(x){
        if (item.id == x.id) {
          self.model.getAccessoryList = x.grnAccessoriesMapper;
        }
      });
    }

    var ViewPOList = function (item) {
      self.model.grnList.items.find(function(x){
        if (item.id == x.id) {
          self.model.poList = x.poList;
        }
      });
    }

    var NavigateToBack = function (){
      $state.go('GRN');
    }

    var ViewTerms = function (viewModal, item) {
      switch(viewModal){
        case "warrantyPeriodModal":
          self.model.grnList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.warrantyPeriod = key.warrentyPeriod;
            }
          });
        break;
        case "expectedSuportTermsModal":
          self.model.grnList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.supportTerms = key.supportTerms;
            }
          });
        break;
        case "expectedPaymentTermsModal":
          self.model.grnList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.paymentTerms = key.expectedPayment;
            }
          });
        break;
        case "expectedTechSpecificationModal":
          self.model.grnList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.technicalSpecs = key.technicalSpecification;
            }
          });
        break;
      }
    }

    var ViewHistory = function(item){
      var URI = CONSTANTS.GLOBAL.PO_HISTORY + $scope.unitId + '/' + item.itemId+ '/' + item.poId;

      $rootScope.startLoaderPopup();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastOrderDetails = response.data;
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
        });
    }

    var ViewLastGrnDetails = function (item) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.PRE_GRN_HISTORY + item.vendorId + "/" +item.items[0].itemId;
     // var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GRN_HISTORY + item.vendorId + "/" + item.items[0].itemId+"/"+item.id;
      $rootScope.startLoaderPopup();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastGRNDetailsList = response.data;
          angular.forEach(self.model.lastGRNDetailsList,function(value, key) {
        	  if(value.expirydate != undefined && value.expirydate != null){
        		  if(value.expirydate.toString().includes("-"))
        		  {
        			  value.stringDate=true;
        		  }
        	  }
          });
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
        });
    }
    
	$scope.getPatientDocumentList = function()
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
       "grnId":$scope.grnIdForDoccumentUpload,
      }
     var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
     BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
       $scope.patientDocumentList = [];
       if (response.data.status == "success") 
       {
       	$scope.patientDocumentList = response.data.listObject; 
       	$('#uploadDetails').modal('show');
       	angular.forEach($scope.patientDocumentList,function(value, key) {
       		if(value.bucketFlag == 1){
       		var filepath = value.documentPath
       		var arr = filepath.split("/");
       		var length=arr.length;
       		var fileName1=arr[length-1];
       		var arr1 = fileName1.split("_");
       		var length1=arr1.length;
       		var fileName=arr1[length1-1];
       		value.fileName=fileName;
       		}else{      			
       			value.fileName=value.fileName;
       		}
       	});
       		
       	
          
           $rootScope.stopLoader();
       }
       else if(response.data.status == "error")
        {
       	$rootScope.stopLoader();
        }
      });
    }
	var bonusBatchExpiryInfoOnClick = function (batchItem, index) {
        self.model.bonusSelectedItemBatchExpiryIndex = index;
        var sumOfBonusReceivedQuantity=0;
        if(batchItem.grnBonusBatchExpiryData!=undefined && batchItem.grnBonusBatchExpiryData!=null && batchItem.grnBonusBatchExpiryData.length>0)
        	{
        angular.forEach(batchItem.grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
        	sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
        	self.model.bonusBatchExpiryData.push({
        		grnBonusBatchExpiryId:grnBonusBatchExpiryCount.grnBonusBatchExpiryId,
        		batchNo: grnBonusBatchExpiryCount.batchNo,
        		expiryDate: grnBonusBatchExpiryCount.expiryDate,
                bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity
            });
          });
        batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
            }
        
    }
	$scope.closeBonusBatchExpiry =function()
    {
    	self.model.bonusBatchExpiryData=[];
    }
    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList) {
        var prQuantity = item.prQuantity;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        uomList.uomDetails = uomList.uomDetails.split(' ')[0];
        if(item.grnUomType=="Purchase")
        {
        	item["uomDetails"]="P - "+item.grnUomUnit;
        }
        if(item.grnUomType=="Stock")
    	  {
    	    item["uomDetails"]="S - "+item.grnUomUnit;
    	  }
        if(item.grnUomType=="Dispense")
    	  {
    	    item["uomDetails"]="D - "+item.grnUomUnit;
    	  }
        if(item.bonusUomType=="Purchase")
    	  {
    	    item["bonusUomDetails"]="P - "+item.bonusUomUnit;
    	  }
	      if(item.bonusUomType=="Stock")
		    {
		      item["bonusUomDetails"]="S - "+item.bonusUomUnit;
		    }
	      if(item.bonusUomType=="Dispense")
		    {
		      item["bonusUomDetails"]="D - "+item.bonusUomUnit;
		    }

        if(item.bonusReceivedQuantity == undefined || item.bonusReceivedQuantity == null || item.bonusReceivedQuantity == ''){
          item.bonusReceivedQuantity = 0;
        }
        if(item.isThreeUOMApplicable == true){
            if(item.poGrnUomTypeId==1)
            	{
            	item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity * (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
            	}
            else if(item.poGrnUomTypeId==2)
            	{
            	item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity * ( item.uom[1].conversion * item.uom[2].conversion));
            	}
            else
            	{
            	item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity * ( item.uom[2].conversion));
            	}
            	}
            else
            	{
            	 if(item.poGrnUomTypeId==1)
             	{
            		 item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity * (item.uom[0].conversion * item.uom[2].conversion));
             	}
             else
             	{
            	 item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity * ( item.uom[2].conversion));
             	}
            	}
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetails == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.grnUom[0].conversion);
            item.receivedQtyCalculation = (item.grnReceivedQuantity * item.grnUom[0].conversion);
            item.uomUnitData = item.uomListdata[2].uomUnit;
            item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetails == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetails == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity);
            item.receivedQtyCalculation = (item.grnReceivedQuantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
            item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity / ( item.uomListdata[2].conversion));
          }
        }
        else{
          if (uomList.uomDetails == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity +"*"+ item.grnUom[0].conversion +"*"+ item.grnUom[1].conversion);
            item.receivedQtyCalculation=(item.grnReceivedQuantity * item.grnUom[0].conversion * item.grnUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
            item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
          } else if (uomList.uomDetails == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity +"*"+ item.grnUom[1].conversion);
            item.receivedQtyCalculation=(item.grnReceivedQuantity * item.grnUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
            item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity / (item.uom[1].conversion * item.uom[2].conversion));
          } else if (uomList.uomDetails == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.receivedQtyCon=(item.grnReceivedQuantity);
            item.receivedQtyCalculation = (item.grnReceivedQuantity);
            item.uomUnitData = uom[2].uomUnit;
            item.grnApprovedQuantity = parseInt(item.grnApprovedQuantity / (item.uom[2].conversion));
          }
        }
        if(item.bonusUomTypeId != undefined && item.bonusUomTypeId != null){
        	if(item.bonusUom == undefined || item.bonusUom == null || item.bonusUom.length == 0){
          	  item.bonusUom = item.uom;
            }
          if(item.isThreeUOMApplicable == false){
            if (item.bonusUomTypeId == 1) {
              item.bonusQtyCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion);
              item.bonusQtyCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion);
              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
            } else if (item.bonusUomTypeId == 2) {
             
            } else if (item.bonusUomTypeId == 3) {
              item.bonusQtyCon=(item.bonusReceivedQuantity);
              item.bonusQtyCalculation = (item.bonusReceivedQuantity);
              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
            }
          }
          else{
            if (item.bonusUomTypeId == 1) {
              item.bonusQtyCon=(item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion +"*"+ item.bonusUom[1].conversion);
              item.bonusQtyCalculation=(item.bonusReceivedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnit;
            } else if (item.bonusUomTypeId == 2) {
              item.bonusQtyCon=(item.bonusReceivedQuantity +"*"+ item.bonusUom[1].conversion);
              item.bonusQtyCalculation=(item.bonusReceivedQuantity * item.bonusUom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnit;
            } else if (item.bonusUomTypeId == 3) {
              item.bonusQtyCon=(item.bonusReceivedQuantity);
              item.bonusQtyCalculation = (item.bonusReceivedQuantity);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
          }
        }
      } else {
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }


    var OpenPopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("GRNdetails", {
      url: "/GRNdetails",
      templateUrl: 'views/procurement/quotation/GRN-details.html',
      controller: "DetailsGRN.Controller",
      controllerAs: "vm",
      title: 'Good Receipt Note'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsGRN.Controller", detailsGRNController);
})();