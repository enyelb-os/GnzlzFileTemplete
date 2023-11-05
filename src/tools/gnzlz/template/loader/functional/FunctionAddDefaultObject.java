package tools.gnzlz.template.loader.functional;

import tools.gnzlz.template.Template;

@FunctionalInterface
public interface FunctionAddDefaultObject<Type> {

    void process(Template template);
}
