package org.mycore.oai.pmh.harvester;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HarvesterUtilTest {

    private static String TEST_URL = "https://zs.thulb.uni-jena.de/oai2";

    @Test
    public void streamRecords() {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        assertTrue("there should be six or more records",
            HarvesterUtil.streamRecords(harvester, "oai_dc", null, null, "jportal_jpjournal_00001217").count() >= 6);
    }

    @Test
    public void streamSets() {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        assertTrue("there should be more than 500 sets", HarvesterUtil.streamSets(harvester).count() > 500);
    }

}
