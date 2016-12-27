package to.dtech.rotadasaguas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.daimajia.slider.library.Indicators.PagerIndicator;

public class AgenciasActivity extends AppCompatActivity {

    String[] cidadesAgencias = {"Socorro","Amparo","Serra Negra","Holambra",
            "Lindóia","Jaguariúna","Pedreira","Monte Alegre do Sul", "Águas de Lindóia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencias);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.lv_agencias_item, cidadesAgencias);

        ListView listView = (ListView) findViewById(R.id.lvAgencias);

        listView.setAdapter(adapter);
    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MinhaRota.class);
        startActivityForResult(myIntent, 0);

        return true;
    }
}
