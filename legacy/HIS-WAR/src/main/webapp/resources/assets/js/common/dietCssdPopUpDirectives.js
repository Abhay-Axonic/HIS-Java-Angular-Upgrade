angular.module('myApp').directive('dietCssdPopUp', function () {
    return {
      template: '<div class="modal fade ng-scope in" role="dialog his-style" style="background:rgba(0, 0, 0, 0.33);">'+
    		'<div class="modal-dialog">'+
		'<div class="modal-content">'+
			'<div class="modal-header">'+
				'<div class="modal-title patient-details-row">'+
					'Confirm'+
					'<button type="button" class="close" data-dismiss="dietCssdPopUp" ng-click="cancel();">&times;</button>'+
				'</div>'+
			'</div>'+
			'<div class="modal-body">'+
				'<div class="margin-left10"><span>Are you sure you want to perform this action?</span></div>'+
			'</div>'+
			'<div class="modal-footer" style="border:none;">'+
				'<button type="button" style="width:auto;" ng-click="DietCssdPopupOkBtn()" data-dismiss="dietCssdPopUp" class="btn btn-primary width15"  id="">Ok</button>'+ 
				'<button type="button" style="width:auto;" data-dismiss="dietCssdPopUp" class="btn btn-primary width15">Cancel</button>'+
			'</div>'+
		'</div>'+
	'</div>'+
'</div>',
      restrict: 'E',
      transclude: true,
      replace:true,
      scope:true,
      link: function postLink(scope, element, attrs) {
          scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else
            $(element).modal('hide');
        });

        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
      }
    };
  });