package to.dtech.rotadasaguas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import to.dtech.rotadasaguas.R;
import to.dtech.rotadasaguas.domain.Agencia;

public class AgenciaAdapter extends BaseAdapter {
    private Context context;
    private List<Agencia> agencias;

    public AgenciaAdapter(Context context, List<Agencia> agencias){
        this.context = context;
        this.agencias = agencias;

    }

    @Override
    public int getCount() {
        return agencias.size();
    }

    @Override
    public Object getItem(int position) {
        return agencias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {

        final TextView nomeAgencia;
        final TextView telefone;
        final TextView endereco;

        public ViewHolder(View view) {

            nomeAgencia = (TextView) view.findViewById(R.id.nomeAgencia);
            telefone = (TextView) view.findViewById(R.id.telefone);
            endereco = (TextView) view.findViewById(R.id.endereco);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if( convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_agencia, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        Agencia agencia = agencias.get(position);
        holder.nomeAgencia.setText(agencia.getNome());
        holder.telefone.setText(agencia.getTelefone());
        holder.endereco.setText(agencia.getEndereco());

        return view;
    }

}

