package com.bigbox.b2csite.order.service.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bigbox.b2csite.order.dao.OrderDao;
import com.bigbox.b2csite.order.model.domain.OrderSummary;
import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.model.entity.OrderItemEntity;
import com.bigbox.b2csite.order.model.transformer.OrderEntityToOrderSummaryTransformer;

public class OrderServiceImplTest {

	private final static long CUSTOMER_ID = 1;
	private final static Long ORDER_ID = 2L;
	private final static Long ITEM_ID = 3L;
	private final static String ORDER_NUMBER = "54";

	private OrderServiceImpl target = null;

	protected @Mock OrderDao mockOrderDao;
	protected @Mock OrderEntityToOrderSummaryTransformer mockTransformer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.target = new OrderServiceImpl();
		this.target.setOrderDao(mockOrderDao);
		this.target.setTransformer(mockTransformer);
	}

	@Test
	public void test_getOrderSummary_success() throws Exception {

		// Setup
		OrderEntity orderEntityFixture = new OrderEntity();
		orderEntityFixture.setCustomerId(CUSTOMER_ID);
		orderEntityFixture.setId(ORDER_ID);
		orderEntityFixture.setOrderNumber(ORDER_NUMBER);
		orderEntityFixture.setOrderItemList(new LinkedList<OrderItemEntity>());

		OrderItemEntity orderItemFixture = new OrderItemEntity();
		orderItemFixture.setId(ITEM_ID);
		orderItemFixture.setOwningOrder(orderEntityFixture);
		orderItemFixture.setSellingPrice(new BigDecimal("1.5"));
		orderItemFixture.setQuantity(2);

		orderEntityFixture.getOrderItemList().add(orderItemFixture);

		List<OrderEntity> orderEntityListFixture = new LinkedList<>();
		orderEntityListFixture.add(orderEntityFixture);

		Mockito.when(mockOrderDao.findOrdersByCustomer(CUSTOMER_ID)).thenReturn(orderEntityListFixture);
		Mockito.when(mockTransformer.transform(orderEntityFixture)).thenCallRealMethod();

		// Execution
		List<OrderSummary> result = target.getOrderSummary(CUSTOMER_ID);

		// Verification
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(2, result.get(0).getItemCount());
	}

	@Test
	@Ignore
	public void test_openNewOrder_successfullyRetriesDataInsert() throws Exception {

		// Setup

		// Execution

		// Verification
	}

	@Test
	@Ignore
	public void test_openNewOrder_failedDataInsert() throws Exception {

		// Setup

		// Execution

		// Verification
	}
}
