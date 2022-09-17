package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.InputDataExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.SubscribeExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.NotEnoughMoneyExeption;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> type = e.getRequiredType();
        String message;
        if (type.isEnum()) {
            String[] s = Arrays.stream(type.getEnumConstants()).map(o -> o.toString()).toArray(String[]::new);
            message = "The parameter " + e.getName() + " must have a value among : ( " + s[0];
            for (int i = 1; i < s.length; i++) message += ", " + s[i];
            message += " )";
        } else {
            message = "The parameter " + e.getName() + " must be of type " + type.getTypeName();
        }
        return new ResponseEntity<Object>(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DBExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleDBExeption(DBExeption e) {
        String message = "Error while working with data in database";
        return new ResponseEntity<Object>(message, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(InputDataExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleInputDataExeption(InputDataExeption e) {
        String message = "Data u send was wrong";
        return new ResponseEntity<Object>(message, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(CantDeleteObjectExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleCantDeleteObjectExeption(CantDeleteObjectExeption e) {
        String message = "U cant delete this object now";
        return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IsAlreadyStartedExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleIsAlreadyStartedExeption(IsAlreadyStartedExeption e) {
        String message = "Object is already started, now it is a bad time to redact it";
        return new ResponseEntity<Object>(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(WrongIdExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleWrongIdExeption(WrongIdExeption e) {
        String message = "Entity with such id doesn't exist in database";
        return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughMoneyExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotEnoughMoneyExeption(NotEnoughMoneyExeption e) {
        String message = "Not enough money to make this payment";
        return new ResponseEntity<Object>(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(SubscribeExeption.class)
    @ResponseBody
    public ResponseEntity<Object> handleSubscribeExeption(SubscribeExeption e) {
        String message = "Cant subscribe user to course";
        return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationFailedResponse onConstraintValidationException(ConstraintViolationException e) {
        ValidationFailedResponse error = new ValidationFailedResponse();
        for (ConstraintViolation violation: e.getConstraintViolations()) {
            error.getViolations().add(new ViolationErrors(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationFailedResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationFailedResponse error = new ValidationFailedResponse();
        for (FieldError fieldError: e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(new ViolationErrors(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }
}