package com.example.inventory.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.example.inventory.service..*(..)) || execution(* com.example.inventory.controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // Argümanları mask’leme
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";
                    String argStr = arg.toString();
                    if (argStr.toLowerCase().contains("password")) return "****";
                    return argStr;
                })
                .collect(Collectors.joining(", "));

        log.info("➡️ Entering [{}::{}] with arguments [{}]", className, methodName, argsString);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
        	log.error("❌ Exception in [{}::{}]: {}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }

        long duration = System.currentTimeMillis() - start;
        log.info("✅ Exiting [{}::{}]; Return: {}; Execution time: {} ms", className, methodName, result, duration);

        return result;
    }
}
