package com.example.ordermanager.service;

import com.example.ordermanager.entity.Order;
import com.example.ordermanager.entity.OrderDTO;
import com.example.ordermanager.entity.OrderStatus;
import com.example.ordermanager.exception.ChangeNotAllowed;
import com.example.ordermanager.exception.EntityNotFound;
import com.example.ordermanager.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProcessorService orderProcessorService;

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    public List<OrderDTO> getOrders() {
        //TODO FELIPE: ADD PAGINATION
        return orderRepository.findAll()
                .stream()
                .map(Order::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) throws EntityNotFound {
        Order order = getEntityByIdIfExists(id);
        return Order.toDTO(order);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = Order.fromDTO(orderDTO);
        order.setCreationDate(new Date());
        order.setStatus(OrderStatus.WAITING.value);
        order = orderRepository.save(order);
        // log order created
        logger.info(String.format("Order created to item %s and quantity %s",
                orderDTO.getItemId(), orderDTO.getQuantity()));
        // run async process to deliver orders
        orderProcessorService.processOrdersFromItem(orderDTO.getItemId());
        return Order.toDTO(order);
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO)
            throws ChangeNotAllowed, EntityNotFound {
        Order order = getEntityByIdIfExists(id);
        checkCanChangeOrder(order);
        //TODO FELIPE: CHECK MORE VALIDATIONS ABOUT THE ORDER DTO, EXAMPLE, QTD SHOULD BE > 0
        Order.mergeWithDTO(order, orderDTO);
        order = orderRepository.save(order);
        // log order updated
        logger.info(String.format("Order updated to item %s and quantity %s",
                orderDTO.getItemId(), orderDTO.getQuantity()));
        // run async process to deliver orders
        orderProcessorService.processOrdersFromItem(order.getItem().getId());
        return Order.toDTO(order);
    }

    public void deleteOrder(Long id) throws EntityNotFound, ChangeNotAllowed {
        Order order = getEntityByIdIfExists(id);
        checkCanChangeOrder(order);
        orderRepository.deleteById(id);
    }

    private Order getEntityByIdIfExists(Long id) throws EntityNotFound {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new EntityNotFound(String.format("Order with id %s not found", id));
        }
    }

    private void checkCanChangeOrder(Order order) throws ChangeNotAllowed {
        if (OrderStatus.FINISHED.equals(OrderStatus.valueOf(order.getStatus()))) {
            throw new ChangeNotAllowed(String.format("Order with id %s is already finished", order.getId()));
        }
    }
}
