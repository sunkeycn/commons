package sunkey.common.utils.template.system;

import java.util.Map;

public class FunctionParamBlock extends ParamBlock {

    private final Function function;
    private final String functionName;

    public FunctionParamBlock(String str, Function function) {
        super(str);
        this.function = function;
        this.functionName = Function.getFunctionName(expression);
        String paramName = Function.getParamName(expression);
        addAlias(paramName);
    }

    @Override
    public String render(Map<String, ?> data) {
        for (String alias : aliases) {
            String result = function.render(functionName, alias, data);
            if (result != null) return result;
        }
        return super.render(data);
    }

}