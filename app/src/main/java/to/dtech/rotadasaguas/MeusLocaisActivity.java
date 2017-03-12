package to.dtech.rotadasaguas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.pedant.SweetAlert.SweetAlertDialog;
import to.dtech.rotadasaguas.adapter.ItensAdapter;
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.domain.util.LibraryClass;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;
import to.dtech.rotadasaguas.util.HttpHandler;

public class MeusLocaisActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private SweetAlertDialog pDialog;
    private RecyclerView mRecyclerView;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    public static List<ItemLocal> listPlaceDetail;
    public static List<String> listPlaceSearch = new ArrayList<String>();

    private DatabaseReference mDatabase;

    public static int ESTADO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_locais);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_alimentacao);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("ESTADO", "entrei no onStart");
        //FIREBASE INIT
        listPlaceSearch.add("");
        mDatabase = LibraryClass.getFirebase();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("favoritos").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SweetAlertDialog ps;
                        ps = new SweetAlertDialog(MeusLocaisActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        ps.getProgressHelper().setBarColor(Color.parseColor("#0066FF"));
                        ps.setTitleText("Carregando");
                        ps.setCancelable(false);
                        ps.show();
                        listPlaceSearch.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            listPlaceSearch.add(data.getValue().toString());
                        }
                        ps.dismiss();


                        new ProcessaDadosWSFavoritos().execute();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), DestaqueActivity.class);
        startActivityForResult(myIntent, 0);

        return true;
    }

    @Override
    public void onClickListener(View view, int position) {

    }

    //tarefas em background
    public class ProcessaDadosWSFavoritos extends AsyncTask<String,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new SweetAlertDialog(MeusLocaisActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066FF"));
            pDialog.setTitleText("Carregando");
            pDialog.setContentText("Calma! Isso pode demorar um pouco :)");
            pDialog.setCancelable(true);
            pDialog.show();

            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    onBackPressed();
                }
            });
        }

        @Override
        protected Integer doInBackground(String... strings) {
            if (!listPlaceSearch.isEmpty()){
                try {
                    listPlaceDetail = null;
                    if (listPlaceDetail == null){
                        listPlaceDetail = getPlaceDetails(listPlaceSearch);
                    }

                }catch (Exception e){

                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);


            ImageView imgSad = (ImageView) findViewById(R.id.imagemSad);
            TextView txtSad = (TextView) findViewById(R.id.textoSad);

            if (listPlaceDetail == null){
                imgSad.setVisibility(View.VISIBLE);
                txtSad.setVisibility(View.VISIBLE);
            }else{
                ItensAdapter adapter = new ItensAdapter(getApplicationContext(), listPlaceDetail);
                adapter.setRecyclerViewOnClickListenerHack(MeusLocaisActivity.this);
                adapter.clearLista(listPlaceDetail.size());

                mRecyclerView.setAdapter( adapter );
            }

                pDialog.dismiss();

        }


        public List<ItemLocal> getPlaceDetails(final List<String> listona) throws ExecutionException, InterruptedException {

            final List<String> listaDeDadosNome = new ArrayList<String>();
            final List<String> listaDeDadosID = new ArrayList<String>();
            final List<String> listaDeDadosEnd = new ArrayList<String>();
            final List<String> listaDeDadosRating = new ArrayList<String>();
            final List<String> listaDeDadosFoto = new ArrayList<String>();

            ExecutorService executor = Executors.newSingleThreadExecutor();

            Future<List<ItemLocal>> futureResult = executor.submit(new Callable<List<ItemLocal>>() {


                @Override
                public List<ItemLocal> call() throws Exception {

                    HttpHandler sh = new HttpHandler();
                    List<ItemLocal> retorno;
                    String auxPlace;
                    String tmpRating;

                    String auxArray = "";
                    for (int i = 0; i < listona.size(); i++){
                        auxPlace = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + listona.get(i) + "&key=AIzaSyCvLptUUleUij6Bu5wsUcgBN5punqYO1Wo&language=pt-BR";

                        auxArray = sh.makeServiceCall(auxPlace);

                        if (auxArray != null) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(auxArray);
                                JSONObject result = jsonObject.getJSONObject("result");
                                tmpRating = result.getString("rating");

                                if (!tmpRating.equals("")){
                                    if (Float.parseFloat(tmpRating) > 3){
                                        listaDeDadosNome.add(result.getString("name"));
                                        listaDeDadosID.add(result.getString("place_id"));
                                        listaDeDadosEnd.add(result.getString("formatted_address"));
                                        listaDeDadosRating.add(result.getString("rating"));
                                        JSONArray photosGoogle = result.getJSONArray("photos");
                                        String photoHash = photosGoogle.getJSONObject(0).getString("photo_reference");
                                        if (photoHash.equals(" ") || photoHash == null){
                                            listaDeDadosFoto.add("http://www.freeiconspng.com/uploads/no-image-icon-32.png");
                                        }else{
                                            listaDeDadosFoto.add(photoHash);
                                        }
                                    }
                                }
                            } catch (final Exception e) {
                                Log.e("Bug: ", e.toString());
                            }
                        }

                    }

                    List<ItemLocal> listAux = new ArrayList<>();

                    for(int i = 0; i <= listaDeDadosNome.size()-1; i++){
                        try {
                            ItemLocal c = new ItemLocal( listaDeDadosNome.get(i), listaDeDadosEnd.get(i), listaDeDadosID.get(i), listaDeDadosFoto.get(i), listaDeDadosRating.get(i) );
                            listAux.add(c);
                        }catch (Exception e){

                        }

                    }

                    if (listAux.size() > 0){
                        retorno = listAux;
                    }
                    else{
                        retorno = null;
                    }

                    return retorno;

                }
            });

            //Obtendo um resultado da execucão da Thread
            List<ItemLocal> resultado = futureResult.get();

            if (resultado != null){
                return resultado;
            }

            return null;

        }


    }


}
