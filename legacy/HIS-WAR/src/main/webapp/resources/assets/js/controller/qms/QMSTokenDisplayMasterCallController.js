/**
 * 
 */

angular.module('myApp')
		.controller("QMSTokenDisplayMasterCallController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','$interval','Pubnub','$cookieStore','$location',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,$interval,Pubnub,$cookieStore,$location){
				
	
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
				
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;

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
			$scope.getQMSMasterList = function() {				
				var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId						
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveQMSMasterList";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$scope.activeQMSList = response.data.listObject;
					angular.element('#qmsKioskDetailsModal').modal('show');
				});
			}
			$scope.getQMSMasterList();

           $scope.firstImg;
           $scope.imageBackground = function(){
                           var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.orgId+"/3"
                               GenericService.serviceAction("GET", URI).then(function(response) {
                            	   if(response.data.object != null){
                               $scope.firstImg=response.data.object.imageURL;
                            	   }
                               
                               });
                           }
                       // $scope.imageBackground();
			
			//setting kioskId in cookies start 
			$scope.SetCookies = function () {
                $cookies.put("kioskIdForDisplay", $scope.common.qmsKioskMasterId);
            };
            $scope.getCookie = function(key) {
                return $cookieStore.get(key);
            };
            $scope.cookieskioskId=$scope.getCookie("kioskIdForDisplay");
            console.log("cookies Id",$scope.cookieskioskId);
          //setting kioskId in cookies end 
            
			$scope.selectQMSKiosk=function(kioskId){
				//setting cookies values
				$scope.SetCookies();
				console.log("kioskId",kioskId);
			}
			
			$scope.deptList = [];
			
			$scope.confirmQMS=function(){
				
				 window.open(BASE_URL + ROOT_URL + "/#/display/"+$scope.orgId+"/"+$scope.orgUnitId+"/"+$scope.common.qmsKioskMasterId+"/"+$scope.userId );
				
				
			}




		
			
}]);