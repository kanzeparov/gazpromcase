package com.example.franck.mapchain.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franck.mapchain.R;
import com.example.franck.mapchain.adapters.MyAdapter;
import com.example.franck.mapchain.contracts.generated.MapChain;
import com.example.franck.mapchain.etherium.EtheriumRunner;
import com.example.franck.mapchain.fragments.FireMissilesDialogFragment;
import com.example.franck.mapchain.fragments.InfoButtonFragment;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolyline;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InfoActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = "INFOACTIVITYTAG";
    private Map map = null;
    private static MapRoute mapRoute = null;
    private MapFragment mapFragment = null;
    private boolean paused = true;
    private TextView textTitle;
    private static int countQuest = -1;
    private MapCircle m_circle;
    private String[] storyQuest = new String[] {
            "Go to this point, to save mr.Durov",
            "This is 1 point, you must to go there and find prices",
            "Sculptures near the house. The sculpture of the Russian and American writer\n" +
                    "English word in quotes",
            "Unexpectedly, there are no such cathedrals in Atlanta or Savannah. But there is one in Moscow. In the nearest public garden find a board with historical information",
            "A little less than 70,000 km² or just over 57,000 km². We will not argue which of the variants is correct.",
            "Complete Quest"
    };

    private TextView textDescription;
    private GeoCoordinate currentGeoCoordinate = new GeoCoordinate(55.779588, 37.6013881);
    private GeoCoordinate firstGeo = new GeoCoordinate(55.765386, 37.577550);
    private GeoCoordinate secondGeo = new GeoCoordinate(55.767255, 37.576788);
    private GeoCoordinate thirdGeo = new GeoCoordinate(55.767225, 37.579502);
    private GeoCoordinate startGeo = new GeoCoordinate(55.767031, 37.578376);
    private GeoCoordinate firstGeoQuest = new GeoCoordinate(55.766449, 37.577217);
    private GeoCoordinate secondGeoQuest = new GeoCoordinate(55.766049, 37.577982);
    private GeoCoordinate thirdGeoQuest = new GeoCoordinate(55.766554, 37.577839);
    private PositioningManager posManager;
    private boolean paid = false;
    private MyTask myTask;
    private MapPolyline m_polyline;
    private LocationManager locationManager;
    private Button infoButton;
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {
                public void onPositionUpdated(PositioningManager.LocationMethod method,
                        GeoPosition position, boolean isMapMatched) {
                    if (!paused) {
                        map.setCenter(position.getCoordinate(),
                                Map.Animation.NONE);
                    }
                }

                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                        PositioningManager.LocationStatus status) {
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        textDescription = findViewById(R.id.descriptionMap);
        textTitle = findViewById(R.id.titleMap);

        infoButton = findViewById(R.id.info_button);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.navigation);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoButtonFragment infoButtonFragment = new InfoButtonFragment();
                infoButtonFragment.show(getSupportFragmentManager(), "info");
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start:
                                initDialogFinish(getCurrentFocus());
                                break;
                            case R.id.action_pay:

