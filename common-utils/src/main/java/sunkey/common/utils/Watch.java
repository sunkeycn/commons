package sunkey.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Sunkey
 * @since 2019-05-09 11:23
 **/
public class Watch {

    private final String name;
    private final LinkedList<Record> history = new LinkedList<>();

    public Watch() {
        this(null);
    }

    public Watch(String name) {
        this.name = name;
        record();
    }

    /**
     * no step. only record a time point.
     *
     * @return last step use time.
     */
    public long record() {
        return record(null);
    }

    /**
     * record a step with a time point.
     *
     * @param stepName
     * @return last step use time.
     */
    public long record(String stepName) {
        long current = System.currentTimeMillis();
        Record originLast = history.peekLast();
        history.addLast(new Record(current, stepName));
        return originLast == null ? 0 : current - originLast.time;
    }

    public long lastUse() {
        if (history.size() >= 2) {
            Iterator<Record> iter = history.descendingIterator();
            Record second = iter.next();
            Record first = iter.next();
            return second.time - first.time;
        }
        return 0;
    }

    public long totalUse() {
        Record first = history.peekFirst();
        Record last = history.peekLast();
        if (first != null) {
            return last.time - first.time;
        }
        return 0;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Record {
        private final long time;
        private final String name;
    }

    /**
     * 精简的时间信息，只有totalUse
     *
     * @return
     */
    @Override
    public String toString() {
        long totalUse = totalUse();
        if (totalUse > 0) {
            return name == null ? "" : name
                    + " use " + totalUse + " ms";
        }
        return "";
    }

    /**
     * 啰嗦的时间信息，包含每个时间节点
     *
     * @return
     */
    public String verbose() {
        if (!history.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Iterator<Record> iter = history.iterator();
            if (name != null) {
                sb.append(name);
            } else {
                sb.append("Watch");
            }
            long prev = iter.next().time;
            long start = prev;
            long last = 0;
            String startTime = Dates.format(start, "yyyy-MM-dd HH:mm:ss.SSS");
            sb.append("(start at ").append(startTime).append(")[");
            while (iter.hasNext()) {
                Record next = iter.next();
                last = next.time;
                if (next.name == null) continue;
                long use = last - prev;
                sb.append(next.name)
                        .append(" use ")
                        .append(use)
                        .append(" ms, ");
                prev = last;
            }
            sb.append(" total use ")
                    .append(last - start)
                    .append(" ms]");
            return sb.toString();
        } else {
            return name == null ? "Watch[]" : name + "[]";
        }
    }
}
