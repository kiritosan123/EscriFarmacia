package com.example.david.escrifarmacia;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.david.escrifarmacia.Database.Database;
import com.example.david.escrifarmacia.Model.Medicament;
import com.example.david.escrifarmacia.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MedicamentDetail extends AppCompatActivity {

    TextView medicament_name, medicament_price, medicament_description;
    ImageView medicament_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String medicamentId="";

    //creamos FIREBASE
    FirebaseDatabase database;
    DatabaseReference medicament;

    Medicament currentMedicament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_detail);

        //iniciamos FIREBASE
        database = FirebaseDatabase.getInstance();
        medicament = database.getReference("Medicament");

        //Inicamos la vista (view)
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        medicamentId,
                        currentMedicament.getName(),
                        numberButton.getNumber(),
                        currentMedicament.getPrice(),
                        currentMedicament.getDiscount()
                ));
                Toast.makeText(MedicamentDetail.this, "Se adiciono al Carrito", Toast.LENGTH_SHORT).show();
            }
        });

        medicament_description = (TextView)findViewById(R.id.medicament_description);
        medicament_name = (TextView)findViewById(R.id.medicament_name);
        medicament_price = (TextView)findViewById(R.id.medicament_price);
        medicament_image = (ImageView)findViewById(R.id.img_medicament);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Obtenemos el Id del Intent
        if(getIntent() != null)
            medicamentId = getIntent().getStringExtra("MedicamentId");
        if(!medicamentId.isEmpty()){
            getdetailMedicament(medicamentId);
        }
    }

    private void getdetailMedicament(String medicamentId){
        medicament.child(medicamentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentMedicament = dataSnapshot.getValue(Medicament.class);

                //Establecer imagen
                Picasso.with(getBaseContext()).load(currentMedicament.getImage())
                        .into(medicament_image);
                //Establecemos el titulo
                collapsingToolbarLayout.setTitle(currentMedicament.getName());
                //Establecemos el precio
                medicament_price.setText(currentMedicament.getPrice());
                //Establecemos el nombre
                medicament_name.setText(currentMedicament.getName());
                //Establecemos el precio
                medicament_description.setText(currentMedicament.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
