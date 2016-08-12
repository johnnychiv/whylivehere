/**
 * @author Fiachra McVicker and Johnathon Chivers
 */
package uk.ac.qub.fmcvicker01.mapstest;


/**
 * list of imports required through out the class.
 * from android, google and java liberies.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Maps activity class extends extends AppCompatActivity and implements OnMapReadyCallback.
 * The class contains the on create method, on map ready ,on create creat options menu, pol tdata method,
 * hide data method, on optionsItemselected, on search, create seek bar, plot radius, add filters, mover camera,
 * add drawer items, set up drawer, on post create, on configfuration changed, show instructions and move to current local.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String jcTableName;
    private ArrayList<Marker> jcMarker;
    private int jcImage;
    private double jcRadius;
    private boolean crimeFirst = true;

    public enum filterNames {
        HOSPITALS, GPS, NURSERY, HEDU, CRIME, BUS, RAIL, TUBE;
    }


    /**
     * Google map variable for holding the map instance, which is a private instance within
     * the class.
     */
    private GoogleMap mMap;
    /**
     * A location variable for holding the current location for use throughout the class once it is assigned
     * a value. It is a private variable and therefore only visable within the class.
     */
    private Location CURRENT_LOCATION;
    /**
     * A circle variable for holding reference to an instantited cirlce object for use within the class across
     * various methods. It is private and tehrefore only visable within the class.
     */
    private Circle circle;
    /**
     * Two marker variables which are assigned values in different methods throughout the class. Both are
     * private and therefore only visiable within the class itself.
     */
    private Marker circleMarker, m;
    /**
     * A private int variable which holds the radius on the plotted circle. It is iniatlised with a value of 1500m
     * for the intial plot on the on map ready method.
     */
    private int radius = 500;
    /**
     * private variable for holding the latlang of the current location, which is used in serval methods
     * through out the class. It is private and therefore only available within the class.
     */
    private LatLng latLng;


    /**
     * Private array list for hospitals that holds type Marker whichis  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> hospitalsMarkers = new ArrayList<>();
    /**
     * Private array list for gp surgries that holds type Marker which is  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> gpSurgeriesMarkers = new ArrayList<>();
    /**
     * Private array list for preSchoolMarker that holds type Marker whichis  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> railMarkers = new ArrayList<>();
    /**
     * Private array list for nursarySchoolMarkers that holds type Marker whichis  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> nursarySchoolsMarkers = new ArrayList<>();
    /**
     * Private array list for primarySchoolMarkers that holds type Marker whichis  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> tubeMarkers = new ArrayList<>();
    /**
     * Private array list for postPrimarySchool which that type Marker which is  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> postPrimarySchoolsMarkers = new ArrayList<>();
    /**
     * Private array list for crimeDataMarker that holds type Marker which is  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> crimeDataMarkers = new ArrayList<>();
    /**
     * Private array list for busStopMarker that holds type Marker which is  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> busStopsMarkers = new ArrayList<>();
    /**
     * Private array list for universityMarker that holds type Marker which is  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> universitiesMarkers = new ArrayList<>();
    /**
     * Private array list for feCollegesMarker that holds type Marker which is  plotted using the data from
     * the appropriate ObjectsToPlot array list.
     */
    private ArrayList<Marker> feCollegesMarkers = new ArrayList<>();

    /**
     * Booleans which holds reference to whether the hospital data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean hospitalsVisable = false;
    /**
     * Booleans which holds reference to whether the gps data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean gpsVisable = false;
    /**
     * Booleans which holds reference to whether the pre-school data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean railStopsVisable = false;
    /**
     * Booleans which holds reference to whether the nursary school data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean nursarySchoolsVisable = false;
    /**
     * Booleans which holds reference to whether the primary school data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean tubeStopsVisable = false;
    /**
     * Booleans which holds reference to whether the post primary data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean postPrimarySchoolsVisable = false;
    /**
     * Booleans which holds reference to whether the crime data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean crimesVisable = false;
    /**
     * Booleans which holds reference to whether the bus stop data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean busStopsVisable = false;
    /**
     * Booleans which holds reference to whether the univeristy data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean universitiesVisable = false;
    /**
     * Booleans which holds reference to whether the fe college data
     * has been set visible by the user via the naviagtion draw.
     */
    private Boolean feCollegesVisable = false;

    /**
     * private variable of type seekbar which holds reference to the implemented
     * seek bar once it is instantiated in the code.
     */
    private SeekBar customSeekBar;

    /**
     * private ListView for holding the draw list once it is created
     */
    private ListView mDrawerList;
    /**
     * private ListView for holding the  mAdapter once it is created
     */
    private ArrayAdapter<String> mAdapter;
    /**
     * private ListView for holding draw toggle once it is created
     */
    private ActionBarDrawerToggle mDrawerToggle;
    /**
     * private draw layout for holding the draw layoutonce it is created
     */
    private DrawerLayout mDrawerLayout;
    /**
     * private string  for holding the applcication title
     */
    private String mActivityTitle;
    /**
     * private ImageButton for holding reference to the current location button on the map once
     * it is created
     */
    private ImageButton currentLocationBtn;

    /**
     * An overridden method which is the starting point of the application and must be implemented.
     * This is the first method that is executed when the app is started up, and as such should contain
     * the minimal amount of information possible to carry out this function.
     *
     * @param savedInstanceState - Bundle variable for the saved instance. automatiicaly imputed on start up
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // try catch block to handle exceptions in the onCreate method withstach trace print.
        try {
            // super class constructor which takes an argument of Bundle.
            super.onCreate(savedInstanceState);
            // links the main activity with the xml fiule
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            // assigns the navList xml to the mDrawerList variable as defined in the class variable list.
            mDrawerList = (ListView) findViewById(R.id.navList);
            // assigns the drawer_layput xml to the mDrawerlayout variable as defined in the class variable list.
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            // assigned the title to the mActivityTitle for use by naviagtion drawer as defined in the class variable list.
            mActivityTitle = getTitle().toString();
            // method which add the Drawer Items to the Naviagtion Drawer
            addDrawerItems();
            // method with set up the Naviagtion Drawer
            setupDrawer();
            // gets the button for the Navigation Drawer
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // gets the button for the Navigation Drawer
            getSupportActionBar().setHomeButtonEnabled(true);
            // create the seekbar listener
            createSeekBarListener();
            // assigns the current location button on map to the currentLocationBtn reference within
            //the clas svariable list.
            currentLocationBtn = (ImageButton) findViewById(R.id.current_location_button);
            // instatiate da mapOps class with reference to this activity and map.

        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isFirstTime = MyPreferences.isFirst(MapsActivity.this);
        if (isFirstTime) {
            showInstructions();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // assigning the class variable nmap with the current google map.
        mMap = googleMap;
        // sets the padding for the current google map so the google maps compass is displayed correctly
        // for the user on screen.
        mMap.setPadding(0, 220, 0, 0);
        // sets the google maps con[ass to visable
        mMap.getUiSettings().setCompassEnabled(true);
        // try catch block around code to get and assign the current location or last known location of the
        // user to the class variable. If location servies or last know location are not available there is
        // a default location of Queens University Belfast.
        try {
            // prevents the google map location as a blue triangle for appearing on screen, therefore allowing the application
            // to set its own marker.
            mMap.setMyLocationEnabled(false);
            // assigns the context to location manager in prepation for getting GPS location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // assigns the GPS location of available
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // assigns the netwoork location of available
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // sets the GPS location time to zero for use
            long GPSLocationTime = 0;
            // if the assigned gps location is not null then assign the
            // then assign the GPSLocation time variable with the time from
            // locationGPS
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }
            // set the Net work location variable to 0
            long NetLocationTime = 0;
            // if the assigned network location is not null then assign the
            // then assign the Network location time variable with the time from
            //  locationNet.
            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }
            // if the gps location - network location time is greater than zero, i.e. the
            // the gps location time is the most recent then assign this to current location.
            // Else assign the network location tot he current location variable as it is the most recent.
            if (0 < GPSLocationTime - NetLocationTime) {
                CURRENT_LOCATION = locationGPS;
            } else {
                CURRENT_LOCATION = locationNet;
            }
            // if the current location is not null, i.e. it has been assigned above then move the camera to
            // the current location.
            // Else assign a default location on Queens University Belfast and move the camera to this location.
            if (CURRENT_LOCATION != null) {
                latLng = new LatLng(CURRENT_LOCATION.getLatitude(), CURRENT_LOCATION.getLongitude());
                moveCamera();
                plotRadius();
            } else {
                LatLng defaultLatLng = new LatLng(54.5845, -5.9351);
                latLng = defaultLatLng;
                moveCamera();
                plotRadius();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * This overridden method creates the options menu on the top right of
     * the user application from the created xml. It contains setting, help
     * and about.
     *
     * @param menu - accepts a Menu object for use int he method
     * @return a boolean of true once complete
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // assigns the menu inflater to a variable
        MenuInflater inflater = getMenuInflater();
        //inflate accepts a parmeter of menu as passed into this method and
        // a menu resource xml
        inflater.inflate(R.menu.menu_main, menu);
        // returns a boolean of true when complete.
        return true;
    }

    /**
     * A method which plots the relevenat data from the selected user input. This includes the
     * type of data which is to be displayed and the size of the radius for the plotting.
     * It calls the database via a method in MapOps. MapOps is referenced in the mapOps variable.
     * The returnValues method in the MapOps class returns an array list of ObjectsToPlot. This array list
     * is then used to create the markers, which are ploted on the map.
     *
     * @param tableName -- Name of the table in which the data is contained in SQLite database
     * @param markers   -- An ArrayList of markers which will be or are plotted on the map.
     * @param image     -- a marker image from the drawable resource folder
     */
    public void plotData(filterNames tableName, ArrayList<Marker> markers, int image) {
        jcTableName = tableName.toString();
        jcMarker = markers;
        jcImage = image;
        jcRadius = circle.getRadius();
        new GetListTask().execute();
    }


    /**
     * This method removes the deselected data as defined by the user via the navigation drawer.
     * It uses an if statement to check a passed in parameter of type array list to determine what
     * what data is to be removed.
     *
     * @param markers - Array List of markers which are to be removed from the map.
     */
    public void hideData(ArrayList<Marker> markers) {
        // if the passed in arraylist is not empty then preform the action
        if (!markers.isEmpty()) {
            // loop to remove the data as defined in the array list
            for (int loop = 0; loop < markers.size(); loop++) {
                markers.get(loop).remove();
            }
            // clears the private class variable that is the array list for future reuse.
            markers.clear();
        }
    }


    /**
     * When a item from the options menu us selected the relevant action is performed.
     * The relevant action is a method.
     * It also acts as a listener for the Navigation Drawer
     *
     * @param item - tyope Menu Item i.e the about, help and setting buttons
     * @return a boolean onc complete.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Navigation Drawer listener
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // switch to performed depending on what item has been selected from the options menu
        switch (item.getItemId()) {
            case R.id.action_about:

                startActivity(new Intent(this, About.class));

                return true;
            case R.id.action_help:
                // shows instructions
                showInstructions();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is used to search an address which is inputted into by the user
     * and return the location of the most relevant address to the application and set the
     * latLag variable. It then calls the plot radius, addfilters, and move camera methods
     * to ajust the settings to the user defined ones on screen.
     *
     * @param view - accepts a View argument as predefined by the application
     */
    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.search_bar);
        String location = location_tf.getText().toString();

        if (location != null && !location.isEmpty()) {
            // list of type address set to null
            List<Address> addressList = null;

            try {
                // populate address list with geocoder based on search
                Geocoder geocoder = new Geocoder(this);
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // if the address list is not empty then get the 1st item in the array. i.e.
            // the first result and assign to address
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                // the private class variable latLng is assigned the latitude and longitude of
                // the first search result
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                // call the plot radius method to adjust
                plotRadius();
                // re-plot user defined filters
                addFilters();
                // move the camera to the search location
                moveCamera();
            }
        }
    }

    /**
     * on seek bar change listener which listens for changes in the seekbar as the user interacts with the app.
     */
    private SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        // as the user tracks the seek bar this method snaps to the closest 500m between 500-6000
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress >= 0 && progress <= 500) {
                seekBar.setProgress(500);
                progress = 500;
            } else if (progress > 500 && progress <= 1000) {
                seekBar.setProgress(1000);
                progress = 1000;
            } else if (progress > 1000 && progress <= 1500) {
                seekBar.setProgress(1500);
                progress = 1500;
            } else if (progress > 1500 && progress <= 2000) {
                seekBar.setProgress(2000);
                progress = 2000;
            } else if (progress > 2000 && progress <= 2500) {
                seekBar.setProgress(2500);
                progress = 2500;
            } else if (progress > 2500 && progress <= 3000) {
                seekBar.setProgress(3000);
                progress = 3000;
            } else if (progress > 3000 && progress <= 3500) {
                seekBar.setProgress(3500);
                progress = 3500;
            } else if (progress > 3500 && progress <= 4000) {
                seekBar.setProgress(4000);
                progress = 4000;
            } else if (progress > 4000 && progress <= 4500) {
                seekBar.setProgress(4500);
                progress = 4500;
            } else if (progress > 4500 && progress <= 5000) {
                seekBar.setProgress(5000);
                progress = 5000;
            }

            // sets the private class variable radius to the value in the seek bar
            radius = progress;
        }//end method onProgress`changed

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }//end method onStartTrackingTouch

        /**
         * Method is called once the user stop stracking.
         * It calls the replot radius method and addFilters methods.
         * It then displays a toast stating the current radius
         * @param seekBar type seekabar, i.e. the seekabr which is on screen
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // re-plot the radius
            plotRadius();
            // add the appropiate user defined filters
            addFilters();
            // set the toast to the radius in km.
            Toast radiusToast = Toast.makeText(getApplicationContext(), "Radius set to: " + (double) radius / 1000 + "km", Toast.LENGTH_SHORT);
            // show the toast
            radiusToast.show();

        } //end method onStopTrackingTouch
    }; //end OnSeekBarChangeListener

    /**
     * Create the seek bar listener and reference the raidusSeek in resources
     */
    public void createSeekBarListener() {
        // assign seek bar
        customSeekBar = (SeekBar) findViewById(R.id.radiusSeek);
        // register seekbar listen
        customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
    }

    /**
     * method plots the radius as defined be the user via the seek bar.
     * this is defaulted to 1500m via a class variable for application start up.
     * It determines whether a circle is present and removes it in order to replot it.
     */
    public void plotRadius() {
        // check to see if a cricle is plotted
        if (circle != null) {
            // removes circle from map
            circle.remove();
        }
        // check to see if circle marker is plotted
        if (circleMarker != null) {
            // removes circle marker from map
            circleMarker.remove();
        }
        // adds a new circle to the map based on user defined settings; radius and latlng class variables.
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(radius)
                .strokeColor(R.color.colorPrimary)
                .fillColor(Color.TRANSPARENT));
        // adds new circle marker to map based on user defined settings; latlng class variable.
        circleMarker = mMap.addMarker(new MarkerOptions().position(circle.getCenter()).title("Chosen Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        // set the ciecle marker visible
        circleMarker.setVisible(true);
    }

    /**
     * Adds the filters to the map after the user has selected them via the Naviagtion Drawer.
     * It checks the appropiate class boolean variable to see if the data is current set to be displayed.
     * It then removes the data and replots it according to the users position and radius settings.
     */
    public void addFilters() {
        // checks to see if the hospital data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (hospitalsVisable) {
            hideData(hospitalsMarkers);
            plotData(filterNames.HOSPITALS, hospitalsMarkers, R.drawable.health_medical);
        }
        // checks to see if the GPS data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (gpsVisable) {
            hideData(gpSurgeriesMarkers);
            plotData(filterNames.GPS, gpSurgeriesMarkers, R.drawable.medical);
        }
        // checks to see if the nursery school data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (nursarySchoolsVisable) {
            hideData(nursarySchoolsMarkers);
            plotData(filterNames.NURSERY, nursarySchoolsMarkers, R.drawable.play_schools);
        }
        // checks to see if the fe colleges data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (feCollegesVisable) {
            hideData(feCollegesMarkers);
            plotData(filterNames.HEDU, feCollegesMarkers, R.drawable.libraries);
        }
        // checks to see if the crime data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (crimesVisable) {
            hideData(crimeDataMarkers);
            plotData(filterNames.CRIME, crimeDataMarkers, R.drawable.employment);
        }
        // checks to see if the bus stop data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (busStopsVisable) {
            hideData(busStopsMarkers);
            plotData(filterNames.BUS, busStopsMarkers, R.drawable.transport);
        }
        // checks to see if the bus stop data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (railStopsVisable) {
            hideData(railMarkers);
            plotData(filterNames.RAIL, railMarkers, R.drawable.transport);
        }
        // checks to see if the bus stop data is visible via a class boolean. if it is true i.e. selected it removes
        // the current markers and replots the relevant markers.
        if (tubeStopsVisable) {
            hideData(tubeMarkers);
            plotData(filterNames.TUBE, tubeMarkers, R.drawable.transport);
        }
    }

    /**
     * Move camera method moves the camera to the current user postion based on the latlng class variable.
     */
    public void moveCamera() {
        // update the current mMap to to display the new latlang
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        // define the camera position as the new latlng as defined by the class varible
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(14)                   // Sets the zoom

                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        // moves the camera
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Thiis method adds the naviagtion drawer items from a string array to the drawer
     * and sets the onclick lister for the items.
     */
    private void addDrawerItems() {
        // String array of item names to be added
        String[] filtersArray = {"Hospitals", "GP Surgeries", "Nursery/Schools", "Higher Edu. Colleges", "Crime Data", "Bus", "Rail", "Tube/Metro"};
        // creates an ArrayAdapter of type string from the filters array, checkable item and a context of this
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, filtersArray);
        // sets the drawer list to tadapter
        mDrawerList.setAdapter(mAdapter);
        // allows for multiple schoice within the naviagtion drawer
        mDrawerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // onclick listener for the items in the naviagtion drawer
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * method which determines the action of the item which is selected/deseected by the
             * user by plotting or giding the data and setting the relevant class boolean appropiately.
             * @param parent -- The adapater view to be passed in
             * @param view -- the View
             * @param position -- position of item clicked
             * @param id -- long reference
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //If hospitals is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(0)) {
                    plotData(filterNames.HOSPITALS, hospitalsMarkers, R.drawable.health_medical);
                    hospitalsVisable = true;
                } else {
                    hideData(hospitalsMarkers);
                    hospitalsVisable = false;
                }
                //If Gps is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(1)) {
                    plotData(filterNames.GPS, gpSurgeriesMarkers, R.drawable.medical);
                    gpsVisable = true;
                } else {
                    hideData(gpSurgeriesMarkers);
                    gpsVisable = false;
                }
                //If nursey is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(2)) {
                    plotData(filterNames.NURSERY, nursarySchoolsMarkers, R.drawable.play_schools);
                    nursarySchoolsVisable = true;
                } else {
                    hideData(nursarySchoolsMarkers);
                    nursarySchoolsVisable = false;
                }
                //If fe colleges is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(3)) {
                    plotData(filterNames.HEDU, feCollegesMarkers, R.drawable.libraries);
                    feCollegesVisable = true;
                } else {
                    hideData(feCollegesMarkers);
                    feCollegesVisable = false;
                }
                //If crime is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(4)) {
                    plotData(filterNames.CRIME, crimeDataMarkers, R.drawable.employment);
                    crimesVisable = true;
                } else {
                    hideData(crimeDataMarkers);
                    crimesVisable = false;
                }
                //If bus stops is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(5)) {
                    plotData(filterNames.BUS, busStopsMarkers, R.drawable.transport);
                    busStopsVisable = true;
                } else {
                    hideData(busStopsMarkers);
                    busStopsVisable = false;
                }
                //If bus stops is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(6)) {
                    plotData(filterNames.RAIL, railMarkers, R.drawable.transport);
                    railStopsVisable = true;
                } else {
                    hideData(railMarkers);
                    railStopsVisable = false;
                }
                //If bus stops is select or deselected then perform the approiate action, Either
                // plot or hide data then change boolean accordingly.
                if (mDrawerList.isItemChecked(7)) {
                    plotData(filterNames.TUBE, tubeMarkers, R.drawable.transport);
                    tubeStopsVisable = true;
                } else {
                    hideData(tubeMarkers);
                    tubeStopsVisable = false;
                }


            }
        });
    }

    /**
     * This method is called to set oup the drawer layout of filters
     * as defined in the String array and the maps activity xml.
     */
    private void setupDrawer() {
        // set the drawer toggle to the actionbar toggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Filters");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // sets the indicator true for the drawer
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // set drawer listener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    /**
     * Overridden method that is ran after creation so the
     * current drawer state can be held in memory.
     *
     * @param savedInstanceState - Bundle of the instance
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // set the super for the saved state
        super.onPostCreate(savedInstanceState);
        // sync the draw toggle state
        mDrawerToggle.syncState();
    }

    /**
     * over ridden method which notes changes in the drawer configuration.
     *
     * @param newConfig - configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // set super to new configuration
        super.onConfigurationChanged(newConfig);
        // set to new changed configureation
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * This method shows the instructions from the instructions class.
     * They are displayed when the app is opened for the first time
     * or can be found in the options menu.
     */
    public void showInstructions() {
        // display instruction
        startActivity(new Intent(this, Instructions_1.class));
    }

    /**
     * Moves camera to current postion
     *
     * @param view - current view
     */
    public void moveToCurrentLocal(View view) {
        // calls the move camera method from this class.
        moveCamera();
    }

    /**
     * Async task class this calls the web server and recieves
     * a json back which is parsed and map markers created before returning to
     * the main thread
     */


    @SuppressWarnings("unchecked")
    private class GetListTask extends AsyncTask<String, String, String> {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;


        String tableName;
        ArrayList<Marker> markers;
        int image;
        double myRadius;
        private ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    GetListTask.this.cancel(true);
                }
            });
        }

        /**
         * ATTENTION
         * <p/>
         * THIS METHOD IS DOING ALL THE WORK FOR GETING DATA and ploting...
         *
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            tableName = jcTableName;
            markers = jcMarker;
            image = jcImage;
            myRadius = jcRadius;

            HttpURLConnection conn = null;
            //URL url = null;
            String phpURL = null;

            // goona need a switch on table name to pick url as string
            switch (tableName) {

                case "NURSERY":
                    phpURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude) + "&radius=" + Integer.toString(radius) + "&type=school&key=AIzaSyANsWnNz30mwHl7hDnXu6o44DPJehlvPUI";
                    break;
                case "HEDU":
                    phpURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude) + "&radius=" + Integer.toString(radius) + "&type=university&key=AIzaSyANsWnNz30mwHl7hDnXu6o44DPJehlvPUI";
                    break;
                case "CRIME":
                    phpURL = "http://whylivehere.co.uk/crime.php";
                    break;
                case "BUS":
                    phpURL = "http://whylivehere.co.uk/bus.php";
                    break;
                case "RAIL":
                    phpURL = "http://whylivehere.co.uk/rail.php";
                    break;
                case "TUBE":
                    phpURL = "http://whylivehere.co.uk/tube.php";
                    break;
                case "GPS":
                    phpURL = "http://whylivehere.co.uk/gp_surgeries.php";
                    break;
                case "HOSPITALS":
                    phpURL = "http://whylivehere.co.uk/hospitals.php";
                    break;
            }


            if (tableName.equals("NURSERY") || tableName.equals("HEDU")) {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL(phpURL);
                    URLConnection urlConnection = url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()), 8);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line + "\n");
                    }
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result = content.toString();
            }
            else if (tableName.equals("CRIME")) {

                try {
                    URL url = new URL(phpURL);
                    //Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("currentLatitude", Double.toString(latLng.latitude)).appendQueryParameter("currentLongitude", Double.toString(latLng.longitude)) .appendQueryParameter("distanceCircle", Integer.toString(radius));
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();


                    int response_code = conn.getResponseCode();
                    String res = Integer.toString(response_code);
                    Log.d("resonses", res);

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        //  Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder resultsb = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            resultsb.append(line);
                        }

                        Log.d("this", resultsb.toString());

                        // Pass data to onPostExecute method
                        return result = resultsb.toString();

                    } else {

                        return ("unsuccessful");
                    }

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }

            } else {


                try {
                    URL url = new URL(phpURL);
                    //Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("currentLatitude", Double.toString(latLng.latitude)).appendQueryParameter("currentLongitude", Double.toString(latLng.longitude));
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();


                    int response_code = conn.getResponseCode();
                    String res = Integer.toString(response_code);
                    Log.d("resonses", res);

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        //  Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder resultsb = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            resultsb.append(line);
                        }

                        Log.d("this", resultsb.toString());

                        // Pass data to onPostExecute method
                        return result = resultsb.toString();

                    } else {

                        return ("unsuccessful");
                    }

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }
            }





        }

        @Override
        protected void onPostExecute(String result) {


            if (tableName.equals("NURSERY") || tableName.equals("HEDU")) {
                // ambil data dari Json database
                try {
                    if (result != null) {

                        JSONObject JsonObj = new JSONObject(result);
                        JSONArray Jarray = JsonObj.getJSONArray("results");

                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject Jasonobject = null;
                            Jasonobject = Jarray.getJSONObject(i);
                            JSONObject Geometry = Jasonobject.getJSONObject("geometry");
                            JSONObject Location = Geometry.getJSONObject("location");
                            double Latitude = Location.getDouble("lat");
                            double Longitude = Location.getDouble("lng");

                            Location current = new Location("point a");
                            current.setLatitude(latLng.latitude);
                            current.setLongitude(latLng.longitude);
                            Location marker = new Location("Point b");
                            marker.setLatitude(Latitude);
                            marker.setLongitude(Longitude);
                            float distance = current.distanceTo(marker);
                            if (distance <= jcRadius) {
                                // plots the marker to map with the relevant icon for the resources folder
                                m = mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(Jasonobject.getString("name")).icon(BitmapDescriptorFactory.fromResource(jcImage)));
                                // inserts markers into an arrya list so there is a reference to clear and re plot as required.
                                jcMarker.add(m);
                            }
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("log_tag", "Error parsing data " + e.toString() + result + "yeoooooooo");
                    e.printStackTrace();
                }
            } else {

                try {
                    JSONArray Jarray = new JSONArray(result);
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject Jasonobject = null;
                        //text_1 = (TextView)findViewById(R.id.txt1);
                        Jasonobject = Jarray.getJSONObject(i);
                        Location current = new Location("point a");
                        current.setLatitude(latLng.latitude);
                        current.setLongitude(latLng.longitude);
                        Location marker = new Location("Point b");
                        marker.setLatitude(new Double(Jasonobject.getString("Latitude")));
                        marker.setLongitude(new Double(Jasonobject.getString("Longitude")));
                        float distance = current.distanceTo(marker);
                        if (distance <= jcRadius) {
                            // plots the marker to map with the relevant icon for the resources folder
                            m = mMap.addMarker(new MarkerOptions().position(new LatLng(new Double(Jasonobject.getString("Latitude")), new Double(Jasonobject.getString("Longitude")))).title(Jasonobject.getString("Name")).snippet(Jasonobject.getString("Description")).icon(BitmapDescriptorFactory.fromResource(jcImage)));
                            // inserts markers into an arrya list so there is a reference to clear and re plot as required.
                            jcMarker.add(m);
                        }
                    }

                    if(tableName.equals("CRIME")&&crimeFirst){
                        Toast crimeToast = Toast.makeText(getApplicationContext(), "Tap icon to iterate through crime types", Toast.LENGTH_SHORT);
                        crimeToast.setGravity(Gravity.CENTER,0,0);
                        crimeToast.show();
                        crimeFirst = false;
                    }


                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("log_tag", "Error parsing data " + e.toString() + result + "yeoooooooo");
                    e.printStackTrace();
                }

            }


                this.progressDialog.dismiss();
            }

        }
    }



