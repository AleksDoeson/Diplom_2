package utils;

import java.util.List;

public class OrderModel {
    private List<String> ingredients;

    public OrderModel() {}

    public OrderModel(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}



