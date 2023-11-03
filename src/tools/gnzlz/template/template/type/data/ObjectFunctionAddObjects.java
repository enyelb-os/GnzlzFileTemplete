package tools.gnzlz.template.template.type.data;

import tools.gnzlz.template.template.type.functional.FunctionAddObjects;

public record ObjectFunctionAddObjects<Type>(FunctionAddObjects<Type> functionAddObjects, Class<Type> type){}