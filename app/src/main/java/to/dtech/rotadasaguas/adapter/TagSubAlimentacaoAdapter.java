package to.dtech.rotadasaguas.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.like.LikeButton;
import java.util.List;

import to.dtech.rotadasaguas.R;
import to.dtech.rotadasaguas.domain.Tag;

public class TagSubAlimentacaoAdapter extends BaseAdapter {
    private Context context;
    private List<Tag> tags;

    public TagSubAlimentacaoAdapter(Context context, List<Tag> tags){
        this.context = context;
        this.tags = tags;

    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {

        final TextView titulo;
        final LikeButton lk;

        public ViewHolder(View view) {

            titulo = (TextView) view.findViewById(R.id.local);
            lk = (LikeButton) view.findViewById(R.id.gostei);

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if( convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_sub, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        boolean likeValue = tags.get(position).getAtivo();
        if(likeValue){
            holder.titulo.setTextColor(Color.parseColor("#2196F3"));
        }else{
            holder.titulo.setTextColor(Color.parseColor("#aaaaaa"));
        }
        holder.lk.setLiked(likeValue);


        Tag tag = tags.get(position);
        holder.titulo.setText(tag.getTitulo());

        return view;
    }

    public void alteraCor(int position){
        tags.get(position).setAtivo(true);
    }
    public void removeCor(int position){
        tags.get(position).setAtivo(false);
    }
}

