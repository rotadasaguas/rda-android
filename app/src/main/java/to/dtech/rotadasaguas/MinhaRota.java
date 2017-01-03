package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import to.dtech.rotadasaguas.domain.util.LibraryClass;
import to.dtech.rotadasaguas.fragment.AlimentacaoFragment;
import to.dtech.rotadasaguas.fragment.EsportesFragment;
import to.dtech.rotadasaguas.fragment.AcomodacaoFragment;

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


    public String urlJson = "";
    public String cidadeJson = "";

    private int[] tabIcons = {
            R.drawable.ic_restaurant_black,
            R.drawable.ic_directions_bike_black,
            R.drawable.ic_local_hotel_black
    };

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
                        ArrayList marcadores = new ArrayList();

                        //CAPTURA OS VALORES DOS MARCADORES NO FIREBASE
                        for (DataSnapshot data : dataSnapshot.child("marcadores").getChildren()) {
                            //INSERE OS VALORES NO ARRAY PARA USO NO WEB SERVICE
                            marcadores.add(data.getValue());
                        }

                        cidadeJson = dataSnapshot.child("cidade").getValue().toString();

                        //REMOVE OS ESPAÇOS E [] DO ARRAY PARA IMPRIMIR A LISTA COMPLETA
                        String textMarcadores = marcadores.toString().replace("[", "").replace("]", "").replace(" ", "");
                        urlJson = "http://siqueiradg.com.br/rotadasaguas/ws-rota/index.php?c=Locais&s=listarPorTag&id=" + textMarcadores;

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
            Bundle args = new Bundle();
            args.putString("url", urlJson);
            args.putString("cidade", cidadeJson);

            switch (position) {
                case 0:
                    AlimentacaoFragment alimentacao = new AlimentacaoFragment();
                    alimentacao.setArguments(args);

                    return alimentacao;
                case 1:
                    EsportesFragment esportes = new EsportesFragment();
                    esportes.setArguments(args);
                    return esportes;
                case 2:
                    AcomodacaoFragment lazer = new AcomodacaoFragment();
                    lazer.setArguments(args);

                    return lazer;
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
}
