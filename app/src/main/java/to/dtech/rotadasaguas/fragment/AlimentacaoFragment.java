package to.dtech.rotadasaguas.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import to.dtech.rotadasaguas.MinhaRota;
import to.dtech.rotadasaguas.R;
import to.dtech.rotadasaguas.adapter.AlimentacaoAdapter;
import to.dtech.rotadasaguas.domain.ItemLocal;
import to.dtech.rotadasaguas.interfaces.RecyclerViewOnClickListenerHack;

public class AlimentacaoFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<ItemLocal> mList;

    private List<String> listaDeDadosNomes = new ArrayList<String>();
    private List<String> listaDeDadosDesc = new ArrayList<String>();
    private List<String> listaDeDadosEnd = new ArrayList<String>();

    private String tags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alimentacao, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_alimentacao);
        mRecyclerView.setHasFixedSize(true);

        Log.d("Dentro", getArguments().getString("url").toString());

       /* mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                AlimentacaoAdapter adapter = (AlimentacaoAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    List<ItemLocal> listAux = ((MinhaRota) getActivity()).getSetCarList(10);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });
        */

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mList = getLocaisAlimentacao();
        AlimentacaoAdapter adapter = new AlimentacaoAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );

        return rootView;
    }

    @Override
    public void onClickListener(View view, int position) {
      /*  AlimentacaoAdapter adapter = (AlimentacaoAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);*/
    }

    public List<ItemLocal> getLocaisAlimentacao(){

        List<ItemLocal> listAux = new ArrayList<>();
        listaDeDadosNomes.add("lol");
        listaDeDadosDesc.add("lol");
        listaDeDadosEnd.add("lol");

        for(int i = 0; i <= listaDeDadosNomes.size()-1; i++){
            ItemLocal c = new ItemLocal( listaDeDadosNomes.get(i), listaDeDadosDesc.get(i), listaDeDadosEnd.get(i) );
            listAux.add(c);
        }
        return(listAux);
    }
}
