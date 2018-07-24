package org.mycore.oai.pmh.harvester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.Set;

public class HarvesterTest {

    private static String TEST_URL = "https://zs.thulb.uni-jena.de/oai2";

    private static String TEST_SET = "journal-type:parliamentDocuments";

    @Test
    public void identify() {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        Identify identify = harvester.identify();
        assertEquals("repository name differs", "JPortal",
            identify.getRepositoryName());
        assertEquals("deletion record policy should be 'No'", "Persistent", identify.getDeletedRecordPolicy().name());
    }

    @Test
    public void listSets() throws Exception {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        OAIDataList<Set> setList = harvester.listSets();
        assertFalse("set list should not be empty", setList.isEmpty());
    }

    @Test
    public void listMetadataFormats() {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        List<MetadataFormat> mfList = harvester.listMetadataFormats();
        assertFalse("metadata format list should not be empty", mfList.isEmpty());
    }

    @Test
    public void listHeaders() throws Exception {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        OAIDataList<Header> headerList = harvester.listIdentifiers("oai_dc", null, null, TEST_SET);
        assertFalse("headers should not be empty", headerList.isEmpty());
        ResumptionToken rsToken = headerList.getResumptionToken();
        assertNotNull("there should be an resumption token", rsToken);
        OAIDataList<Header> headerList2 = harvester.listIdentifiers(rsToken.getToken());
        assertFalse("headers should not be empty", headerList2.isEmpty());
    }

    @Test
    public void listRecords() throws Exception {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        OAIDataList<Record> recordList = harvester.listRecords("oai_dc", null, null, TEST_SET);
        assertFalse("records should not be empty", recordList.isEmpty());
        ResumptionToken rsToken = recordList.getResumptionToken();
        assertNotNull("there should be an resumption token", rsToken);
        OAIDataList<Record> recordList2 = harvester.listRecords(rsToken.getToken());
        assertFalse("records should not be empty", recordList2.isEmpty());
    }

    @Test
    public void getRecord() throws Exception {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        Record record = harvester.getRecord("oai:zs.thulb.uni-jena.de:jportal_jparticle_00000001", "oai_dc");
        assertNotNull("the record should exist", record);
    }

}
