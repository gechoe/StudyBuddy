var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://127.0.0.1:27017/myDatabase'); 

var Schema = mongoose.Schema;

var pointSchema = new Schema({
   username: { type: String, required: true, unique: false },
   points: { type: Number, required: true, unique: false }
});

// export userSchema as a class called Users
module.exports = mongoose.model('Points', pointSchema);
