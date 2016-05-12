/**
 * Created by shekhargulati on 10/06/14.
 */
 
var app = angular.module('todoapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngAnimate'
]);

$(document).ready(function () {
  var trigger = $('.hamburger'),
  overlay = $('.overlay'),
  isClosed = false;

	trigger.click(function () {
	  hamburger_cross();      
	});

	function hamburger_cross() {

	  if (isClosed == true) {          
	    overlay.hide();
	    trigger.removeClass('is-open');
	    trigger.addClass('is-closed');
	    isClosed = false;
	  } else {   
	    overlay.show();
	    trigger.removeClass('is-closed');
	    trigger.addClass('is-open');
	    isClosed = true;
	   }
	}
	  
	  $('[data-toggle="offcanvas"]').click(function () {
	    $('#wrapper').toggleClass('toggled');
	  }); 
	  
});

app.config(function ($routeProvider) {
    $routeProvider.when('/todoList', {
        templateUrl: 'views/toDoList.html',
        controller: 'ListCtrl'
    })
    .when('/createTodo', {
        templateUrl: 'views/createTodo.html',
        controller: 'CreateCtrl'
    })
    .when('/userList', {
        templateUrl: 'views/userList.html',
        controller: 'UserListCtrl'
    })
    .when('/createUser', {
        templateUrl: 'views/createUser.html',
        controller: 'CreateUserCtrl'
    })
    .when('/quizz', {
        templateUrl: 'views/quizz.html',
        controller: 'quizzCtrl'
    })
    .when('/quizzCat/:id', {
        templateUrl: 'views/quizzCat.html',
        controller: 'quizzCatCtrl'
    })
    .when('/createQuizzCat', {
        templateUrl: 'views/createQuizzCat.html',
        controller: 'CreateCatCtrl'
    })
    .when('/noQuestions/:id', {
        templateUrl: 'views/noQuestion.html',
        controller: 'quizzCatCtrl'
    })
    .when('/createQuizzQuest/:id', {
        templateUrl: 'views/createQuizzQuest.html',
        controller: 'CreateQuestCtrl'
    })
    .when('/quizzQuest/:id', {
        templateUrl: 'views/quizzQuest.html',
        controller: 'selectQuestCtrl'
    })
    .when('/todo/:id', {
        templateUrl: 'views/todo.html',
        controller: 'todoCtrl'
    })
    .when('/createTask/:id', {
        templateUrl: 'views/createTask.html',
        controller: 'CreateTaskCtrl'
    })
    .when('/task/:todoId/:taskId', {
        templateUrl: 'views/task.html',
        controller: 'taskCtrl'
    })
    .when('/newTask/:todoId/:taskId', {
        templateUrl: 'views/createTask.html',
        controller: 'CreateTaskCtrl'
    })
    .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'loginCtrl'
    })
    .when('/singOut', {
        templateUrl: 'views/login.html',
        controller: 'signOutCtrl'
    })
    .otherwise({
        redirectTo: '/todoList'
    })
});

