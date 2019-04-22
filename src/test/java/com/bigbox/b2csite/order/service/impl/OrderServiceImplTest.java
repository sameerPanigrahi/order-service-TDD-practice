package com.bigbox.b2csite.order.service.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bigbox.b2csite.common.DataAccessException;
import com.bigbox.b2csite.common.ServiceException;
import com.bigbox.b2csite.order.dao.OrderDao;
import com.bigbox.b2csite.order.model.domain.OrderSummary;
import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.model.entity.OrderItemEntity;
import com.bigbox.b2csite.order.model.transformer.OrderEntityToOrderSummaryTransformer;
import com.bigbox.b2csite.order.service.OrderTaxService;

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
	public void test_openNewOrder_successfullyRetriesDataInsert() throws Exception {

		// Sameer's code
		// Setup
		// mockOrderDao is initiated
		// and set in target OrderServiceImpl in the @Before method

		Mockito
				.when(mockOrderDao.insert(Matchers.any(OrderEntity.class)))
				.thenThrow(new DataAccessException("First Execution"))
				.thenReturn(1);

		// Execution
		target.openNewOrder(CUSTOMER_ID);

		// Verification
		Mockito.verify(mockOrderDao, Mockito.times(2)).insert(Matchers.any(OrderEntity.class));

	}

	@Test(expected = ServiceException.class)
	// @Test takes 2 optional parameters -
	// 'expected' exception object and 'timeout' parameter
	public void test_openNewOrder_failedDataInsert() throws Exception {

		// Sameer's code
		// Setup
		Mockito.when(mockOrderDao.insert(Matchers.any(OrderEntity.class)))
				.thenThrow(new DataAccessException("First Try"))
				.thenThrow(new DataAccessException("Second Try"));

		// Execution
		target.openNewOrder(CUSTOMER_ID);

		// Verification
		Mockito.verify(mockOrderDao, Mockito.times(2)).insert(Matchers.any(OrderEntity.class));
	}

	@Test
	public void test_inorder_Discount_Tax() throws ServiceException, DataAccessException {

		// Sameer's code
		// Setup
		OrderDiscountServiceImpl mockDiscount = Mockito.mock(OrderDiscountServiceImpl.class);
		Mockito
				.when(mockDiscount.setDiscount(Matchers.any(OrderEntity.class)))
				.thenCallRealMethod();

		OrderTaxService mockTax = Mockito.mock(OrderTaxServiceImpl.class);
		Mockito
				.when(mockTax.deductTax(Matchers.any(OrderEntity.class)))
				.thenCallRealMethod();

		Mockito
				.when(mockOrderDao.insert(Matchers.any(OrderEntity.class)))
				.thenReturn(1);

		target.setOrderDiscountService(mockDiscount);
		target.setOrderTaxService(mockTax);

		// Execution
		target.openNewOrder(CUSTOMER_ID);

		// Verification of inorder sequence of Discount and Tax
		InOrder inorder = Mockito.inOrder(mockDiscount, mockTax);
		// first in sequence
		inorder.verify(mockDiscount).setDiscount(Matchers.any(OrderEntity.class));
		// second in sequence
		inorder.verify(mockTax).deductTax(Matchers.any(OrderEntity.class));
	}

	@Test
	public void test_InorderDiscountTax_OnSameEntity() throws ServiceException, DataAccessException {

		// Sameer's code
		// Setup
		OrderDiscountServiceImpl mockDiscount = Mockito.mock(OrderDiscountServiceImpl.class);
		Mockito
				.when(mockDiscount.setDiscount(Matchers.any(OrderEntity.class)))
				.thenCallRealMethod();

		OrderTaxService mockTax = Mockito.mock(OrderTaxServiceImpl.class);
		Mockito
				.when(mockTax.deductTax(Matchers.any(OrderEntity.class)))
				.thenCallRealMethod();

		Mockito
				.when(mockOrderDao.insert(Matchers.any(OrderEntity.class)))
				.thenReturn(1);

		target.setOrderDiscountService(mockDiscount);
		target.setOrderTaxService(mockTax);

		// Execution
		target.openNewOrder(CUSTOMER_ID);

		// Verification of inorder for the same Object Entity using Argument capture
		ArgumentCaptor<OrderEntity> orderEntityCaptor = ArgumentCaptor.forClass(OrderEntity.class);
		InOrder inorder = Mockito.inOrder(mockDiscount, mockTax);
		// first in sequence
		inorder.verify(mockDiscount).setDiscount(orderEntityCaptor.capture());
		OrderEntity o1 = orderEntityCaptor.getValue();
		// second in sequence
		inorder.verify(mockTax).deductTax(orderEntityCaptor.capture());
		OrderEntity o2 = orderEntityCaptor.getValue();

		Assert.assertSame(o1, o2);

	}

}
