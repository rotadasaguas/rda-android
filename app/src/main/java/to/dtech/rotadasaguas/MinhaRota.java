package to.dtech.rotadasaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

import to.dtech.rotadasaguas.domain.Alimentacao;
import to.dtech.rotadasaguas.domain.util.LibraryClass;
import to.dtech.rotadasaguas.fragment.AlimentacaoFragment;
import to.dtech.rotadasaguas.fragment.EsportesFragment;
import to.dtech.rotadasaguas.fragment.LazerFragment;


public class MinhaRota extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private DatabaseReference mDatabase;
    private ProgressDialog pDialog;


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




        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // Retorna a pagina de alimentação
                    return new AlimentacaoFragment();
                case 1:
                    // Retorna a página Esportes
                    return new EsportesFragment();
                case 2:
                    // Retorna a página lazer
                    return new LazerFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }

    public List<Alimentacao> getSetCarList(int qtd){

        //FIREBASE DATABASE
        mDatabase = LibraryClass.getFirebase();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("rotas").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList marcadores = new ArrayList();

                        //CAPTURA OS VALORES DOS MARCADORES NO FIREBASE
                        for (DataSnapshot data : dataSnapshot.child("marcadores").getChildren()){
                            //INSERE OS VALORES NO ARRAY PARA USO NO WEB SERVICE
                            marcadores.add(data.getValue());
                        }
                        //REMOVE OS ESPAÇOS E [] DO ARRAY PARA IMPRIMIR A LISTA COMPLETA
                        String textMarcadores = marcadores.toString().replace("[", "").replace("]", "").replace(" ", "");
                       System.out.println("http://localhost/rotadasaguas/ws-rota/index.php?c=Locais&s=listarPorTag&id=" + textMarcadores);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );

        String[] models = new String[]{"Doce Arte Café", "Sorveteria Ademar", "Mauro's Grill Churrascaria"};
        String[] brands = new String[]{"Serve café da manhã, café e bebidas", " Sorvetes de todos os tipos", "Ótimo para almoço em grupo"};
        String[] photos = new String[]{"{fa-coffee}", "{fa-bitbucket}", "{fa-cutlery}"};
        List<Alimentacao> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            Alimentacao c = new Alimentacao( models[i % models.length], brands[ i % brands.length ], photos[i % models.length] );
            listAux.add(c);
        }
        return(listAux);
    }


    /*MENU LATERAL*/
    @Override
    public void onBackPressed() {

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

        if (id == R.id.nav_minhaRota) {
            //nao realiza ação
        } else if (id == R.id.nav_cidades) {

        } else if (id == R.id.nav_emergency) {

        } else if (id == R.id.nav_minhaconta) {
            Intent intent = new Intent(MinhaRota.this, MinhaConta.class);

            startActivity(intent);

        } else if (id == R.id.nav_avalie) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_minha_rota);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
