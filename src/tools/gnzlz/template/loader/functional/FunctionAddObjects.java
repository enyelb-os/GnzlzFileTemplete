package tools.gnzlz.template.loader.functional;

import tools.gnzlz.template.Template;

@FunctionalInterface
public interface FunctionAddObjects<Type> {

    void process(Template template, Type type);
}
