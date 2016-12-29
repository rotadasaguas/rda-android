package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencia_lista);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetAgencias().execute();


    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), AgenciasActivity.class);
        startActivityForResult(myIntent, 0);

        return true;
    }

    private class GetAgencias extends AsyncTask<Void, Void, Void> {

        Double coordenadasLat = 0.0;
        Double coordenadasLog = 0.0;

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

            String urlGeoCode = "https://maps.googleapis.com/maps/api/geocode/json?address=Estrada+Rio+do+Peixe+Km+9+Caminho+Turístico+do+Rio+do+Peixe+13960000+Socorro+SP";

            try {
                HttpHandler sh = new HttpHandler();
                String jsonGeoCode = sh.makeServiceCall(urlGeoCode);


                if (jsonGeoCode != null){
                    JSONObject jsonObject = new JSONObject(jsonGeoCode);

                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    JSONObject r = resultsArray.getJSONObject(0);

                    JSONObject n = r.getJSONObject("geometry").getJSONObject("location");

                    coordenadasLat = Double.parseDouble(n.getString("lat").toString());
                    coordenadasLog = Double.parseDouble(n.getString("lng").toString());

                    Log.d("Verificar", "Entrei" + coordenadasLat + " - " + coordenadasLog);

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

         /*   final List<Agencia> agencias = getAgencias();
            final ListView listView = (ListView) findViewById(R.id.comentariosLocal);
            listView.setAdapter(new AgenciaAdapter(AgenciaListaActivity.this, agencias));*/




        }
    }

    public List<Agencia> getAgencias(){

        List<Agencia> listAux = new ArrayList<>();

        /*for(int i = 0; i < autores.size(); i++){
            Comentario c = new Comentario(autores.get(i).toString(), datas.get(i).toString(), comentarios.get(i).toString());
            listAux.add(c);
        }*/
        return(listAux);
    }
}
