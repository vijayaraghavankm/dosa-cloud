package dosas;
 import java.util.Optional;
 import dosas.DosaOrder;
 public interface OrderRepository {
  DosaOrder save(DosaOrder order);
 }