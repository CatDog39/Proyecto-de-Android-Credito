package com.example.credito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreditoActivity extends AppCompatActivity {

    EditText jetcodigo_credito,jetidentificacion;
    TextView jtvnombre,jtvprofesion,jtvsalario,jtvingresos_extras,jtvgastos,jtvvalor_prestamo;

    ClsOpenHelper admin=new ClsOpenHelper(this,"Banco.bd",null,1);

    String identificacion,nombre,profesion,salario,ingresos_extras,gastos,valor_prestamo,numero_credito;
    long resp;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);
        getSupportActionBar().hide();
        jetcodigo_credito=findViewById(R.id.etcodigo_credito);
        jetidentificacion=findViewById(R.id.etidentificacion);
        jtvnombre=findViewById(R.id.tvnombre);
        jtvprofesion=findViewById(R.id.tvprofesion);
        jtvsalario=findViewById(R.id.tvsalario);
        jtvingresos_extras=findViewById(R.id.tvingresos_extras);
        jtvgastos=findViewById(R.id.tvgastos);
        jtvvalor_prestamo=findViewById(R.id.tvvalor_prestamo);
    }

    public void Buscar(View view){
        identificacion=jetidentificacion.getText().toString();
        if (identificacion.isEmpty()){
            Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            SQLiteDatabase fila=admin.getReadableDatabase();
            Cursor dato=fila.rawQuery("select * from TblCliente where identificacion='" + identificacion + "'",null);
            if (dato.moveToNext()){
                jtvnombre.setText(dato.getString(1));
                jtvprofesion.setText(dato.getString(2));
                jtvsalario.setText(dato.getString(4));
                jtvingresos_extras.setText(dato.getString(5));
                jtvgastos.setText(dato.getString(6));
            }
            else {
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Ejecutar(View view){
        identificacion=jetidentificacion.getText().toString();
        salario=jtvsalario.getText().toString();
        ingresos_extras=jtvingresos_extras.getText().toString();
        gastos=jtvgastos.getText().toString();

        if (identificacion.isEmpty()){
            Toast.makeText(this, "La identificacion es requerida para la consulta", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            int ejsalario,ejingresos_extras,ejgastos;
            float ejvalor_prestamo;
            ejsalario=Integer.parseInt(salario);
            ejingresos_extras=Integer.parseInt(ingresos_extras);
            ejgastos=Integer.parseInt(gastos);
            ejvalor_prestamo=((ejsalario + ejingresos_extras - ejgastos) * 10);
            jtvvalor_prestamo.setText(String.valueOf(ejvalor_prestamo));
        }

        //poner ramdon
       // numero_credito=jetcodigo_credito.getText().toString();
       // int codigo;
       // codigo=Integer.parseInt(numero_credito);


    }

    public void Guardar(View view){
        numero_credito=jetcodigo_credito.getText().toString();
        identificacion=jetidentificacion.getText().toString();
        valor_prestamo=jtvvalor_prestamo.getText().toString();

        if (numero_credito.isEmpty() || identificacion.isEmpty() || valor_prestamo.isEmpty()){
            Toast.makeText(this, "Todos los datos son necesarios", Toast.LENGTH_SHORT).show();
        }
        else{
            SQLiteDatabase fila=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("cod_credito",numero_credito);
            registro.put("identificacion",identificacion);
            registro.put("valor_prestamo",valor_prestamo);
            if (sw == 0)
                resp=fila.insert("TblCredito",null,registro);
        }


    }


}