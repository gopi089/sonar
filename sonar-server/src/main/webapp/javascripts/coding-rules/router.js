// Generated by CoffeeScript 1.6.3
(function() {
  var __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  define(['backbone'], function(Backbone) {
    var AppRouter, _ref;
    return AppRouter = (function(_super) {
      __extends(AppRouter, _super);

      function AppRouter() {
        _ref = AppRouter.__super__.constructor.apply(this, arguments);
        return _ref;
      }

      AppRouter.prototype.routes = {
        '': 'index',
        ':query': 'index'
      };

      AppRouter.prototype.initialize = function(options) {
        return this.app = options.app;
      };

      AppRouter.prototype.parseQuery = function(query, separator) {
        return (query || '').split(separator || '|').map(function(t) {
          var tokens;
          tokens = t.split('=');
          return {
            key: tokens[0],
            value: decodeURIComponent(tokens[1])
          };
        });
      };

      AppRouter.prototype.emptyQuery = function() {
        return this.navigate('', {
          trigger: true,
          replace: true
        });
      };

      AppRouter.prototype.index = function(query) {
        var f, idObj, params,
          _this = this;
        params = this.parseQuery(query);
        idObj = _.findWhere(params, {
          key: 'id'
        });
        if (idObj) {
          f = this.app.favoriteFilter;
          this.app.canSave = false;
          f.set('id', idObj.value);
          return f.fetch({
            success: function() {
              params = _.extend({}, _this.parseQuery(f.get('query')), params);
              return _this.loadResults(params);
            }
          });
        } else {
          return this.loadResults(params);
        }
      };

      AppRouter.prototype.loadResults = function(params) {
        this.app.filterBarView.restoreFromQuery(params);
        this.app.restoreSorting(params);
        return this.app.fetchFirstPage();
      };

      return AppRouter;

    })(Backbone.Router);
  });

}).call(this);
