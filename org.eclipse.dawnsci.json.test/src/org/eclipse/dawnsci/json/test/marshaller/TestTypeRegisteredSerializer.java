/*-
 * Copyright 2016 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.json.test.marshaller;

import java.io.IOException;

import org.eclipse.dawnsci.json.test.testobject.ITestTypeRegistered;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class TestTypeRegisteredSerializer extends JsonSerializer<ITestTypeRegistered> {

	@Override
	public void serialize(ITestTypeRegistered pos, JsonGenerator gen, SerializerProvider prov) throws IOException, JsonProcessingException {

		try {
			gen.writeString("TestTypeRegisteredSerializer used.");
		} catch (Throwable ne) {
			ne.printStackTrace();
			throw ne;
		}
	}

	@Override
	public void serializeWithType(ITestTypeRegistered pos, JsonGenerator gen, SerializerProvider prov, TypeSerializer typeSer)
			throws IOException, JsonProcessingException {
		serialize(pos, gen, prov);
	}
}
