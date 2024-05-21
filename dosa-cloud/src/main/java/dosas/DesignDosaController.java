package dosas;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import dosas.Ingredient;
import dosas.Ingredient.Type;
import dosas.DosaOrder;
import dosas.Dosa;
import dosas.IngredientRepository;

@Controller
@RequestMapping("/design")
@SessionAttributes("dosaOrder")
public class DesignDosaController {

  private final IngredientRepository ingredientRepo;

  @Autowired
  public DesignDosaController(
        IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }

  @ModelAttribute
  public void addIngredientsToModel(Model model) {
    Iterable<Ingredient> ingredients = ingredientRepo.findAll();
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
  public String processTaco(
      @Valid Dosa dosa, Errors errors,
      @ModelAttribute DosaOrder dosaOrder) {

    if (errors.hasErrors()) {
      return "design";
    }

    dosaOrder.addDosa(dosa);

    return "redirect:/orders/current";
  }

  private Iterable<Ingredient> filterByType(
      Iterable<Ingredient> ingredients, Type type) {
    return StreamSupport.stream(ingredients.spliterator(), false)
              .filter(i -> i.getType().equals(type))
              .collect(Collectors.toList());
  }

}