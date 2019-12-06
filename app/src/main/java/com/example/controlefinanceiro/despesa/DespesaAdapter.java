package com.example.controlefinanceiro.despesa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.controlefinanceiro.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class DespesaAdapter extends ArrayAdapter<Despesa> {

    private final Context context;
    private final ArrayList<Despesa> listaDespesas;

    public DespesaAdapter(Context context, ArrayList<Despesa> despesas) {
        super(context, R.layout.linhadespesa, despesas);
        this.context = context;
        this.listaDespesas = despesas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View linhadespesas = inflater.inflate(R.layout.linhadespesa, parent, false);

        TextView descricao = linhadespesas.findViewById(R.id.tv_descricao);

        DecimalFormat df = new DecimalFormat("###,##0.00"); //Própria função do Java
        TextView valor = linhadespesas.findViewById(R.id.tv_valor);
        //TextView categoria = linhadespesas.findViewById(R.id.tv_categoria);
        //TextView tipo = linhadespesas.findViewById(R.id.tv_tipo);

        descricao.setText(listaDespesas.get(position).getDescricao());
        valor.setText(df.format(listaDespesas.get(position).getValor()));
        //categoria.setText(listaDespesas.get(position).getCategoria());
        //tipo.setText(listaDespesas.get(position).getTipo());

        return linhadespesas;


    }
}
