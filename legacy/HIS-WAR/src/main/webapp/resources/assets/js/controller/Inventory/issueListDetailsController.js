(function () {
    "use strict";

    function issueListDetailsController($scope, $rootScope,$cookies, $http, $state,CONSTANTS, commonDetailService, GenericService, $filter, growl) {
        var self = this;
        var selectItemList = [];
        var masterId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

                requestData: [],
      

            };
            self.model.fromIssueList = fromIssueList;
            fromIssueList();
        }


        function fromIssueList() { 
             
            var issueId = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_ISSUE_LIST.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + issueId;
            var data = {};
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, data).then(
              function (response) {
                // self.model.storeData = materialPickerService.passMaterialPickerId(id);
                // self.model.detailsStoreIndenting = response.data.items;
                self.model.issueListDetail = response.data;
                
                self.model.issueListDetail.items.forEach(function (item) {
                	if(item.uomTypeId!=undefined && item.uomTypeId!=null)
                		{
                    item.isDisabled = true;
                    if (item.rejectQuantity)
                      item.isReject = true;
                    
                    item.selectedUom = item.uom.find(function (uom) {
                        return item.uomTypeId == uom.uomTypeId;
                      });
                      item.uomType = item.selectedUom.uomDetails;
                      if(item.isThreeUOMApplicable == false){
                          item.uomListdata = item.uom;
                          item.uom = [];
                          angular.forEach(item.uomListdata, function(data){
                            if(data.uomType != 'Stock'){
                              item.uom.push(data);
                            }
                          });
                        }
                      
                      var uomdata = item.selectedUom.uomDetails.split(' ')[0];
                      if(item.isThreeUOMApplicable == false){
                          if(uomdata == 'P'){
                            item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomListdata[0].conversion);
                            item.receivedQtyCalculation = (item.issueQuantity * item.uomListdata[0].conversion);
                            item.uomUnitData = item.uomListdata[2].uomUnit;
                          }
                          else if(uomdata == 'S'){
                        	  item.receivedQtyCon = (item.issueQuantity +"*"+ (item.uomListdata[1]!=undefined && item.uomListdata[1]!=null ? item.uomListdata[1].conversion:1));
                              item.receivedQtyCalculation = (item.issueQuantity * (item.uomListdata[1]!=undefined && item.uomListdata[1]!=null ? item.uomListdata[1].conversion:1));
                              item.uomUnitData = (item.uomListdata[1]!=undefined && item.uomListdata[1]!=null ? item.uomListdata[1].uomUnit:'');
                          }
                          else{
                            item.receivedQtyCon = (item.issueQuantity);
                            item.receivedQtyCalculation = (item.issueQuantity);
                            item.uomUnitData = item.uomListdata[2].uomUnit;
                          }
                        } 
                        else{
                          if(uomdata == 'P'){
                            item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                          }
                          else if(uomdata == 'S'){
                            item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.issueQuantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                          }
                          else{
                            item.receivedQtyCon = (item.issueQuantity);
                            item.receivedQtyCalculation = (item.issueQuantity);
                            item.uomUnitData = item.uom[2].uomUnit;
                          }
                        }
                }
                  });
                
                $rootScope.stopLoader();
 
              },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
          }
       
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsIssueList", {
            url: '/detailsIssueList',
            templateUrl: 'views/PharmacyInventory/InPatient/details-issue-list.html',
            controller: "IssueListDetailsMaterialIssue.Controller",
            controllerAs: 'vm',
            title: 'Material Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("IssueListDetailsMaterialIssue.Controller", issueListDetailsController)
})(); 