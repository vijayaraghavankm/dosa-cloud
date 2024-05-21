package dosas;

import java.util.Optional;
import dosas.DosaOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<DosaOrder, Long> {
	DosaOrder save(DosaOrder order);
}