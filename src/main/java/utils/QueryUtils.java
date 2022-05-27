package utils;

public class QueryUtils {
    public static final String PAPELES_DUPLICADOS = "select COUNT(P.Papel) as Cantidad, P.Papel, A.Acto, A.Cod_Acto, L.Libro, L.Cod_Libro from Papel P \n" +
            "INNER JOIN Actos a ON a.Cod_Acto = P.Cod_Acto\n" +
            "INNER JOIN Libros L on L.Cod_Libro = A.Cod_Libro GROUP BY P.Papel, A.Acto, A.Cod_Acto , L.Libro,L.Cod_Libro\n" +
            "HAVING COUNT(P.Papel) > 1 order by Cantidad desc";

    public static final String ACTOS_DUPLICADOS = "select  A.Acto, A.Cod_Libro, L.Libro,  COUNT(*) as Rep  from Actos A\n" +
            "INNER JOIN Libros L ON L.Cod_Libro = A.Cod_Libro\n" +
            "GROUP by A.Acto, A.Cod_Libro, L.Libro\n" +
            "HAVING COUNT (A.Acto) > 1 order by Rep desc";

    public static final String ACTOS_DUPLICADOS_POR_NOMBRE = "select A.Cod_Acto, A.Acto, A.Cod_Libro, L.Libro, \n" +
            "(Select COUNT(Papel) from papel where Papel.Cod_Acto = A.Cod_Acto) as PapelCantidad,\n" +
            "(Select COUNT(I.Id_Inscripcion) from Inscripciones I where I.Cod_Acto = A.Cod_Acto) as InscripcionesCantidad\n" +
            "from Actos A\n" +
            "INNER JOIN Libros L ON L.Cod_Libro = A.Cod_Libro\n" +
            "where Acto = ? and A.Cod_Libro = ? order by A.Cod_Acto";
    public static final String PAPEL_DUPLICADO_POR_NOMBRE = "select P.Cod_Papel, P.Papel, P.Cod_Acto, A.Acto, \n" +
            "(Select COUNT(I.Id_Inscripcion) from Intervinientes I where I.Cod_Papel = P.Cod_Papel) as IntervinientesCantidad\n" +
            "from Papel P\n" +
            "INNER JOIN Actos A ON A.Cod_Acto = P.Cod_Acto\n" +
            "where P.Papel = ? and P.Cod_Acto = ? order by P.Cod_Papel";
    public static final String MOVER_INTERVINIENTES_PAPELES_DUPLICADOS = "update Intervinientes set Cod_Papel = ? where Cod_Papel = ?";
    public static final String MOVER_PAPELES_ACTOS_DUPLICADOS = "update Papel set Cod_Acto = ? where Cod_Acto = ?";
    public static final String MOVER_INSCRIPCIONES_ACTOS_DUPLICADOS = "update Inscripciones set Cod_Acto = ? where Cod_Acto = ?";
    public static final String ELIMINAR_ACTO_DUPLICADO = "delete from Actos where Cod_Acto = ?";
    public static final String ELIMINAR_PAPEL_DUPLICADO = "delete from Papel where Cod_papel = ?";
}
