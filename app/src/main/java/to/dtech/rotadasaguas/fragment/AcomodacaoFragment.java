package to.dtech.rotadasaguas.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<ItemLocal> mList;

    private List<String> listaDeDadosNomes = new ArrayList<String>();
    private List<String> listaDeDadosDesc = new ArrayList<String>();
    private List<String> listaDeDadosEnd = new ArrayList<String>();

    private String argsServer;
    private String cidadeServer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alimentacao, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_alimentacao);
        mRecyclerView.setHasFixedSize(true);

        argsServer = getArguments().getString("url").toString();
        cidadeServer = getArguments().getString("cidade").toString();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        try {
            mList = getLocaisHospedagem(argsServer);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ItensAdapter adapter = new ItensAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );

        return rootView;
    }

    @Override
    public void onClickListener(View view, int position) {
      /*  ItensAdapter adapter = (ItensAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);*/
    }

    public List<ItemLocal> getLocaisHospedagem(final String args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> futureResult = executor.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {

                HttpHandler sh = new HttpHandler();
                String retorno;

                String dadosWS = sh.makeServiceCall(args);

                if (dadosWS != null){
                    retorno = dadosWS;
                }
                else{
                    retorno = null;
                }


                return retorno;
            }
        });

        //Obtendo um resultado da execucão da Thread
        String resultado = futureResult.get();

        if (resultado != null){
            try {
                JSONObject jsonObject = new JSONObject(resultado);

                JSONArray resultsArray = jsonObject.getJSONArray("locais");

                JSONObject r;

                System.out.print(resultsArray.length());

                for (int i = 0; i < resultsArray.length(); i++){
                    r = resultsArray.getJSONObject(i);

                    if (r.getString("categoria").equalsIgnoreCase("Hospedagem")) {
                        if (r.getString("cidade").equalsIgnoreCase(cidadeServer)) {
                            listaDeDadosNomes.add(r.getString("nome"));
                            listaDeDadosDesc.add(r.getString("descricao"));
                            listaDeDadosEnd.add(r.getString("rua").replace(" ", "+") + "+" + r.getString("cidade") + ",SP" + "," +  "Brasil" );
                        }
                    }

                }

            } catch (final Exception e) {
                Log.e("SCRIPT", "Json parsing error: " + e.getMessage());
            }
        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Sem conexão de dados!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        executor.shutdown();

        List<ItemLocal> listAux = new ArrayList<>();

        for(int i = 0; i <= listaDeDadosNomes.size()-1; i++){
            ItemLocal c = new ItemLocal( listaDeDadosNomes.get(i), listaDeDadosDesc.get(i), listaDeDadosEnd.get(i) );
            listAux.add(c);
        }
        return(listAux);


    }
}