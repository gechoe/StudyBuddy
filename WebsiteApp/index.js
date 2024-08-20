// set up Express
var express = require('express');
var app = express();

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// import the User class from User.js and the other classes
var Users = require('./Users.js');
const e = require('express');

/***************************************/

// Testing endpoint creation using Hello World
app.use('/hello', (req, res) => {
	res.send("Hello World");
});

// Endpoint for creating a new user from android app
app.use('/addUserApp', async (req, res) =>{
	const username = req.query.username;
    const password = req.query.password;

    if (!username || !password) {
        return res.status(400).send('Username and password are required');
    }

    try {
        const existingUser = await Users.findOne({ 'username': username });
        if (existingUser) {
            return res.status(400).send('Username already exists');
        }

        const newUser = new Users({
            username: username,
            password: password
        });

        await newUser.save();

        res.status(200).send('User added successfully');
    } catch (error) {
        console.error('Error adding user:', error);
        res.status(500).send('Internal Server Error');
    }
});

// Endpoint to handle the form submission and add a new user
// app.post('/addUser', (req, res) => {
//     // Retrieve username and password from the request body
//     const { username, password } = req.body;

//     // Create a new user object based on the retrieved data
//     var newUser = new Users({
//         username,
//         password
//     });

//     // Save the new user to the database
//     newUser.save((err) => {
//         if (err) {
//             // Handle error
//             console.error('Error adding user: ', err);
//             res.status(500).send('Error adding user');
//         } else {
//             // User successfully added
//             res.status(200).send('User added successfully');
//         }
//     });
// });


//endpoint for creating a new user from "Add User" request form
// Endpoint to handle the form submission and add a new user
app.use('/addUser', (req, res) => {
    // Retrieve username and password from the request body
    const { username, password } = req.body;

    // Create a new user object based on the retrieved data
    var newUser = new Users({
        username,
        password
    });

    // Save the new user to the database
    newUser.save()
        .then(() => {
            // User successfully added
            res.redirect('/website/redirect.html');
        })
        .catch((err) => {
            // Handle error
            console.error('Error adding user: ', err);
            res.status(500).send('Error adding user');
        });
});


// Endpoint for deleting a user in the app
app.use('/deleteUserApp', async (req, res) => {
	try {
		const username = req.query.username; // Assuming username is passed in the request
		const deletedUser = await Users.findOneAndDelete({ 'username': username });
		if (!deletedUser) {
			// Handle case where user is not found
			console.log("User not found");
			return;
		}
		// // Handle successful deletion if necessary
		// console.log("User deleted successfully");
		// // Send success response
		// res.send("User deleted successfully");
		// User successfully deleted
		res.redirect('/website/redirect.html');
	} catch (err) {
		console.error('Uh oh, error:', err);
		// Handle error
		res.status(500).send('Internal Server Error');
	}
});

// Endpoint for deleting a user
app.use('/deleteUser', async (req, res) => {
	try {
		const username = req.query.username; // Assuming username is passed in the request
		console.log("Deleting user:", username);
		const deletedUser = await Users.findOneAndDelete({ 'username': username });
		if (!deletedUser) {
			// Handle case where user is not found
			console.log("User not found");
			return;
		}
		
		// User successfully deleted
		res.redirect('/website/redirect.html');
		// Redirect or send response
		// res.redirect('/allUsers');
	} catch (err) {
		console.error('Uh oh, error:', err);
		// Handle error
		res.status(500).send('Internal Server Error');
	}
});

// Endpoint for user authentication
app.post('/getUserAccount', async (req, res) => {
    const username = req.body.username;

    try {
        // Query the database for the user
        // Find the user by username
        const user = await Users.findOne({ 'username': username });

        if (user) {
            // User found, send user data as JSON response
            res.json(user);
        } else {
            // User not found, send appropriate error response
            res.status(404).json({ message: 'User not found' });
        }
    } catch (error) {
        // Handle errors
        console.error(error);
        res.status(500).json({ message: 'Internal server error' });
    }
});

// Endpoint for user authentication
app.post('/findUser', async (req, res) => {
    const username = req.body.username;

    try {
        // Query the database for the user
        // Find the user by username
        const user = await Users.findOne({ 'username': username });

        if (user) {
            // User found, send user data as JSON response
            res.json(true);
        } else {
            // User not found, send appropriate error response
            res.json(false);
        }
    } catch (error) {
        // Handle errors
        console.error(error);
        res.status(500).json({ message: 'Internal server error' });
    }
});

// app.use('/editUser', (req, res) => {
// 	var changedUser = { 'username': req.query.username };// User we are updating
// 	let user = {};
// 	Users.findOne(changedUser, (err, result) => {
// 		if (err) {
// 			user = {};
// 		}
// 		if (result == null) {
// 			user = result;
// 		}
// 	});

