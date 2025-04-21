package practikum.order;

import java.util.Arrays;
import java.util.List;

public class Order {
    private List<String> ingredients;

    public Order() {
    }

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public static List<Order> getOrderList() {
        return Arrays.asList(
                new Order(List.of("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"))
        );
    }

    public static Order getEmptyIngredients() {
        return new Order(List.of());
    }

    public static Order getInvalidHashIngredient() {
        return new Order(List.of("qwe123"));
    }
}
