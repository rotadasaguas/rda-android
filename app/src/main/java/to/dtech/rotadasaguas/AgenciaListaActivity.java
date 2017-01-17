package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import to.dtech.rotadasaguas.adapter.AgenciaAdapter;
import to.dtech.rotadasaguas.adapter.CommentAdapter;
import to.dtech.rotadasaguas.domain.Agencia;
import to.dtech.rotadasaguas.domain.Comentario;
import to.dtech.rotadasaguas.util.HttpHandler;

public class AgenciaListaActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    public String cidadeServer;
    public List<String> listaDeDadosNomes = new ArrayList<String>();
    public List<String> listaDeDadosDesc = new ArrayList<String>();
    public List<String> listaDeDadosEnd = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencia_lista);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentOld = getIntent();
        cidadeServer = intentOld.getStringExtra("cidade");

        new GetAgencias().execute();


    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), AgenciasActivity.class);
        startActivityForResult(myIntent, 0);

        return true;
    }

    private class GetAgencias extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AgenciaListaActivity.this);
            pDialog.setMessage("Carregando...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            String urlWS = "http://siqueiradg.com.br/rotadasaguas/ws-rota/index.php?c=Locais&s=agencias";

            try {
                HttpHandler sh = new HttpHandler();
                String jsonGeoCode = sh.makeServiceCall(urlWS);


                if (jsonGeoCode != null){
                    JSONObject jsonObject = new JSONObject(jsonGeoCode);

                    JSONArray resultsArray = jsonObject.getJSONArray("agencias");
                    JSONObject r;


                    for (int i = 0; i < resultsArray.length(); i++){
                        r = resultsArray.getJSONObject(i);

                        if (r.getString("cidade").equalsIgnoreCase(cidadeServer)) {
                            listaDeDadosNomes.add(r.getString("nome"));
                            listaDeDadosDesc.add("Telefone(s): " + r.getString("descricao"));
                            listaDeDadosEnd.add(r.getString("rua") + ", " + r.getString("num_end") + ", " + r.getString("bairro"));
                        }

                    }
                    Log.d("Verificar", listaDeDadosNomes.toString());
                    Log.d("Verificar", listaDeDadosDesc.toString());
                    Log.d("Verificar", listaDeDadosEnd.toString());

                }
                else{
                    //algum erro rolou
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            ImageView imgSad = (ImageView) findViewById(R.id.imagemSad);
            TextView txtSad = (TextView) findViewById(R.id.textoSad);

            final List<Agencia> agencias = getAgencias();
            final ListView listView = (ListView) findViewById(R.id.lvAgenciasItens);
            listView.setAdapter(new AgenciaAdapter(AgenciaListaActivity.this, agencias));
            if (agencias.size() > 0){
                imgSad.setVisibility(View.GONE);
                txtSad.setVisibility(View.GONE);
            }
            else{
                listView.setVisibility(View.GONE);
            }


        }
    }

    public List<Agencia> getAgencias(){

        List<Agencia> listAux = new ArrayList<>();

       for(int i = 0; i < listaDeDadosNomes.size(); i++){
          // Agencia c = new Agencia("teste", "teste", "teste");
            Agencia c = new Agencia(listaDeDadosNomes.get(i).toString(), listaDeDadosDesc.get(i).toString(), listaDeDadosEnd.get(i).toString());
            listAux.add(c);
        }
        return(listAux);
    }
}
