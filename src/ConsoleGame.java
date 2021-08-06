import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author AutumnSpark1226
 * @version 2021.7.11
 */

public class ConsoleGame {
    private static BufferedReader br;
    private static int stage = 1;
    private static StringBuilder builder = new StringBuilder();
    private static Boolean alive = true;
    //hero
    private static int heroPos = 0;
    private static int heroHp = 10;
    private static int heroXp = 0;
    private static int heroLevel = 1;
    private static int heroAttackDamage = 1;
    public static final int[] xpTable = {0, 10, 20, 40, 100, 200, 500, 750, 1000, 1500};
    private static final ArrayList<String> inventory = new ArrayList<>(20);
    private static double heroCritValue = 0.01;
    //enemy
    private static int enemyPos = 9;
    private static int enemyHp = 5;
    private static int enemyLevel = 1;
    private static int enemyAttackDamage = 1;
    private static byte attackDamageCounter = 0;
    private static double enemyCritValue = 0.01;
    private static int enemyHPO = 0;

    public static void main(String[] args) {
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(game("show"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.print("> ");
                String input = br.readLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                } else if (input.equalsIgnoreCase("getSaveState")) {
                    System.out.println(getSaveState());
                } else if (input.startsWith("loadsavestate ")) {
                    try {
                        loadSaveState(input.replace("loadsavestate ", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(game(input));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String game(String input) {
        String output;
        if (input.equalsIgnoreCase("show")) {
            output = show();
        } else if (input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
            right();
            output = show();
        } else if (input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left")) {
            left();
            output = show();
        } else if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("weapon")) {
            weapon();
            output = show();
        } else if (input.equalsIgnoreCase("s") || input.equalsIgnoreCase("heal")) {
            heal();
            output = show();
        } else if (input.equalsIgnoreCase("i") || input.equalsIgnoreCase("inventory")) {
            output = showInventory();
            builder = new StringBuilder();
        } else if (input.equalsIgnoreCase("t") || input.equalsIgnoreCase("stats")) {
            output = stats();
            builder = new StringBuilder();
        } else if (input.startsWith("u") || input.startsWith("use")) {
            output = use(input.replace("use ", "").replace("u ", ""));
        } else {
            output = "Unknown command";
        }
        return output;
    }

    private static void generateNextStage() {
        stage++;
        builder.append("Stage: ").append(stage).append("\n");
        if (stage == 2) {
            inventory.add("HPO");
            inventory.add("HPO");
            inventory.add("HPO");
            builder.append("You found three health potions\n");
        } else if (Math.random() < 0.2 + (double) heroLevel / 15 && inventory.size() != 20) {
            inventory.add("HPO");
            builder.append("You found a health potion\n");
        }
        heroHp = heroLevel * 10;
        heroPos = 0;
        enemyPos = 9;
        enemyLevel++;
        enemyHp = 5 * enemyLevel;
        enemyCritValue += 0.01;
        enemyHPO = (int) (Math.random() * (enemyLevel - 2));
        if (attackDamageCounter == 5) {
            enemyAttackDamage = enemyLevel - 1;
            attackDamageCounter = 0;
        } else {
            attackDamageCounter++;
        }
    }

    private static void enemy() {
        if (enemyHp > 0) {
            if ((enemyPos - heroPos) != 1 && (enemyPos - heroPos) > 0) {
                enemyPos--;
            } else if ((heroPos - enemyPos) != 1 && (heroPos - enemyPos) > 0) {
                enemyPos++;
            } else if (enemyHp <= heroAttackDamage && enemyHPO > 0 && heroHp > enemyAttackDamage) {
                enemyHp += enemyLevel * 5;
                enemyHPO--;
            } else if ((enemyPos - heroPos == 1 || heroPos - enemyPos == 1)) {
                if (Math.random() < enemyCritValue) {
                    heroHp -= enemyAttackDamage * 2;
                } else {
                    heroHp -= enemyAttackDamage;
                }
                if (heroHp < 1) {
                    alive = false;
                }
            }
        }
    }

    private static String show() {
        if (!alive) {
            return "Game over!\nStage: " + stage + "\nLevel: " + heroLevel;
        }
        builder.append("HP:").append(heroHp).append("___E:").append(enemyHp).append('\n');
        for (int i = 0; i < (heroPos); i++) {
            builder.append('_');
        }
        builder.append('0');
        for (int i = 0; i < (enemyPos - heroPos - 1); i++) {
            builder.append('_');
        }
        if (enemyHp > 0) {
            builder.append('8');
        } else if (heroPos != 9) {
            builder.append('_');
        }
        if (heroPos < enemyPos) {
            for (int i = 0; i < (9 - enemyPos); i++) {
                builder.append('_');
            }
        }
        builder.append("[]");
        String output = builder.toString();
        builder = new StringBuilder();
        return output;
    }

    private static void right() {
        if (heroPos < 9 && ((enemyPos - heroPos) != 1 || enemyHp < 1)) {
            heroPos += 1;
            enemy();
        } else if (heroPos > 8 && enemyHp < 1) {
            generateNextStage();
        }
    }

    private static void left() {
        if (heroPos >= 1 && ((heroPos - enemyPos) != 1 || enemyHp < 1)) {
            heroPos -= 1;
            enemy();
        }
    }

    private static void weapon() {
        if ((enemyPos - heroPos == 1 || heroPos - enemyPos == 1) && enemyHp > 0) {
            if (Math.random() < heroCritValue) {
                enemyHp -= heroAttackDamage * 2;
            } else {
                enemyHp -= heroAttackDamage;
            }
            if (enemyHp < 1) {
                if (enemyHp < 0) {
                    enemyHp = 0;
                }
                int droppedXp = (int) ((double) enemyLevel * 5 * (Math.random() + 1));
                heroXp += droppedXp;
                enemyPos = 9;
                while (heroLevel < 10 && heroXp >= xpTable[heroLevel]) {
                    heroLevel++;
                    heroHp = heroLevel * 10;
                    heroAttackDamage++;
                    builder.append("Level up!\n");
                    heroCritValue += 0.01;
                }
                builder.append("Drops:\n");
                builder.append("XP: ").append(droppedXp).append("\n");
                if (Math.random() <= 0.46 + (double) heroLevel / 15) {
                    if (inventory.size() != 20) {
                        inventory.add("HPO");
                        builder.append("Health potion\n");
                    } else {
                        builder.append("You inventory is full\n");
                    }
                }
                if (Math.random() < 0.3 + (double) heroLevel / 50) {
                    if (inventory.size() != 20) {
                        inventory.add("HMP");
                        builder.append("Health multiplier\n");
                    } else {
                        builder.append("You inventory is full\n");
                    }
                }
                if (Math.random() < 0.1 + (double) heroLevel / 50) {
                    if (inventory.size() != 20) {
                        inventory.add("ADMP");
                        builder.append("Attack damage multiplier\n");
                    } else {
                        builder.append("You inventory is full\n");
                    }
                }
                if (Math.random() < 0.05 + (double) heroLevel / 100) {
                    if (inventory.size() != 20) {
                        inventory.add("CMP");
                        builder.append("Critical hit multiplier\n");
                    } else {
                        builder.append("You inventory is full\n");
                    }
                }
            }
            enemy();
        }
    }

    private static void heal() {
        if (inventory.contains("HPO")) {
            inventory.remove("HPO");
            heroHp += (enemyLevel * 5 + heroLevel);
            enemy();
        } else {
            builder.append("You don't have any health potions!\n");
        }
    }

    private static String showInventory() {
        if (inventory.size() != 0) {
            builder = new StringBuilder("Inventory");
            int hpo = 0;
            int hmp = 0;
            int admp = 0;
            int cmp = 0;
            for (String s : inventory) {
                if (s.equals("HPO")) {
                    hpo++;
                }
                if (s.equals("HMP")) {
                    hmp++;
                }
                if (s.equals("ADMP")) {
                    admp++;
                }
                if (s.equals("CMP")) {
                    cmp++;
                }
            }
            builder.append(" [").append(inventory.size()).append("/20]");
            if (hpo > 0) {
                builder.append("\nHealth potion(s)[HPO]: ").append(hpo);
            }
            if (hmp > 0) {
                builder.append("\nHealth multiplier(s)[HMP]: ").append(hmp);
            }
            if (admp > 0) {
                builder.append("\nAttack damage multiplier(s)[ADMP]: ").append(admp);
            }
            if (cmp > 0) {
                builder.append("\nCritical hit multiplier(s)[CMP]: ").append(cmp);
            }
        } else {
            return "Your inventory is empty";
        }
        return builder.toString();
    }

    private static String stats() {
        builder = new StringBuilder("Stats:\n");
        builder.append("Level: ").append(heroLevel);
        builder.append("\nXP: ").append(heroXp);
        if (heroLevel < 10) {
            builder.append("\nXP to next: ").append(xpTable[heroLevel] - heroXp);
        }
        builder.append("\nAttack damage: ").append(heroAttackDamage);
        builder.append("\nCritical hit chance: ").append((int) (heroCritValue * 100)).append("%");
        return builder.toString();
    }

    private static String use(String item) {
        String output;
        if (item.equalsIgnoreCase("hpo") || item.equalsIgnoreCase("health potion")) {
            if (inventory.contains("HPO")) {
                heal();
                output = show();
            } else {
                output = "You don't have any health potions!";
            }
        } else if (item.equalsIgnoreCase("hmp") || item.equalsIgnoreCase("health multiplier")) {
            if (inventory.contains("HMP")) {
                if (heroHp * 2 <= (enemyHp + enemyHPO * enemyLevel * 5) - 50) {
                    heroHp *= 2;
                } else {
                    heroHp += heroLevel * 3;
                }
                inventory.remove("HMP");
                enemy();
                output = show();
            } else {
                output = "You don't have any health multipliers!";
            }
        } else if (item.equalsIgnoreCase("admp") || item.equalsIgnoreCase("attack damage multiplier")) {
            if (inventory.contains("ADMP")) {
                if (heroAttackDamage * 2 <= 16) {
                    heroAttackDamage *= 2;
                } else {
                    heroAttackDamage++;
                }
                inventory.remove("ADMP");
                enemy();
                output = show();
            } else {
                output = "You don't have any attack damage multipliers!";
            }
        } else if (item.equalsIgnoreCase("cmp") || item.equalsIgnoreCase("critical hit multiplier")) {
            if (inventory.contains("CMP")) {
                if (heroCritValue * 2 <= 40) {
                    heroCritValue *= 2;
                } else {
                    heroCritValue += 0.1;
                }
                inventory.remove("CMP");
                enemy();
                output = show();
            } else {
                output = "You don't have any attack damage multipliers!";
            }
        } else {
            output = "Unknown item";
        }
        return output;
    }

    public static String getSaveState() {
        StringBuilder stringBuilder = new StringBuilder("CG");
        stringBuilder.append(stage).append(";")
                .append(alive).append(";")
                .append(heroPos).append(";")
                .append(heroHp).append(";")
                .append(heroXp).append(";")
                .append(heroLevel).append(";")
                .append(heroAttackDamage).append(";")
                .append(heroCritValue).append(";")
                .append(enemyPos).append(";")
                .append(enemyHp).append(";")
                .append(enemyLevel).append(";")
                .append(enemyAttackDamage).append(";")
                .append(attackDamageCounter).append(";")
                .append(enemyCritValue).append(";")
                .append(enemyHPO).append(";");
        for (String s : inventory) {
            stringBuilder.append(s).append(";");
        }
        return stringBuilder.toString();
    }

    public static void loadSaveState(String saveState) {
        String[] values = saveState.replace("CG", "").split(";");
        stage = Integer.parseInt(values[0]);
        alive = Boolean.valueOf(values[1]);
        heroPos = Integer.parseInt(values[2]);
        heroHp = Integer.parseInt(values[3]);
        heroXp = Integer.parseInt(values[4]);
        heroLevel = Integer.parseInt(values[5]);
        heroAttackDamage = Integer.parseInt(values[6]);
        heroCritValue = Double.parseDouble(values[7]);
        enemyPos = Integer.parseInt(values[8]);
        enemyHp = Integer.parseInt(values[9]);
        enemyLevel = Integer.parseInt(values[10]);
        enemyAttackDamage = Integer.parseInt(values[11]);
        attackDamageCounter = Byte.parseByte(values[12]);
        enemyCritValue = Double.parseDouble(values[13]);
        enemyHPO = Integer.parseInt(values[14]);
        inventory.clear();
        for (int i = 14; i < values.length; i++) {
            if (values[i].equals("HPO") || values[i].equals("HMP") || values[i].equals("ADMP") || values[i].equals("CMP")) {
                inventory.add(values[i]);
            }
        }
    }
}