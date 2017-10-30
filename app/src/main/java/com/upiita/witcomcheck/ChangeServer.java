package com.upiita.witcomcheck;

import android.content.ActivityNotFoundException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeServer extends AppCompatActivity {
    private static String SERVER = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_server);
        final EditText server = (EditText)findViewById(R.id.server);
        server.setText(Check.SERVER);
        final Button buttonReader = (Button)findViewById(R.id.cargar);

        buttonReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Check.SERVER = server.getText().toString();
                    Toast.makeText(getApplicationContext(), "Configuración cargada" + server.getText().toString(),
                            Toast.LENGTH_LONG).show();
                    finish();
                } catch (ActivityNotFoundException exception) {
                }
            }
        });
    }
}
