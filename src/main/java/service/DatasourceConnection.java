package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import utils.LabelUtils;
import utils.QueryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatasourceConnection extends LabelUtils {
    private static HikariConfig config = new HikariConfig();;
    private static HikariDataSource ds;
    private static Connection con = null;
    private static PreparedStatement pst= null;
    private static ResultSet rs = null;

    public static String jdbcUrl = "";
    public static String user = "";
    public static String pwd = "";

    private static synchronized void buildConnection() throws SQLException {
        //config = new HikariConfig(DB_PROPERTIES_FILEPATH);

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(pwd);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource(config);
        con = ds.getConnection();
    }
    public static Connection createConnection() throws SQLException{
        if (con == null)
            buildConnection();
        return con;
    }
    public static ResultSet getDuplicatesActos() throws SQLException {
        pst = con.prepareStatement(QueryUtils.ACTOS_DUPLICADOS);
        rs = pst.executeQuery();
        return rs;
    }
    public static ResultSet getActosByName(String actoNombre, int codLibro) throws SQLException {
        pst = con.prepareStatement(QueryUtils.ACTOS_DUPLICADOS_POR_NOMBRE);
        pst.setString(1, actoNombre);
        pst.setInt(2, codLibro);
        rs = pst.executeQuery();
        return rs;
    }
    public static int moverActosDuplicados(int codActoOriginal, int codActoDuplicado, String query) throws SQLException {
        pst = con.prepareStatement(query);
        pst.setInt(1, codActoOriginal);
        pst.setInt(2, codActoDuplicado);
        return pst.executeUpdate();
    }
    public static int eliminarActosDuplicados(int codActoDuplicado) throws SQLException {
        pst = con.prepareStatement(QueryUtils.ELIMINAR_ACTO_DUPLICADO);
        pst.setInt(1, codActoDuplicado);
        return pst.executeUpdate();
    }

    /********************** PAPEL **************\
     *
     */
    public static ResultSet getPapelesDuplicados() throws SQLException {
        pst = con.prepareStatement(QueryUtils.PAPELES_DUPLICADOS);
        rs = pst.executeQuery();
        return rs;
    }
    public static ResultSet getPapelDuplicadoPorNombre(String papelNombre, int codActo) throws SQLException {
        pst = con.prepareStatement(QueryUtils.PAPEL_DUPLICADO_POR_NOMBRE);
        pst.setString(1, papelNombre);;
        pst.setInt(2, codActo);
        rs = pst.executeQuery();
        return rs;
    }
    public static int moverPapelDuplicados(int codPapelOriginal, int codPapelDuplicado) throws SQLException {
        pst = con.prepareStatement(QueryUtils.MOVER_INTERVINIENTES_PAPELES_DUPLICADOS);
        pst.setInt(1, codPapelOriginal);
        pst.setInt(2, codPapelDuplicado);
        return pst.executeUpdate();
    }
    public static int eliminarPapelDuplicado(int codPapelDuplicado) throws SQLException {
        pst = con.prepareStatement(QueryUtils.ELIMINAR_PAPEL_DUPLICADO);
        pst.setInt(1, codPapelDuplicado);
        return pst.executeUpdate();
    }
    public static void shutdownConnection(){
        try {

            if (rs != null) {
                rs.close();
            }

            if (pst != null) {
                pst.close();
            }

            if (con != null) {
                con.close();
            }
            if (ds!=null)
                ds.close();
            System.out.println("Todo cerrado...");

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatasourceConnection.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
