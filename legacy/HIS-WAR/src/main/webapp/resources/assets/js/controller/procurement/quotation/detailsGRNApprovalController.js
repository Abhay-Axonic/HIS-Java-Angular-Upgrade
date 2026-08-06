(function () {
  "use strict";

  function detailsGRNApprovalController($scope, $rootScope,$timeout, $location, rejectReasonService, $http, StatusService, CONSTANTS, GenericService, $state,$window, growl, commonDetailService,BillingGenericService, GenericServiceFileUpload,CurrencyService,stopTransactionService,$filter,$cookies) {
    var self = this;
    var itemId;
    var cookieObject = $cookies.getObject('cookieObject');
    function initializeController() {
      $rootScope.loginpage = true;
      var cookieObject = $cookies
		.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
      self.model = {
        SelectedItems: [],
        GRNUoms: [],
        bonusUoms: [],
        otherCharges: [],
        selectAllRows: false,
        otherChargesDropdownList: [],
        otherChargesList: [],
        selectedOtherChargeIndex: null,
        poList: [],
        bonusBatchExpiryData:[],
        bonusSelectedItemBatchExpiryIndex:0,
        defaultOtherCharge: [{
          "otherCharges": "Select Other Charge"
        }],
        grnList: {
          items: []
        }
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      self.model.NavigateToBack = NavigateToBack;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.SaveRejection = SaveRejection;
      self.model.SaveApproval = SaveApproval;
      self.model.ViewGRNUom = ViewGRNUom;
      self.model.ViewBonusUom = ViewBonusUom;
      self.model.RejectionPopup = RejectionPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SaveGRNApproval = SaveGRNApproval;
      self.model.isDisable = isDisable;
      self.model.ViewStagedQuantity = ViewStagedQuantity;
      self.model.ViewOtherCharges = ViewOtherCharges;
      self.model.ViewPOList = ViewPOList;
      self.model.ViewTerms = ViewTerms;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ValidateSaveButton = ValidateSaveButton;
      self.model.ViewItems = ViewItems;
      self.model.ViewApprovedStagedQuantity = ViewApprovedStagedQuantity;
      self.model.SumApprovedQty = SumApprovedQty;
      self.model.ViewHistory = ViewHistory;
      self.model.ModalDismissOnClose = ModalDismissOnClose;
      self.model.ViewLastGrnDetails = ViewLastGrnDetails;
      self.model.SetDecimalOtherCharge = SetDecimalOtherCharge;
      self.model.AddOtherCharge = AddOtherCharge;
      self.model.lessOtherCharge = lessOtherCharge;
      self.model.sumOtherCharges = sumOtherCharges;
      self.model.fillOtherChargesList = fillOtherChargesList;
      self.model.discountCalc = discountCalc;
      self.model.AddItemDiscount = AddItemDiscount;
      self.model.SelectTax = SelectTax;
      self.model.SetDecimal = SetDecimal;
      self.model.calculateOtherChargesAmountForItem = calculateOtherChargesAmountForItem;
      self.model.ShowBarcode = ShowBarcode;
      self.model.PrintBarcode = PrintBarcode;
      self.model.PrintBarcodeOnBarcodeCilck = PrintBarcodeOnBarcodeCilck;
      self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;
      self.model.bonusBatchExpiryModalOnClick=bonusBatchExpiryModalOnClick;

      GetGRNList();
      
      $scope.storeId = $rootScope.getStoreObj() != null ? $rootScope.getStoreObj().storeId : undefined; // store-accessor campaign: null-safe parse + picker trigger, one owner (BARE deref at init, QF-AR class)
    }

    var SetDecimal = function (name, item, value) {
      if (name == "taxPercentFixed") {
        if (isNaN(item.selectedTaxPercent) || !item.selectedTaxPercent) {
          item.selectedTaxPercent = 0;
        } else {
          item.selectedTaxPercent = parseFloat(item.selectedTaxPercent).toFixed(2);
        }
      }
    }

    var SelectTax = function (tax, item) {
      if (!!tax.taxId) {
        item.selectedTaxPercent = tax.taxPercent.toFixed(2);
        item.discountAmount = parseFloat((item.poAmount) * (item.discount / 100)).toFixed(2);
        self.model.disAmount = parseInt(item.discountAmount);
        item.taxAmount = ((item.poAmount-self.model.disAmount) * (item.selectedTaxPercent/100));
        if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
          item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculationSkipPI) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
        }
        else{
          item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
        }
        $timeout( function(){
          $scope.calculateRoundoff();
        },1000);
      }
      else {
        item.selectedTaxPercent = "";
      }
    }

    var discountCalc = function (item) {
      if (!parseFloat(item.discount)) {
        item.discount = 0;
      }
      item.poAmount = item.poAmount || 0;
      if(item.poQuantity){
    	  if($scope.isSkipPhysicalInspection)
          {
    		  item.poAmount = item.cop * item.grnReceivedQuantity;
          }
    	  else
    		  {
          item.poAmount = item.cop * (item.grnReceivedQuantity-(item.rejectedQuantity!=undefined && item.rejectedQuantity!=null ? item.rejectedQuantity : 0 )) ;
    		  }
      }
      else
    	  {
    	  if($scope.isSkipPhysicalInspection)
          {
           item.poAmount = (item.approvedQuantity+item.rejectedQuantity) * item.cop;
          }
         else
          {
           item.poAmount = item.approvedQuantity * item.cop;
          }
    	  }
      item.totalDiscount = ((item.poAmount) * (item.discount / 100)).toFixed(2);
      item.totalDiscount = parseFloat(item.totalDiscount);
      self.model.disAmount = item.totalDiscount;
      item.taxAmount = ((item.poAmount - item.totalDiscount) * (item.selectedTaxPercent/100));
      if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
        item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculationSkipPI) + (item.newBonusReceivedQuantity!=undefined && item.newBonusReceivedQuantity!=null && item.newBonusReceivedQuantity>0 ? parseInt(item.newBonusReceivedQuantity) : parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0))));
      }
      else{
        item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
      }
      $timeout( function(){
        $scope.calculateRoundoff();
      },1000);
    }

    $scope.calculateRoundoff = function () {
      if($scope.amountround1 != 0){
        $scope.amountround1 = 0;
      }
      self.model.totalAmount = 0;
      self.model.totalNetAmount = 0;
      self.model.totalDiscount = 0;
      self.model.totalTax = 0;
      self.model.otherchargesAmount=0;
      self.model.grnList.totalOtherAmount=0;
      angular.forEach(self.model.grnList.items, function(item){
        if(item.otherchargesAmount == undefined)
        {
          item.otherchargesAmount = 0;
        }
        if(item.poAmount == undefined){
          item.poAmount = 0;
        } 
        if(item.selectedTaxPercent == undefined){
          item.selectedTaxPercent = 0;
        }
        if(self.model.otherchargesAmount == undefined){
          self.model.otherchargesAmount = 0;
        }
        if(!item.totalDiscount){
          item.totalDiscount = 0;
        }
        if(item.taxAmount == undefined){
          item.taxAmount = 0;
        }
        if(self.model.prRequestTypeId == 4){
          if(item.grnReceivedQuantity == undefined){
            item.grnReceivedQuantity = item.newPOQty;
          }
          item.poAmount = item.grnReceivedQuantity * item.cop;
        }
        else{
          item.poAmount = item.grnReceivedQuantity * item.cop;
        }
        if(item.isSelected || self.model.grnList.approvalStatusId==5){
          if(item.grnApprovedQuantity!=undefined && item.grnApprovedQuantity!=null && item.grnApprovedQuantity!=0 ){
        	  if($scope.isSkipPhysicalInspection)
       	    {
        		  item.poAmount = ((item.grnApprovedQuantity+item.rejectedQuantity) * item.cop).toFixed(2);
       	    }
            else
       		  {
        	    item.poAmount = (item.grnApprovedQuantity * item.cop).toFixed(2);
       		  }
          }
          else{
            if(item.approvedQuantity!=undefined && item.approvedQuantity!=null && item.approvedQuantity!=0){
              if($scope.isSkipPhysicalInspection)
               {
                item.poAmount = (item.approvedQuantity+item.rejectedQuantity) * item.cop;
               }
              else
               {
                item.poAmount = item.approvedQuantity * item.cop;
               }
            }
            else
            	{
            	if($scope.isSkipPhysicalInspection)
                {
                 item.poAmount = (item.approvedQuantity+item.rejectedQuantity) * item.cop;
                }
            	else
            		{
            		item.poAmount = 0;
            		}
            	}
          }
        }
        else
        {
        	item.poAmount=0;
        }
        if(item.isSelected || self.model.grnList.approvalStatusId==5){
          item.poAmount = parseFloat(item.poAmount);
          item.taxAmount = ((item.poAmount - item.totalDiscount) * (item.selectedTaxPercent/100));
          item.netAmount = ((item.poAmount - item.totalDiscount) + parseFloat(item.taxAmount)+parseFloat(item.otherchargesAmount!=undefined && item.otherchargesAmount!=null && item.poAmount>0 ? parseFloat(item.otherchargesAmount):0.00));
          self.model.totalAmount += item.poAmount;
          self.model.totalDiscount += item.totalDiscount;
          self.model.totalTax += item.taxAmount;
          self.model.otherchargesAmount +=parseFloat(item.otherchargesAmount!=undefined && item.otherchargesAmount!=null && item.poAmount>0 ? parseFloat(item.otherchargesAmount):0.00);
          self.model.grnList.totalOtherAmount +=parseFloat(item.otherchargesAmount!=undefined && item.otherchargesAmount!=null && item.poAmount>0 ? parseFloat(item.otherchargesAmount):0.00);
          if(item.poAmount==undefined || item.poAmount==null || item.poAmount==0)
        	  {
        	  item.otherchargesAmount=0.00;
        	  }
          if(self.model.grnList.discountTypeId == 2)
        	  {
        	  self.model.grnList.discount = self.model.totalDiscount;
        	  }
          else
        	  {
        	  self.model.grnList.discount =self.model.grnList.discount;
        	  }
          self.model.totalNetAmount = parseFloat(self.model.totalNetAmount) + parseFloat(item.netAmount);
          $scope.amountround1 = $scope.amountround1 + (item.poAmount - parseFloat(item.totalDiscount) + item.taxAmount + (item.otherchargesAmount!=undefined && item.otherchargesAmount!=null && self.model.totalNetAmount>0 ? item.otherchargesAmount :0) );
          $scope.amountround = $scope.amountround1.toFixed();
          $scope.amount1 = parseFloat($scope.amountround1).toFixed(2);
          $scope.roundOff1 = parseFloat($scope.amountround) - $scope.amount1;
          if($scope.roundOff1 < 0.50){
            self.model.totalNetAmount = $scope.amountround;
            self.model.grnList.netAmount = self.model.totalNetAmount;
            $scope.roundOff = parseFloat($scope.amountround) - $scope.amount1;
            self.model.grnList.roundOff = $scope.roundOff;
            $scope.roundOff = $scope.roundOff.toFixed(2);
          }
          if($scope.roundOff1 >= 0.50){
            self.model.totalNetAmount = math.round($scope.amount1);
            self.model.grnList.netAmount = self.model.totalNetAmount;
            $scope.roundOff = self.model.totalNetAmount - $scope.amount1;
            self.model.grnList.roundOff = $scope.roundOff;
            $scope.roundOff = $scope.roundOff.toFixed(2);
          }
          $scope.roundAmount=$scope.roundOff;
          $scope.netAmt=self.model.totalNetAmount;
        }
        else{
        	self.model.totalCreditNoteAmount=0.00;
        }
      });
      if(self.model.totalCreditNoteAmount){
        $scope.amountround1 = $scope.amountround1 - self.model.totalCreditNoteAmount;
        $scope.amountround = $scope.amountround1.toFixed();
        $scope.amount1 = parseFloat($scope.amountround1).toFixed(2);
        $scope.roundOff1 = parseFloat($scope.amountround) - $scope.amount1;
        if($scope.roundOff1 < 0.50){
          self.model.totalNetAmount = $scope.amountround;
          $scope.roundOff = parseFloat($scope.amountround) - $scope.amount1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
        }
        if($scope.roundOff1 >= 0.50){
          self.model.totalNetAmount = math.round($scope.amount1);
          $scope.roundOff = self.model.totalNetAmount - $scope.amount1;
          $scope.roundOff = $scope.roundOff.toFixed(2);
        }
      }
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
                bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity,
                origninalBonusQty: grnBonusBatchExpiryCount.origninalBonusQty
            });
          });
        batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
            }
        else
        	{
        	sumOfBonusReceivedQuantity=0;
        	self.model.bonusBatchExpiryData.push({
        		grnBonusBatchExpiryId:null,
        		batchNo: "",
        		expiryDate: "",
        		bonusQuantity: ""
            });
        	}
        
    }

    var bonusBatchExpiryModalOnClick = function () {
      self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData = [];
      var modal_list = self.model.bonusBatchExpiryData;
      modal_list.forEach(function (modal, index) {
        self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.push({
          grnBonusBatchExpiryId:modal.grnBonusBatchExpiryId!=null ? modal.grnBonusBatchExpiryId :null ,
          batchNo: modal.batchNo,
          expiryDate: modal.expiryDate,
          bonusQuantity: (modal.bonusQuantity != undefined && modal.bonusQuantity != null) ? modal.bonusQuantity : 0,
          origninalBonusQty:modal.origninalBonusQty
        });
      });
      var sumOfBonusReceivedQuantity=0;
      var originalSumOfBonusReceivedQuantity=0;
      if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=undefined && self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=null && self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.length>0)
      {
        angular.forEach(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
        	 if($scope.isSkipPhysicalInspection == true)
        		{
        		 originalSumOfBonusReceivedQuantity=originalSumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.origninalBonusQty);
        		}
	        	  sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
        });
      }
      self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity=sumOfBonusReceivedQuantity;
      self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].newBonusReceivedQuantity=originalSumOfBonusReceivedQuantity;
      if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].isThreeUOMApplicable==true)
      {
        if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUomTypeId == 1)
        {
    	    self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCon = (self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity+"*"+ self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion+"*"+ self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCalculation = (self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity * self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion * self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uom[2].uomUnit;
        }
        if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUomTypeId == 2)
        {
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCon=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity+"*"+ self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCalculation =(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity * self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[1].conversion);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uom[2].uomUnit;
        }
        if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUomTypeId == 3)
        {
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCon=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCalculation=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uom[2].uomUnit;
        }
      }
      else
      {
    	  if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUomTypeId == 1)
        {
    		  self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCon=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity +"*"+ self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCalculation=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity * self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUom[0].conversion);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomListdata[2].uomUnit;
        }
        if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusUomTypeId == 3)
        {
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCon=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusQtyCalculation=(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity);
          self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomUnitDataBonus = self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].uomListdata[2].uomUnit;
        }
      }
      self.model.bonusBatchExpiryData=[];
      discountCalc(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex]);
      self.model.ClosePopup('Close', 'bouns_selectBatchExpiry_modal');
    }

    $scope.closeBonusBatchExpiry =function()
    {
    	self.model.bonusBatchExpiryData=[];
    }

    var AddItemDiscount = function (type, item, value, index) {
      if (type == 'percent') {
        item.discount = parseFloat(item.discount);
        if (isNaN(item.discount) || !item.discount || item.discount == 0) {
          item.discount = 0;
          item.totalDiscount = 0;
          if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
            item.lcop = (((item.poAmount - item.discount) + item.taxAmount) / (parseInt(item.receivedQtyCalculationSkipPI) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
          }
          else{
            item.lcop = (((item.poAmount - item.discount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
          }
          $timeout( function(){
        	  if(isNaN(self.model.otherChargesAmount) || self.model.otherChargesAmount==undefined || self.model.otherChargesAmount==null )
        		  {
        		  self.model.otherChargesAmount=self.model.totalOtherCharges!=undefined && self.model.totalOtherCharges!=null ? self.model.totalOtherCharges:0;
        		  }
        	  $scope.calculateRoundoff();
        	calculateOtherChargesAmountForItem();
            $scope.calculateRoundoff();
          },1000);
        } else {
          if (parseFloat(item.discount) > 100) {
            self.model.discount[index] = true;
            growl.error('Discount must be less than 100% of total amount');
          } else {
            if (!item.poAmount) {
              item.totalDiscount = 0;
              item.lcop = 0;
            } else {
              item.totalDiscount = (item.poAmount) * (item.discount / 100);
              self.model.disAmount = item.totalDiscount;
              item.totalDiscount = parseFloat(item.totalDiscount).toFixed(2);
              item.totalDiscount=parseFloat(item.totalDiscount);
              if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
                item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculationSkipPI) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
              }
              else{
                item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
              }
              $timeout( function(){
            	  if(isNaN(self.model.otherChargesAmount) || self.model.otherChargesAmount==undefined || self.model.otherChargesAmount==null )
        		  {
        		  self.model.otherChargesAmount=self.model.totalOtherCharges!=undefined && self.model.totalOtherCharges!=null ? self.model.totalOtherCharges:0;
        		  }
            	  $scope.calculateRoundoff();
            	calculateOtherChargesAmountForItem();
                $scope.calculateRoundoff();
              },1000);
            }
          }
        }

      } else if (type == 'amount') {
        item.totalDiscount = parseFloat(item.totalDiscount).toFixed(2);
        item.totalDiscount=parseFloat(item.totalDiscount);
        if (isNaN(item.totalDiscount) || !item.totalDiscount || item.totalDiscount == 0) {
          item.totalDiscount = 0;
          item.discount = 0;
          $timeout( function(){
        	  if(isNaN(self.model.otherChargesAmount) || self.model.otherChargesAmount==undefined || self.model.otherChargesAmount==null )
    		  {
    		  self.model.otherChargesAmount=self.model.totalOtherCharges!=undefined && self.model.totalOtherCharges!=null ? self.model.totalOtherCharges:0;
    		  }
        	  $scope.calculateRoundoff();
        	calculateOtherChargesAmountForItem();
            $scope.calculateRoundoff();
          },1000);
        } else {
          if (parseFloat(item.totalDiscount) > (parseFloat(item.poAmount) / 2)) {
            self.model.totalDiscount[index] = true;
            growl.error('Discount amount should be less than half of total amount');
          } else {
            if (!item.poAmount) {
              item.discount = 0;
              item.lcop = 0;
            } else {
              item.discount = ((parseFloat(item.totalDiscount) / item.poAmount) * 100);
              self.model.disAmount = item.totalDiscount;
              item.discount = parseFloat(item.discount);
              if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
                item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / ( parseInt(item.receivedQtyCalculationSkipPI) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
              }
              else{
                item.lcop = (((item.poAmount - self.model.disAmount) + item.taxAmount) / ( parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
              }
              $timeout( function(){
            	  if(isNaN(self.model.otherChargesAmount) || self.model.otherChargesAmount==undefined || self.model.otherChargesAmount==null )
        		  {
        		  self.model.otherChargesAmount=self.model.totalOtherCharges!=undefined && self.model.totalOtherCharges!=null ? self.model.totalOtherCharges:0;
        		  }
            	  $scope.calculateRoundoff();
            	calculateOtherChargesAmountForItem();
                $scope.calculateRoundoff();
              },1000);
            }
          }
        }
      }
    }

    var SetDecimalOtherCharge = function (item) {
      if (isNaN(item.amount) || !item.amount || item.amount == 0) {
        item.amount = 0;
      } else {
        item.amount = parseFloat(item.amount).toFixed(2);
      }
    }

    var AddOtherCharge = function (ocId, index) {
      otherCharge: self.model.otherChargesDropdownList[0]
      self.model.otherChargesList.push({ otherChargesId: ocId, amount: 0 });
      self.model.otherChargesList[index + 1].otherCharge = self.model.otherChargesDropdownList[0];
      if(self.model.otherChargesList.length > 1){
        $scope.showMinus = true;
      }
      else{
        $scope.showMinus = false;
      }
    }

    var lessOtherCharge = function (item, idx) {
      var index = self.model.otherChargesList.indexOf(item);
      self.model.otherChargesList.splice(index, 1);
      self.model.grnList.grnOtherChargesMapper.splice(index, 1);
      if(self.model.otherChargesList.length > 1){
        $scope.showMinus = true;
      }
      else{
        $scope.showMinus = false;
      }
    }

    var validateCharges = function () {
      var isValidItem = true;
      if (!self.model.otherChargesList.length) {
        growl.error('Please add other charges and amount');
        isValidItem = false;
      } else {
        angular.forEach(self.model.otherChargesList, function (charges) {

          charges['isChargesValid'] = false;
          charges['isAmountValid'] = false;

          if (parseFloat(charges.amount)) {
            if (!charges.otherCharge.id) {
              charges['isChargesValid'] = true;
              growl.error('Please select other charges');
              isValidItem = false;
            }
          }
        });
      }
      return isValidItem;
    }

    function DuplicateData(chargeList) {
      var isValid = true;
      var object = {};
      var result = [];

      chargeList.forEach(function (item) {
        if (!!item.otherCharge.id) {
          if (!object[item.otherCharge.id])
            object[item.otherCharge.id] = 0;
          object[item.otherCharge.id] += 1;
        }
      })

      for (var prop in object) {
        if (object[prop] >= 2) {
          result.push(prop);
        }
      }

      if (!!result.length) {
        isValid = false;
        growl.error("Duplicate other charges");
      }

      return isValid;
    }
    $scope.cancelOtherCharges = function ()
    {
//    	 var total = 0;
//    	 var otherList=[];
//    	 self.model.otherChargesList=$scope.otherCharList;
//         self.model.otherChargesList.forEach(function (value) {
//        	 value.otherChargesId
//           value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
//             if (value.otherCharge.id == key.id) {
//               value.otherChargesId = key.id;
//               return key;
//             }
//           });
//         });
//         var newOtherChargeList=[];
//         $scope.otherCharList = self.model.grnList.grnOtherChargesMapper;
//         self.model.grnList.grnOtherChargesMapper = [];
//         angular.forEach($scope.otherCharList, function (value, key) {
//           angular.forEach(self.model.otherChargesList, function(datacharges){
//             if(value.otherChargesId != datacharges.otherChargesId){
//               var obj = {
//                 otherChargesId : '',
//                 amount : '',
//                 grnId: '',
//                 isFromGrn:''
//               }
//               obj.amount = datacharges.amount;
//               obj.grnId = value.grnId;
//               obj.isFromGrn = false;
//               if(value.otherChargesId==undefined)
//               {
//             	  obj.otherChargesId = datacharges.otherChargesId;
//               }
//               else
//               {
//             	  obj.otherChargesId = datacharges.otherChargesId;
//               }
//               newOtherChargeList.push(obj);
//             }
//             else if(value.otherChargesId == datacharges.otherChargesId){
//               var obj = {
//                 id: '',
//                 grnId: '',
//                 otherCharges: '',
//                 otherChargesId : '',
//                 amount : '',
//                 isFromGrn :''
//               }
//               obj.id = value.id;
//               obj.grnId = value.grnId;
//               obj.otherCharges = datacharges.otherCharges;
//               obj.amount = datacharges.amount;
//               obj.otherChargesId = datacharges.otherChargesId;
//               obj.isFromGrn = value.isFromGrn;
//               newOtherChargeList.push(obj);
//             }
//             else{
//               if(!value.id){
//                 var obj = {
//                   otherChargesId : '',
//                   amount : '',
//                 }
//                 obj.amount = datacharges.amount;
//                 obj.otherChargesId = datacharges.otherChargesId;
//                 obj.isFromGrn = value.isFromGrn;
//                 newOtherChargeList.push(obj);
//               }  
//             }
//           });
//         });
//         var uniqueArray=[];
//         $.each(newOtherChargeList, function (i, e) {
// 		    var matchingItems = $.grep(uniqueArray, function (item) {
// 		       return item.otherChargesId === e.otherChargesId;
// 		    });
// 		    if (matchingItems.length === 0){
// 		    	uniqueArray.push(e);
// 		    }
// 		});
//         newOtherChargeList=uniqueArray;
//         angular.forEach(newOtherChargeList, function (value, key) {
//         	total = total + parseFloat(value.amount);
//         	if(value.amount!=undefined && value.amount!=null && value.amount>0)
//            {
//            otherList.push(value)
//            }
//        })
//         newOtherChargeList=otherList;
//         self.model.grnList.grnOtherChargesMapper = newOtherChargeList;
//         self.model.grnList.totalOtherAmount = total;
//         self.model.otherChargesAmount = total;
         self.model.ClosePopup('close', 'otherCharges_Modal');
//         calculateOtherChargesAmountForItem();
    }
    var sumOtherCharges = function (chargeList) {
      if (validateCharges() && DuplicateData(chargeList)) {
        var total = 0;
        self.model.otherChargesList.forEach(function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            if (value.otherCharge.id == key.id) {
              value.otherChargesId = key.id;
              return key;
            }
          });
        });

        var newOtherChargeList=[];
        $scope.otherCharList = self.model.grnList.grnOtherChargesMapper;
        self.model.grnList.grnOtherChargesMapper = [];
        angular.forEach($scope.otherCharList, function (value, key) {

          angular.forEach(self.model.otherChargesList, function(datacharges){
            if(value.otherChargesId != datacharges.otherChargesId){
              var obj = {
                otherChargesId : '',
                amount : '',
                grnId: '',
                isFromGrn:''
              }
              obj.amount = datacharges.amount;
              obj.grnId = value.grnId;
              obj.isFromGrn = false;
              if(value.otherChargesId==undefined)
              {
            	  obj.otherChargesId = datacharges.otherChargesId;
              }
              else
              {
            	  obj.otherChargesId = datacharges.otherChargesId;
              }
              newOtherChargeList.push(obj);
            }
            else if(value.otherChargesId == datacharges.otherChargesId){
              var obj = {
                id: '',
                grnId: '',
                otherCharges: '',
                otherChargesId : '',
                amount : '',
                isFromGrn :''
              }
              obj.id = value.id;
              obj.grnId = value.grnId;
              obj.otherCharges = datacharges.otherCharges;
              obj.amount = datacharges.amount;
              obj.otherChargesId = datacharges.otherChargesId;
              obj.isFromGrn = value.isFromGrn;
              newOtherChargeList.push(obj);
            }
            else{
              if(!value.id){
                var obj = {
                  otherChargesId : '',
                  amount : '',
                }
                obj.amount = datacharges.amount;
                obj.otherChargesId = datacharges.otherChargesId;
                obj.isFromGrn = value.isFromGrn;
                newOtherChargeList.push(obj);
              }  
            }
          });
        });
        var uniqueArray=[];
        $.each(newOtherChargeList, function (i, e) {
		    var matchingItems = $.grep(uniqueArray, function (item) {
		       return item.otherChargesId === e.otherChargesId;
		    });
		    if (matchingItems.length === 0){
		    	uniqueArray.push(e);
		    }
		});
        newOtherChargeList=uniqueArray;
        angular.forEach(newOtherChargeList, function (value, key) {
        	total = total + parseFloat(value.amount);
        })
        self.model.grnList.grnOtherChargesMapper = newOtherChargeList;
        self.model.grnList.totalOtherAmount = total;
        self.model.otherChargesAmount = total;
        self.model.ClosePopup('close', 'otherCharges_Modal');
        calculateOtherChargesAmountForItem();
      }
    }

    var calculateOtherChargesAmountForItem = function () {
      angular.forEach(self.model.grnList.items, function(val){
        self.model.otherChargesPer = ((val.netAmount * 100) / self.model.totalNetAmount); 
        self.model.calculateOtherCharges = (self.model.otherChargesAmount * (self.model.otherChargesPer / 100));
        val.otherChargesAmount = self.model.calculateOtherCharges;
        val.otherchargesAmount = self.model.calculateOtherCharges;
        val.discountAmount = (val.poAmount) * (val.discount / 100);
        self.model.disAmount = val.discountAmount;
        val.netAmount = ((val.poAmount - self.model.disAmount) + (val.taxAmount + val.otherChargesAmount));
        if($scope.isSkipPhysicalInspection && (val.receivedQtyCalculationSkipPI != null && val.receivedQtyCalculationSkipPI != undefined)){
          val.lcop = (((val.poAmount - self.model.disAmount) + (val.taxAmount + val.otherChargesAmount)) / (parseInt(val.receivedQtyCalculationSkipPI) + (val.newBonusReceivedQuantity!=undefined && val.newBonusReceivedQuantity!=null && val.newBonusReceivedQuantity>0 ? parseInt(val.newBonusReceivedQuantity) : parseInt(val.bonusQtyCalculation!=undefined && val.bonusQtyCalculation!=null ? val.bonusQtyCalculation:0))));
        }
        else{
          val.lcop = (((val.poAmount - self.model.disAmount) + (val.taxAmount + val.otherChargesAmount)) / (parseInt(val.receivedQtyCalculation) + parseInt(val.bonusQtyCalculation!=undefined && val.bonusQtyCalculation!=null ? val.bonusQtyCalculation:0)));
        }
        $timeout( function(){
          $scope.calculateRoundoff();
        },1000);
      })
    }

    var PrintBarcode = function (item) {
        self.model.barcode = {
          type: "code128",
          code: item.itemId.toString() + " " + item.batchId.toString(),
          hospital: $scope.hospitalName,
          itemName: $scope.itemName,
          itemCode: $scope.itemCode,
          batchCode: $scope.batchNo,
          lotNo: $scope.lotNo,
          expiry: $scope.expiryDate,
          mrp: self.model.Currency + $filter('number')($scope.mrp!=undefined ? $scope.mrp :0.0, 2),
          salePrice: $scope.salePrice,
          grnNo: $scope.grnNo,
          vendorCode: $scope.vendorCode,
          vendorName: $scope.vendorName
        }
        $timeout(function () {
          var printContents = $window.document.getElementById('barcode').innerHTML;
          var popupWin = $window.open('', '_blank', 'width=300,height=400');
          popupWin.document.open();
          popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()" style="height: 150px;">' + printContents + '</body></html>');
          popupWin.document.close();
        }, 100);
      }
    
      var ShowBarcode = function (item) {
        var URI = CONSTANTS.MASTER.UNIT.CONSUMABLE_BARCODE_CONFIG.CONSUMABLE_BARCODE_CONFIG_GET_API + $scope.unitId;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {        
              angular.forEach(response.data, function(barcode){
                  $scope.id = barcode.id;
                  if(barcode.itemName == true){
                      self.model.isItemName = true;
                      $scope.itemName = item.itemName;
                  }
                  if(barcode.itemCode == true){
                      self.model.isItemCode = true;
                      $scope.itemCode = item.itemCode;
                  }
                  if(barcode.batchNo == true){
                      self.model.isBatchNo = true;
                      $scope.batchNo = item.batchNo;
                  }
                  if(barcode.expiryDate == true){
                      self.model.isExpiryDate = true;
                     // $scope.expiryDate = item.expiryDate;
                      $scope.expiryDate = moment(item.expiry).format('MM/YY');
                  }
                  if(barcode.lotNo == true){
                      self.model.isLotNo = true;
                      $scope.lotNo = item.lotNo;
                  }
                  if(barcode.mrp == true){
                      self.model.isMRP = true;
                      $scope.mrp = item.mrp;
                  }
                  if(barcode.salePrice == true){
                      self.model.isSalesPrice = true;
                      $scope.salePrice = item.salePrice;
                  }
                  if(barcode.grnNo == true){
                      self.model.isGrnNo = true;
                      $scope.grnNo = item.grnNo;
                  }
                  if(barcode.vendorName == true){
                      self.model.isVendorName = true;
                      $scope.vendorName = item.vendorName;
                  }
                  if(barcode.vendorCode == true){
                      self.model.isVendorCode = true;
                      $scope.vendorCode = item.vendorCode;
                  }
                  if(barcode.hospitalName == true){
                      self.model.isHopspitalName = true;
                      $scope.hospitalName = item.hospitalName
                  }
              });
              self.model.barcode = {
                  type: "code128",
                  code: item.itemId.toString() + " " + item.batchId.toString(),
                 // code: "156517 2386",
                  hospital: $scope.hospitalName,
                  itemName: $scope.itemName,
                  itemCode: $scope.itemCode,
                  batchCode: $scope.batchNo,
                  lotNo: $scope.lotNo,
                  expiry: $scope.expiryDate,
                  mrp: self.model.Currency + $filter('number')($scope.mrp!=undefined ? $scope.mrp :0.0, 2),
                  salePrice: $scope.salePrice,
                  grnNo: $scope.grnNo,
                  vendorCode: $scope.vendorCode,
                  vendorName: $scope.vendorName
              }
              var popup = angular.element('#barcodePreview');
              popup.modal('show');
              $rootScope.stopLoader();
          },
          function errorCallback(response) {
              $rootScope.stopLoader();
          });
      }
    
      var PrintBarcodeOnBarcodeCilck = function () {
        self.model.barcode = {
          type: "code128",
          code: self.model.barcode.code,
          hospital: $scope.hospitalName,
          itemCode: $scope.itemCode,
          itemName: $scope.itemName,
          batchNo: $scope.batchNo,
          batchCode: $scope.batchCode,
          expiry: $scope.expiryDate,
          mrp: $scope.mrp,
          lotNo: $scope.lotNo,
          salePrice: $scope.salePrice,
          grnNo: $scope.grnNo,
          vendorName: $scope.vendorName,
          vendorCode: $scope.vendorCode
        }

        $timeout(function () {
          var printContents = $window.document.getElementById('barcode').innerHTML;
          var popupWin = $window.open('', '_blank', 'width=300,height=400');
          popupWin.document.open();
          popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
          popupWin.document.close();
        }, 100);
      }
    
    var fillOtherChargesList = function (item) {
       angular.forEach(item.items, function(data){
        if(item.grnOtherChargesMapper){
          $scope.selectedOtherCharges = [];
          angular.forEach(item.grnOtherChargesMapper, function(data){
            var obj = {
              otherChargesId: '',
              amount: ''
            }
            obj.otherChargesId = data.otherChargesId;
            obj.amount = data.amount;
            if(data.grnId && item.grnOtherChargesMapper.length == 1){
              $scope.selectedOtherCharges.push(obj);
            }
            else {
                if(item.grnOtherChargesMapper.length > 1){
                    // if(!data.id){
                     
                    $scope.selectedOtherCharges.push(obj);   
                    // }       
                }
            }
            angular.forEach($scope.selectedOtherCharges, function (value) {
              value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
                return key.id == value.otherChargesId;
              });
            });
            $scope.selectedOtherCharges.forEach(function (othercharge) {
              var amountNumber = parseFloat(othercharge.amount);
              if (amountNumber == 0) {
                amountNumber = 0;
              } else {
                othercharge.amount = amountNumber.toFixed(2);
              }
            });
            $scope.dataother = $scope.selectedOtherCharges;
          });
        }
        else{
          data['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
          angular.forEach(data.selectedOtherCharges, function (value) {
            value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
              return key.id == value.otherChargesId;
            });
          });
          data.selectedOtherCharges.forEach(function (othercharge) {
            var amountNumber = parseFloat(othercharge.amount);
            if (amountNumber == 0) {
              amountNumber = 0;
            } else {
              othercharge.amount = amountNumber.toFixed(2);
            }
          });
        }
        if(!item.grnOtherChargesMapper){
          self.model.otherChargesList = data.selectedOtherCharges;
        }
        else{
        	if($scope.dataother!=undefined && $scope.dataother!=null && $scope.dataother.length>0)
        		{
        		self.model.otherChargesList = $scope.dataother
        		}
	          else
	        	  {
	        	  if(self.model.otherChargesList==undefined || self.model.otherChargesList==null || self.model.otherChargesList.length==0)
	        		  {
	        	  self.model.otherChargesList=[];
	        	  self.model.grnList.grnOtherChargesMapper=[{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
	        	  AddOtherCharge(undefined,-1);
	        	  }
	        	  if(self.model.grnList.totalOtherAmount==undefined || self.model.grnList.totalOtherAmount==null || self.model.grnList.totalOtherAmount==0)
	        		  {
	        		  angular.forEach(self.model.otherChargesList, function(data){
	        			  data.otherChargesId = undefined;
	        			  data.amount = null;
	        		  });
	        		  }
	        	  }
        }
        
        self.model.selectedOtherChargeIndex = self.model.grnList.items.indexOf(data);
        if(self.model.otherChargesList.length > 1){
          $scope.showMinus = true;
        }
        else{
          $scope.showMinus = false;
        }
      });
    }

    var ViewHistory = function(item){
      var URI = CONSTANTS.GLOBAL.PO_HISTORY + $scope.unitId + '/' + item.itemId+ '/' + item.poId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastOrderDetails = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ViewStagedQuantity = function (item) {
      self.model.grnList.items.find(function(key){
        if (item.id == key.id) {
          self.model.grnStagedQuantity = key.grnStagedQuantity;
          key.grnStagedQuantity.forEach(function(data){
            data.poGrnQuantity = item.poGrnQuantity;
            var quantityCalculatedx = 0;
          	 var poQuantityx = 0;
          	
          	if(item.isThreeUOMApplicable == true){
                  if(item.poGrnUomTypeId==1)
                  	{
                  	poQuantityx = parseInt(item.poQuantity * (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
                  	}
                  else if(item.poGrnUomTypeId==2)
                  	{
                  	poQuantityx =parseInt(item.poQuantity * (item.uom[1].conversion * item.uom[2].conversion));
                  	}
                  else
                  	{
                  	poQuantityx =parseInt(item.poQuantity * (item.uom[2].conversion));
                  	}
                  	}
                  else
                  	{
                  	 if(item.poGrnUomTypeId==1)
                   	{
                  		 poQuantityx = parseInt(item.poQuantity * (item.uom[0].conversion *  item.uom[2].conversion));
                   	}
                   else
                   	{
                  	 poQuantityx =parseInt(item.poQuantity * (item.uom[2].conversion));
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
      		// x.poApprovedQuantity = quantityCalculatedx; 
      		 
      		  data.poPendingQuantity = quantityCalculatedx-item.grnReceivedQuantity;
          })
        }
      });
    }

    var ViewPOList = function (item) {
      self.model.grnList.items.find(function(key){
        if (item.id == key.id) {
          self.model.poList = key.poList;
        }
      });
    }

    var ValidateSaveButton = function(){
      var isValid = false;
      if (self.model.grnList.items) {
        self.model.grnList.items.find(function(item){
          if(!!item.isSelected){
            isValid = true;
          }
        });

        return isValid;
      }
    }
    
    $scope.isSkipPhysicalInspection;
    var GetGRNList = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + commonDetailService.getDataId();
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	$rootScope.stopLoader();
          self.model.grnList = response.data;
          $scope.isSkipPhysicalInspection=response.data.isSkipPhysicalInspection;
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.taxList = response.data.taxList;
          self.model.grnTypeId = response.data.grnTypeId;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data.items));
          $scope.otherChargesMapper=response.data.grnOtherChargesMapper;
          self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
          if(self.model.grnList.roundOff != undefined){
            $scope.roundOff = self.model.grnList.roundOff.toFixed(2);
            $scope.roundAmount = $scope.roundOff; 
          }
//          self.model.totalNetAmount = self.model.grnList.netAmount+self.model.grnList.totalOtherAmount;
//          $scope.netAmt = self.model.totalNetAmount;
          self.model.selectAllRows = true;
          self.model.totalAmount = 0;
          self.model.totalDiscount = 0;
          self.model.totalTax = 0;
          self.model.totalNetAmount=0;
          $scope.isGrnBonusBatchExpiry=false;
          self.model.grnList.totalOtherAmount=0;
          var statusCounter=0;
          self.model.grnList.items.forEach(function (item) {
        	  if(item.statusId==10)
        		  {
        		  statusCounter++;
        		  }
          });
          self.model.grnList.items.forEach(function (item) {
        	  if(statusCounter>0)
        		  {
        		  item.statusId=10;
        		  item.status="Pending Approval";
        		  }
        	  if(item.discount!=undefined && item.discount!=null)
        		  {
        	  item.discount=item.discount.toFixed(2);
        		  }
        	  if(isNaN(item.approvedQuantity) || item.approvedQuantity==undefined || item.approvedQuantity==null )
        		  {
        		  item.approvedQuantity=0;
        		  }
        	  if(isNaN(item.rejectedQuantity) || item.rejectedQuantity==undefined || item.rejectedQuantity==null )
    		  {
    		  item.rejectedQuantity=0;
    		  }
            if(self.model.grnList.approvalStatusId == 5){
              item.isSelected = false;
              self.model.selectAllRows = false;
            }
            else{
              item.isSelected = true;
            }
            item.isDisabled = false;
            item.statusId = 5;
            if(item.grnBonusBatchExpiry.length==0)
            {
            	item.isGrnBonusBatchExpiry=true;
            }
            else
            	{
            	item.isGrnBonusBatchExpiry=false;
            	}
            item.grnBonusBatchExpiryData=item.grnBonusBatchExpiry;
            item.grnBonusBatchExpiryData.forEach(function(key){
                key.origninalBonusQty=key.bonusQuantity;
              });
            self.model.SelectedItems.push(item);
            // item.approvedQuantity = item.grnApprovedQuantity!=null ? item.grnApprovedQuantity : 0;
            item.grnStagedQuantity.forEach(function(key){
            	if(self.model.grnList.approvalStatusId!=5)
              {
            		key.approvedQuantity = key.receivedQuantity;
              }
            });
            item.forApproveStageQty = item.grnStagedQuantity;
            item.totalOtherAmountcal = 0;
            item.otherchargesAmount = 0;
            if (self.model.grnTypeId == 4) {
            	if(item.rejectedQuantity>0)
              {
            		item.grnApprovedQuantity=item.grnApprovedQuantity;
              }else
              {
                item.grnApprovedQuantity = item.grnReceivedQuantity;
                item.rejectedQuantity = 0;
              }
            } else if (self.model.grnTypeId == 1) {
            	if(item.rejectedQuantity!=0 && item.rejectedQuantity!= null)
        		  {
            		item.approvedQuantity = item.grnApprovedQuantity;
        		  }else
        			{
                item.approvedQuantity = item.grnReceivedQuantity;
                if(self.model.grnList.discountTypeId == 2){
                  if(item.grnApprovedQuantity != null){
                    item.approvedQuantity = item.grnApprovedQuantity;
                  }
                }
        			}
            }
            angular.forEach(self.model.grnList.grnOtherChargesMapper, function(otherc){
              item.totalOtherAmountcal += otherc.amount;
            });
            if(self.model.grnList.approvalStatusId==7)
            {
                item.totalDiscount=0;
            }
            item.otherchargesAmount += item.totalOtherAmount;
            self.model.grnList.totalOtherAmount += item.totalOtherAmount;
            if(item.grnApprovedQuantity)
            {
              if($scope.isSkipPhysicalInspection)
              {
                item.poAmount = ((item.grnApprovedQuantity + item.rejectedQuantity) * item.cop).toFixed(2);
              }
              else
              {
                item.poAmount = (item.grnApprovedQuantity * item.cop).toFixed(2);
              }
              if(self.model.grnList.discountTypeId != 2){
                item.totalDiscount = (item.poAmount) * (item.discount / 100);
              }    
              if(self.model.grnList.approvalStatusId != 5){
                item.taxAmount = (item.poAmount - item.totalDiscount) * (item.taxPercentage / 100);
              }
              item.netAmount = ((item.poAmount) - item.totalDiscount + (item.taxAmount) + (item.totalOtherAmount));
            }
            else{
            	if(item.isSelected)
              {
                if($scope.isSkipPhysicalInspection)
                {
                  item.poAmount = ((item.approvedQuantity + item.rejectedQuantity) * item.cop).toFixed(2);
                }
                else
                {
                  item.poAmount = (item.approvedQuantity * item.cop).toFixed(2);
                }
                if(self.model.grnList.approvalStatusId != 5){
                  item.taxAmount = (item.poAmount - item.totalDiscount) * (item.taxPercentage / 100);
                } 
                item.netAmount = ((item.poAmount) - item.totalDiscount + (item.taxAmount) + (item.totalOtherAmount));
              }
            	else
              {
            		item.poAmount = 0.00;
            		item.taxAmount = 0.00;
                item.netAmount = 0.00;
                item.totalDiscount = 0.00;
              }
            }
            item.poAmount = parseFloat(item.poAmount);
            self.model.totalAmount += item.poAmount;
            self.model.totalDiscount += item.totalDiscount;
            self.model.totalTax += item.taxAmount;
            if(self.model.grnList.discountTypeId == 2){
              self.model.grnList.discount = self.model.totalDiscount;
            }
            item.fillRejectQtyFlag = true;
            item.selectedTax = self.model.taxList.find(function (val) {
              return item.taxId == val.taxId;
            });
            if(item.selectedTax.taxPercent != 0 && item.selectedTax.taxPercent != undefined) {
              item.selectedTaxPercent = item.selectedTax.taxPercent.toFixed(2);
            }
            if(item.selectedTax.taxPercent == 0) {
              item.selectedTaxPercent = item.selectedTax.taxPercent;
            }
            if(item.selectedTax.taxPercent == undefined){
              angular.forEach(self.model.taxList, function(taxdata){
                if(taxdata.taxName == "Tax 0%" || taxdata.taxName == "0" ){
                  item.selectedTax = taxdata;
                  item.selectedTaxPercent = taxdata.taxPercent.toFixed(2);
                  item.taxId= taxdata.taxId;
                }
              });
            }

            if (!!item.rejectedQuantity) {
              item.isReject = true;
            }
            item['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
            
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
            if(isNaN(self.model.totalAmount)|| self.model.totalAmount==undefined || self.model.totalAmount==null || self.model.totalAmount==0)
            {
                self.model.totalNetAmount=0.00;
            }
            self.model.totalNetAmount+=item.netAmount;
          });
          if(self.model.totalNetAmount==0)
          {
              $scope.roundAmount=0;
          }
          self.model.totalNetAmount+=parseFloat($scope.roundAmount);
          self.model.totalNetAmount = self.model.totalNetAmount - self.model.grnList.totalCreditNoteAmount;
          $scope.netAmt = self.model.totalNetAmount;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }
    
    $scope.validateDiscount = function (item){
        var regexp = /^(\d{0,4}(\.\d{0,2})?)$/;
               
        if(!regexp.test(Number(item.discount))){
        	item.discount = 0;
            item.totalDiscount = 0;
         }
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

        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetails == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            if(item.grnApprovedQuantity != undefined && item.grnApprovedQuantity != null){
              item.receivedQtyCon = (item.grnApprovedQuantity +"*"+ item.grnUom[0].conversion);
              item.receivedQtyCalculation = (item.grnApprovedQuantity * item.grnUom[0].conversion);
              item.uomUnitData = item.uomListdata[2].uomUnit;
            }
            else{
              item.receivedQtyCon = (item.grnReceivedQuantity +"*"+ item.grnUom[0].conversion);
              item.receivedQtyCalculation = (item.grnReceivedQuantity * item.grnUom[0].conversion);
              item.uomUnitData = item.uomListdata[2].uomUnit;
            }
            if($scope.isSkipPhysicalInspection && self.model.grnList.grnTypeId != 4){
              item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity * item.grnUom[0].conversion);
            }
            else{
              item.receivedQtyCalculationSkipPI = ((item.grnReceivedQuantity) * item.grnUom[0].conversion);
            }
          } else if (uomList.uomDetails == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetails == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            if(item.grnApprovedQuantity != undefined && item.grnApprovedQuantity != null){
              item.receivedQtyCon=(item.grnApprovedQuantity);
              item.receivedQtyCalculation = (item.grnApprovedQuantity);
              item.uomUnitData = item.uomListdata[2].uomUnit;
            }
            else{
              item.receivedQtyCon=(item.grnReceivedQuantity);
              item.receivedQtyCalculation = (item.grnReceivedQuantity);
              item.uomUnitData = item.uomListdata[2].uomUnit;
            }
            if($scope.isSkipPhysicalInspection && self.model.grnList.grnTypeId != 4){
              item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity);
            }
            else{
              item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity);
            }
          }
        }
        else{
          if (uomList.uomDetails == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            if(item.grnApprovedQuantity != undefined && item.grnApprovedQuantity != null){
              item.receivedQtyCon=(item.grnApprovedQuantity +"*"+ item.grnUom[0].conversion +"*"+ item.grnUom[1].conversion);
              item.receivedQtyCalculation=(item.grnApprovedQuantity * item.grnUom[0].conversion * item.grnUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            else{
              item.receivedQtyCon=(item.grnReceivedQuantity +"*"+ item.grnUom[0].conversion +"*"+ item.grnUom[1].conversion);
              item.receivedQtyCalculation=(item.grnReceivedQuantity * item.grnUom[0].conversion * item.grnUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            if($scope.isSkipPhysicalInspection && self.model.grnList.grnTypeId != 4){
              item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity * item.grnUom[0].conversion * item.grnUom[1].conversion);
            }
            else{
              item.receivedQtyCalculationSkipPI = ((item.grnReceivedQuantity) * item.grnUom[0].conversion * item.grnUom[1].conversion);
            }
          } else if (uomList.uomDetails == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            if(item.grnApprovedQuantity != undefined && item.grnApprovedQuantity != null){
              item.receivedQtyCon=(item.grnApprovedQuantity +"*"+ item.grnUom[1].conversion);
              item.receivedQtyCalculation=(item.grnApprovedQuantity * item.grnUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            else{
              item.receivedQtyCon=(item.grnReceivedQuantity +"*"+ item.grnUom[1].conversion);
              item.receivedQtyCalculation=(item.grnReceivedQuantity * item.grnUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            if($scope.isSkipPhysicalInspection && self.model.grnList.grnTypeId != 4){
              item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity * item.grnUom[1].conversion);
            }
            else{
              item.receivedQtyCalculationSkipPI = ((item.grnReceivedQuantity) * item.grnUom[1].conversion);
            }
          } else if (uomList.uomDetails == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            if(item.grnApprovedQuantity != undefined && item.grnApprovedQuantity != null){
              item.receivedQtyCon=(item.grnApprovedQuantity);
              item.receivedQtyCalculation = (item.grnApprovedQuantity);
              item.uomUnitData = uom[2].uomUnit; 
            }
            else{
              item.receivedQtyCon=(item.grnReceivedQuantity);
              item.receivedQtyCalculation = (item.grnReceivedQuantity);
              item.uomUnitData = uom[2].uomUnit;
            }
            if($scope.isSkipPhysicalInspection){
              item.receivedQtyCalculationSkipPI = ( item.grnReceivedQuantity);
            }
          }
        }
        if(item.bonusUomTypeId != undefined && item.bonusUomTypeId != null){
        	if(item.bonusUom == undefined || item.bonusUom == null || item.bonusUom.length == 0){
	        	  item.bonusUom = item.uom;
	          }
          if(item.isThreeUOMApplicable == false){
            if (item.bonusUomTypeId == 1) {
              if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
                item.bonusQtyCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion);
                item.bonusQtyCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion);
                item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
              }
              else{
                item.bonusQtyCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion);
                item.bonusQtyCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion);
                item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
              }
            } else if (item.bonusUomTypeId == 2) {
              
            } else if (item.bonusUomTypeId == 3) {
              if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
                item.bonusQtyCon=(item.bonusApprovedQuantity);
                item.bonusQtyCalculation = (item.bonusApprovedQuantity);
                item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
              }
              else{
                item.bonusQtyCon=(item.bonusReceivedQuantity);
                item.bonusQtyCalculation = (item.bonusReceivedQuantity);
                item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
              }
            }
          }
          else{
            if (item.bonusUomTypeId == 1) {
              if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
                item.bonusQtyCon=(item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion +"*"+ item.bonusUom[1].conversion);
                item.bonusQtyCalculation=(item.bonusApprovedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
              else{
                item.bonusQtyCon=(item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion +"*"+ item.bonusUom[1].conversion);
                item.bonusQtyCalculation=(item.bonusReceivedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
            } else if (item.bonusUomTypeId == 2) {
              if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
                item.bonusQtyCon=(item.bonusApprovedQuantity +"*"+ item.bonusUom[1].conversion);
                item.bonusQtyCalculation=(item.bonusApprovedQuantity * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
              else{
                item.bonusQtyCon=(item.bonusReceivedQuantity +"*"+ item.bonusUom[1].conversion);
                item.bonusQtyCalculation=(item.bonusReceivedQuantity * item.bonusUom[1].conversion);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
            } else if (item.bonusUomTypeId == 3) {
              if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
                item.bonusQtyCon=(item.bonusApprovedQuantity);
                item.bonusQtyCalculation = (item.bonusApprovedQuantity);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
              else{
                item.bonusQtyCon=(item.bonusReceivedQuantity);
                item.bonusQtyCalculation = (item.bonusReceivedQuantity);
                item.uomUnitDataBonus = uom[2].uomUnit;
              }
            }
          }
        }
        if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
          item.lcop = (((item.poAmount - item.totalDiscount) + item.taxAmount) / (parseInt(item.receivedQtyCalculationSkipPI) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
        }
        else{
          item.lcop = (((item.poAmount - item.totalDiscount) + item.taxAmount) / (parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
        }
      } else {
        // item.newPRQuantity = item.prQuantity;
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }

    var ViewApprovedStagedQuantity = function (item) {
      var poQty = [];
      item.forApproveStageQty.forEach(function(key){
        poQty.push({
          "id": key.id,
          "poStagedDate": key.poStagedDate,
          "stagedQuantity": key.stagedQuantity,
          "receivedQuantity": key.receivedQuantity,
          "approvedQuantity": self.model.grnList.approvalStatusId!=5 ? item.grnApprovedQuantity : (key.approvedQuantity || 0),
          "poApprovedQuantity": item.poQuantity
        });
      });
      
      self.model.approvedStageQuantity = poQty;
      self.model.selectedIndex = self.model.grnList.items.indexOf(item);
      if (!item.isSelected) {
        self.model.isSumVisible = false;
      } else {
        self.model.isSumVisible = true;
      }
    }

    var SumApprovedQty = function(x){
      var total = 0;
      var selectedItem = self.model.grnList.items[self.model.selectedIndex];
      angular.forEach(self.model.approvedStageQuantity, function (value, key) {
        if (!parseInt(value.approvedQuantity)) {
          value.approvedQuantity = 0
        }
        total = total + parseInt(value.approvedQuantity);
      });
      selectedItem.grnApprovedQuantity = total;
      if (ValidApprovedQty(selectedItem, selectedItem.grnApprovedQuantity)) {
        selectedItem.fillRejectQtyFlag = true;
        selectedItem.forApproveStageQty = self.model.approvedStageQuantity;
        if (selectedItem.grnApprovedQuantity >= selectedItem.grnReceivedQuantity) {
          selectedItem.rejectedQuantity = 0;
          selectedItem.isReject = false;
        } else{
          selectedItem.rejectedQuantity = selectedItem.grnReceivedQuantity - selectedItem.grnApprovedQuantity;
          selectedItem.isReject = true;
          RejectionPopup(selectedItem);
        }
        angular.forEach(self.model.approvedStageQuantity, function(data){
          data.approvedQuantity = selectedItem.grnApprovedQuantity;
        });

        self.model.ClosePopup('close', 'ApprovedStagedQuantity');
      }
      $scope.calculateRoundoff();
    }

    var ValidApprovedQty = function(item, qty){
      var isValid = true;

      if (self.model.grnTypeId == 4) {
        if (parseInt(qty) > parseInt(item.poQuantity)) {
          growl.error("Approved quantity should not greater than PO quantity");
          isValid = false;
          angular.forEach(self.model.approvedStageQuantity, function(data, key){
              // if(key == self.model.selectedIndex){
                data.approvedQuantity = item.approvedQuantity;
                item.grnApprovedQuantity = item.approvedQuantity;
              // }
          });
        }
      }

      return isValid;
    }

    var SaveApproval = function (item) {
      if (item.approvedQuantity > item.grnReceivedQuantity) {
        item.rejectedQuantity = 0;
        self.model.rejectQty = 0;
        item.isReject = false;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
        item.rejectedQuantity = item.grnReceivedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.statusId = setStatus('Rejected');
        RejectionPopup(item);
      } else if (item.approvedQuantity < item.grnReceivedQuantity) {
        item.rejectedQuantity = item.grnReceivedQuantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      } else if (item.approvedQuantity == item.grnReceivedQuantity) {
        item.rejectedQuantity = item.grnReceivedQuantity - item.approvedQuantity;
        item.isReject = false;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      }
    }

    $scope.calculateReceivedQty = function(item){
      var uomTypeId = item.uomDetails.split("-")[0];
      item.quantity = item.grnReceivedQuantity - item.rejectedQuantity;
			if(item.isThreeUOMApplicable == false){
				if(uomTypeId == "P "){
					item.receivedQtyCon = (item.quantity +"*"+ item.grnUom[0].conversion);
					item.receivedQtyCalculation = (item.quantity * item.grnUom[0].conversion);
          item.uomUnitData = item.uomListdata[2].uomUnit;
          if($scope.isSkipPhysicalInspection){
            item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity * item.grnUom[0].conversion);
          }
				}
				else if(uomTypeId == "S "){
					
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
          item.uomUnitData = item.uomListdata[2].uomUnit;
          if($scope.isSkipPhysicalInspection){
            item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity);
          }
				}
			}
			else{
				if(uomTypeId == "P "){
					item.receivedQtyCon = (item.quantity +"*"+ item.grnUom[0].conversion+"*"+ item.grnUom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.grnUom[0].conversion * item.grnUom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
          if($scope.isSkipPhysicalInspection){
            item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity * item.grnUom[0].conversion * item.grnUom[1].conversion);
          }
				}
				else if(uomTypeId == "S "){
					item.receivedQtyCon = (item.quantity +"*"+ item.grnUom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.grnUom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
          if($scope.isSkipPhysicalInspection){
            item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity * item.grnUom[1].conversion);
          }
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
          item.uomUnitData = item.uom[2].uomUnit;
          if($scope.isSkipPhysicalInspection){
            item.receivedQtyCalculationSkipPI = (item.grnReceivedQuantity);
          }
				}
			}
		}

    $scope.calculateBonusReceivedQty = function(item){
      var bonusUomTypeId = item.bonusUomDetails.split("-")[0];
			if(item.isThreeUOMApplicable == false){
				if(bonusUomTypeId == "P "){
					item.bonusQtyCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion);
					item.bonusQtyCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion);
          item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
				}
				else if(bonusUomTypeId == "S "){
					
				}
				else{
					item.bonusQtyCon = (item.bonusReceivedQuantity);
					item.bonusQtyCalculation = (item.bonusReceivedQuantity);
          item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
				}
			}
			else{
				if(bonusUomTypeId == "P "){
					item.bonusQtyCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
					item.bonusQtyCalculation = (item.bonusReceivedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
          item.uomUnitDataBonus = item.uom[2].uomUnit;
				}
				else if(bonusUomTypeId == "S "){
					item.bonusQtyCon = (item.bonusReceivedQuantity +"*"+ item.bonusUom[1].conversion);
					item.bonusQtyCalculation = (item.bonusReceivedQuantity * item.bonusUom[1].conversion);
          item.uomUnitDataBonus = item.uom[2].uomUnit;
				}
				else{
					item.bonusQtyCon = (item.bonusReceivedQuantity);
					item.bonusQtyCalculation = (item.bonusReceivedQuantity);
          item.uomUnitDataBonus = item.uom[2].uomUnit;
				}
			}
      if(self.model.disAmount == undefined || self.model.disAmount == null){
        self.model.disAmount = 0;
      }
      if($scope.isSkipPhysicalInspection && (item.receivedQtyCalculationSkipPI != null && item.receivedQtyCalculationSkipPI != undefined)){
        item.lcop = (((item.poAmount - item.totalDiscount) + (item.taxAmount + item.otherChargesAmount)) / (parseInt(item.receivedQtyCalculationSkipPI) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
      }
      else{
        item.lcop = (((item.poAmount - item.totalDiscount) + (item.taxAmount + item.otherChargesAmount)) / (parseInt(item.receivedQtyCalculation) + parseInt(item.bonusQtyCalculation!=undefined && item.bonusQtyCalculation!=null ? item.bonusQtyCalculation:0)));
      }
		}

    var SaveRejection = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        var rejectQty = parseInt(self.model.rejectQty);
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
            if (self.model.grnTypeId == 1) {
              if (rejectQty > item.grnReceivedQuantity) {
                growl.error("Rejection qauntity must be less than received quantity");
                // growl.error('Please Enter Valid Quantity');
                return false;
              } else if (rejectQty == item.grnReceivedQuantity) {
                item.rejectedQuantity = item.grnReceivedQuantity;
                item.approvedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Rejected');
              } else if (rejectQty == 0 || !rejectQty) {
                item.approvedQuantity = item.grnReceivedQuantity;
                item.rejectedQuantity = 0;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              } else {
                item.rejectedQuantity = rejectQty;
                item.rejectNote = self.model.rejectNote;
                item.approvedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              }
            } else if (self.model.grnTypeId == 4) {
              item.fillRejectQtyFlag = true;
              if (rejectQty > item.grnReceivedQuantity) {
                growl.error('Rejection quantity must be less than received quantity');
                // growl.error('Please Enter Valid Quantity');
                return false;
              } else if (rejectQty == item.grnReceivedQuantity) {
                item.rejectedQuantity = item.grnReceivedQuantity;
                item.grnApprovedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.approvedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.statusId = setStatus('Rejected');
              } else if (rejectQty == 0 || !rejectQty) {
                item.grnApprovedQuantity = item.grnReceivedQuantity;
                item.rejectedQuantity = 0;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              } else {
                item.rejectedQuantity = rejectQty;
                item.rejectNote = self.model.rejectNote;
                item.grnApprovedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.approvedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              }
            }
            if(item.rejectedQuantity == 0){
              item.isReject = false;
            }

            self.model.rejectQty = undefined;
            self.model.rejectNote = undefined;
            if(self.model.assetTypeId == 1){
              $scope.calculateReceivedQty(item);
            }
            ModalDismiss('qtyaccepatReject');
            discountCalc(item);
            $scope.calculateRoundoff();
          }
        });
      }
    }

    $scope.roundoffCalculation = function(){
      self.model.totalNetAmount = 0;
      angular.forEach(self.model.grnList.items, function(val){
        self.model.totalNetAmount += val.netAmount;
      });
      if($scope.roundOff < 0){
        var roundoff = $scope.roundOff.split("-");
        self.model.totalNetAmount = self.model.totalNetAmount - parseFloat(roundoff[1]);
        self.model.totalNetAmount = self.model.totalNetAmount.toFixed(2);
      } 
      if($scope.roundOff > 0){
        self.model.totalNetAmount = self.model.totalNetAmount + parseFloat($scope.roundOff);
        self.model.totalNetAmount = self.model.totalNetAmount.toFixed(2);
      }
      if($scope.roundOff == 0){
        self.model.totalNetAmount = 0;
        angular.forEach(self.model.grnList.items, function(val){
          self.model.totalNetAmount += val.netAmount;
        });
      }
    }
    $scope.saveBtnDisabled=false;
    var SaveGRNApproval = function () {
    	
    	if(self.model.totalNetAmount == undefined || self.model.totalNetAmount == null || self.model.totalNetAmount < 0 ){
    		  growl.error('Please check Credit Note Amount ');
    	       return false;
    	}
    	
    	
    	 $scope.saveBtn=true;
         $scope.saveBtnDisabled=true;
        if (ValidateApproval()) {
        	
        	ActaulSaveGRNApproval();
        }
        else
        	{
        	 $scope.saveBtn= false;
        	}
    }
    
    var ActaulSaveGRNApproval = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
    	 $scope.saveBtn=true;
         $scope.saveBtnDisabled=true;
    	 var count=0;
    	 self.model.grnList.items.forEach(function (item) {
     		  if(item.isSelected)
     			  {
     			  count++;
     			  }
     	  });
     	  if(count!=self.model.grnList.items.length)
       	{
       	growl.error("Please accept / reject all items");
       	$scope.saveBtn=false;
        $scope.saveBtnDisabled=false;
       	return false;
       	}
    	var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.APPROVE_API + commonDetailService.getDataId();        
        var data = {
          "items": [],
          "grnOtherChargesMapper": [],
          "totalNetAmount": parseFloat(self.model.totalNetAmount).toFixed(2),
          "roundOffAmount": (!isNaN($scope.roundOff) && $scope.roundOff != undefined && $scope.roundOff != null && $scope.roundOff != "") ? $scope.roundOff : 0,
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var approvedStageQuantity = [];
          item.forApproveStageQty.forEach(function(key){
            approvedStageQuantity.push({
              "id": key.id,
              "approvedQuantity": parseInt(key.approvedQuantity)
            });
          });
          
          data.grnOtherChargesMapper = self.model.grnList.grnOtherChargesMapper;
          item.grnBonusBatchExpiryData.forEach(function (item) {
              delete item.origninalBonusQty;
          });
          var newItem = {
            "id": item.id,
            //"bonusApprovedQuantity": parseInt(item.bonusApprovedQuantity),
            "bonusApprovedQuantity": parseInt(item.bonusReceivedQuantity !=null ? item.bonusReceivedQuantity :item.bonusApprovedQty),
            "rejectedQuantity": parseInt(item.rejectedQuantity),
            "rejectReasonId": self.model.rejectReason ? self.model.rejectReason.id : 1,
            "rejectNote": self.model.rejectNote,
            "statusId": item.statusId,
            "totalOtherAmount": (item.otherchargesAmount).toFixed(2),
            "cop": item.cop,
            "mrp": item.mrp,
            "discount": item.discount,
            "taxId": item.selectedTax.taxId,
            "taxPercentage": parseFloat(item.selectedTaxPercent),
            "taxAmount": (parseFloat(item.taxAmount)).toFixed(2),
            "totalAmount": (parseFloat(item.totalAmount)).toFixed(2),
            "totalDiscount": (parseFloat(item.totalDiscount)).toFixed(2),
            "netAmount": (parseFloat(item.netAmount)).toFixed(2),
            "grnBonusBatchExpiry":item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null ? item.grnBonusBatchExpiryData:[]
          }

          if (self.model.grnTypeId == 4) {
            newItem.grnApprovedQuantity = parseInt(item.grnApprovedQuantity);
            newItem.grnStagedQuantity = approvedStageQuantity;
          } else if (self.model.grnTypeId == 1) {
            newItem.grnApprovedQuantity = parseInt(item.approvedQuantity);
          }

          data.items.push(newItem);

          var statusHash = {};
          data.statusId = null;
          data.approvalStatusId = null;
          
          data.userId= cookieObject.userId,
          data.organizationId =cookieObject.organizationId,
          data.unitId=cookieObject.unitId
          data.deliveryStoreId =self.model.grnList.storeId          
          data.items.forEach(function(rec) {
            var statusId = rec.statusId;
            statusHash[statusId] = true;
          });
          
          var statuses = Object.keys(statusHash);
          if(statuses.length > 1) {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Approved');
          } else if(statuses[0] == 5) {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Approved');
          } else if(statuses[0] == 7) {
            data.statusId = setStatus('Closed');
            data.approvalStatusId = setStatus('Rejected');
          } else {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Approved');
          }

        });

        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            $scope.saveBtn=true;
            growl.success(response.data.message);
            NavigateToBack();
          }, function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
            $scope.saveBtn=false;
            $scope.saveBtnDisabled=false;
          });
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    function setStatus(key) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == key;
      });

      return status.id;
    }

    var isDisable = function (item) {
      if (item.status !== "Pending Approval") {
        return true;
      }
      return false;
    }
    $scope.saveBtn = false;

    var SelectAllItems = function () {
    	$scope.saveBtn=false;
      if (self.model.selectAllRows) {
        self.model.SelectedItems = [];
        self.model.grnList.items.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            item.approvedQuantity = item.grnReceivedQuantity;
            item.bonusApprovedQty = item.bonusApprovedQuantity;
            item.poAmount = (item.approvedQuantity * item.cop).toFixed(2);
            // item.totalDiscount = ((item.poAmount) * (item.discount/100)).toFixed(2);
            item.taxAmount = ((item.poAmount-item.totalDiscount) * (item.taxPercentage/100));
            item.netAmount = (item.poAmount - item.totalDiscount) + item.taxAmount + item.totalOtherAmount;
            item.statusId = 5;
            self.model.SelectedItems.push(item);

            if (self.model.grnTypeId == 4) {
              item.grnApprovedQuantity = item.grnReceivedQuantity;
              item.rejectedQuantity = 0;
            } else if (self.model.grnTypeId == 1) {
              item.approvedQuantity = item.grnReceivedQuantity;
              item.rejectedQuantity = 0;
            }

            item.grnStagedQuantity.forEach(function(key){
              key.approvedQuantity = key.receivedQuantity;
            });
            $scope.calculateRoundoff();
            $scope.roundOff = $scope.roundAmount;
            self.model.totalNetAmount = $scope.netAmt-self.model.grnList.totalCreditNoteAmount;
          }
        });
      } else {
        self.model.SelectedItems = [];
        self.model.grnList.items.forEach(function (item) {
          item.isSelected = false;
          item.isDisabled = true;
          item.bonusApprovedQty = 0;
          item.statusId = 10;
          item.poAmount = 0;
          item.taxAmount = 0;
          item.netAmount = 0;
          if (self.model.grnTypeId == 4) {
            item.grnApprovedQuantity = 0;
          } else if (self.model.grnTypeId == 1) {
            item.approvedQuantity = 0;
          }

          item.grnStagedQuantity.forEach(function(key){
            key.approvedQuantity = null;
          });
        });
        $scope.roundOff = 0;
        $scope.calculateRoundoff();
      }
    }
  
    var SelectItem = function (item) {
      var items = self.model.SelectedItems;
      $scope.saveBtn=false;

      if (item.isSelected) {
        items.push(item);
        item.isDisabled = false;
        item.approvedQuantity = item.grnReceivedQuantity;
        item.poAmount = (item.approvedQuantity * item.cop).toFixed(2);
        item.taxAmount = ((item.poAmount-item.totalDiscount) * (item.taxPercentage/100));
        item.netAmount = ((item.poAmount - item.totalDiscount) + item.taxAmount + item.totalOtherAmount);
        item.bonusApprovedQty = item.bonusReceivedQuantity;
        item.fillRejectQtyFlag = true;

        if (self.model.grnTypeId == 4) {
          item.grnApprovedQuantity = item.grnReceivedQuantity;
          item.rejectedQuantity = 0;
        } else if (self.model.grnTypeId == 1) {
          item.approvedQuantity = item.grnReceivedQuantity;
        }

        item.grnStagedQuantity.forEach(function(key){
          key.approvedQuantity = key.receivedQuantity;
        });
        item.statusId = 5;
        $scope.calculateRoundoff();  
        $scope.roundOff = $scope.roundAmount;
        self.model.totalNetAmount = $scope.netAmt;
        if (self.model.grnList.items.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
        if(items.length!=self.model.grnList.items.length)
    	  {
    	    growl.error("Please accept / reject all items");
    	    return false;
    	  }
      } 
      else 
      {
        item.rejectedQuantity = 0;
        item.isDisabled = true;
        item.isReject = false;
        item.bonusApprovedQuantity = 0;
        item.statusId = 10;
        item.poAmount = 0;
        item.taxAmount = 0;
        item.netAmount = 0;
        item.discountAmount = 0;
        if (self.model.grnTypeId == 4) {
          item.grnApprovedQuantity = 0;
          item.rejectedQuantity = 0;
        } else if (self.model.grnTypeId == 1) {
          item.approvedQuantity = 0;
        }

        item.grnStagedQuantity.forEach(function(key){
          key.approvedQuantity = null;
        });

        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
        var count=0;
  	    items.forEach(function (item) {
  		    if(item.isSelected)
          {
  			    count++;
  			  }
  	    });
        $scope.roundOff = 0;
        $scope.calculateRoundoff();  
        if (self.model.grnList.items.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
  	    if(count!=self.model.grnList.items.length)
    	  {
    	    growl.error("Please accept / reject all items");
    	    return false;
    	  }
      }
    }

    var NavigateToBack = function () {
      $state.go('GRNApproval');
    }

    var RejectionPopup = function (item) {
    	if(item.isReject)
    		{
    		item.approvedQuantity=item.grnReceivedQuantity;
    		self.model.rejectQty=0;
    		}
    	if(!item.isReject)
		{
		item.approvedQuantity=item.grnReceivedQuantity;
		item.rejectedQuantity=0;
		}
      self.model.itemTrueFalse = item;
      if (item.isReject) {
        itemId = item.id;
        self.model.rejectQty = item.rejectedQuantity;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        self.model.rejectReason = self.model.reasons[0];
        // item.fillRejectQtyFlag = false;
      } else {
        CancelRejection(item);
      }
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.requestItems.find(function (prevItem) {
        prevItem.approvedQuantity = item.approvedQuantity;
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectedQuantity = prevRequest.rejectedQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        if( item.rejectedQuantity!=null &&  item.rejectedQuantity >0)
        {
             item.statusId = prevRequest.statusId;
        }
       // item.statusId = prevRequest.statusId;
        if (self.model.grnTypeId == 4) {
          item.grnApprovedQuantity = prevRequest.approvedQuantity;
        } else if (self.model.grnTypeId == 1) {
          item.approvedQuantity = prevRequest.approvedQuantity;
        }
        item.fillRejectQtyFlag = true;
        if(self.model.assetTypeId == 1){
        	self.model.otherChargesAmount=self.model.otherChargesTotal;
          $scope.calculateReceivedQty(item);
          discountCalc(item);
        }
        $scope.calculateRoundoff();
        calculateOtherChargesAmountForItem();
      }
    }
    var ModalDismissOnClose = function(popupName){
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      var id = itemId;
      self.model.grnList.items.forEach(function(data){
        if(data.isSelected == true && id == data.id){
          data.isReject = false;
          // var approvedQuantity = parseInt(data.approvedQuantity); 
          // data.approvedQuantity = data.rejectedQuantity + approvedQuantity;
          data.rejectedQuantity = 0;
          self.model.rejectQty=0;
          self.model.SaveRejection();
        }
        if(self.model.assetTypeId == 1){
          $scope.calculateReceivedQty(data);
        }
      });
    }
    
    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Please select items to update and save');
        isValid = false;
      }

      self.model.SelectedItems.forEach(function(data){
        if (!data.fillRejectQtyFlag) {
          growl.error('Please fill approved quantity');
          isValid = false;
        }
        
        if (self.model.grnTypeId == 4) {
          if (data.grnApprovedQuantity > data.poQuantity) {
            growl.error("Approved quantity should not greater than PO quantity");
            isValid = false;
          }
        }
        if (!parseFloat(data.cop)) {
        	growl.error("Please enter cost of purchase");
           isValid = false;
         }
      })
      
      $scope.saveBtn = true;
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please select reason to reject');
      }

      return isValid;
    }

    var ViewOtherCharges = function (item){
      self.model.otherCharges = item.grnOtherChargesMapper;
    }

    var ViewGRNUom = function (item) {
      self.model.GRNUoms = [];
      self.model.grnList.items.find(function(key){
        if (item.id == key.id) {
          if(key.isThreeUOMApplicable == false){
            key.grnUom.find(function(d){
              if(d.uomTypeId != 2){
                self.model.GRNUoms.push(d);      
              }
            })
          }
          else{
            self.model.GRNUoms = key.grnUom;
          }
        }
      });
    }

    var ViewBonusUom = function (item) {
      self.model.bonusUoms = [];
      self.model.grnList.items.find(function(key){
        if (item.id == key.id) {
          if(key.isThreeUOMApplicable == false){
            key.bonusUom.find(function(d){
              if(d.uomTypeId != 2){
                self.model.bonusUoms.push(d);      
              }
            })
          }
          else{
            self.model.bonusUoms = key.bonusUom;
          }
        }
      });
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.rejectNote = undefined;
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

    var ViewItems = function (item) {
      self.model.grnList.items.find(function(key){
        if (item.id == key.id) {
          self.model.getAccessoryList = key.grnAccessoriesMapper;
        }
      });
    }

    var OpenPopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
    }

    var ClosePopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('hide');
    }

    var ViewLastGrnDetails = function (item) {
     // var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GRN_HISTORY + item.vendorId + "/" + item.items[0].itemId+"/"+item.id;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.PRE_GRN_HISTORY + item.vendorId + "/" + item.items[0].itemId;
      
      $rootScope.stopLoaderPopup();
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

    
    //doccument upload code
    
	$scope.addImageDetails = function()
	{ 
		var encounterDetails = angular.copy($scope.encounterDetails);
		$scope.encounterDetailsList.push(encounterDetails);
	};

  $scope.removeImageDetails = function(deleteIndex)
	{ 
	   $scope.encounterDetailsList.splice(deleteIndex, 1);
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
	
	$scope.getPatientDocumentList = function(obj)
     {
      
      $rootScope.startLoader();
      var data =
        {
        "patientId":obj.patientId,
        "unitId":$scope.unitId,
        "organisationId":$scope.organizationId,
        "userId":undefined,
        "visitTypeId":undefined,
        "admissionId":undefined,
        "grnId": $scope.grnId,
       }
      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.patientDocumentList = [];
        if (response.data.status == "success") 
        {
        	$scope.patientDocumentList = response.data.listObject;  
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
	 $scope.showDocUPloadPoup   = function()
		{
		 
		 $scope.grnId = commonDetailService.getDataId();
		 $scope.encounterDetails={};
			 $('#fileId').val(undefined);
			$scope.encounterDetailsList=[];
			$scope.encounterDetails.patientId = 0;	
			$scope.encounterDetails.visitTypeId = 1;	
			$scope.encounterDetails.createdBy = $scope.userId;
			$scope.encounterDetails.updatedBy = $scope.userId;
			$scope.encounterDetails.unitId = $scope.unitId;
			$scope.encounterDetails.organisationId = $scope.organizationId;
			$scope.encounterDetails.uploadDoccumentDate=new Date();
			if($scope.encounterDetailsList.length>0)
			{
			  $('#uploadDetails').modal('show');
			}
		else{
			 $('#uploadDetails').modal('show');
			var encounterDetails = angular.copy($scope.encounterDetails);
			$scope.encounterDetailsList.push(encounterDetails);
		}
			$scope.getPatientDocumentList(encounterDetails);
		}
	 
	 $scope.uploadEncounterDoc = function(Obj)
		{
		 
		 $scope.grnId = commonDetailService.getDataId();
		 
			 var fd = new FormData();
		        fd.append('encounterId', 0);
		        fd.append('visitTypeId', Obj.visitTypeId);
		        fd.append('admissionId', 0);
		        fd.append('patientId',Obj.patientId );
		        fd.append('documentTypeId', Obj.documentTypeId);
		        fd.append('createdBy',Obj.createdBy );
		        fd.append('updatedBy', Obj.updatedBy);
		        fd.append('unitId', Obj.unitId);
		        fd.append('organisationId', Obj.organisationId);
		        fd.append('uploadDoccumentDate', moment(Obj.uploadDoccumentDate).format('DD-MM-YYYY HH:mm:ss'));
		        fd.append('file',Obj.file );
		        fd.append('expenseId', 0);
		        fd.append('grnId', $scope.grnId);
                fd.append('poId',0);
		        fd.append('incidentReportingMapperId',0);
		        fd.append('companyExpenseId',0);
		        fd.append('createdDate',null);
				fd.append('fileEditedName',null);
				fd.append('opbId', 0);
				fd.append('vendorId',0);
				var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
		        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
				{	
					return response;
				});
//		 
//		 var fd = new FormData();
//	        fd.append('encounterId', 0);
//	        fd.append('visitTypeId', Obj.visitTypeId);
//	        fd.append('admissionId', 0);
//	        fd.append('patientId',0);
//	        fd.append('documentTypeId', Obj.documentTypeId);
//	        fd.append('createdBy',Obj.createdBy );
//	        fd.append('updatedBy', Obj.updatedBy);
//	        fd.append('unitId', Obj.unitId);
//	        fd.append('organisationId', Obj.organisationId);
//	        fd.append('uploadDoccumentDate', moment(Obj.uploadDoccumentDate).format('DD-MM-YYYY HH:mm:ss'));
//	        fd.append('file',Obj.file );
// fd.append('expenseId', 0);
// fd.append('grnId', $scope.grnId);
// fd.append('poId',0);
// fd.append('incidentReportingMapperId',0);
// fd.append('companyExpenseId',0);
// fd.append('createdDate',null);
//			  fd.append('fileEditedName',null);
// fd.append('opbId',0);
// fd.append('vendorId',0);
//	        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
//	        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
//			{	
//				return response;
//			});
		}
	 
	 
	 $scope.uploadPatientImage = function()
	    {
		  $scope.finalDocUploadList=[];
		  var count=0;
		  $scope.forView = false;
		 angular.forEach($scope.encounterDetailsList, function(value, key) {  
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
				 var ext = value.file.name.split(".").pop().toLowerCase();
					if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
						growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
								{
									title : 'warning!'
								});
						$rootScope.stopLoader();
						return false;
					}
					$scope.finalDocUploadList.push(value);
					 //console.log("final list",$scope.finalDoc);
		});
		 
		 if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
			   $('#uploadDetails').modal('hide');
		
		 $rootScope.startLoader();
		  $scope.count = 0;
		 angular.forEach($scope.encounterDetailsList, function(value, key) 
			{
				var promise = $scope.uploadEncounterDoc(value);
				promise.then(function(response) 
						{
						  if(response.data.status == "success")
							  {
							    $scope.count++;
							    if($scope.encounterDetailsList.length==$scope.count)
								 {
								   growl.success("Document Uploaded Successfully.",
											{
												title : 'Success!'
											});
								   $scope.finalDocUploadList=[];
								   $scope.encounterDetailsList = [];
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
	 
    var rePrintBillSlip = function (grn,storeId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_GRN.rpt",
        "reportFormat": "PDF",
        "exportFileName": "Grn",
        "reportParams": [
          {
            "paramName": "Id",
            "paramType": "Integer",
            "paramValue": grn.id
          },
          {
            "paramName": "Storeid",
            "paramType": "Integer",
            "paramValue": storeId
          }
        ]
      };
      reportService.generate(REPORT_API_URI1, data);
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsGRNApproval", {
      url: "/detailsGRNApproval",
      templateUrl: 'views/procurement/quotation/details-GRN-approval.html',
      controller: "DetailsGRNApproval.Controller",
      controllerAs: "vm",
      title: 'Good Receipt Note Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsGRNApproval.Controller", detailsGRNApprovalController);
})();