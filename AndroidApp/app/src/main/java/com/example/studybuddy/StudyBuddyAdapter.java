package com.example.studybuddy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// StudyBuddyAdapter.java
// Class to control the StudyBuddy image list-view on the user's screen
public class StudyBuddyAdapter extends RecyclerView.Adapter<StudyBuddyAdapter.ViewHolder> {
    private List<String> studyBuddies;
    private List<String> uniqueBuddies;
    private StudyBuddies sb = new StudyBuddies();
    private String[] words = sb.getWords();
    private int[] imageResourcesBackdrop = sb.imageResourcesBackdrop();

    public StudyBuddyAdapter(List<String> uniqueBuddies) {
        this.uniqueBuddies = uniqueBuddies;
        CurrentUser current = new CurrentUser();
        User currUser = current.getCurrentUser();
        studyBuddies = currUser.getStudyBuddies();
    }

    public static <T> int count(List<T> list, T target) {
        int total = 0;
        for (T element : list) {
            if (element.equals(target)) {
                total++;
            }
        }
        return total;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_buddy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0 || position >= uniqueBuddies.size()) {
            return;
        }

        String studyBuddy = uniqueBuddies.get(position);
        holder.textViewName.setText(studyBuddy);
        holder.textViewQuantity.setText("  x" + String.valueOf(count(studyBuddies, studyBuddy)));

        int imageResourceIndex = getImageResource(studyBuddy);

        Log.d("StudyBuddyAdapter", "Study Buddy: " + studyBuddy + ", Image Resource Index: " + imageResourceIndex);
        holder.imageViewStudyBuddy.setImageResource(imageResourcesBackdrop[imageResourceIndex]);
    }

    @Override
    public int getItemCount() {
        return uniqueBuddies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewStudyBuddy;
        public TextView textViewName;
        public TextView textViewQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewStudyBuddy = itemView.findViewById(R.id.imageViewStudyBuddy);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }

    // Method to get the image resource for the given study buddy
    private int getImageResource(String studyBuddy) {
        int index = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(studyBuddy)) {
                index = i;
                break;
            }
        }

        return index;
    }
}