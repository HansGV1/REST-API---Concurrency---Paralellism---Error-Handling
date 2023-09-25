package IGM.Technology.sbtt.CustomAnnotations;

import IGM.Technology.sbtt.Exceptions.CustomHttpClientErrorException;
import org.springframework.core.annotation.AliasFor;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
        maxAttempts = 3,            // Maximum number of retry attempts
        backoff = @Backoff(
                delay = 1000,           // Initial delay in milliseconds
                multiplier = 2,         // Delay multiplier for exponential backoff
                maxDelay = 4000         // Maximum delay in milliseconds
        ),
        exclude = {CustomHttpClientErrorException.class},
        include = {ExhaustedRetryException.class}

)
public @interface RetryRequestBack {
    @AliasFor(annotation = Retryable.class)
    Class<? extends Throwable>[] value() default {};
}