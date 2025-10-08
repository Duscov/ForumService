package cohort_65.java.forumservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class MetricAspect {

    private final ConcurrentHashMap<String, Long> metrics = new ConcurrentHashMap<>();

    @Around("execution(* cohort_65.java.forumservice.accounting.service.*.*(..))")
    public Object trackMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        metrics.put(methodName + ".start", startTime);
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        metrics.put(methodName + ".duration", endTime - startTime);
        System.out.println("Time: " + LocalDateTime.now() + "[METRICS] Method: "
                + methodName + "execution time" + (endTime - startTime) + "ms");
        return result;
    }
}