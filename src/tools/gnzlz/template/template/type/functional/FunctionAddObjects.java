package tools.gnzlz.template.template.type.functional;

import tools.gnzlz.template.template.Template;

@FunctionalInterface
public interface FunctionAddObjects<Type> {

    void process(Template template, Type type);
}
