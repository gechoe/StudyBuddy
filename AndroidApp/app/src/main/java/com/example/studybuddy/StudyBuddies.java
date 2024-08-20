package com.example.studybuddy;

import java.util.HashMap;
import java.util.Map;

public class StudyBuddies {
    // List of StudyBuddy words/types with their likelihood/rarity percentages
    // of being pulled randomly
    private final String[] words = {"Dog", "Bunny", "Turtle", "Bear", "Frog", "Duck", "Panda", "Cat", "Capybara", "Blob"};
    private final double[] likelihoods = {0.17, 0.17, 0.17, 0.17, 0.17, 0.05, 0.05, 0.02, 0.02, 0.01};
    private final int[] imageResources = {R.drawable.dog_buddy, R.drawable.bunny_buddy, R.drawable.turtle_buddy, R.drawable.bear_buddy, R.drawable.frog_buddy, R.drawable.duck_buddy, R.drawable.panda_buddy, R.drawable.cat_buddy, R.drawable.capybara_buddy, R.drawable.blob_buddy};

    private final int[] imageResourcesBackdrop = {R.drawable.color_dog, R.drawable.color_bunny, R.drawable.color_turtle, R.drawable.color_bear, R.drawable.color_frog, R.drawable.color_duck, R.drawable.color_panda, R.drawable.color_cat, R.drawable.color_capybara, R.drawable.color_blob};
    private final int[] imageResourcesBlank = {R.drawable.blank_dog, R.drawable.blank_bunny, R.drawable.blank_turtle, R.drawable.blank_bear, R.drawable.blank_frog, R.drawable.blank_duck, R.drawable.blank_panda, R.drawable.blank_cat, R.drawable.blank_capybara, R.drawable.blank_blob};

    // Categorization of the rarity types in a HashMap
    private final Map<Double, String> rarity = new HashMap<Double, String>() {{
        put(0.17, "Common");
        put(0.05, "Rare");
        put(0.02, "Epic");
        put(0.01, "Legendary");
    }};

    // Getters for the global variables
    public String[] getWords() {
        return words;
    }

    public double[] getLikelihoods() {
        return likelihoods;
    }

    public int[] getImageResources() {
        return imageResources;
    }

    public int[] imageResourcesBackdrop() {
        return imageResourcesBackdrop;
    }

    public int[] imageResourcesBlank() {
        return imageResourcesBlank;
    }

    public Map<Double, String> getRarity() {
        return rarity;
    }
}
