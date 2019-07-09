package sunkey.common.utils;


public class OsUtils {

    public static boolean isWindows() {
        return isOsLike("windows");
    }

    public static boolean isLinux() {
        return isOsLike("linux");
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    private static boolean isOsLike(String name) {
        return getOsName().toLowerCase().contains(name.toLowerCase());
    }

}
