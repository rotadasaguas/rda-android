package to.dtech.rotadasaguas;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import to.dtech.rotadasaguas.adapter.TagSubAdapter;
import to.dtech.rotadasaguas.domain.Rota;
import to.dtech.rotadasaguas.domain.Tag;
import to.dtech.rotadasaguas.domain.util.LibraryClass;

public class SubAcomodacaoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_acomodacao);

        final List<Tag> tags = getTagsAcomodacao();

        final ListView listView = (ListView) findViewById(R.id.subAcomodacao);
        listView.setAdapter(new TagSubAdapter(this, tags));

        final List<String> listaMarcadores = new ArrayList<String>();

        //RECEBE DADOS DA INTENT ANTERIOR E ADICIONA NA NOVA
        Intent intentOld = getIntent();
        ArrayList<String> listaOld = intentOld.getStringArrayListExtra("Marcadores");

        for (int i = 0; i < listaOld.size(); i++ ){
            listaMarcadores.add(listaOld.get(i));
        }
        //OBTEM A CIDADE DA ROTA
        final String cidade = listaMarcadores.get(0);

        //REMOVE A CIDADE DA LISTA DE TAGS
        listaMarcadores.remove(0);


        //FIREBASE INIT
        mDatabase = LibraryClass.getFirebase();
        //OBTEM O ID DO USUARIO LOGADO
        final String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                TextView c = (TextView) view.findViewById(R.id.local);
                LikeButton l = (LikeButton) view.findViewById(R.id.gostei);

                TagSubAdapter adapter = (TagSubAdapter) listView.getAdapter();

                boolean likeValue = tags.get(position).getAtivo();

                if (likeValue == false) {
                    c.setTextColor(Color.parseColor("#2196F3"));
                    adapter.alteraCor(position);
                    l.setLiked(true);
                    listaMarcadores.add(tags.get(position).getNumero());
                } else {
                    c.setTextColor(Color.parseColor("#848484"));
                    l.setLiked(false);
                    adapter.removeCor(position);
                    listaMarcadores.remove(tags.get(position).getNumero());
                }


            }
        });

        Button novaTela = (Button) findViewById(R.id.avancarGostosAcomodacao);
        novaTela.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveRota(idUser, listaMarcadores, cidade);
            }
        });

    }

    public List<Tag> getTagsAcomodacao() {
        String[] tags = new String[]{"Hotel", "Camping", "Pousada", "Estalagem", "Albergue", "Motel", "Hotel-fazenda"};
        String[] numeros = new String[]{"27", "28", "29", "30", "31", "32", "33"};
        Boolean[] likes = new Boolean[]{false};
        List<Tag> listAux = new ArrayList<>();

        for(int i = 0; i < tags.length; i++){
            Tag c = new Tag( tags[i % tags.length], likes[i % likes.length], numeros[i % numeros.length]);
            listAux.add(c);
        }
        return (listAux);
    }


    //FIREBASE
    private void saveRota(String userId, List<String> lista, String cidadeMarcada) {
        Rota rota = new Rota(userId, lista, cidadeMarcada);
        try {
            mDatabase.child("rotas").child(userId).setValue(rota);
            final Intent intentRota = new Intent(SubAcomodacaoActivity.this, MinhaRota.class);
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Rota Criada!")
                    .setContentText("Clique em ok para visualiza-la!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(intentRota);
                        }
                    })
                    .show();
        }catch (Exception e){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Ops!")
                    .setContentText("Algo deu Errado!")
                    .show();
        }
    }
}