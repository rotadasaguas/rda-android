package to.dtech.rotadasaguas.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

import to.dtech.rotadasaguas.LocalActivity;
import to.dtech.rotadasaguas.R;
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;

public class AlimentacaoAdapter extends RecyclerView.Adapter<AlimentacaoAdapter.MyViewHolder> {
    private List<ItemLocal> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public AlimentacaoAdapter(Context c, List<ItemLocal> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_minha_rota, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Log.i("LOG", "onBindViewHolder()");
        myViewHolder.ivCar.setText( mList.get(position).getPhoto() );
        myViewHolder.tvModel.setText(mList.get(position).getNome() );
        myViewHolder.tvBrand.setText( mList.get(position).getDescricao() );
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(ItemLocal c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public IconTextView ivCar;
        public TextView tvModel;
        public TextView tvBrand;

        public MyViewHolder(View itemView) {
            super(itemView);

           // ivCar = (ImageView) itemView.findViewById(R.id.iv_car);
            ivCar = (IconTextView) itemView.findViewById(R.id.iv_car);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
            Intent intent = new Intent(v.getContext(), LocalActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}
