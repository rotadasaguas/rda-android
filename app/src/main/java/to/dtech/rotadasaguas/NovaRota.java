package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import to.dtech.rotadasaguas.domain.util.LibraryClass;

public class NovaRota extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // criando o Array de String
    private static final String[] opcoes = { "Socorro", "Águas de Lindóia", "Serra Negra", "Monte Alegre do Sul", "Amparo", "Jaguariúna", "Holambra", "Pedreira", "Lindóia" };
    ArrayAdapter<String> aOpcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_rota);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = new Intent(NovaRota.this, SubAlimentacaoActivity.class);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        aOpcoes = new ArrayAdapter<String>(this, R.layout.spinner_item, opcoes);
        aOpcoes.setDropDownViewResource(R.layout.spinner_dropdown_item);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCidades);
        spinner.setAdapter(aOpcoes);

        Button button = (Button) findViewById(R.id.botaoCriarAlimentacao);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String valorSpinner = spinner.getSelectedItem().toString();
                intent.putExtra("cidade", valorSpinner);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_destaques) {
            Intent intent = new Intent(NovaRota.this, DestaqueActivity.class);

            startActivity(intent);
        }else if (id == R.id.nav_meusLocais) {
            Intent intent = new Intent(NovaRota.this, MeusLocaisActivity.class);

            startActivity(intent);
        }else if (id == R.id.nav_minhaRota) {
            Intent intent = new Intent(NovaRota.this, MinhaRota.class);

            startActivity(intent);
        } else if (id == R.id.nav_cidades) {
            Intent intent = new Intent(NovaRota.this, CidadesActivity.class);

            startActivity(intent);

        }else if (id == R.id.nav_minhaconta) {
            Intent intent = new Intent(NovaRota.this, MinhaConta.class);

            startActivity(intent);

        } else if (id == R.id.nav_avalie) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
