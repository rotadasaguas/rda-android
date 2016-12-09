package to.dtech.rotadasaguas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import to.dtech.rotadasaguas.adapter.CommentAdapter;
import to.dtech.rotadasaguas.adapter.TagSubAdapter;
import to.dtech.rotadasaguas.domain.Comentario;
import to.dtech.rotadasaguas.domain.Tag;
import to.dtech.rotadasaguas.util.HttpHandler;

public class LocalActivity extends AppCompatActivity  implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private MapView mapView;
    private ProgressDialog pDialog;

    public String nomeLocal;
    public String coordenadas;

    public String urlBusca;

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));

        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_local);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        nomeLocal = "Sorvetreze";
        coordenadas = "-22.593317,-46.528705";

        urlBusca = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + coordenadas + "&radius=500&name=" + nomeLocal + "&key=AIzaSyAqPP51HO6FJIw2ZuSaHfxKqqNPtPXkMVA";

        new GetLocal().execute();

        final List<Comentario> comentarios = getComentarios();

        final ListView listView = (ListView) findViewById(R.id.comentariosLocal);
        listView.setAdapter(new CommentAdapter(this, comentarios));

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for(String name : url_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

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

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MinhaRota.class);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
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


    private class GetLocal extends AsyncTask<Void, Void, Void> {
        private String placeName = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LocalActivity.this);
            pDialog.setMessage("Carregando...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String place_id = null;


            // Making a request to url and getting response
            String jsonIdLocal = sh.makeServiceCall(urlBusca);

            if (jsonIdLocal != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonIdLocal);

                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    JSONObject result = resultsArray.getJSONObject(0);
                    place_id = result.getString("place_id");

                    Log.i("Script", "LOCAL_ID: " + place_id);
                } catch (final Exception e) {
                    Log.e("SCRIPT", "Json parsing error: " + e.getMessage());
                }
                if (place_id != null){
                    String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=AIzaSyAqPP51HO6FJIw2ZuSaHfxKqqNPtPXkMVA";
                    String jsonLocal = sh.makeServiceCall(url);
                    if (jsonLocal != null){
                        Log.i("Script", "passei no IF ");
                        try {
                            Log.i("Script", "entrei no try");
                            JSONObject jsonObject = new JSONObject(jsonLocal);

                            JSONObject result = jsonObject.getJSONObject("result");
                            placeName = result.getString("name");


                        }catch (final Exception e){

                        }
                    }
                }else{

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Sem conexão de dados!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            TextView textView = (TextView) findViewById(R.id.titulo_local);
            textView.setText(placeName);
        }


    }

    public List<Comentario> getComentarios(){
        String[] autores = new String[]{"Jose Pinheiro", "Douglas"};
        String[] comentarios = new String[]{"Muito Bom!", "Lugar maravilhoso!"};
        List<Comentario> listAux = new ArrayList<>();

        for(int i = 0; i <= autores.length; i++){
            Comentario c = new Comentario( autores[i % autores.length], comentarios[i % comentarios.length]);
            listAux.add(c);
        }
        return(listAux);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


}
