package com.example.studybuddy;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CurrentUser {
    private static User currentUser;

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Gets points from the server
    public void savePointsDB(int points) {
        // Get the username of the current user
        CurrentUser current = new CurrentUser();
        User currUser = current.getCurrentUser();
        String username = currUser.getUsername(); // Assuming getUsername() method exists

        String param = "username="+ username + "&points=" + points;
        String link = "http://loin.cs.brynmawr.edu:11111/getUserPointsApp?";

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    URL url = new URL(link + param);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    try {
                        Scanner in = null;
                        conn.connect();

                        // now the response comes back
                        int responsecode = conn.getResponseCode();

                        // make sure the response has "200 OK" as the status
                        if (responsecode != 200) {
                            System.out.println("Unexpected status code: " + responsecode);
                        } else {
                            // made it here, we got a good response, let's read it
                            in = new Scanner(url.openStream());

                            while (in.hasNext()) {
                                String line = in.nextLine();
                            }

                            in.close();
                        }
                    } catch (java.net.ConnectException c) {
                        c.printStackTrace();
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            });
            executor = Executors.newSingleThreadExecutor();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {

            } catch (Exception f) {
            }
        }
    }

    // Function to get user data from the server and convert it into a User object
    public User getUserFromDB(JSONObject userJsonObject) {
        try {
            if (userJsonObject != null) {
                return new User(userJsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Function to fetch the JSON object representing the user from the server
    private JSONObject getUserJsonObject(String username) {
        AtomicReference<JSONObject> userJsonObject = new AtomicReference<>(new JSONObject());

        // Construct the request URL
        String link = "http://loin.cs.brynmawr.edu:11111/users";

        // Send the request to the server
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // Open connection and send HTTP request
                    try {
                        Scanner in = null;
                        conn.connect();

                        // Now the response comes back
                        int responseCode = conn.getResponseCode();

                        // Make sure the response has "200 OK" as the status
                        if (responseCode != 200) {
                            System.out.println("Unexpected status code: " + responseCode);
                        } else {
                            // Made it here, we got a good response, let's read it
                            in = new Scanner(url.openStream());

                            while (in.hasNext()) {
                                String response = in.nextLine();
                                JSONArray ja = new JSONArray(response);

                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = (JSONObject) ja.get(i);
                                    String user = jo.getString("username");

                                    if (user.equals(username)) {
                                        userJsonObject.set(jo);
                                    }
                                }
                            }

                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.shutdown();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userJsonObject.get();
    }

    // Checks if the user is a valid user in the user database
    public boolean isUserValid(String username) {
        AtomicBoolean valid = new AtomicBoolean(false);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> {
                try {
                    // URL, tries to connect to the specific, private url/port
                    // where the user database is located to access the information
                    URL url = new URL("http://loin.cs.brynmawr.edu:11111/users");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());

                    while (in.hasNext()) {
                        String response = in.nextLine();
                        JSONArray ja = new JSONArray(response);

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            String user = jo.getString("username");

                            if (user.equals(username)) {
                                valid.set(true);
                                return;
                            }
                        }
                    }
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // Wait for the task to complete
            future.get();
            executor.shutdown(); // Shutdown the executor

        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid.get();
    }

    // Saves the new user's data to the users database
    public void saveUserData(User user) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... users) {
                if (users.length == 0) {
                    return null;
                }

                User user = users[0];
                String username = user.getUsername();
                String password = user.getPassword();
                List<String> buddies = user.getStudyBuddies();
                List<Task> tasks = user.getTasks();
                int points = user.getPoints();

                try {
                    // Endpoint URL
                    URL url = new URL("http://loin.cs.brynmawr.edu:11111/updateUser?username=" + username); // Update with your actual server address

                    // Creating the connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    connection.connect();

                    // Creating the JSON object with user data
                    JSONObject userData = new JSONObject();
                    userData.put("password", password);
                    userData.put("buddies", new JSONArray(buddies)); // Convert List<String> to JSONArray
                    userData.put("tasks", tasksToJsonArray(tasks)); // Convert List<Task> to JSONArray
                    userData.put("points", points);

                    System.out.println("NAURRRRRR======");
                    System.out.println(userData);

                    // Write the data to connection output stream
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = userData.toString().getBytes("utf-8");
                        System.out.println("RAWRRRRR======");
                        System.out.println(input);
                        os.write(input, 0, input.length);
                        os.flush();
                    }

                    // Reading the response
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;

                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.out.println("Response: " + response.toString());
                    }

                    connection.disconnect(); // Disconnect
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute(user);
    }

    // Converts the specific user's tasks list to a specific type (json array)
    private static JSONArray tasksToJsonArray(List<Task> tasks) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (Task task : tasks) {
                JSONObject taskObject = new JSONObject();
                taskObject.put("task", task.getName());
                taskObject.put("complete", task.isCompleted());
                jsonArray.put(taskObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}