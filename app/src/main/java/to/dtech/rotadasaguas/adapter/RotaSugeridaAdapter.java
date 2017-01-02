package to.dtech.rotadasaguas.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

import to.dtech.rotadasaguas.R;
import to.dtech.rotadasaguas.domain.RotaSugerida;

public class RotaSugeridaAdapter extends BaseAdapter {
    private Context context;
    private List<RotaSugerida> sugestoes;

    public RotaSugeridaAdapter(Context context, List<RotaSugerida> sugestoes){
        this.context = context;
        this.sugestoes = sugestoes;

    }

    @Override
    public int getCount() {
        return sugestoes.size();
    }

    @Override
    public Object getItem(int position) {
        return sugestoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {


        final TextView nome;
        final IconTextView icone;

        public ViewHolder(View view) {

            nome = (TextView) view.findViewById(R.id.nomeRota);
            icone = (IconTextView) view.findViewById(R.id.iconRota);

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if( convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_rota_sugerida, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        RotaSugerida sugestao = sugestoes.get(position);


        holder.nome.setText(sugestao.getNome());
        holder.icone.setText(sugestao.getIcone());

        return view;
    }

}

