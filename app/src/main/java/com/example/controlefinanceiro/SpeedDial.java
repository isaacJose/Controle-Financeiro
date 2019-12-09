package com.example.controlefinanceiro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.controlefinanceiro.despesa.DespesaDAO;
import com.example.controlefinanceiro.despesa.DespesaView;
import com.example.controlefinanceiro.despesa.ListDespesa;
import com.example.controlefinanceiro.receita.ListReceita;
import com.example.controlefinanceiro.receita.ReceitaDAO;
import com.example.controlefinanceiro.receita.ReceitaView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DecimalFormat;

public class SpeedDial extends AppCompatActivity {

    private FloatingActionMenu actionMenu;
    private FloatingActionButton actionButtonReceita;
    private FloatingActionButton actionButtonDespesa;

    private TextView tv_saldoTotal;
    private ReceitaDAO receitaDAO;
    private DespesaDAO despesaDAO;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_dial);

        configurarBotoes(); acaoBotao();
    }

    @Override
    protected void onResume(){
        super.onResume();
        receitaDAO = new ReceitaDAO(SpeedDial.this);
        despesaDAO = new DespesaDAO(SpeedDial.this);
        df = new DecimalFormat("###,##0.00"); //Própria função do Java

        float receita = Float.parseFloat(df.format(receitaDAO.totalReceita()).replaceAll(getString(R.string.charMoeda),""))/100;
        float despesa = Float.parseFloat(df.format(despesaDAO.totalDespesa()).replaceAll(getString(R.string.charMoeda),""))/100;

        tv_saldoTotal.setText(df.format(receita-despesa));
        /*if (Float.parseFloat(tv_saldoTotal.getText().toString().replaceAll("[R,$.]","")) >= 0){
            tv_saldoTotal.setTextColor(Color.parseColor("#41F304"));
        }
        else {
            tv_saldoTotal.setTextColor(Color.parseColor("#FF0000"));

        }*/
    }

    private void configurarBotoes() {

        actionMenu = findViewById(R.id.fabPrincipal);
        actionButtonReceita = findViewById(R.id.receita);
        actionButtonDespesa = findViewById(R.id.despesa);
        tv_saldoTotal = findViewById(R.id.tv_saldoTotal);
        actionMenu.setClosedOnTouchOutside(true);
    }

    private void acaoBotao(){
        actionButtonReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpeedDial.this, ReceitaView.class));
            }
        });

        actionButtonDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpeedDial.this, DespesaView.class));
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
            receitaDAO = new ReceitaDAO(SpeedDial.this);

            new AlertDialog.Builder(SpeedDial.this)
                    .setMessage("Valor total de Receitas: \n"+df.format(receitaDAO.totalReceita()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Todas", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(SpeedDial.this, ListReceita.class)); }
                    }).show();
            return true;
        }

        else if (id == R.id.verDespesa){

            df = new DecimalFormat("###,##0.00"); //Própria função do Java
            despesaDAO = new DespesaDAO(SpeedDial.this);
            new AlertDialog.Builder(SpeedDial.this)
                    .setMessage("Valor total de Despesas: \n"+df.format(despesaDAO.totalDespesa()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Todas", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(SpeedDial.this, ListDespesa.class)); }
                    }).show();
            return true;
        }
        else if (id == R.id.Sobre){
            AlertDialog alertDialog = new AlertDialog.Builder(SpeedDial.this)
                    .setMessage(R.string.info)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return true;
        }
        else if (id == R.id.Sair) {
            AlertDialog alertDialog = new AlertDialog.Builder(SpeedDial.this)
                    .setMessage("Deseja realmente sair?")
                    .setCancelable(false)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SpeedDial.this.finish();
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
                        SpeedDial.this.finish();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