app.run(['$rootScope', '$location', '$http',function($rootScope, $location, $http) {

    $rootScope.$on('$locationChangeStart', function (event, next, current) {
    // redirect to login page if not logged in and trying to access a restricted page
    if($location.path() != '/login' ){
        
        $http.get('/api/v1/testLogin').success(function (data) {
        	if(data == 'true'){
        		$rootScope.loggedIn = true;
                $http.get('/api/v1/userConnected').success(function (data) {
                	if(data && data != 'null')
                		$rootScope.userConnected = data.replace(/"/g, '');
                	else $rootScope.userConnected = null;
                })
        	}
        	else{
        		$rootScope.loggedIn = false;
                $location.path('/login');
        	}
        })
    }
    
    });
}]);
 
app.controller('ListCtrl', function ($scope, $http, $location, $route, $rootScope) {
	
    $http.get('/api/v1/todos').success(function (data) {
    	$scope.todos = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.todoStatusChanged = function (todo) {
        console.log(todo);
        $http.put('/api/v1/todos/' + todo.id, todo).success(function (data) {
            console.log('status changed');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
    
    $scope.deleteToDo = function (todo) {
    	$http.put('/api/v1/delete/' + todo.id).success(function (data) {
            console.log('todo deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
    
    $scope.openTodo = function (todo) {
    	$location.path('/todo/' + this.todo.id);
    }
    
});
 
app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.todo = {
        done: false
    };
    
	$http.get('/api/v1/users').success(function (data) {
		$scope.users = data;
	});
    
    $scope.createTodo = function () {
        console.log($scope.todo);
        $http.post('/api/v1/todos', $scope.todo).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});


app.controller('UserListCtrl', function ($scope, $http, $route) {
    $http.get('/api/v1/users').success(function (data) {
        $scope.users = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.deleteUser = function (user) {
    	$http.put('/api/v1/delete', user).success(function (data) {
            console.log('user deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});
 
app.controller('CreateUserCtrl', function ($scope, $http, $location) {
 
    $scope.createUser = function () {
        console.log($scope.user);
        $http.post('/api/v1/users', $scope.user).success(function (data) {
            $location.path('/userList');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});


app.controller('quizzCtrl', function ($scope, $http, $location, $route) {
	$http.get('/api/v1/quizzCats').success(function (data) {
        $scope.cats = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.openCat = function (){
		$location.path('/quizzCat/' + this.cat.id);
	}
	
	$scope.restartCat = function(cat){
		$http.put('/api/v1/restartCat/' + cat.id).success(function (data) {
            console.log('category restarted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
	
	$scope.deleteCat = function(cat){
		$http.put('/api/v1/deleteCat/' + cat.id).success(function (data) {
            console.log('category deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
});

app.controller('quizzCatCtrl', function ($scope, $http, $location, $routeParams) {
	$http.get('/api/v1/quizzCat/' + $routeParams.id).success(function (data) {
        $scope.cat = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.openCat = function (){
		$location.path('/quizzCat/' + this.cat.id);
	}
	
	$scope.playCat = function(){
		$http.get('/api/v1/playCat/' + $scope.cat.id).success(function (data) {
	        var id = data;
	        if(id == 0){
	        	$location.path('/noQuestions/' + $scope.cat.id);
	        }
	        else{
	        	$location.path('/quizzQuest/' + $scope.cat.id);
	        }
	    }).error(function (data, status) {
	        console.log('Error ' + data)
	    })
	}
	
	$scope.deleteQuest = function(question){
		$http.put('/api/v1/deleteQuest/' + $scope.cat.id, question.id).success(function (data) {
            console.log('question deleted');
            $scope.cat = data;
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
});

app.controller('CreateCatCtrl', function ($scope, $http, $location) {
    
    $scope.createCat = function () {
        console.log($scope.cat);
        $http.post('/api/v1/cat', $scope.cat).success(function (data) {
            $location.path('/quizz');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('CreateQuestCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.catId = $routeParams.id;
    
	$scope.createQuest = function () {
        console.log($scope.quest);
        $http.post('/api/v1/newQuest/' + $scope.catId, $scope.quest).success(function (data) {
            $location.path('/quizzCat/' + $scope.catId);
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('selectQuestCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.catId = $routeParams.id;
	
	$scope.radioValue = '';
	
	$scope.success = null;
	
	$http.get('/api/v1/quest/' + $routeParams.id).success(function (data) {
        $scope.quest = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.radioChanged = function(){
		$scope.answerSelected = this.proposition;
	}
    
    $scope.answerQuest = function () {
        $http.put('/api/v1/updateQuest/' + $scope.catId + '/' +$scope.quest.id, $scope.answerSelected).success(function (data) {
            $scope.success = data;
            
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
    
    $scope.nextQuest = function() {
    	$http.get('/api/v1/findNextQuestion/' + $scope.catId).success(function (data) {
	        var id = data;
	        if(id == 0){
	        	$location.path('/noQuestions/' + $scope.catId);
	        }
	        else{
	        	$http.get('/api/v1/quest/' + $scope.catId).success(function (data) {
	                $scope.quest = data;
	                $scope.success = null;
	            }).error(function (data, status) {
	                console.log('Error ' + data)
	            })
	        }
	    }).error(function (data, status) {
	        console.log('Error ' + data)
	    })
    }
});

app.controller('todoCtrl', function ($scope, $http, $location, $routeParams, $route) {
	
	$http.get('/api/v1/users').success(function (data) {
		$scope.users = data;
	});
	
	$http.get('/api/v1/todos/' + $routeParams.id).success(function (data) {
        $scope.todo = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.taskDoneChanged = function(task){
		$http.put('/api/v1/taskDone/' + $scope.todo.id, task).success(function (data) {
            console.log('task changed');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
	
	$scope.openTask = function (task) {
    	$location.path('/task/' + $routeParams.id + '/' + task.id);
    }
	
	$scope.todoStatusChanged = function (todo) {
        console.log(todo);
        $http.put('/api/v1/todos/' + todo.id, todo).success(function (data) {
            console.log('status changed');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
	
	$scope.deleteTask = function (task) {
    	$http.delete('/api/v1/deleteTask/' + $routeParams.id + '/' + task.id).success(function (data) {
            console.log('task deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
	
});

app.controller('CreateTaskCtrl', function ($scope, $http, $location, $routeParams) {
    $scope.task = {
        done: false
    };
    
	$http.get('/api/v1/users').success(function (data) {
		$scope.users = data;
	});
    
    $scope.createTask = function () {
        console.log($scope.task);
        if($routeParams.todoId == null){
        	$http.post('/api/v1/newTask/' + $routeParams.id, $scope.task).success(function (data) {
                $location.path('/todo/' + $routeParams.id);
            }).error(function (data, status) {
                console.log('Error ' + data)
            })
        }
        else{
        	$http.post('/api/v1/newTask/' + $routeParams.todoId + '/' + $routeParams.taskId, $scope.task).success(function (data) {
                $location.path('/task/' + $routeParams.todoId + '/' + $routeParams.taskId);
            }).error(function (data, status) {
                console.log('Error ' + data)
            })
        }
    }
});

app.controller('taskCtrl', function ($scope, $http, $location, $routeParams, $route) {
	
	$http.get('/api/v1/users').success(function (data) {
		$scope.users = data;
	});
	
	$http.get('/api/v1/task/' + $routeParams.todoId + '/' + $routeParams.taskId).success(function (data) {
        $scope.task = data;
        if(data.parentTaskId == 0){
        	$scope.parentLink = 'todo/' + data.parentTodoId;
        }
        else{
        	$scope.parentLink = 'task/' + data.parentTodoId + '/' + data.parentTaskId;
        }
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.taskDoneChanged = function(task2){
		$http.put('/api/v1/taskDone/' + $routeParams.todoId, task2).success(function (data) {
            console.log('task changed');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
	
	$scope.openTask = function (task2) {
    	$location.path('/task/' + $routeParams.todoId + '/' + task2.id);
    }
	
	$scope.createTask = function (task2) {
		$location.path('/newTask/' + $routeParams.todoId + '/' + task2.id);
	}
	
	$scope.deleteTask = function (task2) {
    	$http.delete('/api/v1/deleteTask/' + $routeParams.todoId + '/' + task2.id).success(function (data) {
            console.log('task deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('loginCtrl', function ($scope, $http, $location, $rootScope) {
	$scope.logOk = true;
    $scope.login = function () {
        $http.put('/api/v1/login', $scope.user).success(function (data) {
        	$rootScope.loggedIn = true;
        	$location.path('/todoList');
        }).error(function (data, status) {
        	$scope.logOk = false;
        })
    }
});

app.controller('signOutCtrl', function ($scope, $http, $location, $rootScope) {
	$scope.logOk = false;
    $http.put('/api/v1/signOut').success(function (data) {
    	$rootScope.loggedIn = false;
    	$location.path('/');
    })
});