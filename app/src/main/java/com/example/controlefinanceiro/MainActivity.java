package com.example.controlefinanceiro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.controlefinanceiro.despesa.DespesaDAO;
import com.example.controlefinanceiro.despesa.DespesaView;
import com.example.controlefinanceiro.receita.ReceitaDAO;
import com.example.controlefinanceiro.receita.ReceitaView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


    private TextView tv_saldoTotal;
    private Button bt_receita;
    private Button bt_despesa;
    private Button bt_sair;

    private ReceitaDAO receitaDAO;
    private DespesaDAO despesaDAO;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurarBotoes(); acaoBotao();

    }

    @Override
    protected void onResume(){
        super.onResume();
        receitaDAO = new ReceitaDAO(MainActivity.this);
        despesaDAO = new DespesaDAO(MainActivity.this);
        df = new DecimalFormat("###,##0.00"); //Própria função do Java

        float receita = Float.parseFloat(df.format(receitaDAO.totalReceita()).replaceAll(getString(R.string.charMoeda),""))/100;
        float despesa = Float.parseFloat(df.format(despesaDAO.totalDespesa()).replaceAll(getString(R.string.charMoeda),""))/100;

        tv_saldoTotal.setText(String.valueOf(receita-despesa));
        if (Float.parseFloat(tv_saldoTotal.getText().toString()) > 0){
            tv_saldoTotal.setTextColor(Color.parseColor("#41F304"));
        }
        else {
            tv_saldoTotal.setTextColor(Color.parseColor("#FF0000"));

        }
    }


    private void configurarBotoes() {

        tv_saldoTotal = findViewById(R.id.tv_saldoTotal);
        bt_receita = findViewById(R.id.bt_receita);
        bt_despesa = findViewById(R.id.bt_despesa);
        bt_sair = findViewById(R.id.bt_sair);

    }

    private void acaoBotao(){

        bt_receita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReceitaView.class));
            }
        });

        bt_despesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DespesaView.class));
            }
        });

        bt_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Deseja realmente sair?")
                        .setCancelable(false)
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("NÃO",null)
                        .show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_receita,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.verReceita){

            df = new DecimalFormat("###,##0.00"); //Própria função do Java
            receitaDAO = new ReceitaDAO(MainActivity.this);

            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Valor total de Receitas: \n"+df.format(receitaDAO.totalReceita()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return true;
        }

        else if (id == R.id.verDespesa){

            df = new DecimalFormat("###,##0.00"); //Própria função do Java
            despesaDAO = new DespesaDAO(MainActivity.this);
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Valor total de Despesas: \n"+df.format(despesaDAO.totalDespesa()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return true;
        }
        else if (id == R.id.Sobre){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage(R.string.info)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return true;
        }
        else if (id == R.id.Sair) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Deseja realmente sair?")
                    .setCancelable(false)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("NÃO",null)
                    .show();
            return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Deseja realmente sair?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

}
