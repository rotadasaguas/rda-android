package to.dtech.rotadasaguas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class LocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Iconify .with(new FontAwesomeModule());
        setContentView(R.layout.activity_local);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
