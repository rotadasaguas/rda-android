package to.dtech.rotadasaguas.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import to.dtech.rotadasaguas.R;
import to.dtech.rotadasaguas.adapter.ItensAdapter;
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;
import to.dtech.rotadasaguas.util.HttpHandler;

public class AcomodacaoFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    public static List<ItemLocal> mList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_minha_rota, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_alimentacao);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        ImageView imgSad = (ImageView) rootView.findViewById(R.id.imagemSad);
        TextView txtSad = (TextView) rootView.findViewById(R.id.textoSad);

        mRecyclerView.setLayoutManager(llm);

        if (mList == null){
            imgSad.setVisibility(View.VISIBLE);
            txtSad.setVisibility(View.VISIBLE);
        }else{
            ItensAdapter adapter = new ItensAdapter(getActivity(), mList);
            adapter.setRecyclerViewOnClickListenerHack(this);

            mRecyclerView.setAdapter( adapter );
        }


        return rootView;
    }

    @Override
    public void onClickListener(View view, int position) {
      /*  ItensAdapter adapter = (ItensAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);*/
    }

}