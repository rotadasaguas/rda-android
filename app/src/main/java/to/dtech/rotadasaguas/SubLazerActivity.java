package to.dtech.rotadasaguas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;

import to.dtech.rotadasaguas.adapter.TagSubAdapter;
import to.dtech.rotadasaguas.domain.Tag;

public class SubLazerActivity extends AppCompatActivity{

    String cidadeOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_lazer);

        final List<Tag> tags = getTagsLazer();

        final ListView listView = (ListView) findViewById(R.id.subEsportes);
        listView.setAdapter(new TagSubAdapter(this, tags));


        final List<String> listaMarcadores = new ArrayList<String>();
        final Intent intent = new Intent(SubLazerActivity.this, SubAcomodacaoActivity.class);


        //RECEBE DADOS DA INTENT ANTERIOR E ADICIONA NA NOVA
        Intent intentOld = getIntent();
        final ArrayList<String> listaAlimentacaoOld = intentOld.getStringArrayListExtra("alimentacao");
        cidadeOld = intentOld.getStringExtra("cidade");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                TextView c = (TextView) view.findViewById(R.id.local);
                LikeButton l = (LikeButton) view.findViewById(R.id.gostei);

                TagSubAdapter adapter = (TagSubAdapter) listView.getAdapter();

                boolean likeValue = tags.get(position).getAtivo();

                if (likeValue == false){
                    c.setTextColor(Color.parseColor("#2196F3"));
                    adapter.alteraCor(position);
                    l.setLiked(true);
                    listaMarcadores.add(tags.get(position).getNumero());
                }
                else{
                    c.setTextColor(Color.parseColor("#848484"));
                    l.setLiked(false);
                    adapter.removeCor(position);
                    listaMarcadores.remove(tags.get(position).getNumero());
                }


            }
        });

        Button novaTela = (Button) findViewById(R.id.avancarGostosLazer);
        novaTela.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putExtra("cidade", cidadeOld);
                intent.putStringArrayListExtra("lazer", (ArrayList<String>) listaMarcadores);
                intent.putStringArrayListExtra("alimentacao", (ArrayList<String>) listaAlimentacaoOld);
                Log.d("gostos alimentacao: ", cidadeOld + listaAlimentacaoOld.toString());
                Log.d("gostos lazer: ", cidadeOld + listaMarcadores.toString());
                startActivity(intent);
            }
        });

    }

    public List<Tag> getTagsLazer(){
        String[] tags = new String[]{"Trilhas", "Monumentos", "Museus", "Downhill", "Pescaria", "Rafting ", "Cavalgada", "Tirolesa", "Arvorismo", "Rappel", "Escalada"};
        String[] numeros = new String[]{"Trilhas", "Monumentos", "Museus", "Downhill", "Pescaria", "Rafting ", "Cavalgada", "Tirolesa", "Arvorismo", "Rappel", "Escalada"};
        Boolean[] likes = new Boolean[]{false};
        List<Tag> listAux = new ArrayList<>();

        for(int i = 0; i < tags.length; i++){
            Tag c = new Tag( tags[i % tags.length], likes[i % likes.length], numeros[i % numeros.length]);
            listAux.add(c);
        }
        return(listAux);
    }


}