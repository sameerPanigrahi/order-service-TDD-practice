package com.bigbox.b2csite.order.service.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bigbox.b2csite.order.dao.OrderDao;
import com.bigbox.b2csite.order.model.transformer.OrderEntityToOrderSummaryTransformer;

public class OrderServiceImplTest {

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
	@Ignore
	public void test_getOrderSummary_success() throws Exception {

		// Setup

		// Execution

		// Verification
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
