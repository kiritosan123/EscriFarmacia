package com.example.david.escrifarmacia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.david.escrifarmacia.Interface.ItemClickListener;
import com.example.david.escrifarmacia.Model.Medicament;
import com.example.david.escrifarmacia.ViewHolder.MedicamentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MedicamentList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference medicamentList;

    String categoryId="";

    FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder> adapter;

    //Busqueda funcional
    FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_list);

        //Conexion con FIREBASE
        database = FirebaseDatabase.getInstance();
        medicamentList = database.getReference("Medicament");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_medicament);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Obtenemos el Intent desde home aqui
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null) {
            loadListMedicament(categoryId);
        }

        //Busqueda
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Ingresa tu Medicamento");
        //materialSearchBar.setSpeechMode(false);
        loadSuggest(); // Escribiremos la funcion sugerir(suggest) para cargarlos al FIREBASE
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Cuando el usuario escriba su texto, los cambiaremos a una lista de sugerencia

                List<String> suggest = new ArrayList<String>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Cuando nuestra barra de sugerencia es cerrado
                //Restauramos el adaptador incial
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Cuando la busqueda termina
                //Mostramos el resultado de la busqueda, en el adaptador de busqueda
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder>(
                Medicament.class,
                R.layout.medicament_item,
                MedicamentViewHolder.class,
                medicamentList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(MedicamentViewHolder viewHolder, Medicament model, int position) {
                viewHolder.medicament_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.medicament_image);

                final Medicament local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Iniciamos nuestra Activity nuevo
                        Intent medicamentDetail = new Intent(MedicamentList.this, MedicamentDetail.class);
                        //enviamos el id del medicamento a la nueva actividad
                        medicamentDetail.putExtra("MedicamentId", searchAdapter.getRef(position).getKey()); //
                        startActivity(medicamentDetail);
                    }
                });

            }
        };
        //ponemos el adaptador para el Recycler View para su resultado de la busqueda
        recyclerView.setAdapter(searchAdapter);

    }

    private void loadSuggest() {
        medicamentList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Medicament item = postSnapshot.getValue(Medicament.class);
                    suggestList.add(item.getName()); // adicionamos el nombre del medicamento para sugerir a la lista

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListMedicament(String categoryId){
        adapter = new FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder>(Medicament.class,
                R.layout.medicament_item,
                MedicamentViewHolder.class,
                medicamentList.orderByChild("MenuId").equalTo(categoryId) //como hacer: Select * from Medicament where MednuId=
                ) {
            @Override
            protected void populateViewHolder(MedicamentViewHolder viewHolder, Medicament model, int position) {
                viewHolder.medicament_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.medicament_image);

                final Medicament local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Iniciamos nuestra Activity nuevo
                        Intent medicamentDetail = new Intent(MedicamentList.this, MedicamentDetail.class);
                        //enviamos el id del medicamento a la nueva actividad
                        medicamentDetail.putExtra("MedicamentId", adapter.getRef(position).getKey()); //
                        startActivity(medicamentDetail);
                    }
                });
            }
        };

        //Cofiguramos el adapter
        recyclerView.setAdapter(adapter);
    }
}
