package com.jupiter.tools.spring.test.mongo.internal.expect.match.simple;

import java.math.BigDecimal;

import com.jupiter.tools.spring.test.mongo.internal.expect.match.MatchData;

/**
 * Created on 01.04.2019.
 *
 * @author Korovin Anatoliy
 */
public class MatchDouble implements MatchData {
	
	@Override
	public boolean match(Object original, Object expected) {
		Number originalNumber = new BigDecimal(original.toString());
		Number expectedNumber = new BigDecimal(expected.toString());
		return originalNumber.equals(expectedNumber);
	}
}
