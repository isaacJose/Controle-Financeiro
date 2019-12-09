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
    //private Button bt_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        configurarBotoes(); acaoBotao();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

    }

    private void configurarBotoes() {

        bt_cadReceita = findViewById(R.id.bt_cadReceita);
        bt_listReceita = findViewById(R.id.bt_listReceita);
        //bt_voltar = findViewById(R.id.bt_voltar);
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

        /*bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
       finish();
    }
}
