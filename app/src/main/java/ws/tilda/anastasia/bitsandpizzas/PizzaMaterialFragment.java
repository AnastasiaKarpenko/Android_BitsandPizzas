package ws.tilda.anastasia.bitsandpizzas;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PizzaMaterialFragment extends Fragment {


    public PizzaMaterialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView pizzaRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_pizza_material,
                container, false);
        String[] pizzaNames = new String[Pizza.pizzas.length];
        for(int i = 0; i < pizzaNames.length; i ++) {
            pizzaNames[i] = Pizza.pizzas[i].getName();
        }

        int[] pizzaImages = new int[Pizza.pizzas.length];
        for (int i = 0; i < pizzaImages.length; i ++) {
            pizzaImages[i] = Pizza.pizzas[i].getImageResourceId();
        }

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(pizzaNames, pizzaImages);
        pizzaRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pizzaRecycler.setLayoutManager(layoutManager);


        //Method onClick implementation of Listener interface which launches Pizza Detail Activity
        adapter.setListener(new CaptionedImagesAdapter.Listener() { public void onClick(int position) {
            Intent intent = new Intent(getActivity(), PizzaDetailActivity.class);
            intent.putExtra(PizzaDetailActivity.EXTRA_PIZZANO, position);
            getActivity().startActivity(intent);
          }
        });

        return pizzaRecycler;

    }

}
