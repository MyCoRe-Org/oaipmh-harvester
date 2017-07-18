package org.mycore.oai.pmh.harvester;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HarvesterUtilTest {

    private static String TEST_URL = "https://edoc.hu-berlin.de/oai/request";

    @Test
    public void listRecords() throws Exception {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        assertTrue("there should be more than 500 records",
            HarvesterUtil.streamRecords(harvester, "oai_dc", null, null, "ddc:310").count() > 300);
        assertTrue("there should be more than 500 sets", HarvesterUtil.streamSets(harvester).count() > 500);
    }

}
