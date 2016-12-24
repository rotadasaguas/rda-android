package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import java.util.ArrayList;
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

        if (id == R.id.nav_minhaRota) {
            Intent intent = new Intent(MinhaRota.this, MinhaRota.class);

            startActivity(intent);
        }else if (id == R.id.nav_agencias) {

        }else if (id == R.id.nav_cidades) {
            Intent intent = new Intent(MinhaRota.this, CidadesActivity.class);

            startActivity(intent);
        }else if (id == R.id.nav_minhaconta) {
            Intent intent = new Intent(MinhaRota.this, MinhaConta.class);

            startActivity(intent);

        } else if (id == R.id.nav_avalie) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_minha_rota);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
