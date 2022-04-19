package ca.mohawk.kim.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Filter options for the web browse list
 */
public class FilterSettings extends Fragment {
    public static final String TAG = "==FilterSettings==";

    private EditText selectTotalTimeMax;
    private Spinner selectLanguage;
    private EditText selectYear;
    public static int currentYear;
    public ArrayList<String> filterYears;
    private Button saveFilterSettings;
    private String language;
    private int totalTimeSecsMax;
    private int copyrightYear;

    public FilterSettings() {
        // Required empty public constructor
    }

    /**
     * Initialize settings view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View filterView = inflater.inflate(R.layout.fragment_filter_settings, container, false);

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        filterYears = new ArrayList<String>();
        for (int i = 1800; i <= currentYear; i++){
            filterYears.add(Integer.toString(i));
        }

        //initialize widgets
        selectYear = (EditText) filterView.findViewById(R.id.selectYear);
        selectTotalTimeMax = (EditText) filterView.findViewById(R.id.selectTotalTime);
        selectLanguage = (Spinner) filterView.findViewById(R.id.selectLanguage);
        saveFilterSettings = (Button) filterView.findViewById(R.id.saveFilterSettings);

        saveFilterSettings.setOnClickListener(this::saveFilterSettingsOnClick);

        //populate widgets
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.filterLanguages, android.R.layout.simple_spinner_dropdown_item);
        selectLanguage.setAdapter(languageAdapter);

        // retrieve settings options to pre-fill settings
        SharedPreferences sharedPreference = MainActivity.getCurrentActivity().getSharedPreferences("libriarchive", Context.MODE_PRIVATE);
        language = sharedPreference.getString("language", "English");
        totalTimeSecsMax = sharedPreference.getInt("totalTimeSecsMax", 1000000);
        copyrightYear = sharedPreference.getInt("copyrightYear", 2000);

        selectLanguage.setSelection(languageAdapter.getPosition(language));
        selectYear.setText(copyrightYear +"");
        selectTotalTimeMax.setText(totalTimeSecsMax+"");

        // Inflate the layout for this fragment
        return filterView;
    }

    /**
     * Save filter settings - button onclick handler
     * @param view
     */
    public void saveFilterSettingsOnClick(View view){
        int selectedCopyrightYear;
        String selectedLanguage;
        int selectedTotalTimeSecsMax;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("libriarchive", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

        if (!selectLanguage.getSelectedItem().toString().isEmpty()){
            selectedLanguage = selectLanguage.getSelectedItem().toString();
            sharedEditor.putString("language", selectedLanguage);
        }
        if (!selectYear.getText().toString().isEmpty()){
            selectedCopyrightYear = Integer.parseInt(selectYear.getText().toString());
            sharedEditor.putInt("copyrightYear", selectedCopyrightYear);
        }
        if (!selectTotalTimeMax.getText().toString().isEmpty()){
            selectedTotalTimeSecsMax = Integer.parseInt(selectTotalTimeMax.getText().toString());
            sharedEditor.putInt("totalTimeSecsMax", selectedTotalTimeSecsMax);
        }

        sharedEditor.commit();

        Toast settingsSaved = Toast.makeText(MainActivity.getCurrentActivity(), "Settings Saved!", Toast.LENGTH_SHORT);
        settingsSaved.show();

        Log.d(TAG, "Save filter settings button clicked");
    }
}