// 	Users.findOneAndUpdate(changedUser, {
// 		$set: {
// 			password: req.query.password ? req.query.password : changedUser.password,
// 			buddies: req.query.buddies ? req.query.buddies : changedUser.buddies,
// 			tasks: req.query.tasks ? req.query.tasks : changedUser.tasks,
// 			points: req.query.points ? req.query.points : changedUser.points,
// 		}
// 	},
// 		(err, result) => {
// 			if (err) {
// 				res.type('html').status(200);
// 				console.log(err);

// 			}

// 			if (result == null) {
// 				res.type('html').status(200);
// 				console.log("original information found " + err);

// 			} else {
// 				//res.redirect('/allUsers');
// 			}
// 		}
// 	);
// });

// Endpoint to handle adding a new task for a user
app.use('/addTaskApp', async (req, res) => {
    try {
        const username = req.body.username; // Assuming username is sent in the request body
        const taskDescription = req.body.task; // Assuming task description is sent in the request body

        // Find the user by username
        const user = await Users.findOne({ 'username': username });

        if (!user) {
            // Handle case where user is not found
            console.log("User not found");
            return res.status(404).send('User not found');
        }

        // Add the new task to the user's tasks array
        user.tasks.push({ task: taskDescription, complete: false }); // Assume new tasks are not completed by default

        // Save the updated user object to the database
        await user.save();

        // Send a success response
        res.status(200).send('Task added successfully');
    } catch (err) {
        console.error('Error adding task:', err);
        res.status(500).send('Internal Server Error');
    }
});

// Endpoint to handle completing a task for a user
app.use('/completeTaskApp', async (req, res) => {
    try {
        const username = req.query.username; // Assuming username is sent in the request query parameters
        const taskName = req.query.task; // Assuming task name is sent in the request query parameters

        // Find the user by username
        const user = await Users.findOne({ 'username': username });

        if (!user) {
            // Handle case where user is not found
            console.log("User not found");
            return res.status(404).send('User not found');
        }

        // Find the task by name in the user's tasks array
        const task = user.tasks.find(task => task.task === taskName);

        if (!task) {
            // Handle case where task is not found
            console.log("Task not found for the user");
            return res.status(404).send('Task not found for the user');
        }

        // Update the task's completion status to true
        task.complete = true;

        // Save the updated user object to the database
        await user.save();

        // Send a success response
        res.status(200).send('Task completed successfully');
    } catch (err) {
        console.error('Error completing task:', err);
        res.status(500).send('Internal Server Error');
    }
});

// Gets the user's incomplete tasks
app.use('/getUserTasksApp', async (req, res) => {
    try {
        const username = req.query.username; // Assuming username is passed in the request query
        const user = await Users.findOne({ 'username': username });
        if (!user) {
            // Handle case where user is not found
            console.log("User not found");
            return res.status(404).send('User not found');
        }

        // Filter tasks where complete is false
        const incompleteTasks = user.tasks.filter(task => !task.complete);
        // Send the incomplete tasks associated with the user as JSON response
        res.status(200).json(incompleteTasks);
    } catch (err) {
        console.error('Error fetching tasks:', err);
        res.status(500).send('Internal Server Error');
    }
});

