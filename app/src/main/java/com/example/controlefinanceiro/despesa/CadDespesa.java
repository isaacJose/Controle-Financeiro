package com.example.controlefinanceiro.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.controlefinanceiro.receita.CadReceita;

import java.text.NumberFormat;

public class CadDespesa extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private EditText et_descricao;
    private Spinner catDespesa;
    private RadioButton rb_fixa, rb_variavel;
    private RadioGroup rb_group;
    private String categoriaDespesa;
    private String tipo;
    private EditText et_valor;
    private Button bt_cadastrar;
    private Button bt_voltar;

    private DespesaDAO despesaDAO;
    private Despesa despesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_despesa);

        configurarBotoes();
        acaoBotao();

        despesaDAO = new DespesaDAO(this);

        //Recebendo objeto Receite vindo de outra activity(ListReceita) para ser atualizado
        Intent intent = getIntent();
        if (intent.hasExtra("despesa")){
            despesa = (Despesa) intent.getSerializableExtra("despesa");
            et_descricao.setText(despesa.getDescricao());
            et_valor.setText(""+despesa.getValor()*10);

            Resources res = getResources();
            String[] cat = res.getStringArray(R.array.catDespesa); //pegando todas as categorias de receita
            int position = 0;

            for (int i=0; i<=(cat.length - 1); i++){ //encontrando a categoria da receita
                if (cat[i].equals(despesa.getCategoria())){
                    position = i;
                    break;
                }
            }
            catDespesa.setSelection(position);
            categoriaDespesa = cat[position];

            //Setando RadioButton
            if (despesa.getTipo().equals(rb_fixa.getText())){
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

        catDespesa.setOnItemSelectedListener(this);
        carregarDadosSpinner();

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
        catDespesa = findViewById(R.id.catDespesa);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        bt_voltar = findViewById(R.id.bt_voltar);

        catDespesa.setOnItemSelectedListener(this);
        carregarDadosSpinner();

        //MODIFICAR VALORES DO FINANCIAMENTO EM TEMPO DE EXECUÇÃO
        et_valor.addTextChangedListener(new TextWatcher() {

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //SE A STRING ATUAL FOR DIFERENTE DE ""
                if (!s.equals(current)) {

                    et_valor.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll(getString(R.string.charMoeda), "");
                    double parsed = Double.parseDouble(cleanString);
                    String formattedString = NumberFormat.getCurrencyInstance().format((parsed / 100));

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

    private void acaoBotao(){

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaCampos()) {
                    AlertDialog dialog = new AlertDialog.Builder(CadDespesa.this)
                            .setTitle("Aviso")
                            .setMessage("Há campos inválidos ou em branco!")
                            .setNeutralButton("OK", null).create();
                    dialog.show();
                }

                else if (despesa == null){
                    Despesa despesa = new Despesa();
                    despesa.setDescricao(et_descricao.getText().toString());
                    despesa.setValor((Float.parseFloat(et_valor.getText().toString().replaceAll(getString(R.string.charMoeda), ""))) / 100);
                    despesa.setCategoria(categoriaDespesa);
                    despesa.setTipo(tipo);
                    long id = despesaDAO.inserir(despesa);
                    Toast.makeText(CadDespesa.this, "Despesa "+id+" cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    CadDespesa.this.finish();

                }

                else {
                        despesa.setDescricao(et_descricao.getText().toString());
                        despesa.setValor((Float.parseFloat(et_valor.getText().toString().replaceAll(getString(R.string.charMoeda), ""))) / 100);
                        despesa.setCategoria(categoriaDespesa);
                        despesa.setTipo(tipo);
                        despesaDAO.atualizar(despesa);
                        Toast.makeText(CadDespesa.this, "Despesa atualizada!", Toast.LENGTH_SHORT).show();
                        finish();
                }
            }
        });
        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                if (tipo == null || categoriaDespesa == null){
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
                        R.array.catDespesa,
                        R.layout.support_simple_spinner_dropdown_item
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catDespesa.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (catDespesa.getItemAtPosition(position).toString().equals("Selecione uma opção")) {
            categoriaDespesa = null;
        } else if (catDespesa.getItemAtPosition(position).toString().equals("Alimentação")) {
            categoriaDespesa = "Alimentação";
        } else if (catDespesa.getItemAtPosition(position).toString().equals("Casa")) {
            categoriaDespesa = "Casa";
        } else if (catDespesa.getItemAtPosition(position).toString().equals("Saúde")) {
            categoriaDespesa = "Saúde";
        } else if (catDespesa.getItemAtPosition(position).toString().equals("Transporte")){
            categoriaDespesa = "Transporte";
        } else if (catDespesa.getItemAtPosition(position).toString().equals("Outras")) {
            categoriaDespesa = "Outras";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
