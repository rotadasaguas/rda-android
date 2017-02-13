package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import to.dtech.rotadasaguas.adapter.AgenciaAdapter;
import to.dtech.rotadasaguas.adapter.ItensAdapter;
import to.dtech.rotadasaguas.domain.Agencia;
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;
import to.dtech.rotadasaguas.util.HttpHandler;

public class RotaSugeridaActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private SweetAlertDialog pDialog;



    private String cidadeServer;
    private String rotaServer;
    private String localizacao;
    private String gostos;

    public List<String> listaDeDadosNome = new ArrayList<String>();
    public List<String> listaDeDadosID = new ArrayList<String>();
    public List<String> listaDeDadosEnd = new ArrayList<String>();

    public List<String> mListAux = new ArrayList<String>();


    public List<String> listPlaceIds = new ArrayList<String>();


    public String urlServer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rota_sugerida_lista);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new GetData().execute();



    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), DestaqueActivity.class);
        startActivityForResult(myIntent, 0);
        finish();

        return true;
    }


    public List<ItemLocal> getPlaceDetails(final List<String> listona) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<ItemLocal>> futureResult = executor.submit(new Callable<List<ItemLocal>>() {


            @Override
            public List<ItemLocal> call() throws Exception {

                HttpHandler sh = new HttpHandler();
                List<ItemLocal> retorno;
                String auxPlace;
                String auxArray = "";
                for (int i = 0; i < listona.size(); i++){
                    auxPlace = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + listona.get(i) + "&key=AIzaSyCvLptUUleUij6Bu5wsUcgBN5punqYO1Wo&language=pt-BR";

                    auxArray = sh.makeServiceCall(auxPlace);

                    if (auxArray != null) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(auxArray);
                            JSONObject result = jsonObject.getJSONObject("result");

                            listaDeDadosNome.add(result.getString("name"));
                            listaDeDadosID.add(result.getString("place_id"));
                            listaDeDadosEnd.add(result.getString("formatted_address"));
                        } catch (final Exception e) {
                            Log.e("Bug: ", e.toString());
                        }
                    }

                }

                List<ItemLocal> listAux = new ArrayList<>();

                for(int i = 0; i <= listaDeDadosNome.size()-1; i++){
                    ItemLocal c = new ItemLocal( listaDeDadosNome.get(i), listaDeDadosEnd.get(i), listaDeDadosID.get(i) );
                    listAux.add(c);
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

    public List<String> getPlaceIDs(final String args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> futureResult = executor.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {

                HttpHandler sh = new HttpHandler();
                String retorno;

                String dadosWS = sh.makeServiceCall(args);


                if (dadosWS != null){
                    retorno = dadosWS;
                }
                else{
                    retorno = null;
                }
                return retorno;
            }
        });

        //Obtendo um resultado da execucão da Thread
        String resultado = futureResult.get();

        if (resultado != null){
            try {
                JSONObject jsonObject = new JSONObject(resultado);

                JSONArray resultsArray = jsonObject.getJSONArray("results");

                JSONObject r;

                for (int i = 0; i < resultsArray.length(); i++){
                    r = resultsArray.getJSONObject(i);
                    listPlaceIds.add(r.getString("place_id"));
                }

            } catch (final Exception e) {
                Log.e("SCRIPT", "Json parsing error: " + e.getMessage());
            }
        }else{

        }

        executor.shutdown();

        List<String> listAux = new ArrayList<>();

        for(int i = 0; i <= listPlaceIds.size()-1; i++){
            listAux.add(listPlaceIds.get(i));
        }
        return(listAux);


    }


    @Override
    public void onClickListener(View view, int position) {

    }


    public String getLocalizacao(String cidade){

        String loc = "";

        if (cidade.equalsIgnoreCase("Socorro")){
            loc = "-22.5951525,-46.5446116";
        }else if(cidade.equalsIgnoreCase("Holambra")){
            loc = " ";
        }else if(cidade.equalsIgnoreCase("Lindóia")){
            loc = " ";
        }else if(cidade.equalsIgnoreCase("Monte Alegre do Sul")){
            loc = " ";
        }else if(cidade.equalsIgnoreCase("Pedreira")){
            loc = " ";
        }else if(cidade.equalsIgnoreCase("Águas de Lindóia")){

        }else if(cidade.equalsIgnoreCase("Amparo")){

        }else if(cidade.equalsIgnoreCase("Serra Negra")){

        }else if(cidade.equalsIgnoreCase("Jaguariúna")){

        }

        return loc;
    }

    public String getValoresRota(String numero){

        String valor = "";

        if (numero.equals("1")){
            valor = "parques|clubes|natureza";
        }else if(numero.equals("2")){
            valor = "casa+noturna|boates";
        }else if(numero.equals("3")){
            valor = "lojas|presentes";
        }else if(numero.equals("4")){
            valor = "turismo";
        }else if(numero.equals("5")){
            valor = "natureza";
        }else if(numero.equals("6")){
            valor = "esportes+radicais";
        }else if(numero.equals("7")){
            valor = "restaurantes|lanchonetes";
        }else if(numero.equals("8")){
            valor = "bar|bares";
        }

        return valor;

    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        public RecyclerView mRecyclerView;
        public List<ItemLocal> mList;

        public ItensAdapter adapter;
        public int v = 0;

        ImageView imgSad = (ImageView) findViewById(R.id.imagemSad);
        TextView txtSad = (TextView) findViewById(R.id.textoSad);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new SweetAlertDialog(RotaSugeridaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066FF"));
            pDialog.setTitleText("Carregando");
            pDialog.setCancelable(false);
            pDialog.show();

            mRecyclerView = (RecyclerView) findViewById(R.id.rv_rota_sugerida);
            mRecyclerView.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(llm);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            Intent intentOld = getIntent();
            cidadeServer = intentOld.getStringExtra("cidade");
            rotaServer = intentOld.getStringExtra("rota");
            localizacao = getLocalizacao(cidadeServer);
            gostos = getValoresRota(rotaServer);

            try {
                urlServer = "https://maps.googleapis.com/maps/api/place/radarsearch/json?keyword="+ gostos +"&location="+ localizacao + "&radius=8000&key=AIzaSyDi_3eGNw22HQfvV4Dfh__-GBCUxOLxdx8&language=pt-BR";
                mListAux = getPlaceIDs(urlServer);
                mList = getPlaceDetails(mListAux);
                v = 1;

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
               // pDialog.dismiss();
            }


            if (v == 1) {
                pDialog.dismiss();
                if (mList == null){
                    imgSad.setVisibility(View.VISIBLE);
                    txtSad.setVisibility(View.VISIBLE);
                }else {
                    imgSad.setVisibility(View.GONE);
                    txtSad.setVisibility(View.GONE);
                    adapter = new ItensAdapter(getApplicationContext(), mList);
                    adapter.setRecyclerViewOnClickListenerHack(RotaSugeridaActivity.this);
                    mRecyclerView.setAdapter(adapter);
                }
            }


        }



    }
}
