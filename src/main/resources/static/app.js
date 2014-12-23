var rocketLauncherApp = angular.module('rocketLauncherApp', []);

rocketLauncherApp.controller('RocketController', function ($scope, $http) {
	
	$scope.sendCommand = function(command) {
		$http.post('/api/rocket/' + command);
	}
	
});