package pl.edu.pw.mwotest.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.mwotest.exceptions.ValidationFailedException;

import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final Validator validator;
    public <T> void validate(T obj) {
        Set<ConstraintViolation<T>> validate = validator.validate(obj);
        if(!validate.isEmpty()){
            throw new ValidationFailedException(Arrays.toString(validate.stream().map(ConstraintViolation::getMessage).toArray(String[]::new)));
        }
    }

}
