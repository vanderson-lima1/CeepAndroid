package br.com.alura.ceep.ui.activity.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.activity.recycler.adapter.listener.OnItemClickListener;

//Utilizando o generics no Adapter
//Para utilizar métodos do NotaViewHolder é necessário realizar um cast no parâmetro recebido pelo
// onBindViewHolder(). Para evitar essa abordagem, modifique a extensão do RecyclerView.Adapter para
// que receba um generics do tipo ListaNotasAdapter.NotaViewHolder.
//
//Repare que com essa modificação é apresentado um erro de compilação, para resolvê-lo,
// modifique a sobrescrita do onCreateViewHolder() e onBindViewHolder() para que lidem diretamente
// com o ListaNotasAdapter.NotaViewHolder.

//sem generics
// public class ListaNotasAdapter extends RecyclerView.Adapter {
public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    // Os atributos são criados apenas no Construtor e não são mais MODIFICADOS,
    // neste caso usar 'final'
    private final List<Nota> notas;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    //Somente para verificar o Log e intender o funcionamento, pode ser retirado do codigo
    private static int quantidadeViewCriada = 0;
    private static int quantidadeBindView = 0;
    private int quantidadeViewHolderCriada = 0;

    public ListaNotasAdapter(Context context,List<Nota> notas){
        this.context = context;
        this.notas = notas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //sem generics
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        quantidadeViewCriada++;
        quantidadeViewHolderCriada++;
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        //Somente para verificar o Log e intender o funcionamento, pode ser retirado do codigo
        Log.i("recyclerView adapter", "View Holder criado contador static   : " + quantidadeViewCriada );
        Log.i("recyclerView adapter", "View Holder criado contador instancia: " + quantidadeViewHolderCriada);
        return new NotaViewHolder(viewCriada);
    }

    //sem generics
    //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    @Override
    public void onBindViewHolder(ListaNotasAdapter.NotaViewHolder holder, int position) {
        quantidadeBindView++;
        Nota nota = notas.get(position);
        //sem generics
        //NotaViewHolder notaViewHolder = (NotaViewHolder) holder;
        holder.vincula(nota);

        //Somente para verificar o Log e intender o funcionamento, pode ser retirado do codigo
        Log.i("recyclerView adapter", "bind posicao: "
                + position +  "View Holder: " + quantidadeBindView);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void altera(int posicao, Nota nota) {
        this.notas.set(posicao,nota);
        notifyDataSetChanged();
        //notifyItemChanged(posicao); Em caso item fosse alterado na propria lista, adiciona efeito
    }

    public void remove(int posicao) {
        notas.remove(posicao);
        //notifyDataSetChanged();
        notifyItemRemoved(posicao); //com efeito
    }

    public void troca(int posicaoIncial, int posicaoFinal) {
        Collections.swap(notas,posicaoIncial,posicaoFinal);
        //notifyDataSetChanged();
        notifyItemMoved(posicaoIncial,posicaoFinal); // com efeito
    }

    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView campoTitulo;
        private final TextView campoDescricao;
        private Nota nota;

        public NotaViewHolder(View itemView) {
            super(itemView);
            campoTitulo = itemView.findViewById(R.id.item_nota_titulo);
            campoDescricao =  itemView.findViewById(R.id.item_nota_descricao);

            //Além de implementar o listener na View do ViewHolder, uma implementação de listener ideal
            // permite que outros membros que façam uso do Adapter consigam definir as ações desejadas
            // quando o evento ocorrer. Para isso, CRIE!!! A Interface OnItemClickListener.
            //Perceba que agora qualquer entidade que fizer uso do adapter será capaz de atribuir
            // ações a partir do listener de clique.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(nota, getAdapterPosition());
                }
            });
        }

        public void vincula(Nota nota) {
            this.nota = nota;
            preencheCampo(nota);
        }

        private void preencheCampo(Nota nota) {
            campoTitulo.setText(nota.getTitulo());
            campoDescricao.setText(nota.getDescricao());
        }
    }

    public void adiciona(Nota nota) {
        this.notas.add(nota);
        notifyDataSetChanged();
    }

}
