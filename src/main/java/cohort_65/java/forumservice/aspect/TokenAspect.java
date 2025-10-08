package cohort_65.java.forumservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenAspect {

    @Around("execution(* cohort_65.java.forumservice.security.service.TokenService.*(..))")
    public Object logTokenActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("[TOKEN] Вызов метода: " + method);
        Object result = joinPoint.proceed();
        System.out.println("[TOKEN] Метод завершён: " + method);
        return result;
    }
}
