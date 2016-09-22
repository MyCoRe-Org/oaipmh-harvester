package org.mycore.oai.pmh.harvester.jaxb;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadResumptionTokenException;
import org.mycore.oai.pmh.BadVerbException;
import org.mycore.oai.pmh.CannotDisseminateFormatException;
import org.mycore.oai.pmh.DefaultResumptionToken;
import org.mycore.oai.pmh.Description;
import org.mycore.oai.pmh.Granularity;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.IdDoesNotExistException;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.Identify.DeletedRecordPolicy;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.NoMetadataFormatsException;
import org.mycore.oai.pmh.NoRecordsMatchException;
import org.mycore.oai.pmh.NoSetHierarchyException;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.OAIUtils;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.SimpleIdentify;
import org.mycore.oai.pmh.SimpleMetadata;
import org.mycore.oai.pmh.harvester.HarvesterConfig;
import org.openarchives.oai.pmh.AboutType;
import org.openarchives.oai.pmh.DescriptionType;
import org.openarchives.oai.pmh.GetRecordType;
import org.openarchives.oai.pmh.HeaderType;
import org.openarchives.oai.pmh.IdentifyType;
import org.openarchives.oai.pmh.ListIdentifiersType;
import org.openarchives.oai.pmh.ListMetadataFormatsType;
import org.openarchives.oai.pmh.ListRecordsType;
import org.openarchives.oai.pmh.ListSetsType;
import org.openarchives.oai.pmh.MetadataFormatType;
import org.openarchives.oai.pmh.OAIPMHerrorType;
import org.openarchives.oai.pmh.OAIPMHerrorcodeType;
import org.openarchives.oai.pmh.OAIPMHtype;
import org.openarchives.oai.pmh.RecordType;
import org.openarchives.oai.pmh.ResumptionTokenType;
import org.openarchives.oai.pmh.SetType;
import org.openarchives.oai.pmh.StatusType;
import org.w3c.dom.Element;

public class JAXBConverter {

    private static Logger LOGGER = LogManager.getLogger(JAXBConverter.class);

    private HarvesterConfig config;

    public JAXBConverter(HarvesterConfig config) {
        this.config = config;
    }

    protected void handleOAIException(OAIPMHtype oaipmh) throws OAIException {
        List<OAIPMHerrorType> errorList = oaipmh.getError();
        if (errorList.size() <= 0) {
            return;
        }
        OAIPMHerrorType firstError = errorList.get(0);
        OAIPMHerrorcodeType errorCode = firstError.getCode();
        String msg = firstError.getValue();
        if (OAIPMHerrorcodeType.BAD_ARGUMENT.equals(errorCode)) {
            throw new BadArgumentException(msg);
        } else if (OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN.equals(errorCode)) {
            throw new BadResumptionTokenException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.BAD_VERB.equals(errorCode)) {
            throw new BadVerbException(msg);
        } else if (OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT.equals(errorCode)) {
            throw new CannotDisseminateFormatException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.ID_DOES_NOT_EXIST.equals(errorCode)) {
            throw new IdDoesNotExistException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.NO_METADATA_FORMATS.equals(errorCode)) {
            throw new NoMetadataFormatsException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.NO_RECORDS_MATCH.equals(errorCode)) {
            throw new NoRecordsMatchException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.NO_SET_HIERARCHY.equals(errorCode)) {
            throw new NoSetHierarchyException().setMessage(msg);
        }
        throw new RuntimeException("Unknown oai exception occur: " + msg);
    }

    public Identify convertIdentify(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        IdentifyType idType = oaipmh.getIdentify();
        if (idType == null) {
            throw new RuntimeException("Identify is empty");
        }
        // create new identify
        SimpleIdentify id = new SimpleIdentify();
        id.setBaseURL(idType.getBaseURL());
        id.setDeletedRecordPolicy(DeletedRecordPolicy.get(idType.getDeletedRecord().name()));
        id.setEarliestDatestamp(idType.getEarliestDatestamp());
        id.setGranularity(Granularity.valueOf(idType.getGranularity().name()));
        id.setProtocolVersion(idType.getProtocolVersion());
        id.setRepositoryName(idType.getRepositoryName());
        for (String mail : idType.getAdminEmail()) {
            id.getAdminEmailList().add(mail);
        }
        // add descriptions
        id.getDescriptionList().addAll(convertDescriptionList(idType.getDescription()));
        return id;
    }

    public OAIDataList<Set> convertListSets(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListSetsType listSetsType = oaipmh.getListSets();
        if (listSetsType == null) {
            throw new RuntimeException("ListSets is empty");
        }
        // create new list sets
        OAIDataList<Set> setList = new OAIDataList<Set>();
        for (SetType setType : listSetsType.getSet()) {
            Set set = new Set(setType.getSetSpec(), setType.getSetName());
            set.getDescription().addAll(convertDescriptionList(setType.getSetDescription()));
            setList.add(set);
        }
        ResumptionToken rsToken = convertResumptionToken(listSetsType.getResumptionToken());
        setList.setResumptionToken(rsToken);
        return setList;
    }

