package dosas;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.asm.Type;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dosas.IngredientRef;
import dosas.Dosa;
import dosas.DosaOrder;

@Repository
public class JdbcOrderRepository implements OrderRepository {

  private JdbcOperations jdbcOperations;

  public JdbcOrderRepository(JdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  @Override
  @Transactional
  public DosaOrder save(DosaOrder order) {
    PreparedStatementCreatorFactory pscf =
      new PreparedStatementCreatorFactory(
        "insert into Dosa_Order "
        + "(delivery_name, delivery_street, delivery_city, "
        + "delivery_state, delivery_zip, cc_number, "
        + "cc_expiration, cc_cvv, placed_at) "
        + "values (?,?,?,?,?,?,?,?,?)",
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
    );
    pscf.setReturnGeneratedKeys(true);

    order.setPlacedAt(new Date());
    PreparedStatementCreator psc =
        pscf.newPreparedStatementCreator(
            Arrays.asList(
                order.getDeliveryName(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getDeliveryState(),
                order.getDeliveryZip(),
                order.getCcNumber(),
                order.getCcExpiration(),
                order.getCcCVV(),
                order.getPlacedAt()));

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);
    long orderId = keyHolder.getKey().longValue();
    order.setId(orderId);

    List<Dosa> dosas = order.getDosas();
    int i=0;
    for (Dosa dosa : dosas) {
      saveDosa(orderId, i++, dosa);
    }

    return order;
  }

  private long saveDosa(Long orderId, int orderKey, Dosa dosa) {
    dosa.setCreatedAt(new Date());
    PreparedStatementCreatorFactory pscf =
            new PreparedStatementCreatorFactory(
        "insert into Dosa "
        + "(name, created_at, dosa_order, dosa_order_key) "
        + "values (?, ?, ?, ?)",
        Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
    );
    pscf.setReturnGeneratedKeys(true);

    PreparedStatementCreator psc =
        pscf.newPreparedStatementCreator(
            Arrays.asList(
                dosa.getName(),
                dosa.getCreatedAt(),
                orderId,
                orderKey));

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);
    long dosaId = keyHolder.getKey().longValue();
    dosa.setId(dosaId);
    saveIngredientRefs(dosaId, dosa.getIngredients());
    return dosaId;
  }

  private void saveIngredientRefs(
      long dosaId, List<IngredientRef> ingredientRefs) {
    int key = 0;
    for (IngredientRef ingredientRef : ingredientRefs) {
      jdbcOperations.update(
          "insert into Ingredient_Ref (ingredient, dosa, dosa_key) "
          + "values (?, ?, ?)",
          ingredientRef.getIngredient(), dosaId, key++);
    }
  }

  @Override
  public Optional<DosaOrder> findById(Long id) {
    try {
      DosaOrder order = jdbcOperations.queryForObject(
          "select id, delivery_name, delivery_street, delivery_city, "
              + "delivery_state, delivery_zip, cc_number, cc_expiration, "
              + "cc_cvv, placed_at from Dosa_Order where id=?",
          (row, rowNum) -> {
            DosaOrder dosaOrder = new DosaOrder();
            dosaOrder.setId(row.getLong("id"));
            dosaOrder.setDeliveryName(row.getString("delivery_name"));
            dosaOrder.setDeliveryStreet(row.getString("delivery_street"));
            dosaOrder.setDeliveryCity(row.getString("delivery_city"));
            dosaOrder.setDeliveryState(row.getString("delivery_state"));
            dosaOrder.setDeliveryZip(row.getString("delivery_zip"));
            dosaOrder.setCcNumber(row.getString("cc_number"));
            dosaOrder.setCcExpiration(row.getString("cc_expiration"));
            dosaOrder.setCcCVV(row.getString("cc_cvv"));
            dosaOrder.setPlacedAt(new Date(row.getTimestamp("placed_at").getTime()));
            dosaOrder.setDosas(findDosasByOrderId(row.getLong("id")));
            return dosaOrder;
          }, id);
      return Optional.of(order);
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
  }

  private List<Dosa> findDosasByOrderId(long orderId) {
    return jdbcOperations.query(
        "select id, name, created_at from Dosa "
        + "where dosa_order=? order by dosa_order_key",
        (row, rowNum) -> {
          Dosa dosa = new Dosa();
          dosa.setId(row.getLong("id"));
          dosa.setName(row.getString("name"));
          dosa.setCreatedAt(new Date(row.getTimestamp("created_at").getTime()));
          dosa.setIngredients(findIngredientsByDosaId(row.getLong("id")));
          return dosa;
        },
        orderId);
  }

  private List<IngredientRef> findIngredientsByDosaId(long dosaId) {
    return jdbcOperations.query(
        "select ingredient from Ingredient_Ref "
        + "where dosa = ? order by dosa_key",
        (row, rowNum) -> {
          return new IngredientRef(row.getString("ingredient"));
        },
        dosaId);
  }

}