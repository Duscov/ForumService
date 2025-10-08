package cohort_65.java.forumservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidationAspect {

    @Around("@annotation(cohort_65.java.forumservice.aspect.ValidateInput)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Validatable) {
                ((Validatable) arg).validate(); // вызываем метод валидации
            }
        }

        return joinPoint.proceed();
    }
}
