package sunkey.common.utils.template.system;

import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Extensions {

    private final Set<Class> types = new HashSet<>();
    private final LinkedList<Extension> extensions = new LinkedList<>();

    public Extensions() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(UrlFunction.class);
        register(TimeFunction.class);
    }

    public Extension find(String expression) {
        for (Extension extension : extensions) {
            if (extension.accept(expression)) {
                return extension;
            }
        }
        return null;
    }

    @SneakyThrows
    public boolean register(Class<? extends Extension> extensionType) {
        if (types.add(extensionType)) {
            extensions.addFirst(extensionType.newInstance());
            return true;
        }
        return false;
    }

}