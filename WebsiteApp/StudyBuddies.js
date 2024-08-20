var mongoose = require('mongoose');
const port = 11111
const express = require('express')

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://127.0.0.1:27017/myDatabase'); 

var Schema = mongoose.Schema;

var buddySchema = new Schema({
   username: { type: String, required: true, unique: false },
   type: { type: String, required: true, unique: false },
   rarity: { type: String, required: false, unique: false },
   description: { type: String, required: false, unique: false }
});

// export buddySchema as a class called Buddies
module.exports = mongoose.model('Buddies', buddySchema);

const app = express()

app.listen(port, function(error){
    if(error) throw error
    console.log("Server created Successfully")
})
