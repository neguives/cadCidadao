package desenvolvimento.vostore.appnaco;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import junit.runner.Version;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private static final String DATABASE_NAME = "bancodedados.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME = "Cidadao";

    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertStnt;
    private static final String INSERT = "INSERT INTO " + TABLE_NAME + "(nome, cpf, telefone, email) values (?,?,?,?)";

    public DBHelper(Context context) {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStnt = this.db.compileStatement(INSERT);

    }
    public long insert(String nome, String cpf, String telefone, String email){
        this.insertStnt.bindString(1,nome);
        this.insertStnt.bindString(2,cpf);
        this.insertStnt.bindString(3,telefone);
        this.insertStnt.bindString(4,email);

        return  this.insertStnt.executeInsert();

    }

    //Deletar todo o bando de dados
    public void deleteAll(){
        this.db.delete(TABLE_NAME,null,null);
    }

    //Consultar Dados
    public List<Cidadao> queryGetAll(){
        List<Cidadao> list = new ArrayList<Cidadao>();

        try
        {
            Cursor cursor = this.db.query(TABLE_NAME,new String[] {"nome","cpf","telefone","email"},
            null,null,null,null,null,null);
            int nregistros = cursor.getCount();
            if (nregistros != 0){
                cursor.moveToFirst();
                do {
                    Cidadao cidadao = new Cidadao (cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3) );
                    list.add(cidadao);
                }while (cursor.moveToNext());
                if (cursor != null  && ! cursor.isClosed())
                    cursor.close();
                return list;

            }else {
                return null;
            }

        }catch (Exception e){
            return null;

        }
    }
    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context){
            super (context, DATABASE_NAME,null,DATABASE_VERSION);
        }
        public void onCreate (SQLiteDatabase db){
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, cpf TEXT, telefone TEXT, email TEXT);";
            db.execSQL(sql);
        }
        public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
            db.execSQL(("DROP TABLE IF EXISTS " + TABLE_NAME));
        }
    }
}
