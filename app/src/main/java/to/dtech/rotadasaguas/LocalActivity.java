package to.dtech.rotadasaguas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.MapboxAccountManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import to.dtech.rotadasaguas.domain.Local;
import to.dtech.rotadasaguas.domain.PlaceId;
import to.dtech.rotadasaguas.util.HttpConnection;


public class LocalActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));

        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_local);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

       callServer("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-22.593317,-46.528705&radius=500&name=SORVETREZE&key=AIzaSyAqPP51HO6FJIw2ZuSaHfxKqqNPtPXkMVA");

        // Create a mapView
        mapView = (MapView) findViewById(R.id.mapview);

       // mapView.setStyleUrl(Style.LIGHT);
        mapView.onCreate(savedInstanceState);

        // Add a MapboxMap
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                // marker view using all the different options available
                mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(-22.593317, -46.528705))
                        .anchor(0.5f, 0.5f)
                        .alpha(0.5f)
                        .title("Sorvetreze")
                        .infoWindowAnchor(0.5f, 0.5f)
                        .flat(true)
                );
            }
        });
    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MinhaRota.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void callServer(final String url){

        new Thread(){
            public void run(){
                try {
                    String data = HttpConnection.getSetDataWeb(url);
                    degeneratePlaceIdJSON(data);
                }catch (Exception e){
                    System.out.print("Erro na Conexão");
                }

            }
       }.start();

    }

    private String degeneratePlaceIdJSON(String data){

        String place = "";

        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray resultsArray = jsonObject.getJSONArray("results");
            JSONObject result = resultsArray.getJSONObject(0);
            String place_id = result.getString("place_id");

            Log.i("Script", "JAZAO: " + place_id);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return place;
    }

}
