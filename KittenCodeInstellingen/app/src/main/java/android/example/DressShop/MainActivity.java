package android.example.DressShop;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShopAdapter.OnItemClickListener {
    View view;                                              //Background-Veranderen

    //Informatie die word doorgestuurd naar de detail-page
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private RecyclerView mRecyclerView;
    private ShopAdapter mExampleAdapter;
    private ArrayList<ShopItem> mExampleList;        //Hier komt onze json data in.
    private RequestQueue mRequestQueue;                 //De RequestQ die je nodig hebt voor volley

    private RelativeLayout RL;

    //Zorgen dat alle foto's op volgorde omlaag staat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lay-out veranderen bij intstellingen
        RL = findViewById(R.id.relativeLayout);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));  //Onze nieuwe items linear worden neegezet.

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);                   //We krijgen een nieuwe requestQ waar we onze json request in kunnen voegen.
        parseJson();

    }

    //Creeeren van Menu-Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    //Checkt welke kleur van de menu-bar is aangeklikt.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                Intent canvas = new Intent(this, Canvas.class);
                startActivity(canvas);

            case R.id.item2:
                String google = "https://www.zalando.nl/dameskleding-jurken/only/";
                Uri webaddress = Uri.parse(google);                 //Verander de string in een URI
                Intent GoToGoogle = new Intent(Intent.ACTION_VIEW, webaddress); //Bij de nieuwe intent creeeren we een Action View die we het URL mee sturen.

                //Check of er een App op de telefoon staat die deze actie kan doen
                if(GoToGoogle.resolveActivity(getPackageManager()) != null){    //Als er iets is op de telefoon die deze actie kan uitvoeren, geef dat ding dan de activity.
                    startActivity(GoToGoogle);
                }

            case R.id.item3:
                Intent canva = new Intent(this, SettingsActivity.class);
                startActivity(canva);

            case R.id.subitem1:
                Toast.makeText(this, "LightMode selected", Toast.LENGTH_SHORT).show();
                view = this.getWindow().getDecorView();
                view.setBackgroundResource(R.color.white);
                return true;
            case R.id.subitem2:
                Toast.makeText(this, "DarkMode selected", Toast.LENGTH_SHORT).show();
                view = this.getWindow().getDecorView();
                view.setBackgroundResource(R.color.black);
                return true;
            case R.id.subitem3:
                Toast.makeText(this, "YoloMode selected", Toast.LENGTH_SHORT).show();
                view = this.getWindow().getDecorView();
                view.setBackgroundResource(R.color.red);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //De code voor de API
    private void parseJson(){
        //Waar we onze Json vandaan halen
        //https://pixabay.com/api/?key={ KEY }&q=yellow+flowers&image_type=photo
        //{Key} is je eigen toegangscode, q= "wat je wilt vinden"
        String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=dress&image_type=photo";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits"); //Hits omdat de json-data in "hits" staat.

                            //for-loop om alle data eruit te krijgen.
                            // Stuurt Json data naar mExampleList
                            for (int i = 0; i<jsonArray.length(); i++){
                                //Haalt het json object eruit.
                                JSONObject hit = jsonArray.getJSONObject(i);

                                //Haalt value uit object.
                                String creatorName = hit.getString("user");
                                String imageURL = hit.getString("webformatURL");
                                int likeCount = hit.getInt("likes");

                                //Voeg de item toe aan de lijst
                                mExampleList.add(new ShopItem(imageURL, creatorName, likeCount));
                            }

                            //Om recycleview te vullen hebben we een adapter nodig.
                            //Stuurt de json data van ExampleList door naar ShopAdapter
                            mExampleAdapter = new ShopAdapter(MainActivity.this, mExampleList);
                            //Stuurt de json data van ShopAdapter door naar de RecyclerView waar het zichtbaar is voor ons.
                            mRecyclerView.setAdapter(mExampleAdapter);

                            //Voor Click-Handler
                            mExampleAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    //Stuurt je door naar item-pagina.
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        ShopItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikeCount());

        startActivity(detailIntent);
    }

    //Wanneer je terug komt bij de app
    @Override
    protected void onResume() {
        if (SettingsActivity.CHANGE_BC(this)) {
            //if setting changed change background color
            RL.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        } else if (!(SettingsActivity.CHANGE_BC(this))) {
            RL.setBackgroundColor(ContextCompat.getColor(this, R.color.black));

        }
        super.onResume();
    }



}
