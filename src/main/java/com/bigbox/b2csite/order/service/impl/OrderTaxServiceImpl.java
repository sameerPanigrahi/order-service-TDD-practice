package com.bigbox.b2csite.order.service.impl;

import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.service.OrderTaxService;

public class OrderTaxServiceImpl implements OrderTaxService {

	@Override
	public OrderEntity deductTax(OrderEntity orderEntity) {
		// no business logic, just return the orderEntity back
		return orderEntity;
	}

}
