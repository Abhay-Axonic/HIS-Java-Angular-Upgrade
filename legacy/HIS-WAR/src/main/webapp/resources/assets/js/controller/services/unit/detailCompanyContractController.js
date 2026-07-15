angular.module("myApp").controller("detailCompanyContractController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','$stateParams','GenericService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,$stateParams,GenericService){
		$rootScope.loginpage = true;
		
		
		var cookieObject = $cookies.getObject('cookieObject');
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		
		$scope.unitId = cookieObject.unitId;
		$scope.orgId= cookieObject.organizationId;
		$scope.organizationId =cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.isSmartCardRequired = cookieObjectAppConfig.isSmartCardRequired;
		
		$scope.contractObj = $stateParams.companyContractObj;
	
		if (angular.equals({},$stateParams.companyContractObj)) {
			$state.go('companyContractMaster');
		}
		console.log("companyContractObj:",$scope.contractObj);
		
		$scope.firstTabLevel = 1;
		
		$scope.getCompanyContractDetail=function(){
			$rootScope.startLoader();
		if($scope.contractObj.contractId!=null && $scope.contractObj.contractId!=undefined){
			var data = {
	                   orgId : $scope.organizationId,
	                   unitId: $scope.unitId,
	                   contractId:$scope.contractObj.contractId
	         }
			var URI = BASE_URL + ROOT_URL+ "/unit/CompanyContract/getCompanyContractDetailByContractId";
		GenericService.serviceAction("POST", URI, data).then(function(response) {
					if (response.data.status == "success") {
						$scope.contractServiceList = [];
						$scope.contractServiceList = response.data.object.listTContractServiceDetailsDto;
						$scope.contractGroupExclusionDetails = response.data.object.listContractGroupExclusion;
						$scope.contractGroupWiseDetails = response.data.object.listContractGroupWiseDetails;
						$scope.contractBedCategoryList = response.data.object.listTContractBedCategoryDetailDto;
						$scope.contractProcedureList=response.data.object.procedureDetailsDtosList;
						$scope.contractCapList=response.data.object.listTContractCapDetailsDto;
						$scope.contractProductCapList=response.data.object.contractCapProductList;
						$scope.groupPharmacyExclusionDetailsList=response.data.object.groupPharmacyExclusionDetailsList;
						$scope.itemList=response.data.object.itemList;
						$scope.packageList=response.data.object.listTContractPackageDetailsDto
						$scope.capServiceWiseDetailsListForDetails = response.data.object.listContractServiceWiseCapDetails;
						$rootScope.stopLoader();
					}
				});
		  }
		else{
			$rootScope.stopLoader();
		}
		}
		$scope.getCompanyContractDetail();
		
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};

	}]);