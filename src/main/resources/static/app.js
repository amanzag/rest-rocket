var rocketLauncherApp = angular.module('rocketLauncherApp', []);

rocketLauncherApp.controller('RocketController', function ($scope, $http, $interval) {
	
	$scope.sendCommand = function(command) {
		$http.post('/api/rocket/' + command);
	}
	$scope.showCamera = false;
	
	$http.head('/api/camera/frame').success(function() {
		$interval(function() {
			$scope.showCamera = true;
			$scope.cameraUrl = '/api/camera/frame?' + Math.random();
		}, 2000);
	});
	
});