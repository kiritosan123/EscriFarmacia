package com.example.david.escrifarmacia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.david.escrifarmacia.Interface.ItemClickListener;
import com.example.david.escrifarmacia.Model.Medicament;
import com.example.david.escrifarmacia.ViewHolder.MedicamentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MedicamentList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference medicamentList;

    String categoryId="";

    FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder> adapter;

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
                        Toast.makeText(MedicamentList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        //Cofiguramos el adapter
        recyclerView.setAdapter(adapter);
    }
}
