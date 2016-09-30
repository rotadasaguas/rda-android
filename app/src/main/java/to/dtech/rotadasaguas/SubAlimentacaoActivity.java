package to.dtech.rotadasaguas;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import to.dtech.rotadasaguas.adapter.TagSubAlimentacaoAdapter;
import to.dtech.rotadasaguas.domain.Alimentacao;
import to.dtech.rotadasaguas.domain.Tag;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;

public class SubAlimentacaoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_alimentacao);

        List<Tag> tags = getTagsSubAlimentacao(3);

        final ListView listView = (ListView) findViewById(R.id.subAlimentacao);
        listView.setAdapter(new TagSubAlimentacaoAdapter(this, tags));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

              /*  LikeButton likeButton = (LikeButton) view.findViewById(R.id.gostei);

                likeButton.setOnLikeListener(
                        new OnLikeListener() {
                            @Override
                            public void liked(LikeButton likeButton) {
                                TextView t = (TextView) view.findViewById(R.id.local);
                                t.setTextColor(Color.BLUE);
                                Toast.makeText(getApplicationContext(), "Posicao LIKE:"+position, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void unLiked(LikeButton likeButton) {
                                Toast.makeText(getApplicationContext(), "Posicao UNLIKE:"+position, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
               */


                TextView c = (TextView) view.findViewById(R.id.local);
                LikeButton lk = (LikeButton) view.findViewById(R.id.gostei);

                lk.setEnabled(false);


                //OBTEM A COR EM INTEIRO E CONVERTE PARA HEXADECIMAL
                Integer intColor = c.getCurrentTextColor();
                String hexColor = "#" + Integer.toHexString(intColor).substring(2);

                if (hexColor.equalsIgnoreCase("#2196F3")){
                    c.setTextColor(Color.parseColor("#aaaaaa"));
                    lk.setLiked(false);

                }else{
                    c.setTextColor(Color.parseColor("#2196F3"));
                    lk.setEnabled(true);
                    lk.setLiked(true);

                }

            }
        });

    }

    public List<Tag> getTagsSubAlimentacao(int qtd){
        String[] tags = new String[]{"Trailers", "Restaurantes", "Bar e Pubs"};
        Boolean[] likes = new Boolean[]{false};
        List<Tag> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            Tag c = new Tag( tags[i % tags.length], likes[i % likes.length]);
            listAux.add(c);
        }
        return(listAux);
    }
}