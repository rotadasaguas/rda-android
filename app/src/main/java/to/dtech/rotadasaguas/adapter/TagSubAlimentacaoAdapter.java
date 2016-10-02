package to.dtech.rotadasaguas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tag tag = tags.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoute = inflater.inflate(R.layout.item_sub, null);

        TextView titulo = (TextView) layoute.findViewById(R.id.local);
        LikeButton lk = (LikeButton) layoute.findViewById(R.id.gostei);
        titulo.setText(tag.getTitulo());
        lk.setLiked(tag.getAtivo());
        return layoute;
    }
}

