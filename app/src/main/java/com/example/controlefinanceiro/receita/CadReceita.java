package com.example.controlefinanceiro.receita;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.controlefinanceiro.R;

import java.text.NumberFormat;
import java.util.Locale;

public class CadReceita extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_descricao;
    private Spinner catReceita;
    private RadioButton rb_fixa, rb_variavel;
    private RadioGroup rb_group;
    private String categoriaReceita;
    private String tipo;
    private EditText et_valor;
    private Button bt_cadastrar;
    //private Button bt_voltar;

    private ReceitaDAO receitaDAO;
    private Receita receita = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_receita);

        configurarBotoes();
        acaoBotao();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        receitaDAO = new ReceitaDAO(this);

        //Recebendo objeto Receite vindo de outra activity(ListReceita) para ser atualizado
        Intent intent = getIntent();
        if (intent.hasExtra("receita")){
            receita = (Receita) intent.getSerializableExtra("receita");
            et_descricao.setText(receita.getDescricao());
            et_valor.setText(""+receita.getValor()*10);

            Resources res = getResources();
            String[] cat = res.getStringArray(R.array.catReceita); //pegando todas as categorias de receita
            int position = 0;

            for (int i=0; i<=(cat.length - 1); i++){ //encontrando a categoria da receita
                if (cat[i].equals(receita.getCategoria())){
                    position = i;
                    break;
                }
            }
            catReceita.setSelection(position);
            categoriaReceita = cat[position];

            //Setando RadioButton
            if (receita.getTipo().equals(rb_fixa.getText())){
                rb_fixa.setChecked(true);
                tipo = rb_fixa.getText().toString();
            }
            else{
                rb_variavel.setChecked(true);
                tipo = rb_variavel.getText().toString();
            }
        }

    }

    @Override
    protected void onResume(){
        super.onResume();

        if (categoriaReceita == null){
            catReceita.setOnItemSelectedListener(this);
            carregarDadosSpinner();
        }

        rb_fixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = rb_fixa.getText().toString();
            }
        });

        rb_variavel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = rb_variavel.getText().toString();
            }
        });
    }

    private void configurarBotoes() {

        et_descricao = findViewById(R.id.et_descricao);
        et_valor = findViewById(R.id.et_valor);
        rb_fixa = findViewById(R.id.rb_fixa);
        rb_variavel = findViewById(R.id.rb_variavel);
        rb_group = findViewById(R.id.rb_group);
        catReceita = findViewById(R.id.catDespesa);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        //bt_voltar = findViewById(R.id.bt_voltar);

        catReceita.setOnItemSelectedListener(this);
        carregarDadosSpinner();

        //MODIFICAR O VALOR EM TEMPO DE EXECUÇÃO
        et_valor.addTextChangedListener(new TextWatcher() {

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final Locale locale = new Locale("en","US");
                //SE A STRING ATUAL FOR DIFERENTE DE ""
                if (!s.equals(current)) {
                    //Montando a máscara
                    et_valor.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll(getString(R.string.charMoeda), "");
                    double parsed = Double.parseDouble(cleanString);
                    String formattedString = NumberFormat.getCurrencyInstance(locale).format((parsed/100));

                    current = formattedString;

                    et_valor.setText(formattedString);
                    et_valor.setSelection(formattedString.length());
                    et_valor.addTextChangedListener(this);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void acaoBotao() {

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaCampos()) {
                    AlertDialog dialog = new AlertDialog.Builder(CadReceita.this)
                            .setTitle("Aviso")
                            .setMessage("Há campos inválidos ou em branco!")
                            .setNeutralButton("OK", null).create();
                    dialog.show();

                }

                else if (receita == null) {
                        Receita receita = new Receita();
                        receita.setDescricao(et_descricao.getText().toString());
                        receita.setValor(Double.parseDouble(et_valor.getText().toString().replaceAll(getString(R.string.charMoeda),"")) / 100);
                        receita.setCategoria(categoriaReceita);
                        receita.setTipo(tipo);
                        long id = receitaDAO.inserir(receita);
                        Toast.makeText(CadReceita.this, "Receita "+id+" cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                        CadReceita.this.finish();
                }

                else {
                    receita.setDescricao(et_descricao.getText().toString());
                    receita.setValor((Double.parseDouble(et_valor.getText().toString().replaceAll(getString(R.string.charMoeda), ""))) / 100);
                    receita.setCategoria(categoriaReceita);
                    receita.setTipo(tipo);
                    receitaDAO.atualizar(receita);
                    Toast.makeText(CadReceita.this, "Receita atualizada!", Toast.LENGTH_SHORT).show();
                    finish();
                }
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

    private boolean validaCampos() {

        boolean res = false;

        String descricao = et_descricao.getText().toString();
        String valor = et_valor.getText().toString();

        if (isCampoVazio(descricao)) {
            et_descricao.requestFocus();
            res = true;
        }
        else {
            if (isCampoVazio(valor) || valor.equals("$0.00")) {
                et_valor.requestFocus();
                res = true;
            }
            else {
                if (tipo == null || categoriaReceita == null){
                    res = true;
                }
            }
        }

        if (res) {
            return true;
        }
        return false;
    }

    private boolean isCampoVazio(String valor) {

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());   //Empty verifica se a string está vazia e Trim retira todos os espaços
        return resultado;
    }

    public void carregarDadosSpinner() {  //Função própria do Android Studio
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (
                        this,
                        R.array.catReceita,
                        R.layout.support_simple_spinner_dropdown_item
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catReceita.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (catReceita.getItemAtPosition(position).toString().equals("Selecione uma opção")) {
            categoriaReceita = null;
        } else if (catReceita.getItemAtPosition(position).toString().equals("Salário")) {
            categoriaReceita = "Salário";
        } else if (catReceita.getItemAtPosition(position).toString().equals("Investimento")) {
            categoriaReceita = "Investimento";
        } else if (catReceita.getItemAtPosition(position).toString().equals("Venda")) {
            categoriaReceita = "Vendas";
        } else if (catReceita.getItemAtPosition(position).toString().equals("Outras")) {
            categoriaReceita = "Outras";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
