package android.example.kittencode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {
    View view;                                              //Background-Veranderen

    //Informatie die word doorgestuurd naar de detail-page
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;        //Hier komt onze json data in.
    private RequestQueue mRequestQueue;                 //De RequestQ die je nodig hebt voor volley



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));  //Want we willen dat onze nieuwe items linear worden neegezet.

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
                                mExampleList.add(new ExampleItem(imageURL, creatorName, likeCount));
                            }

                            //Om recycleview te vullen hebben we een adapter nodig.
                            //Stuurt de json data van ExampleList door naar ExampleAdapter
                            mExampleAdapter = new ExampleAdapter(MainActivity.this, mExampleList);
                            //Stuurt de json data van ExampleAdapter door naar de RecyclerView waar het zichtbaar is voor ons.
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

    //Stuurt je door naar voor wanneer je op een item klikt.
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        ExampleItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikeCount());

        startActivity(detailIntent);
    }
}
