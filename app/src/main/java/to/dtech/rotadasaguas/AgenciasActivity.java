package to.dtech.rotadasaguas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.google.android.gms.vision.text.Text;

import java.util.concurrent.ExecutionException;

public class AgenciasActivity extends AppCompatActivity {

    String[] cidadesAgencias = {"Socorro","Amparo","Serra Negra","Holambra",
            "Lindóia","Jaguariúna","Pedreira","Monte Alegre do Sul", "Águas de Lindóia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencias);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.lv_agencias_item, cidadesAgencias);

        ListView listView = (ListView) findViewById(R.id.lvAgencias);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AgenciaListaActivity.class);
                intent.putExtra("cidade", adapter.getItem(position).toString());
                startActivity(intent);

            }
        });
    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), DestaqueActivity.class);
        startActivityForResult(myIntent, 0);

        return true;
    }
}
