(function ($) { 

    if($.router){
       if (window.console && window.console.warn) {
            window.console.warn("There is already a router plugin present, jQuery-router is backing off...");
            return;
        }
    }

    RouterConfig = function(separator){
        this.separator = separator;
    };

    Route = function(path, callback, reRouteCallback){
        this.path = this.parse(path);
        this.callback = callback;
        this.reRouteCallback = reRouteCallback;
        this.parameters = null;
        this.data = null;
        this.payload = null;
    };
    Route.prototype.init = function(){
        this.parts = this.split(this.path);
    };
    Route.prototype.parse = function(path){
        return decodeURI(path).replace(/\/$/, "");
    };
    Route.prototype.split = function(path){
        return path.startsWith("#") ? 
            path.substring(1).split($.router.config.separator) : 
            path.split($.router.config.separator);
    };
    Route.prototype.matches = function(path, parseParameters){
        pathParts = this.split(this.parse(path));
        if(this.parts.length == pathParts.length){
            var counter = 0;
            var data = [];
            for(var index = 0; index < this.parts.length; index++){
                if(this.parts[index].indexOf(":") === 0){
                    data.push(pathParts[index]);
                    counter++;
                }else if(this.parts[index] == pathParts[index]){
                    counter++;
                }
            }
            if(counter == this.parts.length){
                if(true == parseParameters){
                    this.parameters = data;
                }
                return true;
            }
        }
        return false;
    };

    Router = function(){
        this.routes = [];
    };
    Router.prototype.init = function(config){
        this.config = config ? config : new RouterConfig("/");
        $.each($.router.routes, function(index, route){
            route.init();
            return true;
        });
    };
    Router.prototype.add = function(path, callback, reRouteCallback){
        this.routes.push(new Route(path, callback, reRouteCallback));
    };
    Router.prototype.reRouteCallback = function(path){
         $.each(this.routes, function(index, route){
            if(route.matches(path)){
                if(route.reRouteCallback && typeof route.reRouteCallback === 'function'){
                    route.reRouteCallback.apply(route, route.parameters);
                }
                return false;
            }
            return true;
        });
    };
    Router.prototype.routeCallback = function(path){
        $.each($.router.routes, function(index, route){
            if(route.matches(path, true)){
                route.callback.apply(route, route.parameters);
                return false;
            }
            return true;
        });
    };
    Router.prototype.go = function(newPath, oldPath){
    	oldPath = oldPath ? oldPath : window.location.hash; 
    	$.router.reRouteCallback(oldPath);
    	$.router.routeCallback(newPath);
    };
    Router.prototype.start = function(config){
        this.init(config);
        var router = this;
        $(window).hashchange(function(event){
        	var oldPath = null;
            var newPath = null;
            if(event && event.originalEvent){
                root = window.location.origin + window.location.pathname;
                if(event.originalEvent.oldURL){
                    oldPath = event.originalEvent.oldURL.substring(root.length);
                }
                if(event.originalEvent.newURL){
                    var newPath = event.originalEvent.newURL.substring(root.length);
                }
            }else{
                newPath = window.location.hash;
            }
            oldPath = oldPath ? oldPath : "#initial"; 
            router.go(newPath, oldPath);
        });
    };

    $.router = new Router();

})(jQuery);