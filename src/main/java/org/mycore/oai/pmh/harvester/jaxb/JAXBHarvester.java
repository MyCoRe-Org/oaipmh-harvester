package org.mycore.oai.pmh.harvester.jaxb;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadResumptionTokenException;
import org.mycore.oai.pmh.CannotDisseminateFormatException;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.IdDoesNotExistException;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.NoMetadataFormatsException;
import org.mycore.oai.pmh.NoRecordsMatchException;
import org.mycore.oai.pmh.NoSetHierarchyException;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.harvester.DataProviderConnection;
import org.mycore.oai.pmh.harvester.Harvester;
import org.mycore.oai.pmh.harvester.HarvesterConfig;
import org.openarchives.oai.pmh.OAIPMHtype;

/**
 * JAXB implementation of a {@link Harvester}.
 * 
 * @author Matthias Eichner
 */
public class JAXBHarvester implements Harvester {

    private static Logger LOGGER = Logger.getLogger(JAXBHarvester.class);

    private String baseURL;

    private HarvesterConfig config;

    private JAXBContext jaxbContext;

    public JAXBHarvester(String baseURL, HarvesterConfig config) throws JAXBException {
        this.baseURL = baseURL;
        this.config = config;
        this.jaxbContext = JAXBContext.newInstance(OAIPMHtype.class);
    }

    @SuppressWarnings("unchecked")
    protected OAIPMHtype unmarshall(InputStream is) {
        try {
            Unmarshaller um = jaxbContext.createUnmarshaller();
            return ((JAXBElement<OAIPMHtype>) um.unmarshal(is)).getValue();
        } catch (JAXBException jaxbExc) {
            LOGGER.error("while unmarshalling inputstream", jaxbExc);
            throw new RuntimeException("TODO: use better rt exception");
        }
    }

    @Override
    public Identify identify() throws BadArgumentException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.identify());
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertIdentify(oaipmh);
        } catch (BadArgumentException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Set> listSets() throws BadArgumentException, NoSetHierarchyException {
        try {
            return this.listSets(null);
        } catch (BadResumptionTokenException exc) {
            throw new RuntimeException("Unexcpected bad resumption token exception occur", exc);
        }
    }

    @Override
    public OAIDataList<Set> listSets(String resumptionToken) throws BadArgumentException, NoSetHierarchyException,
            BadResumptionTokenException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listSets(resumptionToken));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListSets(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (BadResumptionTokenException e) {
            throw e;
        } catch (NoSetHierarchyException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public List<MetadataFormat> listMetadataFormats() throws BadArgumentException {
        try {
            return this.listMetadataFormats(null);
        } catch (IdDoesNotExistException e) {
            // TODO: use java 7 catch
            throw new RuntimeException("Unexcpected exception occur", e);
        } catch (NoMetadataFormatsException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public List<MetadataFormat> listMetadataFormats(String identifier) throws BadArgumentException, IdDoesNotExistException,
            NoMetadataFormatsException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listMetadataFormats(identifier));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListMetadataFormats(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (IdDoesNotExistException e) {
            throw e;
        } catch (NoMetadataFormatsException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Header> listIdentifiers(String metadataPrefix, String from, String until, String setSpec)
            throws BadArgumentException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listIdentifiers(metadataPrefix, from, until, setSpec));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListIdentifiers(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (CannotDisseminateFormatException e) {
            throw e;
        } catch (NoRecordsMatchException e) {
            throw e;
        } catch (NoSetHierarchyException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Header> listIdentifiers(String resumptionToken) throws BadArgumentException, BadResumptionTokenException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listIdentifiers(resumptionToken));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListIdentifiers(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (BadResumptionTokenException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Record> listRecords(String metadataPrefix, String from, String until, String setSpec) throws BadArgumentException,
            CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listRecords(metadataPrefix, from, until, setSpec));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListRecords(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (CannotDisseminateFormatException e) {
            throw e;
        } catch (NoRecordsMatchException e) {
            throw e;
        } catch (NoSetHierarchyException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Record> listRecords(String resumptionToken) throws BadArgumentException, BadResumptionTokenException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listRecords(resumptionToken));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListRecords(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (BadResumptionTokenException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

    @Override
    public Record getRecord(String identifier, String metadataPrefix) throws BadArgumentException, CannotDisseminateFormatException,
            IdDoesNotExistException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.getRecord(identifier, metadataPrefix));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertGetRecord(oaipmh);
        } catch (BadArgumentException e) {
            // TODO: use java 7 catch
            throw e;
        } catch (CannotDisseminateFormatException e) {
            throw e;
        } catch (IdDoesNotExistException e) {
            throw e;
        } catch (OAIException e) {
            throw new RuntimeException("Unexcpected exception occur", e);
        }
    }

}
