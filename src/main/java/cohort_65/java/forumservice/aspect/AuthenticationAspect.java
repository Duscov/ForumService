package cohort_65.java.forumservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {

    // логирование попыток аутентификации
    @Around("execution(* org.springframework.security.authentication.AuthenticationManager.authenticate(..))")
    public Object logAuthenticationAttempt(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        System.out.println("[SECURITY] Попытка аутентификации: " + args[0]);
        try {
            Object result = joinPoint.proceed();
            System.out.println("[SECURITY] Аутентификация успешна");
            return result;
        } catch (Exception e) {
            System.out.println("[SECURITY] Ошибка аутентификации: " + e.getMessage());
            throw e;
        }
    }
}
