package com.example.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Search extends AppCompatActivity{

    RadioButton personRadioButton;
    RadioButton locationRadioButton;
    AutoCompleteTextView searchValue;
    Button addTag;
    Button removeTag;
    ListView tagsView; // list of tags?
    Button andSearch;
    Button orSearch;
    ListView searchResults;
    Button back;
    Button displayPhoto;

    // added
    public String type;
    //The matching tags in original form, eg "person=sesh"
    public ArrayList<String> matches;
    // THe matching photos
    public ArrayList<Photo> searchResult;

    private int selectedTagPosition = -1; // for remove_tag
    public int targetTagIndex = -1; // for display photo

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        personRadioButton = findViewById(R.id.person_radio_button); // radioButton
        locationRadioButton = findViewById(R.id.location_radio_button); // radioButton
        searchValue = findViewById(R.id.search_value_edit_text); // authoCompleitionTextView
        addTag = findViewById(R.id.add_tag); // button
        removeTag = findViewById(R.id.remove_tag); // button
        tagsView = findViewById(R.id.tags); // listView
        andSearch = findViewById(R.id.and_search); // button
        orSearch = findViewById(R.id.or_search); // button
        searchResults = findViewById(R.id.searchResults); // listView
        back = findViewById(R.id.back); // button
        displayPhoto = findViewById(R.id.displayPhoto); // button

        // for remove_tag
        tagsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTagPosition = position;
            }
        });

        // for display
        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the text of the clicked item
                String clickedItemName = parent.getItemAtPosition(position).toString();
                // Set the text of the button to "Open" + clickedItemName
                displayPhoto.setText("Open " + clickedItemName);
                targetTagIndex = position;
            }
        });

        // auto completion
        searchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                String tagType = personRadioButton.isChecked() ? "Person: " : "Location: ";
                List<String> suggestions = getAutoCompleteSuggestions(query, tagType);
                AutocompleteSuggestionAdapter adapter = new AutocompleteSuggestionAdapter(Search.this, suggestions);
                searchValue.setAdapter(adapter);
            }
        });

        searchValue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedSuggestion = (String) parent.getItemAtPosition(position);
                    searchValue.setText(selectedSuggestion);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Log the error message or display a toast message
                    //Toast.makeText(Search.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // auto completion
    private List<String> getAutoCompleteSuggestions(String query, String tagType) {
        List<String> suggestions = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (Album album : Album.albums) {
            for (Photo photo : album.getPhotos()) {
                for (String tag : photo.getTags()) {
                    if (tag.toLowerCase().startsWith(tagType.toLowerCase() + lowercaseQuery)) {
                        suggestions.add(tag);
                    }
                }
            }
        }

        return new ArrayList<>(new HashSet<>(suggestions));
    }

    private class AutocompleteSuggestionAdapter extends ArrayAdapter<String> {
        private List<String> suggestions;

        public AutocompleteSuggestionAdapter(Context context, List<String> suggestions) {
            super(context, android.R.layout.simple_dropdown_item_1line, suggestions);
            this.suggestions = suggestions;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(suggestions.get(position));
            return view;
        }
    }

    private List<String> tags = new ArrayList<>(); // Declare a list to store tags

    public void add_tag(View view) {
        String text = searchValue.getText().toString().trim();
        if (text.isEmpty()) { // make sure value is entered
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show());
            return;
        }


        // check for Person or Location type
        String tagType;
        if (personRadioButton.isChecked()) {
            //tagType = "Person: ";
        } else if (locationRadioButton.isChecked()) {
            //tagType = "Location: ";
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Please select one of the radio buttons", Toast.LENGTH_SHORT).show());
            return;
        }

        String tag = text; // tagType + text;
        String lowercaseTag = tag.toLowerCase(); // Convert the new tag to lowercase

        boolean tagExists = false;
        for (Album album : Album.albums) {
            for (Photo photo : album.getPhotos()) {
                for (String photoTag : photo.getTags()) {
                    if (photoTag.equalsIgnoreCase(tag)) {
                        tagExists = true;
                        break;
                    }
                }
                if (tagExists) {
                    break;
                }
            }
        }

        if (tagExists) {
            // Check if the tag already exists (case-insensitive)
            if (!tags.stream().map(String::toLowerCase).anyMatch(t -> t.equals(lowercaseTag))) {
                tags.add(tag);
                updateTagsList(); // Update the ListView with the new tag
                searchValue.setText(""); // Clear the EditText
            } else {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Tag already exists", Toast.LENGTH_SHORT).show());
            }
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Wrong format or Tag combination does not exist", Toast.LENGTH_SHORT).show());
        }

        // hide keyboard after add_tag button hit
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addTag.getWindowToken(), 0);
    }
    public void remove_tag(View view){
        if (selectedTagPosition != -1) {
            tags.remove(selectedTagPosition);
            selectedTagPosition = -1;
            updateTagsList();
        }
    }

    public void and_search(View view) {
        searchResult = new ArrayList<Photo>();
        matches = new ArrayList<String>();

        for (Album album : Album.albums) {
            for (Photo photo : album.getPhotos()) {
                boolean containsAllTags = true;
                for (String tag : tags) {
                    if (!photo.getTags().contains(tag)) {
                        containsAllTags = false;
                        break;
                    }
                }
                if (containsAllTags) {
                    searchResult.add(photo);
                    matches.addAll(photo.getTags());
                }
            }
        }

        // Check size of ArrayList. If zero, display a toast message
        if (searchResult.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No photos found containing all the tags", Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(andSearch.getWindowToken(), 0);

        loadToList();
        Toast.makeText(getApplicationContext(), "Found " + searchResult.size() + " Result(s)", Toast.LENGTH_SHORT).show();
    }

    public void back(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void or_search(View view){
        // implmementation
        // display all photos in searchResults if photo contains at least 1 tag in tag list
        searchResult = new ArrayList<Photo>();
        matches = new ArrayList<String>();
        //type = (personRadioButton.isChecked()?"Person:":"Location:")+" ";
        for(Album album : Album.albums){ // check all albums
            for(Photo photo : album.getPhotos()){ // check all photos in each album
                tagLoop:for(String tag : photo.getTags()){ // for all tags in each photo
                    if(tags.contains(tag)){ // if tag is in tags list
                        // attempt to add the image into the result
                        for(Photo p : searchResult){
                            if(p.getName().equals(photo.getName())){
                                break tagLoop;
                            }
                        }
                        searchResult.add(photo); // arrayList<photo>

                        for(String s : matches){
                            if(s.equals(tag)){
                                break tagLoop;
                            }
                        }
                        matches.add(tag);
                        break;
                    }
                }
            }
        }
        // check size of arraylist. if zero, toast message
        if(searchResult.size()<=0){
            Toast.makeText(getApplicationContext(), "No photos found under the tag(s)", Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(andSearch.getWindowToken(), 0);

        loadToList();
        Toast.makeText(getApplicationContext(), "Found "+matches.size()+ " Result(s)", Toast.LENGTH_SHORT).show();

    }

    public void loadToList() {
        ArrayList<String> temp = new ArrayList<>();
        for (Photo photo : searchResult) {
            // Add all tags of each photo to the temp list
            temp.addAll(photo.getTags());
        }
        // Remove duplicate tags
        ArrayList<String> uniqueTags = new ArrayList<>(new HashSet<>(temp));
        // Pass the new ArrayList<String> to the ArrayAdapter
        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, uniqueTags);
            searchResults.setAdapter(adapter);
        });
    }

    public void display(View view){
        if(targetTagIndex==-1){
            Toast.makeText(getApplicationContext(), "Please select a tag result", Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedTag = searchResults.getItemAtPosition(targetTagIndex).toString();

        Album tempAlb = new Album("Search by " + selectedTag);

        for(Photo p : searchResult){
            if(p.getTags().contains(selectedTag)){
                tempAlb.addPhoto(p);
            }
        }

        if(tempAlb.getPhotos().isEmpty()){
            Toast.makeText(getApplicationContext(), "No photos found for the selected tag", Toast.LENGTH_SHORT).show();
            return;
        }

        // display photos of this temporary album
        AlbumPage.currAlbum = tempAlb;
        AlbumPage.tempAlb = true;

        // Switch scene/stage
        Intent intent = new Intent(this, AlbumPage.class);
        startActivity(intent);
    }

    private void updateTagsList() {
        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tags);
            tagsView.setAdapter(adapter);
        });

    }

}

