package tools.gnzlz.template.instruction.expression;

import java.util.Stack;

public class Resolver {

    /**
     * number
     * @param expression e
     */
    public static Object number(Object expression){
        Number resolve = new Number();
        Stack<Number> resolves = new Stack<Number>();
        resolves.push(resolve);
        String numberCurrent = "";
        for (int i = expression.toString().length() - 1; i >= 0 ; i--) {
            char c = expression.toString().charAt(i);
            if (Character.isDigit(c)) {
                numberCurrent = c + numberCurrent;
            } else if(!numberCurrent.isEmpty()) {
                resolve.addValue(Double.parseDouble(numberCurrent));
                numberCurrent = "";
            }

            if (c == '+' || c == '-' || c == '/' || c == '*') {
                resolve.addOperator(c + "");
            }else if(c == ')'){
                resolves.push(new Number());
                resolve.addValue(resolves.lastElement());
                resolve = resolves.lastElement();
            }else if(c == '('){
                resolves.pop();
                resolve = resolves.lastElement();
            }else if(c != ' ' && !Character.isDigit(c)){
                return expression;
            }
        }

        if(!numberCurrent.isEmpty()) {
            resolve.addValue(Double.parseDouble(numberCurrent));
        }

        if (resolves.size() > 1){
            return expression;
        }

        return resolve.result();
    }
}
