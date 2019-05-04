package com.bigbox.b2csite.order.integration;

import com.bigbox.b2csite.order.model.message.OrderMessage;

public class WarehouseManagementService {

	public static void sendOrder(OrderMessage orderMessage) throws WMSUnavailableException {
		throw new WMSUnavailableException("WMS is currently down for unknown reason");
	}

	public static int confirmShipment(OrderMessage orderMessage) throws WMSUnavailableException {
		voidConfirmShipmentInternal(orderMessage);
		return confirmShipmentInternal(orderMessage);
	}

	private static void voidConfirmShipmentInternal(OrderMessage orderMessage) {
		return;

	}

	private static int confirmShipmentInternal(OrderMessage orderMessage) throws WMSUnavailableException {
		return 1;
	}
}