//                                FireMissilesDialogFragment fireMissilesDialogFragment = new FireMissilesDialogFragment();
//                                fireMissilesDialogFragment.show(getSupportFragmentManager(), "few");
                                if (countQuest == -1) {
                                    initDialog(getCurrentFocus());
                                    break;
                                }
                                if (countQuest == 5) {
                                    break;
                                }
                                if (countQuest == 4) {
                                    initDialogFinish(getCurrentFocus());
                                    break;
                                } else {
                                    initDialogWithoutPaid(getCurrentFocus());
                                }

                                break;
                            case R.id.action_next:
                                if (paid) {
                                    paid = false;
                                    if (countQuest == 5) {
                                        Toast.makeText(getApplicationContext(), "You complete Quest", Toast.LENGTH_LONG);
                                        break;
                                    }
                                    if (countQuest == 0) {
//                                        myTask = new MyTask();
//                                        myTask.execute();
                                        actionNext();
                                        break;
                                    }

                                    if (countQuest == 4) {
                                        textDescription.setText(storyQuest[countQuest]);
                                        break;
                                    }
                                    if (countQuest == 1) {
                                        map.removeMapObject(m_circle);
                                        map.removeMapObject(mapRoute);
                                        setPoints(firstGeoQuest);
                                        break;
                                    }
                                    if (countQuest == 2) {
                                        setPoints(secondGeoQuest);
                                        break;
                                    }
                                    if (countQuest == 3) {
                                        setPoints(thirdGeoQuest);
                                        break;
                                    }
                                }

                                break;
                        }
                        return true;
                    }
                });
    }

    private void actionNext() {

        initState();

        routeMap();

    }

    private void initDialog(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoActivity.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title

        // Setting Dialog Message
        alertDialog.setMessage("Enter Password to pay 0.5 ETH");
        alertDialog.setTitle("Password Etherium");
        final EditText input = new EditText(InfoActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input); // uncomment this line
        //alertDialog.setView(input);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.name);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setCredentialClient(input.getText().toString());
                        countQuest++;
                        paid = true;
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(), "Password Matched", Toast.LENGTH_SHORT).show();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();
    }

    private void initDialogFinish(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoActivity.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title

        // Setting Dialog Message
        alertDialog.setMessage("Do you want finish?");

        //alertDialog.setView(input);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.name);
        alertDialog.setTitle("Finish");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(), "FINISHED", Toast.LENGTH_LONG).show();
                        countQuest++;
                        paid = true;
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
//                        returnMoney("123");
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();
    }

    private void initDialogWithoutPaid(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoActivity.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure?");

        //alertDialog.setView(input);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.name);
        alertDialog.setTitle("Next");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(), "Password Matched", Toast.LENGTH_SHORT).show();
                        countQuest++;
                        paid = true;
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        returnMoney("123");
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();
    }

    private void setCredentialClient(String password) {
        EtheriumRunner etheriumRunner = new EtheriumRunner();

        String filename = "UTC--2018-04-21T23-23-56_772048000Z--09a5dacb427cc8fd596e5b1640fa539dac1a5d6d";
        String string = EtheriumRunner.jsonClient;
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            etheriumRunner.init(password, new File(getApplicationContext().getFilesDir(), filename), "client");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnMoney(String password) {
        EtheriumRunner etheriumRunner = new EtheriumRunner();

        String filename = "UTC--2018-04-21T23-23-56_772048000Z--09a5dacb427cc8fd596e5b1640fa539dac1a5d6d";
        String string = EtheriumRunner.jsonClient;
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            etheriumRunner.returnMoney(password, new File(getApplicationContext().getFilesDir(), filename), getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPoints(final GeoCoordinate geoCoordinate) {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
//                                            GeoCoordinate position = new GeoCoordinate(locationTest.getLatitude(), locationTest.getLongitude());
                    // retrieve a reference of the map from the map fragment
                    addPoint(geoCoordinate);
                    textDescription.setText(storyQuest[countQuest]);
//
//                    createPolyline(firstGeo, secondGeo);
//                    createPolyline(secondGeo, thirdGeo);
//                    createPolyline(thirdGeo, firstGeo);

                } else {
                    Log.e(TAG, "Cannot initialize MapFragment (" + error + ")");
                }
            }
        });
    }

    private void addPoint(GeoCoordinate geoCoordinate) {
        map = mapFragment.getMap();
        // Set the map center coordinate to the Vancouver region (no animation)
//                                            Log.d(TAG, "ALT " + locationTest.getAltitude() + " " + locationTest.getLongitude());
        map.setCenter(geoCoordinate,
                Map.Animation.NONE);
        // Set the map zoom level to the average between min and max (no animation)
        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()));
        MapMarker marker = new MapMarker();
        PositioningManager posManager = PositioningManager.getInstance();
        posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);

        posManager.start(PositioningManager.LocationMethod.GPS);
        Log.d(TAG, "onEngineInitializationCompleted: " +
                posManager.hasValidPosition(PositioningManager.LocationMethod.GPS_NETWORK));
        boolean temp = posManager.hasValidPosition(PositioningManager.LocationMethod.GPS_NETWORK);
        Log.d(TAG, Boolean.toString(temp));

        marker.setCoordinate(geoCoordinate);
        map.addMapObject(marker);
    }

    private void routeMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
