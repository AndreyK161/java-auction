package ws.academy.auction.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Пароль должен содержать минимум 8 символов, одну заглавную," +
            " одну строчную букву, цифру и спецсимвол";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
