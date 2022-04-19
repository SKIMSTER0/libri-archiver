package ca.mohawk.kim.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Activity that contains links to all download url of local audiobook database, seperated by newline
 */
public class ExportData extends AppCompatActivity {
    public static final String TAG = "==ExportData==";

    private TextView exportedDataText;
    private String exportedData;

    /**
     * On-create for export data activity
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_data);

        exportedDataText = findViewById(R.id.exportedData);
        //make scrollable
        exportedDataText.setMovementMethod(new ScrollingMovementMethod());

        //retrieve intent with export data
        Intent getIntent = getIntent();
        exportedData = getIntent.getStringExtra("exportedData");

        if (exportedData == null || exportedData.isEmpty()){
            exportedDataText.setText(R.string.dataPlaceholder);
            Log.d(TAG, "Export data not available!");
        } else{
            exportedDataText.setText(exportedData);
            Log.d(TAG, "Export data shown");
        }
    }
}