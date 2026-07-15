/**
 * 
 */

angular.module('myApp')
		.controller("consultantTypeMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.LISDynamicLabel = "Consultant Type";
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.orgId = 1;
			$scope.orgUnitId = 1;
			$scope.createdBy =1 ;
			$scope.updatedBy =1 ;
			
			
			$scope.initMaster = function(){
				
				$scope.common = {
					id:"",
					code:"",
					desc:""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			// code for display ing name on top side bar for common page only start
				$(".selectedPageName").text(" Consultant Type  ");
				$state.current.title="Consultant Type";
				$(".sidebar-nav li").each(function() {
					$(this).find("a").parents("li").removeClass("active");
				});
				$('a[href$="consultantType"]').parents("li").addClass("active");
			// code for display ing name on top side bar for common page only End

}]);