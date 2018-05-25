package com.example.david.escrifarmacia.Interface;

//funcion para cuando se hace click a uno de las imagenes ... se pasa a otra UI donde esta el detalle

import android.view.View;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
