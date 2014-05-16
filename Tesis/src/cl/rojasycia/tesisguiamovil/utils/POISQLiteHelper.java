package cl.rojasycia.tesisguiamovil.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class POISQLiteHelper extends SQLiteOpenHelper {
 
    //Sentencia SQL para crear la tabla 
    String sqlCreate = "CREATE TABLE PuntoDeInteres (nombrePOI TEXT, tipoPOI TEXT, latitudPOI DOUBLE PRECISION, longitudPOI DOUBLE PRECISION)";
 
    public POISQLiteHelper(Context contexto, String nombre,
                               CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, 
                          int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente 
        //      la opción de eliminar la tabla anterior y crearla de nuevo 
        //      vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la 
        //      tabla antigua a la nueva, por lo que este método debería 
        //      ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS PuntoDeInteres");
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}

