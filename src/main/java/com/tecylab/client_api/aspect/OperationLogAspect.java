package com.tecylab.client_api.aspect;

import com.tecylab.client_api.annotation.TecyLabLog;
import com.tecylab.client_api.model.OperationLog;
import com.tecylab.client_api.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogRepository operationLogRepository;

    @Around("@annotation(tecyLabLog)")
    public Object createOperationLog(ProceedingJoinPoint joinPoint, TecyLabLog tecyLabLog) throws Throwable{
        Object result = joinPoint.proceed(); // Proceed with the method execution

        String description = resolveDescription(tecyLabLog.description(), joinPoint, result);

        OperationLog newOperationLog = OperationLog.builder()
                .operation(tecyLabLog.type().toString())
                .description("Operation executed: " + tecyLabLog.description())
                .timestamp(java.time.LocalDateTime.now())
                .build();

        // Save the operation log to MongoDB
        operationLogRepository.save(newOperationLog);

        return result;
    }

    private String resolveDescription(String description, ProceedingJoinPoint joinPoint, Object result) {
        if (description.isEmpty() || description == null) {
            return joinPoint.getSignature().getName(); // Use method name as description if not provided
        }
        if (description.contains("#id")) {
            try {
                var methodResult = result.getClass().getMethod("id");
                Object idValue = methodResult.invoke(result);
                if (idValue != null) {
                    description = description.replace("#id", idValue.toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return description;
    }
}
