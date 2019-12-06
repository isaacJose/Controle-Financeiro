package com.example.controlefinanceiro.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.controlefinanceiro.R;

import java.util.ArrayList;
import java.util.List;

public class ListDespesa extends AppCompatActivity {

    //private ImageButton bt_voltar;
    private ListView lv_despesas;
    private DespesaDAO despesaDAO;
    List<Despesa> despesas;
    ArrayList<Despesa> despesasfiltradas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_despesa);

        configurarBotoes(); acaoBotao();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        despesaDAO = new DespesaDAO(this);
        despesas = despesaDAO.obterTodos();
        despesasfiltradas.addAll(despesas);

        DespesaAdapter despesaAdapter = new DespesaAdapter(this,despesasfiltradas);
        lv_despesas.setAdapter(despesaAdapter);
        registerForContextMenu(lv_despesas);

    }

    private void configurarBotoes() {

        lv_despesas = findViewById(R.id.lv_despesas);
        //bt_voltar = findViewById(R.id.bt_voltar);
    }

    private void acaoBotao(){

        /*bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_despesa,menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.Buscar).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //À medida que for digitando
            @Override                                                           //sera chamada a função buscar
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscaDespesa(newText);
                return false;
            }
        });

        return true;
    }

    public void buscaDespesa(String nome){
        despesasfiltradas.clear();
        for(Despesa despesa: despesas){
            if(despesa.getDescricao().toLowerCase().contains(nome.toLowerCase()) ||
                    despesa.getTipo().toLowerCase().contains(nome.toLowerCase()) ||
                    despesa.getCategoria().toLowerCase().contains(nome.toLowerCase())){ //deixando letras minúsculas e verificando se nome e descrição
                despesasfiltradas.add(despesa);
            }
        }
        lv_despesas.invalidateViews();
    }
    public void cadastrar(MenuItem menuItem){
        startActivity(new Intent(ListDespesa.this, CadDespesa.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case R.id.menuPrincipal: finish(); break;

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_desp_contexto, contextMenu);
    }

    public boolean onContextItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.Excluir){
            AdapterView.AdapterContextMenuInfo menuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final Despesa despesaExcluir = despesasfiltradas.get(menuInfo.position);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Atenção")
                    .setMessage("Realmente deseja excluir a despesa?")
                    .setNegativeButton("NÃO",null)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                despesasfiltradas.remove(despesaExcluir);
                                despesas.remove(despesaExcluir);
                                despesaDAO.excluir(despesaExcluir);
                                lv_despesas.invalidateViews();
                        }
                    }).create();
            dialog.show();
            return true;
        }

        else if (id == R.id.Atualizar){
            AdapterView.AdapterContextMenuInfo menuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final Despesa despesaAtualizar = despesasfiltradas.get(menuInfo.position);

            Intent intent = new Intent(ListDespesa.this,CadDespesa.class);
            intent.putExtra("despesa",despesaAtualizar);
            startActivity(intent);
            return true;
        }

        return onContextItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        despesas = despesaDAO.obterTodos();
        despesasfiltradas.clear();
        despesasfiltradas.addAll(despesas);
        lv_despesas.invalidateViews();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
