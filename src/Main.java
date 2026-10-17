import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static final int LANES = 3;
    static final int VISIBLE_DISTANCE = 5; // Сколько клеток впереди мы видим

    static Player player = new Player(1); // Игрок стартует в центре
    static Monster monster = new Monster(1, 3); // Монстр в центре, отстает на 3 клетки
    static ArrayList<Obstacle> obstacles = new ArrayList<>();
    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    static boolean isGameOver = false;
    static int score = 0;

    public static void main(String[] args) {
        System.out.println("Игра началась! Управление: 'a' - влево, 'd' - вправо. Нажмите Enter, чтобы бежать.");

        while (!isGameOver) {
            drawField();
            handleInput();
            updateObjects();
            checkCollisions();

            if (monster.getDistanceBehind() <= 0) {
                System.out.println("\nМонстр догнал вас! Игра окончена. Ваш счет: " + score);
                isGameOver = true;
            }
        }
    }

    // Отрисовка игрового поля в консоли
    static void drawField() {
        System.out.println("\n--- ИГРА --- Счет: " + score);
        // Рисуем сверху вниз (от дальних объектов к монстру)
        for (int row = VISIBLE_DISTANCE; row >= 0; row--) {
            for (int lane = 0; lane < LANES; lane++) {
                String cell = " . "; // Пустая клетка

                // Проверяем, есть ли препятствие в этой клетке
                for (Obstacle obs : obstacles) {
                    if (obs.getDistance() == row && obs.getLane() == lane) {
                        cell = obs.getType().equals("trap") ? " X " : " ? "; // X - препятствие, ? - бонус
                    }
                }

                // Проверяем, где игрок (он всегда на 0-й строке)
                if (row == 0 && lane == player.getLane()) {
                    cell = " P ";
                }

                // Проверяем, где монстр (он на отрицательной дистанции относительно игрока)
                if (row == 0 && lane == monster.getLane() && monster.getDistanceBehind() == 1) {
                    cell = " M ";
                } else if (row == 0 && lane == monster.getLane() && monster.getDistanceBehind() == 2) {
                    // показываем монстра на строке ниже, если он далеко
                    // На самом деле в консоли сложно показать отрицательные расстояния,
                    // поэтому монстр просто пишется под полем
                }

                System.out.print(cell);
            }
            System.out.println();
        }

        // Показываем линию монстра снизу
        System.out.print("Монстр: ");
        for (int lane = 0; lane < LANES; lane++) {
            if (lane == monster.getLane()) {
                System.out.print(" M ");
            } else {
                System.out.print(" . ");
            }
        }
        System.out.println(" (Отставание: " + monster.getDistanceBehind() + " клеток)");
    }

    // Обработка ввода
    static void handleInput() {
        System.out.print("Ввод (a/d): ");
        String input = scanner.nextLine();
        if (input.equals("a")) {
            player.moveLeft();
        } else if (input.equals("d")) {
            player.moveRight();
        }
    }

    // Обновление позиций
    static void updateObjects() {
        // Двигаем все препятствия на 1 шаг к игроку
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).moveTowardsPlayer();
            // Если объект прошел мимо игрока (дистанция < 0) - удаляем его
            if (obstacles.get(i).getDistance() < 0) {
                obstacles.remove(i);
                i--;
                score += 10; // Даем очки за то, что увернулись
            }
        }

        // С шансом 30% генерируем новое препятствие на дальнем плане
        if (random.nextInt(100) < 30) {
            int lane = random.nextInt(LANES);
            String type = random.nextBoolean() ? "trap" : "item";
            obstacles.add(new Obstacle(type, lane, VISIBLE_DISTANCE));
        }
    }

    // Проверка столкновений
    static void checkCollisions() {
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obs = obstacles.get(i);
            // Если объект на дистанции 0 и на полосе игрока
            if (obs.getDistance() == 0 && obs.getLane() == player.getLane()) {

                if (obs.getType().equals("trap")) {
                    System.out.println("\n>>> Вы споткнулись о препятствие! Монстр приближается!");
                    monster.getCloser();
                    // Сдвигаем монстра в ту же полосу, где споткнулся игрок
                    monster.moveToLane(player.getLane());
                }
                else if (obs.getType().equals("item")) {
                    System.out.println("\n>>> Вы нашли бонус! Решите пример, чтобы оторваться от монстра.");
                    boolean success = miniGame();
                    if (success) {
                        System.out.println("Верно! Монстр отстает.");
                        monster.fallBehind();
                        score += 50;
                    } else {
                        System.out.println("Ошибка! Монстр приближается.");
                        monster.getCloser();
                    }
                }

                obstacles.remove(i);
                i--;
            }
        }
    }

    // Мини-игра с математикой
    static boolean miniGame() {
        int a = random.nextInt(10) + 1;
        int b = random.nextInt(10) + 1;
        System.out.print("Реши пример: " + a + " + " + b + " = ");
        int answer = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера после ввода числа
        return answer == (a + b);
    }
}