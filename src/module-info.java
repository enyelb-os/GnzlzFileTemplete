module tools.gnzlz.template {
    requires transitive tools.gnzlz.system.io;

    exports tools.gnzlz.template.loader.functional;
    exports tools.gnzlz.template.instruction.reflection.functional;
    exports tools.gnzlz.template;
    exports tools.gnzlz.template.loader.data;
}