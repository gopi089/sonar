// Generated by CoffeeScript 1.6.3
(function() {
  var __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  define(['backbone.marionette', 'common/handlebars-extensions'], function(Marionette) {
    var CodingRulesHeaderView, _ref;
    return CodingRulesHeaderView = (function(_super) {
      __extends(CodingRulesHeaderView, _super);

      function CodingRulesHeaderView() {
        _ref = CodingRulesHeaderView.__super__.constructor.apply(this, arguments);
        return _ref;
      }

      CodingRulesHeaderView.prototype.template = getTemplate('#coding-rules-header-template');

      return CodingRulesHeaderView;

    })(Marionette.ItemView);
  });

}).call(this);
