var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://127.0.0.1:27017/myDatabase'); 

var Schema = mongoose.Schema;

var taskSchema = new Schema({
   username: { type: String, required: true, unique: false },
   taskItem: { type: String, required: true, unique: false }
});

// export taskSchema as a class called Tasks
module.exports = mongoose.model('Tasks', taskSchema);
