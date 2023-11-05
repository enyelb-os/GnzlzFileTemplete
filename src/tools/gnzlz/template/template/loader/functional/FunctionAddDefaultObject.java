package tools.gnzlz.template.template.loader.functional;

import tools.gnzlz.template.template.Template;

@FunctionalInterface
public interface FunctionAddDefaultObject<Type> {

    void process(Template template);
}
