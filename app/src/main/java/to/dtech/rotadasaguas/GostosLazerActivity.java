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

    String[] gostosLazerAventura = {"Downhill", "Pescaria", "Rafting ", "Cavalgada", "Tirolesa", "Arvorismo", "Rappel", "Escalada"};
    String[] numerosLazerAventura = {"12", "14", "13", "37", "15", "16", "11", "17"};
    String[] gostosLazerPasseio = {"Caminhada", "Passeio a Natureza", "Prédios Históricos"};
    String[] numerosLazerPasseio = {"18", "19", "20"};
    String[] gostosLazerAmbos = {"Caminhada", "Passeio a Natureza", "Prédios Históricos", "Downhill", "Pescaria", "Rafting ", "Cavalgada", "Tirolesa", "Arvorismo", "Rappel", "Escalada"};
    String[] numerosLazerAmbos = {"18", "19", "20", "12", "14", "13", "37", "15", "16", "11", "17"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gostos_lazer);

        final List<Tag> tags;

        final ListView listView = (ListView) findViewById(R.id.gostosLazer);

        final List<String> listaMarcadores = new ArrayList<String>();
        final Intent intent = new Intent(GostosLazerActivity.this, SubAcomodacaoActivity.class);


        //RECEBE DADOS DA INTENT ANTERIOR E ADICIONA NA NOVA
        Intent intentOld = getIntent();
        ArrayList<String> listaOld = intentOld.getStringArrayListExtra("Marcadores");

        for (int i = 0; i < listaOld.size(); i++ ){
            listaMarcadores.add(listaOld.get(i));
        }


        //VERIFICA OPCAO DE LAZER MARCADA & CARREGA LISTA DE DADOS
        int tamanhoLista = listaMarcadores.size();

        String tagNomeUm = listaMarcadores.get(tamanhoLista-1);
        String tagNomeDois = listaMarcadores.get(tamanhoLista-2);

        if (tagNomeUm.equalsIgnoreCase("35") && tagNomeDois.equalsIgnoreCase("36") || tagNomeUm.equalsIgnoreCase("36") && tagNomeDois.equalsIgnoreCase("35")){
            tags = getTagsGostosLazer(gostosLazerAmbos, numerosLazerAmbos);
        }
        else if (tagNomeUm.equalsIgnoreCase("35")){
            tags = getTagsGostosLazer(gostosLazerPasseio, numerosLazerPasseio);
        }
        else if (tagNomeUm.equalsIgnoreCase("36")){
            tags = getTagsGostosLazer(gostosLazerAventura, numerosLazerAventura);
        }
        else{
            String[] emBranco = {""};
            tags = getTagsGostosLazer(emBranco, emBranco);
        }
        /*******************************************/

        listView.setAdapter(new TagGostosAdapter(this, tags));
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

        Button novaTela = (Button) findViewById(R.id.avancarAcomodacao);
        novaTela.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putStringArrayListExtra("Marcadores", (ArrayList<String>) listaMarcadores);
                startActivity(intent);
            }
        });

    }

    public List<Tag> getTagsGostosLazer(String[] listaM, String[] listaN){
        String[] tags = listaM;
        Boolean[] likes = new Boolean[]{false};
        String[] numeros = listaN;
        List<Tag> listAux = new ArrayList<>();

        for(int i = 0; i < tags.length; i++){
            Tag c = new Tag( tags[i % tags.length], likes[i % likes.length], numeros[i % numeros.length]);
            listAux.add(c);
        }
        return(listAux);
    }


}