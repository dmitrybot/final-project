package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption;

public class DBExeption extends RuntimeException {

    public DBExeption() {
    }

    public DBExeption(String message) {
        super(message);
    }
}
