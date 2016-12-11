package to.dtech.rotadasaguas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import to.dtech.rotadasaguas.adapter.CommentAdapter;
import to.dtech.rotadasaguas.domain.Comentario;
import to.dtech.rotadasaguas.fragment.MapFragmentLocal;
import to.dtech.rotadasaguas.util.HttpHandler;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class LocalActivity extends AppCompatActivity  implements OnMapReadyCallback, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, ObservableScrollViewCallbacks {

    private ProgressDialog pDialog;

    public String nomeLocal;
    public String coordenadasLog;
    public String coordenadasLat;

    private GoogleMap map;
    private MapFragmentLocal mMapFragmentLocal;

    public String urlBusca;

    private SliderLayout mDemoSlider;

    public HashMap<String,String> imgGoogle = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_local);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        String titulo = "Dados do Local";
        SpannableString s = new SpannableString(titulo);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, titulo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scLocal);
        scrollView.setScrollViewCallbacks(this);
        ActionBar ab = getSupportActionBar();
        ab.hide();

        nomeLocal = "Sorvetreze";
        coordenadasLat = "-22.593317";
        coordenadasLog = "-46.528705";

        urlBusca = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + coordenadasLat + "," + coordenadasLog + "&radius=500&name=" + nomeLocal + "&key=AIzaSyAqPP51HO6FJIw2ZuSaHfxKqqNPtPXkMVA";

        new GetLocal().execute();

        final List<Comentario> comentarios = getComentarios();

        final ListView listView = (ListView) findViewById(R.id.comentariosLocal);
        listView.setAdapter(new CommentAdapter(this, comentarios));

        //GOOGLE MAPS
        mMapFragmentLocal = MapFragmentLocal.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.llMap, mMapFragmentLocal)
                .commit();

        mMapFragmentLocal.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap map) {

        double l = Double.parseDouble(coordenadasLat);
        double g = Double.parseDouble(coordenadasLog);
        LatLng city = new LatLng(l,g);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 18));

        map.addMarker(new MarkerOptions()
                .title(nomeLocal)
                .rotation(10)
                .position(city));

    }
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        ActionBar ab = getSupportActionBar();

        if (scrollY > 80){
            Window w = getWindow();
            ab.show();
            ab.setDisplayShowTitleEnabled(true);
            w.setStatusBarColor(Color.parseColor("#333333"));
        }
        else if(scrollY < 80){
            Window w = getWindow();
            ab.hide();
            w.setStatusBarColor(Color.parseColor("#333333"));
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
       /* ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }*/
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

    private class GetLocal extends AsyncTask<Void, Void, Void> {
        private String placeName = "";
        private HashMap<String,String> imgGoogleAux = new HashMap<String, String>();

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

                            JSONArray photosGoogle = result.getJSONArray("photos");

                            for (int i = 0; i < photosGoogle.length(); i++){
                                String photoHash = photosGoogle.getJSONObject(i).getString("photo_reference");
                                imgGoogleAux.put("Imagem "+i, "https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=" + photoHash + "&key=AIzaSyAqPP51HO6FJIw2ZuSaHfxKqqNPtPXkMVA");
                            }

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

            imgGoogle = imgGoogleAux;

            mDemoSlider = (SliderLayout)findViewById(R.id.slider);

            for(String name : imgGoogle.keySet()){
                DefaultSliderView textSliderView = new DefaultSliderView(LocalActivity.this);
                // initialize a SliderLayout
                textSliderView
                        .image(imgGoogle.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(LocalActivity.this);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(6000);
            mDemoSlider.addOnPageChangeListener(LocalActivity.this);
        }


    }

    public List<Comentario> getComentarios(){
        String[] autores = new String[]{"Jose Pinheiro", "Douglas"};
        String[] datas = new String[]{"dom, 22 de Nov de 2015", "Sex, 05 de Nov de 2016"};
        String[] comentarios = new String[]{"Muito Bom!", "Lugar maravilhoso para ir com a familia, meus filhos adoraram!"};
        List<Comentario> listAux = new ArrayList<>();

        for(int i = 0; i < autores.length; i++){
            Comentario c = new Comentario( autores[i % autores.length], datas[i % datas.length], comentarios[i % comentarios.length]);
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
