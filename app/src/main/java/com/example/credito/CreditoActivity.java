package com.example.credito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreditoActivity extends AppCompatActivity {

    EditText jetcodigo_credito,jetidentificacion;
    TextView jtvnombre,jtvprofesion,jtvsalario,jtvingresos_extras,jtvgastos,jtvvalor_prestamo;
    CheckBox jcbactivo;

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

    public void Buscar_credito(View view){
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

    public void Ejecutar_credito(View view){
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

    public void Guardar_credito(View view){
        numero_credito=jetcodigo_credito.getText().toString();
        identificacion=jetidentificacion.getText().toString();
        valor_prestamo=jtvvalor_prestamo.getText().toString();

        if (numero_credito.isEmpty() || identificacion.isEmpty() || valor_prestamo.isEmpty()){
            Toast.makeText(this, "Todos los datos son necesarios", Toast.LENGTH_SHORT).show();
            jetcodigo_credito.requestFocus();
        }
        else{
            SQLiteDatabase fila=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("cod_credito",numero_credito);
            registro.put("identificacion",identificacion);
            registro.put("valor_prestamo",valor_prestamo);
            if (sw == 0)
                resp=fila.insert("TblCredito",null,registro);
            else{
                resp=fila.update("TblCredito",registro,"cod_credito='" + numero_credito + "'",null);
                sw=0;
            }
            if (resp == 0){
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Resgistro guardado con exito", Toast.LENGTH_SHORT).show();
                Limpiar_campos_credito();
            }
            fila.close();
        }

    }

    public void Consultar_credito(View view){
        numero_credito=jetcodigo_credito.getText().toString();
        if (numero_credito.isEmpty()){
            Toast.makeText(this, "El numero de credito es requerido", Toast.LENGTH_SHORT).show();
            jetcodigo_credito.requestFocus();
        }
        else{
            SQLiteDatabase fila=admin.getReadableDatabase();
            Cursor dato=fila.rawQuery("select * from TblCredito where cod_credito='"+ numero_credito + "'",null);
            if (dato.moveToNext()){
                jetcodigo_credito.setText(dato.getString(0));
                jetidentificacion.setText(dato.getString(1));
                jtvvalor_prestamo.setText(dato.getString(2));
            }
            else{
                Toast.makeText(this, "Credito no registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Anular_credito(View view){
        if (sw == 0){
            Toast.makeText(this, "Primero debe consultar para anular", Toast.LENGTH_SHORT).show();
            jetcodigo_credito.requestFocus();
        }
        else{
            sw=0;
            SQLiteDatabase fila=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=fila.update("TblCredito",registro,"cod_credito='" + numero_credito +"'",null);
            if (resp == 0){
                Toast.makeText(this, "Error al anular registro", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Registro anulado correctamente", Toast.LENGTH_SHORT).show();
                Limpiar_campos_credito();
            }
            fila.close();
        }

    }

    public void Regresar_credito(View view){
        Intent intmenu=new Intent(this,MainActivity.class);
        startActivity(intmenu);
    }

    public void Cancelar_credito(View view){Limpiar_campos_credito();}

    private void Limpiar_campos_credito(){
        jetidentificacion.setText("");
        jetcodigo_credito.setText("");
        jtvgastos.setText("");
        jtvvalor_prestamo.setText("");
        jtvprofesion.setText("");
        jtvsalario.setText("");
        jtvnombre.setText("");
        jtvingresos_extras.setText("");
        jcbactivo.setChecked(false);
        sw=0;
    }

}