/**
 * 
 */

angular.module('myApp')
		.controller("QMSMasterTokenCallController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','$cookieStore','$window','$stateParams',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,$cookieStore,$window,$stateParams){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.initQMSMaster = function() {
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				console.log("cookies Object",cookieObject);
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.hospitalName = cookieObject.unitNme;
		
				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				$rootScope.loginpage = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup
				$scope.kioskName="";
				$scope.tokenNumber="";
				$scope.common = {
						qmsKioskMasterId:"",
						qmsDeptName:"",
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId					
					};
				
			}
			$scope.initQMSMaster();
			
			//setting kioskId in cookies start 
			$scope.SetCookies = function () {
                $cookies.put("kioskIdForTokenGen", $scope.common.qmsKioskMasterId);
            };
            $scope.getCookie = function(key) {
                return $cookieStore.get(key);
            };
            $scope.cookieskioskId=$scope.getCookie("kioskIdForTokenGen");
            console.log("cookies Id",$scope.cookieskioskId);
          //setting kioskId in cookies end 
            
			$scope.getQMSMasterList = function() {				
				var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId						
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveQMSMasterList";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
//					angular.element('#qmsKioskDetailsModal').modal('show');
					$scope.activeQMSList = response.data.listObject;
				});
			}
			$scope.getQMSMasterList();					
			$scope.selectQMSKiosk=function(kioskId){
				//setting cookies values
				$scope.SetCookies();
				$scope.confirmQMS();
				console.log("kioskId",kioskId);
			}
			
			$scope.confirmQMS=function(){
					console.log("kiosk Id",$scope.common.qmsKioskMasterId);
					if($scope.common.qmsKioskMasterId!="" ||$scope.common.qmsKioskMasterId!=undefined){			
					var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId	,
							qmsKioskMasterId:$scope.common.qmsKioskMasterId
							};
			
						var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveDeptListByKioskId";
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.activeQMSDeptList = response.data.listObject;
							$scope.kioskName=response.data.listObject[0].qmsDesc;
							 window.open(this.BASE_URL + ROOT_URL + "/#/QMS" ); //, "width=2002,height=1002"
							//$state.go('QMS',{});
						});



						}
				
					}



			
		
}]);