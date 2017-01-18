package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import to.dtech.rotadasaguas.adapter.AgenciaAdapter;
import to.dtech.rotadasaguas.adapter.ItensAdapter;
import to.dtech.rotadasaguas.domain.Agencia;
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;
import to.dtech.rotadasaguas.util.HttpHandler;

public class RotaSugeridaActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private ProgressDialog pDialog;
    private List<ItemLocal> mList;

    private String cidadeServer;
    private String rotaServer;

    public List<String> listaDeDadosNomes = new ArrayList<String>();
    public List<String> listaDeDadosDesc = new ArrayList<String>();
    public List<String> listaDeDadosEnd = new ArrayList<String>();

    public String urlServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rota_sugerida_lista);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentOld = getIntent();
        cidadeServer = intentOld.getStringExtra("cidade");
        rotaServer = intentOld.getStringExtra("rota");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_rota_sugerida);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);



        ImageView imgSad = (ImageView) findViewById(R.id.imagemSad);
        TextView txtSad = (TextView) findViewById(R.id.textoSad);

        urlServer = "http://siqueiradg.com.br/rotadasaguas/ws-rota/index.php?c=Locais&s=listarLocaisRotas&id=" + rotaServer + "&cidade=" + cidadeServer;

        try {
            mList = getLocaisLazer(urlServer);

            Log.d("ERRO", "LISTA: " + mList);

            if (mList.size() > 0){
                imgSad.setVisibility(View.GONE);
                txtSad.setVisibility(View.GONE);
            }

            ItensAdapter adapter = new ItensAdapter(getApplicationContext(), mList);
            adapter.setRecyclerViewOnClickListenerHack(this);
            mRecyclerView.setAdapter( adapter );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), DestaqueActivity.class);
        startActivityForResult(myIntent, 0);
        finish();

        return true;
    }

    public List<ItemLocal> getLocaisLazer(final String args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> futureResult = executor.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {

                HttpHandler sh = new HttpHandler();
                String retorno;

                String dadosWS = sh.makeServiceCall(args);

                Log.d("ERRO: ", "LISTA: " + dadosWS);

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

                JSONArray resultsArray = jsonObject.getJSONArray("locais");

                JSONObject r;

                System.out.print(resultsArray.length());

                for (int i = 0; i < resultsArray.length(); i++){
                    r = resultsArray.getJSONObject(i);

                        if (r.getString("cidade").equalsIgnoreCase(cidadeServer)) {
                            listaDeDadosNomes.add(r.getString("nome"));
                            listaDeDadosDesc.add(r.getString("descricao"));
                            listaDeDadosEnd.add(r.getString("rua").replace(" ", "+") + "+" + r.getString("cidade") + ",SP" + "," +  "Brasil" );
                        }

                }

            } catch (final Exception e) {
                Log.e("SCRIPT", "Json parsing error: " + e.getMessage());
            }
        }else{

        }

        executor.shutdown();

        List<ItemLocal> listAux = new ArrayList<>();

        for(int i = 0; i <= listaDeDadosNomes.size()-1; i++){
            ItemLocal c = new ItemLocal( listaDeDadosNomes.get(i), listaDeDadosDesc.get(i), listaDeDadosEnd.get(i) );
            listAux.add(c);
        }
        return(listAux);


    }

    @Override
    public void onClickListener(View view, int position) {

    }
}
