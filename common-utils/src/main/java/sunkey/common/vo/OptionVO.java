package sunkey.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sunkey
 * @since 2018-03-20 14:23
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionVO<K, V> {

    private K key;
    private V value;

    public static <K, V> OptionVO<K, V> newOption(K key, V value) {
        return new OptionVO<>(key, value);
    }

}