//                                            GeoCoordinate position = new GeoCoordinate(locationTest.getLatitude(), locationTest.getLongitude());
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center coordinate to the Vancouver region (no animation)
//                                            Log.d(TAG, "ALT " + locationTest.getAltitude() + " " + locationTest.getLongitude());
                    map.setCenter(currentGeoCoordinate,
                            Map.Animation.NONE);

                    MapMarker marker1 = new MapMarker();
                    PositioningManager posManager1 = PositioningManager.getInstance();
                    posManager1.start(PositioningManager.LocationMethod.GPS_NETWORK);

                    posManager1.start(PositioningManager.LocationMethod.GPS);
                    marker1.setCoordinate(startGeo);
                    map.addMapObject(marker1);

                    // 2. Initialize RouteManager
                    RouteManager routeManager = new RouteManager();

                    // 3. Select routing options
                    RoutePlan routePlan = new RoutePlan();

                    RouteOptions routeOptions = new RouteOptions();
                    routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
                    routeOptions.setRouteType(RouteOptions.Type.FASTEST);
                    routePlan.setRouteOptions(routeOptions);

                    // 4. Select Waypoints for your routes
                    // START: Nokia, Burnaby
                    routePlan.addWaypoint(currentGeoCoordinate);

                    // END: Airport, YVR
                    routePlan.addWaypoint(startGeo);

                    // 5. Retrieve Routing information via RouteManagerEventListener
                    RouteManager.Error error1 = routeManager.calculateRoute(routePlan, routeManagerListener);
                    if (error1 != RouteManager.Error.NONE) {
                        Toast.makeText(getApplicationContext(),
                                "Route calculation failed with: " + error.toString(), Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    Log.e(TAG, "Cannot initialize MapFragment (" + error + ")");
                }
            }

        });
    }

    private void initState() {
        Log.d(TAG, " action_pay");
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
//                                            GeoCoordinate position = new GeoCoordinate(locationTest.getLatitude(), locationTest.getLongitude());
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center coordinate to the Vancouver region (no animation)
//                                            Log.d(TAG, "ALT " + locationTest.getAltitude() + " " + locationTest.getLongitude());
                    map.setCenter(currentGeoCoordinate,
                            Map.Animation.NONE);
                    // Set the map zoom level to the average between min and max (no animation)
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()));
                    MapMarker marker = new MapMarker();
                    PositioningManager posManager = PositioningManager.getInstance();
                    posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);

                    posManager.start(PositioningManager.LocationMethod.GPS);
                    Log.d(TAG, "onEngineInitializationCompleted: " +
                            posManager.hasValidPosition(PositioningManager.LocationMethod.GPS_NETWORK));
                    boolean temp = posManager.hasValidPosition(PositioningManager.LocationMethod.GPS_NETWORK);
                    Log.d(TAG, Boolean.toString(temp));

                    marker.setCoordinate(currentGeoCoordinate);
                    map.addMapObject(marker);
                    createCircle(startGeo);
                    textDescription.setText(storyQuest[countQuest]);
