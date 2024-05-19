 package dosas;
  
  import javax.validation.Valid;
  import org.springframework.validation.Errors;
  import java.util.Arrays;
  import java.util.List;
  import java.util.stream.Collectors;
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.ModelAttribute;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.SessionAttributes;
  import lombok.extern.slf4j.Slf4j;
  import dosas.Ingredient;
  import dosas.Ingredient.Type;
  import dosas.Dosa;
  @Slf4j
  @Controller
  @RequestMapping("/design")
  @SessionAttributes("dosaOrder")
  public class DesignDosaController {
  @ModelAttribute
  public void addIngredientsToModel(Model model) {
     List<Ingredient> ingredients = Arrays.asList(
       new Ingredient("RIDO", "Rice Dosa", Type.BASE),
       new Ingredient("MIDO", "Millet Dosa", Type.BASE),
       new Ingredient("PANR", "Paneer", Type.TOPPING),
       new Ingredient("MHRM", "Mushroom", Type.TOPPING),
       new Ingredient("CHTY", "Chutney", Type.SPREAD),
       new Ingredient("PODI", "Podi", Type.SPREAD),
       new Ingredient("OTPM", "Oothappam", Type.STYLE),
       new Ingredient("RGLR", "Regular", Type.STYLE),
       new Ingredient("GRND", "Groundnut Oil", Type.FAT),
       new Ingredient("GHEE", "Cow Ghee", Type.FAT)
     );
     Type[] types = Ingredient.Type.values();
     for (Type type : types) {
       model.addAttribute(type.toString().toLowerCase(),
   filterByType(ingredients, type));
     }
   }
   @ModelAttribute(name = "dosaOrder")
   public DosaOrder order() {
     return new DosaOrder();
   }
   @ModelAttribute(name = "dosa")
   public Dosa dosa() {
     return new Dosa();
   }
   @GetMapping
   public String showDesignForm() {
     return "design";
   }
   
   @PostMapping
   public String processDosa(
		   @Valid Dosa dosa, Errors errors,
		   @ModelAttribute DosaOrder dosaOrder) {
		     if (errors.hasErrors()) {
		       return "design";
		     }
		     dosaOrder.addDosa(dosa);
		     log.info("Processing dosa: {}", dosa);
		     return "redirect:/orders/current";
		   }
   
   private Iterable<Ingredient> filterByType(
       List<Ingredient> ingredients, Type type) {
     return ingredients
       .stream()
       .filter(x -> x.getType().equals(type))
       .collect(Collectors.toList());
   }
 }