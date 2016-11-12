package to.dtech.rotadasaguas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;

import to.dtech.rotadasaguas.adapter.TagGostosAdapter;
import to.dtech.rotadasaguas.domain.Tag;

public class GostosLazerActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gostos_lazer);

        final List<Tag> tags = getTagsGostosLazer();

        final ListView listView = (ListView) findViewById(R.id.gostosLazer);
        listView.setAdapter(new TagGostosAdapter(this, tags));


        final List<String> listaMarcadores = new ArrayList<String>();
        final Intent intent = new Intent(GostosLazerActivity.this, SubAcomodacaoActivity.class);


        //RECEBE DADOS DA INTENT ANTERIOR E ADICIONA NA NOVA
        Intent intentOld = getIntent();
        ArrayList<String> listaOld = intentOld.getStringArrayListExtra("Marcadores");

        for (int i = 0; i < listaOld.size(); i++ ){
            listaMarcadores.add(listaOld.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                TextView c = (TextView) view.findViewById(R.id.local);
                LikeButton l = (LikeButton) view.findViewById(R.id.gostei);

                TagGostosAdapter adapter = (TagGostosAdapter) listView.getAdapter();

                boolean likeValue = tags.get(position).getAtivo();

                if (likeValue == false){
                    c.setTextColor(Color.parseColor("#e50000"));
                    adapter.alteraCor(position);
                    l.setLiked(true);
                    listaMarcadores.add(tags.get(position).getTitulo());
                }
                else{
                    c.setTextColor(Color.parseColor("#848484"));
                    l.setLiked(false);
                    adapter.removeCor(position);
                    listaMarcadores.remove(tags.get(position).getTitulo());
                }


            }
        });

        Button novaTela = (Button) findViewById(R.id.avancarAcomodacao);
        novaTela.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putStringArrayListExtra("Marcadores", (ArrayList<String>) listaMarcadores);
                startActivity(intent);
            }
        });

    }

    public List<Tag> getTagsGostosLazer(){
        String[] tags = new String[]{"Downhill", "Pescaria", "Parques Aquaticos", "Rafting ", "Cavalgada", "Tirolesa", "Arvorismo", "Rappel", "Escalada", "Caminhada", "Passeio a Natureza", "Passeio Predios Historicos"};
        Boolean[] likes = new Boolean[]{false};
        List<Tag> listAux = new ArrayList<>();

        for(int i = 0; i < tags.length; i++){
            Tag c = new Tag( tags[i % tags.length], likes[i % likes.length]);
            listAux.add(c);
        }
        return(listAux);
    }


}