// Gets the user's points
app.get('/getUserPointsApp', async (req, res) => {
    try {
        // Retrieve username from request query parameters
        const { username } = req.query;

        // Find the user in the database by username
        const user = await User.findOne({ username });

        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }

        // Extract and send the points of the user
        res.json({ points: user.points });
    } catch (error) {
        console.error('Error retrieving user points:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

// Define endpoint to get user data by ID
app.get('/users/:userId', async (req, res) => {
    try {
        const userId = req.params.userId;
        const user = await Users.findById(userId);
        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }
        res.json(user);
    } catch (error) {
        console.error('Error fetching user:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

// app.put('/updateUserPoints', async (req, res) => {
//     try {
//         // Retrieve username and new points from request body
//         const { username, newPoints } = req.body;

//         // Find the user in the database by username
//         const user = await User.findOne({ username });

//         if (!user) {
//             return res.status(404).json({ error: 'User not found' });
//         }

//         // Update the user's points
//         user.points = newPoints;

//         // Save the updated user object back to the database
//         await user.save();

//         // Send a success response
//         res.status(200).json({ message: 'User points updated successfully' });
//     } catch (error) {
//         console.error('Error updating user points:', error);
//         res.status(500).json({ error: 'Internal Server Error' });
//     }
// });

app.use('/updateUserPoints', (req, res) => {
	// User we are updating
	const username = req.query.username;
	var points = { 'points': req.query.points };
	let user = {};

	Users.findOne(username, (err, result) => {
		if (err) {
			user = {};
		}
		if (result == null) {
			user = result;
		}
	});

	Users.findOneAndUpdate(changedUser, {
		$set: {
			points: req.query.points ? req.query.points : username.points,
		}
	},
		(err, result) => {
			if (err) {
				res.type('html').status(200);
				console.log(err);

			}
			if (result == null) {
				res.type('html').status(200);
				console.log("original information found " + err);

			} else {
				//res.redirect('/allUsers');
			}
		}
	);
});

// Endpoint for showing all the users enrolled in the database
app.use('/allUsers', async (req, res) => {
	try {
		const users = await Users.find({}).sort({ _id: 'asc' });
		if (users.length === 0) {
			res.type('html').status(200).send('There are no users in the database.');
		} else {
			let responseHTML = 'Here are the users in the database:<ul>';
			users.forEach(user => {
				responseHTML += `<li>Username: ${user.username}; \tPassword: ${user.password}\t`;
				responseHTML += `<a href="/deleteUser?username=${user.username}">[Delete]</a></li>`;
			});
			responseHTML += '</ul>';
			res.type('html').status(200).send(responseHTML);
		}
	} catch (err) {
		console.error('Uh oh, error:', err);
		res.status(500).send('Internal Server Error');
	}
});

// Endpoint for looking at users
// app.use('/users', (req, res) => {
// 	Users.find({}, (err, u) => {
// 		// console.log(u);
// 		if (err) {
// 			console.log(err);
// 			res.json({});
// 		}
// 		else if (u.length == 0) {
// 			res.json({});
// 		}
// 		else {
// 			var returnArray = [];
// 			u.forEach( (users) => {
// 				returnArray.push({ "username": users.username, "password": users.password });
// 			});
// 			res.json(returnArray);
// 		}
// 	});
// });

app.use('/users', async (req, res) => {
	try {
		const u = await Users.find({});
		if (u.length === 0) {
			res.json({});
		} else {
			const returnArray = u.map(user => ({ username: user.username, password: user.password, buddies: user.buddies, tasks: user.tasks, points: user.points }));
			res.json(returnArray);
		}
	} catch (err) {
		console.error(err);
		res.status(500).json({ error: 'Internal server error' });
	}
});


// app.use('/editUser', async (req, res) => {
// 	try {
// 		const changedUser = { 'username': req.query.username };
// 		const user = await Users.findOne(changedUser);
// 		if (!user) {
// 			// Handle case where user is not found
// 			console.log("User not found");
// 			return;
// 		}
// 		const updatedUser = await Users.findOneAndUpdate(changedUser, {
// 			$set: {
// 				password: req.query.password ? req.query.password : user.password,
// 			}
// 		});
// 		// Handle successful update if necessary
// 		console.log("User updated successfully");
// 		// Redirect or send response
// 		// res.redirect('/allUsers');
// 	} catch (err) {
// 		console.error('Uh oh, error:', err);
// 		// Handle error
// 		res.status(500).send('Internal Server Error');
// 	}
// });

// app.use('/editUser', async (req, res) => {
//     try {
//         const username = req.query.username;
// 		console.log("Username:", username);
//         const user = await Users.findOne({ 'username': username }).exec(); // Add .exec() to convert to a promise
//         if (!user) {
//             console.log("User not found");
//             return res.status(404).json({ error: 'User not found' });
//         }

//         const updatedFields = {};
//         if (req.body.password !== undefined) {
//             updatedFields.password = req.body.password;
//         }
//         if (req.body.buddies !== undefined) {
//             updatedFields.buddies = req.body.buddies;
//         }
//         if (req.body.tasks !== undefined) {
//             updatedFields.tasks = req.body.tasks;
//         }
//         if (req.body.points !== undefined) {
//             updatedFields.points = req.body.points;
//         }
//         // Add handling for other fields if needed

//         const updatedUser = await Users.findOneAndUpdate(changedUser, { $set: updatedFields }, { new: true }).exec(); // Add .exec() here as well

//         console.log("User updated successfully");
//         return res.status(200).json(updatedUser);
//     } catch (err) {
//         console.error('Uh oh, error:', err);
//         return res.status(500).send('Internal Server Error');
//     }
// });

// app.get('/editUser', async (req, res) => {
//     try {
//         const username = req.query.username; // Get username from query parameters
//         console.log("Username:", username); // Log the username

//         if (!username) {
//             console.log("Username not provided");
//             return res.status(400).json({ error: 'Username not provided' });
//         }

//         const user = await Users.findOne({ 'username': username }).exec(); // Find user by username

//         if (!user) {
//             console.log("User not found");
//             return res.status(404).json({ error: 'User not found' });
//         }

//         const updatedFields = {};
//         if (req.query.password !== undefined) {
//             updatedFields.password = req.query.password;
//         }
//         if (req.query.buddies !== undefined) {
//             updatedFields.buddies = req.query.buddies;
//         }
//         if (req.query.tasks !== undefined) {
//             updatedFields.tasks = req.query.tasks;
//         }
//         if (req.query.points !== undefined) {
//             updatedFields.points = req.query.points;
//         }
//         // Add handling for other fields if needed

//         const updatedUser = await Users.findOneAndUpdate({ 'username': username }, { $set: updatedFields }, { new: true }).exec(); // Update user document

//         console.log("User updated successfully");
//         return res.status(200).json(updatedUser);
//     } catch (err) {
//         console.error('Uh oh, error:', err);
//         return res.status(500).send('Internal Server Error');
//     }
// });

// app.use('/editUser', async (req, res) => {
// 	try {
//         const changedUser = { 'username': req.query.username }; // User we are updating

//         // Find user by username
//         const user = await Users.findOne(changedUser).exec();

//         if (!user) {
//             console.log("User not found");
//             return res.status(404).json({ error: 'User not found' });
//         }

//         // Update user fields
//         const updatedUser = await Users.findOneAndUpdate(changedUser, {
//             $set: {
//                 password: req.body.password ? req.body.password : user.password,
//                 buddies: req.body.buddies ? req.body.buddies : user.buddies,
//                 tasks: req.body.tasks ? req.body.tasks : user.tasks,
//                 points: req.body.points ? req.body.points : user.points,
//             }
//         }, { new: true }).exec();

//         console.log("User updated successfully");
//         return res.status(200).json(updatedUser);
//     } catch (err) {
//         console.error('Uh oh, error:', err);
//         return res.status(500).send('Internal Server Error');
//     }
// });


// Define the editUser endpoint
app.post('/editUser', async (req, res) => {
	try {
	  	const changedUser = { 'username': req.query.username }; // User we are updating
  
	  	// Find user by username
	  	const user = await Users.findOne(changedUser);
  
	  	if (!user) {
			console.log("User not found");
			return res.status(404).json({ error: 'User not found' });
		}
  
		// Update user fields
		if (req.body.password) user.password = req.body.password;
		if (req.body.buddies) user.buddies = req.body.buddies;
		if (req.body.tasks) user.tasks = req.body.tasks;
		if (req.body.points) user.points = req.body.points;
  
		// Use findOneAndUpdate to find and update the user
		const updatedUser = await Users.findOneAndUpdate(
			{ username }, // Filter: Find user by username
			update,      // Update: Fields to update
			{ new: true } // Options: Return the updated document
		);

		if (!updatedUser) {
			console.log("User not found");
			return res.status(404).json({ error: 'User not found' });
		}
  
		console.log("User updated successfully");
		return res.status(200).json(updatedUser);
	} catch (err) {
	  console.error('Uh oh, error:', err);
	  return res.status(500).send('Internal Server Error');
	}
});

// // Endpoint for creating a new user from android app
// app.use('/updateUserApp', async (req, res) =>{
// 	const user = await Users.findOne(changedUser);
// 	if (!user) {
// 		// Handle case where user is not found
// 		console.log("User not found");
// 		return;
// 	}

// 	var newUser = new Users ({
// 		username: req.query.username,
// 		password: req.query.password,
// 		buddies: req.query.buddies,
// 		tasks: req.query.tasks,
// 		points: req.query.points
// 	});
	
// 	// Save the user to the database
// 	// newUser.save( (err) => { 
// 	// 	if (err) {
// 	// 		res.type('html').status(200);
// 	// 		res.write('Uh oh, error: ' + err);
// 	// 		console.log(err);
// 	// 		res.end();
// 	// 	}
// 	// });

// 	// Save the user to the database
// 	newUser.save()
// 		.then(() => {
// 			res.status(200).send('User added successfully');
// 		})
// 		.catch((err) => {
// 			res.status(500).send('Uh oh, error: ' + err);
// 		});
// });

// /updateUser endpoint to update a user
app.post('/updateUser', async (req, res) => {
    const username = req.query.username;
    const { password, buddies, tasks, points } = req.body;
    console.log(username);
    console.log(req.body);

    try {
        let user = await Users.findOne({ username });

        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }

        if (password) {
            console.log(password);
            user.password = password;
        }

        if (buddies) {
            console.log(buddies);
            user.buddies = buddies;
        }

        if (tasks) {
            console.log(tasks);
            user.tasks = tasks.map(task => ({ task: task.task, complete: task.complete }));
        }

        if (points !== undefined) {
            console.log(points);
            user.points = points;
        }

        await user.save();
        console.log("After save~~~~~~");
        res.json({ message: 'User updated successfully', user });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

  


/*************************************************/

app.use('/website', express.static('website'));

app.get('/', (req, res) => { res.redirect('/website/home.html'); });

// Add exceptions for specific paths
// app.use(['/website/home.html', '/userAddForm.html'], express.static('public'));

app.listen(11111, () => {
	console.log('Listening on port 11111');
});

