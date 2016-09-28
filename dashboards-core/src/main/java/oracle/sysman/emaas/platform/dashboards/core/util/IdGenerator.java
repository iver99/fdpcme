/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author wenjzhu
 */
public class IdGenerator
{
	private static final BigInteger tileIdOffset = BigInteger.valueOf(100L);

	public static BigInteger getDashboardId(UUID uuid)
	{
		UUID id = uuid == null ? UUID.randomUUID() : uuid;
		return IdGenerator.toBigInteger(id);
	}

	public static String getTileId(UUID uuid, long index)
	{
		UUID id = uuid == null ? UUID.randomUUID() : uuid;
		BigInteger idnum = IdGenerator.toBigInteger(id);
		idnum = idnum.multiply(tileIdOffset);
		idnum = idnum.add(BigInteger.valueOf(index));
		return idnum.toString();
	}

	private static BigInteger toBigInteger(UUID randomUUID)
	{
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(randomUUID.getMostSignificantBits());
		bb.putLong(randomUUID.getLeastSignificantBits());
		return new BigInteger(1, bb.array());
	}
}
