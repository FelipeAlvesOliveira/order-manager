package com.example.ordermanager.service;

import com.example.ordermanager.entity.Item;
import com.example.ordermanager.entity.Order;
import com.example.ordermanager.entity.OrderDTO;
import com.example.ordermanager.entity.OrderStatus;
import com.example.ordermanager.entity.User;
import com.example.ordermanager.exception.ChangeNotAllowed;
import com.example.ordermanager.exception.EntityNotFound;
import com.example.ordermanager.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;

    private static final Long FINISHED_ORDER_ID = 1L;
    private static final Long WAITING_ORDER_ID = 2L;
    private static final Long WAITING_ORDER_ID_TO_UPDATE = 3L;
    private static final Long NOT_FOUND_ORDER_ID = 5L;
    private static final Long ITEM_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Date CREATION_DATE = new Date();
    private static final Long QUANTITY = 5L;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        // Prepare find scenario
        Order orderFinished = createOrder(FINISHED_ORDER_ID, OrderStatus.FINISHED.value);
        Mockito.when(orderRepository.findById(FINISHED_ORDER_ID))
                .thenReturn(Optional.of(orderFinished));

        // Prepare find scenario
        Order orderWaiting = createOrder(WAITING_ORDER_ID, OrderStatus.WAITING.value);
        Mockito.when(orderRepository.findById(WAITING_ORDER_ID))
                .thenReturn(Optional.of(orderWaiting));

        // Prepare create scenario
        //TODO FELIPE: FIX THIS UNIT TEST, TRY TO DO IT WIHOUT OVERRIDE EQUALS
        Order orderToSave = createOrder(null, OrderStatus.WAITING.value);
        Mockito.doNothing().when(orderToSave).setCreationDate(any());
        Mockito.when(orderRepository.save(orderToSave))
                .thenReturn(orderWaiting);

        // Prepare update scenario
        Order orderWaitingToUpdate = createOrder(WAITING_ORDER_ID_TO_UPDATE, OrderStatus.WAITING.value);
        Mockito.when(orderRepository.findById(WAITING_ORDER_ID_TO_UPDATE))
                .thenReturn(Optional.of(orderWaitingToUpdate));
        orderWaitingToUpdate.setItem(new Item(2l, null));
        Mockito.when(orderRepository.save(orderWaitingToUpdate))
                .thenReturn(orderWaitingToUpdate);

        // Prepare findAll scenario
        List<Order> orders = new ArrayList<>();
        orders.add(orderFinished);
        orders.add(orderWaiting);
        orders.add(orderWaitingToUpdate);
        Mockito.when(orderRepository.findAll())
                .thenReturn(orders);
    }

    //TODO FELIPE: FIZ UNIT TESTS
    /*@Test
    public void getOrdersByUserTest() {
        List<OrderDTO> result = orderService.getOrders();
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void getOrderByIdTest() {
        try {
            OrderDTO result = orderService.getOrderById(WAITING_ORDER_ID);
            Order orderWaiting = createOrder(WAITING_ORDER_ID, OrderStatus.WAITING.value);
            OrderDTO orderDTO = Order.toDTO(orderWaiting);
            Assertions.assertThat(result.getId()).isEqualTo(orderDTO.getId());
            Assertions.assertThat(result.getUserId()).isEqualTo(orderDTO.getUserId());
            Assertions.assertThat(result.getItemId()).isEqualTo(orderDTO.getItemId());
            Assertions.assertThat(result.getQuantity()).isEqualTo(orderDTO.getQuantity());
        } catch (EntityNotFound e) {
            Assertions.fail(String.format("%s should not be thrown", e.getClass().getName()));
        }
    }

    @Test
    public void createOrderTest() {
        OrderDTO dto = createOrderDTO(null);
        OrderDTO result = orderService.createOrder(dto);
        Assertions.assertThat(result.getId()).isEqualTo(WAITING_ORDER_ID);
    }

    @Test
    public void updateOrderTest() {
        OrderDTO dto = createOrderDTO(WAITING_ORDER_ID_TO_UPDATE);
        dto.setItemId(2l);
        try {
            OrderDTO result = orderService.updateOrder(WAITING_ORDER_ID_TO_UPDATE, dto);
            Assertions.assertThat(result.getItemId()).isEqualTo(2l);
        } catch (ChangeNotAllowed | EntityNotFound e) {
            Assertions.fail(String.format("%s should not be thrown", e.getClass().getName()));
        }
    }

    @Test
    public void updateOrderEntityNotFoundTest() {
        OrderDTO dto = createOrderDTO(NOT_FOUND_ORDER_ID);
        Assertions.assertThatThrownBy(
                () -> orderService.updateOrder(NOT_FOUND_ORDER_ID, dto)
        ).isInstanceOf(EntityNotFound.class);
    }

    @Test
    public void updateOrderChangeNotAllowedTest() {
        OrderDTO dto = createOrderDTO(FINISHED_ORDER_ID);
        Assertions.assertThatThrownBy(
                () -> orderService.updateOrder(FINISHED_ORDER_ID, dto)
        ).isInstanceOf(ChangeNotAllowed.class);
    }

    @Test
    public void deleteOrderTest() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> orderService.deleteOrder(WAITING_ORDER_ID));
    }

    @Test
    public void deleteOrderEntityNotFoundTest() {
        Assertions.assertThatThrownBy(
                () -> orderService.deleteOrder(NOT_FOUND_ORDER_ID)
        ).isInstanceOf(EntityNotFound.class);
    }

    @Test
    public void deleteOrderChangeNotAllowedTest() {
        Assertions.assertThatThrownBy(
                () -> orderService.deleteOrder(FINISHED_ORDER_ID)
        ).isInstanceOf(ChangeNotAllowed.class);
    }*/

    private Order createOrder(Long id, String status) {
        Order order = new Order();
        order.setId(id);
        order.setCreationDate(CREATION_DATE);
        order.setItem(new Item(ITEM_ID, null));
        order.setUser(new User(USER_ID, null, null));
        order.setQuantity(QUANTITY);
        order.setStatus(status);
        return order;
    }

    private OrderDTO createOrderDTO(Long id) {
        OrderDTO order = new OrderDTO();
        order.setId(id);
        order.setCreationDate(CREATION_DATE);
        order.setItemId(ITEM_ID);
        order.setUserId(USER_ID);
        order.setQuantity(QUANTITY);
        return order;
    }
}
