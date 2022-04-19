package ca.mohawk.kim.project;

import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Defines a single audiobook item in the librivox api
 */
public class Audiobook {
    public String id;
    public String title;
    public String description;
    public String url_text_source;
    public String language;
    public ArrayList<Author> authors;
    public Integer totaltimesecs;
    public String copyrightYear;
    public String urlZipFile;

    /**
     * Retrieves the first author if multiple authors
     * @return
     */
    public String getAuthors(){
        if (authors.size() <= 0){
            return("No Author Found");
        }
        return(authors.get(0).firstName + " " + authors.get(0).lastName);
    }

    /**
     * Converts time in seconds to minute format
     * @return
     */
    public String getTimeInMinutes(){
        String minutes = String.format("%.2f", totaltimesecs / 60d);
        return minutes + " Minutes";
    }
}
