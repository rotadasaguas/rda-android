package to.dtech.rotadasaguas;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

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
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.domain.util.LibraryClass;
import to.dtech.rotadasaguas.fragment.AlimentacaoFragment;
import to.dtech.rotadasaguas.fragment.EsportesFragment;
import to.dtech.rotadasaguas.fragment.AcomodacaoFragment;
import to.dtech.rotadasaguas.util.HttpHandler;

import static android.R.attr.fragment;
public class MinhaRota extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private DatabaseReference mDatabase;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private SweetAlertDialog pDialog;


    private int[] tabIcons = {
            R.drawable.ic_restaurant_black,
            R.drawable.ic_directions_bike_black,
            R.drawable.ic_local_hotel_black
    };

    //ATUALIZACAO MARÇO
    public static String nomeCidade = "";
    public static ArrayList alimentacao = new ArrayList();
    public static ArrayList lazer = new ArrayList();
    public static ArrayList acomodacao = new ArrayList();

    public static List<ItemLocal> listPlaceDetailAlimentacao;
    public static List<ItemLocal> listPlaceDetailLazer;
    public static List<ItemLocal> listPlaceDetailAcomodacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_minha_rota);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MinhaRota.this, NovaRota.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(MinhaRota.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Deseja apagar a rota?")
                        .setContentText("Não será possível recuperar a rota!")
                        .setConfirmText("Sim, quero apagar!")
                        .setCancelText("Não")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                apagarRota();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_minha_rota);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //FIREBASE DATABASE
        mDatabase = LibraryClass.getFirebase();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("rotas").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SweetAlertDialog ps;
                        ps = new SweetAlertDialog(MinhaRota.this, SweetAlertDialog.PROGRESS_TYPE);
                        ps.getProgressHelper().setBarColor(Color.parseColor("#0066FF"));
                        ps.setTitleText("Carregando");
                        ps.setCancelable(false);
                        ps.show();
                        //CAPTURA OS VALORES DOS MARCADORES NO FIREBASE
                        for (DataSnapshot data : dataSnapshot.child("alimentacao").getChildren()) {
                            //INSERE OS VALORES NO ARRAY PARA USO NO WEB SERVICE
                            alimentacao.add(data.getValue());
                        }
                        //CAPTURA OS VALORES DOS MARCADORES NO FIREBASE
                        for (DataSnapshot data : dataSnapshot.child("lazer").getChildren()) {
                            //INSERE OS VALORES NO ARRAY PARA USO NO WEB SERVICE
                            lazer.add(data.getValue());
                        }
                        //CAPTURA OS VALORES DOS MARCADORES NO FIREBASE
                        for (DataSnapshot data : dataSnapshot.child("acomodacao").getChildren()) {
                            //INSERE OS VALORES NO ARRAY PARA USO NO WEB SERVICE
                            acomodacao.add(data.getValue());
                        }

                        nomeCidade = dataSnapshot.child("cidade").getValue().toString();
                        if (!nomeCidade.equals("")){
                            if (!acomodacao.equals("") && !alimentacao.equals("") && !lazer.equals("")) {
                                Log.d("DADOS-WS", alimentacao.toString() + "| LAZER->" +  lazer.toString() + "| ACOMODA->" + acomodacao.toString());
                                new ProcessaDadosWS().execute();
                                ps.dismiss();
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );


    }

    public void apagarRota(){
        //FIREBASE DATABASE
        DatabaseReference mDatabase;

        mDatabase = LibraryClass.getFirebase();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("rotas").child(userId).removeValue();

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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Sua rota foi apagada!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), DestaqueActivity.class );
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (listPlaceDetailAlimentacao != null){
                        AlimentacaoFragment.mList = listPlaceDetailAlimentacao;
                    }
                    AlimentacaoFragment alimentacao = new AlimentacaoFragment();

                    return alimentacao;
                case 1:
                    if (listPlaceDetailLazer != null){
                        EsportesFragment.mList = listPlaceDetailLazer;
                    }
                    EsportesFragment esportes = new EsportesFragment();
                    return esportes;
                case 2:
                    if (listPlaceDetailAcomodacao != null){
                        AcomodacaoFragment.mList = listPlaceDetailAcomodacao;
                    }
                    AcomodacaoFragment acomodacao = new AcomodacaoFragment();

                    return acomodacao;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MinhaRota.this, DestaqueActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_destaques) {
            Intent intent = new Intent(MinhaRota.this, DestaqueActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_minhaRota) {
            Intent intent = new Intent(MinhaRota.this, MinhaRota.class);
            startActivity(intent);
        }else if (id == R.id.nav_agencias) {
            Intent intent = new Intent(MinhaRota.this, AgenciasActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_cidades) {
            Intent intent = new Intent(MinhaRota.this, CidadesActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_minhaconta) {
            Intent intent = new Intent(MinhaRota.this, MinhaConta.class);
            startActivity(intent);
        } else if (id == R.id.nav_avalie) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_minha_rota);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //tarefas em background
    public class ProcessaDadosWS extends AsyncTask<String,Integer,Integer> {

        public List<String> listPlaceSearchAlimentacao = new ArrayList<String>();
        public List<String> listPlaceSearchLazer = new ArrayList<String>();
        public List<String> listPlaceSearchAcomodacao = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new SweetAlertDialog(MinhaRota.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066FF"));
            pDialog.setTitleText("Carregando");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Log.d("contador", "chamou");

            //ALIMENTACAO
            if (!alimentacao.equals("")){
                String auxUrl = "https://maps.googleapis.com/maps/api/place/radarsearch/json?keyword=" + alimentacao.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "|") + "&location=" + getLocalizacao(nomeCidade) + "&radius=8000&key=AIzaSyDi_3eGNw22HQfvV4Dfh__-GBCUxOLxdx8";
                Log.d("contador+", auxUrl);
                if (!auxUrl.equals("")){
                    try {
                        listPlaceSearchAlimentacao = getPlaceIDs(auxUrl);
                        listPlaceDetailAlimentacao = getPlaceDetails(listPlaceSearchAlimentacao);
                    }catch (Exception e){

                    }
                }
            }
            //LAZER
            if (!lazer.equals("")){
                String auxUrllazer = "https://maps.googleapis.com/maps/api/place/radarsearch/json?keyword=" + lazer.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "|") + "&location=" + getLocalizacao(nomeCidade) + "&radius=8000&key=AIzaSyDi_3eGNw22HQfvV4Dfh__-GBCUxOLxdx8";
                Log.d("contador+", auxUrllazer);
                if (!auxUrllazer.equals("")){
                    try {
                        listPlaceSearchLazer = getPlaceIDs(auxUrllazer);
                        listPlaceDetailLazer= getPlaceDetails(listPlaceSearchLazer);
                    }catch (Exception e){

                    }
                }
            }
            //ACOMODACAO
            if (!acomodacao.equals("")){
                String auxUrlacomod = "https://maps.googleapis.com/maps/api/place/radarsearch/json?keyword=" + acomodacao.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "|") + "&location=" + getLocalizacao(nomeCidade) + "&radius=8000&key=AIzaSyDi_3eGNw22HQfvV4Dfh__-GBCUxOLxdx8";
                Log.d("contador+", auxUrlacomod);
                if (!auxUrlacomod.equals("")){
                    try {
                        listPlaceSearchAcomodacao = getPlaceIDs(auxUrlacomod);
                        listPlaceDetailAcomodacao = getPlaceDetails(listPlaceSearchAcomodacao);
                    }catch (Exception e){

                    }
                }
            }

            if (listPlaceDetailAlimentacao != null && listPlaceDetailAcomodacao != null && listPlaceDetailLazer != null){
                pDialog.dismiss();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOffscreenPageLimit(3);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }

        public List<String> getPlaceIDs(final String args) throws ExecutionException, InterruptedException {

            List<String> listPlaceIds = new ArrayList<String>();

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

        public List<ItemLocal> getPlaceDetails(final List<String> listona) throws ExecutionException, InterruptedException {

            final List<String> listaDeDadosNome = new ArrayList<String>();
            final List<String> listaDeDadosID = new ArrayList<String>();
            final List<String> listaDeDadosEnd = new ArrayList<String>();
            final List<String> listaDeDadosFoto = new ArrayList<String>();

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
                                JSONArray photosGoogle = result.getJSONArray("photos");
                                String photoHash = photosGoogle.getJSONObject(0).getString("photo_reference");
                                if (photoHash.equals(" ") || photoHash == null){
                                    listaDeDadosFoto.add("http://www.freeiconspng.com/uploads/no-image-icon-32.png");
                                }else{
                                    listaDeDadosFoto.add(photoHash);
                                }

                            } catch (final Exception e) {
                                Log.e("Bug: ", e.toString());
                            }
                        }

                    }

                    List<ItemLocal> listAux = new ArrayList<>();

                    for(int i = 0; i <= listaDeDadosNome.size()-1; i++){
                        try {
                            ItemLocal c = new ItemLocal( listaDeDadosNome.get(i), listaDeDadosEnd.get(i), listaDeDadosID.get(i), listaDeDadosFoto.get(i) );
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

        public String getLocalizacao(String cidade){

            String loc = "";

            if (cidade.equalsIgnoreCase("Socorro")){
                loc = "-22.5951525,-46.5446116";
            }else if(cidade.equalsIgnoreCase("Holambra")){
                loc = "-22.6333,-47.0564";
            }else if(cidade.equalsIgnoreCase("Lindóia")){
                loc = "-22.5234,-46.6503";
            }else if(cidade.equalsIgnoreCase("Monte Alegre do Sul")){
                loc = "-22.6825,-46.6814";
            }else if(cidade.equalsIgnoreCase("Pedreira")){
                loc = "-22.7419,-46.9017";
            }else if(cidade.equalsIgnoreCase("Águas de Lindóia")){
                loc = "-22.4767,-46.6334";
            }else if(cidade.equalsIgnoreCase("Amparo")){
                loc = "-22.7015,-46.7644";
            }else if(cidade.equalsIgnoreCase("Serra Negra")){
                loc = "-22.6118,-46.701";
            }else if(cidade.equalsIgnoreCase("Jaguariúna")){
                loc = "-22.7058,-46.9862";
            }

            return loc;
        }

    }
}