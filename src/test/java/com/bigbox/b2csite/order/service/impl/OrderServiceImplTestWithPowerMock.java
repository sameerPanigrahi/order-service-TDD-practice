package com.bigbox.b2csite.order.service.impl;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.bigbox.b2csite.order.dao.OrderDao;
import com.bigbox.b2csite.order.integration.WMSUnavailableException;
import com.bigbox.b2csite.order.integration.WarehouseManagementService;
import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.model.entity.OrderItemEntity;
import com.bigbox.b2csite.order.model.message.OrderMessage;
import com.bigbox.b2csite.order.model.transformer.OrderEntityToOrderSummaryTransformer;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { WarehouseManagementService.class, OrderServiceImpl.class })
public class OrderServiceImplTestWithPowerMock {

	private final static long CUSTOMER_ID = 1;
	private final static long ORDER_ID = 2L;
	private final static String ORDER_NUMBER = "1234";

	private OrderServiceImpl target = null;

	protected @Mock OrderDao mockOrderDao;
	protected @Mock OrderEntityToOrderSummaryTransformer mockTransformer;

	protected OrderEntity orderFixture = null;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.target = new OrderServiceImpl();
		this.target.setOrderDao(mockOrderDao);
		this.target.setTransformer(mockTransformer);

		OrderItemEntity oiFixture1 = new OrderItemEntity();
		oiFixture1.setSku("SKU1");
		oiFixture1.setQuantity(1);

		OrderItemEntity oiFixture2 = new OrderItemEntity();
		oiFixture2.setSku("SKU2");
		oiFixture2.setQuantity(2);

		orderFixture = new OrderEntity();
		orderFixture.setOrderNumber(ORDER_NUMBER);
		orderFixture.setOrderItemList(new LinkedList<OrderItemEntity>());
		orderFixture.getOrderItemList().add(oiFixture1);
		orderFixture.getOrderItemList().add(oiFixture2);
	}

	@Test
	@Ignore
	public void test_WarehouseSendOrder_success() throws Exception {

		// Setup
		Mockito.when(mockOrderDao.findById(ORDER_ID))
				.thenReturn(this.orderFixture);

		// Add static mocking here
		PowerMockito.spy(WarehouseManagementService.class);
		/*
		 * since this static method returns void, we cannot use then(). A void method
		 * can either execute returning nothing or it can throw an exception. The
		 * exception can be tested to ensure if the method behaves correctly in
		 * execption scenarios This testing is done by first setting the expectation
		 * (via doThrow() ) and then setting the when() clause.
		 */
		PowerMockito.doThrow(new WMSUnavailableException("mock Exception"))
				.when(WarehouseManagementService.class, "sendOrder", Matchers.any(OrderMessage.class));

		// Execution
		// surrounding with a try catch block since this method will throw an exception
		try {
			target.completeOrder(ORDER_ID);
		} catch (Exception e) {
			// do nothing
		}

		// Verification
		Mockito.verify(mockOrderDao).findById(ORDER_ID);

		/*
		 * Note: The verifyStatic method must be called right before any static method
		 * verification for PowerMockito to know that the successive method invocation
		 * is what needs to be verified.
		 */
		PowerMockito.verifyStatic();
		WarehouseManagementService.sendOrder(Matchers.any(OrderMessage.class));
	}

	@Test
	public void test_WarehouseConfirmShipment_success() throws Exception {

		// Setup
		Mockito.when(mockOrderDao.findById(ORDER_ID))
				.thenReturn(orderFixture);

		PowerMockito.mockStatic(WarehouseManagementService.class);
		// PowerMockito.spy(WarehouseManagementService.class);

		// constructor injection
		// OrderMessage mockOrderMessage = PowerMockito.mock(OrderMessage.class);
		OrderMessage mockOrderMessage = PowerMockito.spy(new OrderMessage());
		mockOrderMessage.setOrderNumber("Sagar");
		PowerMockito.whenNew(OrderMessage.class).withNoArguments().thenReturn(mockOrderMessage);

		// set when..then for public-static method and capture the arguement when called
		ArgumentCaptor<OrderMessage> orderMessageArgCapt = ArgumentCaptor.forClass(OrderMessage.class);
		PowerMockito.when(WarehouseManagementService.confirmShipment(orderMessageArgCapt.capture()))
				.thenCallRealMethod();

		// Execution
		// surrounding with a try catch block since this method will throw an exception
		try {
			target.completeOrder(ORDER_ID);
		} catch (Exception e) {
			// do nothing
		}

		// Verification

		// 1.Verify static call
		PowerMockito.verifyStatic();
		WarehouseManagementService.confirmShipment(mockOrderMessage);

		// 2. Verify static call invoked with the same mock parameters injected
		ArgumentCaptor<OrderMessage> orderMessageArgCapt1 = ArgumentCaptor.forClass(OrderMessage.class);
		PowerMockito.verifyStatic();
		WarehouseManagementService.confirmShipment(orderMessageArgCapt1.capture());
		Assert.assertSame(orderMessageArgCapt.getValue(), orderMessageArgCapt1.getValue());
		Assert.assertSame(orderMessageArgCapt1.getValue(), mockOrderMessage);
		/*
		 * This will fail because although you create the mock object mockOrderMessage
		 * for constructor injection the production code changes this value to
		 * orderMessage.setOrderNumber(orderEntity.getOrderNumber()) later.
		 */
		// Assert.assertSame(orderMessageArgCapt.getValue().getOrderNumber(), "Sagar");

	}

	@Test
	public void test_callToPrivateStatic_success() throws Exception {
		// Setup
		Mockito.when(mockOrderDao.findById(ORDER_ID))
				.thenReturn(orderFixture);

		PowerMockito.mockStatic(WarehouseManagementService.class);
		// PowerMockito.spy(WarehouseManagementService.class);

		// constructor injection
		// OrderMessage mockOrderMessage = PowerMockito.mock(OrderMessage.class);
		OrderMessage mockOrderMessage = PowerMockito.spy(new OrderMessage());
		mockOrderMessage.setOrderNumber("Sagar");
		PowerMockito.whenNew(OrderMessage.class).withNoArguments().thenReturn(mockOrderMessage);

		// set when-then for private-static method
//		Method method = WarehouseManagementService.class.getDeclaredMethod("confirmShipmentInternal",
//				OrderMessage.class);
//		PowerMockito.doCallRealMethod().when(WarehouseManagementService.class, method).withArguments(mockOrderMessage);

//		PowerMockito.when(WarehouseManagementService.class, method).withArguments(mockOrderMessage)
//				.thenCallRealMethod();

		PowerMockito.when(WarehouseManagementService.confirmShipment(Matchers.any(OrderMessage.class)))
				.thenCallRealMethod();
		PowerMockito.when(WarehouseManagementService.class, "confirmShipmentInternal", mockOrderMessage)
				.thenCallRealMethod();
		PowerMockito.doCallRealMethod().when(WarehouseManagementService.class, "voidConfirmShipmentInternal",
				mockOrderMessage);

		// Execution
		// surrounding with a try catch block since this method will throw an exception
		try {
			target.completeOrder(ORDER_ID);
		} catch (Exception e) {
			// do nothing
		}

		// Verification
		// verify call to private static method
		PowerMockito.verifyPrivate(WarehouseManagementService.class).invoke("confirmShipmentInternal",
				mockOrderMessage);

		PowerMockito.verifyPrivate(WarehouseManagementService.class).invoke("voidConfirmShipmentInternal",
				mockOrderMessage);

	}
}
