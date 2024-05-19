package dosas;
 import java.util.HashMap;
 import java.util.Map;
 import org.springframework.core.convert.converter.Converter;
 import org.springframework.stereotype.Component;
 import dosas.Ingredient;
 import dosas.Ingredient.Type;
 @Component
 public class IngredientByIdConverter implements Converter<String, Ingredient> {
  private Map<String, Ingredient> ingredientMap = new HashMap<>();
  public IngredientByIdConverter() {
    ingredientMap.put("RIDO", 
new Ingredient("RIDO", "Rice Dosa", Type.BASE));
    ingredientMap.put("COTO", 
new Ingredient("MIDO", "Millet Dosa", Type.BASE));
    ingredientMap.put("PANR", 
new Ingredient("PANR", "Paneer", Type.TOPPING));
    ingredientMap.put("MHRM", 
new Ingredient("MHRM", "Mushroom", Type.TOPPING));
    ingredientMap.put("CHTY", 
new Ingredient("CHTY", "Chutney", Type.SPREAD));
    ingredientMap.put("PODI", 
new Ingredient("PODI", "Podi", Type.SPREAD));
    ingredientMap.put("OTPM", 
new Ingredient("OTPM", "Oothappam", Type.STYLE));
    ingredientMap.put("RGLR", 
new Ingredient("RGLR", "Regular", Type.STYLE));
    ingredientMap.put("GRND", 
new Ingredient("GRND", "Groundnut Oil", Type.FAT));
    ingredientMap.put("GHEE", 
new Ingredient("GHEE", "Cow Ghee", Type.FAT));
  }
  @Override
  public Ingredient convert(String id) {
    return ingredientMap.get(id);
  }
 }