package sunkey.common.utils.excel.valid;

/**
 * @author Sunkey
 * @since 2019-06-11 16:50
 **/
public interface ConstraintValidator<A, T> {

    void validate(T value, A anno, ValidContext context);

}
