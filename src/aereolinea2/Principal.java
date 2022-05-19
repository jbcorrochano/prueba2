package aereolinea2;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author jbcor
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Connection conexi;
        try {
            conexi = DriverManager.getConnection("jdbc:mysql://localhost/aerolinea", "aerolinea", "aerolinea");

            System.out.println();
            verVuelos(conexi);
            System.out.println();
            verPasajeros(conexi);
            System.out.println();
            mostrarPasajerosVuelo(conexi,"IB-SP-4567");
            //insertarVuelo(conexi, "AVD-234-IB", "27/03/99-15:30", "MÁLAGA",
              //      "OSLO", 120, 30, 150, 50);
            System.out.println();
            //Borrar_Vuelo_BaseDatos(conexi, "AVD-234-IB");
            System.out.println();
            //Modificar_Fumador_No_Fumador(conexi);
            // select columna1, columna2, columnaX from tabla 
            // select * from tabla where columna1=tabla2.columna1
            // select count(*) from tabla Esto cuenta todos los registros de la tabla
            // select count(columna) from tabla where columna1= tabla2.columna1 Me devuelve la suma de todos los registros donde tabla.columna1 coincida con tabla2.columna1
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void verVuelos(Connection conexion) {

        try {
            String consulta1 = "select * from vuelos";
            Statement sent1 = conexion.createStatement();
            ResultSet res1 = sent1.executeQuery(consulta1);//Cursor al que le paso la consulta se puede mover por filas...
            System.out.printf("%n %30s %30s %30s %30s %30s %30s %30s %30s",
                    "COD VUELO", "HORA Vuelo", "Destino",
                    "Procedencia", "Plazas Fumador",
                    " PLazas No Fumador", "Plazas Turista",
                    "Plazas Primera");
            System.out.printf("%n %20s %30s %30s %30s %30s %30s %30s %30s",
                    "-----------", "----------------------", "-------------------",
                    "----------------", "---------------",
                    "---------------", "-----------------",
                    "--------------------");
            System.out.println();
            while (res1.next()) {

                System.out.printf("%n %20s %30s %30s %30s %30s %30s %30s %30s",
                        res1.getString(1),//Son las columnas importante saber de que tipo es....
                        res1.getString(2),
                        res1.getString(3),
                        res1.getString(4),
                        res1.getInt(5),
                        res1.getInt(6),
                        res1.getInt(7),
                        res1.getInt(8));
                System.out.println();
            }
            sent1.close();
            res1.close();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void verPasajeros(Connection conexion) {

        try {
            String consulta1 = "select * from pasajeros";
            Statement sent1 = conexion.createStatement();
            ResultSet res1 = sent1.executeQuery(consulta1);
            System.out.printf("%n %20s %30s %30s %30s",
                    "NUM", "COD_VUELO", "TIPO_PLAZA",
                    "FUMADOR");

            System.out.printf("%n %20s %30s %30s %30s ",
                    "-----------", "----------------------", "-------------------",
                    "---------------");
            System.out.println();
            while (res1.next()) {

                System.out.printf("%n %20s %30s %30s %30s",
                        res1.getInt(1),
                        res1.getString(2),
                        res1.getString(3),
                        res1.getString(4));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void mostrarPasajerosVuelo(Connection conexion, String cod_Vuelo) {

        try {

            String consulta2 = "select * from pasajeros where cod_vuelo = ?";
            String consu = "select count(*) from pasajeros where cod_vuelo=?";//Te cuentan los pasajeros por codigo de vuelo.
            PreparedStatement sen = conexion.prepareStatement(consulta2);//Preparo la sentencia
            sen.setString(1, cod_Vuelo);
            ResultSet res2 = sen.executeQuery();///cursor
            System.out.println("PASAJEROS POR VUELO");

            while (res2.next()) {
                System.out.println(res2.getInt(1));
                
            }

            sen.close();
            res2.close();

            System.out.println();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void insertarVuelo(Connection conexion, String Cov_Vuelo, String Hora_Salida,
            String Destino, String Procedencia, int Plazas_Fumador, int Plazas_No_Fumador, int Plazas_Turista, int Plazas_Primera) {

        int executeUpdate = 0;
        try {
            String comprobar = "select * from vuelos where cod_vuelo=?";

            PreparedStatement sentencia = conexion.prepareStatement(comprobar);
            sentencia.setString(1, Cov_Vuelo);
            ResultSet res1 = sentencia.executeQuery();

            int error = 0;
            String mensaje = "";
            if (!res1.next()) {
                System.err.println("No existe el VUELO");

                error = 0;
            } else {

                mensaje += "\n NO SE PUEDE INSERTAR. Existe el vuelo";//mensaje=mensaje+"existe el vuelo"
                error = 1;
            }

            if (error == 0) {
                String consulta1 = "INSERT INTO vuelos(COD_VUELO, HORA_SALIDA,DESTINO,"
                        + "PROCEDENCIA,PLAZAS_FUMADOR,PLAZAS_NO_FUMADOR,"
                        + "PLAZAS_TURISTA,PLAZAS_PRIMERA) VALUES (?,?,?,?,?,?,?,?)";

                PreparedStatement sen = conexion.prepareStatement(consulta1);//
                sen.setString(1, Cov_Vuelo);
                sen.setString(2, Hora_Salida);
                sen.setString(3, Destino);
                sen.setString(4, Procedencia);
                sen.setInt(5, Plazas_Fumador);
                sen.setInt(6, Plazas_No_Fumador);
                sen.setInt(7, Plazas_Turista);
                sen.setInt(8, Plazas_Primera);

                executeUpdate = sen.executeUpdate();

                System.out.println(executeUpdate);

                sen.close();
            } else {
                System.err.println(mensaje);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (executeUpdate == 0) {
            System.out.println("El archivo ya existe, No se ha insertado");
        } else {
            System.out.println("El archivo ha sido insertado con éxito de la base de Datos, de la tabla vuelos");
        }
    }

    private static void Borrar_Vuelo_BaseDatos(Connection conexion, String Cod_Vuelo) {
        int filas;
        filas = 0;
        String consulta1 = "DELETE FROM vuelos WHERE COD_VUELO=?";
        try {
            //Comprobar si existe
            String comprobar = "select * from vuelos where cod_vuelo=?";
            PreparedStatement sen = conexion.prepareStatement(comprobar);
            sen.setString(1, Cod_Vuelo);
            ResultSet res = sen.executeQuery();
            if (!res.next()) {
                System.err.println("EL VUELO NO EXISTE, no se borro");
            } else {
                sen = conexion.prepareStatement(consulta1);
                sen.setString(1, Cod_Vuelo);
                filas = sen.executeUpdate();
                System.out.println("Filas afectadas" + filas);//
            }

           

            sen.close();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (filas == 0) {
            System.out.println("El archivo no existe. Por lo tanto no ha sido borrado");
        } else {
            System.out.println("El archivo ha sido boorrado con éxito de la base de Datos, de la tabla vuelos");
        }
    }

    private static void Modificar_Fumador_No_Fumador(Connection conexion) {

        int filas;
        filas = 0;
        String consulta1 = "UPDATE vuelos SET PLAZAS_NO_FUMADOR=PLAZAS_NO_FUMADOR+PLAZAS_FUMADOR,"
                + " PLAZAS_FUMADOR=0";
        try {
            PreparedStatement sen = conexion.prepareStatement(consulta1);
            //sen.setInt (1,Plazas_Fumador );
            //sen.setInt(2, Plazas_No_Fumador);
            filas = sen.executeUpdate();
            System.out.println("Filas afectadas" + filas);//

            sen.close();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (filas == 0) {
            System.out.println("El archivo no se ha modificado.");
        } else {
            System.out.println("El archivo ha sido modificado con éxito de la base de Datos, de la tabla vuelos");
        }
    }

    //Metodos que no se usan
    private static void antiguoVerPasajerosVuelos(Connection conexion, String cod_Vuelo) {

        try {
            String consulta1 = "select * from vuelos";
            Statement sent1;

            sent1 = conexion.createStatement();
            ResultSet res1;

            res1 = sent1.executeQuery(consulta1);

            //Atento con el while ya que tiene que estar dentro para recorrer todas las filas.En la segunda consulta
            while (res1.next()) {
                System.out.println(res1.getString(1));

                String consulta2 = "select * from pasajeros where cod_vuelo = ?";
                PreparedStatement sen = conexion.prepareStatement(consulta2);//Preparo la sentencia
                sen.setString(1, res1.getString(1));
                ResultSet res2 = sen.executeQuery();///cursor
                System.out.println("PASAJEROS POR VUELO");

                while (res2.next()) {
                    System.out.println(res2.getInt(1));
                }

                res2.close();
                System.out.println();
            }
            sent1.close();
            res1.close();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void mostrarPasajerosVuelo2(Connection conexion, String cod_Vuelo) {

        try {

            String consulta2 = "select * from pasajeros where cod_vuelo = ?";
            String consu = "select count(*) from pasajeros where cod_vuelo=?";
            PreparedStatement sen = conexion.prepareStatement(consulta2);//Preparo la sentencia
            sen.setString(1, cod_Vuelo);
            ResultSet res2 = sen.executeQuery();///cursor
            System.out.println("PASAJEROS POR VUELO");

            while (res2.next()) {
                System.out.println(res2.getInt(1));
            }

            sen = conexion.prepareStatement(consu);
            sen.setString(1, cod_Vuelo);
            res2 = sen.executeQuery();
            res2.next();
            if (!res2.next()) {
                System.out.println("No hay registros");
            } else {
                System.out.println("Si hay registro");
            }
            System.out.println(res2.getString(1));

            res2.close();
            System.out.println();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


/*
private static void  class Actualizacion{
 private PreparedStatement sentencia;
 public void prepararInsercion(){
  String sql = "insert into personas values ( ?, ? ,? )";
  sentencia = conexion.prepareStatement(sql);
 }  
 public void insertarPersona(String nombre, dirección, telefono)
 {
   sentencia.setString(1, nombre); 
   sentencia.setString(2, direccion); 
   sentencia.setString(3, telefono); 
   sentencia.executeUpdate();
  }*/
