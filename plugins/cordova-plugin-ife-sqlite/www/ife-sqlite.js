var exec = require('cordova/exec');

module.exports = {
  insert: function (name, success, error) {
    exec(success, error, 'IfeSQLite', 'insert', [name]);
  },
  getAll: function (success, error) {
    exec(success, error, 'IfeSQLite', 'getAll', []);
  },
  clear: function (success, error) {
    exec(success, error, 'IfeSQLite', 'clear', []);
  }
};
