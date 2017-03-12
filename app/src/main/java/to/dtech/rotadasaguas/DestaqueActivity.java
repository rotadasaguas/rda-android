package to.dtech.rotadasaguas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import to.dtech.rotadasaguas.adapter.RotaSugeridaAdapter;
import to.dtech.rotadasaguas.domain.Comentario;
import to.dtech.rotadasaguas.domain.RotaSugerida;
import to.dtech.rotadasaguas.domain.util.LibraryClass;

public class DestaqueActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    // criando o Array de String
    private static final String[] opcoes = { "Socorro", "Águas de Lindóia", "Serra Negra", "Monte Alegre do Sul", "Amparo", "Jaguariúna", "Holambra", "Pedreira", "Lindóia" };
    ArrayAdapter<String> aOpcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destaques);

        Iconify
                .with(new FontAwesomeModule());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_destaques);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SLIDER
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Moinho Povos Unidos \nHolambra - SP", "http://www.siqueiradg.com.br/rotadasaguas/images/holambra-moinho.jpg");
        url_maps.put("Disneylândia dos Robôs \nSerra Negra - SP", "http://www.siqueiradg.com.br/rotadasaguas/images/serra-negra-disneylandia.jpg");
        url_maps.put("Capela Bom Jesus \nPedreira - SP", "http://www.siqueiradg.com.br/rotadasaguas/images/capela-serra-negra.jpg");
        url_maps.put("Grande Hotel Glória \nÁguas de Lindóia - SP", "http://www.siqueiradg.com.br/rotadasaguas/images/hotel-aguas.jpg");


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(DestaqueActivity.this);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Top);
       // mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(10000);
        mDemoSlider.addOnPageChangeListener(DestaqueActivity.this);

        final List<RotaSugerida> rotaSugeridas = getRotas();

        final GridView gridView = (GridView) findViewById(R.id.lvRotasSugeridas);
        final TextView saudacao = (TextView) findViewById(R.id.nomeUsuario);
        gridView.setFocusable(false);
        gridView.setNumColumns(2);
        final RotaSugeridaAdapter adapter = new RotaSugeridaAdapter(this, rotaSugeridas);
        gridView.setAdapter(adapter);


        DateFormat df = new SimpleDateFormat("HH");
        String date = df.format(Calendar.getInstance().getTime());
        int hora = Integer.parseInt(date);

        if (hora > 00 && hora <= 11){
            saudacao.setText("Bom dia!");
        }else if(hora >= 12 && hora <= 18){
            saudacao.setText("Boa tarde!");
        }else{
            saudacao.setText("Boa noite!");
        }

        aOpcoes = new ArrayAdapter<String>(this, R.layout.spinner_item, opcoes);
        aOpcoes.setDropDownViewResource(R.layout.spinner_dropdown_item);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCidades);
        spinner.setAdapter(aOpcoes);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RotaSugeridaActivity.class);

                final String spinnerCidade = spinner.getSelectedItem().toString();

                if (adapter.getItemId(position) == 0){
                    intent.putExtra("rota", "1");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 1){
                    intent.putExtra("rota", "2");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 2){
                    intent.putExtra("rota", "3");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 3){
                    intent.putExtra("rota", "4");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 4){
                    intent.putExtra("rota", "5");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 5){
                    intent.putExtra("rota", "6");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 6){
                    intent.putExtra("rota", "7");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }
                if (adapter.getItemId(position) == 7){
                    intent.putExtra("rota", "8");
                    intent.putExtra("cidade", spinnerCidade);
                    startActivity(intent);
                }

            }
        });

        LinearLayout btn = (LinearLayout) findViewById(R.id.button_destaques);

        alterarTextoBtn();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRota();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override


    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_destaques);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

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
            Intent intent = new Intent(DestaqueActivity.this, DestaqueActivity.class);

            startActivity(intent);
        }else if (id == R.id.nav_meusLocais) {
            Intent intent = new Intent(DestaqueActivity.this, MeusLocaisActivity.class);

            startActivity(intent);

        }else if (id == R.id.nav_minhaRota) {
            verificarRota();
        }else if (id == R.id.nav_cidades) {
            Intent intent = new Intent(DestaqueActivity.this, CidadesActivity.class);

            startActivity(intent);

        }else if (id == R.id.nav_agencias) {
            Intent intent = new Intent(DestaqueActivity.this, AgenciasActivity.class);

            startActivity(intent);

        }else if (id == R.id.nav_minhaconta) {
            Intent intent = new Intent(DestaqueActivity.this, MinhaConta.class);

            startActivity(intent);

        }else if (id == R.id.nav_avalie) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_destaques);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void verificarRota(){
        //FIREBASE DATABASE
        DatabaseReference mDatabase;

        mDatabase = LibraryClass.getFirebase();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("rotas").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            Intent intent = new Intent(getApplicationContext(), MinhaRota.class );
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), CriarRota.class );
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    public void alterarTextoBtn(){
        //FIREBASE DATABASE
        DatabaseReference mDatabase;

        try {
            mDatabase = LibraryClass.getFirebase();
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase.child("rotas").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                TextView tx = (TextView) findViewById(R.id.textView2);
                                tx.setText("Acessar Minha Rota");
                            }
                            else{
                                TextView tx = (TextView) findViewById(R.id.textView2);
                                tx.setText("Criar Rota Personalizada");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("FIREBASE", "getUser:onCancelled", databaseError.toException());
                        }
                    }
            );
        }catch (Exception e){
            Log.d("FIREBASE", "Erro na autenticacao");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class );
            startActivity(intent);
            finish();
        }

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public List<RotaSugerida> getRotas(){

        List<RotaSugerida> listAux = new ArrayList<>();

        String[] nomesR = new String[]{"Boates", "Museus", "Parques", "Hoteis", "Restaurantes", "Bares"};
        String[] valoresR = new String[]{"boates|discotecas|bares", "contruções históricas", "natureza", "hoteis", "restaurantes|lanchonetes", "bares"};
        String[] iconR = new String[]{"{fa-music}", "{fa-building}", "{fa-tree}", "{fa-bed}", "{fa-cutlery}", "{fa-beer}"};



       for(int i = 0; i < nomesR.length; i++){
            RotaSugerida c = new RotaSugerida(nomesR[i % nomesR.length], valoresR[i % valoresR.length], iconR[i % iconR.length]);
            listAux.add(c);
       }

        return(listAux);
    }

}
