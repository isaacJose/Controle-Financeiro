package com.example.controlefinanceiro.receita;

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

public class ListReceita extends AppCompatActivity {

    //private ImageButton bt_voltar;
    private ListView lv_receitas;
    private ReceitaDAO receitaDAO;
    private List<Receita> receitas;
    private ArrayList<Receita> receitasfiltradas = new ArrayList<>(); //ArrayList que será passado para ReceitaAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_receita);

        configurarBotoes(); acaoBotao();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        receitaDAO = new ReceitaDAO(this);
        receitas = receitaDAO.obterTodos();
        receitasfiltradas.addAll(receitas);

        //Passando arrayList - receitasfiltradas - para o ArrayAdaptador (para que assim os dados possam ir para a ListView)
        ReceitaAdapter receitaAdapter = new ReceitaAdapter(this, receitasfiltradas);
        lv_receitas.setAdapter(receitaAdapter);
        registerForContextMenu(lv_receitas);
    }

    private void configurarBotoes() {
        lv_receitas = findViewById(R.id.lv_receitas);
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
        menuInflater.inflate(R.menu.menureceita2,menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.Buscar).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //À medida que for digitando
            @Override                                                           //sera chamada a função buscar
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarReceita(newText);
                return false;
            }
        });
        return true;
    }

    public void cadastrar(MenuItem menuItem){
        startActivity(new Intent(this,CadReceita.class));
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
    public void buscarReceita(String nome){
        receitasfiltradas.clear();
        for(Receita receita: receitas){
            if(receita.getDescricao().toLowerCase().contains(nome.toLowerCase()) ||
                    receita.getTipo().toLowerCase().contains(nome.toLowerCase()) ||
                    receita.getCategoria().toLowerCase().contains(nome.toLowerCase())){ //deixando letras minúsculas e verificando se nome e descrição
                receitasfiltradas.add(receita);
            }
        }
        lv_receitas.invalidateViews();
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto, contextMenu);
    }

    public boolean onContextItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.excluir){
            AdapterView.AdapterContextMenuInfo menuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            final Receita receitaExluir = receitasfiltradas.get(menuInfo.position);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Atenção")
                    .setMessage("Realmente deseja excluir a receita?")
                    .setNegativeButton("NÃO",null)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            receitasfiltradas.remove(receitaExluir);
                            receitas.remove(receitaExluir);
                            receitaDAO.excluir(receitaExluir);
                            lv_receitas.invalidateViews();
                        }
                    }).create();
            dialog.show();
            return true;
        }
        else if (id == R.id.atualizar){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final Receita receitaAtualizar = receitasfiltradas.get(menuInfo.position);

            Intent intent = new Intent(ListReceita.this,CadReceita.class);
            intent.putExtra("receita",receitaAtualizar);
            startActivity(intent);
            return true;
        }
        return onContextItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        receitas = receitaDAO.obterTodos();
        receitasfiltradas.clear();
        receitasfiltradas.addAll(receitas);
        lv_receitas.invalidateViews();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
