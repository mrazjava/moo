var app = angular.module('mooChatDemo', ['ngStomp']);

app.controller('MooConnectController', function($scope, $http) {

	$scope.mooLogin = function() {
		var nickTxt = angular.element(document.querySelector('#nickName'))[0];

		if($scope.usr == undefined) {
			$scope.usr = {nickName: ' ', autogen: true};; 
		}
		console.log("moo login: [" + $scope.usr.nickName + "]");
		angular.element(document.querySelector('#loginButton'))[0].style.display = "none";
		angular.element(document.querySelector('#logoutButton'))[0].style.display = "inline";
		angular.element(document.querySelector('#sessionExpired'))[0].style.display = "none";
		angular.element(document.querySelector('#msgOutPanel'))[0].style.display = "block";
		nickTxt.disabled = true;
		
		$http.post('/moo/login', $scope.usr.nickName).success(function(data) {
			console.log("login result: " + data);
		});
	};

	$scope.mooLogout = function() {
		console.log("moo logout: " + $scope.usr.nickName);
				
		$http.post('/moo/logout', $scope.usr.nickName).success(function(data) {
			console.log("logout result: " + data);
		});

		if($scope.usr.autogen) {
			$scope.usr.nickName = '';
			$scope.usr.autogen = false;
		}

		resetUiAfterLogout();
	};
});

function resetUiAfterLogout() {
	
	angular.element(document.querySelector('#loginButton'))[0].style.display = "inline";
	angular.element(document.querySelector('#logoutButton'))[0].style.display = "none";
	angular.element(document.querySelector('#nickName'))[0].disabled = false;
	angular.element(document.querySelector('#msgOutPanel'))[0].style.display = "none";
}

app.controller('MooMsgController', function ($scope, $http) {
	  
	  $scope.mooMsg = function() {
		$scope.usr.nickName = angular.element(document.querySelector('#nickName'))[0].value;
	  	console.log("msg out: " + $scope.usr);
	  	
	  	$http.post('/moo/msg', $scope.usr);
	  	angular.element(document.querySelector('#msgOut'))[0].value = '';
	  };
});

app.controller('ChatMsgController', function ($stomp, $scope, $http) {
	  $scope.events = [];

	  $http.get('/latest-events').success(function(data) {
		  $scope.events = data;
	  });
    $stomp.connect('http://localhost:8080/chat-websocket', {})
          .then(function (frame) {
              var subscription1 = $stomp.subscribe('/topic/viewchats', 
                  function (payload, headers, res) {
                      $scope.events = payload;
                      $scope.$apply($scope.events);
              });
              var subscription2 = $stomp.subscribe('/topic/session-expired',
            	  function (payload, headers, res) {
            	  	console.log('session expired! (' + payload + ')');
            	  	resetUiAfterLogout();
            	  	angular.element(document.querySelector('#sessionExpired'))[0].style.display = "inline";
              });
              var subscription3 = $stomp.subscribe('/topic/nick-generated',
                	  function (payload, headers, res) {
          	  	console.log('server generated nick:! (' + payload + ')');
          	  angular.element(document.querySelector('#nickName'))[0].value = payload;
            });            		  
       });
});