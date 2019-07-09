package sunkey.common.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.InputStream;

/**
 * @author Sunkey
 * @since 2018-04-19 下午3:45
 **/
public class ResourceFinder {

    public static final String DEFAULT_CLASSPATH_PREFIX = "classpath:";
    public static final String[] CLASSPATH_PREFIXES = {DEFAULT_CLASSPATH_PREFIX, "cp:"};

    private static PathMatchingResourcePatternResolver resolver =
            new PathMatchingResourcePatternResolver();

    public static PathMatchingResourcePatternResolver getResolver() {
        return resolver;
    }

    public static File getFile(String locationPattern) {
        Resource[] resources = getResources(locationPattern);
        if (resources != null && resources.length > 0) {
            try {
                return resources[0].getFile();
            } catch (Exception ex) {
                throw new ResourceNotFoundException(locationPattern, ex);
            }
        }
        throw new ResourceNotFoundException(locationPattern);
    }

    public static InputStream getResourceAsStream(String locationPattern) {
        Resource[] resources = getResources(locationPattern);
        if (resources != null && resources.length > 0) {
            try {
                return resources[0].getInputStream();
            } catch (Exception ex) {
                throw new ResourceNotFoundException(locationPattern, ex);
            }
        }
        throw new ResourceNotFoundException(locationPattern);
    }

    public static Resource[] getResources(String locationPattern) {
        try {
            return resolver.getResources(locationPattern);
        } catch (Exception ex) {
            throw new ResourceNotFoundException(locationPattern, ex);
        }
    }

    public static InputStream getClasspathResource(String loc) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(loc);
        if (in == null) {
            throw new ResourceNotFoundException(DEFAULT_CLASSPATH_PREFIX + loc);
        }
        return in;
    }

    public static String getRealClasspath(String loc) {
        String locLower = loc.toLowerCase();
        for (String prefix : CLASSPATH_PREFIXES) {
            if (locLower.startsWith(prefix)) {
                return loc.substring(prefix.length());
            }
        }
        return null;
    }

    public static boolean isClasspath(String loc) {
        String locLower = loc.toLowerCase();
        for (String prefix : CLASSPATH_PREFIXES) {
            if (locLower.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException() {
        }

        public ResourceNotFoundException(String location) {
            this(location, null);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super("resource : " + message + " not found, message : " +
                    cause == null ? "" : cause.getMessage(), cause);
        }

    }

}
