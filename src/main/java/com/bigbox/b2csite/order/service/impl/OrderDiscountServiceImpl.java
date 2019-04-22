package com.bigbox.b2csite.order.service.impl;

import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.service.OrderDiscountService;

public class OrderDiscountServiceImpl implements OrderDiscountService {

	@Override
	public OrderEntity setDiscount(OrderEntity orderEntity) {
		// no business logic, just return the orderEntity back
		return orderEntity;
	}

}
