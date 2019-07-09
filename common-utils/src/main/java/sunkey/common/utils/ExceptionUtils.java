package sunkey.common.utils;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * @author Sunkey
 * @since 2018-03-24 11:01
 **/
public class ExceptionUtils {

    private static final Class SQLE_TYPE;

    private static final Method SQLE_GET_ERRORCODE;

    static {
        Class _sqlExType = null;
        Method _getErrorCode = null;
        try {
            _sqlExType = Class.forName("java.sql.SQLException");
            _getErrorCode = _sqlExType.getMethod("getErrorCode");
        } catch (Exception e) {
        }
        SQLE_TYPE = _sqlExType;
        SQLE_GET_ERRORCODE = _getErrorCode;
    }

    public static <T> T handle(Throwable ex) {
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public static String printStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    public static <E extends Throwable> E findException(Throwable ex, Class<E> type) {
        if (ex == null) {
            return null;
        }
        while (true) {
            if (type.isAssignableFrom(ex.getClass())) {
                return (E) ex;
            }
            ex = ex.getCause();
            if (ex == null) {
                break;
            }
        }
        return null;
    }

    public static boolean isSqlDuplicateException(Throwable ex) {
        return getSqlErrorCode(ex) == 1062;
    }

    public static int getSqlErrorCode(Throwable ex) {
        if (SQLE_TYPE == null) throw new IllegalStateException("SQLException class not found!");
        Throwable sqlEx = findException(ex, SQLE_TYPE);
        if (sqlEx != null) {
            try {
                return (Integer) SQLE_GET_ERRORCODE.invoke(sqlEx);
            } catch (Exception e) {
            }
        }
        return 0;
    }

}