//
//                    createPolyline(firstGeo, secondGeo);
//                    createPolyline(secondGeo, thirdGeo);
//                    createPolyline(thirdGeo, firstGeo);
                } else {
                    Log.e(TAG, "Cannot initialize MapFragment (" + error + ")");
                }
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        countQuest = -1;
    }

    private void initialize() {
        setContentView(R.layout.infoactivity);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
            @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                                permissions[index])) {
                            Toast.makeText(this,
                                    "Required permission " + permissions[index] + " not granted. "
                                            + "Please go to settings and turn on for sample app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this,
                                    "Required permission " + permissions[index] + " not granted",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                initialize();
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onResume() {
        super.onResume();
        PositioningManager.getInstance().addListener(
                new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
        paused = false;
        if (posManager != null) {
            Log.d(TAG, posManager.toString());
            posManager.start(
                    PositioningManager.LocationMethod.GPS_NETWORK);
        }
        textTitle.setText(getIntent().getExtras().getString("title"));
        textDescription.setText(getIntent().getExtras().getString("description"));
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center to the Vancouver region (no animation)
                    posManager = PositioningManager.getInstance();
                    PositioningManager.LocationStatus status = posManager.getLocationStatus(PositioningManager.LocationMethod.GPS);
                    Log.d(TAG, status.toString());
                    GeoPosition position = posManager.getPosition();

//                    Log.d(TAG, position.toString());
                    map.setCenter(new GeoCoordinate(55.752023, 37.617499, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    map.getPositionIndicator().setVisible(true);
                } else {
                    Log.e(TAG, "Cannot initialize MapFragment (" + error + ")");
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, " IN check");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);

    }

    // To pause positioning listener
    public void onPause() {
        if (posManager != null) {
            posManager.stop();
        }
        super.onPause();
        paused = true;
        locationManager.removeUpdates(locationListener);
    }

    // To remove the positioning listener
    public void onDestroy() {
        if (posManager != null) {
            // Cleanup
            posManager.removeListener(
                    positionListener);
        }
        map = null;
        super.onDestroy();
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                tvStatusGPS.setText("Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
//                tvStatusNet.setText("Status: " + String.valueOf(status));
            }
        }
    };

    private void showLocation(final Location location) {
        if (location == null) {
            Log.d(TAG, " null location");
            return;
        }
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, formatLocation(location));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            Log.d(TAG, formatLocation(location));
        }
    }

    private String formatLocation(Location location) {
        if (location == null) {
            return "";
        }
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    private RouteManager.Listener routeManagerListener = new RouteManager.Listener() {
        public void onCalculateRouteFinished(RouteManager.Error errorCode,
                List<RouteResult> result) {

            if (errorCode == RouteManager.Error.NONE && result.get(0).getRoute() != null) {
                // create a map route object and place it on the map
                mapRoute = new MapRoute(result.get(0).getRoute());
                map.addMapObject(mapRoute);

                // Get the bounding box containing the route and zoom in (no animation)
                GeoBoundingBox gbb = result.get(0).getRoute().getBoundingBox();
                map.zoomTo(gbb, Map.Animation.NONE, Map.MOVE_PRESERVE_ORIENTATION);
            } else {
            }
        }

        public void onProgress(int percentage) {

        }
    };

    private void createCircle(GeoCoordinate geoCoordinate) {
        // create a MapCircle centered at current location with radius 400
        m_circle = new MapCircle(400.0, geoCoordinate);
        m_circle.setLineColor(Color.BLUE);

        m_circle.setFillColor(Color.YELLOW);
//        m_circle.setFillColor(Color.GRAY);
        m_circle.setLineWidth(2);
        map.addMapObject(m_circle);
    }

    private void createPolyline(GeoCoordinate first, GeoCoordinate second) {
        // create boundingBox centered at current location
        GeoBoundingBox boundingBox = new GeoBoundingBox(map.getCenter(), 1000, 1000);
        // add boundingBox's top left and bottom right vertices to list of GeoCoordinates
        List<GeoCoordinate> coordinates = new ArrayList<GeoCoordinate>();
        coordinates.add(first);
        coordinates.add(second);
        // create GeoPolyline with list of GeoCoordinates
        GeoPolyline geoPolyline = new GeoPolyline(coordinates);
        m_polyline = new MapPolyline(geoPolyline);
        m_polyline.setLineColor(Color.BLUE);
        m_polyline.setLineWidth(12);
        // add GeoPolyline to current active map
        map.addMapObject(m_polyline);
    }

    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ETHR", "ok pre exec action");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TimeUnit.SECONDS.sleep(2);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
