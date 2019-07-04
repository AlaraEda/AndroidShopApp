package android.example.DressShop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.example.DressShop.MainActivity.EXTRA_CREATOR;
import static android.example.DressShop.MainActivity.EXTRA_LIKES;
import static android.example.DressShop.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {
    //Hier moeten de buttons voor GPS
    //Declare button and textview
    private Button button;
    private TextView textView;
    private LocationManager locationManager;        //Instance Manager
    private LocationListener locationListener;      //Location Listener, listens for location changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);  //Alt +Enter zorgt ervoor dat je het import
        int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_like_detail);

        //Wat je ziet
        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
        textViewLikes.setText("Likes: " + likeCount);

        //Toekenning voor GPS
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        //Initaliseer een locatie manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //Vraag om de locatie service van de telefoon
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Word geroepen wanneer de locatie is geupdate
                textView.append("\n " + location.getLatitude() + " " + location.getLongitude());    //Schrijf je GPS coordinaten
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //Word gebruikt wanneer de GPS afgesloten is
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);    //Stuur user naar de settingspanel
                startActivity(intent);
            }
        };

        //Checkt om permision voor het gebruik van de Location Manager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Vraag toestemming voor internet en locatie
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET

            }, 10); //RequestCode.
            return;
        }else{
            //anders ga door
            configureButton();
        }

    }

    //Results from location are stored here
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:    //De code die we bij het checken voor permission hadden opgeschreven
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    //Als we toestemming hebben roep deze functie
                    configureButton();
                return;
        }
    }


    private void configureButton(){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Location Request                                      refresh/5s    location>0m update    roep onze listener op
                locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
            }
        });
    }

}
