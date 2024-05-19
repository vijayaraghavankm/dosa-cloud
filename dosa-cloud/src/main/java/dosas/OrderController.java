 package dosas;
  import javax.validation.Valid;
  import org.springframework.validation.Errors;
  import org.springframework.stereotype.Controller;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.SessionAttributes;
  import org.springframework.web.bind.support.SessionStatus;
  import lombok.extern.slf4j.Slf4j;
  import dosas.DosaOrder;
  @Slf4j
  @Controller
  @RequestMapping("/orders")
  @SessionAttributes("dosaOrder")
  public class OrderController {
   @GetMapping("/current")
   public String orderForm() {
     return "orderForm";
   }  
   @PostMapping
   public String processOrder(@Valid DosaOrder order, Errors errors, 
  SessionStatus sessionStatus) {
    if (errors.hasErrors()) {
      return "orderForm";
    }
    log.info("Order submitted: {}", order);
    sessionStatus.setComplete();
    return "redirect:/";
   }
   
 }