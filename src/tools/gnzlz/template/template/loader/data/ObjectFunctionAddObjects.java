package tools.gnzlz.template.template.loader.data;

import tools.gnzlz.template.template.loader.functional.FunctionAddObjects;

public record ObjectFunctionAddObjects<Type>(FunctionAddObjects<Type> functionAddObjects, Class<Type> type){}