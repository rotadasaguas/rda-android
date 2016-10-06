package to.dtech.rotadasaguas;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                finish();


                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        if( firebaseAuth.getCurrentUser() == null  ){
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this, IntroActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this, CriarRota.class);
                            startActivity(intent);
                        }
                    }
                };

                mAuth = FirebaseAuth.getInstance();
                mAuth.addAuthStateListener( authStateListener );

            }
        }, 2000);
    }

}
