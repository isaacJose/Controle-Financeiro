package com.example.controlefinanceiro.receita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.controlefinanceiro.R;

public class ReceitaView extends AppCompatActivity {

    private Button bt_cadReceita;
    private Button bt_listReceita;
    private Button bt_geRelatorio;
    private Button bt_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        configurarBotoes(); acaoBotao();

    }

    private void configurarBotoes() {

        bt_cadReceita = findViewById(R.id.bt_cadReceita);
        bt_listReceita = findViewById(R.id.bt_listReceita);
        bt_voltar = findViewById(R.id.bt_voltar);
    }

    private void acaoBotao(){
        bt_cadReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceitaView.this, CadReceita.class));
            }
        });

        bt_listReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceitaView.this, ListReceita.class));
            }
        });

        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuPrincipal) {
            finish();
            return true;
        }
        return onOptionsItemSelected(item);
    }
}
