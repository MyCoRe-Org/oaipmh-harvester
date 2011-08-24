package org.mycore.oai.pmh.harvester;

import java.util.List;

import org.junit.Test;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.Set;

public class HarvesterTest {

    private static String DP_URL = "http://mcrsrv1.thulb.uni-jena.de:8080/oai";

    @Test
    public void identify() throws Exception {
//        Harvester harvester = HarvesterBuilder.createNewInstance(DP_URL);
//        harvester.identify();
    }

    @Test
    public void listSets() throws Exception {
//        Harvester harvester = HarvesterBuilder.createNewInstance(DP_URL);
//        OAIDataList<Set> setList = harvester.listSets();
//        setList.getResumptionToken();
    }

    @Test
    public void listMetadataFormats() throws Exception {
//        Harvester harvester = HarvesterBuilder.createNewInstance(DP_URL);
//        List<MetadataFormat> mfList = harvester.listMetadataFormats();
//        mfList.toString();
    }

    @Test
    public void listHeaders() throws Exception {
//        Harvester harvester = HarvesterBuilder.createNewInstance(DP_URL);
//        OAIDataList<Header> headerList = harvester.listIdentifiers("mets", null, null, "image");
//        // next request with rs token
//        ResumptionToken rsToken = headerList.getResumptionToken();
//        OAIDataList<Header> headerList2 = harvester.listIdentifiers(rsToken.getToken());
//        headerList2.toString();
    }

    @Test
    public void listRecords() throws Exception {
//        Harvester harvester = HarvesterBuilder.createNewInstance(DP_URL);
//        OAIDataList<Record> recordList = harvester.listRecords("mets", null, null, "image");
//        // next request with rs token
//        ResumptionToken rsToken = recordList.getResumptionToken();
//        OAIDataList<Record> recordList2 = harvester.listRecords(rsToken.getToken());
//        recordList2.toString();
    }

    @Test
    public void getRecord() throws Exception {
//        Harvester harvester = HarvesterBuilder.createNewInstance(DP_URL);
//        Record record = harvester.getRecord("oai:lidoindex.de:DE-Mb112/lido-obj12000051", "mets");
//        record.toString();
    }

}
