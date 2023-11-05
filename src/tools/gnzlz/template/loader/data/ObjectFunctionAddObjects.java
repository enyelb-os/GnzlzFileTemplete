package tools.gnzlz.template.loader.data;

import tools.gnzlz.template.loader.functional.FunctionAddObjects;

public record ObjectFunctionAddObjects<Type>(FunctionAddObjects<Type> functionAddObjects, Class<Type> type){}