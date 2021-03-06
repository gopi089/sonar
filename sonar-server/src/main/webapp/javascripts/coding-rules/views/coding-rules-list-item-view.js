// Generated by CoffeeScript 1.6.3
(function() {
  var __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  define(['backbone.marionette', 'coding-rules/views/coding-rules-detail-view', 'common/handlebars-extensions'], function(Marionette, CodingRulesDetailView) {
    var CodingRulesListItemView, _ref;
    return CodingRulesListItemView = (function(_super) {
      __extends(CodingRulesListItemView, _super);

      function CodingRulesListItemView() {
        _ref = CodingRulesListItemView.__super__.constructor.apply(this, arguments);
        return _ref;
      }

      CodingRulesListItemView.prototype.tagName = 'li';

      CodingRulesListItemView.prototype.template = getTemplate('#coding-rules-list-item-template');

      CodingRulesListItemView.prototype.activeClass = 'active';

      CodingRulesListItemView.prototype.events = function() {
        return {
          'click': 'showDetail'
        };
      };

      CodingRulesListItemView.prototype.showDetail = function() {
        var _this = this;
        this.$el.siblings().removeClass(this.activeClass);
        this.$el.addClass(this.activeClass);
        this.options.app.layout.showSpinner('detailsRegion');
        return this.model.fetch().done(function() {
          var detailView;
          detailView = new CodingRulesDetailView({
            model: _this.model
          });
          return _this.options.app.layout.detailsRegion.show(detailView);
        });
      };

      return CodingRulesListItemView;

    })(Marionette.ItemView);
  });

}).call(this);
