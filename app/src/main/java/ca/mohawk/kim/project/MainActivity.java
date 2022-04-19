package ca.mohawk.kim.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 I, Stephen Kim, 000801165 certify that this material is my original work. No
 other person's work has been used without due acknowledgement.
 Link to demonstration: https://youtu.be/TV102EygD-4
 */

/**
 * Starting activity of libriarchiver application, initializes with web browsing list
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = "==MainActivity==";

    private DrawerLayout drawer;
    private static Activity currentActivity = null;
    private FragmentManager fm;

    /**
     * On-create for main activity, initializes navigation menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentActivity = this;

        if (savedInstanceState == null){
            switchMenu(NavMenu.BROWSE_WEB);
        }

        //initialize nav drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //sets up activity top toolbar and enables it as home icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //sets up listener for nav bar item click/selected
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        //ties together actionbar framework and drawer layout for nav drawer functionality
        ActionBarDrawerToggle actionBarToggle = new ActionBarDrawerToggle(this, drawer, (R.string.open), (R.string.close));
        drawer.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();
    }

    /**
     * Retrieves current global activity
     * @return
     */
    public static Activity getCurrentActivity(){
        return currentActivity;
    }

    /**
     * Switch fragments menu, based on navigation selected option
     * @param menu
     */
    public void switchMenu(NavMenu menu){
        this.fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;

        switch(menu){
            case BROWSE_LOCAL:
                fragmentTransaction = this.fm.beginTransaction();
                fragmentTransaction.replace(R.id.audiobookFragmentFrame, new BrowseLocal());
                fragmentTransaction.commit();
                Log.d(TAG, "Switched to browse local fragment");
                break;
            case BROWSE_WEB:
                fragmentTransaction = this.fm.beginTransaction();
                fragmentTransaction.replace(R.id.audiobookFragmentFrame, new BrowseWeb());
                fragmentTransaction.commit();
                Log.d(TAG, "Switched to browse web fragment");
                break;
            case FILTER_SETTINGS:
                fragmentTransaction = this.fm.beginTransaction();
                fragmentTransaction.replace(R.id.audiobookFragmentFrame, new FilterSettings());
                fragmentTransaction.commit();
                Log.d(TAG, "Switched to filter settings fragment");
                break;
            case EXPORT_DATA:
                Intent intent = new Intent(this, ExportData.class);
                intent.putExtra("exportedData", exportArchiveLinks());
                this.startActivity(intent);
                Log.d(TAG, "Switched export data activity");
                break;
            case CLEAR_LOCAL:
                clearLocalData();
                Toast cleared = Toast.makeText(this, "Successfully cleared local database!", Toast.LENGTH_SHORT);
                cleared.show();
                Log.d(TAG, "Switched clear local data method");
                break;
        }
    }

    /**
     * Onclick handler for navigation menu item selection
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_browse_web:
                switchMenu(NavMenu.BROWSE_WEB);
                break;
            case R.id.nav_browse_local:
                switchMenu(NavMenu.BROWSE_LOCAL);
                break;
            case R.id.nav_filter_settings:
                switchMenu(NavMenu.FILTER_SETTINGS);
                break;
            case R.id.nav_export_database:
                switchMenu(NavMenu.EXPORT_DATA);
                break;
            case R.id.nav_clear_local:
                switchMenu(NavMenu.CLEAR_LOCAL);
                break;
        }

        return false;
    }

    /**
     * Onclick handler for hamburger option menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find out the current state of the drawer (open or closed)
        boolean isOpen = drawer.isDrawerOpen(GravityCompat.START);

        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home button - open or close the drawer
                if (isOpen == true) {
                    drawer.closeDrawer(GravityCompat.START);
                    Log.d(TAG, "Closed Drawer");
                } else {
                    drawer.openDrawer(GravityCompat.START);
                    Log.d(TAG, "Opened Drawer");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Reads local database, extracts all archive download links and outputs to string for export data
     * @return
     */
    private String exportArchiveLinks(){
        AudiobookSQL dbHelper = new AudiobookSQL(this, AudiobookContract.AudiobookEntry.TABLE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String exportData = "";

        Cursor cursor = db.query(
                AudiobookContract.AudiobookEntry.TABLE_NAME,
                new String[]{AudiobookContract.AudiobookEntry.DOWNLOAD_LINK},
                null,
                null,
                null,
                null,
                null
                );

        try {
            while(cursor.moveToNext()){
                exportData += cursor.getString(
                        cursor.getColumnIndex(AudiobookContract.AudiobookEntry.DOWNLOAD_LINK)) +
                        "\n";
            }
        } finally {
            cursor.close();
        }

        Log.d(TAG, "Exported data:" + exportData);
        return exportData;
    }

    /**
     * clear local audiobook database
     */
    private void clearLocalData(){
        AudiobookSQL dbHelper = new AudiobookSQL(this, AudiobookContract.AudiobookEntry.TABLE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + AudiobookContract.AudiobookEntry.TABLE_NAME);

        Log.d(TAG, "Cleared Local database");
    }
}