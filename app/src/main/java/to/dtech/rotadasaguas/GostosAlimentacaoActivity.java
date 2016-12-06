package to.dtech.rotadasaguas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;

import to.dtech.rotadasaguas.adapter.TagGostosAdapter;
import to.dtech.rotadasaguas.domain.Tag;

public class GostosAlimentacaoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gostos_alimentacao);

        final List<Tag> tags = getTagsGostosAlimentacao();

        final Intent intent = new Intent(GostosAlimentacaoActivity.this, SubLazerActivity.class);
        final List<String> listaMarcadoresAlimentacao = new ArrayList<String>();

        final ListView listView = (ListView) findViewById(R.id.gostosAlimentacao);
        listView.setAdapter(new TagGostosAdapter(this, tags));


        //RECEBE DADOS DA INTENT ANTERIOR E ADICIONA NA NOVA
        Intent intentOld = getIntent();
        ArrayList<String> listaOld = intentOld.getStringArrayListExtra("Marcadores");

        for (int i = 0; i < listaOld.size(); i++ ){
            listaMarcadoresAlimentacao.add(listaOld.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                TextView c = (TextView) view.findViewById(R.id.local);
                LikeButton l = (LikeButton) view.findViewById(R.id.gostei);

                TagGostosAdapter adapter = (TagGostosAdapter) listView.getAdapter();

                boolean likeValue = tags.get(position).getAtivo();

                if (likeValue == false){
                    c.setTextColor(Color.parseColor("#FBC02D"));
                    adapter.alteraCor(position);
                    l.setLiked(true);
                    listaMarcadoresAlimentacao.add(tags.get(position).getTitulo());
                }
                else{
                    c.setTextColor(Color.parseColor("#848484"));
                    l.setLiked(false);
                    adapter.removeCor(position);
                    listaMarcadoresAlimentacao.remove(tags.get(position).getTitulo());
                }


            }
        });

        Button novaTela = (Button) findViewById(R.id.avancarLazer);
        novaTela.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putStringArrayListExtra("Marcadores", (ArrayList<String>) listaMarcadoresAlimentacao);
                startActivity(intent);
            }
        });

    }

    public List<Tag> getTagsGostosAlimentacao(){
        String[] tags = new String[]{"Churrasco", "Doces Regionais", "Comida Caseira", "Comida Italiana", "Sushi", "Café e Chá", "Fast Food", "Sorvete"};
        Boolean[] likes = new Boolean[]{false};
        List<Tag> listAux = new ArrayList<>();

        for(int i = 0; i < tags.length; i++){
            Tag c = new Tag( tags[i % tags.length], likes[i % likes.length]);
            listAux.add(c);
        }
        return(listAux);
    }


}