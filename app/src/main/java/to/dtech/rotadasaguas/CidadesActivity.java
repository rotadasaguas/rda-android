package to.dtech.rotadasaguas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;

import to.dtech.rotadasaguas.fragment.city.AguasLindoiaFragment;
import to.dtech.rotadasaguas.fragment.city.AmparoFragment;
import to.dtech.rotadasaguas.fragment.city.HolambraFragment;
import to.dtech.rotadasaguas.fragment.city.JaguariunaFragment;
import to.dtech.rotadasaguas.fragment.city.LindoiaFragment;
import to.dtech.rotadasaguas.fragment.city.MonteAlegreDoSulFragment;
import to.dtech.rotadasaguas.fragment.city.PedreiraFragment;
import to.dtech.rotadasaguas.fragment.city.SerraNegraFragment;
import to.dtech.rotadasaguas.fragment.city.SocorroFragment;

public class CidadesActivity extends AppCompatActivity {

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
    private ViewPager mViewPager;
    private ImageButton leftNav;
    private ImageButton rightNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cidades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        leftNav = (ImageButton) findViewById(R.id.left_nav);
        rightNav = (ImageButton) findViewById(R.id.right_nav);

        // Images left navigation
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = mViewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    mViewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    mViewPager.setCurrentItem(tab);
                }
            }
        });

        // Images right navigatin
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = mViewPager.getCurrentItem();
                tab++;
                mViewPager.setCurrentItem(tab);
            }
        });

        Button button = (Button) findViewById(R.id.newRota);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CidadesActivity.this, NovaRota.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //handle the home button onClick event here.
                Intent myIntent = new Intent(getApplicationContext(), DestaqueActivity.class);
                startActivityForResult(myIntent, 0);
                return true;
            }

        return super.onOptionsItemSelected(item);
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
                    return new SocorroFragment();
                case 1:
                    return new HolambraFragment();
                case 2:
                    return new AguasLindoiaFragment();
                case 3:
                    return new LindoiaFragment();
                case 4:
                    return new SerraNegraFragment();
                case 5:
                    return new MonteAlegreDoSulFragment();
                case 6:
                    return new AmparoFragment();
                case 7:
                    return new JaguariunaFragment();
                case 8:
                    return new PedreiraFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
