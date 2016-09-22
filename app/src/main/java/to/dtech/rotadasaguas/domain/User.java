package to.dtech.rotadasaguas.domain;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import to.dtech.rotadasaguas.domain.util.CryptWithMD5;
import to.dtech.rotadasaguas.domain.util.LibraryClass;


public class User {
    public static String TOKEN = "to.dtech.rotadasaguas.domain.User.TOKEN";
    public static String ID = "to.dtech.rotadasaguas.domain.User.ID";

    @Exclude
    private String id;
    private String name;
    private String email;
    private String celular;
    @Exclude
    private String password;
    @Exclude
    private String newPassword;


    public User(){}



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void saveIdSP(Context context, String token ){
        LibraryClass.saveSP( context, ID, token );
    }

    public void retrieveIdSP(Context context ){
        this.id = LibraryClass.getSP( context, ID );
    }

    public boolean isSocialNetworkLogged( Context context ){
        String token = getTokenSP( context );
        return( token.contains("facebook") || token.contains("google") );
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setNameInMap( Map<String, Object> map ) {
        if( getName() != null ){
            map.put( "name", getName() );
        }
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void setEmailInMap( Map<String, Object> map ) {
        if( getEmail() != null ){
            map.put( "email", getEmail() );
        }
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    private void setCelularInMap( Map<String, Object> map ) {
        if( getCelular() != null ){
            map.put( "celular", getCelular() );
        }
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void generateCryptPassword() {
        password = CryptWithMD5.cryptWithMD5(password);
    }



    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void generateCryptNewPassword() {
        newPassword = CryptWithMD5.cryptWithMD5(newPassword);
    }



    public void saveTokenSP(Context context, String token ){
        LibraryClass.saveSP( context, TOKEN, token );
    }
    public String getTokenSP(Context context ){
        return( LibraryClass.getSP( context, TOKEN ) );
    }



    public void saveDB( DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );

        Map<String, Object> map = new HashMap<>();
        setNameInMap(map);
        setEmailInMap(map);
        setCelularInMap(map);

        if( map.isEmpty() ){
            return;
        }

        if( completionListener.length > 0 ){
            firebase.updateChildren(map, completionListener[0]);
        }
        else{
            firebase.updateChildren(map);
        }
    }

    public void removeDB(){
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );
        firebase.setValue(null);
    }

    public void contextDataDB( Context context ){
        retrieveIdSP( context );
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );

        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
    }
}
