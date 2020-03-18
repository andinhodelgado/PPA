package br.com.usinasantafe.ppa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DigPesagemActivity extends ActivityGeneric {

    private PPAContext ppaContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dig_pesagem);

        ppaContext = (PPAContext) getApplication();

        Button buttonOkDigPesagem = (Button) findViewById(R.id.buttonOkPadrao);
        Button buttonCancDigPesagem = (Button) findViewById(R.id.buttonCancPadrao);

        buttonOkDigPesagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextPadrao.getText().toString().equals("")) {

                    String pesagemString = editTextPadrao.getText().toString();
                    Double pesagem = Double.valueOf(pesagemString.replace(",", "."));
                    ppaContext.setPesagem(pesagem);

                    Intent it = new Intent(DigPesagemActivity.this, ComentFalhaActivity.class);
                    startActivity(it);
                    finish();

                }

            }

        });

        buttonCancDigPesagem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (editTextPadrao.getText().toString().length() > 0) {
                    editTextPadrao.setText(editTextPadrao.getText().toString().substring(0, editTextPadrao.getText().toString().length() - 1));
                }
            }
        });

    }

    public void onBackPressed() {
            Intent it = new Intent(DigPesagemActivity.this, MenuPesagemActivity.class);
            startActivity(it);
            finish();
    }

}
