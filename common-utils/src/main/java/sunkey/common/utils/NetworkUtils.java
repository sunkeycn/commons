package sunkey.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PatternMatchUtils;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年12月13日 下午2:59:29
 */
public class NetworkUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

    public static MacAddress getFirstMacAddress() {
        for (NetworkInterface ni : getNetInterfaces(true, true, false)) {
            try {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    return new MacAddress(mac);
                }
            } catch (SocketException e) {
                // ignore
            }
        }
        return null;
    }

    public static MacAddress getPreferredMacAddress() {
        MacAddress macAddress = getMacAddress(getPreferredAddress());
        return macAddress != null ? macAddress : getFirstMacAddress();
    }

    public static InetAddress getPreferredAddress() {
        List<InetAddress> addrs = getPublicAddresses();
        if (addrs != null && !addrs.isEmpty()) {
            return addrs.get(0);
        }
        addrs = getPrivateAddresses();
        if (addrs != null && !addrs.isEmpty()) {
            return addrs.get(0);
        }
        return getLocalhost();
    }

    public static String getHostName(SocketAddress addr) {
        if (addr != null) {
            if (addr instanceof InetSocketAddress) {
                return ((InetSocketAddress) addr).getHostName();
            } else {
                return addr.toString();
            }
        } else {
            return "null";
        }
    }

    public static String getHostAddress(SocketAddress addr) {
        if (addr != null) {
            if (addr instanceof InetSocketAddress) {
                return ((InetSocketAddress) addr).getAddress().getHostAddress();
            } else {
                return addr.toString();
            }
        } else {
            return "null";
        }
    }

    /**
     * 不包含localhost
     *
     * @return
     */
    public static final List<NetworkInterface> getUsefulInterfaces() {
        return getNetInterfaces(false, false, true);
    }

    /**
     * @param lo contains localhost
     * @param vr contains virtual
     * @param up only up
     * @return
     */
    public static final List<NetworkInterface> getNetInterfaces(boolean lo, boolean vr, boolean up) {
        try {
            List<NetworkInterface> list = new ArrayList<>();
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                 e.hasMoreElements(); ) {
                NetworkInterface netInterface = e.nextElement();
                if (!lo && netInterface.isLoopback())
                    continue;
                if (!vr && netInterface.isVirtual())
                    continue;
                if (up && !netInterface.isUp())
                    continue;
                list.add(netInterface);
            }
            return list;
        } catch (SocketException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static boolean isPrivate(InetAddress inetAddress) {
        return inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()
                && (inetAddress instanceof Inet4Address);
    }

    public static boolean isPublic(InetAddress inetAddress) {
        return !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
                && !inetAddress.isSiteLocalAddress() && (inetAddress instanceof Inet4Address);
    }

    /**
     * @return 所有外网地址
     */
    public static final List<InetAddress> getPublicAddresses() {
        List<InetAddress> inetAddresses = new LinkedList<InetAddress>();
        for (NetworkInterface networkInterface : getUsefulInterfaces()) {
            for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                InetAddress inetAddress = address.getAddress();
                if (isPublic(inetAddress)) {
                    inetAddresses.add(inetAddress);
                }
            }
        }
        return inetAddresses;
    }

    /**
     * @return 所有内网地址
     */
    public static final List<InetAddress> getPrivateAddresses() {
        List<InetAddress> inetAddresses = new LinkedList<InetAddress>();
        for (NetworkInterface networkInterface : getUsefulInterfaces()) {
            for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                InetAddress inetAddress = address.getAddress();
                if (isPrivate(inetAddress)) {
                    inetAddresses.add(inetAddress);
                }
            }
        }
        return inetAddresses;
    }

    /**
     * 第一个内网地址
     */
    public static InetAddress getPreferredPrivateAddress() {
        List<InetAddress> privateAddresses = getPrivateAddresses();
        if (privateAddresses.isEmpty()) {
            throw new IllegalStateException("empty of private addresses!");
        }
        return privateAddresses.get(0);
    }

    /**
     * 第一个外网地址
     */
    public static InetAddress getPreferredPublicAddress() {
        List<InetAddress> publicAddresses = getPublicAddresses();
        if (publicAddresses.isEmpty()) {
            throw new IllegalStateException("empty of public addresses!");
        }
        return publicAddresses.get(0);
    }

    public static String getPreferredPublicHost() {
        return getPreferredPublicAddress().getHostAddress();
    }

    public static String getPreferredPrivateHost() {
        return getPreferredPrivateAddress().getHostAddress();
    }

    /**
     * filter IPv4 Address
     *
     * @param list
     * @param exps
     * @return
     */
    public static List<InetAddress> filterHosts(List<InetAddress> list, String[] exps) {
        List<InetAddress> result = new ArrayList<>();
        for (InetAddress inad : list) {
            if (inad instanceof Inet4Address && match(exps, inad.getHostAddress())) {
                result.add(inad);
            }
        }
        return result;
    }

    /**
     * 如果一个正向表达式匹配成功则成功
     * 如果一个反向表达式匹配失败则失败
     *
     * @param exps
     * @param host
     * @return
     */
    public static boolean match(String[] exps, String host) {
        boolean allFanxiang = true;
        for (String exp : exps) {
            boolean fanxiang = false;
            String exp_ = exp;
            if (exp.startsWith("!")) { // 反向匹配
                fanxiang = true;
                exp_ = exp_.substring(1);
            }
            boolean match = PatternMatchUtils.simpleMatch(exp_, host);
            if (fanxiang && match) return false;
            if (!fanxiang && match) return true;
            allFanxiang &= fanxiang;
        }
        return allFanxiang;
    }

    public static List<InetAddress> getAllInetAddresses() {
        List<InetAddress> result = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces(); nifs
                    .hasMoreElements(); ) {
                NetworkInterface nif = nifs.nextElement();
                for (Enumeration<InetAddress> inads = nif.getInetAddresses(); inads.hasMoreElements(); ) {
                    InetAddress inad = inads.nextElement();
                    result.add(inad);
                }
            }
        } catch (Exception e) {
            logger.error("查询Localhost失败:{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * simple implement.
     *
     * @return
     */
    public static String getLocalhostAddress() {
        return getLocalhost().getHostAddress();
    }

    public static InetAddress getLocalhost() {
        try {
            return Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            return ExceptionUtils.handle(e);
        }
    }

    public static MacAddress getLocalMacAddress() {
        return getMacAddress(getLocalhost());
    }

    public static MacAddress getMacAddress(InetAddress inad) {
        try {
            return new MacAddress(NetworkInterface.getByInetAddress(inad).getHardwareAddress());
        } catch (Exception e) {
            return null;
        }
    }

    public static String toHex(byte data) {
        return Integer.toHexString(data & 0xFF);
    }

    public static class MacAddress {
        private final byte[] data;

        MacAddress(byte[] data) {
            this.data = data;
        }

        public byte getData(int index) {
            return data[index];
        }

        public byte[] getData() {
            return data;
        }

        public String toMacString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                if (i != 0) sb.append('-');
                String cl = toHex(data[i]);
                if (cl.length() == 1) sb.append('0');
                sb.append(cl);
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return "MAC Address[" + toMacString() + "]";
        }

    }

    public static NetAddress parse(SocketAddress addr) {
        if (addr != null && addr instanceof InetSocketAddress) {
            return new NetAddress((InetSocketAddress) addr);
        }
        return NetAddress.EMPTY;
    }

    public static NetAddress parse(String addr) {
        if (addr == null || addr.isEmpty()) {
            return NetAddress.EMPTY;
        }
        int index = addr.indexOf(':');
        String host = null;
        int port = 0;
        if (index > -1) {
            host = addr.substring(0, index);
            port = Integer.valueOf(addr.substring(index + 1));
        } else {
            host = addr;
        }
        return new NetAddress(host, port);
    }

    public static class NetAddress {

        public static final NetAddress EMPTY = new NetAddress();

        private final String host;
        private final int port;

        private NetAddress() {
            this.host = null;
            this.port = 0;
        }

        public NetAddress(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public NetAddress(InetSocketAddress soc) {
            if (soc != null) {
                this.host = soc.getHostString();
                this.port = soc.getPort();
            } else {
                throw new NullPointerException();
            }
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String toString() {
            return host + ":" + port;
        }

        public String toGenericString() {
            return "NetAddress [" + toString() + "]";
        }

    }

}
