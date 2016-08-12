/**
 * @author Fiachra McVicker and Johnathon Chivers
 */
package uk.ac.qub.fmcvicker01.mapstest;

/**
 * imports for class
 */
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to hold shared preference which determines
 * if app has been previously opened by user. If the
 * app has not been opened before a set of instructions
 * will appear on screen at start up.
 */
public class MyPreferences {

    /**
     * private class variable to hold a string name.
     */
    private static final String MY_PREFERENCES = "my_preferences";

    /**
     * Method returns a boolaen to determine whether the app has bee start or not before.
     * A decion is then made based in the return value as to show the instructions
     * or not.
     * @param context - context of application
     * @return - boolean
     */
    public static boolean isFirst(Context context){
        // gets shared preferences and assigns to reader variable
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        // if it is the first time it has been opned it assigns first as true
        final boolean first = reader.getBoolean("is_first", true);
        // if first is true, set to false
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        // return true or false
        return first;
    }
}
