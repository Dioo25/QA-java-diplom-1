package praktikum;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Burger {

    public Bun bun;
    public List<Ingredient> ingredients = new ArrayList<>();

    public void setBuns(Bun bun) {
        this.bun = bun;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(int index) {
        ingredients.remove(index);
    }

    public void moveIngredient(int index, int newIndex) {
        Ingredient ingredient = ingredients.remove(index);
        ingredients.add(newIndex, ingredient);
    }

    public float getPrice() {
        float total = bun.getPrice() * 2;
        for (Ingredient ingredient : ingredients) {
            total += ingredient.getPrice();
        }
        return total;
    }

    public String getReceipt() {
        // Собираем строку построчно, используем '\n' — совпадает с ожиданием тестов
        StringBuilder sb = new StringBuilder();

        String bunName = bun.getName() == null ? "" : bun.getName().trim();
        sb.append("(==== ").append(bunName).append(" ====)").append("\n");

        for (Ingredient ingredient : ingredients) {
            String type = ingredient.getType() == null ? "" : ingredient.getType().toString().toLowerCase();
            String name = ingredient.getName() == null ? "" : ingredient.getName().trim();
            sb.append("= ").append(type).append(" ").append(name).append(" =").append("\n");
        }

        sb.append("(==== ").append(bunName).append(" ====)").append("\n");
        sb.append("\n");

        // Форматируем цену точно: точка как десятичный разделитель, один знак после точки
        sb.append(String.format(Locale.US, "Price: %.1f", getPrice()));
        sb.append("\n");

        return sb.toString();
    }
}
