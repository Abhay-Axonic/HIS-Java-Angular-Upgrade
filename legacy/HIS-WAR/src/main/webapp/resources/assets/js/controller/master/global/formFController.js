/**
 * 
 */
angular.module('myApp')
		.controller("formFController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader','$stateParams','$timeout',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader,$stateParams,$timeout){
			$rootScope.loginpage = true;
	
			$scope.FroalaEditor = {};
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
	
		
			
			$scope.printTemplate = function()
			{
				
				var test = $("#page-content-wrapper").html();
				console.log("test",test);
				var divContents = document.getElementById("page-content-wrapper").innerHTML;  
	            var printWindow = window.open('', '', 'height=500,width=700');  
	            printWindow.document.write('<html><head><title>FORM F</title>');  
	            printWindow.document.write('</head><body >');  
	            printWindow.document.write(divContents);  
	            printWindow.document.write('</body></html>');  
	            printWindow.document.close();  
	            printWindow.print();
				
			}
			
			
			FroalaEditor.DefineIcon('insert', {
			    NAME: 'plus',
			    SVG_KEY: 'add'
			  });
			   FroalaEditor.RegisterCommand('insert', {
			    title: 'Insert HTML',
			    focus: true,
			    undo: true,
			    refreshAfterCallback: true,
			    callback: function() {
			      this.html.insert(`<input type="checkbox"> `);
			    }
			  });
			$scope.FroalaEditor =   new FroalaEditor('div#froala-editor', {
			   // toolbarButtons: ['insert', 'print', 'getPDF', 'html'],
			    toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', 'fontFamily', 'fontSize', 'color', 'emoticons', 'inlineStyle', 'paragraphStyle', 'paragraphFormat', 'align', 'formatOL', 'formatUL', 'outdent','print', 'getPDF', 'html','insert'],
			    events: {
			      'contentChanged': function() {
			        let editor = this
			        editor.html.get(true);
			       // console.log('abc',editor.html.get(true))
			        $scope.value = editor.html.get(true);
			      }
			    }
			  })
			
			 
			if (angular.equals({},
					$stateParams.itemObj)) {
				return;
			} else {
				$scope.value = $stateParams.itemObj.templateDesc;
				
				/* $timeout( function(){*/
					 $scope.FroalaEditor.html.insert($scope.value);
		         //  },5000);
				 
				
			}
			
			
}]);