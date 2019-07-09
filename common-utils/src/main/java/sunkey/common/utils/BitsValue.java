package sunkey.common.utils;

import java.io.Serializable;
import java.util.BitSet;

/**
 * @author Sunkey
 */
public class BitsValue implements Serializable {

    protected final BitSet bits;

    public BitsValue() {
        this(0L);
    }

    public BitsValue(long value) {
        this(new long[]{value});
    }

    public BitsValue(long[] values) {
        this.bits = BitSet.valueOf(values);
    }

    public BitSet getBitsData() {
        return bits;
    }

    public void setBit(int pos) {
        bits.set(pos);
    }

    public void setBit(int pos, boolean value) {
        bits.set(pos, value);
    }

    public boolean getBit(int pos) {
        return bits.get(pos);
    }

    public long toLongValue() {
        long[] longs = bits.toLongArray();
        return longs.length > 0 ? longs[0] : 0L;
    }

    public long[] toLongArray() {
        return bits.toLongArray();
    }

    @Override
    public String toString() {
        return "BitValue[value=" + bits + "]";
    }
}
