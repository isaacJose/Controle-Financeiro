package com.example.controlefinanceiro.receita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.controlefinanceiro.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReceitaAdapter extends ArrayAdapter<Receita> {

    private final Context context;
    private final ArrayList<Receita> listaReceitas;

    public ReceitaAdapter(Context context, ArrayList<Receita> receitas) {
        super(context, R.layout.linhareceita, receitas);
        this.context = context;
        this.listaReceitas = receitas;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View linhareceitas = inflater.inflate(R.layout.linhareceita, parent, false);

        TextView descricao = linhareceitas.findViewById(R.id.tv_descricao);

        DecimalFormat df = new DecimalFormat("###,##0.00"); //Própria função do Java
        TextView valor = linhareceitas.findViewById(R.id.tv_valor);
        TextView categoria = linhareceitas.findViewById(R.id.tv_categoria);
        TextView tipo = linhareceitas.findViewById(R.id.tv_tipo);

        descricao.setText(listaReceitas.get(position).getDescricao());
        valor.setText(df.format(listaReceitas.get(position).getValor()));
        categoria.setText(listaReceitas.get(position).getCategoria());
        tipo.setText(listaReceitas.get(position).getTipo());

        return linhareceitas;


    }
}
