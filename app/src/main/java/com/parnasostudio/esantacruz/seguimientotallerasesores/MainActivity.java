package com.parnasostudio.esantacruz.seguimientotallerasesores;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText Myplaca;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Myplaca   = (EditText) findViewById(R.id.input_placa);

    }

    /*Se obtiene la direccion mac del dispositivo movil*/
    public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
            /*macAddress = "00:00:00:00:00";*/
        }

        return macAddress;
    }


    /*CADA VEZ que se ingresa la placa este se registrara en la base de datos*/
    public void registrarPlaca(View v){

        if (Myplaca.length()>0){



            Toast toast = Toast.makeText(this,getMacAddress( getBaseContext()),  Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

            InserUpdateDatos();

            Myplaca.setText("");

        }else{
            Toast toast = Toast.makeText(this,"Debe ingresar una Placa válida",  Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

        }



    }


    public boolean ObtenerDatos() {
        String datosConsultado = "";
        try {
            //Se obtiene la conexión
            Connection connect = ConexionSQL.ConnectionHelper();
            //Se genera la consulta
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery("select top 10 IDUSUARIO from usuario");
            while (rs.next()) {
                //Se extraen los datos
                datosConsultado = rs.getString("IDUSUARIO");
            }
            //Se cierra la conexión
            connect.close();
            //Mostramos los datos obtenidos
            Toast.makeText(getApplicationContext(),
                    datosConsultado, Toast.LENGTH_SHORT).show();
            return  true;
        } catch (SQLException e) {
            //Mostramos el error en caso de no conectarse
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean InserUpdateDatos(){
        String consultaSQL;
        consultaSQL = "EXEC stp_InsertUpdate_Control_Taller '"+  Myplaca.getText() + "', N'"+getMacAddress( getBaseContext())+"' , '000'";


        try {
            //Se obtiene la conexión
            Connection connect = ConexionSQL.ConnectionHelper();
            //Se genera la consulta
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(consultaSQL);

            //Se cierra la conexión
            connect.close();
            //Mostramos los datos obtenidos
            Toast toast = Toast.makeText(this,"Se registro correctamente la placa ingresada.",  Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return  true;
        } catch (SQLException e) {
            //Mostramos el error en caso de no conectarse
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
