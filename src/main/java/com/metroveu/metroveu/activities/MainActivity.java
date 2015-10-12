package com.metroveu.metroveu.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.fragments.ParadaFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         Activity: Ventana principal. Ocupa toda la pantalla.
         Fragment: Sub-ventana. Se cargan dentro de las activities.
                   Generalmente dentro de los tags FrameLayout. Si te fijas en el código,
                   cuando se infla o muestra un fragmento veras que se especifica el id del
                   componente en el que carga el fragment. En el ejemplo el id es content_frame
                   lo cual quiere decir que el layout del fragment ira dentro del tag con ese id.

         Las diferentes carpetas son para organizar las diferentes clases según sean activities,
         fragments o otras cosas. Solo preocupate por estos dos por ahora. data es donde ira lo de
         la base de datos y adapters ya vendra si siquiera los usamos en nuestra app asi que
         ignoralos por el momento.

         En cuanto a los layouts, son ficheros xml que contienen diferentes etiquetas que son
         componentes graficos o layouts de android. Si abres el archivo veras que hay Design y Text
         que te muestran o el xml o la version grafica de lo que se esta creando. La version grafica
         esta bien pero segun lo que usemos puede que no se pueda mostrar asi que mejor si intentas
         ir entendiendolo por el xml. Cada etiqueta suele tener un id con el que se referencia a
         ese elemento desde el codigo.

         Todos los strings y colores se ponen en ficheros aparte en el res/values. Mas
         especificamente existen los archivos colors.xml y strings.xml.
        */

        // Creates fragment
        ParadaFragment paradaFragment = new ParadaFragment();
        // Adds fragment to content frame (see res/layout/activity_main.xml) in the activity_main layout
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, paradaFragment).commit();
    }

}