package cohort_65.java.forumservice.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAspect {

    @Pointcut("execution(* cohort_65.java.forumservice..*(..))")
    public void anyForumServiceMethod() {}

    @AfterThrowing(pointcut = "anyForumServiceMethod()", throwing = "ex")
    public void logException(Throwable ex) {
        System.err.println("[EXCEPTION-AOP] Ошибка: " + ex.getClass().getSimpleName() +
                " — " + ex.getMessage());
        // Можно добавить логгер, отправку в мониторинг, сохранение в базу и т.д.
    }
}
