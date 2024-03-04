package com.rustret;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UI {
    Map<String, Board> playerBoards = new HashMap<>();

    public void start() {
        startMenu();
        createPlayersMenu();
        playerBoards.keySet().forEach(this::locateShipsMenu);
    }

    void startMenu() {
        clearScreen();
        drawTitle();
        System.out.print("Натисніть ENTER щоб розпочати...");
        waitEnter();
        clearScreen();
    }

    void createPlayersMenu() {
        drawTitle();
        System.out.println("Хто грає?");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ім'я першого гравця: ");
        playerBoards.put(scanner.next(), new Board());
        System.out.print("Ім'я другого гравця: ");
        playerBoards.put(scanner.next(), new Board());
        clearScreen();
    }

    void locateShipsMenu(String player) {
        locateShipsMenu(player, null);
    }

    void locateShipsMenu(String player, String errorMessage) {
        drawTitle();

        if (errorMessage != null) {
            System.out.println("* ПОМИЛКА: " + errorMessage + "\n");
        }

        System.out.println("Розміщення кораблів гравця " + bold(player));
        Board board = playerBoards.get(player);
        board.draw();

        int size = 0;
        if (board.locatedShips < 4) {
            size = 1;
            System.out.println("Залишилось розмістити одно палубні кораблі: " + (4 - board.locatedShips));
        } else if (board.locatedShips < 10) {
            size = 2;
            System.out.println("Залишилось розмістити двох палубні кораблі: " + ((10 - board.locatedShips) / 2));
        } else if (board.locatedShips < 16) {
            size = 3;
            System.out.println("Залишилось розмістити трьох палубні кораблі: " + ((16 - board.locatedShips) / 3));
        } else if (board.locatedShips == 16) {
            size = 4;
            System.out.println("Залишилось розмістити чотирьох палубні кораблі: " + ((20 - board.locatedShips) / 4));
        } else if (board.locatedShips == Board.SHIPS_COUNT) {
            System.out.print("Це кінцевий вигляд вашого флоту, натисніть ENTER щоб продовжити...");
            waitEnter();
            clearScreen();
            return;
        }

        System.out.println(
                "* Примітка: Координати мають містити відповідну букву з горизонтальної осі РЕСПУБЛІКА, і число від 1 до 10\n" +
                        "            не важливо в якому порядку, також можна в кінці вказати '+' для вертикального розміщення корабля,\n" +
                        "            '-' для горизонтального (за замовчуванням буде вертикальне). Наприклад: р1, Е2-, 10А, 5с+");
        System.out.print("Введіть координати: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        if (!input.matches("^[республікаРЕСПУБЛІКА]([1-9]|10)[+-]?$")
                && !input.matches("^([1-9]|10)[республікаРЕСПУБЛІКА][+-]?$")) {
            clearScreen();
            locateShipsMenu(player, "Неправильний формат координат");
        } else if (!board.insertShip(getX(input), getY(input), size, isVertical(input))) {
            clearScreen();
            locateShipsMenu(player, "Ви намагаєтеся розмістити корабель занадто близько до інших кораблів або корабель виходить за\n" +
                    "           рамки ігрового поля, спробуйте інші координати");
        } else {
            clearScreen();
            locateShipsMenu(player);
        }
    }

    int getY(String input) {
        String result = input.replaceAll("[республікаРЕСПУБЛІКА+-]", "");
        return Integer.parseInt(result);
    }

    int getX(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                switch (Character.toLowerCase(c)) {
                    // республіка
                    case 'р': return 1;
                    case 'е': return 2;
                    case 'с': return 3;
                    case 'п': return 4;
                    case 'у': return 5;
                    case 'б': return 6;
                    case 'л': return 7;
                    case 'і': return 8;
                    case 'к': return 9;
                    case 'а': return 10;
                }
            }
        }
        throw new IllegalStateException();
    }

    boolean isVertical(String input) {
        char last = input.charAt(input.length() - 1);
        return last != '-';
    }

    void waitEnter() {
        try {
            System.in.read();
        } catch (IOException e) {}
    }

    void clearScreen() {
        for (int i = 0; i < 30; i ++) {
            System.out.println();
        }
    }

    String bold(String str) {
        return "\u001B[1m" + str + "\u001B[0m";
    }

    void drawTitle() {
        System.out.println("=== Гра «Морський бій» ===\n");
    }
}
