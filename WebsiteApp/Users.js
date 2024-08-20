var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://127.0.0.1:27017/gchoe');
// mongodb://127.0.0.1:27017 -> mongoDB

var Schema = mongoose.Schema;

var taskSchema = new Schema({
    task: { type: String, required: true },
    complete: { type: Boolean, required: true }
});

var userSchema = new Schema({
    username: { type: String, required: true },
    password: { type: String, required: true },
    buddies: {
        type: [String], // List of strings
        required: false // Set to true if items are required, false if optional
    },
    tasks: [taskSchema],
    points: { type: Number, required: false },

});

// export userSchema as a class called Users
module.exports = mongoose.model('Users', userSchema);