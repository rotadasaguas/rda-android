package to.dtech.rotadasaguas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Arrays;

import to.dtech.rotadasaguas.domain.User;


public class LoginActivity extends CommonActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private User user;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessFacebookLoginData( loginResult.getAccessToken() );
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                FirebaseCrash.report( error );
                showSnackbar( error.getMessage() );
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = getFirebaseAuthResultHandler();
        initViews();
        initUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener( mAuthListener );
    }


    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthListener != null ){
            mAuth.removeAuthStateListener( mAuthListener );
        }
    }


    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler(){
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

                if( userFirebase != null && user.getId() == null ){
                    user.saveIdSP( LoginActivity.this, userFirebase.getUid() );
                    user.setId( userFirebase.getUid() );
                    user.setName( userFirebase.getDisplayName() );
                    user.setEmail( userFirebase.getEmail() );
                    user.saveDB();

                    callMainActivity();
                }
            }
        };
        return( callback );
    }




    protected void initViews(){
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    protected void initUser(){
        user = new User();
        user.setEmail( email.getText().toString() );
        user.setPassword( password.getText().toString() );
        user.generateCryptPassword();
    }

    public void callSignUp(View view){
        Intent intent = new Intent( this, SignUpActivity.class );
        startActivity(intent);
    }

    public void callReset(View view){
        Intent intent = new Intent( this, ResetActivity.class );
        startActivity(intent);
    }

    public void sendLoginData( View view ){
        openProgressBar();
        initUser();
        verifyLogin();
    }


    public void sendLoginFacebookData( View view ){
        openProgressBar();
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginFacebookData()");
        LoginManager
                .getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList("public_profile", "user_friends", "email")
                );
    }

    private void accessFacebookLoginData(AccessToken accessToken){
        accessLoginData(
                "facebook",
                (accessToken != null ? accessToken.getToken() : null)
        );
    }
    private void accessLoginData( String provider, String... tokens ){
        if( tokens != null
                && tokens.length > 0
                && tokens[0] != null ){

            AuthCredential credential = FacebookAuthProvider.getCredential( tokens[0]);
            user.saveIdSP( LoginActivity.this, provider );
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            openProgressBar();

                            if( !task.isSuccessful() ){
                                closeProgressBar();
                                showSnackbar("Login social falhou");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report( e );
                        }
                    });
            closeProgressBar();

        }
        else{
            mAuth.signOut();
        }
    }


    private void callMainActivity(){
        Intent intent = new Intent( this, DestaqueActivity.class );
        startActivity(intent);
        finish();
    }




    private void verifyLogin(){
        user.saveTokenSP( LoginActivity.this, "" );
        mAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if( !task.isSuccessful() ){
                            showSnackbar("Login falhou");
                            return;
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }
}