    public List<MetadataFormat> convertListMetadataFormats(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListMetadataFormatsType listMetadataFormatsType = oaipmh.getListMetadataFormats();
        if (listMetadataFormatsType == null) {
            throw new RuntimeException("ListMetadataFormats is empty");
        }
        // create new list metadata formats
        List<MetadataFormat> metadataFormatList = new ArrayList<MetadataFormat>();
        for (MetadataFormatType mft : listMetadataFormatsType.getMetadataFormat()) {
            MetadataFormat mf = new MetadataFormat(mft.getMetadataPrefix(), mft.getMetadataNamespace(),
                mft.getSchema());
            metadataFormatList.add(mf);
        }
        return metadataFormatList;
    }

    public OAIDataList<Header> convertListIdentifiers(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListIdentifiersType listIdentifiersType = oaipmh.getListIdentifiers();
        if (listIdentifiersType == null) {
            throw new RuntimeException("ListIdentifiers is empty");
        }
        // create new list identifiers
        OAIDataList<Header> headerList = new OAIDataList<Header>();
        for (HeaderType headerType : listIdentifiersType.getHeader()) {
            headerList.add(convertHeader(headerType));
        }
        ResumptionToken rsToken = convertResumptionToken(listIdentifiersType.getResumptionToken());
        headerList.setResumptionToken(rsToken);
        return headerList;
    }

    public OAIDataList<Record> convertListRecords(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListRecordsType listRecordsType = oaipmh.getListRecords();
        if (listRecordsType == null) {
            throw new RuntimeException("ListRecords is empty");
        }
        // create new list identifiers
        OAIDataList<Record> recordList = new OAIDataList<Record>();
        for (RecordType recordType : listRecordsType.getRecord()) {
            recordList.add(convertRecord(recordType));
        }
        ResumptionToken rsToken = convertResumptionToken(listRecordsType.getResumptionToken());
        recordList.setResumptionToken(rsToken);
        return recordList;
    }

    public Record convertGetRecord(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        GetRecordType recordType = oaipmh.getGetRecord();
        if (recordType == null) {
            throw new RuntimeException("GetRecord is empty");
        }
        // create new record
        return convertRecord(recordType.getRecord());
    }

    private Header convertHeader(HeaderType headerType) {
        Header header = new Header(headerType.getIdentifier(), headerType.getDatestamp());
        if (headerType.getStatus() != null && headerType.getStatus().equals(StatusType.DELETED)) {
            header.setDeleted(true);
        }
        for (String spec : headerType.getSetSpec()) {
            header.getSetList().add(new Set(spec));
        }
        return header;
    }

    private Record convertRecord(RecordType recordType) {
        Header header = convertHeader(recordType.getHeader());
        Record record = new Record(header);
        if (recordType.getMetadata() != null) {
            Element domElement = (Element) recordType.getMetadata().getAny();
            if (domElement != null) {
                record.setMetadata(new SimpleMetadata(OAIUtils.domToJDOM(domElement)));
            }
        }
        for (AboutType aboutType : recordType.getAbout()) {
            Element domElement = (Element) aboutType.getAny();
            record.getAboutList().add(OAIUtils.domToJDOM(domElement));
        }
        return record;
    }

    private List<Description> convertDescriptionList(List<DescriptionType> descriptionTypeList) {
        List<Description> descriptionList = new ArrayList<Description>();
        for (DescriptionType descType : descriptionTypeList) {
            Element domElement = (Element) descType.getAny();
            String name = domElement.getLocalName();
            Description description = this.config.createNewDescriptionInstance(name);
            if (description == null) {
                LOGGER.warn("Unable to find matching description for '" + name
                    + "'. Use HarvesterConfig#registerDescription() to add one.");
                continue;
            }
            description.fromXML(OAIUtils.domToJDOM(domElement));
            descriptionList.add(description);
        }
        return descriptionList;
    }

    private ResumptionToken convertResumptionToken(ResumptionTokenType rsTokenType) {
        if (rsTokenType == null) {
            return null;
        }
        DefaultResumptionToken rsToken = new DefaultResumptionToken();
        rsToken.setToken(rsTokenType.getValue());
        rsToken.setCompleteListSize(rsTokenType.getCompleteListSize().intValue());
        rsToken.setCursor(rsTokenType.getCursor().intValue());
        rsToken.setExpirationDate(rsTokenType.getExpirationDate());
        return rsToken;
    }
}
