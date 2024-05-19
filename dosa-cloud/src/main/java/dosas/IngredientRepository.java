package dosas;

import java.util.Optional;
import dosas.Ingredient;

public interface IngredientRepository {
	Iterable<Ingredient> findAll();

	Optional<Ingredient> findById(String id);

	Ingredient save(Ingredient ingredient);
}