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
import android.widget.Toast;

public class UsuarioActivity extends AppCompatActivity {

    EditText jetidentificacion,jetnombre,jetprofesion,jetempresa,jetsalario,jetextras,jetgastos;
    CheckBox jcbactivo;

    //Instanciar la clase que heredo de la clase SqLiteOpenHelper
    ClsOpenHelper admin=new ClsOpenHelper(this,"Banco.bd",null,1);

    String identificacion,nombre,profesion,empresa,salario,extras,gastos;
    long resp;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        //Ocultar barra de titulo y asociar objetos Java y Xml
        getSupportActionBar().hide();
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetprofesion=findViewById(R.id.etprofesion);
        jetempresa=findViewById(R.id.etempresa);
        jetsalario=findViewById(R.id.etsalario);
        jetextras=findViewById(R.id.etextras);
        jetgastos=findViewById(R.id.etgastos);
        jcbactivo=findViewById(R.id.cbactivo);
        sw=0;
    }

    public void Guardar(View view){
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        profesion=jetprofesion.getText().toString();
        empresa=jetempresa.getText().toString();
        salario=jetsalario.getText().toString();
        extras=jetextras.getText().toString();
        gastos=jetgastos.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || profesion.isEmpty() || empresa.isEmpty() || salario.isEmpty() || extras.isEmpty() || gastos.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else {
            SQLiteDatabase fila=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("identificacion",identificacion);
            registro.put("nombre",nombre);
            registro.put("profesion",profesion);
            registro.put("empresa",empresa);
            registro.put("salario",Integer.parseInt(salario));
            registro.put("ingreso_extra",Integer.parseInt(extras));
            registro.put("gastos",gastos);
            if (sw == 0)
                resp=fila.insert("TblCliente",null,registro);
            else {
                resp=fila.update("TblCliente",registro,"identificacion='" + identificacion + "'",null);
                sw=0;
            }
            if (resp == 0){
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            fila.close();
        }
    }

    public void Buscar(View view){
        identificacion=jetidentificacion.getText().toString();
        if (identificacion.isEmpty()){
            Toast.makeText(this, "Identificacion es requerida para la consulta", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            SQLiteDatabase fila=admin.getReadableDatabase();
            Cursor dato=fila.rawQuery("select * from TblCliente where identificacion='" +identificacion+ "'",null);
            if (dato.moveToNext()){
                sw=1;
                jetnombre.setText(dato.getString(1));
                jetprofesion.setText(dato.getString(2));
                jetempresa.setText(dato.getString(3));
                jetsalario.setText(dato.getString(4));
                jetextras.setText(dato.getString(5));
                jetgastos.setText(dato.getString(6));
                if (dato.getString(7).equals("si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }
            else {
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Anular(View view){
        if (sw == 0){
            Toast.makeText(this, "Debe primero consultar para anular", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else{
            identificacion=jetidentificacion.getText().toString();
            if (identificacion.isEmpty()){
                Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
                jetidentificacion.requestFocus();
            }
            else {
                sw=0;
                SQLiteDatabase fila=admin.getWritableDatabase();
                ContentValues registro=new ContentValues();
                registro.put("activo","no");
                resp=fila.update("TblCliente",registro,"identificacion='" + identificacion + "'",null);
                if (resp == 0){
                    Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                    Limpiar_campos();
                }
                fila.close();
            }
        }
    }

    public void Regresar(View view){
        Intent intmenu=new Intent(this,MainActivity.class);
        startActivity(intmenu);
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }



    private void Limpiar_campos(){
        jetidentificacion.setText("");
        jetgastos.setText("");
        jetextras.setText("");
        jetsalario.setText("");
        jetempresa.setText("");
        jetprofesion.setText("");
        jetnombre.setText("");
        jcbactivo.setChecked(false);
        jetidentificacion.requestFocus();
        sw=0;
    }

}