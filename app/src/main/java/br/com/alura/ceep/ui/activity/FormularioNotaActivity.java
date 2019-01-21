package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.CHAVE_POSICAO_NOTA;
import static br.com.alura.ceep.ui.activity.NotaAtivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity  {

    public static final String TITULO_APPBAR_INSERE = "Insere Nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera Nota";
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView campoTitulo;
    private TextView campoDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR_INSERE);

        inicializaCampos();

        Intent dadosRecebidos = getIntent();

        if(dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle(TITULO_APPBAR_ALTERA);

            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO_NOTA, POSICAO_INVALIDA);
            preencheCampos(notaRecebida);
        }
    }

    private void preencheCampos(Nota notaRecebida) {
        inicializaCampos();
        campoTitulo.setText(notaRecebida.getTitulo());
        campoDescricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        campoTitulo = findViewById(R.id.formulario_nota_titulo);
        campoDescricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (ehMenuSalvaNota(item)) {
            Nota novaNota = criaNota();
            retornaNota(novaNota);
            finish(); //volta para lista
        }

        return super.onOptionsItemSelected(item);

    }

    private void retornaNota(Nota novaNota) {
        Intent intentDadosNota = new Intent();
        intentDadosNota.putExtra(CHAVE_NOTA, novaNota);
        intentDadosNota.putExtra(CHAVE_POSICAO_NOTA, posicaoRecebida);
        setResult(Activity.RESULT_OK,intentDadosNota);
    }

    @NonNull
    private Nota criaNota() {
        return new Nota(campoTitulo.getText().toString(),campoDescricao.getText().toString());
    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}
