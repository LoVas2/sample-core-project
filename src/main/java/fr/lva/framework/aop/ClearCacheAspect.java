package fr.lva.framework.aop;

import fr.lva.framework.annotation.ClearCache;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ClearCacheAspect {

    @After("@annotation(clearCache)")
    public String clearCache(JoinPoint pjp, ClearCache clearCache) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        ParserContext parserContext = new TemplateParserContext();
        Expression expression = expressionParser.parseExpression(clearCache.value(), parserContext);
        String value = expression.getValue(new RootObject(pjp.getArgs()), String.class);
        return value;
    }

    protected static class RootObject {

        private final Object[] args;

        private RootObject(Object[] args) {
            super();
            this.args = args;
        }

        public Object[] getArgs() {
            return args;
        }
    }
}
