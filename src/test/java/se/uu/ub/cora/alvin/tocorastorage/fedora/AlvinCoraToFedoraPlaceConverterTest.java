/*
 * Copyright 2019 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.alvin.tocorastorage.fedora;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import resources.ResourceReader;
import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class AlvinCoraToFedoraPlaceConverterTest {
	@Test
	public void testConvertToFedoraXML() throws Exception {

		HttpHandlerFactorySpy httpHandlerFactory = new HttpHandlerFactorySpy();
		httpHandlerFactory.responseText = ResourceReader.readResourceAsString("place/679.xml");

		String fedoraURL = "someFedoraURL";
		AlvinCoraToFedoraConverter converter = AlvinCoraToFedoraPlaceConverter
				.usingHttpHandlerFactoryAndFedoraUrl(httpHandlerFactory, fedoraURL);
		DataGroup record = createPlace679DataGroup();

		String xml = converter.toXML(record);
		assertEquals(httpHandlerFactory.factoredHttpHandlers.size(), 1);
		assertEquals(httpHandlerFactory.urls.get(0),
				fedoraURL + "objects/alvin-place:679/datastreams/METADATA/content");

		HttpHandlerSpy httpHandler = httpHandlerFactory.factoredHttpHandlers.get(0);
		assertEquals(httpHandler.requestMetod, "GET");

		assertEquals(xml, ResourceReader.readResourceAsString("place/expectedUpdated679.xml"));

	}

	private DataGroup createPlace679DataGroup() {
		DataGroup record = DataGroup.withNameInData("authority");
		record.addAttributeByIdWithValue("type", "place");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		record.addChild(recordInfo);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "alvin-place:679"));

		DataGroup authorizedNameGroup = DataGroup.withNameInData("name");
		record.addChild(authorizedNameGroup);
		authorizedNameGroup.addAttributeByIdWithValue("type", "authorized");

		DataGroup defaultNameGroup = DataGroup.withNameInData("namePart");
		authorizedNameGroup.addChild(defaultNameGroup);
		defaultNameGroup.addAttributeByIdWithValue("type", "defaultName");

		DataAtomic defaultNameValue = DataAtomic.withNameInDataAndValue("value",
				"CORA_Uppsala_CORA");
		defaultNameGroup.addChild(defaultNameValue);

		return record;
	}

}
