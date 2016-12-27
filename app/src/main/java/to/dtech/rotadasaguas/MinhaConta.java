package to.dtech.rotadasaguas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MinhaConta extends AppCompatActivity {

    ListView configuracoesDaConta ;
    Button sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configuracoesDaConta = (ListView) findViewById(R.id.listView);

        String[] values = new String[] {
                "Atualizar dados pessoais",
                "Atualizar login",
                "Alterar senha",
                "Apagar Conta"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        configuracoesDaConta.setAdapter(adapter);

        // ListView Item Click Listener
        configuracoesDaConta.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue    = (String) configuracoesDaConta.getItemAtPosition(position);

                if (itemPosition == 0){
                    callListConfig(0);
                }
                if (itemPosition == 1){
                    callListConfig(1);
                }
                if (itemPosition == 2){
                    callListConfig(2);
                }
                if (itemPosition == 3){
                    callListConfig(3);
                }
                if (itemPosition == 4){
                    callListConfig(4);
                }

            }

        });

        sair = (Button) findViewById(R.id.logoff);
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                logoff();
            }
        });
    }


    public void callListConfig(int num){

        if (num == 0){
            Intent intent = new Intent( this, UpdateActivity.class );
            startActivity(intent);
            finish();
        }
        if (num == 1){
            Intent intent = new Intent( this, UpdateLoginActivity.class );
            startActivity(intent);
            finish();
        }
        if (num == 2){
            Intent intent = new Intent( this, UpdatePasswordActivity.class );
            startActivity(intent);
            finish();
        }
        if (num == 3){
            Intent intent = new Intent( this, RemoveUserActivity.class );
            startActivity(intent);
            finish();
        }

    }

    public void logoff(){
        FirebaseAuth.getInstance().signOut();
        finish();
        Toast.makeText(getApplicationContext(), "Logoff efetuado com sucesso!", Toast.LENGTH_LONG).show();
    }

    //BOTAO VOLTAR ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MinhaRota.class);
        startActivityForResult(myIntent, 0);

        return true;
    }
}
