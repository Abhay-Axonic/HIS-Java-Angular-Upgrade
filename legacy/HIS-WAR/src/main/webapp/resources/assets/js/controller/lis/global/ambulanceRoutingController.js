/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceRoutingController",
		[ '$scope',
			'$http',
			'$localStorage',
			'$sessionStorage',
			'$cookies',
			'$rootScope',
			'GenericService',
			'growl',
			'$state',
			'PagerService',
			function($scope, $http, $localStorage, $sessionStorage,
					$cookies, $rootScope, GenericService, growl,
					$state, PagerService) {
			
					$scope.init = function() {
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
		
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.searchKeyword;
								
								$rootScope.loginpage = true;
		
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
							
								var HospitalLocation ={	lat:18.571240,lng:73.785280 };
								var OriginLocation ={ lat:18.566780,lng:73.918533 };
								var directionDisplay = new google.maps.DirectionsRenderer();
								var directionService = new google.maps.DirectionsService();

								//Initialize Hospital Location
								//function initializeDestination();
								//function initializeOrigin();
								// getDirections();

								/*var dest = new google.maps.LatLng(HospitalLocation.lat,HospitalLocation.lng);
								var ori = new google.maps.LatLng(OriginLocation.lat,OriginLocation.lng);
								
								var mapProperties = {zoom:15,center:dest};
								var map = new google.maps.Map(document.getElementById('map-canvas'),mapProperties);
								
								directionDisplay.setMap(map);

								var request = {
									origin: ori,
									destination: dest,
									travelMode:'DRIVING'
								};
								
								directionService.route(request,function(result,status){
									if(status=='OK'){
										directionDisplay.setDirections(result);
									}
								});*/
								//$scope.getDirectionsBtn = function() {}	
					}
					$scope.init();
					
					$scope.getDirections = function() {
						var dest = new google.maps.LatLng(HospitalLocation.lat,HospitalLocation.lng);
						var ori = new google.maps.LatLng(OriginLocation.lat,OriginLocation.lng);
						
						var mapProperties = {zoom:15,center:dest};
						var map = null;
						map = new google.maps.Map(document.getElementById('map-canvas'),mapProperties);
						
						directionDisplay.setMap(map);

						var request = {
							origin: ori,
							destination: dest,
							travelMode:'DRIVING'
						};
						
						directionService.route(request,function(result,status){
							if(status=='OK'){
								directionDisplay.setDirections(result);
							}
							
						});
					}
					
					
					/* code for setting Label */
					$(".selectedPageName").text("Ambulance Directing");
				} ]);