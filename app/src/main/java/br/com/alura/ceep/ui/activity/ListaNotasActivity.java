package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.activity.helper.callback.NotaItemTouchHelperCallBack;
import br.com.alura.ceep.ui.activity.recycler.adapter.ListaNotasAdapter;

import br.com.alura.ceep.ui.activity.recycler.adapter.listener.OnItemClickListener;

import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.CHAVE_POSICAO_NOTA;
import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.CODIGO_REQUESICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.CODIGO_REQUESICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();

        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView textInsereNota = findViewById(R.id.lista_notas_insere_nota);
        textInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent intentParaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intentParaFormularioNota, CODIGO_REQUESICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO notaDAO = new NotaDAO();
        for (int i = 1; i <= 10; i++)
            notaDAO.insere(new Nota("Titulo " + i, "Descricao " + i));
        return notaDAO.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Teste com resultCode separado em caso de erro
        if (ehResultadoInsereNota(requestCode, data)) {
            if (resultadoOK(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            } // exemplo de um teste com resultCode separado em caso de erro
            // else if (resultCode == Activity.RESULT_CANCELED)

        }

        if (ehResultadoAlteraNota(requestCode, resultCode, data)) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoNotaRecebida = data.getIntExtra(CHAVE_POSICAO_NOTA, POSICAO_INVALIDA);

            if (ehPosicaoValida(posicaoNotaRecebida)) {
                altera(notaRecebida, posicaoNotaRecebida);
            } // else {
              //  Toast.makeText(this, "Erro inesperado na alteração da nota", Toast.LENGTH_SHORT).show();
              // }
        }

    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoNota) {
        return posicaoNota > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, int resultCode, Intent data) {
        return ehCodigoRequesicaoAlteraNota(requestCode) &&
                resultadoOK(resultCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequesicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUESICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequesicaoInsereNota(requestCode) &&
                temNota(data);
    }

    // Verifiçao para caso o Operacao de Insere seja cancelada, é nesta caso não é
    // retornado nenhum dado do formulario(data = null)
    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOK(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequesicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUESICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);

        configuraAdapter(todasNotas, listaNotas);

        // classe expecifica do recyclerview para fazer a animaçoes com os itens
        configuraItemTouchHelper(listaNotas);

//      Configura LayoutManager a partir do xml
//      configuraLayoutManager(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallBack(this.adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

// Aula 3 de 6(08:00)
// Já esta Configurado o LayoutManager a partir do xml
// Aula 3 de 9, http://blog.alura.com.br/definindo-dimensao-ideal-para-o-layout-meu-site/
//    private void configuraLayoutManager(RecyclerView listaNotas) {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        listaNotas.setLayoutManager(linearLayoutManager);
//    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        this.adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(this.adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(Nota nota, int position) {
                vaiParaFormularioNotaActivityAltera(nota, position);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int position) {
        Intent vaiParaFormularioComANota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        vaiParaFormularioComANota.putExtra(CHAVE_NOTA, nota);
        vaiParaFormularioComANota.putExtra(CHAVE_POSICAO_NOTA, position);
        startActivityForResult(vaiParaFormularioComANota, CODIGO_REQUESICAO_ALTERA_NOTA);
    }
}
