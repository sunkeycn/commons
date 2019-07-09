package sunkey.common.utils.template.system;

import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParamBlock implements Block {

    @Getter
    protected final String expression;
    @Getter
    protected final String defaultValue;
    @Getter
    protected final Set<String> aliases = new HashSet<>();

    @Override
    public String toString() {
        return defaultValue == "" ?
                expression : expression + "!" + defaultValue;
    }

    public void addAlias(String alias) {
        this.aliases.add(alias);
    }

    public ParamBlock(String str) {
        // resolve default value
        // like : {paramName!Default}
        //        {paramName!null}
        int idx = str.lastIndexOf('!');
        if (idx > -1) {
            this.expression = str.substring(0, idx);
            this.defaultValue = str.substring(idx + 1);
        } else {
            this.expression = str;
            this.defaultValue = "";
        }
        addAlias(this.expression);
    }

    public String render(Map<String, ?> data) {
        if (data == null) return defaultValue;
        Object value = null;
        for (String alias : aliases) {
            value = data.get(alias);
            if (value != null) break;
        }
        if (value == null) return defaultValue;
        else return value.toString();
    }

}
