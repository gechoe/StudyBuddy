//package com.example.studybuddy;
//
//import android.content.Context;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Accounts {
//    public static List<User> usersList = new ArrayList<>();
//    public static User user1;
//    public static User user2;
//
//    public User getUser(String username) {
//        for (User user : usersList) {
//            if (user.getUsername().equals(username)) {
//                return user;
//            }
//        }
//
//        return null; // User not found
//    }
//
//    public boolean userExists(String username) {
//        for (User user : usersList) {
//            if (user.getUsername().equals(username)) {
//                return true;
//            }
//        }
//
//        return false; // User not found
//    }
//
//    // Initialize users
//    static {
//        user1 = new User("user1", "user1");
//        user2 = new User("user2", "user2");
//        usersList.add(user1);
//        usersList.add(user2);
//
//        user2.addStudyBuddy("Bunny");
//        user2.addStudyBuddy("Dog");
//        user2.addStudyBuddy("Duck");
//
////        saveUserData(user2, context);
//    }
//
//    // Method to initialize the usersList and save user data
//    public static void initialize(Context context) {
//        // Initializing user1 and user2
//        user2.addStudyBuddy("Bunny");
//        user2.addStudyBuddy("Dog");
//
//        saveUserData(user2, context);
//    }
//
//    // Method to save user data to SharedPreferences
//    private static void saveUserData(User user, Context context) {
//        if (user != null) {
//            user.saveUserDataToSharedPreferences(context); // Save the user's data to SharedPreferences
//        }
//    }
//}