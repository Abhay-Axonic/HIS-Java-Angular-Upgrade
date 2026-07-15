angular.module('myApp')
		.controller("accessControlController",
		['$scope','$rootScope',
		function($scope,$rootScope){
			$rootScope.loginpage = true;
			//alert('AccessControl');
			$scope.remove = function(scope) {
				console.log(scope);
			      scope.remove();
				/*alert($scope.data.length);
				if($scope.data.length > 1){
					$parentNodesScope.removeNode(e)
					//$(this).closest("li:not(:first-child)").remove();
				}*/
			    };

			    $scope.toggle = function(scope) {
			      scope.toggle();
			    };

			    $scope.moveLastToTheBeginning = function() {
			      var a = $scope.data.pop();
			      $scope.data.splice(0, 0, a);
			    };

			    $scope.newSubItem = function(scope) {
			      var nodeData = scope.$modelValue;
			      nodeData.nodes.push({
			        id: nodeData.id * 10 + nodeData.nodes.length,
			        //title: nodeData.title + '.' + (nodeData.nodes.length + 1),
			        nodes: []
			      });
			    };

			    $scope.collapseAll = function() {
			      $scope.$broadcast('angular-ui-tree:collapse-all');
			    };

			    $scope.expandAll = function() {
			      $scope.$broadcast('angular-ui-tree:expand-all');
			    };
			    
			    $scope.addMenu = function() {
			        $scope.data.push({
			        	'icon': '',
				        'label': '',
				        'url':'',
				        'isUrl':false,
				        'nodes': []
			        });
			    }

			    $scope.data = [{
			    	'icon': '',
			        'label': '',
			        'url':'',
			        'isUrl':false,
			        'nodes': []
			    }];
		}]);
